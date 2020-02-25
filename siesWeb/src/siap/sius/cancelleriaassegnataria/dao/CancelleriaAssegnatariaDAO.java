package siap.sius.cancelleriaassegnataria.dao;

import java.sql.Connection;
import java.util.Date;

import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: CancelleriaAssegnatariaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella CancelleriaAssegnataria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class CancelleriaAssegnatariaDAO extends TableDAO
{
	public CancelleriaAssegnatariaDAO (Connection con)
	{
         super(con);
         setTable("CANCELLERIA_ASSEGNATARIA");

          //Settare la Sequence e i campi chiave

          setField("COD_CANCELLERIA_ASSEGNATARIA", STRING);
          setField("COD_UFFICIO", STRING);
          setField("DESC_CANCELLERIA_ASSEGNATARIA", STRING);
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

  public String 	getCodCancelleriaAssegnataria() 		throws DAOException	 { return getString("COD_CANCELLERIA_ASSEGNATARIA"); }
  public String 	getCodUfficio() 		throws DAOException	 { return getString("COD_UFFICIO"); }
  public String 	getDescCancelleriaAssegnataria() 		throws DAOException	 { return getString("DESC_CANCELLERIA_ASSEGNATARIA"); }
  public String 	getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 	getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 	getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 	getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 	getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 	getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }


  //
  // METODI SET()
  //

  public void  	 setCodCancelleriaAssegnataria(String aValore ) 			 { setString("COD_CANCELLERIA_ASSEGNATARIA", aValore); }
  public void  	 setCodUfficio(String aValore ) 			 { setString("COD_UFFICIO", aValore); }
  public void  	 setDescCancelleriaAssegnataria(String aValore ) 			 { setString("DESC_CANCELLERIA_ASSEGNATARIA", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }

  /**
   * La funzione costruisce il model della CancelleriaAssegnataria a partire dal record corrente in tabella.
   * @return CancelleriaAssegnatariaModel
   * @throws DAOException
   */
  public GenericModel getModel() throws DAOException
  {
     return new CancelleriaAssegnatariaModel(
                         getCodCancelleriaAssegnataria() ,
                         getDescCancelleriaAssegnataria() ,
                         getCodUfficio() ,
                         "",
                         getCodOperatoreInserimento() ,
                         getDataInserimento() ,
                         getCodUfficioInserimento() ,
                         "",
                         getCodOperatoreAggiornamento() ,
                         getDataAggiornamento() ,
                         getCodUfficioAggiornamento(),
                         ""
                            );
   }
   /**
    * Funzione di inserimento: inserisce in tabella i dati ricevuti dal model passato come argomento.
    * @param aModel: CancelleriaAssegnatariaModel
    * @throws DAOException
    */
   public void 	 setDAOFromModel(CancelleriaAssegnatariaModel aModel) throws DAOException
   {
      setCodCancelleriaAssegnataria( aModel.getCodCancelleriaAssegnataria() );
      setCodUfficio( aModel.getCodUfficio() );
      setDescCancelleriaAssegnataria( aModel.getDescCancelleriaAssegnataria() );
      setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
      setDataInserimento( aModel.getDataInserimento() );
      setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
   }

   /**
    * Funzione di aggiornamento dei dati in tabella.
    * L'unico campo che può essere aggiornato è la "Descrizione della Cancelleria Assegnataria"; la selezione del record o dei record che verranno aggiornati è stabilita dal "Codice Cancelleria" e dal "Codice Ufficio" passati nel model.
    * @param aModel: CancelleriaAssegnatariaModel
    * @throws DAOException
    */
    public void 	 setDAOFromModelForUpdate(CancelleriaAssegnatariaModel aModel) throws DAOException
    {
       setDescCancelleriaAssegnataria( aModel.getDescCancelleriaAssegnataria() );
       setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
       setDataAggiornamento( aModel.getDataAggiornamento() );
       setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
       setCondizione(aModel.getCodCancelleriaAssegnataria(), aModel.getCodUfficio());
   }


   /**
    * La funzione valorizza la condizione di filtro attraverso la quale si effettueà un aggiornamento della tabella.
    * La chiave che individua univocamente un record in tabella è costituita da:  COD_CANCELLERIA_ASSEGNATARIA, COD_UFFICIO.
    * Nel chiamare tale funzione bisogna che almeno uno dei parametri di chiave siano valorizzati; in caso contrario viene lanciata una eccezione.
    * @param aCodPosizioneMateriale
    * @param aCodUfficio
    * @throws DAOException
    */
   public void setCondizione(String aCodCancelleriaAssegnataria, String aCodUfficio) throws DAOException
   {
     if (aCodCancelleriaAssegnataria == null ||  aCodCancelleriaAssegnataria.trim().length() < 1 || aCodUfficio == null || aCodUfficio.trim().length() < 1 )
       throw new DAOException("errore nelle condizioni di filtro");
     setCondition("COD_CANCELLERIA_ASSEGNATARIA= '" + aCodCancelleriaAssegnataria + "'" + " AND COD_UFFICIO = '" + aCodUfficio + "'") ;
    }
}
