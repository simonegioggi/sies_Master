<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiDatiFinaliCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiPosizioneGiuridicaCumulo"%>

<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"%>
<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>

<%@ page import="org.apache.log4j.Logger"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="DatiFinaliCumulo"     scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"/>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>

<jsp:useBean id="posizioneGiuridicaLibero"  scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneGiuridicaEspIst"  scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneGiuridicaEspAltro"  scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoUfficioSIUS"  			scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoProvvedimento"			scope="request" class="java.lang.String"/>

<jsp:useBean id="lPosizioneGiuridicaCumulo" scope="request" class="siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel"/>
<jsp:useBean id="tipoPosGiu"  				scope="request" class="java.lang.String"/>

<%
//[FT] - 03/08/2016 - MAC_LOG - Logger per SIESLog
final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

//==============================================================================
//   FORM di inserimento della posizione giuridica di arrivo con eventuali
//   dati aggiuntivi dipendenti dalla posizione giuridica
// - Data Inizio e Istituto di detenzione
// - Estremi del provvedimento delle sorveglianza se in misura o in differimento
//==============================================================================

IstitutoDetenzioneModel lIstituto = new IstitutoDetenzioneModel();

//siesLogger.info("MODALITA : "+modalita );
if (modalita.equals("M") ) {
	siesLogger.info("  >>>>>>>>> lPosizioneGiuridicaCumulo.getTipoPosGiu() : "+lPosizioneGiuridicaCumulo.getTipoPosGiu() );
	if (lPosizioneGiuridicaCumulo.getIstitutoDetenzione()!=null) 
  		lIstituto = lPosizioneGiuridicaCumulo.getIstitutoDetenzione();
}

