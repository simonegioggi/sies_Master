<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="f3b.log.LogF3B"%>
<%@ page import="org.apache.log4j.Logger"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiDatiFinaliCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaRideterminataCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiDatiFinaliUlterioriSanzioni"%>


<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaRideterminataCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.DatiFinaliUlterioriSanzioniModel"%>

<jsp:useBean id="IstruttoriaCumulo"        scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="datiFinaliAggregatoModel" scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="comboLPU" scope="request" class="java.lang.String"/>


<%
//==============================================================================
//     FORM di inserimento Delle pene rideterminate in cumulo
// - Pena Detentiva
// - Sanzioni Sostitutive
// - Liberazione Anticipata  e DL92
// - Pena da Conversione Pena Pecuniaria
// - Sanzioni del Giudice di pace
//==============================================================================
DatiFinaliCumuloModel lDatiFinaliCumulo = datiFinaliAggregatoModel.getDatiFinaliCumulo();
Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

PenaRideterminataCumuloModel lPenaRideterminataCumulo = new PenaRideterminataCumuloModel(); 
if (datiFinaliAggregatoModel.getPenaRideterminataCumulo()!=null){
  lPenaRideterminataCumulo = datiFinaliAggregatoModel.getPenaRideterminataCumulo();

  //siesLogger.debug("lPenaRideterminataCumulo = "+lPenaRideterminataCumulo);
  //siesLogger.debug("isLibAnt() = "+lPenaRideterminataCumulo.isLibAnt());

}


// Sanzioni Sostitutive
DatiFinaliUlterioriSanzioniModel lUltSan_SanSos_SEM     = datiFinaliAggregatoModel.getUltSanSanSosSemidetenzione();
DatiFinaliUlterioriSanzioniModel lUltSan_SanSos_LIBCTRL = datiFinaliAggregatoModel.getUltSanSanSosLibertContrl();
DatiFinaliUlterioriSanzioniModel lUltSan_SanSos_PPM     = datiFinaliAggregatoModel.getUltSanSanSosPPMulta();
DatiFinaliUlterioriSanzioniModel lUltSan_SanSos_PPA     = datiFinaliAggregatoModel.getUltSanSanSosPPAmmenda();
DatiFinaliUlterioriSanzioniModel lUltSan_SanSos_ESP     = datiFinaliAggregatoModel.getUltSanSanSosEspulsione();
DatiFinaliUlterioriSanzioniModel lUltSan_SanSos_LPU     = datiFinaliAggregatoModel.getUltSanSanSosLPU();


if (lUltSan_SanSos_SEM==null)     lUltSan_SanSos_SEM     = new DatiFinaliUlterioriSanzioniModel();
if (lUltSan_SanSos_LIBCTRL==null) lUltSan_SanSos_LIBCTRL = new DatiFinaliUlterioriSanzioniModel();
if (lUltSan_SanSos_PPM==null)     lUltSan_SanSos_PPM     = new DatiFinaliUlterioriSanzioniModel();
if (lUltSan_SanSos_PPA==null)     lUltSan_SanSos_PPA     = new DatiFinaliUlterioriSanzioniModel();
if (lUltSan_SanSos_ESP==null)     lUltSan_SanSos_ESP     = new DatiFinaliUlterioriSanzioniModel();
if (lUltSan_SanSos_LPU==null)     lUltSan_SanSos_LPU     = new DatiFinaliUlterioriSanzioniModel();

// Pena Da conversione pena pecuniaria
DatiFinaliUlterioriSanzioniModel lUltSan_LavSost_ConvPP = datiFinaliAggregatoModel.getUltSanConvPPLavSost();
DatiFinaliUlterioriSanzioniModel lUltSan_LibCtrl_ConvPP = datiFinaliAggregatoModel.getUltSanConvPPLibCtrl();

if (lUltSan_LavSost_ConvPP==null)  lUltSan_LavSost_ConvPP  = new DatiFinaliUlterioriSanzioniModel();
if (lUltSan_LibCtrl_ConvPP==null)  lUltSan_LibCtrl_ConvPP  = new DatiFinaliUlterioriSanzioniModel();

// Sanzioni del giudice di pace   
DatiFinaliUlterioriSanzioniModel lUltSan_PermDom_GP = datiFinaliAggregatoModel.getUltSanGiuPacePermDom();
DatiFinaliUlterioriSanzioniModel lUltSan_LavSost_GP = datiFinaliAggregatoModel.getUltSanGiuPaceLavSost();
DatiFinaliUlterioriSanzioniModel lUltSan_LPU_GP     = datiFinaliAggregatoModel.getUltSanGiuPaceLPU();
DatiFinaliUlterioriSanzioniModel lUltSan_ESP_GP     = datiFinaliAggregatoModel.getUltSanGiuPaceESP();



if (lUltSan_PermDom_GP==null)  lUltSan_PermDom_GP = new DatiFinaliUlterioriSanzioniModel();
if (lUltSan_LavSost_GP==null)  lUltSan_LavSost_GP = new DatiFinaliUlterioriSanzioniModel();
if (lUltSan_LPU_GP==null)      lUltSan_LPU_GP     = new DatiFinaliUlterioriSanzioniModel();
if (lUltSan_ESP_GP==null)      lUltSan_ESP_GP     = new DatiFinaliUlterioriSanzioniModel();


boolean isPrimoCalcolo = false;
if (lPenaRideterminataCumulo.getIdPenaRideterminataCumulo()!=null) {
  isPrimoCalcolo = false;
}


%>

