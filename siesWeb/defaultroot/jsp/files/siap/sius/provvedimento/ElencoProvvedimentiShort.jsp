<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.evento.model.EventoDepositoModel"%>
<%@ page import="siap.sius.impugnazione.model.ImpugnazioneModel"%>

<jsp:useBean id="provvedimenti"             scope="request" class="java.util.Vector"/>
<jsp:useBean id="provvedimentiFoglioComp"   scope="request" class="java.util.Vector"/>
<jsp:useBean id="estremiFoglioComp"         scope="request" class="java.util.Vector"/>
<jsp:useBean id="impugnazioniEvento"  		scope="request" class="java.util.Hashtable"/>
<jsp:useBean id="opposizioniEvento"   		scope="request" class="java.util.Hashtable"/>
<%-- MEV_9: aggiunti useBean --%>
<jsp:useBean id="fascicoloSiusGP" 			scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="dataEsecutivita" 			scope="request" class="java.util.Date"/>

<html>
<table width="100%">
<%
if (provvedimenti.size() == 0) {
%>
	<tr>
   		<td class="int" align="left">Non ci sono provvedimenti allegati al procedimento.</td>
   	</tr>
<%
} else  {
%>
	<tr align="center">
		<td class="int" nowrap>Data emissione</td>
		<td class="int">Tipo</td>
		<td class="int">Motivo</td>
		<td class="int">Esito</td>
		<td class="int" nowrap>Data Deposito</td>
		<%-- MEV_9: aggiunta data esecutivita e gestita nella pagina solo per C050 e C051 --%>
<%
	if (!Utils.isNullObj(fascicoloSiusGP) && !Utils.isNullObj(fascicoloSiusGP.getGeneraleProcedimentoModel())
			&& !Utils.isNullObj(fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
			&& (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C050") == 0
			|| fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C051") == 0)) {
%>
		<td class="int" nowrap>Data Esecutivita&#768;</td>
<%
	}
%>
		<td class="int">Altre Informazioni</td>
		<td class="int">Provv.<br>Validato</td>
		<td class="int">Deposito<br>Validato</td>
    </tr>
<%
    Iterator itx = provvedimenti.iterator();
    String lEstremiFoglioComp = "";
    int i = 0;
    while (itx.hasNext()) {
      	EventoDepositoModel lProv = (EventoDepositoModel) itx.next();
      	// 04/05/2006 Si stabilisce l'eventuale presenza del Foglio Complementare
      	boolean presenzaFoglio = false;
      	for (int jj = 0; jj < provvedimentiFoglioComp.size(); jj++) {
			BigDecimal lID_EVENTO = (BigDecimal)provvedimentiFoglioComp.get(jj);
        	if (lID_EVENTO.compareTo(lProv.getIdEvento()) == 0) {
				presenzaFoglio = true;
				lEstremiFoglioComp = estremiFoglioComp.get(jj).toString();
				break;
        	}
      	}
		int numImpugnazioni = 0;
		int numOpposizioni  = 0;
		String dateRicorso = "";
		String dateOpposizione = "";
		Vector <ImpugnazioneModel> lListaImpugnazioniEvento = (Vector) impugnazioniEvento.get(lProv.getIdEvento());
		Vector <ImpugnazioneModel> lListaOpposizioniEvento  = (Vector) opposizioniEvento.get(lProv.getIdEvento());
      	if (lListaImpugnazioniEvento != null) {
			numImpugnazioni = lListaImpugnazioniEvento.size();
			dateRicorso = "[";
			for (int k = 0; k< lListaImpugnazioniEvento.size(); k++) {
				if (k>0)
					dateRicorso += ",";
			  	dateRicorso += DateUtils.getDateToString(lListaImpugnazioniEvento.elementAt(k).getDataRicorso(), "dd-MM-yyyy");
			}
        	dateRicorso += "]";
      	}
		if (lListaOpposizioniEvento != null) {
        	numOpposizioni = lListaOpposizioniEvento.size();
        	dateOpposizione = "[";
        	for (int k = 0; k < lListaOpposizioniEvento.size(); k++) {
          		if (k > 0)
          			dateOpposizione += ",";
          		dateOpposizione += DateUtils.getDateToString(lListaOpposizioniEvento.elementAt(k).getDataRicorso(),"dd-MM-yyyy");
        	}
        	dateOpposizione += "]";
      	}
%>
	<tr>
		<td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getDataEmissione(), "dd-MM-yyyy"), "-") %></font></td>
		<td class="c"><font class="campo"><%=StringUtils.toStringJSP(lProv.getDescrTipoProvvedimento(),"-")%></font></td>
		<td class="c"><font class="campo"><%=StringUtils.toStringJSP(lProv.getDescrMotivo(),"-")%></font></td>
		<td class="c"><font class="campo"><%=StringUtils.toStringJSP(lProv.getDescrEsito(),"-")%></font></td>
		<td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getDataDeposito(), "dd-MM-yyyy"), "-")%></font></td>
