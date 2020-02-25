<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" %>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>

<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="posizioneGiuridica"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="annotazioneManuale"  scope="request" class="java.util.Vector"/>

<%

  AnnotazioneManualeModel lAnnManApp = new AnnotazioneManualeModel();
  if( !annotazioneManuale.isEmpty() )
  {
    lAnnManApp = (AnnotazioneManualeModel)annotazioneManuale.firstElement();
  }
%>
<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Richiesta Nuovo Residuo Pena</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>

<body class="corpo">

<table>
  <tr>
    <td class="LBG">
      <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
    </td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Dettaglio Richiesta Nuovo Residuo Pena</font>
    </td>

    <%
    //=========================================================================  
    // Visualizzazione del BOTTONE DI STAMPA se evento non ancora validato
    //=========================================================================  
    if (   eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
        || (   eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null
            && eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0
           ) 
       )
    {
    %>
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaRichDetPenAboReato&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
      </jsp:include>
    <%
    } 
    %>

    <td class="LBG">
      <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniRichieste&<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>=<%=StringUtils.toStringJSP(eventonotifica.getEvento().getAnnIdAnnotazioneManuale())%>" >
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>
    </td>
  </tr>
</table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<table width="100%">
  <tr>
    <td class="l">Posizione Giuridica</td>
    <td class="L" >
      <font class="campo"><%=posizioneGiuridica.getDescrPosizioneGiuridica()%></font>
    </td>
  </tr>
</table>
<table width="100%">
  <tr><td>&nbsp;</td></tr>
<%
  for(int i = 0;i<annotazioneManuale.size();i++)
  {
    AnnotazioneManualeModel lAnnPrima = (AnnotazioneManualeModel)annotazioneManuale.get(i);

/*
      if(evento.getCodMotivo().equals("0211"))
      {
*/
      if( "013".equals( lAnnManApp.getCodTipoAnnotazione() ) )
      {
%>
        <tr>
        <td class="l" width="20%">Sentenza Corte Costituzionale</td>
        <td class="l" >
          Anno/Numero
          <font class="campo">
     <%if(lAnnPrima.getAnnoCc() == null || lAnnPrima.getAnnoCc().compareTo(new BigDecimal(0))==0){%>
            -
     <%}else{%>
            <%=StringUtils.toStringJSP(lAnnPrima.getAnnoCc() )%>
     <%}%>
       / <%=StringUtils.toStringJSP(lAnnPrima.getNumeroCc(), "-")%>
          </font>
        &nbsp; in data
          &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnPrima.getDataCC(), "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
<%}%>
<%
/*
    if(evento.getCodMotivo().equals("0210"))
    {
*/
    if( "004".equals( lAnnManApp.getCodTipoAnnotazione() ) 
   	// MEV 37 - Inizio	 
   	|| "017".equals( lAnnManApp.getCodTipoAnnotazione() ) )    		 
   	// MEV 37 - Fine
    {
%>
      <tr>
        <td class="l" width="20%">Fonte</td>
      <td class="l" >
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnPrima.getDescrFonte())%></font>
         Anno&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnPrima.getAnnoFonte())%></font>
        Num.&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnPrima.getNumeroFonte())%></font>
        Art.&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnPrima.getArticolo())%></font>
        </td>
      <td class="l" >Art.Qualificante
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnPrima.getDescrSottonumerazione())%></font>
         Comma&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnPrima.getComma())%></font>
        Let.&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnPrima.getLettera())%></font>
        Num.&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnPrima.getNumero())%></font>
        </td>
      </tr>
<%}
}%>
<tr><td>&nbsp;</td></tr>
</table>
  <table>
  <tr>
   <td class="l">Magistrato Assegnatario
   <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   </td>
  </tr>
<tr>
        <td class="l">Data Emissione</td>
<%if(eventonotifica.getEvento()!= null && eventonotifica.getEvento().getDataEmissione()!= null){%>
 <td class="L" >
       <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
             </td>
<%}%>
</tr>
<tr>
  <td class="l">Data Trasmissione</td>
  <%if(eventonotifica.getEvento()!= null && eventonotifica.getEvento().getDataTrasmissioneAtti()!= null){%>
  <td class="L">
    <font class="campo">    <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy"))%>&nbsp;</font>
  </td>
  <%}%>
</tr>
<tr>
  <td class="l">Giudice dell'esecuzione</td>
  <td class="l">
    <font class="campo">
    <%if(eventonotifica.getNotifiche()!= null && eventonotifica.getNotifiche().length >0) {%>
    <%=eventonotifica.getNotifiche()[0].getUfficio().getDescrTipoUfficio()%>
    <%} %>
    </font>
  </td>
</tr>
<tr>
  <td class="l">Sede</td>
  <td class="l">
    <font class="campo">
    <%if(eventonotifica.getNotifiche()!= null && eventonotifica.getNotifiche().length >0)
    {%>
    <%=eventonotifica.getNotifiche()[0].getUfficio().getDescrComune()%>
    <%} %>
    </font>
  </td>
</tr>

<%if(eventonotifica.getNotifiche()!= null && eventonotifica.getNotifiche().length >0 && eventonotifica.getNotifiche()[0].getNote() != null){%>
<tr>
  <td class="l">Note</td>
  <td class="l">
    <font class="campo">
    <%=eventonotifica.getNotifiche()[0].getNote()%>
    </font>
  </td>
</tr>
<%}%>
</table>
 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActUploadRichDetPenAboReato">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.richiesta.action.ActDettaglioRichDetPenAboReato">
          </td>
        </tr>
      </table>
</form>
</div>
  <br>
  <br>
</body>
</html>