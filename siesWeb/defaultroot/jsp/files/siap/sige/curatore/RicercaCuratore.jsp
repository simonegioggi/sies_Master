<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sige.curatore.action.ICostantiCuratore" %>
<%@ page import="siap.sige.curatore.model.CuratoreModel" %>

<jsp:useBean id="curatori" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Curatori</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

  <table>
    <tr>
    	<td class="LBG">
    		<a href="Javascript:window.print();">
    			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
    		</a>
    	</td>
      <td class="LBG">
      	<font class=label>Funzione :</font>
      	<font class=campo>Elenco Curatori</font> 
      </td>
    </tr>
  </table>

  <br>

 	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
 
  <table>  
      <tr>
        <td class="int" width=15%>Cognome</td>
        <td class="int" width=15%>Nome</td>
        <td class="int" width=15%>Cod. Fiscale</td>
        <td class="int" width=10%>Inizio Validità</td>
        <td class="int" width=10%>Fine Validità</td>
        <td class="int" width=10%>Disponibilità</td>
        <td class="int" width=5%>Azioni</td>
      </tr>
<%
  Iterator itx = curatori.iterator();
  while ( itx.hasNext())
  {
    CuratoreModel curatore = (CuratoreModel)itx.next();
%>
    <tr>
      <td class=l>
      	<%=curatore.getCognome()%>
      </td>
      <td class=l>
      	<%=curatore.getNome()%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(curatore.getCodiceFiscale(),"-")%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(DateUtils.getDateToString(curatore.getDataInizioValidita(),"dd-MM-yyyy"),"-")%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(DateUtils.getDateToString(curatore.getDataFineValidita(),"dd-MM-yyyy"),"-")%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(curatore.getDescrFlagStato())%>
      </td>
     	<td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiCuratore.CAMPO_ID_CURATORE%>" />
          <jsp:param name="ValoreIdEntita" value="<%=curatore.getIdCuratore()%>" />
          <jsp:param name="TornaQui" value="20" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
    </table>
  <br>
  </body>
</html>