package siap.bdmc.sbpren.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ProvvedimentoModel
 * </p>
 * <p>
 * Description: Aggregato Model del Provvedimento BDMC
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProvvedimentoModelBDMC extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5318873010787777438L;

	private SbPrenModel mSbPren;

	private Vector mSoggettiOmonimi;

	private Vector mSbViewProcpena;

	private Vector mSbViewReat;

	private Vector mSbViewCapoImpu;

	private Vector mSbPeriPren;

	private boolean mIsLuogoCheck;

	private boolean mIsSbViewReatCheck;

	private boolean mIsSbViewCapoImpuCheck;

	private boolean mIsSbPeriPrenCheck;

	private boolean mIsSbProcPenaCheck;

	private boolean mIsSbCircostanzeCheck;

	private boolean mEstendiFascicoloSiep;

	private BigDecimal mIdSoggettoOmonimo;

	private SoggettoModel mSoggetto;

	private SentenzaModel mSentenza;

	private String mUtente; // Utente che effettua l'importazione dei dati da
							// Rege

	private String mUfficio;// Ufficio che effettua l'importazione dei dati da
							// Rege

	private Date mDataInserimento;// Data

	private Date mDataArrivoAtto;

	private FascicoloSiepModel mFascicolo;

	public ProvvedimentoModelBDMC() {
		init();
	}

	private void init() {
		mSbPren = null;
		mSbViewProcpena = null;
		mSoggettiOmonimi = null;
		mSentenza = null;
		mSbViewReat = null;
		mSbViewCapoImpu = null;
		mSbPeriPren = null;

		mIsLuogoCheck = false;
		mIsSbViewReatCheck = false;
		mIsSbViewCapoImpuCheck = false;
		mIsSbPeriPrenCheck = false;
		mIsSbProcPenaCheck = false;
		mIsSbCircostanzeCheck = false;
		mEstendiFascicoloSiep = false;
		mIdSoggettoOmonimo = null;
		mSoggetto = null;
		mUtente = "";
		mUfficio = "";
		mDataInserimento = null;
		mDataArrivoAtto = null;
		mFascicolo = null;
	}

	/**
	 * Costruttore di copia
	 */
	public ProvvedimentoModelBDMC(ProvvedimentoModelBDMC aProvv) {
		init();

		if (aProvv.getSentenza() != null)
			mSentenza = new SentenzaModel(aProvv.getSentenza());
		if (aProvv.getSbPren() != null)
			mSbPren = new SbPrenModel(aProvv.getSbPren());
		if (aProvv.getSbViewProcpena() != null)
			mSbViewProcpena = new Vector(aProvv.getSbViewProcpena());
		if (aProvv.getSoggettiOmonimi() != null)
			mSoggettiOmonimi = new Vector(aProvv.getSoggettiOmonimi());
		if (aProvv.getSbViewReat() != null)
			mSbViewReat = new Vector(aProvv.getSbViewReat());
		if (aProvv.getSbPeriPren() != null)
			mSbPeriPren = new Vector(aProvv.getSbPeriPren());
		if (aProvv.getSbViewCapoImpu() != null)
			mSbViewCapoImpu = new Vector(aProvv.getSbViewCapoImpu());

		mIsLuogoCheck = aProvv.isLuogoCheck();
		mIsSbViewReatCheck = aProvv.isSbViewReatCheck();
		mIsSbViewCapoImpuCheck = aProvv.isSbViewCapoImpuCheck();
		mIsSbPeriPrenCheck = aProvv.isSbPeriPrenCheck();
		mIsSbProcPenaCheck = aProvv.isSbProcPenaCheck();
		mIsSbCircostanzeCheck = aProvv.isSbCircostanzeCheck();
		mEstendiFascicoloSiep = aProvv.isEstendiFascicoloSiep();
		mIdSoggettoOmonimo = aProvv.getIdSoggettoOmonimo();

		if (aProvv.getSoggetto() != null)
			mSoggetto = new SoggettoModel(aProvv.getSoggetto());

		mUtente = aProvv.getUtente();
		mDataInserimento = aProvv.getDataInserimento();
		mUfficio = aProvv.getUfficio();
		mDataArrivoAtto = aProvv.getDataArrivoAtto();
		if (aProvv.getFascicoloSiep() != null)
			mFascicolo = new FascicoloSiepModel(aProvv.getFascicoloSiep());
	}

	// Metodi GET
	public SbPrenModel getSbPren() {
		return mSbPren;
	}

	public Vector getSbViewReat() {
		return mSbViewReat;
	}

	public Vector getSoggettiOmonimi() {
		return mSoggettiOmonimi;
	}

	public SentenzaModel getSentenza() {
		return mSentenza;
	}

	public Vector getSbViewProcpena() {
		return mSbViewProcpena;
	}

	public Vector getSbViewCapoImpu() {
		return mSbViewCapoImpu;
	}

	public Vector getSbPeriPren() {
		return mSbPeriPren;
	}

	public boolean isLuogoCheck() {
		return mIsLuogoCheck;
	}

	public boolean isSbViewReatCheck() {
		return mIsSbViewReatCheck;
	}

	public boolean isSbViewCapoImpuCheck() {
		return mIsSbViewCapoImpuCheck;
	}

	public boolean isSbPeriPrenCheck() {
		return mIsSbPeriPrenCheck;
	}

	public boolean isSbProcPenaCheck() {
		return mIsSbProcPenaCheck;
	}

	public boolean isSbCircostanzeCheck() {
		return mIsSbCircostanzeCheck;
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

	public Date getDataArrivoAtto() {
		return mDataArrivoAtto;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicolo;
	}

	// Metodi SET
	public void setSbPren(SbPrenModel aValore) {
		mSbPren = aValore;
	}

	public void setSbViewReat(Vector aValore) {
		mSbViewReat = aValore;
	}

	public void setSoggettiOmonimi(Vector aValore) {
		mSoggettiOmonimi = aValore;
	}

	public void setSentenza(SentenzaModel aValore) {
		mSentenza = aValore;
	}

	public void setSbViewProcpena(Vector aValore) {
		mSbViewProcpena = aValore;
	}

	public void setSbViewCapoImpu(Vector aValore) {
		mSbViewCapoImpu = aValore;
	}

	public void setSbPeriPren(Vector aValore) {
		mSbPeriPren = aValore;
	}

	public void setIsLuogoCheck(boolean aValore) {
		mIsLuogoCheck = aValore;
	}

	public void setIsSbViewReatCheck(boolean aValore) {
		mIsSbViewReatCheck = aValore;
	}

	public void setIsSbViewCapoImpuCheck(boolean aValore) {
		mIsSbViewCapoImpuCheck = aValore;
	}

	public void setIsSbPeriPrenCheck(boolean aValore) {
		mIsSbPeriPrenCheck = aValore;
	}

	public void setIsSbProcPenaCheck(boolean aValore) {
		mIsSbProcPenaCheck = aValore;
	}

	public void setIsSbCircostanzeCheck(boolean aValore) {
		mIsSbCircostanzeCheck = aValore;
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

	public void setDataArrivoAtto(Date aValore) {
		mDataArrivoAtto = aValore;
	}

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicolo = aValore;
	}

}