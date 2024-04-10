package siap.siep.notifica.model;

/**
* <p>Title: NotificaModel</p>
* <p>Description: Classe Model che rappresenta il Notifica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.model.GenericModel;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.curatore.model.CuratoreSiusModel;

public class NotificaModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -2114598667529276025L;
	private BigDecimal mIdNotifica;
	private String mCodTipoNotifica;
	private String mCodUffUepeUssmSS;
	private String mCodUffUdsUdsm;
	private String mCodUffTdsTdsm;
	private String mDescrTipoNotifica;
	private Date mDataAvvenutaNotifica;
	private Date mDataInvio;
	private String mCodEsito;
	private String mDescrEsito;
	private String mNote;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodiceOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mEveIdEvento;
	private BigDecimal mAutEstIdAutoritaEsterna;
	private BigDecimal mSogIdSoggetto;
	private BigDecimal mAvvIdAvvocatoFascicoloSiep;
	private BigDecimal mAvvIdAvvocatoFascicoloSius;
	private BigDecimal mAvvIdAvvocatoFascicoloSige;
	private String mUffCodUfficio;
	private BigDecimal mSollecito;
	private BigDecimal mCssIdCssa;
	private BigDecimal mAutEstIdAutoritaEstDeleg;
	
	// MEV_2023-13 
	private BigDecimal mIdCivilmenteObbligato;	
	private CivilmenteObbligatoModel mCivilmenteObbligato; 
	// MEV_2023-13 - FINE

	// MEV_2023-33
	private Vector <RinnovoModel> mListaRinnovi;
	// MEV_2023-33 - FINE
	
    private AutoritaEsternaModel mAutoritaEsternaDelegata;

	// PM - 20030609
	private String mDescrUfficioDestinatario;
	private AutoritaEsternaModel mAutoritaEsterna;
	private UfficioModel mUfficio;
	private CSSAModel mCSSA;
	// LE - 20030903
	private AvvocatoSiepModel mAvvSiep;
	private AvvocatoSiusModel mAvvSius;
	private AvvocatoSigeModel mAvvSige;
	// LE 20031007
	private String mDescrizione;
	private String mFlagDocRegistrato;
	// modifica relativa al tipo istituto
	private String mIstDetIdIstitutoDetenzione;
	private IstitutoDetenzioneModel mIstitutoDetenzione;
	private RinnovoModel mRinnovo;
	private BigDecimal mCurIdCuratore;
	private CuratoreSiusModel mCurSius;

	private BigDecimal mFlagNotificaViaFax;
	private BigDecimal mIdParteUdienza;
	private AvvocatoModel mAvvParteUdienza;
	// indica se la parte (civile/offesa) è domiciliata presso il difensore
	private String mFlagDomicilioDifensore;
	private AnagraficaPartiUdienzaModel mPartiUdienza;

	private AvvocatoModel mAvvocato = null;

	// COSTRUTTORE DI DEFAULT
	public NotificaModel() {
		this.mIdNotifica = null;
		this.mCodTipoNotifica = "";
		this.mCodUffUepeUssmSS = "";
		this.mCodUffUdsUdsm = "";
		this.mCodUffTdsTdsm = "";
		this.mDescrTipoNotifica = "";
		this.mDataAvvenutaNotifica = null;
		this.mDataInvio = null;
		this.mCodEsito = "";
		this.mDescrEsito = "";
		this.mNote = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodiceOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mEveIdEvento = null;
		this.mAutEstIdAutoritaEsterna = null;
		this.mSogIdSoggetto = null;
		this.mAvvIdAvvocatoFascicoloSiep = null;
		this.mAvvIdAvvocatoFascicoloSius = null;
		this.mAvvIdAvvocatoFascicoloSige = null;
		this.mSollecito = null;
		this.mCssIdCssa = null;
		this.mDescrUfficioDestinatario = "";
		this.mAutoritaEsterna = null;
		this.mUfficio = null;
		this.mCSSA = null;
		this.mAvvSiep = null;
		this.mAvvSius = null;
		this.mAvvSige = null;
		this.mDescrizione = "";
		this.mFlagDocRegistrato = null;
		this.mIstDetIdIstitutoDetenzione = null;
		this.mIstitutoDetenzione = null;
		this.mAutEstIdAutoritaEstDeleg = null;
		this.mAutoritaEsternaDelegata = null;
		this.mRinnovo = null;
		this.mCurIdCuratore = null;
		this.mCurSius = null;
		this.mFlagNotificaViaFax = null;
		this.mIdParteUdienza = null;
		this.mAvvParteUdienza = null;
		this.mFlagDomicilioDifensore = null;
		this.mPartiUdienza = null;
		this.mIdCivilmenteObbligato = null; //MEV_2023-13 
		this.mCivilmenteObbligato = null; //MEV_2023-13 
	}

	// COSTRUTTORE DI COPIA
	public NotificaModel(NotificaModel aModel) {
		this.mIdNotifica = aModel.mIdNotifica;
		this.mCodTipoNotifica = aModel.mCodTipoNotifica;
		this.mCodUffUepeUssmSS = aModel.mCodUffUepeUssmSS;
		this.mCodUffUdsUdsm = aModel.mCodUffUdsUdsm;
		this.mCodUffTdsTdsm = aModel.mCodUffTdsTdsm;
		this.mDescrTipoNotifica = aModel.mDescrTipoNotifica;
		this.mDataAvvenutaNotifica = aModel.mDataAvvenutaNotifica;
		this.mDataInvio = aModel.mDataInvio;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mNote = aModel.mNote;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodiceOperatoreAggiornamento = aModel.mCodiceOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mAutEstIdAutoritaEsterna = aModel.mAutEstIdAutoritaEsterna;
		this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		this.mAvvIdAvvocatoFascicoloSiep = aModel.mAvvIdAvvocatoFascicoloSiep;
		this.mAvvIdAvvocatoFascicoloSius = aModel.mAvvIdAvvocatoFascicoloSius;
		this.mAvvIdAvvocatoFascicoloSige = aModel.mAvvIdAvvocatoFascicoloSige;
		this.mSollecito = aModel.mSollecito;
		this.mCssIdCssa = aModel.mCssIdCssa;
		this.mUffCodUfficio = aModel.mUffCodUfficio;
		// PM - 20030609
		this.mDescrUfficioDestinatario = aModel.mDescrUfficioDestinatario;
		this.mAutoritaEsterna = aModel.mAutoritaEsterna;
		this.mUfficio = aModel.mUfficio;
		this.mCSSA = aModel.mCSSA;
		// LE
		this.mAvvSiep = aModel.mAvvSiep;
		this.mAvvSius = aModel.mAvvSius;
		this.mAvvSige = aModel.mAvvSige;
		this.mDescrizione = aModel.mDescrizione;
		this.mFlagDocRegistrato = aModel.mFlagDocRegistrato;
		// modifica relativa al tipo istituto
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mIstitutoDetenzione = aModel.mIstitutoDetenzione;
		this.mAutEstIdAutoritaEstDeleg = aModel.mAutEstIdAutoritaEstDeleg;
		this.mAutoritaEsternaDelegata = aModel.mAutoritaEsternaDelegata;
		this.mRinnovo = aModel.mRinnovo;
		this.mCurIdCuratore = aModel.mCurIdCuratore;
		this.mCurSius = aModel.mCurSius;
		this.mFlagNotificaViaFax = aModel.getFlagNotificaViaFax();
		this.mIdParteUdienza = aModel.mIdParteUdienza;
		this.mAvvParteUdienza = aModel.mAvvParteUdienza;
		this.mFlagDomicilioDifensore = aModel.mFlagDomicilioDifensore;
		this.mPartiUdienza = aModel.mPartiUdienza;
		
		this.mIdCivilmenteObbligato = aModel.mIdCivilmenteObbligato; //MEV_2023-13 
		this.mCivilmenteObbligato = aModel.mCivilmenteObbligato; //MEV_2023-13 
	}

	// COSTRUTTORE MODEL
	public NotificaModel(BigDecimal aIdNotifica, String aCodTipoNotifica, String aCodUffUepeUssmSS,
			String aCodUffUdsUdsm, String aCodUffTdsTdsm, String aDescrTipoNotifica,
			Date aDataAvvenutaNotifica, Date aDataInvio, String aCodEsito, String aDescrEsito, String aNote,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodiceOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aEveIdEvento,
			BigDecimal aAutEstIdAutoritaEsterna, BigDecimal aSogIdSoggetto,
			BigDecimal aAvvIdAvvocatoFascicoloSiep, BigDecimal aAvvIdAvvocatoFascicoloSius,
			BigDecimal aAvvIdAvvocatoFascicoloSige, String aUffCodUfficio, BigDecimal aSollecito,
			BigDecimal aCssIdCssa, AutoritaEsternaModel aAutorita,
			// modifica relativa al tipo istituto
			String aIstDetIdIstitutoDetenzione, IstitutoDetenzioneModel aIstitutoDetenzione,
			BigDecimal aCurIdCuratore, BigDecimal aFlagNotificaViaFax, BigDecimal aIdParteUdienza
			, BigDecimal aIdCivilmenteObbligato  //MEV_2023-13 
	)

	{
		this.mIdNotifica = aIdNotifica;
		this.mCodTipoNotifica = aCodTipoNotifica;
		this.mCodUffUepeUssmSS = aCodUffUepeUssmSS;
		this.mCodUffUdsUdsm = aCodUffUdsUdsm;
		this.mCodUffTdsTdsm = aCodUffTdsTdsm;
		this.mDescrTipoNotifica = aDescrTipoNotifica;
		this.mDataAvvenutaNotifica = aDataAvvenutaNotifica;
		this.mDataInvio = aDataInvio;
		this.mCodEsito = aCodEsito;
		this.mDescrEsito = aDescrEsito;
		this.mNote = aNote;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodiceOperatoreAggiornamento = aCodiceOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mEveIdEvento = aEveIdEvento;
		this.mAutEstIdAutoritaEsterna = aAutEstIdAutoritaEsterna;
		this.mSogIdSoggetto = aSogIdSoggetto;
		this.mAvvIdAvvocatoFascicoloSiep = aAvvIdAvvocatoFascicoloSiep;
		this.mAvvIdAvvocatoFascicoloSius = aAvvIdAvvocatoFascicoloSius;
		this.mAvvIdAvvocatoFascicoloSige = aAvvIdAvvocatoFascicoloSige;
		this.mUffCodUfficio = aUffCodUfficio;
		this.mSollecito = aSollecito;
		this.mCssIdCssa = aCssIdCssa;
		this.mAutoritaEsterna = aAutorita;
		this.mDescrUfficioDestinatario = "";
		this.mUfficio = null;
		this.mCSSA = null;
		this.mAvvSiep = null;
		this.mAvvSius = null;
		this.mAvvSige = null;
		this.mDescrizione = "";
		this.mFlagDocRegistrato = null;
		// modifica relativa al tipo istituto
		this.mIstDetIdIstitutoDetenzione = "";
		this.mIstitutoDetenzione = aIstitutoDetenzione;
		this.mRinnovo = null;
		this.mCurIdCuratore = null;
		this.mCurSius = null;
		this.mFlagNotificaViaFax = aFlagNotificaViaFax;
		this.mIdParteUdienza = aIdParteUdienza;
		this.mAvvParteUdienza = null;
		
		this.mIdCivilmenteObbligato = aIdCivilmenteObbligato; //MEV_2023-13 
		this.mCivilmenteObbligato = null; //MEV_2023-13 
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdNotifica() {
		return mIdNotifica;
	}

	public String getCodTipoNotifica() {
		return mCodTipoNotifica;
	}

	public String getCodUffUepeUssmSS() {
		return mCodUffUepeUssmSS;
	}

	public String getCodUffUdsUdsm() {
		return mCodUffUdsUdsm;
	}

	public String getCodUffTdsTdsm() {
		return mCodUffTdsTdsm;
	}

	public String getDescrTipoNotifica() {
		return mDescrTipoNotifica;
	}

	public Date getDataAvvenutaNotifica() {
		return mDataAvvenutaNotifica;
	}

	public Date getDataInvio() {
		return mDataInvio;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public String getNote() {
		return mNote;
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

	public String getCodiceOperatoreAggiornamento() {
		return mCodiceOperatoreAggiornamento;
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

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getAutEstIdAutoritaEsterna() {
		return mAutEstIdAutoritaEsterna;
	}

	public BigDecimal getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public BigDecimal getAvvIdAvvocatoFascicoloSiep() {
		return mAvvIdAvvocatoFascicoloSiep;
	}

	public BigDecimal getAvvIdAvvocatoFascicoloSius() {
		return mAvvIdAvvocatoFascicoloSius;
	}

	public BigDecimal getAvvIdAvvocatoFascicoloSige() {
		return mAvvIdAvvocatoFascicoloSige;
	}

	public String getUffCodUfficio() {
		return mUffCodUfficio;
	}

	public AutoritaEsternaModel getAutoritaEsterna() {
		return mAutoritaEsterna;
	}

	public BigDecimal getSollecito() {
		return mSollecito;
	}

	public BigDecimal getCssIdCssa() {
		return mCssIdCssa;
	}

	// PM - 20030609
	public String getDescrUfficioDestinatario() {
		return mDescrUfficioDestinatario;
	}

	public UfficioModel getUfficio() {
		return mUfficio;
	}

	public CSSAModel getCSSA() {
		return mCSSA;
	}

	// LE
	public AvvocatoSiepModel getAvvSiep() {
		return this.mAvvSiep;
	}

	public AvvocatoSiusModel getAvvSius() {
		return this.mAvvSius;
	}

	public AvvocatoSigeModel getAvvSige() {
		return this.mAvvSige;
	}

	public String getDescrizione() {
		return this.mDescrizione;
	}

	public String getFlagDocRegistrato() {
		return mFlagDocRegistrato;
	}

	// modifica relativa al tipo istituto
	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public IstitutoDetenzioneModel getIstitutoDetenzione() {
		return mIstitutoDetenzione;
	}

	public BigDecimal getAutEstIdAutoritaEstDeleg() {
		return mAutEstIdAutoritaEstDeleg;
	}

	public AutoritaEsternaModel getAutoritaEsternaDelegata() {
		return mAutoritaEsternaDelegata;
	}

	public RinnovoModel getRinnovo() {
		return mRinnovo;
	}

	public BigDecimal getCurIdCuratore() {
		return mCurIdCuratore;
	}

	public CuratoreSiusModel getCurSius() {
		return this.mCurSius;
	}

	public BigDecimal getFlagNotificaViaFax() {
		return mFlagNotificaViaFax;
	}

	public BigDecimal getIdParteUdienza() {
		return mIdParteUdienza;
	}

	public AvvocatoModel getAvvParteUdienza() {
		return mAvvParteUdienza;
	}

	public String getFlagDomicilioDifensore() {
		return mFlagDomicilioDifensore;
	}

	public AnagraficaPartiUdienzaModel getPartiUdienza() {
		return mPartiUdienza;
	}
	
	// MEV_2023-13 
    public BigDecimal getIdCivilmenteObbligato() {
        return mIdCivilmenteObbligato;
    }
    public CivilmenteObbligatoModel getCivilmenteObbligato() {
        return mCivilmenteObbligato;
    }
    // MEV_2023-13 - FINE

    // MEV_2023-33 
    public Vector <RinnovoModel> getListaRinnovi () {
    	return mListaRinnovi;
    }
    // MEV_2023-33 - FINE
    
	//
	// METODI SET()
	//
	public void setIdNotifica(BigDecimal aValore) {
		mIdNotifica = aValore;
	}

	public void setCodTipoNotifica(String aValore) {
		mCodTipoNotifica = aValore;
	}

	public void setCodUffUepeUssmSS(String aValore) {
		mCodUffUepeUssmSS = aValore;
	}

	public void setCodUffUdsUdsm(String aValore) {
		mCodUffUdsUdsm = aValore;
	}

	public void setCodUffTdsTdsm(String aValore) {
		mCodUffTdsTdsm = aValore;
	}

	public void setDescrTipoNotifica(String aValore) {
		mDescrTipoNotifica = aValore;
	}

	public void setDataAvvenutaNotifica(Date aValore) {
		mDataAvvenutaNotifica = aValore;
	}

	public void setDataInvio(Date aValore) {
		mDataInvio = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
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

	public void setCodiceOperatoreAggiornamento(String aValore) {
		mCodiceOperatoreAggiornamento = aValore;
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

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setAutEstIdAutoritaEsterna(BigDecimal aValore) {
		mAutEstIdAutoritaEsterna = aValore;
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setAvvIdAvvocatoFascicoloSiep(BigDecimal aValore) {
		mAvvIdAvvocatoFascicoloSiep = aValore;
	}

	public void setAvvIdAvvocatoFascicoloSius(BigDecimal aValore) {
		mAvvIdAvvocatoFascicoloSius = aValore;
	}

	public void setAvvIdAvvocatoFascicoloSige(BigDecimal aValore) {
		mAvvIdAvvocatoFascicoloSige = aValore;
	}

	public void setUffCodUfficio(String aValore) {
		mUffCodUfficio = aValore;
	}

	public void setSollecito(BigDecimal aValore) {
		mSollecito = aValore;
	}

	public void setCssIdCssa(BigDecimal aValore) {
		mCssIdCssa = aValore;
	}

	// PM
	public void setDescrUfficioDestinatario(String aValore) {
		mDescrUfficioDestinatario = aValore;
	}

	public void setAutoritaEsterna(AutoritaEsternaModel aValore) {
		mAutoritaEsterna = aValore;
	}

	public void setUfficio(UfficioModel aValore) {
		mUfficio = aValore;
	}

	public void setCSSA(CSSAModel aValore) {
		mCSSA = aValore;
	}

	// LE
	public void setAvvSiep(AvvocatoSiepModel aValore) {
		this.mAvvSiep = aValore;
	}

	public void setAvvSius(AvvocatoSiusModel aValore) {
		this.mAvvSius = aValore;
	}

	public void setAvvSige(AvvocatoSigeModel aValore) {
		this.mAvvSige = aValore;
	}

	public void setDescrizione(String aValore) {
		this.mDescrizione = aValore;
	}

	public void setFlagDocRegistrato(String aValore) {
		this.mFlagDocRegistrato = aValore;
	}

	// modifica relativa al tipo istituto
	public void setIstDetIdIstitutoDetenzione(String aValore) {
		this.mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setIstitutoDetenzione(IstitutoDetenzioneModel aValore) {
		this.mIstitutoDetenzione = aValore;
	}

	public void setAutEstIdAutoritaEstDeleg(BigDecimal aValore) {
		this.mAutEstIdAutoritaEstDeleg = aValore;
	}

	public void setAutoritaEsternaDelegata(AutoritaEsternaModel aValore) {
		this.mAutoritaEsternaDelegata = aValore;
	}

	public void setRinnovo(RinnovoModel aValore) {
		this.mRinnovo = aValore;
	}

	public void setCurIdCuratore(BigDecimal aValore) {
		mCurIdCuratore = aValore;
	}

	public void setCurSius(CuratoreSiusModel aValore) {
		this.mCurSius = aValore;
	}

	public void setFlagNotificaViaFax(BigDecimal mFlagNotificaViaFax) {
		this.mFlagNotificaViaFax = mFlagNotificaViaFax;
	}

	public void setIdParteUdienza(BigDecimal aValore) {
		mIdParteUdienza = aValore;
	}

	public void setAvvParteUdienza(AvvocatoModel aValore) {
		mAvvParteUdienza = aValore;
	}

	public void setFlagDomicilioDifensore(String aValore) {
		this.mFlagDomicilioDifensore = aValore;
	}

	public void setPartiUdienza(AnagraficaPartiUdienzaModel aValore) {
		this.mPartiUdienza = aValore;
	}

	// MEV_2023-13
    public void setIdCivilmenteObbligato (BigDecimal aValore) {
        this.mIdCivilmenteObbligato = aValore;
    }
    public void setCivilmenteObbligato(CivilmenteObbligatoModel mCivilmenteObbligato) {
        this.mCivilmenteObbligato = mCivilmenteObbligato;
    }
    // MEV_2023-13 - FINE
    
    // MEV_2023-33 
    public void setListaRinnovi (Vector <RinnovoModel> aValore) {
    	this.mListaRinnovi = aValore;
    }
    // MEV_2023-33 - FINE
    
	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "NotificaModel:\n" + "[ mIdNotifica                   = " + mIdNotifica + " ]\n"
				+ "[ mCodTipoNotifica              = " + mCodTipoNotifica + " ]\n"
				+ "[ mCodUffUepeUssmSS             = " + mCodUffUepeUssmSS + " ]\n"
				+ "[ mCodUffUdsUdsm                = " + mCodUffUdsUdsm + " ]\n"
				+ "[ mCodUffTdsTdsm                = " + mCodUffTdsTdsm + " ]\n"
				+ "[ mDescrTipoNotifica            = " + mDescrTipoNotifica + " ]\n"
				+ "[ mDataAvvenutaNotifica         = " + mDataAvvenutaNotifica + " ]\n"
				+ "[ mDataInvio                    = " + mDataInvio + " ]\n"
				+ "[ mCodEsito                     = " + mCodEsito + " ]\n"
				+ "[ mDescrEsito                   = " + mDescrEsito + " ]\n"
				+ "[ mNote                         = " + mNote + " ]\n" + "[ mCodOperatoreInserimento      = "
				+ mCodOperatoreInserimento + " ]\n" + "[ mDataInserimento              = " + mDataInserimento
				+ " ]\n" + "[ mCodUfficioInserimento        = " + mCodUfficioInserimento + " ]\n"
				+ "[ mDescrUfficioInserimento      = " + mDescrUfficioInserimento + " ]\n"
				+ "[ mCodiceOperatoreAggiornamento = " + mCodiceOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento            = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento      = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mDescrUfficioAggiornamento    = " + mDescrUfficioAggiornamento + " ]\n"
				+ "[ mEveIdEvento                  = " + mEveIdEvento + " ]\n"
				+ "[ mAutEstIdAutoritaEsterna      = " + mAutEstIdAutoritaEsterna + " ]\n"
				+ "[ mSogIdSoggetto                = " + mSogIdSoggetto + " ]\n"
				+ "[ mAvvIdAvvocatoFascicoloSiep   = " + mAvvIdAvvocatoFascicoloSiep + " ]\n"
				+ "[ mUffCodUfficio                = " + mUffCodUfficio + " ]\n"
				+ "[ mSollecito                    = " + mSollecito + " ]\n"
				+ "[ mCssIdCssa                    = " + mCssIdCssa + " ]\n"
				+ "[ mDescrizione                  = " + mDescrizione + " ]\n"
				+ "[ mIstDetIdIstitutoDetenzione   = " + mIstDetIdIstitutoDetenzione + " ]\n"
				+ "[ mDescrUfficioDestinatario     = " + mDescrUfficioDestinatario + " ]\n"
				+ "[ mAvvIdAvvocatoFascicoloSius   = " + mAvvIdAvvocatoFascicoloSius + " ]\n"
				+ "[ mAvvIdAvvocatoFascicoloSige   = " + mAvvIdAvvocatoFascicoloSige + " ]\n"
				+ "[ mAutEstIdAutoritaEstDeleg     = " + mAutEstIdAutoritaEstDeleg + " ]\n"
				+ "[ mIdCivilmenteObbligato        = " + mIdCivilmenteObbligato + " ]\n"
				+ "[ mCurIdCuratore				   = " + mCurIdCuratore + " ]\n";

		if (mAutoritaEsterna != null)
			lStr += "mAutoritaEsterna: " + mAutoritaEsterna.toString() + "\n";

		if (mUfficio != null)
			lStr += "mUfficio: " + mUfficio + "\n";

		if (mCSSA != null)
			lStr += "mCSSA: " + mCSSA + "\n";

		if (mAvvSiep != null)
			lStr += "AvvSiep: " + mAvvSiep + "\n";

		if (mAvvSius != null)
			lStr += "AvvSius: " + mAvvSius + "\n";

		if (mAvvSige != null)
			lStr += "AvvSige: " + mAvvSige + "\n";

		// modifica relativa al tipo istituto
		if (mIstitutoDetenzione != null)
			lStr += "mIstitutoDetenzione: " + mIstitutoDetenzione + "\n";

		if (mAutoritaEsternaDelegata != null)
			lStr += "mAutoritaEsternaDelegata: " + mAutoritaEsternaDelegata + "\n";

		if (mCurSius != null)
			lStr += "CurSius: " + mCurSius + "\n";

		return lStr;
	}

	public String toString2() {
		String lStr = new String();

		lStr = "" + mIdNotifica + " - " + mCodTipoNotifica + " - " + mCodUffUepeUssmSS + " - "
				+ mCodUffUdsUdsm + " - " + mCodUffTdsTdsm + " - " + mDescrTipoNotifica + " - "
				+ mDataAvvenutaNotifica + " - " + mDataInvio + " - " + mCodEsito + " - " + mDescrEsito + " - "
				+ mNote + " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - "
				+ mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodiceOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - " + mEveIdEvento + " - "
				+ mAutEstIdAutoritaEsterna + " - " + mSogIdSoggetto + " - " + mAvvIdAvvocatoFascicoloSiep
				+ " - " + mUffCodUfficio + " - " + mSollecito + " - " + mCssIdCssa + " - " + mDescrizione
				+ " - " +
				// modifica relativa al tipo istituto
				mIstDetIdIstitutoDetenzione + " - " +

				mDescrUfficioDestinatario + " - " + // PM - 20030609
				mCurIdCuratore + " - ";

		if (mAutoritaEsterna != null)
			lStr += "" + mAutoritaEsterna.toString();

		if (mUfficio != null)
			lStr += "" + mUfficio;

		if (mCSSA != null)
			lStr += "" + mCSSA;

		if (mAvvSiep != null)
			lStr += "AvvSiep: " + mAvvSiep;

		if (mAvvSius != null)
			lStr += "AvvSius: " + mAvvSius;

		if (mAvvSige != null)
			lStr += "AvvSige: " + mAvvSige;

		if (mCurSius != null)
			lStr += "CurSius: " + mCurSius + "\n";

		// modifica relativa al tipo istituto
		if (mIstitutoDetenzione != null)
			lStr += "" + mIstitutoDetenzione;

		return lStr;
	}

	public AvvocatoModel getAvvocato() {
		return mAvvocato;
	}

	public void setAvvocato(AvvocatoModel mAvvocato) {
		this.mAvvocato = mAvvocato;
	}

}