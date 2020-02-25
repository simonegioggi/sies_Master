<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>

<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="depositoDecretoMotivazioni" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>

<jsp:useBean id="prescrizioni"               scope="request" class="java.util.Vector"/>
<jsp:useBean id="AutoTemplate"               scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoLibertatis"            scope="request" class="java.util.Vector"/>
<jsp:useBean id="tenori"                     scope="request" class="java.util.Vector"/>
<jsp:useBean id="LicenzePeriodi"      scope="request" class="java.util.Vector"/>


<html>
<head>
<title>[S.I.E.S.] - Modifica Decreto </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="/html/gestisciUploadStampa.js"></script>


</head>

  <body class="corpo">
  <form name="dettaglio">

  <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione : </font>
          <font class="campo">Modifica Decreto Revoca Liberazione Anticipata</font>&nbsp;
        </td>
   		<!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
    <jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>"/>

  <table cellspacing=4 cellpadding=4 width=95%>
  <tr>
    <input Title="Id Evento" type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>" >
  </tr>
  <tr>
        <jsp:include page="<%=ICostantiDepositoDecreto.PG_DETTAGLIO_DATA%>"/>
  </tr>
  	<tr>
		<td class="l"> Totale giorni Concessi</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNumeroGiorniRevocaLA()) %> </font></td>
	</tr>
	<br>
</table>

  <jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
   <jsp:param name="EveIdEvento" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>" />
   <jsp:param name="nextaction" value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito" />
  </jsp:include>
  
  	<table cellspacing=4 cellpadding=4>
	    <tr>
	      <td class="l">Ulteriore descrizione della decisione </td>
	      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( depositoDecretoMotivazioni.getDepositoDecreto().getNote() , "-")%></font></td>
	    </tr>
	      <tr> <td> <br></td></tr>
	</table>      
  
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
</form>
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  	<%-- jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_MODIFICA_ORDINANZA%>" --%>
  
  <jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_LOAD_MODIFICA_ORDINANZA_REVOCA_LA%>">
  <jsp:param name="EveIdEvento" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>" />
  <jsp:param name="tipo_provvedimento" value="decreto" />
</jsp:include>
</body>
</html>