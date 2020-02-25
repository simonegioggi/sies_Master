package siap.siep.parametro.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.parametro.model.ParametroModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
 * <p>Title: ParametroDAO</p>
 * <p>Description: Classe DAO che rappresenta la tabella Parametro</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ParametroDAO extends TableDAO
{
  public ParametroDAO (Connection con)
  {
    super(con);
    setTable("PARAMETRO");

    //Settare la Sequence e i campi chiave
    setSequenceField("ID_PARAMETRO", "PAR_SEQ");
    setFieldKey("ID_PARAMETRO", BIG_DECIMAL);

    setField("ID_PARAMETRO", BIG_DECIMAL);
    setField("NOME_PARAMETRO", STRING);
    setField("VALORE", STRING);
    setField("ANNI", BIG_DECIMAL);
    setField("MESI", BIG_DECIMAL);
    setField("GIORNI", BIG_DECIMAL);
    setField("IMPORTO", BIG_DECIMAL);
    setField("DATA_INIZIO_VALIDITA", DATE);
    setField("DATA_FINE_VALIDITA", DATE);
    setField("COD_UFFICIO_VALIDITA", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORMANENTO", STRING);
  }

  //
  // METODI GET()
  //
  public BigDecimal 		 getIdParametro() 		  throws DAOException	 { return getBigDecimal("ID_PARAMETRO"); }
  public String 				 getNomeParametro() 		throws DAOException	 { return getString("NOME_PARAMETRO"); }
  public String 				 getValore() 		        throws DAOException	 { return getString("VALORE"); }
  public BigDecimal 		 getAnni() 		          throws DAOException	 { return getBigDecimal("ANNI"); }
  public BigDecimal 		 getMesi() 		          throws DAOException	 { return getBigDecimal("MESI"); }
  public BigDecimal 		 getGiorni() 		        throws DAOException	 { return getBigDecimal("GIORNI"); }
  public BigDecimal 		 getImporto() 		      throws DAOException	 { return getBigDecimal("IMPORTO"); }
  public Date 					 getDataInizioValidita()throws DAOException	 { return getDate("DATA_INIZIO_VALIDITA"); }
  public Date 					 getDataFineValidita() 	throws DAOException	 { return getDate("DATA_FINE_VALIDITA"); }
  public String 				 getCodUfficioValidita()throws DAOException	 { return getString("COD_UFFICIO_VALIDITA"); }
  public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 					 getDataAggiornamento() 	throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 				 getCodUfficioAggiormanento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORMANENTO"); }


  //
  // METODI SET()
  //
  public void  	 setIdParametro(BigDecimal aValore ) 			 { setBigDecimal("ID_PARAMETRO", aValore); }
  public void  	 setNomeParametro(String aValore ) 			   { setString("NOME_PARAMETRO", aValore); }
  public void  	 setValore(String aValore ) 			         { setString("VALORE", aValore); }
  public void  	 setAnni(BigDecimal aValore ) 			       { setBigDecimal("ANNI", aValore); }
  public void  	 setMesi(BigDecimal aValore ) 			       { setBigDecimal("MESI", aValore); }
  public void  	 setGiorni(BigDecimal aValore ) 			     { setBigDecimal("GIORNI", aValore); }
  public void  	 setImporto(BigDecimal aValore ) 			     { setBigDecimal("IMPORTO", aValore); }
  public void  	 setDataInizioValidita(Date aValore ) 		 { setDate("DATA_INIZIO_VALIDITA", aValore); }
  public void  	 setDataFineValidita(Date aValore ) 			 { setDate("DATA_FINE_VALIDITA", aValore); }
  public void  	 setCodUfficioValidita(String aValore ) 	 { setString("COD_UFFICIO_VALIDITA", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			   { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			    { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiormanento(String aValore ) 	{ setString("COD_UFFICIO_AGGIORMANENTO", aValore); }


  public GenericModel getModel() throws DAOException
  {
    return new ParametroModel(
                              getIdParametro() ,
                              getNomeParametro() ,
                              getValore() ,
                              getAnni() ,
                              getMesi() ,
                              getGiorni() ,
                              getImporto() ,
                              getDataInizioValidita() ,
                              getDataFineValidita() ,
                              getCodUfficioValidita() ,
                              "",
                              getCodOperatoreInserimento() ,
                              getDataInserimento() ,
                              getCodUfficioInserimento() ,
                              "",
                              getCodOperatoreAggiornamento() ,
                              getDataAggiornamento() ,
                              getCodUfficioAggiormanento(),
                              ""
                            );
  }

  public void 	 setDAOFromModel(ParametroModel aModel) throws DAOException
  {
    setIdParametro( aModel.getIdParametro() );
    setNomeParametro( aModel.getNomeParametro() );
    setValore( aModel.getValore() );
    setAnni( aModel.getAnni() );
    setMesi( aModel.getMesi() );
    setGiorni( aModel.getGiorni() );
    setImporto( aModel.getImporto() );
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setCodUfficioValidita( aModel.getCodUfficioValidita() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiormanento( aModel.getCodUfficioAggiormanento() );
  }

  public void setDAOFromModelForUpdate(ParametroModel aModel) throws DAOException
  {
    //setIdParametro( aModel.getIdParametro() );
    
    setNomeParametro( aModel.getNomeParametro() );
    setValore( aModel.getValore() );
    setAnni( aModel.getAnni() );
    setMesi( aModel.getMesi() );
    setGiorni( aModel.getGiorni() );
    setImporto( aModel.getImporto() );
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setCodUfficioValidita( aModel.getCodUfficioValidita() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiormanento( aModel.getCodUfficioAggiormanento() );
    
    setCondizioneUpdate(aModel.getIdParametro());
  }

  public void setCondizione(ParametroModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_PARAMETRO = " + key );
  }
}
