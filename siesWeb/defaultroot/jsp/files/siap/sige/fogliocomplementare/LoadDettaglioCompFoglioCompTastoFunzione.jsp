<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate" %>

<jsp:useBean id="documentoAllegato"     scope="request" class="siap.sige.documentoallegato.model.DocumentoAllegatoModel"/>
<jsp:useBean id="provvSige"             scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Foglio Complementare</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">  
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>

  <body class="corpo">
  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Foglio Complementare</font>
      </td>
      
     <td class="LBG">
     	<a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sige.avvocato.action.ActStampaFoglioComplementare&<%=ICostantiTemplate.CAMPO_ID_TEMPLATE%>=<%="SIGE_ST_006"%> ')">
        	<img id="generaStampa" align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
      	</a>
     </td>

  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
   </table>
<br />

   <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
      </tr>
   </table>

<br />
   
<table>
    <tr>
      <td class="Titolo" colspan="2">Estremi Provvedimento</td>
    </tr>
    
<% if (provvSige != null && provvSige.getProvvedimento() != null){ %>
    <tr>
      <td class="l" colspan="2">
		<%	if(provvSige.getProvvedimento().getCodTipoProvvedimentoSige() != null && provvSige.getProvvedimento().getCodTipoProvvedimentoSige().equals("02")){ %>			
					DECRETO n°
		<%	} else if (provvSige.getProvvedimento().getCodTipoProvvedimentoSige() != null && provvSige.getProvvedimento().getCodTipoProvvedimentoSige().equals("03")){%>      
		      		ORDINANZA n°
		<% } else { %>
					- 
		<% } %>      		
	      <font class="campo"><%=StringUtils.toStringJSP(provvSige.getProvvedimento().getChiaveAnno())%></font>
	      <font class="l">/</font>
	      <font class="campo"><%=StringUtils.toStringJSP(provvSige.getProvvedimento().getChiaveProgr())%></font>
	      <font class="l">del</font>
	      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSige.getProvvedimento().getDataEmissione(),"dd-MM-yyyy"))%></font>
   	  </td>
    </tr>
<% } %>

    <!-- Sono nel Dettaglio Foglio Complementare -->
    <tr>
      <td class="l">Data Compilazione/Trasmissione </td>
      <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>

    <%--  Ticket#20230202011 - si elimina dal dettaglio il riferimento alla motivazione non invio non più prevista --%>
	<% if (documentoAllegato.getDescrMotivazioneNonInvio()!=null && documentoAllegato.getDescrMotivazioneNonInvio().length()>0) { %>
    <tr>
      <td class="l">Motivazione non Inviato </td>
      <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(documentoAllegato.getDescrMotivazioneNonInvio())%></font>&nbsp;
      </td>
      <td class="l">Descrizione </td>
      <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(documentoAllegato.getDescrizioneNonInvio())%></font>&nbsp;
      </td>
    </tr>
	<% } %>
	<%-- Ticket#20230202011 - FINE --%>
	
    <tr>
      <td class="l">Data Inserimento Manuale </td>
      <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataInsMan(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
  </table>
  </body>
</html>