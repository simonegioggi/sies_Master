<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>

<jsp:useBean id="depositoDecretoMotivazioni" 	scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="TornaQui" 						scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" 						scope="request" class="java.lang.String"/>

<%
// Flag per indicare la modalità di Modifica Decreto
boolean modificaDecreto = false;
if (modalita != null && modalita.trim().equalsIgnoreCase("M"))
	modificaDecreto = true;
if (depositoDecretoMotivazioni.getEvento().getEveIdEventoRevoca() != null) {
%>
	<tr>
    	<td class="l"><font class="crosso"> Revocato</font></td>
	</tr>
	<tr>
		<td>
   			<jsp:include page="<%=ICostantiDepositoDecreto.PG_SINTESI_DECRETO_REVOCA%>"/>
			<br>
		</td>
	</tr>
<%
}
if (modificaDecreto) {
	// MEV_2023-35: aggiunta parametrizzazione oggetto procedimento
	String descrContenuto = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoDecreto(),
			depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto());
	FascicoloGPModel fgpm = (FascicoloGPModel) session.getAttribute("fascicoloSiusGP");
	String contenuto = fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
	if ("U128".equals(contenuto)) {
		descrContenuto = "decreto autorizzazione su pene sostitutive";
	}
%>
  	<tr>
    	<td class="l">Tipo di Decreto</td>
    	<td class="l">
    		<font class="campo"><%=descrContenuto%></font>
    	</td>
  	</tr>
<%
} else {
%>
	<tr>
	    <td class="l">Data Emissione</td>
	    <td class="l">
	    	<font class="campo"><%=DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataEmissione(), "dd/MM/yyyy")%></font>
	    </td>
  	</tr>
<%
} 
if (depositoDecretoMotivazioni.getDepositoDecreto().getDataDeposito() != null) {
%>
	<tr>
		<td class="l">Anno / Numero del Decreto</td>
		<td class="l">
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositodecreto.action.ActLoadInserisciDataDepositoDecreto&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoDecretoMotivazioni.getDepositoDecreto().getIdEventoGenerato()%>&TornaQui=<%=TornaQui%>">
				<%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getAnnoS72())%> / <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNumS72())%>
			</a>
		</td>
	</tr>
	<tr>
		<td class="l">Data Deposito in Cancelleria</td>
		<td class="l">
			<font class="campo"><%=DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataDeposito(), "dd/MM/yyyy")%></font>
		</td>
	</tr>
<%
}
%>
	<tr>
    	<td class="l">Stato del provvedimento</td>
<%
if (depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato() != null
		&& depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("A")) {
%>
    	<td class="l"><font class="cRosso">ANNULLATO</font></td>
<%
} else if (depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato() != null
		&& depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S")) {
%>
    	<td class="l"><font class="campo">Validato</font></td>
<%
} else {
%>
    	<td class="l"><font class="campo">Da Validare</font></td>
<%
}
%>
	</tr>