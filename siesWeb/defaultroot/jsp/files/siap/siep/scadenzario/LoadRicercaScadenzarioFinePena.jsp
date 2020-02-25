<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel"%>
<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario"%>

<html>
<head>
  <title> [S.I.E.S.] - Scadenzario Simeone Fine Pena - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  <script language="JavaScript">
  function verifica()
  {
      if(document.LoadRicercaScadenzarioFinePena.tipo[1].checked)
      {

        if(document.LoadRicercaScadenzarioFinePena.<%= ICostantiScadenzario.CAMPO_ANNI_SCADENZA %>.value == "" && document.LoadRicercaScadenzarioFinePena.<%= ICostantiScadenzario.CAMPO_GIORNI_SCADENZA %>.value == "" && document.LoadRicercaScadenzarioFinePena.<%= ICostantiScadenzario.CAMPO_MESI_SCADENZA %>.value == "")
         {
         alert("Inserire Periodo Istanze in Scadenza ");
         return false;
         }
      }
    if(!(document.LoadRicercaScadenzarioFinePena.tipo[0].checked || document.LoadRicercaScadenzarioFinePena.tipo[1].checked || document.LoadRicercaScadenzarioFinePena.tipo[2].checked|| document.LoadRicercaScadenzarioFinePena.tipo[3].checked) )
    {
      alert("E' obbligatorio selezionare almeno un elemento");
      return false;
    }
    return true;
   }

  </script>
 </head>
<%
     ScadenzarioModel lModel = new ScadenzarioModel();

%>
<body class="corpo">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaScadenzarioFinePena">

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActRicercaScadenzarioFinePena">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Consultazione Scadenzario Fine Pena</font></td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2 width="100%">
      <tr>
        <td colspan="4" class="Titolo" width="100%">Consultazione Scadenzario Fine Pena</td>
      </tr>
      <tr>
       <td class="l" width="15%">Tutti</td>
       <td class="l" width="15%"><input type="radio" name="tipo" value="Tutti" ></td>
        <td class="l" width="70%" colspan=2></td>
      </tr>
        <tr>
        <td class="l">In scadenza</td>
        <td class="l"><input type="radio" name="tipo" value="sette" ></td>
        <td class="L" width="10%">entro:
                 Anni
                  <input title="Anni" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumAnni()) %>" type="text" name="<%= ICostantiScadenzario.CAMPO_ANNI_SCADENZA %>"  >
                  Mesi
                  <input title="Mesi" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumMesi()) %>" type="text" name="<%= ICostantiScadenzario.CAMPO_MESI_SCADENZA%>"  >
                  Giorni
                  <input title="Giorni" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getNumGiorni()) %>" type="text" name="<%= ICostantiScadenzario.CAMPO_GIORNI_SCADENZA %>"  >
              </td>
      </tr>
        <tr>
         <td class="l">In scadenza Oggi</td>
         <td class="l"><input type="radio" name="tipo" value="oggi" ></td>
      </tr>
      <tr>
         <td class="l">Scaduti</td>
         <td class="l"><input type="radio" name="tipo" value="scaduto" ></td>
      </tr>

<br>

    <tr>
          <td colspan="2">
              <INPUT class="bottone" type="submit"   name="RICERCA" value="Ricerca" onClick="">
          </td>
        </tr>
    </table>
 </form>
<script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadRicercaScadenzarioFinePena");
    frmvalidator.addValidation("<%=ICostantiScadenzario.CAMPO_ANNI_SCADENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiScadenzario.CAMPO_MESI_SCADENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>","numeric");
    frmvalidator.setAddnlValidationFunction("verifica");
  </script>

</body>

</html>