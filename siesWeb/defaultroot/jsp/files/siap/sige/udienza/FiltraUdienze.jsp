<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="f3b.web.IWebConstants" %>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige" %>

<%
String lTipoRito = request.getParameter("tipoRito").trim();
String lDescrUdienze = "";
if (lTipoRito.compareTo("C") == 0)
	lDescrUdienze="Collegiali";
if (lTipoRito.compareTo("M") == 0)
	lDescrUdienze="Monocratiche";
%>
<html>
  	<head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/ControllaData.js"></script>
    <script language="JavaScript">
	function Verify() {
      	var ritorno = true;
        var dataUdienzaInizio = document.f.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
               					document.f.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value + '/' +
               					document.f.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
        if (dataUdienzaInizio.length <= 2) {
        	alert('Occorre inserire Data Udienza');
          	ritorno = false;
        } else if (!ControllaData( dataUdienzaInizio)) {
        	alert('Data Udienza  non valida');
          	ritorno = false;
        }
        return ritorno;
	}
	</script>
    <title>[S.I.E.S.] - Filtro Udienze</title>
  	</head>

  <body class="corpo" onload="document.f.submit();">
    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f" target=ListaUdienzePopUp>
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.udienza.action.ActElencoUdienzeDaData">
	<input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
	<input type="HIDDEN" name="campoGG" value="<%=request.getParameter("campoGG")%>">
	<input type="HIDDEN" name="campoMM" value="<%=request.getParameter("campoMM")%>">
	<input type="HIDDEN" name="campoAA" value="<%=request.getParameter("campoAA")%>">
	<input type="HIDDEN" name="campoID" value="<%=request.getParameter("campoID")%>">
	<input type="HIDDEN" name="campoColl" value="<%=request.getParameter("campoColl")%>">
	<input type="HIDDEN" name="campoLuogo" value="<%=request.getParameter("campoLuogo")%>">
	<input type="HIDDEN" name="campoCollegio" value="<%=request.getParameter("campoColl")%>">
	<input type="HIDDEN" name="tipoRito" value="<%=request.getParameter("tipoRito")%>">
   	<table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:parent.ListaUdienzePopUp.focus();parent.ListaUdienzePopUp.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa elenco udienze" border=0>
    			</a>
    		</td>
      		<td class=LBG> <font class="campo"> Visualizza le Udienze &nbsp;<%=lDescrUdienze%>&nbsp;a partire dal: </font></td>
      		<td class="l">
<%
Enumeration lEnum = request.getParameterNames();
String name = "";
String data_sistema = "";
while (lEnum.hasMoreElements()) {
	name = (String)lEnum.nextElement();
	if (name.equals("dataOdierna")) {
		data_sistema =  request.getParameter(name);
	} else if(name.equals("campo_sub")) {
%>
				<input type="HIDDEN" name="campo_sub" value="<%=request.getParameter("campo_sub")%>">
<%
	}
}

// 20171003: [SG] se non è dataSI allora cercare a partire dalla data udienza nella pagina
if (data_sistema.compareTo("dataSI") == 0) {
%>
		        <input  value="<%=DateUtils.getSysDate("dd")%>" type="text" name="<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>" maxlength="2" size="2"
		        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		        /
		        <input  value="<%=DateUtils.getSysDate("MM")%>" type="text" name="<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>" maxlength="2" size="2"
		        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		        /
		        <input  value="<%=DateUtils.getSysDate("yyyy")%>" type="text" name="<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>" maxlength="4" size="4"
		        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
} else {
%>
		        <input  value="<%=request.getParameter("campoGG")%>" type="text" name="<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>" maxlength="2" size="2"
		        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		        /
		        <input  value="<%=request.getParameter("campoMM")%>" type="text" name="<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>" maxlength="2" size="2"
		        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		        /
		        <input  value="<%=request.getParameter("campoAA")%>" type="text" name="<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>" maxlength="4" size="4"
		        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
}
%>
      		</td>
    	</tr>
  	</table>
  	<table>
    	<tr>
      		<td><input onclick="Javascript:return Verify();" type="submit" name="go" value="Visualizza >>"></td>
    	</tr>
  	</table>
	</form>
  	</body>
</html>