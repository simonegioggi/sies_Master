package siap.siep.penapecuniaria.model;

/**
* <p>Title: RichiestaConversioneModel</p>
* <p>Description: Classe Model che rappresenta il RichiestaConversione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.evento.model.EventoModel;

public class RichiestaConversioneModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 7652960898545828432L;
	private BigDecimal mIdRichiestaConversione;
	private BigDecimal mAnnoPartita;
	private BigDecimal mNumPartita;
	private String mNumExCampione;
	private String mProtCircosrizioneDoganale;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private Date mDataRicezioneAtto;
	private Date mDataIscrizioneAtto;
	private Date mDataEsazione;
	private BigDecimal mImportoMulta;
	private Date mDataPrescrizioneMulta;
	private String mFlagImprescrittibileMulta;
	private BigDecimal mImportoAmmenda;
	private Date mDataPrescrizioneAmmenda;
	private String mFlagImprescrittibileAmmenda;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mEveIdEvento;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSiuIdFascicoloSius;
	private BigDecimal mDurataEsitoAnni;
	private BigDecimal mDurataEsitoMesi;
	private BigDecimal mDurataEsitoGiorni;
	private BigDecimal mNumeroRate;
	private BigDecimal mValoreRata;
	private BigDecimal mValoreUltimaRata;
	private Date mDataAnnullamento;
	private String mCodTipoSanzione;
	private String mDescrTipoSanzione;
	private String mNote;
	private Date mDataDeposito;
	private Date mDataInizioPagamento;
	private BigDecimal mNumeroGiorniInizioPagamento;

	private Date mDataIrrevocabilita;
	private EventoModel mEventoModel;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public RichiestaConversioneModel() {
		this.mIdRichiestaConversione = null;
		this.mAnnoPartita = null;
		this.mNumPartita = null;
		this.mNumExCampione = "";
		this.mProtCircosrizioneDoganale = "";
		this.mCodTipoAutoritaEmittente = "";
		this.mDescrTipoAutoritaEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";
		this.mDataRicezioneAtto = null;
		this.mDataIscrizioneAtto = null;
		this.mDataEsazione = null;
		this.mImportoMulta = null;
		this.mDataPrescrizioneMulta = null;
		this.mFlagImprescrittibileMulta = "";
		this.mImportoAmmenda = null;
		this.mDataPrescrizioneAmmenda = null;
		this.mFlagImprescrittibileAmmenda = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mEveIdEvento = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSiuIdFascicoloSius = null;
		this.mDurataEsitoAnni = null;
		this.mDurataEsitoMesi = null;
		this.mDurataEsitoGiorni = null;
		this.mNumeroRate = null;
		this.mValoreRata = null;
		this.mValoreUltimaRata = null;
		this.mDataAnnullamento = null;
		this.mCodTipoSanzione = "-";
		this.mDescrTipoSanzione = "";
		this.mNote = "";
		this.mDataDeposito = null;
		this.mDataInizioPagamento = null;
		this.mNumeroGiorniInizioPagamento = null;

		this.mDataIrrevocabilita = null;
		this.mEventoModel = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public RichiestaConversioneModel(RichiestaConversioneModel aModel) {
		this.mIdRichiestaConversione = aModel.mIdRichiestaConversione;
		this.mAnnoPartita = aModel.mAnnoPartita;
		this.mNumPartita = aModel.mNumPartita;
		this.mNumExCampione = aModel.mNumExCampione;
		this.mProtCircosrizioneDoganale = aModel.mProtCircosrizioneDoganale;
		this.mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mDataRicezioneAtto = aModel.mDataRicezioneAtto;
		this.mDataIscrizioneAtto = aModel.mDataIscrizioneAtto;
		this.mDataEsazione = aModel.mDataEsazione;
		this.mImportoMulta = aModel.mImportoMulta;
		this.mDataPrescrizioneMulta = aModel.mDataPrescrizioneMulta;
		this.mFlagImprescrittibileMulta = aModel.mFlagImprescrittibileMulta;
		this.mImportoAmmenda = aModel.mImportoAmmenda;
		this.mDataPrescrizioneAmmenda = aModel.mDataPrescrizioneAmmenda;
		this.mFlagImprescrittibileAmmenda = aModel.mFlagImprescrittibileAmmenda;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mDurataEsitoAnni = aModel.mDurataEsitoAnni;
		this.mDurataEsitoMesi = aModel.mDurataEsitoMesi;
		this.mDurataEsitoGiorni = aModel.mDurataEsitoGiorni;
		this.mNumeroRate = aModel.mNumeroRate;
		this.mValoreRata = aModel.mValoreRata;
		this.mValoreUltimaRata = aModel.mValoreUltimaRata;
		this.mDataAnnullamento = aModel.mDataAnnullamento;
		this.mCodTipoSanzione = aModel.mCodTipoSanzione;
		this.mDescrTipoSanzione = aModel.mDescrTipoSanzione;
		this.mNote = aModel.mNote;
		this.mDataDeposito = aModel.mDataDeposito;
		this.mDataInizioPagamento = aModel.mDataInizioPagamento;
		this.mNumeroGiorniInizioPagamento = aModel.mNumeroGiorniInizioPagamento;

		this.mDataIrrevocabilita = aModel.mDataIrrevocabilita;
		this.mEventoModel = aModel.mEventoModel;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public RichiestaConversioneModel(BigDecimal aIdRichiestaConversione, BigDecimal aAnnoPartita,
			BigDecimal aNumPartita, String aNumExCampione, String aProtCircosrizioneDoganale,
			String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente, String aCodLuogoEmittente,
			String aDescrLuogoEmittente, Date aDataRicezioneAtto, Date aDataIscrizioneAtto,
			Date aDataEsazione, BigDecimal aImportoMulta, Date aDataPrescrizioneMulta,
			String aFlagImprescrittibileMulta, BigDecimal aImportoAmmenda, Date aDataPrescrizioneAmmenda,
			String aFlagImprescrittibileAmmenda, BigDecimal aFasSieIdFascicoloSiep, BigDecimal aEveIdEvento,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, BigDecimal aFasSiuIdFascicoloSius, BigDecimal aDurataEsitoAnni,
			BigDecimal aDurataEsitoMesi, BigDecimal aDurataEsitoGiorni, BigDecimal aNumeroRate,
			BigDecimal aValoreRata, BigDecimal aValoreUltimaRata, Date aDataAnnullamento,
			String aCodTipoSanzione, String aDescrTipoSanzione, String aNote, Date aDataDeposito,
			Date aDataInizioPagamento, BigDecimal aNumeroGiorniInizioPagamento)

	{
		this.mIdRichiestaConversione = aIdRichiestaConversione;
		this.mAnnoPartita = aAnnoPartita;
		this.mNumPartita = aNumPartita;
		this.mNumExCampione = aNumExCampione;
		this.mProtCircosrizioneDoganale = aProtCircosrizioneDoganale;
		this.mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mDataRicezioneAtto = aDataRicezioneAtto;
		this.mDataIscrizioneAtto = aDataIscrizioneAtto;
		this.mDataEsazione = aDataEsazione;
		this.mImportoMulta = aImportoMulta;
		this.mDataPrescrizioneMulta = aDataPrescrizioneMulta;
		this.mFlagImprescrittibileMulta = aFlagImprescrittibileMulta;
		this.mImportoAmmenda = aImportoAmmenda;
		this.mDataPrescrizioneAmmenda = aDataPrescrizioneAmmenda;
		this.mFlagImprescrittibileAmmenda = aFlagImprescrittibileAmmenda;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mEveIdEvento = aEveIdEvento;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		// this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mDurataEsitoAnni = aDurataEsitoAnni;
		this.mDurataEsitoMesi = aDurataEsitoMesi;
		this.mDurataEsitoGiorni = aDurataEsitoGiorni;
		this.mNumeroRate = aNumeroRate;
		this.mValoreRata = aValoreRata;
		this.mValoreUltimaRata = aValoreUltimaRata;
		this.mDataAnnullamento = aDataAnnullamento;
		this.mCodTipoSanzione = aCodTipoSanzione;
		this.mDescrTipoSanzione = aDescrTipoSanzione;
		this.mNote = aNote;
		this.mDataDeposito = aDataDeposito;
		this.mDataInizioPagamento = aDataInizioPagamento;
		this.mNumeroGiorniInizioPagamento = aNumeroGiorniInizioPagamento;

		this.mEventoModel = null;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdRichiestaConversione() {
		return mIdRichiestaConversione;
	}

	public BigDecimal getAnnoPartita() {
		return mAnnoPartita;
	}

	public BigDecimal getNumPartita() {
		return mNumPartita;
	}

	public String getNumExCampione() {
		return mNumExCampione;
	}

	public String getProtCircosrizioneDoganale() {
		return mProtCircosrizioneDoganale;
	}

	public String getCodTipoAutoritaEmittente() {
		return mCodTipoAutoritaEmittente;
	}

	public String getDescrTipoAutoritaEmittente() {
		return mDescrTipoAutoritaEmittente;
	}

	public String getCodLuogoEmittente() {
		return mCodLuogoEmittente;
	}

	public String getDescrLuogoEmittente() {
		return mDescrLuogoEmittente;
	}

	public Date getDataRicezioneAtto() {
		return mDataRicezioneAtto;
	}

	public Date getDataIscrizioneAtto() {
		return mDataIscrizioneAtto;
	}

	public Date getDataEsazione() {
		return mDataEsazione;
	}

	public BigDecimal getImportoMulta() {
		return mImportoMulta;
	}

	public Date getDataPrescrizioneMulta() {
		return mDataPrescrizioneMulta;
	}

	public String getFlagImprescrittibileMulta() {
		return mFlagImprescrittibileMulta;
	}

	public BigDecimal getImportoAmmenda() {
		return mImportoAmmenda;
	}

	public Date getDataPrescrizioneAmmenda() {
		return mDataPrescrizioneAmmenda;
	}

	public String getFlagImprescrittibileAmmenda() {
		return mFlagImprescrittibileAmmenda;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
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

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public BigDecimal getDurataEsitoAnni() {
		return mDurataEsitoAnni;
	}

	public BigDecimal getDurataEsitoMesi() {
		return mDurataEsitoMesi;
	}

	public BigDecimal getDurataEsitoGiorni() {
		return mDurataEsitoGiorni;
	}

	public BigDecimal getNumeroRate() {
		return mNumeroRate;
	}

	public BigDecimal getValoreRata() {
		return mValoreRata;
	}

	public BigDecimal getValoreUltimaRata() {
		return mValoreUltimaRata;
	}

	public Date getDataAnnullamento() {
		return mDataAnnullamento;
	}

	public String getCodTipoSanzione() {
		return mCodTipoSanzione;
	}

	public String getDescrTipoSanzione() {
		return mDescrTipoSanzione;
	}

	public String getNote() {
		return mNote;
	}

	public Date getDataDeposito() {
		return mDataDeposito;
	}

	public Date getDataInizioPagamento() {
		return mDataInizioPagamento;
	}

	public BigDecimal getNumeroGiorniInizioPagamento() {
		return mNumeroGiorniInizioPagamento;
	}

	public Date getDataIrrevocabilita() {
		return mDataIrrevocabilita;
	}

	public EventoModel getEventoModel() {
		return mEventoModel;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdRichiestaConversione(BigDecimal aValore) {
		mIdRichiestaConversione = aValore;
	}

	public void setAnnoPartita(BigDecimal aValore) {
		mAnnoPartita = aValore;
	}

	public void setNumPartita(BigDecimal aValore) {
		mNumPartita = aValore;
	}

	public void setNumExCampione(String aValore) {
		mNumExCampione = aValore;
	}

	public void setProtCircosrizioneDoganale(String aValore) {
		mProtCircosrizioneDoganale = aValore;
	}

	public void setCodTipoAutoritaEmittente(String aValore) {
		mCodTipoAutoritaEmittente = aValore;
	}

	public void setDescrTipoAutoritaEmittente(String aValore) {
		mDescrTipoAutoritaEmittente = aValore;
	}

	public void setCodLuogoEmittente(String aValore) {
		mCodLuogoEmittente = aValore;
	}

	public void setDescrLuogoEmittente(String aValore) {
		mDescrLuogoEmittente = aValore;
	}

	public void setDataRicezioneAtto(Date aValore) {
		mDataRicezioneAtto = aValore;
	}

	public void setDataIscrizioneAtto(Date aValore) {
		mDataIscrizioneAtto = aValore;
	}

	public void setDataEsazione(Date aValore) {
		mDataEsazione = aValore;
	}

	public void setImportoMulta(BigDecimal aValore) {
		mImportoMulta = aValore;
	}

	public void setDataPrescrizioneMulta(Date aValore) {
		mDataPrescrizioneMulta = aValore;
	}

	public void setFlagImprescrittibileMulta(String aValore) {
		mFlagImprescrittibileMulta = aValore;
	}

	public void setImportoAmmenda(BigDecimal aValore) {
		mImportoAmmenda = aValore;
	}

	public void setDataPrescrizioneAmmenda(Date aValore) {
		mDataPrescrizioneAmmenda = aValore;
	}

	public void setFlagImprescrittibileAmmenda(String aValore) {
		mFlagImprescrittibileAmmenda = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setDurataEsitoAnni(BigDecimal aValore) {
		mDurataEsitoAnni = aValore;
	}

	public void setDurataEsitoMesi(BigDecimal aValore) {
		mDurataEsitoMesi = aValore;
	}

	public void setDurataEsitoGiorni(BigDecimal aValore) {
		mDurataEsitoGiorni = aValore;
	}

	public void setNumeroRate(BigDecimal aValore) {
		mNumeroRate = aValore;
	}

	public void setValoreRata(BigDecimal aValore) {
		mValoreRata = aValore;
	}

	public void setValoreUltimaRata(BigDecimal aValore) {
		mValoreUltimaRata = aValore;
	}

	public void setDataAnnullamento(Date aValore) {
		mDataAnnullamento = aValore;
	}

	public void setCodTipoSanzione(String aValore) {
		mCodTipoSanzione = aValore;
	}

	public void setDescrTipoSanzione(String aValore) {
		mDescrTipoSanzione = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setDataDeposito(Date aValore) {
		mDataDeposito = aValore;
	}

	public void setDataInizioPagamento(Date aValore) {
		mDataInizioPagamento = aValore;
	}

	public void setNumeroGiorniInizioPagamento(BigDecimal aValore) {
		mNumeroGiorniInizioPagamento = aValore;
	}

	public void setDataIrrevocabilita(Date aValore) {
		mDataIrrevocabilita = aValore;
	}

	public void setEventoModel(EventoModel aValore) {
		mEventoModel = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "RichiestaConversioneModel:\n" + "[ mIdRichiestaConversione      = " + mIdRichiestaConversione
				+ " ]\n" + "[ mAnnoPartita                 = " + mAnnoPartita + " ]\n"
				+ "[ mNumPartita                  = " + mNumPartita + " ]\n"
				+ "[ mNumExCampione               = " + mNumExCampione + " ]\n"
				+ "[ mProtCircosrizioneDoganale   = " + mProtCircosrizioneDoganale + " ]\n"
				+ "[ mCodTipoAutoritaEmittente    = " + mCodTipoAutoritaEmittente + " ]\n"
				+ "[ mCodLuogoEmittente           = " + mCodLuogoEmittente + " ]\n"
				+ "[ mDataRicezioneAtto           = " + mDataRicezioneAtto + " ]\n"
				+ "[ mDataIscrizioneAtto          = " + mDataIscrizioneAtto + " ]\n"
				+ "[ mDataEsazione                = " + mDataEsazione + " ]\n"
				+ "[ mImportoMulta                = " + mImportoMulta + " ]\n"
				+ "[ mDataPrescrizioneMulta       = " + mDataPrescrizioneMulta + " ]\n"
				+ "[ mFlagImprescrittibileMulta   = " + mFlagImprescrittibileMulta + " ]\n"
				+ "[ mImportoAmmenda              = " + mImportoAmmenda + " ]\n"
				+ "[ mDataPrescrizioneAmmenda     = " + mDataPrescrizioneAmmenda + " ]\n"
				+ "[ mFlagImprescrittibileAmmenda = " + mFlagImprescrittibileAmmenda + " ]\n"
				+ "[ mFasSieIdFascicoloSiep       = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mEveIdEvento                 = " + mEveIdEvento + " ]\n"
				+ "[ mCodOperatoreInserimento     = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento             = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento       = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento   = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento           = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento     = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mFasSiuIdFascicoloSius     	 = " + mFasSiuIdFascicoloSius + " ]\n"
				+ "[ mDurataEsitoAnni     				 = " + mDurataEsitoAnni + " ]\n"
				+ "[ mDurataEsitoMesi     				 = " + mDurataEsitoMesi + " ]\n"
				+ "[ mDurataEsitoGiorni     			 = " + mDurataEsitoGiorni + " ]\n"
				+ "[ mNumeroRate     						 = " + mNumeroRate + " ]\n"
				+ "[ mValoreRata     						 = " + mValoreRata + " ]\n"
				+ "[ mValoreUltimaRata     			 = " + mValoreUltimaRata + " ]\n"
				+ "[ mDataAnnullamento     			 = " + mDataAnnullamento + " ]\n"
				+ "[ mCodTipoSanzione     				 = " + mCodTipoSanzione + " ]\n"
				+ "[ mNote     				 					 = " + mNote + " ]\n"
				+ "[ mDataDeposito			 					 = " + mDataDeposito + " ]\n"
				+ "[ mDataInizioPagamento				 = " + mDataInizioPagamento + " ]\n"
				+ "[ mNumeroGiorniInizioPagamento = " + mNumeroGiorniInizioPagamento + " ]\n" +

				"[ mDataIrrevocabilita    			 = " + mDataIrrevocabilita + " ]\n" + "" + mEventoModel;

		return lStr;
	}
}
