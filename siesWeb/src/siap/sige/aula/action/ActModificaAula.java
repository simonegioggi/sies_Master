package siap.sige.aula.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.aula.controller.IAula;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActModificaCollegio
 * </p>
 * <p>
 * Description: Classe Action per la modifica del Collegio
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
public class ActModificaAula extends ActionSiap implements ICostantiAula {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Modifica dell'Aula.
	 * <p>
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *         <p>
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		AulaUdienzaModel lAulaMod = letturaAulaUdienza();

		IAula lCtrl = SIGELookupRemote.getAulaRemote();

		AulaUdienzaModel lAulaRet = lCtrl.ExRicercaAulaByDescrizione(lAulaMod.getIdSezione(),
				lAulaMod.getDescrizioneAula());
		if (lAulaRet != null && new Boolean(lAulaMod.getIdAula().compareTo(lAulaRet.getIdAula()) != 0)) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Esiste già un'Aula con la stessa descrizione per la Sezione selezionata.");
		}

		// Se è stato selezionato il campo Aula Predefinita uguale a 'S'
		// verifico che non esista per la stessa sezione un'altra aula
		// predefinita.
		if (lAulaMod != null && lAulaMod.getFlagPredefinita().equals("S")) {
			Vector lVectAule = null;
			AulaUdienzaModel lAula = new AulaUdienzaModel();
			try {
				lVectAule = new Vector();
				lVectAule = lCtrl.ExRicercaAulaByIdSezione(lAulaMod.getIdSezione());
			} catch (Exception e) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nessun Elemento trovato");
			}

			// Per ogni sezione sarà presente una sola aula predefinita
			if (lVectAule != null) {
				for (int i = 0; i < lVectAule.size(); i++) {
					lAula = ((AulaUdienzaModel) lVectAule.get(i));
					String lPredefinita = lAula.getFlagPredefinita();
					if (lPredefinita != null && lPredefinita.equals("S")) {
						if (new Boolean(lAulaMod.getIdAula().compareTo(lAula.getIdAula()) != 0)) {
							throw new F3BException(F3BException.USER_MESSAGE,
									"Attenzione: esiste già un'Aula predefinita per la Sezione selezionata!");
						}
					}
				}
			}
		}

		AulaUdienzaModel lAulaModRet = lCtrl.ExModificaAula(lAulaMod);

		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.aula.action.ActDettaglioAula");
		lRedir.setParameter(CAMPO_ID_AULA, lAulaModRet.getIdAula().toString());
		lRedir.setParameter(CAMPO_ID_SEZIONE, lAulaModRet.getIdSezione().toString());

		String lPage = lRedir.toString();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return lPage;
	}

	protected AulaUdienzaModel letturaAulaUdienza() throws Exception {

		AulaUdienzaModel lAulaUdienzaModel = new AulaUdienzaModel();

		lAulaUdienzaModel.setIdAula(getRequestBigDecimalParameter(CAMPO_ID_AULA));
		lAulaUdienzaModel.setIdSezione(getRequestBigDecimalParameter(CAMPO_ID_SEZIONE));
		lAulaUdienzaModel.setDescrizioneAula(getRequestStringParameter(CAMPO_DESCRIZIONE_AULA).trim()
				.toUpperCase());
		if (!isRequestParameterNullObj(CAMPO_DESCRIZIONE_STANZA)) {
			lAulaUdienzaModel.setDescrizioneStanza(getRequestStringParameter(CAMPO_DESCRIZIONE_STANZA).trim()
					.toUpperCase());
		}
		if (!isRequestParameterNullObj(CAMPO_DESCRIZIONE_INGRESSO)) {
			lAulaUdienzaModel.setDescrizioneIngresso(getRequestStringParameter(CAMPO_DESCRIZIONE_INGRESSO)
					.trim().toUpperCase());
		}
		if (!isRequestParameterNullObj(CAMPO_NUMERO_PIANO)) {
			// 20170908: è un varchar nel db
			lAulaUdienzaModel.setNumeroPiano(getRequestStringParameter(CAMPO_NUMERO_PIANO));
			// lAulaUdienzaModel.setNumeroPiano(getRequestBigDecimalParameter(CAMPO_NUMERO_PIANO));
		}
		lAulaUdienzaModel.setFlagPredefinita(getRequestStringParameter(CAMPO_FLAG_PREDEFINITA));
		lAulaUdienzaModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAulaUdienzaModel.setDataAggiornamento(DateUtils.getSysDate());
		lAulaUdienzaModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		return lAulaUdienzaModel;
	}

}