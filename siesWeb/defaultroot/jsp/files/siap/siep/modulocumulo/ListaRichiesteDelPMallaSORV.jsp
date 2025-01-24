<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="ListaRichiesteInviate"     	scope="request" class="java.util.Vector"/>

<!-- 				ListaRichiesteDelPMallaSORV					 -->
<%
//==============================================================================
//  Form con la liste delle richieste del PM già inviate/Emesse 
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
      		lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaDelPMallaSORVEmessa";
      		document.GrigliaRichInv.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTA_INVIATA_CUM %>.value = aIdRich;
      		document.GrigliaRichInv.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichInv.submit();
    	}
    	else if (aTipoAzione=='Cancella')
    	{
	   		lAzione = "siap.siep.modulocumulo.action.ActEmettiRichiesteDelPMallaSORV";
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
  		if(aTipoRich == '014')
		{
   			lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVAltro";
		}
  		else if(aTipoRich == '020')
		{
			lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVRevocaLA";
		}
  		else if(aTipoRich == '022')
		{
			lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVUnificaMS";
		}
  		else if(aTipoRich == '031')
		{
			lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVRevocaMA";
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
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Richieste del PM Inviate alla Sorveglianza</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Lista dei fascicoli coinvolti -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV')">
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
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
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
  
  <table cellpadding="2" cellspacing="2" width="95%" align="center" style="border:0;"
         onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" 
         onMousedown="over_effect(event,'inset')"  onMouseup="over_effect(event,'outset')">
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
	Iterator itx = ListaRichiesteInviate.iterator();
	while ( itx.hasNext()) 
	{
		id_record = id_record +1;
		RichiesteInviateCumModel lRichinviate = (RichiesteInviateCumModel)itx.next();
		
		Vector<RichiestePmInCumuloModel> VecRicSORV = null;
		if(lRichinviate.getListaRichiestePMinCumulo()!=null && lRichinviate.getListaRichiestePMinCumulo().size() > 0)
		{
			VecRicSORV = new Vector<RichiestePmInCumuloModel>(lRichinviate.getListaRichiestePMinCumulo());
		}
%>  
  <tr>
    <td class="c">
      			<a href="javascript:visualizzaNote('rec_<%=id_record%>')" title="Note ">
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
	            <td class="int" title="Titolo per il quale è stata effettuata la richiesta">Titoli</td>
	            <td class="int">Anticipazione</td> 
	            <td class="int">Azioni</td>
	          </tr>
<%
		 	//int id_record = 0;
		 	String lAnticipazione="";
		 	String lSentenza = "";
			Iterator itSORV = VecRicSORV.iterator();
		 	while ( itSORV.hasNext()) 
		 	{
		 		RichiestePmInCumuloModel lRichiestaPM = (RichiestePmInCumuloModel)itSORV.next();
		 		
		 		// Anticipazione
		 		if("A".equals(lRichiestaPM.getFlagAppProvvisoria()) )
		 			lAnticipazione="Si";
		 		else if("R".equals(lRichiestaPM.getFlagAppProvvisoria()) )
		 			lAnticipazione="No";
		 		else
		 			lAnticipazione="-";
		 		
		 		// Titolo
		 		lSentenza = "";
        if (lRichiestaPM.getListaTitoli()!=null && lRichiestaPM.getListaTitoli().size()>0)
        {
Vector <TitoloCumulatoModel> lListaTitoli = lRichiestaPM.getListaTitoli();
for (int i=0; i<  lListaTitoli.size(); i++) 
{
           //TitoloCumulatoModel lTitoloRich = lRichiestaPM.getListaTitoli().elementAt(0);
TitoloCumulatoModel lTitoloRich = lRichiestaPM.getListaTitoli().elementAt(i);
if (i>0) lSentenza += "<br>";
           lSentenza += " "+lTitoloRich.getDescrTipoProvvedimento()+" N. "+lTitoloRich.getAnnoSentenza()+"/"+lTitoloRich.getNumeroSentenza();
           lSentenza += " del "+DateUtils.getDateToString(lTitoloRich.getDataProvvedimento(), "dd-MM-yyyy");
           lSentenza += " emessa da "+lTitoloRich.getDescrTipoAutoritaEmittente()+" di "+lTitoloRich.getDescrLuogoEmittente();

//           if (lRichiestaPM.getListaTitoli().size()>1)
//            lSentenza += " + "+(lRichiestaPM.getListaTitoli().size()-1);

        }
}

/*
		 		if(lRichiestaPM.getAnnoSentenza()!=null && lRichiestaPM.getNumeroSentenza()!=null)
		 		{
		 			lSentenza = lRichiestaPM.getAnnoSentenza()+"/"+lRichiestaPM.getNumeroSentenza();
		 			
		 			if( !("null").equals(lRichiestaPM.getAltri()) && 
		 				!"".equals(lRichiestaPM.getAltri()) &&
		 				!"0".equals(lRichiestaPM.getAltri()) )
		 			{
		 				lSentenza += " + "+lRichiestaPM.getAltri();
		 			}
		 		}
*/		 		
		 %>         
			 	<tr>
			 	  <!--  td class="c" nowrap >< %=StringUtils.toStringJSP(DateUtils.getDateToString(lRichiestaPM.getDataEmissione(), "dd-MM-yyyy"), " - ") %></td -->	
			 	  <td class="c"><%=StringUtils.toStringJSP(lRichiestaPM.getDescrTipoAnnotazione(), "") %></td>
				  <td class="c" style="text-align:left"><%=StringUtils.toStringJSP(lSentenza)%></td>
			      <td class="c">&nbsp;<%=StringUtils.toStringJSP(lAnticipazione) %></td>
			
			      <td class="c" style="text-align:center"> &nbsp;
			          <a href="javascript:eseguiAzioneRigaDett(<%=lRichiestaPM.getIdRichiestePmInCumulo()%>, '<%=lRichiestaPM.getCodTipoAnnotazione()%>')">
			        <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Richiesta" border="0"></a>
			      </td>
			    </tr>
			    
	<%		} 	// Chiude ciclo while sulle singole lRichiestaPM	%>
				    
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