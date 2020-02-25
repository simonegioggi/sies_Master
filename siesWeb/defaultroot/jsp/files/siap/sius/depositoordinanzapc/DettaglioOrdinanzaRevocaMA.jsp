<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="ufficioConcessoRiduzione" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<table cellspacing="2" cellpadding="2">
  <tr>
  	<td class="l">Ulteriore descrizione della decisione </td>
  	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
  </tr>
  <tr><td><br></td></tr>
  <tr>
    <td class="Titolo" colspan=2> Estremi ordinanza revocata: <td>
  </tr>
    <tr>
      <td class="l"> Data Emissione</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataInizioPeriodo(),"dd/MM/yyyy"),"-")%></font></td>
    </tr>
    <tr>
    
 <% 
     	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    	UfficioModel lUffMod = lUteMod.getUfficioUtente();
    	String CodUff = new String(lUffMod.getCodTipoUfficio());
    	
    	String lDescrTipoUfficio = new String("--");
    		
    	if( ufficioConcessoRiduzione.getCodTipoUfficio().equals("TDS") ){
    		if(CodUff.equals("TDSM")){
    			lDescrTipoUfficio = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza emittente";
    		} else {
    			lDescrTipoUfficio = "Tribunale Sorveglianza emittente";
    		}
    	} else if( ufficioConcessoRiduzione.getCodTipoUfficio().equals("UDS")){
    		if(CodUff.equals("UDSM")){
    			lDescrTipoUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni emittente";
    		} else {
    			lDescrTipoUfficio = "Ufficio Sorveglianza emittente";
    		}
    	}
%>
      <td class="l"><%=lDescrTipoUfficio%></td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUffTdsConcessoRiduzione(), "-")%></font></td>
    </tr>
   <tr></tr>
  <tr>
    <td class="Titolo" colspan=2> Estremi decreto Magistrato Sorveglianza: <td>
  </tr>
    <tr>
      <td class="l"> Data Emissione</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataTrasmissione(),"dd/MM/yyyy"),"-")%></font></td>
    </tr>
    <tr>
<% 	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){ %>
      	<td class="l">Ufficio di Sorveglianza presso il Tribunale per Minorenni emittente</td>
<%  } else { %> 
		<td class="l">Ufficio Sorveglianza emittente</td>
<%  } %>     
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font></td>
    </tr>
   <tr></tr>
    <tr>
      <td class="L"> Data decorrenza revoca</td>
      <td class="L"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataDecorrenza(),"dd/MM/yyyy"),"-")%></font></td>
    </tr>
    <tr>
      <td class="l">Pena rideterminata <br> (AA-MM-GG)</td>
      <td class="l">
      <font class="campo">Reclusione  <br>
         <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumAnniDetenzioneDom())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumMesiDetenzioneDom())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumGiorniDetenzioneDom())%>
      </font>
       </td>
      <td class="l">
      <font class="campo">Arresto  <br>
         <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumAnniArrestoRev())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumMesiArrestoRev())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumGiorniArrestoRev())%>
      </font>
       </td>
    </tr>
</table>