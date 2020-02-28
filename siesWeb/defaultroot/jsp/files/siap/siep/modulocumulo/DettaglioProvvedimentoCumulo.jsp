<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.F3BProperties"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.istruttoriacumulo.model.EsitoArchiviazioniCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.util.ModuloCumuloUtils"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaRideterminataCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.cssa.model.CSSAModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<jsp:useBean id="IstruttoriaCumulo"     	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="datiFinaliAggregatoModel" 	scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>
<jsp:useBean id="magistrato" 				scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="modalita"  				scope="request" class="java.lang.String"/>
<%-- MEV 16 CUMULO: aggiunto useBean --%>
<jsp:useBean id="documentoAllegato"			scope="request" class="siap.sius.documentoallegato.model.DocumentoAllegatoModel"/>
<jsp:useBean id="Esiti"  					scope="request" class="java.util.Vector"/>

<%
PosizioneGiuridicaCumuloModel lPosizioneGiuridicaCumulo = datiFinaliAggregatoModel.getPosizioneGiuridicaCumulo();
PenaRideterminataCumuloModel  lPenaRideterminataCumulo = datiFinaliAggregatoModel.getPenaRideterminataCumulo();
PenaRideterminataCumuloModel  lPenaResiduaCumulo = datiFinaliAggregatoModel.getPenaResiduaCumulo();

EventoNotificaModel lProvvedimentoCumulo = datiFinaliAggregatoModel.getProvvedimentoCumulo();

DatiFinaliCumuloModel lDatiFinali = datiFinaliAggregatoModel.getDatiFinaliCumulo();

%>


<%
//==============================================================================
// JSP di visualizzazione del dettaglio del Provvedimento di cumulo
//==============================================================================
%>

<html>
<head>
  <title> [S.I.E.S.] - Dati Finali Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>  
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">  
    function loadModifica (azione) {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = azione;
      document.formName.<%=ICostantiModuloCumulo.MODALITA%>.value = "<%=ICostantiModuloCumulo.MODALITA_MODIFICA%>";
      document.formName.submit();
    }
    
    function loadCancella (azione) {
      var msgConfirm = "Verrà cancellato solo il provvedimento di cumulo. Tutti i dati dell'istruttora rimarranno disponibili per l'emissione di un nuovo provvedimeto. Si vuole procedere?";
      if (window.confirm(msgConfirm)) {     
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = azione;
        document.formName.submit();
      }
    }
    
    function eseguiFunzione(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }
    
    function ApriChiudiRigaDettaglio(NRec)
    {
      var riga = document.getElementById(NRec);  
      if (riga.style.display =="none" )
      {
        riga.style.display = "block";
      }
      else 
      {
        riga.style.display = "none";
      }
    }
    
  </script>
  
  <script language="JavaScript1.2">
    function over_effect(e,state)
    {
      if (document.all)
          source4=event.srcElement
      else if (document.getElementById)
          source4=e.target
      if (source4.className=="menulines")
          source4.style.borderStyle=state
      else
      {
        while(source4.tagName!="TABLE")
        {
          source4=document.getElementById? source4.parentNode : source4.parentElement
            if (source4.className=="menulines")
                source4.style.borderStyle=state
        }
      }
    }
  </script>

  <STYLE>
    .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      /*width:100%;*/
      height:100%;
    }
  </STYLE>   
  
</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Provvedimento Determinazione Pene Concorrenti</font>
      </td>

