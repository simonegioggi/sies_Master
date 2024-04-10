<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.rateizzazionepp.model.RateizzazionePPModel"%>
<%@ page import="siap.siep.rateizzazionepp.action.ICostantiRateizzazionePP"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>


<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"  />
<jsp:useBean id="soggetto"  scope="session" class="siap.sico.soggetto.model.SoggettoModel"  />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="ordineIngiunzione"  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="listaRateizzazioni" scope="request" class="java.util.Vector" />

<jsp:useBean id="notaTrasmissione"  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="notificaAlCondannato" scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="magistrato"           scope="request" class="siap.sico.magistrato.model.MagistratoModel" />


<%
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

<html>
  <head>
    <title>[S.I.E.S.] - Gestione evento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
    <script language="JavaScript">
    function modificaNotaTrasmissione(){
      document.modificaNotaTrasmissione.submit();
    }
    
    function CancellaNotaTrasmissione(){
	    if (window.confirm("Confermi l'eliminazione della Nota di trasmissione?")) {
	      //document.cancellaSollecito.submit();
	    }
    }    
    </script>
  </head>
  
<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Comunicazione Nota Trasmissione Bollettini - Rate successive alla prima&nbsp;</font>
      </td>
  
      <%
      if (   notaTrasmissione.getEvento().getFlagDocumentoRegistrato() == null
          || "N".equals(notaTrasmissione.getEvento().getFlagDocumentoRegistrato())
         )
      {
      %>   
      <td class="LBG">
        <a href="Javascript:modificaNotaTrasmissione()">
          <img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Nota di Trasmissione" width="24" height="24" border="0">
        </a>
      </td>
      <!--  
      <td class="LBG">
        <a href="Javascript:CancellaNotaTrasmissione()">
          <img align="middle" src="/images/delete24.gif" alt="cancella" width="24" height="24" border="0"></a>      
      </td> 
      -->
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActStampaNotaTrasmissione&IdEvento="+notaTrasmissione.getEvento().getIdEvento()%>"/>
      </jsp:include>
      <% } %>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<%
