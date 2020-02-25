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
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="contenuto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"    scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"  scope="request" class="java.util.Date"/>

<jsp:useBean id="decreto"     	scope="request" class="java.lang.String"/>
<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");

	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficio = "";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
	} else {
		labelUfficio = "Ufficio di Sorveglianza";
	}

String lAction = new String();
if(decreto.compareTo("SI") == 0)
	lAction = "siap.sius.depositodecreto.action.ActInserisciDecretoViolazioneCEDU";
else
  lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaViolazioneCEDU";

//-----------------------------------------------------------------------  
	int NumColonne = 1;
	int NumRighe = 1;
	int NumTotale = 1;
  	int NumDate = ICostantiLibertaAnticipata.NUM_PERIODI;	 				// 10

  	int NumTotaleSemestri = NumRighe*NumColonne;							// 1
//------------------------------------------------------------------------  
 
	int NumCheck = 5;         /* numero complessivo dei check box */

  int IndPerGG =  0;     /* indice del check box relativo al Periodo Riduzione Pena */
  int IndPerEU =  1;     /* indice del check box relativo alla somma Euro per risarcimento  */
  int IndRig = 2;  		/* indice del check box relativo a Periodi rigettati */
  int IndIna = 3;  		/* indice del check box relativo a Periodi inammissibili */
  int IndNlp = 4;  		/* indice del check box relativo a Periodi NLP */
  
%>
<!-- InserisciOrdinanzaViolazioneCEDU -->
<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza Rimedi Risarcitori per Violazione ART. 3 CEDU</title>
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

   function init()
   {
     // alert("init ");
      var i=0;

      for (i=0; i<NumTotale; i++)
          giorni[i] = 0;

      for (i=0; i<NumCheck; i++)
           uncheckDate(i);

      	AbilitaLA();
   }

    // Chiusura dell'eventuale blocco aperto
    function Chiusura()
    {
	      var retValue = true;
	      for (var i=0; i < NumCheck; i++)
	      {
		        if (document.InserisciOrdinanzaArt3Cedu.gg[i].checked)
		        {
		              document.InserisciOrdinanzaArt3Cedu.gg[i].checked = false;
		              retValue = ViewLayer(i);
		        }
	      }
	      
	      return retValue;
    }

// -----------------------------------------------------------------------    
    
    // Disabilita i blocchi date vuoti 

    function DisabilitaDate()
    {
      //   alert("DisabilitaDate: inizio");
        var i=0;
        for (i=0; i < NumCheck; i++)
        {
         // Vengono disabilitati tutti i blocchi checkBox periodi vuoti
	          if(!IsCheckedDate(i))
	          {
	             node=document.getElementById('L'+i);
	             node.disabled = true;
	 
	          }
        }
        
      return;
   
    }	// chiude DisabilitaDate
    

    
// -----------------------------------------------------------------------------------

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

       return retValue;
   }

