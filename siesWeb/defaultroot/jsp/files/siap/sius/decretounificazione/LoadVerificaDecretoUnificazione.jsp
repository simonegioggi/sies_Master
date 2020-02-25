<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.decretounificazione.action.ICostantiDecretoUnificazione"%>

<html>
<head>
  <title>[S.I.E.S.] - Inserimento Decreto Unificazione</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

  <script language="JavaScript">
  function Verify()
  {
    // Controllo di uguaglianza tra i 2 procedimenti.
    var anno_da_unif=document.LoadVerificaDecretoUnificazione.<%=ICostantiDecretoUnificazione.CAMPO_ANNO_DA_UNIF%>.value;
    var anno_unificante=document.LoadVerificaDecretoUnificazione.<%=ICostantiDecretoUnificazione.CAMPO_ANNO_UNIFICANTE%>.value;
    var numero_da_unif=document.LoadVerificaDecretoUnificazione.<%=ICostantiDecretoUnificazione.CAMPO_NUMERO_DA_UNIF%>.value;
    var numero_unificante=document.LoadVerificaDecretoUnificazione.<%=ICostantiDecretoUnificazione.CAMPO_NUMERO_UNIFICANTE%>.value;

    // Controllo uguaglianza procedimenti "Da Unificare" e "Unificante".
    if ( anno_da_unif == anno_unificante && numero_da_unif == numero_unificante)
    {
      alert('Impossibile Unificare un procedimento con se stesso!');
      return false;
    }
    return true;
  }
  </script>

</head>

<body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Inserimento Decreto Unificazione</font>
        </td>
      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadVerificaDecretoUnificazione'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.decretounificazione.action.ActLoadInserisciDecretoUnificazione">
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">Anno e Numero del Procedimento da Unificare <font class=ob>(*)</font></td>

        <td class="l">
          <input Title="AnnoDaUnif" type="text" name="<%= ICostantiDecretoUnificazione.CAMPO_ANNO_DA_UNIF %>" maxlength="4" size="4">
          /<input Title="NumeroDaUnif" type="text" name="<%= ICostantiDecretoUnificazione.CAMPO_NUMERO_DA_UNIF %>" maxlength="6" size="6">
        </td>
      </tr>
      <tr>
        <td class="l">Anno e Numero del Procedimento Unificante <font class=ob>(*)</font></td>

        <td class="l">
          <input Title="AnnoUnificante" type="text" name="<%= ICostantiDecretoUnificazione.CAMPO_ANNO_UNIFICANTE %>" maxlength="4" size="4">
          /<input Title="NumeroUnificante" type="text" name="<%= ICostantiDecretoUnificazione.CAMPO_NUMERO_UNIFICANTE %>" maxlength="6" size="6">
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
    var frmvalidator  = new Validator("LoadVerificaDecretoUnificazione");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_ANNO_DA_UNIF%>","req","Il campo Anno da Unificare è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_ANNO_DA_UNIF%>","maxlen=4","La lunghezza massima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_ANNO_DA_UNIF%>","minlen=4","La lunghezza minima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_ANNO_DA_UNIF%>","numeric");

    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_NUMERO_DA_UNIF%>","req","Il campo Numero da Unificare è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_NUMERO_DA_UNIF%>","maxlen=6","La lunghezza massima per il Numero è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_NUMERO_DA_UNIF%>","numeric");

    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_ANNO_UNIFICANTE%>","req","Il campo Anno Unificante è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_ANNO_UNIFICANTE%>","maxlen=4","La lunghezza massima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_ANNO_UNIFICANTE%>","minlen=4","La lunghezza minima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_ANNO_UNIFICANTE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_NUMERO_UNIFICANTE%>","req","Il campo Numero Unificante è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_NUMERO_UNIFICANTE%>","maxlen=6","La lunghezza massima per il Numero è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazione.CAMPO_NUMERO_UNIFICANTE%>","numeric");
  </script>
  </body>
</html>