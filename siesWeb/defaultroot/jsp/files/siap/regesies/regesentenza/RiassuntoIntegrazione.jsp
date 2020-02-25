<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.circostanza.model.CircostanzaModel" %>
<%@ page import="siap.siep.notiziareato.model.NotiziaReatoModel" %>

<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel" %>
<%@ page import="siap.regesies.regereato.model.RegeReatoCircostanzaModel"%>
<%@ page import="siap.regesies.regecircostanza.model.RegeCircostanzaModel" %>
<%@ page import="siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel"%>

<%@ page import="siap.regesies.regesentenza.model.ProvvedimentoModel"%>
<%@ page import="siap.regesies.regesentenza.model.ProvvedimentoSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza"%>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="provvedimento" scope="request" class="siap.regesies.regesentenza.model.ProvvedimentoModel"/>

<html>

  <head>
    <title> [S.I.E.S.] -Riassunto Integrazione Dati da ReGe in SIEP- </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>">
    </script>
  </head>

  <BODY class="corpo">

  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione:</font>&nbsp;
          <font class="campo">Integrazione Dati da ReGe in SIEP</font>
        </td>
       <td class="LBG">
          <jsp:include page="<%=ICostantiRegeSies.PG_TOOLBAR_REGE_HEADER%>">
             <jsp:param name="TornaIndietro" value="SI"/>
            </jsp:include>
        </td>
      </tr>
    </table>
  </FORM>

 <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <br>

<%
String ESITO_POSITIVO = siap.regesies.regesentenza.action.ICostantiRegeSentenza.ESITO_POSITIVO;
ProvvedimentoModel lProvv = provvedimento;
SentenzaModel sentenza = lProvv.getSentenza();
  String finale;
  if(sentenza.getCodTipoProvvedimento().equals("01"))
      {
        finale="a";
      }
      else
      {
        finale="o";
      }

String isVALIGN = "top";
String isBorder = "0";
String lWidth = "96%";
String largh = "8%";
String resto = "92%";

%>
<table width="100%">
<% if (lProvv.isResidenzeCheck())
   {%>
   <tr>
      <td class="LBGISI" width="9%">Residenza</td>
      <td> <jsp:include  page="/jsp/files/siap/regesies/regeresidenza/ElencoResidenzeInclude.jsp"/>
      </td>

    </tr>

    <%}
  if (lProvv.isReatoCheck())
   {%>
   <tr>
      <td class="LBGISI" width="9%">Reati <br>da inserire</td>
      <td>
   <jsp:include page="/jsp/files/siap/regesies/regereato/ElencoReatoInclude.jsp"/>
   </td>
  </tr>
  <%}%>
    <%if (lProvv.isCircostanzaCheck())
   {%>
    <tr>
    <td class="LBGISI" width="9%">Circostanze da inserire</td>
    <td>
  <jsp:include page="/jsp/files/siap/regesies/regecircostanza/ElencoCircostanzeInclude.jsp"/>
  </td>
    </tr>
    <%}
  if (lProvv.isNotiziaReatoCheck())
   {%>
    <tr>
      <td class="LBGISI" width="9%">Notizie di Reato <br>da inserire</td>
     <td>
  <jsp:include page="/jsp/files/siap/regesies/regenotiziareato/ElencoNotizieDiReatoInclude.jsp"/>
 </td>
 </tr>
 <%}
    if (lProvv.isDifensoriCheck())
   {%>
    <tr>
      <td class="LBGISI" width="9%">Difensori</td>
      <td>

    <jsp:include page="/jsp/files/siap/regesies/regeavvocato/ElencoAvvocatoInclude.jsp"/>
    </td>
  </tr>
  <%}%>

<%
  if (lProvv.isDispositivoCheck())
    {%>
    <tr>
      <td class="LBGISI" width="9%">Dispositivo</td>
       <td>
      <table cellspacing=1 cellpadding=1  width="50%">
      <tr><td>
        <font class="campo"><%=StringUtils.toStringJSP(lProvv.getRegeSentenza().getNotaDispositivo())%></font>&nbsp;
      </td></tr></table>
        </td>
    </tr>
  <%}%>
  </table>


  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettaglioProvvedimentoRege"
  onsubmit="javascript:document.DettaglioProvvedimentoRege.R.disabled=true;">
  <table>
  <tr>
   <td colspan=2>
 	      <br>
        <input  class="bottone" type="submit" name="R" value="Integra Procedimento SIEP">
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.regesies.regesentenza.action.ActIntegraFascicolo">
      </td>
  </tr>
  </table>
  </form>


  </body>

</html>