<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page
	import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>


<%@ page
	import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page
	import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page
	import="siap.siep.modulocumulo.action.ICostantiStatoEsecuzioneCumulo"%>


<%@ page import="siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils"%>

<%@ page import="siap.siep.modulocumulo.util.ModuloCumuloUtils"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request"
	class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel" />
<jsp:useBean id="TitoloInCumulo" scope="request"
	class="siap.siep.modulocumulo.model.TitoloCumulatoModel" />

<jsp:useBean id="StatoEsecuzioneTitolo" scope="request"
	class="java.util.Vector" />

<%
//==============================================================================
// Form con le funzioni di gestione dei Dati Analitici del Singolo Titolo
// e visualizzazione dello stato di esecuzione
//==============================================================================

%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript"
	src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript">
    function eseguiFunzione(action)
    {
      document.GrigliaDatiAnalitici.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.GrigliaDatiAnalitici.submit();
    }
    
    function eseguiAzione(aTipoAzione, aIdStatoEsec)
    {
      lAzione = "siap.siep.modulocumulo.action.ActCancellaProvvStatEsec";
      document.GrigliaDatiAnalitici.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.GrigliaDatiAnalitici.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdStatoEsec;
      document.GrigliaDatiAnalitici.azione.value = aTipoAzione;
      
      if (aTipoAzione=='<%=ICostantiStatoEsecuzioneCumulo.AZIONE_DETTAGLIO%>'){
        document.GrigliaDatiAnalitici.submit();
      }
      else if (aTipoAzione=='<%=ICostantiStatoEsecuzioneCumulo.AZIONE_MODIFICA%>'){
        alert ("Funzione in fase di implementazione");
      }
      else if (aTipoAzione=='<%=ICostantiStatoEsecuzioneCumulo.AZIONE_CANCELLA%>'){
        // Cancellazione fisica richiedo conferma
        var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
        if (window.confirm(msgConfirm)) {
          document.GrigliaDatiAnalitici.submit();
        }
      }      
    }
    
    function effettoTree(a)
    {
      var node;
      node=document.getElementById("elenco"+a);
      node.style.display = (node.style.display == "none")? "block" : "none";
      document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
      return false;
    }


    function stampaSiep(lAzione)
    {
      var  hrefStampa = lAzione;
      var lIndice = hrefStampa.indexOf("?");

      var parametri = hrefStampa.substring(lIndice+1,lAzione.length);

      stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
    }

    
    //==========================================================================
    // Ritorna alla Lista dei fascicoli coinvolti
    //==========================================================================
    function tornaIndietro(action)
    {
      document.GrigliaDatiAnalitici.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.GrigliaDatiAnalitici.submit();
    }
    
    function visualizzaGriglia (id_griglia)
    {
      var arrGriglie = new Array (document.getElementById("griglia_Dati_Analitici"),
                                  document.getElementById("griglia_Attivita_PM"),
                                  document.getElementById("griglia_Attivita_GE"),
                                  document.getElementById("griglia_Attivita_SORV")
                                 );
                                 
      var arrBtn = new Array (document.getElementById("btn_Dati_Analitici"),
                              document.getElementById("btn_Attivita_PM"),
                              document.getElementById("btn_Attivita_GE"),
                              document.getElementById("btn_Attivita_SORV")
                             );
                                 

      var griglia = document.getElementById(id_griglia);
      
      var indexSel = 0;
      for (index = 0; index < arrGriglie.length; index++) {
        if (griglia.id == arrGriglie[index].id){
          indexSel = index;
        }
      }
      
      
      if (griglia.style.display=="block") {
        // Sto deselezionando
        griglia.style.display="none";
        
        //Abilito tutti i tasti
        for (index = 0; index < arrBtn.length; index++) {
          arrBtn[index].disabled = false;
        }        
        $('#TitoloStatEsec').html('Stato di esecuzione');        
      }
      else {
        griglia.style.display="block";
        for (index = 0; index < arrBtn.length; index++) {
          if (index!=indexSel)
            arrBtn[index].disabled = true;
        } 
        if (griglia.id=='griglia_Dati_Analitici'){
          $('#TitoloStatEsec').html('Stato di esecuzione');
        }
        else if (griglia.id=='griglia_Attivita_PM'){
          $('#TitoloStatEsec').html('Stato di esecuzione - Attività del PM');
        }
        else if (griglia.id=='griglia_Attivita_GE'){
          $('#TitoloStatEsec').html('Stato di esecuzione - Attività del GE');
        }
        else if (griglia.id=='griglia_Attivita_SORV'){
          $('#TitoloStatEsec').html('Stato di esecuzione - Attività della Sorveglianza');
        }        
      }
      
      if (id_griglia!='griglia_Dati_Analitici')
        document.getElementById("griglia_Dati_Analitici").style.display = "none";
      
      if (id_griglia!='griglia_Attivita_PM')
        document.getElementById("griglia_Attivita_PM").style.display = "none";
      
      if (id_griglia!='griglia_Attivita_GE')
        document.getElementById("griglia_Attivita_GE").style.display = "none";

      if (id_griglia!='griglia_Attivita_SORV')
        document.getElementById("griglia_Attivita_SORV").style.display = "none";
    
    }
    
  <% if (!ModuloCumuloUtils.isMev42Abilitata()) { %>
    $(document).ready(function(){
      visualizzaGriglia('griglia_Dati_Analitici');
    });
  <% } %>

    //=============================================
    function StatoEsecuzionePopup (a_formname)
    {
      <%
      String lStrParametri = "";
      lStrParametri +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
      lStrParametri +="&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO+"="+TitoloInCumulo.getIdTitoloCumulato();
      %>
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActRicercaStatoEsecuzioneFascicolo&formname="+a_formname+"<%=lStrParametri%>"
                          , "Stato_Esecuzione_Fascicolo"
                          , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=yes, width=800, height=500");
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
.menulines {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

.menulines a {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </STYLE>
</head>

<body class="corpo">
	<table>
    	<tr>
      		<td class="LBG"><a href="Javascript:window.print();"><img align="middle"
					src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
					alt="Stampa questa videata" border=0></a></td>
			<td class="LBG"><font class="label">Funzione :</font>&nbsp; <font
				class="campo">Gestione Dati Analitici</font></td>
			<td class="LBG">
				<!-- Tasto indietro alla Lista dei fascicoli coinvolti -->
				<a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti')">
					<img align="middle"
					src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su"
					width="24" height="24" border="0">
				</a>
			</td>
		</tr>
	</table>
  	<br>

  <%--
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  --%>
    
<% // INCLUDE DEL DETTAGLIO DEL TITOLO%>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
			<td><jsp:include
					page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>" />
      </td>
    </tr>
    <tr>
			<td><jsp:include
					page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>" />
      </td>
    </tr>
  </table>

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="GrigliaDatiAnalitici">
<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>" value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
<input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>" value="">
<input type="hidden" name="azione" value="">
<table cellpadding="2" cellspacing="2" width="95%" align="center"
		onMouseover="over_effect(event,'outset')"
		onMouseout="over_effect(event,'solid')"
		onMousedown="over_effect(event,'inset')"
		onMouseup="over_effect(event,'outset')">
	<tr>
      	<!--td width="25%" class="menulines" nowrap-->
      	<td width="25%" align="center" nowrap>
<% if (ModuloCumuloUtils.isMev42Abilitata()) { %>
			<input id="btn_Dati_Analitici" type="button" class="bottone" style="width: 200" onClick="visualizzaGriglia('griglia_Dati_Analitici');" name="Dati analitici" value="Dati analitici">
<% } else { %>
			<input id="btn_Dati_Analitici" type="button" class="bottone" style="width: 200" name="Dati analitici" value="Dati analitici">
<% } %>
		</td>
		<td width="25%" align="center" nowrap>
			<input id="btn_Attivita_PM" type="button" class="bottone" style="width: 200" onClick="visualizzaGriglia('griglia_Attivita_PM');" name="Dati analitici" value="Attività del PM">
		</td>
		<td width="25%" align="center" nowrap>
			<input id="btn_Attivita_GE" type="button" class="bottone" style="width: 200" onClick="visualizzaGriglia('griglia_Attivita_GE');" name="Dati analitici" value="Attività del GE">
		</td>
		<td width="25%" align="center" nowrap>
			<input id="btn_Attivita_SORV" type="button" class="bottone" style="width: 200" onClick="visualizzaGriglia('griglia_Attivita_SORV');" name="Dati analitici" value="Attività della Sorveglianza">
      	</td>
	</tr> 
	<tr>
		<td>&nbsp;</td>
	</tr>
<%
//==========================================================================
//  DATI ANALITICI
//==========================================================================
%>    
	<tr id="griglia_Dati_Analitici" style="display:none" >
      	<td colspan="4">
        	<table cellspacing=2 cellpadding=2 width="100%">
          		<tr>
            		<td colspan="3" class="Titolonocap">Gestione Dati Analitici</td>
          		</tr>        
          		<tr>
					<td width="32%" class="menulines" nowrap>
						<a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaReatoCumulo')">Reati Commessi</a>
					</td>
					<td width="32%" class="menulines" nowrap>
						<a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaPenaComplessivaCumulo')">Pena Principale</a>
					</td>
					<td width="32%" class="menulines" nowrap>
						<a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaPeneAccessorieCumulo')">Pene Accessorie</a>
					</td>
          		</tr>
          		<tr>
					<td width="32%" class="menulines" nowrap>
						<a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaMisuraSicurezzaCumulo')">Misure di Sicurezza</a>
					</td>
					<td width="32%" class="menulines" nowrap>
						<a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaMisureCautelariCumulo')">Misure Cautelari</a>
					</td>
					<td width="32%" class="menulines" nowrap>
						<a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaBeneficiCumulo')">Benefici (disposti in sentenza)</a>
					</td>
          		</tr>
          		<tr>
					<td width="32%" class="menulines" nowrap>
						<a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaRevocheBeneficiCumulo')">Revoche (disposte in sentenza)</a>
					</td>
		            <!--
		            <td width="32%" class="menulines" nowrap>
		              	<a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaCircostanzaCumulo')">Aggravanti soggettive / Attenuanti</a>
		            </td>
		            -->
				</tr>
			</table>
		</td>
	</tr>
<%
//==========================================================================
//  ATTIVITA' DEL PM
//==========================================================================
%>
	<tr id="griglia_Attivita_PM" style="display:none" >
      	<td colspan="4">
        	<table cellspacing=2 cellpadding=2 width="100%">
          		<tr>
            		<td colspan="3" class="Titolonocap">Attivita' del PM</td>
          		</tr>
          		<tr>
					<td width="32%" class="menulines" nowrap><a
						href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaDecretiSospPM')">Decreti
							di sospensione</a></td>
					<td width="32%" class="menulines" nowrap><a
						href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaPresoffertoCumulo')">Presofferto</a>
					     </td>
					<td width="32%" class="menulines" nowrap><a
						href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaFungibilitaCumulo')">Fungibilità</a>
            		</td>
				</tr>
          		<tr>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaEspiato')">Espiazione</a>
            </td>
            <td width="32%" class="menulines" nowrap>
              <%// Vedi Rdeterminazione Pena - Annotazione Pagamento Pena Pecuniaria %>
								<a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaPagamentiPP')">Annotazione Pagamento Pena Pecuniaria</a>
							</td>
							<td width="32%" class="menulines" nowrap><a   <%//siap.siep.calcolopena.action.ActLoadRidetPenaAltro %>
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaRidetPenaPMAltroCumulo')">Rideterminazione Pena Altro</a>
            </td>            
          </tr>
        </table>
      </td>
    </tr>
    
    <%
    //==========================================================================
    //  ATTIVITA' DEL GE
    //==========================================================================
    %>
    <tr id="griglia_Attivita_GE" style="display:none" >
      <td colspan="4">
        <table cellspacing=2 cellpadding=2 width="100%">
          <tr>
            <td colspan="3" class="Titolonocap">Attivita' del GE</td>
          </tr>
          <tr>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaAmnistiaIndultoCumulo')">Amnistia-Indulto</a>
            </td>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaDepenalizzazioneCumulo')">Depenalizzazione</a>
            </td>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaIncostituzionalitaCumulo')">Incostituzionalità</a>
            </td>
          </tr>
          <tr>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaSospensioneCumulo')">Sospensione</a>
							</td>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaInterruzioneCumulo')">Interruzione</a>
            </td>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaAnnotazioneRevocaBeneficioCumulo')">Revoca Beneficio</a>
            </td>
          </tr>          
        </table>
      </td>
    </tr>
    
    <%
    //==========================================================================
    //  ATTIVITA' DELLA SORVEGLIANZA
    //==========================================================================
    %>    
    <tr id="griglia_Attivita_SORV" style="display:none" >
      <td colspan="4">
        <table cellspacing=2 cellpadding=2 width="100%">
          <tr>
							<td colspan="3" class="Titolonocap">Attivita' della
								Sorveglianza</td>
          </tr>
          <tr>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaLiberazioneAnticipataCumulo')">Liberazioni
									Anticipate</a></td>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaScomputoPermessiCumulo')">Scomputo
									Permessi</a></td>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaRimediRisarcitoriDL201492Cumulo')">Rimedi
									Risarcitori D.L. 26 giugno 2014, n. 92</a></td>
          </tr>
          <tr>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaConcMisureAlternativeCumulo')">Concessioni
									Misure Alternative</a></td>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaRevocheMisureAlternativeCumulo')">Revoche
									Misure Alternative</a></td>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaSospMisureAlternativeCumulo')">Sospensioni
									Misure Alternative</a></td>
          </tr>
          <tr>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaSospEsecuzionePenaCumulo')">Sospensione
									Esecuzione</a></td>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaDifferimentoPenaCumulo')">Differimento</a>
            </td>
							<td width="32%" class="menulines" nowrap><a
								href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaEspulsioneCumulo')">Espulsione</a>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    
			<tr>
				<td></td>
			</tr>

<%
//==============================================================================
//  CARICAMENTO STATO ESECUZIONE
//==============================================================================
%>
<% if (ModuloCumuloUtils.isMev42Abilitata() ) { %>
    <%
    if (ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(IstruttoriaCumulo.getFlagStato())) {
    %>
    <tr>
				<td class="l" colspan="4"><a
					href="Javascript:StatoEsecuzionePopup('LoadInserisciUlterioriContinuazioniCumulo');">
						Seleziona Stato Esecuzione <img
						src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0>
				</a></td>
    </tr>
    <% } %>



    <tr>
				<td colspan="4" class="Titolonocap" id="TitoloStatEsec">Stato
					di esecuzione</td>
    </tr> 
    <tr>
      <td colspan="4">
        <table cellspacing=2 cellpadding=2 width="100%">
          <tr>
            <td class="int" width="120px" nowrap>Data Emissione</td>
            <td class="int">Provvedimento</td>
            <td class="int">Esito</td>
            <td class="int">Autorità</td>
            <td class="int" width="70px" nowrap>Azioni</td>
          </tr>

          <%
          Iterator itx = StatoEsecuzioneTitolo.iterator();
          while ( itx.hasNext())
          {
            StatoEsecTitoloCumulatoModel lSetModel = (StatoEsecTitoloCumulatoModel) itx.next();
            %>
            
            <tr>
              <td class="c" nowrap><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSetModel.getDataEmissione(),"dd-MM-yyyy"))%>&nbsp;</td>
              <td class="l">
								<% if ("I".equals(lSetModel.getFlagStato())) {%>(*)<% }%> <%=StringUtils.toStringJSP(lSetModel.getDescrTipoProvvedimento())%>&nbsp;<%=StringUtils.toStringJSP(lSetModel.getDescrMotivo(),"-")%>
              </td>
                        
              <td class="c"><%=StringUtils.toStringJSP(lSetModel.getDescrEsitoTenore(),"&nbsp;")%></td>
              
              <% if ( StatoEsecuzioneCumuloUtils.isSospensioneC5VVR (lSetModel.getCodMotivo()) ) { %>
              <td class="l"><%=StringUtils.toStringJSP(lSetModel.getDescrAutoritaEmittente())+ " " + StringUtils.toStringJSP(lSetModel.getDescrLuogoEmittente())%>&nbsp;</td>
              <% } else { %>
              <td class="l"><%=StringUtils.toStringJSP(lSetModel.getDescrUfficioEmittente())+ " " + StringUtils.toStringJSP(lSetModel.getDescrLuogoEmittente())%>&nbsp;</td>
              <% } %>
							<td class="c" nowrap><a
								href="javascript:eseguiAzione('<%=ICostantiStatoEsecuzioneCumulo.AZIONE_DETTAGLIO%>',<%=lSetModel.getIdStatoEsecTitoloCumulato()%> )">
									<img src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" width="12"
									height="12" alt="Dettaglio" border="0"></a> 
                <!--
                <a href="javascript:eseguiAzione('<%=ICostantiStatoEsecuzioneCumulo.AZIONE_MODIFICA%>',<%=lSetModel.getIdStatoEsecTitoloCumulato()%>)">
                  <img src="<%=IWebConstants.IMAGES_DIR%>modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
                --> <%
                if (ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(IstruttoriaCumulo.getFlagStato())) {
                %> <a
								href="javascript:eseguiAzione('<%=ICostantiStatoEsecuzioneCumulo.AZIONE_CANCELLA%>',<%=lSetModel.getIdStatoEsecTitoloCumulato()%>)">
									<img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" width="12"
									height="12" alt="Elimina" border="0"></a> <% } %></td>
						</tr>
                <% } %>
						<tr>
							<td>&nbsp;</td>
            </tr>
          <% } %>
  			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>