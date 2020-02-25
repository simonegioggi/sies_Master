<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.ReatoCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichPMPenAccCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichPMMisSicCumModel"%>


<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="RichiestaGE"	       scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TitoliRichiesta"      scope="request" class="java.util.Vector"/>

<!-- 						DettaglioRichiestaGEBenefici						 -->
<% 
//============================================================================== 
// Form per la visualizzazione del dettaglio delle richieste al GE di concessione
// Benefici (amnistia/Indulto)
//============================================================================== 

RichiesteInviateCumModel lRicInv = null;

if(RichiestaGE!=null && RichiestaGE.getRichiesteInviateCum()!=null)
{
	lRicInv = (RichiesteInviateCumModel)RichiestaGE.getRichiesteInviateCum();
}	

ProvvedimentoGeSorvCumModel decisioneGE = null;
Boolean lDecisione = false;

Vector<RichPMPenAccCumModel> lVecPA = null;
Vector<RichPMMisSicCumModel> lVecMS = null;

if( RichiestaGE!=null && RichiestaGE.getDecisioneGeSorvCum()!=null && 
	RichiestaGE.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null )
{
	decisioneGE = (ProvvedimentoGeSorvCumModel) RichiestaGE.getDecisioneGeSorvCum();
	lDecisione=true;
	
	if(RichiestaGE.getListaRichPmPenaAccessoriaCum()!=null && RichiestaGE.getListaRichPmPenaAccessoriaCum().size() > 0)
		lVecPA = new Vector<RichPMPenAccCumModel>(RichiestaGE.getListaRichPmPenaAccessoriaCum());
	
	if(RichiestaGE.getListaRichPmMisureSicurezzaCum()!=null && RichiestaGE.getListaRichPmMisureSicurezzaCum().size() > 0 )
		lVecMS = new Vector<RichPMMisSicCumModel>(RichiestaGE.getListaRichPmMisureSicurezzaCum() );
}

%>

