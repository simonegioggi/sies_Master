<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.archiviazione.model.ArchiviazioneModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.sius.documentoallegato.model.DocumentoAllegatoModel"%>

<jsp:useBean id="eventonotifica"      scope="request"  class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request"  class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" />
<jsp:useBean id="magistrato"          scope="request"  class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="penaresidua"         scope="request"  class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="archiviazione"       scope="request"  class="siap.siep.archiviazione.model.ArchiviazioneModel" />

<jsp:useBean id="documentoAllegato"   scope="request"  class="siap.sius.documentoallegato.model.DocumentoAllegatoModel" />

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  ArchiviazioneModel lArcMod = archiviazione;

  if (lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if (lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if (lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
  
  // MEV 16: gestione casistiche per far comparire l'icona del foglio complementare
  List<String> lCodMotivi = Arrays.asList(ICostantiEvento.CODICI_AVVENUTA_ESECUZIONE_PENA);
  String lCodMotivo = eventonotifica.getEvento().getCodMotivo();
  boolean isPresentCodMotivo = false;
  isPresentCodMotivo = lCodMotivi.contains(lCodMotivo);
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione Archiviazione</title>
  <link rel="STYLESHEET" type="text/css"  href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Definizione Procedimento - Pena Espiata&nbsp;</font>
      </td>
      <%
        if (   eventonotifica.getEvento().getFlagDocumentoRegistrato() == null 
            || "N".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())) 
        {
      %>
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.rateizzazionepp.action.ActLoadModificaDefinizioneProcedimentoPP&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
          <img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Provvedimento" width="24" height="24" border="0">
        </a>
      </td>      
      <!-- BOTTONE DI STAMPA -->
      <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink"
          value="<%="/jsp/Main.jsp?Action=siap.siep.rateizzazionepp.action.ActStampaDefinizioneProcedimentoPP&IdEvento="
              + eventonotifica.getEvento().getIdEvento()%>" />
      </jsp:include>
      <% } %>

      <%
      // MEV 16: soltanto per sei casi particolari deve apparire l'icona del foglio complementare
      // FlagDocumentoRegistrato=S l'evento è stato validato  ==> il bottone "FC" deve essere visibile
      if (isPresentCodMotivo && "S".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())) 
      {
        if (   documentoAllegato.getIdDocumentoAllegato() == null 
            || (documentoAllegato.getIdDocumentoAllegato() != null && documentoAllegato.getDataAnnullamento() != null) 
           ) 
        {
        %>
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=InsertFC&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
            <img src="/images/fcNsc.gif" width="30" height="30" alt="Inserimento Foglio Complementare" border="0"></a>
        </td>
        <%} else { %>
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=ModificaFC&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&IdDocumentoAllegato=<%=documentoAllegato.getIdDocumentoAllegato()%>">
            <img src="/images/fcNsc.gif" width="30" height="30" alt="Modifica Foglio Complementare" border="0"></a>
        </td>
        <% } %>
      <% } %>
    </tr>
  </table>


  <br>
  <jsp:include  page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp" />
  <br>
  
  <table>
    <tr>
      <td class="l">Posizione Giuridica</td>
      <td class="L" colspan=3>
        <font class="campo"> 
        <% if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {%>
        DETENUTO PER ALTRA CAUSA 
        <% } else { %> 
        <%=lPosizione.getDescrPosizioneGiuridica()%> 
        <% } %>
        </font>
      </td>
    </tr>
    
    <%
      if ("S".equals(lFascicoloAssociato.getFlagAltraCausa())) 
      {
        if (lAltraCausa.getIstitutoDetenzione() != null) {
        %>
          <tr>
            <td class="l">Detenuto presso</td>
            <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
              di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
          </tr>
        <% } %>  
        
        <% if (lAltraCausa.getAltroLuogo() != null) { %>
        <tr>
          <td class="l">Altro Luogo</td>
          <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;</td>
        </tr>
        <% } %>
      <% 
      } else if (lLuogoDetenzione.getIstitutoDetenzione() != null) {
      %>
      <tr>
        <td class="l">Detenuto presso</td>
        <td class="L" colspan=5><font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
          di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
        </td>
      </tr>
      <% } %>


<%
      // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
      if (    lPosizione.getCodPosizioneGiuridica() != null
          && (   lPosizione.getCodPosizioneGiuridica().equals("02") 
              || lPosizione.getCodPosizioneGiuridica().equals("04"))) 
      {
        if (lLuogoDetenzione.getIstitutoDetenzione() != null) {
        %>
        <tr>
          <td class="l">Indirizzo</td>
          <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;</td>
        </tr>
        <%
        }
      }

      if (penaresidua.getIdPenaResidua() != null && !penaresidua.isErgastolo())
      {
      %>
        <% if (!penaresidua.isQuantumReclusioneZero() || !penaresidua.isMultaZero() ) { %>
        <tr>
          <td class="l">Reclusione</td>
          <td class="l">
            <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(), "0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(), "0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(), "0")%></font>
          </td>

          <td class="l">Multa</td>
          <td class="l">
            <font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;
            <font class="l">Euro</font>
          </td>
        </tr>
        <% } %>
        <% if (!penaresidua.isQuantumArrestoZero() || !penaresidua.isAmmendaZero() ) { %>
        <tr>
          <td class="l">Arresto</td>
          <td class="l">
            <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(), "0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(), "0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(), "0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l">
            <font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;
            <font class="l">Euro</font>
          </td>      
        </tr>
        <% } %>      
      <%
        } // end if not ergastolo
      %>
  </table>
  
  
  
    <%
