<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>

<jsp:useBean id="contenuto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"    scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"  scope="request" class="java.util.Date"/>
<jsp:useBean id="reclamo"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="revoca"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="decreto"     		scope="request" class="java.lang.String"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
%>

<%
  	String lAction = new String();

	if(decreto.compareTo("SI")==0)
		lAction = "siap.sius.depositodecreto.action.ActInserisciDecretoRevocaLA";
	else
		lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaRevocaLA";

  int NumColonne = ICostantiLibertaAnticipata.NUM_COLONNE_SEMESTRI;
  int NumRighe = ICostantiLibertaAnticipata.NUM_RIGHE_SEMESTRI;
  int NumTotale = ICostantiLibertaAnticipata.NUM_TOTALE_SEMESTRI;
  int NumDate = ICostantiLibertaAnticipata.NUM_PERIODI;

  int NumTotaleSemestri = NumRighe*NumColonne;
  int NumCheck = 4 + NumTotaleSemestri;         /* numero complessivo dei check box */

  int IndPer =  NumTotaleSemestri;     /* indice del check box relativo al Periodo unico */
  int IndRig = 1 + NumTotaleSemestri;  /* indice del check box relativo a Periodi rigettati */
  int IndIna = 2 + NumTotaleSemestri;  /* indice del check box relativo a Periodi inammissibili */
  int IndNlp = 3 + NumTotaleSemestri;  /* indice del check box relativo a Periodi NLP */
  
%>
<!-- 	InserisciordinanzaRevocaLA	 -->
<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza di Revoca Liberazione Anticipata</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

    <script language="JavaScript">

   var NumRighe = <%=NumRighe%>;             /* numero di righe gruppo semestri */
   var NumColonne = <%=NumColonne%>;         /* numero di colonne gruppo semestri */
   var NumTotale = <%=NumTotaleSemestri%>;   /* numero complessivo semestri  */
   var NumDate = <%=NumDate%>;               /* numero totale gruppo di date  */
   var giorni = new Array(NumTotale);        /* Array dei giorni totali concessi per L.A. NORMALE  */
   var giorni_spe = new Array(NumTotale);	 /* 				totali concessi per L.A. SPECIALE	*/
   var giorni_int = new Array(NumTotale);	 /* 				totali concessi per INTEGRAZIONE L.A.	*/
   var NumCheck = <%=NumCheck%>;             /* numero complessivo dei check box */
   var IndPer = <%=IndPer%>;
   var IndRig = <%=IndRig%>;
   var IndIna = <%=IndIna%>;
   var IndNlp = <%=IndNlp%>;
   var node;
   var GiorniConcessi = 0;
   var GiorniConcessi_spe = 0;
   var GiorniConcessi_int = 0;
   var SalvaGiorniConcessi = 0;
   var SalvaGiorniConcessi_spe = 0;
   var SalvaGiorniConcessi_int = 0;
   
   var modalita = 'S' ;            /* modalità di scelta. S : Semestr C: periodo Complessivo */
   var flagConcesso = 'C';
   var flagConcesso_spe = 'C';
   var flagConcesso_int = 'C';

   function init()
   {
      //alert("init ");
	      var i=0;
	      for (i=0; i<NumTotale; i++)
	          giorni[i] = 0;
	      for (i=0; i<NumTotale; i++)
	          giorni_spe[i] = 0;
	      for (i=0; i<NumTotale; i++)
	          giorni_int[i] = 0;
	      for (i=0; i<NumCheck; i++)
	           uncheckDate(i);
	      
	      document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value = 0;
	      document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = true;
	      
	      document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value = 0;
	      document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.readOnly = true;
	
	      document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value = 0;
	      document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.readOnly = true;
	
	      DisabilitaLA();
	      DisabilitaLA_SPE();
	      DisabilitaLA_INT();
	      
	      document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO%>.value = "C";
	      document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO%>.value = "C";
	      document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO%>.value = "C";
   }


    // Chiusura dell'eventuale blocco aperto
    function Chiusura()
    {
    	//alert("chiusura - inizio");
	      var retValue = true;
	      for (var i=0; i < NumCheck; i++)
	      {
		        if (document.InsOrdRevocaLA.gg[i].checked)
		        {
		              document.InsOrdRevocaLA.gg[i].checked = false;
		              retValue = ViewLayer(i);
		        }
		        
		        if (document.InsOrdRevocaLA.gg_SPE[i].checked)
		        {
		              document.InsOrdRevocaLA.gg_SPE[i].checked = false;
		              retValue = ViewLayer_SPE(i);
		        }
		        
		        if (document.InsOrdRevocaLA.gg_INT[i].checked)
		        {
		              document.InsOrdRevocaLA.gg_INT[i].checked = false;
		              retValue = ViewLayer_INT(i);
		        }
	      }
	   //   alert("hiusura - fine");
	      return retValue;
    }

// -----------------------------------------------------------------------    
    
    // Disabilita i blocchi date vuoti e quelli
    //della modalità non selezionata (Semestri/Periodo)
    function DisabilitaDate()
    {
      //   alert("DisabilitaDate: inizio");
        var i=0;
        for (i=0; i < NumCheck; i++)
        {
         // Vengono disabilitati tutti i blocchi periodi vuoti
	          if(!IsCheckedDate(i))
	          {
	             node=document.getElementById('L'+i);
	             node.disabled = true;
	 
	          }
        }
        
      if(!document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[0].checked)
      {
        for (i=0; i < NumTotale; i++)
        {
             node=document.getElementById('L'+i);
             node.disabled = true;

        }
      }
      else
      {
             node=document.getElementById('L'+ IndPer);
             node.disabled = true;

      }

      return;
   
    }	// chiude DisabilitaDate
    

// ------  >  L.A. SPCIALE    
    // Disabilita i blocchi date vuoti e quelli
    //della modalità non selezionata (Semestri/Periodo)
    function DisabilitaDate_SPE()
    {
     //   alert("DisabilitaDate_SPE: inizio");
        var i=0;
        for (i=0; i < NumCheck; i++)
        {
	         // Vengono disabilitati tutti i blocchi periodi vuoti
	          if(!IsCheckedDate_spe(i))
	          {
	             node=document.getElementById('L_SPE'+i);
	             node.disabled = true;
	          }
        }
        
      if(!document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[0].checked)
      {
        for (i=0; i < NumTotale; i++)
        {
             node=document.getElementById('L_SPE'+i);
             node.disabled = true;

        }
      }
      else
      {
             node=document.getElementById('L_SPE'+ IndPer);
             node.disabled = true;

      }

      return;
   
    }	// chiude DisabilitaDate_SPE
    
// -----------------------------------------------------------------------------------
// ------  >  L.A. INTEGRAZIONE   
    // Disabilita i blocchi date vuoti e quelli
    //della modalità non selezionata (Semestri/Periodo)
    function DisabilitaDate_INT()
    {
      //  alert("DisabilitaDate_INT: inizio");
        var i=0;
        for (i=0; i < NumCheck; i++)
        {
         // Vengono disabilitati tutti i blocchi periodi vuoti
	          if(!IsCheckedDate_int(i))
	          {

	             node=document.getElementById('L_INT'+i);
	             node.disabled = true;

	          }
        }
        
	    if(!document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>[0].checked)
	    {
	        for (i=0; i < NumTotale; i++)
	        {
	             node=document.getElementById('L_INT'+i);
	             node.disabled = true;

	        }
	    }
	    else
	    {
	             node=document.getElementById('L_INT'+ IndPer);
	             node.disabled = true;
	
	    }
	
	      return;
   
    }	// chiude DisabilitaDate_INT
    
// -----------------------------------------------------------------------------------

// L.A. SPECIALE     
   var DataFine;
   var DataIni;
   /* Controllo  e conteggio date */
   function conteggioDate_spe (id)
   {
       var retValue = true;
       var elem = 0;
       var periodo = 0;
       var data;

       if (id < NumTotale)
          giorni_spe[id] = 0;
       elem=id*NumDate;
       for (var j=0; j<NumDate; j++, elem++)
       {
         DataFine = null;
         DataIni = null;

         if (leggiDate_spe(elem) == false)
         {
            //alert ("Errore nelle date");
            retValue = false;
            periodo = 0;
            break;
         }
         else
         {
            // Solo sugli elementi Periodi Concessi
            if (id < NumTotale)
            {
               if (DataFine != null && DataIni != null)
               { // + 1 ?????
                  periodo = periodo + 1 + Math.floor((DataFine.getTime() - DataIni.getTime())/(1000*60*60*24));
               }
            }
         }
       }

       if (periodo != 0)
       {
          if (periodo != 180 )
          {
             retValue = confirm("Le date inserite individuano un periodo di " + periodo +" giorni e non di 180. Confermi comunque la concessione del semestre?");
          }
          if (retValue)
              giorni_spe[id] = 75;
       }

       return retValue;
   }

/* Lettura  e controllo del periodo di posizione elem  */
   function leggiDate_spe (elem)
   {
      var ret = true;
      var gg0 = FillDM(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[elem].value);
      var mm0 = FillDM(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE %>[elem].value);
      var aa0 = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE %>[elem].value;
      var dataIni = gg0 + "/" + mm0 + "/" + aa0;
      var gg1 = FillDM(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_SPE %>[elem].value);
      var mm1 = FillDM(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_SPE %>[elem].value);
      var aa1 = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_SPE %>[elem].value;
      var dataFine = gg1 + "/" + mm1 + "/" + aa1;

      var data_emissione = '<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>';

      if (dataIni.length == 2)
      {
         if (dataFine.length != 2)
         {
                 ret = false;
                 alert ("Data di inizio periodo mancante");
         }
      }
      else if (dataFine.length == 2)
      {
                 ret = false;
                 alert ("Data di fine periodo mancante");
      }
      else if (ControllaData (dataIni) == false)
      {
             /* entrambe le date valorizzate */
                 ret = false;
                 alert ("Errore nella data : " + dataIni);
      }
      else if (ControllaData (dataFine) == false)
      {
                 ret = false;
                 alert ("Errore nella data : " + dataFine);
      }
      else if (CompareDate(dataIni,dataFine)== false)
      {
                 ret = false;
                 alert ("Data di Fine minore di  Data inizio periodo");
      }
      else if (CompareDate(dataFine, data_emissione)== false)
      {
                 ret = false;
                 alert ("Data di Fine maggiore di Data emissione");
      }
      else
      {

              DataIni = new Date(aa0, mm0-1, gg0);
              DataFine  = new Date(aa1, mm1-1, gg1);
      }
     return ret;
   }

// ----------------------------------------------------------------------

