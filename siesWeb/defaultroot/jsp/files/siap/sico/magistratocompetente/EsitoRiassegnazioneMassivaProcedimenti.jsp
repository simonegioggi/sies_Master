<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.magistratocompetente.action.ICostantiMagistratoCompetente"%>
<%@ page import="siap.sico.magistratocompetente.action.RiassegnazioneMassivaUtil"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="AzioneChiamante"             scope="request" class="java.lang.String"/>
<jsp:useBean id="AmbitoRiassegnazione"        scope="request" class="java.lang.String"/>
<jsp:useBean id="CodMagistratoOrigine"        scope="request" class="java.lang.String"/>
<jsp:useBean id="CognomeMagistratoOrigine"    scope="request" class="java.lang.String"/>
<jsp:useBean id="NomeMagistratoOrigine"       scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoCriterioRiassegnazione"  scope="request" class="java.lang.String"/>
<jsp:useBean id="ValoreCriterioRiassegnazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="totalePendenti"              scope="request" class="java.lang.String"/>
<jsp:useBean id="totaleRiassegnabili"         scope="request" class="java.lang.String"/>

<%
int lRiassegnabili = new Integer(totaleRiassegnabili).intValue();
boolean lPerMagistrato = ICostantiMagistratoCompetente.AMBITO_RIASSEGNAZIONE_MAGISTRATO.equals(AmbitoRiassegnazione);
%>
<html>
<head>
<title>[S.I.E.S.] - Riassegnazione massiva procedimenti</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
var elaborazioneInCorso = false;

function aggiornaEsempio() {
  var tipo = document.RiassegnazioneMassivaProcedimenti.<%=ICostantiMagistratoCompetente.CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE%>.value;
  document.getElementById("esempioCriterio").innerHTML = (tipo == "PROGRESSIVO")
    ? "Una o pi&ugrave; cifre da 0 a 9, finali del numero progressivo (es. 1,3,5)"
    : "Una o pi&ugrave; lettere iniziali del cognome del condannato (es. A,C,E)";
}

function criterioValido() {
  var frm = document.RiassegnazioneMassivaProcedimenti;
  var valore = frm.<%=ICostantiMagistratoCompetente.CAMPO_VALORE_CRITERIO_RIASSEGNAZIONE%>.value;
  if (valore.replace(/\s/g, "") == "") {
    alert("Indicare il valore del criterio di selezione");
    return false;
  }
  var tipo = frm.<%=ICostantiMagistratoCompetente.CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE%>.value;
  var re = (tipo == "PROGRESSIVO") ? /^[0-9,;\s]+$/ : /^[A-Za-z,;\s]+$/;
  if (!re.test(valore)) {
    alert(tipo == "PROGRESSIVO" ? "Indicare solo cifre da 0 a 9" : "Indicare solo lettere da A a Z");
    return false;
  }
  return true;
}

function RifaiStima() {
  var frm = document.RiassegnazioneMassivaProcedimenti;
  if (!criterioValido()) return;
  frm.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sico.magistratocompetente.action.ActRicercaRiassegnazioneMassivaProcedimenti";
  frm.submit();
}

function Conferma() {
  var frm = document.RiassegnazioneMassivaProcedimenti;
  if (elaborazioneInCorso) return;
  if (!criterioValido()) return;
  if (frm.<%=ICostantiMagistratoCompetente.CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE%>.value != frm.TipoStimato.value
      || frm.<%=ICostantiMagistratoCompetente.CAMPO_VALORE_CRITERIO_RIASSEGNAZIONE%>.value.replace(/\s/g, "") != frm.ValoreStimato.value.replace(/\s/g, "")) {
    alert("Il criterio e' stato modificato: effettuare nuovamente la stima prima di confermare");
    return;
  }
  if (frm.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value == "" || frm.<%=ICostantiMagistrato.CAMPO_NOME%>.value == "") {
    alert("Selezionare il Magistrato assegnatario");
    return;
  }
  if (frm.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value == frm.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>_OLD.value) {
    alert("Il nuovo Magistrato coincide con quello di partenza");
    return;
  }
  if (frm.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value.length == 1)
    frm.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value = '0' + frm.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value;
  if (frm.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value.length == 1)
    frm.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value = '0' + frm.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value;
  var data_to_verify = frm.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value
                 +'/'+ frm.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value
                 +'/'+ frm.<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO%>.value;
  if (!ControllaDataPassaVuota(data_to_verify)) {
    alert('Data di inizio competenza non valida');
    return;
  }
  if (!confirm('Verranno riassegnati fino a <%=totaleRiassegnabili%> procedimenti. Confermare?')) return;
  elaborazioneInCorso = true;
  document.getElementById("avvisoElaborazione").style.display = "";
  frm.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sico.magistratocompetente.action.ActEseguiRiassegnazioneMassivaProcedimenti";
  frm.submit();
  // la risposta e' il file Excel scaricato: al termine si ripristina la pagina
  setTimeout(function() {
    document.getElementById("avvisoElaborazione").innerHTML = "Elaborazione terminata: verificare il download del file Excel con i procedimenti riassegnati.";
  }, 15000);
}

