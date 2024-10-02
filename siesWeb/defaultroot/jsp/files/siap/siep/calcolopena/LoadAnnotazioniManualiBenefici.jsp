<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaSigeModel"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<%// Pena residua corrente%>
<jsp:useBean id="PenaComplessiva"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="PenRes1"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="PenRes2"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>

<%// Reati%>
<jsp:useBean id="reati"         scope="request" class="java.util.Vector" /> <% // Reati sul fascicolo %>
<jsp:useBean id="ReaAntEffetti" scope="request" class="java.util.Vector" /> <% // Richieste con anticipazione %>
<jsp:useBean id="ReaRichiesti"  scope="request" class="java.util.Vector" /> <% // Richieste senza anticipazione %>
<jsp:useBean id="RichiesteAlGE" scope="request" class="java.util.Vector" />

<jsp:useBean id="listaDPR"               scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoAnnotazioneManuale" scope="request" class="java.lang.String" />

<%// bean valorizzati se provengo da 'aggiungi' ed è presente già a sistema una annotazione %>
<jsp:useBean id="OrdinanzaGEAnn"   scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<jsp:useBean id="OrdinanzaGEEve"   scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="UfficioEmittente" scope="request" class="java.lang.String" />

<jsp:useBean id="OrdinanzaGE"   scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel"/>
<jsp:useBean id="ProcedimentoGE"   scope="request" class="siap.sige.fascicolo.model.FascicoloSigeModel"/>

<%// bean utilizzati nelle sezioni commentate 
//<jsp:useBean id="TipiFontiReato" scope="request" class="java.lang.String"/>
//<jsp:useBean id="TipiSottonumerazione" scope="request" class="java.lang.String"/>
//<jsp:useBean id="LAConcesse"          scope="request" class ="java.math.BigDecimal" />
//<jsp:useBean id="LADaConcedere"       scope="request" class ="java.math.BigDecimal" />

BigDecimal LAConcesse    = (BigDecimal) request.getAttribute("LAConcesse");
BigDecimal LADaConcedere = (BigDecimal) request.getAttribute("LADaConcedere");

%>

<%
//==============================================================================
//          Form di inserimento dei dati della decisione del GE
// - include DettaglioSoggettoSentenza.jsp
// - Posizione Giuridica
// - Sezione con le annotazioni manuali relative a Richieste con Anticipazione degli effetti (non validate)
// - Sezione con le annotazioni manuali relative a Richieste senza Anticipazione (non validate)
// - Sezione contenente tutti i reati collegati al fascicolo (titoli di reato)
// - Sezione con la pena complessiva (se ergastolo) o PENA RESIDUA
// - SEZIONE CON I CAMPI DI IMPUT
//
// n.b. la pena residua visualizzata
//==============================================================================
%>

<%
  AnnotazioneManualeModel lAnnRichMod = new AnnotazioneManualeModel();
  
  if(RichiesteAlGE != null && RichiesteAlGE.size()!= 0) 
  {
    lAnnRichMod = (AnnotazioneManualeModel)RichiesteAlGE.get(RichiesteAlGE.size()-1);
  }

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

  boolean lOrdGEPresente = false;
  /*if(OrdinanzaGEAnn.getIdAnnotazioneManuale() != null)
  {
    lOrdGEPresente = true;
  }*/
  
  // Flag che indica la presenza dell'Ordinanza emessa dal GE (non simulata dall'ufficio del  PM)
  boolean lFlagOrdinanzaGE = false;
 
  if(OrdinanzaGE.getAnnotazioneManuale() != null && OrdinanzaGE.getAnnotazioneManuale().getIdAnnotazioneManuale() != null )
      lFlagOrdinanzaGE = true;
    
  String Richie="";
%>

<html>
<head>
  <title> [S.I.E.S.] - Annotazioni Manuali - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">

  function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  var data_to_verify;
  var obbligatori;
  var dataesiste;

  //============================================================================
  // funzione per la verifica dei dati imputati in maschera
  //============================================================================
  function Verify()
  {
    <% if (lFlagOrdinanzaGE)  { %>
        return true;
    <%
    }
    else
    { 
    %>
    //==========================================================================
    // I quantum non sono obbligatori se la decisione del GE è un Rigetto o 
    // 'dichiara inammissibile' una Richiesta senza anticipazione.
    // In tutti gli altri casi il quantum va imputato a mano.
    //==========================================================================
    // verifico se sono state selezionate delle richieste
        
    tipoRic = tipoRichiestaSelezionata();
    //alert ('Tipo Richieste = '+tipoRic);
    
    if (   (   document.f.TipoOrd[3].checked==true
            || document.f.TipoOrd[4].checked==true 
      //      || document.f.TipoOrd[5].checked==true
           )
        && (tipoRic=='senzaAnticipazione' )
       )
    {
      // Non controllo i quantum
      //alert("quantum non obbligatori 1");
      //return false;
    }
    // Decisione conforme a richiesta con anticipazione 
    else
    {
      // Flag +/- obbligatorio
      if (document.f.PM[document.f.PM.selectedIndex].value == "")
      {
        alert ("Selezionare tra + e -");
        return false;
      }
        
      obbligatori = (document.f.GRec.value!="" || document.f.MRec.value!="" || document.f.ARec.value!="");
      obbligatori = obbligatori || (document.f.Ammenda.value!="")
      obbligatori = obbligatori || (document.f.GArr.value!="" || document.f.MArr.value!="" || document.f.AArr.value!="");
      obbligatori = obbligatori || (document.f.Multa.value!="");
  
      if (!obbligatori)
      {
        alert ("Selezionare la  durata del periodo per arresto o reclusione oppure la sanzione");  
        return false;
      }
    }
  
    //==========================================================================
    // Controlli su dati dell'ordinanza
    //==========================================================================
    if (document.f.tipoannotazione[document.f.tipoannotazione.selectedIndex].value=="-")
    {
      alert("Selezionare computo beneficio");
      return false;
    }

    if (document.f.dpr[document.f.dpr.selectedIndex].value=="-")
    {
      alert("Selezionare DPR");
      return false;
    }

<%
      if(!lOrdGEPresente)
      {
%>

          if (document.f.DaGiArr.value.length>0 || document.f.DaMeArr.value.length>0 || document.f.DaAnArr.value.length>0)
          {
              if (document.f.DaGiArr.value.length<2)
                document.f.DaGiArr.value="0"+document.f.DaGiArr.value;
              if (document.f.DaMeArr.value.length<2)
                document.f.DaMeArr.value="0"+document.f.DaMeArr.value;
        
              data_to_verify = document.f.DaGiArr.value +"/"+document.f.DaMeArr.value+"/"+document.f.DaAnArr.value;
        
              if (! ControllaData(data_to_verify))
              {
                alert('Data di arrivo documento non valida');
                return false;
              }

              // La Data di arrivo documento deve essere <= SYSDATE
              var sysDate = new Date();
        
              var ggSysDate = sysDate.getDate();
              if(ggSysDate<10)
                ggSysDate = "0"+ggSysDate;
        
              var mmSysDate = (sysDate.getMonth()+1);
              if(mmSysDate<10)
                mmSysDate = "0"+mmSysDate;
        
              var yyyySysDate = sysDate.getYear()
        
              var strSysDate = ggSysDate + "/" + mmSysDate + "/" + yyyySysDate;
              //alert(strSysDate);
              if (!CompareDate(data_to_verify, strSysDate))
              {
                alert('Data di arrivo documento superiore alla data attuale');
                return false;
              }
          
          }  <% // Chiude if (document.f.DaGiArr.value.length>0  %>

  /**
   * CAMPI OBLIGATORI
   */
          var annoGE   = document.f.annoGe.value;
          var numeroGE = document.f.numeroGe.value;
          var AAGE     = document.f.DaAnArr.value;
          var MMGE     = document.f.DaMeArr.value;
          var GGGE     = document.f.DaGiArr.value;
          var motivazioni = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MOTIVAZIONI%>.value;
          var codTipoUffEmi = document.f.CodTipoUffEmi.value;
          var luogoUffEmi   = document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value;
          
          var AnnoSige  = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>.value;
          var ProgrSige = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE%>.value;

          if(   AnnoSige == '' && ProgrSige == ''
             && AAGE == '' && MMGE == '' && GGGE == ''
             && codTipoUffEmi == '-' && luogoUffEmi == '' )
          {
              alert("Dati Declaratoria Obbligatori");
              document.f.<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>.focus();
      
              return false;
          }
          
          if(AnnoSige == '')
          {
              alert("Dati Declaratoria Incompleti - Indicare Anno Procedimento SIGE");
              document.f.<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>.focus();
      
              return false;
          }
          
          if(ProgrSige == '')
          {
              alert("Dati Declaratoria Incompleti - Indicare Numero Procedimento SIGE");
              document.f.<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE%>.focus();
      
              return false;
          }
          if(GGGE == '')
          {
              alert("Dati Declaratoria Incompleti. Indicare la data Emissione.");
              document.f.DaGiArr.focus();      
              return false;
          }
          if(MMGE == '')
          {
              alert("Dati Declaratoria Incompleti. Indicare la data Emissione.");
              document.f.DaMeArr.focus();      
              return false;
          }
          if(AAGE == '')
          {
              alert("Dati Declaratoria Incompleti. Indicare la data Emissione.");
              document.f.DaAnArr.focus();      
              return false;
          }


          

          
/*
      if(motivazioni == '')
      {
        alert("Dati Declaratoria Incompleti");
        document.f.< %=ICostantiAnnotazioneManuale.CAMPO_MOTIVAZIONI%>.focus();

        return false;
      }
*/
          if(codTipoUffEmi == '-')
          {
              alert("Dati Declaratoria Incompleti. Tipo Ufficio Emittente obbligatorio");
              document.f.CodTipoUffEmi.focus();
      
              return false;
          }
          if(luogoUffEmi == '')
          {
              alert("Dati Declaratoria Incompleti. Sede Ufficio Emittente obbligatoria.");
              document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
      
              return false;
          }
<%
        }   // Chiude if(!lOrdGEPresente)
%>
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>.value=document.f.tipoannotazione.value;

          return true;
          
      } <%  // endif (lFlagOrdinanzaGE) %>
<%
  }    // end function verify() 
