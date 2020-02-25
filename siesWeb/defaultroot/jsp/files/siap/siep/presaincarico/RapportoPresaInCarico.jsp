<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

<html>
  <head>  
    <title>[S.I.E.S.] - Dettaglio Istanza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  </head>
  
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td>
      <td class="LBG">
        <font class="label">Funzione: </font>&nbsp;
        <font class="campo">Rapporto presa in carico </font>
      </td>
    </tr>
  </table>

  <FORM action="<%=IWebConstants.PG_MAIN%>"  name="comandi" method="post">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaRicevuti">
    <%--input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.fascicolo.action.ActLoadFascicoloDaRicercaMessaggio"--%>
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">

    <br>

    <table width="300"  cellspacing="0" align="center" class="tab" border="1">
      <tr align="center" valign="middle">
        <td align="center" colspan="2" class="tab">
          <br>
          <b>Ricezione Atto Completata e Esito rispedito al Mittente. Il fascicolo è stato preso in carico!</b>
          <br>
          <br>
        </td>
      </tr>
      
      <tr align="center" valign="middle">
        <td align="center" colspan="2" class="tab2">
          <input type="submit"  value="Prosegui" class="bottone" >
        </td>
      </tr>
      
      <tr align="left" >
        <td colspan="2"  class="tabhead"></td>
      </tr>
    </table>
    
    <p>
    
    <table width="700"  cellspacing="0" align="center" class="tab" border="1">
      <% if (Messaggio.getRapportoEsito()!=null && Messaggio.getRapportoEsito().length()>2) {%>
        <tr>
          <td class="LBG" colspan="2">
            <font class="label">Esito del Trasferimento Procedimento</font>&nbsp;
          </td>
        </tr>
       <%=Messaggio.getRapportoEsito()%>
      <%}%>
    </table>
  </FORM>
  </body>
 </html>