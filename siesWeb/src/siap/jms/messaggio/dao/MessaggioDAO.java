package siap.jms.messaggio.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.jms.messaggio.model.MessaggioModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
* <p>Title: MessaggioDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Messaggio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class MessaggioDAO extends TableDAO
{
  public MessaggioDAO(Connection con)
  {
    super(con);
    setTable("messaggio");

    //Settare la Sequence e i campi chiave

    this.setSequenceField("ID_MESSAGGIO", "MESS_SEQ");
    this.setFieldKey("ID_MESSAGGIO", BIG_DECIMAL);
    setField("ID_MESSAGGIO", BIG_DECIMAL);
    setField("COD_TIPO_MESSAGGIO", STRING);
    setField("COD_TIPO_OPERAZIONE", STRING);
    setField("COD_UFFICIO_MITTENTE", STRING);
    setField("COD_BDI_MITTENTE", STRING);
    setField("COD_UFFICIO_DESTINATARIO", STRING);
    setField("COD_BDI_DESTINATARIA", STRING);
    setField("DATA_INVIO", DATE);
    setField("DATA_ESITO", DATE);
    setField("CODICE_UTENTE_MITTENTE", STRING);
    setField("COD_ESITO", STRING);
    setField("JMS_ID_MESSAGE", STRING);
    setField("JMS_CORRELATION_ID_MESSAGE", STRING);
    setField("FLAG_VISTO", STRING);
    setField("BLOB_ESITO", TBLOB);
    setField("CHIAVE_ANNO_SIEP", BIG_DECIMAL);
    setField("CHIAVE_PROGR_SIEP", BIG_DECIMAL);
    setField("CHIAVE_UFFICIO_SIEP", STRING);
    
    setField("CHIAVE_ANNO_SIUS", BIG_DECIMAL);
    setField("CHIAVE_PROGR_SIUS", BIG_DECIMAL);
    //  UEPE
    setField("CHIAVE_ANNO_SIEPE", BIG_DECIMAL);
    setField("CHIAVE_PROGR_SIEPE", BIG_DECIMAL);
    setField("NOME_SOGGETTO", STRING);
    setField("COGNOME_SOGGETTO", STRING);
    setField("DATA_NASCITA", DATE);
    setField("COD_STATO_NASCITA", STRING);
    setField("COD_COMUNE_NASCITA", STRING);
    //SIEP - Trasmissione per competenza
    setField("CHIAVE_ANNO_FAS_CUMULANTE", BIG_DECIMAL);
    setField("CHIAVE_PROGR_FAS_CUMULANTE", BIG_DECIMAL);
    setField("CHIAVE_UFFICIO_FAS_CUMULANTE", STRING);
    
    
    setField("NOTE", STRING);
    
    setField("DELIVERY_MODE", STRING);
    setField("COD_UFFICIO_INOLTRO", STRING);
    setField("COD_BDI_INOLTRO", STRING);
    setField("COD_UFFICIO_REPLY_TO", STRING);
    setField("COD_BDI_REPLY_TO", STRING);
    setField("JMS_CORRELATION_REPLY_TO", STRING);
    setField("ID_MESSAGGIO_SOLLECITATO", STRING);
    
    setField("ID_RICHIESTA", BIG_DECIMAL);
    
  }

  //
  // METODI GET()
  //

  public BigDecimal getIdMessaggio() 		throws DAOException	            { return getBigDecimal("ID_MESSAGGIO"); }
  public String 	  getTipoMessaggio() 		throws DAOException	          { return getString("COD_TIPO_MESSAGGIO"); }
  public String 	  getTipoOperazione() 		throws DAOException	        { return getString("COD_TIPO_OPERAZIONE"); }
  public String 	  getCodUfficioMittente() 	throws DAOException	      { return getString("COD_UFFICIO_MITTENTE"); }
  public String 	  getCodBdiMittente() 		throws DAOException	        { return getString("COD_BDI_MITTENTE"); }
  public String 	  getCodUfficioDestinatario() 	throws DAOException	  { return getString("COD_UFFICIO_DESTINATARIO"); }
  public String 	  getCodBdiDestinataria() 	throws DAOException	      { return getString("COD_BDI_DESTINATARIA"); }
  public Date 		  getDataInvio() 		throws DAOException	              { return getDate("DATA_INVIO"); }
  public Date 		  getDataEsito() 		throws DAOException	              { return getDate("DATA_ESITO"); }
  public String 	  getCodiceUtenteMittente() 	throws DAOException	    { return getString("CODICE_UTENTE_MITTENTE"); }
  public String 	  getCodEsito() 		        throws DAOException	      { return getString("COD_ESITO"); }
  public String     getJmsIdMessage() 		throws DAOException	          { return getString("JMS_ID_MESSAGE"); }
  public String     getJmsCorrelationIdMessage() 		throws DAOException	{ return getString("JMS_CORRELATION_ID_MESSAGE"); }
  public String     getFlagVisto() 		throws DAOException	              { return getString("FLAG_VISTO"); }
  public BigDecimal getChiaveAnnoSiep() 		throws DAOException	        { return getBigDecimal("CHIAVE_ANNO_SIEP"); }
  public BigDecimal getChiaveProgrSiep() 		throws DAOException	        { return getBigDecimal("CHIAVE_PROGR_SIEP"); }
  public String     getChiaveUfficioSiep()    throws DAOException       { return getString("CHIAVE_UFFICIO_SIEP"); }
  
  public BigDecimal getChiaveAnnoSius() 		throws DAOException	        { return getBigDecimal("CHIAVE_ANNO_SIUS"); }
  public BigDecimal getChiaveProgrSius() 		throws DAOException	        { return getBigDecimal("CHIAVE_PROGR_SIUS"); }
  //  UEPE
  public BigDecimal getChiaveAnnoSiepe() 		throws DAOException	        { return getBigDecimal("CHIAVE_ANNO_SIEPE"); }
  public BigDecimal getChiaveProgrSiepe()		throws DAOException	        { return getBigDecimal("CHIAVE_PROGR_SIEPE"); }
  public String 	  getCognomeSoggetto()    throws DAOException	        { return getString("COGNOME_SOGGETTO"); }
  public String 	  getNomeSoggetto()       throws DAOException	        { return getString("NOME_SOGGETTO"); }
  public Date   	  getDataNascita()        throws DAOException	        { return getDate("DATA_NASCITA"); }
  public String 	  getCodStatoNascita()    throws DAOException	        { return getString("COD_STATO_NASCITA"); }
  public String 	  getCodComuneNascita()   throws DAOException	        { return getString("COD_COMUNE_NASCITA"); }
  
  public BigDecimal getChiaveAnnoFasCumulante() throws DAOException	    { return getBigDecimal("CHIAVE_ANNO_FAS_CUMULANTE");};
  public BigDecimal getChiaveProgrFasCumulante() throws DAOException	  { return getBigDecimal("CHIAVE_PROGR_FAS_CUMULANTE");};
  public String     getChiaveUfficioFasCumulante() throws DAOException  { return getString("CHIAVE_UFFICIO_FAS_CUMULANTE"); }
  
  
  
  public String 	  getNote()   throws DAOException	        { return getString("NOTE"); }

  
  public String     getDeliveryMode()           throws DAOException  { return getString("DELIVERY_MODE"); }
  public String     getCodUfficioInoltro()      throws DAOException  { return getString("COD_UFFICIO_INOLTRO"); }
  public String     getCodBdiInoltro()          throws DAOException  { return getString("COD_BDI_INOLTRO"); }
  public String     getCodUfficioReplyTo()      throws DAOException  { return getString("COD_UFFICIO_REPLY_TO"); }
  public String     getCodBdiReplyTo()          throws DAOException  { return getString("COD_BDI_REPLY_TO"); }
  public String     getJmsCorrelationReplyTo()  throws DAOException  { return getString("JMS_CORRELATION_REPLY_TO"); }
  public String     getIdMessaggioSollecitato() throws DAOException  { return getString("ID_MESSAGGIO_SOLLECITATO"); }

  public BigDecimal getIdRichiesta() 			throws DAOException  { return getBigDecimal("ID_RICHIESTA"); }
  
	//	public Blob 		       getBlobEsito() 		throws DAOException	 { return getBlob("BLOB_ESITO"); }
  public ByteArrayOutputStream    getBlobEsito() 	throws DAOException	     { return getBlob("BLOB_ESITO"); }

  //
  // METODI SET()
  //

  public void setIdMessaggio(BigDecimal aValore ) 		    { setBigDecimal("ID_MESSAGGIO", aValore); }
  public void setTipoMessaggio(String aValore ) 		      { setString("COD_TIPO_MESSAGGIO", aValore); }
  public void setTipoOperazione(String aValore ) 		      { setString("COD_TIPO_OPERAZIONE", aValore); }
  public void setCodUfficioMittente(String aValore ) 	    { setString("COD_UFFICIO_MITTENTE", aValore); }
  public void setCodBdiMittente(String aValore ) 		      { setString("COD_BDI_MITTENTE", aValore); }
  public void setCodUfficioDestinatario(String aValore ) 	{ setString("COD_UFFICIO_DESTINATARIO", aValore); }
  public void setCodBdiDestinataria(String aValore ) 	    { setString("COD_BDI_DESTINATARIA", aValore); }
  public void setDataInvio(Date aValore ) 			          { setDate("DATA_INVIO", aValore); }
  public void setDataEsito(Date aValore ) 			          { setDate("DATA_ESITO", aValore); }
  public void setCodiceUtenteMittente(String aValore ) 	  { setString("CODICE_UTENTE_MITTENTE", aValore); }
  public void setCodEsito(String aValore ) 			          { setString("COD_ESITO", aValore); }
//	public void  	 setBlobEsito(Blob aValore ) 		 { setBlob("BLOB_ESITO", aValore); }
  public void setBlobEsito( ByteArrayInputStream aValore) { setBlob("BLOB_ESITO", aValore); }
  public void setJmsIdMessage(String aValore ) 		        { setString("JMS_ID_MESSAGE", aValore); }
  public void setJmsCorrelationIdMessage(String aValore ) { setString("JMS_CORRELATION_ID_MESSAGE", aValore); }
  public void setFlagVisto(String aValore ) 		          { setString("FLAG_VISTO", aValore); }
  public void setChiaveAnnoSiep(BigDecimal aValore ) 			{ setBigDecimal("CHIAVE_ANNO_SIEP", aValore); }
  public void setChiaveProgrSiep(BigDecimal aValore ) 		{ setBigDecimal("CHIAVE_PROGR_SIEP", aValore); }
  public void setChiaveUfficioSiep(String aValore )       { setString("CHIAVE_UFFICIO_SIEP", aValore); }
  
  
  public void setChiaveAnnoSius(BigDecimal aValore ) 			{ setBigDecimal("CHIAVE_ANNO_SIUS", aValore); }
  public void setChiaveProgrSius(BigDecimal aValore ) 		{ setBigDecimal("CHIAVE_PROGR_SIUS", aValore); }
  //  UEPE
  public void setChiaveAnnoSiepe(BigDecimal aValore ) 		{ setBigDecimal("CHIAVE_ANNO_SIEPE", aValore); }
  public void setChiaveProgrSiepe(BigDecimal aValore ) 		{ setBigDecimal("CHIAVE_PROGR_SIEPE", aValore); }
  public void setCognomeSoggetto(String aValore ) 		    { setString("COGNOME_SOGGETTO", aValore); }
  public void setNomeSoggetto(String aValore ) 		        { setString("NOME_SOGGETTO", aValore); }
  public void setDataNascita(Date aValore )			          { setDate("DATA_NASCITA", aValore); }
  public void setCodStatoNascita(String aValore )	        { setString("COD_STATO_NASCITA", aValore); }
  public void setCodComuneNascita(String aValore )        { setString("COD_COMUNE_NASCITA", aValore); }
  
  public void setChiaveFasAnnoCumulante  (BigDecimal aValore ) { setBigDecimal("CHIAVE_ANNO_FAS_CUMULANTE", aValore);};
  public void setChiaveFasProgrCumulante (BigDecimal aValore ) { setBigDecimal("CHIAVE_PROGR_FAS_CUMULANTE", aValore);};
  public void setChiaveFasUfficioCumulante (String aValore )       { setString("CHIAVE_UFFICIO_FAS_CUMULANTE", aValore); }

  
  public void setDeliveryMode          (String aValore)  { setString("DELIVERY_MODE", aValore); }
  public void setCodUfficioInoltro     (String aValore)  { setString("COD_UFFICIO_INOLTRO", aValore); }
  public void setCodBdiInoltro         (String aValore)  { setString("COD_BDI_INOLTRO", aValore); }
  public void setCodUfficioReplyTo     (String aValore)  { setString("COD_UFFICIO_REPLY_TO", aValore); }
  public void setCodBdiReplyTo         (String aValore)  { setString("COD_BDI_REPLY_TO", aValore); }
  public void setJmsCorrelationReplyTo (String aValore)  { setString("JMS_CORRELATION_REPLY_TO", aValore); }
  public void setIdMessaggioSollecitato(String aValore)  { setString("ID_MESSAGGIO_SOLLECITATO", aValore); }
  
  public void setIdRichiesta(BigDecimal aValore ) 		    { setBigDecimal("ID_RICHIESTA", aValore); }
  public void setNote(String aValore ) 						{ setString("NOTE", aValore);};


  public GenericModel getModel() throws DAOException
  {
    MessaggioModel lModel = new MessaggioModel();

    lModel.setIdMessaggio(this.getIdMessaggio());
    lModel.setCodTipoMessaggio(this.getTipoMessaggio());
    lModel.setCodTipoOperazione(this.getTipoOperazione());
    lModel.setCodUfficioMittente(this.getCodUfficioMittente());
    lModel.setCodBdiMittente(this.getCodBdiMittente());
    lModel.setCodUfficioDestinatario(this.getCodUfficioDestinatario());
    lModel.setCodBdiDestinataria(this.getCodBdiDestinataria());
    lModel.setDataInvio(this.getDataInvio());
    lModel.setDataEsito(this.getDataEsito());
    lModel.setCodiceUtenteMittente(this.getCodiceUtenteMittente());
    lModel.setCodEsito(this.getCodEsito());
    lModel.setJmsIdMessaggio(this.getJmsIdMessage());
    lModel.setFlagVisto(this.getFlagVisto());
    lModel.setBlobOut(this.getBlobEsito());
    lModel.setChiaveAnnoSiep(this.getChiaveAnnoSiep());
    lModel.setChiaveProgrSiep(this.getChiaveProgrSiep());
    lModel.setChiaveUfficioSiep(this.getChiaveUfficioSiep());
    
    lModel.setChiaveAnnoSius(this.getChiaveAnnoSius());
    lModel.setChiaveProgrSius(this.getChiaveProgrSius());
    // UEPE
    lModel.setChiaveAnnoSiepe(this.getChiaveAnnoSiepe());
    lModel.setChiaveProgrSiepe(this.getChiaveProgrSiepe());
    lModel.setCognomeSoggetto(this.getCognomeSoggetto());
    lModel.setNomeSoggetto(this.getNomeSoggetto());
    lModel.setDataNascita(this.getDataNascita());
    lModel.setCodStatoNascita(this.getCodStatoNascita());
    lModel.setCodComuneNascita(this.getCodComuneNascita());
    
    lModel.setChiaveAnnoFasCumulante(this.getChiaveAnnoFasCumulante());
    lModel.setChiaveProgrFasCumulante(this.getChiaveProgrFasCumulante());
    lModel.setChiaveUfficioFasCumulante(this.getChiaveUfficioFasCumulante());
    lModel.setNote(this.getNote());
    
    lModel.setDeliveryMode          (this.getDeliveryMode());    
    lModel.setCodUfficioInoltro     (this.getCodUfficioInoltro());
    lModel.setCodBdiInoltro         (this.getCodBdiInoltro());    
    lModel.setCodUfficioReplyTo     (this.getCodUfficioReplyTo());
    lModel.setCodBdiReplyTo         (this.getCodBdiReplyTo());    
    lModel.setJmsCorrelationReplyTo (this.getJmsCorrelationReplyTo());
    lModel.setIdMessaggioSollecitato(this.getIdMessaggioSollecitato());
    lModel.setIdRichiesta			(this.getIdRichiesta());

    return lModel;
	}


  public void setDAOFromModel(MessaggioModel aModel) throws Exception
  {
    //---setIdMessaggio(aModel.getIdMessaggio());
    setTipoMessaggio(aModel.getCodTipoMessaggio());
    setTipoOperazione(aModel.getCodTipoOperazione());
    setCodUfficioMittente(aModel.getCodUfficioMittente());
    setCodBdiMittente(aModel.getCodBdiMittente());
    setCodUfficioDestinatario(aModel.getCodUfficioDestinatario());
    setCodBdiDestinataria(aModel.getCodBdiDestinataria());
    setDataInvio(aModel.getDataInvio());
    setDataEsito(aModel.getDataEsito());
    setCodiceUtenteMittente(aModel.getCodiceUtenteMittente());
    setCodEsito(aModel.getCodEsito());
    setJmsIdMessage(aModel.getJmsIdMessaggio());
    setJmsCorrelationIdMessage(aModel.getJmsCorrelationIdMessage());
    setFlagVisto(aModel.getFlagVisto());
    //----setBlobEsito(aModel.getBlobIn());
    setChiaveAnnoSiep( aModel.getChiaveAnnoSiep() );
    setChiaveProgrSiep( aModel.getChiaveProgrSiep() );
    setChiaveUfficioSiep(aModel.getChiaveUfficioSiep());
    
    setChiaveAnnoSius( aModel.getChiaveAnnoSius() );
    setChiaveProgrSius( aModel.getChiaveProgrSius() );
    // UEPE
    setChiaveAnnoSiepe(aModel.getChiaveAnnoSiepe());
    setChiaveProgrSiepe(aModel.getChiaveProgrSiepe());
    setCognomeSoggetto(aModel.getCognomeSoggetto());
    setNomeSoggetto(aModel.getNomeSoggetto());
    setDataNascita(aModel.getDataNascita());
    setCodStatoNascita(aModel.getCodStatoNascita());
    setCodComuneNascita(aModel.getCodComuneNascita());
    
    setChiaveFasAnnoCumulante(aModel.getChiaveAnnoFasCumulante());
    setChiaveFasProgrCumulante(aModel.getChiaveProgrFasCumulante());
    setChiaveFasUfficioCumulante(aModel.getChiaveUfficioFasCumulante());
    setNote(aModel.getNote());
    
    setDeliveryMode          (aModel.getDeliveryMode());    
    setCodUfficioInoltro     (aModel.getCodUfficioInoltro());
    setCodBdiInoltro         (aModel.getCodBdiInoltro());    
    setCodUfficioReplyTo     (aModel.getCodUfficioReplyTo());
    setCodBdiReplyTo         (aModel.getCodBdiReplyTo());    
    setJmsCorrelationReplyTo (aModel.getJmsCorrelationReplyTo());
    setIdMessaggioSollecitato(aModel.getIdMessaggioSollecitato());
    
    setIdRichiesta			 (aModel.getIdRichiesta());
    setBlob(aModel);
  }

  public void setDAOFromModelForUpdate(MessaggioModel aModel) throws Exception
  {
    setTipoMessaggio(aModel.getCodTipoMessaggio());
    setTipoOperazione(aModel.getCodTipoOperazione());
    setCodUfficioMittente(aModel.getCodUfficioMittente());
    setCodBdiMittente(aModel.getCodBdiMittente());
    setCodUfficioDestinatario(aModel.getCodUfficioDestinatario());
    setCodBdiDestinataria(aModel.getCodBdiDestinataria());
    setDataInvio(aModel.getDataInvio());
    setDataEsito(aModel.getDataEsito());
    setCodiceUtenteMittente(aModel.getCodiceUtenteMittente());
    setCodEsito(aModel.getCodEsito());
    setJmsIdMessage(aModel.getJmsIdMessaggio());
    setJmsCorrelationIdMessage(aModel.getJmsCorrelationIdMessage());
    setFlagVisto(aModel.getFlagVisto());
    setChiaveAnnoSiep( aModel.getChiaveAnnoSiep() );
    setChiaveProgrSiep( aModel.getChiaveProgrSiep() );
    setChiaveUfficioSiep(aModel.getChiaveUfficioSiep());
    
    setChiaveAnnoSius( aModel.getChiaveAnnoSius() );
    setChiaveProgrSius( aModel.getChiaveProgrSius() );
    // UEPE
    if (aModel.getChiaveAnnoSiepe() != null )
      setChiaveAnnoSiepe(aModel.getChiaveAnnoSiepe());
    if (aModel.getChiaveProgrSiepe() != null )
      setChiaveProgrSiepe(aModel.getChiaveProgrSiepe());
    if (aModel.getCognomeSoggetto() != null )
      setCognomeSoggetto(aModel.getCognomeSoggetto());
    if (aModel.getNomeSoggetto() != null )
      setNomeSoggetto(aModel.getNomeSoggetto());
    if (aModel.getDataNascita() != null )
      setDataNascita(aModel.getDataNascita());
    if (aModel.getCodStatoNascita() != null )
      setCodStatoNascita(aModel.getCodStatoNascita());
    if (aModel.getCodComuneNascita() != null )
      setCodComuneNascita(aModel.getCodComuneNascita());
    
    if (aModel.getChiaveAnnoFasCumulante() != null )
    	setChiaveFasAnnoCumulante(aModel.getChiaveAnnoFasCumulante());
    if (aModel.getChiaveProgrFasCumulante() != null )
    	setChiaveFasProgrCumulante(aModel.getChiaveProgrFasCumulante());
    if (aModel.getChiaveUfficioFasCumulante() != null )
      setChiaveFasUfficioCumulante(aModel.getChiaveUfficioFasCumulante());

    //if (aModel.getNote() != null )
    setNote(aModel.getNote());
    
    if (aModel.getDeliveryMode()!=null)
      setDeliveryMode(aModel.getDeliveryMode());
    if (aModel.getCodUfficioInoltro()!=null)
      setCodUfficioInoltro(aModel.getCodUfficioInoltro());
    if (aModel.getCodBdiInoltro()!=null)
      setCodBdiInoltro(aModel.getCodBdiInoltro());
    if (aModel.getCodUfficioReplyTo()!=null)
      setCodUfficioReplyTo(aModel.getCodUfficioReplyTo());
    if (aModel.getCodBdiReplyTo()!=null)
      setCodBdiReplyTo(aModel.getCodBdiReplyTo());
    if (aModel.getJmsCorrelationReplyTo()!=null)
      setJmsCorrelationReplyTo (aModel.getJmsCorrelationReplyTo());
    if (aModel.getIdMessaggioSollecitato()!=null)
      setIdMessaggioSollecitato (aModel.getIdMessaggioSollecitato());
    
    setCondizioneUpdate(aModel.getIdMessaggio());

    //setBlob(aModel);
  }

  private void setBlob(MessaggioModel aModel) throws IOException
  {
    //Creo dal TreeModel il ByteArrayInputStream
    if (aModel.getTreeModel() != null)
    {
      ByteArrayOutputStream lout = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(lout);

      oos.writeObject(aModel.getTreeModel());
      oos.close();

      byte[] lBuffer = new byte[lout.size()];
      lBuffer = lout.toByteArray();

      ByteArrayInputStream lBufInput = new ByteArrayInputStream(lBuffer);
      setBlobEsito(lBufInput);
    }
  }

  public void setCondizione(MessaggioModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_MESSAGGIO = " + key );
  }
  
  
  // MEV_2024-DNA 
  // Impone la condizione di cancellazione per codufficio mittente o destinatario
  public void setCondizioneByUfficio(String aCodUfficio, Date aDataInviaDal, Date aDataInviaAl)
  {
    String lCondizione = "";
    lCondizione+="  (   COD_UFFICIO_MITTENTE = '"+aCodUfficio+"'";
    lCondizione+="   OR COD_UFFICIO_DESTINATARIO = '"+aCodUfficio+"' )";
    
    if (aDataInviaDal!=null)
      lCondizione+=" AND DATA_INVIO >= TO_DATE('"+DateUtils.getDateToString(aDataInviaDal,"dd/MM/yyyy")+"','dd/mm/yyyy') ";
    
    if (aDataInviaAl!=null)
      lCondizione+=" AND DATA_INVIO <= TO_DATE('"+DateUtils.getDateToString(aDataInviaAl,"dd/MM/yyyy")+"','dd/mm/yyyy') ";
    
    setCondition(lCondizione);
  }
}
