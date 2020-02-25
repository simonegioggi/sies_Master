<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.List" %>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>

<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
 
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>

<%@page import="org.apache.log4j.Logger"%>
<%@page import="f3b.log.LogF3B"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="eventonotifica"      		scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizione"					scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="MisuraModel"				scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="tornadavalida"				scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"					scope="request" class="java.util.Vector"/>
<jsp:useBean id="istitutodetenzione"		scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<%-- MEV_39: aggiunto e gestito useBean su Istituto Detenzione --%>
<jsp:useBean id="strutturaDesignataModel"   scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<%

FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();

MagistratoModel lMagistrato = eventonotifica.getMagistrato();
if( lMagistrato == null)
  lMagistrato = new MagistratoModel();

// Gestione Autorità Esterne sulle Notifiche - ( al MAX 2 Notifiche)
String lCodTipoAutorita1 = "-";
NotificaModel lPrimaNotifica = new NotificaModel();
AutoritaEsternaModel lPrimaAutoritaEsterna = new AutoritaEsternaModel();

if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
{
	    if (eventonotifica.getNotifiche()[0].getAutoritaEsterna() != null && 
	    	!eventonotifica.getNotifiche()[0].getCodTipoNotifica().equals("ND")	)
	    {
	      	lCodTipoAutorita1 = eventonotifica.getNotifiche()[0].getAutoritaEsterna().getCodTipoAutorita();
	    }
	
	    lPrimaNotifica = eventonotifica.getNotifiche()[0];
      	lPrimaAutoritaEsterna = lPrimaNotifica.getAutoritaEsterna();
}

String 	lCodTipoAutorita2 = "-";
NotificaModel lSecondaNotifica = new NotificaModel();
AutoritaEsternaModel lSecondaAutoritaEsterna = new AutoritaEsternaModel();

if(eventonotifica.getNotifiche().length > 1)
{
	if (eventonotifica.getNotifiche()[1].getAutoritaEsterna() != null && 
		!eventonotifica.getNotifiche()[1].getCodTipoNotifica().equals("ND")	)
    {
      	lCodTipoAutorita2 = eventonotifica.getNotifiche()[1].getAutoritaEsterna().getCodTipoAutorita();
    }

	lSecondaNotifica = eventonotifica.getNotifiche()[1];
	lSecondaAutoritaEsterna = lSecondaNotifica.getAutoritaEsterna();	
}

// Misura Sicurezza
String CodMisura="";
String MisidMisura="";
if(MisuraModel != null && MisuraModel.getCodTipo() != null && MisuraModel.getIdMisuraSicurezza()!=null)
{	
	CodMisura = MisuraModel.getCodTipo();
	// id_Misura della misura Corrente = Mis_id_Misura della eventuale Misura precedente  
	MisidMisura= MisuraModel.getIdMisuraSicurezza().toString();
}	

// Posizione Giuridica
String CodPos = "";
if(posizione != null && posizione.getCodPosizioneGiuridica() != null)
	CodPos = posizione.getCodPosizioneGiuridica();

%>
<!--  LoadDettaglioOrdinediConsegna -->
<html>
  <head>
    <title>[S.I.E.S.] -Gestione Misure sicurezza- Ordine di Consegna </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
	<script language="JavaScript">
	
	   function stampaSiep(lAzione)
	   {
	      var  hrefStampa = lAzione;
	      var lIndice = hrefStampa.indexOf("?");
	
	      var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
	
	      stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
	   }	   
	</script>
</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      	<font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Ordine di Consegna per Esecuzione Misure Sicurezza</font>
      </td>
