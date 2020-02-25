<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="evento"              scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="misuraalternativa"   scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="ufficioEmittente"      scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficioEmittenteMaAt"  scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="lPosGiuModificata" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="tipoMisura"       scope="request" class="java.lang.String"/>
<jsp:useBean id="isConCumulo"      scope="request" class="java.lang.String"/>
<%
// penaresidua:
// nuovapenaresidua: 
%>
<jsp:useBean id="penaresidua"       scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="nuovapenaresidua"  scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="magistrato"       scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="notificaIstituto" scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="notificaUEPE"     scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="notificaTDS"      scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="notificaUDS"      scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="notificaForzeP"   scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel  lPosizione       = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel     lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel          lAltraCausa      = posizioneluogoaltra.getAltraCausa();
  
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>

<body class="corpo">
<table>
  <tr>
    <%
    String lTipoTitolo = "";
    //if ("S".equals(isConCumulo))
      //lTipoTitolo = "(CON CUMULO)";
    //else
      //lTipoTitolo = "(SENZA CUMULO)";
    %>  
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    <%if(tipoMisura.equals(ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA)){%>
        <font class="campo">Dettaglio Prosecuzione Affidamento In Prova ex art. 51 bis o.p. <%=lTipoTitolo%></font>
    <%}else if(tipoMisura.equals(ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE)){%>
        <font class="campo">Dettaglio Prosecuzione Detenzione Domiciliare ex art. 51 bis o.p. <%=lTipoTitolo%></font>
    <%}else if(tipoMisura.equals(ICostantiMisuraAlternativa.SEMILIBERTA)) {%>
        <font class="campo">Dettaglio Prosecuzione Semilibertà ex art. 51 bis o.p. <%=lTipoTitolo%></font>
    <%}else if(tipoMisura.equals(ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE)){%>
        <font class="campo">Dettaglio Prosecuzione Detenzione Domiciliare a Termine ex art. 51 bis o.p. <%=lTipoTitolo%></font>
    <%}else if(tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)){%>
        <font class="campo">Dettaglio Prosecuzione Esecuzione Pena Presso Domicilio ex art. 51 bis o.p. <%=lTipoTitolo%></font>
    <%}%>
    </td>

<%if (   eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
      || ("N").equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())
     )
{%>
   <!-- BOTTONE DI VALIDAZIONE DIRETTA -->
  <td class="LBG">
    <a href="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActUploadMAProsecuzione51Bis&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misuraalternativa.action.ActDettaglioMAProsecuzione51Bis&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
      <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
    </a>
  </td>
 <!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActStampaMAProsecuzione51Bis&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&tipoMisura="+tipoMisura%>"/>
   </jsp:include>
<%}%>
</tr>
</table>

 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <br>
 
