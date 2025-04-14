<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<!---------------------------------Soggetto------------------------------------------------->

<%@ page import="f3b.security.model.ProfileModel"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="UtenteConnesso" scope="session"
	class="siap.sico.utente.model.UtenteModel" />

<!---------------------------------Fine Soggetto------------------------------------------------->
<jsp:useBean id="tipoinserimento" scope="request"
	class="java.lang.String" />
<jsp:useBean id="idfascicolo" scope="request" class="java.lang.String" />
<jsp:useBean id="idsentenza" scope="request" class="java.lang.String" />
<jsp:useBean id="idsoggetto" scope="request" class="java.lang.String" />

<html>
<head>
<title>Gestione Nuova Istanza</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript"
	src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
<script language="JavaScript"
	src="<%=ICostantiFasSigeSentenza.JS_SENTENZA%>"></script>
<script language="JavaScript">
  // 10/06/2010 Lista Uffici per TIPO_UFFICIO
  var desktop;
  function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
  {
	    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  function comandainserimento()
  {
 
	   AbilitaSoggetto();	
	   var nodeSog = document.getElementById('divsoggetto'); 
  	 var nodeSen = document.getElementById('divsentenza');  
  	 var nodeIst = document.getElementById('divistanza'); 
  
<%
	  if("nuovo".equals(tipoinserimento)) 
	  {
%>
			//alert (">>>>>>>> nuovo");
			nodeSog.style.display='block';
			nodeSen.style.display='block';
			nodeIst.style.display='block';

 	    AbilitaSentenza();
	    VisualizzaSentenza();
      document.LoadInserisciSentenza.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.disabled=true;
	    		
<%
	  }else if("soggetto".equals(tipoinserimento))
	  {
%> 
			//alert (">>>>>>>> soggetto");
			nodeSog.style.display='block';
			nodeSen.style.display='block';
			nodeIst.style.display='block';  
			
      document.LoadInserisciSentenza.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.disabled=true;

 	    //DisabilitaSoggetto();
 	    AbilitaSentenza();
	    VisualizzaSentenza();		
<%
	  }else if("fascicolo".equals(tipoinserimento))
	  {
%>	
			//alert (">>>>>>>> fascicolo");
			nodeSen.style.display='none';
			nodeSog.style.display='none';
			nodeIst.style.display='block'; 

	    DisabilitaSentenza();
			DisabilitaSentenzaStraniera();
			DisabilitaDecreto();	
			DisabilitaSoggetto();

	    document.LoadInserisciSentenza.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.disabled=false;
<%
	  }
%>
     gestioneTipoIstanza();
 
  }
  
  function ListaComuni(a_formname,a_fieldname)
  {
	var desktop;
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  <!-- 20210524	MEV Scheda-21 -->
  function ListaComuniNascita(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
  }      
  
  function ListaAvvocati(a_formname,a_filtro)
  {
	var desktop;
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadListaAvvocatoPopup&formname="+a_formname+"&filtro="+a_filtro, "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=550,height=500");
  }
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
     function controllaEtaSoggetto()
      {
        var tipoUff = "<%=UtenteConnesso.getUfficioUtente().getCodTipoUfficio()%>";
        var ritorno = true;
        var oggi = new Date();
        var anno = Math.abs(document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value);
        var mese = 1;
        var giorno = 1;
        if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length > 1 )
            mese = document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;
        if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length > 1)
            giorno = document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
        var anno14 = anno + 14;
        var anno18 = anno + 18;
        var data_compleanno14 = new Date( anno14, mese -1, giorno);
        var data_compleanno18 = new Date( anno18, mese -1, giorno);


        if (tipoUff == "PMM" || tipoUff ==  "DIBM")
        {
           // caso Tribunale dei Minori
           if (oggi < data_compleanno14 )
             ritorno = window.confirm('Il soggetto non ha compiuto i 14 anni! Confermi il suo inserimento?');
           if ( oggi > data_compleanno18)
             ritorno = window.confirm('Il soggetto ha più di 18 anni! Confermi il suo inserimento?');
          
        }
        else
        {
           if (oggi < data_compleanno18)
              ritorno = window.confirm('Il soggetto non ha compiuto i 18 anni. Confermi il suo inserimento?');
        }

    
        return ritorno;
      }
      
      function cancellaCodComuneReale() {
      
      	document.LoadInserisciSentenza.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
      }    
    
    function Verify() 
    {
    	var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
    	
       if(!document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOME%>.disabled)
       {
        if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOME%>.value=='')
        {
       	    alert('Il campo  Nome Soggetto è obbligatorio');
            return false;
        }
        
        if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COGNOME%>.value=='')
        {
       	    alert('Il campo Cognome Soggetto è obbligatorio');
            return false;
        }
        
        if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039')
        {
          document.LoadInserisciSentenza.<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>.value="";
          if (document.LoadInserisciSentenza.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value.length==0)
          {
            alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
            return false;
          }
        }
        else {
          document.LoadInserisciSentenza.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value='';
          cancellaCodComuneReale();
		}
		
        if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
          document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
        if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
          document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;
       if(document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>[document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>.selectedIndex].value == 'N')
        {
          var data_to_verify=document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
          if (! ControllaData(data_to_verify))
          {
            alert('Data di nascita non valida');
            return false;
          }
        }

       }

