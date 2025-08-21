<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.rateizzazionepp.model.RateizzazionePPModel"%>
<%@ page import="siap.siep.rateizzazionepp.action.ICostantiRateizzazionePP"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.pagoPA.model.CivilmenteObbligatoModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.istitutodetenzione.action.ICostantiIstitutoDetenzione"%>
<%@ page import="siap.siep.pagoPA.model.BollettinoPagopaModel"%>
<%@ page import="siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>

<jsp:useBean id="listaOrdiniIngiunzione"        scope="request" class="java.util.Vector<EventoRateizzazionePPModel>"/>

<jsp:useBean id="civilmenteObbligati"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="magistrato"              scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="posizioneluogoaltra"     scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="avvocati"                scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaN"        scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaE"        scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaCivilObb" scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita"              scope="request" class="java.lang.String"/>
<jsp:useBean id="eventonotifica"        scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

Date dataEmissione    = DateUtils.getSysDate();
Date dataTrasmissione = DateUtils.getSysDate();

if (eventonotifica.getEvento().getIdEvento() != null) {
  dataEmissione    = eventonotifica.getEvento().getDataEmissione();
  dataTrasmissione = eventonotifica.getNotifiche()[0].getDataInvio();
}

if (lPosizione == null)
	lPosizione = new PosizioneGiuridicaModel();

if (lLuogoDetenzione == null)
	lLuogoDetenzione = new LuogoDetenzioneModel();

if (lAltraCausa == null)
	lAltraCausa = new AltraCausaModel();