<%
//==============================================================================
// Posizione Giuridica - Tipo Pena - Quantum - Decorrenza Scadenza
//==============================================================================
%>
<table width ="100%">
  <tr>
    <td class="l">Posizione Giuridica </td>
    <td class="L" colspan=4>
      <font class="campo">
      <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {%>
              DETENUTO PER ALTRA CAUSA
      <% } else {%>
         <%=lPosizione.getDescrPosizioneGiuridica()%>
      <% } %>
      </font>
    </td>
  </tr>
  
  <% 
  if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) 
  {
    if( lAltraCausa.getIstitutoDetenzione()!= null ) 
    {
    %>
    <tr>
      <td class="l">Detenuto presso </td>
      <td class="L" colspan=4><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
        <% if(lAltraCausa.getIstitutoDetenzione().getDescrComune()!=null) { %>
        di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
        <% } %>
      </td>
    </tr>
    
      <% if (lAltraCausa != null && lAltraCausa.getAltroLuogo()!=null) { %>
      <tr>
        <td class="l">Altro Luogo </td>
        <td class="L" colspan=4>
          <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
        </td>
      </tr>
      <% }
     }
   }
   else if(lLuogoDetenzione.getIstitutoDetenzione() != null )
   {
   %>
          <tr>
            <td class="l">Detenuto presso </td>
            <td class="L" colspan=4>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            <% if(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()!=null) { %>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            <% } %>
            </td>
          </tr>
<%
  }%>
        
  
  <% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
  if(   lPosizione.getCodPosizioneGiuridica() != null 
     && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) 
    )
  {
    if(lLuogoDetenzione.getIstitutoDetenzione() != null)
    {
    %>
    <tr>
      <td class="l">Indirizzo</td>
      <td class="L" colspan=4>
        <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
      </td>
    </tr>
    <%
    }
  }
  %>


  <%
  // Visualizzazione della Pena Residua
  if(   penaresidua.getIdPenaResidua() != null 
     && !penaresidua.isErgastolo()
    )
  {
    if ( !penaresidua.isQuantumReclusioneZero() ) 
    {
    %>
      <tr>
        <td class="l">Reclusione</td>
        <td class="l" colspan=1>
          <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
          <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
          <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
        </td>
        <%if(penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0){%>
        <td class="l">Multa</td>
        <td class="l" colspan=1><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
        <% } %>  
      </tr>
    <%}%>

   
   <% if ( !penaresidua.isQuantumArrestoZero() ) { %>
   <tr>
      <td class="l" >Arresto</td>
      <td class="l" colspan=1>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <%if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0){%>
      <td class="l">Ammenda</td>
      <td class="l" colspan=1><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
      <% } %>
   </tr>
   <% } %>
 <%}%>


  <tr>
    <% if (penaresidua.getDataInizio() != null) { %>
    <td class="l">Data Decorrenza Pena</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
    <% } %>

    <% if ("S".equals(penaresidua.getFlagErgastolo())) { %>
    <td class="l">Pena Detentiva</td>
    <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
    <% } else if ("D".equals(penaresidua.getFlagErgastolo())) { %>
    <td class="l">Pena Detentiva</td>
    <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
    <% } %>

    <% 
    // Se non libero o detenuto altra causa
    if( !lPosizione.isLibero() || (lFascicoloAssociato.getFlagAltraCausa()!=null && "S".equals(lFascicoloAssociato.getFlagAltraCausa())) ) 
    {
      if (!penaresidua.isErgastolo())
      {
        if( penaresidua.getDataFine() != null)
        {
          if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
          {
          %>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              </td>
          <% } else { %>
             <td class="l">Data Fine Pena</td>
             <td class="lRosso" colspan=2>
               <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              </td>
          <% }
        }
      }  // end if (!penaresidua.isErgastolo())
    } // end if not libero o detienuto altra causa
  %>
  </tr>
</table>


<%
//==============================================================================
//
//==============================================================================
%>
<table width="100%">
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>
    </td>
    <td class="l">Data Trasmissione</td>
    <td class="L" >
    <%if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0) { %>
        <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>
     <%} %>
    </td>
  </tr>
</table>
<%
//==============================================================================
// Dati della misura che viene estesa (Solo su prosecuzione MDS)
//==============================================================================
%>
<table width="100%">
<% 
if (   misuraalternativa.getChiaveAnnoFascicoloSiusMaAt()!=null
    || misuraalternativa.getChiaveProgrFascicoloSiusMaAt()!=null
    || misuraalternativa.getAnnoRegistroMaAt()!=null
    || misuraalternativa.getNumeroRegistroMaAt()!=null
    || misuraalternativa.getCodTipoDecisioneMaAt()!=null
    || (ufficioEmittenteMaAt.getCodUfficio()!=null && ufficioEmittenteMaAt.getCodUfficio().trim().length()>0 )
    || (misuraalternativa.getDescrTipoMisuraMaAt()!=null && !misuraalternativa.getDescrTipoMisuraMaAt().equals("-") )
    || misuraalternativa.getDataDecisioneMaAt()!=null
   )
{
%>
<tr>
  <td class="Titolo" colspan="4"> Ordinanza di concessione della Misura in Corso</td>
</tr>
<tr>
  <td class="l">Anno / Numero Sius</td>
  <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSiusMaAt(),"-")%> /</font>
      <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSiusMaAt(),"-")%></font>
  </td>

  <% if("02".equals(misuraalternativa.getCodTipoDecisioneMaAt() )){ %>
  <td class="l" nowrap> Anno / Numero Decreto </td>
  <% } else { %>
  <td class="l" nowrap> Anno / Numero Ordinanza </td>
  <% } %>
  
  <td class="l" nowrap><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistroMaAt(),"-")%> /</font>
   <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistroMaAt(),"-")%></font>
  </td>
</tr>

