<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel" %>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiSanzioneSostitutivaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="ListaTitoli"  			scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

<!-- 		LoadElencoTitoliRichiestaGERevocaSSCum		 --> 

<%
//==================================================================================
// Form di Elenco Titoli da selezionare per l'inserimento delle
//	Richieste del PM al GE di Revoca Sentenza abolizione del Reato
//==================================================================================

int TotTitoli = ListaTitoli.size();

%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Cumulo - Elenco Titoli per Richieste del PM al GE - Revoca Sentenza abolizione del Reato </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
    
   function eseguiFunzione(action)
   {
     document.EleRicGERevoSS.<%=IWebConstants.ACTION_FIELD%>.value = action;
     document.EleRicGERevoSS.submit();
   }
      
   function Verify() 
   { 
   		// controllo che siano presenti dei Titoli/Sanzioni
	 	var total = <%=TotTitoli%>;
	 	if(total == 0)
	 	{
	 		// Non ci Sono Titoli/Sanziono da selezionare
	 		var msgConfirm = "Attenzione: l'Istruttoria corrente non ha Sanzioni Sostitutive! "; 
         	if (window.confirm(msgConfirm)) 
         	{
   	      	 	lAzione = "siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE";
   	         	document.EleRicGERevoSS.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
   	         	document.EleRicGERevoSS.submit();
         	}
         	return false;
	 	}
	 	
	 	//Controllo che sia selezionata almeno una Sanzione
    	if (typeof (document.EleRicGERevoSS.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>[0]) =="undefined" )
		{	
    		// 1 solo oggetto Titolo/Sanzione  presente in maschera.
    		if(!document.EleRicGERevoSS.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>.checked )
    		{	
      			alert(" Attenzione selezionare la Sanzione Sostitutiva ");
	     		return false;
    		}	
		}
    	else
    	{	
    		// n oggetti Titoli/Sanzioni presenti in maschera
    		var Spunta="NO";
	 	    for (var j = 0; j < document.EleRicGERevoSS.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>.length; j++ )
	 		{
	 	    	if(document.EleRicGERevoSS.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>[j].checked )
	 		   	{	
	 	    		Spunta="SI";
	 		   	}
	 		}
	 	    
	 	    if(Spunta=="NO")
	 	    {
	 	    	alert("Attenzione selezionare almeno una Sanzione Sostitutiva ");
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
          <font class="campo">Richieste al G.E. Revoca Sanzioni Sostitutive&nbsp;</font>
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

  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="EleRicGERevoSS" >
  
  	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActLoadInsRichiestaGERevocaSS">
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
      <td class="int" width="10%" >Tipo Sanzione</td>
      <td class="int" width="10%" >Durata / Quantum</td>
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
      <td class="int" width="10%" >Tipo Sanzione</td>
      <td class="int" width="10%" nowrap >Durata / Quantum</td>
      <td class="int" width="5%"  >Selezione</td>
   </tr>
<%
  int id_rec = 0;
  String multa=new String("&nbsp;");	
  String ammenda=new String("&nbsp;");
  String NumAutoritaSiep = "";
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
        	NumAutoritaSiep += " "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getCodTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
        	NumAutoritaSiep += "<br> <font class=\"cRosso\">(Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+")</font>"; 
       	}
       	else {
       		NumAutoritaSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrFasCumulato();
       		NumAutoritaSiep += " "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getCodTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
       	}
 
    }
    
    multa="";
    ammenda="";
    if(lTitoCum.getSanzioneSostitutivaCumulo()!=null && lTitoCum.getSanzioneSostitutivaCumulo().getIdSanzioneSostitutivaCum() != null)
 	{
   	   	id_rec = id_rec +1; 
	 	SanzioneSostitutivaCumuloModel lSSCum = (SanzioneSostitutivaCumuloModel) lTitoCum.getSanzioneSostitutivaCumulo(); 
	 	
	 	if("P".equals(lSSCum.getCodTipoSanzione()) )
		{		
  			if(lSSCum.getSanzionePecuniariaMulta()!=null && lSSCum.getSanzionePecuniariaMulta().compareTo(new BigDecimal(0))!=0 ) 
  				multa = " "+StringUtils.toEuroFormat(lSSCum.getSanzionePecuniariaMulta());

  			if(lSSCum.getSanzionePecuniariaAmmenda()!=null && lSSCum.getSanzionePecuniariaAmmenda().compareTo(new BigDecimal(0))!=0 ) 
	  			ammenda = " "+StringUtils.toEuroFormat(lSSCum.getSanzionePecuniariaAmmenda());
		}	
	  
	  %>
	    <tr>	
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
	      <td class="C">
	        <%=StringUtils.toStringJSP(lSSCum.getDescrTipoSanzione() ,"")%>
	      </td>
	      
	      <td class="C" nowrap>
 <%	if(!"P".equals(lSSCum.getCodTipoSanzione()) )
 	{
 		if (!lSSCum.isDurataSanzioneZero()) 
 		{ %>
        	Anni&nbsp;<%=StringUtils.toStringJSP(lSSCum.getNumAnni(), "0")%>&nbsp;
        	Mesi&nbsp;<%=StringUtils.toStringJSP(lSSCum.getNumMesi(), "0")%>&nbsp;
        	Giorni&nbsp;<%=StringUtils.toStringJSP(lSSCum.getNumGiorni(),"0")%>&nbsp;
<% 		}
 		else
 		{ 	%>
      		&nbsp; - &nbsp;
     <% }  %>
      
<%	}
 	else
 	{ 	
 		if(!multa.equals(""))
 		{	%> 
			Multa &euro;<%=multa%><br>
<%		}
 		
 		if(!ammenda.equals(""))
		{		%>
			Ammenda &euro;<%=ammenda%>			
<%  	}
 	}	%>     		       
	      </td>
	      
          <td class="C">
            <input type="checkbox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>" value="<%=StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato(),"")%>">
            <input type="hidden" name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_ID_SANZIONE_SOSTITUTIVA_CUM %>" value="<%=StringUtils.toStringJSP(lSSCum.getIdSanzioneSostitutivaCum(), "") %>">
            <input type="hidden" name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE %>"  value="<%=StringUtils.toStringJSP(lSSCum.getCodTipoSanzione(), "") %>">
          </td>
         
    	</tr>
    	
<%	 
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
  var frmvalidator  = new Validator("EleRicGERevoSS");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
  