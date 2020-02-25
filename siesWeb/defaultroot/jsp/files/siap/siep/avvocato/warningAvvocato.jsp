<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>

<jsp:useBean id="avvocato"    scope="request" class="siap.siep.avvocato.model.AvvocatoModel"/>

<html>
<head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>F3B - ( Framework Bull Building Blocks ) </title>
    
<!-- INIZIO Definizione Script BOTTONI GRAFICI  -->
<SCRIPT LANGUAGE="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
// preload images:
if (document.images)
{
	clickme1 = new Image(58,17); clickme1.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif";
	clickme2 = new Image(58,17); clickme2.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01-down.gif";
	clickme3 = new Image(58,17); clickme3.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01.gif";
	clickme4 = new Image(58,17); clickme4.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01-down.gif";
}

function hiLite(imgName,imgObjName)
{
  if (document.images)
  {
    document.images[imgName].src = eval(imgObjName + ".src");
  }
}
</SCRIPT>
<!-- Fine Definizione Script BOTTONI GRAFICI -->
<%
	String jumpPage = "history.back()";
	String newPage = (String)request.getAttribute(IWebConstants.GOTO_PAGE);
	if(newPage != null)
	{
		jumpPage = "\""+newPage+"\"";
	}
%>
<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
function vai()
{
 <%
	if(newPage != null)
	  out.print("location=" + jumpPage);
	else
	  out.print("history.back();");
 %>
}
</script>
</head>

<BODY class="corpo">
<%
  String message 	= (String)request.getAttribute(IWebConstants.MESSAGE_TEXT);
	String modalita = (String)request.getAttribute("Modalita");
%>

<br><br><br><br><br>
<table width="300"  cellspacing="0" align="center" class="tab" >
<tr><td class="c">
<form name="warning">
	<table width="300"  cellspacing="0" align="center" class="tab" >
		<tr align="center" valign="middle">
			<td align="center"  colspan="2"  class="tab">
				<p>&nbsp;<p>
				<B><%=message%></B>
				<p>&nbsp;<p>
			</td>
		</tr>
		<tr align="center" valign="middle">
			<td align="center"  class="tab2" width="50%">
      	<a href="javascript:document.warning.submit();" onMouseOver="hiLite('img01','clickme2')" onMouseOut="hiLite('img01','clickme1')">
      		<IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif" BORDER="0" ALT="" NAME="img01">
      	</a>
			</td>
			<td align="center"  class="tab2" width="50%">
      	<a href="javascript:vai();" onMouseOver="hiLite('img02','clickme4')" onMouseOut="hiLite('img02','clickme3')">
      		<IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01.gif" BORDER="0" ALT="" NAME="img02">
      	</a>
			</td>
		</tr>
      <input type="HIDDEN" name="warning" value="S">

      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_COGNOME%>" value="<%=avvocato.getCognome()%>">
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_NOME%>" value="<%=avvocato.getNome()%>">
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_FORO%>"value="<%=avvocato.getForo()%>">

<%if(avvocato.getCodLuogoNascita() != null){%>
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>" value="<%=avvocato.getCodLuogoNascita()%>">
<%}else{%>
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>" value="">

<%}%>
<%if(avvocato.getDataNascita()!= null){%>
    <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>" value="<%=DateUtils.getYearToString(avvocato.getDataNascita())%>">
    <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>" value="<%=DateUtils.getMonthToString(avvocato.getDataNascita())%>">
    <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>" value="<%=DateUtils.getDayToString(avvocato.getDataNascita())%>">
<%}else{%>
    <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>" value="">
    <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>" value="">
    <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>" value="">
<%}%>
<%if(avvocato.getIndirizzo()!= null){%>

 <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>" value="<%=avvocato.getIndirizzo()%>">
<%}else{%>
  <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>" value="">

<%}%>
<%if(avvocato.getCodComuneResidenza()!= null){
%>
   <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA%>"value="<%=avvocato.getCodComuneResidenza()%>">
<%}else{%>
  <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA%>" value="">

<%}%>
<%if(avvocato.getTelefono()!= null){%>

      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_TELEFONO%>" value="<%=avvocato.getTelefono()%>">
<%}else{%>
  <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_TELEFONO%>" value="">

<%}%>
<%if(avvocato.getFax()!= null){%>
  <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_FAX%>" value="<%=avvocato.getFax()%>">
<%}else{%>
  <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_FAX%>" value="">

<%}%>
<%if(avvocato.getEMail()!= null){%>
     <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_E_MAIL%>" value="<%=avvocato.getEMail()%>">
<%}else{%>
     <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_E_MAIL%>" value="">
     
<%}%>     
<%if(avvocato.getCodiceFiscale()!= null){%>
     <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>" value="<%=avvocato.getCodiceFiscale()%>">
<%}else{%>
     <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>" value="">

<%}%>
<%if(avvocato.getDataSospensione()!= null){%>
     <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_SOSPENSIONE%>" value="<%=DateUtils.getDayToString(avvocato.getDataSospensione())%>">
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_MESE_DATA_SOSPENSIONE%>" value="<%=DateUtils.getMonthToString(avvocato.getDataSospensione())%>">
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ANNO_DATA_SOSPENSIONE%>" value="<%=DateUtils.getYearToString(avvocato.getDataSospensione())%>">
<%}else{%>
     <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_SOSPENSIONE%>" value="">
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_MESE_DATA_SOSPENSIONE%>" value="">
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ANNO_DATA_SOSPENSIONE%>" value="">


<%}%>
<%if(avvocato.getDataRadiazione()!= null){%>
     <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_RADIAZIONE%>" value="<%=DateUtils.getDayToString(avvocato.getDataRadiazione())%>">
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_MESE_DATA_RADIAZIONE%>" value="<%=DateUtils.getMonthToString(avvocato.getDataRadiazione())%>">
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ANNO_DATA_RADIAZIONE%>" value="<%=DateUtils.getYearToString(avvocato.getDataRadiazione())%>">
<%}else{%>
     <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_RADIAZIONE%>" value="">
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_MESE_DATA_RADIAZIONE%>" value="">
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ANNO_DATA_RADIAZIONE%>" value="">
<%}%>
<%if(avvocato.getCodNonAttivita()!= null){%>

      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>" value="<%=avvocato.getCodNonAttivita()%>">
<%}else{%>
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>" value="">
<%}%>
<%if(avvocato.getCodUffAppartenenza()!= null){%>
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_COD_UFFICIO_APPARTENENZA%>" value="<%=avvocato.getCodUffAppartenenza()%>">
<%}else{%>
      <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_COD_UFFICIO_APPARTENENZA%>" value="">
<%}%>
<!--aggiunto warning_2 per gestire 2 warning sulla stessa pagina-->

      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=(String)request.getAttribute(IWebConstants.ACTION_FIELD)%>">
		</tr>
	</table>
</form>
			</td>
		</tr>
	</table>
</body>
</html>