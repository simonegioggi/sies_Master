<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.altrigradigiudizio.action.ICostantiAltriGradiGiudizio" %>
<%@ page import="siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="altrigradigiudizio" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Altri Gradi di Giudizio</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Elenco Altri Gradi di Giudizio</font></td>
      </tr>
    </table>

    <br>

    <table cellpadding="4" cellspacing="20" >
		<tr>
	      <td class="int" width="300">Sentenza di I Grado</td>
	                  
	      <td class="int" width="280">Sentenza di II Grado</td>
	      
	      <td class="int" width="400">Sentenza di Cassazione</td>     
	    </tr>
	</table>

<table cellpadding="4" cellspacing="4">
    <tr>
      <td class="int">Data</td>
      <td class="int">Anno/Numero</td>
      <td class="int">Autorità</td>
      <td class="int">Luogo</td>
            
      <td class="int">Data</td>
      <td class="int">Anno/Numero</td>
      <td class="int">Autorità</td>
      <td class="int">Luogo</td>
            
      <td class="int">Anno/Num. Reg.Gen.</td>
      <td class="int">Anno/Num. Sentenza</td>
      <td class="int">Anno/Num. Racc.Gen.</td>
      <td class="int">Dispositivo</td>
      <td class="int">Azioni</td>
    </tr>

<%
    Iterator itx = altrigradigiudizio.iterator();
    while ( itx.hasNext())
    {
      AltriGradiGiudizioModel lAltroGradoGiudizio = (AltriGradiGiudizioModel)itx.next();
%>
    <tr>
      <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltroGradoGiudizio.getDataSentenzaIGrado(),"dd-MM-yyyy"))%></td>
      <td class=C><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getAnnoSentenzaIGrado())%>/<%=StringUtils.toStringJSP(lAltroGradoGiudizio.getNumeroSentenzaIGrado())%></td>
      <td class=C><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getDescrAutEmittSentIGrado())%></td>
      <td class=C><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getDescrLuoEmittSentIGrado())%></td>
      
      <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltroGradoGiudizio.getDataSentenzaIiGrado(),"dd-MM-yyyy"))%></td>
      
      
      <% if (lAltroGradoGiudizio.getNumeroSentenzaIiGrado() != null) {
		%>
			<td class=C><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getAnnoSentenzaIiGrado())%>/<%=StringUtils.toStringJSP(lAltroGradoGiudizio.getNumeroSentenzaIiGrado())%></td>
		<%
		}else{ 
		%>
				<td class=C> - </td>					
	  <%} %>
	  
      <td class=C><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getDescrAutEmittSentIiGrado())%></td>
      <td class=C><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getDescrLuoEmittSentIiGrado())%></td>
      
      
      <% if (lAltroGradoGiudizio.getNumeroRegGenCassaz() != null) {
		%>
			<td class=C><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getAnnoRegGenCassaz())%>/<%=StringUtils.toStringJSP(lAltroGradoGiudizio.getNumeroRegGenCassaz())%></td>
		<%
		}else{ 
		%>
				<td class=C> - </td>					
	  <%} %>
	  
	  <% if (lAltroGradoGiudizio.getNumeroSentenzaCassaz() != null) {
		%>
			<td class=C><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getAnnoSentenzaCassaz())%>/<%=StringUtils.toStringJSP(lAltroGradoGiudizio.getNumeroSentenzaCassaz())%></td>
		<%
		}else{ 
		%>
				<td class=C> - </td>					
	  <%} %>
	  
      
      <% if (lAltroGradoGiudizio.getNumeroRaccGenealeIiGrado() != null) {
		%>
			<td class=C><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getAnnoRaccGenealeIiGrado())%>/<%=StringUtils.toStringJSP(lAltroGradoGiudizio.getNumeroRaccGenealeIiGrado())%></td>
		<%
		}else{ 
		%>
				<td class=C> - </td>					
	  <%} %>
	  	  
      <td class=C><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getDescrTipoDecisioneCassazione())%></td>
      
      <td class=C>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiAltriGradiGiudizio.CAMPO_ID_ALTRIGRADIGIUDIZIO%>"/>
           <jsp:param name="ValoreIdEntita" value="<%=lAltroGradoGiudizio.getIdAltrigradigiudizio()%>"/>
        </jsp:include>
      </td>
    </tr>
<%
    }
%>
    </table>
  </FORM>
  <br>

</body>
</html>