<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige" %>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel" %>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige" %>

<jsp:useBean id="udienze" 			scope="request" class="java.util.Vector" />
<jsp:useBean id="tipo" 				scope="request" class="java.lang.String" />
<jsp:useBean id="tiporicerca" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProc" 			scope="request" class="java.lang.String"/>
<%-- 20170908: [SG] aggiunti useBean --%>
<jsp:useBean id="lStatoProcedimento" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoRito" 			scope="request" class="java.lang.String"/>

<%
String lTipoRito = "C";
String lDescrUdienze = "Collegiale";
// 20171002: [SG] aggiunto controllo sul Tipo rito (ricerche per riti monocratici)
if ("M".equals(tipoRito)) {
	lTipoRito = "M";
	lDescrUdienze = "Monocratica";
}
%>
<html>
<head>
  <title>[S.I.E.S.] - Visualizza Procedimenti fissati per Udienza</title>
  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
</head>

<body class="corpo" >
	<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaSelezioneProcedimentixUdienza">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.udienzaprocedimento.action.ActRicercaUdienzaProcedimento">
  	<table width=100%>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
    		</td>
      	<td class="LBG">
      		<font class="label">Funzione:</font>&nbsp;<font class="campo">Visualizza Procedimenti fissati per Udienza</font>
      	</td>
    	</tr>
  	</table>
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
	if (lTipoRito.compareTo("C")==0) {%>
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
    	if ((lTipoRito.compareTo("C")==0 && lUdienzaSige.getColIdCollegio() != null)
    			|| (lTipoRito.compareTo("M")==0 && lUdienzaSige.getColIdCollegio() == null)) {
%>
      	<tr>
        	<td class="c">
        		<%=DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "dd-MM-yyyy")%> 
					</td>
<%
					if (lTipoRito.compareTo("C")==0) { %>
        	<td class="c">
					<%if (lUdienzaSige.getCollegio() != null && 
					    	lUdienzaSige.getCollegio().getCollegioMagistrati() != null && 
					    	lUdienzaSige.getCollegio().getCollegioMagistrati().length > 0 &&
					    	lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagistrato() != null) 
						{
					%>
        			<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagistrato().getCognome() )%>
							&nbsp;
							<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagistrato().getNome() )%>
      		<%}else{%>-<%}%>
        	</td>
        	<td class="c">
					<%if (lUdienzaSige.getCollegio() != null && 
					    	lUdienzaSige.getCollegio().getCollegioMagistrati() != null && 
					    	lUdienzaSige.getCollegio().getCollegioMagistrati().length > 1 && 
					    	lUdienzaSige.getCollegio().getCollegioMagistrati()[1].getMagistrato() != null) 
						{
					%>
        			<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCollegioMagistrati()[1].getMagistrato().getCognome() )%>
							&nbsp;
							<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCollegioMagistrati()[1].getMagistrato().getNome() )%>
      		<%}else{%>-<%}%>
        	</td>

        	<td class="c">
						<%if(lUdienzaSige.getCollegio() != null){
						%>
        			----<%-- <%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getCodCollegio())%>&nbsp;&nbsp;<%=StringUtils.toStringJSP(lUdienzaSige.getCollegio().getSezione().getDescrizione())%> --%>
        		<%}else{%>-<%}%>	  
        	</td>
			<%}else{%>
        	<td class="c"><%=StringUtils.toStringJSP(lUdienzaSige.getDescrGiudice())%></td>
       		<td class="c"><%=StringUtils.toStringJSP(lUdienzaSige.getDescrProcuratore())%></td>
			<%}%>
        	<td class="c">&nbsp;<%=StringUtils.toStringJSP(lUdienzaSige.getLuogoUdienza())%></td>
<%-- 20170908: [SG] aggiunti parametri di passaggio --%>
<%
BigDecimal idSezione = null;
if ("C".equals(tipoRito) && lUdienzaSige.getCollegio() != null && lUdienzaSige.getCollegio().getSezione() != null){
	idSezione = lUdienzaSige.getCollegio().getSezione().getIdSezione();
}// 20171127: [EC] gestisco idSezione anche per Monocratico
else if ("M".equals(tipoRito) && lUdienzaSige.getCodIdSezioneUdienza()!= null ){
	idSezione = lUdienzaSige.getCodIdSezioneUdienza();
}
%>
        	<td class="c">
        		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.udienzaprocedimento.action.ActRicercaUdienzaProcedimento&tipo=<%=tipo%>&<%=ICostantiUdienzaProcedimentoSige.CAMPO_UDI_ID_UDIENZA_SIGE%>=<%=lUdienzaSige.getIdUdienzaSige()%>&tiporicerca=<%=tiporicerca%>&tipoProc=<%=tipoProc%>&statoProc=<%=lStatoProcedimento%>&dataUdienza=<%=DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "dd/MM/yyyy")%>&codMagis=<%=lUdienzaSige.getCodMagistratoAss()%>&idSezione=<%=StringUtils.toStringJSP(idSezione)%>&tipoRito=<%=tipoRito%>&listaIdUdienze=<%=lUdienzaSige.getListaIdUdienze()%>">
          	<img align="middle" src="/images/fileselected.gif" border="0"></a>
        	</td>
        	<input type="HIDDEN" name='IdUdi' value='<%=lUdienzaSige.getIdUdienzaSige()%>'>
      	</tr>
<%
		}
	}
}
%>
	</table>
	</form>
</body>
</html>