<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>

<jsp:useBean id="LuoghiDet" scope="request" class="java.util.Vector"/>

<html>
<head>
	<title>[S.I.E.S.] - Lista Luoghi detenzione legati al soggetto di un Provvedimento</title>
	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>
<body class=corpo>
  	<table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
    			</a>
    		</td>
      		<td class="LBG">
      			<font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Luoghi Detenzione</font>
      		</td>
     		<!-- BOTTONE DI RITORNO -->
        	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    	</tr>
     	<tr></tr>
     	<tr>
       		<jsp:include page="<%=siap.sius.fascicolo.action.ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    	</tr>
    	<tr>
      		<td>&nbsp;</td>
     	</tr>
  	</table>
	<table style="width: 100%;">
  		<tr>
			<td class=int>Data inizio</td>
			<td class=int>Data fine</td>
			<td class=int>Istituto Detenzione</td>
			<td class=int>Altro Luogo</td>
			<td class=int>Azione</td>
  		</tr>
<%
Iterator itx = LuoghiDet.iterator();
while (itx.hasNext()) {
	LuogoDetenzioneModel lLD = (LuogoDetenzioneModel) itx.next();
%>
		<tr>
        	<td class=l><%=StringUtils.toStringJSP(DateUtils.getDateToString(lLD.getDataInizioDetenzione(),"dd-MM-yyyy"), "-")%></td>
        	<td class=l><%=StringUtils.toStringJSP(DateUtils.getDateToString(lLD.getDataFineDetenzione(),"dd-MM-yyyy"), "-")%></td>
<%
	// 20171213: modifica su segnalazione m_dgDOG07.13-12-2017
	if (lLD.getIstitutoDetenzione() != null) {
%>
        	<td class=l><%=StringUtils.toStringJSP(lLD.getIstitutoDetenzione().getDescrTipoIstituto() +" di "+lLD.getIstitutoDetenzione().getDescrizione() ,"-")%></td>
<%
	} else {
%>
			<td class=l><%=StringUtils.toStringJSP(lLD.getDescrTipoIstituto(), "-")%></td>
<%
	}
%>
        	<td class=l><%=StringUtils.toStringJSP(lLD.getAltroLuogo(), "-")%></td>
        	<td class="c">
          		<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
             		<jsp:param name="CampoIdEntita" value="<%=ICostantiLuogoDetenzione.CAMPO_ID_LUOGO_DETENZIONE%>"/>
             		<jsp:param name="ValoreIdEntita" value="<%=lLD.getIdLuogoDetenzione()%>"/>
          		</jsp:include>
        	</td>
		</tr>
<%
}
%>
	</table>
</body>
</html>