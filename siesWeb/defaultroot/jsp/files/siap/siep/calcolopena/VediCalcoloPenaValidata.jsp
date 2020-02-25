<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>


<jsp:useBean id="aSSResidua"             scope="request" class="siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel" />
<jsp:useBean id="AzioneChiamante"        scope="request" class="java.lang.String" />
<jsp:useBean id="EsisteTrasmissioneAtti" scope="request" class="java.lang.String" />

  
<%
//==============================================================================
// Form per la visualizzazione delle conferma avvenuto aggiornamento del fine
// pena manuale in caso di Primo Calcolo della Pena in decorrenza
//==============================================================================
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <title>[S.I.E.S.] - Calcolo Pena</title>
  </head>
  
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG>
          <font class="label">Funzione :&nbsp;</font><font class="campo">Visualizzazione Pena Validata</font>
        </td>
        <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <table>
      <tr>
        <td class="C" width="50%">Calcolo della Pena Correttamente Effettuato!</td>
      </tr>
    </table>
    
    
<%
  // n.b. a seguito della revisione SS il tasto non deve più essere presente
  // 1==2 // commentato a seguito correzioni SS
  if (    aSSResidua!=null
       && aSSResidua.getIdSanzioneSostResidua()!=null
     ) 
  { 
    String lAzione = "";
    if (AzioneChiamante!=null && AzioneChiamante.equals("siap.siep.sanzionesostitutiva.action.ActLoadInserisciComunicazioneNuovoResiduoPena") )
    {
      lAzione = AzioneChiamante;
    }
    else if (EsisteTrasmissioneAtti!=null && EsisteTrasmissioneAtti.equals("SI")){
      // Ancora primo calcolo, ma già emessa trasmissione atti, per cui vado su 
      // Comunicazione nuovo residuo pena
      lAzione = "siap.siep.sanzionesostitutiva.action.ActLoadInserisciComunicazioneNuovoResiduoPena";
    }
    else {
      lAzione = "siap.siep.sanzionesostitutiva.action.ActLoadInserisciTrasmissioneAttiEsecuzione";
    }
%>    
    
	<form method="POST" name="cnrp" action="<%= IWebConstants.PG_MAIN%>">
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      	<%--input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActLoadInserisciComunicazioneNuovoResiduoPena"--%>
      	<%--input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActLoadInserisciTrasmissioneAttiEsecuzione"--%>
      	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
      	<input type="HIDDEN" name="FromCalcoloPena" value="S">

      <table>
        <tr>
          <td class="l" colspan="4">
            <!--INPUT class="bottone" type="submit" name="conferma" value="Comunicazione Nuovo Residuo Pena" -->
            <INPUT class="bottone" type="submit" name="conferma" value="Conferma" >
          </td>
        </tr>
      </table>
    </form>
<% } %>
    
  </body>
</html>