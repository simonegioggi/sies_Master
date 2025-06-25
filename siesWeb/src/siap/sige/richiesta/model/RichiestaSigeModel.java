package siap.sige.richiesta.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.F3BException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.ufficio.model.UfficioModel;

/**
 * RichiestaSigeModel - Classe Model che rappresenta il RichiestaSige
 *
 * @version 5.0
 */
public class RichiestaSigeModel extends GenericModel {

	/**
	 * Adds a generated serial version ID to the selected type. Use this option to add a compiler-generated ID
	 * if the type did not undergo structural changes since its first release.
	 */
	private static final long serialVersionUID = -1255255107390559219L;

	private BigDecimal mIdRichiestaSige;
	private String mCodTipoAtto;
	private String mDescrTipoAtto;
	private String mCodTipoRichiedente;
	private String mDescrTipoRichiedente;
	private String mCodSedeRichiedente;
	private String mDescrSedeRichiedente;
	private String mDescRichiedente;
	private String mCodUfficioRichiedente;
	private String mDescrUfficioRichiedente;
	private Date mDataEmissione;
	private Date mDataDeposito;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private Date mDataArrivoCancelleria;

	// COSTRUTTORE DI DEFAULT
	public RichiestaSigeModel() {
		mIdRichiestaSige = null;
		mCodTipoAtto = "";
		mDescrTipoAtto = "";
		mCodTipoRichiedente = "";
		mDescrTipoRichiedente = "";
		mCodSedeRichiedente = "";
		mDescrSedeRichiedente = "";
		mDescRichiedente = "";
		mCodUfficioRichiedente = "";
		mDescrUfficioRichiedente = "";
		mDataEmissione = null;
		mDataDeposito = null;
		mCodOperatoreInserimento = "";
		mDataInserimento = null;
		mCodUfficioInserimento = "";
		mDescrUfficioInserimento = "";
		mCodOperatoreAggiornamento = "";
		mDataAggiornamento = null;
		mCodUfficioAggiornamento = "";
		mDescrUfficioAggiornamento = "";
		mFasSieIdFascicoloSiep = null;
		mDataArrivoCancelleria = null;
	}

	// COSTRUTTORE DI COPIA
	public RichiestaSigeModel(RichiestaSigeModel aModel) {
		mIdRichiestaSige = aModel.mIdRichiestaSige;
		mCodTipoAtto = aModel.mCodTipoAtto;
		mDescrTipoAtto = aModel.mDescrTipoAtto;
		mCodTipoRichiedente = aModel.mCodTipoRichiedente;
		mDescrTipoRichiedente = aModel.mDescrTipoRichiedente;
		mCodSedeRichiedente = aModel.mCodSedeRichiedente;
		mDescrSedeRichiedente = aModel.mDescrSedeRichiedente;
		mDescRichiedente = aModel.mDescRichiedente;
		mCodUfficioRichiedente = aModel.mCodUfficioRichiedente;
		mDescrUfficioRichiedente = aModel.mDescrUfficioRichiedente;
		mDataEmissione = aModel.mDataEmissione;
		mDataDeposito = aModel.mDataDeposito;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		mDataArrivoCancelleria = aModel.mDataArrivoCancelleria;
	}