// =======================================================================
//
// =======================================================================
    %>
  <table>
    <tr>
      <td class="l">Numero Protocollo Nota</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lArcMod.getNumNota(),"&nbsp;")%></font></td>
    </tr>
    
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lArcMod.getDataEmissione(), "dd-MM-yyyy"),"&nbsp;")%></font></td>
    </tr>

    <tr>
      <td class="l">Data Ricezione</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lArcMod.getDataRicezione(), "dd-MM-yyyy"),"&nbsp;")%></font></td>
    </tr>

    <tr>
      <td class="l">Autorità che ha inviato la nota</td>
      <% if (lArcMod.getCodTipoAutoritaEmittente()!=null && !lArcMod.getCodTipoAutoritaEmittente().equals("-")) { %>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lArcMod.getDescrTipoAutoritaEmittente(),"&nbsp;")%></font>
        di <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getDescrLuogoEmittente(),"&nbsp;")%></font>
      </td>
      <% } else { %>
      <td class="L">&nbsp;</td>      
      <% } %>
    </tr>

    <tr>
      <td class="l">Indirizzo</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lArcMod.getIndirizzoEmittente(),"&nbsp;")%>&nbsp;</font><td>
    </tr>

    <tr>
      <td class="l">Data Definizione</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lArcMod.getDataDefinizione(), "dd-MM-yyyy"))%></font></td>
    </tr>

    <tr>
      <td class="l">Oggetto Definizione</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lArcMod.getDescrOggettoDefinizione())%></font></td>
    </tr>

    <tr>
      <td class="l">Magistrato</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome())%></font>
        <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome())%></font>
      </td>
    </tr>
    
    <%
    if ( eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0) 
    {
      NotificaModel lNotMod = eventonotifica.getNotifiche()[0];
    %>
    <tr>
      <td class="l">Casellario Giudiziale</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font>&nbsp;</td>
    </tr>    
    <% } %>
  </table>
  <br>
  
  <div align=left style="visibility: hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post"  onSubmit="return controllaUpload();">
      <table>
        <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L"><input class=bottone type="submit" value="Conferma"> 
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.rateizzazionepp.action.ActUploadDefinizioneProcedimentoPP">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>"> 
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.rateizzazionepp.action.ActDettaglioDefinizioneProcedimentoPP">
          </td>
        </tr>
      </table>
    </form>
  </div>
  <br>
  <br>
</body>
</html>