<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraCautelareCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPresoffertoCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       	scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="StatoEsecTitoloCum"   	scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="ComputoCum"	   		scope="request" class="siap.siep.modulocumulo.model.ComputiCumuloModel"/>

<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoMisuraDetentive"    scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraNonDetentive" scope="request" class="java.lang.String"/>

<jsp:useBean id="misurasicurezzacumulo" scope="request" class="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"/>
<jsp:useBean id="naturaMisuraSicurezza" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraSicurezza"   scope="request" class="java.lang.String"/>

<jsp:useBean id="autoritaEmi"  			scope="request" class="java.lang.String"/>
<jsp:useBean id="UfficioEsecEmittente" 	scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEmiSIEP"		scope="request" class="java.lang.String"/>
<jsp:useBean id="UfficioPM"			 	scope="request" class="java.lang.String" />

<jsp:useBean id="ComputiPDAR" 			scope="request" class="java.lang.String" />
<jsp:useBean id="ComputiMCAR"			scope="request" class="java.lang.String"/>

<!-- 		LoadInserisciFungibilitaCumulo		 -->

<% 
//============================================================================== 
// Form per l'inserimento e la modifica dei periodi fungibili
//============================================================================== 

ComputiCumuloModel aComputo = new ComputiCumuloModel();

if ( modalita.equals("M") )
{
	aComputo = ComputoCum;
}

String lBlocca="display:none;";
if ( modalita.equals("M") )
{
	if("G".equals(StatoEsecTitoloCum.getCodTipoIstante()) )
	{	
		lBlocca="display:block;";
	}	
}

String lDisp ="style=\"display:block\"";
if ( modalita.equals("M") )
{
	if(StatoEsecTitoloCum.getCodMotivo().equals("0213"))
	{
		lDisp ="style=\"display:none\"";
	}
}

IstitutoDetenzioneModel IstitutoDetenzione = null;
if (aComputo!=null && aComputo.getIstitutoDetenzione()!=null)
{	
  IstitutoDetenzione = (IstitutoDetenzioneModel)aComputo.getIstitutoDetenzione();
}  
  
String TipoEspiazione = "";
String lAltroLuogo = "";
if (aComputo!=null){
  TipoEspiazione = aComputo.getTipoEspiazioneMC();
  lAltroLuogo = aComputo.getAltroLuogoDetenzione();
}


%> 

