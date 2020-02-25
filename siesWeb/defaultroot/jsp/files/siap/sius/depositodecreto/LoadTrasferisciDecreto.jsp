<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.jms.ICostantiJMS"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.model.UfficiProvvedimentoModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="uffici" scope="request" class="java.lang.String"/>
<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="decreto" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="ufficiInteressati" scope="request" class="java.util.Vector"/>
<jsp:useBean id="postTitle" scope="request" class="java.lang.String"/>
<jsp:useBean id="UEPE" scope="request" class="java.lang.String"/>

<html>
	<head>
  	<title>[S.I.E.S.] - Trasferimento Decreto</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  	<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
  	<script language="JavaScript">
    var desktop;
    // Lista Uffici per TIPO_UFFICIO.
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio) {
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
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
      			<font class="label">Funzione: </font>
<%
String lAction = "siap.sius.depositodecreto.action.ActConfermaTrasmissione";
if (postTitle.compareTo("") == 0) {
%>
          		<font class="campo">Trasferimento Decreto</font>
<%
} else {
%>
          		<font class="campo"><%=postTitle%></font>
<%
}
%>
			</td>
		</tr>
  	</table>
  	<br>
	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  	<br>
  	<form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadTrasferisciDecreto">
    	<td>
			<font class="campo">Decreto N. <%=StringUtils.toStringJSP(decreto.getAnnoS72())%>/<%=StringUtils.toStringJSP(decreto.getNumS72())%></font>
			<font class="Label"> del </font>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decreto.getDataEmissione(), "dd-MM-yyyy"))%></font>
			<font class="Label"> depositato il </font>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decreto.getDataDeposito(), "dd-MM-yyyy"))%></font>
		</td>
    	<br>
    	<table width="100%" cellspacing=2 cellpadding=2>
      	<tr>
	        <td class="l"><font class="label">Data Deposito</font></td>
	        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decreto.getDataDeposito(), "dd-MM-yyyy"))%>&nbsp;</font></td>
      	</tr>
      	<tr>
	        <td class="l"><font class="label">Natura Provvedimento</font></td>
	        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(evento.getDescrEsito())%>&nbsp;</font></td>
      	</tr>
      	<tr>
	        <td class="l"><font class="label">Oggetto Procedimento</font></td>
	        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(evento.getDescrMotivo())%>&nbsp;</font></td>
      	</tr>
      	<tr><td>&nbsp;</td></tr>
   	</table>
<%
// STUB 29/06/2004 Se l'ordinanza riguarda un procedimento di EMA o unificante, è stato preventivamente
// preparato un elenco di uffici di esecuzione interessati alla ricezione del provvedimento.
Iterator itx = ufficiInteressati.iterator();
boolean presenzaDestinatari = false;
if (itx.hasNext()) {
  	presenzaDestinatari = true;
%>

	<table cellspacing=2 cellpadding=2>
		<tr>
          	<td class="label" width="25%" colspan ="2">Selezionare gli Uffici di esecuzione destinatari del provvedimento:</td>
        </tr>
<%-- MEV_39: modificato invio uffici interessato --%>
<%
		int indice = 0;
		while (itx.hasNext()) {
        	UfficiProvvedimentoModel lUffProvModel = (UfficiProvvedimentoModel) itx.next();
          	boolean checked = false;
%>
		<tr>
            <td class="l" colspan="2">
            	<font class="campo">
				<%=lUffProvModel.getDescrTipoUfficio()%>&nbsp;-&nbsp;<%=lUffProvModel.getDescrComune()%>
              	<% if (Utils.isPresent(lUffProvModel.getChiaveAnnoSiep())
            		  && Utils.isPresent(lUffProvModel.getChiaveProgrSiep())) {
              		checked = true;
            	%>
                -&nbsp;Fascicolo SIEP <%=lUffProvModel.getChiaveAnnoSiep()%>&nbsp;/&nbsp;<%=lUffProvModel.getChiaveProgrSiep()%>
                <% } else { %>
                -&nbsp;PER L'ESECUZIONE
                <% } %>
              	</font>
			</td>
            <% if (checked) { %>
            <td class=l width="1%"><input type='checkbox' name='lCheckUffici<%=indice%>' CHECKED></td>
            <% } else { %>
            <td class=l width="1%"><input type='checkbox' name='lCheckUffici<%=indice%>'></td>
            <% } %>
            <input type='hidden' name='lCodUfficio' value=<%=lUffProvModel.getCodUfficio()%>>
            <input type='hidden' name='lChiaveAnno' value=<%=lUffProvModel.getChiaveAnnoSiep()%>>
            <input type='hidden' name='lChiaveProgr' value=<%=lUffProvModel.getChiaveProgrSiep()%>>
		<tr>
<%
		indice++;
	}
%>
	</table>
<%
}
%>

	<table cellspacing="2" cellpadding="2">
		<tr><td>&nbsp;</td></tr>
<%
if (presenzaDestinatari) {
%>
		<tr>
			<td class="label" width="25%" colspan ="2">Eventuali Ulteriori destinatari</td>
		</tr>
<%
}
%>
      	<tr>
	        <td class="l">Destinatario </td>
	        <td class="L">
         		<select Title="Destinatario" name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" >
           			<%=uffici%>
         		</select>
        	</td>
      	</tr>
      	<tr>
        	<td class="l">Sede Destinatario </td>
        	<td class="L">
          		<input title="Sede Destinatario"  type="text" name="<%= ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO %>"  maxlength="35" size="35">
           		<a href="Javascript:ListaUfficiPerTipo('LoadTrasferisciDecreto','<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>[0]',document.LoadTrasferisciDecreto.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[0][document.LoadTrasferisciDecreto.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[0].selectedIndex].value);">
              		<img src="/images/filefolder.gif" border=0>
            	</a>
        	</td>
      	</tr>
      	<tr><td>&nbsp;</td></tr>
      	<tr>
	        <td class="l">UEPE Destinatario </td>
	        <td class="L">
         		<select Title="UEPE Destinatario" name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" >
           			<%=UEPE%>
         		</select>
        	</td>
      	</tr>
      	<tr>
        	<td class="l">Sede UEPE Destinatario </td>
        	<td class="L">
          		<input title="Sede UEPE Destinatario"  type="text" name="<%= ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO %>"  maxlength="35" size="35">
           		<a href="Javascript:ListaUfficiPerTipo('LoadTrasferisciDecreto','<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>[1]',document.LoadTrasferisciDecreto.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[1][document.LoadTrasferisciDecreto.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[1].selectedIndex].value);">
              		<img src="/images/filefolder.gif" border=0>
            	</a>
        	</td>
      	</tr>
      	<tr><td>&nbsp;</td></tr>
      	<tr>
        	<td>
          		<input class="bottone" type="submit" value="Conferma">
        	</td>
      	</tr>
      	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
      	<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=evento.getIdEvento()%>">
<%
if (postTitle.compareTo("") != 0) {
%>
		<input type="HIDDEN" name="codTipoOperazione" value="<%=ICostantiJMS.TRASFERIMENTO_RICORSO%>">
<%
}
%>
	</table>
	</form>
</body>
</html>