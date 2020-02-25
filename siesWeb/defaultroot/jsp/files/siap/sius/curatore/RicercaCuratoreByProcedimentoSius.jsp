<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="f3b.log.LogF3B"%>
<%@page import="siap.sige.curatore.action.ICostantiCuratore"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.curatore.model.CuratoreSiusModel" %>
<%@ page import="siap.sige.curatore.model.CuratoreModel" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="curatori" scope="request" class="java.util.Vector" />
<jsp:useBean id="isModificabile" scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Curatori per Procedimento SIUS </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class=label>Funzione :</font>
        <font class=campo>Gestione Curatori/Tutori per Procedimento SIUS </font>
      </td>

       <!-- BOTTONE DI INSERIMENTO CURATORE -->
       <td class="LBG">
         <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.curatore.action.ActLoadInserisciCuratoreSius&IdFascicoloSius=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>" >
           <img  align="middle" src="/images/new24.gif" alt="Assegna Curatore " width="24" height="24" border="0">
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
  if ( curatori.size()==0 )
  {%>
    <tr>
      <td class="Titolo" colspan=5>Nessun Curatore / Tutore assegnato</td>
	</tr>
<%}else{%>
     <tr>
       <td class="Titolo" colspan=5> Storico assegnazione Curatori </td>
     </tr>

   	<tr>
		<td class="int">Curatore</td>
		<td class="int">Tipo</td>
		<td class="int">Data Inizio Competenza</td>
		<td class="int">Data Fine</td>
		<td class="int">Azioni</td>
   	</tr>
<%
  	Iterator itx = curatori.iterator();

  	while ( itx.hasNext())
  	{
    	CuratoreSiusModel curatore = (CuratoreSiusModel)itx.next();
%>
    <tr>
      <td class="c"><%=StringUtils.toStringJSP(curatore.getCuratore().getCognome(),"-")%> &nbsp;
      							<%=StringUtils.toStringJSP(curatore.getCuratore().getNome(),"-")%></td>
      <td class="c"><%=StringUtils.toStringJSP(curatore.getDescrTipo(),"-")%></td>
      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(curatore.getDataInizio(),"dd-MM-yyyy"),"-")%></td>
      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(curatore.getDataFine(),"dd-MM-yyyy"),"-")%></td>

    	<%if (isModificabile.compareTo("SI")==0 && StringUtils.toStringJSP(DateUtils.getDateToString(curatore.getDataFine(),"dd-MM-yyyy"),"-").equals("-")) 
    	{%>
      <td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=siap.sige.curatore.action.ICostantiCuratore.CAMPO_ID_CURATORE%>" />
           <jsp:param name="ValoreIdEntita" value="<%=curatore.getCuratore().getIdCuratore()%>" />
        </jsp:include>
      </td>
    </tr>
    <%}else{%>
      <td class=c>-</td>
    </tr>
    <%
    }
   }
  }%>
    </table>
  </form>
  </body>
</html>