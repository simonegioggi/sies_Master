<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Date"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>

<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel" />

<jsp:useBean id="modalita"       scope="request" class="java.lang.String"/>
<jsp:useBean id="titolocumulato" scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>


<jsp:useBean id="TipoProvv"   scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRegGen"   scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEmi"  scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmiSorv"  scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoProvvedimenti"    scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimentiRif" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito"             scope="request" class="java.lang.String" />
<jsp:useBean id="tipoDecisioneCassazione" scope="request" class="java.lang.String" />

<%
//==============================================================================
// Form per l'inserimento e la modifica del Titolo iscritto in istruttoria 
// cumulo
//==============================================================================
        
TitoloCumulatoModel lTitoloCumulato        = new TitoloCumulatoModel(); 
TitoloCumulatoModel lTitoloCumulatoDecPen  = new TitoloCumulatoModel(); 
TitoloCumulatoModel lTitoloCumulatoSenStra = new TitoloCumulatoModel(); 
TitoloCumulatoModel lTitoloCumulatoSorv    = new TitoloCumulatoModel(); 

Date lDataIrrevocabilita = null;

if( modalita.equals("I") ) {
}
else if( modalita.equals("M") ) {
  lDataIrrevocabilita = titolocumulato.getDataIrrevocabilita();

  if ("01".equals(titolocumulato.getCodTipoProvvedimento())) {
    lTitoloCumulato = titolocumulato;
  } else if ("02bis".equals(titolocumulato.getCodTipoProvvedimento())) {
    lTitoloCumulatoDecPen = titolocumulato;
  } else if ("05".equals(titolocumulato.getCodTipoProvvedimento())) {
    lTitoloCumulatoSenStra = titolocumulato;
  } else if (   "02".equals(titolocumulato.getCodTipoProvvedimento())
             || "03".equals(titolocumulato.getCodTipoProvvedimento())
            ) {
    lTitoloCumulatoSorv = titolocumulato;
  }    
}
        


