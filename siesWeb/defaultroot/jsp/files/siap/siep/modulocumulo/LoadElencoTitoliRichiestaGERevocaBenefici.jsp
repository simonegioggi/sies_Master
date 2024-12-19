<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel" %>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="ListaTitoli"  			scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

<!-- 		LoadElencoTitoliRichiestaGERevocaBenefici		 --> 

<%
//==================================================================================
// Form di Elenco Titoli da selezionare per l'inserimento delle
//	Richieste del PM al GE di Revoca Benefici
//==================================================================================

int TotTitoli = ListaTitoli.size();

%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Cumulo - Elenco Titoli per Richieste del PM al GE - Revoca Benefici </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
    
    function tornaIndietro(action)
    {
      document.indietroForm.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.indietroForm.submit();
    }
    
    function Verify() 
    { 

      // controllo che siano presenti dei Titoli/Benefici
      var total = <%=TotTitoli%>;
      if(total == 0)
      {
        // Non ci Sono Titoli/Benefici da selezionare
        var msgConfirm = "Attenzione: Per l'Istruttoria corrente non vi sono Benefici da Revocare! "; 
        if (window.confirm(msgConfirm)) 
        {
          lAzione = "siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE";
          document.eleRicGERevoBen.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.eleRicGERevoBen.submit();
        }
        return false;
      }
 
      //========================================================
      //Controllo che sia selezionato almeno un Beneficio
      if (typeof (document.eleRicGERevoBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_BEN_STATOESEC_COMP %>[0]) =="undefined" )
	  {	
    		// 1 solo oggetto Titolo/Beneficio  presente in maschera.
    		if(!document.eleRicGERevoBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_BEN_STATOESEC_COMP %>.checked )
    		{	
      			alert(" Attenzione selezionare il Beneficio ");
	     		return false;
    		}	
	  }
      else
      {
      	  // n oggetti Titoli/Benefici presenti in maschera
	      var Spunta = "NO";
	      var SpuntaTotale = 0;	
	      for (var i = 0; i < document.eleRicGERevoBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_BEN_STATOESEC_COMP %>.length; i++ )	
	      {
	        if(document.eleRicGERevoBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_BEN_STATOESEC_COMP %>[i].checked )
	        { 
	          Spunta = "SI";
	          SpuntaTotale = SpuntaTotale + 1;
	        }
	      }   
	      
	      if(Spunta=="NO")
	      {
	        alert("Attenzione selezionare almeno un Beneficio ");
	        return false;
	      } 
	      
	      var PrimoCheck = 0;
	      var SalvaValore="";
	      for (var j = 0; j < document.eleRicGERevoBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_BEN_STATOESEC_COMP %>.length; j++ )	
	      {
	        if(document.eleRicGERevoBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_BEN_STATOESEC_COMP %>[j].checked )
	        {
	      	    // Controllo Numero Titoli (max 1)
	      	    var ltito  = document.eleRicGERevoBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_BEN_STATOESEC_COMP %>[j].value
	      	    var lIndx = ltito.indexOf(";");
	      	    var ltitoTitolo = ltito.substring(0,lIndx);
				//alert(" ltitoTitolo = "+ltitoTitolo);
				
				PrimoCheck = PrimoCheck + 1;
	            if(PrimoCheck == 1)
	            { 
	             	SalvaValore = ltitoTitolo;
	            }
	            else if(PrimoCheck > 1)
	            {
	            	if(ltitoTitolo != SalvaValore)
	            	{
	              		alert("Attenzione selezionare Solo un Titolo per volta ");
	              		return false;
	            	} 
	          	}
	        }
	      }  
	      
	      //=================================================
	      // Controllo max 2 benefici
	      var CodBen = 0;
		  var ValCodice1 = '';
		  var ValCodice2 = '';
	      for (var j = 0; j < document.eleRicGERevoBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_BEN_STATOESEC_COMP %>.length; j++ )	
	      {
		        if(document.eleRicGERevoBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_BEN_STATOESEC_COMP %>[j].checked )
		        {
		       	     if(document.eleRicGERevoBen.<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO %>[j].value != '' )
		      	   	 {
		      	  		CodBen = CodBen + 1;
			        	if(CodBen > 2)
			        	{
			            	alert("Attenzione selezionare al massimo 2 Benefici (Sospensione/Non Menzione) ");
			            	return false;
			        	}
			        	else if(CodBen == 1)
			          	{
			            	ValCodice1 = document.eleRicGERevoBen.<%=ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO %>[j].value;
			           	 	document.eleRicGERevoBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_BENEFICIO_CUM_SEL_1 %>.value = document.eleRicGERevoBen.<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO%>[j].value; 
			          	}
			          	else if(CodBen == 2)
			          	{	 
			            	ValCodice2 = document.eleRicGERevoBen.<%=ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO %>[j].value;
			            	document.eleRicGERevoBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_BENEFICIO_CUM_SEL_2 %>.value = document.eleRicGERevoBen.<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO%>[j].value;
			          	}
		      	   	 }
		       	     else if(document.eleRicGERevoBen.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO  %>[j].value != '' )
		       	   	 {
		       	   	 	CodBen = CodBen + 1;
		       	   	 } 
		        }
	      
	      }  
	      
	      // Potrebbero essere 2 Benefici selezionati (ma 1 dato in sentenza e 1 dato con provvedimento);
	      // In questo caso entrebbe in questo blocco
	      if(CodBen > 2)
          {
             alert("Attenzione selezionare Max 2 Benefici (Sospensione/Non Menzione) ");
             return false;
          }
	      
	      if(CodBen > 1)
	      { 
		        if(ValCodice1 == '03' || ValCodice1 == '04') 
		        {
		          alert(" è possibile selezionare 2 Benefici solo se sono Sospensione/Non Menzione ");
		          return false;
		        }
		        
		        if(ValCodice2 == '03' || ValCodice2 == '04') 
		        {
		          alert("è possibile selezionare 2 Benefici solo se sono Sospensione/Non Menzione ");
		          return false;
		        }
		        
		        // Potrebbero essere 2 Benefici selezionati (ma 1 dato in sentenza e 1 dato con provvedimento);
			    // in questo caso 1 tra ValCodice1 e ValCodice2 sarebbe a pazio e entrebbe in questo blocco 
		        if(ValCodice1 == '' || ValCodice2 == '') 
		        {
		          alert("è possibile selezionare 2 Benefici solo se sono Sospensione/Non Menzione ");
		          return false;
		        }
	      }
	      
      }  // Chiude la else di if (typeof (document.eleRicGERevoBen.< %=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_BEN_STATOESEC_COMP %>[0]) =="undefined" )

      return true; 

    }  // Chiude function verify()  
      
   </script>
  </head>
  
