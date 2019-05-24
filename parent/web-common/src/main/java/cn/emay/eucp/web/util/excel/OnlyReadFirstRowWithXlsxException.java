package cn.emay.eucp.web.util.excel;

import org.xml.sax.SAXException;

public class OnlyReadFirstRowWithXlsxException extends SAXException {

	private static final long serialVersionUID = 1L;

	public OnlyReadFirstRowWithXlsxException(String error) {
		super(error);
	}
}
