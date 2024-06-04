<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.security.model.FunctionModel"%>
<%@ page import="f3b.security.model.ProfileModel"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="TornaQui"	scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
<SCRIPT LANGUAGE="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
function submit(aAction) {
	var lCampoIdEntita="<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>";
	document.location.href='<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>='+aAction+'&'+lCampoIdEntita+'=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>'+'&TornaQui=<%=TornaQui%>';
}
</SCRIPT>

<%
String lCodUffAppartenenza  = request.getParameter("valoreufficio");
Collection lFunFiglie = (Collection) request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
String lModificabile= "SI";//Parametro che vale per la cancellazione e la modifica
String lCancellabile= "SI";//Parametro che vale solo per la cancellazione e non per la modifica
boolean lDiProprieta = true; //Booleno che indica se il fasicolo è prorpio o di un altro ufficio
String lInsertAllegato= "SI";//Parametro che indica se l'utente potrà inserire allegati (solo per i procedimenti iscritti dal proprio ufficio)
UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
String lUffUtente =  lUtenteMod.getUfficioUtente().getCodUfficio();
ProfileModel lProfilo = (ProfileModel) lUtenteMod.getUserProfile();

// MEV_35: recupero info sul fascicolo per oggetto procedimento
String codOggettoProcedimento = "", tipoSostituzione = "Sanzione";
FascicoloGPModel fgpm = (FascicoloGPModel) session.getAttribute("fascicoloSiusGP");
GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
if (!Utils.isNullObj(fgpm.getGeneraleProcedimentoModel()))
	gpm = fgpm.getGeneraleProcedimentoModel();
if (Utils.isPresent(gpm.getCodOggettoProcedimento()))
	codOggettoProcedimento = gpm.getCodOggettoProcedimento();

// Modifica 22-01-08 per mev a8-rr-012
if (request.getParameter("Cancellabile") != null) {
	//Parametro che vale solo per la cancellazione e non per la modifica
  	lCancellabile = request.getParameter("Cancellabile");
}

// Modifica 14-07-2015 per mev 10 S3
if (request.getParameter("InsertAllegato") != null) {
	lInsertAllegato = request.getParameter("InsertAllegato");
}

if (request.getParameter("Modificabile") != null) {
	lModificabile = request.getParameter("Modificabile");
   	// Nel caso di "Posizione Materiale Fascicolo SIUS" se il fascicolo
   	// non è modificabile bisogna disattivare anche il bottone di Inserimento 
	// 31/05/2010 Bottone di inserimento Posizione Materiale riattivato.
   	// if (lModificabile.equalsIgnoreCase("NO") && request.getParameter("tipo_posizione_materiale") != null) {
   	// 	if (request.getParameter("tipo_posizione_materiale").equalsIgnoreCase("SIUS")) {
   	// 		lDiProprieta = false;
	// 	}
   	// }
} else {
	if (!(lUtenteMod.getUfficioUtente().getCodTipoUfficio().equals("UDS")
			|| lUtenteMod.getUfficioUtente().getCodTipoUfficio().equals("TDS")
			// Ticket#20210709019 - Aggiunta gestione TDSM e UDSM
			|| lUtenteMod.getUfficioUtente().getCodTipoUfficio().equals("UDSM")
			|| lUtenteMod.getUfficioUtente().getCodTipoUfficio().equals("TDSM")
			// Ticket#20210709019 - FINE
			|| lUtenteMod.getUfficioUtente().getCodTipoUfficio().equals("UEPE")
			|| lUtenteMod.getUfficioUtente().getCodTipoUfficio().equals("UEPESS"))) {
		FascicoloSiepModel lFas = (FascicoloSiepModel)session.getAttribute("fascicolo");
     	if (lFas != null) {
			if (!lUffUtente.equals(lFas.getChiaveUfficio())) {
				lDiProprieta = false;
       		}
     	}
	}
}

// Visualizzazione dei bottoni
if (lFunFiglie != null && lFunFiglie.size() != 0) {
	Iterator lIterBottoni = lFunFiglie.iterator();
   	FunctionModel lFun = null;
   	while(lIterBottoni.hasNext()) {
		lFun = (FunctionModel)lIterBottoni.next();
     	if (lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)) {
     		if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) && lDiProprieta) {
%>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0">
			</a>
<%
			}
			if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && (lModificabile.compareTo("SI") == 0 && lDiProprieta)) {
%>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
			</a>
<%
			}
			if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && lModificabile.equals("SI")
					&& !("00000").equals(lCodUffAppartenenza) && lDiProprieta && lCancellabile.equals("SI")) {
%>
			<a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
			</a>
<%
			}
			if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA)) {
				// Nuova Stampa Luigi 26-11-2004
%>
			<a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>')">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>print24.gif" alt="Stampa" width="24" height="24" border="0">
			</a>
<%
			}
			if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO_COPIA)) {
%>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrow24.gif" alt="Inserimento con Copia" width="24" height="24" border="0">
			</a>
<%
			}
			// MEV 10 - Bottoni visibili solo per SIEP
			if (lProfilo.isSiep()) {
				if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_ALLEGARE_DOC) && lInsertAllegato.equals("SI")) {
%>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TipoOperazione=A&TornaQui=<%=TornaQui%>">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>allegatoTrasparente.png" alt="Allegare Documento" width="24" height="24" border="0">
			</a>
<%
				}
				if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_RICERCA)) {
%>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TipoOperazione=R&TornaQui=<%=TornaQui%>">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>ricercaSoggettoNelProcedimentoTrasparente.png" alt="Ricerca Soggetto" width="24" height="24" border="0">
			</a>
<%
				}
			}
		    lIterBottoni.remove();
  		}
	}
	// Visualizzazione della combo
	Iterator lIterCombo = lFunFiglie.iterator();
	if (lFunFiglie.size() != 0) {
		boolean inseritaPrima = false;
		boolean daInserire = false;
		while (lIterCombo.hasNext()) {
  			lFun = (FunctionModel) lIterCombo.next();
  			// MEV_35: cambio nome funziona in un caso particolare (U126)
  			if (lFun.getLabelFunction().contains("Ripresa Sanzione Sostitutiva")
  					&& "U126".equals(codOggettoProcedimento))
  				lFun.setLabelFunction(lFun.getLabelFunction().replace("Ripresa Sanzione Sostitutiva", "Ripresa Pena Sostitutiva"));
			if (lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_COMBO)) {
      			if (lModificabile.compareTo("SI") == 0 && lDiProprieta) {
            		daInserire = true;
      			} else {
          			if (lFun.getFunctionType().compareTo("I") == 0
          					|| lFun.getFunctionType().compareTo("M") == 0
          					|| lFun.getFunctionType().compareTo("C") == 0) {
               			daInserire = false;
            		} else {
              			daInserire = true;
          			}
        		}
        		// Inserimento della funzione nella combo
        		if (daInserire) {
          			if (!inseritaPrima) {
             			inseritaPrima = true;
%>
			<select name="vai">
<%
					}
%>
				<option value="<%=lFun.getNameAction()%>"><%=lFun.getLabelFunction()%></option>
<%
				}
      			lIterCombo.remove();
   			}
 		}
		if (inseritaPrima) {
%>
			</select>
			<a href="javascript:submit(document.forms[0].vai.value);">
			  <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>vedi24.gif" alt="Vai" width="24" height="24" border="0">
			</a>
<%
		}
  	}
}
if (request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE) != null
		&& !request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE).equals("")) {
	String lAzioneChiamante = request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE);
%>
	<td class="LBG">
	  	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lAzioneChiamante%>">
			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
	  	</a>
	</td>
<%
}
%>