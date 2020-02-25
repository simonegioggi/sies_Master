<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel"%>
<%@ page import="siap.siep.altrigradigiudizio.action.ICostantiAltriGradiGiudizio"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="altrigradigiudizio" scope="request" class="siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel" />
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

<head>
<title>[S.I.E.S.] - Gestione Altri Gradi Giudizio</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
	    // 15/07/2010 Lista Uffici per TIPO_UFFICIO
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
      if (document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_I_GRADO%>.value.length==1)
        document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_I_GRADO%>.value='0'+document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_I_GRADO%>.value;
      if (document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_I_GRADO%>.value.length==1)
        document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_I_GRADO%>.value='0'+document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_I_GRADO%>.value;
      if (document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_II_GRADO%>.value.length==1)
        document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_II_GRADO%>.value='0'+document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_II_GRADO%>.value;
      if (document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_II_GRADO%>.value.length==1)
       document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_II_GRADO%>.value='0'+document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_II_GRADO%>.value;
     /*
      if (document.LoadInserisciAltriGradiGiudizio.< %=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciAltriGradiGiudizio.< %=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciAltriGradiGiudizio.< %=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
      if (document.LoadInserisciAltriGradiGiudizio.< %=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciAltriGradiGiudizio.< %=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciAltriGradiGiudizio.< %=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;
     */

      //Data arrivo atto
      /*
      var d1=document.LoadInserisciAltriGradiGiudizio.< %=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value
      +'/'+document.LoadInserisciAltriGradiGiudizio.< %=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value
      +'/'+document.LoadInserisciAltriGradiGiudizio.< %=ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>.value;
      if (! ControllaData(d1))
      {
        alert('Data di arrivo atto non valida');
        return false;
      }*/
      //Data Sentenza primo grado
      var d2=document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_I_GRADO%>.value+'/'+document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_I_GRADO%>.value+'/'+document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_I_GRADO%>.value;
      if (! ControllaData(d2))
      {
        alert('Data Sentenza I° grado non valida');
        return false;
      }
      //Data Sentenza di Secondo grado
      var d3=document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_II_GRADO%>.value+'/'+document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_II_GRADO%>.value+'/'+document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_II_GRADO%>.value;
      if (! ControllaData(d3) && d3.length>2)
      {
        alert('Data Sentenza II° grado non valida');
        return false;
      }
 
/*
  -- Commentato Rework del 17-06-2003 --
  UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
  UfficioModel lUffMod = lUteMod.getUfficioUtente();
  String TipoUff = new String(lUteMod.getUfficioUtente().getDescrTipoUfficio());
  

   * Nel caso in cui l'utente inserisca almeno uno tra i seguenti campi:
   *  - Tipo Sentenza di Riferimento
   *  - Data Sentenza di Riferimento
   *  - Autorità Sentenza di Riferimento
   *  - Luogo Sentenza di Riferimento
   *  deve inserirli tutti
   */
    var TipoSentRif = document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_COD_TIPO_SENTENZA_II_GRADO %>.value;
    var GGSentRif   = document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_II_GRADO %>.value;
    var MMSentRif   = document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_II_GRADO %>.value;
    var AASentRif   = document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_II_GRADO %>.value;
    var TipoAutRif  = document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_II_GRADO %>.value;
    var SedeRif     = document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_II_GRADO %>.value;
    //var CodTipoProvv= document.LoadInserisciAltriGradiGiudizio.< %= ICostantiAltriGradiGiudizio.CAMPO_COD_TIPO_SENTENZA_II_GRADO %>.value;

    var GGSentRifI   = document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_I_GRADO %>.value;
    var MMSentRifI   = document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_I_GRADO %>.value;
    var AASentRifI   = document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_I_GRADO %>.value;
    var TipoAutRifI  = document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_I_GRADO %>.value;
    var SedeRifI     = document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_I_GRADO %>.value;
    //var CodTipoProvvI= document.LoadInserisciAltriGradiGiudizio.< %= ICostantiAltriGradiGiudizio.CAMPO_COD_TIPO_SENTENZA_I_GRADO %>.value;



