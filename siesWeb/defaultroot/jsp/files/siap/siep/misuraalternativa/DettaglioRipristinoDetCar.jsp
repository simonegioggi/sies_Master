<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.verbale.model.VerbaleModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>

<jsp:useBean id="verbale" scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="luogodetenzione" scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Ripristino Detenzione in carcere </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>


<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Ripristino Detenzione in carcere</font>
      </td>
   </tr>

 </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
</FORM>
<form name="f" method="POST" action="/jsp/Main.jsp">
		 <table cellspacing=4 cellpadding=4>


		<tr>
				<td class="l">Data Pervenimento del Verbale</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataPervenimento(),"dd-MM-yyyy"))%> </font></td>
		</tr>
		<tr>
				<td class="l">Data Ingresso in carcere</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
		</tr>
		<tr>
				<td class="l">Autorità che ha Proceduto all'accompagnamento in carcere</td>
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
<%
  if(luogodetenzione != null && luogodetenzione.getIstitutoDetenzione() != null)
  {
%>
		<tr>
				<td class="l">Istituto di detenzione</td>
				<td class="l"><font class="campo"><%=luogodetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
           <font class="campo"><%=StringUtils.toStringJSP(luogodetenzione.getIstitutoDetenzione().getDescrComune()) %></font></td>
		</tr>
<%
  }
%>

    <tr>
       <td>
        <INPUT  class="bottone" type="submit" name="I" value="Ordine di Scarcerazione" onClick="">
       </td>
     </tr>

		</table>
     <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordinescarcerazione.action.ActLoadInserisciOSFuturaMemoria">

  </form>
  </body>
</html>