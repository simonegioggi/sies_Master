<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica" %>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" %>

<jsp:useBean id="posizionigiuridiche" scope="request" class="java.util.Vector" />
<jsp:useBean id="UtenteConnesso"      scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicolo"           scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Posizioni Giuridiche</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Posizioni Giuridiche</font></td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <div align=center>
  <table>
    <tr>
      <td class="int">Posizione Giuridica</td>
      <td class="int">Data Decorrenza</td>
      <td class="int">Data Fine</td>
      <td class="int">Luogo Detenzione</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
<%
  Iterator itx = posizionigiuridiche.iterator();
  while ( itx.hasNext())
  {
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel posizione = (PosizioneGiuridicaLuogoDetenzioneAltraCausaModel)itx.next();
%>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(posizione.getPosizioneGiuridica().getDescrPosizioneGiuridica(), "-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(posizione.getPosizioneGiuridica().getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(posizione.getPosizioneGiuridica().getDataFine(),"dd-MM-yyyy"))%>&nbsp;</td>
      <%if(posizione.getLuogoDetenzione() != null && posizione.getLuogoDetenzione().getIstitutoDetenzione() != null) {%>
        <td class="l">
          <%=StringUtils.toStringJSP(posizione.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%>
          di 
          <%-- =StringUtils.toStringJSP(posizione.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune()) --%>
          <%=StringUtils.toStringJSP(posizione.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione())%>
          <br>
          <%=StringUtils.toStringJSP(posizione.getLuogoDetenzione().getIstitutoDetenzione().getIndirizzo())%>&nbsp;
          &nbsp;
        </td>
      <%}else if(posizione.getLuogoDetenzione() != null && posizione.getLuogoDetenzione().getAltroLuogo() != null) {%>
        <td class="l"><%=StringUtils.toStringJSP(posizione.getLuogoDetenzione().getAltroLuogo())%>&nbsp;</td>
      <%}else if(posizione.getPosizioneGiuridica()!= null && posizione.getPosizioneGiuridica().getLuogoEspiazione() != null) {%>
        <td class="l"><%=StringUtils.toStringJSP(posizione.getPosizioneGiuridica().getLuogoEspiazione())%>&nbsp;</td>
      <%}else{%>
        <td class="l">- &nbsp;</td>
      <%}%>
      <td class="c">
<%
   String modificabile = "";
   if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))
      {modificabile = "SI";}
   else
      {modificabile = "NO";}
%>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita"  value="<%=ICostantiPosizioneGiuridica.CAMPO_ID_POSIZIONE_GIURIDICA%>" />
           <jsp:param name="ValoreIdEntita" value="<%=posizione.getPosizioneGiuridica().getIdPosizioneGiuridica()%>" />
           <jsp:param name="Modificabile"   value="<%=modificabile%>" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
  </table>
  </div>
  </body>
</html>