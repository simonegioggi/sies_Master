<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario" %>
<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<%
	List scadenzario = (List) request.getAttribute("scadenzario");
%>

<%@page import="java.math.BigDecimal;"%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Decreti in corso di Definizione</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;
        <font class="campo">Ricerca Decreti in corso di Definizione</font></td>
      </tr>
    </table>
    <br>
		<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
		<br>
	  <form  method="POST"  action="<%= IWebConstants.PG_MAIN%>" name="f">
	    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActReportExelSimeone">
<%
			String lAnnoIniziale = request.getParameter( ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE );
			
			if ( lAnnoIniziale != null && !lAnnoIniziale.trim().equals(""))
			{
%>
	    	<input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>" value="<%=StringUtils.toStringJSP( (new BigDecimal(lAnnoIniziale.trim())) ) %>">
<%			    
			}
%>

<%
			String lProgrIniziale = request.getParameter( ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE );
			
			if ( lProgrIniziale != null && !lProgrIniziale.trim().equals(""))
			{
%>
	    	<input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>" value="<%=StringUtils.toStringJSP( (new BigDecimal(lProgrIniziale.trim())) ) %>">
<%			    
			}
%>

<%
			String lAnnoFinale = request.getParameter( ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE );
			
			if ( lAnnoFinale != null && !lAnnoFinale.trim().equals(""))
			{
%>
	    	<input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>" value="<%=StringUtils.toStringJSP( (new BigDecimal(lAnnoFinale.trim())) ) %>">
<%			    
			}
%>

<%
			String lProgrFinale = request.getParameter( ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE );
			
			if ( lProgrFinale != null && !lProgrFinale.trim().equals(""))
			{
%>
	    	<input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>" value="<%=StringUtils.toStringJSP( (new BigDecimal(lProgrFinale.trim())) ) %>">
<%			    
			}
%>

<%
			String lAnnoEmissioneIniziale   = request.getParameter("AnnoEmissioneIniziale");
			String lMeseEmissioneIniziale 	= request.getParameter("MeseEmissioneIniziale");
			String lGiornoEmissioneIniziale = request.getParameter("GiornoEmissioneIniziale");
			
			if (   lAnnoEmissioneIniziale != null && !lAnnoEmissioneIniziale.trim().equals("")
			    && lMeseEmissioneIniziale != null && !lMeseEmissioneIniziale.trim().equals("")
			    && lGiornoEmissioneIniziale != null && !lGiornoEmissioneIniziale.trim().equals("")
			    )
			{	
%>
				<input type="HIDDEN" name="GiornoEmissioneIniziale" value="<%=StringUtils.toStringJSP(lGiornoEmissioneIniziale)%>">
				<input type="HIDDEN" name="MeseEmissioneIniziale" value="<%=StringUtils.toStringJSP(lMeseEmissioneIniziale)%>">
				<input type="HIDDEN" name="AnnoEmissioneIniziale" value="<%=StringUtils.toStringJSP(lAnnoEmissioneIniziale)%>">
<%			    
			}
%>

<%
			String lAnnoEmissioneFinale   = request.getParameter("AnnoEmissioneFinale");
			String lMeseEmissioneFinale 	= request.getParameter("MeseEmissioneFinale");
			String lGiornoEmissioneFinale = request.getParameter("GiornoEmissioneFinale");
			
			if (   lAnnoEmissioneFinale != null && !lAnnoEmissioneFinale.trim().equals("")
			    && lMeseEmissioneFinale != null && !lMeseEmissioneFinale.trim().equals("")
			    && lGiornoEmissioneFinale != null && !lGiornoEmissioneFinale.trim().equals("")
			    )
			{	
%>
				<input type="HIDDEN" name="GiornoEmissioneFinale" value="<%=StringUtils.toStringJSP(lGiornoEmissioneFinale)%>">
				<input type="HIDDEN" name="MeseEmissioneFinale" value="<%=StringUtils.toStringJSP(lMeseEmissioneFinale)%>">
				<input type="HIDDEN" name="AnnoEmissioneFinale" value="<%=StringUtils.toStringJSP(lAnnoEmissioneFinale)%>">
<%			    
			}
%>

<%
			String[] lCodiciStatoNotifica = request.getParameterValues( "tipoNotifica" );
			
			if ( lCodiciStatoNotifica != null && lCodiciStatoNotifica.length > 0)
			{
	       for (int i = 0; i < lCodiciStatoNotifica.length; i++)
	       {			  
%>
	    		<input type="HIDDEN" name="tipoNotifica" value="<%=StringUtils.toStringJSP( lCodiciStatoNotifica[i] ) %>">
<%
	       }
			}
%>

		  <table>
		    <tr>
		     <td>
		      <a class="cliccabile" href="javascript:document.f.submit();" title="Elenco Completo per Stampa">
		        Elenco Completo per Stampa
		      </a>
		     </td>
		    </tr>
		  </table>
 		</form>
    <table cellpadding=2 cellspacing=2>
    <tr>
	    <td class="int">N° SIEP</td>
	    <td class="int">Cognome</td>
	    <td class="int">Nome</td>
	    <td class="int">Luogo Nascita</td>
	    <td class="int">Data Nascita</td>
	    <td class="int">Data Emissione Decreto</td>
      <td class="int">Stato delle notifiche</td>
      <td class="int">Azioni</td>
     </tr>
<%
	    Iterator itx = scadenzario.iterator();
	    while ( itx.hasNext())
	    {
	      ScadenzarioModel lSca = (ScadenzarioModel)itx.next();
	      FascicoloSiepModel lFas = lSca.getFascicoloModel();
	      SoggettoModel lSog = lFas.getSoggetto();
%>
 				<tr>
        	<td class=C>
          	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
            	<%=lFas.getChiaveAnno()%>
              /
              <%=lFas.getChiaveProgr()%>
            </a>
          </td>
          <td class=C><%=StringUtils.toStringJSP(lSog.getCognome())%>&nbsp;</td>
          <td class=C><%=StringUtils.toStringJSP(lSog.getNome())%>&nbsp;</td>
          <td class=C><%=StringUtils.toStringJSP(lSog.getDescrComuneNascita())%>&nbsp;</td>
          <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSog.getDataNascita(),"dd/MM/yyyy")) %>&nbsp;</td>
          <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy")) %>&nbsp;</td>
          <td class=C><%=StringUtils.toStringJSP(lSca.getDescrStatoNotifica())%>&nbsp;</td>
		      <td class=C>
		        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
		           <jsp:param name="CampoIdEntita" value="<%=ICostantiScadenzario.CAMPO_ID_SCADENZARIO%>"/>
		           <jsp:param name="ValoreIdEntita" value="<%=lSca.getIdScadenzario()%>"/>
		        </jsp:include>
		      </td>
		    </tr>
<%
    }
%>
    </table>
  </FORM>
</body>
</html>