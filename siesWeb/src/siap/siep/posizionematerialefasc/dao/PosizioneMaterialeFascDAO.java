package siap.siep.posizionematerialefasc.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: PosizioneMaterialeFascDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PosizioneMaterialeFasc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

  public class PosizioneMaterialeFascDAO extends TableDAO
  {
    public PosizioneMaterialeFascDAO(Connection con)
    {
      super(con);
      setTable("POSIZIONE_MATERIALE_FASC");

      //Settare la Sequence e i campi chiave

      setField("COD_POSIZIONE_MATERIALE", STRING);
      setField("COD_UFFICIO", STRING);
      setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
      setField("COD_STATO_PROCEDIMENTO", STRING);
      setField("DATA_INIZIO", DATE);
      setField("DATA_FINE", DATE);
      setField("COD_OPERATORE_INSERIMENTO", STRING);
      setField("DATA_INSERIMENTO", DATE);
      setField("COD_UFFICIO_INSERIMENTO", STRING);
      setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
      setField("DATA_AGGIORNAMENTO", DATE);
      setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
      setField("DESCR_STATO_PROCEDIMENTO", STRING);

    }


    //
    // METODI GET()
    //

    public String getCodPosizioneMateriale() throws DAOException
    {
      return getString("COD_POSIZIONE_MATERIALE");
    }

    public String getCodUfficio() throws DAOException
    {
      return getString("COD_UFFICIO");
    }

    public BigDecimal getFasSieIdFascicoloSiep() throws DAOException
    {
      return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
    }

    public String getCodStatoProcedimento() throws DAOException
    {
      return getString("COD_STATO_PROCEDIMENTO");
    }

    public Date getDataInizio() throws DAOException
    {
      return getDate("DATA_INIZIO");
    }

    public Date getDataFine() throws DAOException
    {
      return getDate("DATA_FINE");
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

    public String getDescrStatoProcedimento() throws DAOException
    {
      return getString("DESCR_STATO_PROCEDIMENTO");
    }

    //
    // METODI SET()
    //

    public void setCodPosizioneMateriale(String aValore)
    {
      setString("COD_POSIZIONE_MATERIALE", aValore);
    }

    public void setCodUfficio(String aValore)
    {
      setString("COD_UFFICIO", aValore);
    }

    public void setFasSieIdFascicoloSiep(BigDecimal aValore)
    {
      setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
    }

    public void setCodStatoProcedimento(String aValore)
    {
      setString("COD_STATO_PROCEDIMENTO", aValore);
    }

    public void setDataInizio(Date aValore)
    {
      setDate("DATA_INIZIO", aValore);
    }

    public void setDataFine(Date aValore)
    {
      setDate("DATA_FINE", aValore);
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

    public void setDescrStatoProcedimento(String aValore)
    {
      setString("DESCR_STATO_PROCEDIMENTO", aValore);
    }


    public GenericModel getModel() throws DAOException
    {
      return new PosizioneMaterialeFascModel(
        getCodPosizioneMateriale(),
        "",
        getCodUfficio(),
        "",
        getFasSieIdFascicoloSiep(),
        getCodStatoProcedimento(),
        getDescrStatoProcedimento(),
        getDataInizio(),
        getDataFine(),
        getCodOperatoreInserimento(),
        getDataInserimento(),
        getCodUfficioInserimento(),
        "",
        getCodOperatoreAggiornamento(),
        getDataAggiornamento(),
        getCodUfficioAggiornamento(),
        "");
    }


    public void setDAOFromModel(PosizioneMaterialeFascModel aModel) throws DAOException
    {
      setCodPosizioneMateriale(aModel.getCodPosizioneMateriale());
      setCodUfficio(aModel.getCodUfficio());
      setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
      setCodStatoProcedimento(aModel.getCodStatoProcedimento());
      setDescrStatoProcedimento(aModel.getDescrStatoProcedimento());
      setDataInizio(aModel.getDataInizio());
      setDataFine(aModel.getDataFine());
      setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
      setDataInserimento(aModel.getDataInserimento());
      setCodUfficioInserimento(aModel.getCodUfficioInserimento());
      setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
      setDataAggiornamento(aModel.getDataAggiornamento());
      setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
    }

/*
    public void setDAOFromModelForUpdate(PosizioneMaterialeFascModel aModel) throws DAOException
    {
      setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
      setDataFine(aModel.getDataFine());
      setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
      setDataAggiornamento(aModel.getDataAggiornamento());
      setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
      setCondizioneUpdate(aModel.getFasSieIdFascicoloSiep());
    }
*/

    public void setCondizione(PosizioneMaterialeFascModel aModel)
    {
      String lCondizioni = new String();

      boolean lInserito = false;
      if (lInserito)
        setCondition(lCondizioni);
    }

    public void setCondizioneUpdate(BigDecimal key)
    {
        setCondition(" DATA_FINE IS NULL AND FAS_SIE_ID_FASCICOLO_SIEP = " + key );
    }


    public void setCondizioneRiattivazione(BigDecimal key)
    {
        setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + key + " AND DATA_AGGIORNAMENTO = (SELECT MAX(DATA_AGGIORNAMENTO) FROM POSIZIONE_MATERIALE_FASC WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + key + ")");
    }



}