<html>
<head>
  <title> Gestione Fungibilità Cumulo </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="/html/jsrsClient.js"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_JQUERY%>></script>
  
  <script language="JavaScript" >

  var anniQuantumCalcolati = 0;
  var mesiQuantumCalcolati = 0;
  var giorniQuantumCalcolati = 0;
  var totGGMessaAllaProva = 0;
  
  var testOnLoad = 'S';

   	 
   	 function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
    {
      var desktop = window.open("/jsp/Main.jsp?Action=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&LoadDescEstesa=SI", "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
    
    function pulisciIstituto (nomeCampoComune, nomeCampoId){
      var campoDescr = document.getElementsByName(nomeCampoComune)[0];
      var campoId    = document.getElementsByName(nomeCampoId)[0];
      campoDescr.value="";
      campoId.value="";
    }
   	 
    function Verify() 
    { 
   	 	// Tipo Provvedimento (COD_MOTIVO)
   	 	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.value.length==1 ||
 			document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.value=='-')
   	 	{
   	 		alert('scegliere tipo di Provvedimento di riconoscimento');
   	 		document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.focus();
   	 		return false;
   	 	}	
   	 
    	 // Data Emissione
      	if (document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        	document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        if (document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        	document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value;

        var data_to_verify=document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>.value;
              
        if (data_to_verify=="//")
        {
        	alert('Data Emissione obbligatoria');
            document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
            return false;
        }
        else if (!ControllaData(data_to_verify))
        {
            alert('Data Emissione non valida');
            document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
            return false;
        }
        
     	// Data Emissione deve essere <= SYSDATE
        var sysDate = new Date();
  
        var ggSysDate = sysDate.getDate();
        if(ggSysDate<10)
          ggSysDate = "0"+ggSysDate;
  
        var mmSysDate = (sysDate.getMonth()+1);
        if(mmSysDate<10)
          mmSysDate = "0"+mmSysDate;
  
        var yyyySysDate = sysDate.getYear()
  
        var strSysDate = ggSysDate + "/" + mmSysDate + "/" + yyyySysDate;

        if (!CompareDate(data_to_verify, strSysDate))
        {
          	alert('Data di Emissione superiore alla data attuale');
          	document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
          	return false;
        }
        
        // Controllo Cod Tipo Istante : Su richiesta di....
		if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE%>[3].checked )
		{
			//alert('radio Button Tipo Istante: Check su Giudice ESECUZIONE');
			// Tipo Autorita Emittemete e relativa Sede 
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_PROVV_RIF%>.value != "" &&
      	  	   document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF%>.value == "-")
	      	{
	      		alert('inserire Tipo Autorita Emittente Ordinanza');
	      		document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF%>.focus();
	      		return false;
	      	}
	      	
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_PROVV_RIF%>.value == "" &&
      	  	   document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF%>.value != "-")
	      	{
	      		alert('inserire Luogo Autorita Emittente Ordinanza');
	      		document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_PROVV_RIF%>.focus();
	      		return false;
	      	}
	      	
	      	// Anno e numero Procedimento SIGE
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_NUMERO_PROVV%>.value == "" &&
      		   document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_ANNO_PROVV%>.value != ""  )
	      	{ 
	      		alert('inserire Numero Procedimento SIGE.');
        	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_NUMERO_PROVV%>.focus();
        	 	return false; 
	      	}
	      	
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_NUMERO_PROVV %>.value != "" &&
      			document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_ANNO_PROVV %>.value == ""  )
	      	{
	      		alert('inserire Anno Procedimento SIGE');
        	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_ANNO_PROVV %>.focus();
        	 	return false; 
	      	}
	      	
	      	 // Data ordinanza del GE (Non Obbligatoria)
	      	if (document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA %>.value.length==1)
	        	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA %>.value='0'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA %>.value;
	        if (document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RICHIESTA %>.value.length==1)
	        	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RICHIESTA %>.value='0'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RICHIESTA %>.value;

	        var data_to_verify=document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA %>.value+'/'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RICHIESTA %>.value+'/'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RICHIESTA %>.value;
	        
	        if (data_to_verify!="//")
	      	{ 
		        if (!ControllaData(data_to_verify) )
		        {
		            alert('Data Ordinanza del G.E. non valida');
		            document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA %>.focus();
		            return false;
		        }
		        
		        if (!CompareDate(data_to_verify, strSysDate))
		        {
		          	alert('Data Ordinanza del G.E. superiore alla data attuale');
		          	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA %>.focus();
		          	return false;
		        }
	      	}  
		}	
		else if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE%>[1].checked)
		{
			//alert('radio Button Tipo Istante: Check su DIFENSORE');
		}
		else
		{
			//alert('radio Button Tipo Istante: Check su ALTRI');
		}	
        
        if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.value=='0212')
      	{
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_COD_COMPUTO_MC%>.value.length=='1' || 
       		   document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_COD_COMPUTO_MC%>.value=='-'  )
    	    {
            	alert('scegliere Causale Computo Misura Cautelare');
        	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_COD_COMPUTO_MC%>.focus();
        	 	return false; 
            }
	      	 
	      	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_COD_CAUSALE_COMPUTO%>.value = document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_COD_COMPUTO_MC%>.value;
	      	
	      	// Anno e numero R.G.P.M.
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUMERO_REGE_PM%>.value=="" &&
      			document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REGE_PM%>.value!=""  )
	      	{
	      		alert('inserire Numero R.G.P.M.');
        	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUMERO_REGE_PM%>.focus();
        	 	return false; 
	      	}
	      	
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUMERO_REGE_PM%>.value!="" &&
      			document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REGE_PM%>.value==""  )
	      	{
	      		alert('inserire Anno R.G.P.M.');
        	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REGE_PM%>.focus();
        	 	return false; 
	      	}
	      	
	      	// Tipo e luogo UFFICIO PM
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_DESCR_COMUNE_PM_SEDE%>.value != "" &&
      	  	   document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_PM_SEDE%>.value == "-")
	      	{
	      		alert('inserire Tipo Ufficio PM');
	      		document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_PM_SEDE%>.focus();
	      		return false;
	      	}
	      	
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_DESCR_COMUNE_PM_SEDE%>.value == "" &&
      	  	   document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_PM_SEDE%>.value != "-")
	      	{
	      		alert('inserire Luogo Ufficio PM');
	      		document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_DESCR_COMUNE_PM_SEDE%>.focus();
	      		return false;
	      	}
	      	
	      	// Anno e numero B.D.M.C.
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUME_FASC_BDMC%>.value == "" &&
      		   document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_FASC_BDMC%>.value != ""  )
	      	{ 
	      		alert('inserire Numero B.D.M.C.');
        	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUME_FASC_BDMC%>.focus();
        	 	return false; 
	      	}
	      	
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUME_FASC_BDMC %>.value != "" &&
      			document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_FASC_BDMC %>.value == ""  )
	      	{
	      		alert('inserire Anno B.D.M.C.');
        	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_FASC_BDMC %>.focus();
        	 	return false; 
	      	}
	      	
	      	// Anno, Numero e tipo Reg.Gen.
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUMERO_REG_GEN %>.value == "" &&
      			document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REG_GEN %>.value != ""  )
	      	{ 
	      		alert('inserire Numero Reg.Gen.');
        	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUMERO_REG_GEN %>.focus();
        	 	return false; 
	      	}
	      	
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUMERO_REG_GEN %>.value != "" &&
      			document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REG_GEN %>.value == ""  )
	      	{
	      		alert('inserire Anno Reg.Gen');
        	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REG_GEN %>.focus();
        	 	return false; 
	      	}
	      	
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUMERO_REG_GEN %>.value != "" &&
      			document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REG_GEN %>.value != ""  )
	      	{ 
	      		if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_REG_GEN %>.value == "-")
	      		{
	      			alert('inserire Tipo ufficio Reg.Gen');
	           	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_REG_GEN %>.focus();
	           	 	return false; 
	      		}	
	      	}	
	      	
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_NUMERO_REG_GEN %>.value == "" &&
      			document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REG_GEN %>.value == ""  )
	      	{ 
	      		if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_REG_GEN %>.value != "-")
	      		{
	      			alert('inserire Anno e Numero Reg.Gen');
	           	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REG_GEN %>.focus();
	           	 	return false; 
	      		}	
	      	}
	      	
	      	// Sede e tipo Autorità emittente 
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_DESCR_AUTORITA_EMITTENTE_LUOGO %>.value == "" &&
	      		document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_AUTORITA_EMITTENTE %>.value != "-" )
	      	{
	      		alert('inserire Luogo Autorità Emittente');
	      		document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_DESCR_AUTORITA_EMITTENTE_LUOGO %>.focus();
	      		return false;
	      	}
	      	
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_DESCR_AUTORITA_EMITTENTE_LUOGO %>.value != "" &&
	      		document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_AUTORITA_EMITTENTE %>.value == "-" )
	      	{
	      		alert('inserire Tipo Autorità Emittente');
	      		document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_AUTORITA_EMITTENTE %>.focus();
	      		return false;
	      	}

	      	 // Data ordinanza (Non Obbligatoria)
	      	if (document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA%>.value.length==1)
	        	document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA%>.value='0'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA %>.value;
	        if (document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA %>.value.length==1)
	        	document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA %>.value='0'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA %>.value;

	        var data_to_verify=document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA %>.value+'/'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA %>.value+'/'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA %>.value;
	        
	        if (data_to_verify!="//")
	      	{ 
		        if (!ControllaData(data_to_verify) )
		        {
		            alert('Data Ordinanza non valida');
		            document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA %>.focus();
		            return false;
		        }
		        
		        if (!CompareDate(data_to_verify, strSysDate))
		        {
		          	alert('Data Ordinanza superiore alla data attuale');
		          	document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA %>.focus();
		          	return false;
		        }
	      	}    
	      	
	        // Espiazione periodo presofferto in istituto 
	        if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE%>[0].checked)
	      	{
	      	  	// Tipo Misura = DETENTIVA 
	      	  	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_MISURA%>.value = document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_COD_TIPO_MISURA_DET%>.value;
	      	  	
	      	  	/*
	      		if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_DESCR_ISTITUTO_DETENZIONE%>.value=="" ||
	   				document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_DESCR_ISTITUTO_DETENZIONE%>.value.length==0	)
	      		{
	      			alert('Inserire Istituto di Detenzione');
	              	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_DESCR_ISTITUTO_DETENZIONE %>.focus();
	              	return false;
	      		}
	      		*/
	      	} 
	        else	
	      	// Espiazione periodo presofferto in ALTRO LUOGO		
	      	{
	      		// Tipo Misura = NON DETENTIVA
	      	  	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_MISURA%>.value = document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_COD_TIPO_MISURA_NONDET%>.value;
	      	  
	      	  /*
		      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_ALTRO_LUOGO_DETENZIONE %>.value=="" ||
	    	 		document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_ALTRO_LUOGO_DETENZIONE%>.value.length==0	)
	        	{
	        		alert('Inserire Altro luogo di Detenzione');
	               	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_ALTRO_LUOGO_DETENZIONE %>.focus();
	               	return false;
	        	}
	        	*/
	      	}
        		
      	}
        else if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.value=='0213')
     	{
      	  	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_COD_COMPUTO_DET%>.value.length=='1' || 
         	   document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_COD_COMPUTO_DET%>.value=='-'  )
      	    {
              	alert('scegliere Causale Computo Pena Detentiva');
          	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_COD_COMPUTO_DET%>.focus();
          	 	return false; 
            }
      	  	
      	  	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_COD_CAUSALE_COMPUTO%>.value = document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_COD_COMPUTO_DET%>.value;
      	  	
      		// Anno, Numero Sentenza
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA %>.value == "" &&
      			document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA %>.value != ""  )
	      	{ 
	      		alert('inserire Numero Sentenza');
        	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA %>.focus();
        	 	return false; 
	      	}
	      	
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA %>.value != "" &&
      			document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA %>.value == ""  )
	      	{
	      		alert('inserire Anno sentenza');
        	 	document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA %>.focus();
        	 	return false; 
	      	}
	      	
	      	 // Data sentenza (NON Obbligatoria)
	      	if (document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>.value.length==1)
	        	document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>.value='0'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>.value;
	        if (document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO %>.value.length==1)
	        	document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO %>.value='0'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO %>.value;

	        var data_to_verify=document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>.value+'/'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO %>.value+'/'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO %>.value;
	              
	        if (data_to_verify!="//")
	      	{  
				if (!ControllaData(data_to_verify) )
	        	{
	            	alert('Data Sentenza non valida');
	            	document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>.focus();
	            	return false;
	        	}
				
				if (!CompareDate(data_to_verify, strSysDate))
		        {
		          	alert('Data Sentenza superiore alla data attuale');
		          	document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>.focus();
		          	return false;
		        }
	        }
	        
	     	// Anno, Numero SIEP
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_NUMERO_PROC_SIEP %>.value == "" &&
     			document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_ANNO_PROC_SIEP %>.value != ""  )
	      	{ 
	      		alert('inserire Numero SIEP');
       	 		document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_NUMERO_PROC_SIEP %>.focus();
       	 		return false; 
	      	}
	      	
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_NUMERO_PROC_SIEP %>.value != "" &&
     			document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_ANNO_PROC_SIEP %>.value == ""  )
	      	{
	      		alert('inserire Anno SIEP');
       	 		document.LoadInserisciFungibilitaCumulo.<%=ICostantiPresoffertoCumulo.CAMPO_ANNO_PROC_SIEP %>.focus();
       	 		return false; 
	      	}
	      	
	      	// Sede e tipo Autorità emittente Sentenza 
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>.value == "" &&
	      		document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value != "-" )
	      	{
	      		alert('inserire Luogo Autorità Emittente sentenza');
	      		document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>.focus();
	      		return false;
	      	}
	      	
	      	if(document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>.value != "" &&
	      		document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value == "-" )
	      	{
	      		alert('inserire Tipo Autorità Emittente sentenza');
	      		document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
	      		return false;
	      	}
	      	
     	} 
      	
        //------------------------------------------------------------------------------------------------------------------
        // 								Controllo periodi presofferto (OBBLIGATORI) :	
        //------------------------------------------------------------------------------------------------------------------
         
        //	DATA INIZIO PERIODO
     	if (document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA %>.value.length==1)
       		document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA %>.value='0'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA %>.value;
       if (document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA %>.value.length==1)
       		document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA %>.value='0'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA %>.value;

       var data_to_verify_ini=document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA %>.value+'/'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA %>.value+'/'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA %>.value;
           
       if (data_to_verify_ini=="//")
       {
       		alert('Data Inizio periodo presofferto Obbligatoria');
           	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA%>.focus();
           	return false;
       }
       else if (!ControllaData(data_to_verify_ini))
       {
           alert('Data Inizio periodo presofferto non valida');
           document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA%>.focus();
           return false;
       }
       
       // DATA FINE PERIODO
      	if (document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A %>.value.length==1)
        		document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A %>.value='0'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A %>.value;
        if (document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A %>.value.length==1)
        		document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A %>.value='0'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A %>.value;

        var data_to_verify_fine=document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A %>.value+'/'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A %>.value+'/'+document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A %>.value;
            
        if (data_to_verify_fine=="//")
        {
        		alert('Data Fine periodo presofferto Obbligatoria');
            	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A%>.focus();
            	return false;
        }
        else if (!ControllaData(data_to_verify_fine))
        {
            alert('Data fine periodo presofferto non valida');
            document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A%>.focus();
            return false;
        }
       
		//	data_to_verify_fine = DATA FINE PERIODO
		//	data_to_verify_ini  = DATA INIZIO PERIODO
		if (!CompareDate(data_to_verify_ini, data_to_verify_fine))
        {
        	alert('Data Inizio periodo presofferto superiore a Data Fine periodo presofferto');
        	document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA %>.focus();
        	return false;
      	}
		
      	return true; 
      	
    }  // Chiude Function verify()
    
    // Lista popup dei Comuni
    function ListaComuniperTipoUfficio(a_formname,a_fieldname,codTipoUfficio)
    {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    //==========================================================================
    // 
    //==========================================================================  
    function tornaIndietro(action)
    {
      document.indietroForm.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.indietroForm.submit();
    }    
    
    
    //==========================================================================
    // 
    //==========================================================================
    function clearQuantum()
    {
      document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.value = "";
      document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.value = "";
      document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.value = "";
      document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP%>.value = "";
      
      // stessa cosa in Jquery
      $('#anniPresofferto').val('');
      $('#mesiPresofferto').val('');
      $('#giorniPresofferto').val('');
      $('#totaleGiorniPre').val('');

      anniQuantumCalcolati = 0;
      mesiQuantumCalcolati = 0;
      giorniQuantumCalcolati = 0;
      totGGMessaAllaProva = 0;
    }
    
    //==========================================================================
    // 
    //==========================================================================
    function testCalcolaPresofferto(){
      if (controllaPeriodi('noreply')==false){
        clearQuantum();
      }
      else {
        callCalcolaQuantum();
      }
    }
    
    //==========================================================================    
    // Verifica la congruenza dei periodi (data dal <= data al)
    //==========================================================================    
    function controllaPeriodi(reply)
    {
      var gg_dal = document.LoadInserisciFungibilitaCumulo.GG_DAL;
      var mm_dal = document.LoadInserisciFungibilitaCumulo.MM_DAL;
      var aa_dal = document.LoadInserisciFungibilitaCumulo.AA_DAL;

      var gg_al = document.LoadInserisciFungibilitaCumulo.GG_AL;
      var mm_al = document.LoadInserisciFungibilitaCumulo.MM_AL;
      var aa_al = document.LoadInserisciFungibilitaCumulo.AA_AL;
      
      
      if (gg_dal.value.length<2 && gg_dal.value.length!=0)
        gg_dal.value="0"+gg_dal.value;
      if (mm_dal.value.length<2 && mm_dal.value.length!=0)
        mm_dal.value="0"+mm_dal.value;


      var dataDAL = gg_dal.value +"/"+mm_dal.value+"/"+aa_dal.value;

      if (! ControllaData(dataDAL))
      {
        if (reply=='noreply') {
          return false
        }
        else {
          alert('Data di Inizio Periodo non valida');
          gg_dal.focus();
          return false;
        }
      }


      if (gg_al.value.length<2 && gg_al.value.length!=0)
        gg_al.value="0"+gg_al.value;
      if (mm_al.value.length<2 && mm_al.value.length!=0)
        mm_al.value="0"+mm_al.value;

     
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;
      
      if (! ControllaData(dataAL))
      {
        if (reply=='noreply') {
          return false
        }
        else {
          alert('Data di Fine Periodo non valida');
          gg_al.focus();
          return false;
        }
      }

      if(!CompareDate(dataDAL, dataAL))
      {
        if (reply=='noreply') {
          return false
        }
        else {
          alert('La Data di Fine Periodo non può essere precedente a quella di Inizio');
          gg_dal.focus();
          return false;
        }
      }
    }
    
    //==========================================================================
    // Affettua la chiamata sincrona alal servlet di calcolo quantum
    //==========================================================================
    function callCalcolaQuantum () {
      //alert("callCalcolaQuantum: ");
      
      //if (controllaPeriodi(idMC)==false)
      //  return;
      
    
      var gg_dal = document.LoadInserisciFungibilitaCumulo.GG_DAL;
      var mm_dal = document.LoadInserisciFungibilitaCumulo.MM_DAL;
      var aa_dal = document.LoadInserisciFungibilitaCumulo.AA_DAL;

      var gg_al = document.LoadInserisciFungibilitaCumulo.GG_AL;
      var mm_al = document.LoadInserisciFungibilitaCumulo.MM_AL;
      var aa_al = document.LoadInserisciFungibilitaCumulo.AA_AL;
      
      var dataDAL = gg_dal.value +"/"+mm_dal.value+"/"+aa_dal.value;
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;

      // Aggiungo Tipo Misura Cautelare
      var tipoEspiazione = $("[name=<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE%>]:checked").val();
      var ObjComboTipoMisura;
      if (tipoEspiazione=='<%=ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO%>'){
        ObjComboTipoMisura = $("#divComboDetentive [name=<%= ICostantiPresoffertoCumulo.CAMPO_COD_TIPO_MISURA_DET %>]");
      }
      else {
        ObjComboTipoMisura = $("#divComboNonDetentive [name=<%= ICostantiPresoffertoCumulo.CAMPO_COD_TIPO_MISURA_NONDET %>]");
      }
      
      var tMis = ObjComboTipoMisura.val();
      
      // Prepara l'array dei dati da passare 
      var myParams = new Array(gg_dal.value,
                               mm_dal.value,
                               aa_dal.value,
                               gg_al.value,
                               mm_al.value,
                               aa_al.value,
                               tMis
                              );

      document.body.style.cursor='wait';
      // Chiamata:
      // jsrsExecute(<nome servlet>,<funzione js da invocare al ritorno>, <nome del metodo server da invocare>, <parametro da passare al server o array di parametri>
      jsrsExecute("/CaricaHTML_Servlet", caricaQuantum, "getQuantumIntervallo",myParams);      

    }
    
    //==========================================================================
    //
    //==========================================================================
    function caricaQuantum(valueTextStr){ 
      document.body.style.cursor='auto';
      //alert("return "+valueTextStr);
      
      var sep = "~#";
      var aPairs = valueTextStr.split(sep);
      
      document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.value = aPairs[0];
      document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.value = aPairs[1];
      document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.value = aPairs[2];
      
      document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP%>.value = aPairs[3];
      
    }    
    
    function radioSuRichiesta()
    {
	      var Richiedente = '<%= ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE %>';
	      
	      if ( $("form [type=radio][name="+Richiedente+"]:checked").val()=="D") 
	      {
	      		//alert("radioSuRichiesta Difensore ");
	      	
	      		//$('#divDatiDifensore').show();
	            //$('#divDatiDifensore select').prop('disabled',false);
	      		
	      		$('#divDatiGEsecuzione').hide();
	         	$('#divDatiGEsecuzione select').prop('disabled',true);
	      }
	      else if ( $("form [type=radio][name="+Richiedente+"]:checked").val()=="G")
	      {	
	      		//alert("radioSuRichiesta Giudice esecuzione ");
	      	
	   			//$('#divDatiDifensore').hide();
	         	//$('#divDatiDifensore select').prop('disabled',true);
	   		
	   			$('#divDatiGEsecuzione').show();
	      		$('#divDatiGEsecuzione select').prop('disabled',false);
	      }
	      else 
	      {
	      		//alert("radioSuRichiesta Altri... ");
	      	
				//$('#divDatiDifensore').hide();
	      		//$('#divDatiDifensore select').prop('disabled',true);
			
				$('#divDatiGEsecuzione').hide();
	   			$('#divDatiGEsecuzione select').prop('disabled',true);
	      }
      
    }
    
    function AbilitaFungibilita(){

      var lTipoComputo = document.LoadInserisciFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.value;
      //alert("AbilitaFungibilita lTipoComputo = "+lTipoComputo);

      if (lTipoComputo=="0212") {
        	// computo Misura Cautelare Altro Reato art. 657 c.p.p
        document.getElementById('Div_0212').style.display='block';
        document.getElementById('Div_0213').style.display='none';
        
        $("#tabPeriodi").show(); 
        document.getElementById('divPeriodi').style.display='block';       

        document.getElementById('divRadioTipoEspiazione').style.display	='block';

        radioTipoEspiazione();	
      }
      else if (lTipoComputo=="0213"){
      		// computo Pena Detentiva Espiata per Altro Reato art. 657 c.p.p
        document.getElementById('Div_0212').style.display='none';
        document.getElementById('Div_0213').style.display='block';
        
        $("#tabPeriodi").show();        
        document.getElementById('divPeriodi').style.display='block';
        
        $("#tdGiorni").hide();        
        document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.disabled=true;
        document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.disabled=true;
        document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.disabled=true;
        

        document.getElementById('divRadioTipoEspiazione').style.display	='none';
        document.getElementById('divComboDetentive').style.display		='none';
        document.getElementById('divComboNonDetentive').style.display	='none';
        document.getElementById('divDatiDetentive').style.display		='none';
        document.getElementById('divDatiNonDetentive').style.display	='none';
      }
      else {
        document.getElementById('Div_0212').style.display='none';
        document.getElementById('Div_0213').style.display='none';
        document.getElementById('divPeriodi').style.display='none';
        $("#tabPeriodi").hide(); 
        
        document.getElementById('divRadioTipoEspiazione').style.display	='none';
        document.getElementById('divComboDetentive').style.display		='none';
        document.getElementById('divComboNonDetentive').style.display	='none';
        document.getElementById('divDatiDetentive').style.display		='none';
        document.getElementById('divDatiNonDetentive').style.display	='none';
      }

    }

//-------------------------------------------------      
// 					Quantum
//-------------------------------------------------
    function radioTipoEspiazione(par)
    {
      var nomeRadio = '<%= ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE %>';
      //alert("radioTipoEspiazione "+nomeRadio);
      
      if ( $("form [type=radio][name="+nomeRadio+"]:checked").val()=="<%= ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO %>") 
      {
      	//alert("if divcombodetentive show");
        $('#divComboDetentive').show();
        $('#divComboDetentive select').prop('disabled',false);

        $('#divDatiDetentive').show();        
        $('#divDatiDetentive input').prop('disabled',false);
        $('#divDatiDetentive select').prop('disabled',false);
        $('#divDatiDetentive textarea').prop('disabled',false);
        
        $('#divComboNonDetentive').hide();
        $('#divComboNonDetentive select').prop('disabled',true);
        $('#divDatiNonDetentive').hide();
        $('#divDatiNonDetentive input').prop('disabled',true);
        $('#divDatiNonDetentive select').prop('disabled',true);
        $('#divDatiNonDetentive textarea').prop('disabled',true);
        
      }
      else {
      	
      	//alert("Else divcombodetentive hide");
      	
        $('#divComboDetentive').hide();
        $('#divComboDetentive select').prop('disabled',true);
        
        $('#divDatiDetentive').hide();
        $('#divDatiDetentive input').prop('disabled',true);
        $('#divDatiDetentive select').prop('disabled',true);
        $('#divDatiDetentive textarea').prop('disabled',true);
        
        $('#divComboNonDetentive').show();
        $('#divComboNonDetentive select').prop('disabled',false);

        $('#divDatiNonDetentive').show();
        $('#divDatiNonDetentive input').prop('disabled',false);
        $('#divDatiNonDetentive select').prop('disabled',false);
        $('#divDatiNonDetentive textarea').prop('disabled',false);
      }
      
      bloccaQuantum(par);
      
    }
      
    function bloccaQuantum(par)
    {
   	   //alert('bloccaQuantum');
      var tipoEspiazione = $("[name=<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE%>]:checked").val();
      var ObjComboTipoMisura;
      if (tipoEspiazione=='<%=ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO%>'){
        ObjComboTipoMisura = $("#divComboDetentive [name=<%= ICostantiPresoffertoCumulo.CAMPO_COD_TIPO_MISURA_DET %>]");
      }
      else {
        ObjComboTipoMisura = $("#divComboNonDetentive [name=<%= ICostantiPresoffertoCumulo.CAMPO_COD_TIPO_MISURA_NONDET %>]");
      }
  
      //var nodeQuantum = document.getElementById('divquantum');
  
      if(ObjComboTipoMisura.val()=='CL') // Messa alla prova
      {
        document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.disabled=false;
        document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.disabled=false;
        document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.disabled=false;
        
        $("#tdGiorni").show();
        document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP%>.disabled=false;
      } 
      else
      {
        document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.disabled=true;
        document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.disabled=true;
        document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.disabled=true;
        
        $("#tdGiorni").hide();
        document.LoadInserisciFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP%>.disabled=true;
      }
      
      if (par!='onLoad') // non effettuo i calcoli quantum se sono chiamato dall'onLoad
        testCalcolaPresofferto();
    }    
    
    function testCalcolaPresofferto(par){
      var reply = 'noreply';
      if (par=='X')
        reply = 'reply';
        
      if (controllaPeriodi(reply)==false){
        clearQuantum();
      }
      else {
        callCalcolaQuantum();
      }
    }
      
    // On load
    $(document).ready(function(){
   	 AbilitaFungibilita();	
      // radioTipoEspiazione();

     });
   
    
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.siep.modulocumulo.action.ActInserisciFungibilitaCumulo"; 
        %>
        <font class="campo">Inserimento Fungibilità &nbsp;</font>
        <%
        }
         else if( modalita.equals("M") ) {
          lAzione = "siap.siep.modulocumulo.action.ActInserisciFungibilitaCumulo";
        %>
        <font class="campo">Modifica Fungibilità &nbsp;</font>
        <%}%>
        
      </td>
      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActRicercaFungibilitaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="indietroForm">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
