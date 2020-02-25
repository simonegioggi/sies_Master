package siap.sius.permesso.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class EventoPermessoLicenzaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 403741287699624343L;

	private BigDecimal mIdEventoPermessoLicenza;
	private String mCodTipoEvento;
	private String mDescrTipoEvento;
	private Date mDataSegnalazione;
	private String mDescrEvento;
	private String mMittenteSegnalazione;
	private String mCodTipoConseguenza;
	private String mDescrTipoConseguenza;
	private String mDescrConseguenze;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private Date mDataInserimento;
	private String mCodOperatoreAggiornamento;
	private String mCodUfficioAggiornamento;
	private Date mDataAggiornamento;
	private BigDecimal mLicIdLicenzaLibAnticipata;

	// COSTRUTTORE DI DEFAULT
	public EventoPermessoLicenzaModel() {
		mIdEventoPermessoLicenza = null;
		mCodTipoEvento = "";
		mDescrTipoEvento = "";
		mDataSegnalazione = null;
		mDescrEvento = "";
		mMittenteSegnalazione = "";
		mCodTipoConseguenza = "";
		mDescrTipoConseguenza = "";
		mDescrConseguenze = "";
		mCodOperatoreInserimento = "";
		mCodUfficioInserimento = "";
		mDataInserimento = null;
		mCodOperatoreAggiornamento = "";
		mCodUfficioAggiornamento = "";
		mDataAggiornamento = null;
		mLicIdLicenzaLibAnticipata = null;
	}

	// COSTRUTTORE DI COPIA
	public EventoPermessoLicenzaModel(EventoPermessoLicenzaModel aModel) {
		mIdEventoPermessoLicenza = aModel.mIdEventoPermessoLicenza;
		mCodTipoEvento = aModel.mCodTipoEvento;
		mDescrTipoEvento = aModel.mDescrTipoEvento;
		mDataSegnalazione = aModel.mDataSegnalazione;
		mDescrEvento = aModel.mDescrEvento;
		mMittenteSegnalazione = aModel.mMittenteSegnalazione;
		mCodTipoConseguenza = aModel.mCodTipoConseguenza;
		mDescrTipoConseguenza = aModel.mDescrTipoConseguenza;
		mDescrConseguenze = aModel.mDescrConseguenze;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mLicIdLicenzaLibAnticipata = aModel.mLicIdLicenzaLibAnticipata;
	}

	// COSTRUTTORE MODEL
	public EventoPermessoLicenzaModel(BigDecimal aIdEventoPermessoLicenza, String aCodTipoEvento,
			String aDescrTipoEvento, Date aDataSegnalazione, String aDescrEvento,
			String aMittenteSegnalazione, String aCodTipoConseguenza, String aDescrTipoConseguenza,
			String aDescrConseguenze, String aCodOperatoreInserimento, String aCodUfficioInserimento,
			Date aDataInserimento, String aCodOperatoreAggiornamento, String aCodUfficioAggiornamento,
			Date aDataAggiornamento, BigDecimal aLicIdLicenzaLibAnticipata) {
		mIdEventoPermessoLicenza = aIdEventoPermessoLicenza;
		mCodTipoEvento = aCodTipoEvento;
		mDescrTipoEvento = aDescrTipoEvento;
		mDataSegnalazione = aDataSegnalazione;
		mDescrEvento = aDescrEvento;
		mMittenteSegnalazione = aMittenteSegnalazione;
		mCodTipoConseguenza = aCodTipoConseguenza;
		mDescrTipoConseguenza = aDescrTipoConseguenza;
		mDescrConseguenze = aDescrConseguenze;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDataInserimento = aDataInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mLicIdLicenzaLibAnticipata = aLicIdLicenzaLibAnticipata;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdEventoPermessoLicenza() {
		return mIdEventoPermessoLicenza;
	}

	public String getCodTipoEvento() {
		return mCodTipoEvento;
	}

	public String getDescrTipoEvento() {
		return mDescrTipoEvento;
	}

	public Date getDataSegnalazione() {
		return mDataSegnalazione;
	}

	public String getDescrEvento() {
		return mDescrEvento;
	}

	public String getMittenteSegnalazione() {
		return mMittenteSegnalazione;
	}

	public String getDescrConseguenze() {
		return mDescrConseguenze;
	}

	public String getCodTipoConseguenza() {
		return mCodTipoConseguenza;
	}

	public String getDescrTipoConseguenza() {
		return mDescrTipoConseguenza;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public BigDecimal getLicIdLicenzaLibAnticipata() {
		return mLicIdLicenzaLibAnticipata;
	}

	//
	// METODI SET()
	//
	public void setIdEventoPermessoLicenza(BigDecimal aValore) {
		mIdEventoPermessoLicenza = aValore;
	}

	public void setCodTipoEvento(String aValore) {
		mCodTipoEvento = aValore;
	}

	public void setDescrTipoEvento(String aValore) {
		mDescrTipoEvento = aValore;
	}

	public void setDataSegnalazione(Date aValore) {
		mDataSegnalazione = aValore;
	}

	public void setDescrEvento(String aValore) {
		mDescrEvento = aValore;
	}

	public void setMittenteSegnalazione(String aValore) {
		mMittenteSegnalazione = aValore;
	}

	public void setCodTipoConseguenza(String aValore) {
		mCodTipoConseguenza = aValore;
	}

	public void setDescrTipoConseguenza(String aValore) {
		mDescrTipoConseguenza = aValore;
	}

	public void setDescrConseguenze(String aValore) {
		mDescrConseguenze = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setLicIdLicenzaLibAnticipata(BigDecimal aValore) {
		mLicIdLicenzaLibAnticipata = aValore;
	}

}