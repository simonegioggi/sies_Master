<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>

<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="datiFinaliAggregatoModel" scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>


<%
DatiFinaliCumuloModel datiFinaliCumulo = datiFinaliAggregatoModel.getDatiFinaliCumulo();

%>

<html>
<head>
  <title> [S.I.E.S.] - Dati Finali Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">    
  
    function loadModifica (azione) {
      document.f.<%=IWebConstants.ACTION_FIELD%>.value = azione;
      document.f.<%=ICostantiModuloCumulo.MODALITA%>.value = "<%=ICostantiModuloCumulo.MODALITA_MODIFICA%>";
      document.f.submit();
    }
  
    function eseguiNavigazione (azione) {
      document.f.<%=IWebConstants.ACTION_FIELD%>.value = azione;
      document.f.submit();
    }
  </script>
</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dati Finali Cumulo</font>
      </td>
      <% if (IstruttoriaCumulo.getFlagStato().equals("A") ){ %>
      <td class="LBG">
        <a href="javascript:loadModifica('siap.siep.modulocumulo.action.ActLoadInserisciDatiFinaliCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
      </td>
      <% } %>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/NavigazioneDatiFinaliCumulo.jsp"/>
  <br>

<div id="divPosizionamento" align="left" style="padding-left: 25px;">    
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActDettaglioPeneRideterminate">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA%>" value="">
    
  <table width="70%">
    <tr>
      <td class="l">Provvedimento di Determinazione delle Pene Concorrenti emesso in data: <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(datiFinaliCumulo.getDataProvvedimento(),"dd-MM-yyyy"))%> &nbsp; </font></td>      
    </tr>
  </table>

  <% if ("03".equals(datiFinaliCumulo.getTipoUfficioEmissione())) { %>
  <table width="70%">
    <tr><td colspan=3 class="Titolonocap">Decisione del Giudice dell' Esecuzione</td></tr>
    <tr>
      <td class="l">Anno/Numero Provvedimento:</td>      
      <td class="l">
         <font class="campo"><%=StringUtils.toStringJSP(datiFinaliCumulo.getAnnoProvvedimento()) %>/<%=StringUtils.toStringJSP(datiFinaliCumulo.getNumeroProvvedimento()) %></font>
      </td>      
      <td class="l">
        <font class="label">emesso in data: </font> &nbsp;&nbsp; <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(datiFinaliCumulo.getDataProvvedimento(),"dd-MM-yyyy"))%>&nbsp; </font>
      </td>
    </tr>

    <tr>
      <td class="l">Ufficio Emittente:</td>
      <td class="l" colspan=2>
        <font class="campo"> <%=StringUtils.toStringJSP(datiFinaliCumulo.getDescrTipoUfficioEmittente()) %>&nbsp; </font>
      </td>
    </tr>
    <tr>
      <td class="l">Sede:</td>
      <td class="l" colspan=1>
        <font class="campo"> <%=StringUtils.toStringJSP(datiFinaliCumulo.getDescrLuogoUfficioEmittente()) %>&nbsp; </font>
      </td>
    </tr>
    <tr>
      <td class="l">Sezione Autorità Emittente:&nbsp;</td>
      <td class="l" colspan=1>
        <font class="campo"> <%=StringUtils.toStringJSP(datiFinaliCumulo.getSezioneUfficioEmittente()) %>&nbsp; </font>
      </td>
    </tr>
  </table>
  <% } %>
  
  <!--table>
    <tr>
      <td>
        <input type="submit" class="bottone" name="Avanti" value="Avanti">
      </td>
    </tr>
  </table-->
  
</form>
</div>
</body>

</html>