%>

  function Verify_Quantum()
  {
    document.f.operazione.value="Quantum";
    var RetVer = Verify();
    return RetVer;
  }

  
  //========================================================================
  // Funzione per nascondere un elemento dalla lista (deselezionarlo)
  //========================================================================
  function rimuovi(id_record)
  {
    document.getElementById(id_record).style.display='none';
    document.getElementById('cb_'+id_record).checked=false;
    
    righe_tabella = document.getElementById('tabella_richieste').all;
    isRichiestaSelezionata = 'false';
    for(i = 0; i < righe_tabella.length; i++){
      if (righe_tabella(i).tagName=='TR'){
        if (   righe_tabella(i).style.display=='block'
            && righe_tabella(i).id!='titolo_richieste'
           )
        {
          //alert(righe_tabella(i).tagName);
          isRichiestaSelezionata='true';
        }
      }
    }
    if (isRichiestaSelezionata=='false'){
      document.getElementById('titolo_richieste').style.display='none';
    }
    
    //= Aggiorno i quantum solo se Richieste senza Anticipazione e Conforme[1]
    //= in questo caso il quantum da concedere è pari alla somma dei quantum richiesti
    //= Oppure rigetto[3] o dichiara inammissibile[4] richieste conAnticipazione, in questo
    //= caso i quantum vanno..
    //= Negli altri casi: senza richiesta [0], diforme[2] i quantum vanno imputati
    //= a mano.
    if (document.f.TipoOrd[1].checked && tipoRichiestaSelezionata()=='senzaAnticipazione')
       aggiornaQuantum('Conferma');
    if (document.f.TipoOrd[3].checked && tipoRichiestaSelezionata()=='conAnticipazione')
       aggiornaQuantum('Annulla');
    if (document.f.TipoOrd[4].checked && tipoRichiestaSelezionata()=='conAnticipazione')
       aggiornaQuantum('Annulla');
  }
  
  //==========================
  //
  //==========================
  function isConforme() {
//    if (document.f.TipoOrd[1].checked && tipoRichiestaSelezionata()=='senzaAnticipazione')
    if (document.f.TipoOrd[1].checked )
      aggiornaQuantum('Conferma');
  }
  
  //==========================
  //
  //==========================
  function isRigetto() {
    if (document.f.TipoOrd[3].checked && tipoRichiestaSelezionata()=='conAnticipazione')
          aggiornaQuantum('Annulla');
    
    if (document.f.TipoOrd[3].checked && tipoRichiestaSelezionata()=='senzaAnticipazione')
        aggiornaQuantum('Conferma');
  }
  
  //==========================
  //
  //==========================
  function isInammissibile() {
    if (document.f.TipoOrd[4].checked && tipoRichiestaSelezionata()=='conAnticipazione')
      aggiornaQuantum('Annulla');
    
    if (document.f.TipoOrd[4].checked && tipoRichiestaSelezionata()=='senzaAnticipazione')
        aggiornaQuantum('Conferma');
  }
  
  //========================================================================
  // Funzione per il caricamento della richieste al GE
  //========================================================================
  function ListaRichiesteAlGE(a_formname, a_beneficio) {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.annotazionemanuale.action.ActListaRichiesteGE&formname="+a_formname+"&beneficio="+a_beneficio+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>", "Lista_Richieste_GE", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
  }

  //========================================================================
  // Restituisce il tipo di richiesta selezionata
  // - nessuna
  // - conAnticipazione
  // - senzaAnticipazione
  // - misto
  //========================================================================
  function tipoRichiestaSelezionata (){
    righe_tabella = document.getElementById('tabella_richieste').all;
    isRichiestaSelezionata = 'false';
    isAnticipazione='false';
    isSenzaAnticipazione='false';
    for(i = 0; i < righe_tabella.length; i++){
      if (righe_tabella(i).tagName=='TR'){
        if (   righe_tabella(i).style.display=='block'
            && righe_tabella(i).id!='titolo_richieste'
           )
        {
          //alert(righe_tabella(i).tagName);
          isRichiestaSelezionata='true';
          if (righe_tabella(i).tipoRich=='A')
            isAnticipazione='true';
          if (righe_tabella(i).tipoRich=='R')
            isSenzaAnticipazione='true';
        }
      }
    }
    
    if (isRichiestaSelezionata=='false')
      return 'nessuna';
    if (isAnticipazione=='true' && isSenzaAnticipazione=='false')
      return 'conAnticipazione';
    if (isAnticipazione=='false' && isSenzaAnticipazione=='true')
      return 'senzaAnticipazione';
    if (isAnticipazione=='true' && isSenzaAnticipazione=='true')
      return 'misto';
  }

  //============================================================================
  // Aggiorna i campi Reclusione e Arresto sommando i dati delle richieste 
  // selezionate
  // Se tipoAggiornamento = Conferma 
  // Se tipoAggiornamento = Annulla
  //============================================================================
  function aggiornaQuantum (tipoAggiornamento){
    //
    righe_tabella = document.getElementById('tabella_richieste').all;
    aaRec = 0;
    mmRec = 0;
    ggRec = 0;
    multa = 0.00;
    
    aaArr = 0;
    mmArr = 0;
    ggArr = 0;
    ammenda = 0.00;
    
    for(i = 0; i < righe_tabella.length; i++){
      if (righe_tabella(i).tagName=='TR'){
        if (   righe_tabella(i).style.display=='block'
            && righe_tabella(i).id!='titolo_richieste'
           )
        {
          if (righe_tabella(i).segno=='+')
          { //alert("revocati");
            aaRec   = aaRec   + parseInt(righe_tabella(i).aaRec);
            mmRec   = mmRec   + parseInt(righe_tabella(i).mmRec);
            ggRec   = ggRec   + parseInt(righe_tabella(i).ggRec);
            multa   = multa   + parseFloat(righe_tabella(i).multa);
            
            aaArr   = aaArr   + parseInt(righe_tabella(i).aaArr);
            mmArr   = mmArr   + parseInt(righe_tabella(i).mmArr);
            ggArr   = ggArr   + parseInt(righe_tabella(i).ggArr);
            ammenda = ammenda + parseFloat(righe_tabella(i).ammenda);
          }
          else
          { //alert("concessi");
            aaRec   = aaRec   - parseInt(righe_tabella(i).aaRec);
            mmRec   = mmRec   - parseInt(righe_tabella(i).mmRec);
            ggRec   = ggRec   - parseInt(righe_tabella(i).ggRec);
            multa   = multa   - parseFloat(righe_tabella(i).multa);
            
            aaArr   = aaArr   - parseInt(righe_tabella(i).aaArr);
            mmArr   = mmArr   - parseInt(righe_tabella(i).mmArr);
            ggArr   = ggArr   - parseInt(righe_tabella(i).ggArr);
            ammenda = ammenda - parseFloat(righe_tabella(i).ammenda);
          }
        }
      }
    }

//    alert ('multa: '+multa);
//    alert ('ammenda: '+ammenda);

    recNorm = normalizzaQuantum(aaRec,mmRec,ggRec);
    annNorm = normalizzaQuantum(aaArr,mmArr,ggArr);
    
//    alert ('segno rec: '+recNorm[0]+','+recNorm[1]+','+recNorm[2]+','+recNorm[3] );
//    alert ('segno arr: '+annNorm[0]+','+annNorm[1]+','+annNorm[2]+','+annNorm[3] );
    
    if (recNorm[0]=='-' || annNorm[0]=='-')
    {
      if (tipoAggiornamento=='Annulla')
        document.f.PM[1].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
      else
        document.f.PM[2].selected=true; // segno -
    }
    else if (recNorm[1]!='0' || recNorm[2]!='0' || recNorm[3]!='0' || annNorm[1]!='0' || annNorm[2]!='0' || annNorm[3]!='0')
    {
      if (tipoAggiornamento=='Annulla')
        document.f.PM[2].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
      else
        document.f.PM[1].selected=true; // segno +
    }
    else {
      // quantum nulli ho solo la pena pecuniaria devo utilizzare il segno della
      // multa o ammenda
//      alert ('quantum nulli');
      if (multa<0 || ammenda<0){
        if (tipoAggiornamento=='Annulla')
          document.f.PM[1].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
        else
          document.f.PM[2].selected=true; // segno -
      }
      else{
        if (tipoAggiornamento=='Annulla')
          document.f.PM[2].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
        else
          document.f.PM[1].selected=true; // segno +
      }
      
    }
    
    if (multa<0)   multa   = multa*(-1);
    if (ammenda<0) ammenda = ammenda*(-1);
      
    // Reclusione    
    document.f.ARec.value  = recNorm[1];
    document.f.MRec.value  = recNorm[2];
    document.f.GRec.value  = recNorm[3];
    document.f.Multa.value = parseInt(multa);
    document.f.Mul_dec.value = getParteDecimale(multa);
    // Arresti    
    document.f.AArr.value=annNorm[1];
    document.f.MArr.value=annNorm[2];
    document.f.GArr.value=annNorm[3];
    document.f.Ammenda.value=parseInt(ammenda); // recupera la parte intera
    document.f.Amm_dec.value=getParteDecimale(ammenda);
  }
  
  //========================================================================
  // 
  //========================================================================
  function normalizzaQuantum(anni,mesi,giorni)
  {
    tot_giorni=anni*30*12 + mesi*30 + giorni;
    segno='+';
    //alert("tot_giorni = "+tot_giorni);
    if (tot_giorni<0){
      tot_giorni=tot_giorni*(-1);
      segno='-';
    }
    giorni = tot_giorni % 30; // restituisce il resto dell'operazione
    tot_giorni = tot_giorni-giorni;
    tot_mesi = parseInt(tot_giorni/30); // es 55 mesi
    mesi = tot_mesi % 12;
    tot_mesi = tot_mesi - mesi;
    anni = parseInt(tot_mesi/12);
    //alert("anni = "+anni);
    //alert("mesi = "+mesi);
    //alert("giorni = "+giorni);
    var quantumNormalizzati = new Array(segno,anni ,mesi ,giorni );
    return quantumNormalizzati;
  }      
  
  //============================================================================
  // estrae la parte decimale da un number
  //============================================================================
  function getParteDecimale(valore){
    //alert("valore = "+valore);
    valoreStr = String(valore);
    //alert("valoreStr = "+valoreStr);
    posVirgola=-1;
    parteDecimale = "00";
    for (i=0;i<valoreStr.length;i++){
      //alert(valoreStr.substr(i,1));
      if (valoreStr.substr(i,1)=='.')
        posVirgola = i;
    }
    if (posVirgola<0)
      parteDecimale = "00";
    else
      parteDecimale = valoreStr.substr(posVirgola+1,valoreStr.length-(posVirgola+1));
      
//    alert ("parteDecimale = "+parteDecimale);
    return parteDecimale;
  }
  
