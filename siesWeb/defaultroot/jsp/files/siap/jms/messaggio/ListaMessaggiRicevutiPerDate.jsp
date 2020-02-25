<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="DataInizioTrasmissioneAtti" scope="request" class="java.lang.String"/>
<jsp:useBean id="DataFineTrasmissioneAtti" scope="request" class="java.lang.String"/>
<%-- // STUB 15/03/2005 Parametri per la visualizzazione dei criteri di ricerca --%>
<jsp:useBean id="codUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="flagIncludeInCarico" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Messaggi Ricevuti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>
</head>

  <body class="corpo">
 <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : </font><font class="campo">Lista Atti ricevuti &nbsp;&nbsp;
<%      if (!DataInizioTrasmissioneAtti.equals("01/01/1900") &&
             DataInizioTrasmissioneAtti.length() > 1 )
        {
%>
          dal <%=DataInizioTrasmissioneAtti%>  <%;
        }
        if (DataFineTrasmissioneAtti.length() > 1)
        {
%>        al <%=DataFineTrasmissioneAtti%>&nbsp;&nbsp;
      <%}%>
      </font>
     </td>
      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
  <br>
  <%-- Visualizzazione Criteri di Ricerca --%>
<%
  if(codUfficio.length()>1 || flagIncludeInCarico.length()>0 )
  {%>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
    </tr>
<%
      if(codUfficio.length()>1 )
      {%>
        <tr>
          <td class="lVerdeNB">Ufficio di provenienza : <%=descrUfficio.toUpperCase()%></td>
        </tr>
<%    }
      if(flagIncludeInCarico.equals("S"))
      {
%>
        <tr>
          <td class="lVerdeNB">Visualizza anche gli atti gia presi in carico (in Verde) </td>
        </tr>
<%    }
    }%>

  </table>

  <div align="left">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Anno/Numero SIEP</td>
      <td class="int">Anno/Numero SIUS</td>
      <td class="int">Anno/Numero SIEPE</td>
      <td class="int">Data Arrivo</td>
      <td class="int">Tipo Operazione</td>
      <td class="int">Ufficio Mittente</td>
      <td class="int">Azioni</td>
    </tr>
<%
  String coloreLinea = "c"; // STUB 15/03/2005
  Iterator itx = Messaggi.iterator();
  while ( itx.hasNext())
  {
    MessaggioModel lMess = (MessaggioModel)itx.next();
    // STUB 15/03/2005
    coloreLinea = "c";
    if (lMess.getFlagVisto().compareTo("S")==0 &&
        flagIncludeInCarico.compareTo("S")==0 )
      coloreLinea = "cVerde";
%>
    <tr>
      <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
      <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSius())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSius())%></td>
      <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiepe())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiepe())%></td>
      <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
      <td class="<%=coloreLinea%>"><%= lMess.getDescrTipoOperazione()%>&nbsp;</td>
      <td class="<%=coloreLinea%>"><%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%></td>
      <td class="<%=coloreLinea%>">
        <%--jsp:include page="<%=ICostantiMessaggio.PG_BUTTONS_DETTAGLI%>"--%>
        <jsp:include page="buttonsMessaggi.jsp">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lMess.getIdMessaggio()%>" />
          <jsp:param name="CodTipoOperazione" value="<%=lMess.getCodTipoOperazione()%>" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
    </table>
  </div>
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
        </body>
</html>