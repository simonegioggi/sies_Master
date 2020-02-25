<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel" %>
<%@ page import="siap.sico.ufficio.util.UfficioAccorpatoUtils" %>

<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>

<%-- // STUB 15/03/2005 Parametri per la visualizzazione dei criteri di ricerca --%>

<jsp:useBean id="codUfficio"   scope="request" class="java.lang.String"/>
<jsp:useBean id="descrUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="data1" scope="request" class="java.lang.String"/>
<jsp:useBean id="data2" scope="request" class="java.lang.String"/>
<jsp:useBean id="CodEsito" scope="request" class="java.lang.String"/>
  
  
  
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String"/>
<jsp:useBean id="flagIncludeInCarico" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
//==============================================================================
// jsp utilizzata per la visualizzazione del risultato della ricerca dei 
// messaggi ricevuti per competenza e presi in carico/retituiti
// n.b. SOLO per questo caso
//==============================================================================
%>

<html>
  <head>
    <title>[S.I.E.S.] - Messaggi Ricevuti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    
    <script language="JavaScript">
    function RestituisciFascicolo(aIdMessaggio)
    {
      var msgConfirm = "Il Titolo selezionato verrà restituito all'ufficio di Origine \n che ne riprenderà la piena titolarità "; 
      if (window.confirm(msgConfirm)) {
        lAzione = "siap.siep.istruttoriacumulo.action.ActLoadRestituzioneFascicolo";
        document.formRestituzione.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formRestituzione.<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>.value = aIdMessaggio;
        document.formRestituzione.submit();
      }
    }    
    </script>
    
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <% if ("01001".equals(CodEsito)) { %>
        <font class="label">Funzione : </font><font class="campo">Lista Atti per Competenza Presi in Carico </font>&nbsp;&nbsp;
        <% } else { %>
        <font class="label">Funzione : </font><font class="campo">Lista Atti per Competenza Restituiti </font>&nbsp;&nbsp;
        <% } %>
      </td>
      <!-- BOTTONE DI RITORNO -->

      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaPresiCarico">
          <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
        
    </tr>

  </table>
  <br>

	<%--	PARAMETRI PER LA PAGINAZIONE --%>
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.presaincarico.action.ActRicercaProvvedimentiRicevuti">
  <input type="hidden" name="<%=IWebConstants.NUM_PAGE%>" value="1">  
  <%-- Visualizzazione Criteri di Ricerca --%>
<%
if(flagIncludeInCarico.equals("S") || (data1.length()>0 && data2.length()>0))
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
      if (data1.length()>0 && data2.length()>0)
      {
%>
        <tr>
          <td class="lVerdeNB"> Dalla data: <%=data1%>&nbsp;&nbsp;&nbsp;
           alla data : <%=data2%></td>
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

<%
 if (Messaggi.size() == 0)
{
%>
<br>
<table>
  <tr>
    <td class="label">    Nessun Elemento trovato ! </td>
  </tr>
</table>
<%
} else {
%>

  <div align="left">
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Anno/Numero SIEP</td>
      <td class="int">Anno/Numero Competente per cumulo</td>
      <td class="int">Soggetto</td>
      <td class="int">Data Presa in carico</td>
      <td class="int">Ufficio Mittente</td>
      <td class="int">Azioni</td>
    </tr>
<%
	UfficioAccorpatoUtils lUffAccorpUtils = new UfficioAccorpatoUtils();
  String coloreLinea = "c"; 
  Iterator itx = Messaggi.iterator();
  while ( itx.hasNext())
  {
    MessaggioModel lMess = (MessaggioModel)itx.next();
    coloreLinea = "c";
    
    UfficioAccorpatoModel lUfficioAccorpato = null;
    lUfficioAccorpato = lUffAccorpUtils.getUfficioAccorpatoByCodAccorpanteProgr ( lMess.getChiaveUfficioSiep(),  lMess.getChiaveProgrSiep());
    
    String lStrFascicoloRicevuto = null;
    if (lUfficioAccorpato!=null){
      BigDecimal lProgOrig = (lMess.getChiaveProgrSiep()).subtract(new BigDecimal(lUfficioAccorpato.getIncrProgressivo()));
      lStrFascicoloRicevuto = lMess.getChiaveAnnoSiep()+"/"+lProgOrig;
      lStrFascicoloRicevuto += "<br> <font class=\"cRosso\">(Ex "+lUfficioAccorpato.getCodTipoUfficio()+" di "+lUfficioAccorpato.getDescrizione()+")</font>"; 
    }
    else {
      lStrFascicoloRicevuto = lMess.getChiaveAnnoSiep()+"/"+lMess.getChiaveProgrSiep();
    }
%>
  <tr>
  	<td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lStrFascicoloRicevuto)%></td>
  	<%-- 
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
    --%>     
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoFasCumulante())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrFasCumulante())%></td>     
    <td class="<%=coloreLinea%>"><%= lMess.getCognomeSoggetto()%>&nbsp;<%= lMess.getNomeSoggetto()%></td>
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataEsito(),"dd-MM-yyyy HH:mm:ss"))%></td>
    <td class="<%=coloreLinea%>"><%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%></td>
    <td class="<%=coloreLinea%>">
      <jsp:include page="<%=ICostantiMessaggio.PG_BUTTONS_MESSAGGIO%>">
        <jsp:param name="CampoIdEntita" value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
        <jsp:param name="ValoreIdEntita" value="<%=lMess.getIdMessaggio()%>" />
        <jsp:param name="CodTipoOperazione" value="<%=lMess.getCodTipoOperazione()%>" />
      </jsp:include>
    <!--/td-->
    
    <% if ("01001".equals (lMess.getCodEsito()) ) { %>
    <%-- td class="<%=coloreLinea%>"--%> 
      <a href="javascript:RestituisciFascicolo(<%=lMess.getIdMessaggio()%>)">
        <img src="/images/Restituzione.gif" title="Restituzione Fascicolo" border="0" width="18" height="18"></a>
    </td>
    <% } %>
  </tr>
<%
  }
%>
    </table>
  </div>
<% } %>


<form name="campi">
  <input type="hidden" value="<%=AzioneChiamante%>" name="<%=ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE%>" >
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
</form>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formRestituzione">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="">
  <input type="hidden" name="<%=IWebConstants.GOTO_PAGE%>"  value="siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaPresiCarico">
</form>

  
  </body>
</html>