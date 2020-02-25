<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.scadenzario.model.ScadenzarioSigeModel"%>
<%@ page import="siap.sige.scadenzario.action.ICostantiScadenzarioSige"%>

<jsp:useBean id="tipoScadenzarioSIGE" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> [S.I.E.S.] - Scadenzario Simeone - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  function verifica()
  {
      if(document.f.tipo[1].checked)
      {
        if(document.f.<%=ICostantiScadenzarioSige.CAMPO_ANNI_SCADENZA%>.value == "" && document.f.<%=ICostantiScadenzarioSige.CAMPO_GIORNI_SCADENZA%>.value == "" && document.f.<%=ICostantiScadenzarioSige.CAMPO_MESI_SCADENZA%>.value == "")
         {
          alert("Inserire Periodo Istanze in Scadenza ");
          return false;
         }
      }
    if(!(document.f.tipo[0].checked || document.f.tipo[1].checked || document.f.tipo[2].checked|| document.f.tipo[3].checked) )
    {
      alert("E' obbligatorio selezionare almeno un criterio");
      return false;
    }
   document.f.submit();
   }
  </script>
 </head>

<body class="corpo">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.scadenzario.action.ActRicercaScadenzarioSige">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Consultazione Scadenzari Sige&nbsp;</font></td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2 width="80%">

      <tr>
        <td class="l" width="20%">Tipo Scadenzario<font class=ob>(*)</font></td>
        <td class="Label">
          <select title="tipoScadenzario" class=small name="<%=ICostantiScadenzarioSige.CAMPO_COD_TIPO_SCADENZARIO%>" >
            <%= tipoScadenzarioSIGE %>
          </select>
        </td>
      </tr>
    </table>

    <table cellspacing=2 cellpadding=2 width="100%">
      <tr>
        <td class="l" width="15%">Tutti</td>
        <td class="l" width="4%"><input type="radio" name="tipo" value="tutti" ></td>
        <td class="l" width="70%" colspan=2></td>
      </tr>
      <tr>
        <td class="l">In scadenza</td>
        <td class="l"><input type="radio" name="tipo" value="intervallo" ></td>
        <td class="L" width="40%">
          entro:  Anni
          <input title="Anni" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzarioSige.CAMPO_ANNI_SCADENZA%>"  >
          Mesi
          <input title="Mesi" size="2" maxlength="2" value="" type="text" name="<%= ICostantiScadenzarioSige.CAMPO_MESI_SCADENZA%>"  >
          Giorni
          <input title="Giorni" size="2" maxlength="2" value="" type="text" name="<%= ICostantiScadenzarioSige.CAMPO_GIORNI_SCADENZA %>"  >
        </td>
        <td class="l" width="30%" colspan=2></td>
      </tr>
        <tr>
         <td class="l">In scadenza Oggi</td>
         <td class="l"><input type="radio" name="tipo" value="oggi" ></td>
      </tr>
      <tr>
         <td class="l">Scaduti</td>
         <td class="l"><input type="radio" name="tipo" value="scaduti" ></td>
      </tr>

<br>

    <tr>
          <td colspan="2">
              <INPUT class="bottone" type="button"   name="RICERCA" value="Ricerca" onClick="javascript:return verifica()">
          </td>
        </tr>
    </table>
    <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");
    frmvalidator.addValidation("<%=ICostantiScadenzarioSige.CAMPO_ANNI_SCADENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiScadenzarioSige.CAMPO_MESI_SCADENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiScadenzarioSige.CAMPO_GIORNI_SCADENZA%>","numeric");

    frmvalidator.setAddnlValidationFunction("verifica");

  </script>

</body>

</html>