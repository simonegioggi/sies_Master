<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.lang.String" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.List" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario" %>
<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel" %>
<%@ page import="siap.siep.scadenzario.util.ScadenzarioUtils" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.siep.pagoPA.model.BollettinoPagopaModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>


<jsp:useBean id="tipo"   scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" scope="request" class="java.lang.String"/>

<jsp:useBean id="criteriRicerca" scope="request" class="siap.siep.scadenzario.model.ScadenzarioModel"/>
<jsp:useBean id="AnniScadenza"   scope="request" class="java.lang.String"/>
<jsp:useBean id="MesiScadenza"   scope="request" class="java.lang.String"/>
<jsp:useBean id="GiorniScadenza" scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Consultazione Scadenzario Fine Pena</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
    function stampa() {
      document.RicercaScadenzarioPP.submit();    
    }
    </script>    
  </head>

<BODY class="corpo">
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;
          <font class="campo">Consultazione Scadenzario Stato Pagamenti Pena Pecuniaria - <%=titolo%>&nbsp;</font>
        </td>
		    <td class="LBG">
		      <a class="cliccabile" href="javascript:stampa()"><img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0"></a>
		    </td>         
		    <td class="LBG">
		       <a href="javascript:history.go(-1)">
		         <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>      
		    </td>        
      </tr>
    </table>
    
    
 <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RicercaScadenzarioPP" >
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActRicercaScadenzarioStatoPagamentiPP">
  <input type="HIDDEN" name="tipoRicerca" value="stampaExcel">
  
  <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>"  value="<%=StringUtils.toStringJSP(criteriRicerca.getChiaveAnnoIniziale(),"")%>" />
  <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" value="<%=StringUtils.toStringJSP(criteriRicerca.getChiaveProgrIniziale(),"")%>" />
  
  <input type="hidden" name="tipo" value="<%=tipo%>" />
  
  <input type="hidden" name="<%=ICostantiScadenzario.CAMPO_ANNI_SCADENZA%>"   value="<%=StringUtils.toStringJSP(criteriRicerca.getNumAnni(),"")%>" />
  <input type="hidden" name="<%=ICostantiScadenzario.CAMPO_MESI_SCADENZA%>"   value="<%=StringUtils.toStringJSP(criteriRicerca.getNumMesi(),"")%>" />
  <input type="hidden" name="<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>" value="<%=StringUtils.toStringJSP(criteriRicerca.getNumGiorni(),"")%>" />
</form>


<table style="width: 500px;">
  <tr><td class="titolo" colspan="4">Criteri Selezionati</td></tr>
  <tr>
    <td class="L">Procedimento  (Anno/Numero)</td>
    <td class="c"><font class="campo"><%=StringUtils.toStringJSP(criteriRicerca.getChiaveAnnoIniziale(),"____")%>/<%=StringUtils.toStringJSP(criteriRicerca.getChiaveProgrIniziale(),"______")%></font></td>
  </tr>
	<tr>
	  <td class="l" width1="15%">Tutti</td>
	  <td class="c" width1="15%">
	  	<% if ("Tutti".equals(criteriRicerca.getTipoRic())) { %>
	  	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
	  	<% } else {%>&nbsp;<% } %>
	  </td>
	  <td class="l" width1="70%" colspan=2></td>
	</tr>
	<tr>
	  <td class="l">In scadenza</td>
	  <td class="c">
	  	<% if ("sette".equals(criteriRicerca.getTipoRic())) { %>
	  	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
	  	<% } else {%>&nbsp;<% } %>
	  </td>
	  <td class="L" width1="10%">entro:
	    Anni <font class="campo"><%=StringUtils.toStringJSP(criteriRicerca.getNumAnni(),"__")%></font>
	    Mesi <font class="campo"><%=StringUtils.toStringJSP(criteriRicerca.getNumMesi(),"__")%></font>
	    Giorni <font class="campo"><%=StringUtils.toStringJSP(criteriRicerca.getNumGiorni(),"__")%></font>
	  </td>
	</tr>
	<tr>
	  <td class="l">In scadenza Oggi</td>
 	  <td class="c">
	  	<% if ("oggi".equals(criteriRicerca.getTipoRic())) { %>
	  	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
	  	<% } else {%>&nbsp;<% } %>
		</td>
	</tr>
	<tr>
	  <td class="l">Scaduti</td>
 	  <td class="c">
	  	<% if ("scaduto".equals(criteriRicerca.getTipoRic())) { %>
	  	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
	  	<% } else {%>&nbsp;<% } %>
		</td>
	</tr>  
</table> 


 
<FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">    
    <br>
    <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
    <br>
 