%>
<html>
<head>
  <title> [S.I.E.S.] - Dati Finali Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">    
    
    var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
    
    //==========================================================================
    // Ritorna alla lista delle Misure Cautelari
    //==========================================================================
    function eseguiFunzione(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }
     
    
    
    //==========================================================================
    // Nasconde/Visualiza i campi in funzione della Posizione Giuridica Selezionata
    //==========================================================================
    function radioTipoPosizione(radioObj){
      //alert(""+radioObj.value);
      if (radioObj.value=="tipoLibero"){ 
        $('#tabLibero').show();
        $('#tabEspIst').hide();
        $('#tabPGMisura').hide();
        
        $('#idComboPGLib').change();
        
        $('#tabLibero input').prop('disabled',false);
        $('#tabLibero select').prop('disabled',false);

        $('#tabEspIst input').prop('disabled',true);
        $('#tabEspIst select').prop('disabled',true);

        $('#tabPGMisura input').prop('disabled',true);
        $('#tabPGMisura select').prop('disabled',true);
      }
      else if (radioObj.value=="tipoEspIst"){
        $('#tabLibero').hide();
        $('#tabEspIst').show();
        $('#tabPGMisura').hide();
        $('#tabMisura').hide();
        $('#tabDiffer').hide();
        $('#tabDifferProvv').hide();
        
        
        $('#tabLibero input').prop('disabled',true);
        $('#tabLibero select').prop('disabled',true);

        $('#tabEspIst input').prop('disabled',false);
        $('#tabEspIst select').prop('disabled',false);
        
        $('#tabPGMisura input').prop('disabled',true);
        $('#tabPGMisura select').prop('disabled',true);
        
        $('#tabMisura input').prop('disabled',true);
        $('#tabMisura select').prop('disabled',true);
        
        $('#tabDiffer input').prop('disabled',true);
        $('#tabDiffer select').prop('disabled',true);
        
        $('#tabDifferProvv input').prop('disabled',true);
        $('#tabDifferProvv select').prop('disabled',true);

        $('#idComboPGIst').change();

      }
      else if (radioObj.value=="tipoEspAltro"){
        $('#tabLibero').hide();
        $('#tabEspIst').hide();
        $('#tabPGMisura').show();
        $('#tabDifferProvv').hide();
        
        $('#idComboPGMis').change();
        
        
        $('#tabLibero input').prop('disabled',true);
        $('#tabLibero select').prop('disabled',true);

        $('#tabEspIst input').prop('disabled',true);
        $('#tabEspIst select').prop('disabled',true);
        
        $('#tabPGMisura input').prop('disabled',false);
        $('#tabPGMisura select').prop('disabled',false);
        
        $('#tabDifferProvv input').prop('disabled',true);
        $('#tabDifferProvv select').prop('disabled',true);
      }
    }
    
    function checkPosizione (comboPG){
      //alert("checkPosizione = "+comboPG.value);
      $('#trDataInizioMisura').hide();
      $('#trDataInizioMisura input').prop('disabled',true);

      if (   comboPG.value=="16" || comboPG.value=="17" // Differimento
          || comboPG.value=="12" || comboPG.value=="13" // Aff e Det dom
          || comboPG.value=="29" || comboPG.value=="54" // Aff e Det dom Provv
          || comboPG.value=="14"                        // Semilibertà
          || comboPG.value=="31" || comboPG.value=="32" || comboPG.value=="33"  // sosp 51 ter
         )
      {
        $('#tabMisura').show();
        $('#tabDiffer').hide();
        
        $('#tabMisura input').prop('disabled',false);
        $('#tabMisura select').prop('disabled',false); 
        $('#tabDiffer input').prop('disabled',true);
        $('#tabDiffer select').prop('disabled',true);

        if (   comboPG.value=="12" || comboPG.value=="13" // Aff e Det dom
            || comboPG.value=="29" || comboPG.value=="54" // Aff e Det dom Provv
            )
        { 
          $('#trDataInizioMisura').show();
          $('#trDataInizioMisura input').prop('disabled',false);
        }
        else {
          $('#trDataInizioMisura').hide();
          $('#trDataInizioMisura input').prop('disabled',true);
        }
        
        if ( comboPG.value=="16" )  { // Differimento 
          $('#idDataFinoAlDiff').html('Rinvio fino al <font class=ob>(*)</font>');
          $('#tabDiffer').show();
          $('#tabDifferProvv').hide();
          
          $('#tabDiffer input').prop('disabled',false);
          $('#tabDiffer select').prop('disabled',false);
          $('#tabDifferProvv input').prop('disabled',true);
          $('#tabDifferProvv select').prop('disabled',true);
        }
        else if ( comboPG.value=="17" )  {  // Differimento Provvisorio
          $('#idDataFinoAlDiff').html('Rinvio fino al ');
          $('#tabDiffer').show();
          $('#tabDifferProvv').show();
          
          $('#tabDiffer input').prop('disabled',false);
          $('#tabDiffer select').prop('disabled',false);
          $('#tabDifferProvv input').prop('disabled',false);
          $('#tabDifferProvv select').prop('disabled',false);
        }
      }
      else {
        $('#tabMisura').hide();
        $('#tabDiffer').hide();
        $('#tabDifferProvv').hide();
      
        $('#tabMisura input').prop('disabled',true);
        $('#tabMisura select').prop('disabled',true); 
        $('#tabDiffer input').prop('disabled',true);
        $('#tabDiffer select').prop('disabled',true);
        $('#tabDifferProvv input').prop('disabled',true);
        $('#tabDifferProvv select').prop('disabled',true);
      }
      
      if (   comboPG.value=="78" || comboPG.value=="79"  
          || comboPG.value=="80" || comboPG.value=="81" 
          || comboPG.value=="-"
         )
      {
        $('#idDataDecPgMisura').html('Data decorrenza ');
      }
      else {
        $('#idDataDecPgMisura').html('Data decorrenza Pena <font class=ob>(*)</font>');
      }
      
      if (comboPG.value=="76" || comboPG.value=="-")
      {
        $('#idDataDecPgIst').html('Data decorrenza ');
      }
      else {
        $('#idDataDecPgIst').html('Data decorrenza <font class=ob>(*)</font>');
      }


      // Esp in istituto
      if (   comboPG.value=="14" // Semilibertà
          || comboPG.value=="31" // Sosp Provv 51 ter (Det Dom)
          || comboPG.value=="32" // Sosp Provv 51 ter (Aff Prov)
          || comboPG.value=="33" // Sosp Provv 51 ter (Semilib)
          )
      {
        $('#trDataInizioMisuraIst').show();
        $('#trDataInizioMisuraIst input').prop('disabled',false);
      }
      else {
        $('#trDataInizioMisuraIst').hide();
        $('#trDataInizioMisuraIst input').prop('disabled',true);
      }

    }
    

    
    //==========================================================================
    //
    //==========================================================================
    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&LoadDescEstesa=SI", "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
    
    function pulisciIstituto (nomeCampoDesc, nomeCampoId){
      var campoDescr = document.getElementsByName(nomeCampoDesc)[0];
      var campoId    = document.getElementsByName(nomeCampoId)[0];
      campoDescr.value="";
      campoId.value="";
    }    
    
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function resetSede(){
      document.formName.<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_SEDE_UFF_FAS_SIUS %>.value="";
    }    
    
    function eseguiSubmit (azione) {
      if (azione=='submit') {
        if (Verify()){
          document.formName.submit();
        }
      }
      else if (azione=='annulla'){
        var msgConfirm = "";
        <% if (modalita.equals(ICostantiModuloCumulo.MODALITA_MODIFICA)) { %>
        msgConfirm = "Attenzione le eventuali modifiche ai dati non verranno salvate. Si vuole procedere?";
        <% } else {%>
        msgConfirm = "Attenzione i dati inseriti non verranno salvati. Si vuole procedere?";
        <% } %>
        
        if (confirm(msgConfirm)){
          document.formAnnulla.submit();
        }
        else {
          return;
        }
      }      
    }

    //==========================================================================
    // Funzione di verifica dati 
    //==========================================================================
    function Verify() {
      //alert ("Verify");
    
      var objPosGiuEnabled = $('select[name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_COD_POSIZIONE_GIURIDICA%>]:not(:disabled)');
    
      if ($('#radioLibero').prop('checked')){
        //alert("Libero");
        // nessun dato obbligatorio se non la posizione giuridica
        if (objPosGiuEnabled.val()=='-'){
          alert("Specificare la posizione giuridica");
          objPosGiuEnabled.focus();
          return false;
        }
        
        if (objPosGiuEnabled.val()=='16' || objPosGiuEnabled.val()=='17'){
          var annoSius = $('#tabMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHIAVE_ANNO_FAS_SIUS%>]').val();
          var annoProvv = $('#tabMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_REGISTRO%>]').val();
          
          if (  annoSius.length>0 
              && (annoSius<1900 || annoSius><%=DateUtils.getSysDate("yyyy")%>)
             )
          {
            alert("Anno SIUS '"+annoSius+"' non valido");
            $('#tabMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHIAVE_ANNO_FAS_SIUS%>]').focus();
            return false;
          }
          
          if (  annoProvv.length>0 
              && (annoProvv<1900 || annoProvv><%=DateUtils.getSysDate("yyyy")%> )
             )
          {
            alert("Anno Provvedimento '"+annoProvv+"' non valido");
            $('#tabMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_REGISTRO%>]').focus();
            return false;
          } 
          
          var giorno = $('#tabDiffer [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_FINE_MISURA%>]').val();
          var mese   = $('#tabDiffer [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_MESE_DATA_FINE_MISURA%>]').val();
          var anno   = $('#tabDiffer [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_DATA_FINE_MISURA%>]').val();
          var data_to_verify = giorno+'/'+mese+'/'+anno;
          
          // Per ora non obbligatoria
          if (!ControllaDataPassaVuota(data_to_verify)){
            alert("Data 'Rinvio fino al' non valida");
            $('#tabDiffer [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_FINE_MISURA%>]').focus();
            return false;
          }
          
          if(objPosGiuEnabled.val()=='16') {
            if (data_to_verify=='//'){
              alert("Data Rinvio obbligatoria");
              $( '#tabDiffer [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_FINE_MISURA%>]').focus();
              return false;
            }
          }
          else if(objPosGiuEnabled.val()=='17') {
            if(giorno=='' &&  !$( '#tabDifferProvv [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_DECISIONE_TDS%>]').prop( "checked") )
            {
                alert("Scegliere tra: Data Rinvio e Rinvio fino alla decisione del TDS");
                $( '#tabDifferProvv [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_DECISIONE_TDS%>]').focus();
                return false;
            }
              
            if(giorno !='' &&  $( '#tabDifferProvv [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_DECISIONE_TDS%>]').prop( "checked") )
            {
                alert("Scegliere Solo uno tra: Data Rinvio e Rinvio fino alla decisione del TDS");
                $( '#tabDifferProvv [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_DECISIONE_TDS%>]').focus();
                return false;
            }
          }
          
          if( !controllaDatiOrdinanza() )
           return false;
          
        }        
      }
      else if ($('#radioEspIst').prop('checked')){
        //alert("Espiazione In Istituto");
        var pgVal = objPosGiuEnabled.val();
        if (pgVal=='-'){        
          alert("Specificare la posizione giuridica");
          objPosGiuEnabled.focus();
          return false;
        }
        
        // Data Inizio
        var giorno = $('#tabEspIst [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO%>]').val();
        var mese   = $('#tabEspIst [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_MESE_DATA_INIZIO%>]').val();
        var anno   = $('#tabEspIst [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_DATA_INIZIO%>]').val();
        var data_to_verify = giorno+'/'+mese+'/'+anno;
        
        if ( pgVal=='76')
          checkData = ControllaDataPassaVuota (data_to_verify); // Data decorrenza non obbligatoria per la custodia cautelare altra causa
        else 
          checkData = ControllaData (data_to_verify);        
        
        if (!checkData){
          alert('Data di Decorrenza non valida');
          $('#tabEspIst [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO%>]').focus();
          return false;
        }
        
        if (data_to_verify!="//")
        {
          if( !CompareDate( data_to_verify, data_sistema) )
          {
            alert('La Data di Decorrenza non può essere superiore alla data odierna!');
            $('#tabEspIst [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO%>]').focus();
            return false;
          }
        }
        
        //Per Semilibero devo testare anche i dati del provvedimento di concessione
        if (   objPosGiuEnabled.val()=='14' 
            || objPosGiuEnabled.val()=='31' || objPosGiuEnabled.val()=="32" || objPosGiuEnabled.val()=="33"
           )
        {
          if( !controllaDatiOrdinanza() )
            return false;
        }

        // Data Inizio Misura
        var dataInizioMisuraEnabled = $('#trDataInizioMisuraIst [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO_MISURA%>]:enabled');
        if (dataInizioMisuraEnabled.size()>0) {
          var giorno = $('#trDataInizioMisuraIst [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO_MISURA%>]').val();
          var mese   = $('#trDataInizioMisuraIst [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_MESE_DATA_INIZIO_MISURA%>]').val();
          var anno   = $('#trDataInizioMisuraIst [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_DATA_INIZIO_MISURA%>]').val();
          var data_to_verify = giorno+'/'+mese+'/'+anno;

          checkData = ControllaDataPassaVuota (data_to_verify); // Data decorrenza non obbligatoria per la custodia cautelare altra causa
          
          if (!checkData){
            alert('Data di Inizio MIsura non valida');
            $('#trDataInizioMisuraIst [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO_MISURA%>]').focus();
            return false;
          }

          if (data_to_verify!="//")
          {
            if( !CompareDate( data_to_verify, data_sistema) )
            {
              alert('La Data Inizio Misura non può essere superiore alla data odierna!');
              $('#trDataInizioMisuraIst [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO_MISURA%>]').focus();
              return false;
            }
          }
        }
      }
      else if ($('#radioEspAltro').prop('checked')){
        //alert("Espiazione Altro");
        var pgVal = objPosGiuEnabled.val();
        if (pgVal=='-'){
          alert("Specificare la posizione giuridica");
          objPosGiuEnabled.focus();
          return false;
        }
        
        if (!$('#tabMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO%>]').prop('disabled'))
        {
          var giorno = $('#tabPGMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO%>]').val();
          var mese   = $('#tabPGMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_MESE_DATA_INIZIO%>]').val();
          var anno   = $('#tabPGMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_DATA_INIZIO%>]').val();
          
          var data_to_verify = giorno+'/'+mese+'/'+anno;
          //alert ("data_to_verify = "+data_to_verify);
          var checkData;
          if ( pgVal=='78' || pgVal=='79' || pgVal=='80' || pgVal=='81' )
            checkData = ControllaDataPassaVuota(data_to_verify); // Data decorrenza non obbligatoria per la custodia cautelare altra causa
          else 
            checkData = ControllaData(data_to_verify);
          
          //if (!ControllaData(data_to_verify)){
          if (!checkData){
            alert('Data di Decorrenza non valida');      
            $('#tabPGMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO%>]').focus();
            return false;
          }
          
          if (data_to_verify!="//")
          {
            if( !CompareDate( data_to_verify, data_sistema) )
            {
              alert('La Data di Decorrenza non può essere superiore alla data odierna!');
              $('#tabPGMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO%>]').focus();
              return false;
            }
          }         
        }
        
        // Data Inizio Misura
        var dataInizioMisuraEnabled = $('#trDataInizioMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO_MISURA%>]:enabled');
        if (dataInizioMisuraEnabled.size()>0) {
          var giorno = $('#trDataInizioMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO_MISURA%>]').val();
          var mese   = $('#trDataInizioMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_MESE_DATA_INIZIO_MISURA%>]').val();
          var anno   = $('#trDataInizioMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_DATA_INIZIO_MISURA%>]').val();
          var data_to_verify = giorno+'/'+mese+'/'+anno;

          checkData = ControllaDataPassaVuota (data_to_verify); // Data decorrenza non obbligatoria per la custodia cautelare altra causa
          
          if (!checkData){
            alert('Data di Inizio MIsura non valida');
            $('#trDataInizioMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO_MISURA%>]').focus();
            return false;
          }

          if (data_to_verify!="//")
          {
            if( !CompareDate( data_to_verify, data_sistema) )
            {
              alert('La Data Inizio Misura non può essere superiore alla data odierna!');
              $('#trDataInizioMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO_MISURA%>]').focus();
              return false;
            }
          }
        }

        if(   objPosGiuEnabled.val()=='12' || objPosGiuEnabled.val()=='13' 
           || objPosGiuEnabled.val()=='29' || objPosGiuEnabled.val()=='54' 
          ) 
        { 
          if(!controllaDatiOrdinanza() )
            return false;
        }  
      }
      else {
        alert("Selezionare la tipologia di posizione");
        return false;
      }
    
    
      return true; 
    }
    
    function controllaDatiOrdinanza()
    {
      var retValue = true;
     // Tipo Ufficio Emittente

      var TipoAutorita = $('#tabMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_TIPO_UFF_FAS_SIUS %>]');
      if (TipoAutorita.val() =='-'){
        alert("Selezionare l'autorità emittente");
        TipoAutorita.focus();
        retValue = false;
      }
     
     // Sede Ufficio Emittente
      if(retValue)
      { 
        var sedeAutorita = $('#tabMisura input[type=text][name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_SEDE_UFF_FAS_SIUS %>]');
        if (sedeAutorita.val()=='') {
          alert("Selezionare la Sede autorità emittente");
          sedeAutorita.focus();
          retValue = false;
        }
      }     
     
     // Tipo Provvedimento
      if(retValue)
      {
        var TipoProvv = $('#tabMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_COD_TIPO_PROVVEDIMENTO %>]');
        //alert('TipoProvv vale = >'+TipoProvv+'<');
        if (TipoProvv.val() =='-')
        {
          alert("Selezionare il tipo provvedimento");
          TipoProvv.focus();
          retValue = false;
        }
      }     
     
     // Data Emissione
      if(retValue)
      {
        var giorno = $('#tabMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_EMISSIONE_PROVV%>]').val();
        var mese   = $('#tabMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_MESE_DATA_EMISSIONE_PROVV%>]').val();
        var anno   = $('#tabMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_DATA_EMISSIONE_PROVV%>]').val();
        var data_to_verify = giorno+'/'+mese+'/'+anno;
       
        if (!ControllaDataPassaVuota(data_to_verify)){
          alert('Data Emissione Provvedimento della Sorveglianza non valida');
          $('#tabMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_EMISSIONE_PROVV%>]').focus();
          retValue = false;
        }
        
        if (data_to_verify!="//")
        {
          if( !CompareDate( data_to_verify, data_sistema) )
          {
            alert('Data Emissione Provvedimento non può essere superiore alla data odierna!');
            $('#tabMisura [name=<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_EMISSIONE_PROVV%>]').focus();
            return false;
          }
        }
      }
     
      return retValue;
     
    }

    // INIT
    $(document).ready(function(){
      $('#tabLibero').hide();
      $('#tabEspIst').hide();
      $('#tabPGMisura').hide();
      $('#tabMisura').hide();
      $('#tabDiffer').hide();
      $('#tabDifferProvv').hide();
      <% if (modalita.equals(ICostantiModuloCumulo.MODALITA_MODIFICA)) { %>
        <% if (tipoPosGiu.equals("tipoLibero") ) {%>
          $('#radioLibero').click();
        <% } else if (tipoPosGiu.equals("tipoEspIst")) {%>
          $('#radioEspIst').click();
        <% } else if (tipoPosGiu.equals("tipoEspAltro")) {%>
          $('#radioEspAltro').click();
        <% } %>
      <% } %>
    });
    
  </script>
