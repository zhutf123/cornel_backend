<%@page import="com.qunar.hotel.qmonitor.QMonitor"%>
<%@ page import="java.util.Map"%>
<%@page contentType="text/plain;charset=UTF-8" language="java"%>
<%
	for (Map.Entry<String, Number> entry : QMonitor.getValues().entrySet()) {
	    String name = entry.getKey();
	    Number value = entry.getValue();
	    out.append(name).append("=").append(String.valueOf(value)).append("\n");
	}
%>