%>
<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>  
<script language="JavaScript">
function ListaComuni(a_formname,a_fieldname) {
  var desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaMagistrati(a_formname) {
  var desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
  var desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function espandi(idEvento){
  var collapseGif = "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
  var hrefNew = "Javascript:collassa('"+idEvento+"');";
	  
  $('#'+idEvento+' a').attr('href',hrefNew);
  $('#'+idEvento+' a').children().attr('src',collapseGif);
	  
  $("tr[customAttrIdEvento='"+idEvento+"']").show(); 
}
	
function collassa(idEvento){
  var expandGif = "<%=IWebConstants.IMAGES_DIR%>expand.gif";
  var hrefNew = "Javascript:espandi('"+idEvento+"');";
  
  $('#'+idEvento+' a').attr('href',hrefNew);
  $('#'+idEvento+' a').children().attr('src',expandGif);
  
  $("tr[customAttrIdEvento='"+idEvento+"']").hide();
}

function Verify() {
	var dataOdierna = "<%=DateUtils.getSysDate("dd")%>-<%=DateUtils.getSysDate("MM")%>-<%=DateUtils.getSysDate("yyyy")%>";
	
  if (document.LoadInserisciEstinzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
    document.LoadInserisciEstinzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciEstinzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
  if (document.LoadInserisciEstinzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
    document.LoadInserisciEstinzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciEstinzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

  var data_to_verify = document.LoadInserisciEstinzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
    +'/'+ document.LoadInserisciEstinzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
    +'/'+ document.LoadInserisciEstinzione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

  if (!ControllaData(data_to_verify)) {
    alert('Data di emissione non valida');
    document.LoadInserisciEstinzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
    return false;
  }
  
  if (!CompareDate(data_to_verify, dataOdierna)) {
		alert('La Data Emissione non può essere superiore alla data odierna');
    document.LoadInserisciEstinzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;  
  }

  // Data Trasmissione
  if (document.LoadInserisciEstinzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
    document.LoadInserisciEstinzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciEstinzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
  if (document.LoadInserisciEstinzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
    document.LoadInserisciEstinzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciEstinzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

  var data_to_verify = document.LoadInserisciEstinzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value
    +'/'+ document.LoadInserisciEstinzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value
    +'/'+ document.LoadInserisciEstinzione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

  if (!ControllaData(data_to_verify)) {
    alert('Data di Trasmissione non valida');
    document.LoadInserisciEstinzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
    return false;
  }

  if (!CompareDate(data_to_verify, dataOdierna)) {
		alert('La Data Trasmissione non può essere superiore alla data odierna');
    document.LoadInserisciEstinzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
		return false;  
	}
  
  // Autorita x la Notifica
  if (typeof document.LoadInserisciEstinzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%> !== "undefined") {
      if (document.LoadInserisciEstinzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value=="-") {
      alert("Selezionare l'autorita' per la notifica al condannato");
      document.LoadInserisciEstinzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
      return false; 
    }
    if (document.LoadInserisciEstinzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "") {
      alert("Selezionare la sede dell'autorita' per la notifica al condannato");
      document.LoadInserisciEstinzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
      return false; 
      }
  } else if (typeof document.LoadInserisciEstinzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%> !== "undefined") {
    if (document.LoadInserisciEstinzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
      alert("Selezionare l'istituto di detenzione per la notifica al condannato");
      document.LoadInserisciEstinzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
      return false; 
      }
  } else if (typeof document.LoadInserisciEstinzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %> !== "undefined") {
    if (document.LoadInserisciEstinzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
      alert("Selezionare l'istituto di detenzione per la notifica al condannato");
      document.LoadInserisciEstinzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
      return false; 
      }          
  } else {
      alert("destinatario sconosciuto");
      return false;
  }
  return true;
}

function caricaNotifiche () {
<%
if ("M".equals(modalita)) {
  NotificaModel[] lNotifiche = eventonotifica.getNotifiche();
  for (int i = 0; i < lNotifiche.length; i++) {
    NotificaModel lNotifica = lNotifiche[i];
    String codTipoAutorita = "";
    String sedeAutorita = "";
    String indirizzoAutorita = "";
    String descIstituto = "";
    String idIstituto = "";
  
    if (lNotifica.getAutoritaEsterna() != null) {
      codTipoAutorita = lNotifica.getAutoritaEsterna().getCodTipoAutorita();
      sedeAutorita    = StringUtils.toStringJSP(lNotifica.getAutoritaEsterna().getDescrSede(),"");
      indirizzoAutorita = StringUtils.toStringJSP(lNotifica.getNote(),"");
      if ("-".equals(sedeAutorita))
        sedeAutorita = "";
      } else if (lNotifica.getIstDetIdIstitutoDetenzione() != null) {
        descIstituto = lNotifica.getIstitutoDetenzione().getDescrTipoIstituto()+" di "+lNotifica.getIstitutoDetenzione().getDescrComune();
        idIstituto   = lNotifica.getIstitutoDetenzione().getIdIstitutoDetenzione();
      }
    
      if (lNotifica.getIdCivilmenteObbligato() == null && lNotifica.getAvvIdAvvocatoFascicoloSiep() == null) {
        if (lNotifica.getAutoritaEsterna() != null) {
%>
          $('#<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%> option[value="<%=codTipoAutorita%>"]').attr("selected", "selected");
          $('#<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>').val("<%=sedeAutorita%>");
          $('#<%=ICostantiNotifica.CAMPO_NOTE_E%>').val("<%=indirizzoAutorita%>");
<%
        } else {
%>
          $('#descIstituto').val("<%=descIstituto%>");
          $('#<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>').val("<%=idIstituto%>");
<%
        }
      }
      
      if (lNotifica.getIdCivilmenteObbligato() != null) {
%>
        $('#<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_CO_<%=lNotifica.getIdCivilmenteObbligato()%> option[value="<%=codTipoAutorita%>"]').attr("selected", "selected");
        $('#<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>_CO_<%=lNotifica.getIdCivilmenteObbligato()%>').val("<%=sedeAutorita%>");
        $('#<%=ICostantiNotifica.CAMPO_NOTE_E%>_CO_<%=lNotifica.getIdCivilmenteObbligato()%>').val("<%=indirizzoAutorita%>");
<%
      }
    
      if (lNotifica.getAvvIdAvvocatoFascicoloSiep() != null) {
%>
        $('#<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_AVV_<%=lNotifica.getAvvIdAvvocatoFascicoloSiep()%> option[value="<%=codTipoAutorita%>"]').attr("selected", "selected");
        $('#<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_AVV_<%=lNotifica.getAvvIdAvvocatoFascicoloSiep()%>').val("<%=sedeAutorita%>");
        $('#<%=ICostantiNotifica.CAMPO_NOTE%>_AVV_<%=lNotifica.getAvvIdAvvocatoFascicoloSiep()%>').val("<%=indirizzoAutorita%>");
<%
      }
  }
}
%>
}
</script>
</head>
  
<body class="corpo" onLoad="caricaNotifiche();">
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
      <font class="campo">Provvedimento Avvenuto Pagamento Pena Pecuniaria</font>
<%
} else if ("M".equals(modalita)) {
%>
      <font class="campo">Modifica Provvedimento Avvenuto Pagamento Pena Pecuniaria</font>
<%
}
%>
    </td>
      <td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzioneAltriProvvedimenti">
          <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>      
      </td>     
  </tr>
</table>
  
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
  
<table>
  <tr>
    <td class="l">Posizione Giuridica </td>
    <td class="L" colspan=5>
        <font class="campo">
<%
if(lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
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
      <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
    </td>
  </tr>
</table>


<%
// Sezione con l'importo da pagare a la rateizzazione
// MEV_2023-33: aggiunto controllo per storicizzazione evento OIP
/*
Iterator iterLR = listaRateizzazioni.iterator();
String lTipoRateizzazione = "";
BigDecimal lImportoDaPagare = null;
while (iterLR.hasNext()) {
  RateizzazionePPModel rata = (RateizzazionePPModel) iterLR.next();
  // RateizzazionePPModel primarata = (RateizzazionePPModel) listaRateizzazioni.elementAt(0);
  if (!rata.isStoricizzato()) {
    lTipoRateizzazione = rata.getTipoRateizzazione();
    lImportoDaPagare = rata.getImportoDaPagare();
    break;
  }
}*/
%>
<br>


<FORM method="POST" name="LoadInserisciEstinzione" action="<%= IWebConstants.PG_MAIN%>">
<table width="70%">
  <tr>
    <td>
<%
if ("I".equals(modalita)) {
%>
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.rateizzazionepp.action.ActInserisciProvvedimentoEstinzionePP">
<%
} else if ("M".equals(modalita)) {
%>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.rateizzazionepp.action.ActModificaProvvedimentoEstinzionePP">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
<%
}
%>
    </td>
  </tr>
</table>



<%
//========================================================================= 
//
//========================================================================= 
%>

<table cellspacing="0" cellpadding="0" width="95%">
<%
Iterator<EventoRateizzazionePPModel> itxOrdini = listaOrdiniIngiunzione.iterator();
while (itxOrdini.hasNext()) {
  EventoRateizzazionePPModel erppm = (EventoRateizzazionePPModel) itxOrdini.next();
  EventoModel em = erppm.getEvento();
  String lTipoRateizzazione = erppm.getListaRateizzazioniPP().elementAt(0).getTipoRateizzazione();
  // BigDecimal lImportoDaPagare = erppm.getListaRateizzazioniPP().elementAt(0).getImportoDaPagare();
  BigDecimal importoDaPagare = new BigDecimal(0);
  BigDecimal importoRate   = new BigDecimal(0);
  BigDecimal importoPagato = new BigDecimal(0);
  String styleImporto = "";
  for (RateizzazionePPModel rateizzazione: erppm.getListaRateizzazioniPP()) {
  	importoDaPagare = rateizzazione.getImportoDaPagare();
  	for (BollettinoPagopaModel bollettino: rateizzazione.getListaBollettini()) {
	    importoRate   = importoRate.add(bollettino.getImportoRata());
	    importoPagato = importoPagato.add(bollettino.getImportoPagato()!=null ? bollettino.getImportoPagato(): new BigDecimal(0) );
  	}
  }
  if (importoDaPagare.compareTo(importoPagato)!=0) 
  	styleImporto="style='color:red;'";
  else 
  	styleImporto="style='color:green;'";
  	
  //#000080; 9595a3 	#727295
  String styleTitoloOrdIng     = " style='text-align:left;'";
  String styleTitoloBollettino = " style='background-color:#000080;' ";
%>  
  <tr style="background-color: green;">
    <td class="int" colspan="9" <%=styleTitoloOrdIng%> id="<%=em.getIdEvento()%>" >
      <a href="Javascript:espandi('<%=em.getIdEvento()%>');"><img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0"/></a>
        <%=StringUtils.toStringJSP(em.getDescrTipoProvvedimento()) + " " + StringUtils.toStringJSP(em.getDescrMotivo())%>
    &nbsp;del&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(em.getDataEmissione(), "dd/MM/yyyy"))%>
    </td>
  </tr>
  <tr>
    <td class="L" colspan="9">
      <font class="label">Importo da pagare</font>
      <font class="campo"><%=StringUtils.toEuroFormat(importoDaPagare)%> &euro;</font>
      <font class="label" <%=styleImporto%>>&nbsp;Importo pagato</font>
      <font class="campo" <%=styleImporto%>>&nbsp;<%=StringUtils.toEuroFormat(importoPagato)%> &euro;</font>
    </td>
  </tr>
  <!--  
  <tr>
    <% if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)) {%>
    <td class="Titolo" colspan="9"> Pagamento in una Unica Soluzione </td>
    <% } else if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)) {%>
    <td class="Titolo" colspan="9"> Pagamento Rateizzato </td>
    <% } %>
  </tr>
  -->
<%
  // Recupero le rateizzazioni
  Vector<RateizzazionePPModel> listaRateizzazioni = erppm.getListaRateizzazioniPP();
  Iterator<RateizzazionePPModel> IteRate = listaRateizzazioni.iterator();
  int conta = 0;
  while (IteRate.hasNext()) {
    conta++;
    RateizzazionePPModel rata = (RateizzazionePPModel) IteRate.next();
    String spunta = "V";
    if ("A".equals(em.getFlagDocumentoRegistrato())) {
      spunta = "TickRed";
    }
    %>
    
  <tr style="display:none;" customAttrIdEvento="<%=em.getIdEvento() %>">
    <% if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)) { %>
    <td class="L" rowspan="<%=new BigDecimal(1).add(rata.getNumeroRate())%>"><font class="label">Rata unica da</font>&nbsp;<font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font></td>      
    <% } else if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)) { %>
    <td class="L" rowspan="<%=new BigDecimal(1).add(rata.getNumeroRate())%>">
      <font class="campo"><%=StringUtils.toStringJSP(rata.getNumeroRate(),"&nbsp;")%></font>
      <font class="label"> rate da </font>
      <font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font>
    </td>
    <% } %>
    <td class="int" <%=styleTitoloBollettino%>>N.ro Ordine</td>
    <td class="int" <%=styleTitoloBollettino%>>Tipo Pagamento</td>
    <td class="int" <%=styleTitoloBollettino%>>IUV</td>
    <td class="int" <%=styleTitoloBollettino%>>Importo</td>
    <td class="int" <%=styleTitoloBollettino%>>Importo Pagato</td>
    <td class="int" <%=styleTitoloBollettino%>>Data Pagamento</td>
    <td class="int" <%=styleTitoloBollettino%>>Data Scadenza</td>
    <td class="int" <%=styleTitoloBollettino%>>Stato</td>
  </tr>
  
