<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"  %>

<jsp:useBean id="SecondoGiro" scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroPosizioneGiuridica" scope="request" class="java.lang.String"/>

 <%
 
 
 boolean flagSecondo  = false;
 String scelta = "";
 
if(SecondoGiro !=null && SecondoGiro.equals("SI")) 
	flagSecondo = true;

if (filtroPosizioneGiuridica != null && filtroPosizioneGiuridica.length() > 0)  {
	
	if (filtroPosizioneGiuridica.equalsIgnoreCase("tutti")) {
		scelta = "Tutte le posizioni giuridiche";
	}else{
	if (filtroPosizioneGiuridica.equalsIgnoreCase("Libero")) {
			scelta = "Solo Procedimenti collegati a soggetti Liberi";
	}else{ if (filtroPosizioneGiuridica.equalsIgnoreCase("Detenuto"))
			scelta = "Solo Procedimenti collegati a soggetti Detenuti";

	}}} %> 
     
 <table width="60%">
  <% if (flagSecondo) { %>
  <tr><td class="l"><font class="cRosso"> <%=scelta%></font></td></tr>
  <% } else {%>
    <tr><td>&nbsp;</td></tr>
      <tr><td class="Titolo" >Posizione giuridica</td></tr>
<tr><td>
<table width="60%">
	<tr><td class="l">Tutti </td> <td class="c"><input type="radio" name="<%=ICostantiStatistiche.FILTRO_POSIZIONE_GIURIDICA%>" value="tutti" checked  ></td></tr>
	<tr><td class="l">Solo procedimenti collegati a soggetti Liberi </td> <td class="c"><input type="radio" name="<%=ICostantiStatistiche.FILTRO_POSIZIONE_GIURIDICA%>" value="Libero" ></td></tr>
	<tr><td class="l">Solo procedimenti collegati soggetti Detenuti </td> <td class="c"> <input type="radio" name="<%=ICostantiStatistiche.FILTRO_POSIZIONE_GIURIDICA%>" value="Detenuto" ></td></tr>
 <tr><td>&nbsp;</td></tr>	 
  </table> 
  </td></tr>
  </table> 
<% } 
 if (flagSecondo) { 
  %>
<input type="HIDDEN" name="filtroPosizioneGiuridica" value=<%=filtroPosizioneGiuridica%>>
<% } %>