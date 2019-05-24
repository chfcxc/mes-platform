package cn.emay.eucp.task.multiple.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;

import cn.emay.eucp.common.dto.fms.page.PageSendParsingDto;
import cn.emay.excel.read.reader.SheetReader;
import cn.emay.excel.utils.ExcelReadUtils;

public class PageSendDTOReader implements SheetReader {

	private List<PageSendParsingDto> list = new ArrayList<PageSendParsingDto>();
	private List<String> titles = new ArrayList<String>();
	private Map<Integer, String> titleMap = new HashMap<Integer, String>();
	private Set<String> needTitles = new HashSet<String>();// 需要的列

	private PageSendParsingDto curr;

	@Override
	public int getStartReadRowIndex() {
		return 0;
	}

	@Override
	public int getEndReadRowIndex() {
		return -1;
	}

	@Override
	public void begin(int sheetIndex, String sheetName) {

	}

	@Override
	public void beginRow(int rowIndex) {
		curr = new PageSendParsingDto();
		curr.setData(new HashMap<String, String>());
		curr.setValue("value:");
	}

	@Override
	public void handleXlsxCell(int rowIndex, int columnIndex, int formatIndex, String value) {
		if (rowIndex == 0) {
			String title = ExcelReadUtils.readString(value);
			titles.add(title);
			if (needTitles != null && needTitles.contains(title)) {
				titleMap.put(columnIndex, title);
			}
		} else {
			if (columnIndex == 0) {// 第一列为手机号
				curr.setMobile(ExcelReadUtils.readString(value));
			} else if (titleMap.get(columnIndex) != null) {
				String title = titleMap.get(columnIndex);
				String dataValue = ExcelReadUtils.readString(value);
				curr.getData().put(title, dataValue);
				curr.setValue(curr.getValue() + "," + dataValue);
			}
		}
	}

	@Override
	public void handleXlsCell(int rowIndex, int columnIndex, Cell cell) {
		if (rowIndex == 0) {
			String title = ExcelReadUtils.readString(cell);
			titles.add(title);
			if (needTitles != null && needTitles.contains(title)) {
				titleMap.put(columnIndex, title);
			}
		} else {
			if (columnIndex == 0) {
				curr.setMobile(ExcelReadUtils.readString(cell));
			} else if (titleMap.get(columnIndex) != null) {
				String title = titleMap.get(columnIndex);
				String dataValue = ExcelReadUtils.readString(cell);
				curr.getData().put(title, dataValue);
				curr.setValue(curr.getValue() + "," + dataValue);
			}
		}
	}

	@Override
	public void endRow(int rowIndex) {
		if (rowIndex > 0 && curr != null) {
			list.add(curr);
		}
	}

	@Override
	public void end(int sheetIndex, String sheetName) {

	}

	public List<PageSendParsingDto> getDatas() {
		return list;
	}

	public List<String> getTitles() {
		return titles;
	}

	public Set<String> getNeedTitles() {
		return needTitles;
	}

	public void setNeedTitles(Set<String> needTitles) {
		this.needTitles = needTitles;
	}

}
