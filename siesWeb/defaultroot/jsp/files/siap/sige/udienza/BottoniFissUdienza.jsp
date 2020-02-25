<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="f3b.security.model.FunctionModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.log.LogF3B"%>

<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<jsp:useBean id="Inseribile" scope="request" class="java.lang.String" />
<jsp:useBean id="Modificabile" scope="request" class="java.lang.String" />
<jsp:useBean id="Cancellabile" scope="request" class="java.lang.String" />

<%
	if (Inseribile.length() == 0 || Inseribile.equals(null) || Inseribile.toLowerCase().compareTo("null") == 0)
		Inseribile = "SI";
	
	if (Modificabile.length() == 0 || Modificabile.equals(null) || Modificabile.toLowerCase().compareTo("null") == 0)
		Modificabile = "SI";
	
	if (Cancellabile.length() == 0 || Cancellabile.equals(null) || Cancellabile.toLowerCase().compareTo("null") == 0)
		Cancellabile = "SI";

	Collection lFunFiglie = (Collection) request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

	//Visualizzazione dei bottoni di Stampa
	if ((lFunFiglie != null) && (lFunFiglie.size() != 0)) {
		Iterator lIterBottoni = lFunFiglie.iterator();

		FunctionModel lFun = null;
		while (lIterBottoni.hasNext()) {
			lFun = (FunctionModel) lIterBottoni.next();
			if (lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)) {
				if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) || lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO)) {
					
					if (Modificabile.compareToIgnoreCase("SI") == 0) {
%>
<!-- BOTTONE DI RIFISSAZIONE UDIENZA -->
<td class="LBG">
	<!-- // Modifica del 24/02/2016 Nuova Infrastruttura - INIZIO ******  -->
	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=request.getParameter("CampoIdEntitaEve")%>=<%=request.getParameter("ValoreIdEntitaEve")%>&ritorno=si&TornaQui=<%=TornaQui%>">
	<!-- // Modifica del 24/02/2016 Nuova Infrastruttura - FINE ******  -->
	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Udienza" width="24" height="24" border="0">
	</a>
</td>
<%
					} else if (Inseribile.compareToIgnoreCase("SI") == 0) {
%>
<!-- BOTTONE DI INSERIMENTO UDIENZA -->
<td class="LBG">
	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&inserisci=si&ritorno=si&TornaQui=<%=TornaQui%>">
	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserimento Udienza" width="24" height="24" border="0">
	</a>
</td>
<%
					}
					
				} else if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)) {
					
					if (Cancellabile.compareToIgnoreCase("SI") == 0) {
%>
<!-- BOTTONE DI ANNULLAMENTO FISSAZIONE UDIENZA -->
<td class="LBG">
	<a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','TornaQui','<%=TornaQui%>');">
	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Annulla Fissazione Udienza" width="24" height="24" border="0">
	</a>
</td>
<%
					}
					
				}
			}

		} // endwhile

	}
%>