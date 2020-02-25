<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimentiRif" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimenti" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimentiAltro" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoDecisioneCassazione" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaProvRif" scope="request" class="java.lang.String" />
<jsp:useBean id="flagSN" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito1" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito2" scope="request" class="java.lang.String" />
<jsp:useBean id="lTipoFunzione" scope="request" class="java.lang.String" />
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<head>
<title>[S.I.E.S.] - Gestione Sentenza</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
	    // 09/06/2010 Lista Uffici per TIPO_UFFICIO
	    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    	{
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    	}
    </script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
    function Verify()
    {
      /*
      if (document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value.length==1)
        document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value='0'+document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value;
      if (document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value.length==1)
        document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value='0'+document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value;
      */
      if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
      if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
      if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value.length==1)
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value;
      if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value.length==1)
        document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value;
     /*
      if (document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
      if (document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;
     */

      //Data arrivo atto
      /*
      var d1=document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value
      +'/'+document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value
      +'/'+document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>.value;
      if (! ControllaData(d1))
      {
        alert('Data di arrivo atto non valida');
        return false;
      }*/
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
      //Data Irrevocabilità
      /*var d4=document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciSentenza.< %=ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
      if (! ControllaData(d4))
      {
        alert('Data irrevocabità non valida');
        return false;
      }*/

      /*if (! CompareDate(d2,d1))
      {
        alert('La Data Sentenza deve essere antecedente alla Data di Arrivo');
        return false;
      }*/
      /*if (! CompareDate(d2,d4))
      {
        alert('La Data di Irrevocabilità deve essere successiva alla data della sentenza');
        return false;
      }*/
/*
      if (! CompareDate(d3,d1))
      {
        alert('La Data Arrivo Atto deve essere successiva alla Data Sentenza di Riferimento');
        return false;
      }
*/

      if( document.LoadInserisciSentenza.TipoRG[document.LoadInserisciSentenza.TipoRG.selectedIndex].value == '-' )
      {
        alert('Il Tipo Registro Generale è obbligatorio');
        document.LoadInserisciSentenza.TipoRG.focus();

        return false;
      }
