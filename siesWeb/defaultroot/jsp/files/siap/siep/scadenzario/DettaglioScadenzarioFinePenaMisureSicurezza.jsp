<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: modificata gestione della pagina --%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.lang.String" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Calendar"%>

<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel"%>
<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.siep.scadenzario.util.ScadenzarioUtils"%>

<jsp:useBean id="scadenzario" 	scope="request" class="siap.siep.scadenzario.model.ScadenzarioModel" />
<jsp:useBean id="fascicoloSiep" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="fascMsToFascSiepModel" scope="request" class="siap.siep.misurasicurezza.model.FascMsToFascSiepModel" />

<html>
	<head>
		<title>[S.I.E.S.] - Dettaglio Scadenzario - Data Scadenza Comunicazione Misura Sicurezza</title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js"></script>
	</head>
	<body class="corpo">
		<FORM name="comandi" >
    	<table>
      		<tr>
      			<td class="LBG">
      				<a href="Javascript:window.print();">
      					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
      				</a>
      			</td>
        		<td class="LBG">
			        <font class="label">Funzione: </font>
			        <font class="campo">Dettaglio Scadenzario - Data Scadenza Comunicazione Misura Sicurezza</font>
      			</td>
      			<td class="LBG">
            		<a href="javascript:history.go(-1);">
             			<img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            		</a>
          		</td>
 				<td class="LBG">
					<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
						<jsp:param name="CampoIdEntita" value="<%=ICostantiScadenzario.CAMPO_ID_SCADENZARIO%>"/>
						<jsp:param name="ValoreIdEntita" value="<%=scadenzario.getIdScadenzario()%>"/>
       				</jsp:include>
     			</td>
     		</tr>
 		</table>
		</FORM>
<%
ScadenzarioModel lSca = new ScadenzarioModel(scadenzario);
FascicoloSiepModel lFas = new FascicoloSiepModel(fascicoloSiep);
SoggettoModel lSog = lFas.getSoggetto();
SentenzaModel lSentMod = lFas.getSentenza();
%>
		<table cellspacing="0" cellpadding="0" width=95%>
    		<tr>
      			<td class="L">
        			<font class="label">Procedimento N.</font>
        			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
						<%=lFas.getChiaveAnno()%>
						/
						<%=lFas.getChiaveProgr()%>
        			</a>
      			</td>
    		</tr>
    		<tr>
      			<td class="L" width=100%><font class="label">Soggetto:</font>
      				<font class="campo">
        				<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=lSog.getIdSoggetto()%>" title="Soggetto">
          					<%=lSog.getCognome()%>&nbsp;<%=lSog.getNome()%>
        				</a>
      				</font>&nbsp;
