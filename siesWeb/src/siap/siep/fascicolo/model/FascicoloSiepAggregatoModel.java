package siap.siep.fascicolo.model;

import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.verbale.model.VerbaleModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: FascicoloSiepAggregatoModel
 * </p>
 * <p>
 * Description: FascicoloSiep Aggregato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 */
public class FascicoloSiepAggregatoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 210667701622942393L;

	private MisuraAlternativaModel mMisuraAlternativa;
	private EventoModel mEvento;
	private PenaResiduaModel mPenaResidua;
	private SospensioneModel mSospensione;
	private FascicoloSiepModel mFascicoloSiep;
	private SoggettoModel mSoggetto;
	private ScadenzarioModel mScadenzario;
	private VerbaleModel mVerbale;

	public FascicoloSiepAggregatoModel() {
		mMisuraAlternativa = new MisuraAlternativaModel();
		mEvento = new EventoModel();
		mPenaResidua = new PenaResiduaModel();
		mSospensione = new SospensioneModel();
		mFascicoloSiep = new FascicoloSiepModel();
		mSoggetto = new SoggettoModel();
		mScadenzario = new ScadenzarioModel();
		mVerbale = new VerbaleModel();
	}

	// Metodi get
	public MisuraAlternativaModel getMisuraAlternativa() {
		return mMisuraAlternativa;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

	public PenaResiduaModel getPenaResidua() {
		return mPenaResidua;
	}

	public SospensioneModel getSospensione() {
		return mSospensione;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public ScadenzarioModel getScadenzario() {
		return mScadenzario;
	}

	public VerbaleModel getVerbale() {
		return mVerbale;
	}

	// Metodi set
	public void setMisuraAlternativa(MisuraAlternativaModel aValore) {
		mMisuraAlternativa = aValore;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public void setPenaResidua(PenaResiduaModel aValore) {
		mPenaResidua = aValore;
	}

	public void setSospensione(SospensioneModel aValore) {
		mSospensione = aValore;
	}

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setScadenzario(ScadenzarioModel aValore) {
		mScadenzario = aValore;
	}

	public void setVerbale(VerbaleModel aValore) {
		mVerbale = aValore;
	}

}