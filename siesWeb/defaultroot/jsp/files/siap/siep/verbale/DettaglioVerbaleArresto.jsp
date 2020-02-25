<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.verbale.model.VerbaleModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>

<jsp:useBean id="verbale" scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="luogodetenzione" scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Verbale Arresto </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>


<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Verbale Arresto</font>
      </td>
   </tr>

 </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
</FORM>
<form name="DettaglioVerbaleArresto" method="POST" action="/jsp/Main.jsp">
		 <table cellspacing=4 cellpadding=4>


		<tr>
				<td class="l">Data Pervenimento del Verbale</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataPervenimento(),"dd-MM-yyyy"))%> </font></td>
		</tr>
		<tr>
				<td class="l">Data di Arresto / decorrenza pena</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
		</tr>
		<tr>
				<td class="l">Autorità che ha Proceduto all'Arresto</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrTipoUfficioFirmatario()) %></font></td>
		</tr>
		<tr>
				<td class="l">Luogo Autorità</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario()) %></font></td>
		</tr>
		<tr>
				<td class="l">Istituto di Detenzione</td>
<%//modifica relativa al tipo istituto%>
			<td class="l"><font class="campo"><%=luogodetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			<!--/td>
		</tr>
		<tr>
			<td class="l">Luogo Detenzione</td>
			<td class="l"-->
			<font class="campo"><%=StringUtils.toStringJSP(luogodetenzione.getIstitutoDetenzione().getDescrComune()) %></font></td>
		</tr>
<%//fine modifica relativa al tipo istituto%>
		<tr>
				<td class="l">Note</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getNote()) %>&nbsp;</font></td>
    </tr>

    <tr>
       <td>
        <INPUT  class="bottone" type="submit" name="CALCOLA" value="Calcola Fine Pena" onClick="">
       </td>
     </tr>

		</table>
     <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.verbale.action.ActCalcoloPenaVerbaleArresto">
     <input type="hidden" name="GiornoInizio" value="<%=DateUtils.getDayToString(verbale.getDataEmissione())%>">
     <input type="hidden" name="MeseInizio"   value="<%=DateUtils.getMonthToString(verbale.getDataEmissione())%>">
     <input type="hidden" name="AnnoInizio"   value="<%=DateUtils.getYearToString(verbale.getDataEmissione())%>">

     <input type="hidden" name="ggpervenimento"  value="<%=DateUtils.getDayToString(verbale.getDataPervenimento())%>">
     <input type="hidden" name="mmpervenimento"  value="<%=DateUtils.getMonthToString(verbale.getDataPervenimento())%>">
     <input type="hidden" name="aapervenimento"  value="<%=DateUtils.getYearToString(verbale.getDataPervenimento())%>">
     <input type="hidden" name="autorita"          value="<%=StringUtils.toStringJSP(verbale.getDescrTipoUfficioFirmatario())%>">
     <input type="hidden" name="luogo"             value="<%=StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario())%>">
<%//modifica relativa al tipo istituto%>
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	<%--input type="hidden" name="tipostituto" value="<%=luogodetenzione.getDescrTipoIstituto()%>"--%>
     <input type="hidden" name="tipostituto"       value="<%=luogodetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%>">
     <input type="hidden" name="Luogodetenzione"   value="<%=StringUtils.toStringJSP(luogodetenzione.getIstitutoDetenzione().getDescrComune())%>">
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	<%--input type="hidden" name="Luogodetenzione" value="<%=StringUtils.toStringJSP(luogodetenzione.getDescrLuogo())%>"--%>
<%//fine modifica relativa al tipo istituto%>
     <input type="hidden" name="Note"              value="<%=StringUtils.toStringJSP(verbale.getNote())%>">


  </form>
  </body>
</html>