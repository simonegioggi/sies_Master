<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="Provvedimento" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>

<!-- 		DettaglioFungibilitaCumulo		 -->

<%
int TotaleComputi= 1;
if(Provvedimento.getListaComputi().size() > 1)
{
	TotaleComputi= Provvedimento.getListaComputi().size();
}
%>


<html>
<head>
  <title> Dettaglio Fungibilità</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // 
    //==========================================================================
    function eseguiFunzione(aTipoAzione)
    {
   	   var totcomputi=<%=TotaleComputi%>;
   	   
      if (aTipoAzione=='Inserisci'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciFungibilitaCumulo";
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.modalita.value = "I";
        
        document.formName.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>.value = "";
        document.formName.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>.value = "";
        document.formName.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>.value = "";
        
        document.formName.submit();
      }      
      else if (aTipoAzione=='Cancella')
      {
          // Cancellazione fisica richiedo conferma
          if(totcomputi > 1)
          {
         	 var msgVai = "Eseguire la cancellazione da Elenco fungibilità! presenza di più periodi computati";
         	 if(window.confirm(msgVai) )
         	 {
         		 lAzione = "siap.siep.modulocumulo.action.ActRicercaFungibilitaCumulo";
                 document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
                 document.formName.submit();
         	 }	 
          }
          else
          {	 
	          var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
	          if (window.confirm(msgConfirm)) 
	          {
	            lAzione = "siap.siep.modulocumulo.action.ActInserisciFungibilitaCumulo";
	            document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
	            document.formName.modalita.value = "C";
	
	            document.formName.submit();
	          }
          }   
      }
      else if (aTipoAzione=='Indietro'){
        lAzione = "siap.siep.modulocumulo.action.ActRicercaFungibilitaCumulo";
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.submit();
      }
    }
    
    
    function nuovoPeriodo(){
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciFungibilitaCumulo";
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.formName.modalita.value = "NP";  
      document.formName.submit();   
    }
  </script>
</head>

<body class="corpo">

<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Fungibilità&nbsp;</font>
      </td>
      <%// if (IstruttoriaCumulo.getFlagStato().equals("A") && 1==1)
      //{ %>
      <%-- td class="LBG">
        <a href="javascript:eseguiFunzione('Inserisci')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
        <%// if (!"C".equals(Provvedimento.getFlagStato()))
          // { %>
        	<a href="javascript:eseguiFunzione('Cancella')">
          	  <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>      
        <%// } %>
      </td --%>
    <%// } %>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('Indietro')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      </td>
    </tr>
  </table>
</FORM>

 <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
 <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(Provvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"      value="<%=StringUtils.toStringJSP(Provvedimento.getFlagStato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>" value="<%=StringUtils.toStringJSP(StringUtils.cStrForJS(Provvedimento.getMotivoModifica()), "") %>">
  
  <input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>" 		value = "<%=Provvedimento.getListaComputi().get(0).getIdComputiCumulo()%> " >
  <input type="hidden" name="modalita" value="">

<div id="divPosizionamento" align="left" style="padding-left: 25px;" >
  <table cellspacing="2" cellpadding="2" width="90%">
    <tr>
      <td class="l">Provvedimento</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrMotivo())%></font>
      </td>
      <td class="l" > del </td>
      <td class="l" align="center">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataEmissione(),"dd-MM-yyyy"))%>&nbsp;&nbsp;</font>
      </td>
