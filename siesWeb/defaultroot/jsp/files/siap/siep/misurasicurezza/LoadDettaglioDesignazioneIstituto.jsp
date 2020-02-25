<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.log.*"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.List" %>

<%@ page import="siap.sico.evento.model.EventoVerbaleModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>

<%@page import="org.apache.log4j.Logger"%>
<%@page import="f3b.log.LogF3B"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="eventoverbale" 		scope="request" class="siap.sico.evento.model.EventoVerbaleModel"/>
<jsp:useBean id="posizione"				scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="MisuraModel"			scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="tornadavalida"			scope="request" class="java.lang.String"/>
<jsp:useBean id="istitutodetenzione" 	scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();

String lCodTipoAutorita = "27";
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//siesLogger.debug("     -------  EVENTO = "+eventoverbale.getEvento());

String lAction = "";
if( eventoverbale != null && eventoverbale.getEvento() != null 
	&& eventoverbale.getEvento().getIdEvento() != null 
	&& eventoverbale.getEvento().getFlagDocumentoRegistrato() != null
	&& eventoverbale.getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0 )
{	
	lAction="siap.siep.misurasicurezza.action.ActLoadInserisciOEInternamento";
}
else if( eventoverbale != null && eventoverbale.getEvento() != null 
	&& eventoverbale.getEvento().getIdEvento() != null
	&& eventoverbale.getEvento().getFlagDocumentoRegistrato() != null
	&& eventoverbale.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0 )
{	
	lAction="siap.siep.misurasicurezza.action.ActUploadDesignazioneIstituto";
}

%>
<!--  LoadDettaglioDesignazioneIstituto -->
<html>
  <head>
    <title>[S.I.E.S.] -Esecuzione Misure sicurezza- Dettaglio Designazione Istituto da parte del DAP</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
     <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
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

    	
	   function stampaSiep(lAzione)
	   {
	      var  hrefStampa = lAzione;
	      var lIndice = hrefStampa.indexOf("?");
	
	      var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
	
	      stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
	   }	   
	</script>
    
    <style>
		.menulines
      	{
	        border:2.5px solid #BEC6FC;
	        text-align : center;
	        font-family: 'Tahoma';
	        color : Navy;
	        font-size : 13px;
	        text-decoration : none;
	        height:100%;
	        font-weight : normal;
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
    </style>    
 
  </head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      	<font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Annotazione Designazione Istituto</font>
      </td>
<%
  	if((eventoverbale.getEvento().getFlagDocumentoRegistrato()==null) ||
     (eventoverbale.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) )
    {%>
  		<!-- BOTTONE DI STAMPA -->
<%-- 	<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>"> --%>
<%-- 		<jsp:param name="ActionLink" value="<%= "/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaRichiestaDAP&autorita="+lCodTipoAutorita+"&IdEvento="+eventoverbale.getEvento().getIdEvento()+"&CodMotivo="+eventoverbale.getEvento().getCodMotivo()+"&IdPosizioneGiuridica="+posizione.getCodPosizioneGiuridica()%>"/> --%>
<%-- 	</jsp:include> --%>

	 <!-- BOTTONE DI STAMPA -->
	<td class="LBG">
   			<a  href="Javascript:stampaSiep('/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaRichiestaDAP&autorita=<%=lCodTipoAutorita%>&IdEvento=<%=eventoverbale.getEvento().getIdEvento()%>&CodMotivo=<%=eventoverbale.getEvento().getCodMotivo()%>&CodPosizioneGiuridica=<%=posizione.getCodPosizioneGiuridica()%>')" onclick="javascript:lookUpload();">
    			 <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
   			</a>
     </td>
     		
	 <!-- BOTTONE DI VALIDAZIONE -->
      <td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActUploadDesignazioneIstituto&IdEvento=<%=eventoverbale.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misurasicurezza.action.ActLoadDettaglioDesignazioneIstituto&IdEvento=<%=eventoverbale.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
          <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
        </a>
      </td>
   		<!-- 	ICONA DI MODIFICA	 -->
		<td class="LBG">
			<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActLoadModificaDesignazioneIstituto&IdEvento=<%=eventoverbale.getEvento().getIdEvento()%>&TornaQui=10">
       		<img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
       		</a>
       	</td>
       	<%-- MEV_39: aggiunti pulsante di cancellazione --%>
       	<!-- ICONA DI CANCELLAZIONE -->
		<td class="LBG">
  			<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActModificaDesignazioneIstituto&IdEvento=<%=eventoverbale.getEvento().getIdEvento()%>&modalita=D&TornaQui=10">
   				<img align="middle" src="/images/delete24.gif" alt="Elimina" width="24" height="24" border="0">
   			</a>
		</td>
		<%-- 20190918 [SG]: collaudo 11.3 elimino torna indietro --%>
		<!-- ICONA DI TORNA INDIETRO -->
<!-- 		<td class="LBG"> -->
<!--			<a href="javascript:history.go(-1);"> -->
<%--				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"> --%>
<!--			</a> -->
<!--		</td> -->
<%   }

	 if((eventoverbale.getEvento().getFlagDocumentoRegistrato()!=null) &&
     (eventoverbale.getEvento().getFlagDocumentoRegistrato().compareTo("S")==0) )
     {	%>
		<!-- BOTTONE DI RITORNO -->
      	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
<%	 } %>      
      
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<!--  Posizione Giuridica -->  
	<table width="90%">
    	<tr>
      		<td class="l" width="20%">Posizione Giuridica </td>
      		<td class="L" colspan=7 >
        		<font class="campo">
         		<%=posizione.getDescrPosizioneGiuridica()%>
  	    		</font>
  	    		<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(posizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" maxlength="6" size="6">
      		</td>
    	</tr>
	
<!--   // Misura di Sicurezza   -->
<%
	List lMisure =(List) request.getAttribute("listaMisure");
	if(lMisure.size() > 0)
	{
	    Iterator itx = lMisure.iterator();
	    while ( itx.hasNext())
	    {
		      MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
%>
		<tr>
			<td class=C width="20%">Misura di Sicurezza da espiare
				<input type="HIDDEN" title="id Misura" value="<%=lMis.getIdMisuraSicurezza()%>" type="text" name="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" >
			</td>
		    <td class=L ><font class="campo">&nbsp;<%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</font></td>
		    <td class=C >Num. Anni</td>
		    <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</font></td>
		    <td class=C>Num. Mesi</td>
		    <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</font></td>
		    <td class=C >Num. Giorni</td>
		    <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</font></td>
		</tr>
<%		}
	}    %>		  		
    </table>
    <br>
    
	<table width="80%" >
	<tr>
       	<td class="l" width="30%">Provvedimento di: </td>
     	<td class="L" colspan=3><font class="campo"> Designazione Istituto per esecuzione Misura Sicurezza dal DAP</font> </td>
    </tr>
	<tr>
   		<td class="l" width="30%">Data Emissione Provvedimento</td>
   		<td class="L" colspan=1 width="50%">
   			<font class="campo">
      			<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoverbale.getEvento().getDataEmissione(),"dd-MM-yyyy"))%>
       		</font>
       	</td>
    </tr>
    <tr><td>&nbsp;</td></tr>    
    <!--Data Pervenimento Richiesta e Data Designazione Istituto-->   	
  		<tr>
       		<td class="l" width="30%">Data Pervenimento </td>
       		<td class="L" colspan=1 width="50%">
       			<font class="campo">
        			<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoverbale.getVerbale().getDataPervenimento(),"dd-MM-yyyy"))%>
        		</font>
        	</td>
        </tr>
        <tr>	
        	<td class="l" width="30%">Data Designazione</td>
	    	<td class="L" colspan=1  width="50%">
	      		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoverbale.getVerbale().getDataEmissione(),"dd-MM-yyyy"))%></font>
	    	</td>
        </tr>
   </table>     	
  <!--Autorità DAP-->   