<%-- MEV 16 CUMULO: gestite casistiche per cui far vedere l'icona del FC --%>
<%
boolean onOffIconFCCumulo = "off".equalsIgnoreCase(F3BProperties.getProperty("onOffIconFCCumulo")) ? true : false;
if (ModuloCumuloUtils.isCumulo(lProvvedimentoCumulo.getEvento().getCodMotivo()) && !onOffIconFCCumulo) {
	if ("C".equals(IstruttoriaCumulo.getFlagStato())) {
		// FlagDocumentoRegistrato=A  il provvedimento è annullato ==> il bottone "FC" non deve essere visibile
		// FlagDocumentoRegistrato=N  il provvedimento non è validato ==> il bottone "FC" non deve essere visibile
		// FlagDocumentoRegistrato=S  l'evento è stato validato  ==> il bottone "FC" deve essere visibile
		if (documentoAllegato.getIdDocumentoAllegato() != null
				&& lProvvedimentoCumulo.getEvento().getFlagDocumentoRegistrato() != null
				&& "A".compareTo(lProvvedimentoCumulo.getEvento().getFlagDocumentoRegistrato()) != 0
				&& "N".compareTo(lProvvedimentoCumulo.getEvento().getFlagDocumentoRegistrato()) != 0
				&& "S".compareTo(lProvvedimentoCumulo.getEvento().getFlagDocumentoRegistrato()) == 0) {
			// il foglio complementare esiste ==> azione: modifica foglio complementare
			if (documentoAllegato.getDataAnnullamento() == null) {
%>
			<td class="LBG">
				<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=ModificaFC&IdEvento=<%=lProvvedimentoCumulo.getEvento().getIdEvento()%>&IdDocumentoAllegato=<%=documentoAllegato.getIdDocumentoAllegato()%>">
					<img src="/images/fcNsc.gif" width="30" height="30" alt="Modifica Foglio Complementare" border="0">
				</a>
			</td>
<%
			// il foglio complementare non esiste(annullato) ==> azione: inserimento foglio complementare
			} else {
%>
			<td class="LBG">
				<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=InsertFC&IdEvento=<%=lProvvedimentoCumulo.getEvento().getIdEvento()%>">
					<img src="/images/fcNsc.gif" width="30" height="30" alt="Inserimento Foglio Complementare" border="0">
				</a>
			</td>
<%
			}
		} else if (documentoAllegato.getIdDocumentoAllegato() == null
				&& lProvvedimentoCumulo.getEvento().getFlagDocumentoRegistrato() != null
				&& "A".compareTo(lProvvedimentoCumulo.getEvento().getFlagDocumentoRegistrato()) != 0
				&& "N".compareTo(lProvvedimentoCumulo.getEvento().getFlagDocumentoRegistrato()) != 0
				&& "S".compareTo(lProvvedimentoCumulo.getEvento().getFlagDocumentoRegistrato()) == 0) {
			// il foglio complementare non esiste ==> azione: inserimento foglio complementare 
			// se il provvedimento è stato validato eventonotifica.getEvento().getFlagDocumentoRegistrato()=="S"
%>
		<td class="LBG">
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=InsertFC&IdEvento=<%=lProvvedimentoCumulo.getEvento().getIdEvento()%>">
				<img src="/images/fcNsc.gif" width="30" height="30" alt="Inserimento Foglio Complementare" border="0">
			</a>
		</td>
<%
		} // chiudo else if
	} // chiudo if FlagStato
}
%>
<%-- FINE MEV 16 CUMULO --%>
      
      <% if (   "N".equals (lProvvedimentoCumulo.getEvento().getFlagDocumentoRegistrato())
             || lProvvedimentoCumulo.getEvento().getFlagDocumentoRegistrato()==null
      ) { %>
      <td class="LBG">
        <a href="javascript:loadModifica('siap.siep.modulocumulo.action.ActLoadInserisciProvvedimentoCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
      </td>
      
      <td class="LBG">
        <a href="javascript:loadCancella('siap.siep.modulocumulo.action.ActCancellaProvvedimentoCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
      </td>

      
      <% if ("03".equals(lDatiFinali.getTipoUfficioEmissione()) ) {%>
      <td class="LBG">
        <a onclick="javascript:lookUpload(); document.comandi.CampoBlob.focus();">
          <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
        </a>
      </td>
      <% } %>
      
      
      <% if (!"0652".equals(lProvvedimentoCumulo.getEvento().getCodMotivo())) {%>
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActStampaProvvedimentoCumulo&IdEvento="+lProvvedimentoCumulo.getEvento().getIdEvento()+"&IdIstruttoriaCumulo="+IstruttoriaCumulo.getIdIstruttoriaCumulo()%>"/>
      </jsp:include>
      <% } %>
      
      <% } %>

    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/NavigazioneDatiFinaliCumulo.jsp"/>
  <br>
  
  
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lProvvedimentoCumulo.getEvento().getIdEvento() %>">
    
    <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA%>" value="<%=modalita%>">

