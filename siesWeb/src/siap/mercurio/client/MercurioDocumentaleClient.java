package siap.mercurio.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import f3b.log.LogF3B;
import siap.mercurio.config.MercurioConfig;
import siap.mercurio.exception.MercurioIntegrationException;
import siap.mercurio.model.MercurioArchiviazioneRequest;
import siap.mercurio.model.MercurioArchiviazioneResponse;
import siap.mercurio.util.MercurioHttpUtil;

/**
 * MercurioDocumentaleClient - Client applicativo per l'invocazione del servizio di archiviazione /
 * conservazione documentale esposto dal Documentale Unico Mercurio
 * (Mercurio_LineeGuidaSviluppo_v1.0.docx §8.3.2 "Creazione documento con contenuti" - operationId
 * {@code createStream}).
 *
 * Il documento (con il relativo contenuto) viene archiviato PRIMA della firma digitale: la firma si
 * applica infatti a un contenuto già esistente su un documento già archiviato (cfr.
 * {@link MercurioFirmaClient}, operationId {@code signContent}, §8.3.14), non viceversa come inizialmente
 * ipotizzato prima della disponibilità della documentazione tecnica completa di Mercurio.
 *
 * Il metodo {@link #archivia} restituisce, oltre all'identificativo del documento
 * ({@code documentIdClient}), anche l'id del contenuto appena caricato ({@code contentId}), necessario
 * per la successiva chiamata a {@link MercurioFirmaClient#firma}.
 *
 * TODO: i nomi esatti delle parti del multipart/form-data (qui assunti "file" per il contenuto binario e
 * "documentCreateForm" per i metadati JSON) e la convenzione di costruzione del path
 * (es. "/MINGG/PROTOCOLLO/UFF01/%date%") non sono specificati in modo univoco nella documentazione
 * consultata; da confermare con il referente funzionale Mercurio (cfr. §7.4 "Esempi di implementazione").
 *
 * @version 2.0
 */
public class MercurioDocumentaleClient {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private MercurioConfig mConfig;
	private MercurioAuthClient mAuthClient;

	public MercurioDocumentaleClient() {
		mConfig = MercurioConfig.getInstance();
		mAuthClient = new MercurioAuthClient();
	}

	public MercurioDocumentaleClient(MercurioAuthClient aAuthClient) {
		mConfig = MercurioConfig.getInstance();
		mAuthClient = aAuthClient;
	}

	/**
	 * Invoca il servizio di creazione documento con contenuti di Mercurio ({@code createStream}, §8.3.2)
	 * archiviando il documento (già firmato o meno) e restituendo l'identificativo Mercurio assegnato,
	 * insieme all'id del contenuto caricato.
	 *
	 * @param aRequest documento e metadati da archiviare.
	 * @return {@link MercurioArchiviazioneResponse} con {@code documentIdClient} e {@code contentId}.
	 * @throws MercurioIntegrationException in caso di errore di comunicazione o di archiviazione applicativa.
	 */
	public MercurioArchiviazioneResponse archivia(MercurioArchiviazioneRequest aRequest)
			throws MercurioIntegrationException {

		if (aRequest == null || aRequest.getContenuto() == null || aRequest.getContenuto().length == 0) {
			throw new MercurioIntegrationException("Documento da archiviare non valido (vuoto o nullo).", null);
		}

		String lDomainCode = mConfig.getDomainCode();
		String lApplicationCode = mConfig.getApplicationCode();
		String lContextCode = aRequest.getCodiceUfficio() != null ? aRequest.getCodiceUfficio() : mConfig.getContextCode();

		String lUrl = mConfig.getDocumentsBaseUrl() + "/" + lDomainCode + "/" + lApplicationCode + "/" + lContextCode
				+ "/create/stream";

		siesLogger.debug("MercurioDocumentaleClient.archivia(): invocazione " + lUrl + " file=" + aRequest.getNomeFile()
				+ " tipoDocumento=" + aRequest.getTipoDocumento());

		HttpClient lClient = MercurioHttpUtil.newHttpClient(mConfig);
		MultipartPostMethod lMethod = new MultipartPostMethod(lUrl);
		lMethod.setRequestHeader("Authorization", "Bearer " + mAuthClient.getValidToken());

		try {
			JSONObject lDocumentCreateForm = new JSONObject();
			lDocumentCreateForm.put("title", aRequest.getNomeFile() != null ? aRequest.getNomeFile() : aRequest.getTipoDocumento());
			lDocumentCreateForm.put("path", buildPath(aRequest));

			Part lFilePart = new FilePart("file",
					new ByteArrayPartSource(aRequest.getNomeFile(), aRequest.getContenuto()));
			Part lMetadataPart = new StringPart("documentCreateForm", lDocumentCreateForm.toString(), "UTF-8");
			lMethod.addPart(lFilePart);
			lMethod.addPart(lMetadataPart);
		} catch (Exception e) {
			throw new MercurioIntegrationException("Errore nella costruzione della richiesta multipart verso Mercurio.", e);
		}

		String lResponseBody = MercurioHttpUtil.execute(lClient, lMethod);
		JSONObject lRoot = MercurioHttpUtil.parseAndCheckSummary(lResponseBody);
		return parseArchiviazioneResponse(lRoot);
	}

	/**
	 * Costruisce il path documentale (es. "/MINGG/PROTOCOLLO/UFF01/%date%") a partire dai metadati della
	 * richiesta. Vedi TODO di classe: la convenzione esatta è da confermare con il referente funzionale.
	 */
	private String buildPath(MercurioArchiviazioneRequest aRequest) {
		StringBuilder lPath = new StringBuilder();
		lPath.append("/").append(mConfig.getApplicationCode());
		if (aRequest.getCodiceUfficio() != null) {
			lPath.append("/").append(aRequest.getCodiceUfficio());
		}
		if (aRequest.getIdFascicolo() != null) {
			lPath.append("/").append(aRequest.getIdFascicolo());
		}
		lPath.append("/%date%");
		return lPath.toString();
	}

	private MercurioArchiviazioneResponse parseArchiviazioneResponse(JSONObject aRoot) {
		MercurioArchiviazioneResponse lResponse = new MercurioArchiviazioneResponse();
		JSONObject lPayload = aRoot.optJSONObject("payload");
		if (lPayload == null) {
			lResponse.setEsito(false);
			return lResponse;
		}
		lResponse.setIdDocMercurio(lPayload.optString("documentIdClient", null));

		JSONObject lDocument = lPayload.optJSONObject("document");
		JSONObject lCurrentVersion = lDocument != null ? lDocument.optJSONObject("currentVersion") : null;
		JSONArray lContents = lCurrentVersion != null ? lCurrentVersion.optJSONArray("contents") : null;
		if (lContents != null && lContents.length() > 0) {
			JSONObject lFirstContent = lContents.optJSONObject(0);
			JSONObject lDocumentContent = lFirstContent != null ? lFirstContent.optJSONObject("documentContent") : null;
			if (lDocumentContent != null) {
				JSONObject lKey = lDocumentContent.optJSONObject("key");
				if (lKey != null) {
					lResponse.setContentId(lKey.optLong("contentId"));
				}
				lResponse.setFilename(lDocumentContent.optString("filename", null));
				lResponse.setContentType(lDocumentContent.optString("contentType", null));
			}
		}
		lResponse.setEsito(true);
		return lResponse;
	}
}
