<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.verbale.model.VerbaleModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>

<jsp:useBean id="verbale" scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="FlagOmesse" scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Verbale Vane Ricerche </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>


<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Verbale Vane Ricerche</font>
      </td>
   </tr>

 </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
</FORM>
<form name="DettaglioVerbaleVaneRicereche" method="POST" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.notifica.action.ActLoadOmessaNotifica">
		 <table cellspacing=4 cellpadding=4>


		<tr>
				<td class="l">Data pervenimento del verbale</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataPervenimento(),"dd-MM-yyyy"))%> </font></td>
		</tr>
		<tr>
				<td class="l">Data verbale</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
		</tr>
		<tr>
				<td class="l">Autorità che ha redatto il verbale</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrTipoUfficioFirmatario()) %></font></td>
		</tr>
		<tr>
				<td class="l">Luogo</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario()) %></font></td>
		</tr>
		<tr>
				<td class="l">Indirizzo</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getNote()) %></font></td>
		</tr>		

		<tr>
				<td class="l">N.Protocollo vane ricerche</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getNumeroProtocollo()) %>&nbsp;</font></td>
    </tr>

  <br>

<%if(FlagOmesse != null && FlagOmesse.equals("S")) //se proviene da omesse notifiche
 {%>


<tr>
<td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="R" value="Rinnovo Ricerche per Omesse Notifiche">
</td>

</tr>

<%}%>

		</table>

  </form>
  </body>
</html>