<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="UtenteConnesso"      scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="PosizioneGiuridica"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="PenaResidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />

<head>
  <title> [S.I.E.S.] - Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo" >
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name=f>
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Richiesta Fascicoli Soggetti a Cumulo</font>
        </td>
      </tr>
    </table>
 
    <br>
    
    <table style="width: 90%;">
     <tr><td class=Titolo>Fascicolo Cumulante</td></tr>
     <tr><td><br>
        <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
      <br></td></tr>
    </table>
    
    <table>
      <tr>
        <td class=l width=25%>Posizione Giuridica : </td>
        <td class=l colspan=3><font class="campo"><%=PosizioneGiuridica.getDescrPosizioneGiuridica()%></font></td>
      </tr>


<%
if (PenaResidua != null && PenaResidua.getIdPenaResidua() != null) {
%>
      <tr>
        <td class="l">Reclusione</td>
        <td class="l">
          <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumAnniReclusione(), "0")%>&nbsp;</font>
          <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumMesiReclusione(), "0")%>&nbsp;</font>
          <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumGiorniReclusione(), "0")%></font>
        </td>
        <td class="l">Multa</td>
        <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(PenaResidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
      </tr>
      <tr>
        <td class="l">Arresto</td>
        <td class="l">
           <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumAnniArresto(), "0")%>&nbsp;</font>
           <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumMesiArresto(), "0")%>&nbsp;</font>
           <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumGiorniArresto(), "0")%></font>
        </td>
        <td class="l">Ammenda</td>
        <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(PenaResidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
      </tr>
<%
}
%>
	</table>
	<%-- MEV 16 CUMULO: spostato link "CaricaModuloWebNsc" in un'altra funzionalità del cumulo --%>
</form>
</body>
</html>