<%
/*
  -- Commentato Rework del 17-06-2003 --
  UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
  UfficioModel lUffMod = lUteMod.getUfficioUtente();
  String TipoUff = new String(lUteMod.getUfficioUtente().getDescrTipoUfficio());
  if (TipoUff.startsWith("PROCURA GENERALE"))
  {
*/
%>
/*
      var Emittente = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value;
      var Riferimento = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.value;

      if (Emittente=='GIP')
      {
        if (document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_REGE_GIP %>.value.length==0 || document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_NUMERO_REGE_GIP%>.value.length==0)
        {
          alert('Il campo Anno/Numero Re.Ge. GIP per Autorità Emittente è obbligatorio');
          document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_REGE_GIP %>.focus();
          return false;
        }
      }

      if (Riferimento.substring(0,3)=='DIB' || Riferimento=='CAS')
      {
        if (document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_REGE_DIB_CAS %>.value.length==0 || document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_NUMERO_REGE_DIB_CAS%>.value.length==0)
        {
          alert('Il campo Anno/Numero Re.Ge. DIB/CAS per Autorità Riferimento è obbligatorio');
          document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_REGE_DIB_CAS %>.focus();
          return false;
        }
      }
      else
      {
        if (Riferimento.substring(0,3)!='DIB' && Riferimento!='CAS' && Riferimento!='-' && Riferimento!='GIP' )
        {
           alert('Autorità Riferimento deve essere di tipo DIB o CAS');
           document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.focus();
           return false;
        }
      }
      if (Emittente.substring(0,3)=='CAP' || Emittente.substring(0,3)=='CASAP')
      {
        if (document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_REGE_CAP_CASAP %>.value.length==0 || document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_NUMERO_REGE_CAP_CASAP%>.value.length==0)
        {
          alert('Il campo Anno/Numero Re.Ge. CAP/CASAP per Autorità Emittente è obbligatorio');
          document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_REGE_CAP_CASAP %>.focus();
          return false;
        }
      }
      else
      {
        if (Emittente.substring(0,3)!='CAP' && Emittente!='CASAP' && Emittente!='GIP')
        {
           alert('Autorità Emittente deve essere di tipo CAP o CASAP');
           document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
           return false;
        }
      }
*/
<%
/*
  }
  else
  {
*/
%>
/*
      var Emittente = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value;
      var Riferimento = document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.value;

      if (Emittente=='GIP')
      { if (document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_REGE_GIP %>.value.length==0 || document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_NUMERO_REGE_GIP%>.value.length==0)
        {
          alert('Il campo Anno/Numero Re.Ge. GIP per Autorità Emittente è obbligatorio');
          document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_REGE_GIP %>.focus();
          return false;
        }
      }

      if (Emittente.substring(0,3)=='DIB' || Emittente=='CAS')
      { if (document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_REGE_DIB_CAS %>.value.length==0 || document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_NUMERO_REGE_DIB_CAS%>.value.length==0)
        {
          alert('Il campo Anno/Numero Re.Ge. DIB/CAS per Autorità Emittente è obbligatorio');
          document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_REGE_DIB_CAS %>.focus();
          return false;
        }
      }
      else
      {
        if (Emittente.substring(0,3)!='DIB' && Emittente!='CAS' && Emittente!='GIP' && Emittente!='-')
        {
           alert('Autorità Emittente deve essere di tipo DIB o CAS o GIP');
           document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
           return false;
        }
      }
      if (Riferimento.substring(0,3)=='CAP' || Riferimento=='CASAP')
      {
        if (document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_REGE_CAP_CASAP %>.value.length==0 || document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_NUMERO_REGE_CAP_CASAP%>.value.length==0)
        {
          alert('Il campo Anno/Numero Re.Ge. CAP/CASAP per Autorità Riferimento è obbligatorio');
          document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_REGE_CAP_CASAP %>.focus();
          return false;
        }
      }
      else
      {
        if (Riferimento.substring(0,3)!='CAP' && Riferimento!='CASAP' && Riferimento!='GIP' && Riferimento!='-')
        {
          alert('Autorità Riferimento deve essere di tipo CAP o CASAP');
          document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.focus();
          return false;
        }
      }
*/
<%
 // }
%>
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
	    if(CodTipoProvv=='53'){
	    	return true;
	    }
    	
      if(TipoSentRif == '-')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.focus();

        return false;
      }
      if(GGSentRif == '')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF %>.focus();

        return false;
      }
      if(MMSentRif == '')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF %>.focus();

        return false;
      }
      if(AASentRif == '')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF %>.focus();

        return false;
      }
      if(TipoAutRif == '-')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.focus();

        return false;
      }
      if(SedeRif == '')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
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
      +'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value
      +'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value;
	//CAMPO_GIORNO_DATA_PROVV_RIF
	
	if(auEmi=="CAP" || 
		auEmi=="CAS" || 
		auEmi=="CASAP" || 
		auEmi=="CAPSM" || 
		auEmi=="CAPMI"){
		if (! CompareDate(d2,dRif)){
        	alert('La Data Sentenza deve essere antecedente alla Data della Sentenza di grado differente');
        	return false;
      	}
	}
	else{
		if (! CompareDate(dRif,d2)){
        	alert('La Data della Sentenza di grado differente deve essere antecedente alla Data Sentenza');
        	return false;
      	}
	}
	
	
    return true;
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
	

	
	function ctrlDiv(){
		if (document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>.value=='53'){
			viewDiv('cassazione', 'LoadInserisciSentenza', '<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>','53')
		}
		if (document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>.value=='53'){
			viewDiv('0', 'LoadInserisciSentenza', '<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>','53')
		}
	}
	
  
  </script>
</head>

