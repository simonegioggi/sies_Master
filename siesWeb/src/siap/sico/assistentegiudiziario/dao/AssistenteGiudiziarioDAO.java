package siap.sico.assistentegiudiziario.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: AssistenteGiudiziarioDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella AssistenteGiudiziario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AssistenteGiudiziarioDAO extends SIAPTableDAO
{
  public AssistenteGiudiziarioDAO (Connection con)
  {
    super(con);
    setTable("ASSISTENTE_GIUDIZIARIO");

     //Settare la Sequence e i campi chiave
    setSequenceField("ID_ASSISTENTE_GIUDIZIARIO","ASS_GIU_SEQ");
    this.setFieldKey("ID_ASSISTENTE_GIUDIZIARIO", BIG_DECIMAL);
    setField("ID_ASSISTENTE_GIUDIZIARIO", BIG_DECIMAL);
    setField("COGNOME", STRING);
    setField("NOME", STRING);
    setField("COD_UFFICIO_APPARTENENZA", STRING);
    setField("FLAG_STATO", STRING);
    setField("DATA_INIZIO_VALIDITA", DATE);
    setField("DATA_FINE_VALIDITA", DATE);
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

  public BigDecimal 	getIdAssistenteGiudiziario() 	throws DAOException	 { return getBigDecimal("ID_ASSISTENTE_GIUDIZIARIO"); }
  public String 		getCognome() 		        throws DAOException	 { return getString("COGNOME"); }
  public String 		getNome() 		        throws DAOException	 { return getString("NOME"); }
  public String 		getCodUfficioAppartenenza() 	throws DAOException	 { return getString("COD_UFFICIO_APPARTENENZA"); }
  public String 		getFlagStato() 		        throws DAOException	 { return getString("FLAG_STATO"); }
  public Date 	        getDataInizioValidita() 	throws DAOException	 { return getDate("DATA_INIZIO_VALIDITA"); }
  public Date 		getDataFineValidita() 		throws DAOException	 { return getDate("DATA_FINE_VALIDITA"); }
  public String 		getCodOperatoreInserimento() 	throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 		getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 		getCodUfficioInserimento() 	throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 		getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 		getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 		getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }


  //
  // METODI SET()
  //

  public void  	 setIdAssistenteGiudiziario(BigDecimal aValore) { setBigDecimal("ID_ASSISTENTE_GIUDIZIARIO", aValore); }
  public void  	 setCognome(String aValore ) 			{ setString("COGNOME", aValore); }
  public void  	 setNome(String aValore ) 			{ setString("NOME", aValore); }
  public void  	 setCodUfficioAppartenenza(String aValore ) 	{ setString("COD_UFFICIO_APPARTENENZA", aValore); }
  public void  	 setFlagStato(String aValore ) 			{ setString("FLAG_STATO", aValore); }
  public void  	 setDataInizioValidita(Date aValore ) 		{ setDate("DATA_INIZIO_VALIDITA", aValore); }
  public void  	 setDataFineValidita(Date aValore ) 		{ setDate("DATA_FINE_VALIDITA", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 	{ setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 		{ setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 	{ setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 	{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 		{ setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 	{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }


  public GenericModel getModel() throws DAOException
  {
    return new AssistenteGiudiziarioModel(
                                       getIdAssistenteGiudiziario() ,
                                       getCognome() ,
                                       getNome() ,
                                       getCodUfficioAppartenenza() ,
                                       "",
                                       getFlagStato() ,
                                       getDataInizioValidita() ,
                                       getDataFineValidita() ,
                                       getCodOperatoreInserimento() ,
                                       getDataInserimento() ,
                                       getCodUfficioInserimento() ,
                                       "",
                                       getCodOperatoreAggiornamento() ,
                                       getDataAggiornamento() ,
                                       getCodUfficioAggiornamento(),
                                        "");
  }


  public void   setDAOFromModel(AssistenteGiudiziarioModel aModel)
          throws DAOException
  {
     setIdAssistenteGiudiziario( aModel.getIdAssistenteGiudiziario() );
     setCognome( aModel.getCognome() );
     setNome( aModel.getNome() );
     setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
     setFlagStato( aModel.getFlagStato() );
     setDataInizioValidita( aModel.getDataInizioValidita() );
     setDataFineValidita( aModel.getDataFineValidita() );
     setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
     setDataInserimento( aModel.getDataInserimento() );
     setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
  }


  public void 	 setDAOFromModelForUpdate(AssistenteGiudiziarioModel aModel)
        throws DAOException
  {
    //setIdAssistenteGiudiziario( aModel.getIdAssistenteGiudiziario() );
    setCognome( aModel.getCognome() );
    setNome( aModel.getNome() );
    //setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
    setFlagStato( aModel.getFlagStato() );
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCondizioneUpdate(aModel.getIdAssistenteGiudiziario());
  }


  public void setCondizione(AssistenteGiudiziarioModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }


  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_ASSISTENTE_GIUDIZIARIO = " + key );
  }

}
