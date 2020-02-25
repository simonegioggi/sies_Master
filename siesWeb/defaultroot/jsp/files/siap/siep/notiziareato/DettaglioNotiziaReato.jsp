<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.notiziareato.action.ICostantiNotiziaReato"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.math.BigDecimal" %>

<jsp:useBean id="notiziareato" scope="request" class="siap.siep.notiziareato.model.NotiziaReatoModel"/>

<%@ page import="f3b.security.model.ProfileModel"%>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>

<%

// Si ricava il profilo dell'utente connesso
ProfileModel lProfilo =(ProfileModel) UtenteConnesso.getUserProfile();

BigDecimal profilo = (BigDecimal) request.getAttribute("profilo");
%>


<html>
	<head>
		<title>[S.I.A.P.] - Dettaglio NotiziaReato </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js"></script>
	</head>


	<body class="corpo">
		<!-- INTESTAZIONE -->
		<FORM name="comandi" >
			<table>
    			<tr>
              <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			        <td class="LBG">
				        <font class="label">Funzione :</font>&nbsp;
				        <font class="campo">Dettaglio Notizia di Reato</font>
			      	</td>
					  <!-- Inserimento bottoni per la modifica e la cancellazione -->
				      <td class="LBG">
				      <% if( lProfilo.isSige()) { %>
				      	<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
				      	<jsp:param name="CampoIdEntita" value="<%=ICostantiNotiziaReato.CAMPO_ID_NOTIZIA_REATO%>" />
                  <jsp:param name="ValoreIdEntita" value="<%=notiziareato.getIdNotiziaReato()%>" />
                  		</jsp:include>
				      <%}else { %>
				        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
				          <jsp:param name="CampoIdEntita" value="<%=ICostantiNotiziaReato.CAMPO_ID_NOTIZIA_REATO%>" />
                  <jsp:param name="ValoreIdEntita" value="<%=notiziareato.getIdNotiziaReato()%>" />
				     	  </jsp:include>
				     <%} %>
				     
				     </td>
   				</tr>
 			</table>

			<!-- Visualizzazione Dettagli Procedimento -->
			<%
		if( lProfilo.isSige()) {
		%>
			<br>
    			<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
    		<br>
    		<%
		}
    	else{%>
    	<br>
      			<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    		<br>
    	<% }
    	%>
		</FORM>
		<table cellspacing=4 cellpadding=4>
			<tr>
				<td class="l">Data Pervenimento</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notiziareato.getDataPervenimento(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			</tr>
			<tr>
				<td class="l">Data Acquisizione</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notiziareato.getDataAcquisizione(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			</tr>
			<tr>
				<td class="l">Acquisizione Diretta</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(notiziareato.getAcquisizioneDiretta()) %></font>&nbsp;</td>
			</tr>
			<tr>
				<td class="l">Data Fatto</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notiziareato.getDataFatto(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			</tr>
			<tr>
				<td class="l">Descrizione Fonte</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(notiziareato.getDescrizioneFonte()) %></font>&nbsp;</td>
			</tr>
			<tr>
				<td class="l">Comune Fonte</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(notiziareato.getDescrComuneFonte()) %></font>&nbsp;</td>
			</tr>
			<tr>
				<td class="l">Numero Registro Autorità</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(notiziareato.getNumRegAutorita()) %></font>&nbsp;</td>
			</tr>
			<tr>
				<td class="l">Luogo Provenienza</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(notiziareato.getLuogoProvenienza()) %></font>&nbsp;</td>
			</tr>
			<tr>
				<td class="l">Numero Ricevuta</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(notiziareato.getNumeroRicevuta()) %></font>&nbsp;</td>
			</tr>			
			<tr>
				<td class="l">Data Arresto</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notiziareato.getDataArresto(),"dd-MM-yyyy"))%>&nbsp;</font></td>
			</tr>
			<tr>
				<td class="l">Data Fermo</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notiziareato.getDataFermo(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			</tr>
			<!-- CAMPI PER INTEGRAZIONE REGE-SIES -->
			<tr>
				<td class="l">Fotosegnalato</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(notiziareato.getFlagFotosegnalato()) %></font>&nbsp;</td>
			</tr>
			<%if(StringUtils.toStringJSP(notiziareato.getFlagFotosegnalato()).equalsIgnoreCase("S")){ %>
				<tr>
					<td class="l">Data Fotosegnalazione </td>
					<td class="l"> 
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notiziareato.getDataFoto(),"dd-MM-yyyy"))%></font>
					</td>
				</tr>
					
				<tr>
					<td class="l">Autorit&agrave; Fotosegnalazione </td>
					<td class="l"><font class="campo"><%=StringUtils.toStringJSP(notiziareato.getDescAutoritaFoto()) %></font>
					</td>						
				</tr>
					
				<tr>						
					<td class="l">Comune Fotosegnalazione </td>						
					<td class="l"><font class="campo"><%=StringUtils.toStringJSP(notiziareato.getDescComuneFoto()) %></font>
					</td>						
				</tr>
			<%}%>
		</table>
	</body>
</html>