<% 
  Iterator<BollettinoPagopaModel> itxBoll = rata.getListaBollettini().iterator();
  while (itxBoll.hasNext()) 
  {
    BollettinoPagopaModel bpm = (BollettinoPagopaModel) itxBoll.next();
    String coloreClasse = "cVerde";
    if ("PN".equals(bpm.getStatoPagamento())) 
      coloreClasse = "cRosso";     
    
    String coloreClasseImporto = "c";
    if (bpm.getImportoPagato()==null || bpm.getImportoRata().compareTo(bpm.getImportoPagato())!=0  ) 
      coloreClasseImporto = "cRosso"; 
    %>   
    <tr style="display:none;" customAttrIdEvento="<%=em.getIdEvento() %>">
      <td class="c"><%=StringUtils.toStringJSP(bpm.getProgRata())%></td>      
      <% if ("U".equals(bpm.getTipoRateizzazione())) {%>
      <td class="l"><%=StringUtils.toStringJSP(bpm.getDescrTipoRateizzazione().toUpperCase())%></td>
      <% } else {%>
      <td class="l"><%=StringUtils.toStringJSP(bpm.getProgRata())%>&nbsp;RATA</td>
      <%}%>     
      <td class="c"><%=StringUtils.toStringJSP(bpm.getIuv(), "-")%></td>
      <td class="c"><%=StringUtils.toEuroFormat(bpm.getImportoRata())%></td>
      <td class="<%=coloreClasseImporto%>"><%=StringUtils.toEuroFormat(bpm.getImportoPagato())%></td>
      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(bpm.getDataAvvPagamento(), "dd/MM/yyyy"), "-")%></td>
      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(bpm.getDataScadenza(), "dd/MM/yyyy"), "-")%></td>     
      <td class="<%=coloreClasse%>"><%=StringUtils.toStringJSP(bpm.getDescrStatoPagamento())%></td>
    </tr>
  <% } // end while sui bollettini%>

  <% for (int i=rata.getListaBollettini().size(); i<rata.getNumeroRate().intValue();i++) {%>
    <tr style="display:none;" customAttrIdEvento="<%=em.getIdEvento() %>">
      <td class="c">n.d.</td>      
      <td class="c">n.d.</td>
      <td class="c">n.d.</td>
      <td class="c">n.d.</td>
      <td class="c">n.d.</td>
      <td class="c">n.d.</td>
      <td class="c">n.d.</td>
      <td class="c">n.d.</td>
    </tr>
  <% } %>
  
