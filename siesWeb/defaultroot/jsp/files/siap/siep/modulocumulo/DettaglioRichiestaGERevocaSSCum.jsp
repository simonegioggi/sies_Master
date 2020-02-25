<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="RichiestaGE"	       scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TitoliRichiesta"      scope="request" class="java.util.Vector"/>

<!-- 						DettaglioRichiestaGERevocaSSCum						 -->
<% 
//============================================================================ 
// Form per la visualizzazione del dettaglio delle richieste al GE di 
// Revoca Sanzione Sostitutiva
//============================================================================ 
RichiesteInviateCumModel lRicInv = null;
if(RichiestaGE!=null && RichiestaGE.getRichiesteInviateCum()!=null)
{
	lRicInv = (RichiesteInviateCumModel)RichiestaGE.getRichiesteInviateCum();
}
%>
<html>
<head>
  <title> Gestione Richieste al GE - Revoca Sanzione Sost Cumulo</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.DettRichGERevocaSS.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichGERevocaSS.submit();
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
	      		document.DettRichGERevocaSS.<%=IWebConstants.ACTION_FIELD%>.value = action;
	            document.DettRichGERevocaSS.modalita.value="C";
	      	    document.DettRichGERevocaSS.submit();
	    	}
	  	 } 
	  	 else 
	  	 {	
	  		document.DettRichGERevocaSS.<%=IWebConstants.ACTION_FIELD%>.value = action;
	        document.DettRichGERevocaSS.modalita.value="I";
	  	    document.DettRichGERevocaSS.submit();
	     }
    }
    
    function vaiaDecisione(action)
    {
      document.DettRichGERevocaSS.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichGERevocaSS.modalita.value="I";
      document.DettRichGERevocaSS.submit();
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
        <font class="campo">Dettaglio Richiesta Revoca Sanzione Sostitutiva&nbsp;</font>
      </td>
      
       <td class="LBG">
         <select name="comboAction" >
           <option value="siap.siep.modulocumulo.action.ActLoadInserisciDecisioneDelGERevocaPenaPrincCum">Inserimento/Modifica Decisione del G.E. </option>
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
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
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
  
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettRichGERevocaSS">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaGE.getIdRichiestePmInCumulo()%>">
  <input type="hidden" name="nextAction" value="siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaSSCum">
  <input type="hidden" name="modalita" value="" >

<% 	
  String AnnoNumero ="";
  Iterator itx = TitoliRichiesta.iterator();
  while(itx.hasNext())
  {	
    TitoloCumulatoModel lTitolo = (TitoloCumulatoModel)itx.next();
    
    AnnoNumero = lTitolo.getAnnoSentenza() +"/"+lTitolo.getNumeroSentenza();
%> 
  <table width="95%" align="center">
    <tr><td colspan="1" class="Titolonocap">In relazione al Titolo</td></tr>
    <tr>
      <td class="l" colspan="1">
        <font class="label"><%=StringUtils.toStringJSP(lTitolo.getDescrTipoProvvedimento() )%>&nbsp;N. &nbsp; </font>
        <font class="campo"><%=AnnoNumero%></font>&nbsp;
        &nbsp;<font class="label"> del </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
        &nbsp;<font class="label"> Emessa da </font>
	 	<font class="campo"><%=StringUtils.toStringJSP(lTitolo.getDescrTipoAutoritaEmittente(), "") %></font>
        <font class="label">&nbsp;di&nbsp; </font>
        <font class="campo"><%=StringUtils.toStringJSP(lTitolo.getDescrLuogoEmittente(), "") %></font>

        &nbsp;<font class="label"> Irrevocabile il  </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
      </td>
    </tr>

<%	// 	------------		SANZIONE SOSTITUTIVA		--------------
	if(lTitolo.getSanzioneSostitutivaCumulo()!=null && lTitolo.getSanzioneSostitutivaCumulo().getIdSanzioneSostitutivaCum() != null)
	{
		SanzioneSostitutivaCumuloModel lSSCum = (SanzioneSostitutivaCumuloModel) lTitolo.getSanzioneSostitutivaCumulo();		%>	
	   <tr>
	      <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_SANZIONE_SOST_SELEZIONATA %>" value="<%=StringUtils.toStringJSP(lSSCum.getIdSanzioneSostitutivaCum()) %>" >
	      <td class="l" >
	         <font class="label" style="color:red; text-align:left"> Sanzione Sostitutiva: </font>
	         &nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lSSCum.getDescrTipoSanzione(),"") %></font>&nbsp;
	<%		if(!"P".equals(lSSCum.getCodTipoSanzione()) )
			{ 	%>         
	         	<font class="label"> per la durata di </font>&nbsp;&nbsp;
	          	Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lSSCum.getNumAnni(), "-") %></font>&nbsp;&nbsp;
	          	Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lSSCum.getNumMesi(), "-") %></font>&nbsp;&nbsp;
	          	Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lSSCum.getNumGiorni(), "-") %></font>&nbsp;&nbsp;
	<%		}
			else
			{	
				if(lSSCum.getSanzionePecuniariaMulta() !=null && lSSCum.getSanzionePecuniariaMulta().compareTo(BigDecimal.ZERO ) > 0 )
				{		%>
					&nbsp;<font class="label"> Multa </font>&nbsp;
					<font class="campoLow"><%=StringUtils.toEuroFormat(lSSCum.getSanzionePecuniariaMulta()) %></font>&nbsp;&euro;&nbsp;
	<%			}
				
				if(lSSCum.getSanzionePecuniariaAmmenda() != null && lSSCum.getSanzionePecuniariaAmmenda().compareTo(BigDecimal.ZERO) > 0 )
				{	%>				
					&nbsp;<font class="label"> Ammenda </font>&nbsp;
					<font class="campoLow"><%=StringUtils.toEuroFormat(lSSCum.getSanzionePecuniariaAmmenda()) %></font>&nbsp;&euro;&nbsp;
	<%			}
  			}	%>	
  			          
	      </td>
	    </tr>		
		
