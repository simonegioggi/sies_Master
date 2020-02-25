package siap.siep.motivoevento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.motivoevento.model.MotivoEventoModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: MotivoEventoSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella MotivoEvento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class MotivoEventoSqlDAO extends SqlDAO
{
	 public MotivoEventoSqlDAO (Connection con)
    {
      super(con);
    }


  public void ricercaMotivoEventoByEveIdEvento( BigDecimal aKey)	 throws DAOException
		{
			 String lSql = getSqlQuery();
		   lSql += " AND EVE_ID_EVENTO = "+ aKey;
			 setStatement(lSql);
		}


  protected String getSqlQuery()
		{			 String lStatement = new String("");

			 lStatement += " SELECT " +
				 "ID_MOTIVO_EVENTO, "+
				 "COD_MOTIVO_REVOCA, CODREVO.RV_MEANING MOTIVO_REVOCA, "+
				 "COD_MOTIVO_REVOCA_PM, CODREVOPM.RV_MEANING MOTIVO_REVOCA_PM, "+
				 "MOTIVAZIONI, "+
				 "EVE_ID_EVENTO ";
			 lStatement += " FROM MOTIVO_EVENTO, CG_REF_CODES CODREVO, CG_REF_CODES CODREVOPM";
			 lStatement += " WHERE ";
       lStatement += " CODREVO.RV_DOMAIN = 'REVOCA_DECSOSP'" ;
       lStatement += " AND COD_MOTIVO_REVOCA = CODREVO.RV_LOW_VALUE ";
       lStatement += " AND CODREVOPM.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'" ;
       lStatement += " AND COD_MOTIVO_REVOCA_PM = CODREVOPM.RV_LOW_VALUE ";


       return lStatement;
     }


  //
  // METODO GETMODEL()
  //
	 public GenericModel  	 getModel() throws DAOException
  		{
				 MotivoEventoModel aModel = new  MotivoEventoModel();
				 aModel.setIdMotivoEvento(getBigDecimal("ID_MOTIVO_EVENTO") );
				 aModel.setCodMotivoRevoca(getString("COD_MOTIVO_REVOCA") );
				 aModel.setDescrMotivoRevoca(getString("MOTIVO_REVOCA") );
				 aModel.setCodMotivoRevocaPm(getString("COD_MOTIVO_REVOCA_PM") );
				 aModel.setDescrMotivoRevocaPm(getString("MOTIVO_REVOCA_PM") );
				 aModel.setMotivazioni(getString("MOTIVAZIONI") );
				 aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO") );
				 return aModel;
		}

}