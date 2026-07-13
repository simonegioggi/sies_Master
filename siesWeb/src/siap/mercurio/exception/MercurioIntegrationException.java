package siap.mercurio.exception;

/**
 * MercurioIntegrationException - Eccezione applicativa sollevata in caso di errore durante l'invocazione
 * dei servizi esposti dal sistema documentale unico Mercurio (firma digitale e/o archiviazione).
 *
 * Viene utilizzata sia dal {@code MercurioFirmaClient} sia dal {@code MercurioDocumentaleClient} per
 * segnalare, in modo uniforme, malfunzionamenti di rete, errori applicativi restituiti da Mercurio
 * (codice/descrizione errore) o timeout.
 *
 * TODO: valutare l'introduzione di sottoclassi dedicate (es. MercurioFirmaException,
 * MercurioArchiviazioneException) qualora sia necessario differenziare la gestione degli errori
 * a livello di chiamante (ActUploadDocument, ActUploadDocumentConfermaTrasmissione, ecc.).
 *
 * @version 1.0
 */
public class MercurioIntegrationException extends Exception {

	private static final long serialVersionUID = 1L;

	// Codice errore restituito dal servizio Mercurio (se disponibile).
	private String mCodiceErrore;

	public MercurioIntegrationException(String aMessage) {
		super(aMessage);
	}

	public MercurioIntegrationException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	public MercurioIntegrationException(String aCodiceErrore, String aMessage, Throwable aCause) {
		super(aMessage, aCause);
		mCodiceErrore = aCodiceErrore;
	}

	public String getCodiceErrore() {
		return mCodiceErrore;
	}

	public void setCodiceErrore(String aCodiceErrore) {
		mCodiceErrore = aCodiceErrore;
	}
}
