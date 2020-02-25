package siap.siep.modulocumulo.model;

/**
* <p>Title: DatiFinaliUlterioriSanzioniModel</p>
* <p>Description: Classe Model che rappresenta il DatiFinaliUlterioriSanzioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import f3b.model.GenericModel;
import f3b.util.StringUtils;

@SuppressWarnings("rawtypes")
public class DatiFinaliUlterioriSanzioniModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6349842245299085326L;

	private BigDecimal mIdDatiFinaliUlterioriSanz;
	private String mCodTipoUlterioreSanzione;
	private String mDescrTipoUlterioreSanzione;
	private String mDescrTipoUlterioreSanzioneXStampa;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private BigDecimal mMulta;
	private BigDecimal mAmmenda;
	private String mFlagEspulPerp;
	private String mCodTipoLpu;
	private String mDescrTipoLpu;
	private BigDecimal mNumOreTot;
	private BigDecimal mNumOreSett;
	private BigDecimal mCodFreqSett;
	private BigDecimal mDatIdDatiFinaliCumulo;
	private BigDecimal mIstrIdIstruttoriaCumulo;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private String mTipoOperazioneCRUD; // I=Insert, D=Delete, U=Update

	private String mStringaDurata;
	private String mStringaImporti;

	private Vector<TipologiaOrarioModel> mListaOrariLPU = null; // x LPU contiene Gli orari x Frequenza
																// Determinata

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public DatiFinaliUlterioriSanzioniModel() {
		this.mIdDatiFinaliUlterioriSanz = null;
		this.mCodTipoUlterioreSanzione = "";
		this.mDescrTipoUlterioreSanzione = "";
		this.mDescrTipoUlterioreSanzioneXStampa = "";
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mMulta = null;
		this.mAmmenda = null;
		this.mFlagEspulPerp = "";
		this.mCodTipoLpu = null;
		this.mDescrTipoLpu = "";
		this.mNumOreTot = null;
		this.mNumOreSett = null;
		this.mCodFreqSett = null;
		this.mDatIdDatiFinaliCumulo = null;
		this.mIstrIdIstruttoriaCumulo = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public DatiFinaliUlterioriSanzioniModel(DatiFinaliUlterioriSanzioniModel aModel) {
		this.mIdDatiFinaliUlterioriSanz = aModel.mIdDatiFinaliUlterioriSanz;
		this.mCodTipoUlterioreSanzione = aModel.mCodTipoUlterioreSanzione;
		this.mDescrTipoUlterioreSanzione = aModel.mDescrTipoUlterioreSanzione;
		this.mDescrTipoUlterioreSanzioneXStampa = aModel.mDescrTipoUlterioreSanzioneXStampa;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mMulta = aModel.mMulta;
		this.mAmmenda = aModel.mAmmenda;
		this.mFlagEspulPerp = aModel.mFlagEspulPerp;
		this.mCodTipoLpu = aModel.mCodTipoLpu;
		this.mDescrTipoLpu = aModel.mDescrTipoLpu;
		this.mNumOreTot = aModel.mNumOreTot;
		this.mNumOreSett = aModel.mNumOreSett;
		this.mCodFreqSett = aModel.mCodFreqSett;
		this.mDatIdDatiFinaliCumulo = aModel.mDatIdDatiFinaliCumulo;
		this.mIstrIdIstruttoriaCumulo = aModel.mIstrIdIstruttoriaCumulo;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public DatiFinaliUlterioriSanzioniModel(BigDecimal aIdDatiFinaliUlterioriSanz,
			String aCodTipoUlterioreSanzione, String aDescrTipoUlterioreSanzione,
			String aDescrTipoUlterioreSanzioneXStampa, BigDecimal aNumAnni, BigDecimal aNumMesi,
			BigDecimal aNumGiorni, BigDecimal aMulta, BigDecimal aAmmenda, String aFlagEspulPerp,
			String aCodTipoLpu, String aDescrTipoLpu, BigDecimal aNumOreTot, BigDecimal aNumOreSett,
			BigDecimal aCodFreqSett, BigDecimal aDatIdDatiFinaliCumulo, BigDecimal aIstrIdIstruttoriaCumulo,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdDatiFinaliUlterioriSanz = aIdDatiFinaliUlterioriSanz;
		this.mCodTipoUlterioreSanzione = aCodTipoUlterioreSanzione;
		this.mDescrTipoUlterioreSanzione = aDescrTipoUlterioreSanzione;
		this.mDescrTipoUlterioreSanzioneXStampa = aDescrTipoUlterioreSanzioneXStampa;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mMulta = aMulta;
		this.mAmmenda = aAmmenda;
		this.mFlagEspulPerp = aFlagEspulPerp;
		this.mCodTipoLpu = aCodTipoLpu;
		this.mDescrTipoLpu = aDescrTipoLpu;
		this.mNumOreTot = aNumOreTot;
		this.mNumOreSett = aNumOreSett;
		this.mCodFreqSett = aCodFreqSett;
		this.mDatIdDatiFinaliCumulo = aDatIdDatiFinaliCumulo;
		this.mIstrIdIstruttoriaCumulo = aIstrIdIstruttoriaCumulo;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdDatiFinaliUlterioriSanz() {
		return mIdDatiFinaliUlterioriSanz;
	}

	public String getCodTipoUlterioreSanzione() {
		return mCodTipoUlterioreSanzione;
	}

	public String getDescrTipoUlterioreSanzione() {
		return mDescrTipoUlterioreSanzione;
	}

	public String getDescrTipoUlterioreSanzioneXStampa() {
		return mDescrTipoUlterioreSanzioneXStampa;
	}

	public BigDecimal getNumAnni() {
		return mNumAnni;
	}

	public BigDecimal getNumMesi() {
		return mNumMesi;
	}

	public BigDecimal getNumGiorni() {
		return mNumGiorni;
	}

	public BigDecimal getMulta() {
		return mMulta;
	}

	public BigDecimal getAmmenda() {
		return mAmmenda;
	}

	public String getFlagEspulPerp() {
		return mFlagEspulPerp;
	}

	public String getCodTipoLpu() {
		return mCodTipoLpu;
	}

	public String getDescrTipoLpu() {
		return mDescrTipoLpu;
	}

	public BigDecimal getNumOreTot() {
		return mNumOreTot;
	}

	public BigDecimal getNumOreSett() {
		return mNumOreSett;
	}

	public BigDecimal getCodFreqSett() {
		return mCodFreqSett;
	}

	public BigDecimal getDatIdDatiFinaliCumulo() {
		return mDatIdDatiFinaliCumulo;
	}

	public BigDecimal getIstrIdIstruttoriaCumulo() {
		return mIstrIdIstruttoriaCumulo;
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

	public String getTipoOperazioneCRUD() {
		return mTipoOperazioneCRUD;
	}

	public Vector<TipologiaOrarioModel> getListaOrariLPU() {
		return mListaOrariLPU;
	}

	public String getStringaDurata() {
		return mStringaDurata;
	}

	public String getStringaImporti() {
		return mStringaImporti;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdDatiFinaliUlterioriSanz(BigDecimal aValore) {
		mIdDatiFinaliUlterioriSanz = aValore;
	}

	public void setCodTipoUlterioreSanzione(String aValore) {
		mCodTipoUlterioreSanzione = aValore;
	}

	public void setDescrTipoUlterioreSanzione(String aValore) {
		mDescrTipoUlterioreSanzione = aValore;
	}

	public void setDescrTipoUlterioreSanzioneXStampa(String aValore) {
		mDescrTipoUlterioreSanzioneXStampa = aValore;
	}

	public void setNumAnni(BigDecimal aValore) {
		mNumAnni = aValore;
	}

	public void setNumMesi(BigDecimal aValore) {
		mNumMesi = aValore;
	}

	public void setNumGiorni(BigDecimal aValore) {
		mNumGiorni = aValore;
	}

	public void setMulta(BigDecimal aValore) {
		mMulta = aValore;
	}

	public void setAmmenda(BigDecimal aValore) {
		mAmmenda = aValore;
	}

	public void setFlagEspulPerp(String aValore) {
		mFlagEspulPerp = aValore;
	}

	public void setCodTipoLpu(String aValore) {
		mCodTipoLpu = aValore;
	}

	public void setDescrTipoLpu(String aValore) {
		mDescrTipoLpu = aValore;
	}

	public void setNumOreTot(BigDecimal aValore) {
		mNumOreTot = aValore;
	}

	public void setNumOreSett(BigDecimal aValore) {
		mNumOreSett = aValore;
	}

	public void setCodFreqSett(BigDecimal aValore) {
		mCodFreqSett = aValore;
	}

	public void setDatIdDatiFinaliCumulo(BigDecimal aValore) {
		mDatIdDatiFinaliCumulo = aValore;
	}

	public void setIstrIdIstruttoriaCumulo(BigDecimal aValore) {
		mIstrIdIstruttoriaCumulo = aValore;
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

	public void setTipoOperazioneCRUD(String aValore) {
		mTipoOperazioneCRUD = aValore;
	}

	public void setListaOrariLPU(Vector<TipologiaOrarioModel> aValore) {
		mListaOrariLPU = aValore;
	}

	public void setStringaDurata(String aValore) {
		mStringaDurata = aValore;
	}

	public void setStringaImporti(String aValore) {
		mStringaImporti = aValore;
	}

	public boolean isDurata() {
		if (mNumAnni != null || mNumMesi != null || mNumGiorni != null)
			return true;
		else
			return false;
	}

	public boolean isEspulsione() {
		if (mFlagEspulPerp != null && !mFlagEspulPerp.equals(""))
			return true;
		else
			return false;
	}

	public String getTipologiaOrarioGiorno(String day) {
		String ret = day;

		try {
			switch (Integer.valueOf(day)) {
			case 1:
				ret = "Lunedi";
				break;
			case 2:
				ret = "Martedi";
				break;
			case 3:
				ret = "Mercoledi";
				break;
			case 4:
				ret = "Giovedi";
				break;
			case 5:
				ret = "Venerdi";
				break;
			case 6:
				ret = "Sabato";
				break;
			case 7:
				ret = "Domenica";
				break;
			}
		} catch (NumberFormatException e) {
		}

		return ret;
	}

	public String getTipologiaOrarioDalleOre(String day) {
		String ret = null;
		if (this.mListaOrariLPU != null) {
			Iterator iter = mListaOrariLPU.iterator();
			while (iter.hasNext()) {
				TipologiaOrarioModel tom = (TipologiaOrarioModel) iter.next();
				if (day.equals(tom.getCodNumGiorno())) {
					ret = tom.getDalleOre();
				}
			}
		}
		return ret;
	}

	public String getTipologiaOrarioAlleOre(String day) {
		String ret = null;
		if (this.mListaOrariLPU != null) {
			Iterator iter = mListaOrariLPU.iterator();
			while (iter.hasNext()) {
				TipologiaOrarioModel tom = (TipologiaOrarioModel) iter.next();
				if (day.equals(tom.getCodNumGiorno())) {
					ret = tom.getAlleOre();
				}
			}
		}
		return ret;
	}

	public String checkTipologiaOrario(String day) {
		String ret = "";
		if (this.mListaOrariLPU != null) {
			Iterator iter = mListaOrariLPU.iterator();
			while (iter.hasNext()) {
				TipologiaOrarioModel tom = (TipologiaOrarioModel) iter.next();
				if (day.equals(tom.getCodNumGiorno())) {
					ret = "checked";
				}
			}
		}
		return ret;
	}

	public void calcolaStringheXStampa() {
		calcolaStringaDurata();
		calcolaStringaImporti();
	}

	public void calcolaStringaDurata() {
		String lStringDurata = "";

		if ((mCodTipoUlterioreSanzione.equals("03") || mCodTipoUlterioreSanzione.equals("13"))
				&& "P".equals(mFlagEspulPerp)) { // Espulsione dalla stato perpetua
			lStringDurata = "Perpetua";
		} else {
			if (mNumAnni != null && mNumAnni.intValue() != 0)
				lStringDurata = "Anni " + mNumAnni;

			if (mNumMesi != null && mNumMesi.intValue() != 0)
				lStringDurata += " Mesi " + mNumMesi;

			if (mNumGiorni != null && mNumGiorni.intValue() != 0)
				lStringDurata += " Giorni " + mNumGiorni;
		}

		if (mCodTipoUlterioreSanzione.equals("11")) {
			lStringDurata = "Nella misura di: " + lStringDurata;
			if (mNumOreTot != null && mNumOreTot.intValue() > 0)
				lStringDurata = lStringDurata + " Pari ad ore complessive " + mNumOreTot;

		}

		if (lStringDurata.length() > 1) {
			this.mStringaDurata = lStringDurata;
		} else {
			this.mStringaDurata = null;
		}

	}

	public void calcolaStringaImporti() {
		String lStrImporti = null;
		if (mMulta != null && mMulta.intValue() > 0)
			lStrImporti = "Multa Euro " + StringUtils.toEuroFormat(mMulta);
		if (mAmmenda != null && mAmmenda.intValue() > 0)
			lStrImporti = "Ammenda Euro " + StringUtils.toEuroFormat(mAmmenda);

		mStringaImporti = lStrImporti;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "DatiFinaliUlterioriSanzioniModel:\n" + "[ mIdDatiFinaliUlterioriSanz = "
				+ mIdDatiFinaliUlterioriSanz + " ]\n" + "[ mCodTipoUlterioreSanzione  = "
				+ mCodTipoUlterioreSanzione + " ]\n" + "[ mDescrTipoXStampa          = "
				+ mDescrTipoUlterioreSanzioneXStampa + " ]\n" + "[ mNumAnni                   = " + mNumAnni
				+ " ]\n" + "[ mNumMesi                   = " + mNumMesi + " ]\n"
				+ "[ mNumGiorni                 = " + mNumGiorni + " ]\n" + "[ mMulta                     = "
				+ mMulta + " ]\n" + "[ mAmmenda                   = " + mAmmenda + " ]\n"
				+ "[ mFlagEspulPerp             = " + mFlagEspulPerp + " ]\n"
				+ "[ mCodTipoLpu                = " + mCodTipoLpu + " ]\n" + "[ mNumOreTot                 = "
				+ mNumOreTot + " ]\n" + "[ mNumOreSett                = " + mNumOreSett + " ]\n"
				+ "[ mCodFreqSett               = " + mCodFreqSett + " ]\n"
				+ "[ mDatIdDatiFinaliCumulo     = " + mDatIdDatiFinaliCumulo + " ]\n"
				+ "[ mIstrIdIstruttoriaCumulo   = " + mIstrIdIstruttoriaCumulo + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mTipoOperazioneCRUD        = " + mTipoOperazioneCRUD + " ]";

		return lStr;
	}

}