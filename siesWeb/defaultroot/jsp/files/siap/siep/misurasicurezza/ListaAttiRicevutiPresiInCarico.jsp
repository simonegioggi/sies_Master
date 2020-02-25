<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.jms.ICostantiJMS"%>

<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>

<jsp:useBean id="aUfficioMittente"     scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="aEsito"               scope="request" class="java.lang.String"/>
<jsp:useBean id="aDataTrasmissioneDal" scope="request" class="java.lang.String"/>
<jsp:useBean id="aDataTrasmissioneAl"  scope="request" class="java.lang.String"/>

<jsp:useBean id="TornaQui"  scope="request" class="java.lang.String"/>


<html>
  <head>
    <title>[S.I.E.S.] - Atti Ricevuti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    
      function visualizzaDettaglio(idMessaggio)
      {
        var riga = document.getElementById(idMessaggio);
        if (riga.style.display == "none")
          riga.style.display = "block";
        else
          riga.style.display = "none";
      }
    
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
      <font class="label">Funzione : </font><font class="campo">Gestione Misure di Sicurezza - Lista Atti Presi In Carico</font>&nbsp;&nbsp;
     </td>
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadRicercaAttiPresiInCarico&<%=(String)request.getAttribute("linkRitorno")%>">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br><br>





  <div align="left">
  
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
    </tr>
    <% if (aUfficioMittente.getCodUfficio()!=null && aUfficioMittente.getCodUfficio().length()>0) { %>
    <tr>
      <td class="lVerdeNB">Ufficio Mittente: &nbsp;<%=aUfficioMittente.getDescrTipoUfficio()%>&nbsp;di&nbsp;<%=aUfficioMittente.getDescrComune()%>
    </tr>
    <% } %>
    
    <% if (aEsito.length()>0) {%>
    <tr>
      <td class="lVerdeNB">Esito: &nbsp;<%=StringUtils.toStringJSP(aEsito)%>
    </tr>
    <% } %>
    
    <% if( !aDataTrasmissioneDal.equals("") || !aDataTrasmissioneAl.equals("") ) { %>
    <tr>
      <td class="lVerdeNB">Procedimenti con Data di Trasmissione :&nbsp;
      <% if(!aDataTrasmissioneDal.equals("") ) { %>
            Dal <%=aDataTrasmissioneDal%>&nbsp;&nbsp;
      <% } %>
      <% if(!aDataTrasmissioneAl.equals("") ) { %>
            &nbsp;Al&nbsp;&nbsp;<%=aDataTrasmissioneAl%>
      <% } %>
      </td>
    </tr>
    <% } %>    
  </table>
  
  
  <%
  //============================================================================
  //
  //============================================================================
  %>
  <br>  
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <br>
  
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td colspan="5"><font class="label">
        Elenco degli atti ricevuti per competenza e presi in carico</font>
      </td>
    <tr>
    <tr>
      <td class="int">Anno/Numero SIEP</td>
      <td class="int">Oggetto</td>
      <td class="int">Ufficio Mittente</td>
      <td class="int">Soggetto</td>
      <td class="int">Data Arrivo</td>
      <td class="int">Data Esito</td>
      <td class="int">Esito</td>
      <td class="int">Azioni</td>
    </tr>
    
    <% 
    if (Messaggi.size() == 0)  { %>
    <tr>
      <td class="label" colspan="6"> &nbsp;</td>
    </tr>
    <tr>
      <td class="label" colspan="6"> Nessun Elemento soddisfa i criteri di ricerca impostati! </td>
    </tr>
    <tr>
      <td class="label" colspan="6"> &nbsp;</td>
    </tr>
    <% 
    } 
    else 
    {    
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
        <% if (lMess.getChiaveAnnoSiep()!=null) { %>
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
        <% } else { %>
          <td class="<%=coloreLinea%>">&nbsp;</td>     
          <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSius())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSius())%></td>     
          
          
          <td class="<%=coloreLinea%>">
            <%= lMess.getDescrUfficioSiep() +" "+ lMess.getDescrSedeUfficioSiep()%>
            <% if (!lMess.getCodUfficioMittente().equals(lMess.getChiaveUfficioSiep())) { %>
              <br>
              <font color="grey">Inoltrato da <br>
              <%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%>
              </font>
            <% } %>
          </td>
        <% } %>
          
          
          <td class="<%=coloreLinea%>"><%= lMess.getNomeSoggetto()%>&nbsp;<%= lMess.getCognomeSoggetto()%></td>
          <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
          <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataEsito(),"dd-MM-yyyy HH:mm:ss"),"&nbsp;")%></td>
          <td class="<%=coloreLinea%>"><%= lMess.getDescrEsito()%>
            <% if(lMess.getFascMsToFascSiepModel()!=null) { %>
            <br>
            <a class="cliccabile" href="/jsp/Main.jsp?Action=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lMess.getFascMsToFascSiepModel().getFasSieIdFascicoloSiep()%>" title="Procedimento">
            <%=lMess.getFascMsToFascSiepModel().getChiaveAnnoSiep()%>
            /
            <%=lMess.getFascMsToFascSiepModel().getChiaveProgrSiep()%>
            </a>&nbsp;
            <% } %>
          </td>
          
          <td class="c">
            <% if ( ICostantiJMS.PRESAINCARICO.equals(lMess.getCodEsito()) ) {  %>
            <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadDettaglioAttoRicevuto&<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>=<%=lMess.getIdMessaggio()%>&TornaQui=<%=TornaQui%>">
              <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
            <% } else if ( ICostantiJMS.ISCRITTO_CLASSE_IV.equals(lMess.getCodEsito()) ) {  %>
            <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadDettaglioAttoRicevuto&<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>=<%=lMess.getIdMessaggio()%>&TornaQui=<%=TornaQui%>">
              <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
            <% } else if ( ICostantiJMS.RESTITUITO.equals(lMess.getCodEsito()) ) {  %>
            <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadDettaglioAttoRicevuto&<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>=<%=lMess.getIdMessaggio()%>&TornaQui=<%=TornaQui%>">
              <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
            <% } else if ( ICostantiJMS.TRASFERITO.equals(lMess.getCodEsito()) ) {  %>
            <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadDettaglioAttoRicevuto&<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>=<%=lMess.getIdMessaggio()%>&TornaQui=<%=TornaQui%>">
              <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
            <% } else { %>
            &nbsp;
            <% } %>
            <!--
            &nbsp;<img src="/images/info.gif" width="12" height="12" alt="Dettaglio" border="0">
            &nbsp;<img src="/images/tip.png" width="12" height="12" alt="Dettaglio" border="0">
            -->
          </td>
        </tr>

