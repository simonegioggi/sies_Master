<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="siap.sico.webservice.model.OmonimiModel" %>
<%@ page import="org.apache.axis.encoding.Base64" %>

<jsp:useBean id="listaOmonimi"         scope="request" class="java.util.Vector" />
<jsp:useBean id="soggetto"             scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="tipologiaCertificato" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoFascicolo"        scope="request" class="java.lang.String" />
<jsp:useBean id="evento"               scope="request" class="siap.sico.evento.model.EventoModel"/>

<%
	// Scorro la lista degli omonimi/sinonimi per vedere se il soggetto è stato trovato
    Iterator itx = listaOmonimi.iterator();
	String soggettoTrovato = "N";
	String presenzaSoggetto = "";
	String presenzaSinonimi = "N";
	while ( itx.hasNext()) {
    	OmonimiModel lOmonimo = (OmonimiModel)itx.next();
		// soggetto trovato
    	if(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getFLAGOMONINOSINONIMO() != null && lOmonimo.getOMONIMO().getDATIANAGRAFICI().getFLAGOMONINOSINONIMO().equals("T")){
    		soggettoTrovato = "S";
    	}
    	// presenza sinonimi
    	if(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getFLAGOMONINOSINONIMO() != null && lOmonimo.getOMONIMO().getDATIANAGRAFICI().getFLAGOMONINOSINONIMO().equals("S")){
    		presenzaSinonimi = "S";
    	}
    	// presenza omonimi
		if(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getFLAGOMONINOSINONIMO() != null && lOmonimo.getOMONIMO().getDATIANAGRAFICI().getFLAGOMONINOSINONIMO().equals("O")){
			soggettoTrovato = "O";
    	}
	}
	
	if(soggettoTrovato.equals("S") || soggettoTrovato.equals("O")){
		presenzaSoggetto = "Elenco Soggetti Trovati";
	} else {
		presenzaSoggetto = "Soggetto Non Trovato";
	}
	
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Risultato Richiesta Certificato Penale</title>
	<script language="JavaScript"> 
		function Verify(){
		    var selezionato = false;
			for (var i = 0; i< document.getElementsByName('progAnagraficaNSC').length; i++) {
				if(document.loadRisultato["progAnagraficaNSC"][i].checked==true){          
			   		selezionato = true;
			    }
	    	}
			
			if(!selezionato){
				alert("Selezionare un Soggetto dall'elenco oppure il Certificato Nullo");
	            return false;
			}
			
			return true;
		}
	</script>
  </head>

  <BODY class="corpo">
  
  <FORM method="POST" name="loadRisultato" action="<%=IWebConstants.PG_MAIN%>">

  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.webservice.action.ActPrelevaDatiRichiestaCertificato" >
    <input type="HIDDEN" name="tipoFascicolo" value="<%=tipoFascicolo%>" >
    <input type="HIDDEN" name="idEvento" value="<%=evento.getIdEvento()%>" >
  
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
       <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Risultato Richiesta Certificato</font></td>
    </tr>
  </table>

  <br>

  <table cellspacing="2" cellpadding="2">
    <tr>
       <td class="L" width=14><font class="label">Certificato: </font></td>
       <td class="L"><font class="campo">CASELLARIO GIUDIZIALE</font></td>
       <td class="L"><font class="label">Tipologia Certificato: </font></td>
       <td class="L"><font class="campo"><%=tipologiaCertificato%></font> </td>
    </tr>
  </table>

  <br>

  <table cellspacing="2" cellpadding="2">
    <tr>
       <td class="L"><font class="label">Cognome: </font></td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCognome())%></font></td>
       <td class="L"><font class="label">Nome: </font></td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNome())%></font> </td>
       <td class="L"><font class="label">Sesso: </font></td>
       <td class="L"><font class="campo">
	   <% if(soggetto.getSesso() != null && soggetto.getSesso().equals("M")){ %>       
       	  	Maschile
       <% } else { %>	  	
       	  	Femminile
       <% } %>
       	  	</font></td>
    </tr>
    <tr>
       <td class="L"><font class="label">Data di Nascita: </font></td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font></td>
       <td class="L"><font class="label">Luogo di Nascita: </font></td>
       <td class="L"><font class="campo">
       <% if (soggetto.getDescrComuneNascita() != null && !soggetto.getDescrComuneNascita().equals("-")){ %>
       		<%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita())%>
       <% } %></font></td>
       <td class="L"><font class="label">Nazione di Nascita: </font></td>
       <td class="L"><font class="campo">
       <% if (soggetto.getDescrNazionalita() != null && !soggetto.getDescrNazionalita().equals("-")){ %>
       		<%=StringUtils.toStringJSP(soggetto.getDescrNazionalita())%>
       <% } %></font></td>	
    </tr>
    <tr>
       <td class="L"><font class="label">Codice Fiscale: </font></td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodFiscale())%></font></td>
       <td class="L"><font class="label">Paternità: </font></td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getPaternita())%></font></td>
       <td class="L"><font class="label">Codice Identificativo: </font></td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodAfis())%></font></td>
    </tr>
  </table>

  <br>
  
  <table cellspacing="2" cellpadding="2">
    <tr>
       <td class="Titolo" colspan="8"><%=presenzaSoggetto%></td>
    </tr>
    <tr>
       <td class="int">&nbsp;</td>
       <td class="int">Cognome</td>
       <td class="int">Nome</td>
       <td class="int">Luogo Nascita</td>
       <td class="int">Data Nascita</td>
       <td class="int">Sesso</td>
       <td class="int">Paternità</td>
       <td class="int">CF/Cod. Identificativo</td>
    </tr>