</form>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciFungibilitaCumulo">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="modalita"   value="<%=modalita%>">
  <input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_COD_CAUSALE_COMPUTO%>" value="" >
  <input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_MISURA%>" value="" >
<%
  if( modalita.equals("M") )
  {
%>  
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>" value="<%=StatoEsecTitoloCum.getIdStatoEsecTitoloCumulato()%>" >
  <input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>"             value="<%=aComputo.getIdComputiCumulo()%>" >
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"         value="<%=StatoEsecTitoloCum.getFlagStato() %>">

  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE %>_PROV"  value="<%=StatoEsecTitoloCum.getCodUfficioEmittente() %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>_PROV"    value="<%=StatoEsecTitoloCum.getCodLuogoEmittente() %>">
<%  } %>

  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td class="titolo" colspan="100%">Provvedimento di riconoscimento</td>
    </tr>
    <tr>
      <td class="l">Provvedimento: <font class=ob>(*)</font></td>
      <td class="l" colspan="1">
        <select Title="Tipo Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>" class="small" 
        								   onChange="javascript:AbilitaFungibilita();">
<%	if( modalita.equals("M") )
   	{
   		if(StatoEsecTitoloCum.getCodMotivo().equals("0212") )
   		{	%>
			<option value = "-" />-
          	<option value = "0212" selected />computo Misura Cautelare Altro Reato art. 657 c.p.p.
          	<option value = "0213" />computo Pena Detentiva Espiata per Altro Reato (fungibilità) art. 657 c.p.p.
<%		}
  		else if(StatoEsecTitoloCum.getCodMotivo().equals("0213") )
  		{ %>  		
			<option value = "-" />-
          	<option value = "0212" />computo Misura Cautelare Altro Reato art. 657 c.p.p.
          	<option value = "0213" selected />computo Pena Detentiva Espiata per Altro Reato (fungibilità) art. 657 c.p.p.
<% 		}  		
  	}
    else
    { %> 								   
          <option value = "-" />-
          <option value = "0212" />computo Misura Cautelare Altro Reato art. 657 c.p.p.
          <option value = "0213" />computo Pena Detentiva Espiata per Altro Reato (fungibilità) art. 657 c.p.p.
<%	} %>          
        </select>
      </td>
        
      <td class="l">Data Emissione<font class=ob>(*)</font></td>
      <td class="l">
          <input type="text" size="2" maxlength="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissione(),"dd"), "" ) %>" 
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" size="2" maxlength="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissione(),"MM"), "" ) %>" 
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" size="4" maxlength="4" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissione(),"yyyy"), "") %>" 
                 name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>
  
  <table cellspacing="2" cellpadding="2" width="95%" align="center">  
  	<tr>
      <td class="L">
  <%if ( modalita.equals("I") ) 
  	{ %>   
   	<input type="radio" value="U" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE%>" checked onClick="radioSuRichiesta();">&nbsp;d'Ufficio &nbsp;
   	<input type="radio" value="D" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE%>" onClick="radioSuRichiesta();">&nbsp;su Richiesta Difensore &nbsp;
   	<input type="radio" value="I" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE%>" onClick="radioSuRichiesta();">&nbsp;su Richiesta Interessato&nbsp;
   	<input type="radio" value="G" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE%>" onClick="radioSuRichiesta();">&nbsp;Provvedimento Giudice Esecuzione &nbsp;
<%	}
    else
    {	
      String checkU = "";
      String checkD = "";
      String checkI = "";
      String checkG = "";
      
      if("U".equals(StatoEsecTitoloCum.getCodTipoIstante())){
        checkU = "checked";
      }
      else if( "D".equals(StatoEsecTitoloCum.getCodTipoIstante())) { 
        checkD = "checked";
      }
      else if("I".equals(StatoEsecTitoloCum.getCodTipoIstante())) { 
        checkI = "checked";
      }
      else if("G".equals(StatoEsecTitoloCum.getCodTipoIstante())) { 
        checkG = "checked";
      } %>

    <input type="radio" value="U" <%=checkU%> name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE%>" onClick="radioSuRichiesta();">&nbsp;d'Ufficio &nbsp;
    <input type="radio" value="D" <%=checkD%> name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE%>" onClick="radioSuRichiesta();">&nbsp;su Richiesta Difensore &nbsp;
    <input type="radio" value="I" <%=checkI%> name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE%>" onClick="radioSuRichiesta();">&nbsp;su Richiesta Interessato&nbsp;
    <input type="radio" value="G" <%=checkG%> name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE%>" onClick="radioSuRichiesta();">&nbsp;Provvedimento Giudice Esecuzione &nbsp;
    
<%	} %>         	
      </td>
  	</tr>
  </table>


