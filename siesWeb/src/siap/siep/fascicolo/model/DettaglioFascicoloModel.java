package siap.siep.fascicolo.model;

import java.math.BigDecimal;
import java.util.List;

import f3b.model.GenericModel;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.calcolopenadl92.model.CalcoloPenaDL92ModelDB;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penapresunta.model.PenaPresuntaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;

/**
 * <p>
 * Title: DettaglioFascicoloModel
 * </p>
 * <p>
 * Description: Model Aggregato del FascicoloSiep
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings("rawtypes")
public class DettaglioFascicoloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7620676521186334905L;

	private FascicoloSiepModel mFascicoloSiep;

	private ResidenzaModel mResidenza, mDomicilio;
	private PosizioneGiuridicaModel mPosizioneGiuridica;
	private LuogoDetenzioneModel mLuogoDetenzione;
	private AltraCausaModel mAltraCausa;
	private List mListAvvocati;
	private List mListReatiCircostanze;
	private PenaComplessivaSanzioneSostitutivaModel mPenComSanSos;
	private List mListPeneAccessorie;
	private List mListBenefici;
	private List mListMisureCautelari;
	private List mListCircostanze;
	private PenaResiduaModel mPenaResidua;
	private List mListEventi;
	private PenaPresuntaModel mPenaPres;
	private List mStatoProcedimento;
	private List mMisureSicurezza;
	private MisuraAlternativaModel mMisuraAlternativa;
	private CalendarModel mCalendario;
	private MagistratoCompetenteMagistratoModel mMagistratoCompetente;
	private BigDecimal mGiorniLibConcessa;
	private BigDecimal mGiorniLibNonConcessa;
	private DecretoOrdinanzaSiepModel mDecretoOrdinanzaSiep;
	private PosizioneMaterialeFascModel mPosizioneMateriale;
	private List mListAvvocatiSIEP; // STUB 01/04/2005
	private String mNoteFascicolo; // STUB 01/04/2005
	private CalcoloPenaModel mCalcoloPenaModel;
	private PenaCumuloModel mPenaCumulo;

	private DatiSiepPerTrasferimentoModel mDatiSiepPerTrasferimento; // STUB 10/12/2007
	private ScambioSanzioneModel mScambioSanzione;
	// 22/05/2014 - Nuova L.A
	private BigDecimal mGiorniLibNONConcessaLA;
	private BigDecimal mGiorniLibNONConcessaLS;
	private BigDecimal mGiorniLibNONConcessaLI;
	private BigDecimal mGiorniLibConcessaLA;
	private BigDecimal mGiorniLibConcessaLS;
	private BigDecimal mGiorniLibConcessaLI;

	// DL92 since 10/2014
	private BigDecimal mGiorniDL92NONDetratti;
	private BigDecimal mGiorniDL92Detratti;

	// MEV26 - Cumulo
	private PenaRideterminataCumuloModel mPenaCumuloNew;
	private List mReatoCumulo; // ReatoCumuloModel
	private List mReatoCircoCumulo; // ReatoCircostanzaCumuloModel
	private List mCircostanzaCumulo; // CircostanzaCumuloModel
	private String mFlagIstruttoriaPresente;
	
	
	// MEV-2026_1
	private CalcoloPenaDL92ModelDB mCalcoloPenaDL92DB;
	private List /*<CalcoloPenaDL92ModelDB>*/ mStoricoCalcoliPenaDL92DB;
	
	
    // COSTRUTTORE DI DEFAULT
	public DettaglioFascicoloModel() {
		mFascicoloSiep = null;
		mResidenza = null;
		mDomicilio = null;
		mPosizioneGiuridica = null;
		mListAvvocati = null;
		mListReatiCircostanze = null;
		mPenComSanSos = null;
		mListPeneAccessorie = null;
		mListBenefici = null;
		mListMisureCautelari = null;
		mListCircostanze = null;
		mPenaResidua = null;
		mLuogoDetenzione = null;
		mListEventi = null;
		mPenaPres = null;
		mStatoProcedimento = null;
		mMisureSicurezza = null;
		mAltraCausa = null;
		mMisuraAlternativa = null;
		mCalendario = null;
		mGiorniLibConcessa = null;
		mGiorniLibNonConcessa = null;
		mMagistratoCompetente = null;
		mPosizioneMateriale = null;
		mDecretoOrdinanzaSiep = null;
		mListAvvocatiSIEP = null; // STUB 01/04/2005
		mNoteFascicolo = null;
		mCalcoloPenaModel = null;
		mPenaCumulo = null;
		mDatiSiepPerTrasferimento = null; // STUB 10/12/2007
		mScambioSanzione = null;
		// 22/05/2014 - Nuova L.A
		mGiorniLibNONConcessaLA = null;
		mGiorniLibNONConcessaLS = null;
		mGiorniLibNONConcessaLI = null;
		mGiorniLibConcessaLA = null;
		mGiorniLibConcessaLS = null;
		mGiorniLibConcessaLI = null;

		mGiorniDL92NONDetratti = null;
		mGiorniDL92Detratti = null;

		// MEV 26 CUMULO
		mReatoCumulo = null;
		mReatoCircoCumulo = null;
		mCircostanzaCumulo = null;
		mFlagIstruttoriaPresente = null;
	}

	public DettaglioFascicoloModel(FascicoloSiepModel aFascicoloSiep, ResidenzaModel aResidenza,
			ResidenzaModel aDomicilio, PosizioneGiuridicaModel aPosizioneGiuridica, List aListAvvocati,
			List aListReatiCircostanze, PenaComplessivaSanzioneSostitutivaModel aPenComSanSos,
			List aListPeneAccessorie, List aListBenefici, List aListMisureCautelari, List aListCircostanze,
			PenaResiduaModel aPenaResidua, LuogoDetenzioneModel aLuogoDetenzione, List aListEventi,
			PenaPresuntaModel aPenaPres, List aStatoProcedimento, List aMisureSicurezza,
			AltraCausaModel aAltraCausa, MisuraAlternativaModel aMisuraAlternativa, CalendarModel aCalendario,
			BigDecimal aGiorniLibConcessa, BigDecimal aGiorniLibNonConcessa,
			MagistratoCompetenteMagistratoModel aMagistratoCompetente,
			DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep, List aListAvvocatiSIEP, String aNoteFascicolo,
			CalcoloPenaModel aCalcoloPenaModel, PenaCumuloModel aPenaCumulo,
			DatiSiepPerTrasferimentoModel aDatiSiepPerTrasferimento, // STUB 10/12/2007
			ScambioSanzioneModel aScambioSanzione, // )
			// 22/05/2014 - Nuova L.A
			BigDecimal aGiorniLibNONConcessaLA, BigDecimal aGiorniLibNONConcessaLS,
			BigDecimal aGiorniLibNONConcessaLI, BigDecimal aGiorniLibConcessaLA,
			BigDecimal aGiorniLibConcessaLS, BigDecimal aGiorniLibConcessaLI,
			// DL92
			BigDecimal aGiorniDL92NONDetratti, BigDecimal aGiorniDL92Detratti) {
		mFascicoloSiep = aFascicoloSiep;
		mResidenza = aResidenza;
		mDomicilio = aDomicilio;
		mPosizioneGiuridica = aPosizioneGiuridica;
		mListAvvocati = aListAvvocati;
		mListReatiCircostanze = aListReatiCircostanze;
		mPenComSanSos = aPenComSanSos;
		mListPeneAccessorie = aListPeneAccessorie;
		mListBenefici = aListBenefici;
		mListMisureCautelari = aListMisureCautelari;
		mListCircostanze = aListCircostanze;
		mPenaResidua = aPenaResidua;
		mLuogoDetenzione = aLuogoDetenzione;
		mListEventi = aListEventi;
		mPenaPres = aPenaPres;
		mStatoProcedimento = aStatoProcedimento;
		mMisureSicurezza = aMisureSicurezza;
		mAltraCausa = aAltraCausa;
		mMisuraAlternativa = aMisuraAlternativa;
		mCalendario = aCalendario;
		mGiorniLibConcessa = aGiorniLibConcessa;
		mGiorniLibNonConcessa = aGiorniLibNonConcessa;
		mMagistratoCompetente = aMagistratoCompetente;
		mDecretoOrdinanzaSiep = aDecretoOrdinanzaSiep;
		mListAvvocatiSIEP = aListAvvocatiSIEP; // STUB 01/04/2005
		mNoteFascicolo = aNoteFascicolo;
		mCalcoloPenaModel = aCalcoloPenaModel;
		mPenaCumulo = aPenaCumulo;
		mDatiSiepPerTrasferimento = aDatiSiepPerTrasferimento; // STUB 10/12/2007
		mScambioSanzione = aScambioSanzione;
		// 22/05/2014 - Nuova L.A
		mGiorniLibNONConcessaLA = aGiorniLibNONConcessaLA;
		mGiorniLibNONConcessaLS = aGiorniLibNONConcessaLS;
		mGiorniLibNONConcessaLI = aGiorniLibNONConcessaLI;
		mGiorniLibConcessaLA = aGiorniLibConcessaLA;
		mGiorniLibConcessaLS = aGiorniLibConcessaLS;
		mGiorniLibConcessaLI = aGiorniLibConcessaLI;
		// DL92
		mGiorniDL92NONDetratti = aGiorniDL92NONDetratti;
		mGiorniDL92Detratti = aGiorniDL92Detratti;
	}

	//
	// METODI GET()
	//
	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	public ResidenzaModel getResidenza() {
		return mResidenza;
	}

	public ResidenzaModel getDomicilio() {
		return mDomicilio;
	}

	public PosizioneGiuridicaModel getPosizioneGiuridica() {
		return mPosizioneGiuridica;
	}

	public List getAvvocati() {
		return mListAvvocati;
	}

	public List getReatiCircostanze() {
		return mListReatiCircostanze;
	}

	public PenaComplessivaSanzioneSostitutivaModel getPenaComplessivaSanzioneSostitutiva() {
		return mPenComSanSos;
	}

	public List getPeneAccessorie() {
		return mListPeneAccessorie;
	}

	public List getBenefici() {
		return mListBenefici;
	}

	public List getMisureCautelari() {
		return mListMisureCautelari;
	}

	public List getCircostanze() {
		return mListCircostanze;
	}

	public PenaResiduaModel getPenaResidua() {
		return mPenaResidua;
	}

	public LuogoDetenzioneModel getLuogoDetenzione() {
		return mLuogoDetenzione;
	}

	public List getEventi() {
		return mListEventi;
	}

	public PenaPresuntaModel getPenaPresunta() {
		return mPenaPres;
	}

	public List getStatoProcedimento() {
		return mStatoProcedimento;
	}

	public List getMisureSicurezza() {
		return mMisureSicurezza;
	}

	public AltraCausaModel getAltraCausa() {
		return mAltraCausa;
	}

	public MisuraAlternativaModel getMisuraAlternativa() {
		return mMisuraAlternativa;
	}

	public CalendarModel getCalendario() {
		return mCalendario;
	}

	public MagistratoCompetenteMagistratoModel getMagistratoCompetente() {
		return mMagistratoCompetente;
	}

	public BigDecimal getGiorniLibConcessa() {
		return mGiorniLibConcessa;
	}

	public BigDecimal getGiorniLibNonConcessa() {
		return mGiorniLibNonConcessa;
	}

	public DecretoOrdinanzaSiepModel getDecretoOrdinanzaSiep() {
		return mDecretoOrdinanzaSiep;
	}

	public List getAvvocatiSIEP() {
		return mListAvvocatiSIEP;
	} // STUB 01/04/2005

	public PosizioneMaterialeFascModel getPosizioneMateriale() {
		return mPosizioneMateriale;
	}

	public String getNoteFascicolo() {
		return mNoteFascicolo;
	}

	public CalcoloPenaModel getCalcoloPenaModel() {
		return mCalcoloPenaModel;
	}

	public PenaCumuloModel getPenaCumulo() {
		return mPenaCumulo;
	}

	public DatiSiepPerTrasferimentoModel getDatiSiepPerTrasferimento() {
		return mDatiSiepPerTrasferimento;
	} // STUB 10/12/2007

	public ScambioSanzioneModel getScambioSanzione() {
		return mScambioSanzione;
	} // STUB 10/12/2007
	// 22/05/2014 - Nuova L.A

	public BigDecimal getGiorniLibNONConcessaLA() {
		return mGiorniLibNONConcessaLA;
	}

	public BigDecimal getGiorniLibNONConcessaLS() {
		return mGiorniLibNONConcessaLS;
	}

	public BigDecimal getGiorniLibNONConcessaLI() {
		return mGiorniLibNONConcessaLI;
	}

	public BigDecimal getGiorniLibConcessaLA() {
		return mGiorniLibConcessaLA;
	}

	public BigDecimal getGiorniLibConcessaLS() {
		return mGiorniLibConcessaLS;
	}

	public BigDecimal getGiorniLibConcessaLI() {
		return mGiorniLibConcessaLI;
	}

	// DL92
	public BigDecimal getGiorniDL92NONDetratti() {
		return mGiorniDL92NONDetratti;
	}

	public BigDecimal getGiorniDL92Detratti() {
		return mGiorniDL92Detratti;
	}

	// MEV26 - Cumulo
	public PenaRideterminataCumuloModel getPenaCumuloNew() {
		return mPenaCumuloNew;
	}

	public List getReatoCumulo() {
		return mReatoCumulo;
	}

	public List getReatoCircoCumulo() {
		return mReatoCircoCumulo;
	}

	public List getCircostanzaCumulo() {
		return mCircostanzaCumulo;
	}

	public String getFlagIstruttoriaPresente() {
		return mFlagIstruttoriaPresente;
	}

    // MEV-2026_1
    public CalcoloPenaDL92ModelDB getCalcoloPenaDL92DB() {
        return mCalcoloPenaDL92DB;
    }
    
    public List getStoricoCalcoliPenaDL92DB() {
        return mStoricoCalcoliPenaDL92DB;
    }
    // MEV-2026_1 - FINE
	
	//
	// METODI SET()
	//
	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}

	public void setResidenza(ResidenzaModel aValore) {
		mResidenza = aValore;
	}

	public void setDomicilio(ResidenzaModel aValore) {
		mDomicilio = aValore;
	}

	public void setPosizioneGiuridica(PosizioneGiuridicaModel aValore) {
		mPosizioneGiuridica = aValore;
	}

	public void setAvvocati(List aValore) {
		mListAvvocati = aValore;
	}

	public void setReatiCircostanze(List aValore) {
		mListReatiCircostanze = aValore;
	}

	public void setPenaComplessivaSanzioneSostitutiva(PenaComplessivaSanzioneSostitutivaModel aValore) {
		mPenComSanSos = aValore;
	}

	public void setPeneAccessorie(List aValore) {
		mListPeneAccessorie = aValore;
	}

	public void setBenefici(List aValore) {
		mListBenefici = aValore;
	}

	public void setMisureCautelari(List aValore) {
		mListMisureCautelari = aValore;
	}

	public void setCircostanze(List aValore) {
		mListCircostanze = aValore;
	}

	public void setPenaResidua(PenaResiduaModel aValore) {
		mPenaResidua = aValore;
	}

	public void setLuogoDetenzione(LuogoDetenzioneModel aValore) {
		mLuogoDetenzione = aValore;
	}

	public void setEventi(List aValore) {
		mListEventi = aValore;
	}

	public void setPenaPresunta(PenaPresuntaModel aValore) {
		mPenaPres = aValore;
	}

	public void setStatoProcedimento(List aValore) {
		mStatoProcedimento = aValore;
	}

	public void setMisureSicurezza(List aValore) {
		mMisureSicurezza = aValore;
	}

	public void setAltraCausa(AltraCausaModel aValore) {
		mAltraCausa = aValore;
	}

	public void setMisuraAlternativa(MisuraAlternativaModel aValore) {
		mMisuraAlternativa = aValore;
	}

	public void setCalendario(CalendarModel aValore) {
		mCalendario = aValore;
	}

	public void setMagistratoCompetente(MagistratoCompetenteMagistratoModel aValore) {
		mMagistratoCompetente = aValore;
	}

	public void setGiorniLibConcessa(BigDecimal aValore) {
		mGiorniLibConcessa = aValore;
	}

	public void setGiorniLibNonConcessa(BigDecimal aValore) {
		mGiorniLibNonConcessa = aValore;
	}

	public void setDecretoOrdinanzaSiep(DecretoOrdinanzaSiepModel aValore) {
		mDecretoOrdinanzaSiep = aValore;
	}

	public void setAvvocatiSIEP(List aValore) {
		mListAvvocatiSIEP = aValore;
	} // STUB 01/04/2005

	public void setPosizioneMateriale(PosizioneMaterialeFascModel aValore) {
		mPosizioneMateriale = aValore;
	}

	public void setNoteFascicolo(String aValore) {
		mNoteFascicolo = aValore;
	}

	public void setCalcoloPenaModel(CalcoloPenaModel aValore) {
		mCalcoloPenaModel = aValore;
	}

	public void setPenaCumulo(PenaCumuloModel aValore) {
		mPenaCumulo = aValore;
	}

	public void setDatiSiepPerTrasferimento(DatiSiepPerTrasferimentoModel aValore) {
		mDatiSiepPerTrasferimento = aValore;
	} // STUB 10/12/2007

	public void setScambioSanzione(ScambioSanzioneModel aValore) {
		mScambioSanzione = aValore;
	}

	// 22/05/2014 - Nuova L.A
	public void setGiorniLibNONConcessaLA(BigDecimal aValore) {
		mGiorniLibNONConcessaLA = aValore;
	}

	public void setGiorniLibNONConcessaLS(BigDecimal aValore) {
		mGiorniLibNONConcessaLS = aValore;
	}

	public void setGiorniLibNONConcessaLI(BigDecimal aValore) {
		mGiorniLibNONConcessaLI = aValore;
	}

	public void setGiorniLibConcessaLA(BigDecimal aValore) {
		mGiorniLibConcessaLA = aValore;
	}

	public void setGiorniLibConcessaLS(BigDecimal aValore) {
		mGiorniLibConcessaLS = aValore;
	}

	public void setGiorniLibConcessaLI(BigDecimal aValore) {
		mGiorniLibConcessaLI = aValore;
	}

	// DL92
	public void setGiorniDL92NONDetratti(BigDecimal aValore) {
		mGiorniDL92NONDetratti = aValore;
	}

	public void setGiorniDL92Detratti(BigDecimal aValore) {
		mGiorniDL92Detratti = aValore;
	}

	// MEV26 - Cumulo
	public void setPenaCumuloNew(PenaRideterminataCumuloModel aValore) {
		mPenaCumuloNew = aValore;
	}

	public void setReatoCumulo(List aValore) {
		mReatoCumulo = aValore;
	}

	public void setReatoCircoCumulo(List aValore) {
		mReatoCircoCumulo = aValore;
	}

	public void setCircostanzaCumulo(List aValore) {
		mCircostanzaCumulo = aValore;
	}

	public void setFlagIstruttoriaPresente(String aValore) {
		mFlagIstruttoriaPresente = aValore;
	}

    // MEV-2026_1
    public void setCalcoloPenaDL92DB(CalcoloPenaDL92ModelDB aValore) {
        mCalcoloPenaDL92DB = aValore;
    }
    public void setStoricoCalcoliPenaDL92DB (List aValore) {
        mStoricoCalcoliPenaDL92DB = aValore;
    }   
    // MEV-2026_1 - FINE   

    
    public String toString() {
        String lStr = "DettaglioFascicoloModel: \n";
        
        if ( mFascicoloSiep!=null) lStr+=" mFascicoloSiep presente \n";
        if ( mResidenza!=null) lStr+=" mResidenza presente \n";
        if ( mPosizioneGiuridica!=null) lStr+=" mPosizioneGiuridica presente \n";
        if ( mListAvvocati!=null) lStr+=" mListAvvocati presente \n";
        if ( mListCircostanze!=null) lStr+=" mListCircostanze presente \n";
        if ( mPenaResidua!=null) lStr+=" mPenaResidua presente \n";
        if ( mListEventi!=null) lStr+=" mListEventi presente \n";
        if ( mStatoProcedimento!=null) lStr+=" mStatoProcedimento presente \n";
        if ( mMagistratoCompetente!=null) lStr+=" mMagistratoCompetente presente \n";  
        if ( mDatiSiepPerTrasferimento!=null) lStr+=" mDatiSiepPerTrasferimento presente \n"; 
        if ( mCalcoloPenaDL92DB!=null) lStr+=" mCalcoloPenaDL92DB presente \n"; 
        if ( mStoricoCalcoliPenaDL92DB!=null) lStr+=" mStoricoCalcoliPenaDL92DB presente \n"; 
        
        return lStr;
    }
}
