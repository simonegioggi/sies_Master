package siap.sige.statistiche.model;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.ufficio.model.UfficioModel;
import siap.sige.statistiche.action.ICostantiStatistiche;
import f3b.model.GenericModel;

/**
 * Title: RicercaFogliCompModel</p>
 * Description: La Classe Model rappresenta il filtro da utilizzare nella 
 * Ricerca dei Fogli Complementari.
 * I possibili parametri utilizzati nella ricerca sono:
 * l'intervallo delle date di data emissione oppure un intervallo di Estremi FC espresso come ANNO / NUM
 * lo stato di validazione: validato, non validato, annullato, tutti.
 * La ricerca è sempre limitata ad un ufficio.
 */
public class RicercaFogliCompModel extends GenericModel implements ICostantiStatistiche
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3427357431816258285L;

	private String mCodUfficioInserimento = "";
	
	// Magistrato Relatore
	private String mCodMagistrato = "";
	private String mDescMagistrato = "";
	
	// Esperto Relatore
	private BigDecimal mCodEsperto = null;
	private String mDescEsperto = "";
	
	// Intervallo per Riferimenti Ordinanze
	private 	BigDecimal	mAnnoIni = null;
	private 	BigDecimal	mNumIni = null;
	private 	BigDecimal	mAnnoFine = null;
	private 	BigDecimal	mNumFine = null;

	// Intervallo per data di deposito
	private 	Date	      mDataDepositoIni = null;
	private 	Date	      mDataDepositoFine = null;

	// Intervallo per data di arrivo in cancelleria
	private 	Date	      mDataArrCancelleriaIni = null;
	private 	Date	      mDataArrCancelleriaFine = null;

	// Intervallo per data di emissione (Foglio Complementare)
	private 	Date	      mDataEmissioneIni = null;
	private 	Date	      mDataEmissioneFine = null;
	
	private String mCodTipoImpugnazione = "";
	
	// Modalità di ricerca (Per: Foglio Complementare.)
	private String mModalitaRicerca = "";
	
	// Tipo di validazione 
	private String mStatoValidazione = TUTTI;
	
	/*
	 * Tipo di Intervallo di ricerca
	 * Può assumere i valori: Intervallo Estremi Provvedimento, Intervallo Data Deposito.
	 */
	private String mTipoIntervalloRicerca = "";
	
	// Resoconto risultato
	
	// Indica che sono stati calcolati i contatori 
	// sul risultato della ricerca
	private boolean mCalcolatiTotali = false;
	
	// Numero totale dei procedimenti risultato della ricerca
	private 	BigDecimal	mNumTotali = new BigDecimal(0);
	// Numero totale dei procedimenti risultato della ricerca Annullati
	private 	BigDecimal	mNumAnnullati = new BigDecimal(0);;
	// Numero totale dei procedimenti risultato della ricerca Non Validati
	private 	BigDecimal	mNumNonValidati = new BigDecimal(0);;
	// Contiene resoconto dei calcoli statistici sulla ricerca effettuata
	private String mDescCalcoli = "";
	
	
	private boolean fcTrasmessi=false;
	private boolean fcIscrittiManualmente=false;
	private boolean provvedimentiPriviFC=false;
	private boolean provvedimentiFCNonTrasmessi=false;
	private boolean fcTrasmessiErrore=false;
	private boolean fcAnnullati=false;
	
	private UfficioModel ufficioConnesso=null;

	//COSTRUTTORE DI DEFAULT
	public RicercaFogliCompModel()
	{
		// Di default la Ricerca è per Estremi Foglio Complementare
		mModalitaRicerca = RICERCA_FC;
		mCalcolatiTotali = false;
	}
	
	// Costruttore di copia
	public RicercaFogliCompModel(RicercaFogliCompModel aModel)
	{
		mCodUfficioInserimento = aModel.getCodUfficioInserimento();
		mCodMagistrato = aModel.getCodMagistrato();
		mDescMagistrato = aModel.getDescMagistrato();
		mCodEsperto = aModel.getCodEsperto();
		mDescEsperto = aModel.getDescEsperto();
		mAnnoIni = aModel.getAnnoIniziale();
		mNumIni = aModel.getNumIniziale();
		mAnnoFine = aModel.getAnnoFinale();
		mNumFine = aModel.getNumFinale();
		mDataDepositoIni = aModel.getDataDepositoIniziale();
		mDataDepositoFine = aModel.getDataDepositoFinale();
		mDataArrCancelleriaIni = aModel.getDataArrivoInCancelleriaIniziale();
		mDataArrCancelleriaFine = aModel.getDataArrivoInCancelleriaFinale();
		mDataEmissioneIni = aModel.getDataEmissioneIniziale();
		mDataEmissioneFine = aModel.getDataEmissioneFinale();
		mCodTipoImpugnazione = aModel.getCodTipoImpugnazione();
		mModalitaRicerca = aModel.getModalitaRicerca();
		mStatoValidazione = aModel.getStatoValidazione();
		mTipoIntervalloRicerca = aModel.getTipoIntervalloRicerca();
		mCalcolatiTotali = aModel.getCalcolatiTotali();
		mNumTotali = aModel.getNumTotali();
		mNumAnnullati = aModel.getNumAnnulati();
		mNumNonValidati = aModel.getNumNonValidati();
	}
 
	
	
