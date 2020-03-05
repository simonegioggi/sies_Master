<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV 16: aggiunti import --%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="siap.siep.modulocumulo.util.ModuloCumuloUtils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.security.model.FunctionModel"%>
<%@ page import="f3b.util.F3BProperties"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.evento.action.ActGestisciButtonsProvvedimento"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp"%>
<%@ page import="siap.util.SIESSwitch"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>

<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>

<table>
	<tr>
<%
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
String lModificabile = "SI";
if (request.getParameter("Modificabile") != null) {
	lModificabile = request.getParameter("Modificabile");
}
String lEventoCancellareAnnullare = null;
if (request.getParameter("EventoCancellareAnnullare") != null) {
    lEventoCancellareAnnullare = request.getParameter("EventoCancellareAnnullare");
}
String lOrdinamento = null;
if (request.getParameter("lOrdinamento") != null) {
    lOrdinamento = request.getParameter("lOrdinamento");
}
Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
// Visualizzazione dei bottoni
if ((lFunFiglie != null) && (lFunFiglie.size() != 0)) {
    Iterator lIterBottoni = lFunFiglie.iterator();
    FunctionModel lFun = null;
    while(lIterBottoni.hasNext()) {
		lFun = (FunctionModel)lIterBottoni.next();
		// ANNA per errore di doppia visualizzazione dettaglio
		if (lFun.getFunctionId().toString().trim().compareTo("31130592") != 0)
			if (lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)) {
	  			if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO)) {
      				if (SIESSwitch.isReworkDettaglio()) {
			    	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			    	  siesLogger.debug("Rework Dettaglio");
         				ActGestisciButtonsProvvedimento lAction = new ActGestisciButtonsProvvedimento();
         				boolean lDett = lAction.isDettaglioVisualizzabile(request.getParameter("TipoProvvedimento"),
																         request.getParameter("MotivoEvento"),
																         request.getParameter("TemIdTemplate"));
        				if (lDett) {
							// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.debug("Dettaglio Visualizzabile");
							// Pre prendere l'action devo anche inviare il fatto che sia registrato o meno
							String lActDettaglio = lAction.getActionDettaglioProvvedimento(request.getParameter("MotivoEvento"),
																				           request.getParameter("TipoEvento"),
																				           request.getParameter("TipoProvvedimento"),
																				           request.getParameter("TemIdTemplate"),
																				           request.getParameter("docRegistrato"));
							// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.debug("Azione -> " + lActDettaglio );
							if (lActDettaglio.length() > 1) {
%>
		<td>
		  	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&modifica=R<%=retParam%>">
		    	<img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
		  	</a>
		</td>
<%
           					}
       					}
    				} else { //Non siamo in REWORK DETTAGLIO funziona tutto come prima
      					if (request.getParameter("docRegistrato") == null
      							|| request.getParameter("docRegistrato").equals("N")) {
							// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.debug("Documento non registrato");
							ActGestisciButtonsProvvedimento lAction = new ActGestisciButtonsProvvedimento();
							String lActDettaglio = lAction.getActionDettaglioProvvedimento(request.getParameter("MotivoEvento"),
									request.getParameter("TipoEvento"));
							if (lActDettaglio.length() > 1) {
%>
		<td>
		  	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&modifica=R<%=retParam%>">
		    	<img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
		  	</a>
		</td>
<%	      
							}
	    				} else {
							// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.debug("Documento registrato");
							String lActDettaglio = "siap.sico.evento.action.ActDettaglioDocumento";
%>
		<td>
		  	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
		    	<img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
		  	</a>
		</td>
<%
	      				}
      				}
	  			}
    			if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) && (lModificabile.equals("SI"))) {
%>
		<td>
		  	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
		    	<img src="/images/esegui.gif" alt="Inserimento" width="12" height="12" border="0">
		  	</a>
		</td>
<%
    			}
	  			// Altro Tipo
	  			if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && (lModificabile.equals("SI"))) {
%>
	    <td>
	      	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
				<img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
	      	</a>
	    </td>
<%
	  			}
	  			String lTornaNonValidati = "";
	  			if ("NV".equals( request.getParameter("modalita"))) {
	    			lTornaNonValidati = "&TornaNonValidati=NV";
	  			}
	  			// Inserimento pulsante per invocare la funzione per la Modifica del Magistrato Firmatario
	  			if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_VALIDAZIONE)
	  					&& request.getParameter("Evento").equals("SI")
	  					&& request.getParameter("docRegistrato").equals("N") && lModificabile.equals("SI")) {
%>	  
		<td>
		   	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=lTornaNonValidati%>">
		  		<img src="/images/modifica_pm_e_valida.gif" alt="Modifica PM e Valida" width="12" height="12" border="0">
		  	</a>
		</td>
<%
				}
				if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)
						&& !request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV).equals("A")
						&& (lModificabile.equals("SI"))
						&& request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA).equals(lEventoCancellareAnnullare)) {
					// MEV 16: aggiunto codice per gestione FC
					ActLoadDettaglioCompFoglioComp aldcfc = new ActLoadDettaglioCompFoglioComp();
					boolean existsFC = aldcfc.existsReallyFC(new BigDecimal(request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)));
