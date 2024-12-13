<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato" %>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>

<jsp:useBean id="depositoDecretoMotivazioni" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori"                     scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicolo_origine"   scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<%
  boolean isFascicoloOrigine = (fascicolo_origine != null && fascicolo_origine.getFascicoloSiusModel() != null && fascicolo_origine.getFascicoloSiusModel().getIdFascicoloSius() != null) ? true : false;
%>
<html>
<head>
<title>[S.I.E.S.] - Dettaglio Decreto Revoca Autorizzazione / Modifica Prescrizioni Pena Sostitutive  </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="/html/gestisciUploadStampa.js"></script>

</head>
<%
  UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
  UfficioModel lUffMod = lUteMod.getUfficioUtente();
  String CodUff = new String(lUffMod.getCodTipoUfficio());
  String labelUfficio = "";
  if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
    labelUfficio = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza";
  } else {
    labelUfficio = "Tribunale di Sorveglianza";
  }
%>

<body class="corpo">
  <form name="dettaglio">

  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione : </font>
        <font class="campo">Dettaglio Decreto Revoca Autorizzazione / Modifica Prescrizioni Pena Sostitutive</font>&nbsp;
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
      <td class="l"> Eventuale Motivazione </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNote(),"-")%></font></td>
    </tr>
    <!--  
    <tr>
      <td class="l"> <%=labelUfficio%> Competente</td>
      <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getDescrTdsComp(),"-")%></font></td>
    </tr> 
    -->   
    <tr>
      <td class="l">Tipo Controllo Esecuzione</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP( depositoDecretoMotivazioni.getDepositoDecreto().getDescrTipoControlloEsecuzione(), "-" )%>
        </font>
      </td>
    </tr>
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
    TenoreModel lTen = (TenoreModel) lInd.next();
%>
   <tr>
      <td class="l" width=70% ><%=lTen.getDescrOggettoTenore()%></td>
      <td class="l" width=30% ><%=lTen.getDescrEsitoTenore()%> </td>
  </tr>
<%
  }
%>
  <tr>
    <td  colspan=2> &nbsp;</td>
  </tr>
  
<jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
  <jsp:param name="EveIdEvento" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>" />
  <jsp:param name="nextaction" value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito" />
</jsp:include>
  
  
  
  <% if (isFascicoloOrigine) { %>
    </table>
    <jsp:include page="<%=ICostantiFascicoloSius.PG_SINTESIPROCEDIMENTOORIGINESIUS%>">
      <jsp:param name="Titolo" value="Dati Autorizzazione Revocata" />
    </jsp:include>  
  <% } else {%>
  <tr>
    <td> <font class="crosso">Non esiste il procedimento collegato per risalire al provvedimento revocato !</font></td>
  </tr>
</table>
  <% } %>
    
  <BR />
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

</html>