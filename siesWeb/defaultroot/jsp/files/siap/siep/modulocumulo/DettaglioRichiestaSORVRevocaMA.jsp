<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="RichiestaSORV"	       scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TitoloRichiesta"      scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<!-- 						DettaglioRichiestaSORVRevocaMA						 -->
<% 
//============================================================================ 
// Form per la visualizzazione del dettaglio delle richieste alla SORVEGLIANZA; 
// Tipo Richiesta = Revoca M.A. (Cod = 031)
//============================================================================ 
RichiesteInviateCumModel lRicInv = null;
if(RichiestaSORV!=null && RichiestaSORV.getRichiesteInviateCum()!=null)
{
	lRicInv = (RichiesteInviateCumModel)RichiestaSORV.getRichiesteInviateCum();
}

ProvvedimentoGeSorvCumModel decisioneSORV = null;
Boolean lArresto = false;
Boolean lReclusione = false;
Boolean lDecisione = false;
if (RichiestaSORV.getDecisioneGeSorvCum()!= null &&
	RichiestaSORV.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null ) 
{ 
	decisioneSORV = (ProvvedimentoGeSorvCumModel) RichiestaSORV.getDecisioneGeSorvCum();
	lDecisione=true;
	
	if(	(decisioneSORV.getNumAnniArrestoD()!=null && decisioneSORV.getNumAnniArrestoD().compareTo(BigDecimal.ZERO) > 0) ||
   		(decisioneSORV.getNumMesiArrestoD()!=null && decisioneSORV.getNumMesiArrestoD().compareTo(BigDecimal.ZERO) > 0) || 
   		(decisioneSORV.getNumGiorniArrestoD()!=null && decisioneSORV.getNumGiorniArrestoD().compareTo(BigDecimal.ZERO) > 0) )
	{
		lArresto = true;
	}
	
	if(	(decisioneSORV.getNumAnniReclusioneD()!=null && decisioneSORV.getNumAnniReclusioneD().compareTo(BigDecimal.ZERO) > 0) ||
   		(decisioneSORV.getNumMesiReclusioneD()!=null && decisioneSORV.getNumMesiReclusioneD().compareTo(BigDecimal.ZERO) > 0) || 
   		(decisioneSORV.getNumGiorniReclusioneD()!=null && decisioneSORV.getNumGiorniReclusioneD().compareTo(BigDecimal.ZERO) > 0) )
	{
		lReclusione = true;
	}
	
}
%>
<html>
<head>
  <title> Gestione Richieste alla SORVEGLIANZA - Revoca Misura Alternativa</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.DettRichSORVRevoMa.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichSORVRevoMa.submit();
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
	      		document.DettRichSORVRevoMa.<%=IWebConstants.ACTION_FIELD%>.value = action;
	            document.DettRichSORVRevoMa.modalita.value="C";
	      	    document.DettRichSORVRevoMa.submit();
	    	}
	  	 } 
	  	 else 
	  	 {	
	  		document.DettRichSORVRevoMa.<%=IWebConstants.ACTION_FIELD%>.value = action;
	        document.DettRichSORVRevoMa.modalita.value="I";
	  	    document.DettRichSORVRevoMa.submit();
	     }
    }
    
    function vaiaDecisione(action)
    {
      document.DettRichSORVRevoMa.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichSORVRevoMa.modalita.value="I";
      document.DettRichSORVRevoMa.submit();
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
        <font class="campo">Dettaglio Richiesta alla SORVEGLIANZA - Revoca Misura Alternativa&nbsp;</font>
      </td>
      
      <td class="LBG">
         <select name="comboAction" >
           <option value="siap.siep.modulocumulo.action.ActLoadInserisciDecisioneDellaSORVRevocaMA">Inserimento/Modifica Decisione della SORVEGLIANZA </option>
<%		if (RichiestaSORV.getDecisioneGeSorvCum()!= null &&
			RichiestaSORV.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null ) { %>
           <option value="siap.siep.modulocumulo.action.ActCancellaDecisioneGeSorvDellaRichiesta">Cancella Decisione della SORVEGLIANZA </option>								   
<% 		}%>
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
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
      	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  		<br>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>
  
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettRichSORVRevoMa">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaSORV.getIdRichiestePmInCumulo()%>">
  <input type="hidden" name="nextAction" value="siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVRevocaMA">
  <input type="hidden" name="modalita" value="" >

<% 	
  String AnnoNumero ="";
  AnnoNumero = TitoloRichiesta.getAnnoSentenza() +"/"+TitoloRichiesta.getNumeroSentenza();
%> 
  <table width="95%" align="center">
    <tr><td colspan="1" class="Titolonocap" style="text-align:left" >In relazione al Titolo:</td></tr>
    <tr>
      <td class="l" colspan="1">
        <font class="label"><%=StringUtils.toStringJSP(TitoloRichiesta.getDescrTipoProvvedimento() )%>&nbsp;N. &nbsp; </font>
        <font class="campo"><%=AnnoNumero%></font>&nbsp;
        &nbsp;<font class="label"> del </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloRichiesta.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
        &nbsp;<font class="label"> Emessa da </font>
	 	<font class="campo"><%=StringUtils.toStringJSP(TitoloRichiesta.getDescrTipoAutoritaEmittente(), "") %></font>
        <font class="label">&nbsp;di&nbsp; </font>
        <font class="campo"><%=StringUtils.toStringJSP(TitoloRichiesta.getDescrLuogoEmittente(), "") %></font>

        &nbsp;<font class="label"> Irrevocabile il  </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloRichiesta.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
      </td>
    </tr>
    
    <%	StatoEsecTitoloCumulatoModel lStatoMod = (StatoEsecTitoloCumulatoModel)TitoloRichiesta.getStatoEsecTitoloCumulato();  %>
	  
	  <tr>
      	<td class="l" >
          <font class="label" style="color:red; text-align:left"> Misura Concessa: </font>
          &nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lStatoMod.getDescrMotivo(),"") %></font>&nbsp;
          
          <font class="label"> Concessa da </font>
          <font class="campo"><%=StringUtils.toStringJSP(lStatoMod.getDescrUfficioEmittente(), "") %></font>
          <font class="label"> di </font>
      	  <font class="campo"><%=StringUtils.toStringJSP(lStatoMod.getDescrLuogoEmittente(), "") %></font>
      	  <font class="label">&nbsp;con </font>
      	  <font class="campo"><%=StringUtils.toStringJSP(lStatoMod.getDescrTipoProvvedimento(), "") %></font>
      	  <font class="label"> del </font>
      	  <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoMod.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>&nbsp;
        </td>
      </tr>
  </table>

