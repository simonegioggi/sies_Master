<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<%
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficio = "";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
	} else {
		labelUfficio = "Ufficio di Sorveglianza";
	}
%>	

<%
// Il modulo JSP viene incluso dalla JSP generica DettaglioOrdinanza durante la costruzione 
// della vista di dettaglio di un'ordinanza.
%>
<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<table cellspacing="2" cellpadding="2">
	<tr>
		<td><br></td>
	</tr>
	<tr>
  	<td class="Titolo" colspan=6> Modifica Permanente Sanzione Sostitutiva <td>
  </tr>
  <tr>
    <td class="l">Rilevato</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento(),"-")%></font></td>
  </tr>
  <tr>
  	<td class="l">Ulteriore Descrizione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getUlterioreDescrizione(),"-")%></font></td>
  </tr>
  <tr>
  	<td class="l"><%=labelUfficio%> Competente</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(),"-")%></font></td>
  </tr>
  <tr>
  	<td><br></td>
  </tr>
</table>