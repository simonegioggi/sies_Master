package siap.siep.fungibilita.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.siep.fungibilita.model.FungibilitaModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
* <p>Title: FungibilitaSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Fungibilita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class FungibilitaSqlDAO extends SqlDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public FungibilitaSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //
  
  /**
   * Restituisce l'elenco dei record fungibilità collegati ad eventi inseriti
   * nel range di date passate in input.
   * n.b. sia l'evento che la fungibilità devono essere validati
   * Le fung
   * @param aIdFascicolo
   * @param aDataDal
   * @param aDataAl
   */
  public void ricercaFungibilitaByFascicoliSiep(BigDecimal aFascID, Date aDataDal, Date aDataAl)
  {
    String lStatement = new String("");

    lStatement += " SELECT fung.ID_FUNGIBILITA, fung.COD_TIPO_FUNGIBILITA, "+
                         " fung.NUM_ANNI, fung.NUM_MESI, fung.NUM_GIORNI, "+
                         " fung.DATA_DA, fung.DATA_A, "+
                         " fung.DATA_INIZIO_VALIDITA, fung.DATA_FINE_VALIDITA, "+
                         " fung.COD_UFFICIO_FRUITORE, "+
                         " fung.COD_OPERATORE_INSERIMENTO, fung.DATA_INSERIMENTO, fung.COD_UFFICIO_INSERIMENTO, "+
                         " fung.COD_OPERATORE_AGGIORNAMENTO, fung.DATA_AGGIORNAMENTO, fung.COD_UFFICIO_AGGIORNAMENTO, "+
                         " fung.FAS_SIE_ID_FASCICOLO_SIEP, "+
                         " fung.EVE_ID_EVENTO, "+
                         " fung.NUM_GIORNI_FRUITI, "+
                         " fung.NUM_GIORNI_NON_FRUITI, "+
                         " fung.FLAG_VALIDATO ";
    lStatement += " FROM FUNGIBILITA fung, EVENTO eve ";
    lStatement += " WHERE fung.FAS_SIE_ID_FASCICOLO_SIEP = "+aFascID;
    lStatement += "   AND eve.FAS_SIE_ID_FASCICOLO_SIEP = "+aFascID;
    lStatement += "   AND fung.EVE_ID_EVENTO = eve.id_evento "; 
    lStatement += "   AND fung.FLAG_VALIDATO = 'S' ";
    lStatement += "   AND eve.FLAG_DOCUMENTO_REGISTRATO = 'S' ";

    if (aDataDal!=null){
      lStatement += "  AND eve.DATA_INSERIMENTO >= to_date ('"+DateUtils.getDateToString(aDataDal,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
    }
    if (aDataAl!=null){// n.b. <= perchè devo beccare anche l'evento corrente
      lStatement += "  AND eve.DATA_INSERIMENTO <= to_date ('"+DateUtils.getDateToString(aDataAl,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
    }

    lStatement += " ORDER BY fung.DATA_INSERIMENTO desc ";
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lStatement: "+lStatement);
    
    setStatement(lStatement);
  }
  
/*  
  public void ricercaFungibilita( FungibilitaModel  aModel)	 throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizione(aModel);
    setStatement(lSql);
  }
*/
  
  public void ricercaFungibilitaByKeyEvento( BigDecimal aIdEvento)	 throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizioneIdEvento(aIdEvento);
    setStatement(lSql);
  }

  public void ricercaFungibilitaByKey( BigDecimal aKey)	 throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizioneByKey(aKey);
    setStatement(lSql);
  }

  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_FUNGIBILITA, "+
                  "COD_TIPO_FUNGIBILITA, "+
                  "NUM_ANNI, "+
                  "NUM_MESI, "+
                  "NUM_GIORNI, "+
                  "DATA_DA, "+
                  "DATA_A, "+
                  "DATA_INIZIO_VALIDITA, "+
                  "DATA_FINE_VALIDITA, "+
                  "COD_UFFICIO_FRUITORE, "+
                  "COD_OPERATORE_INSERIMENTO, "+
                  "DATA_INSERIMENTO, "+
                  "COD_UFFICIO_INSERIMENTO, "+
                  "COD_OPERATORE_AGGIORNAMENTO, "+
                  "DATA_AGGIORNAMENTO, "+
                  "COD_UFFICIO_AGGIORNAMENTO, "+
                  "FAS_SIE_ID_FASCICOLO_SIEP, "+
                  "EVE_ID_EVENTO, "+
                  "NUM_GIORNI_FRUITI, "+
                  "NUM_GIORNI_NON_FRUITI, "+
                  "FLAG_VALIDATO ";
    lStatement += " FROM FUNGIBILITA";
    lStatement += " WHERE ";
    return lStatement;
  }

  //
  // METODO GETMODEL()
  //

  public GenericModel getModel() throws DAOException
  {
    FungibilitaModel aModel = new  FungibilitaModel();

//Inserire le opportune set delle descrizioni!
    aModel.setIdFungibilita(getBigDecimal("ID_FUNGIBILITA") );
    aModel.setCodTipoFungibilita(getString("COD_TIPO_FUNGIBILITA") );
    //aModel.setDescrTipoFungibilita(getString("") );
    aModel.setNumAnni(getBigDecimal("NUM_ANNI") );
    aModel.setNumMesi(getBigDecimal("NUM_MESI") );
    aModel.setNumGiorni(getBigDecimal("NUM_GIORNI") );
    aModel.setDataDa(getDate("DATA_DA") );
    aModel.setDataA(getDate("DATA_A") );
    aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA") );
    aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA") );
    aModel.setCodUfficioFruitore(getString("COD_UFFICIO_FRUITORE") );
    //aModel.setDescrUfficioFruitore(getString("") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP") );
    aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO") );
    aModel.setNumGiorniFruiti(getBigDecimal("NUM_GIORNI_FRUITI") );
    aModel.setNumGiorniNonFruiti(getBigDecimal("NUM_GIORNI_NON_FRUITI") );
    aModel.setFlagValidato(getString("FLAG_VALIDATO") );

    return aModel;
  }

