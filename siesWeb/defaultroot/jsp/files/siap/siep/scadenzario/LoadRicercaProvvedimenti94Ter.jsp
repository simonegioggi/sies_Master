<%-- 20261028-MEV_2026-105_INLAY - MEV Legge 140/2026: ricerca provvedimenti emessi ex art. 94 ter DPR 309/90 --%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Provvedimenti emessi ex art. 94 Ter DPR 309/90</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    function selezionaTipologiaNoTutti() {
      if (document.f.tipoNotifica[1].checked || document.f.tipoNotifica[2].checked || document.f.tipoNotifica[3].checked) {
        document.f.tipoNotifica[0].checked = false;
      }
      return true;
    }
    function selezionaTipologiaTutti() {
      if (document.f.tipoNotifica[0].checked) {
        document.f.tipoNotifica[1].checked = false;
        document.f.tipoNotifica[2].checked = false;
        document.f.tipoNotifica[3].checked = false;
      }
      return true;
    }
    function pad2(c) {
      if (c.value.length == 1) c.value = '0' + c.value;
    }
    function Verify() {
      var f = document.f;
      var aI = f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>, nI = f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>;
      var aF = f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>, nF = f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>;
      if ((nI.value.length != 0) != (aI.value.length != 0)) {
        alert("Valorizzare Anno e Numero iniziale");
        aI.focus();
        return false;
      }
      if ((nF.value.length != 0) != (aF.value.length != 0)) {
        alert("Valorizzare Anno e Numero finale");
        aF.focus();
        return false;
      }
      if (aI.value.length != 0 && aF.value.length != 0) {
        if (parseInt(aF.value, 10) < parseInt(aI.value, 10)) {
          alert("Anno inizio maggiore Anno fine");
          aI.focus();
          return false;
        }
        if (aF.value == aI.value && parseInt(nF.value, 10) < parseInt(nI.value, 10)) {
          alert("Numero inizio maggiore Numero fine");
          nI.focus();
          return false;
        }
      }
      pad2(f.GiornoEmissioneIniziale); pad2(f.MeseEmissioneIniziale);
      pad2(f.GiornoEmissioneFinale); pad2(f.MeseEmissioneFinale);
      var data_inizio = f.GiornoEmissioneIniziale.value + '/' + f.MeseEmissioneIniziale.value + '/' + f.AnnoEmissioneIniziale.value;
      var data_fine = f.GiornoEmissioneFinale.value + '/' + f.MeseEmissioneFinale.value + '/' + f.AnnoEmissioneFinale.value;
      if (!ControllaDataPassaVuota(data_inizio)) {
        alert('Data Emissione iniziale non valida');
        f.GiornoEmissioneIniziale.focus();
        return false;
      }
      if (!ControllaDataPassaVuota(data_fine)) {
        alert('Data Emissione finale non valida');
        f.GiornoEmissioneFinale.focus();
        return false;
      }
      if (f.GiornoEmissioneIniziale.value.length != 0 && f.MeseEmissioneIniziale.value.length != 0 && f.AnnoEmissioneIniziale.value.length != 0
          && f.GiornoEmissioneFinale.value.length != 0 && f.MeseEmissioneFinale.value.length != 0 && f.AnnoEmissioneFinale.value.length != 0) {
        if (!CompareDate(data_inizio, data_fine)) {
          alert('La Data Emissione finale non pu\u00f2 essere inferiore alla Data Emissione iniziale');
          f.GiornoEmissioneIniziale.focus();
          return false;
        }
      }
      if (!f.tipoNotifica[0].checked && !f.tipoNotifica[1].checked && !f.tipoNotifica[2].checked && !f.tipoNotifica[3].checked) {
        alert('Selezionare almeno una Tipologia Provvedimento');
        f.tipoNotifica[0].focus();
        return false;
      }
      return true;
    }
    </script>
  </head>
  <BODY class="corpo">
  <FORM method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>" onSubmit="return Verify();">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActRicercaProvvedimenti94Ter">
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">RICERCA PROVVEDIMENTI EMESSI EX ART. 94 TER DPR 309/90</font></td>
      </tr>
    </table>
    <br>
    <table width="80%" cellpadding=2 cellspacing=2>
      <tr><td class="Titolo" colspan="4">Intervallo Procedimenti</td></tr>
      <tr>
        <td class="L"><font class="label">Anno/Numero Iniziale</font></td>
        <td class="l">
          <input type="text" title="Anno Procedimento Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
          /
          <input type="text" title="Numero Procedimento Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>" maxlength="14" size="14" onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="L"><font class="label">Anno/Numero Finale</font></td>
        <td class="l">
          <input type="text" title="Anno Procedimento Finale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
          /
          <input type="text" title="Numero Procedimento Finale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>" maxlength="14" size="14" onkeypress="return TicTabNumField(this,event)">
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr><td class="Titolo" colspan="4">Intervallo Date Emissione Provvedimento</td></tr>
      <tr>
        <td class="l"><font class="label">Data Emissione Iniziale</font></td>
        <td class="l">
          <input type="text" title="Giorno Emissione Iniziale" name="GiornoEmissioneIniziale" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input type="text" title="Mese Emissione Iniziale" name="MeseEmissioneIniziale" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input type="text" title="Anno Emissione Iniziale" name="AnnoEmissioneIniziale" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l"><font class="label">Data Emissione Finale</font></td>
        <td class="l">
          <input type="text" title="Giorno Emissione Finale" name="GiornoEmissioneFinale" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input type="text" title="Mese Emissione Finale" name="MeseEmissioneFinale" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input type="text" title="Anno Emissione Finale" name="AnnoEmissioneFinale" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>
    <br>
    <table width="80%" cellspacing=2 cellpadding=2>
      <tr><td class="Titolo" colspan="2">Tipologia Provvedimento</td></tr>
      <tr>
        <td width="15%" class="l">Tutti <input type="checkbox" checked name="tipoNotifica" onClick="selezionaTipologiaTutti();" value="Tutti"></td>
        <td width="50%" class="l">OE con sospensione Libero ex art. 94 Ter DPR 309/90 ----------------------------------------------&gt; <input type="checkbox" name="tipoNotifica" onClick="selezionaTipologiaNoTutti();" value="0760"></td>
      </tr>
      <tr>
        <td width="15%">&nbsp;</td>
        <td width="50%" class="l">OE con sospensione Custodia Cautelare Altra Causa Arresti Domiciliari ex art. 94 Ter DPR 309/90 --&gt; <input type="checkbox" name="tipoNotifica" onClick="selezionaTipologiaNoTutti();" value="0761"></td>
      </tr>
      <tr>
        <td width="15%">&nbsp;</td>
        <td width="50%" class="l">OE con sospensione Custodia Cautelare Questa Causa Arresti Domiciliari ex art. 94 Ter DPR 309/90 --&gt; <input type="checkbox" name="tipoNotifica" onClick="selezionaTipologiaNoTutti();" value="0762"></td>
      </tr>
    </table>
    <br>
    <table width="80%" cellspacing=2 cellpadding=2>
      <tr>
        <td width="30%" class="l">Estrazione anche procedimenti Definiti <input type="checkbox" name="estraiDefiniti" value="S"></td>
      </tr>
    </table>
    <br>
    <input type="submit" class="pulsante" value="Ricerca">
  </FORM>
  </body>
</html>