%>
<html>
<head>
  <title> [S.I.E.S.] - Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>" ></script>

  <script language="JavaScript">
    var desktop;
    
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio+"&<%=ICostantiUfficio.CAMPO_FLAG_ACCORP%>=S", "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    //==========================================================================
    // Verifica che non siano state selezionate 2 autorità uguali o dello stesso grado
    // ed abilita la combo tipo Rito per DIB e TRIBSD
    //==========================================================================
    function abilitaTipoRito(objAutorita1, objTipoRito, objAutorita2)
    {
      //alert("abilitaTipoRito");
      
      var arrCmb = new Array(objAutorita1, objAutorita2);
      var arrGrado = new Array();  // 
      
      for(var i=0;i<arrCmb.length;i++) {
        if (arrCmb[i].value == "CSS") {      
          arrGrado[i] = 3;
        }
        else if (arrCmb[i].value == "CAP" || arrCmb[i].value == "CASAP" || arrCmb[i].value == "CAPMI" || 
                 arrCmb[i].value == "CAPSM" || arrCmb[i].value == "CAPMID") {          
          arrGrado[i] = 2;
        }
        else {    
          arrGrado[i] = 1;
        }
      }   
      
      //
      if (objAutorita1.value != "-" && objAutorita2.value != "-") {    
        if (objAutorita1.value == objAutorita2.value) {    
          alert("Non è consentito selezionare due Autorità Emittenti uguali!");
          objAutorita1.selectedIndex = 0;
          objAutorita1.focus();
        }
        else if (arrGrado[0] == arrGrado[1]) {     
          
          // eccezione per Giudice di Pace e Tribunale Ordinario (anche sezione distaccata)
          if (   !(objAutorita1.value == "GP" && (objAutorita2.value == "DIB" || objAutorita2.value == "TRIBSD")) 
              && !(objAutorita2.value == "GP" && (objAutorita1.value == "DIB" || objAutorita1.value == "TRIBSD"))
             ) 
          {          
            alert("Non è consentito selezionare due Autorità Emittenti dello stesso grado!");
            objAutorita1.selectedIndex = 0;
            objAutorita1.focus();
          }
        }
      } 
    
    
      if (objAutorita1.value=='DIB' || objAutorita1.value=='TRIBSD')  {
        objTipoRito.selectedIndex = 0;
        objTipoRito.disabled = false;
      }
      else {
        objTipoRito.selectedIndex = 0;
        objTipoRito.disabled = true;
      }
    }


    //=====================================================
    //    
    //=====================================================
    function gestisciTipoProvvAGDG(objTipoProvv){
      var nodeCass = document.getElementById('trCassazione');
      var codTipoProvv = objTipoProvv.value;      

      if (codTipoProvv=='53') { // Ordinanza Inammissibilità
        // Nascondo la sezione con i dati della Decisione della Cassazione
        // e modifico la sezione 'Altro Grado di Giudizio'
        nodeCass.style.display = "none";       
           
        //document.getElementById('tipoSentenza').style.display="none";
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVV_RIF%>.selectedIndex=0;
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVV_RIF%>.disabled=true;

        document.getElementById('labelSentenza').style.display="none";
        document.getElementById('anSentenza').style.display="none";
        document.getElementById('labelOrdinanza').style.display="block";
        document.getElementById('anOrdinanza').style.display="block";
      }
      else {
        nodeCass.style.display = "block";
        //document.getElementById('tipoSentenza').style.display="block";
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVV_RIF%>.disabled=false;
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVV_RIF%>.selectedIndex=0;
        
        document.getElementById('labelSentenza').style.display="block";
        document.getElementById('anSentenza').style.display="block";
        document.getElementById('labelOrdinanza').style.display="none";
        document.getElementById('anOrdinanza').style.display="none";
      }
    }

    //=====================================================
    //    
    //=====================================================
    function gestisciTipoProvvCASS(objTipoProvv){
      var codTipoProvv = objTipoProvv.value;      
      
      if (codTipoProvv=='53') {
        document.getElementById('anRegGen').style.display="none";
        document.getElementById('anRacGen').style.display="none";
        document.getElementById('disp').style.display="none";
        document.getElementById('lblSentenza').style.display="none";
        document.getElementById('lblOrdinanza').style.display="block";
      }
      else {
        document.getElementById('anRegGen').style.display="block";
        document.getElementById('anRacGen').style.display="block";
        document.getElementById('disp').style.display="block";
        document.getElementById('lblSentenza').style.display="block";
        document.getElementById('lblOrdinanza').style.display="none";
      }
    }
    
    
    //============================
    //============================
    function ViewDiv(){
      var tipoProvv = document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value;

      if (tipoProvv=="-"){
        document.getElementById('divSentenza').style.display="none";
        document.getElementById('divDecrPenale').style.display="none";
        document.getElementById('divStraniera').style.display="none";
        document.getElementById('divSorveglianza').style.display="none";
      }
      else if (tipoProvv=="01"){
        document.getElementById('divSentenza').style.display="block";
        document.getElementById('divDecrPenale').style.display="none";
        document.getElementById('divStraniera').style.display="none";
        document.getElementById('divSorveglianza').style.display="none";
      }
      else if (tipoProvv=="02bis"){
        document.getElementById('divSentenza').style.display="none";
        document.getElementById('divDecrPenale').style.display="block";
        document.getElementById('divStraniera').style.display="none";
        document.getElementById('divSorveglianza').style.display="none";
      }
      else if (tipoProvv=="02" || tipoProvv=="03"){
        document.getElementById('divSentenza').style.display="none";
        document.getElementById('divDecrPenale').style.display="none";
        document.getElementById('divStraniera').style.display="none";
        document.getElementById('divSorveglianza').style.display="block";
      }
      else if (tipoProvv=="05"){
        document.getElementById('divSentenza').style.display="none";
        document.getElementById('divDecrPenale').style.display="none";
        document.getElementById('divStraniera').style.display="block";
        document.getElementById('divSorveglianza').style.display="none";
      }      

      return;


      //alert("tipoPorvv = "+tipoPorvv);
      // 01 - Sentenza
      // 02 - Decreto penale
      if (tipoProvv=="01" || tipoProvv=="-"){
        //
        document.getElementById('trAGDG').style.display="block";

        if ( document.getElementById('trCassazione').style.display=="block") {
          if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>!='53') {
            document.getElementById('anRegGen').style.display="block";
          }
        }
      }
      else {
        // Decreto penale
        // -- Tipo Reg. Gen. fisso a GIP 
        // -- Tipo Rito non previsto
        // -- Le etichette diventano: Sentenza --> Decreto Penale da Eseguire  
        //                            Data Provvedimento --> Data Decreto 
        //                            Anno/Numero Sentenza --> Anno/Numero Decreto
        // -- Nascondo la sezione AGDG
        // -- Cassazione: tipo Provvedimento fisso a Sentenza,  non prevista Anno/Numero Reg.Gen.
        
      
        document.getElementById('trAGDG').style.display="none";
        document.getElementById('trCassazione').style.display="block";
        document.getElementById('anRegGen').style.display="none";
      }
    }
    
    
    
    
    //==========================
    //
    //==========================
    function Verify()
    {
      // Data Irrevocabilità
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;
      
      var dataIrrev =     document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value
                     +'/'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_IRREVOCABILITA%>.value
                     +'/'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
      
      if (! ControllaData(dataIrrev))
      {
        alert('Data Irrevocablità non valida');
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.focus();
        return false;
      }
      
      // Tipo Provvedimento
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value=='-'){
        alert('Selezionare il tipo provvedimento');
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();
        return false;
      }
      
      var tipoProvv = document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value;
      if (tipoProvv=='01'){
        return verifySentenza();
      }
      else if (tipoProvv=='02bis'){
        return verifyDecretoPenale();
      }
      else if (tipoProvv=='02' || tipoProvv=='03'){
        return verifyAltro('_SORV');
      }
      else if (tipoProvv=='05'){
        return verifyAltro('_SENT_STRA');
      }
    }

    function verifySentenza(){    
      // Anno e Numero RGNR - vedi validator
      
      // Anno e Numero Reg. Gen. - vedi validator
      // Tipo Reg Gen
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_TIPO_REG_GEN%>.value=='-'){
        alert('Il tipo Registro Generale è obbligatorio');
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_TIPO_REG_GEN%>.focus();
        return false;
      }
      
      // Data provvedimento
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
      
      var dataProvv =     document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value
                     +'/'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value
                     +'/'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
      
      if (! ControllaData(dataProvv))
      {
        alert('Data Provvedimento non valida');
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.focus();
        return false;
      }
      
      // Autorità emittente
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value=='-'){
        alert('Selezionare il tipo Autorità Emittente');
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.focus();
        return false;
      }
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>.value==''){
        alert('Indicare la Sede Autorità Emittente');
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
        return false;
      }      
    
      //===================================
      // AGDG - Se la sezione viene compilata, i dati obbligatori sono:
      //  - Tipo Sentenza di Riferimento
      //  - Data Sentenza di Riferimento
      //  - Autorità Sentenza di Riferimento
      //  - Luogo Sentenza di Riferimento
      //===================================
      var CodTipoProvv= document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>.value;
      var TipoSentRif = document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVV_RIF %>.value;
      var TipoAutRif  = document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.value;
      var SedeRif     = document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_LUOGO_PROVV_RIF %>.value;

      // Data Provvedimentio AGDG
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVV_RIF%>.value.length==1)
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVV_RIF%>.value='0'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVV_RIF%>.value;
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVV_RIF%>.value.length==1)
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVV_RIF%>.value='0'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVV_RIF%>.value;
      
      var dataProvvRif =     document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVV_RIF%>.value
                        +'/'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVV_RIF%>.value
                        +'/'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVV_RIF%>.value;

     
      if(TipoSentRif != '-' || (dataProvvRif!='//') || TipoAutRif != '-' || SedeRif != '')
      {
        if(TipoSentRif == '-' && CodTipoProvv != '53')
        {
          alert('Dati della Sentenza di Riferimento Incompleti - Tipo Sentenza');
          document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVV_RIF %>.focus();
          return false;
        }        
        
        if (! ControllaData(dataProvvRif)) {
          alert('Dati della Sentenza di Riferimento Incompleti - Data Sentenza Errata');
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVV_RIF%>.focus();
          return false;
        }
        
        if(TipoAutRif == '-') {
          alert('Dati della Sentenza di Riferimento Incompleti - Selezionare Autorità Emittente');
          document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF%>.focus();
          return false;
        }
        
        if(SedeRif == '')  {
          alert('Dati della Sentenza di Riferimento Incompleti - Selezionare Luogo Autorità Emittente');
          document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_LUOGO_PROVV_RIF %>.focus(); 
          return false;
        }
      }
      
      var auEmi = document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value;
      
      if ( dataProvvRif!='//' )
      { // è stata indicata la sentenza AGDG
        if( auEmi=="CAP" || 
            auEmi=="CAPMID" || 
            auEmi=="CASAP"  || 
            auEmi=="CAPSM"  || 
            auEmi=="CAPMI")
        {
          if (CompareDate(dataProvv,dataProvvRif)){
            alert(' La Data Sentenza deve essere successiva alla Data della Sentenza di grado differente');
            return false;
          }
        }
        else{
          if (CompareDate(dataProvvRif,dataProvv)){
            alert(' La Data della Sentenza di grado differente deve essere successiva alla Data Sentenza');
            return false;
          }
        }
      } 

      return true;
    }

    //=========================================
    //
    //=========================================
    function verifyDecretoPenale (tipoTitolo){
      //Anno e numero R.G.N.R.
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_ANNO_REGE_PM%>_DECPEN.value==""){
        alert("Indicare l''anno R.G.N.R.");
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_ANNO_REGE_PM%>_DECPEN.focus();
        return false;
      }
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_NUMERO_REGE_PM%>_DECPEN.value==""){
        alert('Indicare il numero R.G.N.R.');
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_NUMERO_REGE_PM%>_DECPEN.focus();
        return false;
      }
      
      // Anno e numero Reg. Gen.
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_ANNO_REG_GEN%>_DECPEN.value==""){
        alert("Indicare l'anno Reg. Gen. ");
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_ANNO_REG_GEN%>_DECPEN.focus();
        return false;
      }
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_NUMERO_REG_GEN%>_DECPEN.value==""){
        alert('Indicare il numero Reg. Gen.');
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_NUMERO_REG_GEN%>_DECPEN.focus();
        return false;
      }
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_SEDE_NOTIZIA_REATO%>_DECPEN.value==""){
        alert('Indicare la sede PM');
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_SEDE_NOTIZIA_REATO%>_DECPEN.focus();
        return false;
      }       
      
      
      // Data provvedimento
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>_DECPEN.value.length==1)
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>_DECPEN.value='0'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>_DECPEN.value;
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>_DECPEN.value.length==1)
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>_DECPEN.value='0'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>_DECPEN.value;
      
      var dataProvv =     document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>_DECPEN.value
                     +'/'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>_DECPEN.value
                     +'/'+document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO%>_DECPEN.value;
      
      if (! ControllaData(dataProvv))
      {
        alert('Data Provvedimento non valida');
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>_DECPEN.focus();
        return false;
      }
      
      // Autorità emittente
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>_DECPEN.value=='-'){
        alert('Selezionare il tipo Autorità Emittente');
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>_DECPEN.focus();
        return false;
      }
      if (document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>_DECPEN.value==''){
        alert('Indicare la Sede Autorità Emittente');
        document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>_DECPEN.focus();
        return false;
      }

      return true;
    }


    //==========================================================================
    // Controlli per le sezioni: Sentenza Sraniera e Provvedimento Sorveglianza
    //==========================================================================
    function verifyAltro (tipoTitolo){
      // Data provvedimento
      if (document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>"+tipoTitolo)[0].value.length==1)
        document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>"+tipoTitolo)[0].value='0'+document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>"+tipoTitolo)[0].value;
      if (document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>"+tipoTitolo)[0].value.length==1)
        document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>"+tipoTitolo)[0].value='0'+document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>"+tipoTitolo)[0].value;
      
      var dataProvv =     document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>"+tipoTitolo)[0].value
                     +'/'+document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>"+tipoTitolo)[0].value
                     +'/'+document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO%>"+tipoTitolo)[0].value;
      
      
      if (! ControllaData(dataProvv))
      {
        alert('Data Provvedimento non valida');
        document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>"+tipoTitolo)[0].focus();
        return false;
      }
      
      // Anno provv
      if (document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA%>"+tipoTitolo)[0].value==''){
        alert('Indicare anno provvedimento');
        document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA%>"+tipoTitolo)[0].focus();
        return false;
      }
      // Numero provv
      if (document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA%>"+tipoTitolo)[0].value==''){
        alert('Indicare il numero di provvedimento');
        document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA%>"+tipoTitolo)[0].focus();
        return false;
      }     

      // Autorità emittente
      if (document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>"+tipoTitolo)[0].value=='-'){
        alert('Selezionare il tipo Autorità Emittente');
        document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>"+tipoTitolo)[0].focus();
        return false;
      }
      if (document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>"+tipoTitolo)[0].value==''){
        alert('Indicare la Sede Autorità Emittente');
        document.getElementsByName("<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>"+tipoTitolo)[0].focus();
        return false;
      }
      return true;
    }



    //========================================================
    //
    //========================================================
    function carica()
    {      
      <% if (modalita.equals("M")) { %>
      var codTipoProvv = "<%=StringUtils.toStringJSP(titolocumulato.getCodTipoProvvedimento())%>";
      document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value = codTipoProvv;
      document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.onchange();
      
        <% if ("01".equals(titolocumulato.getCodTipoProvvedimento())) { %>
        // Sentenza
          var codAutEmittente = "<%=StringUtils.toStringJSP(lTitoloCumulato.getCodTipoAutoritaEmittente())%>";
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value = codAutEmittente;
          
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.onchange();
          
          var codTipoRitoEmi = "<%=StringUtils.toStringJSP(lTitoloCumulato.getCodTipoRito())%>";
          if (codTipoRitoEmi!="") 
            document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_RITO%>.value = codTipoRitoEmi;
    
          
          // Altro grado di giudizio
          var codTipoProvvRif = "<%=StringUtils.toStringJSP(lTitoloCumulato.getCodTipoProvvedimentoRif())%>";
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF%>.value = codTipoProvvRif;
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF%>.onchange();
        
          var codTipoSentenzaRif = "<%=StringUtils.toStringJSP(lTitoloCumulato.getCodTipoProvvRif())%>";
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVV_RIF%>.value = codTipoSentenzaRif;
        
          var codAutEmittenteRif = "<%=StringUtils.toStringJSP(lTitoloCumulato.getCodTipoAutoritaProvvRif())%>";
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF%>.value = codAutEmittenteRif;
        
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF%>.onchange();
          
          var codTipoRitoRif = "<%=StringUtils.toStringJSP(lTitoloCumulato.getCodTipoRitoRif())%>";
          if (codTipoRitoRif!="") 
            document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_RITO_RIF%>.value = codTipoRitoRif;
    
        
          // Decisioni della Cassazione
          var codTipoProvvAltro = "<%=StringUtils.toStringJSP(lTitoloCumulato.getCodTipoProvvedimentoAltro())%>";
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO%>.value = codTipoProvvAltro;
    
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO%>.onchange();
          
          var codDispositivo = "<%=StringUtils.toStringJSP(lTitoloCumulato.getCodTipoDecisioneCassazione())%>";
          if (codDispositivo!="")
            document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE%>.value = codDispositivo;
        <% } else if ("02bis".equals(titolocumulato.getCodTipoProvvedimento())) { %>
          // Decreto Penale
          var codAutEmittente = "<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getCodTipoAutoritaEmittente())%>";
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>_DECPEN.value = codAutEmittente;
        
          var codDispositivo = "<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getCodTipoDecisioneCassazione())%>";
          if (codDispositivo!="")
            document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE%>_DECPEN.value = codDispositivo;
        <% } else if ("05".equals(titolocumulato.getCodTipoProvvedimento())) { %>
          // Sentenza Straniera
          var codAutEmittente = "<%=StringUtils.toStringJSP(lTitoloCumulatoSenStra.getCodTipoAutoritaEmittente())%>";
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>_SENT_STRA.value = codAutEmittente;
        <% } else if ("02".equals(titolocumulato.getCodTipoProvvedimento()) || "03".equals(titolocumulato.getCodTipoProvvedimento())) { %>
          // Decreto o Ordinanza della Sorveglianza
          var codAutEmittente = "<%=StringUtils.toStringJSP(lTitoloCumulatoSorv.getCodTipoAutoritaEmittente())%>";
          document.LoadInserisciTitoloCumulato.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>_SORV.value = codAutEmittente;
        <% } %>
      <% } // end if modifica%>
    
    }

  </script>
