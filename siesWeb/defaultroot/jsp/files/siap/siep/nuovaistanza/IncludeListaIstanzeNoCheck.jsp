<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<html>
<head>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<jsp:useBean id="listaIstanze"  scope="request" class="java.util.Vector"/>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">   
</head>
<body>
    <table width='100%'>
      	<tr><td></td></tr>
	</table>
         
 <style type="text/css">
td.one {
	background-color: AliceBlue; 
	color : navy;
	font-family: 'Tahoma';
   font-size: 11px;
font-weight : bold;
   }
td.two {
	background-color: Lavender; 
	color : navy;
	font-family: 'Tahoma';
    font-size: 11px;
font-weight : bold;
   }
</style>
<%
String sizeContainer ="30px";
if(listaIstanze!=null && listaIstanze.size()>1)
{
	Integer ciccio = 30*listaIstanze.size()+30;
	sizeContainer = ciccio + "px";
}	
%>	
<style type="text/css"> 
				#containerIst {
				height:<%=sizeContainer%>;
				position:relative;
				}
				#containerIst div {
				position:absolute;
				top:0px; } 
				#containerIst .listIstanza { position:absolute;top:0px; left:0px; z-index:0; display:none; }
 			</style>
<div id="containerIst">
<div id="listIstanza">


<%
if(listaIstanze!=null && listaIstanze.size()>1)
{
%>	
<table width="100%" bordercolor="CCCCCC"> 
  <input type="hidden"  name="IdNuovaIstanza" value="" />     
  <input type="hidden"  name="idEventoIstanza" value="" />     
      <tr><td class="Titolo" colspan="4">Istanze </td></tr>
	<tr>
		<td class="intLavander">Data Istanza</td>
		<td class="intLavander">Contenuto/Oggetto</td>
		<td class="intLavander">Stato Istanza</td>
		<td class="intLavander">Autorità</td>
	</tr>
<% 
Iterator<NuovaIstanzaModel> lItx = listaIstanze.iterator() ;

int i = 1;
String colorTr = "";
while (lItx.hasNext())
{  
	if(i%2==0)
		colorTr = "one";
	else
		colorTr = "two";

   NuovaIstanzaModel lModel =  (NuovaIstanzaModel)lItx.next(); %>
	<tr class="<%=colorTr%>">
		<td class="<%=colorTr%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lModel.getDataIstanza(),"dd-MM-yyyy"))   %></td>
		<td class="<%=colorTr%>"><%=lModel.getDescrContenuto()   %></td>
		<td class="<%=colorTr%>"><%=lModel.getDescrStatoIstanza()   %></td>
	<%
	if (lModel.getDescrSedeMittente().equals("-"))
	{
	%>
		<td class="<%=colorTr%>"><%=lModel.getDescrAutoritaMittente() %> &nbsp;</td>
	<%} else { %>
		<td class="<%=colorTr%>"><%=lModel.getDescrAutoritaMittente() %> &nbsp; di &nbsp;<%=lModel.getDescrSedeMittente() %></td>
	<%} %>
	</tr>
	
	<%i++;
	} %>
</table>
<%
} %>

</div>

<%String lDisplayIstanza = "display:none";
if(listaIstanze!=null && listaIstanze.size() == 1) {
{
	lDisplayIstanza = "display:block";
 }//fine if sulla size==1
Iterator lItx = listaIstanze.iterator() ;

int i = 1;
while (lItx.hasNext())
{  
	NuovaIstanzaModel lModel =  (NuovaIstanzaModel)lItx.next(); %>
	<style type="text/css"> 
	
	   #containerIst .<%=lModel.getIdNuovaIstanza() %> { position:absolute;top:0px; left:0px; z-index:1; }
	</style>
	<table width="100%"  bordercolor="CCCCCC"> 
      <tr>
      	<td class="Titolo">Dati dell'istanza </td>
      	<% 
      	if(lModel.getFlagPresdep().equals("P")){
      	%>
      		<td class="Titolo" colspan="3">PERVENUTA</td>
      	<%}else{ %>
      		<td class="Titolo" colspan="3">DEPOSITATA</td>
      	<% }%>
      </tr>
	<tr> 
		<td class="l">Data Atto</td>
    	<td class="l" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lModel.getDataIstanza(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Mittente</td>
		<td class="l"><font class="campo"><%=lModel.getDescrAutoritaMittente()%></font></td>
		<td class="l">Sede mittente</td>
		<td class="l"><font class="campo"><%=lModel.getDescrSedeMittente()%></font></td>
	</tr>
	<%String NomeAvvocato="-", Foro="-",TipoDifensore="-",  Nominato="";
	if(lModel.getAvvocato()!=null) 
		{ NomeAvvocato = lModel.getAvvocato().getCognome()+" " +lModel.getAvvocato().getNome(); 
		Foro=lModel.getAvvocato().getForo();
		TipoDifensore=lModel.getAvvocato().getDescrTipo();
		Nominato = StringUtils.toStringJSP(DateUtils.getDateToString(lModel.getDataIstanza(),"dd-MM-yyyy"));
		} %>
	<tr><td class="l">Avvocato</td>	<td class="l"><font class="campo"><%=NomeAvvocato%></font> </td>
	<td class="l">Foro Competenza</td><td class="l"><font class="campo"><%=Foro%></font></td></tr>
	<tr><td class="l">Tipo Difensore</td><td class="l"><font class="campo"><%=TipoDifensore%></font></td>
	<td class="l">Nominato in Data</td><td class="l"><font class="campo"><%=Nominato%></font></td>
	</tr>
	<tr><td class="l">Oggetto Istanza</td><td  class="l" colspan="3"><font class="campo"><%=lModel.getDescrContenuto()%></font></td>	</tr>
	<tr><td class="l">Note</td>
	<% 
		if (lModel.getNote()!=null) { %>
	<td  class="l" colspan="3"><font class="campo"><%=lModel.getNote()%></font></td>	</tr>
	<%} %>
	</table>
	<%i++;
	} 
}  // nuovo fine IF per size = 1
%>
</div>

</body>
</html>