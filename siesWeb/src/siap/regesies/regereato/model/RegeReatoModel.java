package siap.regesies.regereato.model;

import java.util.Date;

import siap.regesies.model.RegeModel;
import siap.siep.reato.model.ReatoModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: RegeReatoModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il Rege Reato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
public class RegeReatoModel extends RegeModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 3427000563229790569L;

	private String mIdFile;
	private String mCodTipoReato;
	private String mDescrTipoReato;
	private Date mDataReato;
	private String mProgrNumeroManuale;
	private int mProgrReato;
	private int mProgrCircostanza;
	private Date mDataInizio;
	private int mAnnoInizio;
	private int mMeseInizio;
	private int mGiornoInizio;
	private Date mDataFine;
	private int mAnnoFine;
	private int mMeseFine;
	private int mGiornoFine;
	private String mCodPeriodoConsumazione;
	private String mDescrPeriodoConsumazione;
	private String mDescLuogo;
	private String mCodFonte;
	private String mDescrFonte;
	private int mAnnoFonte;
	private String mNumeroFonte;
	private String mCodSottonumerazione;
	private String mDescrSottonumerazione;
	private String mComma;
	private String mLettera;
	private String mNumero;
	private String mArticolo;
	private String mNote;
	private String mNotaQgf;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mDataReatoCompleta;
	private String mStringaConsumazione;

	// COSTRUTTORE DI DEFAULT
	public RegeReatoModel() {
		this.mIdFile = "";
		this.mCodTipoReato = "-";
		this.mDescrTipoReato = "";
		this.mDataReato = null;
		this.mProgrNumeroManuale = "";
		this.mProgrReato = 0;
		this.mProgrCircostanza = 0;
		this.mDataInizio = null;
		this.mAnnoInizio = 0;
		this.mMeseInizio = 0;
		this.mGiornoInizio = 0;
		this.mDataFine = null;
		this.mAnnoFine = 0;
		this.mMeseFine = 0;
		this.mGiornoFine = 0;
		this.mCodPeriodoConsumazione = "-";
		this.mDescrPeriodoConsumazione = "";
		this.mDescLuogo = "";
		this.mCodFonte = "-";
		this.mDescrFonte = "";
		this.mAnnoFonte = 0;
		this.mNumeroFonte = "";
		this.mCodSottonumerazione = "-";
		this.mDescrSottonumerazione = "";
		this.mComma = "";
		this.mLettera = "";
		this.mNumero = "";
		this.mArticolo = "";
		this.mNote = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mDataReatoCompleta = "";
		this.mStringaConsumazione = "";
		this.mNotaQgf = "";
	}

	// COSTRUTTORE DI COPIA
	public RegeReatoModel(RegeReatoModel aModel) {
		this.mIdFile = aModel.mIdFile;
		this.mCodTipoReato = aModel.mCodTipoReato;
		this.mDescrTipoReato = aModel.mDescrTipoReato;
		this.mDataReato = aModel.mDataReato;
		this.mProgrNumeroManuale = aModel.mProgrNumeroManuale;
		this.mProgrReato = aModel.mProgrReato;
		this.mProgrCircostanza = aModel.mProgrCircostanza;
		this.mDataInizio = aModel.mDataInizio;
		this.mAnnoInizio = aModel.mAnnoInizio;
		this.mMeseInizio = aModel.mMeseInizio;
		this.mGiornoInizio = aModel.mGiornoInizio;
		this.mDataFine = aModel.mDataFine;
		this.mAnnoFine = aModel.mAnnoFine;
		this.mMeseFine = aModel.mMeseFine;
		this.mGiornoFine = aModel.mGiornoFine;
		this.mCodPeriodoConsumazione = aModel.mCodPeriodoConsumazione;
		this.mDescrPeriodoConsumazione = aModel.mDescrPeriodoConsumazione;
		this.mDescLuogo = aModel.mDescLuogo;
		this.mCodFonte = aModel.mCodFonte;
		this.mDescrFonte = aModel.mDescrFonte;
		this.mAnnoFonte = aModel.mAnnoFonte;
		this.mNumeroFonte = aModel.mNumeroFonte;
		this.mCodSottonumerazione = aModel.mCodSottonumerazione;
		this.mDescrSottonumerazione = aModel.mDescrSottonumerazione;
		this.mComma = aModel.mComma;
		this.mLettera = aModel.mLettera;
		this.mNumero = aModel.mNumero;
		this.mArticolo = aModel.mArticolo;
		this.mNote = aModel.mNote;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mNotaQgf = aModel.mNotaQgf;
	}

	// COSTRUTTORE MODEL
	public RegeReatoModel(String aIdFile, String aCodTipoReato, String aDescrTipoReato, Date aDataReato,
			String aProgrNumeroManuale, int aProgrReato, int aProgrCircostanza, Date aDataInizio,
			int aAnnoInizio, int aMeseInizio, int aGiornoInizio, Date aDataFine, int aAnnoFine, int aMeseFine,
			int aGiornoFine, String aCodPeriodoConsumazione, String aDescrPeriodoConsumazione,
			String aDescLuogo, String aCodFonte, String aDescrFonte, int aAnnoFonte, String aNumeroFonte,
			String aCodSottonumerazione, String aDescrSottonumerazione, String aComma, String aLettera,
			String aNumero, String aArticolo, String aNote, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aNotaQgf) {
		this.mIdFile = aIdFile;
		this.mCodTipoReato = aCodTipoReato;
		this.mDescrTipoReato = aDescrTipoReato;
		this.mDataReato = aDataReato;
		this.mProgrNumeroManuale = aProgrNumeroManuale;
		this.mProgrReato = aProgrReato;
		this.mProgrCircostanza = aProgrCircostanza;
		this.mDataInizio = aDataInizio;
		this.mAnnoInizio = aAnnoInizio;
		this.mMeseInizio = aMeseInizio;
		this.mGiornoInizio = aGiornoInizio;
		this.mDataFine = aDataFine;
		this.mAnnoFine = aAnnoFine;
		this.mMeseFine = aMeseFine;
		this.mGiornoFine = aGiornoFine;
		this.mCodPeriodoConsumazione = aCodPeriodoConsumazione;
		this.mDescrPeriodoConsumazione = aDescrPeriodoConsumazione;
		this.mDescLuogo = aDescLuogo;
		this.mCodFonte = aCodFonte;
		this.mDescrFonte = aDescrFonte;
		this.mAnnoFonte = aAnnoFonte;
		this.mNumeroFonte = aNumeroFonte;
		this.mCodSottonumerazione = aCodSottonumerazione;
		this.mDescrSottonumerazione = aDescrSottonumerazione;
		this.mComma = aComma;
		this.mLettera = aLettera;
		this.mNumero = aNumero;
		this.mArticolo = aArticolo;
		this.mNote = aNote;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mNotaQgf = aNotaQgf;
		// this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
	}

	/**
	 * Trasformazione in ReatoModel
	 * 
	 * @return
	 */
	public ReatoModel toReato() {
		ReatoModel lReato = new ReatoModel();

		// lReato.setIdFile(this.mIdFile);
		lReato.setCodTipoReato(this.mCodTipoReato);
		lReato.setDescrTipoReato(this.mDescrTipoReato);
		lReato.setDataReato(this.mDataReato);
		lReato.setProgrNumeroManuale(this.mProgrNumeroManuale);
		lReato.setProgrReato(toBigDecimal(this.mProgrReato));
		lReato.setProgrCircostanza(toBigDecimal(this.mProgrCircostanza));
		lReato.setDataInizio(this.mDataInizio);
		lReato.setAnnoInizio(toBigDecimal(this.mAnnoInizio));
		lReato.setMeseInizio(toBigDecimal(this.mMeseInizio));
		lReato.setGiornoInizio(toBigDecimal(this.mGiornoInizio));
		lReato.setDataFine(this.mDataFine);
		lReato.setAnnoFine(toBigDecimal(this.mAnnoFine));
		lReato.setMeseFine(toBigDecimal(this.mMeseFine));
		lReato.setGiornoFine(toBigDecimal(this.mGiornoFine));
		lReato.setCodPeriodoConsumazione(this.mCodPeriodoConsumazione);
		lReato.setDescrPeriodoConsumazione(this.mDescrPeriodoConsumazione);
		lReato.setDescLuogo(this.mDescLuogo);
		lReato.setCodFonte(this.mCodFonte);
		lReato.setDescrFonte(this.mDescrFonte);
		lReato.setAnnoFonte(toBigDecimal(this.mAnnoFonte));
		lReato.setNumeroFonte(this.mNumeroFonte);
		lReato.setCodSottonumerazione(this.mCodSottonumerazione);
		lReato.setDescrSottonumerazione(this.mDescrSottonumerazione);
		lReato.setComma(this.mComma);
		lReato.setLettera(this.mLettera);
		lReato.setNumero(this.mNumero);
		lReato.setArticolo(this.mArticolo);
		lReato.setNote(this.mNote);
		lReato.setCodOperatoreInserimento(this.mCodOperatoreInserimento);
		lReato.setDataInserimento(this.mDataInserimento);
		lReato.setCodUfficioInserimento(this.mCodUfficioInserimento);
		lReato.setDescrUfficioInserimento(this.mDescrUfficioInserimento);
		lReato.setCodOperatoreAggiornamento(this.mCodOperatoreAggiornamento);
		lReato.setDataAggiornamento(this.mDataAggiornamento);
		lReato.setCodUfficioAggiornamento(this.mCodUfficioAggiornamento);
		lReato.setDescrUfficioAggiornamento(this.mDescrUfficioAggiornamento);

		lReato.setCodTipoSanzione("-");
		lReato.setCodTipoPenaDetentiva("-");
		// aggiungere nota qgf

		return lReato;
	}

	//
	// METODI GET()
	//
	public String getIdFile() {
		return mIdFile;
	}

	public String getCodTipoReato() {
		return mCodTipoReato;
	}

	public String getDescrTipoReato() {
		return mDescrTipoReato;
	}

	public Date getDataReato() {
		return mDataReato;
	}

	public String getProgrNumeroManuale() {
		return mProgrNumeroManuale;
	}

	public int getProgrReato() {
		return mProgrReato;
	}

	public int getProgrCircostanza() {
		return mProgrCircostanza;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public int getAnnoInizio() {
		return mAnnoInizio;
	}

	public int getMeseInizio() {
		return mMeseInizio;
	}

	public int getGiornoInizio() {
		return mGiornoInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public int getAnnoFine() {
		return mAnnoFine;
	}

	public int getMeseFine() {
		return mMeseFine;
	}

	public int getGiornoFine() {
		return mGiornoFine;
	}

	public String getCodPeriodoConsumazione() {
		return mCodPeriodoConsumazione;
	}

	public String getDescrPeriodoConsumazione() {
		return mDescrPeriodoConsumazione;
	}

	public String getDescLuogo() {
		return mDescLuogo;
	}

	public String getCodFonte() {
		return mCodFonte;
	}

	public String getDescrFonte() {
		return mDescrFonte;
	}

	public int getAnnoFonte() {
		return mAnnoFonte;
	}

	public String getNumeroFonte() {
		return mNumeroFonte;
	}

	public String getCodSottonumerazione() {
		return mCodSottonumerazione;
	}

	public String getDescrSottonumerazione() {
		return mDescrSottonumerazione;
	}

	public String getComma() {
		return mComma;
	}

	public String getLettera() {
		return mLettera;
	}

	public String getNumero() {
		return mNumero;
	}

	public String getArticolo() {
		return mArticolo;
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

	public String getDataReatoCompleta() {
		return mDataReatoCompleta;
	}

	public String getStringaConsumazione() {
		return mStringaConsumazione;
	}

	public String getNotaQgf() {
		return mNotaQgf;
	}

	//
	// METODI SET()
	//
	public void setIdFile(String aValore) {
		mIdFile = aValore;
	}

	public void setCodTipoReato(String aValore) {
		mCodTipoReato = aValore;
	}

	public void setDescrTipoReato(String aValore) {
		mDescrTipoReato = aValore;
	}

	public void setDataReato(Date aValore) {
		mDataReato = aValore;
	}

	public void setProgrNumeroManuale(String aValore) {
		mProgrNumeroManuale = aValore;
	}

	public void setProgrReato(int aValore) {
		mProgrReato = aValore;
	}

	public void setProgrCircostanza(int aValore) {
		mProgrCircostanza = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setAnnoInizio(int aValore) {
		mAnnoInizio = aValore;
	}

	public void setMeseInizio(int aValore) {
		mMeseInizio = aValore;
	}

	public void setGiornoInizio(int aValore) {
		mGiornoInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setAnnoFine(int aValore) {
		mAnnoFine = aValore;
	}

	public void setMeseFine(int aValore) {
		mMeseFine = aValore;
	}

	public void setGiornoFine(int aValore) {
		mGiornoFine = aValore;
	}

	public void setCodPeriodoConsumazione(String aValore) {
		mCodPeriodoConsumazione = aValore;
	}

	public void setDescrPeriodoConsumazione(String aValore) {
		mDescrPeriodoConsumazione = aValore;
	}

	public void setDescLuogo(String aValore) {
		mDescLuogo = aValore;
	}

	public void setCodFonte(String aValore) {
		mCodFonte = aValore;
	}

	public void setDescrFonte(String aValore) {
		mDescrFonte = aValore;
	}

	public void setAnnoFonte(int aValore) {
		mAnnoFonte = aValore;
	}

	public void setNumeroFonte(String aValore) {
		mNumeroFonte = aValore;
	}

	public void setCodSottonumerazione(String aValore) {
		mCodSottonumerazione = aValore;
	}

	public void setDescrSottonumerazione(String aValore) {
		mDescrSottonumerazione = aValore;
	}

	public void setComma(String aValore) {
		mComma = aValore;
	}

	public void setLettera(String aValore) {
		mLettera = aValore;
	}

	public void setNumero(String aValore) {
		mNumero = aValore;
	}

	public void setArticolo(String aValore) {
		mArticolo = aValore;
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

	public void setDataReatoCompleta(String aValore) {
		mDataReatoCompleta = aValore;
	}

	public void setStringaConsumazione(String aValore) {
		mStringaConsumazione = aValore;
	}

	public void setNotaQgf(String aValore) {
		mNotaQgf = aValore;
	}

	/**
	 * Calcola la stringa data da inserire all'interno della JSp di vista del reato
	 */
	public void calcolaDataReato() {
		String lDataCompleta = "";

		if (this.mGiornoInizio != 0 || this.mMeseInizio != 0 || this.mAnnoInizio != 0) {
			String lStrGGInizio = StringUtils.intZerotoString(this.mGiornoInizio, "**");
			if (!lStrGGInizio.equals("**") && lStrGGInizio.length() == 1)
				lStrGGInizio = "0" + lStrGGInizio;

			String lStrMMInizio = StringUtils.intZerotoString(this.mMeseInizio, "**");
			if (!lStrMMInizio.equals("**") && lStrMMInizio.length() == 1)
				lStrMMInizio = "0" + lStrMMInizio;

			String lStrAAInizio = StringUtils.intZerotoString(this.mAnnoInizio, "**");

			lDataCompleta = lStrGGInizio + "-" + lStrMMInizio + "-" + lStrAAInizio;
		}
		if ((this.mGiornoInizio != 0 || this.mMeseInizio != 0 || this.mAnnoInizio != 0)
				&& (this.mGiornoFine != 0 || this.mMeseFine != 0 || this.mAnnoFine != 0)) {
			lDataCompleta += "/";
		}

		if (this.mGiornoFine != 0 || this.mMeseFine != 0 || this.mAnnoFine != 0) {
			String lStrGGFine = StringUtils.intZerotoString(this.mGiornoFine, "**");
			if (!lStrGGFine.equals("**") && lStrGGFine.length() == 1)
				lStrGGFine = "0" + lStrGGFine;

			String lStrMMFine = StringUtils.intZerotoString(this.mMeseFine, "**");
			if (!lStrMMFine.equals("**") && lStrMMFine.length() == 1)
				lStrMMFine = "0" + lStrMMFine;

			String lStrAAFine = StringUtils.intZerotoString(this.mAnnoFine, "**");

			lDataCompleta += lStrGGFine + "-" + lStrMMFine + "-" + lStrAAFine;
		}

		if (lDataCompleta.equals("")) {
			lDataCompleta = "-";
		}
		// Set del parametro del model Reato
		this.mDataReatoCompleta = lDataCompleta;
	}

	/**
	 * Calcola la corretta stringa di consumazione del reato
	 */
	public void calcolaStringaConsumazione() {
		String lString = mDescrPeriodoConsumazione;
		// String ltemp = null;
		String lParziale = "";

		lString = lString.replace('[', 'X');
		lString = lString.replace(']', 'X');

		// Esistono le date di consumazione
		if (((mDataFine != null) || (mDataInizio != null)) && (lString != null && lString.length() > 1)) {
			if (mDataInizio != null)
				lString = lString.replaceFirst("Xdata1X",
						DateUtils.getDateToString(mDataInizio, "dd/MM/yyyy"));

			if (mDataFine != null)
				lString = lString.replaceFirst("Xdata2X", DateUtils.getDateToString(mDataFine, "dd/MM/yyyy"));
		}

		// Esistono le date di consumazione parziali
		if ((((mAnnoInizio != 0) || (mMeseInizio != 0) || (mGiornoInizio != 0)) && (mDataInizio == null))
				&& (lString != null && lString.length() > 1)) {
			if (mGiornoInizio == 0)
				lParziale = "**/";
			else
				lParziale = mGiornoInizio + "/";

			if (mMeseInizio == 0)
				lParziale += "**/";
			else
				lParziale += mMeseInizio + "/";

			if (mAnnoInizio == 0)
				lParziale += "****";
			else
				lParziale += mAnnoInizio;

			lString = lString.replaceFirst("Xdata1X", lParziale);

		}

		if ((((mAnnoFine != 0) || (mMeseFine != 0) || (mGiornoFine != 0)) && (mDataFine == null))
				&& (lString != null && lString.length() > 1)) {
			lParziale = "";
			if (mGiornoFine == 0)
				lParziale = "**/";
			else
				lParziale = mGiornoFine + "/";

			if (mMeseFine == 0)
				lParziale += "**/";
			else
				lParziale += mMeseFine + "/";

			if (mAnnoFine == 0)
				lParziale += "****";
			else
				lParziale += mAnnoFine;

			lString = lString.replaceFirst("Xdata2X", lParziale);
		}

		this.mStringaConsumazione = lString;
	}

}