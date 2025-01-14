<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>


<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>


<%@ page import="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="ListaRichiesteInviate"     	scope="request" class="java.util.Vector"/>

<!-- 				ListaRichiesteDelPM					 -->
<%
//==============================================================================
//  Form con la liste delle richieste del PM già Emesse/Inviate al G.E. 
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
      document.GrigliaRichInv.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.GrigliaRichInv.submit();
    }
    
    //==========================================================================
    // Ritorna alla Lista dei fascicoli coinvolti
    //==========================================================================
    function tornaIndietro(action)
    {
      document.GrigliaRichInv.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.GrigliaRichInv.submit();
    }
    
  	//===============================================================================
 	//  Richiama l'azione di Dettaglio / Modifica / Cancellazione
 	//===============================================================================
  	function eseguiAzione(aTipoAzione, aIdRich)
  	{
    	if (aTipoAzione=='Dettaglio')
    	{
      		lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaDelPMEmessa";
      		document.GrigliaRichInv.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTA_INVIATA_CUM %>.value = aIdRich;
      		document.GrigliaRichInv.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichInv.submit();
    	}
    	else if (aTipoAzione=='Cancella')
    	{
	   		lAzione = "siap.siep.modulocumulo.action.ActEmettiRichiesteDelPM";
	   		document.GrigliaRichInv.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTA_INVIATA_CUM %>.value = aIdRich;
	   		document.GrigliaRichInv.modalita.value = "C";
	   		document.GrigliaRichInv.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
	   		retValue = confirm(" Confermi la Cancellazione ? ");
			if (retValue)
			{
				document.GrigliaRichInv.submit();
			}	
 		}
    	else if (aTipoAzione=='Stampa')
    	{
	   		//lAzione = "siap.siep.modulocumulo.action.ActStampaRichiestaDelPM";
	   		lAzione = "siap.siep.modulocumulo.action.ActLoadDocBlobRichiestaDelPMCumulo";
	   		document.GrigliaRichInv.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTA_INVIATA_CUM %>.value = aIdRich;
	   		document.GrigliaRichInv.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
	   		document.GrigliaRichInv.submit();
 		}
    }
  	
  	function eseguiAzioneRigaDett(aIdRichinCum, aTipoRich)
  	{
  		if(aTipoRich == '021')
   		{	
     		lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaBenefici";
   		}
  		else if(aTipoRich == '002' || aTipoRich == '003')
  		{	
   			lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGEBenefici";
  		}
  		else if(aTipoRich == '004' || aTipoRich == '013' || aTipoRich == '017')
  		{
  			lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaPenaPrincCum";
  		}
  		else if(aTipoRich == '023')
   		{	
     		lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaSSCum";
   		}
  		else if(aTipoRich == '024' || aTipoRich == '025')
		{	
  			lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGESostPenaAcc";
		}
  		else if(aTipoRich == '026' || aTipoRich == '027' || aTipoRich == '028')
		{	
  			lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaPenaAcc";
		}
  		else if(aTipoRich == '029' || aTipoRich == '030')
		{	
  			lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGEApplicaPenaAccCum";
		}
  		else if(aTipoRich=='014')
   		{	
        	lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaGEAltro";
    	}
  		
   		document.GrigliaRichInv.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>.value = aIdRichinCum;
   		document.GrigliaRichInv.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
   		document.GrigliaRichInv.submit();
   	}
    
    //==========================================================================
    // Visualizza, nasconde il record con il dettaglio dell'istruttoria
    //==========================================================================
    function visualizzaRecord(idRecord,idImg)
    {
      var riga = document.getElementById(idRecord);  
      var immagine = document.getElementById(idImg);

      if (riga.style.display =="none" )
      {
        riga.style.display = "block";
        immagine.src = "/images/collapse.gif";
      }
      else 
      {
        riga.style.display = "none";
        immagine.src = "/images/expand.gif";
      }
    }
    
    
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Richieste del PM Inviate al G.E.&nbsp;</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Lista dei fascicoli coinvolti -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>

<% // INCLUDE DEL DETTAGLIO FASCICOLO%>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>

<% // INCLUDE DEL DETTAGLIO DEL TITOLO%>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="GrigliaRichInv">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTA_INVIATA_CUM %>" value="">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="">
  <input type="hidden" name="modalita" value="">
  
  <table cellpadding="2" cellspacing="2" width="95%" align="center" style="border:0;">
    <%
    //========================================================================
    //                            
    //========================================================================
    %>
    <tr>
      <td colspan="100%" class="Titolonocap">Richieste Inviate</td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    
    <%
    //========================================================================
    //         LISTA DELLE RICHIESTE GIA' EMESSE / INVIATE 
    //========================================================================
    %>
<tr>
<td colspan="100%" align="center">
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <td class="int">&nbsp;</td>
      <td class="int">Data Emissione</td>
      <td class="int">Data Trasmissione</td>
      <td class="int">Ufficio Destinatario</td>
      <td class="int">Azioni</td>
    </tr>
