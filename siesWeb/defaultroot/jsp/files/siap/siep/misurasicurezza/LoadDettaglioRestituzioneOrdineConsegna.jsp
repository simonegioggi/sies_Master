<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina di caricamento dettaglio restituzione ordine di consegna --%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List" %>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>

<jsp:useBean id="eventonotifica"	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizione"			scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="MisuraModel"		scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="notifica"			scope="request" class="siap.siep.notifica.model.NotificaModel"/>

<%
MagistratoModel lMagistrato = eventonotifica.getMagistrato();
if (lMagistrato == null)
  lMagistrato = new MagistratoModel();

// Misura Sicurezza
String CodMisura = "";
String MisidMisura = "";
if (MisuraModel != null && MisuraModel.getCodTipo() != null && MisuraModel.getIdMisuraSicurezza() != null) {	
	CodMisura = MisuraModel.getCodTipo();
	// id_Misura della misura Corrente = Mis_id_Misura della eventuale Misura precedente
	MisidMisura = MisuraModel.getIdMisuraSicurezza().toString();
}

// Posizione Giuridica
String CodPos = "";
if (posizione != null && posizione.getCodPosizioneGiuridica() != null)
	CodPos = posizione.getCodPosizioneGiuridica();
%>

<!--  LoadDettaglioRestituzioneOrdineConsegna -->
<html>
  	<head>
    <title>[S.I.E.S.] - Gestione Misure sicurezza - Esecuzione MS - Dettaglio Restituzione Ordine di Consegna</title>
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
	   
	   
	   function lookUpload()
	    {
	      var node;
	      node=document.getElementById('upld');
	      node.style.visibility='visible';
	    }
	   
	</script>
	
	</head>
	<body class="corpo">
  	<table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
   				</a>
   			</td>
      		<td class="LBG">
      			<font class="label">Funzione:</font>&nbsp;&nbsp;
        		<font class="campo">Dettaglio Restituzione Ordine di Consegna per Esecuzione Misure Sicurezza</font>
      		</td>
<%
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() == null
		|| eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
%>
			<%-- MEV_39 --%>
			<!-- BOTTONE DI STAMPA -->
			<td class="LBG">
       			<a  href="Javascript:stampaSiep('/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaRestituzioneOrdineConsegna&MisIdMisuraSicurezza=<%=MisidMisura%>&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&CodMotivo=<%=eventonotifica.getEvento().getCodMotivo()%>&CodPosizioneGiuridica=<%=CodPos%>&CodTipo=<%=CodMisura%>')" onclick="javascript:lookUpload();">
        			 <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
       			</a>
     		</td>
     		<!-- BOTTONE DI VALIDAZIONE -->
   			<td class="LBG">
       		<%-- 	<a href="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActUploadRestituzioneOrdineConsegna&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misurasicurezza.action.ActLoadDettaglioRestituzioneOrdineConsegna&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
       				<img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
       			</a> --%>
       			
       			<a  onclick="javascript:lookUpload();">
		        <img  align="middle" src="/images/upload24.gif" alt="Upload Stampa" width="24" height="24" border="0">
		      </a>
      
   			</td>
			<!-- ICONA DI MODIFICA -->
			<td class="LBG">
				<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActLoadModificaRestituzioneOrdineConsegna&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&TornaQui=10">
         			<img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        		</a>
        	</td>
        	<!-- ICONA DI ELIMINAZIONE -->
			<td class="LBG">
				<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActModificaRestituzioneOrdineConsegna&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&modalita=D&TornaQui=10">
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
<%
}
%>
   		</tr>
	</table>
  	<br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  	<br>
	<table cellspacing=0 cellpadding=0 width="95%">
    	<tr>
      		<td class="l" width="20%">Posizione Giuridica</td>
      		<td class="L">
      			<input type="HIDDEN" value="<%=CodPos%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">
	        	<input type="HIDDEN" value="<%=CodMisura%>" name="<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>">
				<input type="HIDDEN" value="<%=MisidMisura%>" name="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>">
				<input type="HIDDEN" value="<%=MisidMisura%>" name="<%=ICostantiMisuraSicurezza.CAMPO_MIS_ID_MISURA_SICUREZZA%>">
        		<font class="campo">
         			<%=posizione.getDescrPosizioneGiuridica()%>
  	    		</font>
      		</td>
    	</tr>
    </table>	
	<%-- Eventuali Misura di Sicurezza --%>
	<table cellspacing=0 cellpadding=0 width="95%">
