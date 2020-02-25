<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%> 

<%@ page import="java.util.Date"%> 
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<%@ page import="siap.sico.evento.model.EventoModel"%> 
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%> 
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%> 
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%> 


<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="aEveNotComunicazione"  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="aPenaResidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="aMisure"               scope="request" class="java.util.Vector"/>

<%// Misure cautelari in sentenza%>
<jsp:useBean id="aTotMCinCarcere"      scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="aTotMCinArrestiDom"   scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="aTotMC"               scope="request" class="siap.sico.calendar.model.CalendarModel"/>



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
        <font class="campo">Dettaglio Comunicazione Nuovo Residuo Pena</font>
      </td>
<%
// Bottone di stampa.
if (   aEveNotComunicazione.getEvento().getFlagDocumentoRegistrato()==null 
    || (   aEveNotComunicazione.getEvento().getFlagDocumentoRegistrato()!=null 
        && aEveNotComunicazione.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0
       )
   ) 
{%>
      
      <%if (lNotificaMod!=null){%>
      <!-- BOTTONE DI STAMPA -->
      <input type="hidden" name="tipo" value="D">
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActStampaComunicazioneNuovoResiduoPena&IdEvento="+aEveNotComunicazione.getEvento().getIdEvento()%>"/>
      </jsp:include>
      <% } else { %>
      <!-- BOTTONE DI VALIDAZIONE -->
      <td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActUploadComunicazioneNuovoResiduoPena&IdEvento=<%=aEveNotComunicazione.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioComunicazioneNuovoResiduoPena&IdEvento=<%=aEveNotComunicazione.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
          <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
        </a>
      </td>
      <% }%>
<%}%>
 
      <!-- TOOLBAR HEADER (per il tasto di TRASMISSIONE -->
      <td class="LBG">
        <jsp:include page="<%=ICostantiEvento.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=aEveNotComunicazione.getEvento().getIdEvento()%>" />
          <jsp:param name="FlagDocumentoRegistrato" value="<%=aEveNotComunicazione.getEvento().getFlagDocumentoRegistrato()%>" />
        </jsp:include>
     </td>
      
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <%
  //============================================================================
  // Sezione con il dettaglio della Posizione Giuridica
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

<%
//==============================================================================
//              Sezione per la visualizzazione della PENA
//==============================================================================
%>
<table>
  <tr>
    <td class="l">Misure Cautelari Computate </td>
    <% if (   CalendarUtil.getTotGiorni(aTotMCinCarcere)>0
           && CalendarUtil.getTotGiorni(aTotMCinArrestiDom)>0
          ) 
    { // presenti sia Arresti che Det. Dom., visualizzo il totale senza dettagliare il tipo misura
    %>
    <td class="l">
      <font class="l">Anni&nbsp;</font><font class="campo" ><%=aTotMC.getNumAnni()%>&nbsp;</font>
      <font class="l">Mesi&nbsp;</font><font class="campo" ><%=aTotMC.getNumMesi()%>&nbsp;</font>
      <font class="l">Giorni&nbsp;</font><font class="campo" ><%=aTotMC.getNumGiorni()%></font>
    </td>
    <% 
    }
    else if (CalendarUtil.getTotGiorni(aTotMCinCarcere)>0)
    {
    %>
    <td class="l">
      <font class="campo">IN CARCERE&nbsp;</font>
      <font class="l">Anni&nbsp;</font><font class="campo" ><%=aTotMCinCarcere.getNumAnni()%>&nbsp;</font>
      <font class="l">Mesi&nbsp;</font><font class="campo" ><%=aTotMCinCarcere.getNumMesi()%>&nbsp;</font>
      <font class="l">Giorni&nbsp;</font><font class="campo" ><%=aTotMCinCarcere.getNumGiorni()%></font>
    </td>
    <%
    }
    else if (CalendarUtil.getTotGiorni(aTotMCinArrestiDom)>0)
    {
    %>
    <td class="l">
      <font class="campo">IN ARRESTI DOMICILIARI&nbsp;</font>
      <font class="l">Anni&nbsp;</font><font class="campo" ><%=aTotMCinArrestiDom.getNumAnni()%>&nbsp;</font>
      <font class="l">Mesi&nbsp;</font><font class="campo" ><%=aTotMCinArrestiDom.getNumMesi()%>&nbsp;</font>
      <font class="l">Giorni&nbsp;</font><font class="campo" ><%=aTotMCinArrestiDom.getNumGiorni()%></font>
    </td>
    <%
    }
    %> 
  </tr>

<tr>
<%  
    if(   aPenaResidua.getIdPenaResidua() != null
       && (    aPenaResidua.getFlagErgastolo() == null
            || (   aPenaResidua.getFlagErgastolo() != null 
                && !aPenaResidua.getFlagErgastolo().equals("S") 
                && !aPenaResidua.getFlagErgastolo().equals("D")
               ) 
          ) 
      )
    {
        //=====================
        // RECLUSIONE se >0
        //=====================
        if ( CalendarUtil.getTotGiorni(aPenaResidua.getQuantumReclusione())>0)
        {
        %>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(aPenaResidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(aPenaResidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(aPenaResidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(aPenaResidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
        <%
        }
        %>
        </tr>
   
        <tr>
        <%
        //=====================
        // ARRESTI se >0
        //=====================
        if ( CalendarUtil.getTotGiorni(aPenaResidua.getQuantumArresto())>0)
        {
        %>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(aPenaResidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(aPenaResidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(aPenaResidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(aPenaResidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
        <%
        }
    }
    %>
    </tr>
    
    
    <%
    //============================================
    // Decorrenza/Scadenza
    //============================================
    %>
    <tr>
      <% if (aPenaResidua.getDataInizio() != null) { %>
        <td class="l">Data Decorrenza Pena</td>
        <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaResidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <% } %>

      <% if ( aPenaResidua.getFlagErgastolo() != null)
      {
        if(aPenaResidua.getFlagErgastolo().equals("S"))
        {
        %>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
        <%
        }
        else if(aPenaResidua.getFlagErgastolo().equals("D"))
        {
        %>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
        <%
        }
      }
      %>

      
      
      <%
      // Detenuta Altra Causa
      if(   !lPosizione.isLibero()
         || (   lFascicoloAssociato.getFlagAltraCausa()!=null 
             && lFascicoloAssociato.getFlagAltraCausa().equals("S") 
            ) 
        )
      { 
        if  (   (aPenaResidua.getFlagErgastolo() == null) 
             || (   aPenaResidua.getFlagErgastolo() != null 
                 && !aPenaResidua.getFlagErgastolo().equals("S") 
                 && !aPenaResidua.getFlagErgastolo().equals("D")
                )
              )
        {
          if( aPenaResidua.getDataFine() != null)
          {
            if(aPenaResidua.getDataFine().equals(aPenaResidua.getDataFinePresunta()))
            {
            %>
              <td class="l">Data Fine Pena</td>
              <td class="L" colspan=2>
                <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaResidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaResidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaResidua.getDataFine(), "yyyy") )%></font>
              </td>
            <%
            }
            else
            {
            %>
              <td class="l">Data Fine Pena</td>
              <td class="lRosso" colspan=2>
                <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaResidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaResidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaResidua.getDataFine(), "yyyy") )%></font>
              </td>
<%          }
          }
        }
      }
%>
  </tr>
  
  
  <tr>
    <td class="l">Sanzione Sostitutiva Da Espiare</td>
    <td class="l">
      <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(aPenaResidua.getNumAnniSS(),"0")%>&nbsp;</font>
      <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(aPenaResidua.getNumMesiSS(),"0")%>&nbsp;</font>
      <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(aPenaResidua.getNumGiorniSS(),"0")%></font>
    </td>
  </tr>
</table>

<form method="POST"  action="<%=IWebConstants.PG_MAIN%>" name="DettaglioComunicazioneNuovoResiduoPena">
  <input type="HIDDEN" name="AzioneChiamante"    value="<%=aAzioneChiamante%>">
  
  <%
  //============================================================================
  //                       Dati della Comunicazione
  //============================================================================
  %>  
  <table>

    <tr>
      <td class="l" >Data Emissione</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEveNotComunicazione.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
      </td>
      <% 
        Date lDataTrasmissione = null;
        NotificaModel lNotMod = aEveNotComunicazione.getNotifiche()[0];
        lDataTrasmissione = lNotMod.getDataInvio();
      %>
      <td class="l">Data Trasmissione</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataTrasmissione,"dd-MM-yyyy"))%></font>
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

    <%
    //==================================================================
    // Destinatari
    //==================================================================
    int count=0;
    while(count < aEveNotComunicazione.getNotifiche().length)
    {
      NotificaModel lNotMod2 = aEveNotComunicazione.getNotifiche()[count];
      
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Notifica jsp = "+lNotMod2);
      
      if (lNotMod2.getCodTipoNotifica().equals("MS"))
      { // Magistrato di Sorveglianza
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug(" ufficio = "+lNotMod2.getUfficio().getDescrComune());
      %>
      <tr>
        <td class="l">Magistrato di Sorveglianza </td>
        <td class="L"> di 
           <font class="campo"><%=StringUtils.toStringJSP(lNotMod2.getUfficio().getDescrComune(),"")%></font>
        </td>
      </tr>
      <tr>
        <td class="L">Note </td>
        <td class="L"> 
           <font class="campo"><%=StringUtils.toStringJSP(lNotMod2.getNote(),"&nbsp;")%></font>
        </td>
      </tr>
      <%  
      }
      else if (lNotMod2.getCodTipoNotifica().equals("C"))
      { // Altra Autorità
      %>
      <tr>
        <td class="l">Altra Autorità</td>
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(lNotMod2.getAutoritaEsterna().getDescrTipoAutorita())%></font>
          di
          <font class="campo"><%=StringUtils.toStringJSP(lNotMod2.getAutoritaEsterna().getDescrSede())%></font>
        </td>
      </tr>
      
      <tr>
        <td class="L">Note </td>
        <td class="L"> 
           <font class="campo"><%=StringUtils.toStringJSP(lNotMod2.getNote(),"&nbsp;")%></font>
        </td>
      </tr>
      <%
      }
      
      count++;
    }
    %>

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
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"             value="siap.siep.sanzionesostitutiva.action.ActUploadComunicazioneNuovoResiduoPena">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"        value="<%= aEveNotComunicazione.getEvento().getIdEvento() %>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sanzionesostitutiva.action.ActLoadDettaglioComunicazioneNuovoResiduoPena">
        </td>
      </tr>
    </table>
  </FORM>
</div>

</body>
</html>
  