</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
        if( modalita.equals("I") ) { %>
        <font class="campo">Inserimento Espiazione Attuale &nbsp;</font>
        <%
        }
        else if( modalita.equals("M") ) { %>
        <font class="campo">Modifica Espiazione Attuale &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaEspiato')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>      
    </tr>
  </table>
	<br>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>
      </td>
    </tr>
  </table>

  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formAnnulla">
    <%//FORM per richiamare i dettagli in POST ma evitare di inviare inutilmente i dati della FORM principale%>
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActDettaglioEspiazioneAttuale">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  </form> 
     
<div id="divPosizionamento" align="left" style="padding-left: 25px; border: 0px solid black;">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciEspiazioneAttuale">
  
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">

    <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA%>" value="<%=modalita%>">

    <input type="hidden" name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM%>" value="<%=StringUtils.toStringJSP(lPosizioneGiuridicaCumulo.getIdPosizioneGiuridicaCum()) %>">

    <input type="hidden" name="<%=ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO%>" value="<%=StringUtils.toStringJSP(DatiFinaliCumulo.getIdDatiFinaliCumulo()) %>">

    <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>" value="<%=StringUtils.toStringJSP(TitoloInCumulo.getIdTitoloCumulato()) %>">

    <%
    //==========================================================================
    // Sezione con la Posizione Giuridica
    //==========================================================================
    %>    
    <table width="800px">
      <tr>
        <td class="l" colspan="2">
          <input type="radio" 
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_RADIO_TIPO_POS %>" 
                 id="radioLibero" 
                 <%=((ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_LIBERO).equals(tipoPosGiu ) )?"checked":"" %>
                 value="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_LIBERO %>" onClick="radioTipoPosizione(this);" >Libero
          &nbsp;
          <input type="radio" 
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_RADIO_TIPO_POS %>" 
                 id="radioEspIst" 
                 <%=((ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPIST).equals(lPosizioneGiuridicaCumulo.getTipoPosGiu()))?"checked":"" %>
                 value="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPIST %>" onClick="radioTipoPosizione(this);" >Espiazione Pena in Istituto di Detenzione
          &nbsp;
          <input type="radio" 
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_RADIO_TIPO_POS %>" 
                 id="radioEspAltro" 
                 <%=((ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPALTRO).equals(lPosizioneGiuridicaCumulo.getTipoPosGiu()))?"checked":"" %>
                 value="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPALTRO %>" onClick="radioTipoPosizione(this);" >Espiazione Pena in Altro Luogo
        </td>
      </tr>
    </table>

    <%
    //==========================================================================
    // LIBERO    
    //==========================================================================
    %>    
    <table id="tabLibero" width="800px">
      <tr>
        <td class="l">Posizione Giuridica <font class="ob">(*)</font></td>
        <td class="l" colspan="1">
          <select title="Posizione Giuridica" name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_COD_POSIZIONE_GIURIDICA %>"
                  id="idComboPGLib"
                  onChange="checkPosizione(this)">
            <option value="-"/>-
            <%=posizioneGiuridicaLibero %>
          </select>
        </td>
      </tr>
    </table>


    <%
    //==========================================================================
    // ESPIAZIONE IN ISTITUTO
    //==========================================================================
    %>    
    <table id="tabEspIst" width="800px">
      <tr>
        <td class="l">Posizione Giuridica <font class="ob">(*)</font></td>
        <td class="l" colspan="1">
          <select title="Posizione Giuridica"
                  name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_COD_POSIZIONE_GIURIDICA %>"
                  id="idComboPGIst"
                  onChange="checkPosizione(this)">
            <option value="-"/>-
            <%=posizioneGiuridicaEspIst %>
          </select>
        </td>
      </tr>
            
      <tr>
        <td class="l" id="idDataDecPgIst">Data di Decorrenza <font class=ob>(*)</font></td>
        <td class="l"  colspan="3">
          <input type="text" Title="Giorno Data Decorrenza" size="2" maxlength="2" 
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO%>" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizio(),"dd")) %>"
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input type="text" Title="Mese Data Decorrenza" size="2" maxlength="2" 
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_MESE_DATA_INIZIO%>" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizio(),"MM")) %>"
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input type="text" Title="Anno Data Decorrenza" size="4" maxlength="4" 
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_DATA_INIZIO%>" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizio(),"yyyy")) %>"
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
       
              
      <tr>
        <td class="l">Istituto</td>
        <td class="l" colspan="3">
          <input type="text" readonly  Title="Istituto" name="DescrIstituto" size="90"
                 <% if (!"".equals(lIstituto.getIdIstitutoDetenzione())) { %>
                 value="<%=StringUtils.toStringJSP(lIstituto.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lIstituto.getDescrizione())%> - <%=StringUtils.toStringJSP(lIstituto.getIndirizzo())%>"
                 <% } %>
                 >
          <input type="hidden"  Title="Istituto" 
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" 
                 value="<%=StringUtils.toStringJSP(lIstituto.getIdIstitutoDetenzione())%>" >
          <a href="Javascript:ListaIstitutoDetenzione('formName','<%= ICostantiPosizioneGiuridicaCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','DescrIstituto');">
            <img src="/images/filefolder.gif" border=0></a>
          <a href="Javascript:pulisciIstituto('DescrIstituto','<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>

        </td>
      </tr>

<!--  ADD DIEGO -->
      <tr id="trDataInizioMisuraIst">
        <td class="l" >Data Inizio Misura</td>
        <td class="l" colspan="3">
            <input type="text" Title="Giorno Data Inizio Misura" size="2" maxlength="2" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizioMisura(),"dd")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" Title="Mese Data Inizio Misura"  size="2" maxlength="2" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_MESE_DATA_INIZIO_MISURA%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizioMisura(),"MM")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" Title="Anno Data Inizio Misura"size="4" maxlength="4" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_DATA_INIZIO_MISURA%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizioMisura(),"yyyy")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>
    
    
    <%
    //==========================================================================
    // Espiazione Pena In Altro Luogo
    //==========================================================================
    %>
    <table id="tabPGMisura" width="800px">
      <tr>
        <td class="l">Posizione Giuridica <font class="ob">(*)</font></td>
        <td class="l" colspan="3">
          <select title="Posizione Giuridica" name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_COD_POSIZIONE_GIURIDICA %>"
                  id="idComboPGMis"
                  onChange="checkPosizione(this)">
            <option value="-"/>-
            <%=posizioneGiuridicaEspAltro %>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l" id="idDataDecPgMisura">Data decorrenza <font class=ob>(*)</font></td>
        <td class="l"  colspan="3">
            <input type="text" Title="Giorno Data Decorrenza" size="2" maxlength="2" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizio(),"dd")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" Title="Mese Data Decorrenza"  size="2" maxlength="2" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_MESE_DATA_INIZIO%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizio(),"MM")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" Title="Anno Data Decorrenza"size="4" maxlength="4" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_DATA_INIZIO%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizio(),"yyyy")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>      
      <tr>
        <td class="l">Luogo di Espiazione</td>
        <td class="L" colspan="3">
          <input type="text" title="Luogo di Espiazione"  size="80"
                 value="<%=StringUtils.toStringJSP ( lPosizioneGiuridicaCumulo.getAltroLuogo()) %>"
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ALTRO_LUOGO%>" >
        </td>
      </tr>

