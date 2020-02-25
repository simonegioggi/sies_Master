package siap.sius.depositosentenza.model;

import siap.sico.evento.model.EventoModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.model.GenericModel;

public class SentenzaEventoTenoriModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3869835846685986658L;

	private EventoModel mEvento;
	private DepositoSentenzaModel mSentenza;
	private TenoreModel[] mTenori;

	public EventoModel getEvento() {
		return mEvento;
	}

	public DepositoSentenzaModel getSentenza() {
		return mSentenza;
	}

	public TenoreModel[] getTenori() {
		return mTenori;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public void setSentenza(DepositoSentenzaModel aValore) {
		mSentenza = aValore;
	}

	public void setTenori(TenoreModel[] aValore) {
		mTenori = aValore;
	}

}