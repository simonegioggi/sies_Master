<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>


<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichPMPenAccCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichPMMisSicCumModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="IstruttoriaCumulo" 	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="UfficioEmittente"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" 			   	scope="request" class="java.lang.String"/>
<jsp:useBean id="RichiestaGE"     		scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TitoliRichiesta"     	scope="request" class="java.util.Vector"/>
<jsp:useBean id="ProvvGECum"     		scope="request" class="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"/>

<!-- 			LoadInserisciDecisioneDelGECumulo				 -->
<%
//==============================================================================
//  Form con le funzioni di Inserimento/Modifica della decisione del G.E.
//	a seguito di Richiesta Applicazione Benefici da parte del P.M.
//==============================================================================

int TotRic = TitoliRichiesta.size();

RichiesteInviateCumModel lRicInv = null;
if(RichiestaGE!=null && RichiestaGE.getRichiesteInviateCum()!=null)
{
	lRicInv = (RichiesteInviateCumModel)RichiestaGE.getRichiesteInviateCum();
	//LogF3B.getLogger().debug(" --XX-- Richiesta Inviata = "+lRicInv);
}

//Data Richiesta al GE
String lDataRich = StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaGE.getDataEmissione(),"dd/MM/yyyy"));

Vector<RichPMPenAccCumModel> lVecPA = null;
Vector<RichPMMisSicCumModel> lVecMS = null;

if("M".equals(modalita))
{
	if(RichiestaGE.getListaRichPmPenaAccessoriaCum()!=null && RichiestaGE.getListaRichPmPenaAccessoriaCum().size() > 0)
		lVecPA = new Vector<RichPMPenAccCumModel>(RichiestaGE.getListaRichPmPenaAccessoriaCum());
	
	if(RichiestaGE.getListaRichPmMisureSicurezzaCum()!=null && RichiestaGE.getListaRichPmMisureSicurezzaCum().size() > 0 )
		lVecMS = new Vector<RichPMMisSicCumModel>(RichiestaGE.getListaRichPmMisureSicurezzaCum() );
}


