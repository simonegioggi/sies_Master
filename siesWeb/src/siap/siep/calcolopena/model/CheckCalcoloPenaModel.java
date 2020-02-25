package siap.siep.calcolopena.model;

import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class CheckCalcoloPenaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1168992407481527551L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private CalcoloPenaModel mCalcoloPenaModel;
	private PenaResiduaModel mUltimaPenaValidata;
	private FascicoloSiepModel mFascicoloSiep;

	private PenaResiduaModel mPenaRicalcolata;

	private boolean mIsErrQuantum = false;
	private boolean mIsErrDate = false;

	private boolean mIsErrDataInizio = false;
	private boolean mIsErrDataFineReclusione = false;
	private boolean mIsErrDataInizioArresto = false;
	private boolean mIsErrDataFine = false;

	private boolean mIsErrCheck = false;

	// COSTRUTTORE DI DEFAULT
	public CheckCalcoloPenaModel() {
		this.mCalcoloPenaModel = null;
		this.mUltimaPenaValidata = null;
		this.mFascicoloSiep = null;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public CalcoloPenaModel getCalcoloPenaModel() {
		return mCalcoloPenaModel;
	}

	public PenaResiduaModel getUltimaPenaValidata() {
		return mUltimaPenaValidata;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	public PenaResiduaModel getPenaRicalcolata() {
		return mPenaRicalcolata;
	}

	public boolean isErrQuantum() {
		return mIsErrQuantum;
	}

	public boolean isErrDate() {
		return mIsErrDate;
	}

	public boolean isErrDataInizio() {
		return mIsErrDataInizio;
	}

	public boolean isErrDataFineReclusione() {
		return mIsErrDataFineReclusione;
	}

	public boolean isErrDataInizioArresto() {
		return mIsErrDataInizioArresto;
	}

	public boolean isErrDataFine() {
		return mIsErrDataFine;
	}

	public boolean isErrCheck() {
		return mIsErrCheck;
	}

	public void setErrCheck(boolean aValore) {
		mIsErrCheck = aValore;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setCalcoloPenaModel(CalcoloPenaModel aValore) {
		mCalcoloPenaModel = aValore;
	}

	public void setUltimaPenaValidata(PenaResiduaModel aValore) {
		mUltimaPenaValidata = aValore;
	}

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}

	/**
	 * 
	 *
	 */
	public void confrontaPene() {

		if (mIsErrCheck) {
			// se è già stato segnalato l'errore non continuo nei controlli
			return;
		}

		try {
			Date lDataInizioPena = this.mUltimaPenaValidata.getDataInizio();
			PenaResiduaModel lPenaDaEspiare = this.mCalcoloPenaModel.getPenaDaEspiare(lDataInizioPena, null,
					"all");

			mPenaRicalcolata = lPenaDaEspiare;

			// Controllo che i quantum coincidano
			if (mUltimaPenaValidata != null && mUltimaPenaValidata.getIdPenaResidua() != null) {
				CalendarModel lRecCalcolata = lPenaDaEspiare.getQuantumReclusione();
				CalendarModel lArrCalcolata = lPenaDaEspiare.getQuantumArresto();

				CalendarModel lRecUltimaVal = mUltimaPenaValidata.getQuantumReclusione();
				CalendarModel lArrUltimaVal = mUltimaPenaValidata.getQuantumArresto();

				if (CalendarUtil.getTotGiorni(lRecCalcolata) != CalendarUtil.getTotGiorni(lRecUltimaVal)
						|| CalendarUtil.getTotGiorni(lArrCalcolata) != CalendarUtil
								.getTotGiorni(lArrUltimaVal)) {
					// quantum differenti
					mIsErrQuantum = true;
				}
			}

			// ========================================================================
			// Controllo che le date di decorrenza coincidano
			// ========================================================================
			if (mUltimaPenaValidata != null && mUltimaPenaValidata.getIdPenaResidua() != null) {
				// Confronto le date
				if ((lPenaDaEspiare.getDataInizio() != null && mUltimaPenaValidata.getDataInizio() == null)
						|| (lPenaDaEspiare.getDataInizio() == null
								&& mUltimaPenaValidata.getDataInizio() != null)
						|| (lPenaDaEspiare.getDataInizio() != null
								&& mUltimaPenaValidata.getDataInizio() != null
								&& !DateUtils.isEquals(lPenaDaEspiare.getDataInizio(),
										mUltimaPenaValidata.getDataInizio()))) {
					// date inizio differenti
					mIsErrDataInizio = true;
				}

				if ((lPenaDaEspiare.getDataFineReclusione() != null
						&& mUltimaPenaValidata.getDataFineReclusione() == null)
						|| (lPenaDaEspiare.getDataFineReclusione() == null
								&& mUltimaPenaValidata.getDataFineReclusione() != null)
						|| (lPenaDaEspiare.getDataFineReclusione() != null
								&& mUltimaPenaValidata.getDataFineReclusione() != null
								&& !DateUtils.isEquals(lPenaDaEspiare.getDataFineReclusione(),
										mUltimaPenaValidata.getDataFineReclusione()))) {
					// date fine reclusione differenti
					mIsErrDataFineReclusione = true;
				}

				if ((lPenaDaEspiare.getDataInizioArresto() != null
						&& mUltimaPenaValidata.getDataInizioArresto() == null)
						|| (lPenaDaEspiare.getDataInizioArresto() == null
								&& mUltimaPenaValidata.getDataInizioArresto() != null)
						|| (lPenaDaEspiare.getDataInizioArresto() != null
								&& mUltimaPenaValidata.getDataInizioArresto() != null
								&& !DateUtils.isEquals(lPenaDaEspiare.getDataInizioArresto(),
										mUltimaPenaValidata.getDataInizioArresto()))) {
					// date inizio arresto differenti
					mIsErrDataInizioArresto = true;
				}

				if ((lPenaDaEspiare.getDataFine() != null && mUltimaPenaValidata.getDataFine() == null)
						|| (lPenaDaEspiare.getDataFine() == null && mUltimaPenaValidata.getDataFine() != null)
						|| (lPenaDaEspiare.getDataFine() != null && mUltimaPenaValidata.getDataFine() != null
								&& !DateUtils.isEquals(lPenaDaEspiare.getDataFine(),
										mUltimaPenaValidata.getDataFine()))) {
					// date fine differenti
					mIsErrDataFine = true;
				}
			}

			if (mIsErrDataInizio || mIsErrDataFineReclusione || mIsErrDataInizioArresto || mIsErrDataFine) {
				mIsErrDate = true;
			}
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ERRORE!!!");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(e.fillInStackTrace());
			mIsErrCheck = true;
		}
	}

}