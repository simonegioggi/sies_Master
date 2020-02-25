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
	clickme1 = new Image(58,17); clickme1.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/esistente.gif";
	clickme2 = new Image(58,17); clickme2.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/esistente_down.gif";
	clickme3 = new Image(58,17); clickme3.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/nuova.gif";
	clickme4 = new Image(58,17); clickme4.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/nuova_down.gif";
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

<script language="JavaScript">

function vai(pulsante)
{
   document.warning.warning_nuova.value=pulsante;
   document.warning.submit();

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
        <a href="javascript:vai('esistente');" onMouseOver="hiLite('img01','clickme2')" onMouseOut="hiLite('img01','clickme1')">
        	<IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/esistente.gif" BORDER="0" ALT="" NAME="img01">
      	</a>
			</td>
			<td align="center"  class="tab2" width="50%">
      	<a href="javascript:vai('nuova');" onMouseOver="hiLite('img02','clickme4')" onMouseOut="hiLite('img02','clickme3')">
      		<IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/nuova.gif" BORDER="0" ALT="" NAME="img02">
      	</a>
			</td>
		</tr>
      <input type="HIDDEN" name="warning_nuova" value="">
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