<body class="corpo">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();">
			<img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0></a></td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp; <%
 	SentenzaModel lSentenza = new SentenzaModel();
 	String lAction = new String();

 	if (modalita.equals("I")) {
 		lAction = "siap.siep.sentenza.action.ActInserisciSentenza";
 %> <font class="campo">Inserimento Sentenza</font> <%
 		} else if (modalita.equals("M")) {
 		lAction = "siap.siep.sentenza.action.ActModificaSentenza";
 		lSentenza = new SentenzaModel(sentenza);
 %> <font class="campo">Modifica Sentenza</font> <%
 }
 %>
		</td>
	</tr>
</table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"
	name="LoadInserisciSentenza">

<table cellspacing=2 cellpadding=2>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	<%-- 
	<tr>
		<td class="l">Data Arrivo Atto <font class="ob">(*)</font></td>
		<td class="L"><input Title="Data Arrivo Atto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"dd")) %>"
			name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Arrivo Atto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"MM")) %>"
			name="<%= ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Arrivo Atto" type="text"
			value=" <%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"yyyy")) %>"
			name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>

		<td class="l" colspan="2">Data Inserimento &nbsp;&nbsp; <font
			class="campo"> <%
 if (modalita.equals("M")) {
 %> <%=StringUtils.toStringJSP(DateUtils.getDateToString(
								lSentenza.getDataInserimento(), "dd-MM-yyyy"))%> <%
 } else {
 %> <%=StringUtils.toStringJSP(DateUtils.getDateToString(
								DateUtils.getSysDate(), "dd"))%> - <%=StringUtils.toStringJSP(DateUtils.getDateToString(
								DateUtils.getSysDate(), "MM"))%> - <%=StringUtils.toStringJSP(DateUtils.getDateToString(
								DateUtils.getSysDate(), "yyyy"))%> <%
 }
 %> </font></td>
	</tr>
	<tr>
		<td class="l">Data Iscrizione <font class="ob">(*)</font></td>
		<td class="L">
		< 
		if (modalita.equals("M")) {
		%><input Title="Data Iscrizione" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"dd")) %>"
			name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Arrivo Atto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"MM")) %>"
			name="<%= ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Arrivo Atto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"yyyy")) %>"
			name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_ISCRIZIONE %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"> <%
 		} else { //nel caso di inserimento della sentrenza viene impostata la dta con la data odieran ma puo'
 		//essere cambiata
 %> <input Title="Data Iscrizione" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"dd")) %>"
			name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Arrivo Atto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"MM")) %>"
			name="<%= ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Arrivo Atto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"yyyy")) %>"
			name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_ISCRIZIONE %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)">
		<%
		}
		%>
		</td>
	</tr>
	--%>
	<tr>
		<td class="l">Anno/Numero R.G.N.R. <font class="ob">(*)</font></td>
		<td class="L">
			<input Title="Anno R.G.N.R." value="<%=StringUtils.toStringJSP( lSentenza.getAnnoRegePm()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM %>" maxlength="4" size="4">/
			<input Title="Numero R.G.N.R." value="<%=lSentenza.getNumeroRegePm() %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM %>" maxlength="6" size="6">
		</td>
		<%
			String ARG = "";
			String NRG = "";
			String Tipo = "";
			if (lSentenza.getAnnoRegeCap() != null) {
				ARG = lSentenza.getAnnoRegeCap() + "";
				NRG = lSentenza.getNumeroRegeCap() + "";
				Tipo = "cap";
			}
			if (lSentenza.getAnnoRegeCas() != null) {
				ARG = lSentenza.getAnnoRegeCas() + "";
				NRG = lSentenza.getNumeroRegeCas() + "";
				Tipo = "cas";
			}
			if (lSentenza.getAnnoRegeDib() != null) {
				ARG = lSentenza.getAnnoRegeDib() + "";
				NRG = lSentenza.getNumeroRegeDib() + "";
				Tipo = "dib";
			}
			if (lSentenza.getAnnoRegeGip() != null) {
				ARG = lSentenza.getAnnoRegeGip() + "";
				NRG = lSentenza.getNumeroRegeGip() + "";
				Tipo = "gip";
			}
			if (lSentenza.getAnnoRegeCasap() != null) {
				ARG = lSentenza.getAnnoRegeCasap() + "";
				NRG = lSentenza.getNumeroRegeCasap() + "";
				Tipo = "casap";
			}
			// MEV_66: aggiunte quattro nuove proprietà
			if (lSentenza.getAnnoRegeGup() != null) {
				ARG = lSentenza.getAnnoRegeGup() + "";
				NRG = lSentenza.getNumeroRegeGup() + "";
				Tipo = "gup";
			}
			if (lSentenza.getAnnoRegeCapsm() != null) {
				ARG = lSentenza.getAnnoRegeCapsm() + "";
				NRG = lSentenza.getNumeroRegeCapsm() + "";
				Tipo = "capsm";
			}
		%>
		<td class="l">Anno/Numero Reg.Gen. <font class="ob">(*)</font></td>
		<td class="L">
			<input Title="Anno Reg.Gen." value="<%=ARG%>" type="text" name="ARG" maxlength="4" size="4"> /
			<input Title="Numero Reg.Gen." value="<%=NRG%>" type="text" name="NRG" maxlength="6" size="6"> &nbsp; 
			<select name="TipoRG"> 
				<option value="-">-</option>
				<%
					String sel = "";
					if (Tipo.equals("gip"))
						sel = " selected";
				%>
				<option value="gip" <%=sel%>>GIP</option>
				<%-- MERGE v10: aggiunta option GUP --%>
				<%
					sel = "";
					if (Tipo.equals("gup"))
						sel = " selected";
				%>
				<option value="gup" <%=sel%>>GUP</option>
				<%
					sel = "";
					if (Tipo.equals("dib"))
						sel = " selected";
				%>
				<option value="dib" <%=sel%>>DIB</option>
				<%
					sel = "";
					if (Tipo.equals("cas"))
						sel = " selected";
				%>
				<option value="cas" <%=sel%>>CAS</option>
				<%
					sel = "";
					if (Tipo.equals("cap"))
						sel = " selected";
				%>
				<option value="cap" <%=sel%>>CAP</option>
				<%
					sel = "";
					if (Tipo.equals("casap"))
						sel = " selected";
				%>
				<option value="casap" <%=sel%>>CASAP</option>
			</select></td>
	</tr>
	<tr>
