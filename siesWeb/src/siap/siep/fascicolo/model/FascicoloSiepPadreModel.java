package siap.siep.fascicolo.model;

/**
* <p>Title: FascicoloSiepModel</p>
* <p>Description: Classe Model che rappresenta il FascicoloSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.model.GenericModel;

public class FascicoloSiepPadreModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6256925903034228194L;

	private BigDecimal mIdFascicoloSiep;
	private BigDecimal mChiaveAnno;
	private String mChiaveUfficio;
	private String mDescrTipoUfficio;
	private String mDescrComuneUfficio;
	private String mCodTipoUfficio;
	private BigDecimal mChiaveProgr;
	private String mCodStatoFascicolo;
	private String mDescrStatoFascicolo;
	private Date mDataIscrizione;
	private Date mDataArchiviazione;
	private String mCodMotivoArchiviazione;
	private String mDescrMotivoArchiviazione;
	private String mLetteraFascicolo;
	private String mAnnoFascicoloUnione;
	private String mNumFascicoloUnione;
	private Date mDataUnione;
	private String mNote;
	private String mCodTipoPosLibero;
	private String mDescrTipoPosLibero;
	private String mFlagValidato;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private BigDecimal mSogIdSoggetto;
	private BigDecimal mSenIdSentenza;
	private BigDecimal mFasSieIdFascicoloSiep;
	private String mFlagAltraCausa;

	private BigDecimal mChiaveProgrIniziale;
	private BigDecimal mChiaveAnnoIniziale;
	private BigDecimal mChiaveProgrFinale;
	private BigDecimal mChiaveAnnoFinale;

	private Date mDataIscrizioneIniziale;
	private Date mDataIscrizioneFinale;
	private String mDescrStatoProcedimento;
	private String mCodDistretto;

	private Date mDataIrrevocabilita;
	private String mFlagCumulante;
	private String mFlagCumulato;
	private String mCodUfficioUnione;
	private String mDescrTipoUfficioUnione;
	private String mDescrComuneUfficioUnione;
	// per interoperabilita tra SIES e NSC (WS)
	private BigDecimal mkeyProvvNsc;

	private SoggettoModel mSoggetto;
	private SentenzaModel mSentenza;

	private BigDecimal mNumFascicoli;
	private int mTipoProgressivo;

	// Modifica Accorpamento Uffici
	private String mCodTipoUfficioInserimento;
	private String mDescrTipoUfficioInserimento;
	private String mDescrComuneUfficioInserimento;
	private BigDecimal mChiaveProgrOrig;
	private String mFlagUfficioAccorpato;

	// COSTRUTTORE DI DEFAULT
	public FascicoloSiepPadreModel() {
		this.mIdFascicoloSiep = null;
		this.mChiaveAnno = null;
		this.mChiaveUfficio = "";
		this.mDescrTipoUfficio = "";
		this.mDescrComuneUfficio = "";
		this.mChiaveProgr = null;
		this.mCodStatoFascicolo = "";
		this.mDescrStatoFascicolo = "";
		this.mDataIscrizione = null;
		this.mDataArchiviazione = null;
		this.mCodMotivoArchiviazione = "";
		this.mDescrMotivoArchiviazione = "";
		this.mLetteraFascicolo = "";
		this.mAnnoFascicoloUnione = "";
		this.mNumFascicoloUnione = "";
		this.mDataUnione = null;
		this.mNote = "";
		this.mCodTipoPosLibero = "";
		this.mDescrTipoPosLibero = "";
		this.mFlagValidato = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mSogIdSoggetto = null;
		this.mSenIdSentenza = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mFlagAltraCausa = "";
		this.mCodTipoUfficio = null;

		this.mChiaveProgrIniziale = null;
		this.mChiaveAnnoIniziale = null;
		this.mChiaveProgrFinale = null;
		this.mChiaveAnnoFinale = null;

		this.mDataIscrizioneIniziale = null;
		this.mDataIscrizioneFinale = null;
		this.mDescrStatoProcedimento = "";
		this.mCodDistretto = "";
		this.mDataIrrevocabilita = null;
		this.mFlagCumulante = "";
		this.mFlagCumulato = "";
		this.mTipoProgressivo = 0;
		this.mCodUfficioUnione = "-";
		this.mDescrTipoUfficioUnione = "";
		this.mDescrComuneUfficioUnione = "";

		this.mSoggetto = null;
		this.mSentenza = null;
		this.mNumFascicoli = null;
		this.mkeyProvvNsc = null;

		this.mCodTipoUfficioInserimento = "";
		this.mDescrTipoUfficioInserimento = "";
		this.mDescrComuneUfficioInserimento = "";
		this.mChiaveProgrOrig = null;
		this.mFlagUfficioAccorpato = "";

	}

	// COSTRUTTORE DI COPIA
	public FascicoloSiepPadreModel(FascicoloSiepPadreModel aModel) {
		this.mIdFascicoloSiep = aModel.mIdFascicoloSiep;
		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveUfficio = aModel.mChiaveUfficio;
		this.mDescrTipoUfficio = aModel.mDescrTipoUfficio;
		this.mDescrComuneUfficio = aModel.mDescrComuneUfficio;
		this.mChiaveProgr = aModel.mChiaveProgr;
		this.mCodStatoFascicolo = aModel.mCodStatoFascicolo;
		this.mDescrStatoFascicolo = aModel.mDescrStatoFascicolo;
		this.mDataIscrizione = aModel.mDataIscrizione;
		this.mDataArchiviazione = aModel.mDataArchiviazione;
		this.mCodMotivoArchiviazione = aModel.mCodMotivoArchiviazione;
		this.mDescrMotivoArchiviazione = aModel.mDescrMotivoArchiviazione;
		this.mLetteraFascicolo = aModel.mLetteraFascicolo;
		this.mAnnoFascicoloUnione = aModel.mAnnoFascicoloUnione;
		this.mNumFascicoloUnione = aModel.mNumFascicoloUnione;
		this.mDataUnione = aModel.mDataUnione;
		this.mNote = aModel.mNote;
		this.mCodTipoPosLibero = aModel.mCodTipoPosLibero;
		this.mDescrTipoPosLibero = aModel.mDescrTipoPosLibero;
		this.mFlagValidato = aModel.mFlagValidato;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		this.mSenIdSentenza = aModel.mSenIdSentenza;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mFlagAltraCausa = aModel.mFlagAltraCausa;
		this.mCodTipoUfficio = aModel.mCodTipoUfficio;

		this.mChiaveProgrIniziale = aModel.mChiaveProgrIniziale;
		this.mChiaveAnnoIniziale = aModel.mChiaveAnnoIniziale;
		this.mChiaveProgrFinale = aModel.mChiaveProgrFinale;
		this.mChiaveAnnoFinale = aModel.mChiaveAnnoFinale;

		this.mDataIscrizioneIniziale = aModel.mDataIscrizioneIniziale;
		this.mDataIscrizioneFinale = aModel.mDataIscrizioneFinale;
		this.mDescrStatoProcedimento = aModel.mDescrStatoProcedimento;
		this.mCodDistretto = aModel.mCodDistretto;
		this.mDataIrrevocabilita = aModel.mDataIrrevocabilita;
		this.mFlagCumulante = aModel.mFlagCumulante;
		this.mFlagCumulato = aModel.mFlagCumulato;

		this.mSoggetto = aModel.mSoggetto;
		this.mSentenza = aModel.mSentenza;
		this.mNumFascicoli = aModel.mNumFascicoli;
		this.mCodUfficioUnione = aModel.mCodUfficioUnione;
		this.mDescrTipoUfficioUnione = aModel.mDescrTipoUfficioUnione;
		this.mDescrComuneUfficioUnione = aModel.mDescrComuneUfficioUnione;
		this.mkeyProvvNsc = aModel.mkeyProvvNsc;

		this.mCodTipoUfficioInserimento = aModel.mCodTipoUfficioInserimento;
		this.mDescrTipoUfficioInserimento = aModel.mDescrTipoUfficioInserimento;
		this.mDescrComuneUfficioInserimento = aModel.mDescrComuneUfficioInserimento;
		this.mChiaveProgrOrig = aModel.mChiaveProgrOrig;
		this.mFlagUfficioAccorpato = aModel.mFlagUfficioAccorpato;

	}

	// COSTRUTTORE MODEL
	public FascicoloSiepPadreModel(BigDecimal aIdFascicoloSiep, BigDecimal aChiaveAnno, String aChiaveUfficio,
			String aDescrTipoUfficio, String aDescrComuneUfficio, BigDecimal aChiaveProgr,
			String aCodStatoFascicolo, String aDescrStatoFascicolo, Date aDataIscrizione,
			Date aDataArchiviazione, String aCodMotivoArchiviazione, String aDescrMotivoArchiviazione,
			String aLetteraFascicolo, String aAnnoFascicoloUnione, String aNumFascicoloUnione,
			Date aDataUnione, String aNote, String aCodTipoPosLibero, String aDescrTipoPosLibero,
			String aFlagValidato, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, BigDecimal aSogIdSoggetto, BigDecimal aSenIdSentenza,
			BigDecimal aFasSieIdFascicoloSiep, String aFlagAltraCausa, BigDecimal aNumFascicoli,
			String aDescrStatoProcedimento, String aCodDistretto, Date aDataIrrevocabilita,
			String aFlagCumulante, String aFlagCumulato, String aCodUfficioUnione,
			String aDescrTipoUfficioUnione, String aDescrComuneUfficioUnione, BigDecimal aKeyProvvNsc,
			BigDecimal aChiaveProgrOrig) {
		this.mIdFascicoloSiep = aIdFascicoloSiep;
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveUfficio = aChiaveUfficio;
		this.mDescrTipoUfficio = aDescrTipoUfficio;
		this.mDescrComuneUfficio = aDescrComuneUfficio;
		this.mChiaveProgr = aChiaveProgr;
		this.mCodStatoFascicolo = aCodStatoFascicolo;
		this.mDescrStatoFascicolo = aDescrStatoFascicolo;
		this.mDataIscrizione = aDataIscrizione;
		this.mDataArchiviazione = aDataArchiviazione;
		this.mCodMotivoArchiviazione = aCodMotivoArchiviazione;
		this.mDescrMotivoArchiviazione = aDescrMotivoArchiviazione;
		this.mLetteraFascicolo = aLetteraFascicolo;
		this.mAnnoFascicoloUnione = aAnnoFascicoloUnione;
		this.mNumFascicoloUnione = aNumFascicoloUnione;
		this.mDataUnione = aDataUnione;
		this.mNote = aNote;
		this.mCodTipoPosLibero = aCodTipoPosLibero;
		this.mDescrTipoPosLibero = aDescrTipoPosLibero;
		this.mFlagValidato = aFlagValidato;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mSogIdSoggetto = aSogIdSoggetto;
		this.mSenIdSentenza = aSenIdSentenza;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mFlagAltraCausa = aFlagAltraCausa;
		this.mDataIscrizioneIniziale = null;
		this.mDataIscrizioneFinale = null;
		this.mDescrStatoProcedimento = aDescrStatoProcedimento;
		this.mCodDistretto = aCodDistretto;
		this.mChiaveProgrIniziale = null;
		this.mChiaveAnnoIniziale = null;
		this.mChiaveProgrFinale = null;
		this.mChiaveAnnoFinale = null;

		this.mDataIrrevocabilita = aDataIrrevocabilita;
		this.mFlagCumulante = aFlagCumulante;
		this.mFlagCumulato = aFlagCumulato;
		this.mCodUfficioUnione = aCodUfficioUnione;
		this.mDescrTipoUfficioUnione = aDescrTipoUfficioUnione;
		this.mDescrComuneUfficioUnione = aDescrComuneUfficioUnione;

		this.mSoggetto = null;
		this.mSentenza = null;
		this.mNumFascicoli = aNumFascicoli;
		this.mkeyProvvNsc = aKeyProvvNsc;

		this.mChiaveProgrOrig = aChiaveProgrOrig;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdFascicoloSiep() {
		return mIdFascicoloSiep;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public String getChiaveUfficio() {
		return mChiaveUfficio;
	}

	public String getDescrTipoUfficio() {
		return mDescrTipoUfficio;
	}

	public String getDescrComuneUfficio() {
		return mDescrComuneUfficio;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public String getCodStatoFascicolo() {
		return mCodStatoFascicolo;
	}

	public String getDescrStatoFascicolo() {
		return mDescrStatoFascicolo;
	}

	public Date getDataIscrizione() {
		return mDataIscrizione;
	}

	public Date getDataArchiviazione() {
		return mDataArchiviazione;
	}

	public String getCodMotivoArchiviazione() {
		return mCodMotivoArchiviazione;
	}

	public String getDescrMotivoArchiviazione() {
		return mDescrMotivoArchiviazione;
	}

	public String getLetteraFascicolo() {
		return mLetteraFascicolo;
	}

	public String getAnnoFascicoloUnione() {
		return mAnnoFascicoloUnione;
	}

	public String getNumFascicoloUnione() {
		return mNumFascicoloUnione;
	}

	public Date getDataUnione() {
		return mDataUnione;
	}

	public String getNote() {
		return mNote;
	}

	public String getCodTipoPosLibero() {
		return mCodTipoPosLibero;
	}

	public String getDescrTipoPosLibero() {
		return mDescrTipoPosLibero;
	}

	public String getFlagValidato() {
		return mFlagValidato;
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

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public BigDecimal getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public BigDecimal getSenIdSentenza() {
		return mSenIdSentenza;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public String getFlagAltraCausa() {
		return mFlagAltraCausa;
	}

	public String getCodTipoUfficio() {
		return mCodTipoUfficio;
	}

	public BigDecimal getChiaveProgrIniziale() {
		return mChiaveProgrIniziale;
	}

	public BigDecimal getChiaveAnnoIniziale() {
		return mChiaveAnnoIniziale;
	}

	public BigDecimal getChiaveProgrFinale() {
		return mChiaveProgrFinale;
	}

	public BigDecimal getChiaveAnnoFinale() {
		return mChiaveAnnoFinale;
	}

	public Date getDataIscrizioneIniziale() {
		return mDataIscrizioneIniziale;
	}

	public Date getDataIscrizioneFinale() {
		return mDataIscrizioneFinale;
	}

	public Date getDataIrrevocabilita() {
		return mDataIrrevocabilita;
	}

	public String getFlagCumulante() {
		return mFlagCumulante;
	}

	public String getFlagCumulato() {
		return mFlagCumulato;
	}

	public String getCodUfficioUnione() {
		return mCodUfficioUnione;
	}

	public String getDescrTipoUfficioUnione() {
		return mDescrTipoUfficioUnione;
	}

	public String getDescrComuneUfficioUnione() {
		return mDescrComuneUfficioUnione;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public SentenzaModel getSentenza() {
		return mSentenza;
	}

	public BigDecimal getNumFascicoli() {
		return mNumFascicoli;
	}

	public String getDescrStatoProcedimento() {
		return mDescrStatoProcedimento;
	}

	public String getCodDistretto() {
		return mCodDistretto;
	}

	public int getTipoProgressivo() {
		return mTipoProgressivo;
	}

	public BigDecimal getKeyProvvNsc() {
		return mkeyProvvNsc;
	}

	public String getCodTipoUfficioInserimento() {
		return mCodTipoUfficioInserimento;
	}

	public String getDescrTipoUfficioInserimento() {
		return mDescrTipoUfficioInserimento;
	}

	public String getDescrComuneUfficioInserimento() {
		return mDescrComuneUfficioInserimento;
	}

	public BigDecimal getChiaveProgrOrig() {
		return mChiaveProgrOrig;
	}

	public String getFlagUfficioAccorpato() {
		return mFlagUfficioAccorpato;
	}

	//
	// METODI SET()
	//

	public void setIdFascicoloSiep(BigDecimal aValore) {
		mIdFascicoloSiep = aValore;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveUfficio(String aValore) {
		mChiaveUfficio = aValore;
	}

	public void setDescrTipoUfficio(String aValore) {
		mDescrTipoUfficio = aValore;
	}

	public void setDescrComuneUfficio(String aValore) {
		mDescrComuneUfficio = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setCodStatoFascicolo(String aValore) {
		mCodStatoFascicolo = aValore;
	}

	public void setDescrStatoFascicolo(String aValore) {
		mDescrStatoFascicolo = aValore;
	}

	public void setDataIscrizione(Date aValore) {
		mDataIscrizione = aValore;
	}

	public void setDataArchiviazione(Date aValore) {
		mDataArchiviazione = aValore;
	}

	public void setCodMotivoArchiviazione(String aValore) {
		mCodMotivoArchiviazione = aValore;
	}

	public void setDescrMotivoArchiviazione(String aValore) {
		mDescrMotivoArchiviazione = aValore;
	}

	public void setLetteraFascicolo(String aValore) {
		mLetteraFascicolo = aValore;
	}

	public void setAnnoFascicoloUnione(String aValore) {
		mAnnoFascicoloUnione = aValore;
	}

	public void setNumFascicoloUnione(String aValore) {
		mNumFascicoloUnione = aValore;
	}

	public void setDataUnione(Date aValore) {
		mDataUnione = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCodTipoPosLibero(String aValore) {
		mCodTipoPosLibero = aValore;
	}

	public void setDescrTipoPosLibero(String aValore) {
		mDescrTipoPosLibero = aValore;
	}

	public void setFlagValidato(String aValore) {
		mFlagValidato = aValore;
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

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setSenIdSentenza(BigDecimal aValore) {
		mSenIdSentenza = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setFlagAltraCausa(String aValore) {
		mFlagAltraCausa = aValore;
	}

	public void setCodTipoUfficio(String aValore) {
		mCodTipoUfficio = aValore;
	}

	public void setChiaveProgrIniziale(BigDecimal aValore) {
		mChiaveProgrIniziale = aValore;
	}

	public void setChiaveAnnoIniziale(BigDecimal aValore) {
		mChiaveAnnoIniziale = aValore;
	}

	public void setChiaveProgrFinale(BigDecimal aValore) {
		mChiaveProgrFinale = aValore;
	}

	public void setChiaveAnnoFinale(BigDecimal aValore) {
		mChiaveAnnoFinale = aValore;
	}

	public void setDataIscrizioneIniziale(Date aValore) {
		mDataIscrizioneIniziale = aValore;
	}

	public void setDataIscrizioneFinale(Date aValore) {
		mDataIscrizioneFinale = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setSentenza(SentenzaModel aValore) {
		mSentenza = aValore;
	}

	public void setNumFascicoli(BigDecimal aValore) {
		mNumFascicoli = aValore;
	}

	public void setDescrStatoProcedimento(String aValore) {
		mDescrStatoProcedimento = aValore;
	}

	public void setCodDistretto(String aValore) {
		mCodDistretto = aValore;
	}

	public void setDataIrrevocabilita(Date aValore) {
		mDataIrrevocabilita = aValore;
	}

	public void setFlagCumulante(String aValore) {
		mFlagCumulante = aValore;
	}

	public void setFlagCumulato(String aValore) {
		mFlagCumulato = aValore;
	}

	public void setCodUfficioUnione(String aValore) {
		mCodUfficioUnione = aValore;
	}

	public void setDescrTipoUfficioUnione(String aValore) {
		mDescrTipoUfficioUnione = aValore;
	}

	public void setDescrComuneUfficioUnione(String aValore) {
		mDescrComuneUfficioUnione = aValore;
	}

	public void setTipoProgressivo(int aValore) {
		mTipoProgressivo = aValore;
	}

	public void setKeyProvvNsc(BigDecimal aValore) {
		mkeyProvvNsc = aValore;
	}

	public void setCodTipoUfficioInserimento(String aValore) {
		mCodTipoUfficioInserimento = aValore;
	}

	public void setDescrTipoUfficioInserimento(String aValore) {
		mDescrTipoUfficioInserimento = aValore;
	}

	public void setDescrComuneUfficioInserimento(String aValore) {
		mDescrComuneUfficioInserimento = aValore;
	}

	public void setChiaveProgrOrig(BigDecimal aValore) {
		mChiaveProgrOrig = aValore;
	}

	public void setFlagUfficioAccorpato(String aValore) {
		mFlagUfficioAccorpato = aValore;
	}

	public String toString() {
		String lToString = this.mIdFascicoloSiep + " - " + this.mChiaveAnno + " - " + this.mChiaveUfficio
				+ " - " + this.mDescrTipoUfficio + " - " + this.mCodTipoUfficio + " - "
				+ this.mDescrComuneUfficio + " - " + this.mChiaveProgr + " - " + this.mCodStatoFascicolo
				+ " - " + this.mDescrStatoFascicolo + " - " + this.mDataIscrizione + " - "
				+ this.mDataArchiviazione + " - " + this.mCodMotivoArchiviazione + " - "
				+ this.mDescrMotivoArchiviazione + " - " + this.mLetteraFascicolo + " - "
				+ this.mAnnoFascicoloUnione + " - " + this.mNumFascicoloUnione + " - " + this.mDataUnione
				+ " - " + this.mNote + " - " + this.mCodTipoPosLibero + " - " + this.mDescrTipoPosLibero
				+ " - " + this.mFlagValidato + " - " + this.mCodOperatoreInserimento + " - "
				+ this.mDataInserimento + " - " + this.mCodUfficioInserimento + " - "
				+ this.mCodOperatoreAggiornamento + " - " + this.mDataAggiornamento + " - "
				+ this.mCodUfficioAggiornamento + " - " + this.mSogIdSoggetto + " - " + this.mSenIdSentenza
				+ " - " + this.mFasSieIdFascicoloSiep + " - " + this.mFlagAltraCausa + " - "
				+ this.mCodDistretto + " - " + this.mDataIrrevocabilita + " - " + this.mFlagCumulante + " - "
				+ this.mFlagCumulato + " - " + mTipoProgressivo + " - " + this.mDescrStatoProcedimento + " - "
				+ this.mCodUfficioUnione + " - " + this.mDescrTipoUfficioUnione + " - "
				+ this.mDescrComuneUfficioUnione + " - " + this.mkeyProvvNsc + " - "
				+ this.mCodUfficioInserimento + " - " + this.mCodTipoUfficioInserimento + " - "
				+ this.mDescrTipoUfficioInserimento + " - " + this.mFlagUfficioAccorpato + " - "
				+ this.mChiaveProgrOrig;

		return lToString;
	}

}