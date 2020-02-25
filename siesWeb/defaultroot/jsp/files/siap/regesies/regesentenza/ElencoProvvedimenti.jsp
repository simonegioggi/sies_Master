<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza" %>
<%@ page import="siap.regesies.regesentenza.model.RegeSentenzaModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="sentenze" scope="request" class="java.util.Vector" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Provvedimento Rege</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" >

      function SubmitElencoProvvedimenti(nomeForm, count)
      {
        aForm = document.getElementById(nomeForm);
        aForm.<%=ICostantiRegeSentenza.CAMPO_PROGR_PROVVEDIMENTO%>.value = count;

        aForm.submit();
     }

      </script>

  </head>

  <BODY class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Elenco Provvedimenti ReGe</font></td>
      </tr>
    </table>

    <br>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

<br>
<br>

  <table cellpadding=2 cellspacing=2>
        <td class="int">Data Tit.Esecutivo</td>
        <td class="int">Tipo Tit.Esecutivo</td>
        <td class="int">Anno/Numero</td>
        <td class="int">Autorità</td>
        <td class="int">Num. Soggetti Associati</td>
        <td class="int">Azioni</td>
    </tr>
<%
    int countSentenza = 1;
    Iterator itx = sentenze.iterator();
    while ( itx.hasNext())
    {
      RegeSentenzaModel sentenza = (RegeSentenzaModel)itx.next();
%>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="ElencoProvvedimenti<%=countSentenza%>">

      <tr>
        <td class=c><font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataProvvedimento(),"dd-MM-yyyy"))%>
         </font><input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%><%=countSentenza%>" value="<%=DateUtils.getDateToString(sentenza.getDataProvvedimento(),"dd")%>">
          <input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%><%=countSentenza%>" value="<%=DateUtils.getDateToString(sentenza.getDataProvvedimento(),"MM")%>">
          <input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%><%=countSentenza%>" value="<%=DateUtils.getDateToString(sentenza.getDataProvvedimento(),"yyyy")%>">
        </td>
       <td class=c><font class="campo">
          <%=StringUtils.toStringJSP(sentenza.getDescrTipoProvvedimento())%>
          </font><input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO%><%=countSentenza%>" value="<%=sentenza.getCodTipoProvvedimento()%>">
          <input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_DESCR_TIPO_PROVVEDIMENTO%><%=countSentenza%>" value="<%=sentenza.getDescrTipoProvvedimento()%>">

          </td>

        <td class=c><font class="campo">
          <%=sentenza.getAnnoSentenza()%>/<%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%>
          </font><input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_ANNO_SENTENZA%><%=countSentenza%>" value="<%=sentenza.getAnnoSentenza()%>">
          <input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_NUMERO_SENTENZA%><%=countSentenza%>" value="<%=sentenza.getNumeroSentenza()%>">
        </td>
        <td class=c><font class="campoLow">
          <%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%>
          </font><input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%><%=countSentenza%>" value="<%=sentenza.getCodTipoAutoritaEmittente()%>">
          <input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_COD_LUOGO_EMITTENTE%><%=countSentenza%>" value="<%=sentenza.getCodLuogoEmittente()%>">
          <input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_DESCR_TIPO_AUTORITA_EMITTENTE%><%=countSentenza%>" value="<%=sentenza.getDescrTipoAutoritaEmittente()%>">
          <input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_DESCR_LUOGO_EMITTENTE%><%=countSentenza%>" value="<%=sentenza.getDescrLuogoEmittente()%>">
         </td>
         <input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_COUNT_SOGGETTI%><%=countSentenza%>" value="<%=sentenza.getCountSoggetti()%>">
         <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.regesies.regesentenza.action.ActDettaglioProvvedimentoDaElenco">
       <td class=c><font class="campo">
         <%=sentenza.getCountSoggetti()%></font>
        </td>
      <td class=c>
          <input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_PROGR_PROVVEDIMENTO%>" value="<%=countSentenza%>">
          <a href="javascript:SubmitElencoProvvedimenti('ElencoProvvedimenti<%=countSentenza%>','<%=countSentenza%>')">
            <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
          </a>
          	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%-- <jsp:include page="< %=ICostantiRegeSentenza.PAGE_BUTTONS_REGE_SENTENZA%>">
           	<jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
           	<jsp:param name="ValoreAzioneChiamante" value="< %=AzioneChiamante%>" />
           	<jsp:param name="CampoIdEntita" value="< %=ICostantiRegeSentenza.CAMPO_PROGR_PROVVEDIMENTO%>"/>
           	<jsp:param name="ValoreIdEntita" value="< %=countSentenza%>"/>
        	</jsp:include>
        	--%>
      </td>
    </tr>
     </FORM>
<%countSentenza++;
  }
%>
    </table>
  <br>
</body>
</html>