function ListaMagistrati(a_formname) {
  // codnum=xxx evita che la pop up precarichi il campo Data Inizio Competenza
  window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname+"&codnum=xxx", "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}
</script>
</head>

<body class="corpo" onload="aggiornaEsempio();">
<table>
  <tr>
    <td class="LBG">
      <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a>
    </td>
    <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Riassegnazione massiva procedimenti</font>
    </td>
    <td class="LBG">
      <a href="javascript:history.back()"><img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
    </td>
  </tr>
</table>

<FORM method="POST" name="RiassegnazioneMassivaProcedimenti" action="<%= IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
<input type="HIDDEN" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value="<%=AzioneChiamante%>">
<input type="HIDDEN" name="<%=ICostantiMagistratoCompetente.CAMPO_AMBITO_RIASSEGNAZIONE%>" value="<%=StringUtils.toStringJSP(AmbitoRiassegnazione)%>">
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" value="">
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>_OLD" value="<%=StringUtils.toStringJSP(CodMagistratoOrigine)%>">
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_NOME%>_OLD" value="<%=StringUtils.toStringJSP(NomeMagistratoOrigine)%>">
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>_OLD" value="<%=StringUtils.toStringJSP(CognomeMagistratoOrigine)%>">
<input type="HIDDEN" name="TipoStimato" value="<%=StringUtils.toStringJSP(TipoCriterioRiassegnazione)%>">
<input type="HIDDEN" name="ValoreStimato" value="<%=StringUtils.toStringJSP(ValoreCriterioRiassegnazione)%>">

<table>
  <tr>
    <td class="Titolo" colspan="2"> Stima procedimenti </td>
  </tr>
  <tr>
    <td class="l">Ambito</td>
    <td class="L"><%= lPerMagistrato
        ? "Magistrato " + StringUtils.toStringJSP(CognomeMagistratoOrigine) + "&nbsp;" + StringUtils.toStringJSP(NomeMagistratoOrigine)
        : "Intero ufficio" %></td>
  </tr>
  <tr>
    <td class="l">Totale procedimenti pendenti</td>
    <td class="L"><b><%=totalePendenti%></b></td>
  </tr>
  <tr>
    <td class="l">Totale procedimenti riassegnabili con il criterio</td>
    <td class="L"><b><%=totaleRiassegnabili%></b></td>
  </tr>
</table>
<br>
<table>
  <tr>
    <td class="Titolo" colspan="2"> Criterio di selezione </td>
  </tr>
  <tr>
    <td class="l">Tipo</td>
    <td class="L">
      <select name="<%=ICostantiMagistratoCompetente.CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE%>" onchange="aggiornaEsempio();">
        <option value="COGNOME"<%= RiassegnazioneMassivaUtil.TIPO_COGNOME.equals(TipoCriterioRiassegnazione) ? " selected" : "" %>>Lettera iniziale del cognome del condannato</option>
        <option value="PROGRESSIVO"<%= RiassegnazioneMassivaUtil.TIPO_PROGRESSIVO.equals(TipoCriterioRiassegnazione) ? " selected" : "" %>>Cifra finale del progressivo</option>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Valore</td>
    <td class="L">
      <input type="text" name="<%=ICostantiMagistratoCompetente.CAMPO_VALORE_CRITERIO_RIASSEGNAZIONE%>" maxlength="60" size="30" value="<%=StringUtils.toStringJSP(ValoreCriterioRiassegnazione)%>">
      <br><font class="label" id="esempioCriterio"></font>
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td class="lNoBord" colspan="2">
      <INPUT class="bottone" type="button" value="Effettua Stima" onclick="RifaiStima();">
    </td>
  </tr>
</table>
<br>
<%
if (lRiassegnabili > 0) {
%>
<table>
  <tr>
    <td class="Titolo" colspan="2"> Nuovo Magistrato Assegnatario </td>
  </tr>
  <tr>
    <td class="l">Magistrato</td>
    <td class="L">
      <input title="Cognome Magistrato" readonly value="" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25">
      <input title="Nome Magistrato" readonly value="" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('RiassegnazioneMassivaProcedimenti');"><img src="/images/filefolder.gif" border="0"></a>
    </td>
  </tr>
  <tr>
    <td class="l">Data Inizio Competenza</td>
    <td class="L">
      <input value="<%=DateUtils.getDateToString(DateUtils.getSysDate(), "dd")%>"   type="text" size="2" maxlength="2" name="<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getDateToString(DateUtils.getSysDate(), "MM")%>"   type="text" size="2" maxlength="2" name="<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getDateToString(DateUtils.getSysDate(), "yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td class="lNoBord" colspan="2">
      <INPUT class="bottone" type="button" value="Conferma riassegnazione" onclick="Conferma();">
    </td>
  </tr>
</table>
<div id="avvisoElaborazione" style="display:none"><font class="label"><b>Attivit&agrave; di riassegnazione in corso, attendere il completamento: al termine verr&agrave; fornito il file Excel con i procedimenti riassegnati.</b></font></div>
<%
} else {
%>
<table cellspacing="2" cellpadding="2" align="center" width="60%">
  <tr>
    <td class="c">
      <font class="label">Nessun procedimento risponde al criterio indicato</font>
    </td>
  </tr>
</table>
<%
}
%>
</form>
</body>
</html>
