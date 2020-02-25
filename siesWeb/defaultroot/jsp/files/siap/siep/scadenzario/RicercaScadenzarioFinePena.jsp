<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.lang.String" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.List" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario" %>
<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel" %>
<%@ page import="siap.siep.scadenzario.util.ScadenzarioUtils" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="tipo" scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Consultazione Scadenzario Fine Pena</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;
        <font class="campo">Consultazione Scadenzario Fine Pena - <%=titolo%></font></td>
      </tr>
    </table>
    <br>
    <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
    <br>
    <br>
    <table cellpadding=2 cellspacing=2>
<%
	 List scadenzario =(List) request.getAttribute("scadenzario");
      if(tipo.equals("Tutti"))
      {
%>
        <tr>
          <td>&nbsp;<td>
          <td>&nbsp;<td>
          <td align="right"><img src="/images/QuadratinoVerde.gif"></td><td class="campo">In Scadenza</td>
          <td align="right"><img src="/images/QuadratinoRosso.gif"></td><td class="campo">In Scadenza Oggi</td>
          <td align="right"><img src="/images/QuadratinoGrigio.gif"></td><td class="campo">Scaduto</td>
        </tr>
<%
      }
%>
      <tr>
        <td class="int">N° SIEP</td>
        <td class="int">Cognome</td>
        <td class="int">Nome</td>
        <td class="int">Luogo Nascita</td>
        <td class="int">Data Nascita</td>
        <td class="int">Data Inizio Pena</td>
<%
        if(!tipo.equals("oggi"))
        {
%>
          <td class="int">Data di Fine Pena</td>
<%
          if(!tipo.equals("scaduto"))
          {
%>
            <td class="int">N° Giorni Residui</td>
<%
          }
        }
%>
        <td class="int">Visto</td>
        <td class="int">Azioni</td>
     </tr>
<%
    Iterator itx = scadenzario.iterator();
    while ( itx.hasNext())
    {
      ScadenzarioModel lSca = (ScadenzarioModel)itx.next();
      FascicoloSiepModel lFas = lSca.getFascicoloModel();
      SoggettoModel lSog = lFas.getSoggetto();
%>
    <tr>
<%
    if(tipo.equals("Tutti"))
    {
      if(  lSca != null
        && lSca.getGiorniResidui() != null
        && lSca.getGiorniResidui().intValue()== 0)
      {
%>

          <td class=crosso>
           <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
            <%=lFas.getChiaveAnno()%>
            /
            <%=lFas.getChiaveProgr()%>
           </a>
          </td>
          <td class=crosso><%=StringUtils.toStringJSP(lSog.getCognome())%>&nbsp;</td>
          <td class=crosso><%=StringUtils.toStringJSP(lSog.getNome())%>&nbsp;</td>
          <td class=crosso><%=StringUtils.toStringJSP(lSog.getDescrComuneNascita())%>&nbsp;</td>
          <td class=crosso><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSog.getDataNascita(),"dd/MM/yyyy")) %>&nbsp;</td>
          <td class=crosso><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>
          <td class=crosso><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>
          <td class=crosso nowrap><%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(),DateUtils.getSysDate())%>&nbsp;</td>
<%
      }
      else
      {
        if( lSca != null
         && lSca.getGiorniResidui() != null
         && lSca.getGiorniResidui().intValue() <= 7
         && lSca.getGiorniResidui().intValue() > 0)
        {
%>
          <td class=cverde>
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
              <%=lFas.getChiaveAnno()%>
              /
              <%=lFas.getChiaveProgr()%>
            </a>
          </td>
          <td class=cverde><%=StringUtils.toStringJSP(lSog.getCognome())%>&nbsp;</td>
          <td class=cverde><%=StringUtils.toStringJSP(lSog.getNome())%>&nbsp;</td>
          <td class=cverde><%=StringUtils.toStringJSP(lSog.getDescrComuneNascita())%>&nbsp;</td>
          <td class=cverde><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSog.getDataNascita(),"dd/MM/yyyy") )%>&nbsp;</td>
          <td class=cverde><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>
          <td class=cverde><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>
          <td class=cverde nowrap><%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(),DateUtils.getSysDate())%>&nbsp;</td>
