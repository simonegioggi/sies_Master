<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaAccessoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="RichiestaGE"	       scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TitoloPA"		       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<!-- 						DettaglioRichiestaGESostPA						 -->
<% 
//============================================================================ 
// Form per la visualizzazione del dettaglio delle richieste al GE di 
// Sostituzione Pene Accessorie
//============================================================================ 
RichiesteInviateCumModel lRicInv = null;
if(RichiestaGE!=null && RichiestaGE.getRichiesteInviateCum()!=null)
{
	lRicInv = (RichiesteInviateCumModel)RichiestaGE.getRichiesteInviateCum();
}
%>
<html>
<head>
  <title> Gestione Richieste al GE - Sostituzione Pena Accessorie</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

    function tornaIndietro(action)
    {
      document.indietroForm.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.indietroForm.submit();
    }

    function eseguiFunzioneSelezionata()
    {
	  	var action = document.f.comboAction[document.f.comboAction.selectedIndex].value ;
	
	  	if( action.indexOf('Cancella') >= 0)
	  	{
	    	if ( !window.confirm("Si vuole procedere alla cancellazione della decisione?") ) 
	    	{
	              return;
	        } 
	    	else
	        {
	      		document.DettRichGESosPA.<%=IWebConstants.ACTION_FIELD%>.value = action;
	            document.DettRichGESosPA.modalita.value="C";
	      	    document.DettRichGESosPA.submit();
	    	}
	  	 } 
	  	 else 
	  	 {	
	  		document.DettRichGESosPA.<%=IWebConstants.ACTION_FIELD%>.value = action;
	        document.DettRichGESosPA.modalita.value="I";
	  	    document.DettRichGESosPA.submit();
	     }
    }
    
    function vaiaDecisione(action)
    {
      document.DettRichGESosPA.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichGESosPA.modalita.value="I";
      document.DettRichGESosPA.submit();
    }
    
  </script>
</head>

<body class="corpo">
 <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Richiesta Sostituzione Pena Accessoria&nbsp;</font>
      </td>
      
      <td class="LBG">
         <select name="comboAction" >
           <option value="siap.siep.modulocumulo.action.ActLoadInserisciDecisioneDelGESostPenaAcc">Inserimento/Modifica Decisione del G.E. </option>
<%		if (RichiestaGE.getDecisioneGeSorvCum()!= null &&
			RichiestaGE.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null ) { %>
           <option value="siap.siep.modulocumulo.action.ActCancellaDecisioneGeSorvDellaRichiesta">Cancella Decisione del G.E. </option>								   
<% 		}%>
         </select>
         <a href="javascript:eseguiFunzioneSelezionata()">
           <img align="middle" src="/images/vedi24.gif" alt="Vai" width="24" height="24" border="0">
         </a>

      </td>
      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
 </FORM>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
      	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  		<br>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>
  
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="indietroForm">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
</form>  
  
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettRichGESosPA">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaGE.getIdRichiestePmInCumulo()%>">
  <input type="hidden" name="nextAction" value="siap.siep.modulocumulo.action.ActDettaglioRichiestaGESostPenaAcc">
  
  <input type="hidden" name="modalita" value="" >

<% 	
  String AnnoNumero ="";
  AnnoNumero = TitoloPA.getAnnoSentenza() +"/"+TitoloPA.getNumeroSentenza();
%> 
  <table width="95%" align="center">
  	<tr><td colspan="1" class="Titolonocap"><font class="campo">Elenco delle PENE ACCESSORIE da Sostituire</font></td></tr>
    <tr><td colspan="1" class="Titolonocap" style="text-align:left" >In relazione al Titolo:</td></tr>
    <tr>
      <td class="l" colspan="1">
        <font class="label"><%=StringUtils.toStringJSP(TitoloPA.getDescrTipoProvvedimento() )%>&nbsp;N. &nbsp; </font>
        <font class="campo"><%=AnnoNumero%></font>&nbsp;
        &nbsp;<font class="label"> del </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloPA.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
        &nbsp;<font class="label"> Emessa da </font>
	 	<font class="campo"><%=StringUtils.toStringJSP(TitoloPA.getDescrTipoAutoritaEmittente(), "") %></font>
        <font class="label">&nbsp;di&nbsp; </font>
        <font class="campo"><%=StringUtils.toStringJSP(TitoloPA.getDescrLuogoEmittente(), "") %></font>

        &nbsp;<font class="label"> Irrevocabile il  </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloPA.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
      </td>
    </tr>

