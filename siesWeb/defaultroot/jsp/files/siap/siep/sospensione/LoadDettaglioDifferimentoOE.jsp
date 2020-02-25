<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.sospensione.model.SospensioneModel"%>
<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<%
//==============================================================================
// Finestra di visualizzazione del dettaglio del Provvedimento dell'Esecuzione
// (differimento) e generazione della stampa e validazione.
// Gestisce i seguenti casi
// - Differimento Provvisorio
// - Differimento Definitivo
// - Rigetto Differimento
// - Revoca Differimento
//
// La maschera è composta dalle seguenti sezioni:
// - Dettaglio posizione giuridica e pena residua
// - Dettaglio Provvedimento della Sorveglianza
// - Dettaglio della Pena Espiata e della Nuova Pena Residua
// - Magistrato competente
// - Destinatari
// - Destinatari per la Notifica
// - Restituzione ordine di esecuzione
// n.b. non tutte le sezioni sono sempre presenti
//==============================================================================
%>
<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="misuraalternativa"   scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="posizioneluogoaltra"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="sospensione"          scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>
<jsp:useBean id="uffTDS"               scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="Istituto"             scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="autorita"             scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="CSSA"                 scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="tipoProvvedimento"    scope="request" class="java.lang.String"/>
<jsp:useBean id="UfficioEmittente"     scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="AzioneChiamante"      scope="request" class="java.lang.String" />
<jsp:useBean id="flagergastolo"       scope="request" class="java.lang.String" />
<jsp:useBean id="codiceAutoritaE"     scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

<%
  String dataeditabile = "N";

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
%>

<%
//==============================================================================
// Sezione per decidere se vanno visualizzate le sezioni con i destinatari
//==============================================================================
boolean flagNotifiche = true;
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Sospensione dell'esecuzione della pena</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  </head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class=lbg>
         <font  class="label">Funzione :&nbsp;</font>
         <% if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV) ) { %>
         <font class="campo">Differimento / Rinvio dell'esecuzione Provvisorio</font>
         <% } else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF) ) { %>
         <font class="campo">Differimento / Rinvio dell'esecuzione Definitiva</font>
         <% } else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO) ) { %>
         <font class="campo">Rigetto Differimento / Rinvio dell'esecuzione</font>
         <% } else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_REVOCA) ) { %>
         <font class="campo">Revoca Differimento / Rinvio dell'esecuzione</font>
         <% }  %>
      </td>
<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null ||
     (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null &&
      eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)) {%>
      <% // BOTTONE DI STAMPA %>
      <input type="hidden" name="tipo" value="D">
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaDifferimento&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
      </jsp:include>
 <%}%>
    </tr>
  </table>

  <br>
     <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<form method="POST"  action="<%=IWebConstants.PG_MAIN%>" name="DettaglioDifferimentoOE">
<%
   BigDecimal lIdOrdinanzaSius = null;
   if(misuraalternativa != null && misuraalternativa.getEveIdEvento() != null) {
     lIdOrdinanzaSius = misuraalternativa.getEveIdEvento();
   }
%>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciDifferimentoOE">
    <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(lIdOrdinanzaSius)%>">
    <input type="HIDDEN" name="<%=ICostantiSospensione.TIPO_DIFFERIMENTO%>"  value="<%=tipoProvvedimento%>">
    <input type="HIDDEN" name="AzioneChiamante"    value="<%=AzioneChiamante%>">

    <input type="HIDDEN" name="idEventoGenerato"     value="<%=misuraalternativa.getEveIdEvento() %> ">
    <input type="HIDDEN" name="CodTipoProvvedimento" value="<%=misuraalternativa.getCodTipoDecisione()%>">
    <input type="HIDDEN" name="CodMotivo"            value="<%=misuraalternativa.getCodTipoMisura()%>">