/**************************Sentenza straniera*************************************************/
        if(!document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_SS%>.disabled)
       {
            
            if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_SS%>[document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_SS%>.selectedIndex].value=='-')
            {
           	 alert('Il campo Autorità Emittente è obbligatorio');
                return false;
            }
            if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE_SS%>.value=='')
            {
           	 alert('Il campo Luogo Autorità Emittente è obbligatorio');
                return false;
            }
            

            if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_SS%>.value.length==1)
          document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_SS%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_SS%>.value;
        if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_SS%>.value.length==1)
          document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_SS%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_SS%>.value;

        //Data arrivo atto
        var d1=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>.value;
 
        //Data Sentenza
        var d2=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_SS%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_SS%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO_SS%>.value;
        if (! ControllaData(d2))
        {
          alert('Data Sentenza non valida');
          return false;
        }

        if (! CompareDate(d2,d1))
        {
          alert('La Data Sentenza deve essere antecedente alla Data di Arrivo');
          return false;
        }
       }

/**************************FINE  Sentenza straniera*******************************************/
/**************************Sentenza Decreto*******************************************/
     if(!document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE_D%>.disabled)
     {

         if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_D%>[document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_D%>.selectedIndex].value=='-')
         {
        	 alert('Il campo Autorità Emittente è obbligatorio');
             return false;
         }
         if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE_D%>.value=='')
         {
        	 alert('Il campo Luogo Autorità Emittente è obbligatorio');
             return false;
         }
         
       if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_D%>.value.length==1)
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_D%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_D%>.value;
       if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_D%>.value.length==1)
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_D%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_D%>.value;


      var d3=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_D%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_D%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO_D%>.value;

       if (! ControllaData(d3))
      {	
    	 alert('Data Decreto non valida');
         return false;
      }
     }
     
/**************************FINE  Sentenza Decreto*******************************************/   
 