/* Lettura  e controllo del periodo di posizione elem  */
   function leggiDate (elem)
   {
      var ret = true;
      var gg0 = FillDM(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value);
      var mm0 = FillDM(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value);
      var aa0 = document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value;
      var dataIni = gg0 + "/" + mm0 + "/" + aa0;
      var gg1 = FillDM(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value);
      var mm1 = FillDM(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value);
      var aa1 = document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value;
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

//	----------------------------------------------------------------------

    function checkDate(id)
    {
       //alert("check id : " + id);
	      if (id == IndPerGG) {
	      document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_PERIODI_CONCESSI_CEDU%>.checked = true;
	      } else if (id == IndPerEU) {
	      document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_EURO_CONCESSI_CEDU%>.checked = true;
	      } else if (id == IndRig) {
	      document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_RIGETTATI_CEDU%>.checked = true;
	      } else if (id == IndIna) {
	      document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_INAMMISSIBILI_CEDU%>.checked = true;
	      } else if (id == IndNlp) {
	      document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_NLP_CEDU%>.checked = true;
	      }

    }

    function uncheckDate(id)
    {
       //alert ("uncheck " + id);

	      if (id == IndPerGG) {
	      document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_PERIODI_CONCESSI_CEDU%>.checked = false;
	      } else if (id == IndPerEU) {
	      document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_EURO_CONCESSI_CEDU%>.checked = false;
	      } else if (id == IndRig) {
	      document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_RIGETTATI_CEDU%>.checked = false;
	      } else if (id == IndIna) {
	      document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_INAMMISSIBILI_CEDU%>.checked = false;
	      } else if (id == IndNlp) {
	      document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_NLP_CEDU%>.checked = false;
	      }

	}

    function IsCheckedDate(id)
    {
        var retValue = false;
       //alert ("IsCheckedDate " + id);

	      if (id == IndPerGG) {
	       if (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_PERIODI_CONCESSI_CEDU%>.checked)
	          retValue = true;
	      }else if (id == IndPerEU) {
	        if (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_EURO_CONCESSI_CEDU%>.checked)
	          retValue = true;
	      }else if (id == IndRig) {
	        if (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_RIGETTATI_CEDU%>.checked)
	          retValue = true;
	      }else if (id == IndIna) {
	        if (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_INAMMISSIBILI_CEDU%>.checked)
	          retValue = true;
	      }else if (id == IndNlp) {
	        if (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_NLP_CEDU%>.checked)
	          retValue = true;
	      }

      return retValue;
    }
//

/* Abilita la modalità di selezione periodi concessi a periodo unico */
    function AbilitaPeriodo()
    {
    	//  alert("AbilitaPeriodo");
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

         return retValue;
   
    } // chiude abilitaperiodo

    var node;

 /* Visualizzazione del layer Date  */
    function ViewLayer(id)
    {

       // Check ON/OFF
       for (var i=0; i<NumCheck; i++)
       {
         if (i != id && document.InserisciOrdinanzaArt3Cedu.gg[id].checked)
         { document.InserisciOrdinanzaArt3Cedu.gg[i].disabled=true;}
         else
         { document.InserisciOrdinanzaArt3Cedu.gg[i].disabled=false;}
       }
       // Apertura o chiusura del campo date e controllo correttezza date in chiusura */
       for (var i=0; i<NumCheck; i++)
       {
          node=document.getElementById('L'+i);
          if (i == id && document.InserisciOrdinanzaArt3Cedu.gg[id].checked)
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
 							document.InserisciOrdinanzaArt3Cedu.gg[id].checked = true;
 			        	}
 			        	else
 			        	{
 							document.InserisciOrdinanzaArt3Cedu.gg[i].disabled=true;
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
                     	document.InserisciOrdinanzaArt3Cedu.gg[id].checked = true;

 	      				for (var i=0; i<NumCheck; i++)
 					    {
 					    	if (i != id)
 					        {
 								document.InserisciOrdinanzaArt3Cedu.gg[i].disabled=true;
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
          blue=blue && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value=="")  && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value=="")  && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value=="");
          blue=blue && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value=="")  && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value=="")  && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value=="");
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
        
        return true;
    
    } // chiude ViewLayer
