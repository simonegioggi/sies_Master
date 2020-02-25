<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<%@ page import="siap.regesies.regenotiziareato.action.ICostantiRegeNotiziaReato" %>
<%@ page import="siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel" %>

<%@ page import="siap.regesies.action.ICostantiRegeSies" %>

<jsp:useBean id="regenotiziareato" scope="request" class="siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Dettaglio notizia reato</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>
<body class="corpo">
<form>
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Dettaglio Rege Notizie di Reato</font></td>
          <%RegeNotiziaReatoModel lNotiziaReato = regenotiziareato;%>
        <td class="LBG">
          <jsp:include page="<%=ICostantiRegeSies.PG_TOOLBAR_REGE_HEADER%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeNotiziaReato.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=lNotiziaReato.getIdFile()%>" />
             <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiRegeNotiziaReato.CAMPO_PROGR_NOTIZIA%>" />
             <jsp:param name="ValoreIdEntitaProvv" value="<%=lNotiziaReato.getProgrNotizia()%>" />
          </jsp:include>
        </td>
    </tr>
  </table>
</form>
  <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/>

<table  cellspacing=2 cellpadding=2>
    <tr><td class=titolo colspan=2>Rege Notizie di Reato</td></tr>
    <tr>
      <td class="l"  width="20%">Progr.</td>  <td class="l">
      <font class="campo"><%=lNotiziaReato.getProgrNotizia()%></font></td>
      </tr>
      <tr>
      <td class="l">Data Pervenimento</td>     <td class="l">
       <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataPervenimento(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      </tr>
      <tr>
      <td class="l">Acq. diretta</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotiziaReato.getAcquisizioneDiretta(),"-")%>&nbsp;</font></td>
      </tr>
      <tr>
      <td class="l">Data Fatto</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataFatto(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      </tr>
      <tr>
      <td class="l">Fonte</td>
      <td class="l"><font class="campo">
                      <%=StringUtils.toStringJSP(lNotiziaReato.getDescrizioneFonte(),"")%> </font>
                      <%if (lNotiziaReato.getDescrizioneFonte()!=null){%>
                      <font class="label">&nbsp;di&nbsp;</font><%}%>
                    <font class="campo"><%=StringUtils.toStringJSP(lNotiziaReato.getDescrComuneFonte(),"")%></font>
                 </td>
      </tr>
      <tr>
      <td class="l">Numero Reg. Autorità</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotiziaReato.getNumRegAutorita(),"-")%>&nbsp;</font></td>
      </tr>
      <tr>
      <td class="l">Luogo Provenienza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotiziaReato.getLuogoProvenienza(),"-")%>&nbsp;</font></td>
      </tr>
      <tr>
      <td class="l">Data Acquisizione</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataAcquisizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      </tr>
      <tr>
      <td class="l">Num. Ricevuta</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotiziaReato.getNumeroRicevuta(),"-")%>&nbsp;</font></td>
      </tr>
</table>
  </body>
</html>