if (   notaTrasmissione.getEvento().getFlagDocumentoRegistrato() == null
    || "N".equals(notaTrasmissione.getEvento().getFlagDocumentoRegistrato())
   )
{
%>  
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="modificaNotaTrasmissione">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActLoadModificaNotaTrasmissione">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=notaTrasmissione.getEvento().getIdEvento() %>">
</form>
<% } %>   

  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
        <font class="campo">
        <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")){%>
          DETENUTO PER ALTRA CAUSA - <%=lAltraCausa.getDescrTipoPosGiuridica()%>
        <%}else{%>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        <%}%>
        </font>
        <input type="HIDDEN" title="Codice Posizione" type="text" maxlength="6" size="6"
               value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" 
               name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"   >
      </td>
    </tr>
  </table>


  <table>
    <tr>
      <td class="L" colspan="5">
        <font class="campo">
              <%=ordineIngiunzione.getEvento().getDescrTipoProvvedimento()%>
              &nbsp;
              <%=ordineIngiunzione.getEvento().getDescrMotivo()%>
              &nbsp;emesso in data&nbsp;
              <%=DateUtils.getDateToString(ordineIngiunzione.getEvento().getDataEmissione(), "dd-MM-yyyy")%>
        </font>
      </td>
    </tr>
  </table>
  
  <br>

  <%
  RateizzazionePPModel primarata = (RateizzazionePPModel) listaRateizzazioni.elementAt(0);
  String lTipoRateizzazione = primarata.getTipoRateizzazione();
  BigDecimal lImportoDaPagare = primarata.getImportoDaPagare();
  %>
  <table>
    <tr>
      <td class="L">
        <font class="label">Importo da pagare</font>
        <font class="campo"><%=StringUtils.toEuroFormat(lImportoDaPagare)%> &euro;</font>
        <font class="label">con le seguenti modalita'</font>
      </td>
    </tr>
  </table>

  <table>
    <tr>
      <% if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)) { %>
      <td class="Titolo" colspan="6"> Pagamento in una Unica Soluzione </td>
      <% } else if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)) { %>
      <td class="Titolo" colspan="6"> Pagamento Rateizzato </td>
      <%}%>
    </tr>
    
    <%
    Iterator IteRate = listaRateizzazioni.iterator();
    int conta = 0;
    while (IteRate.hasNext()) {
      RateizzazionePPModel rata = (RateizzazionePPModel) IteRate.next();
      conta++;
    %>
    <tr>
      <td class="R" nowrap>
        <font class="campo"><%=StringUtils.toStringJSP(rata.getNumeroRate(),"&nbsp;")%></font>
      </td>
      <td class="R" nowrap>
        <font class="label"> rate da </font>
      </td>
      <td class="R" nowrap>
        <font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font>
      </td>      
      <% if (conta == 1) { %>
      <td class="L" nowrap><font class="label">termine di pagamento della prima rata fissato entro </font></td>
      <td class="R" nowrap><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
      <td class="L" nowrap><font class="label">giorni dalla notifica dell'avviso di pagamento </font></td>
      <% } else { %>
      <td class="R" colspan="3">&nbsp;</td>
      <% } %>
    </tr>
    <% } %>
  </table>
  
  <br>
  
  <table>
    <tr>
      <% if(notaTrasmissione.getEvento().getDataEmissione()!= null) {   %>
        <td class="l">Data Emissione</td>
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notaTrasmissione.getEvento().getDataEmissione(), "dd-MM-yyyy") )%></font>
        </td>
      <% } %>

      <%  // SE NON ESISTE NESSUNA NOTIFICA FALLISCE
      if(notaTrasmissione.getNotifiche()[0].getDataInvio()!= null)
      {
      %>
        <td class="l">Data Trasmissione</td>
        <td class="L">
          <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(notaTrasmissione.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>
        </td>
      <% } %>
    </tr>
  </table>
  
  <br> 
    
  <table>
    <% if(magistrato != null) { %>
    <tr>
     <td class="Titolo" colspan="3"> Magistrato </td>
    </tr>    
    <tr>
      <td class="l">Magistrato Assegnatario</td>
      <td class="L">
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
      </td>
    </tr>
    <% } %>  
    
    <tr>
      <td colspan="3"><br></td>
    </tr>
    
    <tr>
      <td class="Titolo" colspan="3"> Notifica al condannato </td>
    </tr>
    
    <% if (notificaAlCondannato.getAutoritaEsterna() != null) { %>
    <tr>
      <td class="l">Autorita' Destinazione</td>
      <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( notificaAlCondannato.getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP( notificaAlCondannato.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
      </td>
    </tr>
      <% if(notificaAlCondannato.getNote()!= null){%>
      <tr>
        <td class="l">Indirizzo</td>
        <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(notificaAlCondannato.getNote())%></font>&nbsp;</td>
      </tr>
      <% } %>
    <% } %>
    
    <% 
    if (notificaAlCondannato.getIstitutoDetenzione() != null) { 
      String lNotificaIstituto = notificaAlCondannato.getIstitutoDetenzione().getDescrTipoIstituto();
      if (notificaAlCondannato.getIstitutoDetenzione().getDescrComune() != null)
        lNotificaIstituto += " di " + notificaAlCondannato.getIstitutoDetenzione().getDescrComune();
      if (notificaAlCondannato.getIstitutoDetenzione().getIndirizzo() != null)
        lNotificaIstituto += " - " + notificaAlCondannato.getIstitutoDetenzione().getIndirizzo();
    %>
    <tr>
      <td class="L">Istituto Notifica</td>
      <td class="L" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(lNotificaIstituto)%></font>&nbsp;
      </td>
    </tr>
    <% } %>
  </table>
  
<br>
<div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
    <table>
      <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
        <td class="L">
          <input class=bottone  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActUploadNotaTrasmissione">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= notaTrasmissione.getEvento().getIdEvento() %>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sanzionesostitutiva.action.ActLoadDettaglioNotaTrasmissione">
        </td>
      </tr>
    </table>
  </form>
</div>

</body>
</html>  
