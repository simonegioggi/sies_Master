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

<% 
	String[] esiti = (String[])request.getAttribute("esiti");
%>

<%
	// Variabili
	Date data_emissione =null;
	Date data_deposito = null;
	TenoreModel[] lTenori = null;
	String lAction = "siap.sius.provvedimento.action.ActModificaRevocaLA";
	String lIdEvento = "";
	String lIdOrdinanza = "";
	String lIdDecreto = "";
	// int    giorniLibAnt = 0;
    
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

// Nuova Ordinanza L.A. - Decreto Legge 2013/46
	int TotggLA	= 0;			/* totali concessi per Accoglie REVOCA L.A. NORMALE  */ 
	int TotggLS	= 0;			/* totali concessi per Accoglie REVOCA L.A. SPECIALE  */
	int TotggLI	= 0;			/* totali concessi per Accoglie REVOCA L.A. INTEGRAZIONE  */
	int TotggLAold = 0;	 	/* totali concessi per Accoglie REVOCA della Vecchia L.A.	*/
//	
	int TotggScompLA	= 0;			/* totali somputati per Accoglie REVOCA L.A. NORMALE  */ 
	int TotggScompLS	= 0;			/* totali somputati per Accoglie REVOCA L.A. SPECIALE  */
	int TotggScompLI	= 0;			/* totali somputati per Accoglie REVOCA L.A. INTEGRAZIONE  */
	int TotggScompLAold = 0;	 	/* totali somputati per Accoglie REVOCA della Vecchia L.A.	*/
// accoglie L.A.	
	boolean perUnicoConcLA_dalal = false;
	boolean perUnicoConcLA_sologg = false;
	int semestriLA = 0;
// accoglie Scomputabili L.A.
	boolean perUnicoScompLA_dalal = false;
	boolean perUnicoScompLA_sologg = false;
	int semestriScompLA = 0;
// accoglie L.A.S.
	boolean perUnicoConcLS_dalal = false;
	boolean perUnicoConcLS_sologg = false;
	int semestriLS = 0;
// accoglie Scomputabili L.A.S.
	boolean perUnicoScompLS_dalal = false;
	boolean perUnicoScompLS_sologg = false;
	int semestriScompLS = 0;
// accoglie L.A.I.	
	boolean perUnicoConcLI_dalal = false;
	boolean perUnicoConcLI_sologg = false;
	int semestriLI = 0;
// accoglie Scomputabili L.A.I.
	boolean perUnicoScompLI_dalal = false;
	boolean perUnicoScompLI_sologg = false;
	int semestriScompLI = 0;
//
	PeriodoLibAnticipataModel[][] plam_arrayLA = new PeriodoLibAnticipataModel[NumCheck][NumDate];
	int [] periodiLA = new int[NumCheck];
	
// 04/2014	NUOVA ORDINANZA L.A. PER DECRETO

	PeriodoLibAnticipataModel[][] plam_arrayLS = new PeriodoLibAnticipataModel[NumCheck][NumDate];
	int [] periodiLS = new int[NumCheck];
	
	PeriodoLibAnticipataModel[][] plam_arrayLI = new PeriodoLibAnticipataModel[NumCheck][NumDate];
	int [] periodiLI = new int[NumCheck];

// END 04/2014
	
	//Estrazione della data minima: data udienza oppure iscrizione fascicolo
	String data1;
	if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
 		data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
	else
		data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria(),"dd/MM/yyyy");
 		//data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");

// Switch fra il caso Decreto e quello  Ordinanza
if( request.getParameter("tipo_provvedimento") != null && request.getParameter("tipo_provvedimento").equalsIgnoreCase("decreto"))
{
	// Modifica Decreto
	data_emissione = depositoDecretoMotivazioni.getDepositoDecreto().getDataEmissione();
	data_deposito = depositoDecretoMotivazioni.getDepositoDecreto().getDataDeposito();
	lTenori = (TenoreModel[])  tenori.toArray(new TenoreModel[0]);
	lIdEvento = depositoDecretoMotivazioni.getEvento().getIdEvento().toString();
	lIdOrdinanza = "";
	lIdDecreto = depositoDecretoMotivazioni.getDepositoDecreto().getIdDepositoDecreto().toString();
}
else
{
	// Modifica Ordinanza
	data_emissione = datiOrdinanza.getEvento().getDataEmissione();
	data_deposito = datiOrdinanza.getOrdinanza().getDataDeposito();
	lTenori = datiOrdinanza.getTenori();
	lIdEvento = datiOrdinanza.getEvento().getIdEvento().toString();
	lIdOrdinanza = datiOrdinanza.getOrdinanza().getIdDepositoOrdinanzaPc().toString();
	// giorniLibAnt = datiOrdinanza.getOrdinanza().getNumGiorniLibanticipata().intValue();
	lIdDecreto = "";
}

%>
	
<html>
 
  <head>
  
    <title>[S.I.E.S.] - Modifica Ordinanza di Revoca Liberazione Anticipata</title>
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
	// var periodi = < %=periodi%>;
	var node;
	var modalita = 'S' ;            /* modalità di scelta. S : Semestr C: periodo Complessivo */
	var checkImcompetenza = 0;
	var flagConcesso = 'C';

	   var GiorniConcessi = 0;
	   var GiorniConcessi_spe = 0;
	   var GiorniConcessi_int = 0;
	   var SalvaGiorniConcessi = 0;
	   var SalvaGiorniConcessi_spe = 0;
	   var SalvaGiorniConcessi_int = 0;

	   var codinizioLA = "";
	   var codinizioLASPE = "";
	   var codinizioLAINT = "";
	   
	function init()
	{
		//alert("inizio");
		var i=0;
		var lungh = <%=lTenori.length%>;

		 for (i=0; i<NumTotale; i++)
	          giorni[i] = 0;
		 
	      for (i=0; i<NumTotale; i++)
	          giorni_spe[i] = 0;
	      
	      for (i=0; i<NumTotale; i++)
	          giorni_int[i] = 0;

 		if(lungh > 1)
		{				// Presenza di più oggetti
			if(document.getElementById("giornidiLA"))
			{
				var save_gg = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA %>.value;
				document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.value = save_gg;
			}
		
			if(document.getElementById("giorni_SPE"))
			{
				var save_gg_spe = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE %>.value;
				document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value = save_gg_spe;
			}
			
			if(document.getElementById("giorni_INT"))
			{
				var save_gg_int = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT %>.value;
				document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value = save_gg_int;
			}
			
			document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[0].checked=true;
			document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO%>.value = "0";
			
			if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[0].value == "0028" ||
				document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[0].value == "2135" )
			{
				codinizioLA = document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[0].value;
				abilitainizioLA();
				DisabilitainizioLA_SPE();
				DisabilitainizioLA_INT();
			}	
			else if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[0].value == "0620" ||
					document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[0].value == "2136")
			{	
				codinizioLASPE = document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[0].value;
				DisabilitainizioLA();
				abilitainizioLA_SPE();
				DisabilitainizioLA_INT();
			}	
			else if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[0].value == "0621" ||
					document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[0].value == "2137")
			{
				codinizioLAINT = document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[0].value;
				DisabilitainizioLA();
				DisabilitainizioLA_SPE();
				abilitainizioLA_INT();
			}	
		}	
		else
		{				// Presenza di un solo oggetto
			document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>.checked=true;
			if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0028" ||
				document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2135")
			{
				codinizioLA = document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value;
				var save_gg = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA %>.value;
				document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.value = save_gg;
				abilitainizioLA();
			}	
			else if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0620" ||
					document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2136")
			{
				codinizioLASPE = document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value;
				var save_gg_spe = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE %>.value;
				document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value = save_gg_spe;
				abilitainizioLA_SPE();
			}	
			else if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0621" ||
					document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2137")
			{
				codinizioLAINT = document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value;
				var save_gg_int = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT %>.value;
				document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value = save_gg_int;
				abilitainizioLA_INT();
			}	
		}
 		
	      document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO%>.value = "C";
	      document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO%>.value = "C";
	      document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO%>.value = "C";
	}
//	
//	---------- > 	Caso in cui il primo oggetto presente è LIBERAZIONE ANTICIPATA (L.A.) :  
//					vengono presentati per primi nella form i periodi relativi a questo oggetto, 
	function abilitainizioLA()
	{
		node=document.getElementById("tipoconcessioneLA");
        node.style.display='block';
        
        AbilitaPeriodo();
        AbilitaSemestri();
        
       // document.ModOrdinanzaRevocaLA.< %=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = "0028"; / "2135"
         document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = codinizioLA;
       
        if(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked)
        	AbilitaPeriodo();

        var valoreLA = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>.value;
        if(valoreLA=="")
        	valoreLA="0";

        document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value = valoreLA;
       
        if(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked)
        	 document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = false;
        else
        	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = true;
	}
	
	function DisabilitainizioLA()
	{
	//	alert("DisabilitainizioLA - inizio");
		if(document.getElementById("giornidiLA"))
		{
	        node=document.getElementById("tipoconcessioneLA");
	        node.style.display='none';
	        node=document.getElementById("comune");
	        node.style.display='none';
	        node=document.getElementById("semestri");
	        node.style.display='none';
	        node=document.getElementById("periodo");
	        node.style.display='none';
	        node=document.getElementById("resto");
	        node.style.display='none';
		}
    //    alert("DisabilitainizioLA - fine");
	}
//	
//	---------- > 	Caso in cui il primo oggetto presente è LIBERAZIONE ANTICIPATA SPECIALE(L.A.S.) :  
//					vengono presentati per primi nella form i periodi relativi a questo oggetto, 

	function abilitainizioLA_SPE()
	{
	//	alert("C  - AbilitainizioLA_SPE");
		
		node=document.getElementById("tipoconcessione_SPE");
        node.style.display='block';
        
        AbilitaPeriodo_SPE();
        AbilitaSemestri_SPE();
        
       // document.ModOrdinanzaRevocaLA.< %=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = "0620";/ "2136"
        document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = codinizioLASPE;
        
        if(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE %>[1].checked)
        	AbilitaPeriodo_SPE();
		
        var valoreLS = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE %>.value;
        if(valoreLS=="")
        	valoreLS="0";
        
        document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value = valoreLS;
       
        if(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE %>[1].checked)
        	 document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.readOnly = false;
        else
        	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.readOnly = true;
	}
	
	function DisabilitainizioLA_SPE()
	{
//		alert("DisabilitainizioLA_SPE - inizio");

		if(document.getElementById("giorni_SPE"))
		{
	        node=document.getElementById("tipoconcessione_SPE");
	        node.style.display='none';
	        node=document.getElementById("comune_SPE");
	        node.style.display='none';
	        node=document.getElementById("semestri_SPE");
	        node.style.display='none';
	        node=document.getElementById("periodo_SPE");
	        node.style.display='none';	
	        node=document.getElementById("resto_SPE");
	        node.style.display='none';
		}
    
  //      alert("DisabilitainizioLA_SPE - fine");
	}

	 
//	
//	---------- > 	Caso in cui il primo oggetto presente è LIBERAZIONE ANTICIPATA INTEGRAZIONE(L.A.I.) :  
//					vengono presentati per primi nella form i periodi relativi a questo oggetto, 

	function abilitainizioLA_INT()
	{
	//	alert("C  - AbilitainizioLA_INT");
		
		node=document.getElementById("tipoconcessione_INT");
        node.style.display='block';
       
        AbilitaPeriodo_INT();
        AbilitaSemestri_INT();
        
        //document.ModOrdinanzaRevocaLA.< %=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = "0621"; / "2137"
        document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = codinizioLAINT;
        
        if(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[1].checked)
        	AbilitaPeriodo_INT();
		
        var valoreLI = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT %>.value;
        if(valoreLI=="")
        	valoreLI="0";
        
        document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value = valoreLI;
       
        if(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[1].checked)
        	 document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.readOnly = false;
        else
        	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.readOnly = true;
	}

	function DisabilitainizioLA_INT()
	{
//		alert("DisabilitainizioLA_INT - inizio");
		if(document.getElementById("giorni_INT"))
		{
	        node=document.getElementById("tipoconcessione_INT");
	        node.style.display='none';
	        node=document.getElementById("comune_INT");
	        node.style.display='none';
	        node=document.getElementById("semestri_INT");
	        node.style.display='none';
	        node=document.getElementById("periodo_INT");
	        node.style.display='none';
	        node=document.getElementById("resto_INT");
	        node.style.display='none';
		} 
//		alert("DisabilitainizioLA_INT - fine");
 	}


// -	-	-	-	-	-	-	
// ---->
// Decreto Legge 2013/146
	/* Abilita la modalità di selezione periodi concessi a seconda di L.A., L.A. speciale, Integrazione L.A.  */
    
	function QualeRevocaSelected(cod,indd)
    {
    	var lungh = <%=lTenori.length%>;
    	document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO%>.value = indd;
    	
		// cod oggetto selezioato 0028 / 2135 : L.A. NORMALE - - -> Abilito i campi di L.A. e Disabilito le altre L.A, e controllo i multipli di 75 e 30
		if(cod == 0028 || cod == 2135)
		{
			//document.ModOrdinanzaRevocaLA.< %=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = "0028";/ 2135
			document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = cod;
			
			if(document.getElementById("giorni_SPE"))
			{
					for(j = 0; j < lungh; j++)
		    		{
		          		if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0620" ||
		          			document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2136" )
		          		{
		          			//if( ["0044","0045","0046","0047","1241","1242","1243","1244"].indexOf(document.ModOrdinanzaRevocaLA.< %= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value ) > 0 )
		          			
		          			if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "0044" || 
		          				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0045" ||
		        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0046" ||
		        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0047" ||
		        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1241" ||
		        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1242" ||
		        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1243" ||
		        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1244" )
		          		{
		          		//	alert("OK");
		          					var totLA_SPE = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value;
		        		        	if (totLA_SPE!=0)
		        		        	{
			        		          	 alert("Azzerare totale Giorni Concessi! ");
			        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE%>.value == "0";
			        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
			        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
			        		          	 document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
			        		          	 return false;
		        		        	}
		        		        	else
		        		        	{
		        		        		document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE%>.value == "0";
		        		        	}	
		          			}
		          			else if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "0043" ||
		          					document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "1240" )	
		    				{	
		          				if (document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value == "0")
		        		        {
			        		          	alert("Inserire totale Giorni Concessi! ");
			        		          	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.focus();
			        		          	document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
			        		          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
			        		          	return false;
		        		        }
		          				else
		          				{
		          					document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO%>.value = "S";	
		          				}
		          				
		    				}	// chiude else if document.ModOrdinanzaRevocaLA.< %= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == ...		          			
	
		          		}
		          		
		    		}	// Chiude for(j = 0; j < lungh; j++)
					
					var totLA_SPE = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value;
			        if (totLA_SPE!=0 && totLA_SPE%75 != 0)
			        {
			          	alert("Il totale Giorni Concessi deve essere multiplo di 75! ");
			          	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
			          	for(j = 0; j < lungh; j++)
			    		{
			          		if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0620" || 
			          			document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2136" )
			          				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
			          		else
			          				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = false;	
			    		}
			          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
			            return false;
			        }
			        
					DisabilitaLA_SPE();
					
			}	// chiude if(document.getElementById("giorni_SPE"))	

			if(document.getElementById("giorni_INT"))
			{
				
				for(j = 0; j < lungh; j++)
	    		{
	          		if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0621" ||
	          			document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>[j].value == "2137" )
	          		{
	          			if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "0044" || 
	          				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0045" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0046" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0047" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1241" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1242" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1243" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1244" )
	          			{
	          					var totLA_INT = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value;
	        		        	if (totLA_INT!=0)
	        		        	{
		        		          	 alert("Azzerare totale Giorni Concessi! ");
		        		          	 document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT %>.value == "0";
		        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
		        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		        		          	 document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		        		          	 return false;
	        		        	}
	        		        	else
	        		        	{
	        		        		document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT %>.value == "0";
	        		        	}	
	          			}
	          			else if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "0043" ||
	          					document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "1240")	
	    				{	
	          				if (document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value == "0")
	        		        {
		        		          	alert("Inserire totale Giorni Concessi! ");
		        		          	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
		        		          	document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		        		          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		        		          	return false;
	        		        }
	          				else
	          				{
	          					document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO%>.value = "S";
	          				}	
	    				}	

	          		} // chiude if(document.ModOrdinanzaRevocaLA.< %= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0621")
	          		
	    		}	// chiude ciclo for
				
				var totLA_INT = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value;
		        if (totLA_INT!=0 && totLA_INT%30 != 0)
		        {
		          	alert("Il totale Giorni Concessi deve essere multiplo di 30! ");
		          	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
		          	for(j = 0; j < lungh; j++)
		    		{
		          		if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0621" || 
		          			document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2137" )
		          				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		          		else
		          				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = false;	
		    		}
		          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		            return false;
		        }
		        
				DisabilitaLA_INT();
				
			}	// chiude if(document.getElementById("giorni_INT"))
				
			modalita="S";
			AbilitaLA();
		
		}	// chiude if(cod == 0028 / 2135)		

		// cod oggetto selezioato 0620 / 2136 : L.A. SPECIALE - - -> Abilito i campi di L.A.Speciale e Disabilito le altre L.A, e controllo i multipli di 45 e 30
		if(cod == 0620 || cod == 2136)
		{
			//document.ModOrdinanzaRevocaLA.< %=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = "0620";/ 2136
				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = cod;

			if(document.getElementById("giornidiLA"))
			{
				for(j = 0; j < lungh; j++)
	    		{
	          		if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0028" ||
	          			document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2135")
	          		{	
	          		//	alert("1 -- Ind = "+j);
	          			if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "0044" || 
	          				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0045" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0046" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0047" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1241" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1242" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1243" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1244" )
	          			{
	          					var totLA = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.value;
	        		        	if (totLA != 0)
	        		        	{
		        		          	 alert("Azzerare totale Giorni Concessi! ");
		        		          	 document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA %>.value == "0";
		        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.focus();
		        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		        		          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		        		          	 return false;
	        		        	}
	        		        	else
	        		        	{
	        		        		document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA %>.value == "0";
	        		        	}	
	          			}
	          			else if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "0043" ||
	          					document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "1240" )	
	    				{	
	          				if (document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value == "0")
	        		        {
		        		          	alert("Inserire totale Giorni Concessi! ");
		        		          	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus();
		        		          	document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		        		          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		        		          	return false;
	        		        }
	          				else
	          				{
	          					document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO%>.value = "S";
	          				}	
	    				}
	          			
	          		}	// Chiude if(document.ModOrdinanzaRevocaLA.< %= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0028")
	          		
	    		}	// chiude ciclo for
				
				var totLA = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value;
		        if (totLA!=0 && totLA%45 != 0)
		        {
		          	alert("Il totale Giorni Concessi deve essere multiplo di 45! ");
		          	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus();
		          	for(j = 0; j < lungh; j++)
		    		{
		          		if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0028" || 
		          			document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2135" )
		          				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		          		else
		          				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = false;	
		    		}
		          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		            return false;
		        } 
		        
				DisabilitaLA();
				
			}	// chiude if(document.getElementById("giornidiLA"))

			if(document.getElementById("giorni_INT"))
			{
				for(j = 0; j < lungh; j++)
	    		{
	          		if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0621" ||
	          			document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2137" )
	          		{
	          			if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "0044" || 
	          				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0045" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0046" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0047" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1241" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1242" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1243" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1244" )
	          			{
	          					var totLA_INT = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value;
	        		        	if (totLA_INT!=0)
	        		        	{
		        		          	 alert("Azzerare totale Giorni Concessi! ");
		        		          	 document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT %>.value = "0";
		        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
		        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		        		          	 document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		        		          	 return false;
	        		        	}
	        		        	else
	        		        	{
	        		        		document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT %>.value = "0";
	        		        	}	
	          			}
	          			else if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "0043" ||
	          					document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "1240")	
	    				{	
	          				//alert("1 - salva gg = "+document.ModOrdinanzaRevocaLA.< %= ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT %>.value);
	          				if (document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value == "0")
	        		        {
		        		          	alert("Inserire totale Giorni Concessi! ");
		        		          	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
		        		          	document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		        		          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		        		          	 return false;
	        		        }
	          				else
	          				{
	          					document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO%>.value = "S";
	          				}	
	    				}	          			

	          		}	
	          		
	    		}	// chiude ciclo for
				
				var totLA_INT = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value;
		        if (totLA_INT != 0 && totLA_INT%30 != 0)
		        {
		          	alert("Il totale Giorni Concessi deve essere multiplo di 30! ");
		          	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
		          	for(j = 0; j < lungh; j++)
		    		{
		          		if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0621" ||
		          			document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2137" )
		          				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		          		else
		          				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = false;	
		    		}
		          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		            return false;
		        }
			
		        DisabilitaLA_INT();
		        
			}	// chiude if(document.getElementById("giorni_INT"))
			
			modalita="S";
			AbilitaLA_SPE();
		
		}	// chiude if(cod == 0620)	
		
		// cod oggetto selezioato 0621 / 2137 : L.A. INTEGRAZIONE - - -> Abilito i campi di L.A.Integraz. e Disabilito le altre L.A, e controllo i multipli di 45 e 75
		if(cod == 0621 || cod == 2137)
		{
			//document.ModOrdinanzaRevocaLA.< %=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = "0621";/ 2137
			document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value = cod;
			
			if(document.getElementById("giornidiLA"))
			{
				for(j = 0; j < lungh; j++)
	    		{
	          		if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0028" ||
	          			document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2135" )
	          		{
	          			if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "0044" || 
	          				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0045" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0046" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0047" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1241" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1242" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1243" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1244" )
	          			{
	          					var totLA = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.value;
	        		        	if (totLA != 0)
	        		        	{
		        		          	 alert("Azzerare totale Giorni Concessi! ");
		        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA %>.value == "0";
		        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.focus();
		        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		        		          	 document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		        		          	 return false;
	        		        	}
	        		        	else
	        		        	{
	        		        		document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA %>.value == "0";
	        		        	}	
	          			}
	          			else if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "0043" ||
	          					document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "1240")	
	    				{	
	          				if (document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value == "0")
	        		        {
		        		          	alert("Inserire totale Giorni Concessi! ");
		        		          	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus();
		        		          	document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		        		          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		        		          	return false;
	        		        }
	          				else
	          				{
	          					document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO%>.value = "S";
	          				}	
	    				}

	          		}
	          		
	    		}	// chiude ciclo for
				
				var totLA = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value;
		        if (totLA!=0 && totLA%45 != 0)
		        {
		          	alert("Il totale Giorni Concessi deve essere multiplo di 45! ");
		          	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus();
		          	for(j = 0; j < lungh; j++)
		    		{
		          		if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0028" ||
		          			document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2135")
		          				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		          		else
		          				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = false;	
		    		}
		          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		            return false;
		        }
		        
				DisabilitaLA();

			}	// chiude if(document.getElementById("giornidiLA"))	

			if(document.getElementById("giorni_SPE"))
			{
				for(j = 0; j < lungh; j++)
	    		{
	          		if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0620" ||
	          			document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2136")
	          		{
	          			if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "0044" || 
	          				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0045" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0046" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0047" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1241" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1242" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1243" ||
	        				document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "1244" )
	          			{
	          					var totLA_SPE = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value;
	        		        	if (totLA_SPE!=0)
	        		        	{
		        		          	 alert("Azzerare totale Giorni Concessi! ");
		        		          	 document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE %>.value == "0";
		        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
		        		          	 document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		        		          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		        		          	 return false;
	        		        	}
	        		        	else
	        		        	{
	        		        		document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE %>.value == "0";
	        		        	}	
	          			}
	          			else if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "0043" ||
	          					document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>[j].value == "1240")	
	    				{	
	          				if (document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value == "0")
	        		        {
		        		          	alert("Inserire totale Giorni Concessi! ");
		        		          	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.focus();
		        		          	document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
		        		          	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
		        		          	return false;
	        		        }
	          				else
	          				{
	          					document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO%>.value = "S";
	          				}	
	    				}
	          		}
	          		
	    		}	// chiude ciclo for
				
				var totLA_SPE = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value;
		        if (totLA_SPE !=0 && totLA_SPE%75 != 0)
				{
				       	alert("Il totale Giorni Concessi deve essere multiplo di 75! ");
				       	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
				       	for(j = 0; j < lungh; j++)
				   		{
				          		if(document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "0620" ||
				          			document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[j].value == "2136")
				          				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = true;
				          		else
				          				document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[j].checked = false;	
				   		}
				       	document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = j;
				        return false;
				}

				DisabilitaLA_SPE();
				
    		} // chiude if(document.getElementById("giorni_SPE"))
    			
			modalita="S";
			AbilitaLA_INT();
			
		}	// chiude if(cod == 0621)	
		
    }   // Chiude Qualeliberazioneconcede()
