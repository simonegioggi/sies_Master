package siap.siep.statis.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 *
 * <p>Title: MagistratoFirmatarioSqlDAO</p>
 * <p>Description: Classe per la selezione dei magistrati firmatari degli eventi
 * in un certo periodo per ufficio</p>
 * <p> </p>
 * <p>Company: Bull Italia S.p.A.</p>
 *  not attributable
 *
 */
public class MagistratoFirmatarioSqlDAO extends SIAPSqlDAO
{
  public MagistratoFirmatarioSqlDAO(Connection con)
  {
    super(con);
  }

  /**
   * selezione dei magistrati firmatari degli eventi
   * in un certo periodo per ufficio
   * @param aUfficio
   * @param aInizio
   * @param aFine
   */
  public void ricercaMagistratoFirmatario(String aUfficio, String aInizio, String aFine)
  {
/*  Luigi 25-01-2008
 * Sostituito per eliminare Magistrato null o '-'
 */
    String lStatement = "select distinct decode(b.cognome,'-',' MAGISTRATO NON ASSEGNATO (-)', null,' MAGISTRATO NULLO', ' ',' MAGISTRATO NULLO',b.cognome)" +
      " cognome_mag, b.nome, a.cod_magistrato CodMagistrato " +
      " from evento a,w_magistrato b" +
      " where a.cod_ufficio_inserimento='" + aUfficio + "'"+
      " and a.data_emissione between to_date('" + aInizio + "','dd/mm/yyyy') and to_date('" + aFine + "','dd/mm/yyyy')" +
      " and a.cod_magistrato=b.cod_magistrato(+)" +
      " order by cognome_mag";
 /*
  * Luigi 25-2-2008
  * Ripristinato vecchia select !
  * Who works and re-works never looses time.
  *   
	    String lStatement = "select distinct b.cognome" +
	      " cognome_mag, b.nome, a.cod_magistrato CodMagistrato " +
	      " from evento a,w_magistrato b" +
	      " where a.cod_ufficio_inserimento='" + aUfficio + "'" +
	      " and 	  a.data_emissione between to_date('" + aInizio + "','dd/mm/yyyy') and to_date('" + aFine + "','dd/mm/yyyy')" +
	      " and a.cod_magistrato=b.cod_magistrato" +
	      " and a.cod_magistrato <> '-'" +
	      " order by cognome_mag";
 */  
    
    setStatement(lStatement);

  }
  
  // NGG
  /*
  public void ricercaMagistratoFirmatario(String aUfficio, String aInizio, String aFine, String Accorpato1, String Accorpato2, String Accorpato3)
  {
    String lStatement = "select distinct decode(b.cognome,'-',' MAGISTRATO NON ASSEGNATO (-)', null,' MAGISTRATO NULLO',b.cognome)" +
      " cognome_mag, b.nome, a.cod_magistrato CodMagistrato " +
      " from evento a,w_magistrato b" +
      " where (a.cod_ufficio_inserimento='" + aUfficio + "'" ;
    
    if(!Accorpato1.equals("-"))
    	lStatement+=" or a.cod_ufficio_inserimento='" + Accorpato1 + "'";
    if(!Accorpato2.equals("-"))
    	lStatement+=" or a.cod_ufficio_inserimento='" + Accorpato2 + "'";
    if(!Accorpato3.equals("-"))
    	lStatement+=" and a.cod_ufficio_inserimento='" + Accorpato3 + "'";
      
    lStatement+= ") and a.data_emissione between to_date('" + aInizio + "','dd/mm/yyyy') and to_date('" + aFine + "','dd/mm/yyyy')" +
      " and a.cod_magistrato=b.cod_magistrato(+)" +
      " order by cognome_mag";
    
    setStatement(lStatement);
  }

*/

  // NGG
  public void ricercaW_Magistrato(String aCodice)
  {
/*  Luigi 25-01-2008
 * Sostituito per eliminare Magistrato null o '-'
 */
    String lStatement = "select distinct decode(cognome,'-',' MAGISTRATO NON ASSEGNATO (-)', null,' MAGISTRATO NULLO',cognome)" +
      " cognome_mag, nome, cod_magistrato CodMagistrato " +
      " from w_magistrato " +
      " where cod_magistrato='" + aCodice + "'"+
       " order by cognome_mag";
    
    setStatement(lStatement);

  }
  
