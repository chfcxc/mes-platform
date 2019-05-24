package cn.emay.eucp.task.multiple.fms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.page.PageSendDto;
import cn.emay.eucp.common.dto.fms.page.PageSendParsingDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateRedisDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBatch;
import cn.emay.eucp.common.moudle.db.system.BaseSectionNumber;
import cn.emay.eucp.common.moudle.db.system.PortableNumber;
import cn.emay.eucp.common.util.RegularCheckUtils;
import cn.emay.eucp.data.service.fms.FmsBatchService;
import cn.emay.eucp.task.multiple.constant.CacheConstant;
import cn.emay.eucp.task.multiple.constant.CommonConstanct;
import cn.emay.eucp.task.multiple.dto.BaseSectionNumberStore;
import cn.emay.eucp.task.multiple.dto.PortableNumberStore;
import cn.emay.eucp.task.multiple.reader.PageSendDTOReader;
import cn.emay.excel.read.ExcelReader;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.ConcurrentPeriodTask;
import cn.emay.util.ZipUtil;

/**
 * @项目名称：eucp-fms-business-service 
 * @类描述：   页面发送文件解析任务
 * @创建人：dinghaijiao  
 * @创建时间：2019年5月8日 上午9:51:10  
 * @修改人：dinghaijiao  
 * @修改时间：2019年5月8日 上午9:51:10    @修改备注：
 */
public class PageSendFileParsingTask implements ConcurrentPeriodTask {

