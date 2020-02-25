package siap.siep.dettaglioprovvedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.dettaglioprovvedimento.model.DettaglioProvvedimentoModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>Title: DettaglioProvvedimentoSqlDAO</p>
 * <p>Description: Classe SqlDAO che rappresenta la tabella DettaglioProvvedimento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class DettaglioProvvedimentoSqlDAO extends SqlDAO
{
	public DettaglioProvvedimentoSqlDAO (Connection con)
	{
		super(con);
	}


	//
	// METODO RICERCA()
	//

	public void ricercaDettaglioProvvedimento( )	 throws DAOException
	{
		String lSql = getSqlQuery();

		setStatement(lSql);
	}


	public void ricercaDettaglioProvvedimentoByKey( BigDecimal aKey)	 throws DAOException
	{
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}


	protected String getSqlQuery()
	{			 String lStatement = new String("");

	lStatement += " SELECT " +
	"COD_TIPO_EVENTO, "+
	"COD_TIPO_PROVVEDIMENTO, "+
	"COD_MOTIVO, "+
	"ACTION_UPLOAD, "+
	"ACTION_DETTAGLIO ";
	lStatement += " FROM dettaglio_provvedimento";
	return lStatement;		}


	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException
	{
		DettaglioProvvedimentoModel aModel = new  DettaglioProvvedimentoModel();

		aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO") );
		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO") );
		aModel.setCodMotivo(getString("COD_MOTIVO") );
		aModel.setActionDettaglio(getString("ACTION_DETTAGLIO") );
		aModel.setChiaveEvento(aModel.getCodTipoEvento() +
				aModel.getCodTipoProvvedimento() +
				aModel.getCodMotivo());
		aModel.setActionUpload(getString("ACTION_UPLOAD") );
		return aModel;
	}


	public String setCondizioniByKey(BigDecimal aKey)
	{
		return " AND ID_dettaglio_provvedimento = " + aKey;
	}
}