<%-- <%  --%>
<!-- // per ora non visualizzo nulla: il messaggio di risposta con le motivazioni infatti -->
<!-- // non viene salvato in locale. Il messaggio ricevuto al quale si risponde potrebbe -->
<!-- // avere il campo note già valorizzato se ricevuto come inoltro. -->
<!-- // Andrebbe aggiunto un ulteriore campo alla tabella MESSAGGIO per memorizzare -->
<!-- // le note della risposta. -->
<%-- if ( lMess.getNote()!=null && lMess.getNote().length()>0 && 1==2) {  %> --%>
<%--         <% if ( ICostantiJMS.RESTITUITO.equals(lMess.getCodEsito()) ) {  %> --%>
<%--         <tr class="c" style="display:block" id="<%=lMess.getIdMessaggio()%>"> --%>
<%--           <td colspan="8" class="c" style="text-align:left;">Motivo restituzione: <font class="campo"><%=StringUtils.toStringJSP(lMess.getNote())%>&nbsp;</font></td> --%>
<!--         </tr>         -->
<%--         <% } else if ( ICostantiJMS.TRASFERITO.equals(lMess.getCodEsito()) ) {  %> --%>
<%--         <tr class="c" style="display:block" id="<%=lMess.getIdMessaggio()%>"> --%>
<%--           <td colspan="8" class="c" style="text-align:left;">Motivo inoltro: <font class="campo"><%=StringUtils.toStringJSP(lMess.getNote())%>&nbsp;</font></td> --%>
<!--         </tr> -->
<%--         <% } %> --%>
<%-- <% } %> --%>
        
      <% } // end while %>

    <% } %>
    </table>
  </div>

  </body>
</html>