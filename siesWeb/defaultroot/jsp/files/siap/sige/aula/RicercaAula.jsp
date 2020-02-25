<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sige.aula.model.AulaUdienzaModel"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sige.aula.action.ICostantiAula" %>
<%@ page import="siap.sige.aula.model.AulaUdienzaModel" %>

<jsp:useBean id="aule"         scope="request" class="java.util.Vector"/>
<jsp:useBean id="cancellabile" scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Aule</title>
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
      		<font class=campo>Elenco Aule</font> 
      	</td>
    	</tr>
  	</table>

  <br>

 	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
 
  <table>  
      <tr>
        <td class="int" width=10%>Sezione</td>
        <td class="int" width=25%>Descrizione</td>
		<td class="int" width=10%>Stanza</td>
		<td class="int" width=5%>Piano</td>
		<td class="int" width=25%>Ingresso</td>
        <td class="int" width=5%>Azioni</td>
      </tr>
<%
  Iterator itx = aule.iterator();
  while ( itx.hasNext())
  {
    AulaUdienzaModel aula = (AulaUdienzaModel)itx.next();
%>
    <tr>
      <td class=l>
      	  <%=StringUtils.toStringJSP(aula.getSezione().getDescrizione(), "-")%>
      </td>
      
      <td class=l>
      	  <%=StringUtils.toStringJSP(aula.getDescrizioneAula(), "-")%>
      </td>

      <td class=l>
      	  <%=StringUtils.toStringJSP(aula.getDescrizioneStanza(), "-")%>
      </td>

      <td class=l>
      	  <%=StringUtils.toStringJSP(aula.getNumeroPiano(), "-")%>
      </td>

      <td class=l>
      	  <%=StringUtils.toStringJSP(aula.getDescrizioneIngresso(), "-")%>
      </td>
      
      <td class=c>
        <jsp:include page="<%= ICostantiAula.PG_BUTTONS_AULA %>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiAula.CAMPO_ID_AULA%>" />
           <jsp:param name="ValoreIdEntita" value="<%=aula.getIdAula()%>" />
           <jsp:param name="CampoIdEntitaIdSezione" value="<%=ICostantiAula.CAMPO_ID_SEZIONE%>" />
           <jsp:param name="ValoreIdEntitaIdSezione" value="<%=aula.getIdSezione()%>" />
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