<%-- MEV_65: aggiunta pagina per gestire nuova funzionalita' --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="CampoChiaveProgrIniziale" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="CampoChiaveAnnoIniziale" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="CampoChiaveProgrFinale" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="CampoChiaveAnnoFinale" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoli" 				scope="request" class="java.util.Vector"/>
<jsp:useBean id="competenza" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="codCancelleria" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="descCancelleria" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="dataIniziale" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="dataFinale" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="pag" 						scope="request" class="java.lang.String"/>

<html>
	<head>
    <title>[S.I.E.S.] - Stampa Copertine Fascicoli SIUS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
    <script language="JavaScript">
	function stampaDoc() {
  		var url = "/jsp/Main.jsp?Action=siap.sius.stampa.action.ActStampaCopertineFascicoliSius&CampoChiaveProgrIniziale=<%=CampoChiaveProgrIniziale%>&CampoChiaveAnnoIniziale=<%=CampoChiaveAnnoIniziale%>&CampoChiaveProgrFinale=<%=CampoChiaveProgrFinale%>&CampoChiaveAnnoFinale=<%=CampoChiaveAnnoFinale%>&competenza=<%=competenza%>&codCancelleria=<%=codCancelleria%>&dataIniziale=<%=dataIniziale%>&dataFinale=<%=dataFinale%>&pag=<%=pag%>";
  		stampa(url);
	}
    </script>
 	</head>
	<body class="corpo">
	<table>
		<tr>
			<td class="LBG">
				<a href="Javascript:window.print();">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
				</a>
			</td>
			<td class="LBG">
   				<font class="label">Funzione:</font>&nbsp;<font class="campo">Stampa Copertine Fascicoli Sius</font>
			</td>
			<td class="LBG">
        		<a href="javascript:history.back()">
          			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="Torna Indietro" width="24" height="24" border="0">
        		</a>
      		</td>
		</tr>
	</table>
	<br>
 	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA_ESITO%>"></jsp:include>
	<div>
	<br>
<%
int pagina = new Integer(pag).intValue();
int da = (10*(pagina-1))+1;
int a = 10*pagina;
if (pagina > 1)
	a = (10*(pagina-1));
if (pagina*10 > fascicoli.size())
	a = (10*(pagina-1))+fascicoli.size();
String intervallo = "";
String s = "o";
if (fascicoli.size() > 1) {
	s = "i";
	intervallo = " (da " + da + " a " + a + ")";
}
%>
	<table style="width: 50%;">
		<tr>
  			<td width="40%" class="l">
   				<table>
   					<tr>
   						<td>
   							<br>
   							<font class="label">
	       						Trovat<%=s%>&nbsp;<b><font color="green"><%=fascicoli.size()%></font></b> procediment<%=s%> <%=intervallo%> da stampare per i seguenti criteri di ricerca:<br><br>
<%
if (Utils.isPresent(CampoChiaveAnnoIniziale)) {
%>
	      						-&nbsp;&nbsp;dal procedimento <font color="green"><b><%=CampoChiaveAnnoIniziale%>/<%=CampoChiaveProgrIniziale%></b></font> al procedimento <b><font color="green"><%=CampoChiaveAnnoFinale%>/<%=CampoChiaveProgrFinale%></font></b>;<br>
<%
}
if (Utils.isPresent(dataIniziale)) {
%>
								-&nbsp;&nbsp;dalla data <font color="green"><b><%=dataIniziale%></b></font> alla data <b><font color="green"><%=dataFinale%></font></b>;<br>
<%
}
if ("0".equals(competenza)) {
%>
	      						-&nbsp;&nbsp;solo i procedimenti dell'ufficio connesso
<%
} else {
%>
								-&nbsp;&nbsp;solo i procedimenti dell'utente connesso
<%
}
if (!Utils.isPresent(descCancelleria)) {
%>
								.<br>
<%
} else {
%>
								;<br>
	      						-&nbsp;&nbsp;solo i procedimenti assegnati alla cancelleria: <font color="green"><b><%=descCancelleria%></b></font>.<br>
<%
}
%>
<!-- 	      						<br>ATTENZIONE! Con i criteri selezionati sono stati trovati più di 10 procedimenti!<br> -->
<!-- 	      						Si consiglia di modificare i criteri di ricerca per evitare tempi di attesa lunghi. -->
   							</font>
     					</td>
     				</tr>
				</table>
			</td>
			<td width="10%" class="LBGISI">
				<a href="Javascript:stampaDoc();">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>/PrintGrande.gif" alt="Stampa Copertine Fascicoli Sius" width="64" height="64" border="3">
				</a>
 			</td>
		</tr>
	</table>
	</div>
	</body>
</html>