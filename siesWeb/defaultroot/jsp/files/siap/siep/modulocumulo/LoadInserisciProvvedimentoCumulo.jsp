<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiDatiFinaliCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaRideterminataCumuloModel"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"%>
<%@ page import="siap.sico.cssa.model.CSSAModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="IstruttoriaCumulo"        scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="datiFinaliAggregatoModel" scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>

<jsp:useBean id="motivoProvvedimento" scope="request" class="java.lang.String"/>

<jsp:useBean id="magistrato" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>

<jsp:useBean id="comboTDS"            scope="request" class="java.lang.String"/>
<jsp:useBean id="comboUDS"            scope="request" class="java.lang.String"/>
<jsp:useBean id="comboAutoritaN"      scope="request" class="java.lang.String"/>
<jsp:useBean id="comboUffRecuperoCrediti" scope="request" class="java.lang.String"/>

<jsp:useBean id="sedeUffRecuperoCrediti" scope="request" class="java.lang.String"/>


<jsp:useBean id="autoritaEsternaAvv"  scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"            scope="request" class="java.util.Vector"/>

<%
PosizioneGiuridicaCumuloModel lPosizioneGiuridicaCumulo = datiFinaliAggregatoModel.getPosizioneGiuridicaCumulo();
PenaRideterminataCumuloModel  lPenaRideterminataCumulo = datiFinaliAggregatoModel.getPenaRideterminataCumulo();
PenaRideterminataCumuloModel  lPenaResiduaCumulo = datiFinaliAggregatoModel.getPenaResiduaCumulo();

EventoModel lEvento = new EventoModel();
EventoNotificaModel lProvvedimentoCumulo = datiFinaliAggregatoModel.getProvvedimentoCumulo();

if (lProvvedimentoCumulo!=null) {
  lEvento = lProvvedimentoCumulo.getEvento();
}

IstitutoDetenzioneModel lIstituto = new IstitutoDetenzioneModel();
CSSAModel lCSSA = new CSSAModel();
UfficioModel lUfficioTDS = new UfficioModel();
UfficioModel lUfficioUDS = new UfficioModel();
AutoritaEsternaModel lAutDiDest = new AutoritaEsternaModel();
String lSedeAutEst = null;

AutoritaEsternaModel lUffRecCred = new AutoritaEsternaModel();
String lSedeUffRecCred = sedeUffRecuperoCrediti;
String lAutDiDestInd = null;

if(   lProvvedimentoCumulo!= null 
   && lProvvedimentoCumulo.getNotifiche()!=null 
   && lProvvedimentoCumulo.getNotifiche().length >0)
{
  int count = 0;
  while(count < lProvvedimentoCumulo.getNotifiche().length)
  {
    NotificaModel lNotMod = lProvvedimentoCumulo.getNotifiche()[count];
  
    if (lNotMod.getIstitutoDetenzione()!=null){
      lIstituto = lNotMod.getIstitutoDetenzione();
    }
    else if (lNotMod.getCSSA()!=null){
      lCSSA = lNotMod.getCSSA();
    }
    else if (   lNotMod.getUfficio()!=null 
             && (   lNotMod.getUfficio().getCodTipoUfficio().equals("TDS")
                 || lNotMod.getUfficio().getCodTipoUfficio().equals("TDSM")
                )
            )
    { 
      lUfficioTDS = lNotMod.getUfficio();
    }
    else if (   lNotMod.getUfficio()!=null 
             && (   lNotMod.getUfficio().getCodTipoUfficio().equals("UDS")
                 || lNotMod.getUfficio().getCodTipoUfficio().equals("UDSM")
                )
            )
    {
      lUfficioUDS = lNotMod.getUfficio();
    }
    else if (   lNotMod.getAutoritaEsterna()!=null 
             && lNotMod.getAvvIdAvvocatoFascicoloSiep()==null
            ) 
    {
      AutoritaEsternaModel lAutModel = lNotMod.getAutoritaEsterna();
      
      if (   lAutModel.getCodTipoAutorita().equals("36")
          || lAutModel.getCodTipoAutorita().equals("37")
          || lAutModel.getCodTipoAutorita().equals("57")
         )
      { // Ufficio recupero credito
        lUffRecCred = lAutModel;
      }
      else{
        lAutDiDest = lAutModel;
        lAutDiDestInd = lNotMod.getNote();
      }
      
    }
    
    count++;
  }
}
//20-06-2016 - MEV 26 STEP 2
else	// Se NON esiste Provvedimento e relative Notifiche, Precarico Istituto Detenzione da posizione_Giuridica_Cumulo
{
	if(datiFinaliAggregatoModel.getPosizioneGiuridicaCumulo()!=null)
	{
		if(datiFinaliAggregatoModel.getPosizioneGiuridicaCumulo().getIstitutoDetenzione()!=null && 
		   datiFinaliAggregatoModel.getPosizioneGiuridicaCumulo().getIstitutoDetenzione().getIdIstitutoDetenzione()!=null )
		{
			lIstituto = datiFinaliAggregatoModel.getPosizioneGiuridicaCumulo().getIstitutoDetenzione();
		}
	}
}
//20-06-2016 - END MEV 26 STEP 2

