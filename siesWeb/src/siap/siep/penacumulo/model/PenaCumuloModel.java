package siap.siep.penacumulo.model;

/**
* <p>Title: PenaCumuloModel</p>
* <p>Description: Classe Model che rappresenta il PenaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class PenaCumuloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 5842071401850483348L;
	private BigDecimal mIdPenaCumulo;
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
	private Date mDataDecorrenzaPena;
	private String mMotivazioni;
	private BigDecimal mNumAnniReclusioneSosp;
	private BigDecimal mNumMesiReclusioneSosp;
	private BigDecimal mNumGiorniReclusioneSosp;
	private BigDecimal mNumAnniArrestoSosp;
	private BigDecimal mNumMesiArrestoSosp;
	private BigDecimal mNumGiorniArrestoSosp;
	private String mEstremiOrdinanza;
	private String mFlagErgastolo;
	private String mNote;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mCumIdCumulo;

	private BigDecimal mNumAnniIsolamentoDiurno;
	private BigDecimal mNumMesiIsolamentoDiurno;
	private BigDecimal mNumGiorniIsolamentoDiurno;

	private String mStringaReclusione;
	private String mStringaArresto;
	private String mStringaReclusioneSosp;
	private String mStringaArrestoSosp;
	private Date mDataEmissione;

	private String mMisuraSicurezza;
	private String mPenaAccessoria;
	private BigDecimal mNumGiorniLibAnticipata;

	private String mStringaIsolamentoDiurno;
	// 20/05/2014 - Nuova L.A. - decreto 2013/146 -
	private BigDecimal mNumGiorniLibAnticipataLA;
	private BigDecimal mNumGiorniLibAnticipataSPE;
	private BigDecimal mNumGiorniLibAnticipataINT;
	// DL 92
	private BigDecimal mNumGiorniRiduzionePena;

	// COSTRUTTORE DI DEFAULT
	public PenaCumuloModel() {
		this.mIdPenaCumulo = null;
		this.mCodTipoPenaDetentiva = "";
		this.mDescrTipoPenaDetentiva = "";
		this.mNumAnniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumGiorniReclusione = null;
		this.mImportoMulta = null;
		this.mNumAnniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumGiorniArresto = null;
		this.mImportoAmmenda = null;
		this.mDataDecorrenzaPena = null;
		this.mMotivazioni = "";
		this.mNumAnniReclusioneSosp = null;
		this.mNumMesiReclusioneSosp = null;
		this.mNumGiorniReclusioneSosp = null;
		this.mNumAnniArrestoSosp = null;
		this.mNumMesiArrestoSosp = null;
		this.mNumGiorniArrestoSosp = null;
		this.mEstremiOrdinanza = "";
		this.mFlagErgastolo = "";
		this.mNote = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mCumIdCumulo = null;

		this.mNumAnniIsolamentoDiurno = null;
		this.mNumMesiIsolamentoDiurno = null;
		this.mNumGiorniIsolamentoDiurno = null;

		this.mMisuraSicurezza = "";
		this.mPenaAccessoria = "";
		this.mNumGiorniLibAnticipata = null;
		// 20/05/2014 - Nuova L.A. - decreto 2013/146
		this.mNumGiorniLibAnticipataLA = null;
		this.mNumGiorniLibAnticipataSPE = null;
		this.mNumGiorniLibAnticipataINT = null;

		// DL92 10/2014
		this.mNumGiorniRiduzionePena = null;

	}

	// COSTRUTTORE DI COPIA
	public PenaCumuloModel(PenaCumuloModel aModel) {
		this.mIdPenaCumulo = aModel.mIdPenaCumulo;
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
		this.mDataDecorrenzaPena = aModel.mDataDecorrenzaPena;
		this.mMotivazioni = aModel.mMotivazioni;
		this.mNumAnniReclusioneSosp = aModel.mNumAnniReclusioneSosp;
		this.mNumMesiReclusioneSosp = aModel.mNumMesiReclusioneSosp;
		this.mNumGiorniReclusioneSosp = aModel.mNumGiorniReclusioneSosp;
		this.mNumAnniArrestoSosp = aModel.mNumAnniArrestoSosp;
		this.mNumMesiArrestoSosp = aModel.mNumMesiArrestoSosp;
		this.mNumGiorniArrestoSosp = aModel.mNumGiorniArrestoSosp;
		this.mEstremiOrdinanza = aModel.mEstremiOrdinanza;
		this.mFlagErgastolo = aModel.mFlagErgastolo;
		this.mNote = aModel.mNote;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mCumIdCumulo = aModel.mCumIdCumulo;

		this.mMisuraSicurezza = aModel.mMisuraSicurezza;
		this.mPenaAccessoria = aModel.mPenaAccessoria;
		this.mNumGiorniLibAnticipata = aModel.mNumGiorniLibAnticipata;

		this.mNumAnniIsolamentoDiurno = aModel.mNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aModel.mNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aModel.mNumGiorniIsolamentoDiurno;
		// 20/05/2014 - Nuova L.A. - decreto 2013/146
		this.mNumGiorniLibAnticipataLA = aModel.mNumGiorniLibAnticipataLA;
		this.mNumGiorniLibAnticipataSPE = aModel.mNumGiorniLibAnticipataSPE;
		this.mNumGiorniLibAnticipataINT = aModel.mNumGiorniLibAnticipataINT;

		this.mNumGiorniRiduzionePena = aModel.mNumGiorniRiduzionePena;

	}

	// COSTRUTTORE MODEL
	public PenaCumuloModel(BigDecimal aIdPenaCumulo, String aCodTipoPenaDetentiva,
			String aDescrTipoPenaDetentiva, BigDecimal aNumAnniReclusione, BigDecimal aNumMesiReclusione,
			BigDecimal aNumGiorniReclusione, BigDecimal aImportoMulta, BigDecimal aNumAnniArresto,
			BigDecimal aNumMesiArresto, BigDecimal aNumGiorniArresto, BigDecimal aImportoAmmenda,
			Date aDataDecorrenzaPena, String aMotivazioni, BigDecimal aNumAnniReclusioneSosp,
			BigDecimal aNumMesiReclusioneSosp, BigDecimal aNumGiorniReclusioneSosp,
			BigDecimal aNumAnniArrestoSosp, BigDecimal aNumMesiArrestoSosp, BigDecimal aNumGiorniArrestoSosp,
			String aEstremiOrdinanza, String aFlagErgastolo, String aNote, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aCumIdCumulo,

			String aMisuraSicurezza, String aPenaAccessoria, BigDecimal aNumGiorniLibAnticipata,

			BigDecimal aNumAnniIsolamentoDiurno, BigDecimal aNumMesiIsolamentoDiurno,
			BigDecimal aNumGiorniIsolamentoDiurno,
			// 20/05/2014 - Nuova L.A. - decreto 2013/146
			BigDecimal aNumGiorniLibAnticipataLA, BigDecimal aNumGiorniLibAnticipataSPE,
			BigDecimal aNumGiorniLibAnticipataINT,

			BigDecimal aNumGiorniRiduzionePena) {
		this.mIdPenaCumulo = aIdPenaCumulo;
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
		this.mDataDecorrenzaPena = aDataDecorrenzaPena;
		this.mMotivazioni = aMotivazioni;
		this.mNumAnniReclusioneSosp = aNumAnniReclusioneSosp;
		this.mNumMesiReclusioneSosp = aNumMesiReclusioneSosp;
		this.mNumGiorniReclusioneSosp = aNumGiorniReclusioneSosp;
		this.mNumAnniArrestoSosp = aNumAnniArrestoSosp;
		this.mNumMesiArrestoSosp = aNumMesiArrestoSosp;
		this.mNumGiorniArrestoSosp = aNumGiorniArrestoSosp;
		this.mEstremiOrdinanza = aEstremiOrdinanza;
		this.mFlagErgastolo = aFlagErgastolo;
		this.mNote = aNote;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mCumIdCumulo = aCumIdCumulo;

		this.mNumAnniIsolamentoDiurno = aNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aNumGiorniIsolamentoDiurno;

		this.mMisuraSicurezza = aMisuraSicurezza;
		this.mPenaAccessoria = aPenaAccessoria;
		this.mNumGiorniLibAnticipata = aNumGiorniLibAnticipata;
		// 20/05/2014 - Nuova L.A. - decreto 2013/146
		this.mNumGiorniLibAnticipataLA = aNumGiorniLibAnticipataLA;
		this.mNumGiorniLibAnticipataSPE = aNumGiorniLibAnticipataSPE;
		this.mNumGiorniLibAnticipataINT = aNumGiorniLibAnticipataINT;

		this.mNumGiorniRiduzionePena = aNumGiorniRiduzionePena;

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdPenaCumulo() {
		return mIdPenaCumulo;
	}

	public String getCodTipoPenaDetentiva() {
		return mCodTipoPenaDetentiva;
	}

	public String getDescrTipoPenaDetentiva() {
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

	public Date getDataDecorrenzaPena() {
		return mDataDecorrenzaPena;
	}

	public String getMotivazioni() {
		return mMotivazioni;
	}

	public BigDecimal getNumAnniReclusioneSosp() {
		return mNumAnniReclusioneSosp;
	}

	public BigDecimal getNumMesiReclusioneSosp() {
		return mNumMesiReclusioneSosp;
	}

	public BigDecimal getNumGiorniReclusioneSosp() {
		return mNumGiorniReclusioneSosp;
	}

	public BigDecimal getNumAnniArrestoSosp() {
		return mNumAnniArrestoSosp;
	}

	public BigDecimal getNumMesiArrestoSosp() {
		return mNumMesiArrestoSosp;
	}

	public BigDecimal getNumGiorniArrestoSosp() {
		return mNumGiorniArrestoSosp;
	}

	public String getEstremiOrdinanza() {
		return mEstremiOrdinanza;
	}

	public String getFlagErgastolo() {
		return mFlagErgastolo;
	}

	public String getNote() {
		return mNote;
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

	public BigDecimal getCumIdCumulo() {
		return mCumIdCumulo;
	}

	public String getMisuraSicurezza() {
		return mMisuraSicurezza;
	}

	public String getPenaAccessoria() {
		return mPenaAccessoria;
	}

	public BigDecimal getNumGiorniLibAnticipata() {
		if (mNumGiorniLibAnticipata != null)
			return mNumGiorniLibAnticipata;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumAnniIsolamentoDiurno() {
		if (mNumAnniIsolamentoDiurno != null)
			return mNumAnniIsolamentoDiurno;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiIsolamentoDiurno() {
		if (mNumMesiIsolamentoDiurno != null)
			return mNumMesiIsolamentoDiurno;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniIsolamentoDiurno() {
		if (mNumGiorniIsolamentoDiurno != null)
			return mNumGiorniIsolamentoDiurno;
		else
			return new BigDecimal(0);
	}

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	public String getStringaReclusioneSosp() {
		return mStringaReclusioneSosp;
	}

	public String getStringaArrestoSosp() {
		return mStringaArrestoSosp;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public String getStringaIsolamentoDiurno() {
		return mStringaIsolamentoDiurno;
	}

	// 20/05/2014 - Nuova L.A. - decreto 2013/146
	public BigDecimal getNumGiorniLibAnticipataLA() {
		return mNumGiorniLibAnticipataLA;
	}

	public BigDecimal getNumGiorniLibAnticipataSPE() {
		return mNumGiorniLibAnticipataSPE;
	}

	public BigDecimal getNumGiorniLibAnticipataINT() {
		return mNumGiorniLibAnticipataINT;
	}

	public BigDecimal getNumGiorniRiduzionePena() {
		return mNumGiorniRiduzionePena;
	}

	//
	// METODI SET()
	//

	public void setIdPenaCumulo(BigDecimal aValore) {
		mIdPenaCumulo = aValore;
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

	public void setDataDecorrenzaPena(Date aValore) {
		mDataDecorrenzaPena = aValore;
	}

	public void setMotivazioni(String aValore) {
		mMotivazioni = aValore;
	}

	public void setNumAnniReclusioneSosp(BigDecimal aValore) {
		mNumAnniReclusioneSosp = aValore;
	}

	public void setNumMesiReclusioneSosp(BigDecimal aValore) {
		mNumMesiReclusioneSosp = aValore;
	}

	public void setNumGiorniReclusioneSosp(BigDecimal aValore) {
		mNumGiorniReclusioneSosp = aValore;
	}

	public void setNumAnniArrestoSosp(BigDecimal aValore) {
		mNumAnniArrestoSosp = aValore;
	}

	public void setNumMesiArrestoSosp(BigDecimal aValore) {
		mNumMesiArrestoSosp = aValore;
	}

	public void setNumGiorniArrestoSosp(BigDecimal aValore) {
		mNumGiorniArrestoSosp = aValore;
	}

	public void setEstremiOrdinanza(String aValore) {
		mEstremiOrdinanza = aValore;
	}

	public void setFlagErgastolo(String aValore) {
		mFlagErgastolo = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
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

	public void setCumIdCumulo(BigDecimal aValore) {
		mCumIdCumulo = aValore;
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

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setStringaArrestoSosp(String aValore) {
		mStringaArrestoSosp = aValore;
	}

	public void setStringaReclusioneSosp(String aValore) {
		mStringaReclusioneSosp = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setMisuraSicurezza(String aValore) {
		mMisuraSicurezza = aValore;
	}

	public void setPenaAccessoria(String aValore) {
		mPenaAccessoria = aValore;
	}

	public void setNumGiorniLibAnticipata(BigDecimal aValore) {
		mNumGiorniLibAnticipata = aValore;
	}

	public void setStringaIsolamentoDiurno(String aValore) {
		mStringaIsolamentoDiurno = aValore;
	}

	// 20/05/2014 - Nuova L.A. - decreto 2013/146
	public void setNumGiorniLibAnticipataLA(BigDecimal aValore) {
		mNumGiorniLibAnticipataLA = aValore;
	}

	public void setNumGiorniLibAnticipataSPE(BigDecimal aValore) {
		mNumGiorniLibAnticipataSPE = aValore;
	}

	public void setNumGiorniLibAnticipataINT(BigDecimal aValore) {
		mNumGiorniLibAnticipataINT = aValore;
	}

	public void setNumGiorniRiduzionePena(BigDecimal aValore) {
		mNumGiorniRiduzionePena = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdPenaCumulo + " - " + mCodTipoPenaDetentiva + " - " + mDescrTipoPenaDetentiva + " - "
				+ mNumAnniReclusione + " - " + mNumMesiReclusione + " - " + mNumGiorniReclusione + " - "
				+ mImportoMulta + " - " + mNumAnniArresto + " - " + mNumMesiArresto + " - "
				+ mNumGiorniArresto + " - " + mImportoAmmenda + " - " + mDataDecorrenzaPena + " - "
				+ mMotivazioni + " - " + mNumAnniReclusioneSosp + " - " + mNumMesiReclusioneSosp + " - "
				+ mNumGiorniReclusioneSosp + " - " + mNumAnniArrestoSosp + " - " + mNumMesiArrestoSosp + " - "
				+ mNumGiorniArrestoSosp + " - " + mEstremiOrdinanza + " - " + mFlagErgastolo + " - " + mNote
				+ " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento
				+ " - " + mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - "
				+ mDataAggiornamento + " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento
				+ " - " + mCumIdCumulo + " - " + mMisuraSicurezza + " - " + mPenaAccessoria + " - "
				+ mNumGiorniLibAnticipata + " - " + mNumAnniIsolamentoDiurno + " - "
				+ mNumMesiIsolamentoDiurno + " - " + mNumGiorniIsolamentoDiurno + " - "
				+ mNumGiorniLibAnticipataLA + " - " + mNumGiorniLibAnticipataSPE + " - "
				+ mNumGiorniLibAnticipataINT + " - " + mNumGiorniRiduzionePena;

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
	 * calcolaStringaArrestoSospensione per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaArrestoSosp() {
		String lStringArrestoSosp = "";
		if (this.getNumAnniArrestoSosp() != null) {
			if (this.getNumAnniArrestoSosp().intValue() != 0)
				lStringArrestoSosp = "Anni " + this.getNumAnniArrestoSosp();
		}
		if (this.getNumMesiArrestoSosp() != null) {
			if (this.getNumMesiArrestoSosp().intValue() != 0)
				lStringArrestoSosp += " Mesi " + this.getNumMesiArrestoSosp();
		}
		if (this.getNumGiorniArrestoSosp() != null) {
			if (this.getNumGiorniArrestoSosp().intValue() != 0)
				lStringArrestoSosp += " Giorni " + this.getNumGiorniArrestoSosp();
		}

		if (lStringArrestoSosp.length() > 1)
			this.mStringaArrestoSosp = lStringArrestoSosp;
		else
			this.mStringaArrestoSosp = null;
	}

	/**
	 * calcolaStringaReclusioneSospensione per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaReclusioneSosp() {
		String lStringReclusioneSosp = "";
		if (this.getNumAnniReclusioneSosp() != null) {
			if (this.getNumAnniReclusioneSosp().intValue() != 0)
				lStringReclusioneSosp = "Anni " + this.getNumAnniReclusioneSosp();
		}
		if (this.getNumMesiReclusioneSosp() != null) {
			if (this.getNumMesiReclusioneSosp().intValue() != 0)
				lStringReclusioneSosp += " Mesi " + this.getNumMesiReclusioneSosp();
		}
		if (this.getNumGiorniReclusioneSosp() != null) {
			if (this.getNumGiorniReclusioneSosp().intValue() != 0)
				lStringReclusioneSosp += " Giorni " + this.getNumGiorniReclusioneSosp();
		}

		if (lStringReclusioneSosp.length() > 1)
			this.mStringaReclusioneSosp = lStringReclusioneSosp;
		else
			this.mStringaReclusioneSosp = null;
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
