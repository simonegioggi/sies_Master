<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>

<jsp:useBean id="MagistratoCompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel" />
<jsp:useBean id="PosizioneGiuridica"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />

<head>
  <title> [S.I.E.S.] - Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
</head>

<body class="corpo">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name=f>
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica">
    <input type="hidden" name="BackToCumulo" value="1">
    <input type="hidden" name="cambioposizione" value="S">
    <input type="hidden" value="<%=PosizioneGiuridica.getIdPosizioneGiuridica()%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_ID_POSIZIONE_GIURIDICA%>">
    
    <table>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
          </a>
        </td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;&nbsp;
          <font class="campo">Inserimento Pena Residua</font>
        </td>
      </tr>
    </table>
    
    <br>
    
    <table>
      <tr><td class=Titolo>Fascicolo Cumulante</td></tr>
      <tr>
        <td align=center>
          <br>
          <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
          <br>
        </td>
      </tr>
      <tr>
        <td class=l>Posizione Giuridica : <font class="campo"><%=PosizioneGiuridica.getDescrPosizioneGiuridica()%></font></td>
      </tr>
    </table>
    <br>

    <table>
      <tr>
        <td class=lNoBord>
          <br><br><input class="bottone" type=submit value="Modifica Posizione Giuridica">
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class=lNoBord>
          <font class="campo">ATTENZIONE: Per controllare e validare la pena residua è necessario richiamare, e poi
          confermare, la funzione "Modifica Posizione Giuridica", anche se la posizione giuridica rimane la stessa.
          </font>
        </td>
      </tr>
    </table>
</form>
</body>
</html>

