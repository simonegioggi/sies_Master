<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina --%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario"%>
<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel"%>
<%@ page import="siap.siep.scadenzario.util.ScadenzarioUtils"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>

<jsp:useBean id="tipo"				scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo"			scope="request" class="java.lang.String"/>
<jsp:useBean id="giorniScadenza"	scope="request" class="java.lang.String"/>
<jsp:useBean id="mesiScadenza"		scope="request" class="java.lang.String"/>
<jsp:useBean id="anniScadenza"		scope="request" class="java.lang.String"/>

<html>
  	<head>
    	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    	<title>[S.I.E.S.] - Consultazione Scadenzario Differimento Misure Sicurezza</title>
    	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  	</head>

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
        			<font class="label">Funzione:</font>&nbsp;
        			<font class="campo">Consultazione Scadenzario Differimento Misure Sicurezza - <%=titolo%></font>
        		</td>
        		<td class="LBG">
            		<a href="javascript:history.go(-1);">
<%-- 					<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActLoadRicercaScadenzarioDifferimentoMisureSicurezza&tipo=<%=tipo%>&giorniScadenza=<%=giorniScadenza%>&mesiScadenza=<%=mesiScadenza%>&anniScadenza=<%=anniScadenza%>"> --%>
             			<img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            		</a>
          		</td>
          		<td class="LBG">
            		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActStampaScadenzarioDifferimentoMS&tipo=<%=tipo%>&giorniScadenza=<%=giorniScadenza%>&mesiScadenza=<%=mesiScadenza%>&anniScadenza=<%=anniScadenza%>">
             			<img align="middle" src="/images/xls.jpg" alt="scarica in excel" width="24" height="24" border="0">
            		</a>
          		</td>
      		</tr>
    	</table>
    	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
    	<table cellspacing="2" cellpadding="2" width="100%">
<%
List scadenzario = (List) request.getAttribute("scadenzario");
if ("Tutti".equals(tipo)) {
%>
	<tr>
        <td class="campo"><img src="/images/QuadratinoVerde.gif">&nbsp;
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActRicercaScadenzarioDifferimentoMisureSicurezza&tipo=sette&<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>=<%=new BigDecimal(7)%>">In Scadenza</a>
        </td>
        <td class="campo"><img src="/images/QuadratinoRosso.gif">&nbsp;
        	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActRicercaScadenzarioDifferimentoMisureSicurezza&tipo=oggi">In Scadenza Oggi</a>
        </td>
        <td class="campo"><img src="/images/QuadratinoGrigio.gif">&nbsp;
        	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActRicercaScadenzarioDifferimentoMisureSicurezza&tipo=scaduto">Scaduto</a>
        </td>
        <td>&nbsp;<td>
        <td>&nbsp;<td>
        <td>&nbsp;<td>
        <td>&nbsp;<td>
        <td>&nbsp;<td>
	</tr>
<%
}
%>
	<tr>
        <td class="int" width="15%">N° SIEP</td>
        <td class="int" width="15%">Cognome</td>
        <td class="int" width="15%">Nome</td>
        <td class="int" width="15%">Data Inizio Differimento</td>
        <td class="int" width="15%">Data Fine Differimento</td>
        <td class="int" width="15%">N° Giorni Residui</td>
		<td class="int" width="5%">Visto</td>
		<td class="int">Azioni</td>
	</tr>
<%
//==========================================================================
// Lista elementi trovati
//==========================================================================
Iterator itx = scadenzario.iterator();
while ( itx.hasNext()) {
  	ScadenzarioModel   lSca = (ScadenzarioModel) itx.next();
  	FascicoloSiepModel lFas = lSca.getFascicoloModel();
  	SoggettoModel      lSog = lFas.getSoggetto();
%>
    		<tr>
<%
	if ("Tutti".equals(tipo)) {
      	// In Scadenza Oggi
      	if (lSca.getGiorniResidui() != null && lSca.getGiorniResidui().intValue() == 0) {
%>
        		<td class="crosso">
         			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
          				<%=lFas.getChiaveAnno()%>
          				/
          				<%=lFas.getChiaveProgr()%>
         			</a>
       			</td>
		        <td class="crosso"><%=StringUtils.toStringJSP(lSog.getCognome())%></td>
		        <td class="crosso"><%=StringUtils.toStringJSP(lSog.getNome())%></td>
		        <td class="crosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy"))%></td>
		        <td class="crosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy")) %></td>
		        <td class="crosso" nowrap><%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(), DateUtils.getSysDate())%></td>
