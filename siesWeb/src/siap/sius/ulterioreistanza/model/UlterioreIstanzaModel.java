package siap.sius.ulterioreistanza.model;

/**
* <p>Title: UlterioreIstanzaModel</p>
* <p>Description: Classe Model che rappresenta il UlterioreIstanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import siap.sius.ulterioreistanzatenore.model.UlterioreIstanzaTenoreModel;
import f3b.model.GenericModel;

@SuppressWarnings("rawtypes")
public class UlterioreIstanzaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3763715237706990688L;

	private BigDecimal mIdUlterioreIstanza;
	private String mCodOggettoProcedimento;
	private String mDescrOggettoProcedimento;
	private Date mDataRichiesta;
	private Date mDataArrivoCancelleria;
	private String mCodTipoAtto;
	private String mDescrTipoAtto;
	private String mCodTipoMittenteAtto;
	private String mDescrTipoMittenteAtto;
	private String mSedeMittente;
	private String mDescrMittente;
	private String mNote;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSiuIdFascicoloSius;

	private UlterioreIstanzaTenoreModel[] mUltIstTenori;

	// COSTRUTTORE DI DEFAULT
	public UlterioreIstanzaModel() {
		this.mIdUlterioreIstanza = null;
		this.mCodOggettoProcedimento = "";
		this.mDescrOggettoProcedimento = "";
		this.mDataRichiesta = null;
		this.mDataArrivoCancelleria = null;
		this.mCodTipoAtto = "";
		this.mDescrTipoAtto = "";
		this.mCodTipoMittenteAtto = "";
		this.mDescrTipoMittenteAtto = "";
		this.mSedeMittente = "";
		this.mDescrMittente = "";
		this.mNote = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSiuIdFascicoloSius = null;

		this.mUltIstTenori = new UlterioreIstanzaTenoreModel[0];

	}

	// COSTRUTTORE DI COPIA
	public UlterioreIstanzaModel(UlterioreIstanzaModel aModel) {
		this.mIdUlterioreIstanza = aModel.mIdUlterioreIstanza;
		this.mCodOggettoProcedimento = aModel.mCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aModel.mDescrOggettoProcedimento;
		this.mDataRichiesta = aModel.mDataRichiesta;
		this.mDataArrivoCancelleria = aModel.mDataArrivoCancelleria;
		this.mCodTipoAtto = aModel.mCodTipoAtto;
		this.mDescrTipoAtto = aModel.mDescrTipoAtto;
		this.mCodTipoMittenteAtto = aModel.mCodTipoMittenteAtto;
		this.mDescrTipoMittenteAtto = aModel.mDescrTipoMittenteAtto;
		this.mSedeMittente = aModel.mSedeMittente;
		this.mDescrMittente = aModel.mDescrMittente;
		this.mNote = aModel.mNote;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;

		this.mUltIstTenori = aModel.mUltIstTenori;
	}

	// COSTRUTTORE MODEL
	public UlterioreIstanzaModel(BigDecimal aIdUlterioreIstanza, String aCodOggettoProcedimento,
			String aDescrOggettoProcedimento, Date aDataRichiesta, Date aDataArrivoCancelleria,
			String aCodTipoAtto, String aDescrTipoAtto, String aCodTipoMittenteAtto,
			String aDescrTipoMittenteAtto, String aSedeMittente, String aDescrMittente, String aNote,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSiuIdFascicoloSius) {
		this.mIdUlterioreIstanza = aIdUlterioreIstanza;
		this.mCodOggettoProcedimento = aCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aDescrOggettoProcedimento;
		this.mDataRichiesta = aDataRichiesta;
		this.mDataArrivoCancelleria = aDataArrivoCancelleria;
		this.mCodTipoAtto = aCodTipoAtto;
		this.mDescrTipoAtto = aDescrTipoAtto;
		this.mCodTipoMittenteAtto = aCodTipoMittenteAtto;
		this.mDescrTipoMittenteAtto = aDescrTipoMittenteAtto;
		this.mSedeMittente = aSedeMittente;
		this.mDescrMittente = aDescrMittente;
		this.mNote = aNote;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdUlterioreIstanza() {
		return mIdUlterioreIstanza;
	}

	public String getCodOggettoProcedimento() {
		return mCodOggettoProcedimento;
	}

	public String getDescrOggettoProcedimento() {
		return mDescrOggettoProcedimento;
	}

	public Date getDataRichiesta() {
		return mDataRichiesta;
	}

	public Date getDataArrivoCancelleria() {
		return mDataArrivoCancelleria;
	}

	public String getCodTipoAtto() {
		return mCodTipoAtto;
	}

	public String getDescrTipoAtto() {
		return mDescrTipoAtto;
	}

	public String getCodTipoMittenteAtto() {
		return mCodTipoMittenteAtto;
	}

	public String getDescrTipoMittenteAtto() {
		return mDescrTipoMittenteAtto;
	}

	public String getSedeMittente() {
		return mSedeMittente;
	}

	public String getDescrMittente() {
		return mDescrMittente;
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

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public UlterioreIstanzaTenoreModel[] getUltIstTenori() {
		return mUltIstTenori;
	}

	/**
	 * Lista dei tenori, presentati non come array, ma come List
	 * <p>
	 * 
	 * @return List insieme dei tenori.
	 */
	public List getListUltIstTenori() {
		List lList = Arrays.asList(mUltIstTenori);
		return lList;
	}

	//
	// METODI SET()
	//
	public void setIdUlterioreIstanza(BigDecimal aValore) {
		mIdUlterioreIstanza = aValore;
	}

	public void setCodOggettoProcedimento(String aValore) {
		mCodOggettoProcedimento = aValore;
	}

	public void setDescrOggettoProcedimento(String aValore) {
		mDescrOggettoProcedimento = aValore;
	}

	public void setDataRichiesta(Date aValore) {
		mDataRichiesta = aValore;
	}

	public void setDataArrivoCancelleria(Date aValore) {
		mDataArrivoCancelleria = aValore;
	}

	public void setCodTipoAtto(String aValore) {
		mCodTipoAtto = aValore;
	}

	public void setDescrTipoAtto(String aValore) {
		mDescrTipoAtto = aValore;
	}

	public void setCodTipoMittenteAtto(String aValore) {
		mCodTipoMittenteAtto = aValore;
	}

	public void setDescrTipoMittenteAtto(String aValore) {
		mDescrTipoMittenteAtto = aValore;
	}

	public void setSedeMittente(String aValore) {
		mSedeMittente = aValore;
	}

	public void setDescrMittente(String aValore) {
		mDescrMittente = aValore;
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setUltIstTenori(UlterioreIstanzaTenoreModel[] aValori) {
		mUltIstTenori = aValori;
	}

}