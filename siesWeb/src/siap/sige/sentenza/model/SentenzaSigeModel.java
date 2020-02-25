package siap.sige.sentenza.model;

/**
* <p>Title: SentenzaSigeModel</p>
* <p>Description: Classe Model che estende  il SentenzaModel per memorizzare oltre alla Sentenza anche l'ID del record che memorizza l'aggregato FascicoloSige-Sentenza.
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;

public class SentenzaSigeModel extends SentenzaModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3442241418394043625L;

	// ID del record nella tabella FAS_SIGE_SENTENZA che individua univocamente l'aggregato
	// FascicoloSige-Sentenza
	private BigDecimal mIdFasSigeSentenza;
	private BigDecimal mFasIdFascicoloSige;
	private Date mDataIrrevocabilitaSige;
	private String mFlagCompetenza;
	private BigDecimal mFasSieIdFascicoloSiep;
	private FascicoloSiepModel mFascicoloSiep = null;

	// COSTRUTTORE DI DEFAULT
	public SentenzaSigeModel() {
		super();
		mIdFasSigeSentenza = null;
		mFasIdFascicoloSige = null;
		mDataIrrevocabilitaSige = null;
		mFlagCompetenza = "";
		mFasSieIdFascicoloSiep = null;
		mFascicoloSiep = null;
	}

	// COSTRUTTORE DI COPIA
	public SentenzaSigeModel(SentenzaSigeModel aModel) {
		super(aModel);
		mIdFasSigeSentenza = aModel.mIdFasSigeSentenza;
		mFasIdFascicoloSige = aModel.mFasIdFascicoloSige;
		mDataIrrevocabilitaSige = aModel.mDataIrrevocabilitaSige;
		mFlagCompetenza = aModel.mFlagCompetenza;
		mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		mFascicoloSiep = aModel.mFascicoloSiep;
	}

	// COSTRUTTORE DI fusione
	public SentenzaSigeModel(SentenzaSigeModel aModel1, SentenzaModel aModel2) {
		super(aModel2);
		mIdFasSigeSentenza = aModel1.mIdFasSigeSentenza;
		mFasIdFascicoloSige = aModel1.mFasIdFascicoloSige;
		mDataIrrevocabilitaSige = aModel1.mDataIrrevocabilitaSige;
		mFlagCompetenza = aModel1.mFlagCompetenza;
		mFasSieIdFascicoloSiep = aModel1.mFasSieIdFascicoloSiep;
		mFascicoloSiep = aModel1.mFascicoloSiep;
	}

	public SentenzaSigeModel(SentenzaSigeModel aModel, SentenzaModel aSentenzaModel,
			FascicoloSiepModel aFasSiepModel) {
		this(aModel, aSentenzaModel);
		if (aFasSiepModel != null)
			mFascicoloSiep = new FascicoloSiepModel(aFasSiepModel);
	}

	// COSTRUTTORE MODEL che crea un nuovo oggetto a partire da una Sentenza e dall'ID aIdFasSisgeSentenza
	public SentenzaSigeModel(BigDecimal aIdFasSisgeSentenza, SentenzaModel aModel) {
		super(aModel);
		mIdFasSigeSentenza = aIdFasSisgeSentenza;
	}

	// COSTRUTTORE MODEL che crea un nuovo oggetto SentenzaSigeModela partire da un SentenzaModel
	public SentenzaSigeModel(SentenzaModel aModel) {
		super(aModel);
		mIdFasSigeSentenza = null;
		mFasIdFascicoloSige = null;
		mDataIrrevocabilitaSige = null;
		mFlagCompetenza = "";
		mFasSieIdFascicoloSiep = null;
		mFascicoloSiep = null;

	}

	// COSTRUTTORE MODEL
	public SentenzaSigeModel(BigDecimal aIdFasSigeSentenza, BigDecimal aFasIdFascicoloSige,
			BigDecimal aSenIdSentenza, Date aDataInserimento, String aCodOperatoreInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, Date aDataIrrevocabilita,
			String aFlagCompetenza, Date aDataAggiornamento, String aCodOperatoreAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep)

	{
		this();
		mIdFasSigeSentenza = aIdFasSigeSentenza;
		mFasIdFascicoloSige = aFasIdFascicoloSige;
		setIdSentenza(aSenIdSentenza);
		setDataInserimento(aDataInserimento);
		setCodOperatoreInserimento(aCodOperatoreInserimento);
		setCodUfficioInserimento(aCodUfficioInserimento);
		setDescrUfficioInserimento(aDescrUfficioInserimento);
		mDataIrrevocabilitaSige = aDataIrrevocabilita;
		mFlagCompetenza = aFlagCompetenza;
		setDataAggiornamento(aDataAggiornamento);
		setCodOperatoreAggiornamento(aCodOperatoreAggiornamento);
		setCodUfficioAggiornamento(aCodUfficioAggiornamento);
		setDescrUfficioAggiornamento(aDescrUfficioAggiornamento);
		mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdFasSigeSentenza() {
		return mIdFasSigeSentenza;
	}

	public BigDecimal getFasIdFascicoloSige() {
		return mFasIdFascicoloSige;
	}

	public Date getDataIrrevocabilita() {
		return mDataIrrevocabilitaSige;
	}

	public String getFlagCompetenza() {
		return mFlagCompetenza;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	//
	// METODI SET()
	//
	public void setIdFasSigeSentenza(BigDecimal aValore) {
		mIdFasSigeSentenza = aValore;
	}

	public void setFasIdFascicoloSige(BigDecimal aValore) {
		mFasIdFascicoloSige = aValore;
	}

	public void setDataIrrevocabilita(Date aValore) {
		mDataIrrevocabilitaSige = aValore;
	}

	public void setFlagCompetenza(String aValore) {
		mFlagCompetenza = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}

}