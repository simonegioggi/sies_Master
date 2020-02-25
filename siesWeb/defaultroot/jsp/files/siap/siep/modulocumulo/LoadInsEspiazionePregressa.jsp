<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.MisuraCautelareCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraCautelareCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"		 scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"		 scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="aProvvedimento" 		scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aIdComputo" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoProvvedimento"	 scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita" 				 scope="request" class="java.lang.String"/>


<%// SE IN MODIFICA %>
<jsp:useBean id="IstitutoDetenzione"     scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>


<% 
//============================================================================== 
// Form per l'inserimento e la modifica delle Espiazioni pregresse in cumulo
//============================================================================== 
ComputiCumuloModel aComputo = new ComputiCumuloModel();
Vector <ComputiCumuloModel> lListaComputi = aProvvedimento.getListaComputi();
if ( modalita.equals("M") )
{
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
MisuraCautelareCumuloModel lMisuraCautelareCumulo = new MisuraCautelareCumuloModel(); 

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
    
    //==========================================================================
    // Ritorna alla lista dell'espiato
    //==========================================================================   
    function tornaIndietro(action)
    {
      document.indietroForm.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.indietroForm.submit();
    }

    
    //==========================================================================
    // 
    //==========================================================================
    function clearQuantum(){
      $('#anniPresofferto').val('');
      $('#mesiPresofferto').val('');
      $('#giorniPresofferto').val('');

      anniQuantumCalcolati = 0;
      mesiQuantumCalcolati = 0;
      giorniQuantumCalcolati = 0;
    }
    
    //==========================================================================
    // 
    //==========================================================================
    function testCalcolaPresofferto(par){
      var reply = 'noreply';
      if (par=='X')
        reply = 'reply';
        
      if (controllaPeriodi(reply,false)==false){
        clearQuantum();
      }
      else {
        callCalcolaQuantum();
      }
    }
    
    //==========================================================================    
    // Verifica la congruenza dei periodi di Presofferto (data dal <= data al)
    //==========================================================================    
    function controllaPeriodi(reply,isDataFineObbligatoria)
    {
      var sysDate = "<%=DateUtils.getSysDate("dd/MM/yyyy")%>";
    
      var gg_dal = document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA%>;
      var mm_dal = document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA%>;
      var aa_dal = document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA%>;

      var gg_al = document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A%>;
      var mm_al = document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A%>;
      var aa_al = document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A%>;
      
      
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
      else if (isDataFineObbligatoria) {
        if (reply=='noreply') {
          return false
        }
        else {
          alert('La Data di Fine Periodo è obbligatoria');
          gg_al.focus();
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
      
      var gg_dal = document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA%>;
      var mm_dal = document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA%>;
      var aa_dal = document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA%>;

      var gg_al = document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A%>;
      var mm_al = document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A%>;
      var aa_al = document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A%>;
      
      var dataDAL = gg_dal.value +"/"+mm_dal.value+"/"+aa_dal.value;
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;
      
      var tMis = "";
      
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
      
      if (testOnLoad!='S') {
        $("#anniPresofferto").val(anni);
        $("#mesiPresofferto").val(mesi);
        $("#giorniPresofferto").val(giorni);
      }
      else {
        testOnLoad = 'N';
      }
      
      testChgQuantum($("#anniPresofferto"));
      testChgQuantum($("#mesiPresofferto"));
      testChgQuantum($("#giorniPresofferto"));

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

      

      if (currVal!=computedVal)
        $(obj).css("color", "red");
      else 
        $(obj).css("color", "blue");

    }
  
    
    // 
    function bloccaQuantum(par)
    {
      var tipoEspiazione = 'TipoEspiazioneIstituto';
  
      document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.disabled=true;
      document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.disabled=true;
      document.LoadInsEspiazionePregressa.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.disabled=true;
      
     
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
      
      var tipoProv = document.LoadInsEspiazionePregressa.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO %>.value;
      
      if (tipoProv=='-'){
        alert ("Indicare il motivo");
        document.LoadInsEspiazionePregressa.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO %>.focus();
        return false;
      }         
      
      // Data emissione Provvedimento?
      

      // Periodi dal al obbligatori. Data al>dal. Date < data di sistema
      if (!controllaPeriodi('reply',true))
        return false;
        

      return true; 
    } 
    
    // On load
    $(document).ready(function(){
      //
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
        <%
        if( modalita.equals("I") ) { %>
        <font class="campo">Inserimento Espiazione Pregressa &nbsp;</font>
        <%
        }
        else if( modalita.equals("M") ) { %>
        <font class="campo">Modifica Espiazione Pregressa &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActRicercaEspiato')">
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



<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="indietroForm">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
</form>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInsEspiazionePregressa">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciEspiazionePregressa">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
 
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getCodTipoProvvedimento()) %>">
  
  <input type="hidden" name="<%= ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>"                       value="<%=StringUtils.toStringJSP(aComputo.getIdComputiCumulo()) %>">
  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>" 					  value="<%=StringUtils.toStringJSP(aProvvedimento.getFlagStato()) %>">

  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_EVENTO_ORIGINE %>"      value="<%=StringUtils.toStringJSP(aProvvedimento.getIdEventoOrigine(),"") %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_EVE_ID_EVENTO_ORIGINE %>"  value="<%=StringUtils.toStringJSP(aProvvedimento.getEveIdEventoOrigine(),"") %>">
                 
  <input type="hidden" name="modalita"  value="<%=StringUtils.toStringJSP(modalita)%>">

  <% if ("M".equals(modalita)) { %>
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE %>"  value="<%=StringUtils.toStringJSP(aProvvedimento.getCodUfficioEmittente(),"") %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>"    value="<%=StringUtils.toStringJSP(aProvvedimento.getCodLuogoEmittente(),"") %>">
  <% } %>
 
  <div id="divPeriodi" style="display:block;">
    <table cellspacing="2" cellpadding="2" width="95%" align="center">
      <tr><td class="titolo" colspan="4">Provvedimento Sospensione/Interruzione</td></tr>
      <tr>
        <td class="l">A seguito di: </td>
        <td class="l" colspan="1">
          <select Title="Tipo Provvedimento" class="small"   name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO %>" >
			<%=motivoProvvedimento%>
          </select>
        </td>
        
        <td class="l">Data Emissione</td>
        <td class="l">
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
    
    <br>
    
    <table width="95%" align="center">
      <tr><td class="titolo" colspan="5">Periodo espiato</td></tr>
      <tr>
        <td class="l" colspan="1" nowrap>Periodo Espiato&nbsp;<font class="ob">(*)</font></td>
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
                <td nowrap>
                  <font class="label">Anni</font>&nbsp;
                  <input type="text" name="<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>" maxlength="2" size="2" id="anniPresofferto"  
                         value="<%=StringUtils.toStringJSP(aComputo.getNumAnniReclusione()) %>"
                         onkeypress="return TicTabNumField(this,event)"
                         onChange="Javascript:testChgQuantum(this)">&nbsp;&nbsp;
                  <font class="label">Mesi</font>&nbsp;
                  <input type="text" name="<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>" maxlength="2" size="2" id="mesiPresofferto" 
                         value="<%=StringUtils.toStringJSP(aComputo.getNumMesiReclusione()) %>"
                         onkeypress="return TicTabNumField(this,event)"
                         onChange="Javascript:testChgQuantum(this)">&nbsp;&nbsp;
                  <font class="label">Giorni</font>&nbsp;
                  <input type="text" name="<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>" maxlength="2" size="2" id="giorniPresofferto" 
                         value="<%=StringUtils.toStringJSP(aComputo.getNumGiorniReclusione()) %>"
                         onkeypress="return TicTabNumField(this,event)"
                         onChange="Javascript:testChgQuantum(this)">&nbsp;&nbsp;&nbsp;&nbsp;
                </td>
              </tr>
            </table>
          </td>
      </tr>
    </table>
  </div>
  
  <br>
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
                 value="<%=StringUtils.toStringJSP (aComputo.getIstDetIdIstitutoDetenzione())%>" 
                 >
          <a href="Javascript:ListaIstitutoDetenzione('LoadInsEspiazionePregressa','<%=ICostantiComputiCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','DescrizioneIstituto');">
          <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0></a>
          <a href="Javascript:pulisciIstituto('DescrizioneIstituto','<%=ICostantiComputiCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>');"><img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border=0></a>
        </td>
      </tr>
      <tr>
        <td class="l" >Altro Luogo di espiazione </td>
        <td class="l" colspan="1">
          <input type="text" size="90"
                 name="<%=ICostantiComputiCumulo.CAMPO_ALTRO_LUOGO_DETENZIONE %>"
                 value="<%=StringUtils.toStringJSP (aComputo.getAltroLuogoDetenzione())%>" >
        </td>
      </tr>    
      <tr>
        <td class="l" >Annotazione</td>
        <td class="l" colspan="1">
          <textarea rows="3" cols="90" name="<%=ICostantiComputiCumulo.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(aComputo.getNote(),"")%></textarea>     
        </td>  
      </tr>        
    </table>

<%
//==============================================================================
// 
//==============================================================================
%>
<!--
  <table width="95%" align="center">
    <tr><td>&nbsp;</td></tr>
    <%
    //==========================================================================
    // Descrizione dello stato visualizzata solo in fase di modifica del dato
    //==========================================================================
    if (lMisuraCautelareCumulo!=null && lMisuraCautelareCumulo.getIdMisuraCautelareCumulo()!=null)
    {
      String lDescStato = "AAAAA";
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

-->
  <table width="95%" align="center">
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
  var frmvalidator  = new Validator("LoadInsEspiazionePregressa");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>