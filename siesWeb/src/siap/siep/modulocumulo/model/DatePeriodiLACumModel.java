package siap.siep.modulocumulo.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: DatePeriodiLACumModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta alcuni attributi di PeriodoLibAntCumuloModel
 * </p>
 *
 * @version 1.0
 */
public class DatePeriodiLACumModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -4358904023786145352L;
	private BigDecimal mIdPeriodoLibAntCumulo;
	private Date mDataInizio;
	private Date mDataFine;
	private BigDecimal mLibIdLibAnticipataCumulo;
	private String mStringaPeriodoDalAl;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public DatePeriodiLACumModel() {
		this.mIdPeriodoLibAntCumulo = null;
		this.mDataInizio = null;
		this.mDataFine = null;

		this.mLibIdLibAnticipataCumulo = null;
		this.mStringaPeriodoDalAl = null;

	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 *
	 * @param aModel
	 ****************************************************************************/
	public DatePeriodiLACumModel(DatePeriodiLACumModel aModel) {
		this.mIdPeriodoLibAntCumulo = aModel.mIdPeriodoLibAntCumulo;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;

		this.mLibIdLibAnticipataCumulo = aModel.mLibIdLibAnticipataCumulo;
		this.mStringaPeriodoDalAl = aModel.mStringaPeriodoDalAl;

	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public DatePeriodiLACumModel(BigDecimal aIdPeriodoLA, Date aDataIni, Date aDataFin, BigDecimal aLibIdLA,
			String aStringaPeriodoDalAl)

	{
		this.mIdPeriodoLibAntCumulo = aIdPeriodoLA;
		this.mDataInizio = aDataIni;
		this.mDataFine = aDataFin;

		this.mLibIdLibAnticipataCumulo = aLibIdLA;
		this.mStringaPeriodoDalAl = aStringaPeriodoDalAl;

	}

	/**
	 * Costruttore che inizializza il model estraendo i dati da un record PeriodoLibAntCumulo
	 *
	 * @param aModel
	 */
	public DatePeriodiLACumModel(PeriodoLibAntCumuloModel aPeriodoCumModel) {
		this.mIdPeriodoLibAntCumulo = aPeriodoCumModel.getIdPeriodoLibAntCumulo();
		this.mDataInizio = aPeriodoCumModel.getDataInizio();
		this.mDataFine = aPeriodoCumModel.getDataFine();
		this.mLibIdLibAnticipataCumulo = aPeriodoCumModel.getLibIdLibAnticipataCumulo();

	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdPeriodoLibAntCumulo() {
		return mIdPeriodoLibAntCumulo;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public BigDecimal getLibIdLibAnticipataCumulo() {
		return mLibIdLibAnticipataCumulo;
	}

	public String getStringaPeriodoDalAl() {
		return mStringaPeriodoDalAl;
	}

	// --
	public String FormaStringaDatePeriodo() {
		String lPeriodoDalAl = "";

		if (this.getDataInizio() != null && this.getDataFine() != null) {
			lPeriodoDalAl += "relativamente ai periodi dal "
					+ DateUtils.getDateToString(this.getDataInizio(), "dd-MM-yyyy") + " al "
					+ DateUtils.getDateToString(this.getDataFine(), "dd-MM-yyyy");
		}

		if (this.getDataInizio() != null && this.getDataFine().equals(null)) {
			lPeriodoDalAl += "relativamente al periodo dal "
					+ DateUtils.getDateToString(this.getDataInizio(), "dd-MM-yyyy");
		}

		if (this.getDataInizio().equals(null) && this.getDataFine() != null) {
			lPeriodoDalAl += "relativamente al periodo fino al "
					+ DateUtils.getDateToString(this.getDataFine(), "dd-MM-yyyy");
		}

		return lPeriodoDalAl;
	}
	// --

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdPeriodoLibAntCumulo(BigDecimal aValore) {
		mIdPeriodoLibAntCumulo = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setLibIdLibAnticipataCumulo(BigDecimal aValore) {
		mLibIdLibAnticipataCumulo = aValore;
	}

	public void setStringaPeriodoDalAl(String aValore) {
		mStringaPeriodoDalAl = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "DatePeriodiLACumModel:\n" + "[ mIdPeriodoLibAntCumulo  = " + mIdPeriodoLibAntCumulo + " ]\n"
				+ "[ mDataInizio	= " + mDataInizio + " ]\n" + "[ mDataFine    = " + mDataFine + " ]\n"
				+ "[ mLibIdLibAnticipataCumulo   = " + mLibIdLibAnticipataCumulo + " ]";

		return lStr;
	}

}