<html>
<head>
  <title> [S.I.E.S.] - Dati Finali Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="javascript">
   //if(history.length>0)history.forward();
  </script>
  
  <script language="JavaScript">
    function CalcoloPenaPopup (a_form_name, a_form_type)
    {
      <%
      String lStrParametri = "";
      lStrParametri +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
      %>
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoriacumulo.action.ActLoadCalcoloPenaCumulo&ParentFormName="+a_form_name+"&ParentFormType="+a_form_type+"<%=lStrParametri%>"    
                          , "CalcoloPenaCumulo"
                          , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=yes, width=940, height=900");
    } 
  </script>  

  <script language="JavaScript">
    //==========================================================================
    // Ritorna alla Griglia Della Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }
    
    function espandi(idTabella){
      var collapseGif = "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
      var hrefNew = "Javascript:collassa('"+idTabella+"');";
      
      $('#'+idTabella+' a').attr('href',hrefNew);
      $('#'+idTabella+' a').children().attr('src',collapseGif);

      //alert("text = "+$('#'+idTabella+' td')[0].text());
      
      var tabella = $('#'+idTabella+' tr:gt(0)').show();
      if (idTabella=='tabSanSost'){
        var checkLPU = $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LPU_SS%>');
        checkAbilitaDisabilitaCampi(checkLPU);
        //$('#tabEsecuzioneLPU').show();
        //$('#tabOrarioLPU').show();
      }      
    }
    
    function collassa(idTabella){
      var expandGif = "<%=IWebConstants.IMAGES_DIR%>expand.gif";
      var hrefNew = "Javascript:espandi('"+idTabella+"');";
      
      $('#'+idTabella+' a').attr('href',hrefNew);
      $('#'+idTabella+' a').children().attr('src',expandGif);
  
      var tabella = $('#'+idTabella+' tr:gt(0)').hide();
      
      //var firstTD= $('#'+idTabella+' td:eq(0)');
      var firstTD= $('#'+idTabella+' td').eq(0);
      var text = firstTD.text();
      //firstTD.text(text+' * ');
      //alert(firstTD.text());
      
      if (idTabella=='tabSanSost'){
        $('#tabEsecuzioneLPU').hide();
        $('#tabOrarioLPU').hide();        
      }
    }
    
    //==========================================================================
    // Abilita/Disabilita i campi di input che si trovano sulla <td> successiva
    // a quella del check box selezionato
    // Nel caso delle SanzSost richiama l'opportuna funzione
    //==========================================================================
    function checkAbilitaDisabilitaCampi(checkObject){
      var jQueryObj = $(checkObject);
      
      if (jQueryObj.prop('checked')){
        //jQueryObj.closest('td').next('td').find('input').prop('disabled',false);
        
        if (jQueryObj.closest('table').attr('id')=='tabLA'){
          jQueryObj.closest('td').nextAll('td').find('input').prop('disabled',false);
          jQueryObj.closest('td').nextAll('td').prop('disabled',false);
        }
        else {
          jQueryObj.closest('td').next('td').find('input').prop('disabled',false);
          jQueryObj.closest('td').next('td').prop('disabled',false);
        }
        
        if (jQueryObj.closest('table').attr('id')=='tabSanSost'){
          // Sono mutuamente esclusive. Se seleziono una SS devo disabilitare
          // tutte le altre (non nel cumulo)
          checkSanzioniSostitutive(checkObject);
        }
        
        if (jQueryObj.attr('id')=='<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_GP%>') {        
          checkEspulsionePerpetua($('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_GP%>'));
        }        
      }
      else {
        if (jQueryObj.closest('table').attr('id')=='tabLA'){
          jQueryObj.closest('td').nextAll('td').find('input').prop('disabled',true);
          jQueryObj.closest('td').nextAll('td').prop('disabled',true);
        }
        else {
          jQueryObj.closest('td').next('td').find('input').prop('disabled',true);
          jQueryObj.closest('td').next('td').prop('disabled',true);
          //jQueryObj.closest('td').next('td').find('input').val(''); // test per ripulire anche i campi
        }
 
        if (jQueryObj.closest('table').attr('id')=='tabSanSost'){
          checkSanzioniSostitutive(checkObject);
        }
        
        if (jQueryObj.attr('id')=='<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_GP%>') {        
          checkEspulsionePerpetua($('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_GP%>'));
        } 
      }
    }
    
    //==========================================================================
    // Gestione specifica per le sanzioni che sono mutuamente esclusive
    // per cui vanno abilitati/disabilitati anche i check della prima colonna
    //==========================================================================
    function checkSanzioniSostitutive(checkObject){
      var jQueryObj = $(checkObject);

      /* in caso di cumulo le SS non sono mutuamente esclusive
      var checkSemid   = $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_SEMIDETENZIONE_SS%>');
      var checkLibCtrl = $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LIBERTACTRL_SS%>');
      var checkMulta   = $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_MULTA_SS%>');
      var checkAmmenda = $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_AMMENDA_SS%>');
      var checkEspuls  = $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_STATO_SS%>');
      var checkLPU     = $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LPU_SS%>');  
    
      
      var myElements = new Array();
      var nx=0;
      myElements[nx++] = checkSemid;
      myElements[nx++] = checkLibCtrl;
      myElements[nx++] = checkMulta;
      myElements[nx++] = checkAmmenda;
      myElements[nx++] = checkEspuls;
      myElements[nx++] = checkLPU;
      
      for (var i=0;i<myElements.length;i++){
        var elem = myElements[i];

        if (elem.attr('id')==jQueryObj.attr('id')){
          //alert("id = "+elem.attr('id'));
        }
        else {
          if (jQueryObj.prop('checked')) {
            // disabilito
            elem.closest('td').prop('disabled',true);
            elem.closest('td').next('td').prop('disabled',true);
          }
          else {
            // riabilito (solo la prima colonna)
            elem.closest('td').prop('disabled',false);
          }
        }
      } 
      */
      
      if (jQueryObj.attr('id')=='<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_STATO_SS%>') {        
        checkEspulsionePerpetua($('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_SS%>'));
      }
      
      if (jQueryObj.attr('id')=='<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LPU_SS%>') {
        checkLavoroPubblicaUtilita(checkObject);
      }
    }
    
    
    function checkEspulsionePerpetua(checkObject) {
      var jQueryObj = $(checkObject);
      
      if (jQueryObj.prop('checked')){
        jQueryObj.closest('td').find('input[type=text]').prop('disabled',true);
      }
      else {
        jQueryObj.closest('td').find('input[type=text]').prop('disabled',false);
      }
    }
    
    // LPU deve abilitare/disabilitare: tipo, la tr nella misura di e la tabella esecuzione
    function checkLavoroPubblicaUtilita(checkObject){
      var jQueryObj = $(checkObject);
      
      if (jQueryObj.prop('checked')) {
        jQueryObj.closest('td').next('td').prop('disabled',false);
        $('#tdLPUMisura').prop('disabled',false);
        $('#tabEsecuzioneLPU').show();
        
        if ( $("#tabEsecuzioneLPU [type=radio][name=<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_COD_FREQ_SETT%>]:checked").val()==0){
          $('#tabOrarioLPU').hide();
        }
        else {
          $('#tabOrarioLPU').show();
        }
      }
      else {
        // disabilito
        jQueryObj.closest('td').next('td').prop('disabled',true);
        $('#tdLPUMisura').prop('disabled',true);
        $('#tabEsecuzioneLPU').hide();
        $('#tabOrarioLPU').hide();
      }      
    }
    
    function checkFrequenzaSettimanale(checkObject) {
      var jQueryObj = $(checkObject);
      if ( jQueryObj.prop('value')=='0') {
        $('#tabOrarioLPU').hide();
      }
      else {
        $('#tabOrarioLPU').show();
      }
    }
    
    
    
    // Effettua l'opportuna submit
    function eseguiSubmit (azione) {
      var action = "";
      
      if (azione=='submit') {
        if (Verify()){
          document.formName.submit();
        }
      }
      else if (azione=='annulla'){
        <% if (modalita.equals(ICostantiModuloCumulo.MODALITA_MODIFICA)) { %>
        if (confirm("Attenzione le eventuali modifiche ai dati non verranno salvate. Si vuole procedere?")){
          $("form[name='formName']").find("input[type='button']").prop('disabled',true);
          document.formAnnulla.submit();
        }
        <% } else {%>
        if (confirm("Attenzione i dati inseriti non verranno salvati. Si vuole procedere?")){
          $("form[name='formName']").find("input[type='button']").prop('disabled',true);
          document.formAnnulla.submit();
        }
        <% } %>
        else {
          return;
        }
      }
    }
    
    
    //==========================================================================
    // Funzione di verifica dati 
    //==========================================================================
    function Verify() { 
      var errore = false;

      var checkInputFields = $('input[type=checkbox][tipolOra!=S]:checked');


      checkInputFields.each ( function (index) {
          //alert($(this).attr('title'));
          var idCheck = $(this).attr('id');

          if (idCheck=='<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_ERGASTOLO_PD%>') {
            //alert("check ergastolo");
            return true; // Salto il controllo
          } 
          else if (idCheck=='<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_SS%>') {
            //alert("check SS Espulsione dallo Stato - Perpetua");
            return true; // Salto il controllo
          }
          else if (idCheck=='<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_GP%>') {
            //alert("check Espulsione dallo Stato GP - Perpetua");
            return true; // Salto il controllo
          }
          else if (idCheck=='<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_STATO_SS%>') {
            var checkPerpetua = $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_SS%>').prop('checked');
            //alert("check SS Espulsione dallo Stato Perpetua = "+checkPerpetua);
            if (checkPerpetua)
              return true; // Salto il controllo sulla durata
          }
          else if (idCheck=='<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESP_GP%>') {
            var checkPerpetua = $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_GP%>').prop('checked');
            //alert("check SS Espulsione dallo Stato Perpetua = "+checkPerpetua);
            if (checkPerpetua)
              return true; // Salto il controllo sulla durata
          }          
          else if (idCheck=='<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LPU_SS%>') {
            if (checkLPU())
              return true;
            else {
              errore = true;
              return false; 
            }
          }


          var inputFields;
          if (idCheck=='<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_LIBANT%>') {
            inputFields = $(this).closest('td').closest('tr').children('td').find('input[type=text]');
          }
          else {
            inputFields = $(this).closest('td').next('td').find('input[type=text]');
          }
          
          //alert ("size = "+inputFields.size());
          var contaValorizzati = 0;
          inputFields.each(function(){
            //alert ("value = " + this.value);
            if (this.value!='') contaValorizzati++;
          });
  
          //alert("checkName = "+this.name+" valorizzati = "+contaValorizzati);
          if (contaValorizzati==0){
            alert(this.title+": Indicare almeno un valore o deselezionare la voce");
            inputFields.eq(0).focus();
            errore = true;
            return false;
          }
        }
      );
      
      if (errore) return false;


      $("form[name='formName']").find("input[type='button']").prop('disabled',true);
      return true;
    } 

    //=================================
    // controllo completezza campi LPU
    //=================================
    function checkLPU (){
      var checkLPU = $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LPU_SS%>');
      var comboLPU = $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_COD_TIPO_LPU_SS%>');
     
      if (comboLPU.val()=='-') {
        alert(checkLPU.attr('title')+": Indicare la tipologia di Lavoro Pubblica Utilità");
        comboLPU.focus();
        return false;
      }
      
      var imputFieldsDurata = checkLPU.closest('tr').next('tr').find('input[type=text]');
      var contaValorizzati = 0;
      imputFieldsDurata.each(function(){
        if (this.value!='') contaValorizzati++;
      });

      //alert("checkName = "+this.name+" valorizzati = "+contaValorizzati);
      if (contaValorizzati==0){
        alert(checkLPU.attr('title')+": Indicare la durara della misura");
        imputFieldsDurata.eq(0).focus();
        return false;
      }
      
      //===  Verifico i campi tipologia Oraria  ====
      if (   $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LPU_SS%>').prop('checked')
          && $('#<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_COD_FREQ_SETT%>_1').prop('checked')
         ) 
      {
        //alert("Tipologia Orario");
        var tipologiaSel = $('input[type=checkbox][tipolOra=S]:checked');
        
        if (tipologiaSel.size()==0){
          alert(checkLPU.attr('title')+": Se la Frequenza settimanale è Determinata, indicare la Tipologia orario per almeno una giornata della settimana");
          return false;
        }
        
        var errore = false;
        tipologiaSel.each( function (index) {
          var inputFields = $(this).closest('td').next('td').find('input');
          var objOraDal = inputFields.eq(0);
          var objOraAl = inputFields.eq(1);
          
          if (objOraDal.val()==''){
            alert ("Orario non indicato");
            objOraDal.focus();
            errore = true;
            return false; // per uscire dall'each
          }     
          else if (objOraAl.val()==''){
            alert ("Orario non indicato");
            objOraAl.focus();
            errore = true;
            return false; // per uscire dall'each
          }
          else if (parseInt(objOraDal.val())>=parseInt(objOraAl.val())) {
            alert ("Orari esecuzione LPU non coerenti: l'oraro di inizio deve essere minore all'orario fine ");
            objOraDal.focus();
            errore = true;
            return false; // per uscire dall'each
          }
        });
      
        if (errore) return false;
      }
      
      return true; 
    }
    
    //================================================
    // Funzione richiamata al caricamento della form
    //================================================
    $(document).ready(function(){
     
      <%if (   datiFinaliAggregatoModel.getUltSanSanSosSemidetenzione()!=null
            || datiFinaliAggregatoModel.getUltSanSanSosLibertContrl()!=null
            || datiFinaliAggregatoModel.getUltSanSanSosPPMulta()!=null
            || datiFinaliAggregatoModel.getUltSanSanSosPPAmmenda()!=null
            || datiFinaliAggregatoModel.getUltSanSanSosEspulsione()!=null
            || datiFinaliAggregatoModel.getUltSanSanSosLPU()!=null
           ) { %>
      espandi('tabSanSost');
      <% } else { %>
      collassa('tabSanSost');
      <% } %>
      
      <%if (!lPenaRideterminataCumulo.isLibAnt()) { %>
      collassa('tabLA');
      <% } else { %>
      espandi('tabLA');
      <% } %>
      
      
      <%if (   datiFinaliAggregatoModel.getUltSanConvPPLavSost()!=null
            || datiFinaliAggregatoModel.getUltSanConvPPLibCtrl()!=null
           ) { %>
      espandi('tabPCPP');
      <% } else { %>
      collassa('tabPCPP');
      <% } %>
      
      <%if (   datiFinaliAggregatoModel.getUltSanGiuPacePermDom()!=null 
            || datiFinaliAggregatoModel.getUltSanGiuPaceLavSost()!=null 
            || datiFinaliAggregatoModel.getUltSanGiuPaceLPU()!=null 
            || datiFinaliAggregatoModel.getUltSanGiuPaceESP()!=null 
           ) { %>
      espandi('tabSGDP');
      <% } else { %>
      collassa('tabSGDP');
      <% } %>
      
      
      //alert("num check = "+$('input[type=checkbox]').size());
      
      //Inizilaizzazione delle check
      $('input[type=checkbox]').each( function (index) {
        checkAbilitaDisabilitaCampi(this);
      });
      
      /*
      $('input[type=text]').focus(function (index) {
          //alert("focus id = "+$(this).css("background-color"));
          $(this).css("background-color","#FFFF00");
         }
       );
       
      $('input[type=text]').blur(function (index) {
          //alert("focus id = "+$(this).css("background-color"));
          $(this).css("background-color","#FFFFFF");
         }
       );       
*/
      
      // Test popup calcolo pena
      <% if (isPrimoCalcolo) { %>
        CalcoloPenaPopup('formName','formType');
      <% } %>
      
      
    });
  </script>
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
        <font class="campo">Dati Finali Cumulo - Pene Determinate in cumulo</font>      
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>


