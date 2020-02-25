<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Vector"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.statistiche.model.RicercaFogliCompModel" %>
<%@ page import="siap.siep.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.siep.statistiche.model.StatisticheFogliComplementariModel"%>

<jsp:useBean id="Provvedimenti" scope="request" class="java.util.Vector" />
<jsp:useBean id="FiltroRicerca"  scope="session" class="siap.siep.statistiche.model.RicercaFogliCompModel" />
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
    <script language="JavaScript" src="/html/gestisciUploadStampa2.js"></script>
 
 </head>

  <BODY class="corpo">

  <table >
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"> <font class=label>Funzione:</font>&nbsp; <font class="campo">Elenco Fogli Complementari</font> </td>
     <!-- BOTTONE DI STAMPA  -->
     <td class=l>
        <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.siep.statistiche.action.ActExportStatisticheFogliComplementariInExcel&stampa=Si&TornaQui=<%=TornaQui%>')">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>print24.gif" alt="Stampa" width="24" height="24" border="0">
          </a>
     </td>
    
    <!-- NUOVO BOTTONE PER STAMPA EXCEL --> 
	<td class=l>
		<a class="cliccabile" href="javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STATISTICA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.siep.statistiche.action.ActExportStatisticheFogliComplementariInExcel')"><img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0"></a>
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
    	    lCriterio2 = "Data Compilazione dal" + DateUtils.getDateToString(FiltroRicerca.getDataEmissioneIniziale(), "dd-MM-yyyy") + " al " + DateUtils.getDateToString(FiltroRicerca.getDataEmissioneFinale(), "dd-MM-yyyy");
    	}
    }
    
    %>    

     <tr>
     	<td class="lVerdeNB"><%=lCriterio1%> </td>
     </tr>
     <tr>
		<td class="lVerdeNB"><%=lCriterio2%> </td>
	 </tr>

     <tr>
 		<td class="lNoBord"><font class="label"> <%=FiltroRicerca.getDescCalcoli() %></font></td>
 	 </tr>	 
  </table>

  <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

 <table cellspacing=2 cellpadding=2 width=95%>
    <tr>
      <td class="int" width=15%>Numero SIEP</td>
      <td class="int" width=15%>Data Provvedimento</td>
      <td class="int" width=40%>Provvedimento</td>
      <td class="int" width=15%>Data Foglio Complementare</td>
      <td class="int" width=15%>Esito</td>
    </tr>
    <%
    Iterator <StatisticheFogliComplementariModel>itProvv=Provvedimenti.iterator();
    while (itProvv.hasNext()) {
    	StatisticheFogliComplementariModel model=itProvv.next();
    	String fascicoloSiep=model.getDescrFascicolo();
    	BigDecimal idFascicolo=model.getIdFascicolo();
    	String dataProvvedimento = DateUtils.getDateToString(model.getDataProvvedimento(), "dd-MM-yyyy");
    	String dataFoglioComplementare=model.getDataFoglioComplementare();
    	String descrProvvedimento=model.getDescrProvvedimento();
    	String esito=model.getDescrEsito();
   %>
   
   <tr>
    </td>
       	<td class="c"><font class="label">
      	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=idFascicolo%><%=retParam%>">
      	<%=fascicoloSiep%>
      	</a>
      	</font>
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