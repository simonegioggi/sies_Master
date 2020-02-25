package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 *
 * <p>
 * Title: ActDettaglioOEAltrePosizioni
 * </p>
 * <p>
 * Description: Classe per il dettaglio dell'ordine di esecuzione di altre posizioni giuridiche
 * </p>
 * <p>
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActDettaglioOEAltrePosizioni extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		/*
		 * REWORK PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); IPosizioneGiuridica lPosCtrl =
		 * SIEPLookupRemote.getPosizioneGiuridicaRemote(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lEveMod.
		 * getEvento().getFasSieIdFascicoloSiep());
		 */
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lId,
						lEveMod.getEvento().getFasSieIdFascicoloSiep());
		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il Procedimento non ha una Posizione Giuridica");

		setRequestAttribute("posizioneluogoaltra", lPos);

		int lIndex = 0;

		Vector lVectAvvocati = new Vector();
		String lFC = "0";
		for (lIndex = 0; lIndex < lEveMod.getNotifiche().length; lIndex++) {
			// Controllo se c'e' un Avvocato associato alla Notifica
			if (lEveMod.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep() != null) {
				IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
				AvvocatoSiepModel lAvvocato = lAvvCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSiep(
						lEveMod.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep());
				lVectAvvocati.add(lAvvocato);
			} else if (lEveMod.getNotifiche()[lIndex].getCodTipoNotifica().equals("C"))
				lFC = "1";
			else if (lEveMod.getNotifiche()[lIndex].getIstitutoDetenzione() != null) {
				setRequestAttribute("istituto", lEveMod.getNotifiche()[lIndex].getIstitutoDetenzione());
				setRequestAttribute("noteistituto", lEveMod.getNotifiche()[lIndex].getNote());
			} else if (lEveMod.getNotifiche()[lIndex].getCodTipoNotifica() != null
					&& "E".equals(lEveMod.getNotifiche()[lIndex].getCodTipoNotifica())
					&& lEveMod.getNotifiche()[lIndex].getAutoritaEsterna() != null) {
				setRequestAttribute("autorita", lEveMod.getNotifiche()[lIndex].getAutoritaEsterna());
				setRequestAttribute("noteautorita", lEveMod.getNotifiche()[lIndex].getNote());
			} else if (lEveMod.getNotifiche()[lIndex].getCodTipoNotifica() != null
					&& "AA".equals(lEveMod.getNotifiche()[lIndex].getCodTipoNotifica())
					&& lEveMod.getNotifiche()[lIndex].getAutoritaEsterna() != null) {
				setRequestAttribute("altraautorita", lEveMod.getNotifiche()[lIndex].getAutoritaEsterna());
				setRequestAttribute("notealtraautorita", lEveMod.getNotifiche()[lIndex].getNote());
			} else if (lEveMod.getNotifiche()[lIndex].getCodTipoNotifica() != null
					&& "MS".equals(lEveMod.getNotifiche()[lIndex].getCodTipoNotifica())
					&& lEveMod.getNotifiche()[lIndex].getUffCodUfficio() != null) {
				setRequestAttribute("mds", lEveMod.getNotifiche()[lIndex].getUfficio());
			}
		}

		setRequestAttribute("fogliocomplementare", lFC);

		if (lVectAvvocati.size() > 0)
			setRequestAttribute("avvocati", lVectAvvocati);

		/*
		 * REWORK IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel llPenMod =
		 * lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 */

		PenaResiduaModel llPenMod = this.getPenaResidua(lId, lEveMod.getEvento().getFasSieIdFascicoloSiep());

		setRequestAttribute("StrdataInizioPena",
				DateUtils.getDateToString(llPenMod.getDataInizio(), "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA",
				DateUtils.getDateToString(llPenMod.getDataFinePresunta(), "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);
		setRequestAttribute("eventonotifica", lEveMod);

		/*
		 * REWORK //Istanza EventoModel lEveIstanzaMod = new EventoModel();
		 * lEveIstanzaMod.setFasSieIdFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep()); IEvento
		 * lEvCtrl = SICOLookupRemote.getEventoRemote(); Vector lVectEvento = null; try { lVectEvento =
		 * lEvCtrl.ExRicercaEventoIstanzaRigettata(lEveIstanzaMod); } catch (F3BException e) { if
		 * (e.getErrorCode() != F3BException.USER_MESSAGE) throw e; } if (lVectEvento != null) { if
		 * (lVectEvento.size() > 0) setRequestAttribute("istanza", (EventoModel) lVectEvento.firstElement());
		 * }
		 */
		EventoModel lEveIstanzaMod = this.getEventoIstanzaRigettata(lId,
				lEveMod.getEvento().getFasSieIdFascicoloSiep());
		setRequestAttribute("istanza", lEveIstanzaMod);

		return PG_DETTAGLIO_OE_ALTRE_POSIZIONI;
	}

}