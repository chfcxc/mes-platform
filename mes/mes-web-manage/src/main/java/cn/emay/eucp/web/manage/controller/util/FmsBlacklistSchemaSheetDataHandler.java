package cn.emay.eucp.web.manage.controller.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.moudle.db.fms.FmsBlacklist;
import cn.emay.eucp.common.util.RegularCheckUtils;
import cn.emay.eucp.data.service.fms.BatchInsertEntityService;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.excel.common.schema.base.ColumnSchema;
import cn.emay.excel.common.schema.base.SheetReadSchemaParams;
import cn.emay.excel.common.schema.base.SheetSchema;
import cn.emay.excel.read.handler.SchemaSheetDataHandler;
import cn.emay.util.PropertiesUtil;

/** @author dejun
 * @version 创建时间：2019年5月7日 下午5:00:42 类说明 */
public class FmsBlacklistSchemaSheetDataHandler implements SchemaSheetDataHandler<FmsBlacklist> {
	private static BatchInsertEntityService batchInsertEntityService = BeanFactoryUtils.getBean(BatchInsertEntityService.class);
	private static long size = PropertiesUtil.getLongProperty("import.size", "eucp.service.properties", 2000);
	List<FmsBlacklist> list = new ArrayList<FmsBlacklist>();
	Long userId;
	Map<String, Object> map = new HashMap<String, Object>();
	List<String[]> errors = new ArrayList<String[]>();
	Long succInsertLength = 0L;
	long count = 0;
	long repeated = 0;
	Map<String, Object> mobileRepert = new HashMap<String, Object>();
	// long time = 0;

	public FmsBlacklistSchemaSheetDataHandler(Long userId) {
		this.userId = userId;
	}

	public FmsBlacklistSchemaSheetDataHandler() {
		super();
	}

	@Override
	public void handle(int rowIndex, FmsBlacklist data) {
		if (rowIndex > 0) {
			map.put("sum", ++count);
			String[] array = new String[1];
			data.setCreateTime(new Date());
			data.setIsDelete(0);
			data.setUserId(userId);
			String checkData = checkData(data.getMobile(), data.getRemark());
			if (!RegularUtils.isEmpty(checkData)) {
				errors.add(RegularUtils.copyOfRange(array, checkData));
				array[0] = checkData;
			}
			if (array[0] == null) {
				if (!mobileRepert.containsKey(data.getMobile())) {
					mobileRepert.put(data.getMobile(), 1);
					list.add(data);
					succInsertLength++;
				} else {
					repeated++;
				}
			}
			if ((rowIndex + 1) % size == 0) {
				// long time1 = System.currentTimeMillis();
				batchInsertEntityService.saveBatchList(list, "fms_blacklist", true, false);
				// System.out.println("1000条消耗时长：" + (System.currentTimeMillis() - time1));
				// time = time + (System.currentTimeMillis() - time1);
				list.clear();
			}
		}
	}

	public void batchInsert() {
		if (list.size() > 0) {
			// long time1 = System.currentTimeMillis();
			batchInsertEntityService.saveBatchList(list, null, true, false);
			// System.out.println("最后1000条消耗时长：" + (System.currentTimeMillis() - time1));
			// time = time + (System.currentTimeMillis() - time1);
			// System.out.println("总时长:" + time);
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
	public Class<FmsBlacklist> getDataClass() {
		return FmsBlacklist.class;
	}

	@Override
	public SheetSchema getSheetSchema() {
		SheetReadSchemaParams sheetReadSchemaParams = new SheetReadSchemaParams();
		Map<String, ColumnSchema> columnSchemaByFieldNames = new HashMap<String, ColumnSchema>();
		columnSchemaByFieldNames.put("mobile", new ColumnSchema(0, "手机号", null));
		columnSchemaByFieldNames.put("remark", new ColumnSchema(1, "创建原因", null));
		return new SheetSchema(sheetReadSchemaParams, columnSchemaByFieldNames);
	}

	public String checkData(String mobile, String remark) {
		if (!RegularCheckUtils.checkMobile(mobile)) {
			return "手机号格式错误";
		}
		if (remark != null) {
			if (remark.length() > 50) {
				return "备注不能超过50字";
			}
		}
		// FmsBlacklist global = fmsBlacklistService.findbymobile(mobile, 0L);
		// if (global != null) {
		// return "黑名单已存在";
		// }
		return null;
	}

}
