package siap.sico.test.model;

import java.util.Vector;

import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * Title: TestModel
 * Description: Model del Test del sistema
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TestModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8324433614796801232L;

	private String mTestJMS;
	private String mTestDataBase;
	private String mTestReport;
	private String mDataOra;
	private String mSistemaOperativo;
	private String mVersioneJDK;
	private String mPathInstallazioneJDK;
	private String mPathTomcat;
	private String mPathOpenJMS;
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	private String siesLogger4J;
	private String mProperties;
	private String mBDIMittente;
	private String mBDIindirizzoMittente;
	private String mErroreJMS;
	private String mCurrentVersion;
	private String mSequence;
	private String mErroreProgressivo;
	private String mJVMTotalMemory;
	private Vector mBDI;
	private String mTestWSIscriviProvvedimentoEsecuzione;
	private String mTestWSIscriviProvvedimentoProvvisorio;
	private String mTestWSTrasferisciFoglioComplementare;
	private String mTestWSRichiestaCertificato;
	private String mTestWebServer;
	// MEV_2023-33 aggiunte due variabili per endpoint address PagoPA-PST (con get&set)
	private String mTestWSServiziInvioPagamentiTelematici;
	private String mTestWSServiziConsultazionePagamentiTelematici;

	// protected static StringManager sm = StringManager.getManager("org.apache.catalina.servlets");

	public TestModel() {

		mTestJMS = "";
		mTestDataBase = "";
		mTestReport = "";
		mDataOra = DateUtils.getSysDate("dd MMM yyyy HH:mm:ss");
		mSistemaOperativo = System.getProperty("os.name") + " " + System.getProperty("os.version") + " "
				+ System.getProperty("sun.os.patch.level");
		mVersioneJDK = System.getProperty("java.version");
		mPathInstallazioneJDK = System.getProperty("java.home");
		// mPathTomcat = System.getProperty("user.dir");
		mPathTomcat = System.getProperty("jboss.home.dir");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger4J = System.getProperty("log4j.configuration");
		mProperties = System.getProperty("path.properties");
		mPathOpenJMS = System.getProperty("openjms.home");
		mSequence = "";
		mCurrentVersion = "";
		mBDIMittente = "";
		mErroreJMS = "";
		mErroreProgressivo = null;
		mJVMTotalMemory = null;
	}

	public String getTestJMS() {
		return mTestJMS;
	}

	public String getTestDataBase() {
		return mTestDataBase;
	}

	public String getTestReport() {
		return mTestReport;
	}

	public String getDataOra() {
		return mDataOra;
	}

	public String getSistemaOperativo() {
		return mSistemaOperativo;
	}

	public String getVersioneJDK() {
		return mVersioneJDK;
	}

	public String getPathInstallazioneJDK() {
		return mPathInstallazioneJDK;
	}

	public String getPathTomcat() {
		return mPathTomcat;
	}

	public String getPathOpenJMS() {
		return mPathOpenJMS;
	}

	public String getLog4J() {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		return siesLogger4J;
	}

	public String getProperties() {
		return mProperties;
	}

	public Vector getBDI() {
		return mBDI;
	}

	public String getBDIMittente() {
		return mBDIMittente;
	}

	public String getErroreJMS() {
		return mErroreJMS;
	}

	public String getBDIindirizzoMittente() {
		return mBDIindirizzoMittente;
	}

	public String getSequence() {
		return mSequence;
	}

	public String getCurrentVersion() {
		return mCurrentVersion;
	}

	public String getErroreProgressivo() {
		return mErroreProgressivo;
	}

	public String getJVMTotalMemory() {
		return mJVMTotalMemory;
	}

	public void setTestJMS(String aValore) {
		mTestJMS = aValore;
	}

	public void setTestDataBase(String aValore) {
		mTestDataBase = aValore;
	}

	public void setTestReport(String aValore) {
		mTestReport = aValore;
	}

	public void setDataOra(String aValore) {
		mDataOra = aValore;
	}

	public void setBDI(Vector aValore) {
		mBDI = new Vector(aValore);
	}

	public void setBDIMittente(String aValore) {
		mBDIMittente = aValore;
	}

	public void setErroreJMS(String aValore) {
		mErroreJMS = aValore;
	}

	public void setBDIindirizzoMittente(String aValore) {
		mBDIindirizzoMittente = aValore;
	}

	public void setSequence(String aValore) {
		mSequence = aValore;
	}

	public void setCurrentVersion(String aValore) {
		mCurrentVersion = aValore;
	}

	public void setErroreProgressivo(String aValore) {
		mErroreProgressivo = aValore;
	}

	public void setJVMTotalMemory(String aValore) {
		mJVMTotalMemory = aValore;
	}

	public String getTestWSIscriviProvvedimentoEsecuzione() {
		return mTestWSIscriviProvvedimentoEsecuzione;
	}

	public void setTestWSIscriviProvvedimentoEsecuzione(String mTestWSIscriviProvvedimentoEsecuzione) {
		this.mTestWSIscriviProvvedimentoEsecuzione = mTestWSIscriviProvvedimentoEsecuzione;
	}

	public String getTestWSIscriviProvvedimentoProvvisorio() {
		return mTestWSIscriviProvvedimentoProvvisorio;
	}

	public void setTestWSIscriviProvvedimentoProvvisorio(String mTestWSIscriviProvvedimentoProvvisorio) {
		this.mTestWSIscriviProvvedimentoProvvisorio = mTestWSIscriviProvvedimentoProvvisorio;
	}

	public String getTestWebServer() {
		return mTestWebServer;
	}

	public void setTestWebServer(String mTestWebServer) {
		this.mTestWebServer = mTestWebServer;
	}

	public String getTestWSTrasferisciFoglioComplementare() {
		return mTestWSTrasferisciFoglioComplementare;
	}

	public void setTestWSTrasferisciFoglioComplementare(String mTestWSTrasferisciFoglioComplementare) {
		this.mTestWSTrasferisciFoglioComplementare = mTestWSTrasferisciFoglioComplementare;
	}

	public String getTestWSRichiestaCertificato() {
		return mTestWSRichiestaCertificato;
	}

	public void setTestWSRichiestaCertificato(String mTestWSRichiestaCertificato) {
		this.mTestWSRichiestaCertificato = mTestWSRichiestaCertificato;
	}

	public String getTestWSServiziInvioPagamentiTelematici() {
		return mTestWSServiziInvioPagamentiTelematici;
	}

	public void setTestWSServiziInvioPagamentiTelematici(String mTestWSServiziInvioPagamentiTelematici) {
		this.mTestWSServiziInvioPagamentiTelematici = mTestWSServiziInvioPagamentiTelematici;
	}

	public String getTestWSServiziConsultazionePagamentiTelematici() {
		return mTestWSServiziConsultazionePagamentiTelematici;
	}

	public void setTestWSServiziConsultazionePagamentiTelematici(
			String mTestWSServiziConsultazionePagamentiTelematici) {
		this.mTestWSServiziConsultazionePagamentiTelematici = mTestWSServiziConsultazionePagamentiTelematici;
	}

}