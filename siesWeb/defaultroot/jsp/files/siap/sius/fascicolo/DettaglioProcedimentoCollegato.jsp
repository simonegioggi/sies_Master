<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.siep.posizionematerialefasc.action.ICostantiPosizioneMaterialeFasc"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="fascicoloPadre" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel" />

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
          <td class="LBG">
<%    if (fascicoloPadre.getFascicoloSiusModel().getIdFascicoloSius() != null) { %>
        <!-- BOTTONE DI MODIFICA -->
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadModificaCollegamento&IdFascicoloSius=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>&provenienza=D" >
            <img  align="middle" src="/images/modifica24.gif" alt="Modifica Procedimento" width="24" height="24" border="0">
          </a>
        </td>
        <!-- BOTTONE DI CANCELLAZIONE -->
        <td class="LBG">
          <a href="Javascript:conferma('siap.sius.fascicolo.action.ActCancellaCollegamento','IdFascicoloSius','<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>','TornaQui','');">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
        </td>
<% } else { %>
        <!-- BOTTONE DI INSERIMENTO -->
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadInserisciCollegamento&IdFascicoloSius=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>" >
            <img  align="middle" src="/images/new24.gif" alt="Modifica Procedimento" width="24" height="24" border="0">
          </a>
        </td>
<% } %>
        <!-- BOTTONE DI RITORNO AL DETTAGLIO FASCICOLO -->
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&IdFascicoloSius=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>" >
            <img  align="middle" src="/images/arrowleft24.gif" alt="Ritorna al Dettaglio Fascicolo" width="24" height="24" border="0">
          </a>
        </td>

    </table>

    <br>
     <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <br>
<% if (fascicoloPadre.getFascicoloSiusModel().getIdFascicoloSius() != null)
		{%>
    	<table>
    		<tr>
					<td colspan='2'><font class="campo"> Collegato al fascicolo :</font></td>
    		</tr>
    		<tr>
      		<td class=c>Anno/Numero SIUS</td>
      		<td class=c><%=fascicoloPadre.getFascicoloSiusModel().getChiaveAnno()%>/<%=fascicoloPadre.getFascicoloSiusModel().getChiaveProgr()%></td>
    		</tr>
    		<tr>
      		<td class=c>Autorità</td>
      		<td class=c><%=fascicoloPadre.getFascicoloSiusModel().getDescrTipoUfficio()%></td>
    		</tr>
    		<tr>
      		<td class=c>Luogo</td>
      		<td class=c><%=fascicoloPadre.getFascicoloSiusModel().getDescrComuneUfficio()%></td>
     			<td class=c>
    		</tr>
    	</table>
		<%}else{ %>
  		<br>
			<font class="campo"> Il Procedimento è privo di collegamento </font>
		<%}%>

  </body>
</html>