<!--  ADD DIEGO -->
      <tr id="trDataInizioMisura">
        <td class="l" >Data Inizio Misura</td>
        <td class="l" colspan="3">
            <input type="text" Title="Giorno Data Inizio Misura" size="2" maxlength="2" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizioMisura(),"dd")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" Title="Mese Data Inizio Misura"  size="2" maxlength="2" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_MESE_DATA_INIZIO_MISURA%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizioMisura(),"MM")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" Title="Anno Data Inizio Misura"size="4" maxlength="4" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_DATA_INIZIO_MISURA%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizioMisura(),"yyyy")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>
    
    <%
    //==========================================================================
    // Sezione con gli estremi dell'ordinanza di concessione MA oppure del 
    // differimento  
    //==========================================================================
    %>    
    <table id="tabMisura" width="800px">
      <tr>
        <td class="Titolo" colspan="4"> Provvedimento di concessione della Misura </td>
      </tr>

      <tr>
        <td class="l">Anno / Numero SIUS</td>
        <td class="l">
          <input type="text" Title="Anno Fascicolo Sius" size="4" maxlength="4"
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHIAVE_ANNO_FAS_SIUS %>" 
                 value="<%=StringUtils.toStringJSP ( lPosizioneGiuridicaCumulo.getChiaveAnnoFasSius()) %>"
                 onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
                 >
          /
          <input type="text" Title="Numero Sius" size="6" maxlength="6"    
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHIAVE_PROGR_FAS_SIUS %>" 
                 value="<%=StringUtils.toStringJSP ( lPosizioneGiuridicaCumulo.getChiaveProgrFasSius()) %>"
                 onkeypress="return TicTabNumField(this,event)"
                 >
        </td>
        <td class="l"> Anno / Numero Provvedimento </td>
        <td class="l">
          <input type="text" Title="Anno Provvedimento" size="4" maxlength="4" 
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_REGISTRO %>" 
                 value="<%=StringUtils.toStringJSP ( lPosizioneGiuridicaCumulo.getAnnoRegistro()) %>"
                 onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
                 >
          /
          <input Title="Numero Provvedimento" type="text" size="6" maxlength="6" 
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_NUMERO_REGISTRO %>" 
                 value="<%=StringUtils.toStringJSP ( lPosizioneGiuridicaCumulo.getNumeroRegistro()) %>"
                 onkeypress="return TicTabNumField(this,event)"
                 >
        </td>
      </tr>
      <tr>
        <td class="l">Ufficio Emittente <font class=ob>(*)</font></td>
        <td class="l" colspan="3">
          <select  Title="Ufficio Emittente" name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_TIPO_UFF_FAS_SIUS %>"  
                             id="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_TIPO_UFF_FAS_SIUS %>" onchange="resetSede()">
            <option value="-"/>-
            <%=tipoUfficioSIUS%>
          </select>      
        </td>
      </tr>
      <tr>
        <td class="l">Sede Ufficio Emittente <font class=ob>(*)</font></td>
        <td class="l" colspan="3">
          <input type="text"  Title="Luogo Ufficio Sorveglianza" size="35"
                 <% if (lPosizioneGiuridicaCumulo.getUfficioSorv()!=null) { %>
                 value="<%=StringUtils.toStringJSP ( lPosizioneGiuridicaCumulo.getUfficioSorv().getDescrComune()) %>"
                 <% } %>
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_SEDE_UFF_FAS_SIUS %>"  >
          <a href="Javascript:ListaUfficiPerTipo('formName'
                                                ,'<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_SEDE_UFF_FAS_SIUS %>'
                                                ,document.formName.<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_TIPO_UFF_FAS_SIUS %>.value);">
            <img src="/images/filefolder.gif" border=0>
          </a>
       </td>
      </tr>
      <tr>
        <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
        <td class="l" colspan="3">
          <select  Title="Tipo Provvedimento" name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_COD_TIPO_PROVVEDIMENTO %>" >
            <%=tipoProvvedimento%>
          </select>
        </td>
      </tr>     

      <tr>
        <td class="l">Data Emissione</td>
        <td class="l" colspan="3">
          <font class="campo">
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_EMISSIONE_PROVV %>"
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataEmissioneProvv(),"dd")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
            <input type="text" size="2" maxlength="2" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_MESE_DATA_EMISSIONE_PROVV %>"
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataEmissioneProvv(),"MM")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
            <input type="text" size="4" maxlength="4" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_DATA_EMISSIONE_PROVV %>"
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataEmissioneProvv(),"yyyy")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
          </font>
        </td>
      </tr>
    </table>  