/***************************SENTENZA*********************************************************/
   if(!document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.disabled)
   {
      if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
      if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
      if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value.length==1)
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value;
      if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value.length==1)
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value;
       //Data Sentenza
      var d2=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
      if (! ControllaData(d2))
      {
        alert('Data Sentenza non valida');
        return false;
      }
      //Data Sentenza di Riferimento
      var d3=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>.value;
      if (! ControllaData(d3) && d3.length>2)
      {
        alert('Data Sentenza di riferimento non valida');
        return false;
      }

 //     var AnnoRG=document.LoadInserisciSentenza.<-%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.value;
 //     var NumeroRG=document.LoadInserisciSentenza.<-%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.value;
 //     if( AnnoRG.length=4	&& 
 //         NumeroRG.length>0	&& 
 //     		document.LoadInserisciSentenza.TipoRG[document.LoadInserisciSentenza.TipoRG.selectedIndex].value == '-' )
 //     {
 //       alert('Il Tipo Registro Generale è obbligatorio');
 //       document.LoadInserisciSentenza.TipoRG.focus();
 //
 //      return false;
 //    }

	  /**
	   * Nel caso in cui l'utente inserisca almeno uno tra i seguenti campi:
	   *  - Tipo Sentenza di Riferimento
	   *  - Data Sentenza di Riferimento
	   *  - Autorità Sentenza di Riferimento
	   *  - Luogo Sentenza di Riferimento
	   *  deve inserirli tutti
	   */
	    var TipoSentRif = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.value;
	    var GGSentRif   = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF %>.value;
	    var MMSentRif   = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF %>.value;
	    var AASentRif   = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF %>.value;
	    var TipoAutRif  = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.value;
	    var SedeRif     = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF %>.value;
	    var CodTipoProvv= document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>.value;
	
	    if(TipoSentRif != '-' || GGSentRif != '' || MMSentRif != '' || AASentRif != '' || TipoAutRif != '-' || SedeRif != '')
	    {
	    /* inizio modifica marzo 2010
	    
		    if(CodTipoProvv=='53'){
		    	return true;
		    }
	    fine  modifica marzo 2010 */    	
	      if(TipoSentRif == '-' && CodTipoProvv != '53')
	      {
	        alert('1 Dati della Sentenza di Riferimento Incompleti');
	        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.focus();
	
	        return false;
	      }
	      if(GGSentRif == '')
	      {
	        alert('2 Dati della Sentenza di Riferimento Incompleti');
	        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF %>.focus();
	
	        return false;
	      }
	      if(MMSentRif == '')
	      {
	        alert('3 Dati della Sentenza di Riferimento Incompleti');
	        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF %>.focus();
	
	        return false;
	      }
	      if(AASentRif == '')
	      {
	        alert('4 Dati della Sentenza di Riferimento Incompleti');
	        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF %>.focus();
	
	        return false;
	      }
	      if(TipoAutRif == '-')
	      {
	        alert('5 Dati della Sentenza di Riferimento Incompleti');
	        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.focus();
	
	        return false;
	      }
	      if(SedeRif == '')
	      {
	        alert('6 Dati della Sentenza di Riferimento Incompleti');
	        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF %>.focus();
	
	        return false;
	      }
	    }
	    /* 
		controllo coerenza date sentenza realizzato inanalogia a quanto sviluppato per i webservices
		(vedi siap.sico.webservices.action.ActNscToSiesLoadSentenza.java)
		*/
		var auEmi = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value;
		var dRif=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value
	      +'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value
	      +'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>.value;
	
		var gRif = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value;
	
		if(gRif!=null && gRif!=''){
			if(auEmi=="CAP" || 
				auEmi=="CAS" || 
				auEmi=="CASAP" || 
				auEmi=="CAPSM" || 
				auEmi=="CAPMI"){
				if (CompareDate(d2,dRif)){
		        	alert(' La Data Sentenza deve essere successiva alla Data della Sentenza di grado differente');
		        	return false;
		      	}
			}
			else{
				if (CompareDate(dRif,d2)){
		        	alert(' La Data della Sentenza di grado differente deve essere successiva alla Data Sentenza');
		        	return false;
		      	}
			}
		}
	
   }
 
