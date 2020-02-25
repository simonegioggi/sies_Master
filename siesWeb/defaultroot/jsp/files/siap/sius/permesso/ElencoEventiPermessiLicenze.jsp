<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.RedirectTo"%>

<%@ page import="siap.sius.permesso.action.ICostantiEventoPermessoLicenza"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sius.permesso.model.EventoPermessoLicenzaModel"%>

<jsp:useBean id="Modificabile"    scope="request" class="java.lang.String"/>
<!--jsp:useBean id="eventiPermessoLicenza" scope="request" class="java.util.Collection"/-->
<jsp:useBean id="IdLicenzaLibanticipata" scope="request" class="java.lang.String"/>



<%

Collection eventiPermessoLicenza =(Collection) request.getAttribute("eventiPermessoLicenza");

%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--  
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Eventi </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
--%>
<!--  <body class="corpo"> -->
		<table cellspacing=2 cellpadding=2 width="95%">
	    <tr>
	    	<td class="Titolo" colspan=5 >Eventi durante la fruizione: </td>
	    	<td>
	     		<jsp:include page="<%=IWebConstants.PG_BUTTON_INSERT%>">
	     		  <jsp:param name="Modificabile" value="<%=Modificabile%>" />
	      		<jsp:param name="CampoIdEntita" value="<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>" />
	        	<jsp:param name="ValoreIdEntita" value="<%=IdLicenzaLibanticipata%>" />
	      	</jsp:include>
	     	</td>
	    </tr>
	    
	    <tr>
	      <td class="int" width=30%>Tipo</td>
	      <td class="int" width=40%>Descrizione</td>
	      <td class="int" width=15%>Data segnalazione</td>
	      <td class="int" width=20%>Mittente segnalazione</td>
	      <td class="int" width=5%>Azioni</td>
	    </tr>

<%
  Iterator itx = eventiPermessoLicenza.iterator();
  while ( itx.hasNext())
  {
    EventoPermessoLicenzaModel lEventoPerLic = (EventoPermessoLicenzaModel)itx.next();
%>
    <tr>
      <td class=l>
      	<%=StringUtils.toStringJSP(lEventoPerLic.getDescrTipoEvento())%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(lEventoPerLic.getDescrEvento())%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEventoPerLic.getDataSegnalazione(),"dd-MM-yyyy"))%>
      </td>
			<td class=l>
				<%=StringUtils.toStringJSP(lEventoPerLic.getMittenteSegnalazione())%>
			</td>
			
			<td class=c>
<% 
				RedirectTo lRedir = new RedirectTo();
				lRedir.setPage(IWebConstants.PG_MAIN);
				lRedir.setAction("siap.sius.permesso.action.ActLoadDettaglioEventoPermessoLicenza");
				lRedir.setParameter(ICostantiEventoPermessoLicenza.CAMPO_ID_EVENTO_PERMESSO_LICENZA,
		    		StringUtils.toStringJSP(lEventoPerLic.getIdEventoPermessoLicenza().toString()));
%>
      	<a href="<%=lRedir%>">
       		<img src="/images/dettagli.gif" alt="Dettaglio Evento" width="12" height="12" border="0">
        </a>
      </td>
    </tr>
<%
  }
%>
    </table>
  	<br>
<!--  
  </body>
</html>
-->