<%
	String sedePM = (modalita.equals("M") ? lSentenza.getDescrSedeNotiziaReato() : UtenteConnesso.getUfficioUtente().getDescrComune());
%>
		<td class="l">Sede PM <font class=ob>(*)</font></td>		
		<td class="L">
			<input Title="Sede PM" name="<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>" value="<%=sedePM%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenza','<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>','PM');">
					<img src="/images/filefolder.gif" border=0>
				</a>
		</td>
	</tr>
	<!---------------------------------------------------------->
	<tr>
		<td class="Titolo" colspan=4>Sentenza da Eseguire</td>
	</tr>
	<tr>
		<td class="l">Data Sentenza <font class="ob">(*)</font></td>
		<td class="L"><input Title="Data Sentenza" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd")) %>"
			name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Sentenza" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"MM")) %>"
			name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Sentenza" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"yyyy")) %>"
			name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>
		<td class="l">Anno/Numero Sentenza <font class="ob">(*)</font></td>
		<td class="L"><input Title="Anno Sentenza"
			value="<%=StringUtils.toStringJSP( lSentenza.getAnnoSentenza()) %>"
			type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA %>"
			maxlength="4" size="4"> /<input Title="Numero Sentenza"
			value="<%=lSentenza.getNumeroSentenza() %>" type="text"
			name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>" maxlength="6"
			size="6"></td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
		<td class="L">
			<select Title="Autorità Emittente" onChange="ctrl_autorita('<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>', '<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>', 'D1', '<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO %>');" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
			<%=autoritaEmi%>
		</select></td>
		<td colspan=2>
		<%
			String visib1 = new String("hidden");
			if (modalita.equals("M")
					&& (lSentenza.getCodTipoAutoritaEmittente().equals("DIB") || lSentenza
					.getCodTipoAutoritaEmittente().equals("TRIBSD"))) {

				visib1 = "visible";
			}
		%>
		<div id=D1 STYLE="visibility: <%=visib1%>">
		<table width=100%>
			<tr>
				<td class="l">Tipo Rito</td>
				<td class="L"><select Title="Tipo Rito"
					name="<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO %>">
					<%=tipoRito1%>
				</select></td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
	<tr>
		<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
		<td class="L"><input Title="Luogo Emittente"
			name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>"
			value="<%=lSentenza.getDescrLuogoEmittente()%>" type="text"
			maxlength="35" size="35"> 
      <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenza','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>',document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
		<img src="/images/filefolder.gif" border=0> </a></td>
		<td class="L">Sezione Autorità Emittente</td>
		<td class="L"><input Title="Sezione Autorità Emittente"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente()) %>"
			type="text"
			name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE %>"
			maxlength="35" size="35"></td>
	</tr>
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	<%-- 
	<tr>
		<td class="l">Sentenza ex art. 444 c.p.p.</td>
		<td class="L" colspan=3>Sì<input type=radio
			name="<%= ICostantiSentenza.CAMPO_FLAG_SENTENZA_APPLICAZ_PENA %>"
			value="S"> No<input type=radio
			name="<%= ICostantiSentenza.CAMPO_FLAG_SENTENZA_APPLICAZ_PENA %>"
			value="N" checked></td>
	</tr>
	--%>
	<!---------------------------------------------------------->
	<tr>
		<td class="Titolo" colspan=4>Altro Grado di Giudizio</td>
	</tr>
	<tr>
		<td class="l">Tipo Provvedimento</td>
		<td class="L">
			<select Title="Tipo Prevvedimento Riferimento"
				name="<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>"
				OnChange="viewDiv(
					'cassazione', 
					'LoadInserisciSentenza', 
					'<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>',
					'53')">
				<%=tipoProvvedimenti%>
			</select>
		</td>
	</tr>
	<tr id="tipoSentenza" style="display:block">
		<td class="l">Tipo Sentenza</td>
		<td class="L"><select Title="Tipo Sentenza Riferimento"
			name="<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>">
			<%=tipoProvvedimentiRif%>
		</select></td>
	</tr>
	<tr>
		<td class="l">Data <div id="labelSentenza">Sentenza</div><div id="labelOrdinanza" style="display:none">Ordinanza</div></td>
		<td class="L"><input Title="Giorno Data Sentenza di Riferimento"
			type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvRif(),"dd")) %>"
			name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Mese Data Sentenza di Riferimento" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvRif(),"MM")) %>"
			name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Anno Data Sentenza di Riferimento" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvRif(),"yyyy")) %>"
			name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>
		<td class="l">Anno/Numero <div id="anSentenza">Sentenza</div><div id="anOrdinanza" style="display:none">Ordinanza</div></td>
		<td class="L"><input Title="Anno Sentenza Riferimento"
			value="<%=StringUtils.toStringJSP(lSentenza.getAnnoProvvRif())%>"
			type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_PROVV_RIF %>"
			maxlength="4" size="4"> /<input
			Title="Numero Sentenza Riferimento"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumeroProvvRif() )%>"
			type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF %>"
			maxlength="6" size="6"></td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente</td>
		<td class="L"><select Title="Autorità Sentenza Riferimento"
			onChange="ctrl_autorita('<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>', '<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>', 'D2', '<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO_RIF %>');"
			name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>">
			<%=autoritaProvRif%>
		</select></td>
		<td colspan=2>
		<%
			String visib2 = new String("hidden");
			if (modalita.equals("M")
					&& (lSentenza.getCodTipoAutoritaProvvRif().equals("DIB") || lSentenza
					.getCodTipoAutoritaProvvRif().equals("TRIBSD"))) {

				visib2 = "visible";
			}
		%>
		<div id=D2 STYLE="visibility: <%=visib2%>">
		<table width=100%>
			<tr>
				<td class="l">Tipo Rito</td>
				<td class="L"><select Title="Tipo Rito Riferimento"
					name="<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO_RIF %>">
					<%=tipoRito2%>
				</select></td>
			</tr>
		</table>		
		</div>
		</td>
	</tr>
	<tr>
		<td class="l">Luogo Emittente</td>
		<td class="L"><input Title="Luogo Sentenza Riferimento"
			value="<%=lSentenza.getDescrLuogoProvvRif() %>" type="text"
			name="<%= ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF %>"
			maxlength="35" size="35"> <a
			href="Javascript:ListaComuni('LoadInserisciSentenza','<%= ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF %>');">
		<img src="/images/filefolder.gif" border=0> </a></td>
		<td class="l">Sezione Autorità Emittente</td>
		<td class="L"><input Title="Sezione Autorità Riferimento"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaProvvRif()) %>"
			type="text"
			name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF %>"
			maxlength="100" size="35"></td>
	</tr>
	</table>
	<!---------------------------DECISIONE CASSAZIONE------------------------------->
	<div id="cassazione" style="display:block">
	<table cellspacing=2 cellpadding=2 width="100%">
	<tr>
		<td class="Titolo" colspan=4>Decisione Cassazione</td>
	</tr>
	<tr>
		<td class="l">Tipo Provvedimento</td>
		<td class="L">
			<select Title="Tipo Prevvedimento Cassazione"
				name="<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>"
				onChange="viewDiv(
					'0', 
					'LoadInserisciSentenza', 
					'<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>',
					'53')">
				<%=tipoProvvedimentiAltro%>
			</select>
		</td>
	</tr>
	<tr id="anRegGen">
		<td class="l">Anno/Numero Reg.Gen.</td>
		<td class="L"><!------------------------------------------------------------------------------
