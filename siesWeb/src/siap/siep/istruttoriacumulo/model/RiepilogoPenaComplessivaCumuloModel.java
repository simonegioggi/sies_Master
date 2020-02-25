package siap.siep.istruttoriacumulo.model;

/**
* <p>Title: RiepilogoPenaComplessivaCumuloModel</p>
* <p>Description: Classe Model che rappresenta la somma della PenaComplessiva </p>
* <p> di tutti i tittoli di una istruttoria		</p
* <p> in ambito Cumulo (Pena_complessiva_cumulo) </p>
*/

import java.math.BigDecimal;
import java.util.Vector;

//import siap.siep.penaresidua.model.PenaResiduaModel;
import f3b.model.GenericModel;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;

@SuppressWarnings("rawtypes")
public class RiepilogoPenaComplessivaCumuloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 4080219054050322470L;
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

	private String mStringaArresto;
	private String mStringaReclusione;

	// La pena complessiva di Ogni Titolo di una istruttora è addizionata al vettore
	private Vector mTotalePeneComplessive;

	// COSTRUTTORE DI DEFAULT
	public RiepilogoPenaComplessivaCumuloModel() {
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

		this.mTotalePeneComplessive = new Vector();

	}

	//
	// METODI GET()
	//
	public String getCodTipoPenaDetentiva() {
		return mCodTipoPenaDetentiva;
	}

	public String getDescTipoPenaDetentiva() {
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

	public BigDecimal getImportoAmmenda() {
		if (mImportoAmmenda != null)
			return mImportoAmmenda;
		else
			return new BigDecimal(0);
	}

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	public Vector getTotalePenaComplessiva() {
		return mTotalePeneComplessive;
	}

	//
	// METODI SET()
	//
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

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setTotalePenaComplessiva(Vector aValore) {
		mTotalePeneComplessive = aValore;
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

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "PenaComplessivaCumuloModel:\n" + "[ mCodTipoPenaDetentiva      = " + mCodTipoPenaDetentiva
				+ " ]\n" + "[ mDescrTipoPenaDetentiva    = " + mDescrTipoPenaDetentiva + " ]\n"
				+ "[ mNumAnniReclusione         = " + mNumAnniReclusione + " ]\n"
				+ "[ mNumMesiReclusione         = " + mNumMesiReclusione + " ]\n"
				+ "[ mNumGiorniReclusione       = " + mNumGiorniReclusione + " ]\n"
				+ "[ mImportoMulta              = " + mImportoMulta + " ]\n"
				+ "[ mNumAnniArresto            = " + mNumAnniArresto + " ]\n"
				+ "[ mNumMesiArresto            = " + mNumMesiArresto + " ]\n"
				+ "[ mNumGiorniArresto          = " + mNumGiorniArresto + " ]\n"
				+ "[ mImportoAmmenda            = " + mImportoAmmenda + " ]";
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
	 * i 2 metodi seguenti Restituiscono la somma di (Arresti e Ammenda , Reclusione e multa) di tutte le
	 * Pena_Complessiva_Cumulo di tutti i Titoli di una istruttoria_Cumulo Richiamati nella stampa prospetto
	 * Titoli in istruttoria
	 */
	public CalendarModel getTotaleReclusioniPenaCumulo() {
		CalendarModel lBenConc = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mTotalePeneComplessive.size(); i++) {
			// BeneficioCumuloModel lBenMod = (BeneficioCumuloModel) mTotalePeneComplessive.elementAt(i);
			PenaComplessivaCumuloModel lBenMod = (PenaComplessivaCumuloModel) mTotalePeneComplessive
					.elementAt(i);
			if (lBenMod != null && lBenMod.getIdPenaComplessivaCum() != null) {
				CalendarModel lCalMod = new CalendarModel();

				if (lBenMod.getNumAnniReclusione() != null)
					lCalMod.setNumAnni(lBenMod.getNumAnniReclusione());
				else
					lCalMod.setNumAnni(new BigDecimal(0));

				if (lBenMod.getNumMesiReclusione() != null)
					lCalMod.setNumMesi(lBenMod.getNumMesiReclusione());
				else
					lCalMod.setNumMesi(new BigDecimal(0));

				if (lBenMod.getNumGiorniReclusione() != null)
					lCalMod.setNumGiorni(lBenMod.getNumGiorniReclusione());
				else
					lCalMod.setNumGiorni(new BigDecimal(0));

				if (lBenMod.getImportoMulta() != null)
					lCalMod.setImportoMulta(lBenMod.getImportoMulta().doubleValue());
				else
					lCalMod.setImportoMulta(0);

				lBenConc = lCalUtil.sommaGiornieValute(lBenConc, lCalMod);
			}
		}

		return lBenConc;

	} // Chiude getTotaleReclusioniPenaCumulo()

	public CalendarModel getTotaleArrestiPenaCumulo() {
		CalendarModel lBenConc = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mTotalePeneComplessive.size(); i++) {
			// BeneficioCumuloModel lBenMod = (BeneficioCumuloModel) mTotalePeneComplessive.elementAt(i);
			PenaComplessivaCumuloModel lBenMod = (PenaComplessivaCumuloModel) mTotalePeneComplessive
					.elementAt(i);
			if (lBenMod != null && lBenMod.getIdPenaComplessivaCum() != null) {
				CalendarModel lCalMod = new CalendarModel();

				if (lBenMod.getNumAnniArresto() != null)
					lCalMod.setNumAnni(lBenMod.getNumAnniArresto());
				else
					lCalMod.setNumAnni(new BigDecimal(0));

				if (lBenMod.getNumMesiArresto() != null)
					lCalMod.setNumMesi(lBenMod.getNumMesiArresto());
				else
					lCalMod.setNumMesi(new BigDecimal(0));

				if (lBenMod.getNumGiorniArresto() != null)
					lCalMod.setNumGiorni(lBenMod.getNumGiorniArresto());
				else
					lCalMod.setNumGiorni(new BigDecimal(0));

				if (lBenMod.getImportoAmmenda() != null)
					lCalMod.setImportoAmmenda(lBenMod.getImportoAmmenda().doubleValue());
				else
					lCalMod.setImportoAmmenda(0);

				lBenConc = lCalUtil.sommaGiornieValute(lBenConc, lCalMod);
			}
		}

		return lBenConc;

	} // chiude getTotaleArrestiPenaCumulo()

	public void SeErgastolo() {
		String lErga = "";
		String lErgaDiu = "";
		for (int i = 0; i < mTotalePeneComplessive.size(); i++) {
			// BeneficioCumuloModel lBenMod = (BeneficioCumuloModel) mTotalePeneComplessive.elementAt(i);
			PenaComplessivaCumuloModel lBenMod = (PenaComplessivaCumuloModel) mTotalePeneComplessive
					.elementAt(i);
			if (lBenMod != null && lBenMod.getIdPenaComplessivaCum() != null) {
				if (lBenMod.getCodTipoPenaDetentiva() != null) {
					if ("03".equals(lBenMod.getCodTipoPenaDetentiva())) {
						lErga = "Ergastolo";
					} else if ("04".equals(lBenMod.getCodTipoPenaDetentiva())) {
						lErgaDiu = "Ergastolo con Isolamento Diurno";
					}
				}
			}
		}

		if (!lErgaDiu.equals("")) {
			mDescrTipoPenaDetentiva = lErgaDiu;
			mCodTipoPenaDetentiva = "04";
		} else if (!lErga.equals("")) {
			mDescrTipoPenaDetentiva = lErga;
			mCodTipoPenaDetentiva = "03";
		} else {
			mDescrTipoPenaDetentiva = "";
			mCodTipoPenaDetentiva = "";
		}

	}

} // Chiude RiepilogoPenaComplessivaCumuloModel()
