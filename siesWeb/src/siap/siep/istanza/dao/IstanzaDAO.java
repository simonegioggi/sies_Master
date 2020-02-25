package siap.siep.istanza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.istanza.model.IstanzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: IstanzaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Istanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class IstanzaDAO extends SIAPTableDAO
{
  public IstanzaDAO (Connection con)
  {
    super(con);
    setTable("ISTANZA");

    setSequenceField("ID_ISTANZA", "IST_SEQ");

    setFieldKey("ID_ISTANZA", BIG_DECIMAL);

    setField("ID_ISTANZA", BIG_DECIMAL);
    setField("COD_MOTIVO", STRING);
    setField("NOTE", STRING);
    setField("COGNOME_SOGGETTO_PRESENTANTE", STRING);
    setField("NOME_SOGGETTO_PRESENTANTE", STRING);
    setField("DATA_PRESENTAZIONE", DATE);
    setField("COD_ESITO", STRING);
    setField("ANNO_REGISTRO", BIG_DECIMAL);
    setField("PROGR_REGISTRO", BIG_DECIMAL);
    setField("COD_TIPO_UFFICIO_DESTINATARIO", STRING);
    setField("COD_LUOGO_DESTINATARIO", STRING);
    setField("COD_UFFICIO_DESTINATARIO", STRING);
    setField("COGNOME_AVVOCATO", STRING);
    setField("NOME_AVVOCATO", STRING);
    setField("FORO_COMPETENZA", STRING);
    setField("ANNO_SENTENZA", BIG_DECIMAL);
    setField("NUMERO_SENTENZA", STRING);
    setField("DATA_SENTENZA", DATE);
    setField("DATA_IRREVOCABILITA", DATE);
    setField("COD_TIPO_AUTORITA_EMITTENTE", STRING);
    setField("COD_LUOGO_EMITTENTE", STRING);
    setField("COD_STATO_ISTANZA", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("SOG_ID_SOGGETTO", BIG_DECIMAL);
    setField("EVE_ID_EVENTO", BIG_DECIMAL);
    setField("CAM_ID_CAMPO_NOTE", BIG_DECIMAL);
  }


  //
  // METODI GET()
  //

  public BigDecimal getIdIstanza() 		throws DAOException	 { return getBigDecimal("ID_ISTANZA"); }
  public String getCodMotivo() 		        throws DAOException	 { return getString("COD_MOTIVO"); }
  public String getNote() 		        throws DAOException	 { return getString("NOTE"); }
  public String getCognomeSoggettoPresentante() throws DAOException	 { return getString("COGNOME_SOGGETTO_PRESENTANTE"); }
  public String getNomeSoggettoPresentante() 	throws DAOException	 { return getString("NOME_SOGGETTO_PRESENTANTE"); }
  public Date 	getDataPresentazione() 		throws DAOException	 { return getDate("DATA_PRESENTAZIONE"); }
  public String getCodEsito() 		        throws DAOException	 { return getString("COD_ESITO"); }
  public BigDecimal getAnnoRegistro() 		throws DAOException	 { return getBigDecimal("ANNO_REGISTRO"); }
  public BigDecimal getProgrRegistro() 		throws DAOException	 { return getBigDecimal("PROGR_REGISTRO"); }
  public String getCodTipoUfficioDestinatario() throws DAOException	 { return getString("COD_TIPO_UFFICIO_DESTINATARIO"); }
  public String getCodLuogoDestinatario() 	throws DAOException	 { return getString("COD_LUOGO_DESTINATARIO"); }
  public String getCodUfficioDestinatario() 	throws DAOException	 { return getString("COD_UFFICIO_DESTINATARIO"); }
  public String getCognomeAvvocato() 		throws DAOException	 { return getString("COGNOME_AVVOCATO"); }
  public String getNomeAvvocato() 		throws DAOException	 { return getString("NOME_AVVOCATO"); }
  public String getForoCompetenza() 		throws DAOException	 { return getString("FORO_COMPETENZA"); }
  public BigDecimal getAnnoSentenza() 		throws DAOException	 { return getBigDecimal("ANNO_SENTENZA"); }
  public String getNumeroSentenza() 		throws DAOException	 { return getString("NUMERO_SENTENZA"); }
  public Date 	getDataSentenza() 		throws DAOException	 { return getDate("DATA_SENTENZA"); }
  public Date 	getDataIrrevocabilita() 	throws DAOException	 { return getDate("DATA_IRREVOCABILITA"); }
  public String getCodTipoAutoritaEmittente() 	throws DAOException	 { return getString("COD_TIPO_AUTORITA_EMITTENTE"); }
  public String getCodLuogoEmittente() 		throws DAOException	 { return getString("COD_LUOGO_EMITTENTE"); }
  public String getCodStatoIstanza() 		throws DAOException	 { return getString("COD_STATO_ISTANZA"); }
  public String getCodOperatoreInserimento() 	throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 	getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String getCodUfficioInserimento() 	throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 	getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal getSogIdSoggetto() 		throws DAOException	 { return getBigDecimal("SOG_ID_SOGGETTO"); }
  public BigDecimal getEveIdEvento() 		throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }
  public BigDecimal getCamIdCampoNote() 		throws DAOException	 { return getBigDecimal("CAM_ID_CAMPO_NOTE"); }


  //
  // METODI SET()
  //

  public void setIdIstanza(BigDecimal aValore ) 			 { setBigDecimal("ID_ISTANZA", aValore); }
  public void setCodMotivo(String aValore ) 			         { setString("COD_MOTIVO", aValore); }
  public void setNote(String aValore ) 			                 { setString("NOTE", aValore); }
  public void setCognomeSoggettoPresentante(String aValore ) 		 { setString("COGNOME_SOGGETTO_PRESENTANTE", aValore); }
  public void setNomeSoggettoPresentante(String aValore ) 		 { setString("NOME_SOGGETTO_PRESENTANTE", aValore); }
  public void setDataPresentazione(Date aValore ) 			 { setDate("DATA_PRESENTAZIONE", aValore); }
  public void setCodEsito(String aValore ) 			         { setString("COD_ESITO", aValore); }
  public void setAnnoRegistro(BigDecimal aValore ) 			 { setBigDecimal("ANNO_REGISTRO", aValore); }
  public void setProgrRegistro(BigDecimal aValore ) 			 { setBigDecimal("PROGR_REGISTRO", aValore); }
  public void setCodTipoUfficioDestinatario(String aValore ) 		 { setString("COD_TIPO_UFFICIO_DESTINATARIO", aValore); }
  public void setCodLuogoDestinatario(String aValore ) 			 { setString("COD_LUOGO_DESTINATARIO", aValore); }
  public void setCodUfficioDestinatario(String aValore ) 		 { setString("COD_UFFICIO_DESTINATARIO", aValore); }
  public void setCognomeAvvocato(String aValore ) 			 { setString("COGNOME_AVVOCATO", aValore); }
  public void setNomeAvvocato(String aValore ) 			         { setString("NOME_AVVOCATO", aValore); }
  public void setForoCompetenza(String aValore ) 			 { setString("FORO_COMPETENZA", aValore); }
  public void setAnnoSentenza(BigDecimal aValore ) 			 { setBigDecimal("ANNO_SENTENZA", aValore); }
  public void setNumeroSentenza(String aValore ) 			 { setString("NUMERO_SENTENZA", aValore); }
  public void setDataSentenza(Date aValore ) 			         { setDate("DATA_SENTENZA", aValore); }
  public void setDataIrrevocabilita(Date aValore ) 			 { setDate("DATA_IRREVOCABILITA", aValore); }
  public void setCodTipoAutoritaEmittente(String aValore ) 		 { setString("COD_TIPO_AUTORITA_EMITTENTE", aValore); }
  public void setCodLuogoEmittente(String aValore ) 			 { setString("COD_LUOGO_EMITTENTE", aValore); }
  public void setCodStatoIstanza(String aValore ) 			 { setString("COD_STATO_ISTANZA", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 		 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 		 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 		 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 		 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setSogIdSoggetto(BigDecimal aValore ) 			 { setBigDecimal("SOG_ID_SOGGETTO", aValore); }
  public void setEveIdEvento(BigDecimal aValore ) 			 { setBigDecimal("EVE_ID_EVENTO", aValore); }
  public void setCamIdCampoNote(BigDecimal aValore ) 		{ setBigDecimal("CAM_ID_CAMPO_NOTE", aValore); }


  public GenericModel getModel() throws DAOException
  {
    return new IstanzaModel(
                             getIdIstanza() ,
                             getCodMotivo() ,
                             "",
                             getNote() ,
                             getCognomeSoggettoPresentante() ,
                             getNomeSoggettoPresentante() ,
                             getDataPresentazione() ,
                             getCodEsito() ,
                             "",
                             getAnnoRegistro() ,
                             getProgrRegistro() ,
                             getCodTipoUfficioDestinatario() ,
                             "",
                             getCodLuogoDestinatario() ,
                             "",
                             getCodUfficioDestinatario() ,
                             "",
                             getCognomeAvvocato() ,
                             getNomeAvvocato() ,
                             getForoCompetenza() ,
                             getAnnoSentenza() ,
                             getNumeroSentenza() ,
                             getDataSentenza() ,
                             getDataIrrevocabilita() ,
                             getCodTipoAutoritaEmittente() ,
                             "",
                             getCodLuogoEmittente() ,
                             "",
                             getCodStatoIstanza() ,
                             "",
                             getCodOperatoreInserimento() ,
                             getDataInserimento() ,
                             getCodUfficioInserimento() ,
                             "",
                             getCodOperatoreAggiornamento() ,
                             getDataAggiornamento() ,
                             getCodUfficioAggiornamento() ,
                             "",
                             getSogIdSoggetto() ,
                             getEveIdEvento(),
                             getCamIdCampoNote()
                           );
  }

  public void setDAOFromModel(IstanzaModel aModel) throws DAOException
  {
    setIdIstanza( aModel.getIdIstanza() );
    setCodMotivo( aModel.getCodMotivo() );
    setNote( aModel.getNote() );
    setCognomeSoggettoPresentante( aModel.getCognomeSoggettoPresentante() );
    setNomeSoggettoPresentante( aModel.getNomeSoggettoPresentante() );
    setDataPresentazione( aModel.getDataPresentazione() );
    setCodEsito( aModel.getCodEsito() );
    setAnnoRegistro( aModel.getAnnoRegistro() );
    setProgrRegistro( aModel.getProgrRegistro() );
    setCodTipoUfficioDestinatario( aModel.getCodTipoUfficioDestinatario() );
    setCodLuogoDestinatario( aModel.getCodLuogoDestinatario() );
    setCodUfficioDestinatario( aModel.getCodUfficioDestinatario() );
    setCognomeAvvocato( aModel.getCognomeAvvocato() );
    setNomeAvvocato( aModel.getNomeAvvocato() );
    setForoCompetenza( aModel.getForoCompetenza() );
    setAnnoSentenza( aModel.getAnnoSentenza() );
    setNumeroSentenza( aModel.getNumeroSentenza() );
    setDataSentenza( aModel.getDataSentenza() );
    setDataIrrevocabilita( aModel.getDataIrrevocabilita() );
    setCodTipoAutoritaEmittente( aModel.getCodTipoAutoritaEmittente() );
    setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
    setCodStatoIstanza( aModel.getCodStatoIstanza() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setSogIdSoggetto( aModel.getSogIdSoggetto() );
    setEveIdEvento( aModel.getEveIdEvento() );
    setCamIdCampoNote( aModel.getCamIdCampoNote() );
  }

  public void setDAOFromModelForUpdate(IstanzaModel aModel) throws DAOException
  {
    //setIdIstanza( aModel.getIdIstanza() );
    setCodMotivo( aModel.getCodMotivo() );
    setNote( aModel.getNote() );
    setCognomeSoggettoPresentante( aModel.getCognomeSoggettoPresentante() );
    setNomeSoggettoPresentante( aModel.getNomeSoggettoPresentante() );
    setDataPresentazione( aModel.getDataPresentazione() );
    setCodEsito( aModel.getCodEsito() );
    //setAnnoRegistro( aModel.getAnnoRegistro() );
    //setProgrRegistro( aModel.getProgrRegistro() );
    setCodTipoUfficioDestinatario( aModel.getCodTipoUfficioDestinatario() );
    setCodLuogoDestinatario( aModel.getCodLuogoDestinatario() );
    setCodUfficioDestinatario( aModel.getCodUfficioDestinatario() );
    setCognomeAvvocato( aModel.getCognomeAvvocato() );
    setNomeAvvocato( aModel.getNomeAvvocato() );
    setForoCompetenza( aModel.getForoCompetenza() );
    setAnnoSentenza( aModel.getAnnoSentenza() );
    setNumeroSentenza( aModel.getNumeroSentenza() );
    setDataSentenza( aModel.getDataSentenza() );
    setDataIrrevocabilita( aModel.getDataIrrevocabilita() );
    setCodTipoAutoritaEmittente( aModel.getCodTipoAutoritaEmittente() );
    setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
    setCodStatoIstanza( aModel.getCodStatoIstanza() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    //setSogIdSoggetto( aModel.getSogIdSoggetto() );
    setEveIdEvento( aModel.getEveIdEvento() );
    setCamIdCampoNote( aModel.getCamIdCampoNote() );

    setCondizioneUpdate(aModel.getIdIstanza());
  }

/*
   public void setCondizione(IstanzaModel aModel)
   {
     String lCondizioni = new String();

     boolean lInserito = false;
     if ( lInserito ) setCondition(lCondizioni);
   }
*/

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_ISTANZA = " + key );
  }
}
