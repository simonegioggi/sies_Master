package siap.regesies.regecircostanza.dao;

import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.regesies.regecircostanza.model.RegeCircostanzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>Title: RegeCircostanzaDAO</p>
 * <p>Description: Classe DAO che rappresenta la tabella RegeCircostanza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class RegeCircostanzaDAO extends SIAPTableDAO
{
  public RegeCircostanzaDAO(Connection con)
  {
    super(con);
    setTable("rege_circostanza");

    //Settare la Sequence e i campi chiave

    setField("ID_FILE", STRING);
    setField("PROGR_CIRCOSTANZA", INT);
    setField("COD_TIPO_CIRCOSTANZA", STRING);
    setField("COD_FONTE", STRING);
    setField("ANNO_FONTE", INT);
    setField("NUMERO_FONTE", STRING);
    setField("COD_SOTTONUMERAZIONE", STRING);
    setField("COMMA", STRING);
    setField("LETTERA", STRING);
    setField("NUMERO", STRING);
    setField("ARTICOLO", STRING);
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

  public String getIdFile() throws DAOException  {    return getString("ID_FILE");}
  public int getProgrCircostanza() throws DAOException{    return getInt("PROGR_CIRCOSTANZA");}
  public String getCodTipoCircostanza() throws DAOException{    return getString("COD_TIPO_CIRCOSTANZA");}
  public String getCodFonte() throws DAOException{    return getString("COD_FONTE");}
  public int getAnnoFonte() throws DAOException{    return getInt("ANNO_FONTE");}
  public String getNumeroFonte() throws DAOException{    return getString("NUMERO_FONTE");}
  public String getCodSottonumerazione() throws DAOException{    return getString("COD_SOTTONUMERAZIONE");}
  public String getComma() throws DAOException{    return getString("COMMA");}
  public String getLettera() throws DAOException{    return getString("LETTERA");}
  public String getNumero() throws DAOException{    return getString("NUMERO");}
  public String getArticolo() throws DAOException{    return getString("ARTICOLO");}
  public String getCodOperatoreInserimento() throws DAOException{    return getString("COD_OPERATORE_INSERIMENTO");}
  public Date getDataInserimento() throws DAOException{    return getDate("DATA_INSERIMENTO");}
  public String getCodUfficioInserimento() throws DAOException{    return getString("COD_UFFICIO_INSERIMENTO");}
  public String getCodOperatoreAggiornamento() throws DAOException{    return getString("COD_OPERATORE_AGGIORNAMENTO");}
  public Date getDataAggiornamento() throws DAOException{    return getDate("DATA_AGGIORNAMENTO");}
  public String getCodUfficioAggiornamento() throws DAOException{    return getString("COD_UFFICIO_AGGIORNAMENTO");}
  //
  // METODI SET()
  //
  public void setIdFile(String aValore){    setString("ID_FILE", aValore);}
  public void setProgrCircostanza(int aValore){    setInt("PROGR_CIRCOSTANZA", aValore);}
  public void setCodTipoCircostanza(String aValore){    setString("COD_TIPO_CIRCOSTANZA", aValore);}
  public void setCodFonte(String aValore){    setString("COD_FONTE", aValore);}
  public void setAnnoFonte(int aValore){    setInt("ANNO_FONTE", aValore);}
  public void setNumeroFonte(String aValore){    setString("NUMERO_FONTE", aValore);}
  public void setCodSottonumerazione(String aValore){    setString("COD_SOTTONUMERAZIONE", aValore);}
  public void setComma(String aValore){    setString("COMMA", aValore);}
  public void setLettera(String aValore){    setString("LETTERA", aValore);}
  public void setNumero(String aValore){    setString("NUMERO", aValore);}
  public void setArticolo(String aValore){    setString("ARTICOLO", aValore);}
  public void setCodOperatoreInserimento(String aValore){    setString("COD_OPERATORE_INSERIMENTO", aValore);}
  public void setDataInserimento(Date aValore){    setDate("DATA_INSERIMENTO", aValore);}
  public void setCodUfficioInserimento(String aValore){    setString("COD_UFFICIO_INSERIMENTO", aValore);}
  public void setCodOperatoreAggiornamento(String aValore){    setString("COD_OPERATORE_AGGIORNAMENTO", aValore);}
  public void setDataAggiornamento(Date aValore){    setDate("DATA_AGGIORNAMENTO", aValore);}
  public void setCodUfficioAggiornamento(String aValore){    setString("COD_UFFICIO_AGGIORNAMENTO", aValore);}

  public GenericModel getModel() throws DAOException
  {
    return new RegeCircostanzaModel(
      getIdFile(),
      getProgrCircostanza(),
      getCodTipoCircostanza(),
      "",
      getCodFonte(),
      "",
      getAnnoFonte(),
      getNumeroFonte(),
      getCodSottonumerazione(),
      "",
      getComma(),
      getLettera(),
      getNumero(),
      getArticolo(),
      getCodOperatoreInserimento(),
      getDataInserimento(),
      getCodUfficioInserimento(),
      "",
      getCodOperatoreAggiornamento(),
      getDataAggiornamento(),
      getCodUfficioAggiornamento()
      );
  }

  public void setDAOFromModel(RegeCircostanzaModel aModel) throws DAOException
  {
    setIdFile(aModel.getIdFile());
    setProgrCircostanza(aModel.getProgrCircostanza());
    setCodTipoCircostanza(aModel.getCodTipoCircostanza());
    setCodFonte(aModel.getCodFonte());
    setAnnoFonte(aModel.getAnnoFonte());
    setNumeroFonte(aModel.getNumeroFonte());
    setCodSottonumerazione(aModel.getCodSottonumerazione());
    setComma(aModel.getComma());
    setLettera(aModel.getLettera());
    setNumero(aModel.getNumero());
    setArticolo(aModel.getArticolo());
    setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
    setDataInserimento(aModel.getDataInserimento());
    setCodUfficioInserimento(aModel.getCodUfficioInserimento());
    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
    setDataAggiornamento(aModel.getDataAggiornamento());
    setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
  }

  public void setDAOFromModelForUpdate(RegeCircostanzaModel aModel) throws DAOException
  {
    setIdFile(aModel.getIdFile());
    setProgrCircostanza(aModel.getProgrCircostanza());
    setCodTipoCircostanza(aModel.getCodTipoCircostanza());
    setCodFonte(aModel.getCodFonte());
    setAnnoFonte(aModel.getAnnoFonte());
    setNumeroFonte(aModel.getNumeroFonte());
    setCodSottonumerazione(aModel.getCodSottonumerazione());
    setComma(aModel.getComma());
    setLettera(aModel.getLettera());
    setNumero(aModel.getNumero());
    setArticolo(aModel.getArticolo());
    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
    setDataAggiornamento(aModel.getDataAggiornamento());
    setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());

    setCondizioneUpdate(aModel.getIdFile(), aModel.getProgrCircostanza());
  }

  public void setCondizione(RegeCircostanzaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if (lInserito)
      setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(String key, int progr)
  {
    String lCondition = " ID_FILE = '" + key + "'";
    lCondition += " AND PROGR_CIRCOSTANZA = " + progr;

    setCondition(lCondition);
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