<!-- 									DATI 	DELLA 	RICHIESTA 								 -->
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della richiesta</td></tr>
    <tr>
      <td class="l" width="250px">Data Richiesta</td>
      <td class="l" >
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaSORV.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>
    </tr>  	
    <tr>
      <td class="l" width="250px">Tipo Richiesta</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaSORV.getDescrTipoAnnotazione() )%></font>
      </td>
    </tr>
    
    <tr>
      <td class="l" width="250px" >Si richiede la Revoca della Misura Alternativa</td>
      <td class="L" >
      	<font class="campo"><%=StringUtils.toStringJSP(lStatoMod.getDescrMotivo(),"") %></font>
      </td>
    </tr> 

<%	if(RichiestaSORV.getMotivazioni()!=null && !RichiestaSORV.getMotivazioni().equals("") )
	{	%>
	<tr>
      <td class="l" colspan="1">Motivazioni :  </td>
      <td class="l" colspan="3">
      	<font class="campo"><%=StringUtils.toStringJSP(RichiestaSORV.getMotivazioni(),"" )%></font>
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
 	<tr><td> </td></tr>           
  </table>
<%	} %>

<!-- 							DATI 	DELLA 	DECISIONE 	DELLA 	SORVEGLIANZA 								 -->
<%	if(lDecisione) 
	{	%>
	  <table width="95%" align="center">
	    <tr><td colspan="4" class="Titolonocap">Dati della Decisione della SORVEGLIANZA</td></tr>
	    <tr>
	      <td class="l" width="200px">Tipo Provvedimento</td>
	      <td class="l" colspan="3">
	      	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getDescrTipoProvvedimento() )%></font>
	      </td>
	    </tr>
 <%	 if(decisioneSORV.getNumeroProvv()!=null)
	 {	%>	    
	    <tr>
	      <td class="l" width="200px">Anno/Numero Provvedimento </td>
	      <td class="l" colspan="3">
	      	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getAnnoProvv() )%></font>
	      	&nbsp;/&nbsp;
	      	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getNumeroProvv() )%></font>
	      </td>
	    </tr>
 <%	 } %>
 
 <%	 if(decisioneSORV.getNumeroSIUS()!=null)
	 {	%>		    
	    <tr>
	      <td class="l" width="200px">Anno/Numero Procedimento SIUS</td>
	      <td class="l" colspan="3">
	      	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getAnnoSIUS() )%></font>
	      	&nbsp;/&nbsp;
	      	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getNumeroSIUS() )%></font>
	      </td>
	    </tr>
 <%	 } %>	    
	    <tr>
	      <td class="l" width="200px">Ufficio Emittente </td>
	      <td class="l" colspan="3">
	      	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getDescrUfficioEmittente() )%></font>
	      	&nbsp;di&nbsp;
	      	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getDescrLuogoEmittente() )%></font>
	      </td>
	    </tr>
	    <tr>
	      <td class="l" width="200px">Data Emissione </td>
	      <td class="l" colspan="3">
	      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decisioneSORV.getDataD(),"dd-MM-yyyy"),"-")%></font>
	      </td>
	    </tr>  	
	
		<tr>
	      <td class="l">Esito :</td>
	 <%	if("C".equals(decisioneSORV.getFlagConforme() ))
	 	{ %>   
	  		<td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., REVOCA la Misura</font></td>
	<%	}
	 	else if("D".equals(decisioneSORV.getFlagConforme() ))
	 	{	%>
	 		<td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M. </font></td>
	<%	}
	 	else if("I".equals(decisioneSORV.getFlagConforme() ))
	 	{	%> 	
			<td class="l" colspan="3"><font class="label" style="color:red" > dichiara INAMMISSIBILE la richiesta del P.M. </font></td>
	<%	}
	 	else if("R".equals(decisioneSORV.getFlagConforme() ))
	 	{	%>
	 		<td class="l" colspan="3"><font class="label" style="color:red" > RIGETTA la richiesta del P.M. </font></td>
	<%	}  %>
		</tr>
		
	<%	Boolean lscrivi=false;
		if(lArresto || lReclusione)
		{	 %>	
		<tr>
		  <td class="l" colspan="1">Quantifica la pena in: </td>
		  <td class="l" colspan="3">
	<%	 
		  if(decisioneSORV.getNumAnniArrestoD()!=null && decisioneSORV.getNumAnniArrestoD().compareTo(BigDecimal.ZERO) > 0 )
		  {	
		  	lscrivi=true;	%>
			<font class="label"> Anni </font>
        	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getNumAnniArrestoD(), " - " ) %></font>&nbsp;
    <%	  }
			
		  if(decisioneSORV.getNumMesiArrestoD()!=null && decisioneSORV.getNumMesiArrestoD().compareTo(BigDecimal.ZERO) > 0 )
	  	  {	
	  	  	lscrivi=true; %>
	  	  	<font class="label"> Mesi </font>
        	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getNumMesiArrestoD(), " - " ) %></font>&nbsp;
    <%	  }
		  
    	  if(decisioneSORV.getNumGiorniArrestoD()!=null && decisioneSORV.getNumGiorniArrestoD().compareTo(BigDecimal.ZERO) > 0 ) 
    	  {	
    	  	lscrivi=true; %>    	
	  	    <font class="label"> Giorni </font>
        	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getNumGiorniArrestoD(), " - " ) %></font>&nbsp;  
    <%	  } 
    
    	  if(lscrivi)
    	  {	%>
    		<font class="label"> di Arresto </font>&nbsp;&nbsp;&nbsp;
    <%	  }
    	  
    	  lscrivi=false; %>
    	  	
   <%	  // RECLUSIONE
   		  if(decisioneSORV.getNumAnniReclusioneD()!=null && decisioneSORV.getNumAnniReclusioneD().compareTo(BigDecimal.ZERO) > 0 )
		  {	
		  	lscrivi=true;	%>
			<font class="label"> Anni </font>
        	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getNumAnniReclusioneD(), " - " ) %></font>&nbsp;
    <%	  }
			
		  if(decisioneSORV.getNumMesiReclusioneD()!=null && decisioneSORV.getNumMesiReclusioneD().compareTo(BigDecimal.ZERO) > 0 )
	  	  {	
	  	  	lscrivi=true; %>
	  	  	<font class="label"> Mesi </font>
        	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getNumMesiReclusioneD(), " - " ) %></font>&nbsp;
    <%	  }
		  
    	  if(decisioneSORV.getNumGiorniReclusioneD()!=null && decisioneSORV.getNumGiorniReclusioneD().compareTo(BigDecimal.ZERO) > 0 ) 
    	  {	
    	  	lscrivi=true; %>    	
	  	    <font class="label"> Giorni </font>
        	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getNumGiorniReclusioneD(), " - " ) %></font>&nbsp;  
    <%	  } 
    
    	  if(lscrivi)
    	  {	%>
    		<font class="label"> di Reclusione </font>&nbsp;&nbsp;&nbsp;
    <%	  } 	  			
    	
		}	%>
    	</td>
      </tr>		    	 		  
	<% 
	  	if(decisioneSORV.getMotivazioniD()!=null && !decisioneSORV.getMotivazioniD().equals("") )
	  	{	%>
		<tr>
		  <td class="l" colspan="1">Motivazioni :  </td>
		  <td class="l" colspan="3">
		   	<font class="campo"><%=StringUtils.toStringJSP(decisioneSORV.getMotivazioniD(),"" )%></font>
		  </td>
		</tr>  		
	<%	} %>
	
		<tr>
	      <td class="l" width="200px">Data Decorrenza Revoca </td>
	      <td class="l" colspan="3">
	      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decisioneSORV.getDataRevoca(),"dd-MM-yyyy"),"-")%></font>
	      </td>
	    </tr> 
		    
  	  </table>   
<%
  	}	// Chiude if Decisione	%>

</FORM>

</body>
</html>