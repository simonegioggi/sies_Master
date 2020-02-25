package siap.sius.prescrizione.model;

/**
* <p>Title: PrescrizioneModel</p>
* <p>Description: Classe Model che rappresenta il Prescrizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class PrescrizioneModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -2338451546812159006L;
	private BigDecimal mIdPrescrizione;
	private String mCodTipoPrescrizione;
	private String mDescrTipoPrescrizione;
	private String mCodLuogoAffidamento;
	private String mDescrLuogoAffidamento;
	private String mCodUffMagistratoCompetente;
	private String mDescrUffMagistratoCompetente;
	private String mCodLuogoAutorizzato;
	private String mDescrLuogoAutorizzato;
	private BigDecimal mIdCssaCompetente;
	private String mDescrComuneCssaCompetente;
	private String mDescrMansioneLavorativa;
	private String mDescrLuogoLavoro;
	private String mCodProvinciaAutorizzata;
	private String mDescrProvinciaAutorizzata;
	private String mOraUscitaAbitazione;
	private String mOraRientroAbitazione;
	private String mAutoritaCompetenteControllo;
	private BigDecimal mNumVolteControllo;
	private String mDescrAltraPrescrizione;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	// private BigDecimal mDepOpidDepositoOrdinanzaPc;
	private BigDecimal mEveIdEve;
	private String mDescrComunitaTerapeutica;
	private BigDecimal mProgrPrescrizione;
	private String mDescrPrescrizione1;
	private String mDescrPrescrizione2;
	private String mDescrPrescrizione3;

	// COSTRUTTORE DI DEFAULT
	public PrescrizioneModel() {
		this.mIdPrescrizione = null;
		this.mCodTipoPrescrizione = "";
		this.mDescrTipoPrescrizione = "";
		this.mCodLuogoAffidamento = "";
		this.mDescrLuogoAffidamento = "";
		this.mCodUffMagistratoCompetente = "";
		this.mDescrUffMagistratoCompetente = "";
		this.mCodLuogoAutorizzato = "";
		this.mDescrLuogoAutorizzato = "";
		this.mIdCssaCompetente = null;
		this.mDescrComuneCssaCompetente = "";
		this.mDescrMansioneLavorativa = "";
		this.mDescrLuogoLavoro = "";
		this.mCodProvinciaAutorizzata = "";
		this.mDescrProvinciaAutorizzata = "";
		this.mOraUscitaAbitazione = "";
		this.mOraRientroAbitazione = "";
		this.mAutoritaCompetenteControllo = "";
		this.mNumVolteControllo = null;
		this.mDescrAltraPrescrizione = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mEveIdEve = null;
		this.mDescrComunitaTerapeutica = "";
		this.mProgrPrescrizione = null;
		this.mDescrPrescrizione1 = "";
		this.mDescrPrescrizione2 = "";
		this.mDescrPrescrizione3 = "";
	}

	// COSTRUTTORE DI COPIA
	public PrescrizioneModel(PrescrizioneModel aModel) {
		this.mIdPrescrizione = aModel.mIdPrescrizione;
		this.mCodTipoPrescrizione = aModel.mCodTipoPrescrizione;
		this.mDescrTipoPrescrizione = aModel.mDescrTipoPrescrizione;
		this.mCodLuogoAffidamento = aModel.mCodLuogoAffidamento;
		this.mDescrLuogoAffidamento = aModel.mDescrLuogoAffidamento;
		this.mCodUffMagistratoCompetente = aModel.mCodUffMagistratoCompetente;
		this.mDescrUffMagistratoCompetente = aModel.mDescrUffMagistratoCompetente;
		this.mCodLuogoAutorizzato = aModel.mCodLuogoAutorizzato;
		this.mDescrLuogoAutorizzato = aModel.mDescrLuogoAutorizzato;
		this.mIdCssaCompetente = aModel.mIdCssaCompetente;
		this.mDescrComuneCssaCompetente = aModel.mDescrComuneCssaCompetente;
		this.mDescrMansioneLavorativa = aModel.mDescrMansioneLavorativa;
		this.mDescrLuogoLavoro = aModel.mDescrLuogoLavoro;
		this.mCodProvinciaAutorizzata = aModel.mCodProvinciaAutorizzata;
		this.mDescrProvinciaAutorizzata = aModel.mDescrProvinciaAutorizzata;
		this.mOraUscitaAbitazione = aModel.mOraUscitaAbitazione;
		this.mOraRientroAbitazione = aModel.mOraRientroAbitazione;
		this.mAutoritaCompetenteControllo = aModel.mAutoritaCompetenteControllo;
		this.mNumVolteControllo = aModel.mNumVolteControllo;
		this.mDescrAltraPrescrizione = aModel.mDescrAltraPrescrizione;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mEveIdEve = aModel.mEveIdEve;
		this.mDescrComunitaTerapeutica = aModel.mDescrComunitaTerapeutica;
		this.mProgrPrescrizione = aModel.mProgrPrescrizione;
		this.mDescrPrescrizione1 = aModel.mDescrPrescrizione1;
		this.mDescrPrescrizione2 = aModel.mDescrPrescrizione2;
		this.mDescrPrescrizione3 = aModel.mDescrPrescrizione3;
	}

	// COSTRUTTORE MODEL
	public PrescrizioneModel(BigDecimal aIdPrescrizione, String aCodTipoPrescrizione,
			String aDescrTipoPrescrizione, String aCodLuogoAffidamento, String aDescrLuogoAffidamento,
			String aCodUffMagistratoCompetente, String aDescrUffMagistratoCompetente,
			String aCodLuogoAutorizzato, String aDescrLuogoAutorizzato, BigDecimal aIdCssaCompetente,
			String aDescrComuneCssaCompetente, String aDescrMansioneLavorativa, String aDescrLuogoLavoro,
			String aCodProvinciaAutorizzata, String aDescrProvinciaAutorizzata, String aOraUscitaAbitazione,
			String aOraRientroAbitazione, String aAutoritaCompetenteControllo, BigDecimal aNumVolteControllo,
			String aDescrAltraPrescrizione, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aEveIdEve, String aDescrComunitaTerapeutica, BigDecimal aProgrPrescrizione,
			String aDescrPrescrizione1, String aDescrPrescrizione2, String aDescrPrescrizione3) {
		this.mIdPrescrizione = aIdPrescrizione;
		this.mCodTipoPrescrizione = aCodTipoPrescrizione;
		this.mDescrTipoPrescrizione = aDescrTipoPrescrizione;
		this.mCodLuogoAffidamento = aCodLuogoAffidamento;
		this.mDescrLuogoAffidamento = aDescrLuogoAffidamento;
		this.mCodUffMagistratoCompetente = aCodUffMagistratoCompetente;
		this.mDescrUffMagistratoCompetente = aDescrUffMagistratoCompetente;
		this.mCodLuogoAutorizzato = aCodLuogoAutorizzato;
		this.mDescrLuogoAutorizzato = aDescrLuogoAutorizzato;
		this.mIdCssaCompetente = aIdCssaCompetente;
		this.mDescrComuneCssaCompetente = aDescrComuneCssaCompetente;
		this.mDescrMansioneLavorativa = aDescrMansioneLavorativa;
		this.mDescrLuogoLavoro = aDescrLuogoLavoro;
		this.mCodProvinciaAutorizzata = aCodProvinciaAutorizzata;
		this.mDescrProvinciaAutorizzata = aDescrProvinciaAutorizzata;
		this.mOraUscitaAbitazione = aOraUscitaAbitazione;
		this.mOraRientroAbitazione = aOraRientroAbitazione;
		this.mAutoritaCompetenteControllo = aAutoritaCompetenteControllo;
		this.mNumVolteControllo = aNumVolteControllo;
		this.mDescrAltraPrescrizione = aDescrAltraPrescrizione;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mEveIdEve = aEveIdEve;
		this.mDescrComunitaTerapeutica = aDescrComunitaTerapeutica;
		this.mProgrPrescrizione = aProgrPrescrizione;
		this.mDescrPrescrizione1 = aDescrPrescrizione1;
		this.mDescrPrescrizione2 = aDescrPrescrizione2;
		this.mDescrPrescrizione3 = aDescrPrescrizione3;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdPrescrizione() {
		return mIdPrescrizione;
	}

	public String getCodTipoPrescrizione() {
		return mCodTipoPrescrizione;
	}

	public String getDescrTipoPrescrizione() {
		return mDescrTipoPrescrizione;
	}

	public String getCodLuogoAffidamento() {
		return mCodLuogoAffidamento;
	}

	public String getDescrLuogoAffidamento() {
		return mDescrLuogoAffidamento;
	}

	public String getCodUffMagistratoCompetente() {
		return mCodUffMagistratoCompetente;
	}

	public String getDescrUffMagistratoCompetente() {
		return mDescrUffMagistratoCompetente;
	}

	public String getCodLuogoAutorizzato() {
		return mCodLuogoAutorizzato;
	}

	public String getDescrLuogoAutorizzato() {
		return mDescrLuogoAutorizzato;
	}

	public BigDecimal getIdCssaCompetente() {
		return mIdCssaCompetente;
	}

	public String getDescrComuneCssaCompetente() {
		return mDescrComuneCssaCompetente;
	}

	public String getDescrMansioneLavorativa() {
		return mDescrMansioneLavorativa;
	}

	public String getDescrLuogoLavoro() {
		return mDescrLuogoLavoro;
	}

	public String getCodProvinciaAutorizzata() {
		return mCodProvinciaAutorizzata;
	}

	public String getDescrProvinciaAutorizzata() {
		return mDescrProvinciaAutorizzata;
	}

	public String getOraUscitaAbitazione() {
		return mOraUscitaAbitazione;
	}

	public String getOraRientroAbitazione() {
		return mOraRientroAbitazione;
	}

	public String getAutoritaCompetenteControllo() {
		return mAutoritaCompetenteControllo;
	}

	public BigDecimal getNumVolteControllo() {
		return mNumVolteControllo;
	}

	public String getDescrAltraPrescrizione() {
		return mDescrAltraPrescrizione;
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

	public BigDecimal getEveIdEve() {
		return mEveIdEve;
	}

	// public BigDecimal getDepOpidDepositoOrdinanzaPc() { return mEveIdEve; }
	public String getDescrComunitaTerapeutica() {
		return mDescrComunitaTerapeutica;
	}

	public BigDecimal getProgrPrescrizione() {
		return mProgrPrescrizione;
	}

	public String getDescrPrescrizione1() {
		return mDescrPrescrizione1;
	}

	public String getDescrPrescrizione2() {
		return mDescrPrescrizione2;
	}

	public String getDescrPrescrizione3() {
		return mDescrPrescrizione3;
	}

	//
	// METODI SET()
	//

	public void setIdPrescrizione(BigDecimal aValore) {
		mIdPrescrizione = aValore;
	}

	public void setCodTipoPrescrizione(String aValore) {
		mCodTipoPrescrizione = aValore;
	}

	public void setDescrTipoPrescrizione(String aValore) {
		mDescrTipoPrescrizione = aValore;
	}

	public void setCodLuogoAffidamento(String aValore) {
		mCodLuogoAffidamento = aValore;
	}

	public void setDescrLuogoAffidamento(String aValore) {
		mDescrLuogoAffidamento = aValore;
	}

	public void setCodUffMagistratoCompetente(String aValore) {
		mCodUffMagistratoCompetente = aValore;
	}

	public void setDescrUffMagistratoCompetente(String aValore) {
		mDescrUffMagistratoCompetente = aValore;
	}

	public void setCodLuogoAutorizzato(String aValore) {
		mCodLuogoAutorizzato = aValore;
	}

	public void setDescrLuogoAutorizzato(String aValore) {
		mDescrLuogoAutorizzato = aValore;
	}

	public void setIdCssaCompetente(BigDecimal aValore) {
		mIdCssaCompetente = aValore;
	}

	public void setDescrComuneCssaCompetente(String aValore) {
		mDescrComuneCssaCompetente = aValore;
	}

	public void setDescrMansioneLavorativa(String aValore) {
		mDescrMansioneLavorativa = aValore;
	}

	public void setDescrLuogoLavoro(String aValore) {
		mDescrLuogoLavoro = aValore;
	}

	public void setCodProvinciaAutorizzata(String aValore) {
		mCodProvinciaAutorizzata = aValore;
	}

	public void setDescrProvinciaAutorizzata(String aValore) {
		mDescrProvinciaAutorizzata = aValore;
	}

	public void setOraUscitaAbitazione(String aValore) {
		mOraUscitaAbitazione = aValore;
	}

	public void setOraRientroAbitazione(String aValore) {
		mOraRientroAbitazione = aValore;
	}

	public void setAutoritaCompetenteControllo(String aValore) {
		mAutoritaCompetenteControllo = aValore;
	}

	public void setNumVolteControllo(BigDecimal aValore) {
		mNumVolteControllo = aValore;
	}

	public void setDescrAltraPrescrizione(String aValore) {
		mDescrAltraPrescrizione = aValore;
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

	public void setEveIdEve(BigDecimal aValore) {
		mEveIdEve = aValore;
	}

	public void setDescrComunitaTerapeutica(String aValore) {
		mDescrComunitaTerapeutica = aValore;
	}

	public void setProgrPrescrizione(BigDecimal aValore) {
		mProgrPrescrizione = aValore;
	}

	public void setDescrPrescrizione1(String aValore) {
		mDescrPrescrizione1 = aValore;
	}

	public void setDescrPrescrizione2(String aValore) {
		mDescrPrescrizione2 = aValore;
	}

	public void setDescrPrescrizione3(String aValore) {
		mDescrPrescrizione3 = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdPrescrizione + " - " + mCodTipoPrescrizione + " - " + mDescrTipoPrescrizione + " - "
				+ mCodLuogoAffidamento + " - " + mDescrLuogoAffidamento + " - " + mCodUffMagistratoCompetente
				+ " - " + mDescrUffMagistratoCompetente + " - " + mCodLuogoAutorizzato + " - "
				+ mDescrLuogoAutorizzato + " - " + mIdCssaCompetente + " - " + mDescrComuneCssaCompetente
				+ " - " + mDescrMansioneLavorativa + " - " + mDescrLuogoLavoro + " - "
				+ mCodProvinciaAutorizzata + " - " + mDescrProvinciaAutorizzata + " - " + mOraUscitaAbitazione
				+ " - " + mOraRientroAbitazione + " - " + mAutoritaCompetenteControllo + " - "
				+ mNumVolteControllo + " - " + mDescrAltraPrescrizione + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - " + mEveIdEve + " - "
				+ mDescrComunitaTerapeutica + " - " + mProgrPrescrizione + " - " + mDescrPrescrizione1 + " - "
				+ mDescrPrescrizione2 + " - " + mDescrPrescrizione3;
		return lStr;
	}
}
