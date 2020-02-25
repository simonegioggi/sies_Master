package siap.sige.richiestaatti.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActLoadRichiestaParere
 * </p>
 * <p>
 * Description: Classe Action per la load di RichiestaCarichiPendenti
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActCancellaRichiestaEsitoParere extends ActRicercaFSigePuntuale implements
		ICostantiRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		// Preleva dalla sessione i dati dell'utente connesso.
		// String lCodiceOperatore = getCodUtenteConnesso();
		// String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// String lCodComune = getCodComuneUtenteConnesso();

		BigDecimal lIdProvvedimento = null;
		lIdProvvedimento = getRequestBigDecimalParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);
		// Ricerca Provvedimento dalla chiave
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel provvedimentoSigeEvento = lCtrlProv
				.ExRicercaProvvedimentoById(lIdProvvedimento);

		// setRequestAttribute("ProvvedimentoEvento", lProvEvento);

		BigDecimal lIdEvento = provvedimentoSigeEvento.getEventoNotifica().getEvento().getIdEvento();

		// Chiama il controller.
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();

		// riempie il model
		EventoModel lEveMod = new EventoModel();
		lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);
		lEveMod.setDataRicezioneAtti(null);
		lEveMod.setCodEsito("-");
		lCtrl.ExModificaEvento(lEveMod);

		// recupero il provvedimento a partire dall'evento
		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();
		provvedimentoSigeEvento.getProvvedimento().setNote(null);
		lProvCtrl.ExModificaProvvedimentoSige(provvedimentoSigeEvento.getProvvedimento(), null);

		// Prepara la pagina di destinazione, precisamente punta
		// ElencoEsitoParere.jsp
		FascicoloSigeEstesoModel lFascicoloEsteso = getFascicoloSigeEstesoInSessione();
		FascicoloSigeModel fascicoloSigeModel = lFascicoloEsteso.getFascicoloSige();

		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sige.richiestaatti.action.ActElencoEsitoParere");
		lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, ""
				+ provvedimentoSigeEvento.getProvvedimento().getIdProvvedimentoSige());
		lPage.setParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO, "" + fascicoloSigeModel.getChiaveAnno());
		lPage.setParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR,
				"" + fascicoloSigeModel.getChiaveProgr());

		return "" + lPage;
	}

}