<% if (tipo.equals("Tutti"))  { %>
      <table cellpadding=2 cellspacing=2>
        <tr>
          <td>&nbsp;<td>
          <td>&nbsp;<td>
          <td align="right"><img src="/images/QuadratinoVerde.gif"></td><td class="campo">In Scadenza (entro 7 giorni)</td>
          <td align="right"><img src="/images/QuadratinoRosso.gif"></td><td class="campo">In Scadenza Oggi</td>
          <td align="right"><img src="/images/QuadratinoGrigio.gif"></td><td class="campo">Scaduto</td>
        </tr>
      </table>
      <br>
<% } %>
    
    <table cellpadding=2 cellspacing=2>
      <tr>
        <td class="int" style="padding-left:5px;padding-right:5px;">N° SIEP</td>
        <td class="int" style="padding-left:5px;padding-right:5px;">Cognome</td>
        <td class="int" style="padding-left:5px;padding-right:5px;">Nome</td>
        <td class="int" style="padding-left:5px;padding-right:5px;">Luogo Nascita</td>
        <td class="int" style="padding-left:5px;padding-right:5px;">Data Nascita</td>
        <td class="int" style="padding-left:5px;padding-right:5px;">Data Notifica</td>
        <td class="int" style="padding-left:5px;padding-right:5px;">Data Scadenza<br>richiesta retizzazione</td>
        <td class="int" style="padding-left:5px;padding-right:5px;">Data Scadenza<br>primo pagamento</td>
        <td class="int" style="padding-left:5px;padding-right:5px;">N.ro<br>giorni</td>
        <td class="int" style="padding-left:5px;padding-right:5px;">Importo rate o unica<br>soluzione</td>
        <td class="int" style="padding-left:5px;padding-right:5px;">Rata</td>
        <td class="int" style="padding-left:5px;padding-right:5px;">Stato<br>pagamento</td>
        <td class="int" style="padding-left:5px;padding-right:5px;">Azioni</td>
     </tr>
<%
    List scadenzario =(List) request.getAttribute("scadenzario");
    Iterator itx = scadenzario.iterator();
    while ( itx.hasNext())
    {
      ScadenzarioModel lSca = (ScadenzarioModel)itx.next();
      FascicoloSiepModel lFas = lSca.getFascicoloModel();
      SoggettoModel lSog = lFas.getSoggetto();
      BollettinoPagopaModel lBollettino = lSca.getBollettinoModel();
      
      // Distingui i 4 casi:
      // - in scadenza oggi
      // - in scadenza nei prossimi 7 giorni
      // - scaduti
      // - in scadenza tra più di 7 gg      
      String tdClass = "";
      if(tipo.equals("Tutti")) {
        if(  lSca.getGiorniResidui() != null && lSca.getGiorniResidui().intValue()== 0)
          tdClass = "'crosso'";
        else if( lSca.getGiorniResidui() != null&& lSca.getGiorniResidui().intValue() <= 7 && lSca.getGiorniResidui().intValue() > 0)
          tdClass = "'cverde'";
        else if( lSca.getGiorniResidui() != null && lSca.getGiorniResidui().intValue() < 0)
          tdClass = "'cgrigio'";
        else
          tdClass = "'C'";
      }
      else {
        tdClass = "'C'";
      }
      
      String giorniStr = "&nbsp;";
      if (lBollettino.getDataScadenza()!=null) {
        if (DateUtils.isGreater(lBollettino.getDataScadenza(),DateUtils.getSysDate())) 
          giorniStr = ScadenzarioUtils.getDifferenza(lBollettino.getDataScadenza(), DateUtils.getSysDate());
        else
          giorniStr = ScadenzarioUtils.getDifferenza(DateUtils.getSysDate(), lBollettino.getDataScadenza());
      }
      
      String dataScadRateizzazione = "&nbsp;";
      if ("U".equals(lBollettino.getTipoRateizzazione())) {
        dataScadRateizzazione = DateUtils.getDateToString (DateUtils.moveDateTo(lSca.getDataInizioScadenza(),Calendar.DAY_OF_MONTH,20),"dd/MM/yyyy");
      }
%>
    <tr>
      <td class=<%=tdClass%>>
       <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
        <%=lFas.getChiaveAnno()%>
        /
        <%=lFas.getChiaveProgr()%>
       </a>
      </td>
      <td class=<%=tdClass%>><%=StringUtils.toStringJSP(lSog.getCognome())%>&nbsp;</td>
      <td class=<%=tdClass%>><%=StringUtils.toStringJSP(lSog.getNome())%>&nbsp;</td>
      <td class=<%=tdClass%>><%=StringUtils.toStringJSP(lSog.getDescrComuneNascita())%>&nbsp;</td>
      <td class=<%=tdClass%>><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSog.getDataNascita(),"dd/MM/yyyy")) %>&nbsp;</td>
      
      <td class=<%=tdClass%>><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>
      <td class=<%=tdClass%>><%=dataScadRateizzazione %></td>
      <td class=<%=tdClass%>><%=StringUtils.toStringJSP(DateUtils.getDateToString(lBollettino.getDataScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>

      <td class=<%=tdClass%> nowrap><%=giorniStr%>&nbsp;</td>
      
      <td class=<%=tdClass%>><%=StringUtils.toStringJSP(lBollettino.getImportoRata(),"&nbsp;")%></td>
      <td class=<%=tdClass%>><%=StringUtils.toStringJSP(lBollettino.getProgRata(),"&nbsp;")%>/<%=StringUtils.toStringJSP(lBollettino.getNumeroRate(),"&nbsp;")%></td>
      <% if ("PN".equals(lBollettino.getStatoPagamento())) { %>
      <td class=<%=tdClass%>>Non Pagato</td>
      <% } else { %>
      <td class=<%=tdClass%>>Pagato</td>
      <% } %>

	    <td class="c">
	      <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActVerificaStatoPagamenti&<%=ICostantiEvento.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>">
	        <img src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
	      </a>
	    </td>

    </tr>
<%
    }
%>
    </table>
    <br>
    <table>
      <tr>
        <td class="l">
        N.B. <font class="campo">N.ro giorni</font> indica il numero di giorni alla scadenza calcolato al momento dell'elaborazione
        </td>
      </tr>
    </table>
  </FORM>
</body>
</html>