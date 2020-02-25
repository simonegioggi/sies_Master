package siap.sige.aula.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.aula.model.AulaUdienzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: AulaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella AULA_UDIENZA</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class AulaDAO extends SIAPTableDAO
{
  public AulaDAO (Connection con)
  {
	    super(con);
	    setTable("AULA_UDIENZA");
	
	    //Settare la Sequence e i campi chiave
	    setSequenceField("ID_AULA", "AULA_SEQ");
	    setFieldKey("ID_AULA", BIG_DECIMAL);
	    setFieldKey("ID_SEZIONE", BIG_DECIMAL);
	
	    setField("ID_AULA", BIG_DECIMAL);
	    setField("ID_SEZIONE", BIG_DECIMAL);
	    setField("DESCRIZIONE_AULA", STRING);
	    setField("DESCRIZIONE_STANZA", STRING);
	    setField("DESCRIZIONE_INGRESSO", STRING);
	    // 20170908: è un varchar nel db
	    setField("NUMERO_PIANO", STRING);
//	    setField("NUMERO_PIANO", BIG_DECIMAL);
	    setField("FLAG_PREDEFINITA", STRING);
	    setField("COD_OPERATORE_INSERIMENTO", STRING);
	    setField("DATA_INSERIMENTO", DATE);
	    setField("COD_UFFICIO_INSERIMENTO", STRING);
	    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
	    setField("DATA_AGGIORNAMENTO", DATE);
	    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
	  }
	
	  //
	  // METODI GET()
	  //
	  public BigDecimal getIdAula()                		throws DAOException	 { return getBigDecimal("ID_AULA"); }
	  public BigDecimal getIdSezione()                	throws DAOException	 { return getBigDecimal("ID_SEZIONE"); }
	  public String 	getDescrizioneAula() 	   		throws DAOException	 { return getString("DESCRIZIONE_AULA"); }
	  public String 	getDescrizioneStanza()     		throws DAOException	 { return getString("DESCRIZIONE_STANZA"); }
	  public String 	getDescrizioneIngresso()   		throws DAOException	 { return getString("DESCRIZIONE_INGRESSO"); }
	  // 20170908: è un varchar nel db
	  public String 	getNumeroPiano()           		throws DAOException	 { return getString("NUMERO_PIANO"); }
//	  public BigDecimal getNumeroPiano()           		throws DAOException	 { return getBigDecimal("NUMERO_PIANO"); }
	  public String 	getFlagPredefinita() 	   		throws DAOException	 { return getString("FLAG_PREDEFINITA"); }
	  public String 	getCodOperatoreInserimento()    throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
	  public Date 		getDataInserimento() 		    throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
	  public String 	getCodUfficioInserimento() 	    throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
	  public String   	getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
	  public Date 		getDataAggiornamento() 		    throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
	  public String 	getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
	
	  //
	  // METODI SET()
	  //
	  public void setIdAula(BigDecimal aValore)             	{ setBigDecimal("ID_AULA", aValore); }
	  public void setIdSezione(BigDecimal aValore)          	{ setBigDecimal("ID_SEZIONE", aValore); }
	  public void setDescrizioneAula(String aValore) 			{ setString("DESCRIZIONE_AULA", aValore); }
	  public void setDescrizioneStanza(String aValore) 			{ setString("DESCRIZIONE_STANZA", aValore); }
	  public void setDescrizioneIngresso(String aValore)    	{ setString("DESCRIZIONE_INGRESSO", aValore); }
	  // 20170908: è un varchar nel db
	  public void setNumeroPiano(String aValore)        		{ setString("NUMERO_PIANO", aValore); }
//	  public void setNumeroPiano(BigDecimal aValore)        	{ setBigDecimal("NUMERO_PIANO", aValore); }
	  public void setFlagPredefinita(String aValore) 			{ setString("FLAG_PREDEFINITA", aValore); }
	  public void setCodOperatoreInserimento(String aValore) 	{ setString("COD_OPERATORE_INSERIMENTO", aValore); }
	  public void setDataInserimento(Date aValore) 		        { setDate("DATA_INSERIMENTO", aValore); }
	  public void setCodUfficioInserimento(String aValore) 	    { setString("COD_UFFICIO_INSERIMENTO", aValore); }
	  public void setCodOperatoreAggiornamento(String aValore) 	{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
	  public void setDataAggiornamento(Date aValore) 		    { setDate("DATA_AGGIORNAMENTO", aValore); }
	  public void setCodUfficioAggiornamento(String aValore) 	{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }

	  public GenericModel getModel()
	  throws DAOException
	  {
	    return new AulaUdienzaModel(
	    			getIdAula(),
	    			getIdSezione(),
	    			getDescrizioneAula(),
	    			getDescrizioneStanza(),
	    			getDescrizioneIngresso(),
	    			getNumeroPiano(),
	    			getFlagPredefinita(),
	    			getCodOperatoreInserimento(),
	                getDataInserimento(),
	                getCodUfficioInserimento(),
	                getCodOperatoreAggiornamento(),
	                getDataAggiornamento() ,
	                getCodUfficioAggiornamento()
	                );
	  }

	  public void setDAOFromModel(AulaUdienzaModel aModel)
			  throws DAOException
	  {
		  setIdAula( aModel.getIdAula() );
		  setIdSezione( aModel.getIdSezione() );
		  setDescrizioneAula( aModel.getDescrizioneAula() );
		  setDescrizioneStanza( aModel.getDescrizioneStanza() );
		  setDescrizioneIngresso( aModel.getDescrizioneIngresso() );
		  setNumeroPiano( aModel.getNumeroPiano() );
		  setFlagPredefinita( aModel.getFlagPredefinita() );
		  setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
		  setDataInserimento( aModel.getDataInserimento() );
		  setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
		  setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
		  setDataAggiornamento( aModel.getDataAggiornamento() );
		  setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
	  }

	  public void setDAOFromModelForUpdate(AulaUdienzaModel aModel)
			  throws DAOException
	  {
		  setDescrizioneAula( aModel.getDescrizioneAula() );
		  setDescrizioneStanza( aModel.getDescrizioneStanza() );
		  setDescrizioneIngresso( aModel.getDescrizioneIngresso() );
		  // 20170908: è un varchar nel db
		  setNumeroPiano( aModel.getNumeroPiano() );
		  setFlagPredefinita( aModel.getFlagPredefinita() );
		  setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
		  setDataAggiornamento( aModel.getDataAggiornamento() );
		  setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
		  setCondizioneUpdate(aModel.getIdAula(), aModel.getIdSezione());
	  }

	  public void setCondizione(AulaUdienzaModel aModel)
	  {
		    String lCondizioni = new String();
		
		    boolean lInserito = false;
		    if ( lInserito ) setCondition(lCondizioni);
	  }
	
	  public void setCondizioneUpdate(BigDecimal idAula, BigDecimal idSezione)
	  {
		  setCondition(" ID_AULA = " + idAula + " AND ID_SEZIONE = " + idSezione );
	  }
	  
}