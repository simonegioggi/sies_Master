<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<%
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
    String labelUfficio = "";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
	  labelUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
	} else {
	  labelUfficio = "Magistrato di Sorveglianza";
	}

%>

<table cellspacing="2" cellpadding="2">
    <tr><td><br></td></tr>
    <tr>
      <td class="Titolo" colspan="6">Misura Alternativa <td>
    </tr>
    <tr>
      <td class="l">Esistenza condizioni ostative </td>
      <% if ("S".equals(datiOrdinanza.getOrdinanza().getFlagEsistenzaReatoostativo())){ %>
      <td class="l"><font class="campo">SI</font></td>
      <% } else if ("N".equals(datiOrdinanza.getOrdinanza().getFlagEsistenzaReatoostativo())){ %>
      <td class="l"><font class="campo">NO</font></td>
      <% } else { %>
      <td class="l"><font class="campo">-</font></td>
      <% } %>  
    </tr>
    <tr>
      <td class="l">Avvenuta espiazione condanna per reato ostativo </td>
      <% if ("S".equals(datiOrdinanza.getOrdinanza().getFlagEspiazioneReatoostativo())){ %>
      <td class="l"><font class="campo">SI</font></td>
      <% } else if ("N".equals(datiOrdinanza.getOrdinanza().getFlagEspiazioneReatoostativo())){ %>
      <td class="l"><font class="campo">NO</font></td>
      <% } else { %>
      <td class="l"><font class="campo">-</font></td>
      <% } %> 
    </tr>
    <tr>
      <td class="l">Data Termine Misura </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataFineMisura(),"dd/MM/yyyy"), "-")%></font></td>
    </tr>
    <tr>
      <td class="l">Luogo svolgimento della prova </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(  datiOrdinanza.getOrdinanza().getLuogoSvolgimentoProva(), "-")%></font></td>
    <tr>    
    </tr>
      <td class="l"><%=labelUfficio%> Competente </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font></td>
    </tr>
    <tr>
      <td class="l">UEPE Competente </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrComuneCssaComp(), "-")%></font></td>
    </tr>
    <tr>
      <td class="l">Autorità delegata alla vigilanza </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getAutoritaVigilante(), "-")%></font></td>
    </tr>

    <tr>
      <td>
        <br>
      </td>
    </tr>
</table>