if(GGSentRifI != '' || MMSentRifI != '' || AASentRifI != '' || TipoAutRifI != '-' || SedeRifI != '')
    {
    	
      if(GGSentRifI == '')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_I_GRADO %>.focus();

        return false;
      }
      if(MMSentRifI == '')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_I_GRADO %>.focus();

        return false;
      }
      if(AASentRifI == '')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_I_GRADO %>.focus();

        return false;
      }
      if(TipoAutRifI == '-')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_I_GRADO %>.focus();

        return false;
      }
      if(SedeRifI == '')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_I_GRADO %>.focus();

        return false;
      }
    }


    if(TipoSentRif != '-' || GGSentRif != '' || MMSentRif != '' || AASentRif != '' || TipoAutRif != '-' || SedeRif != '')
    {
    	
      if(TipoSentRif == '-')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_COD_TIPO_SENTENZA_II_GRADO %>.focus();

        return false;
      }
      if(GGSentRif == '')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_II_GRADO %>.focus();

        return false;
      }
      if(MMSentRif == '')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_II_GRADO %>.focus();

        return false;
      }
      if(AASentRif == '')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_II_GRADO %>.focus();

        return false;
      }
      if(TipoAutRif == '-')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_II_GRADO %>.focus();

        return false;
      }
      if(SedeRif == '')
      {
        alert('Dati della Sentenza di Riferimento Incompleti');
        document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_II_GRADO %>.focus();

        return false;
      }
    }
    /* 
	controllo coerenza date sentenza realizzato inanalogia a quanto sviluppato per i webservices
	(vedi siap.sico.webservices.action.ActNscToSiesLoadSentenza.java)
	*/
	var auEmi = document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_I_GRADO %>.value;
	var dRif=document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_II_GRADO%>.value+'/'+document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_II_GRADO%>.value+'/'+document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_II_GRADO%>.value;
	
	if(dRif!=null && dRif!='//'){
		if(auEmi=="CAP" || 
			auEmi=="CAS" || 
			auEmi=="CASAP" || 
			auEmi=="CAPSM" || 
			auEmi=="CAPMI"){
			if (CompareDate(d2,dRif)){
	        	alert('La Data Sentenza deve essere antecedente alla Data della Sentenza di grado differente');
	        	return false;
	      	}
		}
		else{	
			if (CompareDate(dRif,d2)){
	        	alert('La Data della Sentenza di grado differente deve essere antecedente alla Data Sentenza');
	        	return false;
	      	}
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
		if (document.LoadInserisciAltriGradiGiudizio.<%= ICostantiAltriGradiGiudizio.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>.value=='53')
		{
			viewDiv('0', 'LoadInserisciAltriGradiGiudizio', '<%= ICostantiAltriGradiGiudizio.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>','53')
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
 	AltriGradiGiudizioModel lAltriGradiGiudizio = new AltriGradiGiudizioModel();
 	String lAction = new String();

 	if (modalita.equals("I")) {
 		lAction = "siap.siep.altrigradigiudizio.action.ActInserisciAltriGradiGiudizio";
 %> <font class="campo">Inserimento Altri Gradi Giudizio</font> <%
 		} else if (modalita.equals("M")) {
 		lAction = "siap.siep.altrigradigiudizio.action.ActModificaAltriGradiGiudizio";
 		lAltriGradiGiudizio = new AltriGradiGiudizioModel(altrigradigiudizio);
 %> <font class="campo">Modifica Altri Gradi Giudizio</font> <%
 }
 %>
		</td>
	</tr>
</table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"
	name="LoadInserisciAltriGradiGiudizio">

<table cellspacing=2 cellpadding=2>	
	
	<!---------------------------------------------------------->
	<tr>
		<td class="Titolo" colspan=4>Primo Grado</td>
	</tr>
	<tr>
		<td class="l">Data Sentenza <font class="ob">(*)</font></td>
		<td class="L"><input Title="Data Sentenza" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAltriGradiGiudizio.getDataSentenzaIGrado(),"dd")) %>"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_I_GRADO %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Sentenza" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAltriGradiGiudizio.getDataSentenzaIGrado(),"MM")) %>"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_I_GRADO %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Sentenza" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAltriGradiGiudizio.getDataSentenzaIGrado(),"yyyy")) %>"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_I_GRADO %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>
		<td class="l">Anno/Numero Sentenza <font class="ob">(*)</font></td>
		<td class="L"><input Title="Anno Sentenza"
			value="<%=StringUtils.toStringJSP( lAltriGradiGiudizio.getAnnoSentenzaIGrado()) %>"
			type="text" name="<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_SENTENZA_I_GRADO %>"
			maxlength="4" size="4"> /<input Title="Numero Sentenza"
			value="<%= lAltriGradiGiudizio.getNumeroSentenzaIGrado()%>" type="text"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_NUMERO_SENTENZA_I_GRADO %>" maxlength="6"
			size="6"></td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
		<td class="L"><select Title="Autorità Emittente"
			onChange="ctrl_autorita('<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_I_GRADO %>', '<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_II_GRADO %>', 'D1', '<%= ICostantiAltriGradiGiudizio.CAMPO_COD_TIPO_RITO %>');"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_I_GRADO %>">
			<%=autoritaEmi%>
		</select></td>
		<td colspan=2>
		<%
			String visib1 = new String("hidden");
			if (modalita.equals("M")
					&& (lAltriGradiGiudizio.getCodAutEmittSentIGrado().equals("DIB") || 
							lAltriGradiGiudizio.getCodAutEmittSentIGrado().equals("TRIBSD"))) {

				visib1 = "visible";
			}
		%>
		<div id=D1 STYLE="visibility: <%=visib1%>">
		<table width=100%>
			<tr>
				<td class="l">Tipo Rito</td>
				<td class="L"><select Title="Tipo Rito"	name="<%= ICostantiAltriGradiGiudizio.CAMPO_COD_TIPO_RITO %>">
					<%=tipoRito1%>
				</select></td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
	<tr>
		<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
		<td class="L">
		<input Title="Luogo Emittente" name="<%=ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_I_GRADO%>"
			value="<%=lAltriGradiGiudizio.getDescrLuoEmittSentIGrado()%>" type="text" maxlength="35" size="35"> 
			<%-- a href="Javascript:ListaComuni('LoadInserisciAltriGradiGiudizio','<%= ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_I_GRADO %>');"> --%>
      <a href="Javascript:ListaUfficiPerTipo('LoadInserisciAltriGradiGiudizio','<%=ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_I_GRADO%>',document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_I_GRADO %>[document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_I_GRADO%>.selectedIndex].value);">
				<img src="/images/filefolder.gif" border=0> 
			</a>
		</td>
		<td class="L">Sezione Autorità Emittente</td>
		<td class="L"><input Title="Sezione Autorità Emittente"
			value="<%= StringUtils.toStringJSP(lAltriGradiGiudizio.getNumSezEmittSentIGrado()) %>"
			type="text"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_NUM_SEZ_EMITT_SENT_I_GRADO %>"
			maxlength="35" size="35"></td>
	</tr>
	
	<!---------------------------------------------------------->
	<tr>
		<td class="Titolo" colspan=4>Secondo Grado</td>
	</tr>	
	<tr id="tipoSentenza" style="display:block">
		<td class="l">Tipo Sentenza</td>
		<td class="L"><select Title="Tipo Sentenza Riferimento"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_COD_TIPO_SENTENZA_II_GRADO %>">
			<%=tipoProvvedimentiRif%>
		</select></td>
	</tr>
	<tr>
		<td class="l">Data <div id="labelSentenza">Sentenza</div><div id="labelOrdinanza" style="display:none">Ordinanza</div></td>
		<td class="L"><input Title="Giorno Data Sentenza di Riferimento"
			type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAltriGradiGiudizio.getDataSentenzaIiGrado(),"dd")) %>"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_II_GRADO %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Mese Data Sentenza di Riferimento" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAltriGradiGiudizio.getDataSentenzaIiGrado(),"MM")) %>"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_II_GRADO%>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Anno Data Sentenza di Riferimento" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAltriGradiGiudizio.getDataSentenzaIiGrado(),"yyyy")) %>"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_II_GRADO %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>			
			<td class="l">Anno/Numero <div id="anSentenza">Sentenza</div><div id="anOrdinanza" style="display:none">Ordinanza</div></td>
			<td class="L"><input Title="Anno Sentenza"
			value="<%=StringUtils.toStringJSP( lAltriGradiGiudizio.getAnnoSentenzaIiGrado()) %>"
			type="text" name="<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_SENTENZA_II_GRADO %>"
			maxlength="4" size="4"> /<input Title="Numero Sentenza"
			<% if (lAltriGradiGiudizio.getNumeroSentenzaIiGrado() != null) {
				%>
					value="<%= lAltriGradiGiudizio.getNumeroSentenzaIiGrado()%>" type="text"
					name="<%= ICostantiAltriGradiGiudizio.CAMPO_NUMERO_SENTENZA_II_GRADO %>" maxlength="6"
					size="6">
			<%
			}else{ 
			%>
					value="" type="text"
					name="<%= ICostantiAltriGradiGiudizio.CAMPO_NUMERO_SENTENZA_II_GRADO %>" maxlength="6"
					size="6">					
			<%} %>
			</td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente</td>
		<td class="L"><select Title="Autorità Sentenza Riferimento"
			onChange="ctrl_autorita('<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_II_GRADO %>', '<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_I_GRADO %>', 'D2', '<%= ICostantiAltriGradiGiudizio.CAMPO_COD_TIPO_RITO_RIF %>');"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_II_GRADO %>">
			<%=autoritaProvRif%>
		</select></td>
		<td colspan=2>
		<%
			String visib2 = new String("hidden");
			if (modalita.equals("M")
					&& (lAltriGradiGiudizio.getCodAutEmittSentIiGrado().equals("DIB") || 
							lAltriGradiGiudizio.getCodAutEmittSentIiGrado().equals("TRIBSD"))) {

				visib2 = "visible";
			}
		%>
		<div id=D2 STYLE="visibility: <%=visib2%>">
		<table width=100%>
			<tr>
				<td class="l">Tipo Rito</td>
				<td class="L"><select Title="Tipo Rito Riferimento"
					name="<%= ICostantiAltriGradiGiudizio.CAMPO_COD_TIPO_RITO_RIF %>">
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
		<% if (lAltriGradiGiudizio.getDescrLuoEmittSentIiGrado().length() > 1) {
		%>
			value="<%=lAltriGradiGiudizio.getDescrLuoEmittSentIiGrado() %>"
		<%
		}else{ 
		%>
				value=""					
	  	<%} %>
			type="text"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_II_GRADO %>"
			maxlength="35" size="35"> 
			<%-- a href="Javascript:ListaComuni('LoadInserisciAltriGradiGiudizio','<%= ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_II_GRADO %>');"--%>
      <a href="Javascript:ListaUfficiPerTipo('LoadInserisciAltriGradiGiudizio','<%=ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_II_GRADO%>',document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_II_GRADO %>[document.LoadInserisciAltriGradiGiudizio.<%=ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_II_GRADO%>.selectedIndex].value);">
			<img src="/images/filefolder.gif" border=0> </a></td>
			<td class="l">Sezione Autorità Emittente</td>
			<td class="L"><input Title="Sezione Autorità Riferimento"
			value="<%=StringUtils.toStringJSP(lAltriGradiGiudizio.getNumSezEmittSentIiGrado()) %>"
			type="text"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_NUM_SEZ_EMITT_SENT_II_GRADO %>"
			maxlength="100" size="35"></td>
	</tr>
	</table>
	<!---------------------------DECISIONE CASSAZIONE------------------------------->
	<div id="cassazione" style="display:block">
	<table cellspacing=2 cellpadding=2 width="100%">
	<tr>
		<td class="Titolo" colspan=4>Sentenza Cassazione di Rinvio</td>
	</tr>
	
	<tr id="anRegGen">
		<td class="l">Anno/Numero Reg.Gen.</td>
		<td class="L"><!------------------------------------------------------------------------------
