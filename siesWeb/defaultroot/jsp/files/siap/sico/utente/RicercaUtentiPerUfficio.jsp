<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.utente.model.UtenteViewModel"%>
<%@ page import="siap.sico.utente.action.ICostantiUtente"%>

<html>
<head>
<title>
RicercaUtentiPerUfficio
</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript">
var str;
function conferma(a_action,a_entityname,a_entityvalue)
{
	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue;
	if (window.confirm('Confermi la disattivazione ?'))
        {
        	window.location.href=str;
        }
}

/* versione della funzione con parametro aggiunto */
/* a_destnname, a_destvalue  : nome e valore del Request parameter che definisce l'azione da eseguire dopo
  la prima di cancellazione */
function conferma(a_action,a_entityname,a_entityvalue, a_destnname, a_destvalue )
{
	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue +  "&" + a_destnname + "=" +a_destvalue;
	if (window.confirm('Confermi la disattivazione ?'))
        {
        	window.location.href=str;
        }
}
</script>

<script language="JavaScript">
function confirmReset(Id)
{
   if (window.confirm("Premi OK per confermare il reset della password per l'utente "+Id))
       location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.utente.action.ActForzaPassword&<%=ICostantiUtente.CAMPO_COD_UTENTE%>="+Id;
}
</script>
</head>
<jsp:useBean id="utenti" scope="request" class="java.util.Vector"/>
<jsp:useBean id="ufficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="sede" scope="request" class="java.lang.String"/>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>

<body class="corpo">
<table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Lista Utenti <% if (!ufficio.equals("-")) { %> Per L'Ufficio <%=ufficio%> Della Sede <%=sede%><% }%></font></td>
    </tr>
</table>
<div style="width: 100%;">
	<table cellpadding="2" cellspacing="2">
		<tr>
			<td class="int">Codice Utente</td>
			<td class="int">Nome</td>
			<td class="int">Cognome</th>
			<td class="int">Data Fine Validità</td>
			<td class="int">Data Ora Connessione</td>
			<td class="int">Data Inserimento</td>
			<td class="int">Data Ultima Modifica Password</td>
			<td class="int">IP</td>
			<td class="int" width="9%">Azioni</td>
		</tr>
		<%
                UtenteModel utente=new UtenteModel();
                for (int i=0;i<utenti.size();i++)
                {
                	utente = (UtenteModel)utenti.get(i);
		%>
		<tr>
      			<td class="l"><font class="campoSmall"><%=utente.getUserId() %></font></td>
			<td class="l"><font class="campoSmall"><%=utente.getNome() %></font></td>
      			<td class="l"><font class="campoSmall"><%=utente.getCognome() %></font></td>
      			<td class="c"><font class="campoSmall"><%=StringUtils.toStringJSP(DateUtils.getDateToString(utente.getDataFineValidita(),"dd-MM-yyyy"),"-")%></font></td>
			<td class="c"><font class="campoSmall"><%=StringUtils.toStringJSP(DateUtils.getDateToString(utente.getDataOraConnessione(),"dd-MM-yyyy hh:mm"),"-")%></font></td>
			<td class="c"><font class="campoSmall"><%=StringUtils.toStringJSP(DateUtils.getDateToString(utente.getDataInserimento(),"dd-MM-yyyy"),"-")%></font></td>
      			<td class="c"><font class="campoSmall"><%=StringUtils.toStringJSP(DateUtils.getDateToString(utente.getDataUltimaModifcaPwd(),"dd-MM-yyyy"),"-")%></font></td>
		 	<td class="c"><font class="campoSmall"><%=StringUtils.toStringJSP(utente.getIP(),"-") %></font></td>
    			<td class="c">

                        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.utente.action.ActLoadDettaglioUtente&<%=ICostantiUtente.CAMPO_COD_UTENTE%>=<%=utente.getUserId()%>"><img  alt="Dettaglio Utente" src="/images/dettagli.gif" border=0></a>
			<%
			if( UtenteConnesso.isSysAdmin() )
			{
			%>
				<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.utente.action.ActLoadModificaUtente&<%=ICostantiUtente.CAMPO_COD_UTENTE%>=<%=utente.getUserId()%>"><img  alt="Modifica Utente" src="/images/modifica.gif" border=0></a>
				<a href="Javascript:conferma('siap.sico.utente.action.ActCancellaUtente','<%=ICostantiUtente.CAMPO_COD_UTENTE%>','<%=utente.getUserId()%>')"><img  alt="Disabilitazione Utente"  src="/images/delete.gif" border=0></a>
                		<a href="Javascript:confirmReset('<%=utente.getUserId()%>');"><img alt="Reset Password Utente" src="/images/lucchetto.gif" border=0></a>
			<%
			}
			%>
			</td>
		</tr>
              <% } %>
	</table>
</div>
</body>
</html>