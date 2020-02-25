package siap.siep.revoca.action;

/**
* <p>Title: ActLoadDettaglioOrdineEsecuzioneRevoca</p>
* <p>Description: Classe Action per la load dettaglio Ordine Esecuzione x Revoca Sospensione Pena
* </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadDettaglioOrdineEsecuzioneRevoca extends ActSIESDettaglioProvvedimento
		implements ICostantiRevoca {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// EVENTO
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		setRequestAttribute("eventonotifica", lEveMod);

		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		// PosizioneGiuridicaModel lPosGiuModificata = new PosizioneGiuridicaModel();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		setRequestAttribute("posizioneluogoaltra", lPos);

		// MAGISTRATO
		MagistratoModel lMag = lEveMod.getMagistrato();

		setRequestAttribute("magistrato", lMag);

		// PENA RESIDUA
		// Date lDataInizioPena = null;
		// Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrlp
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());

		/*
		 * lDataInizioPena = llPenMod.getDataInizio(); lDataFinePenaM = llPenMod.getDataFine(); lDataFinePenaA
		 * = llPenMod.getDataFinePresunta();
		 */
		setRequestAttribute("penaresidua", llPenMod);

		// NOTIFICHE
		NotificaModel[] lNotifiche = lEveMod.getNotifiche();
		List lListAvvocatiSiep = new ArrayList();

		for (int i = 0; i < lNotifiche.length; i++) {
			// Autorita Esterne
			if (lNotifiche[i].getAutEstIdAutoritaEsterna() != null
					&& lNotifiche[i].getAvvIdAvvocatoFascicoloSiep() == null) {
				setRequestAttribute("autorita", lNotifiche[i]);
			}

			// Avvocati Siep
			if (lNotifiche[i].getAvvIdAvvocatoFascicoloSiep() != null) {
				lListAvvocatiSiep.add(lNotifiche[i]);
			}
		}

		setRequestAttribute("listaAvvSiep", lListAvvocatiSiep);

		return PG_LOAD_DETTAGLIO_ORDINE_ESECUZIONE_REVOCA;
	}

}