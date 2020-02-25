<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.verbale.model.VerbaleModel"%>
<%@ page import="siap.sico.cssa.model.CSSAModel"%>

<jsp:useBean id="dettaglioProvvedimento" scope="request" class="java.lang.String"/>
<jsp:useBean id="verbale" scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="cssa" scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="posizionegiu" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="misuraposold" scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="misuraalternativa" scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>


<html>
<head>
<title>[S.I.E.S.] - Dettaglio Verbale Sottoscrizione </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>

<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Verbale Sottoscrizione</font>
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
				<td class="l">Data di firma del Verbale</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
		</tr>

		<tr>
				<td class="l">UEPE che ha inviato il verbale</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(cssa.getComune())+ " " +StringUtils.toStringJSP(cssa.getIndirizzo())%></font></td>
		</tr>
<%if(verbale.getNote()!= null){%>
		<tr>
				<td class="l">Indirizzo</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getNote()) %>&nbsp;</font></td>
    </tr>
<%}%>
<% if(posizionegiu.getCodPosizioneGiuridica()!= null &&(!posizionegiu.isLibero()))
  {%>

<table>

     <tr>
        <td class="Titolo" colspan=10><font  class="label">Misura Alternativa Concessa</font></td>
	   </tr>

    <tr>
				<td class="l">Anni</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraposold.getNumAnniMisura())%></font></td>

				<td class="l">Mesi</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraposold.getNumMesiMisura())%></font></td>

      	<td class="l">Giorni</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraposold.getNumGiorniMisura())%></font></td>

        <td class="l">Data Inizio</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraposold.getDataInizioMisura(),"dd/MM/yyyy"))%></font></td>
				<td class="l">Data Fine</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraposold.getDataFineMisura(),"dd/MM/yyyy"))%></font></td>

		</tr>

</table>
<%}else
  {
    if(!dettaglioProvvedimento.equals("SI"))
   {%>
    <tr>
       <td>
        <INPUT  class="bottone" type="submit" name="CALCOLA" value="Calcola Fine Pena" onClick="">
       </td>
     </tr>
 <%}
  }
 %>

		</table>
     <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.verbale.action.ActCalcoloPenaVerbaleSottoscrizione">
     <input type="hidden" name="GiornoInizio" value="<%=DateUtils.getDayToString(verbale.getDataEmissione())%>">
     <input type="hidden" name="MeseInizio"   value="<%=DateUtils.getMonthToString(verbale.getDataEmissione())%>">
     <input type="hidden" name="AnnoInizio"   value="<%=DateUtils.getYearToString(verbale.getDataEmissione())%>">

     <input type="hidden" name="ggpervenimento"  value="<%=DateUtils.getDayToString(verbale.getDataPervenimento())%>">
     <input type="hidden" name="mmpervenimento"  value="<%=DateUtils.getMonthToString(verbale.getDataPervenimento())%>">
     <input type="hidden" name="aapervenimento"  value="<%=DateUtils.getYearToString(verbale.getDataPervenimento())%>">
     <input type="hidden" name="cssacomune"      value="<%=StringUtils.toStringJSP(cssa.getComune())%>">
     <input type="hidden" name="cssaindirizzo"   value="<%=StringUtils.toStringJSP(cssa.getIndirizzo())%>">
     <input type="hidden" name="cssa"            value="<%=StringUtils.toStringJSP(cssa.getIdCSSA())%>">
     <input type="hidden" name="Note"            value="<%=StringUtils.toStringJSP(verbale.getNote())%>">
     <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
     <%--input type="hidden" name="codposizionegiu" value="<%=posizionegiu.getIdPosizioneGiuridica()%>"--%>

  </form>
  </body>
</html>