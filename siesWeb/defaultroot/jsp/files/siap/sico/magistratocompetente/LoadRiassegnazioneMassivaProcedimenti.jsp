<%-- 20261023-MEV_2026-105_INLAY --%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.magistratocompetente.action.ICostantiMagistratoCompetente"%>

<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Riassegnazione massiva procedimenti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript">
      function ambitoMagistrato()
      {
        var frm = document.LoadRiassegnazioneMassivaProcedimenti;
        return frm.<%=ICostantiMagistratoCompetente.CAMPO_AMBITO_RIASSEGNAZIONE%>[1].checked;
      }

      function aggiornaAmbito()
      {
        document.getElementById("rigaMagistrato").style.display = ambitoMagistrato() ? "" : "none";
        if (!ambitoMagistrato())
        {
          var frm = document.LoadRiassegnazioneMassivaProcedimenti;
          frm.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value = "";
          frm.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value = "";
          frm.<%=ICostantiMagistrato.CAMPO_NOME%>.value = "";
        }
      }

      function aggiornaEsempio()
      {
        var tipo = document.LoadRiassegnazioneMassivaProcedimenti.<%=ICostantiMagistratoCompetente.CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE%>.value;
        document.getElementById("esempioCriterio").innerHTML = (tipo == "PROGRESSIVO")
          ? "Una o pi&ugrave; cifre da 0 a 9, finali del numero progressivo (es. 1,3,5)"
          : "Una o pi&ugrave; lettere iniziali del cognome del condannato (es. A,C,E)";
      }

      function Verify()
      {
        var frm = document.LoadRiassegnazioneMassivaProcedimenti;
        if (ambitoMagistrato() && frm.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value == "")
        {
          alert("Selezionare il Magistrato di cui riassegnare i procedimenti");
          return false;
        }
        var valore = frm.<%=ICostantiMagistratoCompetente.CAMPO_VALORE_CRITERIO_RIASSEGNAZIONE%>.value;
        if (valore.replace(/\s/g, "") == "")
        {
          alert("Indicare il valore del criterio di selezione");
          return false;
        }
        var tipo = frm.<%=ICostantiMagistratoCompetente.CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE%>.value;
        var re = (tipo == "PROGRESSIVO") ? /^[0-9,;\s]+$/ : /^[A-Za-z,;\s]+$/;
        if (!re.test(valore))
        {
          alert(tipo == "PROGRESSIVO" ? "Indicare solo cifre da 0 a 9" : "Indicare solo lettere da A a Z");
          return false;
        }
        return true;
      }

      function ListaMagistrati(a_formname)
      {
        window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratiUfficio&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
    </script>
  </head>

<body class="corpo" onload="aggiornaAmbito(); aggiornaEsempio();">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Riassegnazione massiva procedimenti</font>
      </td>
    </tr>
  </table>

  <FORM method="POST" name="LoadRiassegnazioneMassivaProcedimenti" action="<%= IWebConstants.PG_MAIN%>" onsubmit="return Verify();">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.magistratocompetente.action.ActRicercaRiassegnazioneMassivaProcedimenti">
    <input type="HIDDEN" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value="<%=AzioneChiamante%>">
    <input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" value="">

    <table>
      <tr>
        <td class="Titolo" colspan="2"> Procedimenti da riassegnare </td>
      </tr>
      <tr>
        <td class="l">Ambito</td>
        <td class="L">
          <input type="radio" name="<%=ICostantiMagistratoCompetente.CAMPO_AMBITO_RIASSEGNAZIONE%>" value="<%=ICostantiMagistratoCompetente.AMBITO_RIASSEGNAZIONE_UFFICIO%>" checked onclick="aggiornaAmbito();"> Intero ufficio
          &nbsp;&nbsp;
          <input type="radio" name="<%=ICostantiMagistratoCompetente.CAMPO_AMBITO_RIASSEGNAZIONE%>" value="<%=ICostantiMagistratoCompetente.AMBITO_RIASSEGNAZIONE_MAGISTRATO%>" onclick="aggiornaAmbito();"> Singolo Magistrato
        </td>
      </tr>
      <tr id="rigaMagistrato">
        <td class="l">Magistrato</td>
        <td class="L">
          <input title="Cognome Magistrato" readonly value="" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25">
          <input title="Nome Magistrato" readonly value="" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25">
          <a href="Javascript:ListaMagistrati('LoadRiassegnazioneMassivaProcedimenti');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="Titolo" colspan="2"> Criterio di selezione </td>
      </tr>
      <tr>
        <td class="l">Tipo</td>
        <td class="L">
          <select name="<%=ICostantiMagistratoCompetente.CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE%>" onchange="aggiornaEsempio();">
            <option value="COGNOME">Lettera iniziale del cognome del condannato</option>
            <option value="PROGRESSIVO">Cifra finale del progressivo</option>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Valore</td>
        <td class="L">
          <input type="text" name="<%=ICostantiMagistratoCompetente.CAMPO_VALORE_CRITERIO_RIASSEGNAZIONE%>" maxlength="60" size="30">
          <br><font class="label" id="esempioCriterio"></font>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="lNoBord" colspan="2">
          <INPUT class="bottone" type="submit" name="I" value="Effettua Stima">
        </td>
      </tr>
    </table>
  </form>
</body>
</html>
