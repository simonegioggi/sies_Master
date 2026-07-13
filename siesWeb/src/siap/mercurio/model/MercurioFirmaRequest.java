package siap.mercurio.model;

/**
 * MercurioFirmaRequest - Dati per la firma digitale di un contenuto già archiviato su Mercurio
 * (Mercurio_LineeGuidaSviluppo_v1.0.docx §8.3.14 "Firma digitale di un contenuto del documento",
 * Request Payload type {@code com.ibm.mercurio.master.service.request.SignData}):
 *
 * <pre>
 * {
 *     "username":"test.jus.sign",
 *     "password":"Password01",
 *     "pin":"690456",
 *     "reason":"SIGN_REASON"
 * }
 * </pre>
 *
 * Il contenuto da firmare non viene passato in questa richiesta: deve già esistere su Mercurio (creato
 * tramite {@link siap.mercurio.client.MercurioDocumentaleClient#archivia}), identificato da
 * {@code documentIdClient} e {@code contentId} passati come path param al servizio
 * {@link siap.mercurio.client.MercurioFirmaClient#firma}.
 *
 * @version 2.0
 */
public class MercurioFirmaRequest {

	// Username dell'utente/dispositivo abilitato alla firma su Mercurio.
	private String mUsername;

	// Password dell'utente di firma.
	private String mPassword;

	// PIN del dispositivo/certificato di firma.
	private String mPin;

	// Motivazione della firma (es. "SIGN_REASON").
	private String mReason;

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String aUsername) {
		mUsername = aUsername;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String aPassword) {
		mPassword = aPassword;
	}

	public String getPin() {
		return mPin;
	}

	public void setPin(String aPin) {
		mPin = aPin;
	}

	public String getReason() {
		return mReason;
	}

	public void setReason(String aReason) {
		mReason = aReason;
	}
}
