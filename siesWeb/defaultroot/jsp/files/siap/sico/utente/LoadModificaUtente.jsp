<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.utente.action.ICostantiUtente"%>
<%@ page import="siap.sico.profilo.model.ProfiloModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>


<jsp:useBean id="utente" scope="request" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="profili" scope="request" class="java.util.Vector"/>
<jsp:useBean id="uffici" scope="request" class="java.util.Vector"/>
<html>
<head>
<title>[S.I.E.S.] - Dettaglio Utente </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%> ></script>
<script language="JavaScript">
function Verify()
{
   if (document.f.<%= ICostantiUtente.CAMPO_GIORNO_DATA_FINE_VALIDITA %>.value.length==1)
       document.f.<%= ICostantiUtente.CAMPO_GIORNO_DATA_FINE_VALIDITA %>.value="0"+document.f.<%= ICostantiUtente.CAMPO_GIORNO_DATA_FINE_VALIDITA %>.value;
    if (document.f.<%= ICostantiUtente.CAMPO_MESE_DATA_FINE_VALIDITA %>.value.length==1)
       document.f.<%= ICostantiUtente.CAMPO_MESE_DATA_FINE_VALIDITA %>.value="0"+document.f.<%= ICostantiUtente.CAMPO_MESE_DATA_FINE_VALIDITA %>.value;
   var data_to_verify = document.f.<%= ICostantiUtente.CAMPO_GIORNO_DATA_FINE_VALIDITA %>.value+"/"+document.f.<%= ICostantiUtente.CAMPO_MESE_DATA_FINE_VALIDITA %>.value+"/"+document.f.<%= ICostantiUtente.CAMPO_ANNO_DATA_FINE_VALIDITA %>.value;

   if (!ControllaDataPassaVuota(data_to_verify))
   {
       alert ("Data Fine Scadenza Non Valida!");
       return false;
   } else
   return true;
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

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Modifica Utente</font>
      </td>

   </tr>
 </table>

<FORM method=post action="<%= IWebConstants.PG_MAIN %>" name=f>
<input type="hidden" value="siap.sico.utente.action.ActModificaUtente" name="<%=IWebConstants.ACTION_FIELD%>">
<input type="hidden" value="<%=utente.getUserId()%>" name="<%=ICostantiUtente.CAMPO_COD_UTENTE%>">
		 <table cellspacing=2 cellpadding=2>
                <tr>
					<td class="int">Username</td><td class="l"><%=utente.getUserId() %></td>
				</tr>
				<tr>
					<td class="int">Cognome</td><td class="l"><input maxlength=50 size=50 name="<%=ICostantiUtente.CAMPO_COGNOME%>" type="text" value="<%=utente.getCognome() %>"></td>
				</tr>
				<tr>
                	<td class="int">Nome</td><td class="l"><input maxlength=50 size=50 type="text" name="<%=ICostantiUtente.CAMPO_NOME%>" value="<%=utente.getNome() %>"></td>
				</tr>
				<tr>
                	<td class="int">Telefono</td><td class="l"><input maxlength=50 size=50 type="text" name="<%=ICostantiUtente.CAMPO_TELEFONO%>" value="<%=StringUtils.toStringJSP(utente.getTelefono()) %>"></td>
				</tr>
				<tr>
                	<td class="int">Fax</td><td class="l"><input maxlength=50 size=50 type="text" name="<%=ICostantiUtente.CAMPO_FAX%>" value="<%=StringUtils.toStringJSP(utente.getFax()) %>"></td>
				</tr>
				<tr>
                	<td class="int">E-mail</td><td class="l"><input maxlength=50 size=50 type="text" name="<%=ICostantiUtente.CAMPO_E_MAIL%>" value="<%=StringUtils.toStringJSP(utente.getEmail()) %>"></td>
				</tr>
				<tr>
                	<td class="int" width=30%>Ufficio</td>

<%
   String sel= "";
   if (utente.getUfficioUtente()!=null) {
   %>
 	<td class="l"><input type="Hidden" value="<%=utente.getUfficioUtente().getCodUfficio()%>" name="cod_uff">
 	<%=utente.getUfficioUtente().getDescrTipoUfficio()%> DI <%=utente.getUfficioUtente().getDescrComune()%></td>
<%
}
else {%>
	<td class="c"><input type="Hidden" value="" name="ufficioutente">-</td>
<% }%>
				</tr>
				<tr>
                	<td class="int">Profilo</td>
					<td class="l"><select name="cod_prf">
               <%
String cdprf=utente.getUserProfile().getProfileId()+"";
 sel="";
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
if (utente.getDataFineValidita()!=null)
{
  GFV=DateUtils.getDayToString(utente.getDataFineValidita());
  MFV=DateUtils.getMonthToString(utente.getDataFineValidita());
  AFV=DateUtils.getYearToString(utente.getDataFineValidita());
}
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
for (int i=0;i<profili.size();i++)
{
cod=((ProfiloModel)profili.get(i)).getCodProfilo()+"";
desc=((ProfiloModel)profili.get(i)).getDescrizione();
              if (cod.equalsIgnoreCase(cdprf))
              {    sel=" SELECTED";}
              else
                  sel="";
			   		%>
						<option value="<%= cod %>"<%= sel %>><%= desc %></option>
					<%
}
			   %></select>
               </td></tr>
		<tr>
               <td class="int" width=30%>Data Fine Validità</td>
			   <td class="l">
			   <input size=2 maxlength=2 type="text" name="<%=ICostantiUtente.CAMPO_GIORNO_DATA_FINE_VALIDITA%>" value="<%=GFV%>">/
               <input size=2 maxlength=2 type="text" name="<%=ICostantiUtente.CAMPO_MESE_DATA_FINE_VALIDITA%>" value="<%=MFV%>">/
               <input size=4 maxlength=4 type="text" name="<%=ICostantiUtente.CAMPO_ANNO_DATA_FINE_VALIDITA%>" value="<%=AFV%>">
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
		<tr>
               <td class="int" width=30%>Reset Password</td>
			   <td class="l">
			    <a href="Javascript:confirmReset('<%=utente.getUserId()%>');"><img src="/images/lucchetto24.gif" border=0></a>
			   </td>
		</tr>
		<tr>
			   <input name="flag" value="no" type="Hidden">
               <td class="l" colspan=2><input type="submit" class=bottone name="go" value="Salva Modifiche"></td>

		</tr>

		</table>
 </FORM>

 <script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("f");

  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_NOME %>","req","Il campo Nome è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_NOME %>","maxlen=50","La lunghezza massima per il nome è di 50 caratteri");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_NOME %>","alpha","Il campo Nome Utente non può contenere caratteri non alfabetici");

  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COGNOME %>","req","Il campo Cognome è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COGNOME %>","maxlen=50","La lunghezza massima per il cognome è di 50 caratteri");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COGNOME %>","alpha","Il campo Cognome Utente non può contenere caratteri non alfabetici");

  frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>