<body class="corpo" >
  <table>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();">
              <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
          </a>
        </td>
          
        <td class="LBG">
          <font class="label">Funzione :</font>
          <%-- font class="campo">ELENCO TITOLI per Richiesta Applicazione Benefici&nbsp;</font--%>
          <font class="campo">Richieste al G.E. Revoca Benefici&nbsp;</font>
        </td>
        
        <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      	</td>
         
      </tr>
  </table>
  <br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  
  <table cellspacing="2" cellpadding="2" width="100%">
    <tr><td>&nbsp;</td></tr>
    <tr><td class="l"><center><font class="label" style="color:red; font-size: 10pt"  >Selezionare i Titoli oggetto della Richiesta dal successivo Elenco  </font></center></td></tr>
  </table>


<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="indietroForm">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
</form> 

  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="eleRicGERevoBen" >
  
  	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActLoadInsRichiestaGERevocaBenefici">
  	<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  	<input type="hidden" name="modalita"   value="<%=modalita%>">
    <input type="hidden" name="numeroTitoli" value="<%=ListaTitoli.size()%>">
    <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_BENEFICIO_CUM_SEL_1%>" value="">
    <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_BENEFICIO_CUM_SEL_2%>" value="">
    <input type="hidden" name="AssegnatoInSent" value="">
    <input type="hidden" name="AssegnatoInProvv" value="">
  
  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="titolo" colspan=3 width=100%>Elenco  Titoli </td>
    </tr>
  </table>  

