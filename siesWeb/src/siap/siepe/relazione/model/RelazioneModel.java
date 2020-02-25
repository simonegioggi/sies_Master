package siap.siepe.relazione.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: RelazioneModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il Relazione
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
public class RelazioneModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 8062359198596082150L;
	private BigDecimal mIdRelazione;
	private BigDecimal mAttIdAttivita;
	private BigDecimal mRicIdRichiesta;
	private String mNote;
	private Date mDataEmissione;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mFlagDocumentoRegistrato;
	private String mCodUfficioDestinatario;
	private String mDescrUfficioDestinatario;
	private String mDescrSedeUfficioDestinatario;

	// Gestione Blob
	private ByteArrayInputStream mDocBlobIn;
	private ByteArrayOutputStream mDocBlobOut;
	private byte[] mDocPerTrasferimento = null;

	// COSTRUTTORE DI DEFAULT
	public RelazioneModel() {
		this.mIdRelazione = null;
		this.mAttIdAttivita = null;
		this.mRicIdRichiesta = null;
		this.mNote = "";
		this.mDataEmissione = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFlagDocumentoRegistrato = "";
		this.mCodUfficioDestinatario = "";
		this.mDescrUfficioDestinatario = "";
		this.mDescrSedeUfficioDestinatario = "";
		this.mDocBlobIn = null;
		this.mDocBlobOut = null;
	}

	// COSTRUTTORE DI COPIA
	public RelazioneModel(RelazioneModel aModel) {
		this.mIdRelazione = aModel.mIdRelazione;
		this.mAttIdAttivita = aModel.mAttIdAttivita;
		this.mNote = aModel.mNote;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFlagDocumentoRegistrato = aModel.mFlagDocumentoRegistrato;
		this.mDocPerTrasferimento = aModel.getDocPerTrasferimento();
		this.mCodUfficioDestinatario = aModel.mCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aModel.mDescrUfficioDestinatario;
		this.mDescrSedeUfficioDestinatario = aModel.mDescrSedeUfficioDestinatario;
		this.mDocBlobIn = aModel.mDocBlobIn;
		this.mDocBlobOut = aModel.mDocBlobOut;
	}

	// COSTRUTTORE MODEL
	public RelazioneModel(BigDecimal aIdRelazione, BigDecimal aAttIdAttivita, BigDecimal aRichIdRichiesta,
			String aNote, Date aDataEmissione, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			String aFlagDocumentoRegistrato, String aCodUfficioDestinatario, String aDescrUfficioDestinatario,
			String aDescrSedeUfficioDestinatario)

	{
		this.mIdRelazione = aIdRelazione;
		this.mAttIdAttivita = aAttIdAttivita;
		this.mRicIdRichiesta = aRichIdRichiesta;
		this.mNote = aNote;
		this.mDataEmissione = aDataEmissione;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFlagDocumentoRegistrato = aFlagDocumentoRegistrato;
		this.mDocBlobIn = null;
		this.mDocBlobOut = null;
		this.mCodUfficioDestinatario = aCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aDescrUfficioDestinatario;
		this.mDescrSedeUfficioDestinatario = aDescrSedeUfficioDestinatario;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdRelazione() {
		return mIdRelazione;
	}

	public BigDecimal getAttIdAttivita() {
		return mAttIdAttivita;
	}

	public BigDecimal getRicIdRichiesta() {
		return mRicIdRichiesta;
	}

	public String getNote() {
		return mNote;
	}

	public ByteArrayInputStream getDocBlobIn() {
		return mDocBlobIn;
	}

	public ByteArrayOutputStream getDocBlobOut() {
		return mDocBlobOut;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
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

	public String getFlagDocumentoRegistrato() {
		return mFlagDocumentoRegistrato;
	}

	public String getCodUfficioDestinatario() {
		return mCodUfficioDestinatario;
	}

	public String getDescrUfficioDestinatario() {
		return mDescrUfficioDestinatario;
	}

	public String getDescrSedeUfficioDestinatario() {
		return mDescrSedeUfficioDestinatario;
	}

	public byte[] getDocPerTrasferimento() {
		return mDocPerTrasferimento;
	}

	//
	// METODI SET()
	//
	public void setIdRelazione(BigDecimal aValore) {
		mIdRelazione = aValore;
	}

	public void setAttIdAttivita(BigDecimal aValore) {
		mAttIdAttivita = aValore;
	}

	public void setRicIdRichiesta(BigDecimal aValore) {
		mRicIdRichiesta = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setDocBlobIn(ByteArrayInputStream aValore) {
		mDocBlobIn = aValore;
	}

	public void setDocBlobOut(ByteArrayOutputStream aValore) {
		mDocBlobOut = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
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

	public void setFlagDocumentoRegistrato(String aValore) {
		mFlagDocumentoRegistrato = aValore;
	}

	public void setCodUfficioDestinatario(String aValore) {
		mCodUfficioDestinatario = aValore;
	}

	public void setDescrUfficioDestinatario(String aValore) {
		mDescrUfficioDestinatario = aValore;
	}

	public void setDescrSedeUfficioDestinatario(String aValore) {
		mDescrSedeUfficioDestinatario = aValore;
	}

	public void setDocPerTrasferimento(byte[] aValore) {
		mDocPerTrasferimento = aValore;
	}

	public void setDocPerTrasferimento(ByteArrayOutputStream aValore) {
		if (aValore != null) {
			mDocPerTrasferimento = aValore.toByteArray();
		}
	}
}
