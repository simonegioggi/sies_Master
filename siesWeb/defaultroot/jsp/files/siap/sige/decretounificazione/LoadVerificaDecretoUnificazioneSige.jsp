<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sige.decretounificazione.action.ICostantiDecretoUnificazioneSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<jsp:useBean id="elencoUfficiAccorpati" scope="request" class="java.util.Vector" />

<html>
<head>
  <title>[S.I.E.S.] - Inserimento Decreto Unificazione Sige</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

  <script language="JavaScript">
  function Verify()
  {
    // Controllo di uguaglianza tra i 2 procedimenti.
    var anno_da_unif=document.LoadVerificaDecretoUnificazioneSige.<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF%>.value;
    var anno_unificante=document.LoadVerificaDecretoUnificazioneSige.<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_UNIFICANTE%>.value;
    var numero_da_unif=document.LoadVerificaDecretoUnificazioneSige.<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF_ORIGIN%>.value;
    var numero_unificante=document.LoadVerificaDecretoUnificazioneSige.<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_UNIFICANTE_ORIGIN%>.value;

    // Controllo uguaglianza procedimenti "Da Unificare" e "Unificante".
    if ( anno_da_unif == anno_unificante && numero_da_unif == numero_unificante)
    {
      alert('Impossibile Unificare un procedimento con se stesso!');
      return false;
    }
    checkNewProg();
    return true;
  }
  </script>

</head>

<body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Inserimento Decreto Unificazione Sige</font>
        </td>
      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadVerificaDecretoUnificazioneSige'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.decretounificazione.action.ActLoadInserisciDecretoUnificazioneSige">
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">Anno e Numero del Procedimento Sige da Unificare <font class=ob>(*)</font></td>

        <td class="l">
          <input Title="AnnoDaUnif" type="text" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF %>" maxlength="4" size="4">
          /<input Title="NumeroDaUnif" type="text" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF_ORIGIN %>" maxlength="6" size="6">
          <input type="hidden" name="<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF%>" value="">
        </td>
      </tr>
      <tr>
        <td class="l">Anno e Numero del Procedimento Sige Unificante <font class=ob>(*)</font></td>

        <td class="l">
          <input Title="AnnoUnificante" type="text" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_ANNO_UNIFICANTE %>" maxlength="4" size="4">
          /<input Title="NumeroUnificante" type="text" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_UNIFICANTE_ORIGIN %>" maxlength="6" size="6">
          <input type="hidden" name="<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_UNIFICANTE%>" value="">
        </td>
      </tr>
      <tr>
        <td class="l">Ufficio Accorpato</td>

        <td class="l">
         	<select name="<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ACCORPATO%>">
         	<option value="0" >-</option>
  <%
  Iterator it = elencoUfficiAccorpati.iterator();
  int indice = 0;
  while (it.hasNext())
  {
  	UfficioAccorpatoModel ua = (UfficioAccorpatoModel) it.next();
  %>
         	<option value="<%=ua.getIncrProgressivo()%>" ><%=ua.getDescrizione()%> (<%=ua.transCodingTipoUfficioSige()%>)</option>
  <%
	indice ++;
  }
  %>
         	</select>
        </td>
      </tr>

    </table>
    <BR>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Conferma">
        </td>
      </tr>

    </table>
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadVerificaDecretoUnificazioneSige");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF%>","req","Il campo Anno da Unificare è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF%>","maxlen=4","La lunghezza massima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF%>","minlen=4","La lunghezza minima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF%>","numeric");

    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF_ORIGIN%>","req","Il campo Numero da Unificare è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF_ORIGIN%>","maxlen=6","La lunghezza massima per il Numero è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF_ORIGIN%>","numeric");

    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_UNIFICANTE%>","req","Il campo Anno Unificante è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_UNIFICANTE%>","maxlen=4","La lunghezza massima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_UNIFICANTE%>","minlen=4","La lunghezza minima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_UNIFICANTE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_UNIFICANTE_ORIGIN%>","req","Il campo Numero Unificante è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_UNIFICANTE_ORIGIN%>","maxlen=6","La lunghezza massima per il Numero è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_UNIFICANTE_ORIGIN%>","numeric");

    function checkNewProg(){
        var offSet = document.getElementById("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ACCORPATO%>").value;

        var numProgA = document.getElementById("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF_ORIGIN%>").value;
        var numProgB = document.getElementById("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_UNIFICANTE_ORIGIN%>").value;
        var newProgA = parseInt(numProgA) + parseInt(offSet);
        var newProgB = parseInt(numProgB) + parseInt(offSet);
        
        document.getElementById("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF%>").value = newProgA;
        document.getElementById("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_UNIFICANTE%>").value = newProgB;
        return true;
    }
  </script>
  </body>
</html>