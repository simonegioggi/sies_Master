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
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>

<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>
<%@ page import="siap.sius.esecuzionemisurasicurezza.action.ICostantiEsecuzioneMS"%>
<%@ page import="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel"%>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato" %>

<jsp:useBean id="Modificabile"    scope="request" class="java.lang.String"/>
<jsp:useBean id="Stampabile"      scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoTemplate"  scope="request" class="java.lang.String"/>
<jsp:useBean id="flag"            scope="request" class="java.lang.String"/>
<jsp:useBean id="acdest"          scope="request" class="java.lang.String"/>
<jsp:useBean id="datiOrdinanza"   scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="TornaQui"        scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="AbilitaModifica"   scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioProcura"  scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficioTDS"    scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficioUDS"    scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="misuraAlternativa"      scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>

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
  
  //Flag per indicare la modalità di Modifica Ordinanza
 boolean modificaOrdinanza = false;
 if (modalita != null && modalita.trim().equalsIgnoreCase("M"))
    modificaOrdinanza = true;
 
  String titolo = "Dettaglio Ordinanza: Ammissione Provvisoria all'Affidamento in Prova";
  if (modificaOrdinanza)
    titolo = "Modifica Ordinanza: Ammissione Provvisoria all'Affidamento in Prova";

 %>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Ordinanza di Misura Alternativa :Ammissione Provvisoria all'Affidamento in Prova</title>

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
    
    // Nel caso di Modifica Ordinanza si elimina stampa e cancellazione
    if (modificaOrdinanza)
    {
      Modificabile = "NO";
      Stampabile = "NO";
    }

  if (Stampabile.compareTo("SI") == 0)
  {
      // Deve esistere il template : da list o predefinito.
      if ( ((ElencoTemplate != null) && (ElencoTemplate.trim().length() > 0)) || (datiOrdinanza.getEvento().getTemIdTemplate() != null && datiOrdinanza.getEvento().getTemIdTemplate().trim().length() > 1 ))
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
  
  /* ANGELA è stato aggiunto il bottone di modifica*/
  if( Modificabile.compareTo("SI") == 0)
  // if (AbilitaModifica.compareTo("SI") == 0) 
   {
%>
  <td class="LBG">
  <a href="/jsp/Main.jsp?Action=siap.sius.provvedimento.action.ActLoadModificaProvvedimento&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=IdEvento%>&<%=ICostantiProvvedimento.CAMPO_TIPO_PROVVEDIMENTO%>=03<%=retParam%>">
        <img  align="middle" src="/images/modifica24.gif" alt="Modifica Ordinanza" width="24" height="24" border="0">
      </a>
    </td>                 
<%
   }
  
  boolean rimessioneAtti = false;
  if ((datiOrdinanza.getOrdinanza() != null) && (datiOrdinanza.getOrdinanza().getCodTipoOrdinanza() != null) &&
   (datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.RIMESSIONE_ATTI) == 0))
    rimessioneAtti = true;
          
  if ((Modificabile.compareTo("SI") == 0) && (!rimessioneAtti))
  {
%>
    <!-- BOTTONE DI CANCELLAZIONE -->
    <td class="LBG">
      <a href="Javascript:conferma('siap.sius.depositoordinanzapc.action.ActCancellaEmissioneOrdinanza','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=IdEvento%>','TornaQui','<%=TornaQui%>');">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
      </a>
    </td>
    <!-- --------------------------->
<%
  }

  if ((Modificabile.compareTo("SI") == 0) && (rimessioneAtti))
  {
%>
    <!-- BOTTONE DI CANCELLAZIONE -->
    <td class="LBG">
      <a href="Javascript:conferma('siap.sius.depositoordinanzapc.action.ActCancellaRimessioneAtti','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=IdEvento%>','TornaQui','<%=TornaQui%>');">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
      </a>
    </td>
    <!-- --------------------------->
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

  <table cellspacing=4 cellpadding=4  width=95%>
    <tr>
        <td>&nbsp;</td>
        <input Title="Id Evento" type="hidden" name="<%= ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=IdEvento%>" >
    </tr>
    <tr>
        <td class="Titolo" colspan="2"> ORDINANZA </td>
    </tr>
    <tr>
      <td class="l"> Tipo di Ordinanza</td>
      <td class="l">
        <font class="campo">
            <%=( datiOrdinanza != null &&  datiOrdinanza.getOrdinanza() != null && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza()!= null ) ? ( DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoOrdinanza(), datiOrdinanza.getOrdinanza().getCodTipoOrdinanza() )) : ""%>
      </font>
      </td>
    </tr>
