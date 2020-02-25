<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Ricerca Procedimenti per Magistrato </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      function Verify()
      {
        if(document.LoadRicercaProcedimentiMagistratoCompetente.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
        {
          alert("Il Cognome del Magistrato è obbligatorio");
          return false;
        }

        if(document.LoadRicercaProcedimentiMagistratoCompetente.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Nome del Magistrato è obbligatorio");
          return false;
        }

        return true;
      }

      function ListaMagistrati(a_formname)
      {
        var desktop;
        //desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratiUfficio&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
  </script>
</head>
  
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Elenco Procedimenti Assegnati</font>
      </td>
    </tr>
  </table>
  
  
  <FORM method="POST" name="LoadRicercaProcedimentiMagistratoCompetente" action="<%= IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.magistratocompetente.action.ActRicercaProcedimentiAssegnati">
    <input type="HIDDEN" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value=<%=AzioneChiamante%>>
    <input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>" value="" >
    
    <table>
      <tr>
        <td class="Titolo" colspan=6> Magistrato Assegnatario </td>
      </tr>
      <tr>
        <td class="l">Magistrato</td>
        <td class="L">
          <input title="Cognome Magistrato" readonly value="" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>"   maxlength="35" size="25" >
          <input title= "Nome Magistrato"   readonly value="" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>"      maxlength="35" size="25">
          <a href="Javascript:ListaMagistrati('LoadRicercaProcedimentiMagistratoCompetente');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="lNoBord" colspan="2">
          <INPUT class="bottone" type="submit" name="I" value="Conferma">
        </td>
      </tr>
    </table>
  </form>
  
  
<script language="JavaScript" type="text/javascript">
  var frmvalidator = new Validator("LoadRicercaProcedimentiMagistratoCompetente");

  frmvalidator.addValidation("<%=ICostantiMagistrato.CAMPO_COGNOME %>","req","Il Cognome del Magistrato è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiMagistrato.CAMPO_COGNOME %>","req","Il Nome del Magistrato è obbligatorio");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>