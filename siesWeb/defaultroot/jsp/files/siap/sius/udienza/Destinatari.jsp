<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<jsp:useBean id="notifiche" scope="request" class="java.util.Vector"/>
<jsp:useBean id="curatore"  scope="request" class="siap.sius.curatore.model.CuratoreSiusModel"/>

<%-- MEV_39: Scrivo "Destinatari" <=> esistono! --%>
<%
Iterator itx2 = notifiche.iterator();
int count = 0;
while (itx2.hasNext()) {
	boolean test = false;
    NotificaModel notifica = (NotificaModel) itx2.next();
    if (count == 0) {
%>
<table cellspacing="2" cellpadding="2" width="95%">
  	<tr>
    	<td class="Titolo" colspan="2">Destinatari</td>
  	</tr>
<%
    }
    count++;
%>
	<tr>
<%
	if (notifica.getUfficio() != null) { // UFFICIO
%>
        <td class="l">
        	<font class="campo"><%=notifica.getUfficio().getDescrTipoUfficio()%></font>
        	&nbsp;di&nbsp;
        	<font class="campo"><%=notifica.getUfficio().getDescrComune()%></font>
        </td>
<%
		if (notifica.getNote() != null) {
			test = true;
%>
 		<td class="l"><%=notifica.getNote()%></td>
<%
		}
	} else if (notifica.getAutoritaEsterna() != null) { // Autorità Esterna
%>
		<td class="l">
			<font class="campo"><%=notifica.getAutoritaEsterna().getDescrTipoAutorita()%></font>
<%
		if (notifica.getAutoritaEsterna().getDescrSede() != null
				&& !notifica.getAutoritaEsterna().getDescrSede().equals("")
       	   		&& !notifica.getAutoritaEsterna().getDescrSede().equals("-")) {
%>
          	&nbsp;di&nbsp;
          	<font class="campo"><%=notifica.getAutoritaEsterna().getDescrSede()%></font>
<%
		}
%>
          	&nbsp;&nbsp;
<%
		if (notifica.getNote() != null) {
%>
            <font class="campo">-&nbsp;&nbsp;<%=notifica.getNote() %></font>
<%
		}
%>
		</td>
<%
	} else if (notifica.getIstitutoDetenzione() != null) { // Istituto Detenzione
%>
		<td class="l">
			<font class="campo"><%=notifica.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
          	&nbsp;di&nbsp;
          	<font class="campo"><%=notifica.getIstitutoDetenzione().getDescrComune()%></font>
		</td>
<%
	}
	if (notifica.getSogIdSoggetto() != null) { // SOGGETTO
%>
		<td class="l">Per la notifica al Soggetto </td>
<%
   	} else if (notifica.getAvvIdAvvocatoFascicoloSiep() != null) { // Avvocato SIEP
       	if (notifica.getAvvSiep() != null) {
%>
		<td class="l">Per la notifica all' Avvocato &nbsp;<%=notifica.getAvvSiep().getAvvocato().getDescrTipo()%>&nbsp;:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(notifica.getAvvSiep().getAvvocato().getCognome() +" "+notifica.getAvvSiep().getAvvocato().getNome())%></font></td>
<%
		}
   	} else if (notifica.getAvvIdAvvocatoFascicoloSius() != null) { // Avvocato SIUS
       	if (notifica.getAvvSius() != null) {
%>
		<td class="l">Per la notifica all' Avv. <font class="campo"><%=StringUtils.toStringJSP(notifica.getAvvSius().getAvvocato().getCognome() +" "+notifica.getAvvSius().getAvvocato().getNome())%></font>&nbsp; </td>
<%
		}
   	} else if (notifica.getCurIdCuratore() != null) { // Curatore/Tutore
   		if (curatore != null && curatore.getCuratore() != null && curatore.getCuratore().getCognome() != null) {
%>
		<td class="l">Per la notifica al <%=curatore.getDescrTipo().toLowerCase()%>&nbsp; <font class="campo"><%=StringUtils.toStringJSP(curatore.getCuratore().getCognome() +" "+curatore.getCuratore().getNome())%></font>&nbsp; </td>
<%
		}
	} else if (notifica.getCssIdCssa() != null) { // UEPE
       	if (notifica.getCSSA() != null) {
%>
		<td class="l" colspan="2"><font class="campo">UEPE &nbsp;</font>
			<font class="campo"><%=StringUtils.toStringJSP(notifica.getCSSA().getIndirizzo() +" "+notifica.getCSSA().getComune())%></font>&nbsp;
		</td>
<%
		}
	} else if (!test) {
%>
		<td class="l">&nbsp;</td>
<%
	}
%>
	</tr>
<%
}
%>
  	<tr>
      	<td colspan="2">&nbsp;</td>
  	</tr>
</table>