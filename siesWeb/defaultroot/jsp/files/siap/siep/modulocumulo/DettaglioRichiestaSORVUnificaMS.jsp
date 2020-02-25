<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>

<%@page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="RichiestaSORV"	       scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TitoliRichiesta"      scope="request" class="java.util.Vector"/>
<jsp:useBean id="MisureSicurezza"      scope="request" class="java.util.Vector"/>

<jsp:useBean id="DecisioneSorv"	   	   scope="request" class="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"/>

<jsp:useBean id="UfficioEmittente" scope="request" class="java.lang.String" />

<% 
//============================================================================== 
// Form per la visualizzazione del dettaglio delle richieste alla SORV di 
// Unificazione Misure di sicurezza.
//============================================================================== 

RichiesteInviateCumModel lRicInv = null;
if(RichiestaSORV!=null && RichiestaSORV.getRichiesteInviateCum()!=null)
{
	lRicInv = (RichiesteInviateCumModel)RichiestaSORV.getRichiesteInviateCum();
}

%>

<html>
<head>
  <title> Gestione Richieste del PM alla SORV - Unificazione M.S.</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.DettRichSORV.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichSORV.submit();
    }

  function eseguiFunzioneSelezionata()
  {
	var action = document.f.comboAction[document.f.comboAction.selectedIndex].value ;

	if( action.indexOf('Cancella') >= 0){
        if ( !window.confirm("Si vuole procedere alla cancellazione della decisione?") ) {
            return;
        } else {
    		document.DettRichSORV.<%=IWebConstants.ACTION_FIELD%>.value = action;
    	    document.DettRichSORV.submit();
        }
	} else {	
		document.DettRichSORV.<%=IWebConstants.ACTION_FIELD%>.value = action;
	    document.DettRichSORV.submit();
  	}
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
	        <font class="campo">Dettaglio Richiesta Unificazione Misure di Sicurezza&nbsp;</font>
	      </td>
	
	       <td class="LBG">
	         <select name="comboAction" >
	           <option value="siap.siep.modulocumulo.action.ActLoadInserisciDecisioneDellaSorvUnificaMS">Inserimento/Modifica Decisione della Sorveglianza </option>								   
<%				if (DecisioneSorv!= null &&
					DecisioneSorv.getIdProvvedimentoGeSorvCum() != null ) { %>
	           	<option value="siap.siep.modulocumulo.action.ActCancellaDecisioneGeSorvDellaRichiesta">Cancella Decisione della Sorveglianza </option>								   
<% 				}%>
	         </select>
	         <a href="javascript:eseguiFunzioneSelezionata()">
	           <img align="middle" src="/images/vedi24.gif" alt="Vai" width="24" height="24" border="0">
	         </a>
	      </td>
	      <td class="LBG">
	        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV')">
	          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
	        </a>
	      </td>
	    </tr>
	  </table>
	</FORM>
  <br>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
      	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  		<br>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>
  
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettRichSORV">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaSORV.getIdRichiestePmInCumulo()%>">
  <input type="hidden" name="nextAction" value="siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVUnificaMS">

<% 	
  //============================================================================
  // Sezione relativa ai Titoli con Provvedimenti di Misure di Sicurezza
  //============================================================================
  String DescProvv="";
  String DescAuto="";
  String AnnoNumero ="";
  Iterator itx = TitoliRichiesta.iterator();
  while(itx.hasNext())
  {	
    TitoloCumulatoModel lTitolo = (TitoloCumulatoModel)itx.next();
    
    DescProvv = lTitolo.getDescrTipoProvvedimento() +" N. "+lTitolo.getAnnoSentenza() +"/"+lTitolo.getNumeroSentenza();
    AnnoNumero = lTitolo.getAnnoSentenza() +"/"+lTitolo.getNumeroSentenza();
 	DescAuto = lTitolo.getDescrTipoAutoritaEmittente() +" di "+lTitolo.getDescrLuogoEmittente();
%> 

   <table width="95%" align="center">
    <tr>
    
    <table width="95%" align="center">
      <tr>
   	    <td class="titolo" width="90%">In relazione al Titolo</td>
      </tr>
      
      <tr>
      <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO%>" value="<%=StringUtils.toStringJSP(lTitolo.getIdTitoloCumulato()) %>" >
<%		DescProvv = lTitolo.getDescrTipoProvvedimento() +" N. "+lTitolo.getAnnoSentenza() +"/"+lTitolo.getNumeroSentenza();
		DescAuto = lTitolo.getDescrTipoAutoritaEmittente() +" di "+lTitolo.getDescrLuogoEmittente();
%>        
		<td class="l">
          <font class="label"> Provvedimento </font>
          <font class="campo"><%=DescProvv%> </font>&nbsp;
          <font class="label"> del </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
          <font class="label"> Emessa da </font>
          <font class="campo"><%=DescAuto%></font>&nbsp;
          &nbsp;<font class="label"> Irrevocabile il </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
        </td>
      </tr>
    </table>
    
<%	// -----------------------	Misure di Sicurezza del Titolo ----------------------------------------- %>  
    <table width="95%" align="center">
      <tr>
        <td class="titolo" width="30%">Tipo Misura</td>
        <td class="titolo" width="5%">Durata</td>
      </tr>          
<%
		Iterator itxMS = MisureSicurezza.iterator();
		while(itxMS.hasNext()) {
		    MisuraSicurezzaCumuloModel lMSCum = (MisuraSicurezzaCumuloModel)itxMS.next();
			if(lMSCum.getTitIdTitoloCumulato().compareTo(lTitolo.getIdTitoloCumulato() )==0 )
			{	
%>
			    <tr>
			      <td class="c"  >&nbsp;<font class="campoLow"><%=lMSCum.getDescrTipo()%></font></td>
			      <td class="C" >
			        <% if (lMSCum.getNumAnni()!=null) { %>
			          Anni:&nbsp;<%=StringUtils.toStringJSP(lMSCum.getNumAnni() )%>  
			          <% } %>
			        <% if (lMSCum.getNumMesi()!=null) { %>
			          &nbsp;&nbsp;Mesi:&nbsp;<%=StringUtils.toStringJSP(lMSCum.getNumMesi() )%>  
			          <% } %>
			        <% if (lMSCum.getNumGiorni()!=null) { %>
			          &nbsp;&nbsp;Giorni:&nbsp;<%=StringUtils.toStringJSP(lMSCum.getNumGiorni() )%>  
			          <% } %>
			      </td>
			    </tr>
      		
<%			} 
		}  %>
	 </table>

<% 	}  // CHIUDE CICLO while sui Titoli		%> 
  
   </tr>  
  </table>   <%// CHIUDE la TABELLA relativa ad 1 Titolo %>
  <br>
  
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della richiesta</td></tr>
    <tr>
      <td class="l" width="200px">Data Richiesta</td>
      <td class="l" colspan="2">
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaSORV.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>	
    </tr>
    <tr>
      <td class="l" width="200px">Motivazioni : </td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaSORV.getMotivazioni(),"-" )%></font>
      </td>
    </tr>

  </table>
  
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
  