// L.A. INTEGRAZIONE     
   var DataFine;
   var DataIni;
   /* Controllo  e conteggio date */
   function conteggioDate_int (id)
   {
       var retValue = true;
       var elem = 0;
       var periodo = 0;
       var data;

       if (id < NumTotale)
          giorni_int[id] = 0;
       elem=id*NumDate;
       for (var j=0; j<NumDate; j++, elem++)
       {
         DataFine = null;
         DataIni = null;

         if (leggiDate_int(elem) == false)
         {
            //alert ("Errore nelle date int");
            retValue = false;
            periodo = 0;
            break;
         }
         else
         {
            // Solo sugli elementi Periodi Concessi
            if (id < NumTotale)
            {
               if (DataFine != null && DataIni != null)
               { // + 1 ?????
                  periodo = periodo + 1 + Math.floor((DataFine.getTime() - DataIni.getTime())/(1000*60*60*24));
               }
            }
         }
       }

       if (periodo != 0)
       {
          if (periodo != 180 )
          {
             retValue = confirm("Le date inserite individuano un periodo di " + periodo +" giorni e non di 180. Confermi comunque la concessione del semestre?");
          }
          if (retValue)
              giorni_int[id] = 30;
       }

       return retValue;
   }

/* Lettura  e controllo del periodo di posizione elem  */
   function leggiDate_int (elem)
   {
      var ret = true;
      var gg0 = FillDM(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[elem].value);
      var mm0 = FillDM(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT %>[elem].value);
      var aa0 = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT %>[elem].value;
      var dataIni = gg0 + "/" + mm0 + "/" + aa0;
      var gg1 = FillDM(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_INT %>[elem].value);
      var mm1 = FillDM(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_INT %>[elem].value);
      var aa1 = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_INT %>[elem].value;
      var dataFine = gg1 + "/" + mm1 + "/" + aa1;

      var data_emissione = '<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>';

      if (dataIni.length == 2)
      {
         if (dataFine.length != 2)
         {
                 ret = false;
                 alert ("Data di inizio periodo mancante");
         }
      }
      else if (dataFine.length == 2)
      {
                 ret = false;
                 alert ("Data di fine periodo mancante");
      }
      else if (ControllaData (dataIni) == false)
      {
             /* entrambe le date valorizzate */
                 ret = false;
                 alert ("Errore nella data : " + dataIni);
      }
      else if (ControllaData (dataFine) == false)
      {
                 ret = false;
                 alert ("Errore nella data : " + dataFine);
      }
      else if (CompareDate(dataIni,dataFine)== false)
      {
                 ret = false;
                 alert ("Data di Fine minore di  Data inizio periodo");
      }
      else if (CompareDate(dataFine, data_emissione)== false)
      {
                 ret = false;
                 alert ("Data di Fine maggiore di Data emissione");
      }
      else
      {
              DataIni = new Date(aa0, mm0-1, gg0);
              DataFine  = new Date(aa1, mm1-1, gg1);

      }
     return ret;
   }

// ----------------------------------------------------------------------

// L.A.NORMALE
	var DataFine;
   var DataIni;
   /* Controllo  e conteggio date */
   function conteggioDate (id)
   {
       var retValue = true;
       var elem = 0;
       var periodo = 0;
       var data;

       if (id < NumTotale)
          giorni[id] = 0;
       elem=id*NumDate;
       for (var j=0; j<NumDate; j++, elem++)
       {
         DataFine = null;
         DataIni = null;

         if (leggiDate(elem) == false)
         {
            //alert ("Errore nelle date");
            retValue = false;
            periodo = 0;
            break;
         }
         else
         {
            // Solo sugli elementi Periodi Concessi
            if (id < NumTotale)
            {
               if (DataFine != null && DataIni != null)
               { // + 1 ?????
                  periodo = periodo + 1 + Math.floor((DataFine.getTime() - DataIni.getTime())/(1000*60*60*24));
               }
            }
         }
       }

       if (periodo != 0)
       {
          if (periodo != 180 )
          {
             retValue = confirm("Le date inserite individuano un periodo di " + periodo +" giorni e non di 180. Confermi comunque la concessione del semestre?");
          }
          if (retValue)
              giorni[id] = 45;
       }

       return retValue;
   }

/* Lettura  e controllo del periodo di posizione elem  */
   function leggiDate (elem)
   {
      var ret = true;
      var gg0 = FillDM(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value);
      var mm0 = FillDM(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value);
      var aa0 = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value;
      var dataIni = gg0 + "/" + mm0 + "/" + aa0;
      var gg1 = FillDM(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value);
      var mm1 = FillDM(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value);
      var aa1 = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value;
      var dataFine = gg1 + "/" + mm1 + "/" + aa1;

      var data_emissione = '<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>';

      if (dataIni.length == 2)
      {
         if (dataFine.length != 2)
         {
                 ret = false;
                 alert ("Data di inizio periodo mancante");
         }
      }
      else if (dataFine.length == 2)
      {
                 ret = false;
                 alert ("Data di fine periodo mancante");
      }
      else if (ControllaData (dataIni) == false)
      {
             /* entrambe le date valorizzate */
                 ret = false;
                 alert ("Errore nella data : " + dataIni);
      }
      else if (ControllaData (dataFine) == false)
      {
                 ret = false;
                 alert ("Errore nella data : " + dataFine);
      }
      else if (CompareDate(dataIni,dataFine)== false)
      {
                 ret = false;
                 alert ("Data di Fine minore di  Data inizio periodo");
      }
      else if (CompareDate(dataFine, data_emissione)== false)
      {
                 ret = false;
                 alert ("Data di Fine maggiore di Data emissione");
      }
      else
      {
              DataIni = new Date(aa0, mm0-1, gg0);
              DataFine  = new Date(aa1, mm1-1, gg1);

      }
     return ret;
   }

// ----------------------------------------------------------------------

    function aggiornaTotGiorni()
    {
	      //alert("aggiornaTotGiorni(): inizio");
	      if (modalita == 'S')
	      {
	        	var totale = 0;
	        	for (var i=0; i<NumTotale; i++)
	          		totale += giorni[i];

	        	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value = totale;
	      }

   }
 
    // L.A. SPECIALE
     function aggiornaTotGiorni_SPE()
     {
	    //  alert("aggiornaTotGiorniSpe(): inizio");
	      if (modalita == 'S')
	      {
		        var totale = 0;
		        for (var i=0; i<NumTotale; i++)
		        {	
		          	totale += giorni_spe[i];

		        } 

		        document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value = totale;
	      }

    }

    // L.A. INTEGRAZIONE
    function aggiornaTotGiorni_INT()
    {
	      //alert("aggiornaTotGiorniINT(): inizio");
	      if (modalita == 'S')
	      {
		        var totale = 0;
		        for (var i=0; i<NumTotale; i++)
		          totale += giorni_int[i];

		        document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value = totale;
	      }
   }
    
//	----------------------------------------------------------------------

    function checkDate(id)
    {
       //alert("check id : " + id);
	      if (id < NumTotale) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI%>[id].checked = true;
	      } else if (id == IndPer) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO%>.checked = true;
	      } else if (id == IndRig) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI%>.checked = true;
	      } else if (id == IndIna) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI%>.checked = true;
	      } else if (id == IndNlp) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP%>.checked = true;
	      }

    }

    function uncheckDate(id)
    {
       //alert ("uncheck " + id);

	      if (id < NumTotale) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI%>[id].checked = false;
	      } else if (id == IndPer) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO%>.checked = false;
	      } else if (id == IndRig) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI%>.checked = false;
	      } else if (id == IndIna) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI%>.checked = false;
	      } else if (id == IndNlp) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP%>.checked = false;
	      }

}

    function IsCheckedDate(id)
    {
        var retValue = false;
       //alert ("IsCheckedDate " + id);

	      if (id < NumTotale) {
	       if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI%>[id].checked)
	          retValue = true;
	      }else if (id == IndPer) {
	        if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO%>.checked)
	          retValue = true;
	      }else if (id == IndRig) {
	        if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI%>.checked)
	          retValue = true;
	      }else if (id == IndIna) {
	        if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI%>.checked)
	          retValue = true;
	      }else if (id == IndNlp) {
	        if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP%>.checked)
	          retValue = true;
	      }

      return retValue;
    }
//
//------------------------------------------------
//	L.A. SPECIALE
//
	function checkDate_spe(id)
    {

	      if (id < NumTotale) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE%>[id].checked = true;
	      } else if (id == IndPer) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE%>.checked = true;
	      } else if (id == IndRig) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE%>.checked = true;
	      } else if (id == IndIna) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE%>.checked = true;
	      } else if (id == IndNlp) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE%>.checked = true;
	      }

    }

    function uncheckDate_spe(id)
    {

      if (id < NumTotale) {
      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE%>[id].checked = false;
      } else if (id == IndPer) {
      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE%>.checked = false;
      } else if (id == IndRig) {
      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE%>.checked = false;
      } else if (id == IndIna) {
      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE%>.checked = false;
      } else if (id == IndNlp) {
      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE%>.checked = false;
      }

}

    function IsCheckedDate_spe(id)
    {
        var retValue = false;
     //  alert ("IsCheckedDate_SPE " + id);

	      if (id < NumTotale) {
	       if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE%>[id].checked)
	          retValue = true;
	      }else if (id == IndPer) {
	        if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE%>.checked)
	          retValue = true;
	      }else if (id == IndRig) {
	        if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE%>.checked)
	          retValue = true;
	      }else if (id == IndIna) {
	        if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE%>.checked)
	          retValue = true;
	      }else if (id == IndNlp) {
	        if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE%>.checked)
	          retValue = true;
	      }

      return retValue;
    }
//-------------------------------------------------------------------------------------------------
//	L.A. INTEGRAZIONE
//
	function checkDate_int(id)
    {
	      if (id < NumTotale) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT%>[id].checked = true;
	      } else if (id == IndPer) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT%>.checked = true;
	      } else if (id == IndRig) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT%>.checked = true;
	      } else if (id == IndIna) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT%>.checked = true;
	      } else if (id == IndNlp) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT%>.checked = true;
	      }
    }

    function uncheckDate_int(id)
    {
	      if (id < NumTotale) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT%>[id].checked = false;
	      } else if (id == IndPer) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT%>.checked = false;
	      } else if (id == IndRig) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT%>.checked = false;
	      } else if (id == IndIna) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT%>.checked = false;
	      } else if (id == IndNlp) {
	      document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT%>.checked = false;
	      }
	}

    function IsCheckedDate_int(id)
    {
        var retValue = false;

	      if (id < NumTotale) {
		       if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT%>[id].checked)
		          retValue = true;
		      }else if (id == IndPer) {
		        if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT%>.checked)
		          retValue = true;
		      }else if (id == IndRig) {
		        if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT%>.checked)
		          retValue = true;
		      }else if (id == IndIna) {
		        if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT%>.checked)
		          retValue = true;
		      }else if (id == IndNlp) {
		        if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT%>.checked)
		          retValue = true;
	      }

      return retValue;
    }
