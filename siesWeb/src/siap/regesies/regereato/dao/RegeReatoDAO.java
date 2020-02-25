package siap.regesies.regereato.dao;

import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.regesies.regereato.model.RegeReatoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>Title: RegeReatoDAO</p>
 * <p>Description: Classe DAO che rappresenta la tabella RegeReato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class RegeReatoDAO extends SIAPTableDAO
{
  public RegeReatoDAO(Connection con)
  {
    super(con);
    setTable("rege_reato");

    //Settare la Sequence e i campi chiave

    setField("ID_FILE", STRING);
    setField("COD_TIPO_REATO", STRING);
    setField("DATA_REATO", DATE);
    setField("PROGR_NUMERO_MANUALE", STRING);
    setField("PROGR_REATO", INT);
    setField("PROGR_CIRCOSTANZA", INT);
    setField("DATA_INIZIO", DATE);
    setField("ANNO_INIZIO", INT);
    setField("MESE_INIZIO", INT);
    setField("GIORNO_INIZIO", INT);
    setField("DATA_FINE", DATE);
    setField("ANNO_FINE", INT);
    setField("MESE_FINE", INT);
    setField("GIORNO_FINE", INT);
    setField("COD_PERIODO_CONSUMAZIONE", STRING);
    setField("DESC_LUOGO", STRING);
    setField("COD_FONTE", STRING);
    setField("ANNO_FONTE", INT);
    setField("NUMERO_FONTE", STRING);
    setField("COD_SOTTONUMERAZIONE", STRING);
    setField("COMMA", STRING);
    setField("LETTERA", STRING);
    setField("NUMERO", STRING);
    setField("ARTICOLO", STRING);
    setField("NOTE", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);

   // setField("NOTA_QGF", STRING);
  }

  //
  // METODI GET()
  //

  public String getIdFile() throws DAOException  {    return getString("ID_FILE");  }
  public String getCodTipoReato() throws DAOException  {    return getString("COD_TIPO_REATO");  }
  public Date getDataReato() throws DAOException  {    return getDate("DATA_REATO");  }
  public String getProgrNumeroManuale() throws DAOException  {    return getString("PROGR_NUMERO_MANUALE");  }
  public int getProgrReato() throws DAOException  {    return getInt("PROGR_REATO");  }
  public int getProgrCircostanza() throws DAOException  {    return getInt("PROGR_CIRCOSTANZA");  }
  public Date getDataInizio() throws DAOException  {    return getDate("DATA_INIZIO");  }
  public int getAnnoInizio() throws DAOException  {    return getInt("ANNO_INIZIO");  }
  public int getMeseInizio() throws DAOException  {    return getInt("MESE_INIZIO");  }
  public int getGiornoInizio() throws DAOException  {    return getInt("GIORNO_INIZIO");  }
  public Date getDataFine() throws DAOException  {    return getDate("DATA_FINE");  }
  public int getAnnoFine() throws DAOException  {    return getInt("ANNO_FINE");  }
  public int getMeseFine() throws DAOException  {    return getInt("MESE_FINE");  }
  public int getGiornoFine() throws DAOException  {    return getInt("GIORNO_FINE");  }
  public String getCodPeriodoConsumazione() throws DAOException  {    return getString("COD_PERIODO_CONSUMAZIONE");  }
  public String getDescLuogo() throws DAOException  {    return getString("DESC_LUOGO");  }
  public String getCodFonte() throws DAOException  {    return getString("COD_FONTE");  }
  public int getAnnoFonte() throws DAOException {    return getInt("ANNO_FONTE");  }
  public String getNumeroFonte() throws DAOException  {    return getString("NUMERO_FONTE");  }
  public String getCodSottonumerazione() throws DAOException  {    return getString("COD_SOTTONUMERAZIONE");  }
  public String getComma() throws DAOException  {    return getString("COMMA");  }
  public String getLettera() throws DAOException  {    return getString("LETTERA");  }
  public String getNumero() throws DAOException  {    return getString("NUMERO");  }
  public String getArticolo() throws DAOException  {    return getString("ARTICOLO");  }
  public String getNote() throws DAOException  {    return getString("NOTE");  }
  public String getCodOperatoreInserimento() throws DAOException  {    return getString("COD_OPERATORE_INSERIMENTO");  }
  public Date getDataInserimento() throws DAOException  {    return getDate("DATA_INSERIMENTO");  }
  public String getCodUfficioInserimento() throws DAOException  {    return getString("COD_UFFICIO_INSERIMENTO");  }
  public String getCodOperatoreAggiornamento() throws DAOException  {    return getString("COD_OPERATORE_AGGIORNAMENTO");  }
  public Date getDataAggiornamento() throws DAOException  {    return getDate("DATA_AGGIORNAMENTO");  }
  public String getCodUfficioAggiornamento() throws DAOException  {    return getString("COD_UFFICIO_AGGIORNAMENTO");  }
  public String getNotaQgf() throws DAOException  {    return getString("NOTA_QGF");  }

  //
  // METODI SET()
  //

  public void setIdFile(String aValore)  {    setString("ID_FILE", aValore);  }
  public void setCodTipoReato(String aValore)  {    setString("COD_TIPO_REATO", aValore);  }
  public void setDataReato(Date aValore)  {    setDate("DATA_REATO", aValore);  }
  public void setProgrNumeroManuale(String aValore)  {    setString("PROGR_NUMERO_MANUALE", aValore);  }
  public void setProgrReato(int aValore)  {    setInt("PROGR_REATO", aValore);  }
  public void setProgrCircostanza(int aValore)  {    setInt("PROGR_CIRCOSTANZA", aValore);  }
  public void setDataInizio(Date aValore)  {    setDate("DATA_INIZIO", aValore);  }
  public void setAnnoInizio(int aValore)  {    setInt("ANNO_INIZIO", aValore);  }
  public void setMeseInizio(int aValore)  {    setInt("MESE_INIZIO", aValore);  }
  public void setGiornoInizio(int aValore)  {    setInt("GIORNO_INIZIO", aValore);  }
  public void setDataFine(Date aValore)  {    setDate("DATA_FINE", aValore);  }
  public void setAnnoFine(int aValore)  {    setInt("ANNO_FINE", aValore);  }
  public void setMeseFine(int aValore)  {    setInt("MESE_FINE", aValore);  }
  public void setGiornoFine(int aValore)  {    setInt("GIORNO_FINE", aValore);  }
  public void setCodPeriodoConsumazione(String aValore)  {    setString("COD_PERIODO_CONSUMAZIONE", aValore);  }
  public void setDescLuogo(String aValore)  {    setString("DESC_LUOGO", aValore);  }
  public void setCodFonte(String aValore)  {    setString("COD_FONTE", aValore);  }
  public void setAnnoFonte(int aValore)  {    setInt("ANNO_FONTE", aValore);  }
  public void setNumeroFonte(String aValore)  {    setString("NUMERO_FONTE", aValore);  }
  public void setCodSottonumerazione(String aValore)  {    setString("COD_SOTTONUMERAZIONE", aValore);  }
  public void setComma(String aValore)  {    setString("COMMA", aValore);  }
  public void setLettera(String aValore)  {    setString("LETTERA", aValore);  }
  public void setNumero(String aValore)  {    setString("NUMERO", aValore);  }
  public void setArticolo(String aValore)  {    setString("ARTICOLO", aValore);  }
  public void setNote(String aValore)  {    setString("NOTE", aValore);  }
  public void setCodOperatoreInserimento(String aValore)  {    setString("COD_OPERATORE_INSERIMENTO", aValore);  }
  public void setDataInserimento(Date aValore)  {    setDate("DATA_INSERIMENTO", aValore);  }
  public void setCodUfficioInserimento(String aValore)  {    setString("COD_UFFICIO_INSERIMENTO", aValore);  }
  public void setCodOperatoreAggiornamento(String aValore)  {    setString("COD_OPERATORE_AGGIORNAMENTO", aValore);  }
  public void setDataAggiornamento(Date aValore)  {    setDate("DATA_AGGIORNAMENTO", aValore);  }
  public void setCodUfficioAggiornamento(String aValore)  {    setString("COD_UFFICIO_AGGIORNAMENTO", aValore);  }
  public void setNotaQgf(String aValore)  {    setString("NOTA_QGF", aValore);  }



  /**
   * getModel
   * @return
   * @throws DAOException
   */
  public GenericModel getModel() throws DAOException
  {
    return new RegeReatoModel(
      getIdFile(),
      getCodTipoReato(),
      "",
      getDataReato(),
      getProgrNumeroManuale(),
      getProgrReato(),
      getProgrCircostanza(),
      getDataInizio(),
      getAnnoInizio(),
      getMeseInizio(),
      getGiornoInizio(),
      getDataFine(),
      getAnnoFine(),
      getMeseFine(),
      getGiornoFine(),
      getCodPeriodoConsumazione(),
      "",
      getDescLuogo(),
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
      getNote(),
      getCodOperatoreInserimento(),
      getDataInserimento(),
      getCodUfficioInserimento(),
      "",
      getCodOperatoreAggiornamento(),
      getDataAggiornamento(),
      getCodUfficioAggiornamento(),
      getNotaQgf()
      );
  }

  public void setDAOFromModel(RegeReatoModel aModel) throws DAOException
  {
    setIdFile(aModel.getIdFile());
    setCodTipoReato(aModel.getCodTipoReato());
    setDataReato(aModel.getDataReato());
    setProgrNumeroManuale(aModel.getProgrNumeroManuale());
    setProgrReato(aModel.getProgrReato());
    setProgrCircostanza(aModel.getProgrCircostanza());
    setDataInizio(aModel.getDataInizio());
    setAnnoInizio(aModel.getAnnoInizio());
    setMeseInizio(aModel.getMeseInizio());
    setGiornoInizio(aModel.getGiornoInizio());
    setDataFine(aModel.getDataFine());
    setAnnoFine(aModel.getAnnoFine());
    setMeseFine(aModel.getMeseFine());
    setGiornoFine(aModel.getGiornoFine());
    setCodPeriodoConsumazione(aModel.getCodPeriodoConsumazione());
    setDescLuogo(aModel.getDescLuogo());
    setCodFonte(aModel.getCodFonte());
    setAnnoFonte(aModel.getAnnoFonte());
    setNumeroFonte(aModel.getNumeroFonte());
    setCodSottonumerazione(aModel.getCodSottonumerazione());
    setComma(aModel.getComma());
    setLettera(aModel.getLettera());
    setNumero(aModel.getNumero());
    setArticolo(aModel.getArticolo());
    setNote(aModel.getNote());
    setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
    setDataInserimento(aModel.getDataInserimento());
    setCodUfficioInserimento(aModel.getCodUfficioInserimento());
    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
    setDataAggiornamento(aModel.getDataAggiornamento());
    setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
    setNotaQgf(aModel.getNotaQgf());
  }

  /**
   * Setta il Model per l'update
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForUpdate(RegeReatoModel aModel) throws DAOException
  {
    setIdFile(aModel.getIdFile());
    setCodTipoReato(aModel.getCodTipoReato());
    setDataReato(aModel.getDataReato());
    setProgrNumeroManuale(aModel.getProgrNumeroManuale());
    setProgrReato(aModel.getProgrReato());
    setProgrCircostanza(aModel.getProgrCircostanza());
    setDataInizio(aModel.getDataInizio());
    setAnnoInizio(aModel.getAnnoInizio());
    setMeseInizio(aModel.getMeseInizio());
    setGiornoInizio(aModel.getGiornoInizio());
    setDataFine(aModel.getDataFine());
    setAnnoFine(aModel.getAnnoFine());
    setMeseFine(aModel.getMeseFine());
    setGiornoFine(aModel.getGiornoFine());
    setCodPeriodoConsumazione(aModel.getCodPeriodoConsumazione());
    setDescLuogo(aModel.getDescLuogo());
    setCodFonte(aModel.getCodFonte());
    setAnnoFonte(aModel.getAnnoFonte());
    setNumeroFonte(aModel.getNumeroFonte());
    setCodSottonumerazione(aModel.getCodSottonumerazione());
    setComma(aModel.getComma());
    setLettera(aModel.getLettera());
    setNumero(aModel.getNumero());
    setArticolo(aModel.getArticolo());
    setNote(aModel.getNote());
    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
    setDataAggiornamento(aModel.getDataAggiornamento());
    setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
    setNotaQgf(aModel.getNotaQgf());

    setCondizioneUpdate(aModel);
  }

  /**
   * Setta la condizione per l'update del  RegeReato
   * @param aModel
   */
  public void setCondizioneUpdate(RegeReatoModel aModel)
  {
    String lCondizioni = new String();
    lCondizioni  = " ID_FILE = '" + aModel.getIdFile() + "'";
    lCondizioni += " AND PROGR_REATO = " + aModel.getProgrReato();
    lCondizioni += " AND PROGR_CIRCOSTANZA = " + aModel.getProgrCircostanza();

    setCondition(lCondizioni);
  }

  /*
   * Setta la condizione per la cancellazione di tutti i reati
   * @param aModel
   */
  public void setCondizioneDelete(String aKey)
  {
    String lCondizioni = new String();
    lCondizioni = " ID_FILE = '" +aKey + "'";

    setCondition(lCondizioni);
  }
}