<% if (!modificaOrdinanza)
   { %>
      <tr>
        <td class="L"><font class="label"> Data Emissione </font></td>
        <td class="L"><font class="campo"> <%=DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataCameraConsiglio() ,"dd/MM/yyyy")%></font></td>
      </tr>
<%
  }
if (datiOrdinanza.getOrdinanza().getDataDeposito() != null)
{ %>
    <tr>
      <td class="l"> Anno / Numero Ordinanza</td>
      <td class="l">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositoordinanzapc.action.ActLoadInserisciDataDeposito&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=datiOrdinanza.getOrdinanza().getIdEventoGenerato()%><%=retParam%>">
           <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getAnnoS3())%>
            /
           <%=StringUtils.toStringJSP(   datiOrdinanza.getOrdinanza().getNumS3())%>
        </a>
      </td>
    </tr>
    <tr>
      <td class="l"> Data Deposito in Cancelleria</td>
      <td class="l"><font class="campo"> <%=DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataDeposito(),"dd/MM/yyyy")%></font></td>
    </tr>
<%
}
%>
<%  if (ulterioreDescrizione) 
  { %>
    <tr>
      <td class="l">Ulteriore descrizione della decisione </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
    </tr>
<% } %>
  <tr>
    <td class="l"> Stato del provvedimento</td>

<%  if (datiOrdinanza.getEvento().getFlagDocumentoRegistrato() != null && datiOrdinanza.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("A"))
  { %>
      <td class="l"><font class="cRosso">ANNULLATO</font></td>
<%  } 
  else if (datiOrdinanza.getEvento().getFlagDocumentoRegistrato() != null && datiOrdinanza.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S"))
    {
%>
      <td class="l"><font class="campo">Validato</font></td>
<%  }
  else
    {
%>
      <td class="l"><font class="campo">Da Validare </font></td>
<%  } %>
</tr>

<%
  if (datiOrdinanza.getEvento().getEveIdEventoRevoca() != null)
  { %>
      <tr>
        <td class="l"><font class="crosso"> Revocato</font></td>
      </tr>
<%  } %>

  <%  if (datiOrdinanza.getOrdinanza().getUlterioreDescrizione() != null && !datiOrdinanza.getOrdinanza().getUlterioreDescrizione().equals("-")) 
    { %>
      <tr>
        <td class="l">Ulteriore Descrizione </td>
        <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
      </tr>
  <%  } %>

    <%if (datiOrdinanza.getOrdinanza().getCodUfficioMagistratoComp()!= null && !datiOrdinanza.getOrdinanza().getCodUfficioMagistratoComp().equals("-")) 
    { %>
      <tr>
        <td class="l">Magistrato di Sorveglianza Competente </td>
        <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( ufficioUDS.getDescrTipoUfficio())%>
                          di 
                          <%=StringUtils.toStringJSP( ufficioUDS.getDescrComune() )%>
        </font></td>
      </tr>
    <% }  %>
    
    <%if (datiOrdinanza.getOrdinanza().getCodUffTdsConcessoRiduzione() != null && !datiOrdinanza.getOrdinanza().getCodUffTdsConcessoRiduzione().equals("-")) 
    { %>
      <tr>
        <td class="l">Tribunale di Sorveglianza Competente </td>
        <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( ufficioTDS.getDescrTipoUfficio())%>
                          di 
                          <%=StringUtils.toStringJSP( ufficioTDS.getDescrComune() )%>       
        </font></td>
      </tr>
    <% }  %>
  
    <%if (datiOrdinanza.getOrdinanza().getAutoritaVigilante() != null ) 
    { %>
      <tr>
        <td class="l">Procura Competente </td>
        <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( ufficioProcura.getDescrTipoUfficio())%>
                          di 
                          <%=StringUtils.toStringJSP( ufficioProcura.getDescrComune() )%>       
                     </font>
        </td>
      </tr>
    <% }  %>  

      <tr>
        <td class="l"> Luogo Svolgimento della Prova </td>
        <td class="l">
         <font class="campo"> 
            <%= StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getLuogoSvolgimentoProva(), "-" ) %> 
         </font>
        </td>
      </tr>  

