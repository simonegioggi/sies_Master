<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- @since MEV_2026-1 --%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.lang.String"%>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.scadenzario.action.ICostantiScadenzarioSius"%>
<%@ page import="siap.sius.scadenzario.model.ScadenzarioSiusModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="scadenzari"	scope="request" class="java.util.Vector"/>
<jsp:useBean id="intestazione" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"		scope="request" class="java.lang.String"/>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>[S.I.E.S.] - Consultazione Scadenzario Ricerca Fine Pena Procedimenti Pendenti</title>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>

<%
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<BODY class="corpo">
<FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
<table>
  	<tr>
  		<td class="LBG">
  			<a href="Javascript:window.print();">
  				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
  			</a>
  		</td>
    	<td class="LBG">
    		<font class="label">Funzione :</font>&nbsp;
    		<font class="campo">Elenco Procedimenti Pendenti con Data Inizio e Fine Pena</font>
    	</td>
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
		<!-- BOTTONE PER STAMPA EXCEL --> 
		<td class=l>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.statistiche.action.ActRicercaFinePenaProcedimentiPendentiExcel">
				<img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
			</a>
		</td>
	</tr>
</table>
<br>
<table cellpadding="2" cellspacing="2">
	<tr><td class="Titolo"><%=intestazione%></td></tr>
</table>
<br>
<table cellpadding="2" cellspacing="2">
<%
if (scadenzari.size() == 0) {
%>
	<tr><td class="Titolo" colspan="12">Nessun procedimento trovato con i criteri di ricerca selezionati</td></tr>
<%
} else {
%>
	<tr>
		<td class="int">Procedimento SIUS</td>
		<td class="int">Soggetto</td>
		<td class="int">Luogo Nascita</td>
		<td class="int">Data Nascita</td>
		<td class="int">Posizione Giuridica</td>
		<td class="int">Contenuto</td>
		<td class="int">Data Inizio Pena</td>
		<td class="int">Data Fine Pena</td>
		<td class="int">Giorni Residui</td>
		<td class="int">Fine Pena Virtuale</td>
		<td class="int">Giorni Residui</td>
		<td class="int">Procedimento SIEP</td>
	</tr>
<%
	Iterator itx = scadenzari.iterator();
	while (itx.hasNext()) {
		ScadenzarioSiusModel ssm = (ScadenzarioSiusModel) itx.next();
	  	FascicoloSiusModel fsium = ssm.getFascicoloSius();
	  	RiferimentoFascicoloSiepModel rfsm = ssm.getFascicoloSiep();
		SoggettoModel sm = fsium.getSoggetto();
%>
	<tr>
		<td class="crosso">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fsium.getIdFascicoloSius()%><%=retParam%>" title="Procedimento SIUS">
				<%=fsium.getChiaveAnno()%>/<%=fsium.getChiaveProgr()%>&nbsp;
			</a>
		</td>
		<td class="C"><%=sm.getCognome()%></td>
		<td class="C"><%=sm.getNome()%></td>
		<td class="C"><%=sm.getDescrComuneNascita()%></td>
		<td class="C"><%=DateUtils.getDateToString(sm.getDataNascita(),"dd/MM/yyyy") %></td>
		<td class="C"><%=ssm.getEvento().getDescrTipoProvvedimento()%></td>
		<td class="C"><%=DateUtils.getDateToString(ssm.getEvento().getDataEmissione(),"dd/MM/yyyy")%></td>
		<td class="C"><%=DateUtils.getDateToString(ssm.getDataInizioScadenza(),"dd/MM/yyyy") %></td>
		<td class="C"><%=DateUtils.getDateToString(ssm.getDataFineScadenza(),"dd/MM/yyyy") %></td>
		<td class="C"><%=ssm.getGiorniResidui().intValue()%></td>
		<td class="crosso">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=rfsm.getIdRiferimentoFascicoloSiep()%><%=retParam%>" title="Procedimento SIEP">
				<%=rfsm.getAnnoFascicoloSiep()%>/<%=rfsm.getProgrFascicoloSiep()%>
			</a>
		</td>
	</tr>
<%
	}
}
%>
</table>
</FORM>
</body>
</html>