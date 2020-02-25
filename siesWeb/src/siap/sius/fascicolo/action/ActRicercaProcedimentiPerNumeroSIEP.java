package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaProcedimentiPerNumeroSIEP
 * </p>
 * <p>
 * Description: Ricerca dei Procedimenti (rispondenti ai parametri selezionati) del soggetto individuato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaProcedimentiPerNumeroSIEP extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		this.setLinkRitorno();
		// ==========================================================================
		// Verifico che il parametro CAMPO_ID_FASCICOLO_SIEP sia stato passato sulla
		// request.
		// Questo controllo è necessario (29/05/2006) in quanto questa funzione
		// può essere richiamata anche dal menù di scelta rapida
		// ==========================================================================
		BigDecimal lId_FascicoloSiep = null;
		if (!this.isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			lId_FascicoloSiep = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
		} else if (!this.isSessionAttributeNullObj("fascicolo")) {
			// Se provengo del Menù Scelta Rapida devo recuparere i dati dalla sessione
			// Cerco in sessione il fascicolo per recuperare l'id
			lId_FascicoloSiep = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		} else {
			// Se non ho l'id fascicolo ne sulla request ne in sessione restituisco la
			// pagina di ricerca fascicolo
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// UtenteModel lUtenteMod = new
		// UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		// Si chiama il FascicoloSiusController.
		IFascicoloSius lFascSogCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		Vector lFascicoliPerNumeroSIEP = lFascSogCtrl.ExRicercaFascicoliPerNumeroSIEP(lId_FascicoloSiep);

		// Sono un utente di SIEP ed esistono dei provvedimenti caricati da SIUS
		if (!(this.getCodUtenteConnesso().startsWith("D") || this.getCodUtenteConnesso().startsWith("E"))
				&& lFascicoliPerNumeroSIEP != null && lFascicoliPerNumeroSIEP.size() > 0) {
			Hashtable lHash = new Hashtable();
			// chiama l'EventoSimeoneController
			IEventoSimeone lEveSimeoneCtrl = SICOLookupRemote.getEventoSimeoneRemote();
			Vector lEveByIdFascicolo = lEveSimeoneCtrl.ExRicercaUltimoEventoByIdFascicolo(lId_FascicoloSiep);

			// Controlla che il vettore non sia vuoto
			if (lEveByIdFascicolo != null) {
				Object valore = "";
				// scorre il vettore
				Iterator iter = lEveByIdFascicolo.iterator();
				while (iter.hasNext()) {
					EventoModel lEveModel = (EventoModel) iter.next();
					// riempie l'hash con id_evento/flag_documento_registrato
					// Prende tutti gli eventi con flag_documento_registrato = S
					valore = "";
					// controlla che se il valore del flag è null mette un trattino nell'hashtable
					if (lEveModel.getFlagDocumentoRegistrato() == null) {
						valore = "-";
					} else {
						valore = lEveModel.getFlagDocumentoRegistrato();
					}

					// assegna i valori all'Hashtable
					lHash.put(lEveModel.getIdEvento(), valore);
				}
			}

			// Setta la risposta nella request
			setRequestAttribute("HashFlagEventoRegistratoSIUS", lHash);
		}

		// ricerca lato siep ritorna un vettore di MisuraAlternativaAggregatoModel
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		String[] lTipoEvento = { "01" };
		// ANNA per pene Sospese String[] lTipoProv = {"02","03"};
		// mev 39 : aggiungo ordine di liberazione
		String[] lTipoProv = { "02", "03", "57", "65" };
		Vector lVect = lCtrl.ExRicercaOrdinanzeDecretiSiep(lId_FascicoloSiep, lTipoEvento, lTipoProv);

		String lReturnPage = "";
		if ((lFascicoliPerNumeroSIEP == null || lFascicoliPerNumeroSIEP.isEmpty())
				&& (lVect == null || lVect.isEmpty())) {

			return lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lId_FascicoloSiep;

		}
		setRequestAttribute("misuraaggregato", lVect);

		// Setta la risposta nella request
		setRequestAttribute("fascicoli", lFascicoliPerNumeroSIEP);

		if (lFascicoliPerNumeroSIEP != null && !lFascicoliPerNumeroSIEP.isEmpty()) {
			FascicoloGPModel fascicoloUno = (FascicoloGPModel) lFascicoliPerNumeroSIEP.get(0);
			setRequestAttribute("soggetto", fascicoloUno.getFascicoloSiusModel().getSoggetto());
		}

		lReturnPage = ICostantiFascicoloSius.PG_RICERCA_PROCEDIMENTIPERNUMEROSIEP;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}