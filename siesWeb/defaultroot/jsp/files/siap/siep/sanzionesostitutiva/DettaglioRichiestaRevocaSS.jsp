<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%> 

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<%@ page import="siap.sico.evento.model.EventoModel"%> 
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%> 
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%> 
<%@ page import="siap.siep.sospensione.model.SospensioneModel"%> 
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>


<jsp:useBean id="aEveNotRichiesta"      scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="aAzioneChiamante"      scope="request" class="java.lang.String" />
<jsp:useBean id="aPosizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="magistrato"            scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>

<%
//==============================================================================
//         Pagina per la Load Dettaglio Richiesta Revoca Espulsione
//==============================================================================

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione       = aPosizioneluogoaltra.getPosizioneGiuridica();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  // Recupero le notifiche se presenti. 
  NotificaModel lNotificaMod = null;
  
  if( aEveNotRichiesta != null && aEveNotRichiesta.getNotifiche() != null)
  {                                   
    if (aEveNotRichiesta.getNotifiche().length>0)
    {
      lNotificaMod = aEveNotRichiesta.getNotifiche()[0];
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
        <font class="campo">Dettaglio Richiesta Revoca Sanzione Sostitutiva</font>
      </td>
<%
// Bottone di stampa (se non validato)
if (   aEveNotRichiesta.getEvento().getFlagDocumentoRegistrato()==null 
    || (   aEveNotRichiesta.getEvento().getFlagDocumentoRegistrato()!=null 
        && aEveNotRichiesta.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0
       )
   ) 
{%>
      <!-- BOTTONE DI STAMPA -->
      <input type="hidden" name="tipo" value="D">
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActStampaRichiestaRevSS&IdEvento="+aEveNotRichiesta.getEvento().getIdEvento()%>"/>
      </jsp:include>
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
<%
  if(penaresidua != null && penaresidua.getFlagSanzioneSostitutiva()!=null)
  {
%>
     <tr>
      <td class="l">Sanzione sostitutiva da espiare</td>  
      <td class="L">
           <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%>&nbsp;</font>
           <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
           <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
           <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>
<% 
				 if(   (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0)
            || (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0)
           )
         {
%>
          <font class="label"> Sanz.Pec.&nbsp;</font>
          <% if (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0) { %>
          <font class="campo">Multa&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoMultaSS())%>&nbsp;</font>&euro;
          <% } %>
          <% if (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0) { %>
          <font class="campo">Ammenda&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoAmmendaSS())%>&nbsp;</font>&euro;
          <% } %>
<%
         }
%> 
      </td>
    </tr>

<%} %> 
    <tr>
      <td class="l">Data Emissione</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEveNotRichiesta.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>

    <tr>
      <td class="l">Data Trasmissione</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEveNotRichiesta.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>   
  </table>  
  
<form method="POST"  action="<%=IWebConstants.PG_MAIN%>" name="DettaglioRichiestaRevocaEspulsione">
  <input type="HIDDEN" name="AzioneChiamante"    value="<%=aAzioneChiamante%>">
  
  <%
  //============================================================================
  //       
  //============================================================================
  %>  
  <table style="width: 95%;  border: 0;">

    <tr>
      <td class="titolo" colspan="100%">Dati della Richiesta</td>
    </tr>
 
    <tr>
      <td class="l">Oggetto</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(aEveNotRichiesta.getEvento().getDescrMotivo() )%></font>
      </td>
    </tr>


    <%if(   magistrato.getCodMagistrato() != null
         && !magistrato.getCodMagistrato().equals("-") 
        )
    {%>
    <tr>
      <td class="titolo" colspan="100%">Magistrato Firmatario</td>
    </tr>
    <tr>
      <td class="l">Magistrato Firmatario
      <td class="L">
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
      </td>
    </tr>
    <%}%>
    
    <tr>
      <td class="titolo" colspan="100%"> Destinatari </td>
    </tr>

    <%if (lNotificaMod!=null) {%>
    <tr>
      <td class="l">Ufficio Giudice dell'esecuzione</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lNotificaMod.getUfficio().getDescrTipoUfficio())%></font>
        di
        <font class="campo"><%=StringUtils.toStringJSP(lNotificaMod.getUfficio().getDescrComune())%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Motivazioni</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lNotificaMod.getNote())%></font>
      </td>
    </tr>
    <% } %>
    
</table>
  
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
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"             value="siap.siep.sanzionesostitutiva.action.ActUploadRichiestaRevSS">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"        value="<%= aEveNotRichiesta.getEvento().getIdEvento() %>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sanzionesostitutiva.action.ActLoadDettaglioRichiestaRevSS">
        </td>
      </tr>
    </table>
  </FORM>
</div>

</body>
</html>

  