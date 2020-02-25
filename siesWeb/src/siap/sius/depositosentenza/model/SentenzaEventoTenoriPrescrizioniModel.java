package siap.sius.depositosentenza.model;

import siap.sico.evento.model.EventoModel;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.model.GenericModel;

public class SentenzaEventoTenoriPrescrizioniModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3751044096746402252L;

	private EventoModel mEvento;
	private DepositoSentenzaModel mSentenza;
	private TenoreModel[] mTenori;
	private PrescrizioneModel[] mPrescrizioni;

	// Costruttore
	public SentenzaEventoTenoriPrescrizioniModel() {
		mEvento = null;
		mSentenza = null;
		mTenori = null;
		mPrescrizioni = null;
	}

	// Metodi Get
	public EventoModel getEvento() {
		return mEvento;
	}

	public DepositoSentenzaModel getSentenza() {
		return mSentenza;
	}

	public TenoreModel[] getTenori() {
		return mTenori;
	}

	public PrescrizioneModel[] getPrescrizioni() {
		return mPrescrizioni;
	}

	// Metodi Set
	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public void setSentenza(DepositoSentenzaModel aValore) {
		mSentenza = aValore;
	}

	public void setTenori(TenoreModel[] aValore) {
		mTenori = aValore;
	}

	public void setPrescrizioni(PrescrizioneModel[] aValore) {
		mPrescrizioni = aValore;
	}

}