<%	} // Chiude if(lTitolo.getSanzioneSostitutivaCumulo()) %>

  </table>
<%
  } // CHIUDE CICLO while sui Titoli %>

<!-- 									DATI 	DELLA 	RICHIESTA 								 -->
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della richiesta</td></tr>
    <tr>
      <td class="l" width="200px">Data Richiesta</td>
      <td class="l" >
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaGE.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>	
    <tr>
      <td class="l" width="200px">Computo beneficio :</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrTipoAnnotazione() )%></font>
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
         <%					if(RichiestaGE.getImportoMultaR()!=null && RichiestaGE.getImportoMultaR().compareTo(BigDecimal.ZERO ) > 0 )
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

    <tr>
      <td class="l">Esito :</td>
 <%	if("C".equals(decisioneGE.getFlagConforme() ))
 	{ 
 		if( "-".equals(decisioneGE.getFlagPiuMenoD() ))
 		{	%>     
      		<td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., DIMINUISCE la Pena </font></td>
<%		}
 		else if( "+".equals(decisioneGE.getFlagPiuMenoD() ))
 		{ %>
 			<td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., AUMENTA la Pena </font></td>
<%		}
 		else
 		{	%> 		      		
			<td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., CONCEDE </font></td>
<%		}
	}
 	else if("D".equals(decisioneGE.getFlagConforme() ))
 	{ 
 		if( "-".equals(decisioneGE.getFlagPiuMenoD() ))
 		{	%>     
      		<td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M., DIMINUISCE la Pena </font></td>
<%		}
 		else if( "+".equals(decisioneGE.getFlagPiuMenoD() ))
 		{ %>
 			<td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M., AUMENTA la Pena </font></td>
<%		}
 		else
 		{	%> 		      		
			<td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M., CONCEDE </font></td>
<%		}
  	}
	else if("I".equals(decisioneGE.getFlagConforme() ))
 	{ %>     
		<td class="l" colspan="3"><font class="label" style="color:red" > dichiara INAMMISSIBILE la richiesta del P.M. </font></td>
<%	}
	else if("R".equals(decisioneGE.getFlagConforme() ))
	{  %>
		<td class="l" colspan="3"><font class="label" style="color:red" > RIGETTA la richiesta del P.M. </font></td>
<% 	} %>
	 	      
    </tr>

    <tr>
      <td class="l" colspan="1">Reclusione :  </td>
      <td class="l" colspan="3">&nbsp;
      	<font class="campo" style="color:red"><%=StringUtils.toStringJSP(decisioneGE.getFlagPiuMenoD(),"")%></font>&nbsp;&nbsp;
      	<font class="label"> Anni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getNumAnniReclusioneD(), " - ") %></font>&nbsp;
		<font class="label"> Mesi </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getNumMesiReclusioneD(), " - ") %></font>&nbsp;
        <font class="label"> Giorni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getNumGiorniReclusioneD(), " - ") %></font>&nbsp;
         &nbsp;&nbsp;&nbsp;	<font class="label"> Multa : </font>&nbsp;
         <%					if(decisioneGE.getImportoMultaD()!=null && decisioneGE.getImportoMultaD().compareTo(BigDecimal.ZERO) > 0 )
         					{	%>
        						<font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getImportoMultaD()) %></font>&nbsp;&euro;&nbsp;
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
      	<font class="campo" style="color:red"><%=StringUtils.toStringJSP(decisioneGE.getFlagPiuMenoD(),"")%></font>&nbsp;&nbsp;
      	<font class="label"> Anni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getNumAnniArrestoD(), " - " ) %></font>&nbsp;
		<font class="label"> Mesi </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getNumMesiArrestoD(), " - ") %></font>&nbsp;
        <font class="label"> Giorni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getNumGiorniArrestoD(), " - ") %></font>&nbsp;
         &nbsp;&nbsp;&nbsp;	<font class="label"> Ammenda : </font>&nbsp;
         <%					if(decisioneGE.getImportoAmmendaD()!=null && decisioneGE.getImportoAmmendaD().compareTo(BigDecimal.ZERO) > 0 )
         					{	%>
        						<font class="campo"><%=StringUtils.toStringJSP(decisioneGE.getImportoAmmendaD()) %></font>&nbsp;&euro;&nbsp;
        			<%		}
         					else
         					{	%>
         						<font class="campo">&nbsp; - &nbsp; </font>
         				<%	}	 %>				

      </td>
    </tr>

<% 		if(decisioneGE.getMotivazioniD()!=null && !decisioneGE.getMotivazioniD().equals("") )
		{	%>
		<tr>
	      <td class="l" colspan="1">Motivazioni :  </td>
	      <td class="l" colspan="3">&nbsp;
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