</head>

<body class="corpo" onLoad="carica()">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <% if (modalita.equals("I")) { %>
        <font class="campo">Inserimento Manuale Titolo Soggetto a Cumulo</font>
        <% } else { %>
        <font class="campo">Modifica Titolo Soggetto a Cumulo</font>
        <% } %>       
      </td>
      <td class="LBG"><!-- Tasto indietro -->
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>       
    </tr>
  </table>
 
  <br>
  <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>
    
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="LoadInserisciTitoloCumulato">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciTitoloCumulato">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">

  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>" value="<%=StringUtils.toStringJSP(titolocumulato.getIdTitoloCumulato()) %>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_FLAG_STATO%>"         value="<%=StringUtils.toStringJSP(titolocumulato.getFlagStato()) %>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>"    value="<%=StringUtils.toStringJSP(titolocumulato.getMotivoModifica()) %>">

  <input type="hidden" name="modalita" value="<%=modalita%>">

    
<table cellspacing=2 cellpadding=2>
  <tr>
    <td class="l">Data Irrevocabilità <font class="ob">(*)</font></td>
    <td class="L">
      <input type="text" maxlength="2" size="2" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIrrevocabilita,"dd")) %>" 
             name="<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_IRREVOCABILITA%>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
      /
      <input type="text" maxlength="2" size="2" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIrrevocabilita,"MM")) %>" 
             name="<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_IRREVOCABILITA%>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
      /
      <input type="text" maxlength="4" size="4"
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIrrevocabilita,"yyyy")) %>" 
             name="<%=ICostantiTitoloCumulato.CAMPO_ANNO_DATA_IRREVOCABILITA%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
    </td>
  </tr>
  
  <tr>
    <td class="L" width="30%">Tipo Provvedimento <font class="ob">(*)</font></td>
    <td class="L">
      <select name="<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>" onchange="Javascript:ViewDiv();">
        <%//=TipoProvv%>
        <option value="-">-</option>
        <option value="01">Sentenza</option>
        <option value="02bis">Decreto Penale</option>
        <option value="05">Sentenza (di riconoscimento di sentenza straniera)</option>
        <option value="02">Decreto</option>
        <option value="03">Ordinanza</option>
      </select>
    </td>
  </tr> 
