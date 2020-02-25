<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sige.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sige.magistrato.action.ICostantiMagistrato"%>

<html>
  <head>
    <title>[S.I.E.S.] - RicercaMagistrato </title>

    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"> </script>
    <script language="JavaScript">
      function  Verify()
      {
        return true;
      }
    </script>
  </head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
          <font class="campo">Ricerca di un Magistrato</font>
        </td>
      </tr>
    </table>
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaMagistrato">
      <table cellspacing=2 cellpadding=2>
       <tr>
          <td class="l">Cognome</td>
          <td class="l"><input value="" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME %>"> </td>
        </tr>
        <tr>
          <td class="l">Nome</td>
          <td class="l"><input value="" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"></td>
        </tr>
        <tr>
          <td class="l">Codice CSM</td>
          <td class="l"><input value="" type="text" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"></td>
        </tr>
        <tr>
          <td>
            <input class="bottone" type="submit" value="Conferma">
          </td>
        </tr>
      </table>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.magistrato.action.ActRicercaMagistrato" >
    </form>
      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadRicercaMagistrato");

        frmvalidator.setAddnlValidationFunction("Verify");
      </script>

  </body>
</html>