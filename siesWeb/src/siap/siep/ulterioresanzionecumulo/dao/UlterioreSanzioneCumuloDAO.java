package siap.siep.ulterioresanzionecumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: UlterioreSanzioneCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella UlterioreSanzioneCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class UlterioreSanzioneCumuloDAO extends TableDAO
{
	public UlterioreSanzioneCumuloDAO (Connection con)
	{
			 super(con);
			 setTable("ULTERIORE_SANZIONE_CUMULO");
       setSequenceField("ID_ULTERIORE_SANZIONE_CUMULO", "ULT_SAN_SEQ");

			 //Settare la Sequence e i campi chiave

			 setField("ID_ULTERIORE_SANZIONE_CUMULO", BIG_DECIMAL);
			 setField("COD_TIPO_ULTERIORE_SANZIONE", STRING);
			 setField("NUM_ANNI", BIG_DECIMAL);
			 setField("NUM_MESI", BIG_DECIMAL);
			 setField("NUM_GIORNI", BIG_DECIMAL);
			 setField("SANZIONE", BIG_DECIMAL);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
			 setField("CUM_ID_CUMULO", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdUlterioreSanzioneCumulo() 		throws DAOException	 { return getBigDecimal("ID_ULTERIORE_SANZIONE_CUMULO"); }
			public String 				 getCodTipoUlterioreSanzione() 		throws DAOException	 { return getString("COD_TIPO_ULTERIORE_SANZIONE"); }
      public String  	       getDescrTipoUlterioreSanzione( ) 			throws DAOException {return  getString("DESCR_ULTERIORE_SANZIONE"); }

      public BigDecimal 		 getNumAnni() 		throws DAOException	 { return getBigDecimal("NUM_ANNI"); }
			public BigDecimal 		 getNumMesi() 		throws DAOException	 { return getBigDecimal("NUM_MESI"); }
			public BigDecimal 		 getNumGiorni() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI"); }
			public BigDecimal 		 getSanzione() 		throws DAOException	 { return getBigDecimal("SANZIONE"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			public BigDecimal 		 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
      public BigDecimal 		 getCumIdCumulo() 		throws DAOException	 { return getBigDecimal("CUM_ID_CUMULO"); }


  //
  // METODI SET()
  //

			public void  	 setIdUlterioreSanzioneCumulo(BigDecimal aValore ) 			 { setBigDecimal("ID_ULTERIORE_SANZIONE_CUMULO", aValore); }
			public void  	 setCodTipoUlterioreSanzione(String aValore ) 			 { setString("COD_TIPO_ULTERIORE_SANZIONE", aValore); }
      public void  	 setDescrTipoUlterioreSanzione(String aValore ) 			 { setString("DESCR_ULTERIORE_SANZIONE", aValore); }
      public void  	 setNumAnni(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI", aValore); }
			public void  	 setNumMesi(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI", aValore); }
			public void  	 setNumGiorni(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI", aValore); }
			public void  	 setSanzione(BigDecimal aValore ) 			 { setBigDecimal("SANZIONE", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
			public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
      public void  	 setCumIdCumulo(BigDecimal aValore ) 			 { setBigDecimal("CUM_ID_CUMULO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new UlterioreSanzioneCumuloModel(
								 getIdUlterioreSanzioneCumulo() ,
								 getCodTipoUlterioreSanzione() ,
								 getDescrTipoUlterioreSanzione(),
								 getNumAnni() ,
								 getNumMesi() ,
								 getNumGiorni() ,
								 getSanzione() ,
								 getDataInserimento() ,
								 getDataAggiornamento() ,
								 getCodOperatoreInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getFasSieIdFascicoloSiep(),
                 getCumIdCumulo()
								);
		}


	 public void 	 setDAOFromModel(UlterioreSanzioneCumuloModel aModel) throws DAOException
  		{
				 setIdUlterioreSanzioneCumulo( aModel.getIdUlterioreSanzioneCumulo() );
				 setCodTipoUlterioreSanzione( aModel.getCodTipoUlterioreSanzione() );
				 setNumAnni( aModel.getNumAnni() );
				 setNumMesi( aModel.getNumMesi() );
				 setNumGiorni( aModel.getNumGiorni() );
				 setSanzione( aModel.getSanzione() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
         setCumIdCumulo( aModel.getCumIdCumulo() );
     }


	 public void 	 setDAOFromModelForUpdate(UlterioreSanzioneCumuloModel aModel) throws DAOException
  		{
				 setIdUlterioreSanzioneCumulo( aModel.getIdUlterioreSanzioneCumulo() );
				 setCodTipoUlterioreSanzione( aModel.getCodTipoUlterioreSanzione() );
				 setNumAnni( aModel.getNumAnni() );
				 setNumMesi( aModel.getNumMesi() );
				 setNumGiorni( aModel.getNumGiorni() );
				 setSanzione( aModel.getSanzione() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
         setCumIdCumulo( aModel.getCumIdCumulo() );
		     setCondizioneUpdate(aModel.getIdUlterioreSanzioneCumulo());
		}


	public void setCondizione(UlterioreSanzioneCumuloModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_ULTERIORE_SANZIONE_CUMULO = " + key );
		 }

	
	 public void 	 setDAOFromModelForDeleteUlterioriSanz(UlterioreSanzioneCumuloModel aModel, BigDecimal aIdFas) throws DAOException
		{
				 setIdUlterioreSanzioneCumulo( aModel.getIdUlterioreSanzioneCumulo() );
				 setCodTipoUlterioreSanzione( aModel.getCodTipoUlterioreSanzione() );
				 setNumAnni( aModel.getNumAnni() );
				 setNumMesi( aModel.getNumMesi() );
				 setNumGiorni( aModel.getNumGiorni() );
				 setSanzione( aModel.getSanzione() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
      setCumIdCumulo( aModel.getCumIdCumulo() );
      setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFas );
	//     setCondizioneUpdate(aModel.getIdUlterioreSanzioneCumulo());
      
		}	

	public void setCondizioneIdCumulo(BigDecimal key)
	{
		setCondition(" CUM_ID_CUMULO = " + key );
	}
	
}
