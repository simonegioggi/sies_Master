<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.utente.model.UtenteViewModel"%>
<%@ page import="siap.sico.utente.action.ICostantiUtente"%>
<jsp:useBean id="utenti" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tip" scope="request" class="java.lang.String"/>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Profilo </title>

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


<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;
        <font class="campo">Lista Utenti <%= tip.equals("attivi") ? " Attivi" : " Non Attivi" %></font>
       </td> <td class="LBG">
      <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.utente.action.ActLoadInserisciUtente">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
    </td>
</tr>
 </table>

</FORM>

<jsp:include page="<%= IWebConstants.PAGINAZIONE_RICERCA %>"/>

		 <table cellspacing=2 cellpadding=2>
                 <tr>
				<td class="int">Username</td>
				<td class="int">Cognome</font></td>
                                <td class="int">Nome</td>
                                <td class="int" width=30%>Ufficio</td>
                                <td class="int">Profilo</td>
                               <!-- <td class="int">Data Fine Validità</td>-->
                                <td class="int">Azioni</td>
		</tr>
<%
UtenteViewModel utente=new UtenteViewModel();
for (int i=0;i<utenti.size();i++)
{
   utente=(UtenteViewModel)utenti.get(i);
%>

	<tr>
        	<td class="l"><font class="campoSmall"><%=utente.getUserId() %></font></td>
                <td class="l"><font class="campoSmall"><%=utente.getCognome() %></font></td>
                <td class="l"><font class="campoSmall"><%=utente.getNome() %></font></td>
                <td class="l"><font class="campoSmall"><%=utente.getDescTipoUfficio()+" DI <font color=navy>"+utente.getComuneUfficio() %></font></font></td>
                <td class="c"><font class="campoSmall"><%=utente.getDescProfilo() %></font></td>
                <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
                <%--<td class="c"><font class="campoSmall">< %=StringUtils.toStringJSP(DateUtils.getDateToString(utente.getDataFineValidita(),"dd-MM-yyyy"),"-")%> </font></td>--%>
                <td class="c">
<%
	if (tip.equals("attivi") || !UtenteConnesso.isSysAdmin() )
	{
%>
	<a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.utente.action.ActLoadDettaglioUtente&<%=ICostantiUtente.CAMPO_COD_UTENTE%>=<%=utente.getUserId()%>"><img  alt="Dettaglio Utente" src="/images/dettagli.gif" border=0></a>
<% 	}	%>

<% if( UtenteConnesso.isSysAdmin() )
   {
%>
	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.utente.action.ActLoadModificaUtente&<%=ICostantiUtente.CAMPO_COD_UTENTE%>=<%=utente.getUserId()%>"><img  alt="Modifica Utente" src="/images/modifica.gif" border=0></a>
<% 	if (tip.equals("attivi"))
   	{
%>
            <a href="Javascript:conferma('siap.sico.utente.action.ActCancellaUtente','<%=ICostantiUtente.CAMPO_COD_UTENTE%>','<%=utente.getUserId()%>')"><img  alt="Disabilitazione Utente"  src="/images/delete.gif" border=0></a>
            <a href="Javascript:confirmReset('<%=utente.getUserId()%>');"><img alt="Reset Password Utente" src="/images/lucchetto.gif" border=0></a>
<% 	}
  }%>
</td>
		</tr>

<%}%>
		</table>	</body>
</html>