	// COSTRUTTORE MODEL
	public RichiestaSigeModel(BigDecimal aIdRichiestaSige, String aCodTipoAtto, String aDescrTipoAtto,
			String aCodTipoRichiedente, String aDescrTipoRichiedente, String aCodSedeRichiedente,
			String aDescrSedeRichiedente, String aDescRichiedente, String aCodUfficioRichiedente,
			String aDescrUfficioRichiedente, Date aDataEmissione, Date aDataDeposito,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, Date aDataArrivoCancelleria) {
		mIdRichiestaSige = aIdRichiestaSige;
		mCodTipoAtto = aCodTipoAtto;
		mDescrTipoAtto = aDescrTipoAtto;
		mCodTipoRichiedente = aCodTipoRichiedente;
		mDescrTipoRichiedente = aDescrTipoRichiedente;
		mCodSedeRichiedente = aCodSedeRichiedente;
		mDescrSedeRichiedente = aDescrSedeRichiedente;
		mDescRichiedente = aDescRichiedente;
		mCodUfficioRichiedente = aCodUfficioRichiedente;
		mDescrUfficioRichiedente = aDescrUfficioRichiedente;
		mDataEmissione = aDataEmissione;
		mDataDeposito = aDataDeposito;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mDataInserimento = aDataInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		mDataArrivoCancelleria = aDataArrivoCancelleria;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdRichiestaSige() {
		return mIdRichiestaSige;
	}

	public String getCodTipoAtto() {
		return mCodTipoAtto;
	}

	public String getDescrTipoAtto() {
		return mDescrTipoAtto;
	}

	public String getCodTipoRichiedente() {
		return mCodTipoRichiedente;
	}

	public String getDescrTipoRichiedente() {
		return mDescrTipoRichiedente;
	}

	public String getCodSedeRichiedente() {
		return mCodSedeRichiedente;
	}

	public String getDescrSedeRichiedente() {
		return mDescrSedeRichiedente;
	}

	public String getDescRichiedente() {
		return mDescRichiedente;
	}

	public String getCodUfficioRichiedente() {
		return mCodUfficioRichiedente;
	}

	public String getDescrUfficioRichiedente() {
		return mDescrUfficioRichiedente;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public Date getDataDeposito() {
		return mDataDeposito;
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

	public Date getDataArrivoCancelleria() {
		return mDataArrivoCancelleria;
	}

	//
	// METODI SET()
	//

	public void setIdRichiestaSige(BigDecimal aValore) {
		mIdRichiestaSige = aValore;
	}

	public void setCodTipoAtto(String aValore) {
		mCodTipoAtto = aValore;
	}

	public void setDescrTipoRichiesta(String aValore) {
		mDescrTipoAtto = aValore;
	}

	public void setCodTipoRichiedente(String aValore) {
		mCodTipoRichiedente = aValore;
	}

	public void setDescrTipoRichiedente(String aValore) {
		mDescrTipoRichiedente = aValore;
	}

	public void setCodSedeRichiedente(String aValore) {
		mCodSedeRichiedente = aValore;
	}

	public void setDescrSedeRichiedente(String aValore) {
		mDescrSedeRichiedente = aValore;
	}

	public void setDescRichiedente(String aValore) {
		mDescRichiedente = aValore;
	}

	public void setCodUfficioRichiedente(String aValore) {
		mCodUfficioRichiedente = aValore;
	}

	public void setDescrUfficioRichiedente(String aValore) {
		mDescrUfficioRichiedente = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setDataDeposito(Date aValore) {
		mDataDeposito = aValore;
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

	public void setDataArrivoCancelleria(Date aValore) {
		mDataArrivoCancelleria = aValore;
	}

	/**
	 * Il metodo effettua la decodifica di quegli attributi del Model che contengono dei campi codificati.
	 *
	 * La decodifica del campo interessato viene memorizzata nell'attributo di descrizione ad esso relativo.
	 * Gli attributi codificati ed i relativi attributi di decodifica interessati a questa operazione sono:
	 * mCodTipoAtto -> mDescrTipoAtto, mCodTipoRichiedente -> mDescrTipoRichiedente, mCodUfficioRichiedente ->
	 * DescrUfficioRichiedente, mCodSedeRichiedente -> DescrSedeRichiedente.
	 *
	 * @throws F3BException
	 */
	public RichiestaSigeModel decodifica() throws F3BException {
		try {
			// Decodifica Tipo Richiesta
			if (mCodTipoAtto != null && mCodTipoAtto.trim().length() > 0)
				setDescrTipoRichiesta(DecodificheUtils
						.getDescbyCode(DecodificheManager.getInstance().getTipoAttoSige(), mCodTipoAtto));

			// Decodifica Tipo Richiedente
			if (mCodTipoRichiedente != null && mCodTipoRichiedente.trim().length() > 0)
				setDescrTipoRichiedente(DecodificheUtils.getDescbyCode(
						DecodificheManager.getInstance().getTipoRichiedenteSige(), mCodTipoRichiedente));

			// Decodifica Ufficio Richiedente
			if (mCodUfficioRichiedente != null && mCodUfficioRichiedente.trim().length() > 0) {
				UfficioModel lUff = UfficioUtils.getUfficioByCodUfficio(mCodUfficioRichiedente);
				setDescrUfficioRichiedente(lUff.getDescrTipoUfficio() + " " + lUff.getDescrComune());
			}
			// Decodifica Sede (Comune) Richiedente
			if (mCodSedeRichiedente != null && mCodSedeRichiedente.trim().length() > 0) {
				setDescrSedeRichiedente(
						DecodificheUtils.getComuneByCod(mCodSedeRichiedente).getDescrizione());
			}
		} catch (Exception e) {
			throw new F3BException(F3BException.EX_OPERATION_FAILED, "Errore nella trascodifica codice ( "
					+ getClass().getName() + ".decodifica()) -> " + e.getMessage());
		}
		return this;
	}

}