	private Logger logger = Logger.getLogger(PageSendFileParsingTask.class);
	private RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);
	private FmsBatchService fmsBatchService = BeanFactoryUtils.getBean(FmsBatchService.class);
	private long period = 1000L;// 1s

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		String serialNumber = redis.rpop(RedisConstants.FMS_PAGE_SEND_QUEUE);
		FmsBatch entity = null;
		try {
			if (!StringUtils.isEmpty(serialNumber)) {
				PageSendDto sendDto = redis.hget(RedisConstants.FMS_PAGE_SEND_HASH, serialNumber, PageSendDto.class);
				if (sendDto == null) {
					logger.error("文件解析，流水号：" + serialNumber + "的数据不存在！");
					return TaskResult.doBusinessFailResult();
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String datePath = sdf.format(sendDto.getSubmitTime());
				String parseFilePath = CommonConstanct.pageSendFilePath + File.separator + datePath + File.separator + serialNumber;
				File fileDir = new File(parseFilePath);
				boolean errorFile = false;
				if (!fileDir.exists()) {
					logger.error("目录->" + parseFilePath + "不存在！");
					errorFile = true;
				}
				File[] files = fileDir.listFiles();
				if (files == null || files.length == 0) {
					logger.error("目录->" + parseFilePath + "没有文件");
					errorFile = true;
				}
				entity = fmsBatchService.findBySerialNumber(sendDto.getBatchNumber());
				if (errorFile) {
					if (null != entity) {
						errorHandling(entity);
					}
					return TaskResult.doBusinessSuccessResult();
				}
				if (entity == null) {
					logger.error("流水号：" + serialNumber + "没入库");
					// 删除页面发送短信信息hash
					redis.hdel(RedisConstants.FMS_PAGE_SEND_HASH, serialNumber);
					return TaskResult.doBusinessSuccessResult();
				}

				// 更新批次状态--发送中
				entity.setState(FmsBatch.PARSING);
				fmsBatchService.update(entity);

				List<PageSendParsingDto> dtoList = new ArrayList<PageSendParsingDto>();
				String fileName = "";
				List<PageSendParsingDto> readDataList = null;
				List<String> pathList = ZipUtil.getFiles(parseFilePath);// 递归获取路径下所有文件
				Set<String> needTitiles = new HashSet<String>();
				if (entity.getTemplateType() == FmsBatch.COMMON_TEMPLATE) {// 普通类型
					for (String path : pathList) {
						File file = new File(path);
						fileName = file.getName().toLowerCase();
						if (fileName.endsWith(".txt")) {
							readDataList = parseTxt(file);
						} else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
							readDataList = parseExcel(file, entity.getTemplateType(), needTitiles);
						}
					}
					if (readDataList == null || readDataList.size() == 0) {// 模板错误
						errorHandling(entity);
						return TaskResult.doBusinessSuccessResult();
					} else {
						dtoList = readDataList;
					}
				} else {
					FmsTemplateRedisDto template = redis.hget(RedisConstants.FMS_TEMPLATE_HASH, sendDto.getTemplateId(), FmsTemplateRedisDto.class);
					needTitiles = parseTitle(template.getVariable());
					for (String path : pathList) {
						File file = new File(path);
						fileName = file.getName().toLowerCase();
						if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
							readDataList = parseExcel(file, entity.getTemplateType(), needTitiles);
							if (readDataList == null || readDataList.size() == 0) {// 模板错误
								errorHandling(entity);
								return TaskResult.doBusinessSuccessResult();
							}
							dtoList.addAll(readDataList);
							readDataList.clear();
						}
					}
				}

				// int titleSetSize = needTitiles.size();
				PortableNumberStore portableNumberStore = CacheConstant.portableNumberStore;
				BaseSectionNumberStore sectionNumberStore = CacheConstant.baseSectionNumberStore;
				// 参数
				int totalNumber = dtoList.size();// 总数
				int sendSuccessesNumber = 0;// 提交成功数量
				int unrecognizedNumber = 0;// 未识别数量
				int repeatNumber = 0;// 重复数量
				int errorTextNumber = 0;// 内容错误数量
				int cmccNum = 0;
				int cuccNum = 0;
				int ctccNum = 0;
				Set<String> rightSet = new HashSet<String>();
				List<PageSendParsingDto> rightList = new ArrayList<PageSendParsingDto>();
				if (dtoList.size() > 0) {
					for (PageSendParsingDto dto : dtoList) {
						String mobile = dto.getMobile();
						if (!StringUtils.isEmpty(mobile)) {
							if (!RegularCheckUtils.checkMobile(mobile)) {
								unrecognizedNumber++;
							} else {

								// boolean checkPersonality = true;
								// 校验个性短信内容
								// if (entity.getTemplateType() == FmsBatch.PERSONAL_TEMPLATE) {
								// if (dto.getData().isEmpty()) {
								// checkPersonality = false;
								// } else {
								// 校验个性变量参数个数是否==模板变量个数
								// if (dto.getData().size() != titleSetSize) {
								// checkPersonality = false;
								// }
								// // 校验每个变量的参数长度是否大于20
								// for (String dataValue : dto.getData().values()) {
								// if (dataValue.length() > GlobalConstant.TEMPLATE_VAR_MAX_LENGTH) {
								// checkPersonality = false;
								// break;
								// }
								// }
								// }
								// }
								// if (!checkPersonality) {
								// errorTextNumber++;
								// } else {
								// 校验重复号码
								String checkRepeat = "";
								if (entity.getTemplateType() == FmsBatch.COMMON_TEMPLATE) {// 普通
									checkRepeat = mobile;
								} else {// 个性--mobile和短信内容重复
									checkRepeat = mobile + dto.getValue();
								}
								if (rightSet.contains(checkRepeat)) {
									repeatNumber++;
								} else {
									// 是否携号转网
									PortableNumber sectionInfo = portableNumberStore.getSectionInfo(mobile);
									String operatorCode = null;
									if (sectionInfo != null) {
										operatorCode = sectionInfo.getOperatorCode();
									} else {
										BaseSectionNumber baseSectionInfo = sectionNumberStore.getSectionInfo(mobile);
										operatorCode = baseSectionInfo == null ? null : baseSectionInfo.getOperatorCode();
									}
									if (operatorCode == null) {
										unrecognizedNumber++;
										continue;
									} else if ("CMCC".equals(operatorCode)) {
										cmccNum++;
									} else if ("CUCC".equals(operatorCode)) {
										cuccNum++;
									} else if ("CTCC".equals(operatorCode)) {
										ctccNum++;
									}

									rightSet.add(checkRepeat);
									rightList.add(dto);
									sendSuccessesNumber++;
								}
								// }

								if (rightList.size() == CommonConstanct.pageSendBatchNumber) {
									redis.lpush(RedisConstants.FMS_PAGE_SEND_WAITING_SEND_QUEUE + serialNumber, -1, rightList.toArray());
									rightList.clear();
								}
							}
						} else {
							unrecognizedNumber++;
						}
					}
					if (rightList.size() > 0) {
						redis.lpush(RedisConstants.FMS_PAGE_SEND_WAITING_SEND_QUEUE + serialNumber, -1, rightList.toArray());
						rightList.clear();
					}
					redis.zadd(RedisConstants.FMS_SCHEDULING_QUEUE, System.currentTimeMillis(), serialNumber);
					int rightMobileSize = rightSet.size();// 可发往策略的手机号
					rightSet.clear();
					// 更新页面发送批次状态
					entity.setState(FmsBatch.SEND_FINISH);
					entity.setSendTotal(totalNumber);
					entity.setSuccessTotal(sendSuccessesNumber);
					entity.setUnknownTotal(unrecognizedNumber);
					entity.setErrorTextNumber(errorTextNumber);
					entity.setRepeatTotal(repeatNumber);
					entity.setCmccNumber(cmccNum);
					entity.setCuccNumber(cuccNum);
					entity.setCtccNumber(ctccNum);
					fmsBatchService.update(entity);
					if (rightMobileSize == 0) {// 没有可发往策略的手机号
						redis.hdel(RedisConstants.FMS_PAGE_SEND_HASH, serialNumber);
					}
				} else {
					// 没有手机号--模板错误
					errorHandling(entity);
				}
				period = 50L;
			} else {
				period = 1000L;
			}

			return TaskResult.doBusinessSuccessResult();

		} catch (Exception e) {
			logger.error("【PageSendFileParsingTask】出现异常，流水号：" + serialNumber, e);
			if (null != entity) {
				errorHandling(entity);
			}
			return TaskResult.doBusinessFailResult();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Set<String> parseTitle(String variable) {
		String[] arrays = variable.split(",");
		return new HashSet(Arrays.asList(arrays));
	}

	/**
	 * @param needTitiles
	 * @Title: parseExcel
	 * @Description: 读取Excel的数据
	 * @param @param file
	 * @param @param templateType
	 * @param @return
	 * @return List<PageSendParsingDto>
	 */
	private List<PageSendParsingDto> parseExcel(File file, Integer templateType, Set<String> needTitiles) {
		PageSendDTOReader reader = new PageSendDTOReader();
		reader.setNeedTitles(needTitiles);
		ExcelReader.readFirstSheet(file.getPath(), reader);
		List<PageSendParsingDto> pageSendParsingDtoList = reader.getDatas();
		return pageSendParsingDtoList;
	}

	/**
	 * @Title: parseTxt
	 * @Description: 读取txt的数据
	 * @param @param file
	 * @param @return
	 * @return List<PageSendParsingDto>
	 */
	private List<PageSendParsingDto> parseTxt(File file) {
		List<PageSendParsingDto> pageSendParsingDtoList = new ArrayList<PageSendParsingDto>();
		BufferedReader reader = null;
		try {
			InputStreamReader inReader = new InputStreamReader(new FileInputStream(file), "gbk");
			reader = new BufferedReader(inReader);
			String line = reader.readLine();
			if (line != null && !"".equals(line.trim())) {
				while (line != null) {
					if (line != null && !"".equals(line.trim())) {
						String mobile = line.trim();
						pageSendParsingDtoList.add(new PageSendParsingDto(mobile));
					}
					line = reader.readLine();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return pageSendParsingDtoList;
	}

	@Override
	public int needConcurrent(int alive, int min, int max) {
		return redis.llen(RedisConstants.FMS_PAGE_SEND_QUEUE).intValue();
	}

	@Override
	public long period() {
		return period;
	}

	/**
	 * 模板错误处理
	 * 
	 * @param entity
	 */
	private void errorHandling(FmsBatch entity) {
		// 更新页面发送批次状态
		entity.setState(FmsBatch.DATA_ERROR);
		entity.setSendTotal(0);
		entity.setSuccessTotal(0);
		entity.setUnknownTotal(0);
		entity.setErrorTextNumber(0);
		entity.setRepeatTotal(0);
		fmsBatchService.update(entity);
		// 删除页面发送短信信息hash
		redis.hdel(RedisConstants.FMS_PAGE_SEND_HASH, entity.getBatchNumber());
	}
}
