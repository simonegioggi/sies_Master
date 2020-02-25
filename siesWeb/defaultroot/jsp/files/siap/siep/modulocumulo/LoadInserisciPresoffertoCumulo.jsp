<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraCautelareCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
 
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"%>



<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoMisuraDetentive"    scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraNonDetentive" scope="request" class="java.lang.String"/>

<jsp:useBean id="aProvvedimento" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aIdComputo" scope="request" class="java.lang.String"/>

<% 
//============================================================================== 
// Form per l'inserimento e la modifica dei Presofferti
//============================================================================== 


ComputiCumuloModel aComputo = new ComputiCumuloModel();
//if ( aProvvedimento.getListaComputi()!=null && aProvvedimento.getListaComputi().size()>0)
if ( modalita.equals("M") )
{
  Vector <ComputiCumuloModel> lListaComputi = aProvvedimento.getListaComputi();
  Iterator itxComputi = lListaComputi.iterator();
  while ( itxComputi.hasNext()) 
  {
    ComputiCumuloModel lComputo = (ComputiCumuloModel) itxComputi.next();
    BigDecimal lIdCompDaModificare = new BigDecimal (aIdComputo);
    if (lComputo.getIdComputiCumulo().compareTo(lIdCompDaModificare)==0){
      aComputo = lComputo;
      break;
    }
  }
}

IstitutoDetenzioneModel IstitutoDetenzione = new IstitutoDetenzioneModel();
if (aComputo!=null && aComputo.getIstitutoDetenzione()!=null)
  IstitutoDetenzione = aComputo.getIstitutoDetenzione();
  
String TipoEspiazione = "";
String lAltroLuogo = "";
if (aComputo!=null){
  TipoEspiazione = aComputo.getTipoEspiazioneMC();
  lAltroLuogo = aComputo.getAltroLuogoDetenzione();
}
  
%> 