<%
if ( ListaTitoli == null || ListaTitoli.size() == 0 ) 	
{ 	%>  
  <table>
    <tr>
      <td class="int" width="6%"  >Titolo</td>
      <td class="int" width="10%" >Data Titolo </td>
      <td class="int" width="8%"  >Numero sentenza</td>
      <td class="int" width="18%" >Autorità Titolo Esecutivo</td>
      <td class="int" width="10%" >Data Irrevocabilita</td>
      <td class="int" width="8%"  >Numero SIEP</td>
      <td class="int" width="25%" >Ufficio Esecuzione</td>
      <td class="int" width="10%" >Tipo Beneficio</td>
      <td class="int" width="5%"  >Selezione</td>
    </tr>
    <tr>
	  <td colspan="10">Nessun dato presente</td>
    </tr>
  </table>
<%
}
else
{		%>    
  <table>
    <tr>
      <td class="int" width="6%"  >Titolo</td>
      <td class="int" width="10%" >Data Titolo </td>
      <td class="int" width="8%"  >Numero sentenza</td>
      <td class="int" width="18%" >Autorità Titolo Esecutivo</td>
      <td class="int" width="10%" >Data Irrevocabilita</td>
      <td class="int" width="8%"  >Numero SIEP</td>
      <td class="int" width="25%" >Ufficio Esecuzione</td>
      <td class="int" width="10%" >Tipo Beneficio</td>
      <td class="int" width="5%"  >Selezione</td>
   </tr>
<%
  String NumAutoritaSiep = "";
  String Valscelto = "";	
  Iterator itx = ListaTitoli.iterator();
  while ( itx.hasNext())
  {
    TitoloCumulatoModel lTitoCum = (TitoloCumulatoModel) itx.next();
    NumAutoritaSiep = "";
    Valscelto = "";
    
 	if(lTitoCum.getProcedimentoCumulato()!=null) 
    {   
 		if("S".equals(lTitoCum.getProcedimentoCumulato().getFlagAccorpato()) )
 	  	{
 	  		UfficioModel lUfficioOrigine = lTitoCum.getProcedimentoCumulato().getUfficioOrigine();
 	  		
 	  		NumAutoritaSiep = lTitoCum.getProcedimentoCumulato().getChiaveAnnoFasCumulato() +"/"+ lTitoCum.getProcedimentoCumulato().getChiaveProgrOrigine();
 	  		NumAutoritaSiep += "<br> <font class=\"cRosso\">(Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+")</font>";   
 		}
 		else
 		{	
 			NumAutoritaSiep = StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getChiaveAnnoFasCumulato()+"/"+lTitoCum.getProcedimentoCumulato().getChiaveProgrFasCumulato());
 		}
    }
 	
 	int id_rec = 0;
    if(lTitoCum.getBeneficiCumulo()!=null && lTitoCum.getBeneficiCumulo().size() >0)
 	{
	  //int id_rec = 0;
	  Iterator itxB = lTitoCum.getBeneficiCumulo().iterator();
	  while ( itxB.hasNext()) 
	  {	
	 	id_rec = id_rec +1;
	 	BeneficioCumuloModel lBeneCum = (BeneficioCumuloModel) itxB.next(); %>
	    <tr>	
<%		if(id_rec == 1)
		{	%>
	      <td class="L">
	        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoProvvedimento(),"")%>
	      </td>
	      <td class="C" nowrap>
	        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy"))%>
	      </td>
	      <td class="C" nowrap>
	        <%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza(),"") %>
	      </td>
	      <td class="C" >
	        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoAutoritaEmittente()+" "+lTitoCum.getDescrLuogoEmittente())%> 
	      </td>
	      <td class="C" nowrap>
	        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy"))%>
	      </td>
	      
	      <td class="C" nowrap>
   <% 	if(!NumAutoritaSiep.equals("")) 
        { %>   
			<%=NumAutoritaSiep%>
<%      }	
      	else 
      	{   %>
        	&nbsp;
    <%  } 	%>
	      </td>
	      
	      <td class="C" >
	        <% if (lTitoCum.getProcedimentoCumulato()!=null) { %>
	        <%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getDescrTipoUfficioFasCumulato()+" di "+lTitoCum.getProcedimentoCumulato().getDescrLuogoUfficioFasCumulato() )%>
	        <% } else { %>
	        &nbsp;
	        <% } %>
	      </td>
	      <td class="C">
	        <%=StringUtils.toStringJSP(lBeneCum.getDescrTipoBeneficio() ,"")%>
	      </td>
<%		}
		else
		{	%>
		  <td class="C"></td>
		  <td class="C"></td>
		  <td class="C"></td>
		  <td class="C"></td>
		  <td class="C"></td>
		  <td class="C"></td>
		  <td class="C"></td>
		  <td class="C">
	        <%=StringUtils.toStringJSP(lBeneCum.getDescrTipoBeneficio() ,"")%>
	      </td>			
<%		} %>

		<%	Valscelto = StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato())+";"+StringUtils.toStringJSP(lBeneCum.getIdBeneficioCumulo())+";-;-" ; %>
			      
          <td class="C">
          	<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>" value="<%=StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato(),"")%>" >
            <input type="checkbox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_BEN_STATOESEC_COMP %>" value="<%=Valscelto%>" >
            
            <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO %>" value="<%=StringUtils.toStringJSP(lBeneCum.getIdBeneficioCumulo(), "") %>">
            <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO %>"  value="<%=StringUtils.toStringJSP(lBeneCum.getCodTipoBeneficio(), "") %>">
            
            <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="">
	       	<input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO %>" value="" >
          	<input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO %>" value="">
          	<input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>" value="">
           	<input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>"  value="">
            
            <input type="hidden" name="TipoBen" value="BEN">
          </td>
        
        <!--  
    	</tr>
    	 -->
    	
<%	  }		// Chiude iterator su BeneficiCumulo
 	}	// chiude if(lTitoCum.getBeneficiCumulo()!=null	
 %>

