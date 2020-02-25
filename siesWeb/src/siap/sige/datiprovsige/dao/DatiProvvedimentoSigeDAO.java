package siap.sige.datiprovsige.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sige.datiprovsige.model.DatiProvvedimentoSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: DatiProvvedimentoSigeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella DATI_PROVVEDIMENTO_SIGE</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class DatiProvvedimentoSigeDAO extends TableDAO 
{
	public DatiProvvedimentoSigeDAO (Connection con) 
	{
		super(con);
		setTable("DATI_PROVVEDIMENTO_SIGE");

		//Settare la Sequence e i campi chiave
		setSequenceField("ID_DATI_PROVVEDIMENTO_SIGE", "DATI_PROV_SIGE_SEQ");
		setFieldKey("ID_DATI_PROVVEDIMENTO_SIGE", BIG_DECIMAL);

		setField("ID_DATI_PROVVEDIMENTO_SIGE", BIG_DECIMAL);
		setField("COD_TIPO_DATI_PROV", STRING);
		setField("NOTE", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("TEN_ID_TENORE_SIGE", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

	public BigDecimal 	getIdDatiProvvedimentoSige() 		throws DAOException	 { return getBigDecimal("ID_DATI_PROVVEDIMENTO_SIGE"); } 
	public String 		getCodTipoDatiProv() 		throws DAOException	 { return getString("COD_TIPO_DATI_PROV"); } 
	public String 		getNote() 		throws DAOException	 { return getString("NOTE"); } 
	public String 		getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
	public Date 		getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 
	public String 		getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
	public String 		getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); } 
	public Date 		getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); } 
	public String 		getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); } 
	public BigDecimal 	getTenIdTenoreSige() 		throws DAOException	 { return getBigDecimal("TEN_ID_TENORE_SIGE"); } 


  //
  // METODI SET()
  //

	public void  	 setIdDatiProvvedimentoSige(BigDecimal aValore ) 			 { setBigDecimal("ID_DATI_PROVVEDIMENTO_SIGE", aValore); } 
	public void  	 setCodTipoDatiProv(String aValore ) 			 { setString("COD_TIPO_DATI_PROV", aValore); } 
	public void  	 setNote(String aValore ) 			 { setString("NOTE", aValore); } 
	public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); } 
	public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); } 
	public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); } 
	public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
	public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); } 
	public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); } 
	public void  	 setTenIdTenoreSige(BigDecimal aValore ) 			 { setBigDecimal("TEN_ID_TENORE_SIGE", aValore); } 


	public GenericModel getModel() throws DAOException
  	{ 
 		return new DatiProvvedimentoSigeModel(  
								 getIdDatiProvvedimentoSige() , 
								 getCodTipoDatiProv() , 
								 "",
								 getNote() , 
								 getCodOperatoreInserimento() , 
								 getDataInserimento() , 
								 getCodUfficioInserimento() , 
								 "",
								 getCodOperatoreAggiornamento() , 
								 getDataAggiornamento() , 
								 getCodUfficioAggiornamento() , 
								 "",
								 getTenIdTenoreSige()  
								);
		}


	 public void 	 setDAOFromModel(DatiProvvedimentoSigeModel aModel) throws DAOException
  		{
				 setIdDatiProvvedimentoSige( aModel.getIdDatiProvvedimentoSige() );  
				 setCodTipoDatiProv( aModel.getCodTipoDatiProv() );  
				 setNote( aModel.getNote() );  
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
				 setDataInserimento( aModel.getDataInserimento() );  
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
				 setDataAggiornamento( aModel.getDataAggiornamento() );  
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
				 setTenIdTenoreSige( aModel.getTenIdTenoreSige() );  
		}


	 public void 	 setDAOFromModelForUpdate(DatiProvvedimentoSigeModel aModel) throws DAOException
  		{
				 setIdDatiProvvedimentoSige( aModel.getIdDatiProvvedimentoSige() );  
				 setCodTipoDatiProv( aModel.getCodTipoDatiProv() );  
				 setNote( aModel.getNote() );  
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
				 setDataAggiornamento( aModel.getDataAggiornamento() );  
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
				 setTenIdTenoreSige( aModel.getTenIdTenoreSige() );  
		 setCondizioneUpdate(aModel.getIdDatiProvvedimentoSige());
		}


	public void setCondizione(DatiProvvedimentoSigeModel aModel)
	{
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
	}


	public void setCondizioneUpdate(BigDecimal key)
 	{
	 setCondition(" ID_DATI_PROVVEDIMENTO_SIGE = " + key ); 
	}
	
	public void setCondizioneDelete(BigDecimal aIdTenore)
 	{
		setCondition(" TEN_ID_TENORE_SIGE = " + aIdTenore ); 
	}
	
	  public void selCondizioneDeleteProvvedimento(BigDecimal aIdProvSige)
	  {
	    setCondition(" TEN_ID_TENORE_SIGE IN ( SELECT ID_TENORE_SIGE FROM TENORE_SIGE WHERE PROV_ID_PROVVEDIMENTO_SIGE = " + aIdProvSige + " )");
	  }

}