<%	// 	------------		PENE ACCESSORIE		--------------
  if(TitoloPA.getPeneAccessorieCumulo()!=null && TitoloPA.getPeneAccessorieCumulo().size() > 0)
  {
	Iterator itx = TitoloPA.getPeneAccessorieCumulo().iterator();
  	while(itx.hasNext())
  	{	
       PenaAccessoriaCumuloModel lPACum = (PenaAccessoriaCumuloModel)itx.next();%>	
       <tr>
	     <td class="l" >
	       <font class="label" style="color:red; text-align:left"> Pena Accessoria: </font>
	       &nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getDescrTipoPenaAccessoria(),"") %></font>&nbsp;
	       
<%		if(lPACum.getDurata()!= null && !"P".equals(lPACum.getDurata() ) )
		{ 	%>         
         	<font class="label"> per la durata </font>
        <%	if("-".equals(lPACum.getDescrDurata()) ) 
        	{	%>
        		<font class="label"> di: </font>&nbsp;&nbsp; 
        <%	}
        	else
        	{	%>			
         		&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getDescrDurata() )%></font>&nbsp;&nbsp;
		<%  }
        	
        	if(lPACum.getNumAnni()!=null && lPACum.getNumAnni().compareTo(BigDecimal.ZERO) > 0)
	  	  	{ %> 	
          	Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getNumAnni() ) %></font>&nbsp;&nbsp;
         <% }
        	
        	if(lPACum.getNumMesi()!=null && lPACum.getNumMesi().compareTo(BigDecimal.ZERO) > 0)
	   	  	{	%> 	
          	Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getNumMesi() ) %></font>&nbsp;&nbsp;
       <%	}
        	
        	if(lPACum.getNumGiorni()!=null && lPACum.getNumGiorni().compareTo(BigDecimal.ZERO) > 0)
	   	  	{	%>  	
          	Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getNumGiorni() ) %></font>&nbsp;&nbsp;
        <%	} %>  	
<%		}
		else if(lPACum.getDurata()!= null && "P".equals(lPACum.getDurata()) )
   		{	%>
				<font class="label"> Durata: </font>&nbsp;
				<font class="campoLow"><%=StringUtils.toStringJSP(lPACum.getDescrDurata() )%></font>&nbsp;
<%		}	%>	       
  			          
	      </td>
	    </tr>		
		
<%	} // CHIUDE CICLO while sulle P.A. %>

  </table>
<%
  } // Chiude if(lTitolo.getPeneAccessorie()!=null)%>

<!-- 									DATI 	DELLA 	RICHIESTA 								 -->
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della richiesta</td></tr>
    <tr>
      <td class="l" width="200px">Data Richiesta</td>
      <td class="l" >
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaGE.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>
    </tr>  	
    <tr>
      <td class="l" width="200px">Tipo Richiesta</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrTipoAnnotazione() )%></font>
      </td>
    </tr> 
<%	if("025".equals(RichiestaGE.getCodTipoAnnotazione()) )
	{	%>     
	  <tr>
      	<td class="l" width="200px">Estremi Beneficio</td>
      	<td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrBeneficio())%>&nbsp;&nbsp;&nbsp;<%=StringUtils.toStringJSP(RichiestaGE.getDescrDpr())%></font>
      	</td>
      </tr>
<%	} %>      
    <tr>
      <td class="l">Pena Accessoria in Sostituzione</td>
	  <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrTipoPenaAccessoria())%></font>
      </td>	      
    </tr>
	<tr>
      <td class="l">Durata (Tipo / Periodo): </td>
	  <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrTipoDurataPa(), "-")%></font>&nbsp;&nbsp;/&nbsp;
 <%	if(RichiestaGE.getNumAnniPa()!=null && RichiestaGE.getNumAnniPa().compareTo(BigDecimal.ZERO) > 0)
  	{ %> 	
 		Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(RichiestaGE.getNumAnniPa() ) %></font>&nbsp;&nbsp;
<%  }
	
	if(RichiestaGE.getNumMesiPa()!=null && RichiestaGE.getNumMesiPa().compareTo(BigDecimal.ZERO) > 0)
  	{	%> 	
 		Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(RichiestaGE.getNumMesiPa() ) %></font>&nbsp;&nbsp;
<%	}
	
	if(RichiestaGE.getNumGiorniPa()!=null && RichiestaGE.getNumGiorniPa().compareTo(BigDecimal.ZERO) > 0)
  	{	%>  	
 		Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(RichiestaGE.getNumGiorniPa() ) %></font>&nbsp;&nbsp;
<%	} %>       
      </td>	      
    </tr>

<%	if(RichiestaGE.getMotivazioni()!=null && !RichiestaGE.getMotivazioni().equals("") )
	{	%>
	<tr>
      <td class="l" colspan="1">Motivazioni :  </td>
      <td class="l" colspan="3">
      	<font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getMotivazioni(),"" )%></font>
      </td>
    </tr>  		