<div id="divDatiGEsecuzione" style="<%=lBlocca%>">
  <table width="95%" align="center">
	<tr>
      <td class="l" colspan=6>Con Ordinanza Emessa da : </td>
    </tr>
    <tr>
      <td class="l">Autorità</td>	  
      <td class="L">
      	<select Title="Autorità Emittente ordinanza" class="small" name="<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>">
        <%=UfficioEsecEmittente%>
      	</select>
      </td>
      <td class="l">Luogo</td>
      <td class="L">
      	<input  Title="Comune Emittente ordinanza" name="<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_PROVV_RIF%>" 
      			type="text" maxlength="35" size="35" class="small" value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getDescrLuogoAltro(),"")%>" > 
        <a href="Javascript:ListaComuniperTipoUfficio('LoadInserisciFungibilitaCumulo','<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_PROVV_RIF%>',document.LoadInserisciFungibilitaCumulo.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>[document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0> 
        </a>
      </td>
      <td class="L">Sezione</td>
      <td class="L">
      	<input Title="Sezione " value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getSezioneAltro(),"")%>" type="text" name="<%= ICostantiTitoloCumulato.CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF %>" maxlength="35" size="35">
      </td>
    </tr>
        
    <tr>
    <td class="l"> in data : </td>  
    <td class="l">&nbsp;&nbsp;
      <input type="text" title="Giorno Sentenza" name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA %>" maxlength="2" size="2"
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissioneAltro(),"dd"), "" ) %>" 
      	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" title="Mese Sentenza" name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RICHIESTA %>" maxlength="2" size="2"
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissioneAltro(),"MM"), "" ) %>"  
      	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" title="Anno Sentenza" name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RICHIESTA %>" maxlength="4" size="4"
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissioneAltro(),"yyyy"), "" ) %>"  
      	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
    <td class="l">Procedimento SIGE : </td>
      <td class="l">Anno/Numero
        <input type="text" Title="Anno SIGE" name="<%=ICostantiPresoffertoCumulo.CAMPO_ANNO_PROVV %>" size=4 maxlength=4 
        	value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getAnnoProcedimento(), "" ) %>" 
        	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" Title="Numero SIGE" name="<%=ICostantiPresoffertoCumulo.CAMPO_NUMERO_PROVV %>" size=6 maxlength=6
        	value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getProgrProcedimento(), "" ) %>" >
      </td>
    </tr>    
  </table>
