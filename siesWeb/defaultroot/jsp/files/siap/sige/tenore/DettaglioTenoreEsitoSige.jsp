<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeEstesoModel"%>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiDecodifiche"%>
<%@ page import="siap.sige.datiprovsige.model.DatiProvvedimentoSigeModel"%>


<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="titolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="dati_prov" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_esito" scope="request" class="java.lang.String"/>
<jsp:useBean id="tenori" scope="request" class="java.util.Vector"/>
<jsp:useBean id="FlagIndulto" scope="request" class="java.lang.String"/>
<jsp:useBean id="isTitoliEsecutivi" scope="request" class="java.lang.String"/>

<% 

	boolean lEsitoUnico = false;
	
	if (tipo_esito.compareTo("unico")== 0) 
		lEsitoUnico = true;

	// Passaggio del titolo
	String lTitolo = "Dettaglio Oggetto";
	if (titolo != null && titolo.length() > 0)
		lTitolo = titolo;
	
	String lTitoloTipoEsito = "Esito differenziato per Sentenza/Reato";
	if(lEsitoUnico) {
		lTitoloTipoEsito = "Esito Unico";
	}	
	
	String idSentenza=request.getParameter ("idSenSentenza");

 %>

<html>
<head>
<title>[S.I.A.P.] - Dettaglio Esiti Tenori Sige </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
</head>

<body class="corpo">
 
    <table>
        <tr>
        	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        	<td class="LBG">
        		<font class="label">Funzione :</font>&nbsp;
        		<font class="campo"><%=lTitolo %></font>
 	 		</td>     
      		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        </tr>
    </table>
 <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
<br>
<% if (FlagIndulto != null && FlagIndulto.length() > 0) {%>
 	<jsp:include page="<%=ICostantiTenoreSige.PG_INCLUDE_DETT_ANNOTAZIONE_MAN%>">
     	<jsp:param name="nome_frame" value="inserisciEsitiTenore"/>
     	</jsp:include>
<%} %>

 <table  width="95%">
      <tr>
          <td class="titolo" > <%=lTitoloTipoEsito%></td>
      </tr>
    </table>

<%if (tenori  != null && tenori.size() > 0 ) { %>

<table  width="95%">
<%


	String lCodOggetto = "";
	String lIdTenore = "";
	String lIdSentenza = "";
	String lDescOggetto = "";
	TenoreSigeEstesoModel  lTenore =(TenoreSigeEstesoModel) tenori.get(0) ;
	if (lEsitoUnico) {
%>
 			<tr> 			
   			<td class="l" >
   			<font class="label">esito  &nbsp;</font><font class="campo">&nbsp; <%=(lTenore.getTenoreSige().decodifica()).getDescrEsitoSige()%></font>
   			</td>			
   			</tr>
<%		
	}
	
	
	
	   Iterator itx = tenori.iterator();
       while ( itx.hasNext())
       {
    	lTenore = (TenoreSigeEstesoModel)itx.next();
    	
	   
	   if (lEsitoUnico && !lTenore.getSentenza().getIdSentenza().toString().equals(idSentenza) && isTitoliEsecutivi.equalsIgnoreCase("false")) { 
		     continue;
	   }     
%>	   
	   <tr>
  			<td class="l" >
  			<font class="label">oggetto  &nbsp;</font><font class="campo">&nbsp;<%=lTenore.getTenoreSige().getDescrOggettoSige()%></font>
  			</td>			
   		</tr>		

	   <tr>
			<td class="l" width=29%>
		
		<%=lTenore.getSentenza().getCellSentenza()%>
		
		</td>
		</tr>
<%		
       if(lTenore.getReato() != null) {
    	   ReatoCircostanzaModel lReatoCircostanza = new ReatoCircostanzaModel(lTenore.getReato());
    	   request.setAttribute("lReatoCircostanza", lReatoCircostanza);
    	   %>
			<tr>
			<td class="l">
		       <jsp:include page="<%=ICostantiDecodifiche.PG_DETTAGLIO_REATO_SIGE%>"/>
			</td>
			</tr>
		 <%  
       }
       if (!lEsitoUnico) { %>		
	<tr> 			
	<td class="l" >
	<font class="label">esito  &nbsp;</font><font class="campo">&nbsp;<%=lTenore.getTenoreSige().decodifica().getDescrEsitoTenSenRea()%></font>
	</td>
	<tr>	
     <%		
     } // endif Tipo Esito Differenziato


      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Tenore -> " + lTenore.getTenoreSige());
  	} // endwhile
 %>
   </table>
   <br>
 	<table style="width: 95%;">
 	  <tr>
          <td class="titolo" > Ulteriore descrizione della decisione </td>
      </tr>
    <tr>
      <td class="l"> <font class="campo"> <%=StringUtils.toStringJSP(lTenore.getTenoreSige().getNote())%>  </font></td>
    </tr>
</table>      
<% 
} // endif tenori.size

if (dati_prov != null && dati_prov.size() > 0) {%>
  <br>
   <table  width="95%">
    <tr>   
    	<td class="Titolo" > Dati particolari  </td> 
    </tr>
	</table>
	<table  width="95%">
<%
	   Iterator itxDati = dati_prov.iterator();
       while ( itxDati.hasNext())
       {
    	   DatiProvvedimentoSigeModel  lDato = (DatiProvvedimentoSigeModel)itxDati.next();
%>
<tr>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lDato.getDescrTipoDatiProv())%> </font></td>
</tr>
<%
       } // endwhile
 %>
 </table>
 <%} // endif dati %>
 
 </body>
</html>