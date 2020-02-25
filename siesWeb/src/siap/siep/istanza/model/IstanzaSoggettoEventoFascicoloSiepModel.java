package siap.siep.istanza.model;

import f3b.model.GenericModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * <p>
 * Title: IstanzaSoggettoModel
 * </p>
 * <p>
 * Description: Classe model che rappresenta l'istanza e il soggetto associato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */

public class IstanzaSoggettoEventoFascicoloSiepModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 7368834126245056462L;
	private IstanzaModel mIstanza;
	private SoggettoModel mSoggetto;
	private EventoModel mEvento;
	private FascicoloSiepModel mFascicoloSiep;

	public IstanzaSoggettoEventoFascicoloSiepModel() {
		mIstanza = null;
		mSoggetto = null;
		mEvento = null;
		mFascicoloSiep = null;
	}

	public IstanzaSoggettoEventoFascicoloSiepModel(IstanzaModel aIstanza, SoggettoModel aSoggetto,
			EventoModel aEvento, FascicoloSiepModel aFascicoloSiep) {
		mIstanza = aIstanza;
		mSoggetto = aSoggetto;
		mEvento = aEvento;
		mFascicoloSiep = aFascicoloSiep;
	}

	//
	// METODI GET()
	//
	public IstanzaModel getIstanza() {
		return mIstanza;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	//
	// METODI SET()
	//
	public void setIstanza(IstanzaModel aValore) {
		mIstanza = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}
}