</script>

</head>

<body class="corpo">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciAnnotazioniManualiBenefici">
    <input type="hidden" name="operazione" value="">
    
    <input type="hidden" name="flagOrdinanza" value="<%=lOrdGEPresente%>">
    <input type="hidden" name="lFlagPage" value="AMNI">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="0284">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>" value="003">
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%-- input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>" value="<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getIdAnnotazioneManuale())%>"--%>
  <!--  MEV_2019-09 - Id Annotazione della richiesta iniziale -->
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>" value="<%=StringUtils.toStringJSP(lAnnRichMod.getIdAnnotazioneManuale())%>">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Amnistia/Indulto</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
<table>
  <tr>
    <td class="l">
      Posizione Giuridica :
      <font class="campo">
      <%if(lFascicoloAssociato.getFlagAltraCausa() !=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
        DETENUTO PER ALTRA CAUSA <%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>
      <% } else { %>
        <%=lPosizione.getDescrPosizioneGiuridica()%>
      <% } %>
      </font>
    </td>
  </tr>
</table>


<%
  if(!lFlagOrdinanzaGE) 
  {

//==============================================================================
// Tabella con la lista delle richieste con e senza anticipazione già validate
// n.b. nasce con i record nascosti che vengono abilitati da Seleziona Richieste
//      simulando il caricamento dinamico in maschera
//==============================================================================
%>
<input type="hidden" name="NumTotRichieste" value="<%=RichiesteAlGE.size()%>">
<table style="width: 95%;" id="tabella_richieste">
  <tr><td colspan="5" class="Titolo">Richieste al GE</td></tr>
  <tr>
    <td class="l" colspan="5">
      <a href="Javascript:ListaRichiesteAlGE('f', 'AMNI');">
        Seleziona Richieste <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
<%
int id_record = 0;
if (RichiesteAlGE.size()!=0)
{
%>  
  <tr style="display:none;" id="titolo_richieste">
    <td class="c">Tipo</td>
    <td class="c">Reclusione</td>
    <td class="c">Multa</td>
    <td class="c">Arresto</td>
    <td class="c">Ammenda</td>
    <td class="c">Anticipazione</td>
    <td class="c">&nbsp;</td>
  </tr>
<%
  Iterator itx = RichiesteAlGE.iterator();
  for (int i = 0; itx.hasNext(); i++)
  {
    id_record++;
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)itx.next();
    String tipoRichiesta = "";
    if (   lAnnMod.getFlagAppProvvisoria()!=null 
           && lAnnMod.getFlagAppProvvisoria().equals("A")
       )
    {
      tipoRichiesta = "A";
      Richie = "A";
    }
    else
    {
      tipoRichiesta = "R";
    }
%>
  <tr style="display:none;" id="record_<%=id_record%>"
  <%-- Ticket#20210622011 - Ripristino del caricamento dati --%>
   	   tipoRich="<%=tipoRichiesta%>" 
       segno="<%=StringUtils.toStringJSP(lAnnMod.getFlagPiuMeno(),"")%>" 
       aaRec="<%=StringUtils.toStringJSP (lAnnMod.getNumAnniReclusione(),"0")%>" 
       mmRec="<%=StringUtils.toStringJSP (lAnnMod.getNumMesiReclusione(),"0")%>" 
       ggRec="<%=StringUtils.toStringJSP (lAnnMod.getNumGiorniReclusione(),"0")%>" 
       multa ="<%=StringUtils.toStringJSP(lAnnMod.getImportoMulta(),"0.00")%>"  
       aaArr="<%=StringUtils.toStringJSP (lAnnMod.getNumAnniArresto(),"0")%>" 
       mmArr="<%=StringUtils.toStringJSP (lAnnMod.getNumMesiArresto(),"0")%>" 
       ggArr="<%=StringUtils.toStringJSP (lAnnMod.getNumGiorniArresto(),"0")%>" 
       ammenda ="<%=StringUtils.toStringJSP(lAnnMod.getImportoAmmenda(),"0.00")%>"  
  <%-- Ticket#20210622011 --%>
      >
      
    <td class="l" style="text-align:center">
      <font class=campo><%=StringUtils.toStringJSP(lAnnMod.getFlagPiuMeno(),"")%></font>
    </td>
    <td class="l" style="text-align:center">
       Anni   <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumAnniReclusione(),"0")%></font>
       Mesi   <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumMesiReclusione(),"0")%></font>
       Giorni <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumGiorniReclusione(),"0")%></font>
    </td>
    <td class="l" style="text-align:center">   
       Multa  <font class=campo><%=StringUtils.toEuroFormat(lAnnMod.getImportoMulta())%></font>
    </td>
    <td class="l" style="text-align:center">
       Anni    <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumAnniArresto(),"0")%></font>
       Mesi    <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumMesiArresto(),"0")%></font>
       Giorni  <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumGiorniArresto(),"0")%></font>
    </td>
    <td class="l" style="text-align:center">   
       Ammenda <font class=campo><%=StringUtils.toEuroFormat(lAnnMod.getImportoAmmenda())%></font>
    </td>
    <% if ( tipoRichiesta.equals("A")) { %>
    <td class="C"><img src="/images/V.gif"> </td>
    <% } else {%>
    <td class="l">&nbsp;</td>
    <% }%>
    <td class="l">
      <a href="Javascript:rimuovi('record_<%=id_record%>');">
        <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0" title="Elimina dalla lista">
      </a>
    </td>
    <td class="l" style="display:none;">
      <input type="checkbox" name="cb_record_<%=id_record%>" id="cb_record_<%=id_record%>" value="<%=lAnnMod.getIdAnnotazioneManuale()%>" >
    </td>
  </tr>
