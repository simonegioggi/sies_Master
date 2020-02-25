<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.lang.String" %>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sius.tenore.model.TenoreModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="depositoDecretoMotivazioni" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori" scope="request" class="java.util.Vector"/>

<jsp:useBean id="contenuto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"    scope="request" class="java.lang.String"/>

<jsp:useBean id="LicenzePeriodi"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="CodTipoProvvedimento" scope="request" class="java.lang.String"/>

<% 
	String[] esiti = (String[])request.getAttribute("esiti");

	// Variabili
	Date data_emissione =null;
	Date data_deposito = null;
	TenoreModel[] lTenori = null;
	String lAction = "siap.sius.provvedimento.action.ActModificaProvvedimento";
	String lIdEvento = "";
	String lIdOrdinanza = "";
	String lIdDecreto = "";
	String UlterioreDesc="";
	String tipoProvv="";
    
	int NumDate = ICostantiLibertaAnticipata.NUM_PERIODI;		// 10
	int NumColonne = 1;
	int NumRighe = 1;
	int NumTotale = 1;

	int NumTotaleSemestri = NumRighe*NumColonne;			// 1
//--

	int NumCheck = 5;         /* numero complessivo dei check box */

  int IndPerGG =  0;     /* indice del check box relativo al Periodo Riduzione Pena */
  int IndPerEU =  1;     /* indice del check box relativo alla somma Euro per risarcimento */
  int IndRig = 2;  		/* indice del check box relativo a Periodi rigettati */
  int IndIna = 3;  		/* indice del check box relativo a Periodi inammissibili */
  int IndNlp = 4;  		/* indice del check box relativo a Periodi NLP */
//--  

	int TotggLA	= 0;		/* totali gg concessi per Riduzione Pena  */ 
	String SommaInt ="";
	String SommaDec = "";
	String SommaTot = "";
	Boolean periodiGG = false;
	Boolean TotSommaInt = false;

//Estrazione della data minima: data udienza oppure iscrizione fascicolo
	String data1;
	if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
 		data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
	else
		data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria(),"dd/MM/yyyy");
 
 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 //siesLogger.debug(" Modifica Decreto/Ordinanza CEDU --------------->  ");
 		
 // Switch fra il caso Decreto e quello  Ordinanza
if(CodTipoProvvedimento.compareTo("02") == 0)
{	
	tipoProvv = "Decreto";
	// Modifica Decreto
	data_emissione = depositoDecretoMotivazioni.getDepositoDecreto().getDataEmissione();
	data_deposito = depositoDecretoMotivazioni.getDepositoDecreto().getDataDeposito();
	lTenori = (TenoreModel[])  tenori.toArray(new TenoreModel[0]);
	lIdEvento = depositoDecretoMotivazioni.getEvento().getIdEvento().toString();
	lIdOrdinanza = "";
	lIdDecreto = depositoDecretoMotivazioni.getDepositoDecreto().getIdDepositoDecreto().toString();
	//lAction = "siap.sius.provvedimento.action.ActModificaProvvedimento";
	if(depositoDecretoMotivazioni.getDepositoDecreto().getNote() != null)
		UlterioreDesc= depositoDecretoMotivazioni.getDepositoDecreto().getNote().trim();
	
}
else
{	
	// Modifica Ordinanza
	data_emissione = datiOrdinanza.getEvento().getDataEmissione();
	data_deposito = datiOrdinanza.getOrdinanza().getDataDeposito();
	lTenori = datiOrdinanza.getTenori();
	lIdEvento = datiOrdinanza.getEvento().getIdEvento().toString();
	lIdOrdinanza = datiOrdinanza.getOrdinanza().getIdDepositoOrdinanzaPc().toString();
	lIdDecreto = "";
	//lAction = "siap.sius.provvedimento.action.ActModificaProvvedimento";
	if(datiOrdinanza.getOrdinanza().getUlterioreDescrizione() != null)
		UlterioreDesc= datiOrdinanza.getOrdinanza().getUlterioreDescrizione().trim();
}	
 
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//siesLogger.debug(" Modifica Decreto/Ordinanza CEDU ---------------> tipoProvv = "+tipoProvv);
 
