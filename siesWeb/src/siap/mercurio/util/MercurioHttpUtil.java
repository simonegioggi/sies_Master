package siap.mercurio.util;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import f3b.log.LogF3B;
import siap.mercurio.config.MercurioConfig;
import siap.mercurio.exception.MercurioIntegrationException;

/**
 * MercurioHttpUtil - Utility condivisa dai client REST del package {@code siap.mercurio} per
 * l'esecuzione delle chiamate HTTP verso Mercurio e per la lettura della busta "summary" comune a
 * tutte le risposte JSON di Mercurio (cfr. Mercurio_LineeGuidaSviluppo_v1.0.docx, es. §8.1.1, §8.3.1).
 *
 * Basata su Commons HttpClient 2.0.2, già presente tra le dipendenze del progetto SIES (cfr.
 * {@code siap.web.SiapHttpClient}, {@code commons-httpclient-2.0.2.jar}): questa versione della
 * libreria NON dispone delle classi {@code RequestEntity}/{@code AuthScope} introdotte in HttpClient
 * 3.x, per cui il body delle richieste JSON è impostato tramite
 * {@link EntityEnclosingMethod#setRequestBody(java.io.InputStream)} e le credenziali tramite
 * {@link HttpState#setCredentials(String, org.apache.commons.httpclient.Credentials)} con realm nullo
 * (valido per qualsiasi realm).
 *
 * @version 1.0
 */
public final class MercurioHttpUtil {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private MercurioHttpUtil() {
	}

	/**
	 * Crea un {@link HttpClient} con i timeout di connessione/lettura configurati in {@link MercurioConfig}.
	 */
	public static HttpClient newHttpClient(MercurioConfig aConfig) {
		HttpClient lClient = new HttpClient();
		lClient.setConnectionTimeout(aConfig.getConnectTimeoutMs());
		lClient.setTimeout(aConfig.getReadTimeoutMs());
		return lClient;
	}

	/**
	 * Configura sul client le credenziali (HTTP Basic Authentication) dell'utente ADN tecnico usato per
	 * il login su Mercurio, in modalità preemptive (l'header Authorization viene inviato fin dalla prima
	 * richiesta, senza attendere la challenge 401 del server).
	 */
	public static void applyBasicAuth(HttpClient aClient, String aUsername, String aPassword) {
		UsernamePasswordCredentials lCredentials = new UsernamePasswordCredentials(aUsername, aPassword);
		HttpState lState = new HttpState();
		lState.setCredentials(null, lCredentials);
		lState.setAuthenticationPreemptive(true);
		aClient.setState(lState);
	}

	/**
	 * Imposta come request body JSON il contenuto dell'oggetto fornito, con encoding UTF-8 esplicito
	 * (Content-Type: application/json).
	 */
	public static void setJsonRequestBody(EntityEnclosingMethod aMethod, JSONObject aBody)
			throws MercurioIntegrationException {
		try {
			byte[] lBytes = aBody.toString().getBytes("UTF-8");
			aMethod.setRequestBody(new ByteArrayInputStream(lBytes));
			aMethod.setRequestContentLength(lBytes.length);
			aMethod.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new MercurioIntegrationException("Errore nella codifica UTF-8 della richiesta JSON verso Mercurio.", e);
		}
	}

	/**
	 * Esegue il metodo HTTP fornito e restituisce il body della risposta come stringa, verificando che
	 * lo status HTTP sia 2xx. In caso di errore di trasporto o status non atteso viene sollevata una
	 * {@link MercurioIntegrationException}.
	 */
	public static String execute(HttpClient aClient, HttpMethod aMethod) throws MercurioIntegrationException {
		try {
			int lStatus = aClient.executeMethod(aMethod);
			String lBody = aMethod.getResponseBodyAsString();
			if (lStatus < HttpStatus.SC_OK || lStatus >= HttpStatus.SC_MULTIPLE_CHOICES) {
				siesLogger.error("MercurioHttpUtil.execute(): HTTP " + lStatus + " invocando " + aMethod.getPath()
						+ " - body=" + lBody);
				throw new MercurioIntegrationException(String.valueOf(lStatus),
						"Chiamata al servizio Mercurio fallita con status HTTP " + lStatus, null);
			}
			return lBody;
		} catch (MercurioIntegrationException e) {
			throw e;
		} catch (Exception e) {
			siesLogger.error("MercurioHttpUtil.execute(): errore invocando " + aMethod.getPath(), e);
			throw new MercurioIntegrationException("Errore di comunicazione con il servizio Mercurio.", e);
		} finally {
			aMethod.releaseConnection();
		}
	}

	/**
	 * Verifica la sezione {@code summary} presente in ogni risposta Mercurio (cfr. es. risposta di
	 * {@code login}, {@code create}, {@code signContent}): {@code summary.error=true} indica un errore
	 * applicativo, il cui messaggio viene riportato in {@code summary.messages}.
	 *
	 * @throws MercurioIntegrationException se {@code summary.error} è true o la risposta non è un JSON valido.
	 */
	public static JSONObject parseAndCheckSummary(String aResponseBody) throws MercurioIntegrationException {
		JSONObject lRoot;
		try {
			lRoot = new JSONObject(aResponseBody);
		} catch (JSONException e) {
			throw new MercurioIntegrationException("Risposta di Mercurio non valida (JSON non parsabile).", e);
		}
		JSONObject lSummary = lRoot.optJSONObject("summary");
		if (lSummary != null && lSummary.optBoolean("error", false)) {
			String lCode = lSummary.optString("code", null);
			String lMessage = lSummary.optString("code", "Errore Mercurio non specificato");
			JSONArray lMessages = lSummary.optJSONArray("messages");
			if (lMessages != null) {
				try {
					lMessage = lMessages.join(" | ");
				} catch (JSONException e) {
					// messaggi non concatenabili: mantiene il messaggio di fallback già impostato.
					siesLogger.warn("MercurioHttpUtil.parseAndCheckSummary(): errore nel join dei messaggi Mercurio.", e);
				}
			}
			throw new MercurioIntegrationException(lCode, "Mercurio ha restituito un errore: " + lMessage, null);
		}
		return lRoot;
	}
}
