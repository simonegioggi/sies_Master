<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata" %>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato" %>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="depositoDecretoMotivazioni"  scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori"                      scope="request" class="java.util.Vector"/>
<jsp:useBean id="prescrizioni"                scope="request" class="java.util.Vector"/>
<jsp:useBean id="AutoTemplate"                scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoPermesso"               scope="request" class="java.util.Vector"/>
<jsp:useBean id="revoca"                     scope="request" class="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"/>
<jsp:useBean id="decretoRevocato"  scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>
<jsp:useBean id="LicenzePeriodi"      scope="request" class="java.util.Vector"/>

<%
String nomeFunzione="";
String nomeCampoGiorni ="";
if (depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto().compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LIBERAZIONE_ANTICIPATA)== 0)
{
  nomeFunzione = "Dettaglio Decreto Revoca Liberazione Anticipata";
  nomeCampoGiorni = "Periodo scomputato";
}

// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//siesLogger.debug(" Dettaglio Decreto Revoca - getCodTipoDecreto = "+depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto());
%>

<html>
<head>
<title>[S.I.A.P.] - Dettaglio Decreto Revoca Liberazione Anticipata </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="/html/gestisciUploadStampa.js"></script>

</head>
  <body class="corpo">
  <form name="dettaglio">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione : </font>
          <font class="campo"><%=nomeFunzione%></font>&nbsp;
        </td>
        <jsp:include page="<%=ICostantiDepositoDecreto.BOTTONI_DETTAGLIO_DECRETO%>"/>
      </tr>
    </table>
    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%-- jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>"/ --%>
    <%-- jsp:include page="<%=ICostantiDepositoDecreto.PG_DETTAGLIO_DATA%>"/ --%>
    
    <table>
      	<tr>
    		<td class="l"> Tipo di Decreto</td>
    		<td class="l"> <font class="campo"><%=DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoDecreto(), depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto())%></font></td>
  		</tr>
    	<tr>
    		<td class="l"> Data Emissione</td>
    		<td class="l"><font class="campo"> <%=DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataEmissione(),"dd/MM/yyyy")%></font></td>
  		</tr>
<% 		if (depositoDecretoMotivazioni.getDepositoDecreto().getDataDeposito() != null)
		{ %>
			  <tr>
			    <td class="l"> Anno / Numero del Decreto</td>
			    <td class="l">
			       <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositodecreto.action.ActLoadInserisciDataDepositoDecreto&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoDecretoMotivazioni.getDepositoDecreto().getIdEventoGenerato()%>&TornaQui=<%=TornaQui%>">
			           <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getAnnoS72(),"" )%>
			           /
			           <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNumS72(),"" )%>
			        </a>
			    </td>
			  </tr>
			  <tr>
			    <td class="l"> Data Deposito in Cancelleria</td>
			    <td class="l"> <font class="campo"><%=DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataDeposito(),"dd/MM/yyyy")%></font></td>
			  </tr>
<% 		} %> 	
  		<tr>
    	<td class="l"> Stato del provvedimento</td>

<% 		if (depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato() != null && depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("A"))
		{ %>
    		<td class="l"><font class="cRosso">ANNULLATO</font></td>
<% 		} 
		else if (depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato() != null && depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S"))
   		{
%>
    		<td class="l"><font class="campo">Validato</font></td>
<% 		} 
		else
   		{
%>
    		<td class="l"><font class="campo">Da Validare </font></td>
<% 		} %>
	</tr>
	<tr>
		<td class="l"> Totale giorni Concessi</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNumeroGiorniRevocaLA()) %> </font></td>
	</tr>	
   </table>
<br>   

