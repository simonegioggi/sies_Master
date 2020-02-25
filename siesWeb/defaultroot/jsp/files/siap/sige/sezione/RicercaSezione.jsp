<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sige.sezione.action.ICostantiSezione" %>
<%@ page import="siap.sige.sezione.model.SezioneModel" %>

<jsp:useBean id="sezioni" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Sezioni</title>
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
      	<font class=campo>Elenco Sezioni</font> 
      </td>
    </tr>
  </table>

  <br>

 	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
 
  <table>  
      <tr>
        <td class="int" width=5%>Codice</td>
        <td class="int" width=90%>Descrizione</td>
        <td class="int" width=5%>Azioni</td>
      </tr>
<%
  Iterator itx = sezioni.iterator();
  while ( itx.hasNext())
  {
    SezioneModel sezione = (SezioneModel)itx.next();
%>
    <tr>
      <td class=l>
      	<%=sezione.getCodice()%>
      </td>
      <td class=l>
      	<%=sezione.getDescrizione()%>
      </td>
     	<td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiSezione.CAMPO_ID_SEZIONE%>" />
          <jsp:param name="ValoreIdEntita" value="<%=sezione.getIdSezione()%>" />
          <jsp:param name="TornaQui" value="20"/>
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