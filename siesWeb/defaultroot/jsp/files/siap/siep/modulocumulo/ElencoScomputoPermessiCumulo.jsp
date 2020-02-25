<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.modulocumulo.action.ICostantiLibAnticipataCumulo"%>
<%@page import="siap.siep.modulocumulo.model.LibAnticipataCumuloModel"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ListaScomputi"     scope="request" class="java.util.Vector"/>

<!-- 			ElencoRimediRisarcitoriDL201492Cumulo				 -->
<%
//==============================================================
// La form presenta un elenco provvedimenti Scomputo Permessi 
//	legati a un certo Titolo con la possibilità di 
// modificare, cancellare o inserire nuovi provvedimenti
//==============================================================
%>

<html>
<head>
  <title> Elenco Scomputi </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // Visualizza, nasconde il record con le Note di StatoEsecuzione
    //==========================================================================
    function visualizzaNote(idRecord)
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
    function eseguiAzione(aTipoAzione, aIdProvv, aIdLibAnt)
    {
      if (aTipoAzione=='Dettaglio')
      {
        lAzione = "siap.siep.modulocumulo.action.ActDettaglioScomputoPermessiCumulo";
        document.ListaScomputiCum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>.value = aIdProvv;
        document.ListaScomputiCum.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaScomputiCum.submit();
      }
      else if (aTipoAzione=='Cancella')
      {
      	document.ListaScomputiCum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>.value = aIdProvv;
      	document.ListaScomputiCum.<%=ICostantiLibAnticipataCumulo.CAMPO_ID_LIB_ANTICIPATA_CUMULO %>.value = aIdLibAnt;
        document.ListaScomputiCum.modalita.value = "C";

         // Cancellazione fisica richiedo conferma
         var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
         if (window.confirm(msgConfirm)) {
            lAzione = "siap.siep.modulocumulo.action.ActInserisciScomputoPermessiCumulo";
            document.ListaScomputiCum.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;

            document.ListaScomputiCum.submit();
         }
      }
      
    }
    
  	//==========================================
    // Richiama la funzione di Modifica 
    //==========================================
    function eseguiModifica(aIdProvv, aIdLibAnt)
    {
   	 	lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciScomputoPermessiCumulo";
        document.ListaScomputiCum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>.value = aIdProvv;
        document.ListaScomputiCum.<%=ICostantiLibAnticipataCumulo.CAMPO_ID_LIB_ANTICIPATA_CUMULO %>.value = aIdLibAnt;
        document.ListaScomputiCum.modalita.value = "M";
        document.ListaScomputiCum.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaScomputiCum.submit(); 
    }
  
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function nuovoScomputo(){
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciScomputoPermessiCumulo";
      document.ListaScomputiCum.modalita.value = "I";
      document.ListaScomputiCum.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.ListaScomputiCum.submit();
    }
    
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.ListaScomputiCum.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.ListaScomputiCum.submit();
    }

  </script>
  
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Provvedimenti Scomputo Permessi  &nbsp;</font>
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
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
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

<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="ListaScomputiCum">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

 
  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="">
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>"            value="">
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO%>"                 value="">
  <input type="hidden" name="<%=ICostantiLibAnticipataCumulo.CAMPO_ID_LIB_ANTICIPATA_CUMULO %>" value="">
  <input type="hidden" name="modalita"   value="">


