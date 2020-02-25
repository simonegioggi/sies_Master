package siap.regesies.regesentenza.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.regesies.regesoggetto.model.RegeSoggettoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ProvvedimentoModel
 * </p>
 * <p>
 * Description: Aggregato Model del Provvedimento Rege
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProvvedimentoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1457988363475216447L;

	private RegeSentenzaModel mRegeSentenza;
	private RegeSoggettoModel mRegeSoggetto;
	private Vector mResidenze;
	private Vector mSoggettiOmonimi;
	private SentenzaModel mSentenza;
	private Vector mReati;
	private Vector mCircostanze;
	private Vector mNotizieDiReato;
	private Vector mDifensori;
	private boolean mIsResidenzeCheck;
	private boolean mIsReatoCheck;
	private boolean mIsNotiziaReatoCheck;
	private boolean mIsCircostanzaCheck;
	private boolean mEstendiFascicoloSiep;
	private boolean mIsDispositivoCheck;
	private boolean mIsDifensoriCheck;

	private BigDecimal mIdSoggettoOmonimo;
	private SoggettoModel mSoggetto;

	private String mUtente; // Utente che effettua l'importazione dei dati da Rege
	private String mUfficio;// Ufficio che effettua l'importazione dei dati da Rege
	private Date mDataInserimento;// Data

	private FascicoloSiepModel mFascicolo;

	public ProvvedimentoModel() {
		init();
	}

	private void init() {
		mRegeSentenza = null;
		mRegeSoggetto = null;
		mResidenze = null;
		mSoggettiOmonimi = null;
		mSentenza = null;
		mReati = null;
		mCircostanze = null;
		mNotizieDiReato = null;
		mDifensori = null;
		mIsResidenzeCheck = false;
		mIsReatoCheck = false;
		mIsNotiziaReatoCheck = false;
		mIsCircostanzaCheck = false;
		mEstendiFascicoloSiep = false;
		mIsDispositivoCheck = false;
		mIsDifensoriCheck = false;
		mIdSoggettoOmonimo = null;
		mSoggetto = null;
		mUtente = "";
		mUfficio = "";
		mDataInserimento = null;
		mFascicolo = null;
	}

	/**
	 * Costruttore di copia
	 */
	public ProvvedimentoModel(ProvvedimentoModel aProvv) {
		init();

		if (aProvv.getRegeSentenza() != null)
			mRegeSentenza = new RegeSentenzaModel(aProvv.getRegeSentenza());
		if (aProvv.getRegeSoggetto() != null)
			mRegeSoggetto = new RegeSoggettoModel(aProvv.getRegeSoggetto());
		if (aProvv.getResidenze() != null)
			mResidenze = new Vector(aProvv.getResidenze());
		if (aProvv.getSoggettiOmonimi() != null)
			mSoggettiOmonimi = new Vector(aProvv.getSoggettiOmonimi());
		if (aProvv.getSentenza() != null)
			mSentenza = new SentenzaModel(aProvv.getSentenza());
		if (aProvv.getReati() != null)
			mReati = new Vector(aProvv.getReati());
		if (aProvv.getCircostanze() != null)
			mCircostanze = new Vector(aProvv.getCircostanze());
		if (aProvv.getNotizieDiReato() != null)
			mNotizieDiReato = new Vector(aProvv.getNotizieDiReato());
		if (aProvv.getDifensori() != null)
			mDifensori = new Vector(aProvv.getDifensori());

		mIsResidenzeCheck = aProvv.isResidenzeCheck();
		mIsReatoCheck = aProvv.isReatoCheck();
		mIsNotiziaReatoCheck = aProvv.isNotiziaReatoCheck();
		mIsCircostanzaCheck = aProvv.isCircostanzaCheck();
		mEstendiFascicoloSiep = aProvv.isEstendiFascicoloSiep();
		mIsDispositivoCheck = aProvv.isDispositivoCheck();
		mIsDifensoriCheck = aProvv.isDifensoriCheck();
		mIdSoggettoOmonimo = aProvv.getIdSoggettoOmonimo();

		if (aProvv.getSoggetto() != null)
			mSoggetto = new SoggettoModel(aProvv.getSoggetto());

		mUtente = aProvv.getUtente();
		mDataInserimento = aProvv.getDataInserimento();
		mUfficio = aProvv.getUfficio();

		if (aProvv.getFascicoloSiep() != null)
			mFascicolo = new FascicoloSiepModel(aProvv.getFascicoloSiep());
	}

	// Metodi GET
	public RegeSentenzaModel getRegeSentenza() {
		return mRegeSentenza;
	}

	public RegeSoggettoModel getRegeSoggetto() {
		return mRegeSoggetto;
	}

	public Vector getResidenze() {
		return mResidenze;
	}

	public Vector getSoggettiOmonimi() {
		return mSoggettiOmonimi;
	}

	public SentenzaModel getSentenza() {
		return mSentenza;
	}

	public Vector getReati() {
		return mReati;
	}

	public Vector getCircostanze() {
		return mCircostanze;
	}

	public Vector getNotizieDiReato() {
		return mNotizieDiReato;
	}

	public Vector getDifensori() {
		return mDifensori;
	}

	public boolean isResidenzeCheck() {
		return mIsResidenzeCheck;
	}

	public boolean isReatoCheck() {
		return mIsReatoCheck;
	}

	public boolean isNotiziaReatoCheck() {
		return mIsNotiziaReatoCheck;
	}

	public boolean isCircostanzaCheck() {
		return mIsCircostanzaCheck;
	}

	public boolean isDispositivoCheck() {
		return mIsDispositivoCheck;
	}

	public boolean isDifensoriCheck() {
		return mIsDifensoriCheck;
	}

	public boolean isEstendiFascicoloSiep() {
		return mEstendiFascicoloSiep;
	}

	public BigDecimal getIdSoggettoOmonimo() {
		return mIdSoggettoOmonimo;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public String getUtente() {
		return mUtente;
	}

	public String getUfficio() {
		return mUfficio;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicolo;
	}

	// Metodi SET
	public void setRegeSentenza(RegeSentenzaModel aValore) {
		mRegeSentenza = aValore;
	}

	public void setRegeSoggetto(RegeSoggettoModel aValore) {
		mRegeSoggetto = aValore;
	}

	public void setResidenze(Vector aValore) {
		mResidenze = aValore;
	}

	public void setSoggettiOmonimi(Vector aValore) {
		mSoggettiOmonimi = aValore;
	}

	public void setSentenza(SentenzaModel aValore) {
		mSentenza = aValore;
	}

	public void setReati(Vector aValore) {
		mReati = aValore;
	}

	public void setCircostanze(Vector aValore) {
		mCircostanze = aValore;
	}

	public void setNotizieDiReato(Vector aValore) {
		mNotizieDiReato = aValore;
	}

	public void setDifensori(Vector aValore) {
		mDifensori = aValore;
	}

	public void setIsResidenzeCheck(boolean aValore) {
		mIsResidenzeCheck = aValore;
	}

	public void setIsReatoCheck(boolean aValore) {
		mIsReatoCheck = aValore;
	}

	public void setIsDispositivoCheck(boolean aValore) {
		mIsDispositivoCheck = aValore;
	}

	public void setIsDifensoriCheck(boolean aValore) {
		mIsDifensoriCheck = aValore;
	}

	public void setIsNotiziaReatoCheck(boolean aValore) {
		mIsNotiziaReatoCheck = aValore;
	}

	public void setIsCircostanzaCheck(boolean aValore) {
		mIsCircostanzaCheck = aValore;
	}

	public void setEstendiFascicoloSiep(boolean aValore) {
		mEstendiFascicoloSiep = aValore;
	}

	public void setIdSoggettoOmonimo(BigDecimal aValore) {
		mIdSoggettoOmonimo = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setUtente(String aValore) {
		mUtente = aValore;
	}

	public void setUfficio(String aValore) {
		mUfficio = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicolo = aValore;
	}

}