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
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>


<html>
  <head>
    <title>[S.I.E.S.] - Atti Ricevuti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      function conferma(a_idMessaggio)
      {
        if (window.confirm("Confermi l'iscrizione del procedimento di classe IV?"))
        {
          str = "/jsp/Main.jsp?Action=siap.sico.web.ActionUnderConstruction&<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>="+a_idMessaggio;
          window.location.href=str;
        }
      }
 </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : </font><font class="campo">Gestione Misure di Sicurezza - Lista Atti Presi In Carico Ancora Da Iscrivere</font>&nbsp;&nbsp;
     </td>
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActGestioneMisureSicurezza">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br><br>



<% if (Messaggi.size() == 0)  { %>
  <br>
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td colspan="5"><font class="label">
        Elenco degli atti ricevuti per competenza e presi in carico, per i quali 
        non è stato ancora iscritto il Procedimento di Esecuzione delle Misure di Sicurezza
        (Classe IV)</font>
      </td>
    <tr>
    <tr>
      <td class="int">Anno/Numero SIEP</td>
      <td class="int">Oggetto</td>
      <td class="int">Ufficio Mittente</td>
      <td class="int">Data Arrivo</td>
      <td class="int">Soggetto</td>
      <td class="int">Azioni</td>
    </tr>
    <tr>
      <td class="c" colspan="6">  <br>Non sono attualmente presenti atti presi in carico da iscrivere<br> </td>
    </tr>
  </table>
<% } else { %>

  <div align="left">

  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td colspan="5"><font class="label">
        Elenco degli atti ricevuti per competenza e presi in carico, per i quali 
        non è stato ancora iscritto il Procedimento di Esecuzione delle Misure di Sicurezza
        (Classe IV)</font>
      </td>
    <tr>
    <tr>
      <td class="int">Anno/Numero SIEP</td>
      <td class="int">Oggetto</td>
      <td class="int">Ufficio Mittente</td>
      <td class="int">Data Arrivo</td>
      <td class="int">Soggetto</td>
      <td class="int">Azioni</td>
    </tr>
    
<%
  String coloreLinea = "c"; 
  Iterator itx = Messaggi.iterator();
  while ( itx.hasNext())
  {
    MessaggioModel lMess = (MessaggioModel)itx.next();
    coloreLinea = "c";
    //if (lMess.getFlagVisto().compareTo("S")==0)
     //  coloreLinea = "cVerde";

%>
  <tr>
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>     
    
    <% if (ICostantiJMS.TRASFERIMENTO_COMPETENZA_MS.equals(lMess.getCodTipoOperazione())) {%>
    <td class="<%=coloreLinea%>">Trasmissione Atti per competenza  ex artt. 658 e 679 comma 1 c.p.p.</td>
    <% } else if (ICostantiJMS.TRASFERIMENTO_ESECUZIONE_MS.equals(lMess.getCodTipoOperazione())){%>
    <td class="<%=coloreLinea%>">Trasmissione Atti ai fini dell'esecuzione della misura di sicurezza ex art. 658 e 679 comma 2 c.p.p.</td>
    <% } %>
    
    <td class="<%=coloreLinea%>">
      <%= lMess.getDescrUfficioSiep() +" "+ lMess.getDescrSedeUfficioSiep()%>
      <% if (!lMess.getCodUfficioMittente().equals(lMess.getChiaveUfficioSiep())) { %>
        <br>
        <font color="grey">Inoltrato da <br>
        <%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%>
        </font>
      <% } %>
    </td> 
        
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
    <td class="<%=coloreLinea%>"><%= lMess.getNomeSoggetto()%>&nbsp;<%= lMess.getCognomeSoggetto()%></td>
    <td class="c">
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--a href="Javascript:conferma('<%=lMess.getIdMessaggio()%>')"--%>
      <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadDettaglioAttoRicevuto&<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>=<%=lMess.getIdMessaggio()%>&TornaQui=<%=TornaQui%>">
        <img src="/images/dettagli.gif" width="12" height="12" alt="Iscrizione Procedimento di Classe IV" border="0">
      </a>
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