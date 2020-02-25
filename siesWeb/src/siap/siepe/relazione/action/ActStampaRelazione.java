package siap.siepe.relazione.action;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.web.ActionSiap;
import siap.siepe.SIEPEException;
import siap.siepe.attivita.action.ActStampaAttivita;
import siap.siepe.attivita.action.ICostantiAttivita;
import siap.siepe.richiesta.action.ActStampaRichiesta;
import siap.siepe.richiesta.action.ICostantiRichiesta;

/**
 * <p>
 * Title: ActStampaRelazione
 * </p>
 * <p>
 * Description: Classe Azione demandata all'attuazione del modello di stampa per la Relazione.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActStampaRelazione extends ActionSiap implements ICostantiRelazione {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// ID TEMPLATE fissato per testare prima di implementare la COMBO
	public final String TEMPLATE_ATTIVITA_01 = "SIEPE_ATT_001";

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// Documento di stampa
		ByteArrayOutputStream lReport = null;

		// BigDecimal lIdAttivita = null;
		// BigDecimal lIRichiesta = null;

		// Preleva dalla sessione i dati dell'utente connesso.
		// String lCodiceOperatore = getCodUtenteConnesso();
		// String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// UfficioModel lUfficio = getUfficioUtenteConnesso();

		// Si fissa il valore di default ad un template di prova
		String lIdTemplate = TEMPLATE_ATTIVITA_01;
		// Se non passato attraverso la request
		if (!isRequestParameterNullObj(ICostantiTemplate.CAMPO_ID_TEMPLATE)) {
			lIdTemplate = getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE);
		}

		if (!isRequestParameterNullObj(ICostantiAttivita.CAMPO_ID_ATTIVITA)) {
			// Se si tratta di una Relazione X Attività si richiama la generazione della stampa ATTIVITA
			ActStampaAttivita lAct = new ActStampaAttivita();
			lAct.setReqSes(this.getRequest(), this.getSession());
			lReport = lAct.generaStampa(getRequestBigDecimalParameter(ICostantiAttivita.CAMPO_ID_ATTIVITA),
					lIdTemplate);
		} else if (!isRequestParameterNullObj(ICostantiRichiesta.CAMPO_ID_RICHIESTA)) {
			// Se si tratta di una Relazione X Richiesta si richiama la generazione della stampa RICHIESTA
			ActStampaRichiesta lAct = new ActStampaRichiesta();
			lAct.setReqSes(this.getRequest(), this.getSession());
			lReport = lAct.generaStampa(getRequestBigDecimalParameter(ICostantiRichiesta.CAMPO_ID_RICHIESTA),
					lIdTemplate);
		} else
			throw new SIEPEException(SIEPEException.USER_MESSAGE, "Chiave Stampa Mancante !");

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIEPEException(SIEPEException.USER_MESSAGE, "Nessun documento è stato generato.");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return IWebConstants.PG_DOWNLOAD_NEW;
	}

}