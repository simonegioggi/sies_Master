package siap.siep.scambiosanzione.dao;

import java.math.BigDecimal;
import java.sql.Connection;

/**
 * Ricerca dei provvedimenti di Sorveglianza (SIUS) relativi alle pene sostitutive (Riforma Cartabia) da
 * annotare sul procedimento SIEP. I dati sono letti direttamente dall'EVENTO SIUS collegato al fascicolo SIEP
 * e restituiti con le stesse colonne di SCAMBIO_SANZIONE, in modo da riutilizzare il model e la popup di
 * selezione esistenti. ID_SCAMBIO_SANZIONE contiene l'id dell'evento SIUS.
 *
 * @author SG
 * @since 20261124-MEV_2026-124_SG
 */
public class ProvvedimentoPenaSostitutivaSqlDAO extends ScambioSanzioneSqlDAO {

	public ProvvedimentoPenaSostitutivaSqlDAO(Connection con) {
		super(con);
	}

	private String getSqlQueryPS() {
		StringBuilder lSql = new StringBuilder();

		lSql.append(" SELECT EVENTO.ID_EVENTO ID_SCAMBIO_SANZIONE, ");
		lSql.append(" EVENTO.COD_TIPO_PROVVEDIMENTO COD_TIPO_DECISIONE, ");
		lSql.append(" D_TIPO_DECISIONE.RV_MEANING DESCR_TIPO_DECISIONE, ");
		lSql.append(" EVENTO.COD_ESITO COD_NATURA_SANZIONE, ");
		lSql.append(" D_ESITO.RV_MEANING DESCR_NATURA_SANZIONE, ");
		lSql.append(" EVENTO.COD_MOTIVO COD_TIPO_SANZIONE, ");
		lSql.append(" D_MOTIVO.RV_MEANING DESCR_TIPO_SANZIONE, ");
		lSql.append(" CAST(NULL AS DATE) DATA_INIZIO, CAST(NULL AS DATE) DATA_FINE, ");
		lSql.append(" CAST(NULL AS VARCHAR2(10)) NOTE, ");
		lSql.append(" COALESCE(DEP.ANNO_S3, DDEC.ANNO_S72) ANNO_REGISTRO, ");
		lSql.append(" COALESCE(DEP.NUM_S3, DDEC.NUM_S72) NUMERO_REGISTRO, ");
		lSql.append(" FS.CHIAVE_ANNO CHIAVE_ANNO_FASCICOLO_SIUS, ");
		lSql.append(" FS.CHIAVE_PROGR CHIAVE_PROGR_FASCICOLO_SIUS, ");
		lSql.append(" FS.CHIAVE_UFFICIO COD_UFFICIO_SORVEGLIANZA, ");
		lSql.append(" D_UFF_SORV.DESCR_TIPO_UFFICIO DESCR_UFFICIO_SORVEGLIANZA, ");
		lSql.append(" D_UFF_SORV.DESCR_COMUNE COMUNE_UFFICIO_SORVEGLIANZA, ");
		lSql.append(" EVENTO.COD_UFFICIO_EMITTENTE COD_UFFICIO_EMITTENTE, ");
		lSql.append(" D_UFF_EM.DESCR_TIPO_UFFICIO DESCR_UFFICIO_EMITTENTE, ");
		lSql.append(" D_UFF_EM.DESCR_COMUNE COMUNE_UFFICIO_EMITTENTE, ");
		lSql.append(" EVENTO.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, ");
		lSql.append(" EVENTO.DATA_INSERIMENTO DATA_INSERIMENTO, ");
		lSql.append(" EVENTO.COD_UFFICIO_INSERIMENTO COD_UFFICIO_INSERIMENTO, ");
		lSql.append(" D_UFF_INS.DESCR_TIPO_UFFICIO DESCR_UFFICIO_INSERIMENTO, ");
		lSql.append(" EVENTO.COD_OPERATORE_AGGIORNAMENTO COD_OPERATORE_AGGIORNAMENTO, ");
		lSql.append(" EVENTO.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO, ");
		lSql.append(" EVENTO.COD_UFFICIO_AGGIORNAMENTO COD_UFFICIO_AGGIORNAMENTO, ");
		lSql.append(" D_UFF_AGG.DESCR_TIPO_UFFICIO DESCR_UFFICIO_AGGIORNAMENTO, ");
		lSql.append(" EVENTO.DATA_EMISSIONE DATA_EMISSIONE, ");
		lSql.append(" EVENTO.ID_EVENTO EVE_ID_EVENTO, ");
		lSql.append(" EVENTO.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, ");
		lSql.append(" CAST(NULL AS NUMBER) NUM_GIORNI_RECLUSIONE, CAST(NULL AS NUMBER) NUM_MESI_RECLUSIONE, ");
		lSql.append(" CAST(NULL AS NUMBER) NUM_ANNI_RECLUSIONE, CAST(NULL AS NUMBER) NUM_GIORNI_ARRESTO, ");
		lSql.append(" CAST(NULL AS NUMBER) NUM_MESI_ARRESTO, CAST(NULL AS NUMBER) NUM_ANNI_ARRESTO ");
		lSql.append(" FROM EVENTO ");
		lSql.append(" LEFT OUTER JOIN FASCICOLO_SIUS FS ON (FS.ID_FASCICOLO_SIUS = EVENTO.FAS_SIU_ID_FASCICOLO_SIUS) ");
		lSql.append(" LEFT OUTER JOIN DEPOSITO_ORDINANZA_PC DEP ON (DEP.ID_EVENTO_GENERATO = EVENTO.ID_EVENTO) ");
		lSql.append(" LEFT OUTER JOIN DEPOSITO_DECRETO DDEC ON (DDEC.ID_EVENTO_GENERATO = EVENTO.ID_EVENTO) ");
		lSql.append(" LEFT OUTER JOIN CG_REF_CODES D_TIPO_DECISIONE ON (EVENTO.COD_TIPO_PROVVEDIMENTO = D_TIPO_DECISIONE.RV_LOW_VALUE");
		lSql.append("   AND D_TIPO_DECISIONE.RV_DOMAIN = 'TIPO_PROVVEDIMENTO') ");
		lSql.append(" LEFT OUTER JOIN CG_REF_CODES D_ESITO ON (EVENTO.COD_ESITO = D_ESITO.RV_LOW_VALUE");
		lSql.append("   AND D_ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO') ");
		lSql.append(" LEFT OUTER JOIN CG_REF_CODES D_MOTIVO ON (EVENTO.COD_MOTIVO = D_MOTIVO.RV_LOW_VALUE");
		lSql.append("   AND D_MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') ");
		lSql.append(" LEFT OUTER JOIN UFFICIO_DESCR D_UFF_SORV ON (FS.CHIAVE_UFFICIO = D_UFF_SORV.COD_UFFICIO) ");
		lSql.append(" LEFT OUTER JOIN UFFICIO_DESCR D_UFF_EM ON (EVENTO.COD_UFFICIO_EMITTENTE = D_UFF_EM.COD_UFFICIO) ");
		lSql.append(" LEFT OUTER JOIN UFFICIO_DESCR D_UFF_INS ON (EVENTO.COD_UFFICIO_INSERIMENTO = D_UFF_INS.COD_UFFICIO) ");
		lSql.append(" LEFT OUTER JOIN UFFICIO_DESCR D_UFF_AGG ON (EVENTO.COD_UFFICIO_AGGIORNAMENTO = D_UFF_AGG.COD_UFFICIO) ");
		lSql.append(" WHERE EVENTO.FAS_SIU_ID_FASCICOLO_SIUS IS NOT NULL ");
		lSql.append(" AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03' ");
		lSql.append(" AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' ");

		return lSql.toString();
	}

