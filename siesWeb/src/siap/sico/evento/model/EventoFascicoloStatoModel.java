package siap.sico.evento.model;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: EventoFascicoloStatoModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta l'Evento legata al FascicoloSiepModel e Stato Procedimento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class EventoFascicoloStatoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -666627055347089499L;

	private FascicoloSiepModel mFascicoloSiep;
	private EventoModel mEvento;
	private StatoProcedimentoModel mStatoProc;

	// COSTRUTTORE DI DEFAULT
	public EventoFascicoloStatoModel() {
		mFascicoloSiep = null;
		mEvento = null;
		mStatoProc = null;

	}

	public EventoFascicoloStatoModel(FascicoloSiepModel aFascicoloSiep, EventoModel aEvento,
			StatoProcedimentoModel aStatoProc) {
		mFascicoloSiep = aFascicoloSiep;
		mEvento = aEvento;
		mStatoProc = aStatoProc;

	}

	//
	// METODI GET()
	//
	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

	public StatoProcedimentoModel getStatoProcedimento() {
		return mStatoProc;
	}

	//
	// METODI SET()
	//
	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public void setStatoProcedimento(StatoProcedimentoModel aValore) {
		mStatoProc = aValore;
	}

}
