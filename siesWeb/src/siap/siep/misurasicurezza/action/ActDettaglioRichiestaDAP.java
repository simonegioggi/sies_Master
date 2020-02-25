package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioRichiestaDAP
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio Richiesta al DAP
 * </p>
 * <p>
 * della designazione Istituto per applicazione Misura di Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AMBROS
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioRichiestaDAP extends ActSIESDettaglioProvvedimento implements
		ICostantiMisuraSicurezza, ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		/*
		 * // se vengo da ricerca String torna="";
		 * 
		 * if(!this.isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)) {
		 * torna=getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE); }
		 * 
		 * setRequestAttribute("tornadavalida", torna);
		 */
		// EventoNotifica
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		setRequestAttribute("eventonotifica", lEveMod);
		// Posizione Giuridica
		PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
//		String lCodPosGiu = lPos.getCodPosizioneGiuridica();

		setRequestAttribute("posizione", lPos);

		/*
		 * // PenaResidua Date lDataInizioPena = null; Date lDataFinePenaA = null; Date lDataFinePenaM = null;
		 * 
		 * // BigDecimal idPenaRes =
		 * getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		 * 
		 * PenaResiduaModel llPenMod = new PenaResiduaModel(); IPenaResidua lPenResCtrl =
		 * SIEPLookupRemote.getPenaResiduaRemote(); llPenMod =
		 * lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 * 
		 * if (llPenMod != null ) { lDataInizioPena = llPenMod.getDataInizio(); lDataFinePenaM =
		 * llPenMod.getDataFine(); lDataFinePenaA = llPenMod.getDataFinePresunta(); }
		 * setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		 * setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		 * setRequestAttribute("penaresidua", llPenMod);
		 */

		// Misure Sicurezza

		// Ricerca Misure sicurezza già presenti nel fascicolo : Sono ORDINATE per DATA_INSERIMENTO
		List lListMis = new ArrayList();
		MisuraSicurezzaModel lMisSicMod = null;
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
		// prendo solo l'ultima MISURA
		if (lListMis != null && lListMis.size() > 0) {
			lMisSicMod = (MisuraSicurezzaModel) lListMis.get(lListMis.size() - 1);
		}

		setRequestAttribute("MisuraModel", lMisSicMod);
		setRequestAttribute("listaMisure", lListMis);

		return PG_DETTAGLIO_RICHIESTA_DAP;

	} // chiude processRequest

} // Chiude Classe