//
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
				var gg0 = document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value;

				if(gg0 != '')
				{
					riga = true;
				}
			}
			else
			{
				var gg0 = document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value;
				var gg0rigaprima = document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem-1].value;
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
		// alert("verify");
          var retValue = false;
          var lEsiti=document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
          var GiorniConcessi = 0;
          var RisarcimentoEuro = 0;
 
         retValue = VerifyCombo(lEsiti,"Esito")
         if (retValue)
         {
        	 
	            GiorniConcessi = document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU%>.value;
	            RisarcimentoEuro = document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>.value;

	            if(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2070" )
	        	{	
	            	if (RisarcimentoEuro != 0 && RisarcimentoEuro != "" ) 
			        {
				         alert("Tipo Esito INCONGRUENTE con Somma Liquidata a Titolo Risarcimento !");
				         document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>.focus();
				         return false;
			        }
	            }
	            
	            if(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2072" )
	        	{	
	            	if (GiorniConcessi != 0  && GiorniConcessi != "" ) 
		            {
				         alert("Tipo Esito INCONGRUENTE con Giorni Riduzione Pena Concessi! ");
				         document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU%>.focus();
				         return false;
			        }
	            }
	            
	            if(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2070" || 
        			document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2071" )
        		{	
		              if (GiorniConcessi == 0 || GiorniConcessi == ""  ) 
		              {
				            alert("Valorizzare Giorni Riduzione Pena Concessi! ");
				            document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU%>.focus();
				            return false;
		              }
            	}
	            
	            if(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2072" || 
	        		document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2071" )
	        	{	
			    	if (RisarcimentoEuro == 0 || RisarcimentoEuro == "" ) 
			        {
					            alert("Valorizzare Somma Liquidata a Titolo Risarcimento! ");
					            document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>.focus();
					            return false;
			        }
	            }
	            
	            if(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2071" )
	            {
	                var blueGG=true;
	                for (var elem=0; elem<10; elem++)
	                {
	                  	blueGG=blueGG && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value=="")  && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value=="")  && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value=="");
	                  	blueGG=blueGG && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value=="")  && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value=="")  && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value=="");
	                }
	                if (blueGG)
	                {
	                 	alert ("Inserire Periodo di Riduzione Pena!");
						return false;
	                }
	                else
	                {
		                var blueEU=true;
		                for (var elem=10; elem<20; elem++)
		                {
		                  	blueEU=blueEU && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value=="")  && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value=="")  && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value=="");
		                  	blueEU=blueEU && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value=="")  && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value=="")  && (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value=="");
		                }
		                if (blueEU)
		                {
		                 	alert ("Inserire Periodo di Somma Liquidata per Risarcimento!");
							return false;
		                }	                	
	                }
	
	            		
	            }
	            
            	if (ComboRigetta() || ComboIncompetenza() || ComboNDPNLP() || ComboInammissibile() )
            	{
            		retValue = Chiusura();
            		if (retValue)
            		{
              			retValue = InseritoPeriodo();
            		}

            		if(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2073" || 
                		document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2076" || 
                		document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2074" || 
                		document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "2075"  )
                	{
            			if(GiorniConcessi != 0 && GiorniConcessi != "")
          			   	{
          			       alert("Azzerare i Giorni Riduzione Pena Concessi! ");
          			       document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU%>.focus();
          			       return false;
          			   	}
            			
            			if(RisarcimentoEuro != 0 && RisarcimentoEuro != "")
          			   	{

          			       alert("Azzerare Somma Liquidata a Titolo Risarcimento! ");
          			       document.InserisciOrdinanzaArt3Cedu.<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>.focus();
          			       return false;
          			   	}
             				
                	}	
            		
            	}	// CHIUDE if(ComboRigetta() || ComboIncompetenza() || ComboNDPNLP() || ComboInammissibile() )

 // Nel caso di Incompetenza non vengono inseriti periodi e viene inserito il Magistrato Competente
	            
 				if (ComboIncompetenza())
	            {
		              init();
		              return true;
	            }
	         //   else
	         //    	document.InserisciOrdinanzaArt3Cedu.< %=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>.value = "";

            	retValue = Chiusura();
            	if (retValue)
            	{
		            retValue = InseritoPeriodo();
		            if(retValue)
              		{
              		}
              		else
            		{
              			if (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value != "2073" && 
           					document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value != "2074" &&
           					document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value != "2075" &&
           					document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value != "2076" )
              		  	{
						// 10-11-2014 Su richiesta di Gasbarri si possono inserire Rigetti senza periodi :
              			//Se Esito = Dichiara Incompetenza(2075) o Rigetta(2073) o Dichiara NDP / NLP(2074) o Dichiara Inammissibilità(2076)
	               		//	       e non ci sono periodi è TUTTO OK (non sono OBBLIGATORI),
	              			alert("Nessun periodo inserito !");
	              			return false;
              		  	}	
            		}
            	
            	} // chiude if retvalue1
            
          } // chiude if retvalue

          return retValue;
          
       }	// CHIUDE function Verify()


    function ComboIncompetenza()
    {
      //alert("ComboIncompetenza : inizio");
	      var ritorno = true;
		  if (document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value != "2075")
		  {
		       ritorno = false;
		  }
	      return ritorno;
    }

    function ComboRigetta()
    {
    	//alert("comborigetta");
      	  var ritorno = true;
    	  if(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value != "2073")
     	  {	
     			ritorno = false;
     	  }
    	  
      	  return ritorno;
    }

    function ComboNDPNLP()
    {
    	//alert("ComboNDPNLP");
      	var ritorno = true;
     	if(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value != "2074")
     	{	
     		ritorno = false;
     	}
     	
        return ritorno;
    }

    function ComboInammissibile()
    {
    	//alert("ComboInammissibile");
      	var ritorno = true;
	   	if(document.InserisciOrdinanzaArt3Cedu.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value != "2076")
	 	{	
	 		ritorno = false;
	 	}
	   	
      	return ritorno;
    }

    // Funzione di controllo per determinare se è stato inserito almeno un periodo.
    function InseritoPeriodo()
    {
    	//	alert(" InseritoPeriodo - inizio");
        var i=0;
 		for (i=0; i < NumCheck; i++)
		{
		   // Vengono disabilitati tutti i blocchi periodi vuoti
		   if(IsCheckedDate(i))
		   {
		        return true;
		    }
		}
    }	// chiude InseritoPeriodo()

