<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>
<%@ page import="siap.sige.decretounificazione.action.ICostantiDecretoUnificazioneSige" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<jsp:useBean id="eventoUnificazione" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="provvedimentoUnificazione" scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>
<jsp:useBean id="fascicoloUnificante" scope="request" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="dataUnificazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="annoFascicoloUnificato" scope="request" class="java.lang.String"/>
<jsp:useBean id="progrFascicoloUnificato" scope="request" class="java.lang.String"/>
<jsp:useBean id="tenoriFascicoloSige" scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<!-- 16/01/2018 SC *** inizio *** Risolta anomalia su tasto 'Indietro' riscontrata durante fase di test di pre-collaudo -->
<jsp:useBean id="flagIns" scope="request" class="java.lang.String"/>
<jsp:useBean id="idFascicoloUnificato" scope="request" class="java.lang.String"/>
<!-- 16/01/2018 SC *** fine *** -->

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Decreto di Unificazione Sige</title>

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
        <font class="campo">Dettaglio Decreto Unificazione Sige</font>&nbsp;
      </td>
<%
 if( eventoUnificazione.getFlagDocumentoRegistrato() == null  ||
  eventoUnificazione.getFlagDocumentoRegistrato().compareTo("N")==0 )
{
%>
  <!-- BOTTONE DI STAMPA -->
    <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIGE%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiDecretoUnificazioneSige.CAMPO_ID_DECRETO_UNIFICAZIONE%>"/>
          <jsp:param name="ValoreIdEntita" value="<%=provvedimentoUnificazione.getProvvedimento().getIdProvvedimentoSige()%>"/>
          </jsp:include>

         <!-- BOTTONE DI CANCELLAZIONE -->
         <td class="LBG">
           <!-- 16/01/2018 SC *** Risolta anomalia su tasto 'Indietro' riscontrata durante fase di test di pre-collaudo -->
           <a href="Javascript:conferma('siap.sige.decretounificazione.action.ActCancellaDecretoUnificazioneSige','<%=ICostantiDecretoUnificazioneSige.CAMPO_ID_DECRETO_UNIFICAZIONE%>','<%=provvedimentoUnificazione.getProvvedimento().getIdProvvedimentoSige()%>','TornaQui','<%=TornaQui%>', '<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>', '<%=idFascicoloUnificato%>');">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
           </a>
         </td>

          <!-- BOTTONE DI RITORNO -->
          <%-- <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/> --%>
<%}%>
    <!-- Modifica del 03/03/2017 sposto il tasto "Indietro" fuori dall'if, affinchè
    	 sia visibile anche dopo la validazione
    	 
    	 Se provengo dall'inserimento del Decreto di Unificazione, il tasto Indietro
    	 mi deve riportare al Dettaglio del Fascicolo Unificato.
    -->
<!-- 16/01/2018 SC *** inizio *** Risolta anomalia su tasto 'Indietro' riscontrata durante fase di test di pre-collaudo -->
<% if (flagIns != null && !flagIns.equals("") && flagIns.equals("Y")) { %>
        <td class="LBG">
          <a href="Main.jsp?Action=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=idFascicoloUnificato%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
<% } else { %>
   		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
<% } %> 
<!-- 16/01/2018 SC *** fine *** -->
         
    </tr>
  </table>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Label" colspan="2"><font class="label">Dati di sintesi del Procedimento Unificante</font></td>
   </tr>
    <tr>
      <td class="L"><font class="label" >Cognome Nome Soggetto </font></td>
      <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      <td class="L"><font class="campo"><%=fascicoloUnificante.getSoggetto().getCognome()%>&nbsp;<%=fascicoloUnificante.getSoggetto().getNome()%></font></td>
   </tr>
    <tr>
      <td class="L"><font class="label">Data Nascita </font></td>
      <td class="L"><font class="campo"><%=DateUtils.getDateToString(fascicoloUnificante.getSoggetto().getDataNascita(),"dd-MM-yyyy")%></font></td>
    </tr>
    <tr>
      <td class="L"><font class="label">Luogo Nascita </font></td>
<%
      if (fascicoloUnificante.getSoggetto().getDescrComuneNascita().compareTo("-")==0)
      {
%>
        <td class="L"><font class="campo"><%=fascicoloUnificante.getSoggetto().getDescrStatoNascita()%> </font></td>
<%
      }
      else
      {
%>
        <td class="L"><font class="campo"><%=fascicoloUnificante.getSoggetto().getDescrComuneNascita() + "  ("+fascicoloUnificante.getSoggetto().getCodProvinciaNascita()+")" %></font></td>
<%
      }
%>
    </tr>

    <tr>
      <td class="l">Numero SIGE</td>
      <td class="l"><font class="campo"><%=fascicoloUnificante.getFascicoloSige().getChiaveAnno() %>/<%=fascicoloUnificante.getFascicoloSige().getChiaveProgr() %></font></td>
    </tr>

    <tr>
      <td class="l">Numero SIEP</td>
      <td class="l"><font class="campo">
<%    if ((fascicoloUnificante.getFascicoloSiep() != null) && (fascicoloUnificante.getFascicoloSiep().getChiaveAnno()!= null) )
      {
%>
        <%=fascicoloUnificante.getFascicoloSiep().getChiaveAnno() %>/<%=fascicoloUnificante.getFascicoloSiep().getChiaveProgr()%>
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
        <td class="l">Oggetto</td>
        <td class="l">
        <font class="campo">

<%
        // 05/11/2003 REWORK FascicoloGPModel (Elenco Tenori).
        Iterator itx = tenoriFascicoloSige.iterator();
          while( itx.hasNext() )
          {
        	  TenoreSigeModel lTenSigeMod = (TenoreSigeModel)itx.next();
%>
            <%=lTenSigeMod.getDescrOggettoSige()%><BR>
<%
          }
%>
        </font></td>
    </tr>

    <tr>
      <td class="Label" colspan="2"><font class="label">&nbsp;</font></td>
    </tr>

    <tr>
      <td class="Label" colspan="2"><font class="label">Dati di Unificazione</font></td>
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
       <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%=eventoUnificazione.getIdEvento()%>">
       <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.decretounificazione.action.ActLoadDettaglioDecretoUnificazioneSige">
       <input type="HIDDEN" name="<%=IWebConstants.ACTION_DOPO_CANCELLAZIONE%>"  value="siap.sius.decretounificazione.action.ActLoadVerificaDecretoUnificazioneSige">
      </td>
    </tr>
  </table>

  </FORM>
  </div>

</body>
</html>