  public GenericModel getModel() throws DAOException
  {
    MagistratoModel aModel = new MagistratoModel();

    aModel.setCognome(getString("cognome_mag"));
    aModel.setNome(getString("nome"));
    aModel.setCodMagistrato(getString("CodMagistrato"));

    return aModel;

  }
  
  /**
   * Selezione dei magistrati associati 
   * ad oggetti SIUS già estratti nella tabella  
   * ISP_ESTRAZIONE_OGGETTI_TRIB
   * per elaborazioni statistiche.
   */
  public void ricercaMagistratoProSIUS(String aUfficio) /* mod. michele 2/12/2008 */
  {
    String lStatement = "select distinct decode(cognome,'-',' MAGISTRATO NON ASSEGNATO (-)', null,' MAGISTRATO NULLO',cognome)" +
      " cognome_mag, nome, cod_magistrato CodMagistrato " +
      " from w_magistrato" +
      " where COD_MAGISTRATO in " +
      " (select COD_MAGISTRATO from ISP_ESTRAZIONE_OGGETTI_TRIB where FAS_SIU_CHIAVE_UFFICIO = " + "'" + aUfficio + "')" +  /* mod. michele 2/12/2008 */
      " union " + 
      " select distinct decode(cognome ,'-',' esperto NON ASSEGNATO (-)', NULL,' esperto NULLO',cognome) " + 
      " cognome_mag, nome || '(ESPERTO)', TO_CHAR(id_esperto) || ' (ESPERTO)'  CodMagistrato FROM ESPERTO " +
      " WHERE to_char(id_esperto) || ' (ESPERTO)' IN " +
      " (select cod_magistrato from ISP_ESTRAZIONE_OGGETTI_TRIB where FAS_SIU_CHIAVE_UFFICIO = " + "'" + aUfficio + "')" + 
      " order by cognome_mag";

    setStatement(lStatement);

  }
  
  
  public void ricercaMagistratoByCod(String aCodMag)
  {
    String lStatement = "select distinct decode(cognome,'-',' MAGISTRATO NON ASSEGNATO (-)', null,' MAGISTRATO NULLO',cognome)" +
      " cognome_mag, nome, cod_magistrato CodMagistrato " +
      " from w_magistrato" +
      " where COD_MAGISTRATO = " + "'" + aCodMag + "'";
    setStatement(lStatement);

  }

  /**
   * Selezione dei magistrati associati 
   * ad oggetti SIUS già estratti nella tabella  
   * ISP_PROC_INTERVALLI
   * per elaborazioni statistiche.
   * 
   * metodo creato per anomalia del collaudo 11.3 (terza sessione)
   */
  public void ricercaMagistratiProcIntervalli(String aUfficio) 
  {
    String lStatement = "select distinct decode(cognome,'-',' MAGISTRATO NON ASSEGNATO (-)', null,' MAGISTRATO NULLO',cognome)" +
      " cognome_mag, nome, cod_magistrato CodMagistrato " +
      " from w_magistrato" +
      " where COD_MAGISTRATO in " +
      " (select COD_MAGISTRATO from ISP_PROC_INTERVALLI where FAS_SIU_CHIAVE_UFFICIO = " + "'" + aUfficio + "')" +  /* mod. michele 2/12/2008 */
      " union " + 
      " select distinct decode(cognome ,'-',' esperto NON ASSEGNATO (-)', NULL,' esperto NULLO',cognome) " + 
      " cognome_mag, nome || '(ESPERTO)', TO_CHAR(id_esperto) || ' (ESPERTO)'  CodMagistrato FROM ESPERTO " +
      " WHERE to_char(id_esperto) || ' (ESPERTO)' IN " +
      " (select cod_magistrato from ISP_PROC_INTERVALLI where FAS_SIU_CHIAVE_UFFICIO = " + "'" + aUfficio + "')" + 
      " order by cognome_mag";

    setStatement(lStatement);

  } 
  
}