<%
//==============================================================================
// Sezione relativa alla Posizione Giuridica e Pena
// - Posizione giuridica
// - Luogo di detenzione
//   -- istituto di detenzione (se detenuto per questo o altra causa)
//   -- altro luogo
//   -- Indirizzo (se arresti domiciliari)
// - Pena Residua (se presente)
//   -- Reclusione + Arresti
//   -- Data Inizio, Tipo Ergastolo (se ergastolo)
//   -- Data fine (editabile se non validata(?))
//==============================================================================
%>

  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
        <font class="campo">
          <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
              DETENUTO PER ALTRA CAUSA
          <% } else { %>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
          <% } %>
        </font>
      </td>
    </tr>

    <%
      // Se Detenuto altra causa: Istituto di detenzione o Altro Luogo
      if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
        if( lAltraCausa.getIstitutoDetenzione()!= null ) { %>
          <tr>
            <td class="l">Detenuto presso </td>
            <td class="L" colspan=5>
              <font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            <% if(lAltraCausa.getIstitutoDetenzione().getDescrComune()!=null) { %>
               di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
            <% } %>
            </td>
          </tr>

          <% if (lAltraCausa != null && lAltraCausa.getAltroLuogo()!=null) { %>
          <tr>
            <td class="l">Altro Luogo </td>
            <td class="L" colspan=5>
              <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
            </td>
          </tr>
          <% }
        } // fine istituto di detenzione
      }
      else if(lLuogoDetenzione.getIstitutoDetenzione() != null ) { %>
        <tr>
          <td class="l">Detenuto presso </td>
          <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            <% if(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()!=null) { %>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            <% } %>
          </td>
        </tr>
      <%}%>

      <%
        // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(   lPosizione.getCodPosizioneGiuridica() != null
           && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
          if(lLuogoDetenzione.getIstitutoDetenzione() != null) { %>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
            </tr>
          <% }
        }
      %>

      <%
        //======================================================================
        // Aggiungo i dati della pena Espiata se presenti (reclusione/Arresto)
        //======================================================================
    	  if (  sospensione.getNumAnniPenaEspiata().intValue()!=0
    	  	 || sospensione.getNumMesiPenaEspiata().intValue()!=0
    	  	 || sospensione.getNumGiorniPenaEspiata().intValue()!=0 )
    	  {
      %>
      <tr>
        <td class="l">
        	<font class="label">Pena Espiata</font>
        </td>
    		<td class="l">
    			<font class="label">Anni</font>
    			<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaEspiata(), "0")%></font>
    			<font class="label">Mesi</font>
    			<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaEspiata(), "0")%></font>
    			<font class="label">Giorni</font>
    			<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaEspiata(), "0")%></font>

          <%  // Agiungo la Multa se presente
          if(sospensione.getMultaEspiata()!=null && sospensione.getMultaEspiata().compareTo(new BigDecimal(0))!=0) {
          %>
          <font class="label">Multa </font>
          <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getMultaEspiata())%></font>&nbsp;€&nbsp;
          <% }

          //
          if(sospensione.getAmmendaEspiata()!=null && sospensione.getAmmendaEspiata().compareTo(new BigDecimal(0))!=0) {
          %>
          <font class="label">Ammenda </font>
          <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getAmmendaEspiata())%></font>&nbsp;€&nbsp;
          <%}%>
    		</td>
      </tr>
      <% } %>

      <%
      //==========================================================================
      // Aggiungo i dati della pena residua se presenti (reclusione/Arresto)
      // (non ergastolo)
      //==========================================================================
      if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
      {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
           )
        {}
        else
        { %>
        <tr>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <%if(penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
          <% } %>
        </tr>
        <%}%>

        <%
        //===============
        // Arresti
        //===============
        if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
        {}
        else
        { %>
        <tr>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <%if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
          <% } %>
        <tr>
      <% }
      }  // fine IF sulla pena residua
      %>


      <%
      //==========================================================================
      // Inserisco il rigo con Data Inizio e Tipo Ergastolo (se presente)
      //==========================================================================
      %>
      <tr>
      <% if (penaresidua.getDataInizio() != null) { %>
           <td class="l">Data Decorrenza Pena</td>
           <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <% } %>

      <% if (penaresidua.getFlagErgastolo() != null) {
           if(penaresidua.getFlagErgastolo().equals("S")) { %>
             <td class="l">Pena Detentiva</td>
             <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
           <% }
           else if(penaresidua.getFlagErgastolo().equals("D")) { %>
             <td class="l">Pena Detentiva</td>
             <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
           <% }
         }
      %>

      <%
      //========================================================================
      // Inserisco la data Fine pena
      // - se non libero o comunque detenuto per altra causa
      // - se non in ergastolo
      // - se data editabile (se la pena residua recuperata è non validata)
      //   inserisco i campi altrimenti solo label
      //========================================================================
      if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) ) {
        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
          if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
          %>
           <td class="l">Data Fine Pena</td>
           <td class="L" colspan=2>
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
             -
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
             -
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
           </td>
          <% }
          else if( penaresidua.getDataFine() != null) {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
            %>
               <td class="l">Data Fine Pena</td>
               <td class="L" colspan=2>
                 <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               </td>
            <%
            }else{%>
               <td class="l">Data Fine Pena</td>
               <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               </td><%
            }
          }
        }
      } %>
    </tr>

    <tr height="3"><td></td></tr>
  </table>