<%  if (soggettoTrovato.equals("N")){ %>
    <!-- SOGGETTO NON TROVATO -->
    <tr>
       <td class="L">&nbsp;</td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCognome())%></font></td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNome())%></font></td>
       <td class="L"><font class="campo">
       <% if(soggetto.getDescrComuneNascita() != null && !soggetto.getDescrComuneNascita().equals("-") ) {%>
       		<%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita())%>
       <% } %></font></td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font></td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getSesso())%></font></td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getPaternita())%></font></td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodFiscale())%>/<%=StringUtils.toStringJSP(soggetto.getCodAfis())%></font></td>
    </tr>

    <tr>
       <td class="c"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>stop.gif" alt="Attenzione" width="24" height="24" border="0"></td>
       <td class="c" colspan="7"><font class="label">Attenzione! Il soggetto non è presente in archivio; è possibile effettuare la stampa del certificato nullo selezionando la seguente opzione:</font></td>
    </tr>
    <tr>
       <td class="c"><input type="radio" name="progAnagraficaNSC" value="-1" ></td>
       <td class="c"><font class="label">Certificato Nullo</font></td>
       <td class="c" colspan="7">&nbsp;</td>
    </tr>

<% 
	} else { %>
    <!-- SOGGETTO TROVATO -->
<%
	itx = listaOmonimi.iterator();	
	String lDataNascitaVis="";  
    while ( itx.hasNext()) {
    	OmonimiModel lOmonimo = (OmonimiModel)itx.next();
		// presenza soggetto ed eventuali omonimi
    	if(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getFLAGOMONINOSINONIMO() != null && 
    	   (lOmonimo.getOMONIMO().getDATIANAGRAFICI().getFLAGOMONINOSINONIMO().equals("T") ||
    	    lOmonimo.getOMONIMO().getDATIANAGRAFICI().getFLAGOMONINOSINONIMO().equals("O"))		   
        ){
%>
	<!-- Dati necessari per richiedere il certificato in caso di sinonimi -->
	<input type="hidden" name="cognome_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSCOGNOME()%>" >       
	<input type="hidden" name="nome_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSNOME()%>" >       
	<input type="hidden" name="codLuogoNascita_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODILUOGONASCITA()%>" >       
	<input type="hidden" name="codEsteroNascita_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODISTATOESTERONAS()%>" >
	<input type="hidden" name="descEsteroNascita_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDESCCOMUNEESTERO()%>" >
	<input type="hidden" name="dataNascitaGG_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().getGIORNO()%>" >
	<input type="hidden" name="dataNascitaMM_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().getMESE()%>" >
	<input type="hidden" name="dataNascitaAAAA_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().getANNO()%>" >
	<input type="hidden" name="sesso_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getSESSO()%>" >
	<input type="hidden" name="paternita_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSPATERNITA()%>" >
	<input type="hidden" name="codFiscale_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIFISCALE()%>" >
	<input type="hidden" name="cognomeMadre_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSCOGNOMEMADRE()%>" >
	<input type="hidden" name="nomeMadre_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSNOMEMADRE()%>" >
	<input type="hidden" name="codiImpronta_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIIMPRONTADIGITALE()%>" >
	
    <tr>
       <td class="c"><input type="radio" name="progAnagraficaNSC" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" ></td>
       <td class="c"><%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSCOGNOME()%></td>
       <td class="c"><%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSNOME()%></td>

      <% if (lOmonimo.getDescLuogoNascita() == null || lOmonimo.getDescLuogoNascita().equals("")){
      %>
            <%if(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDESCCOMUNEESTERO() != null && !lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDESCCOMUNEESTERO().equals("")){
            %>
            	<td class="c">
        		<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDESCCOMUNEESTERO()%>&nbsp;
        	    <%if(lOmonimo.getDescStatoEstero() != null && !lOmonimo.getDescStatoEstero().equals("")) 
              	  {%>
        		  	(<%=lOmonimo.getDescStatoEstero().toUpperCase()%>)		
        		<%} %>
        	<%} else { %>
        		<td class="c">&nbsp;
        	<%} %>	
        	&nbsp; 
        </td>
      <% 
      	}else {
      %>
		<td class="c">
       	<%=lOmonimo.getDescLuogoNascita()%> (<%=lOmonimo.getProvNascita()%>)&nbsp;</td> 
      <%}%>

<%     
	   if (lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA() == null || lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().equals("")) { %>     
			<td class="c">
	   		- 
<%     }else {
          String lAnno, lMese, lGiorno;
          Date lDataNascita;
          lAnno = lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().getANNO();
          lMese = lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().getMESE();
          lGiorno = lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().getGIORNO();
          lDataNascita = DateUtils.getDate(lAnno,lMese, lGiorno);
%>
			<td class="c">   
          <%=DateUtils.getDateToString(lDataNascita,"dd-MM-yyyy")%>
<%     }%>

       </td>

	   <td class="c"><%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getSESSO()%></td>
       
      <% if (lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSPATERNITA() != null && !lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSPATERNITA().equals("")) { %>
      		<td class="c">
      		<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSPATERNITA()%></td>
      <% } else {%>		
      		<td class="c">&nbsp;</td>
      <% } %>

	  <td class="c">
	      <% if (lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIFISCALE() != null && !lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIFISCALE().equals("")) {%>
	      		<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIFISCALE()%>
		  <% } else if (lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIIMPRONTADIGITALE() != null && !lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIIMPRONTADIGITALE().equals("")) {%>      
	            / <td class="c"><%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIIMPRONTADIGITALE()%></td>
	      <% } else {%>		
	      		<td>&nbsp;</td>
	      <% } %>
	  </td>
    </tr>
<%  
		}	
	}
 } 