<%
  	if((eventonotifica.getEvento().getFlagDocumentoRegistrato()==null) ||
     (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) )
    {%>
    		<!-- BOTTONE DI STAMPA -->
<%-- 			 <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>"> --%>
<%-- 				<jsp:param name="ActionLink" value="<%= "/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaOrdinediConsegnaEsecMS&MisIdMisuraSicurezza="+MisidMisura+"&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&CodMotivo="+eventonotifica.getEvento().getCodMotivo()+"&CodPosizioneGiuridica="+CodPos+"&CodTipo="+CodMisura%>"/> --%>
<%-- 			</jsp:include>    --%>
			<%-- MEV_39: aggiunti pulsanti di validazione e cancellazione --%>
			<!-- BOTTONE DI STAMPA -->
			<td class="LBG">
       			<a  href="Javascript:stampaSiep('/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaOrdinediConsegnaEsecMS&MisIdMisuraSicurezza=<%=MisidMisura%>&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&CodMotivo=<%=eventonotifica.getEvento().getCodMotivo()%>&CodPosizioneGiuridica=<%=CodPos%>&CodTipo=<%=CodMisura%>')" onclick="javascript:lookUpload();">
        			 <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
       			</a>
     		</td>
     		
			<!-- BOTTONE DI VALIDAZIONE -->
     		<td class="LBG">
       			<a href="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActUploadEsecuzioneMS&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misurasicurezza.action.ActLoadDettaglioOrdinediConsegna&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
        			<img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
       			</a>
     		</td>
			<!-- ICONA DI MODIFICA -->
			<td class="LBG">
				<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActLoadModificaComunicazioneOrdineConsegnaMS&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&TornaQui=10">
         		<img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        		</a>
        	</td>
        	<!-- ICONA DI CANCELLAZIONE -->
			<td class="LBG">
	  			<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActModificaComunicazioneOrdineConsegnaMS&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&modalita=D&TornaQui=10">
      				<img align="middle" src="/images/delete24.gif" alt="Elimina" width="24" height="24" border="0">
      			</a>
			</td>
			<%-- 20190918 [SG]: collaudo 11.3 elimino torna indietro --%>
			<!-- ICONA DI TORNA INDIETRO -->
<!-- 			<td class="LBG"> -->
<!-- 				  <a href="javascript:history.go(-1);"> -->
<%-- 				   <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"> --%>
<!-- 				  </a> -->
<!-- 			</td> -->
<%   }%>

    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
	<table>
    	<tr>
      		<td class="l" width="20%">Posizione Giuridica </td>
      		<td class="L" colspan=5 width="30%">
        		<font class="campo">
         		<%=posizione.getDescrPosizioneGiuridica()%>
  	    		</font>
      		</td>
        	<input type="HIDDEN" title="Codice Posizione" value="<%=CodPos%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" >
        	<input type="HIDDEN" title="Codice Misura" value="<%=CodMisura%>" type="text" name="<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>" >
			<input type="HIDDEN" title="Id Misura" value="<%=MisidMisura%>" type="text" name="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" >
			<input type="HIDDEN" title="Mis Id Misura" value="<%=MisidMisura%>" type="text" name="<%=ICostantiMisuraSicurezza.CAMPO_MIS_ID_MISURA_SICUREZZA%>" >
    	</tr>
    </table>	

<%  // Eventuali Misura di Sicurezza  %>

	<table>
	<%	List lMisure =(List) request.getAttribute("listaMisure");
		if(lMisure.size() > 0)
		{
	    	Iterator itx = lMisure.iterator();
	    	while ( itx.hasNext())
	    	{
	    		MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
	%>
			<tr>
				<td class=C>Misura di Sicurezza da espiare</td>
			    <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</font></td>
			    <td class=C> Anni</td>
			    <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</font></td>
			    <td class=C> Mesi</td>
			    <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</font></td>
			    <td class=C> Giorni</td>
			    <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</font></td>	
			</tr>
<%			}
		}	%>		  		
    </table>
<!--Richiesta -->  
	<table width="90%">
	  <tr>
        	<td class="l" width="20%">Tipo Provvedimento </td>
        	<td class="L" colspan=1 width="70%">
        		<font class="campo">
        		<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrTipoProvvedimento())%>
        		&nbsp;&nbsp;
        		<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%>&nbsp;
        		</font>
        <%		if(eventonotifica != null && eventonotifica.getEvento() != null &&
        		eventonotifica.getEvento().getFlagPiuMeno() != null &&
        		eventonotifica.getEvento().getFlagPiuMeno().compareTo("D") == 0 )
        		{%>
        			<font color ="red">(Disposta Esecuzione Immediata) </font>	
        <%		} %>				
        		
        	</td>
        </tr>	
	  <tr>
	    <td class="l" width="20%">Data Emissione</td>
	    <td class="L" colspan=1>
	      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
	    </td>
	  </tr>
	  <tr>  
	    <td class="l" width="20%">Data Trasmissione</td>
	    <td class="L" colspan=1>
	      <font class="campo">
	        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPrimaNotifica.getDataInvio(),"dd-MM-yyyy"))%>
	      </font>
	    </td>
     </tr>
  </table>
  <!--Magistrato Firmatario -->   