<!-- 								NEW								NEW									 -->
   <table cellspacing=4 cellpadding=4  width=90% >  
      <tr>
       <td class="Titolo" colspan="3"> Esiti</td>
     </tr>  
  <%
    //Elenco Tenori.
    
    int lSize = tenori.size();
  	Iterator lInd = tenori.iterator();
  	while (lInd.hasNext())
  	{  
  		TenoreModel lTen = (TenoreModel) lInd.next();
  %>
        <tr>
          <td class="l" width=30% ><%= lTen.getDescrOggettoTenore()%></td>
          <td class="l" width=40% ><%= lTen.getDescrEsitoTenore()%></td>
          
    <%    // Totale giorni per L.A. SPECIALE
        if( lTen.getCodOggettoTenore().equals("2131") ||  	// L.A. Speciale
          lTen.getCodOggettoTenore().equals("1013")  || 	// Reclamo su L.A. Speciale
          lTen.getCodOggettoTenore().equals("0620")  ||		// Revoca su L.A. Speciale	TDS
          lTen.getCodOggettoTenore().equals("2136") )		// Revoca su L.A. Speciale	UDS
		{
	          int GiorniLS=0;
	          Iterator Itrx = LicenzePeriodi.iterator();
	          while(Itrx.hasNext() )
	          { 
		            LicenzaPeriodiLibAnticipataModel lLicMod = (LicenzaPeriodiLibAnticipataModel) Itrx.next();
		            if( lLicMod.getLicenza().getDescrStatoPermesso() != null )
		            {
			              if(lLicMod.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LS") )
			              {
			                GiorniLS = GiorniLS + lLicMod.getLicenza().getNumeroGiorni().intValue(); 
			                %>      
			        <%    }
		            }
	          } %>
          
        <%    if(GiorniLS > 0) 
              { %>  
            		<td class="l" width=20% ><%=StringUtils.toStringJSP(GiorniLS)%> giorni</td> 
      <%      }
          	  else
          	  { %>  
            		<td class="l" width=20%>&nbsp;</td>         
    <%        }
        }
        // Totale giorni per L.A. INTEGRAZIONE
        else if(lTen.getCodOggettoTenore().equals("2132") ||	// Ordinanza L.A. Integrazione
            lTen.getCodOggettoTenore().equals("1014")  || 		// Ordinanza Reclamo L.A. Integrazione 
            lTen.getCodOggettoTenore().equals("0621")  ||		// Ordinanza Revoca L.A. Integrazione TDS
            lTen.getCodOggettoTenore().equals("2137") )			//Ordinanza Revoca L.A. Integrazione UDS
        { 
	          int GiorniLI=0;
	          Iterator Itrx = LicenzePeriodi.iterator();
	          while(Itrx.hasNext() )
	          { 
		            LicenzaPeriodiLibAnticipataModel lLicMod = (LicenzaPeriodiLibAnticipataModel) Itrx.next();
		            if( lLicMod.getLicenza().getDescrStatoPermesso() != null )
		            {
			              if(lLicMod.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LI") )
			              { 
			                GiorniLI = GiorniLI + lLicMod.getLicenza().getNumeroGiorni().intValue(); 
			                %>      
			      <%      } 
		            }
	          } %>
	
	        <%if(GiorniLI > 0) 
	          { %>  
	            <td class="l" width=20% ><%=StringUtils.toStringJSP(GiorniLI)%> giorni</td> 
	      <%  }
	          else
	          { %>  
	            <td class="l" width=20%>&nbsp;</td>         
	    <%    }
        }
        // Totale giorni per L.A.normale
        else if(lTen.getCodOggettoTenore().equals("2130") || 		// Ordinanza L.A. 
            lTen.getCodOggettoTenore().equals("0113") || 			// Ordinanza Reclamo L.A. 
            lTen.getCodOggettoTenore().equals("0028") ||			// Ordinanza revoca L.A. TDS
            lTen.getCodOggettoTenore().equals("2135") )				// Ordinanza Revoca L.A. UDS 
        {
	          int GiorniLA=0;
	          Iterator Itrx = LicenzePeriodi.iterator();
	          while(Itrx.hasNext() )
	          { 
		            LicenzaPeriodiLibAnticipataModel lLicMod = (LicenzaPeriodiLibAnticipataModel) Itrx.next();
		            if( lLicMod.getLicenza().getDescrStatoPermesso() != null )
		            { 
			              if(lLicMod.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LA") ) 
			              { 
			                GiorniLA = GiorniLA + lLicMod.getLicenza().getNumeroGiorni().intValue(); 
			                %>      
			    <%        }
		            }
		            else
		            { 
		                GiorniLA = GiorniLA+lLicMod.getLicenza().getNumeroGiorni().intValue(); 
		                %>
		  <%        }
	          } %>
	          
	        <%if(GiorniLA > 0) 
	          { %>  
	            <td class="l" width=20% ><%=StringUtils.toStringJSP(GiorniLA)%> giorni</td> 
	      <%  }
	          else
	          { %>  
	            <td class="l" width=20%>&nbsp;</td>
	  <%      }
	        
        }	// CHIUDE If else if (lTen.getCodOggettoTenore())
 %>
        </tr>
  <%
    }  // Chiude Iterator Tenori
%>
    <tr>
      <td  colspan="2"> &nbsp;</td>
    </tr>

   </table> 
 
         <jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
         <jsp:param name="EveIdEvento" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>" />
         <jsp:param name="nextaction" value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito" />
         </jsp:include>

    	 <tr>
      		<input Title="Id Evento" type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>" >
    	 </tr>
   
<!-- 							END NEW								END NEW									END NEW				 -->

	<table cellspacing=4 cellpadding=4>
	    <tr>
	      <td class="l">Ulteriore descrizione della decisione </td>
	      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( depositoDecretoMotivazioni.getDepositoDecreto().getNote() , "-")%></font></td>
	    </tr>
	      <tr> <td> <br></td></tr>
		<tr>
	      <td class="Titolo" colspan=2> Estremi provvedimento Revocato: <td>
	  	</tr>  
	    <tr>
	      <td class="l"> Data Emissione</td>
	      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataEmissione(),"dd/MM/yyyy"),"-")%></font></td>
	    </tr>
	    <tr>
	      <td class="l">Ufficio Sorveglianza emittente </td>
	      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	      <%-- td class="l"><font class="campo"><%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font></td --%>
	      <td class="l"><font class="campo"> - </font></td>
	    </tr>
	<br>	        
	</table>  
	
	<jsp:include page="<%=ICostantiLibertaAnticipata.PG_DETTAGLIO_REVOCA_LIBANTICIPATA%>"/>

<!--  Modelli di STAMPA -->
   	 <jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
  </form>

  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post">
    <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
        <td class="L">
          <input class="bottone"  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
          <input type="HIDDEN" name="IdEvento"  value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito">
          <input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.EMISSIONE_DECRETO%>">
        </td>
      </tr>
    </table>
    </FORM>
  </div>
</body>
</html>