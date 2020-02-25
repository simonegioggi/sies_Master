<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.model.EventoFascicoloModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.evento.model.EventoFascicoloModel"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicoliEventi" scope="request" class="java.util.ArrayList"/>

<%@page import="siap.sico.evento.model.EventoFascicoloModel;"%>
<html>
  <head>
    <title>[S.I.E.S.] - Elenco Omesse Notifiche OE con  Sospensione </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  </head>
  
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :  Elenco Omesse Notifiche OE con  Sospensione </font>&nbsp;&nbsp;
    <input type="HIDDEN" name="validato" value="">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
     </td>
    </tr>
  </table>
	<br>	
	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
	<br>
  <table cellspacing=2 cellpadding=2>
    <tr>
			<td class="int">Numero SIEP</td>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Data Emissione</td>
      <td class="int">Provvedimento</td>
      <td class="int">Autorità</td>
      <td class="int">Azioni</td>
    </tr>
<%
		// Si considerano le notifiche del primo evento trovato
		int lIndiceEventi =0;
    Iterator itx = fascicoliEventi.iterator();
    while ( itx.hasNext())
    {
      EventoFascicoloModel lFasEve = (EventoFascicoloModel)itx.next();
      FascicoloSiepModel lFas = lFasEve.getFascicoloSiep();
      
      EventoNotificaModel lEveNot = new EventoNotificaModel();
      if( !lFasEve.getEventi().isEmpty() )
      {
        lEveNot = (EventoNotificaModel)lFasEve.getEventi().get(0);
      }
%>
    <tr>
      <td class="c"><%=StringUtils.toStringJSP(lFas.getChiaveAnno())%>/<%=StringUtils.toStringJSP(lFas.getChiaveProgr())%></td>
<%
			if( lFas.getSoggetto() != null )
			{
%>
      	<td class="l"><%=StringUtils.toStringJSP(lFas.getSoggetto().getCognome())%></td>
      	<td class="l"><%=StringUtils.toStringJSP(lFas.getSoggetto().getNome())%></td>
<%			  
			}
			else
			{
%>
      	<td class="c">&nbsp;</td>
      	<td class="c">&nbsp;</td>
<%			  
			}
%>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEveNot.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></td>
			<td class="l">
				<%=StringUtils.toStringJSP(lEveNot.getEvento().getDescrTipoProvvedimento())%>
				&nbsp;
				<%=StringUtils.toStringJSP(lEveNot.getEvento().getDescrMotivo(),"-")%>
			</td>
			<td class="l"><%= StringUtils.toStringJSP(lEveNot.getEvento().getDescrUfficioEmittente()) + " " + StringUtils.toStringJSP(lEveNot.getEvento().getDescrLuogoEmittente()) %>&nbsp;</td>
	      <td class="c">
		      <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS%>">
		        <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
		        <jsp:param name="ValoreIdEntita" value="<%=lEveNot.getEvento().getIdEvento()%>"/>
		      </jsp:include>
      	</td>
			</tr>
<%
		}
%>   
  </table>
</body>
</html>