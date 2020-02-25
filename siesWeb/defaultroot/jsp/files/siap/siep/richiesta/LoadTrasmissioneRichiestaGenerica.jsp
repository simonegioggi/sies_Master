<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina per trasmissione richiesta generica --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="eventonotifica" 	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="uffici" 			scope="request" class="java.lang.String"/>

<!-- LoadTrasmissioneRichiestaGenerica -->
<html>
<head>
  	<title>[S.I.E.S.] - Trasmissione Richiesta Generica</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  	<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
  	<script language="JavaScript">
	var desktop;
    function ListaComuniTds(formname,fieldname) {
		var codTipoSede = document.LoadTrasferisciRichiestaGenerica.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.value;
      	if (codTipoSede == "" || codTipoSede == '-') {
	  		alert("Ufficio Destinatario è obbligatorio");       
	  	} else {
      		desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname+"&typename="+codTipoSede, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	  	}
	}
  	</script>
</head>
  	<body class="corpo">
  	<table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
    			</a>
    		</td>
      		<td class="LBG">
      			<font class="label">Funzione :</font>&nbsp;&nbsp;
<%
String action = "siap.siep.richiesta.action.ActConfermaTrasmissioneRichiestaGenerica";
String descrMotivo = eventonotifica.getEvento().getDescrMotivo();
%>
        		<font class="campo">Trasferimento Atti - <%=descrMotivo%></font>
        	</td>
		</tr>
  	</table>
  	<br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  	<br>
  	<form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadTrasferisciRichiestaGenerica" onsubmit="document.forms[0].go.disabled=true">
    <table cellspacing="2" cellpadding="2">
		<tr>
        	<td class="l">Destinatario</td>
        	<td class="L">
         		<select Title="Destinatario" name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>">
           			<%=uffici%>
         		</select>
        	</td>
      	</tr>
      	<tr>
        	<td class="l">Sede Destinatario</td>
        	<td class="L">
          		<input title="Sede Destinatario" type="text" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>" maxlength="35" size="35">
           		<a href="Javascript:ListaComuniTds('LoadTrasferisciRichiestaGenerica','<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>');">
              		<img src="/images/filefolder.gif" border="0">
           		</a>
        	</td>
		</tr>
	  	<tr><td>&nbsp;</td></tr>
      	<tr>
        	<td>
          		<input name="go" class="bottone" type="submit" value="Conferma Trasmissione">
          		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=action%>">
		      	<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
        	</td>
		</tr>
    </table>
  	</form>
	</body>
</html>