<table width="80%">  
  <tr>
     <td class="l" width="20%">Magistrato </td>
     <td class="L" colspan="2" width="60%">
          <font class="campo">
            <%=StringUtils.toStringJSP(lMagistrato.getCognome())%> &nbsp;<%=StringUtils.toStringJSP(lMagistrato.getNome())%>
          </font>
      </td>
    </tr>    
         <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(lMagistrato.getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
         
<!--Tipo di Istituto (se presente) 	-->
<%		if(istitutodetenzione != null && istitutodetenzione.getDescrTipoIstituto() != null && 
			!istitutodetenzione.getDescrTipoIstituto().equals(""))
		{ 
				%>
		   <tr>
			    <td class="l" width="20%">Istituto di Detenzione</td>
			    <td class="L" colspan=2 width="60%">
			      <font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getDescrTipoIstituto() )%></font>&nbsp;
			      di
			      <font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getDescrizione() )%></font>&nbsp;
			      -&nbsp;
			      <font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getIndirizzo() )%></font>&nbsp;
			      ,&nbsp;
			       <font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getDescrComune() )%></font>&nbsp;
			       (
			       <font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getCodProvincia() )%></font>&nbsp;)
			    </td>
		   </tr>

<%		} %>
    
    	<!--Struttura Designata-->
	<tr>	
		<td class="L">Struttura Designata</td>
	<%
	if (strutturaDesignataModel != null && Utils.isPresent(strutturaDesignataModel.getIdIstitutoDetenzione())) {
	%>
				<td class="l" colspan="3">

              <font class="campo"><%=StringUtils.toStringJSP(strutturaDesignataModel.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(strutturaDesignataModel.getDescrizione())%> - <%=StringUtils.toStringJSP(strutturaDesignataModel.getIndirizzo())%></font>&nbsp;
				</td>
	<%
	} else {
	%>
			<td class="l" colspan="3">
		        <font class="campo">&nbsp;&nbsp;</font>
		      
		    </td>
	<%
		} 
	%>
	</tr>
	     
<!--Autorita ESTERNA o Altra Autorita 	PRIMA-->

<%	if(lPrimaNotifica.getAutoritaEsterna() != null)
	{ %>
		   <tr>
			    <td class="l" width="20%"> Autorita</td>
			    <td class="L" colspan=2 width="60%">
			      <font class="campo"><%=StringUtils.toStringJSP( lPrimaNotifica.getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
			      di
			      <font class="campo"><%=StringUtils.toStringJSP( lPrimaNotifica.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
			    </td>
		   </tr>
<%  
			if(lPrimaNotifica != null &&
			   lPrimaNotifica.getNote()!= null)
			{
%>
		   		<tr>
		    			<td class="l" width="20%">Note</td>
		    			<td class="L" colspan="2" width="60%"><font class="campo"><%=StringUtils.toStringJSP(lPrimaNotifica.getNote())%></font>&nbsp;</td>
		   		</tr>
<%			}
		}
%>
<!--Autorita ESTERNA o Altra Autorita 	SECONDA (se presente) 	-->

<%	if(!lCodTipoAutorita2.equals("-") )
	{ %>
		   <tr>
			    <td class="l" width="20%">Altra Autorita</td>
			    <td class="L" colspan=2 width="60%">
			      <font class="campo"><%=StringUtils.toStringJSP( lSecondaAutoritaEsterna.getDescrTipoAutorita() )%></font>&nbsp;
			      di
			      <font class="campo"><%=StringUtils.toStringJSP( lSecondaAutoritaEsterna.getDescrSede())%></font>&nbsp;
			    </td>
		   </tr>
<%  
			if(lSecondaNotifica != null &&
				lSecondaNotifica.getNote()!= null)
			{
%>
		   		<tr>
		    			<td class="l" width="20%">Note</td>
		    			<td class="L" colspan="2" width="60%"><font class="campo"><%=StringUtils.toStringJSP(lSecondaNotifica.getNote())%></font>&nbsp;</td>
		   		</tr>
<%			}
		}
%>
</table>

<!-- 				Destinatario MDS		 -->
<%	if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
	{ 	
		for (int i = 0; i < eventonotifica.getNotifiche().length; i++)
		{
			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			//siesLogger.debug("  ---- XXXXXX -- DettaglioOEInterna - Not = "+eventonotifica.getNotifiche()[i]);
			if(eventonotifica.getNotifiche()[i] != null 
			   && ("MS").equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica() ) )
			{	%>
				<table width="80%" >
					<tr>
				     	<td class="l" width="20%">Destinatario Sorveglianza </td>
			         	<td class="L" colspan=2 width="60%">
			            	<font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[i].getUfficio().getDescrTipoUfficio()) %>
			            	</font>&nbsp;
							di 
			          		<font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[i].getUfficio().getDescrComune() ) %>
			          		</font>&nbsp;
			            </td>
			        </tr>
			    </table>        		
<%			}
		}
	}	%>	


