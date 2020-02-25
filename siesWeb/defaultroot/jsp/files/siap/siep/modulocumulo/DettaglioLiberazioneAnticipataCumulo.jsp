<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.modulocumulo.model.LibAnticipataCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PeriodoLibAntCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiLibAnticipataCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPeriodoLibAntCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       	scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="Provvedimento"   		scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>

<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

<!-- 		DettaglioLiberazioneAnticipataCumulo		 -->

<%
// modifica per gestire il caso di dati estratti privi di record LA (rigetta)
Boolean PresenzaLA=false;
Boolean PresenzaPeriodi=false;
Vector<LibAnticipataCumuloModel> lListaLA = Provvedimento.getListaLiberazioniAnticipate();

BigDecimal lIdLibAnt = null;

if (lListaLA!=null) 
{ 
  PresenzaLA = true;

  lIdLibAnt = lListaLA.get(0).getIdLibAnticipataCumulo();

  Iterator ITXP = lListaLA.iterator();
  while(ITXP.hasNext())
  {
    LibAnticipataCumuloModel LAMCumodel = (LibAnticipataCumuloModel)ITXP.next();
    if(LAMCumodel!=null && LAMCumodel.getIdLibAnticipataCumulo()!=null )
    {
      if(LAMCumodel.getListaPeriodiLibAnticipate() != null && LAMCumodel.getListaPeriodiLibAnticipate().size() > 0 )
      {
        PresenzaPeriodi = true;  
      }
    }
  } 
}

%>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Liberazione Anticipata - Titolo Cumulato </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script>
    
  function eseguiFunzione(aTipoAzione)
  {
 	   //var totcomputi=< =TotaleComputi%>;
 	   
    if (aTipoAzione=='Inserisci'){
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciLiberazioneAnticipataCumulo";
      document.DettaglioLA.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.DettaglioLA.modalita.value = "I";
      
      document.DettaglioLA.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>.value = "";
      document.DettaglioLA.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>.value = "";
      document.DettaglioLA.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>.value = "";
      
      document.DettaglioLA.submit();
    }
    else if (aTipoAzione=='Modifica')
   	{
   	 	lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciLiberazioneAnticipataCumulo";
        document.DettaglioLA.modalita.value = "M";
        document.DettaglioLA.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.DettaglioLA.submit();
   	} 
    else if (aTipoAzione=='Cancella')
    {
        // Cancellazione fisica richiedo conferma
        
       /* if(totcomputi > 1)
        {
       	 var msgVai = "Eseguire la cancellazione da Elenco fungibilità! presenza di più periodi computati";
       	 if(window.confirm(msgVai) )
       	 {
       		 lAzione = "siap.siep.modulocumulo.action.ActRicercaFungibilitaCumulo";
               document.DettaglioLA.< %=IWebConstants.ACTION_FIELD%>.value = lAzione;
               document.DettaglioLA.submit();
       	 }	 
        }
        else
        {
      */	  
	          var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
	          if (window.confirm(msgConfirm)) 
	          {
	            lAzione = "siap.siep.modulocumulo.action.ActInserisciLiberazioneAnticipataCumulo";
	            document.DettaglioLA.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
	            document.DettaglioLA.modalita.value = "C";
	
	            document.DettaglioLA.submit();
	          }
       // }   
    }
    else if (aTipoAzione=='Indietro'){
      lAzione = "siap.siep.modulocumulo.action.ActRicercaLiberazioneAnticipataCumulo";
      document.DettaglioLA.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.DettaglioLA.submit();
    }
  }
  
  
  </script>
</head>