%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo - Decisione del GE su Richiesta Applicazione Benefici</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">
  	
	//Apre la finestra con la Lista delle Sedi uffici in base alla tipologia di Ufficio Selezionata
  	function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
  	{
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}
	
    //==========================================================================
    // Ritorna alla Griglia Delle Richieste al GE
    //==========================================================================
    function tornaIndietro(action)
    {
      document.indietroForm.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.indietroForm.submit();
    }
    
    function Verify() 
    { 
   	 	var total = <%=TotRic%>;
   	 	
   		// Anno e Numero Procedimento SIGE
        if(document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value == '' && 
        	 document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value != ''	 )
        {
        	alert('Anno Procedimento G.E. NON valido');
            document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.focus();
            return false;
        } 
         
        if(document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value != '' && 
        	 document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value == ''	 )
        {
        	alert('Numero Procedimento G.E. NON valido');
            document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.focus();
            return false;
        }
         
        if(document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value == '' && 
           document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value == ''	 )
        {
  			alert('digitare Anno e Numero Procedimento G.E.');
            document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.focus();
            return false;
        }

 	   // Data Emissione Procedimento
       if (document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value.length==1)
         	document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value='0'+document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value;
       if (document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value.length==1)
         	document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value='0'+document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value;

       var data_to_verify = document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value+'/'+document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value+'/'+document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE_D%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Emissione Procedimento');
           document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Emissione Procedimento NON valida');
           document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.focus();
           return false;
       }
       
        // Data Emissione Procedimento SIGE deve essere <= Data del Giorno
        var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        if(!CompareDate(data_to_verify, data_od))
        {
          alert('Data Emissione Procedimento SIGE NON può essere superiore alla Data Odierna');
          document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>.focus();
          return false;
        }
        
        // Data della Richiesta deve essere <= Data Emissione Procedimento SIGE 
        var data_Ric = '<%=lDataRich%>';
        if(!CompareDate(data_Ric, data_to_verify))
        {
          alert('Data Emissione Procedimento SIGE DEVE essere superiore o uguale \nalla Data Richiesta del PM al G.E.');
          document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>.focus();
          return false;
        }
       
       // Tipo Ufficio e sede Ufficio Emittente
       if(document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.value == '-' || 
      	 document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.value.length == 1	 )
       {
      	 	alert('Ufficio Emittente non valido');
          	document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
          	return false;
       } 
       
       if(document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value == '-' || 
      	 document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value.length == 1 ||
      	 document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value.length == 0 )
       {
      	 	alert('Sede Ufficio Emittente non valida');
          	document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
          	return false;
       }
       
       // FlagConforme
       if(!document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME%>[0].checked && 
      	  !document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME%>[1].checked &&
      	  !document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME%>[2].checked &&
      	  !document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME%>[3].checked )
      {
      	 	alert('Selezionare il radioButton ESITO: \nConformità/Difformità/Rigetto/Inammissibilità ');
        	document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME%>[0].focus();
        	return false;
      }	 
       
      // Quantum se Conforme/Difforme
      var tipoDec = $('input[name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>"]:checked').val();
      if(tipoDec == "C" || tipoDec == "D")
      {
        // Quantum
        var pos = document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_D%>INT.value.indexOf('.');
        if(pos > 0)
        {
          alert('inserire correttamente il valore INTERO della Multa');
          document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_D%>INT.focus();
          return false;
        }
    
        pos = document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_D%>INT.value.indexOf('.');
        if(pos > 0)
        {
          alert('inserire correttamente il valore INTERO della Ammenda');
          document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_D%>INT.focus();
          return false;
        }
    
        if(document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_D%>.value == ''  )
        {
          alert('digitare il segno + / - quntum pena');
          document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_D%>.focus();
          return false;
        }
       
        if( ( document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_D%>.value == '' ||
           document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_D%>.value.length == 0 )  &&
           ( document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_D%>.value == '' ||
             document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_D%>.value.length == 0  ) &&  
           ( document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_D%>.value == '' ||
             document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_D%>.value.length == 0 ) &&
             
           ( document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_D%>.value == '' ||
             document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_D%>.value.length == 0 ) &&
           ( document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_D%>.value == '' ||
             document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_D%>.value.length == 0 ) &&
           ( document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_D%>.value == '' ||
             document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_D%>.value.length == 0 ) &&  
             
           ( document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_D%>INT.value.length == 0 ) &&
           ( document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_D%>DEC.value.length == 0 ) &&  
           ( document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_D%>INT.value.length == 0 ) &&
           ( document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_D%>DEC.value.length == 0 ) 
         )  
        {
          alert('digitare una quantità di pena');
          document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_D%>.focus();
          return false;
        }
      }
      return true; 
    }
    
    function MettiCondono()
    {
   	 	$("input[name='idPenaAccSel']" ).prop("checked",true);
   	 	
   	 	$("[name=idMisuraSel]" ).prop("checked",true);

    }
    
  </script>

</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
<%	if("I".equals(modalita))
	{	%>        
        <font class="campo">Inserimento Decisione del G.E. su Richiesta Applicazione Benefici</font>
<%	}
	else if("M".equals(modalita))
	{	%>
		<font class="campo">Modifica Decisione del G.E. su Richiesta Applicazione Benefici</font>
<%	} %>	        
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia delle Richieste al GE-->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>

<% // INCLUDE DEL DETTAGLIO FASCICOLO%>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>