<% } // end while sulle rate %>
  <tr>
    <td colspan="9">&nbsp;<td>
  </tr>
<% } // end while su oi rate %>

</table> 

<%
//========================================================================= 
//
//========================================================================= 
%>

<br>

<table>
<%
  Iterator<CivilmenteObbligatoModel> itx = civilmenteObbligati.iterator();
  while (itx.hasNext()) {
    CivilmenteObbligatoModel com = (CivilmenteObbligatoModel) itx.next();
%>
  <tr>
    <td class="l">Civilmente Obbligato: </td>
    <td class="l">
      <font class="campo"><%=com.getCognome()%></font>&nbsp;<font class="campo"><%=com.getNome()%></font>
<%
    if ("G".equals(com.getCodPersona())) {
%>
      <font class="label"> in qualita' di Legale Rappresentante di </font>
      <font class="campo"><%=com.getDenominazione()%></font>
<%
    }
%>
    </td>
  </tr>
<%
  }
%>
</table>

<br>

<table>
  <tr>
    <td class="L">Data Emissione</td>
    <td class="L" colspan="2" >
      <input value="<%=DateUtils.getDateToString(dataEmissione, "dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getDateToString(dataEmissione, "MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getDateToString(dataEmissione, "yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
    </td>
    <td class="L">Data Trasmissione</td>
    <td class="L" colspan="2">
      <input value="<%=DateUtils.getDateToString(dataTrasmissione, "dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=DateUtils.getDateToString(dataTrasmissione, "MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getDateToString(dataTrasmissione, "yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>
  </tr>
</table>

<%
//=======================================================================
//                     Magistrato
//=======================================================================
%>
<br>
<table width="100%">
  <tr>
      <td class="Titolo" colspan=6> Magistrato </td>
  </tr>
  <tr>
    <td class="l">Magistrato</td>
    <td class="L" colspan="3">
      <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('LoadInserisciEstinzione');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
    <td>
      <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
    </td>
  </tr>
</table>

<%
//=======================================================================
//                     Notifica al condannato
//=======================================================================
%>
<table width="100%">
  <tr>
    <td class="Titolo" colspan="8">Notifica al Condannato</td>
  </tr>
<%
  if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
      // Se detenuto altra causa in Custodia Cautelare
      if (posizioneluogoaltra.getAltraCausa() != null
          && (posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("23") // Custodia Cautelare in Regime di Arresti Domiciliari
          || posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("78") // Custodia Cautelare per AC
          || posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("79") // Custodia Cautelare per AC
          || posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("80") // Custodia Cautelare per AC
          || posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("81"))) { // Custodia Cautelare per AC
%>
    <tr>
    <td class="L" width="20%">Autorita' Destinazione <font class="ob">(*)</font></td>
    <td class="L"  colspan="3">
       <select  Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>"
                id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>"
       >
        <%=autoritaEsternaE%>
       </select>
      </td>
    </tr>
    
    <tr>
      <td class="L">Sede <font class="ob">(*)</font></td>
      <td class="L">
        <input title="Sede Autorita Esterna" value="" type="text" maxlength="35" size="35"
                name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>" 
                  id="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>">
        <a href="Javascript:ListaComuni('LoadInserisciEstinzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td class="L">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" id="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="30"></textarea>
      </td>
    </tr>
  <%} else {%>
    <tr>
      <td class="l" width="20%">Autorita' Destinazione <font class=ob>(*)</font></td>

    <%if(   posizioneluogoaltra!= null 
         && posizioneluogoaltra.getAltraCausa()!= null 
         && posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione() != null)
     {%>
      <td class="l">
        <input readonly Title="Istituto" name="Comune" id="descIstituto" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrComune())%>" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" 
               id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>"
               value="<%=posizioneluogoaltra.getAltraCausa().getIstDetIdIstitutoDetenzione()%>" size="50">
        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciEstinzione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          <img src="/images/filefolder.gif" border=0></a>
      </td>
      <% } else {%>
        <td class="l">
          <input readonly Title="Istituto" name="Comune" id="descIstituto"  value="" size="50">
          <input type="hidden"  Title="Istituto" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" 
                 id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>" value="">
          <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciEstinzione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          <img src="/images/filefolder.gif" border=0></a>
        </td>
      <% } %>

      <td class="l">Note</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="35"></textarea>
      </td>
   </tr>
  <%}%>

<%
} // Fine detenuto Altra causa   
else 
{
%>  
    <tr>
  <%if(    lPosizione.getCodPosizioneGiuridica().equals("74") || lPosizione.getCodPosizioneGiuridica().equals("75") 
        || lPosizione.getCodPosizioneGiuridica().equals("76") || lPosizione.getCodPosizioneGiuridica().equals("77"))
      // ALTRA_CAUSA
      // Espiazione pena per Altra Causa in Regime di Detenzione
      // Espiazione pena per Altra Causa in Misura Sicurezza Detentiva (Internato)
      // Custodia Cautelare per Altra Causa in Regime di Detenzione
      // Espiazione pena per Altra Causa in Misura di Sicurezza Applicata in Via Provvisoria
      
  {%>
     <td class="l" width="20%">Istituto di Detenzione <font class=ob>(*)</font></td>
  <%} else {%>
     <td class="l" width="20%">Autorita' Destinazione <font class=ob>(*)</font></td>
  <%}%>

  <%if(   lPosizione.getCodPosizioneGiuridica().equals("07") || lPosizione.getCodPosizioneGiuridica().equals("10") 
       || lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")
       || lPosizione.getCodPosizioneGiuridica().equals("16") || lPosizione.getCodPosizioneGiuridica().equals("20") 
       || lPosizione.getCodPosizioneGiuridica().equals("46") || lPosizione.getCodPosizioneGiuridica().equals("47")   
       || lPosizione.getCodPosizioneGiuridica().equals("78") || lPosizione.getCodPosizioneGiuridica().equals("79") 
       || lPosizione.getCodPosizioneGiuridica().equals("80") || lPosizione.getCodPosizioneGiuridica().equals("81")
       || lPosizione.getCodPosizioneGiuridica().equals("70") || lPosizione.getCodPosizioneGiuridica().equals("71")
       || lPosizione.getCodPosizioneGiuridica().equals("72") )
  {%>
      <td class="L" colspan="3">
       <select  Title="Autorita Esterna" class="small" 
                 name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>" 
                   id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
        <%=autoritaEsternaE%>
       </select>
      </td>

      <td class="l">Sede <font class=ob>(*)</font></td>
      <td class="L">
        <input title="Sede Autorita Esterna" value="" type="text" maxlength="35" size="35"
                name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>" 
                  id="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>">
        <a href="Javascript:ListaComuni('LoadInserisciEstinzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" id="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="30"></textarea>
      </td>
  <%
  } 
  else 
  {
    // Detenuto    
    if(lLuogoDetenzione != null && lLuogoDetenzione.getIstitutoDetenzione() != null){%>
    <td class="l">
        <input readonly Title="Istituto" name="Comune" id="descIstituto"  value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" 
               id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>"
               value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>">
        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciEstinzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          <img src="/images/filefolder.gif" border=0></a>
    </td>
    <%}else {%>
    <td class="l">
        <input readonly Title="Istituto" name="Comune" id="descIstituto"  value="" size="50">
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" 
               id="<%=ICostantiIstitutoDetenzione.CAMPO_ID_ISTITUTO_DETENZIONE%>" value="" >
        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciEstinzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          <img src="/images/filefolder.gif" border=0></a>
    </td>
    <%}%>
  </tr>
  <tr>
      <td class="l">Note</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=35></textarea>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
 <% } // %>
<% } %>
</table>


