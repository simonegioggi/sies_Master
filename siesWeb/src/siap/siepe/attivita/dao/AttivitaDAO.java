package siap.siepe.attivita.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siepe.attivita.model.AttivitaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: AttivitaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Attivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AttivitaDAO extends TableDAO
{
  public AttivitaDAO (Connection con)
  {
    super(con);
    setTable("ATTIVITA");

    //Settare la Sequence e i campi chiave
    setSequenceField("ID_ATTIVITA", "ATT_SEQ");

    setField("ID_ATTIVITA", BIG_DECIMAL);
    setField("DATA_INIZIO", DATE);
    setField("DATA_CHIUSURA", DATE);
    setField("COD_TIPO_ATTIVITA", STRING);
    setField("NOTE", STRING);
    //setField("DOC_BLOB", BLOB);
    setField("DOC_BLOB", TBLOB);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("FAS_SIE_ID_FAS_SIEPE", BIG_DECIMAL);
    setField("ASS_SOC_ID_ASS_SOCIALE", BIG_DECIMAL);
    setField("FLAG_DOCUMENTO_REGISTRATO", STRING);
    setField("COD_ESITO_ATTIVITA", STRING);
    setField("NOTA_CHIUSURA", STRING);
  }


  //
  // METODI GET()
  //

  public BigDecimal 		 getIdAttivita() 		throws DAOException	 { return getBigDecimal("ID_ATTIVITA"); }
  public Date 			getDataInizio() 		throws DAOException	 { return getDate("DATA_INIZIO"); }
  public Date 			getDataChiusura() 		throws DAOException	 { return getDate("DATA_CHIUSURA"); }
  public String 		getCodTipoAttivita() 		throws DAOException	 { return getString("COD_TIPO_ATTIVITA"); }
  public String 		getNote() 		throws DAOException	 { return getString("NOTE"); }
  //public Blob 		 getDocBlob() 		throws DAOException	 { return getBlob("DOC_BLOB"); }
  public ByteArrayOutputStream    getDocBlob() 		    throws DAOException	  { return getBlob("DOC_BLOB"); }
  public String 		getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 			getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 		getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 		getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 			getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 		getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal 		getFasSieIdFasSiepe() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FAS_SIEPE"); }
  public BigDecimal 		getAssSocIdAssSociale() 		throws DAOException	 { return getBigDecimal("ASS_SOC_ID_ASS_SOCIALE"); }
  public String 		getFlagDocumentoRegistrato()   throws DAOException	    { return getString("FLAG_DOCUMENTO_REGISTRATO"); }
  public String 		getCodEsitoAttivita() 		throws DAOException	 { return getString("COD_ESITO_ATTIVITA"); }
  public String 		getNotaChiusura() 		throws DAOException	 { return getString("NOTA_CHIUSURA"); }


  //
  // METODI SET()
  //

  public void  	 setIdAttivita(BigDecimal aValore ) 			 { setBigDecimal("ID_ATTIVITA", aValore); }
  public void  	 setDataInizio(Date aValore ) 			 { setDate("DATA_INIZIO", aValore); }
  public void  	 setDataChiusura(Date aValore ) 			 { setDate("DATA_CHIUSURA", aValore); }
  public void  	 setCodTipoAttivita(String aValore ) 			 { setString("COD_TIPO_ATTIVITA", aValore); }
  public void  	 setNote(String aValore ) 			 { setString("NOTE", aValore); }
  //public void  	 setDocBlob(Blob aValore ) 			 { setBlob("DOC_BLOB", aValore); }
  public void setDocBlob( ByteArrayInputStream aValore )      { setBlob("DOC_BLOB", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setFasSieIdFasSiepe(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FAS_SIEPE", aValore); }
  public void  	 setAssSocIdAssSociale(BigDecimal aValore ) 			 { setBigDecimal("ASS_SOC_ID_ASS_SOCIALE", aValore); }
  public void      setFlagDocumentoRegistrato(String aValore )        { setString("FLAG_DOCUMENTO_REGISTRATO", aValore); }
  public void  	 setCodEsitoAttivita(String aValore ) 			 { setString("COD_ESITO_ATTIVITA", aValore); }
  public void  	 setNotaChiusura(String aValore ) 			 { setString("NOTA_CHIUSURA", aValore); }


  public GenericModel getModel() throws DAOException
  {
    return new AttivitaModel(
        getIdAttivita() ,
        getDataInizio() ,
        getDataChiusura() ,
        getCodTipoAttivita() ,
        "",
        getNote() ,
        /* getDocBlob() ,*/
        getCodOperatoreInserimento() ,
        getDataInserimento() ,
        getCodUfficioInserimento() ,
        "",
        getCodOperatoreAggiornamento() ,
        getDataAggiornamento() ,
        getCodUfficioAggiornamento() ,
        "",
        getFasSieIdFasSiepe() ,
        getAssSocIdAssSociale(),
        getFlagDocumentoRegistrato(),
        getCodEsitoAttivita(),
        "",
        getNotaChiusura(),
        null
      );
  }


  public void 	 setDAOFromModel(AttivitaModel aModel) throws DAOException
  {
    setIdAttivita( aModel.getIdAttivita() );
    setDataInizio( aModel.getDataInizio() );
    setDataChiusura( aModel.getDataChiusura() );
    setCodTipoAttivita( aModel.getCodTipoAttivita() );
    setNote( aModel.getNote() );
    /*setDocBlob( aModel.getDocBlob() );*/
    setDocBlob( aModel.getDocBlobIn() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSieIdFasSiepe( aModel.getFasSieIdFasSiepe() );
    setAssSocIdAssSociale( aModel.getAssSocIdAssSociale() );
    setFlagDocumentoRegistrato(aModel.getFlagDocumentoRegistrato());
    setCodEsitoAttivita(aModel.getCodEsitoAttivita());
    setNotaChiusura(aModel.getNotaChiusura());
  }


  public void setDAOFromModelForUpdate(AttivitaModel aModel) throws DAOException
  {
    setDataInizio( aModel.getDataInizio() );
    setDataChiusura( aModel.getDataChiusura() );
    //setCodTipoAttivita( aModel.getCodTipoAttivita() );
    setNote( aModel.getNote() );
    //setDocBlob( aModel.getDocBlobIn() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    //setFasSieIdFasSiepe( aModel.getFasSieIdFasSiepe() );
    setAssSocIdAssSociale( aModel.getAssSocIdAssSociale() );
    //setFlagDocumentoRegistrato(aModel.getFlagDocumentoRegistrato());
     this.setCodEsitoAttivita(aModel.getCodEsitoAttivita());
     setNotaChiusura(aModel.getNotaChiusura());
    setCondizioneUpdate(aModel.getIdAttivita());
  }

  public void 	 setDAOFromModelForUpdateBlob(AttivitaModel aModel) throws DAOException
  {
     setDocBlob( aModel.getDocBlobIn() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setFlagDocumentoRegistrato( aModel.getFlagDocumentoRegistrato() );

    setCondizioneUpdate(aModel.getIdAttivita());
  }



  public void setCondizione(AttivitaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }


  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_ATTIVITA = " + key );
  }

}