%>

<html>
<head>
  <title> [S.I.E.S.] - Dati Finali Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">
    //if(history.length>0)history.forward();
    
    var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
    
    function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna){    
      day=dataOdierna.substring(0,2);
      month=dataOdierna.substring(3,5);
      year=dataOdierna.substring(6,10);
      document.getElementsByName(campo_giorno).item(0).value = day;
      document.getElementsByName(campo_mese).item(0).value = month;
      document.getElementsByName(campo_anno).item(0).value = year;      
    }  
  
    function ListaComuni(a_formname,a_fieldname)     {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function ListaCSSA(a_formname,a_fieldname,a_field2)  {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
    
    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)  {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&LoadDescEstesa=SI", "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
    
    function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3) {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
    
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function pulisciIstituto (nomeCampoDesc, nomeCampoId){
      var campoDescr = document.getElementsByName(nomeCampoDesc)[0];
      var campoId    = document.getElementsByName(nomeCampoId)[0];
      campoDescr.value="";
      campoId.value="";
    } 
    
    function pulisciUEPE (nomeCampoDesc, nomeCampoId){
      var campoDescr = document.getElementsByName(nomeCampoDesc)[0];
      var campoId    = document.getElementsByName(nomeCampoId)[0];
      campoDescr.value="";
      campoId.value="";
      
      document.f.<%=MinorMask.ComboCSSAId%>.value = '-';
    } 
    
    function resetSede(campoSede){
        document.getElementsByName(campoSede)[0].value='';
    }
    
    function ChangeTipologia(){
      // Da implementare
    }
    
    function eseguiSubmit (azione) {
      var action = "";
      
      if (azione=='submit') {
        if (Verify()){
          document.f.Conferma.disabled=true;
          try {document.f.Annulla.disabled=true;}catch(err) {}
          document.f.submit();
        }
      }
      else if (azione=='annulla'){
        <% if (modalita.equals(ICostantiModuloCumulo.MODALITA_MODIFICA)) { %>
        if (confirm("Attenzione le eventuali modifiche ai dati non verranno salvate. Si vuole procedere?")){
          document.formAnnulla.submit();
        }
        <% } else  { %> 
        if (confirm("Attenzione i dati inseriti non verranno salvati. Si vuole procedere?")){
          document.formAnnulla.submit();
        }
        <% } %>
        else { 
          return;
        }
      }
    }
   

    function Verify()
    {
      // Data Emissione
      var data_to_verify =     document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
                          +'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
                          +'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (!ControllaData(data_to_verify)){
        alert('Data Emissione non corretta');
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }
      
      if( !CompareDate( data_to_verify, data_sistema) )
      {
        alert('La Data Emissione non può essere superiore alla data odierna!');
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }      
      
      // Data Trasmissione
      var data_to_verify =     document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value
                          +'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value
                          +'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;
      if (!ControllaData(data_to_verify)){
        alert('Data Trasmissione non corretta');      
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
        return false;
      }
      
      if( !CompareDate( data_to_verify, data_sistema) )
      {
        alert('La Data Trasmissione non può essere superiore alla data odierna!');
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
        return false;
      }      
      
      
      if (document.f.<%=ICostantiDatiFinaliCumulo.CAMPO_MOTIVO_PROVV%>.value=='-') {
        alert('Selezionare la Tipologia di provvedimento'); 
        document.f.<%=ICostantiDatiFinaliCumulo.CAMPO_MOTIVO_PROVV%>.focus();
        return false;
      }
      
      if (document.f.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value=='') {
        alert('Selezionare il Magistrato Firmatario'); 
        document.f.<%=ICostantiMagistrato.CAMPO_COGNOME%>.focus();
        return false;
      }
    
      //CSSA
       if (  document.f.<%=MinorMask.ComboCSSAId%>.value!='-' 
            && document.f.IndirizzoUEPE.value==''          
          )
      {
        alert('Indicare correttamente i dati del UEPE/USSM. Dati Incompleti'); 
        document.f.<%=MinorMask.ComboCSSAId%>.focus();
        return false;
      }     
    
      // TDS
      var sedeTDS = document.f.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.value;
      sedeTDS = $.trim(sedeTDS);
      if (  (document.f.<%=ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO%>_TDS.value=='-' && sedeTDS!='')
          ||(document.f.<%=ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO%>_TDS.value!='-' && sedeTDS=='')
         )
      {
        alert('Indicare correttamente i dati del Tribunale di Sorveglianza. Dati Incompleti'); 
        document.f.<%=ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO%>_TDS.focus();
        return false;
      }
     
      // MDS
      var sedeMDS = document.f.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.value;
      sedeMDS = $.trim(sedeMDS);
      if (  (document.f.<%=ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO%>_UDS.value=='-' && sedeMDS!='')
          ||(document.f.<%=ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO%>_UDS.value!='-' && sedeMDS=='')
         )
      {
        alert('Indicare correttamente i dati del Magistrato di Sorveglianza. Dati Incompleti'); 
        document.f.<%=ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO%>_UDS.focus();
        return false;
      }
      
      // Altra Autorità
      var sedeAut = document.f.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N%>.value;
      sedeAut = $.trim(sedeAut);
      var indirizzo = document.f.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_N%>.value;
      indirizzo = $.trim(indirizzo);
      if (   document.f.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N%>.value=='-' 
          && ( sedeAut!="" || indirizzo!="")
         ) 
      {
        alert("Indicare correttamente i dati dell'Autorità di Destinazione. Dati Incompleti sono stati indicati Sede e/o Indirizzo ma non la tipologia autorità"); 
        document.f.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N%>.focus();
        return false;
      }
      
      // Ufficio recupero crediti . Se indicato deve essere completo
      if (typeof(document.f.<%=ICostantiNotifica.CAMPO_COD_TIPO_UFF_REC_CREDITI%>)!="undefined") {
        var sedeURC = document.f.<%=ICostantiNotifica.CAMPO_SEDE_UFF_REC_CREDITI%>.value;
        sedeURC = $.trim(sedeURC);
        if (  (document.f.<%=ICostantiNotifica.CAMPO_COD_TIPO_UFF_REC_CREDITI%>.value=='-' && sedeURC!='')
            ||(document.f.<%=ICostantiNotifica.CAMPO_COD_TIPO_UFF_REC_CREDITI%>.value!='-' && sedeURC=='')
           )
        {
          alert("Indicare correttamente i dati dell'ufficio Recupero Crediti. Dati Incompleti"); 
          document.f.<%=ICostantiNotifica.CAMPO_COD_TIPO_UFF_REC_CREDITI%>.focus();
          return false;
        }
      }
      
      
      return true;
    }
        

    $(document).ready(function(){      
      var tipoCSSA = "-";
      <% if (lCSSA.getIdCSSA()!=null) { %>  
      tipoCSSA = "<%=lCSSA.getTipoDesc()%>";
      <% } else { %>
      tipoCSSA = "-";
      <% } %>
      
      if (tipoCSSA!='-'){
        $("#<%=MinorMask.ComboCSSAId%>").val(tipoCSSA);
      }
      
      $("#<%=MinorMask.ComboCSSAId%>").change(function(){
        //alert("onChange");
        // Ripulisco il campo
        document.f.IndirizzoUEPE.value="";
        document.f.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value="";
      });
      
    });
      
  </script>
  

  
  <jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dati Finali Cumulo - Emissione Provvedimento</font>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/NavigazioneDatiFinaliCumulo.jsp"/>
  <br>
  <%
  //============================================================================
  // Dettaglio riepilogativo dei dati del cumulo, eventualmente espandi/nascondi
  // - Pena Detentiva
  // - LA
  // - Benefici
  // 
  //============================================================================
  %>
  
<table cellspacing="2" cellpadding="2">
  <tr>
    <td class="L" >Posizione Giuridica</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lPosizioneGiuridicaCumulo.getDescrPosizioneGiuridica()) %>&nbsp;</font></td>
  </tr>
