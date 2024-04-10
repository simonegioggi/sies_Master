package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * Classe Action per il dettaglio della Definizione Procedimento Pena Pecuniaria
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActDettaglioDefinizioneProcedimentoPP extends ActSIESDettaglioProvvedimento
		implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicoloSiep = fsm.getIdFascicoloSiep();

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();
		lArcMod = lCtrlArc.ExRicercaArchiviazioneCssaIstitutoByIdEvento(idEvento);
		setRequestAttribute("archiviazione", lArcMod);

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(idEvento, idFascicoloSiep);
		setRequestAttribute("posizioneluogoaltra", lPos);

		// MAGISTRATO
		MagistratoModel lMag = enm.getMagistrato();
		setRequestAttribute("magistrato", lMag);

		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return PG_DETTAGLIO_DEFINIZIONE_PROCEDIMENTO_PP;
	}

}