</div>  
  
  <div id="Div_0212" style="display:none">
    <table cellspacing="2" cellpadding="2" width="95%" align="center">
      
    <tr id="TR_MC_1" style="display:block">
      <td class="titolo" colspan="100%">Dati identificativi della misura cautelare da computare</td>
    </tr>
    
    <!-- 			CAUSALE COMPUTO PER PROVVEDIMENTO DI TIPO 0212 (computo Misura Cautelare Altro Reato art. 657 c.p.p) -->
    <tr id="TR_MC_2" style="display:block">
      <td class="l">Causale computo : </td>
      <td class="l" colspan=2>
        <select Title="Causale Computo" name="<%=ICostantiComputiCumulo.CAMPO_COD_COMPUTO_MC %>" class="small">
          <%=ComputiMCAR%>
        </select>
      </td>
    </tr>
    
    <tr id="TR_MC_3" style="display:block">
      <td class="l" nowrap>Procedimento R.G.N.R. : </td>
      <td class="l">Anno/Numero
        <input type="text" Title="Anno R.G.N.R." name="<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REGE_PM %>" 
        	size=4 maxlength=4 value="<%=StringUtils.toStringJSP(aComputo.getAnnoRegePM(), "" ) %>"
       	 	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" Title="Numero R.G.N.R." name="<%=ICostantiMisuraCautelareCumulo.CAMPO_NUMERO_REGE_PM %>" 
        	size=6 maxlength=6 value="<%=StringUtils.toStringJSP(aComputo.getNumeroRegePM(), "" ) %>" >
      </td>
    </tr>
    <tr>   
      <td class="l">Tipo Ufficio PM </td>
      <td class="l">
        <select Title="Autorità" name="<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_PM_SEDE %>" class="small">
        <%=UfficioPM %>
         </select>    
      </td>
      <td class="l">Luogo</td>
      <td class="l" >
      	<input type="text" Title="Luogo" name="<%=ICostantiMisuraCautelareCumulo.CAMPO_DESCR_COMUNE_PM_SEDE %>" 
      			maxlength="35" size="35" value="<%=StringUtils.toStringJSP(aComputo.getDescrSedeUfficioPM(), "" ) %>" >
      	<a href="Javascript:ListaComuniperTipoUfficio('LoadInserisciFungibilitaCumulo','<%=ICostantiMisuraCautelareCumulo.CAMPO_DESCR_COMUNE_PM_SEDE %>',
      	  document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_PM_SEDE %>
      	  [document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_PM_SEDE %>.selectedIndex].value);">
      	  <img src="/images/filefolder.gif" border=0>
      	</a>
      </td>
    </tr>

    <tr id="TR_MC_4" style="display:block">
     <tr><td>&nbsp;</td></tr> 
     <td class="l" nowrap>Procedimento B.D.M.C. : </td>
      <td class="l">Anno/Numero
        <input type="text" title="Anno B.D.M.C." name="<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_FASC_BDMC %>" 
        	size=4 maxlength=4 value="<%=StringUtils.toStringJSP(aComputo.getAnnoBDMC(), "" ) %>" 
        	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input type=text title="Numero B.D.M.C." name="<%=ICostantiMisuraCautelareCumulo.CAMPO_NUME_FASC_BDMC %>" 
        	size=6 maxlength=6 value="<%=StringUtils.toStringJSP(aComputo.getNumeroBDMC(), "" ) %>">
      </td>
      
      <td class="l">Anno/Numero Reg.Gen. </td>
      <td class="L">
      	<input Title="Anno Reg.Gen." type="text" name="<%=ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REG_GEN %>" 
      		maxlength="4" size="4" value="<%=StringUtils.toStringJSP(aComputo.getAnnoRege(), "" ) %>" 
      		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
      	<input Title="Numero Reg.Gen." type="text" name="<%=ICostantiMisuraCautelareCumulo.CAMPO_NUMERO_REG_GEN %>" 
      		maxlength="6" size="6" value="<%=StringUtils.toStringJSP(aComputo.getNumeroRege(), "" ) %>" > &nbsp; 
      	  <select Title="Tipo Reg.Gen." name="<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_REG_GEN %>">
  	<%	if ( modalita.equals("I") ) 
  		{ %>  	  
        	<option value="-">-</option> 
        	<option value="gip">GIP</option>
        	<option value="dib">DIB</option>
        	<option value="cas">CAS</option>
        	<option value="cap">CAP</option>
        	<option value="casap">CASAP</option>
<%		}
    	else
    	{ 
    		String Tipo="";
    		if(aComputo.getTipoRege()!=null)
				Tipo=aComputo.getTipoRege(); 
				%>      	  
			<option value="-">-</option>
		<%	
			String sel = "";
			if (Tipo.equals("gip"))
				sel = " selected";
			%> 
				<option value="gip" <%=sel%>>GIP</option>
			<%	sel = "";
				if (Tipo.equals("dib"))
					sel = " selected";
			%> 				
				<option value="dib" <%=sel%>>DIB</option>
			<%	sel = "";
				if (Tipo.equals("cas"))
					sel = " selected";
			%> 				
				<option value="cas" <%=sel%>>CAS</option>
			<%	sel = "";
				if (Tipo.equals("cap"))
					sel = " selected";
			%> 				
				<option value="cap" <%=sel%>>CAP</option>
			<%	sel = "";
				if (Tipo.equals("casap"))
					sel = " selected";
			%> 				
				<option value="casap" <%=sel%>>CASAP</option>
<%		} %>	
	      </select>
      	  
      </td>
    </tr>
	<tr>
      <td class="l">Autorità Emittente</td>
      <td class="L">
      	<select class="small" Title="Autorità Emittente misura" name="<%= ICostantiMisuraCautelareCumulo.CAMPO_AUTORITA_EMITTENTE %>">
        <%=autoritaEmi%>
      	</select>
      </td>
    </tr>
    <tr>
      <td class="l">Luogo Emittente </td>
      <td class="L">
      	<input Title="Luogo Emittente misura" name="<%=ICostantiMisuraCautelareCumulo.CAMPO_DESCR_AUTORITA_EMITTENTE_LUOGO%>" 
      		type="text" maxlength="35" size="35" value="<%=StringUtils.toStringJSP(aComputo.getDescrLuogoAutoritaRege(), "" ) %>" > 
        <a href="Javascript:ListaComuniperTipoUfficio('LoadInserisciFungibilitaCumulo','<%=ICostantiMisuraCautelareCumulo.CAMPO_DESCR_AUTORITA_EMITTENTE_LUOGO%>',
        	document.LoadInserisciFungibilitaCumulo.<%= ICostantiMisuraCautelareCumulo.CAMPO_AUTORITA_EMITTENTE %>
        	[document.LoadInserisciFungibilitaCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0> 
        </a>
      </td>
      <td class="l">Data Emissione ordinanza </td>
      <td class="l"> 
      	<input type="text" size="2" maxlength="2" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aComputo.getDataEmissioneOrdRege(),"dd")) %>" 
             name="<%= ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
        >&nbsp;/&nbsp;
      	<input type="text" size="2" maxlength="2" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aComputo.getDataEmissioneOrdRege(),"MM")) %>" 
             name="<%= ICostantiMisuraCautelareCumulo.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
       	>&nbsp;/&nbsp;
      	<input type="text" size="4" maxlength="4" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aComputo.getDataEmissioneOrdRege(),"yyyy")) %>" 
             name="<%= ICostantiMisuraCautelareCumulo.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA %>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>