/*
  public String setCondizione(FungibilitaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    return lCondizioni;
  }
*/
  
  public String setCondizioneIdEvento(BigDecimal IdEvento)
  {
    String lCondizioni = new String();
    lCondizioni = " EVE_ID_EVENTO = " + IdEvento;

    return lCondizioni;
  }

  public String setCondizioneByKey(BigDecimal aKey)
  {
    String lCondizioni = new String();
    lCondizioni = " ID_FUNGIBILITA = " + aKey;

    return lCondizioni;
  }

  public void ricercaFungibilitaDescByIdFascicolo( BigDecimal aKeyFascicolo)	 throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo + " AND FLAG_VALIDATO = 'N'";
    lSql += " ORDER BY DATA_INSERIMENTO DESC";

    setStatement(lSql);
  }

  // Modifica l'ultimo record di fungibilità se presente non validato
  // altrimenti lo inserisce
  public void inserisciOModificaFungibilita(FungibilitaModel aFungibilita) throws DAOException
  {
    FungibilitaDAO lFunDao = new FungibilitaDAO( this.mCon);

    // Cerca se esiste un record di fungibilita' per quel fascicolo non validato
    ricercaFungibilitaDescByIdFascicolo(aFungibilita.getFasSieIdFascicoloSiep());
    FungibilitaModel lFunMod = (FungibilitaModel) getModelByKey();
    if (lFunMod != null) // se esiste lo aggiorna
    {
      aFungibilita.setIdFungibilita(lFunMod.getIdFungibilita());

      lFunDao.setDAOFromModelForUpdate(aFungibilita);
      lFunDao.update();
      lFunDao.stop();
    }
    else // altrimenti lo inserisce
    {
      lFunDao.setDAOFromModel(aFungibilita);
      BigDecimal lKey = lFunDao.insert();
      lFunDao.stop();

      aFungibilita.setIdFungibilita(lKey);
    }
  }
}