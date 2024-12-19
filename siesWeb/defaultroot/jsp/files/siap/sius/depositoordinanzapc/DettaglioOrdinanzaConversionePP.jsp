<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneEstesaModel"%>
<%@ page import="siap.siep.rateizzazionepp.model.RateizzazionePPModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>
<%@ page import="siap.sius.penapecuniaria.action.ICostantiSiusPenaPecuniaria"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>

<%@ page import="java.util.Iterator"%>

<jsp:useBean id="richiesteconversioni" 	scope="request" class="java.util.Vector"/>
<jsp:useBean id="datiOrdinanza" 		scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<%-- MEV_2023-35: aggiunti useBean e gestiti nella pagina --%>
<jsp:useBean id="rate" 					scope="request" class="java.util.Vector<siap.siep.rateizzazionepp.model.RateizzazionePPModel>"/>
<jsp:useBean id="dopm" 					scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"/>

<table cellspacing="2" cellpadding="2" width="90%">
<%
TenoreModel[] tenori = datiOrdinanza.getTenori(); 
Iterator itx = richiesteconversioni.iterator();
while (itx.hasNext()) {
	RichiestaConversioneEstesaModel lRicConEstesa = (RichiestaConversioneEstesaModel) itx.next();
	String lTitoloRichiestaCPP = ""; 
	if (lRicConEstesa.getRichiestaConversione().getFasSieIdFascicoloSiep() != null
			&& lRicConEstesa.getRichiestaConversione().getFasSiuIdFascicoloSius() == null) 
		lTitoloRichiestaCPP = "Pena Pecuniaria relativa al N. SIEP " + lRicConEstesa.getFasSiep().getChiaveAnno() + " / " + lRicConEstesa.getFasSiep().getChiaveProgr();
	else
		lTitoloRichiestaCPP = "Pena Pecuniaria inserita dall'UDS";
	// Distinzione tra "Conversione e Rateizzazione"
	if (tenori[0].getCodOggettoTenore().compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_CONVERSIONE) == 0) {
%>
	<tr>
  		<td class="Titolo" width="100%" colspan="6" ><%=lTitoloRichiestaCPP%></td>
	</tr>
	<tr>
		<td rowspan="3" width="20%" class="l"> 
			<font class="label"> Multa</font>&nbsp;
<%
		if (lRicConEstesa.getRichiestaConversione().getImportoMulta() != null) {
%>
			<font class="campo"><%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoMulta())%></font><br>
<%
		}
%>
			<br>
			<font class="label"> Ammenda</font>&nbsp;
<%
		if (lRicConEstesa.getRichiestaConversione().getImportoAmmenda() != null) {
%>
			<font class="campo"><%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoAmmenda())%></font>&nbsp;
<%
		}
%>
		</td>
  		<td rowspan="3" width="80%" class="l"> 
   			<font class="label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; C&nbsp;&nbsp;  o&nbsp;&nbsp;  n&nbsp;&nbsp;  v&nbsp;&nbsp;  e&nbsp;&nbsp;  r&nbsp;&nbsp;  t&nbsp;&nbsp;  i&nbsp;&nbsp;  t&nbsp;&nbsp;  a&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       i&nbsp;&nbsp;  n &nbsp;<br><br></font>
			<font class="label">&nbsp;Anni: </font>
			<font class="crosso"> <%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getDurataEsitoAnni())%></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<font class="label">&nbsp;Mesi: </font>
			<font class="crosso"> <%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getDurataEsitoMesi())%></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<font class="label">&nbsp;Giorni: </font>
			<font class="crosso"> <%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getDurataEsitoGiorni())%></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<font class="label"> di&nbsp;&nbsp; </font>
<%
		if (lRicConEstesa.getRichiestaConversione().getCodTipoSanzione().compareTo("01") == 0) {
%>
			<font class="crosso">Libertà controllata </font>
<%
		} else if (lRicConEstesa.getRichiestaConversione().getCodTipoSanzione().compareTo("02") == 0) {
%>
			<font class="crosso"> Lavoro sostitutivo </font>
<%
		} else if (lRicConEstesa.getRichiestaConversione().getCodTipoSanzione().compareTo("03") == 0) {
%>
			<font class="crosso"> Rateizzazione Pena Pecuniaria </font>
<%
		}
%>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%
	} else if (tenori[0].getCodOggettoTenore().trim().compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_RATEIZZAZIONE) == 0) {
%>
	<tr>
		<td class="Titolo" colspan="6"> <%=lTitoloRichiestaCPP%></td>
	</tr>
	<tr>
		<td rowspan="3" width="20%" class="l"> 
			<font class="label"> Multa</font>&nbsp;
<%
		if (lRicConEstesa.getRichiestaConversione().getImportoMulta() != null) { %>
			<font class="campo"> <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoMulta())%></font>&nbsp;&nbsp;&nbsp;
<%
		}
%>
			<br>
			<font class="label"> Ammenda </font>&nbsp;
<%
		if (lRicConEstesa.getRichiestaConversione().getImportoAmmenda() != null) {
%>
			<font class="campo"> <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoAmmenda())%></font>&nbsp;
