<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.detenzione.model.FasSigeDetenzioneModel"%>

<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>

<%
	if (FascicoloSigeEsteso != null && FascicoloSigeEsteso.getDetenzione() != null) {
		FasSigeDetenzioneModel lDetenzione = FascicoloSigeEsteso.getDetenzione();
		String luogoDetenzione = "";
		if (lDetenzione != null) {
    		// Dettaglio del luogo detenzione.
			if ((lDetenzione.getLuogoDetenzione() != null) && (lDetenzione.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null)) {
				// MERGE v10 COLLAUDO: aggiunta descrizione altro luogo se presente
				String altroLuogo = Utils.isPresent(lDetenzione.getLuogoDetenzione().getAltroLuogo()) ? " " + lDetenzione.getLuogoDetenzione().getAltroLuogo() : "";
				luogoDetenzione = lDetenzione.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto()+" - "+lDetenzione.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune() + altroLuogo;
			} else if ((lDetenzione.getAltraCausa()!= null) &&   lDetenzione.getAltraCausa().getIstDetIdIstitutoDetenzione() != null) {
				luogoDetenzione = lDetenzione.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto()+" - "+lDetenzione.getAltraCausa().getIstitutoDetenzione().getDescrComune();
			}
		} // endif dettaglio
    	// Gestione del luogo detenzione.
    	if (luogoDetenzione.trim().length() > 0) {
%>
      		<tr>
        		<td>
          			<font class="label">Detenuto in &nbsp;&nbsp;</font>
          			<font class="campo"><%=luogoDetenzione%></font>
         		</td>
            </tr>
        		<% if (lDetenzione.getLuogoDetenzione() != null && lDetenzione.getLuogoDetenzione().getDataFineDetenzione() != null) { %>
        			<tr>
	        			<td>
	        				<font class="label">Data Fine Detenzione &nbsp;&nbsp;</font>
	        				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(FascicoloSigeEsteso.getDetenzione().getLuogoDetenzione().getDataFineDetenzione(),"dd-MM-yyyy"),"-")%></font>
	        			</td>
	        		</tr>
        		<% } %>
<%
    	}
	}
%>