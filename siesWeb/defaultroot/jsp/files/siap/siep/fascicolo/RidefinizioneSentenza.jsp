<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>

<jsp:useBean id="sentenza" scope="session" class="siap.siep.sentenza.model.SentenzaModel"/>

<head>
  <title> [S.I.E.S.] - Ridefinizione <%=sentenza.getDescrTipoProvvedimento().toLowerCase()%> - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
</head>

<body class="corpo" onLoad="document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.fascicolo.action.ActRidefinizioneSentenza">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_SEN_ID_SENTENZA%>" value="<%=sentenza.getIdSentenza()%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;<font class="campo">Ridefinizione <%=sentenza.getDescrTipoProvvedimento().toLowerCase()%></font></td>
    </tr>
  </table>

  <br>

<table cellspacing=0 cellpadding=0 width=95%>
	<tr>
      	<td class="L">
			<font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
        	<font class="campo">
        		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="<%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%>">
           			<%=StringUtils.toStringJSP(sentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%>
      			</a>&nbsp;
          		<font class="label">del</font>&nbsp;
            	<%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
        	</font>
<%
if (!sentenza.getCodTipoProvvedimento().equals("02")) {
%>
			&nbsp;<font class="label"> Emessa da: </font>
<% 
} else {
%>
			&nbsp;<font class="label"> Emesso da: </font>
<%
}
%>     
        	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
<%
if (sentenza.getNumSezioneAutoritaEmittente() != null) {
%>
          	<font class="label">(Sez.</font> <font class="campo"><%=StringUtils.toStringJSP(sentenza.getNumSezioneAutoritaEmittente())%> </font> <font class="label">) </font>
<%
}
%>
			<font class="label"> di </font>
        	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%></font>
<%
if (sentenza.getAnnoRegeGip() != null) {
%>
          	&nbsp;<font class="label"> (N.Reg.Gen. </font>
          	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGip())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeGip())%></font>
          	<font class="label"> GIP) </font>
<%
} else {
	if (sentenza.getAnnoRegeDib() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeDib())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeDib())%></font>
			<font class="label"> DIB) </font>
<%
	// MEV_66: aggiunte quattro nuove proprietà
	} else if (sentenza.getAnnoRegeGup() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGup())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeGup())%></font>
			<font class="label"> GUP) </font>
<%
	} else if (sentenza.getAnnoRegeCapsm() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCapsm())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCapsm())%></font>
			<font class="label"> CAPSM) </font>
<%
	// MEV_66: aggiunti anche CAS, CAP e CASAP
	} else if (sentenza.getAnnoRegeCap() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCap())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCap())%></font>
			<font class="label"> CAP) </font>
<%
	} else if (sentenza.getAnnoRegeCas() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCas())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCas())%></font>
			<font class="label"> CAS) </font>
<%
	} else if (sentenza.getAnnoRegeCasap() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCasap())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCasap())%></font>
			<font class="label"> CASAP) </font>
<%
	}
}
%>
		</td>
    </tr>
</table>
<br>
  <table cellpadding=2 cellspacing=2>
    <tr>
      <td class="L"> Anno/Numero SIEP
        <input type="text" title="Anno" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero SIEP" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" maxlength="6" size="6">
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <br><br>
        <input class="bottone" type="submit" name="CONFERMA" value="CONFERMA">
      </td>
    </tr>    
  </table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","req","Il campo Numero Procedimento è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","maxlen=6","La lunghezza massima per il Numero Procedimento è di 6 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","numeric");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","req","Il campo Anno Procedimento è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","numeric");
</script>
</body>
</html>