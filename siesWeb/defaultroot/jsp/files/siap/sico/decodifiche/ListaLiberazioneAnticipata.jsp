<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="java.util.ListIterator" %>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.util.DateUtils" %>

<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%--@ page import="siap.sico.decodifiche.model.DecodificheModel" --%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel" %>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata" %>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc" %>

<jsp:useBean id="licenze" scope="request" class="java.util.ArrayList" />

<%
  boolean lFlagPopUp = false;
  if( (request.getParameter("PopUp") != null)&& (request.getParameter("PopUp").equals("Y")) )
  {
    lFlagPopUp = true;
  }
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Liberazioni Anticipate</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">

      function Verify(id, elaborato, conProvvValidato)
      {
        if( elaborato=='S' || conProvvValidato == 'true')
        {
          alert("Liberazione Anticipata già elaborata");
        }
        else
        {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sico.libertaanticipata.action.ActDettaglioLiberazioneAnticipata";
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>.value=id;

          window.parent.opener.document.<%=request.getParameter("formname")%>.bottConferma.disabled=true;
          window.parent.opener.document.<%=request.getParameter("formname")%>.submit();

          window.parent.close();
        }
      }

      function controlla()
      {
        if(document.elenco.numeroLiberazioni.value==0)
        {
          alert("Nessuna Liberazione Anticipata Presente");

          window.parent.close();
        }
      }

    </script>

  </head>

  <body class="corpo" onload="controlla();">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <input type="hidden" name="numeroLiberazioni" value="<%=licenze.size()%>">
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
        </td>
        <td class="LBG">
          <font class=label>Funzione :</font>
          <font class=campo>Elenco Liberazioni Anticipate</font>
        </td>
      </tr>
    </table>
    <br>
    <table>
      <tr>
        <td class="int">Descrizione</td>
        <td class="int">Anno Sius</td>
        <td class="int">Numero Sius</td>
        <td class="int">Autorità Emittente</td>
        <td class="int">Data Emissione Ordinanza</td>
<!--
        <td class="int">Giorni concessi</td>
-->
        <td class="int" width=5%>Azioni</td>
      </tr>
<%
  if(licenze.size()>0)
  {
    BigDecimal lIdlicenza = ((LicenzaLibAnticipataModel)licenze.get(0)).getIdLicenzaLibanticipata();

    Iterator itx = licenze.iterator();
    for (int i = 0; itx.hasNext(); i++)
    {
      LicenzaLibAnticipataModel licenzeMod = (LicenzaLibAnticipataModel)itx.next();

      // Confronto per evitare ripetizioni licenze appartenenti alla stessa ordinanza
      boolean lRipetizione = false;
      for(int j = 0; j < i && !lRipetizione; j++)
      {
        LicenzaLibAnticipataModel licVecchia = (LicenzaLibAnticipataModel)licenze.get(j);
        if (licVecchia.getEveIdEvento() != null && licenzeMod.getEveIdEvento() != null && licVecchia.getEveIdEvento().equals(licenzeMod.getEveIdEvento()))
          lRipetizione = true;
      }

      if (!lRipetizione)
      {
%>
        <tr>
          <td class="l"><%=StringUtils.toStringJSP(licenzeMod.getDescrTipoLicenza(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(licenzeMod.getAnnoSius(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(licenzeMod.getNumeroSius(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(licenzeMod.getDescrUfficioEmittente(),"-")%> di <%=StringUtils.toStringJSP(licenzeMod.getDescrLuogoEmittente(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(licenzeMod.getDataEmissioneOrdinanza(),"dd-MM-yyyy"),"-")%></td>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
          <td class="l">< %=StringUtils.toStringJSP(licenzeMod.getNumeroGiorni(),"-")%></td>
--%>
          <input type="HIDDEN" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>" value="<%=licenzeMod.getIdLicenzaLibanticipata()%>">
          <input type="HIDDEN" name="FlagElaborato" value="<%=StringUtils.toStringJSP(licenzeMod.getFlagElaborato())%>">

          <td class="c">
            <a href="Javascript:Verify(<%=licenzeMod.getIdLicenzaLibanticipata()%>,'<%=StringUtils.toStringJSP(licenzeMod.getFlagElaborato())%>', '<%=licenzeMod.isConProvvedimentoValidato()%>');">
              <img align="middle" src="/images/fileselected.gif" border=0>
            </a>
<%
            if( "S".equals(licenzeMod.getFlagElaborato())
                || licenzeMod.isConProvvedimentoValidato()
               )
            {
%>
              <font class="cRosso"> Elaborato </font>
<%
            }
%>
          </td>
        </tr>
<%
      } // endif ripetizione
    }
  }
%>
    </table>
  </form>
  </body>
</html>