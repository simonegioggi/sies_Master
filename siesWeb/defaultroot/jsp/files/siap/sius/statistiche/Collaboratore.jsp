<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"  %>

<jsp:useBean id="collaboratore" scope="request" class="java.lang.String"/>
<jsp:useBean id="SecondoGiro" scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroCollaboratore" scope="request" class="java.lang.String"/>

 <%
 if (collaboratore != null && collaboratore.length() > 0) {
 
 
 boolean flagSecondo  = false;
 String scelta = "";
 
if(SecondoGiro !=null && SecondoGiro.equals("SI")) 
	flagSecondo = true;

if (filtroCollaboratore != null && filtroCollaboratore.length() > 0)  {
	
	if (filtroCollaboratore.equalsIgnoreCase("SI")) {
			scelta = "Solo Procedimenti collegati a Collaboratore di Giustizia";
	}else{ if (filtroCollaboratore.equalsIgnoreCase("NO"))
			scelta = "Solo Procedimenti non collegati a Collaboratore di Giustizia";

 }} %>     
 <table width="60%">
  <% if (flagSecondo) { %>
  <tr><td class="l"><font class="cred"> <%=scelta%></font></td></tr>
  <% } else {%>
    <tr><td>&nbsp;</td></tr>
      <tr><td class="Titolo" >Tipo di Procedimento</td></tr>
<tr>
<table width="60%">
	<tr><td class="l">Tutti </td> <td class="c"><input type="radio" name="<%=ICostantiStatistiche.FILTRO_COLLABORATORE%>" value="tutti" checked  ></td></tr>
	<tr><td class="l">Procedimenti collegati a Collaboratore </td> <td class="c"><input type="radio" name="<%=ICostantiStatistiche.FILTRO_COLLABORATORE%>" value="SI" ></td></tr>
	<tr><td class="l">Procedimenti non collegati a Collaboratore </td> <td class="c"> <input type="radio" name="<%=ICostantiStatistiche.FILTRO_COLLABORATORE%>" value="NO" ></td></tr>
 <tr><td>&nbsp;</td></tr>	 
  </table> 
  </tr>
  </table> 
<% } 
 if (flagSecondo) { 
  %>
<input type="HIDDEN" name="filtroCollaboratore" value=<%=filtroCollaboratore%>>
<% }} %>