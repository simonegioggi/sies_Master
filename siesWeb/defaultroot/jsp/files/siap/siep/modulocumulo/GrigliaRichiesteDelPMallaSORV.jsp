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

<jsp:useBean id="IstruttoriaCumulo"  scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="ListaRichiesteSORV" scope="request" class="java.util.Vector"/>

<%
//==============================================================================
//  Form con la griglia delle richieste alla Sorveglianza
//==============================================================================
%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">
    //==========================================================================
    // 
    //==========================================================================
    function eseguiFunzione(action)
    {
      document.GrigliaRichiesteDelPMallaSORV.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.GrigliaRichiesteDelPMallaSORV.submit();
    }
    
    //==========================================================================
    // Ritorna alla Lista dei fascicoli coinvolti
    //==========================================================================
    function tornaIndietro(action)
    {
      document.GrigliaRichiesteDelPMallaSORV.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.GrigliaRichiesteDelPMallaSORV.submit();
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
    function eseguiAzione(aTipoAzione, aIdRich, aTipoAnn)
    {
    	var AnnList = [020,022,014,031];
      	if (aTipoAzione=='Dettaglio')
      	{
    		if (aTipoAnn==020)
        		lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVRevocaLA";
    		else if (aTipoAnn==022)
        		lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVUnificaMS";
    		else if (aTipoAnn==014)
        		lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVAltro";
    		else if (aTipoAnn==031)
        		lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVRevocaMA";

    		if( $.inArray(aTipoAnn, AnnList) != -1){
	        	document.GrigliaRichiesteDelPMallaSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>.value = aIdRich;
	        	document.GrigliaRichiesteDelPMallaSORV.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
	        	document.GrigliaRichiesteDelPMallaSORV.submit();			
			}
    		else
            	alert ("Errore: il tipo di Richiesta del PM alla Sorveglianza non è valido");
      	}
      	else if (aTipoAzione=='Modifica')
      	{ 
      	 	var msgConfirm = ""; 
       		if (aTipoAnn==020){
       			lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiestaSORVRevocaLA";
           		msgConfirm = "Attenzione: sono modificabili esclusivamente i dati della Richiesta. \nPer modificare i dati relativi ai Titoli / Provvedimenti di L.A. Cancellare e Reinserire la Richiesta. \nSi vuole procedere con la Modifica?"; 
       		}
   			else if (aTipoAnn==022) {
       			lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiestaSORVUnificaMS";
           	 	msgConfirm = "Attenzione: sono modificabili esclusivamente i dati della Richiesta. \nPer modificare i dati relativi ai Titoli / Misure di Sicurezza, Cancellare e Reinserire la Richiesta. \nSi vuole procedere con la Modifica?"; 
   			}
   			else if (aTipoAnn==031) {
       			lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiestaSORVRevocaMA";
           	 	msgConfirm = "Attenzione: sono modificabili esclusivamente i dati della Richiesta. \nPer modificare i dati relativi ai Titoli / Misure Alternative, Cancellare e Reinserire la Richiesta. \nSi vuole procedere con la Modifica?"; 
   			}
   			else if (aTipoAnn==014) {
       			lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiestaSORVAltro";
   			}
       		
    		if( $.inArray(aTipoAnn, AnnList) != -1)
    		{
         		document.GrigliaRichiesteDelPMallaSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>.value = aIdRich;
         		document.GrigliaRichiesteDelPMallaSORV.modalita.value="M";
         		document.GrigliaRichiesteDelPMallaSORV.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
         		
         		if(aTipoAnn==014)
         		{
         			document.GrigliaRichiesteDelPMallaSORV.submit();
         		}
         		else if (window.confirm(msgConfirm)) 
             	{
         			document.GrigliaRichiesteDelPMallaSORV.submit();
        		}
   			}
       		else
               	alert ("Errore: il tipo di Richiesta del PM alla Sorveglianza non è valido");

      	}
      	else if (aTipoAzione=='Cancella')
      	{
       		if (aTipoAnn==020)
        		lAzione = "siap.siep.modulocumulo.action.ActInserisciRichiestaSORVRevocaLA";
    		else if (aTipoAnn==022)
        		lAzione = "siap.siep.modulocumulo.action.ActInserisciRichiestaSORVUnificaMS";
    		else if (aTipoAnn==014)
        		lAzione = "siap.siep.modulocumulo.action.ActInserisciRichiestaSORVAltro";
    		else if (aTipoAnn==031)
        		lAzione = "siap.siep.modulocumulo.action.ActInserisciRichiestaSORVRevocaMA";
       		
    		if( $.inArray(aTipoAnn, AnnList) != -1){
	         	document.GrigliaRichiesteDelPMallaSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>.value = aIdRich;
	         	document.GrigliaRichiesteDelPMallaSORV.modalita.value="C";
	         	document.GrigliaRichiesteDelPMallaSORV.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;

         		retValue = confirm(" Confermi la Cancellazione ? ");
      	 		if (retValue)
      	 		{	
      				document.GrigliaRichiesteDelPMallaSORV.submit();
      	 		}
			} else
            	alert ("Errore: il tipo di Richiesta del PM alla Sorveglianza non è valido");
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
        <font class="campo">Richieste del PM alla Sorveglianza</font>
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

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="GrigliaRichiesteDelPMallaSORV">
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
        <%--a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadInsRichiestaSORVRevocaLA')">Richiesta Revoca di Liberazione Anticipata</a--%>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadElencoTitoliRichiestaSORVRevocaLA')">Richiesta Revoca di Liberazione Anticipata</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <%--a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadInsRichiestaSORVUnificaMS')">Richiesta Unificazione di Misure Sicurezza</a--%>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadElencoTitoliRichiestaSORVUnificaMS')">Richiesta Unificazione di Misure Sicurezza</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <%-- a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadInsRichiestaSORVRevocaMA')">Richiesta Revoca Misure Alternative a Detenzione</a --%>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadElencoTitoliRichiestaSORVRevocaMA')">Richiesta Revoca Misure Alternative a Detenzione</a>
      </td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadInsRichiestaSORVAltro')">Altre Richieste</a>
      </td>
    </tr>
    

    <%
    //========================================================================
    //              Stampa e Invio Richieste, Visualizzazione
    //========================================================================
    %>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td colspan="3" class="Titolonocap">Richieste alla Sorveglianza</td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadEmettiRichiestaDelPMallaSORV')">Stampa Richiesta da Inviare</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadVisualizzaRichiesteDelPMallaSORV')">Visualizza Richieste Inviate</a>
      </td>
      <%--
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction">???</a>
      </td>
      --%>
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
            <td class="int" title="Presenza scarico della Decisione">Decisione<br>SORV</td> 
            <td class="int">Azioni</td>
          </tr>
 <%
 	int id_record = 0;
 	String lStatoRich = "";
 	String lSentenza = "";
	Iterator itx = ListaRichiesteSORV.iterator();
 	while ( itx.hasNext()) 
 	{
 		RichiestePmInCumuloModel lRichiestaSORV = (RichiestePmInCumuloModel)itx.next();
 		
 		// stato
 		lStatoRich = "";
 		if(lRichiestaSORV.getRicIdRichiesteInviateCum() != null )
	 		lStatoRich = "Inviata";
 		else
 			lStatoRich = "Da Inviare";
 		
 		// Titolo
 		lSentenza = "";
 		if(lRichiestaSORV.getAnnoSentenza()!=null && lRichiestaSORV.getNumeroSentenza()!=null)
 		{
 			lSentenza = lRichiestaSORV.getAnnoSentenza()+"/"+lRichiestaSORV.getNumeroSentenza();
 			
 			if( !("null").equals(lRichiestaSORV.getAltri()) && 
 				!"".equals(lRichiestaSORV.getAltri()) &&
 				!"0".equals(lRichiestaSORV.getAltri()) )
 			{
 				lSentenza += " + "+lRichiestaSORV.getAltri();
 			}
 		}
 %>         
 	<tr>
 	  <td class="c" nowrap ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRichiestaSORV.getDataEmissione(), "dd-MM-yyyy"), " - ") %></td>	
 	  <td class="c"><%=StringUtils.toStringJSP(lRichiestaSORV.getDescrTipoAnnotazione(), "") %></td>
      <td class="c"><%=lStatoRich%></td>
	  <td class="c" style="text-align:left">&nbsp;Sentenza N. <%=StringUtils.toStringJSP(lSentenza)%></td>
      <td class="c"> 
