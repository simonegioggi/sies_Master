<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.decodifiche.model.ComuneProvinciaModel" %>
<jsp:useBean id="ProvList" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Province</title>
  </head>

  <body class="corpo" onload="focus();">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f">

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.soggetto.action.ActRicercaSoggetto">
    <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
    <input type="HIDDEN" name="fieldname" value="<%=request.getParameter("fieldname")%>">
    <table>
      <tr>

      </tr>
    </table>

    <br>
	<table>
	<tr>
		<td class=LBG>Seleziona Una Provincia</td>
		<td>
		<select name="provincia">
		<%
		Iterator itx = ProvList.iterator();
	  	while ( itx.hasNext())
	  	{
	    	ComuneProvinciaModel prov = (ComuneProvinciaModel)itx.next();
		%>
		<option value="<%=prov.getCodProvincia()%>"><%= prov.getProvincia() %></option>
		<%
		}

		%>
		</select>
		</td>
		<td><input type="submit" name="go" value="Visualizza >>"></td>
	</tr>
	</table>
	</form>
  </body>
</html>