</table>


<%
//==============================================================================
//                                  SENTENZA
//==============================================================================
%>
<div id="divSentenza" style="display:none"> 
<table cellspacing="2" cellpadding="2" width="95%" >
  <tr><td class="Titolo" colspan="100%">Sentenza</td></tr>
  <tr>
    <td class="l">Anno/Numero R.G.N.R. <font class="ob">(*)</font></td>
    <td class="L">
      <input type="text" maxlength="4" size="6" 
             Title="Anno R.G.N.R."
             name="<%=ICostantiTitoloCumulato.CAMPO_ANNO_REGE_PM %>"  
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getAnnoRegePm()) %>"
             onkeypress="return TicTabNumField(this,event)" 
             onBlur="javascript:value=FillYear(value)"> 
      <input type="text" maxlength="8" size="8" 
             Title="Numero R.G.N.R."
             name="<%=ICostantiTitoloCumulato.CAMPO_NUMERO_REGE_PM %>"  
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getNumeroRegePm()) %>"
             onkeypress="return TicTabNumField(this,event)" >
    </td>
    <td class="l">Anno/Numero Reg.Gen. <font class="ob">(*)</font></td>
    <td class="L">
      <input type="text" maxlength="4" size="6" Title="Anno Reg.Gen."
             name="<%=ICostantiTitoloCumulato.CAMPO_ANNO_REG_GEN %>"  
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getAnnoRegGen()) %>"
             onkeypress="return TicTabNumField(this,event)" 
             onBlur="javascript:value=FillYear(value)"> 
      /
      <input type="text" maxlength="8" size="8" title="Numero Reg.Gen."
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getNumeroRegGen()) %>"
             name="<%= ICostantiTitoloCumulato.CAMPO_NUMERO_REG_GEN %>" 
             onkeypress="return TicTabNumField(this,event)"  
             > 
      
      <select name="<%= ICostantiTitoloCumulato.CAMPO_TIPO_REG_GEN %>" >
        <%=tipoRegGen%>
      </select>
    </td>
  </tr>
  
  <tr>
    <td class="l">Sede PM <font class=ob>(*)</font></td>    
    <td class="L">
      <input type="text"  Title="Sede PM" maxlength="35" size="35"
             name="<%= ICostantiTitoloCumulato.CAMPO_COD_SEDE_NOTIZIA_REATO %>" 
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getDescrSedeNotiziaReato()) %>">
    </td>
  </tr>
  
  <%//========================================================================%>
  <tr>
    <td class="l">Data Provvedimento <font class="ob">(*)</font></td>
    <td class="l"> 
      <input type="text" size="2" maxlength="2" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulato.getDataProvvedimento(),"dd")) %>" 
             name="<%= ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
       >&nbsp;/&nbsp;
      <input type="text" size="2" maxlength="2" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulato.getDataProvvedimento(),"MM")) %>" 
             name="<%= ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
       >&nbsp;/&nbsp;
      <input type="text" size="4" maxlength="4" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulato.getDataProvvedimento(),"yyyy")) %>" 
             name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
    
    <td class="l">Anno/Numero Sentenza <font class="ob">(*)</font></td>
    <td class="l">     
      <input type="text" maxlength="4" size="6"  Title="Anno Sentenza" 
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getAnnoSentenza()) %>"
             name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA %>" 
             onkeypress="return TicTabNumField(this,event)" 
             onBlur="javascript:value=FillYear(value)">
      &nbsp;/&nbsp;
      <input type="text" maxlength="8" size="8" Title="Numero Sentenza"
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getNumeroSentenza()) %>"
             name="<%= ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA %>"  
             onkeypress="return TicTabNumField(this,event)" > 
    </td>
  </tr>


  <tr>
    <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
    <td class="L">
      <select Title="Autorità Emittente" 
              onChange="abilitaTipoRito(this, document.getElementsByName('<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_RITO %>')[0]
                                       , document.getElementsByName('<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF%>')[0]
                                       )" 
              name="<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>"  >
      <%=autoritaEmi%>
      </select>
    </td>    
 
    <td class="l">Tipo Rito &nbsp;</td>
    <td class="l">
      <select Title="Tipo Rito" name="<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_RITO %>" disabled>
        <%=tipoRito%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Luogo Emittente <font class=ob>(*)</font></td>
    <td class="l"> 
      <input type="text" maxlength="35" size="35" Title="Luogo Emittente"
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getDescrLuogoEmittente(),"") %>"
             name="<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>"  
             >
      <a href="Javascript:ListaUfficiPerTipo('LoadInserisciTitoloCumulato','<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>',document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.selectedIndex].value);">
        <img src="/images/filefolder.gif" border=0> </a>                
    </td> 
    <td class="L">Sezione Autorità Emittente</td>
    <td class="l"> 
      <input type="text" maxlength="35" size="35" Title="Sezione Autorità Emittente" 
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getNumSezioneAutoritaEmittente()) %>"
             name="<%= ICostantiTitoloCumulato.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE %>"  
             > 
    </td> 
  </tr> 
  
  <%
  //============================================================================
  //                          ALTRO GRADO DI GIUDIZIO
  //============================================================================  
  %>