<%		}
%>
		</td>
		<td rowspan="3" class="l"> 
     		<font class="label">Rateizzati in  </font>
     		<font class="crosso"> <%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getNumeroRate())%></font>
			<font class="label">&nbsp;rate da &nbsp;&nbsp;</font>
			<font class="cRosso">&nbsp;&euro;<%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getValoreRata())%></font>&nbsp;
			<font class="label">&nbsp;&nbsp;e&nbsp; </font> <font class="cRosso">1</font>&nbsp; <font class="label">rata finale da&nbsp; </font> 
			<font class="crosso">&euro; &nbsp; <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getValoreUltimaRata())%></font>
		</td>
	</tr>
<%
	}
}
// MEV_2023-35: aggiunto controllo
if (tenori[0].getDescrOggettoTenore().trim().compareTo("Rateizzazione pena pecuniaria") == 0
		&& !richiesteconversioni.isEmpty()) {
	RichiestaConversioneEstesaModel lRicConEstesa = (RichiestaConversioneEstesaModel) richiesteconversioni.get(0);
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
  		<td class="l"> 
  			<font class="label"> Termine pagamento 1° Rata : </font>&nbsp;
		</td>
   		<td class="l"> 
        	<font class="label">entro il </font>
        	<font class="crosso">
<%
	if (lRicConEstesa.getRichiestaConversione().getDataInizioPagamento() != null) {
%>
			<%=DateUtils.getDateToString(lRicConEstesa.getRichiestaConversione().getDataInizioPagamento(), "dd-MM-yyyy")%>
<%
	} else {
%>
			 - 
<%
	}
%>
			</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;            	 	
<%
	if (lRicConEstesa.getRichiestaConversione().getNumeroGiorniInizioPagamento() !=null
			&& lRicConEstesa.getRichiestaConversione().getNumeroGiorniInizioPagamento().intValue() > 0)	{
%>
			<font class="label"> oppure entro &nbsp; </font>
			<font class="crosso">
				<%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getNumeroGiorniInizioPagamento())%> 
			</font>&nbsp;&nbsp;
			<font class="label"> giorni dalla data di notifica </font>
<%
	}
%>            	 	
	    </td>
	<tr>
<%
}

// MEV_2023-35: aggiunte rate per U142, U143, U145
if ("0159".equals(tenori[0].getCodEsitoTenore()) && !Utils.isNullObj(rate) && !rate.isEmpty()) {
	Iterator<RateizzazionePPModel> itRate = rate.iterator();
	int i = 0;
	while (itRate.hasNext()) {
		RateizzazionePPModel rppm = (RateizzazionePPModel) itRate.next();
		if (i == 0) {
%>
	<tr>
		<td class="Titolo" colspan="2">Pena Pecuniaria inserita dall'UDS</td>
	</tr>
	<tr>
		<td width="20%" class="l">
			<font class="label">Totale Importo Rateizzato:</font>
			<font class="crosso">&euro;&nbsp;<%=StringUtils.toEuroFormat(rppm.getImportoDaPagare())%></font>
			<font class="label">&nbsp;in:</font>
		</td>
	</tr>
<%
		}
		i++;
%>
	<tr>
		<td class="l">
     		<font class="label">Progressivo Rata: <%=StringUtils.toStringJSP(rppm.getProgressivoRata())%></font>
     		<font class="crosso">&nbsp;&nbsp;&nbsp;Numero Rate: <%=StringUtils.toStringJSP(rppm.getNumeroRate())%></font>
			<font class="label">da</font>
			<font class="cRosso">&euro;&nbsp;<%=StringUtils.toEuroFormat(rppm.getImportoRata())%></font>
		</td>
	</tr>
<%
	}
} else if ("0276".equals(tenori[0].getCodEsitoTenore()) || "0277".equals(tenori[0].getCodEsitoTenore())
		|| "0278".equals(tenori[0].getCodEsitoTenore()) || "0279".equals(tenori[0].getCodEsitoTenore())
		|| "0281".equals(tenori[0].getCodEsitoTenore())) {
	String descrTipoSanzione = "01".equals(dopm.getCodTipoSanzione()) ? "Semilibert&agrave;" :
		 "02".equals(dopm.getCodTipoSanzione()) ? "Detenzione Domiciliare" : 
			 "03".equals(dopm.getCodTipoSanzione()) ? "Lavoro Pubblica Utilit&agrave;" : "Permanenza Domiciliare";
%>
	<tr>
		<td class="Titolo" colspan="2">Pena Pecuniaria inserita dall'UDS</td>
	</tr>
	<tr>
		<td width="20%" class="l">
			<font class="label">Totale Importo Convertito:</font>
			<font class="crosso">&nbsp;&euro;&nbsp;<%=StringUtils.toEuroFormat(dopm.getSommaRisarcimento())%></font><br>
			<font class="label">Quantum Pena Sostitutiva:</font>
			<font class="cRosso">&nbsp;ANNI: <%=StringUtils.toStringJSP(dopm.getNumAnniDetenzioneDom(), "0")%></font>
			<font class="cRosso">&nbsp;MESI: <%=StringUtils.toStringJSP(dopm.getNumMesiDetenzioneDom(), "0")%></font>
			<font class="cRosso">&nbsp;GIORNI: <%=StringUtils.toStringJSP(dopm.getNumGiorniDetenzioneDom(), "0")%></font>
			<font class="label">di</font>
			<font class="cRosso">&nbsp;<%=StringUtils.toStringJSP(descrTipoSanzione)%></font>
		</td>
	</tr>
<%
}
%>
	<tr><td>&nbsp;</td></tr>
</table>