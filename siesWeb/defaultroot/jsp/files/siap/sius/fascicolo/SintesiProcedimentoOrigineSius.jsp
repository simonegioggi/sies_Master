<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="fascicolo_origine"   scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<%
  SoggettoModel soggetto = fascicolo_origine.getFascicoloSiusModel().getSoggetto();
  // Flag che indica la presenza del fascicolo origine;
  boolean isFascicoloOrigine = (fascicolo_origine != null && fascicolo_origine.getFascicoloSiusModel() != null && fascicolo_origine.getFascicoloSiusModel().getIdFascicoloSius() != null) ? true : false;
 if(isFascicoloOrigine)
{
%>

<html>
<head>
<title>[S.I.E.S.] - Sintesi Procedimento Origine SIUS</title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

</head>

  <body class="corpo">

  <table cellspacing=0 cellpadding=0 width=95%>
<%
String lTitolo = "Procedimento di riferimento";

  if (request.getParameter("Titolo") != null)
{
    lTitolo = request.getParameter("Titolo");
 } %>
    <tr>
        <td class="Titolo" colspan=6 > <%=lTitolo%> </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">Procedimento N.</font>
          <%=fascicolo_origine.getFascicoloSiusModel().getChiaveAnno()%>
          /
          <%=fascicolo_origine.getFascicoloSiusModel().getChiaveProgr()%>&nbsp;
          &nbsp;<%=fascicolo_origine.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicolo_origine.getFascicoloSiusModel().getDescrComuneUfficio()%>&nbsp;-&nbsp;
        <font class="label"> relativo a: </font>
        <font class="campo"><%=fascicolo_origine.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font>
       </td>
    </tr>
    <tr>
      <td class="L" width=80%><font class="label">Soggetto:</font>
      <font class="campo">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
      </font>&nbsp;
<%
        if (soggetto.getSesso().compareTo("F")==0)
        {
%>
          <font class="label">nata il :</font>&nbsp;
<%
        }
        else
        {
%>
          <font class="label">nato il :</font>&nbsp;
<%
        }
%>
      <font class="campo"><%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
      <font class="label">in : </font>
      <font class="campo">
<%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {
%>
        <%=soggetto.getDescrStatoNascita()%>
<%
      }
      else
      {
%>
        <%=soggetto.getDescrComuneNascita()+ "  ("+soggetto.getCodProvinciaNascita()+")" %>
<%
      }
%>
      </font>
     </td>
    </tr>

    <tr>
      <td class="L">
        <font class="label">Data Udienza : </font>
        <font class="campo">
<%
        if (fascicolo_origine.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("10") == 0 )
        {%>
          <%=fascicolo_origine.getFascicoloSiusModel().getDescrStatoFascicolo()%>
<%      } else {%>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString( fascicolo_origine.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd-MM-yyyy"), "-" )%>
<%      }%>
        </font>
        <%if( fascicolo_origine.getUdiPro()  != null && fascicolo_origine.getUdiPro().getFlagRinviata() != null && fascicolo_origine.getUdiPro().getFlagRinviata().equalsIgnoreCase("P"))
         {
%>
          <font class="cRosso">  ( Prefissata ) </font>
<%
         }
         %>
      </td>
    </tr>

  </table>
</body>
</html>
<% } %>