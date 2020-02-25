<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="java.util.Date" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="siap.sico.util.CalendarUtil" %>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale" %>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" %>
<%@ page import="siap.sico.calendar.model.CalendarModel" %>

<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel" %>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.fungibilita.model.FungibilitaModel" %>



<jsp:useBean id="aPenaIniziale"      scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="aListaQuantum"      scope="request" class="java.util.Vector" />
<jsp:useBean id="aListaLA"           scope="request" class="java.util.Vector" />
<jsp:useBean id="aListaPresofferti"  scope="request" class="java.util.Vector" />

<jsp:useBean id="aDaCalcolo"         scope="request" class="java.lang.String" />

<jsp:useBean id="aPenaRideterminata"  scope="request" class="siap.siep.calcolopena.model.CalcoloPenaModel" />

<jsp:useBean id="aPenaEspiata"          scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="aPenaResiduaDaInterr"  scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />


<html>

<%
Date aDataDecorrenza = null;
Date aDataScarcerazione = null;
Date aDataInterruzione = null;

if (aPenaRideterminata.getPenaResiduaRicalcolata()!=null)
{
  aDataDecorrenza = aPenaRideterminata.getPenaResiduaRicalcolata().getDataInizio();
}

aDataScarcerazione = (Date)request.getAttribute("aDataScarcerazione");
aDataInterruzione  = (Date)request.getAttribute("aDataInterruzione");

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                                    ATTENZIONE
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// Problemi per la log_attività
// ogni record Quantum pesa 230 caratteri
// ogni record Presofferto pesa 106 caratteri
// A questi vanno aggiunto altri 190 caratteri
// Il campo RECORD della tabella è di 4000 caratteri
// Possibili combinazioni (rec_quantum, rec_presoff)=(14,5)(13,7)
// n.b. non contano quelli visualizzati, ma i max anche se nascosti vengono 
//      passati sulla request
// Verificare come non loggare questa action
// Eliminare dalla main.jsp il controllo
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
int min_rec_quantum = 4;
int max_rec_quantum = 20;

int min_rec_presofferto = 2;
int max_rec_presofferto = 20;

int last_rec_quantum = min_rec_quantum;
int last_record_presofferto = min_rec_presofferto;

for (int i=0; i<aListaQuantum.size(); i++){
  AnnotazioneManualeModel lAnnoModel = (AnnotazioneManualeModel) aListaQuantum.elementAt(i);
  if (   lAnnoModel.getIdAnnotazioneManuale()!=null
      && lAnnoModel.getIdAnnotazioneManuale().intValue()>last_rec_quantum)
  {
    last_rec_quantum = lAnnoModel.getIdAnnotazioneManuale().intValue();
  }
}

for (int i=0; i<aListaLA.size(); i++){
  LicenzaLibAnticipataModel lLibAntModel = (LicenzaLibAnticipataModel) aListaLA.elementAt(i);
  if (  lLibAntModel.getIdLicenzaLibanticipata()!=null
      && lLibAntModel.getIdLicenzaLibanticipata().intValue()>last_rec_quantum)
  {
    last_rec_quantum = lLibAntModel.getIdLicenzaLibanticipata().intValue();
  }
}

int conta_presofferti = 0;
for (int i=0; i<aListaPresofferti.size(); i++){
  AnnotazioneManualeModel lAnnoModel = (AnnotazioneManualeModel) aListaPresofferti.elementAt(i);
  if (lAnnoModel.getIdAnnotazioneManuale()!=null){
    conta_presofferti = lAnnoModel.getIdAnnotazioneManuale().intValue();
  }
}
if (last_record_presofferto<conta_presofferti)
{
  last_record_presofferto = conta_presofferti;
}
%>