<div id="divPosizionamento" align="left" style="padding-left: 25px;">    


  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formAnnulla">
    <%//FORM per richiamare i dettagli in POST ma evitare di inviare inutilmente i dati della FORM principale%>
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActDettaglioPeneRideterminate">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  </form>
    
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciPeneRideterminate">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    <input type="hidden" name="<%=ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO%>" value="<%=StringUtils.toStringJSP(lDatiFinaliCumulo.getIdDatiFinaliCumulo()) %>">

    <input type="hidden" name="<%= ICostantiPenaRideterminataCumulo.CAMPO_ID_PENA_RIDETERMINATA_CUMULO %>" value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getIdPenaRideterminataCumulo()) %>">
    <input type="hidden" name="<%= ICostantiPenaRideterminataCumulo.CAMPO_FLAG_PENA_RESIDUA_CUMULO %>" value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getFlagPenaResiduaCumulo()) %>">
  
    <input type="hidden" name="<%= ICostantiDatiFinaliUlterioriSanzioni.CAMPO_ID_SEMIDETENZIONE_SS %>" 
                         value="<%=StringUtils.toStringJSP(lUltSan_SanSos_SEM.getIdDatiFinaliUlterioriSanz()) %>"  >
    <input type="hidden" name="<%= ICostantiDatiFinaliUlterioriSanzioni.CAMPO_ID_LIBERTCONTR_SS %>" 
                         value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LIBCTRL.getIdDatiFinaliUlterioriSanz()) %>"  >
    <input type="hidden" name="<%= ICostantiDatiFinaliUlterioriSanzioni.CAMPO_ID_MULTA_SS %>" 
                         value="<%=StringUtils.toStringJSP(lUltSan_SanSos_PPM.getIdDatiFinaliUlterioriSanz()) %>"  >
    <input type="hidden" name="<%= ICostantiDatiFinaliUlterioriSanzioni.CAMPO_ID_AMMENDA_SS %>" 
                         value="<%=StringUtils.toStringJSP(lUltSan_SanSos_PPA.getIdDatiFinaliUlterioriSanz()) %>"  >
    <input type="hidden" name="<%= ICostantiDatiFinaliUlterioriSanzioni.CAMPO_ID_ESPULSIONE_SS %>" 
                         value="<%=StringUtils.toStringJSP(lUltSan_SanSos_ESP.getIdDatiFinaliUlterioriSanz()) %>"  >
    <input type="hidden" name="<%= ICostantiDatiFinaliUlterioriSanzioni.CAMPO_ID_LPU_SS %>" 
                         value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getIdDatiFinaliUlterioriSanz()) %>"  >
  
    <input type="hidden" name="<%= ICostantiDatiFinaliUlterioriSanzioni.CAMPO_ID_LAVSOST_PP %>" 
                         value="<%=StringUtils.toStringJSP(lUltSan_LavSost_ConvPP.getIdDatiFinaliUlterioriSanz()) %>"  >
    <input type="hidden" name="<%= ICostantiDatiFinaliUlterioriSanzioni.CAMPO_ID_LIBCTRL_PP %>" 
                         value="<%=StringUtils.toStringJSP(lUltSan_LibCtrl_ConvPP.getIdDatiFinaliUlterioriSanz()) %>"  >

    <input type="hidden" name="<%= ICostantiDatiFinaliUlterioriSanzioni.CAMPO_ID_PERMDOM_GP %>" 
                         value="<%=StringUtils.toStringJSP(lUltSan_PermDom_GP.getIdDatiFinaliUlterioriSanz()) %>"  >
    <input type="hidden" name="<%= ICostantiDatiFinaliUlterioriSanzioni.CAMPO_ID_LAVSOST_GP %>" 
                         value="<%=StringUtils.toStringJSP(lUltSan_LavSost_GP.getIdDatiFinaliUlterioriSanz()) %>"  >
    <input type="hidden" name="<%= ICostantiDatiFinaliUlterioriSanzioni.CAMPO_ID_LPU_GP %>" 
                         value="<%=StringUtils.toStringJSP(lUltSan_LPU_GP.getIdDatiFinaliUlterioriSanz()) %>"  >
    <input type="hidden" name="<%= ICostantiDatiFinaliUlterioriSanzioni.CAMPO_ID_ESPULSIONE_GP %>" 
                         value="<%=StringUtils.toStringJSP(lUltSan_ESP_GP.getIdDatiFinaliUlterioriSanz()) %>"  >




 
    <input type="hidden" name="modalita" value="<%=modalita%>">


