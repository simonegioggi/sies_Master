package siap.sius.statistiche.dao;
import java.sql.Connection;

import siap.sius.statistiche.model.IspEstrazioneOggettiModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: IspFascicoliSqlDAO</p>
* <p>Description: Classe SqlDAO che effettua le ricerche di Fascicoli estratti nella tabella ISP_ESTRAZIONE_OGGETTI_TRIB dalle funzioni di statistica dell'Ispettorato.
* </p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* <p> La classe implementa le funzioni per accedere ai dati su questa tabella.
* @version 2.4
*/

public class IspFascicoliSqlDAO extends SqlDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public IspFascicoliSqlDAO (Connection con)
    {
		super(con);
    }


       //
       // METODI RICERCA()
       //
       public void ricercaFascicoliEstrattiPendenti(String aCodUfficio, String lCodMagistrato) throws DAOException
       {
          String lSql = getSqlQuery();

          // modificato da michele 15/12/2008
          lSql += " WHERE DEFINITO = 'N'" ;
          if (lCodMagistrato != null && lCodMagistrato.length() > 0 && lCodMagistrato.compareTo("0")!= 0)
			{
        	  lSql += " AND COD_MAGISTRATO = '" + lCodMagistrato + "'"; 
			}
          lSql += " AND FAS_SIU_CHIAVE_UFFICIO = '" + aCodUfficio + "' ";
          lSql += " ORDER BY FAS_SIU_CHIAVE_ANNO, FAS_SIU_CHIAVE_PROGR ";
          //lSql += " ORDER BY COD_OGGETTO_PROCEDIMENTO, FAS_SIU_CHIAVE_ANNO, FAS_SIU_CHIAVE_PROGR "; //Michele 9/2/2009

          setStatement(lSql);
       }
       
       //
       // METODI RICERCA()
       //
       public void ricercaOggettiEstrattiPendenti(String aCodUfficio, String lCodMagistrato) throws DAOException
       {
          String lSql = getSqlQuery();

          // modificato da michele 15/12/2008
          lSql += " WHERE DEFINITO = 'N'" ;
          if (lCodMagistrato != null && lCodMagistrato.length() > 0 && lCodMagistrato.compareTo("0")!= 0)
  			{
        	  lSql += " AND COD_MAGISTRATO = '" + lCodMagistrato + "'"; 
  			}
          lSql += " AND FAS_SIU_CHIAVE_UFFICIO = '" + aCodUfficio + "' ";
          lSql += " ORDER BY COD_OGGETTO_TENORE, FAS_SIU_CHIAVE_ANNO, FAS_SIU_CHIAVE_PROGR "; // Michele 6/2/2009
          
          setStatement(lSql);
       }
       
       public void ricercaFascicoliEstrattiUnificati(String aCodUfficio, String lCodMagistrato) throws DAOException
       {
          String lSql = getSqlQuery();

          lSql += " WHERE FAS_SIU_COD_STATO_FASCICOLO = '05'" ;
          lSql += " AND DEFINITO = 'S'" ;
          if (lCodMagistrato != null && lCodMagistrato.length() > 0 && lCodMagistrato.compareTo("0")!= 0)
			{
        	  lSql += " AND COD_MAGISTRATO = '" + lCodMagistrato + "'"; 
			}
          lSql += " AND FAS_SIU_CHIAVE_UFFICIO = '" + aCodUfficio + "' ";
          lSql += " ORDER BY FAS_SIU_CHIAVE_ANNO, FAS_SIU_CHIAVE_PROGR ";

          setStatement(lSql);
       }

       protected String getSqlQuery()
       {
          String lStatement = new String("");

          // Vengono letti i solo i campi relativi ai Fascicoli
          lStatement += " SELECT DISTINCT " +
          	  "FAS_SIU_ID_FASCICOLO_SIUS, " +
              "FAS_SIU_CHIAVE_ANNO, "+      
              "FAS_SIU_CHIAVE_UFFICIO, "+  
              "FAS_SIU_CHIAVE_PROGR, "+
              "FAS_SIU_DATA_ISCRIZIONE, "+
              "COD_OGGETTO_TENORE, "+ 		// Michele 6/2/2009
              "COD_OGGETTO_PROCEDIMENTO, "+ // Michele 9/2/2009
              "DEFINITO ";
              lStatement += " FROM ISP_ESTRAZIONE_OGGETTI_TRIB ";
              return lStatement;
      }
       
       
      //
      // METODO GETMODEL()
      //
      public GenericModel getModel() throws DAOException
      {
    	  IspEstrazioneOggettiModel aModel = new IspEstrazioneOggettiModel();
    	  
    	  aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
    	  aModel.setFasSiuChiaveAnno(getBigDecimal("FAS_SIU_CHIAVE_ANNO"));
    	  aModel.setFasSiuChiaveProgr(getBigDecimal( "FAS_SIU_CHIAVE_PROGR"));
    	  aModel.setFasSiuChiaveUfficio(getString("FAS_SIU_CHIAVE_UFFICIO"));
    	  aModel.setFasSiuDataIscrizione(getDate("FAS_SIU_DATA_ISCRIZIONE"));
    	  aModel.setCodOggettoTenore(getString("COD_OGGETTO_TENORE")); 				// Michele 6/2/2009
    	  aModel.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO")); 	// Michele 9/2/2009
    	  aModel.setDefinito(getString("DEFINITO"));
        
    	  //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    	  //siesLogger.debug("IspEstrazioneOggettiModel: " + aModel);  
        return aModel;    
    }
   

}