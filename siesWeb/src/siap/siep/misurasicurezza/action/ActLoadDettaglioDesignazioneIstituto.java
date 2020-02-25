package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoVerbaleModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioDesignazioneIstituto
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio Annotazione
 * </p>
 * <p>
 * della designazione Istituto da part edel DAP
 * </p>
 * <p>
 * per applicazione Misura di Sicurezza
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
public class ActLoadDettaglioDesignazioneIstituto extends ActSIESDettaglioProvvedimento implements
		ICostantiMisuraSicurezza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// EventoVerbale
		EventoVerbaleModel lEveVerMod = new EventoVerbaleModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveVerMod = lCtrl.ExRicercaEventoVerbaleByIdEve(lId);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" -------- ActLoadDettaglioDesignazioneIstituto - Torno da Ricerca - Model = "
				+ lEveVerMod);
		setRequestAttribute("eventoverbale", lEveVerMod);

		// Posizione Giuridica
		PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
//		String lCodPosGiu = lPos.getCodPosizioneGiuridica();
		setRequestAttribute("posizione", lPos);

		// Misure Sicurezza

		// Ricerca Misure sicurezza già presenti nel fascicolo : Sono ORDINATE per DATA_INSERIMENTO
		List lListMis = new ArrayList();
		MisuraSicurezzaModel lMisSicMod = null;
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());

		// prendo solo l'ultima MISURA
		if (lListMis != null && lListMis.size() > 0)
			lMisSicMod = (MisuraSicurezzaModel) lListMis.get(lListMis.size() - 1);
		setRequestAttribute("MisuraModel", lMisSicMod);
		setRequestAttribute("listaMisure", lListMis);

		// model Istituto Detenzione
		IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();
		if (lEveVerMod != null && lEveVerMod.getVerbale() != null
				&& lEveVerMod.getVerbale().getIstDetIdIstitutoDetenzione() != null
				&& !lEveVerMod.getVerbale().getIstDetIdIstitutoDetenzione().equals("-")) {
			IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
			lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lEveVerMod.getVerbale()
					.getIstDetIdIstitutoDetenzione());
		}
		setRequestAttribute("istitutodetenzione", lIstMod);

		setRequestAttribute("modifica", "N");

		return PG_LOAD_DETTAGLIO_DESIGNAZIONE_ISTITUTO;

	} // chiude processRequest

} // Chiude Classe