<tr id="trAGDG" style="display:block">
  <td colspan=4>
    <table width="100%">
  
  <tr>
    <td class="Titolo" colspan=4>Altro Grado di Giudizio</td>
  </tr>
  <tr>
    <td class="l">Tipo Provvedimento</td>
    <td class="L">
      <select Title="Tipo Provvedimento Riferimento"
              name="<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>" 
              onchange="gestisciTipoProvvAGDG(this)"
              >
        <%=tipoProvvedimenti%>
      </select>
    </td>
  <!--/tr>
  <tr id="tipoSentenza" style="display:block"-->
    <td class="l">Tipo Sentenza</td>
    <td class="L">
      <select Title="Tipo Sentenza Riferimento"
              name="<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVV_RIF %>" >
        <%=tipoProvvedimentiRif%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Data <div id="labelSentenza">Sentenza</div><div id="labelOrdinanza" style="display:none">Ordinanza</div></td>
    <td class="l"> 
      <input type="text" size="2" maxlength="2" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulato.getDataProvvRif(),"dd")) %>" 
             name="<%= ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVV_RIF %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
       >&nbsp;/&nbsp;
      <input type="text" size="2" maxlength="2" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulato.getDataProvvRif(),"MM")) %>" 
             name="<%= ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVV_RIF %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
       >&nbsp;/&nbsp;
      <input type="text" size="4" maxlength="4" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulato.getDataProvvRif(),"yyyy")) %>" 
             name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVV_RIF %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
    <td class="l">Anno/Numero <div id="anSentenza">Sentenza</div><div id="anOrdinanza" style="display:none">Ordinanza</div></td>
    <td class="l"> 
      <input type="text" maxlength="4" size="6" 
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getAnnoProvvRif()) %>"
             name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_PROVV_RIF %>" 
             onkeypress="return TicTabNumField(this,event)" 
             onBlur="javascript:value=FillYear(value)" > 
      &nbsp;/&nbsp;
      <input type="text" maxlength="8" size="8" 
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getNumeroProvvRif()) %>"
             name="<%= ICostantiTitoloCumulato.CAMPO_NUMERO_PROVV_RIF %>" > 
    </td> 
  </tr>
  
  <tr>
    <td class="l">Autorità Emittente</td>
    <td class="L">
      <select Title="Autorità Sentenza Riferimento"
              name="<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>"  
              onChange="abilitaTipoRito(this, document.getElementsByName('<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_RITO_RIF %>')[0]
                                       , document.getElementsByName('<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>')[0]
                                       )" >
        <%=autoritaEmi%>
      </select>
    </td>
    <td class="l">Tipo Rito &nbsp;</td>
    <td class="L">
      <select Title="Tipo Rito Riferimento"  name="<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_RITO_RIF %>" disabled>
        <%=tipoRito%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Luogo Emittente</td>
    <td class="L">
      <input type="text" Title="Luogo Sentenza Riferimento"
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getDescrLuogoProvvRif()) %>"
             name="<%= ICostantiTitoloCumulato.CAMPO_COD_LUOGO_PROVV_RIF %>"
             maxlength="35" size="35"> 
      <a href="Javascript:ListaUfficiPerTipo('LoadInserisciTitoloCumulato','<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_PROVV_RIF %>',document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>[document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.selectedIndex].value);">
        <img src="/images/filefolder.gif" border=0> </a>     
    
    </td>
    <td class="l">Sezione Autorità Emittente</td>
    <td class="L">
      <input type="text" Title="Sezione Autorità Riferimento"
             value="<%=StringUtils.toStringJSP(lTitoloCumulato.getNumSezioneAutoritaProvvRif()) %>"
             name="<%= ICostantiTitoloCumulato.CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF %>"  
             maxlength="100" size="35">
    </td>
  </tr>
