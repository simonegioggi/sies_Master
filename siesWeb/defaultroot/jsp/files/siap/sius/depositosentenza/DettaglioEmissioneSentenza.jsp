<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>
<%@ page import="siap.sius.depositosentenza.action.ICostantiDepositoSentenza"%>

<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>
<%@ page import="siap.sius.esecuzionemisurasicurezza.action.ICostantiEsecuzioneMS"%>
<%@ page import="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel"%>

<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>

<jsp:useBean id="Modificabile"       scope="request" class="java.lang.String"/>
<jsp:useBean id="Stampabile"         scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoTemplate"     scope="request" class="java.lang.String"/>
<jsp:useBean id="flag"               scope="request" class="java.lang.String"/>
<jsp:useBean id="acdest"             scope="request" class="java.lang.String"/>
<jsp:useBean id="datiSentenza"       scope="request" class="siap.sius.depositosentenza.model.SentenzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="TornaQui"           scope="request" class="java.lang.String"/>
<jsp:useBean id="SentenzaRevocata"  scope="request" class="siap.sius.depositosentenza.model.SentenzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="modalita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="AbilitaModifica"    scope="request" class="java.lang.String"/>
<jsp:useBean id="misuresicurezza"    scope="request" class="java.util.Vector"/>
<jsp:useBean id="PeriodoAltraMisura" scope="request" class="siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel"/>
<jsp:useBean id="misuraSicurezza"    scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="LicenzePeriodi"     scope="request" class="java.util.Vector"/>

<%
//==============================================================================
// Jsp utilizzata per la visualizzazione del Dettaglio e anche in caso di 
// Modifica Sentenza
//==============================================================================
%>

<%
	BigDecimal IdEvento = (BigDecimal) request.getAttribute("IdEvento");
%>

<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<%
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  boolean ulterioreDescrizione = false;
  
  // Flag per indicare la modalità di Modifica Sentenza
  boolean modificaSentenza = false;
  if (modalita != null && modalita.trim().equalsIgnoreCase("M"))
    modificaSentenza = true;
  String titolo = "Dettaglio Sentenza";
  if (modificaSentenza)
    titolo = "Modifica Sentenza";
%>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Sentenza </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="/html/conferma.js"></script>

  <script language="JavaScript">
    function Verify()
    {
      return true;
    }
  </script>

</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG>
        <font class="label">Funzione : </font>
        <font class="campo"><%=titolo%></font>
      </td>

<%
  if (Modificabile == null || Modificabile.trim().length() < 1)
      Modificabile = "SI";

  if (Stampabile == null || Stampabile.trim().length() < 1)
      Stampabile = "SI";
  
  // Nel caso di Modifica Sentenza si elimina stampa e cancellazione
  if (modificaSentenza)
  {
    Modificabile = "NO";
    Stampabile = "NO";
  }

  if (Stampabile.compareTo("SI") == 0)
  {
    // Deve esistere il template : da list o predefinito.
    if ( ((ElencoTemplate != null) && (ElencoTemplate.trim().length() > 0)) || (datiSentenza.getEvento().getTemIdTemplate() != null && datiSentenza.getEvento().getTemIdTemplate().trim().length() > 1 ))
    {
%>
    <!-- BOTTONE DI STAMPA -->
    <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
      <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
      <jsp:param name="ValoreIdEntita" value="<%=IdEvento%>"/>
    </jsp:include>
<%
     } /* endif esistenza ElencoTemplate */
  }  /* endif Stampabile = SI */
  
  if( Modificabile.compareTo("SI") == 0)
  {
%>
  <td class="LBG">
    <a href="/jsp/Main.jsp?Action=siap.sius.provvedimento.action.ActLoadModificaProvvedimento&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=IdEvento%>&<%=ICostantiProvvedimento.CAMPO_TIPO_PROVVEDIMENTO%>=sentenza<%=retParam%>">
        <img  align="middle" src="/images/modifica24.gif" alt="Modifica Sentenza" width="24" height="24" border="0">
      </a>
    </td>
<%
  }
  
  boolean rimessioneAtti = false;
  if ((datiSentenza.getSentenza() != null) && (datiSentenza.getSentenza().getCodTipoSentenza() != null) &&
   (datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.RIMESSIONE_ATTI) == 0))
    rimessioneAtti = true;
          
  if ((Modificabile.compareTo("SI") == 0) && (!rimessioneAtti))
  {
%>
    <!-- BOTTONE DI CANCELLAZIONE -->
    <td class="LBG">
      <a href="Javascript:conferma('siap.sius.depositosentenza.action.ActCancellaEmissioneSentenza','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=IdEvento%>','TornaQui','<%=TornaQui%>');">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
      </a>
    </td>
<%
  }

  if ((Modificabile.compareTo("SI") == 0) && (rimessioneAtti))
  {
%>
    <!-- BOTTONE DI CANCELLAZIONE -->
    <td class="LBG">
      <a href="Javascript:conferma('siap.sius.depositosentenza.action.ActCancellaRimessioneAtti','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=IdEvento%>','TornaQui','<%=TornaQui%>');">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
      </a>
    </td>
<%
  }
