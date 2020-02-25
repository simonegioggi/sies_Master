package siap.sico.magistrato.dao;

import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sico.magistrato.model.MagistratoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: MagistratoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Magistrato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class MagistratoDAO extends SIAPTableDAO
{
  public MagistratoDAO (Connection con)
  {
    super(con);
    setTable("MAGISTRATO");

    //Settare la Sequence e i campi chiave
    setField("COD_MAGISTRATO", STRING);
    setField("COGNOME", STRING);
    setField("NOME", STRING);
    setField("FLAG_STATO", STRING);
    setField("COD_UFFICIO_APPARTENENZA", STRING);
    setField("DATA_INIZIO_VALIDITA", DATE);
    setField("DATA_FINE_VALIDITA", DATE);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("E_MAIL_UFFICIO", STRING);
    setField("E_MAIL_PRIVATA", STRING);
    setField("NUM_CELLULARE", STRING);
  }


  //
  // METODI GET()
  //

  public String   getCodMagistrato() 	          throws DAOException	 { return getString("COD_MAGISTRATO"); }
  public String   getCognome() 		          throws DAOException	 { return getString("COGNOME"); }
  public String   getNome() 		          throws DAOException	 { return getString("NOME"); }
  public String   getFlagStato() 	          throws DAOException	 { return getString("FLAG_STATO"); }
  public String   getCodUfficioAppartenenza() 	  throws DAOException	 { return getString("COD_UFFICIO_APPARTENENZA"); }
  public Date 	  getDataInizioValidita() 	  throws DAOException	 { return getDate("DATA_INIZIO_VALIDITA"); }
  public Date 	  getDataFineValidita() 	  throws DAOException	 { return getDate("DATA_FINE_VALIDITA"); }
  public String   getCodOperatoreInserimento() 	  throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 	  getDataInserimento() 		  throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String   getCodUfficioInserimento() 	  throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String   getCodOperatoreAggiornamento()  throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 	  getDataAggiornamento() 	  throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String   getCodUfficioAggiornamento() 	  throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public String   getEMailUfficio() 		  throws DAOException	 { return getString("E_MAIL_UFFICIO"); }
  public String   getEMailPrivata() 		  throws DAOException	 { return getString("E_MAIL_PRIVATA"); }
  public String   getNumCellulare() 		  throws DAOException	 { return getString("NUM_CELLULARE"); }



  //
  // METODI SET()
  //

  public void  	 setCodMagistrato(String aValore ) 	        { setString("COD_MAGISTRATO", aValore); }
  public void  	 setCognome(String aValore ) 		        { setString("COGNOME", aValore); }
  public void  	 setNome(String aValore ) 		        { setString("NOME", aValore); }
  public void  	 setFlagStato(String aValore ) 		        { setString("FLAG_STATO", aValore); }
  public void  	 setCodUfficioAppartenenza(String aValore )     { setString("COD_UFFICIO_APPARTENENZA", aValore); }
  public void  	 setDataInizioValidita(Date aValore ) 	        { setDate("DATA_INIZIO_VALIDITA", aValore); }
  public void  	 setDataFineValidita(Date aValore ) 	        { setDate("DATA_FINE_VALIDITA", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore )    { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 	        { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore )      { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore )  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 		{ setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 	{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setEMailUfficio(String aValore ) 		{ setString("E_MAIL_UFFICIO", aValore); }
  public void  	 setEMailPrivata(String aValore ) 		{ setString("E_MAIL_PRIVATA", aValore); }
  public void  	 setNumCellulare(String aValore ) 		{ setString("NUM_CELLULARE", aValore); }


  public GenericModel getModel() throws DAOException
  {
    return new MagistratoModel( getCodMagistrato() ,
                                "",
                                getCognome() ,
                                getNome() ,
                                getFlagStato() ,
                                getCodUfficioAppartenenza() ,
                                "",
                                getDataInizioValidita() ,
                                getDataFineValidita() ,
                                getCodOperatoreInserimento() ,
                                getDataInserimento() ,
                                getCodUfficioInserimento() ,
                                "",
                                getCodOperatoreAggiornamento() ,
                                getDataAggiornamento() ,
                                getCodUfficioAggiornamento(),
                                "" ,
				 getEMailUfficio() ,
                                 getEMailPrivata() ,
                                 getNumCellulare() ,null
                                 );
    }

    public void setDAOFromModel(MagistratoModel aModel) throws DAOException
    {
      setCodMagistrato( aModel.getCodMagistrato() );
      setCognome( aModel.getCognome() );
      setNome( aModel.getNome() );
      setFlagStato( aModel.getFlagStato() );
      setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
      setDataInizioValidita( aModel.getDataInizioValidita() );
      setDataFineValidita( aModel.getDataFineValidita() );
      setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
      setDataInserimento( aModel.getDataInserimento() );
      setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setEMailUfficio( aModel.getEMailUfficio() );
      setEMailPrivata( aModel.getEMailPrivata() );
      setNumCellulare( aModel.getNumCellulare() );
    }


    public void setDAOFromModelForUpdate(MagistratoModel aModel) throws DAOException
    {
      //setCodMagistrato( aModel.getCodMagistrato() );
      //setCognome( aModel.getCognome() );
      //setNome( aModel.getNome() );
      setFlagStato( aModel.getFlagStato() );
      //setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
      setDataInizioValidita( aModel.getDataInizioValidita() );
      setDataFineValidita( aModel.getDataFineValidita() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setEMailUfficio( aModel.getEMailUfficio() );
      setEMailPrivata( aModel.getEMailPrivata() );
      setNumCellulare( aModel.getNumCellulare() );
      setCondizioneUpdate(aModel.getCodMagistrato(), aModel.getCodUfficioAppartenenza());
    }


    public void setCondizione(MagistratoModel aModel)
    {
      String lCondizioni = new String();

      boolean lInserito = false;
      if ( lInserito ) setCondition(lCondizioni);
    }


    public void setCondizioneUpdate(String key)
    {
      setCondition(" COD_MAGISTRATO = '" + key + "'");
    }

    public void setCondizioneUpdate(String key,String aCodUff)
    {
      setCondition(" COD_MAGISTRATO = '" + key +  "' AND COD_UFFICIO_APPARTENENZA = '" + aCodUff + "'");
    }
}
