<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
      String result = doAction (request, response);
      out.println (result);
%>

<%!
private String doAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
	String idUdienza = request.getParameter ("idUdienza");
	return idUdienza;
}
%>