//
// Oggetto : L.A.(Liberazione Anticipata)
	function AbilitaLA()
	{
        node=document.getElementById("tipoconcessioneLA");
        node.style.display='block';
       
        AbilitaPeriodo();
        AbilitaSemestri();
        if(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked)
        	AbilitaPeriodo();

        node=document.getElementById("resto");
        node.style.display='block';
        
        var valoreLA = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>.value;
        if(valoreLA=="")
        	valoreLA="0";
        
        document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value = valoreLA;
       
        if(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked)
        	 document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = false;
        else
        	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = true;
	}
     
	function DisabilitaLA()
	{
	//	alert("DisabilitaLA - inizio");
		if(document.getElementById("giornidiLA"))
		{
			var valLA = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value;
			document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>.value = valLA;
	
	        node=document.getElementById("tipoconcessioneLA");
	        node.style.display='none';
	        node=document.getElementById("comune");
	        node.style.display='none';
	        node=document.getElementById("semestri");
	        node.style.display='none';
	        node=document.getElementById("periodo");
	        node.style.display='none';
	        node=document.getElementById("resto");
	        node.style.display='none';
		}
    //    alert("DisabilitaLA - fine");
	}
	
// Oggetto : L.A.S.(Liberazione Anticipata Speciale)
	function AbilitaLA_SPE()
	{
        node=document.getElementById("tipoconcessione_SPE");
        node.style.display='block';
        
        AbilitaPeriodo_SPE();
        AbilitaSemestri_SPE();  
        if(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[1].checked)
        	AbilitaPeriodo_SPE();

        node=document.getElementById("resto_SPE");
        node.style.display='block';
        
        var valoreSPE=document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE%>.value;
        if(valoreSPE=="")
        	valoreSPE="0";
        

        document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value = valoreSPE;
        
        if(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[1].checked)
       	 	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.readOnly = false;
        else
        	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.readOnly = true;
	}
     
	function DisabilitaLA_SPE()
	{
//		alert("DisabilitaLA_SPE - inizio");

		if(document.getElementById("giorni_SPE"))
		{
			var valSPE = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value;
			document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE%>.value = valSPE;
			
	        node=document.getElementById("tipoconcessione_SPE");
	        node.style.display='none';
	        node=document.getElementById("comune_SPE");
	        node.style.display='none';
	        node=document.getElementById("semestri_SPE");
	        node.style.display='none';
	        node=document.getElementById("periodo_SPE");
	        node.style.display='none';	
	        node=document.getElementById("resto_SPE");
	        node.style.display='none';
		}
    
  //      alert("DisabilitaLA_SPE - fine");
	}

	// Oggetto : L.A.I.(Liberazione Anticipata Integrazione)
	function AbilitaLA_INT()
	{
	//	alert("AbilitaLA_INT - inizio");
		
        node=document.getElementById("tipoconcessione_INT");
        node.style.display='block';

        AbilitaPeriodo_INT();
        AbilitaSemestri_INT();
        
        if(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>[1].checked)
        	AbilitaPeriodo_INT();     
        
        node=document.getElementById("resto_INT");
        node.style.display='block';

        var valoreLA_INT = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT%>.value;
        if(valoreLA_INT=="")
        	valoreLA_INT="0";
        
        document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value = valoreLA_INT;
        
        if(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>[1].checked)
       	 	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.readOnly = false;
        else
        	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.readOnly = true;
        	
     //   alert("AbilitaLA_INT - fine");	
	}
     
	function DisabilitaLA_INT()
	{
//		alert("DisabilitaLA_INT - inizio");
		if(document.getElementById("giorni_INT"))
		{
			var valLA_INT = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value;
			document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT%>.value = valLA_INT;
	
	        node=document.getElementById("tipoconcessione_INT");
	        node.style.display='none';
	        node=document.getElementById("comune_INT");
	        node.style.display='none';
	        node=document.getElementById("semestri_INT");
	        node.style.display='none';
	        node=document.getElementById("periodo_INT");
	        node.style.display='none';
	        node=document.getElementById("resto_INT");
	        node.style.display='none';
		} 
//		alert("DisabilitaLA_INT - fine");
 	}

	// -------------------- > 	JAVASCRIPT L.A.		LIBERAZIONE ANTICIPATA 
	
	// Chiusura dell'eventuale blocco aperto di L.A. / L.A. Speciale /L.A. Integrazione
    function Chiusura()
    {
    	//	alert("Chiusura: inizio - NumCheck = "+NumCheck);
	      var retValue = true;
	      for (var i=0; i < NumCheck; i++)
	      {
	    	    if(document.getElementById("giornidiLA"))
				{ 
			        if (document.ModOrdinanzaRevocaLA.gg[i].checked)
			        {
			              document.ModOrdinanzaRevocaLA.gg[i].checked = false;
			              retValue = ViewLayer(i);
			        }
				}    
		        
	    	    if(document.getElementById("giorni_SPE"))
				{
			        if (document.ModOrdinanzaRevocaLA.gg_SPE[i].checked)
			        {
			              document.ModOrdinanzaRevocaLA.gg_SPE[i].checked = false;
			              retValue = ViewLayer_SPE(i);
			        }
				} 
	    	    
	    	    if(document.getElementById("giorni_INT"))
				{
			        if (document.ModOrdinanzaRevocaLA.gg_INT[i].checked)
			        {
			              document.ModOrdinanzaRevocaLA.gg_INT[i].checked = false;
			              retValue = ViewLayer_INT(i);
			        }
				}    
		        
	      }
	      
      	  return retValue;
    }
    
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
		            //  alert("DisabilitaDate disabilito L" + i);
		             node=document.getElementById('L'+i);
		             node.disabled = true;
		             //alert("disabilitato L" + i);
		          }
	        }
	        
	      if(!document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[0].checked)
	      {
		        for (i=0; i < NumTotale; i++)
		        {
		             node=document.getElementById('L'+i);
		             node.disabled = true;
		           //  alert("DisabilitaDate disabilitato L" + i);
		        }
	      }
	      else
	      {
	             node=document.getElementById('L'+ IndPer);
	             node.disabled = true;
	           //  alert("DisabilitaDate disabilitato L" + IndPer);
	
	      }

      //  alert("DisabilitaDate: fine");
      	return;
    }

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
        //alert ("giorni = " + periodo);
        if (periodo != 0)
        {
           if (periodo != 180 )
           {
              retValue = confirm("Le date inserite individuano un periodo di " + periodo +" giorni e non di 180. Confermi comunque la concessione del semestre?");
           }
           if (retValue)
               giorni[id] = 45;
        }
        else
        {
        	if (id < NumTotale)
        		document.ModOrdinanzaRevocaLA.gg[id].value = 1; 
        }          
        //alert("conteggioDate fine ");
        return retValue;
    
    }	// chiude function conteggioDate (id)

    /* Lettura  e controllo del periodo di posizione elem  */
    function leggiDate (elem)
    {
       var ret = true;
       var gg0 = FillDM(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value);
       var mm0 = FillDM(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value);
       var aa0 = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value;
       var dataIni = gg0 + "/" + mm0 + "/" + aa0;
       var gg1 = FillDM(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value);
       var mm1 = FillDM(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value);
       var aa1 = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value;
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

    function aggiornaTotGiorni()
    {
     	//  alert("aggiornaTotGiorni(): modalita = "+modalita);
	      if (modalita == 'S')
	      {
		        var totale = 0;
		        for (var i=0; i<NumTotale; i++)
		        {  
		            if (document.ModOrdinanzaRevocaLA.gg[i].value == 2)
		                giorni[i] = 45;
		            totale += giorni[i];
		            
		        }
	        // alert ("Totale giorni concessi: " +totale);
	        	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value = totale;
	       }
    // alert("aggiornaTotGiorni(): fine");
   }

    function checkDate(id)
    {
       //alert("check id : " + id);
      if (id < NumTotale) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI%>[id].checked = true;
      } else if (id == IndPer) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO%>.checked = true;
      } else if (id == IndRig) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI%>.checked = true;
      } else if (id == IndIna) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI%>.checked = true;
      } else if (id == IndNlp) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP%>.checked = true;
      }
      //alert("check id : " + id);
    }

    function uncheckDate(id)
    {
       //alert ("uncheck " + id);

      if (id < NumTotale) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI%>[id].checked = false;
      } else if (id == IndPer) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO%>.checked = false;
      } else if (id == IndRig) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI%>.checked = false;
      } else if (id == IndIna) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI%>.checked = false;
      } else if (id == IndNlp) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP%>.checked = false;
      }
       //alert ("uncheck " + id);
	}

    function IsCheckedDate(id)
    {
        var retValue = false;
       //alert ("IsCheckedDate " + id);

      if (id < NumTotale) {
       if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI%>[id].checked)
          retValue = true;
      }else if (id == IndPer) {
        if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO%>.checked)
          retValue = true;
      }else if (id == IndRig) {
        if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI%>.checked)
          retValue = true;
      }else if (id == IndIna) {
        if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI%>.checked)
          retValue = true;
      }else if (id == IndNlp) {
        if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP%>.checked)
          retValue = true;
      }
      //alert ("IsCheckedDate " + retValue);

      return retValue;
    }

    /* Impedisce Focus su campo testo input numero giorni quando l'inserimento è per semestri */
    function rifiutaFocusSemestri()
    {
    	if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[0].checked)
    		document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.blur();
    	
    }
    
    /* Abilita la modalità di selezione periodi concessi a semestri */
    function AbilitaSemestri()
    {
      //  alert("AbilitaSemestri: inizio - modalita = "+modalita);
          var retValue = true;
 	      if (modalita != 'S')
	      {
		         retValue = Chiusura();
		         if (retValue)
		         {
		            node=document.getElementById("semestri");
		         	node.style.display='block';
		         	
		            node=document.getElementById("periodo");
		         	node.style.display='none';
		         	
		           // Salvataggio dei giorni concessi per il periodo unico
		           GiorniConcessi = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value;
		           
		           // Nell'inserimento per semestri il numero dei giorni concessi non può essere inputato a mano
		           document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = true;
		
		           // Ripristino dei giorni concessi per semestre
		           modalita = 'S';
		           aggiornaTotGiorni();
		         }
		         else
		         {
		           document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked = true;
		           document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[0].checked = false;
		         }
	      }
  //       alert("AbilitaSemestri: fine");
         return retValue;
    }

    /* Abilita la modalità di selezione periodi concessi a periodo unico */
    function AbilitaPeriodo()
    {
    	// alert("C AbilitaPeriodo - inizio - modalita = "+modalita);
         var retValue = true;
 	      if (modalita != "C")
	      {
	         retValue = Chiusura();
	         if (retValue)
	         {
	        	   node=document.getElementById("comune");
		           node.style.display='block';
		           node=document.getElementById("semestri");
		           node.style.display='none';
		           node=document.getElementById("periodo");
		           node.style.display='block';
		          
		           // Ripristino dei giorni concessi per periodo unico
		           modalita = "C";
		           // Nell'inserimento per unico periodo il numero dei giorni concessi deve essere inputato a mano
		           document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = false;
		
		           document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value = GiorniConcessi;
	         }
	         else
	         {
	           document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[0].checked = true;
	           document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked = false;
	         }
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
	         if (i != id && document.ModOrdinanzaRevocaLA.gg[id].checked)
	         { document.ModOrdinanzaRevocaLA.gg[i].disabled=true;}
	         else
	         { document.ModOrdinanzaRevocaLA.gg[i].disabled=false;}
       }
       
       // Apertura o chiusura del campo date e controllo correttezza date in chiusura */
     	//  alert ("C ViewLayer 1 - NumCheck = " + NumCheck);
       for (var i=0; i<NumCheck; i++)
       {
 
    	   	node=document.getElementById('L'+i);
          	if (i == id && document.ModOrdinanzaRevocaLA.gg[id].checked)
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
 								document.ModOrdinanzaRevocaLA.gg[id].checked = true;
 			        		}
 			        		else
 			        		{
 								document.ModOrdinanzaRevocaLA.gg[i].disabled=true;
 			        		}
 			      		}
 						alert('Le date devono essere contigue.');
 						return false;
 					}
 					else
 					{
 						if (conteggioDate (i) == false)
                 		{
                     		document.ModOrdinanzaRevocaLA.gg[id].checked = true;

 							// disabilita gli altri Check
 	      					for (var i=0; i<NumCheck; i++)
 					    	{
 					    		if (i != id)
 					        	{
 									document.ModOrdinanzaRevocaLA.gg[i].disabled=true;
 					        	}
 					    	}

 	                    	return false;
                 		}
 						
 					}	// chiude else if (chkDateContigue(id) == false)
	 				
             }	// chiude if(i=id)
             	
             node.style.display='none';

          } // chiude la else di if (i == id && document.ModOrdinanzaRevocaLA.gg[id].checked)
 
        }	// chiude ciclo for (var i=0; i<NumCheck; i++)
       
        // Colore dei check
        var blue=true;
        node=document.getElementById('SL'+id);
        
        var elem = 0;
        elem=id*NumDate;
        for (var j=0; j<NumDate; j++, elem++)
        {
	          blue=blue && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value=="")  && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value=="")  && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value=="");
	          blue=blue && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value=="")  && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value=="")  && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value=="");
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
 
        aggiornaTotGiorni();
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
				var gg0 = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value;

				if(gg0 != '')
				{
					riga = true;
				}
			}
			else
			{
				var gg0 = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value;
				var gg0rigaprima = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem-1].value;
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

// FINE FUNZIONI JAVASCRIPT L.A.
// -	-	-	-	-	-	-	-	-	-	