<%
		} else {
        // In scadenza (<7gg)
        	if (lSca.getGiorniResidui() != null
        		&& lSca.getGiorniResidui().intValue() <= 7
           		&& lSca.getGiorniResidui().intValue() > 0) {
%>
          		<td class="cverde">
            		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
              			<%=lFas.getChiaveAnno()%>
		              	/
		              	<%=lFas.getChiaveProgr()%>
            		</a>
          		</td>
				<td class="cverde"><%=StringUtils.toStringJSP(lSog.getCognome())%></td>
				<td class="cverde"><%=StringUtils.toStringJSP(lSog.getNome())%></td>
				<td class="cverde"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy"))%></td>
				<td class="cverde"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy"))%></td>
				<td class="cverde" nowrap><%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(), DateUtils.getSysDate())%></td>
<%
        	} else {
          		// Scaduti
          		if (lSca.getGiorniResidui() != null && lSca.getGiorniResidui().intValue() < 0) {
%>
            	<td class="cgrigio">
              		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
                		<%=lFas.getChiaveAnno()%>
                		/
                		<%=lFas.getChiaveProgr()%>
              		</a>
           		</td>
	            <td class="cgrigio"><%=StringUtils.toStringJSP(lSog.getCognome())%></td>
	            <td class="cgrigio"><%=StringUtils.toStringJSP(lSog.getNome())%></td>
	            <td class="cgrigio"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy"))%></td>
	            <td class="cgrigio"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy"))%></td>
	            <td class="cgrigio" nowrap><%=ScadenzarioUtils.getDifferenza(DateUtils.getSysDate(), lSca.getDataFineScadenza())%></td>
<%
          		} else {
            		// A scadere > 7 gg
%>
            	<td class="C">
              		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
                		<%=lFas.getChiaveAnno()%>
                		/
               			<%=lFas.getChiaveProgr()%>
              		</a>
           		</td>
	            <td class="C"><%=StringUtils.toStringJSP(lSog.getCognome())%></td>
	            <td class="C"><%=StringUtils.toStringJSP(lSog.getNome())%></td>
	            <td class="C"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy"))%></td>
	            <td class="C"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy"))%></td>
<%
					if (lSca != null && lSca.getDataFineScadenza() != null) {
%>
              	<td class="C"><%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(), DateUtils.getSysDate())%></td>
<%
					} else {
%>
              	<td class="C">&nbsp;</td>
<%
					}
				}
        	}
      	}
	} else { // FINE TUTTI
%>
        		<td class="C">
       				<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
            			<%=lFas.getChiaveAnno()%>
            			/
            			<%=lFas.getChiaveProgr()%>
         			</a>
        		</td>
		        <td class="C"><%=StringUtils.toStringJSP(lSog.getCognome())%></td>
		        <td class="C"><%=StringUtils.toStringJSP(lSog.getNome())%></td>
		        <td class="C"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy"))%></td>
          		<td class="C"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy"))%></td>
<%
		// Scaduti
		if (lSca.getGiorniResidui() != null && lSca.getGiorniResidui().intValue() < 0) {
%>
            	<td class="C"><%=ScadenzarioUtils.getDifferenza(DateUtils.getSysDate(),lSca.getDataFineScadenza())%></td>
<%
		// in scadenza
		} else {
%>
				<td class="C"><%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(), DateUtils.getSysDate())%></td>
<%
		}
	}
	if (lSca != null && lSca.getFlagVisto() != null && "S".equals(lSca.getFlagVisto())) {
%>
				<%-- VISTO --%>
        		<td class="C"><img src="/images/TickRed.gif"></td>
<%
	} else {
%>
        		<td class="C">&nbsp;</td>
<%
	}
%>
				<%-- AZIONI --%>
      			<td class="C">
<%--         			<jsp:include page="<%=IWebConstants.PG_BUTTONS%>"> --%>
<%--           				<jsp:param name="CampoIdEntita" value="<%=ICostantiScadenzario.CAMPO_ID_SCADENZARIO%>"/> --%>
<%--           				<jsp:param name="ValoreIdEntita" value="<%=lSca.getIdScadenzario()%>"/> --%>
<%--           				<jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>"/> --%>
<%--           				<jsp:param name="ValoreIdEntitaProvv" value="<%=lFas.getIdFascicoloSiep()%>"/> --%>
<%--         			</jsp:include> --%>
					<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActLoadDettaglioScadenzarioDifferimentoMisureSicurezza&<%=ICostantiScadenzario.CAMPO_ID_SCADENZARIO%>=<%=lSca.getIdScadenzario()%>&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>&tipo=<%=tipo%>&giorniScadenza=<%=giorniScadenza%>&mesiScadenza=<%=mesiScadenza%>&anniScadenza=<%=anniScadenza%>">
                  		<img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                	</a>
      			</td>
    		</tr>
<%
} // END WHILE
%>
    	</table>
	</FORM>
</body>
</html>