/*****************************FINE SENTENZA*****************************************************/ 
 
 
       if(!document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_DEPOSITO%>.disabled)
       {
        var data_to_verify_de = document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_DEPOSITO%>.value+'/'+ 
                             document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_DEPOSITO%>.value+'/'+ 
                             document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_DEPOSITO%>.value; 
        if (!ControllaData(data_to_verify_de)){ 
          alert('Data Deposito non valida'); 
          document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_DEPOSITO%>.focus(); 
          return false; 
        } 
       }
          	
     if(!document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_ISTANZA%>.disabled)
     {
      var data_to_verify_is = document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_ISTANZA%>.value+'/'+ 
                           document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_ISTANZA%>.value+'/'+ 
                           document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_ISTANZA%>.value; 
      if (!ControllaData(data_to_verify_is)){ 
        alert('Data Istanza non valida'); 
        document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_ISTANZA%>.focus(); 
        return false; 
      } 
     }

 	if(document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_COD_CONTENUTO%>.value=='-')
     {
        alert('Inserire il contenuto istanza'); 
        document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_COD_CONTENUTO%>.focus(); 
        return false; 
     }


  	// 20210812 MEV_21 - Aggiunti controlli per inserimento manuale avvocato non certificato.
	var lTipoIns = document.LoadInserisciSentenza.lTipoInserimento.value;
	if (lTipoIns == 'manuale' ) {
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value.length==0 ) {
			alert('Il Cognome è obbligatorio');
			document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_COGNOME %>.focus;
			return false;
		}
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_NOME%>.value.length==0 ) {
			alert('Il Nome è obbligatorio');
			document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_NOME %>.focus;
			return false;
		}
	  	
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>[document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039') {
			document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE %>.value="";
			if (document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.value.length==0) {
				alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
				document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.focus;
				return false;
			}
		} else 	if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>[document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value!='-') {
			document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.value='';
			cancellaCodComuneReale();
		}
	
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value.length > 0 	&&
			document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE %>.value.length > 0 ) {
				alert('Il Comune di Nascita e il luogo di Nascita Estero sono alternativi');
				document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.focus;
				return false;
		}
			
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
			document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value;
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
			document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value;
	
		var data_to_verify=document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value;
		if (! ControllaData(data_to_verify)) {
			alert('Data di nascita non valida');
			return false;
		}
	
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>.value == "-") {
			alert('Il tipo difensore è obbligatorio');
			return false;
		}
	}

  	// 20210812 MEV_21 - Controlli per inserimento manuale avvocato presentante solo se check depositata.
	var lTipoInsP = document.LoadInserisciSentenza.lTipoInserimentoP.value;
	if ((document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_FLAG_PRESDEP %>[0].checked == true)	&& 
	    (lTipoInsP == 'manuale' ) )
	{
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COGNOME_P%>.value.length==0 ) {
			alert('Il Cognome è obbligatorio');
			document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_COGNOME_P %>.focus;
			return false;
		}
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_NOME_P%>.value.length==0 ) {
			alert('Il Nome è obbligatorio');
			document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_NOME_P %>.focus;
			return false;
		}
	  	
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA_P%>[document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA_P%>.selectedIndex].value=='039') {
			document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE_P %>.value="";
			if (document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA_P %>.value.length==0) {
				alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
				document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA_P %>.focus;
				return false;
			}
		} else 	if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA_P%>[document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA_P%>.selectedIndex].value!='-') {
			document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA_P %>.value='';
			cancellaCodComuneReale();
		}

		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA_P%>.value.length > 0 	&&
			document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE_P %>.value.length > 0 ) {
				alert('Il Comune di Nascita e il luogo di Nascita Estero sono alternativi');
				document.LoadInserisciSentenza.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA_P %>.focus;
				return false;
		}
			
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA_P%>.value.length==1)
			document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA_P%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA_P%>.value;
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA_P%>.value.length==1)
			document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA_P%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA_P%>.value;

		var data_to_verify=document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA_P%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA_P%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA_P%>.value;
		if (! ControllaData(data_to_verify)) {
			alert('Data di nascita non valida');
			return false;
		}
	}

 	
     var lRitorno = true
     if(!document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOME%>.disabled)
     {
       lRitorno = controllaEtaSoggetto();
     }

     //Controllo Data Irrevocabilità
     var d_irr=document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
     if (d_irr.length <3)
     {
       lRitorno = window.confirm('Data Irrevocabilità non valorizzata. Continuare?');
     }else if (ControllaData(d_irr)) 
     {
        if (! CompareDate(d_irr,data_sistema))
     		{
       		alert('La data irrevocabilità non può essere successiva alla data odierna!');
       		return false;
     		}
     }
      
      return lRitorno;
  }	// end Verify()
     
	function AbilitaSentenza()
	{	  
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.disabled=false;
        document.LoadInserisciSentenza.ARG.disabled=false;

        document.LoadInserisciSentenza.NRG.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.disabled=false;        

        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_RITO%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF%>.disabled=false;

        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_PROVV_RIF%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF%>.disabled=false;

        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_RITO_RIF%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO%>.disabled=false;
        document.LoadInserisciSentenza.ANNOREGECAS.disabled=false; 
        document.LoadInserisciSentenza.NUMREGECAS.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NOTE%>.disabled=false;
           

    	  
	}
	function DisabilitaSentenza()
	{
	       document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.disabled=true;
	        document.LoadInserisciSentenza.ARG.disabled=true;

	        document.LoadInserisciSentenza.NRG.disabled=true; 
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.disabled=true; 
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.disabled=true;        

	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_RITO%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF%>.disabled=true; 
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF%>.disabled=true;

	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_PROVV_RIF%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF%>.disabled=true; 
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF%>.disabled=true;

	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_RITO_RIF%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF%>.disabled=true; 
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO%>.disabled=true;
	        document.LoadInserisciSentenza.ANNOREGECAS.disabled=true; 
	        document.LoadInserisciSentenza.NUMREGECAS.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE%>.disabled=true; 
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE%>.disabled=true; 
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE%>.disabled=true;
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE%>.disabled=true; 
	        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NOTE%>.disabled=true;        
	  
	}    
	function AbilitaSentenzaStraniera()
	{	  
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_SS%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_SS%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO_SS%>.disabled=false;

        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA_SS%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA_SS%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_SS%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE_SS%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE_SS%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NOTE_SS%>.disabled=false; 
        
	  
	}
	function DisabilitaSentenzaStraniera()
	{
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_SS%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_SS%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO_SS%>.disabled=true;

        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA_SS%>.disabled=true; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA_SS%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_SS%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE_SS%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE_SS%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NOTE_SS%>.disabled=true;           
	  
	}
    
	function AbilitaDecreto()
	{
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM_D%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM_D%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO_D%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_REGE_GIP_D%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_GIP_D%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_D%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_D%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO_D%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA_D%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA_D%>.disabled=false;           
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_D%>.disabled=false;           		
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE_D%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE_D%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE_D%>.disabled=false;           
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE_D%>.disabled=false;           
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE_D%>.disabled=false;           		  
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE_D%>.disabled=false;           
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE_D%>.disabled=false;           
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NOTE_D%>.disabled=false;           
	  
	}
	function DisabilitaDecreto()
	{
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM_D%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM_D%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO_D%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_REGE_GIP_D%>.disabled=true; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_GIP_D%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_D%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_D%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO_D%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA_D%>.disabled=true; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA_D%>.disabled=true;           
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_D%>.disabled=true;           		
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE_D%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE_D%>.disabled=true; 
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE_D%>.disabled=true;           
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE_D%>.disabled=true;           
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE_D%>.disabled=true;           		  
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE_D%>.disabled=true;           
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE_D%>.disabled=true;           
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_NOTE_D%>.disabled=true;           
	  
	}

	function AbilitaSoggetto()
	{
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COGNOME%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOME%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_SESSO%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NAZIONALITA%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.disabled=false;           
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>.disabled=false;           		
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_PATERNITA%>.disabled=false;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COGNOME_MADRE%>.disabled=false; 
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOME_MADRE%>.disabled=false;           
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COD_FISCALE%>.disabled=false;           
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_ATTO_NASCITA%>.disabled=false;           		  
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>.disabled=false;           
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOTE%>.disabled=false;           
        document.LoadInserisciSentenza.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.disabled=false;           
	  
	}
	function DisabilitaSoggetto()
	{
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COGNOME%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOME%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_SESSO%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.disabled=true; 
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NAZIONALITA%>.disabled=true; 
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.disabled=true;           
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>.disabled=true;           		
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_PATERNITA%>.disabled=true;
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COGNOME_MADRE%>.disabled=true; 
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOME_MADRE%>.disabled=true;           
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COD_FISCALE%>.disabled=true;           
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_ATTO_NASCITA%>.disabled=true;           		  
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>.disabled=true;           
        document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOTE%>.disabled=true;           
        document.LoadInserisciSentenza.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.disabled=true;           

    
	}
	       
	function VisualizzaSentenza()
	{
	  	   var nodeSen = document.getElementById('SentenzaDiv'); 
	  	   var nodeSenStr = document.getElementById('SentenzaStranieraDiv');  
	  	   var nodeDec = document.getElementById('DecretoDiv'); 
	  	    
		   if (document.LoadInserisciSentenza.TipoProvv[0].checked == true)//sentenza
	  	   {
			     nodeSen.style.display='block';
			     nodeSenStr.style.display='none';
			     nodeDec.style.display='none';
		     
			     AbilitaSentenza();
			     DisabilitaSentenzaStraniera();
			     DisabilitaDecreto();			     	
	  	   }
		   else if (document.LoadInserisciSentenza.TipoProvv[1].checked == true)//decreto
		   {
			     nodeSen.style.display='none';
			     nodeSenStr.style.display='none';
			     nodeDec.style.display='block';	

			     DisabilitaSentenza();
			     DisabilitaSentenzaStraniera();
			     AbilitaDecreto();	  		
	  	   }
		   else if (document.LoadInserisciSentenza.TipoProvv[2].checked == true)//sentenza straniera
		   {	
			     nodeSen.style.display='none';
			     nodeSenStr.style.display='block';
			     nodeDec.style.display='none';	

			     DisabilitaSentenza();
			     AbilitaSentenzaStraniera();
			     DisabilitaDecreto();
		   }	  	    	   
	}

  	   
    function gestioneTipoIstanza()
    {    
  	   var nodeper = document.getElementById('divpervenuta'); 
  	   var nodedep = document.getElementById('divdepositata');  
  	        	     
  	   if (document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_FLAG_PRESDEP %>[0].checked == true)
  	    {
  		 nodeper.style.display='block';
  		 nodedep.style.display='none';
  		 
         document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_ISTANZA%>.disabled=false;
         document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_ISTANZA%>.disabled=false;
         document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_ISTANZA%>.disabled=false;
         document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_COD_AUTORITA_MITTENTE%>.disabled=false; 
         document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_COD_SEDE_MITTENTE%>.disabled=false;
     
         document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_DEPOSITO%>.disabled=true;
         document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_DEPOSITO%>.disabled=true;
         document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_DEPOSITO%>.disabled=true;
         document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_SOGG_PRESENTANTE%>.disabled=true; 
         document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_SOGG_PRESENTANTE_IDENTIFICATO%>.disabled=true;           
         document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO_PRESENTANTE%>.disabled=true;           
			 }
  	   else if (document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_FLAG_PRESDEP %>[1].checked == true)
  	   {
  		 nodedep.style.display='block';
  		 nodeper.style.display='none';

          document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_ISTANZA%>.disabled=true;
          document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_ISTANZA%>.disabled=true;
          document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_ISTANZA%>.disabled=true;
          document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_COD_AUTORITA_MITTENTE%>.disabled=true; 
          document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_COD_SEDE_MITTENTE%>.disabled=true;
      


          document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_DEPOSITO%>.disabled=false;
          document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_DEPOSITO%>.disabled=false;
          document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_DEPOSITO%>.disabled=false;
          document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_SOGG_PRESENTANTE%>.disabled=false; 
          document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_SOGG_PRESENTANTE_IDENTIFICATO%>.disabled=false;           
          document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO_PRESENTANTE%>.disabled=false;           

  	    }
	  	// 20210730 Controllo tipo inserimento non Reginde
		var lTipoIns = document.LoadInserisciSentenza.lTipoInserimento.value;
		if (lTipoIns == 'manuale' ) {
			document.getElementById('inserimento').style.visibility = 'visible';
			document.getElementById('confermaBtn').style.visibility = 'hidden';
			document.getElementById('ricReginde').style.visibility = 'hidden';
			document.getElementById('<%=ICostantiAvvocato.CAMPO_COGNOME%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_NOME%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>').disabled = false;
			document.getElementById('<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_FORO%>').disabled = false;
			document.getElementById('<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_TELEFONO%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_FAX%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_E_MAIL%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_PEC%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>').readOnly = false; 
			document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>').disabled = false;
		}
  		var lTipoInsP = document.LoadInserisciSentenza.lTipoInserimentoP.value;
  		if (lTipoInsP == 'manuale' ) {
  			document.getElementById('inserimentoP').style.visibility = 'visible';
  			document.getElementById('ricRegindeP').style.visibility = 'hidden';
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_COGNOME_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_NOME_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA_P%>').disabled = false;
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_FORO_P%>').disabled = false;
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_INDIRIZZO_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_TELEFONO_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_FAX_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_E_MAIL_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_PEC_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE_P%>').readOnly = false; 
  			document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA_P%>').disabled = false;
  		}
 	    
    } 

    function ctrl_autorita(idcmb1, idcmb2, idDiv, idTipoRito) {
    	
    	// cmb1 è la combo che fa scattare la funzione
    	var cmb1 = document.getElementById(idcmb1);	
    	var cmb2 = document.getElementById(idcmb2);
    		
    	var arrCmb = new Array(cmb1, cmb2);
    	var arrGrado = new Array();
    	
     	for(var i=0;i<arrCmb.length;i++) {

    		if (arrCmb[i].value == "CSS") {
    			
    			arrGrado[i] = 3;
    		}
    		else if (arrCmb[i].value == "CAP" || arrCmb[i].value == "CASAP" || arrCmb[i].value == "CAPSM") {
    			
    			arrGrado[i] = 2;
    		}
    		else {
    		
    			arrGrado[i] = 1;
    		}
    	}		
     	
     	if (cmb1.value != "-" && cmb2.value != "-") {
    		
    		if (cmb1.value == cmb2.value) {
    		
    			alert("Non è consentito selezionare due Autorità Emittenti uguali!");
    			cmb1.selectedIndex = 0;
    			cmb1.focus();
    		}
    		else if (arrGrado[0] == arrGrado[1]) {			
    			
    			// eccezione per Giudice di Pace e Tribunale Ordinario (anche sezione distaccata)
    			if (!(cmb1.value == "GP" && (cmb2.value == "DIB" || cmb2.value == "TRIBSD")) 
    			 && !(cmb2.value == "GP" && (cmb1.value == "DIB" || cmb1.value == "TRIBSD"))) {
    			
    				alert("Non è consentito selezionare due Autorità Emittenti dello stesso grado!");
    				cmb1.selectedIndex = 0;
    				cmb1.focus();
    			}
    		}
    	}	
    	
    				
    	var node = document.getElementById(idDiv);
    	var cmbRito = document.getElementById(idTipoRito);
    	
    	if (cmb1.value == "DIB" || cmb1.value == "TRIBSD") {
    		
    		node.style.visibility = "visible";
    	}
    	else {
    		
    		node.style.visibility = "hidden";
    		cmbRito.selectedIndex = 0;		
    	}
    }
      
      	function viewDiv(idDiv, aForm, aField, valueHidden){
    		var node = document.getElementById(idDiv);
    	    var valF = eval('document.'+aForm+'.'+aField+'.value');
    	   
    	    if(idDiv=='cassazione'){	    
    			if (valF==valueHidden) {
    				node.style.display = "none";
    				document.getElementById('tipoSentenza').style.display="none";				
    				document.getElementById('labelSentenza').style.display="none";
    				document.getElementById('anSentenza').style.display="none";				
    				document.getElementById('labelOrdinanza').style.display="block";
    				document.getElementById('anOrdinanza').style.display="block";
    				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.value=""');
    				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>.value="-"');
    				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>.value=""');
    				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE %>.value=""');
    				eval('document.'+aForm+'.ANNOREGECAS.value=""');
    				eval('document.'+aForm+'.NUMREGECAS.value=""');
    				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.value="-"');
    				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>.value="-"');
    				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE %>.value=""');
    				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE%>.value=""');
    							
    			}
    			else {
    				node.style.display = "block";
    				document.getElementById('tipoSentenza').style.display="block";
    				document.getElementById('labelSentenza').style.display="block";
    				document.getElementById('anSentenza').style.display="block";
    				document.getElementById('labelOrdinanza').style.display="none";
    				document.getElementById('anOrdinanza').style.display="none";
    				
    			}
    		}
    		else if(idDiv=='0'){
    			if (valF==valueHidden) {						
    				document.getElementById('anRegGen').style.display="none";				
    				document.getElementById('anRacGen').style.display="none";				
    				document.getElementById('disp').style.display="none";				
    				document.getElementById('lblSentenza').style.display="none";
    				document.getElementById('lblOrdinanza').style.display="block";
    				//PULISCO I CAMPI
    				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>.value="-"');
    				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>.value=""');
    				eval('document.'+aForm+'.<%= ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE %>.value=""');
    				eval('document.'+aForm+'.ANNOREGECAS.value=""');
    				eval('document.'+aForm+'.NUMREGECAS.value=""');
    				
    			}
    			else {
    				document.getElementById('anRegGen').style.display="block";
    				document.getElementById('anRacGen').style.display="block";
    				document.getElementById('disp').style.display="block";
    				document.getElementById('lblSentenza').style.display="block";
    				document.getElementById('lblOrdinanza').style.display="none";
    			}
    		}
    	}
    	
        <%-- MEV_21: aggiunta chiamata a WS per individuare lista avvocato in RegInde --%>
        function ListaAvvocatiRegInde(a_formname) {
        	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocatoRegInde&formname="+a_formname,"Ricerca_Avvocato_RegInde","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1000,height=600");
        }


        <%-- 20210809 MEV_21: Gestione Inserimento estemporaneo di avvocato da RegInde o manuale --%>
        function ListaInsAvvRegInde(a_formname) {
        	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaInsAvvRegInde&formname="+a_formname,"Ricerca_Avvocato_RegInde","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1000,height=600");
        }
        
     	// Enable ComboBox
        function EnableCombo() {
        	document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.disabled = false;
        	document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_FORO%>.disabled = false;
        	document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>.disabled = false;
        	document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA_P%>.disabled = false;
        	document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_FORO_P%>.disabled = false;
        	document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA_P%>.disabled = false;
        }
        
        
  </script>