// -------------------- > 	JAVASCRIPT L.A.S	LIBERAZIONE ANTICIPATA SPECIALE

    function DisabilitaDate_SPE()
    {
      //  alert("DisabilitaDate_SPE: inizio");
        var i=0;
        for (i=0; i < NumCheck; i++)
        {
         // Vengono disabilitati tutti i blocchi periodi vuoti
	          if(!IsCheckedDate_SPE(i))
	          {
	          //   alert(" DisabilitaDate_SPE disabilito L" + i);
	             node=document.getElementById('L_SPE'+i);
	             node.disabled = true;
	             //alert("disabilitato L" + i);
	          }
        }
        
	      if(!document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE %>[0].checked)
	      {
		        for (i=0; i < NumTotale; i++)
		        {
		             node=document.getElementById('L_SPE'+i);
		             node.disabled = true;
		          //   alert(" DisabilitaDate_SPE disabilitato L" + i);
		        }
	      }
	      else
	      {
	             node=document.getElementById('L_SPE'+ IndPer);
	             node.disabled = true;
	          //   alert(" DisabilitaDate_SPE disabilitato L" + IndPer);
	
	      }

    //    alert("DisabilitaDate_SPE: fine");
      return;
    }

    var DataFine;
    var DataIni;
    /* Controllo  e conteggio date  per L.A.S.*/
    function conteggioDate_SPE (id)
    {
    //	alert ("conteggioDate_SPE - inizio - id = "+id);
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

	          if (leggiDate_SPE(elem) == false)
	          {
	            // alert ("Errore nelle date");
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
        else
        {
        	if (id < NumTotale)
        		document.ModOrdinanzaRevocaLA.gg_SPE[id].value = 1; 
        }          
        //alert("conteggioDate fine ");
        return retValue;
        
    }

    /* Lettura  e controllo del periodo di posizione elem per L.A.S. */
    function leggiDate_SPE (elem)
    {
   // 	alert ("leggiDate_SPE - inizio - elem = "+elem);
       var ret = true;
       var gg0 = FillDM(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[elem].value);
       var mm0 = FillDM(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE %>[elem].value);
       var aa0 = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE %>[elem].value;
       var dataIni = gg0 + "/" + mm0 + "/" + aa0;
       var gg1 = FillDM(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_SPE %>[elem].value);
       var mm1 = FillDM(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_SPE %>[elem].value);
       var aa1 = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_SPE %>[elem].value;
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
       }
       
      return ret;
      
    }	// chiude leggiDate_SPE

    function aggiornaTotGiorni_SPE()
    {
     	//  alert("aggiornaTotGiorni_SPE(): modalita = "+modalita);
	      if (modalita == 'S')
	      {
		        var totale = 0;
		        for (var i=0; i<NumTotale; i++)
		        {  
		            if (document.ModOrdinanzaRevocaLA.gg_SPE[i].value == 2)
		                giorni_spe[i] = 75;
		            totale += giorni_spe[i];
		            
		        }
	        	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value = totale;
	       }
   }

    function checkDate_SPE(id)
    {
       //alert("check_SPE id : " + id);
	      if (id < NumTotale) {
	      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE %>[id].checked = true;
	      } else if (id == IndPer) {
	      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE %>.checked = true;
	      } else if (id == IndRig) {
	      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE %>.checked = true;
	      } else if (id == IndIna) {
	      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE %>.checked = true;
	      } else if (id == IndNlp) {
	      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE %>.checked = true;
	      }
      //alert("check id : " + id);
    }

    function uncheckDate_SPE(id)
    {
       //alert ("uncheck_SPE " + id);

	      if (id < NumTotale) {
	      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE %>[id].checked = false;
	      } else if (id == IndPer) {
	      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE %>.checked = false;
	      } else if (id == IndRig) {
	      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE %>.checked = false;
	      } else if (id == IndIna) {
	      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE %>.checked = false;
	      } else if (id == IndNlp) {
	      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE %>.checked = false;
	      }
       //alert ("uncheck " + id);
	}

    function IsCheckedDate_SPE(id)
    {
        	var retValue = false;
       //alert ("IsCheckedDate " + id);

	      if (id < NumTotale) {
	       if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE %>[id].checked)
	          retValue = true;
	      }else if (id == IndPer) {
	        if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE %>.checked)
	          retValue = true;
	      }else if (id == IndRig) {
	        if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE %>.checked)
	          retValue = true;
	      }else if (id == IndIna) {
	        if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE %>.checked)
	          retValue = true;
	      }else if (id == IndNlp) {
	        if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE %>.checked)
	          retValue = true;
	      }

      return retValue;
    }

    /* Impedisce Focus su campo testo input numero giorni quando l'inserimento è per semestri */
    function rifiutaFocusSemestri_SPE()
    {
    	if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE %>[0].checked)
    		document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.blur();
    	
    }
    
    /* Abilita la modalità di selezione periodi concessi a semestri di L.A.*/
    function AbilitaSemestri_SPE()
    {
   // 	alert("C inizio - AbilitaSemestri_SPE");
   
	       var retValue = true;
	       if (modalita != 'S')
	       {
		         retValue = Chiusura();
		         if (retValue)
		         {
			            node=document.getElementById("semestri_SPE");
			         	node.style.display='block';
			         	
			            node=document.getElementById("periodo_SPE");
			         	node.style.display='none';
			         	
			           // Salvataggio dei giorni concessi per il periodo unico
			           GiorniConcessi_spe = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value;
			           
			           // Nell'inserimento per semestri il numero dei giorni concessi non può essere inputato a mano
			           document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.readOnly = true;
			
			           // Ripristino dei giorni concessi per semestre
			           modalita = 'S';
			           aggiornaTotGiorni_SPE();
		         }
		         else
		         {
		           		document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE %>[1].checked = true;
		           		document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE %>[0].checked = false;
		         }
	       }
        // alert("AbilitaSemestri_SPE: fine");
         return retValue;
    
    }	// chiude AbilitaSemestri_SPE()

    /* Abilita la modalità di selezione periodi concessi a periodo unico di L.A.S.*/
    function AbilitaPeriodo_SPE()
    {
   		//  alert("C inizio - AbilitaPeriodoUnico_SPE - modalita = "+modalita);
          var retValue = true;

	      if (modalita != 'C')
	      {
		         retValue = Chiusura();
		         if (retValue)
		         {
		        	  node=document.getElementById("comune_SPE");
			          node.style.display='block';
			          node=document.getElementById("semestri_SPE");
			          node.style.display='none';
			          node=document.getElementById("periodo_SPE");
			          node.style.display='block';

			           // Ripristino dei giorni concessi per periodo unico
			           modalita = 'C';
			           // Nell'inserimento per unico periodo il numero dei giorni concessi deve essere inputato a mano
			           document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.readOnly = false;
			
			           document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value = GiorniConcessi_spe;
		         }
		         else
		         {

			           document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE %>[0].checked = true;
			           document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE %>[1].checked = false;
		         }
	      }
	//        alert("AbilitaPeriodo spe : fine");
	         return retValue;
    }
    
    /* Visualizzazione del layer Date corrispondente alla posizione id  per L.A.S. */
    function ViewLayer_SPE(id)
    {
   //   alert ("C ViewLayer_SPE id: " + id);
       // Check ON/OFF
       for (var i=0; i<NumCheck; i++)
       {
	         if (i != id && document.ModOrdinanzaRevocaLA.gg_SPE[id].checked)
	         { document.ModOrdinanzaRevocaLA.gg_SPE[i].disabled=true;}
	         else
	         { document.ModOrdinanzaRevocaLA.gg_SPE[i].disabled=false;}
       }
       
       // Apertura o chiusura del campo date e controllo correttezza date in chiusura */
       for (var i=0; i<NumCheck; i++)
       {
 
    	   node=document.getElementById('L_SPE'+i);
          if (i == id && document.ModOrdinanzaRevocaLA.gg_SPE[id].checked)
          { // apertura
           //  alert ("ViewLayer_SPE Sto aprendo - i = " + i);
        	 node.style.display='block';
          }
          else
          {
             if (i == id)
             { // chiusura
              //  alert ("ViewLayer_SPE Sto chiudendo - i =" +i+"  id = "+id);
	 				if (chkDateContigue_SPE(id) == false)
	 				{
	 				 	// Reinserisce i Check
	      					for (var i=0; i<NumCheck; i++)
	 			      		{
		 			        	if (i == id)
		 			        	{
		 							document.ModOrdinanzaRevocaLA.gg_SPE[id].checked = true;
		 			        	}
		 			        	else
		 			        	{
		 							document.ModOrdinanzaRevocaLA.gg_SPE[i].disabled=true;
		 			        	}
	 			      		}
	 						alert('Le date devono essere contigue.');
	 						return false;
	 				}
 					else
	 				{
	 					if (conteggioDate_SPE(i) == false)
	                 	{
	                     	document.ModOrdinanzaRevocaLA.gg_SPE[id].checked = true;
	 						// disabilita gli altri Check
	 	      				for (var i=0; i<NumCheck; i++)
	 					    {
	 					    	if (i != id)
	 					        {
	 								document.ModOrdinanzaRevocaLA.gg_SPE[i].disabled=true;
	 					        }
	 					    }
	
	 	                    return false;
	                 	 }

	 				}
             }
             node.style.display='none';

          } // chiude la else di if (i == id && document.ModOrdinanzaRevocaLA.gg[id].checked)
 
        }	// chiude ciclo for (var i=0; i<NumCheck; i++)
       
        var blue=true;
        node=document.getElementById('SL_SPE'+id);
        
        var elem = 0;
        elem=id*NumDate;
        for (var j=0; j<NumDate; j++, elem++)
        {
	          blue=blue && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[elem].value=="")  && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE %>[elem].value=="")  && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE %>[elem].value=="");
	          blue=blue && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_SPE %>[elem].value=="")  && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_SPE %>[elem].value=="")  && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_SPE %>[elem].value=="");
        }
        if (blue)
        {
          node.style.color="Navy";
          uncheckDate_SPE(id);
        }
        else
        {
          node.style.color="Red";
          checkDate_SPE(id);
        }
 
        aggiornaTotGiorni_SPE();
        //alert("ViewLayeer_SPE fine ");
        return true;
    }

  //CONTROLLO DATE CONTIGUE
	function chkDateContigue_SPE(id)
	{
		var returnchkDC = true;

		var rigaprima = false;
		var riga = false;

		elem=id*NumDate;
		for (var j=0; j<NumDate; j++, elem++)
		{
			if(j==0)
			{
				var gg0 = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[elem].value;

				if(gg0 != '')
				{
					riga = true;
				}
			}
			else
			{
				var gg0 = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[elem].value;
				var gg0rigaprima = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[elem-1].value;
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
//
// -------------------- > 	JAVASCRIPT L.A.I	LIBERAZIONE ANTICIPATA INTEGRAZIONE

    function DisabilitaDate_INT()
    {
	      //alert("DisabilitaDate_INT: inizio");
	      var i=0;
	      for (i=0; i < NumCheck; i++)
	      {
		         // Vengono disabilitati tutti i blocchi periodi vuoti
		          if(!IsCheckedDate_INT(i))
		          {
		             // alert("disabilito L_INT" + i);
		             node=document.getElementById('L_INT'+i);
		             node.disabled = true;
		          }
	      }
	      if(!document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[0].checked)
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
	
	        //alert("DisabilitaDate_INT: fine");
	      return;
    }

    var DataFine;
    var DataIni;
    
    /* Controllo  e conteggio date  per L.A.I.*/
    function conteggioDate_INT (id)
    {
    //	alert ("conteggioDate_INT - inizio - id = "+id);
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

	          if (leggiDate_INT(elem) == false)
	          {
		            // alert ("Errore nelle date INT");
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
        else
        {
        	if (id < NumTotale)
        		document.ModOrdinanzaRevocaLA.gg_INT[id].value = 1; 
        }          
//        alert("conteggioDate_INT fine ");
        return retValue;
    
    }	// chiude function conteggioDate_INT (id)

    /* Lettura  e controllo del periodo di posizione elem per L.A.I. */
    function leggiDate_INT (elem)
    {
    //	alert ("leggiDate_INT - inizio - elem = "+elem);
	       var ret = true;
	       var gg0 = FillDM(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[elem].value);
	       var mm0 = FillDM(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT %>[elem].value);
	       var aa0 = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT %>[elem].value;
	       var dataIni = gg0 + "/" + mm0 + "/" + aa0;
	       var gg1 = FillDM(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_INT %>[elem].value);
	       var mm1 = FillDM(document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_INT %>[elem].value);
	       var aa1 = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_INT %>[elem].value;
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
	       }
	       
	      return ret;
      
    }	// chiude function leggiDate_INT (elem)

    function aggiornaTotGiorni_INT()
    {
     	//  alert("aggiornaTotGiorni_INT(): modalita = "+modalita);
	      if (modalita == 'S')
	      {
		        var totale = 0;
		        for (var i=0; i<NumTotale; i++)
		        {  
		            if (document.ModOrdinanzaRevocaLA.gg_INT[i].value == 2)
		                giorni_int[i] = 30;
		            
		            totale += giorni_int[i];
		            
		        }
	        	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value = totale;
	       }
   }

    function checkDate_INT(id)
    {
       //alert("check_INT id : " + id);
      if (id < NumTotale) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT %>[id].checked = true;
      } else if (id == IndPer) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT %>.checked = true;
      } else if (id == IndRig) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT %>.checked = true;
      } else if (id == IndIna) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT %>.checked = true;
      } else if (id == IndNlp) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT %>.checked = true;
      }
      //alert("check_INT id : " + id);
    }

    function uncheckDate_INT(id)
    {
       //alert ("Inizio uncheckdate_INT - id = " + id);

      if (id < NumTotale) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT %>[id].checked = false;
      } else if (id == IndPer) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT %>.checked = false;
      } else if (id == IndRig) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT %>.checked = false;
      } else if (id == IndIna) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT %>.checked = false;
      } else if (id == IndNlp) {
      document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT %>.checked = false;
      }
       //alert ("Fine uncheckdate_INT id = " + id);
	}

    function IsCheckedDate_INT(id)
    {
        var retValue = false;
       //alert ("inizio IsCheckedDate_INT - id = " + id);

	      if (id < NumTotale) 
	      {
	       		if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT %>[id].checked)
	          		retValue = true;
	      }
	      else if (id == IndPer) 
	      {
	        	if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT %>.checked)
	          	retValue = true;
	      }
	      else if (id == IndRig) 
	      {
	        if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT %>.checked)
	          retValue = true;
	      }
	      else if (id == IndIna) 
	      {
	        if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT %>.checked)
	          retValue = true;
	      }
	      else if (id == IndNlp) 
	      {
	        if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT %>.checked)
	          retValue = true;
	      }
	      //alert ("fine IsCheckedDate_INT  - retvalue " + retValue);

      return retValue;
    }

    /* Impedisce Focus su campo testo input numero giorni quando l'inserimento è per semestri */
    function rifiutaFocusSemestri_INT()
    {
    	if (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[0].checked)
    		document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.blur();
    	
    }
    
    /* Abilita la modalità di selezione periodi concessi a semestri di L.A.I.*/
    function AbilitaSemestri_INT()
    {
    //	alert("C inizio - AbilitaSemestri_INT - modalita = " +modalita);
   
       	var retValue = true;
       	if (modalita != 'S')
      	{

	         retValue = Chiusura();
	         if (retValue)
	         {
	        	 	node=document.getElementById("comune_INT");
		          	node.style.display='block';
		            node=document.getElementById("semestri_INT");
		         	node.style.display='block';
		         	
		            node=document.getElementById("periodo_INT");
		         	node.style.display='none';
		         	
		           // Salvataggio dei giorni concessi per il periodo unico
		           GiorniConcessi_int = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value;
		           
		           // Nell'inserimento per semestri il numero dei giorni concessi non può essere inputato a mano
		           document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.readOnly = true;
		
		           // Ripristino dei giorni concessi per semestre
		           modalita = 'S';
		           aggiornaTotGiorni_INT();
	         }
	         else
	         {
	           		document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[1].checked = true;
	           		document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[0].checked = false;
	         }
      	}
     //    alert("AbilitaSemestri_INT: fine");
         return retValue;
    
    }	// chiude AbilitaSemestri_INT()

    /* Abilita la modalità di selezione periodi concessi a periodo unico di L.A.I.*/
    function AbilitaPeriodo_INT()
    {
   		//  alert("C - AbilitaPeriodo_INTE - modalita = "+modalita);
          var retValue = true;

	      if (modalita != 'C')
	      {
		         retValue = Chiusura();
		         if (retValue)
		         {
		        	  node=document.getElementById("comune_INT");
			          node.style.display='block';
			          node=document.getElementById("semestri_INT");
			          node.style.display='none';
			          node=document.getElementById("periodo_INT");
			          node.style.display='block';

			           // Ripristino dei giorni concessi per periodo unico
			           modalita = 'C';
			           // Nell'inserimento per unico periodo il numero dei giorni concessi deve essere inputato a mano
			           document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.readOnly = false;
			           document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value = GiorniConcessi_int;
		         }
		         else
		         {
			           document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[0].checked = true;
			           document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[1].checked = false;
		         }
	      }
	  //      alert("AbilitaPeriodo INT : fine");
	         return retValue;
	         
    }	// chiude function AbilitaPeriodo_INT()

    
    /* Visualizzazione del layer Date corrispondente alla posizione id  per L.A.I. */
    function ViewLayer_INT(id)
    {
  //    alert ("C ViewLayer_INT id: " + id);
       // Check ON/OFF
       for (var i=0; i<NumCheck; i++)
       {
	         if (i != id && document.ModOrdinanzaRevocaLA.gg_INT[id].checked)
	         { 
	        	 document.ModOrdinanzaRevocaLA.gg_INT[i].disabled=true;
	         }
	         else
	         { 
	        	 document.ModOrdinanzaRevocaLA.gg_INT[i].disabled=false;
	          }
       }
       
       // Apertura o chiusura del campo date e controllo correttezza date in chiusura */
     //  alert ("C ViewLayer_INT 1 - NumCheck = " + NumCheck);
       for (var i=0; i<NumCheck; i++)
       {
    	   node=document.getElementById('L_INT'+i);
           if (i == id && document.ModOrdinanzaRevocaLA.gg_INT[id].checked)
           { 	// apertura
	        	 node.style.display='block';
           }
           else
           {
	             if (i == id)
	             { // chiusura
//	            	 alert('INT chiudo  - i = '+i);
	 				if (chkDateContigue_INT(id) == false)
	 				{
	 				 	// Reinserisce i Check
	 				 		// alert('INT Reinserisce i Check  - id = '+id);
	      					for (var i=0; i<NumCheck; i++)
		 			      	{
		 			        	if (i == id)
		 			        	{
		 							document.ModOrdinanzaRevocaLA.gg_INT[id].checked = true;
		 			        	}
		 			        	else
		 			        	{
		 							document.ModOrdinanzaRevocaLA.gg_INT[i].disabled=true;
		 			        	}
		 			      	}
	      					
		 					alert('Le date devono essere contigue.');
		 					return false;
	 				}
	 				else
	 				{
	 					if (conteggioDate_INT(i) == false)
	                 	{
	                     	document.ModOrdinanzaRevocaLA.gg_INT[id].checked = true;
	
	 						// disabilita gli altri Check
	 	      				for (var i=0; i<NumCheck; i++)
	 					    {
	 					    	if (i != id)
	 					        {
	 								document.ModOrdinanzaRevocaLA.gg_INT[i].disabled=true;
	 					        }
	 					    }
	
	 	                    return false;
	                 	}
//
	 				}	// chiude else if (chkDateContigue_INT(id) == false)
//	 				
	             }	// chiude if (i == id)

	             node.style.display='none';

          } // chiude la else di if (i == id && document.ModOrdinanzaRevocaLA.gg_INT[id].checked)
 
        }	// chiude for (var i=0; i<NumCheck; i++)
       
        var blue=true;
        node=document.getElementById('SL_INT'+id);
         
        var elem = 0;
        elem=id*NumDate;
 
        for (var j=0; j<NumDate; j++, elem++)
        {
	          blue=blue && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[elem].value=="")  && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT %>[elem].value=="")  && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT %>[elem].value=="");
	          blue=blue && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_INT %>[elem].value=="")  && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_INT %>[elem].value=="")  && (document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_INT %>[elem].value=="");
        }
        if (blue)
        {
	          node.style.color="Navy";
	          uncheckDate_INT(id);
        }
        else
        {
	          node.style.color="Red";
	          checkDate_INT(id);
        }
 
	    aggiornaTotGiorni_INT();
        return true;
   
    } // chiude function ViewLayer_INT(id)

  //CONTROLLO DATE CONTIGUE
	function chkDateContigue_INT(id)
	{
	//	alert('INT - chkDateContigue_INT. - id ='+id);
		var returnchkDC = true;

		var rigaprima = false;
		var riga = false;

		elem=id*NumDate;
		for (var j=0; j<NumDate; j++, elem++)
		{
			if(j==0)
			{
				var gg0 = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[elem].value;

				if(gg0 != '')
				{
					riga = true;
				}
			}
			else
			{
				var gg0 = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[elem].value;
				var gg0rigaprima = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[elem-1].value;
				if(gg0 != '')
				{
					riga = true;
				}
				else
				{
					riga = false
				}

				if(gg0rigaprima != '')
				{
					rigaprima = true;
				}
				else
				{
					rigaprima = false
				}

				if(rigaprima == false && riga == true)
				{
					returnchkDC = false;
				}
			}
		}
	//	alert(" FINE - CONTROLLO DATE CONTIGUE");
		return returnchkDC;
	
	}	//FINE - CONTROLLO DATE CONTIGUE

