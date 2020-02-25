package siap.siep.modulocumulo.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.model.PenaResiduaModel;

/**
 * <p>
 * Title: PenaRideterminataCumuloModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il PenaRideterminataCumulo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class PenaRideterminataCumuloModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 3455572538660359194L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private BigDecimal mIdPenaRideterminataCumulo;
	private String mCodTipoPenaDetentiva;
	private String mDescrTipoPenaDetentiva;
	private BigDecimal mNumAnniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mImportoMulta;
	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumGiorniArresto;
	private BigDecimal mImportoAmmenda;
	private BigDecimal mNumAnniIsolamentoDiurno;
	private BigDecimal mNumMesiIsolamentoDiurno;
	private BigDecimal mNumGiorniIsolamentoDiurno;
	private BigDecimal mNumeroGiorniLA;
	private BigDecimal mNumeroGiorniLS;
	private BigDecimal mNumeroGiorniLI;
	private BigDecimal mNumeroGiorniRiduzione;
	private BigDecimal mNumeroGiorniScomputo;

	private String mFlagPenaResiduaCumulo;
	private Date mDataInizio;
	private Date mDataFineReclusione;
	private Date mDataInizioArresto;
	private Date mDataFinePresunta;
	private Date mDataFine;

	private String mIsPenaDaRicalcolare;

	private BigDecimal mDatIdDatiFinaliCumulo;
	private BigDecimal mIstrIdIstruttoriaCumulo;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private String mTipoOperazioneCRUD; // I=Insert, D=Delete, U=Update

	// Stringhe per le stampe
	private String mStringaErgastolo;
	private String mStringaReclusione;
	private String mStringaMulta;

	private String mStringaArresto;
	private String mStringaAmmenda;

	private String mStringaLibAnt;
	private String mStringaScomputi;

	private String mStringaPenaResiduaXStampa;

	private BigDecimal mTotLA;

	private BigDecimal mTotPenaGG;

	private String mStringaPenaResiduaAdOggi;

	private String mAggiornaFlagRicalcoloPR;

	private String mStringaIsolamentoDiurno;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public PenaRideterminataCumuloModel() {
		this.mIdPenaRideterminataCumulo = null;
		this.mCodTipoPenaDetentiva = null;
		this.mDescrTipoPenaDetentiva = "";
		this.mNumAnniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumGiorniReclusione = null;
		this.mImportoMulta = null;
		this.mNumAnniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumGiorniArresto = null;
		this.mImportoAmmenda = null;
		this.mNumAnniIsolamentoDiurno = null;
		this.mNumMesiIsolamentoDiurno = null;
		this.mNumGiorniIsolamentoDiurno = null;
		this.mNumeroGiorniLA = null;
		this.mNumeroGiorniLS = null;
		this.mNumeroGiorniLI = null;
		this.mNumeroGiorniRiduzione = null;
		this.mNumeroGiorniScomputo = null;
		this.mFlagPenaResiduaCumulo = "";
		this.mDataInizio = null;
		this.mDataFineReclusione = null;
		this.mDataInizioArresto = null;
		this.mDataFinePresunta = null;
		this.mDataFine = null;
		this.mIsPenaDaRicalcolare = "";
		this.mDatIdDatiFinaliCumulo = null;
		this.mIstrIdIstruttoriaCumulo = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";

	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public PenaRideterminataCumuloModel(PenaRideterminataCumuloModel aModel) {
		this.mIdPenaRideterminataCumulo = aModel.mIdPenaRideterminataCumulo;
		this.mCodTipoPenaDetentiva = aModel.mCodTipoPenaDetentiva;
		this.mDescrTipoPenaDetentiva = aModel.mDescrTipoPenaDetentiva;
		this.mNumAnniReclusione = aModel.mNumAnniReclusione;
		this.mNumMesiReclusione = aModel.mNumMesiReclusione;
		this.mNumGiorniReclusione = aModel.mNumGiorniReclusione;
		this.mImportoMulta = aModel.mImportoMulta;
		this.mNumAnniArresto = aModel.mNumAnniArresto;
		this.mNumMesiArresto = aModel.mNumMesiArresto;
		this.mNumGiorniArresto = aModel.mNumGiorniArresto;
		this.mImportoAmmenda = aModel.mImportoAmmenda;
		this.mNumAnniIsolamentoDiurno = aModel.mNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aModel.mNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aModel.mNumGiorniIsolamentoDiurno;
		this.mNumeroGiorniLA = aModel.mNumeroGiorniLA;
		this.mNumeroGiorniLS = aModel.mNumeroGiorniLS;
		this.mNumeroGiorniLI = aModel.mNumeroGiorniLI;
		this.mNumeroGiorniRiduzione = aModel.mNumeroGiorniRiduzione;
		this.mNumeroGiorniScomputo = aModel.mNumeroGiorniScomputo;
		this.mFlagPenaResiduaCumulo = aModel.mFlagPenaResiduaCumulo;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFineReclusione = aModel.mDataFineReclusione;
		this.mDataInizioArresto = aModel.mDataInizioArresto;
		this.mDataFinePresunta = aModel.mDataFinePresunta;
		this.mDataFine = aModel.mDataFine;
		this.mIsPenaDaRicalcolare = aModel.mIsPenaDaRicalcolare;
		this.mDatIdDatiFinaliCumulo = aModel.mDatIdDatiFinaliCumulo;
		this.mIstrIdIstruttoriaCumulo = aModel.mIstrIdIstruttoriaCumulo;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public PenaRideterminataCumuloModel(BigDecimal aIdPenaRideterminataCumulo, String aCodTipoPenaDetentiva,
			String aDescrTipoPenaDetentiva, BigDecimal aNumAnniReclusione, BigDecimal aNumMesiReclusione,
			BigDecimal aNumGiorniReclusione, BigDecimal aImportoMulta, BigDecimal aNumAnniArresto,
			BigDecimal aNumMesiArresto, BigDecimal aNumGiorniArresto, BigDecimal aImportoAmmenda,
			BigDecimal aNumAnniIsolamentoDiurno, BigDecimal aNumMesiIsolamentoDiurno,
			BigDecimal aNumGiorniIsolamentoDiurno, BigDecimal aNumeroGiorniLA, BigDecimal aNumeroGiorniLS,
			BigDecimal aNumeroGiorniLI, BigDecimal aNumeroGiorniRiduzione, BigDecimal aNumeroGiorniScomputo,
			String aFlagPenaResiduaCumulo, Date aDataInizio, Date aDataFineReclusione,
			Date aDataInizioArresto, Date aDataFinePresunta, Date aDataFine, String aIsPenaDaRicalcolare,
			BigDecimal aDatIdDatiFinaliCumulo, BigDecimal aIstrIdIstruttoriaCumulo,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdPenaRideterminataCumulo = aIdPenaRideterminataCumulo;
		this.mCodTipoPenaDetentiva = aCodTipoPenaDetentiva;
		this.mDescrTipoPenaDetentiva = aDescrTipoPenaDetentiva;
		this.mNumAnniReclusione = aNumAnniReclusione;
		this.mNumMesiReclusione = aNumMesiReclusione;
		this.mNumGiorniReclusione = aNumGiorniReclusione;
		this.mImportoMulta = aImportoMulta;
		this.mNumAnniArresto = aNumAnniArresto;
		this.mNumMesiArresto = aNumMesiArresto;
		this.mNumGiorniArresto = aNumGiorniArresto;
		this.mImportoAmmenda = aImportoAmmenda;
		this.mNumAnniIsolamentoDiurno = aNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aNumGiorniIsolamentoDiurno;
		this.mNumeroGiorniLA = aNumeroGiorniLA;
		this.mNumeroGiorniLS = aNumeroGiorniLS;
		this.mNumeroGiorniLI = aNumeroGiorniLI;
		this.mNumeroGiorniRiduzione = aNumeroGiorniRiduzione;
		this.mNumeroGiorniScomputo = aNumeroGiorniScomputo;
		this.mFlagPenaResiduaCumulo = aFlagPenaResiduaCumulo;
		this.mDataInizio = aDataInizio;
		this.mDataFineReclusione = aDataFineReclusione;
		this.mDataInizioArresto = aDataInizioArresto;
		this.mDataFinePresunta = aDataFinePresunta;
		this.mDataFine = aDataFine;
		this.mIsPenaDaRicalcolare = aIsPenaDaRicalcolare;
		this.mDatIdDatiFinaliCumulo = aDatIdDatiFinaliCumulo;
		this.mIstrIdIstruttoriaCumulo = aIstrIdIstruttoriaCumulo;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	/**
	 * 
	 * @param aModel
	 */
	public PenaRideterminataCumuloModel(PenaResiduaModel aModel) {
		this.mIdPenaRideterminataCumulo = null;
		this.mCodTipoPenaDetentiva = null;
		this.mDescrTipoPenaDetentiva = null;
		this.mNumAnniReclusione = aModel.getNumAnniReclusione();
		this.mNumMesiReclusione = aModel.getNumMesiReclusione();
		this.mNumGiorniReclusione = aModel.getNumGiorniReclusione();
		this.mImportoMulta = aModel.getImportoMulta();
		this.mNumAnniArresto = aModel.getNumAnniArresto();
		this.mNumMesiArresto = aModel.getNumMesiArresto();
		this.mNumGiorniArresto = aModel.getNumGiorniArresto();
		this.mImportoAmmenda = aModel.getImportoAmmenda();
		this.mNumAnniIsolamentoDiurno = aModel.getNumAnniIsolamentoDiurno();
		this.mNumMesiIsolamentoDiurno = aModel.getNumMesiIsolamentoDiurno();
		this.mNumGiorniIsolamentoDiurno = aModel.getNumGiorniIsolamentoDiurno();
		this.mNumeroGiorniLA = null;
		this.mNumeroGiorniLS = null;
		this.mNumeroGiorniLI = null;
		this.mNumeroGiorniRiduzione = null;
		this.mNumeroGiorniScomputo = null;
		this.mFlagPenaResiduaCumulo = "S";
		this.mDataInizio = aModel.getDataInizio();
		this.mDataFineReclusione = aModel.getDataFineReclusione();
		this.mDataInizioArresto = aModel.getDataInizioArresto();
		this.mDataFinePresunta = aModel.getDataFinePresunta();
		this.mDataFine = aModel.getDataFine();
	}

	/**
	 * 
	 * @param aModel
	 * @return
	 */
	public boolean checkPenaModificata(PenaRideterminataCumuloModel aModel) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("this = " + this);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("aModel = " + aModel);

		if ((this.mCodTipoPenaDetentiva != null && aModel.mCodTipoPenaDetentiva == null)
				|| (this.mCodTipoPenaDetentiva == null && aModel.mCodTipoPenaDetentiva != null)
				|| (this.mCodTipoPenaDetentiva != null && aModel.mCodTipoPenaDetentiva != null
						&& !this.mCodTipoPenaDetentiva.equals(aModel.mCodTipoPenaDetentiva)))
			return true;

		// ==========================================================================
		if ((this.mNumAnniReclusione != null && aModel.mNumAnniReclusione == null)
				|| (this.mNumAnniReclusione == null && aModel.mNumAnniReclusione != null)
				|| (this.mNumAnniReclusione != null && aModel.mNumAnniReclusione != null
						&& this.mNumAnniReclusione.compareTo(aModel.mNumAnniReclusione) != 0))
			return true;

		if ((this.mNumMesiReclusione != null && aModel.mNumMesiReclusione == null)
				|| (this.mNumMesiReclusione == null && aModel.mNumMesiReclusione != null)
				|| (this.mNumMesiReclusione != null && aModel.mNumMesiReclusione != null
						&& this.mNumMesiReclusione.compareTo(aModel.mNumMesiReclusione) != 0))
			return true;

		if ((this.mNumGiorniReclusione != null && aModel.mNumGiorniReclusione == null)
				|| (this.mNumGiorniReclusione == null && aModel.mNumGiorniReclusione != null)
				|| (this.mNumGiorniReclusione != null && aModel.mNumGiorniReclusione != null
						&& this.mNumGiorniReclusione.compareTo(aModel.mNumGiorniReclusione) != 0))
			return true;

		if ((this.mImportoMulta != null && aModel.mImportoMulta == null)
				|| (this.mImportoMulta == null && aModel.mImportoMulta != null)
				|| (this.mImportoMulta != null && aModel.mImportoMulta != null
						&& this.mImportoMulta.compareTo(aModel.mImportoMulta) != 0))
			return true;

		// ==========================================================================
		if ((this.mNumAnniArresto != null && aModel.mNumAnniArresto == null)
				|| (this.mNumAnniArresto == null && aModel.mNumAnniArresto != null)
				|| (this.mNumAnniArresto != null && aModel.mNumAnniArresto != null
						&& this.mNumAnniArresto.compareTo(aModel.mNumAnniArresto) != 0))
			return true;

		if ((this.mNumMesiArresto != null && aModel.mNumMesiArresto == null)
				|| (this.mNumMesiArresto == null && aModel.mNumMesiArresto != null)
				|| (this.mNumMesiArresto != null && aModel.mNumMesiArresto != null
						&& this.mNumMesiArresto.compareTo(aModel.mNumMesiArresto) != 0))
			return true;

		if ((this.mNumGiorniArresto != null && aModel.mNumGiorniArresto == null)
				|| (this.mNumGiorniArresto == null && aModel.mNumGiorniArresto != null)
				|| (this.mNumGiorniArresto != null && aModel.mNumGiorniArresto != null
						&& this.mNumGiorniArresto.compareTo(aModel.mNumGiorniArresto) != 0))
			return true;

		if ((this.mImportoAmmenda != null && aModel.mImportoAmmenda == null)
				|| (this.mImportoAmmenda == null && aModel.mImportoAmmenda != null)
				|| (this.mImportoAmmenda != null && aModel.mImportoAmmenda != null
						&& this.mImportoAmmenda.compareTo(aModel.mImportoAmmenda) != 0))
			return true;

		// ==========================================================================
		if ((this.mNumAnniIsolamentoDiurno != null && aModel.mNumAnniIsolamentoDiurno == null)
				|| (this.mNumAnniIsolamentoDiurno == null && aModel.mNumAnniIsolamentoDiurno != null)
				|| (this.mNumAnniIsolamentoDiurno != null && aModel.mNumAnniIsolamentoDiurno != null
						&& this.mNumAnniIsolamentoDiurno.compareTo(aModel.mNumAnniIsolamentoDiurno) != 0))
			return true;

		if ((this.mNumMesiIsolamentoDiurno != null && aModel.mNumMesiIsolamentoDiurno == null)
				|| (this.mNumMesiIsolamentoDiurno == null && aModel.mNumMesiIsolamentoDiurno != null)
				|| (this.mNumMesiIsolamentoDiurno != null && aModel.mNumMesiIsolamentoDiurno != null
						&& this.mNumMesiIsolamentoDiurno.compareTo(aModel.mNumMesiIsolamentoDiurno) != 0))
			return true;

		if ((this.mNumGiorniIsolamentoDiurno != null && aModel.mNumGiorniIsolamentoDiurno == null)
				|| (this.mNumGiorniIsolamentoDiurno == null && aModel.mNumGiorniIsolamentoDiurno != null)
				|| (this.mNumGiorniIsolamentoDiurno != null && aModel.mNumGiorniIsolamentoDiurno != null
						&& this.mNumGiorniIsolamentoDiurno.compareTo(aModel.mNumGiorniIsolamentoDiurno) != 0))
			return true;

		// ==========================================================================
		if ((this.mNumeroGiorniLA != null && aModel.mNumeroGiorniLA == null)
				|| (this.mNumeroGiorniLA == null && aModel.mNumeroGiorniLA != null)
				|| (this.mNumeroGiorniLA != null && aModel.mNumeroGiorniLA != null
						&& this.mNumeroGiorniLA.compareTo(aModel.mNumeroGiorniLA) != 0))
			return true;

		if ((this.mNumeroGiorniLS != null && aModel.mNumeroGiorniLS == null)
				|| (this.mNumeroGiorniLS == null && aModel.mNumeroGiorniLS != null)
				|| (this.mNumeroGiorniLS != null && aModel.mNumeroGiorniLS != null
						&& this.mNumeroGiorniLS.compareTo(aModel.mNumeroGiorniLS) != 0))
			return true;

		if ((this.mNumeroGiorniLI != null && aModel.mNumeroGiorniLI == null)
				|| (this.mNumeroGiorniLI == null && aModel.mNumeroGiorniLI != null)
				|| (this.mNumeroGiorniLI != null && aModel.mNumeroGiorniLI != null
						&& this.mNumeroGiorniLI.compareTo(aModel.mNumeroGiorniLI) != 0))
			return true;

		if ((this.mNumeroGiorniRiduzione != null && aModel.mNumeroGiorniRiduzione == null)
				|| (this.mNumeroGiorniRiduzione == null && aModel.mNumeroGiorniRiduzione != null)
				|| (this.mNumeroGiorniRiduzione != null && aModel.mNumeroGiorniRiduzione != null
						&& this.mNumeroGiorniRiduzione.compareTo(aModel.mNumeroGiorniRiduzione) != 0))
			return true;

		if ((this.mNumeroGiorniScomputo != null && aModel.mNumeroGiorniScomputo == null)
				|| (this.mNumeroGiorniScomputo == null && aModel.mNumeroGiorniScomputo != null)
				|| (this.mNumeroGiorniScomputo != null && aModel.mNumeroGiorniScomputo != null
						&& this.mNumeroGiorniScomputo.compareTo(aModel.mNumeroGiorniScomputo) != 0))
			return true;
		// this.mDataInizio = aModel.mDataInizio;
		// this.mDataFineReclusione = aModel.mDataFineReclusione;
		// this.mDataInizioArresto = aModel.mDataInizioArresto;
		// this.mDataFinePresunta = aModel.mDataFinePresunta;
		// this.mDataFine = aModel.mDataFine;
		return false;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdPenaRideterminataCumulo() {
		return mIdPenaRideterminataCumulo;
	}

	public String getCodTipoPenaDetentiva() {
		return mCodTipoPenaDetentiva;
	}

	public String getDescrTipoPenaDetentiva() {
		return mDescrTipoPenaDetentiva;
	}

	public BigDecimal getNumAnniReclusione() {
		return mNumAnniReclusione;
	}

	public BigDecimal getNumMesiReclusione() {
		return mNumMesiReclusione;
	}

	public BigDecimal getNumGiorniReclusione() {
		return mNumGiorniReclusione;
	}

	public BigDecimal getImportoMulta() {
		return mImportoMulta;
	}

	public BigDecimal getNumAnniArresto() {
		return mNumAnniArresto;
	}

	public BigDecimal getNumMesiArresto() {
		return mNumMesiArresto;
	}

	public BigDecimal getNumGiorniArresto() {
		return mNumGiorniArresto;
	}

	public BigDecimal getImportoAmmenda() {
		return mImportoAmmenda;
	}

	public BigDecimal getNumAnniIsolamentoDiurno() {
		return mNumAnniIsolamentoDiurno;
	}

	public BigDecimal getNumMesiIsolamentoDiurno() {
		return mNumMesiIsolamentoDiurno;
	}

	public BigDecimal getNumGiorniIsolamentoDiurno() {
		return mNumGiorniIsolamentoDiurno;
	}

	public BigDecimal getNumeroGiorniLA() {
		return mNumeroGiorniLA;
	}

	public BigDecimal getNumeroGiorniLS() {
		return mNumeroGiorniLS;
	}

	public BigDecimal getNumeroGiorniLI() {
		return mNumeroGiorniLI;
	}

	public BigDecimal getNumeroGiorniRiduzione() {
		return mNumeroGiorniRiduzione;
	}

	public BigDecimal getNumeroGiorniScomputo() {
		return mNumeroGiorniScomputo;
	}

	public String getFlagPenaResiduaCumulo() {
		return mFlagPenaResiduaCumulo;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFineReclusione() {
		return mDataFineReclusione;
	}

	public Date getDataInizioArresto() {
		return mDataInizioArresto;
	}

	public Date getDataFinePresunta() {
		return mDataFinePresunta;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public String getIsPenaDaRicalcolare() {
		return mIsPenaDaRicalcolare;
	}

	public BigDecimal getDatIdDatiFinaliCumulo() {
		return mDatIdDatiFinaliCumulo;
	}

	public BigDecimal getIstrIdIstruttoriaCumulo() {
		return mIstrIdIstruttoriaCumulo;
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

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getTipoOperazioneCRUD() {
		return mTipoOperazioneCRUD;
	}

	public String getStringaErgastolo() {
		return mStringaErgastolo;
	}

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getStringaMulta() {
		return mStringaMulta;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	public String getStringaAmmenda() {
		return mStringaAmmenda;
	}

	public String getStringaPenaResiduaXStampa() {
		return mStringaPenaResiduaXStampa;
	}

	public String getStringaLibAnt() {
		return mStringaLibAnt;
	}

	public String getStringaScomputi() {
		return mStringaScomputi;
	}

	public BigDecimal getTotLA() {
		return mTotLA;
	}

	public BigDecimal getTotPenaGG() {
		return mTotPenaGG;
	}

	public String getStringaPenaResiduaAdOggi() {
		return mStringaPenaResiduaAdOggi;
	}

	public String getAggiornaFlagRicalcoloPR() {
		return mAggiornaFlagRicalcoloPR;
	}

	public String getStringaIsolamentoDiurno() {
		return mStringaIsolamentoDiurno;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdPenaRideterminataCumulo(BigDecimal aValore) {
		mIdPenaRideterminataCumulo = aValore;
	}

	public void setCodTipoPenaDetentiva(String aValore) {
		mCodTipoPenaDetentiva = aValore;
	}

	public void setDescrTipoPenaDetentiva(String aValore) {
		mDescrTipoPenaDetentiva = aValore;
	}

	public void setNumAnniReclusione(BigDecimal aValore) {
		mNumAnniReclusione = aValore;
	}

	public void setNumMesiReclusione(BigDecimal aValore) {
		mNumMesiReclusione = aValore;
	}

	public void setNumGiorniReclusione(BigDecimal aValore) {
		mNumGiorniReclusione = aValore;
	}

	public void setImportoMulta(BigDecimal aValore) {
		mImportoMulta = aValore;
	}

	public void setNumAnniArresto(BigDecimal aValore) {
		mNumAnniArresto = aValore;
	}

	public void setNumMesiArresto(BigDecimal aValore) {
		mNumMesiArresto = aValore;
	}

	public void setNumGiorniArresto(BigDecimal aValore) {
		mNumGiorniArresto = aValore;
	}

	public void setImportoAmmenda(BigDecimal aValore) {
		mImportoAmmenda = aValore;
	}

	public void setNumAnniIsolamentoDiurno(BigDecimal aValore) {
		mNumAnniIsolamentoDiurno = aValore;
	}

	public void setNumMesiIsolamentoDiurno(BigDecimal aValore) {
		mNumMesiIsolamentoDiurno = aValore;
	}

	public void setNumGiorniIsolamentoDiurno(BigDecimal aValore) {
		mNumGiorniIsolamentoDiurno = aValore;
	}

	public void setNumeroGiorniLA(BigDecimal aValore) {
		mNumeroGiorniLA = aValore;
	}

	public void setNumeroGiorniLS(BigDecimal aValore) {
		mNumeroGiorniLS = aValore;
	}

	public void setNumeroGiorniLI(BigDecimal aValore) {
		mNumeroGiorniLI = aValore;
	}

	public void setNumeroGiorniRiduzione(BigDecimal aValore) {
		mNumeroGiorniRiduzione = aValore;
	}

	public void setNumeroGiorniScomputo(BigDecimal aValore) {
		mNumeroGiorniScomputo = aValore;
	}

	public void setFlagPenaResiduaCumulo(String aValore) {
		mFlagPenaResiduaCumulo = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFineReclusione(Date aValore) {
		mDataFineReclusione = aValore;
	}

	public void setDataInizioArresto(Date aValore) {
		mDataInizioArresto = aValore;
	}

	public void setDataFinePresunta(Date aValore) {
		mDataFinePresunta = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setIsPenaDaRicalcolare(String aValore) {
		mIsPenaDaRicalcolare = aValore;
	}

	public void setDatIdDatiFinaliCumulo(BigDecimal aValore) {
		mDatIdDatiFinaliCumulo = aValore;
	}

	public void setIstrIdIstruttoriaCumulo(BigDecimal aValore) {
		mIstrIdIstruttoriaCumulo = aValore;
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

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setTipoOperazioneCRUD(String aValore) {
		mTipoOperazioneCRUD = aValore;
	}

	public void setStringaErgastolo(String aValore) {
		mStringaErgastolo = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setStringaMulta(String aValore) {
		mStringaMulta = aValore;
	}

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaScomputi(String aValore) {
		mStringaScomputi = aValore;
	}

	public void setStringaAmmenda(String aValore) {
		mStringaAmmenda = aValore;
	}

	public void setStringaLibAnt(String aValore) {
		mStringaLibAnt = aValore;
	}

	public void setStringaPenaResiduaXStampa(String aValore) {
		mStringaPenaResiduaXStampa = aValore;
	}

	public void setTotLA(BigDecimal aValore) {
		mTotLA = aValore;
	}

	public void setTotPenaGG(BigDecimal aValore) {
		mTotPenaGG = aValore;
	}

	public void setStringaPenaResiduaAdOggi(String aValore) {
		mStringaPenaResiduaAdOggi = aValore;
	}

	public void setAggiornaFlagRicalcoloPR(String aValore) {
		mAggiornaFlagRicalcoloPR = aValore;
	}

	public void setStringaIsolamentoDiurno(String aValore) {
		mStringaIsolamentoDiurno = aValore;
	}

	// ============================================================================
	public boolean isReclusione() {
		if ((mNumAnniReclusione != null && mNumAnniReclusione.intValue() != 0)
				|| (mNumMesiReclusione != null && mNumMesiReclusione.intValue() != 0)
				|| (mNumGiorniReclusione != null && mNumGiorniReclusione.intValue() != 0))
			return true;
		else
			return false;
	}

  public boolean isNegativeReclusione(){
    int totGGRec = 0;
    if (mNumAnniReclusione!=null) totGGRec += 360*mNumAnniReclusione.intValue();
    if (mNumMesiReclusione!=null) totGGRec += 30*mNumMesiReclusione.intValue();
    if (mNumGiorniReclusione!=null) totGGRec += mNumGiorniReclusione.intValue();
    
    if (totGGRec<0)
      return true;
    else 
      return false;
  }
  
  public boolean isNegativeArresto() {
    int totGGArr = 0;
    if (mNumAnniArresto!=null) totGGArr += 360*mNumAnniArresto.intValue();
    if (mNumMesiArresto!=null) totGGArr += 30*mNumMesiArresto.intValue();
    if (mNumGiorniArresto!=null) totGGArr += mNumGiorniArresto.intValue();
    
    if (totGGArr<0)
      return true;
    else 
      return false;
  }
  
  
	public boolean isMulta() {
		if (mImportoMulta != null && mImportoMulta.compareTo(BigDecimal.ZERO) > 0)
			return true;
		else
			return false;
	}

	public boolean isArresto() {
		if ((mNumAnniArresto != null && mNumAnniArresto.intValue() != 0)
				|| (mNumMesiArresto != null && mNumMesiArresto.intValue() != 0)
				|| (mNumGiorniArresto != null && mNumGiorniArresto.intValue() != 0))
			return true;
		else
			return false;
	}

	public boolean isAmmenda() {
		if (mImportoAmmenda != null && mImportoAmmenda.compareTo(BigDecimal.ZERO) > 0)
			return true;
		else
			return false;
	}

	public boolean isErgastolo() {
		// if (mNumAnniIsolamentoDiurno!=null || mNumMesiIsolamentoDiurno!=null ||
		// mNumGiorniIsolamentoDiurno!=null)
		if (mCodTipoPenaDetentiva != null)
			return true;
		else
			return false;
	}

	public boolean isLibAnt() {
		if (mNumeroGiorniLA != null || mNumeroGiorniLS != null || mNumeroGiorniLI != null
				|| mNumeroGiorniRiduzione != null || mNumeroGiorniScomputo != null)
			return true;
		else
			return false;
	}

	public boolean isValorizzato() {
		if (isReclusione() || isArresto() || isErgastolo() || isLibAnt() || isMulta() || isAmmenda()) {
			return true;
		} else
			return false;
	}

	/**
	 * Restituice un pena ResiduaModel prelevando i dati dalle pena rideterminate
	 * 
	 * @return
	 */
	public PenaResiduaModel getPenaResidua() {
		PenaResiduaModel lPenaResidua = new PenaResiduaModel();

		lPenaResidua.setIdPenaResidua(null);

		// Reclusione/multa
		lPenaResidua.setNumAnniReclusione(mNumAnniReclusione);
		lPenaResidua.setNumMesiReclusione(mNumMesiReclusione);
		lPenaResidua.setNumGiorniReclusione(mNumGiorniReclusione);
		lPenaResidua.setImportoMulta(mImportoMulta);

		// Arresti/Ammenda
		lPenaResidua.setNumAnniArresto(mNumAnniArresto);
		lPenaResidua.setNumMesiArresto(mNumMesiArresto);
		lPenaResidua.setNumGiorniArresto(mNumGiorniArresto);
		lPenaResidua.setImportoAmmenda(mImportoAmmenda);

		// Decorrenza/Scadenza
		lPenaResidua.setDataInizio(mDataInizio);
		lPenaResidua.setDataFineReclusione(mDataFineReclusione);
		lPenaResidua.setDataInizioArresto(mDataInizioArresto);
		lPenaResidua.setDataFinePresunta(mDataFinePresunta);
		lPenaResidua.setDataFine(mDataFine);

		// Non significativo si mette a S (favor rei)
		lPenaResidua.setDiesAQuo("S");

		// N, S = SI, D = S con isolamento diurno
		if ("03".equals(mCodTipoPenaDetentiva))
			lPenaResidua.setFlagErgastolo("S"); // Ergastolo Semplice
		else if ("04".equals(mCodTipoPenaDetentiva))
			lPenaResidua.setFlagErgastolo("D"); // Ergastolo Con isolamento Diurno
		else
			lPenaResidua.setFlagErgastolo("N"); // NON Ergastolo Con

		lPenaResidua.setNumAnniIsolamentoDiurno(mNumAnniIsolamentoDiurno);
		lPenaResidua.setNumMesiIsolamentoDiurno(mNumMesiIsolamentoDiurno);
		lPenaResidua.setNumGiorniIsolamentoDiurno(mNumGiorniIsolamentoDiurno);

		// Campi non gestiti in SIEP
		lPenaResidua.setDataInizioIsolamentoDiurno(null);
		lPenaResidua.setDataFineIsolamentoDiurno(null);

		//
		lPenaResidua.setCodOperatoreInserimento(mCodOperatoreInserimento);
		lPenaResidua.setCodUfficioInserimento(mCodUfficioInserimento);
		lPenaResidua.setDataInserimento(mDataInserimento);

		return lPenaResidua;
	}

	/**
	 * Metodo che rimappa i dati della PenaRideterminata nel model della PenaCumulo per le stampe
	 * 
	 * @return
	 */
	public PenaCumuloModel getPenaCumuloModel() {
		PenaCumuloModel lPenaCumulo = new PenaCumuloModel();

		lPenaCumulo.setIdPenaCumulo(null);

		// N, S = SI, D = S con isolamento diurno
		if ("03".equals(mCodTipoPenaDetentiva))
			lPenaCumulo.setFlagErgastolo("S"); // Ergastolo Semplice
		else if ("04".equals(mCodTipoPenaDetentiva))
			lPenaCumulo.setFlagErgastolo("D"); // Ergastolo Con isolamento Diurno
		else
			lPenaCumulo.setFlagErgastolo("N"); // NON Ergastolo Con

		// FIXME da verificare se nel caso di Ergastolo + Detentiva si vogliono
		// visualizzare i dati della detentiva
		// if (1 == 1 || "N".equals(lPenaCumulo.getFlagErgastolo())) {
		// Reclusione/multa
		lPenaCumulo.setNumAnniReclusione(mNumAnniReclusione);
		lPenaCumulo.setNumMesiReclusione(mNumMesiReclusione);
		lPenaCumulo.setNumGiorniReclusione(mNumGiorniReclusione);
		lPenaCumulo.setImportoMulta(mImportoMulta);

		// Arresti/Ammenda
		lPenaCumulo.setNumAnniArresto(mNumAnniArresto);
		lPenaCumulo.setNumMesiArresto(mNumMesiArresto);
		lPenaCumulo.setNumGiorniArresto(mNumGiorniArresto);
		lPenaCumulo.setImportoAmmenda(mImportoAmmenda);
		// }

		int totLA = 0;
		if (mNumeroGiorniLA != null)
			totLA += mNumeroGiorniLA.intValue();
		if (mNumeroGiorniLS != null)
			totLA += mNumeroGiorniLS.intValue();
		if (mNumeroGiorniLI != null)
			totLA += mNumeroGiorniLI.intValue();

		if (totLA > 0)
			lPenaCumulo.setNumGiorniLibAnticipata(new BigDecimal(totLA));

		lPenaCumulo.setNumGiorniLibAnticipataLA(mNumeroGiorniLA);
		lPenaCumulo.setNumGiorniLibAnticipataSPE(mNumeroGiorniLS);
		lPenaCumulo.setNumGiorniLibAnticipataINT(mNumeroGiorniLI);
		lPenaCumulo.setNumGiorniRiduzionePena(mNumeroGiorniRiduzione);

		// Decorrenza/Scadenza
		lPenaCumulo.setDataDecorrenzaPena(mDataInizio);

		lPenaCumulo.setNumAnniIsolamentoDiurno(mNumAnniIsolamentoDiurno);
		lPenaCumulo.setNumMesiIsolamentoDiurno(mNumMesiIsolamentoDiurno);
		lPenaCumulo.setNumGiorniIsolamentoDiurno(mNumGiorniIsolamentoDiurno);

		return lPenaCumulo;
	}

	/**
	 * Metodo cre crea i record LA a pertire dei dati presenti su PenaRideterminataCumulo
	 * 
	 * @return
	 */
	public Vector<LicenzaLibAnticipataModel> getLiberazioniAnticipate() {
		Vector<LicenzaLibAnticipataModel> lListaLA = new Vector<>();

		String lFlagElaborato = "N";
		// LA Elaborate se fine pena valorizzato e non Ergastolo
		if (mDataFine != null && !"03".equals(mCodTipoPenaDetentiva) && !"04".equals(mCodTipoPenaDetentiva)) {
			lFlagElaborato = "S";
		}

		if (mNumeroGiorniLA != null && mNumeroGiorniLA.intValue() > 0) {
			LicenzaLibAnticipataModel lLA = new LicenzaLibAnticipataModel();

			lLA.setCodTipoLicenza("LA"); // Sempre LA
			lLA.setNumeroGiorni(mNumeroGiorniLA);

			lLA.setFlagConcesso("C");
			lLA.setFlagElaborato(lFlagElaborato);
			lLA.setDescrStatoPermesso("LA");
			lLA.setFlagScorta("N");

			lLA.setCodOperatoreInserimento(mCodOperatoreInserimento);
			lLA.setCodUfficioInserimento(mCodUfficioInserimento);
			lLA.setDataInserimento(mDataInserimento);

			lListaLA.add(lLA);
		}

		if (mNumeroGiorniLI != null && mNumeroGiorniLI.intValue() > 0) {
			LicenzaLibAnticipataModel lLA = new LicenzaLibAnticipataModel();

			lLA.setCodTipoLicenza("LA"); // Sempre LA
			lLA.setNumeroGiorni(mNumeroGiorniLI);

			lLA.setFlagConcesso("C");
			lLA.setFlagElaborato(lFlagElaborato);
			lLA.setDescrStatoPermesso("LI");
			lLA.setFlagScorta("N");

			lLA.setCodOperatoreInserimento(mCodOperatoreInserimento);
			lLA.setCodUfficioInserimento(mCodUfficioInserimento);
			lLA.setDataInserimento(mDataInserimento);

			lListaLA.add(lLA);
		}

		if (mNumeroGiorniLS != null && mNumeroGiorniLS.intValue() > 0) {
			LicenzaLibAnticipataModel lLA = new LicenzaLibAnticipataModel();

			lLA.setCodTipoLicenza("LA"); // Sempre LA
			lLA.setNumeroGiorni(mNumeroGiorniLS);

			lLA.setFlagConcesso("C");
			lLA.setFlagElaborato(lFlagElaborato);
			lLA.setDescrStatoPermesso("LS");
			lLA.setFlagScorta("N");

			lLA.setCodOperatoreInserimento(mCodOperatoreInserimento);
			lLA.setCodUfficioInserimento(mCodUfficioInserimento);
			lLA.setDataInserimento(mDataInserimento);

			lListaLA.add(lLA);
		}

		if (mNumeroGiorniRiduzione != null && mNumeroGiorniRiduzione.intValue() > 0) {
			LicenzaLibAnticipataModel lLA = new LicenzaLibAnticipataModel();

			lLA.setCodTipoLicenza("RD"); // RD
			lLA.setNumeroGiorni(mNumeroGiorniRiduzione);

			lLA.setFlagConcesso("C");
			lLA.setFlagElaborato(lFlagElaborato);
			lLA.setDescrStatoPermesso(null);
			lLA.setFlagScorta("N");

			lLA.setCodOperatoreInserimento(mCodOperatoreInserimento);
			lLA.setCodUfficioInserimento(mCodUfficioInserimento);
			lLA.setDataInserimento(mDataInserimento);

			lListaLA.add(lLA);
		}

		// FIXME verificare se funziona
		if (mNumeroGiorniScomputo != null && mNumeroGiorniScomputo.intValue() > 0) {
			LicenzaLibAnticipataModel lLA = new LicenzaLibAnticipataModel();

			lLA.setCodTipoLicenza("PP"); // Può essere solo uno scomputo
			lLA.setNumeroGiorni(mNumeroGiorniScomputo);

			lLA.setFlagConcesso("C"); //
			lLA.setFlagElaborato(lFlagElaborato);
			lLA.setDescrStatoPermesso(null);
			lLA.setFlagScorta("N");

			lLA.setCodOperatoreInserimento(mCodOperatoreInserimento);
			lLA.setCodUfficioInserimento(mCodUfficioInserimento);
			lLA.setDataInserimento(mDataInserimento);

			lListaLA.add(lLA);
		}

		return lListaLA;
	}

	public void calcolaStringheXStampa() {
		calcolaStringaErgastolo();
		calcolaStringaReclusione();
		calcolaStringaMulta();
		calcolaStringaAmmenda();
		calcolaStringaArresto();
		calcolaStringaLA();
		calcolaStringaScomputi();

		calcolaTotLA();
		calcolaTotPenaGG();

		calcolaStringaPenaResidaXStampa();
	}

	public void calcolaStringaErgastolo() {
		String lStrErgastolo = null;

		if ("03".equals(mCodTipoPenaDetentiva)) {
			lStrErgastolo = "Ergastolo";
		} else if ("04".equals(mCodTipoPenaDetentiva)) {
			lStrErgastolo = "Ergastolo con isolamento diurno";

			String lDurata = "";
			if (mNumAnniIsolamentoDiurno != null && mNumAnniIsolamentoDiurno.intValue() != 0)
				lDurata += "Anni " + mNumAnniIsolamentoDiurno;

			if (mNumMesiIsolamentoDiurno != null && mNumMesiIsolamentoDiurno.intValue() != 0)
				lDurata += " Mesi " + mNumMesiIsolamentoDiurno;

			if (mNumGiorniIsolamentoDiurno != null && mNumGiorniIsolamentoDiurno.intValue() != 0)
				lDurata += " Giorni " + mNumGiorniIsolamentoDiurno;

			lStrErgastolo += " per " + lDurata;
		}
		mStringaErgastolo = lStrErgastolo;
	}

	public void calcolaStringaReclusione() {
		String lStringReclusione = "";
		if (mNumAnniReclusione != null && mNumAnniReclusione.intValue() != 0)
			lStringReclusione = "Anni " + mNumAnniReclusione;

		if (mNumMesiReclusione != null && mNumMesiReclusione.intValue() != 0)
			lStringReclusione += " Mesi " + mNumMesiReclusione;

		if (mNumGiorniReclusione != null && mNumGiorniReclusione.intValue() != 0)
			lStringReclusione += " Giorni " + mNumGiorniReclusione;

		if (lStringReclusione.length() > 1) {
			this.mStringaReclusione = lStringReclusione.trim();
		} else {
			this.mStringaReclusione = null;
		}
	}

	public void calcolaStringaMulta() {
		String lStrMulta = null;
		if (mImportoMulta != null && mImportoMulta.intValue() > 0)
			lStrMulta = "Multa Euro " + StringUtils.toEuroFormat(mImportoMulta);

		mStringaMulta = lStrMulta;
	}

	public void calcolaStringaArresto() {
		String lStringArresto = "";

		if (mNumAnniArresto != null && mNumAnniArresto.intValue() != 0)
			lStringArresto = "Anni " + mNumAnniArresto;

		if (mNumMesiArresto != null && mNumMesiArresto.intValue() != 0)
			lStringArresto += " Mesi " + mNumMesiArresto;

		if (mNumGiorniArresto != null && mNumGiorniArresto.intValue() != 0)
			lStringArresto += " Giorni " + mNumGiorniArresto;

		if (lStringArresto.length() > 1) {
			this.mStringaArresto = lStringArresto.trim();
		} else {
			this.mStringaArresto = null;
		}
	}

	public void calcolaStringaAmmenda() {
		String lStrAmmenda = null;

		if (mImportoAmmenda != null && mImportoAmmenda.intValue() > 0)
			lStrAmmenda = "Ammenda Euro " + StringUtils.toEuroFormat(mImportoAmmenda);

		mStringaAmmenda = lStrAmmenda;
	}

	public void calcolaStringaLA() {
		String lStrLA = null;
		int lTotLA = 0;

		if (mNumeroGiorniLA != null && mNumeroGiorniLA.intValue() > 0)
			lTotLA = lTotLA + mNumeroGiorniLA.intValue();

		if (mNumeroGiorniLS != null && mNumeroGiorniLS.intValue() > 0)
			lTotLA = lTotLA + mNumeroGiorniLS.intValue();

		if (mNumeroGiorniLI != null && mNumeroGiorniLI.intValue() > 0)
			lTotLA = lTotLA + mNumeroGiorniLI.intValue();

		if (mNumeroGiorniRiduzione != null && mNumeroGiorniRiduzione.intValue() > 0)
			lTotLA = lTotLA + mNumeroGiorniRiduzione.intValue();

		if (lTotLA > 0)
			lStrLA = "giorni " + lTotLA + "";

		mStringaLibAnt = lStrLA;
	}

	public void calcolaStringaScomputi() {
		String lStrScomputi = null;
		int lTotScomputi = 0;

		if (mNumeroGiorniScomputo != null && mNumeroGiorniScomputo.intValue() > 0)
			lTotScomputi = lTotScomputi + mNumeroGiorniScomputo.intValue();

		if (lTotScomputi > 0)
			lStrScomputi = "giorni " + lTotScomputi + "";

		mStringaScomputi = lStrScomputi;
	}

	public void calcolaStringaPenaResidaXStampa() {
		String lPenaResiduaStr = "";

		if ("03".equals(mCodTipoPenaDetentiva)) {
			lPenaResiduaStr = "Ergastolo";
		} else if ("04".equals(mCodTipoPenaDetentiva)) {
			lPenaResiduaStr = "Ergastolo con isolamento diurno";

			String lDurata = "";
			if (mNumAnniIsolamentoDiurno != null && mNumAnniIsolamentoDiurno.intValue() != 0)
				lDurata = "Anni " + mNumAnniIsolamentoDiurno;

			if (mNumMesiIsolamentoDiurno != null && mNumMesiIsolamentoDiurno.intValue() != 0)
				lDurata += " Mesi " + mNumMesiIsolamentoDiurno;

			if (mNumGiorniIsolamentoDiurno != null && mNumGiorniIsolamentoDiurno.intValue() != 0)
				lDurata += " Giorni " + mNumGiorniIsolamentoDiurno;

			lPenaResiduaStr += " per " + lDurata;
		} else {
			calcolaStringaReclusione();
			if (mStringaReclusione != null)
				lPenaResiduaStr += " Reclusione " + mStringaReclusione;

			if (mImportoMulta != null && mImportoMulta.intValue() > 0)
				lPenaResiduaStr += " Multa Euro " + StringUtils.toEuroFormat(mImportoMulta);

			calcolaStringaArresto();
			if (mStringaArresto != null)
				lPenaResiduaStr += " Arresto " + mStringaArresto;

			if (mImportoAmmenda != null && mImportoAmmenda.intValue() > 0)
				lPenaResiduaStr += " Ammenda Euro " + StringUtils.toEuroFormat(mImportoAmmenda);
		}

		mStringaPenaResiduaXStampa = lPenaResiduaStr.trim();

	}

	public void calcolaTotLA() {
		int lTotLA = 0;

		if (mNumeroGiorniLA != null && mNumeroGiorniLA.intValue() > 0)
			lTotLA = lTotLA + mNumeroGiorniLA.intValue();

		if (mNumeroGiorniLS != null && mNumeroGiorniLS.intValue() > 0)
			lTotLA = lTotLA + mNumeroGiorniLS.intValue();

		if (mNumeroGiorniLI != null && mNumeroGiorniLI.intValue() > 0)
			lTotLA = lTotLA + mNumeroGiorniLI.intValue();

		if (mNumeroGiorniRiduzione != null && mNumeroGiorniRiduzione.intValue() > 0)
			lTotLA = lTotLA + mNumeroGiorniRiduzione.intValue();

		if (lTotLA > 0)
			mTotLA = new BigDecimal(lTotLA);
		else
			mTotLA = null;
	}

	public void calcolaTotPenaGG() {
		int lTotPenaGG = 0;

		// Reclusione
		if (mNumAnniReclusione != null && mNumAnniReclusione.intValue() > 0)
			lTotPenaGG = lTotPenaGG + mNumAnniReclusione.intValue() * 360;

		if (mNumMesiReclusione != null && mNumMesiReclusione.intValue() > 0)
			lTotPenaGG = lTotPenaGG + mNumMesiReclusione.intValue() * 30;

		if (mNumGiorniReclusione != null && mNumGiorniReclusione.intValue() > 0)
			lTotPenaGG = lTotPenaGG + mNumGiorniReclusione.intValue();

		// Arresto
		if (mNumAnniArresto != null && mNumAnniArresto.intValue() > 0)
			lTotPenaGG = lTotPenaGG + mNumAnniArresto.intValue() * 360;

		if (mNumMesiArresto != null && mNumMesiArresto.intValue() > 0)
			lTotPenaGG = lTotPenaGG + mNumMesiArresto.intValue() * 30;

		if (mNumGiorniArresto != null && mNumGiorniArresto.intValue() > 0)
			lTotPenaGG = lTotPenaGG + mNumGiorniArresto.intValue();

		if (lTotPenaGG > 0)
			mTotPenaGG = new BigDecimal(lTotPenaGG);
		else
			mTotPenaGG = null;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "PenaRideterminataCumuloModel:\n" + "[ mIdPenaRideterminataCumulo = "
				+ mIdPenaRideterminataCumulo + " ]\n" + "[ mCodTipoPenaDetentiva      = "
				+ mCodTipoPenaDetentiva + " ]\n" + "[ mNumAnniReclusione         = " + mNumAnniReclusione
				+ " ]\n" + "[ mNumMesiReclusione         = " + mNumMesiReclusione + " ]\n"
				+ "[ mNumGiorniReclusione       = " + mNumGiorniReclusione + " ]\n"
				+ "[ mImportoMulta              = " + mImportoMulta + " ]\n"
				+ "[ mNumAnniArresto            = " + mNumAnniArresto + " ]\n"
				+ "[ mNumMesiArresto            = " + mNumMesiArresto + " ]\n"
				+ "[ mNumGiorniArresto          = " + mNumGiorniArresto + " ]\n"
				+ "[ mImportoAmmenda            = " + mImportoAmmenda + " ]\n"
				+ "[ mNumAnniIsolamentoDiurno   = " + mNumAnniIsolamentoDiurno + " ]\n"
				+ "[ mNumMesiIsolamentoDiurno   = " + mNumMesiIsolamentoDiurno + " ]\n"
				+ "[ mNumGiorniIsolamentoDiurno = " + mNumGiorniIsolamentoDiurno + " ]\n"
				+ "[ mNumeroGiorniLA            = " + mNumeroGiorniLA + " ]\n"
				+ "[ mNumeroGiorniLS            = " + mNumeroGiorniLS + " ]\n"
				+ "[ mNumeroGiorniLI            = " + mNumeroGiorniLI + " ]\n"
				+ "[ mNumeroGiorniRiduzione     = " + mNumeroGiorniRiduzione + " ]\n"
				+ "[ mNumeroGiorniScomputo      = " + mNumeroGiorniScomputo + " ]\n"
				+ "[ mFlagPenaResiduaCumulo     = " + mFlagPenaResiduaCumulo + " ]\n"
				+ "[ mDataInizio                = " + DateUtils.getDateToString(mDataInizio, "dd/MM/yyyy")
				+ " ]\n" + "[ mDataFineReclusione        = "
				+ DateUtils.getDateToString(mDataFineReclusione, "dd/MM/yyyy") + " ]\n"
				+ "[ mDataInizioArresto         = "
				+ DateUtils.getDateToString(mDataInizioArresto, "dd/MM/yyyy") + " ]\n"
				+ "[ mDataFinePresunta          = "
				+ DateUtils.getDateToString(mDataFinePresunta, "dd/MM/yyyy") + " ]\n"
				+ "[ mDataFine                  = " + DateUtils.getDateToString(mDataFine, "dd/MM/yyyy")
				+ " ]\n" + "[ mIsPenaDaRicalcolare       = " + mIsPenaDaRicalcolare + " ]\n"
				+ "[ mDatIdDatiFinaliCumulo     = " + mDatIdDatiFinaliCumulo + " ]\n"
				+ "[ mIstrIdIstruttoriaCumulo   = " + mIstrIdIstruttoriaCumulo + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}
}