package siap.sige.tenore.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sige.SIGEException;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;

/**
 * <p>
 * Title: ActCancellaOggetto
 * </p>
 * <p>
 * Description: Classe Action per la cancellazione di un Oggetto Sige in sessione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCancellaOggetto extends ActionSige implements ICostantiTenoreSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// ID Tenore da cancellare parametro di ingresso
		String lIdTenore = null;
		if (!isRequestParameterNullObj(CAMPO_ID_TENORE_SIGE)) {
			lIdTenore = getRequestStringParameter(CAMPO_ID_TENORE_SIGE);
		}

		// ID Sentenza da cancellare parametro di ingresso
		BigDecimal lIdSentenza = null;
		if (!isRequestParameterNullObj(CAMPO_SEN_ID_SENTENZA)) {
			lIdSentenza = getRequestBigDecimalParameter(CAMPO_SEN_ID_SENTENZA);
		}

		if (lIdTenore != null && lIdSentenza != null) {
			cancellazioneTenoriInSessione(lIdTenore, lIdSentenza);
		} else {
			// si è richiesta la cancellazione generale (eliminazione di tutti gli
			// Oggetti/Titoli Esecutivi legati ad una richiesta Sige) non abilitata
			// quando si proviene da Emissione Ordinanza
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Non è possibile effettuare la Cancellazione Generale. Procedere con la cancellazione dei singoli record.");
		}

		// Ritorno al punto di partenza
		String lRetPage = ritornoDopoCancellazione("Oggetto cancellato", null);
		return lRetPage;
	}

	private void cancellazioneTenoriInSessione(String aIdTenore, BigDecimal aIdSentenza) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".cancellazioneTenoriInSessione: inizio");

		Vector lElencoTenori;
		Vector lElencoNuovo = new Vector();
		TenoreSigeEstesoModel lTenoreEsteso = null;

		// Si preleva dalla sessione l'elenco dei Tenori
		if (!isSessionAttributeNullObj("tenori"))
			lElencoTenori = (Vector) getSessionAttribute("tenori");
		else
			lElencoTenori = new Vector();

		int lRiemp = lElencoTenori.size();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("num tenori già in sessione : " + lRiemp);

		// Si ricostruisce l'elenco dei Tenori svuotato del tenore/titolo esecutivo da cancellare
		for (int i = 0; i < lRiemp; i++) {
			lTenoreEsteso = (TenoreSigeEstesoModel) lElencoTenori.get(i);
			if (!lTenoreEsteso.getTenoreSige().getIdTenoreSige().toString().equalsIgnoreCase(aIdTenore)) {
				lElencoNuovo.add(lTenoreEsteso);
			} else {
				if (!lTenoreEsteso.getSentenzaSige().getIdSentenza().toString()
						.equalsIgnoreCase(aIdSentenza.toString())) {
					lElencoNuovo.add(lTenoreEsteso);
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("nuovo num tenori aggiornati in sessione : " + lElencoNuovo.size());

		// Si rimette in sessione l'elenco aggiornato
		setSessionAttribute("tenori", lElencoNuovo);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".cancellazioneTenoriInSessione: fine");
	}

}