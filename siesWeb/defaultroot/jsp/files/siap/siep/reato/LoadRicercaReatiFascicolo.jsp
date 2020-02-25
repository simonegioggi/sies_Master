<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<jsp:useBean id="formname" scope="request" class="java.lang.String" />

<head>
  <title> [S.I.E.S.] - Ricerca Procedimento - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>


</head>

<body class="corpo" onLoad="document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f' target="listareati">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.reato.action.ActRicercaReatiFascicolo">
  <input type="HIDDEN" name="formname" value="<%=formname%>">
  <br>
  <table cellpadding=2 cellspacing=2 width="80%">
    <tr>
      <td class="L"> Anno/Numero Procedimento
        <input type="text" title="Anno" value="" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero SIEP" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" maxlength="6" size="6" >
      </td>
      <td>
        <input class="bottone" type="submit" name="CONFERMA" value="RICERCA">
      </td>
    </tr>
  </table>
</form>
  <script language="JavaScript" type="text/javascript">
     var frmvalidator  = new Validator("f");

     frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","req","Il campo Numero Procedimento è obbligatorio");
     frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","maxlen=6","La lunghezza massima per il Numero Procedimento è di 6 caratteri");
     frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","numeric");

     frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","req","Il campo Anno Procedimento è obbligatorio");
     frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
     frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
     frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","numeric");
   </script>
</body>
</html>