//  ---------------------- FINE -----------------------
	// STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
    	//alert("Verify: inizio");
	      var retValue = false;
	      var lEsiti=document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
	      
	        //Verifica sulle date
	        retValue = VerificaDate();
	        if (retValue)
	        	retValue = false;
	        else
	            return false;

	     	retValue = VerifyCombo(lEsiti,"Esito");		// Verifica che ci siano tutti gli esiti
	     	if (retValue)
	     	{						
	     		//alert("Verify: inizio 2");
	     		if (ComboConcedeRevoca())
		        {
	     			 var codOgg = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>.value;	
		        
			     // concessione REVOCA L.A. 
					 if(document.getElementById("giornidiLA"))
					 {
						 
						 GiorniConcessi = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.value;
						 SalvaGiorniConcessi = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA %>.value;
					 }
					 
					 if( codOgg == "0028" || codOgg == "2135")
					 {
						 if( GiorniConcessi == 0 || GiorniConcessi%45 != 0) 
			         	{
			               alert("Il totale Giorni deve essere Maggiore di ZERO e multiplo di 45! ");
			               document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus();
			               return false;
			         	}
						 
						flagConcesso = 'S';
			       		document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO%>.value = "S";
					 }
			         // concessione REVOCA L.A. SPECIALE
					 if(document.getElementById("giorni_SPE"))
					 {
						 GiorniConcessi_spe = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value;
						 SalvaGiorniConcessi_spe = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE %>.value;
					 }
					 
			         if(codOgg == "0620" || codOgg == "2136") 
			         {
			        	 if(GiorniConcessi_spe == 0 || GiorniConcessi_spe%75 != 0)
			        	 {	 
			                alert("Il totale Giorni deve essere Maggiore di ZERO e multiplo di 75! ");
			                document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.focus();
			                return false;
			        	 }
	            			flagConcesso_spe = 'S';
	            			document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO%>.value = "S";
			        	 
			         }
			         
			         // concessione L.A. INTEGRAZIONE
					 if(document.getElementById("giorni_INT"))
					 {
						 GiorniConcessi_int = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value;
						 SalvaGiorniConcessi_int = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT %>.value;
					 }
					 
			         if( codOgg == "0621" || codOgg == "2137")  
			         {
			        	  if(GiorniConcessi_int == 0 || GiorniConcessi_int%30 != 0)
			        	  {	  
				                alert("Il totale Giorni deve essere Maggiore di ZERO e multiplo di 30! ");
				                document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.focus();
				                return false;
			        	  } 
	            			flagConcesso_int = 'S';
	            			document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO%>.value = "S";
			        	  
			         }
			         
		              if (GiorniConcessi == 0 && GiorniConcessi_spe == 0 && GiorniConcessi_int == 0) // valido sia nel caso di LA che LA Speciale che Integrazione
		              {
			              if (SalvaGiorniConcessi == 0 && SalvaGiorniConcessi_spe == 0 && SalvaGiorniConcessi_int == 0) // valido sia nel caso di LA che LA Speciale che Integrazione
			              {
				                alert("Valorizzare periodi e giorni da concedere! ");
				                return false;
			              } 
		              }

		        } // Chiude if(ComboConcedeRevpca) 
	     		
	        // STUB 16/07/2008 Nel caso di Rigetto (Non Revoca), Incompetenza, NDP/NLP, Inammissibilità,  va controllato che siano = 0 i gg Concessi.
			//	alert("Verify: inizio 3");
	        	if (ComboRigetta() || ComboIncompetenza() || ComboNDPNLP() || ComboInammissibile() )
		        {
	        		//	alert("Verify: inizio 4");
			        	retValue = Chiusura();
		        		// ?????
			        	if (retValue)
			        	{
			          		retValue = InseritoPeriodo();
			          		// ?????
			        	}
	
	            		if (typeof (document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
						{	// Un solo Oggetto/Esitp L.A. presente
		        		
				          	if(document.getElementById("giornidiLA"))
							{
				          		document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>.value = "0";
								GiorniConcessi = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.value;
								
								if ( (document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0028" ||
					          		 document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2135" ) && GiorniConcessi != "0" )
						        {
						              alert("Azzerare i giorni da concedere! ");
						              document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.focus();
						              return false;
					          	}
							}
	
							 if(document.getElementById("giorni_SPE"))
							 {
								 document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE %>.value = "0";
								 GiorniConcessi_spe = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value;

					          	 if ( (document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0620" ||
						          	  document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2136" ) && GiorniConcessi_spe != "0" )

						         {
							          alert("Azzerare i giorni da concedere! ");
							          document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.focus();
							          return false;
						         }
							 }		
				          	
							 if(document.getElementById("giorni_INT"))
							 {
								 document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT%>.value = "0";
								 GiorniConcessi_int = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value;
					          	 
					          	 if ( (document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "0621" ||
						          		document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>.value == "2137") && GiorniConcessi_int != "0" )
							     { 
							              alert("Azzerare i giorni da concedere! ");
							              document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.focus();
							              return false;
						         }
							 }
						}
	            		else
	            		{	// Più Oggetti L.A. presenti
                			var indsel = document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
                			
                			if(document.getElementById("giornidiLA"))
							{
				          		document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>.value = "0";
								GiorniConcessi = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.value;
								
				          		if ( (document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "0028" ||
				          			 document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "2135" ) && GiorniConcessi != "0" )
					          	{
						              alert("Azzerare i giorni da concedere! ");
						              document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus();
						              return false;
					          	}
							}
	
							 if(document.getElementById("giorni_SPE"))
							 {
								 document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE %>.value = "0";
								 GiorniConcessi_spe = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value;
								 
					          	 if ( (document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "0620" ||
					          		  document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "2136" ) && GiorniConcessi_spe != "0" )
						         {
							              alert("Azzerare i giorni da concedere! ");
							              document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.focus();
							              return false;
						         }
							 }		
				          	
							 if(document.getElementById("giorni_INT"))
							 {
								 document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT%>.value = "0";
								 GiorniConcessi_int = document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value;
								 
					          	 if ( (document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "0621" ||
					          			document.ModOrdinanzaRevocaLA.<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>[indsel].value == "2137") && GiorniConcessi_int != "0" )
						         {
							              alert("Azzerare i giorni da concedere! ");
							              document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.focus();
							              return false;
						         }
							 }
	            		}	

		        } // chiude if (ComboRigetta() || ComboIncompetenza() || ... o 

	        // Nel caso di Incompetenza non vengono inseriti periodi e
	        // viene inserito il Magistrato Competente
		        if (ComboIncompetenza())
		        {
			          checkImcompetenza = 1;
			          init();
			          return true;
		        }
		        else
		         	document.ModOrdinanzaRevocaLA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>.value = "";
        
	        	retValue = Chiusura();
		        if (retValue)
		        {
			          retValue = InseritoPeriodo();
			          if(!retValue)
			            //30/06/2006 alert("Nessun periodo inserito !");
			          {
				       		if (! confirm("Nessun periodo inserito ! Si vuole continuare ?" ) )
				            	return false;
				            else
							{
				            	if(document.getElementById("giornidiLA"))
				            	{
				               		DisabilitaDate();
				            	}
				             		
				            	if(document.getElementById("giorni_SPE"))
				            	{
				               		DisabilitaDate_SPE();
				            	}
				                		
				            	if(document.getElementById("giorni_INT"))
				            	{
				            		DisabilitaDate_INT();
				            	}	
				                		
									    return true;
							}
			          }
			          else
			          {	  
			        	  	if(document.getElementById("giornidiLA"))
	            		  	{	
	                    		DisabilitaDate();
	            		  	}
	                		
	                		if(document.getElementById("giorni_SPE"))
	            			{
	                    		DisabilitaDate_SPE();
	            			}
	                		
	                		if(document.getElementById("giorni_INT"))
	            			{
	                			DisabilitaDate_INT();
	            			}
			          }	
			          
		        }	// // 	chiude if (retValue)
		        
	      } // 	chiude if (retValue)
	
	  //    alert("Verify: fine "+retValue);
	      return retValue;
   }

//  Controllo 	ComboIncompetenza
    function ComboIncompetenza()
    {
      //alert("ComboIncompetenza : inizio");
      
	      var ritorno = false;
	      if (typeof (document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
	      {
	    	  	if(document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0046" ||
	    	  		document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1243")
	       	 	{	
	       			ritorno = true;
	       	 	}
	      }
	      else
	      {
	    	  	var indj = document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
	       	 	if(document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "0046" ||
	       	 		document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "1243" )
	       	 	{	
	       			ritorno = true;
	       	 	}
	      }
      
      	return ritorno;
    }
    
 //  Controllo Concessione Revovca
    function ComboConcedeRevoca()
    {
    	// alert ("ComboConcedeRevoca - inizio");
	      var ritorno = false;
	      
	      if (typeof (document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
	      {	
	    	  	if (document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0043" ||
	    	  		document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1240" )
		      	{ 
		          	ritorno = true;
		        }
	      }
	      else
	      {		
		        var indiceSelezionato = document.ModOrdinanzaRevocaLA.<%=ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO%>.value;
		        if (document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indiceSelezionato].value == "0043" ||
		        	document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indiceSelezionato].value == "1240" )
				{ 
				      ritorno = true;
				}
	      }
	      
	      return ritorno;
    }

 	// controlla ComboRigetta
     function ComboRigetta()
    {
    	//alert("ComboRigetta");
	      var ritorno = false;
	      if (typeof (document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
	      {
	    	  if(document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0047" ||
	    		  document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1241" )
	     	  {	
	     			ritorno = true;
	     	  } 
	      }
	      else
	      {
	    	  	var indj = document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
	       	 	if(document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "0047" ||
	       	 		document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "1241")
	       	 	{	
	       			ritorno = true;
	       	 	}
	      }
	      return ritorno;
    }
    
    //  Controllo ComboNDPNLP.
    function ComboNDPNLP()
    {
    	//alert("ComboNDPNLP");
	      var ritorno = false;
	      if (typeof (document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
	      {
	    	  	if(document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0045" ||
	    	  		document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1242")
	     	 	{	
	     			ritorno = true;
	     	 	}
	      }
	      else
	      {
	    	  	var indj = document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
	     	 	if(document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "0045" ||
	     	 		document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "1242")
	     	 	{	
	     			ritorno = true;
	     	 	}
	      }
	      return ritorno;
    }
    
    //  Controllo Inammissibilità.
    function ComboInammissibile()
    {
    	//alert("ComboInammissibile");
	      var ritorno = false;
	      if (typeof (document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) =="undefined" )
	      {
	    	  	if(document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "0044" ||
	    	  		document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value == "1244" )
		 	 	{	
		 			ritorno = true;
		 	 	}
	      }
	      else
	      {
	    	  	var indj = document.ModOrdinanzaRevocaLA.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
		   	 	if(document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "0044" ||
		   	 		document.ModOrdinanzaRevocaLA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[indj].value == "1244" )
		   	 	{	
		   			ritorno = true;
		   	 	}
	      }
	      return ritorno;
    }

    // Funzione di controllo per determinare se è stato inserito almeno un periodo.
    function InseritoPeriodo()
    {
    //	alert("InseritoPeriodo ");
        var i=0;
        var ok=0;
        for (i=0; i < NumCheck; i++)
        {
	         // Vengono disabilitati tutti i blocchi periodi vuoti
	         if(document.getElementById("giornidiLA"))
			 {
		          if(IsCheckedDate(i))
		          {
		        	  ok = 1;
		             //return true;
		          }
			 }
	         
	         if(document.getElementById("giorni_SPE"))
			 {
		          if(IsCheckedDate_SPE(i))
		          {
		        	  ok = 1;
		             //return true;
		          }
			 }
	         
	         if(document.getElementById("giorni_INT"))
			 {
		          if(IsCheckedDate_INT(i))
		          {
		        	  ok = 1;
		             //return true;
		          }
			 }
	         
        }
        
        if(ok == 1)
       	 	return true;
       	else 
        	return false;
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
        var data_emissione = document.ModOrdinanzaRevocaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModOrdinanzaRevocaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModOrdinanzaRevocaLA.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
 
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

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ModOrdinanzaRevocaLA">
 
 <%       // Inizio nuova parte introdotta 20/4/2009 -
  		//		+ modifiche per Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014	%>
  		
<!-- 				PARTE PER INSERIRE LA DESCRIZIONE TOTALE DEI PERIODI CONCESSI E NON, UGUALE A QUELLA DEL DETTAGLIO   -->
<%  		
  int[] numDT  = {0,0,0,0,0,0};
  int[] nDT = {0,0,0,0,0,0};
  String[] titoloDT = new String[6];
  String[] codDT = {"C","C","R","I","N","S"};

  Iterator itxDT = LicenzePeriodi.iterator();
  // Conteggio delle licenze distinte per tipo
  while (itxDT.hasNext())
  {
      LicenzaPeriodiLibAnticipataModel lLicPerDT = (LicenzaPeriodiLibAnticipataModel) itxDT.next();
      if( lLicPerDT.getLicenza().getFlagConcesso().compareTo("C") == 0) 
      {
    	    if (lLicPerDT.getLicenza().getFlagScorta() != null && lLicPerDT.getLicenza().getFlagScorta().compareTo("C") == 0)
    	    {	  
          		numDT[0]++;
          	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          	//	siesLogger.debug("--> A totali Dettaglio - numDT[0] = "+numDT[0] ); 
    	    }    
    	  	else if(lLicPerDT.getLicenza().getFlagScorta() != null && lLicPerDT.getLicenza().getFlagScorta().compareTo("S") == 0) 
    	  	{	
    	  		numDT[1]++;
    	  	}

      }
      else if ( lLicPerDT.getLicenza().getFlagConcesso().compareTo("R") == 0)
          numDT[2]++;
      else if ( lLicPerDT.getLicenza().getFlagConcesso().compareTo("I")  == 0)
          numDT[3]++;
      else if ( lLicPerDT.getLicenza().getFlagConcesso().compareTo("N") == 0)
          numDT[4]++;
      else if ( lLicPerDT.getLicenza().getFlagConcesso().compareTo("S") == 0) 
          numDT[5]++;
  }

  
// ------  
  titoloDT[0] = "Periodi concessi: " + numDT[0];
  titoloDT[1] = "Semestri concessi: " + numDT[1];
  titoloDT[2] = "Periodi Rigettati (non Revocati): " + numDT[2];
  titoloDT[3] = "Periodi Inammissibili: " + numDT[3];
  titoloDT[4] = "Periodi N.L.P./N.D.P.: " + numDT[4];
  titoloDT[5] = "Periodi Scomputati: " + numDT[5];
  
  if (numDT[5] == 0) 
	  titoloDT[5] = "";
// - - -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-

 // I 'PERIODI CONCESSI' (titolo[0] + num[0];) SONO CALCOLATI FUORI CICLO - Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014
 // N.B. PER LA REVOCA NON CI SONO PERIODI CONCESSI,( getFlagConcesso().compareTo("C") == 0  ) 
 if(numDT[0] > 0)
  {
 %>
	    <table cellspacing="2" cellpadding="2">
	    	<tr> <td> <br></td></tr>
	    	<tr>
	        <td class="Titolo" colspan=6> <%=titoloDT[0]%></td>
	    	</tr>
	    </table>
<%	
		Iterator itxDT1 = LicenzePeriodi.iterator();
		while (itxDT1.hasNext())
		{
			LicenzaPeriodiLibAnticipataModel lLicPerConcDT1 = (LicenzaPeriodiLibAnticipataModel) itxDT1.next();
    		if( lLicPerConcDT1.getLicenza().getFlagConcesso().compareTo("C") == 0 )
    		{	
 	    			if(lLicPerConcDT1.getLicenza().getFlagScorta() != null && 
	    				lLicPerConcDT1.getLicenza().getFlagScorta().compareTo("C") == 0 )
	    			{
			      		if(lLicPerConcDT1 != null && lLicPerConcDT1.getPeriodi() != null )
			        	{ %>
			        	
							<table cellspacing="2" cellpadding="2">
		<%
			      			String tipoDT="";
			      			if(lLicPerConcDT1.getLicenza().getDescrStatoPermesso() != null)
			      			{
				      				if(lLicPerConcDT1.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LS") )
					        				tipoDT = "REVOCA Liberazione Anticipata Speciale";
					        		else if(lLicPerConcDT1.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LI") )
					        					tipoDT = "REVOCA Integrazione Liberazione Anticipata";	
					        		else 
					        					tipoDT = "REVOCA Liberazione Anticipata";
			      			}
				      			
						    nDT[0]++;
						    PeriodoLibAnticipataModel[] ppDT1 = lLicPerConcDT1.getPeriodi();

						    if( lLicPerConcDT1.getLicenza().getFlagConcesso().compareTo("C") == 0 )
						    {	
					%>	
						            	<tr>
						            		<td class="L"><font class="l"><%= tipoDT %>&nbsp; <%= nDT[0] %>)<br>
						            			</font>
						            		</td>							
				   <%		}
								        
				   			for (int i = 0; i < ppDT1.length; i++)
						    {
							%>
							           <td class="L">
							           <font class="l">
						              		<%=DateUtils.getDateToString(ppDT1[i].getDataInizio(),"dd/MM/yyyy")%>-
						              		<%=DateUtils.getDateToString(ppDT1[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;
							          </font>
							          </td>
							<%
						     }
							%>
						      </tr>
		<%
			        	} // chiude if(lLicPerConc != nul
	    			
   				}	// chiude if(lLicPerConc.getLicenza().getFlagScorta() != null && 

	   		} 	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0 )
		
		}	// chiude while
%>
		</table>
<%			

  }  // chiude if(num[0] > 0)
  
// - -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	 

//  CICLO FOR: PARTO DA 'SEMESTRI CONCESSI' (titolo[1] + num[1]) e ARRIVO  
// 				FINO A  'PERIODI NON CONCESSI N.L.P./N.D.P' (titolo[4] + num[4]) 
//		Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014

	 // N.B. PER LA REVOCA NON CI SONO SEMESTRI CONCESSI,( getFlagConcesso().compareTo("C") == 0  ) 

  for (int k= 1; k < 5; k++)
  {
%>
	    <table cellspacing="2" cellpadding="2">
	    <tr> <td> <br></td></tr>
	    <tr>
	        <td class="Titolo" colspan=6> <%=titoloDT[k]%><td>
	    </tr>
	    </table>
   
<%  	if (numDT[k] > 0)
	    {
      		Iterator itxCDT = LicenzePeriodi.iterator();
%>
	  		<table cellspacing="2" cellpadding="2">
<%
      		while (itxCDT.hasNext())
      		{
       			LicenzaPeriodiLibAnticipataModel lLicPerConcCDT = (LicenzaPeriodiLibAnticipataModel) itxCDT.next();
        		if( lLicPerConcCDT.getLicenza().getFlagConcesso().compareTo(codDT[k]) == 0)
        		{
		      		if( lLicPerConcCDT.getLicenza() != null && 
			      		lLicPerConcCDT.getLicenza().getFlagScorta() != null && 
			      		lLicPerConcCDT.getLicenza().getFlagScorta().compareTo("C") != 0 )
			        {
			      			if( lLicPerConcCDT != null && lLicPerConcCDT.getPeriodi() != null )
			      			{
					      			String tipoCDT="";
					      			if(lLicPerConcCDT.getLicenza().getDescrStatoPermesso() != null)
					      			{
						      				if(lLicPerConcCDT.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LS") )
							        				tipoCDT = "Revoca Liberazione Anticipata Speciale";
							        		else if(lLicPerConcCDT.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LI") )
							        					tipoCDT = "Revoca Integrazione Liberazione Anticipata";	
							        		else 
							        					tipoCDT = "Revoca Liberazione Anticipata";
					      			}        			

					   				nDT[k]++;
			           				PeriodoLibAnticipataModel[] pCDT = lLicPerConcCDT.getPeriodi();
%>
							            	<tr><td class="L"><font class="l"> <%= tipoCDT%>&nbsp;<%=nDT[k]%>)<br>
							            	</font> </td>
<%
						           for (int i = 0; i < pCDT.length; i++)
						           {
			%>
							           <td class="L">
							           <font class="l">
							              <%=DateUtils.getDateToString(pCDT[i].getDataInizio(),"dd/MM/yyyy")%>-
							              <%=DateUtils.getDateToString(pCDT[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;
							          </font>
					          		  </td>
			<%
			           			 	}
			%>
 				            		</tr>
				<%
				
			      			} // chiude if(lLicPerConc != nul
			      			
			         } // chiude if(lLicPerConc.getLicenza() != null && 
	
				}	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().com
      	
      		}	// chiude while (itxC.hasNext())
%>
         	</table>

<%  	}  // chiude if (num[k] > 0)
 
  }	// chiude for (int k= 1; k < 5; k++)

	  
// Aggiunta per modifica Ordinanza REVOCA LA - 19/10/2009 :
//		in mezzo ci sono le aggiunte per la Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014  
  
  if (numDT[5] > 0) 
  { %>
	    <table cellspacing="2" cellpadding="2">
	    <tr> <td> <br></td></tr>
	    <tr>
	        <td class="Titolo" colspan=6> <%=titoloDT[5]%><td>
	    </tr>
	    </table>
	<%	  
		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("--> C Modifica REVOCA - numDT[5] > 0 =  "+numDT[5] ); 
  		Iterator itxC5 = LicenzePeriodi.iterator();
	    while (itxC5.hasNext())
        {
	       LicenzaPeriodiLibAnticipataModel lLicPerConc5 = (LicenzaPeriodiLibAnticipataModel) itxC5.next();
	       if(lLicPerConc5.getLicenza().getFlagConcesso().compareTo(codDT[5]) == 0)
	       {
	    	   // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    	   siesLogger.debug("--> C Modifica REVOCA - FlagConcesso = S"); 
	    	    if(lLicPerConc5 != null && lLicPerConc5.getPeriodi() != null )
	        	{ %>
						<table cellspacing="2" cellpadding="2">
<%	
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.debug("--> C Modifica REVOCA - Periodi presenti"); 
						String tipoRe="";
						if(lLicPerConc5.getLicenza().getDescrStatoPermesso() != null)
						{
								if(lLicPerConc5.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LS") )
									tipoRe = "Revoca Liberazione Anticipata Speciale";
							else if(lLicPerConc5.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LI") )
									tipoRe = "Revoca Integrazione Liberazione Anticipata";	
							else 
									tipoRe = "Revoca Liberazione Anticipata";
						}
			          	nDT[5]++;
			          	PeriodoLibAnticipataModel[] psc5 = lLicPerConc5.getPeriodi();
	%>
			           	<tr><td class="L"><font class="l"> <%=tipoRe%>&nbsp;<%=nDT[5]%>)<br>
			           	</font> </td>
	<%
			          	for (int i = 0; i < psc5.length; i++)
			          	{
			%>
						      <td class="L">
						      <font class="l">
						           <%=DateUtils.getDateToString(psc5[i].getDataInizio(),"dd/MM/yyyy")%>-
						           <%=DateUtils.getDateToString(psc5[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;
						       </font>
						       </td>
			<%
			          	}
	%>
	           			</tr>
<%	        	}
			} 	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().compareTo(cod[5]) == 0)
     
       }	// chiude while (itxC.hasNext())
%>
  		</table>  	   
<%  
  }		// chiude if (num[5] > 0)  
%> 
 
<!-- 	FINE DELLA PARTE DESCRIZIONE TOTALE PERIODI  --> 
<%
//
//		OGNI OGGETTO REVOCA L.A. VIENE ESAMINATO
//
%> 
<!-- 	INIZIO PARTE CONTEGGI SPECIFICI DI REVOCA ORDINANZA L.A. -->  
<% 	      
  		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  		siesLogger.debug("--> C Inizio Modifica - REVOCA LA "); 
  		
  int[] numLA  = {0,0,0,0,0,0};
  int[] nLA = {0,0,0,0,0,0};
  String[] codLA = {"C","C","R","I","N","S"};
  int sceltaSemestriLA = 1;
  int plamLA_i = 0;
  int plamLA_j = 0;

  // PeriodoLibAnticipataModel[][] plam_arrayLA = new PeriodoLibAnticipataModel[NumCheck][NumDate];
  for (int jCheck=0; jCheck<NumCheck; jCheck++)
  {
	  plam_arrayLA[jCheck] = null;
	  periodiLA[jCheck] = 0;
  }
  //PeriodoLibAnticipataModel[][] plam_arrayLA = null;
  
  Iterator itx = LicenzePeriodi.iterator();
  // Conteggio delle licenze distinte per tipo
  while (itx.hasNext())
  {
      LicenzaPeriodiLibAnticipataModel lLicPer = (LicenzaPeriodiLibAnticipataModel) itx.next();
      if(lLicPer.getLicenza().getDescrStatoPermesso() != null)
      {
    	  if(lLicPer.getLicenza().getDescrStatoPermesso().substring(0, 2).compareTo("LA") == 0 )
    	  {	  
		      if( lLicPer.getLicenza().getFlagConcesso().compareTo("C") == 0) 
		      {
		    	    if (lLicPer.getLicenza().getFlagScorta() != null && lLicPer.getLicenza().getFlagScorta().compareTo("C") == 0)
		    	    {	  
		          		numLA[0]++;
		                plamLA_i = NumTotaleSemestri;
		                sceltaSemestriLA = 0;
		            // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		            //    siesLogger.debug("--> C Modifica  - plamLA_i = "+plamLA_i); 
		            // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		            //    siesLogger.debug("--> C Modifica  - periodi numLA[0] = "+numLA[0]); 
		    	    }    
		    	  	else if(lLicPer.getLicenza().getFlagScorta() != null && lLicPer.getLicenza().getFlagScorta().compareTo("S") == 0) 
		    	  	{	
		    	  		numLA[1]++;
		    	  	}
		
		      }
		      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("R") == 0)
		          numLA[2]++;
		      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("I")  == 0)
		          numLA[3]++;
		      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("N") == 0)
		          numLA[4]++;
		      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("S") == 0) 
		      {
		          numLA[5]++;
		          if (lLicPer.getLicenza().getFlagScorta() != null && lLicPer.getLicenza().getFlagScorta().compareTo("C") == 0)
		          {
		               plamLA_i = NumTotaleSemestri;
		               sceltaSemestriLA = 0;
		          }
		      }
    	  }     
      }
      else
      {
    	  
      }
  }

 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 // siesLogger.debug("--> Cpasso la conta dei giorniLA - plamLA_i = "+plamLA_i); 
  
// - - -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-

 // I 'PERIODI CONCESSI' (titolo[0] + num[0];) SONO CALCOLATI FUORI CICLO - Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014
  if(numLA[0] > 0)
  {
 		Iterator itxP = LicenzePeriodi.iterator();
		while (itxP.hasNext())
		{
			  LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxP.next();
		      if(lLicPerConc.getLicenza().getDescrStatoPermesso() != null)
		      {
		    	  if(lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0, 2).compareTo("LA") == 0 )
		    	  {				
			    		if( lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0 )
			    		{	
			 	    			if(lLicPerConc.getLicenza().getFlagScorta() != null && 
				    				lLicPerConc.getLicenza().getFlagScorta().compareTo("C") == 0 )
				    			{
						      		if(lLicPerConc != null && lLicPerConc.getPeriodi() != null )
						        	{  
									    nLA[0]++;
									    PeriodoLibAnticipataModel[] pp = lLicPerConc.getPeriodi();
									    
									    plam_arrayLA[plamLA_i] = lLicPerConc.getPeriodi();
									// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
									//    siesLogger.debug("--> C k=0 - numro periodi CONCESSI : " + pp.length +" - plamLA_i = "+plamLA_i); 
				          			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				          			//	siesLogger.debug("--> C k=0 - plam_arrayLA[plamLA_i] = "+plam_arrayLA[plamLA_i] );
			
						        	} // chiude if(lLicPerConc != nul
				    			
			   				}	// chiude if(lLicPerConc.getLicenza().getFlagScorta() != null && 
							
			   				if(plamLA_i < NumTotaleSemestri)    // era k == 0
							{
									plamLA_i++;
							}
			
		    			} 	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0 )

		    	  }
		    	  
		      }		    	  
		
		 }	// chiude while
			
		 if (plamLA_i < NumTotaleSemestri)
		 {
		       	plamLA_i = NumTotaleSemestri + 1;
		    // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		    //   	siesLogger.debug("--> C k=0 -LA 2 - plamLA_i = "+plamLA_i); 
		 }
		 else
		 {
				plamLA_i++;
			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			//	siesLogger.debug("--> C k=0 -LA 3 - plamLA_i = "+plamLA_i); 
		 }			

  }  // chiude if(numLA[0] > 0)
  
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//  siesLogger.debug("--> C Passo ciclo k=0 LA- plamLA_i = "+plamLA_i); 
  
// - -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	 

//  CICLO FOR: PARTO DA 'SEMESTRI CONCESSI' (titolo[1] + num[1]) e ARRIVO  
// 				FINO A  'PERIODI NON CONCESSI N.L.P./N.D.P' (titolo[4] + num[4]) 
//		Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014

  for (int k= 1; k < 5; k++)
  {
	  	if (numLA[k] > 0)
	    {
      		Iterator itxC = LicenzePeriodi.iterator();
      		while (itxC.hasNext())
      		{
       			LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxC.next();
  		      	if(lLicPerConc.getLicenza().getDescrStatoPermesso() != null)
  		      	{
	  		    	  if(lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0, 2).compareTo("LA") == 0 )
	  		    	  {	      			
			        		if( lLicPerConc.getLicenza().getFlagConcesso().compareTo(codLA[k]) == 0)
			        		{
					      		if( lLicPerConc.getLicenza() != null && 
						      		lLicPerConc.getLicenza().getFlagScorta() != null && 
						      		lLicPerConc.getLicenza().getFlagScorta().compareTo("C") != 0 )
						        {
						      			if( lLicPerConc != null && lLicPerConc.getPeriodi() != null )
						      			{

								   				nLA[k]++;
						           				PeriodoLibAnticipataModel[] p = lLicPerConc.getPeriodi();
						           				
						           				plam_arrayLA[plamLA_i] = lLicPerConc.getPeriodi();		// si salva i periodi che verranno dettagliati poi nella form
			
						          			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						          			//	siesLogger.debug("-------------> C periodi = " + p.length +" - plamLA_i = "+plamLA_i); 
						          			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						          			//	siesLogger.debug("-------------> C plam_arrayLA[plamLA_i] = "+plam_arrayLA[plamLA_i] );
							
						      			} // chiude if(lLicPerConc != nul
						      			
						         } // chiude if(lLicPerConc.getLicenza() != null && 
						         
						         if  ((k == 1) && (plamLA_i < NumTotaleSemestri))    // era k == 0
								 {
										plamLA_i++;
								 }
				
							}	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().com
									
	  		    	  }
  		      	}	  
      	
      		}	// chiude while (itxC.hasNext())
      			
		    if ((k == 1) && (plamLA_i < NumTotaleSemestri))
		    {
		       	plamLA_i = NumTotaleSemestri + 1;
		       // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		       //	siesLogger.debug("--> C - K=1/5 1 -LA plamLA_i = semes+1 = "+plamLA_i); 
		    }
		    else
			{
				plamLA_i++;
			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			//	siesLogger.debug("--> C - K=1/5 2 -LA plamLA_i = "+plamLA_i); 
			}

	  	}  // chiude if (num[k] > 0)
		else
		{						// metto (if k == 1) per avere i periodi NON CONCESSI
			if (k == 1)
	      	{
				plamLA_i = NumTotaleSemestri + 1;
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				//siesLogger.debug("--> C- K=1/5 3 -LA plamLA_i = semes+1 = "+plamLA_i); 
	      	}
			else
			{
		   		plamLA_i++;
		   		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		   		//siesLogger.debug("--> C - K=1/5 4 -LA plam_i = "+plamLA_i); 
			}
		}	
 
  }	// chiude for (int k= 1; k < 5; k++)

	  
// Aggiunta per modifica Ordinanza REVOCA LA - 19/10/2009 :
//		in mezzo ci sono le aggiunte per la Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014  
  
  if (numLA[5] > 0) 
  { 
  		Iterator itxC = LicenzePeriodi.iterator();
  		int plamS = 0;
	   while (itxC.hasNext())
       {
	       LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxC.next();
	       if( lLicPerConc.getLicenza().getFlagConcesso().compareTo(codLA[5]) == 0)
	       {
	    		if( lLicPerConc != null && lLicPerConc.getPeriodi() != null )
     			{
			          nLA[5]++;
			          PeriodoLibAnticipataModel[] psc = lLicPerConc.getPeriodi();
			          
			          if (lLicPerConc.getLicenza().getFlagScorta() != null && lLicPerConc.getLicenza().getFlagScorta().compareTo("C") == 0)
			          		plam_arrayLA[NumTotaleSemestri] = lLicPerConc.getPeriodi();
			          else
			          {
			        	  plam_arrayLA[plamS] = lLicPerConc.getPeriodi();
			          	  plamS++;
		          	  }
     			}
			} 	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().compareTo(cod[5]) == 0)
       }	// chiude while (itxC.hasNext())
  }		// chiude if (num[5] > 0) 
 
// Fine Aggiunta	-	-	-	-	-	>	Fine nuova parte introdotta 20/4/2009         %>

 <!-- 	FINE PARTE CONTEGGI SPECIFICI DI REVOCA ORDINANZA L.A. -->  
 
 <!-- -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	- -->
 
 <!-- 	INIZIO PARTE CONTEGGI SPECIFICI DI REVOCA ORDINANZA L.A. SPECIALE ( L.A.S.) -->  
<% 	      
  		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  		siesLogger.debug("-------> C Inizio Modifica REVOCA LS - TotggLS = "+TotggLS ); 
  		
  int[] numLS  = {0,0,0,0,0,0};
  int[] nLS = {0,0,0,0,0,0};
  String[] codLS = {"C","C","R","I","N","S"};
  int sceltaSemestriLS = 1;
  int plamLS_i = 0;
  int plamLS_j = 0;

  // PeriodoLibAnticipataModel[][] plam_arrayLS = new PeriodoLibAnticipataModel[NumCheck][NumDate];
  for (int jCheck=0; jCheck<NumCheck; jCheck++)
  {
	  plam_arrayLS[jCheck] = null;
	  periodiLS[jCheck] = 0;
  }
  
   itx = LicenzePeriodi.iterator();
  // Conteggio delle licenze distinte per tipo
  while (itx.hasNext())
  {
      LicenzaPeriodiLibAnticipataModel lLicPer = (LicenzaPeriodiLibAnticipataModel) itx.next();
      if(lLicPer.getLicenza().getDescrStatoPermesso() != null)
      {
    	  if(lLicPer.getLicenza().getDescrStatoPermesso().substring(0, 2).compareTo("LS") == 0 )
    	  {	  
		      if( lLicPer.getLicenza().getFlagConcesso().compareTo("C") == 0) 
		      {
		    	    if (lLicPer.getLicenza().getFlagScorta() != null && lLicPer.getLicenza().getFlagScorta().compareTo("C") == 0)
		    	    {	  
		          		numLS[0]++;
		                plamLS_i = NumTotaleSemestri;
		                sceltaSemestriLS = 0;
		              // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		              //  siesLogger.debug("--> C ModificaLS  - plamLS_i = "+plamLS_i); 
		              // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		              //  siesLogger.debug("--> C ModificaLS  - periodi numLS[0] = "+numLS[0]); 
		    	    }    
		    	  	else if(lLicPer.getLicenza().getFlagScorta() != null && lLicPer.getLicenza().getFlagScorta().compareTo("S") == 0) 
		    	  	{	
		    	  		numLS[1]++;
		    	  	}
		
		      }
		      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("R") == 0)
		          numLS[2]++;
		      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("I")  == 0)
		          numLS[3]++;
		      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("N") == 0)
		          numLS[4]++;
		      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("S") == 0) 
		      {
		          numLS[5]++;
		          // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		          siesLogger.debug("-------> C  REVOCA sco ls  - numLS[5] = "+numLS[5] ); 
		          if (lLicPer.getLicenza().getFlagScorta() != null && lLicPer.getLicenza().getFlagScorta().compareTo("C") == 0)
		          {
		               plamLS_i = NumTotaleSemestri;
		               sceltaSemestriLS = 0;
		               // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		               siesLogger.debug("-------> C  REVOCA sco ls  fLAGsCORTA = C - plamLS_i = "+plamLS_i ); 
		          }
		      }
    	  }     
      }
      else
      {
    	  
      }
  }

 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 // siesLogger.debug("--> C LS passo la conta dei giorniLS - plamLS_i = "+plamLS_i); 
  
// - - -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-

 // I 'PERIODI CONCESSI' (titolo[0] + num[0];) SONO CALCOLATI FUORI CICLO - Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014
  if(numLS[0] > 0)
  {
 		Iterator itxP1 = LicenzePeriodi.iterator();
		while (itxP1.hasNext())
		{
			  LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxP1.next();
		      if(lLicPerConc.getLicenza().getDescrStatoPermesso() != null)
		      {
		    	  if(lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0, 2).compareTo("LS") == 0 )
		    	  {				
			    		if( lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0 )
			    		{	
			 	    			if(lLicPerConc.getLicenza().getFlagScorta() != null && 
				    				lLicPerConc.getLicenza().getFlagScorta().compareTo("C") == 0 )
				    			{
						      		if(lLicPerConc != null && lLicPerConc.getPeriodi() != null )
						        	{  
									    nLS[0]++;
									    PeriodoLibAnticipataModel[] pp = lLicPerConc.getPeriodi();
									    
									    plam_arrayLS[plamLS_i] = lLicPerConc.getPeriodi();
									 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
									 //   siesLogger.debug("--> C LS k=0 - numro periodi CONCESSI : " + pp.length +" - plamLS_i = "+plamLS_i); 
				          			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				          			//	siesLogger.debug("--> C LS k=0 - plam_arrayLS[plamLS_i] = "+plam_arrayLS[plamLS_i] );
			
						        	} // chiude if(lLicPerConc != nul
				    			
			   				}	// chiude if(lLicPerConc.getLicenza().getFlagScorta() != null && 
							
			   				if(plamLS_i < NumTotaleSemestri)    
							{
									plamLS_i++;
							}
			
		    			} 	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0 )

		    	  }
		    	  
		      }		    	  
		
		 }	// chiude while
			
		 if (plamLS_i < NumTotaleSemestri)
		 {
		       	plamLS_i = NumTotaleSemestri + 1;
		     // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		     //  	siesLogger.debug("--> C k=0 -LS 2 - plamLS_i = "+plamLS_i); 
		 }
		 else
		 {
				plamLS_i++;
			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			//	siesLogger.debug("--> C k=0 -LS 3 - plamLS_i = "+plamLS_i); 
		 }			

  }  // chiude if(numLS[0] > 0)
  
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//  siesLogger.debug("--> C Passo ciclo k=0 LS- plamLS_i = "+plamLS_i); 
  
// - -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	 

//  CICLO FOR: PARTO DA 'SEMESTRI CONCESSI' (titolo[1] + num[1]) e ARRIVO  
// 				FINO A  'PERIODI NON CONCESSI N.L.P./N.D.P' (titolo[4] + num[4]) 
//		Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014

  for (int k= 1; k < 5; k++)
  {
	  	if (numLS[k] > 0)
	    {
      		Iterator itxC1 = LicenzePeriodi.iterator();
      		while (itxC1.hasNext())
      		{
       			LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxC1.next();
  		      	if(lLicPerConc.getLicenza().getDescrStatoPermesso() != null)
  		      	{
	  		    	  if(lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0, 2).compareTo("LS") == 0 )
	  		    	  {	      			
			        		if( lLicPerConc.getLicenza().getFlagConcesso().compareTo(codLS[k]) == 0)
			        		{
					      		if( lLicPerConc.getLicenza() != null && 
						      		lLicPerConc.getLicenza().getFlagScorta() != null && 
						      		lLicPerConc.getLicenza().getFlagScorta().compareTo("C") != 0 )
						        {
						      			if( lLicPerConc != null && lLicPerConc.getPeriodi() != null )
						      			{

								   				nLS[k]++;
						           				PeriodoLibAnticipataModel[] p = lLicPerConc.getPeriodi();
						           				
						           				plam_arrayLS[plamLS_i] = lLicPerConc.getPeriodi();		// si salva i periodi che verranno dettagliati poi nella form
			
						          			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						          			//	siesLogger.debug("-------------> C LS periodi = " + p.length +" - plamLS_i = "+plamLS_i); 
						          			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						          			//	siesLogger.debug("-------------> C LS plam_arrayLS[plamLS_i] = "+plam_arrayLS[plamLS_i] );
							
						      			} // chiude if(lLicPerConc != nul
						      			
						         } // chiude if(lLicPerConc.getLicenza() != null && 
						         
						         if  ((k == 1) && (plamLS_i < NumTotaleSemestri)) 
								 {
										plamLS_i++;
								 }
				
							}	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().com
									
	  		    	  }
  		      	}	  
      	
      		}	// chiude while (itxC.hasNext())
      			
		    if ((k == 1) && (plamLS_i < NumTotaleSemestri))
		    {
		       	plamLS_i = NumTotaleSemestri + 1;
		     // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		     //  	siesLogger.debug("--> C - K=1/5 1 -LS plamLS_i = semes+1 = "+plamLS_i); 
		    }
		    else
			{
				plamLS_i++;
			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			//	siesLogger.debug("--> C - K=1/5 2 -LS plamLS_i = "+plamLS_i); 
			}

	  	}  // chiude if (numLS[k] > 0)
		else
		{						// metto (if k == 1) per avere i periodi NON CONCESSI
			if (k == 1)
	      	{
				plamLS_i = NumTotaleSemestri + 1;
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				//siesLogger.debug("--> C- K=1/5 3 -LS plamLS_i = semes+1 = "+plamLS_i); 
	      	}
			else
			{
		   		plamLS_i++;
		   	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		   	//	siesLogger.debug("--> C - K=1/5 4 -LS plamLS_i = "+plamLS_i); 
			}
		}	
 
  }	// chiude for (int k= 1; k < 5; k++)


// Aggiunta per modifica Ordinanza REVOCA LS - 19/10/2009 :
//		in mezzo ci sono le aggiunte per la Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014  
  
  if (numLS[5] > 0) 
  { 
	   // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug("--> MODIFICA REVOCA LA - scomputo LAS - numLS[5] > 0 = "+numLS[5]); 
  		Iterator itxC2 = LicenzePeriodi.iterator();
  		int plamS = 0;
	   while (itxC2.hasNext())
       {
	       LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxC2.next();
	       if( lLicPerConc.getLicenza().getFlagConcesso().compareTo(codLS[5]) == 0)
	       {
	    	    if( lLicPerConc != null && lLicPerConc.getPeriodi() != null )
     			{
			          nLS[5]++;
			          PeriodoLibAnticipataModel[] psc = lLicPerConc.getPeriodi();
			          
			          if (lLicPerConc.getLicenza().getFlagScorta() != null && lLicPerConc.getLicenza().getFlagScorta().compareTo("C") == 0)
			          {	 
			        	    // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			        	    siesLogger.debug("-->  scomputo LAS - FlagScorta = C ");  
			          		plam_arrayLS[NumTotaleSemestri] = lLicPerConc.getPeriodi();
			          		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			          		siesLogger.debug("-->  scomputo LAS - plam_arrayLS[NumTotaleSemestri] "); 
			          		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			          		siesLogger.debug("-->  scomputo LAS - [NumTotaleSemestri] = "+NumTotaleSemestri); 
			          		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			          		siesLogger.debug("-->  scomputo LAS - plam_arrayLS = "+plam_arrayLS[NumTotaleSemestri]); 
			          }		
			          else
			          {
			        	  plam_arrayLS[plamS] = lLicPerConc.getPeriodi();
			          	  plamS++;
		          	  }
     			}
			} 	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().compareTo(codLS[5]) == 0)
       }	// chiude while (itxC.hasNext())
  }		// chiude if (numLS[5] > 0) 
 
// Fine Aggiunta	-	-	-	-	-	>	Fine nuova parte introdotta 20/4/2009         %>

 <!-- 	FINE PARTE CONTEGGI SPECIFICI DI REVOCA ORDINANZA L.A. SPECIALE (L.A.S) -->  	  

 <!-- 	INIZIO PARTE CONTEGGI SPECIFICI DI REVOCA ORDINANZA L.A.INTEGRAZIONE (L.A.I.) -->  
<% 	      
  		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  		siesLogger.debug("--> C Inizio Modifica  LI -" ); 
  		
  int[] numLI  = {0,0,0,0,0,0};
  int[] nLI = {0,0,0,0,0,0};
  String[] codLI = {"C","C","R","I","N","S"};
  int sceltaSemestriLI = 1;
  int plamLI_i = 0;
  int plamLI_j = 0;

  // PeriodoLibAnticipataModel[][] plam_arrayLI = new PeriodoLibAnticipataModel[NumCheck][NumDate];
  for (int jCheck=0; jCheck<NumCheck; jCheck++)
  {
	  plam_arrayLI[jCheck] = null;
	  periodiLI[jCheck] = 0;
  }
  
  Iterator itx11 = LicenzePeriodi.iterator();
  // Conteggio delle licenze distinte per tipo
  while (itx11.hasNext())
  {
      LicenzaPeriodiLibAnticipataModel lLicPer = (LicenzaPeriodiLibAnticipataModel) itx11.next();
      if(lLicPer.getLicenza().getDescrStatoPermesso() != null)
      {
    	  if(lLicPer.getLicenza().getDescrStatoPermesso().substring(0, 2).compareTo("LI") == 0 )
    	  {	  
		      if( lLicPer.getLicenza().getFlagConcesso().compareTo("C") == 0) 
		      {
		    	    if (lLicPer.getLicenza().getFlagScorta() != null && lLicPer.getLicenza().getFlagScorta().compareTo("C") == 0)
		    	    {	  
		          		numLI[0]++;
		                plamLI_i = NumTotaleSemestri;
		                sceltaSemestriLI = 0;
		              // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		              //  siesLogger.debug("--> C Modifica LI  - plamLI_i = "+plamLI_i); 
		              // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		              //  siesLogger.debug("--> C Modifica LI  - periodi numLI[0] = "+numLI[0]); 
		    	    }    
		    	  	else if(lLicPer.getLicenza().getFlagScorta() != null && lLicPer.getLicenza().getFlagScorta().compareTo("S") == 0) 
		    	  	{	
		    	  		numLI[1]++;
		    	  	}
		
		      }
		      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("R") == 0)
		      {	  
		          numLI[2]++;
		         // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		         // siesLogger.debug("--> C Modifica LI  - periodi numLI[2] = "+numLI[2]); 
		      }    
		      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("I")  == 0)
		          numLI[3]++;
		      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("N") == 0)
		          numLI[4]++;
		      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("S") == 0) 
		      {
		          numLI[5]++;
		          if (lLicPer.getLicenza().getFlagScorta() != null && lLicPer.getLicenza().getFlagScorta().compareTo("C") == 0)
		          {
		               plamLI_i = NumTotaleSemestri;
		               sceltaSemestriLI = 0;
		          }
		      }
    	  }     
      }
      else
      {
    	  
      }
  }

 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 // siesLogger.debug("--> C LI passo la conta dei giorniLI - plamLI_i = "+plamLI_i); 
  
// - - -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-

 // I 'PERIODI CONCESSI' (titolo[0] + num[0];) SONO CALCOLATI FUORI CICLO - Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014
  if(numLI[0] > 0)
  {
 		Iterator itxP11 = LicenzePeriodi.iterator();
		while (itxP11.hasNext())
		{
			  LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxP11.next();
		      if(lLicPerConc.getLicenza().getDescrStatoPermesso() != null)
		      {
		    	  if(lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0, 2).compareTo("LI") == 0 )
		    	  {				
			    		if( lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0 )
			    		{	
			 	    			if(lLicPerConc.getLicenza().getFlagScorta() != null && 
				    				lLicPerConc.getLicenza().getFlagScorta().compareTo("C") == 0 )
				    			{
						      		if(lLicPerConc != null && lLicPerConc.getPeriodi() != null )
						        	{  
									    nLI[0]++;
									    PeriodoLibAnticipataModel[] pp = lLicPerConc.getPeriodi();
									    
									    plam_arrayLI[plamLI_i] = lLicPerConc.getPeriodi();
									 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
									 //   siesLogger.debug("--> C LI k=0 - numro periodi CONCESSI : " + pp.length +" - plamLI_i = "+plamLI_i ); 
				          			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				          			//	siesLogger.debug("--> C LI k=0 - plam_arrayLI[plamLI_i] = "+plam_arrayLI[plamLI_i] );
			
						        	} // chiude if(lLicPerConc != nul
				    			
			   				}	// chiude if(lLicPerConc.getLicenza().getFlagScorta() != null && 
							
			   				if(plamLI_i < NumTotaleSemestri)    
							{
									plamLI_i++;
							}
			
		    			} 	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0 )

		    	  }
		    	  
		      }		    	  
		
		 }	// chiude while
			
		 if (plamLI_i < NumTotaleSemestri)
		 {
		       	plamLI_i = NumTotaleSemestri + 1;
		 }
		 else
		 {
				plamLI_i++;
		 }			

  }  // chiude if(numLI[0] > 0)
  
// - -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	 

//  CICLO FOR: PARTO DA 'SEMESTRI CONCESSI' (titolo[1] + num[1]) e ARRIVO  
// 				FINO A  'PERIODI NON CONCESSI N.L.P./N.D.P' (titolo[4] + num[4]) 
//		Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014

  for (int k= 1; k < 5; k++)
  {
	  	if (numLI[k] > 0)
	    {
	  		Iterator itxC111 = LicenzePeriodi.iterator();
      		while (itxC111.hasNext())
      		{
       			LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxC111.next();
  		      	if(lLicPerConc.getLicenza().getDescrStatoPermesso() != null)
  		      	{
	  		    	  if(lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0, 2).compareTo("LI") == 0 )
	  		    	  {	      			
			        		if( lLicPerConc.getLicenza().getFlagConcesso().compareTo(codLI[k]) == 0)
			        		{
					      		if( lLicPerConc.getLicenza() != null && 
						      		lLicPerConc.getLicenza().getFlagScorta() != null && 
						      		lLicPerConc.getLicenza().getFlagScorta().compareTo("C") != 0 )
						        {
						      			if( lLicPerConc != null && lLicPerConc.getPeriodi() != null )
						      			{

								   				nLI[k]++;
						           				PeriodoLibAnticipataModel[] p = lLicPerConc.getPeriodi();
						           				
						           				plam_arrayLI[plamLI_i] = lLicPerConc.getPeriodi();		// si salva i periodi che verranno dettagliati poi nella form
			
						          			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						          			//	siesLogger.debug("-------------> C LI periodi = " + p.length +" - plamLI_i = "+plamLI_i); 
						          			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						          			//	siesLogger.debug("-------------> C LI plam_arrayLI[plamLI_i] = "+plam_arrayLI[plamLI_i] );
							
						      			} // chiude if(lLicPerConc != nul
						      			
						         } // chiude if(lLicPerConc.getLicenza() != null && 
						         
						         if  ((k == 1) && (plamLI_i < NumTotaleSemestri)) 
								 {
										plamLI_i++;
								 }
				
							}	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().com
									
	  		    	  }
  		      	}	  
      	
      		}	// chiude while (itxC.hasNext())
      			
		    if ((k == 1) && (plamLI_i < NumTotaleSemestri))
		    {
		       	plamLI_i = NumTotaleSemestri + 1;
		    }
		    else
			{
				plamLI_i++;
			}

	  	}  // chiude if (numLI[k] > 0)
		else
		{						// metto (if k == 1) per saltare i periodi NON CONCESSI
			if (k == 1)
	      	{
				plamLI_i = NumTotaleSemestri + 1;
	      	}
			else
			{
		   		plamLI_i++;
			}
		}	
 
  }	// chiude for (int k= 1; k < 5; k++)