<% }   %>
</table>
<%
}
%>
    
<%
//==============================================================================
// Sezione contenente l'elenco delle richieste con anticipazione degli effetti.
// Quali, quelle non ancora validate
// Per ogni annotazione vengono visualizzati i seguenti dati:
// - reato o Pena Complessiva
// - quantum reclusione richiesto
// - quantum arresto    richiesto
//==============================================================================
if (ReaAntEffetti.size()!=0)
{
%>
<table style="width: 95%;">
  <tr><td colspan=3 class="Titolo">Richiesta Anticipazioni degli effetti</td></tr>
  <tr>
    <td class="c">Applicata a</td>
    <td class="c">Reclusione</td>
    <td class="c">Arresto</td>
  </tr>
<%
  AnnotazioneManualeModel tmp = null;
  ReatoModel rtmp = new ReatoModel();
  BigDecimal idRea;

  for (int i=0; i<ReaAntEffetti.size(); i++)
  {
      out.println("<tr>");

      tmp=(AnnotazioneManualeModel)ReaAntEffetti.get(i);

      idRea=tmp.getReaIdReato();
      boolean found=false;
      for (int itR=0;itR<reati.size();itR++)
      {
        rtmp=(ReatoModel)reati.get(itR);
        if (rtmp.getIdReato().equals(idRea))
        {
          found=true;
          itR=reati.size()+1;
        }
      }

      out.println("<td class=\"l\">");

      if (found) // esiste reato associato all'annotazione
      {
        boolean lFlagAnnoNumero;
        lFlagAnnoNumero = false;
        if( rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals("") && rtmp.getNumeroFonte() != null  && !rtmp.getNumeroFonte().equals("") )
          lFlagAnnoNumero = true;
%>
        <font class="label">
<%
        if (rtmp.getProgrNumeroManuale() != null && !rtmp.getProgrNumeroManuale().equals(""))
        {
          out.println("n." + rtmp.getProgrNumeroManuale()+": ");
        }
        else
        {
          out.println("n." + rtmp.getProgrReato()+": ");
        }
%>
        </font>
        <font class="campo">
<%
        if(lFlagAnnoNumero)
        {
          if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
            out.println(rtmp.getDescrFonte()+" ");
          if(rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals(""))
            out.println(rtmp.getAnnoFonte());
          if(rtmp.getNumeroFonte() != null && !rtmp.getNumeroFonte().equals(""))
            out.println("/"+rtmp.getNumeroFonte());
        }

        if(rtmp.getArticolo() != null && !rtmp.getArticolo().equals(""))
          out.println("art."+rtmp.getArticolo());
        if(rtmp.getDescrSottonumerazione() != null && !rtmp.getDescrSottonumerazione().equals("") && !rtmp.getDescrSottonumerazione().equals("-"))
          out.println(" "+rtmp.getDescrSottonumerazione());

        if(!lFlagAnnoNumero)
        {
          if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
            out.println(rtmp.getDescrFonte());
        }

        if(rtmp.getComma() != null && !rtmp.getComma().equals(""))
          out.println(" c. "+rtmp.getComma());
        if(rtmp.getLettera() != null && !rtmp.getLettera().equals(""))
          out.println(" l. "+rtmp.getLettera());
        if(rtmp.getNumero() != null && !rtmp.getNumero().equals(""))
          out.println(" n. "+rtmp.getNumero());%></font>
<%
      }
      else
      { // non è stato associato un reato un fase di richiesta
        out.println("<font class=\"campo\">Pena Complessiva</font>");
      }
      out.println("</td>");
%>
      <td class="l">
         Anni   <font class=campo><%=StringUtils.toStringJSP(tmp.getNumAnniReclusione(),"0")%></font>
         Mesi   <font class=campo><%=StringUtils.toStringJSP(tmp.getNumMesiReclusione(),"0")%></font>
         Giorni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumGiorniReclusione(),"0")%></font>
         Multa  <font class=campo><%=StringUtils.toEuroFormat(tmp.getImportoMulta())%></font>
      </td>
      <td class="l">
         Anni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumAnniArresto(),"0")%></font>
         Mesi <font class=campo><%=StringUtils.toStringJSP(tmp.getNumMesiArresto(),"0")%></font>
         Giorni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumGiorniArresto(),"0")%></font>
         Ammenda <font class=campo><%=StringUtils.toEuroFormat(tmp.getImportoAmmenda())%></font>
      </td>
