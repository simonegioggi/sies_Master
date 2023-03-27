<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.siep.rateizzazionepp.model.RateizzazionePPModel"%>
<%@ page import="siap.siep.rateizzazionepp.action.ICostantiRateizzazionePP"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>

<jsp:useBean id="listaRateizzazioni"        scope="request"   class="java.util.Vector" />
<jsp:useBean id="dettaglioPenaComplessiva" 	scope="request" class="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel"/>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>


<html>
<head>
<title>[S.I.E.S.] - Rateizzazione Pena Pecuniaria</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">

function cancellaRate(){
  var lAzione = "siap.siep.rateizzazionepp.action.ActCancellaRateizzazione";

  <% if (listaRateizzazioni.size()>0 && ((RateizzazionePPModel)listaRateizzazioni.elementAt(0)).getEveIdEvento()!=null) { %>
  alert("Non e' possibile Cancellare le rate in quanto e' gia' stato emesso o e' in fase di inserimento un Ordine di Ingiunzione.");
  <% } else { %>
  var msgConfirm = "Si vuole procedere con la cancellazione di tutte le rate?"; 
  msgConfirm = msgConfirm+" Gli eventuali bollettini già emessi verranno cancellati."

  if (window.confirm(msgConfirm)) {
    document.RateizzazionePP.Action.value = lAzione;
    document.RateizzazionePP.submit();
  }
  <% } %>
}

function modificaRate(){
  <% if (listaRateizzazioni.size()>0 && ((RateizzazionePPModel)listaRateizzazioni.elementAt(0)).getEveIdEvento()!=null) { %>
  alert("Non e' possibile Modificare le rate in quanto e' gia' stato emesso o e' in fase di inserimento un Ordine di Ingiunzione.");
  <% } else { %>
  var lAzione = "siap.siep.rateizzazionepp.action.ActLoadModificaRateizzazione";
  document.RateizzazionePP.Action.value = lAzione;
  document.RateizzazionePP.submit();
  <% } %>
}
</script>
</head>

<body class="corpo" >
<table>
  <tr>
    <td class="LBG">
      <a href="Javascript:window.print();">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
      </a>
    </td>
    <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Dettaglio Modalita' pagamento Pena Pecuniaria</font>
    </td>
    <td class="LBG">
       <% if (!"S".equals(fascicolo.getFlagValidato())) { %>
       <a href="javascript:modificaRate()">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Rate" width="24" height="24" border="0"></a>
          
      <a href="javascript:cancellaRate()" >
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella Rate" width="24" height="24" border="0"></a>
      <% } %>
    </td>
    <td class="LBG">
      <a href="<%=IWebConstants.PG_MAIN %>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP %>=<%=fascicolo.getIdFascicoloSiep()%>">
              <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
    </td>
  </tr>
</table>

<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RateizzazionePP">
  <input type="HIDDEN" name="Action" value="">
</form>


<%
PenaComplessivaModel     lPenCom = (dettaglioPenaComplessiva!= null && dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva() != null) ? dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva() : null;
SanzioneSostitutivaModel lSanSos = (dettaglioPenaComplessiva!= null && dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva() != null) ? dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva() : null;
%>
  <table width="50%">
    <tr>
      <td class="L">
        <font class="label">Pena Pecuniaria: </font>&nbsp;
        <font class="label">MULTA</font> <font class="campo"><%=StringUtils.toEuroFormat( (lPenCom!=null ? lPenCom.getImportoMulta() : null) )%></font>&nbsp;<font class="label">&euro;</font>
        <font class="label">,&nbsp;AMMENDA</font> <font class="campo"><%=StringUtils.toEuroFormat( (lPenCom!=null ? lPenCom.getImportoAmmenda() : null) )%> </font>&nbsp;<font class="label">&euro;</font>  
      </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">Pena Pecuniaria Sostitutiva: </font>&nbsp;
        <font class="label">MULTA</font> <font class="campo"><%=StringUtils.toEuroFormat( (lSanSos!=null ? lSanSos.getSanzionePecuniariaMulta() : null) )%></font>&nbsp;<font class="label">&euro;</font>
        <font class="label">,&nbsp;AMMENDA</font> <font class="campo"><%=StringUtils.toEuroFormat( (lSanSos!=null ? lSanSos.getSanzionePecuniariaAmmenda() : null) )%> </font>&nbsp;<font class="label">&euro;</font>  
      </td>
    </tr>
  </table>
  
