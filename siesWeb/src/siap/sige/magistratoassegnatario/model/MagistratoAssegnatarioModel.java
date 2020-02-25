package siap.sige.magistratoassegnatario.model;

/**
* <p>Title: MagistratoAssegnatarioModel</p>
* <p>Description: Classe Model che rappresenta
* il Magistrato Assegnatario del Fascicolo SIGE.</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.magistrato.model.MagistratoModel;

public class MagistratoAssegnatarioModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4043059438823443481L;
	private Date mDataInizio;
	private Date mDataFine;
	private String mCodRuoloMagistrato;
	private String mDescrRuoloMagistrato;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mMagCodMagistrato;
	private BigDecimal mFasSigeIdFascicoloSige;
	private MagistratoModel mMagistrato;

	// intervento per nuova gestione udienze monocratiche/collegiali
	private 	String	mCodProcuratore;
	private 	BigDecimal	mIdAssistente;
	private 	String	mFlagModifBlocco;
	// fine intervento per nuova gestione udienze monocratiche/collegiali

	// COSTRUTTORE DI DEFAULT
	public MagistratoAssegnatarioModel() {
		mDataInizio = null;
		mDataFine = null;
		mCodRuoloMagistrato = "";
		mDescrRuoloMagistrato = "";
		mCodOperatoreInserimento = "";
		mDataInserimento = null;
		mCodUfficioInserimento = "";
		mDescrUfficioInserimento = "";
		mCodOperatoreAggiornamento = "";
		mDataAggiornamento = null;
		mCodUfficioAggiornamento = "";
		mDescrUfficioAggiornamento = "";
		mMagCodMagistrato = "";
		mFasSigeIdFascicoloSige = null;
		mMagistrato = null;
			 mCodProcuratore = "";
			 mIdAssistente = null;
			 mFlagModifBlocco = "";
	}

	// COSTRUTTORE DI COPIA
	public MagistratoAssegnatarioModel(MagistratoAssegnatarioModel aModel) {
		mDataInizio = aModel.mDataInizio;
		mDataFine = aModel.mDataFine;
		mCodRuoloMagistrato = aModel.mCodRuoloMagistrato;
		mDescrRuoloMagistrato = aModel.mDescrRuoloMagistrato;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		mMagCodMagistrato = aModel.mMagCodMagistrato;
		mFasSigeIdFascicoloSige = aModel.mFasSigeIdFascicoloSige;
		mMagistrato = aModel.mMagistrato;
			 // intervento per nuova gestione udienze monocratiche/collegiali
			mCodProcuratore = aModel.getCodProcuratore();
			mIdAssistente = aModel.getIdAssistente();
			mFlagModifBlocco = aModel.getFlagModifBlocco();
	}

	// COSTRUTTORE MODEL
	public MagistratoAssegnatarioModel(Date aDataInizio, Date aDataFine, String aCodRuoloMagistrato,
			String aDescrRuoloMagistrato, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			String aMagCodMagistrato, BigDecimal aFasSigeIdFascicoloSige,
				   String	 aCodProcuratore,
				   BigDecimal	 aIdAssistente,
				   String	 aFlagModifBlocco) {
		mDataInizio = aDataInizio;
		mDataFine = aDataFine;
		mCodRuoloMagistrato = aCodRuoloMagistrato;
		mDescrRuoloMagistrato = aDescrRuoloMagistrato;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mDataInserimento = aDataInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		mMagCodMagistrato = aMagCodMagistrato;
		mFasSigeIdFascicoloSige = aFasSigeIdFascicoloSige;
				 mCodProcuratore = aCodProcuratore;
				 // intervento per nuova gestione udienze monocratiche/collegiali
				 mIdAssistente = aIdAssistente;
		mMagistrato = null;
				 mFlagModifBlocco= aFlagModifBlocco;
	}

	//
	// METODI GET()
	//

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public String getCodRuoloMagistrato() {
		return mCodRuoloMagistrato;
	}

	public String getDescrRuoloMagistrato() {
		return mDescrRuoloMagistrato;
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

	public String getMagCodMagistrato() {
		return mMagCodMagistrato;
	}

	public BigDecimal getFasSigeIdFascicoloSige() {
		return mFasSigeIdFascicoloSige;
	}
		 // intervento per nuova gestione udienze monocratiche/collegiali
		 public String 			getCodProcuratore()			{ return mCodProcuratore; } 
		 public BigDecimal 	 	getIdAssistente()	{ return mIdAssistente; } 
		 public String 			getFlagModifBlocco()			{ return mFlagModifBlocco; } 

	public MagistratoModel getMagistrato() {
		return mMagistrato;
	}

	//
	// METODI SET()
	//

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setCodRuoloMagistrato(String aValore) {
		mCodRuoloMagistrato = aValore;
	}

	public void setDescrRuoloMagistrato(String aValore) {
		mDescrRuoloMagistrato = aValore;
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

	public void setMagCodMagistrato(String aValore) {
		mMagCodMagistrato = aValore;
	}

	public void setFasSigeIdFascicoloSige(BigDecimal aValore) {
		mFasSigeIdFascicoloSige = aValore;
	}

	public void setMagistrato(MagistratoModel aValore) {
		mMagistrato = aValore;
	}
		// intervento per nuova gestione udienze monocratiche/collegiali
		 public void  	 setCodProcuratore(String aValore ) 			{ mCodProcuratore = aValore; } 
		 public void  	 setIdAssistente(BigDecimal aValore ) { mIdAssistente= aValore; } 
		 public void  	 setFlagModifBlocco(String aValore ) 			{ mFlagModifBlocco = aValore; } 


}