<!--    									PARTE NUOVA 					--> 	

<%	if(lTitoCum.getStatoEsecuzioneTitoloCumulato()!=null && lTitoCum.getStatoEsecuzioneTitoloCumulato().size() >0)
	{
	  String lP = " (P)";
	  Iterator itxST = lTitoCum.getStatoEsecuzioneTitoloCumulato().iterator();
	  while ( itxST.hasNext()) 
	  {	
	 	id_rec = id_rec +1;
	 	StatoEsecTitoloCumulatoModel lStatCum = (StatoEsecTitoloCumulatoModel) itxST.next();
	 	if(lStatCum.getListaComputi()!=null && lStatCum.getListaComputi().size() > 0)
	 	{
	 		Iterator itxC = lStatCum.getListaComputi().iterator();
	 		while( itxC.hasNext() )
	 		{	
	 			ComputiCumuloModel lCompMod = (ComputiCumuloModel) itxC.next();	%>
	 		
	 		<tr>
<%			if(id_rec == 1)
			{	%>	 		
	 		  <td class="L">
	        	<%=StringUtils.toStringJSP(lTitoCum.getDescrTipoProvvedimento(),"")%>
	      	  </td>
		      <td class="C" nowrap>
		        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy"))%>
		      </td>
		      <td class="C" nowrap>
		        <%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza(),"") %>
		      </td>
		      <td class="C" >
		        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoAutoritaEmittente()+" "+lTitoCum.getDescrLuogoEmittente())%> 
		      </td>
		      <td class="C" nowrap>
		        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy"))%>
		      </td>
		      
		      <td class="C" nowrap>
		   <% if (!NumAutoritaSiep.equals("")) 
		      {   %>
					<%=NumAutoritaSiep%>
		<%    }	
		      else 
		      {   %>
		        	&nbsp;
		   <% } 	%>
		      </td>
		      
		      <td class="C" >
		        <% if (lTitoCum.getProcedimentoCumulato()!=null) { %>
		        <%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getDescrTipoUfficioFasCumulato()+" di "+lTitoCum.getProcedimentoCumulato().getDescrLuogoUfficioFasCumulato() )%>
		        <% } else { %>
		        &nbsp;
		        <% } %>
		      </td>
		      <td class="C">
		        <%=StringUtils.toStringJSP(lCompMod.getDescrTipoAnnotazione())%><%=lP%>
		      </td>	
<%			} 
			else
			{	%>
			  <td class="C"></td>
			  <td class="C"></td>
			  <td class="C"></td>
			  <td class="C"></td>
			  <td class="C"></td>
			  <td class="C"></td>
			  <td class="C"></td>
			  <td class="C">
			     <%=StringUtils.toStringJSP(lCompMod.getDescrTipoAnnotazione())%><%=lP%>
			  </td>			
	<%		} %>
			
			<%	Valscelto = StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato())+";-;"+StringUtils.toStringJSP(lStatCum.getIdStatoEsecTitoloCumulato())+";"+StringUtils.toStringJSP(lCompMod.getIdComputiCumulo(),""); %>
			
		      <td class="C">
		      	<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>" value="<%=StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato(),"")%>" >
            	<input type="checkbox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_BEN_STATOESEC_COMP %>" value="<%=Valscelto%>" >
            
            	<input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(lStatCum.getIdStatoEsecTitoloCumulato(), "") %>">
            	<input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO %>"  value="<%=StringUtils.toStringJSP(lStatCum.getCodMotivo(), "") %>">
            	<input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO %>"  value="<%=StringUtils.toStringJSP(lStatCum.getCodTipoProvvedimento(), "") %>">
            	<input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>" value="<%=StringUtils.toStringJSP(lCompMod.getIdComputiCumulo(), "") %>">
            	<input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_ANNOTAZIONE %>"  value="<%=StringUtils.toStringJSP(lCompMod.getCodTipoAnnotazione(), "") %>">
            	
            	<input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO %>" value="">
            	<input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO %>"  value="">
            	
            	<input type="hidden" name="TipoBen" value="STE">
          	 </td>
	 	</tr>	
	 	
<%	 		} // Chiude Iterator Computi
	 	} 
	 	
	  } // Chiude Iterator StatoEsec
	
	} // Chiude if(ListaStatoEsec != null)	 

}	 // Chiude iterator su TitoloCumulo
%>
	</table>
	
 <table>
  <tr>
    <td class="lNoBord" colspan="2">
     <INPUT class="bottone" type="submit" name="bottConferma" value="Conferma">&nbsp;&nbsp;
    </td>
  </tr>
 </table>
<%
 }  // Chiude il listaTitoli == null
%>
</form>
  
</body>
</html>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("eleRicGERevoBen");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
  
