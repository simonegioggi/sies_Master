<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.residenza.action.ICostantiResidenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.residenza.model.ResidenzaModel" %>
<%@ page import="siap.sico.residenza.model.ResidenzaAssociataModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="isModificabile" scope="request" class="java.lang.String" />
<jsp:useBean id="residenze" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Domicili per Procedimento</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class=label>Funzione :</font>
        <font class=campo>Elenco Domicili per Procedimento</font>
      </td>
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
  <br>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>
<%
  BigDecimal lIdSoggetto = ((ResidenzaAssociataModel)residenze.get(0)).getResidenza().getSogIdSoggetto();
%>

  <table>
    <tr>
      <td class="int">Indirizzo</td>
      <td class="int">CAP</td>
      <td class="int">Luogo / Comune Estero</td>
      <td class="int">Stato</td>
      <td class="int">Data inizio val.</td>
      <td class="int">Data fine val.</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
<%
  Iterator itx = residenze.iterator();
  while ( itx.hasNext()) {
    ResidenzaAssociataModel residenza = (ResidenzaAssociataModel)itx.next();
%>

    <tr>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getResidenza().getIndirizzo(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getResidenza().getCap(),"-")%></td>
<%
      if (residenza.getResidenza().getCodComune().compareTo("-")==0)
      {
%>
        <td class="l"><%=StringUtils.toStringJSP(residenza.getResidenza().getDescComuneEstero(),"-")%></td>
      <%}else{ %>
        <td class="l"><%=StringUtils.toStringJSP(residenza.getResidenza().getDescrComune(),"-")%> (<%=residenza.getResidenza().getCodProvincia()%>) </td>
      <%}%>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getResidenza().getDescrStato(),"-")%></td>
      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(residenza.getResidenzaFascicoloSius().getDataInizioValidita(),"dd-MM-yyyy"),"-")%></td>
      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(residenza.getResidenzaFascicoloSius().getDataFineValidita(),"dd-MM-yyyy"),"-")%></td>
    <%if (isModificabile.compareTo("SI")==0 && StringUtils.toStringJSP(DateUtils.getDateToString(residenza.getResidenzaFascicoloSius().getDataFineValidita(),"dd-MM-yyyy"),"-").equals("-"))
    {
    %>
      <td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>" />
           <jsp:param name="ValoreIdEntita" value="<%=residenza.getResidenza().getIdResidenza()%>" />
        </jsp:include>
      </td>
    </tr>
    <%
    }
      else
    {
    %>
      <td class=c>-</td>
    </tr>
    <%
    }
  }
%>
    </table>
  </form>
  </body>
</html>