<%	//ComputiCumuloModel lComputoGE = (ComputiCumuloModel)Provvedimento.getListaComputi().get(0);

	if("D".equals(Provvedimento.getCodTipoIstante()))
	{	%>
	  <td class="l"><font class="campo">su Istanza del difensore</font></td>
<%	}
	else if("I".equals(Provvedimento.getCodTipoIstante()))
	{	%>
	  <td class="l"><font class="campo">su Istanza del condannato</font></td>
<%	}
	else if("U".equals(Provvedimento.getCodTipoIstante()))
	{	%>
	  <td class="l"><font class="campo">(aperto d'Ufficio)</font></td>      
<%  }
	else if("G".equals(Provvedimento.getCodTipoIstante()))
	{	%>
	  <td class="l"><font class="campo">su Ordinanza del G.E.</font></td> 
<%  } %>
    </tr>
 </table>
 
<%	if("G".equals(Provvedimento.getCodTipoIstante()))
	{	%>
	<table cellspacing="2" cellpadding="2" width="90%">    
    <tr>
      <td class="l" colspan="2">
       Numero SIGE
        <font class="campo">&nbsp;&nbsp;<%=StringUtils.toStringJSP(Provvedimento.getAnnoProcedimento())%> / <%=StringUtils.toStringJSP(Provvedimento.getProgrProcedimento())%></font>&nbsp;&nbsp;
       emessa il
        <font class="campo">&nbsp;&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataEmissioneAltro(),"dd-MM-yyyy"))%></font>&nbsp;&nbsp;
      </td>
    </tr>
    <tr>  
      <td class="l" colspan="2" >
       da
        <font class="campo">&nbsp;&nbsp;<%=StringUtils.toStringJSP(Provvedimento.getDescrTipoUfficioAltro(), "" ) %></font>&nbsp;
       di
        <font class="campo">&nbsp;<%=StringUtils.toStringJSP(Provvedimento.getDescrLuogoAltro(), "") %></font>&nbsp;
      </td>
    </tr>  
  </table>
  <br>
 <%	} %>

