package siap.siep.sentenzariunita.model;

/**
* <p>Title: SentenzaRiunitaModel</p>
* <p>Description: Classe Model che rappresenta il SentenzaRiunita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class SentenzaRiunitaModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 9113098954720158159L;

	private BigDecimal mIdSentenzaRiunita;
	private Date mDataSentenza;
	private BigDecimal mAnnoSentenza;
	private String mNumeroSentenza;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodAutoritaEmittente;
	private String mDescrAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private String mSezioneAutoritaEmittente;
	private BigDecimal mAnnoRegePm;
	private String mNumeroRegePm;
	private BigDecimal mAnnoRegeGip;
	private String mNumeroRegeGip;
	private BigDecimal mAnnoRegeDib;
	private String mNumeroRegeDib;
	private BigDecimal mAnnoRegeCas;
	private String mNumeroRegeCas;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mSenIdSentenza;

	// NUOVI CAMPI PER REVISIONE SENTENZA
	private String mCodSedeNotiziaReato;
	private String mDescrSedeNotiziaReato;

	// COSTRUTTORE DI DEFAULT
	public SentenzaRiunitaModel() {
		this.mIdSentenzaRiunita = null;
		this.mDataSentenza = null;
		this.mAnnoSentenza = null;
		this.mNumeroSentenza = "";
		this.mCodTipoAutoritaEmittente = "";
		this.mDescrTipoAutoritaEmittente = "";
		this.mCodAutoritaEmittente = "";
		this.mDescrAutoritaEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";
		this.mSezioneAutoritaEmittente = "";
		this.mAnnoRegePm = null;
		this.mNumeroRegePm = "";
		this.mAnnoRegeGip = null;
		this.mNumeroRegeGip = "";
		this.mAnnoRegeDib = null;
		this.mNumeroRegeDib = "";
		this.mAnnoRegeCas = null;
		this.mNumeroRegeCas = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mSenIdSentenza = null;

		this.mCodSedeNotiziaReato = "";
		this.mDescrSedeNotiziaReato = "";
	}

	// COSTRUTTORE DI COPIA
	public SentenzaRiunitaModel(SentenzaRiunitaModel aModel) {
		this.mIdSentenzaRiunita = aModel.mIdSentenzaRiunita;
		this.mDataSentenza = aModel.mDataSentenza;
		this.mAnnoSentenza = aModel.mAnnoSentenza;
		this.mNumeroSentenza = aModel.mNumeroSentenza;
		this.mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mCodAutoritaEmittente = aModel.mCodAutoritaEmittente;
		this.mDescrAutoritaEmittente = aModel.mDescrAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mSezioneAutoritaEmittente = aModel.mSezioneAutoritaEmittente;
		this.mAnnoRegePm = aModel.mAnnoRegePm;
		this.mNumeroRegePm = aModel.mNumeroRegePm;
		this.mAnnoRegeGip = aModel.mAnnoRegeGip;
		this.mNumeroRegeGip = aModel.mNumeroRegeGip;
		this.mAnnoRegeDib = aModel.mAnnoRegeDib;
		this.mNumeroRegeDib = aModel.mNumeroRegeDib;
		this.mAnnoRegeCas = aModel.mAnnoRegeCas;
		this.mNumeroRegeCas = aModel.mNumeroRegeCas;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mSenIdSentenza = aModel.mSenIdSentenza;

		this.mCodSedeNotiziaReato = aModel.getCodSedeNotiziaReato();
		this.mDescrSedeNotiziaReato = aModel.getDescrSedeNotiziaReato();
	}

	// COSTRUTTORE MODEL
	public SentenzaRiunitaModel(BigDecimal aIdSentenzaRiunita, Date aDataSentenza, BigDecimal aAnnoSentenza,
			String aNumeroSentenza, String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente,
			String aCodAutoritaEmittente, String aDescrAutoritaEmittente, String aCodLuogoEmittente,
			String aDescrLuogoEmittente, String aSezioneAutoritaEmittente, BigDecimal aAnnoRegePm,
			String aNumeroRegePm, BigDecimal aAnnoRegeGip, String aNumeroRegeGip, BigDecimal aAnnoRegeDib,
			String aNumeroRegeDib, BigDecimal aAnnoRegeCas, String aNumeroRegeCas,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aSenIdSentenza,
			String aCodSedeNotiziaReato, String aDescrSedeNotiziaReato) {
		this.mIdSentenzaRiunita = aIdSentenzaRiunita;
		this.mDataSentenza = aDataSentenza;
		this.mAnnoSentenza = aAnnoSentenza;
		this.mNumeroSentenza = aNumeroSentenza;
		this.mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mCodAutoritaEmittente = aCodAutoritaEmittente;
		this.mDescrAutoritaEmittente = aDescrAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mSezioneAutoritaEmittente = aSezioneAutoritaEmittente;
		this.mAnnoRegePm = aAnnoRegePm;
		this.mNumeroRegePm = aNumeroRegePm;
		this.mAnnoRegeGip = aAnnoRegeGip;
		this.mNumeroRegeGip = aNumeroRegeGip;
		this.mAnnoRegeDib = aAnnoRegeDib;
		this.mNumeroRegeDib = aNumeroRegeDib;
		this.mAnnoRegeCas = aAnnoRegeCas;
		this.mNumeroRegeCas = aNumeroRegeCas;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mSenIdSentenza = aSenIdSentenza;
		this.mCodSedeNotiziaReato = aCodSedeNotiziaReato;
		this.mDescrSedeNotiziaReato = aDescrSedeNotiziaReato;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdSentenzaRiunita() {
		return mIdSentenzaRiunita;
	}

	public Date getDataSentenza() {
		return mDataSentenza;
	}

	public BigDecimal getAnnoSentenza() {
		return mAnnoSentenza;
	}

	public String getNumeroSentenza() {
		return mNumeroSentenza;
	}

	public String getCodTipoAutoritaEmittente() {
		return mCodTipoAutoritaEmittente;
	}

	public String getDescrTipoAutoritaEmittente() {
		return mDescrTipoAutoritaEmittente;
	}

	public String getCodAutoritaEmittente() {
		return mCodAutoritaEmittente;
	}

	public String getDescrAutoritaEmittente() {
		return mDescrAutoritaEmittente;
	}

	public String getCodLuogoEmittente() {
		return mCodLuogoEmittente;
	}

	public String getDescrLuogoEmittente() {
		return mDescrLuogoEmittente;
	}

	public String getSezioneAutoritaEmittente() {
		return mSezioneAutoritaEmittente;
	}

	public BigDecimal getAnnoRegePm() {
		return mAnnoRegePm;
	}

	public String getNumeroRegePm() {
		return mNumeroRegePm;
	}

	public BigDecimal getAnnoRegeGip() {
		return mAnnoRegeGip;
	}

	public String getNumeroRegeGip() {
		return mNumeroRegeGip;
	}

	public BigDecimal getAnnoRegeDib() {
		return mAnnoRegeDib;
	}

	public String getNumeroRegeDib() {
		return mNumeroRegeDib;
	}

	public BigDecimal getAnnoRegeCas() {
		return mAnnoRegeCas;
	}

	public String getNumeroRegeCas() {
		return mNumeroRegeCas;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public BigDecimal getSenIdSentenza() {
		return mSenIdSentenza;
	}

	public String getCodSedeNotiziaReato() {
		return mCodSedeNotiziaReato;
	}

	public String getDescrSedeNotiziaReato() {
		return mDescrSedeNotiziaReato;
	}

	//
	// METODI SET()
	//

	public void setIdSentenzaRiunita(BigDecimal aValore) {
		mIdSentenzaRiunita = aValore;
	}

	public void setDataSentenza(Date aValore) {
		mDataSentenza = aValore;
	}

	public void setAnnoSentenza(BigDecimal aValore) {
		mAnnoSentenza = aValore;
	}

	public void setNumeroSentenza(String aValore) {
		mNumeroSentenza = aValore;
	}

	public void setCodTipoAutoritaEmittente(String aValore) {
		mCodTipoAutoritaEmittente = aValore;
	}

	public void setDescrTipoAutoritaEmittente(String aValore) {
		mDescrTipoAutoritaEmittente = aValore;
	}

	public void setCodAutoritaEmittente(String aValore) {
		mCodAutoritaEmittente = aValore;
	}

	public void setDescrAutoritaEmittente(String aValore) {
		mDescrAutoritaEmittente = aValore;
	}

	public void setCodLuogoEmittente(String aValore) {
		mCodLuogoEmittente = aValore;
	}

	public void setDescrLuogoEmittente(String aValore) {
		mDescrLuogoEmittente = aValore;
	}

	public void setSezioneAutoritaEmittente(String aValore) {
		mSezioneAutoritaEmittente = aValore;
	}

	public void setAnnoRegePm(BigDecimal aValore) {
		mAnnoRegePm = aValore;
	}

	public void setNumeroRegePm(String aValore) {
		mNumeroRegePm = aValore;
	}

	public void setAnnoRegeGip(BigDecimal aValore) {
		mAnnoRegeGip = aValore;
	}

	public void setNumeroRegeGip(String aValore) {
		mNumeroRegeGip = aValore;
	}

	public void setAnnoRegeDib(BigDecimal aValore) {
		mAnnoRegeDib = aValore;
	}

	public void setNumeroRegeDib(String aValore) {
		mNumeroRegeDib = aValore;
	}

	public void setAnnoRegeCas(BigDecimal aValore) {
		mAnnoRegeCas = aValore;
	}

	public void setNumeroRegeCas(String aValore) {
		mNumeroRegeCas = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setSenIdSentenza(BigDecimal aValore) {
		mSenIdSentenza = aValore;
	}

	public void setCodSedeNotiziaReato(String aValore) {
		mCodSedeNotiziaReato = aValore;
	}

	public void setDescrSedeNotiziaReato(String aValore) {
		mDescrSedeNotiziaReato = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdSentenzaRiunita + " - " + mDataSentenza + " - " + mAnnoSentenza + " - "
				+ mNumeroSentenza + " - " + mCodTipoAutoritaEmittente + " - " + mDescrTipoAutoritaEmittente
				+ " - " + mCodAutoritaEmittente + " - " + mDescrAutoritaEmittente + " - " + mCodLuogoEmittente
				+ " - " + mDescrLuogoEmittente + " - " + mSezioneAutoritaEmittente + " - " + mAnnoRegePm
				+ " - " + mNumeroRegePm + " - " + mAnnoRegeGip + " - " + mNumeroRegeGip + " - " + mAnnoRegeDib
				+ " - " + mNumeroRegeDib + " - " + mAnnoRegeCas + " - " + mNumeroRegeCas + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mSenIdSentenza + " - " +

				mCodSedeNotiziaReato;

		return lStr;
	}

}