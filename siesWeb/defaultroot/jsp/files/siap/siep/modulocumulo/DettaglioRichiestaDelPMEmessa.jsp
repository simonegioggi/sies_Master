<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"%>
<%@ page import="siap.sico.magistrato.model.MagistratoModel" %>

<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="RichiestaInviata"     scope="request" class="siap.siep.modulocumulo.model.RichiesteInviateCumModel"/>
<jsp:useBean id="Magistrato"	       scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>

<!--  jsp:useBean id="TitoliRichiesta"      scope="request" class="java.util.Vector"/ -->

<!-- 			DettaglioRichiestaDelPMEmessa			 -->

<% 
//=================================================================================== 
// Form per la visualizzazione del dettaglio delle richieste del PM Emessa (Inviata)
//=================================================================================== 

Vector<RichiestePmInCumuloModel> VecRich = null;
if(RichiestaInviata.getListaRichiestePMinCumulo()!=null && RichiestaInviata.getListaRichiestePMinCumulo().size() > 0)
{
	VecRich = new Vector<RichiestePmInCumuloModel>(RichiestaInviata.getListaRichiestePMinCumulo() );
}
%>

<html>
<head>
  <title> Gestione Cumulo - Richieste del PM  </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.DettRichEmessa.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichEmessa.submit();
    }
  
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Richiesta del PM Inviata&nbsp;</font>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadVisualizzaRichieste')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
      
<% 
// MEV_2025-48 - ALTRO - Si aggiunge il tasto di validazione diretta
String strUpload="/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActUploadRichiestaDelPM";
strUpload+="&"+ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTA_INVIATA_CUM+"="+RichiestaInviata.getIdRichiesteInviateCum();
strUpload+="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
strUpload+="&"+ICostantiRichiestePmInCumulo.CAMPO_AZIONE_DETTAGLIO+"=siap.siep.modulocumulo.action.ActDettaglioRichiestaDelPMEmessa";
strUpload+="&noblob=S&CampoValida=1";
if (RichiestaInviata.getFlagDocValidato() == null || "N".equals(RichiestaInviata.getFlagDocValidato()) ) 
{
%>
  <!-- BOTTONE DI STAMPA -->
  <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
    <jsp:param name="ActionLink" value="<%= "/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActStampaRichiestaDelPM&IdRichiestaInviataCum="+RichiestaInviata.getIdRichiesteInviateCum().toString()+"&IdIstruttoriaCumulo="+IstruttoriaCumulo.getIdIstruttoriaCumulo() %>"/>
  </jsp:include>
  <td class="LBG">
    <a  href="<%=strUpload%>">
      <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
    </a>
  </td>
<% } %>  
    </tr>
  </table>

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
  
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettRichEmessa">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTA_INVIATA_CUM %>" value="<%=RichiestaInviata.getIdRichiesteInviateCum()%>">

  <table width="95%" align="center">
    <tr><td colspan="2" class="Titolonocap">Dati della richiesta Emessa</td></tr>
    <tr>
      <td class="l" width="200px">Data Richiesta</td>
      <td class="l" colspan="1">
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaInviata.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>	
    </tr>
    <tr>
      <td class="l" width="200px">Data Trasmissione</td>
      <td class="l" colspan="1">
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaInviata.getDataTrasmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>	
    </tr>
    
    <tr>
      <td class="l" width="200px">Trasmessa a </td>
      <td class="l" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(RichiestaInviata.getDescrUfficioDest(),"")%></font>
      	 di <font class="campo"><%=StringUtils.toStringJSP(RichiestaInviata.getDescrLuogoDest(),"")%></font>
      </td>
    </tr>  
    
    <tr>
	  	<td class="l" width="200px">Contenuto </td>
        <td class="l" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(RichiestaInviata.getContenuto(),"")%></font></td>
    </tr>
    
    <tr>
      <td class="l" width="200px">Magistrato firmatario</td>
      <td class="L" colspan=1><font class="campo"><%=StringUtils.toStringJSP(Magistrato.getCognome())%> &nbsp;<%=StringUtils.toStringJSP(Magistrato.getNome())%></font>
      </td>
    </tr> 
  </table>
 
 		<!-- 				RICHIESTE COINVOLTE					 -->				 
<% 
if(VecRich!=null && VecRich.size() > 0)
{	%>
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati delle singole richieste</td></tr>
<%   Iterator itx = VecRich.iterator();
  while(itx.hasNext())
  {	
  	RichiestePmInCumuloModel RichiestaPM = (RichiestePmInCumuloModel)itx.next();
%>  
	 <tr>
      <td class="l" width="200px" >Titoli Coinvolti </td>
      <td class="l" > Sentenza N.
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getAnnoSentenza() )%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(RichiestaPM.getNumeroSentenza() )%></font>&nbsp;
<%	if( !("null").equals(RichiestaPM.getAltri()) && 
			!"".equals(RichiestaPM.getAltri()) &&
			!"0".equals(RichiestaPM.getAltri()) )
		{	%>
			&nbsp;+&nbsp; <font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getAltri())%></font>
<% 		} %>
      </td>		 	      
    </tr>    
