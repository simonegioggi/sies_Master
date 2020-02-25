<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.sico.decodifiche.model.ComuneModel" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>

<jsp:useBean id="ListaUfficiDistretto" scope="request" class="java.util.Vector" />
<html>
<head>
    <title>[S.I.E.S.] - Lista Procure</title>
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
      <td class=LBG>Elenco Comuni Sedi UNEP</td>
    </tr>
 </table>
 <Table width="100%">

 <%
  	Iterator itx = ListaUfficiDistretto.iterator();
    ComuneModel lComune;
  	while ( itx.hasNext())
  	{
  		lComune = (ComuneModel) itx.next();
    	String lDescComune = lComune.getDescrizione();
	%>
	<tr>
        <td class=l><%=lDescComune%></td>
        <td class=c><a href="Javascript:insertIT('<%=StringUtils.cStrForJS(lDescComune)%>')"><img align="middle" src="/images/fileselected.gif" border=0></a></td></tr>
	<%
	}
%>
</table>

</body>
</html>