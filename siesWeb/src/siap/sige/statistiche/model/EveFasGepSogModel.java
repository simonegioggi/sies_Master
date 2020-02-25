package siap.sige.statistiche.model;

import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import f3b.model.GenericModel;

/**
 * Model rappresentativo dell'aggregato di vari model: Evento, Fascicolo SIGE, Provvedimento_SIGE, Soggetto,
 * Documento_Allegato. Utilizzato per contenere i risultati di ricerche su dati strutturati SIGE.
 */
public class EveFasGepSogModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4682161526744841920L;

	private EventoModel mEvento = null;
	private FascicoloSigeModel mFascicoloSige = null;
	private ProvvedimentoSigeModel mProvvedimento = null;
	private ProvvedimentoSigeEventoModel mProvvSigeEvento = null;
	private SoggettoModel mSoggetto = null;
	private DocumentoAllegatoModel mDocumentoAllegato = null;

	private Integer mTotale = null;

	// COSTRUTTORE DI DEFAULT
	public EveFasGepSogModel() {
		mSoggetto = null;
		mEvento = null;
		mFascicoloSige = null;
		mProvvedimento = null;
		mProvvSigeEvento = null;
		mDocumentoAllegato = null;
	}

	public EveFasGepSogModel(EventoModel aEvento) {
		this();
		if (aEvento != null)
			mEvento = new EventoModel(aEvento);

	}

	public EveFasGepSogModel(EventoModel aEvento, FascicoloSigeModel aFascicoloSige) {
		this(aEvento);
		if (aFascicoloSige != null)
			mFascicoloSige = new FascicoloSigeModel(aFascicoloSige);

	}

	public EveFasGepSogModel(EventoModel aEvento, FascicoloSigeModel aFascicoloSige,
			ProvvedimentoSigeEventoModel aProvvSigeEvento) {
		this(aEvento, aFascicoloSige);
		if (aProvvSigeEvento != null)
			mProvvSigeEvento = new ProvvedimentoSigeEventoModel(aProvvSigeEvento);
	}

	public EveFasGepSogModel(EventoModel aEvento, FascicoloSigeModel aFascicoloSige,
			ProvvedimentoSigeEventoModel aProvvSigeEvento, SoggettoModel aSoggetto) {
		this(aEvento, aFascicoloSige, aProvvSigeEvento);
		if (aSoggetto != null)
			mSoggetto = new SoggettoModel(aSoggetto);
	}

	public EveFasGepSogModel(EventoModel aEvento, FascicoloSigeModel aFascicoloSige,
			ProvvedimentoSigeEventoModel aProvvSigeEvento, SoggettoModel aSoggetto,
			DocumentoAllegatoModel aDocumentoAllegato) {
		this(aEvento, aFascicoloSige, aProvvSigeEvento, aSoggetto);
		if (aDocumentoAllegato != null)
			mDocumentoAllegato = new DocumentoAllegatoModel(aDocumentoAllegato);
	}

	// COSTRUTTORE DI COPIA
	public EveFasGepSogModel(EveFasGepSogModel aModel) {
		this(aModel.getEvento(), aModel.getFascicoloSige(), aModel.getProvvedimentoSigeEvento(),
				aModel.getSoggetto(), aModel.getDocumentoAllegato());
	}

	// METODI GET()
	public EventoModel getEvento() {
		return mEvento;
	}

	public FascicoloSigeModel getFascicoloSige() {
		return mFascicoloSige;
	}

	public ProvvedimentoSigeModel getProvvedimentoSige() {
		return mProvvedimento;
	}

	public ProvvedimentoSigeEventoModel getProvvedimentoSigeEvento() {
		return mProvvSigeEvento;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public DocumentoAllegatoModel getDocumentoAllegato() {
		return mDocumentoAllegato;
	}

	public Integer getTotale() {
		return mTotale;
	}

	public boolean isDepositoValidato() {
		if (mDocumentoAllegato != null && mDocumentoAllegato.getFlagDocumentoRegistrato() != null
				&& mDocumentoAllegato.getFlagDocumentoRegistrato().equalsIgnoreCase("S"))
			return true;
		else
			return false;
	}

	// METODI SET()
	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public void setFascicoloSige(FascicoloSigeModel aValore) {
		mFascicoloSige = aValore;
	}

	public void setProvvedimentoSige(ProvvedimentoSigeModel aValore) {
		mProvvedimento = aValore;
	}

	public void setProvvedimentoSigeEvento(ProvvedimentoSigeEventoModel aValore) {
		mProvvSigeEvento = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setDocumentoAllegato(DocumentoAllegatoModel aValore) {
		mDocumentoAllegato = aValore;
	}

	public void setTotale(Integer aValore) {
		mTotale = aValore;
	}

}