<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.trasmissione.model.TrasmissioniModel" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<jsp:useBean id="trasmissioni" scope="request" class="java.util.Vector" />

<html>
  <head>
    	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    	<title>[S.I.E.S.] - Visualizzazione Dati Trasmissioni </title>
   </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>" >
  
  <table>
    <tr>
    		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Trasmissioni</font></td>
    </tr>
  </table>

 <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include> 

  <br>

  <div align=center>
  <table>
    	<tr>
		      <td class="int">Data Trasmissione</td>
		      <td class="int">Procedimento</td>
		      <td class="int" width=5%>Esito</td>
		      <td class="int">Tipo Operazione</td>
		      <td class="int">Destinazione</td>
		      <td class="int">Chiavi SIES</td>
		      <td class="int">Chiavi NSC</td>
   	 </tr>

<%
  Iterator itx = trasmissioni.iterator();

  while ( itx.hasNext())
  {
    TrasmissioniModel lTrasmissioni = (TrasmissioniModel)itx.next();
    
%>
    	<tr>
    				
      				
      		<td class=c><%=DateUtils.getDateToString(lTrasmissioni.getDataTrasmissione(),"dd-MM-yyyy HH:mm:ss")%></td>
      		<%if (lTrasmissioni.getChiaveAnno() != null && lTrasmissioni.getChiaveProgr()!= null) 
      				{%>  
      						<td class=c><%=lTrasmissioni.getChiaveAnno()%> / <%=lTrasmissioni.getChiaveProgr()%></td>
      				<%} 
      				else 
      				{%> <td class=c>&nbsp;</td>
      				<%}%>
      				
      			<% 
      				if (lTrasmissioni.getEsitoTrasmissione().equals("0") )
      		    {
      		  			%><td class=c>Trasferito</td><%
      		    }
      				else if (lTrasmissioni.getEsitoTrasmissione().equals("100") || lTrasmissioni.getEsitoTrasmissione().equals("200"))
      				{
    		  			%><td class=c>NON Trasferito</td><%
    		    	}
      				else if (lTrasmissioni.getEsitoTrasmissione().equals("2") )
      				{
    		  			%><td class=c>Procedimento già Presente</td><%
    		    	}
      				%>
      				<td class=c><%=lTrasmissioni.getTipoOperazione()%></td>
      				<td class=c><%=lTrasmissioni.getDestinazione()%></td>

      				<%if (lTrasmissioni.getChiaveSiesSogg() != null && lTrasmissioni.getChiaveSiesFasc() != null) 
      				{
		      				if (lTrasmissioni.getChiaveSiesSogg().intValue() == 0 && lTrasmissioni.getChiaveSiesFasc().intValue() == 0)
		      				{
		      				  %><td class=c>&nbsp;</td><% 
		      				}
		      				else
		      				{  
		      				  %><td class=c><%=lTrasmissioni.getChiaveSiesSogg()%> / <%=lTrasmissioni.getChiaveSiesFasc()%></td><% 
		      			  }%>
		      		<%} else {%>
		      					<td class=c>&nbsp;</td>
		      		<%}%>
		      		
      				<%if (lTrasmissioni.getChiaveNscSogg() != null && lTrasmissioni.getChiaveNscProv() != null) 
      				{
		      				if (lTrasmissioni.getChiaveNscSogg().intValue() == 0 && lTrasmissioni.getChiaveNscProv().intValue() == 0)
		      				{
		      				  %><td class=c>&nbsp;</td><% 
		      				}
		      				else
		      				{  
		      				  %><td class=c><%=lTrasmissioni.getChiaveNscSogg()%> / <%=lTrasmissioni.getChiaveNscProv()%></td><% 
		      			  }%>
		      		<%} else {%>
		      					<td class=c>&nbsp;</td>
		      		<%}%>
		      		
		      						   
      	</tr>		
    <%
  	}
    %>  	
    </table>
</div>
</form>
  </body>
</html>