<% // INCLUDE DEL DETTAGLIO DELL'ISTRUTTORIA %>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="indietroForm">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
</form> 

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="LoadInsDecGE">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciDecisioneDelGECumulo">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaGE.getIdRichiestePmInCumulo() %>" >
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA %>" value="01" >
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PROVVEDIMENTO %>" value="03" >
  <input type="hidden" name="modalita" value="<%=modalita%>" >

  <%
  //========================================================================
  //  Dati esclusivi della Richiesta
  //========================================================================
  %>
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della richiesta</td></tr>
    <tr>
      <td class="l" width="200px">Data Richiesta</td>
      <td class="l" colspan="2">
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaGE.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>	
    <tr>
      <td class="l" width="200px">Computo beneficio :</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrTipoAnnotazione() )%></font>
      </td>
      <td class="l">DPR :</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrDpr() )%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Anticipazione degli effetti :</td>
 <%	if("A".equals(RichiestaGE.getFlagAppProvvisoria() ))
 	{ %>     
      	<td class="l" colspan="3"><img src="/images/V.gif"> </td>
<%	}
 	else if("R".equals(RichiestaGE.getFlagAppProvvisoria() ))
 	{  %>
 		<td class="l" colspan="3"><font class="label" style="color:red" > NO </font></td>
<% 	}
	else
	{	%>
		<td class="l" colspan="3"> &nbsp;&nbsp; - &nbsp;&nbsp;</td>
<%	} %>	 	      
    </tr>

    <tr>
      <td class="l" colspan="1">Reclusione :  </td>
      <td class="l" colspan="3">&nbsp;
      	<font class="campo" style="color:red"><%=StringUtils.toStringJSP(RichiestaGE.getFlagPiuMenoR(),"")%></font>&nbsp;&nbsp;
      	<font class="label"> Anni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumAnniReclusioneR(), " - ") %></font>&nbsp;
		<font class="label"> Mesi </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumMesiReclusioneR(), " - ") %></font>&nbsp;
        <font class="label"> Giorni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumGiorniReclusioneR(), " - ") %></font>&nbsp;
         &nbsp;&nbsp;&nbsp;	<font class="label"> Multa : </font>&nbsp;
         <%					if(RichiestaGE.getImportoMultaR()!=null && RichiestaGE.getImportoMultaR().compareTo(BigDecimal.ZERO) > 0 )
         					{	%>
        						<font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getImportoMultaR()) %></font>&nbsp;&euro;&nbsp;
        			<%		}
         					else
         					{	%>
         						<font class="campo">&nbsp; - &nbsp; </font>
         				<%	}	 %>				

      </td>
    </tr>
	<tr>
      <td class="l" colspan="1">Arresto :  </td>
      <td class="l" colspan="3">&nbsp;
      	<font class="campo" style="color:red"><%=StringUtils.toStringJSP(RichiestaGE.getFlagPiuMenoR(),"")%></font>&nbsp;&nbsp;
      	<font class="label"> Anni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumAnniArrestoR(), " - " ) %></font>&nbsp;
		<font class="label"> Mesi </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumMesiArrestoR(), " - ") %></font>&nbsp;
        <font class="label"> Giorni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumGiorniArrestoR(), " - ") %></font>&nbsp;
         &nbsp;&nbsp;&nbsp;	<font class="label"> Ammenda : </font>&nbsp;
         <%					if(RichiestaGE.getImportoAmmendaR()!=null && RichiestaGE.getImportoAmmendaR().compareTo(BigDecimal.ZERO) > 0 )
         					{	%>
        						<font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getImportoAmmendaR()) %></font>&nbsp;&euro;&nbsp;
        			<%		}
         					else
         					{	%>
         						<font class="campo">&nbsp; - &nbsp; </font>
         				<%	}	 %>				

      </td>
    </tr>
  </table>
  
 <%
  //=======================================================
  //  				Eventuale Richiesta Inviata
  //=======================================================
 %>
  
<%	if(lRicInv!=null && lRicInv.getIdRichiesteInviateCum()!=null )
	{	%>  
  <table width="95%" align="center" style="display:block">
  	<tr><td> </td></tr>
    <tr>
      <td class="l" colspan="1" width="200px">Inviata a : </td>
      <td class="l" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(lRicInv.getDescrUfficioDest(),"")%></font>
      	 di <font class="campo"><%=StringUtils.toStringJSP(lRicInv.getDescrLuogoDest(),"")%></font>
      </td>
      <td class="l" colspan="1">in data: <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRicInv.getDataEmissione(),"dd-MM-yyyy"),"-")%></font></td>
    </tr>
<%	  if( !("null").equals(lRicInv.getContenuto()) )
	  {		%>
	  <tr>
	  	<td class="l" colspan="1" width="200px">Contenuto </td>
        <td class="l" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lRicInv.getContenuto(),"")%></font></td>
      </tr>
 <%	  } %>           
  </table>
<%	} %>  
  	
  	<br>
  
<!-- 							Inizio Dati Inseribili/Modificabili										 -->  
  <%
  //========================================================================
  //  Dati esclusivi della Decisione da Inserire / Modificare
  //========================================================================
  %>  
<tr>
  <td colspan="100%" align="center">

