package siap.sius.sanzionesostitutiva.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;

/**
 * MEV_2026-124_SG: ricerca dell'ultima Ordinanza di Sospensione Esecuzione Pene Sostitutive validata,
 * non emessa dal TDS e non ancora annotata come Periodo Altra Sanzione, per precompilare la form di
 * annotazione della sospensione.
 */
public class OrdinanzaSospensioneDaAnnotareSqlDAO extends SIAPSqlDAO {

	public OrdinanzaSospensioneDaAnnotareSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void ricerca(BigDecimal aIdFascicoloSius) {
		String lStatement = "SELECT * FROM (SELECT o.ID_EVENTO_GENERATO, o.DATA_SOSPENSIONE_SS, "
				+ "o.DATA_SCADENZA_SOSPENSIONE_SS, o.SOSPENSIONE_AA, o.SOSPENSIONE_MM, o.SOSPENSIONE_GG "
				+ "FROM DEPOSITO_ORDINANZA_PC o, EVENTO e, UFFICIO u "
				+ "WHERE o.COD_TIPO_ORDINANZA = '35' AND e.ID_EVENTO = o.ID_EVENTO_GENERATO "
				+ "AND e.FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFascicoloSius
				+ " AND e.FLAG_DOCUMENTO_REGISTRATO = 'S' AND o.DATA_SOSPENSIONE_SS IS NOT NULL "
				+ "AND u.COD_UFFICIO = o.COD_UFFICIO_INSERIMENTO AND u.COD_TIPO_UFFICIO <> 'TDS' "
				+ "AND NOT EXISTS (SELECT 1 FROM PERIODO_ALTRA_SANZIONE p "
				+ "WHERE p.EVE_ID_EVENTO = o.ID_EVENTO_GENERATO) "
				+ "ORDER BY o.DATA_INSERIMENTO DESC) WHERE ROWNUM = 1";
		setStatement(lStatement);
	}

	public GenericModel getModel() throws DAOException {
		PeriodoAltraSanzioneModel lModel = new PeriodoAltraSanzioneModel();
		lModel.setEveIdEvento(getBigDecimal("ID_EVENTO_GENERATO"));
		lModel.setDataInizioEsecuzione(getDate("DATA_SOSPENSIONE_SS"));
		lModel.setDataScadenza(getDate("DATA_SCADENZA_SOSPENSIONE_SS"));
		lModel.setSospensioneAA(getBigDecimal("SOSPENSIONE_AA"));
		lModel.setSospensioneMM(getBigDecimal("SOSPENSIONE_MM"));
		lModel.setSospensioneGG(getBigDecimal("SOSPENSIONE_GG"));
		return lModel;
	}
}