<tr>
	<td class="l">Ufficio Emittente </td>
  	<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
    <%
    	String descrTipoUfficioMaAt = StringUtils.toStringJSP(ufficioEmittenteMaAt.getDescrTipoUfficio());
    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
    			"UDSM".equals(ufficioEmittenteMaAt.getCodTipoUfficio())) {
    		descrTipoUfficioMaAt = "Magistrato di Sorveglianza per i Minorenni";
    	}
    %>
  	<td class="l" colspan="4">
    	<font class="campo"><%=StringUtils.toStringJSP(descrTipoUfficioMaAt,"-")%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(ufficioEmittenteMaAt.getDescrComune(),"-")%></font>
  	</td>
</tr>
 
<tr>
   <% if("02".equals(misuraalternativa.getCodTipoDecisioneMaAt() )){ %>
   <td class="l">Oggetto Decreto </td>
   <% } else { %>
   <td class="l">Oggetto Ordinanza </td>
   <% } %>
   <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisuraMaAt())%></font>&nbsp;</td>
   
   <% if("02".equals(misuraalternativa.getCodTipoDecisioneMaAt() )){ %>
   <td class="l" nowrap>Data Emissione Decreto </td>
   <% } else { %>
   <td class="l" nowrap>Data Emissione Ordinanza </td>
   <% } %>

   <td class="l" nowrap>
    <font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(),"dd-MM-yyyy"),"&nbsp;")%>
    </font>
   </td>
</tr>
<% } %>

<%
//==============================================================================
// Dati dell'Ordinanza di concessione Prosecuzione
//==============================================================================
%>
<tr>
  <td class="Titolo" colspan="4"> Dati Del Provvedimento della Sorveglianza di Concessione della Prosecuzione ex art. 51 bis</td>
</tr>
<tr>
  <%if(misuraalternativa.getChiaveAnnoFascicoloSius()!= null){%>
  <td class="l">Anno / Numero Sius</td>
  <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
      <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
  </td>
<%}
  if(misuraalternativa.getAnnoRegistro()!= null){%>
     <% if("02".equals(misuraalternativa.getCodTipoDecisione() )){ %>
     <td class="l" nowrap> Anno / Numero Decreto </td>
     <% } else { %>
     <td class="l" nowrap> Anno / Numero Ordinanza </td>
     <% } %>
     
     <td class="l" nowrap><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
       <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
     </td>
  <%}%>
</tr>

<tr>
	<td class="l">Ufficio Emittente</td>
  	<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
    <%
    	String descrTipoUfficio = StringUtils.toStringJSP(ufficioEmittente.getDescrTipoUfficio());
    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
    			"UDSM".equals(ufficioEmittente.getCodTipoUfficio())) {
    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
    	}
    %>
  	<td class="l" colspan="3"> <font class="campo"><%=descrTipoUfficio%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(ufficioEmittente.getDescrComune())%></font></td>
</tr>
 
<tr>
  <% if("02".equals(misuraalternativa.getCodTipoDecisione() )){ %>
  <td class="l">Oggetto Decreto </td>
  <% } else { %>
  <td class="l">Oggetto Ordinanza </td>
  <% } %>
  <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
  
  <% if("02".equals(misuraalternativa.getCodTipoDecisione() )){ %>
  <td class="l" nowrap>Data Emissione Decreto </td>
  <% } else { %>
  <td class="l" nowrap>Data Emissione Ordinanza </td>
  <% } %>

  <td class="l" nowrap>
    <font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"),"&nbsp;")%>
    </font>
  </td>
</tr>

<tr>
  <td class="l">Data decorrenza misura</td>
  <td class="l" colspan="3">
    <font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"),"&nbsp;")%>
    </font>
  </td>
</tr>

<% 
if (   (misuraalternativa.getNumAnniMisura()!=null   && misuraalternativa.getNumAnniMisura().intValue()>0 )
    || (misuraalternativa.getNumMesiMisura()!=null   && misuraalternativa.getNumMesiMisura().intValue()>0 )
    || (misuraalternativa.getNumGiorniMisura()!=null && misuraalternativa.getNumGiorniMisura().intValue()>0 )
   )
{ %>
<tr>
  <td class="l">Quantità della misura</td>
  <td class="l" colspan="3">
    <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(misuraalternativa.getNumAnniMisura(),"0")%>&nbsp;</font>
    <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(misuraalternativa.getNumMesiMisura(),"0")%>&nbsp;</font>
    <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniMisura(),"0")%></font>
  </td>
</tr>
<% } %>