<%--  <%if ( (ListaScomputi == null || ListaScomputi.size() == 0) && 1==2 )	 --%>
<%--    { %> --%>
<!-- 	  <table cellspacing="2" cellpadding="2" align="center" width="98%"> -->
<!-- 	    <tr> -->
<%-- 	      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %> --%>
<!-- 	      <td class="int">Provvedimento</td> -->
<!-- 	      <td class="int">Emesso in data</td> -->
<!-- 	      <td class="int">Esito</td> -->
<!-- 	      <td class="int">Giorni</td> -->
<!-- 	      <td class="int">Stato</td> -->
<!-- 	      <td class="int">Note</td> -->
<!-- 	      <td class="int">Azioni</td> -->
<!-- 	    </tr> -->
<!-- 	    <tr> -->
<!--       		<td colspan="10">Nessun dato presente</td> -->
<!--     	</tr> -->
<!--       </table> -->
<%-- <%} --%>
<!--    else -->
<%--   {		%> --%>
	  <table cellspacing="2" cellpadding="2" align="center" width="98%">
	    <tr>
	      <td class="int">Provvedimento</td>
	      <td class="int">Emesso in data</td>
	      <td class="int">Esito</td>	
	      <td class="int">Giorni</td>
	      <td class="int">Stato</td>
	      <td class="int">Note</td>
	      <td class="int">Azioni</td>
	    </tr> 
  
<%		int id_record = 0;
		Iterator itx = ListaScomputi.iterator();
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
	      
	 	 	if(lprovvedimento.getListaLiberazioniAnticipate()!=null && lprovvedimento.getListaLiberazioniAnticipate().size() >0)
		 	{
			 	int id_rec = 0;
			 	Iterator itxL = lprovvedimento.getListaLiberazioniAnticipate().iterator();
			 	while ( itxL.hasNext()) 
			    {	
			 		id_rec = id_rec +1;
			 		LibAnticipataCumuloModel lLibAnt = (LibAnticipataCumuloModel) itxL.next();   %>
					<tr>
<% 			 		if(lLibAnt!=null && lLibAnt.getIdLibAnticipataCumulo()!=null)  
			 		{
			 			if(id_rec == 1)
				 		{
%>
			 			  <td class="l" <%=lFontColor%> ><%=StringUtils.toStringJSP(lprovvedimento.getDescrTipoProvvedimento(),"&nbsp;")%>&nbsp; <%=StringUtils.toStringJSP(lprovvedimento.getDescrMotivo(),"&nbsp;")%></td>
		      		      <td nowrap class="c" <%=lFontColor%> ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lprovvedimento.getDataEmissione(),"dd-MM-yyyy"),"-")%></td>
			 	      	  <td class="c" <%=lFontColor%> > <%=StringUtils.toStringJSP(lprovvedimento.getDescrEsito(),"&nbsp;")%> </td>
			 	      	  <td nowrap class="c" <%=lFontColor%> >&nbsp; <%=StringUtils.toStringJSP(lLibAnt.getNumeroGiorni(),"&nbsp;")%> </td>
<% 	 					}
			 			else if(id_rec > 1)
			 			{	  %>		
			 			  <td class="l"></td>
		      			  <td class="c"></td>
			 	      	  <td class="c" <%=lFontColor%> >&nbsp; <%=StringUtils.toStringJSP(lprovvedimento.getDescrEsito(),"&nbsp;")%> </td>
			 	      	  <td nowrap class="c" <%=lFontColor%> >&nbsp; <%=StringUtils.toStringJSP(lLibAnt.getNumeroGiorni(),"&nbsp;")%> </td>
<%						} %>
	    
			    		  <td class="c" <%=lFontColor%> >&nbsp; <%=lStato%></td>

		<%	//		Inserire le NOTE  %>
				<%  if (lprovvedimento.getNote()!=null && lprovvedimento.getNote().length()>0) 
					{ %>
				     <td class="c">
				        <a href="javascript:visualizzaNote('rec_<%=id_record%>')" title="Note ">
				        note
				        </a>
				     </td>  
			 <% 	}
			  		else 
			  		{	%>
				      <td class="c" >&nbsp; - &nbsp;</td>
			 <% 	}	 %>
	
				      
		<!-- 		Azioni		 -->	
			    		 <td nowrap class="c" style="text-align:center"> &nbsp;

<%			//======================================================================
			// Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
			//======================================================================
%>
		        		<a href="javascript:eseguiAzione('Dettaglio', <%=lprovvedimento.getIdStatoEsecTitoloCumulato() %> )">
		          	  	  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
				        
<%         	//======================================================================
		    // Modifiche consentite solo ad istrittoria aperta e su dati non Cancellati
		    //======================================================================
			    	  if(IstruttoriaCumulo.getFlagStato().equals("A") && !lprovvedimento.getFlagStato().equals("C"))
					  { %>
				    	<a href="javascript:eseguiModifica( <%=lprovvedimento.getIdStatoEsecTitoloCumulato()%>, <%=lLibAnt.getIdLibAnticipataCumulo()%> )">
				       	  <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
				    	<a href="javascript:eseguiAzione('Cancella', <%=lprovvedimento.getIdStatoEsecTitoloCumulato()%>, <%=lLibAnt.getIdLibAnticipataCumulo()%> )">
				       	  <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
		 <%  		  } %>
				    	 </td>
 	 	
<%					} // Chiude if(lLibAnt!=null && lLibAnt.getIdLibAnticipataCumulo()!=null)  %>
				</tr>
<% 				}	// end while su iterator ItxL
 				 
		 	}  //	Chiude if(lprovvedimento.getListaLiberazioniAnticipate()!=null	%>
		 	
	<!-- Record Hidden con le note di Motivo/Modifica-->
    <tr style="display:none" id="rec_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(lprovvedimento.getNote(),"&nbsp;")%>
        </font>
      </td>
    </tr>
		 	
<%     	}  // Chiude ciclo while 	%>

	</table>

<%-- <% } // end else di if (ListaScomputi == null || ListaScomputi.size() == 0 ) %> --%>
    
   
  <% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
  <table cellspacing="2" cellpadding="2" align="center" width="98%">    
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci nuovo Provvedimento" onClick="javascript:nuovoScomputo();">
      </td>
    </tr>
  </table>
  <% } %>
</FORM>
</body>
</html>