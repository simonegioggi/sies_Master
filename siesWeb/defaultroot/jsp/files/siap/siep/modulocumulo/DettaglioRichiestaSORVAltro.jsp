<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="RichiestaSORV"	       scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="Titolo"		       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<!-- 						DettaglioRichiestaSORVAltro						 -->
<% 
//============================================================================ 
// Form per la visualizzazione del dettaglio delle richieste alla SORVEGLIANZA; 
// Tipo Richiesta = Altro (Cod = 014)
//============================================================================ 
RichiesteInviateCumModel lRicInv = null;
if(RichiestaSORV!=null && RichiestaSORV.getRichiesteInviateCum()!=null)
{
	lRicInv = (RichiesteInviateCumModel)RichiestaSORV.getRichiesteInviateCum();
}
%>
<html>
<head>
  <title> Gestione Richieste alla SORVEGLIANZA - Altre Richieste</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.DettRichSORValtro.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichSORValtro.submit();
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
	      		document.DettRichSORValtro.<%=IWebConstants.ACTION_FIELD%>.value = action;
	            document.DettRichSORValtro.modalita.value="C";
	      	    document.DettRichSORValtro.submit();
	    	}
	  	 } 
	  	 else 
	  	 {	
	  		document.DettRichSORValtro.<%=IWebConstants.ACTION_FIELD%>.value = action;
	        document.DettRichSORValtro.modalita.value="I";
	  	    document.DettRichSORValtro.submit();
	     }
    }
    
    function vaiaDecisione(action)
    {
      document.DettRichSORValtro.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichSORValtro.modalita.value="I";
      document.DettRichSORValtro.submit();
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
        <font class="campo">Dettaglio Richiesta alla SORVEGLIANZA - Altre Richieste&nbsp;</font>
      </td>
      
      <td class="LBG">
         <select name="comboAction" >
           <option value="siap.siep.modulocumulo.action.ActLoadInserisciDecisioneDellaSORVAltro">Inserimento/Modifica Decisione della SORVEGLIANZA </option>
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
  
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettRichSORValtro">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaSORV.getIdRichiestePmInCumulo()%>">
  <input type="hidden" name="nextAction" value="siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVAltro">
  <input type="hidden" name="modalita" value="" >

<% 	
  String AnnoNumero ="";
  AnnoNumero = Titolo.getAnnoSentenza() +"/"+Titolo.getNumeroSentenza();
%> 
  <table width="95%" align="center">
    <tr><td colspan="1" class="Titolonocap" style="text-align:left" >In relazione al Titolo:</td></tr>
    <tr>
      <td class="l" colspan="1">
        <font class="label"><%=StringUtils.toStringJSP(Titolo.getDescrTipoProvvedimento() )%>&nbsp;N. &nbsp; </font>
        <font class="campo"><%=AnnoNumero%></font>&nbsp;
        &nbsp;<font class="label"> del </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Titolo.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
        &nbsp;<font class="label"> Emessa da </font>
	 	<font class="campo"><%=StringUtils.toStringJSP(Titolo.getDescrTipoAutoritaEmittente(), "") %></font>
        <font class="label">&nbsp;di&nbsp; </font>
        <font class="campo"><%=StringUtils.toStringJSP(Titolo.getDescrLuogoEmittente(), "") %></font>

        &nbsp;<font class="label"> Irrevocabile il  </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Titolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
      </td>
    </tr>
  </table>

<!-- 									DATI 	DELLA 	RICHIESTA 								 -->
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della richiesta</td></tr>
    <tr>
      <td class="l" width="200px">Data Richiesta</td>
      <td class="l" >
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaSORV.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>
    </tr>  	
    <tr>
      <td class="l" width="200px">Tipo Richiesta</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaSORV.getDescrTipoAnnotazione() )%></font>
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
	<tr><td>&nbsp;</td></tr>

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

<!-- 							DATI 	DELLA 	DECISIONE 	DELLA 	SORVEGLIANZA 								 -->
<%	if (RichiestaSORV.getDecisioneGeSorvCum()!= null &&
		RichiestaSORV.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null ) 
	{ 
		ProvvedimentoGeSorvCumModel decisioneSORV = RichiestaSORV.getDecisioneGeSorvCum();%>
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
	
	<%// Solo per Decisione su Tipo Richiesta :  Altro (cod = 014) ============================================	--%>
		<tr>
	      <td class="l">Esito :</td>
	 <%	if("C".equals(decisioneSORV.getFlagConforme() ))
	 	{ %>   
	  		<td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., DECIDE </font></td>
	<%	}
	 	else if("D".equals(decisioneSORV.getFlagConforme() ))
	 	{	%>
	 		<td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M., DECIDE </font></td>
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
		    
  	  </table>   
<%
  	}	// Chiude if Decisione	%>

</FORM>

</body>
</html>