<%
		if (!Utils.isNullObj(fascicoloSiusGP) && !Utils.isNullObj(fascicoloSiusGP.getGeneraleProcedimentoModel())
				&& !Utils.isNullObj(fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
				&& (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C050") == 0
				|| fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C051") == 0)) {
			if ("0270".equals(lProv.getCodEsito()) && "03".equals(lProv.getCodTipoProvvedimento())) {
%>
		<td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEsecutivita, "dd-MM-yyyy"), "-")%></font></td>
<%
			} else {
%>
		<td class="c"><font class="campo">-</font></td>
<%
			}
		}
%>
		<td class="c">
			<font class="campo">      
<%
		if (numImpugnazioni > 0 || numOpposizioni > 0 || presenzaFoglio) {
			if (numImpugnazioni > 0) {
%>
				<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.impugnazione.action.ActRicercaImpugnazioniDelProvvedimento&IdEvento=<%=lProv.getIdEvento()%>&CodTipoProvvedimento=<%=StringUtils.toStringJSP(lProv.getCodTipoProvvedimento())%>&TornaQui=<%=0%>" title="Data Ricorso : '<%=dateRicorso%>'"> <img src="/images/dettaglioR24.gif" width="22" height="22" border="0"></a>
<%
			}
			if (numOpposizioni > 0) {
%>
              	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.impugnazione.action.ActRicercaOpposizioniDelProvvedimento&IdEvento=<%=lProv.getIdEvento()%>&CodTipoProvvedimento=<%=StringUtils.toStringJSP(lProv.getCodTipoProvvedimento())%>&TornaQui=<%=0%>" title="Data Opposizione : '<%=dateOpposizione%>'"> <img src="/images/dettaglioR24.gif" width="22" height="22" border="0"></a>
<%
			}
			if (presenzaFoglio) {
%>
            	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              	<%-- a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.provvedimento.action.ActLoadInserisciCompFoglioComp&IdEvento=<%=lProv.getIdEvento()%>&CodTipoProvvedimento=<%=StringUtils.toStringJSP(lProv.getCodTipoProvvedimento())%>&TornaQui=<%=0%>" title="Foglio Complementare <%=lEstremiFoglioComp%>"> <img src="/images/dettaglioFC24.gif" width="22" height="22" border="0"></a> --%>
              	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.provvedimento.action.ActLoadDettaglioCompFoglioComp&Provenienza=DettaglioFC&IdEvento=<%=lProv.getIdEvento()%>&CodTipoProvvedimento=<%=StringUtils.toStringJSP(lProv.getCodTipoProvvedimento())%>&TornaQui=<%=0%>" title="Foglio Complementare <%=lEstremiFoglioComp%>">
              		<img src="/images/dettaglioFC24.gif" width="22" height="22" border="0">
              	</a>
<%
			}
		} else {
%>
				-
<%
		}
		if (!Utils.isNullObj(lProv.getEveIdEventoRevoca())) {
		// Provvedimento revocato
%>
            	<font class="cRosso">revocato</font>
<%
		}
%>
        	</font>
		</td>
      	<td class="c">
<%
		if ((lProv.getFlagDocumentoRegistrato() != null) && (lProv.getFlagDocumentoRegistrato().compareTo("S") == 0)) {
			// Provvedimento VALIDATO
%>
			<img src="/images/TickRed.gif">
<%
    	// Provvedimento ANNULLATO
		} else if (lProv.getFlagDocumentoRegistrato() != null && lProv.getFlagDocumentoRegistrato().compareTo("A") == 0) {
%>
			<font class="cRosso">ANNULLATO</font>
<%
		} else {
%>
          	-
<%
		}
%>
		</td>
       	<td class="c">
<%
		if (lProv.getFlagDocumentoRegistrato() != null && lProv.getFlagDocumentoRegistrato().compareTo("S") == 0
				&& lProv.getNumAllValidati() > 0) {
%>
			<img src="/images/TickRed.gif">
<%
		} else {
%>
          	-
<%
		}
%>
		</td>
    </tr>
<%
		i++;
    } // endwhile
}  // endif provvedimenti.size()
%>
</table>
</html>