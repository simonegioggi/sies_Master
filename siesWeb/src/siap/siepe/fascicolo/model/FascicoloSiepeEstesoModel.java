package siap.siepe.fascicolo.model;

/**
* <p>Title: FascicoloSoggAttModel</p>
* <p>Description: Questo Classe Model raggruppa in un unico aggregato tutti i dati collegati ad un Fascicolo SIEPE, cioè:</p>
 * Fascicolo Siepe, Soggetto, Elenco Attività, Fascicolo SIUS, Fascicolo SIEP, Evento.
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.model.GenericModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FascicoloSiepeEstesoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7744431059353270036L;

	private FascicoloSiepeModel mFascicoloSiepe;
	private SoggettoModel mSoggetto;
	private Vector mElencoAttivita;
	private FascicoloGPModel mFasGPSius = null;
	private FascicoloSiepModel mFascicoloSiep = null;
	private EventoModel mEvento = null;

	// COSTRUTTORE DI DEFAULT
	public FascicoloSiepeEstesoModel() {
		mFascicoloSiepe = null;
		mSoggetto = null;
		mElencoAttivita = null;
		mFasGPSius = null;
		mFascicoloSiep = null;
		mEvento = null;
	}

	public FascicoloSiepeEstesoModel(FascicoloSiepeModel aFascicoloSiepe) {
		this();
		if (aFascicoloSiepe != null)
			mFascicoloSiepe = new FascicoloSiepeModel(aFascicoloSiepe);

	}

	public FascicoloSiepeEstesoModel(FascicoloSiepeModel aFascicoloSiepe, SoggettoModel aSoggetto) {
		this(aFascicoloSiepe);
		if (aSoggetto != null)
			mSoggetto = new SoggettoModel(aSoggetto);
	}

	public FascicoloSiepeEstesoModel(FascicoloSiepeModel aFascicoloSiepe, FascicoloGPModel aFascicoloSius) {
		this(aFascicoloSiepe);
		if (aFascicoloSius != null)
			mFasGPSius = new FascicoloGPModel(aFascicoloSius);
	}

	public FascicoloSiepeEstesoModel(FascicoloSiepeModel aFascicoloSiepe, SoggettoModel aSoggetto,
			Vector aElencoAttivita) {
		this(aFascicoloSiepe, aSoggetto);
		if (aElencoAttivita != null)
			mElencoAttivita = new Vector(aElencoAttivita);
	}

	public FascicoloSiepeEstesoModel(FascicoloSiepeModel aFascicoloSiepe, SoggettoModel aSoggetto,
			Vector aElencoAttivita, FascicoloGPModel aFascicoloSius) {
		this(aFascicoloSiepe, aSoggetto, aElencoAttivita);
		if (aFascicoloSius != null)
			mFasGPSius = new FascicoloGPModel(aFascicoloSius);
	}

	public FascicoloSiepeEstesoModel(FascicoloSiepeModel aFascicoloSiepe, SoggettoModel aSoggetto,
			Vector aElencoAttivita, FascicoloGPModel aFascicoloSius, FascicoloSiepModel aFascicoloSiep) {
		this(aFascicoloSiepe, aSoggetto, aElencoAttivita, aFascicoloSius);
		if (aFascicoloSiep != null)
			mFascicoloSiep = new FascicoloSiepModel(aFascicoloSiep);
	}

	public FascicoloSiepeEstesoModel(FascicoloSiepeModel aFascicoloSiepe, SoggettoModel aSoggetto,
			Vector aElencoAttivita, FascicoloGPModel aFascicoloSius, FascicoloSiepModel aFascicoloSiep,
			EventoModel aEvento) {
		this(aFascicoloSiepe, aSoggetto, aElencoAttivita, aFascicoloSius, aFascicoloSiep);
		if (aEvento != null)
			mEvento = new EventoModel(aEvento);
	}

	// COSTRUTTORE DI COPIA
	public FascicoloSiepeEstesoModel(FascicoloSiepeEstesoModel aModel) {
		this(aModel.getFascicoloSiepe(), aModel.getSoggetto(), aModel.getElencoAttivita(),
				aModel.getFascicoloSius(), aModel.getFascicoloSiep());
	}

	// METODI GET()
	//
	public FascicoloSiepeModel getFascicoloSiepe() {
		return mFascicoloSiepe;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public Vector getElencoAttivita() {
		return mElencoAttivita;
	}

	public FascicoloGPModel getFascicoloSius() {
		return mFasGPSius;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

	// METODI SET()
	//
	public void setFascicoloSiepe(FascicoloSiepeModel aValore) {
		mFascicoloSiepe = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setElencoAttivita(Vector aValore) {
		mElencoAttivita = aValore;
	}

	public void setFascicoloSiusGP(FascicoloGPModel aValore) {
		mFasGPSius = aValore;
	}

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

}