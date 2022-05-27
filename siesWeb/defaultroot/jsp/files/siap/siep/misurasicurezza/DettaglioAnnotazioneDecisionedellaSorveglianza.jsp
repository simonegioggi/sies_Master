<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel" %>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>

<jsp:useBean id="eventonotifica"			scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra"		scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"				scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="StrdataInizioPena"			scope="request" class="java.lang.String"/>
<jsp:useBean id="StrdataFinePenaA"			scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"				scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="tipoMisuraSicurezza" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="naturaMisuraSicurezza" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="provvedimento"				scope="request" class="siap.sius.misurasicurezza.model.ProvvedimentoEventoTenoreFascicoloSiusModel"/>
<jsp:useBean id="VecMisureSic"				scope="request" class="java.util.Vector"/>
<%
String FlagIstanza="";
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
if (lPosizione == null)
	lPosizione = new PosizioneGiuridicaModel();
UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
MisuraSicurezzaModel nuovaMis = null;
List listaMisure =(List) request.getAttribute("listaMisureSic");
/*
int nMis = 0;
if (MisuraMod == null) {
 	List listaMisure =(List) request.getAttribute("listaMisureSic");
 	if(listaMisure != null)
  		nMis = listaMisure.size();
	if(nMis != 0)
		nuovaMis = (MisuraSicurezzaModel)listaMisure.get(nMis-1);
} else {	
	nMis++;
	nuovaMis = new MisuraSicurezzaModel(MisuraMod);
}
*/
String tipoProvv = "";
String annoS3S72 = "";
String numS3S72 = "";
%>
<!-- DettaglioAnnotazioneDecisioneDellaSorveglianza -->
<html>
<head>
<title>[S.I.E.S.] - Gestione Misure sicurezza - Annotazione Decisione della Sorveglianza</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
</script>
<script language="JavaScript1.2">
function over_effect(e, state) {
	if (document.all)
	  	source4 = event.srcElement;
	else if (document.getElementById)
	  	source4 = e.target;
	if (source4.className == "menulines")
	  	source4.style.borderStyle = state;
	else {
	   while (source4.tagName != "TABLE") {
	     	source4 = document.getElementById ? source4.parentNode : source4.parentElement;
	     	if (source4.className=="menulines")
	       		source4.style.borderStyle = state;
	   }
	}
}
</script>
<style>
.menulines {
	border:2.5px solid #BEC6FC;
	text-align : center;
	font-family: 'Tahoma';
	color : Navy;
	font-size : 13px;
	text-decoration : none;
	height:100%;
	font-weight : normal;
}
.menulines a {
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
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
      	<td class="LBG">
	      	<font class="label">Funzione :</font>&nbsp;&nbsp;
	        <font class="campo">Dettaglio Annotazione Decisione della Sorveglianza</font>
      	</td>
<%
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() == null
		|| eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
%>
		<!-- BOTTONE DI VALIDAZIONE -->
      	<td class="LBG">
        	<a href="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActUploadAnnotazioneDecisioneDellaSorveglianza&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misurasicurezza.action.ActDettaglioAnnotazioneDecisioneDellaSorveglianza&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
          		<img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
        	</a>
      	</td>
<%
}
%>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<table>
	<tr>
		<td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          	<font class="campo"><%=lPosizione.getDescrPosizioneGiuridica()%></font>
	        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
	      	<input type="HIDDEN" title="id Evento" value="" type="text" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>">
        </td>
	</tr>
<%	//fine modifica relativa al tipo istituto
if (penaresidua.getIdPenaResidua() != null
		&& ((penaresidua.getFlagErgastolo() == null)
				|| (penaresidua.getFlagErgastolo() != null
    			&& !penaresidua.getFlagErgastolo().equals("S") 
    			&& !penaresidua.getFlagErgastolo().equals("D")))) {
	if (penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0
	   		&& penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0) {

  	} else {
%>
	<tr>
		<td class="l">Reclusione</td>
		<td class="l" colspan=2>
		  	<font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
		</td>
		<td class="l">Multa</td>
		<td class="l" colspan=2>
			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;
			<font class="l">Euro</font>
		</td>
	</tr>
<%
   	}
	if (penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0) {

	} else {
%>
	<tr>
	   	<td class="l" >Arresto</td>
	   	<td class="l" colspan=2>
	      	<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
		</td>
		<td class="l">Ammenda</td>
		<td class="l" colspan=2>
			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;
			<font class="l">Euro</font>
		</td>
	</tr>
<%
	}
} // CHIUDO if(penaresidua...)
%>
	<tr>
