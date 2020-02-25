<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<%@ page import="siap.siep.competenza.action.ICostantiCompetenza"%>

<%@ page import="siap.sico.utente.model.UtenteModel"%>



<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="competenza"          scope="request" class="siap.siep.competenza.model.CompetenzaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>

<jsp:useBean id="UfficioProcRichiesto"  scope="request" class="siap.sico.ufficio.model.UfficioModel" />
<jsp:useBean id="UfficioOrigine"        scope="request" class="siap.sico.ufficio.model.UfficioModel" />



<%
//==============================================================================
// Form per la visualizzazione del dettaglio evento di Richiesta Atti Per
// Cumulo
//==============================================================================
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
    

  UtenteModel lUtenteConnesso = (UtenteModel) session.getAttribute("UtenteConnesso");

    
    
    
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione Richiesta </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>


<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Richiesta Trasmissione Atti Per Competenza</font>
      </td>
      
    <% if(   !"S".equalsIgnoreCase (eventonotifica.getEvento().getFlagDocumentoRegistrato())
          && !"A".equalsIgnoreCase (eventonotifica.getEvento().getFlagDocumentoRegistrato())    
         )
       { 
     %>
      <!-- BOTTONE DI STAMPA -->
      <input type="hidden" name="tipo" value="D">
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActStampaRichiestaTrasmissioneTitolo&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
      </jsp:include>
    <%} %>
   
  <!-- TOOLBAR HEADER (per il tasto di TRASMISSIONE -->  
  <%
  if ( lUtenteConnesso.getUfficioUtente().getCodUfficio().equals(lFascicoloAssociato.getChiaveUfficio()))
  {

  //if(eventonotifica.getEvento().getDataTrasmissioneAtti().equals(eventonotifica.getEvento().getDataEmissione()))
  //{
    String lModificabile = "NO";
    if(eventonotifica.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("N"))
      lModificabile = "SI";
  %>
    <td class="LBG">  
      <jsp:include page="<%=ICostantiEvento.PG_TOOLBAR_HEADER%>">
        <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
        <jsp:param name="ValoreIdEntita" value="<%=eventonotifica.getEvento().getIdEvento()%>" />
        <jsp:param name="FlagDocumentoRegistrato" value="<%=eventonotifica.getEvento().getFlagDocumentoRegistrato()%>" />
        <jsp:param name="Modificabile" value="<%=lModificabile %>" />
      </jsp:include>
    </td>
  <%//} %>
  <% } %>
</tr>
</table>


<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<table>
  <tr>
    <%if(eventonotifica.getEvento().getDataEmissione()!= null){%>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>
    </td>
    <%}%>
    
    <%if(   eventonotifica.getNotifiche()!= null 
         && eventonotifica.getNotifiche().length >0 
         && eventonotifica.getNotifiche()[0] != null 
         && eventonotifica.getNotifiche()[0].getDataInvio()!= null)
    {%>
    <td class="l">Data Trasmissione</td>
    <td class="L">
      <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy") )%></font>
    </td>
    <%}%>
  </tr>
</table>
    
<table width=90%>
  <tr>
    <td class="Titolo" colspan="4">Titolo Da Assorbire in Cumulo</td>
  </tr>
  
  <tr>
    <td class="l">Tipo Provvedimento</td>
    <td class="l" colspan="1">
      <font class="campo"><%=StringUtils.toStringJSP(competenza.getDescrTipoProvvedimento())%></font>
    </td>
    <td class="L" align="right">Anno/Numero Provvedimento</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(competenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(competenza.getNumeroSentenza())%></font></td>
  </tr>
  <tr>
    <td class="l">Data Provvedimento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(competenza.getDataProvvedimento(), "dd-MM-yyyy"))%></font></td>
    <td class="l">Definitivo in Data</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(competenza.getDataIrrevocabilita(), "dd-MM-yyyy"))%></font></td>
  </tr>   
  <tr>
    <td class="l">Pronunciata da</td>
    <td class="l"  colspan="3"><font class="campo"><%=StringUtils.toStringJSP(competenza.getDescrTipoAutoritaEmittente())%></font></td>
  </tr>
  <tr>
    <td class="l">Luogo</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(competenza.getDescrLuogoEmittente())%></font></td>
    <td class="l">Sezione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(competenza.getNumSezioneAutoritaEmittente(),"&nbsp;")%></font></td>
  </tr>
  
  <tr>
    <td class="Titolo" colspan="4">Procedimento di esecuzione da assorbire</td>
  </tr>
  <tr>
    <td class="l">Ufficio Pubblico Ministero</td>
    <td class="l" colspan="3">
       <font class="campo"><%=StringUtils.toStringJSP(UfficioProcRichiesto.getDescrTipoUfficio())%></font>&nbsp;
     </td>
  </tr>
  <tr>
    <td class="l">Luogo</td>
    <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(UfficioProcRichiesto.getDescrComune())%></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Relativo al Procedimento Numero</td>
      <%
      String lStrFascicoloRichiesto = "<font class=\"campo\">"+StringUtils.toStringJSP(competenza.getChiaveAnno())+"/";
      
      if ("S".equals(competenza.getFlagAccorpato())) {
        lStrFascicoloRichiesto += competenza.getChiaveProgrOrigine()+"</font>";
        
        lStrFascicoloRichiesto+=" <font class=\"cRosso\">(ex "+UfficioOrigine.getDescrTipoUfficio()+" di "+UfficioOrigine.getDescrComune()+") </font>";
      }
      else {
        lStrFascicoloRichiesto+=StringUtils.toStringJSP(competenza.getChiaveProgr())+"</font>";
      }
      %>
    <td class="l"  colspan="3">
      <%=lStrFascicoloRichiesto%>
      <%--
      <font class="campo">
        <%=StringUtils.toStringJSP(competenza.getChiaveAnno())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(competenza.getChiaveProgr())%>
      </font>
      --%>
    </td>
  </tr>

    
  <tr>
    <td class="Titolo" colspan="4">Dati Atto</td>
  </tr>
  <tr>
    <td class="l">Tipologia Atto</td>
    <td class="l"  colspan="3"><font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrTipoProvvedimento())%></font></td>
  </tr>
  <tr>
    <td class="l">Oggetto Atto</td>
    <td class="l"  colspan="3"><font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%></font></td>
  </tr>     
    

  <%if(eventonotifica.getCampoNote()!= null && eventonotifica.getCampoNote().length >0 && eventonotifica.getCampoNote()[0] != null && eventonotifica.getCampoNote()[0].getDescr() != null){%>
  <tr>
      <td class="l">Contenuto </td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getCampoNote()[0].getDescr())%></font>
     </td>
  </tr>
  <% } %>



  <tr>
    <td class="l">Magistrato Firmatario
    <td class="L" colspan="3">
      <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
      <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
    </td>
  </tr>

  <% if (   eventonotifica.getNotifiche()!=null
         && eventonotifica.getNotifiche().length>0
         && eventonotifica.getNotifiche()[0].getAutoritaEsterna() !=null) {%>
    <tr>
      <td class="l">Altro Destinatario
      <td class="L" colspan="3">
        <font class="campo">
        <%=eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrTipoAutorita()%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Luogo
      <td class="L" colspan="3">
        <font class="campo">
        <%=eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrSede()%>&nbsp;
        </font>
      </td>
    </tr> 
  <%}%>
</table>


<br>
  <div align=left style="visibility:hidden" id="upld"><%-- onSubmit="return controllaUpload();" --%>
    <FORM name="comandi" enctype="multipart/form-data" method="post">
      <table>
        <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class="bottone"  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActUploadRichiestaTrasmissioneTitolo">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.modulocumulo.action.ActDettaglioRichiestaTrasmissioneTitolo">
          </td>
        </tr>
      </table>
    </FORM>
  </div>
  <br>
  <br>
</body>
</html>