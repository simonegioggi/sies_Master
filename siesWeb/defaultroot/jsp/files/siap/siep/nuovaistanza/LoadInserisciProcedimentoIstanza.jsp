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


<jsp:useBean id="idfascicolo" scope="request" class="java.lang.String" />

<html>
<head>
  <title> Gestione Nuova Istanza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript">
  
   function ListaComuni(a_formname,a_fieldname)
   {
 	var desktop;
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
   }

   <!-- 20210810	MEV Scheda-21 -->
   function ListaComuniNascita(a_formname,a_fieldname)
   {
     //if (document.LoadInserisciSentenza.lTipoInserimento.value != "reginde")
     	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
   }      
   
   function ListaAvvocati(a_formname,a_filtro)
   {
	var desktop;
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadListaAvvocatoPopup&formname="+a_formname+"&filtro="+a_filtro, "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=550,height=500");
   }
  
   function Verify() 
   {
 
      if(!document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_DEPOSITO%>.disabled)
       {
        var data_to_verify_de = document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_DEPOSITO%>.value+'/'+ 
                             document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_DEPOSITO%>.value+'/'+ 
                             document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_DEPOSITO%>.value; 
        if (!ControllaData(data_to_verify_de))
        { 
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
      if (!ControllaData(data_to_verify_is))
      { 
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
	
<%-- 		var data_to_verify=document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value; --%>
// 		if (! ControllaData(data_to_verify)) {
// 			alert('Data di nascita non valida');
// 			return false;
// 		}
	
		if (document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>.value == "-") {
			alert('Il tipo difensore è obbligatorio');
			return false;
		}
	}

  	// 20210812 MEV_21 - Controlli per inserimento manuale avvocato presentante solo se check depositata.
	var lTipoInsP = document.LoadInserisciSentenza.lTipoInserimentoP.value;
	if ((document.LoadInserisciSentenza.<%=ICostantiNuovaIstanza.CAMPO_FLAG_PRESDEP %>[0].checked == true)
			&& (lTipoInsP == 'manuale')) {
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

<%-- 		var data_to_verify=document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA_P%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA_P%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA_P%>.value; --%>
// 		if (! ControllaData(data_to_verify)) {
// 			alert('Data di nascita non valida');
// 			return false;
// 		}
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

<body class="corpo" onLoad="Javascript:gestioneTipoIstanza();">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
        String lAzione = new String();
  
        lAzione = "siap.siep.nuovaistanza.action.ActInserisciNuovaIstanza"; 
       %>
           <font class="campo">Iscrizione Istanza per Procedimento SIEP</font>
        

      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadInserisciSentenza">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=idfascicolo%>">
  
<!-- 
    INIZIO dell'include! 

     All'interno dell'include si trova il sorgente della pagina visualizzata,
     il sorgente è comune a tutte le iscrizioni dell'ISTANZA! 
     
     ---ATTENZIONE QUANDO SI MODIFICA!! --- 
     
     Dario  -- 
-->   

 		 <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	  <br>

  	<div id="divistanza" style="display:block; position:relative; ">  
      <jsp:include page="/jsp/files/siap/siep/nuovaistanza/IncludeIstanza.jsp"/>   	  	
    </div> 
    
    
<!-- 
     FINE dell'include! 
     
     ---ATTENZIONE QUANDO SI MODIFICA!! --- 
     
     Dario  -- 
-->    
<table>
    <tr>
      <td class="l">
        <input class="bottone" type="submit" name="conferma" value="Conferma" onClick="Javascript:return EnableCombo();">
      </td>
    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciSentenza");

frmvalidator.setAddnlValidationFunction("Verify"); 
</script>
</body>
</html>