<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel"%>
<%@ page import="siap.sius.cancelleriaassegnataria.action.ICostantiCancelleriaAssegnataria"%>


<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<jsp:useBean id="modalita"       scope="request" class="java.lang.String"/>
<jsp:useBean id="cancelleriaassegnataria"       scope="request" class="siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel"/>
<jsp:useBean id="uffici" scope="request" class="java.util.Vector"/>


<html>
<head>
<title>[S.I.A.P.] - GestioneCancelleriaAssegnataria </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript">
function Verify()
{
   return true;
}
</script>

</head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;

 <%
   String lReadOnly = "";
   CancelleriaAssegnatariaModel lCancelleriaAssegnataria = new CancelleriaAssegnatariaModel();


 String lAzione = new String();
   if( modalita.equals("I") )
   {
     lAzione = "siap.sius.cancelleriaassegnataria.action.ActInserisciCancelleriaAssegnataria";

%>
  <font class="campo">Inserimento Cancelleria Assegnataria</font>
<%
   }
   else if( modalita.equals("M") )
   {
      lReadOnly = "readonly";
      lAzione = "siap.sius.cancelleriaassegnataria.action.ActModificaCancelleriaAssegnataria";
      lCancelleriaAssegnataria = cancelleriaassegnataria;
%>
   <font class="campo">Modifica Cancelleria Assegnataria</font>
<%}
   else if( modalita.equals("R") )
   {
      lAzione = "siap.sius.cancelleriaassegnataria.action.ActRicercaCancelleriaAssegnataria";
      lCancelleriaAssegnataria = cancelleriaassegnataria;
%>
   <font class="campo">Ricerca Cancelleria Assegnataria</font>
<%}%>


    </td>
    </tr>
   </table>
   <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciCancelleriaAssegnataria">
  <input type="hidden" value="<%=lAzione%>" name="<%=IWebConstants.ACTION_FIELD%>">

  <table cellspacing=4 cellpadding=4>
     <tr>
      <td class="int">Codice Cancelleria Assegnataria</td>
      <td class="l"><input value="<%=lCancelleriaAssegnataria.getCodCancelleriaAssegnataria() %>" type="text" name="<%=ICostantiCancelleriaAssegnataria.CAMPO_COD_CANCELLERIA_ASSEGNATARIA%>" size= 3; maxlength=3;  <%=lReadOnly%> ></td>
     </tr>
 <tr>
  <td class="int">Descrizione Cancelleria Assegnataria</td>
  <td class="l"><input value="<%=lCancelleriaAssegnataria.getDescCancelleriaAssegnataria() %>" type="text" name="<%= ICostantiCancelleriaAssegnataria.CAMPO_DESC_CANCELLERIA_ASSEGNATARIA %>"  size= 40; maxlength=100; ></td>
 </tr>

 <tr>
 <td class="int">Ufficio</td>
 <td class="l">
  <select name="<%=ICostantiCancelleriaAssegnataria.CAMPO_COD_UFFICIO %>"  class="small">
  <%
    String cod;
    String desc;
   if( modalita.equals("M"))
   {
       cod=lCancelleriaAssegnataria.getCodUfficio();
       desc=lCancelleriaAssegnataria.getDescrUfficio();
 %>
 <option value="<%=cod%>"><%=desc%></option>
 <%
   }
   else
   {
     for (int i=0;i<uffici.size();i++)
     {
       cod=((UfficioModel)uffici.get(i)).getCodUfficio();
       desc=((UfficioModel)uffici.get(i)).getDescrTipoUfficio()+ " di " +((UfficioModel)uffici.get(i)).getDescrComune()+ " ("+((UfficioModel)uffici.get(i)).getCodProvincia() + ")";
 %>
 <option value="<%=cod%>"><%=desc%></option>
 <%
 } // end For
} // endIF
 %>
  </select>
 </td>
 </tr>
</table>
<BR>
<input type="submit" class=bottone name="go" value="Conferma">
</form>
 <script language="JavaScript" type="text/javascript" >
var frmvalidator  = new Validator("LoadInserisciCancelleriaAssegnataria");
// frmvalidator.addValidation();
  frmvalidator.setAddnlValidationFunction("Verify");
<%
   if( modalita.equals("I") || modalita.equals("M"))
   {
%>
  frmvalidator.addValidation("<%=ICostantiCancelleriaAssegnataria.CAMPO_COD_CANCELLERIA_ASSEGNATARIA%>","req","Il campo Codice è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiCancelleriaAssegnataria.CAMPO_DESC_CANCELLERIA_ASSEGNATARIA%>","req","Il campo Descrizione è obbligatorio");
<% } %>
 </script>
	</body>
</html>