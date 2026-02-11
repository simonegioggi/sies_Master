<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.RichPMTitoloCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="ListaRichiesteGE" 	scope="request" class="java.util.Vector"/>

<!-- 				GrigliaRichiesteDelPmalGE					 -->
<%
//==============================================================================
//  Form con la griglia delle richieste al GE
//==============================================================================
%>


<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // 
    //==========================================================================
    function eseguiFunzione(action)
    {
      document.GrigliaRichiesteDelPM.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.GrigliaRichiesteDelPM.submit();
    }
    
    //==========================================================================
    // Ritorna alla Lista dei fascicoli coinvolti
    //==========================================================================
    function tornaIndietro(action)
    {
      document.GrigliaRichiesteDelPM.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.GrigliaRichiesteDelPM.submit();
    }
    
    //==========================================================================
    // Visualizza, nasconde il record con il dettaglio dell'istruttoria
    //==========================================================================
    function visualizzaRecord(idRecord,idImg)
    {
      var riga = document.getElementById(idRecord); 
      //var immagine = document.getElementById(idImg);
      
      if (riga.style.display =="none" )
      {
        riga.style.display = "block";
        //immagine.src = "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
      }
      else 
      {
        riga.style.display = "none";
        //immagine.src = "<%=IWebConstants.IMAGES_DIR%>expand.gif";
      }
    }
    
    //===============================================================================
   	//  Richiama l'azione di Dettaglio / Modifica / Cancellazione
   	//===============================================================================
   	var lAzione = "";
    function eseguiAzione(aTipoAzione, aIdRich, aTipoRich)
    {
      if (aTipoAzione=='Dettaglio')
      {
      	if(aTipoRich=='021')
      	{	
        	lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaBenefici";
      	}
      	else if(aTipoRich=='002' || aTipoRich=='003')
      	{
      		lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGEBenefici";
      	}
      	else if(aTipoRich=='004' || aTipoRich=='013' || aTipoRich=='017')
      	{
      		lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaPenaPrincCum";
      	}
      	else if(aTipoRich=='023')
      	{	
           	lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaSSCum";
       	} 
      	else if(aTipoRich=='024' || aTipoRich=='025')
      	{	
           	lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGESostPenaAcc";
       	} 
      	else if(aTipoRich=='026' || aTipoRich=='027' ||  aTipoRich=='028')
      	{	
           	lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaPenaAcc";
       	}
      	else if(aTipoRich=='029' || aTipoRich=='030')
      	{	
           	lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGEApplicaPenaAccCum";
       	}
      	else if(aTipoRich=='014')
      	{	
           	lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGEAltro";
       	}
      	
        document.GrigliaRichiesteDelPM.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>.value = aIdRich;
        document.GrigliaRichiesteDelPM.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.GrigliaRichiesteDelPM.submit();
      }
      else if (aTipoAzione=='Modifica')
      {
      	 var msgConfirm="";
      	 if(aTipoRich=='021')
     	 {	
      		 lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiestaGERevocaBenefici";
      		 msgConfirm = "Attenzione: sono modificabili esclusivamente i dati della Richiesta. \nPer modificare i dati relativi ai Titoli e/o Benefici, Cancellare e poi Reinserire la Richiesta. \nSi vuole procedere con la Modifica?";
     	 }
      	 else if(aTipoRich=='002' || aTipoRich=='003')
      	 {	 
      		 lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiestaGEBenefici"; 
      	 	 msgConfirm = "Attenzione: sono modificabili esclusivamente i dati della Richiesta. \nPer modificare i dati relativi ai Titoli / Reati / Pene Acc. / Misure Sic. Cancellare e poi Reinserire la Richiesta. \nSi vuole procedere con la Modifica?"; 
      	 }
      	 else if(aTipoRich=='004' || aTipoRich=='013' || aTipoRich=='017')
       	 {
      		 lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiestaGERevocaPenaPrinc"; 
      	 	 msgConfirm = "Attenzione: sono modificabili esclusivamente i dati della Richiesta. \nPer modificare i dati relativi ai Titoli e/o Reati, \nCancellare e poi Reinserire la Richiesta. \nSi vuole procedere con la Modifica?"; 
       	 }
      	 else if(aTipoRich=='023')
     	 {	
      		 lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiestaGERevocaSS";
      		 msgConfirm = "Attenzione: sono modificabili esclusivamente i dati della Richiesta. \nPer modificare i dati relativi ai Titoli e/o Sanzioni Sostitutive, \nCancellare e poi Reinserire la Richiesta. \nSi vuole procedere con la Modifica?";
     	 }
      	 else if(aTipoRich=='024' || aTipoRich=='025')
       	 {	
            lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiestaGESostPA";
            msgConfirm = "Attenzione: sono modificabili esclusivamente i dati della Richiesta. \nPer modificare i dati relativi ai Titoli e/o Pene Accessorie, \nCancellare e poi Reinserire la Richiesta. \nSi vuole procedere con la Modifica?";
       	 }
      	 else if(aTipoRich=='026' || aTipoRich=='027' ||  aTipoRich=='028')
       	 {	
           	lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiestaGERevocaPA";
            msgConfirm = "Attenzione: sono modificabili esclusivamente i dati della Richiesta. \nPer modificare i dati relativi ai Titoli e/o Pene Accessorie, \nCancellare e poi Reinserire la Richiesta. \nSi vuole procedere con la Modifica?";
         }

      	 if(aTipoRich !='029' && aTipoRich !='030' && aTipoRich !='014')
      	 {	 
	      	 if (window.confirm(msgConfirm)) 
	         {
		         document.GrigliaRichiesteDelPM.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>.value = aIdRich;
		         document.GrigliaRichiesteDelPM.modalita.value="M";
		         document.GrigliaRichiesteDelPM.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
		         document.GrigliaRichiesteDelPM.submit();
	         }
      	 }
      	 else
      	 {
      		 if(aTipoRich == '014')
      		 {	 
      			 lAzione ="siap.siep.modulocumulo.action.ActLoadInsRichiestaGEAltro";
      		 }	 
      		 else if(aTipoRich == '029' || aTipoRich == '030')
      		 {	 
      		 	lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiestaGEApplicaPA";
      		 }	
      		 
      		 document.GrigliaRichiesteDelPM.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>.value = aIdRich;
	         document.GrigliaRichiesteDelPM.modalita.value="M";
	         document.GrigliaRichiesteDelPM.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
	         document.GrigliaRichiesteDelPM.submit();
      	 }	 
      }
      else if (aTipoAzione=='Cancella')
      {
      	 if(aTipoRich=='021')
      	 {	
        	lAzione = "siap.siep.modulocumulo.action.ActInserisciRichiestaGERevocaBenefici";
      	 }
      	 else if(aTipoRich=='002' || aTipoRich=='003')
      	 {
      	 	lAzione = "siap.siep.modulocumulo.action.ActInserisciRichiestaGEBenefici";
      	 }
      	 else if(aTipoRich=='004' || aTipoRich=='013' || aTipoRich=='017')
       	 {
       		lAzione = "siap.siep.modulocumulo.action.ActInsRichiestaGERevocaPenaPrincCum";
       	 }
      	 else if(aTipoRich=='023')
      	 {	
          	lAzione = "siap.siep.modulocumulo.action.ActInserisciRichiestaGERevocaSSCum";
       	 }
      	 else if(aTipoRich=='024' || aTipoRich=='025')
       	 {	
            lAzione = "siap.siep.modulocumulo.action.ActInserisciRichiestaGESostPenaAcc";
       	 }
      	 else if(aTipoRich=='026' || aTipoRich=='027' ||  aTipoRich=='028')
       	 {	
      		 lAzione = "siap.siep.modulocumulo.action.ActInserisciRichiestaGERevocaPenaAcc";
       	 }
      	 else if(aTipoRich=='029' || aTipoRich=='030')
       	 {	
            lAzione = "siap.siep.modulocumulo.action.ActInserisciRichiestaGEApplicaPACum";
       	 }
      	 else if(aTipoRich=='014')
      	 {	
          	lAzione = "siap.siep.modulocumulo.action.ActInserisciRichiestaGEAltro";
       	 }
      	 
         document.GrigliaRichiesteDelPM.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>.value = aIdRich;
         document.GrigliaRichiesteDelPM.modalita.value="C";
         document.GrigliaRichiesteDelPM.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
         retValue = confirm(" Confermi la Cancellazione ? ");
      	 if (retValue)
      	 {	
      		document.GrigliaRichiesteDelPM.submit();
      	 }	
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
      width:100%;
      height:100%;
    }
  </STYLE>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Richieste del PM al GE&nbsp;</font>
      </td>
      <td class="LBG"><!-- Tasto indietro  -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPM')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>

<% // INCLUDE DEL DETTAGLIO FASCICOLO%>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>

<% // INCLUDE DEL DETTAGLIO DELL'ISTRUTTORIA %>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="GrigliaRichiesteDelPM">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>" value="" >

  <input type="hidden" name="modalita"   value="I">
  
  <table cellpadding="2" cellspacing="2" width="95%" align="center"
         onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" 
         onMousedown="over_effect(event,'inset')"  onMouseup="over_effect(event,'outset')">
    <%
    //========================================================================
    //                           
    //========================================================================
    %>
    <tr>
      <td colspan="3" class="Titolonocap">Richieste del PM dell'Esecuzione</td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadElencoTitoliRichiestaGEBenefici')">Richiesta Applicazione Benefici</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadElencoTitoliRichiestaGERevocaBenefici')">Richiesta Revoca Benefici</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadElencoTitoliRichiestaGERevocaPenaCum')">Richiesta Revoca della sentenza per abolizione del reato</a>
      </td>
    </tr>
    <%
    //========================================================================
    //                            
    //========================================================================
    %>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadElencoTitoliRichiestaGERevocaSSCum')">Richiesta Revoca Sanzioni Sostitutive di una Pena</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadElencoTitoliRichGESostituzionePenaAccCum')">Richiesta Sostituzione di Pena Accessoria</a>
      </td>
      <td width="32%" class="menulines" nowrap>
      	<%-- 
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadInsRichiestaGERevocaPA')">Richiesta Revoca di Pena Accessoria</a>
        --%>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadElencoTitoliRichGERevocaPenaAccCum')">Richiesta Revoca di Pena Accessoria</a>
      </td>
    </tr>
    <%
    //========================================================================
    //                            
    //========================================================================
    %>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadInsRichiestaGEApplicaPA')">Richiesta Applicazione di Pena Accessoria</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadInsRichiestaGEAltro')">Altre Richieste</a>
      </td>
    </tr>

    <%
    //========================================================================
    //              Stampa e Invio Richieste, Visualizzazione
    //========================================================================
    %>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td colspan="3" class="Titolonocap">Richieste al Giudice dell'Esecuzione</td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadEmettiRichiestaDelPM')">Stampa Richiesta da Inviare</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadVisualizzaRichieste')">Visualizza Richieste Inviate</a>
      </td>

    </tr>
    <tr><td>&nbsp;</td></tr>
    
    <%
    //========================================================================
    //         INSERIRE QUI LA LISTE DELLE RICHIESTE GIA' A SISTEMA
    //========================================================================
    %>
