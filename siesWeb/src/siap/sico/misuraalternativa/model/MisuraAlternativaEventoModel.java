package siap.sico.misuraalternativa.model;

import siap.sico.evento.model.EventoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.model.GenericModel;

public class MisuraAlternativaEventoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8943984508644239125L;

	private MisuraAlternativaModel mMisuraAlternativa;
	private EventoModel mEvento;
	private FascicoloSiepModel mFascicoloSiep;

	public MisuraAlternativaEventoModel() {
		mMisuraAlternativa = new MisuraAlternativaModel();
		mEvento = new EventoModel();
		mFascicoloSiep = new FascicoloSiepModel();
	}

	// Metodi get
	public MisuraAlternativaModel getMisuraAlternativa() {
		return mMisuraAlternativa;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	// Metodi set
	public void setMisuraAlternativa(MisuraAlternativaModel aValore) {
		mMisuraAlternativa = aValore;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}

}