<%
	int id_record = 0;
	Vector<RichiestePmInCumuloModel> VecRicGE = null;
	Iterator itx = ListaRichiesteInviate.iterator();
	while ( itx.hasNext()) 
	{
		id_record = id_record +1;
		RichiesteInviateCumModel lRichinviate = (RichiesteInviateCumModel)itx.next();
		
		VecRicGE = null;
		if(lRichinviate.getListaRichiestePMinCumulo()!=null && lRichinviate.getListaRichiestePMinCumulo().size() > 0)
		{
			VecRicGE = new Vector<RichiestePmInCumuloModel>(lRichinviate.getListaRichiestePMinCumulo());
		}
%>  
  <tr>
    <td class="c">
<%--       			<a href="javascript:visualizzaNote('rec_<%=id_record%>')" title="Note "> --%>
      <a href="javascript:visualizzaRecord('rec_<%=id_record%>','img_<%=id_record%>')"> 
        <img src="/images/expand.gif" width="16" height="16" alt="Dettaglio Richieste" border="0" id="img_<%=id_record%>" >
      </a>
    </td>
    <td class="c" nowrap ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRichinviate.getDataEmissione(), "dd-MM-yyyy"), " - ") %></td>
    <td class="c" nowrap ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRichinviate.getDataTrasmissione(), "dd-MM-yyyy"), " - ") %></td>
    <td class="c" nowrap>&nbsp;<%=StringUtils.toStringJSP(lRichinviate.getDescrUfficioDest(),"")%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(lRichinviate.getDescrLuogoDest(),"") %></td>
    <td class="c" style="text-align:left"> &nbsp;
      <a href="javascript:eseguiAzione('Dettaglio', <%=lRichinviate.getIdRichiesteInviateCum()%>)">
        <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Richiesta" border="0"></a>
<%	if( lRichinviate.getFlagDocValidato() != null && lRichinviate.getFlagDocValidato().compareTo("S") == 0 )
	{ %>  
		<!-- 			Icona di Stampa del Blob della Richiesta Validata 			 -->  
      <a href="javascript:eseguiAzione('Stampa', <%=lRichinviate.getIdRichiesteInviateCum()%>)">
        <img src="/images/print.gif" width="12" height="12" alt="Visualizza Blob di Stampa" border="0"></a>
<%	} 
	else
	{	%> 
		<a href="javascript:eseguiAzione('Cancella', <%=lRichinviate.getIdRichiesteInviateCum()%>)">
        	<img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
<%	} %>	     
    </td>
  </tr>
  
  <!-- singole richieste Raggruppate -->
  	<tr style="display:none" id="rec_<%=id_record%>">
      <td colspan="100%" align="center">
      
      		<!-- 	table -->
	        <table cellspacing="2" cellpadding="2" align="center" width="95%">
	          <tr>
	            <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
	            <!--  td class="int">Data Richiesta</td -->
	            <td class="int">Tipo Richiesta</td>
	            <td class="int">D.P.R.</td>
	            <td class="int" title="Titolo per il quale è stata effettuata la richiesta">Titoli</td>
	            <td class="int">Anticipazione</td> 
	            <td class="int">Azioni</td>
	          </tr>
<%
		 	//int id_record = 0;
		 	String lAnticipazione="";
		 	String lSentenza = "";
		 	String lTipo = "";
			if( VecRicGE!=null && VecRicGE.size()>0 )  {
				Iterator itGE = VecRicGE.iterator();
			 	while ( itGE.hasNext()) 
			 	{
			 		RichiestePmInCumuloModel lRichiestaGE = (RichiestePmInCumuloModel)itGE.next();
			 		
			 		// Anticipazione
			 		if("A".equals(lRichiestaGE.getFlagAppProvvisoria()) )
			 			lAnticipazione="Si";
			 		else if("R".equals(lRichiestaGE.getFlagAppProvvisoria()) )
			 			lAnticipazione="No";
			 		else
			 			lAnticipazione="-";
			 		
			 		// Titolo
			 		lSentenza = "";
			 		if(lRichiestaGE.getAnnoSentenza()!=null && lRichiestaGE.getNumeroSentenza()!=null)
			 		{
			 			lSentenza = lRichiestaGE.getAnnoSentenza()+"/"+lRichiestaGE.getNumeroSentenza();
			 			
			 			if( !("null").equals(lRichiestaGE.getAltri()) && 
			 				!"".equals(lRichiestaGE.getAltri()) &&
			 				!"0".equals(lRichiestaGE.getAltri()) )
			 			{
			 				lSentenza += " + "+lRichiestaGE.getAltri();
			 			}
			 		}
			 		
			 		// Descrizione Richiesta
			  		lTipo = "";
/*			  		if( !"021".equals(lRichiestaGE.getCodTipoAnnotazione()) &&
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
						lTipo +="Revoca Sentenza abolizione del Reato: ";
					}
			 %>         
				 	<tr>
				 	  <%-- td class="c" nowrap ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRichiestaGE.getDataEmissione(), "dd-MM-yyyy"), " - ") %></td --%>	
				 	  <td class="c"><%=lTipo%>&nbsp;<%=StringUtils.toStringJSP(lRichiestaGE.getDescrTipoAnnotazione(), "") %></td>
				 	  <td class="c"><%=StringUtils.toStringJSP(lRichiestaGE.getDescrDpr(), "") %></td>
					  <td class="c" style="text-align:left">&nbsp;Sentenza N. <%=StringUtils.toStringJSP(lSentenza)%></td>
				      <td class="c">&nbsp;<%=StringUtils.toStringJSP(lAnticipazione) %></td>
				
				      <td class="c" style="text-align:center"> &nbsp;
				          <a href="javascript:eseguiAzioneRigaDett(<%=lRichiestaGE.getIdRichiestePmInCumulo()%>, '<%=lRichiestaGE.getCodTipoAnnotazione()%>')">
				        <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Richiesta" border="0"></a>
				      </td>
				    </tr>
				    
		<%		} 	// Chiude ciclo while sulle singole lRichiestaGE	%>
				    
		<%	} 	// Chiude if (itGE!=null ...	%>
	        </table>
	        <!--  chiuso table	 -->
	        
	  </td>
	</tr>	<%//  Chiude la/le linea/e aperte cliccando sul +  %>
<%
	}	// Chiude ciclo while sulle RichiestaInviate
	%>
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