<%
//==============================================================================
// Per ora è una form di prova
//==============================================================================
%>
<table cellspacing=2 cellpadding=4 width="800px">
  <tr>
    <td class="l">
      <a href="Javascript:CalcoloPenaPopup('formName','formType');">
        Dettaglio della pena <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0></a>
    </td>
  </tr>
</table>

<table cellspacing=2 cellpadding=4 width="800px">
  <tr><td class="Titolo" colspan=4>Pena Detentiva</td></tr>
  <tr id="trReclusionePD">
    <td class="l" colspan="1" >
      <input type="checkbox" 
             name="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_RECLUSIONE_PD%>"
             value="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_RECLUSIONE_PD_VAL%>"
             title="Pena Detentiva - Reclusione" 
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);" 
             <%=lPenaRideterminataCumulo.isReclusione()?"checked":""%>
             >&nbsp;Reclusione
    </td>
    <td class="l" colspan=1>
      Anni&nbsp;<input type="text" Title="Anni Reclusione"  maxlength="2" size="2"
                       name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_ANNI_RECLUSIONE %>" 
                       value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumAnniReclusione()) %>"
                       onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;<input type="text" Title="Mesi Reclusione" maxlength="2" size="2" 
                       name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_MESI_RECLUSIONE %>" 
                       value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumMesiReclusione()) %>"
                       onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;<input type="text" Title="Giorni Reclusione" maxlength="4" size="4" 
                         name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_GIORNI_RECLUSIONE %>" 
                         value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumGiorniReclusione()) %>"
                         onkeypress="return TicTabNumField(this,event)">
    </td>
    
    <td class="l" colspan="1">
      <input type="checkbox" title="Pena Detentiva - Multa" 
             name="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_MULTA_PD%>"
             value="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_MULTA_PD_VAL%>"
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);" 
             <%=lPenaRideterminataCumulo.getImportoMulta()!=null?"checked":""%>
             >&nbsp;Multa
    </td>
    <%-- Ticket#202409100121 - siep Procura Generale di Napoli --%>
    <%-- aumentate le size a 14 ovunque per multa e ammenda --%>
    <td class="l" colspan=1 >
      <input type="text" Title="Multa" size="14" maxlength="14"  style="text-align:right"
             name="<%= ICostantiPenaRideterminataCumulo.CAMPO_IMPORTO_MULTA%>INT" 
             value="<%=StringUtils.getParteIntera(lPenaRideterminataCumulo.getImportoMulta()) %>"
             onkeypress="return TicTabNumField(this,event)">
      ,
      <input type="text" Title="Multa" size="2" maxlength="2" 
             name="<%= ICostantiPenaRideterminataCumulo.CAMPO_IMPORTO_MULTA %>DEC"
             value="<%=StringUtils.getParteDecimale(lPenaRideterminataCumulo.getImportoMulta()) %>"
             onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
  
  <tr id="trArrestoPD">
    <td class="l" colspan="1">
      <input type="checkbox" title="Pena Detentiva - Arresto"
             name="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_ARRESTO_PD%>"
             value="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_ARRESTO_PD_VAL%>"
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);" 
             <%=lPenaRideterminataCumulo.isArresto()?"checked":""%>
             >&nbsp;Arresto            
    </td>
    <td class="l" colspan="1">
      Anni&nbsp;<input type="text" Title="Anni Arresto"  maxlength="2" size="2"                       
                       name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_ANNI_ARRESTO %>" 
                       value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumAnniArresto()) %>"
                       onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;<input type="text" Title="Mesi Arresto" maxlength="2" size="2" 
                       name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_MESI_ARRESTO %>"        
                       value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumMesiArresto()) %>"
                       onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;<input type="text"  Title="Giorni Arresto" maxlength="4" size="4" 
                         name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_GIORNI_ARRESTO %>" 
                         value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumGiorniArresto()) %>"
                         onkeypress="return TicTabNumField(this,event)">
    </td>
      
    <td class="l" colspan="1">
      <input type="checkbox" title="Pena Detentiva - Ammenda" 
             name="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_AMMENDA_PD%>"
             value="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_AMMENDA_PD_VAL%>"
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);" 
             <%=lPenaRideterminataCumulo.getImportoAmmenda()!=null?"checked":""%>
             >&nbsp;Ammenda            
    </td>
    <td class="l" colspan="1">
      <input type="text" Title="Ammenda" size="14" maxlength="14"  style="text-align:right"
             value="<%=StringUtils.getParteIntera(lPenaRideterminataCumulo.getImportoAmmenda()) %>"
             name="<%= ICostantiPenaRideterminataCumulo.CAMPO_IMPORTO_AMMENDA%>INT"       
             onkeypress="return TicTabNumField(this,event)">
      ,
      <input type="text" Title="Ammenda" size=2 maxlength=2
             value="<%=StringUtils.getParteDecimale(lPenaRideterminataCumulo.getImportoAmmenda()) %>"
             name="<%= ICostantiPenaRideterminataCumulo.CAMPO_IMPORTO_AMMENDA %>DEC"        
             onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
    
  <tr id="trErgastoloPD"> 
    <td class="l" colspan="1">
      <input type="checkbox" 
             name="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_ERGASTOLO_PD%>"
             id="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_ERGASTOLO_PD%>"
             value="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_ERGASTOLO_PD_VAL%>"
             title="Pena Detentiva - Ergastolo" onClick="Javascript:checkAbilitaDisabilitaCampi(this);" 
             <%=lPenaRideterminataCumulo.isErgastolo()?"checked":""%>
             >&nbsp;Ergastolo            
    </td>
    <td class="l" colspan="3">
      Durata Isolamento Diurno: Anni
      <input type="text" Title="Anni Isolamento Diurno"  maxlength="2" size="2" 
             name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO %>"
             value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumAnniIsolamentoDiurno()) %>"
             onkeypress="return TicTabNumField(this,event)">
        Mesi
      <input type="text" Title="Mesi Isolamento Diurno"  maxlength="2" size="2"
             name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_MESI_ISOLAMENTO_DIURNO %>"   
             value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumMesiIsolamentoDiurno()) %>"
             onkeypress="return TicTabNumField(this,event)">
        Giorni
      <input type="text" Title="Giorni Isolamento Diurno"  maxlength="4" size="4" 
             name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO %>"        
             value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumGiorniIsolamentoDiurno()) %>"
             onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
