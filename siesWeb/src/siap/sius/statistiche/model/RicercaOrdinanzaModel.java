package siap.sius.statistiche.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sius.statistiche.action.ICostantiStatistiche;

/**
 * *
 * <p>
 * Title: RicercaOrdinanzaModel
 * </p>
 * <p>
 * Description: La Classe Model rappresenta il filtro da utilizzare nella Ricerca di Procedimenti Sius per
 * Estremi Ordinanza. I possibili parametri utilizzati nella ricerca sono: l'intervallo delle date di deposito
 * oppure un intervallo di Estremi Ordinanza espresso come ANNO / NUM lo stato di validazione: validato, non
 * validato, annullato, tutti. La ricerca è sempre limitata ad un ufficio.
 * 
 * @author Lesposito
 *
 */
public class RicercaOrdinanzaModel extends GenericModel implements ICostantiStatistiche {

	/**
	 *
	 */
	private static final long serialVersionUID = -356522442593490122L;

	private String mCodUfficioInserimento = "";

	// Magistrato Relatore
	private String mCodMagistrato = "";
	private String mDescMagistrato = "";

	// Esperto Relatore
	private BigDecimal mCodEsperto = null;
	private String mDescEsperto = "";

	// Intervallo per Riferimenti Ordinanze
	private BigDecimal mAnnoIni = null;
	private BigDecimal mNumIni = null;
	private BigDecimal mAnnoFine = null;
	private BigDecimal mNumFine = null;

	// Intervallo per data di deposito
	private Date mDataDepositoIni = null;
	private Date mDataDepositoFine = null;

	// Intervallo per data di arrivo in cancelleria
	private Date mDataArrCancelleriaIni = null;
	private Date mDataArrCancelleriaFine = null;

	// Intervallo per data di emissione (Foglio Complementare)
	private Date mDataEmissioneIni = null;
	private Date mDataEmissioneFine = null;

	private String mCodTipoImpugnazione = "";

	// Modalità di ricerca (Per: Ordinanza, Decreto, Impugnazione, Foglio Complementare.)
	private String mModalitaRicerca = "";

	// Tipo di validazione
	private String mStatoValidazione = TUTTI;

	// Tipo di decreto
	private String mTipoDecreto = TUTTI_DECRETI;

	/*
	 * Tipo di Intervallo di ricerca Può assumere i valori: Intervallo Estremi Provvedimento, Intervallo Data
	 * Deposito.
	 */
	private String mTipoIntervalloRicerca = "";

	// Resoconto risultato

	// Indica che sono stati calcolati i contatori
	// sul risultato della ricerca
	private boolean mCalcolatiTotali = false;

	// Numero totale dei procedimenti risultato della ricerca
	private BigDecimal mNumTotali = new BigDecimal(0);
	// Numero totale dei procedimenti risultato della ricerca Annullati
	private BigDecimal mNumAnnullati = new BigDecimal(0);;
	// Numero totale dei procedimenti risultato della ricerca Non Validati
	private BigDecimal mNumNonValidati = new BigDecimal(0);;
	// Contiene resoconto dei calcoli statistici sulla ricerca effettuata
	private String mDescCalcoli = "";

	// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
	private String[] mTipiControlliEsecuzione = new String[2];

	// COSTRUTTORE DI DEFAULT
	public RicercaOrdinanzaModel() {
		// Di default la Ricerca è per Estremi Ordinanza
		mModalitaRicerca = RICERCA_ORDINANZA;
		mCalcolatiTotali = false;
	}

	// Costruttore di copia
	public RicercaOrdinanzaModel(RicercaOrdinanzaModel aModel) {
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
		mTipoDecreto = aModel.getTipoDecreto();
		mTipoIntervalloRicerca = aModel.getTipoIntervalloRicerca();
		mCalcolatiTotali = aModel.getCalcolatiTotali();
		mNumTotali = aModel.getNumTotali();
		mNumAnnullati = aModel.getNumAnnulati();
		mNumNonValidati = aModel.getNumNonValidati();
		// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
		mTipiControlliEsecuzione = aModel.getTipiControlliEsecuzione();
	}

