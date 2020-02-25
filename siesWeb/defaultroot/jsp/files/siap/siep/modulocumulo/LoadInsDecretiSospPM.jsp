<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="java.util.Vector"%> 
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiNotificaCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.NotificaCumuloModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>


<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="TipoSosp"     scope="request" class="java.lang.String"/>
<jsp:useBean id="DescTipoSosp" scope="request" class="java.lang.String"/>
<jsp:useBean id="idProvvMod"   scope="request" class="java.lang.String"/>
<jsp:useBean id="flagStato"    scope="request" class="java.lang.String"/>

<jsp:useBean id="aProvvedimentoSosp" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aProvvedimentoRev"  scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aProvvedimentoSorv" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>

<%// Sospensione Simeone%>
<jsp:useBean id="aVerbaleVaneRicerche"   scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aDecretoIrreperibilita" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aIstanzaSimeone"        scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aDecretoRevoca"         scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aProvvRevoca"           scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aOrdinanzaRevocaC5"     scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>


<jsp:useBean id="tipoAutorita"    scope="request" class="java.lang.String"/>
<jsp:useBean id="comboUfficiSorv" scope="request" class="java.lang.String"/>
<jsp:useBean id="comboUfficiUDS"  scope="request" class="java.lang.String"/>
<jsp:useBean id="comboUfficiTDS"  scope="request" class="java.lang.String"/>
  
  
<jsp:useBean id="comboIstanzaPerDep"  scope="request" class="java.lang.String"/>
<jsp:useBean id="comboContenutoIstanza"  scope="request" class="java.lang.String"/>

  
  
<jsp:useBean id="comboTipoProvvC5"    scope="request" class="java.lang.String"/>
<jsp:useBean id="comboTipoProvvC5Rev" scope="request" class="java.lang.String"/>
<jsp:useBean id="comboMotivoRevC5"    scope="request" class="java.lang.String"/>
<jsp:useBean id="comboMotivoRevPMC5"  scope="request" class="java.lang.String"/>
<jsp:useBean id="comboMotivoRevTDSC5" scope="request" class="java.lang.String"/>
  

<jsp:useBean id="comboTipoProvv78" scope="request" class="java.lang.String"/>

<jsp:useBean id="comboTipoProvv199Conc" scope="request" class="java.lang.String"/> 
<jsp:useBean id="comboTipoProvv199Rev"  scope="request" class="java.lang.String"/>
<jsp:useBean id="comboTipoProvv199Sorv" scope="request" class="java.lang.String"/>


<% 
//============================================================================== 
// Form per l'inserimento e la modifica dei Decreti di sospensione
//============================================================================== 
%> 

