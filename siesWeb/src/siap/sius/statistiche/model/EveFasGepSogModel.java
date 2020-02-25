package siap.sius.statistiche.model;

/**
* <p>Title: FascicoloSoggAttModel</p>
* <p>Description: Questo Classe Model raggruppa in un unico aggregato tutti i dati collegati ad un Fascicolo SIEPE, cioè:</p>
 * Fascicolo Siepe, Soggetto, Elenco Attività, Fascicolo SIUS, Fascicolo SIEP, Evento.
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import f3b.model.GenericModel;

/**
 * Model rappresentativo dell'aggregato di vari model: Evento, Fascicolo SIUS, Generale Procedimento,
 * Soggetto, Documento_Allegato. Utilizzato per contenere i risultati di ricerche su dati strutturati SIUS.
 * 
 * @author Lesposito
 *
 */
public class EveFasGepSogModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1086173954050936991L;

	private EventoModel mEvento = null;
	private FascicoloSiusModel mFascicoloSius = null;
	private GeneraleProcedimentoModel mGenProc = null;
	private SoggettoModel mSoggetto = null;
	private DocumentoAllegatoModel mDocumentoAllegato = null;

	private Integer mTotale = null;

	// COSTRUTTORE DI DEFAULT
	public EveFasGepSogModel() {
		mSoggetto = null;
		mEvento = null;
		mFascicoloSius = null;
		mGenProc = null;
		mDocumentoAllegato = null;
	}

	public EveFasGepSogModel(EventoModel aEvento) {
		this();
		if (aEvento != null)
			mEvento = new EventoModel(aEvento);

	}

	public EveFasGepSogModel(EventoModel aEvento, FascicoloSiusModel aFascicoloSius) {
		this(aEvento);
		if (aFascicoloSius != null)
			mFascicoloSius = new FascicoloSiusModel(aFascicoloSius);

	}

	public EveFasGepSogModel(EventoModel aEvento, FascicoloSiusModel aFascicoloSius,
			GeneraleProcedimentoModel aGenProc) {
		this(aEvento, aFascicoloSius);
		if (aGenProc != null)
			mGenProc = new GeneraleProcedimentoModel(aGenProc);
	}

	public EveFasGepSogModel(EventoModel aEvento, FascicoloSiusModel aFascicoloSius,
			GeneraleProcedimentoModel aGenProc, SoggettoModel aSoggetto) {
		this(aEvento, aFascicoloSius, aGenProc);
		if (aSoggetto != null)
			mSoggetto = new SoggettoModel(aSoggetto);
	}

	public EveFasGepSogModel(EventoModel aEvento, FascicoloSiusModel aFascicoloSius,
			GeneraleProcedimentoModel aGenProc, SoggettoModel aSoggetto,
			DocumentoAllegatoModel aDocumentoAllegato) {
		this(aEvento, aFascicoloSius, aGenProc, aSoggetto);
		if (aDocumentoAllegato != null)
			mDocumentoAllegato = new DocumentoAllegatoModel(aDocumentoAllegato);
	}

	// COSTRUTTORE DI COPIA
	public EveFasGepSogModel(EveFasGepSogModel aModel) {
		this(aModel.getEvento(), aModel.getFascicoloSius(), aModel.getGeneraleProcedimento(),
				aModel.getSoggetto(), aModel.getDocumentoAllegato());
	}

	// METODI GET()
	//
	public EventoModel getEvento() {
		return mEvento;
	}

	public FascicoloSiusModel getFascicoloSius() {
		return mFascicoloSius;
	}

	public GeneraleProcedimentoModel getGeneraleProcedimento() {
		return mGenProc;
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

	public void setFascicoloSius(FascicoloSiusModel aValore) {
		mFascicoloSius = aValore;
	}

	public void setGeneraleProcedimento(GeneraleProcedimentoModel aValore) {
		mGenProc = aValore;
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