<%
//==============================================================================
//           DATI DEL PROVVEDIMENTO DI DIFFERIMENTO DELL'ESECUZIONE
//==============================================================================
%>
  <table style="width: 95%; border: 0;">
    <tr>
    <% if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV) ) { %>
      <td colspan="4" class="titolo">Riepilogo Dati del Differimento/Rinvio</td>
    <% } else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF) ) { %>
      <td colspan="4" class="titolo">Riepilogo Dati del Differimento/Rinvio</td>
    <% } else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO) ) { %>
      <td colspan="4" class="titolo">Riepilogo Dati del Differimento/Rinvio</td>
    <% } else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_REVOCA) ) { %>
      <td colspan="4" class="titolo">Riepilogo Dati del Differimento/Rinvio</td>
    <% }  %>
    </tr>

    <tr>
      <td class="l" width=15%>Anno / Numero Sius</td>
      <td class="l" width=20%>
        <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
        <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
      </td>
      <td class="l" width='25%'> Anno / Numero Provvedimento </td>
        <td class="l" width=20%>
         <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
         <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
      </td>
    </tr>

    <tr>
      <td class="l">Tipo provvedimento </td>
      <td class="l" colspan="3">
        <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoDecisione())%></font>
      </td>
    </tr>

	<tr>
		<td class="l">Autorità Emittente </td>
      	<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
	    <%
	    	String descrTipoUfficio = StringUtils.toStringJSP(UfficioEmittente.getDescrTipoUfficio());
	    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
	    			"UDSM".equals(UfficioEmittente.getCodTipoUfficio())) {
	    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
	    	}
	    %>
      	<td class="l" colspan="3"> <font class="campo"><%=descrTipoUfficio%> di <%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%></font></td>
	</tr>

    <tr>
      <td class="l">Oggetto Decisione</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;
      </td>
    </tr>

    <% if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO) ) { %>
    <tr>
      <td class="l">Tipologia</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrNaturaDecisione())%></font>&nbsp;
      </td>
    </tr>
    <% } %>

    <tr>
      <td class="l">Data Emissione Provvedimento </td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>

    <tr>
      <td class="l">Annotazioni</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNote())%></font>&nbsp;
    </tr>

    <tr>
      <td class="l">Data Differimento</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%></font>&nbsp;
        <%
          if( misuraalternativa.getCodTipoUfficioScarcerazione()!=null
             && misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV")
             )
          {
            out.print("         [Già scarcerato]");
          }
          else if(misuraalternativa.getCodTipoUfficioScarcerazione()!=null
                && misuraalternativa.getCodTipoUfficioScarcerazione().equals("PROC"))
          {
            out.print("         [Da scarcerare]");
          }
        %>
      </td>
    </tr>
    <tr height="3"><td></td></tr>
  </table>

<%
//==============================================================================
// Sezione con i dati del Magistrato
//==============================================================================
%>
<table style="width: 95%;">
  <tr>
    <td class="Titolo" colspan=2> Provvedimento </td>
  </tr>

  <tr>
    <td class="l">Data emissione</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
    </td>
  </tr>

  <tr>
    <td class="l">Data trasmissione</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy"))%></font>
    </td>
  </tr>

  <tr>
   <td class="l">Magistrato Competente</td>
   <td class="L">
     <font class="campo"><%=StringUtils.toStringJSP(magistratocompetente.getCognome() )%> &nbsp; <%=StringUtils.toStringJSP(magistratocompetente.getNome() )%></font>
   </td>
  </tr>
  <tr height="3"><td></td></tr>
</table>


<%
//==============================================================================
// Sezione con i dati dei Destinatari
//==============================================================================
%>
<table style="width: 95%;">
  <tr>
    <td colspan ="3" class="titolo">Destinatari del provvedimento</td>
  </tr>

<%
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("uffTDS = "+uffTDS);
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("Istituto = "+Istituto);
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("CSSA = "+CSSA);
%>

