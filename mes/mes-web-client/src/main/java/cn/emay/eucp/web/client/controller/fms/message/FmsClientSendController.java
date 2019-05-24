package cn.emay.eucp.web.client.controller.fms.message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.page.PageSendDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateAuditDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBatch;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateServiceCodeAssign;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.util.GZip;
import cn.emay.eucp.common.util.OnlyIdGenerator;
import cn.emay.eucp.data.service.fms.FmsBatchService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.data.service.fms.FmsTemplateServiceCodeAssignService;
import cn.emay.eucp.data.service.fms.FmsUserServiceCodeAssignService;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.eucp.web.util.RarUtil;
import cn.emay.eucp.web.util.excel.OnlyReadFirstRowUtil;
import cn.emay.util.PropertiesUtil;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;
import cn.emay.util.ZipUtil;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午3:35:32 类说明 */
@PageAuth("FMS_CLINT_MESSAGE_SEND")
@RequestMapping("/fms/client/messagesend")
@Controller
public class FmsClientSendController {

	@Resource
	FmsServiceCodeService fmsServiceCodeService;
	@Resource
	private RedisClient redis;
	@Resource
	private FmsUserServiceCodeAssignService fmsUserServiceCodeAssignService;
	@Resource
	private FmsTemplateServiceCodeAssignService fmsTemplateServiceCodeAssignService;
	@Resource
	private FmsBatchService fmsBatchService;

	private Logger logger = Logger.getLogger(FmsClientSendController.class);

	private static String pageSendPath = PropertiesUtil.getProperty("page.send.mobile.file.path", "eucp.service.properties");

