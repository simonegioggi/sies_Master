package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActDefinizioneManualeProcedimento
 * </p>
 * <p>
 * Description: Classe Azione di definizione Fascicolo SIGE.
 * </p>
 * In base ai dati nella request viene chiamato l'Update della tabella:
 * </p>
 * Fascicolo_SIGE .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 */
@SuppressWarnings("rawtypes")
public class ActDefinizioneProcedimento extends ActionSige implements ICostantiFascicoloSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		gestioneRitorno();

		// il Fascicolo si ricava dalla sessione
		FascicoloSigeEstesoModel lFascicoloEsteso = getFascicoloSigeEstesoInSessione();

		FascicoloSigeModel lFascicolo = lFascicoloEsteso.getFascicoloSige();

		IFascicoloSige lFasCtrl = SIGELookupRemote.getFascicoloSigeRemote();

		Vector lVect = null;
		ProvvedimentoSigeEventoModel provvSigeEveMod = null;
		IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
		String lTipiProvv = "'" + ICostantiProvvedimentoSige.DEFINIZIONE_MANUALE + "'"; // Definizione
																						// Manuale.
		lVect = mCtrl.ExRicercaProvvSigePerIdFasSigeTipiProvv(lFascicolo.getIdFascicoloSige(), lTipiProvv);
		if (lVect != null && lVect.size() > 0) {
			provvSigeEveMod = (ProvvedimentoSigeEventoModel) lVect.firstElement();
		}

		// Dati da aggiornare in Fascicolo SIGE by Reference
		lFascicolo.setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice dell'operatore che
																			// inserisce
		lFascicolo.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice dell'operatore che
																				// inserisce
		lFascicolo.setDataAggiornamento(DateUtils.getSysDate());
		lFascicolo.setCodTipoDefinizione(getRequestStringParameter(CAMPO_TIPO_DEFINIZIONE));
		lFascicolo.setDataDefinizione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
				CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
		lFascicolo.setDescrDefinizione(getRequestStringParameter(CAMPO_DESCR_DEFINIZIONE));

		if (provvSigeEveMod != null
				&& provvSigeEveMod.getEventoNotifica().getEvento().getIdEvento() != null) {
			// SONO IN MODIFICA
			// Evento
			provvSigeEveMod.getEventoNotifica().getEvento().setDataEmissione(getRequestDateParameter(
					CAMPO_ANNO_DATA_DEFINIZIONE, CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
			provvSigeEveMod.getEventoNotifica().getEvento().setDataTrasmissioneAtti(getRequestDateParameter(
					CAMPO_ANNO_DATA_DEFINIZIONE, CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
			provvSigeEveMod.getEventoNotifica().getEvento()
					.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			provvSigeEveMod.getEventoNotifica().getEvento()
					.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			provvSigeEveMod.getEventoNotifica().getEvento().setDataAggiornamento(DateUtils.getSysDate());
			// Provvedimento
			provvSigeEveMod.getProvvedimento().setDataEmissione(getRequestDateParameter(
					CAMPO_ANNO_DATA_DEFINIZIONE, CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
			provvSigeEveMod.getProvvedimento().setDataDeposito(getRequestDateParameter(
					CAMPO_ANNO_DATA_DEFINIZIONE, CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
			provvSigeEveMod.getProvvedimento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
			provvSigeEveMod.getProvvedimento().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			provvSigeEveMod.getProvvedimento().setDataAggiornamento(DateUtils.getSysDate());
			// Modifica Definizione Manuale
			lFasCtrl.ExModificaDefinizioneManualeFascicoloSige(provvSigeEveMod.getProvvedimento(),
					provvSigeEveMod.getEventoNotifica().getEvento(), lFascicolo);

		} else {
			// SONO IN INSERIMENTO
			// Evento
			EventoModel lEveMod = new EventoModel();
			lEveMod.setCodTipoEvento("01"); // Provvedimento
			lEveMod.setCodTipoProvvedimento(ICostantiProvvedimentoSige.DEFINIZIONE_MANUALE); // Definizione
																								// Manuale
			lEveMod.setCodMotivo("0353"); // archiviazione per fascicolo iscritto per errore
			lEveMod.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
			lEveMod.setCodLuogoEmittente(getCodComuneUtenteConnesso());
			lEveMod.setCodTipoUfficioDestinatario("-");
			lEveMod.setCodLuogoDestinatario("-");
			lEveMod.setCodEsito("-");
			lEveMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
					CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
			lEveMod.setDataTrasmissioneAtti(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
					CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
			lEveMod.setFlagVideoSiep("S");
			lEveMod.setFlagStampaSiep("S");
			lEveMod.setFlagDocumentoRegistrato("N");
			lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
			lEveMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lEveMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lEveMod.setDataInserimento(DateUtils.getSysDate());
			if (lFascicoloEsteso.getMagAssegnatario() != null
					&& lFascicoloEsteso.getMagAssegnatario().getMagCodMagistrato() != null) {
				lEveMod.setCodMagistrato(lFascicoloEsteso.getMagAssegnatario().getMagCodMagistrato());
			} else {
				lEveMod.setCodMagistrato(null);
			}

			// Provvedimento Sige
			ProvvedimentoSigeModel lProMod = new ProvvedimentoSigeModel();
			lProMod.setFasIdFascicoloSige(lFascicolo.getIdFascicoloSige());
			lProMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
					CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
			lProMod.setDataDeposito(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
					CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
			lProMod.setCodTipoProvvedimento(ICostantiProvvedimentoSige.DEFINIZIONE_MANUALE); // Definizione
																								// Manuale
			lProMod.setCodTipoProvvedimentoSige(ICostantiProvvedimentoSige.DEFINIZIONE_MANUALE);
			lProMod.setDefinitorio("S");
			lProMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lProMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lProMod.setDataInserimento(DateUtils.getSysDate());
			lFasCtrl.ExDefinizioneManualeFascicoloSige(lProMod, lEveMod, lFascicolo);
		}

		// Prepara la "pagina" di dettaglio
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.fascicolo.action.ActLoadDefinizioneProcedimento");
		lRedirigi.setParameter(CAMPO_CHIAVE_ANNO, lFascicolo.getChiaveAnno().toString());
		lRedirigi.setParameter(CAMPO_CHIAVE_PROGR, lFascicolo.getChiaveProgr().toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lRedirigi.toString();
	}

}