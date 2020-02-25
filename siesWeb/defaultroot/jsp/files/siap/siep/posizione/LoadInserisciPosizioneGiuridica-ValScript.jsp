<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!--
***** SCRIPT DI VALIDAZIONE SEZIONE 1 ***** 
 -->

<%@page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@page import="siap.siep.misuracautelare.action.ICostantiMisuraCautelare"%>
<%@page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<script language="JavaScript" type="text/javascript">
function VerifyS1() {
	// MERGE v10: aggiunto controllo preventivo
    // Controllo di Obligatorietà Tipo Posizione Giuridica
    if (document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex] &&
    		document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value == '-') {
      alert('Il Tipo Posizione Giuridica è obbligatorio');
      document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.focus();
      return false;
    }

	var radioTipoAltraCausa=document.getElementsByName('tipoAltraCausa');
	var checkDetenutoAltraCausa=document.getElementById('<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>S1');
	
	if(radioTipoAltraCausa[0].checked) {
		// *** Controlli Sezione: Libero - Definitiva in istituto di detenzione ***       
		// Controllo di Obligatorietà Tipo Misura
    	if(checkDetenutoAltraCausa.checked) {
			if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA%>.selectedIndex].value == '-') {
	      		alert('Il Tipo Misura è obbligatorio');
	      		document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA%>.focus();
	      		return false;
	    	}
    	}
		// Controllo Data di Scadenza detenuto altra causa
	    if (document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.value.length==1)
	      document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.value='0'+document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.value;
	    if (document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>.value.length==1)
	      document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>.value='0'+document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>.value;

	    var dtScadenza=document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.value+'/'+document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>.value+'/'+document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>.value;
	    if (dtScadenza.length > 2 && !ControllaData(dtScadenza)) {
	      alert('Data Scadenza Altra Pena non valida');
	      document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.focus();
	      return false;
	    }

	} else if (radioTipoAltraCausa[1].checked) {
		// *** Controlli Sezione: Libero - Misura cautelare in istituto detenzione ***

		// Controllo di Obligatorietà Tipo Ufficio PM
    	if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_Tipo_L2[document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_Tipo_L2.selectedIndex].value == '-') {
      		alert('Il Tipo Ufficio PM è obbligatorio');
      		document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_Tipo_L2.focus();
      		return false;
    	}
	
		// Controllo di Obligatorietà Sede
	    if(!document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_L2.disabled)
	    {
	      if (document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_L2.value == '')
	      {
	         alert("Il Campo Sede è obbligatorio");
	         return false;
	      }
	    } 
				
		// Controllo di Obligatorietà Autorità Emittente
    	if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE%>_L2[document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE%>_L2.selectedIndex].value == '-') {
      		alert('Autorità Emittente è obbligatoria');
      		document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE%>_L2.focus();
      		return false;
    	}
		
		// Controllo di Obligatorietà Tipo Misura
    	if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA%>_L2[document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA%>_L2.selectedIndex].value == '-') {
      		alert('Il Tipo Misura è obbligatorio');
      		document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA%>_L2.focus();
      		return false;
    	}
		
	} else if (radioTipoAltraCausa[2].checked) {
		// *** Controlli Sezione: Libero - Misura cautelare in altro luogo ***

		// Controllo di Obligatorietà Tipo Ufficio PM
    	if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_Tipo_L3[document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_Tipo_L3.selectedIndex].value == '-') {
      		alert('Il Tipo Ufficio PM è obbligatorio');
      		document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_Tipo_L3.focus();
      		return false;
    	}
	
		// Controllo di Obligatorietà Sede
	    if(!document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_L3.disabled)
	    {
	      if (document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_L3.value == '')
	      {
	         alert("Il Campo Sede è obbligatorio");
	         return false;
	      }
	    } 
				
		// Controllo di Obligatorietà Autorità Emittente
    	if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE%>_L3[document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE%>_L3.selectedIndex].value == '-') {
      		alert('Autorità Emittente è obbligatoria');
      		document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE%>_L3.focus();
      		return false;
    	}
		
		// Controllo di Obligatorietà Tipo Misura
    	if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA%>_L3[document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA%>_L3.selectedIndex].value == '-') {
      		alert('Il Tipo Misura è obbligatorio');
      		document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA%>_L3.focus();
      		return false;
    	}	
	}

    return true;
}
</script>

<script language="JavaScript" type="text/javascript">
var frmvalidatorS1 = new Validator("LoadInserisciPosizioneGiuridicaS1");

frmvalidatorS1.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>","req","Il tipo Posizione Giuridica è obbligatorio");

//frmvalidatorS1.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>","numeric");
//frmvalidatorS1.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>","gt=1900");
//frmvalidatorS1.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>","lt=3000");