</table>
</td>
</tr>
  <%
  //============================================================================
  //                        DECISIONE DELLA CASSAZIONE
  //============================================================================
  %>
<tr id="trCassazione" style="display:block">
  <td colspan=4>
    <table width="100%">
      <tr>
        <td class="Titolo" colspan=4>Decisione Cassazione</td>
      </tr>
      <tr>
        <td class="l">Tipo Provvedimento</td>
        <td class="L">
          <select Title="Tipo Provvedimento Cassazione"
                  name="<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>"
                  onChange="gestisciTipoProvvCASS(this)">
            <%=tipoProvvedimenti%>
          </select>
        </td>
        <td class="l">Anno/Numero<div id="lblSentenza">Sentenza</div><div id="lblOrdinanza" style="display:none">Ordinanza</div></td> 
        <td class="L">
          <input type="text" Title="Anno Sentenza Cassazione" maxlength="4" size="4"  
                 value="<%=StringUtils.toStringJSP(lTitoloCumulato.getAnnoSentenzaCassazione()) %>"
                 name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA_CASSAZIONE %>"  
                 onkeypress="return TicTabNumField(this,event)"
                 onBlur="javascript:value=FillYear(value)" > 
          &nbsp;/&nbsp; 
          <input type="text" Title="Numero Sentenza Cassazione" maxlength="8" size="8" 
                 value="<%=StringUtils.toStringJSP(lTitoloCumulato.getNumeroSentenzaCassazione()) %>"
                 name="<%= ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA_CASSAZIONE %>" > 
        </td>    
      </tr>
      <tr id="anRegGen">
        <td class="l">Anno/Numero Reg.Gen.</td>
        <td class="L">
          <input type="text" Title="Anno Re.Ge. CASSAZIONE" maxlength="4" size="4"
                 value="<%=StringUtils.toStringJSP(lTitoloCumulato.getNote1DecisioneCassazione()) %>"
                 name="<%= ICostantiTitoloCumulato.CAMPO_NOTE1_DECISIONE_CASSAZIONE %>"
                 onkeypress="return TicTabNumField(this,event)"
                 onBlur="javascript:value=FillYear(value)"> 
          &nbsp;/&nbsp; 
          <input type="text" Title="Numero Re.Ge. CASSAZIONE" maxlength="8" size="8"
                 value="<%=StringUtils.toStringJSP(lTitoloCumulato.getNote2DecisioneCassazione()) %>"
                 name="<%= ICostantiTitoloCumulato.CAMPO_NOTE2_DECISIONE_CASSAZIONE %>"
                 >
        </td>
      </tr>
      <tr id="anRacGen"> 
        <td class="l">Anno/Numero Raccolta Generale</td>
        <td class="L">    
          <input type="text" Title="Anno Raccolta Generale" maxlength="4" size="4"
                 value="<%=StringUtils.toStringJSP(lTitoloCumulato.getAnnoRaccoltaGenerale()) %>"
                 name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_RACCOLTA_GENERALE %>"
                 onkeypress="return TicTabNumField(this,event)"
                 onBlur="javascript:value=FillYear(value)"
                 > 
          &nbsp;/&nbsp;      
          <input type="text" Title="Numero Raccolta Generale" maxlength="8" size="8"
                 value="<%=StringUtils.toStringJSP(lTitoloCumulato.getNumeroRaccoltaGenerale()) %>"
                 name="<%= ICostantiTitoloCumulato.CAMPO_NUMERO_RACCOLTA_GENERALE %>"
                 >
        </td>
      </tr>  
      <tr id="disp">
        <td class="l">Dispositivo</td>
        <td class="L" colspan="4">
          <select Title="Dispositivo Cassazione"
                  name="<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>" >
            <%=tipoDecisioneCassazione%>
          </select>
        </td>
      </tr>  
    </table>
  </td>
