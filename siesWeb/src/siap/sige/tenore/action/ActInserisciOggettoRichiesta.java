package siap.sige.tenore.action;

import java.math.BigDecimal;
import java.util.StringTokenizer;

/**
 * <p>
 * Title: ActInserisciOggettoRichiesta
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento
 * </p>
 * La classe attiva la ricerca degli Oggetti (Tenore_Sige) legati alla richiesta del Fascicolo SIGE ed apre la
 * finestra per la Gestione degli Oggetti legati alla Richiesta/Atto.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciOggettoRichiesta extends ActAggiornaTenoriSige implements ICostantiTenoreSige {
	BigDecimal mIdRichiesta = null;

	public String processRequest() throws Exception {
		// ID Richiesta parametro di ingresso
		mIdRichiesta = getRequestBigDecimalParameter(CAMPO_RIC_SIG_ID_RICHIESTA_SIGE);
		String codContenuto = getRequestStringParameter(COD_CONTENUTO_SEL);
		// Lettura Id degli Oggetti selezionati
		StringTokenizer lCodOggetto = new StringTokenizer(getRequestStringParameter(CAMPO_COD_OGGETTO_SIGE),
				"|");

		// Lettura Id dei Titoli Esecutivi selezionati
		StringTokenizer lIdSentenza = new StringTokenizer(getRequestStringParameter(CAMPO_SEN_ID_SENTENZA),
				"|");

		// Lettura Id dei Reati selezionati
		StringTokenizer lIdReati = new StringTokenizer(getRequestStringParameter(CAMPO_REA_ID_REATO), "|");

		// 20190514 [SG]: aggiunto recupero proprieta'
		String codContenutoOld = "";
		if (!isRequestParameterNullObj("codContenutoOld"))
			codContenutoOld = getRequestStringParameter("codContenutoOld");

		// Inserimento Tenori Sige
		// 20190514 [SG]: aggiunto parametro di passaggio
		caricaTenori(mIdRichiesta, lCodOggetto, lIdSentenza, lIdReati, "I", codContenuto, codContenutoOld);

		// Ritorno al punto di partenza
		String lRetPage = ritornoDopoCancellazione("Inserimento effettuato con successo.", null);
		return lRetPage;
	}

}