// Aggiunta per modifica Ordinanza RECLAMA LA - 19/10/2009 :
//		in mezzo ci sono le aggiunte per la Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014  
  
  if (numLI[5] > 0) 
  { 
	  Iterator itxC22 = LicenzePeriodi.iterator();
  		int plamS = 0;
	   while (itxC22.hasNext())
       {
	       LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxC22.next();
	       if( lLicPerConc.getLicenza().getFlagConcesso().compareTo(codLI[5]) == 0)
	       {
	    	    if( lLicPerConc != null && lLicPerConc.getPeriodi() != null )
     			{
			          nLI[5]++;
			          PeriodoLibAnticipataModel[] psc = lLicPerConc.getPeriodi();
			          
			          if (lLicPerConc.getLicenza().getFlagScorta() != null && lLicPerConc.getLicenza().getFlagScorta().compareTo("C") == 0)
			          		plam_arrayLI[NumTotaleSemestri] = lLicPerConc.getPeriodi();
			          else
			          {
			        	  plam_arrayLI[plamS] = lLicPerConc.getPeriodi();
			          	  plamS++;
		          	  }
     			}			          
 
			} 	// chiude if( lLicPerConc.getLicenza().getFlagConcesso().compareTo(codLS[5]) == 0)
     
       }	// chiude while (itxC.hasNext())
  
  }		// chiude if (numLS[5] > 0) 
 
