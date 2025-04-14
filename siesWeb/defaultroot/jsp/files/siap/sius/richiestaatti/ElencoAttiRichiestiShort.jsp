<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.security.model.FunctionModel"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.model.FunzioneModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="atti"            scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>

<html>
<table width="100%">
<%
if (atti.size() == 0) {
%>
	<tr><td class="int" align="left"> Non ci sono richieste di atti per il procedimento.</td></tr>
<%
} else {
%>
	<tr>
		<td class="int" width="30%">Tipo atto istruttorio richiesto</td>
		<td class="int" width="40%">Destinatario</td>
		<td class="int" width="15%">Data richiesta</td>
		<td class="int" width="15%">Data restituzione</td>
	</tr>
<%
	Iterator itx = atti.iterator();
    while (itx.hasNext()) {
		NotificaModel atto = (NotificaModel) itx.next();
%>
	<tr>
		<td class="L"><font class="campo"><%=atto.getDescrizione()%></font></td>
		<td class="L"><font class="campo">
<%
    	// UFFICIO
    	if (atto.getUfficio() != null) {
%>
			<%=atto.getUfficio().getDescrTipoUfficio()%>&nbsp;di&nbsp;<%=atto.getUfficio().getDescrComune()%>
<%
    	} else if (atto.getAutoritaEsterna() != null) { // Autorità Esterna
%>
			<%=atto.getAutoritaEsterna().getDescrTipoAutorita()%>&nbsp;di&nbsp;<%=atto.getAutoritaEsterna().getDescrSede()%>
<%
    	} else if (atto.getAvvIdAvvocatoFascicoloSiep() != null) { // Avvocato SIEP
      		if (atto.getAvvSiep() != null) {
%>
			Avv.&nbsp;<%=StringUtils.toStringJSP(atto.getAvvSiep().getAvvocato().getCognome() + " " + atto.getAvvSiep().getAvvocato().getNome())%>
<%
      		}
    	} else if (atto.getAvvIdAvvocatoFascicoloSius() != null) { // Avvocato SIUS
      		if (atto.getAvvSius() != null) {
%>
            Avvocato&nbsp;<%=StringUtils.toStringJSP(atto.getAvvSius().getAvvocato().getCognome() +" "+atto.getAvvSius().getAvvocato().getNome())%>
<%
      		}
    	} else if (atto.getCssIdCssa() != null) { // UEPE
      		if (atto.getCSSA() != null) {
%>
            UEPE&nbsp;<%=StringUtils.toStringJSP(atto.getCSSA().getIndirizzo() + " " + atto.getCSSA().getComune())%>&nbsp;
<%
      		}
    	}
%>
			</font>
		</td>
      	<td class="L">
      		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(atto.getDataInvio(), "dd-MM-yyyy"), "-")%></font>
      	</td>
      	<td class="L">
      		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(atto.getDataAvvenutaNotifica(), "dd-MM-yyyy"), "-")%>
<%
		String isBlob = "SI";
		if (atto.getFlagDocRegistrato() == null) {
			isBlob = "NO";
		}
%>
      		</font>
      	</td>
	</tr>
<%
	}
}
%>
</table>
</html>