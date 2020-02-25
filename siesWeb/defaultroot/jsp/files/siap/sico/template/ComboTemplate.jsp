<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="ElencoTemplate"	scope="request" class="java.lang.String"/>
<jsp:useBean id="AutoTemplate"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="Stampabile"		scope="request" class="java.lang.String"/>

<%
// Elenco template di stampa solo se il documento è stampabile
if (Stampabile == null || Stampabile.trim().length() < 1)
    Stampabile = "SI";
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
siesLogger.info("Stampabile -> "+ Stampabile);

if (Stampabile.compareTo("SI") == 0) {
%>
<table cellspacing="2" cellpadding="2" width="95%">
<%
	if (ElencoTemplate == null || ElencoTemplate.length() == 0) {
%>
	<tr>
 		<td>
 			<font color="red">Modelli di stampa non disponibili!</font>
 		</td>
	</tr>
<%
	} else {
%>
	<tr>
    	<td class="l" width="70%">Modello di stampa</td>
    	<td class="l" width="30%">
       		<select Title="Modello di stampa" name="ListaTemplate">
        		<%=ElencoTemplate%>
      		</select>
    	</td>
	</tr>
<%
		if (AutoTemplate.compareTo("") != 0) {
%>
	<tr>
		<td class="l" width="70%">default:</td>
		<td class="l" width="30%"><%=AutoTemplate%></td>
	</tr>
<%
		}
	}
%>
</table>
<%
}
%>