<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-33: aggiunta pagina --%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>

<%@ page import="siap.sico.evento.controller.IEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>


<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="oggettodefinzione"   scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutorita"      scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="modalita"       scope="request" class="java.lang.String"/>
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="archiviazione"  scope="request" class="siap.siep.archiviazione.model.ArchiviazioneModel"/>

<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

Date dataEmissione   = DateUtils.getSysDate();
Date dataRicezione   = DateUtils.getSysDate();
Date dataDefinizione = null;
String sedeAutEst = "";
String indAutEst = "";
if (archiviazione.getIdArchiviazione()!=null){
  dataEmissione    = archiviazione.getDataEmissione();
  dataRicezione    = archiviazione.getDataRicezione();
  dataDefinizione  = archiviazione.getDataDefinizione();
  sedeAutEst       = archiviazione.getDescrLuogoEmittente();
  indAutEst        = archiviazione.getIndirizzoEmittente();
}

SoggettoModel lSoggettoAssociato = lFascicoloAssociato.getSoggetto();

UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
 
String lCasellario = lUfficioUtenteConnesso.getDescrComune();

if( lSoggettoAssociato != null 
    && 
      (    lSoggettoAssociato.getCodStatoNascita() == null
        ||  "".equals(lSoggettoAssociato.getCodStatoNascita()) 
        || "-".equals(lSoggettoAssociato.getCodStatoNascita())
       )
   )
{
  lCasellario = "-";
}

if (eventonotifica.getNotifiche()!=null && eventonotifica.getNotifiche().length>0){
	// Sto in modifica
  lCasellario = eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrSede();
}
%>
<html>
<head>
<title>[S.I.E.S.] - Gestione Provvedimento Rideterminazione Pena Pecuniaria </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>  
<script language="JavaScript">
function ListaComuni(a_formname,a_fieldname)
{
  var desktop;
  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaMagistrati(a_formname)
{
  var desktop;
  desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
{
  desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function Verify()
{
  var dataOdierna = "<%=DateUtils.getSysDate("dd")%>-<%=DateUtils.getSysDate("MM")%>-<%=DateUtils.getSysDate("yyyy")%>";
  
  //DATA EMISSIONE
  if (document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
  if (document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value;

  var data_to_verify = document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.value;

  if (!ControllaDataPassaVuota(data_to_verify) )
  {
    alert('Data emissione non valida');
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
    return false;
  }
  
  if (data_to_verify!='//' && !CompareDate(data_to_verify, dataOdierna)) {
    alert('La Data Emissione non può essere superiore alla data odierna');
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
    return false;  
   }
  
  //DATA RICEZIONE
  if (document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value.length==1)
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value='0'+document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value;
  if (document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value.length==1)
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value='0'+document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value;

  var data_to_verify = document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value+'/'+document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value+'/'+document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>.value;

  if (!ControllaDataPassaVuota(data_to_verify) )
  {
    alert('Data ricezione non valida');
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.focus();
    return false;
  }
  
  if (data_to_verify!='//' && !CompareDate(data_to_verify, dataOdierna)) {
    alert('La Data Ricezione non può essere superiore alla data odierna');
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.focus();
    return false;  
   }
  
  //DATA DEFINIZIONE
  if (document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value.length==1)
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
  if (document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value;

  var data_to_verify = document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

  if (!ControllaData(data_to_verify) )
  {
    alert('Data definizione non valida');
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();
    return false;
  }
  
  if (data_to_verify!='//' && !CompareDate(data_to_verify, dataOdierna)) {
    alert('La Data Definizione non può essere superiore alla data odierna');
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();
    return false;  
  }
  
  if (document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.selectedIndex].value == '-')
  {
    alert("Il Campo Oggetto definizione è obbligatorio");
    document.LoadInserisciDefinizionePP.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.focus();
    return false;
  }

  if(document.LoadInserisciDefinizionePP.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
  {
    alert("Il Cognome del Magistrato è obbligatorio");
    document.LoadInserisciDefinizionePP.<%=ICostantiMagistrato.CAMPO_COGNOME%>.focus();
    return false;
  }

  if(document.LoadInserisciDefinizionePP.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
  {
    alert("Il Nome del Magistrato è obbligatorio");
    document.LoadInserisciDefinizionePP.<%=ICostantiMagistrato.CAMPO_NOME%>.focus();
    return false;
  }  
  
  if (   document.LoadInserisciDefinizionePP.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '-' 
      || document.LoadInserisciDefinizionePP.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '' 
     )
  {
     alert("Il campo Casellario Giudiziale è obbligatorio!");
     document.LoadInserisciDefinizionePP.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS%>.focus();
     return false;
  }
  return true;
}
</script>
</head>

<body class="corpo">
<table>
  <tr>
    <td class="LBG">
      <a href="Javascript:window.print();">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
      </a>
    </td>
    <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
<%
if ("I".equals(modalita)) {
%>
      <font class="campo">Inserimento Definizione Procedimento - Pena Pecuniaria</font>
<%
} else if ("M".equals(modalita)) {
%>
      <font class="campo">Modifica Definizione Procedimento - Pena Pecuniaria</font>
<%
}
%>
    </td>
    <td class="LBG">
      <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzioneAltriProvvedimenti">
        <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>      
    </td>
  </tr>
</table>
  
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
  
<table cellspacing="0" cellpadding="0" width="95%">
  <tr>
    <td class="l" width="20%">Posizione Giuridica</td>
    <td class="L">
      <font class="campo">
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
        DETENUTO PER ALTRA CAUSA - <%=lAltraCausa.getDescrTipoPosGiuridica()%>
<%
} else {
%>
        <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
}
%>
      </font>
    </td>
  </tr>
</table>

<FORM method="POST" name="LoadInserisciDefinizionePP" action="<%=IWebConstants.PG_MAIN%>">
<% if ("I".equals(modalita)) { %>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.rateizzazionepp.action.ActInserisciDefinizioneProcedimentoPP">
<%} else if ("M".equals(modalita)) { %>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.rateizzazionepp.action.ActModificaDefinizioneProcedimentoPP">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
<% } %>

<%
//==============================================================================
//
//==============================================================================
%>
  <br>
  <table width="100%">
    <tr>
      <td colspan=4 class="titolo">Autorità emittente</td>
    </tr>
    <tr>
      <td class="l" width="25%">Numero Protocollo Nota</td>
      <td class="l" colspan="3">
         <input Title="Numero Nota" value="<%=StringUtils.toStringJSP(archiviazione.getNumNota())%>" name="<%=ICostantiArchiviazione.CAMPO_NUM_NOTA%>" type="text" size="35" maxlength="35">
      </td>
    </tr>
    <tr>
      <td class="l" width="25%">Data Emissione</td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissione, "dd"),"")%>" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissione, "MM"),"")%>" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissione, "yyyy"),"")%>" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l" width="25%">Data Ricezione</td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno Ricezione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataRicezione, "dd"),"")%>" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Ricezione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataRicezione, "MM"),"")%>" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Ricezione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataRicezione, "yyyy"),"")%>" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>