<tr>
<td colspan="100%" align="center">
<table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr style="display:block" id="richiesta_1">
      <td colspan="100%" align="center">
      
        <table cellspacing="2" cellpadding="2" align="center" width="95%">
          <tr>
            <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
            <td class="int">Data Richiesta</td>
            <td class="int">Tipo Richiesta</td>
            <td class="int" title="Indica se già inviata">Stato</td>
            <td class="int" title="Titolo per il quale è stata effettuata la richiesta">Titoli</td>
            <td class="int">Anticipazione</td> 	<%// Indica se la richiesta è con anticipazione degli Effetti%>
            <td class="int" >Decisione<br>GE</td> 	<%// Indica se è presente lo scarico della DECISIONE DEL GE %>
            <td class="int">Azioni</td>
          </tr>
 <%
 	int id_record = 0;
 	String lAnticipazione="";
 	String lStatoRich = "";
 	String lSentenza = "";
 	String lTipo = "";
	Iterator itx = ListaRichiesteGE.iterator();
 	while ( itx.hasNext()) 
 	{
 		RichiestePmInCumuloModel lRichiestaGE = (RichiestePmInCumuloModel)itx.next();
 		
 		// Anticipazione
 		if("A".equals(lRichiestaGE.getFlagAppProvvisoria()) )
 			lAnticipazione="Si";
 		else if("R".equals(lRichiestaGE.getFlagAppProvvisoria()) )
 			lAnticipazione="No";
 		else
 			lAnticipazione="-";
 		
 		// stato
 		lStatoRich = "";
 		if(lRichiestaGE.getRicIdRichiesteInviateCum() != null )
	 		lStatoRich = "Inviata";
 		else
 			lStatoRich = "Da Inviare";
 		
 		// Titolo
 		lSentenza = "";
 		if(lRichiestaGE.getAnnoSentenza()!=null && lRichiestaGE.getNumeroSentenza()!=null)
 		{
 			lSentenza = lRichiestaGE.getAnnoSentenza()+"/"+lRichiestaGE.getNumeroSentenza();
 			
 		    // MEV_2025-48 - ALTRO - Visualizza data Sentenza su Richieste PM
 		    if (lRichiestaGE.getDataSentenza()!=null){
 		       lSentenza+=" del "+DateUtils.getDateToString(lRichiestaGE.getDataSentenza(), "dd/MM/yyyy") ;
 		    }
 			
 			if( !("null").equals(lRichiestaGE.getAltri()) && 
 				!"".equals(lRichiestaGE.getAltri()) &&
 				!"0".equals(lRichiestaGE.getAltri()) )
 			{
 				lSentenza += " + "+lRichiestaGE.getAltri();
 			}
 		}
 		
 		// Descrizione Richiesta
 		lTipo = "";
 /*		if( !"021".equals(lRichiestaGE.getCodTipoAnnotazione()) &&
			!"023".equals(lRichiestaGE.getCodTipoAnnotazione()) &&
			!"024".equals(lRichiestaGE.getCodTipoAnnotazione()) && 
			!"025".equals(lRichiestaGE.getCodTipoAnnotazione()) )
*/	 
		if( "002".equals(lRichiestaGE.getCodTipoAnnotazione()) ||
			"003".equals(lRichiestaGE.getCodTipoAnnotazione()) )
 		{
 			lTipo +=" Applicazione ";
 		}
		else if( "004".equals(lRichiestaGE.getCodTipoAnnotazione()) ||
				 "013".equals(lRichiestaGE.getCodTipoAnnotazione()) ||
				 "017".equals(lRichiestaGE.getCodTipoAnnotazione()) )
 		{
 			lTipo +="Revoca della sentenza per abolizione del reato: ";
 		}
		
 		
 %>         
 	<tr>
 	  <td class="c" nowrap ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRichiestaGE.getDataEmissione(), "dd-MM-yyyy"), " - ") %></td>	
 	  <td class="c"><%=lTipo%>&nbsp;<%=StringUtils.toStringJSP(lRichiestaGE.getDescrTipoAnnotazione(), "") %></td>
      <td class="c"><%=lStatoRich%></td>
	  <td class="c" style="text-align:left">&nbsp;Sentenza N. <%=StringUtils.toStringJSP(lSentenza)%></td>
      <td class="c">&nbsp;<%=StringUtils.toStringJSP(lAnticipazione) %></td>
      <td class="c"> 
