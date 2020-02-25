<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>

<jsp:useBean id="ListaUfficiDistretto" scope="request" class="java.util.Vector" />
<jsp:useBean id="NomeLista" scope="request" class="java.lang.String"/>

<html>
	<head>
    	<title>[S.I.E.S.] - Lista Procure</title>
    	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
    	<script language="JavaScript">
      		function insertIT(str, competente) {
	        	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=str;
        		if (window.parent.opener.loadUfficiAccorpati) {
            		window.parent.opener.loadUfficiAccorpati(competente);
        		}
        		window.parent.close();
      		}
    	</script>
	</head>

<%
String Titolo = "Elenco Procure";
if (NomeLista != null && !"".equals(NomeLista.trim())) {
	Titolo = NomeLista;
}
%>

<body onload="focus();" >
 <table>
    <tr>
      <td class=LBG><%=Titolo%></td>
    </tr>
 </table>
 <Table width="100%">

 <%
  	Iterator itx = ListaUfficiDistretto.iterator();

  	while ( itx.hasNext())
  	{
    	UfficioModel ufficio = (UfficioModel)itx.next();
	%>
	<tr>
        <td class=l><%=ufficio.getDescrComune()%></td>
        <td class=c><a href="Javascript:insertIT('<%=StringUtils.cStrForJS(ufficio.getDescrComune())%>','<%=StringUtils.cStrForJS(ufficio.getCodUfficio())%>')"><img align="middle" src="/images/fileselected.gif" border=0></a></td></tr>
	<%
	}
%>
</table>

</body>
</html>