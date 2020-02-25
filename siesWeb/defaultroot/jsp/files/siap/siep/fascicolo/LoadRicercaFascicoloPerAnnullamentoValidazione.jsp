<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<jsp:useBean id="elencoUfficiAccorpati" scope="request" class="java.util.Vector" />

<html>

<head>
  <title> [S.I.E.S.] - Ricerca Procedimento - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
</head>

<body class="corpo" onLoad="document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>.focus();">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.fascicolo.action.ActSvalidaFascicolo">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;<font class="campo">Annullamento Validazione Procedimento</font></td>
    </tr>
  </table>

  <br>

  <table cellpadding=2 cellspacing=2>
    <tr>
      <td class="L"> Anno/Numero SIEP</td>
      <td class="l">
        <input type="text" title="Anno" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero SIEP" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>" maxlength="14" size="14">
        <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" value="">
      </td>
    </tr>

      <tr>
         <td class="L">Ufficio Accorpato</td>
         <td class="l">
         	<select name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>">
         	<option value="0" >-</option>

  <%
  Iterator it = elencoUfficiAccorpati.iterator();
  int indice = 0;

  while (it.hasNext())
  {
  	UfficioAccorpatoModel ua = (UfficioAccorpatoModel) it.next();
  %>
         	<option value="<%=ua.getIncrProgressivo()%>" ><%=ua.getDescrizione()%></option>
  <%
	indice ++;
  }
  %>

         	</select>
         </td>
      </tr>

    <tr>
      <td colspan="2">
        <br><br>
        <input class="bottone" type="submit" name="CONFERMA" value="CONFERMA" onClick="javascript:return checkNewProg();">
      </td>
    </tr>

  </table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>","req","Il campo Numero Procedimento è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>","maxlen=14","La lunghezza massima per il Numero Procedimento è di 14 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>","numeric");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","req","Il campo Anno Procedimento è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","numeric");

function resetUfficiAccorpati(){
	var ufficioAccorpatoSelect = document.getElementById("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>");
	ufficioAccorpatoSelect.options[0].setAttribute("selected", "selected");
}

function checkNewProg(){
    var numProg = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>.value;
    var offSet = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.value;
    var newProg = parseInt(numProg) + parseInt(offSet);
    document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value = newProg;
    //resetUfficiAccorpati();
    return true;
}
</script>
</body>
</html>