// ---->

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
     
      var desktop;
      // Chiamata all'elenco degli UDS
      function ListaUDS(a_formname,a_fieldname,a_typename)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
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
      	<%	if(decreto.compareTo("SI") == 0)
      		{	%>
     				<font class="campo">Emissione Decreto di Rimedi Risarcitori Violazione Art.3 CEDU </font>&nbsp;
     	<%	}
      		else
      		{	%>
      				<font class="campo">Emissione Ordinanza di Rimedi Risarcitori Violazione Art.3 CEDU </font>&nbsp;
      	<%	} %>		
      		</td>
    	</tr>
    </table>
    <br>
    <table>
   	 	<tr>
       		<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    	</tr>
    </table>
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  	<%--  FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaArt3Cedu"--%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaArt3Cedu">
    <table width=35%>
   <tr>
     <td class="l" width=30%> Data Emissione</td>
     <td class="l" width=70%> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   </tr>
    </table>

    <tr> <td>&nbsp;</td> </tr>
    <table width="90%">
    <tr>
        <td class="Titolo" colspan=6 > Specificare esito per ciascun oggetto: </td>
    </tr>

    <tr>
        <td class="l" colspan=3 width="45%"> Oggetto </td>
        <td>&nbsp;</td>
        <td class="l" colspan=3 width="45%"> Esito   </td>
    </tr>
    <%
   for (int i=0; i< tenori.length;i++)
    {
    %>
       <tr>
        <td class="l"colspan=3 width="45%">
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=70%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
        <td>&nbsp;</td>	
        <td class="l"colspan=3 width="45%">
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
    
<!--  < < < < < <  INSERISCI L.A. - LIB. ANT. NORMALE 45gg 	> > > > > > >	-->

<br>
	<div id="comune" style="position: relative; top: 0; left: 0;" >
		<div id="periodo" style="position: relative; top: 0; left: 0; display:none; " >
	    	<!--  table cellspacing="2" cellpadding="2" width=26% -->
	    	<table width=70%>
	      		<tr>
	      			<td class="l" style="width:300px">Riduzione Pena: Totale giorni Riduzione concessi </td>
	      			<td class="l" style="width:180px">
	        			<input type="text" size="5" maxlength="4" style="text-align:right" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU%>" value="" ONKEYPRESS="return TicTabNumField(this,event)">
	      			</td>
	       			<td class="l" ><span id="SL<%=IndPerGG%>" style="color=navy;font-weight:bold;">Periodi</span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndPerGG%>');"></td>
	      		</tr>
	    	</table>
	  </div>
	
	  <div id="somma" style="position: relative; top: 0; left: 0; display:none; " >
	    	<table width=70%>
	      		<tr>
	      	  		<td class="l" style="width:300px">Somma Liquidata a titolo Risarcimento: Euro </td>
	      	  		<td class="l" style="width:180px">
						<input type="text" maxlength="7" size="9" style="text-align:right" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>" value="" ONKEYPRESS="return TicTabNumField(this,event)">
							<strong>&nbsp;,&nbsp;</strong>
						<input type="text" maxlength="2" size="2"  name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU%>" value="" ONKEYPRESS="return TicTabNumField(this,event)">
					</td>
	       	  		<td class="l" ><span id="SL<%=IndPerEU%>" style="color=navy;font-weight:bold;">Periodi</span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndPerEU%>');"></td>
	      		</tr>
	    	</table>
	  </div>
	  <br>
	 <div id="resto" style="position: relative; top: -10; left: 0;" >
    	<table cellspacing="2" cellpadding="2" width=35%>
    		<tr>
        		<td class="Titolo" colspan=6> Periodi non concessi:&nbsp;&nbsp;&nbsp; </td>
    		</tr>
    	</table>
    	<table width=35%>
    	<tr>	
      		<td width=3% nowrap ><span id="SL<%=IndRig%>" style="color=navy;font-weight:bold;">Rigettati </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndRig%>');"></td>
      		<td width=3% nowrap ><span id="SL<%=IndIna%>" style="color=navy;font-weight:bold;">Inammissibili </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndIna%>');"></td>
      		<td width=3% nowrap ><span id="SL<%=IndNlp%>" style="color=navy;font-weight:bold;">N.L.P./N.D.P. </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndNlp%>');"></td>
   		</tr>
   		</table>
	</div>
	
	<%
	for (int i=0; i<NumCheck; i++)
	{
	%>
	<div id="L<%=i%>" style="position: relative; top: -50; left: 60%; display:none;" >
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
	      if (i == (IndPerGG) )
	     {
	     %>
	    <input type=checkbox name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_PERIODI_CONCESSI_CEDU%>" value=1 style="display:none;">
	     <% } else if (i == (IndPerEU) )
	     {%>
	    <input type=checkbox name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_EURO_CONCESSI_CEDU%>" value=1 style="display:none;">
	     <% } else if (i == (IndRig) )
	     {%>
	    <input type=checkbox name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_RIGETTATI_CEDU%>" value=1 style="display:none;">
	     <% } else if (i == ( IndIna) )
	     {%>
	    <input type=checkbox name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_INAMMISSIBILI_CEDU%>" value=1 style="display:none;">
	     <% } else if (i == ( IndNlp) )
	     {%>
	    <input type=checkbox name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_NLP_CEDU%>" value=1 style="display:none;">
	  <%  } %>
	</div>
	<%}%>
	
</div>
 
<!--  < < < < < < < <   FINE FORM   > > > > > > > > >     -->
 <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
      <td class="l">In caso di Incompetenza indicare <%=labelUfficio%> destinatario </td>
      <td class="l">
        <input Title="<%=labelUfficio%>" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>" value="" size=35 >
        <a href="Javascript:ListaUDS('InserisciOrdinanzaArt3Cedu','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>','<%=CodUff%>');">
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

<!--   QUI C'ERA LA CHIUSURA DIV id=resto -->

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaArt3Cedu");


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