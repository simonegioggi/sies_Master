<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina per gestione OE per Differimento MS --%>
<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>
<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="listaOrd" scope="request" class="java.util.Vector"/>

<html>
	<head>
    	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    	<title>[S.I.E.S.] - Ricerca Provvedimenti Differimento della Sorveglianza</title>
    	<script language="JavaScript" src="/html/conferma.js"></script>
    	<script language="JavaScript">
      	function insertIT(idFascicoloSius, chiaveAnno, chiaveProgr, chiaveUfficio, codLuogoEmittente, codUfficioEmittente, descrTipoUfficio, descrComuneUfficio,
      			codTipoProvvedimento, codMotivo, giornoDataEmissione, meseDataEmissione, annoDataEmissione, idEvento, codEsito, annoProtocollo, progrProtocollo,
      			descrOggetto, descrEsito, descrProvvedimento, giornoDataInizioPeriodo, meseDataInizioPeriodo, annoDataInizioPeriodo, giornoDataFineMisura,
      			meseDataFineMisura, annoDataFineMisura, numAnniSospensione, numMesiSospensione, numGiorniSospensione, flagDecisioneTribunale, luogoSvolgimentoProva) {
    	  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>.value = idFascicoloSius;
    	  	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>.value = chiaveAnno;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>.value = chiaveProgr;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.value = chiaveUfficio;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value = codLuogoEmittente;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.value = codUfficioEmittente;
			window.parent.opener.document.<%=request.getParameter("formname")%>.descrTipoUfficio.value = descrTipoUfficio;
			window.parent.opener.document.<%=request.getParameter("formname")%>.descrComuneUfficio.value = descrComuneUfficio;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value = codTipoProvvedimento;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value = codMotivo;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value = giornoDataEmissione;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value = meseDataEmissione;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>.value = annoDataEmissione;
			window.parent.opener.document.<%=request.getParameter("formname")%>.idEventoFascSius.value = idEvento;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_ESITO%>.value = codEsito;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value = annoProtocollo;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.value = progrProtocollo;
			window.parent.opener.document.<%=request.getParameter("formname")%>.descrOggetto.value = descrOggetto;
			window.parent.opener.document.<%=request.getParameter("formname")%>.descrEsito.value = descrEsito;
			window.parent.opener.document.<%=request.getParameter("formname")%>.descrProvvedimento.value = descrProvvedimento;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value = giornoDataInizioPeriodo;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value = meseDataInizioPeriodo;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value = annoDataInizioPeriodo;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value = giornoDataFineMisura;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value = meseDataFineMisura;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value = annoDataFineMisura;
			window.parent.opener.document.<%=request.getParameter("formname")%>.numAnniSospensione.value = numAnniSospensione;
			window.parent.opener.document.<%=request.getParameter("formname")%>.numMesiSospensione.value = numMesiSospensione;
			window.parent.opener.document.<%=request.getParameter("formname")%>.numGiorniSospensione.value = numGiorniSospensione;
			window.parent.opener.document.<%=request.getParameter("formname")%>.flagDecisioneTribunale.checked = flagDecisioneTribunale == "S" ? true : false;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value = luogoSvolgimentoProva;
			if (giornoDataInizioPeriodo != "" && giornoDataInizioPeriodo != null
					&& meseDataInizioPeriodo != "" && meseDataInizioPeriodo != null
					&& annoDataInizioPeriodo != "" && annoDataInizioPeriodo != null) {
				window.parent.opener.document.<%=request.getParameter("formname")%>.eseguita[0].checked = false;
				window.parent.opener.document.<%=request.getParameter("formname")%>.eseguita[1].checked = true;
				window.parent.opener.document.getElementById("rigaDataDiff").style.display = "block";
				window.parent.opener.document.getElementById("rigaScarcerazione").style.display = "none";
			} else {
				window.parent.opener.document.<%=request.getParameter("formname")%>.eseguita[0].checked = true;
				window.parent.opener.document.<%=request.getParameter("formname")%>.eseguita[1].checked = false;
				window.parent.opener.document.getElementById("rigaDataDiff").style.display = "none";
				window.parent.opener.document.getElementById("rigaScarcerazione").style.display = "block";
			}
			window.parent.close();
		}

      	function controlla() {
      		if (document.elenco.numeroDifferimenti.value == 0) {
      			alert("Nessun Provvedimento Presente");
		        window.parent.close();
	        }
      	}
    	</script>
	</head>

  	<body class="corpo" onload="controlla();">
  		<form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  			<table width="100%">
    			<input type="hidden" name="numeroDifferimenti" value="<%=listaOrd.size()%>">
      			<tr>
        			<td class="LBG">
          				<a href="Javascript:window.print();">
          					<center>
          						<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
       						</center>
          				</a>
        			</td>
        			<td class="LBG">
        				<center>
	          				<font class=label>Funzione:</font>
	          				<font class=campo>Elenco Provvedimenti della Sorveglianza</font>
	          			</center>
        			</td>
      			</tr>
    		</table>
    		<br>
     		<!--Ordinanze Sorveglianza -->  
   			<table align="center" cellspacing="2" cellpadding="2" width="100%">
   				<tr><td class="Titolo" colspan="7"> Dati ordinanza Ufficio di Sorveglianza</td></tr>
    			<tr>
					<td class="int" width="10%">Anno / Numero SIUS</td>
					<td class="int" width="10%">Anno / Numero Provv</td>
					<td class="int" width="20%">Autorità emittente</td>
					<td class="int" width="10%">Data</td>
					<td class="int" width="20%">Oggetto</td>
					<td class="int" width="25%">Esito</td>
					<td class="int" width="5%">Azioni</td>
    			</tr>
