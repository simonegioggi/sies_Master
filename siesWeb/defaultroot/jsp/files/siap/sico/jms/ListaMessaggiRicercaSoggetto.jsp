<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.xml.TreeModel" %>
<%@ page import="siap.jms.util.ParserMessage" %>
<%@ page import="siap.jms.messaggio.model.ContatoreEsitiModel" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<%@ page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>

<jsp:useBean id="dataRicercaInizio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataRicercaFine" scope="request" class="java.lang.String" />
<jsp:useBean id="codUtente" scope="request" class="java.lang.String" />
<jsp:useBean id="descTipoUtente" scope="request" class="java.lang.String" />

<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <title>[S.I.E.S.] - Messaggi Spediti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <SCRIPT LANGUAGE="JavaScript">
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <!-- preload images: -->
      if (document.images)
      {
        clickme1 = new Image(58,17); clickme1.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif";
        clickme2 = new Image(58,17); clickme2.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01-down.gif";
      }

      function hiLite(imgName,imgObjName)
      {
        if (document.images)
        {
            document.images[imgName].src = eval(imgObjName + ".src");
        }
      }
    </SCRIPT>
  </head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : </font><font class="campo">Controllo Esito Messaggi Ricerca Soggetto</font>&nbsp;&nbsp;
     </td>
    </tr>
  </table>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA_ESITO%>"></jsp:include>

