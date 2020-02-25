package siap.sius.avvocatura.dao;

import it.eng.giustizia.avvocatura.util.PropertyUtil;

import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sius.avvocatura.model.AvvisiElencoModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class AvvisiAvvocatoSqlDAO extends SqlDAO {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	public AvvisiAvvocatoSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void ricercaAvvisiSius(String codFiscaleAvv, String codTipoUfficio, String codDistretto,
            String flagVisualizzazione, Date dataInizioRicerca, Date dataFineRicerca ) {
		
		// info per il log
		avvocaturaLogger.info("Starting Point della classe: AvvisiAvvocatoSqlDAO, metodo: ricercaAvvisiSius");

		String lSql = " SELECT *  FROM ( ";
		
		lSql += getSqlQuery();
		
		// aggiungere LE CONDITION IN ARRIVO
		if (PropertyUtil.isPresent(codFiscaleAvv)) {
			lSql += " AND AV.COD_FISCALE = '" + codFiscaleAvv+ "'";
		}

		if (PropertyUtil.isPresent(codTipoUfficio)) {
			lSql += " AND UF.COD_TIPO_UFFICIO = '" + codTipoUfficio+ "'";
		}
		
		if (PropertyUtil.isPresent(codDistretto)) {
			lSql += " AND UF.COD_DISTRETTO = '" + codDistretto+ "'";
		}
		
		if (PropertyUtil.isPresent(flagVisualizzazione) && !"T".equals(flagVisualizzazione)) {
			lSql += " AND A.FLAG_VISUALIZZAZIONE = '" + flagVisualizzazione + "'";
		}

		if (PropertyUtil.isPresent(dataInizioRicerca) && PropertyUtil.isPresent(dataFineRicerca)) {
			lSql += "  AND TRUNC(A.DATA_INSERIMENTO) BETWEEN TO_DATE('"
					+ DateUtils.getDateToString(dataInizioRicerca, "ddMMyyyy")
					+ "', 'DD/MM/YYYY') AND TO_DATE('"
					+ DateUtils.getDateToString(dataFineRicerca, "ddMMyyyy") + "', 'DD/MM/YYYY') ";
		}
		
		lSql += " ORDER by A.DATA_INSERIMENTO DESC ";

		lSql += " ) WHERE ROWNUM <= 50 ORDER BY ROWNUM" ;
		
		setStatement(lSql);
	}

	private String getSqlQuery() {
		
		String lStatement = new String("");

		lStatement += " SELECT A.ID_AVVISO, A.DESC_PROVVEDIMENTO, A.TESTO_AVVISO, A.UFFICIO_EMITTENTE, "
						+ " NVL(COALESCE(DD.DATA_DEPOSITO, DO.DATA_DEPOSITO, DS.DATA_DEPOSITO), E.DATA_EMISSIONE) DATAEMISSIONE_DEPOS,"
						+ " S.CHIAVE_ANNO, S.CHIAVE_PROGR, A.COGNOME_SOGGETTO, A.NOME_SOGGETTO, UD.DATA_UDIENZA,"
						+ " A.FLAG_VISUALIZZAZIONE, A.DATA_INSERIMENTO, E.ID_EVENTO, "
						+ " E.COD_TIPO_PROVVEDIMENTO, E.COD_ESITO, AV.COD_FISCALE, A.UFFICIO_EMITTENTE UFFICIO"
						+ " FROM"
						+ " AVVISI_AVVOCATO A, AVVOCATO AV, UFFICIO UF, "
						+ " EVENTO E LEFT JOIN DEPOSITO_DECRETO DD ON (DD.ID_EVENTO_GENERATO = E.ID_EVENTO)"
						+ " LEFT JOIN DEPOSITO_ORDINANZA_PC DO ON (DO.ID_EVENTO_GENERATO = E.ID_EVENTO) "
						+ " LEFT JOIN DEPOSITO_SENTENZA DS ON (DS.ID_EVENTO_GENERATO = E.ID_EVENTO) "
						// EC@22/05/2017 MODIFICHIAMO QUERY AGGIUNGENDO LE OUTER JOIN PER UDIENZA_PROCEDIMENTO E
						// UDIENZA
						+ " LEFT JOIN UDIENZA_PROCEDIMENTO UP  ON   (UP.EVE_ID_EVENTO=E.ID_EVENTO )"
						+ " LEFT JOIN UDIENZA UD ON   (UD.ID_UDIENZA=UP.UDI_ID_UDIENZA   ),"
						+ " FASCICOLO_SIUS S,"
						//+ " UDIENZA_PROCEDIMENTO UP,"
						//+ " UDIENZA UD,"
						+ " AVVOCATO_FASCICOLO_SIUS AFS" + " WHERE A.COD_UFFICIO_INSERIMENTO = UF.COD_UFFICIO"
						+ " AND A.ID_AVVOCATO = AV.ID_AVVOCATO" + " AND A.ID_EVENTO = E.ID_EVENTO"
						//+ " AND A.ID_EVENTO = UP.EVE_ID_EVENTO"
						+ " AND E.FAS_SIU_ID_FASCICOLO_SIUS = S.ID_FASCICOLO_SIUS"
						//+ " AND UP.UDI_ID_UDIENZA = UD.ID_UDIENZA "
						+ " AND AFS.FAS_SIU_ID_FASCICOLO_SIUS = E.FAS_SIU_ID_FASCICOLO_SIUS "
						+ " AND AFS.DATA_FINE_VALIDITA IS NULL AND AFS.AVV_ID_AVVOCATO = A.ID_AVVOCATO ";

		return lStatement;
		
	}
	
	/**
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getElencoAvvisiModel() throws DAOException {
		AvvisiElencoModel elencoAvvisiModel = new AvvisiElencoModel();

		// Inserire le opportune set delle descrizioni!
		elencoAvvisiModel.setmDescProvvedimento(getString("DESC_PROVVEDIMENTO"));
		elencoAvvisiModel.setmTestoAvviso(getString("TESTO_AVVISO"));
		elencoAvvisiModel.setmUfficioEmittente(getString("UFFICIO_EMITTENTE"));
		elencoAvvisiModel.setmDataDeposito(getDate("DATAEMISSIONE_DEPOS"));
		elencoAvvisiModel.setmAnnoSius(getBigDecimal("CHIAVE_ANNO"));
		elencoAvvisiModel.setmNumeroSius(getBigDecimal("CHIAVE_PROGR"));
		elencoAvvisiModel.setmCodFiscaleAvvocato(getString("COD_FISCALE"));
		elencoAvvisiModel.setmCognomeSoggetto(getString("COGNOME_SOGGETTO"));
		elencoAvvisiModel.setmNomeSoggetto(getString("NOME_SOGGETTO"));
		elencoAvvisiModel.setmDataUdienza(getDate("DATA_UDIENZA"));
		
		elencoAvvisiModel.setmFlagVisualizzazione(getString("FLAG_VISUALIZZAZIONE"));
		elencoAvvisiModel.setmIdAvviso(getBigDecimal("ID_AVVISO"));
		elencoAvvisiModel.setmIdEvento(getBigDecimal("ID_EVENTO"));
		elencoAvvisiModel.setmCodiEsito(getString("COD_ESITO"));
		elencoAvvisiModel.setmCodiTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));		
		elencoAvvisiModel.setmUfficioEmittente(getString("UFFICIO"));		

		return elencoAvvisiModel;		
	}
}
