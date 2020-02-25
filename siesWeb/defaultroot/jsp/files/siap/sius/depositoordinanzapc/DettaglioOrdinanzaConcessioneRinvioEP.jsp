<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
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
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni emittente";
	} else {
		labelUfficio = "Ufficio di Sorveglianza emittente";
	}
%>
<table cellspacing="4" cellpadding="4">
   <tr>
  	<td class="l">Ulteriore descrizione della decisione </td>
  	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
   </tr>
   
   <tr><td><br></td></tr>
 
 	<!-- [ Che sta a fare questa Roba ? Chiedere a Umberto ] -->
    <tr>
        <td class="Titolo" colspan=6> Concessione Rinvio Esecuzione della Pena <td>
    </tr>
    <!--  <tr> <td> <br></td></tr> -->
  <tr>
    <td class="Titolo" colspan=2> Estremi decreto Magistrato Sorveglianza: <td>
  </tr>
    <tr>
      <td class="l"> Data Emissione</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataTrasmissione(),"dd/MM/yyyy"),"-")%></font></td>
    </tr>
    <tr>
      <td class="l"><%=labelUfficio%></td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font></td>
    </tr>
   <tr><td> <br></td></tr>
     <tr>
      <td class="l">Data Inizio Periodo </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataInizioPeriodo(),"dd/MM/yyyy"), "-")%></font></td>
    </tr>

     <tr>
      <td class="l">Data Termine Periodo </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataFineMisura(),"dd/MM/yyyy"), "-")%></font></td>
    </tr>
   <tr>
      <td class="l">Durata sospensione  (AA-MM-GG)</td>
      <td class="l">
      <font class="campo">
         <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumAnniDetenzioneDom())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumMesiDetenzioneDom())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumGiorniDetenzioneDom())%>
      </font>
       </td>
    </tr>
    <tr> <td> <br></td></tr>

</table>

