package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per modificare la Rideterminazione Pena Pecuniaria
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActModificaRideterminazionePP extends ActInserisciRideterminazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		siesLogger.debug("ID_FASCICOLO = " + fsm.getIdFascicoloSiep());

		EventoNotificaModel enm = new EventoNotificaModel();
		// Procedo alla cancellazione e quind al nuovo inserimento
		EventoModel em = super.getEventoRideterminazionePP(fsm.getIdFascicoloSiep());
		NotificaModel[] nmArray = super.getNotificheRideterminazionePP();

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		em.setIdEvento(idEvento);

		enm.setEvento(em);
		enm.setNotifiche(nmArray);

		// recupero le rateizzazioni da collegare all'evento
		String[] arrayIdRate = getRequestStringParameters(ICostantiRateizzazionePP.CAMPO_EVE_ID_EVENTO);

		// Annotazione Manuale
		AnnotazioneManualeModel amm = getAnnotazioneManuale(fsm.getIdFascicoloSiep());

		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		irpp.exModificaRideterminazionePP(enm, arrayIdRate, amm);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.rateizzazionepp.action.ActDettaglioRideterminazionePP&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + enm.getEvento().getIdEvento();
	}

}