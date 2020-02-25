package siap.sige.richiesta.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sige.richiesta.model.RichiestaSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: RichiestaSigeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RichiestaSige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RichiestaSigeDAO extends TableDAO 
{
	public RichiestaSigeDAO (Connection con) 
	{
		super(con);
		setTable("RICHIESTA_SIGE");

		 //Settare la Sequence e i campi chiave
		setSequenceField("ID_RICHIESTA_SIGE", "RIC_SIGE_SEQ");
		setFieldKey("ID_RICHIESTA_SIGE", BIG_DECIMAL);

		setField("ID_RICHIESTA_SIGE", BIG_DECIMAL);
		setField("COD_TIPO_ATTO", STRING);
		setField("COD_TIPO_RICHIEDENTE", STRING);
		setField("COD_SEDE_RICHIEDENTE", STRING);
		setField("DESC_RICHIEDENTE", STRING);
		setField("COD_UFFICIO_RICHIEDENTE", STRING);		
		setField("DATA_EMISSIONE", DATE);
		setField("DATA_DEPOSITO", DATE);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("DATA_ARRIVO_CANCELLERIA", DATE);
	}


  //
  // METODI GET()
  //
		public BigDecimal 	getIdRichiestaSige() 		throws DAOException	 { return getBigDecimal("ID_RICHIESTA_SIGE"); } 
		public String 		getCodTipoAtto() 		throws DAOException	 { return getString("COD_TIPO_ATTO"); } 
		public String 		getCodTipoRichiedente() 	throws DAOException	 { return getString("COD_TIPO_RICHIEDENTE"); } 
		public String 		getCodSedeRichiedente() 	throws DAOException	 { return getString("COD_SEDE_RICHIEDENTE"); } 
		public String 		getDescRichiedente() 		throws DAOException	 { return getString("DESC_RICHIEDENTE"); } 
		public String 		getCodUfficioRichiedente() 	throws DAOException	 { return getString("COD_UFFICIO_RICHIEDENTE"); } 
		public Date 		getDataEmissione() 			throws DAOException	 { return getDate("DATA_EMISSIONE"); } 
		public Date 		getDataDeposito() 			throws DAOException	 { return getDate("DATA_DEPOSITO"); } 
		public String 		getCodOperatoreInserimento() 	throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
		public Date 		getDataInserimento() 			throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 
		public String 		getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
		public String 		getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); } 
		public Date 		getDataAggiornamento() 			throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); } 
		public String 		getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); } 
		public BigDecimal 	getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); } 
		public Date 		getDataArrivoCancelleria() 		throws DAOException	 { return getDate("DATA_ARRIVO_CANCELLERIA"); } 


  //
  // METODI SET()
  //
		public void  	 setIdRichiestaSige(BigDecimal aValore ) 		{ setBigDecimal("ID_RICHIESTA_SIGE", aValore); } 
		public void  	 setCodTipoAtto(String aValore ) 			{ setString("COD_TIPO_ATTO", aValore); } 
		public void  	 setCodTipoRichiedente(String aValore ) 		{ setString("COD_TIPO_RICHIEDENTE", aValore); } 
		public void  	 setCodSedeRichiedente(String aValore ) 		{ setString("COD_SEDE_RICHIEDENTE", aValore); } 
		public void  	 setDescRichiedente(String aValore ) 		{ setString("DESC_RICHIEDENTE", aValore); } 
		public void  	 setCodUfficioRichiedente(String aValore ) 		{ setString("COD_UFFICIO_RICHIEDENTE", aValore); } 
		public void  	 setDataEmissione(Date aValore ) 			 	{ setDate("DATA_EMISSIONE", aValore); } 
		public void  	 setDataDeposito(Date aValore ) 			 	{ setDate("DATA_DEPOSITO", aValore); } 
		public void  	 setCodOperatoreInserimento(String aValore ) 	{ setString("COD_OPERATORE_INSERIMENTO", aValore); } 
		public void  	 setDataInserimento(Date aValore ) 			 	{ setDate("DATA_INSERIMENTO", aValore); } 
		public void  	 setCodUfficioInserimento(String aValore ) 		{ setString("COD_UFFICIO_INSERIMENTO", aValore); } 
		public void  	 setCodOperatoreAggiornamento(String aValore ) 	{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
		public void  	 setDataAggiornamento(Date aValore ) 			{ setDate("DATA_AGGIORNAMENTO", aValore); } 
		public void  	 setCodUfficioAggiornamento(String aValore ) 	{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); } 
		public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 	{ setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); } 
		public void  	 setDataArrivoCancelleria(Date aValore ) 			 { setDate("DATA_ARRIVO_CANCELLERIA", aValore); } 


	public GenericModel getModel() throws DAOException
  	{ 
 				 return new RichiestaSigeModel(  
								 getIdRichiestaSige() , 
								 getCodTipoAtto() , 
								 "",
								 getCodTipoRichiedente() , 
								 "",
								 getCodSedeRichiedente(),
								   "",
								   getDescRichiedente(),				   
								   getCodUfficioRichiedente(),
								   "",
								 getDataEmissione() , 
								 getDataDeposito() , 
								 getCodOperatoreInserimento() , 
								 getDataInserimento() , 
								 getCodUfficioInserimento() , 
								 "",
								 getCodOperatoreAggiornamento() , 
								 getDataAggiornamento() , 
								 getCodUfficioAggiornamento() , 
								 "",
								 getFasSieIdFascicoloSiep(), 
								getDataArrivoCancelleria()  
								);
	}


	public void 	 setDAOFromModel(RichiestaSigeModel aModel) throws DAOException
  	{
		setIdRichiestaSige( aModel.getIdRichiestaSige() );  
		setCodTipoAtto( aModel.getCodTipoAtto() );  
		setCodTipoRichiedente( aModel.getCodTipoRichiedente() );  
		setCodSedeRichiedente(aModel.getCodSedeRichiedente());
		setDescRichiedente(aModel.getDescRichiedente());
		setCodUfficioRichiedente( aModel.getCodUfficioRichiedente() );  
		setDataEmissione( aModel.getDataEmissione() );  
		setDataDeposito( aModel.getDataDeposito() );  
		setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
		setDataInserimento( aModel.getDataInserimento() );  
		setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
		setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
		setDataAggiornamento( aModel.getDataAggiornamento() );  
		setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );  
		setDataArrivoCancelleria( aModel.getDataArrivoCancelleria() );  

  	}

	public void 	 setDAOFromModelForUpdate(RichiestaSigeModel aModel) throws DAOException
  	{
		//setIdRichiestaSige( aModel.getIdRichiestaSige() );  
		setCodTipoAtto( aModel.getCodTipoAtto() );  
		setCodTipoRichiedente( aModel.getCodTipoRichiedente() );  
		setCodSedeRichiedente(aModel.getCodSedeRichiedente());
		setDescRichiedente(aModel.getDescRichiedente());
		setCodUfficioRichiedente( aModel.getCodUfficioRichiedente() );  
		setDataEmissione( aModel.getDataEmissione() );  
		//setDataDeposito( aModel.getDataDeposito() );  
		setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
		setDataAggiornamento( aModel.getDataAggiornamento() );  
		setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		//setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );  
		setDataArrivoCancelleria( aModel.getDataArrivoCancelleria() );  

		setCondizioneUpdate(aModel.getIdRichiestaSige());
	}


	public void setCondizione(RichiestaSigeModel aModel)
	{
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
	}


	public void setCondizioneUpdate(BigDecimal key)
 	{
		setCondition(" ID_RICHIESTA_SIGE = " + key ); 
	}

	public void setDAOFromModelForUpdateDeassegnazione(RichiestaSigeModel aModel) throws DAOException
  	{
		setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
		setDataAggiornamento( aModel.getDataAggiornamento() );  
		setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );  

		setCondizioneUpdate(aModel.getIdRichiestaSige());
	}
	
}
