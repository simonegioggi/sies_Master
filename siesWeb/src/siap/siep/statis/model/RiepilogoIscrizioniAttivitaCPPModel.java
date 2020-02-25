package siap.siep.statis.model;

/**
* <p>Title: RiepilogoIscrizioniModel - Settembre 2015</p>
* <p>Description: Classe Model che rappresenta il Isp_TempiIscrizione_CPP</p>
*/

import f3b.model.GenericModel;

public class RiepilogoIscrizioniAttivitaCPPModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 8087183604194358980L;
	private Integer mIscritti;
	private Integer mIscrittiErrore;
	private Integer mAttesaInoltroUDS;
	private Integer mInoltroUDSinAttesaRisposta;
	private Integer mInAttesaEsecuzione;
	private Integer mSenzaClasseI;
	private Integer mAnno;
	private String mPeriodo;

	// COSTRUTTORE DI DEFAULT
	public RiepilogoIscrizioniAttivitaCPPModel() {
		this.mIscritti = null;
		this.mIscrittiErrore = null;
		this.mAttesaInoltroUDS = null;
		this.mInoltroUDSinAttesaRisposta = null;
		this.mInAttesaEsecuzione = null;
		this.mSenzaClasseI = null;
		this.mAnno = null;
		this.mPeriodo = null;
	}

	// COSTRUTTORE MODEL per SqlDAO
	public RiepilogoIscrizioniAttivitaCPPModel(Integer aIscritti, Integer aIscrittiErrore,
			Integer aAttesaInoltroUDS, Integer aInoltroUDSinAttesaRisposta, Integer aInAttesaEsecuzione,
			Integer asenzaClasseI, Integer aAnno, String aPeriodo) {
		this.mIscritti = aIscritti;
		this.mIscrittiErrore = aIscrittiErrore;
		this.mAttesaInoltroUDS = aAttesaInoltroUDS;
		this.mInoltroUDSinAttesaRisposta = aInoltroUDSinAttesaRisposta;
		this.mInAttesaEsecuzione = aInAttesaEsecuzione;
		this.mSenzaClasseI = asenzaClasseI;
		this.mAnno = aAnno;
		this.mPeriodo = aPeriodo;
	}

	// Anno
	public Integer getAnno() {
		return mAnno;
	}

	public void setAnno(Integer anno) {
		mAnno = anno;
	}

	// Periodo (Per Statistiche Semestrali o Trimestrali)
	public String getPeriodo() {
		return mPeriodo;
	}

	public void setPeriodo(String aValore) {
		mPeriodo = aValore;
	}

	// Iscritti
	public Integer getIscritti() {
		return mIscritti;
	}

	public void setIscritti(Integer Iscritti) {
		mIscritti = Iscritti;
	}

	// Iscritti Errore
	public Integer getIscrittiErrore() {
		return mIscrittiErrore;
	}

	public void setIscrittiErrore(Integer IscrittiErrore) {
		mIscrittiErrore = IscrittiErrore;
	}

	// Attesa Inoltro UDS
	public Integer getAttesaInoltroUDS() {
		return mAttesaInoltroUDS;
	}

	public void setAttesaInoltroUDS(Integer ValoreAttesa) {
		mAttesaInoltroUDS = ValoreAttesa;
	}

	// Inoltrati a UDS in attesa di Risposta
	public Integer getInoltroUDSinAttesadiRisposta() {
		return mInoltroUDSinAttesaRisposta;
	}

	public void setInoltroUDSinAttesadiRisposta(Integer ValoreAttesaRisposta) {
		mInoltroUDSinAttesaRisposta = ValoreAttesaRisposta;
	}

	// emessa ordinanza, in Attesa Esecuzione
	public Integer getInAttesaEsecuzione() {
		return mInAttesaEsecuzione;
	}

	public void setInAttesaEsecuzione(Integer ValoreAttesaEsecuzione) {
		mInAttesaEsecuzione = ValoreAttesaEsecuzione;
	}

	// Iscritti direttamente in classe VII, Senza Classe I
	public Integer getSenzaClasseI() {
		return mSenzaClasseI;
	}

	public void setSenzaClasseI(Integer ValoresenzaClasseI) {
		mSenzaClasseI = ValoresenzaClasseI;
	}

}