<html>
<head>
  <title> Gestione Presofferto Cumulo </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="/html/jsrsClient.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>  
    
  <script language="JavaScript" >
    var anniQuantumCalcolati = 0;
    var mesiQuantumCalcolati = 0;
    var giorniQuantumCalcolati = 0;
    var totGGMessaAllaProva = 0;
    
    var testOnLoad = 'N';
  
    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
    {
      var desktop = window.open("/jsp/Main.jsp?Action=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&LoadDescEstesa=SI", "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
    
    function pulisciIstituto (nomeCampoComune, nomeCampoId){
      var campoDescr = document.getElementsByName(nomeCampoComune)[0];
      var campoId    = document.getElementsByName(nomeCampoId)[0];
      campoDescr.value="";
      campoId.value="";
    }
    
    
    //==========================================================================
    // Ritorna alla lista dei presofferti
    //==========================================================================
    function eseguiFunzione(action)
    {
      document.LoadInserisciPresoffertoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.LoadInserisciPresoffertoCumulo.submit();
    }
    
    
    //==========================================================================
    // 
    //==========================================================================
    function clearQuantum(){
      $('#anniPresofferto').val('');
      $('#mesiPresofferto').val('');
      $('#giorniPresofferto').val('');
      $('#totaleGiorniPre').val('');

      anniQuantumCalcolati = 0;
      mesiQuantumCalcolati = 0;
      giorniQuantumCalcolati = 0;
      totGGMessaAllaProva = 0;
    }
    
    //==========================================================================
    // 
    //==========================================================================
    /*
    function testCalcolaPresofferto(){
      alert("testCalcolaPresofferto");
      if (controllaPeriodi('noreply')==false){
        clearQuantum();
      }
      else {
        callCalcolaQuantum();
      }
    }
    */
        
    //==========================================================================    
    // Verifica la congruenza dei periodi di Presofferto (data dal <= data al)
    //==========================================================================    
    function controllaPeriodi(reply)
    {
      //alert("testCalcolaPresofferto("+reply+")");
      var sysDate = "<%=DateUtils.getSysDate("dd/MM/yyyy")%>";
    
      var gg_dal = document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA%>;
      var mm_dal = document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA%>;
      var aa_dal = document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA%>;

      var gg_al = document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A%>;
      var mm_al = document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A%>;
      var aa_al = document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A%>;
      
      
      if (gg_dal.value.length<2 && gg_dal.value.length!=0)
        gg_dal.value="0"+gg_dal.value;
      if (mm_dal.value.length<2 && mm_dal.value.length!=0)
        mm_dal.value="0"+mm_dal.value;
      if (aa_dal.value.length==2) {
        if (aa_dal.value>50)
          aa_dal.value = '19'+aa_dal.value;
        else 
          aa_dal.value = '20'+aa_dal.value;
      }        


      var dataDAL = gg_dal.value +"/"+mm_dal.value+"/"+aa_dal.value;

      if (! ControllaData(dataDAL))
      {
        if (reply=='noreply') {
          return false
        }
        else {
          alert('Data di Inizio Periodo non valida');
          gg_dal.focus();
          return false;
        }
      }

      if (!CompareDate(dataDAL,sysDate)){
        if (reply=='noreply') {
          return false
        }
        else {
          alert('La Data di Inizio Periodo non può essere una data futura');
          gg_dal.focus();
          return false;
        }
      }
      
      if (aa_dal.value<1900){
        if (reply=='noreply') {
          return false
        }
        else {
          alert('La Data di Inizio Periodo deve essere maggiore del 1900');
          aa_dal.focus();
          return false;
        }
      }  

      if (gg_al.value.length<2 && gg_al.value.length!=0)
        gg_al.value="0"+gg_al.value;
      if (mm_al.value.length<2 && mm_al.value.length!=0)
        mm_al.value="0"+mm_al.value;
      if (aa_al.value.length==2) {
        if (aa_al.value>50)
          aa_al.value = '19'+aa_al.value;
        else 
          aa_al.value = '20'+aa_al.value;
      }

      // Data Fine obbligatoria
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;
      if (dataAL=="//") {
        if (reply=='noreply') {
          return false
        }
        else {
          alert('Specificare la Data di Fine Periodo');
          gg_al.focus();
          return false;
        }
      }
      
      if (! ControllaData(dataAL))
      {
        if (reply=='noreply') {
          return false
        }
        else {
          alert('Data di Fine Periodo non valida');
          gg_al.focus();
          return false;
        }
      }
      
      if (!CompareDate(dataAL,sysDate)){
        if (reply=='noreply') {
          return false
        }
        else {
          alert('La Data di Fine Periodo non può essere una data futura');
          gg_dal.focus();
          return false;
        }
      }
      
      if (aa_al.value<1900){
        if (reply=='noreply') {
          return false
        }
        else {
          alert('La Data di Fine Periodo deve essere maggiore del 1900');
          aa_al.focus();
          return false;
        } 
      } 
      
      if(!CompareDate(dataDAL, dataAL))
      {
        if (reply=='noreply') {
          return false
        }
        else {
          alert('La Data di Fine Periodo non può essere precedente a quella di Inizio');
          gg_dal.focus();
          return false;
        }
      }      
           
      return true;
    }
    
    //==========================================================================
    // Affettua la chiamata sincrona alla servlet di calcolo quantum
    //==========================================================================
    function callCalcolaQuantum () {
      //alert("callCalcolaQuantum: ");
      
      var gg_dal = document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA%>;
      var mm_dal = document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA%>;
      var aa_dal = document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA%>;

      var gg_al = document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A%>;
      var mm_al = document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A%>;
      var aa_al = document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A%>;
      
      var dataDAL = gg_dal.value +"/"+mm_dal.value+"/"+aa_dal.value;
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;
      
      // aggiungo il parametro TipoMisuraCautelare (Cod)
      var tipoEspiazione = $("[name=<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE%>]:checked").val();
      var ObjComboTipoMisura;
      if (tipoEspiazione=='<%=ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO%>'){
        ObjComboTipoMisura = $("#divComboDetentive [name=<%= ICostantiComputiCumulo.CAMPO_COD_TIPO_MISURA %>]");
      }
      else {
        ObjComboTipoMisura = $("#divComboNonDetentive [name=<%= ICostantiComputiCumulo.CAMPO_COD_TIPO_MISURA %>]");
      }
      
      var tMis = ObjComboTipoMisura.val();
      
      // Prepara l'array dei dati da passare 
      var myParams = new Array(gg_dal.value,
                               mm_dal.value,
                               aa_dal.value,
                               gg_al.value,
                               mm_al.value,
                               aa_al.value,
                               tMis
                              );

      document.body.style.cursor='wait';
      //alert('Chiamata');
      // jsrsExecute(<nome servlet>,<funzione js da invocare al ritorno>, <nome del metodo server da invocare>, <parametro da passare al server o array di parametri>
      jsrsExecute("/CaricaHTML_Servlet", caricaQuantum, "getQuantumIntervallo",myParams);      

    }
    
    //==========================================================================
    // Funzione di callback richiamata dopo il calcolo del quantum
    //==========================================================================
    function caricaQuantum(valueTextStr){ 
      document.body.style.cursor='auto';
      //alert("return "+valueTextStr);
      
      var sep = "~#";
      var aPairs = valueTextStr.split(sep);
      
      anni = aPairs[0];
      mesi = aPairs[1];
      giorni = aPairs[2];
      
      totGG= aPairs[3];
      
      // Valorizzo le var nascoste dei quantum calcolati
      anniQuantumCalcolati   = anni;
      mesiQuantumCalcolati   = mesi;
      giorniQuantumCalcolati = giorni;
      totGGMessaAllaProva    = totGG;
      
      if (testOnLoad!='S') {
        $("#anniPresofferto").val(anni);
        $("#mesiPresofferto").val(mesi);
        $("#giorniPresofferto").val(giorni);
        $("#totaleGiorniPre").val(totGG);
      }
      else {
        testOnLoad = 'N';
      }
      
      testChgQuantum($("#anniPresofferto"));
      testChgQuantum($("#mesiPresofferto"));
      testChgQuantum($("#giorniPresofferto"));
      testChgQuantum($("#totaleGiorniPre"));

    } 

    function testChgQuantum (obj) {
      var valChanged = false;
      var currVal = $(obj).val();
      var computedVal = 0;
      
      if (  $(obj).attr('id')=='anniPresofferto' )
        computedVal = anniQuantumCalcolati;
      else if (  $(obj).attr('id')=='mesiPresofferto' )
        computedVal = mesiQuantumCalcolati;
      else if (  $(obj).attr('id')=='giorniPresofferto' )
        computedVal = giorniQuantumCalcolati;
      else if (  $(obj).attr('id')=='totaleGiorniPre' )
        computedVal = totGGMessaAllaProva;
      

      if (currVal!=computedVal)
        $(obj).css("color", "red");
      else 
        $(obj).css("color", "blue");

    }


    
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
    function Verify() { 
      
      var gg_emi = document.LoadInserisciPresoffertoCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>;
      var mm_emi = document.LoadInserisciPresoffertoCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>;
      var aa_emi = document.LoadInserisciPresoffertoCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>;

      var dataEmissione = gg_emi.value +"/"+mm_emi.value+"/"+aa_emi.value;
      
      if (! ControllaData(dataEmissione))
      {
        alert ("Data Emissione Provvedimento non valida");
        
        document.LoadInserisciPresoffertoCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }
      
      if (dataEmissione!="//"){
        var sysDate = "<%=DateUtils.getSysDate("dd/MM/yyyy")%>";

        if (!CompareDate(dataEmissione,sysDate)){
            alert('La Data Emissione non può essere una data futura');
            document.LoadInserisciPresoffertoCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
            return false;
        }
      }
      
      
      // N.b. Trattasi di dati estratti o comunque provenienti da altri fascicoli
      //      non si impone 'obbligatorietà se non sulla presenza e congruenza
      //      dei periodi. Alcuni dati comunque se inseriti devono essere completi
      var tipoEspiazione = $("[name=<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE%>]:checked").val();
      
      var ObjComboTipoMisura;
      if (tipoEspiazione=='<%=ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO%>'){
        ObjComboTipoMisura = $("#divComboDetentive [name=<%= ICostantiMisuraCautelareCumulo.CAMPO_COD_TIPO_MISURA %>]");
      }
      else {
        ObjComboTipoMisura = $("#divComboNonDetentive [name=<%= ICostantiMisuraCautelareCumulo.CAMPO_COD_TIPO_MISURA %>]");
      }
      
      /*
      if (ObjComboTipoMisura.val()=='-'){
        alert('Selezionare la natura della misura');
        ObjComboTipoMisura.focus();
        return false;
      }
      */
      var tipoMisura = ObjComboTipoMisura.val();  
      
            // Periodi dal al obbligatori. Data al>dal. Date < data di sistema
      if (!controllaPeriodi())
        return false;

      // Se messa alla prova i quantum e gg sono editabili. Vanno verificati  
      if (ObjComboTipoMisura.val()=='CL'){
        var totQuantum = 0;
        totQuantum += $("#anniPresofferto").val();
        totQuantum += $("#mesiPresofferto").val();
        totQuantum += $("#giorniPresofferto").val();
        if (totQuantum==0)
          if ( !window.confirm("Non sono stati specificati i quantum equivalenti per la Messa Alla Prova. Si vuole procedere comunque?") )
            return false;
        var totGiorni = 0;
        totGiorni += $("#totaleGiorniPre").val();
        if (totGiorni==0)
          if ( !window.confirm("Non sono stati specificati i giorni equivalenti per la Messa Alla Prova. Si vuole procedere comunque?") )
            return false;
      }
      
      return true; 
    } 
    
    function radioTipoEspiazione(par)
    {
      var nomeRadio = '<%= ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE %>';
      
      //alert("radioTipoEspiazione");
      //$("input[name=tipo]:radio").
      //alert ("value = " + $("form [type=radio][name="+nomeRadio+"]:checked").val());
      
      if ( $("form [type=radio][name="+nomeRadio+"]:checked").val()=="<%= ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO %>") {
        $('#divComboDetentive').show();
        $('#divComboDetentive select').prop('disabled',false);

        $('#divDatiDetentive').show();        
        $('#divDatiDetentive input').prop('disabled',false);
        $('#divDatiDetentive select').prop('disabled',false);
        $('#divDatiDetentive textarea').prop('disabled',false);
        
        $('#divComboNonDetentive').hide();
        $('#divComboNonDetentive select').prop('disabled',true);
        $('#divDatiNonDetentive').hide();
        $('#divDatiNonDetentive input').prop('disabled',true);
        $('#divDatiNonDetentive select').prop('disabled',true);
        $('#divDatiNonDetentive textarea').prop('disabled',true);
        
      }
      else {
        $('#divComboDetentive').hide();
        $('#divComboDetentive select').prop('disabled',true);
        
        $('#divDatiDetentive').hide();
        $('#divDatiDetentive input').prop('disabled',true);
        $('#divDatiDetentive select').prop('disabled',true);
        $('#divDatiDetentive textarea').prop('disabled',true);
        
        $('#divComboNonDetentive').show();
        $('#divComboNonDetentive select').prop('disabled',false);

        $('#divDatiNonDetentive').show();
        $('#divDatiNonDetentive input').prop('disabled',false);
        $('#divDatiNonDetentive select').prop('disabled',false);
        $('#divDatiNonDetentive textarea').prop('disabled',false);
      }
      
      bloccaQuantum(par);
    }    
    
    // 
    function bloccaQuantum(par)
    {
      var tipoEspiazione = $("[name=<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE%>]:checked").val();
      var ObjComboTipoMisura;
      if (tipoEspiazione=='<%=ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO%>'){
        ObjComboTipoMisura = $("#divComboDetentive [name=<%= ICostantiComputiCumulo.CAMPO_COD_TIPO_MISURA %>]");
      }
      else {
        ObjComboTipoMisura = $("#divComboNonDetentive [name=<%= ICostantiComputiCumulo.CAMPO_COD_TIPO_MISURA %>]");
      }
  
      //var nodeQuantum = document.getElementById('divquantum');
  
      if(ObjComboTipoMisura.val()=='CL') // Messa alla prova
      {
        document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.disabled=false;
        document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.disabled=false;
        document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.disabled=false;
        
        $("#tdGiorni").show();
        document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP%>.disabled=false;
      } 
      else
      {
        document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.disabled=true;
        document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.disabled=true;
        document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.disabled=true;
        
        $("#tdGiorni").hide();
        document.LoadInserisciPresoffertoCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP%>.disabled=true;
      }
      
      if (par!='onLoad') // non effettuo i calcoli quantum se sono chiamato dall'onLoad
        testCalcolaPresofferto();
    }    
    
    function testCalcolaPresofferto(par){
      //alert("testCalcolaPresofferto("+par+")");
      var reply = 'noreply';
      if (par=='X')
        reply = 'reply';
        
      if (controllaPeriodi(reply)==false){
        clearQuantum();
      }
      else {
        callCalcolaQuantum();
      }
    }
    
    
    // On load
    $(document).ready(function(){
      radioTipoEspiazione("onLoad");
    });

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
        <% if( modalita.equals("I") ) { %>
        <font class="campo">Inserimento Presofferto &nbsp;</font>
        <% } else if( modalita.equals("M") || modalita.equals("NP") ) { %>
        <font class="campo">Modifica Presofferto &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaPresoffertoCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciPresoffertoCumulo">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciPresoffertoCumulo">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="modalita" value="<%=modalita%>">
  
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="hidden" name="<%= ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>"                       value="<%=StringUtils.toStringJSP(aComputo.getIdComputiCumulo()) %>">
  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>" 					  value="<%=StringUtils.toStringJSP(aProvvedimento.getFlagStato()) %>">

  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_EVENTO_ORIGINE %>"      value="<%=aProvvedimento.getIdEventoOrigine() %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_EVE_ID_EVENTO_ORIGINE %>"  value="<%=aProvvedimento.getEveIdEventoOrigine() %>">

<% if ("M".equals(modalita) || "NP".equals(modalita)) {%>
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE %>"  value="<%=aProvvedimento.getCodUfficioEmittente() %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>"    value="<%=aProvvedimento.getCodLuogoEmittente() %>">
<% } %>


<table cellspacing="2" cellpadding="2" width="95%" align="center">
  <tr>
    <td class="titolo" colspan="100%">Provvedimento di computo</td>
  </tr>  
  <tr>
    <td class="l" width="150px">Tipo Provvedimento <font class="ob">(*)</font></td>
    <td class="l">
      <select Title="Tipo Misura Cautelare" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO %>" >
        <option value = "0121"  />computo Misura Cautelare stesso Reato art. 657 c.p.p.
      </select>
    </td>
    <td class="l">          
      &nbsp;&nbsp;<font class="label">emesso in data <font class="ob">(*)</font></font>
      &nbsp;&nbsp;
      <input type="text"  maxlength="2" size="2"
             name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE %>"  
             value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"dd"))%>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" maxlength="2" size="2"
             name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE %>"  
             value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"MM"))%>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" maxlength="4" size="4" 
             name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE %>" 
             value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"yyyy"))%>"  
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>    
  </tr>  
