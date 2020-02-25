package siap.sico.libertaanticipata.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: PeriodoLibanticipataDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PeriodoLibanticipata</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class PeriodoLibanticipataDAO extends TableDAO
{
  public PeriodoLibanticipataDAO (Connection con)
  {
       super(con);
       setTable("PERIODO_LIBANTICIPATA");

       //Settare la Sequence e i campi chiave
       setSequenceField("ID_PERIODO_LIBANTICIPATA","PER_LIB_SEQ");
       setFieldKey("ID_LICENZA_LIBANTICIPATA", BIG_DECIMAL);

       setField("ID_PERIODO_LIBANTICIPATA", BIG_DECIMAL);
       setField("DATA_INIZIO", DATE);
       setField("DATA_FINE", DATE);
       setField("FLAG_CONCESSO", STRING);
       setField("DATA_INSERIMENTO", DATE);
       setField("COD_OPERATORE_INSERIMENTO", STRING);
       setField("COD_UFFICIO_INSERIMENTO", STRING);
       setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
       setField("DATA_AGGIORNAMENTO", DATE);
       setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
       setField("LIC_ID_LICENZA_LIBANTICIPATA", BIG_DECIMAL);
  }


  //
  // METODI GET()
  //

  public BigDecimal 	getIdPeriodoLibanticipata() 	throws DAOException	 { return getBigDecimal("ID_PERIODO_LIBANTICIPATA"); }
  public Date 		getDataInizio() 		throws DAOException	 { return getDate("DATA_INIZIO"); }
  public Date 		getDataFine() 		throws DAOException	 { return getDate("DATA_FINE"); }
  public String 	getFlagConcesso() 	throws DAOException	 { return getString("FLAG_CONCESSO"); }
  public Date 		getDataInserimento() 	throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 	getCodOperatoreInserimento() 	throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public String 	getCodUfficioInserimento() 	throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 	getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 		getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 	getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal 	getLicIdLicenzaLibanticipata() 	throws DAOException	 { return getBigDecimal("LIC_ID_LICENZA_LIBANTICIPATA"); }


  //
  // METODI SET()
  //

  public void  	 setIdPeriodoLibanticipata(BigDecimal aValore )   { setBigDecimal("ID_PERIODO_LIBANTICIPATA", aValore); }
  public void  	 setDataInizio(Date aValore ) 			  { setDate("DATA_INIZIO", aValore); }
  public void  	 setDataFine(Date aValore ) 			  { setDate("DATA_FINE", aValore); }
  public void  	 setFlagConcesso(String aValore ) 		  { setString("FLAG_CONCESSO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 		  { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 	  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 	  { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 	  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 		  { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 	  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setLicIdLicenzaLibanticipata(BigDecimal aValore) { setBigDecimal("LIC_ID_LICENZA_LIBANTICIPATA", aValore); }


  public GenericModel getModel() throws DAOException
  {
    return new PeriodoLibAnticipataModel(
           getIdPeriodoLibanticipata() ,
           getDataInizio() ,
           getDataFine() ,
           getFlagConcesso() ,
           getDataInserimento() ,
           getCodOperatoreInserimento() ,
           getCodUfficioInserimento() ,
           "",
           getCodOperatoreAggiornamento() ,
           getDataAggiornamento() ,
           getCodUfficioAggiornamento() ,
           "",
           getLicIdLicenzaLibanticipata()
           );
  }


  public void 	 setDAOFromModel(PeriodoLibAnticipataModel aModel) throws DAOException
  {
       setIdPeriodoLibanticipata( aModel.getIdPeriodoLibanticipata() );
       setDataInizio( aModel.getDataInizio() );
       setDataFine( aModel.getDataFine() );
       setFlagConcesso( aModel.getFlagConcesso() );
       setDataInserimento( aModel.getDataInserimento() );
       setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
       setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
       setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
       setDataAggiornamento( aModel.getDataAggiornamento() );
       setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
       setLicIdLicenzaLibanticipata( aModel.getLicIdLicenzaLibanticipata() );
  }


  public void 	 setDAOFromModelForUpdate(PeriodoLibAnticipataModel aModel) throws DAOException
  {
       setIdPeriodoLibanticipata( aModel.getIdPeriodoLibanticipata() );
       setDataInizio( aModel.getDataInizio() );
       setDataFine( aModel.getDataFine() );
       setFlagConcesso( aModel.getFlagConcesso() );
       setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
       setDataAggiornamento( aModel.getDataAggiornamento() );
       setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
       setLicIdLicenzaLibanticipata( aModel.getLicIdLicenzaLibanticipata() );
       setCondizioneUpdate(aModel.getIdPeriodoLibanticipata());
  }


  public void setCondizione(PeriodoLibAnticipataModel aModel)
  {
       String lCondizioni = new String();

       boolean lInserito = false;
       if ( lInserito ) setCondition(lCondizioni);
  }


  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_PERIODO_LIBANTICIPATA = " + key );
  }

}