//  ---------------------------------------------------------------------------------------  
// - - - - - - - - > Inserimento function per periodi e senmesti di L.A. Normale     
//

    /* Impedisce Focus su campo testo input numero giorni quando l'inserimento è per semestri  L.A Normale*/
    function rifiutaFocusSemestri()
    {
    	if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[0].checked)
    		document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.blur();
    	
    }

	/* Abilita la modalità di selezione periodi concessi a semestri */
    function AbilitaSemestri()
    {
         var retValue = true;
       	// alert("AbilitaSemestri: inizio - modalita = "+modalita);
	      if (modalita != 'S')
	      {
		         retValue = Chiusura();
		         if (retValue)
		         {
		        	 //	alert("AbilitaSemestri: apro le div  - modalita = "+modalita);
			           node=document.getElementById("semestri");
			           node.style.display='block';
			           node=document.getElementById("periodo");
			           node.style.display='none';
			           node=document.getElementById("resto");
				       node.style.display='block';
			           // Salvataggio dei giorni concessi per il periodo unico
			           GiorniConcessi = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value;
			           // Ripristino dei giorni concessi per semestre
			           // Nell'inserimento per semestri il numero dei giorni concessi non può essere inputato a mano
			           document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = true;
			
			           modalita = 'S';
			           aggiornaTotGiorni();
		         }
		         else
		         {
		           		document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked = true;
		           		document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[0].checked = false;
		         }
	      }
	   //   alert("AbilitaSemestri: FINE");
         return retValue;
    
    } // chiude AbilitaSemestri

/* Abilita la modalità di selezione periodi concessi a periodo unico */
    function AbilitaPeriodo()
    {
    	//	  alert("AbilitaPeriodo inizio - modalita = "+modalita);
          var retValue = true;
	      if (modalita != 'C')
	      {
		         retValue = Chiusura();
		         if (retValue)
		         {
		        	// alert("AbilitaPeriodo  sto per aprire le div");
			           node=document.getElementById("semestri");
			           node.style.display='none';
			           node=document.getElementById("periodo");
			           node.style.display='block';
			           node=document.getElementById("resto");
				       node.style.display='block';
			           // Ripristino dei giorni concessi per periodo unico
			           modalita = 'C';
			           // Nell'inserimento per unico periodo il numero dei giorni concessi deve essere inputato a mano
			           document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = false;
			
			           document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value = GiorniConcessi;
		         }
		         else
		         {
		           		document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[0].checked = true;
		           		document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked = false;
		         }
	      }
	  //    alert("AbilitaPeriodo  Fine");
         return retValue;
   
    } // chiude abilitaperiodo

    var node;

 /* Visualizzazione del layer Date corrispondente alla posizione id  L.A. NORMALE  */
    function ViewLayer(id)
    {

       // Check ON/OFF
       for (var i=0; i<NumCheck; i++)
       {
         if (i != id && document.InsOrdRevocaLA.gg[id].checked)
         { document.InsOrdRevocaLA.gg[i].disabled=true;}
         else
         { document.InsOrdRevocaLA.gg[i].disabled=false;}
       }
       // Apertura o chiusura del campo date e controllo correttezza date in chiusura */
       for (var i=0; i<NumCheck; i++)
       {
          node=document.getElementById('L'+i);
          if (i == id && document.InsOrdRevocaLA.gg[id].checked)
          { 
           // alert ("Sto aprendo " + i);
             node.style.display='block';
          }
          else
          {
             if (i == id)
             { // chiusura
 				if (chkDateContigue(id) == false)
 				{

      					for (var i=0; i<NumCheck; i++)
 			      	{
 			        	if (i == id)
 			        	{
 							document.InsOrdRevocaLA.gg[id].checked = true;
 			        	}
 			        	else
 			        	{
 							document.InsOrdRevocaLA.gg[i].disabled=true;
 			        	}
 			      	}
 					alert('Le date devono essere contigue.');
 					return false;
 				}
 				else
 				{
 					if (conteggioDate (i) == false)
                 	{
                  		/* Riposizionamento del check  */
                     	document.InsOrdRevocaLA.gg[id].checked = true;

 	      				for (var i=0; i<NumCheck; i++)
 					    {
 					    	if (i != id)
 					        {
 								document.InsOrdRevocaLA.gg[i].disabled=true;
 					        }
 					    }

 	                    return false;
                 	}
 				}
             }
             node.style.display='none';

          }
        }
        // Colore dei check
        var blue=true;
        node=document.getElementById('SL'+id);
        var elem = 0;
        elem=id*NumDate;
        for (var j=0; j<NumDate; j++, elem++)
        {
          blue=blue && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value=="")  && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value=="")  && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value=="");
          blue=blue && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value=="")  && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value=="")  && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value=="");
        }
        if (blue)
        {
          //alert ("blue");
          node.style.color="Navy";
          uncheckDate(id);
        }
        else
        {
          //alert ("red");
          node.style.color="Red";
          checkDate(id);
        }
        
        // gg(13) Rigettati - gg(14) Inammissibili - gg(15) NLP  :
        // In questi casi NON debve aggiornare i giorni;
        
         if(!document.InsOrdRevocaLA.gg[<%=IndRig%>].checked && 
     	  !document.InsOrdRevocaLA.gg[<%=IndIna%>].checked &&
     	  !document.InsOrdRevocaLA.gg[<%=IndNlp%>].checked )
        {	  
        	aggiornaTotGiorni();
        }	

        return true;
    
    } // chiude ViewLayer

//	---------------------------------------------------------------------------------------
//	-- - > Inserimento function duplicate per periodi e senmesti di L.A. SPECIALE
//

    /* Impedisce Focus su campo testo input numero giorni quando l'inserimento è per semestri  L.A SPECIALE*/
    function rifiutaFocusSemestri_SPE()
    {

    	if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[0].checked)
    		document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.blur();
    }
    
	/* Abilita la modalità di selezione periodi concessi a semestri L.A. SPECIALE */
    function AbilitaSemestri_SPE()
    {
         var retValue = true;
         //alert("AbilitaSemestri_SPE: inizio");
      if (modalita != 'S')
      {

         retValue = Chiusura();
         if (retValue)
         {
	           node=document.getElementById("semestri_SPE");
	           node.style.display='block';
	           node=document.getElementById("periodo_SPE");
	           node.style.display='none';
	           node=document.getElementById("resto_SPE");
		       node.style.display='block';
	           // Salvataggio dei giorni concessi per il periodo unico
	           GiorniConcessi_spe = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value;
	           // Ripristino dei giorni concessi per semestre
	           // Nell'inserimento per semestri il numero dei giorni concessi non può essere inputato a mano
	           document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.readOnly = true;
	
	           modalita = 'S';
	           aggiornaTotGiorni_SPE();
         }
         else
         {
	           document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[1].checked = true;
	           document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[0].checked = false;
         }
      }
         return retValue;
    
    }	// chiude AbilitaSemestri_SPE

/* Abilita la modalità di selezione periodi concessi a periodo unico  L.A. SPECIALE */
    function AbilitaPeriodo_SPE()
    {
         var retValue = true;
      if (modalita != 'C')
      {
         retValue = Chiusura();
         if (retValue)
         {
	           node=document.getElementById("semestri_SPE");
	           node.style.display='none';
	           node=document.getElementById("periodo_SPE");
	           node.style.display='block';
	           node=document.getElementById("resto_SPE");
		       node.style.display='block';
	           // Ripristino dei giorni concessi per periodo unico
	           modalita = 'C';
	           // Nell'inserimento per unico periodo il numero dei giorni concessi deve essere inputato a mano
	           document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.readOnly = false;
	
	           document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value = GiorniConcessi_spe;
         }
         else
         {
	           document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[0].checked = true;
	           document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[1].checked = false;
         }
      }
         return retValue;
   
    } 	// chiude AbilitaPeriodo_SPE    
    

   var node;

/* Visualizzazione del layer Date corrispondente alla posizione id  L.A. SPECIALE */
   function ViewLayer_SPE(id)
   {
      for (var i=0; i<NumCheck; i++)
      {
	        if (i != id && document.InsOrdRevocaLA.gg_SPE[id].checked)
	        { document.InsOrdRevocaLA.gg_SPE[i].disabled=true;}
	        else
	        { document.InsOrdRevocaLA.gg_SPE[i].disabled=false;}
      }
      // Apertura o chiusura del campo date e controllo correttezza date in chiusura */
      for (var i=0; i<NumCheck; i++)
      {
         node=document.getElementById('L_SPE'+i);
         if (i == id && document.InsOrdRevocaLA.gg_SPE[id].checked)
         {  // apertura
            node.style.display='block';
         }
         else
         {
            if (i == id)
            { // chiusura
				if (chkDateContigue_spe(id) == false)
				{

     					for (var i=0; i<NumCheck; i++)
			      	{
			        	if (i == id)
			        	{
							document.InsOrdRevocaLA.gg_SPE[id].checked = true;
			        	}
			        	else
			        	{
							document.InsOrdRevocaLA.gg_SPE[i].disabled=true;
			        	}
			      	}
					alert('Le date devono essere contigue.');
					return false;
				}
				else
				{
					if (conteggioDate_spe (i) == false)
                	{
                 		/* Riposizionamento del check  */
                    	document.InsOrdRevocaLA.gg_SPE[id].checked = true;

	      				for (var i=0; i<NumCheck; i++)
					    {
					    	if (i != id)
					        {
								document.InsOrdRevocaLA.gg_SPE[i].disabled=true;
					        }
					    }

	                    return false;
                	}
				}
            }
            node.style.display='none';

         }

      }
      
       // Colore dei check
       var blue=true;
       node=document.getElementById('SL_SPE'+id);
       var elem = 0;
       elem=id*NumDate;
       for (var j=0; j<NumDate; j++, elem++)
       {
         blue=blue && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[elem].value=="")  && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE %>[elem].value=="")  && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE %>[elem].value=="");
         blue=blue && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_SPE %>[elem].value=="")  && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_SPE %>[elem].value=="")  && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_SPE %>[elem].value=="");
       }
       if (blue)
       {
         //alert ("blue");
         node.style.color="Navy";
         uncheckDate_spe(id);
       }
       else
       {
         //alert ("red");
         node.style.color="Red";
         checkDate_spe(id);
       }
       
       // gg_SPE(13) Rigettati - gg_SPE(14) Inammissibili - gg_SPE(15) NLP  :
       // In questi casi NON debve aggiornare i giorni;
       
        if(!document.InsOrdRevocaLA.gg_SPE[<%=IndRig%>].checked && 
    	  !document.InsOrdRevocaLA.gg_SPE[<%=IndIna%>].checked &&
    	  !document.InsOrdRevocaLA.gg_SPE[<%=IndNlp%>].checked )
       {	  
    	   aggiornaTotGiorni_SPE();
       }
       
        return true;
   
   }	// chiude ViewLayer_SPE(
