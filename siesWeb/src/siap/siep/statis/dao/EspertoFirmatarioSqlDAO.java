package siap.siep.statis.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sius.esperto.model.EspertoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 *
 * <p>Title: EspertoFirmatarioSqlDAO</p>
 * <p>Description: Classe per la selezione degli esperti relatori (non firmatari in eventi)
 * in un certo periodo per ufficio</p>
 * <p> </p>
 * <p>Company: Bull Italia S.p.A.</p>
 *  not attributable
 *
 */
public class EspertoFirmatarioSqlDAO extends SIAPSqlDAO
{
  public EspertoFirmatarioSqlDAO(Connection con)
  {
    super(con);
  }

    public GenericModel getModel() throws DAOException
  {
    EspertoModel aModel = new EspertoModel();

    aModel.setCognome(getString("cognome_esperto"));
    aModel.setNome(getString("nome"));
    aModel.setIdEsperto(getBigDecimal("IdEsperto"));

    return aModel;

  }
  
  public void ricercaEspertoByCod(String aCodEsperto)
  {
             
	String lStatement = "select distinct decode(cognome,'-',' ESPERTO NON ASSEGNATO (-)', null,' ESPERTO NULLO',cognome)" +
      " cognome_esperto, nome, id_esperto IdEsperto " +
      " from esperto" +
      " where to_char(id_esperto) || ' (ESPERTO)' = " + "'" + aCodEsperto + "'";
    setStatement(lStatement);

  }
  
  
  
}