</div>

<%
//==============================================================================
//
//==============================================================================
%>
<div id="Div_0213" style="display:none">
  <table cellspacing="2" cellpadding="2" width="95%" align="center">

  <tr id="TR_PD_1" style="display:block">
    <td class="Titolonocap" colspan="100%">Dati identificativi del procedimento SIEP cui si riferisce la pena espiata in eccesso</td>
  </tr>
  
  <!-- 			CAUSALE COMPUTO PER PROVVEDIMENTO DI TIPO 0213 (computo Pena Detentiva Espiata per Altro Reato art. 657 c.p.p) -->
  <tr id="TR_PD_3" style="display:block">
    <td class="l">Causale computo : </td>
    <td class="l" colspan=2>
      <select Title="Causale Computo" class="small" name="<%=ICostantiComputiCumulo.CAMPO_COD_COMPUTO_DET %>">
        <%=ComputiPDAR%>
      </select>
    </td>
  </tr>
  
  <tr id="TR_PD_2" style="display:block">
    <td class="l">Sentenza : </td>
    <td class="l">Anno/Numero </td>
    <td class="L">
      &nbsp;
      <input type="text" title="Anno Sentenza" name="<%=ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA %>" 
      	size=4 maxlength=4 value="<%=StringUtils.toStringJSP(aComputo.getAnnoSentenza(), "" ) %>" 
       onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      /
      <input type="text" title="Numero Sentenza" name="<%=ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA %>" 
      	size=4 maxlength=4 value="<%=StringUtils.toStringJSP(aComputo.getNumeroSentenza(), "" ) %>" >
      &nbsp;
    </td>
    
    <td class="l"> Emessa in data : </td>  
    <td class="l">&nbsp;&nbsp;
      <input type="text" title="Giorno Sentenza" name="<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>" 
      	maxlength="2" size="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aComputo.getDataSentenza(),"dd")) %>"
      	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" title="Mese Sentenza" name="<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO %>" 
      	maxlength="2" size="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aComputo.getDataSentenza(),"MM")) %>"
      	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" title="Anno Sentenza" name="<%=ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO %>" 
      	maxlength="4" size="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aComputo.getDataSentenza(),"yyyy")) %>" 
      	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
  
  <tr>
    <td class="l"> da : </td>
	<td class="l">Autorità Emittente</td>	  
    <td class="L">
      	<select Title="Autorità Emittente sentenza" class="small" name="<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
        <%=autoritaEmiSIEP%>
      	</select>
    </td>
    <td class="l">Luogo</td>
    <td class="L">
      	<input Title="Comune Emittente sentenza" name="<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>" 
      		value="<%=StringUtils.toStringJSP(aComputo.getDescrLuogoEmittente(), "" ) %>" type="text" maxlength="35" size="35" class="small"> 
        <a href="Javascript:ListaComuniperTipoUfficio('LoadInserisciFungibilitaCumulo','<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>',document.LoadInserisciFungibilitaCumulo.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.LoadInserisciFungibilitaCumulo.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0> 
        </a>
    </td>
  </tr>
  <tr>  
    <td class="l" nowrap>procedimento SIEP</td>
    <td class="l">Anno/Numero</td>
    <td class="L">
      <input type="text" title="Anno fascicolo SIEP" name="<%=ICostantiPresoffertoCumulo.CAMPO_ANNO_PROC_SIEP %>" 
      	size=4 maxlength=4 value="<%=StringUtils.toStringJSP(aComputo.getChiaveAnnoSIEP(), "" ) %>" 
       	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      /
      <input type=text title="Numero fascicolo SIEP" name="<%=ICostantiPresoffertoCumulo.CAMPO_NUMERO_PROC_SIEP %>" 
      	size=6 maxlength=6 value="<%=StringUtils.toStringJSP(aComputo.getChiaveNumeroSIEP(), "" ) %>" >
    </td>
  </tr>  
  