</table>                 

<table cellspacing="2" cellpadding="2" width="95%" align="center">
  <tr>
    <td class="titolo" colspan="100%">Dati identificativi della misura cautelare da computare</td>
  </tr>  

  <tr>
    <td class="L">
     Espiazione pena in istituto di detenzione &nbsp;
     <input type="radio" value="<%=ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO%>" 
            name="<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE%>"  
            <%=(TipoEspiazione.equals("") || TipoEspiazione.equals(ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO) )?"checked":"" %>
            onClick="radioTipoEspiazione();">
     Espiazione pena in altro luogo &nbsp;
     <input type="radio" value="<%=ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ALTRO%>" 
            name="<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE%>" 
            <%=(TipoEspiazione.equals(ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ALTRO) )?"checked":"" %>
            onClick="radioTipoEspiazione();">                
    </td>
  </tr>
</table> 
  
<div id="divComboDetentive" style="display:block;">
  <table width="95%" align="center">
    <tr>
      <td class="l" width="150px">Natura Misura</td>
      <td class="l">
        <select Title="Tipo Misura Cautelare" name="<%= ICostantiComputiCumulo.CAMPO_COD_TIPO_MISURA %>" onChange="Javascript:bloccaQuantum();">
        <%=tipoMisuraDetentive%>
        </select>
      </td>
    </tr>
  </table>
