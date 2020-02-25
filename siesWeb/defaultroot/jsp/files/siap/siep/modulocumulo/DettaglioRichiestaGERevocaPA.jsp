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
<jsp:useBean id="ListaTitoliPA"	       scope="request" class="java.util.Vector"/>

<!-- 						DettaglioRichiestaGERevocaPA						 -->
<% 
//============================================================================ 
// Form per la visualizzazione del dettaglio delle richieste al GE di 
// Revoca Pene Accessorie
//============================================================================ 
RichiesteInviateCumModel lRicInv = null;
if(RichiestaGE!=null && RichiestaGE.getRichiesteInviateCum()!=null)
{
	lRicInv = (RichiesteInviateCumModel)RichiestaGE.getRichiesteInviateCum();
}

//Costruzione Norma legislativa per Depenalizzazione o illecito Amministrativo
 String DescrBisTer = "";
 String DescrFonte = "";
 String lAnnoNum = "";
 String lArt = "";
 String lComma = "";
 String lLettera = "";
 String lNumero = "";
 if( "027".equals(RichiestaGE.getCodTipoAnnotazione()) )
 {	
	if(RichiestaGE.getCodFonte() != null )
		DescrFonte = RichiestaGE.getDescrFonte();

	if(RichiestaGE.getAnnoFonte() != null && RichiestaGE.getAnnoFonte().intValue() > 0)
		lAnnoNum += StringUtils.toStringJSP(RichiestaGE.getAnnoFonte());
	
	if(RichiestaGE.getNumeroFonte() != null && !RichiestaGE.getNumeroFonte().equals("") )
		lAnnoNum += "/"+RichiestaGE.getNumeroFonte()+" ";
	
	if(RichiestaGE.getArticolo()!=null)
		lArt += RichiestaGE.getArticolo() +" ";
	
	if(RichiestaGE.getCodSottonumerazione()!= null)
		DescrBisTer = RichiestaGE.getDescrSottonumerazione();
	
	if(RichiestaGE.getComma()!=null && !RichiestaGE.getComma().equals("") )
		lComma += ""+RichiestaGE.getComma() +" ";
	
	if(RichiestaGE.getLettera()!=null && !RichiestaGE.getLettera().equals("") )
		lLettera += ""+RichiestaGE.getLettera() +" ";
	
	if(RichiestaGE.getNumero()!=null && !RichiestaGE.getNumero().equals("") )
		lNumero += ""+RichiestaGE.getNumero() +" ";


 } 

%>
<html>
<head>
  <title> Gestione Richieste al GE - Revoca Pene Accessorie</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.DettRichGERevoPA.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichGERevoPA.submit();
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
	      		document.DettRichGERevoPA.<%=IWebConstants.ACTION_FIELD%>.value = action;
	            document.DettRichGERevoPA.modalita.value="C";
	      	    document.DettRichGERevoPA.submit();
	    	}
	  	 } 
	  	 else 
	  	 {	
	  		document.DettRichGERevoPA.<%=IWebConstants.ACTION_FIELD%>.value = action;
	        document.DettRichGERevoPA.modalita.value="I";
	  	    document.DettRichGERevoPA.submit();
	     }
    }
    
    function vaiaDecisione(action)
    {
      document.DettRichGERevoPA.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichGERevoPA.modalita.value="I";
      document.DettRichGERevoPA.submit();
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
        <font class="campo">Dettaglio Richiesta Revoca Pena Accessoria&nbsp;</font>
      </td>
      
      <td class="LBG">
         <select name="comboAction" >
           <option value="siap.siep.modulocumulo.action.ActLoadInsDecisioneDelGERevocaPenaAccCum">Inserimento/Modifica Decisione del G.E. </option>
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
  
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettRichGERevoPA">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaGE.getIdRichiestePmInCumulo()%>">
  <input type="hidden" name="nextAction" value="siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaPenaAcc">

  <input type="hidden" name="modalita" value="" >

	<table width="95%" align="center">
	  <tr><td colspan="1" class="Titolonocap"><font class="campo">Elenco delle PENE ACCESSORIE da Revocare</font></td></tr>
<% 	
  String AnnoNumero ="";
  Iterator itxT = ListaTitoliPA.iterator();
  while(itxT.hasNext())
  {	
	TitoloCumulatoModel TitoloPA = (TitoloCumulatoModel)itxT.next();
  	AnnoNumero = TitoloPA.getAnnoSentenza() +"/"+TitoloPA.getNumeroSentenza();
%> 
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
		
<%		} // CHIUDE CICLO while sulle P.A. 
  	} // Chiude if(lTitolo.getPeneAccessorie()!=null) 
  }	// Chiude ciclo while sui Titoli
%>

  </table>
  
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
<%	if("028".equals(RichiestaGE.getCodTipoAnnotazione()) )
	{	%>     
	  <tr>
      	<td class="l" width="200px">Estremi Beneficio</td>
      	<td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrBeneficio())%>&nbsp;&nbsp;&nbsp;<%=StringUtils.toStringJSP(RichiestaGE.getDescrDpr())%></font>
      	</td>
      </tr>
<%	} 

	if("027".equals(RichiestaGE.getCodTipoAnnotazione()) )
	{	
		if(!DescrFonte.equals(""))
		{	%>
	  	<tr>	
		  <td class="l" >Norma di Legge </td>
		  <td class="l">
			<font class="campo"><%=DescrFonte%></font>&nbsp;
		<%	if(!lAnnoNum.equals(""))
			{	%>
				<font class="label"> Anno/Num. </font>		
				<font class="campo"><%=lAnnoNum%></font>&nbsp;&nbsp;
		<%	} %>
		
		<%	if(!lArt.equals(""))
			{	%>
				<font class="label"> Art. </font>		
				<font class="campo"><%=lArt%></font>&nbsp;
		<%	} %>
		
		<%	if(!DescrBisTer.equals(""))
			{	%>
				<font class="campo"><%=DescrBisTer%></font>&nbsp;&nbsp;
		<%	} %>
	
		<%	if(!lComma.equals(""))
			{	%>
				<font class="label"> Comma </font>		
				<font class="campo"><%=lComma%></font>&nbsp;
		<%	} %>	
		
		<%	if(!lLettera.equals(""))
			{	%>
				<font class="label"> L. </font>		
				<font class="campo"><%=lLettera%></font>&nbsp;
		<%	} %>
			
		<%	if(!lNumero.equals(""))
			{	%>
				<font class="label"> Num. </font>		
				<font class="campo"><%=lNumero%></font>&nbsp;
		<%	} %>
	
		  </td>
	  	</tr>		
<%		}
	} %>

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
	 	{ %>   
	  		<td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., CONFERMA la Revoca di Pena Accessoria</font></td>
	<%	}
	 	else if("D".equals(decisioneGE.getFlagConforme() ))
	 	{	%>
	 		<td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M., CONFERMA la Revoca di Pena Accessoria</font></td>
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
    

<% 		if(decisioneGE.getMotivazioniD()!=null && !decisioneGE.getMotivazioniD().equals("") )
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