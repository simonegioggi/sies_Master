package siap.siep.penacomplessiva.model;

/**
* <p>Title: PenaComplessivaModel</p>
* <p>Description: Classe Model che rappresenta il PenaComplessiva</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.siep.penaresidua.model.PenaResiduaModel;

public class PenaComplessivaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -3422768316774453483L;
	private BigDecimal mIdPenaComplessiva;
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
	private Date mDataInizio;
	private Date mDataFine;
	private String mCodTipoRito;
	private String mDescrTipoRito;
	private String mFlagPenaInContinuazione;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mNumAnniCondonati;
	private BigDecimal mNumMesiCondonati;
	private BigDecimal mNumGiorniCondonati;
	private BigDecimal mImportoCondonato;
	private BigDecimal mFasSieIdFascicoloSiep;
	private Date mDataInizioIsolamentoDiurno;
	private Date mDataFineIsolamentoDiurno;
	private BigDecimal mNumAnniIsolamentoDiurno;
	private BigDecimal mNumMesiIsolamentoDiurno;
	private BigDecimal mNumGiorniIsolamentoDiurno;
	private Date mDataPrescrizione;

	private String mStringaReclusione;
	private String mStringaArresto;
	private String mStringaIsolamentoDiurno;

	/**
	 * L'attributo mPenaResidua, se valorizzato, contiene l'ultima pena residua validata associata al
	 * fascicolo. L'ultima pena residua contiene il campo flag ergastolo, che viene valorizzato anche se
	 * l'ergastolo viene dato in cumulo. Il nuovo attributo mPenaResidua viene valorizzato contestualemente
	 * alla valorizzazione del PenaComplessivaModel.
	 */
	private PenaResiduaModel mPenaResidua;

	// MEV_2023-33
	private BigDecimal mImportoTotale;
	
	// COSTRUTTORE DI DEFAULT
	public PenaComplessivaModel() {
		this.mIdPenaComplessiva = null;
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
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mCodTipoRito = null;
		this.mDescrTipoRito = null;
		this.mFlagPenaInContinuazione = null;
		this.mCodOperatoreInserimento = null;
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = null;
		this.mDescrUfficioInserimento = null;
		this.mCodOperatoreAggiornamento = null;
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = null;
		this.mDescrUfficioAggiornamento = null;
		this.mNumAnniCondonati = null;
		this.mNumMesiCondonati = null;
		this.mNumGiorniCondonati = null;
		this.mImportoCondonato = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mDataInizioIsolamentoDiurno = null;
		this.mDataFineIsolamentoDiurno = null;
		this.mNumAnniIsolamentoDiurno = null;
		this.mNumMesiIsolamentoDiurno = null;
		this.mNumGiorniIsolamentoDiurno = null;
		this.mDataPrescrizione = null;

		this.mPenaResidua = null;
	}

	// COSTRUTTORE DI COPIA
	public PenaComplessivaModel(PenaComplessivaModel aModel) {
		this.mIdPenaComplessiva = aModel.mIdPenaComplessiva;
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
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mCodTipoRito = aModel.mCodTipoRito;
		this.mDescrTipoRito = aModel.mDescrTipoRito;
		this.mFlagPenaInContinuazione = aModel.mFlagPenaInContinuazione;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mNumAnniCondonati = aModel.mNumAnniCondonati;
		this.mNumMesiCondonati = aModel.mNumMesiCondonati;
		this.mNumGiorniCondonati = aModel.mNumGiorniCondonati;
		this.mImportoCondonato = aModel.mImportoCondonato;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mDataInizioIsolamentoDiurno = aModel.mDataInizioIsolamentoDiurno;
		this.mDataFineIsolamentoDiurno = aModel.mDataFineIsolamentoDiurno;
		this.mNumAnniIsolamentoDiurno = aModel.mNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aModel.mNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aModel.mNumGiorniIsolamentoDiurno;
		this.mDataPrescrizione = aModel.mDataPrescrizione;

		this.mPenaResidua = aModel.mPenaResidua;
	}

	// COSTRUTTORE MODEL
	public PenaComplessivaModel(BigDecimal aIdPenaComplessiva, String aCodTipoPenaDetentiva,
			String aDescrTipoPenaDetentiva, BigDecimal aNumAnniReclusione, BigDecimal aNumMesiReclusione,
			BigDecimal aNumGiorniReclusione, BigDecimal aImportoMulta, BigDecimal aNumAnniArresto,
			BigDecimal aNumMesiArresto, BigDecimal aNumGiorniArresto, BigDecimal aImportoAmmenda,
			Date aDataInizio, Date aDataFine, String aCodTipoRito, String aDescrTipoRito,
			String aFlagPenaInContinuazione, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aNumAnniCondonati, BigDecimal aNumMesiCondonati, BigDecimal aNumGiorniCondonati,
			BigDecimal aImportoCondonato, BigDecimal aFasSieIdFascicoloSiep, Date aDataInizioIsolamentoDiurno,
			Date aDataFineIsolamentoDiurno, BigDecimal aNumAnniIsolamentoDiurno,
			BigDecimal aNumMesiIsolamentoDiurno, BigDecimal aNumGiorniIsolamentoDiurno,
			Date aDataPrescrizione) {
		this.mIdPenaComplessiva = aIdPenaComplessiva;
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
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mCodTipoRito = aCodTipoRito;
		this.mDescrTipoRito = aDescrTipoRito;
		this.mFlagPenaInContinuazione = aFlagPenaInContinuazione;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mNumAnniCondonati = aNumAnniCondonati;
		this.mNumMesiCondonati = aNumMesiCondonati;
		this.mNumGiorniCondonati = aNumGiorniCondonati;
		this.mImportoCondonato = aImportoCondonato;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mDataInizioIsolamentoDiurno = aDataInizioIsolamentoDiurno;
		this.mDataFineIsolamentoDiurno = aDataFineIsolamentoDiurno;
		this.mNumAnniIsolamentoDiurno = aNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aNumGiorniIsolamentoDiurno;
		this.mDataPrescrizione = aDataPrescrizione;

		this.mPenaResidua = null;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdPenaComplessiva() {
		return mIdPenaComplessiva;
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

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public String getCodTipoRito() {
		return mCodTipoRito;
	}

	public String getDescrTipoRito() {
		return mDescrTipoRito;
	}

	public String getFlagPenaInContinuazione() {
		return mFlagPenaInContinuazione;
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

	public BigDecimal getNumAnniCondonati() {
		return mNumAnniCondonati;
	}

	public BigDecimal getNumMesiCondonati() {
		return mNumMesiCondonati;
	}

	public BigDecimal getNumGiorniCondonati() {
		return mNumGiorniCondonati;
	}

	public BigDecimal getImportoCondonato() {
		return mImportoCondonato;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public Date getDataInizioIsolamentoDiurno() {
		return mDataInizioIsolamentoDiurno;
	}

	public Date getDataFineIsolamentoDiurno() {
		return mDataFineIsolamentoDiurno;
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

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	public PenaResiduaModel getPenaResidua() {
		return mPenaResidua;
	}

	//MEV_2023-33
	public BigDecimal getImportoTotale() {
	  return mImportoTotale;
	}
  
	//
	// METODI SET()
	//
	public void setIdPenaComplessiva(BigDecimal aValore) {
		mIdPenaComplessiva = aValore;
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

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setCodTipoRito(String aValore) {
		mCodTipoRito = aValore;
	}

	public void setDescrTipoRito(String aValore) {
		mDescrTipoRito = aValore;
	}

	public void setFlagPenaInContinuazione(String aValore) {
		mFlagPenaInContinuazione = aValore;
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

	public void setNumAnniCondonati(BigDecimal aValore) {
		mNumAnniCondonati = aValore;
	}

	public void setNumMesiCondonati(BigDecimal aValore) {
		mNumMesiCondonati = aValore;
	}

	public void setNumGiorniCondonati(BigDecimal aValore) {
		mNumGiorniCondonati = aValore;
	}

	public void setImportoCondonato(BigDecimal aValore) {
		mImportoCondonato = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setDataInizioIsolamentoDiurno(Date aValore) {
		mDataInizioIsolamentoDiurno = aValore;
	}

	public void setDataFineIsolamentoDiurno(Date aValore) {
		mDataFineIsolamentoDiurno = aValore;
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

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setPenaResidua(PenaResiduaModel aValore) {
		mPenaResidua = aValore;
	}

	// MEV_2023-33
  public void setImportoTotale(BigDecimal aValore) {
    mImportoTotale = aValore;
  }
	 
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdPenaComplessiva + " - " + mCodTipoPenaDetentiva + " - " + mDescrTipoPenaDetentiva
				+ " - " + mNumAnniReclusione + " - " + mNumMesiReclusione + " - " + mNumGiorniReclusione
				+ " - " + mImportoMulta + " - " + mNumAnniArresto + " - " + mNumMesiArresto + " - "
				+ mNumGiorniArresto + " - " + mImportoAmmenda + " - " + mDataInizio + " - " + mDataFine
				+ " - " + mCodTipoRito + " - " + mDescrTipoRito + " - " + mFlagPenaInContinuazione + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mNumAnniCondonati + " - " + mNumMesiCondonati + " - " + mNumGiorniCondonati + " - "
				+ mImportoCondonato + " - " + mFasSieIdFascicoloSiep + " - " + mDataInizioIsolamentoDiurno
				+ " - " + mDataFineIsolamentoDiurno + " - " + mNumAnniIsolamentoDiurno + " - "
				+ mNumMesiIsolamentoDiurno + " - " + mNumGiorniIsolamentoDiurno + " - " + mDataPrescrizione;

		return lStr;
	}

	/**
	 * calcolaStringaArresto per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaArresto() {
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
	public void calcolaStringaReclusione() {
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
	public void calcolaStringaIsolamento() {
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
