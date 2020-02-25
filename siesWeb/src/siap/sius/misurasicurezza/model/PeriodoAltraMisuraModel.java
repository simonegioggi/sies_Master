package siap.sius.misurasicurezza.model;

/**
* <p>Title: PeriodoAltraMisuraModel</p>
* <p>Description: Classe Model che rappresenta il PeriodoAltraMisura</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class PeriodoAltraMisuraModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -7590757103246493189L;
	private BigDecimal mIdPeriodoAltraMisura;
	private Date mDataInizioEsecuzione;
	private Date mDataScadenza;
	private String mIstDetIdIstitutoDetenzione;
	private String mMotivazione;
	private BigDecimal mEveIdEvento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSiuIdFascicoloSius;
	private String mFlagMotivo;
	private String mDescrMotivo;
	private String mFlagValida;
	private String mCodTipoAutorita;
	private String mCodLuogoAutorita;
	private String mDescrTipoAutorita;
	private String mDescrLuogoAutorita;
	private BigDecimal mSospensioneGG;
	private BigDecimal mSospensioneMM;
	private BigDecimal mSospensioneAA;
	// 12-05-2008 Modifica Periodo da Recuperare in GG, MM, AA
	private String mDaRecuperare;
	private BigDecimal mDaRecuperareGG;
	private BigDecimal mDaRecuperareMM;
	private BigDecimal mDaRecuperareAA;
	// private BigDecimal mNumeroGiorni;
	private String mCodTipoUfficioSosp;
	private String mDescrTipoUfficio;

	// Usati per il calcolo della Misura Sicurezza Espiata a da Espiare
	private BigDecimal mEspiataGG;
	private BigDecimal mEspiataMM;
	private BigDecimal mEspiataAA;
	private BigDecimal mResiduaGG;
	private BigDecimal mResiduaMM;
	private BigDecimal mResiduaAA;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public PeriodoAltraMisuraModel() {
		this.mIdPeriodoAltraMisura = null;
		this.mDataInizioEsecuzione = null;
		this.mDataScadenza = null;
		this.mIstDetIdIstitutoDetenzione = "";
		this.mMotivazione = "";
		this.mEveIdEvento = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSiuIdFascicoloSius = null;
		this.mFlagMotivo = "";
		this.mDescrMotivo = "";
		this.mFlagValida = "";
		this.mCodTipoAutorita = "";
		this.mCodLuogoAutorita = "";
		this.mDescrTipoAutorita = "";
		this.mDescrLuogoAutorita = "";
		this.mSospensioneGG = null;
		this.mSospensioneMM = null;
		this.mSospensioneAA = null;
		this.mDaRecuperare = "";
		this.mDaRecuperareGG = null;
		this.mDaRecuperareMM = null;
		this.mDaRecuperareAA = null;
		// 12-05-2008 this.mNumeroGiorni = null;
		this.mCodTipoUfficioSosp = "";
		this.mDescrTipoUfficio = "";
		this.mEspiataGG = null;
		this.mEspiataMM = null;
		this.mEspiataAA = null;
		this.mResiduaGG = null;
		this.mResiduaMM = null;
		this.mResiduaAA = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public PeriodoAltraMisuraModel(PeriodoAltraMisuraModel aModel) {
		this.mIdPeriodoAltraMisura = aModel.mIdPeriodoAltraMisura;
		this.mDataInizioEsecuzione = aModel.mDataInizioEsecuzione;
		this.mDataScadenza = aModel.mDataScadenza;
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mMotivazione = aModel.mMotivazione;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mFlagMotivo = aModel.mFlagMotivo;
		this.mDescrMotivo = aModel.mDescrMotivo;
		this.mFlagValida = aModel.mFlagValida;
		this.mCodTipoAutorita = aModel.mCodTipoAutorita;
		this.mCodLuogoAutorita = aModel.mCodLuogoAutorita;
		this.mDescrTipoAutorita = aModel.mDescrTipoAutorita;
		this.mDescrLuogoAutorita = aModel.mDescrLuogoAutorita;
		this.mSospensioneGG = aModel.mSospensioneGG;
		this.mSospensioneMM = aModel.mSospensioneMM;
		this.mSospensioneAA = aModel.mSospensioneAA;
		this.mDaRecuperare = aModel.mDaRecuperare;
		this.mDaRecuperareGG = aModel.mDaRecuperareGG;
		this.mDaRecuperareMM = aModel.mDaRecuperareMM;
		this.mDaRecuperareAA = aModel.mDaRecuperareAA;
		// 12-05-2008 this.mNumeroGiorni = aModel.mNumeroGiorni;
		this.mCodTipoUfficioSosp = aModel.mCodTipoUfficioSosp;
		this.mDescrTipoUfficio = aModel.mDescrTipoUfficio;
		this.mEspiataGG = aModel.mEspiataGG;
		this.mEspiataMM = aModel.mEspiataMM;
		this.mEspiataAA = aModel.mEspiataAA;
		this.mResiduaGG = aModel.mResiduaGG;
		this.mResiduaMM = aModel.mResiduaMM;
		this.mResiduaAA = aModel.mResiduaAA;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public PeriodoAltraMisuraModel(BigDecimal aIdPeriodoAltraMisura, Date aDataInizioEsecuzione,
			Date aDataScadenza, String aIstDetIdIstitutoDetenzione, String aMotivazione,
			BigDecimal aEveIdEvento, BigDecimal aFasSieIdFascicoloSiep, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aFasSiuIdFascicoloSius, String aFlagMotivo,
			String aDescrMotivo, String aFlagValida, String aCodTipoAutorita, String aCodLuogoAutorita,
			String aDescrTipoAutorita, String aDescrLuogoAutorita, BigDecimal aSospensioneGG,
			BigDecimal aSospensioneMM, BigDecimal aSospensioneAA, String aDaRecuperare,
			BigDecimal aDaRecuperareGG, BigDecimal aDaRecuperareMM, BigDecimal aDaRecuperareAA,
			// 12/05/2008 BigDecimal aNumeroGiorni,
			String aCodTipoUfficioSosp, String aDescrTipoUfficio, BigDecimal aEspiataGG,
			BigDecimal aEspiataMM, BigDecimal aEspiataAA, BigDecimal aResiduaGG, BigDecimal aResiduaMM,
			BigDecimal aResiduaAA) {
		this.mIdPeriodoAltraMisura = aIdPeriodoAltraMisura;
		this.mDataInizioEsecuzione = aDataInizioEsecuzione;
		this.mDataScadenza = aDataScadenza;
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mMotivazione = aMotivazione;
		this.mEveIdEvento = aEveIdEvento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mFlagMotivo = aFlagMotivo;
		this.mDescrMotivo = aDescrMotivo;
		this.mFlagValida = aFlagValida;
		this.mCodTipoAutorita = aCodTipoAutorita;
		this.mCodLuogoAutorita = aCodLuogoAutorita;
		this.mDescrTipoAutorita = aDescrTipoAutorita;
		this.mDescrLuogoAutorita = aDescrLuogoAutorita;
		this.mSospensioneGG = aSospensioneGG;
		this.mSospensioneMM = aSospensioneMM;
		this.mSospensioneAA = aSospensioneAA;
		this.mDaRecuperare = aDaRecuperare;
		this.mDaRecuperareGG = aDaRecuperareGG;
		this.mDaRecuperareMM = aDaRecuperareMM;
		this.mDaRecuperareAA = aDaRecuperareAA;
		// 12/05/2008 this.mNumeroGiorni = aNumeroGiorni;
		this.mCodTipoUfficioSosp = aCodTipoUfficioSosp;
		this.mDescrTipoUfficio = aDescrTipoUfficio;
		this.mEspiataGG = aEspiataGG;
		this.mEspiataMM = aEspiataMM;
		this.mEspiataAA = aEspiataAA;
		this.mResiduaGG = aResiduaGG;
		this.mResiduaMM = aResiduaMM;
		this.mResiduaAA = aResiduaAA;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdPeriodoAltraMisura() {
		return mIdPeriodoAltraMisura;
	}

	public Date getDataInizioEsecuzione() {
		return mDataInizioEsecuzione;
	}

	public Date getDataScadenza() {
		return mDataScadenza;
	}

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public String getMotivazione() {
		return mMotivazione;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
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

	public String getFlagMotivo() {
		return mFlagMotivo;
	}

	public String getDescrMotivo() {
		return mDescrMotivo;
	}

	public String getFlagValida() {
		return mFlagValida;
	}

	public String getCodTipoAutorita() {
		return mCodTipoAutorita;
	}

	public String getCodLuogoAutorita() {
		return mCodLuogoAutorita;
	}

	public String getDescrTipoAutorita() {
		return mDescrTipoAutorita;
	}

	public String getDescrLuogoAutorita() {
		return mDescrLuogoAutorita;
	}

	public BigDecimal getSospensioneGG() {
		return mSospensioneGG;
	};

	public BigDecimal getSospensioneMM() {
		return mSospensioneMM;
	};

	public BigDecimal getSospensioneAA() {
		return mSospensioneAA;
	};

	public String getDaRecuperare() {
		return mDaRecuperare;
	};

	public BigDecimal getDaRecuperareGG() {
		return mDaRecuperareGG;
	};

	public BigDecimal getDaRecuperareMM() {
		return mDaRecuperareMM;
	};

	public BigDecimal getDaRecuperareAA() {
		return mDaRecuperareAA;
	};

	// 12/05/2008 public BigDecimal getNumeroGiorni() { return mNumeroGiorni; } ;
	public String getCodTipoUfficioSosp() {
		return mCodTipoUfficioSosp;
	};

	public String getDescrTipoUfficio() {
		return mDescrTipoUfficio;
	};

	public BigDecimal getEspiataGG() {
		return mEspiataGG;
	};

	public BigDecimal getEspiataMM() {
		return mEspiataMM;
	};

	public BigDecimal getEspiataAA() {
		return mEspiataAA;
	};

	public BigDecimal getResiduaGG() {
		return mResiduaGG;
	};

	public BigDecimal getResiduaMM() {
		return mResiduaMM;
	};

	public BigDecimal getResiduaAA() {
		return mResiduaAA;
	};

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdPeriodoAltraMisura(BigDecimal aValore) {
		mIdPeriodoAltraMisura = aValore;
	}

	public void setDataInizioEsecuzione(Date aValore) {
		mDataInizioEsecuzione = aValore;
	}

	public void setDataScadenza(Date aValore) {
		mDataScadenza = aValore;
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setMotivazione(String aValore) {
		mMotivazione = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
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

	public void setFlagMotivo(String aValore) {
		mFlagMotivo = aValore;
	}

	public void setDescrMotivo(String aValore) {
		mDescrMotivo = aValore;
	}

	public void setFlagValida(String aValore) {
		mFlagValida = aValore;
	}

	public void setCodTipoAutorita(String aValore) {
		mCodTipoAutorita = aValore;
	}

	public void setCodLuogoAutorita(String aValore) {
		mCodLuogoAutorita = aValore;
	}

	public void setDescrTipoAutorita(String aValore) {
		mDescrTipoAutorita = aValore;
	}

	public void setDescrLuogoAutorita(String aValore) {
		mDescrLuogoAutorita = aValore;
	}

	public void setSospensioneGG(BigDecimal aValore) {
		mSospensioneGG = aValore;
	}

	public void setSospensioneMM(BigDecimal aValore) {
		mSospensioneMM = aValore;
	}

	public void setSospensioneAA(BigDecimal aValore) {
		mSospensioneAA = aValore;
	}

	public void setDaRecuperare(String aValore) {
		mDaRecuperare = aValore;
	}

	public void setDaRecuperareGG(BigDecimal aValore) {
		mDaRecuperareGG = aValore;
	}

	public void setDaRecuperareMM(BigDecimal aValore) {
		mDaRecuperareMM = aValore;
	}

	public void setDaRecuperareAA(BigDecimal aValore) {
		mDaRecuperareAA = aValore;
	}

	// 12/05/2008 public void setNumeroGiorni (BigDecimal aValore ) { mNumeroGiorni = aValore; }
	public void setCodTipoUfficioSosp(String aValore) {
		mCodTipoUfficioSosp = aValore;
	}

	public void setDescrTipoUfficio(String aValore) {
		mDescrTipoUfficio = aValore;
	}

	public void setEspiataGG(BigDecimal aValore) {
		mEspiataGG = aValore;
	}

	public void setEspiataMM(BigDecimal aValore) {
		mEspiataMM = aValore;
	}

	public void setEspiataAA(BigDecimal aValore) {
		mEspiataAA = aValore;
	}

	public void setResiduaGG(BigDecimal aValore) {
		mResiduaGG = aValore;
	}

	public void setResiduaMM(BigDecimal aValore) {
		mResiduaMM = aValore;
	}

	public void setResiduaAA(BigDecimal aValore) {
		mResiduaAA = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "PeriodoAltraMisuraModel:\n" + "[ mIdPeriodoAltraMisura     = " + mIdPeriodoAltraMisura
				+ " ]\n" + "[ mDataInizioEsecuzione       = " + mDataInizioEsecuzione + " ]\n"
				+ "[ mDataScadenza               = " + mDataScadenza + " ]\n"
				+ "[ mIstDetIdIstitutoDetenzione = " + mIstDetIdIstitutoDetenzione + " ]\n"
				+ "[ mMotivazione                = " + mMotivazione + " ]\n"
				+ "[ mEveIdEvento                = " + mEveIdEvento + " ]\n"
				+ "[ mFasSieIdFascicoloSiep      = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mCodOperatoreInserimento    = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento            = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento      = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento  = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento          = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento    = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mFasSiuIdFascicoloSius      = " + mFasSiuIdFascicoloSius + " ]\n"
				+ "[ mFlagMotivo    			  = " + mFlagMotivo + " ]\n"
				+ "[ mDescrMotivo    	  		  = " + mDescrMotivo + " ]"
				+ "[ mFlagValida    			  = " + mFlagValida + " ]"
				+ "[ mCodTipoAutorita    		  = " + mCodTipoAutorita + " ]"
				+ "[ mCodLuogoAutorita    		  = " + mCodLuogoAutorita + " ]"
				+ "[ mDescrTipoAutorita    	  = " + mDescrTipoAutorita + " ]"
				+ "[ mDescrLuogoAutorita    	  = " + mDescrLuogoAutorita + " ]\n"
				+ "[ mSospensioneGG    	  	  = " + mSospensioneGG + " ]\n" + "[ mSospensioneMM    	      = "
				+ mSospensioneMM + "  ]\n" + "[ mSospensioneAA    	      = " + mSospensioneAA + "  ]\n"
				+ "[ mDaRecuperare    	          = " + mDaRecuperare + "  ]\n"
				+ "[ mDaRecuperareGG    	          = " + mDaRecuperareGG + "  ]\n"
				+ "[ mDaRecuperareMM    	          = " + mDaRecuperareMM + "  ]\n"
				+ "[ mDaRecuperareAA    	          = " + mDaRecuperareAA + "  ]\n" +
				// 12/05/2008 "[ mNumeroGiorni = "+mNumeroGiorni+" ]\n"+
				"[ mCodTipoUfficioSosp    	  = " + mCodTipoUfficioSosp + " ]\n"
				+ "[ mDescrTipoUfficio    	  	  = " + mDescrTipoUfficio + " ]"
				+ "[ mEspiataGG    	  	  	  = " + mEspiataGG + " ]\n" + "[ mEspiataMM    	      	  = "
				+ mEspiataMM + "  ]\n" + "[ mEspiataAA    	      	  = " + mEspiataAA + "  ]\n"
				+ "[ mResiduaGG    	  	  	  = " + mResiduaGG + " ]\n" + "[ mResiduaMM    	      	  = "
				+ mResiduaMM + "  ]\n" + "[ mResiduaAA    	      	  = " + mResiduaAA + "  ]\n";
		return lStr;
	}
}