</div>
<div id="divComboNonDetentive" style="display:block;">
  <table width="95%" align="center">
    <tr>
      <td class="l" width="150px">Natura Misura</td>
      <td class="l" colspan="2">
        <select Title="Tipo Misura Cautelare" name="<%= ICostantiComputiCumulo.CAMPO_COD_TIPO_MISURA %>" onChange="Javascript:bloccaQuantum();">
        <%=tipoMisuraNonDetentive%>
        </select>
      </td>
    </tr>
  </table>
</div>
  
  
  
  <div id="divPeriodi" style="display:block;">
    <table width="95%" align="center">
      <tr>
        <td class="l" colspan="1" nowrap>Periodo sofferto&nbsp;<font class="ob">(*)</font></td>
        <td class="l" colspan="1" width="170px" nowrap>
          <font class="label">Dal&nbsp;</font>
                                 
          <input type="text" maxlength="2" size="2" id="GG_DAL_1"
                 name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"dd"))%>" 
                 <%=IWebConstants.UTIL_DATA%>
                 onChange="Javascript:testCalcolaPresofferto()">
          <input type="text" maxlength="2" size="2" id="MM_DAL_1"
                 name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"MM"))%>" 
                 <%=IWebConstants.UTIL_DATA%>
                 onChange="Javascript:testCalcolaPresofferto()">
          <input type="text"  maxlength="4" size="4"  id="AA_DAL_1" 
                 name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"yyyy"))%>" 
                 <%=IWebConstants.UTIL_DATA_ANNO%>
                 onChange="Javascript:testCalcolaPresofferto()">
        </td>
        <td class="l" colspan="1" width="160px" nowrap>
          <font  class="label">Al&nbsp;</font>
                                 
          <input type="text" maxlength="2" size="2" id="GG_AL_1"
                 name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"dd"))%>" 
                 <%=IWebConstants.UTIL_DATA%>
                 onChange="Javascript:testCalcolaPresofferto()">
          <input type="text" maxlength="2" size="2" id="MM_AL_1" 
                 name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"MM"))%>" 
                 <%=IWebConstants.UTIL_DATA%>
                 onChange="Javascript:testCalcolaPresofferto()">
          <input type="text" maxlength="4" size="4" id="AA_AL_1"
                 name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"yyyy"))%>" 
                 <%=IWebConstants.UTIL_DATA_ANNO%>
                 onChange="Javascript:testCalcolaPresofferto()">
        </td>       

        <td class="l" nowrap id="tdQuantum" width="75px" nowrap>&nbsp;
          <font class="label">Pari a&nbsp;</font><a href="Javascript:testCalcolaPresofferto('X');"><img src="<%=IWebConstants.IMAGES_DIR%>freccia_verde.gif" alt="Calcola Quantum" border=0></a>:
        </td>   
        
        <td class="c">
          <table>
            <tr>
              <td id="tdGiorni" nowrap>
                <font class="label">Tot Giorni</font>&nbsp;
                <input type="text" name="<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP%>" maxlength="4" size="4" id="totaleGiorniPre" 
                       value="<%=StringUtils.toStringJSP(aComputo.getNumGiorniMap()) %>"
                       onkeypress="return TicTabNumField(this,event)"
                       onChange="Javascript:testChgQuantum(this)">&nbsp;
                <font class="label">equiparati a:</font>&nbsp;     
              </td>
                              
              <td nowrap>
                <font class="label">Anni</font>&nbsp;
                <input type="text" name="<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>" maxlength="2" size="2" id="anniPresofferto"  
                       value="<%=StringUtils.toStringJSP (aComputo.getNumAnniReclusione()) %>"
                       onkeypress="return TicTabNumField(this,event)"
                       onChange="Javascript:testChgQuantum(this)">&nbsp;&nbsp;
                <font class="label">Mesi</font>&nbsp;
                <input type="text" name="<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>" maxlength="2" size="2" id="mesiPresofferto" 
                       value="<%=StringUtils.toStringJSP (aComputo.getNumMesiReclusione()) %>"
                       onkeypress="return TicTabNumField(this,event)"
                       onChange="Javascript:testChgQuantum(this)">&nbsp;&nbsp;
                <font class="label">Giorni</font>&nbsp;
                <input type="text" name="<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>" maxlength="2" size="2" id="giorniPresofferto" 
                       value="<%=StringUtils.toStringJSP (aComputo.getNumGiorniReclusione()) %>"
                       onkeypress="return TicTabNumField(this,event)"
                       onChange="Javascript:testChgQuantum(this)">&nbsp;&nbsp;&nbsp;&nbsp;
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </div>  
  
  
  <div id="divDatiDetentive" style="display:block;">
    <table width="95%" align="center">
      <tr>
        <td class="l" >Istituto</td>
        <td class="l">
          <%
          String lDescrIstituto = "";
          if (IstitutoDetenzione.getIdIstitutoDetenzione()!=null && !"".equals(IstitutoDetenzione.getIdIstitutoDetenzione())  ){
           lDescrIstituto = IstitutoDetenzione.getDescrizioneIstitutoPerVisualizzazione();          
          }
          
          %>
          <input readonly Title="Istituto" name="DescrizioneIstituto" size="90"
                 value="<%=StringUtils.toStringJSP (lDescrIstituto)%>" >
          <input type="hidden"  Title="Istituto" 
                 name="<%=ICostantiComputiCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>"
                 value="" 
                 >
          <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciPresoffertoCumulo','<%=ICostantiComputiCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','DescrizioneIstituto');">
          <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0></a>
          <a href="Javascript:pulisciIstituto('DescrizioneIstituto','<%=ICostantiComputiCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>');"><img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border=0></a>
        </td>
      </tr>
    </table>
  </div>    
  
  
  <div id="divDatiNonDetentive" style="display:block;">
    <table width="95%" align="center">
      <tr>
        <td class="l" >Luogo di espiazione </td>
        <td class="l" colspan="3">
          <textarea rows="3" cols="80" 
                    name="<%=ICostantiComputiCumulo.CAMPO_ALTRO_LUOGO_DETENZIONE %>"
          ><%=StringUtils.toStringJSP (lAltroLuogo)%></textarea>
        </td>
      </tr>
    </table>
  </div>       
    

  
<%  
//==============================================================================
//==============================================================================
%>
  <table cellspacing="2" cellpadding="2" width="95%" align="center">  
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</form>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciPresoffertoCumulo");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>