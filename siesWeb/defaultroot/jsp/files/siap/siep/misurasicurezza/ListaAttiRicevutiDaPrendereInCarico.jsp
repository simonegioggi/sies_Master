<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.jms.ICostantiJMS"%>

<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>


<%
//==============================================================================
// Jsp per la visualizzazione degli atti ricevuti e ancora da prendere in carico
//==============================================================================
%>

<html>
  <head>
    <title>[S.I.E.S.] - Atti Ricevuti</title>
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
      <!-- BOTTONE DI RITORNO -->
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/--%>
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActGestioneMisureSicurezza">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>
  <br>


<% if (Messaggi.size() == 0)  { %>
  <br>
  <table cellspacing=2 cellpadding=2 width="80%">
    <tr>
      <td class="int">Anno/Numero <br>SIEP</td>
      <td class="int">Oggetto</td>
      <td class="int">Ufficio Mittente</td>
      <td class="int">Data Arrivo</td>
      <td class="int">Soggetto</td>
      <td class="int">Azioni</td>
    </tr>
    <tr>
      <td class="c" colspan="6">  <br>Non sono attualmente presenti atti da prendere in carico <br> </td>
    </tr>
  </table>
<% } else { %>

  <div align="center">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Anno/Numero <br>SIEP</td>
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
    if (lMess.getFlagVisto().compareTo("S")==0)
      coloreLinea = "cVerde";

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
      
      <% 
      if (lMess.getMessaggiCorrelati()!=null && lMess.getMessaggiCorrelati().size()>0 ) 
      {
        Vector lMessaggiSollecito = lMess.getMessaggiCorrelati();
        
        for (int i=0;i<lMessaggiSollecito.size();i++) 
        {
          MessaggioModel lMessSoll = (MessaggioModel) lMessaggiSollecito.elementAt(i);
      
      %>
      
        <br>
        <font color="red">Ricevuto Sollecito il <%= StringUtils.toStringJSP(DateUtils.getDateToString(lMessSoll.getDataInvio(),"dd-MM-yyyy HH:mm"),"") %></font>
        <% } // end for %>
      <% } %>
    </td>    
    
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm"))%></td>
    <td class="<%=coloreLinea%>"><%= lMess.getNomeSoggetto()%>&nbsp;<%= lMess.getCognomeSoggetto()%></td>
    <td class="<%=coloreLinea%>" >
    <table>
      <tr>
        <td>
          <jsp:include page="<%=ICostantiMessaggio.PG_BUTTONS_MESSAGGIO%>">
            <jsp:param name="CampoIdEntita"     value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
            <jsp:param name="ValoreIdEntita"    value="<%=lMess.getIdMessaggio()%>" />
            <jsp:param name="CodTipoOperazione" value="<%=lMess.getCodTipoOperazione()%>" />
          </jsp:include>
        </td>
        <%
        //======================================================================
        // TEST se il parser non può leggere il messaggio si segnala come 
        // non elaborabile
        //======================================================================
        %>
<%--         <% if (lMess.getIsErroreParser() && 1==2) { %>         --%>
<!--         <td> -->
<!--           <font color="red"><b><img src="/images/attenzione.jpg" width="12" height="12" alt="Attenzione. Messaggio non elaborabile in quanto inviato con una versione antecedente di SIEP" border="0"></b></font> -->
<!--         </td> -->
<%--         <% } %> --%>
      </tr>
    </table>

      
    </td>
    
    <% if (lMess.getIsErroreParser()) { %>
      <td>
        <font color="red"><b><img src="/images/attenzione.jpg" width="12" height="12" alt="Attenzione. Messaggio non elaborabile in quanto inviato con una versione SIEP differente da quella attualmente in uso da questo Ufficio" border="0"></b></font>
      </td>
    <% } %>
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