<% 

 Iterator itxC = Provvedimento.getListaComputi().iterator(); 
 while(itxC.hasNext())
 {
   	ComputiCumuloModel lComputo = (ComputiCumuloModel)itxC.next();
 
	if(Provvedimento.getCodMotivo().equals("0212"))
	{ %>
		<table cellspacing="2" cellpadding="2" width="90%">
		  <tr>
		    <td class="Titolonocap" colspan="100%">Dati identificativi della Misura Cautelare da computare</td>
		  </tr>
		  <tr>  
	   		<td class="l" width="25%">Causale </td>
	   		<td class="l">
	       	  <font class="campo"><%=StringUtils.toStringJSP(lComputo.getDescrCausaleComputo()) %></font>&nbsp;&nbsp;&nbsp;
	        </td>
	      </tr>
	     
	  <%  if(lComputo.getNumeroRegePM()!=null )
	  	  {  %>
	  	  	<tr>  
	   		  <td class="l" width="25%">procedimento R.G.P.M. </td>
	   		  <td class="l">  	  
	       	   <font class="campo"><%=StringUtils.toStringJSP(lComputo.getAnnoRegePM())%> / <%=StringUtils.toStringJSP(lComputo.getNumeroRegePM())%></font>&nbsp;
	   		  </td>
	  	    </tr>
	  	<%} %> 
	  	
	  	<%if(lComputo.getCodTipoUfficioPM()!=null )
	  	  {  %>   
	  	    <tr>  
	   		  <td class="l" width="25%">Ufficio PM </td>
	   		  <td class="l">
	   		    <font class="campo"><%=StringUtils.toStringJSP(lComputo.getDescrTipoUfficioPM(), "" ) %></font>&nbsp;&nbsp;
	    	    di
	   	        <font class="campo">&nbsp;&nbsp;<%=StringUtils.toStringJSP(lComputo.getDescrSedeUfficioPM(), "") %>	</font>
	   		  </td>
	  	    </tr>
	  	<%} %> 
	  	   
	  	</table>
	  	
	  	  
	  	  <!--  	Dati sulla Misua cautelare		 -->
	  <%if(lComputo.getAnnoBDMC()!=null || lComputo.getAnnoRege()!=null || lComputo.getCodTipoAutoritaRege()!=null || lComputo.getDataEmissioneOrdRege()!=null)
	  	{  %>
	  	<table cellspacing="2" cellpadding="2" width="90%">
	  	  <%if(lComputo.getAnnoBDMC()!=null)
	  	    {	%>
	  	    <tr>  
	   		  <td class="l" width="25%">Numero B.D.M.C. </td>
	   		  <td class="l">
	   		   <font class="campo"><%=StringUtils.toStringJSP(lComputo.getAnnoBDMC())%> / <%=StringUtils.toStringJSP(lComputo.getNumeroBDMC())%></font>&nbsp;&nbsp;
	   		  </td>
	   		</tr>
	   	<%	} %>
	   	
	   	<%	if(lComputo.getAnnoRege()!=null)
	  	    {	%>
	  	    <tr>  
	   		  <td class="l" width="25%">Procedimento Reg. Gen.</td>
	   		  <td class="l">
	   		   <font class="campo"><%=StringUtils.toStringJSP(lComputo.getAnnoRege())%> / <%=StringUtils.toStringJSP(lComputo.getNumeroRege())%></font>&nbsp;&nbsp;
	   		   <font class="campo"><%=StringUtils.toStringJSP(lComputo.getTipoRege())%></font>&nbsp;
	   		  </td>
	   		</tr>
	   	<%	} %>
	   		   
	   <%	if(lComputo.getCodTipoAutoritaRege()!=null)
	  	    {	%>
	  	    <tr>  
	   		  <td class="l" width="25%">Autorita Reg. Gen.</td>
	   		  <td class="l">
	   		   <font class="campo"><%=StringUtils.toStringJSP(lComputo.getDescrTipoAutoritaRege())%></font>&nbsp;&nbsp;
	   		    di 
	   		   <font class="campo">&nbsp;&nbsp;<%=StringUtils.toStringJSP(lComputo.getDescrLuogoAutoritaRege())%></font>&nbsp;&nbsp;
	   		  </td>
	   		</tr>
	   	<%	} %>
	   	
	   	<%	if(lComputo.getDataEmissioneOrdRege()!=null)
	  	    {	%>
	  	    <tr>  
	   		  <td class="l" width="25%">Data emissione ordinanza </td>
	   		  <td class="l">
	   		   <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataEmissioneOrdRege(),"dd-MM-yyyy"))%></font>&nbsp;&nbsp;
	   		  </td>
	   		</tr>
	   	<%	} %>		   
	  		   
		 </table>
	<%	} %>	 
		
	<%	if(lComputo.getCodTipoMisura()!=null)
	  	{	%>
	  	<table cellspacing="2" cellpadding="2" width="90%"> 
	      <tr>  
	   		<td class="l" width="25%" >Natura Misura</td>
	   		<td class="l">
	      	  <font class="campo"><%=StringUtils.toStringJSP(lComputo.getDescrTipoMisura()) %></font>&nbsp;
	   		</td>
	  	  </tr>
	  	  
	  	  <tr>
	        <td class="l">Luogo di Espiazione</td>
	        <td class="l">
	         <% if ( lComputo.getIstitutoDetenzione()!=null ) {  %>
	         <font class="campo"><%=StringUtils.toStringJSP (lComputo.getIstitutoDetenzione().getDescrizioneIstitutoPerVisualizzazione(), "&nbsp;") %></font>&nbsp;
	         <% } else { %>
	         <font class="campo"><%=StringUtils.toStringJSP (lComputo.getAltroLuogoDetenzione(),"&nbsp;" )%></font>&nbsp;
	         <% } %>
	        </td>
	      </tr>
	  	</table>	  
	  <% } %>
	    
	<%
	 } 	
	 else if(Provvedimento.getCodMotivo().equals("0213"))
	 { %>	 
		<table cellspacing="2" cellpadding="2" width="90%">
		  <tr>
		    <td class="Titolonocap" colspan="90%">Dati identificativi del provvedimento SIEP</td>
		  </tr>
		  <tr>  
	   		<td class="l" width="25%">Causale </td>
	   		<td class="l">
	       	  <font class="campo"><%=StringUtils.toStringJSP(lComputo.getDescrCausaleComputo()) %></font>&nbsp;&nbsp;&nbsp;
	        </td>
	      </tr>
	     
	  <%  if(lComputo.getNumeroSentenza()!=null )
	  	  {  %>
	  	  	<tr>  
	   		  <td class="l" width="25%">Numero Sentenza</td>
	   		  <td class="l">  	  
	       	   <font class="campo"><%=StringUtils.toStringJSP(lComputo.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(lComputo.getNumeroSentenza())%></font>&nbsp;
	   		  </td>
	  	    </tr>
	  	<%} %> 
	  	
	  	<%if(lComputo.getDataSentenza()!=null )
	  	  {  %>   
	  	    <tr>  
	   		  <td class="l" width="25%">Data sentenza</td>
	   		  <td class="l">
	   		    <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataSentenza(),"dd-MM-yyyy"))%></font>
	   		  </td>
	  	    </tr>
	  	<%} %>
	  	
	  	<%if(lComputo.getCodTipoAutoritaEmittente()!=null )
	  	  {  %>   
	  	    <tr>  
	   		  <td class="l" width="25%">Autorità Emittente</td>
	   		  <td class="l">
	   		    <font class="campo"><%=StringUtils.toStringJSP(lComputo.getDescrTipoAutoritaEmittente(), "" ) %></font>&nbsp;&nbsp;
	    	    di
	   	        <font class="campo">&nbsp;&nbsp;<%=StringUtils.toStringJSP(lComputo.getDescrLuogoEmittente(), "") %></font>
	   		  </td>
	  	    </tr>
	  	<%} %>
	  	
	  	<%if(lComputo.getChiaveAnnoSIEP()!=null )
	  	  {  %>
	  	  	<tr>  
	   		  <td class="l" width="25%">Anno e Numero SIEP</td>
	   		  <td class="l">  	  
	       	   <font class="campo"><%=StringUtils.toStringJSP(lComputo.getChiaveAnnoSIEP())%> / <%=StringUtils.toStringJSP(lComputo.getChiaveNumeroSIEP())%></font>&nbsp;
	   		  </td>
	  	    </tr>
	  	<%} %> 
	  	   
	  	</table>


