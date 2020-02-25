<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants"%>
<%-- <%@ page import="f3b.util.DateUtils"%> --%>
<%@ page import="siap.siep.scarti.model.WScartiModel"%>
<%@ page import="siap.siep.scarti.action.ICostantiWScarti" %>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.util.StringUtils" %>

<jsp:useBean id="wscarti"    scope="request" class="java.util.Vector"/>


<html>
<head>
<title>[S.I.E.S.] - Lista Scarti </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>

</head>

<body class="corpo">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>" >
		<table>
    <tr>
			  <td class=LBG><font class="label">Funzione :</font>&nbsp;&nbsp;
			    <font class="campo">Lista Scarti</font>
        </td>
      </tr>

    </table>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <br>

  <div align=center>
<table>
    <tr>
      <td class="int" width="5%">Identificativo Res</td>
      <td class="int">Record Scartato nella funzione</td>
      <td class="int">Motivazione</td>
    </tr>
 <%
  Iterator itx = wscarti.iterator();

  while ( itx.hasNext())
  {

      WScartiModel  lScarti = (WScartiModel)itx.next();
%>
    <tr>

 <td class=c>
          <%=StringUtils.toStringJSP(lScarti.getAnnRes())%> /  <%=StringUtils.toStringJSP(lScarti.getNumRes())%> &nbsp;
 </td>
 <td class=c>
          <%=StringUtils.toStringJSP(lScarti.getNoteScarto())%>&nbsp;
 </td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lScarti.getCausaScarto())%></td>
    </tr>
<%
  }
%>
    </table>

</div>
</form>
	</body>
</html>