%>
 
    	<!-- BOTTONE DI RITORNO -->
	    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
     <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    <tr>
      <jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
    </tr>

  </table>

<%
if ( (datiSentenza != null && datiSentenza.getSentenza() != null && datiSentenza.getSentenza().getCodTipoSentenza() != null && 
      datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.REVOCA_SENTENZA) != 0 ) || SentenzaRevocata == null || 
      SentenzaRevocata.getSentenza() == null || SentenzaRevocata.getSentenza().getIdDepositoSentenza() == null)
   {    %>
  		<jsp:include page="<%=ICostantiFascicoloSius.PG_SINTESIPROCEDIMENTOORIGINESIUS%>"/>
<% } %>

  <table cellspacing=4 cellpadding=4  width=95%>
    <tr>
      	<td>&nbsp;</td>
      	<input Title="Id Evento" type="hidden" name="<%= ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=IdEvento%>" >
    </tr>
  	<tr>
    	<td class="l"> Tipo di Sentenza</td>
    	<td class="l"> <font class="campo"><%=( datiSentenza != null &&  datiSentenza.getSentenza() != null && datiSentenza.getSentenza().getCodTipoSentenza() != null ) ? ( DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoSentenza(), datiSentenza.getSentenza().getCodTipoSentenza() )) : ""%>
		</font></td>
  	</tr>
<% if (!modificaSentenza){ %>
    <tr>
      <td class="L"><font class="label"> Data Emissione </font></td>
      <td class="L"><font class="campo"> <%=DateUtils.getDateToString(datiSentenza.getEvento().getDataEmissione(),"dd/MM/yyyy")%></font></td>
    </tr>
<%
}
if (datiSentenza.getSentenza().getDataDeposito() != null)
{ %>
  <tr>
    <td class="l"> Anno / Numero Sentenza</td>
    <td class="l">
      <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositosentenza.action.ActLoadInserisciDataDeposito&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=datiSentenza.getSentenza().getIdEventoGenerato()%><%=retParam%>">
         <%=StringUtils.toStringJSP( datiSentenza.getSentenza().getAnnoSentenza())%>
          /
         <%=StringUtils.toStringJSP( datiSentenza.getSentenza().getNumSentenza())%>
      </a>
    </td>
  </tr>
  <tr>
    <td class="l"> Data Deposito in Cancelleria</td>
    <td class="l"><font class="campo"> <%=DateUtils.getDateToString(datiSentenza.getSentenza().getDataDeposito(),"dd/MM/yyyy")%></font></td>
  </tr>
<%
}
%>
<% if (ulterioreDescrizione) { %>
  <tr>
    <td class="l">Ulteriore descrizione della decisione </td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiSentenza.getSentenza().getUlterioreDescrizione(), "-")%></font></td>
  </tr>
<% } %>
  <tr>
    <td class="l"> Stato del provvedimento</td>

<% if (datiSentenza.getEvento().getFlagDocumentoRegistrato() != null && datiSentenza.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("A"))
{ %>
    <td class="l"><font class="cRosso">ANNULLATO</font></td>
<% } else if (datiSentenza.getEvento().getFlagDocumentoRegistrato() != null && datiSentenza.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S"))
   {
%>
    <td class="l"><font class="campo">Validato</font></td>
<% } else
   {
%>
    <td class="l"><font class="campo">Da Validare </font></td>
<% } %>
</tr>

<%
if (datiSentenza.getEvento().getEveIdEventoRevoca() != null)
{ %>
  <tr>
    <td class="l"><font class="crosso"> Revocato</font></td>
</tr>
<% } %>

<%
  if(datiSentenza.getSentenza().getCodTipoSentenza() != null)
   if (datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.GENERICA) == 0 ||
	   datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.RIABILITAZIONE_SPECIALE) == 0 ||
	   datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.REVOCA_RIABILITAZIONE_SPECIALE) == 0 ||
	   datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.CORREZIONE_ERRORE_MATERIALE) == 0 ||
	   datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.RINVIO_UDIENZA) == 0 ||
	   datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.RIMESSIONE_ATTI) == 0
	   ) {%>
    <tr>
      <td class="L"><font class="label">Dispositivo</font></td>
      <td class="L"><font class="campo"> <%=StringUtils.toStringJSP(datiSentenza.getSentenza().getCodNaturaProvvedimento(),"-")%></font></td>
    </tr>
    <tr>
      <td class="l">Ulteriore descrizione della decisione </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiSentenza.getSentenza().getUlterioreDescrizione(), "-")%></font></td>
    </tr>
