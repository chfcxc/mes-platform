package cn.emay.eucp.web.common.tag;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class AuthTag  extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	private String property;

	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		Object obj = request.getAttribute(property);
		if(obj != null){
			BodyContent bodyContent = getBodyContent();
			try {
				pageContext.getOut().write(bodyContent.getString().trim());
			} catch (IOException e) {
				throw new JspException(e.getMessage(), e);
			}
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