<%
List lMisure = (List) request.getAttribute("listaMisure");
if (lMisure.size() > 0) {
	Iterator itx = lMisure.iterator();
	while (itx.hasNext()) {
		MisuraSicurezzaModel lMis = (MisuraSicurezzaModel) itx.next();
%>
		<tr>
			<td class="l" width="20%">Misura di Sicurezza da espiare</td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%></font></td>
			<td class="l">Anni</td>
			<td class="c"><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%></font></td>
			<td class="l">Mesi</td>
			<td class="c"><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%></font></td>
			<td class="l">Giorni</td>
			<td class="c"><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%></font></td>	
		</tr>
<%
	}
}
%>		  		
	</table>

	<br>
	<!-- Evento -->  
	<table cellspacing=0 cellpadding=0 width="95%">
		<tr>
			<td class="l" width="20%">Tipo Provvedimento</td>
        	<td class="L">
        		<font class="campo">
        			<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrTipoProvvedimento())%>
        			&nbsp;
        			<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%>
        		</font>
        	</td>
        </tr>
	  	<tr>
	    	<td class="l">Data Emissione</td>
	    	<td class="L">
	      		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy"))%></font>
	    	</td>
	  	</tr>
	  	<tr>  
	    	<td class="l" width="20%">Data Trasmissione</td>
	    	<td class="L">
	      		<font class="campo">
	        		<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifica.getDataInvio(),"dd-MM-yyyy"))%>
	      		</font>
	    	</td>
     	</tr>
     	<!-- Magistrato -->
     	 <tr>
     		<td class="l" width="20%">Magistrato </td>
     		<td class="L">
     			<input type="HIDDEN" value="<%=StringUtils.toStringJSP(lMagistrato.getCodMagistrato())%>" name="<%=ICostantiEvento.CAMPO_COD_MAGISTRATO%>">
          		<font class="campo">
            		<%=StringUtils.toStringJSP(lMagistrato.getCognome())%> &nbsp;<%=StringUtils.toStringJSP(lMagistrato.getNome())%>
          		</font>
      		</td>
		</tr>		
		<!-- Autorita' -->
		<tr>
			<td class="Titolo" colspan="2">Destinatari</td>
		</tr>
		<tr>
		    <td class="l">Destinatari per la Restituzione Ordine di Consegna</td>
		    <td class="L">
<%
if (notifica.getAutoritaEsterna() != null) {
%>
				<font class="campo"><%=StringUtils.toStringJSP(notifica.getAutoritaEsterna().getDescrTipoAutorita())%></font>
					&nbsp;di&nbsp;
				<font class="campo"><%=StringUtils.toStringJSP(notifica.getAutoritaEsterna().getDescrSede())%></font>
<%
} else {
	if (notifica.getIstitutoDetenzione() != null) {
%>
				<font class="campo">Istituto di Detenzione </font>
				&nbsp;
				<font class="campo"><%=StringUtils.toStringJSP(notifica.getIstitutoDetenzione().getDescrTipoIstituto())%></font>
				&nbsp;di&nbsp;
				<font class="campo"><%=StringUtils.toStringJSP(notifica.getIstitutoDetenzione().getDescrizione())%></font>
			</td>
		</tr>
<%
	}
}
if (notifica.getNote() != null) {
%>
		<tr>
			<td class="l">Indirizzo</td>
			<td class="L"><font class="campo"><%=StringUtils.toStringJSP(notifica.getNote())%></font></td>
		</tr>
<%
}
if (eventonotifica.getCampoNote() != null && eventonotifica.getCampoNote().length > 0) {
%>
		<tr>
			<td class="l">Note</td>
			<td class="L"><font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getCampoNote()[0].getDescr())%></font></td>
		</tr>
<%
}
%>
	</table>
	<br>
  	<div align="left" style="visibility: hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
	<table>
		<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>"/>
        <tr>
          	<td class="L">
	            <input class="bottone" type="submit" value="Conferma">
	            <input type="HIDDEN" name="motivo" value="<%=eventonotifica.getEvento().getCodMotivo()%>">
	            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActUploadRestituzioneOrdineConsegna">
	            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
	            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActLoadDettaglioRestituzioneOrdineConsegna">
          	</td>
		</tr>
	</table>
    </FORM>
  	</div>
</body>
</html>