<% } 

if (!modificaSentenza)
{
   if ((datiSentenza.getSentenza().getCodTipoSentenza() != null) &&
      (datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.RIMESSIONE_ATTI) == 0) &&
      (datiSentenza.getSentenza().getCodNaturaProvvedimento() != null) )
   {
     %>
       <tr>
          <td class="l">Motivazioni </td>
          <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiSentenza.getSentenza().getCodNaturaProvvedimento(), "-")%></font></td>
      </tr>
<% } %>
  
     <tr>
       <td colspan=2>&nbsp;</td>
     </tr>
        
     <tr>
       <td class="Titolo" colspan="2"> Esiti</td>
      </tr>
  </table>
  
  <table cellspacing=4 cellpadding=4  width=95% >
   
  <%
    //Elenco Tenori.
    int lSize = datiSentenza.getTenori().length;
    for( int x=0; x<lSize; x++ )
    {
  %>
        <tr>
          <td class="l" width=35% ><%=datiSentenza.getTenori()[x].getDescrOggettoTenore()%></td>
          <td class="l" width=45% ><%=datiSentenza.getTenori()[x].getDescrEsitoTenore()%></td>
          
    <%    // Totale giorni per L.A. SPECIALE
        if( datiSentenza.getTenori()[x].getCodOggettoTenore().equals("2131") ||  	// L.A. Speciale
        	datiSentenza.getTenori()[x].getCodOggettoTenore().equals("1013")  || 	// Reclamo su L.A. Speciale
        	datiSentenza.getTenori()[x].getCodOggettoTenore().equals("0620")  ||	// Revoca su L.A. Speciale	TDS
        	datiSentenza.getTenori()[x].getCodOggettoTenore().equals("2136") )		// Revoca su L.A. Speciale	UDS
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
        <%        }
            }
          } %>
          
        <%  if(GiorniLS > 0) 
            { %>  
            <td class="l" width=20% ><%=StringUtils.toStringJSP(GiorniLS)%> giorni</td> 
      <%      }
          else
          { %>  
            <td class="l" width=20%>&nbsp;</td>         
    <%      }
        }
        // Totale giorni per L.A. INTEGRAZIONE
        else if(datiSentenza.getTenori()[x].getCodOggettoTenore().equals("2132") ||	// Ordinanza L.A. Integrazione
        		datiSentenza.getTenori()[x].getCodOggettoTenore().equals("1014")  || 		// Ordinanza Reclamo L.A. Integrazione 
        		datiSentenza.getTenori()[x].getCodOggettoTenore().equals("0621")  ||		// Ordinanza Revoca L.A. Integrazione TDS
        		datiSentenza.getTenori()[x].getCodOggettoTenore().equals("2137") )			//Ordinanza Revoca L.A. Integrazione UDS
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
      <%        } 
            }
          } %>

        <%  if(GiorniLI > 0) 
            { %>  
            <td class="l" width=20% ><%=StringUtils.toStringJSP(GiorniLI)%> giorni</td> 
      <%      }
          else
          { %>  
            <td class="l" width=20%>&nbsp;</td>         
    <%      }
        }
        // Totale giorni per L.A.normale
        else if(datiSentenza.getTenori()[x].getCodOggettoTenore().equals("2130") || 		// Ordinanza L.A. 
        		datiSentenza.getTenori()[x].getCodOggettoTenore().equals("0113") || 			// Ordinanza Reclamo L.A. 
        		datiSentenza.getTenori()[x].getCodOggettoTenore().equals("0028") ||			// Ordinanza revoca L.A. TDS
        		datiSentenza.getTenori()[x].getCodOggettoTenore().equals("2135") )				// Ordinanza Revoca L.A. UDS 
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
    <%            }
            }
            else
            { 
                GiorniLA = GiorniLA+lLicMod.getLicenza().getNumeroGiorni().intValue(); 
                %>
  <%          }
          } %>
          
        <%  if(GiorniLA > 0) 
            { %>  
            <td class="l" width=20% ><%=StringUtils.toStringJSP(GiorniLA)%> giorni</td> 
      <%      }
          else
          { %>  
            <td class="l" width=20%>&nbsp;</td>
  <%        }
        }
        // Inserimento del Destinatario nel caso della Rimessione Atti 
        if(  (datiSentenza.getSentenza() != null) &&
              (datiSentenza.getSentenza().getCodTipoSentenza() != null) &&
              (datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.RIMESSIONE_ATTI)) == 0) 
        { %>
                <td class="l" width=30% ><%=datiSentenza.getSentenza().getOggettoProcedimento()%></td>
      <%} %>
        
        </tr>
  <%
    } 

} // Chiude if (!modificaSentenza)
%>
    <tr>
      <td  colspan="2"> &nbsp;</td>
    </tr>

   </table>   