<head>
  <title> [S.I.E.S.] - Calcolo Rapido della Pena </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>jsrsClient.js"></script>
  
  <script language="JavaScript">
    //==========================================================================
    // Verifica che i dati digitati in maschera siano coerenti prima di 
    // sottomettere la richiesta
    //==========================================================================
    function Verify_Dati()
    {
      // Verifica che almeno il primo rigo dei quantum sia stato valorizzato
      
      // Per ogni rigo dei quantum verifica che se sono presenti dati, sia 
      // stato specificato il segno
      // alert ("last_record_quantum ="+last_record_quantum)
      for (var i=2; i<=last_record_quantum; i++){
        //alert ("Check quantum record = "+i);
        if (controlla_quantum(i)==false){
          return false;
        }
      }
      
      // Per ogni rigo dei presofferti verifica la correttezza e coerenza delle date
      for (var i=1; i<=last_record_presofferto; i++){
        //alert ("Check presofferto record = "+i);
        if (     document.getElementById("GG_DAL_"+i).value!=""
              || document.getElementById("MM_DAL_"+i).value!=""	
              || document.getElementById("AA_DAL_"+i).value!=""	
              || document.getElementById("GG_AL_"+i).value!=""	
              || document.getElementById("MM_AL_"+i).value!=""	
              || document.getElementById("AA_AL_"+i).value!=""	
            )
        {
          if (controllaPeriodi("MC_"+i,'')==false){
            return false;
          }
        }
      }
      
      
      // Verifica la correttezza della data di decorrenza
      if (     document.getElementById("GG_decorrenza").value!=""
            || document.getElementById("MM_decorrenza").value!=""	
            || document.getElementById("AA_decorrenza").value!=""	
          )
      {
        if (document.getElementById("GG_decorrenza").value.length<2 && document.getElementById("GG_decorrenza").value.length!=0)
          document.getElementById("GG_decorrenza").value="0"+document.getElementById("GG_decorrenza").value;
        if (document.getElementById("MM_decorrenza").length<2 && document.getElementById("MM_decorrenza").length!=0)
          document.getElementById("MM_decorrenza").value="0"+document.getElementById("MM_decorrenza").value;
          
        var dataDecorrenza=document.getElementById("GG_decorrenza").value +"/"+document.getElementById("MM_decorrenza").value+"/"+document.getElementById("AA_decorrenza").value;
        
        if (! ControllaData(dataDecorrenza))
        {
          alert('Data di Decorrenza non valida');
          document.getElementById("GG_decorrenza").focus();
  
          return false;
        }
          
      }      

      // Verifica la correttezza della data di Scarcerazione
      if (     document.getElementById("GG_scarcerazione").value!=""
            || document.getElementById("MM_scarcerazione").value!=""	
            || document.getElementById("AA_scarcerazione").value!=""	
          )
      {
        if (document.getElementById("GG_scarcerazione").value.length<2 && document.getElementById("GG_scarcerazione").value.length!=0)
          document.getElementById("GG_scarcerazione").value="0"+document.getElementById("GG_scarcerazione").value;
        if (document.getElementById("MM_scarcerazione").length<2 && document.getElementById("MM_scarcerazione").length!=0)
          document.getElementById("MM_scarcerazione").value="0"+document.getElementById("MM_scarcerazione").value;
          
        var dataScarcerazione=document.getElementById("GG_scarcerazione").value +"/"+document.getElementById("MM_scarcerazione").value+"/"+document.getElementById("AA_scarcerazione").value;
        
        if (! ControllaData(dataScarcerazione))
        {
          alert('Data di Scarcerazione non valida');
          document.getElementById("GG_scarcerazione").focus();
  
          return false;
        }
          
      }      

      // Verifica la correttezza della data di Interruzione
      if (     document.getElementById("GG_interruzione").value!=""
            || document.getElementById("MM_interruzione").value!=""	
            || document.getElementById("AA_interruzione").value!=""	
          )
      {
        if (document.getElementById("GG_interruzione").value.length<2 && document.getElementById("GG_interruzione").value.length!=0)
          document.getElementById("GG_interruzione").value="0"+document.getElementById("GG_interruzione").value;
        if (document.getElementById("MM_interruzione").length<2 && document.getElementById("MM_interruzione").length!=0)
          document.getElementById("MM_interruzione").value="0"+document.getElementById("MM_interruzione").value;
          
        var dataInterruzione=document.getElementById("GG_interruzione").value +"/"+document.getElementById("MM_interruzione").value+"/"+document.getElementById("AA_interruzione").value;
        
        if (! ControllaData(dataInterruzione))
        {
          alert('Data di Interruzione non valida');
          document.getElementById("GG_interruzione").focus();
  
          return false;
        }
          
      }      

      document.f.subm2.disabled=true;
      document.f.submit();
    }


    //==========================================================================    
    // Verifica che sia presente il segno per ogni record quantum che contiene
    // almeno un dato
    //==========================================================================    
    function controlla_quantum (id_rec){
      //alert("controlla_quantum rec="+id_rec);
      //alert(document.getElementById("PM_"+id_rec).selectedIndex);
      
      // Verifico il contenuto dei campi
      if (   document.getElementById("PM_"+id_rec).selectedIndex==0	
          && ( // Reclusione
                 document.getElementById("AA_Rec_"+id_rec).value!=""
              || document.getElementById("MM_Rec_"+id_rec).value!=""	
              || document.getElementById("GG_Rec_"+id_rec).value!=""	
              || document.getElementById("Multa_int_"+id_rec).value!=""	
              || document.getElementById("Multa_dec_"+id_rec).value!=""	

              // Arresto
              || document.getElementById("AA_Arr_"+id_rec).value!=""	
              || document.getElementById("MM_Arr_"+id_rec).value!=""	
              || document.getElementById("GG_Arr_"+id_rec).value!=""	
              || document.getElementById("Ammenda_int_"+id_rec).value!=""	
              || document.getElementById("Ammenda_dec_"+id_rec).value!=""	

              // LA
              || document.getElementById("GG_LA_"+id_rec).value!=""	
             )
        )
      {
        alert ("Attenzione! Indicare se Quantum e/o LA sono Concessi o Revocati");
        document.getElementById("PM_"+id_rec).focus();
        return false;
      }
      return true;
    }




    //==========================================================================    
    // Verifica la congruenza dei periodi di Presofferto (data dal <= data al)
    //==========================================================================    
    function controllaPeriodi(idMC,reply) //OK
    {
      i=idMC.length-1;
      while (idMC.charAt(i)!="_"){
        i--;
      }
      var id = idMC.substr(i+1,idMC.length-i-1);
    
      
      var gg_dal = document.getElementsByName("GG_DAL_"+id)[0];
      var mm_dal = document.getElementsByName("MM_DAL_"+id)[0];
      var aa_dal = document.getElementsByName("AA_DAL_"+id)[0];

      var gg_al = document.getElementsByName("GG_AL_"+id)[0];
      var mm_al = document.getElementsByName("MM_AL_"+id)[0];
      var aa_al = document.getElementsByName("AA_AL_"+id)[0];
      
      
      if (gg_dal.value.length<2 && gg_dal.value.length!=0)
        gg_dal.value="0"+gg_dal.value;
      if (mm_dal.value.length<2 && mm_dal.value.length!=0)
        mm_dal.value="0"+mm_dal.value;


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


      if (gg_al.value.length<2 && gg_al.value.length!=0)
        gg_al.value="0"+gg_al.value;
      if (mm_al.value.length<2 && mm_al.value.length!=0)
        mm_al.value="0"+mm_al.value;

     
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;
      
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
    
    
    //==========================================================================
    // Recupera data inizio e data fine e chiama la servlet per il calcolo dei 
    // quantum
    //==========================================================================
    var idMisura;
    function callCalcolaQuantum (idMC) { //OK
      //alert("callCalcolaQuantum: "+idMC);
      
      if (controllaPeriodi(idMC)==false)
        return;
      
      idMisura= idMC;
      
      i=idMC.length-1;
      while (idMC.charAt(i)!="_"){
        i--;
      }
      var id = idMC.substr(i+1,idMC.length-i-1);

      
      var gg_dal = document.getElementsByName("GG_DAL_"+id)[0];
      var mm_dal = document.getElementsByName("MM_DAL_"+id)[0];
      var aa_dal = document.getElementsByName("AA_DAL_"+id)[0];

      var gg_al = document.getElementsByName("GG_AL_"+id)[0];
      var mm_al = document.getElementsByName("MM_AL_"+id)[0];
      var aa_al = document.getElementsByName("AA_AL_"+id)[0];
      
      
      var dataDAL = gg_dal.value +"/"+mm_dal.value+"/"+aa_dal.value;
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;

      
      // verifica che data inizio e fine appartengano all'intervallo selezionato
      var myParams = new Array(gg_dal.value,
                               mm_dal.value,
                               aa_dal.value,
                               gg_al.value,
                               mm_al.value,
                               aa_al.value
                              );

      document.body.style.cursor='wait';
      // Chiamata:
      // jsrsExecute(<nome servlet>,<funzione js da invocare al ritorno>, <nome del metodo server da invocare>, <parametro da passare al server o array di parametri>
      jsrsExecute("/CaricaHTML_Servlet", caricaQuantum, "getQuantumIntervallo",myParams);      

    }

    //==========================================================================
    // Funzione invocata di ritorno. Riceve in input una stringa con i quantum
    // e visualizza il dato nell'opportuno campo
    //==========================================================================
    function caricaQuantum(valueTextStr){ //OK
      document.body.style.cursor='auto';
      //alert("return "+valueTextStr);
      
      var sep = "~#";
      var aPairs = valueTextStr.split(sep);

      strQuantum = 'Anni <font class="campo">'+aPairs[0]+'</font> '+
                   'Mesi <font class="campo">'+aPairs[1]+'</font> '+
                   'Giorni <font class="campo">'+aPairs[2]+'</font>';
      
      //var id = idMisura.substr(idMisura.length-1);
      i=idMisura.length-1;
      while (idMisura.charAt(i)!="_"){
        i--;
      }
      var id = idMisura.substr(i+1,idMisura.length-i-1);

      document.getElementById('Quantum_MC_'+id).innerHTML = strQuantum;
    }
    
    //==========================================================================
    //
    //==========================================================================
    function clearQuantum(idMC){ //OK
      i=idMC.length-1;
      while (idMC.charAt(i)!="_"){
        i--;
      }
      var id = idMC.substr(i+1,idMC.length-i-1);

      document.getElementById('Quantum_MC_'+id).innerHTML = '&nbsp;';
    }
    
    
    //==========================================================================
    //
    //==========================================================================
    function testCalcolaPresofferto(idMC){ //OK
      if (controllaPeriodi(idMC,'noreply')==false){
        clearQuantum(idMC);
      }
      else {
        callCalcolaQuantum(idMC);
      }
    }

    //==========================================================================
    //
    //==========================================================================
    function pulisciMaschera(){  //OK
      document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadCalcolatrice";
      document.f.submit();
    }
    
    //==========================================================================
    // Visualizza una nuova riga della tabella dei quantum
    //==========================================================================
    var last_record_quantum = <%=last_rec_quantum%>; //ultimo record visibile
    var min_record_quantum  = <%=min_rec_quantum%>;  //min record visibili
    var max_record_quantum  = <%=max_rec_quantum%>;  //max record visibili
    function aggiungi_record_quantum() //OK
    {
      if (last_record_quantum==max_record_quantum)
      {
        alert("Non è possibile aggiungere ulteriori record");
        return;
      }
      
      last_record_quantum = last_record_quantum + 1;
      
      var riga = document.getElementById("rec_quantum_"+last_record_quantum);	
      riga.style.display = "block";
    }

    //==========================================================================
    // Nasconde e pulisce l'ultima riga della tabella dei quantum
    //==========================================================================
    function elimina_record_quantum() //OK
    {
      if (last_record_quantum==min_record_quantum)
      {
        alert("Non è possibile eliminare tutti i  record");
        return;
      }
      
      pulisci_rigo_quantum(last_record_quantum);

      var riga = document.getElementById("rec_quantum_"+last_record_quantum);	
      riga.style.display = "none";

      last_record_quantum = last_record_quantum - 1;
    }

    //==========================================================================
    // Ripulisce il contenuto di un rigo della tabella quantum
    //==========================================================================
    function pulisci_rigo_quantum(id_rec) //OK
    {
      // Ripulisco il comtenuto dei campi
      document.getElementById("PM_"+id_rec).selectedIndex=0;	

      // Reclusione
      document.getElementById("AA_Rec_"+id_rec).value="";	
      document.getElementById("MM_Rec_"+id_rec).value="";	
      document.getElementById("GG_Rec_"+id_rec).value="";	
      document.getElementById("Multa_int_"+id_rec).value="";	
      document.getElementById("Multa_dec_"+id_rec).value="";	

      // Arresto
      document.getElementById("AA_Arr_"+id_rec).value="";	
      document.getElementById("MM_Arr_"+id_rec).value="";	
      document.getElementById("GG_Arr_"+id_rec).value="";	
      document.getElementById("Ammenda_int_"+id_rec).value="";	
      document.getElementById("Ammenda_dec_"+id_rec).value="";	

      // LA
      document.getElementById("GG_LA_"+id_rec).value="";	
    
    }

    //==========================================================================    
    // Visualizza una nuova riga della tabella dei Presofferti
    //==========================================================================    
    var last_record_presofferto = <%=last_record_presofferto%>; //ultimo record visibile
    var min_record_presofferto  = <%=min_rec_presofferto%>;     //min record visibili
    var max_record_presofferto  = <%=max_rec_presofferto%>;     //max record visibili
    function aggiungi_record_presofferto() //OK
    {
      if (last_record_presofferto==max_record_presofferto)
      {
        alert("Non è possibile aggiungere ulteriori record");
        return;
      }
      
      last_record_presofferto = last_record_presofferto + 1;
      
      var riga = document.getElementById("rec_presofferto_"+last_record_presofferto);	
      riga.style.display = "block";
    }

    //==========================================================================    
    // Nasconde e pulisce l'ultima riga della tabella dei Presofferti
    //==========================================================================    
    function elimina_record_presofferto()  //OK
    {
      if (last_record_presofferto==min_record_presofferto)
      {
        alert("Non è possibile eliminare tutti i  record");
        return;
      }
      
      pulisci_rigo_presofferto(last_record_presofferto);
      
      var riga = document.getElementById("rec_presofferto_"+last_record_presofferto);	
      riga.style.display = "none";

      last_record_presofferto = last_record_presofferto - 1;
    }
    
    //==========================================================================    
    // Ripulisce il contenuto di un rigo della tabella presofferti
    //==========================================================================    
    function pulisci_rigo_presofferto(id_rec){  //OK
      // DAL
      document.getElementById("AA_DAL_"+id_rec).value="";	
      document.getElementById("MM_DAL_"+id_rec).value="";	
      document.getElementById("GG_DAL_"+id_rec).value="";	

      // AL 
      document.getElementById("AA_AL_"+id_rec).value="";	
      document.getElementById("MM_AL_"+id_rec).value="";	
      document.getElementById("GG_AL_"+id_rec).value="";	

      // Quantum
      document.getElementById("Quantum_MC_"+id_rec).innerHTML = "&nbsp;";
    }
    
    //==========================================================================
    //
    //==========================================================================
<% if (aDataInterruzione!=null){ %>
    var aDisplayInterr = true;
<% } else { %>      
    var aDisplayInterr = false;
<% } %>      
    
    function load_this()
    {
      if (document.f.DaCalcolo.value="S"){
        
        if (aDisplayInterr){
          document.getElementById("Pena_espiata").focus();
        }else{
          document.getElementById("Data_fine_pena").focus();
        }
      }
    }
    
  </script>
</head>


<body class="corpo" onLoad="Javascript:load_this();">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActCalcolatrice">
    <input type="hidden" name="DaCalcolo" value="<%=aDaCalcolo%>">
    <input type="hidden" name="max_record_quantum"  value="<%=max_rec_quantum%>">
    <input type="hidden" name="max_rec_presofferto" value="<%=max_rec_presofferto%>">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Calcolo Rapido della Pena</font>
        </td>
      </tr>
    </table>
  
    <br><br>

<!-- 
================================================================================
                     TABELLA CON I QUANTUM e LE LA 
================================================================================
-->    
    <table width="100%" style="border; 0;" id="tabQuantum" cellspacing="0" cellpadding="1">
      <tr>
        <td class=titolo>&nbsp;</td>
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class=titolo >
          <a href="Javascript:aggiungi_record_quantum();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" alt="Aggiungi nuovo rigo" border=0></a>
          <a href="Javascript:elimina_record_quantum();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" alt="Elimina ultimo rigo" border=0></a>
        </td--%>
        <td class=titolo colspan=2>Reclusione</td>
        <td width="1">&nbsp;</td>
        <td class="titolo" colspan=2>Arresto</td>
        <td width="1">&nbsp;</td>
        <td class="titolo" colspan="1" title="Liberazione Anticipata">Lib. An.</td>
        <td class="titolo">&nbsp;</td>
      </tr>


<!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
      <% for (int i=1;i<=max_rec_quantum;i++) 
      {
        AnnotazioneManualeModel lAnnoModel = new AnnotazioneManualeModel();
        if (aListaQuantum.size()>=i){
          lAnnoModel = (AnnotazioneManualeModel) aListaQuantum.elementAt(i-1);
        }
        
        LicenzaLibAnticipataModel lLibAntModel = new LicenzaLibAnticipataModel();
        if (aListaLA.size()>=i){
          lLibAntModel = (LicenzaLibAnticipataModel) aListaLA.elementAt(i-1);
        }
        
        String lSegno = null;
        
        if (lAnnoModel.getIdAnnotazioneManuale()!=null)
        {
          lSegno = lAnnoModel.getFlagPiuMeno();
        }
        
        if (lLibAntModel.getIdLicenzaLibanticipata()!=null) {
          if (lLibAntModel.getFlagConcesso().equals("R"))
            lSegno = "+";
          else if (lLibAntModel.getFlagConcesso().equals("C"))
            lSegno = "-";
        }
      %>
      
      <% if (   i<=min_rec_quantum 
             || lAnnoModel.getIdAnnotazioneManuale()!=null
             || lLibAntModel.getIdLicenzaLibanticipata()!=null 
            ) 
      {%>
      <tr style="display:block" id="rec_quantum_<%=i%>">
      <% } else { %>
      <tr style="display:none" id="rec_quantum_<%=i%>">
      <% } %>
        <td class="c">
          <table style="border; 0;" cellspacing="0" cellpadding="0">
            <% if (i==1){ %>
            <tr><td colspan="2">&nbsp;</td></tr>
            <% } %>
            <tr>
              <td>+/- <font class="ob">(*)</font></td>
              <td>
                <% if (i==1){ %>
                <select name="PM_<%=i%>" id="PM_<%=i%>">
                  <option value="+" selected>+</option>
                </select>
                <% } else { %>
                <select name="PM_<%=i%>" id="PM_<%=i%>">
                  <option value=""></option>
                  <% if (lSegno!=null && lSegno.equals("+")) {%>
                  <option value="+" selected>+</option>
                  <% } else {%>
                  <option value="+">+</option>
                  <% } %>
                  <% if (lSegno!=null && lSegno.equals("-")) {%>
                  <option value="-" selected>-</option>
                  <% } else {%>
                  <option value="-">-</option>
                  <% } %>
                </select>
                <% } %>            
              </td>
            </tr>
          </table>
        </td>
  
        <td class="c">
          <table style="border; 0;" cellspacing="0" cellpadding="0" >
            <% if (i==1){ %>
            <tr>
              <td align="center"><font class="label">Anni</font></td>
              <td align="center"><font class="label">Mesi</font></td>
              <td align="center"><font class="label">Giorni</font></td>
            </tr>
            <% } %>
            <tr>
              <td><input type="text" name="AA_Rec_<%=i%>" id="AA_Rec_<%=i%>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lAnnoModel.getNumAnniReclusione(),"")%>" 
                         onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;</td>
              <td><input type="text" name="MM_Rec_<%=i%>" id="MM_Rec_<%=i%>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lAnnoModel.getNumMesiReclusione(),"")%>" 
                         onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;</td>
              <td><input type="text" name="GG_Rec_<%=i%>" id="GG_Rec_<%=i%>" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(lAnnoModel.getNumGiorniReclusione(),"")%>" 
                         onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"></td>
            </tr>
          </table>
        </td>
        <td class="c">
          <table style="border; 0;" cellspacing="0"  cellpadding="0">
            <% if (i==1){ %>
            <tr>
              <td align="center"><font class="label">Multa</font></td>
            </tr>
            <% } %>
            <tr>
              <td align="center">
                <input style="text-align:right" type="text" name="Multa_int_<%=i%>" id="Multa_int_<%=i%>" maxlength="8" size="6" value="<%=StringUtils.getParteIntera(lAnnoModel.getImportoMulta())%>" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
                ,
                <input style="text-align:left" type="text" name="Multa_dec_<%=i%>" id="Multa_dec_<%=i%>" maxlength="2" size="2" value="<%=StringUtils.getParteDecimale(lAnnoModel.getImportoMulta())%>" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
              </td>
            </tr>
          </table>
        </td>
        
        <td width="1">&nbsp;</td>
        
        <td class="c">
          <table cellspacing="0"  cellpadding="0">
            <% if (i==1){ %>
            <tr>
              <td align="center"><font class="label">Anni</font></td>
              <td align="center"><font class="label">Mesi</font></td>
              <td align="center"><font class="label">Giorni</font></td>
            </tr>
            <% } %>
            <tr>
              <td><input type="text" name="AA_Arr_<%=i%>" id="AA_Arr_<%=i%>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lAnnoModel.getNumAnniArresto(),"")%>" 
                         onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;</td>
              <td><input type="text" name="MM_Arr_<%=i%>" id="MM_Arr_<%=i%>" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lAnnoModel.getNumMesiArresto(),"")%>" 
                         onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;</td>
              <td><input type="text" name="GG_Arr_<%=i%>" id="GG_Arr_<%=i%>" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(lAnnoModel.getNumGiorniArresto(),"")%>" 
                         onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"></td>
            </tr>
          </table>
        </td>
    
        <td class="c">
          <table style="border; 0;" cellspacing="0" cellpadding="0">
            <% if (i==1){ %>
            <tr>
              <td align="center"><font class="label">Ammenda</font></td>
            </tr>
            <% } %>
            <tr>
              <td>
                <input style="text-align:right" type="text" name="Ammenda_int_<%=i%>" id="Ammenda_int_<%=i%>" maxlength="8" size="6" value="<%=StringUtils.getParteIntera(lAnnoModel.getImportoAmmenda())%>" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
                ,
                <input style="text-align:left" type="text" name="Ammenda_dec_<%=i%>" id="Ammenda_dec_<%=i%>" maxlength="2" size="2" value="<%=StringUtils.getParteDecimale(lAnnoModel.getImportoAmmenda())%>" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
              </td>
            </tr>
          </table>
        </td>
    
        <td width="1">&nbsp;</td>
    
        <td class="c">
          <table style="border; 0;" cellspacing="0" cellpadding="0">
            <% if (i==1){ %>
            <tr>
              <td align="center" colspan="2"><font class="label">Giorni</font></td>
            </tr>
            <tr>
              <td>
                <input style="background-color: #DCDCDC;" type="text" name="GG_LA_<%=i%>" id="GG_LA_<%=i%>" maxlength="4" size="6" value="" readonly>
              </td>
            </tr>
            <% } else { %>
            <tr>
              <td>
                <% if (lLibAntModel.getIdLicenzaLibanticipata()!=null) { %>
                <input type="text" name="GG_LA_<%=i%>" id="GG_LA_<%=i%>" maxlength="4" size="6" value="<%=StringUtils.toStringJSP(lLibAntModel.getNumeroGiorni(),"")%>" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
                <% } else {%>
                <input type="text" name="GG_LA_<%=i%>" id="GG_LA_<%=i%>" maxlength="4" size="6" value="" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
                <% } %>
              </td>
            </tr>
            <% } %>
          </table>
        </td>

        <td class="c">
          <table style="border; 0;" cellspacing="0" cellpadding="0">
            <% if (i==1){ %>
            <tr>
              <td align="center" colspan="2"><font class="label">&nbsp;</font></td>
            </tr>
            <tr>
              <td>
                <a href="Javascript:pulisci_rigo_quantum(<%=i%>);"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>siapcan.gif" alt="Cancella contenuto della riga" border=0></a>
              </td>
            </tr>
            <% } else { %>
            <tr>
              <td>
                <a href="Javascript:pulisci_rigo_quantum(<%=i%>);"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>siapcan.gif" alt="Cancella contenuto della riga" border=0></a>
              </td>
            </tr>
            <% } %>
          </table>
        </td>
      </tr>
      <% } %>
    </table>
    <table>
      <tr>
        <td>
          <a href="Javascript:aggiungi_record_quantum();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" alt="Aggiungi nuovo rigo" border=0></a>
        </td>
        <td>
          <a href="Javascript:elimina_record_quantum();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" alt="Elimina ultimo rigo" border=0></a>
        </td>
      </tr>
    </table>
    
