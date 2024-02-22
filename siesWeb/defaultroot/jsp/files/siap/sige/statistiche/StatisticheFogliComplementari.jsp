<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Vector"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.statistiche.model.RicercaFogliCompModel" %>
<%@ page import="siap.sige.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.sige.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.sige.statistiche.model.StatisticheFogliComplementariModel"%>

<jsp:useBean id="Provvedimenti" scope="request" class="java.util.Vector" />
<jsp:useBean id="FiltroRicerca"  scope="session" class="siap.sige.statistiche.model.RicercaFogliCompModel" />
<jsp:useBean id="TornaQui"      scope="request" class="java.lang.String"/>

<html>
<%
   // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
 
%>

  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Procedimenti per estremi Foglio Complementare</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
 </head>

  <BODY class="corpo">

  <table >
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"> <font class=label>Funzione:</font>&nbsp; <font class="campo">Elenco Fogli Complementari</font> </td>
     <!-- BOTTONE DI STAMPA  -->
     <td class="LBG">
        <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sige.statistiche.action.ActExportStatisticheFogliComplementariInExcel&stampa=Si&TornaQui=<%=TornaQui%>')">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>print24.gif" alt="Stampa" width="24" height="24" border="0">
          </a>
     </td>
    
    <!-- NUOVO BOTTONE PER STAMPA EXCEL --> 
	<td class="LBG">
		<a class="cliccabile" href="javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STATISTICA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sige.statistiche.action.ActExportStatisticheFogliComplementariInExcel')"><img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0"></a>
	</td>

    <!-- BOTTONE DI RITORNO -->
    <td class="LBG">
      <a href="javascript:history.go(-1);">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>
    </td>

    </tr>
  </table>


  <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="lNoBord"><font class="label">Criteri di Ricerca selezionati:</font></td>
      </tr>
      
    <%
    String lCriterio1 = "";
    String lCriterio2 = "";
    
    if (FiltroRicerca.getAnnoIniziale() != null) {
    	if (FiltroRicerca.getAnnoFinale()==null) {
    		lCriterio1="Anno: " + FiltroRicerca.getAnnoIniziale();
    	}
    	
    	if (FiltroRicerca.getAnnoFinale() != null) {
    		lCriterio1="Dall'Anno: " + FiltroRicerca.getAnnoIniziale() + " all'anno " + FiltroRicerca.getAnnoFinale ();
    	}
    }
    
    if (FiltroRicerca.getDataEmissioneIniziale() != null) {
    	if (FiltroRicerca.getDataEmissioneFinale() == null) {
    	    lCriterio2 = "Data Compilazione: " + DateUtils.getDateToString(FiltroRicerca.getDataEmissioneIniziale(), "dd-MM-yyyy");
    	}
    	
    	if (FiltroRicerca.getDataEmissioneFinale() != null) {
    	    lCriterio2 = "Data Compilazione dal " + DateUtils.getDateToString(FiltroRicerca.getDataEmissioneIniziale(), "dd-MM-yyyy") + " al " + DateUtils.getDateToString(FiltroRicerca.getDataEmissioneFinale(), "dd-MM-yyyy");
    	}
    }
    
    %>    

     <tr>
     	<td class="lVerdeNB"><%=lCriterio1%> </td>
     </tr>
     <tr>
		<td class="lVerdeNB"><%=lCriterio2%> </td>
	 </tr>

<%-- Ticket#20230202011 - Aggiunto dettaglio delle tipologie --%>
     <tr>
		<td class="lVerdeNB">Tipologia foglio Complementare: </td>
	 </tr>	 

     <tr>
		<td class="lVerdeNB">
			<ul>
				<% if (FiltroRicerca.isFcTrasmessi()) { %>
					<li>Provvedimenti con Fogli Complementari</li>
				<% } %>
				<% if (FiltroRicerca.isFcIscrittiManualmente()) { %>
					<li>Fogli Complementari Iscritti Manulamente o con altre opzioni</li>
				<% } %>
				<% if (FiltroRicerca.isProvvedimentiPriviFC()) { %>
					<li>Provvedimenti Privi di Fogli Complementari</li>
				<% } %>
				<% if (FiltroRicerca.isFcAnnullati()) { %>
					<li>Fogli complementari annullati</li>
				<% } %>
			</ul>
		</td> 
 	 </tr>	
<%-- Ticket#20230202011 - FINE --%>

     <tr>
 		<td class="lNoBord"><font class="label"> <%=FiltroRicerca.getDescCalcoli() %></font></td>
 	 </tr>	 
  </table>

  <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

 <table cellspacing=2 cellpadding=2 width=95%>
    <tr>
      <td class="int" width=15%>Numero SIGE</td>
      <td class="int" width=15%>Data Provvedimento</td>
      <td class="int" width=40%>Provvedimento</td>
      <td class="int" width=15%>Data Foglio Complementare</td>
      <td class="int" width=15%>Esito</td>
    </tr>
    <%
    Iterator <StatisticheFogliComplementariModel>itProvv=Provvedimenti.iterator();
    BigDecimal lastIdFascSIGE = null;
    String lastAnnoNumeroFC = null;
    while (itProvv.hasNext()) {
    	StatisticheFogliComplementariModel model=itProvv.next();
    	
    	String fascicoloSiep=model.getDescrFascicolo();
    	BigDecimal idFascicolo=model.getIdFascicolo();
    	String dataProvvedimento = DateUtils.getDateToString(model.getDataProvvedimento(), "dd-MM-yyyy");
    	String dataFoglioComplementare=model.getDataFoglioComplementare();
    	String descrProvvedimento=model.getDescrProvvedimento();
    	String esito=model.getDescrEsito();
    	
    	// Ticket#20210514016 - gestione righe multiple
    	if (lastIdFascSIGE!=null && model.getIdFascicolo().compareTo(lastIdFascSIGE)==0) {
    		idFascicolo = null;
    		dataProvvedimento = "";
    		dataFoglioComplementare ="";
    		esito = "";
    	}
    	else {
    		lastIdFascSIGE = model.getIdFascicolo();
    	}
    	// Ticket#20210514016 - Fine
    	
    	// Ticket#20230202011 - Gestita la visualizzazione in caso di + fogli complementari sullo stesso
    	//                      provvedimento (uno valido e N annullati. Visualizzava solo il primo annullato)
    	if (   lastAnnoNumeroFC!=null && model.getAnnoNumeroFoglioComplementare()!=null 
    	    && !lastAnnoNumeroFC.equals(model.getAnnoNumeroFoglioComplementare())
    	   )
    	{
    		dataFoglioComplementare = model.getDataFoglioComplementare();
    	 	esito = model.getDescrEsito();
    	}
    	lastAnnoNumeroFC = model.getAnnoNumeroFoglioComplementare();
    	// Ticket#20230202011 - FINE
   %>
   
   <tr>
    <td class="c">
       	<% if (idFascicolo!=null) { %>
    	<font class="label">
      	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=idFascicolo%><%=retParam%>">
      	<%=fascicoloSiep%>
      	</a>
      	</font>
		<% } else { %>
		
		<% } %>
    </td>
    <td class="c"><font class="label"><%=dataProvvedimento %></font></td>
    <td class="l"><font class="label"><%=descrProvvedimento %></font></td>
    <td class="c"><font class="label"><%=dataFoglioComplementare %></font></td>
    <td class="c"><font class="label"><%=esito %></font></td>
   </tr>
    	
   <%}
    %>
    </table>
  <br>
  </body>
</html>