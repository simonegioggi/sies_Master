<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.regesies.regesentenza.model.RegeSentenzaModel"%>
<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza"%>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="regesentenza" scope="request" class="siap.regesies.regesentenza.model.RegeSentenzaModel"/>

<% RegeSentenzaModel lSentenza = new RegeSentenzaModel(regesentenza);%>

<html>

  <head>
    <title> [S.I.E.S.] - Dettaglio Rege Decreto - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>">
    </script>
  </head>

  <BODY class="corpo">

  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Rege Decreto </font>
        </td>
       <td class="LBG">
          <jsp:include page="<%=ICostantiRegeSies.PG_TOOLBAR_REGE_HEADER%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=lSentenza.getIdFile()%>" />
             <jsp:param name="TornaIndietro" value="SI"/>

           </jsp:include>
        </td>
      </tr>
    </table>
  </FORM>
     <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_SOLO_INCLUDE%>"/>
<br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">Data Arrivo Atto</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
       </tr>
  	<tr>
      <td class="l">Data Inserimento</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataInserimento(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Data Iscrizione</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataIscrizione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
  	<tr>
      <td class="l">Anno/Numero R.G.N.R.</td>
      <td class="L">
        <font class="campo"><%=StringUtils.intZerotoString(lSentenza.getAnnoRegePm())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroRegePm())%></font>&nbsp;
      </td>
   	</tr>
  	<tr>
      <td class="l">Numero Reg.Gen. GIP</td>
      <td class="L">
        <font class="campo"><%=StringUtils.intZerotoString(lSentenza.getAnnoRegeGip())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroRegeGip())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Data Decreto</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Decreto</td>
      <td class="L">
        <font class="campo"><%=StringUtils.intZerotoString(lSentenza.getAnnoSentenza())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Emittente</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
      </td>
		</tr>
   	<tr>
      <td class="l">Luogo Emittente</td>
      <td class="L"  >
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrLuogoEmittente())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Sezione Autorità Emittente</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente())%></font>&nbsp;
      </td>
		</tr>
       <tr>
      <td class="l">Anno/Numero Raccolta Generale</td>
      <td class="L">
        <font class="campo"><%=StringUtils.intZerotoString(lSentenza.getAnnoRaccoltaGenerale())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroRaccoltaGenerale())%></font>&nbsp;
      </td>
		</tr>
    <tr>
      <td class="l">Anno/Numero Sentenza Cass.</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.intZerotoString(lSentenza.getAnnoSentenzaCassazione())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenzaCassazione())%></font>&nbsp;
      </td>
		</tr>
    <tr>
      <td class="l">Data Irrevocabilità</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
  	<tr>
      <td class="l">Note</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote())%></font>&nbsp;
      </td>
		</tr>
  </table>
  <br>
  </body>

</html>