<!-- 
================================================================================
                            PRESOFFERTI INTERRUZIONI
================================================================================
    <br>
-->
    
    <table width="100%" style="border; 0;" cellspacing="0" cellpadding="1">
      <tr><td class="titolo" colspan="100%" >Presofferti</td></tr>

      <% for (int i=1;i<=max_rec_presofferto;i++) 
      {
        AnnotazioneManualeModel lAnnoModel = new AnnotazioneManualeModel();
        if (aListaPresofferti.size()>=i){
          lAnnoModel = (AnnotazioneManualeModel) aListaPresofferti.elementAt(i-1);
        }
      
      %>    
      <% if (i<=min_rec_presofferto || lAnnoModel.getIdAnnotazioneManuale()!=null) {%>
      <tr style="display:block" id="rec_presofferto_<%=i%>">
      <% } else { %>
      <tr style="display:none" id="rec_presofferto_<%=i%>">
      <% } %>
        <td>
          <table width="50%" style="border; 0;" cellspacing="0" cellpadding="0">
            <tr>
              <td class="l" colspan="1" nowrap>
                <font class="label">Dal &nbsp;&nbsp;</font>
                <% if (lAnnoModel.getIdAnnotazioneManuale()!=null) {%>
                <input type="text" name="GG_DAL_<%=i%>" id="GG_DAL_<%=i%>" maxlength="2" size="2" 
                       value="<%=DateUtils.getDayToString(lAnnoModel.getDataReclusioneDa())%>" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="Javascript:testCalcolaPresofferto('MC_<%=i%>')">
                /
                <input type="text" name="MM_DAL_<%=i%>" id="MM_DAL_<%=i%>" maxlength="2" size="2" 
                       value="<%=DateUtils.getMonthToString(lAnnoModel.getDataReclusioneDa())%>" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="Javascript:testCalcolaPresofferto('MC_<%=i%>')">
                /
                <input type="text" name="AA_DAL_<%=i%>" id="AA_DAL_<%=i%>" maxlength="4" size="4" 
                       value="<%=DateUtils.getYearToString(lAnnoModel.getDataReclusioneDa())%>" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="Javascript:testCalcolaPresofferto('MC_<%=i%>')">
                <% } else { %>                       
                <input type="text" name="GG_DAL_<%=i%>" id="GG_DAL_<%=i%>" maxlength="2" size="2" value="" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="Javascript:testCalcolaPresofferto('MC_<%=i%>')">
                /
                <input type="text" name="MM_DAL_<%=i%>" id="MM_DAL_<%=i%>" maxlength="2" size="2" value="" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="Javascript:testCalcolaPresofferto('MC_<%=i%>')">
                /
                <input type="text" name="AA_DAL_<%=i%>" id="AA_DAL_<%=i%>" maxlength="4" size="4" value="" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="Javascript:testCalcolaPresofferto('MC_<%=i%>')">
                <% } %>                       
              </td>
              <td class="l" colspan="1" nowrap>
                <font  class="label">Al &nbsp;&nbsp;</font>
                <% if (lAnnoModel.getIdAnnotazioneManuale()!=null) {%>
                <input type="text" name="GG_AL_<%=i%>" id="GG_AL_<%=i%>" maxlength="2" size="2" 
                       value="<%=DateUtils.getDayToString(lAnnoModel.getDataReclusioneA())%>" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="Javascript:testCalcolaPresofferto('MC_<%=i%>')">
                /
                <input type="text" name="MM_AL_<%=i%>" id="MM_AL_<%=i%>" maxlength="2" size="2"
                       value="<%=DateUtils.getMonthToString(lAnnoModel.getDataReclusioneA())%>" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="Javascript:testCalcolaPresofferto('MC_<%=i%>')">
                /
                <input type="text" name="AA_AL_<%=i%>" id="AA_AL_<%=i%>" maxlength="4" size="4" 
                       value="<%=DateUtils.getYearToString(lAnnoModel.getDataReclusioneA())%>" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="Javascript:testCalcolaPresofferto('MC_<%=i%>')">
                <% } else { %>                       
                <input type="text" name="GG_AL_<%=i%>" id="GG_AL_<%=i%>" maxlength="2" size="2" value="" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="Javascript:testCalcolaPresofferto('MC_<%=i%>')">
                /
                <input type="text" name="MM_AL_<%=i%>" id="MM_AL_<%=i%>" maxlength="2" size="2" value="" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="Javascript:testCalcolaPresofferto('MC_<%=i%>')">
                /
                <input type="text" name="AA_AL_<%=i%>" id="AA_AL_<%=i%>" maxlength="4" size="4" value="" 
                       onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="Javascript:testCalcolaPresofferto('MC_<%=i%>')">
                <% } %>                       
              </td>
              <td class="L" nowrap>
                Pari a <a href="Javascript:callCalcolaQuantum('MC_<%=i%>');"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>freccia_verde.gif" alt="Calcola Quantum" border=0></a>:
              </td>  
              <td class="l" id="Quantum_MC_<%=i%>" nowrap width="200px">
                <% if (lAnnoModel.getIdAnnotazioneManuale()!=null) {%>
                Anni   <font class="campo"><%=lAnnoModel.getNumAnniReclusione()%></font>
                Mesi   <font class="campo"><%=lAnnoModel.getNumMesiReclusione()%></font>
                Giorni <font class="campo"><%=lAnnoModel.getNumGiorniReclusione()%></font>
                <% } else { %>   
                &nbsp;                    
                <% } %>                       
              </td>
              <td class="l">
                <a href="Javascript:pulisci_rigo_presofferto(<%=i%>);"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>siapcan.gif" alt="Cancella contenuto della riga" border=0></a>
              </td>
            </tr>            
          </table>
        </td>
      </tr>
      <% } %>
    </table>
    <table>
      <tr>
        <td>
          <a href="Javascript:aggiungi_record_presofferto();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" alt="Aggiungi rigo" border=0></a>
        </td>
        <td>
          <a href="Javascript:elimina_record_presofferto();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" alt="Elimina rigo" border=0></a>
        </td>
      </tr>
    </table>


