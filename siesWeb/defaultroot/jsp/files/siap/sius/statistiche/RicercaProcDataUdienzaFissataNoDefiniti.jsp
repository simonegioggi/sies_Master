<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.RedirectTo" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche" %>
<%@ page import="siap.sius.statistiche.model.EveFasGepSogProvModel" %>
<%@ page import="siap.sius.statistiche.model.RicercaProcedimentoModel" %>

<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>


<jsp:useBean id="ricercaProcedimenti"   scope="session" class="siap.sius.statistiche.model.RicercaProcedimentoModel" />
<jsp:useBean id="elencoProcedimenti"    scope="request" class="java.util.ArrayList" />
<jsp:useBean id="TornaQui"              scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Procedimenti per Data Udienza Fissata non Definiti </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

  <BODY class="corpo">
	  <table>
	    <tr>
	       <td class="LBG">
	         <a href="Javascript:window.print();">
	         <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
	       </td>
	       <td class="LBG">
	       	 <font class=label>Funzione :</font>&nbsp;
	       	 <font class="campo"> Elenco Procedimenti per Data Udienza fissata non definiti </font>
	       </td>
	       <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	       <!-- NUOVO BOTTONE PER STAMPA EXCEL --> 
					<td class=l>
						<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.statistiche.action.ActRicercaProcDataUdienzaFissataNonDefinitiExcel">
							<img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
						</a>
					</td>
	  	</table>

  <br>
  
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

  <table cellspacing=2 cellpadding=2>
  
<% 	if( ricercaProcedimenti != null ) { %>

      	<tr>
        	<td class="Cliccabile">Criteri di Ricerca selezionati:</td>
      	</tr>
<%
      	if( ricercaProcedimenti.getDataIscrizioneInizio() != null || ricercaProcedimenti.getDataIscrizioneFine() != null ) {
%>
        	<tr>
          	<td class="lVerdeNB">Procedimenti con Data Iscrizione :&nbsp;&nbsp;
<% 					if( ricercaProcedimenti.getDataIscrizioneInizio() != null ) { %>          	
							Dal <%=DateUtils.getDateToString( ricercaProcedimenti.getDataIscrizioneInizio(), "dd-MM-yyyy" )%>&nbsp;&nbsp; 
<% 					} %>

<% 	  			if( ricercaProcedimenti.getDataIscrizioneFine() != null) { %>
            	&nbsp;Al&nbsp;&nbsp;<%=DateUtils.getDateToString( ricercaProcedimenti.getDataIscrizioneFine(),"dd-MM-yyyy")%>
            </td>
<%        	} %>
        	</tr>
<%			} %>

<% 			if( ricercaProcedimenti.getDataCameraConsiglioInizio() != null || ricercaProcedimenti.getDataCameraConsiglioFine() != null ) { %>
        <tr>
          <td class="lVerdeNB">Procedimenti con Data Udienza Fissata :&nbsp;&nbsp;
<%				if( ricercaProcedimenti.getDataCameraConsiglioInizio() != null ) { %>
            Dal <%=DateUtils.getDateToString(ricercaProcedimenti.getDataCameraConsiglioInizio(), "dd-MM-yyyy")%>&nbsp;&nbsp;
<%      	} %>
          
<% 				if( ricercaProcedimenti.getDataCameraConsiglioFine() != null ) { %>
            &nbsp;Al&nbsp;&nbsp;<%=DateUtils.getDateToString(ricercaProcedimenti.getDataCameraConsiglioFine(), "dd-MM-yyyy")%>
          </td>
<%        } %>
       	</tr>
<%      } %>
      
<% 			if(!ricercaProcedimenti.getCodOggettoProcedimento().equals("-")) { %>
        	<tr>
          	<td class="lVerdeNB">Solo i procedimenti di : <%=ricercaProcedimenti.getDescrOggettoProcedimento()%></td>
        	</tr>
<%    	} %>

<%  		if(!ricercaProcedimenti.getCodPosizioneGiuridica().equals("-")) { %>
        	<tr>
          	<td class="lVerdeNB">Solo i procedimenti con posizione giuridica : <%=ricercaProcedimenti.getDescrPosizioneGiuridica()%></td>
        	</tr>
<%    	} %>

<%	} %>

  </table>
  <br>
