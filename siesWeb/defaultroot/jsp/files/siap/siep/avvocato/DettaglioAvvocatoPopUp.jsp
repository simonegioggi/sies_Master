<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="avvocato" scope="request" class="siap.siep.avvocato.model.AvvocatoModel" />

<html>
<head>
<title>[S.I.E.S.] - GestioneAvvocato</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
	<script language="JavaScript">
	
		function insertIT(id,cognome,nome,foro)
		{
	   		var filtro='<%=request.getParameter("filtro")%>';

         	if(filtro ==  "completo")
       		{
       	     	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO%>.value=id;  
		     	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value=cognome;
			    if(nome == "-")
	          	{
	           		 	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value="";
	         	}else
	         	{
	           			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value=nome;
	         	}
         		 window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FORO%>.value=foro;
         	}	 
       		
       		if(filtro ==  "parziale")
       	 	{
	          	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO_PRESENTANTE%>.value=id;
	          	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_NOME_COGNOME_AVVOCATO%>.value=cognome+" "+nome;
       		}
      		window.parent.close();
		}
	</script>
</head>


<%
	AvvocatoModel lAvvocato = new AvvocatoModel(avvocato);
%>

<body class="corpo">

<form name="f">
	<table cellspacing=2 cellpadding=2>
	
		<tr>
			<td class="l"><font class="label">Cognome</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getCognome())%></font></td>
		</tr>
		<tr>
			<td class="l"><font class="label">Nome</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getNome())%>&nbsp;</font></td>
		</tr>
		<tr>
			<td class="l"><font class="label">Luogo Nascita</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getDescLuogoNascita())%>&nbsp;</font></td>
		</tr>
		<tr>
			<td class="l"><font class="label">Data Nascita</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
								lAvvocato.getDataNascita(), "dd-MM-yyyy"))%>&nbsp;</font></td>
		</tr>
		<tr>
			<td class="l"><font class="label">Foro</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getForo())%></font></td>
		</tr>
		<tr>
			<td class="l"><font class="label">Indirizzo</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getIndirizzo())%></font>&nbsp;</td>
		</tr>
		<tr>
			<td class="l"><font class="label">Con Studio in</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato
								.getDescComuneResidenza())%></font>&nbsp;</td>
		</tr>
		<tr>
			<td class="l"><font class="label">Telefono</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getTelefono())%></font>&nbsp;</td>
		</tr>
		<tr>
			<td class="l"><font class="label">Fax</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getFax())%></font>&nbsp;</td>
		</tr>
		<tr>
			<td class="l"><font class="label">EMail</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getEMail())%></font>&nbsp;</td>
		</tr>
		<tr>
			<td class="l"><font class="label">Codice Fiscale</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getCodiceFiscale())%></font>&nbsp;</td>
		</tr>
	
		<tr>
			<td class="l"><font class="label">Sospeso fino al</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
									lAvvocato.getDataSospensione(), "dd-MM-yyyy"))%></font>&nbsp;</td>
		</tr>
		<tr>
			<td class="l"><font class="label">Radiato dal</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
									lAvvocato.getDataRadiazione(), "dd-MM-yyyy"))%></font>&nbsp;</td>
		</tr>
		<tr>
			<td class="l"><font class="label">Non in attività per </font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato
									.getDescrNonAttivita())%></font>&nbsp;</td>
		</tr>
		<tr>
			<td class="l"><font class="label">Note </font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getNote())%></font>&nbsp;</td>
		</tr>
		<tr>
		   <td class=c>
		   <a href="Javascript:insertIT(
		   '<%=lAvvocato.getIdAvvocato()%>',
		   '<%=StringUtils.cStrForJS(lAvvocato.getCognome())%>',
		   '<%=StringUtils.cStrForJS(lAvvocato.getNome())%>',
		   '<%=StringUtils.cStrForJS(lAvvocato.getForo())%>');">
		   <img align="middle" src="/images/arrow24.gif" border=0></a></td> 
		<input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
	  	<input type="HIDDEN" name="filtro" value="<%=request.getParameter("filtro")%>">
	  	</tr>

	</table>
</form>
</body>
</html>