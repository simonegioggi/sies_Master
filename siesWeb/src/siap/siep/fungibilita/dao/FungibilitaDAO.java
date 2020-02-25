package siap.siep.fungibilita.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.fungibilita.model.FungibilitaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: FungibilitaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Fungibilita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class FungibilitaDAO extends TableDAO
{
	public FungibilitaDAO (Connection con)
	{
			 super(con);

			 setTable("FUNGIBILITA");
       setSequenceField("ID_FUNGIBILITA","FNG_SEQ");

			 //Settare la Sequence e i campi chiave
			 setField("ID_FUNGIBILITA", BIG_DECIMAL);
			 setField("COD_TIPO_FUNGIBILITA", STRING);
			 setField("NUM_ANNI", BIG_DECIMAL);
			 setField("NUM_MESI", BIG_DECIMAL);
			 setField("NUM_GIORNI", BIG_DECIMAL);
			 setField("DATA_DA", DATE);
			 setField("DATA_A", DATE);
			 setField("DATA_INIZIO_VALIDITA", DATE);
			 setField("DATA_FINE_VALIDITA", DATE);
			 setField("COD_UFFICIO_FRUITORE", STRING);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
			 setField("EVE_ID_EVENTO", BIG_DECIMAL);
			 setField("FLAG_VALIDATO", STRING);
       setField("NUM_GIORNI_FRUITI", BIG_DECIMAL);
       setField("NUM_GIORNI_NON_FRUITI", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdFungibilita() 		throws DAOException	 { return getBigDecimal("ID_FUNGIBILITA"); }
			public String 				 getCodTipoFungibilita() 		throws DAOException	 { return getString("COD_TIPO_FUNGIBILITA"); }
			public BigDecimal 		 getNumAnni() 		throws DAOException	 { return getBigDecimal("NUM_ANNI"); }
			public BigDecimal 		 getNumMesi() 		throws DAOException	 { return getBigDecimal("NUM_MESI"); }
			public BigDecimal 		 getNumGiorni() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI"); }
			public Date 					 getDataDa() 		throws DAOException	 { return getDate("DATA_DA"); }
			public Date 					 getDataA() 		throws DAOException	 { return getDate("DATA_A"); }
			public Date 					 getDataInizioValidita() 		throws DAOException	 { return getDate("DATA_INIZIO_VALIDITA"); }
			public Date 					 getDataFineValidita() 		throws DAOException	 { return getDate("DATA_FINE_VALIDITA"); }
			public String 				 getCodUfficioFruitore() 		throws DAOException	 { return getString("COD_UFFICIO_FRUITORE"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			public BigDecimal 		 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
			public BigDecimal 		 getEveIdEvento() 		throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }
			public String 				 getFlagValidato() 		throws DAOException	 { return getString("FLAG_VALIDATO"); }

      public BigDecimal      getNumGiorniFruiti()     throws DAOException  { return getBigDecimal("NUM_GIORNI_FRUITI"); }
      public BigDecimal      getNumGiorniNonFruiti()     throws DAOException  { return getBigDecimal("NUM_GIORNI_NON_FRUITI"); }

  //
  // METODI SET()
  //

			public void  	 setIdFungibilita(BigDecimal aValore ) 			 { setBigDecimal("ID_FUNGIBILITA", aValore); }
			public void  	 setCodTipoFungibilita(String aValore ) 			 { setString("COD_TIPO_FUNGIBILITA", aValore); }
			public void  	 setNumAnni(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI", aValore); }
			public void  	 setNumMesi(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI", aValore); }
			public void  	 setNumGiorni(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI", aValore); }
			public void  	 setDataDa(Date aValore ) 			 { setDate("DATA_DA", aValore); }
			public void  	 setDataA(Date aValore ) 			 { setDate("DATA_A", aValore); }
			public void  	 setDataInizioValidita(Date aValore ) 			 { setDate("DATA_INIZIO_VALIDITA", aValore); }
			public void  	 setDataFineValidita(Date aValore ) 			 { setDate("DATA_FINE_VALIDITA", aValore); }
			public void  	 setCodUfficioFruitore(String aValore ) 			 { setString("COD_UFFICIO_FRUITORE", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
			public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
			public void  	 setEveIdEvento(BigDecimal aValore ) 			 { setBigDecimal("EVE_ID_EVENTO", aValore); }
			public void  	 setFlagValidato(String aValore ) 			 { setString("FLAG_VALIDATO", aValore); }

      public void    setNumGiorniFruiti    (BigDecimal aValore )       { setBigDecimal("NUM_GIORNI_FRUITI", aValore); }
      public void    setNumGiorniNonFruiti (BigDecimal aValore )       { setBigDecimal("NUM_GIORNI_NON_FRUITI", aValore); }

	public GenericModel getModel() throws DAOException
  			 {
 				 return new FungibilitaModel(
								 getIdFungibilita() ,
								 getCodTipoFungibilita() ,
								 "",
								 getNumAnni() ,
								 getNumMesi() ,
								 getNumGiorni() ,
								 getDataDa() ,
								 getDataA() ,
								 getDataInizioValidita() ,
								 getDataFineValidita() ,
								 getCodUfficioFruitore() ,
								 "",
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getFasSieIdFascicoloSiep() ,
								 getEveIdEvento() ,
								 getNumGiorniFruiti(),
								 getNumGiorniNonFruiti(),
								 getFlagValidato()
								);
		}


	 public void 	 setDAOFromModel(FungibilitaModel aModel) throws DAOException
  		{
				 setIdFungibilita( aModel.getIdFungibilita() );
				 setCodTipoFungibilita( aModel.getCodTipoFungibilita() );
				 setNumAnni( aModel.getNumAnni() );
				 setNumMesi( aModel.getNumMesi() );
				 setNumGiorni( aModel.getNumGiorni() );
				 setDataDa( aModel.getDataDa() );
				 setDataA( aModel.getDataA() );
				 setDataInizioValidita( aModel.getDataInizioValidita() );
				 setDataFineValidita( aModel.getDataFineValidita() );
				 setCodUfficioFruitore( aModel.getCodUfficioFruitore() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
				 setEveIdEvento( aModel.getEveIdEvento() );
				 setFlagValidato( aModel.getFlagValidato() );
				 
				 setNumGiorniFruiti( aModel.getNumGiorniFruiti() );
				 setNumGiorniNonFruiti( aModel.getNumGiorniNonFruiti());
				 
		}

  public void setDAOFromModelForUpdate(FungibilitaModel aModel) throws DAOException
  {
    //setIdFungibilita( aModel.getIdFungibilita() );
    setCodTipoFungibilita( aModel.getCodTipoFungibilita() );
    setNumAnni( aModel.getNumAnni() );
    setNumMesi( aModel.getNumMesi() );
    setNumGiorni( aModel.getNumGiorni() );
    setDataDa( aModel.getDataDa() );
    setDataA( aModel.getDataA() );
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setCodUfficioFruitore( aModel.getCodUfficioFruitore() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setEveIdEvento( aModel.getEveIdEvento() );
    setFlagValidato( aModel.getFlagValidato() );
    setNumGiorniFruiti( aModel.getNumGiorniFruiti() );
    setNumGiorniNonFruiti( aModel.getNumGiorniNonFruiti());

    setCondizioneUpdate(aModel.getIdFungibilita());
  }

/*
  public void setCondizione(FungibilitaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }
*/
  
	public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_FUNGIBILITA = " + key );
  }

  public void setCondizioneUpdateEveIdEvento(BigDecimal key)
  {
    setCondition(" EVE_ID_EVENTO = " + key );
  }
}
