<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sius.udienza.model.UdienzaModel"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>

<jsp:useBean id="udienza" scope="request" class="siap.sius.udienza.model.UdienzaModel"/>
<jsp:useBean id="checkInsFissUdienza"  scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunto riferimento al codice tipo ufficio --%>
<jsp:useBean id="codTipoUfficio" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Udienza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/conferma.js"></script>
</head>

  <body class="corpo">
  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Udienza UDS</font>
        </td>

        <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>" />
            <jsp:param name="ValoreIdEntita" value="<%=udienza.getIdUdienza()%>" />
          </jsp:include>
	        
<%
		  // Modifica del 04/10/2013 mev "Revisione Misure di Sicurezza SIUS"
		  // Se si proviene dalla pagina di "Inserimento Fissazione Udienza"
		  // dopo aver inserito l'udienza il bottone consentirà di tornare alla
		  // pagina chiamante con i campi dell'udienza preimpostati. 
		  if(checkInsFissUdienza != null && checkInsFissUdienza.equals("S") ){
%>
	      	  <!-- BOTTONE DI RITORNO ALLA PAGINA DI "INSERIMENTO FISSAZIONE UDIENZA" -->
	          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienza.action.ActLoadInserisciFissazioneUdienza&Provenienza=DettaglioUdienza&IdUdienza=<%=udienza.getIdUdienza()%>" >
	            <img  align="middle" src="/images/arrowleft24.gif" alt="Ritorna alla maschera di Inserimento Fissazione Utenza" width="24" height="24" border="0">
	          </a>
<%
		  }
%>
        </td>
      </tr>
    </table>
  </FORM>

  <table cellspacing=2 cellpadding=2>

  <tr>
    <td class="l">Data Udienza</td>
    <td class="l"><font class="campo"><%=DateUtils.getDateToString(udienza.getDataUdienza(),"dd-MM-yyyy")%> </font></td>
  </tr>
  <tr>
    <td class="l">N.ro Udienza</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(udienza.getNumCollegio(),"-" )%> </font></td>
  </tr>
  <tr>
    <td class="l">Magistrato</td>
    <td class="l"><font class="campo"><%=udienza.getDescrPresidente() %></font></td>
  </tr>

  <%--tr>
    <td class="l">Giudice relatore</td>
    <td class="l"><font class="campo"><%=udienza.getDescrGiudice1() %></font></td>
  </tr--%>

  <%--tr>
    <td class="l">Giudice relatore</td>
    <td class="l"><font class="campo"><%=udienza.getDescrGiudice2() %></font></td>
  </tr--%>

	<tr>
  		<%-- MEV10-s3: cambiata etichetta (ex Procuratore Repubblica) in funzione dell'utenza collegata --%>
    	<% if ("UDSM".equals(codTipoUfficio)) { %>
      		<td class="l">Procuratore della Repubblica presso il Tribunale dei Minorenni</td>
      	<% } else { %>
      		<td class="l">Procuratore della Repubblica</td>
      	<% } %>
    	<td class="l"><font class="campo"><%=udienza.getDescrPg()%></font></td>
  	</tr>

  <%--tr>
    <td class="l">Esperto n. 1</td>
    <td class="l"><font class="campo"><%=udienza.getDescrIdEsperto1() %></font></td>
  </tr--%>

  <%--tr>
    <td class="l">Esperto n. 2</td>
    <td class="l"><font class="campo"><%=udienza.getDescrIdEsperto2() %></font></td>
  </tr--%>

  <tr>
    <td class="l">Assistente Udienza</td>
    <td class="l"><font class="campo"><%=udienza.getDescrIdAssistente() %></font></td>
  </tr>

  <tr>
    <td class="l">N.ro Massimo Fascicoli</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(udienza.getNumeroMaxFascicoli(),"-")%></font></td>
  </tr>
  <tr>
    <td class="l">Luogo</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(udienza.getLuogoUdienza(),"-")%></font></td>
  </tr>

  <tr>
    <td class="l">Orario Inizio</td>
    <td class="l">
    	<font class="campo"><%=StringUtils.toStringJSP(udienza.getOraInizio(),"--")%> : <%=StringUtils.toStringJSP(udienza.getMinInizio(),"--")%></font>
    </td>
  </tr>

  <tr>
    <td class="l">Orario Fine</td>
    <td class="l">
    	<font class="campo"><%=StringUtils.toStringJSP(udienza.getOraFine(),"--")%> : <%=StringUtils.toStringJSP(udienza.getMinFine(),"--")%></font>
    </td>
  </tr>


  </table>
  </body>
</html>