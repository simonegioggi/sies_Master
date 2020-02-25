<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sico.decodifiche.action.ICostantiComune" %>
<%@ page import="siap.sico.decodifiche.model.ComuneModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>

<jsp:useBean id="ListaComuni" scope="request" class="java.util.Vector" />

<%
    String NomeLista = request.getParameter("NomeLista");

    // Valorizzazione del titolo
    if ((NomeLista == null ) || (NomeLista.length() <1)) {
      NomeLista = "Lista Tribunali di Sorveglianza";
    }
%>

<html>
<head>
	<title>[S.I.E.S.] - Lista Tribunali di Sorveglianza</title>
	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
	<script language="JavaScript">
	function insertIT(str)
	{
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=str;
          window.parent.close();
	}
	</script>
</head>

<body onload="focus();">
 <table>
    <tr>
      <td class=LBG><%=NomeLista%></td>
    </tr>
 </table>
 <Table width="100%">

 <%
  	Iterator itx = ListaComuni.iterator();

  	while ( itx.hasNext())
  	{
    	ComuneModel comune = (ComuneModel)itx.next();
	%>
	<tr>
        <td class=l><%=comune.getDescrizione()%></td>
        <td class=c><a href="Javascript:insertIT('<%=StringUtils.cStrForJS(comune.getDescrizione())%>')"><img align="middle" src="/images/fileselected.gif" border=0></a></td></tr>
	<%
	}
%>
</table>

</body>
</html>