<%-- MEV10-s3: nuova gestione, invece che stringa fissa inserisco valore dalla combo --%>
<% if (uffTDS.getDescrComune() != null && !uffTDS.getDescrComune().equals("")) { %>
	<tr>
		<td class="l">Destinatario</td >
		<td class="L">
			<font class="campo"><%=uffTDS.getDescrTipoUfficio()%></font> di <font class="campo"><%=StringUtils.toStringJSP(uffTDS.getDescrComune())%></font>
     	</td>
	</tr>
  	<tr height="3"><td></td></tr>
<% } %>

  <%if(Istituto.getDescrTipoIstituto()!=null && !Istituto.getDescrTipoIstituto().equals("")){%>
  <tr>
    <td class="l" >Istituto di Detenzione </td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(Istituto.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(Istituto.getDescrComune())%></font>
    </td>
  </tr>
  <tr height="3"><td></td></tr>
  <%}%>

  <%if(autorita.getDescrTipoAutorita()!=null && !autorita.getDescrTipoAutorita().equals("")){%>
  <tr>
    <td class="l" >Destinatario per esecuzione </td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(autorita.getDescrTipoAutorita())%> di <%=StringUtils.toStringJSP(autorita.getDescrSede())%></font>
    </td>
  </tr>
  <tr height="3"><td></td></tr>
  <%}%>

<%-- MEV10-s3: nuova gestione, invece che stringa fissa inserisco valore dalla combo --%>
<% if (CSSA.getIdCSSA() != null) { %>
	<tr>
    	<td class="l" ><%=StringUtils.toStringJSP(CSSA.getTipoDesc())%></td>
    	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
    	<td class="l">
      		<font class="campo"><%=StringUtils.toStringJSP(CSSA.getTipo())%> di <%=StringUtils.toStringJSP(CSSA.getComune())%>&nbsp;<%=StringUtils.toStringJSP(CSSA.getIndirizzo())%></font>
    	</td>
  	</tr>
  	<tr height="3"><td></td></tr>
<% } %>

<%
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("Notifiche = "+eventonotifica.getNotifiche().length);
  //============================================================================
  // Aggiungo la sezione degli Avvocati
  //============================================================================
  int count=0;
  while(count < eventonotifica.getNotifiche().length){
    NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//    siesLogger.debug("eventonotifica = "+lNotMod);

// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//    siesLogger.debug("getAvvSiep() = "+lNotMod.getAvvSiep());
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//    siesLogger.debug("getAutoritaEsterna() = "+lNotMod.getAutoritaEsterna());

    if (   lNotMod.getCodTipoNotifica().equals("N")
        && lNotMod.getAutoritaEsterna()!=null && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null) {
      AvvocatoSiepModel    lAvvMod = eventonotifica.getNotifiche()[count].getAvvSiep();
      AutoritaEsternaModel  lAuMod = eventonotifica.getNotifiche()[count].getAutoritaEsterna();
    %>
    <tr>
      <td class="l">Avvocato per  Notifica</td>
      <td class="L" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome())+" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;
        &nbsp;Foro di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%>
        </font>
        &nbsp;Difensore di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%>
        </font>
      </td>
    </tr>
    <tr>
	    <td class="l">Autorità Notifica</td>
      <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
         di
        <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
    </tr>
    <tr height="3"><td></td></tr>
    <%
    }

    count++;
  }  // end while
%>

  <tr height="3"><td></td></tr>

<%
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("Aggiungo la sezione dei destinatari per la Restituzione Ordine Esecuzione");
  //============================================================================
  // Aggiungo la sezione dei destinatari per la Restituzione Ordine Esecuzione
  //============================================================================
  int count2=0;
  while(count2 < eventonotifica.getNotifiche().length){
    NotificaModel lNotMod = eventonotifica.getNotifiche()[count2];

    if ( lNotMod.getCodTipoNotifica().equals("R") ) {
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("notifica = "+lNotMod);
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("getAutoritaEsterna() = "+lNotMod.getAutoritaEsterna());

    %>
    <tr>
      <td class="Titolo" colspan="4">Destinatari per la Restituzione Ordine di Esecuzione</td>
    </tr>
    <%
      if (lNotMod.getIstDetIdIstitutoDetenzione()!= null ) {
        // Istituto di detenzione
        IstitutoDetenzioneModel istitutoModel = lNotMod.getIstitutoDetenzione();
    %>
    <tr>
      <td class="l" >Istituto di Detenzione </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(istitutoModel.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(istitutoModel.getDescrComune())%></font>
      </td>
    </tr>
    <%
      }
      else if (lNotMod.getAutoritaEsterna() != null) {
        // Autorità competente
        AutoritaEsternaModel lModAut = lNotMod.getAutoritaEsterna();
    %>
    <tr>
      <td class="l" >Destinatario per esecuzione </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lModAut.getDescrTipoAutorita())%> di <%=StringUtils.toStringJSP(lModAut.getDescrSede())%></font>
      </td>
    </tr>
    <%
      }
    %>
    <tr height="3"><td></td></tr>
    <%
    }

    count2++;
  }  // end while
%>

</table>

</form>

<%
//==============================================================================
// Sezione per la Validazione
//==============================================================================
%>
<div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
    <table width="90%">
      <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
        <td class="L">
          <input  class=bottone  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"             value="siap.siep.sospensione.action.ActUploadDifferimentoNew">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"        value="<%= eventonotifica.getEvento().getIdEvento() %>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sospensione.action.ActLoadDettaglioDifferimentoOE">
        </td>
      </tr>
    </table>
  </FORM>
</div>

<script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("DettaglioDifferimentoOE");

    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>