<%	if(lRichiestaSORV.getDecisioneGeSorvCum()!=null && lRichiestaSORV.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum()!=null)
  	{ %>
  		<font style="color:green"><img src="/images/V.gif"></font>
<%	}
	else	
	{	%>
		&nbsp;
<%	} %>	  		    
      </td>
      
	  <!-- 				azioni				 -->
      <td class="c" style="text-align:center"> &nbsp;
          <a href="javascript:eseguiAzione('Dettaglio', <%=lRichiestaSORV.getIdRichiestePmInCumulo()%>, <%=lRichiestaSORV.getCodTipoAnnotazione()%>) ">
        <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Richiesta" border="0"></a>
              <!--a href="javascript:eseguiAzione('Stampa','')">
                <img src="/images/print.gif" width="12" height="12" alt="Dettaglio Stampa" border="0">
              </a-->
 <% 
      if(IstruttoriaCumulo.getFlagStato().equals("A") 
      	 &&	lStatoRich.compareTo("Da Inviare")==0 )   // NON deve essere stata già INVIATA
      { %>
          <a href="javascript:eseguiAzione('Modifica', <%=lRichiestaSORV.getIdRichiestePmInCumulo()%>, <%=lRichiestaSORV.getCodTipoAnnotazione()%>)">
        <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
     	  <a href="javascript:eseguiAzione('Cancella', <%=lRichiestaSORV.getIdRichiestePmInCumulo()%>, <%=lRichiestaSORV.getCodTipoAnnotazione()%>)">
        <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
   <% } %>
      </td>
    </tr>
<%	}  // Chiude Ciclo while sulle Richieste %>

    </table>
  </td>
 </tr>
</table>

  </td>
 </tr>    
</table>

</form>    
</body>
</html>