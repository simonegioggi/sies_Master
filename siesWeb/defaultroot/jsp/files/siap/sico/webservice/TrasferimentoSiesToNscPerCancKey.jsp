<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>

<html>
	<head>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<title> Trasferimento Provvedimento da Sies a  Nsc </title>
		<script language="JavaScript">
  			function CaricaDatiFascicolo() 
  			{ 
 						document.LoadInviaNsc.submit();
				}
  		</script>
	</head>

	<body onLoad="CaricaDatiFascicolo()" class="corpo"> 
				<FORM method="POST"  action="<%=IWebConstants.PG_MAIN%>" name="LoadInviaNsc"> 
							<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" 
									value="siap.sico.webservice.action.ActPrelevaDatiFascicoloCancellato" />
							
						<div align=center id="ciao" style="visibility:visible;position:absolute;top:200px;left:200px">
				     			<table bgcolor="#EEEEEE">
				       				<tr>
				         					<td>
				           						<img src="/images/rotelle3.gif">
				         					</td>
				         					<td>
				           						<font size=+1 color=navy>Attendere... Trasferimento in corso.</font>
				         					</td>
				       				</tr>
				     			</table>
				   	</div> 	
				  				
				</FORM>
	</body>
</html>