package siap.sige.fascicolo.model;

/**
* <p>Title: FascicoloSigeModel</p>
* <p>Description: Classe Model che rappresenta il FascicoloSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 5.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.util.SIGELookupRemote;
import f3b.model.GenericModel;
import f3b.util.F3BException;

public class FascicoloSigeModel extends GenericModel 
{
	
	private static final long serialVersionUID = 6219799378226954633L;
	private 	BigDecimal	mIdFascicoloSige;
	private 	BigDecimal	mSogIdSoggetto;
	private 	BigDecimal	mChiaveAnno;

	private 	String	    mChiaveUfficio;
	private 	String	    mDescrUfficio;
	private 	BigDecimal	mChiaveProgr;

	private 	BigDecimal	mSenIdSentenzaCumulo;
	private 	BigDecimal	mIdSezione;
	private 	String		mDescrSezione;
	private 	String		mCodStatoFascicolo;
	private 	String		mDescrStatoFascicolo;
	private 	String		mCodTipoGiudizio;
	private 	String		mDescrTipoGiudizio;
	private 	Date		mDataIscrizione;

	private 	Date		mDataDefinizione;
	private 	BigDecimal	mRicIdRichiestaSige;
	private 	String		mCodOperatoreInserimento;
	private 	String		mCodUfficioInserimento;
	private 	String		mDescrUfficioInserimento;
	private 	Date		mDataInserimento;
	private 	String		mCodOperatoreAggiornamento;
	private 	String		mCodUfficioAggiornamento;
	private 	String		mDescrUfficioAggiornamento;
	private 	Date		mDataAggiornamento;
	private 	String		mNote;
	private 	String		mCodPosizioneGiuridica;
	private 	String		mDescrPosizioneGiuridica;
	private 	Date		mDataFinePena;
    private 	String 		mCodTipoDefinizione;
    private 	String 		mDescrTipoDefinizione;
    private 	String 		mDescrDefinizione;
    private 	String 		mMotivoAltraDefinizione;
    private 	String 		mTipologiaRito;
	private 	BigDecimal	mFasSigIdFascicoloSige;  // 26/07/2010
    private 	BigDecimal  mNumeroFascicoliUnificati;  // 26/07/2010

    //Modifica Accorpamento Uffici  
    private 	String  	mCodTipoUfficioInserimento;
    private 	String  	mDescrTipoUfficioInserimento;
    private 	String  	mDescrComuneUfficioInserimento;
    private 	BigDecimal 	mChiaveProgrOrig;
    private 	String  	mFlagUfficioAccorpato;
    private 	String 		nomeCognomeMagistrato;
    private 	Date 		mDataPrimaUdienza;
    private 	Date 		mDataUltimaUdienza;
    private 	BigDecimal 	mNumGiorniIntercorsiTraIscrizioneEDeposito;
    private 	BigDecimal 	mNumGiorniIntercorsiTraIscrizioneEDepositoAnno;
    
    private 	BigDecimal 	mIdFascicoloSigeOrigine;
    
    private 	Date		mDataDefinizioneOrigine;
    
    private 	BigDecimal	mIdEventoProvvCumulo;
    
    // MEV_15_S4 aggiunto parametro che identifica il codice tenore decisione
    // presente sull'impugnazione sige.
    private String mCodTenoreDecisione; 
    // Parametro utilizzato nella Statistica della funzionalità:
    // "Ricerca Procedimenti per Estremi Atto"
	private String mDescOggetto;
	
	//COSTRUTTORE DI DEFAULT 
	public FascicoloSigeModel ()
	{
		mIdFascicoloSige = null;
		mSogIdSoggetto = null;
		mChiaveAnno = null;
		mChiaveUfficio = "";
		mDescrUfficio = "";
		mChiaveProgr = null;
		mSenIdSentenzaCumulo = null;
		mIdSezione = null;
		mDescrSezione = "";
		mCodStatoFascicolo = "";
		mDescrStatoFascicolo = "";
		mCodTipoGiudizio = "";
		mDescrTipoGiudizio = "";
		mDataIscrizione = null;
		mDataDefinizione = null;
		mRicIdRichiestaSige = null;
		mCodOperatoreInserimento = "";
		mCodUfficioInserimento = "";
		mDescrUfficioInserimento = "";
		mDataInserimento = null;
		mCodOperatoreAggiornamento = "";
		mCodUfficioAggiornamento = "";
		mDescrUfficioAggiornamento = "";
		mDataAggiornamento = null;
		mNote = "";
		mCodPosizioneGiuridica = "";
		mDescrPosizioneGiuridica = "";
		mDataFinePena = null;
		mCodTipoDefinizione = null;
	    mDescrTipoDefinizione = "";
	    mDescrDefinizione = "";
	    mMotivoAltraDefinizione = "";
	    mTipologiaRito ="";
	    mFasSigIdFascicoloSige = null; // 26/07/2010
	    mNumeroFascicoliUnificati = new BigDecimal(0); // 26/07/2010

	    this.mCodTipoUfficioInserimento = "";
	    this.mDescrTipoUfficioInserimento = "";
	    this.mDescrComuneUfficioInserimento = "";
	    this.mChiaveProgrOrig = null;
	    this.mFlagUfficioAccorpato = "";
	    
	    this.mIdFascicoloSigeOrigine = null;
	    
	    mIdEventoProvvCumulo = null;
	    
	    mCodTenoreDecisione = "";
	    mDescOggetto ="";

	}

	//COSTRUTTORE DI COPIA 
	public FascicoloSigeModel ( FascicoloSigeModel aModel )
	{			 
		mIdFascicoloSige = aModel.mIdFascicoloSige;
		mSogIdSoggetto = aModel.mSogIdSoggetto;
		mChiaveAnno = aModel.mChiaveAnno;
		mChiaveUfficio = aModel.mChiaveUfficio;
		mDescrUfficio = aModel.mDescrUfficio;
		mChiaveProgr = aModel.mChiaveProgr;
		mSenIdSentenzaCumulo = aModel.mSenIdSentenzaCumulo;
		mIdSezione = aModel.mIdSezione;
		mDescrSezione = aModel.mDescrSezione;
		mCodStatoFascicolo = aModel.mCodStatoFascicolo;
		mDescrStatoFascicolo = aModel.mDescrStatoFascicolo;
		mCodTipoGiudizio = aModel.mCodTipoGiudizio;
		mDescrTipoGiudizio = aModel.mDescrTipoGiudizio;
		mDataIscrizione = aModel.mDataIscrizione;
		mDataDefinizione = aModel.mDataDefinizione;
		mRicIdRichiestaSige = aModel.mRicIdRichiestaSige;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mNote = aModel.mNote;
		mCodPosizioneGiuridica = aModel.mCodPosizioneGiuridica;
		mDescrPosizioneGiuridica = aModel.mDescrPosizioneGiuridica;
		mDataFinePena = aModel.mDataFinePena;
		mCodTipoDefinizione = aModel.mCodTipoDefinizione;
        mDescrTipoDefinizione = aModel.mDescrTipoDefinizione;
        mDescrDefinizione = aModel.mDescrDefinizione;
        mMotivoAltraDefinizione = aModel.mMotivoAltraDefinizione;
        mTipologiaRito = aModel.mTipologiaRito;
	    mFasSigIdFascicoloSige = aModel.mFasSigIdFascicoloSige; // 26/07/2010
	    mNumeroFascicoliUnificati = aModel.mNumeroFascicoliUnificati; // 26/07/2010

	    this.mCodTipoUfficioInserimento = aModel.mCodTipoUfficioInserimento;
	    this.mDescrTipoUfficioInserimento = aModel.mDescrTipoUfficioInserimento;
	    this.mDescrComuneUfficioInserimento = aModel.mDescrComuneUfficioInserimento;
	    this.mChiaveProgrOrig = aModel.mChiaveProgrOrig;
	    this.mFlagUfficioAccorpato = aModel.mFlagUfficioAccorpato;
	    
	    this.mIdFascicoloSigeOrigine = aModel.mIdFascicoloSigeOrigine;
	    
	    mIdEventoProvvCumulo = aModel.mIdEventoProvvCumulo;
	    
	    mCodTenoreDecisione = aModel.mCodTenoreDecisione;
	    mDescOggetto = aModel.mDescOggetto;
	}

	//COSTRUTTORE MODEL 
	public FascicoloSigeModel (
		BigDecimal	 aIdFascicoloSige,
		BigDecimal	 aSogIdSoggetto,
		BigDecimal	 aChiaveAnno,
		BigDecimal	 aChiaveAnnoIniziale,
		BigDecimal	 aChiaveAnnoFinale,
		String	     aChiaveUfficio,
		String	     aDescrUfficio,
		BigDecimal	 aChiaveProgr,
		BigDecimal	 aChiaveProgrIniziale,
		BigDecimal	 aChiaveProgrFinale,
		BigDecimal   aSenIdSentenzaCumulo,
		BigDecimal	 aIdSezione,
		String	     aDescrSezione,
		String	     aCodStatoFascicolo,
		String	     aDescrStatoFascicolo,
		String	     aCodTipoGiudizio,
		String	     aDescrTipoGiudizio,
		Date	     aDataIscrizione,
		Date	 	 aDataIscrizioneIniziale,
		Date	 	 aDataIscrizioneFinale,
		Date	 	 aDataDefinizione,
		BigDecimal	 aRicIdRichiestaSige,
		String	 	 aCodOperatoreInserimento,
		String	 	 aCodUfficioInserimento,
		String	 	 aDescrUfficioInserimento,
		Date	 	 aDataInserimento,
		String	 	 aCodOperatoreAggiornamento,
		String	 	 aCodUfficioAggiornamento,
		String	 	 aDescrUfficioAggiornamento,
		Date	 	 aDataAggiornamento,
		String 		 aNote,
	    String	 	 aCodPosizioneGiuridica,
	    String	 	 aDescrPosizioneGiuridica,
	    Date	 	 aDataFinePena,
	    String 		 aCodTipoDefinizione,
	    String 		 aDescrTipoDefinizione,
	    String 		 aDescrDefinizione,
	    String       aMotivoAltraDefinizione,
	    String       aTipologiaRito,
		BigDecimal	 aFasSigIdFascicoloSige,
		BigDecimal	 aNumeroFascicoliUnificati,
		BigDecimal   aChiaveProgrOrig,
		BigDecimal   aIdFascicoloSigeOrigine,
		BigDecimal   aIdEventoProvvCumulo,
		String       aCodTenoreDecisione,
		String       aDescOggetto)

	{
		mIdFascicoloSige = aIdFascicoloSige;
		mSogIdSoggetto = aSogIdSoggetto;
		mChiaveAnno = aChiaveAnno;
		mChiaveUfficio = aChiaveUfficio;
		mDescrUfficio = aDescrUfficio;
		mChiaveProgr = aChiaveProgr;
		mSenIdSentenzaCumulo = aSenIdSentenzaCumulo;
		mIdSezione = aIdSezione;
		mDescrSezione = aDescrSezione;
		mCodStatoFascicolo = aCodStatoFascicolo;
		mDescrStatoFascicolo = aDescrStatoFascicolo;
		mCodTipoGiudizio = aCodTipoGiudizio;
		mDescrTipoGiudizio = aDescrTipoGiudizio;
		mDataIscrizione = aDataIscrizione;
		mDataDefinizione = aDataDefinizione;
		mRicIdRichiestaSige = aRicIdRichiestaSige;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;
		mDataInserimento = aDataInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mNote = aNote;
		mCodPosizioneGiuridica = aCodPosizioneGiuridica;
		mDescrPosizioneGiuridica = aDescrPosizioneGiuridica;
		mDataFinePena = aDataFinePena;
		mCodTipoDefinizione = aCodTipoDefinizione;
		mDescrTipoDefinizione = aDescrTipoDefinizione;
		mDescrDefinizione = aDescrDefinizione;
		mMotivoAltraDefinizione = aMotivoAltraDefinizione;
		mTipologiaRito = aTipologiaRito;
		mFasSigIdFascicoloSige = aFasSigIdFascicoloSige; // 26/07/2010
		mNumeroFascicoliUnificati = aNumeroFascicoliUnificati; // 26/07/2010
		mChiaveProgrOrig = aChiaveProgrOrig;
		mIdFascicoloSigeOrigine = aIdFascicoloSigeOrigine;
		mIdEventoProvvCumulo = aIdEventoProvvCumulo;
		mCodTenoreDecisione = aCodTenoreDecisione;
		mDescOggetto = aDescOggetto;
	}

  //
  // METODI GET()
  //

	 public BigDecimal 	 	getIdFascicoloSige() 			{ return mIdFascicoloSige; } 
	 public BigDecimal 	 	getSogIdSoggetto() 				{ return mSogIdSoggetto; } 
	 public BigDecimal 	 	getChiaveAnno() 				{ return mChiaveAnno; } 
	 public String 			getChiaveUfficio() 				{ return mChiaveUfficio; } 
	 public String 			getDescrUfficio() 				{ return mDescrUfficio; } 

	 public BigDecimal 	 	getChiaveProgr() 				{ return mChiaveProgr; } 
	 public BigDecimal 		getSenIdSentenzaCumulo() 		{ return mSenIdSentenzaCumulo; } 
	 public BigDecimal 		getIdSezione() 					{ return mIdSezione; } 
	 public String 			getDescrSezione() 				{ return mDescrSezione; } 
	 public String 			getCodStatoFascicolo() 			{ return mCodStatoFascicolo; } 
	 public String 			getDescrStatoFascicolo() 		{ return mDescrStatoFascicolo; } 
	 public String 			getCodTipoGiudizio() 			{ return mCodTipoGiudizio; } 
	 public String 			getDescrTipoGiudizio() 			{ return mDescrTipoGiudizio; } 
	 public Date 			getDataIscrizione() 			{ return mDataIscrizione; } 
	 public Date 			getDataDefinizione() 			{ return mDataDefinizione; } 
	 public BigDecimal 	 	getRicIdRichiestaSige() 		{ return mRicIdRichiestaSige; } 
	 public String 			getCodOperatoreInserimento() 	{ return mCodOperatoreInserimento; } 
	 public String 			getCodUfficioInserimento() 		{ return mCodUfficioInserimento; } 
	 public String 			getDescrUfficioInserimento() 	{ return mDescrUfficioInserimento; } 
	 public Date 			getDataInserimento() 			{ return mDataInserimento; } 
	 public String 			getCodOperatoreAggiornamento()  { return mCodOperatoreAggiornamento; } 
	 public String 			getCodUfficioAggiornamento() 	{ return mCodUfficioAggiornamento; } 
	 public String 			getDescrUfficioAggiornamento()  { return mDescrUfficioAggiornamento; } 
	 public Date 			getDataAggiornamento() 			{ return mDataAggiornamento; } 
	 public String 			getNote() 						{ return mNote; } 
	 public String 			getCodPosizioneGiuridica() 		{ return mCodPosizioneGiuridica; } 
	 public String 			getDescrPosizioneGiuridica() 	{ return mDescrPosizioneGiuridica; } 
	 public Date 			getDataFinePena() 			 	{ return mDataFinePena; } 
	 public String 			getCodTipoDefinizione() 		{ return mCodTipoDefinizione; } 
	 public String 			getDescrTipoDefinizione() 		{ return mDescrTipoDefinizione; } 
	 public String 			getDescrDefinizione() 			{ return mDescrDefinizione; } 
	 public String 			getMotivoAltraDefinizione() 	{ return mMotivoAltraDefinizione; }
	 public String 			getTipologiaRito() 			    { return mTipologiaRito; }
	 public BigDecimal 	 	getFasSigIdFascicoloSige()		{ return mFasSigIdFascicoloSige; }  // 26/07/2010 
	 public BigDecimal 	 	getNumeroFascicoliUnificati()   { return mNumeroFascicoliUnificati; }  // 26/07/2010 
	 
	 //Modifica Accorpamento Uffici
	 public String     		getCodTipoUfficioInserimento() 	  { return mCodTipoUfficioInserimento; }
	 public String     		getDescrTipoUfficioInserimento()  { return mDescrTipoUfficioInserimento; }
	 public String     		getDescrComuneUfficioInserimento(){ return mDescrComuneUfficioInserimento; }
	 public BigDecimal 		getChiaveProgrOrig()              { return mChiaveProgrOrig; }
	 public String     		getFlagUfficioAccorpato() 	      { return mFlagUfficioAccorpato; }
	 
	 public BigDecimal      getIdFascicoloSigeOrigine()       {	return mIdFascicoloSigeOrigine;	}
	 
	 public Date 			getDataDefinizioneOrigine() 	  { return mDataDefinizioneOrigine; } 
	 public BigDecimal 	 	getIdEventoProvvCumulo()   		  { return mIdEventoProvvCumulo; }

	 public String     		getCodTenoreDecisione() 	      { return mCodTenoreDecisione; }
	 
	 public String 			getDescOggetto() 			      { return mDescOggetto; }

	 public String 			getNomeCognomeMagistrato()                         { return nomeCognomeMagistrato; }
	 public Date 			getDataPrimaUdienza() 						       { return mDataPrimaUdienza; }
	 public Date 			getDataUltimaUdienza()						   	   { return mDataUltimaUdienza; }
	 public BigDecimal		getNumGiorniIntercorsiTraIscrizioneEDeposito() 	   { return mNumGiorniIntercorsiTraIscrizioneEDeposito;	}
	 public BigDecimal 		getNumGiorniIntercorsiTraIscrizioneEDepositoAnno() { return mNumGiorniIntercorsiTraIscrizioneEDepositoAnno;	}

	 //
	 // METODI SET()
	 //
	 public void	setIdFascicoloSige(BigDecimal aValore ) 		{ mIdFascicoloSige = aValore; } 
	 public void	setSogIdSoggetto(BigDecimal aValore ) 			{ mSogIdSoggetto = aValore; } 
	 public void	setChiaveAnno(BigDecimal aValore ) 				{ mChiaveAnno = aValore; } 
	 public void	setChiaveUfficio(String aValore ) 				{ mChiaveUfficio = aValore; } 
	 public void	setDescrUfficio(String aValore ) 				{ mDescrUfficio = aValore; } 
	 public void	setChiaveProgr(BigDecimal aValore ) 			{ mChiaveProgr = aValore; } 
	 public void	setSenIdSentenzaCumulo(BigDecimal aValore ) 	{ mSenIdSentenzaCumulo = aValore; } 
	 public void	setIdSezione(BigDecimal aValore ) 				{ mIdSezione = aValore; } 
	 public void	setDescrSezione(String aValore ) 				{ mDescrSezione = aValore; } 
	 public void	setCodStatoFascicolo(String aValore ) 			{ mCodStatoFascicolo = aValore; } 
	 public void	setDescrStatoFascicolo(String aValore ) 		{ mDescrStatoFascicolo = aValore; } 
	 public void	setCodTipoGiudizio(String aValore ) 			{ mCodTipoGiudizio = aValore; } 
	 public void	setDescrTipoGiudizio(String aValore ) 			{ mDescrTipoGiudizio = aValore; } 
	 public void	setDataIscrizione(Date aValore ) 				{ mDataIscrizione = aValore; } 
	 public void	setDataDefinizione(Date aValore ) 				{ mDataDefinizione = aValore; } 
	 public void	setRicIdRichiestaSige(BigDecimal aValore )		{ mRicIdRichiestaSige = aValore; } 
	 public void	setCodOperatoreInserimento(String aValore ) 	{ mCodOperatoreInserimento = aValore; } 
	 public void	setCodUfficioInserimento(String aValore ) 		{ mCodUfficioInserimento = aValore; } 
	 public void	setDescrUfficioInserimento(String aValore ) 	{ mDescrUfficioInserimento = aValore; } 
	 public void	setDataInserimento(Date aValore ) 			 	{ mDataInserimento = aValore; } 
	 public void	setCodOperatoreAggiornamento(String aValore)	{ mCodOperatoreAggiornamento = aValore; } 
	 public void	setCodUfficioAggiornamento(String aValore ) 	{ mCodUfficioAggiornamento = aValore; } 
	 public void	setDescrUfficioAggiornamento(String aValore)	{ mDescrUfficioAggiornamento = aValore; } 
	 public void	setDataAggiornamento(Date aValore ) 			{ mDataAggiornamento = aValore; } 
	 public void	setNote(String aValore) 						{ mNote = aValore;}
	 public void	setCodPosizioneGiuridica(String aValore ) 		{ mCodPosizioneGiuridica = aValore; } 
	 public void	setDescrPosizioneGiuridica(String aValore ) 	{ mDescrPosizioneGiuridica = aValore; } 
	 public void	setDataFinePena(Date aValore ) 			 		{ mDataFinePena = aValore; } 
	 public void	setCodTipoDefinizione(String aValore ) 			{ mCodTipoDefinizione = aValore; } 
	 public void	setDescrTipoDefinizione(String aValore ) 		{ mDescrTipoDefinizione = aValore; } 
	 public void	setDescrDefinizione(String aValore ) 			{ mDescrDefinizione = aValore; } 
	 public void	setMotivoAltraDefinizione(String aValore ) 		{ mMotivoAltraDefinizione = aValore; } 
	 public void	setTipologiaRito(String aValore ) 		        { mTipologiaRito = aValore; }
	 public void	setFasSigIdFascicoloSige(BigDecimal aValore)	{ mFasSigIdFascicoloSige = aValore; }   // 26/07/2010
	 public void	setNumeroFascicoliUnificati(BigDecimal aValore) { mNumeroFascicoliUnificati = aValore; }   // 26/07/2010

	//Modifica Accorpamento Uffici 
	 public void	setChiaveProgrOrig(BigDecimal aValore) 			{ mChiaveProgrOrig = aValore; }
	 public void 	setCodTipoUfficioInserimento(String aValore) 	{ mCodTipoUfficioInserimento = aValore; }
	 public void 	setDescrTipoUfficioInserimento(String aValore) 	{ mDescrTipoUfficioInserimento = aValore; }
	 public void 	setDescrComuneUfficioInserimento(String aValore){ mDescrComuneUfficioInserimento = aValore; }
	 public void 	setFlagUfficioAccorpato(String aValore)         { mFlagUfficioAccorpato = aValore; }

	 public void    setIdFascicoloSigeOrigine(BigDecimal aValore)   { mIdFascicoloSigeOrigine = aValore; }
	 
	 public void	setDataDefinizioneOrigine(Date aValore ) 		{ mDataDefinizioneOrigine = aValore; }
	 
	 public void    setIdEventoProvvCumulo(BigDecimal aValore)   	{ mIdEventoProvvCumulo = aValore; }
	 
	 public void 	setCodTenoreDecisione(String aValore) 			{ mCodTenoreDecisione = aValore; }
	 
	 public void	setDescOggetto(String aValore ) 		        { mDescOggetto = aValore; }

	 public void 	setNomeCognomeMagistrato(String aValore) 							 { nomeCognomeMagistrato = aValore;	}
	 public void 	setDataPrimaUdienza(Date aValore)     								 { mDataPrimaUdienza = aValore; }
	 public void 	setDataUltimaUdienza(Date aValore)      							 { mDataUltimaUdienza = aValore; }
	 public void 	setNumGiorniIntercorsiTraIscrizioneEDeposito(BigDecimal aValore) 	 { mNumGiorniIntercorsiTraIscrizioneEDeposito = aValore; }
	 public void 	setNumGiorniIntercorsiTraIscrizioneEDepositoAnno(BigDecimal aValore) { mNumGiorniIntercorsiTraIscrizioneEDepositoAnno = aValore; }
	 /**
	  * Il metodo effettua la decodifica di quegli attributi del Model che contengono dei campi codificati.
	  * 
	  * La decodifica del campo interessato viene memorizzata nell'attributo di descrizione ad esso relativo.
	  * Gli attributi codificati ed i relativi attributi di decodifica interessati a questa operazione sono:
	  * mCodStatoFascicolo -> mDescrStatoFascicolo,
	  * mCodTipoGiudizio -> mDescrTipoGiudizio,
	  * mChiaveUfficio -> mDescrUfficio.
	  * 
	  * @throws F3BException
	  */
	 public FascicoloSigeModel decodifica() throws F3BException 
	 {
		 try
		 {
		 // Decodifica Stato Fascicolo
		  if (mCodStatoFascicolo != null && mCodStatoFascicolo.trim().length() > 0)
			  setDescrStatoFascicolo(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getStatoFascicolo(), mCodStatoFascicolo));
		  
			 // Decodifica Tipo Giudizio
		  if (mCodTipoGiudizio != null && mCodTipoGiudizio.trim().length() > 0)
			  setDescrTipoGiudizio(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoGiudizioSige()  , mCodTipoGiudizio));
	    
		    // Decodifica Chiave Ufficio
		  if (mChiaveUfficio != null && mChiaveUfficio.trim().length() > 0)
		  {
		      UfficioModel lUff = UfficioUtils.getUfficioByCodUfficio(mChiaveUfficio);
		      setDescrUfficio(lUff.getDescrTipoUfficio() + " " +  lUff.getDescrComune());
		  }
		  // Decodifica Sezione
		  if (mIdSezione != null)
		  {
			  SezioneModel lSezione = getSezioneById(mIdSezione);
			  if (lSezione != null)
				  setDescrSezione(lSezione.getDescrizione());
		  }
		  else
			  setDescrSezione("-");
		  
			 // Decodifica Tipo Definizione 
		  if (mCodTipoDefinizione != null && mCodTipoDefinizione.trim().length() > 0)
			  setDescrTipoDefinizione(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoDefinizione()  , mCodTipoDefinizione));

		  
		 }
		 catch (Exception e)
	     {
	    	 throw new F3BException(F3BException.EX_OPERATION_FAILED, "Errore nella trascodifica codice ( " + getClass().getPackage().getName() + ".decodifica()) -> "+ e.getMessage());
	     }
		 return this;
	 }
	 
	/**
	 * STUB : Funzione da spostare in sezione.utils
	 * @param aIdSezione
	 * @return
	 * @throws F3BException
	 */
	 public SezioneModel getSezioneById(BigDecimal aIdSezione) 
	  throws F3BException
	 {
		 SezioneModel lSezione = null;
		
		 ISezione lCtrl = SIGELookupRemote.getSezioneRemote(); 
		 lSezione = lCtrl.ExRicercaSezioneByKey(aIdSezione);
		 return lSezione;    
	 }

}