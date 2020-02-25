<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.richiesta.action.ICostantiRichiesta" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<jsp:useBean id="soggettoSiep"   scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="sentenzaSiep"   scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="misuraalternativa"   scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel" />
<jsp:useBean id="sedeUfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel" />
<jsp:useBean id="magistratosorveglianza"   scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="eventonotifica"   scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="varieopzioni"   scope="request" class="java.lang.String" />
<%
FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

  //PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
 // LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
//String lcodicePosizione =lPosizione.getCodPosizioneGiuridica();
 // AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  /*if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();*/
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>


</head>
<body class="corpo">
<table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
 <%if(varieopzioni.equals("S")){%>
      <font class="campo">Dettaglio Estinzione Pena</font>
<%}else
{%>
      <font class="campo">Dettaglio Esito Espiazione Pena</font>
<%}%>
</td>

     <%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
 if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) {%>
 <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
     <%--td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActRichiestaStampaMS&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
            <img align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
        </td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActRichiestaStampaMS&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
 <%}%>

<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
 {%>
 <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
     <%--td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActRichiestaStampaMS&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
            <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
        </td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActRichiestaStampaMS&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
<%}%>
</tr>
</table>
<br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
   <table>
		<tr>
      <td class="l">Anno /Numero SIUS</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
      <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
     </td>
  </tr>
  <tr>
     <td class="l"> Anno / Numero Ordinanza </td>
     <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
     <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
     </td>
  </tr>
 <tr>
    <td class="l">Ufficio Emittente </td>
    <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrTipoUfficio())%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescProvincia())%></font></td>
    <input type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>" value="<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescProvincia())%>">
</tr>
 <tr>
   <td class="l">Oggetto Ordinanza </td>
   <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
 </tr>
 <tr>
  <td class="l">Data Emissione Ordinanza </td>
   <td class="l"><font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%>
    </font>
   </td>
  </tr>
<%if(varieopzioni.equals("S"))
{%>
<tr>
  <td class="l">Data Inizio Misura </td>
   <td class="l"><font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%>
    </font>&nbsp;
   </td>
  </tr>
<tr>
  <td class="l">Data Fine Misura </td>
   <td class="l"><font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataFineMisura(),"dd-MM-yyyy"))%>
    </font>&nbsp;
   </td>
  </tr>
<%}%>
<%for(int conta=0; conta<eventonotifica.getNotifiche().length; conta++)
{
if(eventonotifica.getNotifiche()[conta].getCodTipoNotifica().equals("N"))
{%>
<tr>
  <td  class="l">Note</td>
  <td class="l"><font class="campo"><%=eventonotifica.getNotifiche()[conta].getNote()%></font></td>
</tr>
<%}}
for(int conta2=0; conta2<eventonotifica.getNotifiche().length; conta2++)
{
if(eventonotifica.getNotifiche()[conta2].getCodTipoNotifica().equals("C"))
{%>
<tr><td class="Titolo" colspan="2">Magistrato di Sorveglianza</td></tr>
<tr>
  <td  class="l">Magistrato </td>
  <td class="l"><font class="campo"><%=magistratosorveglianza.getCognome()%>&nbsp;<%=magistratosorveglianza.getNome()%></font></td>
</tr>
<tr>
  <td  class="l">Note</td>
  <td class="L"><font class="campo"><%=eventonotifica.getNotifiche()[conta2].getNote()%></font></td>
</tr>
<%}
}%>
     <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
     <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.richiesta.action.ActDettaglioRichiestaMS">
</table>
</body>
</html>