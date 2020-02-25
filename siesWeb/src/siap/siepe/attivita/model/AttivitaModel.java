package siap.siepe.attivita.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.siepe.relazione.model.RelazioneModel;

/**
 * <p>
 * Title: AttivitaModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il Attivita
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class AttivitaModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -7099347536196420323L;
	private BigDecimal mIdAttivita;
	private Date mDataInizio;
	private Date mDataChiusura;
	private String mCodTipoAttivita;
	private String mDescrTipoAttivita;
	private String mNote;
	// private Blob mDocBlob;
	private ByteArrayInputStream mDocBlobIn;
	private ByteArrayOutputStream mDocBlobOut;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFasSiepe;
	private BigDecimal mAssSocIdAssSociale;
	private String mFlagDocumentoRegistrato;
	private String mCodEsitoAttivita;
	private String mDescrEsitoAttivita;
	private byte[] mDocPerTrasferimento = null;
	private String mNotaChiusura;
	private RelazioneModel[] mRelazioni;

	// COSTRUTTORE DI DEFAULT
	public AttivitaModel() {
		mIdAttivita = null;
		mDataInizio = null;
		mDataChiusura = null;
		mCodTipoAttivita = "";
		mDescrTipoAttivita = "";
		mNote = "";
		// mDocBlob = null;
		mDocBlobIn = null;
		mDocBlobOut = null;
		mCodOperatoreInserimento = "";
		mDataInserimento = null;
		mCodUfficioInserimento = "";
		mDescrUfficioInserimento = "";
		mCodOperatoreAggiornamento = "";
		mDataAggiornamento = null;
		mCodUfficioAggiornamento = "";
		mDescrUfficioAggiornamento = "";
		mFasSieIdFasSiepe = null;
		mAssSocIdAssSociale = null;
		mFlagDocumentoRegistrato = null;
		mCodEsitoAttivita = "";
		mDescrEsitoAttivita = "";
		mNotaChiusura = "";
		mRelazioni = null;
	}

	// COSTRUTTORE DI COPIA
	public AttivitaModel(AttivitaModel aModel) {
		mIdAttivita = aModel.mIdAttivita;
		mDataInizio = aModel.mDataInizio;
		mDataChiusura = aModel.mDataChiusura;
		mCodTipoAttivita = aModel.mCodTipoAttivita;
		mDescrTipoAttivita = aModel.mDescrTipoAttivita;
		mNote = aModel.mNote;
		// mDocBlob = aModel.mDocBlob;
		mDocBlobIn = aModel.mDocBlobIn;
		mDocBlobOut = aModel.mDocBlobOut;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		mFasSieIdFasSiepe = aModel.mFasSieIdFasSiepe;
		mAssSocIdAssSociale = aModel.mAssSocIdAssSociale;
		mFlagDocumentoRegistrato = aModel.mFlagDocumentoRegistrato;
		mCodEsitoAttivita = aModel.getCodEsitoAttivita();
		setDescrTipoAttivita(aModel.getDescrTipoAttivita());
		mDocPerTrasferimento = aModel.getDocPerTrasferimento();
		mNotaChiusura = aModel.mNotaChiusura;
		mRelazioni = aModel.mRelazioni;
	}

	// COSTRUTTORE MODEL
	public AttivitaModel(BigDecimal aIdAttivita, Date aDataInizio, Date aDataChiusura,
			String aCodTipoAttivita, String aDescrTipoAttivita, String aNote,
			/* Blob aDocBlob, */
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aFasSieIdFasSiepe,
			BigDecimal aAssSocIdAssSociale, String aFlagDocumentoRegistrato, String aCodEsitoAttivita,
			String aDescrEsitoAttivita, String aNotaChiusura, RelazioneModel[] aRelazioni) {
		mIdAttivita = aIdAttivita;
		mDataInizio = aDataInizio;
		mDataChiusura = aDataChiusura;
		mCodTipoAttivita = aCodTipoAttivita;
		mDescrTipoAttivita = aDescrTipoAttivita;
		mNote = aNote;
		/* mDocBlob = aDocBlob; */
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mDataInserimento = aDataInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		mFasSieIdFasSiepe = aFasSieIdFasSiepe;
		mAssSocIdAssSociale = aAssSocIdAssSociale;
		mFlagDocumentoRegistrato = aFlagDocumentoRegistrato;
		mCodEsitoAttivita = aCodEsitoAttivita;
		mDescrEsitoAttivita = aDescrEsitoAttivita;
		mNotaChiusura = aNotaChiusura;
		mRelazioni = aRelazioni;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdAttivita() {
		return mIdAttivita;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataChiusura() {
		return mDataChiusura;
	}

	public String getCodTipoAttivita() {
		return mCodTipoAttivita;
	}

	public String getDescrTipoAttivita() {
		return mDescrTipoAttivita;
	}

	public String getNote() {
		return mNote;
	}

	// public Blob getDocBlob() { return mDocBlob; }
	public ByteArrayInputStream getDocBlobIn() {
		return mDocBlobIn;
	}

	public ByteArrayOutputStream getDocBlobOut() {
		return mDocBlobOut;
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

	public BigDecimal getFasSieIdFasSiepe() {
		return mFasSieIdFasSiepe;
	}

	public BigDecimal getAssSocIdAssSociale() {
		return mAssSocIdAssSociale;
	}

	public String getFlagDocumentoRegistrato() {
		return mFlagDocumentoRegistrato;
	}

	public String getCodEsitoAttivita() {
		return mCodEsitoAttivita;
	}

	public String getDescrEsitoAttivita() {
		return mDescrEsitoAttivita;
	}

	public byte[] getDocPerTrasferimento() {
		return mDocPerTrasferimento;
	}

	public String getNotaChiusura() {
		return mNotaChiusura;
	}

	public RelazioneModel[] getRelazioni() {
		return mRelazioni;
	}

	//
	// METODI SET()
	//
	public void setIdAttivita(BigDecimal aValore) {
		mIdAttivita = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataChiusura(Date aValore) {
		mDataChiusura = aValore;
	}

	public void setCodTipoAttivita(String aValore) {
		mCodTipoAttivita = aValore;
	}

	public void setDescrTipoAttivita(String aValore) {
		mDescrTipoAttivita = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	// public void setDocBlob(Blob aValore ) { mDocBlob = aValore; }
	public void setDocBlobIn(ByteArrayInputStream aValore) {
		mDocBlobIn = aValore;
	}

	public void setDocBlobOut(ByteArrayOutputStream aValore) {
		mDocBlobOut = aValore;
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

	public void setFasSieIdFasSiepe(BigDecimal aValore) {
		mFasSieIdFasSiepe = aValore;
	}

	public void setAssSocIdAssSociale(BigDecimal aValore) {
		mAssSocIdAssSociale = aValore;
	}

	public void setFlagDocumentoRegistrato(String aValore) {
		mFlagDocumentoRegistrato = aValore;
	}

	public void setCodEsitoAttivita(String aValore) {
		mCodEsitoAttivita = aValore;
	}

	public void setDescrEsitoAttivita(String aValore) {
		mDescrEsitoAttivita = aValore;
	}

	public void setDocPerTrasferimento(byte[] aValore) {
		mDocPerTrasferimento = aValore;
	}

	public void setDocPerTrasferimento(ByteArrayOutputStream aValore) {
		if (aValore != null) {
			mDocPerTrasferimento = aValore.toByteArray();
		}
	}

	public void setNotaChiusura(String aValore) {
		mNotaChiusura = aValore;
	}

	public void setRelazioni(RelazioneModel[] aValore) {
		mRelazioni = aValore;
	}

}