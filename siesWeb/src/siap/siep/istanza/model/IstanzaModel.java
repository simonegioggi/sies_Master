package siap.siep.istanza.model;

/**
* <p>Title: IstanzaModel</p>
* <p>Description: Classe Model che rappresenta il Istanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class IstanzaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 2123529527040997993L;
	private BigDecimal mIdIstanza;
	private String mCodMotivo;
	private String mDescrMotivo;
	private String mNote;
	private String mCognomeSoggettoPresentante;
	private String mNomeSoggettoPresentante;
	private Date mDataPresentazione;
	private String mCodEsito;
	private String mDescrEsito;
	private BigDecimal mAnnoRegistro;
	private BigDecimal mProgrRegistro;
	private String mCodTipoUfficioDestinatario;
	private String mDescrTipoUfficioDestinatario;
	private String mCodLuogoDestinatario;
	private String mDescrLuogoDestinatario;
	private String mCodUfficioDestinatario;
	private String mDescrUfficioDestinatario;
	private String mCognomeAvvocato;
	private String mNomeAvvocato;
	private String mForoCompetenza;
	private BigDecimal mAnnoSentenza;
	private String mNumeroSentenza;
	private Date mDataSentenza;
	private Date mDataIrrevocabilita;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private String mCodStatoIstanza;
	private String mDescrStatoIstanza;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mSogIdSoggetto;
	private BigDecimal mEveIdEvento;
	private BigDecimal mCamIdCampoNote;

	// COSTRUTTORE DI DEFAULT
	public IstanzaModel() {
		this.mIdIstanza = null;
		this.mCodMotivo = "";
		this.mDescrMotivo = "";
		this.mNote = "";
		this.mCognomeSoggettoPresentante = "";
		this.mNomeSoggettoPresentante = "";
		this.mDataPresentazione = null;
		this.mCodEsito = "";
		this.mDescrEsito = "";
		this.mAnnoRegistro = null;
		this.mProgrRegistro = null;
		this.mCodTipoUfficioDestinatario = "";
		this.mDescrTipoUfficioDestinatario = "";
		this.mCodLuogoDestinatario = "";
		this.mDescrLuogoDestinatario = "";
		this.mCodUfficioDestinatario = "";
		this.mDescrUfficioDestinatario = "";
		this.mCognomeAvvocato = "";
		this.mNomeAvvocato = "";
		this.mForoCompetenza = "";
		this.mAnnoSentenza = null;
		this.mNumeroSentenza = "";
		this.mDataSentenza = null;
		this.mDataIrrevocabilita = null;
		this.mCodTipoAutoritaEmittente = "";
		this.mDescrTipoAutoritaEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";
		this.mCodStatoIstanza = "";
		this.mDescrStatoIstanza = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mSogIdSoggetto = null;
		this.mEveIdEvento = null;
		this.mCamIdCampoNote = null;
	}

	// COSTRUTTORE DI COPIA
	public IstanzaModel(IstanzaModel aModel) {
		this.mIdIstanza = aModel.mIdIstanza;
		this.mCodMotivo = aModel.mCodMotivo;
		this.mDescrMotivo = aModel.mDescrMotivo;
		this.mNote = aModel.mNote;
		this.mCognomeSoggettoPresentante = aModel.mCognomeSoggettoPresentante;
		this.mNomeSoggettoPresentante = aModel.mNomeSoggettoPresentante;
		this.mDataPresentazione = aModel.mDataPresentazione;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mAnnoRegistro = aModel.mAnnoRegistro;
		this.mProgrRegistro = aModel.mProgrRegistro;
		this.mCodTipoUfficioDestinatario = aModel.mCodTipoUfficioDestinatario;
		this.mDescrTipoUfficioDestinatario = aModel.mDescrTipoUfficioDestinatario;
		this.mCodLuogoDestinatario = aModel.mCodLuogoDestinatario;
		this.mDescrLuogoDestinatario = aModel.mDescrLuogoDestinatario;
		this.mCodUfficioDestinatario = aModel.mCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aModel.mDescrUfficioDestinatario;
		this.mCognomeAvvocato = aModel.mCognomeAvvocato;
		this.mNomeAvvocato = aModel.mNomeAvvocato;
		this.mForoCompetenza = aModel.mForoCompetenza;
		this.mAnnoSentenza = aModel.mAnnoSentenza;
		this.mNumeroSentenza = aModel.mNumeroSentenza;
		this.mDataSentenza = aModel.mDataSentenza;
		this.mDataIrrevocabilita = aModel.mDataIrrevocabilita;
		this.mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mCodStatoIstanza = aModel.mCodStatoIstanza;
		this.mDescrStatoIstanza = aModel.mDescrStatoIstanza;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mCamIdCampoNote = aModel.mCamIdCampoNote;
	}

	// COSTRUTTORE MODEL
	public IstanzaModel(BigDecimal aIdIstanza, String aCodMotivo, String aDescrMotivo, String aNote,
			String aCognomeSoggettoPresentante, String aNomeSoggettoPresentante, Date aDataPresentazione,
			String aCodEsito, String aDescrEsito, BigDecimal aAnnoRegistro, BigDecimal aProgrRegistro,
			String aCodTipoUfficioDestinatario, String aDescrTipoUfficioDestinatario,
			String aCodLuogoDestinatario, String aDescrLuogoDestinatario, String aCodUfficioDestinatario,
			String aDescrUfficioDestinatario, String aCognomeAvvocato, String aNomeAvvocato,
			String aForoCompetenza, BigDecimal aAnnoSentenza, String aNumeroSentenza, Date aDataSentenza,
			Date aDataIrrevocabilita, String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente,
			String aCodLuogoEmittente, String aDescrLuogoEmittente, String aCodStatoIstanza,
			String aDescrStatoIstanza, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aSogIdSoggetto, BigDecimal aEveIdEvento, BigDecimal aCamIdCampoNote) {
		this.mIdIstanza = aIdIstanza;
		this.mCodMotivo = aCodMotivo;
		this.mDescrMotivo = aDescrMotivo;
		this.mNote = aNote;
		this.mCognomeSoggettoPresentante = aCognomeSoggettoPresentante;
		this.mNomeSoggettoPresentante = aNomeSoggettoPresentante;
		this.mDataPresentazione = aDataPresentazione;
		this.mCodEsito = aCodEsito;
		this.mDescrEsito = aDescrEsito;
		this.mAnnoRegistro = aAnnoRegistro;
		this.mProgrRegistro = aProgrRegistro;
		this.mCodTipoUfficioDestinatario = aCodTipoUfficioDestinatario;
		this.mDescrTipoUfficioDestinatario = aDescrTipoUfficioDestinatario;
		this.mCodLuogoDestinatario = aCodLuogoDestinatario;
		this.mDescrLuogoDestinatario = aDescrLuogoDestinatario;
		this.mCodUfficioDestinatario = aCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aDescrUfficioDestinatario;
		this.mCognomeAvvocato = aCognomeAvvocato;
		this.mNomeAvvocato = aNomeAvvocato;
		this.mForoCompetenza = aForoCompetenza;
		this.mAnnoSentenza = aAnnoSentenza;
		this.mNumeroSentenza = aNumeroSentenza;
		this.mDataSentenza = aDataSentenza;
		this.mDataIrrevocabilita = aDataIrrevocabilita;
		this.mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mCodStatoIstanza = aCodStatoIstanza;
		this.mDescrStatoIstanza = aDescrStatoIstanza;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mSogIdSoggetto = aSogIdSoggetto;
		this.mEveIdEvento = aEveIdEvento;
		this.mCamIdCampoNote = aCamIdCampoNote;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdIstanza() {
		return mIdIstanza;
	}

	public String getCodMotivo() {
		return mCodMotivo;
	}

	public String getDescrMotivo() {
		return mDescrMotivo;
	}

	public String getNote() {
		return mNote;
	}

	public String getCognomeSoggettoPresentante() {
		return mCognomeSoggettoPresentante;
	}

	public String getNomeSoggettoPresentante() {
		return mNomeSoggettoPresentante;
	}

	public Date getDataPresentazione() {
		return mDataPresentazione;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public BigDecimal getAnnoRegistro() {
		return mAnnoRegistro;
	}

	public BigDecimal getProgrRegistro() {
		return mProgrRegistro;
	}

	public String getCodTipoUfficioDestinatario() {
		return mCodTipoUfficioDestinatario;
	}

	public String getDescrTipoUfficioDestinatario() {
		return mDescrTipoUfficioDestinatario;
	}

	public String getCodLuogoDestinatario() {
		return mCodLuogoDestinatario;
	}

	public String getDescrLuogoDestinatario() {
		return mDescrLuogoDestinatario;
	}

	public String getCodUfficioDestinatario() {
		return mCodUfficioDestinatario;
	}

	public String getDescrUfficioDestinatario() {
		return mDescrUfficioDestinatario;
	}

	public String getCognomeAvvocato() {
		return mCognomeAvvocato;
	}

	public String getNomeAvvocato() {
		return mNomeAvvocato;
	}

	public String getForoCompetenza() {
		return mForoCompetenza;
	}

	public BigDecimal getAnnoSentenza() {
		return mAnnoSentenza;
	}

	public String getNumeroSentenza() {
		return mNumeroSentenza;
	}

	public Date getDataSentenza() {
		return mDataSentenza;
	}

	public Date getDataIrrevocabilita() {
		return mDataIrrevocabilita;
	}

	public String getCodTipoAutoritaEmittente() {
		return mCodTipoAutoritaEmittente;
	}

	public String getDescrTipoAutoritaEmittente() {
		return mDescrTipoAutoritaEmittente;
	}

	public String getCodLuogoEmittente() {
		return mCodLuogoEmittente;
	}

	public String getDescrLuogoEmittente() {
		return mDescrLuogoEmittente;
	}

	public String getCodStatoIstanza() {
		return mCodStatoIstanza;
	}

	public String getDescrStatoIstanza() {
		return mDescrStatoIstanza;
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

	public BigDecimal getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getCamIdCampoNote() {
		return mCamIdCampoNote;
	}

	//
	// METODI SET()
	//
	public void setIdIstanza(BigDecimal aValore) {
		mIdIstanza = aValore;
	}

	public void setCodMotivo(String aValore) {
		mCodMotivo = aValore;
	}

	public void setDescrMotivo(String aValore) {
		mDescrMotivo = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCognomeSoggettoPresentante(String aValore) {
		mCognomeSoggettoPresentante = aValore;
	}

	public void setNomeSoggettoPresentante(String aValore) {
		mNomeSoggettoPresentante = aValore;
	}

	public void setDataPresentazione(Date aValore) {
		mDataPresentazione = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setAnnoRegistro(BigDecimal aValore) {
		mAnnoRegistro = aValore;
	}

	public void setProgrRegistro(BigDecimal aValore) {
		mProgrRegistro = aValore;
	}

	public void setCodTipoUfficioDestinatario(String aValore) {
		mCodTipoUfficioDestinatario = aValore;
	}

	public void setDescrTipoUfficioDestinatario(String aValore) {
		mDescrTipoUfficioDestinatario = aValore;
	}

	public void setCodLuogoDestinatario(String aValore) {
		mCodLuogoDestinatario = aValore;
	}

	public void setDescrLuogoDestinatario(String aValore) {
		mDescrLuogoDestinatario = aValore;
	}

	public void setCodUfficioDestinatario(String aValore) {
		mCodUfficioDestinatario = aValore;
	}

	public void setDescrUfficioDestinatario(String aValore) {
		mDescrUfficioDestinatario = aValore;
	}

	public void setCognomeAvvocato(String aValore) {
		mCognomeAvvocato = aValore;
	}

	public void setNomeAvvocato(String aValore) {
		mNomeAvvocato = aValore;
	}

	public void setForoCompetenza(String aValore) {
		mForoCompetenza = aValore;
	}

	public void setAnnoSentenza(BigDecimal aValore) {
		mAnnoSentenza = aValore;
	}

	public void setNumeroSentenza(String aValore) {
		mNumeroSentenza = aValore;
	}

	public void setDataSentenza(Date aValore) {
		mDataSentenza = aValore;
	}

	public void setDataIrrevocabilita(Date aValore) {
		mDataIrrevocabilita = aValore;
	}

	public void setCodTipoAutoritaEmittente(String aValore) {
		mCodTipoAutoritaEmittente = aValore;
	}

	public void setDescrTipoAutoritaEmittente(String aValore) {
		mDescrTipoAutoritaEmittente = aValore;
	}

	public void setCodLuogoEmittente(String aValore) {
		mCodLuogoEmittente = aValore;
	}

	public void setDescrLuogoEmittente(String aValore) {
		mDescrLuogoEmittente = aValore;
	}

	public void setCodStatoIstanza(String aValore) {
		mCodStatoIstanza = aValore;
	}

	public void setDescrStatoIstanza(String aValore) {
		mDescrStatoIstanza = aValore;
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

	public void setSogIdSoggetto(BigDecimal aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setCamIdCampoNote(BigDecimal aValore) {
		mCamIdCampoNote = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdIstanza + " - " + mCodMotivo + " - " + mDescrMotivo + " - " + mNote + " - "
				+ mCognomeSoggettoPresentante + " - " + mNomeSoggettoPresentante + " - " + mDataPresentazione
				+ " - " + mCodEsito + " - " + mDescrEsito + " - " + mAnnoRegistro + " - " + mProgrRegistro
				+ " - " + mCodTipoUfficioDestinatario + " - " + mDescrTipoUfficioDestinatario + " - "
				+ mCodLuogoDestinatario + " - " + mDescrLuogoDestinatario + " - " + mCodUfficioDestinatario
				+ " - " + mDescrUfficioDestinatario + " - " + mCognomeAvvocato + " - " + mNomeAvvocato + " - "
				+ mForoCompetenza + " - " + mAnnoSentenza + " - " + mNumeroSentenza + " - " + mDataSentenza
				+ " - " + mDataIrrevocabilita + " - " + mCodTipoAutoritaEmittente + " - "
				+ mDescrTipoAutoritaEmittente + " - " + mCodLuogoEmittente + " - " + mDescrLuogoEmittente
				+ " - " + mCodStatoIstanza + " - " + mDescrStatoIstanza + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - " + mSogIdSoggetto
				+ " - " + mEveIdEvento + " - " + mCamIdCampoNote;

		return lStr;
	}
}