%>
<!-- ModificaOrdinanzaViolazioneCEDU -->	
<html>
 
  <head>
  
    <title>[S.I.E.S.] - Modifica Ordinanza/Decreto di Liberazione Anticipata</title>
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
	var giorni = new Array(NumTotale);        /* Array dei giorni totali concessi per Riduzione Pena  */
   
	var NumCheck = <%=NumCheck%>;             /* numero complessivo dei check box */
	
	var IndPerGG = <%=IndPerGG%>;
	var IndPerEU = <%=IndPerEU%>;
	var IndRig = <%=IndRig%>;
	var IndIna = <%=IndIna%>;
	var IndNlp = <%=IndNlp%>;

	var node;
	var flagConcesso = 'C';

	   var GiorniConcessi = 0;
	   var SalvaGiorniConcessi = 0;


	function init()
	{
		//alert("inizio");
		var i=0;
		for (i=0; i<NumTotale; i++)
	          giorni[i] = 0;
		
		var save_gg = document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_SALVA_GIORNI_CEDU %>.value;
		document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU %>.value = save_gg;
		
		var save_Euro_int = document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_SALVA_INTERO_CEDU %>.value;
		document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU %>.value = save_Euro_int;

		var save_Euro_dec = document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_SALVA_DECIMALE_CEDU %>.value;
		document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU %>.value = save_Euro_dec;
	
		AbilitaLA();

	}

	function AbilitaLA()
	{
        node=document.getElementById("comune");
        node.style.display='block';
        node=document.getElementById("periodo");
        node.style.display='block';
        node=document.getElementById("somma");
        node.style.display='block';
        node=document.getElementById("resto");
        node.style.display='block';
       
        AbilitaPeriodo();

	}
	
    function Chiusura()
    {
    	//	alert("Chiusura: inizio - NumCheck = "+NumCheck);
	      var retValue = true;
	      for (var i=0; i < NumCheck; i++)
	      {
		        if (document.ModificaOrdViolaCedu.gg[i].checked)
		        {
		              document.ModificaOrdViolaCedu.gg[i].checked = false;
		              retValue = ViewLayer(i);
		        }
	      }
	      
      	  return retValue;
    }
    
    // Disabilita i blocchi date vuoti 

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
      //  alert("DisabilitaDate: fine");
      	return;
    }

    var DataFine;
    var DataIni;
    /* Controllo  e conteggio date */
    function conteggioDate (id)
    {
    	//alert("conteggioDate - id = "+id);
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
	           //  alert ("Errore nelle date");
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
        }
        else
        {
	       	if (id < NumTotale)
        	{	
        		document.ModificaOrdViolaCedu.gg[id].value = 1; 
        	}	
        }          

        return retValue;
    
    }	// chiude function conteggioDate (id)

    /* Lettura  e controllo del periodo di posizione elem  */
    function leggiDate (elem)
    {
       var ret = true;
       var gg0 = FillDM(document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value);
       var mm0 = FillDM(document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value);
       var aa0 = document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value;
       var dataIni = gg0 + "/" + mm0 + "/" + aa0;
       var gg1 = FillDM(document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value);
       var mm1 = FillDM(document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value);
       var aa1 = document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value;
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
               /* OK Date presenti */
               DataIni = new Date(aa0, mm0-1, gg0);
               DataFine  = new Date(aa1, mm1-1, gg1);
               //alert("Data iniziale : " + DataIni.toString());
              // alert("Data finale : " + DataFine.toString());
       }
      return ret;
    }

    function checkDate(id)
    {
       //alert("check id : " + id);
      if (id == IndPerGG) {
      document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_PERIODI_CONCESSI_CEDU%>.checked = true;
      } else if (id == IndPerEU) {
      document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_EURO_CONCESSI_CEDU%>.checked = true;
      } else if (id == IndRig) {
      document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_RIGETTATI_CEDU%>.checked = true;
      } else if (id == IndIna) {
      document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_INAMMISSIBILI_CEDU%>.checked = true;
      } else if (id == IndNlp) {
      document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_NLP_CEDU%>.checked = true;
      }
      //alert("check id : " + id);
    }

    function uncheckDate(id)
    {
       //alert ("uncheck " + id);

      if (id == IndPerGG) {
      document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_PERIODI_CONCESSI_CEDU%>.checked = false;
      } else if (id == IndPerEU) {
      document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_EURO_CONCESSI_CEDU%>.checked = false;
      } else if (id == IndRig) {
      document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_RIGETTATI_CEDU%>.checked = false;
      } else if (id == IndIna) {
      document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_INAMMISSIBILI_CEDU%>.checked = false;
      } else if (id == IndNlp) {
      document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_NLP_CEDU%>.checked = false;
      }
       //alert ("uncheck " + id);
	}

    function IsCheckedDate(id)
    {
        var retValue = false;
       //alert ("IsCheckedDate " + id);

      if (id == IndPerGG) {
       if (document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_PERIODI_CONCESSI_CEDU%>.checked)
          retValue = true;
      }else if (id == IndPerEU) {
        if (document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_EURO_CONCESSI_CEDU%>.checked)
          retValue = true;
      }else if (id == IndRig) {
        if (document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_RIGETTATI_CEDU%>.checked)
          retValue = true;
      }else if (id == IndIna) {
        if (document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_INAMMISSIBILI_CEDU%>.checked)
          retValue = true;
      }else if (id == IndNlp) {
        if (document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_NLP_CEDU%>.checked)
          retValue = true;
      }
      //alert ("IsCheckedDate " + retValue);

      return retValue;
    }

    
    /* Abilita la modalità di selezione periodi concessi a periodo unico */
    function AbilitaPeriodo()
    {
    	// alert("C AbilitaPeriodo - inizio - "");
         var retValue = true;
	     retValue = Chiusura();
	     if (retValue)
	     {
	           node=document.getElementById("periodo");
	           node.style.display='block';
        	   node=document.getElementById("somma");
	           node.style.display='block';
	           node=document.getElementById("resto");
	           node.style.display='block';
	     }

         // alert("AbilitaPeriodo: fine");
         return retValue;
    }
    
    /* Visualizzazione del layer Date corrispondente alla posizione id   */
    function ViewLayer(id)
    {
     	// alert ("C ViewLayer id: " + id);
       // Check ON/OFF
       for (var i=0; i<NumCheck; i++)
       {
	         if (i != id && document.ModificaOrdViolaCedu.gg[id].checked)
	         { document.ModificaOrdViolaCedu.gg[i].disabled=true;}
	         else
	         { document.ModificaOrdViolaCedu.gg[i].disabled=false;}
       }
       
       // Apertura o chiusura del campo date e controllo correttezza date in chiusura */
       for (var i=0; i<NumCheck; i++)
       {
    	   	node=document.getElementById('L'+i);
          	if (i == id && document.ModificaOrdViolaCedu.gg[id].checked)
          	{ // apertura
            	// alert ("Sto aprendo - i = " + i);
        	 	node.style.display='block';
          	}
          	else
          	{
             	if (i == id)
             	{ // chiusura
                	//alert ("Sto chiudendo " + i);
	 				if (chkDateContigue(id) == false)
 					{
 					 	// Reinserisce i Check
      					for (var i=0; i<NumCheck; i++)
 			    	  	{
 			        		if (i == id)
 			        		{
 								document.ModificaOrdViolaCedu.gg[id].checked = true;
 			        		}
 			        		else
 			        		{
 								document.ModificaOrdViolaCedu.gg[i].disabled=true;
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
                     		document.ModificaOrdViolaCedu.gg[id].checked = true;

 							// disabilita gli altri Check
 	      					for (var i=0; i<NumCheck; i++)
 					    	{
 					    		if (i != id)
 					        	{
 									document.ModificaOrdViolaCedu.gg[i].disabled=true;
 					        	}
 					    	}

 	                    	return false;
                 		}
 						
 					}	// chiude else if (chkDateContigue(id) == false)
	 				
             }	// chiude if(i=id)
             	
             node.style.display='none';

          } // chiude la else di if (i == id && document.ModificaOrdViolaCedu.gg[id].checked)
 
        }	// chiude ciclo for (var i=0; i<NumCheck; i++)
       
        // Colore dei check
        var blue=true;
        node=document.getElementById('SL'+id);
        
        var elem = 0;
        elem=id*NumDate;
        for (var j=0; j<NumDate; j++, elem++)
        {
	          blue=blue && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value=="")  && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value=="")  && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value=="");
	          blue=blue && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value=="")  && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value=="")  && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value=="");
        }
        if (blue)
        {
          	node.style.color="Navy";
          	uncheckDate(id);
        }
        else
        {
          	node.style.color="Red";
          	checkDate(id);
        }

        return true;
        
    } 	// chiude function ViewLayer(id)

  //CONTROLLO DATE CONTIGUE
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
				var gg0 = document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value;

				if(gg0 != '')
				{
					riga = true;
				}
			}
			else
			{
				var gg0 = document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value;
				var gg0rigaprima = document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem-1].value;
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
	//FINE - CONTROLLO DATE CONTIGUE
	}

