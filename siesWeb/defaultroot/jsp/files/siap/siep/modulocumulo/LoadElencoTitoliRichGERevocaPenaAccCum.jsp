<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel" %>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaAccessoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="ListaTitoli"  			scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

<!-- 		LoadElencoTitoliRichGERevocaPenaAccCum		 --> 

<%
//==================================================================================
// Form di Elenco Titoli da selezionare per Inserimento 
//	Richieste al GE di Revoca Pena Accessoria
//==================================================================================

int TotTitoli = ListaTitoli.size();

%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Cumulo - Elenco Titoli Richieste al GE - Revoca Pena Accessoria </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
    
   function eseguiFunzione(action)
   {
     document.EleRicGERevoSosPA.<%=IWebConstants.ACTION_FIELD%>.value = action;
     document.EleRicGERevoSosPA.submit();
   }
   
   function Verify() 
   { 
   		// controllo che siano presenti dei Titoli/P.A.
	 	var total = <%=TotTitoli%>;
	 	if(total == 0)
	 	{
	 		// Non ci Sono Titoli/P.A. da selezionare
	 		var msgConfirm = "Attenzione: l'Istruttoria corrente non ha Pene Accessorie correlate! "; 
         	if (window.confirm(msgConfirm)) 
         	{
   	      	 	lAzione = "siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE";
   	         	document.EleRicGERevoSosPA.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
   	         	document.EleRicGERevoSosPA.submit();
         	}
         	return false;
	 	}
	 	
	 	//Controllo che sia selezionata almeno una P.A.
    	if (typeof (document.EleRicGERevoSosPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_PA_SELEZIONATI %>[0]) =="undefined" )
		{	
    		// 1 solo oggetto Titolo/P.A.  presente in maschera.
    		if(!document.EleRicGERevoSosPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_PA_SELEZIONATI %>.checked )
    		{	
      			alert(" Attenzione selezionare la Pena Accessoria ");
	     		return false;
    		}	
		}
    	else
    	{	
    		// n oggetti Titoli/P.A. presenti in maschera
    		var Spunta="NO";
	 	    for (var j = 0; j < document.EleRicGERevoSosPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_PA_SELEZIONATI %>.length; j++ )
	 		{
	 	    	if(document.EleRicGERevoSosPA.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_PA_SELEZIONATI %>[j].checked )
	 		   	{	
	 	    		Spunta="SI";
	 		   	}
	 		}
	 	    
	 	    if(Spunta=="NO")
	 	    {
	 	    	alert("Attenzione selezionare almeno una Pena Accessoria ");
	       		return false;
	 	    }
    	}
	 	
	 	return true; 
   } 
      
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
          <font class="campo">Richieste al G.E. Revoca Pene Accessorie&nbsp;</font>
        </td>
        
        <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
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

  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="EleRicGERevoSosPA" >
  	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActLoadInsRichiestaGERevocaPA">
  	<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  	<input type="hidden" name="modalita"   value="<%=modalita%>">

  
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
      <td class="int" width="25%" >Autorità Titolo Esecutivo</td>
      <td class="int" width="10%" >Data Irrevocabilita</td>
      <td class="int" width="15%" >Numero SIEP</td>
      <td class="int" width="10%" >Tipo Pena Accessoria</td>
      <td class="int" width="10%" >Tipo Durata</td>
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
      <td class="int" width="20%" >Autorità Titolo Esecutivo</td>
      <td class="int" width="10%" >Data Irrevocabilita</td>
      <td class="int" width="15%" >Numero SIEP</td>
      <td class="int" width="20%" nowrap>Tipo Pena Accessoria</td>
      <td class="int" width="10%" nowrap >Tipo Durata</td>
      <td class="int" width="5%"  >Selezione</td>
   </tr>
