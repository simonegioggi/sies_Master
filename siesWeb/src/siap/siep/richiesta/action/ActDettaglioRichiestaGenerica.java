package siap.siep.richiesta.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioRichiestaGenerica
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Richiesta Generica
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActDettaglioRichiestaGenerica extends ActSIESDettaglioProvvedimento
		implements ICostantiRichiesta {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Pena Residua
		/*
		 * REWORK DETTAGLIO IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel
		 * llPenMod = lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 */

		PenaResiduaModel llPenMod = getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("penaresidua", llPenMod);

		// ricerca posizione giuridica
		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); PosizioneGiuridicaModel lPosGiuModificata = new
		 * PosizioneGiuridicaModel(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.
		 * getIdFascicoloSiep());
		 */

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = getPosizioneGiuridicaLuogoDetenzioneAltraCausa(
				lIdEvento, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("eventonotifica", lEveMod);

		// descrizione per il dettaglio
		String lDescr = null;
		if (lEveMod != null && lEveMod.getEvento() != null && lEveMod.getEvento().getCodMotivo() != null) {
			String lMotivo = lEveMod.getEvento().getCodMotivo();
			if (lMotivo.equals("0351") || lMotivo.equals("0336") || lMotivo.equals("0337") || // parere
					lMotivo.equals("0338") || // visto
					lMotivo.equals("0342")) { // ricorso
				lDescr = "Parere/Visto/Ricorso";
			} else if (lMotivo.equals("0339") || lMotivo.equals("0340")) {
				// trasmissione
				lDescr = "Trasmissione";
			} else {
				// Richiesta/Comunicazione
				lDescr = "Richiesta/Comunicazione";
			}

			setRequestAttribute("titolodettaglio", lDescr);
		}

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());

		setRequestAttribute("magistrato", lMagMod);

		// notifiche
		for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
			NotificaModel lNotMod = new NotificaModel();
			lNotMod = lEveMod.getNotifiche()[i];
			if (lNotMod != null && lNotMod.getAutEstIdAutoritaEsterna() != null) {
				setRequestAttribute("autoritaEsterna", lNotMod.getAutoritaEsterna());
				setRequestAttribute("noteautoritaEsterna", lNotMod.getNote());
			} else if (lNotMod != null && lNotMod.getIstDetIdIstitutoDetenzione() != null) {
				setRequestAttribute("istituto", lNotMod.getIstitutoDetenzione());
			} else if (lNotMod != null && lNotMod.getUffCodUfficio() != null
					&& "E".equals(lNotMod.getCodTipoNotifica())) {
				setRequestAttribute("ufficioge", lNotMod.getUfficio());
			} else if (lNotMod != null && lNotMod.getUffCodUfficio() != null
					&& !"T".equals(lNotMod.getCodTipoNotifica())
					// MEV_66: aggiunto campo in visualizzazione tdsm
					&& ("TDS".equals(lNotMod.getUfficio().getCodTipoUfficio())
							|| "TDSM".equals(lNotMod.getUfficio().getCodTipoUfficio()))) {
				setRequestAttribute("ufficiotds", lNotMod.getUfficio());
			} else if (lNotMod != null && lNotMod.getUffCodUfficio() != null
					&& !"T".equals(lNotMod.getCodTipoNotifica())
					// MEV_66: aggiunto campo in visualizzazione udsm
					&& ("UDS".equals(lNotMod.getUfficio().getCodTipoUfficio())
							|| "UDSM".equals(lNotMod.getUfficio().getCodTipoUfficio()))) {
				setRequestAttribute("ufficiouds", lNotMod.getUfficio());
			} else if (lNotMod != null && lNotMod.getUffCodUfficio() != null
					&& !lNotMod.getCodTipoNotifica().equals("T")) {
				setRequestAttribute("ufficiopm", lNotMod.getUfficio());
			}
			// MEV_66: aggiunto UEPE/USSM
			else if (lNotMod != null && lNotMod.getCSSA() != null)
				setRequestAttribute("cssa", lNotMod.getCSSA());
		}

		// valore di ritorno
		return PG_DETTAGLIO_RICHIESTA_COMUNICAZIONE;
	}

}