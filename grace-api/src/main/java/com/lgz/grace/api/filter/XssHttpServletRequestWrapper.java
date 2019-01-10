package com.lgz.grace.api.filter;


import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}


	@Override
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = dealString(values[i]);
		}
		return encodedValues;
	}

    @Override
    public Object getAttribute(String name) {
        Object value = super.getAttribute(name);
        if (value != null && value instanceof String) {
            dealString((String) value);
        }
        return value;
    }


	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		return dealString(value);
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		return dealString(value);
	}


	private String dealString(String value) {
		if (value != null) {
            value = StringEscapeUtils.escapeHtml4(value);
            return value;
		}
		return value;
	}
}