/*
 * Get
 */
	public Date getDataDepositoIniziale() {return mDataDepositoIni;}
	public Date getDataDepositoFinale() {return mDataDepositoFine;}
	public Date getDataArrivoInCancelleriaIniziale() {return mDataArrCancelleriaIni;}
	public Date getDataArrivoInCancelleriaFinale() {return mDataArrCancelleriaFine;}
	public Date getDataEmissioneIniziale() {return mDataEmissioneIni;}
	public Date getDataEmissioneFinale() {return mDataEmissioneFine;}
	public BigDecimal getAnnoIniziale(){return mAnnoIni ;}
	public BigDecimal getNumIniziale(){return mNumIni ;}
	public BigDecimal getAnnoFinale(){return mAnnoFine ;}
	public BigDecimal getNumFinale(){return mNumFine ;}
	public String     getCodUfficioInserimento() { return mCodUfficioInserimento; }
	public String     getCodMagistrato() { return mCodMagistrato; }
	public String     getDescMagistrato() { return mDescMagistrato; }
	public BigDecimal getCodEsperto() { return mCodEsperto; }
	public String     getDescEsperto() { return mDescEsperto; }
	public String     getTipoIntervalloRicerca() { return mTipoIntervalloRicerca; }
	public String     getStatoValidazione() { return mStatoValidazione; }
	public String     getModalitaRicerca() { return mModalitaRicerca; }
	public String     getCodTipoImpugnazione() { return mCodTipoImpugnazione; }
	public boolean 	  getCalcolatiTotali()	{return mCalcolatiTotali;}
	public BigDecimal getNumTotali(){return mNumTotali ;}
	public BigDecimal getNumAnnulati(){return mNumAnnullati;}
	public BigDecimal getNumNonValidati(){return mNumNonValidati;}
	
	public String     getDescCalcoli() { return mDescCalcoli; }
	
	
	
	public boolean isTipoIntervalloRicercaXEstremiProvvedimento()
	{
		boolean lRet = false;
		if (mTipoIntervalloRicerca != null && mTipoIntervalloRicerca.equalsIgnoreCase(ESTREMI_PROVVEDIMENTO_INTERVALLO))
			lRet = true;
		return lRet;
	}
	
	public boolean isRicercaXDateEmissione()
	{
		boolean lRet = false;
		if (mTipoIntervalloRicerca != null && mTipoIntervalloRicerca.equalsIgnoreCase(DATA_EMISSIONE_INTERVALLO))
			lRet = true;
		return lRet;
	}
	
	public boolean isRicercaXOrdinanza()
	{
		boolean lRet = false;
		if (mModalitaRicerca != null && mModalitaRicerca.equalsIgnoreCase(RICERCA_ORDINANZE_PRIVE_DI_FC) ){
			lRet = true;
		}
		return lRet;
	}

	public boolean isRicercaXDateDeposito()
	{
		boolean lRet = false;
		if (mTipoIntervalloRicerca != null && mTipoIntervalloRicerca.equalsIgnoreCase(DATA_DEPOSITO_INTERVALLO))
			lRet = true;
		return lRet;
	}

	public boolean isRicercaXFoglioComplementare()
	{
		boolean lRet = false;
		if (mModalitaRicerca != null && 
			( mModalitaRicerca.equalsIgnoreCase(RICERCA_FOGLIO_COMPLEMENTARE) ||
			  mModalitaRicerca.equalsIgnoreCase(RICERCA_FC_TRASMESSI) ||
			  mModalitaRicerca.equalsIgnoreCase(RICERCA_FC_DA_TRASMETTERE) ||
			  mModalitaRicerca.equalsIgnoreCase(RICERCA_FC_TRASMESSI_CON_ERRORE) ||
			  mModalitaRicerca.equalsIgnoreCase(RICERCA_FC_ISCRITTI_MANUALMENTE) )
		   )
			lRet = true;
		return lRet;
	}

	public boolean isRicercaAnnullati()
	{
		boolean lRet = false;
		if (mStatoValidazione != null && mStatoValidazione.equalsIgnoreCase(ANNULLATI))
			lRet = true;
		return lRet;
	}
	
	public boolean isRicercaStatoTutti()
	{
		boolean lRet = false;
		if (mStatoValidazione != null && mStatoValidazione.equalsIgnoreCase(TUTTI))
			lRet = true;
		return lRet;
	}
	
	public String getDescModalitaRicerca()
	{
		String lRet = "";
		if (isRicercaXOrdinanza())
			lRet = "Ricerca Procedimenti per estremi Ordinanza";
		else if (isRicercaXFoglioComplementare())
			lRet = "Ricerca Procedimenti per estremi Foglio Complementare";

		if (isTipoIntervalloRicercaXEstremiProvvedimento())
			lRet += " in intervallo Anno e Num ";
		else if (isRicercaXDateEmissione())
			lRet += " in intervallo date di emissione ";
		
		lRet += " Stato: ";
		lRet += getDescStatoValidazione();
			
		return lRet;
	}
	
	public String getDescStatoValidazione()
	{
		String lRet = "";
		if (mStatoValidazione.equalsIgnoreCase(ANNULLATI))
			lRet = "ANNULLATO";
		else if (mStatoValidazione.equalsIgnoreCase(VALIDATI))
			lRet = "VALIDATO";
		else if (mStatoValidazione.equalsIgnoreCase(NON_VALIDATI))
			lRet = "NON VALIDATO";
		else if (mStatoValidazione.equalsIgnoreCase(TUTTI))
			lRet = "TUTTI";
		else if (mStatoValidazione.equalsIgnoreCase(NON_ANNULLATI))
			lRet = "NON ANNULLATI";

		return lRet;
	}
	
	/**
	 * La funzione valorizza la stringa Descrizione dei calcoli statistici effettuati 
	 * in base al contenuto dei contatori parziali ed al tipo di ricerca effettuato.
	 */
	private String resetDescCalcoli()
	{
		String lRet = "";
		if (isRicercaStatoTutti() && mCalcolatiTotali)
		{
			lRet = "Totale atti: " + getNumTotali();
			lRet += "    Annullati: " + getNumAnnulati() ;
			if (isRicercaXOrdinanza())
				lRet += "   Non Validati: " + getNumNonValidati(); 
		}

		return lRet;
	}
	
	/*
	 * Set
	 */
	public void setAnnoIniziale(BigDecimal aValore)  { mAnnoIni = aValore; }
	public void setAnnoFinale(BigDecimal aValore)  { mAnnoFine = aValore; }
	public void setNumIniziale(BigDecimal aValore)  { mNumIni = aValore; }
	public void setNumFinale(BigDecimal aValore)  { mNumFine = aValore; }
	public void setAnnoNumIniziale(BigDecimal aAnno, BigDecimal aNum) {setAnnoIniziale(aAnno); setNumIniziale(aNum);}
	public void setAnnoNumFinale(BigDecimal aAnno, BigDecimal aNum) {setAnnoFinale(aAnno); setNumFinale(aNum);}
	public void setDataDepositoIniziale(Date aValore)  { mDataDepositoIni = aValore; }
	public void setDataDepositoFinale(Date aValore)  { mDataDepositoFine = aValore; }
	public void setDataArrivoInCancelleriaIniziale(Date aValore)  { mDataArrCancelleriaIni = aValore; }
	public void setDataArrivoInCancelleriaFinale(Date aValore)  { mDataArrCancelleriaFine = aValore; }
	public void setDataEmissioneIniziale(Date aValore)  { mDataEmissioneIni = aValore; }
	public void setDataEmissioneFinale(Date aValore)  { mDataEmissioneFine = aValore; }
	public void setCodUfficioInserimento(String aValore )   { mCodUfficioInserimento = aValore; }
	public void setCodMagistrato(String aValore )   { mCodMagistrato = aValore; }
	public void setDescMagistrato(String aValore )   { mDescMagistrato = aValore; }
	public void setCodEsperto(BigDecimal aValore )   { mCodEsperto = aValore; }
	public void setDescEsperto(String aValore )   { mDescEsperto = aValore; }
	public void setTipoIntervalloRicerca(String aValore )   { mTipoIntervalloRicerca = aValore; }
	public void setStatoValidazione(String aValore )   { mStatoValidazione = aValore; }
	public void setModalitaRicerca(String aValore )   { mModalitaRicerca = aValore; }
	public void setCodTipoImpugnazione(String aValore )   { mCodTipoImpugnazione = aValore; }

	public void setStatoAnnullati() {setStatoValidazione(ANNULLATI);}
	public void setStatoNonValidati() {setStatoValidazione(NON_VALIDATI);}
	
	public void	setCalcolatiTotali(boolean aValore) {mCalcolatiTotali = aValore;}
	public void setNumTotali(BigDecimal aValore) {mNumTotali = aValore;}
	public void setNumAnnullati(BigDecimal aValore) {mNumAnnullati = aValore;}
	public void setNumNonValidati(BigDecimal aValore) {mNumNonValidati = aValore;}
	
	/*
	 * Se il parametro passato è valorizzato
	 *  viene usato per valorizzare la stringa che fornisce 
	 *  il resoconto dei calcoli effettuati nell'ultima ricerca.
	 *  Se invece non è valorizzato viene chiamata la funzione che valorizza la stringa di resoconto in base 
	 *  ai contatori già calcolati nell'ultima ricerca.
	 */
	public void setDescCalcoli(String aValore) 
	{   
		if (aValore != null )
			mDescCalcoli = aValore; 
		else
			mDescCalcoli = resetDescCalcoli();
			
	}

	public boolean isFcTrasmessi() {
		return fcTrasmessi;
	}

	public void setFcTrasmessi(boolean fcTrasmessi) {
		this.fcTrasmessi = fcTrasmessi;
	}

	public boolean isFcIscrittiManualmente() {
		return fcIscrittiManualmente;
	}

	public void setFcIscrittiManualmente(boolean fcIscrittiManualmente) {
		this.fcIscrittiManualmente = fcIscrittiManualmente;
	}

	public boolean isProvvedimentiPriviFC() {
		return provvedimentiPriviFC;
	}

	public void setProvvedimentiPriviFC(boolean provvedimentiPriviFC) {
		this.provvedimentiPriviFC = provvedimentiPriviFC;
	}

	public boolean isProvvedimentiFCNonTrasmessi() {
		return provvedimentiFCNonTrasmessi;
	}

	public void setProvvedimentiFCNonTrasmessi(boolean provvedimentiFCNonTrasmessi) {
		this.provvedimentiFCNonTrasmessi = provvedimentiFCNonTrasmessi;
	}

	public boolean isFcTrasmessiErrore() {
		return fcTrasmessiErrore;
	}

	public void setFcTrasmessiErrore(boolean fcTrasmessiErrore) {
		this.fcTrasmessiErrore = fcTrasmessiErrore;
	}

	public boolean isFcAnnullati() {
		return fcAnnullati;
	}

	public void setFcAnnullati(boolean fcAnnullati) {
		this.fcAnnullati = fcAnnullati;
	}

	public UfficioModel getUfficioConnesso() {
		return ufficioConnesso;
	}

	public void setUfficioConnesso(UfficioModel ufficioConnesso) {
		this.ufficioConnesso = ufficioConnesso;
	}

}