<%
  Iterator itx = elencoProcedimenti.iterator();
%>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Numero SIUS</td>
      <td class="int">Data Iscrizione</td>
      <td class="int">Data Udienza</td>
      <td class="int">Contenuto</td>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Pos. Giuridica</td>
      <td class="int">Provvedimento</td>
      <td class="int">Azioni</td>
    </tr>
<%
   while ( itx.hasNext()) {
	   EveFasGepSogProvModel procedimento = (EveFasGepSogProvModel)itx.next();
%>
      <tr>
        <td class="c">
        	<font class="label">
        	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
          	<a class="cliccabile" 
          		href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=procedimento.getFascicoloSius().getIdFascicoloSius()%><%=retParam%>" 
          		Title="<%=procedimento.getFascicoloSius().getDescrTipoUfficio()%>&nbsp;<%=procedimento.getFascicoloSius().getDescrComuneUfficio()%> - Dettaglio Procedimento" >
            	<%=procedimento.getFascicoloSius().getChiaveAnno()%>/<%=procedimento.getFascicoloSius().getChiaveProgr()%>
          	</a>
        	</font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(procedimento.getFascicoloSius().getDataIscrizione(),"dd-MM-yyyy"),"-")%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(procedimento.getGeneraleProcedimento().getDataCameraConsiglio() ,"dd-MM-yyyy"),"-")%></font>
        </td>
        <td class="c">
        	<font class="label"><%=procedimento.getGeneraleProcedimento().getDescrOggettoProcedimento()%></font>
        </td>
        <td class="c">
        	<font class="label"><%=procedimento.getFascicoloSius().getSoggetto().getCognome()%></font>
        </td>
        <td class="c">
        	<font class="label"><%=procedimento.getFascicoloSius().getSoggetto().getNome()%></font>
        </td>
        <td class="c">
        	<font class="label"><%=procedimento.getGeneraleProcedimento().getDescrPosGiuridica()%></font>
        </td>
        <td class="c">
<% 			
				String lDescrProvvedimento = "-";		
				if( procedimento.getEvento() != null && procedimento.getEvento().getCodTipoProvvedimento()!=null )
					lDescrProvvedimento = procedimento.getEvento().getDescrTipoProvvedimento() + " - " + 
																procedimento.getEvento().getDescrEsito() + " - " + 
																procedimento.getEvento().getDescrMotivo();
%>
        		<font class="label"><%=lDescrProvvedimento%></font>
        </td>
        <td class="c">
	      <% 
	      	if ( procedimento.getEvento().getIdEvento() != null ) {
	    	  	String lAction = new String();
	    	  	RedirectTo lRedirect = new RedirectTo();
	    	  	lRedirect.setPage(IWebConstants.PG_MAIN);
	    	  	lRedirect.setAction("siap.sius.provvedimento.action.ActDettaglioProvvedimentoByIdEvento");
	    	  	lRedirect.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, ""+procedimento.getEvento().getIdEvento());
	    	  	if ((TornaQui != null) && TornaQui.trim().length() > 1) 
	    	  		lRedirect.setParameter("TornaQui", TornaQui);
	    	  	
	    	  	lAction = lRedirect.toString();
	      %>

					<a href="<%=lAction%>">
       			<img src="/images/dettagli.gif" alt="Dettaglio Provvedimento" width="12" height="12" border="0">
        	</a>

				<% }else { %>
						-
				<% }%>
        </td>
      </tr>
<%
  }
%>
    </table>

  </body>
</html>