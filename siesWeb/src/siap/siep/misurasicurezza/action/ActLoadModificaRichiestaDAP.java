package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaRichiestaDAP
 * </p>
 * <p>
 * Description: Classe Action per la load Modifica della Richiesta
 * </p>
 * <p>
 * al DAP per la designazione REMS , per la fase istruttoria Misure di Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: IntersistemiItalia spa
 * </p>
 * 
 * @author AMBROSINO
 * @version 1.0
 */
public class ActLoadModificaRichiestaDAP extends ActionSiap implements ICostantiMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("evento", getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "La " + lck.getEntity()
					+ " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		// EventoNotifica
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);
		setRequestAttribute("eventonotifica", lEveMod);

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());
//		String lCodPosGiu = "";
//		if (lPos != null && lPos.getPosizioneGiuridica() != null)
//			lCodPosGiu = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica();

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Magistrato
		IMagistrato lMCtrl = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagMod = lMCtrl.ExRicercaMagistratoByEvento(lId);
		setRequestAttribute("magistrato", lMagMod);

		// Misure Sicurezza
		// Ricerca Misure sicurezza presenti nel fascicolo : Sono ORDINATE per DATA_INSERIMENTO
		List lListMis = new ArrayList();
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("listaMisure", lListMis);

		// Autorità per Notifiche
		String lCodTipoAutorita = "-";

		if (lEveMod.getNotifiche() != null && lEveMod.getNotifiche().length > 0) {
			for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
				if (lEveMod.getNotifiche()[i] != null
						&& ("E").equals(lEveMod.getNotifiche()[i].getCodTipoNotifica())) {
					if (lEveMod.getNotifiche()[i].getAutoritaEsterna() != null) {
						lCodTipoAutorita = lEveMod.getNotifiche()[i].getAutoritaEsterna()
								.getCodTipoAutorita();
					}
				}

			}
		}

		// Riempimento ComboBox Autorità
		Option lOptionE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		lOptionE.setFilter("27");
		if (lCodTipoAutorita.compareTo("-") != 0) {
			lOptionE.setSelected(lCodTipoAutorita);
		}
		setRequestAttribute("autoritaEsternaE", "" + lOptionE);

		return PG_MODIFICA_RICHIESTA_AL_DAP;
	} // chiude processRequest

} // Chiude Classe