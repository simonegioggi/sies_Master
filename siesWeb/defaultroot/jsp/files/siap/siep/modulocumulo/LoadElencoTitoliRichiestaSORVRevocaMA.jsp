<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel" %>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="ListaTitoli"  			scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

<!-- 		LoadElencoTitoliRichiestaSORVRevocaMA		 --> 

<%
//==================================================================================
// Form di Elenco Titoli da selezionare per l'inserimento delle
//	Richieste del PM alla SORVEGLIANZA di Revoca M.A.
//==================================================================================

int TotTitoli = ListaTitoli.size();

%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Cumulo - Elenco Titoli per Richieste del PM alla SORVEGLIANZA - Revoca M.A. </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
    
   function eseguiFunzione(action)
   {
     document.EleRicSORVRevoMA.<%=IWebConstants.ACTION_FIELD%>.value = action;
     document.EleRicSORVRevoMA.submit();
   }
      
   function Verify() 
   { 
   		// controllo che siano presenti dei Titoli/Sanzioni
	 	var total = <%=TotTitoli%>;
	 	if(total == 0)
	 	{
	 		// Non ci Sono Titoli/Sanziono da selezionare
	 		var msgConfirm = "Attenzione: l'Istruttoria corrente non ha Misure Alternative! "; 
         	if (window.confirm(msgConfirm)) 
         	{
   	      	 	lAzione = "siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV";
   	         	document.EleRicSORVRevoMA.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
   	         	document.EleRicSORVRevoMA.submit();
         	}
         	return false;
	 	}
	 	
	 	// =================================================
	 	//Controllo che sia selezionata almeno una Misura
    	if (typeof (document.EleRicSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_MIS_ALT_SELEZIONATO %>[0]) =="undefined" )
		{	
    		// 1 solo oggetto Titolo/Misura  presente in maschera.
    		if(!document.EleRicSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_MIS_ALT_SELEZIONATO %>.checked )
    		{	
      			alert(" Attenzione selezionare la Misura ");
	     		return false;
    		}	
		}
    	else
    	{	
    		// n oggetti Titoli/Misure presenti in maschera
    		var Spunta="NO";
    		var SpuntaTotale = 0;
	 	    for (var j = 0; j < document.EleRicSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_MIS_ALT_SELEZIONATO %>.length; j++ )
	 		{
	 	    	if(document.EleRicSORVRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_MIS_ALT_SELEZIONATO %>[j].checked )
	 		   	{	
	 	    		Spunta="SI";
	 	    		SpuntaTotale = SpuntaTotale + 1;
	 		   	}
	 		}
	 	    
	 	    if(Spunta=="NO")
	 	    {
	 	    	alert("Attenzione selezionare almeno una Misura Alternativa ");
	       		return false;
	 	    }
	 	    
	 	    // ============================================
		 	// Controllo che sia selezionato solo 1 Titolo
	 	    if(SpuntaTotale > 1)
	 	   	{
	 	   		alert("Attenzione selezionare Solo una Misura ");
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
          <font class="campo">Richieste alla SORVEGLIANZA di Revoca Misure Alternative alla Detenzione </font>
        </td>
        
        <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV')">
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

  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="EleRicSORVRevoMA" >
  
  	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActLoadInsRichiestaSORVRevocaMA">
  	<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  	<input type="hidden" name="modalita"   value="<%=modalita%>">
    <input type="hidden" name="numeroTitoli" value="<%=ListaTitoli.size()%>">
  
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
      <td class="int" width="20%" >Tipo Misura</td>
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
      <td class="int" width="25%" >Autorità Titolo Esecutivo</td>
      <td class="int" width="10%" >Data Irrevocabilita</td>
      <td class="int" width="15%" >Numero SIEP</td>
      <td class="int" width="20%" >Tipo Misura</td>
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
        	NumAutoritaSiep += " "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getCodTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());;
        	NumAutoritaSiep += "<br> <font class=\"cRosso\">(Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+")</font>"; 
       	}
       	else {
       		NumAutoritaSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrFasCumulato();
       		NumAutoritaSiep += " "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getCodTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
       	}
 
    }

    if(lTitoCum.getStatoEsecuzioneTitoloCumulato() !=null && lTitoCum.getStatoEsecuzioneTitoloCumulato().size() > 0)
 	{
   	 	id_rec = 0;
   	 	Iterator itxS = lTitoCum.getStatoEsecuzioneTitoloCumulato().iterator();
		while ( itxS.hasNext()) 
   		{	
   	   		id_rec = id_rec +1; 
   	   		Valscelto = "";
   	   		//PenaAccessoriaCumuloModel lPACum = (PenaAccessoriaCumuloModel) itxS.next(); 
   	   		StatoEsecTitoloCumulatoModel lStatoEse = (StatoEsecTitoloCumulatoModel) itxS.next(); %>
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
   	  	  	   <%=StringUtils.toStringJSP(lStatoEse.getDescrMotivo() ,"")%>
   	  	  	</td>
 	  	
   		<%	
   			Valscelto = StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato())+";"+StringUtils.toStringJSP(lStatoEse.getIdStatoEsecTitoloCumulato()); 
   		%>
   			  	  			      
            <td class="C">
              <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>" value="<%=StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato())%>" >
              <input type="checkbox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_MIS_ALT_SELEZIONATO %>"  value="<%=Valscelto %>" >
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
  var frmvalidator  = new Validator("EleRicSORVRevoMA");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
  