<%
//=======================================================================
//                      Notifica al Difensore
//=======================================================================
%>

    <table width=100%>
      <tr>
        <td class="Titolo" colspan="6">Notifica al Difensore</td>
      </tr>
    </table>
<%
      int lIdxAvv = 0;
      int lNumAvvocati = avvocati.size();
      Iterator lItxAvv = avvocati.iterator();
      while( lItxAvv.hasNext() )
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        <table width="100%">
          <tr>
            <td class="l">Per Avvocato </td>
            <td class="L" colspan="3">
              <input type="hidden" name="indexAvvocati" value="<%=lIdxAvv%>">
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              </font>
              &nbsp;Foro di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              </font>
              &nbsp;Difensore di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              </font>
              <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
            </td>
          </tr>        
          <tr>
            <td class="l">Autorita' Destinazione </td >
            <td class="L" colspan="3">
               <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" 
                       id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_AVV_<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()%>">
                 <%=autoritaEsternaN%>
               </select>
      </td>
  </tr>
  <tr>
    <td class="l">Sede </td>
    <td class="L">
            <input title="Sede Foro Avvocato" type="text" maxlength="35" size="35" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" 
                   id="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_AVV_<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()%>">
            <% if( lNumAvvocati < 2 ) { %>
      <a href="Javascript:ListaComuni('LoadInserisciEstinzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>');">
        <img src="/images/filefolder.gif" border="0">
      </a>
            <% } else {%>
             <a href="Javascript:ListaComuni('LoadInserisciEstinzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
        <img src="/images/filefolder.gif" border="0">
      </a>
            <% } %>
    </td>
    <td class="l">Note</td>
    <td class="L">
      <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols="35" id="<%=ICostantiNotifica.CAMPO_NOTE%>_AVV_<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()%>"></textarea>
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
</table>
<%
  lIdxAvv++;
}
%>    

