<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.giudicepopolare.action.ICostantiGiudicePopolare"%>
<%@ page import="siap.sige.giudicepopolare.model.GiudicePopolareModel"%>

<jsp:useBean id="giudicipopolari" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Giudici Popolari</title>
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
      	<font class=campo>Elenco Giudici Popolari</font> 
      </td>
    </tr>
  </table>

  <br>

 	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <table>  
      <tr>
        <td class="int" width=20%>Cognome</td>
        <td class="int" width=20%>Nome</td>
				<td class="int" width=10%>Data Nascita</td>
        <td class="int" width=15%>Sezione</td>
        <td class="int" width=10%>Inizio Validità</td>
        <td class="int" width=10%>Fine Validità</td>
        <td class="int" width=10%>Ruolo</td>
        <td class="int" width=5%>Azioni</td>
      </tr>
<%
  Iterator itx = giudicipopolari.iterator();
  while ( itx.hasNext())
  {
    GiudicePopolareModel giudicepopolare = (GiudicePopolareModel)itx.next();
%>
    <tr>
      <td class=l>
      	<%=giudicepopolare.getCognome()%>
      </td>
      <td class=l>
      	<%=giudicepopolare.getNome()%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(
      	    DateUtils.getDateToString(giudicepopolare.getDataNascita(),"dd-MM-yyyy"),"-")%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(giudicepopolare.getDescrSezione(),"-")%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(
      	    DateUtils.getDateToString(giudicepopolare.getDataInizioValidita(),"dd-MM-yyyy"),"-")%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(
      	    DateUtils.getDateToString(giudicepopolare.getDataFineValidita(),"dd-MM-yyyy"),"-")%>
      </td>
      <td class=l>
      	<%=StringUtils.toStringJSP(giudicepopolare.getDescrRuolo())%>
      </td>
     	<td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>" />
          <jsp:param name="ValoreIdEntita" value="<%=giudicepopolare.getIdGiudicePopolare()%>" />
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