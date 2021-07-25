<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel" %>


<jsp:useBean id="avvocato" scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<html>
<head>
<title>[S.I.E.S.] - Lista Difensori</title>
<link rel="STYLESHEET" type="text/css" href="/css/style.css">
<%
if (! modalita.equals("NoPop")) {
%>
<script language="JavaScript">
function controlla(id) {
	if (id=="S") {
		alert("Il difensore risulta sospeso");
		return false;
	}
	if (id=="R") {
		alert("Il difensore risulta radiato");
		return false;
	}
	if (id=="A"	&& id != "-") {
		alert("Il difensore risulta non in attività");
		return false;
	}
	// MEV_21: elimino questa casistica
	// 	if (id=="B") {
	return true;
	// 	}
}

<%-- MEV_21: aggiunti campi pec e nazione e descComuneStudio (al posto di residenza) --%>
function insertIT(id,cognome,nome,foro,indirizzo,telefono,fax,email,pec,codicefiscale,luogoNascita,nazione,giornoNascita,meseNascita,annoNascita,descComuneStudio,stato) {
	var flag = controlla(stato);
    if (flag) {
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value=id;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value=cognome;
		if (nome == "-") {
		    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value="";
		} else {
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value=nome;
		}
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FORO%>.value=foro;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>.value=indirizzo;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_TELEFONO%>.value=telefono;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FAX%>.value=fax;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_E_MAIL%>.value=email;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_PEC%>.value = pec;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>.value=codicefiscale;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.value = nazione;
		if (nazione == "039") {
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value = luogoNascita;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>.value = "";
		} else {
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value = "";
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>.value = luogoNascita;
		}
		// Popola altri campi
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value=giornoNascita;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value=meseNascita;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value=annoNascita;
<%-- 	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value=luogoNascita; --%>
<%-- 	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA%>.value=residenza; --%>
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>.value = descComuneStudio;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>.value = stato;
		window.parent.close();
	}
}
</script>
<%
}
%>
</head>

<body class=corpo >
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    	<td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Difensori</font></td>
  	</tr>
</table>
<%
if (modalita.equals("NoPop")) {
%>
<BR>
<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiSoggFasSiep.jsp"/>
<BR>
<%
}
%>
<form name="f">
<Table width="100%">
	<tr>
		<td class=int width=40%>Nome</td>
		<td class=int width=10%>Foro</td>
		<td class=int width=40%>Indirizzo</td>
<%
if (! modalita.equals("NoPop")) {
%>
		<td class=int width=10%>Seleziona</td>
<%
}
%>
	</tr>
<%
Iterator itx = avvocato.iterator();
while (itx.hasNext()) {
	AvvocatoModel lAvv = ((AvvocatoSigeModel)itx.next()).getAvvocato();
%>
	<tr>
		<td class=l><%=StringUtils.toStringJSP(lAvv.getCognome()) + " " +  StringUtils.toStringJSP(lAvv.getNome())%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getForo(),"-")%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getIndirizzo(),"-") + " - " + StringUtils.toStringJSP(lAvv.getDescComuneResidenza())%></td>
<%
// MEV_21: commento lo stato ('<%=stato>') e metto il get della proprietà nell'insertIT
// 	String stato = null;
// 	if (lAvv.getDataSospensione() != null) {
//     		stato = "S";
// 	} else if (lAvv.getDataRadiazione() != null) {
//     		stato = "R";
// 	} else if (!"-".equals(lAvv.getCodNonAttivita())) {
//    		stato = "A";
// 	} else {
// 		stato = "B";
// 	}
	if (!modalita.equals("NoPop")) {
%>
		<%-- MEV_21: aggiunti campi pec e nazione e descComuneStudio (al posto di residenza) --%>
		<td class=c>
			<a href="Javascript:insertIT(
				'<%=lAvv.getIdAvvocato()%>','<%=StringUtils.cStrForJS(lAvv.getCognome())%>','<%=StringUtils.cStrForJS(lAvv.getNome())%>',
				'<%=StringUtils.cStrForJS(lAvv.getForo())%>','<%=StringUtils.cStrForJS(lAvv.getIndirizzo())%>',
				'<%=StringUtils.cStrForJS(lAvv.getTelefono())%>','<%=StringUtils.cStrForJS(lAvv.getFax())%>',
				'<%=StringUtils.cStrForJS(lAvv.getEMail())%>','<%=StringUtils.cStrForJS(lAvv.getPec())%>',
				'<%=StringUtils.cStrForJS(lAvv.getCodiceFiscale())%>',
				'<%=StringUtils.cStrForJS(lAvv.getDescLuogoNascita())%>','<%=StringUtils.cStrForJS(lAvv.getCodStatoNascita())%>',
				'<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"dd"))%>',
				'<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"MM"))%>',
				'<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"yyyy"))%>',
				'<%=StringUtils.cStrForJS(lAvv.getDescrComuneStudio())%>','<%=StringUtils.cStrForJS(lAvv.getCodNonAttivita())%>');">
				<img align="middle" src="/images/fileselected.gif" border=0>
			</a>
		</td>
<%
	}
%>
	</tr>
<%
}
%>
</table>
</form>
</body>
</html>