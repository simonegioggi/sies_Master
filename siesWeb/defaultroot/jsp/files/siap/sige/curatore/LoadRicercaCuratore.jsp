<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.curatore.model.CuratoreModel"%>
<%@ page import="siap.sige.curatore.action.ICostantiCuratore"%>


<html>
  <head>
    <title>[S.I.E.S.] - Ricerca Curatore </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>> </script>
    <script language="JavaScript">

      function init()
      {
      	document.LoadRicercaCuratore.<%=ICostantiCuratore.CAMPO_COGNOME%>.focus();
      }

      function  Verify()
      {
        return true;
      }

    </script>

  </head>

  <body class="corpo" onload="Javascript:init();">
    <table>
    <tr>
			<td class="LBG">
				<a href="Javascript:window.print();">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
				</a>
			</td>
    	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    		<font class="campo">Ricerca Curatore</font>
    	</td>
    </tr>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaCuratore">
      <table cellspacing=4 cellpadding=4>
        <tr>
          <td class="l">Cognome</td>
          <td class="l"><input type="text" name="<%= ICostantiCuratore.CAMPO_COGNOME %>"  ></td>
        </tr>
        <tr>
          <td class="l">Nome</td>
          <td class="l"><input type="text" name="<%= ICostantiCuratore.CAMPO_NOME %>"  ></td>
        </tr>
        <tr>
          <td>
            <input class="bottone" type="submit" value="Conferma">
          </td>
        </tr>
      </table>
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.curatore.action.ActRicercaCuratore" >
      </form>
      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadRicercaCuratore");

        frmvalidator.setAddnlValidationFunction("Verify");
      </script>

   </body>
</html>