// Pezza d'appoggio....occhio che è palesemente na fregnaccia
// Gianluca 15.03.2004 NOTA commento aggiunto da Daniele il 19-01-2004
// per avere un riferimento (vedi siap.siep.sentenza.action.ActInserisciSentenza)
//---------------------------------------------------------------------------->
		<input Title="Anno Re.Ge. CASSAZIONE"
			value="<%=StringUtils.toStringJSP(lAltriGradiGiudizio.getAnnoRegGenCassaz())%>"
			type="text" name="<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_REG_GEN_CASSAZ %>" maxlength="4" size="4"> / <input
			Title="Numero Re.Ge. CASSAZIONE"
			value="<%=StringUtils.toStringJSP( lAltriGradiGiudizio.getNumeroRegGenCassaz())%>"
			type="text" name="<%= ICostantiAltriGradiGiudizio.CAMPO_NUMERO_REG_GEN_CASSAZ %>" maxlength="6" size="6"></td>
	</tr>
	<tr>
		<td class="l">Anno/Numero
		<div id="lblSentenza">Sentenza</div><div id="lblOrdinanza" style="display:none">Ordinanza</div>
		</td>	
		<td class="L"><input Title="Anno Sentenza Cassazione"
			value="<%=StringUtils.toStringJSP( lAltriGradiGiudizio.getAnnoSentenzaCassaz())%>"
			type="text"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_SENTENZA_CASSAZ %>"
			maxlength="4" size="4"> / <input
			Title="Numero Sentenza Cassazione"
			value="<%=StringUtils.toStringJSP(lAltriGradiGiudizio.getNumeroSentenzaCassaz()) %>"
			type="text"
			name="<%= ICostantiAltriGradiGiudizio.CAMPO_NUMERO_SENTENZA_CASSAZ%>"
			maxlength="6" size="6"></td>
	</tr>
	<tr id="anRacGen"> 
		<td class="l">Anno/Numero Raccolta Generale</td>
		<td class="L">
			<input Title="Anno Raccolta Generale"	value="<%=StringUtils.toStringJSP( lAltriGradiGiudizio.getAnnoRaccGenealeIiGrado())%>" 
					type="text" name="<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_RACC_GENEALE_II_GRADO %>" maxlength="4" size="4"> /
			<input Title="Numero Raccolta Generale" value="<%=StringUtils.toStringJSP(lAltriGradiGiudizio.getNumeroRaccGenealeIiGrado())%>" 
					type="text" name="<%= ICostantiAltriGradiGiudizio.CAMPO_NUMERO_RACC_GENEALE_II_GRADO %>" maxlength="6" size="6">
		</td>
	</tr>
	<tr id="disp">
		<td class="l">Dispositivo</td>
		<td class="L" colspan=3><select Title="Dispositivo Cassazione" name="<%= ICostantiAltriGradiGiudizio.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>">
			<%=tipoDecisioneCassazione%>
		</select></td>
	</tr>
	
	</table>
	</div>
	<table cellspacing=2 cellpadding=2>
	
	<tr>
		<td colspan=2><br>
		<INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
		</td>
	</tr>