<%
//=======================================================================
//                  Notifica al Civilmente Obbligato
//=======================================================================
%>

<%
Iterator<CivilmenteObbligatoModel> itx1 = civilmenteObbligati.iterator();
while (itx1.hasNext()) {
  CivilmenteObbligatoModel lObbligatoModel = (CivilmenteObbligatoModel) itx1.next();
%>
<table width="100%">
  <tr><td class="Titolo" colspan="4">Notifica al Civilmente Obbligato </td></tr>
  <tr>
    <td class="L">Civilmente Obbligato: </td>
    <td class="L" colspan="3">
      <font class="campo"><%=lObbligatoModel.getCognome()%></font>&nbsp;
      <font class="campo"><%=lObbligatoModel.getNome()%></font>&nbsp;
      <font class="label">nato a</font>&nbsp;<font class="campo"><%=lObbligatoModel.getDescComuneNascita()%></font>&nbsp;
      <font class="label">il</font>&nbsp;<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lObbligatoModel.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
      <% if ("G".equals(lObbligatoModel.getCodPersona())) { %>
      <font class="label"> in qualita' di Legale Rappresentante di </font>
      <font class="campo"><%=lObbligatoModel.getDenominazione()%></font>
      <% } %>    
    </td> 
  </tr>    
  
  <tr>
    <td class="L">Autorita' Destinazione</td>
    <td class="L" colspan="3">
     <select Title="Autorita Esterna" class="small" 
             id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"
             name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"
     >
      <%=autoritaEsternaCivilObb%>
     </select>
    </td>
  </tr>
  
  <tr>
    <td class="L">Sede</td>
    <td class="L">
      <input type="text" maxlength="35" size="35" title="Sede Autorita Esterna"
             id="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"
             name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"  >
      <a href="Javascript:ListaComuni('LoadInserisciEstinzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>');">
        <img src="/images/filefolder.gif" border="0">
      </a>
    </td>
    <td class="L">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" cols="30" id="<%=ICostantiNotifica.CAMPO_NOTE_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>_CO_<%=lObbligatoModel.getIdCivilmenteObbligato()%>"></textarea>
    </td>
  </tr>  
</table>  
<%
  }
%>
  
<%
//=======================================================================
//        Notifica al Difensore del Civilmente Obbligato
//=======================================================================
/*
<table width="100%">
  <tr><td class="Titolo" colspan="6">Notifica al Difensore del Civilmente Obbligato </td></tr>
</table>
*/
%>

<table width="100%">
    <tr>
      <td class="lNoBord" colspan="2">
          <INPUT class="bottone" type="submit" value="Conferma">
      </td>
    </tr>
</table>  

</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciEstinzione");  
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>