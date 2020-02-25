<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.regesies.regereato.action.ICostantiRegeReato" %>
<%@ page import="siap.regesies.action.ICostantiRegeSies" %>
<%@ page import="siap.regesies.regereato.model.RegeReatoModel" %>

<jsp:useBean id="regereato" scope="request" class="siap.regesies.regereato.model.RegeReatoModel"/>

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
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Rege Reato</font>
        </td>
        <%RegeReatoModel reato = regereato;%>
        <td class="LBG">
          <jsp:include page="<%=ICostantiRegeSies.PG_TOOLBAR_REGE_HEADER%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeReato.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=reato.getIdFile()%>" />
             <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiRegeReato.CAMPO_PROGR_REATO%>" />
             <jsp:param name="ValoreIdEntitaProvv" value="<%=reato.getProgrReato()%>" />
             <jsp:param name="ChiaveTre" value="<%=ICostantiRegeReato.CAMPO_PROGR_CIRCOSTANZA%>" />
             <jsp:param name="ValoreTre" value="<%=reato.getProgrCircostanza()%>" />
          </jsp:include>
        </td>
      </tr>
    </table>
    <br>
   <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/>
   <br>
   </FORM>
  <table cellspacing=2 cellpadding=2 width=95%>
    <tr><td class="Titolo">ReGe Reato</td></tr>
    <tr>
      <td class="l">
        <font class="campo">
<%        String lProgressivo = "";
          if(reato.getProgrCircostanza() == 1)
          if(reato.getProgrNumeroManuale() != null)
              lProgressivo =  reato.getProgrNumeroManuale();
          else
              lProgressivo += reato.getProgrReato();
            //lProgressivo = (reato.getProgrNumeroManuale() != null) ? reato.getProgrNumeroManuale() : reato.getProgrReato();

          if(!lProgressivo.equals(""))
          {
%>
            <font class="campoNoCap">
<%
              out.print("Reato N."+lProgressivo+": ");
%>
            </font>
<%
          }
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
<%
  //Se il reato è quello principale
  if(reato.getProgrCircostanza() == 1)
  {
%>
    <table cellspacing=2 cellpadding=2 width="95%">
<%
    if(reato.getDescrTipoReato() != null && !reato.getDescrTipoReato().equals("") && !reato.getDescrTipoReato().equals("-"))
    {
%>
      <tr>
				<td class="l" width="20%">Tipo Reato</td>
        <td class="l" width="75%" > <font class="campo"><%=StringUtils.toStringJSP(reato.getDescrTipoReato())%>&nbsp;</font></td>
       </tr>
<%
    }
    if(reato.getDescLuogo() != null && !reato.getDescLuogo().equals(""))
    {
%>
      <tr>
        <td class="l">Luogo</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(reato.getDescLuogo())%>&nbsp;</font></td>
        </tr>
<%
    }
    if(reato.getCodPeriodoConsumazione() != null && !reato.getCodPeriodoConsumazione().equals("") && !reato.getCodPeriodoConsumazione().equals("-"))
    {
%>
      <tr>
        <td class="l">Periodo Consumazione</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(reato.getStringaConsumazione())%>&nbsp;</font></td>
        </tr>
 <%}
  if(reato.getNote() != null && !reato.getNote().equals(""))
    {
%>
      <tr>
        <td class="l"><font class="label">Note</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(reato.getNote())%>&nbsp;</font></td>
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