<body class="corpo">
<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Liberazione Anticipata&nbsp;</font>
      </td>
      <%// if (IstruttoriaCumulo.getFlagStato().equals("A") && 1==1)
      //{ %>
	      <%-- td class="LBG">
	        <a href="javascript:eseguiFunzione('Inserisci')">
	          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
	        <% if (!"C".equals(Provvedimento.getFlagStato()))
	           { %>
	           	<a href="javascript:eseguiFunzione('Modifica')">
	          	  <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
	        	<a href="javascript:eseguiFunzione('Cancella')">
	          	  <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>      
	        <% } %>
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

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettaglioLA">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(Provvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"      value="<%=StringUtils.toStringJSP(Provvedimento.getFlagStato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>" value="<%=StringUtils.toStringJSP(StringUtils.cStrForJS(Provvedimento.getMotivoModifica()), "") %>">
  
  <input type="hidden" name="<%=ICostantiLibAnticipataCumulo.CAMPO_ID_LIB_ANTICIPATA_CUMULO%>" value = "<%=StringUtils.toStringJSP(lIdLibAnt,"")%>" >
  <input type="hidden" name="modalita" value="">

<%
//==============================================================================
//         Sezione contenente il Riepilogo dei dati del Provvedimento
//==============================================================================
%>
<table cellspacing="2" cellpadding="2" width="90%">
  <tr>
    <td class="Titolo" colspan=10> Provvedimento</td>
  </tr>
</table>

<table width="90%" cellspacing="2" cellpadding="2" >
  <tr>
    <td class="l">Oggetto &nbsp;</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrMotivo())%></font>
    </td>
  </tr>
  <tr>
    <td class="l">Estremi Provvedimento &nbsp;</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrTipoProvvedimento())%>&nbsp;</font>
  <%if(Provvedimento.getAnnoProvvedimento()!=null )
    {  %>    
      N.
      <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getAnnoProvvedimento())%>/<%=StringUtils.toStringJSP(Provvedimento.getProgrProvvedimento())%>&nbsp;</font>
  <%} %> 
  
  <%if(Provvedimento.getAnnoProcedimento()!=null)
    {  %>   
      - Procedimeto SIUS N.
      <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getAnnoProcedimento())%>/<%=StringUtils.toStringJSP(Provvedimento.getProgrProcedimento())%>&nbsp;</font>
  <% } %>    
    </td>
  </tr>
  <tr>
    <td class="l" width="20%">Emesso il &nbsp;</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataEmissione(), "dd-MM-yyyy") )%>&nbsp;</font>
 	  da
      <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrUfficioEmittente())%>&nbsp; </font>
      di
      <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrLuogoEmittente())%>&nbsp; </font>
    </td>
  </tr>
  <tr>
    <td class="l">Esito &nbsp;</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrEsito())%></font>
    </td>
  </tr>


</table>
  <br>

<%
//=============================
if (PresenzaLA)
{
  Iterator ITX = Provvedimento.getListaLiberazioniAnticipate().iterator();


if(PresenzaPeriodi)
{
  
%>
<table width="90%">
  <tr>

<% if(Provvedimento.getCodEsito().equals("0020") || Provvedimento.getCodEsito().equals("0406") ) 
   {	%>
	<td class="Titolo" colspan="2"> Totale Giorni Concessi </td>
<% } else if(Provvedimento.getCodEsito().equals("0023") || Provvedimento.getCodEsito().equals("0407") ) { %>
    <td class="Titolo" colspan="2"> Totale Giorni Scomputati </td>
<% } %>

    <td class="Titolo"  colspan="4"> Relativamente ai Periodi </td>
  </tr>
<%
while(ITX.hasNext())
{
  LibAnticipataCumuloModel LAModel = (LibAnticipataCumuloModel)ITX.next();
  
  int Totgg=0;
  String Descrgg="";
  
  if(LAModel.getNumeroGiorni()!=null && LAModel.getNumeroGiorni().intValue() > 0 )
  {	
	Totgg = LAModel.getNumeroGiorni().intValue();	  
    if("LA".equals(LAModel.getTipoLa() ) ) 
    {	
       Descrgg = "Giorni di Liberazione Anticipata";
    }
    else if("LS".equals(LAModel.getTipoLa() ) ) 
    {	
       Descrgg = "Giorni di Liberazione Anticipata Speciale";
    }
    else if("LI".equals(LAModel.getTipoLa() ) ) 
    {	
       Descrgg = "Giorni di Integrazione Liberazione Anticipata";
    }
  }
   
  if(LAModel.getListaPeriodiLibAnticipate()!=null && LAModel.getListaPeriodiLibAnticipate().size() > 0)
  {
	  Vector<PeriodoLibAntCumuloModel> VecPerLA = (Vector)LAModel.getListaPeriodiLibAnticipate();
	  for(int k=0; k < VecPerLA.size(); k++)
	  {  
		PeriodoLibAntCumuloModel PeriodoMod = (PeriodoLibAntCumuloModel)VecPerLA.get(k);  %>
		<tr> 
<% 		if (k > 0)
        {   %>
          <td>&nbsp;</td>     
          <td>&nbsp;</td>  
<%      } 
		else
		{ %>
		  <td class="l" width="40%"><%=Descrgg%></td>
    	  <td class="L">
      		<font class="campo"><%=Totgg%></font>
    	  </td>
<%		} %>    	  
		  <td class="l" width ="5%"><font class="label"><%=k+1%>)</font></td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoMod.getDataInizio(),"dd-MM-yyyy"))%></font>&nbsp;&nbsp;&nbsp;&nbsp;
		  </td>
		  <td class="l" width ="5%"><font class="label">&nbsp;&nbsp; - &nbsp;&nbsp; </font></td>
 		  <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoMod.getDataFine(),"dd-MM-yyyy"))%></font>&nbsp;&nbsp;&nbsp;&nbsp;
		  </td>
        </tr>    
