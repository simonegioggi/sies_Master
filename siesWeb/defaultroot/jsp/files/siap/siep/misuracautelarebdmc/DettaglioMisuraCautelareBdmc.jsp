<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel"%>
<%@ page import="siap.siep.misuracautelarebdmc.action.ICostantiMisuraCautelareBdmc"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="misuracautelarebdmc" scope="request" class="siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel"/>

<html>
<head>
  <%  if( modalita.equals("D") )  {%> 
  <title> Dettaglio MisuraCautelareBdmc </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione MisuraCautelareBdmc </title>
  <%}%> 
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
  <script language="JavaScript" >
    function Verify() { 
      var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
      if (window.confirm(msgConfirm)) 
        return true; 
      else 
        return false; 
    } 
  </script>
</head>

<body class="corpo">

<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
      <%  if( modalita.equals("D") )  {%> 
        <font class="campo">Dettaglio MisuraCautelareBdmc</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione MisuraCautelareBdmc</font>
      <%}%> 
      </td>
      <td class="LBG">
         <a href="Main.jsp?Action=siap.siep.misuracautelarebdmc.action.ActLoadInserisciMisuraCautelareBdmc&TornaQui=10">
       <img align="middle" src="/images/new24.gif" alt="Inserisci" width="24" height="24" border="0">
    </a>
 
       <a href="Main.jsp?Action=siap.siep.misuracautelarebdmc.action.ActLoadModificaMisuraCautelareBdmc&IdMisuraCautelareBdmc=<%=misuracautelarebdmc.getIdMisuraCautelare()%>&TornaQui=10">
         <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
 
         <a href="Javascript:conferma('siap.siep.misuracautelarebdmc.action.ActLoadCancellaMisuraCautelareBdmc','IdMisuraCautelareBdmc','<%=misuracautelarebdmc.getIdMisuraCautelare()%>');">
         <img align="middle" src="/images/delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
 
      </td>
    </tr>
  </table>
</FORM>

<%  if( modalita.equals("C") )  {%> 
<FORM method="POST" action="Main.jsp" name="CancellaMisuraCautelareBdmc">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.siep.misuracautelarebdmc.action.ActCancellaMisuraCautelareBdmc">
  <input type="HIDDEN" name="<%=ICostantiMisuraCautelareBdmc.CAMPO_ID_MISURA_CAUTELARE%>" value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getIdMisuraCautelare()) %>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
<%}%> 
<table cellspacing=4 cellpadding=4>
  <tr>
    <td class="l">Id Misura Cautelare</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getIdMisuraCautelare()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Fas Sie Id Fascicolo Siep</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getFasSieIdFascicoloSiep()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Sog Id Soggetto</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getSogIdSoggetto()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Eve Id Evento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getEveIdEvento()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Pen Res Id Pena Residua</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getPenResIdPenaResidua()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Id Pren</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getIdPren()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Prog Peri Pres</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getProgPeriPres()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Caricamento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getFlagCaricamento()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Stato</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getFlagStato()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Tipo Misura</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodTipoMisura()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Inizio</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataInizio(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Fine</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataFine(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Num Anni</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNumAnni()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Num Mesi</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNumMesi()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Num Giorni</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNumGiorni()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Ist Det Id Istituto Detenzione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getIstDetIdIstitutoDetenzione()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Altro Luogo Detenzione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getAltroLuogoDetenzione()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Computabile</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getFlagComputabile()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Motivo Non Computabile</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodMotivoNonComputabile()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Tipo Ufficio Rifer</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodTipoUfficioRifer()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Luogo Ufficio Rifer</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodLuogoUfficioRifer()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Computo</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataComputo(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Fasc Siep</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoFascSiep()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Fasc Siep</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeFascSiep()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Note</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNote()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Fasc Bdmc</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoFascBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Fasc Bdmc</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeFascBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Bdmc</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Rgnr</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoRgnr()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Rgnr</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeRgnr()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Rgnr</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioRgnr()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Rege Gip</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoRegeGip()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Numero Rege Gip</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeroRegeGip()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Gip</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioGip()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Rege Dib</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoRegeDib()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Numero Rege Dib</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeroRegeDib()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Dib</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioDib()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Rege Cas</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoRegeCas()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Numero Rege Cas</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeroRegeCas()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Cas</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioCas()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Rege Cap</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoRegeCap()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Numero Rege Cap</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeroRegeCap()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Cap</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioCap()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Rege Casap</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoRegeCasap()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Numero Rege Casap</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeroRegeCasap()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Casap</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioCasap()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Operatore Inserimento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodOperatoreInserimento()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Inserimento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataInserimento(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Inserimento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioInserimento()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Operatore Aggiornamento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodOperatoreAggiornamento()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Aggiornamento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataAggiornamento(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Aggiornamento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioAggiornamento()) %></font>&nbsp;</td>
  </tr>

<%  if( modalita.equals("C") )  {%> 
  <tr>
    <td align="center">
      <input class="bottone" type="submit" name="conferma" value="Conferma"  onclick="Javascript: return Verify();">
    </td>
  </tr>
<%}%>

</table>
<%  if( modalita.equals("C") )  {%> 
</form>
<%}%>
</body>
</html>