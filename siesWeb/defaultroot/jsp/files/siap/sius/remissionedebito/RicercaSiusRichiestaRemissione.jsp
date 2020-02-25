<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="siap.sico.residenza.action.ICostantiResidenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.residenza.model.ResidenzaModel" %>
<%@ page import="siap.sico.residenza.model.ResidenzaAssociataModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="f3b.web.RedirectTo"%>

<jsp:useBean id="richiesteRemissioni" scope="request" class="java.util.Vector" />
<jsp:useBean id="isModificabile" scope="request" class="java.lang.String" />
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="caller" scope="request" class="java.lang.String" />

<%@page import="siap.sius.remissionedebito.model.RichiestaRemissioneModel"%>
<%@page import="siap.sius.remissionedebito.action.ICostantiSiusRemissioneDebito"%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Richieste Remissione Debito</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class=label>Funzione :</font>
        <font class=campo>Elenco Richieste Remissione Debito</font>
      </td>

      <!-- BOTTONE DI INSERIMENTO -->
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.remissionedebito.action.ActLoadInserisciRichiestaRemissioneDebito&IdFascicoloSius=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>" >
          <img  align="middle" src="/images/new24.gif" alt="Inserimento Richiesta Remissione Debito" width="24" height="24" border="0">
        </a>
      </td>
      <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        
    </tr>
  </table>
  <br>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>

  <table>
<%
	if (richiesteRemissioni.size() == 0 )
  	{
%>
    <td class="L">
      <font class="label"> Non ci sono Richieste Remissione Debito per il Fascicolo </font>
    </td>
<% 	} else { %>
    	<table width="96%">
  
    	<tr>
      	<td class="int">Tipo provvedimento</td>
      	<td class="int">Data emissione</td>
      	<td class="int">Autorità</td>
      	<td class="int">Spese mantenimento</td>
      	<td class="int">Spese procedimento</td>
      	<td class="int" width=5%>Azioni</td>
    	</tr>
<%
  		Iterator itx = richiesteRemissioni.iterator();

  		while ( itx.hasNext())
  		{
  			RichiestaRemissioneModel richRemissione = (RichiestaRemissioneModel)itx.next();
%>
    		<tr>
<%
			if ((richRemissione.getCodTipoProvvedimento() != null) && (richRemissione.getCodTipoProvvedimento().compareTo("-") != 0) ){ %>
				<td class="c"><%=StringUtils.toStringJSP(richRemissione.getDescrTipoProvvedimento() )%></td>
			<%}else {%>
				<td class="c">-</td><%}%>
				
			<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richRemissione.getDataEmissione(),"dd-MM-yyyy"))%></td>

<%   
			if ((richRemissione.getCodAutoritaEmittenteProvv() != null) && (richRemissione.getCodAutoritaEmittenteProvv().compareTo("-") != 0) ){ %>
      			<td class="c"><%=StringUtils.toStringJSP(richRemissione.getDescrAutoritaEmittenteProvv())%> di <%=StringUtils.toStringJSP(richRemissione.getDescrLuogoEmittenteProvv())%></td>
  			<%}else {%>
  					<td class="c">-</td><%}%>

		    <td class="c">
		    			<%if (richRemissione.getFlagSpeseCarcere().compareTo("S")==0) {%>
			    		<img src="/images/TickRed.gif">&nbsp;&nbsp; <%}%>
		    			<%if (richRemissione.getImportoSpeseCarcere() != null) {%>
			    			&nbsp;€&nbsp;<%=StringUtils.toEuroFormat(richRemissione.getImportoSpeseCarcere() ) %>&nbsp; 			    	
			    		<%} else {%>
			    			&nbsp;-&nbsp; 
			    		<%}%>
			</td>
			<td class="c">
					    <%if (richRemissione.getFlagSpeseProcedimento().compareTo("S")==0) {%>
			    		<img src="/images/TickRed.gif">&nbsp;&nbsp; <%}%>
		    			<%if (richRemissione.getImportoSpeseProcedimento() != null) {%>
			    			&nbsp;€&nbsp;<%=StringUtils.toEuroFormat(richRemissione.getImportoSpeseProcedimento() ) %>&nbsp; 			    	
			    		<%} else {%>
			    			&nbsp;-&nbsp; 
			    		<%}%>
			</td>
  					
<%				if (isModificabile.compareTo("SI")==0 )
    			{ %>
       			<td class=c>
        			<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           			<jsp:param name="CampoIdEntita" value="<%=ICostantiSiusRemissioneDebito.CAMPO_ID_RICHIESTA_REMISSIONE%>" />
           			<jsp:param name="ValoreIdEntita" value="<%=richRemissione.getIdRichiestaRemissione()%>" />
           			<jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS %>" />
           			<jsp:param name="ValoreIdEntitaProvv" value="<%=richRemissione.getFasSiuIdFascicoloSius() %>" />
        			</jsp:include>
      			</td>
    		</tr>
    		<%} else {%>
      			<td class=c>-</td>
    	</tr>
    		<%}
  		}
%>
    </table>
		<% } %>
  </form>
  </body>
</html>