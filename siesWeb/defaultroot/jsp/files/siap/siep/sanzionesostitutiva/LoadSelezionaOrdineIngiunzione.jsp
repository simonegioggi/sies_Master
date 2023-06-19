<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%-- MEV_2023-33: aggiunta pagina --%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel"%>
<%@ page import="siap.siep.rateizzazionepp.model.RateizzazionePPModel"%>

<jsp:useBean id="listaOrdiniIngiunzione"  scope="request" class="java.util.Vector<EventoRateizzazionePPModel>"/>
<jsp:useBean id="azioneChiamante" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>

<html>
<head>
	<title>[S.I.E.S.] - Gestione Ordine Esecuzione Ingiunzione al Pagamento</title>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	<script language="JavaScript">	
		function eseguiAzione(id) {
		    document.SelezionaOrdineIngiunzione.<%=ICostantiEvento.CAMPO_ID_EVENTO%>.value = id;
		    document.SelezionaOrdineIngiunzione.<%=IWebConstants.ACTION_FIELD%>.value = "<%=azioneChiamante%>";
		    document.SelezionaOrdineIngiunzione.<%=IWebConstants.LINK_RITORNO%>.value = "<%=TornaQui%>";
		    document.SelezionaOrdineIngiunzione.submit();
		}
		
		function tornaIndietro(action) {
		  document.SelezionaOrdineIngiunzione.<%=IWebConstants.ACTION_FIELD%>.value = action;
		  document.SelezionaOrdineIngiunzione.submit();
		}
	</script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Gestione Ordine Esecuzione Ingiunzione al Pagamento</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia dei dati analitici -->
        <a href="javascript:tornaIndietro('siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzione')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="SelezionaOrdineIngiunzione">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=IWebConstants.LINK_RITORNO%>" value="">
  <input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="">
  
  <table cellspacing="0" cellpadding="0" width="95%">
    <tr>
      <td class="int">Provvedimento</td>
      <td class="int">Modalit&agrave; Pagamento</td>
      <td class="int">Seleziona</td>
    </tr>
<%
  Iterator<EventoRateizzazionePPModel> itx = listaOrdiniIngiunzione.iterator();
  while (itx.hasNext()) {
    EventoRateizzazionePPModel erppm = (EventoRateizzazionePPModel) itx.next();
    EventoModel em = erppm.getEvento();
    
    // Recupero le rateizzazioni
    Vector<RateizzazionePPModel> rateizzazioni = erppm.getListaRateizzazioniPP();
    Iterator<RateizzazionePPModel> iter = rateizzazioni.iterator();
    String modalitaPagamento = "";
    int cont = 0;
    
    while (iter.hasNext()) {
      RateizzazionePPModel rata = iter.next();
      if (cont == 0)
        modalitaPagamento = "Importo da Pagare: <font style=\"color:red\">" + StringUtils.toEuroFormat(rata.getImportoDaPagare()) + " &euro;</font>";
      
      if ("R".equals(rata.getTipoRateizzazione())) { // RATE
        if (cont == 0) {
          modalitaPagamento += "in:";
          modalitaPagamento += "<ul>";
        }
        modalitaPagamento += "<li>" + "" + rata.getNumeroRate() + " rate da " + ""
            + StringUtils.toEuroFormat(rata.getImportoRata());
        if (!Utils.isNullObj(rata.getScadenzaGiorni()) && cont == 0)
          modalitaPagamento += ", termine di pagamento fissato entro " + rata.getScadenzaGiorni().toString()
              + " giorni dalla Notifica dell'Avviso di Pagamento" + "</li>";
        else
          modalitaPagamento += "</li>";
        if (cont == rateizzazioni.size() - 1)
          modalitaPagamento += "</ul>";
      } else { // UNICA SOLUZIONE
        modalitaPagamento += " in un'unica soluzione";
        if (!Utils.isNullObj(rata.getScadenzaGiorni()))
          modalitaPagamento += ", termine di pagamento fissato entro <font class=\"campo\" style=\"text-transform:lowercase;\">" + rata.getScadenzaGiorni().toString()
              + " giorni</font> dalla Notifica dell'Avviso di Pagamento";
      }
      cont++;
    }
%>
  <tr>
    <td class="c" style="padding-left:5px;padding-right:5px;">
      <%=StringUtils.toStringJSP(em.getDescrTipoProvvedimento()) 
      + " " + StringUtils.toStringJSP(em.getDescrMotivo())%>
      &nbsp;del&nbsp;<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(em.getDataEmissione(), "dd/MM/yyyy"))%></font>
    </td>
    <td class="l" style="padding-left:5px;padding-right:5px;">
      <%=modalitaPagamento%>&nbsp;
    </td>
    <td class="c" style="padding-left:5px;padding-right:5px;">
      <a href="javascript:eseguiAzione(<%=em.getIdEvento()%>)">
        <img src="/images/dettagli.gif" alt="Seleziona Ordine Ingiunzione" width="12" height="12" border="0">
      </a>
    </td>
  </tr>
<%
  } // end while su iterator sugli eventi
%>
</table>
</FORM>
</body>
</html>