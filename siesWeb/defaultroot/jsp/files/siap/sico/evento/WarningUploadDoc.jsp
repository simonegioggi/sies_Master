<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<html>
<head>
<title>[S.I.E.S.] - Warning Upload Document</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<SCRIPT LANGUAGE="JavaScript">
// preload images:
if (document.images) {
	clickme1 = new Image(58,17); clickme1.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif";
	clickme2 = new Image(58,17); clickme2.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01-down.gif";
	clickme3 = new Image(58,17); clickme3.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01.gif";
	clickme4 = new Image(58,17); clickme4.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01-down.gif";
}

function hiLite(imgName,imgObjName) {
	if (document.images) {
    	document.images[imgName].src = eval(imgObjName + ".src");
  	}
}
//-->
</SCRIPT>
<!-- Fine Definizione Script BOTTONI GRAFICI -->
<%
String message = (String) request.getAttribute(IWebConstants.MESSAGE_TEXT);
%>
<script language="JavaScript">
// funzione collegata al bottone NO
function stop() {
	history.back();
}
</script>
</head>

<body class="corpo">
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="WarningUpload">
<br><br><br><br><br>
<table width="300"  cellspacing="0" align="center" class="tab">
	<tr>
		<td class="c">
        	<table width="300" cellspacing="0" align="center" class="tab">
           		<tr align="center" valign="middle">
             		<td align="center" colspan="2" class="tab">
               			<p>&nbsp;<p>
               			<B><%=message%></B>
             		</td>
           		</tr>
           		<tr><td><br></td></tr>
       			<tr align="center" valign="middle">
             		<td class="c" colspan="2" class="tab">
                		<B>OK per proseguire comunque;<br>Annulla per annullare l'operazione.</B>
             		</td>
             	</tr>
             	<tr><td><br></td></tr>
           		<tr align="center" valign="middle">
             		<td align="center" class="tab2" width="50%">
               			<a href="javascript:document.WarningUpload.submit();" onMouseOver="hiLite('img01','clickme2')" onMouseOut="hiLite('img01','clickme1')">
                  			<IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif" BORDER="0" ALT="" NAME="img01">
               			</a>
             		</td>
             		<td align="center"  class="tab2" width="50%">
               			<a href="javascript:stop();" onMouseOver="hiLite('img02','clickme4')" onMouseOut="hiLite('img02','clickme3')">
                  			<IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01.gif" BORDER="0" ALT="" NAME="img02">
               			</a>
             		</td>
          		</tr>
   			</table>
   		</td>
 	</tr>
</table>
<input  type="HIDDEN" name="<%=ICostantiEvento.CAMPO_CK_WARNING%>" value="S">
<% if( request.getAttribute(IWebConstants.ACTION_FIELD) != null) {%>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"  value="<%=(String) request.getAttribute(IWebConstants.ACTION_FIELD)%>">
<%}else {%>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
<% } %>
<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%=(String) request.getAttribute(ICostantiEvento.CAMPO_ID_EVENTO)%>">
<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="<%=(String) request.getAttribute(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO)%>">
<% if( request.getAttribute(ICostantiEvento.CAMPO_VALIDA) != null) {%>
<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_VALIDA%>"  value="<%=(String) request.getAttribute(ICostantiEvento.CAMPO_VALIDA)%>">
<%}%>
<!-- MEV_AVVOCATURA -->
<% if( request.getAttribute("FlagAvvocatura") != null) {%>
<input type="HIDDEN" name="FlagAvvocatura"  value="<%=(String) request.getAttribute("FlagAvvocatura")%>">
<%}%>
<!-- //@emma 09072018 intervento post COLLAUDO 11.2 -->
<% if( request.getAttribute("validazioneEsito") != null) {%>
<input type="HIDDEN" name="validazioneEsito"  value="<%=(String) request.getAttribute("validazioneEsito")%>">
<%}%>
<% if( request.getAttribute(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE) != null) {%>
<input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>"  value="<%=(BigDecimal) request.getAttribute(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE)%>">
<%}%>
</FORM>
</body>
</html>