<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel"%>
<%@ page import="siap.siep.misuracautelare.action.ICostantiMisuraCautelare"%>

<jsp:useBean id="misuracautelare" scope="request" class="siap.siep.misuracautelare.model.MisuraCautelareModel"/>
<jsp:useBean id="computabilità" scope="request" class="java.lang.String"/>
<jsp:useBean id="lPenaResMod"    	 scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="AutoritaEmittenteDesc"        scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioPM"        scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioPmSede"        scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaCompTerritorio"        scope="request" class="java.lang.String"/>

<%
//==============================================================================
//  ATTENZIONE!!     FORM MAI UTILIZZATA GENERATA DAL FRAMEWORK
//==============================================================================
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio MisuraCautelare </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
</head>
<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Misura Cautelare</font>
      </td>
   <%   
   // richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena 
   // paolo cherubini lunedi 11/10/2010
   	if (lPenaResMod!=null  && lPenaResMod.getFlagValidato().equals("N"))  {%>
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiMisuraCautelare.CAMPO_ID_MISURA_CAUTELARE%>" />
          <jsp:param name="ValoreIdEntita" value="<%=misuracautelare.getIdMisuraCautelare()%>" />
          </jsp:include>
      </td>
      <% }  %>
       <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
  <br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
