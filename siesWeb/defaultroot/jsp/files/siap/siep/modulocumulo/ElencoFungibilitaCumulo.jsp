<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel "%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ListaFungibilita"     scope="request" class="java.util.Vector"/>

<!-- 			ElencoFungibilitaCumulo				 -->
<%
//==============================================================================
// Form per la visualizzazione dei Fungibilita legati a un certo Titolo.
//
// La form presenta un elenco dei Fungibilita già presenti con la possibilità di 
// modificarle, cancellarle o inserirne delle nuove 
//==============================================================================
%>

<html>
<head>
  <title> Elenco Fungibilità</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // Visualizza, nasconde il record con il dettaglio dell'istruttoria
    //==========================================================================
    function visualizzaMotivo(idRecord)
    {
		var riga = document.getElementById(idRecord);  
	    if (riga.style.display =="none" )
	    {
	        riga.style.display = "block";
	    }
	    else 
	    {
	        riga.style.display = "none";
	    }
    }
    
    //==========================================================================
    // Richiama l'opportuna azione
    //==========================================================================
    function eseguiAzione(aTipoAzione, aIdProvv, aIdComputo)
    {
      if (aTipoAzione=='Dettaglio'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadDettaglioFungibilitaCumulo";
        document.ListaFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>.value = aIdProvv;
        document.ListaFungibilitaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaFungibilitaCumulo.submit();
      }
      else if (aTipoAzione=='Cancella')
      {
      	document.ListaFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>.value = aIdComputo;
      	document.ListaFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdProvv;
        document.ListaFungibilitaCumulo.modalita.value = "C";

         // Cancellazione fisica richiedo conferma
         var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
         if (window.confirm(msgConfirm)) {
            lAzione = "siap.siep.modulocumulo.action.ActInserisciFungibilitaCumulo";
            document.ListaFungibilitaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;

            document.ListaFungibilitaCumulo.submit();
         }
      }
    }
    
  //==========================================================================
    // Richiama la funzione di Modifica (Provvedimento e/o Periodo fungibilita)
    //==========================================================================
    function eseguiModifica(aIdProvv, aIdComputo)
    {
   	 	lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciFungibilitaCumulo";
        document.ListaFungibilitaCumulo.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>.value = aIdProvv;
        document.ListaFungibilitaCumulo.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>.value = aIdComputo;
        document.ListaFungibilitaCumulo.modalita.value = "M";
        document.ListaFungibilitaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaFungibilitaCumulo.submit(); 
    }
  
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function nuovaFungibilita(){
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciFungibilitaCumulo";
      document.ListaFungibilitaCumulo.modalita.value = "I";
      document.ListaFungibilitaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.ListaFungibilitaCumulo.submit();
    }
    
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.ListaFungibilitaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.ListaFungibilitaCumulo.submit();
    }

  </script>
  
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Provvedimenti fungibilita' &nbsp;</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia dei dati analitici -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="<%=ICostantiModuloCumulo.PG_DETTAGLIO_ISTRUTTORIA_INCLUDE%>"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
      </td>
    </tr>
  </table>

