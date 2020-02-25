<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.decretounificazione.action.ICostantiDecretoUnificazione" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<jsp:useBean id="eventoUnificazione" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="fascicoloUnificante" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="dataUnificazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="annoFascicoloUnificato" scope="request" class="java.lang.String"/>
<jsp:useBean id="progrFascicoloUnificato" scope="request" class="java.lang.String"/>
<jsp:useBean id="flagInsert" scope="request" class="java.lang.String"/>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Unificazione </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="/html/conferma.js"></script>
  <script language="JavaScript" >
    function lookUpload()
    {
      var node;
      node=document.getElementById('upld');
      node.style.visibility='visible';
    }
  </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione : </font>
        <font class="campo">Dettaglio Unificazione da Verbale</font>&nbsp;
      </td>
<%
      if( eventoUnificazione.getFlagDocumentoRegistrato() == null  ||
          eventoUnificazione.getFlagDocumentoRegistrato().compareTo("N")==0 )
      {
        if (!flagInsert.trim().equals("Y") )
        {
%>
          <!-- BOTTONE DI CANCELLAZIONE DA ELENCO -->
          <td class="LBG">
            <a href="Javascript:conferma1('siap.sius.unificazione.action.ActCancellaUnificazione','<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>=<%=annoFascicoloUnificato%>&<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>=<%=progrFascicoloUnificato%>','<%=ICostantiDecretoUnificazione.CAMPO_ID_EVENTO_UNIFICAZIONE%>','<%=eventoUnificazione.getIdEvento()%>','<%=ICostantiDecretoUnificazione.ACTION_DOPO_CANCELLAZIONE%>','siap.sius.provvedimento.action.ActRicercaFSProvvedimenti');">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
            </a>
          </td>
          <!-- BOTTONE DI RITORNO -->
          <td class="LBG">
            <a href="javascript:history.go(-1);">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
          </td>
       <%}
      else{%>
          <!-- BOTTONE DI CANCELLAZIONE DA INSERIMENTO -->
          <td class="LBG">
            <a href="Javascript:conferma1('siap.sius.unificazione.action.ActCancellaUnificazione','<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>=<%=annoFascicoloUnificato%>&<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>=<%=progrFascicoloUnificato%>','<%=ICostantiDecretoUnificazione.CAMPO_ID_EVENTO_UNIFICAZIONE%>','<%=eventoUnificazione.getIdEvento()%>','<%=ICostantiDecretoUnificazione.ACTION_DOPO_CANCELLAZIONE%>','siap.sius.unificazione.action.ActLoadVerificaUnificazione');">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
            </a>
          </td>

      <%}
      }%>
    </tr>
  </table>

  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Label" colspan="2"><font class="label">Dati di sintesi del Procedimento Unificante :&nbsp;</font></td>
   </tr>
    <tr>
      <td class="L"><font class="label" >Cognome Nome Soggetto </font></td>
      <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      <td class="L"><font class="campo"><%=fascicoloUnificante.getFascicoloSiusModel().getSoggetto().getCognome()%>&nbsp;<%=fascicoloUnificante.getFascicoloSiusModel().getSoggetto().getNome()%></font></td>
   </tr>
    <tr>
      <td class="L"><font class="label">Data Nascita </font></td>
      <td class="L"><font class="campo"><%=DateUtils.getDateToString(fascicoloUnificante.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy")%></font></td>
    </tr>
    <tr>
      <td class="L"><font class="label">Luogo Nascita </font></td>
<%
      if (fascicoloUnificante.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita().compareTo("-")==0)
      {
%>
        <td class="L"><font class="campo"><%=fascicoloUnificante.getFascicoloSiusModel().getSoggetto().getDescrStatoNascita()%> </font></td>
<%
      }
      else
      {
%>
        <td class="L"><font class="campo"><%=fascicoloUnificante.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita() + "  ("+fascicoloUnificante.getFascicoloSiusModel().getSoggetto().getCodProvinciaNascita()+")" %></font></td>
<%
      }
%>
    </tr>

    <tr>
      <td class="l">Numero SIUS</td>
      <td class="l"><font class="campo"><%=fascicoloUnificante.getFascicoloSiusModel().getChiaveAnno() %>/<%=fascicoloUnificante.getFascicoloSiusModel().getChiaveProgr() %></font></td>
    </tr>

    <tr>
      <td class="l">Numero SIEP</td>
      <td class="l"><font class="campo">
<%    if ((fascicoloUnificante.getFascicoloSiusModel().getChiaveAnnoSIEP()!= null) )
      {
%>
        <%=fascicoloUnificante.getFascicoloSiusModel().getChiaveAnnoSIEP() %>/<%=fascicoloUnificante.getFascicoloSiusModel().getChiaveProgrSIEP()%>
<%
      }
      else
      {
        %>-&nbsp;<%}%>

      </font></td>

    </tr>

    <tr>
    <td>&nbsp;</td>
    </tr>

    <tr>
        <td class="l">Contenuto</td>
        <td class="l"><font class="campo"><%=fascicoloUnificante.getGeneraleProcedimentoModel().getDescrOggettoProcedimento() %></font></td>
    </tr>

    <tr>
        <td class="l">Oggetto</td>
        <td class="l">
        <font class="label">

<%
          int lSize = fascicoloUnificante.getTenori().length;
          for( int x=0; x<lSize; x++ )
          {
%>
            <%=fascicoloUnificante.getTenori()[x].getDescrOggettoTenore()%><BR>
<%
          }
          if (!(lSize>0)) {%>&nbsp;-&nbsp;<%}
%>
        </font></td>
    </tr>

    <tr>
      <td class="Label" colspan="2"><font class="label">&nbsp;</font></td>
    </tr>

    <tr>
      <td class="Label" colspan="2"><font class="label">Dati di Unificazione :&nbsp;</font></td>
    </tr>

    <tr>
      <td class="l">Data Unificazione</td>
      <td class="l"><font class="campo"><%=dataUnificazione%></font></td>
    </tr>

    <tr>
      <td class="l">Fascicolo Unificato</td>
      <td class="l"><font class="campo"><%=annoFascicoloUnificato%>/<%=progrFascicoloUnificato%></font></td>
    </tr>

  </table>

 <div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post">
  <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
    <tr>
      <td class="L">
       <input class="bottone"  type="submit" value="Conferma">
       <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
       <input type="HIDDEN" name="IdEvento"  value="<%=eventoUnificazione.getIdEvento()%>">
       <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.decretounificazione.action.ActLoadDettaglioDecretoUnificazione">
      </td>
    </tr>
  </table>

  </FORM>
  </div>

</body>
</html>