<div id="idDivDecisioneGE"  style="display:block" >   
  <table width="95%" align="center">
    <tr><td colspan="3" class="Titolonocap">Decisione del GE</td></tr>      
    <tr>
      <td class="l">Ordinanza <font class="ob">(*)</font> :</td>
		<td class="l">
          Anno/Numero Procedimento SIGE
          <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>" size=4 maxlength=4 value="<%=StringUtils.toStringJSP(ProvvGECum.getAnnoProvv(),"") %>" 
          		onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
          /
          <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>" size=8 maxlength=6 value="<%=StringUtils.toStringJSP(ProvvGECum.getNumeroProvv(),"") %>" >
      	</td>
      	<td class="l">
          <font class="label">Data emissione Procedimento</font>
          &nbsp;&nbsp;
           <input type="text"  title="Giorno di arrivo documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvGECum.getDataD(), "dd"), "") %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
             /
           <input type="text"  title="Mese di arrivo documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D %>" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvGECum.getDataD(), "MM"), "") %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
           /
           <input type="text"  title="Anno di arrivo documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE_D %>" maxlength="4" size="4" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvGECum.getDataD(), "yyyy"), "") %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>	
     
    </tr>
    
    <tr>
      <td class="l">Ufficio Emittente <font class="ob">(*)</font> :</td>
      <td class="l" colspan="1">
          <select Title="Ufficio Emittente" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>">
            <%=UfficioEmittente%>
          </select>
      </td>
      <td class="l">Sede Ufficio Emittente <font class="ob">(*)</font> :
          <font class="campo">
            <input type="text" Title="Luogo Ufficio Emittente" size="35"
                   name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(ProvvGECum.getDescrLuogoEmittente(), "") %>" >
            <a href="Javascript:ListaUfficiComuni('LoadInsDecGE','<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>',
            					document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>[document.LoadInsDecGE.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.options.selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0>
            </a>
          </font>
      </td>
    </tr>
  </table>
    
    <table width="95%" align="center">
      <tr>
        <td class="l" colspan=3>
<% 
		String checkC = "";
      	String checkD = "";
      	String checkR = "";
      	String checkI = "";
      
      	if("C".equals(ProvvGECum.getFlagConforme()) ){
        	checkC = "checked";
      	}
      	else if("D".equals(ProvvGECum.getFlagConforme()) ) { 
        	checkD = "checked";
      	}
      	else if("I".equals(ProvvGECum.getFlagConforme()) ) { 
        	checkI = "checked";
      	}
      	else if("R".equals(ProvvGECum.getFlagConforme()) ) { 
        	checkR = "checked";
      	}
      //	else { checkC = "checked"; }
%>  
		  <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="C" <%=checkC%> onclick="javascript:MettiCondono();" >in conformita' alla richiesta del PM &nbsp;&nbsp;	<!-- Conforme -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="D" <%=checkD%> >in difformita' alla richiesta del PM &nbsp;&nbsp;	<!-- Difforme -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="R" <%=checkR%> >rigetta &nbsp;&nbsp;	<!-- Rigetta -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="I" <%=checkI%> >dichiara inammissibile &nbsp;&nbsp;	<!-- Inammissibile -->	
	        
        </td>
      </tr>
    </table>
    
<!-- 		Parte con Elenco Titoli, Pene_Accessorie, e Misure di Sicurezza da Condonare/Revocare	 -->    
 <table cellpadding="2" cellspacing="2" width="95%" align="center" style="border:0;">
   <tr><td> </td></tr>	
   <tr><td class="Titolo" colspan="100%">In relazione ai seguenti Titoli:</td></tr>
 </table>  
<%      	
  String DescAuto="";
  String AnnoNumero ="";
  Iterator itx = TitoliRichiesta.iterator();
  while(itx.hasNext())
  {	
    TitoloCumulatoModel lTitolo = (TitoloCumulatoModel)itx.next();
    
    AnnoNumero = lTitolo.getAnnoSentenza() +"/"+lTitolo.getNumeroSentenza();
 	DescAuto = lTitolo.getDescrTipoAutoritaEmittente() +" di "+lTitolo.getDescrLuogoEmittente();
%>
  <table width="95%" align="center"> 
    <tr>
      <td class="l" colspan="8">
        <font class="label"><%=StringUtils.toStringJSP(lTitolo.getDescrTipoProvvedimento() )%>&nbsp;N. &nbsp; </font>
        <font class="campo"><%=AnnoNumero%></font>&nbsp;
        <font class="label"> del </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
        <font class="label"> Emessa da </font>
        <font class="campo"><%=DescAuto%></font>&nbsp;
        &nbsp;<font class="label"> Irrevocabile il  </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
      </td>
    </tr>
    <tr><td></td></tr>   
  </table>

<%  // ------- 	 	MISURA di SICUREZZA		-------------------
	if(lTitolo.getMisureSicurezzaCumulo()!=null && lTitolo.getMisureSicurezzaCumulo().size() > 0 )
	{	    %>
    <table width="95%" align="center">
<%	  for(int km = 0; km < lTitolo.getMisureSicurezzaCumulo().size(); km++ )
	  {
		MisuraSicurezzaCumuloModel lMisura = (MisuraSicurezzaCumuloModel) lTitolo.getMisureSicurezzaCumulo().get(km);  %>      
      <tr>
 <%		if(km == 0) 
      	{	%>  
        <td class="l" style="color:red; text-align:left" width="12%">&nbsp;Misure Sicurezza: </td>
<%	 	}
 		else
 		{	%>  
 		<td class="l" width="10%">&nbsp;</td>
 <%		} %>
  
 		<td class="l">            	
         <font class="campoLow"><%=StringUtils.toStringJSP(lMisura.getDescrTipo(),"") %></font>&nbsp;&nbsp;
         <font class="label"> per la durata di </font>&nbsp;&nbsp;
          Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lMisura.getNumAnni(), " - ") %></font>&nbsp;&nbsp;
          Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lMisura.getNumMesi(), " - ") %></font>&nbsp;&nbsp;
          Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lMisura.getNumGiorni(), " - ") %></font>&nbsp;&nbsp;
        </td>

    <%  String checkMis = "";
    	if(lVecMS!=null && lVecMS.size()>0)
		{	
		  for(int ii=0; ii<lVecMS.size(); ii++)
		  {
		    RichPMMisSicCumModel lMSMod = (RichPMMisSicCumModel)lVecMS.elementAt(ii);

		    if(lMSMod.getMisIdMisSicCumulo().equals(lMisura.getIdMisuraSicurezzaCumulo() ) )
		 	{	
		 	  if(lMSMod.getFlagCondono()!=null && lMSMod.getFlagCondono().equals("S") )
		 	  {
		 		  checkMis = "checked";
		 	  }
		 	}
		  }
		}
    %>	
		<td class="c" width="150px"><font class="label"> CONDONO </font> 	  
          <input type="checkbox" name="idMisuraSel" value="<%=StringUtils.toStringJSP(lMisura.getIdMisuraSicurezzaCumulo()) %>" <%=checkMis%> >                 
        </td>
        
      </tr>
   <% } %>  
    </table>
