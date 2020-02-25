<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>


<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.jms.ICostantiJMS"%>


<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>


<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%// per ora non utilizzati%>
<jsp:useBean id="flagIncludeInCarico" scope="request" class="java.lang.String"/>
<jsp:useBean id="data1" scope="request" class="java.lang.String"/>
<jsp:useBean id="data2" scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form per la visualizzazione dei messaggio di presa in carico atti
// (trasmissione x competenza cumulo)
//==============================================================================
%>

<html>
  <head>
    <title>[S.I.E.S.] - Riscontro Trasmissioni/Solleciti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  </head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : </font><font class="campo">Riscontro Trasmissioni/Solleciti</font>&nbsp;&nbsp;
     </td>
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG"><!-- Tasto indietro alla Griglia Della Gestione Cumulo -->
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActIstruttorieGriglia">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>


  <br>  
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <br>
  
<%
 if (Messaggi.size() == 0)
{
%>
<br>
<table>
  <td class="label">    Nessun Elemento trovato ! </td>
</table>
<%
} else {
%>

<div align="left">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Anno/Numero&nbsp;<br>SIEP</td>
      <td class="int">Ufficio Destinatario Atti&nbsp;</td>
      <td class="int">Tipo operazione&nbsp;</td>
      <td class="int">Data Trasmissione&nbsp;<br>Esito</td>
      <td class="int">Esito&nbsp;</td>
      <td class="int">Motivazioni&nbsp;</td>
      <td class="int">Data Ultimo Sollecito&nbsp;</td>
      <td class="int">Azioni&nbsp;</td>
    </tr>
<%
  String coloreLinea = "c"; // STUB 15/03/2005
  Iterator itx = Messaggi.iterator();
  while ( itx.hasNext())
  {
    MessaggioModel lMess = (MessaggioModel)itx.next();
    // STUB 15/03/2005
    coloreLinea = "c";
   // if (lMess.getFlagVisto().compareTo("S")==0)
   //   coloreLinea = "cVerde";

%>
  <tr>
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>     
    <td class="<%=coloreLinea%>"><%= lMess.getDescrUfficioMittente() + " " + lMess.getDescrSedeUfficioMittente()%></td>     
    <td class="<%=coloreLinea%>"><%= lMess.getDescrTipoOperazione()%></td>     
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm"))%></td>
    
<%	if(lMess.getCodEsito()!=null && lMess.getCodEsito().compareTo("01001")==0 )	// Atti Presi in carico
	{%>    
		<td class="cVerde"><%= lMess.getDescrEsito()%></td>
<%	}
	else if(lMess.getCodEsito()!=null && lMess.getCodEsito().compareTo("01007")==0 )	// Atti Rigettati
	{%>
		<td class="cRosso"><%= lMess.getDescrEsito()%></td>
<%	}
	else	// Atti Trasmessi o Restituiti
	{%>
      <td class="<%=coloreLinea%>"><%= lMess.getDescrEsito()%></td>
<%	} %>
      
      <td class="<%=coloreLinea%>"><%if(lMess.getNote()!=null){%><%=lMess.getNote()%><%} %>&nbsp;</td>

<%	if(lMess.getDataUltimoSollecito()!=null )
	{%>      
      <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataUltimoSollecito(),"dd-MM-yyyy HH:mm"))%></td>
<%	}
	else
	{	%>
		<td class="<%=coloreLinea%>"> &nbsp; </td>
<%	} %>	      
      <td class="<%=coloreLinea%>">
        <%--jsp:include page="<%=IWebConstants.PG_BUTTONS%>"--%>
        <jsp:include page="<%=ICostantiMessaggio.PG_BUTTONS_MESSAGGIO%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lMess.getIdMessaggio()%>" />
          <jsp:param name="CodTipoOperazione" value="<%=lMess.getCodTipoOperazione()%>" />
        </jsp:include>&nbsp;
    </td>
  </tr>
<%
  }
%>
    </table>
  </div>
<% } %>
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
  </body>
</html>