<%	} %>	    
  </table>

<!-- 									DATI 	INVIO 	RICHIESTA 								 -->  
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
        <td class="l" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lRicInv.getContenuto(),"-")%></font></td>
      </tr>
 <%	  } %>           
  </table>
<%	} %>

<!-- 							DATI 	DELLA 	DECISIONE 	DEL 	G.E. 								 -->
<%	if (RichiestaGE.getDecisioneGeSorvCum()!= null &&
		RichiestaGE.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null ) { 
		ProvvedimentoGeSorvCumModel decisioneGE = RichiestaGE.getDecisioneGeSorvCum();%>
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della Decisione del G.E.</td></tr>
    <tr>
      <td class="l" width="200px">Anno/Numero Procedimento SIGE</td>
      <td class="l" colspan="3">
      	<font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getAnnoProvv() )%></font>
      	&nbsp;/&nbsp;
      	<font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getNumeroProvv() )%></font>
      </td>
    </tr>
    <tr>
      <td class="l" width="200px">Ufficio Emittente </td>
      <td class="l" colspan="3">
      	<font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getDescrUfficioEmittente() )%></font>
      	&nbsp;di&nbsp;
      	<font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getDescrLuogoEmittente() )%></font>
      </td>
    </tr>
    <tr>
      <td class="l" width="200px">Data Emissione </td>
      <td class="l" colspan="3">
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decisioneGE.getDataD(),"dd-MM-yyyy"),"-")%></font>
      </td>
    </tr>  	

<%// Solo per Decisione su Richiesta di 'SOSTITUZIONE PENA ACCESSORIA' ============================================			 --%>
<%
    if("024".equals(RichiestaGE.getCodTipoAnnotazione()) ||
	   "025".equals(RichiestaGE.getCodTipoAnnotazione()) )
    {	  %>
		<tr>
	      <td class="l">Esito :</td>
	 <%	if("C".equals(decisioneGE.getFlagConforme() ))
	 	{ %>   
	  		<td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., CONFERMA la Pena Accessoria </font></td>
	<%	}
	 	else if("D".equals(decisioneGE.getFlagConforme() ))
	 	{	%>
	 		<td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M., CAMBIA la Pena Accessoria </font></td>
	<%	}
	 	else if("I".equals(decisioneGE.getFlagConforme() ))
	 	{	%> 	
			<td class="l" colspan="3"><font class="label" style="color:red" > dichiara INAMMISSIBILE la richiesta del P.M. </font></td>
	<%	}
	 	else if("R".equals(decisioneGE.getFlagConforme() ))
	 	{	%>
	 		<td class="l" colspan="3"><font class="label" style="color:red" > RIGETTA la richiesta del P.M. </font></td>
	<%	}  %>
		</tr>
	
	 	<tr>
	      <td class="l">Pena Accessoria in Sostituzione</td>
		  <td class="l" colspan="3" >
	        <font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getDescrTipoPenaAccessoriaD())%></font>
	      </td>	      
	    </tr>
		<tr>
	      <td class="l">Durata (Tipo / Periodo): </td>
		  <td class="l" colspan="3">
	        <font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getDescrTipoDurataPaD(), "-")%></font>&nbsp;&nbsp;/&nbsp;
	 <%	if(decisioneGE.getNumAnniPaD()!=null && decisioneGE.getNumAnniPaD().compareTo(BigDecimal.ZERO) > 0)
	  	{ %> 	
	 		Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(decisioneGE.getNumAnniPaD() ) %></font>&nbsp;&nbsp;
	<%  }
		
		if(decisioneGE.getNumMesiPaD()!=null && decisioneGE.getNumMesiPaD().compareTo(BigDecimal.ZERO) > 0)
	  	{	%> 	
	 		Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(decisioneGE.getNumMesiPaD() ) %></font>&nbsp;&nbsp;
	<%	}
		
		if(decisioneGE.getNumGiorniPaD()!=null && decisioneGE.getNumGiorniPaD().compareTo(BigDecimal.ZERO) > 0)
	  	{	%>  	
	 		Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(decisioneGE.getNumGiorniPaD() ) %></font>&nbsp;&nbsp;
	<%	} %>       
	      </td>	      
	    </tr>
<% 
    } // chiude if cod = 024

    if(decisioneGE.getMotivazioniD()!=null && !decisioneGE.getMotivazioniD().equals("") )
    {	%>
		<tr>
	      <td class="l" colspan="1">Motivazioni :  </td>
	      <td class="l" colspan="3">
	      	<font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getMotivazioniD(),"" )%></font>
	      </td>
	    </tr>  		
<%	} %>	    
  </table>   
<%
  }	// Chiude if Decisione	%>

</FORM>

</body>
</html>