<%
if (penaresidua.getDataInizio() != null) {
%>
		<td class="l">Data Decorrenza Pena</td>
		<td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
<%
}
if (penaresidua.getFlagErgastolo() != null) {
	if (penaresidua.getFlagErgastolo().equals("S")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
	} else if (penaresidua.getFlagErgastolo().equals("D")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
	}
}
if  ((penaresidua.getFlagErgastolo() == null)
		|| (penaresidua.getFlagErgastolo() != null
		&& !penaresidua.getFlagErgastolo().equals("S")
		&& !penaresidua.getFlagErgastolo().equals("D"))) {
	if (penaresidua.getDataFine() != null) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
		  	<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
		</td>

<%
	}
}
%>
	</tr>
</table>

<!-- Misure di Sicurewzza -->
<%	
if (VecMisureSic!=null && VecMisureSic.size() > 0) {
	Iterator Itx3 = VecMisureSic.iterator();	%>
<table>
<%
	while (Itx3.hasNext()) {
		nuovaMis = (MisuraSicurezzaModel) Itx3.next();
%>
	<tr>
		<td class=C>Misura di Sicurezza da espiare</td>
	  	<td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getDescrTipo())%>&nbsp;</font></td>
		<td class=C> Anni</td>
		<td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getNumAnni(),"0")%>&nbsp;</font></td>
		<td class=C> Mesi</td>
		<td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getNumMesi(),"0")%>&nbsp;</font></td>
		<td class=C> Giorni</td>
		<td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getNumGiorni(),"0")%>&nbsp;</font></td>	
	</tr>
<%
	}
%>
</table>
<%
} else if (listaMisure != null && listaMisure.size() > 0) {
	Iterator Itx4 = listaMisure.iterator();
%>
<table>
<%
	while (Itx4.hasNext()) {
		nuovaMis = (MisuraSicurezzaModel) Itx4.next();
%>
	<tr>
		<td class=C>Misura di Sicurezza da espiare</td>
	  	<td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getDescrTipo())%>&nbsp;</font></td>
		<td class=C> Anni</td>
		<td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getNumAnni(),"0")%>&nbsp;</font></td>
		<td class=C> Mesi</td>
		<td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getNumMesi(),"0")%>&nbsp;</font></td>
		<td class=C> Giorni</td>
		<td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getNumGiorni(),"0")%>&nbsp;</font></td>	
	</tr>
<%
	}
%>
</table>
<%
} else {
%>
<table><tr><td class="l">Fascicolo privo di Misure di Sicurezza</td></tr></table>
<%
}
%>
<br>
<table>
	<tr>
		<td class="l">Data Ricezione</td>
		<td class="L" colspan=1>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
		</td>
	</tr>
 	<!--Provvedimenti Sorveglianza -->  
	<tr>
	   	<td class="l" width="20%">Tipo Provvedimento </td>
	   	<td class="L" colspan=1 width="60%">
	   		<font class="campo">
	   			<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%>
			</font>
		</td>
	</tr>
	<tr>
		<td class="l" width="20%">Anno / Numero SIUS </td>
		<td class="L" colspan=1 width="60%">
			<font class="campo">
				<%=StringUtils.toStringJSP(provvedimento.getFascicoloSiusModel().getChiaveAnno())%>
				/
				<%=StringUtils.toStringJSP(provvedimento.getFascicoloSiusModel().getChiaveProgr())%>
			</font>
		</td>
	</tr>
