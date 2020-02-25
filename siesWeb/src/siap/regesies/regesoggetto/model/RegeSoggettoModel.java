package siap.regesies.regesoggetto.model;

/**
* <p>Title: RegeSoggettoModel</p>
* <p>Description: Classe Model che rappresenta il RegeSoggetto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import siap.sico.soggetto.model.SoggettoModel;
import f3b.model.GenericModel;

public class RegeSoggettoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2081120901794693040L;

	private String mIdFile;
	private String mFlagTipoSogg;
	private String mCodFiscale;
	private String mCodCs;
	private String mCodAfis;
	private String mCognome;
	private String mNome;
	private int mAnnoNascita;
	private Date mDataNascita;
	private String mCodComuneNascita;
	private String mDescrComuneNascita;
	private String mCodProvinciaNascita;
	private String mDescrProvinciaNascita;
	private String mCodStatoNascita;
	private String mDescrStatoNascita;
	private String mDescComuneNascitaEstero;
	private String mNazionalita;
	private String mPaternita;
	private String mCognomeMadre;
	private String mNomeMadre;
	private String mSesso;
	private String mAttoNascita;
	private String mNote;
	private String mDenoSogg;
	private String mRagiSogg;
	private String mNomeRappLega;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	private String mCodComuneCasellario;

	// COSTRUTTORE DI DEFAULT
	public RegeSoggettoModel() {
		this.mIdFile = "";
		this.mFlagTipoSogg = "";
		this.mCodFiscale = "";
		this.mCodCs = "";
		this.mCodAfis = "";
		this.mCognome = "";
		this.mNome = "";
		this.mAnnoNascita = 0;
		this.mDataNascita = null;
		this.mCodComuneNascita = "";
		this.mDescrComuneNascita = "";
		this.mCodProvinciaNascita = "";
		this.mDescrProvinciaNascita = "";
		this.mCodStatoNascita = "";
		this.mDescrStatoNascita = "";
		this.mDescComuneNascitaEstero = "";
		this.mNazionalita = "";
		this.mPaternita = "";
		this.mCognomeMadre = "";
		this.mNomeMadre = "";
		this.mSesso = "";
		this.mAttoNascita = "";
		this.mNote = "";
		this.mDenoSogg = "";
		this.mRagiSogg = "";
		this.mNomeRappLega = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mCodComuneCasellario = "";
	}

	// COSTRUTTORE DI COPIA
	public RegeSoggettoModel(RegeSoggettoModel aModel) {
		this.mIdFile = aModel.mIdFile;
		this.mFlagTipoSogg = aModel.mFlagTipoSogg;
		this.mCodFiscale = aModel.mCodFiscale;
		this.mCodCs = aModel.mCodCs;
		this.mCodAfis = aModel.mCodAfis;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mAnnoNascita = aModel.mAnnoNascita;
		this.mDataNascita = aModel.mDataNascita;
		this.mCodComuneNascita = aModel.mCodComuneNascita;
		this.mDescrComuneNascita = aModel.mDescrComuneNascita;
		this.mCodProvinciaNascita = aModel.mCodProvinciaNascita;
		this.mDescrProvinciaNascita = aModel.mDescrProvinciaNascita;
		this.mCodStatoNascita = aModel.mCodStatoNascita;
		this.mDescrStatoNascita = aModel.mDescrStatoNascita;
		this.mDescComuneNascitaEstero = aModel.mDescComuneNascitaEstero;
		this.mNazionalita = aModel.mNazionalita;
		this.mPaternita = aModel.mPaternita;
		this.mCognomeMadre = aModel.mCognomeMadre;
		this.mNomeMadre = aModel.mNomeMadre;
		this.mSesso = aModel.mSesso;
		this.mAttoNascita = aModel.mAttoNascita;
		this.mNote = aModel.mNote;
		this.mDenoSogg = aModel.mDenoSogg;
		this.mRagiSogg = aModel.mRagiSogg;
		this.mNomeRappLega = aModel.mNomeRappLega;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
	}

	// COSTRUTTORE DI COPIA da Soggetto Model
	public RegeSoggettoModel(SoggettoModel aModel) {
		this.mIdFile = aModel.getIdSoggetto().toString();
		// this.mFlagTipoSogg = aModel.getFlagTipoSogg();
		this.mCodFiscale = aModel.getCodFiscale();
		this.mCodCs = aModel.getCodCs();
		this.mCodAfis = aModel.getCodAfis();
		this.mCognome = aModel.getCognome();
		this.mNome = aModel.getNome();
		if (aModel.getAnnoNascita() != null)
			this.mAnnoNascita = aModel.getAnnoNascita().intValue();

		this.mDataNascita = aModel.getDataNascita();
		this.mCodComuneNascita = aModel.getCodComuneNascita();
		this.mDescrComuneNascita = aModel.getDescrComuneNascita();
		this.mCodProvinciaNascita = aModel.getCodProvinciaNascita();
		this.mDescrProvinciaNascita = aModel.getDescrProvinciaNascita();
		this.mCodStatoNascita = aModel.getCodStatoNascita();
		this.mDescrStatoNascita = aModel.getDescrStatoNascita();
		this.mDescComuneNascitaEstero = aModel.getDescComuneNascitaEstero();
		this.mNazionalita = aModel.getNazionalita();
		this.mPaternita = aModel.getPaternita();
		this.mCognomeMadre = aModel.getCognomeMadre();
		this.mNomeMadre = aModel.getNomeMadre();
		this.mSesso = aModel.getSesso();
		this.mAttoNascita = aModel.getAttoNascita();
		this.mNote = aModel.getNote();
		// this.mDenoSogg = aModel.getDenoSogg();
		// this.mRagiSogg = aModel.getRagiSogg();
		// this.mNomeRappLega = aModel.getNomeRappLega();
		this.mCodOperatoreInserimento = aModel.getCodOperatoreInserimento();
		this.mDataInserimento = aModel.getDataInserimento();
		this.mCodUfficioInserimento = aModel.getCodUfficioInserimento();
		this.mDescrUfficioInserimento = aModel.getDescrUfficioInserimento();
		this.mCodOperatoreAggiornamento = aModel.getCodOperatoreAggiornamento();
		this.mDataAggiornamento = aModel.getDataAggiornamento();
		this.mCodUfficioAggiornamento = aModel.getCodUfficioAggiornamento();
		this.mDescrUfficioAggiornamento = aModel.getDescrUfficioAggiornamento();
	}

	// COSTRUTTORE MODEL
	public RegeSoggettoModel(String aIdFile, String aFlagTipoSogg, String aCodFiscale, String aDescrFiscale,
			String aCodCs, String aDescrCs, String aCodAfis, String aDescrAfis, String aCognome, String aNome,
			int aAnnoNascita, Date aDataNascita, String aCodComuneNascita, String aDescrComuneNascita,
			String aCodProvinciaNascita, String aDescrProvinciaNascita, String aCodStatoNascita,
			String aDescrStatoNascita, String aDescComuneNascitaEstero, String aNazionalita,
			String aPaternita, String aCognomeMadre, String aNomeMadre, String aSesso, String aAttoNascita,
			String aNote, String aDenoSogg, String aRagiSogg, String aNomeRappLega,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento) {
		this.mIdFile = aIdFile;
		this.mFlagTipoSogg = aFlagTipoSogg;
		this.mCodFiscale = aCodFiscale;
		this.mCodCs = aCodCs;
		this.mCodAfis = aCodAfis;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mAnnoNascita = aAnnoNascita;
		this.mDataNascita = aDataNascita;
		this.mCodComuneNascita = aCodComuneNascita;
		this.mDescrComuneNascita = aDescrComuneNascita;
		this.mCodProvinciaNascita = aCodProvinciaNascita;
		this.mDescrProvinciaNascita = aDescrProvinciaNascita;
		this.mCodStatoNascita = aCodStatoNascita;
		this.mDescrStatoNascita = aDescrStatoNascita;
		this.mDescComuneNascitaEstero = aDescComuneNascitaEstero;
		this.mNazionalita = aNazionalita;
		this.mPaternita = aPaternita;
		this.mCognomeMadre = aCognomeMadre;
		this.mNomeMadre = aNomeMadre;
		this.mSesso = aSesso;
		this.mAttoNascita = aAttoNascita;
		this.mNote = aNote;
		this.mDenoSogg = aDenoSogg;
		this.mRagiSogg = aRagiSogg;
		this.mNomeRappLega = aNomeRappLega;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;

		// this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
	}

	//
	// METODI GET()
	//
	public String getIdFile() {
		return mIdFile;
	}

	public String getFlagTipoSogg() {
		return mFlagTipoSogg;
	}

	public String getCodFiscale() {
		return mCodFiscale;
	}

	public String getCodCs() {
		return mCodCs;
	}

	public String getCodAfis() {
		return mCodAfis;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public int getAnnoNascita() {
		return mAnnoNascita;
	}

	public Date getDataNascita() {
		return mDataNascita;
	}

	public String getCodComuneNascita() {
		return mCodComuneNascita;
	}

	public String getDescrComuneNascita() {
		return mDescrComuneNascita;
	}

	public String getCodProvinciaNascita() {
		return mCodProvinciaNascita;
	}

	public String getDescrProvinciaNascita() {
		return mDescrProvinciaNascita;
	}

	public String getCodStatoNascita() {
		return mCodStatoNascita;
	}

	public String getDescrStatoNascita() {
		return mDescrStatoNascita;
	}

	public String getDescComuneNascitaEstero() {
		return mDescComuneNascitaEstero;
	}

	public String getNazionalita() {
		return mNazionalita;
	}

	public String getPaternita() {
		return mPaternita;
	}

	public String getCognomeMadre() {
		return mCognomeMadre;
	}

	public String getNomeMadre() {
		return mNomeMadre;
	}

	public String getSesso() {
		return mSesso;
	}

	public String getAttoNascita() {
		return mAttoNascita;
	}

	public String getNote() {
		return mNote;
	}

	public String getDenoSogg() {
		return mDenoSogg;
	}

	public String getRagiSogg() {
		return mRagiSogg;
	}

	public String getNomeRappLega() {
		return mNomeRappLega;
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

	public String getCodComuneCasellario() {
		return mCodComuneCasellario;
	}

	//
	// METODI SET()
	//

	public void setIdFile(String aValore) {
		mIdFile = aValore;
	}

	public void setFlagTipoSogg(String aValore) {
		mFlagTipoSogg = aValore;
	}

	public void setCodFiscale(String aValore) {
		mCodFiscale = aValore;
	}

	public void setCodCs(String aValore) {
		mCodCs = aValore;
	}

	public void setCodAfis(String aValore) {
		mCodAfis = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setAnnoNascita(int aValore) {
		mAnnoNascita = aValore;
	}

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	}

	public void setCodComuneNascita(String aValore) {
		mCodComuneNascita = aValore;
	}

	public void setDescrComuneNascita(String aValore) {
		mDescrComuneNascita = aValore;
	}

	public void setCodProvinciaNascita(String aValore) {
		mCodProvinciaNascita = aValore;
	}

	public void setDescrProvinciaNascita(String aValore) {
		mDescrProvinciaNascita = aValore;
	}

	public void setCodStatoNascita(String aValore) {
		mCodStatoNascita = aValore;
	}

	public void setDescrStatoNascita(String aValore) {
		mDescrStatoNascita = aValore;
	}

	public void setDescComuneNascitaEstero(String aValore) {
		mDescComuneNascitaEstero = aValore;
	}

	public void setNazionalita(String aValore) {
		mNazionalita = aValore;
	}

	public void setPaternita(String aValore) {
		mPaternita = aValore;
	}

	public void setCognomeMadre(String aValore) {
		mCognomeMadre = aValore;
	}

	public void setNomeMadre(String aValore) {
		mNomeMadre = aValore;
	}

	public void setSesso(String aValore) {
		mSesso = aValore;
	}

	public void setAttoNascita(String aValore) {
		mAttoNascita = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setDenoSogg(String aValore) {
		mDenoSogg = aValore;
	}

	public void setRagiSogg(String aValore) {
		mRagiSogg = aValore;
	}

	public void setNomeRappLega(String aValore) {
		mNomeRappLega = aValore;
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

	public void setCodComuneCasellario(String aValore) {
		mCodComuneCasellario = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdFile + " - " + mFlagTipoSogg + " - " + mCodFiscale + " - " + mCodCs + " - " + mCodAfis
				+ " - " + mCognome + " - " + mNome + " - " + mAnnoNascita + " - " + mDataNascita + " - "
				+ mCodComuneNascita + " - " + mDescrComuneNascita + " - " + mCodProvinciaNascita + " - "
				+ mDescrProvinciaNascita + " - " + mCodStatoNascita + " - " + mDescrStatoNascita + " - "
				+ mDescComuneNascitaEstero + " - " + mNazionalita + " - " + mPaternita + " - " + mCognomeMadre
				+ " - " + mNomeMadre + " - " + mSesso + " - " + mAttoNascita + " - " + mNote + " - "
				+ mDenoSogg + " - " + mRagiSogg + " - " + mNomeRappLega + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento;

		return lStr;
	}

	/**
	 * Restituisce un SoggettoModel
	 * 
	 * @return
	 */
	public SoggettoModel toSoggettoModel() {
		SoggettoModel lSogg = new SoggettoModel();
		// lSogg.setIdFile(this.getIdSoggetto().toString());
		// lSogg.setFlagTipoSogg(this.getFlagTipoSogg());
		lSogg.setCodFiscale(this.getCodFiscale());
		lSogg.setCodCs(this.getCodCs());
		lSogg.setCodAfis(this.getCodAfis());
		lSogg.setCognome(this.getCognome());
		lSogg.setNome(this.getNome());
		lSogg.setDataNascitaPresunta("N");
		// lSogg.setAnnoNascita(new BigDecimal(this.getAnnoNascita()));
		lSogg.setDataNascita(this.getDataNascita());
		lSogg.setCodComuneNascita(this.getCodComuneNascita());
		lSogg.setDescrComuneNascita(this.getDescrComuneNascita());
		lSogg.setCodProvinciaNascita(this.getCodProvinciaNascita());
		lSogg.setDescrProvinciaNascita(this.getDescrProvinciaNascita());
		lSogg.setCodStatoNascita(this.getCodStatoNascita());
		lSogg.setDescrStatoNascita(this.getDescrStatoNascita());
		lSogg.setDescComuneNascitaEstero(this.getDescComuneNascitaEstero());
		lSogg.setNazionalita(this.getNazionalita());
		lSogg.setPaternita(this.getPaternita());
		lSogg.setCognomeMadre(this.getCognomeMadre());
		lSogg.setNomeMadre(this.getNomeMadre());
		lSogg.setSesso(this.getSesso());
		lSogg.setAttoNascita(this.getAttoNascita());
		lSogg.setNote(this.getNote());
		lSogg.setCodOperatoreInserimento(this.getCodOperatoreInserimento());
		lSogg.setDataInserimento(this.getDataInserimento());
		lSogg.setCodUfficioInserimento(this.getCodUfficioInserimento());
		lSogg.setDescrUfficioInserimento(this.getDescrUfficioInserimento());
		lSogg.setCodOperatoreAggiornamento(this.getCodOperatoreAggiornamento());
		lSogg.setDataAggiornamento(this.getDataAggiornamento());
		lSogg.setCodUfficioAggiornamento(this.getCodUfficioAggiornamento());
		lSogg.setDescrUfficioAggiornamento(this.getDescrUfficioAggiornamento());
		lSogg.setCodComuneCasellario(this.getCodComuneCasellario());

		return lSogg;
	}

}