<!--  Stato Libertatis -->
      <tr>
        <td class="l">Stato Libertà Personale</td>

<%    if (datiOrdinanza.getOrdinanza().getNumMesiArrestoRev() != null ) 
    { 
        if(datiOrdinanza.getOrdinanza().getNumMesiArrestoRev().toString().equals("1") )
        { %>        
          <td class="l"><font class="campo"> LIBERO </font></td>
    <%    }
        else if(datiOrdinanza.getOrdinanza().getNumMesiArrestoRev().toString().equals("2") )
        {   %>  
          <td class="l"><font class="campo"> DETENUTO </font></td>
    <%    } 
        else 
        { %>        
          <td class="l"><font class="campo">  -  </font></td> 
<%        }       
    }
    else
    { 
      %>    
      <td class="l"><font class="campo">  -  </font></td> 
<%    }  %>
  
    </tr>
    
    <tr>
      <td class="l">Tipo Controllo Esecuzione</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrTipoControlloEsecuzione(), "-" )%>
        </font>
      </td>
    </tr>

<!--  ESITI  -->

<%  if (!modificaOrdinanza) 
  { %>        
      <tr>
        <td colspan=2>&nbsp;</td>
      </tr>
  
      <tr>
        <td class="Titolo" colspan="2"> Esiti</td>
      </tr>

<%
      //Elenco Tenori.
      int lSize = datiOrdinanza.getTenori().length;
      for( int x=0; x<lSize; x++ )
      {
  %>
          <tr>
            <td class="l"><%=datiOrdinanza.getTenori()[x].getDescrOggettoTenore()%></td>
            <td class="l"><%=datiOrdinanza.getTenori()[x].getDescrEsitoTenore()%></td>
            <%// Inserimento del Destinatario nel caso della Rimessione Atti 
              if(  (datiOrdinanza.getOrdinanza() != null) &&
                (datiOrdinanza.getOrdinanza().getCodTipoOrdinanza() != null) &&
                (datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.RIMESSIONE_ATTI)) == 0) 
              { %>
                  <td class="l"><%=datiOrdinanza.getOrdinanza().getOggettoProcedimento()%></td>
            <% } %>
    </tr>
<%
        }
  } //chiude if(!modificaOrdinanza)     
%>
    <tr>
      <td  colspan=2> &nbsp;</td>
    </tr>
   </table>   

<!--      PRESCRIZIONI   ???? -->
<%
// Controllo su tipo Ordinanza per determinare se visualizzare le Prescrizioni
  if (datiOrdinanza.getOrdinanza().getCodTipoOrdinanza() != null && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_MA) != 0  && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.RICOVERO_OPG) != 0 && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.ESTINZIONE_PENA) != 0  && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LC) != 0 && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.EST_PENA_LIB_CONDIZIONALE) != 0   && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.CONC_RINVIO_EP) != 0 && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.PROROGA_DETENZIONE_SPECIALE) != 0 && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.PROROGA_DETENZIONE_DOMICILIARE) != 0
      && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.SOSPENSIONE_ESECUTIVA_ORDINANZA) != 0  && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_PERMESSO) != 0 && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_SCOMPUTO) != 0 && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.LIBERAZIONE_ANTICIPATA) != 0 && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_ORDINANZA) != 0  && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoDecreto.RICOVERO_OPG_OSS_PSICHE) != 0 && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoDecreto.DECLARATORIA_ESTINZIONE_SS) != 0 && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoDecreto.MODIFICA_PERMANENTE_SS) != 0 && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoDecreto.SOSPENSIONE_ESECUZIONE_SS) != 0
      && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_SANZIONE_SOSTITUTIVA) != 0
    && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_SANZIONI_SOSTITUTIVE ) != 0   
    && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_SANZIONI_SOSTITUTIVE  ) != 0
    && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.RIMESSIONE_ATTI  ) != 0
  )
   {
    // 12-03-2009 Caso Applicazione Sanzione Sostitutiva o Conversione Pene Pecuniarie (Tipo di Prescrizioni diverso)
    // 08-04-2011 Stessa gestione per Applicazione Misure Sicurezza
      if (datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.APPLICAZIONE_SANZIONI_SOSTITUTIVE ) == 0  || 
          datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE ) == 0  || 
          datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.MISURA_SICUREZZA ) == 0  )
      { 
  %>      
         <jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
         <jsp:param name="EveIdEvento" value="<%=IdEvento%>" />
         <jsp:param name="nextaction" value="siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza" />
        <jsp:param name="PrescrizioniSSePP" value="SSoPP" />
         </jsp:include>
      
  <%    }
      else
      { %>
         <jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
         <jsp:param name="EveIdEvento" value="<%=IdEvento%>" />
         <jsp:param name="nextaction" value="siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza" />
         </jsp:include>
   <%
     }
  }
