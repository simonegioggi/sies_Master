<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
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

<jsp:useBean id="regenotizieReati" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca notizia reato</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>
<body class="corpo">
<form>
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Rege Notizie di Reato</font>
        </td>
    </tr>
  </table>
</form>
  <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/>
<div align=center>
<table >
    <tr><td class=titolo colspan=13>Rege Notizie di Reato</td></tr>
    <tr>
      <td class="int">Progr.</td>
      <td class="int">Data Pervenimento</td>
      <td class="int">Acq. diretta</td>
      <td class="int">Data Fatto</td>
      <td class="int">Fonte</td>
      <td class="int">Numero Reg. Autorità</td>
      <td class="int">Luogo Provenienza</td>
      <td class="int">Data Acquisizione</td>
      <td class="int">Num. Ricevuta</td>

      <td class="int" width=5%>Azioni</td>
   </tr>
 <%
  if(regenotizieReati!=null && regenotizieReati.size()>0)
   {
    Iterator lIternotizieReati = regenotizieReati.iterator();

    while(lIternotizieReati.hasNext())
      {
          RegeNotiziaReatoModel lNotiziaReato = (RegeNotiziaReatoModel)lIternotizieReati.next();
      %>     <tr>
       <td class="l">
      <font class="label"><%=lNotiziaReato.getProgrNotizia()%></font></td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataPervenimento(),"dd-MM-yyyy"),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(lNotiziaReato.getAcquisizioneDiretta(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataFatto(),"dd-MM-yyyy"),"-")%>&nbsp;</td>
      <td class="l">
                      <font class="label"><%=StringUtils.toStringJSP(lNotiziaReato.getDescrizioneFonte(),"")%> </font>
                      <%if(lNotiziaReato.getDescrizioneFonte()!=null){%>
                      &nbsp;di&nbsp;<%}%>
                    <%=StringUtils.toStringJSP(lNotiziaReato.getDescrComuneFonte())%>
                 </td>
      <td class="l"><%=StringUtils.toStringJSP(lNotiziaReato.getNumRegAutorita(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(lNotiziaReato.getLuogoProvenienza(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataAcquisizione(),"dd-MM-yyyy"),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(lNotiziaReato.getNumeroRicevuta(),"-")%>&nbsp;</td>
      <td class="c">
          <jsp:include page="<%=ICostantiRegeSies.PAGE_BUTTONS_REGE%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeNotiziaReato.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=lNotiziaReato.getIdFile()%>" />
             <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiRegeNotiziaReato.CAMPO_PROGR_NOTIZIA%>" />
             <jsp:param name="ValoreIdEntitaProvv" value="<%=lNotiziaReato.getProgrNotizia()%>" />
         </jsp:include>
      </td>
      </tr>
<%}
}%>
</table>
</div>
  </body>
</html>