</FORM>
	<table>
		<% if (misuracautelare!=null  && (misuracautelare.getAnnoRgnr()!=null || misuracautelare.getNumeroRgnr()!=null))  {%>
      	<tr>
          <td class="l">Anno/Numero R.G.N.R. </td>
          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getAnnoRgnr()+"/"+StringUtils.toStringJSP(misuracautelare.getNumeroRgnr()))%> </font></td>     
          <td class="l"> &nbsp; </td>
          <td class="l"> &nbsp; </td>
          <td class="l"> &nbsp; </td>
          <td class="l"> &nbsp; </td>
        </tr>
        <% }  %>
       
        <tr>          
          <% if (tipoUfficioPM!=null && !tipoUfficioPM.equals(""))  {%>
          <td class="l">Tipo Ufficio PM (*) </td>
          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(tipoUfficioPM) %></font></td>
          <% } else { %>
          <td class="l"> &nbsp; </td>
          <td class="l"> &nbsp; </td>
          <% } %>
          <% if (tipoUfficioPmSede!=null && !tipoUfficioPmSede.equals(""))  {%>
          <td class="l">Sede (*) </td>
          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(tipoUfficioPmSede) %></font></td>
          <% } else { %>
          <td class="l"> &nbsp; </td>
          <td class="l"> &nbsp; </td>
          <% } %>
          <td class="l"> &nbsp; </td>
          <td class="l"> &nbsp; </td>
        </tr>
       
        <tr>
		    <td class="Titolo" colspan="6" height="20px" >   </td>
		</tr>
		<tr>
		  <% if (misuracautelare!=null  && (misuracautelare.getAnnoFascBdmc()!=null || misuracautelare.getNumeFascBdmc()!=null))  {%>
          <td class="L"> Anno/Numero B.D.M.C. </td>
          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getAnnoFascBdmc()+"/"+StringUtils.toStringJSP(misuracautelare.getNumeFascBdmc()))%> </font></td>
          <% } else { %>
          <td class="l"> &nbsp; </td>
          <td class="l"> &nbsp; </td>
          <% } %>
          <% if (misuracautelare!=null  && (misuracautelare.getAnnoRegGen()!=null || misuracautelare.getNumeroRegGen()!=null))  {%>
          <td class="L"> Anno/Numero Reg. Gen. </td>
          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getAnnoRegGen()+"/"+StringUtils.toStringJSP(misuracautelare.getNumeroRegGen()))%> </font></td>        	     		
      	  <% } else { %>
          <td class="l"> &nbsp; </td>
          <td class="l"> &nbsp; </td>
          <% } %>
          <% if (misuracautelare!=null  && (misuracautelare.getTipoUfficioRegGen()!=null && !misuracautelare.getTipoUfficioRegGen().equals("-")))  {%>
      	  <td class="l">Tipo ufficio Reg. Gen.</td>
       	  <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getTipoUfficioRegGen()) %></font></td>
       	  <% } else { %>
          <td class="l"> &nbsp; </td>
          <td class="l"> &nbsp; </td>
          <% } %>
        </tr>
        <% if (misuracautelare!=null  && (AutoritaEmittenteDesc!=null || misuracautelare.getAutoritaEmittenteLuogoDesc()!=null))  {%>
      	<tr>	     		
      	    <% if (AutoritaEmittenteDesc!=null && !AutoritaEmittenteDesc.equals(""))  {%>
      		<td class="l">Autorità Emittente (*)</td>
        	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(AutoritaEmittenteDesc) %></font></td>
        	<% } else { %>
            <td class="l"> &nbsp; </td>
            <td class="l"> &nbsp; </td>
            <% } %>
        	<% if (misuracautelare!=null && misuracautelare.getAutoritaEmittenteLuogoDesc()!=null && !misuracautelare.getAutoritaEmittenteLuogoDesc().equals(""))  {%>
          	<td class="l">Luogo</td>
          	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getAutoritaEmittenteLuogoDesc()) %></font></td>  
          	<% } else { %>
            <td class="l"> &nbsp; </td>
            <td class="l"> &nbsp; </td>
            <% } %>
         	<td class="l"> &nbsp; </td>
         	<td class="l"> &nbsp; </td>
         </tr> 
         <% } %>
         <% if (misuracautelare!=null  && misuracautelare.getDataEmissioneOrdinanza()!=null)  {%>
         <tr>    	
          	<td class="l">Data emissione Ordinanza</td>
	        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataEmissioneOrdinanza(),"dd-MM-yyyy"))%> </font></td>     
      		<td class="l"> &nbsp; </td>
      		<td class="l"> &nbsp; </td>
      		<td class="l"> &nbsp; </td>
      		<td class="l"> &nbsp; </td>
      	 </tr>
      	 <% }  %>
      	 <tr>
		    <td class="Titolo" colspan="6" height="20px" >   </td>
		 </tr>
		 <tr>
			<td class="l">Tipo Misura</td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getDescrTipoMisura()) %></font></td>
			<td class="l" width="10px">  </td>
      		<td class="l"> &nbsp; </td>
      		<td class="l"> &nbsp; </td>
      		<td class="l"> &nbsp; </td>
		 </tr>
		 <tr>
		    <% if (misuracautelare!=null  && misuracautelare.getDataInizio()!=null)  {%>
			<td class="l">Data Inizio</td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataInizio(),"dd-MM-yyyy"))%> </font></td>
			<% } else { %>
            <td class="l"> &nbsp; </td>
            <td class="l"> &nbsp; </td>
            <% } %>
			<% if (misuracautelare!=null  && misuracautelare.getDataFine()!=null)  {%>
			<td class="l">Data Fine</td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataFine(),"dd-MM-yyyy"))%> </font></td>
			<% } else { %>
            <td class="l"> &nbsp; </td>
            <td class="l"> &nbsp; </td>
            <% } %>
		    <td class="l"> &nbsp; </td>
      		<td class="l"> &nbsp; </td>
		 </tr>
      	<%
      	// nuova infrastruttura: aggiunto controllo preventivo
      	String ctm = misuracautelare.getCodTipoMisura();
      	if (!("AD".equals(ctm) || "CL".equals(ctm) || "CB".equals(ctm) || "CC".equals(ctm) || "CE".equals(ctm))) {
			if (misuracautelare.getAutoritaCompetente()!=null &&
	      		!misuracautelare.getAutoritaCompetente().equalsIgnoreCase("") &&
	      		!misuracautelare.getAutoritaCompetente().equalsIgnoreCase("-")) {
			%>
	        <tr>
		        <td class="l">Istituto di Detenzione</td>
		        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getAutoritaCompetenteDesc(),"-") +" - "+StringUtils.toStringJSP(misuracautelare.getAutoritaCompetenteSedeDesc(),"-")%></font></td>
		        <td class="l"> &nbsp; </td>
		        <td class="l"> &nbsp; </td>
		        <td class="l"> &nbsp; </td>
		        <td class="l"> &nbsp; </td>
	        </tr>
	      	<%
	      	} else if (misuracautelare.getIstDetIdIstitutoDetenzione()!=null &&
	      		!misuracautelare.getIstDetIdIstitutoDetenzione().equalsIgnoreCase("")) {
	      	%>
	      	<tr>
		      	<td class="l">Istituto di Detenzione</td>
		      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getIstitutoDetenzione().getDescrTipoIstituto(),"-") +" - "+StringUtils.toStringJSP(misuracautelare.getIstitutoDetenzione().getDescrComune(),"-")%></font></td>     
		        <td class="l"> &nbsp; </td>
		      	<td class="l"> &nbsp; </td>
		      	<td class="l"> &nbsp; </td>
		      	<td class="l"> &nbsp; </td>
	      	</tr>
	      	<% } else { %>
	        <tr>
		        <td class="l">Istituto di Detenzione</td>
		      	<td class="l" align="center"><font class="campo"><%=StringUtils.toStringJSP("- - -")%></font></td>
		      	<td class="l"> &nbsp; </td>
		      	<td class="l"> &nbsp; </td>
		      	<td class="l"> &nbsp; </td>
		      	<td class="l"> &nbsp; </td>
	        </tr>
	      	<% }
      	} %>
		<% if(misuracautelare.getAltroLuogoDetenzione()!= null && !misuracautelare.getAltroLuogoDetenzione().equals("")) {%>
         <tr>
			 <td class="l">Luogo Espiazione</td>
			 <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getAltroLuogoDetenzione()) %></font></td>
             <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
		</tr>
		<%}%>
		<% if(misuracautelare.getAutoritaCompetente()!= null && !misuracautelare.getAutoritaCompetente().equals("") && !misuracautelare.getAutoritaCompetente().equals("-")) {%>
         <tr>
			 <td class="l">Autorità Competente per territorio</td>
			 <td class="l"><font class="campo"><%=StringUtils.toStringJSP(autoritaCompTerritorio) %></font></td>
             <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
		</tr>
		<%}%>
		<% if(misuracautelare.getAutoritaCompetenteSedeDesc()!= null && !misuracautelare.getAutoritaCompetenteSedeDesc().equals("")) {%>
        <tr>
			 <td class="l">Sede</td>
			 <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getAutoritaCompetenteSedeDesc()) %></font></td>
			 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>             
      		 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
	   </tr>
	   <%}%>
	   <% if(misuracautelare.getAutoritaCompetenteIndirizzo()!= null && !misuracautelare.getAutoritaCompetenteIndirizzo().equals("")) {%>
	   <tr>			 
      		 <td class="l">Indirizzo</td>
			 <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getAutoritaCompetenteIndirizzo()) %></font></td>
             <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
	   </tr>
	   <%}%>
	   <%if(computabilità.equals("N")){%>
	   <% if(misuracautelare.getDescrMotivoNonComputabile()!= null && !misuracautelare.getDescrMotivoNonComputabile().equals("") && !misuracautelare.getDescrMotivoNonComputabile().equals("-")) {%>
	   <tr>
	   		 <td class="l">Motivo non Computabilità</td>
			 <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getDescrMotivoNonComputabile()) %></font></td>
			 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
	   </tr>
	   <%}%>
	   
	   <tr>
	   	     <% if(misuracautelare.getDescrTipoUfficioRifer()!= null && !misuracautelare.getDescrTipoUfficioRifer().equals("") && !misuracautelare.getDescrTipoUfficioRifer().equals("-")) {%>
			 <td class="l">Ufficio</td>
			 <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getDescrTipoUfficioRifer()) %></font></td>
			 <%} else {%> 
			 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
			 <%}%> 
			 <% if(misuracautelare.getDescrLuogoUfficioRifer()!= null && !misuracautelare.getDescrLuogoUfficioRifer().equals("") && !misuracautelare.getDescrLuogoUfficioRifer().equals("-")) {%>
			 <td class="l">Luogo Ufficio</td>
			 <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getDescrLuogoUfficioRifer()) %></font></td>
			 <%} else {%> 
			 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
			 <%}%> 
			 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
	   </tr>

	   <% if(misuracautelare.getDataFungibilita()!= null && !misuracautelare.getDataFungibilita().equals("")) {%>
	   <tr>
			 <td class="l">Data provvedimento di Fungibilita'</td>
			 <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataFungibilita(),"dd-MM-yyyy")) %></font></td>
			 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
      		 <td class="l"> &nbsp; </td>
	   </tr>
	   <%}%>
	   <% if (misuracautelare!=null  && (misuracautelare.getAnnoRifer()!=null || misuracautelare.getNumRifer()!=null))  {%>	   		
      	<tr>
          <td class="L"> Siep Anno/Numero  </td>
          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getAnnoRifer()+"/"+StringUtils.toStringJSP(misuracautelare.getNumRifer()))%> </font></td>     
          <td class="l"> &nbsp; </td>
          <td class="l"> &nbsp; </td>
          <td class="l"> &nbsp; </td>
          <td class="l"> &nbsp; </td>
        </tr>
        <% }  %>       
	    <% if(misuracautelare.getNote()!= null && !misuracautelare.getNote().equals("")) {%>
	    <tr>
		  <td class="l">Note</td>
		  <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelare.getNote()) %></font></td>
		  <td class="l"> &nbsp; </td>
      	  <td class="l"> &nbsp; </td>
      	  <td class="l"> &nbsp; </td>
      	  <td class="l"> &nbsp; </td>
	    </tr>
	    <%}%>
	<%} %>
	</table>

     <%
    // richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena 
    // paolo cherubini lunedi 11/10/2010

     if (lPenaResMod!=null  && lPenaResMod.getFlagValidato().equals("N"))  {%>
    <FORM name="calcolopena" >   
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActLoadCalcoloPena">
	<table>
	<tr>
		<td class="lRosso">
			<font class="lRosso">
				Attenzione! Primo calcolo della pena già effettuato. Per computare le misure modificate,
				è necessario procedere nuovamente con il calcolo della pena
			</font>
		</td>
	</tr>
	 <tr>
       <td>
        <INPUT  class="bottone" type="submit" name="CALCOLA" value="Calcola Fine Pena" onClick="">
       </td>
     </tr>
	</table>
	</FORM>
 <%   }
    // fine a9/rr/075 
    
 %>

    </body>
</html>