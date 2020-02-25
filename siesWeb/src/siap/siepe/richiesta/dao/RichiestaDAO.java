package siap.siepe.richiesta.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siepe.richiesta.model.RichiestaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: RichiestaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Richiesta</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class RichiestaDAO extends TableDAO
{
  /**
   * Costruttore di classe con parametro.
   * <p>
   * @param con Connection parametro che contine la connessione al dbase.
   */
  public RichiestaDAO (Connection con)
  {
    super(con);
    setTable("RICHIESTA");

    setSequenceField("ID_RICHIESTA", "RIC_SIEPE_SEQ");

    setFieldKey("ID_RICHIESTA", BIG_DECIMAL);

    setField("DATA_RICHIESTA", DATE);
    setField("COD_TIPO_RICHIESTA", STRING);
    setField("COD_TIPO_RICHIEDENTE", STRING);
    setField("NOTE", STRING);
    setField("DOC_BLOB", TBLOB);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("FAS_SIE_ID_FAS_SIEPE", BIG_DECIMAL);
    setField("FLAG_DOCUMENTO_REGISTRATO", STRING);
    setField("COD_UFFICIO_DESTINATARIO", STRING);
  }

  //
  // METODI GET()
  //
  public BigDecimal getIdRichiesta() 		throws DAOException	 { return getBigDecimal("ID_RICHIESTA"); }
  public Date getDataRichiesta() 		throws DAOException	 { return getDate("DATA_RICHIESTA"); }
  public String getCodTipoRichiesta() 		throws DAOException	 { return getString("COD_TIPO_RICHIESTA"); }
  public String getCodTipoRichiedente() 	throws DAOException	 { return getString("COD_TIPO_RICHIEDENTE"); }
  public String getNote() 		        throws DAOException	 { return getString("NOTE"); }
//public Blob 		 getDocBlob() 		throws DAOException	 { return getBlob("DOC_BLOB"); }

  public ByteArrayOutputStream getDocBlob() 	throws DAOException	 { return getBlob("DOC_BLOB"); }
  public String getCodOperatoreInserimento() 	throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 	getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String getCodUfficioInserimento() 	throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal getFasSieIdFasSiepe() 	throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FAS_SIEPE"); }
  public String getFlagDocumentoRegistrato() 		throws DAOException	 { return getString("FLAG_DOCUMENTO_REGISTRATO"); }
  public String getCodUfficioDestinatario() 	throws DAOException	 { return getString("COD_UFFICIO_DESTINATARIO"); }

  //
  // METODI SET()
  //
  public void setIdRichiesta(BigDecimal aValore ) 			 { setBigDecimal("ID_RICHIESTA", aValore); }
  public void setDataRichiesta(Date aValore ) 			         { setDate("DATA_RICHIESTA", aValore); }
  public void setCodTipoRichiesta(String aValore ) 			 { setString("COD_TIPO_RICHIESTA", aValore); }
  public void setCodTipoRichiedente(String aValore ) 			 { setString("COD_TIPO_RICHIEDENTE", aValore); }
  public void setNote(String aValore ) 			                 { setString("NOTE", aValore); }
  //public void  setDocBlob(Blob aValore ) 			         { setBlob("DOC_BLOB", aValore); }
  public void setDocBlob( ByteArrayInputStream aValore )      { setBlob("DOC_BLOB", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 	         { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 		 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 		 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 		 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setFasSieIdFasSiepe(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FAS_SIEPE", aValore); }
  public void setFlagDocumentoRegistrato(String aValore ) 			         { setString("FLAG_DOCUMENTO_REGISTRATO", aValore); }
  public void setCodUfficioDestinatario(String aValore ) 	         { setString("COD_UFFICIO_DESTINATARIO", aValore); }

  /**
   * Metodo che ritorna un  GenriModel, contente i dati prelevati dalla tupla.
   * <p>
   * @throws DAOException propaga errore di eccezione.
   * @return GenericModel Instanza Model autocasting al padre di RichiestaModel.
   */
  public GenericModel getModel() throws DAOException
  {

    return new RichiestaModel(  getIdRichiesta() ,
                                getDataRichiesta() ,
                                getCodTipoRichiesta() ,
                                "",  // DescrTipoRichiesta
                                getCodTipoRichiedente() ,
                                "",  // DescrTipoRichiedente
                                getNote() ,
                                /*getDocBlob() ,*/
                                getCodOperatoreInserimento() ,
                                getDataInserimento() ,
                                getCodUfficioInserimento() ,
                                "",  // DescrUfficioInserimento
                                getCodOperatoreAggiornamento() ,
                                getDataAggiornamento(),
                                getCodUfficioAggiornamento(),
                                "",  // DescrUfficioAggiornamento
                                getFasSieIdFasSiepe(),
                                getFlagDocumentoRegistrato(),
                                getCodUfficioDestinatario(),
                                "", // DescrUfficioDestinatario
                                ""  // DescrSedeUfficioDestinatario
                                );
  }

  /**
   * Metodo che popola il dao con i dati cotenuti nel model.
   * <p>
   * @param aModel RichiestaModel model passato come parametro.
   * @throws DAOException propaga errore di eccezione.
   */
  public void setDAOFromModel(RichiestaModel aModel) throws DAOException
  {
    setIdRichiesta( aModel.getIdRichiesta() );
    setDataRichiesta( aModel.getDataRichiesta() );
    setCodTipoRichiesta( aModel.getCodTipoRichiesta() );
    setCodTipoRichiedente( aModel.getCodTipoRichiedente() );
    setNote( aModel.getNote() );
    //setDocBlob( aModel.getDocBlob() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento(aModel.getCodUfficioInserimento());
    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
    setDataAggiornamento(aModel.getDataAggiornamento());
    setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
    setFasSieIdFasSiepe(aModel.getFasSieIdFasSiepe());
    setFlagDocumentoRegistrato(aModel.getFlagDocumentoRegistrato());
    setCodUfficioDestinatario(aModel.getCodUfficioDestinatario());
  }

  /**
   * Imposta il DAO con i dati prelevati dal Model.
   * <p>
   * @param aModel RichiestaModel
   * @throws DAOException propaga errore di eccezione.
   */
  public void setDAOFromModelForUpdate(RichiestaModel aModel) throws DAOException
  {
    //setIdRichiesta(aModel.getIdRichiesta());
    setDataRichiesta(aModel.getDataRichiesta());
    setCodTipoRichiesta(aModel.getCodTipoRichiesta());
    setCodTipoRichiedente(aModel.getCodTipoRichiedente());
    setNote(aModel.getNote());
    //setDocBlob(aModel.getDocBlob());
    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
    setDataAggiornamento(aModel.getDataAggiornamento());
    setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
    //setFasSieIdFasSiepe(aModel.getFasSieIdFasSiepe());
    //setFlagDocumentoRegistrato(aModel.getFlagDocumentoRegistrato());
    setCodUfficioDestinatario(aModel.getCodUfficioDestinatario());
    setCondizioneUpdate(aModel.getIdRichiesta());
  }

  /**
   * Imposta la condizione di filtro, dipendente dai valori impostati nel model
   * <p>
   * @param aModel RichiestaModel Model conente i valori per le condizioni di filtro.
   */
  public void setCondizione(RichiestaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  /**
   * Imposta la condizione di UPDATE, passando come parametro l'id della chiave
   * afferente al record interessato.
   * <p>
   * @param key BigDecimal Id chiave record.
   */
  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition( " ID_RICHIESTA = " + key );
  }

  public void 	 setDAOFromModelForUpdateBlob(RichiestaModel aModel) throws DAOException
  {
     setDocBlob( aModel.getDocBlobIn() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setFlagDocumentoRegistrato( aModel.getFlagDocumentoRegistrato() );

    setCondizioneUpdate(aModel.getIdRichiesta());
  }

}