frmvalidatorS1.setAddnlValidationFunction("VerifyS1");
</script>


<!--
***** SCRIPT DI VALIDAZIONE SEZIONE 2 ***** 
 -->
<script language="JavaScript" type="text/javascript">
function VerifyS2() {

    // Controllo di Obligatorietà Tipo Posizione Giuridica
    if( document.LoadInserisciPosizioneGiuridicaS2.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaS2.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value == '-') {
      alert('Il Tipo Posizione Giuridica è obbligatorio');
      document.LoadInserisciPosizioneGiuridicaS2.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.focus();
      return false;
    }

    // Controllo Data di Decorrenza posizione giuridica
    if (document.LoadInserisciPosizioneGiuridicaS2.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.value.length==1)
      document.LoadInserisciPosizioneGiuridicaS2.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.value='0'+document.LoadInserisciPosizioneGiuridicaS2.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.value;
    if (document.LoadInserisciPosizioneGiuridicaS2.<%=ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>.value.length==1)
      document.LoadInserisciPosizioneGiuridicaS2.<%=ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>.value='0'+document.LoadInserisciPosizioneGiuridicaS2.<%=ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>.value;

    var dtDecorrenza=document.LoadInserisciPosizioneGiuridicaS2.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+document.LoadInserisciPosizioneGiuridicaS2.<%=ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>.value+'/'+document.LoadInserisciPosizioneGiuridicaS2.<%=ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>.value;
    if ((dtDecorrenza.length > 2 && dtDecorrenza.length < 10) || (dtDecorrenza.length != 2 && !ControllaData(dtDecorrenza)) ) {
        alert('Data di Decorrenza Pena non valida');
        //document.LoadInserisciPosizioneGiuridicaS2.<--%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.focus();
        return false;
    } else if(!ControllaData(dtDecorrenza)){
	      alert('La Data di Decorrenza Pena è obbligatoria per questa posizione!');
	      return false;
	} 
   

    return true;
}
</script>

<script language="JavaScript" type="text/javascript">
var frmvalidatorS2 = new Validator("LoadInserisciPosizioneGiuridicaS2");

frmvalidatorS2.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>","req","Il tipo Posizione Giuridica è obbligatorio");

frmvalidatorS2.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>","numeric");
frmvalidatorS2.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
frmvalidatorS2.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>","lt=3000");

frmvalidatorS2.setAddnlValidationFunction("VerifyS2");
</script>


<!--
***** SCRIPT DI VALIDAZIONE SEZIONE 3 ***** 
 -->

 <script language="JavaScript" type="text/javascript">
function VerifyS3() {

    // Controllo di Obligatorietà Tipo Posizione Giuridica
    if( document.LoadInserisciPosizioneGiuridicaS3.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaS3.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value == '-') {
      alert('Posizione Giuridica è obbligatorio');
      document.LoadInserisciPosizioneGiuridicaS3.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.focus();
      return false;
    }

	// Controllo Data di Decorrenza posizione giuridica
    if (document.LoadInserisciPosizioneGiuridicaS3.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.value.length==1)
      document.LoadInserisciPosizioneGiuridicaS3.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.value='0'+document.LoadInserisciPosizioneGiuridicaS3.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.value;
    if (document.LoadInserisciPosizioneGiuridicaS3.<%=ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>.value.length==1)
      document.LoadInserisciPosizioneGiuridicaS3.<%=ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>.value='0'+document.LoadInserisciPosizioneGiuridicaS3.<%=ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>.value;

    var d1=document.LoadInserisciPosizioneGiuridicaS3.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+document.LoadInserisciPosizioneGiuridicaS3.<%=ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>.value+'/'+document.LoadInserisciPosizioneGiuridicaS3.<%=ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>.value;
    if ((d1.length > 2 && d1.length < 10) || (d1.length != 2 && !ControllaData(d1)) ) {
        alert('Data di Decorrenza Pena non valida');
        //document.LoadInserisciPosizioneGiuridicaS2.<--%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.focus();
        return false;
    } else if (!ControllaData(d1)) {
		alert('La Data di Decorrenza Pena è obbligatoria per questa posizione!');
	    return false;
	} 

    return true;
}
</script>

<script language="JavaScript" type="text/javascript">
var frmvalidatorS3 = new Validator("LoadInserisciPosizioneGiuridicaS3");

frmvalidatorS3.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>","req","Il tipo Posizione Giuridica è obbligatorio");

frmvalidatorS3.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>","numeric");
frmvalidatorS3.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
frmvalidatorS3.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>","lt=3000");

frmvalidatorS3.setAddnlValidationFunction("VerifyS3");
</script>
 