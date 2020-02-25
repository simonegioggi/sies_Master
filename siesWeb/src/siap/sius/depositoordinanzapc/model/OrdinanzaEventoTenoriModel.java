package siap.sius.depositoordinanzapc.model;

import f3b.model.GenericModel;
import siap.sico.evento.model.EventoModel;
import siap.sius.tenore.model.TenoreModel;

public class OrdinanzaEventoTenoriModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -2187174031038560799L;
	private EventoModel mEvento;
	private DepositoOrdinanzaPcModel mOrdinanza;
	private TenoreModel[] mTenori;

	public EventoModel getEvento() {
		return mEvento;
	}

	public DepositoOrdinanzaPcModel getOrdinanza() {
		return mOrdinanza;
	}

	public TenoreModel[] getTenori() {
		return mTenori;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public void setOrdinanza(DepositoOrdinanzaPcModel aValore) {
		mOrdinanza = aValore;
	}

	public void setTenori(TenoreModel[] aValore) {
		mTenori = aValore;
	}

}