%>

 <% if (!modificaOrdinanza) 
 { %>
  
 <form name="dettaglio">
<%
if (misuraAlternativa != null && misuraAlternativa.getFlFormaMisura() != null) {
   	String labelFormaMisura="&nbsp;";
   	if (misuraAlternativa.getFlFormaMisura() != null) {
   		labelFormaMisura="Permanenza in casa";
   		if (misuraAlternativa.getFlFormaMisura().compareTo(new BigDecimal(2)) == 0) {
   			labelFormaMisura="Collocamento in comunità";
   		}
   	}
   %>
    <table cellspacing=4 cellpadding=4 width="95%">
        <tr>
            <td class="l">La misura deve essere eseguita nelle forme della: </td> 
            <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(labelFormaMisura)%> </font></td>
          </tr>
          <%
          if (misuraAlternativa.getFlFormaMisura().compareTo(new BigDecimal (2))==0 && misuraAlternativa.getDescrizioneComunita() != null) {
          %>
           <tr>
            <td class="l">Comunità: </td> 
            <td class="l"><font class="campo"> <%=(misuraAlternativa.getDescrizioneComunita()==null?"":StringUtils.toStringJSP(misuraAlternativa.getDescrizioneComunita())) %> </font></td>
          </tr>
          <%
          }
          %>
      </table>
   <%
} 
  if(datiOrdinanza.getOrdinanza().getCodTipoOrdinanza() != null)
  {
     if (datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.LIBERAZIONE_ANTICIPATA) == 0)
     {
       if (datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp() != null && datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp().length() > 0)
       {
   %>
      <table cellspacing=4 cellpadding=4>
        <tr>
          <td class="l">Ufficio di Sorveglianza destinatario di </td>
          <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font></td>
        </tr>
      </table>
  <%   } %>
  
        <table cellspacing=4 cellpadding=4>
          <tr>
            <td class="l">Ulteriore descrizione della decisione </td>
            <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
          </tr>
        </table>  
  
         <jsp:include page="<%=ICostantiLibertaAnticipata.PG_DETTAGLIO_LIBANTICIPATA%>"/>
  <%
     }
       else  if (datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.LICENZA) == 0)
       {
  %>
       <jsp:include page="<%=ICostantiLicenzaLibanticipata.PG_LOAD_DETTAGLIOLICENZALIBANTICIPATA%>"/>
<%
      }
   
   
if (datiOrdinanza.getEvento().getEveIdEventoRevoca() != null)
{ %>
 <jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_SINTESI_ORDINANZA_REVOCA%>"/>
<% } %>

<!--   TEMPLATE  -->
<%     
     if(datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().trim().length() > 1 )
     {
     // Combo template di stampa solo sulle Emissioni di Ordinanza Nuove
     // E se non esiste il template predefinito.
     
   //    if(datiOrdinanza.getEvento().getTemIdTemplate() == null || datiOrdinanza.getEvento().getTemIdTemplate().trim().length() < 2 )
      
      if(datiOrdinanza.getEvento().getTemIdTemplate() == null )
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
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza">
            <input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.EMISSIONE_ORDINANZA%>">
          </td>
        </tr>
        </table>
        </FORM>
      </div>
      
<%  } // endif modificaOrdinanza 
    else
  { %>
      <jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_MODIFICA_ORDINANZA%>" />
  
<%  } %>

</body>
</html>