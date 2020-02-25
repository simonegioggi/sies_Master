package siap.sius.depositodecreto.model;

import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: OrdinanzaEventoTenoriFascicoloSiusModel
 * </p>
 * <p>
 * Description: Model per la Ricerca dei FASCICOLI nell'iscrizione del procedimento di Misura Sicurezza Emessa
 * Fuori Sentenza e per L'iscrizione Annotazione Decisioni della Sorveglianza nella applicazione Misure
 * Sicurezza
 * </p>
 * <p>
 * Modificato il 12/11/2014 (Aggiunto Model Soggetto)
 * </p>
 */
public class DecretoEventoTenoriFascicoloSiusModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 3706001005456495222L;
	private FascicoloSiusModel mFascicolo;
	private String mDescrTipoUfficio;
	private String mDescrComuneUfficio;
	private EventoModel mEvento;
	private DepositoDecretoModel mDecreto;
	private String mDescrProvvedimento;
	private String mDescrOggetto;
	private String mDescrEsito;
	private String mCodEsitoAlt3;
	private TenoreModel mTenore;
	private SoggettoModel mSoggetto;
	private MisuraAlternativaModel mMisuraAlternativa;	
    private String mAnnoProtocollo;
    private String mNumeroProtocollo;
	
	
	// COSTRUTTORE DI DEFAULT
	public DecretoEventoTenoriFascicoloSiusModel() {

		this.mFascicolo = null;
		this.mDescrTipoUfficio = "";
		this.mDescrComuneUfficio = "";
		this.mEvento = null;
		this.mDecreto = null;
		this.mDescrProvvedimento = "";
		this.mDescrOggetto = "";
		this.mDescrEsito = "";
		this.mCodEsitoAlt3 = "";
		this.mTenore = null;
		this.mSoggetto = null;
		// MEV_39: aggiunta valorizzazione di variabile
		this.mMisuraAlternativa = null;		
		this.mAnnoProtocollo = null;
		this.mNumeroProtocollo = null;
	}

	// COSTRUTTORE DI COPIA
	public DecretoEventoTenoriFascicoloSiusModel(DecretoEventoTenoriFascicoloSiusModel aModel) {

		this.mFascicolo = new FascicoloSiusModel(aModel.mFascicolo);
		this.mDescrTipoUfficio = aModel.mDescrTipoUfficio;
		this.mDescrComuneUfficio = aModel.mDescrComuneUfficio;
		this.mEvento = new EventoModel(aModel.mEvento);
		this.mDecreto = new DepositoDecretoModel(aModel.mDecreto);
		this.mDescrProvvedimento = aModel.mDescrProvvedimento;
		this.mDescrOggetto = aModel.mDescrOggetto;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mCodEsitoAlt3 = aModel.mCodEsitoAlt3;
		this.mTenore = new TenoreModel(aModel.mTenore);
		this.mSoggetto = new SoggettoModel(aModel.mSoggetto);
		// MEV_39: aggiunta valorizzazione di variabile
		this.mMisuraAlternativa = new MisuraAlternativaModel(aModel.mMisuraAlternativa);	
		this.mAnnoProtocollo = aModel.mAnnoProtocollo;
		this.mNumeroProtocollo = aModel.mNumeroProtocollo;
	}

	// COSTRUTTORE MODEL
	public DecretoEventoTenoriFascicoloSiusModel(FascicoloSiusModel aFascicoloSiusModel,
			String aDescrTipoUfficio, String aDescrComuneUfficio, EventoModel aEventoModel,
			DepositoDecretoModel aDepositoDecretoModel, String aDescrProvvedimento,
			String aDescrOggetto, String aDescrEsito, String aCodEsitoAlt3, TenoreModel aTenoreModel,
			SoggettoModel aSoggetto, MisuraAlternativaModel aMisuraAlternativa, String annoP, String numeroP) {

		this.mFascicolo = aFascicoloSiusModel;
		this.mDescrTipoUfficio = aDescrTipoUfficio;
		this.mDescrComuneUfficio = aDescrComuneUfficio;
		this.mEvento = aEventoModel;
		this.mDecreto = aDepositoDecretoModel;
		this.mDescrProvvedimento = aDescrProvvedimento;
		this.mDescrOggetto = aDescrOggetto;
		this.mDescrEsito = aDescrEsito;
		this.mCodEsitoAlt3 = aCodEsitoAlt3;
		this.mTenore = aTenoreModel;
		this.mSoggetto = aSoggetto;
		this.mMisuraAlternativa = aMisuraAlternativa;
		this.mAnnoProtocollo = annoP;
		this.mNumeroProtocollo = numeroP;
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

	public DepositoDecretoModel getDecreto() {
		return mDecreto;
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

	public String getCodEsitoAlt3() {
		return mCodEsitoAlt3;
	}

	public TenoreModel getTenore() {
		return mTenore;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	// MEV_39: aggiunto metodo get
	public MisuraAlternativaModel getMisuraAlternativa() {
		return mMisuraAlternativa;
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

	public void setDeceto(DepositoDecretoModel aValore) {
		mDecreto = aValore;
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

	public void setCodEsitoAlt3(String aValore) {
		mCodEsitoAlt3 = aValore;
	}

	public void setTenore(TenoreModel aValore) {
		mTenore = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	// MEV_39: aggiunto metodo set
	public void setMisuraAlternativa(MisuraAlternativaModel aValore) {
		mMisuraAlternativa = aValore;
	}
    
	
	public void setAnnoProtocollo(String aValore) {
		mAnnoProtocollo = aValore;
	}
	
	public String getAnnoProtocollo() {
		return mAnnoProtocollo;
	}
	
	public void setNumeroProtocollo(String aValore) {
		mNumeroProtocollo = aValore;
	}
	
	public String getNumeroProtocollo() {
		return mNumeroProtocollo;
	}
}