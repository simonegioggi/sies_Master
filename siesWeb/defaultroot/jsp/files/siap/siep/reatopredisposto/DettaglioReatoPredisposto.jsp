<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.reatopredisposto.action.ICostantiReatoPredisposto" %>
<%@ page import="siap.siep.reatopredisposto.model.ReatoPredispostoModel" %>

<jsp:useBean id="reatopredisposto"  scope="request" class="siap.siep.reatopredisposto.model.ReatoPredispostoModel"/>
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Reato Predisposto</font>
        </td><td class="LBG">
        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiReatoPredisposto.CAMPO_ID_REATO_PREDISPOSTO%>" />
            <jsp:param name="ValoreIdEntita" value="<%=reatopredisposto.getIdReatoPredisposto()%>" />
          </jsp:include>
          </td>
          <td class="LBG">
      <a href="javascript:history.back();">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>
    </td>

      </tr>
    </table>
    <br>
  </FORM>
  <table cellspacing=2 cellpadding=2 width=95%>
    <tr><td class="Titolo">ReatoPredisposto</td></tr>
    <tr>
      <td class="l">
        <font class="campo">
<%
        	out.print("Elemento ");
%>
            <font class="campoNoCap">
<%
              out.print(reatopredisposto.getNomeElemento()+": ");
%>
            </font>
<%

          boolean lFlagAnnoNumero = false;
          if( reatopredisposto.getAnnoFonte() != null && !reatopredisposto.getAnnoFonte().equals("")
              && reatopredisposto.getNumeroFonte() != null && !reatopredisposto.getNumeroFonte().equals("") )
          {
            lFlagAnnoNumero = true;
          }

          if(lFlagAnnoNumero)
          {
            if(reatopredisposto.getDescrFonte() != null && !reatopredisposto.getDescrFonte().equals("") && !reatopredisposto.getDescrFonte().equals("-"))
              out.println(reatopredisposto.getDescrFonte()+" ");
            if(reatopredisposto.getAnnoFonte() != null && !reatopredisposto.getAnnoFonte().equals(""))
              out.println(reatopredisposto.getAnnoFonte());
            if(reatopredisposto.getNumeroFonte() != null && !reatopredisposto.getNumeroFonte().equals(""))
              out.println("/"+reatopredisposto.getNumeroFonte());
          }

          if(reatopredisposto.getArticolo() != null && !reatopredisposto.getArticolo().equals(""))
            out.println("art."+reatopredisposto.getArticolo());
          if(reatopredisposto.getDescrSottonumerazione() != null && !reatopredisposto.getDescrSottonumerazione().equals("") && !reatopredisposto.getDescrSottonumerazione().equals("-"))
            out.println(" "+reatopredisposto.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(reatopredisposto.getDescrFonte() != null && !reatopredisposto.getDescrFonte().equals("") && !reatopredisposto.getDescrFonte().equals("-"))
              out.println(reatopredisposto.getDescrFonte());
          }

          if(reatopredisposto.getComma() != null && !reatopredisposto.getComma().equals(""))
            out.println(" c. "+reatopredisposto.getComma());
          if(reatopredisposto.getLettera() != null && !reatopredisposto.getLettera().equals(""))
            out.println(" l. "+reatopredisposto.getLettera());
          if(reatopredisposto.getNumero() != null && !reatopredisposto.getNumero().equals(""))
            out.println(" n. "+reatopredisposto.getNumero());
%>
        </font>
      </td>
    </tr>
  </table>
<%
  //Se il reatopredisposto è quello principale
  if(reatopredisposto.getProgrNorma().intValue() == 1)
  {
%>
    <table cellspacing=2 cellpadding=2 width=95%>
<%

    if(reatopredisposto.getNoteElemento() != null && !reatopredisposto.getNoteElemento().equals(""))
    {
%>
      <tr>
        <td class="l"><font class="label">Note</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(reatopredisposto.getNoteElemento())%>&nbsp;</font></td>
      </tr>
<%
    }
%>
    </table>
<%
  }
%>

</body>
</html>