package siap.mercurio.model;

/**
 * MercurioArchiviazioneRequest - Metadati documentali richiesti dal Documentale Unico Mercurio per
 * archiviare/conservare un documento (già firmato) e associarlo al fascicolo/procedimento di
 * riferimento.
 *
 * TODO: mappare i metadati definitivi richiesti da Mercurio (piano di classificazione documentale,
 * tipo documento, AOO/ufficio, ecc.) non appena disponibile la documentazione ufficiale.
 *
 * @version 1.0
 */
public class MercurioArchiviazioneRequest {

	// Contenuto del documento (già firmato) da archiviare.
	private byte[] mContenuto;

	// Nome file da assegnare in Mercurio.
	private String mNomeFile;

	// Tipologia/classificazione documentale (es. "EVENTO", "ISTANZA", "DOCUMENTO_ALLEGATO").
	private String mTipoDocumento;

	// Identificativo del fascicolo/procedimento SIES a cui il documento è associato.
	private String mIdFascicolo;

	// Codice ufficio giudiziario (contesto organizzativo Mercurio).
	private String mCodiceUfficio;

	// Utente/operatore che ha effettuato l'upload (per tracciabilità).
	private String mUtente;

	public byte[] getContenuto() {
		return mContenuto;
	}

	public void setContenuto(byte[] aContenuto) {
		mContenuto = aContenuto;
	}

	public String getNomeFile() {
		return mNomeFile;
	}

	public void setNomeFile(String aNomeFile) {
		mNomeFile = aNomeFile;
	}

	public String getTipoDocumento() {
		return mTipoDocumento;
	}

	public void setTipoDocumento(String aTipoDocumento) {
		mTipoDocumento = aTipoDocumento;
	}

	public String getIdFascicolo() {
		return mIdFascicolo;
	}

	public void setIdFascicolo(String aIdFascicolo) {
		mIdFascicolo = aIdFascicolo;
	}

	public String getCodiceUfficio() {
		return mCodiceUfficio;
	}

	public void setCodiceUfficio(String aCodiceUfficio) {
		mCodiceUfficio = aCodiceUfficio;
	}

	public String getUtente() {
		return mUtente;
	}

	public void setUtente(String aUtente) {
		mUtente = aUtente;
	}
}
