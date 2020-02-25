package siap.siep.notifica.model;

/**
* <p>Title: NotificaSoggFasSiusModel</p>
* <p>Description: Classe Model estensione della NotificaModel.</p>
* <p> Estensione realizzata per contenere anche riferimenti al Fascicolo Sius di riferimento, l'Evento ed il Soggetto. </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.evento.model.EventoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;

public class NotificaFasSiusEveModel extends NotificaModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 5217097234295442112L;
	private FascicoloSiusModel mFasSius = null;
	private EventoModel mEvento = null;

	// COSTRUTTORE DI DEFAULT
	public NotificaFasSiusEveModel() {
		super();
	}

	public NotificaFasSiusEveModel(NotificaModel aModel) {
		super(aModel);
	}

	public NotificaFasSiusEveModel(NotificaModel aNotificaModel, FascicoloSiusModel aFasModel) {
		this(aNotificaModel);
		mFasSius = new FascicoloSiusModel(aFasModel);
	}

	public NotificaFasSiusEveModel(NotificaModel aNotificaModel, FascicoloSiusModel aFasModel,
			EventoModel aEvento) {
		this(aNotificaModel, aFasModel);
		mEvento = new EventoModel(aEvento);
	}

	public NotificaFasSiusEveModel(NotificaFasSiusEveModel aModel) {
		super(aModel);
		mFasSius = aModel.mFasSius;
		mEvento = aModel.mEvento;
	}

	//
	// METODI GET()
	//
	public FascicoloSiusModel getFascicoloSius() {
		return mFasSius;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

	//
	// METODI SET()
	//
	public void setFascicoloSius(FascicoloSiusModel aValore) {
		mFasSius = aValore;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

}