<%	  } // Chiude ciclo for()
	
  	} // chiude if(LAModel.getListaPeriodi!=null)   
  	else
  	{	%>
  	    <tr>
  	     <td class="l" width="40%"><%=Descrgg%></td>
    	  <td class="L">
      		<font class="campo"><%=Totgg%></font>
    	 </td>
    	 <td colspan=4>&nbsp;</td>
  		</tr>
<%	}
  }  // Chiude ciclo while()   %>
 
  </table>
<% 
}
else  // Else di if(prresenzaPeriodi)
{	
 %>
  <table width="65%">
    <tr>
	
<% if(Provvedimento.getCodEsito().equals("0020") || Provvedimento.getCodEsito().equals("0406") ) 
   {	%>
	  <td class="Titolo" colspan="2"> Totale Giorni Concessi </td>
<% } else if(Provvedimento.getCodEsito().equals("0023") || Provvedimento.getCodEsito().equals("0407") ) { %>
      <td class="Titolo" colspan="2"> Totale Giorni Scomputati </td>
<% } %>	  

    </tr>
 <%
  while(ITX.hasNext())
  {
     LibAnticipataCumuloModel LAModel = (LibAnticipataCumuloModel)ITX.next();
   
     int Totgg=0;
     String Descrgg="";
   
     if(LAModel.getNumeroGiorni()!=null && LAModel.getNumeroGiorni().intValue() > 0 )
     { 	
 		 Totgg = LAModel.getNumeroGiorni().intValue();	  
	     if("LA".equals(LAModel.getTipoLa() ) ) 
	     {	
	        Descrgg = "Giorni di Liberazione Anticipata";
	     }
	     else if("LS".equals(LAModel.getTipoLa() ) ) 
	     {	
	        Descrgg = "Giorni di Liberazione Anticipata Speciale";
	     }
	     else if("LI".equals(LAModel.getTipoLa() ) ) 
	     {	
	        Descrgg = "Giorni di Integrazione Liberazione Anticipata";
	     }
     }
 %>  
	   <tr>
	     <td class="l" width="40%"><%=Descrgg%></td>
    	  <td class="L">
      		<font class="campo"><%=Totgg%></font>
    	 </td>
	   </tr>
	   
<% 
   } // chiude ciclo while()		  
  %>
  </table>
  
<%
} // chiude Else di if(prresenzaPeriodi)


} // chiude if Presenza LA

%>  
<br>
<% if(Provvedimento.getNote()!=null && !"".equals(Provvedimento.getNote()) )
   {	%>
	<table cellspacing="2" cellpadding="2" width="90%">
	 <tr>
      <td class="l">Note :&nbsp;</td>
      <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getNote(), "")%></font>
      </td>
     </tr>
	</table>
<% } %>

</FORM>
</body>
</html>