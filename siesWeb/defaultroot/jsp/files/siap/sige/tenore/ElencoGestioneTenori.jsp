<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeEstesoModel"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>

<jsp:useBean id="ProvvedimentoEvento" scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />
<jsp:useBean id="Modificabile" scope="request" class="java.lang.String" />
<jsp:useBean id="Stampabile" scope="request" class="java.lang.String" />
<jsp:useBean id="Upload" scope="request" class="java.lang.String" />

<html>
<%
	String statoProvvValidato = "N";
	if (ProvvedimentoEvento != null && ProvvedimentoEvento.getEventoNotifica() != null &&
	    ProvvedimentoEvento.getEventoNotifica().getEvento() != null &&
	    ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null &&
	    ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S"))
	{
		statoProvvValidato = "S";
	}

	Vector<TenoreSigeEstesoModel> tenori = (Vector<TenoreSigeEstesoModel>) request.getAttribute("tenoriEstesi");
	BigDecimal idProvvedimento = (BigDecimal) request.getAttribute("idProvvedimentoTitoliEsecutivi");
	String idTenore = (String) request.getParameter("idTenoreSige");
	int rows = 1;

	if (tenori != null && tenori.size() == 0) {
%>
<td class="int" align="left">Non ci sono oggetti assegnati
	all'Atto.</td>
<%
	} else {
		String lIdTenore = "";
		String lCodTenore = "";
		String idSentenza = "";
		
		for (TenoreSigeEstesoModel lTenoreEsteso : tenori) {
			BigDecimal idFascicoloSiep = lTenoreEsteso.getSentenzaSige().getFasSieIdFascicoloSiep();
			
			TenoreSigeModel lTenore = lTenoreEsteso.getTenoreSige();
			boolean isTitoliEsecutivi = false;

			String lmodifica = "NO";
			lIdTenore = lTenore.getIdTenoreSige().toString();

			if (!lTenore.getCodOggettoSige().equalsIgnoreCase(
					lCodTenore)) {
				lCodTenore = lTenore.getCodOggettoSige();
				isTitoliEsecutivi = true;
				rows = calcolaRows(lCodTenore, tenori);
			}

			String imgEsito = "-";
			if (lTenore.getCodEsitoSige() != null
					&& lTenore.getCodEsitoSige().length() > 1) {
				imgEsito = "<img src=\"/images/PresaVisione.gif\" border=0>";
				lmodifica = "SI";
			}

    // Modifica del 29/03/2017
    // La Sentenza a cui sono associati più Reati viene visualizzata una sola
    // volta nell'elenco "Oggetti Titoli Esecutivi"
	//if (!lTenoreEsteso.getSentenzaSige().getIdSentenza().toString().equalsIgnoreCase(idSentenza)) { 
	 
		idSentenza = lTenoreEsteso.getSentenzaSige().getIdSentenza().toString();
%>
		<tr>
			<td class="L" width="75%"><font class="label"> <span
					style="color: #556B2F"><%=lTenore.getDescrOggettoSige().toUpperCase()%></span>&nbsp;<%=lTenoreEsteso.getSentenza().getCellSentenza()
									.replaceAll("<br>", "")%>
			</font></td>
			<td class="c" width="5%"><font class="label"> <%=imgEsito%>
			</font></td>
			<td class="c" width="10%"><font class="label"> <jsp:include
						page="<%=ICostantiTenoreSige.PG_BUTTONS_PROVVEDIMENTO_TENORI%>">
						<jsp:param name="CampoIdEntita"
							value="<%=ICostantiTenoreSige.CAMPO_ID_TENORE_SIGE%>" />
						<jsp:param name="ValoreIdEntita"
							value="<%=lTenore.getIdTenoreSige()%>" />
						<jsp:param name="Modifica" value="<%=lmodifica%>" />
						<jsp:param name="flagTitoliEsecutivi" value="<%=isTitoliEsecutivi%>" />
						<jsp:param name="codOggettoSige" value="<%=lCodTenore%>" />
						<jsp:param name="idProvvedimento"
							value="<%=idProvvedimento.toString()%>" />
						<jsp:param name="IdFascicoloSiepSentenza"
							value="<%=(idFascicoloSiep==null?"":idFascicoloSiep.toString())%>" />
						<jsp:param name="idTenoreSige" value="<%=idTenore%>" />
						<jsp:param name="idSenSentenza" value="<%=lTenoreEsteso.getSentenza().getIdSentenza() %>" />
						<jsp:param name="esitoTenore" value="<%=lTenore.getCodEsitoSige() %>" />
						<jsp:param name="statoProvvValidato" value="<%=statoProvvValidato%>" />
					</jsp:include>
			</font></td>
			<%
			if (isTitoliEsecutivi && "SI".equalsIgnoreCase(Modificabile) && Upload.equalsIgnoreCase("SI")) {
			%>
			<%-- <td class="c" width="10%" rowspan="<%=rows%>">  --%>
			<td class="c" width="10%">
			    <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.tenore.action.ActLoadModificaEsiti&IdTenoreSige=<%=lTenore.getIdTenoreSige()%>&TornaQui=20&codOggettoSige=<%=lCodTenore%>&idProvvedimento=<%=idProvvedimento%>&isTitoliEsecutivi=true">
					<img src="/images/modifica24.gif"
					alt="Impostazione Esito per Oggetto" width="12" height="12"
					border="0">
			    </a>
			    
			    <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.tenore.action.ActLoadDettaglioOggettoProv&IdTenoreSige=<%=lTenore.getIdTenoreSige()%>&TornaQui=20&codOggettoSige=<%=lCodTenore%>&idProvvedimento=<%=idProvvedimento%>&isTitoliEsecutivi=true">
					<img src="/images/dettagli.gif"
					alt="Dettagli Esito per Oggetto" width="12" height="12"
					border="0">
			    </a>
			</td>
			<%
			}
			
			if (isTitoliEsecutivi && "SI".equalsIgnoreCase(Modificabile) && Upload.equalsIgnoreCase("NO")) {
				%>
				<%-- <td class="c" width="10%" rowspan="<%=rows%>">  --%>
				<td class="c" width="10%">
				    <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.tenore.action.ActLoadModificaEsiti&IdTenoreSige=<%=lTenore.getIdTenoreSige()%>&TornaQui=20&codOggettoSige=<%=lCodTenore%>&idProvvedimento=<%=idProvvedimento%>&isTitoliEsecutivi=true">
						<img src="/images/icona-piena.png"
						alt="Impostazione Esito per Oggetto" width="12" height="12"
						border="0">
				    </a>
				</td>
				<%
			}
			%>
		
		
		</tr>
<%
	//} // endif (!lTenoreEsteso.getSentenzaSige().getIdSentenza().toString().equalsIgnoreCase(idSentenza)){
		} // endfor
	} // endwhile
%>
<%!private int calcolaRows(String codOggetto,
			Vector<TenoreSigeEstesoModel> tenori) {
		int rows = 0;
		for (TenoreSigeEstesoModel tenore : tenori) {
			if (tenore.getTenoreSige().getCodOggettoSige()
					.equalsIgnoreCase(codOggetto))
				rows = rows + 1;
		}
		return rows;
	}%>
</html>