</table>
    
<%
//==============================================================================
// Sanzione Sostitutiva
//==============================================================================
%>
<br>
<table id="tabSanSost" cellspacing="2" cellpadding="4" width="800px">
  <tr>
    <td class="titolo" style="text-align:left" colspan="4">
      <a href="Javascript:espandi('tabSanSost');"><img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0"/></a>
      Sanzione Sostitutiva
    </td>
  </tr>

  <tr id="trSemidetenzione">
    <td class="l">
      <input type="checkbox" 
        name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_SEMIDETENZIONE_SS%>" 
        id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_SEMIDETENZIONE_SS%>"
        value="<%=ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_SEMIDET_SS%>"
        title="Sanzione Sostitutiva - Semidetenzione"
        <%=lUltSan_SanSos_SEM.isDurata()?"checked":""%>
        onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Semidetenzione
    </td>
    <td class="l">
      Anni&nbsp;
      <input type="text"  maxlength="2" size="2" title="Anni Semidetenzione" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_SEMIDETENZIONE_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_SEMIDETENZIONE_SS%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_SEM.getNumAnni()) %>"    
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;
      <input type="text" maxlength="2" size="2" title="Mesi Semidetenzione"  
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_SEMIDETENZIONE_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_SEMIDETENZIONE_SS%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_SEM.getNumMesi()) %>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;
      <input type="text" maxlength="3" size="3" title="Giorni Semidetenzione"
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_SEMIDETENZIONE_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_SEMIDETENZIONE_SS%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_SEM.getNumGiorni()) %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
             
    </td>
  </tr>
  
  <tr id="trLibCtrl">
    <td class="l">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LIBERTACTRL_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LIBERTACTRL_SS%>" 
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LIBCTRL_SS%>" 
             title="Sanzione Sostitutiva - Libertà Controllata"
             <%=lUltSan_SanSos_LIBCTRL.isDurata()?"checked":""%>
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Libertà Controllata
    </td>
    <td class="l" >
      Anni&nbsp;
      <input type="text" maxlength="2" size="2" title="Anni Liberta Controllata"
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_LIBERTACTRL_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_LIBERTACTRL_SS%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LIBCTRL.getNumAnni()) %>"    
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;
      <input type="text" maxlength="2" size="2" title="Mesi Liberta Controllata"
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_LIBERTACTRL_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_LIBERTACTRL_SS%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LIBCTRL.getNumMesi()) %>"    
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;
      <input type="text" maxlength="3" size="3" title="Giorni Liberta Controllata" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_LIBERTACTRL_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_LIBERTACTRL_SS%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LIBCTRL.getNumGiorni()) %>"    
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
  
  <tr>
    <td class="l">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_MULTA_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_MULTA_SS%>" 
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_MULTA_SS%>"
             title="Sanzione Sostitutiva - Pena Pecuniaria - Multa"
             <%=lUltSan_SanSos_PPM.getMulta()!=null?"checked":""%>
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Pena Pecuniaria: 
    </td>
    <td class="l">
      Multa&nbsp;
      <input type="text" size="14" maxlength="14" title="Multa" style="text-align:right"
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_INTERO_MULTA_APPLICATA_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_INTERO_MULTA_APPLICATA_SS%>"
             value="<%=StringUtils.getParteIntera(lUltSan_SanSos_PPM.getMulta()) %>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      ,
      <input type="text" size="2" maxlength="2" title="Multa" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_DECIMALE_MULTA_APPLICATA_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_DECIMALE_MULTA_APPLICATA_SS%>"
             value="<%=StringUtils.getParteDecimale(lUltSan_SanSos_PPM.getMulta()) %>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
  
  <tr>
    <td class="l">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_AMMENDA_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_AMMENDA_SS%>" 
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_AMMENDA_SS%>"
             title="Sanzione Sostitutiva - Pena Pecuniaria - Ammenda"
             <%=lUltSan_SanSos_PPA.getAmmenda()!=null?"checked":""%>
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Pena Pecuniaria: 
    </td>
    <td class="l">
      Ammenda&nbsp;
      <input type="text" size="14" maxlength="14" title="Ammenda" style="text-align:right"
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_INTERO_AMMENDA_APPLICATA_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_INTERO_AMMENDA_APPLICATA_SS%>"
             value="<%=StringUtils.getParteIntera(lUltSan_SanSos_PPA.getAmmenda()) %>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      ,
      <input type="text" size="2" maxlength="2" title="Ammenda" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_DECIMALE_AMMENDA_APPLICATA_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_DECIMALE_AMMENDA_APPLICATA_SS%>"
             value="<%=StringUtils.getParteDecimale(lUltSan_SanSos_PPA.getAmmenda()) %>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr> 
  
  <% // Espulsione %>
  <tr>
    <td class="l">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_STATO_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_STATO_SS%>" 
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_ESPULSIONE_SS%>" 
             title="Sanzione Sostitutiva - Espulsione dallo Stato"
             <%=lUltSan_SanSos_ESP.isEspulsione()?"checked":""%>
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Espulsione dallo Stato
    </td>
    <td class="l">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_SS%>" 
             value="P"
             title="Sanzione Sostitutiva - Espulsione Perpetua"
             <%="P".equals(lUltSan_SanSos_ESP.getFlagEspulPerp())?"checked":""%>
             onClick="Javascript:checkEspulsionePerpetua(this);" >
      Perpetua oppure Temporanea per 
      Anni&nbsp;
      <input type="text" maxlength="2" size="2" title="Anni Espulsione dallo Stato" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_ESPULSIONE_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_ESPULSIONE_SS%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_ESP.getNumAnni()) %>"             
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;
      <input type="text" maxlength="2" size="2" title="Mesi Espulsione dallo Stato" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_ESPULSIONE_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_ESPULSIONE_SS%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_ESP.getNumMesi()) %>"             
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;
      <input type="text" maxlength="3" size="3" title="Giorni Espulsione dallo Stato"
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_ESPULSIONE_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_ESPULSIONE_SS%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_ESP.getNumGiorni()) %>"             
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
  
  <tr>
    <td class="l">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LPU_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LPU_SS%>" 
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LPU_SS%>" 
             title="Sanzione Sostitutiva - Lavoro Pubblica Utilità"
             <%=lUltSan_SanSos_LPU.getCodTipoLpu()!=null?"checked":""%>
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);" >
      Lavoro Pubblica Utilità
    </td>
    <td class="l">
      Tipo
      <select Title="Tipo Lavoro Pubblica Utilità" name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_COD_TIPO_LPU_SS%>" 
              id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_COD_TIPO_LPU_SS%>" 
              >
        <option value="-"/>-
        <%=comboLPU%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Nella misura di</td>
    <td class="l" id="tdLPUMisura">
      Anni&nbsp;
      <input type="text" maxlength="2" size="2" title="Anni Lavoro Pubblica Utilità"
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_LPU_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_LPU_SS%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getNumAnni()) %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;
      <input type="text" maxlength="2" size="2" title="Mesi Lavoro Pubblica Utilità" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_LPU_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_LPU_SS%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getNumMesi()) %>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;
      <input type="text" maxlength="5" size="5" title="Giorni Lavoro Pubblica Utilità"
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_LPU_SS%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_LPU_SS%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getNumGiorni()) %>"              
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      Pari ad ore complessive&nbsp;
      <input type="text" maxlength="5" size="5" title="Ore Lavoro Pubblica Utilità" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ORE_TOT%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ORE_TOT%>"
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getNumOreTot()) %>"     
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
</table>