<%
//==============================================================================
// Tabella Differimento
//==============================================================================
%>
  <table id="tabDiffer" width="800px">
    <tr>

        <td class="l" width="20%">Rinvio nella misura di</td>
        <td class="l" colspan="1">
          Anni
          <input type="text" Title="Anni rinvio " maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(lPosizioneGiuridicaCumulo.getNumAnniMisura() )%>" 
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_NUM_ANNI_MISURA%>" 
                 onkeypress="return TicTabNumField(this,event)"
                 >
          Mesi
          <input type="text" Title="Mesi rinvio " maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(lPosizioneGiuridicaCumulo.getNumMesiMisura() )%>" 
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_NUM_MESI_MISURA%>" 
                 onkeypress="return TicTabNumField(this,event)"
                 >
          Giorni
          <input type="text" Title="Giorni rinvio " maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(lPosizioneGiuridicaCumulo.getNumGiorniMisura() )%>" 
                 name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_NUM_GIORNI_MISURA%>" 
                 onkeypress="return TicTabNumField(this,event)"
                 >
        </td>         
    
        <td class="l" id="idDataFinoAlDiff">Rinvio fino al <font class=ob>(*)</font></td>
        <td class="l"  colspan="1">
            <input type="text" Title="Giorno fine Differimento" size="2" maxlength="2" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_GIORNO_DATA_FINE_MISURA%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataFineMisura(),"dd")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" Title="Mese fine Differimento"  size="2" maxlength="2" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_MESE_DATA_FINE_MISURA%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataFineMisura(),"MM")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" Title="Anno fine Differimento" size="4" maxlength="4" 
                   name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ANNO_DATA_FINE_MISURA%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataFineMisura(),"yyyy")) %>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        
   
      </tr>
    </table>  
    
    <table id="tabDifferProvv" width="800px">  
      <tr>
        <td class="l" >
        <%  if(lPosizioneGiuridicaCumulo.getFlagDecisioneTDS()!=null && lPosizioneGiuridicaCumulo.getFlagDecisioneTDS().equals("S") )  {  %>
          <input type="checkbox" name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_DECISIONE_TDS%>"  value="S" CHECKED > Fino alla decisione del TDS
        <%  }else { %>
          <input type="checkbox" name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_DECISIONE_TDS%>"  value="S" > Fino alla decisione del TDS
        <%  }  %>
      </td>     
      </tr>    
    </table>
 
 <%
//==============================================================================
// 
//==============================================================================
%>
<br>
<table width="800px">
  <tr>
    <td>
      <input type="button" class="bottone" name="Conferma" value="Conferma" onClick="eseguiSubmit('submit')">
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
        