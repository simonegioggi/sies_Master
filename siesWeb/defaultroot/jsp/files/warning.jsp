<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Set"%>

<%@ page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="postaParametri" scope="request" class="java.lang.String"/>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>F3B - ( Framework Bull Building Blocks ) </title>
<!-- INIZIO Definizione Script BOTTONI GRAFICI  -->
<SCRIPT LANGUAGE="JavaScript">
// preload images:
if (document.images) {
	clickme1 = new Image(58,17); clickme1.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif";
	clickme2 = new Image(58,17); clickme2.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01-down.gif";
	clickme3 = new Image(58,17); clickme3.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01.gif";
	clickme4 = new Image(58,17); clickme4.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01-down.gif";
}

function hiLite(imgName,imgObjName) {
	if (document.images) {
    	document.images[imgName].src = eval(imgObjName + ".src");
  	}
}
</SCRIPT>
<!-- Fine Definizione Script BOTTONI GRAFICI -->
<%
String jumpPage = "history.back()";
String newPage = (String) request.getAttribute(IWebConstants.GOTO_PAGE);
if (newPage != null) {
	jumpPage = "\""+newPage+"\"";
}
%>
<script language="JavaScript">
function vai() {
<%
if (newPage != null)
  	out.print("location=" + jumpPage);
else
  	out.print("history.back();");
%>
}
</script>
</head>

<BODY class="corpo">
<%
String message  = (String)request.getAttribute(IWebConstants.MESSAGE_TEXT);
String modalita = (String)request.getAttribute("Modalita");
%>
<br><br><br><br><br>
<table width="300"  cellspacing="0" align="center" class="tab">
	<tr>
		<td class="c">
			<form name="warning">
<%
if ("S".equals(postaParametri)) {
	Map lParametri = request.getParameterMap();
	Set lChiavi = lParametri.keySet();
	Iterator lKeyIter = lChiavi.iterator();
  	while (lKeyIter.hasNext()) {
		Object lChiaveObj = lKeyIter.next();
		String lChiave = (String) lChiaveObj;
		String[] lValori = (String[]) lParametri.get(lChiaveObj);
		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("lChiave/lValore = "+lChiave+"/"+lValori[0]);
%>
				<input type="HIDDEN" name="<%=lChiave%>" value="<%=lValori[0]%>">
<%
	}
}
%>
				<table width="300"  cellspacing="0" align="center" class="tab">
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
    				<tr>
    					<td>
      						<input type="HIDDEN" name="warning" value="S">
<%
if ((String)request.getAttribute("lFlagPassato") == null) {
%>
							<!--aggiunto warning_2 per gestire 2 warning sulla stessa pagina-->
      						<input type="HIDDEN" name="warning_2" value="S">
<%
}
%>
      						<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=(String)request.getAttribute(IWebConstants.ACTION_FIELD)%>">
      					</td>
      				</tr>
  				</table>
			</form>
		</td>
    </tr>
</table>
</body>
</html>