</table>

<table cellspacing="2" cellpadding="2">
  <tr>
    <td class="Titolo" colspan="9">Pena da eseguire</td>
  </tr>
  
  <%
  //============================================================================
  // Si visualizza la pena da eseguire. Se presente Ergastolo + Detentiva si 
  // visualizza solo l'ergastolo
  //============================================================================
  %>
  <% if (lPenaResiduaCumulo.isErgastolo()) {%>
  <tr>
    <td class="l"><font class="label">Pena Detentiva: </font></td>
    <td class="l" colspan="7">
      <font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getDescrTipoPenaDetentiva())%></font>
        <% if ("04".equals(lPenaResiduaCumulo.getCodTipoPenaDetentiva())) { %>
        Durata Isolamento Diurno: Anni <font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumAnniIsolamentoDiurno(),"-") %></font>
                                  Mesi <font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumMesiIsolamentoDiurno(),"-") %></font>
                                  Giorni <font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumGiorniIsolamentoDiurno(),"-") %></font>
        <% } %>   
    </td>
  </tr>
  <% } else { %>  
    <% if (lPenaResiduaCumulo.isReclusione() || lPenaResiduaCumulo.isMulta() ) {%>
    <tr>
      <td class="l"><font class="label">Reclusione: </font></td>
      <% if ( lPenaResiduaCumulo.isReclusione() ) {%>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumAnniReclusione())%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumMesiReclusione())%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumGiorniReclusione())%></font></td>
      <% } else { %>
      <td class="l" colspan="6"><font color="red">nulla residua da espiare</font></td>
      <% } %>
      <td class="l"><font class="label">Importo Multa</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(lPenaResiduaCumulo.getImportoMulta()) %></font></td>
    </tr>
    <% } %>
    <% if (lPenaResiduaCumulo.isArresto() || lPenaResiduaCumulo.isAmmenda() ) {%>
    <tr>
      <td class="l"><font class="label">Arresto:</font></td>
      <% if ( lPenaResiduaCumulo.isArresto() && !lPenaResiduaCumulo.isNegativeArresto() ) {%>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumAnniArresto())%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumMesiArresto())%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumGiorniArresto())%></font></td>
      <% } else { %>
      <td class="l" colspan="6"><font color="red">nulla residua da espiare</font></td>
      <% } %>
      <td class="l"><font class="label">Importo Ammenda</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(lPenaResiduaCumulo.getImportoAmmenda()) %></font></td>
    </tr>
    <% } %>
    <%--
    Se Arresto < 0 vuol dire c'è pena espiata in eccesso? 
    --%>
    <% if (lPenaResiduaCumulo.isNegativeArresto()) {%>
    <tr>
      <td class="l" >Pena  espiata in eccesso:</td>
      <td class="l" style="width:250px">
        <font class="l" color="red">Anni&nbsp;</font><font class="campo" style='color: red;'><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumAnniArresto())%>&nbsp;</font>
        <font class="l" color="red">Mesi&nbsp;</font><font class="campo" style='color: red;'><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumMesiArresto())%>&nbsp;</font>
        <font class="l" color="red">Giorni&nbsp;</font><font class="campo" style='color: red;'><%=StringUtils.toStringJSP(lPenaResiduaCumulo.getNumGiorniArresto())%>&nbsp;</font>
      </td>
    </tr>
  <% } %>
  <% } %>
