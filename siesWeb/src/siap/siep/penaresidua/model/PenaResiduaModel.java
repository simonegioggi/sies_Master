package siap.siep.penaresidua.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.sico.calendar.model.CalendarModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;

/**
 * <p>
 * Title: PenaResiduaModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il PenaResidua
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class PenaResiduaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 8709571809380682399L;
	private BigDecimal mIdPenaResidua;
	private Date mDataInizio;
	private Date mDataFine;
	private BigDecimal mNumAnniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mImportoMulta;
	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumGiorniArresto;
	private BigDecimal mImportoAmmenda;
	private String mDiesAQuo;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mEveIdEvento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mMisAltIdMisuraAlternativa;
	private String mFlagPenaSospesa;
	private String mFlagValidato;
	private Date mDataFinePresunta;
	private Date mDataFineReclusione;
	private Date mDataInizioArresto;
	private String mFlagErgastolo;
	private Date mDataInzioIsolamentoDiurno;
	private Date mDataFineIsolamentoDiurno;
	private BigDecimal mNumAnniIsolamentoDiurno;
	private BigDecimal mNumMesiIsolamentoDiurno;
	private BigDecimal mNumGiorniIsolamentoDiurno;

	// =============================================
	//
	// =============================================
	private String mStringaReclusione;
	private String mStringaArresto;
	private String mStringaReclusioneResidua;
	private String mStringaArrestoResidua;
	private BigDecimal mImportoMultaResidua;
	private BigDecimal mImportoAmmendaResidua;

	private String mImmediataScarcerazione;
	private String mFungibilita;
	private String mStringaIsolamentoDiurno;

	// ===========================================================
	// Campi aggiunti per la gestione delle Sanzioni Sostitutive
	// - dalla 3.0 indicano la SS residua da espiare
	// ==========================================================
	private SanzioneSostResiduaModel mSanzSostResidua;

	/**
	 * Costruttore di default
	 */
	public PenaResiduaModel() {
		this.mIdPenaResidua = null;
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mNumAnniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumGiorniReclusione = null;
		this.mImportoMulta = null;
		this.mNumAnniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumGiorniArresto = null;
		this.mImportoAmmenda = null;
		this.mDiesAQuo = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mEveIdEvento = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mFlagValidato = "";
		this.mDataFinePresunta = null;
		this.mDataFineReclusione = null;
		this.mDataInizioArresto = null;
		this.mFlagErgastolo = "";
		this.mDataInzioIsolamentoDiurno = null;
		this.mDataFineIsolamentoDiurno = null;
		this.mNumAnniIsolamentoDiurno = null;
		this.mNumMesiIsolamentoDiurno = null;
		this.mNumGiorniIsolamentoDiurno = null;
		this.mMisAltIdMisuraAlternativa = null;
		this.mFlagPenaSospesa = "";
		this.mImmediataScarcerazione = "";
		this.mFungibilita = "";

		// Sanzione Sostitutiva
		this.mSanzSostResidua = null;

	}

	/**
	 * Costruttore di copia
	 * 
	 * @param aModel
	 */
	public PenaResiduaModel(PenaResiduaModel aModel) {
		this.mIdPenaResidua = aModel.mIdPenaResidua;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mNumAnniReclusione = aModel.mNumAnniReclusione;
		this.mNumMesiReclusione = aModel.mNumMesiReclusione;
		this.mNumGiorniReclusione = aModel.mNumGiorniReclusione;
		this.mImportoMulta = aModel.mImportoMulta;
		this.mNumAnniArresto = aModel.mNumAnniArresto;
		this.mNumMesiArresto = aModel.mNumMesiArresto;
		this.mNumGiorniArresto = aModel.mNumGiorniArresto;
		this.mImportoAmmenda = aModel.mImportoAmmenda;
		this.mDiesAQuo = aModel.mDiesAQuo;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mFlagValidato = aModel.mFlagValidato;
		this.mDataFinePresunta = aModel.mDataFinePresunta;
		this.mDataFineReclusione = aModel.mDataFineReclusione;
		this.mDataInizioArresto = aModel.mDataInizioArresto;
		this.mFlagErgastolo = aModel.mFlagErgastolo;
		this.mDataInzioIsolamentoDiurno = aModel.mDataInzioIsolamentoDiurno;
		this.mDataFineIsolamentoDiurno = aModel.mDataFineIsolamentoDiurno;
		this.mNumAnniIsolamentoDiurno = aModel.mNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aModel.mNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aModel.mNumGiorniIsolamentoDiurno;
		this.mMisAltIdMisuraAlternativa = aModel.mMisAltIdMisuraAlternativa;
		this.mImmediataScarcerazione = aModel.mImmediataScarcerazione;
		this.mFungibilita = aModel.mFungibilita;

		this.mFlagPenaSospesa = aModel.mFlagPenaSospesa;

		// Campi della Sanzione Sostitutiva da Eseguire
		this.mSanzSostResidua = aModel.mSanzSostResidua;

	}

	/**
	 * Costruttore del Model
	 */
	public PenaResiduaModel(BigDecimal aIdPenaResidua, Date aDataInizio, Date aDataFine,
			BigDecimal aNumAnniReclusione, BigDecimal aNumMesiReclusione, BigDecimal aNumGiorniReclusione,
			BigDecimal aImportoMulta, BigDecimal aNumAnniArresto, BigDecimal aNumMesiArresto,
			BigDecimal aNumGiorniArresto, BigDecimal aImportoAmmenda, String aDiesAQuo,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aEveIdEvento,
			BigDecimal aFasSieIdFascicoloSiep, String aFlagValidato, Date aDataFinePresunta,
			Date aDataFineReclusione, Date aDataInizioArresto, String aFlagErgastolo,
			Date aDataInzioIsolamentoDiurno, Date aDataFineIsolamentoDiurno,
			BigDecimal aNumAnniIsolamentoDiurno, BigDecimal aNumMesiIsolamentoDiurno,
			BigDecimal aNumGiorniIsolamentoDiurno, BigDecimal aMisAltIdMisuraAlternativa,
			String aFlagPenaSospesa) {
		this.mIdPenaResidua = aIdPenaResidua;
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mNumAnniReclusione = aNumAnniReclusione;
		this.mNumMesiReclusione = aNumMesiReclusione;
		this.mNumGiorniReclusione = aNumGiorniReclusione;
		this.mImportoMulta = aImportoMulta;
		this.mNumAnniArresto = aNumAnniArresto;
		this.mNumMesiArresto = aNumMesiArresto;
		this.mNumGiorniArresto = aNumGiorniArresto;
		this.mImportoAmmenda = aImportoAmmenda;
		this.mDiesAQuo = aDiesAQuo;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mEveIdEvento = aEveIdEvento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mFlagValidato = aFlagValidato;
		this.mDataFinePresunta = aDataFinePresunta;
		this.mDataFineReclusione = aDataFineReclusione;
		this.mDataInizioArresto = aDataInizioArresto;
		this.mFlagErgastolo = aFlagErgastolo;
		this.mDataInzioIsolamentoDiurno = aDataInzioIsolamentoDiurno;
		this.mDataFineIsolamentoDiurno = aDataFineIsolamentoDiurno;
		this.mNumAnniIsolamentoDiurno = aNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aNumGiorniIsolamentoDiurno;
		this.mMisAltIdMisuraAlternativa = aMisAltIdMisuraAlternativa;
		this.mFlagPenaSospesa = aFlagPenaSospesa;

	}

	// ===========================
	// METODI GET()
	// ===========================
	public BigDecimal getIdPenaResidua() {
		return mIdPenaResidua;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public BigDecimal getNumAnniReclusione() {
		if (mNumAnniReclusione != null)
			return mNumAnniReclusione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiReclusione() {
		if (mNumMesiReclusione != null)
			return mNumMesiReclusione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniReclusione() {
		if (mNumGiorniReclusione != null)
			return mNumGiorniReclusione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getImportoMulta() {
		if (mImportoMulta != null)
			return mImportoMulta;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumAnniArresto() {
		if (mNumAnniArresto != null)
			return mNumAnniArresto;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiArresto() {
		if (mNumMesiArresto != null)
			return mNumMesiArresto;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniArresto() {
		if (mNumGiorniArresto != null)
			return mNumGiorniArresto;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getImportoAmmenda() {
		if (mImportoAmmenda != null)
			return mImportoAmmenda;
		else
			return new BigDecimal(0);
	}

	public String getDiesAQuo() {
		return mDiesAQuo;
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

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public String getFlagValidato() {
		return mFlagValidato;
	}

	public Date getDataFinePresunta() {
		return mDataFinePresunta;
	}

	public Date getDataFineReclusione() {
		return mDataFineReclusione;
	}

	public Date getDataInizioArresto() {
		return mDataInizioArresto;
	}

	public String getFlagErgastolo() {
		return mFlagErgastolo;
	}

	public Date getDataInizioIsolamentoDiurno() {
		return mDataInzioIsolamentoDiurno;
	}

	public Date getDataFineIsolamentoDiurno() {
		return mDataFineIsolamentoDiurno;
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

	public BigDecimal getMisAltIdMisuraAlternativa() {
		return mMisAltIdMisuraAlternativa;
	}

	public String getImmediataScarcerazione() {
		return mImmediataScarcerazione;
	}

	public String getFungibilita() {
		return mFungibilita;
	}

	public String getFlagPenaSospesa() {
		return mFlagPenaSospesa;
	}

	public String getStringaReclusioneResidua() {
		return mStringaReclusioneResidua;
	}

	public String getStringaArrestoResidua() {
		return mStringaArrestoResidua;
	}

	// public BigDecimal getImportoMultaResidua() { return mImportoMultaResidua;}
	// public BigDecimal getImportoAmmendaResidua() { return mImportoAmmendaResidua;}

	public BigDecimal getImportoMultaResidua() {
		if (mImportoMultaResidua != null)
			return mImportoMultaResidua;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getImportoAmmendaResidua() {
		if (mImportoAmmendaResidua != null)
			return mImportoAmmendaResidua;
		else
			return new BigDecimal(0);
	}

	public String getStringaIsolamentoDiurno() {
		return mStringaIsolamentoDiurno;
	}

	// Sanzione Sostitutiva
	public SanzioneSostResiduaModel getSanzSostResidua() {
		return mSanzSostResidua;
	}

	public String getFlagSanzioneSostitutiva() {
		if (mSanzSostResidua == null) {
			return "N";
		} else {
			return "S";
		}
	}

	public String getCodTipoSanzione() {
		if (mSanzSostResidua == null) {
			return "";
		} else {
			return mSanzSostResidua.getCodTipoSanzione();
		}
	}

	public BigDecimal getNumAnniSS() {
		if (mSanzSostResidua == null) {
			return null;
		} else {
			return mSanzSostResidua.getNumAnni();
		}
	}

	public BigDecimal getNumMesiSS() {
		if (mSanzSostResidua == null) {
			return null;
		} else {
			return mSanzSostResidua.getNumMesi();
		}
	}

	public BigDecimal getNumGiorniSS() {
		if (mSanzSostResidua == null) {
			return null;
		} else {
			return mSanzSostResidua.getNumGiorni();
		}
	}

	public BigDecimal getImportoMultaSS() {
		if (mSanzSostResidua == null) {
			return null;
		} else if (mSanzSostResidua.getSanzionePecuniariaMulta() != null) {
			return mSanzSostResidua.getSanzionePecuniariaMulta();
		} else
			return null;
	}

	public BigDecimal getImportoAmmendaSS() {
		if (mSanzSostResidua == null) {
			return null;
		} else if (mSanzSostResidua.getSanzionePecuniariaAmmenda() != null) {
			return mSanzSostResidua.getSanzionePecuniariaAmmenda();
		} else
			return null;
	}

	public Date getDataInizioSS() {
		if (mSanzSostResidua == null) {
			return null;
		} else {
			return mSanzSostResidua.getDataInizio();
		}
	}

	public Date getDataFineSS() {
		if (mSanzSostResidua == null) {
			return null;
		} else {
			return mSanzSostResidua.getDataFine();
		}
	}

	public String getDescrTipoSanzione() {
		if (mSanzSostResidua == null) {
			return null;
		} else {
			return mSanzSostResidua.getDescrTipoSanzione();
		}
	}

	// ================
	// METODI SET()
	// ================
	public void setIdPenaResidua(BigDecimal aValore) {
		mIdPenaResidua = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
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

	public void setDiesAQuo(String aValore) {
		mDiesAQuo = aValore;
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

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setFlagValidato(String aValore) {
		mFlagValidato = aValore;
	}

	public void setDataFinePresunta(Date aValore) {
		mDataFinePresunta = aValore;
	}

	public void setDataFineReclusione(Date aValore) {
		mDataFineReclusione = aValore;
	}

	public void setDataInizioArresto(Date aValore) {
		mDataInizioArresto = aValore;
	}

	public void setFlagErgastolo(String aValore) {
		mFlagErgastolo = aValore;
	}

	public void setDataInizioIsolamentoDiurno(Date aValore) {
		mDataInzioIsolamentoDiurno = aValore;
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

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setMisAltIdMisuraAlternativa(BigDecimal aValore) {
		mMisAltIdMisuraAlternativa = aValore;
	}

	public void setFlagPenaSospesa(String aValore) {
		mFlagPenaSospesa = aValore;
	}

	public void setImmediataScarcerazione(String aValore) {
		mImmediataScarcerazione = aValore;
	}

	public void setFungibilita(String aValore) {
		mFungibilita = aValore;
	}

	public void setStringaArrestoResidua(String aValore) {
		mStringaArrestoResidua = aValore;
	}

	public void setStringaReclusioneResidua(String aValore) {
		mStringaReclusioneResidua = aValore;
	}

	public void setImportoMultaResidua(BigDecimal aValore) {
		mImportoMultaResidua = aValore;
	}

	public void setImportoAmmendaResidua(BigDecimal aValore) {
		mImportoAmmendaResidua = aValore;
	}

	public void setStringaIsolamentoDiurno(String aValore) {
		mStringaIsolamentoDiurno = aValore;
	}

	// Sanzioni Sostitutive
	public void setSanzSostResidua(SanzioneSostResiduaModel aValore) {
		mSanzSostResidua = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "PenaResiduaModel:\n" + "[ mIdPenaResidua              = " + mIdPenaResidua + " ]\n"
				+ "[ mFasSieIdFascicoloSiep      = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mEveIdEvento                = " + mEveIdEvento + " ]\n"
				+ "[ mDataInizio                 = " + mDataInizio + " ]\n"
				+ "[ mDataFineReclusione         = " + mDataFineReclusione + " ]\n"
				+ "[ mDataInizioArresto          = " + mDataInizioArresto + " ]\n"
				+ "[ mDataFinePresunta           = " + mDataFinePresunta + " ]\n"
				+ "[ mDataFine                   = " + mDataFine + " ]\n" + "[ mNumAnniReclusione          = "
				+ mNumAnniReclusione + " ]\n" + "[ mNumMesiReclusione          = " + mNumMesiReclusione
				+ " ]\n" + "[ mNumGiorniReclusione        = " + mNumGiorniReclusione + " ]\n"
				+ "[ mImportoMulta               = " + mImportoMulta + " ]\n"
				+ "[ mNumAnniArresto             = " + mNumAnniArresto + " ]\n"
				+ "[ mNumMesiArresto             = " + mNumMesiArresto + " ]\n"
				+ "[ mNumGiorniArresto           = " + mNumGiorniArresto + " ]\n"
				+ "[ mImportoAmmenda             = " + mImportoAmmenda + " ]\n"
				+ "[ mDiesAQuo                   = " + mDiesAQuo + " ]\n" + "[ mFlagValidato               = "
				+ mFlagValidato + " ]\n" + "[ mCodOperatoreInserimento    = " + mCodOperatoreInserimento
				+ " ]\n" + "[ mDataInserimento            = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento      = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento  = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento          = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento    = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mFlagErgastolo              = " + mFlagErgastolo + " ]\n"
				+ "[ mDataInizioIsolamentoDiurno = " + mDataInzioIsolamentoDiurno + " ]\n"
				+ "[ mDataFineIsolamentoDiurno   = " + mDataFineIsolamentoDiurno + " ]\n"
				+ "[ mNumAnniIsolamentoDiurno    = " + mNumAnniIsolamentoDiurno + " ]\n"
				+ "[ mNumMesiIsolamentoDiurno    = " + mNumMesiIsolamentoDiurno + " ]\n"
				+ "[ mNumGiorniIsolamentoDiurno  = " + mNumGiorniIsolamentoDiurno + " ]\n"
				+ "[ mMisAltIdMisuraAlternativa  = " + mMisAltIdMisuraAlternativa + " ]\n"
				+ "[ mFlagPenaSospesa            = " + mFlagPenaSospesa + " ]";

		if (mSanzSostResidua != null) {
			String lStrSSres = new String();
			// lStr += "\n"+mSanzSostResidua.toString();

			lStrSSres = "[ mIdSanzioneSostResidua     = " + mSanzSostResidua.getIdSanzioneSostResidua()
					+ " ]\n" + "[ mFasSieIdFascicoloSiep     = " + mSanzSostResidua.getFasSieIdFascicoloSiep()
					+ " ]\n" + "[ mEveIdEvento               = " + mSanzSostResidua.getEveIdEvento() + " ]\n"
					+ "[ mPenResIdPenaResidua       = " + mSanzSostResidua.getPenResIdPenaResidua() + " ]\n"
					+ "[ mCodTipoSanzione           = " + mSanzSostResidua.getCodTipoSanzione() + " ]\n"
					+ "[ mDescrTipoSanzione         = " + mSanzSostResidua.getDescrTipoSanzione() + " ]\n"
					+ "[ mNumAnni                   = " + mSanzSostResidua.getNumAnni() + " ]\n"
					+ "[ mNumMesi                   = " + mSanzSostResidua.getNumMesi() + " ]\n"
					+ "[ mNumGiorni                 = " + mSanzSostResidua.getNumGiorni() + " ]\n"
					+ "[ mSanzionePecuniariaMulta   = " + mSanzSostResidua.getSanzionePecuniariaMulta()
					+ " ]\n" + "[ mSanzionePecuniariaAmmenda = "
					+ mSanzSostResidua.getSanzionePecuniariaAmmenda() + " ]";

			lStr += "\n" + lStrSSres;
		}
		return lStr;
	}

	public String toString2() {
		String lStr = new String();

		lStr = "" + mIdPenaResidua + " - " + mDataInizio + " - " + mDataFine + " - " + mNumAnniReclusione
				+ " - " + mNumMesiReclusione + " - " + mNumGiorniReclusione + " - " + mImportoMulta + " - "
				+ mNumAnniArresto + " - " + mNumMesiArresto + " - " + mNumGiorniArresto + " - "
				+ mImportoAmmenda + " - " + mDiesAQuo + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mEveIdEvento + " - " + mFasSieIdFascicoloSiep
				+ " - " + mFlagValidato + " - " + mDataFinePresunta + " - " + mDataFineReclusione + " - "
				+ mDataInizioArresto + " - " + mFlagErgastolo + " - " + mDataInzioIsolamentoDiurno + " - "
				+ mDataFineIsolamentoDiurno + " - " + mNumAnniIsolamentoDiurno + " - "
				+ mNumMesiIsolamentoDiurno + " - " + mNumGiorniIsolamentoDiurno + " - "
				+ mImmediataScarcerazione + " - " + mFungibilita + " - " + mMisAltIdMisuraAlternativa + " - "
				+ mFlagPenaSospesa;

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

		if (lStringArresto.length() > 1) {
			this.mStringaArresto = lStringArresto;
			this.mStringaArrestoResidua = lStringArresto;

		} else {
			this.mStringaArresto = null;
			this.mStringaArrestoResidua = null;

		}
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

		if (lStringReclusione.length() > 1) {
			this.mStringaReclusione = lStringReclusione;
			this.mStringaReclusioneResidua = lStringReclusione;

		} else {
			this.mStringaReclusione = null;
			this.mStringaReclusioneResidua = null;

		}
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

	public boolean isQuantumReclusioneZero() {
		return (getNumAnniReclusione().intValue() == 0 && getNumMesiReclusione().intValue() == 0
				&& getNumGiorniReclusione().intValue() == 0);
	}

	public boolean isQuantumArrestoZero() {
		return (getNumAnniArresto().intValue() == 0 && getNumMesiArresto().intValue() == 0
				&& getNumGiorniArresto().intValue() == 0);
	}

	public boolean isMultaZero() {
		return (getImportoMulta().intValue() == 0);
	}

	public boolean isAmmendaZero() {
		return (getImportoAmmenda().intValue() == 0);
	}

	public boolean isErgastolo() {
		if (getFlagErgastolo() != null && (getFlagErgastolo().equals("S") || getFlagErgastolo().equals("D")))
			return true;
		else
			return false;
	}

	public boolean isSignificativa() {
		return (!isQuantumReclusioneZero() || !isQuantumArrestoZero() || !isMultaZero() || !isAmmendaZero());
	}

	/**
	 * Setto i parametri che servono per la stampa
	 * 
	 * @param lPos
	 */
	public void setPenaResiduaPerStampa(PosizioneGiuridicaModel lPos) {
		this.calcolaStringaReclusione();
		this.calcolaStringaArresto();
		this.calcolaStringaIsolamento();

		if (((this.getDataFine() != null && this.getDataFine().compareTo(DateUtils.getSysDate()) <= 0)
				&& (lPos != null && !(lPos.isLibero())))
				|| (this.getStringaArresto() == null && this.getStringaReclusione() == null)) {
			this.setImmediataScarcerazione("S");
		} else {
			this.setImmediataScarcerazione("N");
		}

		if ((this.getDataFine() != null && this.getDataFine().compareTo(DateUtils.getSysDate()) < 0)
				&& (lPos != null && !(lPos.isLibero()))) {
			this.setFungibilita("S");
		} else {
			this.setFungibilita("N");
		}
	}

	/**
	 * 
	 * @param aCalReclusione
	 */
	public void setQuantumReclusione(CalendarModel aCalReclusione) {
		this.mNumGiorniReclusione = new BigDecimal(aCalReclusione.getNumGiorni());
		this.mNumMesiReclusione = new BigDecimal(aCalReclusione.getNumMesi());
		this.mNumAnniReclusione = new BigDecimal(aCalReclusione.getNumAnni());
	}

	/**
	 * 
	 * @param aCalArresto
	 */
	public void setQuantumArresto(CalendarModel aCalArresto) {
		this.mNumGiorniArresto = new BigDecimal(aCalArresto.getNumGiorni());
		this.mNumMesiArresto = new BigDecimal(aCalArresto.getNumMesi());
		this.mNumAnniArresto = new BigDecimal(aCalArresto.getNumAnni());
	}

	/**
	* 
	*/
	public CalendarModel getQuantumReclusione() {
		CalendarModel lCalReclusione = new CalendarModel();

		lCalReclusione.setNumGiorni(this.mNumGiorniReclusione);
		lCalReclusione.setNumMesi(this.mNumMesiReclusione);
		lCalReclusione.setNumAnni(this.mNumAnniReclusione);

		return lCalReclusione;
	}

	/**
	* 
	*/
	public CalendarModel getQuantumArresto() {
		CalendarModel lCalArresto = new CalendarModel();

		lCalArresto.setNumGiorni(this.mNumGiorniArresto);
		lCalArresto.setNumMesi(this.mNumMesiArresto);
		lCalArresto.setNumAnni(this.mNumAnniArresto);

		return lCalArresto;
	}

	/**
	 * Restituisce un calendarModel con i dati della Sanzione Sostitutiva
	 * 
	 * @return
	 */
	public CalendarModel getSanzioneResidua() {
		CalendarModel lCalSS = new CalendarModel();

		if (mSanzSostResidua != null) {
			lCalSS.setNumGiorni(mSanzSostResidua.getNumGiorni());
			lCalSS.setNumMesi(mSanzSostResidua.getNumMesi());
			lCalSS.setNumAnni(mSanzSostResidua.getNumAnni());

			if (mSanzSostResidua.getSanzionePecuniariaMulta() != null) {
				lCalSS.setImportoMulta(mSanzSostResidua.getSanzionePecuniariaMulta().doubleValue());
			}
			if (mSanzSostResidua.getSanzionePecuniariaAmmenda() != null) {
				lCalSS.setImportoAmmenda(mSanzSostResidua.getSanzionePecuniariaAmmenda().doubleValue());
			}
		}
		return lCalSS;
	}
}
