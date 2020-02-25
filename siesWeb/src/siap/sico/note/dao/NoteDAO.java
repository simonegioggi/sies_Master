package siap.sico.note.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sico.note.model.NoteModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: NoteDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Note</p>
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class NoteDAO extends SIAPTableDAO
{
	/**
   * Costruttore di classe con argomento.
   * <p>
   * @param aConn connessione al dbase.
   */
  public NoteDAO (Connection aConn)
  {
    super(aConn);
    setTable("NOTE");
    setSequenceField("ID_NOTE", "NOTE_SEQ");
    setFieldKey("ID_NOTE", BIG_DECIMAL);
    setField("ID_NOTE", BIG_DECIMAL);
    setField("DATA", DATE);
    setField("DESCRIZIONE", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("FAS_SIGE_ID_FASCICOLO_SIGE", BIG_DECIMAL);
  }

  //
  // METODI GET()
  //
  public BigDecimal 	getIdNote() 		           throws DAOException	 { return getBigDecimal("ID_NOTE"); }
  public Date 			getData() 		               throws DAOException	 { return getDate("DATA"); }
  public String 		getDescrizione()               throws DAOException	 { return getString("DESCRIZIONE"); }
  public String 		getCodOperatoreInserimento()   throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 			getDataInserimento() 		   throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 		getCodUfficioInserimento() 	   throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public BigDecimal 	getFasSiuIdFascicoloSius()     throws DAOException	 { return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"); }
  public BigDecimal 	getFasSieIdFascicoloSiep()     throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public BigDecimal 	getFasSigeIdFascicoloSige()    throws DAOException	 { return getBigDecimal("FAS_SIGE_ID_FASCICOLO_SIGE"); }
  
  //
  // METODI SET()
  //
  public void  	 setIdNote(BigDecimal aValore ) 			      { setBigDecimal("ID_NOTE", aValore); }
  public void  	 setData(Date aValore ) 			              { setDate("DATA", aValore); }
  public void  	 setDescrizione(String aValore )                  { setString("DESCRIZIONE", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 	  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			      { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 		  { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setFasSiuIdFascicoloSius(BigDecimal aValore )    { setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore); }
  public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore )    { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void  	 setFasSigeIdFascicoloSige(BigDecimal aValore )   { setBigDecimal("FAS_SIGE_ID_FASCICOLO_SIGE", aValore); }
  
  /**
   * Ritorna il model popolato con il dati prelevati dal Dbase.
   * <p>
   * @return model <code>GenericModel</code> popolato.
   * @throws DAOException propaga l'errore di eccezione.
   */
	public GenericModel getModel() throws DAOException
  {
    return new NoteModel(
      getIdNote() ,
      getData() ,
      getDescrizione() ,
      getCodOperatoreInserimento() ,
      getDataInserimento() ,
      getCodUfficioInserimento() ,
      "",
      getFasSieIdFascicoloSiep(),
      getFasSiuIdFascicoloSius(),
      getFasSigeIdFascicoloSige());
  }

  /**
   * Imposta i dati del model con quelli passati come argomento.
   * <p>
   * @param aModel model con i dati utili per impostare il model stesso.
   * @throws DAOException propage l'errore di eccezione.
   */
  public void setDAOFromModel(NoteModel aModel) throws DAOException
  {
     setIdNote( aModel.getIdNote() );
     setData( aModel.getData() );
     setDescrizione( aModel.getDescrizione() );
     setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
     setDataInserimento( aModel.getDataInserimento() );
     setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
     setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
     setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
     setFasSigeIdFascicoloSige( aModel.getFasSigeIdFascicoloSige() );
  }

  /**
   * Imposta i dati del model con quelli passati come argomento.
   * per fase di update.
   * <p>
   * @param aModel model con i dati utili per impostare il model stesso.
   * @throws DAOException propage l'errore di eccezione.
   */
  public void 	setDAOFromModelForUpdate(NoteModel aModel) throws DAOException
  {
    setIdNote( aModel.getIdNote() );
    setData( aModel.getData() );
    setDescrizione( aModel.getDescrizione() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setFasSigeIdFascicoloSige( aModel.getFasSigeIdFascicoloSige() ); 
    setCondizioneUpdate(aModel.getIdNote());
  }


  public void setCondizione(NoteModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  /**
   * Imposta la condizione per l'update dei dati.
   * <p>
   * @param key chaive id del record da aggiornare.
   */
   public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_NOTE = " + key );
  }

  /**
   * Imposta la condizione al ID Fascicolo SIUS.
   * <p>
   * @param key chiave id_Fascicolo_Sius.
   */
   public void setCondizioneFascicoloSius(BigDecimal aIdFascicoloSius )
  {
    setCondition(" FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFascicoloSius );
  }

   /**
    * Imposta la condizione al ID Fascicolo SIGE.
    * <p>
    * @param key chiave id_Fascicolo_Sius.
    */
    public void setCondizioneFascicoloSige(BigDecimal aIdFascicoloSige )
   {
     setCondition(" FAS_SIGE_ID_FASCICOLO_SIGE = " + aIdFascicoloSige );
   }

}
