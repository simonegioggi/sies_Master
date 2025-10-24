<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.List"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>

<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato" %>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="depositoDecretoMotivazioni" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori"    scope="request" class="java.util.Vector"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Decreto Irreperibibilità </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
 <script language="JavaScript">
function lookUpload() {
      var node;
      node=document.getElementById('upld');
                        node.style.visibility='visible';
    }
  </script>
</head>

  <body class="corpo">
    <table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
		<td class="LBG">
			<font class="label">Funzione:&nbsp;</font>
  			<font class="campo">Dettaglio Decreto Irreperibilità</font>
        </td>

<%
if (depositoDecretoMotivazioni != null && depositoDecretoMotivazioni.getDepositoDecreto() != null
		&& depositoDecretoMotivazioni.getEvento() != null) {
	if (depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato() == null
			|| depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
%>
  <!-- BOTTONE DI STAMPA -->
    <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
          <jsp:param name="ValoreIdEntita" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>"/>
          </jsp:include>
<%
		if (depositoDecretoMotivazioni.getDepositoDecreto().getDataDeposito() == null) {
%>
    <!-- BOTTONE DI CANCELLAZIONE -->
    <td class="LBG">
      <a href="Javascript:conferma('siap.sius.depositodecreto.action.ActCancellaDepositoDecreto','<%=ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO%>','<%=depositoDecretoMotivazioni.getDepositoDecreto().getIdDepositoDecreto()%>','<%=ICostantiDepositoDecreto.ACTION_DOPO_CANCELLAZIONE%>','siap.sius.depositodecreto.action.ActLoadFSPInserisciDecretoIrreperibilità');">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
      </a>
    </td>
<%
      }
    }
  }
 %>
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    <tr>
    <jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
    </tr>
    <tr>
      <td>&nbsp;</td>

     </tr>

  </table>

  <table cellspacing=4 cellpadding=4 width=95%>


  <tr>
        <jsp:include page="<%=ICostantiDepositoDecreto.PG_DETTAGLIO_DATA%>"/>
  </tr>

  <tr>
    <td colspan=2>&nbsp;</td>
  </tr>

  <tr>
    <td class="Titolo" colspan=2> Oggetti</td>
  </tr>

<%
  Iterator lInd = tenori.iterator();
while (lInd.hasNext()) {
%>
   <tr>
      <%
        TenoreModel lTen = (TenoreModel) lInd.next();
      %>
      <td class="l"><%=lTen.getDescrOggettoTenore()%></td>
  </tr>
<%
  }
%>
  <tr>
    <td  colspan=2> &nbsp;</td>
  </tr>
   <tr>
        <td class="l">Informative</td>
        <%-- [SG]: gestione informative nulle --%>
        <td class="l"><%=!Utils.isNullObj(depositoDecretoMotivazioni.getDepositoDecreto().getNote()) ? depositoDecretoMotivazioni.getDepositoDecreto().getNote() : ""%></td>
   </tr>

  </table>

 <div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post">
  <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
    <tr>
    <td>&nbsp;</td>
    	<td>
    <input Title="Id Evento" type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>" >
  		</td>
  </tr>
    <tr>
      <td class="L">
       <input class="bottone"  type="submit" value="Conferma">
       <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
       <%--input type="HIDDEN" name="IdEvento"  value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>"--%>
       <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoIrreperibilità">
       <input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.EMISSIONE_DECRETO%>">
      </td>
    </tr>


  </table>

  </FORM>
  </div>

</body>
</html>