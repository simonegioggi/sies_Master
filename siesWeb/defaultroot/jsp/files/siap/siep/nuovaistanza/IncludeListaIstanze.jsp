<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<html>
<head>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="listaIstanze"  scope="request" class="java.util.Vector"/>
<jsp:useBean id="listaeventi"  scope="request" class="java.util.Vector"/>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
  <script language="JavaScript" src="/html/gestisciUploadStampa2.js"></script>

<script language="JavaScript">

 function dettaglioIstanza(idIstanza,idEventoIstanza)
 {
	   document.getElementById(idIstanza).style.visibility = 'visible';
	   document.getElementById(idIstanza).style.display = 'block';
	   
	   document.getElementById("listIstanza").style.visibility = 'hidden';
	   document.getElementById("listIstanza").style.display = 'none';
	   document.getElementById('containerIst').style.height = '160px';
				 
	   document.getElementById("datiInoltro").style.visibility = 'visible';
	   document.getElementById("datiInoltro").style.display = 'block';

	   document.getElementById("IdNuovaIstanza").value =idIstanza;
	   document.getElementById("IdEventoNuovaIstanza").value =idEventoIstanza;
 }

function conferma(a_action, idIstanza, eventodacanc, tipo)
 {
  if (window.confirm('Confermi la cancellazione ?'))
  {
    if (tipo=="A")
    {
       var  desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=" + a_action + "&IdEvento=" + eventodacanc +"&IdIstanza=" + idIstanza+ "&TipoOp=" + tipo +"&TipoProvvedimento=Inoltro", "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=450,height=200");
         window.parent.close();
    }else
    {
      str = "/jsp/Main.jsp?Action=siap.siep.nuovaistanza.action.ActCancellaInoltroNuovaIstanza&IdEvento="+eventodacanc+"&IdIstanza="+idIstanza+"&TipoOp="+tipo+"&TipoProvvedimento=Inoltro";
               window.location.href=str;
    }
  }
 }
   </script>
   
</head>
<body>
    <table width='100%'>
      <tr><td> </td></tr> 
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
}	%>	
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
<table width="100%" bordercolor="CCCCCC"> 


<%

if((listaIstanze!=null && listaIstanze.size()>1) ||
	(listaeventi.size()>0 && listaIstanze.size()==1))
{
%>	
  <input type="hidden"  name="IdNuovaIstanza" value="" />     
  <input type="hidden"  name="idEventoIstanza" value="" />     
	<tr>
		<td colspan="3" class="L">Seleziona istanza dalla lista:</td>
	</tr>	
	<tr>
		<td class="intLavander">Data Istanza</td>
		<td class="intLavander">Contenuto/Oggetto</td>
		<td class="intLavander">Data Inoltro</td>
		<td class="intLavander">Autorità</td>
		<td class="intLavander">Validato</td>
		<td class="intLavander">Azioni</td>
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

   NuovaIstanzaModel lModel =  (NuovaIstanzaModel)lItx.next(); 
   %>
	<tr class="<%=colorTr%>">
	<td class="<%=colorTr%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lModel.getDataIstanza(),"dd-MM-yyyy"))   %></td>
	<td class="<%=colorTr%>"><%=lModel.getDescrContenuto()   %></td>
	<td class="<%=colorTr%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lModel.getDataInoltroPM(),"dd-MM-yyyy"))%></td>
	
	<%
	if (lModel.getDescrSedeMittente().equals("-"))
	{
	%>
		<td class="<%=colorTr%>"><%=lModel.getDescrAutoritaMittente() %> &nbsp;</td>
	<%
	} else { %>
		<td class="<%=colorTr%>"><%=lModel.getDescrAutoritaMittente() %> &nbsp; di &nbsp;<%=lModel.getDescrSedeMittente() %></td>
	<%} %>
	
	<td class="<%=colorTr%>">&nbsp;
	<% if (!lModel.getDescrStatoIstanza().contains("da Validare") && lModel.getDataInoltroPM()!=null) {%>
	          <img src="/images/TickRed.gif">
	<%}%>	
	</td>
	<td class="<%=colorTr%>">
	<% 
	if (lModel.getDescrStatoIstanza().contains("da Validare") || lModel.getDataInoltroPM()!=null) {
	%>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza&IdEvento=<%=lModel.getCodEsito()%>&TipoVis=Inoltro">
			    <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Inoltro PM" border="0">
			</a>		
	<%
	}
	if (!lModel.getDescrStatoIstanza().contains("da Validare")&&lModel.getDataInoltroPM()!=null) {
	%>
			<!-- BOTTONE DI STAMPA -->
			<a href="Javascript:stampa2('<%=ISIAPCostantiWeb.PG_STAMPA%>','<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActLoadDocumento&IdEvento=<%=lModel.getCodEsito()%>');">
				<img src="/images/print.gif" alt="Stampa Inoltro PM" width="12" height="12" border="0">
			</a>
	<%
	}
	if(lModel.getDataInoltroPM()!=null) { 
		if (!lModel.getDescrStatoIstanza().contains("da Validare")){
	%>
	      <a href="Javascript:conferma('siap.siep.nuovaistanza.action.ActLoadCancellaInoltroNuovaIstanza','<%=lModel.getIdNuovaIstanza()%>','<%=lModel.getCodEsito()%>','A');">
	     	<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
	      </a>
	<%
		} else {
	%>
	      <a href="Javascript:conferma('siap.siep.nuovaistanza.action.ActCancellaInoltroNuovaIstanza','<%=lModel.getIdNuovaIstanza()%>','<%=lModel.getCodEsito()%>','C');">
	     	<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
	      </a>	
	<%			
		}
	} 
	if(lModel.getDataInoltroPM()==null) { 
	%>
			<input type="radio" name="checkBox" onClick="javascript:dettaglioIstanza(<%=lModel.getIdNuovaIstanza()%>,<%=lModel.getEveIdEvento()%>);">
	<%
	} 
	%>
	</td>

<!-- ADESSO devo inserire  eventuali annullamenti dell'inoltro -->
<% 
Iterator<EventoModel> lIty = listaeventi.iterator() ;
while (lIty.hasNext())
{  
   EventoModel leveModel =  (EventoModel)lIty.next(); 
  // if (lModel.getEveIdEvento().equals(leveModel.getEveIdEvento()) && leveModel.getFlagDocumentoRegistrato().equals("A")) {
	  if (lModel.getEveIdEvento().equals(leveModel.getEveIdEvento()) && ("A").equals(leveModel.getFlagDocumentoRegistrato())) {
%>
	<tr class="cGrigio" style="font-size: 11px; text-align=left" >
	<td class="cGrigio" style="font-size: 11px; text-align=left" ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lModel.getDataIstanza(),"dd-MM-yyyy"))   %></td>
	<td class="cGrigio" style="font-size: 11px; text-align=left" ><%=lModel.getDescrContenuto()   %></td>
	<td class="cGrigio" style="font-size: 11px; text-align=left" ><%=StringUtils.toStringJSP(DateUtils.getDateToString(leveModel.getDataEmissione(),"dd-MM-yyyy"))%></td>
	<td class="cGrigio" style="font-size: 11px; text-align=left" ><%=lModel.getDescrAutoritaMittente() %> &nbsp;
	<%
	if (!lModel.getDescrSedeMittente().equals("-")){%>
		di &nbsp;<%=lModel.getDescrSedeMittente() %>
	<%} %>
	</td>
	<td class="cRosso" style="font-size: 11px; text-align=left" >Annullato</td>	
	<td class="cGrigio" style="font-size: 11px; text-align=left" >
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza&IdEvento=<%=leveModel.getIdEvento()%>&TipoVis=Inoltro">
			    <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Inoltro PM" border="0">
			</a>		
	</td>
<%
   }
}  
%>



