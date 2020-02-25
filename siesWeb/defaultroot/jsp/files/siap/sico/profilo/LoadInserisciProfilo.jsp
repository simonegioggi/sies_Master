<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.profilo.model.ProfiloModel"%>
<%@ page import="siap.sico.profilo.action.ICostantiProfilo"%>
<jsp:useBean id="profilo" scope="request" class="siap.sico.profilo.model.ProfiloModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<html>
<head>
<title>[S.I.E.S.] - GestioneProfilo </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%> ></script>
<script language="JavaScript">
function Verify()
{
   if (document.LoadInserisciProfilo.<%= ICostantiProfilo.CAMPO_GIORNO_DATA_FINE_VALIDITA %>.value.length==1)
       document.LoadInserisciProfilo.<%= ICostantiProfilo.CAMPO_GIORNO_DATA_FINE_VALIDITA %>.value="0"+document.LoadInserisciProfilo.<%= ICostantiProfilo.CAMPO_GIORNO_DATA_FINE_VALIDITA %>.value;
    if (document.LoadInserisciProfilo.<%= ICostantiProfilo.CAMPO_MESE_DATA_FINE_VALIDITA %>.value.length==1)
       document.LoadInserisciProfilo.<%= ICostantiProfilo.CAMPO_MESE_DATA_FINE_VALIDITA %>.value="0"+document.LoadInserisciProfilo.<%= ICostantiProfilo.CAMPO_MESE_DATA_FINE_VALIDITA %>.value;
   var data_to_verify = document.LoadInserisciProfilo.<%= ICostantiProfilo.CAMPO_GIORNO_DATA_FINE_VALIDITA %>.value+"/"+document.LoadInserisciProfilo.<%= ICostantiProfilo.CAMPO_MESE_DATA_FINE_VALIDITA %>.value+"/"+document.LoadInserisciProfilo.<%= ICostantiProfilo.CAMPO_ANNO_DATA_FINE_VALIDITA %>.value;

   if (!ControllaDataPassaVuota(data_to_verify))
   {
       alert ("Data Fine Scadenza Non Valida!");
       return false;
   }
}
</script>
</head>


		<body class="corpo">
			<table>
			<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			 <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
			 ProfiloModel lProfilo = new ProfiloModel();
			 String lAzione = new String();
			 if( modalita.equals("I") )
			 {
			   lAzione = "siap.sico.profilo.action.ActInserisciProfilo";

%>

			<font class="campo">Inserimento di un Profilo</font>
			 <%

			   }

			   else if( modalita.equals("M") )

			   {

			   lAzione = "siap.sico.profilo.action.ActModificaProfilo";
			     lProfilo = profilo;
			  %>			     <font class="campo">Modifica di un Profilo</font>
			  <%}%>			 </td>
</tr>
</table>
		<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciProfilo">
		 <table cellspacing=4 cellpadding=4>
                 <input value="<%=lProfilo.getCodProfilo() %>" type="hidden" name="<%= ICostantiProfilo.CAMPO_COD_PROFILO %>">
                 <input value="<%=lAzione %>" type="hidden" name="<%= IWebConstants.ACTION_FIELD %>">
		<tr>
				<td class="l">Descrizione</td>
				<td class="l"><input size=80 maxlength=60 value="<%=lProfilo.getDescrizione() %>" type="text" name="<%= ICostantiProfilo.CAMPO_DESCRIZIONE %>"  ></td>
		</tr>
		<tr>
				<td class="l">Data Fine Validita</td>
<% if (lProfilo.getDataFineValidita()!=null)
{
%>
				<td class="l"><input value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(lProfilo.getDataFineValidita())) %>" type="text" size="2" maxlength="2" name="<%= ICostantiProfilo.CAMPO_GIORNO_DATA_FINE_VALIDITA %>"  >
				/<input value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(lProfilo.getDataFineValidita())) %>" type="text" size="2" maxlength="2" name="<%= ICostantiProfilo.CAMPO_MESE_DATA_FINE_VALIDITA %>"  >
				/<input value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(lProfilo.getDataFineValidita())) %>" type="text" size="4" maxlength="4" name="<%= ICostantiProfilo.CAMPO_ANNO_DATA_FINE_VALIDITA %>"  ></td>
<%}else
{
%>
				<td class="l"><input value="" type="text" size="2" maxlength="2" name="<%= ICostantiProfilo.CAMPO_GIORNO_DATA_FINE_VALIDITA %>"  >
                                /<input value="" type="text" size="2" maxlength="2" name="<%= ICostantiProfilo.CAMPO_MESE_DATA_FINE_VALIDITA %>"  >
				/<input value="" type="text" size="4" maxlength="4" name="<%= ICostantiProfilo.CAMPO_ANNO_DATA_FINE_VALIDITA %>"  ></td>
<%}%>
		</tr>
<tr>
				<td class="l"><input type=submit value="Salva Modifiche"> </td>

		</tr>
</table>

		</form>
 <script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciProfilo");
frmvalidator.addValidation("<%= ICostantiProfilo.CAMPO_DESCRIZIONE%>","req");
frmvalidator.setAddnlValidationFunction("Verify");
 </script>
	</body>
</html>