//
//	---------------------------------------------------------------------------------------
//	-- - > Inserimento function duplicate per periodi e senmesti di L.A. INTEGRAZIONE
//
    /* Impedisce Focus su campo testo input numero giorni quando l'inserimento è per semestri  L.A INTEGRAZIONE */
    function rifiutaFocusSemestri_INT()
    {
    	if (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>[0].checked)
    		document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.blur();
    }
    
	/* Abilita la modalità di selezione periodi concessi a semestri L.A. INTEGRAZIONE */
    function AbilitaSemestri_INT()
    {
         var retValue = true;
         //alert("AbilitaSemestriINT: inizio");
      if (modalita != 'S')
      {
         retValue = Chiusura();
         if (retValue)
         {
           node=document.getElementById("semestri_INT");
           node.style.display='block';
           node=document.getElementById("periodo_INT");
           node.style.display='none';
           node=document.getElementById("resto_INT");
	       node.style.display='block';
           // Salvataggio dei giorni concessi per il periodo unico
           GiorniConcessi_int = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value;
           // Ripristino dei giorni concessi per semestre
           // Nell'inserimento per semestri il numero dei giorni concessi non può essere inputato a mano
           document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.readOnly = true;

           modalita = 'S';
           aggiornaTotGiorni_INT();
         }
         else
         {
           document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>[1].checked = true;
           document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>[0].checked = false;
         }
      }
         return retValue;
    
    }	// chiude AbilitaSemestri_INT

/* Abilita la modalità di selezione periodi concessi a periodo unico  L.A. INTEGRAZIONE */
    function AbilitaPeriodo_INT()
    {
          var retValue = true;
	      if (modalita != 'C')
	      {
	         retValue = Chiusura();
	         if (retValue)
	         {
		           node=document.getElementById("semestri_INT");
		           node.style.display='none';
		           node=document.getElementById("periodo_INT");
		           node.style.display='block';
		           node=document.getElementById("resto_INT");
			       node.style.display='block';
		           // Ripristino dei giorni concessi per periodo unico
		           modalita = 'C';
		           // Nell'inserimento per unico periodo il numero dei giorni concessi deve essere inputato a mano
		           document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.readOnly = false;
		
		           document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value = GiorniConcessi_int;
	         }
	         else
	         {
	           		document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>[0].checked = true;
	           		document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>[1].checked = false;
	         }
	      }
          return retValue;
   
    } 	// chiude AbilitaPeriodo_INT    
    

   var node;

/* Visualizzazione del layer Date corrispondente alla posizione id  L.A. INTEGRAZIONE */
   function ViewLayer_INT(id)
   {

      for (var i=0; i<NumCheck; i++)
      {

	        if (i != id && document.InsOrdRevocaLA.gg_INT[id].checked)
	        { document.InsOrdRevocaLA.gg_INT[i].disabled=true;}
	        else
	        { document.InsOrdRevocaLA.gg_INT[i].disabled=false;}
      }
      // Apertura o chiusura del campo date e controllo correttezza date in chiusura */
      for (var i=0; i<NumCheck; i++)
      {
         node=document.getElementById('L_INT'+i);
         if (i == id && document.InsOrdRevocaLA.gg_INT[id].checked)
         {  // apertura

            node.style.display='block';
         }
         else
         {
            if (i == id)
            { // chiusura

				if (chkDateContigue_int(id) == false)
				{

     					for (var i=0; i<NumCheck; i++)
			      	{
			        	if (i == id)
			        	{
							document.InsOrdRevocaLA.gg_INT[id].checked = true;
			        	}
			        	else
			        	{
							document.InsOrdRevocaLA.gg_INT[i].disabled=true;
			        	}
			      	}
					alert('Le date devono essere contigue.');
					return false;
				}
				else
				{
					if (conteggioDate_int (i) == false)
                	{
                 		/* Riposizionamento del check  */
                    	document.InsOrdRevocaLA.gg_INT[id].checked = true;

	      				for (var i=0; i<NumCheck; i++)
					    {
					    	if (i != id)
					        {
								document.InsOrdRevocaLA.gg_INT[i].disabled=true;
					        }
					    }

	                    return false;
                	}
				}
            }
            node.style.display='none';
         }
       }
      
       // Colore dei check
       var blue=true;
       node=document.getElementById('SL_INT'+id);
       var elem = 0;
       elem=id*NumDate;
       for (var j=0; j<NumDate; j++, elem++)
       {
         blue=blue && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[elem].value=="")  && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT %>[elem].value=="")  && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT %>[elem].value=="");
         blue=blue && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_INT %>[elem].value=="")  && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_INT %>[elem].value=="")  && (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_INT %>[elem].value=="");
       }
       if (blue)
       {
         //alert ("blue");
         node.style.color="Navy";
         uncheckDate_int(id);
       }
       else
       {
         //alert ("red");
         node.style.color="Red";
         checkDate_int(id);
       }

       // gg_INT(13) Rigettati - gg_INT(14) Inammissibili - gg_INT(15) NLP  :
       // In questi casi NON debve aggiornare i giorni;
       
        if(!document.InsOrdRevocaLA.gg_INT[<%=IndRig%>].checked && 
    	  !document.InsOrdRevocaLA.gg_INT[<%=IndIna%>].checked &&
    	  !document.InsOrdRevocaLA.gg_INT[<%=IndNlp%>].checked )
       {	  
       		aggiornaTotGiorni_INT();
       }	

       return true;
   
   }	// chiude ViewLayer_INT(
   //
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
   //
	//CONTROLLO DATE CONTIGUE	L.A. NORMALE
	function chkDateContigue(id)
	{
		var returnchkDC = true;

		var rigaprima = false;
		var riga = false;

		elem=id*NumDate;
		for (var j=0; j<NumDate; j++, elem++)
		{
			if(j==0)
			{
				var gg0 = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value;

				if(gg0 != '')
				{
					riga = true;
				}
			}
			else
			{
				var gg0 = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value;
				var gg0rigaprima = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem-1].value;
				if(gg0 != '')
				{
					riga = true;
				}
				else{
					riga = false
				}

				if(gg0rigaprima != '')
				{
					rigaprima = true;
				}
				else{
					rigaprima = false
				}

				if(rigaprima == false && riga == true){
					returnchkDC = false;
				}
			}
		}
		return returnchkDC;

	} // chiude ckeckdate contigue
 //
//	L.A. SPECIALE
	function chkDateContigue_spe(id)
	{
		var returnchkDC = true;

		var rigaprima = false;
		var riga = false;

		elem=id*NumDate;
		for (var j=0; j<NumDate; j++, elem++)
		{
			if(j==0)
			{
				var gg0 = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE%>[elem].value;

				if(gg0 != '')
				{
					riga = true;
				}
			}
			else
			{
				var gg0 = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE%>[elem].value;
				var gg0rigaprima = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE%>[elem-1].value;
				if(gg0 != '')
				{
					riga = true;
				}
				else{
					riga = false
				}

				if(gg0rigaprima != '')
				{
					rigaprima = true;
				}
				else{
					rigaprima = false
				}

				if(rigaprima == false && riga == true){
					returnchkDC = false;
				}
			}
		}
		return returnchkDC;

	} // chiude ckeckdate contigue

//	L.A. INTEGRAZIONE
	function chkDateContigue_int(id)
	{
		var returnchkDC = true;

		var rigaprima = false;
		var riga = false;

		elem=id*NumDate;
		for (var j=0; j<NumDate; j++, elem++)
		{
			if(j==0)
			{
				var gg0 = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT%>[elem].value;

				if(gg0 != '')
				{
					riga = true;
				}
			}
			else
			{
				var gg0 = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT%>[elem].value;
				var gg0rigaprima = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT%>[elem-1].value;
				if(gg0 != '')
				{
					riga = true;
				}
				else{
					riga = false
				}

				if(gg0rigaprima != '')
				{
					rigaprima = true;
				}
				else{
					rigaprima = false
				}

				if(rigaprima == false && riga == true){
					returnchkDC = false;
				}
			}
		}
		return returnchkDC;

	} // chiude ckeckdate contigue

