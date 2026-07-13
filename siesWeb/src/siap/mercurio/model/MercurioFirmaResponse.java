package siap.mercurio.model;

/**
 * MercurioFirmaResponse - Esito della richiesta di firma digitale di un contenuto già archiviato su
 * Mercurio (Mercurio_LineeGuidaSviluppo_v1.0.docx §8.3.14 "signContent").
 *
 * La risposta di Mercurio riporta solo i METADATI del nuovo contenuto firmato (nome file con estensione
 * "p7m", content-type "application/pkcs7-mime", {@code contentId}): NON contiene i byte del documento
 * firmato. Per ottenere i byte del contenuto firmato è necessario invocare separatamente il servizio di
 * download (§8.3.23 "Creazione sessione di download contenuto" + §8.3.24 "Download di un contenuto"),
 * non ancora implementato in questo package.
 *
 * @version 2.0
 */
public class MercurioFirmaResponse {

	// Identificativo del documento su cui è stata apposta la firma.
	private String mDocumentIdClient;

	// Identificativo del (nuovo) contenuto firmato, da usare per l'eventuale download successivo.
	private Long mContentId;

	// Nome file del contenuto firmato (tipicamente con estensione ".p7m").
	private String mFilename;

	// Content-type del contenuto firmato (tipicamente "application/pkcs7-mime").
	private String mContentType;

	// Esito applicativo restituito da Mercurio (OK / KO).
	private boolean mEsito;

	// Codice/descrizione errore in caso di esito negativo.
	private String mCodiceErrore;
	private String mDescrizioneErrore;

	public String getDocumentIdClient() {
		return mDocumentIdClient;
	}

	public void setDocumentIdClient(String aDocumentIdClient) {
		mDocumentIdClient = aDocumentIdClient;
	}

	public Long getContentId() {
		return mContentId;
	}

	public void setContentId(Long aContentId) {
		mContentId = aContentId;
	}

	public String getFilename() {
		return mFilename;
	}

	public void setFilename(String aFilename) {
		mFilename = aFilename;
	}

	public String getContentType() {
		return mContentType;
	}

	public void setContentType(String aContentType) {
		mContentType = aContentType;
	}

	public boolean isEsito() {
		return mEsito;
	}

	public void setEsito(boolean aEsito) {
		mEsito = aEsito;
	}

	public String getCodiceErrore() {
		return mCodiceErrore;
	}

	public void setCodiceErrore(String aCodiceErrore) {
		mCodiceErrore = aCodiceErrore;
	}

	public String getDescrizioneErrore() {
		return mDescrizioneErrore;
	}

	public void setDescrizioneErrore(String aDescrizioneErrore) {
		mDescrizioneErrore = aDescrizioneErrore;
	}
}