<html>
<head>
  <title> Gestione Decreti di Sospensione Cumulo </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript" >
    var sysDate = "<%=DateUtils.getSysDate("dd/MM/yyyy")%>";
    
    
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?Action=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    //==========================================================================
    // 
    //==========================================================================
    function eseguiFunzione(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }
    
    function visualizzaDivProvv()
    {
      var tipoSosp = $('#<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_TIPO_SOSP%>').val();
      
      if (tipoSosp=='<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5%>') {   
        AbilitaDisabilitaSezione('Div_Simeone','show');
        AbilitaDisabilitaSezione('Div_78_2013','hide');
        AbilitaDisabilitaSezione('Div_199_2010','hide');       
      }
      else if (tipoSosp=='<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_78%>') {
        AbilitaDisabilitaSezione('Div_Simeone','hide');
        AbilitaDisabilitaSezione('Div_78_2013','show');
        AbilitaDisabilitaSezione('Div_199_2010','hide');        
      }
      else if (tipoSosp=='<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_199%>') {
        AbilitaDisabilitaSezione('Div_Simeone','hide');
        AbilitaDisabilitaSezione('Div_78_2013','hide');
        AbilitaDisabilitaSezione('Div_199_2010','show');      
      } 
      else {
        AbilitaDisabilitaSezione('Div_Simeone','hide');
        AbilitaDisabilitaSezione('Div_78_2013','hide');
        AbilitaDisabilitaSezione('Div_199_2010','hide');     
      }
    }
    
    function AbilitaDisabilitaSezione (divName, operation) {
      if (operation=='hide'){
        $('#'+divName+'').hide();
        
        $('#'+divName+' input').prop('disabled',true);
        $('#'+divName+' select').prop('disabled',true);
        $('#'+divName+' textarea').prop('disabled',true);
      }
      else if (operation=='show'){
        $('#'+divName+'').show();
        $('#'+divName+' input').prop('disabled',false);
        $('#'+divName+' select').prop('disabled',false);
        $('#'+divName+' textarea').prop('disabled',false);
      }        
    }
    
    
    function gestioneTipoIstanza()
    {    
       var nodeper = document.getElementById('divpervenuta'); 
       var nodedep = document.getElementById('divdepositata');  
                   
       if (document.formName.FlagPresdep[0].checked == true)
       {
         nodeper.style.display='block';
         nodedep.style.display='none';
       
         document.formName.GiornoDataIstanza.disabled=false;
         document.formName.MeseDataIstanza.disabled=false;
         document.formName.AnnoDataIstanza.disabled=false;
         document.formName.CodAutoritaMittente.disabled=false; 
         document.formName.CodSedeMittente.disabled=false;

         document.formName.GiornoDataDeposito.disabled=true;
         document.formName.MeseDataDeposito.disabled=true;
         document.formName.AnnoDataDeposito.disabled=true;
         document.formName.SoggPresentante.disabled=true; 
         document.formName.SoggPresentanteIdentificato.disabled=true;           
         document.formName.AvvIdAvvocatoPresentante.disabled=true;           
       }
       else if (document.formName.FlagPresdep[1].checked == true)
       {
          nodedep.style.display='block';
          nodeper.style.display='none';

          document.formName.GiornoDataIstanza.disabled=true;
          document.formName.MeseDataIstanza.disabled=true;
          document.formName.AnnoDataIstanza.disabled=true;
          document.formName.CodAutoritaMittente.disabled=true; 
          document.formName.CodSedeMittente.disabled=true;
          
          document.formName.GiornoDataDeposito.disabled=false;
          document.formName.MeseDataDeposito.disabled=false;
          document.formName.AnnoDataDeposito.disabled=false;
          document.formName.SoggPresentante.disabled=false; 
          document.formName.SoggPresentanteIdentificato.disabled=false;           
          document.formName.AvvIdAvvocatoPresentante.disabled=false;           
       }
    } 
    
    function comboMotivoRevoca(combo) {
      var motivo = combo.value;
      
      if (motivo=='0003') {
        // Omessa Istanza - nessun dato aggiuntivo
        AbilitaDisabilitaSezione('TR_Combo_Motivo_PM','hide');
        AbilitaDisabilitaSezione('TR_Motivazioni_PM','hide');
        AbilitaDisabilitaSezione('tabOrdRigC5','hide');        
      } else if (motivo=='0002') {
        // Reiezione Istanza - Visualizzo la sezione con i dati del tribunale di sorveglianza
        AbilitaDisabilitaSezione('TR_Combo_Motivo_PM','hide');
        AbilitaDisabilitaSezione('TR_Motivazioni_PM','hide');
        <% if (modalita.equals("I") ) { %>
        AbilitaDisabilitaSezione('tabOrdRigC5','show');
        <% } %>      
      } else if (motivo=='0001') {
        // Revoca del PM - Visualizzo motivo revoca
        AbilitaDisabilitaSezione('TR_Combo_Motivo_PM','show');
        AbilitaDisabilitaSezione('TR_Motivazioni_PM','show');
        AbilitaDisabilitaSezione('tabOrdRigC5','hide');          
      } else if (motivo=='-') {
        // 
        AbilitaDisabilitaSezione('TR_Combo_Motivo_PM','hide');
        AbilitaDisabilitaSezione('TR_Motivazioni_PM','hide');
        AbilitaDisabilitaSezione('tabOrdRigC5','hide');           
      }
    }
    
    function Verify() { 
      // Aggiungere i controlli sui campi
      
      var tipoSosp = document.formName.<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_TIPO_SOSP%>.value;
      var tipoOper = '<%=modalita%>';
      
      if (tipoSosp=='<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5%>') {
        // Sosp Comma 5 - Potenzialmente 6 provvedimenti
        // In inserimento sono presenti 5 sezioni, in modifica solo 1
        if ($('#tabOESosp').is(":visible")){
          var codMotivo = $('#tabOESosp [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>]').val();
          
          if (codMotivo=='-' && tipoOper=='M' ){
            alert ("Indicare il tipo di provvedimento");
            $('#tabOESosp [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>]').focus();
            return false;
          }
          
          // Data emissione provv //Modificato il 18/03/2019
          var giorno = $('#tabOESosp [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>]').val();
          var mese   = $('#tabOESosp [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>]').val();
          var anno   = $('#tabOESosp [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>]').val();
          var data_to_verify = giorno+'/'+mese+'/'+anno;
          						
		  checkData = ControllaData (data_to_verify);
          						
          if (!checkData){
              alert('Data di Emissione non valida');
              $('#tabOESosp [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>]').focus();
            return false;
          }
          

          // data notifica avv
          if (!checkData( $('#tabOESosp [name=<%=ICostantiNotificaCumulo.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_C5_AVV]')
                         ,$('#tabOESosp [name=<%=ICostantiNotificaCumulo.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>_C5_AVV]')
                         ,$('#tabOESosp [name=<%=ICostantiNotificaCumulo.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_C5_AVV]')
                         ,true, true, 'notifica Avvocato'
                         ))
            return false;
            
          // data notifica condannato
          if (!checkData( $('#tabOESosp [name=<%=ICostantiNotificaCumulo.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_C5_COND]')
                         ,$('#tabOESosp [name=<%=ICostantiNotificaCumulo.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>_C5_COND]')
                         ,$('#tabOESosp [name=<%=ICostantiNotificaCumulo.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_C5_COND]')
                         ,true, true, 'notifica Condannato'
                         ))
            return false;
          
          contaVal = contaCampiValorizzati ('tabOESosp');
          if (codMotivo=='-' && contaVal>1) {
            alert("Verificare la sezione con i dati del decreto di sospensione. E' stata indicata la data emissione o i dati delle notifiche ma non il tipo di provvedimento");
            $('#tabOESosp [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>]').focus();
            return false;            
          }
        }
        
        if ($('#trVVR').is(":visible")){
          var tipoAutVVR = $('#trVVR [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_AUTORITA_EMITTENTE%>_VVR]').val();
          var sedeAutVVR = $('#trVVR [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_DESC_LUOGO_EMITTENTE%>_VVR]').val();
          
          if (tipoAutVVR=='-' && tipoOper=='M' ){
            alert ("Indicare il tipo di Autorità");
            $('#trVVR [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_AUTORITA_EMITTENTE%>_VVR]').focus();
            return false;
          }
          
          if (  (tipoAutVVR=='-' && sedeAutVVR!='')
              ||(tipoAutVVR!='-' && sedeAutVVR=='')
             )
          {
            alert ("Dati Autorità del Verbale vane ricerche incompleti. Indicare il tipo di Autorità e la Sede");
            $('#trVVR [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_AUTORITA_EMITTENTE%>_VVR]').focus();
            return false;            
          }
           
          
          // data verbale
          if (!checkData( $('#trVVR [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_VVR]')
                         ,$('#trVVR [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>_VVR]')
                         ,$('#trVVR [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>_VVR]')
                         ,true, true, 'Verbale Vane Ricerche'
                         ))
            return false;
            
          contaVal = contaCampiValorizzati ('trVVR');
          if (tipoAutVVR=='-' && contaVal>1) {
            alert("Verificare la sezione con i dati del Verbale Vane Ricerche. E' stata indicata la data Verbale ma non i dati dell'Autorità emittente");
            $('#trVVR [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_AUTORITA_EMITTENTE%>_VVR]').focus();
            return false;
          }
        }
        
        if ($('#tabDecIrr').is(":visible")){
          // Data Decreto Irreperibilità
          if (!checkData( $('#tabDecIrr [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_IRR]')
                         ,$('#tabDecIrr [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>_IRR]')
                         ,$('#tabDecIrr [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>_IRR]')
                         ,true, true, 'Decreto Irreperibilità'
                         ))
            return false;
          
          ggData = $('#tabDecIrr [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_IRR]').val();

          if (ggData=='' && tipoOper=='M' ){
            alert ("Indicare la data del Decreto");
            $('#tabDecIrr [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_IRR]').focus();
            return false;
          }
            
        }
        
        if ($('#tabIstanza').is(":visible")){
          flagIstanza = $('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_ISTANZA_PRESDEP%>]').val();
          contenuto   = $('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_CONTENUTO_ISTANZA%>]').val();
          dataIstanza =      $('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_ISTANZA%>]').val()
                        +"/"+$('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_ISTANZA%>]').val()
                        +"/"+$('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_ISTANZA%>]').val();
          tipoUffTrasmIst = $('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>]').val();
          sedeUffTrasmIst = $('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_DESTINATARIO%>]').val();

          dataTrasmIstanza =      $('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_TRASMISSIONE%>]').val()
                             +"/"+$('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_TRASMISSIONE%>]').val()
                             +"/"+$('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_TRASMISSIONE%>]').val();

          
          if (flagIstanza=='-' && tipoOper=='M' ){
            alert ("Indicare lo stato dell'Istanza");
            $('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_ISTANZA_PRESDEP%>]').focus();
            return false;
          }
          
          if (!checkData( $('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_ISTANZA%>]')
                         ,$('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_ISTANZA%>]')
                         ,$('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_ISTANZA%>]')
                         ,true, true, 'presentazione Istanza'
                         ))
            return false;
          
          contaVal = 0;
          if (flagIstanza!='-')  contaVal++;
          if (contenuto!='-')    contaVal++;
          if (dataIstanza!='//') contaVal++;

          if (contaVal>0 && contaVal<3){
            alert ("Dati dell'Istanza incompleti. Se Istanza presente vanno valorizzati i seguenti campi: presentata/depositata, in data, contenuto.");
            $('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_ISTANZA_PRESDEP%>]').focus();
            return false;
          }
          if (contaVal==0 && (tipoUffTrasmIst!='-' || sedeUffTrasmIst!=''|| dataTrasmIstanza!='//')){
            alert ("Dati dell'Istanza incompleti. Se Istanza presente vanno valorizzati i seguenti campi: presentata/depositata, in data, contenuto.");
            $('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_ISTANZA_PRESDEP%>]').focus();
            return false;
          }
          
          contaVal = 0;
          if (tipoUffTrasmIst!='-')   contaVal++;
          if (sedeUffTrasmIst!='')    contaVal++;
          if (dataTrasmIstanza!='//') contaVal++;          
          if (contaVal>0 && contaVal<3){
            alert ("Se Istanza trasmessa indicare i dati in modo completo: Ufficio, Sede e Data");
            $('#tabIstanza [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>]').focus();
            return false;
          }
        }
        
        //
        if ($('#tabRevoca').is(":visible")){
          motivoProvv   = $('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_C5_REV]').val();
          dataEmissione =      $('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_C5_REV]').val()
                          +"/"+$('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>_C5_REV]').val()
                          +"/"+$('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>_C5_REV]').val();
          motivoRevoca    = $('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO_REVOCA%>]').val();
          motivoRevocaPM  = $('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO_REVOCA_PM%>]').val();

          if (motivoProvv=='-' && tipoOper=='M' ){
            alert ("Indicare il tipo provvedimento");
            $('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_C5_REV]').focus();
            return false;
          }
          
          if (!checkData( $('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_C5_REV]')
               ,$('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>_C5_REV]')
               ,$('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>_C5_REV]')
               ,true, true, 'provvedimento di revoca'
               ))
            return false; 

          if (motivoProvv=='-' && dataEmissione!='//'){
            alert ("Dati provvedimento di revoca incompleti. Indicare il tipo provvedimento.");
            $('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_C5_REV]').focus();
            return false;            
          }
          if (motivoProvv!='-' && motivoRevoca=='-'){
            alert ("Dati provvedimento di revoca incompleti. Indicare il motivo revoca.");
            $('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO_REVOCA%>]').focus();
            return false;            
          }
          if (motivoProvv!='-' && motivoRevoca=='0001' && motivoRevocaPM=='-'){
            alert ("Dati provvedimento di revoca incompleti. Indicare il motivo revoca del PM.");
            $('#tabRevoca [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO_REVOCA_PM%>]').focus();
            return false;            
          }
        }
        
        if ($('#tabOrdRigC5').is(":visible")){
          motivoProvvOrd   = $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_C5_REV_ORD]').val();
          
          if (motivoProvvOrd=='-' && tipoOper=='M' ){
            alert ("Indicare l'Oggetto dell'Ordinanza");
            $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_C5_REV_ORD]').focus();
            return false;
          }

          if (!checkData( $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_C5_REV_ORD]')
               ,$('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>_C5_REV_ORD]')
               ,$('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>_C5_REV_ORD]')
               ,true, true, 'emissione Ordinanza'
               ))
            return false; 
          
          
          tipoUffOrd = $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE%>_C5_REV_ORD]').val();
          sedeUffOrd = $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_DESC_LUOGO_EMITTENTE%>_C5_REV_ORD]').val();
          if (motivoProvvOrd!='-' && (tipoUffOrd=='-' || sedeUffOrd=='') ){
            alert("Verificare la sezione con i dati dell'Ordinanza di rigetto. Dati incompleti. Indicare l'Ufficio emittente.");
            $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE%>_C5_REV_ORD]').focus();
            return false;
          }
          
          dataEmissione =      $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_C5_REV_ORD]').val()
                          +"/"+$('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>_C5_REV_ORD]').val()
                          +"/"+$('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>_C5_REV_ORD]').val();


          contaVal = contaCampiValorizzati ('tabOrdRigC5');
          //alert ("contaVal = "+contaVal);
          if (motivoProvvOrd=='-' && contaVal>1) {
            alert("Verificare la sezione con i dati dell'Ordinanza di rigetto. Dati incompleti. Se presente l'Ordinanza vanno indicati almeno l'Oggetto e l'Ufficio emittente.");
            $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_C5_REV_ORD]').focus();
            return false;
          }
          
          annoSIUS = $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>_C5_REV_ORD]').val();
          numSIUS  = $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>_C5_REV_ORD]').val();
          
          if ( (annoSIUS!='' && numSIUS=='') || (annoSIUS=='' && numSIUS!='') ){
            alert("Indicare correttamente gli estremi del Procedimento: Anno/Numero SIUS");
            $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>_C5_REV_ORD]').focus();
            return false;            
          }
          
          annoORD = $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>_C5_REV_ORD]').val();
          numORD  = $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>_C5_REV_ORD]').val();
          if ( (annoORD!='' && numORD=='') || (annoORD=='' && numORD!='') ){
            alert("Indicare correttamente gli estremi dell'Ordinanza: Anno/Numero Ordinanza");
            $('#tabOrdRigC5 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>_C5_REV_ORD]').focus();
            return false;
          }
          
        }
        
        
      }
      else if (tipoSosp=='<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_78%>') {
        // DL 78/2013
        // TipoProvv obbligatorio
        
        var codMotivo = $('#Div_78_2013 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>]').val();
        
        //alert ("codMotivo = "+codMotivo);
        if (codMotivo=='-'){
          alert ("Indicare il tipo di provvedimento");
          $('#Div_78_2013 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>]').focus();
          return false;
        }
        
        // Data emissione provv //Modificato il 18/03/2019
        var giorno = $('#Div_78_2013 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>]').val();
        var mese   = $('#Div_78_2013 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>]').val();
        var anno   = $('#Div_78_2013 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>]').val();
        var data_to_verify = giorno+'/'+mese+'/'+anno;
        						
		  checkData = ControllaData (data_to_verify);
        						
        if (!checkData){
            alert('Data di Emissione non valida');
            $('#Div_78_2013 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>]').focus();
          return false;        
        }

        var tipoUffSorv = $('#Div_78_2013 [name=<%=ICostantiNotificaCumulo.CAMPO_COD_TIPO_UFF_NOT%>_78]').val();
        var sedeUffSorv = $('#Div_78_2013 [name=<%=ICostantiNotificaCumulo.CAMPO_SEDE_UFF_NOT%>_78]').val();
        
        if (   (tipoUffSorv!='-' && $.trim(sedeUffSorv)=='')
            || (tipoUffSorv=='-' && $.trim(sedeUffSorv)!='')
           ) 
        {
          alert('Dati Ufficio di Sorveglianza incompleti');
          $('#Div_78_2013 [name=<%=ICostantiNotificaCumulo.CAMPO_COD_TIPO_UFF_NOT%>_78]').focus();
          return false;          
        }
       
        if (!checkData( $('#Div_78_2013 [name=<%=ICostantiNotificaCumulo.CAMPO_GIORNO_DATA_INVIO%>_78]')
                       ,$('#Div_78_2013 [name=<%=ICostantiNotificaCumulo.CAMPO_MESE_DATA_INVIO%>_78]')
                       ,$('#Div_78_2013 [name=<%=ICostantiNotificaCumulo.CAMPO_ANNO_DATA_INVIO%>_78]')
                       ,true, true, 'invio notifica'
                       ))
          return false;
      }
      else if (tipoSosp=='<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_199%>') {
        // 199/2010
        // In inserimento sono presenti tre sezioni, in modifica solo 1

        
        // Provv Concessione
        if ($('#tabProvvConc199').is(":visible")){
          var codMotivo = $('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>]').val();
  
          if (codMotivo=='-' && tipoOper=='M' ){
            alert ("Indicare il tipo di provvedimento");
            $('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>]').focus();
            return false;
          }
          
          // Data emissione provv //Modificato il 18/03/2019
          var giorno = $('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>]').val();
          var mese   = $('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>]').val();
          var anno   = $('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>]').val();
          var data_to_verify = giorno+'/'+mese+'/'+anno;
          						
  		  checkData = ControllaData (data_to_verify);
          						
          if (!checkData){
              alert('Data di Emissione non valida');
              $('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>]').focus();
            return false;          
        }
        
        }
        
        // Revoca 199
        if ($('#tabProvvRevoca199').is(":visible")){
          var objCodMotivo = $('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_REV]');
  
          if (objCodMotivo.val()=='-' && tipoOper=='M' ){
            alert ("Indicare il tipo di provvedimento");
            objCodMotivo.focus();
            return false;
          }
          
          if (!checkData( $('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_REV]')
                         ,$('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>_REV]')
                         ,$('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>_REV]')
                         ,true, true, 'emissione provvedimento'
                         ))
            return false;
        }

        // Ordinanza 199
        if ($('#tabOrdinanzaRigetto199').is(":visible")){
          // Dati obbligatori Ufficio e Oggetto
          
          var objCodMotivo = $('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_SORV]');
          var objTipoUff   = $('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE%>_199]');
          var objSedeUff   = $('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_DESC_LUOGO_EMITTENTE%>_199]');
          
          // verifico se presenti campi valorizzati, in questo caso sono obbligatori
          if (objCodMotivo.val()=='-' && tipoOper=='M' ){
            alert ("Indicare il tipo di provvedimento");
            objCodMotivo.focus();
            return false;
          }

          contaVal = contaCampiValorizzati ('tabOrdinanzaRigetto199');
          if (objCodMotivo.val()!="-") {
            // Verifico ufficio e sede obbligatori
            if (objTipoUff.val()=='-'){
              alert("Indicare l'Ufficio emittente l'ordinanza di rigetto");
              objTipoUff.focus();
              return false;
            }
            if (objSedeUff.val()==''){
              alert("Indicare la Sede Ufficio emittente l'ordinanza di rigetto");
              objSedeUff.focus();
              return false;
            }
            
            if (!checkData( $('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_SORV]')
                           ,$('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>_SORV]')
                           ,$('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>_SORV]')
                           ,true, true, 'emissione ordinanza'
                           ))
              return false; 
          }
          else if (contaVal>0){
            alert("Nella sezione dell'ordinanza di Rigetto vanno indicati almeno l'Oggetto Ordinanza e la l'Ufficio emittente, altrimenti lasciare vuoti tutti i campi");
            objCodMotivo.focus();
            return false;
          }
        }
        
        //
      }
      
      return true; 
    } 
    
    function checkData (campoGG, campoMM, campoAA, consentiVuota, setFocus, tipoDataTxt){
      var giorno = campoGG.val();
      var mese   = campoMM.val();
      var anno   = campoAA.val();
      var dataDaControllare = giorno+'/'+mese+'/'+anno;
      //alert ("dataDaControllare "+dataDaControllare);
      
      
      if (consentiVuota){
        if (! ControllaDataPassaVuota(dataDaControllare))
        {
          alert ("Data "+tipoDataTxt+" non valida");
          if (setFocus) campoGG.focus();
          return false;
        }        
      }
      else {
        if (! ControllaData(dataDaControllare))
        {
          alert ("Data "+tipoDataTxt+" non valida");
          if (setFocus) campoGG.focus();
          return false;
        }        
      }
      if (dataDaControllare!="//"){
        if (!CompareDate(dataDaControllare,sysDate)){
          alert('La data '+tipoDataTxt+' non può essere una data futura');
          if (setFocus) campoGG.focus();
          return false;
        }
      }      
      
      return true;      
    }
    
    
    function contaCampiValorizzati (idContenitore) {
      var contaVal = 0;
      
      var campiInput = $('#'+idContenitore+' input[type=text]');
      var contaVal = 0;
      campiInput.each ( function (index) {
          if ($(this).val()!="") {
            //alert( $(this).attr('name')+" = "+$(this).val() );
            contaVal++;
          }
        }
      );
      
      // combo
      var campiCombo = $('#'+idContenitore+' select');
      campiCombo.each ( function (index) {
          if ($(this).val()!="-") {
            //alert( $(this).attr('name')+" = "+$(this).val() );
            contaVal++;
          }
        }
      );

      var campiTextarea = $('#'+idContenitore+' textarea');
      campiTextarea.each ( function (index) {
          if ($(this).val()!="") {
            //alert( $(this).attr('name')+" = "+$(this).val() );
            contaVal++;
          }
        }
      );
      
      return contaVal;  
    }
    
    // On load
    $(document).ready(function(){
      $('#<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_TIPO_SOSP%>').val('<%=TipoSosp%>');
      visualizzaDivProvv();
      //alert("1");
      
      <% if (modalita.equals("M") ) { %>
        if ($('#Div_Simeone [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>]').val()=='-')
          AbilitaDisabilitaSezione('tabOESosp','hide');
          
        if ($('#Div_Simeone [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_AUTORITA_EMITTENTE%>_VVR]').val()=='-'){
          AbilitaDisabilitaSezione('trTitoloVVR','hide');
          AbilitaDisabilitaSezione('trVVR','hide');
        }
        
        if ($('#Div_Simeone [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_IRR]').val()==''){
          AbilitaDisabilitaSezione('trTitoloIRR','hide');
          AbilitaDisabilitaSezione('tabDecIrr','hide');
        }
                 
        if ($('#Div_Simeone [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_ISTANZA_PRESDEP%>]').val()=='-')
          AbilitaDisabilitaSezione('tabIstanza','hide');
          
        if ($('#Div_Simeone [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_C5_REV]').val()=='-')
          AbilitaDisabilitaSezione('tabRevoca','hide');
        else {
//          $('#Div_Simeone [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO_REVOCA%>]').trigger("change");
          $('#Div_Simeone [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO_REVOCA%>]').change();
        }
        
        
        if ($('#Div_Simeone [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_C5_REV_ORD]').val()!='-')
          AbilitaDisabilitaSezione('tabOrdRigC5','show');
        
        //
        if ($('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>]').val()=='-')
          AbilitaDisabilitaSezione('tabProvvConc199','hide');
        if ($('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_REV]').val()=='-')
          AbilitaDisabilitaSezione('tabProvvRevoca199','hide');
        if ($('#Div_199_2010 [name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_SORV]').val()=='-')
          AbilitaDisabilitaSezione('tabOrdinanzaRigetto199','hide');
      <% } %>
    });    
        
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;

        <% if (modalita.equals("I") ) { %>
        <font class="campo">Inserimento Decreto di Sospensione &nbsp;</font>
        <% } else if( modalita.equals("M") ) { %>
        <font class="campo">Modifica Decreto di Sospensione &nbsp;</font>
        <% } %>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaDecretiSospPM')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciDecretiSospPM">
  
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">

  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="HIDDEN" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>" value="<%=idProvvMod%>">
  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"					value="<%=flagStato%>">
                 

  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td class="l" width="150px">Tipologia Sospensione: </td>
      <td class="l" colspan="2">
        <% if (modalita.equals("I") ) { %>
        <select Title="Tipo Decreto" name="<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_TIPO_SOSP%>" 
                id="<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_TIPO_SOSP%>"
                onChange="javascript:visualizzaDivProvv();">
          <option value="-" />-
          <option value="<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5%>" /><%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5_DESC%>
          <option value="<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_78%>" /><%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_78_DESC%>
          <option value="<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_199%>" /><%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_199_DESC%>
        </select>
        <% } else if( modalita.equals("M") ) { %>
          <font class="campo"><%=DescTipoSosp%></font>
          <input type="HIDDEN" name="<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_TIPO_SOSP%>" 
           id="<%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_TIPO_SOSP%>" value="<%=TipoSosp%>">
        <% } %>        
      </td>
    </tr>
  </table>

  <%
  //============================================================================
  // DIV con i dati della Sospensione esecuzione ex art. 656 c.p.p (Simeone)  
  //============================================================================
  %>
  <div id="Div_Simeone" style="display:none">
    <table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabOESosp">
      <tr>
       <td class="titolo" colspan="4" width="95%">Dati Provvedimento - Sospensione esecuzione ex art. 656 c.p.p</td>    
      </tr>
      <tr>
        <td class="l">Ordine Esecuzione: </td>
        <td class="l" colspan="1">
          <select Title="Tipo Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>" onChange="javascript:null;">
            <option value = "-" />-
            <%=comboTipoProvvC5%>
            <!--
            <option value = "0061" />con contestuale Sospensione - Libero
            <option value = "0104" />con contestuale Sospensione - Libero - Istanza
            <option value = "0063" />con contestuale Sospensione - Arresti domiciliari
            <option value = "0117" />con contestuale Sospensione - Detenuto altra causa
            -->
          </select>
        </td>
        
        <td class="l">Data Emissione</td>
        <td class="l">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE %>"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoSosp.getDataEmissione(),"dd"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE %>"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoSosp.getDataEmissione(),"MM"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE %>" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoSosp.getDataEmissione(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      
      <% // DATI DELLE NOTIFICHE %>
      <tr>
        <td class="titolo" colspan="4" width="95%">Notifiche</td>    
      </tr>
      <%
      Vector <NotificaCumuloModel> lListaNotificheSimeone = aProvvedimentoSosp.getListaNotifiche();
      
      NotificaCumuloModel lNotificaAvv = new NotificaCumuloModel();
      NotificaCumuloModel lNotificaCond = new NotificaCumuloModel();
      if (lListaNotificheSimeone!=null) {
        Iterator itxNotSimeone = lListaNotificheSimeone.iterator();
        while (itxNotSimeone.hasNext()){
          NotificaCumuloModel lNot = (NotificaCumuloModel) itxNotSimeone.next();
          if ("N".equals(lNot.getCodTipoNotifica()))
            lNotificaAvv = lNot;
          else if ("E".equals(lNot.getCodTipoNotifica()))
            lNotificaCond = lNot;          
        }
      }
      %>
      <tr>
        <td class="l">Avvocato</td>
        <td class="l">&nbsp;</td>
        
        <td class="l">Data Notifica</td>
        <td class="l">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiNotificaCumulo.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_C5_AVV"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (lNotificaAvv.getDataAvvenutaNotifica(),"dd"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiNotificaCumulo.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>_C5_AVV"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (lNotificaAvv.getDataAvvenutaNotifica(),"MM"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiNotificaCumulo.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_C5_AVV" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (lNotificaAvv.getDataAvvenutaNotifica(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          <input type="hidden" name="<%=ICostantiNotificaCumulo.CAMPO_ID_NOTIFICA_CUMULO%>_C5_AVV" 
                               value="<%=StringUtils.toStringJSP (lNotificaAvv.getIdNotificaCumulo())%>" >
        </td>
      </tr>
      
      <tr>
        <td class="l">Condannato</td>
        <td class="l">&nbsp;</td>
        
        <td class="l">Data Notifica</td>
        <td class="l">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiNotificaCumulo.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_C5_COND"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (lNotificaCond.getDataAvvenutaNotifica(),"dd"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiNotificaCumulo.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>_C5_COND"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (lNotificaCond.getDataAvvenutaNotifica(),"MM"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiNotificaCumulo.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_C5_COND" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (lNotificaCond.getDataAvvenutaNotifica(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          <input type="hidden" name="<%=ICostantiNotificaCumulo.CAMPO_ID_NOTIFICA_CUMULO%>_C5_COND" 
                               value="<%=StringUtils.toStringJSP (lNotificaCond.getIdNotificaCumulo())%>" >
        </td>        
      </tr> 
    </table>
      
    <table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabVVR">
      <% if (modalita.equals("I") ) { %>
      <tr id="trTitolo">
        <td class="titolo" colspan="4" width="95%">Verbale Vane ricerche/Decreto Irreperibilità</td>    
      </tr>
      <% } else { %>
      <tr id="trTitoloVVR">
        <td class="titolo" colspan="4" width="95%">Verbale Vane ricerche</td>    
      </tr>
      <tr id="trTitoloIRR">
        <td class="titolo" colspan="4" width="95%">Decreto Irreperibilità</td>    
      </tr>
      <% } %>
      <tr id="trVVR">
        <td class="l">Verbale Vane ricerche</td>        
        <td class="l">
          &nbsp; Autorità
          <select Title="Autorità" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_AUTORITA_EMITTENTE%>_VVR"  >
            <%=tipoAutorita%>
          </select>
          &nbsp;Sede 
          <input type="text" title="Sede" maxlength="35" size="35"
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_DESC_LUOGO_EMITTENTE%>_VVR"
                 value="<%=StringUtils.toStringJSP (aVerbaleVaneRicerche.getDescrLuogoEmittente())%>"  
                 >
            <a href="Javascript:ListaComuni('formName','<%=ICostantiStatoEsecTitoloCumulato.CAMPO_DESC_LUOGO_EMITTENTE%>_VVR');">
              <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0>
            </a>          
        </td>        
        
        <td class="l">Data Verbale</td>
        <td class="l">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_VVR" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aVerbaleVaneRicerche.getDataEmissione(),"dd"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>_VVR" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aVerbaleVaneRicerche.getDataEmissione(),"MM"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>_VVR" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aVerbaleVaneRicerche.getDataEmissione(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>        
      </tr>
    <!--
    </table>
    
    <table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabDecIrr">
    -->
      <tr id="tabDecIrr">
        <td class="l">Decreto Irreperibilità</td>
        <td class="l">&nbsp;</td>

        <td class="l">Data Emissione</td>
        <td class="l">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_IRR" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aDecretoIrreperibilita.getDataEmissione(),"dd"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>_IRR" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aDecretoIrreperibilita.getDataEmissione(),"MM"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>_IRR" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aDecretoIrreperibilita.getDataEmissione(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>
          
    <% // DATI DELL'ISTANZA %>
    <table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabIstanza">
      <tr>
       <td class="titolo" colspan="4" width="95%">Istanza</td>    
      </tr>
      <tr>
        <td class="l">Istanza</td>
        <td class="l" colspan="1">
          <select Title="TipoIstanza" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_ISTANZA_PRESDEP%>">
            <%=comboIstanzaPerDep%>
          </select>
        </td>
        <td class="l" nowrap>In Data</td>
        <td class="l">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_ISTANZA%>" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aIstanzaSimeone.getDataIstanza(),"dd"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_ISTANZA%>" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aIstanzaSimeone.getDataIstanza(),"MM"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_ISTANZA%>" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aIstanzaSimeone.getDataIstanza(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <tr>
        <td class="l">Contenuto</td>
        <td class="l" colspan="3"> 
          <select name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_CONTENUTO_ISTANZA%>">
            <%=comboContenutoIstanza%>
          </select>
      </tr>
      
      <tr>
        <td class="l">Trasmessa a </td>
        <td class="l">          
          <select Title="Destinatario" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" class="small">
              <option value = "-"  />-
              <%=comboUfficiSorv%>
          </select>
          &nbsp;Sede 
          <input type="text" title="Sede Destinatario"  maxlength="35" size="30"
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_DESTINATARIO%>" 
                 value="<%=StringUtils.toStringJSP (aIstanzaSimeone.getDescrLuogoDestinatario())%>">
            <a href="Javascript:ListaUfficiPerTipo('formName'
                                                  ,'<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_DESTINATARIO%>'
                                                  ,document.formName.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.value);">
              <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0>
            </a>          
        </td>
        <td class="l" nowrap>In Data</td>
        <td class="l">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_TRASMISSIONE%>" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aIstanzaSimeone.getDataTrasmissione(),"dd"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_TRASMISSIONE%>" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aIstanzaSimeone.getDataTrasmissione(),"MM"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_TRASMISSIONE%>" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aIstanzaSimeone.getDataTrasmissione(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>
    
    <%
    //==========================================================================
    //  Revoca del decreto di Sospensione
    //==========================================================================
    %>      
    <table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabRevoca">
      <tr>
       <td class="titolo" colspan="4" width="95%">Provvedimento di Revoca</td>    
      </tr>
      <tr>
        <td class="l">Provvedimento: </td>
        <td class="l" colspan="1">
          <select Title="Tipo Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_C5_REV">
            <%=comboTipoProvvC5Rev%>
          </select>
        </td>
        
        <td class="l">Data Emissione</td>
        <td class="l">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE %>_C5_REV"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvRevoca.getDataEmissione(),"dd"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE %>_C5_REV"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvRevoca.getDataEmissione(),"MM"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE %>_C5_REV" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvRevoca.getDataEmissione(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <tr>
        <td class="l" >Motivo Revoca</td>
        <td class="l" colspan="3">
          <select Title="Motivo Revoca " name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO_REVOCA%>" 
                  onChange="comboMotivoRevoca(this);">
            <option value="-"  />-
            <%=comboMotivoRevC5%>
          </select>
        </td>
      </tr>
      <tr id="TR_Combo_Motivo_PM" style="display:none">
        <td class="l">Motivo Revoca PM</td>
        <td class="L" colspan="3">
          <select  Title="Motivo Revoca PM"  name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO_REVOCA_PM%>">
            <%=comboMotivoRevPMC5%>
          </select>
        </td>
      </tr>
      <tr id="TR_Motivazioni_PM" style="display:none">
        <td  class="l">Motivazioni</td>
        <td  class="L"  colspan="3">
         <TEXTAREA title="Note" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE%>_C5_REV" cols="80" rows="2"><%=StringUtils.toStringJSP (aProvvRevoca.getNote())%></textarea>
        </td>
      </tr>
    </table>
    
    <table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabOrdRigC5" style="display:none">
      <tr>
        <td class="Titolo" colspan='8'> Dati Ordinanza del Tribunale di Sorveglianza </td>
      </tr>
      <tr>
        <td class="l">Anno /Numero SIUS</td>
        <td class="l">
          <input type="text" Title="Anno Fascicolo Sius" size="4" maxlength="4"
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>_C5_REV_ORD"   
                 value="<%=StringUtils.toStringJSP (aOrdinanzaRevocaC5.getAnnoProcedimento())%>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          /
          <input type="text" Title="Numero Sius" size="6" maxlength="6" 
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>_C5_REV_ORD"   
                 value="<%=StringUtils.toStringJSP (aOrdinanzaRevocaC5.getProgrProcedimento())%>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="l"> Anno / Numero Ordinanza</td>
        <td class="l">
          <input type="text" Title="Anno Ordinanza" size="4" maxlength="4" 
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>_C5_REV_ORD"   
                 value="<%=StringUtils.toStringJSP (aOrdinanzaRevocaC5.getAnnoProvvedimento())%>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          /
          <input type="text" Title="Numero Ordinanza"  size="6" maxlength="6" 
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>_C5_REV_ORD"   
                 value="<%=StringUtils.toStringJSP (aOrdinanzaRevocaC5.getProgrProvvedimento())%>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
      </tr>
      <tr>
        <td class="l">Ufficio Emittente</td>
        <td class="l" colspan="3">
           <select Title="Tribunale Sorveglianza" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE%>_C5_REV_ORD">
            <option value = "-"  />-
            <%=comboUfficiTDS%>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede Ufficio Emittente</td>
        <td class="l" colspan="3">
          <font class="campo">
            <input type="text" Title="Luogo Ufficio Sorveglianza" size="35"
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_DESC_LUOGO_EMITTENTE%>_C5_REV_ORD"  
                   value="<%=StringUtils.toStringJSP (aOrdinanzaRevocaC5.getDescrLuogoEmittente()) %>" 
                   >
            <a href="Javascript:ListaUfficiPerTipo('formName','<%=ICostantiStatoEsecTitoloCumulato.CAMPO_DESC_LUOGO_EMITTENTE%>_C5_REV_ORD'
                                                   ,document.formName.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE%>_C5_REV_ORD.value);">
              <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0>
            </a>
          </font>
        </td>
      </tr>
      <tr>
        <td class="l">Oggetto Ordinanza</td>
        <td class="L" colspan="3">
          <select  Title="Codice Motivo" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_C5_REV_ORD">
            <%=comboMotivoRevTDSC5%>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Data Emissione Ordinanza </td>
        <td class="l" colspan="3">
          <input type="text" size="2" maxlength="2" 
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>_C5_REV_ORD" 
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aOrdinanzaRevocaC5.getDataEmissione(),"dd"))%>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" size="2" maxlength="2" 
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>_C5_REV_ORD" 
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aOrdinanzaRevocaC5.getDataEmissione(),"MM"))%>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" size="4" maxlength="4" 
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>_C5_REV_ORD" 
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString (aOrdinanzaRevocaC5.getDataEmissione(),"yyyy"))%>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </font>
        </td>
      </tr>
      <tr>
        <td  class="l">Note</td>
        <td  class="L"  colspan="3">
          <TEXTAREA title="Note" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE%>_C5_REV_ORD" cols="80" rows="2"><%=StringUtils.toStringJSP (aOrdinanzaRevocaC5.getNote())%></textarea>
        </td>
      </tr>
    </table>
  </div>
  
  <%
  //============================================================================
  // DIV con i dati della Sospensione DL78/2013  
  //============================================================================
  %>
  <div id="Div_78_2013" style="display:none">
    <table cellspacing="2" cellpadding="2" width="95%" align="center">
      <tr>
       <td class="titolo" colspan="4" width="95%">Dati Provvedimento - DL 78/2013</td>    
      </tr>
      <tr>
        <td class="l">Comunicazione: <font class=ob>(*)</font></td>
        <td class="l" colspan="1">
          <select Title="Tipo Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>" onChange="javascript:null;">
            <option value = "-" />-
            <%=comboTipoProvv78%>
          </select>
        </td>
        
        <td class="l">Data Emissione</td>
        <td class="l">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE %>"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoSosp.getDataEmissione(),"dd"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE %>"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoSosp.getDataEmissione(),"MM"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE %>" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoSosp.getDataEmissione(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>      
      
      <tr>
        <td class="Titolo" colspan="6"> Notifica Sorveglianza</td>
      </tr>
      <%
      NotificaCumuloModel lNotifica78 = new NotificaCumuloModel();
      UfficioModel lUfficioSorv78 = new UfficioModel();
      Vector <NotificaCumuloModel> lListaNotifica78 = aProvvedimentoSosp.getListaNotifiche();
      if (lListaNotifica78!=null && lListaNotifica78.size()>0) {
        lNotifica78 = lListaNotifica78.elementAt(0);
        if (lNotifica78.getUfficio()!=null) lUfficioSorv78 = lNotifica78.getUfficio();
      }
      %>
      <tr>
        <td class="l">Destinatario</td>
        <td class="l" colspan="1">    
          <select Title="Ufficio di Sorveglianza" name="<%=ICostantiNotificaCumulo.CAMPO_COD_TIPO_UFF_NOT%>_78" >
            <option value = "-" />-
            <%=comboUfficiUDS%>
          </select>
          <input type="hidden" name="<%=ICostantiNotificaCumulo.CAMPO_ID_NOTIFICA_CUMULO%>_78" 
                               value="<%=StringUtils.toStringJSP (lNotifica78.getIdNotificaCumulo())%>" >
        </td>
        
        <td class="l">Data Invio</td>
        <td class="l">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiNotificaCumulo.CAMPO_GIORNO_DATA_INVIO %>_78"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lNotifica78.getDataInvio(),"dd"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiNotificaCumulo.CAMPO_MESE_DATA_INVIO %>_78"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lNotifica78.getDataInvio(),"MM"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiNotificaCumulo.CAMPO_ANNO_DATA_INVIO %>_78" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lNotifica78.getDataInvio(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>        
      </tr>

      <tr>
        <td class="L">Sede</td>
        <td class="L">
            <input type="text" maxlength="35" size="35" Title="Sede Ufficio di Sorveglianza" 
                   name="<%=ICostantiNotificaCumulo.CAMPO_SEDE_UFF_NOT%>_78" 
                   value="<%=StringUtils.toStringJSP (lUfficioSorv78.getDescrComune())%>" 
                   >
            <a href="Javascript:ListaUfficiPerTipo('formName','<%=ICostantiNotificaCumulo.CAMPO_SEDE_UFF_NOT%>_78', document.formName.<%=ICostantiNotificaCumulo.CAMPO_COD_TIPO_UFF_NOT%>_78.value);">
              <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0>
            </a>
        </td>
        <td class="l">Note</td>
        <td class="L">
          <TEXTAREA title="Note ufficio sorveglianza" name="<%=ICostantiNotificaCumulo.CAMPO_NOTE%>_78"  cols="35"><%=StringUtils.toStringJSP ( lNotifica78.getNote() ) %></textarea>
        </td>
      </tr>
      
    </table>
  </div>
  
  <%
  //============================================================================
  // DIV con i dati della Sospensione Legge n. 199/2010  
  //============================================================================
  %>
  <div id="Div_199_2010" style="display:none">
    <table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabProvvConc199">
      <tr>
       <td class="titolo" colspan="4" width="95%">Dati Provvedimento - Espiazione Presso il Domicilio (Legge n.199/2010)</td>    
      </tr>
      <tr>
        <td class="l">Provvedimento: </td>
        <td class="l" colspan="1">
          <select Title="Tipo Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>" >
            <option value = "-" />-
            <%=comboTipoProvv199Conc%>
            <!--
            <option value = "0499" />decreto sospensione dell'ordine esecuzione per la carcerazione - Libero (legge n. 199/2010)
            <option value = "0364" />Ordine esecuzione con contestuale Sospensione - Libero (legge n. 199/2010)
            -->
          </select>
        </td>
        
        <td class="l">Data Emissione</td>
        <td class="l">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE %>"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoSosp.getDataEmissione(),"dd"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE %>"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoSosp.getDataEmissione(),"MM"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE %>" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoSosp.getDataEmissione(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>  
    </table>

    
    <table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabProvvRevoca199">
      <tr>
       <td class="titolo" colspan="4" width="95%">Provvedimento di Revoca</td>    
      </tr>
      <tr>
        <td class="l">Tipologia Provvedimento: </td>
        <td class="l" colspan="1">
          <select Title="Tipo Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_REV">
            <option value = "-" />-
            <%=comboTipoProvv199Rev%>
          </select>
        </td>
        
        <td class="l">Data Emissione</td>
        <td class="l">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE %>_REV"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoRev.getDataEmissione(),"dd"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE %>_REV"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoRev.getDataEmissione(),"MM"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE %>_REV" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoRev.getDataEmissione(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>  
      <tr>
        <td class="l">Motivo Revoca</td>
        <td class="l" colspan="3">
          <select  Title="Motivo Revoca "  name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO_REVOCA%>">
            <option value = "0002"  />Reiezione Istanza
          </select>
        </td>
      </tr>
    </table>

      
    <table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabOrdinanzaRigetto199">
      <tr>
        <td class="Titolo" colspan="8"> Dati Ordinanza della Sorveglianza </td>
      </tr>
      <tr>
        <td class="l">Anno /Numero SIUS</td>
        <td class="l">
          <input type="text" Title="Anno Fascicolo Sius"  size="4" maxlength="4"
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>"  
                 value="<%=StringUtils.toStringJSP (aProvvedimentoSorv.getAnnoProcedimento()) %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
                 >
          /
          <input type="text" Title="Numero Sius" size="6" maxlength="6"
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>" 
                 value="<%=StringUtils.toStringJSP (aProvvedimentoSorv.getProgrProcedimento()) %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
                 >
        </td>
        <td class="l"> Anno / Numero Ordinanza</td>
        <td class="l">
          <input type="text" Title="Anno Orinanza" size="4" maxlength="4"
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>" 
                 value="<%=StringUtils.toStringJSP (aProvvedimentoSorv.getAnnoProvvedimento()) %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
                 >
          /
          <input type="text" Title="Numero Ordinanza" size="6" maxlength="6"
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>"
                 value="<%=StringUtils.toStringJSP (aProvvedimentoSorv.getProgrProvvedimento()) %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
                 >
        </td>
      </tr>
      <tr>
        <td class="l">Ufficio Emittente</td>
        <td class="l" colspan="3">
          <select Title="Ufficio di Sorveglianza" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE%>_199" >
            <option value = "-" />-
            <%=comboUfficiSorv%>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede Ufficio Emittente</td>
        <td class="l" colspan="3">
          <font class="campo">
            <input type="text" size="35" Title="Luogo Ufficio Sorveglianza" 
                   value="<%=StringUtils.toStringJSP (aProvvedimentoSorv.getDescrLuogoEmittente()) %>"
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_DESC_LUOGO_EMITTENTE%>_199" >
            
            <a href="Javascript:ListaUfficiPerTipo('formName','<%=ICostantiStatoEsecTitoloCumulato.CAMPO_DESC_LUOGO_EMITTENTE%>_199'
                                                             , document.formName.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE%>_199.value);">
              <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0>
            </a>
          </font>
        </td>
      </tr>
      <tr>
        <td class="l">Oggetto Ordinanza  <font class=ob>(*)</font></td>
        <td class="L" colspan="3">
          <select  Title="Codice Motivo" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>_SORV">
            <option value = "-" />-
            <%=comboTipoProvv199Sorv%>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Data Emissione Ordinanza </td>
        <td class="l" colspan="3">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE %>_SORV"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoSorv.getDataEmissione(),"dd"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE %>_SORV"  
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoSorv.getDataEmissione(),"MM"))%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE %>_SORV" 
                   value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimentoSorv.getDataEmissione(),"yyyy"))%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <tr>
        <td  class="l">Note</td>
         <td  class="L"  colspan="3">
           <TEXTAREA title="Note" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE %>_SORV" cols="80" rows="2" ><%=StringUtils.toStringJSP (aProvvedimentoSorv.getNote()) %></textarea>
        </td>
      </tr>               
    </table>
  </div>
 

<%
//==========================================================================
%>

  <br>
  
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</form>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("formName");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script></body>
</html>

