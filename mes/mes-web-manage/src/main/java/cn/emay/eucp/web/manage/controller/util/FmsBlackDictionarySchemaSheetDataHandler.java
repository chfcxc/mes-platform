package cn.emay.eucp.web.manage.controller.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.moudle.db.fms.FmsBlackDictionary;
import cn.emay.eucp.data.service.fms.BatchInsertEntityService;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.excel.common.schema.base.ColumnSchema;
import cn.emay.excel.common.schema.base.SheetReadSchemaParams;
import cn.emay.excel.common.schema.base.SheetSchema;
import cn.emay.excel.read.handler.SchemaSheetDataHandler;
import cn.emay.util.PropertiesUtil;

/** @author dejun
 * @version 创建时间：2019年5月7日 下午5:00:42 类说明 */
public class FmsBlackDictionarySchemaSheetDataHandler implements SchemaSheetDataHandler<FmsBlackDictionary> {
	private static BatchInsertEntityService batchInsertEntityService = BeanFactoryUtils.getBean(BatchInsertEntityService.class);
	private static long size = PropertiesUtil.getLongProperty("import.size", "eucp.service.properties", 2000);
	List<FmsBlackDictionary> list = new ArrayList<FmsBlackDictionary>();
	Long userId;
	Map<String, Object> map = new HashMap<String, Object>();
	List<String[]> errors = new ArrayList<String[]>();
	Long succInsertLength = 0L;
	long count = 0;
	long repeated = 0;
	Map<String, Object> contentRepert = new HashMap<String, Object>();

	public FmsBlackDictionarySchemaSheetDataHandler(Long userId) {
		this.userId = userId;
	}

	public FmsBlackDictionarySchemaSheetDataHandler() {
		super();
	}

	@Override
	public void handle(int rowIndex, FmsBlackDictionary data) {
		if (rowIndex > 0) {
			map.put("sum", ++count);
			String[] array = new String[1];
			data.setCreateTime(new Date());
			data.setIsDelete(0);
			data.setUserId(userId);
			String checkData = checkData(data.getContent(), data.getRemark());
			if (!RegularUtils.isEmpty(checkData)) {
				errors.add(RegularUtils.copyOfRange(array, checkData));
				array[0] = checkData;
			}
			if (array[0] == null) {
				if (!contentRepert.containsKey(data.getContent())) {
					contentRepert.put(data.getContent(), 1);
					list.add(data);
					succInsertLength++;
				} else {
					repeated++;
				}
			}
			if ((rowIndex + 1) % size == 0) {
				batchInsertEntityService.saveBatchList(list, "fms_blacklist", true, false);
				list.clear();
			}
		}
	}

	public void batchInsert() {
		if (list.size() > 0) {
			batchInsertEntityService.saveBatchList(list, null, true, false);
			list.clear();
		}
	}

	public Map<String, Object> getState() {
		map.put("errors", errors);
		map.put("fail", (errors.size()));
		map.put("success", succInsertLength);
		map.put("repeated", repeated);
		return map;
	}

	@Override
	public Class<FmsBlackDictionary> getDataClass() {
		return FmsBlackDictionary.class;
	}

	@Override
	public SheetSchema getSheetSchema() {
		SheetReadSchemaParams sheetReadSchemaParams = new SheetReadSchemaParams();
		Map<String, ColumnSchema> columnSchemaByFieldNames = new HashMap<String, ColumnSchema>();
		columnSchemaByFieldNames.put("content", new ColumnSchema(0, "内容", null));
		columnSchemaByFieldNames.put("remark", new ColumnSchema(1, "创建原因", null));
		return new SheetSchema(sheetReadSchemaParams, columnSchemaByFieldNames);
	}

	private String checkData(String content, String remark) {
		if (RegularUtils.isEmpty(content)) {
			return "内容不能为空";
		}
		if (content.length() > 50) {
			return "内容输入过长，不能大于50字";
		}
		// FmsBlackDictionary name = fmsBlackDictionaryService.findbyName(content);
		// if (name != null) {
		// return "黑字典内容已存在";
		// }
		if (!RegularUtils.isEmpty(remark) && remark.length() > 50) {
			return "创建原因不能超过50个字符";
		}
		return null;
	}

}
