<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.modulocumulo.model.MisuraCautelareCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraCautelareCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>


<jsp:useBean id="tipoMisuraDetentive"    scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraNonDetentive" scope="request" class="java.lang.String"/>
<jsp:useBean id="comboAutCompTerritorio" scope="request" class="java.lang.String"/>

<%// SE IN MODIFICA %>
<jsp:useBean id="MisuraCautelareCumulo" scope="request" class="siap.siep.modulocumulo.model.MisuraCautelareCumuloModel"/>
<jsp:useBean id="IstitutoDetenzione"    scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="TipoEspiazione"        scope="request" class="java.lang.String"/>

<% 
//============================================================================== 
// Form per l'inserimento e la modifica delle Misure Cautelari in cumulo
//============================================================================== 
%> 

<html>
<head>
  <title> Gestione Presofferto Cumulo </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript" src="/html/jsrsClient.js"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_JQUERY%>></script>  
  
  <script language="JavaScript" >
    var anniQuantumCalcolati = 0;
    var mesiQuantumCalcolati = 0;
    var giorniQuantumCalcolati = 0;
    var totGGMessaAllaProva = 0;
    
    var testOnLoad = 'S';
    
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
    
    function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio
                          , "Ricerca_Ufficio"
                          ,"toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    //==========================================================================
    // Ritorna alla lista delle Misure Cautelari
    //==========================================================================
    function eseguiFunzione(action)
    {
      document.LoadInserisciMisuraCautelareCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.LoadInserisciMisuraCautelareCumulo.submit();
    }
    

    
    //==========================================================================
    // 
    //==========================================================================
    function clearQuantum(){
      //document.LoadInserisciMisuraCautelareCumulo.< %=ICostantiMisuraCautelareCumulo.CAMPO_NUM_ANNI%>.value = "";
      //document.LoadInserisciMisuraCautelareCumulo.< %=ICostantiMisuraCautelareCumulo.CAMPO_NUM_MESI%>.value = "";
      //document.LoadInserisciMisuraCautelareCumulo.< %=ICostantiMisuraCautelareCumulo.CAMPO_NUM_GIORNI%>.value = "";
      //document.LoadInserisciMisuraCautelareCumulo.< %=ICostantiMisuraCautelareCumulo.CAMPO_GIORNI%>.value = "";

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
    function testCalcolaPresofferto(par){
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
    
    //==========================================================================    
    // Verifica la congruenza dei periodi di Presofferto (data dal <= data al)
    //==========================================================================    
    function controllaPeriodi(reply)
    {
      var sysDate = "<%=DateUtils.getSysDate("dd/MM/yyyy")%>";
    
      var gg_dal = document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_INIZIO%>;
      var mm_dal = document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_MESE_DATA_INIZIO%>;
      var aa_dal = document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_DATA_INIZIO%>;

      var gg_al = document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_FINE%>;
      var mm_al = document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_MESE_DATA_FINE%>;
      var aa_al = document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_DATA_FINE%>;
      
      
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

      // Data Fine non obbligatoria ma se inserita effettuo i controlli
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;
      if (dataAL!="//")
      {
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
      }
           
      return true;
    }
    
    //==========================================================================
    // Affettua la chiamata sincrona alla servlet di calcolo quantum
    //==========================================================================
    function callCalcolaQuantum () {
      //alert("callCalcolaQuantum: ");
      
      //if (controllaPeriodi(idMC)==false)
      //  return;
      
    
      var gg_dal = document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_INIZIO%>;
      var mm_dal = document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_MESE_DATA_INIZIO%>;
      var aa_dal = document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_DATA_INIZIO%>;

      var gg_al = document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_FINE%>;
      var mm_al = document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_MESE_DATA_FINE%>;
      var aa_al = document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_DATA_FINE%>;
      
      var dataDAL = gg_dal.value +"/"+mm_dal.value+"/"+aa_dal.value;
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;
      
      // aggiungo il parametro TipoMisuraCautelare (Cod)
      var tipoEspiazione = $("[name=<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE%>]:checked").val();
      var ObjComboTipoMisura;
      if (tipoEspiazione=='<%=ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO%>'){
        ObjComboTipoMisura = $("#divComboDetentive [name=<%= ICostantiMisuraCautelareCumulo.CAMPO_COD_TIPO_MISURA %>]");
      }
      else {
        ObjComboTipoMisura = $("#divComboNonDetentive [name=<%= ICostantiMisuraCautelareCumulo.CAMPO_COD_TIPO_MISURA %>]");
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
    // Funzione di callback invocata di ritorno dalla servlet
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
    
    function radioTipoEspiazione(par)
    {
      var nomeRadio = '<%= ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE %>';
      
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
        ObjComboTipoMisura = $("#divComboDetentive [name=<%= ICostantiMisuraCautelareCumulo.CAMPO_COD_TIPO_MISURA %>]");
      }
      else {
        ObjComboTipoMisura = $("#divComboNonDetentive [name=<%= ICostantiMisuraCautelareCumulo.CAMPO_COD_TIPO_MISURA %>]");
      }
  
      //var nodeQuantum = document.getElementById('divquantum');
  
      if(ObjComboTipoMisura.val()=='CL')
      {
        document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUM_GIORNI%>.disabled=false;
        document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUM_MESI%>.disabled=false;
        document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUM_ANNI%>.disabled=false;
        
        $("#tdGiorni").show();
        document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNI%>.disabled=false;
      } 
      else
      {
        document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUM_GIORNI%>.disabled=true;
        document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUM_MESI%>.disabled=true;
        document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUM_ANNI%>.disabled=true;
        
        $("#tdGiorni").hide();
        document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNI%>.disabled=true;
      }
      
      if (par!='onLoad') // non effettuo i calcoli quantum se sono chiamato dall'onLoad
        testCalcolaPresofferto();
    }
    
    //============================================================================
    // 
    //============================================================================
    function Verify() { 
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
      
      if (ObjComboTipoMisura.val()=='-'){
        alert('Selezionare la natura della misura');
        ObjComboTipoMisura.focus();
        return false;
      }

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
      
      // n.b. questi dati non sono obbligatori nemmeno su SIEP per cui sono lasciati 
      //      liberi nel modulo cumulo
      /*
      if (tipoEspiazione=='<%=ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO%>'){
        //Detentiva
        if (document.LoadInserisciMisuraCautelareCumulo.DescrizioneIstituto.value==''){
          alert("Indicare l'istituto di detenzione");
          //document.LoadInserisciMisuraCautelareCumulo.DescrizioneIstituto.focus();
          return false;
        }
      }
      else {
        if (document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ALTRO_LUOGO_DETENZIONE%>.value==''){
            alert("Indicare il Luogo di espiazione ");
            document.LoadInserisciMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ALTRO_LUOGO_DETENZIONE%>.focus();
            return false;
        }
      }
      */


      return true; 
    } 
    
    // On load
    $(document).ready(function(){
      //radioTipoEspiazione('onLoad');
      radioTipoEspiazione();
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
        <%
        MisuraCautelareCumuloModel lMisuraCautelareCumulo = new MisuraCautelareCumuloModel(); 
        if( modalita.equals("I") ) {
        %>
        <font class="campo">Inserimento Misura Cautelare Per Titolo &nbsp;</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lMisuraCautelareCumulo = MisuraCautelareCumulo;
        %>
        <font class="campo">Modifica Misura Cautelare Per Titolo &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaMisureCautelariCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
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


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraCautelareCumulo">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciMisuraCautelareCumulo">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%= ICostantiMisuraCautelareCumulo.CAMPO_ID_MISURA_CAUTELARE_CUMULO %>" value="<%=StringUtils.toStringJSP(lMisuraCautelareCumulo.getIdMisuraCautelareCumulo()) %>">
  <input type="hidden" name="<%= ICostantiMisuraCautelareCumulo.CAMPO_FLAG_STATO %>"                 value="<%=StringUtils.toStringJSP(lMisuraCautelareCumulo.getFlagStato()) %>">
                 
  <input type="hidden" name="modalita"  value="<%=StringUtils.toStringJSP(modalita)%>">


  
  <table cellspacing=2 cellpadding=2  width="95%" align="center">
    <tr><td class="titolo" colspan="1">&nbsp;</td></tr>
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
  
  <div id="divComboDetentive" style="width: 95%; display:block;">
    <table style="width: 95%;" align="center">
      <tr>
        <td class="l" width="150px">Natura Misura <font class="ob">(*)</font></td>
        <td class="l">
          <select Title="Tipo Misura Cautelare" name="<%= ICostantiMisuraCautelareCumulo.CAMPO_COD_TIPO_MISURA %>" onChange="Javascript:bloccaQuantum();">
          <%=tipoMisuraDetentive%>
          </select>
        </td>
      </tr>
    </table>
  </div>
  <div id="divComboNonDetentive" style="width: 95%; display:block;">
    <table style="width: 95%;" align="center">
      <tr>
        <td class="l" width="150px">Natura Misura <font class="ob">(*)</font></td>
        <td class="l" colspan="2">
          <select Title="Tipo Misura Cautelare" name="<%= ICostantiMisuraCautelareCumulo.CAMPO_COD_TIPO_MISURA %>" onChange="Javascript:bloccaQuantum();">
          <%=tipoMisuraNonDetentive%>
          </select>
        </td>
      </tr>
    </table>
  </div>
  
  
  <div id="divPeriodi" style="width: 95%; display:block;">
    <table style="width: 95%;" align="center">
      <tr>
        <td class="l" colspan="1" nowrap>Periodo sofferto&nbsp;<font class="ob">(*)</font></td>
        <td class="l" colspan="1" width="170px" nowrap>
          <font class="label">Dal&nbsp;</font>
                                 
          <input type="text" maxlength="2" size="2" id="GG_DAL_1"
                 name="<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_INIZIO%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lMisuraCautelareCumulo.getDataInizio(),"dd"))%>" 
                 <%=IWebConstants.UTIL_DATA%>
                 onChange="Javascript:testCalcolaPresofferto()">
          <input type="text" maxlength="2" size="2" id="MM_DAL_1"
                 name="<%=ICostantiMisuraCautelareCumulo.CAMPO_MESE_DATA_INIZIO%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lMisuraCautelareCumulo.getDataInizio(),"MM"))%>" 
                 <%=IWebConstants.UTIL_DATA%>
                 onChange="Javascript:testCalcolaPresofferto()">
          <input type="text"  maxlength="4" size="4"  id="AA_DAL_1" 
                 name="<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_DATA_INIZIO%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lMisuraCautelareCumulo.getDataInizio(),"yyyy"))%>" 
                 <%=IWebConstants.UTIL_DATA_ANNO%>
                 onChange="Javascript:testCalcolaPresofferto()">
        </td>
        <td class="l" colspan="1" width="160px" nowrap>
          <font  class="label">Al&nbsp;</font>
                                 
          <input type="text" maxlength="2" size="2" id="GG_AL_1"
                 name="<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_FINE%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lMisuraCautelareCumulo.getDataFine(),"dd"))%>" 
                 <%=IWebConstants.UTIL_DATA%>
                 onChange="Javascript:testCalcolaPresofferto()">
          <input type="text" maxlength="2" size="2" id="MM_AL_1" 
                 name="<%=ICostantiMisuraCautelareCumulo.CAMPO_MESE_DATA_FINE%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lMisuraCautelareCumulo.getDataFine(),"MM"))%>" 
                 <%=IWebConstants.UTIL_DATA%>
                 onChange="Javascript:testCalcolaPresofferto()">
          <input type="text" maxlength="4" size="4" id="AA_AL_1"
                 name="<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_DATA_FINE%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lMisuraCautelareCumulo.getDataFine(),"yyyy"))%>" 
                 <%=IWebConstants.UTIL_DATA_ANNO%>
                 onChange="Javascript:testCalcolaPresofferto()">
        </td>
       
        <!--div id="divquantum" style="width: 95%; display:block;" -->
          <td class="l" nowrap id="tdQuantum" width="75px" nowrap>&nbsp;
            <font class="label">Pari a&nbsp;</font><a href="Javascript:testCalcolaPresofferto('X');"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>freccia_verde.gif" alt="Calcola Quantum" border=0></a>:
          </td>   
          <td class="c">
            <table>
              <tr>
                <td id="tdGiorni" nowrap>
                  <font class="label">Tot Giorni</font>&nbsp;
                  <input type="text" name="<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNI%>" maxlength="4" size="4" id="totaleGiorniPre" 
                         value="<%=StringUtils.toStringJSP(lMisuraCautelareCumulo.getGiorni()) %>"
                         onkeypress="return TicTabNumField(this,event)"
                         onChange="Javascript:testChgQuantum(this)">&nbsp;
                  <font class="label">equiparati a:</font>&nbsp;     
                </td>
                                
                <td nowrap>
                  <font class="label">Anni</font>&nbsp;
                  <input type="text" name="<%=ICostantiMisuraCautelareCumulo.CAMPO_NUM_ANNI%>" maxlength="2" size="2" id="anniPresofferto"  
                         value="<%=StringUtils.toStringJSP(lMisuraCautelareCumulo.getNumAnni()) %>"
                         onkeypress="return TicTabNumField(this,event)"
                         onChange="Javascript:testChgQuantum(this)">&nbsp;&nbsp;
                  <font class="label">Mesi</font>&nbsp;
                  <input type="text" name="<%=ICostantiMisuraCautelareCumulo.CAMPO_NUM_MESI%>" maxlength="2" size="2" id="mesiPresofferto" 
                         value="<%=StringUtils.toStringJSP(lMisuraCautelareCumulo.getNumMesi()) %>"
                         onkeypress="return TicTabNumField(this,event)"
                         onChange="Javascript:testChgQuantum(this)">&nbsp;&nbsp;
                  <font class="label">Giorni</font>&nbsp;
                  <input type="text" name="<%=ICostantiMisuraCautelareCumulo.CAMPO_NUM_GIORNI%>" maxlength="2" size="2" id="giorniPresofferto" 
                         value="<%=StringUtils.toStringJSP(lMisuraCautelareCumulo.getNumGiorni()) %>"
                         onkeypress="return TicTabNumField(this,event)"
                         onChange="Javascript:testChgQuantum(this)">&nbsp;&nbsp;&nbsp;&nbsp;
                </td>
              </tr>
            </table>
          </td>
        <!--/div-->  
      </tr>
    </table>
  </div>
  
  <br>
  <div id="divDatiDetentive" style="width: 95%; display:block;">
    <table style="width: 95%;" align="center">
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
                 name="<%=ICostantiMisuraCautelareCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>"
                 value="<%=StringUtils.toStringJSP (lMisuraCautelareCumulo.getIstDetIdIstitutoDetenzione())%>" 
                 >
          <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelareCumulo','<%=ICostantiMisuraCautelareCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','DescrizioneIstituto');">
          <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0></a>
          <a href="Javascript:pulisciIstituto('DescrizioneIstituto','<%=ICostantiMisuraCautelareCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>');"><img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border=0></a>
        </td>
      </tr>
    </table>
  </div>  
  

  <div id="divDatiNonDetentive" style="width: 95%; display:block;">
    <table style="width: 95%;" align="center">
      <tr>
        <td class="l" >Luogo di espiazione </td>
        <td class="l" colspan="3">
          <textarea rows="3" cols="55" style="color: blue"  
                    name="<%=ICostantiMisuraCautelareCumulo.CAMPO_ALTRO_LUOGO_DETENZIONE %>"
          ><%=StringUtils.toStringJSP (lMisuraCautelareCumulo.getAltroLuogoDetenzione())%></textarea>
        </td>
      </tr>
      
      <tr>
        <td class="l">Autorita' competente per territorio</td>
        <td class="l" colspan="3">
          <select Title="autoritaCompetentePerTerritorio" name="<%= ICostantiMisuraCautelareCumulo.CAMPO_AUTORITA_COMPETENTE%>" >
            <%=comboAutCompTerritorio%>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede</td>
        <td class="l">
          <input type="text" size="40"
                 name="<%=ICostantiMisuraCautelareCumulo.CAMPO_DESCR_AUTORITA_COMPETENTE_SEDE%>" 
                 value="<%=StringUtils.toStringJSP (lMisuraCautelareCumulo.getDescrAutoritaCompetenteSede())%>">
          <a href="Javascript:ListaComuni('LoadInserisciMisuraCautelareCumulo'
                                         ,'<%=ICostantiMisuraCautelareCumulo.CAMPO_DESCR_AUTORITA_COMPETENTE_SEDE%>');">
              <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0></a>  
        </td>
        <td class="l">Indirizzo: &nbsp;</td>
        <td class="l">
          <textarea rows="3" cols="50" style="color: blue" 
                    name="<%= ICostantiMisuraCautelareCumulo.CAMPO_AUTORITA_COMPETENTE_INDIRIZZO %>"
          ><%=StringUtils.toStringJSP (lMisuraCautelareCumulo.getAutoritaCompetenteIndirizzo())%></textarea>
        </td>
      </tr>
    </table>
  </div> 

<%
//==============================================================================
// 
//==============================================================================
%>

  <table style="width: 95%;" align="center">
    <tr><td>&nbsp;</td></tr>
    <%
    //==========================================================================
    // Descrizione dello stato visualizzata solo in fase di modifica del dato
    //==========================================================================
    if (lMisuraCautelareCumulo!=null && lMisuraCautelareCumulo.getIdMisuraCautelareCumulo()!=null)
    {
      String lDescStato = "";
        if      ( lMisuraCautelareCumulo.getFlagStato().equals("E")){lDescStato = "Dato Estratto dal fascicolo originale";}
        else if ( lMisuraCautelareCumulo.getFlagStato().equals("I")){lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
        else if ( lMisuraCautelareCumulo.getFlagStato().equals("M")){lDescStato = "Dato estratto modificato";}
        else if ( lMisuraCautelareCumulo.getFlagStato().equals("C")){lDescStato = "Dato estratto cancellato";}
    %>
    <tr>
      <td class="l">&nbsp;</td>
      <td class="l"> <%=lDescStato %></td> 
    </tr>
    <% } %>
  
    <%
    //========================================================================== 
    // Campo note visualizzato sia in inserimento sia in modifica dove l'utente
    // può motivare l'intervento sui dati su cui sta intervenendo
    //========================================================================== 
    %>
    <tr>
      <td class="l">Motivo Inserimento/Modifica</td>
      <td class="l">
        <textarea cols="100" rows="6" 
                  name="<%=ICostantiMisuraCautelareCumulo.CAMPO_MOTIVO_MODIFICA%>"
        ><%=StringUtils.toStringJSP(lMisuraCautelareCumulo.getMotivoModifica()) %></textarea>
      </td>
    </tr>
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
  var frmvalidator  = new Validator("LoadInserisciMisuraCautelareCumulo");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>