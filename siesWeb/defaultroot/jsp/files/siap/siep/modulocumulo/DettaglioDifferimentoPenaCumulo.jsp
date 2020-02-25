<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>


<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="Provvedimento" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>

<jsp:useBean id="descrTDSCompetente" scope="request" class="java.lang.String"/>

<%
Vector <ComputiCumuloModel> lListaComputi = Provvedimento.getListaComputi();
ComputiCumuloModel lComputo = new ComputiCumuloModel();

if (lListaComputi!=null) {

	Iterator itxComputi = lListaComputi.iterator();
	int ind = 0;
	while ( itxComputi.hasNext()) 
	{
		lComputo = (ComputiCumuloModel) itxComputi.next();
		ind++;
	}
	
    lComputo.setDescrOggettoDecisione ( DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getMotivoInterruzione(),lComputo.getCodOggettoDecisione() ) );  
}
	// Reclusione
 	CalendarModel lResiduaReclusione = new CalendarModel();
 	if(lComputo.getNumAnniRevocaReclusione()!=null ) {
 	 	lResiduaReclusione.setNumAnni(lComputo.getNumAnniRevocaReclusione());
 	}
 	if(lComputo.getNumMesiRevocaReclusione()!=null ) {
 		 lResiduaReclusione.setNumMesi(lComputo.getNumMesiRevocaReclusione());
 	}
 	if(lComputo.getNumGiorniRevocaReclusione()!=null ) {
 		 lResiduaReclusione.setNumGiorni(lComputo.getNumGiorniRevocaReclusione());
 	}
 	// Arresto
 	CalendarModel lResiduaArresto = new CalendarModel();	 
 	if(lComputo.getNumAnniRevocaArresto()!=null ) {
 		 lResiduaArresto.setNumAnni(lComputo.getNumAnniRevocaArresto());
 	}
 	if(lComputo.getNumMesiRevocaArresto()!=null ) {
 		 lResiduaArresto.setNumMesi(lComputo.getNumMesiRevocaArresto());
 	}
 	if(lComputo.getNumGiorniRevocaArresto()!=null ) {
 		 lResiduaArresto.setNumGiorni(lComputo.getNumGiorniRevocaArresto());
 	}
 	//  PENA RESIDUA TOTALE = Reclusione + Arresto 
 	CalendarModel lResiduoDaEspiare = new CalendarModel();
 	CalendarUtil  lCalUtil = new CalendarUtil();
 	lResiduoDaEspiare = lCalUtil.sommaGiorni(lResiduaReclusione, lResiduaArresto);
// ==============================================================================================	

String appendTDSCompetente="";
	String appendTDSTrasmissione="";
	if (lComputo.getFlagDecisioneTribunale()!=null	) {			
	   if (lComputo.getFlagDecisioneTribunale().compareTo("S") == 0) {
	appendTDSCompetente="la decisione del TDS";
			appendTDSTrasmissione = " al TDS di ";
	   } else 	if (lComputo.getFlagDecisioneTribunale().compareTo("M") == 0) {
			appendTDSCompetente="la decisione del TDSM";
			appendTDSTrasmissione = " al TDS dei Minori di ";
	   }
	}
%>

<html>
<head>
  <title> Dettaglio Differimento Pena </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_JQUERY%>></script>  
 
  <script language="JavaScript">

    function eseguiFunzione(aTipoAzione)
    {
      if (aTipoAzione=='Indietro'){
        lAzione = "siap.siep.modulocumulo.action.ActRicercaDifferimentoPenaCumulo";
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.submit();
      }
    }

    function showDivForOggetto()
    {
      var codOggetto = '<%= (Provvedimento.getCodMotivo())%>' ;
      var arrayEmpty = ["-","0203","0204","0205","0206","0207","0208"];
      var arrayUDS = [ "2010","2011"];
      var arrayTDS = [ "0030","0031","0032","0033","0031","0201","0202"];


      if ($.inArray(codOggetto, arrayEmpty)>-1) {
		$('#divEmpty').show();
		$('#divEmpty text').prop('disabled',false);
      }
      else {
		$('#divEmpty').hide();
       	$('#divEmpty text').prop('disabled',true);
      }
      if ($.inArray(codOggetto, arrayUDS)>-1) {
        $('#divUDS').show();
        $('#divUDS textarea').prop('disabled',false);
      }
      else {
        $('#divUDS').hide();
        $('#divUDS textarea').prop('disabled',true);
      }

      if ($.inArray(codOggetto, arrayTDS)>-1) {
		$('#divTDS').show();
		$('#divTDS textarea').prop('disabled',false);
	  }
      else {
		$('#divTDS').hide();
		$('#divTDS textarea').prop('disabled',true);
	  }
    }
    
  </script>