<%
    out.println("</tr>");
   }
%>
   </table>
<%
  }  // fine elenco richieste con anticipazione


//==============================================================================
// Sezione con le annotazioni manuali relative a Richieste senza Anticipazione
// Per ogni annotazione vengono visualizzati i seguenti dati:
// - reato o Pena Complessiva
// - quantum reclusione richiesto
// - quantum arresto    richiesto
//==============================================================================
if (ReaRichiesti.size()!=0)
{
%>
<table style="width: 95%;">
  <tr><td colspan=3 class="Titolo">Richiesta</td></tr>
  <tr>
    <td class="c">Applicata a</td>
    <td class="c">Reclusione</td>
    <td class="c">Arresto</td>
  </tr>
<%
  AnnotazioneManualeModel tmp = null;
  ReatoModel rtmp = new ReatoModel();
  BigDecimal idRea;

  for (int i=0; i<ReaRichiesti.size(); i++)
  {
    out.println("<tr>");
    tmp=(AnnotazioneManualeModel)ReaRichiesti.get(i);
    idRea=tmp.getReaIdReato();
    boolean found=false;
    for (int itR=0;itR<reati.size();itR++)
    {
      rtmp=(ReatoModel)reati.get(itR);
      if (rtmp.getIdReato().equals(idRea))
      {
       found=true;
       itR=reati.size()+1;
      }
    }

    out.println("<td class=\"l\">");

    if (found)
    {
      boolean lFlagAnnoNumero;
      lFlagAnnoNumero = false;
      if( rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals("") && rtmp.getNumeroFonte() != null  && !rtmp.getNumeroFonte().equals("") )
        lFlagAnnoNumero = true;
%>
        <font class="label">
<%
        if (rtmp.getProgrNumeroManuale() != null && !rtmp.getProgrNumeroManuale().equals(""))
        {
          out.println("n." + rtmp.getProgrNumeroManuale()+": ");
        }
        else
        {
          out.println("n." + rtmp.getProgrReato()+": ");
        }
%>
      </font>
      <font class="campo">
<%
          if(lFlagAnnoNumero)
          {
            if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
              out.println(rtmp.getDescrFonte()+" ");
            if(rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals(""))
              out.println(rtmp.getAnnoFonte());
            if(rtmp.getNumeroFonte() != null && !rtmp.getNumeroFonte().equals(""))
              out.println("/"+rtmp.getNumeroFonte());
          }

          if(rtmp.getArticolo() != null && !rtmp.getArticolo().equals(""))
            out.println("art."+rtmp.getArticolo());
          if(rtmp.getDescrSottonumerazione() != null && !rtmp.getDescrSottonumerazione().equals("") && !rtmp.getDescrSottonumerazione().equals("-"))
            out.println(" "+rtmp.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
              out.println(rtmp.getDescrFonte());
          }

          if(rtmp.getComma() != null && !rtmp.getComma().equals(""))
            out.println(" c. "+rtmp.getComma());
          if(rtmp.getLettera() != null && !rtmp.getLettera().equals(""))
            out.println(" l. "+rtmp.getLettera());
          if(rtmp.getNumero() != null && !rtmp.getNumero().equals(""))
            out.println(" n. "+rtmp.getNumero());%></font>
<%
      }
      else
      {
        out.println("<font class=\"campo\">Pena Complessiva</font>");
      }

      out.println("</td>");
%>
        <td class="l">
           Anni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumAnniReclusione(),"0")%></font>
           Mesi <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumMesiReclusione(),"0")%></font>
           Giorni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumGiorniReclusione(),"0")%></font>
           Multa <font class="campo"><%=StringUtils.toEuroFormat(tmp.getImportoMulta())%></font>
        </td>
        <td class="l">
           Anni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumAnniArresto(),"0")%></font>
           Mesi <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumMesiArresto(),"0")%></font>
           Giorni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumGiorniArresto(),"0")%></font>
           Ammenda <font class="campo"><%=StringUtils.toEuroFormat(tmp.getImportoAmmenda())%></font>
        </td>
<%
    out.println("</tr>");
   }
%>
   </table>
<%
}
  } // endif (!lFlagOrdinanzaGE)
%>
<br>
<%
//==============================================================================
//       Sezione contenente tutti i reati collegati al fascicolo
//==============================================================================
if (!reati.isEmpty())
{
%>
<table style="width: 95%;">
  <tr><td colspan=7 class="Titolonocap">Titoli di reato</td></tr>
  <tr>
    <td class="c">Reato</td>
    <td class="c">Durata</td>
    <td class="c">Sanzione</td>
    <td class="c">Sel.</td>
    <td class="c">Ann.Inserita</td>
  </tr>
<%
  ReatoModel lReato;
  boolean lFlagAnnoNumero;
  for (int i=0;i<reati.size();i++)
  {
    lReato=(ReatoModel)reati.get(i);
    lFlagAnnoNumero = false;
    
    if(  lReato.getAnnoFonte() != null
       && !lReato.getAnnoFonte().equals("")
       && lReato.getNumeroFonte() != null
       && !lReato.getNumeroFonte().equals("") )
    {
      lFlagAnnoNumero = true;
    }
%>
  <tr>
    <td class="l">
      <font class="label">
      <% if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals("")) { %>
        n.<%=lReato.getProgrNumeroManuale()%>: 
      <% } else { %>
        n.<%=lReato.getProgrReato()%>:
      <% } %>
      </font>
      <font class="L">
<%
        if(lFlagAnnoNumero)
        {
          if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
            out.println(lReato.getDescrFonte()+" ");
          if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
            out.println(lReato.getAnnoFonte());
          if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
            out.println("/"+lReato.getNumeroFonte());
        }

        if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
          out.println("art."+lReato.getArticolo());
        if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
          out.println(" "+lReato.getDescrSottonumerazione());

        if(!lFlagAnnoNumero)
        {
          if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
            out.println(lReato.getDescrFonte());
        }

        if(lReato.getComma() != null && !lReato.getComma().equals(""))
          out.println(" c. "+lReato.getComma());
        if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
          out.println(" l. "+lReato.getLettera());
        if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
          out.println(" n. "+lReato.getNumero());%>
      </font>
      
      <% if(lReato.getStringaConsumazione()!= null) { %>
      <font class="campo"><%=StringUtils.toStringJSP(lReato.getStringaConsumazione())%>,</font>
      <% } %>
    
      <%if(lReato.getNote() != null && !lReato.getNote().equals("")) { %>
      <font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
      <% } %>

      <% if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals("")) { %>
      <font class="label">Luogo</font>&nbsp;
      <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
      <% } %>
    </td>

    <td class="l">
      <table>
        <tr>
          <td class="lnobord"><font class="label">AA</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumAnni(),"0")%></font></td>
          <td class="lnobord"><font class="label">MM</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumMesi(),"0")%></font></td>
          <td class="lnobord"><font class="label">GG</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumGiorni(),"0")%></font></td>
        </tr>
      </table>
    </td>
    <td class="r">
      <font class="campo">
        <%= StringUtils.toEuroFormat(lReato.getSanzionePecuniaria())%>
      </font>
      €
      <% if (lReato.getSanzionePecuniaria() != null && lReato.getSanzionePecuniaria().compareTo(new BigDecimal(0)) != 0) { %>
        di
        <font class="campo">
          <%= StringUtils.toStringJSP(lReato.getDescrTipoSanzione())%>
        </font>&nbsp;
      <% } %>
    </td>
    <td class="c"><input type="radio" name="IdReato" value="<%= lReato.getIdReato() %>"></td>
    <% if(lReato.getFlagVisto()!= null && lReato.getFlagVisto().equals("S")) { %>
    <td class="C"><img src="/images/V.gif"> </td>
    <% } else { %>
    <td class="C"> &nbsp;</td>
    <% } %>
    </tr>