<%	if(lRichiestaGE.getDecisioneGeSorvCum()!=null && lRichiestaGE.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum()!=null)
  	{ %>
  		<font style="color:green"><img src="/images/V.gif"></font>
<%	}
	else	
	{	%>
		&nbsp;
<%	} %>	  		    
      </td>

			<!-- 				azioni				 -->
      <td class="c" style="text-align:center" nowrap> &nbsp;
          <a href="javascript:eseguiAzione('Dettaglio', <%=lRichiestaGE.getIdRichiestePmInCumulo()%>, '<%=lRichiestaGE.getCodTipoAnnotazione()%>')">
        <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Richiesta" border="0"></a>
              <!--a href="javascript:eseguiAzione('Stampa','')">
                <img src="/images/print.gif" width="12" height="12" alt="Dettaglio Stampa" border="0">
              </a-->
 <% 
      if(IstruttoriaCumulo.getFlagStato().equals("A") 
      	 &&	lStatoRich.compareTo("Da Inviare")==0 )   // NON deve essere stata già INVIATA
      { %>
          <a href="javascript:eseguiAzione('Modifica', <%=lRichiestaGE.getIdRichiestePmInCumulo()%>,'<%=lRichiestaGE.getCodTipoAnnotazione()%>')">
        <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
     	  <a href="javascript:eseguiAzione('Cancella', <%=lRichiestaGE.getIdRichiestePmInCumulo()%>, '<%=lRichiestaGE.getCodTipoAnnotazione()%>')">
        <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
   <% } %>
      </td>
    </tr>

<%	}  // Chiude Ciclo for sulle Richieste%>

        </table>
</td>
</tr>
</table>
      </td>
    </tr>
    
</table>
    <%
    //========================================================================
    //                            
    //========================================================================
    %>
</form>    
</body>
</html>