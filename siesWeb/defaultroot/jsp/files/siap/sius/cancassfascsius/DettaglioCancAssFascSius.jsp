<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.siep.posizionematerialefasc.action.ICostantiPosizioneMaterialeFasc"%>
<%@ page import="siap.sius.cancassfascsius.model.CancAssFascSiusModel"%>
<%@ page import="siap.sius.cancassfascsius.action.ICostantiCancAssFascSius"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="elenco" scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="isModificabile" scope="request" class="java.lang.String" />

<%
   BigDecimal lIdFascicolo;
     lIdFascicolo = fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius();
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco delle Assegnazioni di un Procedimento SIUS a Cancellerie</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class=label>Funzione :</font> <font class=campo> Cancellerie Assegnatarie associate al procedimento</font> </td>
          <td class="LBG">
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiCancAssFascSius.CAMPO_FAS_SIUS_ID_FASCICOLO_SIUS%>" />
            <jsp:param name="ValoreIdEntita" value="<%=lIdFascicolo.toString()%>" />
            <jsp:param name="Modificabile" value="<%=isModificabile%>"/>
            <jsp:param name="tipo_posizione_materiale" value="SIUS"/>
           </jsp:include>
          </td>
         <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
    <br>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <br>
<% if (elenco != null && elenco.size() > 0)
{ %>
  <table width=90%>
     <div align=center>
        <tr>
          <td class="int" width=10%>Codice</td>
          <td class="int" width=40%>Descrizione</td>
          <td class="int" width=20%>Data Inizio</td>
          <td class="int" width=20%>Data Fine</td>
          <td class="int" width=10%>Azioni</td>
        </tr>
      </div>
<%
  boolean bottone = true;
  Iterator itx = elenco.iterator();
  while ( itx.hasNext())
  {
    CancAssFascSiusModel lCancAssFascSius = (CancAssFascSiusModel)itx.next();
%>
    <tr>
      <td class=c><%=lCancAssFascSius.getCodCancelleriaAssegnataria()%></td>
      <td class=c><%=lCancAssFascSius.getDescCancelleriaAssegnataria()%></td>
      <td class=c><%=DateUtils.getDateToString(lCancAssFascSius.getDataInizio(),"dd-MM-yyyy")%></td>
      <td class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(lCancAssFascSius.getDataFine(),"dd-MM-yyyy"), "-")%></td>
     <td class=c>
<% if (bottone)
   {
%>
        <jsp:include page="<%=ICostantiPosizioneMaterialeFasc.PG_BUTTONS%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiCancAssFascSius.CAMPO_FAS_SIUS_ID_FASCICOLO_SIUS%>" />
            <jsp:param name="ValoreIdEntita" value="<%=lIdFascicolo.toString()%>" />
          <jsp:param name="Modificabile" value="<%=isModificabile%>"/>
       </jsp:include>
<%
     bottone = false;
  } else { %>
&nbsp;
<% } %>
      </td>
    </tr>
<%
  }
%>
    </table>
<% } else { %>
  <br>
<font class="campo"> Il Procedimento non risulta assegnato ad alcuna Cancelleria </font>
<% } %>
  </body>
</html>