</tr>
      
      
      
  <tr>
    <td class="l">Note</td>
    <td class="L" colspan=3>
      <textarea cols="80" rows="5" Title="Note Aggiuntive"
                name="<%=ICostantiTitoloCumulato.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(lTitoloCumulato.getNote()) %></textarea>
    </td>
  </tr>
</table>
</div>

<%
//==============================================================================
// DIV per i dati 02bis = decreto penale
// questi titolo hano un numero limitato di dati
//==============================================================================
%>
<div id="divDecrPenale" style="display:none"> 
  <table cellspacing="2" cellpadding="2" width="95%" >
    <tr><td class="Titolo" colspan="100%">Decreto Penale da Eseguire</td></tr>
    <tr>
      <td class="l">Anno/Numero R.G.N.R. <font class="ob">(*)</font></td>
      <td class="L">
        <input type="text" maxlength="4" size="6" 
               Title="Anno R.G.N.R."
               name="<%=ICostantiTitoloCumulato.CAMPO_ANNO_REGE_PM %>_DECPEN"  
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getAnnoRegePm()) %>"
               onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillYear(value)"> 
        <input type="text" maxlength="8" size="8" 
               Title="Numero R.G.N.R."
               name="<%=ICostantiTitoloCumulato.CAMPO_NUMERO_REGE_PM %>_DECPEN"  
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getNumeroRegePm()) %>"
               onkeypress="return TicTabNumField(this,event)" >
      </td>
      <td class="l">Anno/Numero Reg.Gen. GIP <font class="ob">(*)</font></td>
      <td class="L">
        <input type="text" maxlength="4" size="6" Title="Anno Reg.Gen."
               name="<%=ICostantiTitoloCumulato.CAMPO_ANNO_REG_GEN %>_DECPEN"  
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getAnnoRegGen()) %>"
               onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillYear(value)"> 
        /
        <input type="text" maxlength="8" size="8" title="Numero Reg.Gen."
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getNumeroRegGen()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_NUMERO_REG_GEN %>_DECPEN" 
               onkeypress="return TicTabNumField(this,event)"  
               > 
        <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_TIPO_REG_GEN %>_DECPEN" value="GIP">
      </td>
    </tr>
    
    <tr>
      <td class="l">Sede PM <font class=ob>(*)</font></td>    
      <td class="L">
        <input type="text"  Title="Sede PM" maxlength="35" size="35"
               name="<%= ICostantiTitoloCumulato.CAMPO_COD_SEDE_NOTIZIA_REATO %>_DECPEN" 
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getDescrSedeNotiziaReato()) %>">
      </td>
    </tr>
   
    <tr>
      <td class="l">Data Decreto <font class="ob">(*)</font></td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulatoDecPen.getDataProvvedimento(),"dd")) %>" 
               name="<%= ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>_DECPEN" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulatoDecPen.getDataProvvedimento(),"MM")) %>" 
               name="<%= ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO %>_DECPEN" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulatoDecPen.getDataProvvedimento(),"yyyy")) %>" 
               name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO %>_DECPEN" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      
      <td class="l">Anno/Numero Decreto <font class="ob">(*)</font></td>
      <td class="l">     
        <input type="text" maxlength="4" size="6"  Title="Anno Sentenza" 
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getAnnoSentenza()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA %>_DECPEN" 
               onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillYear(value)">
        &nbsp;/&nbsp;
        <input type="text" maxlength="8" size="8" Title="Numero Sentenza"
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getNumeroSentenza()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA %>_DECPEN"  
               onkeypress="return TicTabNumField(this,event)" > 
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      <td class="L">
        <select Title="Autorità Emittente" 
                name="<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>_DECPEN"  >
        <%=autoritaEmi%>
        </select>
      </td>   
    </tr>
    <tr>
      <td class="l">Luogo Emittente <font class=ob>(*)</font></td>
      <td class="l"> 
        <input type="text" maxlength="35" size="35" Title="Luogo Emittente"
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getDescrLuogoEmittente(),"") %>"
               name="<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>_DECPEN" >
        <a href="Javascript:ListaUfficiPerTipo('LoadInserisciTitoloCumulato','<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>_DECPEN',document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>_DECPEN[document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>_DECPEN.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0> </a>                
      </td> 
      <td class="L">Sezione Autorità Emittente</td>
      <td class="l"> 
        <input type="text" maxlength="35" size="35" Title="Sezione Autorità Emittente" 
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getNumSezioneAutoritaEmittente()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE %>_DECPEN"  
               > 
      </td> 
    </tr>
    
    <tr><td class="Titolo" colspan="100%">Sentenza Cassazione</td></tr>
    <tr>
      <td class="l">Anno/Numero Sentenza</td> 
      <td class="L">
        <input type="text" Title="Anno Sentenza Cassazione" maxlength="4" size="4"  
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getAnnoSentenzaCassazione()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA_CASSAZIONE %>_DECPEN"  
               onkeypress="return TicTabNumField(this,event)"
               onBlur="javascript:value=FillYear(value)" > 
        &nbsp;/&nbsp; 
        <input type="text" Title="Numero Sentenza Cassazione" maxlength="8" size="8" 
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getNumeroSentenzaCassazione()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA_CASSAZIONE %>_DECPEN" > 
      </td>    
    </tr>
    <tr> 
      <td class="l">Anno/Numero Raccolta Generale</td>
      <td class="L">    
        <input type="text" Title="Anno Raccolta Generale" maxlength="4" size="4"
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getAnnoRaccoltaGenerale()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_RACCOLTA_GENERALE %>_DECPEN"
               onkeypress="return TicTabNumField(this,event)"
               onBlur="javascript:value=FillYear(value)"
               > 
        &nbsp;/&nbsp;      
        <input type="text" Title="Numero Raccolta Generale" maxlength="8" size="8"
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getNumeroRaccoltaGenerale()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_NUMERO_RACCOLTA_GENERALE %>_DECPEN"
               >
      </td>
    </tr>  
    <tr>
      <td class="l">Dispositivo Cassazione</td>
      <td class="L" colspan="4">
        <select Title="Dispositivo Cassazione"
                name="<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>_DECPEN" >
          <%=tipoDecisioneCassazione%>
        </select>
      </td>
    </tr>  
    <tr>
      <td class="l">Note</td>
      <td class="L" colspan=3>
        <textarea cols="80" rows="5" Title="Note Aggiuntive"
                  name="<%=ICostantiTitoloCumulato.CAMPO_NOTE%>_DECPEN"><%=StringUtils.toStringJSP(lTitoloCumulatoDecPen.getNote()) %></textarea>
      </td>
    </tr>   
  </table>