<%
int idR = 0;
Iterator Itx1 = listaOrd.iterator();
while (Itx1.hasNext()) {
	idR = idR + 1;
	OrdinanzaEventoTenoriFascicoloSiusModel lOrdMod = (OrdinanzaEventoTenoriFascicoloSiusModel) Itx1.next();
%>
				<tr>
					<td class="l"><%=StringUtils.toStringJSP(lOrdMod.getFascicoloSiusModel().getChiaveAnno())%>
						/
						<%=StringUtils.toStringJSP(lOrdMod.getFascicoloSiusModel().getChiaveProgr())%>
					</td>
					<td class="l"><%=StringUtils.toStringJSP(lOrdMod.getEvento().getAnnoProtocollo() ) %>
						/
						<%=StringUtils.toStringJSP(lOrdMod.getEvento().getProgrProtocollo()) %>
					</td>
					<td class="l"><%=lOrdMod.getDescrTipoUfficio()+" di "+lOrdMod.getDescrComuneUfficio() %></td>
					<td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getEvento().getDataEmissione(),"dd-MM-yyyy")) %></td>
					<td class="l"><%=lOrdMod.getDescrOggetto() %></td>
					<td class="l"><%=lOrdMod.getDescrEsito() %></td>
					<td class="c">
	         			<a href="Javascript:insertIT(
							'<%=lOrdMod.getFascicoloSiusModel().getIdFascicoloSius()%>',
							'<%=StringUtils.toStringJSP(lOrdMod.getFascicoloSiusModel().getChiaveAnno(),"")%>',
							'<%=StringUtils.toStringJSP(lOrdMod.getFascicoloSiusModel().getChiaveProgr(),"")%>',
							'<%=StringUtils.toStringJSP(lOrdMod.getFascicoloSiusModel().getChiaveUfficio(),"")%>',
							'<%=StringUtils.toStringJSP(lOrdMod.getEvento().getCodLuogoEmittente(),"")%>',
							'<%=StringUtils.toStringJSP(lOrdMod.getEvento().getCodTipoUfficioEmittente(),"")%>',
							'<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lOrdMod.getDescrTipoUfficio()),"")%>',
							'<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lOrdMod.getDescrComuneUfficio()),"")%>',
							'<%=StringUtils.toStringJSP(lOrdMod.getEvento().getCodTipoProvvedimento(),"")%>',
							'<%=StringUtils.toStringJSP(lOrdMod.getEvento().getCodMotivo(),"")%>',
							'<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getEvento().getDataEmissione(),"dd"))%>',
							'<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getEvento().getDataEmissione(),"MM"))%>',
							'<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getEvento().getDataEmissione(),"yyyy"))%>',
							'<%=lOrdMod.getEvento().getIdEvento()%>',
							'<%=StringUtils.toStringJSP(lOrdMod.getEvento().getCodEsito(),"")%>',
							'<%=StringUtils.toStringJSP(lOrdMod.getEvento().getAnnoProtocollo(),"")%>',
							'<%=StringUtils.toStringJSP(lOrdMod.getEvento().getProgrProtocollo(),"")%>',
							'<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lOrdMod.getDescrOggetto()),"")%>',
							'<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lOrdMod.getDescrEsito()),"")%>',
							'<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lOrdMod.getDescrProvvedimento()),"")%>',
							'<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getOrdinanza().getDataInizioPeriodo(),"dd"))%>',
							'<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getOrdinanza().getDataInizioPeriodo(),"MM"))%>',
							'<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getOrdinanza().getDataInizioPeriodo(),"yyyy"))%>',
							'<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getOrdinanza().getDataFineMisura(),"dd"))%>',
							'<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getOrdinanza().getDataFineMisura(),"MM"))%>',
							'<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getOrdinanza().getDataFineMisura(),"yyyy"))%>',
							'<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(lOrdMod.getOrdinanza().getSospensioneAASS()),"")%>',
							'<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(lOrdMod.getOrdinanza().getSospensioneMMSS()),"")%>',
							'<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(lOrdMod.getOrdinanza().getSospensioneGGSS()),"")%>',
							'<%=StringUtils.toStringJSP(new String(""))%>',
							'<%=StringUtils.cStrForJS(lOrdMod.getOrdinanza().getLuogoSvolgimentoProva())%>'
						);">
							<img align="middle" src="/images/fileselected.gif" border="0">
						</a>
					</td>
				</tr>
<% } %>
    		</table>
  		</form>
  	</body>
</html>