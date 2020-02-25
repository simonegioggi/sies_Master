package siap.siepe.relazione.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siepe.relazione.model.RelazioneModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: RelazioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Relazione</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RelazioneDAO extends TableDAO
{
	public RelazioneDAO (Connection aCon)
	{
    super(aCon);
    setTable("RELAZIONE");

    //Settare la Sequence e i campi chiave
    setSequenceField("ID_RELAZIONE", "REL_SEQ");

    setField("ID_RELAZIONE", BIG_DECIMAL);
    setField("ATT_ID_ATTIVITA", BIG_DECIMAL);
    setField("RIC_ID_RICHIESTA", BIG_DECIMAL);
    setField("NOTE", STRING);
    setField("DOC_BLOB", TBLOB);
    setField("DATA_EMISSIONE",DATE);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("FLAG_DOCUMENTO_REGISTRATO", STRING);
    setField("COD_UFFICIO_DESTINATARIO", STRING);
	}


  //
  // METODI GET()
  //
  public BigDecimal getIdRelazione() throws DAOException	 { return getBigDecimal("ID_RELAZIONE"); }
  public BigDecimal getAttIdAttivita() throws DAOException	 { return getBigDecimal("ATT_ID_ATTIVITA"); }
  public BigDecimal getRicIdRichiesta() throws DAOException	 { return getBigDecimal("RIC_ID_RICHIESTA"); }
  public String getNote() throws DAOException	 { return getString("NOTE"); }
  public ByteArrayOutputStream  getDocBlob() throws DAOException	  { return getBlob("DOC_BLOB"); }
  public Date getDataEmissione()    throws DAOException  { return getDate("DATA_EMISSIONE"); }
  public String getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public String getFlagDocumentoRegistrato() 		throws DAOException	 { return getString("FLAG_DOCUMENTO_REGISTRATO"); }
  public String getCodUfficioDestinatario() 	throws DAOException	 { return getString("COD_UFFICIO_DESTINATARIO"); }


  //
  // METODI SET()
  //
  public void setIdRelazione(BigDecimal aValore ) 			 { setBigDecimal("ID_RELAZIONE", aValore); }
  public void setAttIdAttivita(BigDecimal aValore ) 			 { setBigDecimal("ATT_ID_ATTIVITA", aValore); }
  public void setRicIdRichiesta(BigDecimal aValore ) 			 { setBigDecimal("RIC_ID_RICHIESTA", aValore); }
  public void setNote(String aValore ) 			 { setString("NOTE", aValore); }
  public void setDocBlob( ByteArrayInputStream aValore )      { setBlob("DOC_BLOB", aValore); }
  public void setDataEmissione(Date aValore)     { setDate("DATA_EMISSIONE", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setFlagDocumentoRegistrato(String aValore ) 			 { setString("FLAG_DOCUMENTO_REGISTRATO", aValore); }
  public void setCodUfficioDestinatario(String aValore ) 	         { setString("COD_UFFICIO_DESTINATARIO", aValore); }

  public GenericModel getModel() throws DAOException
  {
      RelazioneModel lRelMod = new RelazioneModel(
          getIdRelazione() ,
          getAttIdAttivita() ,
          getRicIdRichiesta(),
          getNote() ,
          getDataEmissione(),
          getCodOperatoreInserimento() ,
          getDataInserimento() ,
          getCodUfficioInserimento() ,
          "",
          getCodOperatoreAggiornamento() ,
          getDataAggiornamento() ,
          getCodUfficioAggiornamento() ,
          "",
          getFlagDocumentoRegistrato(),
          getCodUfficioDestinatario(),
          "", // DescrUfficioDestinatario
          ""  // DescrSedeUfficioDestinatario
          );
    // lRelMod.setDocBlobOut(this.getDocBlob());
     return lRelMod;
   }

   public GenericModel getModelWithBlob() throws DAOException
   {
     RelazioneModel lRelMod = (RelazioneModel) getModel();
     lRelMod.setDocBlobOut(this.getDocBlob());
     return lRelMod;
   }

   public void setDAOFromModel(RelazioneModel aModel) throws DAOException
   {
      setIdRelazione( aModel.getIdRelazione() );
      setAttIdAttivita( aModel.getAttIdAttivita() );
      setRicIdRichiesta( aModel.getRicIdRichiesta() );
      setNote( aModel.getNote() );
      setDocBlob( aModel.getDocBlobIn() );
      setDataEmissione( aModel.getDataEmissione());
      setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
      setDataInserimento( aModel.getDataInserimento() );
      setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setFlagDocumentoRegistrato( aModel.getFlagDocumentoRegistrato() );
      setCodUfficioDestinatario(aModel.getCodUfficioDestinatario());
   }

   public void setDAOFromModelForUpdate(RelazioneModel aModel) throws DAOException
   {
      setIdRelazione( aModel.getIdRelazione() );
      setAttIdAttivita( aModel.getAttIdAttivita() );
      setRicIdRichiesta( aModel.getRicIdRichiesta() );
      setNote( aModel.getNote() );
      setDataEmissione( aModel.getDataEmissione());
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setFlagDocumentoRegistrato( aModel.getFlagDocumentoRegistrato() );
      setCodUfficioDestinatario(aModel.getCodUfficioDestinatario());
      setCondizioneUpdate(aModel.getIdRelazione());
   }

   /**
    * Imposta le condizioni di filtro attraverso i dati utili
    * inseriti nel Model.
    * <p>
    * @param aModel RelazioneModel Model con i dati di condizione.
    */
   public void setCondizione(RelazioneModel aModel)
   {
     String lCondizioni = new String();
     boolean lInserito = false;

    // Attivita
    if (aModel.getAttIdAttivita() != null && aModel.getAttIdAttivita().doubleValue() != 0 )
    {
      lCondizioni += ( lInserito ? " AND " : " " ) + " ATT_ID_ATTIVITA = " + aModel.getAttIdAttivita();
      lInserito = true;
    }
    // Richiesta
    if (aModel.getRicIdRichiesta() != null && aModel.getRicIdRichiesta().doubleValue() != 0 )
    {
      lCondizioni += ( lInserito ? " AND " : " " ) + " RIC_ID_RICHIESTA = " + aModel.getRicIdRichiesta();
      lInserito = true;
    }
    setCondition(lCondizioni);
   }

   public void setCondizioneByIdAttivita(BigDecimal key)
   {
     setCondition(" ATT_ID_ATTIVITA = " + key );
   }

   /**
    * Imposta il filtro di ricerca per l'IDRichiesta
    * <p>
    * @param key BigDecimal Chiave ID Richiesta
    */
   public void setCondizioneByIdRichiesta(BigDecimal key)
   {
     setCondition(" RIC_ID_RICHIESTA = " + key );
   }

   public void setCondizioneUpdate(BigDecimal key)
   {
     setCondition(" ID_RELAZIONE = " + key );
   }
}