<%
//==============================================================================
// 
//==============================================================================
%>
<div id="divPosizionamento" align="left" style="padding-left: 25px;"> 
<table width="90%">
  <tr>
    <td class="l" style="width:150px">Posizione Giuridica</td>
    <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lPosizioneGiuridicaCumulo.getDescrPosizioneGiuridica()) %>&nbsp;</font></td>
  </tr>
  
  <% if (lPenaResiduaCumulo.isErgastolo()) {%>
  <tr>
    <td class="l" style="width:150px"><font class="label">Pena Detentiva </font></td>
    <td class="l" colspan="3">
      <font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getDescrTipoPenaDetentiva())%></font>
        <% if ("04".equals(lPenaResiduaCumulo.getCodTipoPenaDetentiva())) { %>
        Durata Isolamento Diurno: Anni <font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumAnniIsolamentoDiurno(),"-") %></font>
                                  Mesi <font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumMesiIsolamentoDiurno(),"-") %></font>
                                  Giorni <font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumGiorniIsolamentoDiurno(),"-") %></font>
        <% } %>   
    </td>
  </tr>
  <% } %>
 
  <% if (lPenaResiduaCumulo.isReclusione() || lPenaResiduaCumulo.isMulta() ) {%>
  <tr>
    <td class="l">Reclusione</td>
    <td class="l" style="width:250px">
      <% if ( lPenaResiduaCumulo.isReclusione() ) {%>
      <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumAnniReclusione())%>&nbsp;</font>
      <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumMesiReclusione())%>&nbsp;</font>
      <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumGiorniReclusione())%></font>
      <% } else { %>
      <font color="red">nulla residua da espiare</font>
      <% } %>    
    </td>
    <td class="l" style="width:100px">Multa</td>
    <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(lPenaResiduaCumulo.getImportoMulta()) %></font>&nbsp;<font class="l">Euro</font></td>
  </tr>
  <% } %>
  <% if (lPenaResiduaCumulo.isArresto() || lPenaResiduaCumulo.isAmmenda() ) {%>
  <tr>
    <td class="l" >Arresto</td>
    <td class="l" style="width:250px">
      <% if ( lPenaResiduaCumulo.isArresto() && !lPenaResiduaCumulo.isNegativeArresto()) {%>
      <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumAnniArresto())%>&nbsp;</font>
      <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumMesiArresto())%>&nbsp;</font>
      <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumGiorniArresto())%>&nbsp;</font>
      <% } else { %>
      <font color="red">nulla residua da espiare</font>
      <% } %>    
    </td>
    <td class="l" style="width:100px">Ammenda</td>
    <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(lPenaResiduaCumulo.getImportoAmmenda()) %></font>&nbsp;<font class="l">Euro</font></td>
  </tr>
  <% } %>
  
  <%--
  Se Arresto < 0 vuol dire c'è pena espiata in eccesso? 
  --%>
  <% if (lPenaResiduaCumulo.isNegativeArresto()) {%>
  <tr>
    <td class="l" >Pena  espiata in eccesso</td>
    <td class="l" style="width:250px">
      <font class="l" color="red">Anni&nbsp;</font><font class="campo" style='color: red;'><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumAnniArresto())%>&nbsp;</font>
      <font class="l" color="red">Mesi&nbsp;</font><font class="campo" style='color: red;'><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumMesiArresto())%>&nbsp;</font>
      <font class="l" color="red">Giorni&nbsp;</font><font class="campo" style='color: red;'><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumGiorniArresto())%>&nbsp;</font>
    </td>
  </tr>
  <% } %>
</table>  

