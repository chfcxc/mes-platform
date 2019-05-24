package cn.emay.eucp.web.util.excel;

import org.apache.poi.poifs.filesystem.OfficeXmlFileException;

public class OnlyReadFirstRowWithXlsException extends OfficeXmlFileException {

	private static final long serialVersionUID = 1L;

	public OnlyReadFirstRowWithXlsException(String error) {
		super(error);
	}
}