</table>


  
<% if (lPenaRideterminataCumulo.isLibAnt()) { %>
<table cellspacing="2" cellpadding="2">
  <tr>
    <td class="Titolo" colspan="6" >
      Liberazione Anticipata Concessa da Detrarre dal Cumulo
    </td>
  </tr>
  <tr>
    <td></td>
    <td class="c" style="width:130px">Ordinaria</td>
    <td class="c" style="width:130px">Speciale</td>
    <td class="c" style="width:130px">Integrazione</td>
    <td class="c" style="width:130px">D.L.92/2014</td>
    <td class="c" style="width:130px">Scomputi</td>
  </tr>
  <tr>
    <td class="l">
      <font class="label">Totale Liberazione Anticipata (giorni):</font>
    </td>
    <td class="c">
      <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniLA()) %>&nbsp;</font>
    </td>
    <td class="c">
      <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniLS()) %>&nbsp;</font>
    </td>
    <td class="c">
      <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniLI()) %>&nbsp;</font>
    </td>
    <td class="c">
      <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniRiduzione()) %>&nbsp;</font>
    </td>
    <td class="c">
      <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniScomputo()) %>&nbsp;</font>
    </td>
  </tr>
</table>
<% } %>

<% if (lPenaResiduaCumulo.getDataInizio()!=null ) { %>
<table>
  <tr>
    <td class="l">Data Decorrenza Pena: </td>
    <td class="l">
      <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaResiduaCumulo.getDataInizio(),"dd-MM-yyyy"))%>
      </font>
    </td>
    <td class="l">Data Fine Pena: </td>
    <td class="l">
    <% if (lPenaResiduaCumulo.isErgastolo()) { %>
      <font class="cRosso">MAI</font>
    <% } else { %>
	<!-- MEV 16 CUMULO: aggiunto controllo preventivo -->
		<% if ((lPenaResiduaCumulo.getDataFinePresunta() != null && lPenaResiduaCumulo.getDataFine() != null) &&
				DateUtils.isEquals(lPenaResiduaCumulo.getDataFinePresunta(), lPenaResiduaCumulo.getDataFine())) { %>
      <font class="campo">
      <% } else { %>
      <font class="cRosso">
      <% } %>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaResiduaCumulo.getDataFine(),"dd-MM-yyyy"))%>
      </font>
    <% } %>
    </td>
  </tr>
