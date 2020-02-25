<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel" %>
<%@ page import="siap.sico.ufficio.util.UfficioAccorpatoUtils" %>


<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>

<%-- // STUB 15/03/2005 Parametri per la visualizzazione dei criteri di ricerca --%>
<jsp:useBean id="codUfficio"   scope="request" class="java.lang.String"/>
<jsp:useBean id="descrUfficio" scope="request" class="java.lang.String"/>

<jsp:useBean id="flagIncludeInCarico" scope="request" class="java.lang.String"/>
<jsp:useBean id="data1"               scope="request" class="java.lang.String"/>
<jsp:useBean id="data2"               scope="request" class="java.lang.String"/>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>


<%
//==================================================================================
// 11/09/2015 jsp invocata solo dalla ActLoadRicercaAttiCompetenzaRicevuti per la
// visualizzazione degli atti ricevuti per competenza (00066) e Seguito Atti (00078) 
// ancora da prendere in carico (CUMULO)
// A dispetto da quanto previsto nel codice, non sono previsti criteri di 
// ricerca e non vengono passati i messaggi già presi in carico visualizzati
// in altra JSP.
//==================================================================================

String lHrefIstruttoria = "";
if(IstruttoriaCumulo !=null && IstruttoriaCumulo.getIdIstruttoriaCumulo()!=null) {
  lHrefIstruttoria="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
}



%>


<html>
  <head>
    <title>[S.I.E.S.] - Messaggi Ricevuti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  </head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : </font><font class="campo">Lista Atti Ricevuti per Competenza</font>&nbsp;&nbsp;
     </td>
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
 
  <br>
	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaRicevuti">
    <input type="hidden" name="<%=IWebConstants.NUM_PAGE%>" value="1"> 

<% if(IstruttoriaCumulo !=null && IstruttoriaCumulo.getIdIstruttoriaCumulo()!=null){%>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
    <br>
<%
} else {
  // Visualizzazione Criteri di Ricerca
  // DA CORREGGERE i criteri non vengono mai indicati per questo tipo di
%>


  <% if(flagIncludeInCarico.equals("S") || (data1.length()>0 && data2.length()>0)) {%>
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
<% } %>

<% if (Messaggi.size() == 0) {%>
<br>
<table>
  <tr>
    <td class="label"> Nessun Elemento trovato ! </td>
  </tr>
</table>
<% } else { %>

<div align="left">
	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Anno/Numero SIEP</td>
      <td class="int">Anno/Numero Fascicolo Cumulante</td>
      <td class="int">Soggetto</td>
      <td class="int">Data Arrivo</td>
      <td class="int">Tipo Operazione</td>
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
    if (lMess.getFlagVisto().compareTo("S")==0)
      coloreLinea = "cVerde";


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
      
      <% if (lMess.getChiaveAnnoFasCumulante()!=null) {%>
      <td class="<%=coloreLinea%>">
      <% } else { %>
      <td class="cRosso">
      <% } %>
        <%= StringUtils.toStringJSP(lMess.getChiaveAnnoFasCumulante(),"n.d.")%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrFasCumulante(),"n.d.")%>
      </td>
      <td class="<%=coloreLinea%>"><%= lMess.getCognomeSoggetto()%>&nbsp;<%= lMess.getNomeSoggetto()%></td>
      <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
      <td class="<%=coloreLinea%>"><%= lMess.getDescrTipoOperazione()%>&nbsp;</td>
      <td class="<%=coloreLinea%>"><%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%></td>
      <td class="<%=coloreLinea%>">
      
      <%
      String lStrHref = IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.siep.presaincarico.action.ActDettaglioPresaincaricoCompetenza";
      lStrHref+="&"+ICostantiMessaggio.CAMPO_ID_MESSAGGIO+"="+lMess.getIdMessaggio();
      lStrHref+="&TornaQui="+TornaQui;
      lStrHref+="&"+request.getParameter("CampoAzioneChiamante")+"="+request.getParameter("ValoreAzioneChiamante");
      lStrHref+=lHrefIstruttoria;
      %>
  <table>
    <tr>
      <td>
        <a href="<%=lStrHref%>">
          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
        </a>
      </td>
    </tr>
  </table>
      
      
      <%--
        <jsp:include page="buttonsMessaggi.jsp">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lMess.getIdMessaggio()%>" />
          <jsp:param name="CodTipoOperazione" value="<%=lMess.getCodTipoOperazione()%>" />
        </jsp:include>
      --%>
      </td>
    </tr>
<%
  }
%>
    </table>
  </div>
<% } %>
  
 
  </body>
</html>