<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
 
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.RedirectTo" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche" %>
<%@ page import="siap.sius.statistiche.model.ProcAggregatiCognomeModel" %>
<%@ page import="siap.sius.statistiche.model.RicercaAggregatiCognomeModel" %>

<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>


<jsp:useBean id="ricercaProcedimenti"   scope="session" class="siap.sius.statistiche.model.RicercaAggregatiCognomeModel" />
<jsp:useBean id="elencoProcedimenti"    scope="request" class="java.util.ArrayList" />
<jsp:useBean id="TornaQui"              scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Procedimenti aggregati per Cognome </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

  <BODY class="corpo">
	  <table>
	    <tr>
	       <td class="LBG">
	         <a href="Javascript:window.print();">
	         <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
	       </td>
	       <td class="LBG">
	       	 <font class=label>Funzione :</font>&nbsp;
	       	 <font class="campo"> Elenco Procedimenti aggregati per Cognome </font>
	       </td>
	       <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	       <!-- NUOVO BOTTONE PER STAMPA EXCEL --> 
					<td class=l>
						<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.statistiche.action.ActRicercaProcAggregatiPerInizialeCognomeExcel">
							<img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
						</a>
					</td>
	  	</table>

  <br>
  
  <table cellspacing=2 cellpadding=2>
  
<%
	if( ricercaProcedimenti != null ) { 
      	if( ricercaProcedimenti.getDataInizio() != null || ricercaProcedimenti.getDataFine() != null ) {
%>
        	<tr>
          		<td class="lVerdeNB">Periodo&nbsp;&nbsp;
					dal <%=DateUtils.getDateToString( ricercaProcedimenti.getDataInizio(), "dd-MM-yyyy" )%>
					&nbsp;&nbsp;
            		al <%=DateUtils.getDateToString( ricercaProcedimenti.getDataFine(), "dd-MM-yyyy" )%>
            	</td>
        	</tr>
<%
		} 
	} 
%>

  </table>
  <br>
<%
  Iterator itx = elencoProcedimenti.iterator();
  BigDecimal totaleComplessivo = new BigDecimal("0");
%>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Procedimenti con soggetti avente iniziale cognome</td>
      <td class="int">Totale</td>
    </tr>
<%
   while ( itx.hasNext()) {
       ProcAggregatiCognomeModel elenco = (ProcAggregatiCognomeModel)itx.next();
       totaleComplessivo = totaleComplessivo.add(elenco.getTotale());
%>
      <tr>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP(elenco.getIniziale())%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP(elenco.getTotale())%></font>
        </td>
      </tr>
<%
  }
%>
      <tr>
        <td class="c">
        	<font class="label">Totale complessivo</font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP(totaleComplessivo)%></font>
        </td>
      </tr>
    </table>

  </body>
</html>