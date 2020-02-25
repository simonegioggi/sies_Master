<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.collaboratore.model.CollaboratoreModel"  %>
<%@ page import="siap.sius.collaboratore.action.ICostantiCollaboratore"  %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>


<jsp:useBean id="isModificabile" scope="request" class="java.lang.String"/>
<jsp:useBean id="idFascicoloSius" scope="request" class="java.lang.String"/>
<jsp:useBean id="isCollaboratore" scope="request" class="java.lang.String"/>
<jsp:useBean id="movimenti_collaboratore" scope="request" class="java.util.Vector"/>



<html>
<head>
<title>[S.I.A.P.] - Dettaglio Collaboratore di Giustizia </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>

	<body class="corpo">
    <table>
      <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Collaboratore di Giustizia</font>
      </td>
 <%if (!isCollaboratore.equalsIgnoreCase("SI")) { %>
      <!-- BOTTONE DI INSERIMENTO -->
          <td class="LBG">
            <jsp:include page="<%=ICostantiCollaboratore.PG_BUTTONS_LINK %>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" />
            <jsp:param name="ValoreIdEntita" value="<%=idFascicoloSius%>" />
            <jsp:param name="Modificabile" value="<%=isModificabile%>"/>
            </jsp:include>
          </td>
 <%} %>
  	<!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
   </tr>
 </table>
   <br>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <br>
<% if (movimenti_collaboratore != null && movimenti_collaboratore.size() > 0)
{ %>
  <table width=90%>
     <div align=center>
        <tr>
          <td class="int" width=20%>Data Inserimento</td>
          <td class="int" width=20%>Data Inizio</td>
          <td class="int" width=20%>Data Fine</td>
          <td class="int" width=10%>Azioni</td>
        </tr>
      </div>
<%
  boolean bottone = true;
  Iterator itx = movimenti_collaboratore.iterator();
  while ( itx.hasNext())
  {
	  CollaboratoreModel lCollaboratore = (CollaboratoreModel)itx.next();
%>
    <tr>
      <td class=c><%=DateUtils.getDateToString(lCollaboratore.getDataInserimento(),"dd-MM-yyyy")%></td>
      <td class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(lCollaboratore.getDataInizio(),"dd-MM-yyyy"),"-")%></td>
      <td class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(lCollaboratore.getDataFine(),"dd-MM-yyyy"), "-")%></td>
     <td class=c>
<% if (bottone)
   {
%>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiCollaboratore.CAMPO_ID_COLLABORATORE%>" />
            <jsp:param name="ValoreIdEntita" value="<%=lCollaboratore.getIdCollaboratore().toString()%>" />
          <jsp:param name="Modificabile" value="<%=isModificabile%>"/>
       </jsp:include>
<%
    bottone = false;
  } else { %>
&nbsp;
<% } %>
      </td>
    </tr>
<%
  }
%>
    </table>
<% } else { %>
  <br>
<font class="campo"> Il Procedimento non risulta associato ad un Collaboratore di Giustizia </font>
<% } %>
</body>
</html>