<% if (misuraalternativa.getDataFineMisura()!=null) { %>
<tr>
  <td class="l">Data fine misura</td>
  <td class="l" colspan="3">
    <font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataFineMisura(),"dd-MM-yyyy"),"&nbsp;")%>
    </font>
  </td>
</tr>
<% } %>

<%if(misuraalternativa.getNote() != null){%>
<tr>
  <td class="l">Note</td>
  <td class="l">
    <font class="campo"><%=misuraalternativa.getNote()%>&nbsp;</font>
  <td>
</tr>
<%}%>

<% if(nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua()!=null && nuovapenaresidua.getDataInizio()!=null) {%>
  <tr>
    <td class="Titolo" colspan="4"> Decorrenza Scadenza Rideterminata </td>
  </tr>

  <tr>
    <td class="l">Data Decorrenza Pena da Espiare</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataInizio(),"dd-MM-yyyy"))%> </font></td>
  </tr>
  
  <tr>
    <td class="l">Data Fine Pena da Espiare</td>
    <%if(nuovapenaresidua.getDataFine()!=null ){%>
      <%if(nuovapenaresidua.getDataFine().equals(nuovapenaresidua.getDataFinePresunta())){%>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
      </td>
      <% } else { %>
      <td class="lRosso" >
        <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
      </td>
      <%}%>
    <% } else {%>
      <td class="L" >
        <font class="campo">&nbsp;</font>
      </td>
    <% } %>
  </tr>
<%}%>


<tr>
  <td class="Titolo" colspan="4"> Magistrato Firmatario</td>
</tr>

<tr>
  <td class="l">Magistrato Firmatario</td>
  <td class="L">
    <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
    <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
  </td>
</tr>


<%
//==============================================================================
//                        Sezione con i destinatari
// - Istituto detenzione (E)
// - UEPE (E)
// - MDS (C)
// - Avvocati (N)
//==============================================================================
%>
<tr>
  <td class="Titolo" colspan="4"> Destinatari </td>
</tr>
<%if(notificaIstituto!=null && notificaIstituto.getIdNotifica()!=null){%>
  <tr>
    <td class="l">Istituto Detenzione</td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(notificaIstituto.getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
      <font class="campo"><%=StringUtils.toStringJSP(notificaIstituto.getIstitutoDetenzione().getDescrComune())%></font>
    </td>
  </tr>
  
  <%if(notificaIstituto.getNote()!= null && !notificaIstituto.getNote().equals("")){%>
  <tr>
    <td class="l">Note</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(notificaIstituto.getNote())%>&nbsp;</font></td>
  </tr>
  <% } %>
<% } %>

<%-- MEV10-s3: nuova gestione, invece che stringa fissa inserisco valore dalla combo (x 3 occorrenze) --%>
<%
//=============================================
// UEPE
//=============================================
if (notificaUEPE != null && notificaUEPE.getIdNotifica() != null) { %>
	<tr>
    	<td class="l"><%=StringUtils.toStringJSP(notificaUEPE.getCSSA().getTipoDesc())%></td>
    	<td class="l" colspan="3">
      		<font class="campo"><%=StringUtils.toStringJSP(notificaUEPE.getCSSA().getComune())%>-<%=StringUtils.toStringJSP(notificaUEPE.getCSSA().getIndirizzo())%></font>
    	</td>
  	</tr>

  	<% if (notificaUEPE.getNote() != null && !"".equals(notificaUEPE.getNote())) { %>
	  	<tr>
	    	<td class="l">Note</td>
	    	<td class="L" colspan="3">
	      		<font class="campo"><%=StringUtils.toStringJSP(notificaUEPE.getNote())%>&nbsp;</font>
	    	</td>
	  	</tr>
  	<% } %>
<% } %>

<% if (notificaTDS != null && notificaTDS.getIdNotifica() != null) { %>
	<tr>
    	<td class="l">Tribunale Emittente</td>
    	<td class="L" colspan="3">
      		<font class="campo"><%=StringUtils.toStringJSP(notificaTDS.getUfficio().getDescrTipoUfficio())%></font> di <font class="campo"><%=StringUtils.toStringJSP(notificaTDS.getUfficio().getDescrComune())%></font>
  	</tr>  

	<% if (notificaTDS.getNote() != null && !"".equals(notificaTDS.getNote())) { %>
	  	<tr>
	    	<td class="l">Note</td>
	    	<td  class="L" colspan="3">
	      		<font class="campo"><%=StringUtils.toStringJSP(notificaTDS.getNote())%>&nbsp;</font>
	    	</td>
	  	</tr>
	<% } %>
<% } %>

<% if (notificaUDS != null && notificaUDS.getIdNotifica() != null) { %>
	<tr>
    	<td class="l">Magistrato Preposto al controllo</td>
    	<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
	    <%
	    	String descrTipoUfficioUDS = StringUtils.toStringJSP(notificaUDS.getUfficio().getDescrTipoUfficio());
	    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
	    			"UDSM".equals(notificaUDS.getUfficio().getCodTipoUfficio())) {
	    		descrTipoUfficioUDS = "Magistrato di Sorveglianza per i Minorenni";
	    	}
	    %>
    	<td class="L" colspan="3">
      		<font class="campo"><%=descrTipoUfficioUDS%></font> di <font class="campo"><%=StringUtils.toStringJSP(notificaUDS.getUfficio().getDescrComune())%></font>
  	</tr>  

	<% if (notificaUDS.getNote() != null && !"".equals(notificaUDS.getNote())) { %>
		<tr>
    		<td class="l">Note</td>
    		<td  class="L" colspan="3">
      			<font class="campo"><%=StringUtils.toStringJSP(notificaUDS.getNote())%>&nbsp;</font>
    		</td>
  		</tr>
  	<% } %>
<% } %>