<%	String lTipo = "";
	if (RichiestaPM.getCodTipoRichiesta().equals(ICostantiRichiestePmInCumulo.RICHIESTA_AL_GE))  {
		
		// Descrizione Richiesta
	 	lTipo = "";
/*	 	if( !"021".equals(RichiestaPM.getCodTipoAnnotazione()) && 
	 		!"023".equals(RichiestaPM.getCodTipoAnnotazione()) && 
	 		!"024".equals(RichiestaPM.getCodTipoAnnotazione()) &&
			!"025".equals(RichiestaPM.getCodTipoAnnotazione()) )
*/
		if( "002".equals(RichiestaPM.getCodTipoAnnotazione()) ||
			"003".equals(RichiestaPM.getCodTipoAnnotazione()) )
	 	{
	 		lTipo +=" Applicazione ";
	 	}
		else if( "004".equals(RichiestaPM.getCodTipoAnnotazione()) ||
				 "013".equals(RichiestaPM.getCodTipoAnnotazione()) ||
				 "017".equals(RichiestaPM.getCodTipoAnnotazione()) )
		{
			lTipo +="Revoca Sentenza abolizione del Reato: ";
		}
%>		
	    <tr>
<%		if(	"024".equals(RichiestaPM.getCodTipoAnnotazione()) ||
 			"025".equals(RichiestaPM.getCodTipoAnnotazione()) ||
 			"029".equals(RichiestaPM.getCodTipoAnnotazione()) ||
 			"030".equals(RichiestaPM.getCodTipoAnnotazione()) ||
 			"014".equals(RichiestaPM.getCodTipoAnnotazione()) )
		{ %>
		  <td class="l" width="200px" >Tipo Richiesta </td>
<%		}
		else
		{	%>		  		    
	      <td class="l" width="200px" >Computo beneficio </td>
<%		} %>	      
	      <td class="l" >
	        <font class="campo"><%=lTipo%>&nbsp;<%=StringUtils.toStringJSP(RichiestaPM.getDescrTipoAnnotazione() )%></font>&nbsp;
	        del
	      	&nbsp;<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaPM.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>&nbsp;&nbsp;
	<%	if(RichiestaPM.getCodDpr()!=null )
		{	%>
		    &nbsp;<font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getDescrBeneficio(),"" )%></font>&nbsp;    
			DPR:
	        &nbsp;<font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getDescrDpr() )%></font>&nbsp;
	<%	} %>        
			
	      </td>		 	      
	    </tr>
<%// Solo per Richiesta di 'SOSTITUZIONE e/o APPLICAZIONE di PENA ACCESSORIA'  ============================================			
  		if(	"024".equals(RichiestaPM.getCodTipoAnnotazione()) ||
	 		"025".equals(RichiestaPM.getCodTipoAnnotazione()) ||
	 		"029".equals(RichiestaPM.getCodTipoAnnotazione()) ||
 			"030".equals(RichiestaPM.getCodTipoAnnotazione()) )
  		{	  %>
		<tr>
      	  	<td class="l">Pena Accessoria </td>
	  		<td class="l" >
        	  <font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getDescrTipoPenaAccessoria())%></font>
      		</td>	      
    	</tr>
		<tr>
      		<td class="l">Durata (Tipo / Periodo): </td>
	  		<td class="l" >
        	 <font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getDescrTipoDurataPa(), "-")%></font>&nbsp;&nbsp;/&nbsp;
		 <%	if(RichiestaPM.getNumAnniPa()!=null && RichiestaPM.getNumAnniPa().compareTo(BigDecimal.ZERO) > 0)
		  	{ %> 	
		 		Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(RichiestaPM.getNumAnniPa() ) %></font>&nbsp;&nbsp;
		<%  }
			
			if(RichiestaPM.getNumMesiPa()!=null && RichiestaPM.getNumMesiPa().compareTo(BigDecimal.ZERO) > 0)
		  	{	%> 	
		 		Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(RichiestaPM.getNumMesiPa() ) %></font>&nbsp;&nbsp;
		<%	}
			
			if(RichiestaPM.getNumGiorniPa()!=null && RichiestaPM.getNumGiorniPa().compareTo(BigDecimal.ZERO) > 0)
		  	{	%>  	
		 		Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(RichiestaPM.getNumGiorniPa() ) %></font>&nbsp;&nbsp;
		<%	} %>       
      </td>	      
    </tr>  
