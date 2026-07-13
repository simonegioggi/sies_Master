package siap.mercurio.model;

/**
 * MercurioArchiviazioneResponse - Esito della richiesta di archiviazione al Documentale Unico Mercurio.
 *
 * L'identificativo restituito ({@code mIdDocMercurio}) deve essere persistito sulla tabella applicativa
 * di dominio (es. EVENTO.ID_DOC_MERCURIO, DOCUMENTO_ALLEGATO.ID_DOC_MERCURIO) per consentire la
 * consultazione/verifica futura del documento direttamente dal sistema documentale.
 *
 * @version 1.0
 */
public class MercurioArchiviazioneResponse {

	// Identificativo univoco assegnato da Mercurio al documento archiviato (documentIdClient, es.
	// "MINGG-20220706-1-1-1-1-000001978U").
	private String mIdDocMercurio;

	// Identificativo del contenuto (documentContent.key.contentId) appena archiviato: necessario per
	// poter successivamente invocare il servizio di firma digitale (MercurioFirmaClient.firma) su
	// questo specifico contenuto (Mercurio_LineeGuidaSviluppo_v1.0.docx §8.3.14 "signContent").
	private Long mContentId;

	// Nome file e content-type del contenuto archiviato, così come riportati da Mercurio.
	private String mFilename;
	private String mContentType;

	// Esito applicativo restituito da Mercurio (OK / KO).
	private boolean mEsito;

	// Codice/descrizione errore in caso di esito negativo.
	private String mCodiceErrore;
	private String mDescrizioneErrore;

	public String getIdDocMercurio() {
		return mIdDocMercurio;
	}

	public void setIdDocMercurio(String aIdDocMercurio) {
		mIdDocMercurio = aIdDocMercurio;
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