</table>

<input type="HIDDEN" name="Action" value="<%= lAction %>"> 
<input type="HIDDEN" name="<%= ICostantiSecurity.CAMPO_ID_FUNZIONE %>" value="<%= request.getAttribute(ICostantiSecurity.CAMPO_ID_FUNZIONE) %>">
<input type="HIDDEN" name="<%= ICostantiAltriGradiGiudizio.CAMPO_ID_ALTRIGRADIGIUDIZIO %>" value="<%= lAltriGradiGiudizio.getIdAltrigradigiudizio() %>"> 
<input type="HIDDEN" name="<%= ICostantiAltriGradiGiudizio.CAMPO_SEN_ID_SENTENZA %>" value="<%= lAltriGradiGiudizio.getSenIdSentenza() %>"> 
<input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>"> </form>

<script language="JavaScript" type="text/javascript">
	//verifica iniziale per la sezione cassazione
	ctrlDiv();

    var frmvalidator  = new Validator("LoadInserisciAltriGradiGiudizio");

    
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_I_GRADO%>","req","Il Giorno della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_I_GRADO %>","numeric");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_I_GRADO %>","gt=1");

    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_I_GRADO %>","req","Il Mese della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_I_GRADO %>","numeric");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_I_GRADO %>","gt=1");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_I_GRADO %>","lt=12");

    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_I_GRADO %>","req","L'Anno della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_I_GRADO %>","numeric");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_I_GRADO %>","gt=1900");

    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_SENTENZA_I_GRADO %>","req","L'Anno Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_SENTENZA_I_GRADO %>","numeric");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_SENTENZA_I_GRADO %>","gt=1900");

    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_NUMERO_SENTENZA_I_GRADO %>","req","Il Numero sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_NUMERO_SENTENZA_I_GRADO %>","alfanumeric");

    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_I_GRADO %>","req","L'Autorità Emittente è obbligatorio");

    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_I_GRADO %>","req","Il Luogo Emittente è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_I_GRADO %>","alphabetic");

    //frmvalidator.addValidation("< %= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_II_GRADO%>","req","Il Giorno della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_II_GRADO %>","numeric");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_GIORNO_DATA_SENTENZA_II_GRADO %>","gt=1");

    //frmvalidator.addValidation("< %= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_II_GRADO %>","req","Il Mese della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_II_GRADO %>","numeric");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_II_GRADO %>","gt=1");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_MESE_DATA_SENTENZA_II_GRADO %>","lt=12");

    //frmvalidator.addValidation("< %= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_II_GRADO %>","req","L'Anno della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_II_GRADO %>","numeric");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_DATA_SENTENZA_II_GRADO %>","gt=1900");

    //frmvalidator.addValidation("< %= ICostantiAltriGradiGiudizio.CAMPO_ANNO_SENTENZA_II_GRADO %>","req","L'Anno Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_SENTENZA_II_GRADO %>","numeric");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_ANNO_SENTENZA_II_GRADO %>","gt=1900");

    //frmvalidator.addValidation("< %= ICostantiAltriGradiGiudizio.CAMPO_NUMERO_SENTENZA_II_GRADO %>","req","Il Numero sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_NUMERO_SENTENZA_II_GRADO %>","alfanumeric");

    //frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_COD_AUT_EMITT_SENT_II_GRADO %>","req","L'Autorità Emittente è obbligatorio");

    frmvalidator.addValidation("<%= ICostantiAltriGradiGiudizio.CAMPO_COD_LUO_EMITT_SENT_II_GRADO %>","alphabetic");

    
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>