<%

  int id_rec = 0;
  String NumAutoritaSiep = "";
  String Valscelto = "";
  Iterator itx = ListaTitoli.iterator();
  while ( itx.hasNext())
  {
    TitoloCumulatoModel lTitoCum = (TitoloCumulatoModel) itx.next();
    
    NumAutoritaSiep = "";
    ProcedimentoCumulatoModel lProcedimentoCumulatoModel = lTitoCum.getProcedimentoCumulato();
    if (lProcedimentoCumulatoModel!=null)
    {
       	if ("S".equals(lProcedimentoCumulatoModel.getFlagAccorpato()) ){
        	UfficioModel lUfficioOrigine = lProcedimentoCumulatoModel.getUfficioOrigine();

        	NumAutoritaSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrOrigine();
        	NumAutoritaSiep += "<br> "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getCodTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());;
        	NumAutoritaSiep += "<br> <font class=\"cRosso\">(Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+")</font>"; 
       	}
       	else {
       		NumAutoritaSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrFasCumulato();
       		NumAutoritaSiep += "<br> "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getCodTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
       	}
 
    }

    if(lTitoCum.getPeneAccessorieCumulo() !=null && lTitoCum.getPeneAccessorieCumulo().size() > 0)
 	{
   	 	id_rec = 0;
   	 	Iterator itxP = lTitoCum.getPeneAccessorieCumulo().iterator();
		while ( itxP.hasNext()) 
   		{	
   	   		id_rec = id_rec +1; 
   	   		Valscelto = "";
   	   		PenaAccessoriaCumuloModel lPACum = (PenaAccessoriaCumuloModel) itxP.next(); %>
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
	        <% if(lTitoCum.getProcedimentoCumulato()!=null) { %>
				<%=NumAutoritaSiep%>
	        <% } else { %>
	        	&nbsp;-&nbsp;
	        <% } %>
	      	  </td>
  	  <%	}
   	  	  	else
   	  	  	{	%>
   	  	  	  <td class="C"></td>
   	  	  	  <td class="C"></td>
   	  	  	  <td class="C"></td>
   	  	  	  <td class="C"></td>
   	  	  	  <td class="C"></td>
   	  	  	  <td class="C"></td>
  <%		} %>
     	  	  	  
   	  	  	<td class="C" >
   	  	  	   <%=StringUtils.toStringJSP(lPACum.getDescrTipoPenaAccessoria() ,"")%>
   	  	  	</td>
   	  	  	<td class="C" >
	   <%	if(lPACum.getDurata()!= null && !"P".equals(lPACum.getDurata()) && !"D".equals(lPACum.getDurata()) )
	   	  	{  
	   			if(lPACum.getNumAnni()!=null && lPACum.getNumAnni().compareTo(BigDecimal.ZERO) > 0)
	   	  	  	{	%>
	   	  	  	 	Anni&nbsp;<%=StringUtils.toStringJSP(lPACum.getNumAnni() )%>&nbsp;
	    <%		}
	   		  
	   	  		if(lPACum.getNumMesi()!=null && lPACum.getNumMesi().compareTo(BigDecimal.ZERO) > 0)
	   	  	  	{	%>	  		
	        		Mesi&nbsp;<%=StringUtils.toStringJSP(lPACum.getNumMesi() )%>&nbsp;
	    <%		}
	   	  		
	   	  		if(lPACum.getNumGiorni()!=null && lPACum.getNumGiorni().compareTo(BigDecimal.ZERO) > 0)
	   	  	  	{	%> 		
	        		Giorni&nbsp;<%=StringUtils.toStringJSP(lPACum.getNumGiorni() )%>&nbsp;
	    <%		} 
	   	  	}
	   		else if(   "P".equals(lPACum.getDurata()) 
	   		        || "D".equals(lPACum.getDurata()))
	   		{		%> 
	   			<%=StringUtils.toStringJSP(lPACum.getDescrDurata() )%>
	   	<%	} %>
	   	    &nbsp;
   	  	  	</td>			
   	  	  	
   		<%	
   			Valscelto = StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato())+";"+StringUtils.toStringJSP(lPACum.getIdPenaAccessoriaCumulo()); 
   		%>
   			  	  			      
            <td class="C">
              <input type="checkbox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_PA_SELEZIONATI %>"  value="<%=Valscelto %>" >
            </td>
  	  	           
       	</tr>   	   		
<% 		} 

 	}	// chiude if(lTitoCum.getSanzioneSostitutivaCumulo()!=null
 			
  }	 // Chiude iterator su TitoloCumulo
%>
	</table>
	
 <table>
  <tr>
    <td class="lNoBord" colspan="2">
     <INPUT class="bottone" type="submit" name="bottConferma" value="Conferma" >&nbsp;&nbsp;
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
  var frmvalidator  = new Validator("EleRicGERevoSosPA");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
  