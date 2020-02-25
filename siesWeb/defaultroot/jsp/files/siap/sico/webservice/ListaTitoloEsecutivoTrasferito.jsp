<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<jsp:useBean id="FascSoggSent" scope="request" class="java.util.Vector" />

<html>
  <head>
    	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    	<title>[S.I.E.S.] - Visualizzazione Titoli Esecutivi Trasferiti </title>
   </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>" >
  
  <table>
    <tr>
    		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Titoli Esecutivi Trasferiti</font></td>
    </tr>
  </table>

 <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include> 

  <br>

  <div align=center>
  <table>
    	<tr>
    			<td class="int">Data Trasferimento</td>
    			<td class="int">Procedimento</td>
		      <td class="int">Data Sentenza</td>
		      <td class="int"> Anno / Numero Sentenza</td>
		      <td class="int">Soggetto</td>
		      <td class="int">Stato</td>
   	 </tr>

<%
  Iterator itx = FascSoggSent.iterator();

  while ( itx.hasNext())
  {
  		FascicoloSiepModel lFascicolo = (FascicoloSiepModel)itx.next();
    
%>
    	<tr>
    			<td class=c><%=DateUtils.getDateToString(lFascicolo.getDataIscrizione(),"dd-MM-yyyy")%></td>
    			<td class=c><%=lFascicolo.getChiaveAnno()%> / <%=lFascicolo.getChiaveProgr()%></td>
      		<td class=c><%=DateUtils.getDateToString(lFascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></td>
      		<td class=c><%=lFascicolo.getSentenza().getAnnoSentenza()%> / <%=lFascicolo.getSentenza().getNumeroSentenza()%></td>
					<td class=c><%=lFascicolo.getSoggetto().getCognome()%>&nbsp;<%=lFascicolo.getSoggetto().getNome()%></td>      				
				  <td class=c><%=lFascicolo.getDescrStatoFascicolo()%>
      	</tr>		
    <%
  	}
    %>  	
    </table>
</div>
</form>
  </body>
</html>