package cn.emay.eucp.web.common.tag;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class TempleWriteTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	public static final String PREFIX = "JspTemplateBlockName_";

	private String property;

	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		BodyContent bodyContent = getBodyContent();
		request.setAttribute(PREFIX + property, bodyContent.getString().trim());
		return super.doEndTag();
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
