package cn.emay.eucp.web.manage.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.constant.Operator;
import cn.emay.eucp.common.constant.Province;
import cn.emay.eucp.common.moudle.db.system.SectionNumber;
import cn.emay.eucp.data.service.system.ExcelReaderService;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.excel.common.schema.base.ColumnSchema;
import cn.emay.excel.common.schema.base.SheetReadSchemaParams;
import cn.emay.excel.common.schema.base.SheetSchema;
import cn.emay.excel.read.handler.SchemaSheetDataHandler;
import cn.emay.util.PropertiesUtil;

public class SectionNumberSchemaSheetDataHandler implements SchemaSheetDataHandler<SectionNumber> {
	private static ExcelReaderService excelReaderService = BeanFactoryUtils.getBean(ExcelReaderService.class);
	private static long size = PropertiesUtil.getLongProperty("import.size", "eucp.service.properties", 2000);
	List<SectionNumber> list = new ArrayList<SectionNumber>();
	Map<String, Object> map = new HashMap<String, Object>();
	List<String[]> errors = new ArrayList<String[]>();
	Long succInsertLength = 0L;
	long count = 0;

	@Override
	public void handle(int rowIndex, SectionNumber data) {
		if (rowIndex > 0) {
			map.put("sum", ++count);
			String[] array = { data.getNumber(), data.getOperatorCode(), data.getProvinceName(), data.getCity() };
			String checkData = checkData(data.getNumber(), data.getOperatorCode(), data.getProvinceName());
			if (!RegularUtils.isEmpty(checkData)) {
				errors.add(RegularUtils.copyOfRange(array, checkData));
			} else {
				String operatorCode = Operator.findCodeByName(data.getOperatorCode());
				String provinceCode = Province.findCodeByName(data.getProvinceName());
				data.setOperatorCode(operatorCode);
				data.setProvinceCode(provinceCode);
				data.setIsDelete(false);
				list.add(data);
				succInsertLength++;
			}
		}

		if ((rowIndex + 1) % size == 0) {
			excelReaderService.saveBatchList(list, "system_section_number", true, false);
			list.clear();
		}
	}

	@Override
	public Class<SectionNumber> getDataClass() {
		return SectionNumber.class;
	}

	@Override
	public SheetSchema getSheetSchema() {
		SheetReadSchemaParams sheetReadSchemaParams = new SheetReadSchemaParams();
		Map<String, ColumnSchema> columnSchemaByFieldNames = new HashMap<String, ColumnSchema>();
		columnSchemaByFieldNames.put("number", new ColumnSchema(0, "号码段", null));
		columnSchemaByFieldNames.put("operatorCode", new ColumnSchema(1, "运营商", null));
		columnSchemaByFieldNames.put("provinceName", new ColumnSchema(2, "省份", null));
		columnSchemaByFieldNames.put("city", new ColumnSchema(3, "城市", null));
		return new SheetSchema(sheetReadSchemaParams, columnSchemaByFieldNames);
	}

	public String checkData(String number, String operatorName, String provinceName) {
		if (StringUtils.isEmpty(number)) {
			return "号段不能为空";
		}
		if (!RegularUtils.checkSNumber(number)) {
			return "请输入以1开头的7位数字";
		}
		if (number.length() < 7) {
			return "号段号为7位";
		}
		
		if (StringUtils.isEmpty(operatorName)) {
			return "运营商不能为空";
		}
		String ecode = Operator.findCodeByName(operatorName);
		if (!StringUtils.isEmpty(operatorName) && null == ecode) {
			return "输入运营商不正确";
		}
		String code = Province.findCodeByName(provinceName);
		if (StringUtils.isEmpty(code)) {
			return "省份输入错误";
		}
		return null;
	}
	
	public void surplusDataInsert() {
		if (list.size() > 0) {
			excelReaderService.saveBatchList(list, "system_section_number", true, false);
			list.clear();
		}
	}
	
	public Map<String, Object> getState() {
		map.put("errors", errors);
		map.put("fail", (errors.size()));
		map.put("success", succInsertLength);
		return map;
	}

}