<!--Autorita ESTERNA per Notifica AVVOCATO Difensore (se presente) 	-->
<%  Iterator lItxAvv = avvocati.iterator(); %>
<%	for (int ind = 0;ind < eventonotifica.getNotifiche().length; ind++ )
	{
			if( eventonotifica.getNotifiche()[ind].getCodTipoNotifica().equals("ND"))
			{	%>
				<table width="80%" >
<%
				      //Iterator lItxAvv = avvocati.iterator();
				      if( lItxAvv.hasNext() )
				      {
				        AvvocatoSiepModel lAvv = (AvvocatoSiepModel)lItxAvv.next();
				%>
				          <tr>
				            <td class="l" width="20%" >Notifica al Difensore</td>
				            <td class="L">
				              <font class="campo">
				                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
				              </font>
				              &nbsp;Foro di&nbsp;
				              <font class="campo">
				                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
				              </font>
				              &nbsp;Difensore di&nbsp;
				              <font class="campo">
				                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
				              </font>
				            </td>
				          </tr>
				 <%
				  		}
%>
  					<tr>
<%  if (StringUtils.toStringJSP( eventonotifica.getNotifiche()[ind].getAutoritaEsterna().getCodTipoAutorita().trim()).compareTo("C0") == 0) 
	{ %>
			    		<td class="l" width="20%"> tramite </td>
			    		<td class="L" colspan=2 width="60%">
			      		<font class="campo"><%=eventonotifica.getNotifiche()[ind].getAutoritaEsterna().getDescrTipoAutorita().trim()%></font>&nbsp;
<% 	} else { %>
			    		<td class="l" width="20%"> presso </td>
			    		<td class="L" colspan=2 width="60%">
			      		<font class="campo"><%=eventonotifica.getNotifiche()[ind].getAutoritaEsterna().getDescrTipoAutorita().trim()%></font>&nbsp;
			      		di
			      		<font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[ind].getAutoritaEsterna().getDescrSede())%></font>&nbsp;
			    		</td>
<% 	}  %>
		   			</tr>
<%					if(eventonotifica.getNotifiche()[ind] != null && eventonotifica.getNotifiche()[ind].getNote() != null)
					{ %>		   			
				   		<tr>
				    		<td class="l" width="20%">Indirizzo</td>
				    		<td class="L" colspan="2" width="60%"><font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[ind].getNote())%></font>&nbsp;</td>
				   		</tr>

<%					} %>

					</table>
<%			}
	} %>	

<br>
  <div align=left style="visibility:hidden" id="upld" >
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class="bottone" type="submit" value="Conferma">
            <input type="HIDDEN" name="motivo" value="<%=eventonotifica.getEvento().getCodMotivo()%>">
            <input type="HIDDEN" name="autorita" value="<%=lPrimaAutoritaEsterna.getCodTipoAutorita()%>">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActUploadEsecuzioneMS">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActLoadDettaglioOrdinediConsegna">
          </td>
        </tr>
      </table>
    </FORM>
  </div>
<br>
<br>
</body>
</html>