<%   }  // chiude if(Provvedimento.)   %>
	

	 <table cellspacing="2" cellpadding="2" width="90%">
	  <tr>
	    <td class="Titolonocap" colspan="90%">periodi computati</td>
	  </tr>
    
	    <tr>
	      <td class="l">Periodo sofferto </td>
	      <td class="l">
	        dal&nbsp; 
	        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataReclusioneDa(),"dd-MM-yyyy"))%></font>&nbsp;
	        al&nbsp; 
	        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataReclusioneA(),"dd-MM-yyyy"))%></font>&nbsp;&nbsp;&nbsp;
	        Pari a:  Anni
	        <font class="campo"><%=StringUtils.toStringJSP (lComputo.getNumAnniReclusione()) %></font>&nbsp;
	        mesi
	        <font class="campo"><%=StringUtils.toStringJSP (lComputo.getNumMesiReclusione()) %></font>&nbsp;
	        giorni
	        <font class="campo"><%=StringUtils.toStringJSP (lComputo.getNumGiorniReclusione()) %></font>&nbsp;&nbsp;
    
 <% if ("CL".equals(lComputo.getCodTipoMisura()) && lComputo.getNumGiorniMap()!=null) 
 	{ %>
			per un totale di 
	        <font class="campo"><%=StringUtils.toStringJSP (lComputo.getNumGiorniMap(),"&nbsp;") %></font>&nbsp;
	        giorni
 <%	} %>   
 
      	  </td>
		</tr>
 
    <tr><td>&nbsp;</td></tr>
    
    <%--
    //==========================================================================
    // Descrizione dello stato visualizzata solo in fase di modifica del dato
    //==========================================================================
      String lDescStato = "";
      if      ( lComputo.getFlagStato().equals("E")){lDescStato = "Dato Estratto dal fascicolo originale";}
      else if ( lComputo.getFlagStato().equals("I")){lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
      else if ( lComputo.getFlagStato().equals("M")){lDescStato = "Dato estratto modificato";}
      else if ( lComputo.getFlagStato().equals("C")){lDescStato = "Dato estratto cancellato";}
    %>
    <tr>
      <td class="l" colspan="2"> <%=lDescStato %></td> 
    </tr>
  
    <%
    //========================================================================== 
    // Campo note visualizzato sia in inserimento sia in modifica dove l'utente
    // può motivare l'intervento sui dati su cui sta intervenendo
    //========================================================================== 
    %>
    <% if (!lComputo.getFlagStato().equals("E")){ %>
    <tr>
      <td class="l">Motivo Inserimento/Modifica</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lComputo.getMotivoModifica()) %>&nbsp;</font></td>
    </tr>
    <% } %>
    --%>

<%
 } // end while %>
  
</table>
    
<% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
<table cellspacing="2" cellpadding="2">
<tr>
  <td>
    <INPUT class="bottone" type="button" name="AGGIUNGI" value="Aggiungi ulteriore periodo" onClick="javascript:nuovoPeriodo();">
  </td>      
</tr>
</table>
 <% } %>  
    
</div >

</form>
</body>
</html>