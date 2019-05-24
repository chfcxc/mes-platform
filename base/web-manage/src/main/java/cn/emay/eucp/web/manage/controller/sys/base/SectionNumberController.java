package cn.emay.eucp.web.manage.controller.sys.base;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.common.excel.ExcelBean;
import cn.emay.common.excel.ExcelHelper;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.constant.Operator;
import cn.emay.eucp.common.constant.Province;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.SectionNumber;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.ExcelReaderService;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.data.service.system.SectionNumberService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.eucp.web.manage.handler.SectionNumberSchemaSheetDataHandler;
import cn.emay.excel.read.ExcelReader;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

import com.google.gson.reflect.TypeToken;

/**
 * 精准号段管理
 *
 * @author 徐亚光
 *
 */

@PageAuth("SYS_BASE_SECTION")
@RequestMapping("/sys/base/section")
@Controller
public class SectionNumberController {

	@Resource(name = "sectionNumberService")
	SectionNumberService sectionNumberService;
	@Resource(name = "baseExcelReaderService")
	private ExcelReaderService excelReaderService;

	private static Logger log = Logger.getLogger(SectionNumberController.class);
	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("list", Province.toDtos());
		return "/sys/base/sectionnumber";
	}

	/**
	 *
	 * 查询
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String number = RequestUtils.getParameter(request, "number");
		String operatorCode = RequestUtils.getParameter(request, "operatorCode");
		String provinceCode = RequestUtils.getParameter(request, "provinceCode");
		Page<SectionNumber> page = sectionNumberService.findByNumberAndoperatorCode(number, operatorCode, provinceCode, start, limit);
		for (SectionNumber sec : page.getList()) {
			sec.setOperatorCode(Operator.findNameByCode(sec.getOperatorCode()));
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(page));

	}

	/**
	 *
	 * 增加跳转页面
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@OperationAuth("OPER_SYS_SECTION_ADD")
	@RequestMapping("toadd")
	public String addSN(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("list", Province.toDtos());
		return "sys/base/addsectionnumber";
	}

	/**
	 *
	 * 增加
	 *
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_SECTION_ADD")
	@RequestMapping("addsn")
	public void addSectionNumber(HttpServletRequest request, HttpServletResponse response) {
		String number = RequestUtils.getParameter(request, "number");
		String provinceCode = RequestUtils.getParameter(request, "provinceCode");
		String provinceName = Province.findNameByCode(provinceCode);
		String city = RequestUtils.getParameter(request, "city");
		String operatorCode = RequestUtils.getParameter(request, "operatorCode");
		Result result2 = sectionNumberService.verify(number, provinceCode, provinceName, city, operatorCode);
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		boolean sn = sectionNumberService.findNumber(number, null);// 换成布尔类型
		if (!sn) {
			SectionNumber sectionNumber = new SectionNumber();
			sectionNumber.setNumber(number);
			sectionNumber.setOperatorCode(operatorCode);
			sectionNumber.setProvinceName(provinceName);
			sectionNumber.setCity(city);
			sectionNumber.setProvinceCode(provinceCode);
			sectionNumber.setIsDelete(false);
			Result result = sectionNumberService.addSectionNumber(sectionNumber);
			User currentUser = WebUtils.getCurrentUser(request, response);
			if (result.getSuccess()) {
				String service = "公共服务";
				String context = "增加精确号段{0}";
				String module = "精确号段";
				manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, number), ManageUserOperLog.OPERATE_ADD);
				log.info("用户:" + currentUser.getUsername() + "增加精确号段,号段名称为:" + number);
			}
			ResponseUtils.outputWithJson(response, Result.rightResult());
		} else {
			ResponseUtils.outputWithJson(response, Result.badResult("数据已存在"));
			return;
		}

	}

	/**
	 *
	 * 修改跳转页面
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@OperationAuth("OPER_SYS_SECTION_UPDATE")
	@RequestMapping("toupdate")
	public String update(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("list", Province.toDtos());
		long id = RequestUtils.getLongParameter(request, "id", 0l);
		SectionNumber sectionNumber = sectionNumberService.findById(id);
		request.setAttribute("sectionNumber", sectionNumber);
		return "sys/base/updatebasesectionnumber";
	}

	/**
	 *
	 * 修改
	 *
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_SECTION_UPDATE")
	@RequestMapping("updatesn")
	public void updateSectionNumber(HttpServletRequest request, HttpServletResponse response) {
		long id = RequestUtils.getLongParameter(request, "id", 0l);
		String number = RequestUtils.getParameter(request, "number");
		String provinceCode = RequestUtils.getParameter(request, "provinceCode");
		String provinceName = Province.findNameByCode(provinceCode);
		String city = RequestUtils.getParameter(request, "city");
		String operatorCode = RequestUtils.getParameter(request, "operatorCode");
		Result result2 = sectionNumberService.verify(number, provinceCode, provinceName, city, operatorCode);
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		boolean sn = sectionNumberService.findNumber(number, id);
		SectionNumber sectionNumber = new SectionNumber();
		if (!sn) {
			sectionNumber.setId(id);
			sectionNumber.setNumber(number);
			sectionNumber.setOperatorCode(operatorCode);
			sectionNumber.setCity(city);
			sectionNumber.setProvinceCode(provinceCode);
			sectionNumber.setProvinceName(provinceName);
			sectionNumber.setIsDelete(false);
			Result result = sectionNumberService.updateSectionNumber(sectionNumber);
			User currentUser = WebUtils.getCurrentUser(request, response);
			if (result.getSuccess()) {
				String service = "公共服务";
				String context = "修改精确号段{0}";
				String module = "精确号段";
				manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, number), ManageUserOperLog.OPERATE_MODIFY);
				log.info("用户:" + currentUser.getUsername() + "修改精确号段,号段名称为:" + number);
			}
		} else {
			ResponseUtils.outputWithJson(response, Result.badResult("数据已存在"));
			return;
		}
		ResponseUtils.outputWithJson(response, Result.rightResult());

	}

	/**
	 *
	 * 删除
	 *
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_SECTION_DELETE")
	@RequestMapping("delete")
	public void deleteSectionNumber(HttpServletRequest request, HttpServletResponse response) {
		long id = RequestUtils.getLongParameter(request, "id", 0l);
		Result result = sectionNumberService.deleteSectionNumber(id);
		SectionNumber sectionNumber = sectionNumberService.findById(id);
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "删除精确号段{0}";
			String module = "精确号段";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, sectionNumber.getNumber()), ManageUserOperLog.OPERATE_DELETE);
			log.info("用户:" + currentUser.getUsername() + "删除精确号段,号段名称为:" + sectionNumber.getNumber());
		}
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	/**
	 *
	 * 导入
	 *
	 * @throws IOException
	 * @throws FileUploadException
	 */
	@OperationAuth("OPER_SYS_SECTION_ADD")
	@RequestMapping("/importFile")
	public void importnumber(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Result result = WebUtils.importExcel(request, 1024 * 1024 * 5);
		//final Map<String, Object> map = new HashMap<String, Object>();
		//final List<String[]> errors = new ArrayList<String[]>();
		//final List<Integer> sum = new ArrayList<Integer>();
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		///String[] columns = new String[] { "number", "province_code", "province_name", "city", "operator_code", "is_delete" };
		//String tableName = "system_section_number";
//		ExcelRowReaderDB readerDBx = new ExcelRowReaderDB(tableName, columns, 1000) {
//			@Override
//			public boolean checkRow(int sheetIndex, int curRow, List<String> rowlist) {
//				if (curRow == 0) { // 标题行放过
//					return false;
//				}
//				if (null == rowlist || rowlist.size() == 0) {
//					return false;
//				}
//				sum.add(curRow);
//				int rowLength = rowlist.size();
//				String[] array = new String[rowLength];
//				rowlist.toArray(array);
//				if (rowLength < 3 || rowLength > 4) {
//					errors.add(RegularUtils.copyOfRange(array, "行数不正确"));
//					return false;
//				}
//				String number = rowlist.get(0);
//				String operatorName = rowlist.get(1);
//				String provinceName = rowlist.get(2);
//				String city = "";
//				if (rowlist.size() == 4) {
//					city = rowlist.get(3);
//				}
//				String[] error = { number, operatorName, provinceName, city };
//				if (StringUtils.isEmpty(rowlist.get(0))) {
//					errors.add(RegularUtils.copyOfRange(error, "号段不能为空"));
//					return false;
//				}
//				if (!RegularUtils.checkSNumber(rowlist.get(0))) {
//					errors.add(RegularUtils.copyOfRange(error, "请输入以1开头的7位数字"));
//					return false;
//				}
//				if (rowlist.get(0).length() < 7) {
//					errors.add(RegularUtils.copyOfRange(array, "号段号为7位"));
//					return false;
//				}
//				String ecode = Operator.findCodeByName(operatorName);
//				if (!StringUtils.isEmpty(operatorName) && null == ecode) {
//					errors.add(RegularUtils.copyOfRange(error, "输入运营商不正确"));
//					return false;
//				}
//				String code = Province.findCodeByName(provinceName);
//				if (StringUtils.isEmpty(code)) {
//					errors.add(RegularUtils.copyOfRange(error, "省份输入错误"));
//					return false;
//				}
//				if (rowlist.size() == 3) {
//					rowlist.add(3, null);
//				}
//				rowlist.set(1, code);
//				rowlist.add(4, ecode);
//				rowlist.add(5, "0");
//				return true;
//
//			}
//
//			@Override
//			public int saveDB(String sql, List<Object[]> setters) {
//				return excelReaderService.saveBatchPreparedStatement(sql, setters);
//			}
//		};
//		readerDBx.setShowPrint(false); // 是否同步输出数据
//		ExcelReaderUtil.readExcel(readerDBx, excelPath);
		
		
		SectionNumberSchemaSheetDataHandler readData = new SectionNumberSchemaSheetDataHandler();
		String excelPath = (String) result.getResult();
		ExcelReader.readFirstSheet(excelPath, readData);
		
		readData.surplusDataInsert();
		
		Map<String, Object> map =readData.getState();
		
		@SuppressWarnings("unchecked")
		List<String[]> errors=(List<String[]>)map.get("errors");
		
		User currentUser = WebUtils.getCurrentUser(request, response);
		String downloadKey = currentUser.getId() + UUID.randomUUID().toString();
		sectionNumberService.redisSaveDownloadError(downloadKey, errors);
		map.put("sum", map.get("sum"));
		map.put("fail",  map.get("fail"));
		map.put("downloadKey", downloadKey);
		map.put("success", map.get("success"));
		log.info("user : " + currentUser.getUsername() + " import section number");
		manageUserOperLogService.saveLog("公共服务", "精准号段", currentUser, "导入精准号段", "新增");
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}

	/**
	 * 错误信息导出
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String downloadKey = request.getParameter("downloadKey");
		String value = sectionNumberService.redisGetDownloadErrorInfo(downloadKey);
		if (null == value) {
			ResponseUtils.outputWithJson(response, Result.badResult("信息不存在！"));
			return;
		}
		// json解析
		List<String[]> errors = JsonHelper.fromJson(new TypeToken<ArrayList<String[]>>() {
		}, value);
		ExcelBean bean = new ExcelBean();
		bean.addSheet(0, "精确号段");
		for (String[] str : errors) {
			bean.setRowContentBySheet(0, str);
		}
		String headStr = "attachment;filename=sectionNumber.xlsx";
		response.setHeader("Content-Disposition", headStr);
		response.setCharacterEncoding("utf-8");
		ExcelHelper.writeExcelInOutputStream(bean, false, true, response.getOutputStream());
	}

}
