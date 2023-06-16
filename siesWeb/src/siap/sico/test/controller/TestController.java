package siap.sico.test.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Vector;

import javax.jms.QueueConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.WebServiceException;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.F3BProperties;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import siap.jms.config.JMSProperties;
import siap.jms.connection.ConnectionPoolJMS;
import siap.jms.jmscode.model.JmsCodeModel;
import siap.sico.evento.model.XModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.test.model.TestModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.versione.util.VersionProperties;
import siap.sico.webservice.config.NscProperties;

/**
 * Title: TestController Description: Classe che realizza il test del sistema SIES interrogando varie
 * componenti del nostro sistema e compilando il documento di test
 */
@SuppressWarnings("rawtypes")
public class TestController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Unico metodo publico che stampa i risultati del test del sistema
	 *
	 * @param aUtente
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExTestSistema(UtenteModel aUtente, UfficioModel aUfficio)
			throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;
		TestModel lTest = new TestModel();

		// Test OpenJMS
		try {
			Vector allBDI = JMSProperties.getInstance().getAllBDI();
			lTest.setBDI(allBDI);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Partito il test di connessione OpenJMS.");
			ConnectionPoolJMS mPoolConnection = ConnectionPoolJMS.getInstance();
			QueueConnection lQueueConn = mPoolConnection.getConnection();
			mPoolConnection.releaseConnection(lQueueConn);
			lTest.setTestJMS("Test OpenJMS terminato con successo!");
		} catch (Exception ex) {
			lTest.setTestJMS("Test OpenJMS Fallito!");
			lTest.setErroreJMS(ex.toString());
		}

		try {
			// NUOVA INFRASTRUTTURA: aggiunti controlli sui WS
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Partito il test di connessione dei WS.");
			lTest.setTestWebServer(interrogateWS("webserver"));
			lTest.setTestWSIscriviProvvedimentoProvvisorio(interrogateWS("wsipp"));
			lTest.setTestWSIscriviProvvedimentoEsecuzione(interrogateWS("wsipe"));
			lTest.setTestWSTrasferisciFoglioComplementare(interrogateWS("wstfc"));
			lTest.setTestWSRichiestaCertificato(interrogateWS("wsrc"));
			// MEV_2023-33 aggiunti due controlli per endpoint address PagoPA-PST
			lTest.setTestWSServiziInvioPagamentiTelematici(interrogateWS("wssipt"));
			lTest.setTestWSServiziConsultazionePagamentiTelematici(interrogateWS("wsscpt"));
		} catch (Exception ex) {
			lTest.setTestWebServer(ex.toString());
			lTest.setTestWSIscriviProvvedimentoProvvisorio(ex.toString());
			lTest.setTestWSIscriviProvvedimentoEsecuzione(ex.toString());
			lTest.setTestWSTrasferisciFoglioComplementare(ex.toString());
			lTest.setTestWSRichiestaCertificato(ex.toString());
			// MEV_2023-33 aggiunti due controlli per endpoint address PagoPA-PST
			lTest.setTestWSServiziInvioPagamentiTelematici(ex.toString());
			lTest.setTestWSServiziConsultazionePagamentiTelematici(ex.toString());
		}

		String lBDINome = JMSProperties.getInstance().getProperty("JMS_LOCAL_MITTENTE");
		lTest.setBDIMittente(lBDINome);
		String lBDI = JMSProperties.getInstance().getProperty("JMS_LOCAL");
		lTest.setBDIindirizzoMittente(lBDI);
		String lStrSeq = F3BProperties.getProperty("sequence.command");

		if (lStrSeq != null) {
			int lIntProgressivo = lStrSeq.indexOf("'");
			String lProgressivo = "";
			if (lIntProgressivo > 0) {
				lProgressivo = lStrSeq.substring(lIntProgressivo + 1, lIntProgressivo + 3);
				lTest.setSequence(lProgressivo);
			}
			Vector lProgr = JMSProperties.getInstance().getProgressiviBDI();

			Iterator lItx = lProgr.iterator();

			boolean lTrovato = false;
			while (lItx.hasNext()) {
				JmsCodeModel lCode = (JmsCodeModel) lItx.next();
				if (lBDINome.compareTo(lCode.getCodice()) == 0) {
					if (lProgressivo.compareTo(lCode.getDescrizione()) != 0) {
						lTest.setErroreProgressivo(
								"con progressivo ERRATO. Nel file di configurazione e' presente "
										+ lProgressivo + " ma dovrebbe essere " + lCode.getDescrizione()
										+ ". Provvedere alla sostituzione del paramentro nel file "
										+ "f3b.properties nella directory " + lTest.getProperties() + ".");
					}
					lTrovato = true;
				}
			}

			if (!lTrovato) {
				lTest.setErroreProgressivo("con progressivo ERRATO. Nel file di configurazione e' presente "
						+ lProgressivo + " ma la descrizione della BDI non trova riscontro nella tabella "
						+ "dei CodiciBDI JMS_CODE.");
			}
		}

		String lVersion = VersionProperties.getVersion();
		lTest.setCurrentVersion(lVersion);

		// JVM Memory
		lTest.setJVMTotalMemory("" + Runtime.getRuntime().totalMemory());

		TreeModel lTree = prelevaDati(lTest, aUtente, aUfficio);

		String lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIES_TEST");

		ReportGenerator lReport = new ReportGenerator();
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

		return lByteArrayOut;
	}

	/**
	 * Preleva i dati del test
	 *
	 * @param aUtente
	 * @return
	 */
	private TreeModel prelevaDati(TestModel lTest, UtenteModel aUtente, UfficioModel aUfficio)
			throws F3BException {

		TreeModel lTreeRoot = new TreeModel(createRootTest(aUtente, aUfficio));

		TreeModel lTreeTest = new TreeModel(lTest);
		// Test Database andato ok se si e' arrivati fino a qui....
		if (lTest.getBDI() != null) {
			Iterator lItx = lTest.getBDI().iterator();
			while (lItx.hasNext()) {
				JmsCodeModel lCod = (JmsCodeModel) lItx.next();
				// Ticket 20210507017 - veniva sovreascritto il codice BDI del singleton con la COnnection
				// string, corrompendo il contenuto del singleton. Commentato il vecchio codice e corretto.
				JmsCodeModel perStampa = new JmsCodeModel(lCod);
				perStampa.setCodice(JMSProperties.getInstance().getConnectionString(lCod.getDescrizione()));
				lTreeTest.add(new TreeModel(perStampa));
				// lCod.setCodice(JMSProperties.getInstance().getConnectionString(lCod.getDescrizione()));
				// lTreeTest.add(new TreeModel(lCod));
				// FINE Ticket 20210507017
			}
		}
		// TestOpenJMS
		lTreeRoot.add(lTreeTest);
		lTreeRoot.add(new TreeModel(aUtente));

		return lTreeRoot;
	}

	/**
	 * Crea la root del Documento
	 *
	 * @param aEveModel
	 * @return
	 */
	public XModel createRootTest(UtenteModel aUtenteModel, UfficioModel aUfficio) throws F3BException {

		XModel lStampa = new XModel();
		lStampa.setUfficio(aUfficio.getDescrComune().toUpperCase());
		lStampa.setTipoUfficio(aUfficio.getDescrTipoUfficio().toUpperCase());
		lStampa.setDataElaborazione(DateUtils.getSysDate());

		return lStampa;
	}

	/**
	 * NUOVA INFRASTRUTTURA: interrogo il WS
	 *
	 * @param cases
	 * @return String
	 * @throws F3BException
	 */
	private String interrogateWS(String cases) throws F3BException {

		// MEV_2023-33 aggiunte due variabili per endpoint address PagoPA-PST
		String messaggio = "", iscriviProvvedimentoProvvisorio = "", iscriviProvvedimentoEsecuzione = "",
				trasferisciFoglioComplementare = "", richiestaCertificato = "",
				serviziInvioPagamentiTelematici = "", serviziConsultazionePagamentiTelematici = "";
		NscProperties nscProperty = NscProperties.getInstance();
		try {
			if ("webserver".equals(cases)) {
				String nscServer = nscProperty.getProperty("NscServer");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Indirizzo pagina Web SIES NSC: #" + nscServer + "#");
				String[] resp = testConnection(nscServer);
				if ("200".equals(resp[0]))
					messaggio = "La pagina Web di Interconnessione SIES-NSC e' disponibile "
							+ "all'indirizzo: " + nscServer;
				else
					messaggio = "ERRORE: " + resp[1] + ". Pagina web non disponibile all'indirizzo: "
							+ nscServer;
			} else if ("wsipe".equals(cases)) {
				iscriviProvvedimentoEsecuzione = nscProperty.getProperty("nsc.address");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Indirizzo WS ISCRIZIONE PROVVEDIMENTO ESECUZIONE: #"
						+ iscriviProvvedimentoEsecuzione + "#");
				String[] resp = testConnection(iscriviProvvedimentoEsecuzione);
				if ("200".equals(resp[0]))
					messaggio = "Il Web Service di Iscrizione Provvedimento Esecutivo e' disponibile "
							+ "all'indirizzo: " + iscriviProvvedimentoEsecuzione;
				else
					messaggio = "ERRORE: " + resp[1] + ". Web Service non disponibile all'indirizzo: "
							+ iscriviProvvedimentoEsecuzione;
			} else if ("wsipp".equals(cases)) {
				iscriviProvvedimentoProvvisorio = nscProperty.getProperty("NscWsAddress");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Indirizzo WS ISCRIZIONE PROVVEDIMENTO PROVVISORIO: #"
						+ iscriviProvvedimentoProvvisorio + "#");
				String[] resp = testConnection(iscriviProvvedimentoProvvisorio);
				if ("200".equals(resp[0]))
					messaggio = "Il Web Service di Iscrizione Provvedimento Provvisorio e' disponibile "
							+ "all'indirizzo: " + iscriviProvvedimentoProvvisorio;
				else
					messaggio = "ERRORE: " + resp[1] + ". Web Service non disponibile all'indirizzo: "
							+ iscriviProvvedimentoProvvisorio;
			} else if ("wstfc".equals(cases)) {
				trasferisciFoglioComplementare = nscProperty.getProperty("tfc.address");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Indirizzo WS TRASFERIMENTO FOGLIO COMPLEMENTARE: #"
						+ trasferisciFoglioComplementare + "#");
				String[] resp = testConnection(trasferisciFoglioComplementare);
				if ("200".equals(resp[0]))
					messaggio = "Il Web Service di Trasferimento Foglio Complementare e' disponibile "
							+ "all'indirizzo: " + trasferisciFoglioComplementare;
				else
					messaggio = "ERRORE: " + resp[1] + ". Web Service non disponibile all'indirizzo: "
							+ trasferisciFoglioComplementare;
			} else if ("wsrc".equals(cases)) {
				richiestaCertificato = nscProperty.getProperty("WsAddressSippiWeb");
				siesLogger.info("Indirizzo WS RICHIESTA CERTIFICATO: #" + richiestaCertificato + "#");
				String[] resp = testConnection(richiestaCertificato);
				if ("200".equals(resp[0]))
					messaggio = "Il Web Service di Richiesta Certificato e' disponibile all'indirizzo: "
							+ richiestaCertificato;
				else
					messaggio = "ERRORE: " + resp[1] + ". Web Service non disponibile all'indirizzo: "
							+ richiestaCertificato;
			} else if ("wssipt".equals(cases)) {
				// MEV_2023-33 aggiunti due casi per endpoint address PagoPA-PST
				serviziInvioPagamentiTelematici = F3BProperties.getProperty("EAPPA_SIPT");
				siesLogger.info("Indirizzo WS SERVIZI INVIO PAGAMENTI TELEMATICI: #"
						+ serviziInvioPagamentiTelematici + "#");
				disableSslVerification();
				String[] resp = testConnection(serviziInvioPagamentiTelematici);
				// String[] resp = testConnectionPagoPA(serviziInvioPagamentiTelematici);
				if ("200".equals(resp[0]))
					messaggio = "Il Web Service di Servizi Invio Pagamenti Telematici e' disponibile "
							+ "all'indirizzo: " + serviziInvioPagamentiTelematici;
				else
					messaggio = "ERRORE: " + resp[1] + ". Web Service non disponibile all'indirizzo: "
							+ serviziInvioPagamentiTelematici;
			} else if ("wsscpt".equals(cases)) {
				// MEV_2023-33 aggiunti due casi per endpoint address PagoPA-PST
				serviziConsultazionePagamentiTelematici = F3BProperties.getProperty("EAPPA_SCPT");
				siesLogger.info("Indirizzo WS SERVIZI CONSULTAZIONE PAGAMENTI TELEMATICI: #"
						+ serviziConsultazionePagamentiTelematici + "#");
				disableSslVerification();
				String[] resp = testConnection(serviziConsultazionePagamentiTelematici);
				// String[] resp = testConnectionPagoPA(serviziConsultazionePagamentiTelematici);
				if ("200".equals(resp[0]))
					messaggio = "Il Web Service di Servizi Consultazione Pagamenti Telematici e' disponibile"
							+ " all'indirizzo: " + serviziConsultazionePagamentiTelematici;
				else
					messaggio = "ERRORE: " + resp[1] + ". Web Service non disponibile all'indirizzo: "
							+ serviziConsultazionePagamentiTelematici;
			} else
				messaggio = "ERRORE GENERICO";
		} catch (MalformedURLException murle) {
			messaggio = "ERRORE: URL inesistente";
			murle.printStackTrace();
		} catch (RemoteException re) {
			messaggio = "ERRORE: " + re.getMessage();
			re.printStackTrace();
		} catch (WebServiceException wse) {
			messaggio = "ERRORE: " + wse.getMessage();
			wse.printStackTrace();
		} catch (IOException ioe) {
			if (ioe instanceof UnknownHostException)
				messaggio = "ERRORE: il Web Service " + ioe.getMessage() + " NON e' disponibile";
			else
				messaggio = "ERRORE: " + ioe.getMessage();
			ioe.printStackTrace();
		} finally {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(messaggio);
		}
		// valore di ritorno
		return messaggio;
	}

	/**
	 * Metodo che ritorna la risposta al test di connessione
	 *
	 * @param stringaConnessione
	 * @return String[]
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private String[] testConnection(String stringaConnessione) throws MalformedURLException, IOException {

		URL url = new URL(stringaConnessione);
		HttpsURLConnection connections;
		HttpURLConnection connection;
		String code, msg;
		if (stringaConnessione.contains("https")) {
			connections = (HttpsURLConnection) url.openConnection();
			// CODICE VALIDO SOLO IN LOCALHOST
			// HostnameVerifier v = new HostnameVerifier() {
			// @Override
			// public boolean verify(String hostname, SSLSession session) {
			// return true;
			// }
			// };
			// connections.setHostnameVerifier(v);
			code = "" + connections.getResponseCode();
			msg = connections.getResponseMessage();
		} else {
			connection = (HttpURLConnection) url.openConnection();
			code = "" + connection.getResponseCode();
			msg = connection.getResponseMessage();
		}
		String[] resp = new String[2];
		resp[0] = code;
		resp[1] = msg;
		return resp;
	}

	/*
	 * ISSUE MEV : aggiunto metodo di test connessione Numero MEV : 33 Autore : sgioggi Data : 8 giu 2023
	 * Branch : MEV_33
	 */
	// private String[] testConnectionPagoPA(String endpointAddress) throws F3BException, RemoteException {
	//
	// String code = "200", msg = "";
	// if (endpointAddress.contains("Consultazione")) {
	// ServiziConsultazionePagamentiTelematiciBeanServiceLocator scptbsl = new
	// ServiziConsultazionePagamentiTelematiciBeanServiceLocator();
	// scptbsl.setServiziConsultazionePagamentiTelematiciSOAPPortEndpointAddress(endpointAddress);
	// ServiziConsultazionePagamentiTelematici scpt;
	// try {
	// scpt = scptbsl.getServiziConsultazionePagamentiTelematiciSOAPPort();
	// scpt.elencoPagamenti("", "", "", "", "", "", null, null, 0, 0);
	// } catch (ServiceException e) {
	// code = "-1";
	// msg = e.getMessage();
	// throw new F3BException(e);
	// } catch (RemoteException e) {
	// code = "-1";
	// msg = e.getMessage();
	// throw e;
	// }
	// } else {
	// ServiziInvioPagamentiTelematiciBeanServiceLocator service = new
	// ServiziInvioPagamentiTelematiciBeanServiceLocator();
	// service.setServiziInvioPagamentiTelematiciSOAPPortEndpointAddress(endpointAddress);
	// ServiziInvioPagamentiTelematici sipt;
	// try {
	// sipt = service.getServiziInvioPagamentiTelematiciSOAPPort();
	// AnagraficaSoggetto as1 = new AnagraficaSoggetto("", "", "", "", "", "", "", "", "", "", "");
	// AnagraficaSoggetto as2 = new AnagraficaSoggetto("", "", "", "", "", "", "", "", "", "", "");
	// DatiSingoloVersamento[] dsvs = new DatiSingoloVersamento[1];
	// DatiSingoloVersamento dsv = new DatiSingoloVersamento(null, "", "", null);
	// dsvs[0] = dsv;
	// DatiVersamento dv = new DatiVersamento(null, "", "", dsvs);
	// sipt.generaAvviso(new RichiestaPagamentoTelematico("", "", "", as1, as2, dv, null));
	// // sipt.generaAvviso(new RichiestaPagamentoTelematico("", "", "", null, null, null, null));
	// } catch (ServiceException e) {
	// if (!Utils.isNullObj(e) && e.toString().contains("soggettoPagatore")) {
	// msg = e.getMessage();
	// } else {
	// code = "-1";
	// msg = e.getMessage();
	// throw new F3BException(e);
	// }
	// } catch (RemoteException e) {
	// if (!Utils.isNullObj(e) && e.toString().contains("soggettoPagatore")) {
	// msg = e.getMessage();
	// } else {
	// code = "-1";
	// msg = e.getMessage();
	// throw e;
	// }
	// }
	// }
	//
	// String[] resp = new String[2];
	// resp[0] = code;
	// resp[1] = msg;
	// // valore di ritorno
	// return resp;
	// }
	// ***** FINE INTERVENTO MEV_33 *****//

	private static void disableSslVerification() {
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

}