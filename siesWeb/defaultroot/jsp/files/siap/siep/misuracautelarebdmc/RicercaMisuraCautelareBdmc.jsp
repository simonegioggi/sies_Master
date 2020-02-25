<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel"%>
<%@ page import="siap.siep.misuracautelarebdmc.action.ICostantiMisuraCautelareBdmc"%>

<jsp:useBean id="misuracautelarebdmc" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> Ricerca MisuraCautelareBdmc </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaMisuraCautelareBdmc">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco MisuraCautelareBdmc</font>
      </td>
    </tr>
  </table>

  <% //=============================================== 
     // Include della jsp che gestisce la paginazione 
     //=============================================== %>
  <%
  if (tipo_ricerca.equals("paginata")) { %>
    <jsp:include page="<%=ISIAPCostantiWeb.PAGINAZIONE_RICERCA%>"></jsp:include>
  <%}
  %>

<div>
  <table align="center">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Id Misura Cautelare</td>
      <td class="int">Fas Sie Id Fascicolo Siep</td>
      <td class="int">Sog Id Soggetto</td>
      <td class="int">Eve Id Evento</td>
      <td class="int">Pen Res Id Pena Residua</td>
      <td class="int">Id Pren</td>
      <td class="int">Prog Peri Pres</td>
      <td class="int">Flag Caricamento</td>
      <td class="int">Flag Stato</td>
      <td class="int">Cod Tipo Misura</td>
      <td class="int">Data Inizio</td>
      <td class="int">Data Fine</td>
      <td class="int">Num Anni</td>
      <td class="int">Num Mesi</td>
      <td class="int">Num Giorni</td>
      <td class="int">Ist Det Id Istituto Detenzione</td>
      <td class="int">Altro Luogo Detenzione</td>
      <td class="int">Flag Computabile</td>
      <td class="int">Cod Motivo Non Computabile</td>
      <td class="int">Cod Tipo Ufficio Rifer</td>
      <td class="int">Cod Luogo Ufficio Rifer</td>
      <td class="int">Data Computo</td>
      <td class="int">Anno Fasc Siep</td>
      <td class="int">Nume Fasc Siep</td>
      <td class="int">Note</td>
      <td class="int">Anno Fasc Bdmc</td>
      <td class="int">Nume Fasc Bdmc</td>
      <td class="int">Cod Ufficio Bdmc</td>
      <td class="int">Anno Rgnr</td>
      <td class="int">Nume Rgnr</td>
      <td class="int">Cod Ufficio Rgnr</td>
      <td class="int">Anno Rege Gip</td>
      <td class="int">Numero Rege Gip</td>
      <td class="int">Cod Ufficio Gip</td>
      <td class="int">Anno Rege Dib</td>
      <td class="int">Numero Rege Dib</td>
      <td class="int">Cod Ufficio Dib</td>
      <td class="int">Anno Rege Cas</td>
      <td class="int">Numero Rege Cas</td>
      <td class="int">Cod Ufficio Cas</td>
      <td class="int">Anno Rege Cap</td>
      <td class="int">Numero Rege Cap</td>
      <td class="int">Cod Ufficio Cap</td>
      <td class="int">Anno Rege Casap</td>
      <td class="int">Numero Rege Casap</td>
      <td class="int">Cod Ufficio Casap</td>
      <td class="int">Cod Operatore Inserimento</td>
      <td class="int">Data Inserimento</td>
      <td class="int">Cod Ufficio Inserimento</td>
      <td class="int">Cod Operatore Aggiornamento</td>
      <td class="int">Data Aggiornamento</td>
      <td class="int">Cod Ufficio Aggiornamento</td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = misuracautelarebdmc.iterator();
      while ( itx.hasNext()) {
        MisuraCautelareBdmcModel lMisuraCautelareBdmc = (MisuraCautelareBdmcModel)itx.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getIdMisuraCautelare(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getFasSieIdFascicoloSiep(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getSogIdSoggetto(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getEveIdEvento(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getPenResIdPenaResidua(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getIdPren(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getProgPeriPres(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getFlagCaricamento(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getFlagStato(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodTipoMisura(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraCautelareBdmc.getDataInizio(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraCautelareBdmc.getDataFine(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getNumAnni(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getNumMesi(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getNumGiorni(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getIstDetIdIstitutoDetenzione(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getAltroLuogoDetenzione(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getFlagComputabile(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodMotivoNonComputabile(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodTipoUfficioRifer(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodLuogoUfficioRifer(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraCautelareBdmc.getDataComputo(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getAnnoFascSiep(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getNumeFascSiep(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getNote(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getAnnoFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getNumeFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodUfficioBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getAnnoRgnr(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getNumeRgnr(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodUfficioRgnr(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getAnnoRegeGip(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getNumeroRegeGip(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodUfficioGip(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getAnnoRegeDib(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getNumeroRegeDib(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodUfficioDib(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getAnnoRegeCas(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getNumeroRegeCas(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodUfficioCas(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getAnnoRegeCap(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getNumeroRegeCap(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodUfficioCap(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getAnnoRegeCasap(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getNumeroRegeCasap(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodUfficioCasap(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodOperatoreInserimento(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraCautelareBdmc.getDataInserimento(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodUfficioInserimento(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodOperatoreAggiornamento(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraCautelareBdmc.getDataAggiornamento(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lMisuraCautelareBdmc.getCodUfficioAggiornamento(),"&nbsp;")%></td>
      <td class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile 

      %>
      <table>
       <tr>
 
                 <td>
               
                   <a href="Main.jsp?Action=siap.siep.misuracautelarebdmc.action.ActLoadDettaglioMisuraCautelareBdmc&IdMisuraCautelareBdmc=<%=lMisuraCautelareBdmc.getIdMisuraCautelareBdmc()%>&TornaQui=20">
                     <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                   </a>
 
               
                 </td>
 
                 <td>
                   <a href="Main.jsp?Action=siap.siep.misuracautelarebdmc.action.ActLoadModificaMisuraCautelareBdmc&IdMisuraCautelareBdmc=<%=lMisuraCautelareBdmc.getIdMisuraCautelareBdmc()%>&TornaQui=20">
                     <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                   </a>
                 </td>
 
                 <td>
                   <a href="Javascript:conferma('siap.siep.misuracautelarebdmc.action.ActLoadCancellaMisuraCautelareBdmc','IdMisuraCautelareBdmc','<%=lMisuraCautelareBdmc.getIdMisuraCautelareBdmc()%>');">
                     <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                   </a>
                 </td>
 
       </tr>
     </table>
       
      </td>
    </tr>
    <% } // end while su iterator %>
  </table>
</div>
</FORM>
</body>
</html>