	private static String pageSendFileSize = PropertiesUtil.getProperty("fms.page.send.file.size", "eucp.service.properties");
	public static String pageSendCode = PropertiesUtil.getProperty("eucp.pagesend.code", "eucp.service.properties");

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		// 用户关联服务号
		List<FmsServiceCode> voiceServiceCodeList = fmsServiceCodeService.findByUserId(currentUser.getId());
		model.addAttribute("voiceServiceCodeList", voiceServiceCodeList);
		return "fms/message/send";
	}

	public List<FmsTemplateServiceCodeAssign> getUser(Long userId, Long serviceCodeId, String templateId) {
		List<FmsTemplateServiceCodeAssign> list = fmsTemplateServiceCodeAssignService.findByAppIdAndTemplateId(userId, serviceCodeId, templateId);
		return list;

	}

	@RequestMapping("/send")
	public void send(HttpServletRequest request, HttpServletResponse response) {
		// 参数
		String appId = null;
		String content = null;
		String mobiles = null;
		String title = null;
		String templateId = null;
		Long cmccChannelId = null;
		Long cuccChannelId = null;
		Long ctccChannelId = null;
		Long contentTypeId = null;
		String cmccTemplateId = null;
		String cuccTemplateId = null;
		String ctccTemplateId = null;
		Integer cmccAuditState = null;
		Integer cuccAuditState = null;
		Integer ctccAuditState = null;
		Long serviceCodeId = null;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setHeaderEncoding("UTF-8");
		InputStream input = null;
		String fileName = "";
		String setFileSize = pageSendFileSize;
		long maxSize = Long.parseLong(setFileSize) * 1024 * 1024;
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> itemList = sfu.parseRequest(request);
			for (FileItem fileItem : itemList) {
				if (fileItem.isFormField()) {
					String temp = new String(fileItem.getString().getBytes("iso8859-1"), "utf-8");
					if ("mobiles".equals(fileItem.getFieldName())) {
						mobiles = temp;
					} else if ("title".equals(fileItem.getFieldName())) {
						title = temp;
					} else if ("content".equals(fileItem.getFieldName())) {
						content = temp;
					} else if ("templateId".equals(fileItem.getFieldName())) {
						if (!StringUtils.isEmpty(temp)) {
							templateId = temp;
						} else {
							ResponseUtils.outputWithJson(response, Result.badResult("请选择模板"));
							return;
						}
					} else if ("appId".equals(fileItem.getFieldName())) {
						appId = temp;
					} else if ("cmccChannelId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							cmccChannelId = Long.valueOf(temp);
						}
					} else if ("cuccChannelId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							cuccChannelId = Long.valueOf(temp);
						}
					} else if ("ctccChannelId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							ctccChannelId = Long.valueOf(temp);
						}
					} else if ("contentTypeId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							contentTypeId = Long.valueOf(temp);
						}
					} else if ("cmccTemplateId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							cmccTemplateId = temp;
						}
					} else if ("cuccTemplateId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							cuccTemplateId = temp;
						}
					} else if ("ctccTemplateId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							ctccTemplateId = temp;
						}
					} else if ("cmccAuditState".equals(fileItem.getFieldName())) {
						if (temp != null) {
							cmccAuditState = Integer.valueOf(temp);
						}
					} else if ("cuccAuditState".equals(fileItem.getFieldName())) {
						if (temp != null) {
							cuccAuditState = Integer.valueOf(temp);
						}
					} else if ("ctccAuditState".equals(fileItem.getFieldName())) {
						if (temp != null) {
							ctccAuditState = Integer.valueOf(temp);
						}
					} else if ("serviceCodeId".equals(fileItem.getFieldName())) {
						if (!StringUtils.isEmpty(temp)) {
							serviceCodeId = Long.valueOf(temp);
						} else {
							ResponseUtils.outputWithJson(response, Result.badResult("请选择模板"));
							return;
						}
					}
				} else {
					Long size = fileItem.getSize();
					fileName = fileItem.getName();
					input = fileItem.getInputStream();
					String fileNameToLowerCase = fileItem.getName().toLowerCase();
					if (size > maxSize) {
						ResponseUtils.outputWithJson(response, Result.badResult("导入文件大小不能超过" + setFileSize + "M"));
						return;
					}
					if (!fileNameToLowerCase.endsWith(".txt") && !fileNameToLowerCase.endsWith(".xls") && !fileNameToLowerCase.endsWith(".xlsx") && !fileNameToLowerCase.endsWith(".zip")
							&& !fileNameToLowerCase.endsWith(".rar") && !fileNameToLowerCase.endsWith(".tar.gz")) {
						ResponseUtils.outputWithJson(response, Result.badResult("导入文件格式不正确"));
						return;
					}
				}
			}
		} catch (FileUploadException e) {
			logger.error("import file error!", e);
			ResponseUtils.outputWithJson(response, Result.badResult("文件导入异常"));
			return;
		} catch (UnsupportedEncodingException e) {
			logger.error("import file error!", e);
			ResponseUtils.outputWithJson(response, Result.badResult("文件导入异常-编码异常"));
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("import file error!", e);
			ResponseUtils.outputWithJson(response, Result.badResult("文件导入异常-IO异常"));
			e.printStackTrace();
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result checkResult = check(mobiles);
		if (!checkResult.getSuccess()) {
			ResponseUtils.outputWithJson(response, checkResult);
			return;
		}
		if (StringUtils.isEmpty(fileName) && StringUtils.isEmpty(mobiles)) {
			ResponseUtils.outputWithJson(response, Result.badResult("请输入手机号或者上传号码文件"));
			return;
		}
		String batchNo = OnlyIdGenerator.genOnlyBId(pageSendCode);
		String serialNumber = batchNo;// 生成流水号
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String datePath = sdf.format(new Date());
		String tarFilePath = pageSendPath + File.separator + datePath + File.separator + serialNumber;// 文件目标路径
		File disfile = new File(tarFilePath);
		if (!disfile.exists()) {
			boolean flag = disfile.mkdirs();
			if (!flag) {
				ResponseUtils.outputWithJson(response, Result.badResult("创建文件夹异常！"));
				return;
			}
		}
		// 如果存在手动输入手机号，则生成.txt文件
		if (!StringUtils.isEmpty(mobiles)) {
			String[] mobileArr = mobiles.split(",");
			if (mobileArr != null && mobileArr.length > 0) {
				String mobileFileName = "mobile_" + serialNumber + ".txt";
				try {
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(tarFilePath + File.separator + mobileFileName)), "gbk"));
					for (String mobile : mobileArr) {
						if (!StringUtils.isEmpty(mobile)) {
							bw.write(mobile.trim());
							bw.newLine();
						}
					}
					bw.close();
				} catch (Exception e) {
					logger.error("save file error:", e);
				}
			}
		}
		// 用户上传文件另存
		if (!StringUtils.isEmpty(fileName)) {// 用户上传了文件
			FileOutputStream os = null;
			File file = null;
			try {
				byte[] bs = new byte[1024 * 10];
				file = new File(tarFilePath + File.separator + fileName);
				os = new FileOutputStream(file);
				int len;
				while ((len = input.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
			} catch (IOException e) {
				logger.error("save file error:", e);
			} finally {
				try {
					if (null != os) {
						os.close();
					}
					if (null != input) {
						input.close();
					}
				} catch (IOException e) {
					logger.error("save file error:", e);
				}
			}
			// 同步解压压缩包，异步解析文件
			try {
				String unpack = tarFilePath + File.separator + "unpack";// 解压路径
				Result result = unzip(file, unpack, setFileSize);
				if (!result.getSuccess()) {
					ResponseUtils.outputWithJson(response, result);
					return;
				}
			} catch (Exception e) {
				ResponseUtils.outputWithJson(response, Result.badResult("压缩包解压异常"));
				logger.error("压缩包解压异常:", e);
				return;
			}
		}
		Date submitTime = new Date();
		List<FmsTemplateServiceCodeAssign> list = getUser(currentUser.getId(), serviceCodeId, templateId);
		if (list == null || list.size() == 0) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户信息不正确"));
			return;
		}
		FmsTemplateServiceCodeAssign fmsTemplateServiceCodeAssign = list.get(0);
		// String remoteIp = RequestUtils.getRemoteRealIp(request);
		// 保存批次表
		FmsBatch fmsBacth = new FmsBatch();
		fmsBacth.setAppId(fmsTemplateServiceCodeAssign.getAppId());
		fmsBacth.setBatchNumber(batchNo);
		fmsBacth.setTemplateId(templateId);
		fmsBacth.setTitle(title);
		fmsBacth.setTemplateType(FmsBatch.COMMON_TEMPLATE);
		fmsBacth.setUserId(currentUser.getId());
		fmsBacth.setSubmitTime(submitTime);
		fmsBacth.setState(FmsBatch.WATING_PARSE);
		fmsBacth.setContent(content);
		fmsBacth.setContentTypeId(fmsTemplateServiceCodeAssign.getContentTypeId());
		fmsBacth.setServiceCodeId(serviceCodeId);
		fmsBatchService.saveBatch(fmsBacth);
		// 入文件解析队列和hash
		PageSendDto pageSendDto = new PageSendDto(templateId, FmsBatch.COMMON_TEMPLATE, appId, submitTime, FmsBatch.WATING_PARSE, title, batchNo, PageSendDto.PAGE_TYPE, cmccChannelId, cuccChannelId,
				ctccChannelId, contentTypeId, cmccTemplateId, cuccTemplateId, ctccTemplateId, cmccAuditState, cuccAuditState, ctccAuditState);
		redis.hset(RedisConstants.FMS_PAGE_SEND_HASH, serialNumber, pageSendDto, 0);
		redis.lpush(RedisConstants.FMS_PAGE_SEND_QUEUE, serialNumber, 0);
		logger.info("页面发送提交成功流水号为" + serialNumber);
		ResponseUtils.outputWithJson(response, Result.rightResult());

	}

	@RequestMapping("/sendPersonal")
	public void sendPersonal(HttpServletRequest request, HttpServletResponse response) {

		// 参数
		String appId = null;
		String content = null;
		String mobiles = null;
		String title = null;
		String templateId = null;
		Long cmccChannelId = null;
		Long cuccChannelId = null;
		Long ctccChannelId = null;
		Long contentTypeId = null;
		String cmccTemplateId = null;
		String cuccTemplateId = null;
		String ctccTemplateId = null;
		Integer cmccAuditState = null;
		Integer cuccAuditState = null;
		Integer ctccAuditState = null;
		Long serviceCodeId = null;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setHeaderEncoding("UTF-8");
		InputStream input = null;
		String fileName = "";
		String setFileSize = pageSendFileSize;
		long maxSize = Long.parseLong(setFileSize) * 1024 * 1024;
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> itemList = sfu.parseRequest(request);
			for (FileItem fileItem : itemList) {
				if (fileItem.isFormField()) {
					String temp = new String(fileItem.getString().getBytes("iso8859-1"), "utf-8");
					if ("mobiles".equals(fileItem.getFieldName())) {
						mobiles = temp;
					} else if ("title".equals(fileItem.getFieldName())) {
						title = temp;
					} else if ("content".equals(fileItem.getFieldName())) {
						content = temp;
					} else if ("templateId".equals(fileItem.getFieldName())) {
						if (!StringUtils.isEmpty(temp)) {
							templateId = temp;
						} else {
							ResponseUtils.outputWithJson(response, Result.badResult("请选择模板"));
							return;
						}
					} else if ("appId".equals(fileItem.getFieldName())) {
						appId = temp;
					} else if ("cmccChannelId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							cmccChannelId = Long.valueOf(temp);
						}
					} else if ("cuccChannelId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							cuccChannelId = Long.valueOf(temp);
						}
					} else if ("ctccChannelId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							ctccChannelId = Long.valueOf(temp);
						}
					} else if ("contentTypeId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							contentTypeId = Long.valueOf(temp);
						}
					} else if ("cmccTemplateId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							cmccTemplateId = temp;
						}
					} else if ("cuccTemplateId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							cuccTemplateId = temp;
						}
					} else if ("ctccTemplateId".equals(fileItem.getFieldName())) {
						if (temp != null) {
							ctccTemplateId = temp;
						}
					} else if ("cmccAuditState".equals(fileItem.getFieldName())) {
						if (temp != null) {
							cmccAuditState = Integer.valueOf(temp);
						}
					} else if ("cuccAuditState".equals(fileItem.getFieldName())) {
						if (temp != null) {
							cuccAuditState = Integer.valueOf(temp);
						}
					} else if ("ctccAuditState".equals(fileItem.getFieldName())) {
						if (temp != null) {
							ctccAuditState = Integer.valueOf(temp);
						}
					} else if ("serviceCodeId".equals(fileItem.getFieldName())) {
						if (!StringUtils.isEmpty(temp)) {
							serviceCodeId = Long.valueOf(temp);
						} else {
							ResponseUtils.outputWithJson(response, Result.badResult("请选择模板"));
							return;
						}
					}
				} else {
					Long size = fileItem.getSize();
					fileName = fileItem.getName();
					input = fileItem.getInputStream();
					String fileNameToLowerCase = fileItem.getName().toLowerCase();
					if (size > maxSize) {
						ResponseUtils.outputWithJson(response, Result.badResult("导入文件大小不能超过" + setFileSize + "M"));
						return;
					}
					if (!fileNameToLowerCase.endsWith(".txt") && !fileNameToLowerCase.endsWith(".xls") && !fileNameToLowerCase.endsWith(".xlsx") && !fileNameToLowerCase.endsWith(".zip")
							&& !fileNameToLowerCase.endsWith(".rar") && !fileNameToLowerCase.endsWith(".tar.gz")) {
						ResponseUtils.outputWithJson(response, Result.badResult("导入文件格式不正确"));
						return;
					}
				}
			}
		} catch (FileUploadException e) {
			logger.error("import file error!", e);
			ResponseUtils.outputWithJson(response, Result.badResult("文件导入异常"));
			return;
		} catch (UnsupportedEncodingException e) {
			logger.error("import file error!", e);
			ResponseUtils.outputWithJson(response, Result.badResult("文件导入异常-编码异常"));
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("import file error!", e);
			ResponseUtils.outputWithJson(response, Result.badResult("文件导入异常-IO异常"));
			e.printStackTrace();
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result checkResult = check(mobiles);
		if (!checkResult.getSuccess()) {
			ResponseUtils.outputWithJson(response, checkResult);
			return;
		}
		if (StringUtils.isEmpty(fileName)) {
			ResponseUtils.outputWithJson(response, Result.badResult("请上传内容文件"));
			return;
		}
		String batchNo = OnlyIdGenerator.genOnlyBId(pageSendCode);
		String serialNumber = batchNo;// 生成流水号
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String datePath = sdf.format(new Date());
		String tarFilePath = pageSendPath + File.separator + datePath + File.separator + serialNumber;// 文件目标路径
		File disfile = new File(tarFilePath);
		if (!disfile.exists()) {
			boolean flag = disfile.mkdirs();
			if (!flag) {
				ResponseUtils.outputWithJson(response, Result.badResult("创建文件夹异常！"));
				return;
			}
		}
		// 用户上传文件另存
		if (!StringUtils.isEmpty(fileName)) {// 用户上传了文件
			FileOutputStream os = null;
			File file = null;
			try {
				byte[] bs = new byte[1024 * 10];
				file = new File(tarFilePath + File.separator + fileName);
				os = new FileOutputStream(file);
				int len;
				while ((len = input.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
			} catch (IOException e) {
				logger.error("save file error:", e);
			} finally {
				try {
					if (null != os) {
						os.close();
					}
					if (null != input) {
						input.close();
					}
				} catch (IOException e) {
					logger.error("save file error:", e);
				}
			}
			// 同步解压压缩包，异步解析文件
			try {
				String unpack = tarFilePath + File.separator + "unpack";// 解压路径
				Result result = unzip(file, unpack, setFileSize);
				if (!result.getSuccess()) {
					ResponseUtils.outputWithJson(response, result);
					return;
				}
			} catch (Exception e) {
				ResponseUtils.outputWithJson(response, Result.badResult("压缩包解压异常"));
				logger.error("压缩包解压异常:", e);
				return;
			}
		}
		Date submitTime = new Date();
		List<FmsTemplateServiceCodeAssign> list = getUser(currentUser.getId(), serviceCodeId, templateId);
		if (list == null || list.size() == 0) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户信息不正确"));
			return;
		}
		FmsTemplateServiceCodeAssign fmsTemplateServiceCodeAssign = list.get(0);
		// String remoteIp = RequestUtils.getRemoteRealIp(request);
		// 保存批次表

		FmsBatch fmsBacth = new FmsBatch();
		fmsBacth.setAppId(fmsTemplateServiceCodeAssign.getAppId());
		fmsBacth.setBatchNumber(batchNo);
		fmsBacth.setTemplateId(templateId);
		fmsBacth.setTitle(title);
		fmsBacth.setTemplateType(FmsBatch.PERSONAL_TEMPLATE);
		fmsBacth.setUserId(currentUser.getId());
		fmsBacth.setSubmitTime(submitTime);
		fmsBacth.setState(FmsBatch.WATING_PARSE);
		fmsBacth.setContent(content);
		fmsBacth.setContentTypeId(fmsTemplateServiceCodeAssign.getContentTypeId());
		fmsBacth.setServiceCodeId(serviceCodeId);
		fmsBatchService.saveBatch(fmsBacth);
		// 入文件解析队列和hash
		PageSendDto pageSendDto = new PageSendDto(templateId, FmsBatch.PERSONAL_TEMPLATE, appId, submitTime, FmsBatch.WATING_PARSE, title, batchNo, PageSendDto.PAGE_TYPE, cmccChannelId, cuccChannelId,
				ctccChannelId, contentTypeId, cmccTemplateId, cuccTemplateId, ctccTemplateId, cmccAuditState, cuccAuditState, ctccAuditState);
		redis.hset(RedisConstants.FMS_PAGE_SEND_HASH, serialNumber, pageSendDto, 0);
		redis.lpush(RedisConstants.FMS_PAGE_SEND_QUEUE, serialNumber, 0);
		logger.info("页面发送提交成功流水号为" + serialNumber);
		ResponseUtils.outputWithJson(response, Result.rightResult());

	}

	@RequestMapping("/ajax/gettemplate")
	public void getTemplate(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String title = RequestUtils.getParameter(request, "templateName");
		String content = RequestUtils.getParameter(request, "content");
		int templateType = RequestUtils.getIntParameter(request, "templateType", -1);
		User user = WebUtils.getCurrentUser(request, response);
		List<FmsServiceCode> serviceCodes = fmsUserServiceCodeAssignService.findByAssignUserId(user.getId());
		Set<String> appIdSet = new HashSet<String>();
		Map<String, FmsServiceCode> map = new HashMap<>();
		for (FmsServiceCode fmsServiceCode : serviceCodes) {
			appIdSet.add(fmsServiceCode.getAppId());
			map.put(fmsServiceCode.getAppId(), fmsServiceCode);
		}
		List<String> appIds = new ArrayList<String>();
		appIds.addAll(appIdSet);
		Page<FmsTemplateDto> page = fmsTemplateServiceCodeAssignService.findTempLetPage(title, content, appIds, null, null, null, null, PageSendDto.PAGE_TYPE, null, null, null, start, limit,
				templateType);
		List<FmsTemplateAuditDto> list = new ArrayList<>();
		if (page.getList() != null && page.getList().size() > 0) {
			for (FmsTemplateDto fms : page.getList()) {
				FmsTemplateAuditDto fmsTemplateAuditDto = new FmsTemplateAuditDto();
				BeanUtils.copyProperties(fms, fmsTemplateAuditDto);
				fmsTemplateAuditDto.setServiceCode(map.get(fms.getAppId()).getServiceCode());
				fmsTemplateAuditDto.setServiceCodeId(map.get(fms.getAppId()).getId());
				list.add(fmsTemplateAuditDto);
			}
		}
		Page<FmsTemplateAuditDto> page2 = new Page<FmsTemplateAuditDto>();
		page2.setList(list);
		page2.setStart(page.getStart());
		page2.setLimit(page.getLimit());
		page2.setTotalCount(page.getTotalCount());
		page2.setTotalPage(page.getTotalPage());
		page2.setCurrentPageNum(page.getCurrentPageNum());
		ResponseUtils.outputWithJson(response, Result.rightResult(page2));
	}

	public Result check(String mobiles) {
		if (!StringUtils.isEmpty(mobiles)) {
			String[] mobileArr = mobiles.split(",");
			if (mobileArr != null && mobileArr.length > 0) {
				Set<String> errorMobile = new HashSet<String>();// 错误手机号
				Set<String> repeatMobile = new HashSet<String>();// 重复手机号
				Set<String> mobileSet = new HashSet<String>();
				for (String mobile : mobileArr) {
					if (null != mobile) {
						mobile = mobile.trim();
					}
					if (!StringUtils.isEmpty(mobile)) {
						if (mobileSet.contains(mobile)) {
							repeatMobile.add(mobile);
						} else {
							mobileSet.add(mobile);
						}
						if (!RegularUtils.checkMobileFormat(mobile)) {
							errorMobile.add(mobile);
						}
					}
				}
				if (repeatMobile.size() > 0) {
					return Result.badResult("手机号：" + org.apache.commons.lang3.StringUtils.join(repeatMobile, ",") + "重复");
				}
				if (errorMobile.size() > 0) {
					return Result.badResult("手机号：" + org.apache.commons.lang3.StringUtils.join(errorMobile, ",") + "不正确");
				}

			}
		}
		// if (StringUtils.isEmpty(content) || "".equals(content.trim())) {
		// return Result.badResult("短信内容不能为空");
		// }
		// if (content.trim().length() > GlobalConstant.TEMPLATE_MAX_LENGTH) {
		// return Result.badResult("短信内容长度不能超过70字");
		// }
		return Result.rightResult();
	}

	/** 上传压缩包进行压缩处理
	 * 
	 * @param unzipFile
	 * @param tarFilePath
	 * @return */
	public Result unzip(File unzipFile, String tarFilePath, String setFileSize) {
		long maxSize = Long.parseLong(setFileSize) * 1024 * 1024;
		String fileName = unzipFile.getName();
		if (fileName.toLowerCase().endsWith(".zip") || fileName.toLowerCase().endsWith(".rar") || fileName.toLowerCase().endsWith(".tar.gz")) {
			if (fileName.toLowerCase().endsWith(".zip")) {
				ZipUtil.upzipFile(unzipFile.getPath(), tarFilePath);
			} else if (fileName.toLowerCase().endsWith(".rar")) {
				RarUtil.unRarFile(unzipFile.getPath(), tarFilePath);
			} else {// .tar.gz
				GZip.unTargzFile(unzipFile.getPath(), tarFilePath);
			}
			File fileDir = new File(tarFilePath);
			if (!fileDir.exists()) {
				return Result.badResult("上传压缩包解压失败");
			}
			File[] files = fileDir.listFiles();
			if (files == null || files.length == 0) {
				logger.error("上传压缩包解压失败");
				return Result.badResult("上传压缩包解压失败");
			}
			long fileSize = 0L;
			List<String> pathList = ZipUtil.getFiles(tarFilePath);// 递归获取解压路径下所有文件
			for (String path : pathList) {
				File file = new File(path);
				String unzipFileName = file.getName().toLowerCase();
				if (!unzipFileName.endsWith(".txt") && !unzipFileName.endsWith(".xls") && !unzipFileName.endsWith(".xlsx")) {
					return Result.badResult("压缩包中文件格式不正确");
				}
				fileSize += file.length();
			}
			if (fileSize > maxSize) {
				return Result.badResult("压缩包内文件大小不能超过" + setFileSize + "M");
			}
		}
		return Result.rightResult();
	}

	public Result parsingFile(String parseFilePath) {
		File fileDir = new File(parseFilePath);
		if (!fileDir.exists()) {
			logger.error("目录->" + parseFilePath + "不存在！");
			return Result.badResult("目录->" + parseFilePath + "不存在！");
		}
		File[] files = fileDir.listFiles();
		if (files == null || files.length == 0) {
			logger.error("目录->" + parseFilePath + "没有文件");
			return Result.badResult("目录->" + parseFilePath + "没有文件");
		}
		String fileName = "";
		List<String> pathList = ZipUtil.getFiles(parseFilePath);// 递归获取路径下所有文件
		for (int i = 0; i < pathList.size(); i++) {
			String s = pathList.get(i);
			if (s.toLowerCase().endsWith(".zip") || s.toLowerCase().endsWith(".rar") || s.toLowerCase().endsWith(".tar.gz")) {
				pathList.remove(i);
				i--;
			}
		}
		if (pathList.size() > 1) {
			return Result.badResult("文件过多");
		}
		String[] titles = null;
		for (String path : pathList) {
			File file = new File(path);
			fileName = file.getName().toLowerCase();
			// 过滤压缩包文件,只解析txt,xls,xlsx文件(已解压)
			if (fileName.endsWith(".txt")) {
				try {
					titles = OnlyReadFirstRowUtil.getFirstRowFromTxt(file);
				} catch (Exception e) {
					return Result.badResult("文件解析异常");
				}
			} else if (fileName.endsWith(".xlsx")) {
				try {
					titles = OnlyReadFirstRowUtil.getFirstRowFromXlsx(file);
				} catch (Exception e) {
					return Result.badResult("文件解析异常");
				}
			} else if (fileName.endsWith(".xls")) {
				try {
					titles = OnlyReadFirstRowUtil.getFirstRowFromXls(file);
				} catch (Exception e) {
					return Result.badResult("文件解析异常");
				}
			}
		}
		if (null == titles || titles.length == 0) {
			return Result.badResult("模板错误，未找到标题");
		}
		Set<String> repeatTitle = new HashSet<String>();
		for (String s : titles) {
			if (repeatTitle.contains(s)) {
				return Result.badResult("模板错误，存在重复标题");
			}
			repeatTitle.add(s);
		}
		if (!repeatTitle.contains("手机号码")) {
			return Result.badResult("模板错误，未找到手机号列");
		}
		return Result.rightResult(titles);
	}
}