<%	}	 %>    

<%	// -----------------------	PENE ACCESSORIE	-----------------------------------------  
	if(lTitolo.getPeneAccessorieCumulo()!=null && lTitolo.getPeneAccessorieCumulo().size() > 0 )
	{	%>
    <table width="95%" align="center">
<% 	  for(int kp = 0; kp < lTitolo.getPeneAccessorieCumulo().size(); kp++ )
	  {	
		PenaAccessoriaCumuloModel lPenAcc = (PenaAccessoriaCumuloModel)lTitolo.getPeneAccessorieCumulo().get(kp);	%>
      <tr>
 <%		if(kp == 0) 
      	{	%>  
        <td class="l" style="color:red; text-align:left" width="12%">&nbsp;Pene Accessorie: </td>
<%	 	}
 		else
 		{	%>  
 		<td class="l" width="10%">&nbsp;</td>
 <%		} %>      
        <td class="l">
         <font class="campoLow"><%=StringUtils.toStringJSP(lPenAcc.getDescrTipoPenaAccessoria(),"") %></font>&nbsp;&nbsp;
         
         <% if ("D".equals(lPenAcc.getDurata()) || "P".equals(lPenAcc.getDurata())) { %>
         <font class="label"> Durata: <%=StringUtils.toStringJSP(lPenAcc.getDescrDurata(), "") %> </font>
         <% } else { %>
         <font class="label"> per la durata di </font>&nbsp;&nbsp;
          Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPenAcc.getNumAnni(), " - ") %></font>&nbsp;&nbsp;
          Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPenAcc.getNumMesi(), " - ") %></font>&nbsp;&nbsp;
          Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPenAcc.getNumGiorni(), " - ") %></font>&nbsp;&nbsp;
          <% } %>
        </td>
        
  <%    String checkpa = "";
    	if(lVecPA!=null && lVecPA.size()>0)
		{	
		  for(int i=0; i<lVecPA.size(); i++)
		  {
		    RichPMPenAccCumModel lPAMod = (RichPMPenAccCumModel)lVecPA.elementAt(i);

		    if(lPAMod.getPenIdPenaAccessoriaCumulo().equals(lPenAcc.getIdPenaAccessoriaCumulo() ) )
		 	{	
		 	  if(lPAMod.getFlagCondono()!=null && lPAMod.getFlagCondono().equals("S") )
		 	  {
		 		 checkpa = "checked";
		 	  }
		 	}
		  }
		}
    %>	      
        <td class="c" width="150px"><font class="label"> CONDONO </font>
          <input type="checkbox" name="idPenaAccSel" value="<%=StringUtils.toStringJSP(lPenAcc.getIdPenaAccessoriaCumulo() ) %>" <%=checkpa%> >                 
        </td>
      </tr>