<br>
<%
if(Messaggi.size()==0)
{
%>
   <p>&nbsp;<p>&nbsp;<p>&nbsp;
   <table width="300"  cellspacing="0" align="center" class="tab" border="1">
     <tr align="center" valign="middle">
        <td align="center" colspan="2" class="tab">
          <p>&nbsp;<p>
          <B>Nessun Messaggio Spedito da verificare</B>
          <p>&nbsp;<p>
        </td>
      </tr>
      <tr align="center" valign="middle">
        <td align="center" colspan="2" class="tab2">
          <a href="javascript:history.go(-1);" onMouseOver="hiLite('img01','clickme2')" onMouseOut="hiLite('img01','clickme1')">
            <IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif" BORDER="0" ALT="" NAME="img01">
          </a>
        </td>
      </tr>
      <tr align="left" >
        <td colspan="2"  class="tabhead"></td>
      </tr>
    </table>
<%
}
else
{
%>

  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
    </tr>
<%
      if(!(dataRicercaInizio.equals(""))||!(dataRicercaFine.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Data di Ricerca :&nbsp;&nbsp;
<%
          if(!(dataRicercaInizio.equals("")))
          {
%>
            Dal <%=dataRicercaInizio%>&nbsp;&nbsp;
<%        }
          if(!(dataRicercaFine.equals("")))
          {
%>
            &nbsp;Al&nbsp;&nbsp;<%=dataRicercaFine%>
            </td>
<%        }
       %></tr><%
      }%>
      <tr>
        <td class="lVerdeNB">Utente che ha effettuato la ricerca :
<%        if(codUtente.length()>1 )
	    {%> <%=codUtente%><%}
	  else
	    {%> <%=descTipoUtente%><%}
%>
	</td>
      </tr>
  </table>


 <div id="elenco" style="width: 100%;">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Data Nascita</td>
      <td class="int">Luogo Nascita</td>
      <td class="int">Codice CUI</td>
      <td class="int">Data Invio Richiesta</td>
		  <td class="int">Esito</td>
      <td class="int">Azioni</td>
    </tr>
<%
  Iterator itx = Messaggi.iterator();
  int cont = 1;
  while ( itx.hasNext())
  {
    MessaggioModel lMess = (MessaggioModel)itx.next();
    TreeModel lTree =  lMess.getTreeModel();
    ParserMessage lParser = new ParserMessage(lTree);
%>
   <tr>
<!--  INIZIO INTERVENTO PER SEGNALAZIONE m_dg.DOG07.06-08-2018.0025591.U per versione sies 11.3 (introduco il catch per InvalidClassException) -->
<%  if ( lParser != null) { %>
      
      <td class="c"><%= StringUtils.toStringJSP(lParser.getSoggetto()!=null? lParser.getSoggetto().getCognome() : "--")%></td>
      <td class="c"><%= StringUtils.toStringJSP(lParser.getSoggetto()!=null? lParser.getSoggetto().getNome() : "--")%></td>

	 <% if(lParser.getSoggetto()!= null && lParser.getSoggetto().getDataNascita()!=null ){%>
      <td class="c"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lParser.getSoggetto().getDataNascita(),"dd-MM-yyyy"))%></td>
	 <%} else {%>
	  <td class="c"><%="--"%></td>
     <%}%>
        
      <% if(lParser.getSoggetto()!= null && lParser.getSoggetto().getCodComuneNascita()!=null && lParser.getSoggetto().getCodComuneNascita().length()>1 ){%>
      <td class="c"><%= StringUtils.toStringJSP(lParser.getSoggetto().getDescrComuneNascita())%></td>
      <%}
      else
      {
       if(lParser.getSoggetto() != null && !lParser.getSoggetto().getCodStatoNascita().equals("039") && lParser.getSoggetto().getDescrStatoNascita()!=null)
       {%>
       <td class="c"><%= StringUtils.toStringJSP(lParser.getSoggetto().getDescrStatoNascita().toUpperCase())%></td>
      <% } else{%>
       <td class="c"><%="--"%></td>
      <%}
     }%>
      
      <td class="c"><%= StringUtils.toStringJSP(lParser.getSoggetto()!=null? lParser.getSoggetto().getCodAfis() : "--")%></td>
      
             <% if(lParser.getSoggetto()!=null && lParser.getSoggetto().getDataNascita()!=null ){%>
      <td class="c"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
<%} else {
 %>
  <td class="c"><%="--"%></td>
   <% }
   %>
      
      
<% } %>      


<%

  if ( lMess.getMessaggiCorrelati() != null )
  {
    if(lMess.getMessaggiCorrelati().lastElement() instanceof ContatoreEsitiModel)
    {
      ContatoreEsitiModel lContatore = (ContatoreEsitiModel)lMess.getMessaggiCorrelati().lastElement();
      %>
       <td class="l">
        <table>
        <tr><td >
         <font class="cVerde"><%=lContatore.getTrovati()%></font>&nbsp; </td><td  class="label">BDI con soggetto trovato</td> </tr>
         <tr><td class="lNoBord"><font class="cVerde"><%=lContatore.getNonTrovati()%> </font>&nbsp;</td><td  class="label" >BDI con soggetto non trovato</td></tr>
         <tr><td class="lNoBord"><font class="cRosso"><%=lContatore.getNonSpediti()%></font>&nbsp; </td><td  class="label"> BDI Rispedizione in corso </td></tr>
         <tr><td class="lNoBord"><font class="cRosso"><%=lContatore.getInAttesa()%></font> &nbsp;</td><td class="label"> BDI in attesa di risposta</td></tr>
         <tr><td class="lNoBord"><font class="cGrigio"><%=lContatore.getNonCoinvolte()%></font> &nbsp;</td><td class="label"><font class="cGrigio"> BDI non coinvolte per la ricerca</font></td></tr>

         </table>
       </td>
      <%
    }
  }
  else
  {//Esiste una sola risposta dato che la richiesta ricerca e' stata effettuata
   //su una sola BDI

    if(lMess.getMessaggioCorrelato() != null)
    {
     %>
    <% if (lMess.getMessaggioCorrelato().getCodEsito().equals("10000"))
       {%>
        <td class="l">Soggetto  <font class="cVerde">trovato</font>  a <%=lMess.getDescrBdiDestinataria()%>  </td>
     <%}%>
   <% if (lMess.getMessaggioCorrelato().getCodEsito().equals("10001"))
       {%>
        <td class="l">Soggetto<font class="cRosso">  non trovato</font>  a <%=lMess.getDescrBdiDestinataria()%>  </td>
     <%}%>
      <% if (lMess.getMessaggioCorrelato().getCodEsito().equals("00100"))
       {%>
         <td class="l">richiesta <font class="cRosso">non partita</font> per <%=lMess.getDescrBdiDestinataria()%>   </td>
     <%}%>
      <% if (lMess.getMessaggioCorrelato().getCodEsito().equals("01000"))
       {%>
         <td class="l">richiesta <font class="cRosso">cancellata</font> per <%=lMess.getDescrBdiDestinataria()%>   </td>
     <%}%>
           <% if (lMess.getMessaggioCorrelato().getCodEsito().equals("11111"))
       {%>
         <td class="l">Installazione locale del SIES <font class="cRosso">incompatibile</font> rispetto al distretto di <%=lMess.getDescrBdiDestinataria()%>   </td>
     <%}%>
   <%}

    else
    {
    %>
          <td class="l"> <font class="cRosso">In attesa</font> di risposta da <%=lMess.getDescrBdiDestinataria()%>     </td>
  <%}
  }


%>

      <td class="c">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
           <jsp:param name="ValoreIdEntita" value="<%=lMess.getIdMessaggio()%>" />
        </jsp:include>
       </td>
     </tr>
<%
  }
}
%>
  </table>
  </div>
	</body>
</html>