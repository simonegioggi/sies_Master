package siap.sige.detenzione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: FasSigeDetenzioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella FasSigeDetenzione</p>
* <p> La tabella memorizza la relazione tra Fascicolo SIGE ed il luogo detenzione 
* che può essere memorizzato nella tabella LUOGO_DETENZIONE o in ALTRA_CAUSA.
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class FasSigeDetenzioneDAO extends TableDAO 
{
	public FasSigeDetenzioneDAO (Connection con) 
	{
		super(con);
		setTable("FAS_SIGE_DETENZIONE");

		//La Sequence ed il campo chiave
		setSequenceField("ID_FAS_SIGE_DETENZIONE", "FAS_SIGE_DET_SEQ");
		setFieldKey("ID_FAS_SIGE_DETENZIONE", BIG_DECIMAL);
		
		// I campi della tabella
		setField("ID_FAS_SIGE_DETENZIONE", BIG_DECIMAL);
		setField("FAS_ID_FAS_SIGE", BIG_DECIMAL);
		setField("LD_ID_LUOGO_DETENZIONE", BIG_DECIMAL);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("AC_ID_ALTRA_CAUSA", BIG_DECIMAL);
	}


	//
	// METODI GET()
	//

	public BigDecimal 		getIdFasSigeDetenzione() 		throws DAOException	 { return getBigDecimal("ID_FAS_SIGE_DETENZIONE"); } 
	public BigDecimal 		getFasIdFasSige() 				throws DAOException	 { return getBigDecimal("FAS_ID_FAS_SIGE"); } 
	public BigDecimal 		getLdIdLuogoDetenzione() 		throws DAOException	 { return getBigDecimal("LD_ID_LUOGO_DETENZIONE"); } 
	public Date 			getDataInserimento() 			throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 
	public String 			getCodOperatoreInserimento() 	throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
	public String 			getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
	public Date 			getDataAggiornamento() 			throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); } 
	public String 			getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); } 
	public String 			getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); } 
	public BigDecimal 		getAcIdAltraCausa() 			throws DAOException	 { return getBigDecimal("AC_ID_ALTRA_CAUSA"); } 


	//
	// METODI SET()
	//

	public void  	 setIdFasSigeDetenzione(BigDecimal aValore ) 			{ setBigDecimal("ID_FAS_SIGE_DETENZIONE", aValore); } 
	public void  	 setFasIdFasSige(BigDecimal aValore ) 			 		{ setBigDecimal("FAS_ID_FAS_SIGE", aValore); } 
	public void  	 setLdIdLuogoDetenzione(BigDecimal aValore ) 			{ setBigDecimal("LD_ID_LUOGO_DETENZIONE", aValore); } 
	public void  	 setDataInserimento(Date aValore ) 			 			{ setDate("DATA_INSERIMENTO", aValore); } 
	public void  	 setCodOperatoreInserimento(String aValore ) 			{ setString("COD_OPERATORE_INSERIMENTO", aValore); } 
	public void  	 setCodUfficioInserimento(String aValore ) 			 	{ setString("COD_UFFICIO_INSERIMENTO", aValore); } 
	public void  	 setDataAggiornamento(Date aValore ) 			 		{ setDate("DATA_AGGIORNAMENTO", aValore); } 
	public void  	 setCodOperatoreAggiornamento(String aValore ) 			{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
	public void  	 setCodUfficioAggiornamento(String aValore ) 			{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); } 
	public void  	 setAcIdAltraCausa(BigDecimal aValore ) 			 	{ setBigDecimal("AC_ID_ALTRA_CAUSA", aValore); } 


	public GenericModel getModel() throws DAOException
  	{ 
		return new FasSigeDetenzioneModel(  
				getIdFasSigeDetenzione() , 
				getFasIdFasSige() , 
				getLdIdLuogoDetenzione() , 
				getDataInserimento() , 
				getCodOperatoreInserimento() , 
				getCodUfficioInserimento() , 
				"",
				getDataAggiornamento() , 
				getCodOperatoreAggiornamento() , 
				getCodUfficioAggiornamento() , 
				"",
				getAcIdAltraCausa()  
			);
  	}

	public void setDAOFromModel(FasSigeDetenzioneModel aModel) throws DAOException
  	{
		setIdFasSigeDetenzione( aModel.getIdFasSigeDetenzione() );  
		setFasIdFasSige( aModel.getFasIdFasSige() );  
		setLdIdLuogoDetenzione( aModel.getLdIdLuogoDetenzione() );  
		setDataInserimento( aModel.getDataInserimento() );  
		setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
		setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
		setDataAggiornamento( aModel.getDataAggiornamento() );  
		setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
		setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		setAcIdAltraCausa( aModel.getAcIdAltraCausa() );  
	}


	public void setDAOFromModelForUpdate(FasSigeDetenzioneModel aModel) throws DAOException
  	{
		setIdFasSigeDetenzione( aModel.getIdFasSigeDetenzione() );  
		setFasIdFasSige( aModel.getFasIdFasSige() );  
		setLdIdLuogoDetenzione( aModel.getLdIdLuogoDetenzione() );  
		setDataAggiornamento( aModel.getDataAggiornamento() );  
		setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
		setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		setAcIdAltraCausa( aModel.getAcIdAltraCausa() );  
		setCondizioneByKey(aModel.getIdFasSigeDetenzione());
	}


	public void setCondizione(FasSigeDetenzioneModel aModel)
	{
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
	}

	public void setCondizioneByIdFasSige(FasSigeDetenzioneModel aModel)
	{
		setCondition(" FAS_ID_FAS_SIGE = " + aModel.getFasIdFasSige() ); 
	}

	public void setCondizioneByKey(BigDecimal key)
 	{
		setCondition(" ID_FAS_SIGE_DETENZIONE = " + key ); 
	}
	
	/**
	 * Prepara la condizione di Ricerca per ottenere l'ultimo 
	 * record inserito per un determinato Fascicolo.
	 * @param aKey
	 * @throws DAOException
	 */
	  public void setCondizioneUltimoLuogoByFascicolo(BigDecimal aKey) throws DAOException
	  {
	    String lSql = "";
	    lSql += " DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM FAS_SIGE_DETENZIONE WHERE FAS_ID_FAS_SIGE = " + aKey + ")";
	    lSql += " AND FAS_ID_FAS_SIGE = " + aKey ;
	    setCondition(lSql);
	  }

}