//---	
   // STUB 21/07/2004 Controllo obbligatorietà esiti.
   function Verify()
   {
	//	alert("verify - inizio");
          var retValue = false;
          var lEsiti=document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
          
         retValue = VerifyCombo(lEsiti,"Esito")
         if (retValue)
         {
        	//  alert("verify - 1");

	            GiorniConcessi = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value;
	            GiorniConcessi_spe = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value;
	            GiorniConcessi_int = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value;

	            SalvaGiorniConcessi = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>.value;
	            SalvaGiorniConcessi_spe = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE%>.value;
	            SalvaGiorniConcessi_int = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT%>.value;
	        //    alert("verify - 2");
	            
	            if (ComboConcede())
	            {
					//	 alert("verify -comboconcede");
		      		if (typeof (document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
					{	
		      			//  esito Concede Revoca	        	 				
		      			if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0028" || 
		      				document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2135"	) 
		      			{	
		      				if( GiorniConcessi == 0 || GiorniConcessi%45 != 0 ) 
      			        	{
      			              	// ...L.A. Normale
      			             	alert("Il totale Giorni deve essere Maggiore di ZERO e multiplo di 45! ");
      			                document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus();
      			                return false;
      			        	}
		      				else
		      				{
		            			flagConcesso = 'S';
		            			document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO%>.value = "S";
	
		      				}
		      			}		
      			              		
      			        if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0620" ||
      			        	document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2136") 
      			        {	
      			        	if( GiorniConcessi_spe == 0 || GiorniConcessi_spe%75 != 0 )
      			        	{
      			          		// ...L.A. Speciale
      			                 alert("Il totale Giorni deve essere Maggiore di ZERO e multiplo di 75! ");
      			                document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
      			                return false;
      			        	}
   							else
   							{
		            			flagConcesso_spe = 'S';
		            			document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO%>.value = "S";
   								
   							}	
	   
      			        }
      			              		
      			        if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0621" ||
      			        	document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2137")
      			        {	
      			        	if( GiorniConcessi_int == 0 || GiorniConcessi_int%30 != 0 )
      			        	{
      			          		// ...L.A. Integrazione
      			                 alert("Il totale Giorni deve essere Maggiore di ZERO e multiplo di 30! ");
      			                document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
      			                return false;
      			        	}
      			        	else
      			        	{
		            			flagConcesso_int = 'S';
		            			document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO%>.value = "S";
      			        		
      			        	}	
      			        }
					}
		      		else
	            	{
		      			var codOgg = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value;	
                		var indsel = document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
                		//alert("1 - INDSEL = "+indsel+" --  OGGETTO = "+codOgg);
						if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "0028" ||
								document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "2135") 
		        	 	{
							if( GiorniConcessi == 0 || GiorniConcessi%45 != 0 ) 
      			        	{
								// ...L.A. Normale
      			             	alert("Il totale Giorni deve essere Maggiore di ZERO e multiplo di 45! ");
      			                document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus();
      			                return false;
			            	}
			            	else
			            	{	
			            		flagConcesso = 'S';
			            		document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO%>.value = "S";
			            	}
		        	 	}
						
						if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "0620" ||
							document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "2136") 
		        	 	{
      			        	if( GiorniConcessi_spe == 0 || GiorniConcessi_spe%75 != 0 )
      			        	{
      			          		// ...L.A. Speciale
      			                 alert("Il totale Giorni deve essere Maggiore di ZERO e multiplo di 75! ");
      			                document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
      			                return false;
      			        	}
			            	else
			            	{	
			            		flagConcesso_spe = 'S';
			            		document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO%>.value = "S";
			            	}
		        	 	}

						if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "0621" ||
							document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "2137") 
		        	 	{
      			        	if( GiorniConcessi_int == 0 || GiorniConcessi_int%30 != 0 )
      			        	{
      			          		// ...L.A. Integrazione
      			                 alert("Il totale Giorni deve essere Maggiore di ZERO e multiplo di 30! ");
      			                document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
      			                return false;
      			        	}
			            	else
			            	{	
			            		flagConcesso_int = 'S';
			            		document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO%>.value = "S";
			            	}
		        	 	}

	            	}	// Chiude if else ......=="undefined"

	            	//	alert("verify - 4");
            	}	// Chiude if(comboconcede)
	            
	           	// STUB 16/07/2008 Nel caso di NonRevoca, Incompetenza, NDP/NLP, Inammissibilità,  va controllato che siano = 0 i gg Concessi.
            	if (ComboNonRevoca() || ComboIncompetenza() || ComboNDPNLP() || ComboInammissibile() )
            	{
                	//alert("dentro comboXrigetti = true ....");
            		retValue = Chiusura();
            		if (retValue)
            		{
            			//alert("verify - 6");
              			retValue = InseritoPeriodo();
            		}
 //           		alert("verify - 7");
            		if (typeof (document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
					{
            				if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0044" || 
                				document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0045" || 
                				document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0046" || 
                				document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0047" ||
                				document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1241" ||
                				document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1242" ||
                				document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1243" ||
                				document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1244" ) 
                			{
            					if(	(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0028" || 
            					  document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2135" ) && GiorniConcessi != 0)
          			          	{
          			              	// L.A. Normale
          			               alert("Azzerare i Giorni Concessi! ");
          			               document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus();
          			               return false;
          			          	}
          			              		
          			          	if( (document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0620" ||
          			          		document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2136" ) && GiorniConcessi_spe != 0)
          			          	{
          			          		// L.A. Speciale
          			                alert("Azzerare i Giorni Concessi! ");
          			                document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
          			                return false;
          			          	}
          			              		
          			          	if( (document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0621" ||
          			          		document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2137" ) && GiorniConcessi_int != 0)
          			          	{
          			          		// L.A. Integrazione
          			                alert("Azzerare i Giorni Concessi! ");
          			                document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
          			                return false;
          			          	}
                				
                			}	
					}
            		else
            		{	
            			//alert("verify - 7.1");
                			var codOgg = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value;	
                			var indsel = document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;

            				if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indsel].value == "0044" || 
            					document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indsel].value == "0045" || 
            					document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indsel].value == "0046" || 
            					document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indsel].value == "0047" ||
            					document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indsel].value == "1241" ||
            					document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indsel].value == "1242" ||
            					document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indsel].value == "1243" ||
            					document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indsel].value == "1244" )
            				{
            					if( (document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "0028" ||
            						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "2135"	) && GiorniConcessi != 0)
          			          	{
          			              	// L.A. Normale
          			               alert("Azzerare i Giorni Concessi! ");
          			               document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus();
          			               return false;
          			          	}
          			              		
          			          	if( (document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "0620" ||
          			          		document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "2136" ) && GiorniConcessi_spe != 0)
          			          	{
          			          		// L.A. Speciale
          			                alert("Azzerare i Giorni Concessi! ");
          			                document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
          			                return false;
          			          	}
          			              		
          			          	if( (document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "0621" ||
          			          		document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "2137") && GiorniConcessi_int != 0)
          			          	{
          			          		// L.A. Integrazione
          			                alert("Azzerare i Giorni Concessi! ");
          			                document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
          			                return false;
          			          	}
            				
            				}	
            		}
            		
            	} // Chiude if (ComboNonRevoca() || ComboIncompetenza() || ComboNDPNLP() || ComboInammissibile() )

// Nel caso di Incompetenza non vengono inseriti periodi e viene inserito il Magistrato Competente
	    //   alert("verify - 4.1");
	    //        if (ComboIncompetenza())
	    //        {
		//              init();
		//              return true;
	    //        }
	   //         else
	    //         	document.InsOrdRevocaLA.< %=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>.value = "";

            	retValue = Chiusura();
            //	 alert("verify - 4.2");
				DisabilitaDate();
        		DisabilitaDate_SPE();
        		DisabilitaDate_INT();

            	if (retValue)
            	{
		            retValue = InseritoPeriodo();	// SEMPRE FALSE!!!!		
		            if(!retValue)
                			//30/06/2006 alert("Nessun periodo inserito !");
              		{
			            if (! confirm("Nessun periodo inserito ! Si vuole continuare ?" ) )
			              		return false;
			            else
						{
			            		DisabilitaDate();
			                    DisabilitaDate_SPE();
			                    DisabilitaDate_INT();
								return true;
						}
              		}
              		else
            		{
                		DisabilitaDate();
              			DisabilitaDate_SPE();
              			DisabilitaDate_INT();
            		}
            	
            	} // chiude if retvalue1
            
          } // chiude if retvalue

          return retValue;
          
       }	// CHIUDE function Verify()


// Il confronto viene fatto con la descrizione e non con il codice perchè i codici sono molti.
    function ComboIncompetenza()
    {
      //alert("ComboIncompetenza : inizio");
	      var ritorno = false;
	      if (typeof (document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
	      {
	       	 	if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0046" ||
	       	 		document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1243" )
	       	 	{	
	       			ritorno = true;
	       	 	}
	      }
	      else
	      {
	    	 	 var indj = document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
	       	 	if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "0046" ||
	       	 		document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "1243")
	       	 	{	
	       			ritorno = true;
	       	 	}
	      }
	      return ritorno;
    }

    function ComboConcede()
    {
    	//alert("comboconcede");
	      var ritorno = false;
	      
	      if (typeof (document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
	      { // Presente un solo oggetto
		       // alert ("Presente un solo oggetto");
	    	  	if (document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0043" ||
	    	  		document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1240" ) 
		      	{ 
		          	ritorno = true;
		        }
	      }
	      else
	      {	
		        //alert ("Presenti più oggetti scorro");
		        var indj = document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
		        if (document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "0043" || 
		        	document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "1240") 
			    { 
			          	ritorno = true;
			    }
	      }
	      
	      return ritorno;
    
    } // chiude ComboConcede

    // STUB 26/03/2007 Controllo Combo.
    function ComboNonRevoca()
    {
    	//alert("comboNoRev");
      var ritorno = false;
      if (typeof (document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
      {
    	  if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0047" ||
    		  document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1241" )
     	  {	
     			ritorno = true;
     	  } 
      }
      else
      {
       	 	var indj = document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
       	 	if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "0047" ||
       	 		document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "1241" )
       	 	{	
       			ritorno = true;
       	 	}	
      }
      return ritorno;
    }

    // STUB 16/07/2008 Controllo ComboNDPNLP.
    function ComboNDPNLP()
    {
    	//alert("ComboNDPNLP");
      var ritorno = false;
      if (typeof (document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
      {
     	 	if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0045" ||
     	 		document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1242")
     	 	{	
     			ritorno = true;
     	 	}
      }
      else
      {
    	  	var indj = document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
     	 	if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "0045" ||
     	 		document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "1242")
     	 	{	
     			ritorno = true;
     	 	}
      }
      return ritorno;
    }

    // STUB 16/07/2008 Controllo Inammissibilità.
    function ComboInammissibile()
    {
    	//alert("ComboInammissibile");
      var ritorno = false;
      if (typeof (document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
      {
	   	 	if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0044" || 
	   	 		document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1244")
	 	 	{	
	 			ritorno = true;
	 	 	}
      }
      else
      {
	    	var indj = document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
	   	 	if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "0044" ||
	   	 		document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "1244")
	   	 	{	
	   			ritorno = true;
	   	 	}
      }
      return ritorno;
    }


    // Funzione di controllo per determinare se è stato inserito almeno un periodo.
    function InseritoPeriodo()
    {
    	//	alert(" InseritoPeriodo - inizio");

        if (typeof (document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[0]) =="undefined" )
        {	// esiste solo 1 oggetto Liberazione Anticipata
        	if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0028" || 
        		document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2135")
    		{
    			// L.A. Normale
		        for (i=0; i < NumCheck; i++)
		        {
			         // Vengono disabilitati tutti i blocchi periodi vuoti
			          if(IsCheckedDate(i))
			          {
			             return true;
			          }
		        }
    		}

    		if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0620" ||
    			document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2136")
    		{
    			// L.A. Speciale
			        for (i=0; i < NumCheck; i++)
		        	{
				         // Vengono disabilitati tutti i blocchi periodi vuoti
				          if(IsCheckedDate_spe(i))
				          {
				             return true;
				          }
		        	}
    		}

    		if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0621" ||
    			document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2137")
    		{
    			// L.A. Integrazione
		        for (i=0; i < NumCheck; i++)
		        {
			         // Vengono disabilitati tutti i blocchi periodi vuoti
			          if(IsCheckedDate_int(i))
			          {
			             return true;
			          }
		        }
    		}
        }	
        else
        {	// esistono più oggetti Liberazione Anticipata

        	var indj = document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
        	if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indj].value == "0028" || 
        		document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indj].value == "2135")
	   	 	{
        		// L.A. Normale
		        for (i=0; i < NumCheck; i++)
		        {
			         // Vengono disabilitati tutti i blocchi periodi vuoti
			          if(IsCheckedDate(i))
			          {
			             return true;
			          }
		        }
	   	 	}
        	
        	if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indj].value == "0620" ||
        		document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indj].value == "2136")
    		{
    			// L.A. Speciale
			        for (i=0; i < NumCheck; i++)
		        {
			         // Vengono disabilitati tutti i blocchi periodi vuoti
			          if(IsCheckedDate_spe(i))
			          {
			             return true;
			          }
		        }
    		}

    		if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indj].value == "0621" ||
    			document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indj].value == "2137")
    		{
    			// L.A. Integrazione
		        for (i=0; i < NumCheck; i++)
		        {
			         // Vengono disabilitati tutti i blocchi periodi vuoti
			          if(IsCheckedDate_int(i))
			          {
			             return true;
			          }
		        }
    		}
        	
        }    // chiude else if undefined	
        
        return false;
        
    }	// chiude InseritoPeriodo()

