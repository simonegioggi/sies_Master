package siap.sige.magistratoassegnatario.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: MagistratoAssegnatarioDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella MagistratoAssegnatario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class MagistratoAssegnatarioDAO extends TableDAO 
{
	public MagistratoAssegnatarioDAO (Connection con) 
	{
			 super(con);
			 setTable("MAGISTRATO_ASSEGNATARIO");

			 //Settare la Sequence e i campi chiave

			 setField("DATA_INIZIO", DATE);
			 setField("DATA_FINE", DATE);
			 setField("COD_RUOLO_MAGISTRATO", STRING);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("MAG_COD_MAGISTRATO", STRING);
			 setField("FAS_SIGE_ID_FASCICOLO_SIGE", BIG_DECIMAL);
			 // intervento per nuova gestione udienze monocratiche/collegiali
			 setField("COD_PROCURATORE"            , STRING);
			 setField("COD_ID_ASSISTENTE"          , BIG_DECIMAL);
			 setField("FLAG_MODIF_IN_BLOCCO"	   , STRING); 
			 
	}


  //
  // METODI GET()
  //

			public Date 					 getDataInizio() 		throws DAOException	 { return getDate("DATA_INIZIO"); } 
			public Date 					 getDataFine() 		throws DAOException	 { return getDate("DATA_FINE"); } 
			public String 				 getCodRuoloMagistrato() 		throws DAOException	 { return getString("COD_RUOLO_MAGISTRATO"); } 
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); } 
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); } 
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); } 
			public String 				 getMagCodMagistrato() 		throws DAOException	 { return getString("MAG_COD_MAGISTRATO"); } 
			public BigDecimal 		 getFasSigeIdFascicoloSige() 		throws DAOException	 { return getBigDecimal("FAS_SIGE_ID_FASCICOLO_SIGE"); }
			// intervento per nuova gestione udienze monocratiche/collegiali
			public  String      getCodProcuratore()             throws DAOException  { return getString     ("COD_PROCURATORE"            ); } 
			public  BigDecimal  getCodIdAssistente()            throws DAOException  { return getBigDecimal ("COD_ID_ASSISTENTE"          ); } 
			public  String      getFlagModifBlocco()              throws DAOException  { return getString     ("FLAG_MODIF_IN_BLOCCO"     ); } 


  //
  // METODI SET()
  //

			public void  	 setDataInizio(Date aValore ) 			 { setDate("DATA_INIZIO", aValore); } 
			public void  	 setDataFine(Date aValore ) 			 { setDate("DATA_FINE", aValore); } 
			public void  	 setCodRuoloMagistrato(String aValore ) 			 { setString("COD_RUOLO_MAGISTRATO", aValore); } 
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); } 
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); } 
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); } 
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); } 
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); } 
			public void  	 setMagCodMagistrato(String aValore ) 			 { setString("MAG_COD_MAGISTRATO", aValore); } 
			public void  	 setFasSigeIdFascicoloSige(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIGE_ID_FASCICOLO_SIGE", aValore); } 
			// intervento per nuova gestione udienze monocratiche/collegiali
			public void  setCodProcuratore             (String      aValore )   { setString     ("COD_PROCURATORE"            , aValore); } 
			public void  setCodIdAssistente            (BigDecimal  aValore )   { setBigDecimal ("COD_ID_ASSISTENTE"          , aValore); }
			public void  setFlagModifBlocco            (String  aValore )   { setString ("FLAG_MODIF_IN_BLOCCO"          , aValore); } 


	public GenericModel getModel() throws DAOException
  			 { 
 				 return new MagistratoAssegnatarioModel(  
								 getDataInizio() , 
								 getDataFine() , 
								 getCodRuoloMagistrato() , 
								 "",
								 getCodOperatoreInserimento() , 
								 getDataInserimento() , 
								 getCodUfficioInserimento() , 
								 "",
								 getCodOperatoreAggiornamento() , 
								 getDataAggiornamento() , 
								 getCodUfficioAggiornamento() , 
								 "",
								 getMagCodMagistrato() , 
								 getFasSigeIdFascicoloSige()  ,
								 getCodProcuratore(),
								 getCodIdAssistente(),
								 getFlagModifBlocco()
								);
		}


	 public void 	 setDAOFromModel(MagistratoAssegnatarioModel aModel) throws DAOException
  		{
				 setDataInizio( aModel.getDataInizio() );  
				 setDataFine( aModel.getDataFine() );  
				 setCodRuoloMagistrato( aModel.getCodRuoloMagistrato() );  
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
				 setDataInserimento( aModel.getDataInserimento() );  
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
				 setDataAggiornamento( aModel.getDataAggiornamento() );  
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
				 setMagCodMagistrato( aModel.getMagCodMagistrato() );  
				 setFasSigeIdFascicoloSige( aModel.getFasSigeIdFascicoloSige() );  
				 // intervento per nuova gestione udienze monocratiche/collegiali
				 setCodProcuratore(aModel.getCodProcuratore());
				 setCodIdAssistente(aModel.getIdAssistente());
				 setFlagModifBlocco(aModel.getFlagModifBlocco());
		}
	 public void 	 setDAOFromModelForInserimento(MagistratoAssegnatarioModel aModel) throws DAOException
		{
				 setDataInizio( aModel.getDataInizio() );  
				 setDataFine( aModel.getDataFine() );  
				 setCodRuoloMagistrato( aModel.getCodRuoloMagistrato() );  
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
				 setDataInserimento( aModel.getDataInserimento() );  
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
				 setMagCodMagistrato( aModel.getMagCodMagistrato() );  
				 setFasSigeIdFascicoloSige( aModel.getFasSigeIdFascicoloSige() );  
				 // intervento per nuova gestione udienze monocratiche/collegiali
				 setCodProcuratore(aModel.getCodProcuratore());
				 setCodIdAssistente(aModel.getIdAssistente());
				 setFlagModifBlocco(aModel.getFlagModifBlocco());
		}


	 public void 	 setDAOFromModelForUpdate(MagistratoAssegnatarioModel aModel) throws DAOException
  		{
				 setDataInizio( aModel.getDataInizio() );  
				 setDataFine( aModel.getDataFine() );  
				 setCodRuoloMagistrato( aModel.getCodRuoloMagistrato() );  
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
				 setDataAggiornamento( aModel.getDataAggiornamento() );  
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
				 setMagCodMagistrato( aModel.getMagCodMagistrato() );  
				 setFasSigeIdFascicoloSige( aModel.getFasSigeIdFascicoloSige() );  
				 setCodProcuratore(aModel.getCodProcuratore());
				 setCodIdAssistente(aModel.getIdAssistente());
				 setFlagModifBlocco(aModel.getFlagModifBlocco());
		}


	public void setCondizione(MagistratoAssegnatarioModel aModel)
		 {
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_MAGISTRATO_ASSEGNATARIO = " + key ); 
		 }
	
	/**
	 * Prepara lo statement per chiudere il magistrato assegnatario attuale.
	 * @param aModel
	 * @throws DAOException
	 */
	 public void setDAOFromModelForChiusura(MagistratoAssegnatarioModel aModel) throws DAOException
		{
				 setDataFine( aModel.getDataFine() );  
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
				 setDataAggiornamento( aModel.getDataAggiornamento() );  
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
				 setCondizioneAttivo(aModel.getFasSigeIdFascicoloSige());
		}
	
	/**
	   * Imposta condizione SQL per individuare il magistrato corrente di un fascicolo.
	   * <p>
	   * @param aIdFascicolo l'id del fascicolo SIGE.
	   */
	  public void setCondizioneAttivo(BigDecimal aIdFascicolo)
	  {
		String lSql = new String("");
	    lSql += " FAS_SIGE_ID_FASCICOLO_SIGE = " + aIdFascicolo ;
	    lSql += " AND DATA_FINE IS null " ;

	    setCondition( lSql );
	  }

     public void setCondizioneUpdate(BigDecimal key, String lKey )
      {
        String lCondizioni = null;

        lCondizioni = " MAG_COD_MAGISTRATO = " + "'" + lKey + "'" ;
        lCondizioni += " AND FAS_SIGE_ID_FASCICOLO_SIGE = " + key ;

        setCondition(lCondizioni);
      }
	  
     public void setCondizioneUpdateExtend(BigDecimal key, String lKey, String flagBlocco )
     {
       String lCondizioni = null;

       lCondizioni = " MAG_COD_MAGISTRATO = " + "'" + lKey + "'" ;
       lCondizioni += " AND FAS_SIGE_ID_FASCICOLO_SIGE = " + key ;
       if(flagBlocco!=null || !"".equals(flagBlocco))
    	   lCondizioni += " AND (FLAG_MODIF_IN_BLOCCO = '" + flagBlocco + "' OR FLAG_MODIF_IN_BLOCCO IS NULL ) AND  DATA_FINE IS null ";
       setCondition(lCondizioni);
     }
     
}
