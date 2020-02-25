<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.udienza.model.UdienzaModel"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento"%>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="udienza"       scope="request" class="siap.sius.udienza.model.UdienzaModel"/>
<jsp:useBean id="sudienze"     scope="request" class="java.lang.String"/>

 <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="/html/conferma.js"></script>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Rinvio Udienza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">
  <table>
      <tr><td class="LBG"> <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label"> Funzione :</font>
      <font class="campo"> Dettaglio Rinvio Udienza</font>
      </td>
<%
if (request.getAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO) != null )
{
%>
          <jsp:include page="<%=ICostantiUdienza.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=request.getAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO)%>" />
          </jsp:include>
<%
}
%>
    </tr>

    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
 <tr> <td>&nbsp;</td> </tr>
  </table>
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  	<%--FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="DettaglioRinvioUdienza"--%>
  <table cellspacing=2 cellpadding=2>

<%
  //  if (DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"yyyyMMdd").trim().compareTo(DateUtils.getDateToString(udienza.getDataUdienza(),"yyyyMMdd").trim() ) != 0 )
  if (fascicoloSiusGP.getGeneraleProcedimentoModel().getUdiIdUdienza() != null)
  {
%>
      <tr>
        <td class="l">Nuova Data Udienza</td>
        <td class="L" colspan="5">
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString( udienza.getDataUdienza(), "dd-MM-yyyy"), "-" )%>
        </font>
      </tr>

      <tr>
        <td class="l">Luogo Svolgimento Udienza</td>
        <td class="L" colspan="5">
        <font class="campo"><%=StringUtils.toStringJSP(udienza.getLuogoUdienza())%></font>&nbsp;
        </td>
      </tr>
<%
    }else{
%>
      <tr>
        <td class="label">Il Procedimento è stato rinviato a nuovo ruolo</td>
      </tr>
      <tr>
        <td class="label">&nbsp;</td>
      </tr>
<%
    }
%>

    <tr>
      <td class="l">Date Udienze Precedenti: </td>
      <td class="L" colspan="5">
        <font class="campo"><%=sudienze%></font>&nbsp;
      </td>
    </tr>

    </table>
  <!--/form-->

  </body>
</html>