</head>

<body class="corpo" onLoad="Javascript:comandainserimento();">
	<table>
		<tr>
			<td class="LBG"><a href="Javascript:window.print();"> <img
					align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
					alt="Stampa questa videata" border="0">
			</a></td>
			<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
				<%
        NuovaIstanzaModel lNuovaIstanza = new NuovaIstanzaModel(); 
        String lAzione = new String();
  
          lAzione = "siap.siep.nuovaistanza.action.ActInserisciNuovaIstanza"; 
         %> <font class="campo">Iscrizione Istanza per Soggetto</font></td>
		</tr>
	</table>

	<FORM method="POST" action="Main.jsp" name="LoadInserisciSentenza">
		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"
			value="<%=lAzione%>">
		<!-- 
    INIZIO dell'include! 

     All'interno dell'include si trova il sorgente della pagina visualizzata,
     il sorgente è comune a tutte le iscrizioni dell'ISTANZA! 
     
     ---ATTENZIONE QUANDO SI MODIFICA!! --- 
     
     Dario  -- 25/06/2009
-->


		<input type="HIDDEN" name="tipoinserimento"
			value="<%=tipoinserimento%>"> <input type="HIDDEN"
			name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>"
			value="<%=idsoggetto%>"> <input type="HIDDEN"
			name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>"
			value="<%=idfascicolo%>">
		<%
	  if("fascicolo".equals(tipoinserimento)) 
	  {%>
		<jsp:include
			page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp" />
		<br>

		<%
	  }else if("soggetto".equals(tipoinserimento))
	  {%>

		<jsp:include page="/jsp/files/siap/sico/soggetto/SintesiSoggetto.jsp" />
		<br>
		<%
	  }%>

		<div id="divsoggetto" style="display: none; position: relative;">
			<jsp:include
				page="/jsp/files/siap/siep/nuovaistanza/IncludeIstanzaSoggetto.jsp" />
		</div>

		<div id="divsentenza" style="display: none; position: relative;">
			<jsp:include
				page="/jsp/files/siap/siep/nuovaistanza/IncludeProvvedimento.jsp" />
		</div>

		<div id="divistanza" style="display: none; position: relative;">
			<jsp:include
				page="/jsp/files/siap/siep/nuovaistanza/IncludeIstanza.jsp" />
		</div>


		<!-- 
     FINE dell'include! 
     
     ---ATTENZIONE QUANDO SI MODIFICA!! --- 
     
     Dario  -- 25/06/2009