<% if (notificaForzeP != null && notificaForzeP.getIdNotifica() != null) { %>
	<tr>
  		<td class="l">Autorità competente per territorio</td>
  		<td class="L" colspan="3">
    		<font class="campo"><%=StringUtils.toStringJSP(notificaForzeP.getAutoritaEsterna().getDescrTipoAutorita())%></font>
    		<% if (Utils.isPresent(notificaForzeP.getAutoritaEsterna().getDescrSede()) &&
    				!"-".equals(notificaForzeP.getAutoritaEsterna().getDescrSede())) { %>
    			<font class="campo">&nbsp;di&nbsp;<%=StringUtils.toStringJSP(notificaForzeP.getAutoritaEsterna().getDescrSede())%></font>
    		<% } %>
  		</td>
	</tr>

  	<% if (notificaForzeP.getNote() != null && !"".equals(notificaForzeP.getNote())) { %>
  		<tr>
    		<td class="l">Indirizzo</td>
    		<td  class="L" colspan="3">
      			<font class="campo"><%=StringUtils.toStringJSP(notificaForzeP.getNote())%>&nbsp;</font>
    		</td>
  		</tr>
  	<% } %>
<% } %>

<%
//==============================================================================
// Notifiche agli avvocati
//==============================================================================
int count=0;
while(count < eventonotifica.getNotifiche().length)
{
  NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
  
  if(   lNotMod.getCodTipoNotifica().equals("N") 
     && lNotMod.getAutoritaEsterna()!=null 
     && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null
    )
  {
     AvvocatoSiepModel   lAvvMod = lNotMod.getAvvSiep();
     AutoritaEsternaModel lAuMod = lNotMod.getAutoritaEsterna();
  %>
  <tr>
    <td class="l">Avvocato per  Notifica</td>
    <td class="L" colspan="3">
      <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome()) +" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;
      &nbsp;Foro di&nbsp;
      <font class="campo">
        <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%>
      </font>
      &nbsp;Difensore di&nbsp;
      <font class="campo">
        <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%>
      </font>
    </td>
  </tr>
  <tr>
    <td class="l">Autorita Notifica</td>
    <td class="L" colspan="3">
      <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
      di
      <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
    </td>
  </tr>
  
  <% if (lNotMod.getNote()!= null) { %>
  <tr>
    <td class="l">Note</td>
    <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
  </tr>
  <%}
  }
  
  count++;
}
%>

</table>

 <br>
 
<div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
    <table>
      <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
        <td class="L">
          <input  class=bottone  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActUploadMAProsecuzione51Bis">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misuraalternativa.action.ActDettaglioMAProsecuzione51Bis">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">

          <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>">
          <input type="HIDDEN" name="tipoMisura" value="<%=tipoMisura%>">
          <input type="HIDDEN" name="IdPosizioneGiuridica" value="<%=lPosizione.getIdPosizioneGiuridica()%>">
        </td>
      </tr>
    </table>
  </form>
</div>

  <br>
  <br>
</body>
</html>