//  ---------------------- FINE -----------------------
	// STUB 21/07/2004 Controllo obbligatorietà esiti.
	
	function Verify() {
		var retValue = false;
		var lEsiti=document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
		//Verifica sulle date
		retValue = VerificaDate();
		if (retValue)
			retValue = false;
		else
		    return false;
		retValue = VerifyCombo(lEsiti,"Esito")
		if (retValue) {
			GiorniConcessi = document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU %>.value;
			SalvaGiorniConcessi = document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_SALVA_GIORNI_CEDU %>.value;
			RisarcimentoEuro = document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>.value;
			if (ComboConcede())	{
				// In caso di ALMENO 1 CONCESSIONE ENTRA QUI' e vengono controllati i gg concessi
				if (document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2070") {	
					if (RisarcimentoEuro != 0 && RisarcimentoEuro != "") {
						alert("Tipo Esito INCONGRUENTE con Somma Liquidata a Titolo Risarcimento !");
						document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>.focus();
						return false;
					}
				}
				if (document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2072") {
					if (GiorniConcessi != 0  && GiorniConcessi != "") {
						alert("Tipo Esito INCONGRUENTE con Giorni Riduzione Pena Concessi! ");
				        document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU%>.focus();
				        return false;
			        }
				}
				if (document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2070"
						|| document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2071") {
					if (GiorniConcessi == 0 || GiorniConcessi == "") {
						alert("Valorizzare Giorni Riduzione Pena Concessi!");
						document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU%>.focus();
						return false;
					}
            	}
				if (document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2072"
						|| document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2071") {
					if (RisarcimentoEuro == 0 || RisarcimentoEuro == "") {
						alert("Valorizzare Somma Liquidata a Titolo Risarcimento!");
						document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>.focus();
						return false;
					}
	            }
				if (document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2071") {
					var blueGG=true;
					for (var elem=0; elem<10; elem++) {
						blueGG=blueGG && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value=="")  && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value=="")  && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value=="");
						blueGG=blueGG && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value=="")  && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value=="")  && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value=="");
					}
					if (blueGG) {
	                 	alert ("Inserire Periodo di Riduzione Pena!");
						return false;
	                } else {
						var blueEU=true;
						for (var elem=10; elem<20; elem++) {
							blueEU=blueEU && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value=="")  && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value=="")  && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value=="");
							blueEU=blueEU && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value=="")  && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value=="")  && (document.ModificaOrdViolaCedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value=="");
          				}
           				if (blueEU) {
            				alert ("Inserire Periodo di Somma Liquidata per Risarcimento!");
							return false;
           				}
          			}
     	 		}
			}

	        // STUB 16/07/2008 Nel caso di Rigetto, Incompetenza, NDP/NLP, Inammissibilità,  va controllato che siano = 0 i gg Concessi.
			if (ComboRigetta() || ComboIncompetenza() || ComboNDPNLP() || ComboInammissibile()) {
				retValue = Chiusura();
				if (retValue) {
					retValue = InseritoPeriodo();
	        	}
            	if (document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2073"
            			|| document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2076"
            			|| document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2074"
            			|| document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2075") {
            		if (GiorniConcessi != 0 && GiorniConcessi != "") {
            			alert("Azzerare i Giorni Riduzione Concessi! ");
       			       	document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU%>.focus();
						return false;
					}
					if (RisarcimentoEuro != 0 && RisarcimentoEuro != "") {
						alert("Azzerare Somma Liquidata a Titolo Risarcimento! ");
						document.ModificaOrdViolaCedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>.focus();
						return false;
  					}
				}
			} // chiude if (ComboRigetta() || ComboIncompetenza() || ...

	        // Nel caso di Incompetenza non vengono inseriti periodi e
	        // viene inserito il Magistrato Competente
	        if (ComboIncompetenza()) {
          		init();
				return true;
			}
			// else
			// document.ModificaOrdViolaCedu.< %=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>.value = "";
        
			retValue = Chiusura();
	        if (retValue) {
	        	retValue = InseritoPeriodo();
		        if (!retValue) {
					// 10-11-2014 Su richiesta di Gasbarri si possono inserire Rigetti senza periodi :
					// Se Esito = Dichiara Incompetenza(2075) o Rigetta(2073) o Dichiara NDP / NLP(2074) o Dichiara Inammissibilità(2076)
					// e non ci sono periodi è TUTTO OK (non sono OBBLIGATORI).
					if (document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value != "2073"
							&& document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value != "2074"
							&& document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value != "2075"
							&& document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value != "2076") {
						// 20170922: [SG] non obbligatorietà ma warning per 2764 rigetto, 2765 inammissibilità, 2766 nlp/ndp
						if (document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2764"
								|| document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2765"
								|| document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2766") {
							if (!confirm("Nessun periodo inserito! Si vuole continuare?"))
			              		return false;
							else
								retValue = true;
						} else {
   	              			alert("Nessun periodo inserito!");
              				return false;
           		  		}
		          	}
	        	} // chiude if (!retValue)
      		} // chiude if (retValue)
      		return retValue;
		}
	}

 // Il confronto viene fatto con la descrizione e non con il codice perchè i codici sono molti.
    function ComboIncompetenza()
    {
      //alert("ComboIncompetenza : inizio");
	      var ritorno = false;
   	  	if (document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2075")
        {
	       	ritorno = true;
        }
      	return ritorno;
    }

    function ComboConcede()
    {
	    var ritorno = false;
        if ( document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2070" ||
        	document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2071" ||
        	document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2072" )
        {
          	ritorno = true;
        }
       return ritorno;
    }
 
    function ComboRigetta()
    {
	      var ritorno = false;
			if (document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2073" )
			{
					ritorno = true;
			}
	      return ritorno;
    }

    function ComboNDPNLP()
    {
	      var ritorno = false;
			if (document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2074" )
			{
					ritorno = true;
			}
	      return ritorno;
    }
 
    function ComboInammissibile()
    {
	      var ritorno = false;
			if (document.ModificaOrdViolaCedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2076" )
			{
				ritorno = true;
			}
	      return ritorno;
    }

    // Funzione di controllo per determinare se è stato inserito almeno un periodo.
    function InseritoPeriodo()
    {
    	//alert("InseritoPeriodo ");
        var i=0;
        var ok=0;
        for (i=0; i < NumCheck; i++)
        {
	         // Vengono disabilitati tutti i blocchi periodi vuoti
	          if(IsCheckedDate(i))
	          {
	        	  ok = 1;
	             //return true;
	          }
        }
        
        if(ok == 1)
        {
       	 	return true;
        } 	
       	else
       	{	
        	return false;
       	}	
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

<%	
 // Estrazione della data massima: data di deposito o data di sistema
 String data2;
 if( data_deposito != null)
	  data2 = DateUtils.getDateToString(data_deposito,"dd/MM/yyyy");
 else
  	  data2 = DateUtils.getSysDate("dd/MM/yyyy");
%>
 
    <script language="JavaScript">
    function VerificaDate()
    {
        // alert("VerificaDate");
        
        // data1 = Data Arrivo in cancelleria
        // data2 = data Deposito 
        var ritorno = true;
        var data_camera = '<%=data1%>';			
        var data_deposito = '<%=data2%>';
        var data_emissione = document.ModificaOrdViolaCedu.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModificaOrdViolaCedu.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModificaOrdViolaCedu.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
 
      // Controllo della data emissione.
     	// alert("data emissione ->" + data_emissione);

     	if (! ControllaData(data_emissione))
      	{
        	alert('Data emissione non valida!');
        	ritorno =  false;
      	}
      	// Controllo data di sistema >= Data Emissione .
      	else if( !CompareDate( data_emissione, data_deposito) )
      	{
	        alert("La data di emissione non può essere maggiore della Data di Sistema!");
	        ritorno =  false;
      	}
      // Controllo della data deposito <= data camera di consiglio
      // alert("data_camera ->" + data_camera);
      	else if ( !CompareDate( data_camera, data_emissione) )
      	{
	        alert("La data di emissione non può essere minore della Data Udienza!");
	        ritorno =  false;
      	}
     	
     	return ritorno;
    
    }	 
 
    </script>
    
  </head>
  
  <body class="corpo" onLoad="Javascript:init();" >

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ModificaOrdViolaCedu">
 
 <%       // Inizio nuova parte introdotta 20/4/2009 -
  		//		+ modifiche per Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014	%>
  		
<!-- 				PARTE PER INSERIRE LA DESCRIZIONE TOTALE DEI PERIODI CONCESSI E NON, UGUALE A QUELLA DEL DETTAGLIO   -->
<%  		
  int[] numDT  = {0,0,0,0,0};
  int[] nDT = {0,0,0,0,0};
  String[] titoloDT = new String[5];
  String[] codDT = {"C","C","R","I","N"};

  Iterator itxDT = LicenzePeriodi.iterator();
  // Conteggio delle licenze distinte per tipo
  while (itxDT.hasNext())
  {
      LicenzaPeriodiLibAnticipataModel lLicPerDT = (LicenzaPeriodiLibAnticipataModel) itxDT.next();
      if( lLicPerDT.getLicenza().getFlagConcesso().compareTo("C") == 0) 
      {
 	     	if(lLicPerDT.getLicenza().getCodTipoLicenza() != null && lLicPerDT.getLicenza().getCodTipoLicenza().compareTo("RD") == 0 )
         		numDT[0]++;
        	 else if(lLicPerDT.getLicenza().getCodTipoLicenza() != null && lLicPerDT.getLicenza().getCodTipoLicenza().compareTo("SL") == 0 ) 
       			numDT[1]++;	    	  
      }
      else if ( lLicPerDT.getLicenza().getFlagConcesso().compareTo("R") == 0)
          numDT[2]++;
      else if ( lLicPerDT.getLicenza().getFlagConcesso().compareTo("I")  == 0)
          numDT[3]++;
      else if ( lLicPerDT.getLicenza().getFlagConcesso().compareTo("N") == 0)
          numDT[4]++;

  }

  
// ------  
//  titoloDT[0] = "Periodi valutati per Riduzione Pena: " + numDT[0];
	titoloDT[0] = "Periodi valutati per Riduzione Pena: ";
//  titoloDT[1] = "Periodi valutati per liquidazione Somma: " + numDT[1];
	titoloDT[1] = "Periodi valutati per liquidazione Somma: ";
  titoloDT[2] = "Periodi non concessi Rigettati: " + numDT[2];
  titoloDT[3] = "Periodi non concessi Inammissibili: " + numDT[3];
  titoloDT[4] = "Periodi non concessi N.L.P./N.D.P.: " + numDT[4];

// - - -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-
//
// I 'PERIODI CONCESSI' SONO CALCOLATI FUORI CICLO nella parte  if(numDT[0] , numDT[1]> 0)
// Giorni Concessi di Riduzione pena 
// 

	PeriodoLibAnticipataModel[][] plam_arrayLA = new PeriodoLibAnticipataModel[NumCheck][NumDate];
	
	  // Array dei periodi (NumCheck = 5)
	  for (int jCheck=0; jCheck<NumCheck; jCheck++)
	  {
		  plam_arrayLA[jCheck] = null;
	  }

 if(numDT[0] > 0)
 {
	 	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 	//siesLogger.debug(" XXXX ----> ModificaOrdCEDU - Conteggio Giorni Concessi Riduzione pena Num[0]");
 		Iterator itxDT1 = LicenzePeriodi.iterator();
		while (itxDT1.hasNext())
		{
			LicenzaPeriodiLibAnticipataModel lLicPerConcDT1 = (LicenzaPeriodiLibAnticipataModel) itxDT1.next();
    		if( lLicPerConcDT1.getLicenza().getFlagConcesso().compareTo("C") == 0 )
    		{	
    			if(lLicPerConcDT1.getLicenza().getCodTipoLicenza() != null && lLicPerConcDT1.getLicenza().getCodTipoLicenza().compareTo("RD") == 0 )
    			{
    				if(lLicPerConcDT1.getLicenza().getNumeroGiorni() != null )
    				{	
    					TotggLA += lLicPerConcDT1.getLicenza().getNumeroGiorni().intValue(); 
%>
	    				<table cellspacing="2" cellpadding="2" width="90%">
					    	<tr>
	          					<td class="l" width="35%"> Totale Giorni Riduzione Pena Concessi  </td>
	          					<td class="l" ><font class="campo"><%=StringUtils.toStringJSP(lLicPerConcDT1.getLicenza().getNumeroGiorni(),"-" )%> </font></td>
					    	</tr>
					    </table>	
<%     			
	 		      		if(lLicPerConcDT1 != null && lLicPerConcDT1.getPeriodi() != null )
				        {
	 		      			 plam_arrayLA[0] = lLicPerConcDT1.getPeriodi();
	 		      			 periodiGG = true;
	 		      			 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 		      			 //siesLogger.debug("XXXXX ModificaOrdCEDU --> C Periodi Riduzione Pena - plam_arrayLA[0] = "+plam_arrayLA[0].toString()); 
%>
							<table cellspacing="2" cellpadding="2" width="90%">
				            	<tr>
				            		<td class="l" width="35%"><%=titoloDT[0]%></td>
				            		<td class="L">
			<%
						
						    PeriodoLibAnticipataModel[] ppDT1 = lLicPerConcDT1.getPeriodi();
				   			for (int i = 0; i < ppDT1.length; i++)
						    {
							%>
							           <font class="l">
						              		<%=DateUtils.getDateToString(ppDT1[i].getDataInizio(),"dd/MM/yyyy")%>-
						              		<%=DateUtils.getDateToString(ppDT1[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;&nbsp;
							          </font>
								<%
						     }
								%>
									</td>
						      </tr>
							</table>
							<br>					      
			<%
				       } // chiude if(lLicPerConcDT1.getPeriodi() != null
				         
    				}	// Chiude if(lLicPerConcDT1.getLicenza().getNumeroGiorni() != null )			         
	    			
   				}	// chiude if(lLicPerConcDT1.getLicenza().getCodTipoLicenza().compareTo("RD") == 0  

	   		} 	// chiude if( lLicPerConcDT1.getLicenza().getFlagConcesso().compareTo("C") == 0 )
		
		}	// chiude while

  }  // chiude if(num[0] > 0)
	  
//-----------------------------
//
// Somma Concessa a Risarcimento 
//  
  if(numDT[1] > 0)
  {
	  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  	//siesLogger.debug(" XXXX ----> ModificaOrdCEDU - Conteggio Somma liquidata per Risarcimento Num[1]");
  		Iterator itxDT2 = LicenzePeriodi.iterator();
 		while (itxDT2.hasNext())
 		{
 			LicenzaPeriodiLibAnticipataModel lLicPerConcDT2 = (LicenzaPeriodiLibAnticipataModel) itxDT2.next();
     		if( lLicPerConcDT2.getLicenza().getFlagConcesso().compareTo("C") == 0 )
     		{	
     			if(lLicPerConcDT2.getLicenza().getCodTipoLicenza() != null && lLicPerConcDT2.getLicenza().getCodTipoLicenza().compareTo("SL") == 0 )
     			{
		    		if(lLicPerConcDT2.getLicenza().getSommaRisarcDanni() != null &&
		    			lLicPerConcDT2.getLicenza().getSommaRisarcDanni().intValue() > 0)
		    		{	
		    			SommaTot = lLicPerConcDT2.getLicenza().getSommaRisarcDanni().toString();
    					int punto = SommaTot.indexOf(".");
    					if(punto > 0 )
    					{	
							SommaInt = SommaTot.substring(0, punto);
							SommaDec = SommaTot.substring(punto+1);
    					}
    					else
    					{
    						SommaInt = SommaTot;
    					}
		%>
	     				<table cellspacing="2" cellpadding="2" width="90%">
	 				    	<tr>
	           					<td class="l" width="35%"> Somma Liquidata a Titolo Risarcimento Danno:  </td>
	           					<td class="l" ><font class="campo"><%=StringUtils.toStringJSP(lLicPerConcDT2.getLicenza().getSommaRisarcDanni() )%> &nbsp;&euro;</font></td>
	 				    	</tr>
	 				    </table>
	 <% 		      	if(lLicPerConcDT2 != null && lLicPerConcDT2.getPeriodi() != null )
	 			        {
			 				plam_arrayLA[1] = lLicPerConcDT2.getPeriodi();
			 				TotSommaInt = true;
							 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							 //siesLogger.debug("XXXXX ModificaOrdCEDU --> C Periodi Somma liquidata per Risarcimento - plam_arrayLA[1] = "+plam_arrayLA[1].toString()); 
 %>
	 						<table cellspacing="2" cellpadding="2" width="90%">
	 			            	<tr>
	 			            		<td class="l" width="35%"><%=titoloDT[1]%></td>
	 			            		<td class="L">
	 		<%
	 					    PeriodoLibAnticipataModel[] ppDT2 = lLicPerConcDT2.getPeriodi();
	 			   			for (int i = 0; i < ppDT2.length; i++)
	 					    {
	 						%>
	 						           <font class="l">
	 					              		<%=DateUtils.getDateToString(ppDT2[i].getDataInizio(),"dd/MM/yyyy")%>-
	 					              		<%=DateUtils.getDateToString(ppDT2[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;&nbsp;
	 						          </font>
	 							<%
	 					     }
	 							%>
	 								</td>
	 					      </tr>
	 						</table>
	 						<br>					      
	 		<%
	 			         } // chiude if(lLicPerConcDT2.getPeriodi() != null
	 			         
		    		}	 // Chiude if(lLicPerConcDT2.getLicenza().getAnnotazione() != null)			         
 	    			
    			}	// chiude if(lLicPerConcDT2.getLicenza().getCodTipoLicenza().compareTo("SL") == 0  

 	   		} 	// chiude if( lLicPerConcDT2.getLicenza().getFlagConcesso().compareTo("C") == 0 )
 		
 		}	// chiude while

   }  // chiude if(num[1] > 0)	  
  
// - -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	 
//
//  CICLO FOR PER I CASI 'Periodi non concessi':RIGETTATI,INAMMISSIBULI E N.L.P./N.D.P.'(numDT[2,3,4] / Titolo[2,3,4])
//
  for (int k= 2; k < 5; k++)
  {
%>
	    <table cellspacing="2" cellpadding="2" width="30%">
	    	<tr> <td> <br></td></tr>
	    	<tr>
	        	<td class="Titolo" colspan=6 style="text-align:left"> <%=titoloDT[k]%><td>
	    	</tr>
	    </table>
   
<%  	if (numDT[k] > 0)
	    {
			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			//siesLogger.debug(" XXXX ----> ModificaOrdCEDU - Conteggio Periodi NON Concessi Num[2,3,4]");
      		Iterator itxCDT = LicenzePeriodi.iterator();
%>
	  		<table cellspacing="2" cellpadding="2" width="90%">
<%
      		while (itxCDT.hasNext())
      		{
       			LicenzaPeriodiLibAnticipataModel lLicPerConcCDT = (LicenzaPeriodiLibAnticipataModel) itxCDT.next();
        		if( lLicPerConcCDT.getLicenza().getFlagConcesso().compareTo(codDT[k]) == 0)
        		{
	      			if( lLicPerConcCDT != null && lLicPerConcCDT.getPeriodi() != null )
	      			{
		      			 plam_arrayLA[k] = lLicPerConcCDT.getPeriodi();
		      			 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		      			 //siesLogger.debug("XXXXX ModificaOrdCEDU --> C Periodi Non Concessi - k = "+k+" - plam_arrayLA[k] = "+plam_arrayLA[k].toString()); 
	      				
			   				nDT[k]++;
	           				PeriodoLibAnticipataModel[] pCDT = lLicPerConcCDT.getPeriodi();
%>
			            	<tr>
			            		<td class="L" width="3%">
			            			<font class="l"><%=nDT[k]%>)</font>
			            		</td>
			            		<td class="L">
<%
				           for (int i = 0; i < pCDT.length; i++)
				           {
			%>
					           	<font class="l">
					              <%=DateUtils.getDateToString(pCDT[i].getDataInizio(),"dd/MM/yyyy")%>-
					              <%=DateUtils.getDateToString(pCDT[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;&nbsp;
					          	</font>
			<%
	           			 	}
			%>
								</td>
		            		</tr>
				<%
				
	      			} // chiude if(lLicPerConcCDT.getPeriodi() != null 
			      			
				}	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().com
      	
      		}	// chiude while (itxC.hasNext())
%>
         	</table>

<%  	}  // chiude if (num[k] > 0)
 
  }	// chiude for (int k= 1; k < 5; k++)

%>	  
<br>
<%  	
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
/*	 siesLogger.debug("XXXXXXX --> ModificaOrdCEDU -  SommaTot = "+SommaTot ); 
	 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.debug("XXXXXXX --> ModificaOrdCEDU -  SommaInt = "+SommaInt ); 
	 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.debug("XXXXXXX --> ModificaOrdCEDU -  SommaDec = "+SommaDec ); 
	 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.debug("XXXXXXX --> ModificaOrdCEDU -  TotggLA = "+TotggLA );
*/
%>

<!-- 																																			 -->					 
					 
    <table cellspacing="2" cellpadding="2"   width=95%>
     <tr >
        <td class="Titolo" colspan=8 ><font class="label"> Dati Modificabili </font></td>
    </tr>
    </table>
    <br>

      <table cellspacing="2" cellpadding="2"   width=95%>
	    <tr>
	      <td class="l">Data Emissione<font class="ob"> (*)</font></td>
	      <td class="L">
	        <input value="<%=DateUtils.getDateToString(data_emissione,"dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
	        <input value="<%=DateUtils.getDateToString(data_emissione,"MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
	        <input value="<%=DateUtils.getDateToString(data_emissione,"yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
	      </td>
	    </tr>
     </table>
    
       <br>
    <table cellspacing="4" cellpadding="4"   width=95%>
    	<tr>
        	<td class="Titolo" colspan=2 width=45%> Oggetto </td>
        	<td>&nbsp;</td>
        	<td class="Titolo" colspan=2 width=50%> specificare esito per ciascuno oggetto: </td>
    	</tr>
    <%
   for (int i=0; i< lTenori.length;i++)
   {
    %>
	       <tr>
	        <td class="l"  colspan=2 width="45%">
	           <input Title="Oggetto" 					  name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" 	value="<%=lTenori[i].getDescrOggettoTenore()%>"  readonly size=70%>
	           <input Title="ID Tenore" 	type="hidden" name="<%=ICostantiTenore.CAMPO_ID_TENORE %>"	 			value="<%=lTenori[i].getIdTenore().toString()%>" >
	           <input Title="Cod Oggetto" 	type="hidden" name="<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" 	value="<%=lTenori[i].getCodOggettoTenore()%>" >
	        </td>
	        <td>&nbsp;</td>
	        <td class="l"  colspan=2 width="50%">
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
    		<td class="l" colspan="2" width="60%" >
    		<TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" 
    				cols="80" rows="4" ><%=UlterioreDesc %></textarea></td>
		</tr>
    </table> 
    <br>
    <%
   for (int indTeno = 0; indTeno < lTenori.length; indTeno++)
   {
	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  // siesLogger.debug(" ------------------------------------------------> INIZIO GIRO - indTeno = " + indTeno);
	  if(lTenori[indTeno].getCodOggettoTenore().compareTo("9027") == 0)
		{ 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				//siesLogger.debug("tenore oggetto 9027 - Violazione CEDU " );
		%>
		
	<!--  	> > >  		INIZIO PARTE CON LE DIV PER 	LIBERAZIONE ANTICIPATA normale ( L.A.)		  	< < < 	-->	
		
			<div id="comune" style="position: relative; top: 0; left: 0;" >
				<div id="periodo" style="position: relative; top: 0; left: 0; display:none; ">			
			    	<table width=70%>
			      		<tr>
			      			<td class="l" style="width:300px" >Riduzione Pena: Totale giorni Riduzione concessi </td>
			      			<td class="l" style="width:180px">
			        			<input type="text" size="5" maxlength="4" style="text-align:right" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU%>" value="" ONKEYPRESS="return TicTabNumField(this,event)">
			      			</td>
		      	<%		if(periodiGG) 
			      	  	{	%>		
			       			<td class="l" ><span id="SL<%=IndPerGG%>" style="color=red;font-weight:bold;">Periodi</span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndPerGG%>');"></td>
			    <%		}
		      			else
		      			{	%>
		      				<td class="l" ><span id="SL<%=IndPerGG%>" style="color=navy;font-weight:bold;">Periodi</span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndPerGG%>');"></td>
		      	<%		} %>			   			
			      		</tr>
			    	</table>
				</div>	<!--  chiude DIV = "periodo" -->
				
				<br>
				
				<div id="somma" style="position: relative; top: 0; left: 0; display:none; ">			
			    	<table width=70%>
			      		<tr>
			      			<td class="l" style="width:300px" >Somma Liquidata a titolo Risarcimento: Euro</td>
	      	  				<td class="l" style="width:180px">
								<input type="text" maxlength="9" size="9" style="text-align:right" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>" value="" ONKEYPRESS="return TicTabNumField(this,event)">
								<strong>&nbsp;,&nbsp;</strong>
								<input type="text" maxlength="2" size="2"  name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU%>" value="" ONKEYPRESS="return TicTabNumField(this,event)">
							</td>
		      	<%		if(TotSommaInt) 
			      	  	{	%>		
			       			<td class="l" ><span id="SL<%=IndPerEU%>" style="color=red;font-weight:bold;">Periodi</span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndPerEU%>');"></td>
			    <%		}
		      			else
		      			{	%>
		      				<td class="l" ><span id="SL<%=IndPerEU%>" style="color=navy;font-weight:bold;">Periodi</span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndPerEU%>');"></td>
		      	<%		} %>			   			
			      		</tr>
			    	</table>
				</div>	<!--  chiude DIV = "somma" -->	
							
			<%
			int jDate = 0;
			String iChecked = "";
			
			for ( int t = 0; t < NumCheck; t++)
			{
			
				iChecked = "";
				if (plam_arrayLA[t] != null)
				{
					// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					//siesLogger.debug("--------------> C Plam_arrayLA[t] NON nullo -  ichecked = t = "+t ); 
					iChecked = "checked";
				}	
					
			%>
			<div id="L<%=t%>" style="position: relative; top: -80; left: 600; display:none;" >
			  <table>
			   <!-- COSTRUZIONE DEI CAMPI DATA DAL - AL -->
			   <% 
			   
			    for (int k=0; k<NumDate; k++)
				{
			    	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			    	//siesLogger.debug("-----------------> t = "+t+" - k = "+k+" - plam_arrayLA[t]lengh = "+plam_arrayLA[t]);
				   %>
			    <tr>
			      <td class=l>
				  	Dal
				  	<% 	
				 
				  	if ( ( ( (t == (IndPerGG)) && (numDT[0] > 0)) && (plam_arrayLA[t] != null) && (k < plam_arrayLA[t].length) )  ||
					 	 (   (t == (IndPerEU)) && (numDT[1] > 0) && (plam_arrayLA[t] != null) && (k < plam_arrayLA[t].length) ) ||
					 	 (   (t == (IndRig)) && (numDT[2] > 0) && (plam_arrayLA[t] != null) && (k < plam_arrayLA[t].length) ) ||
					 	 (   (t == (IndIna)) && (numDT[3] > 0) && (plam_arrayLA[t] != null) && (k < plam_arrayLA[t].length) ) ||
					 	 (   (t == (IndNlp)) && (numDT[4] > 0) && (plam_arrayLA[t] != null) && (k < plam_arrayLA[t].length) )  )	
				 	{ 

			%>
							<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>" maxlength="2" size="2" value="<%=DateUtils.getDateToString(plam_arrayLA[t][k].getDataInizio(),"dd")%>" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>" maxlength="2" size="2" value="<%=DateUtils.getDateToString(plam_arrayLA[t][k].getDataInizio(),"MM")%>" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>" maxlength="4" size="4" value="<%=DateUtils.getDateToString(plam_arrayLA[t][k].getDataInizio(),"yyyy")%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
					     	&nbsp;&nbsp;
					     	Al
							<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>" maxlength="2" size="2" value="<%=DateUtils.getDateToString(plam_arrayLA[t][k].getDataFine(),"dd")%>" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>" maxlength="2" size="2" value="<%=DateUtils.getDateToString(plam_arrayLA[t][k].getDataFine(),"MM")%>" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>" maxlength="4" size="4" value="<%=DateUtils.getDateToString(plam_arrayLA[t][k].getDataFine(),"yyyy")%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
			      <% }
			         else
			         { %>
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
			     <%  } %>
			     
			      </td>
			    </tr>
		  
		  <%	}%>
			 </table>
			     <%
			      if (t == IndPerGG )
			     {
			     %>
			    <input type=checkbox name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_PERIODI_CONCESSI_CEDU%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (t == IndPerEU)
			     {%>
			    <input type=checkbox name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_EURO_CONCESSI_CEDU%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (t == (IndRig))
			     {%>
			    <input type=checkbox name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_RIGETTATI_CEDU %>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (t == ( IndIna) )
			     {%>
			    <input type=checkbox name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_INAMMISSIBILI_CEDU %>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (t == ( IndNlp) )
			     {%>
			    <input type=checkbox name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_NLP_CEDU%>" value=1 <%=iChecked%> style="display:none;">
			  <% } %>
			  
			</div>		<!--  chiude DIV id="L< %=i%>" -->
		
		<%	 }%>
			
			</div>	<!--  chiude DIV id="comune" -->
			
			<br>
			
			<div id="resto" style="position: relative; top: 0; left: 0;" >
			    <table cellspacing="2" cellpadding="2" width=35%>
			    	<tr>
			        	<td class="Titolo" colspan=6> Periodi non concessi:&nbsp;&nbsp;&nbsp; </td>
			    	</tr>
			    </table>

			    <table width=35%>
			      <% if (numDT[IndRig] > 0)  { %>
			      <td width=3%><span id="SL<%=IndRig%>" style="color=red;font-weight:bold;">Rigettati    </span> <input type=checkbox name=gg value=2 onclick="Javascript:ViewLayer('<%=IndRig%>');"></td>
			<%     }
			       else { %>
			       <td width=3%><span id="SL<%=IndRig%>" style="color=navy;font-weight:bold;">Rigettati    </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndRig%>');"></td>
			       <%     }  %> 
			      <% if (numDT[IndIna] > 0)  { %>
			      <td width=3%><span id="SL<%=IndIna%>" style="color=red;font-weight:bold;">Inammissibili    </span> <input type=checkbox name=gg value=2 onclick="Javascript:ViewLayer('<%=IndIna%>');"></td>
			<%     }
			       else { %>
			       <td width=3%><span id="SL<%=IndIna%>" style="color=navy;font-weight:bold;">Inammissibili    </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndIna%>');"></td>
			       <%     }  %> 
			      <% if (numDT[IndNlp] > 0)  { %>
			      <td width=3%><span id="SL<%=IndNlp%>" style="color=red;font-weight:bold;">N.L.P./N.D.P.  </span> <input type=checkbox name=gg value=2 onclick="Javascript:ViewLayer('<%=IndNlp%>');"></td>
			<%     }
			       else { %>
			       <td width=3%><span id="SL<%=IndNlp%>" style="color=navy;font-weight:bold;">N.L.P./N.D.P.  </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndNlp%>');"></td>
			       <%     }  %>     
			   </table>
			</div>		<!-- Chiude DIV id='resto' -->
			   
<%		}
			
   }	// chiude ciclo for (int i=0; i< lTenori.length;i++)
    %>
  
  <br><br>
	<table cellspacing="2" cellpadding="2" style="width: 90%;">
	   <tr>
	     <td class="l">In caso di Incompetenza indicare l'Ufficio di Sorveglianza destinatario </td>
	     <td class="l">
	        <input Title="UfficioDiSorveglianza" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>" value="" size=35 >
		        <a href="Javascript:ListaUDS('ModificaOrdViolaCedu','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>');">
		        <img src="/images/filefolder.gif" border=0></a>
	     </td>
	   </tr>
	   <tr> <td>&nbsp;</td> </tr>
  </table>
  
  <table>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>
 
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="VC" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC%>" value="<%=lIdOrdinanza%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO%>" value="<%=lIdDecreto%>" >
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lIdEvento%>" >
   
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_SALVA_GIORNI_CEDU%>" value="<%=TotggLA %>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_SALVA_INTERO_CEDU%>" value="<%=SommaInt %>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_SALVA_DECIMALE_CEDU%>" value="<%=SommaDec %>" >

  <!--  /div>	-->

<%-- // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger() --%>
<%			siesLogger.debug("--> C Fine Modifica  ");    %>

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("ModificaOrdViolaCedu");


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
				mex ="Periodi Non Concessi: Rigettati \\n\\n";
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

		 cont = cont + 1;
		}
  	}
	%>

    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

 </body>

</html>