</div>

<%
//==============================================================================
// DIV per i dati 05 = sentenza straniera
// questi titolo hano un numero limitato di dati
//==============================================================================
%>
<div id="divStraniera" style="display:none"> 
  <table cellspacing="2" cellpadding="2" width="95%" >
    <tr><td class="Titolo" colspan="100%">Sentenza di Appello da eseguire</td></tr>
    <tr>
      <td class="l">Data Provvedimento <font class="ob">(*)</font></td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulatoSenStra.getDataProvvedimento(),"dd")) %>" 
               name="<%= ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>_SENT_STRA" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulatoSenStra.getDataProvvedimento(),"MM")) %>" 
               name="<%= ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO %>_SENT_STRA" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulatoSenStra.getDataProvvedimento(),"yyyy")) %>" 
               name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO %>_SENT_STRA" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      
      <td class="l">Anno/Numero Sentenza <font class="ob">(*)</font></td>
      <td class="l">     
        <input type="text" maxlength="4" size="6"  Title="Anno Sentenza" 
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoSenStra.getAnnoSentenza()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA %>_SENT_STRA" 
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        &nbsp;/&nbsp;
        <input type="text" maxlength="8" size="8" Title="Numero Sentenza"
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoSenStra.getNumeroSentenza()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA %>_SENT_STRA"  
               onkeypress="return TicTabNumField(this,event)" > 
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      <td class="L">
        <select Title="Autorità Emittente" name="<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>_SENT_STRA">
        	<%=autoritaEmi%>
        </select>
      </td>   
    </tr>
    <tr>
      <td class="l">Luogo Emittente <font class=ob>(*)</font></td>
      <td class="l"> 
        <input type="text" maxlength="35" size="35" Title="Luogo Emittente"
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoSenStra.getDescrLuogoEmittente(),"") %>"
               name="<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>_SENT_STRA" >
        <a href="Javascript:ListaUfficiPerTipo('LoadInserisciTitoloCumulato','<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>_SENT_STRA',document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>_SENT_STRA[document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>_SENT_STRA.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0> </a>                
      </td> 
      <td class="L">Sezione Autorità Emittente</td>
      <td class="l"> 
        <input type="text" maxlength="35" size="35" Title="Sezione Autorità Emittente" 
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoSenStra.getNumSezioneAutoritaEmittente()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE %>_SENT_STRA"  
               > 
      </td> 
    </tr>

    <tr><td class="Titolo" colspan="100%">Sentenza di Riferimeno</td></tr>
    <tr>
      <td class="l">Estremi Sentenza Straniera </td>
      <td class="L" colspan=3>
        <textarea cols="80" rows="5" Title="Estremi Sentenza Straniera"
                  name="<%=ICostantiTitoloCumulato.CAMPO_NOTE%>_SENT_STRA"><%=StringUtils.toStringJSP(lTitoloCumulatoSenStra.getNote()) %></textarea>
      </td>
    </tr>   

  </table>
</div>


<%
//==============================================================================
// DIV per i dati di 02-Decreto - 03-ordinanza
// questi titolo hano un numero limitato di dati
//==============================================================================
%>
<div id="divSorveglianza" style="display:none"> 
  <table cellspacing="2" cellpadding="2" width="95%" >
    <tr><td class="Titolo" colspan="100%">Provvedimento della Sorveglianza</td></tr>
    <tr>
      <td class="l">Data Provvedimento <font class="ob">(*)</font></td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulatoSorv.getDataProvvedimento(),"dd")) %>" 
               name="<%= ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>_SORV" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulatoSorv.getDataProvvedimento(),"MM")) %>" 
               name="<%= ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO %>_SORV" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloCumulatoSorv.getDataProvvedimento(),"yyyy")) %>" 
               name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO %>_SORV" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      
      <td class="l">Anno/Numero Provvedimento <font class="ob">(*)</font></td>
      <td class="l">     
        <input type="text" maxlength="4" size="6"  Title="Anno Sentenza" 
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoSorv.getAnnoSentenza()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA %>_SORV" 
               onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillYear(value)">
        &nbsp;/&nbsp;
        <input type="text" maxlength="8" size="8" Title="Numero Sentenza"
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoSorv.getNumeroSentenza()) %>"
               name="<%= ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA %>_SORV"  
               onkeypress="return TicTabNumField(this,event)" > 
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      <td class="L">
        <select Title="Autorità Emittente" name="<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>_SORV">
        <%=autoritaEmiSorv%>
        </select>
      </td>   
    </tr>
    <tr>
      <td class="l">Luogo Emittente <font class=ob>(*)</font></td>
      <td class="l"> 
        <input type="text" maxlength="35" size="35" Title="Luogo Emittente"
               value="<%=StringUtils.toStringJSP(lTitoloCumulatoSorv.getDescrLuogoEmittente(),"") %>"
               name="<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>_SORV" >
        <a href="Javascript:ListaUfficiPerTipo('LoadInserisciTitoloCumulato','<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>_SORV',document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>_SORV[document.LoadInserisciTitoloCumulato.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>_SORV.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0> </a>                
      </td> 
    </tr>
  </table>
</div>

  <br>

  <table>
    <tr>
      <td class="lNoBord">
        <input type="submit" class="bottone"  value="Conferma">
      </td>
    </tr>
  </table>
</form>



<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciTitoloCumulato");
  frmvalidator.setAddnlValidationFunction("Verify");
  



</script>
</body>
</html>