<!-- 
================================================================================
                         SEZIONE CON I DATI CALCOLATI
================================================================================
    <br>
-->
    <table width="100%" style="border; 0;">
      <tr><td class="titolo" colspan="100%" >Totali Finali</td></tr>
      <tr>
        <td class="l"><font class="l">Data Decorrenza: &nbsp;</font>
          <input type="text" name="GG_decorrenza"  id="GG_decorrenza" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aDataDecorrenza, "dd"))%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" name="MM_decorrenza" id="MM_decorrenza" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aDataDecorrenza, "MM"))%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" name="AA_decorrenza" id="AA_decorrenza"maxlength="4" size="4" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aDataDecorrenza, "yyyy"))%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          &nbsp;&nbsp;
          <INPUT class="bottone" type="button" name="subm2" value="Conferma" onClick="javascript:Verify_Dati();">&nbsp;&nbsp;
          &nbsp;&nbsp;
          <INPUT class="bottone" type="button" name="subm2" value="Pulisci Dati" title="Pulisce i dati in maschera per un nuovo calcolo" onClick="javascript:pulisciMaschera();">&nbsp;&nbsp;
        </td>
      </tr>
      <tr>
        <td class="l"><font class="l">Data Scarcerazione: &nbsp;</font>
          <input type="text" name="GG_scarcerazione"  id="GG_scarcerazione" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aDataScarcerazione, "dd"))%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" name="MM_scarcerazione" id="MM_scarcerazione" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aDataScarcerazione, "MM"))%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" name="AA_scarcerazione" id="AA_scarcerazione"maxlength="4" size="4" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aDataScarcerazione, "yyyy"))%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        <!--/td>
      </tr>      
      <tr>
        <td class="l"-->
        &nbsp;&nbsp;<font class="l">Data Interruzione: &nbsp;</font>
          <input type="text" name="GG_interruzione"  id="GG_interruzione" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aDataInterruzione, "dd"))%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" name="MM_interruzione" id="MM_interruzione" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aDataInterruzione, "MM"))%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" name="AA_interruzione" id="AA_interruzione"maxlength="4" size="4" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aDataInterruzione, "yyyy"))%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>      

      <tr><td>&nbsp;</td></tr>
      
      <tr>
        <td>
          <table style="border; 0;">
            <tr>
              <td class=titolo colspan=1>Totale Reclusione</td>
              <td width=1>&nbsp;</td>
              <td class="titolo" colspan=1>Totale Arresto</td>
              <td width=1>&nbsp;</td>
              <td class="titolo" colspan="1" title="Liberazione Anticipata">Tot. Lib. An.</td>
              <td width=1>&nbsp;</td>
              <td class="titolo" colspan=1>Totale Presofferti</td>
            </tr>
            
            <tr>
              <td class="l" width="250px" nowrap>
                <% if (aPenaRideterminata.getPenaResiduaRicalcolata()!=null) {%>
                Anni   <font class=campo><%= StringUtils.toStringJSP  ( aPenaRideterminata.getPenaResiduaRicalcolata().getNumAnniReclusione()   , "-")%></font>
                Mesi   <font class=campo><%= StringUtils.toStringJSP  ( aPenaRideterminata.getPenaResiduaRicalcolata().getNumMesiReclusione()   , "-")%></font>
                Giorni <font class=campo><%= StringUtils.toStringJSP  ( aPenaRideterminata.getPenaResiduaRicalcolata().getNumGiorniReclusione() , "-")%></font>
                Multa  <font class=campo><%= StringUtils.toEuroFormat ( aPenaRideterminata.getPenaResiduaRicalcolata().getImportoMulta())%></font>
                <% } else { %>
                &nbsp;-
                <% } %>
              </td>
            
              <td width=1>&nbsp;</td>
              
              <td class="l" width="250px" nowrap>
                <% if (aPenaRideterminata.getPenaResiduaRicalcolata()!=null) {%>
                Anni   <font class=campo><%= StringUtils.toStringJSP  ( aPenaRideterminata.getPenaResiduaRicalcolata().getNumAnniArresto()   , "-")%></font>
                Mesi   <font class=campo><%= StringUtils.toStringJSP  ( aPenaRideterminata.getPenaResiduaRicalcolata().getNumMesiArresto()   , "-")%></font>
                Giorni <font class=campo><%= StringUtils.toStringJSP  ( aPenaRideterminata.getPenaResiduaRicalcolata().getNumGiorniArresto() , "-")%></font>
                Multa  <font class=campo><%= StringUtils.toEuroFormat ( aPenaRideterminata.getPenaResiduaRicalcolata().getImportoAmmenda())%></font>
                <% } else { %>
                &nbsp;-
                <% } %>
              </td>
        
              <td width="1">&nbsp;</td>
        
              <td class="l" width="100px">
                <% if (aPenaRideterminata.getPenaResiduaRicalcolata()!=null) {%>
                Giorni <font class=campo><%= aPenaRideterminata.getLiberazioneAnticipata()%></font>
                <% } else { %>
                &nbsp;-
                <% } %>
              </td>

              <td width="1">&nbsp;</td>

              <td class="l" width="170px" nowrap>
                <% if (aPenaRideterminata.getPenaResiduaRicalcolata()!=null) 
                {
                  CalendarModel lComputiReclusione = aPenaRideterminata.getComputiReclusione("-");
                %>
                Anni   <font class=campo><%= lComputiReclusione.getNumAnni()   %></font>
                Mesi   <font class=campo><%= lComputiReclusione.getNumMesi()   %></font>
                Giorni <font class=campo><%= lComputiReclusione.getNumGiorni() %></font>
                <% } else { %>
                &nbsp;-
                <% } %>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <!-- -->

      <%              
      //========================================================================
      // Decorrenza Scadenza ed eventuale Fungibilità
      //========================================================================
