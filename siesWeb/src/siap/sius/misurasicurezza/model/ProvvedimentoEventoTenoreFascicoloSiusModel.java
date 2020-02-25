package siap.sius.misurasicurezza.model;

import f3b.model.GenericModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: ProvvedimentoEventoTenoreFascicoloSiusModel
 * </p>
 * <p>
 * Description: Model per la Ricerca dei FASCICOLI nell'iscrizione del procedimento di Misura Sicurezza Emessa
 * Fuori Sentenza e per L'iscrizione Annotazione Decisioni della Sorveglianza nella applicazione Misure
 * Sicurezza
 * </p>
 */
public class ProvvedimentoEventoTenoreFascicoloSiusModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -527211030731424593L;
	private FascicoloSiusModel mFascicolo;
	private String mDescrTipoUfficio;
	private String mDescrComuneUfficio;
	private EventoModel mEvento;
	private DepositoOrdinanzaPcModel mOrdinanza;
	private String mDescrProvvedimento;
	private String mDescrOggetto;
	private String mDescrEsito;
	private TenoreModel mTenore;
	private SoggettoModel mSoggetto;
	private DepositoDecretoModel mDecreto;
	private String mTipoEsitoMisura;

	// COSTRUTTORE DI DEFAULT
	public ProvvedimentoEventoTenoreFascicoloSiusModel() {
		this.mFascicolo = null;
		this.mDescrTipoUfficio = "";
		this.mDescrComuneUfficio = "";
		this.mEvento = null;
		this.mOrdinanza = null;
		this.mDescrProvvedimento = "";
		this.mDescrOggetto = "";
		this.mDescrEsito = "";
		this.mTenore = null;
		this.mSoggetto = null;
		this.mTipoEsitoMisura = "";
		this.mEvento = null;
	}

	// COSTRUTTORE DI COPIA
	public ProvvedimentoEventoTenoreFascicoloSiusModel(ProvvedimentoEventoTenoreFascicoloSiusModel aModel) {
		this.mFascicolo = new FascicoloSiusModel(aModel.mFascicolo);
		this.mDescrTipoUfficio = aModel.mDescrTipoUfficio;
		this.mDescrComuneUfficio = aModel.mDescrComuneUfficio;
		this.mEvento = new EventoModel(aModel.mEvento);
		this.mOrdinanza = new DepositoOrdinanzaPcModel(aModel.mOrdinanza);
		this.mDescrProvvedimento = aModel.mDescrProvvedimento;
		this.mDescrOggetto = aModel.mDescrOggetto;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mTenore = new TenoreModel(aModel.mTenore);
		this.mSoggetto = new SoggettoModel(aModel.mSoggetto);
		this.mTipoEsitoMisura = aModel.mTipoEsitoMisura;
	}

	// COSTRUTTORE MODEL
	public ProvvedimentoEventoTenoreFascicoloSiusModel(FascicoloSiusModel aFascicoloSiusModel,
			String aDescrTipoUfficio, String aDescrComuneUfficio, EventoModel aEvento,
			DepositoOrdinanzaPcModel aOrdinanza, String aDescrProvvedimento, String aDescrOggetto,
			String aDescrEsito, TenoreModel aTenore, SoggettoModel aSoggetto, String aTipoEsitoMisura,
			DepositoDecretoModel aDecreto) {
		this.mFascicolo = aFascicoloSiusModel;
		this.mDescrTipoUfficio = aDescrTipoUfficio;
		this.mDescrComuneUfficio = aDescrComuneUfficio;
		this.mEvento = aEvento;
		this.mOrdinanza = aOrdinanza;
		this.mDescrProvvedimento = aDescrProvvedimento;
		this.mDescrOggetto = aDescrOggetto;
		this.mDescrEsito = aDescrEsito;
		this.mTenore = aTenore;
		this.mSoggetto = aSoggetto;
		this.mTipoEsitoMisura = aTipoEsitoMisura;
		this.mDecreto = aDecreto;
	}

	// METODI GET()

	public FascicoloSiusModel getFascicoloSiusModel() {
		return mFascicolo;
	}

	public String getDescrTipoUfficio() {
		return mDescrTipoUfficio;
	}

	public String getDescrComuneUfficio() {
		return mDescrComuneUfficio;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

	public DepositoOrdinanzaPcModel getOrdinanza() {
		return mOrdinanza;
	}

	public String getDescrProvvedimento() {
		return mDescrProvvedimento;
	}

	public String getDescrOggetto() {
		return mDescrOggetto;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public TenoreModel getTenore() {
		return mTenore;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public String getTipoEsitoMisura() {
		return mTipoEsitoMisura;
	}

	public DepositoDecretoModel getDecreto() {
		return mDecreto;
	}

	// METODI SET()

	public void setFascicoloSius(FascicoloSiusModel aValore) {
		mFascicolo = aValore;
	}

	public void setDescrTipoUfficio(String aValore) {
		mDescrTipoUfficio = aValore;
	}

	public void setDescrComuneUfficio(String aValore) {
		mDescrComuneUfficio = aValore;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public void setOrdinanza(DepositoOrdinanzaPcModel aValore) {
		mOrdinanza = aValore;
	}

	public void setDescrProvvedimento(String aValore) {
		mDescrProvvedimento = aValore;
	}

	public void setDescrOggetto(String aValore) {
		mDescrOggetto = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setTenore(TenoreModel aValore) {
		mTenore = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setTipoEsitoMisura(String aValore) {
		mTipoEsitoMisura = aValore;
	}

	public void setDecreto(DepositoDecretoModel aValore) {
		mDecreto = aValore;
	}

}