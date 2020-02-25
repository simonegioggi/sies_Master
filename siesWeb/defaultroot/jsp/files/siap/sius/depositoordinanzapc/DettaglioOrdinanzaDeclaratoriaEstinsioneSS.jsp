<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<%
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficio = "";
	if(CodUff.equals("UDSM")){
	  labelUfficio = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza Competente";
	} else {
	  labelUfficio = "Tribunale di Sorveglianza Competente";
	}
%>

<table cellspacing="2" cellpadding="2">
    <tr> <td> <br></td></tr>
    <tr>
        <td class="Titolo" colspan=6> Declaratoria Estinzione Sanzione Sostitutiva <td>
    </tr>
  <tr>
      <td class="l"> Rilevato</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento(),"-")%></font></td>
  </tr>
  <tr>
  
    <td class="l"><%=labelUfficio%></td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getDescrUffTdsConcessoRiduzione(),"-")%></font></td>
  </tr>
    <tr> <td> <br></td></tr>
</table>