// ---->
// Decreto Legge 2013/146

	/* Abilita la modalità di selezione periodi concessi a seconda di L.A., L.A. speciale, Integrazione L.A.  */
    function QualeRevocaSeleziona(cod,indd)
    {
    	var lungh = <%=tenori.length%>;
    	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = indd;
    	//alert("- QualeRevocaSeleziona - Assegno CAMPO_QUALE_IND_SELEZIONATO =  ind = "+indd +" cod = "+cod);

    	// assegno valori fittizi agli INDICI degli Oggetti L.A.; 
    	// ------ - 	> NON posso assegnare 0 perchè è un INDICE VALIDO ; 
        var ind_per_LA = 9;
        var ind_per_LA_SPE = 9;
        var ind_per_LA_INT = 9;
        
        if (typeof (document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
        {
        }	  
        else
        {		// assegno un valore fisso agli indici degli oggetti (possono essere 2 o 3 oggetti)
      	  	for (j = 0; j < document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++ )
  	        {
  	            if (document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0028" ||
  	            	document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2135")
  	            {
  	            	ind_per_LA = j;
  	            }
  	            else if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0620" ||
  	            		document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2136")
  	            {
  	            	ind_per_LA_SPE = j;
  	            }
  	            else if(document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0621" ||
  	            		document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2137")
  	            {
  	            	ind_per_LA_INT = j;
  	            }
  	        }
        }	  

		// cod oggetto selezioato 0028/2135 : L.A. NORMALE - - -> Abilito i campi di L.A. e Disabilito le altre L.A, e controllo i multipli di 75 e 30
		if(cod == 0028 || cod == 2135)
		{
			// document.InsOrdRevocaLA.< %=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = "0028";
				document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = cod;
			
			var totLA_SPE = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value;
			// se indice != 9 Vuol dire che l'oggetto esiste) 
			if(ind_per_LA_SPE != 9)
			{
				if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_SPE].value == "0043" ||
					document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_SPE].value == "1240")
				{	// Accoglimento Revoca						
					
					if(totLA_SPE == 0 || totLA_SPE%75 != 0)
					{
						alert("Il totale Giorni Concessi deve essere \n maggiore di 0 e multiplo di 75!");
			          	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
			          	document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[ind_per_LA_SPE].checked = true;
			          	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = ind_per_LA_SPE;
			          	return false;
					}
					
					document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO%>.value = "S";
				}
				else if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_SPE].value == "0044" || 
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_SPE].value == "0045" || 
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_SPE].value == "0046" ||
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_SPE].value == "0047"  ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_SPE].value == "1241" || 
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_SPE].value == "1242" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_SPE].value == "1243" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_SPE].value == "1244" )
				{	// rigetti
					if(totLA_SPE != 0 )
					{
						alert("Se NON concede, il totale Giorni Concessi deve essere 0 ! ");
			          	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
			          	document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[ind_per_LA_SPE].checked = true;
			          	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = ind_per_LA_SPE;
			          	return false;
					}	
				}
			}	
			DisabilitaLA_SPE();
// -	-	>
			var totLA_INT = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value;
			if(ind_per_LA_INT != 9)
			{
				if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_INT].value == "0043" ||
					document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_INT].value == "1240")
				{	// Accoglimento revoca						
					if(totLA_INT == 0 || totLA_INT%30 != 0)
					{
						alert("Il totale Giorni Concessi deve essere \n maggiore di 0 e multiplo di 30! ");
			          	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
			          	document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[ind_per_LA_INT].checked = true;
			          	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = ind_per_LA_INT;
			          	return false;
					}
					document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO%>.value = "S";
				}
				else if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_INT].value == "0044" || 
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_INT].value == "0045" || 
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_INT].value == "0046" ||
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_INT].value == "0047" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_INT].value == "1241" || 
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_INT].value == "1242" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_INT].value == "1243" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_INT].value == "1244" )
				{	// rigetti
					if(totLA_INT != 0 )
					{
						alert("Se NON concede, il totale Giorni Concessi deve essere 0 ! ");
			          	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
			          	document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[ind_per_LA_INT].checked = true;
			          	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = ind_per_LA_INT;
			          	return false;
					}	
				}
			}	
			DisabilitaLA_INT();
//	-	-	>
			AbilitaLA();
		
		} // chiude if(cod == 0028 / 2135)
		
//->>>>>>>>>>>>

		// cod oggetto selezioato 0620 / 2136: L.A. SPECIALE - - -> Abilito i campi di L.A.Speciale e Disabilito le altre L.A, e controllo i multipli di 45 e 30
		if(cod == 0620 || cod == 2136)
		{
			//document.InsOrdRevocaLA.< %=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = "0620";
			document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = cod;
			
			var totLA = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value;
			if(ind_per_LA != 9)
			{
				if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA].value == "0043" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA].value == "1240")
				{	// Accoglimento Revoca
					if(totLA == 0 || totLA%45 != 0)
					{
						alert("Il totale Giorni Concessi deve essere \n maggiore di 0 e multiplo di 45!");
			          	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.focus();
			          	document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[ind_per_LA].checked = true;
			          	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = ind_per_LA;
			          	return false;
					}
					document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO%>.value = "S";
				}
				else if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA].value == "0044" || 
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA].value == "0045" || 
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA].value == "0046" ||
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA].value == "0047" || 
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA].value == "1241" || 
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA].value == "1242" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA].value == "1243" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA].value == "1244" )
				{	// rigetti
					if(totLA != 0 )
					{
						alert("Se NON concede, il totale Giorni Concessi deve essere 0 ! ");
			          	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus();
			          	document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[ind_per_LA].checked = true;
			          	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = ind_per_LA;
			          	return false;
					}	
				}
			}	
			DisabilitaLA();
// -	-	>
			var totLA_INT = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value;
			if(ind_per_LA_INT != 9)
			{
				if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_INT].value == "0043" ||
					document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_INT].value == "1240")
				{	// Accoglimento Revoca	
					if(totLA_INT == 0 || totLA_INT%30 != 0)
					{
						alert("Il totale Giorni Concessi deve essere \n maggiore di 0 e multiplo di 30! ");
			          	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
			          	document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[ind_per_LA_INT].checked = true;
			          	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = ind_per_LA_INT;
			          	return false;
					}
					document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO%>.value = "S";
				}	
				else if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_INT].value == "0044" || 
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_INT].value == "0045" || 
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_INT].value == "0046" ||
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_INT].value == "0047" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_INT].value == "1241" || 
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_INT].value == "1242" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_INT].value == "1243" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_INT].value == "1244" )
				{	// rigetti
					if(totLA_INT != 0 )
					{
						alert("Se NON concede, il totale Giorni Concessi deve essere 0 ! ");
			          	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
			          	document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[ind_per_LA_INT].checked = true;
			          	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = ind_per_LA_INT;
			          	return false;
					}	
				}
			}	
			DisabilitaLA_INT();
// -	-	>			
		AbilitaLA_SPE();
		
		} // chiude if(cod == "0620" || 2136)	
		
// >>>>>>>>>		
		// cod oggetto selezioato 0621 / 2137 : L.A. INTEGRAZIONE - - -> Abilito i campi di L.A.Integraz. e Disabilito le altre L.A, e controllo i multipli di 45 e 75
		if(cod == 0621 || cod == 2137)
		{
			//document.InsOrdRevocaLA.< %=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = "0621";
			document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = cod;
			
			var totLA = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value;
			if(ind_per_LA != 9)
			{
				if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA].value == "0043" ||
					document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA].value == "1240")
				{	// Accoglimento Revoca	
					if(totLA == 0 || totLA%45 != 0)
					{
						alert("Il totale Giorni Concessi deve essere \n maggiore di 0 e multiplo di 45!");
			          	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.focus();
			          	document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[ind_per_LA].checked = true;
			          	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = ind_per_LA;
			          	return false;
					}
					document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO%>.value = "S";
				}
				else if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA].value == "0044" || 
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA].value == "0045" || 
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA].value == "0046" ||
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA].value == "0047"  ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA].value == "1241" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA].value == "1242" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA].value == "1243" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA].value == "1244" )
				{	// rigetti
					if(totLA != 0 )
					{
						alert("Se NON concede, il totale Giorni Concessi deve essere 0 ! ");
			          	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus();
			          	document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[ind_per_LA].checked = true;
			          	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = ind_per_LA;
			          	return false;
					}	
				}
			}
			DisabilitaLA();
// 	-	-	>			
			var totLA_SPE = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value;
			if(ind_per_LA_SPE != 9)
			{
				if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_SPE].value == "0043" ||
					document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_SPE].value == "1240")
				{	// Accoglimento Revoca	
					if(totLA_SPE == 0 || totLA_SPE%75 != 0)
					{
						alert("Il totale Giorni Concessi deve essere \n maggiore di 0 e multiplo di 75!");
			          	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
			          	document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[ind_per_LA_SPE].checked = true;
			          	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = ind_per_LA_SPE;
			          	return false;
					}
					document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO%>.value = "S";
				}	
				else if(document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_SPE].value == "0044" || 
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_SPE].value == "0045" || 
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_SPE].value == "0046" ||
						document.InsOrdRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[ind_per_LA_SPE].value == "0047" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_SPE].value == "1241" || 
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_SPE].value == "1242" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_SPE].value == "1243" ||
						document.InsOrdRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[ind_per_LA_SPE].value == "1244")
				{	// rigetti
					if(totLA_SPE != 0 )
					{
						alert("Se NON concede, il totale Giorni Concessi deve essere 0 ! ");
			          	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
			          	document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[ind_per_LA_SPE].checked = true;
			          	document.InsOrdRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = ind_per_LA_SPE;
			          	return false;
					}	
				}
			}	
			DisabilitaLA_SPE();
//	-	-	>		
			AbilitaLA_INT();
			
		}	//  chiude if(cod == "0621" || 2137)	
		
    }   // Chiude QualeRevocaSeleziona()


