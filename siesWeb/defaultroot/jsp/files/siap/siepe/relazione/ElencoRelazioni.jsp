<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.siepe.relazione.model.RelazioneModel"%>
<%@ page import="siap.siepe.relazione.action.ICostantiRelazione"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="relazioni" scope="request" class="java.util.Vector" />
<jsp:useBean id="Modificabile"              scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<%
 boolean isModificabile = false;

if (Modificabile == null || Modificabile.trim().length() < 1  ||  Modificabile.compareTo("SI") == 0)
    isModificabile = true;
if (relazioni.size() > 0) {
%>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW %>"></script>

<html>
  <head>
    <title>[S.I.E.S.] - Elenco Relazioni collegate ad attività</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  </head>
  <body class="corpo">

  <table>
    <tr><td class="Titolo" > Elenco Relazioni </td></tr>
    <%
    Iterator itx = relazioni.iterator();

    while ( itx.hasNext() )
    {
      RelazioneModel lRelazione = (RelazioneModel)itx.next();
%>
    	<tr>
      	<td>
        	<%=StringUtils.toStringJSP(lRelazione.getNote(), "-")%>
        </td>

        <td>
        	<%=DateUtils.getDateToString( lRelazione.getDataEmissione(), "dd-MM-yyy") %>
        </td>

<% 		if(isModificabile) { %>
		<%if(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(lRelazione.getCodUfficioInserimento()) )
    	{%>
        <td>
        	<a href="Javascript:conferma('siap.siepe.relazione.action.ActCancellaRelazione','<%=ICostantiRelazione.CAMPO_ID_RELAZIONE%>','<%=lRelazione.getIdRelazione()%>');">
          	<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
          </a>
        </td>
<% 	}	} %>
        <td>
        	<a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.siepe.relazione.action.ActLoadDettaglioRelazione&<%=ICostantiRelazione.CAMPO_ID_RELAZIONE%>=<%=lRelazione.getIdRelazione()%>')">
          	<img src="/images/print.gif" alt="Stampa" width="12" height="12" border="0">
          </a>
        </td>
<!-- BOTTONE DI TRASFERIMENTO -->
<% if(isModificabile) { %>
		<%if(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(lRelazione.getCodUfficioInserimento()) )
    	{%>
    					<td>
      					<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siepe.relazione.action.ActLoadTrasferisciRelazione&<%=ICostantiRelazione.CAMPO_ID_RELAZIONE%>=<%=lRelazione.getIdRelazione()%>">
        					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>net24.gif" width="12" height="12" alt="Trasferisci" width="24" height="24" border="0">
      					</a>
    					</td>
<% } } %>

    </tr>
<% } %>
  </table>
</body>
</html>
<% } %>