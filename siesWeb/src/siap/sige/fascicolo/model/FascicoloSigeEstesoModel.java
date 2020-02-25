package siap.sige.fascicolo.model;

import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.richiesta.model.RichiestaSigeModel;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;

/**
 * <p>
 * Title: FascicoloSigeEstesoModel
 * </p>
 * <p>
 * Description: Questo Classe Model raggruppa in un unico aggregato tutti i dati collegati ad un Fascicolo
 * SIGE.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class FascicoloSigeEstesoModel extends GenericModel {

	private static final long serialVersionUID = 6274017420892175519L;

	private FascicoloSigeModel mFascicoloSige;
	private SoggettoModel mSoggetto;
	private RichiestaSigeModel mRichiestaSige;
	private FascicoloSiepModel mFascicoloSiep = null;
	private MagistratoAssegnatarioModel mMagAss = null;
	private FasSigeDetenzioneModel mDetenzione = null;
	private ResidenzaModel mResidenza = null;
	private ResidenzaModel mDomicilio = null;
	private UdienzaProcedimentoSigeModel mUdienzaProcedimento = null;
	private Date mDataInvioAttiInArchivio = null;
	private Date mDataCompilazioneFoglioComplementare = null;

	// MEV_65: aggiunte variabili per gestire nuova funzionalità
	private ImpugnazioneSigeModel mImpugnazioneSige = null;
	private ProvvedimentoSigeEventoModel mProvvedimentoSigeEvento = null;

	// COSTRUTTORE DI DEFAULT
	public FascicoloSigeEstesoModel() {
		mFascicoloSige = null;
		mSoggetto = null;
		mRichiestaSige = null;
		mFascicoloSiep = null;
		mMagAss = null;
		mDetenzione = null;
		mResidenza = null;
		mDomicilio = null;
		mUdienzaProcedimento = null;
		mDataInvioAttiInArchivio = null;
		mDataCompilazioneFoglioComplementare = null;
		// MEV_65
		mImpugnazioneSige = null;
		mProvvedimentoSigeEvento = null;
	}

	public FascicoloSigeEstesoModel(FascicoloSigeModel aFascicoloSige) {
		this();
		if (aFascicoloSige != null)
			mFascicoloSige = new FascicoloSigeModel(aFascicoloSige);
	}

	public FascicoloSigeEstesoModel(FascicoloSigeModel aFascicoloSige, SoggettoModel aSoggetto) {
		this(aFascicoloSige);
		if (aSoggetto != null)
			mSoggetto = new SoggettoModel(aSoggetto);
	}

	public FascicoloSigeEstesoModel(FascicoloSigeModel aFascicoloSige, RichiestaSigeModel aRichiestaSige) {
		this(aFascicoloSige);
		if (aRichiestaSige != null)
			mRichiestaSige = new RichiestaSigeModel(aRichiestaSige);
	}

	public FascicoloSigeEstesoModel(FascicoloSigeModel aFascicoloSige, SoggettoModel aSoggetto,
			RichiestaSigeModel aRichiestaSige) {
		this(aFascicoloSige, aSoggetto);
		if (aRichiestaSige != null)
			mRichiestaSige = new RichiestaSigeModel(aRichiestaSige);
	}

	public FascicoloSigeEstesoModel(FascicoloSigeModel aFascicoloSige, SoggettoModel aSoggetto,
			RichiestaSigeModel aRichiestaSige, FascicoloSiepModel aFascicoloSiep) {
		this(aFascicoloSige, aSoggetto, aRichiestaSige);
		if (aFascicoloSiep != null)
			mFascicoloSiep = new FascicoloSiepModel(aFascicoloSiep);
	}

	public FascicoloSigeEstesoModel(FascicoloSigeModel aFascicoloSige, SoggettoModel aSoggetto,
			RichiestaSigeModel aRichiestaSige, FascicoloSiepModel aFascicoloSiep,
			MagistratoAssegnatarioModel aMagAss) {
		this(aFascicoloSige, aSoggetto, aRichiestaSige, aFascicoloSiep);
		if (aMagAss != null)
			mMagAss = new MagistratoAssegnatarioModel(aMagAss);
	}

	public FascicoloSigeEstesoModel(FascicoloSigeModel aFascicoloSige, SoggettoModel aSoggetto,
			RichiestaSigeModel aRichiestaSige, FascicoloSiepModel aFascicoloSiep,
			MagistratoAssegnatarioModel aMagAss, FasSigeDetenzioneModel aDetenzione) {
		this(aFascicoloSige, aSoggetto, aRichiestaSige, aFascicoloSiep, aMagAss);
		if (aDetenzione != null)
			mDetenzione = new FasSigeDetenzioneModel(aDetenzione);
	}

	public FascicoloSigeEstesoModel(FascicoloSigeModel aFascicoloSige, SoggettoModel aSoggetto,
			RichiestaSigeModel aRichiestaSige, FascicoloSiepModel aFascicoloSiep,
			MagistratoAssegnatarioModel aMagAss, FasSigeDetenzioneModel aDetenzione,
			ResidenzaModel aResidenza) {
		this(aFascicoloSige, aSoggetto, aRichiestaSige, aFascicoloSiep, aMagAss, aDetenzione);
		if (aResidenza != null)
			mResidenza = new ResidenzaModel(aResidenza);
	}

	public FascicoloSigeEstesoModel(FascicoloSigeModel aFascicoloSige, SoggettoModel aSoggetto,
			RichiestaSigeModel aRichiestaSige, FascicoloSiepModel aFascicoloSiep,
			MagistratoAssegnatarioModel aMagAss, FasSigeDetenzioneModel aDetenzione,
			ResidenzaModel aResidenza, ResidenzaModel aDomicilio) {
		this(aFascicoloSige, aSoggetto, aRichiestaSige, aFascicoloSiep, aMagAss, aDetenzione, aResidenza);
		if (aDomicilio != null)
			mDomicilio = new ResidenzaModel(aDomicilio);
	}

	public FascicoloSigeEstesoModel(FascicoloSigeModel aFascicoloSige, SoggettoModel aSoggetto,
			RichiestaSigeModel aRichiestaSige, FascicoloSiepModel aFascicoloSiep,
			MagistratoAssegnatarioModel aMagAss, FasSigeDetenzioneModel aDetenzione,
			ResidenzaModel aResidenza, ResidenzaModel aDomicilio,
			UdienzaProcedimentoSigeModel aUdienzaProcedimento) {
		this(aFascicoloSige, aSoggetto, aRichiestaSige, aFascicoloSiep, aMagAss, aDetenzione, aResidenza,
				aDomicilio);
		if (aUdienzaProcedimento != null)
			mUdienzaProcedimento = new UdienzaProcedimentoSigeModel(aUdienzaProcedimento);
	}

	// MEV_65
	public FascicoloSigeEstesoModel(FascicoloSigeModel aFascicoloSige, SoggettoModel aSoggetto,
			RichiestaSigeModel aRichiestaSige, FascicoloSiepModel aFascicoloSiep,
			MagistratoAssegnatarioModel aMagAss, FasSigeDetenzioneModel aDetenzione,
			ResidenzaModel aResidenza, ResidenzaModel aDomicilio,
			UdienzaProcedimentoSigeModel aUdienzaProcedimento, ImpugnazioneSigeModel aImpugnazioneSige) {
		this(aFascicoloSige, aSoggetto, aRichiestaSige, aFascicoloSiep, aMagAss, aDetenzione, aResidenza,
				aDomicilio, aUdienzaProcedimento);
		if (aImpugnazioneSige != null)
			mImpugnazioneSige = new ImpugnazioneSigeModel(aImpugnazioneSige);
	}

	// MEV_65
	public FascicoloSigeEstesoModel(FascicoloSigeModel aFascicoloSige, SoggettoModel aSoggetto,
			RichiestaSigeModel aRichiestaSige, FascicoloSiepModel aFascicoloSiep,
			MagistratoAssegnatarioModel aMagAss, FasSigeDetenzioneModel aDetenzione,
			ResidenzaModel aResidenza, ResidenzaModel aDomicilio,
			UdienzaProcedimentoSigeModel aUdienzaProcedimento, ImpugnazioneSigeModel aImpugnazioneSige,
			ProvvedimentoSigeEventoModel aProvvedimentoSigeEvento) {
		this(aFascicoloSige, aSoggetto, aRichiestaSige, aFascicoloSiep, aMagAss, aDetenzione, aResidenza,
				aDomicilio, aUdienzaProcedimento, aImpugnazioneSige);
		if (aProvvedimentoSigeEvento != null)
			mProvvedimentoSigeEvento = new ProvvedimentoSigeEventoModel(aProvvedimentoSigeEvento);
	}

	// COSTRUTTORE DI COPIA
	public FascicoloSigeEstesoModel(FascicoloSigeEstesoModel aModel) {
		this(aModel.getFascicoloSige(), aModel.getSoggetto(), aModel.getRichiestaSige(),
				aModel.getFascicoloSiep(), aModel.getMagAssegnatario(), aModel.getDetenzione(),
				aModel.getResidenza(), aModel.getDomicilio(), aModel.getUdienzaProcedimento(),
				// MEV_65
				aModel.getImpugnazioneSige(), aModel.getProvvedimentoEventoSige());
	}

	// METODI GET()
	//
	public FascicoloSigeModel getFascicoloSige() {
		return mFascicoloSige;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public RichiestaSigeModel getRichiestaSige() {
		return mRichiestaSige;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	public MagistratoAssegnatarioModel getMagAssegnatario() {
		return mMagAss;
	}

	public FasSigeDetenzioneModel getDetenzione() {
		return mDetenzione;
	}

	public ResidenzaModel getResidenza() {
		return mResidenza;
	}

	public ResidenzaModel getDomicilio() {
		return mDomicilio;
	}

	public UdienzaProcedimentoSigeModel getUdienzaProcedimento() {
		return mUdienzaProcedimento;
	}

	public Date getDataInvioAttiInArchivio() {
		return mDataInvioAttiInArchivio;
	}

	public Date getDataCompilazioneFoglioComplementare() {
		return mDataCompilazioneFoglioComplementare;
	}

	// MEV_65
	public ImpugnazioneSigeModel getImpugnazioneSige() {
		return mImpugnazioneSige;
	}

	public ProvvedimentoSigeEventoModel getProvvedimentoEventoSige() {
		return mProvvedimentoSigeEvento;
	}

	// METODI SET()
	//
	public void setFascicoloSige(FascicoloSigeModel aValore) {
		mFascicoloSige = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setRichiestaSige(RichiestaSigeModel aValore) {
		mRichiestaSige = aValore;
	}

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}

	public void setMagAssegnatario(MagistratoAssegnatarioModel aValore) {
		mMagAss = aValore;
	}

	public void setDetenzione(FasSigeDetenzioneModel aValore) {
		mDetenzione = aValore;
	}

	public void setResidenza(ResidenzaModel aValore) {
		mResidenza = aValore;
	}

	public void setDomicilio(ResidenzaModel aValore) {
		mDomicilio = aValore;
	}

	public void setUdienzaProcedimento(UdienzaProcedimentoSigeModel aValore) {
		mUdienzaProcedimento = aValore;
	}

	public void setDataInvioAttiInArchivio(Date dataInvioAttiInArchivio) {
		mDataInvioAttiInArchivio = dataInvioAttiInArchivio;
	}

	public void setDataCompilazioneFoglioComplementare(Date dataCompilazioneFoglioComplementare) {
		mDataCompilazioneFoglioComplementare = dataCompilazioneFoglioComplementare;
	}

	// MEV_65
	public void setImpugnazioneSige(ImpugnazioneSigeModel aValore) {
		mImpugnazioneSige = aValore;
	}

	public void setProvvedimentoSigeEvento(ProvvedimentoSigeEventoModel aValore) {
		mProvvedimentoSigeEvento = aValore;
	}

	public String toString() {

		String lRet = getClass().getName() + "\n";
		if (mFascicoloSige != null)
			lRet += mFascicoloSige.toString() + "\n";
		if (mSoggetto != null)
			lRet += mSoggetto.toString() + "\n";
		if (mRichiestaSige != null)
			lRet += mRichiestaSige.toString() + "\n";
		if (mFascicoloSiep != null)
			lRet += mFascicoloSiep.toString() + "\n";
		if (mMagAss != null)
			lRet += mMagAss.toString() + "\n";
		if (mDetenzione != null)
			lRet += mDetenzione.toString() + "\n";
		if (mResidenza != null)
			lRet += mResidenza.toString() + "\n";
		if (mDomicilio != null)
			lRet += mDomicilio.toString() + "\n";
		if (mUdienzaProcedimento != null)
			lRet += mUdienzaProcedimento.toString() + "\n";
		// MEV_65
		if (mImpugnazioneSige != null)
			lRet += mImpugnazioneSige.toString() + "\n";
		if (mProvvedimentoSigeEvento != null)
			lRet += mProvvedimentoSigeEvento.toString() + "\n";

		return lRet;
	}

}