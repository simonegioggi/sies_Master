<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.Utils"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocato"%>

<%@ page import="siap.sius.esperto.model.EspertoModel" %>
<%@ page import="siap.sius.esperto.action.ICostantiEsperto"%>

<jsp:useBean id="evento"  scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="avvocato" scope="request" class="java.util.Vector" />
<jsp:useBean id="magistrato"  scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="esperto"  scope="request" class="siap.sius.esperto.model.EspertoModel"/>

<%
  EventoModel lEve = evento;
%>

<html>
  <head>
<title> [S.I.E.S.] - Dettaglio Verbale Udienza </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" >
function lookUpload() {
        var node;
        node=document.getElementById('upld');
			  node.style.visibility='visible';
      }
    </script>
  </head>

  <%
if (lEve.getFlagDocumentoRegistrato() != null) {
	if (lEve.getFlagDocumentoRegistrato().compareTo("N") == 0) {
  %>
   <BODY class="corpo" onload="javascript:lookUpload();">
  <%
    }
} else {
  %>
   <BODY class="corpo">
  <%
  }
  %>
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Verbale Udienza</font>
        </td>

  <%
if (lEve.getFlagDocumentoRegistrato() == null || lEve.getFlagDocumentoRegistrato().compareTo("N") == 0) {
  %>

  <!-- BOTTONE DI STAMPA -->
    <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lEve.getIdEvento()%>" />
          </jsp:include>
  <%
  }
  %>
  </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>

    <table width="100%" cellspacing=2 cellpadding=2>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="Titolo" colspan=3 width="100%"> Difensori </td>
    </tr>
<%
  	Iterator itx = avvocato.iterator();
while (itx.hasNext()) {
      	AvvocatoModel lAvv = (AvvocatoModel)itx.next();
%>
      	<tr style="width: 100%;">
          <td class=l><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%></td>
          <td class=l><%=StringUtils.toStringJSP(lAvv.getForo(),"-")%></td>
          <td class=l><%=StringUtils.toStringJSP(lAvv.getIndirizzo(),"-")%></td>
        </tr>
<%
	  }
%>
    </table>
    <table cellspacing=2 cellpadding=2 width="100%">
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="Titolo" colspan=3 width="100%"> Magistrato Relatore </td>
    </tr>
    <tr>
      <td class="L" colspan=3>
        <%=StringUtils.toStringJSP(magistrato.getCognome())%>
        <%=StringUtils.toStringJSP(magistrato.getNome())%>
      </td>
    </tr>
    <tr>
      <td class="L" colspan=3>
        <%=StringUtils.toStringJSP(esperto.getCognome())%>
        <%=StringUtils.toStringJSP(esperto.getNome())%>
      </td>
    </tr>
  </table>


  <div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post">
    <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
        <td class="L">
          <input class="bottone"  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
          <input type="HIDDEN" name="IdEvento"  value="<%= lEve.getIdEvento() %>">
			<%-- [SG]: refactoring della pagina --%>
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.sius.udienza.action.ActLoadDettaglioVerbaleUdienza">
        </td>
      </tr>
    </table>

    </FORM>
    </div>
  </body>

</html>