<table id="tabEsecuzioneLPU" cellspacing="2" cellpadding="4" width="800px">
  <tr>
    <td class="Titolo" colspan="4">Modalità di Esecuzione Lavoro Pubblica Utilità</td>
  </tr>
  <tr>
    <td class="l">
      Ore di lavoro Settimanali da svolgere
      <input type="text" maxlength="2" size="2" Title="Ore Settimanali Lavoro Pubblica Utilità" 
             value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getNumOreSett()) %>" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ORE_SETT%>"  
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
    </td>
    <td class="l">
      Frequenza Settimanale:
      Non Determinata&nbsp;
      <input type="radio" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_COD_FREQ_SETT%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_COD_FREQ_SETT%>_0"
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.VAL_COD_FREQ_NON_DET%>" 
             <%=(lUltSan_SanSos_LPU.getCodFreqSett()==null || lUltSan_SanSos_LPU.getCodFreqSett().compareTo(new BigDecimal(ICostantiDatiFinaliUlterioriSanzioni.VAL_COD_FREQ_NON_DET))==0)?"checked":""%>
             title="Frequenza Settimanale - Non Determinata" 
             onClick="Javascript:checkFrequenzaSettimanale(this);">
      Determinata&nbsp;
      <input type="radio" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_COD_FREQ_SETT%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_COD_FREQ_SETT%>_1" 
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.VAL_COD_FREQ_DETER%>" 
             <%=(lUltSan_SanSos_LPU.getCodFreqSett()!=null && lUltSan_SanSos_LPU.getCodFreqSett().compareTo(new BigDecimal(ICostantiDatiFinaliUlterioriSanzioni.VAL_COD_FREQ_DETER))==0)?"checked":""%>
             title="Frequenza Settimanale - Determinata" 
             onClick="Javascript:checkFrequenzaSettimanale(this);">
    </td>
  </tr>
</table>

<table id="tabOrarioLPU" cellspacing="2" cellpadding="4" width="800px">
  <tr>
    <td class="l" colspan="4">
      Tipologia Orario: 
    </td>
  </tr>

