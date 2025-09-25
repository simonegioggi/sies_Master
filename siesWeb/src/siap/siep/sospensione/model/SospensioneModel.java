package siap.siep.sospensione.model;

/**
* <p>Title: SospensioneModel</p>
* <p>Description: Classe Model che rappresenta il Sospensione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class SospensioneModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8659112782452125482L;

	private BigDecimal mIdSospensione;
	private Date mDataInizio;
	private Date mDataFine;
	private BigDecimal mNumAnniRinvio;
	private BigDecimal mNumMesiRinvio;
	private BigDecimal mNumGiorniRinvio;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mPenResIdPenaResidua;
	private BigDecimal mNumAnniPenaEspiata;
	private BigDecimal mNumMesiPenaEspiata;
	private BigDecimal mNumGiorniPenaEspiata;
	private BigDecimal mNumAnniPenaResiduaReclus;
	private BigDecimal mNumMesiPenaResiduaReclus;
	private BigDecimal mNumGiorniPenaResiduaReclus;
	private BigDecimal mNumAnniPenaResiduaArres;
	private BigDecimal mNumMesiPenaResiduaArres;
	private BigDecimal mNumGiorniPenaResiduaArres;
	private BigDecimal mNumAnniInterruzione;
	private BigDecimal mNumMesiInterruzione;
	private BigDecimal mNumGiorniInterruzione;
	private BigDecimal mMultaEspiata;
	private BigDecimal mAmmendaEspiata;
	private BigDecimal mMultaResidua;
	private BigDecimal mAmmendaResidua;
	private BigDecimal mFasSieIdFascicoloSiep;
	private String mFlagInterruzione;
	private Date mAssenzaDa;
	private Date mAssenzaA;
	private BigDecimal mNumGiorniLibanticipata;

	// COSTRUTTORE DI DEFAULT
	public SospensioneModel() {
		this.mIdSospensione = null;
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mNumAnniRinvio = null;
		this.mNumMesiRinvio = null;
		this.mNumGiorniRinvio = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mPenResIdPenaResidua = null;
		this.mNumAnniPenaEspiata = null;
		this.mNumMesiPenaEspiata = null;
		this.mNumGiorniPenaEspiata = null;
		this.mNumAnniPenaResiduaReclus = null;
		this.mNumMesiPenaResiduaReclus = null;
		this.mNumGiorniPenaResiduaReclus = null;
		this.mNumAnniPenaResiduaArres = null;
		this.mNumMesiPenaResiduaArres = null;
		this.mNumGiorniPenaResiduaArres = null;
		this.mNumAnniInterruzione = null;
		this.mNumMesiInterruzione = null;
		this.mNumGiorniInterruzione = null;
		this.mMultaEspiata = null;
		this.mAmmendaEspiata = null;
		this.mMultaResidua = null;
		this.mAmmendaResidua = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mFlagInterruzione = "";
		this.mNumGiorniLibanticipata = null;
	}

	// COSTRUTTORE DI COPIA
	public SospensioneModel(SospensioneModel aModel) {
		this.mIdSospensione = aModel.mIdSospensione;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mNumAnniRinvio = aModel.mNumAnniRinvio;
		this.mNumMesiRinvio = aModel.mNumMesiRinvio;
		this.mNumGiorniRinvio = aModel.mNumGiorniRinvio;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mPenResIdPenaResidua = aModel.mPenResIdPenaResidua;
		this.mNumAnniPenaEspiata = aModel.mNumAnniPenaEspiata;
		this.mNumMesiPenaEspiata = aModel.mNumMesiPenaEspiata;
		this.mNumGiorniPenaEspiata = aModel.mNumGiorniPenaEspiata;
		this.mNumAnniPenaResiduaReclus = aModel.mNumAnniPenaResiduaReclus;
		this.mNumMesiPenaResiduaReclus = aModel.mNumMesiPenaResiduaReclus;
		this.mNumGiorniPenaResiduaReclus = aModel.mNumGiorniPenaResiduaReclus;
		this.mNumAnniPenaResiduaArres = aModel.mNumAnniPenaResiduaArres;
		this.mNumMesiPenaResiduaArres = aModel.mNumMesiPenaResiduaArres;
		this.mNumGiorniPenaResiduaArres = aModel.mNumGiorniPenaResiduaArres;
		this.mNumAnniInterruzione = aModel.mNumAnniInterruzione;
		this.mNumMesiInterruzione = aModel.mNumMesiInterruzione;
		this.mNumGiorniInterruzione = aModel.mNumGiorniInterruzione;
		this.mMultaEspiata = aModel.mMultaEspiata;
		this.mAmmendaEspiata = aModel.mAmmendaEspiata;
		this.mMultaResidua = aModel.mMultaResidua;
		this.mAmmendaResidua = aModel.mAmmendaResidua;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mFlagInterruzione = aModel.mFlagInterruzione;
		this.mNumGiorniLibanticipata = aModel.mNumGiorniLibanticipata;
	}

	// COSTRUTTORE MODEL
	public SospensioneModel(BigDecimal aIdSospensione, Date aDataInizio, Date aDataFine,
			BigDecimal aNumAnniRinvio, BigDecimal aNumMesiRinvio, BigDecimal aNumGiorniRinvio,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aPenResIdPenaResidua, BigDecimal aNumAnniPenaEspiata, BigDecimal aNumMesiPenaEspiata,
			BigDecimal aNumGiorniPenaEspiata, BigDecimal aNumAnniPenaResiduaReclus,
			BigDecimal aNumMesiPenaResiduaReclus, BigDecimal aNumGiorniPenaResiduaReclus,
			BigDecimal aNumAnniPenaResiduaArres, BigDecimal aNumMesiPenaResiduaArres,
			BigDecimal aNumGiorniPenaResiduaArres, BigDecimal aNumAnniInterruzione,
			BigDecimal aNumMesiInterruzione, BigDecimal aNumGiorniInterruzione, BigDecimal aMultaEspiata,
			BigDecimal aAmmendaEspiata, BigDecimal aMultaResidua, BigDecimal aAmmendaResidua,
			BigDecimal aFasSieIdFascicoloSiep, String aFlagInterruzione, BigDecimal aNumGiorniLibanticipata) {
		this.mIdSospensione = aIdSospensione;
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mNumAnniRinvio = aNumAnniRinvio;
		this.mNumMesiRinvio = aNumMesiRinvio;
		this.mNumGiorniRinvio = aNumGiorniRinvio;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mPenResIdPenaResidua = aPenResIdPenaResidua;
		this.mNumAnniPenaEspiata = aNumAnniPenaEspiata;
		this.mNumMesiPenaEspiata = aNumMesiPenaEspiata;
		this.mNumGiorniPenaEspiata = aNumGiorniPenaEspiata;
		this.mNumAnniPenaResiduaReclus = aNumAnniPenaResiduaReclus;
		this.mNumMesiPenaResiduaReclus = aNumMesiPenaResiduaReclus;
		this.mNumGiorniPenaResiduaReclus = aNumGiorniPenaResiduaReclus;
		this.mNumAnniPenaResiduaArres = aNumAnniPenaResiduaArres;
		this.mNumMesiPenaResiduaArres = aNumMesiPenaResiduaArres;
		this.mNumGiorniPenaResiduaArres = aNumGiorniPenaResiduaArres;
		this.mNumAnniInterruzione = aNumAnniInterruzione;
		this.mNumMesiInterruzione = aNumMesiInterruzione;
		this.mNumGiorniInterruzione = aNumGiorniInterruzione;
		this.mMultaEspiata = aMultaEspiata;
		this.mAmmendaEspiata = aAmmendaEspiata;
		this.mMultaResidua = aMultaResidua;
		this.mAmmendaResidua = aAmmendaResidua;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mFlagInterruzione = aFlagInterruzione;
		this.mNumGiorniLibanticipata = aNumGiorniLibanticipata;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdSospensione() {
		return mIdSospensione;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public BigDecimal getNumAnniRinvio() {
		return mNumAnniRinvio;
	}

	public BigDecimal getNumMesiRinvio() {
		return mNumMesiRinvio;
	}

	public BigDecimal getNumGiorniRinvio() {
		return mNumGiorniRinvio;
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

	public BigDecimal getPenResIdPenaResidua() {
		return mPenResIdPenaResidua;
	}

	public Date getAssenzaDa() {
		return mAssenzaDa;
	}

	public Date getAssenzaA() {
		return mAssenzaA;
	}

	public BigDecimal getNumAnniPenaEspiata() {
		if (mNumAnniPenaEspiata != null)
			return mNumAnniPenaEspiata;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiPenaEspiata() {
		if (mNumMesiPenaEspiata != null)
			return mNumMesiPenaEspiata;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniPenaEspiata() {
		if (mNumGiorniPenaEspiata != null)
			return mNumGiorniPenaEspiata;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumAnniPenaResiduaReclus() {
		if (mNumAnniPenaResiduaReclus != null)
			return mNumAnniPenaResiduaReclus;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiPenaResiduaReclus() {
		if (mNumMesiPenaResiduaReclus != null)
			return mNumMesiPenaResiduaReclus;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniPenaResiduaReclus() {
		if (mNumGiorniPenaResiduaReclus != null)
			return mNumGiorniPenaResiduaReclus;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumAnniPenaResiduaArres() {
		if (mNumAnniPenaResiduaArres != null)
			return mNumAnniPenaResiduaArres;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiPenaResiduaArres() {
		if (mNumMesiPenaResiduaArres != null)
			return mNumMesiPenaResiduaArres;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniPenaResiduaArres() {
		if (mNumGiorniPenaResiduaArres != null)
			return mNumGiorniPenaResiduaArres;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumAnniInterruzione() {
		if (mNumAnniInterruzione != null)
			return mNumAnniInterruzione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiInterruzione() {
		if (mNumMesiInterruzione != null)
			return mNumMesiInterruzione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniInterruzione() {
		if (mNumGiorniInterruzione != null)
			return mNumGiorniInterruzione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getMultaEspiata() {
		return mMultaEspiata;
	}

	public BigDecimal getAmmendaEspiata() {
		return mAmmendaEspiata;
	}

	public BigDecimal getMultaResidua() {
		return mMultaResidua;
	}

	public BigDecimal getAmmendaResidua() {
		return mAmmendaResidua;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public String getFlagInterruzione() {
		return mFlagInterruzione;
	}

	public BigDecimal getNumGiorniLibanticipata() {
		if (mNumGiorniLibanticipata != null)
			return mNumGiorniLibanticipata;
		else
			return new BigDecimal(0);
	}

	//
	// METODI SET()
	//

	public void setIdSospensione(BigDecimal aValore) {
		mIdSospensione = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setNumAnniRinvio(BigDecimal aValore) {
		mNumAnniRinvio = aValore;
	}

	public void setNumMesiRinvio(BigDecimal aValore) {
		mNumMesiRinvio = aValore;
	}

	public void setNumGiorniRinvio(BigDecimal aValore) {
		mNumGiorniRinvio = aValore;
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

	public void setPenResIdPenaResidua(BigDecimal aValore) {
		mPenResIdPenaResidua = aValore;
	}

	public void setNumAnniPenaEspiata(BigDecimal aValore) {
		mNumAnniPenaEspiata = aValore;
	}

	public void setNumMesiPenaEspiata(BigDecimal aValore) {
		mNumMesiPenaEspiata = aValore;
	}

	public void setNumGiorniPenaEspiata(BigDecimal aValore) {
		mNumGiorniPenaEspiata = aValore;
	}

	public void setNumAnniPenaResiduaReclus(BigDecimal aValore) {
		mNumAnniPenaResiduaReclus = aValore;
	}

	public void setNumMesiPenaResiduaReclus(BigDecimal aValore) {
		mNumMesiPenaResiduaReclus = aValore;
	}

	public void setNumGiorniPenaResiduaReclus(BigDecimal aValore) {
		mNumGiorniPenaResiduaReclus = aValore;
	}

	public void setNumAnniPenaResiduaArres(BigDecimal aValore) {
		mNumAnniPenaResiduaArres = aValore;
	}

	public void setNumMesiPenaResiduaArres(BigDecimal aValore) {
		mNumMesiPenaResiduaArres = aValore;
	}

	public void setNumGiorniPenaResiduaArres(BigDecimal aValore) {
		mNumGiorniPenaResiduaArres = aValore;
	}

	public void setNumAnniInterruzione(BigDecimal aValore) {
		mNumAnniInterruzione = aValore;
	}

	public void setNumMesiInterruzione(BigDecimal aValore) {
		mNumMesiInterruzione = aValore;
	}

	public void setNumGiorniInterruzione(BigDecimal aValore) {
		mNumGiorniInterruzione = aValore;
	}

	public void setMultaEspiata(BigDecimal aValore) {
		mMultaEspiata = aValore;
	}

	public void setAmmendaEspiata(BigDecimal aValore) {
		mAmmendaEspiata = aValore;
	}

	public void setMultaResidua(BigDecimal aValore) {
		mMultaResidua = aValore;
	}

	public void setAmmendaResidua(BigDecimal aValore) {
		mAmmendaResidua = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setFlagInterruzione(String aValore) {
		mFlagInterruzione = aValore;
	}

	public void setAssenzaDa(Date aValore) {
		mAssenzaDa = aValore;
	}

	public void setAssenzaA(Date aValore) {
		mAssenzaA = aValore;
	}

	public void setNumGiorniLibanticipata(BigDecimal aValore) {
		mNumGiorniLibanticipata = aValore;
	}

	/*
	 * Metodo che inizializza i campi BigDecimal dei campi quantum a 0
	 */
	public void setQuantumZero() {
		this.mNumAnniRinvio = new BigDecimal(0);
		this.mNumMesiRinvio = new BigDecimal(0);
		this.mNumGiorniRinvio = new BigDecimal(0);

		this.mNumAnniPenaEspiata = new BigDecimal(0);
		this.mNumMesiPenaEspiata = new BigDecimal(0);
		this.mNumGiorniPenaEspiata = new BigDecimal(0);
		this.mNumAnniPenaResiduaReclus = new BigDecimal(0);
		this.mNumMesiPenaResiduaReclus = new BigDecimal(0);
		this.mNumGiorniPenaResiduaReclus = new BigDecimal(0);
		this.mNumAnniPenaResiduaArres = new BigDecimal(0);
		this.mNumMesiPenaResiduaArres = new BigDecimal(0);
		this.mNumGiorniPenaResiduaArres = new BigDecimal(0);
		this.mNumAnniInterruzione = new BigDecimal(0);
		this.mNumMesiInterruzione = new BigDecimal(0);
		this.mNumGiorniInterruzione = new BigDecimal(0);
	}

	// MEV_9-SIEP aggiunto metodo di comodo per jsp
	public boolean isQuantumEspiataZero() {
		return (getNumAnniPenaEspiata().intValue() == 0 && getNumMesiPenaEspiata().intValue() == 0
				&& getNumGiorniPenaEspiata().intValue() == 0);
	}
	
	public boolean isQuantumReclusioneResiduoZero() {
		return (getNumAnniPenaResiduaReclus().intValue() == 0 && getNumMesiPenaResiduaReclus().intValue() == 0
				&& getNumGiorniPenaResiduaReclus().intValue() == 0);
	}
	
	public boolean isQuantumArrestoResiduoZero() {
		return (getNumAnniPenaResiduaArres().intValue() == 0 && getNumMesiPenaResiduaArres().intValue() == 0
				&& getNumGiorniPenaResiduaArres().intValue() == 0);
	}
	// MEV_9-SIEP - FINE
	
	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SospensioneModel:\n" + "[ mIdSospensione              = " + mIdSospensione + " ]\n"
				+ "[ mDataInizio                 = " + mDataInizio + " ]\n"
				+ "[ mDataFine                   = " + mDataFine + " ]\n" + "[ mNumAnniRinvio              = "
				+ mNumAnniRinvio + " ]\n" + "[ mNumMesiRinvio              = " + mNumMesiRinvio + " ]\n"
				+ "[ mNumGiorniRinvio            = " + mNumGiorniRinvio + " ]\n"
				+ "[ mPenResIdPenaResidua        = " + mPenResIdPenaResidua + " ]\n"
				+ "[ mNumAnniPenaEspiata         = " + mNumAnniPenaEspiata + " ]\n"
				+ "[ mNumMesiPenaEspiata         = " + mNumMesiPenaEspiata + " ]\n"
				+ "[ mNumGiorniPenaEspiata       = " + mNumGiorniPenaEspiata + " ]\n"
				+ "[ mNumAnniPenaResiduaReclus   = " + mNumAnniPenaResiduaReclus + " ]\n"
				+ "[ mNumMesiPenaResiduaReclus   = " + mNumMesiPenaResiduaReclus + " ]\n"
				+ "[ mNumGiorniPenaResiduaReclus = " + mNumGiorniPenaResiduaReclus + " ]\n"
				+ "[ mNumAnniPenaResiduaArres    = " + mNumAnniPenaResiduaArres + " ]\n"
				+ "[ mNumMesiPenaResiduaArres    = " + mNumMesiPenaResiduaArres + " ]\n"
				+ "[ mNumGiorniPenaResiduaArres  = " + mNumGiorniPenaResiduaArres + " ]\n"
				+ "[ mNumAnniInterruzione        = " + mNumAnniInterruzione + " ]\n"
				+ "[ mNumMesiInterruzione        = " + mNumMesiInterruzione + " ]\n"
				+ "[ mNumGiorniInterruzione      = " + mNumGiorniInterruzione + " ]\n"
				+ "[ mMultaEspiata               = " + mMultaEspiata + " ]\n"
				+ "[ mAmmendaEspiata             = " + mAmmendaEspiata + " ]\n"
				+ "[ mMultaResidua               = " + mMultaResidua + " ]\n"
				+ "[ mAmmendaResidua             = " + mAmmendaResidua + " ]\n"
				+ "[ mCodOperatoreInserimento    = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento            = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento      = " + mCodUfficioInserimento + " ]\n"
				+ "[ mDescrUfficioInserimento    = " + mDescrUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento  = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento          = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento    = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mDescrUfficioAggiornamento  = " + mDescrUfficioAggiornamento + " ]\n"
				+ "[ mFasSieIdFascicoloSiep      = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mNumGiorniLibanticipata     = " + mNumGiorniLibanticipata + " ]\n"
				+ "[ mFlagInterruzione           = " + mFlagInterruzione + " ]";

		return lStr;
	}

	public String toString2() {
		String lStr = new String();

		lStr = "" + mIdSospensione + " - " + mDataInizio + " - " + mDataFine + " - " + mNumAnniRinvio + " - "
				+ mNumMesiRinvio + " - " + mNumGiorniRinvio + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mPenResIdPenaResidua + " - "
				+ mNumAnniPenaEspiata + " - " + mNumMesiPenaEspiata + " - " + mNumGiorniPenaEspiata + " - "
				+ mNumAnniPenaResiduaReclus + " - " + mNumMesiPenaResiduaReclus + " - "
				+ mNumGiorniPenaResiduaReclus + " - " + mNumAnniPenaResiduaArres + " - "
				+ mNumMesiPenaResiduaArres + " - " + mNumGiorniPenaResiduaArres + " - " + mNumAnniInterruzione
				+ " - " + mNumMesiInterruzione + " - " + mNumGiorniInterruzione + " - " + mMultaEspiata
				+ " - " + mAmmendaEspiata + " - " + mMultaResidua + " - " + mAmmendaResidua + " - "
				+ mFasSieIdFascicoloSiep + " - " + mFlagInterruzione;

		return lStr;
	}

}
