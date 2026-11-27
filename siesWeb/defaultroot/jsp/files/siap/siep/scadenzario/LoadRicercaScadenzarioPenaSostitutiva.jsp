<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- 20261124-MEV_2026-124_SG --%>
<%@ page import="f3b.web.IWebConstants"%>

<html>
<head>
  <title> [S.I.E.S.] - Scadenzario Pene Sostitutive Scadute - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  <script language="JavaScript">
  function verifica()
  {
    var f = document.LoadRicercaScadenzarioPenaSostitutiva;
    if(!(f.tipo[0].checked || f.tipo[1].checked || f.tipo[2].checked || f.tipo[3].checked))
    {
      alert("E' obbligatorio selezionare almeno un elemento");
      return false;
    }
    if(f.tipo[1].checked && f.AnniScadenza.value == "" && f.MesiScadenza.value == "" && f.GiorniScadenza.value == "")
    {
      alert("Inserire il periodo di scadenza");
      return false;
    }
    return true;
  }
  </script>
</head>
<body class="corpo">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaScadenzarioPenaSostitutiva">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActRicercaScadenzarioPenaSostitutiva">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Consultazione Scadenzario Pene Sostitutive Scadute</font></td>
      </tr>
    </table>
    <br>
    <table cellspacing=2 cellpadding=2 width="100%">
      <tr>
        <td colspan="4" class="Titolo" width="100%">Consultazione Scadenzario Pene Sostitutive Scadute</td>
      </tr>
      <tr>
        <td class="l" width="15%">Tutte</td>
        <td class="l" width="15%"><input type="radio" name="tipo" value="Tutti"></td>
        <td class="l" width="70%" colspan=2></td>
      </tr>
      <tr>
        <td class="l">In scadenza</td>
        <td class="l"><input type="radio" name="tipo" value="sette"></td>
        <td class="L" width="10%">entro:
          Anni <input title="Anni" size=2 maxlength=2 type="text" name="AnniScadenza">
          Mesi <input title="Mesi" size=2 maxlength=2 type="text" name="MesiScadenza">
          Giorni <input title="Giorni" size=2 maxlength=2 type="text" name="GiorniScadenza">
        </td>
      </tr>
      <tr>
        <td class="l">In scadenza Oggi</td>
        <td class="l"><input type="radio" name="tipo" value="oggi"></td>
      </tr>
      <tr>
        <td class="l">Scadute</td>
        <td class="l"><input type="radio" name="tipo" value="scaduto"></td>
      </tr>
      <tr>
        <td colspan="2"><INPUT class="bottone" type="submit" name="RICERCA" value="Ricerca"></td>
      </tr>
    </table>
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadRicercaScadenzarioPenaSostitutiva");
    frmvalidator.addValidation("AnniScadenza","numeric");
    frmvalidator.addValidation("MesiScadenza","numeric");
    frmvalidator.addValidation("GiorniScadenza","numeric");
    frmvalidator.setAddnlValidationFunction("verifica");
  </script>
</body>
</html>
