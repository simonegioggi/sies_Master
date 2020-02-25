<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<%
	UtenteModel lUteMod =(UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
%>

<table cellspacing="2" cellpadding="2">
  <tr>
  	<td class="l">Ulteriore descrizione della decisione </td>
  	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
  </tr>
  <tr> <td> <br></td></tr>
  <%if(datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().equals("43"))
  	{  %>
	<tr>
    	<td class="Titolo" colspan=2> Estremi provvedimento Revocato: <td>
  	</tr>  
<%	}
    else
    {%>  
  	<tr>
    	<td class="Titolo" colspan=2> Estremi provvedimento reclamato: <td>
  	</tr>
<%	} %>  	
    <tr>
      <td class="l"> Data Emissione</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataTrasmissione(),"dd/MM/yyyy"),"-")%></font></td>
    </tr>
    <tr>
<%
	if( CodUff.equals("TDS") || CodUff.equals("UDS") ){
%>	
		<td class="l">Ufficio Sorveglianza emittente </td>
<%		
	} else {
%>
		<td class="l">Ufficio Sorveglianza presso Tribunale per Minorenni emittente </td>
<%		
	}
%>
      
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font></td>
    </tr>
</table>