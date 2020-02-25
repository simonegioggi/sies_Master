package siap.sige.impugnazione.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.siep.notifica.model.NotificaModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ImpugnazioneSigeModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta l' Impugnazione Sige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ImpugnazioneSigeModel extends GenericModel {

	private static final long serialVersionUID = -7501657175700406998L;
	private BigDecimal mIdImpugnazioneSige;
	private BigDecimal mAnnoS7;
	private BigDecimal mProgrS7;
	private String mCodTipoImpugnazione;
	private String mDescrTipoImpugnazione;
	private String mSoggettoImpugnante;
	private String mDescrSoggettoImpugnante;
	private Date mDataRicorso;
	private Date mDataAnnotazione;
	private String mAnnotazione;
	private Date mDataArrivoCancelleria;
	private Date mDataTrasmissioneAtti;
	private String mCodAutoritaDestinataria;
	private String mDescrAutoritaDestinataria;
	private Date mDataDecisione;
	private String mCodTenoreDecisione;
	private String mDescrTenoreDecisione;
	private Date mDataRestituzioneAtti;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mProvvIdProvvedimentoSige;
	private String mFlagAnnullamento;
	private Date mDataAnnullamento;
	private String mMotivoAnnullamento;
	private String mFlagSospEsec;
	private BigDecimal mIdProvvedimentoGenerato;
	private String mConvRicorsoInCass;
	private BigDecimal mIdOpposizioneConvRicorso;

	private ProvvedimentoSigeEventoModel provvedimentoSige;
	private ProvvedimentoSigeEventoModel provvedimentoSigeGenerato;
	private Vector<NotificaModel> notifiche = new Vector<NotificaModel>();
	//@emma 11072018 intervento post COLLAUDO 11.2 
	private String mFlagValidazioneEsito;
	
	// COSTRUTTORE DI DEFAULT
	public ImpugnazioneSigeModel() {
		this.mIdImpugnazioneSige = null;
		this.mAnnoS7 = null;
		this.mProgrS7 = null;
		this.mCodTipoImpugnazione = "";
		this.mDescrTipoImpugnazione = "";
		this.mSoggettoImpugnante = "";
		this.mDescrSoggettoImpugnante = "";
		this.mDataRicorso = null;
		this.mDataAnnotazione = null;
		this.mAnnotazione = "";
		this.mDataArrivoCancelleria = null;
		this.mDataTrasmissioneAtti = null;
		this.mCodAutoritaDestinataria = "";
		this.mDescrAutoritaDestinataria = "";
		this.mDataDecisione = null;
		this.mCodTenoreDecisione = "";
		this.mDescrTenoreDecisione = "";
		this.mDataRestituzioneAtti = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mProvvIdProvvedimentoSige = null;
		this.mFlagAnnullamento = "";
		this.mDataAnnullamento = null;
		this.mMotivoAnnullamento = "";
		this.mFlagSospEsec = "";
		this.mIdProvvedimentoGenerato = null;
		this.mConvRicorsoInCass = "";
		this.mIdOpposizioneConvRicorso = null;
		//@emma 13072018 intervento post COLLAUDO 11.2
		this.mFlagValidazioneEsito = "";
	}

	// COSTRUTTORE DI COPIA
	public ImpugnazioneSigeModel(ImpugnazioneSigeModel aModel) {
		this.mIdImpugnazioneSige = aModel.mIdImpugnazioneSige;
		this.mAnnoS7 = aModel.mAnnoS7;
		this.mProgrS7 = aModel.mProgrS7;
		this.mCodTipoImpugnazione = aModel.mCodTipoImpugnazione;
		this.mDescrTipoImpugnazione = aModel.mDescrTipoImpugnazione;
		this.mSoggettoImpugnante = aModel.mSoggettoImpugnante;
		this.mDescrSoggettoImpugnante = aModel.mDescrSoggettoImpugnante;
		this.mDataRicorso = aModel.mDataRicorso;
		this.mDataAnnotazione = aModel.mDataAnnotazione;
		this.mAnnotazione = aModel.mAnnotazione;
		this.mDataArrivoCancelleria = aModel.mDataArrivoCancelleria;
		this.mDataTrasmissioneAtti = aModel.mDataTrasmissioneAtti;
		this.mCodAutoritaDestinataria = aModel.mCodAutoritaDestinataria;
		this.mDescrAutoritaDestinataria = aModel.mDescrAutoritaDestinataria;
		this.mDataDecisione = aModel.mDataDecisione;
		this.mCodTenoreDecisione = aModel.mCodTenoreDecisione;
		this.mDescrTenoreDecisione = aModel.mDescrTenoreDecisione;
		this.mDataRestituzioneAtti = aModel.mDataRestituzioneAtti;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mProvvIdProvvedimentoSige = aModel.mProvvIdProvvedimentoSige;
		this.mFlagAnnullamento = aModel.mFlagAnnullamento;
		this.mDataAnnullamento = aModel.mDataAnnullamento;
		this.mMotivoAnnullamento = aModel.mMotivoAnnullamento;
		this.mFlagSospEsec = aModel.mFlagSospEsec; // 30/04/2007
		this.mIdProvvedimentoGenerato = aModel.mIdProvvedimentoGenerato;
		this.mConvRicorsoInCass = aModel.mConvRicorsoInCass;
		this.mIdOpposizioneConvRicorso = aModel.mIdOpposizioneConvRicorso;
		//@emma 11072018 intervento post COLLAUDO 11.2 
		this.mFlagValidazioneEsito = aModel.mFlagValidazioneEsito;
	}

	// COSTRUTTORE MODEL
	public ImpugnazioneSigeModel(BigDecimal aIdImpugnazioneSige, BigDecimal aAnnoS7, BigDecimal aProgrS7,
			String aCodTipoImpugnazione, String aDescrTipoImpugnazione, String aSoggettoImpugnante,
			String aDescrSoggettoImpugnante, Date aDataRicorso, Date aDataAnnotazione, String aAnnotazione,
			Date aDataArrivoCancelleria, Date aDataTrasmissioneAtti, String aCodAutoritaDestinataria,
			String aDescrAutoritaDestinataria, Date aDataDecisione, String aCodTenoreDecisione,
			String aDescrTenoreDecisione, Date aDataRestituzioneAtti, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aProvvIdProvvedimentoSige,
			String aFlagAnnullamento, Date aDataAnnullamento, String aMotivoAnnullamento,
			String aFlagSospEsec, BigDecimal aIdProvvedimentoGenerato, String aConvRicorsoInCass,
			//@emma 13072018 intervento post COLLAUDO 11.2
			BigDecimal aIdOpposizioneConvRicorso, String aFlagValidazioneEsito) {
		this.mIdImpugnazioneSige = aIdImpugnazioneSige;
		this.mAnnoS7 = aAnnoS7;
		this.mProgrS7 = aProgrS7;
		this.mCodTipoImpugnazione = aCodTipoImpugnazione;
		this.mDescrTipoImpugnazione = aDescrTipoImpugnazione;
		this.mSoggettoImpugnante = aSoggettoImpugnante;
		this.mDescrSoggettoImpugnante = aDescrSoggettoImpugnante;
		this.mDataRicorso = aDataRicorso;
		this.mDataAnnotazione = aDataAnnotazione;
		this.mAnnotazione = aAnnotazione;
		this.mDataArrivoCancelleria = aDataArrivoCancelleria;
		this.mDataTrasmissioneAtti = aDataTrasmissioneAtti;
		this.mCodAutoritaDestinataria = aCodAutoritaDestinataria;
		this.mDescrAutoritaDestinataria = aDescrAutoritaDestinataria;
		this.mDataDecisione = aDataDecisione;
		this.mCodTenoreDecisione = aCodTenoreDecisione;
		this.mDescrTenoreDecisione = aDescrTenoreDecisione;
		this.mDataRestituzioneAtti = aDataRestituzioneAtti;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mProvvIdProvvedimentoSige = aProvvIdProvvedimentoSige;
		this.mFlagAnnullamento = aFlagAnnullamento;
		this.mDataAnnullamento = aDataAnnullamento;
		this.mMotivoAnnullamento = aMotivoAnnullamento;
		this.mFlagSospEsec = aFlagSospEsec; // 30/04/2007
		this.mProvvIdProvvedimentoSige = aIdProvvedimentoGenerato;
		this.mConvRicorsoInCass = aConvRicorsoInCass;
		this.mIdOpposizioneConvRicorso = aIdOpposizioneConvRicorso;
		//@emma 11072018 intervento post COLLAUDO 11.2
		this.mFlagValidazioneEsito = aFlagValidazioneEsito;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdImpugnazioneSige() {
		return mIdImpugnazioneSige;
	}

	public BigDecimal getAnnoS7() {
		return mAnnoS7;
	}

	public BigDecimal getProgrS7() {
		return mProgrS7;
	}

	public String getCodTipoImpugnazione() {
		return mCodTipoImpugnazione;
	}

	public String getDescrTipoImpugnazione() {
		return mDescrTipoImpugnazione;
	}

	public String getSoggettoImpugnante() {
		return mSoggettoImpugnante;
	}

	public String getDescrSoggettoImpugnante() {
		return mDescrSoggettoImpugnante;
	}

	public Date getDataRicorso() {
		return mDataRicorso;
	}

	public Date getDataAnnotazione() {
		return mDataAnnotazione;
	}

	public String getAnnotazione() {
		return mAnnotazione;
	}

	public Date getDataArrivoCancelleria() {
		return mDataArrivoCancelleria;
	}

	public Date getDataTrasmissioneAtti() {
		return mDataTrasmissioneAtti;
	}

	public String getCodAutoritaDestinataria() {
		return mCodAutoritaDestinataria;
	}

	public String getDescrAutoritaDestinataria() {
		return mDescrAutoritaDestinataria;
	}

	public Date getDataDecisione() {
		return mDataDecisione;
	}

	public String getCodTenoreDecisione() {
		return mCodTenoreDecisione;
	}

	public String getDescrTenoreDecisione() {
		return mDescrTenoreDecisione;
	}

	public Date getDataRestituzioneAtti() {
		return mDataRestituzioneAtti;
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

	public BigDecimal getProvvIdProvvedimentoSige() {
		return mProvvIdProvvedimentoSige;
	}

	public String getFlagAnnullamento() {
		return this.mFlagAnnullamento;
	}

	public Date getDataAnnullamento() {
		return this.mDataAnnullamento;
	}

	public String getMotivoAnnullamento() {
		return this.mMotivoAnnullamento;
	}

	public String getFlagSospEsec() {
		return this.mFlagSospEsec;
	} // 30/04/2007

	public BigDecimal getIdProvvedimentoGenerato() {
		return mIdProvvedimentoGenerato;
	}

	public String getConvRicorsoInCass() {
		return this.mConvRicorsoInCass;
	}

	public BigDecimal getIdOpposizioneConvRicorso() {
		return mIdOpposizioneConvRicorso;
	}
	
	//@emma 11072018 intervento post COLLAUDO 11.2 
	public String getFlagValidazioneEsito() {
		return this.mFlagValidazioneEsito;
	}
	

	//
	// METODI SET()
	//
	public void setIdImpugnazioneSige(BigDecimal aValore) {
		mIdImpugnazioneSige = aValore;
	}

	public void setAnnoS7(BigDecimal aValore) {
		mAnnoS7 = aValore;
	}

	public void setProgrS7(BigDecimal aValore) {
		mProgrS7 = aValore;
	}

	public void setCodTipoImpugnazione(String aValore) {
		mCodTipoImpugnazione = aValore;
	}

	public void setDescrTipoImpugnazione(String aValore) {
		mDescrTipoImpugnazione = aValore;
	}

	public void setSoggettoImpugnante(String aValore) {
		mSoggettoImpugnante = aValore;
	}

	public void setDescrSoggettoImpugnante(String aValore) {
		mDescrSoggettoImpugnante = aValore;
	}

	public void setDataRicorso(Date aValore) {
		mDataRicorso = aValore;
	}

	public void setDataAnnotazione(Date aValore) {
		mDataAnnotazione = aValore;
	}

	public void setAnnotazione(String aValore) {
		mAnnotazione = aValore;
	}

	public void setDataArrivoCancelleria(Date aValore) {
		mDataArrivoCancelleria = aValore;
	}

	public void setDataTrasmissioneAtti(Date aValore) {
		mDataTrasmissioneAtti = aValore;
	}

	public void setCodAutoritaDestinataria(String aValore) {
		mCodAutoritaDestinataria = aValore;
	}

	public void setDescrAutoritaDestinataria(String aValore) {
		mDescrAutoritaDestinataria = aValore;
	}

	public void setDataDecisione(Date aValore) {
		mDataDecisione = aValore;
	}

	public void setCodTenoreDecisione(String aValore) {
		mCodTenoreDecisione = aValore;
	}

	public void setDescrTenoreDecisione(String aValore) {
		mDescrTenoreDecisione = aValore;
	}

	public void setDataRestituzioneAtti(Date aValore) {
		mDataRestituzioneAtti = aValore;
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

	public void setProvvIdProvvedimentoSige(BigDecimal aValore) {
		mProvvIdProvvedimentoSige = aValore;
	}

	public void setFlagAnnullamento(String aValore) {
		mFlagAnnullamento = aValore;
	}

	public void setDataAnnullamento(Date aValore) {
		mDataAnnullamento = aValore;
	}

	public void setMotivoAnnullamento(String aValore) {
		this.mMotivoAnnullamento = aValore;
	}

	public void setFlagSospEsec(String aValore) {
		this.mFlagSospEsec = aValore;
	} // 30/04/2007

	public void setIdProvvedimentoGenerato(BigDecimal aValore) {
		this.mIdProvvedimentoGenerato = aValore;
	}

	public void setConvRicorsoInCass(String aValore) {
		this.mConvRicorsoInCass = aValore;
	}

	public void setIdOpposizioneConvRicorso(BigDecimal aValore) {
		this.mIdOpposizioneConvRicorso = aValore;
	}
	
	//@emma 11072018 intervento post COLLAUDO 11.2 
	public void setFlagValidazioneEsito(String aValore) {
		this.mFlagValidazioneEsito = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdImpugnazioneSige + " - " + mAnnoS7 + " - " + mProgrS7 + " - " + mCodTipoImpugnazione
				+ " - " + mDescrTipoImpugnazione + " - " + mSoggettoImpugnante + " - "
				+ mDescrSoggettoImpugnante + " - " + mDataRicorso + " - " + mDataAnnotazione + " - "
				+ mAnnotazione + " - " + mDataArrivoCancelleria + " - " + mDataTrasmissioneAtti + " - "
				+ mCodAutoritaDestinataria + " - " + mDescrAutoritaDestinataria + " - " + mDataDecisione
				+ " - " + mCodTenoreDecisione + " - " + mDescrTenoreDecisione + " - " + mDataRestituzioneAtti
				+ " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - "
				+ mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mProvvIdProvvedimentoSige + " - "
				+ mFlagAnnullamento + " - " + mDataAnnullamento + " - " + mMotivoAnnullamento + " - "
				+ mFlagSospEsec + " - " + mIdProvvedimentoGenerato + " - " + mFlagValidazioneEsito;

		return lStr;
	}

	public ProvvedimentoSigeEventoModel getProvvedimentoSige() {
		return provvedimentoSige;
	}

	public void setProvvedimentoSige(ProvvedimentoSigeEventoModel provvedimentoSige) {
		this.provvedimentoSige = provvedimentoSige;
	}

	public ProvvedimentoSigeEventoModel getProvvedimentoSigeGenerato() {
		return provvedimentoSigeGenerato;
	}

	public void setProvvedimentoSigeGenerato(ProvvedimentoSigeEventoModel provvedimentoSigeGenerato) {
		this.provvedimentoSigeGenerato = provvedimentoSigeGenerato;
	}

	public boolean isTrasmissibile() {
		boolean flag = false;
		if (this.provvedimentoSigeGenerato != null
				&& "S".equalsIgnoreCase(this.provvedimentoSigeGenerato.getEventoNotifica().getEvento()
						.getFlagDocumentoRegistrato()))
			flag = true;

		return flag;
	}

	public boolean isValidabile() {
		boolean flag = false;
		if (this.provvedimentoSigeGenerato != null
				&& !"S".equalsIgnoreCase(this.provvedimentoSigeGenerato.getEventoNotifica().getEvento()
						.getFlagDocumentoRegistrato()) &&
				// Modifica del 06/12/2016
				// Aggiungo ulteriore condizione: se l'opposizione è stata convertita in ricorso
				// (CONV_RICORSO_IN_CASS = 'S')
				// sull'opposizione non deve essere permessa alcuna azione, le azioni sono nuovamente attive
				// se
				// il ricorso viene cancellato
				(mConvRicorsoInCass == null || (mConvRicorsoInCass != null && mConvRicorsoInCass.equals("N"))))
			flag = true;

		return flag;
	}

	public boolean isModificabile() {
		boolean flag = true;
		// MAC 2017/04/21
		// Come risulta dal documento di analisi SIGI_PNL_AF_2017 03 30_1.4_MEV 15
		// e come richiesto anche da Nunzia, l'icona di modifica deve essere sempre presente.
		// if ((this.provvedimentoSigeGenerato != null &&
		// "S".equalsIgnoreCase(this.provvedimentoSigeGenerato.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()))
		// ||
		// Modifica del 06/12/2016
		// Aggiungo ulteriore condizione: se l'opposizione è stata convertita in ricorso (CONV_RICORSO_IN_CASS
		// = 'S')
		// sull'opposizione non deve essere permessa alcuna azione, le azioni sono nuovamente attive se
		// il ricorso viene cancellato
		if ((mConvRicorsoInCass != null && !mConvRicorsoInCass.equals("") && mConvRicorsoInCass.equals("S")))
			flag = false;

		return flag;
	}

	public boolean isCancellabile() {
		boolean flag = true;
		if ((this.provvedimentoSigeGenerato != null && "S".equalsIgnoreCase(this.provvedimentoSigeGenerato
				.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()))
				||
				// Modifica del 06/12/2016
				// Aggiungo ulteriore condizione: se l'opposizione è stata convertita in ricorso
				// (CONV_RICORSO_IN_CASS = 'S')
				// sull'opposizione non deve essere permessa alcuna azione, le azioni sono nuovamente attive
				// se
				// il ricorso viene cancellato
				(mConvRicorsoInCass != null && !mConvRicorsoInCass.equals("") && mConvRicorsoInCass
						.equals("S")))
			flag = false;

		return flag;
	}

	public boolean canSetResult() {
		boolean flag = false;
		// Modifica del 11/11/2016
		// L'icona per Aggiornare il Ricorso/Opposizione
		// ossia modificare l'esito deve essere sempre visibile
		// boolean flag=true;
		// if (this.provvedimentoSigeGenerato != null &&
		// "S".equalsIgnoreCase(this.provvedimentoSigeGenerato.getEventoNotifica().getEvento().getFlagDocumentoRegistrato())
		// &&
		// this.mCodTenoreDecisione == null){
		// flag=false;
		// }

		return flag;
	}

	public boolean isAnnullabile() {
		boolean flag = true;
		if ("S".equalsIgnoreCase(mFlagAnnullamento))
			flag = false;

		return flag;
	}

	// 20170703: eliminato metodo inutile: torna sempre true
	// public boolean isStampabile () {
	// boolean flag=true;
	// //if (this.getCodTenoreDecisione()== null || this.getCodTenoreDecisione().equals("-"))
	// // flag=false;
	//
	// return flag;
	// }

	public Vector<NotificaModel> getNotifiche() {
		return notifiche;
	}

	public void setNotifiche(Vector<NotificaModel> notifiche) {
		this.notifiche = notifiche;
	}

}