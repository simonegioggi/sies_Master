<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel" %>
<%@ page import="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioMagistratoModel" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>

<jsp:useBean id="TornaQui"            scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="magistrati"          scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco Magistrati Assegnatari per Procedimento SIGE </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class=label>Funzione :</font>
        <font class=campo>Gestione Magistrati per Procedimento SIGE </font>
      </td>

      <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
  <br>
      <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
  <br>
<%
  BigDecimal lIdSoggetto = (FascicoloSigeEsteso.getSoggetto().getIdSoggetto() );
%>
  <table>
<%
  if ( magistrati.size()==0 )
  {%>
    <tr>
      <td class="Titolo" colspan=5>Nessun Magistrato assegnato</td>
	</tr>
<%}else{%>
     <tr>
       <td class="Titolo" colspan=5> Storico assegnazione Magistrati </td>
     </tr>

   	<tr>
		<td class="int" colspan="2" >Magistrato</td>
		<td class="int">Data Inizio Competenza</td>
		<td class="int">Data Fine</td>
   	</tr>
<%
  	Iterator itx = magistrati.iterator();

 	while ( itx.hasNext())
  	{
    	MagistratoAssegnatarioMagistratoModel magistrato = (MagistratoAssegnatarioMagistratoModel)itx.next();
%>
    <tr>
      <td class="c" colspan="2"><%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome(),"-")%> &nbsp;
      							<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome(),"-")%></td>
      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(magistrato.getMagistratoAssegnatario().getDataInizio(),"dd-MM-yyyy"),"-")%></td>
      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(magistrato.getMagistratoAssegnatario().getDataFine(),"dd-MM-yyyy"),"-")%></td>
<%  } // end while
  } // end else
%>
    </table>
  </form>
  </body>
</html>