<br>

<%
RateizzazionePPModel primarata = (RateizzazionePPModel) listaRateizzazioni.elementAt(0);
String lTipoRateizzazione = primarata.getTipoRateizzazione();
BigDecimal lImportoDaPagare = primarata.getImportoDaPagare();
%>
  <table width="70%">
    <tr>
      <td class="L">
        <font class="label">Importo da pagare</font>
        <font class="campo"><%=StringUtils.toEuroFormat(lImportoDaPagare)%> &euro;</font>
      </td>
    </tr>
  </table>


  <table width="70%">
    <tr>
       <% if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)) { %>
       <td class="Titolo" colspan="6"> Pagamento in una Unica Soluzione </td>
       <% } else if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)) { %>
       <td class="Titolo" colspan="6"> Pagamento Rateizzato </td>
       <% } %>
       <td class="Titolo" colspan="1">Emessi bollettini</td>
       <td class="Titolo" colspan="1">Emesso ordine di ingiunzione</td>
    </tr>
    
    <!--
    <tr>
      <td class="R" colspan="4">&nbsp;</td>
    </tr>
    -->
    <%
    Iterator IteRate = listaRateizzazioni.iterator();
    int conta = 0;
    while(IteRate.hasNext()) {
      conta++;
      RateizzazionePPModel rata = (RateizzazionePPModel) IteRate.next();
      
      if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)) {
    %>
    <tr>
      <td class="L" nowrap><font class="label">Rata unica da</font>&nbsp;<font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font></td>      
      <td class="R" nowrap><font class="label">termine di pagamento fissato entro </font></td>
      <td class="R"><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
      <td class="L" colspan="3"><font class="label">giorni dalla notifica dell'avviso di pagamento</font></td>

        <%  if ( rata.getListaBollettini()!=null && rata.getListaBollettini().size()>0)  {%>   
          <td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%>V.gif"></td>
        <% } else { %>
          <td class="r">&nbsp;</td>
        <% } %>
        
        <%  if ( rata.getEveIdEvento()!=null)  {%>   
          <td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%>V.gif"></td>
        <% } else { %>
          <td class="r">&nbsp;</td>
        <% } %>       
            
    </tr>
    <% } else if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)) { %>
    <tr>
      <td class="R"><font class="campo"><%=StringUtils.toStringJSP(rata.getNumeroRate(),"&nbsp;")%></font></td>
      <td class="L" nowrap><font class="label"> rate da </font></td>
      <td class="R" nowrap><font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font></td>   
      <% if (conta==1) { %>
	      <td class="L"><font class="label">termine di pagamento della prima rata fissato entro </font></td>
	      <td class="R"><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
	      <td class="L"><font class="label">giorni dalla notifica dell'avviso di pagamento </font></td>
      <% } else { %>
          <td class="R" colspan="3">&nbsp;</td>
      <% } %>
        

        <%  if ( rata.getListaBollettini()!=null && rata.getListaBollettini().size()>0)  {%>   
          <td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%>V.gif"></td>
        <% } else { %>
          <td class="r">&nbsp;</td>
        <% } %>
        
        <%  if ( rata.getEveIdEvento()!=null)  {%>   
          <td class="C" nowrap><img src="<%=IWebConstants.IMAGES_DIR%>V.gif"></td>
        <% } else { %>
          <td class="r">&nbsp;</td>
        <% } %>
    </tr>    
    <% } %>
    
    <%
    }
    %>
  </table>

</body>
</html>