</head>

<body class="corpo" onLoad="showDivForOggetto();" >

<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Differimento Pena &nbsp;</font>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('Indietro')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      </td>
    </tr>
  </table>
</FORM>

  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>
      </td>
    </tr>
  </table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(Provvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"      value="<%=StringUtils.toStringJSP(Provvedimento.getFlagStato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>" value="<%=StringUtils.toStringJSP(StringUtils.cStrForJS(Provvedimento.getMotivoModifica()), "") %>">


  	<table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
		
		<tr>
	      <td class="titolo" colspan="4">Dati del Provvedimento</td>
		</tr>
	     
		<tr>
	      <td class="l" width="210px">Data emissione provvedimento </td>
	      <td class="l" >
	         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataEmissioneProvv(),"dd-MM-yyyy"))%></font>&nbsp;
	      </td>
	      <td class="l">Tipo provvedimento </td>
	      <td class="l" >
			<font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrTipoProvvedimento() )%> </font>&nbsp;
	      </td>		      
	    </tr>

		<tr>
	      <td class="l">Anno / Numero Provvedimento </td>
	      <td class="l" >
	         <font class="campo"><%=StringUtils.toStringJSP(lComputo.getAnnoProvv() )%> &nbsp;/ <%=StringUtils.toStringJSP(lComputo.getProgrProvv() )%></font>&nbsp;
	      </td>
	      
	      <td class="l">Anno / Numero SIUS </td>
	      <td class="l" >
	         <font class="campo"><%=StringUtils.toStringJSP(lComputo.getAnnoProc() )%> &nbsp;/ <%=StringUtils.toStringJSP(lComputo.getProgrProc() )%></font>&nbsp;
	      </td>
	    </tr>

	    <tr> 
	      <td class="l">Autorità Emittente </td>
	      <td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrUfficioEmittente())+ " " + StringUtils.toStringJSP(Provvedimento.getDescrLuogoEmittente())%> </font>&nbsp;
	      </td>
	    </tr>
	     
	    <tr> 
	      <td class="l">Oggetto Procedimento</td>
	      <td class="l"colspan = "3">
	         <font class="campo"><%=StringUtils.toStringJSP( Provvedimento.getDescrMotivo() ) %></font>&nbsp;
		  </td>
	    </tr>


		<div id="blank" style="display:block;">
		  <table width="95%" align="center" >
		  <tr>
		   </tr>
		  </table>
		</div>  

		<div id="divUDS" style="display:block;">
		  <table width="95%" align="center" >
	
	  		<tr>
	      	  <td class="l" width="210">Data Decorrenza Pena </td>
	      	  <td class="l" width="240">
	         	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataReclusioneDa(),"dd-MM-yyyy"),"")%></font>
	      	  </td>
	      	  <td class="l" width="210" >Data Scadenza Pena </td>
	      	  <td class="l" >
	         	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataReclusioneA(),"dd-MM-yyyy"),"")%></font>
	      	  </td>
      		</tr>
			<tr><td>&nbsp;</td></tr>
	  		<tr>
			    <td class="l" width="210" >
			        Data Differimento Esecuzione</td>
			    <td class="l" colspan="3">
		         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataInizioMisura(),"dd-MM-yyyy"))%></font>
			    </td>
			</tr>
	
			<tr>
			  	<td colspan="4" class="titolo">Durata e Differimento Della Pena </td>
			</tr>
	
	  		<tr>
			    <td class="l" width="240" >
			        Rinvio fino al<%=appendTDSCompetente %> </td>
			    <td class="l" colspan=2>
		         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataFineMisura(),"dd-MM-yyyy"))%></font>
		    				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		    		Atti Trasmessi<%=appendTDSTrasmissione%>&nbsp;&nbsp;&nbsp;&nbsp;

		         <font class="campo"><%=StringUtils.toStringJSP(descrTDSCompetente)%></font>
			    </td>
			</tr>
			<tr>
<%	//		if (lComputo.getFlagDecisioneTribunale()==null	||
	//			lComputo.getFlagDecisioneTribunale().compareTo("S")!=0) { %>	
		      <td class="l"  width="240">Rinvio nella Misura di </td>
	          <td class="l" colspan= "2">Anni
				<font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumAnniMisura())%></font>	          
		          &nbsp; Mesi
				<font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumMesiMisura())%></font>	          
		          &nbsp; Giorni
				<font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumGiorniMisura())%></font>	          
			  </td>
