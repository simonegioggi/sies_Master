<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-13_ aggiunta pagina di esito generazione avviso PagoPA --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="idFascicolo"   scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"        scope="request" class="siap.sico.evento.model.EventoModel"/>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>Visualizza Avviso PagoPA</title>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

<script language="JavaScript">
function VisualizzaAvviso(lAzione) {
	var  hrefStampa = lAzione;
  	var lIndice = hrefStampa.indexOf("?");
	var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
	stampa2("/jsp/files/Stampa.jsp", parametri);
	lookUpload();
}
</script>
</head>

<body class="corpo" onLoad="VisualizzaAvviso('/jsp/Main.jsp?Action=siap.siep.pagoPA.action.ActLoadAvvisoPagoPA&IDFascicolo=<%=idFascicolo%>')"> 

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="VisualizzaAvvisoPagoPA">
<table>
    <tr>
	    <td class="LBG">
	    	<a href="Javascript:window.print();">
	    		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
	    	</a>
	    </td>
	    <td class="LBG">
	    	<font class="label">Funzione :</font>&nbsp;&nbsp;
	    	<font class="campo">Risultato Generazione Avviso PagoPA</font>
		</td>
   	</tr>
</table>

<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<br>
<table>
	<tr>
      	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
</table>
<br>
<input type="hidden" name="IDFascicolo" value="<%=idFascicolo%>"/>
<input type="hidden" name="idEvento" 	value="<%=evento.getIdEvento()%>"/>

<div align=center id="visualizzaCertificato" style="visibility:visible;position:absolute;top:150px;left:250px">
<table bgcolor="#EEEEEE">
	<tr>
		<td>
			<font size=+1 color=navy>La Generazione dell'Avviso PagoPA è andata a buon fine.</font>
		</td>
	</tr>
</table>
</div>
</FORM>
</body>
</html>