</table>
<% } %>




<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formAnnulla">
  <%//FORM per richiamare i dettagli in POST ma evitare di inviare inutilmente i dati della FORM principale%>
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActLoadDettaglioProvvedimentoCumulo">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
</form>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciProvvedimentoCumulo">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA%>" value="<%=modalita%>">
  <input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(lEvento.getIdEvento())%>">
    
<table>
  <tr>
    <td class="l" width="25%">Data Emissione <font class=ob>(*)</font></td>
    <td class="L" >
      <input type="text" size="2" maxlength="2" 
             name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd")) %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input type="text" size="2" maxlength="2" 
             name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"MM")) %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input type="text" size="4" maxlength="4" 
             name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"yyyy")) %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      &nbsp;
      <a href="Javascript:impostaDataOdierna('<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>'
                                            ,'<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>'
                                            ,'<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>'
                                            ,'<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
        <img src="<%=IWebConstants.IMAGES_DIR%>Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna"></a>
    
    </td>
  
    <td class="l">Data Trasmissione <font class=ob>(*)</font></td>
    <td class="L">
      <input type="text" size="2" maxlength="2" 
             name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataTrasmissioneAtti(),"dd")) %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input type="text" size="2" maxlength="2" 
             name="<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataTrasmissioneAtti(),"MM")) %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input type="text" size="4" maxlength="4" 
             name="<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataTrasmissioneAtti(),"yyyy")) %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      &nbsp;
      <a href="Javascript:impostaDataOdierna('<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI %>'
                                            ,'<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>'
                                            ,'<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>'
                                            , '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
        <img src="<%=IWebConstants.IMAGES_DIR%>Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna"></a>
    </td>
  </tr>
