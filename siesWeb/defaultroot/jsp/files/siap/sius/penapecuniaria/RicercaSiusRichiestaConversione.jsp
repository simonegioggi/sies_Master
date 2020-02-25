<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.residenza.action.ICostantiResidenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.residenza.model.ResidenzaModel" %>
<%@ page import="siap.sico.residenza.model.ResidenzaAssociataModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="richiesteConversioni" scope="request" class="java.util.Vector" />
<jsp:useBean id="isModificabile" scope="request" class="java.lang.String" />
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<%@page import="siap.siep.penapecuniaria.model.RichiestaConversioneModel"%>
<%@page import="siap.sius.penapecuniaria.action.ICostantiSiusPenaPecuniaria"%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Richieste Conversione Pena Pecuniaria</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class=label>Funzione :</font>
        <font class=campo>Elenco Richieste Conversione Pena Pecuniaria</font>
      </td>

      <!-- BOTTONE DI INSERIMENTO -->
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.penapecuniaria.action.ActLoadInserisciRichiestaConversionePP&IdFascicoloSius=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>" >
          <img  align="middle" src="/images/new24.gif" alt="Inserimento Richiesta Conversione Pena Pecuniaria" width="24" height="24" border="0">
        </a>
      </td>
      <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
  <br>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>

  <table>
<%
	if (richiesteConversioni.size() == 0 )
  	{
%>
    <td class="L">
      <font class="label"> Non ci sono Richieste Conversione Pena Pecuniaria per il Fascicolo </font>
    </td>
<% 	} else { %>
    	<table width="96%">
  
    	<tr>
      	<td class="int">Anno / Numero Partita</td>
      	<td class="int">Num. ex Campione Penale</td>
      	<td class="int">Prot.Circoscrizione Doganale</td>
      	<td class="int">Autorità</td>
      	<td class="int">Data ricezione atto</td>
      	<td class="int">Data com. impossibile esazione</td>
      	<td class="int" width=5%>Azioni</td>
    	</tr>
<%
  		Iterator itx = richiesteConversioni.iterator();

  		while ( itx.hasNext())
  		{
  			RichiestaConversioneModel richConversione = (RichiestaConversioneModel)itx.next();
%>
    		<tr>
      		<td class="c"><%=StringUtils.toStringJSP(richConversione.getAnnoPartita())%> / <%=StringUtils.toStringJSP(richConversione.getNumPartita())%></td>
      		<td class="c"><%=StringUtils.toStringJSP(richConversione.getNumExCampione(),"-")%></td>
      		<td class="c"><%=StringUtils.toStringJSP(richConversione.getProtCircosrizioneDoganale(),"-")%></td>
<%		
					if (richConversione.getCodTipoAutoritaEmittente() != null){ %>
      			<td class="c"><%=StringUtils.toStringJSP(richConversione.getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(richConversione.getDescrLuogoEmittente())%></td>
  			<%}else {%>
  					<td class="c">-</td><%}%>
      		<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richConversione.getDataRicezioneAtto(),"dd-MM-yyyy"),"-")%></td>
      		<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richConversione.getDataEsazione(),"dd-MM-yyyy"),"-")%></td>
<%				if (isModificabile.compareTo("SI")==0 )
    			{ %>
      			<td class=c>
        			<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           			<jsp:param name="CampoIdEntita" value="<%=ICostantiSiusPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE%>" />
           			<jsp:param name="ValoreIdEntita" value="<%=richConversione.getIdRichiestaConversione()%>" />
        			</jsp:include>
      			</td>
    		</tr>
    		<%} else {%>
      			<td class=c>-</td>
    	</tr>
    		<%}
  		}
%>
    </table>
		<% } %>
  </form>
  </body>
</html>