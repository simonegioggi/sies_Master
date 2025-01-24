<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel" %>

<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="java.util.Iterator" %>

<jsp:useBean id="udienze" scope="request" class="java.util.Vector" />

<%
String lTipoRito = request.getParameter("tipoRito").trim();
String lDescrUdienze = "";
if (lTipoRito.compareTo("C")==0)
	lDescrUdienze="Collegiali";
if (lTipoRito.compareTo("M")==0)
	lDescrUdienze="Monocratiche";
// Flag presenza Numero Procedimenti per Udienza
boolean numPrc = false;
if (request.getAttribute("num_proc") != null)
	numPrc = true;
%>

<html>
<head>
	<title>[S.I.E.S.] - Elenco Udienze</title>
  	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
	<script language="JavaScript">
	<%-- 20171003: [SG] aggiunti parametri di passaggio e modificati i nomi dei parametri nella forma
	campoGG --> ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA, etc. --%>
	function insertIT(aValoreGG, aValoreMM, aValoreAA, aValoreLuogo, aIdUdienza, aNumCollegio, dataUdienza, codMagis, idSezione, tipoRito, listaIdUdienze) {
	
<%
if (request.getParameter("campoGG") != null) {
%>
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value=aValoreGG;
<%
}
if (request.getParameter("campoMM") != null) {
%>
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value=aValoreMM;
<%
}
if (request.getParameter("campoAA") != null) {
%>
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value=aValoreAA;
<%
}
if (request.getParameter("campoLuogo") != null) {
%>
		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA%>.value=aValoreLuogo;
<%
}
if (request.getParameter("campoID") != null) {
%>
	    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiUdienzaProcedimentoSige.CAMPO_UDI_ID_UDIENZA_SIGE%>.value=aIdUdienza;
<%
}
if (request.getParameter("campoColl") != null) {
%>
	    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value=aNumCollegio;
<%
}
// 20171002: [SG] gestione parametri di passaggio (dataUdienza, codMagis, idSezione, tipoRito)
%>
		window.parent.opener.document.<%=request.getParameter("formname")%>.dataUdienza.value=dataUdienza;
		window.parent.opener.document.<%=request.getParameter("formname")%>.codMagis.value=codMagis;
		window.parent.opener.document.<%=request.getParameter("formname")%>.idSezione.value=idSezione;
		// intervento per 11.2.1
		if(listaIdUdienze != null)
			window.parent.opener.document.<%=request.getParameter("formname")%>.listaIdUdienze.value=listaIdUdienze;
		
<%
if (request.getParameter("tipoRito") != null) {
%>
		window.parent.opener.document.<%=request.getParameter("formname")%>.tipoRito.value=tipoRito;
<%
}if (request.getParameter("campo_sub") != null) {
%>
		window.parent.opener.document.<%=request.getParameter("formname")%>.submit();
<%
}
%>
  		try {
  			window.parent.opener.loadAula (aIdUdienza);
  		} catch (e) {}
 		window.parent.close();
	}
	</script>
	</head>

	<body class="corpo">
  	<table>
    	<tr>
      		<td class="LBG">Elenco Udienze &nbsp;<%=lDescrUdienze%></td>
   		</tr>
  	</table>
	<%-- 20171003: restyling della pagina --%>
  	<table width="100%">
<%
// 20171003: se non ci sono udienze inserisco messaggio 
if (udienze.isEmpty()) {
%>
		<tr>
			<td class="c">
				Per la data selezionata non sono presenti Udienze.
			</td>
		</tr>
<%
} else {
%>
    	<tr>
			<td class="int">Data</td>
<%
if (lTipoRito.compareTo("C") == 0) {
%>
	      	<td class="int">Presidente</td>
	      	<td class="int">Consigliere/Giudice</td>
	      	<td class="int">Collegio e Sezione</td>
<%
} else {
%>
	      	<td class="int">Giudice Assegnatario</td>
	      	<td class="int">Procuratore</td>
<%
}
%>
      		<td class="int">Luogo Udienza</td>
      		<td class="int">Selezione</td>
    	</tr>
<%
	Iterator itx = udienze.iterator();
	while (itx.hasNext()) {
		UdienzaSigeModel lUdienzaSige = (UdienzaSigeModel) itx.next();
		if ((lTipoRito.compareTo("C") == 0 && lUdienzaSige.getColIdCollegio() != null)
	   			|| (lTipoRito.compareTo("M") == 0 && lUdienzaSige.getColIdCollegio() == null)) {
%>
      	<tr>
        	<td class="c">
        		<%=DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "dd-MM-yyyy")%> 
			</td>
<%
			if (lTipoRito.compareTo("C") == 0) {
%>
        	<td class="c">
<%
				if (lUdienzaSige.getCollegio() != null
						&& lUdienzaSige.getCollegio().getCollegioMagistrati() != null
						&& lUdienzaSige.getCollegio().getCollegioMagistrati().length > 0
						&& lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagistrato() != null) {
%>
				<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagistrato().getCognome() )%>
				&nbsp;
				<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagistrato().getNome() )%>
<%
				} else {
%>
				-
<%
				}
%>
        	</td>
        	<td class="c">
<%
				if (lUdienzaSige.getCollegio() != null
						&& lUdienzaSige.getCollegio().getCollegioMagistrati() != null
						&& lUdienzaSige.getCollegio().getCollegioMagistrati().length > 1
						&& lUdienzaSige.getCollegio().getCollegioMagistrati()[1].getMagistrato() != null) {
%>
				<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCollegioMagistrati()[1].getMagistrato().getCognome())%>
				&nbsp;
				<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCollegioMagistrati()[1].getMagistrato().getNome())%>
