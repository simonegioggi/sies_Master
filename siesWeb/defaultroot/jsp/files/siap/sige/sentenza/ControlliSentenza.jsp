<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>

<script language="JavaScript">
	function Verify()
	{
	  <%-- Ticket#20221018019 - La data arrivo atto non è presente in form e sltavano tutti i controlli js ininserimento e modifica)
	  if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value.length==1)
	    document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value;
	  if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value.length==1)
	    document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value;
	  Ticket#20221018019 - FINE --%>
	  if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
	    document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
	  if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
	    document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
	  if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value.length==1)
	    document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value;
	  if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value.length==1)
	    document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value;
	
	  <%-- Ticket#20221018019 - La data arrivo atto non è presente in form e sltavano tutti i controlli js ininserimento e modifica)
	  //Data arrivo atto
	  var d1=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>.value;
	  if (! ControllaData(d1))
	  {
	    alert('Data di arrivo atto non valida');
	    return false;
	  }
	  --%>
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
	
	  <%-- Ticket#20221018019 - La data arrivo atto non è presente in form e sltavano tutti i controlli js ininserimento e modifica)
	  if (! CompareDate(d2,d1))
	  {
	    alert('La Data Sentenza deve essere antecedente alla Data di Arrivo');
	    return false;
	  }
	  --%>
	
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
	
	  if(TipoSentRif != '-' || GGSentRif != '' || MMSentRif != '' || AASentRif != '' || TipoAutRif != '-' || SedeRif != '')
	  {
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
	  return true;
	}
  
	function ctrl_autorita(idcmb1, idcmb2, idDiv, idTipoRito)
	{
		// cmb1 è la combo che fa scattare la funzione
		var cmb1 = document.getElementById(idcmb1);	
		var cmb2 = document.getElementById(idcmb2);
			
		var arrCmb = new Array(cmb1, cmb2);
		var arrGrado = new Array();
		
	 	for(var i=0;i<arrCmb.length;i++)
	 	{
			if (arrCmb[i].value == "CSS") 
			{
				arrGrado[i] = 3;
			}
			else if (arrCmb[i].value == "CAP" || arrCmb[i].value == "CASAP" || arrCmb[i].value == "CAPSM") 
			{
				arrGrado[i] = 2;
			}
			else
			{
				arrGrado[i] = 1;
			}
		}		
	 	
	 	if (cmb1.value != "-" && cmb2.value != "-")
	 	{
			if (cmb1.value == cmb2.value) 
			{
				alert("Non è consentito selezionare due Autorità Emittenti uguali!");
				cmb1.selectedIndex = 0;
				cmb1.focus();
			}
			else if (arrGrado[0] == arrGrado[1])
			{			
				// eccezione per Giudice di Pace e Tribunale Ordinario (anche sezione distaccata)
				if (!( (cmb1.value == "GP" || cmb2.value == "PT") && (cmb2.value == "DIB" || cmb2.value == "TRIBSD")) 
				 && !( (cmb2.value == "GP" || cmb2.value == "PT") && (cmb1.value == "DIB" || cmb1.value == "TRIBSD")))
				{
				
					alert("Non è consentito selezionare due Autorità Emittenti dello stesso grado!");
					cmb1.selectedIndex = 0;
					cmb1.focus();
				}
			}
		}	
				
		var node = document.getElementById(idDiv);
		var cmbRito = document.getElementById(idTipoRito);
	
		if (cmb1.value == "DIB" || cmb1.value == "TRIBSD")
		{
			node.style.visibility = "visible";
		}
		else
		{
			node.style.visibility = "hidden";
			cmbRito.selectedIndex = 0;		
		}
	}
</script>