	/*
	 * Get
	 */
	public Date getDataDepositoIniziale() {
		return mDataDepositoIni;
	}

	public Date getDataDepositoFinale() {
		return mDataDepositoFine;
	}

	public Date getDataArrivoInCancelleriaIniziale() {
		return mDataArrCancelleriaIni;
	}

	public Date getDataArrivoInCancelleriaFinale() {
		return mDataArrCancelleriaFine;
	}

	public Date getDataEmissioneIniziale() {
		return mDataEmissioneIni;
	}

	public Date getDataEmissioneFinale() {
		return mDataEmissioneFine;
	}

	public BigDecimal getAnnoIniziale() {
		return mAnnoIni;
	}

	public BigDecimal getNumIniziale() {
		return mNumIni;
	}

	public BigDecimal getAnnoFinale() {
		return mAnnoFine;
	}

	public BigDecimal getNumFinale() {
		return mNumFine;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getDescMagistrato() {
		return mDescMagistrato;
	}

	public BigDecimal getCodEsperto() {
		return mCodEsperto;
	}

	public String getDescEsperto() {
		return mDescEsperto;
	}

	public String getTipoIntervalloRicerca() {
		return mTipoIntervalloRicerca;
	}

	public String getStatoValidazione() {
		return mStatoValidazione;
	}

	public String getTipoDecreto() {
		return mTipoDecreto;
	}

	public String getModalitaRicerca() {
		return mModalitaRicerca;
	}

	public String getCodTipoImpugnazione() {
		return mCodTipoImpugnazione;
	}

	public boolean getCalcolatiTotali() {
		return mCalcolatiTotali;
	}

	public BigDecimal getNumTotali() {
		return mNumTotali;
	}

	public BigDecimal getNumAnnulati() {
		return mNumAnnullati;
	}

	public BigDecimal getNumNonValidati() {
		return mNumNonValidati;
	}

	public String getDescCalcoli() {
		return mDescCalcoli;
	}

	// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
	public String[] getTipiControlliEsecuzione() {
		return mTipiControlliEsecuzione;
	}

	public boolean isTipoIntervalloRicercaXEstremiProvvedimento() {
		boolean lRet = false;
		if (mTipoIntervalloRicerca != null
				&& mTipoIntervalloRicerca.equalsIgnoreCase(ESTREMI_PROVVEDIMENTO_INTERVALLO))
			lRet = true;
		return lRet;
	}

	public boolean isRicercaXDateDeposito() {
		boolean lRet = false;
		if (mTipoIntervalloRicerca != null
				&& mTipoIntervalloRicerca.equalsIgnoreCase(DATA_DEPOSITO_INTERVALLO))
			lRet = true;
		return lRet;
	}

	public boolean isRicercaXDateArrivoCancelleria() {
		boolean lRet = false;
		if (mTipoIntervalloRicerca != null
				&& mTipoIntervalloRicerca.equalsIgnoreCase(DATA_ARRIVO_CANCELLERIA_INTERVALLO))
			lRet = true;
		return lRet;
	}

	public boolean isRicercaXDateEmissione() {
		boolean lRet = false;
		if (mTipoIntervalloRicerca != null
				&& mTipoIntervalloRicerca.equalsIgnoreCase(DATA_EMISSIONE_INTERVALLO))
			lRet = true;
		return lRet;
	}

	public boolean isRicercaXSentenza() {
		boolean lRet = false;
		if (mModalitaRicerca != null && mModalitaRicerca.equalsIgnoreCase(RICERCA_ESTREMI_SENTENZA))
			lRet = true;
		return lRet;
	}

	public boolean isRicercaXOrdinanza() {
		boolean lRet = false;
		if (mModalitaRicerca != null && (mModalitaRicerca.equalsIgnoreCase(RICERCA_ORDINANZA)
				|| mModalitaRicerca.equalsIgnoreCase(RICERCA_ORDINANZE_PRIVE_DI_FC)))
			lRet = true;
		return lRet;
	}

	public boolean isRicercaXDecreto() {
		boolean lRet = false;
		if (mModalitaRicerca != null && mModalitaRicerca.equalsIgnoreCase(RICERCA_DECRETO))
			lRet = true;
		return lRet;
	}

	public boolean isRicercaXImpugnazioneRicorso() {
		boolean lRet = false;
		if (mModalitaRicerca != null && mModalitaRicerca.equalsIgnoreCase(RICERCA_IMPUGNAZIONE))
			lRet = true;
		return lRet;
	}

	public boolean isRicercaXFoglioComplementare() {
		boolean lRet = false;
		if (mModalitaRicerca != null && (mModalitaRicerca.equalsIgnoreCase(RICERCA_FOGLIO_COMPLEMENTARE)
				|| mModalitaRicerca.equalsIgnoreCase(RICERCA_FC_TRASMESSI)
				|| mModalitaRicerca.equalsIgnoreCase(RICERCA_FC_DA_TRASMETTERE)
				|| mModalitaRicerca.equalsIgnoreCase(RICERCA_FC_TRASMESSI_CON_ERRORE)
				|| mModalitaRicerca.equalsIgnoreCase(RICERCA_FC_ISCRITTI_MANUALMENTE)))
			lRet = true;
		return lRet;
	}

	public boolean isRicercaAnnullati() {
		boolean lRet = false;
		if (mStatoValidazione != null && mStatoValidazione.equalsIgnoreCase(ANNULLATI))
			lRet = true;
		return lRet;
	}

	public boolean isRicercaStatoTutti() {
		boolean lRet = false;
		if (mStatoValidazione != null && mStatoValidazione.equalsIgnoreCase(TUTTI))
			lRet = true;
		return lRet;
	}

	public String getDescModalitaRicerca() {
		String lRet = "";
		if (isRicercaXOrdinanza())
			lRet = "Ricerca Procedimenti per estremi Ordinanza";
		else if (isRicercaXDecreto())
			lRet = "Ricerca Procedimenti per estremi Decreto";
		else if (isRicercaXImpugnazioneRicorso())
			lRet = "Ricerca Procedimenti per estremi Ricorso o Impugnazione";
		else if (isRicercaXFoglioComplementare())
			lRet = "Ricerca Procedimenti per estremi Foglio Complementare";
		else if (isRicercaXSentenza())
			lRet = "Ricerca Procedimenti per estremi Sentenza";

		if (isTipoIntervalloRicercaXEstremiProvvedimento())
			lRet += " in intervallo Anno e Num ";
		else if (isRicercaXDateDeposito())
			lRet += " in intervallo date di deposito ";
		else if (isRicercaXDateArrivoCancelleria())
			lRet += " in intervallo date di arrivo in cancelleria ";
		else if (isRicercaXDateEmissione())
			lRet += " in intervallo date di emissione ";

		lRet += " Stato: ";
		lRet += getDescStatoValidazione();

		return lRet;
	}

	public String getDescStatoValidazione() {
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

	public String getDescTipoImpugnazione() {
		String lRet = "";
		if (isRicercaXImpugnazioneRicorso()) {
			lRet = "Tutti";
			if (getCodTipoImpugnazione().length() > 0)
				lRet = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoRicorso(),
						getCodTipoImpugnazione());
		}
		return lRet;
	}

	/**
	 * La funzione valorizza la stringa Descrizione dei calcoli statistici effettuati in base al contenuto dei
	 * contatori parziali ed al tipo di ricerca effettuato.
	 * 
	 * @return
	 */
	private String resetDescCalcoli() {
		String lRet = "";
		if (isRicercaStatoTutti() && mCalcolatiTotali) {
			lRet = "Totale atti: " + getNumTotali();
			lRet += "    Annullati: " + getNumAnnulati();
			if (isRicercaXDecreto() || isRicercaXOrdinanza() || isRicercaXSentenza())
				lRet += "   Non Validati: " + getNumNonValidati();
		}

		return lRet;
	}

	/*
	 * Set
	 */
	public void setAnnoIniziale(BigDecimal aValore) {
		mAnnoIni = aValore;
	}

	public void setAnnoFinale(BigDecimal aValore) {
		mAnnoFine = aValore;
	}

	public void setNumIniziale(BigDecimal aValore) {
		mNumIni = aValore;
	}

	public void setNumFinale(BigDecimal aValore) {
		mNumFine = aValore;
	}

	public void setAnnoNumIniziale(BigDecimal aAnno, BigDecimal aNum) {
		setAnnoIniziale(aAnno);
		setNumIniziale(aNum);
	}

	public void setAnnoNumFinale(BigDecimal aAnno, BigDecimal aNum) {
		setAnnoFinale(aAnno);
		setNumFinale(aNum);
	}

	public void setDataDepositoIniziale(Date aValore) {
		mDataDepositoIni = aValore;
	}

	public void setDataDepositoFinale(Date aValore) {
		mDataDepositoFine = aValore;
	}

	public void setDataArrivoInCancelleriaIniziale(Date aValore) {
		mDataArrCancelleriaIni = aValore;
	}

	public void setDataArrivoInCancelleriaFinale(Date aValore) {
		mDataArrCancelleriaFine = aValore;
	}

	public void setDataEmissioneIniziale(Date aValore) {
		mDataEmissioneIni = aValore;
	}

	public void setDataEmissioneFinale(Date aValore) {
		mDataEmissioneFine = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setDescMagistrato(String aValore) {
		mDescMagistrato = aValore;
	}

	public void setCodEsperto(BigDecimal aValore) {
		mCodEsperto = aValore;
	}

	public void setDescEsperto(String aValore) {
		mDescEsperto = aValore;
	}

	public void setTipoIntervalloRicerca(String aValore) {
		mTipoIntervalloRicerca = aValore;
	}

	public void setStatoValidazione(String aValore) {
		mStatoValidazione = aValore;
	}

	public void setTipoDecreto(String aValore) {
		mTipoDecreto = aValore;
	}

	public void setModalitaRicerca(String aValore) {
		mModalitaRicerca = aValore;
	}

	public void setCodTipoImpugnazione(String aValore) {
		mCodTipoImpugnazione = aValore;
	}

	public void setStatoAnnullati() {
		setStatoValidazione(ANNULLATI);
	}

	public void setStatoNonValidati() {
		setStatoValidazione(NON_VALIDATI);
	}

	public void setCalcolatiTotali(boolean aValore) {
		mCalcolatiTotali = aValore;
	}

	public void setNumTotali(BigDecimal aValore) {
		mNumTotali = aValore;
	}

	public void setNumAnnullati(BigDecimal aValore) {
		mNumAnnullati = aValore;
	}

	public void setNumNonValidati(BigDecimal aValore) {
		mNumNonValidati = aValore;
	}

	// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
	public void setTipiControlliEsecuzione(String[] aValore) {
		mTipiControlliEsecuzione = aValore;
	}

	/*
	 * Se il parametro passato è valorizzato viene usato per valorizzare la stringa che fornisce il resoconto
	 * dei calcoli effettuati nell'ultima ricerca. Se invece non è valorizzato viene chiamata la funzione che
	 * valorizza la stringa di resoconto in base ai contatori già calcolati nell'ultima ricerca.
	 * 
	 */
	public void setDescCalcoli(String aValore) {
		if (aValore != null)
			mDescCalcoli = aValore;
		else
			mDescCalcoli = resetDescCalcoli();

	}

}
