package siap.regesies.regeresidenza.dao;

import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.regesies.regeresidenza.model.RegeResidenzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>Title: RegeResidenzaDAO</p>
 * <p>Description: Classe DAO che rappresenta la tabella RegeResidenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class RegeResidenzaDAO extends SIAPTableDAO
{
  public RegeResidenzaDAO(Connection con)
  {
    super(con);
    setTable("rege_residenza");

    //Settare la Sequence e i campi chiave

    setField("ID_FILE", STRING);
    setField("COD_STATO", STRING);
    setField("COD_PROVINCIA", STRING);
    setField("COD_COMUNE", STRING);
    setField("CAP", STRING);
    setField("INDIRIZZO", STRING);
    setField("COD_TIPO_RESIDENZA", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("DESC_COMUNE_ESTERO", STRING);
  }

  //
  // METODI GET()
  //

  public String getIdFile() throws DAOException
  {
    return getString("ID_FILE");
  }

  public String getCodStato() throws DAOException
  {
    return getString("COD_STATO");
  }

  public String getCodProvincia() throws DAOException
  {
    return getString("COD_PROVINCIA");
  }

  public String getCodComune() throws DAOException
  {
    return getString("COD_COMUNE");
  }

  public String getCap() throws DAOException
  {
    return getString("CAP");
  }

  public String getIndirizzo() throws DAOException
  {
    return getString("INDIRIZZO");
  }

  public String getCodTipoResidenza() throws DAOException
  {
    return getString("COD_TIPO_RESIDENZA");
  }

  public String getCodOperatoreInserimento() throws DAOException
  {
    return getString("COD_OPERATORE_INSERIMENTO");
  }

  public Date getDataInserimento() throws DAOException
  {
    return getDate("DATA_INSERIMENTO");
  }

  public String getCodUfficioInserimento() throws DAOException
  {
    return getString("COD_UFFICIO_INSERIMENTO");
  }

  public String getCodOperatoreAggiornamento() throws DAOException
  {
    return getString("COD_OPERATORE_AGGIORNAMENTO");
  }

  public Date getDataAggiornamento() throws DAOException
  {
    return getDate("DATA_AGGIORNAMENTO");
  }

  public String getCodUfficioAggiornamento() throws DAOException
  {
    return getString("COD_UFFICIO_AGGIORNAMENTO");
  }

  public String getDescComuneEstero() throws DAOException
  {
    return getString("DESC_COMUNE_ESTERO");
  }

  //
  // METODI SET()
  //

  public void setIdFile(String aValore)
  {
    setString("ID_FILE", aValore);
  }

  public void setCodStato(String aValore)
  {
    setString("COD_STATO", aValore);
  }

  public void setCodProvincia(String aValore)
  {
    setString("COD_PROVINCIA", aValore);
  }

  public void setCodComune(String aValore)
  {
    setString("COD_COMUNE", aValore);
  }

  public void setCap(String aValore)
  {
    setString("CAP", aValore);
  }

  public void setIndirizzo(String aValore)
  {
    setString("INDIRIZZO", aValore);
  }

  public void setCodTipoResidenza(String aValore)
  {
    setString("COD_TIPO_RESIDENZA", aValore);
  }

  public void setCodOperatoreInserimento(String aValore)
  {
    setString("COD_OPERATORE_INSERIMENTO", aValore);
  }

  public void setDataInserimento(Date aValore)
  {
    setDate("DATA_INSERIMENTO", aValore);
  }

  public void setCodUfficioInserimento(String aValore)
  {
    setString("COD_UFFICIO_INSERIMENTO", aValore);
  }

  public void setCodOperatoreAggiornamento(String aValore)
  {
    setString("COD_OPERATORE_AGGIORNAMENTO", aValore);
  }

  public void setDataAggiornamento(Date aValore)
  {
    setDate("DATA_AGGIORNAMENTO", aValore);
  }

  public void setCodUfficioAggiornamento(String aValore)
  {
    setString("COD_UFFICIO_AGGIORNAMENTO", aValore);
  }

  public void setDescComuneEstero(String aValore)
  {
    setString("DESC_COMUNE_ESTERO", aValore);
  }

  public GenericModel getModel() throws DAOException
  {
    return new RegeResidenzaModel(
      getIdFile(),
      getCodStato(),
      "",
      getCodProvincia(),
      "",
      getCodComune(),
      "",
      getCap(),
      getIndirizzo(),
      getCodTipoResidenza(),
      "",
      getCodOperatoreInserimento(),
      getDataInserimento(),
      getCodUfficioInserimento(),
      "",
      getCodOperatoreAggiornamento(),
      getDataAggiornamento(),
      getCodUfficioAggiornamento(),
      "",
      getDescComuneEstero()
      );
  }

  public void setDAOFromModel(RegeResidenzaModel aModel) throws DAOException
  {
    setIdFile(aModel.getIdFile());
    setCodStato(aModel.getCodStato());
    setCodProvincia(aModel.getCodProvincia());
    setCodComune(aModel.getCodComune());
    setCap(aModel.getCap());
    setIndirizzo(aModel.getIndirizzo());
    setCodTipoResidenza(aModel.getCodTipoResidenza());
    setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
    setDataInserimento(aModel.getDataInserimento());
    setCodUfficioInserimento(aModel.getCodUfficioInserimento());
    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
    setDataAggiornamento(aModel.getDataAggiornamento());
    setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
    setDescComuneEstero(aModel.getDescComuneEstero());
  }

  public void setDAOFromModelForUpdate(RegeResidenzaModel aModel) throws DAOException
  {
    setCodStato(aModel.getCodStato());
    setCodProvincia(aModel.getCodProvincia());
    setCodComune(aModel.getCodComune());
    setCap(aModel.getCap());
    setIndirizzo(aModel.getIndirizzo());
    setCodTipoResidenza(aModel.getCodTipoResidenza());
    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
    setDataAggiornamento(aModel.getDataAggiornamento());
    setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
    setDescComuneEstero(aModel.getDescComuneEstero());

    setCondizioneUpdate(aModel.getIdFile(),aModel.getCodTipoResidenza());
  }

  public void setCondizione(RegeResidenzaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if (lInserito)
      setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(String key,String tipo)
  {
    setCondition(" ID_FILE = '" + key + "' and cod_tipo_residenza ='" +tipo+"'");
  }

/* Setta la condizione per la cancellazione
   * @param aModel
   */
  public void setCondizioneDelete(String aKey)
  {
    String lCondizioni = new String();
    lCondizioni = " ID_FILE = '" +aKey + "'";

    setCondition(lCondizioni);
  }


}