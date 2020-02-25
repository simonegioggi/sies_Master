package siap.sius.depositoordinanzapc.model;

import f3b.model.GenericModel;
import siap.sico.evento.model.EventoModel;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.tenore.model.TenoreModel;

public class OrdinanzaEventoTenoriPrescrizioniModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 4009477364737765516L;
	private EventoModel mEvento;
	private DepositoOrdinanzaPcModel mOrdinanza;
	private TenoreModel[] mTenori;
	private PrescrizioneModel[] mPrescrizioni;

	// Costruttore
	public OrdinanzaEventoTenoriPrescrizioniModel() {
		mEvento = null;
		mOrdinanza = null;
		mTenori = null;
		mPrescrizioni = null;
	}

	// Metodi Get
	public EventoModel getEvento() {
		return mEvento;
	}

	public DepositoOrdinanzaPcModel getOrdinanza() {
		return mOrdinanza;
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

	public void setOrdinanza(DepositoOrdinanzaPcModel aValore) {
		mOrdinanza = aValore;
	}

	public void setTenori(TenoreModel[] aValore) {
		mTenori = aValore;
	}

	public void setPrescrizioni(PrescrizioneModel[] aValore) {
		mPrescrizioni = aValore;
	}

}