<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-33: aggiunta pagina per la trasmissione atti coìnversione pena pecuniaria --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="eventonotifica"	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="tipoUDS"           scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Gestione Trasferimento Atti Conversione Pene Pecuniarie </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript">
var desktop;
function ListaComuniUfficio(a_formname, a_fieldname) {
	codTipoUfficio = document.LoadTrasferisciAttiConversione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.value;
	if (codTipoUfficio == '-') {
		alert("Selezionare il tipo di ufficio emittente");
		document.LoadTrasferisciAttiConversione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.focus();
	} else {
	    desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&TipoUfficio="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}
}
</script>
</head>
<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
      	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        	<font class="campo">Trasferimento Atti Conversione Pene Pecuniarie</font>
        </td>
    </tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadTrasferisciAttiConversione" onsubmit="document.forms[0].go.disabled=true">
<table cellspacing=2 cellpadding=2>
	<tr>
  		<td class="titolo" colspan="3">Destinatario</td>
	</tr>
 	<tr>
		<td class="L">Magistrato di Sorveglianza <font class=ob>(*)</font></td>
   		<td class="L">
  	  		<select Title="Magistrato di Sorveglianza" class="small" name="<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>" >
				<%=tipoUDS%>
  			</select>
		</td>
		<td class="L" COLSPAN=2>
  			<input type="text" title="ufficio" value="<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[0].getUfficio().getDescrComune())%>" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>" maxlength="35" size="25">
			<a href="Javascript:ListaComuniUfficio('LoadTrasferisciAttiConversione','<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>');">
	     		<img src="/images/filefolder.gif" border=0>
   			</a> 
 		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
  	<tr>
    	<td>
      		<input name="go" class="bottone" type="submit" value="Conferma">
    	</td>
  	</tr>
  	<tr>
  		<td>
	  		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.rateizzazionepp.action.ActTrasferisciAttiConversione">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
		</td>
	</tr>
  </table>
</form>
</body>
</html>