// Oggetto : L.A.(Liberazione Anticipata)
	function AbilitaLA()
	{
	//	alert("AbilitaLA");
        node=document.getElementById("TotaleLA");
        node.style.display='block';
       
        AbilitaPeriodo();
        AbilitaSemestri();
        if(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked)
        	AbilitaPeriodo();
		
        var valoreLA = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>.value;
        if(valoreLA=="")
        	valoreLA="0";
        
        document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value = valoreLA;
       
        if(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked)
        	 document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = false;
        else
        	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = true;
	}
     
	function DisabilitaLA()
	{
		//alert("Disab LA");
		var valLA = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value;
		document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>.value = valLA;
		
        node=document.getElementById("TotaleLA");
        node.style.display='none';
        node=document.getElementById("semestri");
        node.style.display='none';
        node=document.getElementById("periodo");
        node.style.display='none';
        node=document.getElementById("resto");
        node.style.display='none';
	}
	
// Oggetto : L.A.S.(Liberazione Anticipata Speciale)
	function AbilitaLA_SPE()
	{
        node=document.getElementById("TotaleLA_SPE");
        node.style.display='block';
        
        AbilitaPeriodo_SPE();
        AbilitaSemestri_SPE();  
        if(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[1].checked)
        	AbilitaPeriodo_SPE();
        
        var valoreSPE=document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE%>.value;
        if(valoreSPE=="")
        	valoreSPE="0";

        document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value = valoreSPE;
        
        if(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[1].checked)
       	 	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.readOnly = false;
        else
        	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.readOnly = true;
	}
     
	function DisabilitaLA_SPE()
	{
		//alert("Disab LA_SPE");
		var valSPE = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value;
		document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE%>.value = valSPE;
		
        node=document.getElementById("TotaleLA_SPE");
        node.style.display='none';
        node=document.getElementById("semestri_SPE");
        node.style.display='none';
        node=document.getElementById("periodo_SPE");
        node.style.display='none';	
        node=document.getElementById("resto_SPE");
        node.style.display='none';
	}

	// Oggetto : L.A.I.(Liberazione Anticipata Integrazione)
	function AbilitaLA_INT()
	{
        node=document.getElementById("TotaleLA_INT");
        node.style.display='block';
        
        AbilitaPeriodo_INT();
        AbilitaSemestri_INT();
        if(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>[1].checked)
        	AbilitaPeriodo_INT();     

        var valoreLA_INT = document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT%>.value;
        if(valoreLA_INT=="")
        	valoreLA_INT="0";
        
        document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value = valoreLA_INT;
        
        if(document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>[1].checked)
       	 	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.readOnly = false;
        else
        	document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.readOnly = true;
	}
     
	function DisabilitaLA_INT()
	{
		//alert("Disab LA_INT");
		var valLA_INT = document.InsOrdRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value;
		document.InsOrdRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT%>.value = valLA_INT;

        node=document.getElementById("TotaleLA_INT");
        node.style.display='none';
        node=document.getElementById("semestri_INT");
        node.style.display='none';
        node=document.getElementById("periodo_INT");
        node.style.display='none';
        node=document.getElementById("resto_INT");
        node.style.display='none';
 	}
		
	
    </script>

    <script language="JavaScript">
      var desktop;
      // Chiamata all'elenco degli UDS
      function ListaUDS(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      // Chiamata all'elenco dei CSSA
      function ListaCSSA(a_formname,a_fieldname, a_fieldcode)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSA&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldcode="+a_fieldcode, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    </script>

  </head>

  <body class="corpo" onLoad="Javascript:init();">

    <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font>
    <%	if(decreto.compareTo("SI")==0)
    	{	%>  
     		<font class="campo">Emissione Decreto di Revoca Liberazione Anticipata</font>&nbsp;
    <%	}
    	else
    	{	%>
    		<font class="campo">Emissione Ordinanza di Revoca Liberazione Anticipata</font>&nbsp;
    <%	} %>	 		
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InsOrdRevocaLA">

    <table width=35%>
   <tr>
     <td class="l" width=30%> Data Emissione</td>
     <td class="l" width=70%> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   </tr>
    </table>
        <tr> <td>&nbsp;</td> </tr>
    <table cellspacing="3" cellpadding="2" width=95%>
    <tr>
        <td class="Titolo" colspan=6 > Specificare esito per ciascun oggetto: </td>
    </tr>
    <tr>
        <td class="l" colspan=2 width="25%"> Oggetto </td>
        <td class="l" colspan=2 width="20%" > Seleziona </td>
        <td class="l" colspan=2 width="40%"> Esito   </td>
    </tr>
    <%
   for (int i=0; i< tenori.length;i++)
    {
    %>
       <tr>
        <td class="l"colspan=2 width="25%">
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=60%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
        <td class="l"colspan=2 width="20%">
        	<input value="" onclick="Javascript:return QualeRevocaSeleziona(<%=tenori[i].getCodOggettoTenore()%>,<%=i %>);" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>" >&nbsp;&nbsp;&nbsp;  
        </td>	
          <td class="l"colspan=2 width="40%">
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>">
             <%=esiti[i]%>
          </select>
        </td>
      </tr>
    <%
    }
    %>
	</table>

    <tr><td>&nbsp;</td> </tr>
	<table cellspacing="3" cellpadding="2" width=95%>
    	<tr>
			<td class="l" colspan="2" width="20%" >Ulteriore descrizione della decisione</td>
    		<td class="l" colspan="2" width="60%" ><TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="80" rows="4" ></textarea></td>
		</tr>
    </table>
<br>
<!-- 						prova						prova									prova					 -->

       <jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_RIFERIMENTO_ORDINANZA_RECLAMO%>">
          <jsp:param name="FormName" value="InsOrdRevocaLA"/>
        </jsp:include>

<!-- 						prova						prova									prova					 -->
<br>
<!--  < < < < < <  INSERISCI REVOCA L.A. - LIB. ANT. NORMALE 45gg 	> > > > > > >	-->
<div id="TotaleLA" style="position: relative; top: 0; left: 0;" >
	<table>
	      <tr>
	        <td class="l"> Modalità di scelta dei periodi di L.A. Revocati </td>
	      </tr>
		  <tr>
	          <td> <input value="S" onclick="Javascript:return AbilitaSemestri();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>" CHECKED  > per semestri &nbsp;
	           <input value="C" onclick="Javascript:return AbilitaPeriodo();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>" > unico periodo</td>
	      </tr>
	  	  <tr> <td>&nbsp;</td> </tr>
	</table>

	<div id="comune" style="position: relative; top: 0; left: 0;" >
	  <div id="semestri" style="position: relative; top: 0; left: 0; " >
	    <table cellspacing="2" cellpadding="2" width=26%>
	    <tr>
	        <td class="Titolo" colspan=6> Semestri Revocati:&nbsp;&nbsp;&nbsp; </td>
	    </tr>
	    </table>
	    <table width=26%>
	    <%
	    for (int i=0;i<NumRighe;i++)
	    {
	    %><tr><%
	      for (int j=0;j<NumColonne;j++)
	      {
	      %>
	      <td width=3%><span id="SL<%=i*NumColonne+j%>" style="color=navy;font-weight:bold;">Giorni 45</span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=i*NumColonne+j%>');"></td>
	      <%}%>
	      </tr>
	     <%}%>
	     </table>
	</div>
	
	<div id="periodo" style="position: relative; top: 0; left: 0; display:none; " >
	    <table cellspacing="2" cellpadding="2" width=26%>
	      <tr>
	        <td class="Titolo" colspan=6> Periodo Revocato:&nbsp;&nbsp;&nbsp; </td>
	      </tr>
	    </table>
	    <table width=26%>
	      <tr>
	       <td width=3%><span id="SL<%=IndPer%>" style="color=navy;font-weight:bold;">Periodo </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndPer%>');"></td>
	      </tr>
	    </table>
	  </div>
	
	<%
	for (int i=0; i<NumCheck; i++)
	{
	%>
	<div id="L<%=i%>" style="position: relative; top: -130; left: 40%; display:none;" >
	  <table>
	   <!-- COSTRUZIONE DEI CAMPI DATA DAL - AL -->
	   <% for (int k=0; k<NumDate; k++)
	   {%>
	    <tr>
	      <td class=l>
		  	Dal
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
	        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
	        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
	     	&nbsp;&nbsp;
	     	Al
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
			/
	        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
			/
	        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
	      </td>
	    </tr>
	  <%}%>
	 </table>
	     <%
	      if (i < NumTotaleSemestri )
	     {
	     %>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI%>" value=1 style="display:none;">
	     <% } else if (i == IndPer)
	     {%>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO%>" value=1 style="display:none;">
	     <% } else if (i == (IndRig))
	     {%>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI%>" value=1 style="display:none;">
	     <% } else if (i == ( IndIna) )
	     {%>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI%>" value=1 style="display:none;">
	     <% } else if (i == ( IndNlp) )
	     {%>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP%>" value=1 style="display:none;">
	  <%  } %>
	</div>
	<%}%>
	
	 <table width=35%>
	    <tr>
	      <td class="l">Totale giorni Revocati </td>
	      <td class="l">
	        <input Title="Totale Giorni L.A." name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>" value="" size=5 onFocus="javascript:rifiutaFocusSemestri()">
	      </td>
	    </tr>
	    </table>
	</div>
</div> 
<br>
<div id="resto" style="position: relative; top: -10; left: 0;" >
    <table cellspacing="2" cellpadding="2" width=35%>
    <tr>
        <td class="Titolo" colspan=6> Periodi non Revocati:&nbsp;&nbsp;&nbsp; </td>
    </tr>
    </table>
    <table width=35%>
      <td width=3%><span id="SL<%=IndRig%>" style="color=navy;font-weight:bold;">Rigettati </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndRig%>');"></td>
      <td width=3%><span id="SL<%=IndIna%>" style="color=navy;font-weight:bold;">Inammissibili </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndIna%>');"></td>
      <td width=3%><span id="SL<%=IndNlp%>" style="color=navy;font-weight:bold;">N.L.P./N.D.P. </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndNlp%>');"></td>
   </table>
</div>

<!--  < < < < < <  INSERISCI REVOCA L.A.S. - LIB. ANT. SPECIALE 75gg 	> > > > > > >	-->
 
<br>
<div id="TotaleLA_SPE" style="position: relative; top: 0; left: 0;" >
	<table>
	      <tr>
	        <td class="l"> Modalità di scelta dei periodi di L.A. Speciale Revocati</td>
	      </tr>
		  <tr>
	          <td> <input value="S" onclick="Javascript:return AbilitaSemestri_SPE();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>"  CHECKED > per semestri &nbsp;
	           <input value="C" onclick="Javascript:return AbilitaPeriodo_SPE();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>" > unico periodo </td>
	      </tr>
	  	  <tr> <td>&nbsp;</td> </tr>
	</table>

	<div id="comune_SPE" style="position: relative; top: 0; left: 0;" >
	  <div id="semestri_SPE" style="position: relative; top: 0; left: 0; " >
	    <table cellspacing="2" cellpadding="2" width=26%>
	    <tr>
	        <td class="Titolo" colspan=6> Semestri Revocati:&nbsp;&nbsp;&nbsp; </td>
	    </tr>
	    </table>
	    <table width=26%>
	    <%
	    for (int i=0;i<NumRighe;i++)
	    {
	    %><tr><%
	      for (int j=0;j<NumColonne;j++)
	      {
	      %>
	      <td width=3%><span id="SL_SPE<%=i*NumColonne+j%>" style="color=navy;font-weight:bold;">Giorni 75</span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=i*NumColonne+j%>');"></td>
	      <%}%>
	      </tr>
	     <%}%>
	     </table>
	</div>
	
	<div id="periodo_SPE" style="position: relative; top: 0; left: 0; display:none; " >
	    <table cellspacing="2" cellpadding="2" width=26%>
	      <tr>
	        <td class="Titolo" colspan=6> Periodo Revocatoo:&nbsp;&nbsp;&nbsp; </td>
	      </tr>
	    </table>
	    <table width=26%>
	      <tr>
	       <td width=3%><span id="SL_SPE<%=IndPer%>" style="color=navy;font-weight:bold;">Periodo </span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndPer%>');"></td>
	      </tr>
	    </table>
	  </div>
	
	<%
	for (int i=0; i<NumCheck; i++)
	{
	%>
	<div id="L_SPE<%=i%>" style="position: relative; top: -130; left: 40%; display:none;" >
	  <table>
	   <!-- COSTRUZIONE DEI CAMPI DATA DAL - AL -->
	   <% for (int k=0; k<NumDate; k++)
	   {%>
	    <tr>
	      <td class=l>
		  	Dal
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
	        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
	        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE %>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
	     	&nbsp;&nbsp;
	     	Al
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_SPE %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
			/
	        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_SPE %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
			/
	        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_SPE %>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
	      </td>
	    </tr>
	  <%}%>
	 </table>
	     <%
	      if (i < NumTotaleSemestri )
	     {
	     %>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE%>" value=1 style="display:none;">
	     <% } else if (i == IndPer)
	     {%>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE%>" value=1 style="display:none;">
	     <% } else if (i == (IndRig))
	     {%>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE%>" value=1 style="display:none;">
	     <% } else if (i == ( IndIna) )
	     {%>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE%>" value=1 style="display:none;">
	     <% } else if (i == ( IndNlp) )
	     {%>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE%>" value=1 style="display:none;">
	  <%  } %>
	</div>
	<%}%>
	
	 <table width=35%>
	    <tr>
	      <td class="l">Totale giorni Revocati: </td>
	      <td class="l">
	        <input Title="Totale Giorni L.A. Speciale" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>" value="" size=5 onFocus="javascript:rifiutaFocusSemestri_SPE()">
	      </td>
	    </tr>
	    </table>
	</div>
</div> 
<br>
<div id="resto_SPE" style="position: relative; top: -10; left: 0;" >
    <table cellspacing="2" cellpadding="2" width=35%>
    <tr>
        <td class="Titolo" colspan=6> Periodi non Revocati:&nbsp;&nbsp;&nbsp; </td>
    </tr>
    </table>
    <table width=35%>
      <td width=3%><span id="SL_SPE<%=IndRig%>" style="color=navy;font-weight:bold;">Rigettati </span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndRig%>');"></td>
      <td width=3%><span id="SL_SPE<%=IndIna%>" style="color=navy;font-weight:bold;">Inammissibili </span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndIna%>');"></td>
      <td width=3%><span id="SL_SPE<%=IndNlp%>" style="color=navy;font-weight:bold;">N.L.P./N.D.P. </span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndNlp%>');"></td>
   </table>
</div>   

<!--  < < < < < <  INSERISCI REVOCA I.L.A. - LIB. ANT. INTEGRAZIONE 30gg 	> > > > > > >	-->

<br>
<div id="TotaleLA_INT" style="position: relative; top: 0; left: 0;" >
	<table>
	      <tr>
	        <td class="l"> Modalità di scelta dei periodi di Integrazione L.A. Revocati</td>
	      </tr>
		  <tr>
	          <td> <input value="S" onclick="Javascript:return AbilitaSemestri_INT();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>"  CHECKED > per semestri &nbsp;
	           <input value="C" onclick="Javascript:return AbilitaPeriodo_INT();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>" > unico periodo</td>
	      </tr>
	  	  <tr> <td>&nbsp;</td> </tr>
	</table>

	<div id="comune_INT" style="position: relative; top: 0; left: 0;" >
	  <div id="semestri_INT" style="position: relative; top: 0; left: 0; " >
	    <table cellspacing="2" cellpadding="2" width=26%>
	    <tr>
	        <td class="Titolo" colspan=6> Semestri Revocati:&nbsp;&nbsp;&nbsp; </td>
	    </tr>
	    </table>
	    <table width=26%>
	    <%
	    for (int i=0;i<NumRighe;i++)
	    {
	    %><tr><%
	      for (int j=0;j<NumColonne;j++)
	      {
	      %>
	      <td width=3%><span id="SL_INT<%=i*NumColonne+j%>" style="color=navy;font-weight:bold;">Giorni 30</span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=i*NumColonne+j%>');"></td>
	      <%}%>
	      </tr>
	     <%}%>
	     </table>
	</div>
	
	<div id="periodo_INT" style="position: relative; top: 0; left: 0; display:none; " >
	    <table cellspacing="2" cellpadding="2" width=26%>
	      <tr>
	        <td class="Titolo" colspan=6> Periodo Revocato:&nbsp;&nbsp;&nbsp; </td>
	      </tr>
	    </table>
	    <table width=26%>
	      <tr>
	       <td width=3%><span id="SL_INT<%=IndPer%>" style="color=navy;font-weight:bold;">Periodo </span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndPer%>');"></td>
	      </tr>
	    </table>
	  </div>
	
	<%
	for (int i=0; i<NumCheck; i++)
	{
	%>
	<div id="L_INT<%=i%>" style="position: relative; top: -130; left: 40%; display:none;" >
	  <table>
	   <!-- COSTRUZIONE DEI CAMPI DATA DAL - AL -->
	   <% for (int k=0; k<NumDate; k++)
	   {%>
	    <tr>
	      <td class=l>
		  	Dal
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
	        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
	        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
	     	&nbsp;&nbsp;
	     	Al
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_INT%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
			/
	        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_INT%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
			/
	        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_INT%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
	      </td>
	    </tr>
	  <%}%>
	 </table>
	     <%
	      if (i < NumTotaleSemestri )
	     {
	     %>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT%>" value=1 style="display:none;">
	     <% } else if (i == IndPer)
	     {%>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT%>" value=1 style="display:none;">
	     <% } else if (i == (IndRig))
	     {%>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT%>" value=1 style="display:none;">
	     <% } else if (i == ( IndIna) )
	     {%>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT%>" value=1 style="display:none;">
	     <% } else if (i == ( IndNlp) )
	     {%>
	    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT%>" value=1 style="display:none;">
	  <%  } %>
	</div>
	<%}%>
	
	 <table width=35%>
	    <tr>
	      <td class="l">Totale giorni Revocati: </td>
	      <td class="l">
	        <input Title="Totale Giorni Integrazione L.A." name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>" value="" size=5 onFocus="javascript:rifiutaFocusSemestri_INT()">
	      </td>
	    </tr>
	    </table>
	</div>
</div> 
<br>
<div id="resto_INT" style="position: relative; top: -10; left: 0;" >
    <table cellspacing="2" cellpadding="2" width=35%>
    <tr>
        <td class="Titolo" colspan=6> Periodi Non Revocatii: &nbsp;&nbsp;&nbsp; </td>
    </tr>
    </table>
    <table width=35%>
      <td width=3%><span id="SL_INT<%=IndRig%>" style="color=navy;font-weight:bold;">Rigettati </span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndRig%>');"></td>
      <td width=3%><span id="SL_INT<%=IndIna%>" style="color=navy;font-weight:bold;">Inammissibili </span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndIna%>');"></td>
      <td width=3%><span id="SL_INT<%=IndNlp%>" style="color=navy;font-weight:bold;">N.L.P./N.D.P. </span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndNlp%>');"></td>
   </table>
</div>

<!--  < < < < < < < <   FINE FORM   > > > > > > > > >     -->
<br><br>
 <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
      <td class="l">In caso di Incompetenza indicare l'Ufficio di Sorveglianza destinatario </td>
      <td class="l">
        <input Title="UfficioDiSorveglianza" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>" value="" size=35 >
        <a href="Javascript:ListaUDS('InsOrdRevocaLA','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </td>
    </tr>
  <tr> <td>&nbsp;</td> </tr>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO%>" >
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>" value="" >
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE%>" value="" >
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT%>" value="" >
     <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>" value="" >
     <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO%>" value="" >

<!--   QUI C'ERA LA CHIUSURA DIV id=resto -->

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InsOrdRevocaLA");


	<%
	// CONTROLLO PER I CAMPI ANNO DATE DAL AL SEMESTRI/PERIODI
	int cont = 0;
	String mex = "";
	for (int i=0; i<NumCheck; i++)
	{
		//for (int x=0; x< NumCheck*NumDate; x++)
		for (int x=0; x< NumDate; x++)
   		{
			// Costruzione dei messaggi di errore in base alla sezione Semestri o Periodi
			if(i<12)
			{
				mex ="Semestri Concessi: sezione "+ (i+1) +"\\n\\n";
			}
			else if (i == 12)
			{
				mex ="Periodo Concesso: \\n\\n";
			}
			else if (i == 13)
			{
				mex ="Periodi Non Concessi: Rigettati (Non revocati) \\n\\n";
			}
			else if (i == 14)
			{
				mex ="Periodi Non Concessi: Inammissibili \\n\\n";
			}
			else if (i == 15)
			{
				mex ="Periodi Non Concessi: N.L.P./N.D.P. \\n\\n";
			}

			else
			{
				mex ="";
			}
	%>
			frmvalidator.addValidationWithIdx("<%= ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>","<%=cont%>","numeric");
			frmvalidator.addValidationWithIdx("<%= ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>","<%=cont%>","gt=1900","<%=mex%> Il valore del campo Anno Data Inizio dovrebbe essere maggiore di 1900");

			frmvalidator.addValidationWithIdx("<%= ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>","<%=cont%>","numeric");
		  	frmvalidator.addValidationWithIdx("<%= ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>","<%=cont%>","gt=1900","<%=mex%> Il valore del campo Anno Data Fine dovrebbe essere maggiore di 1900");
	<%
		 cont = cont + 1;
		}
  	}
	%>

    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

 </body>

</html>