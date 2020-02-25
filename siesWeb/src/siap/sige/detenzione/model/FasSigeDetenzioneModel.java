package siap.sige.detenzione.model;

/**
* <p>Title: FasSigeDetenzioneModel</p>
* <p>Description: Classe Model che rappresenta il FasSigeDetenzione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import f3b.model.GenericModel;

public class FasSigeDetenzioneModel extends GenericModel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5333719691981138092L;
	private 	BigDecimal	mIdFasSigeDetenzione;
	private 	BigDecimal	mFasIdFasSige;
	private 	BigDecimal	mLdIdLuogoDetenzione;
	private 	Date	mDataInserimento;
	private 	String	mCodOperatoreInserimento;
	private 	String	mCodUfficioInserimento;
	private 	String	mDescrUfficioInserimento;
	private 	Date	mDataAggiornamento;
	private 	String	mCodOperatoreAggiornamento;
	private 	String	mCodUfficioAggiornamento;
	private 	String	mDescrUfficioAggiornamento;
	private 	BigDecimal	mAcIdAltraCausa;
	private     String mDescrLuogoDetenzione;
	
	private LuogoDetenzioneModel mLuogoDetenzione;
	private AltraCausaModel	mAltraCausa;


	//COSTRUTTORE DI DEFAULT 
	public FasSigeDetenzioneModel ()
	{
		mIdFasSigeDetenzione = null;
		mFasIdFasSige = null;
		mLdIdLuogoDetenzione = null;
		mDataInserimento = null;
		mCodOperatoreInserimento = "";
		mCodUfficioInserimento = "";
		mDescrUfficioInserimento = "";
		mDataAggiornamento = null;
		mCodOperatoreAggiornamento = "";
		mCodUfficioAggiornamento = "";
		mDescrUfficioAggiornamento = "";
		mAcIdAltraCausa = null;
		mLuogoDetenzione = null;
		mAltraCausa = null;
		
	}


	//COSTRUTTORE DI COPIA 
	public FasSigeDetenzioneModel ( FasSigeDetenzioneModel aModel )
	{			 
		mIdFasSigeDetenzione = aModel.mIdFasSigeDetenzione;
		mFasIdFasSige = aModel.mFasIdFasSige;
		mLdIdLuogoDetenzione = aModel.mLdIdLuogoDetenzione;
		mDataInserimento = aModel.mDataInserimento;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		mAcIdAltraCausa = aModel.mAcIdAltraCausa;
		
		mLuogoDetenzione = aModel.mLuogoDetenzione;
		mAltraCausa = aModel.mAltraCausa;
		
	}
	public FasSigeDetenzioneModel ( FasSigeDetenzioneModel aFasSige, LuogoDetenzioneModel aLuogo, AltraCausaModel aAltraCausa )
	{			 
		this(aFasSige);
		mLuogoDetenzione = new LuogoDetenzioneModel(aLuogo);
		mAltraCausa = new AltraCausaModel(aAltraCausa);
	}

	//COSTRUTTORE MODEL 
	public FasSigeDetenzioneModel (
				   BigDecimal	 aIdFasSigeDetenzione,
				   BigDecimal	 aFasIdFasSige,
				   BigDecimal	 aLdIdLuogoDetenzione,
				   Date	 aDataInserimento,
				   String	 aCodOperatoreInserimento,
				   String	 aCodUfficioInserimento,
				   String	 aDescrUfficioInserimento,
				   Date	 aDataAggiornamento,
				   String	 aCodOperatoreAggiornamento,
				   String	 aCodUfficioAggiornamento,
				   String	 aDescrUfficioAggiornamento,
				   BigDecimal	 aAcIdAltraCausa)
			{
				 mIdFasSigeDetenzione = aIdFasSigeDetenzione;
				 mFasIdFasSige = aFasIdFasSige;
				 mLdIdLuogoDetenzione = aLdIdLuogoDetenzione;
				 mDataInserimento = aDataInserimento;
				 mCodOperatoreInserimento = aCodOperatoreInserimento;
				 mCodUfficioInserimento = aCodUfficioInserimento;
				 mDescrUfficioInserimento = aDescrUfficioInserimento;
				 mDataAggiornamento = aDataAggiornamento;
				 mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
				 mCodUfficioAggiornamento = aCodUfficioAggiornamento;
				 mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
				 mAcIdAltraCausa = aAcIdAltraCausa;
			}

	//
	// METODI GET()
	//

	public BigDecimal 	getIdFasSigeDetenzione() 		{ return mIdFasSigeDetenzione; } 
	public BigDecimal 	getFasIdFasSige() 				{ return mFasIdFasSige; } 
	public BigDecimal 	getLdIdLuogoDetenzione() 		{ return mLdIdLuogoDetenzione; } 
	public Date 		getDataInserimento() 			{ return mDataInserimento; } 
	public String 		getCodOperatoreInserimento() 	{ return mCodOperatoreInserimento; } 
	public String 		getCodUfficioInserimento() 		{ return mCodUfficioInserimento; } 
	public String 		getDescrUfficioInserimento() 	{ return mDescrUfficioInserimento; } 
	public Date 		getDataAggiornamento() 			{ return mDataAggiornamento; } 
	public String 		getCodOperatoreAggiornamento() 	{ return mCodOperatoreAggiornamento; } 
	public String 		getCodUfficioAggiornamento() 	{ return mCodUfficioAggiornamento; } 
	public String 		getDescrUfficioAggiornamento() 	{ return mDescrUfficioAggiornamento; } 
	public BigDecimal 	getAcIdAltraCausa() 			{ return mAcIdAltraCausa; } 
	
	public LuogoDetenzioneModel getLuogoDetenzione() { return mLuogoDetenzione; } 
	public AltraCausaModel getAltraCausa() { return mAltraCausa; } 
	

	//
	// METODI SET()
	//

	public void  	 setIdFasSigeDetenzione(BigDecimal aValore ) 			{ mIdFasSigeDetenzione = aValore; } 
	public void  	 setFasIdFasSige(BigDecimal aValore ) 			 		{ mFasIdFasSige = aValore; } 
	public void  	 setLdIdLuogoDetenzione(BigDecimal aValore ) 			{ mLdIdLuogoDetenzione = aValore; } 
	public void  	 setDataInserimento(Date aValore ) 			 			{ mDataInserimento = aValore; } 
	public void  	 setCodOperatoreInserimento(String aValore ) 			{ mCodOperatoreInserimento = aValore; } 
	public void  	 setCodUfficioInserimento(String aValore ) 			 	{ mCodUfficioInserimento = aValore; } 
	public void  	 setDescrUfficioInserimento(String aValore ) 			{ mDescrUfficioInserimento = aValore; } 
	public void  	 setDataAggiornamento(Date aValore ) 			 		{ mDataAggiornamento = aValore; } 
	public void  	 setCodOperatoreAggiornamento(String aValore ) 			{ mCodOperatoreAggiornamento = aValore; } 
	public void  	 setCodUfficioAggiornamento(String aValore ) 			{ mCodUfficioAggiornamento = aValore; } 
	public void  	 setDescrUfficioAggiornamento(String aValore ) 			{ mDescrUfficioAggiornamento = aValore; } 
	public void  	 setAcIdAltraCausa(BigDecimal aValore ) 			 	{ mAcIdAltraCausa = aValore; } 

	public void  	 setLuogoDetenzione(LuogoDetenzioneModel aValore ) 		{ mLuogoDetenzione = aValore; } 
	public void  	 setAltraCausa(AltraCausaModel aValore ) 			 	{ mAltraCausa = aValore; }


	public String getDescrLuogoDetenzione() {
	    // Dettaglio del luogo detenzione.
		if ( (this.getLuogoDetenzione()!= null) && (this.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()!= null) ) {
		    this.mDescrLuogoDetenzione = getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto()+" - "+getLuogoDetenzione().getIstitutoDetenzione().getDescrComune();
		} else if ( (getAltraCausa()!= null) &&   getAltraCausa().getIstDetIdIstitutoDetenzione()!=null){
			this.mDescrLuogoDetenzione = getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto()+" - "+getAltraCausa().getIstitutoDetenzione().getDescrComune();
		}
        return this.mDescrLuogoDetenzione;		
	}


	public void setDescrLuogoDetenzione(String mDescrLuogoDetenzione) {
		this.mDescrLuogoDetenzione = mDescrLuogoDetenzione;
	} 

}