</table>
    
<br>
<table width="90%">
  <tr>
    <td class="Titolo" colspan="2"> Provvedimento di esecuzione di pene concorrenti </td>
  </tr>
  <tr>
    <td class="l" style="width:10%">Tipologia <font class=ob>(*)</font></td>
    <td class="L" colspan="1" >
      <select class="small" name="<%=ICostantiDatiFinaliCumulo.CAMPO_MOTIVO_PROVV%>" onChange="ChangeTipologia();">
        <option value="-">-
        <%=motivoProvvedimento%>
      </select>
    </td>
  </tr>
</table>



<%
//==========================================================================
// Magistrato firmatario
//==========================================================================
%>
<table width= "90%">
  <tr>
    <td class="Titolo" colspan="2"> Magistrato Competente </td>
  </tr>
  <tr>
    <td class="l" style="width:200px">Magistrato Competente <font class=ob>(*)</font></td>
    <td class="L">
       <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistrato.getCodMagistrato() )%>"  name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"   >
       <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input readonly title= "Nome Magistrato"   
          value="<%=StringUtils.toStringJSP(magistrato.getNome() )%>" 
          type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25" >
        <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
          <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0></a>
    </td>
  </tr>
</table>

<table width= "90%">
  <tr>
    <td class="Titolo" colspan="5"> Destinatari </td>
  </tr>

  <tr id="idTrIstituto">
    <td class="l" width="30%">Istituto di Detenzione</td>
    <td class="l" colspan="3">
      <input readonly Title="Istituto" size="90" name="DescrIstituto" 
             <% if (!"".equals(lIstituto.getIdIstitutoDetenzione())) { %>
             value="<%=StringUtils.toStringJSP(lIstituto.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lIstituto.getDescrizione())%> - <%=StringUtils.toStringJSP(lIstituto.getIndirizzo())%>"
             <% } %>         
         >
      <input type="hidden"  Title="IdIstituto" 
             name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" 
             value="<%=StringUtils.toStringJSP(lIstituto.getIdIstitutoDetenzione())%>" >
      <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','DescrIstituto');">
        <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0></a>
      <a href="Javascript:pulisciIstituto('DescrIstituto','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
        <img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border=0></a>
    </td>
  </tr> 
    
  <tr id="idTrUEPE">
    <td class="l" width="30%">UEPE/USSM </td>
    <td class="l" colspan="3">
      <%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%>
      <input readonly Title="UEPE Competente" name="IndirizzoUEPE"  size="80"
             <% if (lCSSA.getIdCSSA()!=null) { %>
             value="<%=StringUtils.toStringJSP(lCSSA.getComune())%>-<%=StringUtils.toStringJSP(lCSSA.getIndirizzo())%>"
             <% } %> 
      >
      <input type="hidden" Title="UEPE Competente" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" 
             value="<%=StringUtils.toStringJSP(lCSSA.getIdCSSA())%>" >
             
      <a href="Javascript:ListaCSSAMinor('f','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','IndirizzoUEPE');">
        <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border="0"></a>
      <a href="Javascript:pulisciUEPE('IndirizzoUEPE','<%=ICostantiCSSA.CAMPO_ID_CSSA%>');">
        <img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border="0"></a>

    </td>
  </tr>
  
  <tr id="idTrTDS">
    <td class="L" width="30%">Tribunale di Sorveglianza</td>
    <td class="L" colspan="1">
      <select  Title="Trubunale di Sorveglianza" name="<%=ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO%>_TDS"  onchange="resetSede('<%=ICostantiNotifica.CAMPO_SEDE_TDS %>')">
        <option value="-"/>-
        <%=comboTDS%>
      </select>      
    </td>
    <td class="L" colspan="2">&nbsp;di&nbsp;
      <input type="text" title="Sede Tribunale Sorveglianza" maxlength="35" size="35"
             value="<%=StringUtils.toStringJSP(lUfficioTDS.getDescrComune())%>"  
             name="<%= ICostantiNotifica.CAMPO_SEDE_TDS %>"  >
      <a href="Javascript:ListaUfficiPerTipo('f','<%= ICostantiNotifica.CAMPO_SEDE_TDS%>'
                                                ,document.f.<%=ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO%>_TDS.value );">
        <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  
  <tr id="idTrMDS">
    <td class="l">Magistrato di Sorveglianza </td>
    <td class="L" colspan="1">
      <select  Title="Ufficio di Sorveglianza" name="<%=ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO%>_UDS"  onchange="resetSede('<%=ICostantiNotifica.CAMPO_SEDE_MDS%>')">
        <option value="-"/>-
        <%=comboUDS%>
      </select>      
    </td>
    <td class="L" colspan="2">&nbsp;di&nbsp;
      <input type="text" title="ufficio" maxlength="35" size="35"
             value="<%=StringUtils.toStringJSP(lUfficioUDS.getDescrComune())%>"  
             name="<%=ICostantiNotifica.CAMPO_SEDE_MDS%>" >
      <a href="Javascript:ListaUfficiPerTipo('f','<%= ICostantiNotifica.CAMPO_SEDE_MDS%>'
                                                ,document.f.<%=ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO%>_UDS.value );">
        <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  
  <tr>
    <td class="l">Autorità di Destinazione </td>
    <td class="L">
      <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N%>">
        <%=comboAutoritaN%>
      </select>
    </td>
    <td class="L" rowspan="2">Indirizzo</td>
    <td class="L" rowspan="2">
      <textarea cols="50" rows="3" title="Indirizzo" 
                name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_N%>"><%=StringUtils.toStringJSP(lAutDiDestInd)%></textarea>
    </td>    
  </tr>
  
  <tr>
    <td class="l">Sede</td>
    <td class="L">
      <input type="text" title="Sede Autorita Esterna" maxlength="35" size="35"
             name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N%>" 
             value="<%=StringUtils.toStringJSP(lAutDiDest.getDescrSede())%>">
      <a href="Javascript:ListaComuni('f','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N%>');">
        <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  
  <% if (lPenaResiduaCumulo.isMulta() || lPenaResiduaCumulo.isAmmenda() ) {%>
  <tr>
    <td class="l">Ufficio Recupero Crediti</td>
    <td class="L">
      <select Title="Ufficio Recupero Crediti" 
              name="<%=ICostantiNotifica.CAMPO_COD_TIPO_UFF_REC_CREDITI%>">
        <%=comboUffRecuperoCrediti %>
      </select>
    </td>
    <td class="L" colspan="2">&nbsp;di&nbsp;
      <input type="text" title="ufficio" maxlength="35" size="35"
             value="<%=StringUtils.toStringJSP(lSedeUffRecCred)%>"  
             name="<%=ICostantiNotifica.CAMPO_SEDE_UFF_REC_CREDITI%>" >
      <a href="Javascript:ListaComuni('f','<%=ICostantiNotifica.CAMPO_SEDE_UFF_REC_CREDITI%>');">
        <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border="0">
      </a>
    </td>    
  </tr>
  <% } %>
  
</table>




<table width="90%">
  <tr>
    <td class="Titolo" colspan=6>Destinatario per Notifica </td></tr>
    <%
    int lIdxAvv = 0;
    Iterator lItxAvv = avvocati.iterator();
    while(lItxAvv.hasNext())
    { 
      AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
    %>
      <table width="90%">
        <tr>
          <td class="l">Per Avvocato&nbsp;
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
          </td>
          <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
        </tr>
      </table>
<%
    lIdxAvv++;
 }
%>
</table>

<br>

  <table>
    <tr>
      <td class="lNoBord" colspan="2">
        <input type="button" class="bottone" name="Conferma" value="Conferma" onClick="eseguiSubmit('submit')">
        <% if (modalita.equals(ICostantiModuloCumulo.MODALITA_MODIFICA)) { %>
        <input type="button" class="bottone" name="Annulla" value="Annulla" onClick="eseguiSubmit('annulla')">
        <% } %>
      </td>
    </tr>
  </table>
   
  </form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
</body>

</html>
    
    
    
    
    
    
    
    