//if (aDataInterruzione==null){      
      CalendarModel lQuantumFungibilita = null;
      String lDataScarcString = "";
      if ( aPenaRideterminata.getFungibilitaCalcolata()!=null) 
      {
        FungibilitaModel lFungibilitaModel = (FungibilitaModel) aPenaRideterminata.getFungibilitaCalcolata();
        lQuantumFungibilita = lFungibilitaModel.getQuantumFungibilita();
      
        CalendarUtil lCalendarUtil = new CalendarUtil();
        if ( !lCalendarUtil.isZero(lQuantumFungibilita) )
        {
          if (aDataDecorrenza==null){
            lDataScarcString = "";
          }
          else if (aDataScarcerazione==null){
            lDataScarcString = "ad <font color='red'>Oggi</font>";
          } 
          else {
            lDataScarcString = "al <font color='red'>"+DateUtils.getDateToString(aDataScarcerazione,"dd-MM-yyyy")+"</font>";
          }
        }
        else
        {
          lQuantumFungibilita = null;
        }
      }
      %>
      
<% if (aDataInterruzione==null){ %>
      <tr>
<% } else { %>      
      <tr style="display:none">
<% } %>      
        <td>
          <table>
            <tr>
              <td class="titolo">Data Decorrenza Pena </td>
              <td class="titolo">Data Fine Reclusione </td>
              <td class="titolo">Data Inizio Arresto </td>
              <td class="titolo">Data Fine Pena </td>
              <% if (lQuantumFungibilita!=null){ %>
              <td width="30">&nbsp;</td>
              <td class="titolo">Pena espiata in eccesso </td>
              <% }%>
            </tr>
            <tr>
              <% 
              if (aPenaRideterminata.getPenaResiduaRicalcolata()!=null) {%>
              <td class="l"><font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaRideterminata.getPenaResiduaRicalcolata().getDataInizio(),"dd-MM-yyyy"),"-")%></font></td>
              <td class="l"><font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaRideterminata.getPenaResiduaRicalcolata().getDataFineReclusione(),"dd-MM-yyyy"),"-")%></font></td>
              <td class="l"><font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaRideterminata.getPenaResiduaRicalcolata().getDataInizioArresto(),"dd-MM-yyyy"),"-")%></font></td>
              <td class="l" id="Data_fine_pena"><font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaRideterminata.getPenaResiduaRicalcolata().getDataFine(),"dd-MM-yyyy"),"-")%></font></td>
              <% if (lQuantumFungibilita!=null){ %>
              <td width="30">&nbsp;</td>
              <td class="l"><%=lDataScarcString%>
                  Anni   <font color="red"> <%= StringUtils.toStringJSP  ( new BigDecimal (lQuantumFungibilita.getNumAnni()  ), "0") %></font>
                  Mesi   <font color="red"> <%= StringUtils.toStringJSP  ( new BigDecimal (lQuantumFungibilita.getNumMesi()  ), "0") %></font>
                  Giorni <font color="red"> <%= StringUtils.toStringJSP  ( new BigDecimal (lQuantumFungibilita.getNumGiorni()), "0") %></font>
              </td>
              <% }  %>
              <% } else { %>
              <td class="l"><font class=campo>-</font></td>
              <td class="l"><font class=campo>-</font></td>
              <td class="l"><font class=campo>-</font></td>
              <td class="l" id="Data_fine_pena"><font class=campo>-</font></td>
              <% }  %>
            </tr>
          </table>
        </td>
      </tr>
      
      <!-- 
      Pena residua, pena espiata, nel caso di interruzione
      -->
      <% 
