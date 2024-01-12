<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-33_ aggiunta pagina di Avviso Batch PagoPA --%>
<%@ page import="f3b.web.IWebConstants"%>


<jsp:useBean id="BatchPagoPa" scope="request" class="siap.siep.pagoPaBatch.model.BatchPagopaModel" />
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>Avviso Batch PagoPA</title>
<script language="JavaScript">
</script>
</head>

<body class="corpo">
<FORM method="POST"  action="<%=IWebConstants.PG_MAIN%>" name="LoadDownloadAvvisoPagoPA"> 
<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.pagoPA.action.ActInvocaWSGeneraAvvisoPagoPA"/>
	
<div align=center id="richCert" style="visibility:visible;position:absolute;top:200px;left:200px">
<table bgcolor="#EEEEEE">
	<tr>
		<td>
			<center>
				<font size=+1 color=navy>Attenzione. Il batch notturno non è andato a buon fine!</font>
			</center>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td></tr>
	<% if (UtenteConnesso.getUserProfile().getProfileId().intValue() == 99 ) { %>
  <tr>
    <td>
      <center>
        <font size=+1 color=navy>E' possibile controllare l'esito dell'ultima esecuzione cliccando sull'icona sottostante!</font>
        <br><br><br>
        <a href="<%=IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.siep.pagoPaBatch.action.ActLoadDettaglioEsecuzioneBatchPagoPa&IdBatchPagoPa="+BatchPagoPa.getIdBatchPagopa()%>">
          <img src="/images/esegui.gif" alt="" width="32" height="32" border="0" align="middle" title="Verifica Batch PagoPA">
          <font class=label>Verifica Batch PagoPA</font>
        </a>
      </center>
    </td>
  </tr>	
  <% } else { %>
  <tr>
    <td>
      <center>
        <font size=+1 color=navy>Avvisare l'Amministratore di sistema</font>
      </center>
    </td>
  </tr>  
  <% } %>
</table>
</div>
</FORM>
</body>
</html>