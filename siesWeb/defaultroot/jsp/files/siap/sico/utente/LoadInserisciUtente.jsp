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
        <font class="campo">Inserimento Utente</font>
      </td>
   </tr>
 </table>

<FORM method=post action="<%= IWebConstants.PG_MAIN %>" name=f>
	<input type="hidden" value="siap.sico.utente.action.ActInserisciUtente" name="<%=IWebConstants.ACTION_FIELD%>">
	<table cellspacing=2 cellpadding=2>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
		<%--
		<tr>
			<td class="int">Username</td><td class="l"><input name="<%=ICostantiUtente.CAMPO_COD_UTENTE%>" VALUE="" type="text"></td>
		</tr>
		--%>
		<tr>
			<td class="int">Cognome</td><td class="l"><input maxlength=50 size=50 name="<%=ICostantiUtente.CAMPO_COGNOME%>" type="text" value=""></td>
		</tr>
		<tr>
        	<td class="int">Nome</td><td class="l"><input maxlength=50 size=50 type="text" name="<%=ICostantiUtente.CAMPO_NOME%>" value=""></td>
		</tr>
		<tr>
              <td class="int">Telefono</td><td class="l"><input maxlength=50 size=50 type="text" name="<%=ICostantiUtente.CAMPO_TELEFONO%>" value=""></td>
		</tr>
		<tr>
              <td class="int">Fax</td><td class="l"><input maxlength=50 size=50 type="text" name="<%=ICostantiUtente.CAMPO_FAX%>" value=""></td>
		</tr>
		<tr>
              <td class="int">E-mail</td><td class="l"><input maxlength=50 size=50 type="text" name="<%=ICostantiUtente.CAMPO_E_MAIL%>" value=""></td>
		</tr>
		<tr>
              <td class="int">Ufficio</td>
              <td class="l"><select name="cod_uff" class=small>
<%

String cod;
String desc;
for (int i=0;i<uffici.size();i++)
{
cod=((UfficioModel)uffici.get(i)).getCodUfficio()+"";
desc=((UfficioModel)uffici.get(i)).getDescrTipoUfficio()+ " di " +((UfficioModel)uffici.get(i)).getDescrComune()+ " ("+((UfficioModel)uffici.get(i)).getCodProvincia()+ ") - "+StringUtils.toStringJSP(((UfficioModel)uffici.get(i)).getIndirizzo());

			   		%>
						<option value="<%= cod %>"><%= desc %></option>
					<%
}%>
                       </select> </td>
				</tr>
				<tr>
                	<td class="int">Profilo</td>
					<td class="l"><select name="cod_prf">
<%


for (int i=0;i<profili.size();i++)
{
cod=((ProfiloModel)profili.get(i)).getCodProfilo()+"";
desc=((ProfiloModel)profili.get(i)).getDescrizione();

			   		%>
						<option value="<%= cod %>"><%= desc %></option>
					<%
}
			   %></select>
               </td>
		</tr>

		<tr>
               <td class="int">Data Fine Validità</td>
			   <td class="l">
			   <input size=2 maxlength=2 type="text" name="<%=ICostantiUtente.CAMPO_GIORNO_DATA_FINE_VALIDITA%>" value="">/
               <input size=2 maxlength=2 type="text" name="<%=ICostantiUtente.CAMPO_MESE_DATA_FINE_VALIDITA%>" value="">/
               <input size=4 maxlength=4 type="text" name="<%=ICostantiUtente.CAMPO_ANNO_DATA_FINE_VALIDITA%>" value="">
			   </td>
		</tr>

			<input type="Hidden" name="flag" value="no">

		<tr>
               <td class="l" colspan=2><input type="submit" class=bottone name="go" value="Inserisci"></td>

		</tr>

		</table>
    </FORM>

  <script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("f");

  /*frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COD_UTENTE %>","req","Il campo Username Utente è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COD_UTENTE %>","maxlen=100","La lunghezza massima per username è di 100 caratteri");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COD_UTENTE %>","alpha");*/


  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_NOME %>","req","Il campo Nome Utente è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_NOME %>","maxlen=50","La lunghezza massima per il nome è di 50 caratteri");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_NOME %>","alpha","Il campo Nome Utente non può contenere caratteri non alfabetici");

  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COGNOME %>","req","Il campo Cognome Utente è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COGNOME %>","maxlen=50","La lunghezza massima per il cognome è di 50 caratteri");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COGNOME %>","alpha","Il campo Cognome Utente non può contenere caratteri non alfabetici");
  frmvalidator.setAddnlValidationFunction("Verify");
  </script>
    	</body>
</html>