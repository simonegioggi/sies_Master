package siap.siepe.richiesta.model;

/**
* <p>Title: RichiestaModel</p>
* <p>Description: Classe Model che rappresenta il Richiesta</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

// 20060918-21:26 Controllare e ripulire da metodi e/o oggetti non usati.

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.siepe.relazione.model.RelazioneModel;

public class RichiestaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 6636275314603926323L;
	private BigDecimal mIdRichiesta;
	private Date mDataRichiesta;
	private String mCodTipoRichiesta;
	private String mDescrTipoRichiesta;
	private String mCodTipoRichiedente;
	private String mDescrTipoRichiedente;
	private String mNote;
	// private Blob mDocBlob;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFasSiepe;
	private String mFlagDocumentoRegistrato;
	private String mCodUfficioDestinatario;
	private String mDescrUfficioDestinatario;
	private String mDescrSedeUfficioDestinatario;
	// Gestione Blob
	private ByteArrayInputStream mDocBlobIn;
	private ByteArrayOutputStream mDocBlobOut;
	private byte[] mDocPerTrasferimento = null;
	private RelazioneModel[] mRelazioni;

	// COSTRUTTORE DI DEFAULT
	public RichiestaModel() {
		this.mIdRichiesta = null;
		this.mDataRichiesta = null;
		this.mCodTipoRichiesta = "";
		this.mDescrTipoRichiesta = "";
		this.mCodTipoRichiedente = "";
		this.mDescrTipoRichiedente = "";
		this.mNote = "";
		// this.mDocBlob = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSieIdFasSiepe = null;
		this.mFlagDocumentoRegistrato = "";
		this.mCodUfficioDestinatario = "";
		this.mDescrUfficioDestinatario = "";
		this.mDescrSedeUfficioDestinatario = "";
		this.mDocBlobIn = null;
		this.mDocBlobOut = null;
		this.mRelazioni = null;
	}

	// COSTRUTTORE DI COPIA
	public RichiestaModel(RichiestaModel aModel) {
		this.mIdRichiesta = aModel.mIdRichiesta;
		this.mDataRichiesta = aModel.mDataRichiesta;
		this.mCodTipoRichiesta = aModel.mCodTipoRichiesta;
		this.mDescrTipoRichiesta = aModel.mDescrTipoRichiesta;
		this.mCodTipoRichiedente = aModel.mCodTipoRichiedente;
		this.mDescrTipoRichiedente = aModel.mDescrTipoRichiedente;
		this.mNote = aModel.mNote;
		// this.mDocBlob = aModel.mDocBlob;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFasSiepe = aModel.mFasSieIdFasSiepe;
		this.mFlagDocumentoRegistrato = aModel.mFlagDocumentoRegistrato;
		this.mCodUfficioDestinatario = aModel.mCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aModel.mDescrUfficioDestinatario;
		this.mDescrSedeUfficioDestinatario = aModel.mDescrSedeUfficioDestinatario;
		this.mDocBlobIn = aModel.mDocBlobIn;
		this.mDocBlobOut = aModel.mDocBlobOut;
		this.mDocPerTrasferimento = aModel.getDocPerTrasferimento();
		this.mRelazioni = aModel.mRelazioni;
	}

	// COSTRUTTORE MODEL
	public RichiestaModel(BigDecimal aIdRichiesta, Date aDataRichiesta, String aCodTipoRichiesta,
			String aDescrTipoRichiesta, String aCodTipoRichiedente, String aDescrTipoRichiedente,
			String aNote,
			// Blob aDocBlob,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aFasSieIdFasSiepe,
			String aFlagDocumentoRegistrato, String aCodUfficioDestinatario, String aDescrUfficioDestinatario,
			String aDescrSedeUfficioDestinatario) {
		this.mIdRichiesta = aIdRichiesta;
		this.mDataRichiesta = aDataRichiesta;
		this.mCodTipoRichiesta = aCodTipoRichiesta;
		this.mDescrTipoRichiesta = aDescrTipoRichiesta;
		this.mCodTipoRichiedente = aCodTipoRichiedente;
		this.mDescrTipoRichiedente = aDescrTipoRichiedente;
		this.mNote = aNote;
		// this.mDocBlob = aDocBlob;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFasSiepe = aFasSieIdFasSiepe;
		this.mFlagDocumentoRegistrato = aFlagDocumentoRegistrato;
		this.mCodUfficioDestinatario = aCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aDescrUfficioDestinatario;
		this.mDescrSedeUfficioDestinatario = aDescrSedeUfficioDestinatario;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdRichiesta() {
		return mIdRichiesta;
	}

	public Date getDataRichiesta() {
		return mDataRichiesta;
	}

	public String getCodTipoRichiesta() {
		return mCodTipoRichiesta;
	}

	public String getDescrTipoRichiesta() {
		return mDescrTipoRichiesta;
	}

	public String getCodTipoRichiedente() {
		return mCodTipoRichiedente;
	}

	public String getDescrTipoRichiedente() {
		return mDescrTipoRichiedente;
	}

	public String getNote() {
		return mNote;
	}

	// public Blob getDocBlob() { return mDocBlob; }
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

	public ByteArrayInputStream getDocBlobIn() {
		return mDocBlobIn;
	}

	public ByteArrayOutputStream getDocBlobOut() {
		return mDocBlobOut;
	}

	public byte[] getDocPerTrasferimento() {
		return mDocPerTrasferimento;
	}

	public RelazioneModel[] getRelazioni() {
		return mRelazioni;
	}

	//
	// METODI SET()
	//
	public void setIdRichiesta(BigDecimal aValore) {
		mIdRichiesta = aValore;
	}

	public void setDataRichiesta(Date aValore) {
		mDataRichiesta = aValore;
	}

	public void setCodTipoRichiesta(String aValore) {
		mCodTipoRichiesta = aValore;
	}

	public void setDescrTipoRichiesta(String aValore) {
		mDescrTipoRichiesta = aValore;
	}

	public void setCodTipoRichiedente(String aValore) {
		mCodTipoRichiedente = aValore;
	}

	public void setDescrTipoRichiedente(String aValore) {
		mDescrTipoRichiedente = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	// public void setDocBlob(Blob aValore ) { mDocBlob = aValore; }
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

	public void setDocBlobIn(ByteArrayInputStream aValore) {
		mDocBlobIn = aValore;
	}

	public void setDocBlobOut(ByteArrayOutputStream aValore) {
		mDocBlobOut = aValore;
	}

	public void setDocPerTrasferimento(byte[] aValore) {
		mDocPerTrasferimento = aValore;
	}

	public void setDocPerTrasferimento(ByteArrayOutputStream aValore) {
		if (aValore != null) {
			mDocPerTrasferimento = aValore.toByteArray();
		}
	}

	public void setRelazioni(RelazioneModel[] aValore) {
		mRelazioni = aValore;
	}
}