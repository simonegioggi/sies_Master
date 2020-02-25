package siap.siep.istruttoria.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;


/**
 * IstruttoriaSqlDAO
 * @author Giselda De Vita
 *
 */
public class IstruttoriaSqlDAO extends SIAPSqlDAO {
	
	public IstruttoriaSqlDAO(Connection lConn)
	{
		super(lConn);		
	}
	
  /**
   * Ricerca degli Id_facicoli che rientrano nel'intervallo anno-numero specificato 
   * 
   * @param aFasc
   * @throws DAOException
   */
	public void ricercaIdFascicoli(FascicoloSiepModel aFasc)
	throws DAOException
	{
		String lSql = "select id_fascicolo_siep ";
		lSql += "from fascicolo_siep where ";

		lSql += "CHIAVE_UFFICIO = '"+aFasc.getChiaveUfficio()+"' ";
	  
		  if ( (aFasc.getChiaveAnnoIniziale() != null ) && (aFasc.getChiaveAnnoIniziale().intValue() >= 0)
			         && (aFasc.getChiaveProgrIniziale() != null ) && (aFasc.getChiaveProgrIniziale().intValue() >= 0) )
			    {
			  		lSql += " AND ( (CHIAVE_ANNO > "+aFasc.getChiaveAnnoIniziale()+")";
			  		lSql +=      " OR (CHIAVE_ANNO = "+aFasc.getChiaveAnnoIniziale()+" AND CHIAVE_PROGR >= "+aFasc.getChiaveProgrIniziale()+"))";
			    }
//		 Cerca i fascicoli fino ad una coppia Progressivo/Anno
		   if ( (aFasc.getChiaveAnnoFinale() != null ) && (aFasc.getChiaveAnnoFinale().intValue() >= 0)
		         && (aFasc.getChiaveProgrFinale() != null ) && (aFasc.getChiaveProgrFinale().intValue() >= 0) )
		    {
		      // Nel caso non venga specificata la coppia di ricerca iniziale,
		      // vengono cercati i fascicoli
		      // a partire dal primo fascicolo dell'anno finale specificato
		      if ( (aFasc.getChiaveAnnoIniziale() == null ) || (aFasc.getChiaveAnnoIniziale().intValue() <= 0)
		          && (aFasc.getChiaveProgrIniziale() == null ) || (aFasc.getChiaveProgrIniziale().intValue() <= 0) )
		      {
		    	  lSql += "  ( (CHIAVE_ANNO > "+aFasc.getChiaveAnnoFinale()+")";
		    	  lSql +=      " OR (CHIAVE_ANNO = "+aFasc.getChiaveAnnoFinale()+" AND CHIAVE_PROGR >= 1))";
		      }

		      lSql += " AND ( (CHIAVE_ANNO < "+aFasc.getChiaveAnnoFinale()+")";
		      lSql +=      " OR (CHIAVE_ANNO = "+aFasc.getChiaveAnnoFinale()+" AND CHIAVE_PROGR <= "+aFasc.getChiaveProgrFinale()+"))";
		    }
		
		this.setStatement(lSql);
		
	}

	/**
	 * Ricerca degli Id_facicoli che rientrano nel'intervallo anno-numero specificato 
   * solo per i fascicoli in esecuzione (VALIDATI)
	 * @param aFasc
	 * @throws DAOException
	 */
	public void ricercaIdFascicoliEsecuzione(FascicoloSiepModel aFasc)
	throws DAOException
	{
		String lSql = "select id_fascicolo_siep ";
		lSql += "from fascicolo_siep where ";

		lSql += "CHIAVE_UFFICIO = '"+aFasc.getChiaveUfficio()+"' ";
		lSql += " AND  FLAG_VALIDATO ='S'  ";
		  
		  if ( (aFasc.getChiaveAnnoIniziale() != null ) && (aFasc.getChiaveAnnoIniziale().intValue() >= 0)
			         && (aFasc.getChiaveProgrIniziale() != null ) && (aFasc.getChiaveProgrIniziale().intValue() >= 0) )
			    {
			  		lSql += " AND ( (CHIAVE_ANNO > "+aFasc.getChiaveAnnoIniziale()+")";
			  		lSql +=      " OR (CHIAVE_ANNO = "+aFasc.getChiaveAnnoIniziale()+" AND CHIAVE_PROGR >= "+aFasc.getChiaveProgrIniziale()+"))";
			    }
//		 Cerca i fascicoli fino ad una coppia Progressivo/Anno
		   if ( (aFasc.getChiaveAnnoFinale() != null ) && (aFasc.getChiaveAnnoFinale().intValue() >= 0)
		         && (aFasc.getChiaveProgrFinale() != null ) && (aFasc.getChiaveProgrFinale().intValue() >= 0) )
		    {
		      // Nel caso non venga specificata la coppia di ricerca iniziale,
		      // vengono cercati i fascicoli
		      // a partire dal primo fascicolo dell'anno finale specificato
		      if ( (aFasc.getChiaveAnnoIniziale() == null ) || (aFasc.getChiaveAnnoIniziale().intValue() <= 0)
		          && (aFasc.getChiaveProgrIniziale() == null ) || (aFasc.getChiaveProgrIniziale().intValue() <= 0) )
		      {
		    	  lSql += "  ( (CHIAVE_ANNO > "+aFasc.getChiaveAnnoFinale()+")";
		    	  lSql +=      " OR (CHIAVE_ANNO = "+aFasc.getChiaveAnnoFinale()+" AND CHIAVE_PROGR >= 1))";
		      }

		      lSql += " AND ( (CHIAVE_ANNO < "+aFasc.getChiaveAnnoFinale()+")";
		      lSql +=      " OR (CHIAVE_ANNO = "+aFasc.getChiaveAnnoFinale()+" AND CHIAVE_PROGR <= "+aFasc.getChiaveProgrFinale()+"))";
		    }
		
		this.setStatement(lSql);
		
	}

	
	public GenericModel getModel() 
	throws DAOException
	{
		FascicoloSiepModel lFasc = new FascicoloSiepModel();
		
		lFasc.setIdFascicoloSiep(this.getBigDecimal("id_fascicolo_siep"));
		return lFasc;
	}

}