<!-- Fine -->	 
	</tr>
	
	<%i++;
	} 
}%>
</table>
</div>

<%String lDisplayIstanza = "display:none";
if(listaIstanze.size()==1 && 
		(listaeventi.size()==0))
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
	<div style="<%=lDisplayIstanza%>" id="<%=lModel.getIdNuovaIstanza() %>" >
  	<input type="hidden"  name="IdNuovaIstanza" value="<%=lModel.getIdNuovaIstanza()%>" />     
  	<input type="hidden"  name="IdEventoNuovaIstanza" value="<%=lModel.getEveIdEvento()%>" />     

    <table>
      <tr>
      	<td class="Titolo">Dati dell'Istanza </td>
      	<% 
      	if(lModel.getFlagPresdep().equals("P")){
      	%>
      		<td class="Titolo">Pervenuta</td>
      	<%}else{ %>
      		<td class="Titolo">Depositata</td>
      	<%}%>
      </tr>
<% 
	if(lModel.getFlagPresdep().equals("P")){
%>
	<tr> 
		<td class="l">Data Atto</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lModel.getDataIstanza(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Autorità Mittente</td>
		<td class="l"><font class="campo"><%=lModel.getDescrAutoritaMittente()%></font> di <font class="campo"><%=lModel.getDescrSedeMittente()%></font></td>
	</tr>
<%}else{%>	
  <tr>
    <td class="l">Depositata in data</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lModel.getDataIstanza(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Soggetto Presentante</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lModel.getSoggPresentante()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Identificato con</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lModel.getSoggPresentanteIdentificato()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Presentata da Avvocato</td>
		<%if(lModel.getAvvocatoPresentante() != null)
  		{%>
  		<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
    		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lModel.getAvvocatoPresentante().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lModel.getAvvocatoPresentante().getNome()) %></font>&nbsp;</td>
		<%}else{%>
   			<td class="l">&nbsp;</td>
		<%}%>
	</tr>  
<%}%>
	
	<%String NomeAvvocato="-", Foro="-",TipoDifensore="-",  Nominato="";
	if(lModel.getAvvocato()!=null)
	{
		NomeAvvocato = lModel.getAvvocato().getCognome()+" " +lModel.getAvvocato().getNome(); 
		Foro=lModel.getAvvocato().getForo();
		TipoDifensore=lModel.getAvvocato().getDescrTipo();
		Nominato = StringUtils.toStringJSP(DateUtils.getDateToString(lModel.getDataIstanza(),"dd-MM-yyyy"));
	}%>
	<tr><td class="l">Avvocato</td>	<td class="l"><font class="campo"><%=NomeAvvocato%></font> </td></tr>
	<tr><td class="l">Foro di Competenza</td><td class="l"><font class="campo"><%=Foro%></font></td></tr>
	<tr><td class="l">Tipo Difensore</td><td class="l"><font class="campo"><%=TipoDifensore%></font></td></tr>
	<tr><td class="l">Nominato in Data</td><td class="l"><font class="campo"><%=Nominato%>&nbsp;</font></td></tr>
	
	<tr><td class="l">Oggetto Istanza</td>
			<td class="l"><font class="campo"><%=lModel.getDescrContenuto()%></font></td>
	</tr>
	<tr><td class="l">Note</td>
		<td  class="l"><font class="campo"><%=StringUtils.toStringJSP(lModel.getNote())%>&nbsp;</font></td>
	</tr>
	</table>
</div>
	<%i++;
	} 
%>
<br><br><br>
<br><br><br>
</div>

</body>
</html>