package siap.sige.tenore.model;

/**
* <p>Title: TenoreSentenzaReatoModel</p>
* <p>Description: Classe Model che rappresenta il TenoreSentenzaReato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import f3b.model.GenericModel;
import f3b.util.F3BException;

public class TenoreSentenzaReatoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8812989577112348166L;

	private BigDecimal mTenIdTenoreSige;
	private BigDecimal mSenIdSentenza;
	private BigDecimal mReaIdReato;
	private Date mDataInserimento;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private BigDecimal mIdTenSenRea;
	private String mCodEsito;
	private String mDescrEsito;
	private Date mDataAggiornamento;
	private String mCodOperatoreAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	// COSTRUTTORE DI DEFAULT
	public TenoreSentenzaReatoModel() {
		mIdTenSenRea = null;
		mCodEsito = null;
		mDescrEsito = "";
		mDataAggiornamento = null;
		mCodOperatoreAggiornamento = "";
		mCodUfficioAggiornamento = "";
		mDescrUfficioAggiornamento = "";

		mTenIdTenoreSige = null;
		mSenIdSentenza = null;
		mReaIdReato = null;
		mDataInserimento = null;
		mCodOperatoreInserimento = "";
		mCodUfficioInserimento = "";
		mDescrUfficioInserimento = "";
	}

	// COSTRUTTORE DI COPIA
	public TenoreSentenzaReatoModel(TenoreSentenzaReatoModel aModel) {
		mTenIdTenoreSige = aModel.mTenIdTenoreSige;
		mSenIdSentenza = aModel.mSenIdSentenza;
		mReaIdReato = aModel.mReaIdReato;
		mDataInserimento = aModel.mDataInserimento;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		mIdTenSenRea = aModel.mIdTenSenRea;
		mCodEsito = aModel.mCodEsito;
		mDescrEsito = aModel.mDescrEsito;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
	}

	// COSTRUTTORE MODEL
	public TenoreSentenzaReatoModel(BigDecimal aTenIdTenoreSige, BigDecimal aSenIdSentenza,
			BigDecimal aReaIdReato, Date aDataInserimento, String aCodOperatoreInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, BigDecimal aIdTenSenRea,
			String aCodEsito, String aDescrEsito, Date aDataAggiornamento, String aCodOperatoreAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento) {
		mTenIdTenoreSige = aTenIdTenoreSige;
		mSenIdSentenza = aSenIdSentenza;
		mReaIdReato = aReaIdReato;
		mDataInserimento = aDataInserimento;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;

		mIdTenSenRea = aIdTenSenRea;
		mCodEsito = aCodEsito;
		mDescrEsito = aDescrEsito;
		mDataAggiornamento = aDataAggiornamento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
	}

	//
	// METODI GET()
	//

	public BigDecimal getTenIdTenoreSige() {
		return mTenIdTenoreSige;
	}

	public BigDecimal getSenIdSentenza() {
		return mSenIdSentenza;
	}

	public BigDecimal getReaIdReato() {
		return mReaIdReato;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public BigDecimal getIdTenSenRea() {
		return mIdTenSenRea;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	//
	// METODI SET()
	//

	public void setTenIdTenoreSige(BigDecimal aValore) {
		mTenIdTenoreSige = aValore;
	}

	public void setSenIdSentenza(BigDecimal aValore) {
		mSenIdSentenza = aValore;
	}

	public void setReaIdReato(BigDecimal aValore) {
		mReaIdReato = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setIdTenSenRea(BigDecimal aValore) {
		mIdTenSenRea = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public TenoreSentenzaReatoModel decodifica() throws F3BException {
		try {

			// Decodifica Esito Oggetto
			if (mCodEsito != null && mCodEsito.trim().length() > 0)
				setDescrEsito(DecodificheUtils
						.getDescbyCode(DecodificheManager.getInstance().getEsitoTenoreSige(), mCodEsito));
		} catch (Exception e) {
			throw new F3BException(F3BException.EX_OPERATION_FAILED, "Errore nella trascodifica codice ( "
					+ getClass().getName() + ".decodifica()) -> " + e.getMessage());
		}
		return this;
	}

}