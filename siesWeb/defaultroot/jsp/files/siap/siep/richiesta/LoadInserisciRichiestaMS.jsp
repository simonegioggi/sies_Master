<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.richiesta.action.ICostantiRichiesta" %>

<jsp:useBean id="soggettoSiep"   scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="sentenzaSiep"   scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="misuraalternativa"   scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel" />
<jsp:useBean id="sedeUfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel" />
<jsp:useBean id="magistratosorveglianza"   scope="request" class="siap.sico.magistrato.model.MagistratoModel" />

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  //PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
 // LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
//String lcodicePosizione =lPosizione.getCodPosizioneGiuridica();
 // AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  /*if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();*/
%>
<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  function Verify()
	{
    if(document.LoadInserisciRichieste.<%=ICostantiMisuraAlternativa.CAMPO_COGNOME %>.value=="" && document.LoadInserisciRichieste.<%=ICostantiMisuraAlternativa.CAMPO_NOME %>.value=="")
      {
        alert("Il  Magistrato competente è obbligatorio");
        return false;
      }
  }
    function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
  </script>
</head>
<body class="corpo">
<table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Esito Espiazione Pena</font>
      </td>
    </tr>
  </table>
<br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadInserisciRichieste" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActInserisciRichiestaMS">
  <table>
		<tr>
      <td class="l">Anno /Numero SIUS</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
      <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
     </td>
  </tr>
  <tr>
     <td class="l"> Anno / Numero Ordinanza </td>
     <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
     <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
     </td>
  </tr>
 <tr>
    <td class="l">Ufficio Emittente </td>
    <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrTipoUfficio())%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescProvincia())%></font></td>
    <input type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>" value="<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescProvincia())%>">
</tr>
 <tr>
   <td class="l">Oggetto Ordinanza </td>
   <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
 </tr>
 <tr>
  <td class="l">Data Emissione Ordinanza </td>
   <td class="l"><font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%>
    </font>
   </td>
  </tr>
<tr>
  <td  class="l">Note</td>
     <td  class="L">
       <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE %>" cols=40 rows=2></textarea>
    </td>
</tr>
<tr><td class="Titolo" colspan="2">Magistrato di Sorveglianza</td></tr>
<tr>
  <td  class="l">Magistrato </td>
     <td class="L">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratosorveglianza.getCodMagistrato() )%>" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
       <input title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratosorveglianza.getCognome() )%>" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratosorveglianza.getNome() )%>" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciRichieste','<%=ICostantiMisuraAlternativa.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMisuraAlternativa.CAMPO_COGNOME %>','<%= ICostantiMisuraAlternativa.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
        </a>
    </td>
</tr>
<tr>
  <td  class="l">Note</td>
     <td  class="L">
       <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_TDS%>" cols=40 rows=2></textarea>
    </td>
</tr>
 <tr><td>&nbsp;</td></tr>
 <tr>
   <td class="lNoBord" colspan="2">
     <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
   </td>
 </tr>
</table>
</form>
</body>
</html>