%>  
  
  </table>  
  <br>

  <!-- ELENCO SINONIMI -->
  <table cellspacing="2" cellpadding="2">
    <tr>
       <td class="Titolo" colspan="8">Elenco Sinonimi (in arancione sono evidenziate le differenze rispetto al soggetto richiesto)</td>
    </tr>
<%
  if(presenzaSinonimi.equals("S")){
%>
    <tr>
       <td class="int"></td>
       <td class="int">Cognome</td>
       <td class="int">Nome</td>
       <td class="int">Luogo Nascita</td>
       <td class="int">Data Nascita</td>
       <td class="int">Sesso</td>
       <td class="int">Paternità</td>
       <td class="int">CF/Cod. Identificativo</td>
    </tr>
    
<%
	String lDataNascitaVis="";
	itx = listaOmonimi.iterator();
    while ( itx.hasNext()) {
    	OmonimiModel lOmonimo = (OmonimiModel)itx.next();
		// presenza sinonimo
    	if(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getFLAGOMONINOSINONIMO() != null && lOmonimo.getOMONIMO().getDATIANAGRAFICI().getFLAGOMONINOSINONIMO().equals("S")){
%>
	<!-- Dati necessari per richiedere il certificato in caso di sinonimi -->
	<input type="hidden" name="cognome_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSCOGNOME()%>" >       
	<input type="hidden" name="nome_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSNOME()%>" >       
	<input type="hidden" name="codLuogoNascita_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODILUOGONASCITA()%>" >       
	<input type="hidden" name="codEsteroNascita_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODISTATOESTERONAS()%>" >
	<input type="hidden" name="descEsteroNascita_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDESCCOMUNEESTERO()%>" >
	<input type="hidden" name="dataNascitaGG_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().getGIORNO()%>" >
	<input type="hidden" name="dataNascitaMM_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().getMESE()%>" >
	<input type="hidden" name="dataNascitaAAAA_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().getANNO()%>" >
	<input type="hidden" name="sesso_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getSESSO()%>" >
	<input type="hidden" name="paternita_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSPATERNITA()%>" >
	<input type="hidden" name="codFiscale_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIFISCALE()%>" >
	<input type="hidden" name="cognomeMadre_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSCOGNOMEMADRE()%>" >
	<input type="hidden" name="nomeMadre_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSNOMEMADRE()%>" >
	<input type="hidden" name="codiImpronta_<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIIMPRONTADIGITALE()%>" >

    <tr>
       <td class="c"><input type="radio" name="progAnagraficaNSC" value="<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPROGANAGRAFICA()%>" ></td>
<%
		if(soggetto.getCognome().equals(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSCOGNOME())){
%>       
         	<td class="c">
  <%    } else { %>	 
         	<td class="orange">
  <%    } %>

       <%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSCOGNOME()%></td>

<%
		if(soggetto.getNome().equals(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSNOME())){
%>       
         	<td class="c">
  <%    } else { %>	 
         	<td class="orange">
  <%    } %>
       
       <%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSNOME()%></td>

      <% if (lOmonimo.getDescLuogoNascita() == null || lOmonimo.getDescLuogoNascita().equals("")){
      %>
            <%if(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDESCCOMUNEESTERO() != null && !lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDESCCOMUNEESTERO().equals("")){
            	  if(soggetto.getDescrComuneNascita().equals(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDESCCOMUNEESTERO())){
            %>
            	  <td class="c">
            <%    } else { %>	 
            	  <td class="orange">
            <%    } %>
        		  <%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDESCCOMUNEESTERO()%>&nbsp;
        	    <%if(lOmonimo.getDescStatoEstero() != null && !lOmonimo.getDescStatoEstero().equals("")) 
              	  {%>
        		  	(<%=lOmonimo.getDescStatoEstero().toUpperCase()%>)		
        		<%} %>
        	<%} else {%>
        		<td class="c">&nbsp;
        	<%} %>	
        	&nbsp; 
        </td>
      <% 
      	}else {
            if(!soggetto.getDescrComuneNascita().equalsIgnoreCase(lOmonimo.getDescLuogoNascita())){
      %>
				<td class="orange">
<%          } else { %>
       			<td class="c">
<%          } %>       	
       	<%=lOmonimo.getDescLuogoNascita()%> (<%=lOmonimo.getProvNascita()%>)&nbsp;</td> 
      <%}%>

<%     
	   if (lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA() == null || lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().equals("")) { %>     
			<td class="c">
	   		- 
<%     }else {
          String lAnno, lMese, lGiorno;
          Date lDataNascita;
          lAnno = lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().getANNO();
          lMese = lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().getMESE();
          lGiorno = lOmonimo.getOMONIMO().getDATIANAGRAFICI().getDATANASCITA().getGIORNO();
          lDataNascita = DateUtils.getDate(lAnno,lMese, lGiorno);

			if( DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy").equals(DateUtils.getDateToString(lDataNascita,"dd-MM-yyyy")) ){
%>
				<td class="c">   
<%
			} else {
%>				       
				<td class="orange">
<%          } %>
          <%=DateUtils.getDateToString(lDataNascita,"dd-MM-yyyy")%>
<%     }%>

       </td>

<%
		if(soggetto.getSesso() != null && soggetto.getSesso().equals(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getSESSO().toString())){
%>       
         	<td class="c">
  <%    } else { %>	 
         	<td class="orange">
  <%    } %>
      <%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getSESSO()%></td>

      <% if (lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSPATERNITA() != null && !lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSPATERNITA().equals("")) { 

			if(soggetto.getPaternita() != null && soggetto.getPaternita().equals(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSPATERNITA())){
	  %>      
	         	<td class="c">
	  <%    } else { %>	 
	         	<td class="orange">
	  <%    } %>

      		<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getPERSPATERNITA()%></td>
      <% } else {%>		
      		<td class="c">&nbsp;</td>
      <% } %>

	  
	      <% if (lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIFISCALE() != null && !lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIFISCALE().equals("")) {
				if(soggetto.getCodFiscale() != null && soggetto.getCodFiscale().equals(lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIFISCALE())){
%>       
         			<td class="c">
<%  			} else { %>	 
         			<td class="orange">
<%    			} %>

	      		<%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIFISCALE()%>

		  <% } else if (lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIIMPRONTADIGITALE() != null && !lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIIMPRONTADIGITALE().equals("")) {%>      
	            / <td class="c"><%=lOmonimo.getOMONIMO().getDATIANAGRAFICI().getCODIIMPRONTADIGITALE()%>
	      <% } else {%>		
	      		<td class="c">&nbsp;
	      <% } %>
	  </td>
    </tr>
<%  
		}	
	}
} else {
%>
    <tr>
       <td class="lNoBord" colspan="8">Sinonimi non trovati</td>
    </tr>
<%  
}
%>      
  </table>

  <br>

  <table cellspacing="2" cellpadding="2">
   <tr>
       <td class="lNoBord" colspan="2">
       		<br><br><INPUT class="bottone" type="submit" name="I" value="Richiesta Certificato" onClick="javascript:return Verify();">
       </td>
   </tr>
  </table>
  
  </FORM>
  </body>
</html>