<%
    }
%>
    </table>
<%
  }
%>

<%
//==============================================================================
//               Sezione con la Visualizzazione della Pena
// - Se ERGASTOLO: solo data inizio e data fine MAI
// - Se Libero vengono visualizzati i Quantum 
//   - PenRes1 = Reclusione
//   - PenRes2 = Arresti
// - Se detenuto viene visualizzato il quantum residuo calcolato al volo tra la 
//   data di sistema e la data fine pena prevista (da correggere) e le date di
//   decorrenza
//==============================================================================
// se la Pena Complessiva è un ergastolo o ergastolo con isolamento

if(   PenaComplessiva.getCodTipoPenaDetentiva() != null
   && PenaComplessiva.getCodTipoPenaDetentiva() != ""
   && ( PenaComplessiva.getCodTipoPenaDetentiva().equals("03") || PenaComplessiva.getCodTipoPenaDetentiva().equals("04") )
    )
{
%>
<table style="width: 95%;">
  <tr>
    <td colspan=3 class="Titolonocap">Pena complessiva</td>
  </tr>
  <tr>
    <td class="l">
      <font class="campo">
        <%=StringUtils.toStringJSP(PenaComplessiva.getDescrTipoPenaDetentiva())%>
      </font>
    </td>
    <td class="l">Data Inizio : <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy"))%> </font></td>
    <td class="l">Data Fine : <font class="campo">MAI</font></td>
  </tr>
</table>
<%
}
else if (!PenRes1.getErrorMsg().equals("-"))
{
%>
<table style="width: 95%;">
  <tr>
  <% if (PenRes1.getErrorMsg().endsWith("COMPLESSIVA")) { %>
    <td class=Titolonocap colspan=6 width=80%> Pena complessiva </td>
  <% } else { %>
    <td class=Titolonocap colspan=6 width=80%> Pena residua da espiare </td>
  <% } %>
  </tr>
  
  <%
  if (PenRes1.getErrorMsg().startsWith("Libero")) //Patch per gestire il titolo
  {
%>
  <tr>
    <td class="l"> Reclusione :
      Anni <font class=campo><%=PenRes1.getNumAnni()%></font>
      Mesi <font class=campo><%=PenRes1.getNumMesi()%></font>
      Giorni <font class=campo><%=PenRes1.getNumGiorni()%></font>
      Multa <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
    </td>
    <td class="l"> Arresto :
      Anni <font class=campo><%=PenRes2.getNumAnni()%></font>
      Mesi <font class=campo><%=PenRes2.getNumMesi()%></font>
      Giorni <font class=campo><%=PenRes2.getNumGiorni()%></font>
      Ammenda <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
    </td>
  </tr>
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>  
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>  
<%
  }
  else //non libero
  { // pena in espiazione ??? calcolo al volo la pena residua come intervallo
    // tra date
    //==========================================================================
    // Solo se la pena è in espiazione calcolo il quantum residuo, altrimenti
    // visualizzo solo decorrenza-scadenza.
    // n.b. la pena non viene considerata in espiazione se:
    //      1 - data inizio >= sysdate (pene con decorrenza futura)
    //      2 - data fine < sysdate (in questo caso il condannato viene considerato scarcerato)
    //     data inizio <= sysdate <data fine
    %>
    <!--tr-->
    <%
    if (   PenRes1.getDataFine()!=null
        && !DateUtils.isLower(DateUtils.getSysDate(), PenRes1.getDataInizio() ) 
        && DateUtils.isLower( DateUtils.getSysDate(),PenRes1.getDataFine() ) 
       )
    {
      //CalendarUtil lCU = new CalendarUtil();
      //PenRes2.setDataFine(PenRes1.getDataFine());
      //PenRes2.setDataInizio(DateUtils.getSysDate());
      //PenRes2=lCU.ricalcolaGAM(lCU.CalcolaNumGiorniMesiAnni(PenRes2, true));
    %>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--td class="l">
      Anni : <font class=campo><%=PenRes2.getNumAnni()%> </font>
      Mesi : <font class=campo><%=PenRes2.getNumMesi()%> </font>
      Giorni : <font class=campo><%=PenRes2.getNumGiorni()%></font>
    </td--%>
    <%
    }
    %>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--td class=lNoBord>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td class=l>Data Inizio : <font class=campo><%=DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy")%> </font></td>
    <td class=lNoBord>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td class=l>Data Fine : <font class=campo><%=DateUtils.getDateToString(PenRes1.getDataFine(),"dd/MM/yyyy") %> </font></td--%>
  <!--/tr-->
    <tr>
      <td class="l"> Reclusione :
         Anni <font class="campo"><%=PenRes1.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes1.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes1.getNumGiorni()%></font>
         Multa <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
      </td>
      <td class="l"> Arresto :
         Anni <font class="campo"><%=PenRes2.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes2.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes2.getNumGiorni()%></font>
         Ammenda <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Data Inizio : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(), "dd/MM/yyyy"))%></font></td>
      <td class="l">Data Fine : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataFine(), "dd/MM/yyyy"))%></font></td>
    </tr>
  
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>  
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>  
<%
  }
%>
</table>
<%
  }
%>

<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
  <table>
    <tr><td colspan=19 class=Titolonocap>Pena complessiva in sentenza</td></tr>
    <tr>
      <td class="l"><font class="label">Reclusione: </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessiva.getNumAnniReclusione(),"0")%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessiva.getNumMesiReclusione(),"0")%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessiva.getNumGiorniReclusione(),"0")%></font></td>
      <td class="l"><font class="label">Multa</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaComplessiva.getImportoMulta())%></font></td>
      <td>&nbsp;</td>
      <td class="l"><font  class="label">Arresto: </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessiva.getNumAnniArresto(),"0")%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessiva.getNumMesiArresto(),"0")%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessiva.getNumGiorniArresto(),"0")%></font></td>
      <td class="l"><font class="label">Ammenda</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaComplessiva.getImportoAmmenda())%></font></td>
    </tr>
  </table>