	/**
	 * Provvedimenti SIUS (ordinanze depositate) con uno dei motivi indicati, non ancora annotati sul
	 * fascicolo SIEP.
	 */
	public void ricercaProvvedimentiPS(BigDecimal aFascicoloSiep, String[] aMotivi) {
		StringBuilder lSql = new StringBuilder(getSqlQueryPS());

		lSql.append(" AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = ").append(aFascicoloSiep);
		lSql.append(" AND EVENTO.COD_MOTIVO IN (");
		for (int i = 0; i < aMotivi.length; i++) {
			if (i > 0)
				lSql.append(",");
			lSql.append("'").append(aMotivi[i]).append("'");
		}
		lSql.append(")");
		// 20261127-MEV_2026-124_SG: le conversioni di pene pecuniarie sono annotabili solo se l'esito e' una pena sostitutiva
		lSql.append(" AND (EVENTO.COD_MOTIVO NOT IN ('3180','3181') OR EVENTO.COD_ESITO IN ('0156','0157','0276','0277','0278'))");
		lSql.append(" AND NOT EXISTS (SELECT 1 FROM EVENTO ANN WHERE ANN.EVE_ID_EVENTO = EVENTO.ID_EVENTO");
		lSql.append("   AND ANN.FAS_SIE_ID_FASCICOLO_SIEP = EVENTO.FAS_SIE_ID_FASCICOLO_SIEP");
		lSql.append("   AND ANN.FAS_SIU_ID_FASCICOLO_SIUS IS NULL AND ANN.COD_TIPO_PROVVEDIMENTO = '04')");
		lSql.append(" ORDER BY EVENTO.DATA_EMISSIONE DESC, EVENTO.ID_EVENTO DESC ");

		setStatement(lSql.toString());
	}

	// 20261127-MEV_2026-124_SG: ultimo provvedimento SIUS di applicazione/conversione pena sostitutiva (annotato o meno)
	public void ricercaUltimoProvvedimentoPS(BigDecimal aFascicoloSiep) {
		StringBuilder lSql = new StringBuilder("SELECT * FROM (");
		lSql.append(getSqlQueryPS());
		lSql.append(" AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = ").append(aFascicoloSiep);
		lSql.append(" AND EVENTO.COD_MOTIVO IN ('2900','2901','3180','3181')");
		lSql.append(" AND (EVENTO.COD_MOTIVO NOT IN ('3180','3181') OR EVENTO.COD_ESITO IN ('0156','0157','0276','0277','0278'))");
		lSql.append(" ORDER BY EVENTO.DATA_EMISSIONE DESC, EVENTO.ID_EVENTO DESC ) WHERE ROWNUM = 1");
		setStatement(lSql.toString());
	}

	public void ricercaProvvedimentoPSByIdEvento(BigDecimal aIdEvento) {
		setStatement(getSqlQueryPS() + " AND EVENTO.ID_EVENTO = " + aIdEvento);
	}
}
