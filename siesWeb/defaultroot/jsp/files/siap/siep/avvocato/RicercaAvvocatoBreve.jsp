<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="avvocato" scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<html>
<head>
	<title>[S.I.E.S.] - Lista Avvocati</title>
	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <% if (! modalita.equals("NoPop"))
  {
  %>
	<script language="JavaScript">
	function insertIT(id,cognome,nome,foro,indirizzo,telefono,fax,email,pec,codicefiscale,luogoNascita,nazione,giornoNascita,meseNascita,annoNascita,descComuneStudio,stato) {
	   	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value = id;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value = cognome;
		if (nome == "-") {
	 			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value = "";
		} else {
	 			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value = nome;
		}
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FORO%>.value = foro;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>.value = indirizzo;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_TELEFONO%>.value = telefono;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FAX%>.value = fax;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_E_MAIL%>.value = email;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_PEC%>.value = pec;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>.value = codicefiscale;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.value = nazione;
		if (nazione == "039") {
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value = luogoNascita;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>.value = "";
		} else {
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value = "";
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>.value = luogoNascita;
		}
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value = giornoNascita;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value = meseNascita;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value = annoNascita;

		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>.value = descComuneStudio;
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>.value = stato;

	   	window.parent.close();
	}
	
	</script>
 <%
  }
 %>
	<script language="JavaScript">

 function avvocati()
    {
      if ("<%=avvocato.size()%>" == 0)
        alert('Attenzione! Nessun Difensore trovato.');
      if ("<%=avvocato.size()%>" == 200)
        alert('Attenzione! Visualizzati solo i primi 200 Difensori individuati. Perfezionare la ricerca!');
    }
	</script>

</head>

<body class=corpo onload="avvocati();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Difensori certificati Reginde presenti in SIES</font></td>
    </tr>
  </table>
 <% if (modalita.equals("NoPop"))
  {
  %>
 <BR>
 <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <BR>
  <% } %>
<%if(avvocato.size()>0){%>
 <Table width="100%">
   <tr>
		<td class="int">Cognome e Nome</td>
		<td class="int">Codice Fiscale</td>
		<td class="int">Foro</td>
		<td class="int">Luogo e Data Nascita</td>
		<td class="int">Indirizzo Studio</td>
		<td class="int">Stato</td>
<% if (! modalita.equals("NoPop")) { %>
    	<td class=int width=10%>Seleziona</td>
<% } %>
  </tr>
 <%
	int id_record = 0;
	Iterator itx = avvocato.iterator();
	boolean testAvvocatiValidi = false;
  	while ( itx.hasNext())
  	{
    	AvvocatoModel lAvv = (AvvocatoModel)itx.next();
	%>
	<tr>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class=l><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%></td--%>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getCognome()) + " " + StringUtils.toStringJSP(StringUtils.toStringJSP(lAvv.getNome()))%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getCodiceFiscale())%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getForo())%></td>
		<!-- Luogo e Data Nascita -->
		<td class=l><%=StringUtils.toStringJSP(lAvv.getDescLuogoNascita())%>,&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvv.getDataNascita(),"dd-MM-yyyy"))%></td>
		<!-- Indirizzo Studio -->
    	<td class=l><%=StringUtils.toStringJSP(lAvv.getIndirizzo())%></td>
		<!-- Stato Avvocato   -->
    	<td class=l><%=StringUtils.toStringJSP(lAvv.getDescrNonAttivita())%></td>

        <% if (! modalita.equals("NoPop"))
        { %>
        <td class=c>
        	<a href="Javascript:insertIT(
        		'<%=lAvv.getIdAvvocato()%>','<%=StringUtils.cStrForJS(lAvv.getCognome())%>',
        		'<%=StringUtils.cStrForJS(lAvv.getNome())%>','<%=StringUtils.cStrForJS(lAvv.getForo())%>',
				'<%=StringUtils.cStrForJS(lAvv.getIndirizzo())%>','<%=StringUtils.cStrForJS(lAvv.getTelefono())%>',
				'<%=StringUtils.cStrForJS(lAvv.getFax())%>','<%=StringUtils.cStrForJS(lAvv.getEMail())%>',
				'<%=StringUtils.cStrForJS(lAvv.getPec())%>','<%=StringUtils.cStrForJS(lAvv.getCodiceFiscale())%>',
				'<%=StringUtils.cStrForJS(lAvv.getDescLuogoNascita())%>','<%=StringUtils.cStrForJS(lAvv.getDescrStatoNascita())%>',
				'<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"dd"))%>',
				'<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"MM"))%>',
				'<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"yyyy"))%>',
				'<%=StringUtils.cStrForJS(lAvv.getDescrComuneStudio())%>','<%=StringUtils.cStrForJS(lAvv.getDescrNonAttivita())%>');">
				<img align="middle" src="/images/fileselected.gif" border="0" style="vertical-align: super;" alt="Inserisci">
			</a>

        <% } %>
        </tr>
	<%
	}
%>
</table>
<%}%>
</body>
</html>