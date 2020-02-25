<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="fascicolo" 		scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="ListaRichieste" 	scope="request" class="java.util.Vector"/>

<%
//==============================================================================
//               Griglia delle richieste Istruttorie Cumulo
//==============================================================================

  UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
  UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
%>

<!-- 				GrigliaRichiesteIstruttorieCumulo				 -->
<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="/html/gestisciUploadStampa2.js"></script>
  <script language="JavaScript">

  function eseguiFunzioneDettaglio(CodMot,IdEve )
  {
    	document.GrigliaRichiesteIstruttorie.<%=ICostantiEvento.CAMPO_ID_EVENTO %>.value = IdEve;
    	document.GrigliaRichiesteIstruttorie.modifica.value="R";
    	
    	if(CodMot == '0049')
    	{	
    		lAzione = "siap.siep.istruttoria.action.ActDettaglioEstrattoSentenza";
      		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichiesteIstruttorie.submit();
    	}
    	else if(CodMot == '1050')
    	{	
    		lAzione = "siap.siep.istruttoria.action.ActDettaglioIstruttoriaRichiestaPagamentoPP";
      		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichiesteIstruttorie.submit();
    	}
    	else if(CodMot == '0051')
    	{	
    		lAzione = "siap.siep.istruttoria.action.ActDettaglioPosizioneGiuridica";
      		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichiesteIstruttorie.submit();
    	}
    	else if(CodMot == '0053')
    	{	
    		lAzione = "siap.siep.istruttoria.action.ActDettaglioInfoArrestoDenuncia";
      		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichiesteIstruttorie.submit();
    	}
    	else if(CodMot == '0045')
    	{	
    		lAzione = "siap.siep.istruttoria.action.ActDettaglioArrestiDomiciliariPrecedenti";
      		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichiesteIstruttorie.submit();
    	}
    	else if(CodMot == '0052')
    	{	
    		lAzione = "siap.siep.istruttoria.action.ActDettaglioArrestiDomiciliariAttuali";
      		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichiesteIstruttorie.submit();
    	}
    	else if(CodMot == '0054' || CodMot == '0579' || CodMot == '0580' )
    	{	
    		lAzione = "siap.siep.istruttoria.action.ActDettaglioAnagraficaCittadini";
      		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichiesteIstruttorie.submit();
    	}
    	else if(CodMot == '0050')
    	{	
    		lAzione = "siap.siep.istruttoria.action.ActDettaglioCertificatoPenale";
      		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichiesteIstruttorie.submit();
    	}
    	else if(CodMot == '0557' || CodMot == '0578' )
    	{	
    		lAzione = "siap.siep.istruttoria.action.ActDettaglioRichiestaCodiceCui";
      		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichiesteIstruttorie.submit();
    	}
    	else if(CodMot == '0044')
    	{	
    		lAzione = "siap.siep.istruttoria.action.ActDettaglioCertificatoDap";
      		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichiesteIstruttorie.submit();
    	}
    	else if(CodMot == '0048')
    	{	
    		lAzione = "siap.siep.istruttoria.action.ActDettaglioCertificatoEsecuzione";
      		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichiesteIstruttorie.submit();
    	}
    	else if(CodMot == '0565' || CodMot == '0566' )
    	{	
    		lAzione = "siap.siep.istruttoria.action.ActDettaglioNotEspSanSost";
      		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      		document.GrigliaRichiesteIstruttorie.submit();
    	}
    	else 
    	{
    		alert('Cod Motivo NON riconosciuto = '+CodMot+' - Id Evento = '+IdEve);
    	}	
    }
    
    function eseguiFunzioneCancella(IdEve, lDocReg, lIdIstru )
    {
    	//alert("IdEve = "+IdEve+" - lDocReg = "+lDocReg+" - lIdIstru = "+lIdIstru);
    	document.GrigliaRichiesteIstruttorie.<%=ICostantiEvento.CAMPO_ID_EVENTO %>.value = IdEve;
    	var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
        if (window.confirm(msgConfirm))
        {
        	if(lDocReg == "S")	// Record Validato: Cancellazione logica 
        	{		
     	       var  desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActLoadCancellaRichiesteIstruttorie&IdEvento="+IdEve+"&IdIstruttoriaCumulo="+lIdIstru, "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
    	       window.parent.close();

        	}	
        	else
        	{					// Record NON Validato: Cancellazione Fisica di Evento
          		lAzione = "siap.siep.modulocumulo.action.ActCancellaRichiesteIstruttorie";
          		document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
    	  		document.GrigliaRichiesteIstruttorie.submit();
        	}
        }  
    }
    
    function stampaSiep(lAzione)
    {
    	//alert ('stampaSiep - lAzione = '+lAzione);
       var  hrefStampa = lAzione;
       var lIndice = hrefStampa.indexOf("?");

       var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
       stampa2("/jsp/files/Stampa.jsp",  parametri);
    }
    
    //==========================================================================
    // Ritorna alla Lista Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.GrigliaRichiesteIstruttorie.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.GrigliaRichiesteIstruttorie.submit();
    }
    
    
    function chiama(idEvento)
	{
		window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActLoadCancellaProvvedimento&IdEvento="+idEvento,"Cancella_provvedimento", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
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
        <font class="campo">Richieste/Istruttorie</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Lista Gestione Cumulo -->
        <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo')">
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

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="GrigliaRichiesteIstruttorie">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="">
  <input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="">
  <input type="hidden" name="modifica" value="R" >

  
  <table cellpadding="2" cellspacing="2" width="95%" align="center"
         onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" 
         onMousedown="over_effect(event,'inset')"  onMouseup="over_effect(event,'outset')">
    <%
    //========================================================================
    //                           
    //========================================================================
    %>
    <tr>
      <td colspan="3" class="Titolonocap">Richieste Istruttorie</td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciEstrattoSentenze&IdIstruttoriaCumulo=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">Richiesta Sentenza Integrale</a>
      </td>
     <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciIstruttoriaRichiestaPagamentoPP&IdIstruttoriaCumulo=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">Notizie Pagamento Pena Pecuniaria</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciPosizioneGiuridica&IdIstruttoriaCumulo=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">Richiesta Posizione Giuridica da Istituto Penitenziario</a>
      </td>
    </tr>
    <%
    //========================================================================
    //                            
    //========================================================================
    %>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciInfoArrestoDenuncia&IdIstruttoriaCumulo=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">Informazioni Arresto e/o Denuncia</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciArrestiDomiciliariPrecedenti&IdIstruttoriaCumulo=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">Arresti Domiciliari Precedenti</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciArrestiDomiciliariAttuali&IdIstruttoriaCumulo=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">Arresti Domiciliari Attuali</a>
      </td>
    </tr>
    <%
    //========================================================================
    //                            
    //========================================================================
    %>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciAnagraficaCittadini&IdIstruttoriaCumulo=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">Accertamento Anagrafica</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciCertificatoPenale&IdIstruttoriaCumulo=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">Richiesta Certificato Penale</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciRichiestaCodiceCui&IdIstruttoriaCumulo=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">Richiesta Codice CUI e Cartellino Dattiloscopico</a>
      </td>
    </tr>
    <%
    //========================================================================
    //                            
    //========================================================================
    %>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciCertificatoDap&IdIstruttoriaCumulo=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">Richiesta Certificato DAP</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciCertificatoEsecuzione&IdIstruttoriaCumulo=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">Certificato Stato Esecuzione</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciNotEspSanSost&IdIstruttoriaCumulo=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">Notizie Espulsione Sanzione Sostitutiva</a>
      </td>
    </tr>
</table>

    <%
    //==========================================================================
    //                    Lista delle richieste effettuate
    //==========================================================================
    %>
    <br>
    <table cellpadding="2" cellspacing="2" width="75%" align="center">
      <tr>
        <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
        <td class="int">Data Richiesta</td>
        <td class="int">Tipo Richiesta</td>
        <td class="int">Documento<br>Validato</td>
        <td class="int">Azioni</td>
      </tr>
          
    <%	
	if (ListaRichieste.size()==0)
   	{%>
	      <tr>
	        <td class="c" colspan="4">Nessuna richiesta presente in istruttoria</td>
	      </tr>
  <%}
    else
    {
		int id_record = 0;
	    Iterator itx = ListaRichieste.iterator();
    	while ( itx.hasNext())
    	{
        	id_record = id_record +1;
        	EventoModel lEveMod = (EventoModel)itx.next();
 %>
      		<tr>
        <%// Inserire qui le get dei campi da visualizzare %>
        		<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEveMod.getDataEmissione(),"dd-MM-yyyy"),"&nbsp;")%></td>
        		<td class="c"><%=StringUtils.toStringJSP(lEveMod.getDescrMotivo(),"&nbsp;")%></td>
	      		
	      		<td class="c">&nbsp;
<%	      		if (lEveMod.getFlagDocumentoRegistrato()!=null)
		      	{
		        	if (lEveMod.getFlagDocumentoRegistrato().compareTo("S")==0)
		        	{ %>
		          		<img src="/images/TickRed.gif">
<%  				}
        			else if(lEveMod.getFlagDocumentoRegistrato().compareTo("A")==0)
        			{   %>
	             		<a class="cliccabile" href="javascript:chiama('<%=lEveMod.getIdEvento()%>');" title="ANNULLAMENTO">
	             		<font class="cRosso">ANNULLATO</font>
	             		</a>
<%					}
		      	}	%>	             		
				</td>
				
				<td class=c>
				<!-- 	Azione di Dettaglio	 -->
				<a href="javascript:eseguiFunzioneDettaglio('<%=lEveMod.getCodMotivo()%>','<%=lEveMod.getIdEvento()%>')">
           			<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>	
<%        
         	String lDocReg = "N";
        	if(  lEveMod.getFlagDocumentoRegistrato() != null
          	  && lEveMod.getFlagDocumentoRegistrato().equals("S"))
        	{
          		lDocReg = "S";
        	}
      	
        	if(  lEveMod.getFlagDocumentoRegistrato() != null
          	  && lEveMod.getFlagDocumentoRegistrato().equals("A"))
        	{
          		lDocReg = "A";
        	}
      	

 			if (lUfficioUtenteConnesso.getCodUfficio().equals(fascicolo.getChiaveUfficio()))	// Modificabile
      		{	
   				if(lDocReg.compareTo("A") != 0 )
   				{ %>
   					<!-- 	Azione di Cancellazione	 -->
   					<a href="javascript:eseguiFunzioneCancella('<%=lEveMod.getIdEvento()%>','<%=lDocReg%>','<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>')" >
            			<img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
<%				}    				
			} 
			
 			if(lEveMod.getFlagDocumentoRegistrato()!= null)
 			{	
 				if(lDocReg.compareTo("N")==0 || lDocReg.compareTo("S")==0 )
 				{
 					String lActStampa ="siap.sico.evento.action.ActLoadDocumento"; %>
					<!-- 	Azione di Stampa	 -->
      				<a href="Javascript:stampaSiep('<%="/jsp/Main.jsp?Action="+lActStampa+"&IdEvento="+lEveMod.getIdEvento()%>')">
        				<img src="/images/print.gif" alt="Visualizza Stampa" width="12" height="12" border="0"></a>
 				
<%  			}
 			}	%>
			</td>
			</tr>
<% 		}
   	}  %>
    </table>
    
</form>    
</body>
</html>