// Fine Aggiunta	-	-	-	-	-	>	Fine nuova parte introdotta 20/4/2009         %>

 <!-- 	FINE PARTE CONTEGGI SPECIFICI DI REVOCA ORDINANZA L.A. INTEGRAZIONE (L.A.I) -->  	  


<%  // Nuova Ordinanza L.A. del Decreto Legge 2013/46	- 20/03/2014 
	// Ciclo per totalizzare i gg di L.A, L.A. SPECIALE, e L.A. INTEGRAZIONE e 
	// 		 per  totalizzare i periodi e i semestri di L.A, L.A. SPECIALE, e L.A. INTEGRAZIONE
	//		 per totalizzare i le concessioni e i rigetti (inamm, NLP, scomputo)
	
	Iterator itxGG = LicenzePeriodi.iterator();
	while (itxGG.hasNext())
	{
	    LicenzaPeriodiLibAnticipataModel lLicConc = (LicenzaPeriodiLibAnticipataModel) itxGG.next();
		if( lLicConc.getLicenza().getFlagConcesso().compareTo("C") == 0)	// accoglimento REVOCA	*/
		{	    
		    if(lLicConc.getLicenza().getDescrStatoPermesso() != null)
			{
		    	if( lLicConc.getLicenza().getDescrStatoPermesso().substring(0,2).compareTo("LA") == 0)	/* L.A. NORMALE	*/
	    		{	    
					TotggLA += lLicConc.getLicenza().getNumeroGiorni().intValue();				/* TOT gg L.A. NORMALE concessi	*/
				    if(lLicConc.getLicenza().getFlagScorta().compareTo("C") == 0)				/* periodo Unico concesso	*/
				    {
				    	if(lLicConc.getPeriodi() != null)
				    	{	
				    		perUnicoConcLA_dalal = true;					/* periodo Unico con Periodi	*/
				    	}
				    	else							/* oppure	*/
				    	{
				    		perUnicoConcLA_sologg = true;						/* periodo Unico con soli gg SENZA Periodi	*/
				    	}
				    	
				    }
				    else if(lLicConc.getLicenza().getFlagScorta().compareTo("S") == 0)			/* Semestri concessi	*/
				    {
				    	semestriLA++;											/* TOT check semestri	*/
				    }
	    			
			 	}
		    	else if( lLicConc.getLicenza().getDescrStatoPermesso().substring(0,2).compareTo("LS") == 0)		/* L.A. SPECIALE	*/
			    {
		    		TotggLS += lLicConc.getLicenza().getNumeroGiorni().intValue();					/* TOT gg L.A. SPECIALE	*/
				   	if(lLicConc.getLicenza().getFlagScorta().compareTo("C") == 0)					/* periodo Unico concesso di L.A.S.	*/
				   	{
				   		if(lLicConc.getPeriodi() != null)
				   		{	
				   			perUnicoConcLS_dalal = true;						/* periodo Unico con Periodi di L.A.S.	*/
				   		}
				   		else							/* oppure	*/
				   		{
				   			perUnicoConcLS_sologg = true;						/* periodo Unico con soli gg SENZA Periodi di L.A.S.	*/
				   		}
				   	
				   	}
				   	else if(lLicConc.getLicenza().getFlagScorta().compareTo("S") == 0)			/* Semestri concessi di L.A.S.	*/
				    {
				    		semestriLS++;											/* TOT check semestri di L.A.S.	*/
				    }
			    }
		    	else if( lLicConc.getLicenza().getDescrStatoPermesso().substring(0,2).compareTo("LI") == 0)		/* L.A. INTEGRAZIONE	*/
			    {
		    		TotggLI += lLicConc.getLicenza().getNumeroGiorni().intValue();					/* TOT gg L.A. INTEGRAZIONE		*/
				   	if(lLicConc.getLicenza().getFlagScorta().compareTo("C") == 0)					/* periodo Unico concesso di L.A.I.	*/
				   	{
				   		if(lLicConc.getPeriodi() != null)
				   		{	
				   			perUnicoConcLI_dalal = true;						/* periodo Unico con Periodi	di L.A.I.	*/
				   		}
				   		else									/* oppure	*/
				   		{
				   			perUnicoConcLI_sologg = true;						/* periodo Unico con soli gg SENZA Periodi di L.A.I.	*/
				   		}
				    	
				   	}
				    else if(lLicConc.getLicenza().getFlagScorta().compareTo("S") == 0)			/* Semestri concessi	di L.A.I.	*/
				    {
				    	semestriLI++;											/* TOT check semestri	di L.A.I.	*/
				    }
			    } //
	    		
		    }
		    else		/* DescStatoPermesso = null - Concessione Vecchia Ordinanza L.A.	*/
		    {
			       if (datiOrdinanza.getOrdinanza().getNumGiorniLibanticipata() != null ) 
			    		TotggLAold = datiOrdinanza.getOrdinanza().getNumGiorniLibanticipata().intValue();
		    }
		    
		}	
		else if(lLicConc.getLicenza().getFlagConcesso().compareTo("S") == 0)	// Accoglie REVOCA con SCOMPUTO 
		{
			if(lLicConc.getLicenza().getDescrStatoPermesso() != null)
			{
		    	if( lLicConc.getLicenza().getDescrStatoPermesso().substring(0,2).compareTo("LA") == 0)	/* L.A. NORMALE	*/
	    		{	    
					TotggLA += lLicConc.getLicenza().getNumeroGiorni().intValue();				/* TOT gg da Scomputare L.A. NORMALE  */
				    if(lLicConc.getLicenza().getFlagScorta().compareTo("C") == 0)				/* periodo Unico concesso	*/
				    {
				    	if(lLicConc.getPeriodi() != null)
				    	{	
				    		perUnicoConcLA_dalal = true;					/* periodo Unico con Periodi da Scomputare	*/
				    	}
				    	else							/* oppure	*/
				    	{
				    		perUnicoConcLA_sologg = true;						/* periodo Unico con soli gg SENZA Periodi da Scomputare	*/
				    	}
				    	
				    }
				    else if(lLicConc.getLicenza().getFlagScorta().compareTo("S") == 0)			/* Semestri concessi	*/
				    {
				    	semestriLA++;											/* TOT check semestri da Scomputare	*/
				    }
	    			
			 	}
		    	else if( lLicConc.getLicenza().getDescrStatoPermesso().substring(0,2).compareTo("LS") == 0)		/* L.A. SPECIALE	*/
			    {
		    		TotggLS += lLicConc.getLicenza().getNumeroGiorni().intValue();					/* TOT gg da Scomputare L.A. SPECIALE	*/
				   	if(lLicConc.getLicenza().getFlagScorta().compareTo("C") == 0)					/* periodo Unico concesso di L.A.S.	*/
				   	{
				   		if(lLicConc.getPeriodi() != null)
				   		{	
				   			perUnicoConcLS_dalal = true;						/* periodo Unico con Periodi di L.A.S. da Scomputare */
				   		}
				   		else							/* oppure	*/
				   		{
				   			perUnicoConcLS_sologg = true;						/* periodo Unico con soli gg SENZA Periodi di L.A.S. da Scomputare	*/
				   		}
				   	
				   	}
				   	else if(lLicConc.getLicenza().getFlagScorta().compareTo("S") == 0)			/* Semestri concessi di L.A.S.	*/
				    {
				    		semestriLS++;											/* TOT check semestri di L.A.S. da Scomputare	*/
				    }
			    }
		    	else if( lLicConc.getLicenza().getDescrStatoPermesso().substring(0,2).compareTo("LI") == 0)		/* L.A. INTEGRAZIONE	*/
			    {
		    		TotggLI += lLicConc.getLicenza().getNumeroGiorni().intValue();					/* TOT gg da Scomputare L.A. INTEGRAZIONE		*/
				   	if(lLicConc.getLicenza().getFlagScorta().compareTo("C") == 0)					/* periodo Unico concesso di L.A.I.	*/
				   	{
				   		if(lLicConc.getPeriodi() != null)
				   		{	
				   			perUnicoConcLI_dalal = true;						/* periodo Unico con Periodi di L.A.I. da Scomputare	*/
				   		}
				   		else									/* oppure	*/
				   		{
				   			perUnicoConcLI_sologg = true;						/* periodo Unico con soli gg SENZA Periodi di L.A.I. da Scomputare	*/
				   		}
				    	
				   	}
				    else if(lLicConc.getLicenza().getFlagScorta().compareTo("S") == 0)			/* Semestri concessi	di L.A.I.	*/
				    {
				    	semestriLI++;											/* TOT check semestri di L.A.I. da Scomputare	*/
				    }
			    } //
	    		
		    }
		    else		/* DescStatoPermesso = null - Concessione Vecchia Ordinanza L.A.	*/
		    {
			       if (datiOrdinanza.getOrdinanza().getNumGiorniLibanticipata() != null ) 
			    		TotggLAold = datiOrdinanza.getOrdinanza().getNumGiorniLibanticipata().intValue();
		    }	
		}  // chiude else if( lLicConc.getLicenza().getFlagConcesso().compareTo("S") == 0)
			
	}	/* chiude while	*/
	
	/*  
	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("--> C periodoSemestriLA = "+semestriLA ); 
	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("--> C perUnicoConcLA_dalal = "+perUnicoConcLA_dalal );
	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("--> C perUnicoConcLA_sologg = "+perUnicoConcLA_sologg ); 
	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("--> C TotggLA = "+TotggLA ); 
	 */ 
