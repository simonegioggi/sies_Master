<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="revoca"     		scope="request" class="java.lang.String"/>

    <script language="JavaScript">
      // Chiamata all'elenco degli UDS
      var desktop;

      function ListaUDS(a_formname, a_fieldname, a_typename)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    </script>


<br>
 <table cellspacing="2" cellpadding="2" style="width: 90%;">
  <tr> </tr>
  <tr>
<%	if(revoca != null && revoca.compareTo("SI")== 0) 
	{ %>  
    	<td class="Titolo" colspan=6> Estremi provvedimento di Liberazione Anticipata Revocato: <td>
 <% }
    else
    {	%>
    	<td class="Titolo" colspan=6> Estremi provvedimento di Liberazione Anticipata reclamato: <td>
 <%}	%>
  </tr>
    <tr>
      <td class="l">Data Emissione <br> (gg-mm-aaaa)</td>
      <td class="L">
        <input value="<%=(datiOrdinanza.getEvento() != null ? DateUtils.getDateToString(datiOrdinanza.getEvento().getDataEmissione(),"dd") : "")%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=(datiOrdinanza.getEvento() != null ? DateUtils.getDateToString(datiOrdinanza.getEvento().getDataEmissione(),"MM") : "")%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=(datiOrdinanza.getEvento() != null ? DateUtils.getDateToString(datiOrdinanza.getEvento().getDataEmissione(),"yyyy") : "")%>" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
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
      <td class="l"><%=labelUfficio%></td>	  
      <td class="l">
        <input Title="<%=labelUfficio%>" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA %>" value="<%=(datiOrdinanza.getOrdinanza() != null ? datiOrdinanza.getOrdinanza().getDescrUfficioInserimento() : "")%>" size=35 >
        <a href="Javascript:ListaUDS('<%=request.getParameter("FormName")%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA %>','<%=CodUff%>');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </td>
    </tr>
  <tr> <td>&nbsp;</td> </tr>

  <tr>
 <% if(revoca != null && revoca.compareTo("SI")== 0) 
	{ %> 
    	<td class="Titolo" colspan=6> In caso di Revoca, indicare: <td>
 <% }
    else
    { %>  
    	<td class="Titolo" colspan=6> In caso di accoglimento del Reclamo, indicare: <td> 
 <% }	%>   
  </tr>
</table>
