package cn.emay.eucp.web.common.tag;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class TempleBlockTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	private String property;

	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		String bodyContent = (String) request.getAttribute(TempleWriteTag.PREFIX + property);
		bodyContent = bodyContent == null ? "" : bodyContent;
		try {
			pageContext.getOut().write(bodyContent);
		} catch (IOException e) {
			throw new JspException(e.getMessage(), e);
		}
		return super.doEndTag();
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