//      } else if (aDataInterruzione!=null) { 
%>
<% if (aDataInterruzione!=null){ %>
      <tr>
<% } else { %>      
      <tr style="display:none">
<% } %>      
        <td>
          <table>
            <!--tr>
              <td class=titolo colspan="100%">Dopo Interruzione</td>
            </tr-->
            <tr>
              <td class=titolo colspan=1>Reclusione Residua</td>
              <td width=1>&nbsp;</td>
              <td class="titolo" colspan=1>Arresto Residuo</td>
              <td width=1>&nbsp;</td>
              <td class="titolo" colspan=1>Totale Espiata</td>
            </tr>
            
            <tr>
              <td class="l" width="250px" nowrap>
                <% if (aPenaResiduaDaInterr!=null) {%>
                Anni   <font class=campo><%= StringUtils.toStringJSP  ( aPenaResiduaDaInterr.getNumAnniReclusione()   , "-")%></font>
                Mesi   <font class=campo><%= StringUtils.toStringJSP  ( aPenaResiduaDaInterr.getNumMesiReclusione()   , "-")%></font>
                Giorni <font class=campo><%= StringUtils.toStringJSP  ( aPenaResiduaDaInterr.getNumGiorniReclusione() , "-")%></font>
                Multa  <font class=campo><%= StringUtils.toEuroFormat ( aPenaResiduaDaInterr.getImportoMulta())%></font>
<%--                 <% } else { %> --%>
<!--                 &nbsp;- -->
                <% } %>
              </td>
            
              <td width=1>&nbsp;</td>
              
              <td class="l" width="250px" nowrap>
                <% if (aPenaResiduaDaInterr!=null) {%>
                Anni   <font class=campo><%= StringUtils.toStringJSP  ( aPenaResiduaDaInterr.getNumAnniArresto()   , "-")%></font>
                Mesi   <font class=campo><%= StringUtils.toStringJSP  ( aPenaResiduaDaInterr.getNumMesiArresto()   , "-")%></font>
                Giorni <font class=campo><%= StringUtils.toStringJSP  ( aPenaResiduaDaInterr.getNumGiorniArresto() , "-")%></font>
                Multa  <font class=campo><%= StringUtils.toEuroFormat ( aPenaResiduaDaInterr.getImportoAmmenda())%></font>
<%--                 <% } else { %> --%>
<!--                 &nbsp;- -->
                <% } %>
              </td>
        
              <td width="1">&nbsp;</td>

              <td class="l" width="170px" nowrap id="Pena_espiata">
                <% if (aPenaEspiata!=null) { %>
                Anni   <font class=campo><%= aPenaEspiata.getNumAnni()   %></font>
                Mesi   <font class=campo><%= aPenaEspiata.getNumMesi()   %></font>
                Giorni <font class=campo><%= aPenaEspiata.getNumGiorni() %></font>
<%--                 <% } else { %> --%>
<!--                 &nbsp;- -->
                <% } %>
              </td>
            </tr>         
          </table>
        </td>
      </tr>
      <% //} %>
      

    </table>
  </form>  
</body>


</html>