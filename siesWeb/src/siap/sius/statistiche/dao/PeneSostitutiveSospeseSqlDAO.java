package siap.sius.statistiche.dao;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sius.statistiche.model.PenaSostitutivaSospesaModel;

/**
 * MEV_2026-124_SG: ricerca dei procedimenti con pena sostitutiva sospesa (FLAG_MOTIVO 03) e priva di una
 * successiva ripresa (FLAG_MOTIVO 02) nella tabella PERIODO_ALTRA_SANZIONE.
 */
public class PeneSostitutiveSospeseSqlDAO extends SIAPSqlDAO {

	public PeneSostitutiveSospeseSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void ricercaSospeseSenzaRipresa(String aCodUfficio) {
		String lStatement = "SELECT fasc.ID_FASCICOLO_SIUS, fasc.CHIAVE_ANNO, fasc.CHIAVE_PROGR, "
				+ "sog.COGNOME, sog.NOME, fasc.DATA_ISCRIZIONE, "
				+ "ps.DATA_INIZIO_ESECUZIONE DATA_INIZIO_SOSP, ps.DATA_SCADENZA DATA_FINE_SOSP, "
				+ "ps.SOSPENSIONE_AA, ps.SOSPENSIONE_MM, ps.SOSPENSIONE_GG, ps.COD_TIPO_UFFICIO_SOSP "
				+ "FROM PERIODO_ALTRA_SANZIONE ps, FASCICOLO_SIUS fasc, SOGGETTO sog "
				+ "WHERE fasc.CHIAVE_UFFICIO = '" + aCodUfficio.replace("'", "''") + "' "
				+ "AND ps.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
				+ "AND sog.ID_SOGGETTO = fasc.SOG_ID_SOGGETTO " + "AND ps.FLAG_MOTIVO = '03' "
				+ "AND NOT EXISTS (SELECT 1 FROM PERIODO_ALTRA_SANZIONE pr "
				+ "WHERE pr.FAS_SIU_ID_FASCICOLO_SIUS = ps.FAS_SIU_ID_FASCICOLO_SIUS "
				+ "AND pr.FLAG_MOTIVO = '02' AND pr.DATA_INIZIO_ESECUZIONE > ps.DATA_INIZIO_ESECUZIONE) "
				+ "ORDER BY fasc.CHIAVE_ANNO, fasc.CHIAVE_PROGR, ps.DATA_INIZIO_ESECUZIONE";
		setStatement(lStatement);
	}

	public GenericModel getModel() throws DAOException {
		PenaSostitutivaSospesaModel lModel = new PenaSostitutivaSospesaModel();
		lModel.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lModel.setCognome(getString("COGNOME"));
		lModel.setNome(getString("NOME"));
		lModel.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		lModel.setDataInizioSospensione(getDate("DATA_INIZIO_SOSP"));
		lModel.setDataFineSospensione(getDate("DATA_FINE_SOSP"));
		lModel.setSospensioneAA(getBigDecimal("SOSPENSIONE_AA"));
		lModel.setSospensioneMM(getBigDecimal("SOSPENSIONE_MM"));
		lModel.setSospensioneGG(getBigDecimal("SOSPENSIONE_GG"));
		lModel.setCodTipoUfficioSosp(getString("COD_TIPO_UFFICIO_SOSP"));
		return lModel;
	}
}
