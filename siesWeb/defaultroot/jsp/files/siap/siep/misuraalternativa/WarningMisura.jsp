<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa" %>
<%@ page import="java.math.BigDecimal" %>



<html>
<head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>F3B - ( Framework Bull Building Blocks ) </title>
<!-- INIZIO Definizione Script BOTTONI GRAFICI  -->
<SCRIPT LANGUAGE="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
// preload images:
if (document.images)
{
	clickme1 = new Image(58,17); clickme1.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif";
	clickme2 = new Image(58,17); clickme2.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01-down.gif";
	clickme3 = new Image(58,17); clickme3.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01.gif";
	clickme4 = new Image(58,17); clickme4.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01-down.gif";
}

function hiLite(imgName,imgObjName)
{
  if (document.images)
  {
    document.images[imgName].src = eval(imgObjName + ".src");
  }
}
</SCRIPT>
<!-- Fine Definizione Script BOTTONI GRAFICI -->
<%
	String jumpPage = "history.back()";
	String newPage = (String)request.getAttribute(IWebConstants.GOTO_PAGE);
	if(newPage != null)
	{
		jumpPage = "\""+newPage+"\"";
	}
%>
<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
function vai()
{
 <%
	if(newPage != null)
	  out.print("location=" + jumpPage);
	else
	  out.print("history.back();");
 %>
}
</script>
</head>

<BODY class="corpo">
<%
  String message 	= (String)request.getAttribute(IWebConstants.MESSAGE_TEXT);
	String modalita = (String)request.getAttribute("Modalita");
%>

<br><br><br><br><br>
<table width="300"  cellspacing="0" align="center" class="tab" >
<tr><td class="c">
<form name="warning">
	<table width="300"  cellspacing="0" align="center" class="tab" >
		<tr align="center" valign="middle">
			<td align="center"  colspan="2"  class="tab">
				<p>&nbsp;<p>
				<B><%=message%></B>
				<p>&nbsp;<p>
			</td>
		</tr>
		<tr align="center" valign="middle">
			<td align="center"  class="tab2" width="50%">
      	<a href="javascript:document.warning.submit();" onMouseOver="hiLite('img01','clickme2')" onMouseOut="hiLite('img01','clickme1')">
      		<IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif" BORDER="0" ALT="" NAME="img01">
      	</a>
			</td>
			<td align="center"  class="tab2" width="50%">
      	<a href="javascript:vai();" onMouseOver="hiLite('img02','clickme4')" onMouseOut="hiLite('img02','clickme3')">
      		<IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01.gif" BORDER="0" ALT="" NAME="img02">
      	</a>
			</td>
		</tr>
      <input type="HIDDEN" name="warning" value="S">
<!--aggiunto warning_2 per gestire 2 warning sulla stessa pagina-->
 <%if((String)request.getAttribute("lFlagPassato") == null)
{%>
      <input type="HIDDEN" name="warning_2" value="S">
<%}%>

<!--aggiunto warning_proroga per gestire il warning della proroga-->
 <%if(request.getAttribute("warning_proroga") != null)
{%>
      <input type="HIDDEN" name="warning_proroga" value="<%=(String)request.getAttribute("warning_proroga")%>">
<%}%>

<!--aggiunto warning_nuova per gestire il warning del rigetto e della revoca sospensione esecuzione-->
 <%if(request.getAttribute("warning_nuova") != null)
{%>
      <input type="HIDDEN" name="warning_nuova" value="<%=(String)request.getAttribute("warning_nuova")%>">
<%}%>

      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=(String)request.getAttribute(IWebConstants.ACTION_FIELD)%>">
		</tr>
	</table>
    <input type="HIDDEN" value="<%=(String) request.getAttribute("idmisuraalternativa")%>" type="text" size="2" maxlength="2" name="idmisuraalternativa"  >

</form>
			</td>
		</tr>
	</table>
</body>
</html>