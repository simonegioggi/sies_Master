package siap.sius.permesso.model;

/**
* <p>Title: LicenzaModel</p>
* <p>Description: Classe Model che rappresenta tutti i dati di FascicoloSius,
*                 Generale Procedimento ed Evento (legato alla licenza) </p>
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.sius.depositodecreto.model.DepositoDecretoFascicoloModel;

public class ProvvedimentoPermessoLicenzaModel extends DepositoDecretoFascicoloModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 8043400252010510811L;
	private SoggettoModel mSoggetto = null;
	private LicenzaLibAnticipataModel mLicenza = null;
	private IstitutoDetenzioneModel mIstituto = null;
	private EventoModel mEvento = null;
	private String mEsisteRicorso = new String("N"); // Per default is N ( No ).

	// COSTRUTTORE DI CLASSE.
	public ProvvedimentoPermessoLicenzaModel() {
		super();
	}

	public LicenzaLibAnticipataModel getLicenza() {
		return this.mLicenza;
	}

	public void setLicenza(LicenzaLibAnticipataModel aLicenza) {
		mLicenza = aLicenza;
	}

	public SoggettoModel getSoggetto() {
		return this.mSoggetto;
	}

	public void setSoggetto(SoggettoModel aSoggetto) {
		mSoggetto = aSoggetto;
	}

	public IstitutoDetenzioneModel getIstitutoDetenzione() {
		return this.mIstituto;
	}

	public void setIstitutoDetenzione(IstitutoDetenzioneModel aIstituto) {
		mIstituto = aIstituto;
	}

	public EventoModel getEvento() {
		return this.mEvento;
	}

	public void setEvento(EventoModel aEvento) {
		mEvento = aEvento;
	}

	public String getEsisteRicorso() {
		return mEsisteRicorso;
	}

	public void setEsisteRicorso(String aValue) {
		mEsisteRicorso = aValue;
	}
}