<% if (lPenaRideterminataCumulo.isLibAnt()) { %>
<table cellspacing="2" cellpadding="2">
  <tr>
    <td class="Titolo" colspan="6" >
      Liberazione Anticipata Concessa da Detrarre dal Cumulo
    </td>
  </tr>
  <tr>
    <td></td>
    <td class="c" style="width:130px">Ordinaria</td>
    <td class="c" style="width:130px">Speciale</td>
    <td class="c" style="width:130px">Integrazione</td>
    <td class="c" style="width:130px">D.L.92/2014</td>
    <td class="c" style="width:130px">Scomputo</td>
  </tr>
  <tr>
    <td class="l">
      <font class="label">Totale Liberazione Anticipata (giorni):</font>
    </td>
    <td class="c">
      <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniLA()) %>&nbsp;</font>
    </td>
    <td class="c">
      <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniLS()) %>&nbsp;</font>
    </td>
    <td class="c">
      <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniLI()) %>&nbsp;</font>
    </td>
    <td class="c">
      <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniRiduzione()) %>&nbsp;</font>
    </td>
    <td class="c">
      <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniScomputo()) %>&nbsp;</font>
    </td>
  </tr>
</table>
<% } %>

<% if (lPenaResiduaCumulo.getDataInizio()!=null ) { %>
<table>
  <tr>
    <td class="l">Data Decorrenza Pena: </td>
    <td class="l">
      <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaResiduaCumulo.getDataInizio(),"dd-MM-yyyy"))%>
      </font>
    </td>
    <td class="l">Data Fine Pena: </td>
    <td class="l">
    
    <% if (lPenaResiduaCumulo.isErgastolo()) { %>
      <font class="cRosso">MAI</font>
    <% } else { %>
	<!-- MEV 16 CUMULO: aggiunto controllo preventivo -->
		<% if ((lPenaResiduaCumulo.getDataFinePresunta() != null && lPenaResiduaCumulo.getDataFine() != null) &&
				DateUtils.isEquals(lPenaResiduaCumulo.getDataFinePresunta(), lPenaResiduaCumulo.getDataFine())) { %>
      <font class="campo">
      <% } else { %>
      <font class="cRosso">
      <% } %>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaResiduaCumulo.getDataFine(),"dd-MM-yyyy"))%>
      </font>
    <% } %>
    
    </td>
  </tr>
</table>
<% } %>


<table width="90%">
  	<tr>
    	<td class="Titolo" colspan="4"> Provvedimento di esecuzione di pene concorrenti </td>
  	</tr>
  	<tr>
    	<td class="l" style="width:100px">Tipologia</td>
    	<td class="L" colspan="3" >
      		<font class="campo"> <%=StringUtils.toStringJSP(lProvvedimentoCumulo.getEvento().getDescrMotivo())%></font>
    	</td>
  	</tr>
  	<tr>
    	<td class="l">Data Emissione</td>
    	<td class="L" >
      		<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvvedimentoCumulo.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
    	</td>
   		<td class="l">Data Trasmissione</td>
    	<td class="L">
      		<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvvedimentoCumulo.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy"))%></font>
    	</td>
  	</tr>
  	<tr>
   		<td class="l">Magistrato Competente</td>
   		<td class="L" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(magistrato.getNome())%></font>
   		</td>
  	</tr>
</table>

<table width="90%"> 
  	<tr>
   		<td class="Titolo" colspan="100%">Destinatari</td>
  	</tr>
