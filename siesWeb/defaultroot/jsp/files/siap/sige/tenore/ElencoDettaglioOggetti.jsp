<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeEstesoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>

<jsp:useBean id="tenori" scope="request" class="java.util.Vector"/>
<jsp:useBean id="ProvvedimentoEvento"		scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />

<html>
<%
if (tenori != null &&  tenori.size() == 0) {
%>
        <td class="int" align="left">Non ci sono oggetti assegnati all'Atto.</td>
<%
} else {
	// 20190520 [SG]: aggiunto codice per gestire passaggio da TenoreSigeModel a TenoreSigeEstesoModel
	Object o = tenori.firstElement();
	   Iterator itx = tenori.iterator();
	   String lIdTenore = "";
	   String lCodTenore = "";
	while (itx.hasNext()) {
	    if (!(o instanceof TenoreSigeEstesoModel)) {
				TenoreSigeModel lTenore = (TenoreSigeModel)itx.next();
		  	lTenore.decodifica();  // 07/01/2010
				// Raggruppamento X ID Tenore
			if (lTenore != null && !lTenore.getIdTenoreSige().toString().equalsIgnoreCase(lIdTenore)) {
					lIdTenore = lTenore.getIdTenoreSige().toString();
					lCodTenore = lTenore.getCodOggettoSige();
%>
      <tr>
		<td class="L"><font class="label"><%=lTenore.getDescrOggettoSige()%></font></td>
  		</tr>
<%
 		} // endif
	    } else {
	    	TenoreSigeEstesoModel lTenore = (TenoreSigeEstesoModel) itx.next();
		  	lTenore.getTenoreSige().decodifica();  // 07/01/2010
			// Raggruppamento X ID Tenore
			if (lTenore != null
					&& lTenore.getTenoreSige() != null
					&& !lTenore.getTenoreSige().getIdTenoreSige().toString().equalsIgnoreCase(lIdTenore)) {
				lIdTenore = lTenore.getTenoreSige().getIdTenoreSige().toString();
				lCodTenore = lTenore.getTenoreSige().getCodOggettoSige();
%>
	<tr>
		<td class="L"><font class="label"><%=lTenore.getTenoreSige().getDescrOggettoSige()%></font></td>
	</tr>
<%
			} // endif
	    } // endelse
       } // endwhile
  }  // endif provvedimenti.size()
%>
</html>