<%
if (lSog.getSesso().compareTo("F") == 0) {
%>
          			<font class="label">nata il:</font>&nbsp;
<%
} else {
%>
          			<font class="label">nato il:</font>&nbsp;
<%
}
%>
	      			<font class="campo"><%=DateUtils.getDateToString(lSog.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
	      			<font class="label">in: </font>
	      			<font class="campo">
<%
if (lSog.getDescrComuneNascita().compareTo("-") == 0) {
%>
        				<%=lSog.getDescrStatoNascita()%>
<%
} else {
%>
        				<%=lSog.getDescrComuneNascita() + " ("+lSog.getCodProvinciaNascita() + ")"%>
<%
}
%>
     				</font>
     			</td>
    		</tr>
    		<tr>
      			<td class="L">
	        		<font class="campo"><%=lSentMod.getDescrTipoProvvedimento()%></font>&nbsp;<font class="label">N.</font>
	        		<font class="campo">
	          			<%=lSentMod.getAnnoSentenza()%> / <%=lSentMod.getNumeroSentenza()%>&nbsp;
	          			<font class="label">del</font>&nbsp;
	       				<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=lSentMod.getIdSentenza()%>" title="Sentenza">
	         				<%=DateUtils.getDateToString(lSentMod.getDataProvvedimento(), "dd-MM-yyyy")%>
	       				</a>
	       			</font>
<%
if (!"02".equals(lSentMod.getCodTipoProvvedimento())) {
%>
					&nbsp;<font class="label">Emessa da: </font>
<%
} else {
%>
					&nbsp;<font class="label">Emesso da: </font>
<%
}
%>
        			<font class="campo"><%=lSentMod.getDescrTipoAutoritaEmittente()%></font>&nbsp;
<%
if (lSentMod.getNumSezioneAutoritaEmittente() != null) {
%>
	          		<font class="label">(Sez.</font>
	          		<font class="campo"><%=lSentMod.getNumSezioneAutoritaEmittente()%></font>
	          		<font class="label">)</font>
<%
}
%>
	        		<font class="label"> di </font>
	        		<font class="campo"><%=lSentMod.getDescrLuogoEmittente()%></font>
	      		</td>
	    	</tr>
	    	<tr>
	      		<td class="L">
	        		<font class="label">Data irrevocabilità: </font>
	        		<font class="campo"><%=DateUtils.getDateToString(lFas.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
	      		</td>
	   		</tr>
	  	</table>
		<br>
		<!-- Misure di Sicurezza già presenti -->
		<table cellspacing="0" cellpadding="0" width=95%>
<%
List lMisure = (List) request.getAttribute("listaMisure");
if (lMisure.size() > 0) {
	Iterator itx = lMisure.iterator();
	while (itx.hasNext()) {
		MisuraSicurezzaModel lMis = (MisuraSicurezzaModel) itx.next();
 %>		
			<tr>
				<td class="L" width="20%">Misura di Sicurezza da espiare</td>
				<td class="L" width="40%"><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%></font></td>
				<td class="L">
					Anni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(), "0")%>&nbsp;</font>
					Mesi&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(), "0")%>&nbsp;</font>
					Giorni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(), "0")%></font>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
<%
	}
}
%>					
		</table>
		<table cellspacing="0" cellpadding="0" width="95%">
			<tr>
				<td class="l" width="20%">N° SIEP Collegato</td>
<%
		 if (fascMsToFascSiepModel != null && fascMsToFascSiepModel.getChiaveAnnoSiepCollegato() !=null) {
%>				
      	  		<td class="l">
      	  			<font class="campo">
	           			<%=fascMsToFascSiepModel.getChiaveAnnoSiepCollegato()%>
	           			/
	           			<%=fascMsToFascSiepModel.getChiaveProgrSiepCollegato()%>
	           		</font>
             	</td>
<%
		} 
%> 
          	</tr>
			<tr>
				<td class="l">Data Inizio Pena</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy"))%></font></td>
          	</tr>  		
          	<tr>
				<td class="l">Data Fine Pena</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy"))%></font></td>
          	</tr>
          	<tr>
				<td class="l">Data Scadenza Comunicazione</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataScadenzaComunicazione(),"dd/MM/yyyy"))%></font></td>
          	</tr>
          	<tr>
				<td class="l">N° Giorni Residui</td>
				<td class="l">
					<font class="campo">
<%
// Scaduti
if (lSca.getDataFineScadenza() != null) {
	if (lSca.getGiorniResidui() != null && lSca.getGiorniResidui().intValue() < 0) {
%>
						<%=ScadenzarioUtils.getDifferenza(DateUtils.getSysDate(), lSca.getDataFineScadenza())%>
<%
	} else {
%>
						<%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(), DateUtils.getSysDate())%>
<%
	}
}
%>
					</font>
				</td>
			</tr>
			<tr>
				<td class="l">Tipologia Differimento</td>
				<td class="l">
					<font class="campo">
<%
// In Scadenza Oggi
if (lSca.getGiorniResidui() != null && lSca.getGiorniResidui().intValue() == 0) {
%>
						In Scadenza Oggi
<%
}
// In scadenza (< 7gg)
else if (lSca.getGiorniResidui() != null
		&& lSca.getGiorniResidui().intValue() <= 7
  		&& lSca.getGiorniResidui().intValue() > 0) {
%>
						In Scadenza Entro 7 Giorni
<%
}
// In scadenza
else if (lSca.getGiorniResidui() != null
		&& lSca.getGiorniResidui().intValue() > 7) {
%>
						In Scadenza
<%
}
// Scaduti
else {
%>
						Scaduto
<%
}
%>
					</font>
				</td>
			</tr>
  		</table>
	</body>
</html>