<!-- 							DATI 	DELLA 	DECISIONE 	DELLA 	SORV. 								 -->
<%	if (DecisioneSorv!= null &&
		DecisioneSorv.getIdProvvedimentoGeSorvCum() != null ) { 
%>
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della Decisione della Sorveglianza </td></tr>
    <tr>
      <td class="l">Anno/Numero Provvedimento</td>
      <td class="l">
      	<font class="campo"><%=StringUtils.toStringJSP(DecisioneSorv.getAnnoProvv() )%></font>
      	&nbsp;/&nbsp;
      	<font class="campo"><%=StringUtils.toStringJSP(DecisioneSorv.getNumeroProvv() )%></font>
      </td>

      <td class="l" width="240px">Anno/Numero Procedimento SIUS</td>
      <td class="l" >
      	<font class="campo"><%=StringUtils.toStringJSP(DecisioneSorv.getAnnoSIUS() )%></font>
      	&nbsp;/&nbsp;
      	<font class="campo"><%=StringUtils.toStringJSP(DecisioneSorv.getNumeroSIUS() )%></font>
      </td>
    </tr>
    <tr>
      <td class="l" width="200px">Ufficio Emittente </td>
      <td class="l" colspan="3">
      	<font class="campo"><%=StringUtils.toStringJSP(DecisioneSorv.getDescrUfficioEmittente() )%></font>
      	&nbsp;di&nbsp;
      	<font class="campo"><%=StringUtils.toStringJSP(DecisioneSorv.getDescrLuogoEmittente() )%></font>
      </td>
    </tr>
    <tr>
      <td class="l" width="200px">Data Emissione </td>
      <td class="l" colspan="3">
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(DecisioneSorv.getDataD(),"dd-MM-yyyy"),"-")%></font>
      </td>
    </tr>  	

    <tr>
      <td class="l">Esito :</td>

 <%	if(RichiestaSORV.getCodTipoAnnotazione().compareTo("020")==0) {
      
    if("C".equals(DecisioneSorv.getFlagConforme() ))
 	{ 
 		if( "-".equals(DecisioneSorv.getFlagPiuMenoD() ) )
 		{	%>     
      		<td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., DIMINUISCE la Pena </font></td>
<%		}
 		else if( "+".equals(DecisioneSorv.getFlagPiuMenoD() ))
 		{ %>
 			<td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., AUMENTA la Pena </font></td>
<%		}
 		else
 		{	%> 		      		
			<td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., CONCEDE </font></td>
<%		}
	}
 	else if("D".equals(DecisioneSorv.getFlagConforme() ))
 	{ 
 		if( "-".equals(DecisioneSorv.getFlagPiuMenoD() ))
 		{	%>     
      		<td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M., DIMINUISCE la Pena </font></td>
<%		}
 		else if( "+".equals(DecisioneSorv.getFlagPiuMenoD() ))
 		{ %>
 			<td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M., AUMENTA la Pena </font></td>
<%		}
 		else
 		{	%> 		      		
			<td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M., CONCEDE </font></td>
<%		}
  	}
   }
   if(RichiestaSORV.getCodTipoAnnotazione().compareTo("022")==0) {
    if("C".equals(DecisioneSorv.getFlagConforme() ))
 	{ %>
		<td class="l" colspan="3"><font class="label"> Unifica le Misure </font></td>
<%	}

 	else if("D".equals(DecisioneSorv.getFlagConforme() ))
 	{ %>
		<td class="l" colspan="3"><font class="label"> Non Unifica le Misure</font></td>
<%	}
   }
     
	if("I".equals(DecisioneSorv.getFlagConforme() ) )
 	{ %>     
		<td class="l" colspan="3"><font class="label" style="color:red" > dichiara INAMMISSIBILE la richiesta del P.M. </font></td>
<%	}
	else if("R".equals(DecisioneSorv.getFlagConforme() ))
	{  %>
		<td class="l" colspan="3"><font class="label" style="color:red" > RIGETTA la richiesta del P.M. </font></td>
<% 	} %>
	 	      
    </tr>