<%	//		} %>			
		   </tr>
		   
		   <tr><td>&nbsp;</td></tr>
	    	
	    	<tr>
	      	  <td class="l"  width="210">Pena Espiata: </td>
	      	  <td class="l" align="center">
       			<font class="l">Anni</font>&nbsp;
		        <font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumAnniReclusione() )%></font>&nbsp;&nbsp;&nbsp;&nbsp;
	       		<font class="l">Mesi</font>&nbsp;
		        <font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumMesiReclusione() )%></font>&nbsp;&nbsp;&nbsp;&nbsp;
	       		<font class="l">Giorni</font>
		        <font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumGiorniReclusione() )%></font>
	   		  </td>
	   		</tr>
	   		<tr>  
	   		  <td class="l"  width="210">Pena residua da Espiare: </td>
		      <td class="l" align="center">
	       		<font class="l">Anni</font>&nbsp;
		        <font class="campo"><%=StringUtils.toStringJSP(lResiduoDaEspiare.getNumAnni() )%></font>&nbsp;&nbsp;&nbsp;&nbsp;
	       		<font class="l">Mesi</font>&nbsp;
		        <font class="campo"><%=StringUtils.toStringJSP(lResiduoDaEspiare.getNumMesi() )%></font>&nbsp;&nbsp;&nbsp;&nbsp;
	       		<font class="l">Giorni</font>
		        <font class="campo"><%=StringUtils.toStringJSP(lResiduoDaEspiare.getNumGiorni() )%></font>
	   		  </td>
      		</tr>
		   
		  </table>
		</div>
	
		<div id="divTDS" style="display:block;">
		  <table width="95%" align="center" >
	
	  		<tr>
	      	  <td class="l" width="210">Data Decorrenza Pena </td>
	      	  <td class="l" width="240">
	         	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataReclusioneDa(),"dd-MM-yyyy"),"")%></font>
	      	  </td>
	      	  <td class="l" width="210" >Data Scadenza Pena </td>
	      	  <td class="l" >
	         	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataReclusioneA(),"dd-MM-yyyy"),"")%></font>
	      	  </td>
      		</tr>
			<tr><td>&nbsp;</td></tr>
	  		<tr>
			    <td class="l" width="210" >
			        Data Differimento Esecuzione</td>
			    <td class="l" colspan="3">
		         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataInizioMisura(),"dd-MM-yyyy"))%></font>
			    </td>
			</tr>
	
			<tr>
			  	<td colspan="4" class="titolo">Durata e Differimento Della Pena </td>
			</tr>
	
	  		<tr>
			    <td class="l" width="240" >
			        Rinvio fino al </td>
			    <td class="l" colspan=3>
		         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataFineMisura(),"dd-MM-yyyy"))%></font>
			</tr>
	
			<tr>
		      <td class="l"  width="240">Rinvio nella Misura di </td>
	          <td class="l">Anni
				<font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumAnniMisura())%></font>	          
		          &nbsp; Mesi
				<font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumMesiMisura())%></font>	          
		          &nbsp; Giorni
				<font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumGiorniMisura())%></font>	          
		      </td>
		   </tr>

		   <tr><td>&nbsp;</td></tr>
	    	
	    	<tr>
	      	  <td class="l"  width="210">Pena Espiata: </td>
	      	  <td class="l" align="center">
       			<font class="l">Anni</font>&nbsp;
		        <font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumAnniReclusione() )%></font>&nbsp;&nbsp;&nbsp;&nbsp;
	       		<font class="l">Mesi</font>&nbsp;
		        <font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumMesiReclusione() )%></font>&nbsp;&nbsp;&nbsp;&nbsp;
	       		<font class="l">Giorni</font>
		        <font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumGiorniReclusione() )%></font>
	   		  </td>
	   		</tr>
	   		<tr>  
	   		  <td class="l"  width="210">Pena residua da Espiare: </td>
		      <td class="l" align="center">
	       		<font class="l">Anni</font>&nbsp;
		        <font class="campo"><%=StringUtils.toStringJSP(lResiduoDaEspiare.getNumAnni() )%></font>&nbsp;&nbsp;&nbsp;&nbsp;
	       		<font class="l">Mesi</font>&nbsp;
		        <font class="campo"><%=StringUtils.toStringJSP(lResiduoDaEspiare.getNumMesi() )%></font>&nbsp;&nbsp;&nbsp;&nbsp;
	       		<font class="l">Giorni</font>
		        <font class="campo"><%=StringUtils.toStringJSP(lResiduoDaEspiare.getNumGiorni() )%></font>
	   		  </td>
      		</tr>

		  </table>
		</div>

	</table>

<br>

</form>
</body>
</html>