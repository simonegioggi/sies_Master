<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sius.esperto.action.ICostantiEsperto" %>
<%@ page import="siap.sius.esperto.model.EspertoModel" %>

<%@ page import="siap.sico.decodifiche.util.DecodificheUtils" %>

<jsp:useBean id="esperti" scope="request" class="java.util.Vector"/>

<% 
	Collection flags =(Collection) request.getAttribute("flags");
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Esperti</title>
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
      	<font class=campo> Elenco Esperti</font> 
      </td>
    </tr>
  </table>

  <br>

 	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"/>
 
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
  Iterator itx = esperti.iterator();
  while ( itx.hasNext())
  {
    EspertoModel esperto = (EspertoModel)itx.next();
%>
    <tr>
      <td class=l>
      	<%=esperto.getCognome()%>
      </td>
      <td class=l>
      	<%=esperto.getNome()%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(esperto.getCodiceFiscale(),"-")%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(DateUtils.getDateToString(esperto.getDataInizioValidita(),"dd-MM-yyyy"),"-")%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(DateUtils.getDateToString(esperto.getDataFineValidita(),"dd-MM-yyyy"),"-")%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(DecodificheUtils.getDescbyCode(flags,esperto.getFlagStato()),"-")%>
      </td>
     	<td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEsperto.CAMPO_ID_ESPERTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=esperto.getIdEsperto()%>" />
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