<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.List"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sius.udienza.model.UdienzaModel"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sius.generaleprocedimento.action.ICostantiGeneraleProcedimento"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento"%>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato" %>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="modalita"      	scope="request" class="java.lang.String"/>
<jsp:useBean id="udienza"       	scope="request" class="siap.sius.udienza.model.UdienzaModel"/>
<jsp:useBean id="evento"        	scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="tipoAutorita"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="codOggetti"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="descOggetti"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="sudienze"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="tenori"    scope="request" class="java.util.Vector"/>
<jsp:useBean id="depositoDecretoMotivazioni" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>

<%
	EventoModel lEve = evento;
%>

<html>
	<head>
	<% 
		String lTitle = new String();
		if (lEve.getCodTipoProvvedimento().equals("50"))
			lTitle = "Dettaglio Verbale Rinvio Udienza";
		else if (lEve.getCodTipoProvvedimento().equals("01"))
			lTitle = "Dettaglio Sentenza Rinvio Udienza";
		else
		  	lTitle = "Dettaglio Ordinanza Rinvio Udienza";
	%>
		<title>[S.I.E.S.] - <%=lTitle %> </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js"></script>
		<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
		<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
		<script language="JavaScript">
	    	function lookUpload() {
	      		var node;
	     	 	node = document.getElementById('upld');
				node.style.visibility='visible';
	    	}
	  	</script>
	</head>

	<body class="corpo">
  		<table>
    		<tr>
    			<td class="LBG">
    				<a href="Javascript:window.print();">
    					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
    				</a>
    			</td>
      			<td class="LBG">
      				<font class="label"> Funzione :</font>&nbsp;
        			<font class="campo"> <%=lTitle %></font>
      			</td>
<%
if (request.getAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO) != null) {
%>
				<jsp:include page="<%=ICostantiUdienza.PG_BUTTONS%>">
  					<jsp:param name="CampoIdEntita" value="<%=ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO%>" />
  					<jsp:param name="ValoreIdEntita" value="<%=request.getAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO)%>" />
  				</jsp:include>
<%
}
%>
<%
// 20080125 - Non è richiesta la stampa per verbale rinvio udienza
if (!lEve.getCodTipoProvvedimento().equals("50")) {
    if (lEve.getFlagDocumentoRegistrato() == null ||
    		lEve.getFlagDocumentoRegistrato().compareTo("N") == 0) {
%>
    			<!-- BOTTONE DI STAMPA -->
    			<!-- MERGE v10: aggiunto parametro -->
    			<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
    				<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
      				<jsp:param name="ValoreIdEntita" value="<%=lEve.getIdEvento()%>" />
      				<jsp:param name="CodTipoProvvedimento" value="<%=lEve.getCodTipoProvvedimento()%>" />
    			</jsp:include>
<%
	}
}
%>
				<!-- BOTTONE DI RITORNO -->
    			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    		</tr>
    		<tr>
       			<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    		</tr>
		</table>

  		<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="DettaglioVerbaleRinvioUdienza">
  			<table cellspacing=2 cellpadding=2>
  				<tr>
    				<td class="Titolo" colspan=2> Oggetti</td>
  				</tr>
<%
// genny 02/03/2004
Iterator lInd = tenori.iterator();
while (lInd.hasNext()) {
%>
   				<tr>
<%
	TenoreModel lTen = (TenoreModel) lInd.next();
%>
      				<td class="l"><%=lTen.getDescrOggettoTenore()%></td>
  				</tr>
<%
}
%>
  				<tr>
    				<td colspan=2> &nbsp;</td>
  				</tr>

  				<tr>
				  	<td class="l"> Data Emissione</td>
				    <td class="L" colspan="5">
				    <font class="campo">
				    	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getDataEmissione(), "dd-MM-yyyy"), "-")%>
				    </font>
			  	</tr>
  
			  	<tr>
				  	<td class="l">Nuova Data Udienza</td>
				    <td class="L" colspan="5">
   						<font class="campo">
<%
if (fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("10")==0) {
%>
	Rinviato a nuovo Ruolo
<%
} else {
%>
	<%=StringUtils.toStringJSP(DateUtils.getDateToString( udienza.getDataUdienza(), "dd-MM-yyyy"), "-")%>
<%
}
%>
    					</font>
    				</td>
				</tr>
				<tr>
				    <td class="l">
				    	<input value="<%= udienza.getIdUdienza()%>" type="HIDDEN" name="<%= ICostantiUdienza.CAMPO_ID_UDIENZA%>" maxlength="20" size="20">
				  	</td>
			  	</tr>
     			<tr>
			      	<td class="l">Luogo Svolgimento Udienza</td>
			      	<td class="L" colspan="5">
			        	<%--font class="campo"><%=StringUtils.toStringJSP(fascicoloSiusGP.getGeneraleProcedimentoModel().getAnnotazione())%></font--%>
			      		<font class="campo"><%=StringUtils.toStringJSP(udienza.getLuogoUdienza())%></font>&nbsp;
			      	</td>
				</tr>
<%
String  dateUdienzaPrecedenti = new String();
// N.B. "sudienze"
dateUdienzaPrecedenti = sudienze;
// dateUdienzaPrecedenti = RicercaUdienzePrecedenti;
%>
     			<tr>
      				<td class="l">Date Udienze Precedenti: </td>
      				<td class="L" colspan="5">
        				<font class="campo"><%=dateUdienzaPrecedenti%></font>&nbsp;
      				</td>
    			</tr>
			    <%--tr>
			      	<td>
			        	<input class="bottone" type="submit" value="Conferma">
			      	</td>
			    </tr--%>
    		</table>
    		<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" value="<%=codOggetti%>">
    		<input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>">
  		</form>

 		<div align=left style="visibility:hidden" id="upld">
  			<FORM name="comandi" enctype="multipart/form-data" method="post">
  				<table>
          			<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
    				<tr>
      					<td class="L">
				       		<input class="bottone"  type="submit" value="Conferma">
				       		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
				       		<input type="HIDDEN" name="IdEvento"  value="<%=lEve.getIdEvento()%>">
				       		<input type="HIDDEN" name="<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>" value="<%=udienza.getIdUdienza()%>">
				       		<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.udienza.action.ActDettaglioVerbaleRinvioUdienza">
				      		<input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.ORDINANZA_RINVIO_UDIENZA%>">
				      	</td>
					</tr>
				</table>
  			</FORM>
  		</div>
  	</body>
</html>