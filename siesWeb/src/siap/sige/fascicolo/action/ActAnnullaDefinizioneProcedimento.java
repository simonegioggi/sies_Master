package siap.sige.fascicolo.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siepe.SIEPEException;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: ActAnnullaDefinizioneProcedimento
 * </p>
 * <p>
 * Description: Classe Action per annullare la Definizione del Procedimento.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActAnnullaDefinizioneProcedimento extends ActionSige implements ICostantiFascicoloSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		String lRetPage = null;
		// gestioneRitorno();

		this.annulla();
		lRetPage = ritornoDopoCancellazione("Annullata la Definizione del Procedimento !", lRetPage);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lRetPage;
	}

	private void annulla() throws Exception {
		// il Fascicolo si ricava dalla sessione
		FascicoloSigeModel lFascicolo = getFascicoloSigeInSessione();

		// Dati da aggiornare in Fascicolo SIGE.
		lFascicolo.setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice dell'operatore che modifica
		lFascicolo.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice dell'operatore che
																				// modifica
		lFascicolo.setDataAggiornamento(DateUtils.getSysDate());
		lFascicolo.setCodStatoFascicolo(COD_ISCRITTO);
		lFascicolo.setDataDefinizione(null);
		lFascicolo.setCodTipoDefinizione(null);
		lFascicolo.setDescrDefinizione("");

		Vector lVect = null;
		ProvvedimentoSigeEventoModel provvSigeEveMod = null;
		IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
		// String lTipiProvv = "'" + ICostantiProvvedimentoSige.COD_ANNOTAZIONE + "'"; // CODICE
		// PROVVEDIMENTO_SIGE.
		String lTipiProvv = "'" + ICostantiProvvedimentoSige.DEFINIZIONE_MANUALE + "'"; // CODICE
																						// PROVVEDIMENTO_SIGE.
		lVect = mCtrl.ExRicercaProvvSigePerIdFasSigeTipiProvv(lFascicolo.getIdFascicoloSige(), lTipiProvv);
		if (lVect != null && lVect.size() > 0) {
			provvSigeEveMod = (ProvvedimentoSigeEventoModel) lVect.firstElement();
		}

		// Viene richiamato il Controller per eseguire l'Update dei campi afferenti alla
		// definizione procedimento del fascisolo siepe interessato.
		IFascicoloSige lFasCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		if (provvSigeEveMod != null) {
			lFasCtrl.ExAnnullaDefinizioneManualeFascicoloSige(provvSigeEveMod.getProvvedimento(),
					provvSigeEveMod.getEventoNotifica().getEvento(), lFascicolo);
		} else {
			throw new SIEPEException(SIEPEException.USER_MESSAGE, "Provvedimento Sige non trovato");
		}
	}

}