<%
int count=0;
if (lProvvedimentoCumulo.getNotifiche() != null && lProvvedimentoCumulo.getNotifiche().length >0) {
	while (count < lProvvedimentoCumulo.getNotifiche().length) {
    	NotificaModel lNotMod = lProvvedimentoCumulo.getNotifiche()[count];
    	if (lNotMod.getIstitutoDetenzione() != null) {
      		// Istituto di detenzione
      		IstitutoDetenzioneModel lIstituto = lNotMod.getIstitutoDetenzione();
%>
	<tr>
      	<td class="l">Istituto Detenzione</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(lIstituto.getDescrTipoIstituto())%></font>&nbsp;di&nbsp;
        	<font class="campo"><%=StringUtils.toStringJSP(lIstituto.getDescrizione())%>,&nbsp;<%=StringUtils.toStringJSP(lIstituto.getIndirizzo())%></font>
      	</td>
    </tr>       
<%
    	} else if (lNotMod.getCSSA() != null) {
      		// UEPE
      		CSSAModel lCSSA = lNotMod.getCSSA();
%>
	<tr>
      	<td class="l">UEPE</td>
      	<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(lCSSA.getComune())%>&nbsp;-&nbsp;<%= StringUtils.toStringJSP(lCSSA.getIndirizzo())%></font>
     	</td>
	</tr>
<%
    	} else if (lNotMod.getUfficio() != null) {
    		// TDS/UDS
     		UfficioModel lUfficio = lNotMod.getUfficio();
%>
	<tr>
<%
			if (lUfficio.getCodTipoUfficio().equals("TDS") || lUfficio.getCodTipoUfficio().equals("TDSM")) {
%>
		<td class="l">Tribunale di Sorveglianza</td>
<%
			} else {
%>
		<td class="l">Magistrato di Sorveglianza</td>
<%
			}
%>
        <td class="l">
          	<font class="campo"><%=StringUtils.toStringJSP(lUfficio.getDescrTipoUfficio())%></font>&nbsp;di&nbsp;
          	<font class="campo"><%=StringUtils.toStringJSP(lUfficio.getDescrComune())%></font>
		</td>
	</tr>
<%
		} else if (lNotMod.getAutoritaEsterna() != null
				&& lNotMod.getAvvIdAvvocatoFascicoloSiep() == null) {
      		// Autorità di polizia
      		AutoritaEsternaModel lAutEst = lNotMod.getAutoritaEsterna();
%>
	<tr>
		<td class="l">Autorità di destinazione</td>
       	<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(lAutEst.getDescrTipoAutorita())%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(lAutEst.getDescrSede())%>
<%
			if (lNotMod.getNote() != null) {
%>
         		,&nbsp;<%=StringUtils.toStringJSP(lNotMod.getNote())%>
<%
			}
%>
         	</font>
		</td>
	</tr>
<% 
		}
    	count++;
  	}
}
%>
<tr><td>&nbsp;</td></tr>
<%
count = 0;
if (lProvvedimentoCumulo.getNotifiche() != null && lProvvedimentoCumulo.getNotifiche().length > 0) {
	while (count < lProvvedimentoCumulo.getNotifiche().length) {
    	NotificaModel lNotMod = lProvvedimentoCumulo.getNotifiche()[count];
    	if ("N".equals(lNotMod.getCodTipoNotifica())
    			&& lNotMod.getAvvIdAvvocatoFascicoloSiep() != null) { // && lNotMod.getAutoritaEsterna() != null
			AvvocatoSiepModel lAvvMod = lProvvedimentoCumulo.getNotifiche()[count].getAvvSiep();
      		// AutoritaEsternaModel lAuMod = lProvvedimentoCumulo.getNotifiche()[count].getAutoritaEsterna();
%>
	<tr>
		<td class="l">Avvocato per Notifica</td>
       	<td class="L" colspan="2">
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
     <%if(lNotMod != null && lNotMod.getNote()!= null) {%>
     <tr>
       <td class="l">Note</td>
       <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
     </tr>
     <% } %>
   <% } // end if lNotMod.getCodTipoNotifica().equals("N") 
  count++;
  }
}
%>
</table>
<%
 if(Esiti.size()>0)
 {	 
	 int NroRecord = 0;
	Iterator itx = Esiti.iterator();	%>
	<table width="90%">
	  <tr><td>&nbsp;</td></tr>	
      <tr>
   		<td class="Titolonocap" colspan=4>Stato dei Fascicoli Cumulati appartenenti al Proprio Ufficio</td>
  	  </tr>

<% 	while(itx.hasNext())
	{	
		EsitoArchiviazioniCumuloModel lEsiModel = (EsitoArchiviazioniCumuloModel) itx.next();
		if(lEsiModel!=null && lEsiModel.getIdEsitoArchiviazioniCumulo()!=null)
		{
			NroRecord = NroRecord + 1;
			%>  
			<tr>
			  <td class="l" nowrap>Fascicolo SIEP</td>
			  <td class="L">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lEsiModel.getFasIdFascicoloSiep()%>">
            <%=StringUtils.toStringJSP(lEsiModel.getChiaveAnnoFascSiep())%>
            /
            <%=StringUtils.toStringJSP(lEsiModel.getChiaveProgrFascSiep())%>
          </a>&nbsp;
          <%--
			    <font class="campo"><%=StringUtils.toStringJSP(lEsiModel.getChiaveAnnoFascSiep())%>/<%=StringUtils.toStringJSP(lEsiModel.getChiaveProgrFascSiep())%></font>
			    --%>
			  </td>
		<%	// 15/04/2018  MEV70 Gestione archiviazione manuale dei fascicoli di classe III
			if("E".equals(lEsiModel.getFlagArchiviato()))
			{	%>		  
			  <td class="l"><font class="crosso">Procedimento di classe III non archiviato. Per eventuale Archiviazione Procedere Manualmente</font></td>
			
		<%	} else if("N".equals(lEsiModel.getFlagArchiviato()))
			{	%>		  
			  <td class="l"><font class="crosso">Non è stato possibile Archiviare automaticamente il Fascicolo. Procedere all'Archiviazione Manualmente!</font>&nbsp;&nbsp;&nbsp;
			  				&nbsp;&nbsp;<a href="Javascript:ApriChiudiRigaDettaglio('Dett_<%=NroRecord%>');" title="Descrizione Errore riscontrato"><font class="label" style="color:black; font-size:8pt">(Dettaglio)</font></a></td>
		<%	}
			else if("S".equals(lEsiModel.getFlagArchiviato()))
			{ %>	
			  <td class="L"><font class="campo">Fascicolo Archiviato Correttamente</font></td>
		<%	} %>	  
			</tr>
			
			<!-- Record Hidden con la descrizione dell'Errore in fase di Archiviazione Automatica-->
		    <tr style="display:none" id="Dett_<%=NroRecord%>" >
		      <td class="l" colspan="100%">
		        <font class="campoSmall">
		        <%=StringUtils.toStringJSP(lEsiModel.getDescrizioneEsito(),"&nbsp;")%>
		        </font>
		      </td>
		    </tr>
			
			<tr>
			  <td>&nbsp;</td>
			  <td class="l" nowrap>Stato Attuale</td>
			  <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lEsiModel.getDescrizione())%></font></td>
			</tr>	
<%		}
	} 		%>
	</table>  
<% 	
 }	// Chiude dettagli ESITI Archiviazioni
%>  


</div>
</form>

<% if ("S".equals (lProvvedimentoCumulo.getEvento().getFlagDocumentoRegistrato())) { %>

<table cellpadding="4" cellspacing="4"  align="left"  
       onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">

  <tr>
    <td colspan="3" class="Titolonocap">COMUNICAZIONI</td>
  </tr>
  <tr>
    <td width="250px" class="menulines" nowrap>
      <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadComunicazioniEsecSorv')">Uffici Esecuzione Penale/Sorveglianza</a>
    </td>
    <td width="250px" class="menulines" nowrap>
      <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadComunicazioniCancellerie')">Cancellerie</a>
    </td>
    <td width="250px" class="menulines" nowrap>
      <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadComunicazioniAltro')">Altre Autorità</a>
    </td>
  </tr>
</table>
<% } else { %>
  <div align="left" style="visibility:hidden" id="upld">
    <form name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
        <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActValidaProvvedimentoCumulo">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= lProvvedimentoCumulo.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.modulocumulo.action.ActLoadDettaglioProvvedimentoCumulo">
          </td>
        </tr>
      </table>
    </form>
  </div>
<% } %>

</body>
  
</html>