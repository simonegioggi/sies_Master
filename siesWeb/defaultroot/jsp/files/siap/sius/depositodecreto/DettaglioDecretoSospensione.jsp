<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.prescrizione.model.PrescrizioneModel"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato" %>


<jsp:useBean id="depositoDecretoMotivazioni" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori"                     scope="request" class="java.util.Vector"/>
<jsp:useBean id="StatoLibertatis"            scope="request" class="java.util.Vector"/>
<jsp:useBean id="misuraAlternativa"      	 scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>

<%
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficioTrib = "";
	String labelUfficioSorv = "";
	String labelUfficioProc = "";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelUfficioTrib = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza";
		labelUfficioSorv = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
		labelUfficioProc = "Procura della Repubblica presso il Tribunale per Minorenni";
	} else {
		labelUfficioTrib = "Tribunale di Sorveglianza";
		labelUfficioSorv = "Ufficio di Sorveglianza";
		labelUfficioProc = "Procura";
	}
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Decreto Applicazione Provvisoria di Misura Alternativa </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="/html/gestisciUploadStampa.js"></script>

 <script language="JavaScript">
    function InserisciPrescrizioni()
    {
    alert("da implementare");
    }
 </script>


</head>

  <body class="corpo">
  <form name="dettaglio">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione : </font>
          <font class="campo">Dettaglio Decreto Applicazione Provvisoria di Misura Alternativa</font>&nbsp;
        </td>
        <jsp:include page="<%=ICostantiDepositoDecreto.BOTTONI_DETTAGLIO_DECRETO%>"/>
      </tr>
    </table>
    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
    <jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>"/>

  <table cellspacing=4 cellpadding=4 width=95%>
  <tr>
    <input Title="Id Evento" type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>" >
  </tr>
  <tr>
        <jsp:include page="<%=ICostantiDepositoDecreto.PG_DETTAGLIO_DATA%>"/>
  </tr>
  <tr>
    <td class="l"> Rilevato</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNote(),"-")%></font></td>
  </tr>
  <tr>
    <td class="l"> <%=labelUfficioTrib%> Competente</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getDescrTdsComp(),"-")%></font></td>
  </tr>
  <tr>
    <td class="l"> <%=labelUfficioSorv%> Competente</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getDescrUfficioCompetente(),"-")%></font></td>
  </tr>
  <tr>
    <td class="l"> <%=labelUfficioProc%> Competente</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getDescrProcuraEsecuzione(),"-")%></font></td>
  </tr>
  <tr>
    <td class="l"> Luogo Svolgimento della Prova</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getLuogoSvolgimentoProva(),"-")%></font></td>
  </tr>
  <tr>
    <td class="l">Status Persona</td>
    <td class="l"><font class="campo"> <%=DecodificheUtils.getDescbyCode(StatoLibertatis,depositoDecretoMotivazioni.getDepositoDecreto().getStatusPersona())%></font></td>
  </tr>
  
<%
	if (depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoControlloEsecuzione() != null) {
%>
		<tr>
      <td class="l">Tipo Controllo Esecuzione</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP( depositoDecretoMotivazioni.getDepositoDecreto().getDescrTipoControlloEsecuzione() )%></font></td>
    </tr>
<%
} 
%>    
  
<%
if (depositoDecretoMotivazioni.getDepositoDecreto().getAnnoProcRevocato() != null)
{
 %>
 <td class="l"> Rif. procedimento da revocare </td>
    <td class="l"><font class="campo"> <%=depositoDecretoMotivazioni.getDepositoDecreto().getAnnoProcRevocato()%>/<%=depositoDecretoMotivazioni.getDepositoDecreto().getProgrProcRevocato()%> </font></td>
  </tr>
 <tr>
 <td class="l"> Sede procedimento da revocare</td>
 <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getDescrProcuraRevocato(),"-")%></font></td>
 </tr>
<% } %>
  <tr>
    <td colspan=2>&nbsp;</td>
  </tr>

  <tr>
    <td class="Titolo" colspan=2> Esiti</td>
  </tr>
</table>
<table cellspacing=4 cellpadding=4 width=95%>

<%
  Iterator lInd = tenori.iterator();
  while (lInd.hasNext())
  {
%>
   <tr>
      <%
        TenoreModel lTen = (TenoreModel) lInd.next();
      %>
      <td class="l" width=70% ><%=lTen.getDescrOggettoTenore()%></td>
      <td class="l" width=30% ><%=lTen.getDescrEsitoTenore()%></td>
  </tr>
<%
  }
%>
  <tr>
    <td  colspan=2> &nbsp;</td>
  </tr>
  </table>

  <jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
   <jsp:param name="EveIdEvento" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>" />
   <jsp:param name="nextaction" value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito" />
  </jsp:include>
  
  <!-- MEV63 -->
<%
if (misuraAlternativa != null && misuraAlternativa.getFlFormaMisura() != null) {
   	String labelFormaMisura="&nbsp;";
   	if (misuraAlternativa.getFlFormaMisura() != null) {
   		labelFormaMisura="Permanenza in casa";
   		if (misuraAlternativa.getFlFormaMisura().compareTo(new BigDecimal(2)) == 0) {
   			labelFormaMisura="Collocamento in comunità";
   		}
   	}
   %>
    <table cellspacing=4 cellpadding=4 width="95%">
        <tr>
            <td class="l">La misura deve essere eseguita nelle forme della: </td> 
            <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(labelFormaMisura)%> </font></td>
          </tr>
          <%
          if (misuraAlternativa.getFlFormaMisura().compareTo(new BigDecimal (2))==0 && misuraAlternativa.getDescrizioneComunita() != null) {
          %>
           <tr>
            <td class="l">Comunità: </td> 
            <td class="l"><font class="campo"> <%=(misuraAlternativa.getDescrizioneComunita()==null?"":StringUtils.toStringJSP(misuraAlternativa.getDescrizioneComunita())) %> </font></td>
          </tr>
          <%
          }
          %>
      </table>
   <%
}
%>
  <jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
</form>

 <div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post">
  <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
    <tr>
      <td class="L">
       	<input class="bottone"  type="submit" value="Conferma">
       	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
       	<input type="HIDDEN" name="IdEvento"  value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
      	<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito">
      	<input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.EMISSIONE_DECRETO%>">
      </td>
    </tr>
  </table>

  </FORM>
  </div>

</body>
<script language="JavaScript">
   //document.dettaglio.ListaTemplate.options[document.dettaglio.ListaTemplate.options.selectedIndex].text=red;
</script>

</html>