</table>
</div>
  
  
<table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabPeriodi">
  <tr>
    <td class="titolo" colspan="100%">Periodi Computati</td>
  </tr>
</table>

<div id="divRadioTipoEspiazione" style="display:block;">
<table cellspacing="2" cellpadding="2" width="95%" align="center">  
  <tr>
    <td class="L">
     Espiazione pena in istituto di detenzione &nbsp;
     <input type="radio" value="<%=ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO%>" 
            name="<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE%>"
            <%=(TipoEspiazione.equals("") || TipoEspiazione.equals(ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO) )?"checked":"" %>  
            onClick="radioTipoEspiazione();">
     Espiazione pena in altro luogo &nbsp;
     <input type="radio" value="<%=ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ALTRO%>" 
            name="<%=ICostantiMisuraCautelareCumulo.CAMPO_TIPO_ESPIAZIONE%>"
            <%=(TipoEspiazione.equals(ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ALTRO) )?"checked":"" %> 
            onClick="radioTipoEspiazione();">                
    </td>
  </tr>
</table>
</div>
  
  <div id="divComboDetentive" <%=lDisp%>>
  <table width="95%" align="center">
    <tr>
      <td class="l" width="150px">Natura Misura</td>
      <td class="l">
        <select Title="Tipo Misura Cautelare" name="<%= ICostantiPresoffertoCumulo.CAMPO_COD_TIPO_MISURA_DET %>" class="small" onChange="Javascript:bloccaQuantum();">
        <%=tipoMisuraDetentive%>
        </select>
      </td>
    </tr>
  </table>