// Pezza d'appoggio....occhio che è palesemente na fregnaccia
// Gianluca 15.03.2004 NOTA commento aggiunto da Daniele il 19-01-2004
// per avere un riferimento (vedi siap.siep.sentenza.action.ActInserisciSentenza)
//---------------------------------------------------------------------------->
		<input Title="Anno Re.Ge. CASSAZIONE"
			value="<%=StringUtils.toStringJSP( lSentenza.getNote1DecisioneCassazione())%>"
			type="text" name="ANNOREGECAS" maxlength="4" size="4"> / <input
			Title="Numero Re.Ge. CASSAZIONE"
			value="<%=StringUtils.toStringJSP( lSentenza.getNote2DecisioneCassazione())%>"
			type="text" name="NUMREGECAS" maxlength="6" size="6"></td>
	</tr>
	<tr>
		<td class="l">Anno/Numero
		<div id="lblSentenza">Sentenza</div><div id="lblOrdinanza" style="display:none">Ordinanza</div>
		</td>	
		<td class="L"><input Title="Anno Sentenza Cassazione"
			value="<%=StringUtils.toStringJSP( lSentenza.getAnnoSentenzaCassazione())%>"
			type="text"
			name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE %>"
			maxlength="4" size="4"> / <input
			Title="Numero Sentenza Cassazione"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumeroSentenzaCassazione()) %>"
			type="text"
			name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE%>"
			maxlength="6" size="6"></td>
	</tr>
	<tr id="anRacGen"> 
		<td class="l">Anno/Numero Raccolta Generale</td>
		<td class="L"><input Title="Anno Raccolta Generale"
			value="<%=StringUtils.toStringJSP( lSentenza.getAnnoRaccoltaGenerale())%>"
			type="text"
			name="<%= ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>"
			maxlength="4" size="4"> / <input
			Title="Numero Raccolta Generale"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumeroRaccoltaGenerale())%>"
			type="text"
			name="<%= ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE %>"
			maxlength="6" size="6"></td>
	</tr>
	<tr id="disp">
		<td class="l">Dispositivo</td>
		<td class="L" colspan=3><select Title="Dispositivo Cassazione"
			name="<%= ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>">
			<%=tipoDecisioneCassazione%>
		</select></td>
	</tr>
	<!---------------------------------------------------------->
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	<%-- 
	<tr>
		<td class="Titolo" colspan=4>Irrevocabilità</td>
	</tr>
	<tr>
		<td class="l">Data Irrevocabilità <font class="ob">(*)</font></td>
		<td class="L" colspan=3><input
			Title="Giorno Data Irrevocabilità " type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"dd")) %>"
			name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Mese Data Irrevocabilità " type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"MM")) %>"
			name="<%= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Anno Data Irrevocabilità " type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"yyyy")) %>"
			name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>
	</tr>
	<tr>
		<td class="l">Numero Ufficio Recupero Crediti</td>
		<td class="L" colspan=3><input Title="Numero Campione Penale "
			type="text"
			value="<%=StringUtils.toStringJSP( lSentenza.getDescrNumCampionePenale()) %>"
			name="<%= ICostantiSentenza.CAMPO_DESCR_NUM_CAMPIONE_PENALE %>"
			maxlength="30" size="30"></td>
	</tr>
	--%>
	</table>
	</div>
	<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan=3><textarea cols=80 rows=5
			Title="Note Aggiuntive" name="<%=ICostantiSentenza.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(lSentenza.getNote())%></textarea>
		</td>
	</tr>
	<tr>
		<td colspan=2><br>
		<INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
		</td>
	</tr>
