/**
 * 
 */
package siap.siep.statoesecuzione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.dao.SIAPSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;

/**
 * @author Giselda De Vita
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatoEsecuzioneSqlDAO extends SIAPSqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzioneSqlDAO(Connection aConn) {
		super(aConn);
	}

	public void ricercaEventoByFascicoloSiepXStampa(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		// ---GDV 22122003 -- tutti gli eventi lStatement += " AND COD_TIPO_EVENTO IN ('01','02','04')"; // Le
		// ISTANZE vengono cercate in una select a parte
		lStatement += " AND FLAG_STAMPA_SIEP = 'S'";
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO = 'S'";
		lStatement += " AND COD_TIPO_PROVVEDIMENTO <> '02' ";
		lStatement += " AND COD_TIPO_PROVVEDIMENTO <> '03' ";

		// Gli eventi vengono ordinati
		// per data emissione, data inserimento, id evento
		lStatement += " ORDER BY DATA_EMISSIONE , DATA_INSERIMENTO, ID_EVENTO";

		setStatement(lStatement);
	}

	protected String getSqlQuery() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		// GDV
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";

		lStatement += " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
		lStatement += " UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFF_EMITTENTE , ";
		lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " COGNOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " DATA_EMISSIONE, ";
		lStatement += " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
		lStatement += " FLAG_PIU_MENO, ";
		lStatement += " DATA_TRASMISSIONE_ATTI, ";
		lStatement += " DATA_RICEZIONE_ATTI, ";
		lStatement += " COD_UFFICIO_DESTINATARIO, ";
		lStatement += " ANNO_PROTOCOLLO, ";
		lStatement += " PROGR_PROTOCOLLO, ";
		lStatement += " COD_OPERATORE_INSERIMENTO, ";
		lStatement += " DATA_INSERIMENTO,";
		lStatement += " COD_UFFICIO_INSERIMENTO,";
		lStatement += " COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " DATA_AGGIORNAMENTO,";
		lStatement += " COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE,  ";
		lStatement += " FLAG_STAMPA_SIEP,  ";
		lStatement += " FLAG_STAMPA_SIUS,  ";
		lStatement += " FLAG_VIDEO_SIEP,  ";
		lStatement += " FLAG_VIDEO_SIUS,  ";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP,  ";
		lStatement += " EVE_ID_EVENTO,  ";
		lStatement += " EVE_ID_EVENTO_REVOCA, ";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
		lStatement += " PEN_ID_PENA_RESIDUA ";
		lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI ";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND (CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' OR CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') AND"; // ****
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE ";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";

		return lStatement;
	}

	/**
	 * ricerca Anno Numero Sius
	 * 
	 * @param aKey
	 * @param lCodTipoProvv
	 * @throws DAOException
	 */
	public void ricercaAnnoNumeroSius(BigDecimal aKey, String lCodTipoProvv) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--XX-- - ricercaAnnoNumeroSius INIZIO");
		String lStatement = "SELECT ";

		if (lCodTipoProvv.equals("03"))
			lStatement += "ANNO_S3 ANNO, NUM_S3 NUMERO, FLAG_ELABORATO ELABORATO FROM DEPOSITO_ORDINANZA_PC ";
		else
			lStatement += "ANNO_S72 ANNO, NUM_S72 NUMERO, FLAG_ELABORATO ELABORATO FROM DEPOSITO_DECRETO ";

		lStatement += "WHERE ID_EVENTO_GENERATO=" + aKey;

		setStatement(lStatement);
	}

	/**
	 * Ricerca dei tenori
	 * 
	 * @param aKey
	 * @param lCodTipoProvv
	 * @return
	 * @throws DAOException
	 */
	public Vector ricercaTenori(BigDecimal aKey, String lCodTipoProvv) throws DAOException {
		String lStatement = " SELECT  id_tenore, cod_esito_tenore, esito.rv_meaning descr_esito, "
				+ "      cod_oggetto_tenore, oggetto_tenore.rv_meaning desc_oggetto_tenore,"
				+ "      progr_tenore, PESO_TENORE.RV_HIGH_VALUE PESO_ESITO_TENORE" + " FROM tenore ten,"
				+ "      cg_ref_codes oggetto_tenore," + " 	 CG_REF_CODES PESO_TENORE,"
				+ "      cg_ref_codes esito, ";

		if (lCodTipoProvv.equals("03"))
			lStatement += "deposito_ordinanza_pc";
		else
			lStatement += "	deposito_decreto";

		lStatement += " WHERE " + " id_evento_generato = " + aKey + " and"
				+ "  (oggetto_tenore.rv_low_value = cod_oggetto_tenore)"
				+ "  AND (oggetto_tenore.rv_domain = 'MOTIVO_PROVVEDIMENTO')"
				+ "  AND (esito.rv_low_value = cod_esito_tenore)"
				+ "  AND (esito.rv_domain = 'ESITO_PROVVEDIMENTO')"
				+ "  AND (PESO_TENORE.RV_DOMAIN = 'PESO_ESITO_TENORE' AND PESO_TENORE.RV_LOW_VALUE = COD_ESITO_TENORE) ";

		if (lCodTipoProvv.equals("03"))
			lStatement += "  AND dep_opid_deposito_ordinanza_pc = id_deposito_ordinanza_pc";
		else
			lStatement += "AND DEP_DEC_ID_DEPOSITO_DECRETO = id_deposito_decreto";

		lStatement += "  ORDER BY peso_tenore.rv_high_value, ten.cod_oggetto_tenore";

		setStatement(lStatement);

		Vector lTenori = new Vector();

		start();
		while (next()) {
			TenoreModel lTenore = new TenoreModel();
			lTenore.setIdTenore(getBigDecimal("id_tenore"));
			lTenore.setCodEsitoTenore(getString("cod_esito_tenore"));
			lTenore.setDescrEsitoTenore(getString("descr_esito"));
			lTenore.setCodOggettoTenore(getString("cod_oggetto_tenore"));
			lTenore.setDescrOggettoTenore(getString("desc_oggetto_tenore"));
			lTenore.setProgrTenore(getBigDecimal("progr_tenore"));

			lTenori.add(lTenore);
		}

		stop();
		return lTenori;
	}

	/**
	 * Metodo per l'archiviazione che recupera i dati del fascicolo che cumula.
	 * 
	 * @param aKeyFascicolo
	 * @return FascicoloSiepModel
	 * @throws DAOException
	 */
	public FascicoloSiepModel ricercaFascicoloCumulo(BigDecimal aKeyFascicolo) throws DAOException {
		FascicoloSiepModel lReturn = new FascicoloSiepModel();

		String lStatement = " SELECT ANNO_FASCICOLO_UNIONE , " + " DATA_ARCHIVIAZIONE, " + " DATA_UNIONE, "
				+ " NUM_FASCICOLO_UNIONE, " + " COD_UFFICIO_UNIONE, "
				+ " DESCR_TIPO_UFFUNIONE.RV_MEANING DESCR_TIPO_UFFICIO_UNIONE, "
				+ " DESCR_COM_UFFUNIONE.DESCRIZIONE DESCR_COMUNE_UFFICIO_UNIONE   "
				+ " FROM FASCICOLO_SIEP FASC, " + "  UFFICIO UFFUNIONE, "
				+ " CG_REF_CODES DESCR_TIPO_UFFUNIONE, " + " COMUNE DESCR_COM_UFFUNIONE  " + " WHERE "
				+ " (DESCR_TIPO_UFFUNIONE.RV_DOMAIN = 'TIPO_UFFICIO' AND UFFUNIONE.COD_TIPO_UFFICIO = DESCR_TIPO_UFFUNIONE.RV_LOW_VALUE) AND "
				+ " (COD_UFFICIO_UNIONE = UFFUNIONE.COD_UFFICIO) AND (UFFUNIONE.COD_COMUNE = DESCR_COM_UFFUNIONE.COD_COMUNE)  "
				+ " AND ID_FASCICOLO_SIEP = " + aKeyFascicolo;

		setStatement(lStatement);

		start();
		while (next()) {

			lReturn.setAnnoFascicoloUnione(getString("ANNO_FASCICOLO_UNIONE"));
			lReturn.setNumFascicoloUnione(getString("NUM_FASCICOLO_UNIONE"));
			lReturn.setDescrTipoUfficioUnione(getString("DESCR_TIPO_UFFICIO_UNIONE"));
			lReturn.setDescrComuneUfficioUnione(getString("DESCR_COMUNE_UFFICIO_UNIONE"));
			lReturn.setDataArchiviazione(getDate("DATA_ARCHIVIAZIONE"));
			lReturn.setDataUnione(getDate("DATA_UNIONE"));
		}

		return lReturn;

	}

}