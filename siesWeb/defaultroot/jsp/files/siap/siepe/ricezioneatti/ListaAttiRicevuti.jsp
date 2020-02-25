<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siepe.ricezioneatti.action.ICostantiRicezioneAtti"%>

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
      <font class="label">Funzione : </font><font class="campo">Lista Atti ricevuti</font>
     </td>
      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
  <%-- Visualizzazione Criteri di Ricerca --%>
    <jsp:include page="<%=ICostantiRicezioneAtti.PG_CRITERI_DI_RICERCA%>"/>


  <div align="left">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Anno/Numero SIEP</td>
      <td class="int">Anno/Numero SIUS</td>
      <td class="int">Anno/Numero SIEPE</td>
      <td class="int">Data Arrivo</td>
      <td class="int">Tipo Atto</td>
      <td class="int">Stato</td>
      <td class="int">Ufficio Mittente</td>
      <td class="int">Soggetto</td>
      <td class="int">Azioni</td>
    </tr>
<%
  Iterator itx = Messaggi.iterator();
  while ( itx.hasNext())
  {
  	String descStato = "Ricevuto";
    MessaggioModel lMess = (MessaggioModel)itx.next();
    if (lMess.getFlagVisto().compareTo("S")==0 )
        descStato = "Preso in Carico";
    else if (lMess.getFlagVisto().compareTo("V")==0 )
        descStato = "Preso in Visione";
    else if (lMess.getFlagVisto().compareTo("R")==0 )
        descStato = "Restituito";
%>
    <tr>    	
      <td class="c"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
      <td class="c"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSius())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSius())%></td>
      <td class="c"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiepe())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiepe())%></td>
      <td class="c"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
      <td class="c"><%= lMess.getDescrTipoOperazione()%>&nbsp;</td>
      <td class="c"><%= descStato%>&nbsp;</td>
      <td class="c"><%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%></td>
      <td class="c"><%= StringUtils.toStringJSP(lMess.getCognomeSoggetto(),"-") +" "+ StringUtils.toStringJSP(lMess.getNomeSoggetto(),"-")%></td>
     	<td class="c">
      	<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
        	<jsp:param name="CampoIdEntita" value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lMess.getIdMessaggio()%>" />
          <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiMessaggio.CAMPO_TIPO_OPERAZIONE%>" />
          <jsp:param name="ValoreIdEntitaProvv" value="<%=lMess.getCodTipoOperazione()%>" />
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