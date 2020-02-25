<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<%
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficioTrib = "";
	String labelUfficioProc = "";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelUfficioTrib = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza";
		labelUfficioProc = "Procura della Repubblica presso il Tribunale per Minorenni";
	} else {
		labelUfficioTrib = "Tribunale di Sorveglianza";
		labelUfficioProc = "Procura";
	}
%>

<table cellspacing="2" cellpadding="2">
    <tr> <td> <br></td></tr>
    <tr>
        <td class="Titolo" colspan=6> Sopravvenienza Nuovo Titolo <td>
    </tr>
  <tr>
    <td class="l"> Rilevato</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento(),"-")%></font></td>
  </tr>
  <tr>
    <td class="l"> <%=labelUfficioTrib%> Competente</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrUffTdsConcessoRiduzione(),"-")%></font></td>
  </tr>
  <tr>
    <td class="l"> <%=labelUfficioProc%> Competente</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getLuogoSvolgimentoProva(),"-")%></font></td>
  </tr>
  <tr>
    <td class="l"> Istituto Detenzione per l'espiazione</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getServizioTerapeuticoComp(),"-")%></font></td>
  </tr>
    <tr> <td> <br></td></tr>
</table>

