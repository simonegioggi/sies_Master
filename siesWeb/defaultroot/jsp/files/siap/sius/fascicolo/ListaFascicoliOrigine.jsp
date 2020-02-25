<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
	<script language="JavaScript">
	function insertIT(str1, str2, str3)
	{
          window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname1")%>.value=str1;
          window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname2")%>.value=str2;
          window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname3")%>.value=str3;

         window.close();
	}
	</script>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Procedimenti di Riferimento</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco Procedimenti di Riferimento</font></td>
    </tr>
    <tr></tr>
    <tr></tr>
  </table>

  <br>

  <table cellspacing=2 cellpadding=2>

    <tr>
      <td class="int">Numero SIUS</td>
      <td class="int">Descr. Ufficio</td>
      <td class="int">Procedimento</td>
      <td class="int">Data Richiesta Procedimento</td>
      <td class="int">selezione</td>
    </tr>

<%
    Iterator itx = fascicoli.iterator();
%>

<%
    while ( itx.hasNext())
    {
      FascicoloGPModel fascicolo = (FascicoloGPModel)itx.next();
%>
      <tr>
        <td class="c"><font class="label">
            <%=fascicolo.getFascicoloSiusModel().getChiaveAnno()%>
            /
            <%=fascicolo.getFascicoloSiusModel().getChiaveProgr()%>

        </font></td>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getDescrTipoUfficio()%> / <%=fascicolo.getFascicoloSiusModel().getDescrComuneUfficio()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font></td>
        <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"dd-MM-yyyy")%></font></td>
        <td class=c><a href="Javascript:insertIT('<%=fascicolo.getFascicoloSiusModel().getIdFascicoloSius().toString()%>', '<%=fascicolo.getFascicoloSiusModel().getChiaveAnno()%>', '<%=fascicolo.getFascicoloSiusModel().getChiaveProgr()%>')"><img align="middle" src="/images/fileselected.gif" border=0></a></td></tr>


</tr>
<%
  }
%>
    </table>
  </FORM>
  <br>

  </body>
</html>