package siap.sige.provvedimento.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.F3BException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sige.udienza.model.UdienzaSigeModel;

/**
 * <p>
 * Title: ProvvedimentoSigeModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il ProvvedimentoSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
public class ProvvedimentoSigeModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 6671286475678477411L;
	private BigDecimal mIdProvvedimentoSige;
	private BigDecimal mFasIdFascicoloSige;
	private BigDecimal mIdEventoGenerato;
	private BigDecimal mUdiIdUdienzaSige;
	private BigDecimal mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String mChiaveUfficio;
	private Date mDataEmissione;
	private Date mDataDeposito;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private Date mDataInserimento;
	private String mCodOperatoreAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private Date mDataAggiornamento;
	private String mDefinitorio;
	private String mFlagOrdineTraduzione;
	private String mLuogoSvolgimento;
	private BigDecimal mColIdCollegio;
	private String mCodTipoProvvedimentoSige;
	private String mDescrTipoProvvedimentoSige;
	private String mCodUfficioDestinatario; // 08/01/2010
	private String mCodUffCompCorteSuprema;
	private String mDescrUfficioDestinatario; // 08/01/2010
	private String mDescrUffCompCorteSuprema;
	private String mNote; // 08/01/2010
	private BigDecimal mProvvIdProvvedimentoSige;

	private UdienzaSigeModel udienzaSige;

	// COSTRUTTORE DI DEFAULT
	public ProvvedimentoSigeModel() {
		this.mIdProvvedimentoSige = null;
		this.mFasIdFascicoloSige = null;
		this.mIdEventoGenerato = null;
		this.mUdiIdUdienzaSige = null;
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mChiaveUfficio = "";
		this.mDataEmissione = null;
		this.mDataDeposito = null;
		this.mCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mCodOperatoreInserimento = "";
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mDataInserimento = null;
		this.mCodOperatoreAggiornamento = "";
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mDefinitorio = "";
		this.mFlagOrdineTraduzione = "";
		this.mLuogoSvolgimento = "";
		mColIdCollegio = null;
		this.mCodTipoProvvedimentoSige = "";
		this.mDescrTipoProvvedimentoSige = "";
		this.mCodUfficioDestinatario = ""; // 08/01/2010
		this.mCodUffCompCorteSuprema = "";
		this.mDescrUfficioDestinatario = ""; // 08/01/2010
		this.mDescrUffCompCorteSuprema = "";
		this.mNote = ""; // 08/01/2010
		this.mProvvIdProvvedimentoSige = null;
	}

	// COSTRUTTORE DI COPIA
	public ProvvedimentoSigeModel(ProvvedimentoSigeModel aModel) {
		this.mIdProvvedimentoSige = aModel.mIdProvvedimentoSige;
		this.mFasIdFascicoloSige = aModel.mFasIdFascicoloSige;
		this.mIdEventoGenerato = aModel.mIdEventoGenerato;
		this.mUdiIdUdienzaSige = aModel.mUdiIdUdienzaSige;
		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveProgr = aModel.mChiaveProgr;
		this.mChiaveUfficio = aModel.mChiaveUfficio;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mDataDeposito = aModel.mDataDeposito;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mDefinitorio = aModel.mDefinitorio;
		this.mFlagOrdineTraduzione = aModel.mFlagOrdineTraduzione;
		this.mLuogoSvolgimento = aModel.mLuogoSvolgimento;
		this.mColIdCollegio = aModel.mColIdCollegio;
		this.mCodTipoProvvedimentoSige = aModel.mCodTipoProvvedimentoSige;
		this.mDescrTipoProvvedimentoSige = aModel.mDescrTipoProvvedimentoSige;
		this.mCodUfficioDestinatario = aModel.mCodUfficioDestinatario; // 08/01/2010
		this.mCodUffCompCorteSuprema = aModel.mCodUffCompCorteSuprema;
		this.mDescrUfficioDestinatario = aModel.mDescrUfficioDestinatario; // 08/01/2010
		this.mDescrUffCompCorteSuprema = aModel.mDescrUffCompCorteSuprema;
		this.mNote = aModel.mNote; // 08/01/2010
		this.mProvvIdProvvedimentoSige = aModel.mProvvIdProvvedimentoSige;
	}

	// COSTRUTTORE MODEL
	public ProvvedimentoSigeModel(BigDecimal aIdProvvedimentoSige, BigDecimal aFasIdFascicoloSige,
			BigDecimal aIdEventoGenerato, BigDecimal aUdiIdUdienzaSige, BigDecimal aChiaveAnno,
			BigDecimal aChiaveProgr, String aChiaveUfficio, Date aDataEmissione, Date aDataDeposito,
			String aCodTipoProvvedimento, String aDescrTipoProvvedimento, String aCodOperatoreInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, Date aDataInserimento,
			String aCodOperatoreAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, Date aDataAggiornamento, String aDefinitorio,
			String aFlagOrdineTraduzione, String aLuogoSvolgimento, BigDecimal aColIdCollegio,
			String aCodTipoProvvedimentoSige, String aDescrTipoProvvedimentoSige,
			String aCodUfficioDestinatario, String aCodUffCompCorteSuprema, String aDescrUfficioDestinatario,
			String aDescrUffCompCorteSuprema, String aNote, BigDecimal aProvvIdProvvedimentoSige) {
		this.mIdProvvedimentoSige = aIdProvvedimentoSige;
		this.mFasIdFascicoloSige = aFasIdFascicoloSige;
		this.mIdEventoGenerato = aIdEventoGenerato;
		this.mUdiIdUdienzaSige = aUdiIdUdienzaSige;
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;
		this.mChiaveUfficio = aChiaveUfficio;
		this.mDataEmissione = aDataEmissione;
		this.mDataDeposito = aDataDeposito;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mDefinitorio = aDefinitorio;
		this.mFlagOrdineTraduzione = aFlagOrdineTraduzione;
		this.mLuogoSvolgimento = aLuogoSvolgimento;
		this.mColIdCollegio = aColIdCollegio;
		this.mCodTipoProvvedimentoSige = aCodTipoProvvedimentoSige;
		this.mDescrTipoProvvedimentoSige = aDescrTipoProvvedimentoSige;
		this.mCodUfficioDestinatario = aCodUfficioDestinatario; // 08/01/2010
		this.mCodUffCompCorteSuprema = aCodUffCompCorteSuprema;
		this.mDescrUfficioDestinatario = aDescrUfficioDestinatario; // 08/01/2010
		this.mDescrUffCompCorteSuprema = aDescrUffCompCorteSuprema;
		this.mNote = aNote; // 08/01/2010
		this.mProvvIdProvvedimentoSige = aProvvIdProvvedimentoSige;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdProvvedimentoSige() {
		return mIdProvvedimentoSige;
	}

	public BigDecimal getFasIdFascicoloSige() {
		return mFasIdFascicoloSige;
	}

	public BigDecimal getIdEventoGenerato() {
		return mIdEventoGenerato;
	}

	public BigDecimal getUdiIdUdienzaSige() {
		return mUdiIdUdienzaSige;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public String getChiaveUfficio() {
		return mChiaveUfficio;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public Date getDataDeposito() {
		return mDataDeposito;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
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

	public Date getDataInserimento() {
		return mDataInserimento;
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

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getDefinitorio() {
		return mDefinitorio;
	}

	public String getFlagOrdineTraduzione() {
		return mFlagOrdineTraduzione;
	}

	public String getLuogoSvolgimento() {
		return mLuogoSvolgimento;
	}

	public BigDecimal getColIdCollegio() {
		return mColIdCollegio;
	}

	public String getCodTipoProvvedimentoSige() {
		return mCodTipoProvvedimentoSige;
	}

	public String getDescrTipoProvvedimentoSige() {
		return mDescrTipoProvvedimentoSige;
	}

	public String getCodUfficioDestinatario() {
		return mCodUfficioDestinatario;
	} // 08/01/2010

	public String getCodUffCompCorteSuprema() {
		return mCodUffCompCorteSuprema;
	}

	public String getDescrUfficioDestinatario() {
		return mDescrUfficioDestinatario;
	} // 08/01/2010

	public String getDescrUffCompCorteSuprema() {
		return mDescrUffCompCorteSuprema;
	}

	public String getNote() {
		return mNote;
	} // 08/01/2010

	public BigDecimal getProvvIdProvvedimentoSige() {
		return mProvvIdProvvedimentoSige;
	}

	//
	// METODI SET()
	//

	public void setIdProvvedimentoSige(BigDecimal aValore) {
		mIdProvvedimentoSige = aValore;
	}

	public void setFasIdFascicoloSige(BigDecimal aValore) {
		mFasIdFascicoloSige = aValore;
	}

	public void setIdEventoGenerato(BigDecimal aValore) {
		mIdEventoGenerato = aValore;
	}

	public void setUdiIdUdienzaSige(BigDecimal aValore) {
		mUdiIdUdienzaSige = aValore;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setChiaveUfficio(String aValore) {
		mChiaveUfficio = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setDataDeposito(Date aValore) {
		mDataDeposito = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
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

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
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

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setDefinitorio(String aValore) {
		mDefinitorio = aValore;
	}

	public void setFlagOrdineTraduzione(String aValore) {
		mFlagOrdineTraduzione = aValore;
	}

	public void setLuogoSvolgimento(String aValore) {
		mLuogoSvolgimento = aValore;
	}

	public void setColIdCollegio(BigDecimal aValore) {
		mColIdCollegio = aValore;
	}

	public void setCodTipoProvvedimentoSige(String aValore) {
		mCodTipoProvvedimentoSige = aValore;
	}

	public void setDescrTipoProvvedimentoSige(String aValore) {
		mDescrTipoProvvedimentoSige = aValore;
	}

	public void setCodUfficioDestinatario(String aValore) {
		mCodUfficioDestinatario = aValore;
	} // 08/01/2010

	public void setCodUffCompCorteSuprema(String aValore) {
		mCodUffCompCorteSuprema = aValore;
	}

	public void setDescrUfficioDestinatario(String aValore) {
		mDescrUfficioDestinatario = aValore;
	}// 08/01/2010

	public void setDescrUffCompCorteSuprema(String aValore) {
		mDescrUffCompCorteSuprema = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	} // 08/01/2010

	public void setProvvIdProvvedimentoSige(BigDecimal aValore) {
		mProvvIdProvvedimentoSige = aValore;
	}

	public ProvvedimentoSigeModel decodifica() throws F3BException {
		try {
			// Decodifica Tipo Provvedimento
			if (mCodTipoProvvedimento != null && mCodTipoProvvedimento.trim().length() > 0)
				setDescrTipoProvvedimento(DecodificheUtils.getDescbyCode(
						DecodificheManager.getInstance().getTipoProvvedimenti(), mCodTipoProvvedimento));
			if (mCodTipoProvvedimentoSige != null && mCodTipoProvvedimentoSige.trim().length() > 0)
				setDescrTipoProvvedimentoSige(DecodificheUtils.getDescbyCode(
						DecodificheManager.getInstance().getTipoProvvedimentoSige(),
						mCodTipoProvvedimentoSige));
		} catch (Exception e) {
			throw new F3BException(F3BException.EX_OPERATION_FAILED, "Errore nella trascodifica codice ( "
					+ getClass().getName() + ".decodifica()) -> " + e.getMessage());
		}
		return this;
	}

	public UdienzaSigeModel getUdienzaSige() {
		return udienzaSige;
	}

	public void setUdienzaSige(UdienzaSigeModel udienzaSige) {
		this.udienzaSige = udienzaSige;
	}

}