package siap.mercurio.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import f3b.log.LogF3B;
import siap.mercurio.config.MercurioConfig;
import siap.mercurio.exception.MercurioIntegrationException;
import siap.mercurio.model.MercurioFirmaRequest;
import siap.mercurio.model.MercurioFirmaResponse;
import siap.mercurio.util.MercurioHttpUtil;

/**
 * MercurioFirmaClient - Client applicativo per l'invocazione del servizio di firma digitale di un
 * contenuto documentale esposto da Mercurio (Documentale Unico del Ministero della Giustizia),
 * Mercurio_LineeGuidaSviluppo_v1.0.docx §8.3.14 "Firma digitale di un contenuto del documento"
 * (operationId {@code signContent}).
 *
 * Il servizio firma un contenuto GIÀ archiviato su Mercurio: va quindi invocato DOPO
 * {@link MercurioDocumentaleClient#archivia}, passando l'identificativo del documento
 * ({@code documentIdClient}) e del contenuto ({@code contentId}) restituiti da quest'ultimo. Questo è
 * l'ordine corretto del flusso, come emerso dalla documentazione tecnica completa di Mercurio (in
 * precedenza, in assenza di tale documentazione, si era ipotizzato l'ordine inverso: firma e poi
 * archiviazione).
 *
 * @version 2.0
 */
public class MercurioFirmaClient {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private MercurioConfig mConfig;
	private MercurioAuthClient mAuthClient;

	public MercurioFirmaClient() {
		mConfig = MercurioConfig.getInstance();
		mAuthClient = new MercurioAuthClient();
	}

	public MercurioFirmaClient(MercurioAuthClient aAuthClient) {
		mConfig = MercurioConfig.getInstance();
		mAuthClient = aAuthClient;
	}

	/**
	 * Invoca il servizio di firma digitale di Mercurio ({@code signContent}, §8.3.14) sul contenuto
	 * identificato da {@code aDocumentIdClient}/{@code aContentId}, entrambi ottenuti da una precedente
	 * chiamata a {@link MercurioDocumentaleClient#archivia}.
	 *
	 * @param aDocumentIdClient identificativo del documento (restituito da {@code create}/{@code createStream}).
	 * @param aContentId        identificativo del contenuto da firmare.
	 * @param aRequest          credenziali/PIN/motivazione della firma (SignData).
	 * @return {@link MercurioFirmaResponse} con i metadati del contenuto firmato.
	 * @throws MercurioIntegrationException in caso di errore di comunicazione o di firma applicativa.
	 */
	public MercurioFirmaResponse firma(String aDocumentIdClient, long aContentId, MercurioFirmaRequest aRequest)
			throws MercurioIntegrationException {

		if (aDocumentIdClient == null || aDocumentIdClient.isEmpty()) {
			throw new MercurioIntegrationException("documentIdClient non valido: impossibile firmare il contenuto.", null);
		}
		if (aRequest == null || aRequest.getUsername() == null) {
			throw new MercurioIntegrationException("Dati di firma (SignData) non validi o mancanti.", null);
		}

		String lUrl = mConfig.getDocumentsBaseUrl() + "/" + mConfig.getDomainCode() + "/" + mConfig.getApplicationCode()
				+ "/" + mConfig.getContextCode() + "/" + aDocumentIdClient + "/content/" + aContentId + "/sign";

		siesLogger.debug("MercurioFirmaClient.firma(): invocazione " + lUrl + " documentIdClient=" + aDocumentIdClient
				+ " contentId=" + aContentId);

		HttpClient lClient = MercurioHttpUtil.newHttpClient(mConfig);
		PutMethod lMethod = new PutMethod(lUrl);
		lMethod.setRequestHeader("Authorization", "Bearer " + mAuthClient.getValidToken());

		JSONObject lBody = new JSONObject();
		try {
			lBody.put("username", aRequest.getUsername());
			lBody.put("password", aRequest.getPassword());
			lBody.put("pin", aRequest.getPin());
			lBody.put("reason", aRequest.getReason());
		} catch (JSONException e) {
			throw new MercurioIntegrationException("Errore nella costruzione della richiesta di firma Mercurio.", e);
		}
		MercurioHttpUtil.setJsonRequestBody(lMethod, lBody);

		String lResponseBody = MercurioHttpUtil.execute(lClient, lMethod);
		JSONObject lRoot = MercurioHttpUtil.parseAndCheckSummary(lResponseBody);
		return parseFirmaResponse(lRoot);
	}

	private MercurioFirmaResponse parseFirmaResponse(JSONObject aRoot) {
		MercurioFirmaResponse lResponse = new MercurioFirmaResponse();
		JSONObject lPayload = aRoot.optJSONObject("payload");
		if (lPayload == null) {
			lResponse.setEsito(false);
			return lResponse;
		}
		lResponse.setDocumentIdClient(lPayload.optString("documentIdClient", null));

		JSONObject lDocument = lPayload.optJSONObject("document");
		JSONObject lCurrentVersion = lDocument != null ? lDocument.optJSONObject("currentVersion") : null;
		JSONArray lContents = lCurrentVersion != null ? lCurrentVersion.optJSONArray("contents") : null;
		if (lContents != null) {
			// Il contenuto firmato (content-type "application/pkcs7-mime") è tipicamente l'ultimo aggiunto
			// alla versione corrente del documento.
			for (int i = lContents.length() - 1; i >= 0; i--) {
				JSONObject lRelation = lContents.optJSONObject(i);
				JSONObject lDocumentContent = lRelation != null ? lRelation.optJSONObject("documentContent") : null;
				if (lDocumentContent != null
						&& "application/pkcs7-mime".equals(lDocumentContent.optString("contentType", null))) {
					JSONObject lKey = lDocumentContent.optJSONObject("key");
					if (lKey != null) {
						lResponse.setContentId(lKey.optLong("contentId"));
					}
					lResponse.setFilename(lDocumentContent.optString("filename", null));
					lResponse.setContentType(lDocumentContent.optString("contentType", null));
					break;
				}
			}
		}
		lResponse.setEsito(true);
		return lResponse;
	}
}