%>
		<td>
	      	<a href="Javascript:conferma('<%=lFun.getNameAction()%>',
	      								'<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>',
	      								'<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>',
	      								'<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV)%>',
	      								'<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV)%>',
	      								'<%=lOrdinamento%>',
	      								'<%=existsFC%>');">
	     		<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
	      	</a>
		</td>
<%
	  			}
	  			if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA)
	  					&& (request.getParameter("Evento").compareTo("SI") == 0)
	  					&& !request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV).equals("A")
	   					&& !request.getParameter("docRegistrato").equals("M")) { // migrato
					// ActGestisciButtonsProvvedimento lAction = new ActGestisciButtonsProvvedimento();
					// String lActDettaglio = lAction.getActionStampaProvvedimento(request.getParameter("MotivoEvento"));
					String lActStampa ="siap.sico.evento.action.ActLoadDocumento";
%>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	    <%--td>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActStampa%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
			  	<img src="/images/print.gif" alt="Visualizza Stampa" width="12" height="12" border="0">
			</a>
	    </td--%>

		<!-- BOTTONE DI STAMPA -->
		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_RICERCHE_STAMPA_SIEP%>">
			<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action="+lActStampa+"&"+request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)+"="+request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>"/>
		</jsp:include>
<%
	  			}
	  			if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_TRASFERIMENTO)) {
%>
		<td>
	  	  	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
				<img src="/images/net16.gif" alt="Trasferisci" width="12" height="12" border="0">
	      	</a>
	    </td>
<%
	  			}
				// MEV 16: aggiunto codice per gestione FC
				ActLoadDettaglioCompFoglioComp actFC = new ActLoadDettaglioCompFoglioComp();
				boolean isFCIconVisible = actFC.isFCIconVisible(request.getParameter("MotivoEvento"), new BigDecimal (request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)));
				boolean existFC = actFC.existsReallyFC(new BigDecimal(request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)));
				String operation = "InsertFC";
				String tooltip = "Inserimento Foglio Complementare";
				// ex dettaglioFC24.gif
				String coloreIcona = "dettaglioFC24V.png";
				if (existFC) {
					// operation = "ModificaFC";
					tooltip = "Modifica Foglio Complementare";
					coloreIcona = "dettaglioFC24R.png";
				}
				// MEV 16 CUMULO: gestite casistiche per cui far vedere l'icona del FC
				boolean onOffIconFCCumulo = "off".equalsIgnoreCase(F3BProperties.getProperty("onOffIconFCCumulo")) ? true : false;
				String codMotivo = request.getParameter("MotivoEvento");
  				if (ModuloCumuloUtils.isCumulo(Utils.isPresent(codMotivo) ? codMotivo : "")) {
					// String valoreIdEntitaProvv = request.getParameter("ValoreIdEntitaProvv");
  					String docRegistrato = request.getParameter("docRegistrato");
  					if (Utils.isPresent(docRegistrato)
  							&& !"S".equals(docRegistrato)
  							&& !onOffIconFCCumulo) {
  						// l'evento deve essere validato!
  						onOffIconFCCumulo = true;
  					}
  					isFCIconVisible = !onOffIconFCCumulo;
  				}
  				if (lFun.getFunctionType().equals("F") && isFCIconVisible) {
%>
		<td>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&Provenienza=<%=operation%>">
				<img src="/images/<%=coloreIcona%>" alt="<%=tooltip%>" width="12" height="12" border="0">
	      	</a>
		</td>
<%
				}
			}
		}
	}
%>
	</tr>
</table>