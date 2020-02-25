package siap.siep.misurasicurezza.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.evento.model.EventoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;

/**
 * <p>
 * Title: MisuraSicurezzaModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il MisuraSicurezza
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
public class MisuraSicurezzaModel extends GenericModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -626110402999720109L;

	private BigDecimal mIdMisuraSicurezza;
	private String mCodNatura;
	private String mDescrNatura;
	private String mCodTipo;
	private String mDescrTipo;
	private String mCodOggettoEsecuzione;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private BigDecimal mAnnoReg38;
	private BigDecimal mNumReg38;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mFasSiuIdFascicoloSius;
	private BigDecimal mEveIdEvento;

	private BigDecimal mIdFascicoloSiep = null;
	private BigDecimal mChiaveAnno = null;
	private BigDecimal mChiaveProgr = null;
	private String mFlagValidato = null;

	private BigDecimal mFasSieIdFascicoloSiepRif;
	private EventoModel mEvento;
	private BigDecimal mSenIdSentenza;
	private Date mDataDecorrenza;
	private BigDecimal mFlFormaMisura;
	private String mDescrizioneComunita;
	// 12-12-2014
	private String mFlagAnnullaMisura;
	private Date mDataFineValidita;
	private BigDecimal mMisIdMisura;
	// 18-02-2015
	private String mIstDetIdIstitutoDetenzione;
	private String mLuogoEsecuzioneMisura;

	private FascicoloSiepModel mFascicoloSiep;
	private RiferimentoFascicoloSiepModel mRiferimentoFascicoloSiep;

	// mev56 Inizio ***************
	private String mNumOrdDec;
	private String mAnnoOrdDec;
	private Date mDataEmissione;
	private String mLuogoEmittente;
	private String mCodEsito;
	private String mDescEsitoTemplate;
	// aggiungo per parametrizzare decreti/ordinanza (conseguenza all'anomalia 4 della terza sessione collaudo 11.3)
	private String mDescTipoOrdDec;
	private String mDescrMotivoOrdDec;

	// mev56 Fine ***************

	// COSTRUTTORE DI DEFAULT
	public MisuraSicurezzaModel() {

		mIdMisuraSicurezza = null;
		mCodNatura = "";
		mDescrNatura = "";
		mCodTipo = "";
		mDescrTipo = "";
		mCodOggettoEsecuzione = "";
		mNumAnni = null;
		mNumMesi = null;
		mNumGiorni = null;
		mAnnoReg38 = null;
		mNumReg38 = null;
		mCodOperatoreInserimento = "";
		mDataInserimento = null;
		mCodUfficioInserimento = "";
		mDescrUfficioInserimento = "";
		mCodOperatoreAggiornamento = "";
		mDataAggiornamento = null;
		mCodUfficioAggiornamento = "";
		mDescrUfficioAggiornamento = "";
		mFasSieIdFascicoloSiep = null;
		mFasSiuIdFascicoloSius = null;
		mEveIdEvento = null;
		mIdFascicoloSiep = null;
		mChiaveAnno = null;
		mChiaveProgr = null;
		mFlagValidato = null;
		mFasSieIdFascicoloSiepRif = null;
		mRiferimentoFascicoloSiep = null;
		mEvento = null;
		mSenIdSentenza = null;
		mFascicoloSiep = null;
		mDataDecorrenza = null;
		mDescrizioneComunita = "";
		mFlFormaMisura = null;
		mFlagAnnullaMisura = null;
		mDataFineValidita = null;
		mMisIdMisura = null;
		mIstDetIdIstitutoDetenzione = null;
		mLuogoEsecuzioneMisura = null;
	}

	// COSTRUTTORE DI COPIA
	public MisuraSicurezzaModel(MisuraSicurezzaModel aModel) {

		mIdMisuraSicurezza = aModel.mIdMisuraSicurezza;
		mCodNatura = aModel.mCodNatura;
		mDescrNatura = aModel.mDescrNatura;
		mCodTipo = aModel.mCodTipo;
		mDescrTipo = aModel.mDescrTipo;
		mCodOggettoEsecuzione = aModel.mCodOggettoEsecuzione;
		mNumAnni = aModel.mNumAnni;
		mNumMesi = aModel.mNumMesi;
		mNumGiorni = aModel.mNumGiorni;
		mAnnoReg38 = aModel.mAnnoReg38;
		mNumReg38 = aModel.mNumReg38;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		mEveIdEvento = aModel.mEveIdEvento;
		mIdFascicoloSiep = aModel.mIdFascicoloSiep;
		mChiaveAnno = aModel.mChiaveAnno;
		mChiaveProgr = aModel.mChiaveProgr;
		mFlagValidato = aModel.mFlagValidato;
		mFasSieIdFascicoloSiepRif = aModel.mFasSieIdFascicoloSiepRif;
		mRiferimentoFascicoloSiep = aModel.mRiferimentoFascicoloSiep;
		mEvento = aModel.mEvento;
		mSenIdSentenza = aModel.mSenIdSentenza;
		mFascicoloSiep = aModel.mFascicoloSiep;
		mDataDecorrenza = aModel.mDataDecorrenza;
		mFlFormaMisura = aModel.mFlFormaMisura;
		mDescrizioneComunita = aModel.mDescrizioneComunita;
		mFlagAnnullaMisura = aModel.mFlagAnnullaMisura;
		mDataFineValidita = aModel.mDataFineValidita;
		mMisIdMisura = aModel.mMisIdMisura;
		mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		mLuogoEsecuzioneMisura = aModel.mLuogoEsecuzioneMisura;
	}

	// COSTRUTTORE MODEL
	public MisuraSicurezzaModel(BigDecimal aIdMisuraSicurezza, String aCodNatura, String aDescrNatura,
			String aCodTipo, String aDescrTipo, String aCodOggettoEsecuzione, BigDecimal aNumAnni,
			BigDecimal aNumMesi, BigDecimal aNumGiorni, BigDecimal aAnnoReg38, BigDecimal aNumReg38,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aFasSiuIdFascicoloSius, BigDecimal aEveIdEvento,
			BigDecimal aFasSieIdFascicoloSiepRif, BigDecimal aSenIdSentenza, Date aDataDecorrenza,
			BigDecimal aFlFormaMisura, String aDescrizioneComunita, String aFlagAnnullaMisura,
			Date aDataFineValidita, BigDecimal aMisIdMisura, String aIstDetIdIstitutoDetenzione,
			String aLuogoEsecuzioneMisura) {

		mIdMisuraSicurezza = aIdMisuraSicurezza;
		mCodNatura = aCodNatura;
		mDescrNatura = aDescrNatura;
		mCodTipo = aCodTipo;
		mDescrTipo = aDescrTipo;
		mCodOggettoEsecuzione = aCodOggettoEsecuzione;
		mNumAnni = aNumAnni;
		mNumMesi = aNumMesi;
		mNumGiorni = aNumGiorni;
		mAnnoReg38 = aAnnoReg38;
		mNumReg38 = aNumReg38;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mDataInserimento = aDataInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		mEveIdEvento = aEveIdEvento;
		mFasSieIdFascicoloSiepRif = aFasSieIdFascicoloSiepRif;
		mRiferimentoFascicoloSiep = null;
		mEvento = null;
		mSenIdSentenza = aSenIdSentenza;
		mDataDecorrenza = aDataDecorrenza;
		mFlFormaMisura = aFlFormaMisura;
		mDescrizioneComunita = aDescrizioneComunita;
		mFlagAnnullaMisura = aFlagAnnullaMisura;
		mDataFineValidita = aDataFineValidita;
		mMisIdMisura = aMisIdMisura;
		mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		mLuogoEsecuzioneMisura = aLuogoEsecuzioneMisura;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdMisuraSicurezza() {
		return mIdMisuraSicurezza;
	}

	public String getCodNatura() {
		return mCodNatura;
	}

	public String getDescrNatura() {
		return mDescrNatura;
	}

	public String getCodTipo() {
		return mCodTipo;
	}

	public String getDescrTipo() {
		return mDescrTipo;
	}

	public String getCodOggettoEsecuzione() {
		return mCodOggettoEsecuzione;
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

	public BigDecimal getAnnoReg38() {
		return mAnnoReg38;
	}

	public BigDecimal getNumReg38() {
		return mNumReg38;
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

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getIdFascicoloSiep() {
		return mIdFascicoloSiep;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public String getFlagValidato() {
		return mFlagValidato;
	}

	public BigDecimal getFasSieIdFascicoloSiepRif() {
		return mFasSieIdFascicoloSiepRif;
	}

	public RiferimentoFascicoloSiepModel getRiferimentoFascicoloSiep() {
		return mRiferimentoFascicoloSiep;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

	public BigDecimal getSenIdSentenza() {
		return mSenIdSentenza;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	public Date getDataDecorrenza() {
		return mDataDecorrenza;
	}

	public BigDecimal getFlFormaMisura() {
		return mFlFormaMisura;
	}

	public String getDescrizioneComunita() {
		return mDescrizioneComunita;
	}

	public String getFlagAnnullaMisura() {
		return mFlagAnnullaMisura;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public BigDecimal getMisIdMisuraSicurezza() {
		return mMisIdMisura;
	}

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public String getLuogoEsecuzioneMisura() {
		return mLuogoEsecuzioneMisura;
	}

	//
	// METODI SET()
	//

	public void setIdMisuraSicurezza(BigDecimal aValore) {
		mIdMisuraSicurezza = aValore;
	}

	public void setCodNatura(String aValore) {
		mCodNatura = aValore;
	}

	public void setDescrNatura(String aValore) {
		mDescrNatura = aValore;
	}

	public void setCodTipo(String aValore) {
		mCodTipo = aValore;
	}

	public void setDescrTipo(String aValore) {
		mDescrTipo = aValore;
	}

	public void setCodOggettoEsecuzione(String aValore) {
		mCodOggettoEsecuzione = aValore;
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

	public void setAnnoReg38(BigDecimal aValore) {
		mAnnoReg38 = aValore;
	}

	public void setNumReg38(BigDecimal aValore) {
		mNumReg38 = aValore;
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

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setIdFascicoloSiep(BigDecimal aValore) {
		mIdFascicoloSiep = aValore;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setFlagValidato(String aValore) {
		mFlagValidato = aValore;
	}

	public void setFasSieIdFascicoloSiepRif(BigDecimal aValore) {
		mFasSieIdFascicoloSiepRif = aValore;
	}

	public void setRiferimentoFascicoloSiep(RiferimentoFascicoloSiepModel aValore) {
		mRiferimentoFascicoloSiep = aValore;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public void setSenIdSentenza(BigDecimal aValore) {
		mSenIdSentenza = aValore;
	}

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}

	public void setDataDecorrenza(Date aValore) {
		mDataDecorrenza = aValore;
	}

	public void setFlFormaMisura(BigDecimal aValore) {
		mFlFormaMisura = aValore;
	}

	public void setDescrizioneComunita(String aValore) {
		mDescrizioneComunita = aValore;
	}

	public void setFlagAnnullaMisura(String aValore) {
		mFlagAnnullaMisura = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setMisIdMisuraSicurezza(BigDecimal aValore) {
		mMisIdMisura = aValore;
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setLuogoEsecuzioneMisura(String aValore) {
		mLuogoEsecuzioneMisura = aValore;
	}

	public boolean isDurataZero() {
		if ((mNumAnni != null && mNumAnni.intValue() > 0) || (mNumMesi != null && mNumMesi.intValue() > 0)
				|| (mNumGiorni != null && mNumGiorni.intValue() > 0)) {
			return false;
		} else {
			return true;
		}
	}

	public String toString() {
		String lStr = new String();
		lStr = "" + mIdMisuraSicurezza + " - " + mCodNatura + " - " + mDescrNatura + " - " + mCodTipo + " - "
				+ mDescrTipo + " - " + mCodOggettoEsecuzione + " - " + mNumAnni + " - " + mNumMesi + " - "
				+ mNumGiorni + " - " + mAnnoReg38 + " - " + mNumReg38 + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mFasSieIdFascicoloSiep + " - " + mFasSiuIdFascicoloSius + " - " + mEveIdEvento + " - "
				+ mIdFascicoloSiep + " - " + mChiaveAnno + " - " + mChiaveProgr + " - " + mFlagValidato
				+ " - " + mFasSieIdFascicoloSiepRif + " - " + mSenIdSentenza + " - " + mDataDecorrenza + " - "
				+ mFlagAnnullaMisura + " - " + mDataFineValidita + " - " + mMisIdMisura + " - "
				+ mIstDetIdIstitutoDetenzione + " - " + mLuogoEsecuzioneMisura;

		return lStr;
	}

	public String getNumOrdDec() {
		return mNumOrdDec;
	}

	public void setNumOrdDec(String aValore) {
		mNumOrdDec = aValore;
	}

	public String getAnnoOrdDec() {
		return mAnnoOrdDec;
	}

	public void setAnnoOrdDec(String aValore) {
		mAnnoOrdDec = aValore;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public String getLuogoEmittente() {
		return mLuogoEmittente;
	}

	public void setLuogoEmittente(String aValore) {
		mLuogoEmittente = aValore;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public String getDescEsitoTemplate() {
		return mDescEsitoTemplate;
	}

	public void setDescEsitoTemplate(String aValore) {
		mDescEsitoTemplate = aValore;
	}
	
	public String getDescTipoOrdDec() {
		return mDescTipoOrdDec;
	}

	public void setDescTipoOrdDec(String aValore) {
		mDescTipoOrdDec = aValore;
	}
	
	public String getDescrMotivoOrdDec() {
		return mDescrMotivoOrdDec;
	}

	public void setDescrMotivoOrdDec(String aValore) {
		mDescrMotivoOrdDec = aValore;
	}	

}