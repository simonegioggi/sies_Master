<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>

<jsp:useBean id="richiestaconversione" scope="request" class="siap.siep.penapecuniaria.model.RichiestaConversioneModel"/>


<html>
	<head>
	    <title> [S.I.E.S.] - Dettaglio Annulla Partita di Credito - </title>
	    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
	    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

	</head>
	<BODY class="corpo">
		<FORM name="DettaglioAnnullaPartita">
	    	<table>
	      		<tr>
	      			<td class="LBG"><a href="Javascript:window.print();">
	      				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
	      			</td>
	        		<td class="LBG">
			        	<font class="label">Funzione :</font>&nbsp;
			        	<font class="campo">Dettaglio Annotazione Annullamento Partita di Credito</font>
			        </td>
	        		<td class="LBG">
	      				<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
	      					<jsp:param name="CampoIdEntita" value="<%=ICostantiPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE%>" />
             				<jsp:param name="ValoreIdEntita" value="<%=richiestaconversione.getIdRichiestaConversione()%>" />
           				</jsp:include>
					</td>
				</tr>
	    	</table>
		</FORM>
  		<br>
    	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  		<br>
		<table>
	  		<tr>
				<td class="l">Anno/Numero Partita</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getAnnoPartita()) %>
			 							  			/ 
			 					 		  		  <%=StringUtils.toStringJSP(richiestaconversione.getNumPartita()) %></font>
				</td>
			    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getNumExCampione()) %></font>&nbsp;</td>
			</tr>
			<tr>
    			<td class="l">Autorita Emittente</td>
    			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDescrTipoAutoritaEmittente()) %></font>&nbsp;</td>
  			</tr>
			<tr>
		    	<td class="l">Sede</td>
		    	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDescrLuogoEmittente()) %></font>&nbsp;</td>
			</tr>
			<tr>
		    	<td class="l">Data Ricezione Atto</td>
		    	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataRicezioneAtto(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			</tr>
			<tr>
		    	<td class="l">Data Iscrizione Atto</td>
		    	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataIscrizioneAtto(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			</tr>
			<tr>
		    	<td class="l">Data Annullamento Partita di Credito</td>
		    	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataEsazione(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			</tr>

      	</table>
	</body>
</html>