//  -	-	-	LS
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//	  siesLogger.debug("--> C periodoSemestriLS = "+semestriLS ); 
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//	  siesLogger.debug("--> C REVOCA perUnicoConcLS_dalal = "+perUnicoConcLS_dalal );
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//	  siesLogger.debug("--> C REVOCA perUnicoConcLS_sologg = "+perUnicoConcLS_sologg ); 
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//	  siesLogger.debug("--> C TotggLS = "+TotggLS ); 
%>
 
<!-- 		
				---------------	S E P A R A Z I O N E   D E G L I   I N D I C I  -------------- 
					 -->  
					 


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
    <table cellspacing="2" cellpadding="2"   width=95%>
    	<tr>
        	<td class="Titolo" colspan=2 width=50%> Oggetto </td>
        	<td class="l" colspan=2 width="20%"> Seleziona </td>
        	<td class="Titolo" colspan=2 width=50%> specificare esito per ciascuno oggetto: </td>
    	</tr>
    <%
   for (int i=0; i< lTenori.length;i++)
   {
    %>
	       <tr>
	        <td class="l"  colspan=2 width="25%">
	           <input Title="Oggetto" 					  name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" 	value="<%=lTenori[i].getDescrOggettoTenore()%>"  readonly size=60%>
	           <input Title="ID Tenore" 	type="hidden" name="<%=ICostantiTenore.CAMPO_ID_TENORE %>"	 			value="<%=lTenori[i].getIdTenore().toString()%>" >
	           <input Title="Cod Oggetto" 	type="hidden" name="<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" 	value="<%=lTenori[i].getCodOggettoTenore()%>" >
	       </td>
	        
	        <td class ="l"colspan=2 width="20%">
	        	<input value="" type="radio" onclick="Javascript:return QualeRevocaSelected(<%=lTenori[i].getCodOggettoTenore()%>,<%=i %>);" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>" >&nbsp;&nbsp;&nbsp;  
	        </td>
	                
	        <td class="l" colspan=2>
	         <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>">
	             <%=esiti[i]%>
	         </select>
	        </td>
	      </tr>
    <%
   }
    %>
    </table>
    <br> 
    <%
   for (int indTeno = 0; indTeno < lTenori.length; indTeno++)
   {
	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  // siesLogger.debug(" ------------------------------------------------> INIZIO GIRO - indTeno = " + indTeno);
		if( lTenori[indTeno].getCodOggettoTenore().compareTo("0028") == 0 ||
			lTenori[indTeno].getCodOggettoTenore().compareTo("2135") == 0 )
		{ 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("tenore oggetto 0028 L.A. normale " );
		%>
		
	<!--  	> > >  		INIZIO PARTE CON LE DIV PER 	REVOCA LIBERAZIONE ANTICIPATA normale ( L.A.)		  	< < < 	-->	
		
			<div id="tipoconcessioneLA" style="position: relative; top: 0; left: 0;" >  
		   		<table>
		      		<tr>
		        		<td class="l"> Modalità di scelta dei periodi di Revoca </td>
		      		</tr>
					<tr>
				<% if(perUnicoConcLA_dalal == true || perUnicoConcLA_sologg == true )
					{ 
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						//siesLogger.debug(" L.A. unico - numLA[0] -> " + numLA[0]); 	%>	
				         	<td> <input value="S" onclick="Javascript:return AbilitaSemestri();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>"  > per semestri &nbsp;
				            <input value="C" onclick="Javascript:return AbilitaPeriodo();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>" CHECKED > unico periodo</td>
				<%   }
					 else
					 {	
						 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						 //siesLogger.debug(" NO PERIODO - L.A. semestr - numLA[1] -> " + numLA[1]); %> 
				      		<td> <input value="S" onclick="Javascript:return AbilitaSemestri();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>"  CHECKED > per semestri &nbsp;
				           	<input value="C" onclick="Javascript:return AbilitaPeriodo();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>" > unico periodo</td>
				<%	 } %>
				      </tr>
				      <tr> <td>&nbsp;</td> </tr>
				</table>
				
			</div>	<!--  chiude DIV id="tipoconcessioneLA" -->
			
			<div id="comune" style="position: relative; top: 0; left: 0;" >
			
			  <%if( semestriLA > 0) 
			  	{%>
			  		<div id="semestri" style="position: relative; top: 0; left: 0; " >
			  <%}
			    else  
			    { %>
			        <div id="semestri" style="position: relative; top: 0; left: 0; display:none; ">
	       <%   }%>
	       
			    <table cellspacing="2" cellpadding="2" width=26%>
			    	<tr>
			        	<td class="Titolo" colspan=6> Semestri Revocati:&nbsp;&nbsp;&nbsp; </td>
			    	</tr>
			    </table>
			    
			    <table width=26%>
  <%		   
 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 /* siesLogger.debug("--> C - L.A. - Period num[0] = "+numLA[0] ); 
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C - L.A. - semest num[1] = "+numLA[1] );
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C - L.A. - Rigett num[2] = "+numLA[2] );
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C - L.A. - Inammi num[3] = "+numLA[3] );
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C - L.A. - NLP    num[4] = "+numLA[4] );
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C - L.A. - Scompu num[5] = "+numLA[5] ); 
 */ 
  				for (int ii = 0; ii <NumRighe; ii++)
			    {    
			    	%>
			    	<tr>
  	<%
			      	for (int j=0;j<NumColonne;j++)
			      	{	
			      		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			      		//siesLogger.debug("--> C int j = "+j ); 
			      		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			      		//siesLogger.debug("--> C int i*NumColonne+j = "+(i*NumColonne+j) ); 
			      		
					//	if ((((i*NumColonne+j) < num[0]) || ((i*NumColonne+j) < num[4])) && (semestriLA > 0) ) 
						if ((((ii*NumColonne+j) < numLA[1]) || ((ii*NumColonne+j) < numLA[5])) && (semestriLA > 0) ) 
			      		{ 			
			      				
			      			%>
			      			<td width=3%><span id="SL<%=ii*NumColonne+j%>" style="color=red;font-weight:bold;">Giorni 45</span> <input type=checkbox name=gg value=2 onclick="Javascript:ViewLayer('<%=ii*NumColonne+j%>');"></td>
			<%     		}
			       		else 
			       		{ 	%>
			       			<td width=3%><span id="SL<%=ii*NumColonne+j%>" style="color=navy;font-weight:bold;">Giorni 45</span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=ii*NumColonne+j%>');"></td>
			<%     		}
						
			    	} 	%>
			    	
			        </tr>
			        
	     <%		}		%>
			     </table>
			     
			</div>	<!--  chiude DIV = "semestri" -->
						
			  <%if( semestriLA > 0) 
			  	{
				  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  	//siesLogger.debug("--> C Div periododo NONE  = "+numLA[0] ); 	%>
			  		<div id="periodo" style="position: relative; top: 0; left: 0; display:none; " >
			  <%}
			    else  
			    { 
			    	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			    	//siesLogger.debug("--> C Div periododo DISPLAY  = "+numLA[0] );  %>
			    	<div id="periodo" style="position: relative; top: 0; left: 0; " >
 <%			    }	%>
 
			    	<table cellspacing="2" cellpadding="2" width=26%>
			      		<tr>
			        		<td class="Titolo" colspan=6> Periodo Revocato:&nbsp;&nbsp;&nbsp; </td>
			      		</tr>
			    	</table>
			    		
			    	<table width=26%>
			      		<tr>
			<%	//	if ( ((num[0] > 0) || (num[4] > 0)) && (semestriLA == 0) ) // Rimane num[0] perchè trttasi di Periodo 
					if ( ((numLA[0] > 0) || (numLA[5] > 0)) && (semestriLA == 0) && (perUnicoConcLA_dalal == true) )
			      	{ %>
			       			<td width=3%><span id="SL<%=IndPer%>" style="color=red;font-weight:bold;">Periodo  </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndPer%>');"></td>
			<%      }
			        else
			        { %>
			       			<td width=3%><span id="SL<%=IndPer%>" style="color=navy;font-weight:bold;">Periodo  </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndPer%>');"></td>
			<%      } %>
			      		</tr>
			    	</table>
			    	
			  </div>	<!--  chiude DIV id="periodo" -->
			
			<%
			int jDate = 0;
			String iChecked = "";
			
			for ( int t = 0; t < NumCheck; t++)
			{
			
				iChecked = "";
				if (plam_arrayLA[t] != null)
				{
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				//	siesLogger.debug("--------------> C Plam_arrayLA[t] NON nullo -  ichecked = t = "+t ); 
					iChecked = "checked";
				}	
					
			%>
			<div id="L<%=t%>" style="position: relative; top: -120; left: 500; display:none;" >
			  <table>
			   <!-- COSTRUZIONE DEI CAMPI DATA DAL - AL -->
			   <% 
			   
			   // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			   //siesLogger.debug("--> C Prima del ciclo for per le date Dal Al - NumDate = "+NumDate+" t = "+t ); 
			    for (int k=0; k<NumDate; k++)
				{
			    	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			    	//siesLogger.debug("-----------------> t = "+t+" - k = "+k+" - plam_arrayLA[t]lengh = "+plam_arrayLA[t]);
				   %>
			    <tr>
			      <td class=l>
				  	Dal
				  	<% 	
				 
				 //  Per semesri: t < numLA[1]  -  per periodi: t < numLA[0]  -  per periodi scomputati: t == (IndPer) && numLA[5] > 0 
				 
				  	if ((((t < numLA[1]) || (t < numLA[5])) && (plam_arrayLA[t] != null) && (k < plam_arrayLA[t].length) )  ||
					 	( (t == (IndPer)) && (numLA[0] > 0 || numLA[5] > 0) && (plam_arrayLA[t] != null) && (k < plam_arrayLA[t].length) ) ||
					 	( (t == (IndRig)) && (numLA[2] > 0) && (plam_arrayLA[t] != null) && (k < plam_arrayLA[t].length) ) ||
					 	( (t == (IndIna)) && (numLA[3] > 0) && (plam_arrayLA[t] != null) && (k < plam_arrayLA[t].length) ) ||
					 	( (t == (IndNlp)) && (numLA[4] > 0) && (plam_arrayLA[t] != null) && (k < plam_arrayLA[t].length) ) )	
				 	{ 
				  		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  		// siesLogger.debug("--> C LA DENTRO for per le date Dal Al -  t  = "+t+"  k = "+k );
				  		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  		// siesLogger.debug("--> C LA DENTRO for per le date Dal Al - plam_arrayLA[t] = "+plam_arrayLA[0]);
				  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  	//	siesLogger.debug("--> C LA DENTRO for per le date Dal Al - plam_arrayLA[t]lengh = "+plam_arrayLA[t].length);
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
			      if (t < NumTotaleSemestri )
			     {
			     %>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (t == IndPer)
			     {%>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (t == (IndRig))
			     {%>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (t == ( IndIna) )
			     {%>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (t == ( IndNlp) )
			     {%>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP%>" value=1 <%=iChecked%> style="display:none;">
			  <% } %>
			  
			</div>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%--  chiude DIV id="L<%=i%>" --%>
		
		<%	 }%>
			
			    <table width=35%>
			    <tr>
			      <td class="l">Totale giorni Revocati </td>
			      <td class="l">
			        <input id="giornidiLA" Title="TotGiorni_LA" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>" value="" size=5  onFocus="javascript:rifiutaFocusSemestri()">
			      </td>
			    </tr>
			    </table>
			    
			  </div>	<!--  chiude DIV id="comune" -->
			
			<br>
			
			<div id="resto" style="position: relative; top: 0; left: 0;" >
			    <table cellspacing="2" cellpadding="2" width=35%>
			    <tr>
			        <td class="Titolo" colspan=6> Periodi non Revocati:&nbsp;&nbsp;&nbsp; </td>
			    </tr>
<%
			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			/*	siesLogger.debug("--> C - NumTotaleSemestri = "+NumTotaleSemestri ); 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C - IndRig = "+IndRig ); 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C - IndIna = "+IndIna ); 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C - IndNlp = "+IndNlp ); 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C - IndPer = "+IndPer ); 
			*/	
%>			    
			    </table>
			    <table width=35%>
			      <% if (numLA[(IndRig+1) - NumTotaleSemestri] > 0)  { %>
			      <td width=3%><span id="SL<%=IndRig%>" style="color=red;font-weight:bold;">Rigettati    </span> <input type=checkbox name=gg value=2 onclick="Javascript:ViewLayer('<%=IndRig%>');"></td>
			<%     }
			       else { %>
			       <td width=3%><span id="SL<%=IndRig%>" style="color=navy;font-weight:bold;">Rigettati    </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndRig%>');"></td>
			       <%     }  %> 
			      <% if (numLA[(IndIna+1) - NumTotaleSemestri] > 0)  { %>
			      <td width=3%><span id="SL<%=IndIna%>" style="color=red;font-weight:bold;">Inammissibili    </span> <input type=checkbox name=gg value=2 onclick="Javascript:ViewLayer('<%=IndIna%>');"></td>
			<%     }
			       else { %>
			       <td width=3%><span id="SL<%=IndIna%>" style="color=navy;font-weight:bold;">Inammissibili    </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndIna%>');"></td>
			       <%     }  %> 
			      <% if (numLA[(IndNlp+1) - NumTotaleSemestri] > 0)  { %>
			      <td width=3%><span id="SL<%=IndNlp%>" style="color=red;font-weight:bold;">N.L.P./N.D.P.  </span> <input type=checkbox name=gg value=2 onclick="Javascript:ViewLayer('<%=IndNlp%>');"></td>
			<%     }
			       else { %>
			       <td width=3%><span id="SL<%=IndNlp%>" style="color=navy;font-weight:bold;">N.L.P./N.D.P.  </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndNlp%>');"></td>
			       <%     }  %>     
			   </table>
			</div>		<!-- Chiude DIV id='resto' -->
<%		}
		else if(lTenori[indTeno].getCodOggettoTenore().compareTo("0620") == 0 || 
				lTenori[indTeno].getCodOggettoTenore().compareTo("2136") == 0 )
		{				
            // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.debug("tenore oggetto 0620 L.A. Speciale = " ); 	
%>
	<!--  	> > >  		INIZIO PARTE CON LE DIV PER 	REVOCA LIBERAZIONE ANTICIPATA SPECIALE ( L.A.S.)		  	< < < 	-->	

			<div id="tipoconcessione_SPE" style="position: relative; top: 0; left: 0;" >  
		   		<table>
		      		<tr>
		        		<td class="l"> Modalità di scelta dei periodi di Revoca </td>
		      		</tr>
					<tr>
				<% if(perUnicoConcLS_dalal == true || perUnicoConcLS_sologg == true )
					{ 
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						//	siesLogger.debug(" numLS[0] SPE periodo CHECKED -> - per = " + numLS[0]); 	%>	
				         	<td> <input value="S" onclick="Javascript:return AbilitaSemestri_SPE();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>"  > per semestri &nbsp;
				            <input value="C" onclick="Javascript:return AbilitaPeriodo_SPE();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>" CHECKED > unico periodo </td>
				<%   }
					 else 
					 {	
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						//	 siesLogger.debug(" NO PERIODO - numLS[1] SPE semestri CHECKED-> - sem = " + numLS[1]); %> 
				      		<td> <input value="S" onclick="Javascript:return AbilitaSemestri_SPE();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>"  CHECKED > per semestri &nbsp;
				           	<input value="C" onclick="Javascript:return AbilitaPeriodo_SPE();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>" > unico periodo </td>
				<%	 } %>					

				      </tr>
				      <tr> <td>&nbsp;</td> </tr>
				</table>
				
			</div>	<!--  chiude DIV id="tipoconcessione_SPE" -->
			
			<div id="comune_SPE" style="position: relative; top: 0; left: 0;" >
			
			  <%if( semestriLS > 0) 
			  	{
				  		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  		siesLogger.debug("--> C REVOCAsemestri_SPE - display_block  " );
			  		%>
			  		<div id="semestri_SPE" style="position: relative; top: 0; left: 0; " >
			  <%}
			    else  
			    { 
			    		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			    		siesLogger.debug("--> C REVOCAsemestri_SPE - display_none " );
			    	%>
			        <div id="semestri_SPE" style="position: relative; top: 0; left: 0; display:none; ">
	       <%   }%>
	       
			    <table cellspacing="2" cellpadding="2" width=26%>
			    	<tr>
			        	<td class="Titolo" colspan=6> Semestri Revocati:&nbsp;&nbsp;&nbsp; </td>
			    	</tr>
			    </table>
			    
			    <table width=26%>
  <%		   

// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
/*  siesLogger.debug("--> C REVOCA_SPE Period num[0] = "+numLS[0] ); 
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C REVOCA_SPE semest num[1] = "+numLS[1] );
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C REVOCA_SPE Rigett num[2] = "+numLS[2] );
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C REVOCA_SPE Inammi num[3] = "+numLS[3] );
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C REVOCA_SPE NLP    num[4] = "+numLS[4] );
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C REVOCA_SPE Scompu num[5] = "+numLS[5] ); 
*/ 
  				for (int r = 0; r <NumRighe; r++)
			    {    
			    	%>
			    	<tr>
  	<%
			      	for (int j=0;j<NumColonne;j++)
			      	{	
			      		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			      		//	siesLogger.debug("--> C _SPE int j = "+j ); 
			      		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			      		//	siesLogger.debug("--> C _SPE int r*NumColonne+j = "+(r*NumColonne+j) ); 

						if ((((r*NumColonne+j) < numLS[1]) || ((r*NumColonne+j) < numLS[5])) && (semestriLS > 0) ) 
			      		{ 			
			      				
			      			%>
			      			<td width=3%><span id="SL_SPE<%=r*NumColonne+j%>" style="color=red;font-weight:bold;">Giorni 75</span> <input type=checkbox name=gg_SPE value=2 onclick="Javascript:ViewLayer_SPE('<%=r*NumColonne+j%>');"></td>
			<%     		}
			       		else 
			       		{ 	%>
			       			<td width=3%><span id="SL_SPE<%=r*NumColonne+j%>" style="color=navy;font-weight:bold;">Giorni 75</span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=r*NumColonne+j%>');"></td>
			<%     		}
						
			    	} 	%>
			    	
			        </tr>
			        
	     <%		}		%>
			     </table>
			     
			</div>	<!--  chiude DIV = "semestri_SPE" -->
						
			  <%if( semestriLS > 0) 
			  	{
				  		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  		siesLogger.debug("--> CREVOCA periodo_SPE - display_none " );
			  		%>
			  		<div id="periodo_SPE" style="position: relative; top: 0; left: 0; display:none; " >
			  <%}
			    else  
			    { 
			    		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			    		siesLogger.debug("--> C REVOCAperiodo_SPE  - display -  " );
			    	%>
			    	<div id="periodo_SPE" style="position: relative; top: 0; left: 0; " >
 <%			    }	%>
 
			    	<table cellspacing="2" cellpadding="2" width=26%>
			      		<tr>
			        		<td class="Titolo" colspan=6> Periodo Revocato:&nbsp;&nbsp;&nbsp; </td>
			      		</tr>
			    	</table>
			    		
			    	<table width=26%>
			      		<tr>
			<%		if ( ((numLS[0] > 0) || (numLS[5] > 0)) && (semestriLS == 0) && (perUnicoConcLS_dalal == true) )
			      	{ %>
			       			<td width=3%><span id="SL_SPE<%=IndPer%>" style="color=red;font-weight:bold;">Periodo </span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndPer%>');"></td>
			<%      }
			        else
			        { %>
			       			<td width=3%><span id="SL_SPE<%=IndPer%>" style="color=navy;font-weight:bold;">Periodo </span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndPer%>');"></td>
			<%      } %>
			      		</tr>
			    	</table>
			    	
			  </div>	<!--  chiude DIV id="periodo_SPE" -->
			
			<%
			int jDate = 0;
			String iChecked = "";
			
			for ( int in=0; in<NumCheck; in++)
			{
			
				iChecked = "";
				if (plam_arrayLS[in] != null)
				{
					// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("--> C REVOCA_SPE - Plam_arrayLS[in] NON nullo  - ichecked = in = "+in ); 
					iChecked = "checked";
				}	
					
			%>
			<div id="L_SPE<%=in%>" style="position: relative; top: -120; left: 500; display:none;" >
			  <table>
			   <!-- COSTRUZIONE DEI CAMPI DATA DAL - AL -->
			   <% 
			   
			   // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			   siesLogger.debug("--> CREVOCA_SPE Prima del ciclo for per le date Dal Al - NumDate = "+NumDate+" in = "+in ); 
			    for (int k=0; k<NumDate; k++)
				{
				   %>
			    <tr>
			      <td class=l>
				  	Dal
				  	<% 	
				 //   //  Per semesri: t < numLS[1] - per periodi: t < numLS[0] - per periodi scomputati: t == (IndPer) && numLS[5] > 0 
						 
				  	if ((((in < numLS[1]) || (in < numLS[5])) && (plam_arrayLS[in] != null) && (k < plam_arrayLS[in].length) )  ||
					 	( (in == (IndPer)) && (numLS[0] > 0 || numLS[5] > 0) && (plam_arrayLS[in] != null) && (k < plam_arrayLS[in].length) ) ||
					 	( (in == (IndRig)) && (numLS[2] > 0) && (plam_arrayLS[in] != null) && (k < plam_arrayLS[in].length) ) ||
					 	( (in == (IndIna)) && (numLS[3] > 0) && (plam_arrayLS[in] != null) && (k < plam_arrayLS[in].length) ) ||
					 	( (in == (IndNlp)) && (numLS[4] > 0) && (plam_arrayLS[in] != null) && (k < plam_arrayLS[in].length) ) )	
				 	{ 
				  		 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  		 //	siesLogger.debug("--> C LS DENTRO for per le date Dal Al -  in  = "+in+"  k = "+k );
				  		 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  		 //	siesLogger.debug("--> C LS DENTRO for per le date Dal Al - plam_arrayLS[in] = "+plam_arrayLS[0]);
				  		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  		//	siesLogger.debug("--> C LS DENTRO for per le date Dal Al - plam_arrayLS[in]lengh = "+plam_arrayLS[in].length);
			%>
							<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE%>" maxlength="2" size="2" value="<%=DateUtils.getDateToString(plam_arrayLS[in][k].getDataInizio(),"dd")%>" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE%>" maxlength="2" size="2" value="<%=DateUtils.getDateToString(plam_arrayLS[in][k].getDataInizio(),"MM")%>" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE%>" maxlength="4" size="4" value="<%=DateUtils.getDateToString(plam_arrayLS[in][k].getDataInizio(),"yyyy")%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
					     	&nbsp;&nbsp;
					     	Al
							<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_SPE%>" maxlength="2" size="2" value="<%=DateUtils.getDateToString(plam_arrayLS[in][k].getDataFine(),"dd")%>" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_SPE%>" maxlength="2" size="2" value="<%=DateUtils.getDateToString(plam_arrayLS[in][k].getDataFine(),"MM")%>" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_SPE%>" maxlength="4" size="4" value="<%=DateUtils.getDateToString(plam_arrayLS[in][k].getDataFine(),"yyyy")%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
			      <% }
			         else
			         { %>
					       <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
					     	&nbsp;&nbsp;
					     	Al
							<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_SPE%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_SPE%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_SPE%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
			     <%  } %>
			     
			      </td>
			    </tr>
		  
		  <%	}%>
			 </table>
			     <%
			      if (in < NumTotaleSemestri )
			     {
			     %>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (in == IndPer)
			     {%>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (in == (IndRig))
			     {%>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (in == ( IndIna) )
			     {%>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (in == ( IndNlp) )
			     {%>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE%>" value=1 <%=iChecked%> style="display:none;">
			  <% } %>
			  
			</div>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%--  chiude DIV id="L_SPE<%=in%>" --%>
		
		<%	 }%>
			
			    <table width=35%>
			    <tr>
			      <td class="l">Totale Giorni Revocati</td>
			      <td class="l">
			        <input id="giorni_SPE" Title="TotGiorni_SPE" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>" value="" size=5  onFocus="javascript:rifiutaFocusSemestri_SPE()">
			      </td>
			    </tr>
			    </table>
			    
			  </div>	<!--  chiude DIV id="comune_SPE" -->
			
			<br>
			
			<div id="resto_SPE" style="position: relative; top: 0; left: 0;" >
			    <table cellspacing="2" cellpadding="2" width=35%>
			    <tr>
			        <td class="Titolo" colspan=6> Periodi Non Revocati:&nbsp;&nbsp;&nbsp; </td>
			    </tr>
<%
			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			/*	siesLogger.debug("--> C _SPE - NumTotaleSemestri = "+NumTotaleSemestri ); 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C _SPE - IndRig = "+IndRig ); 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C _SPE - IndIna = "+IndIna ); 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C _SPE - IndNlp = "+IndNlp ); 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C _SPE - IndPer = "+IndPer ); 
			*/	
%>			    
			    </table>
			    <table width=35%>
			      <% if (numLS[(IndRig+1) - NumTotaleSemestri] > 0)  { %>
			      <td width=3%><span id="SL_SPE<%=IndRig%>" style="color=red;font-weight:bold;">Rigettati </span> <input type=checkbox name=gg_SPE value=2 onclick="Javascript:ViewLayer_SPE('<%=IndRig%>');"></td>
			<%     }
			       else { %>
			       <td width=3%><span id="SL_SPE<%=IndRig%>" style="color=navy;font-weight:bold;">Rigettati </span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndRig%>');"></td>
			       <%     }  %> 
			      <% if (numLS[(IndIna+1) - NumTotaleSemestri] > 0)  { %>
			      <td width=3%><span id="SL_SPE<%=IndIna%>" style="color=red;font-weight:bold;">Inammissibili </span> <input type=checkbox name=gg_SPE value=2 onclick="Javascript:ViewLayer_SPE('<%=IndIna%>');"></td>
			<%     }
			       else { %>
			       <td width=3%><span id="SL_SPE<%=IndIna%>" style="color=navy;font-weight:bold;">Inammissibili </span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndIna%>');"></td>
			       <%     }  %> 
			      <% if (numLS[(IndNlp+1) - NumTotaleSemestri] > 0)  { %>
			      <td width=3%><span id="SL_SPE<%=IndNlp%>" style="color=red;font-weight:bold;">N.L.P./N.D.P. </span> <input type=checkbox name=gg_SPE value=2 onclick="Javascript:ViewLayer_SPE('<%=IndNlp%>');"></td>
			<%     }
			       else { %>
			       <td width=3%><span id="SL_SPE<%=IndNlp%>" style="color=navy;font-weight:bold;">N.L.P./N.D.P. </span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndNlp%>');"></td>
			       <%     }  %>     
			   </table>
			</div>		<!-- Chiude DIV id='resto_SPE' -->

<%            
		}
		else if(lTenori[indTeno].getCodOggettoTenore().compareTo("0621") == 0 || 
				lTenori[indTeno].getCodOggettoTenore().compareTo("2137") == 0 )
		{
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("tenore oggetto 0621 L.A. Integrazione " );
%>

	<!--  	> > >  		INIZIO PARTE CON LE DIV PER 	REVOCA LIBERAZIONE ANTICIPATA INTEGRAZIONE ( L.A.I.)		  	< < < 	-->	

			<div id="tipoconcessione_INT" style="position: relative; display: none; top: 0; left: 0;" >  
		   		<table>
		      		<tr>
		        		<td class="l"> Modalità di scelta dei periodi di Revoca </td>
		      		</tr>
					<tr>
				<% if(perUnicoConcLI_dalal == true || perUnicoConcLI_sologg == true )
					{ 
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						//	siesLogger.debug(" numLI[0] INT - periodo CHECKED -> - per = " + numLI[0]); 	%>	
				         	<td> <input value="S" onclick="Javascript:return AbilitaSemestri_INT();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>"  > per Semestri &nbsp;
				            <input value="C" onclick="Javascript:return AbilitaPeriodo_INT();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>" CHECKED > Unico Periodo </td>
				<%   }
					 else 
					 {	
						 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						 //	siesLogger.debug(" NO PERIODO - numLI[1] INT semestri CHECKED-> - sem = " + numLI[1]); %> 
				      		<td> <input value="S" onclick="Javascript:return AbilitaSemestri_INT();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>"  CHECKED > per Semestri &nbsp;
				           	<input value="C" onclick="Javascript:return AbilitaPeriodo_INT();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>" > unico periodo </td>
				<%	 } %>
				      </tr>
				      <tr> <td>&nbsp;</td> </tr>
				</table>
				
			</div>	<!--  chiude DIV id="tipoconcessione_INT" -->
			
			<div id="comune_INT" style="position: relative; top: 0; left: 0;" >
			
			  <%if( semestriLI > 0) 
			  	{
				  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  	//	siesLogger.debug("--> C semestri_INT - display_block  " );
			  		%>
			  		<div id="semestri_INT" style="position: relative; top: 0; left: 0; " >
			  <%}
			    else  
			    { 
			    	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			    	//	siesLogger.debug("--> C semestri_INT - display_none " );
			    	%>
			        <div id="semestri_INT" style="position: relative; top: 0; left: 0; display:none; ">
	       <%   }%>
	       
			    <table cellspacing="2" cellpadding="2" width=26%>
			    	<tr>
			        	<td class="Titolo" colspan=6> Semestri Revocati:&nbsp;&nbsp;&nbsp; </td>
			    	</tr>
			    </table>
			    
			    <table width=26%>
  <%
 /* 
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C _INT Period num[0] = "+numLI[0] ); 
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C _INT semest num[1] = "+numLI[1] );
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C _INT Rigett num[2] = "+numLI[2] );
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C _INT Inammi num[3] = "+numLI[3] );
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C _INT NLP    num[4] = "+numLI[4] );
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> C _INT Scompu num[5] = "+numLI[5] ); 
 */ 
  				for (int xx=0; xx<NumRighe; xx++)
			    {    
			    	%>
			    	<tr>
  	<%
			      	for (int j=0;j<NumColonne;j++)
			      	{	
			      		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			      		//	siesLogger.debug("--> C _INT int j = "+j ); 
			      		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			      		//	siesLogger.debug("--> C _INT int xx*NumColonne+j = "+(xx*NumColonne+j) ); 
 
						if ((((xx*NumColonne+j) < numLI[1]) || ((xx*NumColonne+j) < numLI[5])) && (semestriLI > 0) ) 
			      		{ 			
			      				
			      			%>
			      			<td width=3%><span id="SL_INT<%=xx*NumColonne+j%>" style="color=red;font-weight:bold;">Giorni 30</span> <input type=checkbox name=gg_INT value=2 onclick="Javascript:ViewLayer_INT('<%=xx*NumColonne+j%>');"></td>
			<%     		}
			       		else 
			       		{ 	%>
			       			<td width=3%><span id="SL_INT<%=xx*NumColonne+j%>" style="color=navy;font-weight:bold;">Giorni 30</span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=xx*NumColonne+j%>');"></td>
			<%     		}
						
			    	} 	%>
			    	
			        </tr>
			        
	     <%		}		%>
			     </table>
			     
			</div>	<!--  chiude DIV = "semestri_INT" -->
						
			  <%if( semestriLI > 0) 
			  	{
				  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  	//	siesLogger.debug("--> C periodo_INT - display_none - " );
			  		%>
			  		<div id="periodo_INT" style="position: relative; top: 0; left: 0; display:none; " >
			  <%}
			    else  
			    { 
			    	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			    	//	siesLogger.debug("--> C semes periodo_INT  display_block -  " );
			    	%>
			    	<div id="periodo_INT" style="position: relative; top: 0; left: 0; " >
 <%			    }	%>
 
			    	<table cellspacing="2" cellpadding="2" width=26%>
			      		<tr>
			        		<td class="Titolo" colspan=6> Periodo Revocato:&nbsp;&nbsp;&nbsp; </td>
			      		</tr>
			    	</table>
			    		
			    	<table width=26%>
			      		<tr>
			<%	//	 Rimane num[0] perchè trttasi di Periodo 
					if ( ((numLI[0] > 0) || (numLI[5] > 0)) && (semestriLI == 0)  && (perUnicoConcLI_dalal == true) )
			      	{ %>
			       			<td width=3%><span id="SL_INT<%=IndPer%>" style="color=red;font-weight:bold;">Periodo </span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndPer%>');"></td>
			<%      }
			        else
			        { %>
			       			<td width=3%><span id="SL_INT<%=IndPer%>" style="color=navy;font-weight:bold;">Periodo </span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndPer%>');"></td>
			<%      } %>
			      		</tr>
			    	</table>
			    	
			  </div>	<!--  chiude DIV id="periodo_INT" -->
			
			<%
			int jDate = 0;
			String iChecked = "";
			
			for (int i1=0; i1<NumCheck; i1++)
			{
			
				iChecked = "";
				if (plam_arrayLI[i1] != null)
				{
					// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					//siesLogger.debug("--> C INT Prima del ciclo for per le date Dal Al - ichecked = i1 = "+i1 ); 
					iChecked = "checked";
				}	
					
			%>
			<div id="L_INT<%=i1%>" style="position: relative; top: -120; left: 500; display:none;" >
			  <table>
			   <!-- COSTRUZIONE DEI CAMPI DATA DAL - AL -->
			   <% 
			   
			   // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			   //siesLogger.debug("--> C INT Prima del ciclo for per le date Dal Al - NumDate = "+NumDate+" i1 = "+i1 ); 
			    for (int k=0; k<NumDate; k++)
				{
				   %>
			    <tr>
			      <td class=l>
				  	Dal
				  	<% 	
				  //	Per semesri: t < numLI[1]  -  per periodi: t < numLI[0]  -  per periodi scomputati: t == (IndPer) && numLI[5] > 0 
				  	
				  	if ((((i1 < numLI[1]) || (i1 < numLI[5])) && (plam_arrayLI[i1] != null) && (k < plam_arrayLI[i1].length) )  ||
					 	( (i1 == (IndPer)) && (numLI[0] > 0 || numLI[5] > 0 ) && (plam_arrayLI[i1] != null) && (k < plam_arrayLI[i1].length) ) ||
					 	( (i1 == (IndRig)) && (numLI[2] > 0) && (plam_arrayLI[i1] != null) && (k < plam_arrayLI[i1].length) ) ||
					 	( (i1 == (IndIna)) && (numLI[3] > 0) && (plam_arrayLI[i1] != null) && (k < plam_arrayLI[i1].length) ) ||
					 	( (i1 == (IndNlp)) && (numLI[4] > 0) && (plam_arrayLI[i1] != null) && (k < plam_arrayLI[i1].length) ) )	
				 	{ 
				  		 	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  		 	//siesLogger.debug("--> C INT DENTRO for per le date Dal Al -  i1  = "+i1+"  k = "+k );
				  		 	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  		 	//siesLogger.debug("--> C INT DENTRO for per le date Dal Al - plam_arrayLI[i1] = "+plam_arrayLI[0]);
				  			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  			//siesLogger.debug("--> C INT DENTRO for per le date Dal Al - plam_array[i1]lengh = "+plam_arrayLI[i1].length);
			%>
							<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT%>" maxlength="2" size="2" value="<%=DateUtils.getDateToString(plam_arrayLI[i1][k].getDataInizio(),"dd")%>" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT%>" maxlength="2" size="2" value="<%=DateUtils.getDateToString(plam_arrayLI[i1][k].getDataInizio(),"MM")%>" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT%>" maxlength="4" size="4" value="<%=DateUtils.getDateToString(plam_arrayLI[i1][k].getDataInizio(),"yyyy")%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
					     	&nbsp;&nbsp;
					     	Al
							<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_INT%>" maxlength="2" size="2" value="<%=DateUtils.getDateToString(plam_arrayLI[i1][k].getDataFine(),"dd")%>" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_INT%>" maxlength="2" size="2" value="<%=DateUtils.getDateToString(plam_arrayLI[i1][k].getDataFine(),"MM")%>" <%=IWebConstants.UTIL_DATA%>>
							/
					        <input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_INT%>" maxlength="4" size="4" value="<%=DateUtils.getDateToString(plam_arrayLI[i1][k].getDataFine(),"yyyy")%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
			      <% }
			         else
			         { %>
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
			     <%  } %>
			     
			      </td>
			    </tr>
		  
		  <%	}%>
			 </table>
			     <%
			      if (i1 < NumTotaleSemestri )
			     {
			     %>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (i1 == IndPer)
			     {%>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (i1 == (IndRig))
			     {%>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (i1 == ( IndIna) )
			     {%>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT%>" value=1 <%=iChecked%> style="display:none;">
			     <% } else if (i1 == ( IndNlp) )
			     {%>
			    <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT%>" value=1 <%=iChecked%> style="display:none;">
			  <% } %>
			  
			</div>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%-- chiude DIV id="L_INT<%=i%>" --%>
		
		<%	 }%>
			
			    <table width=35%>
			    <tr>
			      <td class="l">Totale Giorni Revocati</td>
			      <td class="l">
			        <input id="giorni_INT" Title="TotGiorni_LAI" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>" value="" size=5  onFocus="javascript:rifiutaFocusSemestri_INT()">
			      </td>
			    </tr>
			    </table>
			    
			  </div>	<!--  chiude DIV id="comune_INT" -->
			
			<br>
			
			<div id="resto_INT" style="position: relative; top: 0; left: 0;" >
			    <table cellspacing="2" cellpadding="2" width=35%>
			    <tr>
			        <td class="Titolo" colspan=6> Periodi Non Revocati:&nbsp;&nbsp;&nbsp; </td>
			    </tr>
<%
	/*
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C _INT - NumTotaleSemestri = "+NumTotaleSemestri ); 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C _INT - IndRig = "+IndRig ); 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C _INT - IndIna = "+IndIna ); 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C _INT - IndNlp = "+IndNlp ); 
				// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--> C _INT - IndPer = "+IndPer );
	*/			
%>			    
			    </table>
			    <table width=35%>
			      <% if (numLI[(IndRig+1) - NumTotaleSemestri] > 0)  { %>
			      <td width=3%><span id="SL_INT<%=IndRig%>" style="color=red;font-weight:bold;">Rigettati  </span> <input type=checkbox name=gg_INT value=2 onclick="Javascript:ViewLayer_INT('<%=IndRig%>');"></td>
			<%     }
			       else { %>
			       <td width=3%><span id="SL_INT<%=IndRig%>" style="color=navy;font-weight:bold;">Rigettati  </span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndRig%>');"></td>
			       <%     }  %> 
			      <% if (numLI[(IndIna+1) - NumTotaleSemestri] > 0)  { %>
			      <td width=3%><span id="SL_INT<%=IndIna%>" style="color=red;font-weight:bold;">Inammissibili  </span> <input type=checkbox name=gg_INT value=2 onclick="Javascript:ViewLayer_INT('<%=IndIna%>');"></td>
			<%     }
			       else { %>
			       <td width=3%><span id="SL_INT<%=IndIna%>" style="color=navy;font-weight:bold;">Inammissibili  </span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndIna%>');"></td>
			       <%     }  %> 
			      <% if (numLI[(IndNlp+1) - NumTotaleSemestri] > 0)  { %>
			      <td width=3%><span id="SL_INT<%=IndNlp%>" style="color=red;font-weight:bold;">N.L.P./N.D.P.  </span> <input type=checkbox name=gg_INT value=2 onclick="Javascript:ViewLayer_INT('<%=IndNlp%>');"></td>
			<%     }
			       else { %>
			       <td width=3%><span id="SL_INT<%=IndNlp%>" style="color=navy;font-weight:bold;">N.L.P./N.D.P.  </span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndNlp%>');"></td>
			       <%     }  %>     
			   </table>
			</div>		<!-- Chiude DIV id='resto_INT' -->
<%			
		}	// chiude else if(lTenori[i].getCodOggettoTenore().compareTo("0621") == 0)
			
	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	//	siesLogger.debug("---------------------------> C prima di chiusura ciclo FOR  - indTeno = "+indTeno ); 	
			
   }	// chiude ciclo for (int i=0; i< lTenori.length;i++)
    %>
  
  <br><br>
	<table cellspacing="2" cellpadding="2" style="width: 90%;">
	   <tr>
	     <td class="l">In caso di Incompetenza indicare l'Ufficio di Sorveglianza destinatario </td>
	     <td class="l">
	        <input Title="UfficioDiSorveglianza" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>" value="" size=35 >
		        <a href="Javascript:ListaUDS('ModOrdinanzaRevocaLA','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>');">
		        <img src="/images/filefolder.gif" border=0></a></td>
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
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="43" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC%>" value="<%=lIdOrdinanza%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO%>" value="<%=lIdDecreto%>" >
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lIdEvento%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO%>" >   
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>" value="<%=TotggLA %>" >
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE%>" value="<%=TotggLS %>" >
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT%>" value="<%=TotggLI %>" >
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>" value="" >
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO%>" value="" >
  <!--  /div>	-->

<%-- // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger() --%>
<%			siesLogger.debug("--> C Fine Modifica - totggLA = "+TotggLA );    %>

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("ModOrdinanzaRevocaLA");


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