<%	  } 	%>
	 </table>
<% 	} // Chiude Pene_Accessoria			

  }	// Chiude ciclo while(Titolo)  %> 

<!-- 								Q U A N T U M    D E C I S I O N E							 -->   
    <table width="95%" align="center">
    <tr>
      <td colspan=6>
        <hr width="100%">
      </td>
    </tr>
    <tr>
      <td valign="middle" class="c" rowspan=3>+/- <font class="ob">(*)</font><br>
        <select name="<%= ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_D %>">
   <%	if( "-".equals(ProvvGECum.getFlagPiuMenoD()) ) 
    	{	%>    
          <option value = "" ></option>
          <option value = "+" >+</option>
          <option value = "-" selected >-</option>
   <%	}
    	else if( "+".equals(ProvvGECum.getFlagPiuMenoD()) ) 
    	{ %>
    	  <option value = "" ></option>
          <option value = "+" selected >+</option>
          <option value = "-" >-</option>
  <%	}
    	else
    	{ %>
    	  <option value = "" ></option>
          <option value = "+" >+</option>
          <option value = "-" >-</option>
  <%	} %> 
	
        </select>
      </td>
      <td class="titolo" colspan=2>Reclusione</td>
      <td width="25">&nbsp;</td>
      <td class="titolo" colspan=2>Arresto</td>
    </tr>
    <tr>
      <td class="c">
        <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Giorni</font><br>
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_D%>" 
               value="<%=StringUtils.toStringJSP(ProvvGECum.getNumAnniReclusioneD(),"")%>"
               onkeypress="return TicTabNumField(this,event)">&nbsp;
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_D %>"  
               value="<%=StringUtils.toStringJSP(ProvvGECum.getNumMesiReclusioneD(),"") %>"
               onkeypress="return TicTabNumField(this,event)">&nbsp;
        <input type="text" maxlength="4" size="4"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_D %>"                 
               value="<%=StringUtils.toStringJSP(ProvvGECum.getNumGiorniReclusioneD(),"") %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="c">
        <font  class="label">Multa</font><br>
        <input type="text" maxlength="7" size="7" style="align:right"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_D%>INT"
               value="<%=StringUtils.getParteIntera(ProvvGECum.getImportoMultaD()) %>"
               onkeypress="return TicTabNumField(this,event)" >
        ,
        <input type="text" maxlength="2" size="2"  style="align:right"
               name="<%= ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_D %>DEC"
               value="<%=StringUtils.getParteDecimale(ProvvGECum.getImportoMultaD()) %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
      <td width="25">&nbsp;</td>
      <td class=c>
        <font  class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font  class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font  class="label">Giorni</font><br>
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_D %>"  
               value="<%=StringUtils.toStringJSP(ProvvGECum.getNumAnniArrestoD(),"") %>"
               onkeypress="return TicTabNumField(this,event)" >&nbsp;
        <input type="text" maxlength="2" size="2" 
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_D %>"  
               value="<%=StringUtils.toStringJSP(ProvvGECum.getNumMesiArrestoD(),"") %>"
               onkeypress="return TicTabNumField(this,event)" >&nbsp;
        <input type="text" maxlength="4" size="4"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_D %>"          
               value="<%=StringUtils.toStringJSP(ProvvGECum.getNumGiorniArrestoD(),"") %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="c">
        <font  class="label">Ammenda</font><br>
        <input type="text" maxlength="7" size="7" style="align:right"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_D%>INT"         
               value="<%=StringUtils.getParteIntera(ProvvGECum.getImportoAmmendaD()) %>"               
               onkeypress="return TicTabNumField(this,event)">
        ,
        <input type="text" maxlength="2" size="2" style="align:right" 
               name="<%= ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_D %>DEC"  
               value="<%=StringUtils.getParteDecimale(ProvvGECum.getImportoAmmendaD()) %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
    </tr>
    <tr>
      <td class="c" colspan=5>
        <font class="label" style="vertical-align: top;">Note</font>
        <textarea cols="60" rows="2" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI_D %>"><%=StringUtils.toStringJSP(ProvvGECum.getMotivazioniD(),"") %></textarea>
      </td>
      <td width=20>&nbsp;</td>
    </tr>
  </table>
  <br>
  
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>
     
</div>

</td>
</tr>
<!-- 					end dati inseribili / modificabili													-->
  
</form>

</body>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInsDecGE");
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>

</html>


