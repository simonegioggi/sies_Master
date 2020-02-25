<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<%@ page import="siap.regesies.regereato.action.ICostantiRegeReato" %>
<%@ page import="siap.regesies.regereato.model.RegeReatoCircostanzaModel" %>
<%@ page import="siap.regesies.regereato.model.RegeReatoModel" %>
<%@ page import="siap.regesies.action.ICostantiRegeSies" %>

<jsp:useBean id="regereati" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Reato</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>
<body class="corpo">
<form>
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Rege Reati</font></td>
        </td>
    </tr>
  </table>
</form>
  <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/></td>
<div align=center>
<table >
    <tr><td class=titolo colspan=13>Rege Reati</td></tr>
    <tr>
      <td class="int">N. Reato</td>
      <td class="int">Tipo Reato</td>
      <td class="int">Data Reato</td>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Art.Qual.</td>
      <td class="int">Comma</td>
      <td class="int">Lettera</td>
      <td class="int">Numero</td>
      <td class="int">Luogo</td>
      <td class="int" width=5%>Azioni</td>
   </tr>
 <%
     if(regereati!=null && regereati.size()>0)
    {
    Iterator lIterReati = regereati.iterator();

    while(lIterReati.hasNext())
      {
          RegeReatoCircostanzaModel lReatoCircostanza = (RegeReatoCircostanzaModel)lIterReati.next();
          RegeReatoModel lReato = lReatoCircostanza.getReato();
          RegeReatoModel[] lCircostanze = lReatoCircostanza.getCircostanze();
%>     <tr>
       <td class="l">
<%                 //REATO
      if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
      {%>
        <font class="label"><%=lReato.getProgrNumeroManuale()%></font>
<%    }
    else
      {%>
      <font class="label"><%=lReato.getProgrReato()%></font>
<%    }            %></td>
<td class="l"><%=lReato.getDescrTipoReato()%>&nbsp;</td>
      <td class="l"><%=lReato.getDataReatoCompleta()%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(lReato.getDescrFonte(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.intZerotoString(lReato.getAnnoFonte(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(lReato.getNumeroFonte(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(lReato.getArticolo(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(lReato.getDescrSottonumerazione(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(lReato.getComma(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(lReato.getLettera(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(lReato.getNumero(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(lReato.getDescLuogo(),"-")%>&nbsp;</td>
      <td class="c">
          <jsp:include page="<%=ICostantiRegeSies.PAGE_BUTTONS_REGE%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeReato.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=lReato.getIdFile()%>" />
             <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiRegeReato.CAMPO_PROGR_REATO%>" />
             <jsp:param name="ValoreIdEntitaProvv" value="<%=lReato.getProgrReato()%>" />
             <jsp:param name="ChiaveTre" value="<%=ICostantiRegeReato.CAMPO_PROGR_CIRCOSTANZA%>" />
             <jsp:param name="ValoreTre" value="<%=lReato.getProgrCircostanza()%>" />
          </jsp:include>
      </td>


<%
                  //CIRCOSTANZE
if(lCircostanze != null)
 {
    RegeReatoModel lCirc = null;

    for(int i=0; i<lCircostanze.length; i++)
    {
       lCirc = lCircostanze[i];%>
      <tr>
      <td class="l"></td>
      <td class="l"></td>
      <td class="l"></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getDescrFonte(),"-")%></td>
      <td class="l"><%=StringUtils.intZerotoString(lCirc.getAnnoFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getNumeroFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getArticolo(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getDescrSottonumerazione(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getComma(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getLettera(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getNumero(),"-")%></td>
      <td class="l">&nbsp;</td>
      <td class="c">
          <jsp:include page="<%=ICostantiRegeSies.PAGE_BUTTONS_REGE%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeReato.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=lCirc.getIdFile()%>" />
             <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiRegeReato.CAMPO_PROGR_REATO%>" />
             <jsp:param name="ValoreIdEntitaProvv" value="<%=lCirc.getProgrReato()%>" />
             <jsp:param name="ChiaveTre" value="<%=ICostantiRegeReato.CAMPO_PROGR_CIRCOSTANZA%>" />
             <jsp:param name="ValoreTre" value="<%=lCirc.getProgrCircostanza()%>" />
          </jsp:include>
      </td>

     </tr>
<%
        }
       }
      }
    }%>
  </body>
</html>