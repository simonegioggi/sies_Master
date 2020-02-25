package siap.siep.modulocumulo.model;

/**
* <p>Title: PenaComplessivaumuloModel</p>
* <p>Description: Classe Model che rappresenta il PenaComplessiva</p>
* <p> in ambito Cumulo (Pena_complessiva_cumulo) </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import f3b.model.GenericModel;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;

public class PenaComplessivaCumuloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -1844488789023834477L;
	private BigDecimal mIdPenaComplessivaCum;
	private String mCodTipoPenaDetentiva;
	private String mDescrTipoPenaDetentiva;

	private BigDecimal mNumAnniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mImportoMulta;

	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumGiorniArresto;
	private BigDecimal mImportoAmmenda;

	private BigDecimal mNumAnniIsolamentoDiurno;
	private BigDecimal mNumMesiIsolamentoDiurno;
	private BigDecimal mNumGiorniIsolamentoDiurno;

	private Date mDataPrescrizione;

	private String mFlagPenaInContinuazione;

	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mTitIdTitoloCumulato;
	private BigDecimal mIdPenaComplessivaOrigine;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private SanzioneSostitutivaCumuloModel mSanzioneSostitutivaCumulo;
	private List<ContinuazioneCumuloModel> mListaContinuazioniCumulo;

	/**
	 * L'attributo mPenaResidua, se valorizzato, contiene l'ultima pena residua validata associata al
	 * fascicolo. L'ultima pena residua contiene il campo flag ergastolo, che viene valorizzato anche se
	 * l'ergastolo viene dato in cumulo. Il nuovo attributo mPenaResidua viene valorizzato contestualemente
	 * alla valorizzazione del PenaComplessivaModel.
	 */
	private PenaResiduaModel mPenaResidua;

	private String mStringaArresto;
	private String mStringaReclusione;
	private String mStringaIsolamentoDiurno;

	// COSTRUTTORE DI DEFAULT
	public PenaComplessivaCumuloModel() {
		this.mIdPenaComplessivaCum = null;
		this.mCodTipoPenaDetentiva = null;
		this.mDescrTipoPenaDetentiva = null;

		this.mNumAnniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumGiorniReclusione = null;
		this.mImportoMulta = null;

		this.mNumAnniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumGiorniArresto = null;
		this.mImportoAmmenda = null;

		this.mNumAnniIsolamentoDiurno = null;
		this.mNumMesiIsolamentoDiurno = null;
		this.mNumGiorniIsolamentoDiurno = null;

		this.mDataPrescrizione = null;

		this.mFlagPenaInContinuazione = null;

		this.mFlagStato = null;
		this.mMotivoModifica = null;
		this.mTitIdTitoloCumulato = null;
		this.mIdPenaComplessivaOrigine = null;

		this.mCodOperatoreInserimento = null;
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = null;
		this.mCodOperatoreAggiornamento = null;
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = null;

		this.mSanzioneSostitutivaCumulo = null;
		this.mListaContinuazioniCumulo = null;

		this.mPenaResidua = null;

	}

	// COSTRUTTORE DI COPIA
	public PenaComplessivaCumuloModel(PenaComplessivaCumuloModel aModel) {
		this.mIdPenaComplessivaCum = aModel.mIdPenaComplessivaCum;
		this.mCodTipoPenaDetentiva = aModel.mCodTipoPenaDetentiva;
		this.mDescrTipoPenaDetentiva = aModel.mDescrTipoPenaDetentiva;

		this.mNumAnniReclusione = aModel.mNumAnniReclusione;
		this.mNumMesiReclusione = aModel.mNumMesiReclusione;
		this.mNumGiorniReclusione = aModel.mNumGiorniReclusione;
		this.mImportoMulta = aModel.mImportoMulta;

		this.mNumAnniArresto = aModel.mNumAnniArresto;
		this.mNumMesiArresto = aModel.mNumMesiArresto;
		this.mNumGiorniArresto = aModel.mNumGiorniArresto;
		this.mImportoAmmenda = aModel.mImportoAmmenda;

		this.mNumAnniIsolamentoDiurno = aModel.mNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aModel.mNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aModel.mNumGiorniIsolamentoDiurno;

		this.mDataPrescrizione = aModel.mDataPrescrizione;

		this.mFlagPenaInContinuazione = aModel.mFlagPenaInContinuazione;

		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mIdPenaComplessivaOrigine = aModel.mIdPenaComplessivaOrigine;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;

		this.mPenaResidua = aModel.mPenaResidua;
	}

	// COSTRUTTORE MODEL
	public PenaComplessivaCumuloModel(BigDecimal aIdPenaComplessivaCum, String aCodTipoPenaDetentiva,
			String aDescrTipoPenaDetentiva,

			BigDecimal aNumAnniReclusione, BigDecimal aNumMesiReclusione, BigDecimal aNumGiorniReclusione,
			BigDecimal aImportoMulta,

			BigDecimal aNumAnniArresto, BigDecimal aNumMesiArresto, BigDecimal aNumGiorniArresto,
			BigDecimal aImportoAmmenda,

			BigDecimal aNumAnniIsolamentoDiurno, BigDecimal aNumMesiIsolamentoDiurno,
			BigDecimal aNumGiorniIsolamentoDiurno,

			Date aDataPrescrizione,

			String aFlagPenaInContinuazione,

			String aFlagStato, String aMotivoModifica, BigDecimal aTitIdTitoloCumulato,
			BigDecimal aIdPenaComplessivaOrigine,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdPenaComplessivaCum = aIdPenaComplessivaCum;
		this.mCodTipoPenaDetentiva = aCodTipoPenaDetentiva;
		this.mDescrTipoPenaDetentiva = aDescrTipoPenaDetentiva;
		this.mNumAnniReclusione = aNumAnniReclusione;
		this.mNumMesiReclusione = aNumMesiReclusione;
		this.mNumGiorniReclusione = aNumGiorniReclusione;
		this.mImportoMulta = aImportoMulta;
		this.mNumAnniArresto = aNumAnniArresto;
		this.mNumMesiArresto = aNumMesiArresto;
		this.mNumGiorniArresto = aNumGiorniArresto;
		this.mImportoAmmenda = aImportoAmmenda;

		this.mNumAnniIsolamentoDiurno = aNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aNumGiorniIsolamentoDiurno;

		this.mDataPrescrizione = aDataPrescrizione;

		this.mFlagPenaInContinuazione = aFlagPenaInContinuazione;

		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mIdPenaComplessivaOrigine = aIdPenaComplessivaOrigine;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;

		this.mPenaResidua = null;
	}

	/**
	 * Costruttore di inizializzazione a partire da PenaComplessivaModel Inizializza solo i dati di interesse
	 * 
	 * @param lPenMod
	 */
	public PenaComplessivaCumuloModel(PenaComplessivaModel lPenMod) {
		this.mCodTipoPenaDetentiva = lPenMod.getCodTipoPenaDetentiva();

		this.mNumAnniReclusione = lPenMod.getNumAnniReclusione();
		this.mNumMesiReclusione = lPenMod.getNumMesiReclusione();
		this.mNumGiorniReclusione = lPenMod.getNumGiorniReclusione();
		this.mImportoMulta = lPenMod.getImportoMulta();

		this.mNumAnniArresto = lPenMod.getNumAnniArresto();
		this.mNumMesiArresto = lPenMod.getNumMesiArresto();
		this.mNumGiorniArresto = lPenMod.getNumGiorniArresto();
		this.mImportoAmmenda = lPenMod.getImportoAmmenda();

		this.mNumAnniIsolamentoDiurno = lPenMod.getNumAnniIsolamentoDiurno();
		this.mNumMesiIsolamentoDiurno = lPenMod.getNumMesiIsolamentoDiurno();
		this.mNumGiorniIsolamentoDiurno = lPenMod.getNumGiorniIsolamentoDiurno();

		this.mDataPrescrizione = lPenMod.getDataPrescrizione();

		this.mFlagPenaInContinuazione = lPenMod.getFlagPenaInContinuazione();

		this.mIdPenaComplessivaOrigine = lPenMod.getIdPenaComplessiva();
	}

	/**
	 * L'attributo mPenaResidua, se valorizzato, contiene l'ultima pena residua validata associata al
	 * fascicolo. L'ultima pena residua contiene il campo flag ergastolo, che viene valorizzato anche se
	 * l'ergastolo viene dato in cumulo. Il nuovo attributo mPenaResidua viene valorizzato contestualemente
	 * alla valorizzazione del PenaComplessivaModel.
	 * 
	 * I metodi che attualmente in tutta l'applicazione (classi, jsp, template) vengono testati per
	 * controllare se la pena è in ergastolo oppure no sono : getCodTipoPenaDetentiva() e
	 * getDescrTipoPenaDetentiva(). In questi metodi il valore del flag ergastolo su pena residua è
	 * prioritario sul tipo pena detentiva della pena complessiva
	 * 
	 * @return il cod tipo pena detentiva "logico", non quello che sta sul DB ("fisico")
	 * 
	 */

	//
	// METODI GET()
	//
	public BigDecimal getIdPenaComplessivaCum() {
		return mIdPenaComplessivaCum;
	}

	public String getCodTipoPenaDetentiva() {
		if (getPenaResidua() == null) {
			return mCodTipoPenaDetentiva;
		} else {
			PenaResiduaModel lUltimaPenaResidua = getPenaResidua();
			String lFlagErgastolo = lUltimaPenaResidua.getFlagErgastolo();

			if (lFlagErgastolo == null || "N".equals(lFlagErgastolo)) {
				return "-";
			} else if ("S".equals(lFlagErgastolo)) {
				return "03"; // ERGASTOLO
			} else if ("D".equals(lFlagErgastolo)) {
				return "04"; // ERGASTOLO CON ISOLAMENTO DIURNO
			}
		}

		return mCodTipoPenaDetentiva;
	}

	/**
	 * L'attributo mPenaResidua, se valorizzato, contiene l'ultima pena residua validata associata al
	 * fascicolo. L'ultima pena residua contiene il campo flag ergastolo, che viene valorizzato anche se
	 * l'ergastolo viene dato in cumulo. Il nuovo attributo mPenaResidua viene valorizzato contestualemente
	 * alla valorizzazione del PenaComplessivaModel.
	 * 
	 * I metodi che attualmente in tutta l'applicazione (classi, jsp, template) vengono testati per
	 * controllare se la pena è in ergastolo oppure no sono : getCodTipoPenaDetentiva() e
	 * getDescrTipoPenaDetentiva(). In questi metodi il valore del flag ergastolo su pena residua è
	 * prioritario sul tipo pena detentiva della pena complessiva
	 * 
	 * @return la descrizione del cod tipo pena detentiva "logica", non quella relativa al DB ("fisica")
	 * 
	 */
	public String getDescrTipoPenaDetentiva() {
		if (getPenaResidua() == null) {
			return mDescrTipoPenaDetentiva;
		} else {
			String lCodTipoPenaDetentiva = getCodTipoPenaDetentiva();

			if (lCodTipoPenaDetentiva != null) {
				if ("03".equals(lCodTipoPenaDetentiva)) {
					return "Ergastolo";
				} else if ("04".equals(lCodTipoPenaDetentiva)) {
					return "Ergastolo con Isolamento Diurno";
				} else {
					return "-";
				}
			}
		}

		return mDescrTipoPenaDetentiva;
	}

	/**
	 * Ritorna il cod tipo pena detentiva "fisica" a differenza del metodo getCodTipoPenaDetentiva() che
	 * ritorna quello "logico"
	 * 
	 * @return il cod tipo pena detentiva "fisico", non quello "logico" ritornato dal metodo
	 *         getCodTipoPenaDetentiva()
	 * 
	 */
	public String getCodTipoPenaDetentivaDB() {
		return mCodTipoPenaDetentiva;
	}

	/**
	 * Ritorna la descrizione del cod tipo pena detentiva "fisico" a differenza del metodo
	 * getDescrTipoPenaDetentiva() che ritorna quella "logica"
	 * 
	 * @return la descrizione del cod tipo pena detentiva "fisica", non quello "logica" ritornato dal metodo
	 *         getDescrTipoPenaDetentiva()
	 * 
	 */
	public String getDescrTipoPenaDetentivaDB() {
		return mDescrTipoPenaDetentiva;
	}

	public BigDecimal getNumAnniReclusione() {
		return mNumAnniReclusione;
	}

	public BigDecimal getNumMesiReclusione() {
		return mNumMesiReclusione;
	}

	public BigDecimal getNumGiorniReclusione() {
		return mNumGiorniReclusione;
	}

	public String getStringaIsolamentoDiurno() {
		return mStringaIsolamentoDiurno;
	}

	// public BigDecimal getImportoMulta() { return mImportoMulta; }
	public BigDecimal getImportoMulta() {
		if (mImportoMulta != null)
			return mImportoMulta;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumAnniArresto() {
		return mNumAnniArresto;
	}

	public BigDecimal getNumMesiArresto() {
		return mNumMesiArresto;
	}

	public BigDecimal getNumGiorniArresto() {
		return mNumGiorniArresto;
	}

	// public BigDecimal getImportoAmmenda() { return mImportoAmmenda; }
	public BigDecimal getImportoAmmenda() {
		if (mImportoAmmenda != null)
			return mImportoAmmenda;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumAnniIsolamentoDiurno() {
		return mNumAnniIsolamentoDiurno;
	}

	public BigDecimal getNumMesiIsolamentoDiurno() {
		return mNumMesiIsolamentoDiurno;
	}

	public BigDecimal getNumGiorniIsolamentoDiurno() {
		return mNumGiorniIsolamentoDiurno;
	}

	public Date getDataPrescrizione() {
		return mDataPrescrizione;
	}

	public String getFlagPenaInContinuazione() {
		return mFlagPenaInContinuazione;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	public BigDecimal getIdPenaComplessivaOrigine() {
		return mIdPenaComplessivaOrigine;
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

	public SanzioneSostitutivaCumuloModel getSanzioneSostitutivaCumulo() {
		return mSanzioneSostitutivaCumulo;
	}

	public List<ContinuazioneCumuloModel> getContinuazioniCumulo() {
		return mListaContinuazioniCumulo;
	}

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	public PenaResiduaModel getPenaResidua() {
		return mPenaResidua;
	}

	//
	// METODI SET()
	//
	public void setIdPenaComplessivaCum(BigDecimal aValore) {
		mIdPenaComplessivaCum = aValore;
	}

	public void setCodTipoPenaDetentiva(String aValore) {
		mCodTipoPenaDetentiva = aValore;
	}

	public void setDescrTipoPenaDetentiva(String aValore) {
		mDescrTipoPenaDetentiva = aValore;
	}

	public void setNumAnniReclusione(BigDecimal aValore) {
		mNumAnniReclusione = aValore;
	}

	public void setNumMesiReclusione(BigDecimal aValore) {
		mNumMesiReclusione = aValore;
	}

	public void setNumGiorniReclusione(BigDecimal aValore) {
		mNumGiorniReclusione = aValore;
	}

	public void setImportoMulta(BigDecimal aValore) {
		mImportoMulta = aValore;
	}

	public void setNumAnniArresto(BigDecimal aValore) {
		mNumAnniArresto = aValore;
	}

	public void setNumMesiArresto(BigDecimal aValore) {
		mNumMesiArresto = aValore;
	}

	public void setNumGiorniArresto(BigDecimal aValore) {
		mNumGiorniArresto = aValore;
	}

	public void setImportoAmmenda(BigDecimal aValore) {
		mImportoAmmenda = aValore;
	}

	public void setNumAnniIsolamentoDiurno(BigDecimal aValore) {
		mNumAnniIsolamentoDiurno = aValore;
	}

	public void setNumMesiIsolamentoDiurno(BigDecimal aValore) {
		mNumMesiIsolamentoDiurno = aValore;
	}

	public void setNumGiorniIsolamentoDiurno(BigDecimal aValore) {
		mNumGiorniIsolamentoDiurno = aValore;
	}

	public void setDataPrescrizione(Date aValore) {
		mDataPrescrizione = aValore;
	}

	public void setFlagPenaInContinuazione(String aValore) {
		mFlagPenaInContinuazione = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public void setIdPenaComplessivaOrigine(BigDecimal aValore) {
		mIdPenaComplessivaOrigine = aValore;
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

	public void setSanzioneSostitutivaCumulo(SanzioneSostitutivaCumuloModel aValore) {
		mSanzioneSostitutivaCumulo = aValore;
	}

	public void setContinuazioniCumulo(List<ContinuazioneCumuloModel> aValore) {
		mListaContinuazioniCumulo = aValore;
	}

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setPenaResidua(PenaResiduaModel aValore) {
		mPenaResidua = aValore;
	}

	public boolean isQuantumArrestoZero() {
		if ((mNumAnniArresto != null && mNumAnniArresto.intValue() > 0)
				|| (mNumMesiArresto != null && mNumMesiArresto.intValue() > 0)
				|| (mNumGiorniArresto != null && mNumGiorniArresto.intValue() > 0))
			return false;
		else
			return true;
	}

	public boolean isQuantumReclusioneZero() {
		if ((mNumAnniReclusione != null && mNumAnniReclusione.intValue() > 0)
				|| (mNumMesiReclusione != null && mNumMesiReclusione.intValue() > 0)
				|| (mNumGiorniReclusione != null && mNumGiorniReclusione.intValue() > 0))
			return false;
		else
			return true;
	}

	public boolean isDurataIsolamentoDiurnoZero() {
		if ((mNumAnniIsolamentoDiurno != null && mNumAnniIsolamentoDiurno.intValue() > 0)
				|| (mNumMesiIsolamentoDiurno != null && mNumMesiIsolamentoDiurno.intValue() > 0)
				|| (mNumGiorniIsolamentoDiurno != null && mNumGiorniIsolamentoDiurno.intValue() > 0))
			return false;
		else
			return true;
	}

	public boolean isErgastolo() {
		if ("03".equals(getCodTipoPenaDetentiva()) || "04".equals(getCodTipoPenaDetentiva()))
			return true;
		else
			return false;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "PenaComplessivaCumuloModel:\n" + "[ mIdPenaComplessivaCum      = " + mIdPenaComplessivaCum
				+ " ]\n" + "[ mCodTipoPenaDetentiva      = " + mCodTipoPenaDetentiva + " ]\n"
				+ "[ mNumAnniReclusione         = " + mNumAnniReclusione + " ]\n"
				+ "[ mNumMesiReclusione         = " + mNumMesiReclusione + " ]\n"
				+ "[ mNumGiorniReclusione       = " + mNumGiorniReclusione + " ]\n"
				+ "[ mImportoMulta              = " + mImportoMulta + " ]\n"
				+ "[ mNumAnniArresto            = " + mNumAnniArresto + " ]\n"
				+ "[ mNumMesiArresto            = " + mNumMesiArresto + " ]\n"
				+ "[ mNumGiorniArresto          = " + mNumGiorniArresto + " ]\n"
				+ "[ mImportoAmmenda            = " + mImportoAmmenda + " ]\n"
				+ "[ mNumAnniIsolamentoDiurno   = " + mNumAnniIsolamentoDiurno + " ]\n"
				+ "[ mNumMesiIsolamentoDiurno   = " + mNumMesiIsolamentoDiurno + " ]\n"
				+ "[ mNumGiorniIsolamentoDiurno = " + mNumGiorniIsolamentoDiurno + " ]\n"
				+ "[ mFlagPenaInContinuazione   = " + mFlagPenaInContinuazione + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mDataPrescrizione          = " + mDataPrescrizione + " ]\n"
				+ "[ mFlagStato                 = " + mFlagStato + " ]\n" + "[ mMotivoModifica            = "
				+ mMotivoModifica + " ]\n" + "[ mTitIdTitoloCumulato       = " + mTitIdTitoloCumulato + " ]\n"
				+ "[ mIdPenaComplessivaOrigine  = " + mIdPenaComplessivaOrigine + " ]";
		return lStr;
	}

	/**
	 * calcolaStringaArresto per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaArrestoCum() {
		String lStringArresto = "";
		if (this.getNumAnniArresto() != null) {
			if (this.getNumAnniArresto().intValue() != 0)
				lStringArresto = "Anni " + this.getNumAnniArresto();
		}
		if (this.getNumMesiArresto() != null) {
			if (this.getNumMesiArresto().intValue() != 0)
				lStringArresto += " Mesi " + this.getNumMesiArresto();
		}
		if (this.getNumGiorniArresto() != null) {
			if (this.getNumGiorniArresto().intValue() != 0)
				lStringArresto += " Giorni " + this.getNumGiorniArresto();
		}

		if (lStringArresto.length() > 1)
			this.mStringaArresto = lStringArresto;
		else
			this.mStringaArresto = null;
	}

	/**
	 * calcolaStringaReclusione per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaReclusioneCum() {
		String lStringReclusione = "";
		if (this.getNumAnniReclusione() != null) {
			if (this.getNumAnniReclusione().intValue() != 0)
				lStringReclusione = "Anni " + this.getNumAnniReclusione();
		}
		if (this.getNumMesiReclusione() != null) {
			if (this.getNumMesiReclusione().intValue() != 0)
				lStringReclusione += " Mesi " + this.getNumMesiReclusione();
		}
		if (this.getNumGiorniReclusione() != null) {
			if (this.getNumGiorniReclusione().intValue() != 0)
				lStringReclusione += " Giorni " + this.getNumGiorniReclusione();
		}

		if (lStringReclusione.length() > 1)
			this.mStringaReclusione = lStringReclusione;
		else
			this.mStringaReclusione = null;
	}

	/**
	 * calcolaStringaIsolamento per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaIsolamentoCum() {
		String lStringIsolamento = "";
		if (this.getNumAnniIsolamentoDiurno() != null) {
			if (this.getNumAnniIsolamentoDiurno().intValue() != 0)
				lStringIsolamento = "Anni " + this.getNumAnniIsolamentoDiurno();
		}
		if (this.getNumMesiIsolamentoDiurno() != null) {
			if (this.getNumMesiIsolamentoDiurno().intValue() != 0)
				lStringIsolamento += " Mesi " + this.getNumMesiIsolamentoDiurno();
		}
		if (this.getNumGiorniIsolamentoDiurno() != null) {
			if (this.getNumGiorniIsolamentoDiurno().intValue() != 0)
				lStringIsolamento += " Giorni " + this.getNumGiorniIsolamentoDiurno();
		}

		if (lStringIsolamento.length() > 1) {
			this.mStringaIsolamentoDiurno = lStringIsolamento;
		} else {
			this.mStringaIsolamentoDiurno = null;
		}
	}
}
