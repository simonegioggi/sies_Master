<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%> 

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<%@ page import="siap.sico.evento.model.EventoModel"%> 
<%@ page import="siap.siep.verbale.model.VerbaleModel"%> 
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%> 
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%> 
<%@ page import="siap.siep.sospensione.model.SospensioneModel"%> 
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>


<jsp:useBean id="aEveNotComunicazione"  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="aVerbale"        		  scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="aCampoNotaModel"   	  scope="request" class="siap.sico.camponota.model.CampoNotaModel"/>
  
<jsp:useBean id="aPenaResidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="aAzioneChiamante"      scope="request" class="java.lang.String" />
<jsp:useBean id="aPosizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="magistrato"            scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>

<%
//==============================================================================
//               Pagina per la Load Dettaglio Mancata Espulsione
//==============================================================================

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione       = aPosizioneluogoaltra.getPosizioneGiuridica();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  // Recupero le notifiche se presenti. 
  NotificaModel lNotificaMod = null;
  
  if( aEveNotComunicazione != null && aEveNotComunicazione.getNotifiche() != null)
  {                                   
    if (aEveNotComunicazione.getNotifiche().length>0)
    {
      lNotificaMod = aEveNotComunicazione.getNotifiche()[0];
    }
  }
  
%>


<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <!-- Load Dettaglio Mancata Espulsione  -->
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
        </a>
      </td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Annotazione Mancata Espulsione</font>
      </td>
<%
// Bottone di stampa. (per evento non validato)
// n.b. non più prevista la stampa, ma la sola validazione 
if (   aEveNotComunicazione.getEvento().getFlagDocumentoRegistrato()==null 
    || (   aEveNotComunicazione.getEvento().getFlagDocumentoRegistrato()!=null 
        && aEveNotComunicazione.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0
       )
   ) 
{%>

    <!-- BOTTONE DI VALIDAZIONE -->
      <td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActUploadMancataEspulsione&IdEvento=<%=aEveNotComunicazione.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioMancataEspulsione&IdEvento=<%=aEveNotComunicazione.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
          <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
        </a>
      </td>
    
<%}%>
      
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <%
  //============================================================================
  // Sezione con il dettaglio della posizione giuridica
  //============================================================================
  %>
  <%
if (   aEveNotComunicazione.getEvento().getFlagDocumentoRegistrato()==null 
    || (   aEveNotComunicazione.getEvento().getFlagDocumentoRegistrato()!=null 
        && aEveNotComunicazione.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0
       )
   ) 
{%>
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
        <font class="campo">
        <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
          DETENUTO PER ALTRA CAUSA
        <% } else { %>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        <% } %>
        </font>
      </td>
    </tr>
  </table>  
<% } %>  
<form method="POST"  action="<%=IWebConstants.PG_MAIN%>" name="DettaglioMancataEspulsione">
  <input type="HIDDEN" name="AzioneChiamante"    value="<%=aAzioneChiamante%>">
  
  <%
  //============================================================================
  //                           Dati del Verbale  VEdi DettaglioComputoCustodiaCautelare.jsp
  //============================================================================
  %>  
  <table style="width: 95%;  border: 0;">
    <tr>
      <td class="titolo" colspan="100%">Nota Mancata Espulsione </td>
    </tr>
    <tr>
      <td class="l" width="30%">Numero Protocollo</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(aVerbale.getNumeroProtocollo(),"&nbsp;")%></font>
      </td>
    </tr>

    <tr>
      <td class="l">Data Nota</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aVerbale.getDataEmissione(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>

    <tr>
      <td class="l">Data Annotazione</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEveNotComunicazione.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>
    
    <tr>
      <td class="l">Autorità Emittente</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(aVerbale.getDescrTipoUfficioFirmatario()) %></font>
        di 
        <font class="campo"><%=StringUtils.toStringJSP(aVerbale.getDescrLuogoUfficioFirmatario()) %></font>
        &nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(aVerbale.getNote()) %></font>
      </td>
    </tr>
    

    <tr>
      <td class="l">Note</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(aCampoNotaModel.getDescr(),"") %></font>
      </td>
    </tr>


    <%if(   magistrato.getCodMagistrato() != null
         && !magistrato.getCodMagistrato().equals("-") 
        )
    {%>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l">Magistrato Firmatario
      <td class="L">
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
      </td>
    </tr>
    <%}%>
</table>



  <%
  //============================================================================
  //                         Dati del Provvedimento
  //============================================================================
  %>
  <% 
  if (lNotificaMod!=null) { %>
  <table style="width: 95%;  border: 0;">
    <tr>
      <td class="titolo" colspan="100%"> Destinatari Per la Comunicazione </td>
    </tr>
    
    <tr>
      <td class="l">Autorità di Polizia</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lNotificaMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>
        di
        <font class="campo"><%=StringUtils.toStringJSP(lNotificaMod.getAutoritaEsterna().getDescrSede())%></font>
      </td>
    </tr>
  </table>
  <%}%>
  
</form>
<%
//==============================================================================
// Sezione per la Validazione
//==============================================================================
%>
<div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
    <table width="90%">
      <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
        <td class="L">
          <input  class=bottone  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"             value="siap.siep.sanzionesostitutiva.action.ActUploadMancataEspulsione">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"        value="<%= aEveNotComunicazione.getEvento().getIdEvento() %>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sanzionesostitutiva.action.ActLoadDettaglioMancataEspulsione">
        </td>
      </tr>
    </table>
  </FORM>
</div>

</body>
</html>
</form>
  