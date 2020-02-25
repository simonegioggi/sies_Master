<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>

<jsp:useBean id="UtenteConnesso" 		scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicoloSigeEsteso" 	scope="request" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="fascicoloPadre" 	    scope="request" class="siap.sige.fascicolo.model.FascicoloSigeModel" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Dettaglio Estremi Procedimento Collegato</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class=label>Funzione :</font> <font class=campo> Dettaglio Estremi Procedimento Collegato</font> </td>

<% if (fascicoloPadre != null && fascicoloPadre.getIdFascicoloSige() != null) { %>
        <!-- BOTTONE DI MODIFICA -->
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadModificaCollegamento&IdFascicoloSige=<%=fascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige()%>&provenienza=D" >
            <img  align="middle" src="/images/modifica24.gif" alt="Modifica Procedimento" width="24" height="24" border="0">
          </a>
        </td>
        <!-- BOTTONE DI CANCELLAZIONE -->
        <td class="LBG">
          <a href="Javascript:conferma('siap.sige.fascicolo.action.ActCancellaCollegamento','IdFascicoloSige','<%=fascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige()%>','TornaQui','');">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
        </td>
<% } else { %>
        <!-- BOTTONE DI INSERIMENTO -->
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadInserisciCollegamento&IdFascicoloSige=<%=fascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige()%>" >
            <img  align="middle" src="/images/new24.gif" alt="Modifica Procedimento" width="24" height="24" border="0">
          </a>
        </td>
<% } %>
        <!-- BOTTONE DI RITORNO AL DETTAGLIO FASCICOLO -->
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&IdFascicoloSige=<%=fascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige()%>" >
            <img  align="middle" src="/images/arrowleft24.gif" alt="Ritorna al Dettaglio Fascicolo" width="24" height="24" border="0">
          </a>
        </td>

    </table>

    <br>
     <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    <br>
<% 	if (fascicoloPadre != null && fascicoloPadre.getIdFascicoloSige() != null)
		{%>
    	<table>
    		<tr>
					<td colspan='2'><font class="campo"> Collegato al fascicolo :</font></td>
    		</tr>
    		<tr>
      		<td class=c>Anno/Numero SIGE</td>
      		<td class=c><%=fascicoloPadre.getChiaveAnno()%>/<%=fascicoloPadre.getChiaveProgr()%></td>
    		</tr>
    		<tr>
      		<td class=c>Autorità</td>
      		<td class=c><%=fascicoloPadre.getDescrTipoUfficioInserimento()%></td>
    		</tr>
    		<tr>
      		<td class=c>Luogo</td>
      		<td class=c><%=fascicoloPadre.getDescrUfficio()%></td>
     			<td class=c>
    		</tr>
    	</table>
		<%}else{ %>
  		<br>
			<font class="campo"> Il Procedimento è privo di collegamento </font>
		<%}%>

  </body>
</html>