<!-- CICLO GIORNI SETTIMANA - START -->
<%
int day = 1;
while (day<=4){
  String dayCol1 = "0"+day;
  String dayCol2 = "0"+(day+4);
%>  
    <tr>
      <td class="l">
        <input type="checkbox"
               name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_CHECK_ORARIO_DAY%>_<%=dayCol1%>"  
               id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_CHECK_ORARIO_DAY%>_<%=dayCol1%>"
               value="<%=dayCol1%>" 
               <%=lUltSan_SanSos_LPU.checkTipologiaOrario(dayCol1)%>
               tipolOra="S"
               title="Check per selezionare la giornata" 
               onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
        <%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getTipologiaOrarioGiorno(dayCol1))%>
      </td>
      <td class="l">
        dalle ore&nbsp;
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_DALLE_ORE_DAY%>_<%=dayCol1%>"  
               id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_DALLE_ORE_DAY%>_<%=dayCol1%>"
               value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getTipologiaOrarioDalleOre(dayCol1))%>" 
               title="dalle ore" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        alle ore&nbsp;
        <input type="text" maxlength="2" size="2" 
               name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_ALLE_ORE_DAY%>_<%=dayCol1%>"  
               id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_ALLE_ORE_DAY%>_<%=dayCol1%>"
               value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getTipologiaOrarioAlleOre(dayCol1))%>"
               title="alle ore" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      </td>
<%  if (day<4) { %>
      <td class="l"> 
        <input type="checkbox"
               name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_CHECK_ORARIO_DAY%>_<%=dayCol2%>"  
               id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_CHECK_ORARIO_DAY%>_<%=dayCol2%>"
               value="<%=dayCol2%>" 
               <%=lUltSan_SanSos_LPU.checkTipologiaOrario(dayCol2)%>
               title="Check per selezionare il giorno" 
               onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
        <%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getTipologiaOrarioGiorno(dayCol2))%>
      </td>
      <td class="l">
        dalle ore&nbsp;
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_DALLE_ORE_DAY%>_<%=dayCol2%>"  
               id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_DALLE_ORE_DAY%>_<%=dayCol2%>"
               value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getTipologiaOrarioDalleOre(dayCol2))%>" 
               title="dalle ore" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        alle ore&nbsp;
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_ALLE_ORE_DAY%>_<%=dayCol2%>"  
               id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_ALLE_ORE_DAY%>_<%=dayCol2%>"
               value="<%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getTipologiaOrarioAlleOre(dayCol2))%>"  
               title="alle ore" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      </td>
<%  } else { %>
      <td class="l"></td>
      <td class="l"></td>
<%  }%>
    </tr>
<%
  day++;
}
%>
<!-- CICLO GIORNI SETTIMANA - END -->
</table>


<%
//==============================================================================
// Liberazione anticipata - DL 92
//==============================================================================
%>
<br>
<table id="tabLA" cellspacing="2" cellpadding="4" width="800px">
  <tr>
    <td class="Titolo" style="text-align:left" colspan="6" id="tabLAText">
      <a href="Javascript:espandi('tabLA');"><img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0"/></a>
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
    <td class="l" nowrap>
     <input type="checkbox"      
         name="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_LIBANT%>" 
         id="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_LIBANT%>" 
         value="<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_LIBANT_VAL%>" 
         title="Liberazione Anticipata"
         <%=lPenaRideterminataCumulo.isLibAnt()?"checked":""%>
         onClick="Javascript:checkAbilitaDisabilitaCampi(this);"> 
      <font class="label">Totale Liberazione Anticipata (giorni):</font>
    </td>
    <td class="c">
      <input type="text" size="4" maxlength="4" 
             name="<%=ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_LA %>"  
             value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniLA()) %>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
    </td>
    <td class="c">
      <input type="text" size="4" maxlength="4" 
             name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_LS %>" 
             value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniLS()) %>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
    </td>
    <td class="c">
      <input type="text" size="4" maxlength="4" 
             name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_LI %>"
             value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniLI()) %>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
    </td>
    <td class="c">
      <input type="text" size="4" maxlength="4"  
             name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_RIDUZIONE %>"         
             value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniRiduzione()) %>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
    </td>
    <td class="c">
      <input type="text" size="4" maxlength="4"  
             name="<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_SCOMPUTO %>"         
             value="<%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniScomputo()) %>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
    </td>
  </tr>
</table>


<%
//==============================================================================
// Pena da conversione Pena Pecuniaria
//==============================================================================
%>
<br>
<table id="tabPCPP" cellspacing="2" cellpadding="4" width="800px">
  <tr>
    <td class="Titolo" style="text-align:left" colspan="2" >
      <a href="Javascript:espandi('tabPCPP');"><img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0"/></a>
      Pena da conversione Pena Pecuniaria
    </td>
  </tr>
  <tr>
    <td class="l" colspan="1" style="width:250px">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LAVSOST_PP%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LAVSOST_PP%>" 
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LAVSOST_PP%>"
             <%=lUltSan_LavSost_ConvPP.isDurata()?"checked":""%>
             title="Lavoro Sostitutivo"
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
    Lavoro Sostitutivo
    </td>
    <td class="l" colspan=1>
      Anni&nbsp;<input type="text" Title="Anni Lavoro Sostitutivo" maxlength="2" size="2"
                       name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_LAVSOST_PP%>" 
                       id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_LAVSOST_PP%>"
                       value="<%=StringUtils.toStringJSP(lUltSan_LavSost_ConvPP.getNumAnni()) %>"    
                       onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;<input type="text" Title="Mesi Lavoro Sostitutivo" maxlength="2" size="2" 
                       name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_LAVSOST_PP%>" 
                       id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_LAVSOST_PP%>"
                       value="<%=StringUtils.toStringJSP(lUltSan_LavSost_ConvPP.getNumMesi()) %>"    
                       onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;<input type="text" Title="Giorni Lavoro Sostitutivo" maxlength="4" size="4" 
                         name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_LAVSOST_PP%>" 
                         id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_LAVSOST_PP%>"
                         value="<%=StringUtils.toStringJSP(lUltSan_LavSost_ConvPP.getNumGiorni()) %>"    
                         onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>


  <tr>
    <td class="l" colspan=1 style="width:250px">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LIBCTRL_PP%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LIBCTRL_PP%>" 
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LIBCTRL_PP%>"
             <%=lUltSan_LibCtrl_ConvPP.isDurata()?"checked":""%>
             title="Libertà Controllata"
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
     Libertà controllata
    </td>
    <td class="l" colspan=1>
      Anni&nbsp;<input type="text" Title="Anni Libertà Controllata" maxlength="2" size="2"
                       name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_LIBCTRL_PP%>" 
                       id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_LIBCTRL_PP%>"
                       value="<%=StringUtils.toStringJSP(lUltSan_LibCtrl_ConvPP.getNumAnni()) %>"    
                       onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;<input type="text" Title="Mesi Libertà Controllata" maxlength="2" size="2" 
                       name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_LIBCTRL_PP%>" 
                       id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_LIBCTRL_PP%>"
                       value="<%=StringUtils.toStringJSP(lUltSan_LibCtrl_ConvPP.getNumMesi()) %>"    
                       onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;<input type="text" Title="Giorni Libertà Controllata" maxlength="4" size="4" 
                         name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_LIBCTRL_PP%>" 
                         id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_LIBCTRL_PP%>"
                         value="<%=StringUtils.toStringJSP(lUltSan_LibCtrl_ConvPP.getNumGiorni()) %>"    
                         onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
  <tr><td></td></tr>