</div>

<div id="divComboNonDetentive" <%=lDisp%>>
  <table width="95%" align="center">
    <tr>
      <td class="l" width="150px">Natura Misura</td>
      <td class="l" colspan="2">
        <select Title="Tipo Misura Cautelare" name="<%= ICostantiPresoffertoCumulo.CAMPO_COD_TIPO_MISURA_NONDET %>" class="small" onChange="Javascript:bloccaQuantum();">
        <%=tipoMisuraNonDetentive%>
        </select>
      </td>
    </tr>
  </table>
</div>
  
<div id="divPeriodi" style="display:block;">
    <table width="95%" align="center">
      <tr>
        <td class="l" colspan="1" nowrap>Periodo sofferto&nbsp;<font class="ob">(*)</font></td>
        <td class="l" colspan="1" width="170px" nowrap>
          <font class="label">Dal&nbsp;</font>
                                 
          <input type="text" maxlength="2" size="2" id="GG_DAL"
                 name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"dd"))%>" 
                 <%=IWebConstants.UTIL_DATA%>
                 onChange="Javascript:testCalcolaPresofferto()">
          <input type="text" maxlength="2" size="2" id="MM_DAL"
                 name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"MM"))%>" 
                 <%=IWebConstants.UTIL_DATA%>
                 onChange="Javascript:testCalcolaPresofferto()">
          <input type="text"  maxlength="4" size="4"  id="AA_DAL" 
                 name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"yyyy"))%>" 
                 <%=IWebConstants.UTIL_DATA_ANNO%>
                 onChange="Javascript:testCalcolaPresofferto()">
        </td>
        <td class="l" colspan="1" width="160px" nowrap>
          <font  class="label">Al&nbsp;</font>
                                 
          <input type="text" maxlength="2" size="2" id="GG_AL"
                 name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"dd"))%>" 
                 <%=IWebConstants.UTIL_DATA%>
                 onChange="Javascript:testCalcolaPresofferto()">
          <input type="text" maxlength="2" size="2" id="MM_AL" 
                 name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"MM"))%>" 
                 <%=IWebConstants.UTIL_DATA%>
                 onChange="Javascript:testCalcolaPresofferto()">
          <input type="text" maxlength="4" size="4" id="AA_AL"
                 name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A%>"
                 value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"yyyy"))%>" 
                 <%=IWebConstants.UTIL_DATA_ANNO%>
                 onChange="Javascript:testCalcolaPresofferto()">
        </td>       

        <td class="l" nowrap id="tdQuantum" width="75px" nowrap>&nbsp;
          <font class="label">Pari a&nbsp;</font><a href="Javascript:testCalcolaPresofferto('X');"><img src="<%=IWebConstants.IMAGES_DIR%>freccia_verde.gif" alt="Calcola Quantum" border=0></a>:
        </td>   
        
        <td class="c">
          <table>
            <tr>
              <td id="tdGiorni" nowrap>
                <font class="label">Tot Giorni</font>&nbsp;
                <input type="text" title="totale giorni sofferti" name="<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP%>" maxlength="4" size="4" id="totaleGiorniPre" 
                       value="<%=StringUtils.toStringJSP(aComputo.getNumGiorniMap()) %>"
                       onkeypress="return TicTabNumField(this,event)"
                       onChange="Javascript:testChgQuantum(this)">&nbsp;
                <font class="label">equiparati a:</font>&nbsp;     
              </td>
                              
              <td nowrap>
                <font class="label">Anni</font>&nbsp;
                <input type="text" title="anni sofferti" name="<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>" maxlength="2" size="2" id="anniPresofferto"  
                       value="<%=StringUtils.toStringJSP (aComputo.getNumAnniReclusione()) %>"
                       onkeypress="return TicTabNumField(this,event)"
                       onChange="Javascript:testChgQuantum(this)">&nbsp;&nbsp;
                <font class="label">Mesi</font>&nbsp;
                <input type="text" title="mesi sofferti" name="<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>" maxlength="2" size="2" id="mesiPresofferto" 
                       value="<%=StringUtils.toStringJSP (aComputo.getNumMesiReclusione()) %>"
                       onkeypress="return TicTabNumField(this,event)"
                       onChange="Javascript:testChgQuantum(this)">&nbsp;&nbsp;
                <font class="label">Giorni</font>&nbsp;
                <input type="text" title="giorni sofferti" name="<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>" maxlength="2" size="2" id="giorniPresofferto" 
                       value="<%=StringUtils.toStringJSP (aComputo.getNumGiorniReclusione()) %>"
                       onkeypress="return TicTabNumField(this,event)"
                       onChange="Javascript:testChgQuantum(this)">&nbsp;&nbsp;&nbsp;&nbsp;
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </div>
  
  <div id="divDatiDetentive" <%=lDisp%>>
    <table width="95%" align="center">
      <tr>
        <td class="l" >Istituto</td>
        <td class="l">
          <%
          String lDescrIstituto = "";
          if (IstitutoDetenzione != null && IstitutoDetenzione.getIdIstitutoDetenzione()!=null && IstitutoDetenzione.getDescrizioneIstitutoPerVisualizzazione()!=null )
          {
           	lDescrIstituto = IstitutoDetenzione.getDescrizioneIstitutoPerVisualizzazione();          
          }
          
          %>
          <input type="hidden"  Title="Istituto" name="<%=ICostantiComputiCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" >
          
          <input Title="Istituto" name="<%=ICostantiComputiCumulo.CAMPO_DESCR_ISTITUTO_DETENZIONE%>" size="90" value="<%=StringUtils.toStringJSP(lDescrIstituto)%>" >
			<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciFungibilitaCumulo','<%=ICostantiComputiCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','<%=ICostantiComputiCumulo.CAMPO_DESCR_ISTITUTO_DETENZIONE%>');">
          	  <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0></a>
          	<a href="Javascript:pulisciIstituto('<%=ICostantiComputiCumulo.CAMPO_DESCR_ISTITUTO_DETENZIONE%>','<%=ICostantiComputiCumulo.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>');">
          	  <img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border=0></a>
        </td>
      </tr>
    </table>
  </div>    
  
  <div id="divDatiNonDetentive" <%=lDisp%>>
    <table width="95%" align="center">
      <tr>
        <td class="l" >Luogo di espiazione </td>
        <td class="l" colspan="3">
          <textarea rows="3" cols="80" Title="altro luogo detenzione"
                    name="<%=ICostantiComputiCumulo.CAMPO_ALTRO_LUOGO_DETENZIONE %>"
          ><%=StringUtils.toStringJSP (lAltroLuogo)%></textarea>
        </td>
      </tr>
    </table>
  </div> 

	<table cellspacing="2" cellpadding="2" width="95%" align="center">
  	  <tr><td>&nbsp;</td></tr>
     <tr><td><input class="bottone" type="submit" name="conferma" value="Conferma"></td></tr>
	</table>
	
</form>
</body>
</html>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciFungibilitaCumulo");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>