<%
        }
        else
        {
          if(  lSca != null
            && lSca.getGiorniResidui() != null
            && lSca.getGiorniResidui().intValue() < 0)
          {
%>
            <td class=cgrigio>
              <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
                <%=lFas.getChiaveAnno()%>
                /
                <%=lFas.getChiaveProgr()%>
              </a>
            </td>
            <td class=cgrigio><%=StringUtils.toStringJSP(lSog.getCognome())%>&nbsp;</td>
            <td class=cgrigio><%=StringUtils.toStringJSP(lSog.getNome())%>&nbsp;</td>
            <td class=cgrigio><%=StringUtils.toStringJSP(lSog.getDescrComuneNascita())%>&nbsp;</td>
            <td class=cgrigio><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSog.getDataNascita(),"dd/MM/yyyy") )%>&nbsp;</td>
            <td class=cgrigio><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>
            <td class=cgrigio><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>
            <td class=cgrigio nowrap><%=ScadenzarioUtils.getDifferenza(DateUtils.getSysDate(),lSca.getDataFineScadenza())%>&nbsp;</td>
<%
          }
          else
          {
%>
            <td class=C>
              <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
                <%=lFas.getChiaveAnno()%>
                /
                <%=lFas.getChiaveProgr()%>
              </a>
            </td>
            <td class=C><%=StringUtils.toStringJSP(lSog.getCognome())%>&nbsp;</td>
            <td class=C><%=StringUtils.toStringJSP(lSog.getNome())%>&nbsp;</td>
            <td class=C><%=StringUtils.toStringJSP(lSog.getDescrComuneNascita())%>&nbsp;</td>
            <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSog.getDataNascita(),"dd/MM/yyyy")) %>&nbsp;</td>
            <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>
            <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>
<%
            if(lSca != null && lSca.getDataFineScadenza() != null)
            {
%>
              <td class=C><%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(), DateUtils.getSysDate())%>&nbsp;</td>
<%
            }
            else
            {
%>
              <td class=C>&nbsp;</td>
<%
            }
          }
        }
      }
    }// FINE TUTTI
    else
    {
%>
        <td class=C>
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
            <%=lFas.getChiaveAnno()%>
            /
            <%=lFas.getChiaveProgr()%>
          </a>
        </td>
        <td class=C><%=StringUtils.toStringJSP(lSog.getCognome())%>&nbsp;</td>
        <td class=C><%=StringUtils.toStringJSP(lSog.getNome())%>&nbsp;</td>
        <td class=C><%=StringUtils.toStringJSP(lSog.getDescrComuneNascita())%></td>
        <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSog.getDataNascita(),"dd/MM/yyyy")) %>&nbsp;</td>
        <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>
<%
        if(!tipo.equals("oggi"))
        {
%>
          <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>
<%
          if(!tipo.equals("scaduto"))
          {
%>
            <td class=C><%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(),DateUtils.getSysDate())%>&nbsp;</td>
<%
          }
        }
      }

      if(  lSca != null
        && lSca.getFlagVisto() != null
        && lSca.getFlagVisto().equals("S") )
      {
%>
        <td class=C><img src="/images/TickRed.gif"> </td>
<%
      }
      else
      {
%>
        <td class=C> &nbsp;</td>
<%
      }
%>
      <td class=C>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiScadenzario.CAMPO_ID_SCADENZARIO%>"/>
          <jsp:param name="ValoreIdEntita" value="<%=lSca.getIdScadenzario()%>"/>
        </jsp:include>
      </td>
    </tr>
<%
    }
%>
    </table>
  </FORM>
</body>
</html>