<%
if (provvedimento.getEvento().getCodTipoProvvedimento().compareTo("02") == 0) {
	tipoProvv = " Decreto ";
	annoS3S72 = StringUtils.toStringJSP(provvedimento.getDecreto().getAnnoS72()); 
	numS3S72 =  StringUtils.toStringJSP(provvedimento.getDecreto().getNumS72()); 
}
if (provvedimento.getEvento().getCodTipoProvvedimento().compareTo("03") == 0) {
	tipoProvv = " Ordinanza ";
	annoS3S72 = StringUtils.toStringJSP(provvedimento.getOrdinanza().getAnnoS3()); 
	numS3S72 =  StringUtils.toStringJSP(provvedimento.getOrdinanza().getNumS3());
}
%>        	
	<tr>
		<td class="l" width="20%">Anno / Numero <%=tipoProvv%> </td>
		<td class="L" colspan=1 width="60%">
			<font class="campo">
				<%=annoS3S72 %>
				/
				<%=numS3S72%>
			</font>
		</td>
	</tr>    
	<tr>
		<td class="l" width="20%">Ufficio Emittente</td>
		<td class="L" colspan=1 width="60%">
			<font class="campo">
				<%=provvedimento.getDescrTipoUfficio()+" di "+provvedimento.getDescrComuneUfficio() %>
			</font>
		</td>
	</tr>  
    <tr>
    	<td class="l" width="20%">Data Emissione <%=tipoProvv%></td>
    	<td class="L" colspan=1 width="60%">
    		<font class="campo">
    			<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getEvento().getDataEmissione(),"dd-MM-yyyy")) %>
    		</font>
    	</td>
    </tr>
    <tr>
    	<td class="l" width="20%">Oggetto Provvedimento</td>
    	<td class="L" colspan=1 width="60%">
    		<font class="campo">
    			<%=provvedimento.getDescrOggetto() %>
    		</font>
    	</td>
    </tr>
    <tr>
    	<td class="l" width="20%">Esito Provvedimento</td>
    	<td class="L" colspan=1 width="60%">
    		<font class="campo">
    			<%=provvedimento.getDescrEsito() %>
    		</font>
    	</td>
    </tr>                           	
</table>
<!-- Menù di prosecuzione Esecuzione misura - Solo dopo Validazione Provvedimento -->	
<%
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
		&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
%>
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
		<td width="32%" class="menulines" nowrap>
		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
			=siap.siep.misurasicurezza.action.ActLoadInserisciOEInternamento">Ordine Esecuzione per Internamento
		</a>
		</td>
		<td width="32%" class="menulines" nowrap>
		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
			=siap.siep.misurasicurezza.action.ActLoadInserisciOrdineLiberazione">Ordine di Liberazione
		</a>
		</td>
		<td width="32%" class="menulines" nowrap>
		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
			=siap.siep.misurasicurezza.action.ActLoadInserisciRichiestaDAP">Richiesta al D.A.P.
		</a>
		</td>
	</tr>
	<tr>
		<%-- MEV_39: aggiunta funzionalità --%>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciOLDifferimento&from=dads&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventonotifica.getEvento().getIdEvento()%>">Differimento Misura Sicurezza
			</a>
		</td>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciArchiviazionePerProvvSorveglianza">Archiviazione per Provvedimento Sorveglianza
			</a>
		</td>
   	</tr>
</table>
<%
}
%>
<!-- Bottone di Conferma, in DIV perchè deve cambiare posizione -->
<br>
<div align=left style="visibility:hidden" id="upld" >
<FORM name="DettaglioAnnotaDec" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
<table>
	<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
	<tr>
		<td class="L">
			<input class="bottone" type="submit" value="Conferma">
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"             value="siap.siep.misurasicurezza.action.ActUploadAnnotazioneDecisioneDellaSorveglianza">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"        value="<%= eventonotifica.getEvento().getIdEvento() %>">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActDettaglioAnnotazioneDecisioneDellaSorveglianza">
		</td>
	</tr>
</table>
</FORM>
</div>
<br>
<br>
</body>
</html>