<%
				} else {
%>
				-
<%
				}
%>
        	</td>
        	<td class="c">
<%
				if (lUdienzaSige.getCollegio() != null) {
%>
				<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCodCollegio())%>&nbsp;&nbsp;<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getSezione().getDescrizione())%>
<%
				} else {
%>
				-
<%
				}
%>	  
        	</td>
<%
			} else {
%>
        	<td class="c"><%=StringUtils.toStringJSP(lUdienzaSige.getDescrGiudice())%></td>
       		<td class="c"><%=StringUtils.toStringJSP(lUdienzaSige.getDescrProcuratore())%></td>
<%
			}
%>
			<td class="c">&nbsp;<%=StringUtils.toStringJSP(lUdienzaSige.getLuogoUdienza())%></td>
        	<td class="c">
<%
//20171002: [SG] aggiunte impostazioni di parametri per gesione udienze
Date dataUdienza = lUdienzaSige.getDataUdienza();
//String codMagis = lUdienzaSige.getCodGiudice();
String codMagis = lUdienzaSige.getCodMagistratoAss();
BigDecimal idSezione = null;
if ("C".equals(lTipoRito) && lUdienzaSige.getCollegio() != null && lUdienzaSige.getCollegio().getSezione() != null){
	idSezione = lUdienzaSige.getCollegio().getSezione().getIdSezione();
} // 20171127: [EC] gestisco idSezione anche per Monocratico
else if("M".equals(lTipoRito) && lUdienzaSige.getCodIdSezioneUdienza() != null){
	idSezione = lUdienzaSige.getCodIdSezioneUdienza() ;
}

if (lUdienzaSige.getCollegio() != null) {
%>
				<a href="Javascript:insertIT('<%=DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "dd")%>',
                   	'<%=DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "MM")%>',
                   	'<%=DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "yyyy")%>',
                   	'<%=StringUtils.cStrForJS(lUdienzaSige.getLuogoUdienza())%>',
                   	'<%=lUdienzaSige.getIdUdienzaSige()%>',
                  	'<%=lUdienzaSige.getCollegio().getCodCollegio()%>',
                   	'<%=DateUtils.getDateToString(dataUdienza, "dd/MM/yyyy")%>',
                   	'<%=codMagis%>', '<%=StringUtils.toStringJSP(idSezione)%>', '<%=lTipoRito%>', '<%=lUdienzaSige.getListaIdUdienze()%>');">
<%
			} else {
%>
				<a href="Javascript:insertIT(
					'<%=DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "dd")%>',
                   	'<%=DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "MM")%>',
                   	'<%=DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "yyyy")%>',
                   	'<%=StringUtils.cStrForJS(lUdienzaSige.getLuogoUdienza())%>',
                   	'<%=lUdienzaSige.getIdUdienzaSige()%>',
                  	'null',
                   	'<%=DateUtils.getDateToString(dataUdienza, "dd/MM/yyyy")%>',
                   	'<%=codMagis%>', '<%=StringUtils.toStringJSP(idSezione)%>', '<%=lTipoRito%>' , '<%=lUdienzaSige.getListaIdUdienze()%>');">
<%
			}
%>
          			<img src="/images/fileselected.gif" border="0">
          		</a>
	        	<input type="HIDDEN" name='IdUdi' value='<%=lUdienzaSige.getIdUdienzaSige()%>'>
        	</td>
      	</tr>
<%
		}
	}
}
%>
	</table>
	</body>
</html>