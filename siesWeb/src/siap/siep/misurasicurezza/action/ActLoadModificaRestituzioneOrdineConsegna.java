package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import siap.siep.misurasicurezza.model.MisuraSicurezzaNotificataModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * MEV_39
 * <p>
 * Title: ActLoadModificaRestituzioneOrdineConsegna
 * </p>
 * <p>
 * Description: Caricamento per la Modifica della Restituzione Ordine di consegna
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @author SGIOGGI
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadModificaRestituzioneOrdineConsegna extends ActionSiap implements
		ICostantiMisuraSicurezza, ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lm = lockIfNotLocked("evento", getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO),
				getCodUtenteConnesso());
		if (lm != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "La " + lm.getEntity()
					+ " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		// EventoNotifica
		EventoNotificaModel enm = new EventoNotificaModel();
		IEvento iEvento = SICOLookupRemote.getEventoRemote();
		enm = iEvento.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		// Posizione Giuridica
		PosizioneGiuridicaModel pgm = new PosizioneGiuridicaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgm = ipg.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(fsm.getIdFascicoloSiep());
		setRequestAttribute("posizione", pgm);

		// Magistrato
		IMagistrato iMagistrato = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel mm = iMagistrato.ExRicercaMagistratoByEvento(idEvento);
		setRequestAttribute("magistrato", mm);

		// Ricerca Misure sicurezza già presenti nel fascicolo: Sono ORDINATE per DATA_INSERIMENTO
		List listaMisure = new ArrayList();
		IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
		listaMisure = ims.ExRicercaMisuraSicurezzaByIdFascicoloOrd(fsm.getIdFascicoloSiep());
		setRequestAttribute("listaMisure", listaMisure);

		// Ricerca evento e Misure notificate
		List elencoMisureNotificate = new ArrayList();
		elencoMisureNotificate = ims.ExRicercaMSNotificateByIdFasc(fsm.getIdFascicoloSiep(), "M", enm.getEvento().getEveIdEvento()); // parametro di modifica
		setRequestAttribute("elencoMisureNotificate", elencoMisureNotificate);
		MisuraSicurezzaNotificataModel msnm = null;
		if (!elencoMisureNotificate.isEmpty()) {
			for (int i = 0; i < elencoMisureNotificate.size(); i++) {
				MisuraSicurezzaNotificataModel temp = (MisuraSicurezzaNotificataModel) elencoMisureNotificate
						.get(i);
				if (temp.getIdEvento().equals(enm.getEvento().getEveIdEvento())) {
					if (temp.getAutEstIdAutoritaEsterna() != null
							&& enm.getNotifiche()[0].getAutEstIdAutoritaEsterna() != null
							&& temp.getAutEstIdAutoritaEsterna().equals(
									enm.getNotifiche()[0].getAutEstIdAutoritaEsterna())) {
						msnm = new MisuraSicurezzaNotificataModel(temp);
						break;
					}
					if (temp.getIdIstitutoDetenzione() != null
							&& enm.getNotifiche()[0].getIstDetIdIstitutoDetenzione() != null
							&& temp.getIdIstitutoDetenzione().equals(
									enm.getNotifiche()[0].getIstDetIdIstitutoDetenzione())) {
						msnm = new MisuraSicurezzaNotificataModel(temp);
						break;
					}
				}
			}
		}
		setRequestAttribute("misuraNotificata", msnm);

		return PG_MODIFICA_RESTITUZIONE_ORDINE_CONSEGNA;
	} // chiude processRequest
} // Chiude Classe