</table>

<input type="HIDDEN" name="Action" value="<%=lAction%>"> <input
	type="HIDDEN" name="<%=ICostantiSecurity.CAMPO_ID_FUNZIONE%>"
	value="<%=request.getAttribute(ICostantiSecurity.CAMPO_ID_FUNZIONE)%>">
<input type="HIDDEN" name="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>"
	value="<%=lSentenza.getIdSentenza()%>"> <input type="HIDDEN"
	name="lTipoFunzione" value="<%=lTipoFunzione%>"></form>
<script language="JavaScript" type="text/javascript">
//verifica iniziale per la sezione cassazione
ctrlDiv();

    var frmvalidator  = new Validator("LoadInserisciSentenza");

    /*
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>","req","Il Giorno di Arrivo dell'Atto è obbligatorio");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","numeric");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","gt=1");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","lt=31");

    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO %>","req","Il Mese di Arrivo dell'Atto è obbligatorio");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>","numeric");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>","gt=1");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>","lt=12");

    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO %>","req","L'Anno di Arrivo dell'Atto è obbligatorio");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","numeric");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","gt=1900");
    */

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>","req","Il Giorno della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>","req","Il Mese della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>","req","L'Anno della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM %>","req","L'Anno Re.Ge PM è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","gt=1900");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM %>","req","Il Numero Re.Ge PM è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>","numeric");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA %>","req","L'Anno Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","gt=1900");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>","req","Il Numero sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>","alfanumeric");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","L'Autorità Emittente è obbligatorio");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il Luogo Emittente è obbligatorio");
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

    /*
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","req","Il Giorno della Data irrevocabilità è obbligatorio");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","numeric");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","gt=1");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","lt=31");

    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","req","Il Mese della Data irrevocabilità è obbligatorio");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","numeric");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","gt=1");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","lt=12");

    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","req","L'Anno della Data irrevocabilità è obbligatorio");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","numeric");

    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>","req","Il Giorno della Data irrevocabilità è obbligatorio");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>","numeric");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>","gt=1");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>","lt=31");

    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>","req","Il Mese della Data irrevocabilità è obbligatorio");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>","numeric");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>","gt=1");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>","lt=12");

    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>","req","L'Anno della Data irrevocabilità è obbligatorio");
    frmvalidator.addValidation("< %= ICostantiSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>","numeric");
    */

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>