<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.web.Action" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaFascicoliModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel" %>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc" %>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="UtenteConnesso" 	scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggettifascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="RequestForPaging" 	scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui" 			scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante" 	scope="request" class="java.lang.String" />
<jsp:useBean id="FascicoliOrdinaze" scope="request" class="java.util.Vector" />
<jsp:useBean id="NumerazioneManualeMisureProvvFS"    scope="request" class="java.lang.String"/>

<%
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//siesLogger.debug(" --XX-- RicercaProv - NumerazioneManualeMisureProvvFS = "+NumerazioneManualeMisureProvvFS);
%>

<!-- 		 RicercaProvvedimentiIscrizioneMisuraFuoriSentenza		-->
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Provvedimenti - Misura Sicurezza Disposta Fuori Sentenza </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
var node;
 function soggetto(idSog, idOrd )
 {
	 //alert("  idSog = "+idSog+" -  idOrd = "+idOrd);
	  document.elenco.<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>.disabled=false;
	  document.elenco.<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>.value=idSog;
	  
	  document.elenco.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC%>.disabled=false;
	  document.elenco.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC%>.value=idOrd;
 }

 function nuovo()
 {
	  document.elenco.<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>.disabled=true;
	  document.elenco.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC%>.disabled=true;
 }
</script>
  </head>

  <BODY class="corpo" onLoad="Javascript:nuovo()">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActLoadIscrizioneProcedimentoMisuraFuoriSentenza">
  <input type="hidden" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value=""> 
  <input type="hidden" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC %>" value="">
  <input type="HIDDEN" name="<%=ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS%>" value="<%=NumerazioneManualeMisureProvvFS %>"> 
  
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
         <font class="label">Funzione:</font>&nbsp;
         <font class="campo">Elenco Provvedimenti della Sorveglianza relativi a Misure Sicurezza Disposte fuori Sentenza </font>
        </td>
				<!-- BOTTONE DI RITORNO -->
				<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
			</tr>

    </table>
    <br>
 
<% //if (!(RequestForPaging.equals("NO"))) {%>
	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<%//}%>

<br>
<br>
<%

  String wCol1="9%";
  String wCol2="10%";
  String wCol3="11%";
  String wCol4="8%"; 
  String wCol5="8%";
  String wCol6="8%";
  String wCol7="21%";
  String wCol8="20%";
  String wCol9="5%";

%>
	<td class=l>
		<font style="color:red; font-size: 9pt;" class="label"> N.B.: I provvedimenti già ELABORATI sono evidenziati in rosso nell'elenco </font>
	</td>
<table cellspacing=2 cellpadding=2 style="width:100%;">
	<br>
	<tr>
    	<td class="int" width="<%=wCol1%>">Anno/Numero Sius</td>
    	<td class="int" width="<%=wCol2%>">Ufficio Mittente</td>
    	<td class="int" width="<%=wCol3%>">Soggetto</td>
      	<td class="int" width="<%=wCol4%>">Data Emissione</td>
      	<td class="int" width="<%=wCol5%>">Tipo Provv</td>
      	<td class="int" width="<%=wCol6%>">Anno/Numero</td>
		<td class="int" width="<%=wCol7%>">Descrizione Motivo</td>
		<td class="int" width="<%=wCol8%>">Esito</td>
      	<td class="int" width="<%=wCol9%>" Title="Selezione">Sel.</td>
    </tr>
</table>
 
<%
    Iterator itx = FascicoliOrdinaze.iterator();
    while (itx.hasNext()) {
    	Boolean giaElabo = false;
    	String lClasse="C";
    	OrdinanzaEventoTenoriFascicoloSiusModel FascOrdModel = (OrdinanzaEventoTenoriFascicoloSiusModel)itx.next();
    	SoggettoModel soggetto = FascOrdModel.getSoggetto();
    	
    	if (FascOrdModel != null && FascOrdModel.getOrdinanza() != null && 
    		FascOrdModel.getOrdinanza().getFlagElaborato() != null &&
    		FascOrdModel.getOrdinanza().getFlagElaborato().compareTo("S") == 0) {
    		giaElabo=true;
    		lClasse="crosso";
    	}

%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	<%-- <%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%> --%>
     	<table cellspacing=2 cellpadding=2 style="width:100%;">
   			<tr>
			<td class="<%=lClasse%>" width="<%=wCol1%>">
   				<%=StringUtils.toStringJSP(FascOrdModel.getFascicoloSiusModel().getChiaveAnno()) %>
       			/
       			<%=StringUtils.toStringJSP(FascOrdModel.getFascicoloSiusModel().getChiaveProgr()) %>
       		</td>       		
       		<td class="<%=lClasse%>" width="<%=wCol2%>">
				<%=StringUtils.toStringJSP(FascOrdModel.getDescrTipoUfficio()) %>
				di
				<%=StringUtils.toStringJSP(FascOrdModel.getDescrComuneUfficio()) %>
       		</td>
       		<td class="<%=lClasse%>" width="<%=wCol3%>">
				<%= StringUtils.toStringJSP(soggetto.getCognome()) %>&nbsp;<%=StringUtils.toStringJSP(soggetto.getNome()) %>
       		</td>
       		<td class="<%=lClasse%>" width="<%=wCol4%>">
				<%=DateUtils.getDateToString(FascOrdModel.getEvento().getDataEmissione(),"dd-MM-yyyy")%>
       		</td>
       		<td class="<%=lClasse%>" width="<%=wCol5%>">
				<%= StringUtils.toStringJSP(FascOrdModel.getDescrProvvedimento() ) %>
       		</td>
	       	<td class="<%=lClasse%>" width="<%=wCol6%>">
				<%= StringUtils.toStringJSP(FascOrdModel.getOrdinanza().getAnnoS3()) %>
				/
				<%= StringUtils.toStringJSP(FascOrdModel.getOrdinanza().getNumS3()) %>	
       		</td>
       		<td class="<%=lClasse%>" width="<%=wCol7%>">
				<%= StringUtils.toStringJSP(FascOrdModel.getDescrOggetto()) %>
       		</td>
       		<td class="<%=lClasse%>" width="<%=wCol8%>">
				<%= StringUtils.toStringJSP(FascOrdModel.getDescrEsito()) %>
       		</td>       		       		
   			<td class="<%=lClasse%>" width="<%=wCol9%>">
         		<input type="radio" title="Selezione del Soggetto" name="radioins" onClick ="Javascript:soggetto('<%=soggetto.getIdSoggetto()%>', '<%=FascOrdModel.getOrdinanza().getIdDepositoOrdinanzaPc() %>')">   
     		</td>
    		</tr>
	 	</table>
	 	
<% 	}%>
	  <td class=l>
		<font style="color:green; font-size: 9pt;" class="label"> N.B.: Se non si seleziona nessun provvedimento, il sistema proporrà la maschera di Iscrizione senza campi precompilati</font>
	  </td>
  <table>
  <br>
     <tr>
      <td>
        <input class="bottone" type="submit" name="Avanti" value="Avanti >>>">
      </td>
    </tr>
  </table>
  </FORM>
  <br>

</body>
</html>