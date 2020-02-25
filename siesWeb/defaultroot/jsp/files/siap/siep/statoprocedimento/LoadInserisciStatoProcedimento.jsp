<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.statoprocedimento.action.ICostantiStatoProcedimento"%>

<html>

<jsp:useBean id="statoprocedimento" scope="request" class="java.lang.String"/>

<head>
<title>[S.I.E.S.] - Stato del Procedimento </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
function Verifica()
{
      if (document.LoadInserisciStatoProcedimento.<%=ICostantiStatoProcedimento.CAMPO_GIORNO_DATA%>.value.length==1)
       document.LoadInserisciStatoProcedimento.<%=ICostantiStatoProcedimento.CAMPO_GIORNO_DATA%>.value='0'+document.LoadInserisciStatoProcedimento.<%=ICostantiStatoProcedimento.CAMPO_GIORNO_DATA%>.value;
      if (document.LoadInserisciStatoProcedimento.<%=ICostantiStatoProcedimento.CAMPO_MESE_DATA%>.value.length==1)
       document.LoadInserisciStatoProcedimento.<%=ICostantiStatoProcedimento.CAMPO_MESE_DATA%>.value='0'+document.LoadInserisciStatoProcedimento.<%=ICostantiStatoProcedimento.CAMPO_MESE_DATA%>.value;
        var data_to_verify = document.LoadInserisciStatoProcedimento.<%=ICostantiStatoProcedimento.CAMPO_GIORNO_DATA%>.value+'/'+document.LoadInserisciStatoProcedimento.<%=ICostantiStatoProcedimento.CAMPO_MESE_DATA%>.value+'/'+document.LoadInserisciStatoProcedimento.<%=ICostantiStatoProcedimento.CAMPO_ANNO_DATA%>.value;

   if (!ControllaData(data_to_verify))
      {
        alert('Data non valida');
        return false;
      }

    if(document.LoadInserisciStatoProcedimento.<%=ICostantiStatoProcedimento.CAMPO_COD_STATO_PROCEDIMENTO%>.value=="-")
      {
        alert("Stato del Procedimento obbligatorio");
        return false;
      }
   document.LoadInserisciStatoProcedimento.INSERISCI.disabled=true;
}
</script>
</head>
<body class="corpo">
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
     <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Assegnazione Stato del Procedimento</font>
     </td>
    </tr>
   </table>
    <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
		<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciStatoProcedimento">
     <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.statoprocedimento.action.ActInserisciStatoProcedimento">

		 <table cellspacing=2 cellpadding=2>
      <tr>
         <td class="l">Stato del Procedimento <font class=ob>(*)</font></td>
         <td class="L">
            <select  Title="Stato Procedimento"  class="small" name="<%=ICostantiStatoProcedimento.CAMPO_COD_STATO_PROCEDIMENTO%>">
             <%=statoprocedimento%>
            </select>
         </td>
     </tr>

		 <tr>
				<td class="l">Data</td>
				<td class="l">
        <input title="Giorno" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiStatoProcedimento.CAMPO_GIORNO_DATA %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
				-
        <input title="Mese" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiStatoProcedimento.CAMPO_MESE_DATA %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
				-
        <input title="Anno" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiStatoProcedimento.CAMPO_ANNO_DATA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
       </td>
	 	</tr>

     <tr>
       <td>
       <br> <INPUT  class="bottone" type="submit" name="INSERISCI" value="Conferma">
       </td>
     </tr>

</table>
		</form>
 <script language="JavaScript" type="text/javascript">
 var frmvalidator  = new Validator("LoadInserisciStatoProcedimento");
  frmvalidator.addValidation("<%= ICostantiStatoProcedimento.CAMPO_GIORNO_DATA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiStatoProcedimento.CAMPO_GIORNO_DATA%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiStatoProcedimento.CAMPO_MESE_DATA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiStatoProcedimento.CAMPO_MESE_DATA%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiStatoProcedimento.CAMPO_ANNO_DATA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiStatoProcedimento.CAMPO_ANNO_DATA%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiStatoProcedimento.CAMPO_ANNO_DATA%>","lt=2050");


   frmvalidator.setAddnlValidationFunction("Verifica");

 </script>
	</body>
</html>