<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.magistratocompetente.action.ICostantiMagistratoCompetente"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="aMagistrato"        scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="aListaProcedimenti" scope="request" class="java.util.Vector"/>
<jsp:useBean id="AzioneChiamante"    scope="request" class="java.lang.String"/>
<jsp:useBean id="totaleProcedimenti" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoCriterioRiassegnazione"  scope="request" class="java.lang.String"/>
<jsp:useBean id="ValoreCriterioRiassegnazione" scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Stima dei procedimenti da riassegnare in base al criterio scelto
//==============================================================================
int totale = new Integer(totaleProcedimenti).intValue();
int MAX_ANTEPRIMA = 200;
String lDescrCriterio = "PROGRESSIVO".equals(TipoCriterioRiassegnazione)
    ? "Cifre finali del progressivo" : "Iniziali del cognome del soggetto";
%>
<html>
<head>
<title>[S.I.E.S.] - Riassegnazione massiva procedimenti</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
function Verify() {
  var frm = document.RiassegnazioneMassivaProcedimenti;
  if (frm.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value == "" || frm.<%=ICostantiMagistrato.CAMPO_NOME%>.value == "") {
    alert("Selezionare il nuovo Magistrato assegnatario");
    return false;
  }
  if (frm.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value == frm.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>_OLD.value) {
    alert("Il nuovo Magistrato coincide con quello attuale");
    return false;
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
    return false;
  }
  return confirm('Verranno riassegnati <%=totaleProcedimenti%> procedimenti. Confermare?');
}

function ListaMagistrati(a_formname) {
  // codnum=xxx evita che la pop up precarichi il campo Data Inizio Competenza
  window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname+"&codnum=xxx", "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}
</script>
</head>

<body class="corpo">
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

<FORM method="POST" name="RiassegnazioneMassivaProcedimenti" action="<%= IWebConstants.PG_MAIN%>" onsubmit="return Verify();">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.magistratocompetente.action.ActModificaMultiplaMagistratoAssegnatario">
<input type="HIDDEN" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value="<%=AzioneChiamante%>">
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" value="">
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>_OLD" value="<%=StringUtils.toStringJSP(aMagistrato.getCodMagistrato())%>">
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_NOME%>_OLD" value="<%=StringUtils.toStringJSP(aMagistrato.getNome())%>">
<input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>_OLD" value="<%=StringUtils.toStringJSP(aMagistrato.getCognome())%>">
<input type="HIDDEN" name="<%=ICostantiMagistratoCompetente.CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE%>" value="<%=StringUtils.toStringJSP(TipoCriterioRiassegnazione)%>">
<input type="HIDDEN" name="<%=ICostantiMagistratoCompetente.CAMPO_VALORE_CRITERIO_RIASSEGNAZIONE%>" value="<%=StringUtils.toStringJSP(ValoreCriterioRiassegnazione)%>">
<input type="HIDDEN" name="selezionaAll" value="selezionaAll">

<table>
  <tr>
    <td class="Titolo" colspan="2"> Stima procedimenti da riassegnare </td>
  </tr>
  <tr>
    <td class="l">Magistrato attuale</td>
    <td class="L"><%=StringUtils.toStringJSP(aMagistrato.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(aMagistrato.getNome())%></td>
  </tr>
  <tr>
    <td class="l">Criterio</td>
    <td class="L"><%=lDescrCriterio%>: <%=StringUtils.toStringJSP(ValoreCriterioRiassegnazione)%></td>
  </tr>
  <tr>
    <td class="l">Procedimenti pendenti stimati</td>
    <td class="L"><b><%=totaleProcedimenti%></b></td>
  </tr>
</table>
<br>
<%
if (totale > 0) {
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
      <INPUT class="bottone" type="submit" name="I" value="Conferma riassegnazione">
    </td>
  </tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" align="center" width="60%">
  <tr>
    <td class="int" colspan="2"> Anteprima procedimenti<%= (totale > MAX_ANTEPRIMA) ? " (primi " + MAX_ANTEPRIMA + " di " + totale + ")" : "" %></td>
  </tr>
  <tr>
    <td class="int">Numero SIEP</td>
    <td class="int">Magistrato Assegnatario</td>
  </tr>
<%
for (int i = 0; i < aListaProcedimenti.size() && i < MAX_ANTEPRIMA; i++) {
  FascicoloSiepModel lFascicolo = (FascicoloSiepModel) aListaProcedimenti.elementAt(i);
%>
  <tr>
    <td class="c"><font class="label"><%=StringUtils.toStringJSP(lFascicolo.getChiaveAnno(),"")%>/<%=StringUtils.toStringJSP(lFascicolo.getChiaveProgr(),"")%></font></td>
    <td class="c"><font class="label"><%=StringUtils.toStringJSP(aMagistrato.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(aMagistrato.getNome())%></font></td>
  </tr>
<%
}
%>
</table>
<%
} else {
%>
<table cellspacing="2" cellpadding="2" align="center" width="60%">
  <tr>
    <td class="c">
      <font class="label">Nessun procedimento del Magistrato <%=StringUtils.toStringJSP(aMagistrato.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(aMagistrato.getNome())%> risponde al criterio indicato</font>
    </td>
  </tr>
</table>
<%
}
%>
</form>
</body>
</html>
