package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.ProvvedimentoEventoTenoreFascicoloSiusModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActDettaglioAnnotazioneDecisioneDellaSorveglianza - Classe Action per il dettaglio dell' Inserimento del
 * provvedimento di Annotazione della Decisione della Sorveglianza
 *
 * @version 1.0
 */
public class ActDettaglioAnnotazioneDecisioneDellaSorveglianza extends ActSIESDettaglioProvvedimento
		implements ICostantiMisuraSicurezza, ICostantiOrdineEsecuzione {

	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Ticket#202602170130 - Il provvedimento SIUS collegato all'annotazione SIEP che si sta visualizzando
		// DEVE Essere recuperato dall'EVE_ID_EVENTO
		/*
		 * // Lista PROVV. SIUS Vector Provvedimenti; IPeriodoAltraMisura lPAMCtrl =
		 * SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		 * siesLogger.debug("Ricerca provve per fascicolo di sessione "); Provvedimenti =
		 * lPAMCtrl.ExRicercaProvvedimentoEventoByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 *
		 *
		 *
		 * // MEV_39 ***** inizio ***** // -----> controllo se fascicolo di classe I è legato a fascicolo di
		 * classe IV // -----> in caso affermativo recupero i provvedimenti SIUS legati al fascicolo // ----->
		 * di classe IV IMisuraSicurezza lCtrMis = SIEPLookupRemote.getMisuraSicurezzaRemote(); BigDecimal
		 * fasSiefascCollegato = new BigDecimal(0); Vector vectFasIV = new Vector(); vectFasIV =
		 * lCtrMis.ExRicercaFascicoliCollegati(lFascMod.getIdFascicoloSiep()); if(vectFasIV.size() > 0){
		 * Iterator iteIV = vectFasIV.iterator(); while(iteIV.hasNext() ) { FascMsToFascSiepModel fascMSMod =
		 * (FascMsToFascSiepModel)iteIV.next(); fasSiefascCollegato =
		 * fascMSMod.getFasSieIdFascicoloCollegato();
		 *
		 * } }
		 *
		 * if(vectFasIV.size() > 0 && Provvedimenti.isEmpty()){ Provvedimenti =
		 * lPAMCtrl.ExRicercaProvvedimentoEventoByFascicoloSiep(fasSiefascCollegato); } // MEV_39 ***** fine
		 * ******
		 *
		 * // setRequestAttribute("ListaProvv", Provvedimenti);
		 */
		// EventoNotifica (Provvedimento di annotazione Inserito)
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		setRequestAttribute("eventonotifica", lEveMod);

		// Ticket#202602170130 - Il provvedimento SIUS collegato all'annotazione SIEP che si sta visualizzando
		//
		// siesLogger.debug("Nuova ricerca per eve_id_evento "+lEveMod.getEvento().getEveIdEvento());
		IPeriodoAltraMisura lPAMCtrl = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		ProvvedimentoEventoTenoreFascicoloSiusModel ProvvModel = lPAMCtrl
				.ExRicercaProvvedimentoEventoByIdEvento(lEveMod.getEvento().getEveIdEvento());

		/*
		 * // Dati PROVVEDIMENTO SPECIFICO SIUS ProvvedimentoEventoTenoreFascicoloSiusModel ProvvModel = new
		 * ProvvedimentoEventoTenoreFascicoloSiusModel(); Iterator Itx = Provvedimenti.iterator(); while
		 * (Itx.hasNext()) { ProvvModel = (ProvvedimentoEventoTenoreFascicoloSiusModel) Itx.next(); if
		 * (ProvvModel.getEvento().getIdEvento().equals(lEveMod.getEvento().getEveIdEvento())) { break; } }
		 */
		setRequestAttribute("provvedimento", ProvvModel);

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());
		// String lCodPosGiu = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica();

		setRequestAttribute("posizioneluogoaltra", lPos);

		// PenaResidua
		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		// BigDecimal idPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);

		PenaResiduaModel llPenMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		llPenMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (llPenMod != null) {
			lDataInizioPena = llPenMod.getDataInizio();
			// lDataFinePenaM = llPenMod.getDataFine();
			lDataFinePenaA = llPenMod.getDataFinePresunta();
		}
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);

		// Misure Sicurezza

		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		Vector MisSicVec = null;
		List lListMis = new ArrayList();

		MisSicVec = new Vector(lMisCtrl.ExRicercaMisuraSicurezzaByEventoKey(lId));
		if (MisSicVec != null && MisSicVec.size() > 0) {
			setRequestAttribute("VecMisureSic", MisSicVec);
		} else {
			lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
			if (lListMis != null) {
				setRequestAttribute("listaMisureSic", lListMis);
			}
		}

		// pagina di ritorno
		return PG_DETTAGLIO_ANNOTA_DECISIONE_SORVEGLIANZA;
	} // chiude processRequest

} // Chiude Classe