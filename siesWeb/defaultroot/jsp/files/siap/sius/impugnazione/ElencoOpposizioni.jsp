<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal"%>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>


<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.impugnazione.action.ICostantiImpugnazione" %>
<%@ page import="siap.sius.impugnazione.model.ImpugnazioneModel"%>

<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<jsp:useBean id="provvedimento"   scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="impugnazioni"    scope="request" class="java.util.Vector"/>

<jsp:useBean id="numOrdDec"     scope="request" class="java.lang.String"/>
<jsp:useBean id="dataOrdDec"    scope="request" class="java.lang.String"/>
<jsp:useBean id="dataDeposito"  scope="request" class="java.lang.String"/>

<jsp:useBean id="flag_valida"  scope="request" class="java.lang.String"/>


<jsp:useBean id="tipoUfficio"  scope="request" class="java.lang.String"/>


<%
//==============================================================================
// Elenco delle Opposizioni presenti su un provvedimento (evento)
//==============================================================================
String modificabile = "S";

if (   "01".equals (fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo())
    || "05".equals (fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo())
    || !"S".equals (provvedimento.getFlagDocumentoRegistrato())
   )
{
  modificabile = "N";
}
%>

<html>
<head>
  <title>[S.I.E.S.] - Lista Opposizioni per Provvedimento</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Elenco Opposizioni per Provvedimento</font>
      </td>

      <!-- BOTTONE DI ISCRIZIONE NUOVO RICORSO -->
      <% if ("S".equals(modificabile)) { %>
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.impugnazione.action.ActLoadInserisciOpposizione&IdEvento=<%=provvedimento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=provvedimento.getCodTipoProvvedimento()%>" >
          <img  align="middle" src="/images/new24.gif" alt="Inserimento Ulteriore Opposizione" width="24" height="24" border="0">
        </a>
      </td>
      <% } %>

      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>
  </table>
<br>


<%
  if (flag_valida.equals(""))
    flag_valida="SI";
%>

<% if (fascicoloSiusGP != null) { %>
  <table>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>
<%}%>

  <br>
  
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="label">Avverso il Provvedimento : </td>
    </tr>

    <tr>
      <% if (numOrdDec.compareTo("")==0 ) { %>
      <td class="campo"><%=provvedimento.getDescrTipoProvvedimento() +" di "+ provvedimento.getDescrMotivo()+" del "+ DateUtils.getDateToString ( provvedimento.getDataEmissione(), "dd/MM/yyyy" ) %></td>
      <%} else {%>
      <td> <font class="campo"><%=provvedimento.getDescrTipoProvvedimento()%> N. <%=numOrdDec%> </font> <font class="Label"> del </font> <font class="campo"> <%=dataOrdDec%> </font> <font class="Label"> depositato il </font> <font class="campo"> <%=dataDeposito%> </font> </td>
      <%}%>
    </tr>
  </table>
  
  
  <table width="96%">
  <% if ( impugnazioni.size() == 0 ) { %>
    <tr>
      <td class="L">
        <font class="label"> Non ci sono ricorsi per il provvedimento. </font>
      </td>
    </tr>
  <% } else { %>
    <tr>
      <td class="int" >Anno/Numero</td>
      <td class="int" >Tipo</td>
      <td class="int" >Presentato da</td>
      <td class="int" >Data atto</td>
      <td class="int" >Stato</td>
      <td class="int" >Azioni</td>
    </tr>

      <%
      Iterator itx = impugnazioni.iterator();
      while ( itx.hasNext())
      {
        ImpugnazioneModel lImp = (ImpugnazioneModel)itx.next();
        %>
          <tr align="right">
            <td class="c" ><%=StringUtils.toStringJSP(lImp.getAnnoS7(),"-")%>/<%=StringUtils.toStringJSP(lImp.getProgrS7(),"-")%></td>
            <td class="c" ><%=StringUtils.toStringJSP(lImp.getDescrTipoImpugnazione(),"-")%></td>
            <td class="c" ><%=StringUtils.toStringJSP(lImp.getDescrSoggettoImpugnante(),"-")%></td>
            <td class="c" ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lImp.getDataRicorso(),"dd-MM-yyyy"),"-") %></td>
            <td class="lRosso" style="text-align:center;">
            <% if ("S".equals (lImp.getFlagAnnullamento())) { %>
              ANNULLATO
            <%}else{%>
            &nbsp;
            <%}%>
            </td>

            <td class="c">
              <jsp:include page="<%=ICostantiImpugnazione.PG_BUTTONS_ELENCO_OPPOSIZIONI%>">
                <jsp:param name="CampoIdEntita"        value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
                <jsp:param name="ValoreIdEntita"       value="<%=provvedimento.getIdEvento()%>" />
                <jsp:param name="CampoIdImpugnazione"  value="<%=lImp.getIdImpugnazione()%>" />
                <jsp:param name="CodTipoProvvedimento" value="<%=provvedimento.getCodTipoProvvedimento()%>" />
                <jsp:param name="CodMotivo"            value="<%=provvedimento.getCodMotivo()%>" />
                <jsp:param name="FlagAnnullato"        value="<%=lImp.getFlagAnnullamento()%>" />
                <jsp:param name="Modificabile"         value="<%=modificabile%>" />
              </jsp:include>
            </td>
          </tr>
<%
      } // endwhile

  %>
  </table>
<%
  }  // endif provvedimenti.size()
%>
  </body>
</html>