<%		}
  		else if("002".equals(RichiestaPM.getCodTipoAnnotazione()) ||	// applicaz. Benefici
  				"003".equals(RichiestaPM.getCodTipoAnnotazione()) ||
  				"004".equals(RichiestaPM.getCodTipoAnnotazione()) ||	// revoca Sentenza abolizione del Reato
  				"013".equals(RichiestaPM.getCodTipoAnnotazione()) ||
  				"017".equals(RichiestaPM.getCodTipoAnnotazione()) ||
  				"021".equals(RichiestaPM.getCodTipoAnnotazione()) ||	// revoca Benefici
  		 		"023".equals(RichiestaPM.getCodTipoAnnotazione()) )		// revoca Sanz. Sostitutive
  		{	
%> 	    
		<tr>
	      <td class="l" >Reclusione </td>
	      <td class="l" >&nbsp;
	      	<font class="campo" style="color:red"><%=StringUtils.toStringJSP(RichiestaPM.getFlagPiuMenoR(),"")%></font>&nbsp;&nbsp;
	      	<font class="label"> Anni </font>&nbsp;
	        <font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getNumAnniReclusioneR(), " - ") %></font>&nbsp;
			<font class="label"> Mesi </font>&nbsp;
	        <font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getNumMesiReclusioneR(), " - ") %></font>&nbsp;
	        <font class="label"> Giorni </font>&nbsp;
	        <font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getNumGiorniReclusioneR(), " - ") %></font>&nbsp;
	         &nbsp;&nbsp;&nbsp;	<font class="label"> Multa : </font>&nbsp;
	         <%					if(RichiestaPM.getImportoMultaR()!=null && RichiestaPM.getImportoMultaR().compareTo(BigDecimal.ZERO) > 0 )
	         					{	%>
	        						<font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getImportoMultaR()) %></font>&nbsp;&euro;&nbsp;
	        			<%		}
	         					else
	         					{	%>
	         						<font class="campo">&nbsp; - &nbsp; </font>
	         				<%	}	 %>				
	
	      </td>
		</tr>
		<tr>
	      <td class="l" >Arresto </td>
	      <td class="l" >&nbsp;
	      	<font class="campo" style="color:red"><%=StringUtils.toStringJSP(RichiestaPM.getFlagPiuMenoR(),"")%></font>&nbsp;&nbsp;
	      	<font class="label"> Anni </font>&nbsp;
	        <font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getNumAnniArrestoR(), " - " ) %></font>&nbsp;
			<font class="label"> Mesi </font>&nbsp;
	        <font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getNumMesiArrestoR(), " - ") %></font>&nbsp;
	        <font class="label"> Giorni </font>&nbsp;
	        <font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getNumGiorniArrestoR(), " - ") %></font>&nbsp;
	         &nbsp;&nbsp;&nbsp;	<font class="label"> Ammenda : </font>&nbsp;
	         <%					if(RichiestaPM.getImportoAmmendaR()!=null && RichiestaPM.getImportoAmmendaR().compareTo(BigDecimal.ZERO) > 0 )
	         					{	%>
	        						<font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getImportoAmmendaR()) %></font>&nbsp;&euro;&nbsp;
	        			<%		}
	         					else
	         					{	%>
	         						<font class="campo">&nbsp; - &nbsp; </font>
	         				<%	}	 %>				
	
	      </td>
	    </tr>
	    <tr>
	      <td class="l">Anticipazione degli effetti </td>
		 <%	if("A".equals(RichiestaPM.getFlagAppProvvisoria() ))
		 	{ %>     
		      	<td class="l" ><img src="/images/V.gif"> </td>
		<%	}
		 	else 
		 	{  %>
		 		<td class="l" ><font class="label" style="color:red" > NO </font></td>
		<% 	} %>	 	      
		</tr>

<% 		} %>
	
<%		if(RichiestaPM.getMotivazioni()!=null && !RichiestaPM.getMotivazioni().equals(""))
		{	%>
		<td class="l" width="200px" > Motivazioni </td>
	    <td class="l" ><font class="campo"><%=StringUtils.toStringJSP(RichiestaPM.getMotivazioni(), "") %></font></td>
			
	<%	} %>			
	
<%	}  // Chiude l'if RICHIESTA_AL_GE %>
<%
	if (RichiestaPM.getCodTipoRichiesta().equals(ICostantiRichiestePmInCumulo.RICHIESTA_ALLA_SORV))  { 
%>		
    
	<%	}  // Chiude l'if RICHIESTA_ALLA_SORV %>
	
		<tr><td>&nbsp;</td></tr>
<%} // chiude ciclo while	%>

  </table>
<%
}	// chiude if(VecRic != null)
%>   
</FORM>

  <div align=left style="visibility:hidden" id="upld" >
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <!--  jsp:include page="< %=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" / -->
          <tr>
          <td class="L">Valida Documento</td>
          <td class="L">
            <input type=checkbox name="<%=ICostantiRichiestePmInCumulo.CAMPO_VALIDA%>" value=1>
          </td>
        </tr>
        <tr>
          <td class="l" rowspan=2>Indica il percorso locale del documento da salvare</td>
          <td class="L">
            <font class="campo">
            <input type=file size="35" name="<%=ICostantiRichiestePmInCumulo.CAMPO_BLOB%>"></font>
          </td>
        </tr>
        <tr>
          <td class="L">
            <input class="bottone" type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActUploadRichiestaDelPM">
            <input type="HIDDEN" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTA_INVIATA_CUM %>" value="<%=RichiestaInviata.getIdRichiesteInviateCum() %>">
            <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
            <input type="HIDDEN" name="<%=ICostantiRichiestePmInCumulo.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.modulocumulo.action.ActDettaglioRichiestaDelPMEmessa">
          </td>
        </tr>
      </table>
    </FORM>
  </div>

</body>
</html>