<html>
<head>
  <title> Gestione Richieste al GE - Applicazione Benefici</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.DettRichGE.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichGE.submit();
    }

    function eseguiFunzioneSelezionata()
    {
  	var action = document.f.comboAction[document.f.comboAction.selectedIndex].value ;

  	if( action.indexOf('Cancella') >= 0){
          if ( !window.confirm("Si vuole procedere alla cancellazione della decisione?") ) {
              return;
          } else {
      		document.DettRichGE.<%=IWebConstants.ACTION_FIELD%>.value = action;
            document.DettRichGE.modalita.value="C";
      	    document.DettRichGE.submit();
          }
  	} else {	
  		document.DettRichGE.<%=IWebConstants.ACTION_FIELD%>.value = action;
        document.DettRichGE.modalita.value="I";
  	    document.DettRichGE.submit();
    	}
    }
    
    function vaiaDecisione(action)
    {
      document.DettRichGE.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichGE.modalita.value="I";
      document.DettRichGE.submit();
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
        <font class="campo">Dettaglio Richiesta Applicazione Benefici&nbsp;</font>
      </td>
      
       <td class="LBG">
         <select name="comboAction" >
           <option value="siap.siep.modulocumulo.action.ActLoadInserisciDecisioneDelGECumulo">Inserimento/Modifica Decisione del G.E. </option>
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
  
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettRichGE">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaGE.getIdRichiestePmInCumulo()%>">
  <input type="hidden" name="nextAction" value="siap.siep.modulocumulo.action.ActDettaglioRichiestaGEBenefici">
  <input type="hidden" name="modalita" value="">

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
    <tr><td colspan="8" class="Titolonocap">In relazione al Titolo</td></tr>
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
  </table>

<%	// 	------------		REATI		--------------
	if(lTitolo.getReatoCircostanzaCumulo()!=null && lTitolo.getReatoCircostanzaCumulo().size() > 0)
	{	%>  
  <table width="95%" align="center">
    <tr>
      <td class="c" style="color:red; text-align:left" >Reati Selezionati</td>
      <td class="c">Durata</td>
      <td class="c">Sanzione</td>
    </tr>
<%	  String lStringaSanzione="";
	  String lStringPenaReato ="";
	  for(int kr = 0; kr < lTitolo.getReatoCircostanzaCumulo().size(); kr++ )
	  {
		ReatoCircostanzaCumuloModel lReatoCircostanza = (ReatoCircostanzaCumuloModel)lTitolo.getReatoCircostanzaCumulo().get(kr);
    	ReatoCumuloModel lReato = lReatoCircostanza.getReatoCum();
    	ReatoCumuloModel[] lCircostanze = lReatoCircostanza.getCircostanzeCum();
   		
    	boolean lFlagAnnoNumero = false;
        if( lReato.getAnnoFonte() != null
         && !lReato.getAnnoFonte().equals("")
         && lReato.getNumeroFonte() != null
         && !lReato.getNumeroFonte().equals("") )
        {
        	lFlagAnnoNumero = true;
        }
			%>  
    <tr>
      <td class="l">
<%      //REATO
       if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
       {	%>
         <font class="label">
         &nbsp;&nbsp;<font class="label"> N. <%=StringUtils.toStringJSP(lReato.getProgrNumeroManuale()) %> : </font>
         </font>
<%     }
       else
       {	%>
         <font class="label">
         &nbsp;&nbsp;<font class="label"> N. <%=StringUtils.toStringJSP(lReato.getProgrReato()) %> : </font>
		 </font> 	
<%     }  %>    

		<font class="campo">
<%		if(lFlagAnnoNumero)
		{
		   if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
		     out.println(lReato.getDescrFonte()+" ");
		   if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
		     out.println(lReato.getAnnoFonte());
		   if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
		     out.println("/"+lReato.getNumeroFonte());
 		}

		if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
		   out.println("art."+lReato.getArticolo());
		if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
		   out.println(" "+lReato.getDescrSottonumerazione());

		if(!lFlagAnnoNumero)
		{
		   if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
		     out.println(lReato.getDescrFonte());
 		}

		if(lReato.getComma() != null && !lReato.getComma().equals(""))
			out.println(" c. "+lReato.getComma());
	 	if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
	   		out.println(" l. "+lReato.getLettera());
	 	if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
	  		out.println(" n. "+lReato.getNumero());	
		 	
       	//CIRCOSTANZE
       	if(lCircostanze != null)
       	{
       		ReatoCumuloModel lCirc = null;
       		for(int i=0; i<lCircostanze.length; i++)
       		{
          		lCirc = lCircostanze[i];
%>		
		 ,
		 
<%	
			   boolean lFlagAnnoNumeroCirc = false;
		       if( lCirc.getAnnoFonte() != null
		           && !lCirc.getAnnoFonte().equals("")
		           && lCirc.getNumeroFonte() != null
		           && !lCirc.getNumeroFonte().equals("") )
		       {
			         lFlagAnnoNumeroCirc = true;
		       }
		       if(lFlagAnnoNumeroCirc)
		       {
		         if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
		           out.println(lCirc.getDescrFonte()+" ");
		         if(lCirc.getAnnoFonte() != null && !lCirc.getAnnoFonte().equals(""))
		           out.println(lCirc.getAnnoFonte());
		         if(lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals(""))
		           out.println("/"+lCirc.getNumeroFonte());
		       }
			
		       if(lCirc.getArticolo() != null && !lCirc.getArticolo().equals(""))
		         out.println("art."+lCirc.getArticolo());
		       if(lCirc.getDescrSottonumerazione() != null && !lCirc.getDescrSottonumerazione().equals("") && !lCirc.getDescrSottonumerazione().equals("-"))
		         out.println(" "+lCirc.getDescrSottonumerazione());
			
		       if(!lFlagAnnoNumeroCirc)
		       {
		         if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
		           out.println(lCirc.getDescrFonte());
		       }

		       if(lCirc.getComma() != null && !lCirc.getComma().equals(""))
		         out.println(" c. "+lCirc.getComma());
		       if(lCirc.getLettera() != null && !lCirc.getLettera().equals(""))
		         out.println(" l. "+lCirc.getLettera());
		       if(lCirc.getNumero() != null && !lCirc.getNumero().equals(""))
		         out.println(" n. "+lCirc.getNumero());
		       
   			} // Chiude for
           		
		}  // Chiude if(Circostanze) 
   			
   		lStringaSanzione = "";
   		lStringPenaReato = "";
       		
   		// Quantum Pena Detentiva
	    if(lReato.getDescrTipoPenaDetentiva()!=null && 
       		lReato.getDescrTipoPenaDetentiva().length()>0)
       	{
       	    lStringPenaReato = lReato.getDescrTipoPenaDetentiva();
       	    
       	  	if (lReato.getNumAnni()!=null)
       	   	{
       	       	if (lReato.getNumAnni().intValue()!= 0)
       	       		lStringPenaReato += " Anni " + lReato.getNumAnni();
       	   	}
       		   	
       	   	if (lReato.getNumMesi()!= null)
       	   	{
       	      	if (lReato.getNumMesi().intValue()!= 0)
       	       		lStringPenaReato += " Mesi " + lReato.getNumMesi();
       	   	}
       	      
       	   	if (lReato.getNumGiorni()!= null)
       	   	{
       	       	if (lReato.getNumGiorni().intValue()!= 0)
       	       		lStringPenaReato += " Giorni " + lReato.getNumGiorni();
       	   	}
       	}
       		 
       	// Quantum Sanzione Pecuniaria
       	if(lReato.getDescrTipoSanzione()!=null && lReato.getDescrTipoSanzione().length()>0)
       	{
       		lStringaSanzione = lReato.getDescrTipoSanzione();
       	      
       	   	if ( lStringaSanzione.length() > 1)
       	   	{
       	       	//lStringaSanzione += ""+"&Euro;"+"";
       	       	lStringaSanzione += ": "+lReato.getSanzionePecuniaria()+" "+"&euro;";
       	   	}
       	}
	%>			
		</font> 
      </td>
      <td class="l" nowrap><center><font class="campo"><%=StringUtils.toStringJSP(lStringPenaReato ,"")%></font></center></td>

      <td class="r" nowrap><center><font class="campo"><%=StringUtils.toStringJSP(lStringaSanzione ,"")%></font></center></td>
    </tr>
<%	  }  // chiude ciclo(for reatoCirc) %>
  </table>
<%  } // Chiude if(Titolo.getReatoCirc) %>

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
	        <td class="l" style="color:red; text-align:left" width="12%">Misure Sicurezza: </td>
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
            <td class="l" width="150px">
	 <%	 if(lDecisione)
		 {	
		 	if(lVecMS!=null && lVecMS.size()>0)
		 	{	
		 		for(int ii=0; ii<lVecMS.size(); ii++)
		 		{
		 			RichPMMisSicCumModel lMSMod = (RichPMMisSicCumModel)lVecMS.elementAt(ii);
		 			
		 			if(lMSMod!=null && lMSMod.getMisIdMisSicCumulo()!=null )
		 			{	
		 				if(lMSMod.getMisIdMisSicCumulo().equals(lMisura.getIdMisuraSicurezzaCumulo() ) )
		 				{	
		 					if(lMSMod.getFlagCondono()!=null && lMSMod.getFlagCondono().equals("S") )
		 					{	%>
		 					  <font class="campoLow"> Misura CONDONATA </font>
		 				<%	}
		 					else if(lMSMod.getFlagCondono()!=null && lMSMod.getFlagCondono().equals("N") )
		 					{	%>	
		 					  <font class="campoLow" style="color:red"> Misura NON CONDONATA </font>
	<% 	 					}
		 				}	
					}
		 		}
		 	}
		 }
	 	 else	
	 	 {	%>
		   <font class="label"><center>&nbsp;-&nbsp;</center></font>
		 
	 <%	 }   %>	          
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
	        <td class="l" style="color:red; text-align:left" width="12%">Pene Accessorie: </td>
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
            <td class="l" width="150px">
	 <%	 if(lDecisione)
		 {	
		 	if(lVecPA!=null && lVecPA.size()>0)
		 	{	
		 		for(int i=0; i<lVecPA.size(); i++)
		 		{
		 			RichPMPenAccCumModel lPAMod = (RichPMPenAccCumModel)lVecPA.elementAt(i);
		 			
		 			if(lPAMod!=null && lPAMod.getPenIdPenaAccessoriaCumulo()!=null )
		 			{	
		 				if(lPAMod.getPenIdPenaAccessoriaCumulo().equals(lPenAcc.getIdPenaAccessoriaCumulo() ) )
		 				{	
		 					if(lPAMod.getFlagCondono()!=null && lPAMod.getFlagCondono().equals("S") )
		 					{	%>
		 					  <font class="campoLow"> Pena CONDONATA </font>
		 				<%	}
		 					else if(lPAMod.getFlagCondono()!=null && lPAMod.getFlagCondono().equals("N") )
		 					{	%>	
		 					  <font class="campoLow" style="color:red"> Pena NON CONDONATA </font>
	<% 	 					}
		 				}	
					}
		 		}
		 	}
		 }
	 	 else	
	 	 {	%>
		   <font class="label"><center>&nbsp;-&nbsp;</center></font>
	<%	 } %>	   
        </td>
      </tr>
<%	  } 	%>
	 </table>
<% 	} // Chiude Pene_Accessoria			%>
  
  <br>
  
<%
  } // CHIUDE CICLO while sui Titoli %>

<!-- 									DATI 	DELLA 	RICHIESTA 								 -->
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della richiesta</td></tr>
    <tr>
      <td class="l" width="200px">Data Richiesta</td>
      <td class="l" colspan="3">
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaGE.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>
    </tr>
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
      <td class="l" colspan="3">&nbsp;
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
<%	if( lDecisione ) 
	{ 	%>
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