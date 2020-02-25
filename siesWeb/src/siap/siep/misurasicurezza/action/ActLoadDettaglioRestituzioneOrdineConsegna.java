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
import f3b.util.Utils;

/**
 * MEV_39
 * <p>
 * Title: ActLoadDettaglioRestituzioneOrdineConsegna
 * </p>
 * <p>
 * Description: Caricamento per il Dettaglio della Restituzione Ordine di consegna
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
public class ActLoadDettaglioRestituzioneOrdineConsegna extends ActSIESDettaglioProvvedimento implements
		ICostantiMisuraSicurezza, ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel fsp = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// EventoNotifica
		EventoNotificaModel enm = new EventoNotificaModel();
		IEvento iEvento = SICOLookupRemote.getEventoRemote();
		enm = iEvento.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		// c'è sicuramente una sola notifica
		setRequestAttribute("notifica", enm.getNotifiche()[0]);

		// Posizione Giuridica
		PosizioneGiuridicaModel pgm = new PosizioneGiuridicaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgm = ipg.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(fsp.getIdFascicoloSiep());
		setRequestAttribute("posizione", pgm);

		// Ricerca Misure Sicurezza presenti nel fascicolo: Sono ORDINATE per DATA_INSERIMENTO
		List listaMisure = new ArrayList();
		MisuraSicurezzaModel msm = null;
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		listaMisure = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(fsp.getIdFascicoloSiep());
		// prendo solo l'ultima MISURA
		if (Utils.isPresent(listaMisure))
			msm = (MisuraSicurezzaModel) listaMisure.get(listaMisure.size() - 1);

		setRequestAttribute("MisuraModel", msm);
		setRequestAttribute("listaMisure", listaMisure);

		return PG_LOAD_DETTAGLIO_RESTITUZIONE_ORDINE_CONSEGNA;
	} // chiude processRequest

} // Chiude Classe