<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="ListaFungibilitaCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

 
  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="">
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>"            value="">
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO%>"                 value="">
  <input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>" value="">
  <input type="hidden" name="modalita"   value="">


 <%if (ListaFungibilita == null || ListaFungibilita.size() == 0 )	
   { %>
	  <table cellspacing="2" cellpadding="2" align="center" width="95%">
	    <tr>
	      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
	      <td class="int">Provvedimento</td>
	      <td class="int">Emesso in data</td>
	      <td class="int">Periodo dal</td>
	      <td class="int">Periodo al</td>
	      <td class="int">Anni</td>
	      <td class="int">Mesi</td>
	      <td class="int">Giorni</td>
	      <td class="int">Stato</td>
	      <td class="int">Note</td>
	      <td class="int">Azioni</td>
	    </tr>
	    <tr>
      		<td colspan="10">Nessun dato presente</td>
    	</tr>
      </table>
<%}
  else
  {		%>
	  <table cellspacing="2" cellpadding="2" align="center" width="95%">
	    <tr>
	      <td class="int">Provvedimento</td>
	      <td class="int">Emesso in data</td>
	      <td class="int">Periodo dal</td>
	      <td class="int">Periodo al</td>
	      <td class="int">Anni</td>
	      <td class="int">Mesi</td>
	      <td class="int">Giorni</td>
	      <td class="int">Stato</td>
	      <td class="int">Note</td>
	      <td class="int">Azioni</td>
	    </tr> 
  
<%    int id_record = 0;

      Iterator itx = ListaFungibilita.iterator();
      while ( itx.hasNext()) 
      {
        id_record = id_record +1;
        StatoEsecTitoloCumulatoModel lprovvedimento = (StatoEsecTitoloCumulatoModel)itx.next();
        
        String lStato = "";
        String lDescStato = "";
        String lFontColor = "";
        
        if      ( lprovvedimento.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
        else if ( lprovvedimento.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
        else if ( lprovvedimento.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
        else if ( lprovvedimento.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
      
 	 	if(lprovvedimento.getListaComputi()!=null && lprovvedimento.getListaComputi().size() >0)
	 	{
	 
		 	int id_record_comp = 0;
		 	Iterator itxC = lprovvedimento.getListaComputi().iterator();
		 	while ( itxC.hasNext()) 
		    {	
		 		id_record_comp = id_record_comp +1;
		 		ComputiCumuloModel lComputi = (ComputiCumuloModel) itxC.next(); 	%>
		 		  <tr>
	<% 	 		if(id_record_comp == 1)
		 		{	%>
		 			<td class="l" <%=lFontColor%> ><%=StringUtils.toStringJSP(lprovvedimento.getDescrTipoProvvedimento()+" "+lprovvedimento.getDescrMotivo(),"&nbsp;")%></td>
	      		    <td class="c" <%=lFontColor%> nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lprovvedimento.getDataEmissione(),"dd-MM-yyyy"),"-")%></td>
	      		    
		 			<td class="c" <%=lFontColor%> nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputi.getDataReclusioneDa() ,"dd-MM-yyyy"),"-")%></td>
		 	      	<td class="c" <%=lFontColor%> nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputi.getDataReclusioneA(),"dd-MM-yyyy"),"-")%></td>
		 	      
		 	      	<td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lComputi.getNumAnniReclusione(),"&nbsp;")%></td>
		 	      	<td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lComputi.getNumMesiReclusione(),"&nbsp;")%></td>
		 	      	<td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lComputi.getNumGiorniReclusione(),"&nbsp;")%></td>	
		 <% 	}
		 		else if(id_record_comp > 1)
		 		{	%>     
	       			<td class="l"></td>
		      		<td class="c"></td>
		      		<td class="c" <%=lFontColor%> nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputi.getDataReclusioneDa() ,"dd-MM-yyyy"),"-")%></td>
	      			<td class="c" <%=lFontColor%> nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputi.getDataReclusioneA(),"dd-MM-yyyy"),"-")%></td>
	      
	      			<td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lComputi.getNumAnniReclusione(),"&nbsp;")%></td>
	      			<td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lComputi.getNumMesiReclusione(),"&nbsp;")%></td>
	      			<td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lComputi.getNumGiorniReclusione(),"&nbsp;")%></td>
		    <%	} %>
		    
		    		<td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
		    
		    	         <!--     Motivo Inserimento/Modifica   -->
			<%  if (lprovvedimento.getMotivoModifica()!=null && lprovvedimento.getMotivoModifica().length()>0) 
				{ %>
				     <td class="c">
				        <a href="javascript:visualizzaMotivo('rec_<%=id_record%>')" title="motivo inserimento/modifica>">
				        note
				        </a>
				     </td>  
			 <% }
			  	else 
			  	{	%>
				      <td class="c" >&nbsp; - &nbsp;</td>
			 <% }	 %>
				      
				<!-- 		Azioni		 -->	
			    	<td class="c" style="text-align:center" nowrap> &nbsp;
				 <%
				        //======================================================================
				        // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
				       //======================================================================
				      %>
		        	<a href="javascript:eseguiAzione('Dettaglio',<%=lprovvedimento.getIdStatoEsecTitoloCumulato() %> )">
		          	  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
				        
				        <% 
		        	//======================================================================
				    // Modifiche consentite solo ad istrittoria aperta e su dati non Cancellati
				    //======================================================================
				    if(IstruttoriaCumulo.getFlagStato().equals("A") && !lprovvedimento.getFlagStato().equals("C"))
				    { %>
				        <a href="javascript:eseguiModifica(<%=lprovvedimento.getIdStatoEsecTitoloCumulato() %>,<%=lComputi.getIdComputiCumulo()%> )">
				          <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
				        <a href="javascript:eseguiAzione('Cancella',<%=lprovvedimento.getIdStatoEsecTitoloCumulato() %>, <%=lComputi.getIdComputiCumulo()%>)">
				          <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
				 <% } %>
				      </td>
				    </tr>
		<%   }	
	    
 		}	%>
 		
 		 <!-- Record Hidden con le note di Motivo/Modifica-->
    <tr style="display:none" id="rec_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(lprovvedimento.getMotivoModifica(),"&nbsp;")%>
        </font>
      </td>
    </tr>
 	 	
<%    }	// end while su iterator	%>

	</table>

<%} // end else di if (ListaFungibilita == null || ListaFungibilita.size() == 0 ) %>
    
   
  <% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
  <table cellspacing="2" cellpadding="2" align="center" width="95%">    
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci nuovo Provvedimento" onClick="javascript:nuovaFungibilita();">
      </td>
    </tr>
  </table>
  <% } %>
</FORM>
</body>
</html>