</table>

<%
//==============================================================================
// Sanzioni del giudice di pace
//==============================================================================
%>
<br>
<table id="tabSGDP" cellspacing="2" cellpadding="4" width="800px">
  <tr>
    <td class="Titolo" style="text-align:left" colspan="2" >
      <a href="Javascript:espandi('tabSGDP');"><img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0"/></a>
      Sanzioni del giudice di pace&nbsp;&nbsp;
    </td>
  </tr>

  <tr>
    <td class="l" style="width:250px">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_PERMANENZA_GP%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_PERMANENZA_GP%>" 
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_PERMDOM_GP%>" 
             title="Permanenza Domiciliare"
             <%=lUltSan_PermDom_GP.isDurata()?"checked":""%>
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Permanenza Domiciliare
    </td>
    <td class="l" >      
      Anni&nbsp;<input type="text" Title="Anni Permanenza Domiciliare" maxlength="2" size="2"
                       name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_PERMDOM_GP%>" 
                       id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_PERMDOM_GP%>"
                       value="<%=StringUtils.toStringJSP(lUltSan_PermDom_GP.getNumAnni()) %>"    
                       onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;<input type="text" Title="Mesi Permanenza Domiciliare" maxlength="2" size="2" 
                       name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_PERMDOM_GP%>" 
                       id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_PERMDOM_GP%>"
                       value="<%=StringUtils.toStringJSP(lUltSan_PermDom_GP.getNumMesi()) %>"    
                       onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;<input type="text" Title="Giorni Permanenza Domiciliare" maxlength="4" size="4" 
                         name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_PERMDOM_GP%>" 
                         id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_PERMDOM_GP%>"
                         value="<%=StringUtils.toStringJSP(lUltSan_PermDom_GP.getNumGiorni()) %>"    
                         onkeypress="return TicTabNumField(this,event)">    
    </td>
  </tr>

  <tr>
    <td class="l" style="width:250px">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LPU_GP%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LPU_GP%>" 
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LPU_GP%>"
             title="Lavoro pubblica utilità"
             <%=lUltSan_LPU_GP.isDurata()?"checked":""%>
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Lavoro pubblica utilità
    </td>
    <td class="l" >      
      Anni&nbsp;<input type="text" Title="Anni Lavoro pubblica utilità" maxlength="2" size="2"
                       name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_LPU_GP%>" 
                       id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_LPU_GP%>"
                       value="<%=StringUtils.toStringJSP(lUltSan_LPU_GP.getNumAnni()) %>"    
                       onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;<input type="text" Title="Mesi Lavoro pubblica utilità" maxlength="2" size="2" 
                       name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_LPU_GP%>" 
                       id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_LPU_GP%>"
                       value="<%=StringUtils.toStringJSP(lUltSan_LPU_GP.getNumMesi()) %>"    
                       onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;<input type="text" Title="Giorni Lavoro pubblica utilità" maxlength="4" size="4" 
                         name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_LPU_GP%>" 
                         id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_LPU_GP%>"
                         value="<%=StringUtils.toStringJSP(lUltSan_LPU_GP.getNumGiorni()) %>"    
                         onkeypress="return TicTabNumField(this,event)">    
    </td>
  </tr>
    
  <tr>
    <td class="l" style="width:250px">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LAVSOST_GP%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_LAVSOST_GP%>" 
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LAVSOST_GP%>" 
             title="Lavoro sostitutivo"
             <%=lUltSan_LavSost_GP.isDurata()?"checked":""%>
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Lavoro sostitutivo
    </td>
    <td class="l">      
      Anni&nbsp;<input type="text" Title="Anni Lavoro sostitutivo" maxlength="2" size="2"
                       name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_LAVSOST_GP%>" 
                       id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_LAVSOST_GP%>"
                       value="<%=StringUtils.toStringJSP(lUltSan_LavSost_GP.getNumAnni()) %>"    
                       onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;<input type="text" Title="Mesi Lavoro sostitutivo" maxlength="2" size="2" 
                       name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_LAVSOST_GP%>" 
                       id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_LAVSOST_GP%>"
                       value="<%=StringUtils.toStringJSP(lUltSan_LavSost_GP.getNumMesi()) %>"    
                       onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;<input type="text" Title="Giorni Lavoro sostitutivo" maxlength="4" size="4" 
                         name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_LAVSOST_GP%>" 
                         id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_LAVSOST_GP%>"
                         value="<%=StringUtils.toStringJSP(lUltSan_LavSost_GP.getNumGiorni()) %>"    
                         onkeypress="return TicTabNumField(this,event)">    
    </td>
  </tr> 
  
  <% // Espulsione GP %>
  <tr>
    <td class="l">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESP_GP%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESP_GP%>" 
             value="<%=ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_ESP_GP%>" 
             title="Sanzioni del giudice di pace - Espulsione dallo Stato"
             <%=lUltSan_ESP_GP.isEspulsione()?"checked":""%>
             onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      Espulsione dallo Stato
    </td>
    <td class="l">
      <input type="checkbox" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_GP%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_CHECK_ESPULSIONE_PERPETUA_GP%>" 
             value="P"
             title="Sanzioni del giudice di pace - Espulsione Perpetua"
             <%="P".equals(lUltSan_ESP_GP.getFlagEspulPerp())?"checked":""%>
             onClick="Javascript:checkEspulsionePerpetua(this);" >
      Perpetua oppure Temporanea per 
      Anni&nbsp;
      <input type="text" maxlength="2" size="2" title="Anni Espulsione dallo Stato" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_ESP_GP%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_ANNI_ESP_GP%>"
             value="<%=StringUtils.toStringJSP(lUltSan_ESP_GP.getNumAnni()) %>"             
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;
      <input type="text" maxlength="2" size="2" title="Mesi Espulsione dallo Stato" 
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_ESP_GP%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_MESI_ESP_GP%>"
             value="<%=StringUtils.toStringJSP(lUltSan_ESP_GP.getNumMesi()) %>"             
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;
      <input type="text" maxlength="3" size="3" title="Giorni Espulsione dallo Stato"
             name="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_ESP_GP%>" 
             id="<%=ICostantiDatiFinaliUlterioriSanzioni.CAMPO_NUM_GIORNI_ESP_GP%>"
             value="<%=StringUtils.toStringJSP(lUltSan_ESP_GP.getNumGiorni()) %>"             
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
  <tr><td></td></tr>
</table>


<table width="800px">
  <tr>
    <td>
      <input type="button" class="bottone" name="PeneRideterminate" value="Conferma" onClick="eseguiSubmit('submit')">
      &nbsp;
      <% if (modalita.equals(ICostantiModuloCumulo.MODALITA_MODIFICA)) { %>
      <input type="button" class="bottone" name="Annulla" value="Annulla" onClick="eseguiSubmit('annulla')">
      <% } %>
    </td>
  </tr>
</table>


    
  </form>
  
</div>

  <script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("formName");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
</body>

</html>