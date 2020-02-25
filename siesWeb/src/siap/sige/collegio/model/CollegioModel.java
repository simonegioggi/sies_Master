package siap.sige.collegio.model;

/**
* <p>Title: CollegioModel</p>
* <p>Description: Classe Model che rappresenta il Collegio</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/
import java.math.BigDecimal;
import java.util.Date;

import siap.sige.collegioesperto.model.CollegioEspertoModel;
import siap.sige.collegiogiudicepopolare.model.CollegioGiudicePopolareModel;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.sezione.model.SezioneModel;
import f3b.model.GenericModel;

public class CollegioModel extends GenericModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7283145088432887091L;

	private BigDecimal mIdCollegio;
	private String mCodCollegio;
	private BigDecimal mSezIdSezione;
	private String mCodUfficioAppartenenza;
	private String mDescrUfficioAppartenenza;
	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	private SezioneModel mSezione;
	private CollegioMagistratoModel[] mCollegioMagistrati = null;
	private CollegioGiudicePopolareModel[] mCollegioGiudiciPopolari = null;
	private CollegioEspertoModel[] mCollegioEsperti = null;

	// aggiungo proprieta per intervento 11.2.1
	private String mMagCodMagistrato;
	private String mDescrMagistratoPresidente;
	
	// 20190507 [SG]: aggiunta variabile
	private Date mDataUdienza;

	// COSTRUTTORE DI DEFAULT
	public CollegioModel() {
		this.mIdCollegio = null;
		this.mCodCollegio = null;
		this.mSezIdSezione = null;
		this.mCodUfficioAppartenenza = "";
		this.mDescrUfficioAppartenenza = "";
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mMagCodMagistrato = "";
		this.mDescrMagistratoPresidente="";
	}

	// COSTRUTTORE DI COPIA
	public CollegioModel(CollegioModel aModel) {
		this.mIdCollegio = aModel.mIdCollegio;
		this.mCodCollegio = aModel.mCodCollegio;
		this.mSezIdSezione = aModel.mSezIdSezione;
		this.mCodUfficioAppartenenza = aModel.mCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aModel.mDescrUfficioAppartenenza;
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;

		this.mSezione = aModel.mSezione;

		this.mCollegioMagistrati = aModel.mCollegioMagistrati;
		this.mCollegioGiudiciPopolari = aModel.mCollegioGiudiciPopolari;
		this.mCollegioEsperti = aModel.mCollegioEsperti;
		this.mMagCodMagistrato = aModel.mMagCodMagistrato;
		this.mDescrMagistratoPresidente = aModel.mDescrMagistratoPresidente;
	}

	// COSTRUTTORE MODEL
	public CollegioModel(BigDecimal aIdCollegio, String aCodCollegio, BigDecimal aSezIdSezione,
			String aCodUfficioAppartenenza, String aDescrUfficioAppartenenza, Date aDataInizioValidita,
			Date aDataFineValidita, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			String aMagCodMagistrato, String aDescrMagistratoPresidente) {
		this.mIdCollegio = aIdCollegio;
		this.mCodCollegio = aCodCollegio;
		this.mSezIdSezione = aSezIdSezione;
		this.mCodUfficioAppartenenza = aCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aDescrUfficioAppartenenza;
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mMagCodMagistrato = aMagCodMagistrato;
		this.mDescrMagistratoPresidente = aDescrMagistratoPresidente;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdCollegio() {
		return mIdCollegio;
	}

	public String getCodCollegio() {
		return mCodCollegio;
	}

	public BigDecimal getSezIdSezione() {
		return mSezIdSezione;
	}

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public String getCodUfficioAppartenenza() {
		return mCodUfficioAppartenenza;
	}

	public String getDescrUfficioAppartenenza() {
		return mDescrUfficioAppartenenza;
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

	public SezioneModel getSezione() {
		return mSezione;
	}

	public CollegioMagistratoModel[] getCollegioMagistrati() {
		return mCollegioMagistrati;
	}

	public CollegioGiudicePopolareModel[] getCollegioGiudiciPopolari() {
		return mCollegioGiudiciPopolari;
	}

	public CollegioEspertoModel[] getCollegioEsperti() {
		return mCollegioEsperti;
	}

	public String getMagCodMagistrato() {
		return mMagCodMagistrato;
	}

	public Date getDataUdienza() {
		return mDataUdienza;
	}	
	
	public String getDescrMagistratoPresidente() {
		return mDescrMagistratoPresidente;
	}

	//
	// METODI SET()
	//
	public void setIdCollegio(BigDecimal aValore) {
		mIdCollegio = aValore;
	}

	public void setCodCollegio(String aValore) {
		mCodCollegio = aValore;
	}

	public void setSezIdSezione(BigDecimal aValore) {
		mSezIdSezione = aValore;
	}

	public void setCodUfficioAppartenenza(String aValore) {
		mCodUfficioAppartenenza = aValore;
	}

	public void setDescrUfficioAppartenenza(String aValore) {
		mDescrUfficioAppartenenza = aValore;
	}

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
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

	public void setSezione(SezioneModel aValore) {
		mSezione = aValore;
	}

	public void setCollegioMagistrati(CollegioMagistratoModel[] aValori) {
		mCollegioMagistrati = aValori;
	}

	public void setCollegioGiudiciPopolari(CollegioGiudicePopolareModel[] aValori) {
		mCollegioGiudiciPopolari = aValori;
	}

	public void setCollegioEsperti(CollegioEspertoModel[] aValori) {
		mCollegioEsperti = aValori;
	}

	public void setMagCodMagistrato(String aValore) {
		mMagCodMagistrato = aValore;
	}

	public void setDataUdienza(Date mDataUdienza) {
		this.mDataUdienza = mDataUdienza;
	}
	
	public void setDescrMagistratoPresidente(String mDescrMagistratoPresidente) {
		this.mDescrMagistratoPresidente = mDescrMagistratoPresidente;
	}

}