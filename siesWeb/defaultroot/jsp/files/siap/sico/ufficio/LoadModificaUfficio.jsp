<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="uffici" scope="request" class="java.util.Vector"/>

<html>
<head>
<title>[S.I.E.S.] - Modifica Ufficio </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%> ></script>
<script language="JavaScript">
function Verify()
{
   return true;
}
</script>

<script language="JavaScript">
// Variabili contenenti i dati di tutti gli uffici
var codUfficio =new Array(<%=uffici.size()%>);
var indirizzo =new Array(<%=uffici.size()%>);
var cap =new Array(<%=uffici.size()%>);
var tel =new Array(<%=uffici.size()%>);
var fax =new Array(<%=uffici.size()%>);
var EMail =new Array(<%=uffici.size()%>);

<%
    for (int i=0;i<uffici.size();i++)
    {
%>
        codUfficio[<%=i%>] = "<%=StringUtils.toStringJSP(( (UfficioModel)uffici.get(i)).getCodUfficio() , " ")%>";
        indirizzo[<%=i%>] = "<%=StringUtils.toStringJSP(((UfficioModel)uffici.get(i)).getIndirizzo(), " ")%>";
        cap[<%=i%>] = "<%=StringUtils.toStringJSP(((UfficioModel)(uffici.get(i))).getCap(), " ")%>";
        tel[<%=i%>] = "<%=StringUtils.toStringJSP(((UfficioModel)uffici.get(i)).getTelefono(), " ")%>";
        fax[<%=i%>] = "<%=StringUtils.toStringJSP(((UfficioModel)uffici.get(i)).getFax(), " ")%>";
        EMail[<%=i%>] = "<%=StringUtils.toStringJSP(((UfficioModel)uffici.get(i)).getEMail()," ")%>";
<%
    }
%>
</script>

<script language="JavaScript">
// La funzione attiva l'aggiornamento dei campi contenenti i dati dell'ufficio selezionato
 function cambia()
 {
    //alert("cambia");
    var cod;

   for (var ii = 0; ii < <%=uffici.size()%>; ii++)
   {
     cod = document.f.<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>.value;
     if (cod == codUfficio[ii])
     {
      aggiorna (ii);
       break;
     }
   }
   return;
 }

</script>
<script language="JavaScript">

function aggiorna(ind)
{
   //alert("aggiorna ->" + ind);
   document.f.<%=ICostantiUfficio.CAMPO_INDIRIZZO%>.value = indirizzo[ind];
   document.f.<%=ICostantiUfficio.CAMPO_CAP%>.value = cap[ind];
   document.f.<%=ICostantiUfficio.CAMPO_TELEFONO%>.value = tel[ind];
   document.f.<%=ICostantiUfficio.CAMPO_FAX%>.value = fax[ind];
   document.f.<%=ICostantiUfficio.CAMPO_E_MAIL%>.value = EMail[ind];
}
</script>

</head>


<body class="corpo" onload="JavaScript:cambia();">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Aggiornamento dati dell'Ufficio</font>
      </td>
   </tr>
 </table>

<FORM method=post action="<%= IWebConstants.PG_MAIN %>" name = f >
<input type="hidden" value="siap.sico.ufficio.action.ActModificaUfficio" name="<%=IWebConstants.ACTION_FIELD%>">

 <table cellspacing=2 cellpadding=2>

 <tr>
 <td class="int">Ufficio</td>
 <td class="l">
  <select name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>" class="small" onchange="JavaScript:cambia();">
  <%
    String cod;
    String desc;
    for (int i=0;i<uffici.size();i++)
    {
       cod=((UfficioModel)uffici.get(i)).getCodUfficio();
       desc=cod + " - " +((UfficioModel)uffici.get(i)).getDescrTipoUfficio()+ " di " +((UfficioModel)uffici.get(i)).getDescrComune()+ " ("+((UfficioModel)uffici.get(i)).getCodProvincia() + ")" /*+ "-" +StringUtils.toStringJSP(((UfficioModel)uffici.get(i)).getIndirizzo() )*/;
 %>
 <option value="<%=cod%>"><%=desc%></option>
<%} %>
  </select>
 </td>
 </tr>

    <tr>
      <td class="int">Indirizzo</td>
      <td class="l"><input name="<%=ICostantiUfficio.CAMPO_INDIRIZZO%>" value="" maxlength=200 size=150 type="text"></td>
    </tr>
    <tr>
      <td class="int">CAP</td>
      <td class="l"><input maxlength=5 size=8 type="text" name="<%=ICostantiUfficio.CAMPO_CAP%>" value=""></td>
    </tr>
    <tr>
      <td class="int">Telefono</td><td class="l"><input maxlength=250 size=150 type="text" name="<%=ICostantiUfficio.CAMPO_TELEFONO%>" value=""></td>
    </tr>
    <tr>
      <td class="int">Fax</td><td class="l"><input maxlength=250 size=150 type="text" name="<%=ICostantiUfficio.CAMPO_FAX%>" value=""></td>
    </tr>
    <tr>
      <td class="int">E-mail</td><td class="l"><input maxlength=250 size=150 type="text" name="<%=ICostantiUfficio.CAMPO_E_MAIL%>" value=""></td>
    </tr>
    <tr>
      <td class="l" colspan=2><input type="submit" class=bottone name="go" value="Conferma"></td>
    </tr>
  </table>
</FORM>

 <script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("f");

  frmvalidator.setAddnlValidationFunction("Verify");
  frmvalidator.addValidation("<%=ICostantiUfficio.CAMPO_INDIRIZZO%>","req","Il campo Indirizzo è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiUfficio.CAMPO_CAP%>","numeric","Il CAP è solo numerico");

  </script>
  </body>
</html>