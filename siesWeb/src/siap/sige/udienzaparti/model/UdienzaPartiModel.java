package siap.sige.udienzaparti.model;

/**
* <p>Title: UdienzaPartiModel</p>
* <p>Description: Classe Model che rappresenta l'associazione tra l'udienza e le parti (Offese, Civili) coinvolte</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class UdienzaPartiModel extends GenericModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -989389229478701510L;
	private BigDecimal  mIdSoggetto;
	private BigDecimal  mIdUdienzaProcedimentoSige;
    private String	  	mCodOperatoreInserimento;
	private Date 		mDataInserimento;
	private String	  	mCodUfficioInserimento;
	private String	  	mCodOperatoreAggiornamento;
	private Date 		mDataAggiornamento;
	private String	  	mCodUfficioAggiornamento;
	
	//COSTRUTTORE DI DEFAULT
	public UdienzaPartiModel ()
	{

		this.mIdSoggetto = null;
		this.mIdUdienzaProcedimentoSige = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
	}

	//COSTRUTTORE DI COPIA
	public UdienzaPartiModel ( UdienzaPartiModel aModel )
	{
		this.mIdSoggetto = aModel.mIdSoggetto;
		this.mIdUdienzaProcedimentoSige = aModel.mIdUdienzaProcedimentoSige;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	//COSTRUTTORE MODEL
  	public UdienzaPartiModel ( 
				  			BigDecimal  aIdSoggetto,
				  			BigDecimal  aIdUdienzaProcedimentoSige,
				  			String	  	aCodOperatoreInserimento,
				  			Date 		aDataInserimento,
				  			String	  	aCodUfficioInserimento,
				  			String	  	aCodOperatoreAggiornamento,
				  			Date 		aDataAggiornamento,
				  			String	  	aCodUfficioAggiornamento )
  	{
		this.mIdSoggetto = aIdSoggetto;
		this.mIdUdienzaProcedimentoSige = aIdUdienzaProcedimentoSige;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
  	}

  	//
  	// METODI GET()
  	//
  	public BigDecimal getIdSoggetto() 			      { return mIdSoggetto; }
  	public BigDecimal getIdUdienzaProcedimentoSige()  { return mIdUdienzaProcedimentoSige; }
	public String 	  getCodOperatoreInserimento()    { return mCodOperatoreInserimento; }
	public Date 	  getDataInserimento()		      { return mDataInserimento; }
	public String 	  getCodUfficioInserimento() 	  { return mCodUfficioInserimento; }
	public String 	  getCodOperatoreAggiornamento()  { return mCodOperatoreAggiornamento; }
	public Date 	  getDataAggiornamento()       	  { return mDataAggiornamento; }
	public String 	  getCodUfficioAggiornamento()    { return mCodUfficioAggiornamento; }
  
	//
	// METODI SET()
	//
  	public void   setIdSoggetto(BigDecimal aValore) 			    { mIdSoggetto = aValore; }
  	public void   setIdUdienzaProcedimentoSige(BigDecimal aValore)  { mIdUdienzaProcedimentoSige = aValore; }
	public void   setCodOperatoreInserimento(String aValore)        { mCodOperatoreInserimento = aValore; }
	public void   setDataInserimento(Date aValore)		   	    	{ mDataInserimento = aValore; }
	public void   setCodUfficioInserimento(String aValore) 	    	{ mCodUfficioInserimento = aValore; }
	public void   setCodOperatoreAggiornamento(String aValore)  	{ mCodOperatoreAggiornamento = aValore; }
	public void   setDataAggiornamento(Date aValore)            	{ mDataAggiornamento = aValore; }
	public void   setCodUfficioAggiornamento(String aValore)    	{ mCodUfficioAggiornamento = aValore; }

}