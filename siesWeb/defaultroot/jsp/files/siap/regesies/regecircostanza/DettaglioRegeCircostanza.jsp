<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.regesies.regecircostanza.action.ICostantiRegeCircostanza" %>
<%@ page import="siap.regesies.action.ICostantiRegeSies" %>
<%@ page import="siap.regesies.regecircostanza.model.RegeCircostanzaModel" %>

<jsp:useBean id="regecircostanza" scope="request" class="siap.regesies.regecircostanza.model.RegeCircostanzaModel"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.A.P] - Gestione Reato - </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();">
      <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td><td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Rege Circostanza</font>
        </td>
        <%RegeCircostanzaModel reato = regecircostanza;%>
        <td class="LBG">
          <jsp:include page="<%=ICostantiRegeSies.PG_TOOLBAR_REGE_HEADER%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeCircostanza.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=reato.getIdFile()%>" />
             <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiRegeCircostanza.CAMPO_PROGR_CIRCOSTANZA%>" />
             <jsp:param name="ValoreIdEntitaProvv" value="<%=reato.getProgrCircostanza()%>" />
          </jsp:include>
        </td>
      </tr>
    </table>

   <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/>
   <br>
   </FORM>
  <table cellspacing=2 cellpadding=2 width=95%>
    <tr><td class="Titolo">ReGe Circostanza</td></tr>
    <tr>
      <td class="l">
        <font class="campo">

<%
          boolean lFlagAnnoNumero = false;
          if( reato.getAnnoFonte() != 0
              && reato.getNumeroFonte() != null && !reato.getNumeroFonte().equals("") )
          {
            lFlagAnnoNumero = true;
          }

          if(lFlagAnnoNumero)
          {
            if(reato.getDescrFonte() != null && !reato.getDescrFonte().equals("") && !reato.getDescrFonte().equals("-"))
              out.println(reato.getDescrFonte()+" ");
            if(reato.getAnnoFonte() != 0 )
              out.println(reato.getAnnoFonte());
            if(reato.getNumeroFonte() != null && !reato.getNumeroFonte().equals(""))
              out.println("/"+reato.getNumeroFonte());
          }

          if(reato.getArticolo() != null && !reato.getArticolo().equals(""))
            out.println("art."+reato.getArticolo());
          if(reato.getDescrSottonumerazione() != null && !reato.getDescrSottonumerazione().equals("") && !reato.getDescrSottonumerazione().equals("-"))
            out.println(" "+reato.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(reato.getDescrFonte() != null && !reato.getDescrFonte().equals("") && !reato.getDescrFonte().equals("-"))
              out.println(reato.getDescrFonte());
          }

          if(reato.getComma() != null && !reato.getComma().equals(""))
            out.println(" c. "+reato.getComma());
          if(reato.getLettera() != null && !reato.getLettera().equals(""))
            out.println(" l. "+reato.getLettera());
          if(reato.getNumero() != null && !reato.getNumero().equals(""))
            out.println(" n. "+reato.getNumero());
%>
        </font>
      </td>
    </tr>
  </table>
</body>
</html>