--%>

<%
// Presenza di un'Ordinanza emessa dal GE
if (lFlagOrdinanzaGE) {
%>  
<br>
    <jsp:include page="/jsp/files/siap/siep/calcolopena/DettaglioDecisioneGE.jsp"/>
  <br>
<% }else {

//====================================================================================
// SEZIONE CON I CAMPI DI IMPUT DEI DATI DELLA DECISIONE DEL GIUDICE DELL' ESECUZIONE
// se provengo da 'aggiungi' i campi vengono precaricati con i dati precedentemente
// inseriti non modificabili
//====================================================================================
%>
<br>
<table style="width: 95%;">
  <tr><td colspan=8 class="Titolonocap">Decisione del Giudice dell' Esecuzione</td></tr>
  <tr>
    <td class="l" nowrap>Computo Beneficio <font class="ob">(*)</font> :&nbsp;</td>
    <td class="l"><select name="tipoannotazione"><%= TipoAnnotazioneManuale %></select></td>
    <td class="l" nowrap>DPR <font class=ob>(*)</font> :&nbsp;<select name=dpr><%=listaDPR%></select></td>
  </tr>
<%
  //i dati dell'ordinanza dovranno essere prelevati se esistenti da una tabella x
%>
  <% 
  //======================================================================
  // se non provengo da aggiungi bisogna inserire i campi dell'ordinanza
  //======================================================================
  if (!lOrdGEPresente) 
  {  %>
  <tr>
    <td class="l">Ordinanza <font class="ob">(*)</font> :</td>
    <td class="l">
      Anno/Numero
      <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>" size=4 maxlength=4 value="<%=StringUtils.toStringJSP( OrdinanzaGEAnn.getChiaveAnnoSige() )%>" <%=IWebConstants.UTIL_DATA_ANNO%> >
      /
      <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE%>" size=8 maxlength=38 value="<%=StringUtils.toStringJSP( OrdinanzaGEAnn.getChiaveNumeroSige() )%>" onkeypress="return TicTabNumField(this,event)" >
      Procedimento SIGE
    </td>
    <td class="l">
      <font class="label">in data </font>
      &nbsp;&nbsp;
        <input title = "Giorno di arrivo documento" type="text" name="DaGiArr" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "dd"))%>" <%=IWebConstants.UTIL_DATA%>>
        /
        <input title = "Mese di arrivo documento" type="text" name="DaMeArr" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "MM"))%>" <%=IWebConstants.UTIL_DATA%>>
        /
        <input title = "Anno di arrivo documento" type="text" name="DaAnArr" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "yyyy"))%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>

  <input type="hidden" name="annoGe" value="<%=StringUtils.toStringJSP( OrdinanzaGEAnn.getAnnoGe() )%>">
  <input type="hidden" name="numeroGe" value="<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumeroGe())%>">

  <tr>
    <td class="l">Ufficio  <font class="ob">(*)</font> :</td>
    <td class="l" colspan=2>
        <select Title="Ufficio Emittente" name="CodTipoUffEmi">
          <%=UfficioEmittente%>
        </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede  <font class="ob">(*)</font> :</td>
    <td class="l" colspan=2>
        <font class="campo">
          <input Title="Luogo Ufficio Emittente" name="<%= ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrLuogoEmittente())%>" size=35 type="text">
          <a href="Javascript:ListaUfficiComuni('f','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>',document.f.CodTipoUffEmi[document.f.CodTipoUffEmi.options.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </font>
    </td>
  </tr>
  <tr>
    <td class="l">Motivazioni :</td>
    <td class="l" colspan=3>
        <textarea cols="58" rows="2" name="<%=ICostantiAnnotazioneManuale.CAMPO_MOTIVAZIONI%>"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getMotivazioni())%></textarea>
    </td>
  </tr>
  <% 
  } 
  else 
  { 
  //============================================================================
  // Provengo da 'Aggiungi' i dati dell'ordinanza vengono visualizzati come 
  // campi fissi
  // i dati vengono recuperati da.......
  //============================================================================
  %>
  <tr>
    <td class="l">Declaratoria <font class="ob">(*)</font> :</td>
    <td class="l">
    &nbsp;
    Anno/Numero Ordinanza:
        <font class="campo">
          <%=StringUtils.toStringJSP( OrdinanzaGEAnn.getAnnoGe() )%> / <%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumeroGe())%>
        </font>
    </td>
    <td class="l">
      <font class="label">in data </font>
      &nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "dd-MM-yyyy"))%>&nbsp;
        </font>
    </td>
  </tr>
<%if(ProcedimentoGE!=null && ProcedimentoGE.getIdFascicoloSige()!=null)
  {  %>
    <tr>
    <td>&nbsp;&nbsp;&nbsp;</td>    
    <td class="l">
    &nbsp;
    Anno/Numero Procedimento SIGE:
      <font class="campo">
      <%=StringUtils.toStringJSP( ProcedimentoGE.getChiaveAnno() )%> / <%=StringUtils.toStringJSP(ProcedimentoGE.getChiaveProgr())%>
      </font>
    </td>
    </tr>
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE %>" value="<%=StringUtils.toStringJSP(ProcedimentoGE.getChiaveAnno() )%>">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE %>" value="<%=StringUtils.toStringJSP(ProcedimentoGE.getChiaveProgr())%>">
<%} %>   
  <tr>
    <td class="l">Ufficio  <font class="ob">(*)</font> :</td>
    <td class="l" colspan=2>
        <font class="campo">
          <%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrUfficioEmittente())%>&nbsp;
        </font>
    </td>
  </tr>
  <tr>
    <td class="l">Sede  <font class="ob">(*)</font> :</td>
    <td class="l" colspan=2>
        <font class="campo">
          <%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrLuogoEmittente())%>&nbsp;
        </font>
    </td>
  </tr>
  <tr>
    <td class="l">Motivazioni :</td>
    <td class="l" colspan=3>
        <font class="campo">
          <%=StringUtils.toStringJSP(OrdinanzaGEAnn.getMotivazioni())%>&nbsp;
        </font>
    </td>
  </tr>
  
  <input type="HIDDEN" name="IdAnnGE" value="<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getIdAnnotazioneManuale())%>">
  <input type="HIDDEN" name="annoGE" value="<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getAnnoGe())%>">
  <input type="HIDDEN" name="numGE" value="<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumeroGe())%>">
  <input type="HIDDEN" name="DaAnArr" value="<%=DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "yyyy")%>">
  <input type="HIDDEN" name="DaGiArr" value="<%=DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "dd")%>">
  <input type="HIDDEN" name="DaMeArr" value="<%=DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "MM")%>">
  
  <% } %>

  
  <tr>
    <td class="l" colspan=3>
      <input type="radio" name="TipoOrd" value="SenzaRichiesta" checked>senza richiesta &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="Conforme" onclick="Javascript:isConforme();">in conformita' alla richiesta del PM &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="Difforme">in difformita' alla richiesta del PM
    </td>
  </tr>
  <tr>
    <td class="l" colspan=3>
      <input type="radio" name="TipoOrd" value="Rigetta"       onclick="Javascript:isRigetto();">rigetta &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="Inammissibile" onclick="Javascript:isInammissibile();">dichiara inammissibile &nbsp;&nbsp;
      
    <!--   <input type="radio" name="TipoOrd" value="Riunisce">riunisce &nbsp;&nbsp;  --> 
    </td>
  </tr>
  <tr>
  <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_TIPO_RICHIE%>" value="<%=Richie%>">
  </tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
  <tr>
    <td class="Titolo">Fonte</td>
    <td class="Titolo">Anno</td>
    <td class="Titolo">Numero</td>
    <td class="Titolo">Articolo</td>
    <td class="Titolo">Art.qualificante</td>
    <td class="Titolo">Comma</td>
    <td class="Titolo">Lettera</td>
    <td class="Titolo">Numero</td>
  </tr>
  <tr>
    <td class="c">
      <select name="<%= ICostantiReato.CAMPO_COD_FONTE %>">
        <%=TipiFontiReato%>
      </select>
    </td>
    <td class="c">
      <input size=4 maxlength=4 title="Anno Fonte" value="" type="text" name="<%=ICostantiReato.CAMPO_ANNO_FONTE %>">
    </td>
    <td class="c">
      <input size=6 maxlength=6 title="Numero Fonte" value="" type="text" name="<%= ICostantiReato.CAMPO_NUMERO_FONTE %>">
    </td>
    <td class="c">
      <input size=5 maxlength=5 title="Articolo Fonte" value="" type="text" name="<%= ICostantiReato.CAMPO_ARTICOLO %>">
    </td>
    <td class="c">
      <select name="<%= ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE %>">
        <%=TipiSottonumerazione %>
      </select>
    </td>
    <td class="c">
      <strong>C</strong>
      <input size=10 maxlength=10 title="Comma" value="" type="text" name="<%= ICostantiReato.CAMPO_COMMA %>">
    </td>
    <td class="c">
      <strong>L</strong>
      <input size=2 maxlength=2 title="Lettera" value="" type="text" name="<%= ICostantiReato.CAMPO_LETTERA %>">
    </td>
    <td class="c">
     <strong>N</strong>
     <input size=2 maxlength=2 title="Numero" value="" type="text" name="<%= ICostantiReato.CAMPO_NUMERO %>">
    </td>
  </tr>
