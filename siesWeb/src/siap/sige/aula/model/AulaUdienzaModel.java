package siap.sige.aula.model;

/**
* <p>Title: AulaUdienzaModel</p>
* <p>Description: Classe Model che rappresenta l'Aula Udienza</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
import java.math.BigDecimal;
import java.util.Date;

import siap.sige.sezione.model.SezioneModel;
import f3b.model.GenericModel;

public class AulaUdienzaModel extends GenericModel
{
	
	private static final long serialVersionUID = -2839962220268331264L;
	private BigDecimal  mIdAula;
	private BigDecimal  mIdSezione;
	private String	  	mDescrizioneAula;
	private String	  	mDescrizioneStanza;
	private String	  	mDescrizioneIngresso;
	// 20170908: è un varchar nel db
//	private BigDecimal  mNumeroPiano;
	private String  	mNumeroPiano;
	private String      mFlagPredefinita;
	private String 	  	codOperatoreInserimento;  
	private Date 		dataInserimento;  
	private String 	  	codUfficioInserimento;
	private String 	  	codOperatoreAggiornamento;
	private Date 		dataAggiornamento;
	private String 	  	codUfficioAggiornamento;  
  
	private SezioneModel mSezione;

	//COSTRUTTORE DI DEFAULT
	public AulaUdienzaModel ()
	{
		this.mIdAula = null;
		this.mIdSezione = null;
		this.mDescrizioneAula = "";
		this.mDescrizioneStanza = "";
		this.mDescrizioneIngresso = "";
		this.mNumeroPiano = "";
		this.mFlagPredefinita = "";
		this.codOperatoreInserimento = "";  
		this.dataInserimento = null;  
		this.codUfficioInserimento = "";
		this.codOperatoreAggiornamento = "";
		this.dataAggiornamento = null;
		this.codUfficioAggiornamento = "";  
	}

	//COSTRUTTORE DI COPIA
	public AulaUdienzaModel ( AulaUdienzaModel aModel )
	{
		this.mIdAula = aModel.mIdAula;
		this.mIdSezione = aModel.mIdSezione;
		this.mDescrizioneAula = aModel.mDescrizioneAula;
		this.mDescrizioneStanza = aModel.mDescrizioneStanza;
		this.mDescrizioneIngresso = aModel.mDescrizioneIngresso;
		this.mNumeroPiano = aModel.mNumeroPiano;
    	this.mFlagPredefinita = aModel.mFlagPredefinita;
    	this.codOperatoreInserimento = aModel.codOperatoreInserimento;  
    	this.dataInserimento = aModel.dataInserimento;  
    	this.codUfficioInserimento = aModel.codUfficioInserimento;
    	this.codOperatoreAggiornamento = aModel.codOperatoreAggiornamento;
    	this.dataAggiornamento = aModel.dataAggiornamento;
    	this.codUfficioAggiornamento = aModel.codUfficioAggiornamento;  
    
    	this.mSezione = aModel.mSezione;
	}

	//COSTRUTTORE MODEL
  	public AulaUdienzaModel ( BigDecimal aIdAula,
		  					  BigDecimal aIdSezione,
		  					  String     aDescrizioneAula,
		  					  String     aDescrizioneStanza,
		  					  String     aDescrizioneIngresso,
		  					  // 20170908: è un varchar nel db
		  					  String aNumeroPiano,
//		  					  BigDecimal aNumeroPiano,
		  					  String     aFlagPredefinita, 
							  String     aCodOperatoreInserimento,  
							  Date       aDataInserimento,  
							  String     aCodUfficioInserimento,
							  String 	 aCodOperatoreAggiornamento,
							  Date 	     aDataAggiornamento,
							  String 	 aCodUfficioAggiornamento )
  	{
	    this.mIdAula = aIdAula;
	    this.mIdSezione = aIdSezione;
	    this.mDescrizioneAula = aDescrizioneAula;
	    this.mDescrizioneStanza = aDescrizioneStanza;
	    this.mDescrizioneIngresso = aDescrizioneIngresso;
	    this.mNumeroPiano = aNumeroPiano;
	    this.mFlagPredefinita = aFlagPredefinita;
		this.codOperatoreInserimento = aCodOperatoreInserimento;  
		this.dataInserimento = aDataInserimento;  
		this.codUfficioInserimento = aCodUfficioInserimento;
		this.codOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.dataAggiornamento = aDataAggiornamento;
		this.codUfficioAggiornamento = aCodUfficioAggiornamento;  
  	}

  	//
  	// METODI GET()
  	//
  	public BigDecimal getIdAula() 	         		 { return mIdAula; }
  	public BigDecimal getIdSezione()          	     { return mIdSezione; }
  	public String 	  getDescrizioneAula() 	 	     { return mDescrizioneAula; }
  	public String 	  getDescrizioneStanza() 	 	 { return mDescrizioneStanza; }
  	public String 	  getDescrizioneIngresso() 		 { return mDescrizioneIngresso; }
  	// 20170908: è un varchar nel db
  	public String 	getNumeroPiano() 	     		 { return mNumeroPiano; }
//  	public BigDecimal getNumeroPiano() 	     		 { return mNumeroPiano; }
  	public String 	  getFlagPredefinita() 	 		 { return mFlagPredefinita; }
  	public String 	  getCodOperatoreInserimento()   { return codOperatoreInserimento; }  
	public Date 	  getDataInserimento()           { return dataInserimento; }
	public String 	  getCodUfficioInserimento()     { return codUfficioInserimento; }
	public String 	  getCodOperatoreAggiornamento() { return codOperatoreAggiornamento; }
	public Date 	  getDataAggiornamento()         { return dataAggiornamento; }
	public String 	  getCodUfficioAggiornamento()   { return codUfficioAggiornamento; } 

	public SezioneModel getSezione()                 { return mSezione; }
  
	//
	// METODI SET()
	//
	public void setIdAula(BigDecimal aValore) 	  	 		  { mIdAula = aValore; }
  	public void setIdSezione(BigDecimal aValore)       		  { mIdSezione = aValore; }
  	public void setDescrizioneAula(String aValore) 	 		  { mDescrizioneAula = aValore; }
  	public void setDescrizioneStanza(String aValore)   		  { mDescrizioneStanza = aValore; }
  	public void setDescrizioneIngresso(String aValore) 		  { mDescrizioneIngresso = aValore; }
  	// 20170908: è un varchar nel db
//  	public void setNumeroPiano(BigDecimal aValore) 	 		  { mNumeroPiano = aValore; }
  	public void setNumeroPiano(String aValore) 	 		 	  { mNumeroPiano = aValore; }
  	public void setFlagPredefinita(String aValore) 	 		  { mFlagPredefinita = aValore; }
	public void setCodOperatoreInserimento(String aValore)    { codOperatoreInserimento = aValore; }  
	public void setDataInserimento(Date aValore)              { dataInserimento = aValore; }
	public void setCodUfficioInserimento(String aValore)      { codUfficioInserimento = aValore; }
	public void setCodOperatoreAggiornamento(String aValore)  { codOperatoreAggiornamento = aValore; }
	public void setDataAggiornamento(Date aValore)            { dataAggiornamento = aValore; }
	public void setCodUfficioAggiornamento(String aValore)    { codUfficioAggiornamento = aValore; } 
  
	public void setSezione(SezioneModel aValore)              { mSezione = aValore; }
	
	public String getDescrAulaPredefinita() { 
		String descAulaPred = "";
		if(mFlagPredefinita != null && mFlagPredefinita.equals("S")){
			descAulaPred = "Si";
		} else {
			descAulaPred = "No";
		}
		
		return descAulaPred; 
	}
  
}