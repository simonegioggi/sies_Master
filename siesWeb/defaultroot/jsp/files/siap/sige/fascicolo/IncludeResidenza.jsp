<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.detenzione.model.FasSigeDetenzioneModel"%>
<%@ page import="siap.sico.residenza.model.ResidenzaModel"%>

<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>

<%
String isVALIGN = "top";

	if (FascicoloSigeEsteso != null) {
%>
	    <tr>
	       <td ><font class="label">Residenza:</font>
	<%
	if (FascicoloSigeEsteso.getResidenza() != null) {
		ResidenzaModel lRes = FascicoloSigeEsteso.getResidenza();
	  	if ("039".compareTo(lRes.getCodStato()) == 0) {
	%>
			<font class="campo"><%=lRes.getDescrComune()%> (<%=lRes.getCodProvincia()%>) </font>
			<font class="label"> - </font>
			<font class="campo"><%=lRes.getIndirizzo()%> </font>
		<% } else {
			if (lRes.getDescComuneEstero() != null && !"-".equals(lRes.getDescComuneEstero())) {
		%>
			<font class="campo"><%=lRes.getDescComuneEstero()%> (<%=lRes.getDescrStato()%>)</font>
			<font class="label"> - </font>
			<% } %>
			<font class="campo"><%=lRes.getIndirizzo()%> </font>
		<% } %>
	<%    		
		} else {
	 %>
	    <font class="label"> - </font>
	<%  } // endif Residenza() != null
	%>
	    </tr>
	    <tr>
	    <td><font class="label">Domicilio:</font>
<%
        String indirizzoDomicilio = "<font class=\"campo\"> - </font>";
	    if (FascicoloSigeEsteso.getDomicilio() != null) {
		  	ResidenzaModel lDom = FascicoloSigeEsteso.getDomicilio();
		  	if ("S".equalsIgnoreCase(lDom.getFlgDomicilioDifensore())) {
		  		indirizzoDomicilio= "<font class=\"campo\">Presso il Difensore (ex. art. 161 c.p.p.)</font>";
		  	}
		  	if (!"S".equalsIgnoreCase(lDom.getFlgDomicilioDifensore()) && "039".equals(lDom.getCodStato())) {
		  		boolean isDescrizioneComunePresente = (lDom.getDescrComune() != null && !"-".equals(lDom.getDescrComune()));
		  		indirizzoDomicilio = (isDescrizioneComunePresente ? "<font class=\"campo\">" + lDom.getDescrComune() + " (" + lDom.getCodProvincia() + ") </font>" : "");
		  		indirizzoDomicilio += (lDom.getIndirizzo() != null ? "<font class=\"label\"> - </font> <font class=\"campo\">" + lDom.getIndirizzo() + "</font>" : "");
		  	}
		  	if (!"S".equalsIgnoreCase(lDom.getFlgDomicilioDifensore()) && !"039".equals(lDom.getCodStato())) {
		  		boolean isDescrizioneComunePresente = (lDom.getDescComuneEstero() != null && !"-".equals(lDom.getDescComuneEstero()));
		  		indirizzoDomicilio = (isDescrizioneComunePresente ? "<font class=\"campo\">" + lDom.getDescComuneEstero() + " ("+lDom.getDescrStato()+") </font>)" : "");
		  	}
	    }
	    if ("".equals(indirizzoDomicilio))
	    	indirizzoDomicilio= "<font class=\"campo\"> - </font>";
%>
 		 <%= indirizzoDomicilio %> 	
        </td>		  	
	    </tr>
<%
    } // endif FascicoloSigeEsteso
%>