<%  if(RichiestaSORV.getCodTipoAnnotazione().compareTo("022")==0) { %>
    </tr>
		<td class="l"><font class="label"> Tipo Misura</font></td>
	    <td class="l">
	      	<font class="campo"><%=StringUtils.toStringJSP(DecisioneSorv.getDescrTipoMsD(),"" ) %></font>
	    </td>
		<td class="l">
			<font class="label"> Durata     Anni </font>
	      	<font class="campo"><%=StringUtils.toStringJSP(DecisioneSorv.getNumAnniMsD(),"" )%></font>&nbsp;&nbsp;&nbsp;&nbsp;
			<font class="label"> Mesi </font>
	      	<font class="campo"><%=StringUtils.toStringJSP(DecisioneSorv.getNumMesiMsD(),"" )%></font>&nbsp;&nbsp;&nbsp;&nbsp;
			<font class="label"> Giorni </font>
	      	<font class="campo"><%=StringUtils.toStringJSP(DecisioneSorv.getNumGiorniMsD(),"" )%></font>
		</td>
		
<%	}%>

<%	if(DecisioneSorv.getMotivazioniD()!=null && !DecisioneSorv.getMotivazioniD().equals("") )
	{	%>
	<tr>
	     <td class="l" colspan="1">Motivazioni :  </td>
	     <td class="l" colspan="3">&nbsp;
	      	<font class="campo"><%=StringUtils.toStringJSP(DecisioneSorv.getMotivazioniD(),"" )%></font>
	     </td>
	</tr>  		
<%	} %>	    
  </table>   
<%
  }	// Chiude if Decisione	%>




</FORM>

</body>
</html>