<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.utente.action.ICostantiUtente"%>
<%@ page import="siap.sico.profilo.model.ProfiloModel"%>
<jsp:useBean id="utente" scope="request" class="siap.sico.utente.model.UtenteModel"/>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Utente </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript">
function confirmReset(Id)
{
   if (window.confirm("Premi OK per confermare il reset della password per l'utente "+Id))
       location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.utente.action.ActForzaPassword&<%=ICostantiUtente.CAMPO_COD_UTENTE%>="+Id;
}
</script>

</head>


		<body class="corpo">

    <table>
      <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Utente</font>
      </td>
      <%
	if( UtenteConnesso.isSysAdmin() || !utente.isSysAdmin() )
	{
      %>
	<td class="LBG">
	  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.utente.action.ActLoadInserisciUtente">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
	  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.utente.action.ActLoadModificaUtente&<%=ICostantiUtente.CAMPO_COD_UTENTE%>=<%=utente.getUserId()%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
	   <a href="Javascript:conferma('siap.sico.utente.action.ActCancellaUtente','<%=ICostantiUtente.CAMPO_COD_UTENTE%>','<%=utente.getUserId()%>')">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Disabilita" width="24" height="24" border="0"></a>
	   <a href="Javascript:confirmReset('<%=utente.getUserId()%>');"><img alt="Reset Password Utente" src="/images/lucchetto24.gif" border=0></a>
        </td>
	<%
	}
	%>
		<td>
   		<!-- BOTTONE DI RITORNO -->
   		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>   
		</td>
  </tr>
 </table>

<br>
<br>
<br>

		 <table cellspacing=2 cellpadding=2>
                <tr>
					<td class="int">Username</td><td class="l"><%=utente.getUserId() %></td>
				</tr>
				<tr>
					<td class="int">Cognome</td><td class="l"><%=utente.getCognome() %></td>
				</tr>
				<tr>
                	<td class="int">Nome</td><td class="l"><%=utente.getNome() %></td>
				</tr>
				<tr>
                	<td class="int">Telefono</td><td class="l"><%=StringUtils.toStringJSP(utente.getTelefono(),"-") %></td>
				</tr>
				<tr>
                	<td class="int">Fax</td><td class="l"><%=StringUtils.toStringJSP(utente.getFax(),"-") %></td>
				</tr>
				<tr>
                	<td class="int">E-mail</td><td class="l"><%=StringUtils.toStringJSP(utente.getEmail(),"-") %></td>
				</tr>
				<tr>
                	<td class="int" width=30%>Ufficio</td><td class="l"><%=utente.getUfficioUtente().getDescrTipoUfficio()+" DI <font color=navy>"+utente.getUfficioUtente().getDescrComune() %></font></td>
				</tr>
				<tr>
                	<td class="int">Profilo</td>

               <%
String Dprf=utente.getUserProfile().getDescription();
String sel="";
String cod=new String();
String desc=new String();
String GFV="";
String MFV="";
String AFV="";
String GLL="";
String MLL="";
String ALL="";
String GUM="";
String MUM="";
String AUM="";
String DUM="";
String DLL="";
String DFV="";
if (utente.getDataFineValidita()!=null)
{
  GFV=DateUtils.getDayToString(utente.getDataFineValidita());
  MFV=DateUtils.getMonthToString(utente.getDataFineValidita());
  AFV=DateUtils.getYearToString(utente.getDataFineValidita());
  DFV=DateUtils.getDateToString(utente.getDataFineValidita(),"dd/MM/yyyy");
}else
	DFV="-";
if (utente.getDataOraConnessione()!=null)
{
  GLL=DateUtils.getDayToString(utente.getDataOraConnessione());
  MLL=DateUtils.getMonthToString(utente.getDataOraConnessione());
  ALL=DateUtils.getYearToString(utente.getDataOraConnessione());
  DLL=DateUtils.getDateToString(utente.getDataOraConnessione(),"dd/MM/yyyy HH:mm:ss");
}else
  DLL="-";
if (utente.getDataUltimaModifcaPwd()!=null)
{
  GUM=DateUtils.getDayToString(utente.getDataUltimaModifcaPwd());
  MUM=DateUtils.getMonthToString(utente.getDataUltimaModifcaPwd());
  AUM=DateUtils.getYearToString(utente.getDataUltimaModifcaPwd());
  DUM=DateUtils.getDateToString(utente.getDataUltimaModifcaPwd(),"dd/MM/yyyy HH:mm:ss");
}else
  DUM="-";
%>
<td class="l"><%=Dprf%></td></tr>
               </td></tr>
		<tr>
               <td class="int" width=30%>Data Fine Validità</td>
			   <td class="l">
			  <%= DFV %>
			   </td>
		</tr>
		<tr>
               <td class="int" width=30%>Data Ultima Modifica Password</td>
			   <td class="l">
			   <%= DUM %>
			   </td>
		</tr>
		<tr>
               <td class="int" width=30%>Data Ultimo Login</td>
			   <td class="l">
			   <%=DLL%>
			   </td>
		</tr>
                <tr>
               <td class="int" width=30%>IP Ultimo Login</td>
			   <td class="l">
			   <%=StringUtils.toStringJSP(utente.getIP(),"-")%>
			   </td>
		</tr>


		</table>
		<!--  <br> -->
		<!-- <br> -->
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    	<%--  <form method=post name=f  onsubmit="document.f.go.disabled=true" action="<%= IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.sico.utente.action.ActListaUtenti"%>">--%>
		<%--  <input type="submit" name="go" value="Torna alla lista Utenti Attivi" class="bottone" > --%>
    </form>
		</body>
</html>