-->
		<table>
			<tr>
				<td class="l">
        			<input class="bottone" type="submit" name="conferma" value="Conferma" onClick="Javascript:return EnableCombo();">
			</tr>
		</table>
	</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciSentenza");


/**************************soggetto****************************************/
//soggetto
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","alpha");
	
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","alpha");
	
	
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");
	
	<%-- frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_FISCALE %>","alphanumeric"); --%>
	<%-- frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_AFIS %>","alphanumeric"); --%>
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NAZIONALITA%>","alphabetic");
	<%-- frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>","alphanumeric"); --%>
	<%-- frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>","alphanumeric"); --%>
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_PATERNITA%>","alphabetic");
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE%>","alphabetic");
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME_MADRE%>","alphabetic");
	<%-- frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA%>","alphanumeric"); --%>
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>","alpha");
	frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>","alpha");
	/**************************fine soggetto****************************************/
	/**************************Sentenza straniera****************************************/	
	
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_SS%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_SS%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA_SS%>","numeric");


	/**************************FINE Sentenza straniera****************************************/	
 	/**************************Sentenza Decreto****************************************/		 

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM_D%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM_D%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM_D%>","lt=2999");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM_D%>","numeric");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_D%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_D%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_D%>","lt=2999");

   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA_D%>","numeric");


	/**************************FINE Sentenza Decreto****************************************/	
	/**************************Sentenza****************************************/		
     frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","gt=1900");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>","numeric");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","gt=1900");

    <%-- frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>","alfanumeric"); --%>

  
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>","alphabetic");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>","maxlen=4","La lunghezza massima per l'anno della data Sentenza di riferimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>","gt=1900");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF%>","alphabetic");

	/**************************FINE Sentenza ****************************************/	
 
    frmvalidator.setAddnlValidationFunction("Verify"); 
</script>
</body>
</html>