<table width="80%">  
  <tr>
     <td class="l" width="30%">Autorità che ha proceduto alla Designazione</td>
     <td class="L" width="50%">
     	<font class="campo"><%=StringUtils.toStringJSP(eventoverbale.getVerbale().getDescrTipoUfficioFirmatario())%>&nbsp;</font>
    	 	di
     	<font class="campo">  &nbsp;ROMA </font>   
     </td>
  </tr>
</table>  
  <!--Istituto Designato-->   
<table width="80%">  
  <tr>
     <td class="l" width="30%">Struttura Designata</td>
     <td class="L" width="50%">
          <font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getDescrTipoIstituto())%>&nbsp;</font>
			di
          <font class="campo"> &nbsp;<%=StringUtils.toStringJSP(istitutodetenzione.getDescrComune()) %></font>
      </td>
  </tr>
</table>  
<!--Numero di protocollo-->
<table width="80%">  
  <tr>
     <td class="l" width="30%">Numero Protocollo Nota</td>
     <td class="L" width="50%">
          <font class="campo">
            <%=StringUtils.toStringJSP(eventoverbale.getVerbale().getNumeroProtocollo(), "-" )%>
          </font>
      </td>
  </tr>
</table>

<%if((eventoverbale.getEvento().getFlagDocumentoRegistrato()!=null) &&
    (eventoverbale.getEvento().getFlagDocumentoRegistrato().compareTo("S")==0) )
  { %>
  		<!--form name="comandiperOE" method="POST" action="/jsp/Main.jsp">
		  <table>
        	<tr>
          		<td class="L">
            	  <INPUT  class="bottone" type="submit" name="ORDESEC" value="Ordine Esecuzione per Internato">
		    	  <input type="HIDDEN" name="motivo" value="<%=eventoverbale.getEvento().getCodMotivo()%>">
				  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventoverbale.getEvento().getIdEvento() %>">
		    	  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActLoadDettaglioDesignazioneIstituto">
				  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActLoadInserisciOEInternamento">
          		</td>
        	</tr>
      	  </table>
    	</form -->
    	<br>
    	<table cellpadding="5" cellspacing="5" width="32%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
		<tr>
			<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciComunicazionePolizia">Comunicazione 
			</a>
			</td>		
			<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciOrdinediConsegna">Ordine di Consegna 
			</a>
			</td>
			<%-- MEV_39: aggiunto parametro di passaggio per la action chiamata --%>
			<td width="32%" class="menulines" nowrap>
				<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
					=siap.siep.misurasicurezza.action.ActLoadInserisciOEInternamento&idIstitutoDetenzione=<%=istitutodetenzione.getIdIstitutoDetenzione()%>">Ordine Esecuzione per Internamento
				</a>
			</td>
		</tr>
		</table>	
<%}%>
	     
  <div align=left style="visibility:hidden" id="upld" >
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
		<br>
		<table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
          <tr>
          <td class="L">
            <input class="bottone" type="submit" value="Conferma">
      		<input type="HIDDEN" name="motivo" value="<%=eventoverbale.getEvento().getCodMotivo()%>">
      		<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventoverbale.getEvento().getIdEvento() %>">
      		<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActLoadDettaglioDesignazioneIstituto">
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActUploadDesignazioneIstituto"> 
          </td>
          </tr>
       </table>
    </FORM>
  </div>
  
  
<br>
<br>
</body>
</html>