<%
//==============================================================================
//
//==============================================================================
%>
  <table width="100%">
    <tr>
      <td colspan=4 class="titolo">Dati Definizione Procedimento</td>
    </tr>
    <tr>
      <td class="l" width="25%">Data Definizione <font class=ob>(*)</font></td>
      <td class="l">
        <input type="text" Title="Giorno definizione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataDefinizione, "dd"),"")%>" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese definizione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataDefinizione, "MM"),"")%>" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno definizione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataDefinizione, "yyyy"),"")%>" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l" width="25%">Oggetto Definizione <font class=ob>(*)</font></td>
        <td class="l">
          <select Title="Oggetto Definzione" class="small" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>" onchange="tendina();">
            <%=oggettodefinzione%>
          </select>
        </td>
    </tr>
  </table>
  
  <table width="100%">  
    <tr>
      <td class="l" width="25%">Autorità che ha inviato la nota</td>
      <td class="L" colspan="3">
        <select  Title="Autorita"  class="small" name="<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>">
          <%=codiceAutorita%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
        <input title="Sede Autorita"  type="text" name="<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>"  value="<%=StringUtils.toStringJSP(sedeAutEst)%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciDefinizionePP','<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiArchiviazione.CAMPO_INDIRIZZO_EMITTENTE%>" cols=30 ><%=StringUtils.toStringJSP(indAutEst)%></textarea>
      </td>
    </tr>
  </table>  
  
<%
//==============================================================================
//
//==============================================================================
%>
  <table width="100%">
    <tr>
      <td colspan=4 class="titolo">Magistrato Firmatario</td>
    </tr>
    <tr>
      <td class="l">Magistrato Firmatario</td>
      <td class="L" colspan="3">
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"   value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>"    type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"      maxlength="35" size="25">
         <a href="Javascript:ListaMagistrati('LoadInserisciDefinizionePP');">
           <img src="/images/filefolder.gif" border=0>
         </a>
       </td>
       <td>
         <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35">
       </td>
    </tr>
    <tr>
      <td colspan=4 class="titolo">Destinatari</td>
    </tr>
    <tr>
      <td class="l" width="25%">Casellario Giudiziale</td>
      <td class="l" id="inputCasellario">
        <input title="Sede Casellario Giudiziale" value="<%= StringUtils.toStringJSP( lCasellario ) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>"  maxlength="35" size="35">
        <a href="Javascript:ListaUfficiPerTipo('LoadInserisciDefinizionePP','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>','DIB');">
            <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
  </table>  
  
  <br>
  <table width="100%">   
    <tr>
      <td class="lNoBord" >
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator = new Validator("LoadInserisciDefinizionePP");  
  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>