--%>
</table>

<%
//==============================================================================
//                    Sezione con i quantum da imputare
//==============================================================================
%>

<table width="97%">
  <tr><td colspan=6><hr width="100%"></td></tr>
  <tr>
    <td valign="middle" class=c rowspan=3>+/- <font class="ob">(*)</font><br>
      <select name="PM">
        <option value=""></option>
        <option value="+">+</option>
        <option value="-">-</option>
      </select>
  </td>
  <td class=titolo colspan=2>Reclusione</td>
  <td width=25>&nbsp;</td>
  <td class="titolo" colspan=2>Arresto</td>
  </tr>
  <tr>
      <td class="c">
        <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Giorni</font><br>
        <input type="text" name="ARec" maxlength="2" size="2" value="">&nbsp;
        <input type="text" name="MRec" maxlength="2" size="2" value="">&nbsp;
        <input type="text" name="GRec" maxlength="2" size="2" value="">
      </td>
      <td class="c">
        <font class="label">Multa</font><br>
        <input style="text-align:right" type="text" name="Multa" maxlength="8" size="6" value="">
        ,
        <input style="text-align:left" type="text" name="Mul_dec" maxlength="2" size="2" value="">
      </td>
      <td width=25>&nbsp;</td>
      <td class="c">
        <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Giorni</font><br>
        <input type="text" name="AArr" maxlength="2" size="2" value="">&nbsp;
        <input type="text" name="MArr" maxlength="2" size="2" value="">&nbsp;
        <input type="text" name="GArr" maxlength="2" size="2" value="">
      </td>
      <td class="c">
        <font class="label">Ammenda</font><br>
        <input style="text-align:right" type="text" name="Ammenda" maxlength="8" size="6" value="">
        ,
        <input style="text-align:left" type="text" name="Amm_dec" maxlength="2" size="2" value="">
      </td>
    </tr>
    <tr>
      <td class="c" colspan=5><font class="label" style="vertical-align: top;">Note</font>
      <textarea cols="60" rows="2" name="noteRec"></textarea></td>
      <td width=20>&nbsp;</td>
<!--
      <td class=l colspan=2>
      <font class="label" style="vertical-align: top;">Note</font>
      <textarea cols="30" rows="2" name="noteArr"></textarea>
      </td>
-->
      </tr>
  </table>
  
<%} // endif (lFlagOrdinanzaGE) { %>

  <table>
    <tr>
      <td class="lNoBord" colspan="2">
        <INPUT class="bottone" type="submit" value="Conferma">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
</form>
<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("f");
<%
if (!lFlagOrdinanzaGE) {
%>
  // Controllo campi Anno e Numero Declaratoria
  // MEV29 - Anno e numero ordinanza non vengono più riportati. Sostituiti da anno e numero procedimento SIGE
  //frmvalidator.addValidation("annoGe","numeric","Il campo Anno Ordinanza può contenere solo caratteri numerici");
  //frmvalidator.addValidation("annoGe","maxlen=4","La lunghezza massima per l'Anno Ordinanza è di 4 caratteri");
  //frmvalidator.addValidation("annoGe","minlen=4","La lunghezza minima per l'Anno Ordinanza è di 4 caratteri");

  //frmvalidator.addValidation("numeroGe","numeric","Il campo Numero Ordinanza può contenere solo caratteri numerici");
  //frmvalidator.addValidation("numeroGe","maxlen=13","La lunghezza massima per il Numero Ordinanza è di 13 caratteri");

  // Controllo su Anno Numero Procedimento SIGE
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>","numeric","Il campo Anno Procedimento SIGE può contenere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento SIGE è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>","minlen=4","La lunghezza minima per l'Anno Procedimento SIGE è di 4 caratteri");

  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE%>","numeric","Il campo Numero Procedimento SIGE può contenere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE%>","maxlen=38","La lunghezza massima per il Numero Procedimento SIGE è di 38 caratteri");
  
  // Controllo Data di Arrivo Documento
  frmvalidator.addValidation("DaGiArr","numeric");
  frmvalidator.addValidation("DaGiArr","gt=1");
  frmvalidator.addValidation("DaGiArr","lt=31");

  frmvalidator.addValidation("DaMeArr","numeric");
  frmvalidator.addValidation("DaMeArr","gt=1");
  frmvalidator.addValidation("DaMeArr","lt=12");

  frmvalidator.addValidation("DaAnArr","numeric");
  frmvalidator.addValidation("DaAnArr","gt=1900");

  // Controllo campi Reclusione
  frmvalidator.addValidation("ARec","numeric","Il campo Anni Reclusione può contenere solo caratteri numerici");
  frmvalidator.addValidation("MRec","numeric","Il campo Mesi Reclusione può contenere solo caratteri numerici");
  frmvalidator.addValidation("GRec","numeric","Il campo Giorni Reclusione può contenere solo caratteri numerici");
  frmvalidator.addValidation("Multa","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");
  frmvalidator.addValidation("Mul_dec","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");

  // Controllo campi Arresto
  frmvalidator.addValidation("AArr","numeric","Il campo Anni Arresto può contenere solo caratteri numerici");
  frmvalidator.addValidation("MArr","numeric","Il campo Mesi Arresto può contenere solo caratteri numerici");
  frmvalidator.addValidation("GArr","numeric","Il campo Giorni Arresto può contenere solo caratteri numerici");
  frmvalidator.addValidation("Ammenda","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");
  frmvalidator.addValidation("Amm_dec","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");

  frmvalidator.setAddnlValidationFunction("Verify_Quantum");
<% } %>
</script>

</body>
</html>