<% if (!modificaSentenza) { %>
  
 <form name="dettaglio">
<%
	if(datiSentenza.getSentenza().getCodTipoSentenza() != null)
	{
		if ( (datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.RIMESSIONE_ATTI) == 0)
			  || (datiSentenza.getSentenza().getCodTipoSentenza().compareTo("MA") == 0) 	
		   )
		{    
%>
			<jsp:include page="<%=ICostantiDepositoSentenza.PG_LOAD_DETTAGLIO_RIMESSIONE_ATTI %>"/>
<%
		}
	
		if (datiSentenza.getEvento().getEveIdEventoRevoca() != null)
		{ 
%>
		 	<jsp:include page="<%=ICostantiDepositoSentenza.PG_SINTESI_SENTENZA_REVOCA%>"/>
<% 		
		} 
%>

<%
	   if(datiSentenza.getSentenza().getCodTipoSentenza().trim().length() > 1 )
	   {
	   // Combo template di stampa solo sulle Emissioni di Sentenza Nuove
	   // E se non esiste il template predefinito.
	     if(datiSentenza.getEvento().getTemIdTemplate() == null || datiSentenza.getEvento().getTemIdTemplate().trim().length() < 2 )
	     {
%>
      		<jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
<%
     	 }
       }
    } // endif cod tipo ordinanza != null
%>
</form>

  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post">
    <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
            <input type="HIDDEN" name="IdEvento"  value="<%= IdEvento%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.depositosentenza.action.ActLoadDettaglioSentenza">
          </td>
        </tr>
        </table>
        </FORM>
      </div>
      
<% } // end if(!modificaSentenza) 
else
{ 
  //===========================
  // Sono in modifica
  //===========================

    // Rimessione Atti
  	if (datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.RIMESSIONE_ATTI) == 0){	
%>
	    <jsp:include page="<%=ICostantiDepositoSentenza.PG_MODIFICA_RIMESSIONE_ATTI%>" />
<%  
	// Riabilitazione
	} else if (datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.RIABILITAZIONE_SPECIALE) == 0){
%>
		<jsp:include page="<%=ICostantiDepositoSentenza.PG_MODIFICA_SENTENZA%>" />
<%
	// Revoca Riabilitazione
	} else if (datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.REVOCA_RIABILITAZIONE_SPECIALE) == 0){
%>
		<jsp:include page="<%=ICostantiDepositoSentenza.PG_MODIFICA_SENTENZA%>" />	
<%		
	// Correzione Errore Materiale
	} else if (datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.CORREZIONE_ERRORE_MATERIALE) == 0){
%>
		<jsp:include page="<%=ICostantiDepositoSentenza.PG_MODIFICA_SENTENZA%>" />		
<%	
	// Rinvio Udienza
	} else if (datiSentenza.getSentenza().getCodTipoSentenza().compareTo(ICostantiDepositoSentenza.RINVIO_UDIENZA) == 0){
%>
		<jsp:include page="<%=ICostantiDepositoSentenza.PG_MODIFICA_SENTENZA%>" />
<%		
	} else {
%>
		<jsp:include page="<%=ICostantiDepositoSentenza.PG_MODIFICA_SENTENZA%>" />
<%		
	}
  
} 
%>

</body>
</html>