package siap.sige.udienzaprocedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.Utils;
import siap.dao.SIAPSqlDAO;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sige.avvocato.dao.AvvocatoSqlDAO;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.tenore.dao.TenoreSigeSqlDAO;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel;

/**
 * <p>
 * Title: ProcedimentixUdienzaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che consente l'accesso ai Procedimenti
 * </p>
 * fissati per un'Udienza la tabella UdienzaProcedimento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class ProcedimentixUdienzaSqlDAO extends SIAPSqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public ProcedimentixUdienzaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	/**
	 * Metodo che imposta le condizioni di ricerca, dei procedimenti per udienza.
	 * <p>
	 *
	 * @param aIdUdienza
	 *            id dell'udienza.
	 * @param aOrderBy
	 *            tipo di ordinamento.
	 */
	public void ricercaProcedimentixUdienzaByUdienza(BigDecimal aIdUdienza, String aOrderBy)
			throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaProcedimentixUdienzaByUdienza: OrderBy -> " + aOrderBy);

		String lSql = getSqlQuery(aIdUdienza);
		lSql += getOrderBy(aOrderBy);
		setStatement(lSql);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
	}

	/**
	 * Metodo di ricerca procedimenti per udienza.
	 * <p>
	 *
	 * @param aIdUdienza
	 *            id dell'udienza.
	 * @param aOrderBy
	 *            tipo di ordinamnto.
	 * @param aStatoProcedimento
	 *            tipo stato del procedimento.
	 * @param aTipoProc
	 *            tipo procedimento.
	 * @throws DAOException
	 *             propga errore di eccezione.
	 */
	public void ricercaProcedimentiRuoloByUdienza(BigDecimal aIdUdienza, String aOrderBy,
			String aStatoProcedimento, String aTipoProc, String flagModifBlocco, String codMagi) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaProcedimentiRuoloByUdienza: OrderBy -> " + aOrderBy);

		String lSql = getSqlQuery(aIdUdienza);
		lSql += getFiltroTipoProc(aTipoProc);
		lSql += getFiltroStatoProc(aStatoProcedimento);
		lSql += getFiltroFlagModifBlocco(flagModifBlocco);
		lSql += getFiltroCodMagAss(codMagi);
		
		lSql += getOrderBy(aOrderBy);

		setStatement(lSql);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
	}

	public void ricercaProcedimentixUdienzaByUdienzaIdFascicolo(BigDecimal aIdUdienza,
			BigDecimal aIdFascicolo, String aOrderBy, String aStatoProcedimento, String aTipoProc)
			throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaProcedimentixUdienzaByUdienza: OrderBy -> " + aOrderBy);

		String lSql = getSqlQuery_NO_Avv(aIdUdienza);
		// lSql += getFiltroTipoProc(aTipoProc);
		// lSql += getFiltroStatoProc(aStatoProcedimento);
		lSql += getCondizioneByIdFascicolo(aIdFascicolo);
		lSql += getOrderBy(aOrderBy);

		setStatement(lSql);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
	}

	/**
	 * Metodo di ricerca procedimenti per udienza.
	 * <p>
	 *
	 * @param aIdUdienza
	 *            id dell'udienza.
	 * @param aOrderBy
	 *            tipo di ordinamnto.
	 * @param aStatoProcedimento
	 *            tipo stato del procedimento.
	 * @param aTipoProc
	 *            tipo procedimento.
	 * @throws DAOException
	 *             propga errore di eccezione.
	 */
	public void ricercaProcedimentixUdienzaByUdienza(BigDecimal aIdUdienza, String aCodMagistrato,
			String aOrderBy, String aStatoProcedimento, String aTipoProc) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaProcedimentixUdienzaByUdienza: OrderBy -> " + aOrderBy);

		String lSql = getSqlQuery_NO_Avv(aIdUdienza);
		lSql += getFiltroTipoProc(aTipoProc);
		lSql += getFiltroStatoProc(aStatoProcedimento);
		lSql += getFiltroCodMagistrato(aCodMagistrato);
		lSql += getOrderBy(aOrderBy);

		setStatement(lSql);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
	}

	/**
	 * Metodo di ricerca procedimenti per data udienza.
	 * <p>
	 *
	 * @param aDataUdienza
	 *            data udienza.
	 * @param aOrderBy
	 *            tipo di ordinamnto.
	 * @param aStatoProcedimento
	 *            tipo stato del procedimento.
	 * @param aTipoProc
	 *            tipo procedimento.
	 * @throws DAOException
	 *             propga errore di eccezione.
	 */
	public void ricercaProcedimentixUdienzaByDataUdienza(Date aDataUdienza, String aOrderBy,
			String aStatoProcedimento, String aTipoProc, String aCodUfficioConnesso) throws DAOException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaProcedimentixUdienzaByDataUdienza: OrderBy -> " + aOrderBy);

		String lSql = getSqlQuery(aDataUdienza);
		lSql += getFiltroCodUfficio(aCodUfficioConnesso);
		lSql += getFiltroTipoProc(aTipoProc);
		lSql += getFiltroStatoProc(aStatoProcedimento);
		lSql += getOrderBy(aOrderBy);

		setStatement(lSql);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");

	}

	/**
	 * Metodo di ricerca procedimenti per data udienza.
	 * <p>
	 *
	 * @param aDataUdienza
	 *            data udienza.
	 * @param aOrderBy
	 *            tipo di ordinamnto.
	 * @param aStatoProcedimento
	 *            tipo stato del procedimento.
	 * @param aTipoProc
	 *            tipo procedimento.
	 * @throws DAOException
	 *             propga errore di eccezione.
	 */
	public void ricercaProcedimentixUdienzaByDataUdienza(Date aDataUdienza, String aCodMagistrato,
			String aOrderBy, String aStatoProcedimento, String aTipoProc, String aCodUfficioConnesso)
			throws DAOException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaProcedimentixUdienzaByDataUdienza: OrderBy -> " + aOrderBy);

		String lSql = getSqlQuery_NO_Avv(aDataUdienza);
		lSql += getFiltroCodUfficio(aCodUfficioConnesso);
		lSql += getFiltroTipoProc(aTipoProc);
		lSql += getFiltroStatoProc(aStatoProcedimento);
		lSql += getFiltroCodMagistrato(aCodMagistrato);
		lSql += getOrderBy(aOrderBy);

		setStatement(lSql);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");

	}

	/**
	 * Slq Query di base.
	 * <p>
	 *
	 * @param aValue
	 * @return
	 * @throws DAOException
	 */
	protected String getSqlQuery(Object aValue) throws DAOException {
		String lStatement = new String("");
		/*
		 * lStatement += "SELECT DISTINCT " + " UP.ID_UDIENZA_PROCEDIMENTO_SIGE, " + " UP.FLAG_RINVIATA, " +
		 * " UP.DESC_FLAG_UDIENZA.RV_MEANING AS DESC_FLAG_UDIENZA, " + " UP.COD_OPERATORE_INSERIMENTO," +
		 * " UP.DATA_INSERIMENTO, " + " UP.COD_UFFICIO_INSERIMENTO, " + " UP.COD_OPERATORE_AGGIORNAMENTO, " +
		 * " UP.DATA_AGGIORNAMENTO, " + " UP.COD_UFFICIO_AGGIORNAMENTO, " + " UP.FAS_ID_FASCICOLO_SIGE, " +
		 * " UP.UDI_ID_UDIENZA_SIGE, " + " UP.UDI_ID_UDIENZA_RINVIO, " + " UP.EVE_ID_EVENTO, " +
		 * " PROVVEDIMENTO_SIGE.ID_PROVVEDIMENTO_SIGE, "+ " PROVVEDIMENTO_SIGE.ID_EVENTO_GENERATO, " +
		 * " PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE, " + " UDIENZA_SIGE.ID_UDIENZA_SIGE, " +
		 * " UDIENZA_SIGE.DATA_UDIENZA, " + " COLLEGIO.COD_COLLEGIO, " +
		 * " MAS.MAG_COD_MAGISTRATO AS MAG_COD_MAGISTRATO_ASS, " + " MAG.NOME AS NOME_MAGISTRATO, " +
		 * " MAG.COGNOME AS COGNOME_MAGISTRATO, " + " AVV.ID_AVVOCATO, " + " AVV.COGNOME AS COGNOME_AVV, "+
		 * " AVV.NOME AS NOME_AVV, "+ " AVV.FORO AS FORO, "+
		 * " DESC_TIPO_AVV.RV_MEANING AS DESC_TIPO_AVVOCATO,"+ " SEZIONE.DESCRIZIONE AS DESC_SEZIONE, " +
		 * " FASCICOLO_SIGE.CHIAVE_ANNO, " + " FASCICOLO_SIGE.CHIAVE_PROGR, " +
		 * " FASCICOLO_SIGE.COD_POSIZIONE_GIURIDICA, " + " FASCICOLO_SIGE.ID_FASCICOLO_SIGE, " +
		 * " FASCICOLO_SIGE.COD_STATO_FASCICOLO, " +
		 * " DESC_STATO_FASCICOLO.RV_MEANING AS DESC_STATO_FASCICOLO, " +
		 * " DESC_POSIZIONE_GIURIDICA.RV_MEANING AS DESC_POSIZIONE_GIURIDICA, " +
		 * " DESC_ESITO_PROVVEDIMENTO.RV_MEANING AS DESC_ESITO_PROVVEDIMENTO, " +
		 * " DESC_TIPO_PROVVEDIMENTO_SIGE.RV_MEANING AS DESC_TIPO_PROVVEDIMENTO_SIGE, " +
		 * //DESC_OGGETTO_SIGE.RV_MEANING AS DESC_OGGETTO_SIGE, " SOGGETTO.ID_SOGGETTO, " +
		 * " SOGGETTO.COGNOME AS COGNOME, " + " SOGGETTO.NOME NOME, " +
		 * " SOGGETTO.DATA_NASCITA AS DATA_NASCITA, " + " COMUNE.DESCRIZIONE AS DESCR_COMUNE_NASCITA, " +
		 * " PROV.RV_MEANING AS DESCR_PROVINCIA_NASCITA, " + " NAZ.RV_MEANING AS DESCR_STATO_NASCITA " +
		 * " FROM UDIENZA_PROCEDIMENTO_SIGE UP " +
		 * " INNER JOIN UDIENZA_SIGE ON ID_UDIENZA_SIGE = UDI_ID_UDIENZA_SIGE " +
		 * " LEFT OUTER JOIN COLLEGIO ON UDIENZA_SIGE.COL_ID_COLLEGIO = COLLEGIO.ID_COLLEGIO " +
		 * " LEFT OUTER JOIN SEZIONE ON COLLEGIO.SEZ_ID_SEZIONE = SEZIONE.ID_SEZIONE " +
		 * " LEFT OUTER JOIN MAGISTRATO_ASSEGNATARIO MAS ON " +
		 * " MAS.FAS_SIGE_ID_FASCICOLO_SIGE = FAS_ID_FASCICOLO_SIGE AND MAS.DATA_FINE IS NULL " +
		 * " LEFT OUTER JOIN MAGISTRATO MAG ON MAG.COD_MAGISTRATO = MAS.MAG_COD_MAGISTRATO  " +
		 * " INNER JOIN FASCICOLO_SIGE ON ID_FASCICOLO_SIGE = FAS_ID_FASCICOLO_SIGE " +
		 * " INNER JOIN CG_REF_CODES DESC_STATO_FASCICOLO ON " +
		 * " DESC_STATO_FASCICOLO.RV_LOW_VALUE = FASCICOLO_SIGE.COD_STATO_FASCICOLO " +
		 * " AND DESC_STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO' " +
		 * " INNER JOIN SOGGETTO ON ID_SOGGETTO = FASCICOLO_SIGE.SOG_ID_SOGGETTO " +
		 * " INNER JOIN AVVOCATO_FASCICOLO_SIGE AVV_FAS_SIGE ON " +
		 * " AVV_FAS_SIGE.FAS_SIGE_ID_FASCICOLO_SIGE = FASCICOLO_SIGE.ID_FASCICOLO_SIGE " +
		 * " AND AVV_FAS_SIGE.DATA_FINE_VALIDITA IS NULL "+
		 * " INNER JOIN AVVOCATO AVV ON AVV.ID_AVVOCATO = AVV_FAS_SIGE.AVV_ID_AVVOCATO " +
		 * " INNER JOIN CG_REF_CODES DESC_TIPO_AVV ON " +
		 * " AVV_FAS_SIGE.COD_TIPO_AVVOCATO = DESC_TIPO_AVV.RV_LOW_VALUE " +
		 * " AND DESC_TIPO_AVV.RV_DOMAIN ='TIPO_AVVOCATO'" +
		 * " INNER JOIN COMUNE ON SOGGETTO.COD_COMUNE_NASCITA = COMUNE.COD_COMUNE " +
		 * " INNER JOIN CG_REF_CODES PROV ON PROV.RV_LOW_VALUE = SOGGETTO.COD_PROVINCIA_NASCITA " +
		 * " AND PROV.RV_DOMAIN = 'PROVINCIA' " +
		 * " INNER JOIN CG_REF_CODES NAZ ON NAZ.RV_LOW_VALUE = SOGGETTO.COD_STATO_NASCITA " +
		 * " AND NAZ.RV_DOMAIN = 'NAZIONE' " +
		 * " INNER JOIN CG_REF_CODES DESC_POSIZIONE_GIURIDICA ON DESC_POSIZIONE_GIURIDICA.RV_LOW_VALUE = FASCICOLO_SIGE.COD_POSIZIONE_GIURIDICA  "
		 * + " AND DESC_POSIZIONE_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA' " +
		 * " INNER JOIN PROVVEDIMENTO_SIGE ON PROVVEDIMENTO_SIGE.FAS_ID_FASCICOLO_SIGE = FASCICOLO_SIGE.ID_FASCICOLO_SIGE "
		 * + " AND ( FASCICOLO_SIGE.ID_FASCICOLO_SIGE, PROVVEDIMENTO_SIGE.DATA_INSERIMENTO ) " +
		 * "  IN ( SELECT PROVVEDIMENTO_SIGE.FAS_ID_FASCICOLO_SIGE, MAX(PROVVEDIMENTO_SIGE.DATA_INSERIMENTO) FROM PROVVEDIMENTO_SIGE "
		 * + " WHERE (COD_TIPO_PROVVEDIMENTO = '03' OR COD_TIPO_PROVVEDIMENTO = '02') " +
		 * " GROUP BY FAS_ID_FASCICOLO_SIGE ) " +
		 * " INNER JOIN EVENTO ON ID_EVENTO = PROVVEDIMENTO_SIGE.ID_EVENTO_GENERATO " +
		 * " AND EVENTO.COD_ESITO = '0601' " +
		 * " AND ( EVENTO.FLAG_DOCUMENTO_REGISTRATO <>'A' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL ) " +
		 * " INNER JOIN CG_REF_CODES DESC_ESITO_PROVVEDIMENTO ON EVENTO.COD_ESITO = DESC_ESITO_PROVVEDIMENTO.RV_LOW_VALUE "
		 * + " AND DESC_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO' " +
		 * " LEFT OUTER JOIN CG_REF_CODES DESC_FLAG_UDIENZA ON UP.FLAG_RINVIATA = DESC_FLAG_UDIENZA.RV_LOW_VALUE "
		 * + " AND DESC_FLAG_UDIENZA.RV_DOMAIN ='STATO_FLAG_RINVIATA' " +
		 * " INNER JOIN CG_REF_CODES DESC_TIPO_PROVVEDIMENTO_SIGE ON PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE = DESC_TIPO_PROVVEDIMENTO_SIGE.RV_LOW_VALUE "
		 * + " AND DESC_TIPO_PROVVEDIMENTO_SIGE.RV_DOMAIN ='TIPO_PROVVEDIMENTO_SIGE' " ;
		 */

		lStatement += "SELECT DISTINCT " + " UP.ID_UDIENZA_PROCEDIMENTO_SIGE, " + " UP.FLAG_RINVIATA, "
		// NUOVA INFRASTRUTTURA: modificata la query (erased UP.)
				+ " DESC_FLAG_UDIENZA.RV_MEANING AS DESC_FLAG_UDIENZA, " + " UP.COD_OPERATORE_INSERIMENTO,"
				+ " UP.DATA_INSERIMENTO, " + " UP.COD_UFFICIO_INSERIMENTO, "
				+ " UP.COD_OPERATORE_AGGIORNAMENTO, " + " UP.DATA_AGGIORNAMENTO, "
				+ " UP.COD_UFFICIO_AGGIORNAMENTO, " + " UP.FAS_ID_FASCICOLO_SIGE, "
				+ " UP.UDI_ID_UDIENZA_SIGE, " + " UP.UDI_ID_UDIENZA_RINVIO, " + " UP.EVE_ID_EVENTO, "
				+ " PROVVEDIMENTO_SIGE.ID_PROVVEDIMENTO_SIGE, " + " PROVVEDIMENTO_SIGE.ID_EVENTO_GENERATO, "
				+ " PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE, " + " UDIENZA_SIGE.ID_UDIENZA_SIGE, "
				+ " UDIENZA_SIGE.DATA_UDIENZA, " + " COLLEGIO.COD_COLLEGIO, "
				+ " MAS.MAG_COD_MAGISTRATO AS MAG_COD_MAGISTRATO_ASS, " + " MAG.NOME AS NOME_MAGISTRATO, "
				+ " MAG.COGNOME AS COGNOME_MAGISTRATO, " + " AVV.ID_AVVOCATO, "
				+ " AVV.COGNOME AS COGNOME_AVV, " + " AVV.NOME AS NOME_AVV, " + " AVV.FORO AS FORO, "
				+ " DESC_TIPO_AVV.RV_MEANING AS DESC_TIPO_AVVOCATO,"
				+ " SEZIONE.DESCRIZIONE AS DESC_SEZIONE, " + " FASCICOLO_SIGE.CHIAVE_ANNO, "
				+ " FASCICOLO_SIGE.CHIAVE_PROGR, " + " FASCICOLO_SIGE.COD_POSIZIONE_GIURIDICA, "
				+ " FASCICOLO_SIGE.ID_FASCICOLO_SIGE, " + " FASCICOLO_SIGE.COD_STATO_FASCICOLO, "
				+ " DESC_STATO_FASCICOLO.RV_MEANING AS DESC_STATO_FASCICOLO, "
				+ " DESC_POSIZIONE_GIURIDICA.RV_MEANING AS DESC_POSIZIONE_GIURIDICA, "
				+ " DESC_ESITO_PROVVEDIMENTO.RV_MEANING AS DESC_ESITO_PROVVEDIMENTO, "
				+ " DESC_TIPO_PROVVEDIMENTO_SIGE.RV_MEANING AS DESC_TIPO_PROVVEDIMENTO_SIGE, " +
				// DESC_OGGETTO_SIGE.RV_MEANING AS DESC_OGGETTO_SIGE,
				" SOGGETTO.ID_SOGGETTO, " + " SOGGETTO.COGNOME AS COGNOME, " + " SOGGETTO.NOME NOME, "
				+ " SOGGETTO.DATA_NASCITA AS DATA_NASCITA, " + " COMUNE.DESCRIZIONE AS DESCR_COMUNE_NASCITA, "
				+ " PROV.RV_MEANING AS DESCR_PROVINCIA_NASCITA, " + " NAZ.RV_MEANING AS DESCR_STATO_NASCITA, "
				
				// inizio modifica per sies 11.2.1
				+ " PROC.COGNOME                                   AS COGNOME_PROCUR,     "
				+ " PROC.NOME                                      AS NOME_PROCUR,"
				+ " CAN.COGNOME                                    AS COGNOME_CANC,"
				+ " CAN.NOME                                       AS NOME_CANC"				
				// fine modifica
				
				+ " FROM UDIENZA_PROCEDIMENTO_SIGE UP "
				+ " INNER JOIN UDIENZA_SIGE ON ID_UDIENZA_SIGE = UDI_ID_UDIENZA_SIGE "
				+ " LEFT OUTER JOIN COLLEGIO ON UDIENZA_SIGE.COL_ID_COLLEGIO = COLLEGIO.ID_COLLEGIO "
				+ " LEFT OUTER JOIN SEZIONE ON COLLEGIO.SEZ_ID_SEZIONE = SEZIONE.ID_SEZIONE "
				+ " LEFT OUTER JOIN MAGISTRATO_ASSEGNATARIO MAS ON "
				+ " MAS.FAS_SIGE_ID_FASCICOLO_SIGE = FAS_ID_FASCICOLO_SIGE AND MAS.DATA_FINE IS NULL "
				+ " LEFT OUTER JOIN MAGISTRATO MAG ON MAG.COD_MAGISTRATO = MAS.MAG_COD_MAGISTRATO  "
				+ " INNER JOIN FASCICOLO_SIGE ON ID_FASCICOLO_SIGE = FAS_ID_FASCICOLO_SIGE "
				+ " INNER JOIN CG_REF_CODES DESC_STATO_FASCICOLO ON "
				+ " DESC_STATO_FASCICOLO.RV_LOW_VALUE = FASCICOLO_SIGE.COD_STATO_FASCICOLO "
				+ " AND DESC_STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO' "
				+ " INNER JOIN SOGGETTO ON ID_SOGGETTO = FASCICOLO_SIGE.SOG_ID_SOGGETTO "
				// 20170914: [SG] left outer su avvocato
				+ " LEFT OUTER JOIN AVVOCATO_FASCICOLO_SIGE AVV_FAS_SIGE ON "
				+ " AVV_FAS_SIGE.FAS_SIGE_ID_FASCICOLO_SIGE = FASCICOLO_SIGE.ID_FASCICOLO_SIGE "
				+ " AND AVV_FAS_SIGE.DATA_FINE_VALIDITA IS NULL "
				+ " LEFT OUTER JOIN AVVOCATO AVV ON AVV.ID_AVVOCATO = AVV_FAS_SIGE.AVV_ID_AVVOCATO "
				+ " LEFT OUTER JOIN CG_REF_CODES DESC_TIPO_AVV ON "
				+ " AVV_FAS_SIGE.COD_TIPO_AVVOCATO = DESC_TIPO_AVV.RV_LOW_VALUE "
				+ " AND DESC_TIPO_AVV.RV_DOMAIN ='TIPO_AVVOCATO'"
				+ " INNER JOIN COMUNE ON SOGGETTO.COD_COMUNE_NASCITA = COMUNE.COD_COMUNE "
				+ " INNER JOIN CG_REF_CODES PROV ON PROV.RV_LOW_VALUE = SOGGETTO.COD_PROVINCIA_NASCITA "
				+ " AND PROV.RV_DOMAIN = 'PROVINCIA' " +

				" INNER JOIN CG_REF_CODES NAZ ON NAZ.RV_LOW_VALUE = SOGGETTO.COD_STATO_NASCITA "
				+ " AND NAZ.RV_DOMAIN = 'NAZIONE' "
				+ " INNER JOIN CG_REF_CODES DESC_POSIZIONE_GIURIDICA ON DESC_POSIZIONE_GIURIDICA.RV_LOW_VALUE = FASCICOLO_SIGE.COD_POSIZIONE_GIURIDICA  "
				+ " AND DESC_POSIZIONE_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA' " +

				// 2011-01-31
				" INNER JOIN TENORE_SIGE TEN ON TEN.FAS_ID_FASCICOLO_SIGE = ID_FASCICOLO_SIGE "
				+ " AND TEN.DATA_FINE IS NULL "
				+ " INNER JOIN PROVVEDIMENTO_SIGE ON ID_PROVVEDIMENTO_SIGE = TEN.PROV_ID_PROVVEDIMENTO_SIGE "
				+ " AND ( PROVVEDIMENTO_SIGE.DEFINITORIO IN ('S','N') OR PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE IN ('01','04','50')  )"
				+

				/*
				 * " INNER JOIN PROVVEDIMENTO_SIGE ON PROVVEDIMENTO_SIGE.FAS_ID_FASCICOLO_SIGE = FASCICOLO_SIGE.ID_FASCICOLO_SIGE "
				 * + " AND ( FASCICOLO_SIGE.ID_FASCICOLO_SIGE, PROVVEDIMENTO_SIGE.DATA_INSERIMENTO ) " +
				 * "  IN ( SELECT PROVVEDIMENTO_SIGE.FAS_ID_FASCICOLO_SIGE, MAX(PROVVEDIMENTO_SIGE.DATA_INSERIMENTO) FROM PROVVEDIMENTO_SIGE "
				 * + " WHERE (COD_TIPO_PROVVEDIMENTO = '03' OR COD_TIPO_PROVVEDIMENTO = '02') " +
				 * " GROUP BY FAS_ID_FASCICOLO_SIGE ) " +
				 * " INNER JOIN EVENTO ON ID_EVENTO = PROVVEDIMENTO_SIGE.ID_EVENTO_GENERATO " +
				 * " AND EVENTO.COD_ESITO = '0601' " +
				 * " AND ( EVENTO.FLAG_DOCUMENTO_REGISTRATO <>'A' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL ) "
				 * +
				 */
				// 2011-01-31
				" INNER JOIN EVENTO ON ID_EVENTO = PROVVEDIMENTO_SIGE.ID_EVENTO_GENERATO "
				+ " AND ( EVENTO.FLAG_DOCUMENTO_REGISTRATO <>'A' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL ) "
				+

				" INNER JOIN CG_REF_CODES DESC_ESITO_PROVVEDIMENTO ON EVENTO.COD_ESITO = DESC_ESITO_PROVVEDIMENTO.RV_LOW_VALUE "
				+ " AND DESC_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO' "
				+ " LEFT OUTER JOIN CG_REF_CODES DESC_FLAG_UDIENZA ON UP.FLAG_RINVIATA = DESC_FLAG_UDIENZA.RV_LOW_VALUE "
				+ " AND DESC_FLAG_UDIENZA.RV_DOMAIN ='STATO_FLAG_RINVIATA' "
				+ " INNER JOIN CG_REF_CODES DESC_TIPO_PROVVEDIMENTO_SIGE ON PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE = DESC_TIPO_PROVVEDIMENTO_SIGE.RV_LOW_VALUE "
				+ " AND DESC_TIPO_PROVVEDIMENTO_SIGE.RV_DOMAIN ='TIPO_PROVVEDIMENTO_SIGE' "

			   // inizio modifica per sies 11.2.1
				+ " LEFT OUTER JOIN MAGISTRATO PROC ON PROC.COD_MAGISTRATO = MAS.COD_PROCURATORE"
				+ " LEFT OUTER JOIN ASSISTENTE_GIUDIZIARIO CAN  ON CAN.ID_ASSISTENTE_GIUDIZIARIO = MAS.COD_ID_ASSISTENTE "  ;
			   // fine modifica per sies 11.2.1

		// Aggiunge la where condition su id_udienza o data_udienza
		if (aValue instanceof java.math.BigDecimal)
			lStatement += getCondizioneByKey((BigDecimal) aValue);
		else if (aValue instanceof java.util.Date)
			lStatement += getCondizioneByDate((Date) aValue);
		else
			throw new DAOException(
					"Il valore passato non è del tipo : BigDecimal ( IDUdienza ) o Date ( DataUdienza ).");

		return lStatement;
	}

	/**
	 * Metodo che compone, imposta e ritorna la condizione di filtro per l'id dell'udienza.
	 * <p>
	 *
	 * @param aKey
	 *            id dell'udienza.
	 * @return ritorna la condizione di filtro.
	 */
	public String getCondizioneByKey(BigDecimal aKey) {
		return " WHERE UDIENZA_SIGE.ID_UDIENZA_SIGE = " + aKey;
	}

	/**
	 * Metodo che compone, imposta e ritorna la condizione di filtro per la data dell'udienza.
	 * <p>
	 *
	 * @param aDate
	 *            Data dell'udienza.
	 * @return ritorna la condizione di filtro.
	 */
	public String getCondizioneByDate(Date aDate) {
		return " WHERE UDIENZA_SIGE.DATA_UDIENZA = " + " TO_DATE("
				+ DateUtils.getDateToString(aDate, "yyyyMMdd") + ",'YYYYMMDD') ";
	}

	public String getCondizioneByIdFascicolo(BigDecimal aKey) {
		return " AND FASCICOLO_SIGE.ID_FASCICOLO_SIGE = " + aKey;
	}

	/**
	 * Ritorna la condizione filtro su codice ufficio di appartenenza.
	 * <p>
	 *
	 * @param aCodUfficioConnesso
	 * @return
	 */
	public String getFiltroCodUfficio(String aCodUfficioConnesso) {
		String lSql = new String();
		if (aCodUfficioConnesso != null && aCodUfficioConnesso.length() > 1)
			lSql += " AND UDIENZA_SIGE.COD_UFFICIO_APPARTENENZA = '" + aCodUfficioConnesso + "' ";
		return lSql;
	}

	/**
	 * Ritorna la condizione filtro su codice ufficio di appartenenza.
	 * <p>
	 *
	 * @param aCodUfficioConnesso
	 * @return
	 */
	public String getFiltroCodMagistrato(String aCodMagistrato) {
		String lSql = new String();
		if (aCodMagistrato != null && aCodMagistrato.length() > 1)
			lSql += " AND MAS.MAG_COD_MAGISTRATO = '" + aCodMagistrato + "' ";
		return lSql;
	}

	/**
	 *
	 * @param aStatoProc
	 * @return
	 */
	public String getFiltroStatoProc(String aStatoProc) {
		
		siesLogger.info("inizio");
		String lFiltro = new String();
		// 20170803: [SG] aggiunto codice 20 = Decreto Fissazione Udienza (x2) + 14 + 18
		lFiltro = " AND FASCICOLO_SIGE.COD_STATO_FASCICOLO IN ('01','02','07','10','14','18','20','21')";

		if (aStatoProc != null) {
			if (aStatoProc.compareTo("U") == 0) // Procedimenti Unificati '05'
				lFiltro = " AND FASCICOLO_SIGE.COD_STATO_FASCICOLO IN ('01','02','05','07','10','14','18','20', '21')";
		}
		// INTERVERVENT PER NUOVA GESTIONE UDIENZE MONOCRATICHE/COLLEGIALI SIES VERS. 11.2.1
		if (aStatoProc != null) {
			if (aStatoProc.compareTo("ND") == 0) // Procedimenti NON DEFINITI, QUINDI NON PRENDO IL CODICE '01'
				lFiltro = " AND FASCICOLO_SIGE.COD_STATO_FASCICOLO IN ('02','05','10','14','18','20','21')";
		}
		
		siesLogger.info("fine");
		return lFiltro;
	}

	
	/**
	 * 
	 * @param aStatoProc
	 * @return
	 */
	public String getFiltroStatoProcOrd(String aStatoProc) {
		
		siesLogger.info("inizio");
		String lFiltro = new String();
		// 20170803: [SG] aggiunto codice 20 = Decreto Fissazione Udienza (x2) + 14 + 18
		lFiltro = " AND FAS.COD_STATO_FASCICOLO IN ('01','02','07','10','14','18','20','21')";

		if (aStatoProc != null) {
			if (aStatoProc.compareTo("U") == 0) // Procedimenti Unificati '05'
				lFiltro = " AND FAS.COD_STATO_FASCICOLO IN ('01','02','05','07','10','14','18','20', '21')";
		}
		// INTERVERVENT PER NUOVA GESTIONE UDIENZE MONOCRATICHE/COLLEGIALI SIES VERS. 11.2.1
		if (aStatoProc != null) {
			if (aStatoProc.compareTo("ND") == 0) // Procedimenti NON DEFINITI, QUINDI NON PRENDO IL CODICE '01'
				lFiltro = " AND FAS.COD_STATO_FASCICOLO IN ('02','05','10','14','18','20','21')";
		}
		
		siesLogger.info("fine");
		return lFiltro;
	}
	

	/**
	 * Ritorna la condizione sql per filtro su tipo procedimento.
	 * <p>
	 *
	 * @param aFiltroTipoProc
	 * @return
	 */
	public String getFiltroTipoProc(String aFiltroTipoProc) {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");
		String lSql = new String();
		// 20170803: [SG] aggiunti codici trovati nel DB --> A, F, M, N, R, S
		// public static final String UDIENZA_ANNULLATA = "A";
		// public static final String UDIENZA_FISSATA = "F";
		// public static final String UDIENZA_MODIFICATA = "M";
		// public static final String NUOVO_RUOLO = "N";
		// public static final String UDIENZA_PREFISSATA = "P";
		// public static final String UDIENZA_RINVIATA = "R";
		// public static final String UDIENZA_SEGUITO_RINVIO = "S";
		if (aFiltroTipoProc != null) {
			if (aFiltroTipoProc.compareTo("FISSATI") == 0) {
				lSql += " AND UP.FLAG_RINVIATA IN ('F','R','S') ";
			} else if (aFiltroTipoProc.compareTo("PREFISSATI") == 0) {
				lSql += " AND UP.FLAG_RINVIATA IN ('P') ";
			} else if (aFiltroTipoProc.compareTo("TUTTI") == 0) {
				lSql += " AND UP.FLAG_RINVIATA IN ('F','M','N','P','R','S') ";
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
		return lSql;
	}

	
	
	/**
	 * Ritorna la condizione sql per filtro su tipo procedimento.
	 * <p>
	 * 
	 * @param aFiltroTipoProc
	 * @return
	 */
	public String getFiltroTipoProcOrd(String aFiltroTipoProc) {
	
		siesLogger.info("inizio");
		String lSql = new String();
		
		if (aFiltroTipoProc != null) {
			if (aFiltroTipoProc.compareTo("FISSATI") == 0) {
				lSql += " AND ( UP.FLAG_RINVIATA IN ('F','R','S') OR UP.FLAG_RINVIATA IS NULL) ";
			} else if (aFiltroTipoProc.compareTo("PREFISSATI") == 0) {
				lSql += " AND ( UP.FLAG_RINVIATA IN ('P') OR UP.FLAG_RINVIATA IS NULL) ";
			} else if (aFiltroTipoProc.compareTo("TUTTI") == 0) {
				lSql += " AND (UP.FLAG_RINVIATA IN ('F','M','N','P','R','S') OR UP.FLAG_RINVIATA IS NULL) ";
			}
		}

		siesLogger.info("fine");
		return lSql;
	}

	/**
	 * Ritorna condizione di ordinamento dei dati.
	 * <p>
	 *
	 * @param aOrderBy
	 * @return
	 */
	public String getOrderBy(String aOrderBy) {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");
		String lSqlOrder = new String();
		if (aOrderBy != null) {
			lSqlOrder += " ORDER BY ";
			lSqlOrder += aOrderBy.startsWith("M") ? "COGNOME_MAGISTRATO,NOME_MAGISTRATO," : "";
			if (aOrderBy.equals("PP") || aOrderBy.equals("MPP")) // Per Progressivo Procedimento
				lSqlOrder += " CHIAVE_ANNO, CHIAVE_PROGR ";
			else if (aOrderBy.equals("CNS") || aOrderBy.equals("MCNS")) // Per Cognome/Nome Soggetto
				lSqlOrder += " COGNOME, NOME ";
			else if (aOrderBy.equals("PGCNS") || aOrderBy.equals("MPGCNS")) // Per Posizione Giuridica e
																			// Cognome/Nome
																			// Soggetto
				lSqlOrder += " DESC_POSIZIONE_GIURIDICA, COGNOME, NOME";
			else if (aOrderBy.equals("PGPP") || aOrderBy.equals("MPGPP")) // Per Posizione Giuridica e
																			// Progressivo
																			// Procedimento
				lSqlOrder += " DESC_POSIZIONE_GIURIDICA, CHIAVE_ANNO, CHIAVE_PROGR ";
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
		return lSqlOrder;
	}

	//
	// METODO GETMODEL()
	//
	/**
	 * Metodo che recupera i dati dal resulset e ne popola il model.
	 * <p>
	 *
	 * @return istanza model ProcedimentixUdienzaModel
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public GenericModel getModel() throws DAOException {
		ProcedimentixUdienzaModel aModel = new ProcedimentixUdienzaModel();

		aModel.setIdFasSIGE(getBigDecimal("ID_FASCICOLO_SIGE"));
		aModel.setChiaveAnnoFasSIGE(getBigDecimal("CHIAVE_ANNO"));
		aModel.setChiaveProgrFasSIGE(getBigDecimal("CHIAVE_PROGR"));
		aModel.setCodStatoFasSIGE(getString("COD_STATO_FASCICOLO"));
		aModel.setDescrStatoFasSIGE(getString("DESC_STATO_FASCICOLO"));
		aModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		aModel.setCognomeSog(getString("COGNOME"));
		aModel.setNomeSog(getString("NOME"));
		aModel.setDataNascitaSog(getDate("DATA_NASCITA"));
		aModel.setDescrComuneNascitaSog(getString("DESCR_COMUNE_NASCITA"));
		aModel.setDescPosizioneGiuridica(getString("DESC_POSIZIONE_GIURIDICA"));
		// aModel.setDescrComuneNascitaEsteroSog(getString("DESCR_LUOGO_NASCITA_ESTERO_SOG") );

		// ++ 2011-01-21 ...da performare...
		aModel.setDescrOggettiProcedimento(getDescOggetti(getBigDecimal("ID_PROVVEDIMENTO_SIGE")));
		aModel.setTenori(getTenori(getBigDecimal("ID_PROVVEDIMENTO_SIGE")));
		// ||

		aModel.setIdUdienza(getBigDecimal("ID_UDIENZA_SIGE"));
		aModel.setIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setFlagRinviata(getString("FLAG_RINVIATA"));
		aModel.setSezioneProcedimento(getString("DESC_SEZIONE"));
		aModel.setNumCollegio(getInteger("COD_COLLEGIO"));
		aModel.setEsitoProvvedimento(getString("DESC_ESITO_PROVVEDIMENTO"));

		aModel.setCodMagistrato(getString("MAG_COD_MAGISTRATO_ASS"));
		aModel.setCognomeMagistrato(getString("COGNOME_MAGISTRATO"));
		aModel.setNomeMagistrato(getString("NOME_MAGISTRATO"));

		aModel.setIdAvvocato(getBigDecimal("ID_AVVOCATO"));
		aModel.setCognomeAvvocato(getString("COGNOME_AVV"));
		aModel.setNomeAvvocato(getString("NOME_AVV"));
		aModel.setDescrTipoAvvocato(getString("DESC_TIPO_AVVOCATO"));

		// 2011-01-10
		// Oggetti aggregati d'uso per la generazione xml per stampe
		// Nota : Verificare se normalizzare, anche le view in jsp

		// Fascicolo Sige
		aModel.getFascicoloSige().setIdFascicoloSige(aModel.getIdFasSIGE());
		aModel.getFascicoloSige().setChiaveAnno(aModel.getChiaveAnnoFasSIGE());
		aModel.getFascicoloSige().setChiaveProgr(aModel.getChiaveProgrFasSIGE());
		aModel.getFascicoloSige().setCodStatoFascicolo(aModel.getCodStatoFasSIGE());
		aModel.getFascicoloSige().setDescrStatoFascicolo(aModel.getDescrStatoFasSIGE());
		// Soggetto
		aModel.getSoggetto().setIdSoggetto(aModel.getIdSoggetto());
		aModel.getSoggetto().setCognome(aModel.getCognomeSog());
		aModel.getSoggetto().setNome(aModel.getNomeSog());
		// Magistrato Assegnatario
		aModel.getMagistrato().setCodMagistrato(aModel.getCodMagistrato());
		aModel.getMagistrato().setCognome(aModel.getCognomeMagistrato());
		aModel.getMagistrato().setNome(aModel.getNomeMagistrato());
		// Avvocato
		aModel.getAvvocato().setIdAvvocato(aModel.getIdAvvocato());
		aModel.getAvvocato().setCognome(aModel.getCognomeAvvocato());
		aModel.getAvvocato().setNome(aModel.getNomeAvvocato());
		aModel.getAvvocato().setDescrTipo(aModel.getDescrTipoAvvocato());
		aModel.getAvvocato().setForo(getString("FORO"));
		// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
		// metodo introdotto per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018 (errore
		// prototipo PALERMO)
		aModel.setAvvocati(getAvvocati(getBigDecimal("ID_FASCICOLO_SIGE")));
		//interventi per sies 11.2.1
		try {
			if(mRs.getString("COGNOME_PROCUR")!=null){

				String procur= getString("COGNOME_PROCUR")!=null? getString("COGNOME_PROCUR") + " " + getString("NOME_PROCUR") :"";
				
				aModel.setDescrProcuratore(procur);	
			}
		} catch (SQLException e) {		}		
		 try {
				if(mRs.getString("COGNOME_CANC")!=null){
					String cance= getString("COGNOME_CANC")!=null? getString("COGNOME_CANC") + " " + getString("NOME_CANC") :"";
					
					aModel.setDescrIdAssistente(cance);	
				}
				
		} catch (SQLException e) {		}		
	

		return aModel;
	}

	/**
	 * Metodo che recupera i dati dal resulset e ne popola il model completo dei dati afferenti all'evento.
	 * <p>
	 *
	 * @return istanza model ProcedimentixUdienzaModel
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public GenericModel getModelEvento() throws DAOException {
		ProcedimentixUdienzaModel aModel = new ProcedimentixUdienzaModel();

		aModel.setIdFasSIGE(getBigDecimal("ID_FASCICOLO_SIGE"));
		aModel.setChiaveAnnoFasSIGE(getBigDecimal("CHIAVE_ANNO"));
		aModel.setChiaveProgrFasSIGE(getBigDecimal("CHIAVE_PROGR"));
		aModel.setCodStatoFasSIGE(getString("COD_STATO_FASCICOLO"));
		aModel.setDescrStatoFasSIGE(getString("DESC_STATO_FASCICOLO"));
		aModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		aModel.setCognomeSog(getString("COGNOME"));
		aModel.setNomeSog(getString("NOME"));
		aModel.setDataNascitaSog(getDate("DATA_NASCITA"));
		aModel.setDescrComuneNascitaSog(getString("DESCR_COMUNE_NASCITA"));
		// aModel.setDescrComuneNascitaEsteroSog(getString("DESCR_PROVINC_NASCITA_ESTERO_SOG") );
		aModel.setIdUdienza(getBigDecimal("ID_UDIENZA_SIGE"));
		aModel.setNumCollegio(getInteger("COD_COLLEGIO"));

		// ++ 2011-01-21 ...da performare...
		aModel.setDescrOggettiProcedimento(getDescOggetti(getBigDecimal("ID_PROVVEDIMENTO_SIGE")));
		aModel.setTenori(getTenori(getBigDecimal("ID_PROVVEDIMENTO_SIGE")));
		// ||

		// aModel.setDescrOggettoProcedimento(getString("OGGETTO_PROCEDIMENTO") );
		// aModel.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO") );
		aModel.setSezioneProcedimento(getString("DESC_SEZIONE"));
		aModel.setFlagRinviata(getString("FLAG_RINVIATA"));
		// aModel.setMotivoProvvedimento(getString("DESC_MOTIVO_PROVVEDIMENTO") );
		aModel.setEsitoProvvedimento(getString("DESC_ESITO_PROVVEDIMENTO"));
		// aModel.setDescStatoUdienza(getString("DESC_STATO_UDIENZA") );
		aModel.setDescPosizioneGiuridica(getString("DESC_POSIZIONE_GIURIDICA"));

		aModel.setIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		// aModel.setCodEsito(getString("COD_ESITO") );
		// aModel.setCognomeMagistrato(getString("COGNOMEMAG") );
		// aModel.setNomeMagistrato(getString("NOMEMAG") );
		// aModel.setCodMagistrato(getString("COD_MAGISTRATO") );
		// aModel.setIdEsperto(getBigDecimal("ID_ESPERTO"));

		/*
		 * if ( aModel.getCodMagistrato() == null || aModel.getCodMagistrato().trim().length() < 1) {
		 * aModel.setNomeMagistrato(getString("NOMEESP") );
		 * aModel.setCognomeMagistrato(getString("COGNOMEESP") ); }
		 */

		aModel.setCodMagistrato(getString("MAG_COD_MAGISTRATO_ASS"));
		aModel.setCognomeMagistrato(getString("COGNOME_MAGISTRATO"));
		aModel.setNomeMagistrato(getString("NOME_MAGISTRATO"));

		aModel.setCognomeAvvocato(getString("COGNOME_AVV"));
		aModel.setNomeAvvocato(getString("NOME_AVV"));
		aModel.setDescrTipoAvvocato(getString("DESC_TIPO_AVVOCATO"));

		// 2011-01-10
		// Oggetti aggregati d'uso per la generazione xml per stampe
		// Nota : Verificare se normalizzare, anche le view in jsp...

		// Fascicolo Sige
		aModel.getFascicoloSige().setIdFascicoloSige(aModel.getIdFasSIGE());
		aModel.getFascicoloSige().setChiaveAnno(aModel.getChiaveAnnoFasSIGE());
		aModel.getFascicoloSige().setChiaveProgr(aModel.getChiaveProgrFasSIGE());
		aModel.getFascicoloSige().setCodStatoFascicolo(aModel.getCodStatoFasSIGE());
		aModel.getFascicoloSige().setDescrStatoFascicolo(aModel.getDescrStatoFasSIGE());
		// Soggetto
		aModel.getSoggetto().setIdSoggetto(aModel.getIdSoggetto());
		aModel.getSoggetto().setCognome(aModel.getCognomeSog());
		aModel.getSoggetto().setNome(aModel.getNomeSog());
		aModel.getSoggetto().setDataNascita(aModel.getDataNascitaSog());
		aModel.getSoggetto().setDescrComuneNascita(aModel.getDescrComuneNascitaSog());
		aModel.getSoggetto().setDescComuneNascitaEstero(aModel.getDescrComuneNascitaEsteroSog());
		// Magistrato Assegnatario
		aModel.getMagistrato().setCodMagistrato(aModel.getCodMagistrato());
		aModel.getMagistrato().setCognome(aModel.getCognomeMagistrato());
		aModel.getMagistrato().setNome(aModel.getNomeMagistrato());
		// Avvocato
		aModel.getAvvocato().setIdAvvocato(aModel.getIdAvvocato());
		aModel.getAvvocato().setCognome(aModel.getCognomeAvvocato());
		aModel.getAvvocato().setNome(aModel.getNomeAvvocato());
		aModel.getAvvocato().setDescrTipo(aModel.getDescrTipoAvvocato());
		aModel.getAvvocato().setForo(getString("FORO"));
		// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
		// metodo introdotto per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018 (errore
		// prototipo PALERMO)
		aModel.setAvvocati(getAvvocati(getBigDecimal("ID_FASCICOLO_SIGE")));

		return aModel;
	}

	/**
	 * Metodo per recuperare gli avvocati di un fascicoloo sige metodo introdotto per problematica inviata
	 * tramite email Maffucci/Alfieri del 02/01/2018 (errore prototipo PALERMO)
	 *
	 *
	 * @param idFascSige
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	private AvvocatoModel[] getAvvocati(BigDecimal idFascSige) throws DAOException {

		siesLogger.info("inizio");
		AvvocatoModel[] lAvvocati = new AvvocatoModel[0];
		AvvocatoFascicoloSigeModel aAvvFascMod = new AvvocatoFascicoloSigeModel();
		aAvvFascMod.setFasSigeIdFascicoloSige(idFascSige);
		AvvocatoSqlDAO lAvvDao = new AvvocatoSqlDAO(super.mCon);
		lAvvDao.ricercaAvvocatoByIDFascicoloSige(idFascSige);

		ArrayList<GenericModel> la = new ArrayList<GenericModel>(lAvvDao.getModels());
		lAvvocati = la.toArray(new AvvocatoModel[0]);
		siesLogger.info("fine");
		return lAvvocati;
	}

	/**
	 * Recupera la descrizione degli oggetti afferenti al provvedimento.
	 * <p>
	 *
	 * @param aIdProvvedimento
	 * @return
	 * @throws DAOException
	 */
	public String[] getDescOggetti(BigDecimal aIdProvvedimento) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");
		String[] lDescOggetti = new String[0];

		TenoreSigeSqlDAO lTenSqlDAO = new TenoreSigeSqlDAO(super.mCon);
		lTenSqlDAO.ricercaTenoriByProvvedimento(aIdProvvedimento);
		lTenSqlDAO.start();

		ArrayList<String> la = new ArrayList<>();
		while (lTenSqlDAO.next())
			la.add(lTenSqlDAO.getString("RV_MEANING"));

		lTenSqlDAO.stop();
		lDescOggetti = la.toArray(new String[0]);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine : " + Utils.arrayToString(lDescOggetti, ";"));
		return lDescOggetti;
	}

	/**
	 *
	 * <p>
	 *
	 * @param aIdProvvedimento
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public TenoreSigeModel[] getTenori(BigDecimal aIdProvvedimento) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");
		TenoreSigeModel[] lTenori = new TenoreSigeModel[0];

		TenoreSigeSqlDAO lTenSqlDAO = new TenoreSigeSqlDAO(super.mCon);
		lTenSqlDAO.ricercaTenoriByProvvedimento(aIdProvvedimento);
		// lTenSqlDAO.start();

		// Da modificare.... dele prelevare models
		ArrayList<GenericModel> la = new ArrayList<GenericModel>(lTenSqlDAO.getModels());
		/*
		 * while( lTenSqlDAO.next() ) la.add((TenoreSigeModel)lTenSqlDAO.getModel());
		 *
		 * lTenSqlDAO.stop();
		 */
		lTenori = la.toArray(new TenoreSigeModel[0]);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine" /* + Utils.arrayToString(lDescOggetti,";") */);
		return lTenori;
	}

	/*
	 * public String setCondizione(ProcedimentixUdienzaModel aModel) { String lCondizioni = new String();
	 *
	 * boolean lInserito = false; return lCondizioni; }
	 */

	/**
	 * La funzione ritorna il Numero di Procedimenti assegnati ad una udienza.
	 * <p>
	 *
	 * @param aIdUdienza
	 *            id dell'udienza.
	 * @return BigDecimal Numero Procedimenti.
	 * @throws DAOException
	 *             propga errore di eccezione.
	 */
	/*
	 * public BigDecimal getNumProcedimentiXUdienza(BigDecimal aIdUdienza) throws DAOException { BigDecimal
	 * lCount = new BigDecimal(0);
	 *
	 * // Preparazione dello statement // 12/04/2007 Aggiunto controllo sui procedimenti unificati. //String
	 * lStatement ="SELECT COUNT(*) NUM FROM UDIENZA_PROCEDIMENTO_SIGE"; //lStatement +=
	 * " WHERE UDI_ID_UDIENZA = " + aIdUdienza; //lStatement +=
	 * " AND  UDIENZA_PROCEDIMENTO_SIGE.flag_rinviata not in ('M','A')"; String lStatement
	 * ="SELECT COUNT(*) NUM FROM UDIENZA_PROCEDIMENTO_SIGE, FASCICOLO_SIGE, generale_procedimento";
	 * lStatement += " WHERE UDIENZA_PROCEDIMENTO_SIGE.UDI_ID_UDIENZA = " + aIdUdienza; lStatement +=
	 * " and FASCICOLO_SIGE.id_FASCICOLO_SIGE = generale_procedimento.FAS_SIG_ID_FASCICOLO_SIGE "; lStatement
	 * +=
	 * " and generale_procedimento.id_generale_procedimento = UDIENZA_PROCEDIMENTO_SIGE.gen_prid_generale_procedimento "
	 * ; lStatement += " and FASCICOLO_SIGE.cod_stato_fascicolo <> '05' "; lStatement +=
	 * " AND  UDIENZA_PROCEDIMENTO_SIGE.flag_rinviata not in ('M','A')"; setStatement(lStatement);
	 *
	 * // Attivazione della query e lettura del risultato start(); next(); lCount = getBigDecimal("NUM");
	 * stop();
	 *
	 * return lCount; }
	 */
	/**
	 * La funzione ritorna il Numero di Procedimenti assegnati ad una udienza da "rinvio".
	 * <p>
	 *
	 * @param aIdUdienza
	 *            id dell'udienza.
	 * @return BigDecimal Numero Procedimenti.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	/*
	 * public BigDecimal getNumProcDaRinvioXUdienza(BigDecimal aIdUdienza) throws DAOException { BigDecimal
	 * lCount = new BigDecimal(0);
	 *
	 * // Preparazione dello statement // 12/04/2007 Aggiunto controllo sui procedimenti unificati. // String
	 * lStatement ="SELECT COUNT(*) NUM FROM UDIENZA_PROCEDIMENTO_SIGE"; // lStatement +=
	 * " WHERE UDI_ID_UDIENZA = " + aIdUdienza; // lStatement +=
	 * " AND  UDIENZA_PROCEDIMENTO_SIGE.flag_rinviata = 'S' "; String lStatement
	 * ="SELECT COUNT(*) NUM FROM UDIENZA_PROCEDIMENTO_SIGE, FASCICOLO_SIGE, generale_procedimento";
	 * lStatement += " WHERE UDIENZA_PROCEDIMENTO_SIGE.UDI_ID_UDIENZA = " + aIdUdienza; lStatement +=
	 * " and FASCICOLO_SIGE.id_FASCICOLO_SIGE = generale_procedimento.FAS_SIG_ID_FASCICOLO_SIGE "; lStatement
	 * +=
	 * " and generale_procedimento.id_generale_procedimento = UDIENZA_PROCEDIMENTO_SIGE.gen_prid_generale_procedimento "
	 * ; lStatement += " and FASCICOLO_SIGE.cod_stato_fascicolo <> '05' "; lStatement +=
	 * " AND  UDIENZA_PROCEDIMENTO_SIGE.flag_rinviata = 'S' "; setStatement(lStatement);
	 *
	 * // Attivazione della query e lettura del risultato start(); next(); lCount = getBigDecimal("NUM");
	 * stop();
	 *
	 * return lCount; }
	 */

	/**
	 * Slq Query di base che estrapola i procedimenti distinti senza i dati dati dell'avvocato metodo
	 * introdotto per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018 (errore prototipo
	 * PALERMO)
	 * <p>
	 *
	 * @param aValue
	 * @return
	 * @throws DAOException
	 */
	protected String getSqlQuery_NO_Avv(Object aValue) throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT DISTINCT " + " UP.ID_UDIENZA_PROCEDIMENTO_SIGE, " + " UP.FLAG_RINVIATA, "
		// NUOVA INFRASTRUTTURA: modificata la query (erased UP.)
				+ " DESC_FLAG_UDIENZA.RV_MEANING AS DESC_FLAG_UDIENZA, " + " UP.COD_OPERATORE_INSERIMENTO,"
				+ " UP.DATA_INSERIMENTO, " + " UP.COD_UFFICIO_INSERIMENTO, "
				+ " UP.COD_OPERATORE_AGGIORNAMENTO, " + " UP.DATA_AGGIORNAMENTO, "
				+ " UP.COD_UFFICIO_AGGIORNAMENTO, " + " UP.FAS_ID_FASCICOLO_SIGE, "
				+ " UP.UDI_ID_UDIENZA_SIGE, " + " UP.UDI_ID_UDIENZA_RINVIO, " + " UP.EVE_ID_EVENTO, "
				+ " PROVVEDIMENTO_SIGE.ID_PROVVEDIMENTO_SIGE, " + " PROVVEDIMENTO_SIGE.ID_EVENTO_GENERATO, "
				+ " PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE, " + " UDIENZA_SIGE.ID_UDIENZA_SIGE, "
				+ " UDIENZA_SIGE.DATA_UDIENZA, " + " COLLEGIO.COD_COLLEGIO, "
				+ " MAS.MAG_COD_MAGISTRATO AS MAG_COD_MAGISTRATO_ASS, " + " MAG.NOME AS NOME_MAGISTRATO, "
				+ " MAG.COGNOME AS COGNOME_MAGISTRATO, "

				+ " 0 AS ID_AVVOCATO," + "  '' AS COGNOME_AVV, " + "  '' AS NOME_AVV, " + "  '' AS FORO ,"
				// + " AVV.ID_AVVOCATO, "
				// + " AVV.COGNOME AS COGNOME_AVV, "
				// + " AVV.NOME AS NOME_AVV, "
				// + " AVV.FORO AS FORO, "
				+ " DESC_TIPO_AVV.RV_MEANING AS DESC_TIPO_AVVOCATO,"
				+ " SEZIONE.DESCRIZIONE AS DESC_SEZIONE, " + " FASCICOLO_SIGE.CHIAVE_ANNO, "
				+ " FASCICOLO_SIGE.CHIAVE_PROGR, " + " FASCICOLO_SIGE.COD_POSIZIONE_GIURIDICA, "
				+ " FASCICOLO_SIGE.ID_FASCICOLO_SIGE, " + " FASCICOLO_SIGE.COD_STATO_FASCICOLO, "
				+ " DESC_STATO_FASCICOLO.RV_MEANING AS DESC_STATO_FASCICOLO, "
				+ " DESC_POSIZIONE_GIURIDICA.RV_MEANING AS DESC_POSIZIONE_GIURIDICA, "
				+ " DESC_ESITO_PROVVEDIMENTO.RV_MEANING AS DESC_ESITO_PROVVEDIMENTO, "
				+ " DESC_TIPO_PROVVEDIMENTO_SIGE.RV_MEANING AS DESC_TIPO_PROVVEDIMENTO_SIGE, " +
				// DESC_OGGETTO_SIGE.RV_MEANING AS DESC_OGGETTO_SIGE,
				" SOGGETTO.ID_SOGGETTO, " + " SOGGETTO.COGNOME AS COGNOME, " + " SOGGETTO.NOME NOME, "
				+ " SOGGETTO.DATA_NASCITA AS DATA_NASCITA, " + " COMUNE.DESCRIZIONE AS DESCR_COMUNE_NASCITA, "
				+ " PROV.RV_MEANING AS DESCR_PROVINCIA_NASCITA, " + " NAZ.RV_MEANING AS DESCR_STATO_NASCITA, "
				
				//inizio modifica per sies 11.2.1
				+ " PROC.COGNOME                                   AS COGNOME_PROCUR,     "
				+ " PROC.NOME                                      AS NOME_PROCUR,"
				+ " CAN.COGNOME                                    AS COGNOME_CANC,"
				+ " CAN.NOME                                       AS NOME_CANC"				
				// fine modifica

				+ " FROM UDIENZA_PROCEDIMENTO_SIGE UP "
				+ " INNER JOIN UDIENZA_SIGE ON ID_UDIENZA_SIGE = UDI_ID_UDIENZA_SIGE "
				+ " LEFT OUTER JOIN COLLEGIO ON UDIENZA_SIGE.COL_ID_COLLEGIO = COLLEGIO.ID_COLLEGIO "
				+ " LEFT OUTER JOIN SEZIONE ON COLLEGIO.SEZ_ID_SEZIONE = SEZIONE.ID_SEZIONE "
				+ " LEFT OUTER JOIN MAGISTRATO_ASSEGNATARIO MAS ON "
				+ " MAS.FAS_SIGE_ID_FASCICOLO_SIGE = FAS_ID_FASCICOLO_SIGE AND MAS.DATA_FINE IS NULL "
				+ " LEFT OUTER JOIN MAGISTRATO MAG ON MAG.COD_MAGISTRATO = MAS.MAG_COD_MAGISTRATO  "
				+ " INNER JOIN FASCICOLO_SIGE ON ID_FASCICOLO_SIGE = FAS_ID_FASCICOLO_SIGE "
				+ " INNER JOIN CG_REF_CODES DESC_STATO_FASCICOLO ON "
				+ " DESC_STATO_FASCICOLO.RV_LOW_VALUE = FASCICOLO_SIGE.COD_STATO_FASCICOLO "
				+ " AND DESC_STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO' "
				+ " INNER JOIN SOGGETTO ON ID_SOGGETTO = FASCICOLO_SIGE.SOG_ID_SOGGETTO "
				// 20170914: [SG] left outer su avvocato
				+ " LEFT OUTER JOIN AVVOCATO_FASCICOLO_SIGE AVV_FAS_SIGE ON "
				+ " AVV_FAS_SIGE.FAS_SIGE_ID_FASCICOLO_SIGE = FASCICOLO_SIGE.ID_FASCICOLO_SIGE "
				+ " AND AVV_FAS_SIGE.DATA_FINE_VALIDITA IS NULL "
				+ " LEFT OUTER JOIN AVVOCATO AVV ON AVV.ID_AVVOCATO = AVV_FAS_SIGE.AVV_ID_AVVOCATO "
				+ " LEFT OUTER JOIN CG_REF_CODES DESC_TIPO_AVV ON "
				+ " AVV_FAS_SIGE.COD_TIPO_AVVOCATO = DESC_TIPO_AVV.RV_LOW_VALUE "
				+ " AND DESC_TIPO_AVV.RV_DOMAIN ='TIPO_AVVOCATO'"
				+ " INNER JOIN COMUNE ON SOGGETTO.COD_COMUNE_NASCITA = COMUNE.COD_COMUNE "
				+ " INNER JOIN CG_REF_CODES PROV ON PROV.RV_LOW_VALUE = SOGGETTO.COD_PROVINCIA_NASCITA "
				+ " AND PROV.RV_DOMAIN = 'PROVINCIA' " +

				" INNER JOIN CG_REF_CODES NAZ ON NAZ.RV_LOW_VALUE = SOGGETTO.COD_STATO_NASCITA "
				+ " AND NAZ.RV_DOMAIN = 'NAZIONE' "
				+ " INNER JOIN CG_REF_CODES DESC_POSIZIONE_GIURIDICA ON DESC_POSIZIONE_GIURIDICA.RV_LOW_VALUE = FASCICOLO_SIGE.COD_POSIZIONE_GIURIDICA  "
				+ " AND DESC_POSIZIONE_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA' " +

				// 2011-01-31
				" INNER JOIN TENORE_SIGE TEN ON TEN.FAS_ID_FASCICOLO_SIGE = ID_FASCICOLO_SIGE "
				+ " AND TEN.DATA_FINE IS NULL "
				+ " INNER JOIN PROVVEDIMENTO_SIGE ON ID_PROVVEDIMENTO_SIGE = TEN.PROV_ID_PROVVEDIMENTO_SIGE "
				+ " AND ( PROVVEDIMENTO_SIGE.DEFINITORIO IN ('S','N') OR PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE IN ('01','04','50')  )"
				+

				" INNER JOIN EVENTO ON ID_EVENTO = PROVVEDIMENTO_SIGE.ID_EVENTO_GENERATO "
				+ " AND ( EVENTO.FLAG_DOCUMENTO_REGISTRATO <>'A' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL ) "
				+

				" INNER JOIN CG_REF_CODES DESC_ESITO_PROVVEDIMENTO ON EVENTO.COD_ESITO = DESC_ESITO_PROVVEDIMENTO.RV_LOW_VALUE "
				+ " AND DESC_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO' "
				+ " LEFT OUTER JOIN CG_REF_CODES DESC_FLAG_UDIENZA ON UP.FLAG_RINVIATA = DESC_FLAG_UDIENZA.RV_LOW_VALUE "
				+ " AND DESC_FLAG_UDIENZA.RV_DOMAIN ='STATO_FLAG_RINVIATA' "
				+ " INNER JOIN CG_REF_CODES DESC_TIPO_PROVVEDIMENTO_SIGE ON PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE = DESC_TIPO_PROVVEDIMENTO_SIGE.RV_LOW_VALUE "
				+ " AND DESC_TIPO_PROVVEDIMENTO_SIGE.RV_DOMAIN ='TIPO_PROVVEDIMENTO_SIGE' "

				 // inizio modifica per sies 11.2.1
				+ " LEFT OUTER JOIN MAGISTRATO PROC ON PROC.COD_MAGISTRATO = MAS.COD_PROCURATORE"
				+ " LEFT OUTER JOIN ASSISTENTE_GIUDIZIARIO CAN  ON CAN.ID_ASSISTENTE_GIUDIZIARIO = MAS.COD_ID_ASSISTENTE "  ;
			   // fine modifica per sies 11.2.1

		// Aggiunge la where condition su id_udienza o data_udienza
		if (aValue instanceof java.math.BigDecimal)
			lStatement += getCondizioneByKey((BigDecimal) aValue);
		else if (aValue instanceof java.util.Date)
			lStatement += getCondizioneByDate((Date) aValue);
		else
			throw new DAOException(
					"Il valore passato non è del tipo : BigDecimal ( IDUdienza ) o Date ( DataUdienza ).");

		return lStatement;
	}

	/**
	 * Ritorna la condizione sql per filtro su FlagModifBlocco
	 * <p>
	 * 
	 * @param aFiltroTipoProc
	 * @return
	 */
	public String getFiltroFlagModifBlocco(String FlagModifBlocco) {
		
		siesLogger.info("inizio");
		String lSql = new String();

         if("B".equals(FlagModifBlocco)){
        	 lSql+=" AND ( MAS.Flag_Modif_In_Blocco='B' or MAS.Flag_Modif_In_Blocco is null ) ";
         }
         else
        	 lSql+=" AND ( MAS.Flag_Modif_In_Blocco='B' or MAS.Flag_Modif_In_Blocco is null OR MAS.Flag_Modif_In_Blocco='P' ) ";
		
		siesLogger.info("fine");
		return lSql;
	}
	
	/**
	 * Ritorna la condizione sql per filtro su FlagModifBlocco
	 * <p>
	 * 
	 * @param aFiltroTipoProc
	 * @return
	 */
	public String getFiltroCodMagAss(String codMagAss) {
		
		siesLogger.info("inizio");
		String lSql = new String();
		
		if (codMagAss != null && !"null".equals(codMagAss)) {			
				lSql += " AND MAS.MAG_COD_MAGISTRATO ='"+codMagAss + "'";			
		}		
		siesLogger.info("fine");
		return lSql;
	}
	
	
	public GenericModel getExtendModel() throws DAOException {
		ProcedimentixUdienzaModel aModel = new ProcedimentixUdienzaModel();

		aModel= (ProcedimentixUdienzaModel) this.getModel();
		  //interventi per sies 11.2.1
		String procur= getString("COGNOME_PROCUR")!=null? getString("COGNOME_PROCUR") + " " + getString("NOME_PROCUR") :"";
		String cance= getString("COGNOME_CANC")!=null? getString("COGNOME_CANC") + " " + getString("NOME_CANC") :"";
		aModel.setDescrProcuratore(procur);		
		aModel.setDescrIdAssistente(cance);
		return aModel;
	}
	
	
	/**
	 * Metodo di ricerca procedimenti per udienza.
	 * <p>
	 * 
	 * @param aIdUdienza
	 *            id dell'udienza.
	 * @param aOrderBy
	 *            tipo di ordinamnto.
	 * @param aStatoProcedimento
	 *            tipo stato del procedimento.
	 * @param aTipoProc
	 *            tipo procedimento.
	 * @throws DAOException
	 *             propga errore di eccezione.
	 */
	public void ricercaProcedimentiRuoloByUdienzaOrdinanza(BigDecimal aIdUdienza, String aOrderBy,
			String aStatoProcedimento, String aTipoProc, String flagModifBlocco, String codMagi) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaProcedimentiRuoloByUdienza: OrderBy -> " + aOrderBy);

		String lSql = getSqlQuery(aIdUdienza);
		lSql += getFiltroTipoProc(aTipoProc);
		lSql += getFiltroStatoProc(aStatoProcedimento);
		lSql += getFiltroFlagModifBlocco(flagModifBlocco);
		lSql += getFiltroCodMagAss(codMagi);
		
		lSql += getOrderBy(aOrderBy);
		
		// aggiungo la UNION PER LA CASISTICA DEI PROCEDIMENTI SENZI DECRETI DI FISSAZIONE UDIENZA
		
		lSql += " UNION ";
		
		lSql +=  getSqlQueryProvvSIGE(aIdUdienza);
		
		lSql += getFiltroStatoProc(aStatoProcedimento);
		lSql += getFiltroFlagModifBlocco(flagModifBlocco);
		lSql += getFiltroCodMagAss(codMagi);
		
		lSql += getOrderBy(aOrderBy);

		setStatement(lSql);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
	}
    
	
	
	protected String getSqlQueryProvvSIGE(Object aValue) throws DAOException {
		String lStatement = new String("");


		lStatement += "SELECT DISTINCT " 
				+ " NULL, " 
				+ " NULL, "		
				+ " NULL AS DESC_FLAG_UDIENZA, " 
				
				+ " UP.COD_OPERATORE_INSERIMENTO,"
				+ " UP.DATA_INSERIMENTO, " + " UP.COD_UFFICIO_INSERIMENTO, "
				+ " UP.COD_OPERATORE_AGGIORNAMENTO, " + " UP.DATA_AGGIORNAMENTO, "
				+ " UP.COD_UFFICIO_AGGIORNAMENTO, " + " UP.FAS_ID_FASCICOLO_SIGE, "
				+ " UP.UDI_ID_UDIENZA_SIGE, " + 	
				
				" NULL, " +
				" UP.ID_EVENTO_GENERATO, "		
				
				+ " PROVVEDIMENTO_SIGE.ID_PROVVEDIMENTO_SIGE, " + " PROVVEDIMENTO_SIGE.ID_EVENTO_GENERATO, "
				+ " PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE, " + " UDIENZA_SIGE.ID_UDIENZA_SIGE, "
				+ " UDIENZA_SIGE.DATA_UDIENZA, " + " COLLEGIO.COD_COLLEGIO, "
				+ " MAS.MAG_COD_MAGISTRATO AS MAG_COD_MAGISTRATO_ASS, " + " MAG.NOME AS NOME_MAGISTRATO, "
				+ " MAG.COGNOME AS COGNOME_MAGISTRATO, " + " AVV.ID_AVVOCATO, "
				+ " AVV.COGNOME AS COGNOME_AVV, " + " AVV.NOME AS NOME_AVV, " + " AVV.FORO AS FORO, "
				+ " DESC_TIPO_AVV.RV_MEANING AS DESC_TIPO_AVVOCATO,"
				+ " SEZIONE.DESCRIZIONE AS DESC_SEZIONE, " + " FASCICOLO_SIGE.CHIAVE_ANNO, "
				+ " FASCICOLO_SIGE.CHIAVE_PROGR, " + " FASCICOLO_SIGE.COD_POSIZIONE_GIURIDICA, "
				+ " FASCICOLO_SIGE.ID_FASCICOLO_SIGE, " + " FASCICOLO_SIGE.COD_STATO_FASCICOLO, "
				+ " DESC_STATO_FASCICOLO.RV_MEANING AS DESC_STATO_FASCICOLO, "
				+ " DESC_POSIZIONE_GIURIDICA.RV_MEANING AS DESC_POSIZIONE_GIURIDICA, "
				+ " DESC_ESITO_PROVVEDIMENTO.RV_MEANING AS DESC_ESITO_PROVVEDIMENTO, "
				+ " DESC_TIPO_PROVVEDIMENTO_SIGE.RV_MEANING AS DESC_TIPO_PROVVEDIMENTO_SIGE, " +				
				" SOGGETTO.ID_SOGGETTO, " + " SOGGETTO.COGNOME AS COGNOME, " + " SOGGETTO.NOME NOME, "
				+ " SOGGETTO.DATA_NASCITA AS DATA_NASCITA, " + " COMUNE.DESCRIZIONE AS DESCR_COMUNE_NASCITA, "
				+ " PROV.RV_MEANING AS DESCR_PROVINCIA_NASCITA, " + " NAZ.RV_MEANING AS DESCR_STATO_NASCITA, "				
				+ " PROC.COGNOME                                   AS COGNOME_PROCUR,     "
				+ " PROC.NOME                                      AS NOME_PROCUR,"
				+ " CAN.COGNOME                                    AS COGNOME_CANC,"
				+ " CAN.NOME                                       AS NOME_CANC"		
				
				+ " FROM PROVVEDIMENTO_SIGE UP "
				+ " INNER JOIN UDIENZA_SIGE ON ID_UDIENZA_SIGE = UDI_ID_UDIENZA_SIGE "
				+ " LEFT OUTER JOIN COLLEGIO ON UDIENZA_SIGE.COL_ID_COLLEGIO = COLLEGIO.ID_COLLEGIO "
				+ " LEFT OUTER JOIN SEZIONE ON COLLEGIO.SEZ_ID_SEZIONE = SEZIONE.ID_SEZIONE "
				+ " LEFT OUTER JOIN MAGISTRATO_ASSEGNATARIO MAS ON "
				+ " MAS.FAS_SIGE_ID_FASCICOLO_SIGE = FAS_ID_FASCICOLO_SIGE AND MAS.DATA_FINE IS NULL "
				+ " LEFT OUTER JOIN MAGISTRATO MAG ON MAG.COD_MAGISTRATO = MAS.MAG_COD_MAGISTRATO  "
				+ " INNER JOIN FASCICOLO_SIGE ON ID_FASCICOLO_SIGE = FAS_ID_FASCICOLO_SIGE "
				+ " INNER JOIN CG_REF_CODES DESC_STATO_FASCICOLO ON "
				+ " DESC_STATO_FASCICOLO.RV_LOW_VALUE = FASCICOLO_SIGE.COD_STATO_FASCICOLO "
				+ " AND DESC_STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO' "
				+ " INNER JOIN SOGGETTO ON ID_SOGGETTO = FASCICOLO_SIGE.SOG_ID_SOGGETTO "
				+ " LEFT OUTER JOIN AVVOCATO_FASCICOLO_SIGE AVV_FAS_SIGE ON "
				+ " AVV_FAS_SIGE.FAS_SIGE_ID_FASCICOLO_SIGE = FASCICOLO_SIGE.ID_FASCICOLO_SIGE "
				+ " AND AVV_FAS_SIGE.DATA_FINE_VALIDITA IS NULL "
				+ " LEFT OUTER JOIN AVVOCATO AVV ON AVV.ID_AVVOCATO = AVV_FAS_SIGE.AVV_ID_AVVOCATO "
				+ " LEFT OUTER JOIN CG_REF_CODES DESC_TIPO_AVV ON "
				+ " AVV_FAS_SIGE.COD_TIPO_AVVOCATO = DESC_TIPO_AVV.RV_LOW_VALUE "
				+ " AND DESC_TIPO_AVV.RV_DOMAIN ='TIPO_AVVOCATO'"
				+ " INNER JOIN COMUNE ON SOGGETTO.COD_COMUNE_NASCITA = COMUNE.COD_COMUNE "
				+ " INNER JOIN CG_REF_CODES PROV ON PROV.RV_LOW_VALUE = SOGGETTO.COD_PROVINCIA_NASCITA "
				+ " AND PROV.RV_DOMAIN = 'PROVINCIA' " +

				" INNER JOIN CG_REF_CODES NAZ ON NAZ.RV_LOW_VALUE = SOGGETTO.COD_STATO_NASCITA "
				+ " AND NAZ.RV_DOMAIN = 'NAZIONE' "
				+ " INNER JOIN CG_REF_CODES DESC_POSIZIONE_GIURIDICA ON DESC_POSIZIONE_GIURIDICA.RV_LOW_VALUE = FASCICOLO_SIGE.COD_POSIZIONE_GIURIDICA  "
				+ " AND DESC_POSIZIONE_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA' " +

				
				" INNER JOIN TENORE_SIGE TEN ON TEN.FAS_ID_FASCICOLO_SIGE = ID_FASCICOLO_SIGE "
				+ " AND TEN.DATA_FINE IS NULL "
				+ " INNER JOIN PROVVEDIMENTO_SIGE ON PROVVEDIMENTO_SIGE.ID_PROVVEDIMENTO_SIGE = TEN.PROV_ID_PROVVEDIMENTO_SIGE "
				+ " AND ( PROVVEDIMENTO_SIGE.DEFINITORIO IN ('S','N') OR PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE IN ('01','04','50')  )"
				+
				
				" INNER JOIN EVENTO ON ID_EVENTO = PROVVEDIMENTO_SIGE.ID_EVENTO_GENERATO "
				+ " AND ( EVENTO.FLAG_DOCUMENTO_REGISTRATO <>'A' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL ) "
				+

				" INNER JOIN CG_REF_CODES DESC_ESITO_PROVVEDIMENTO ON EVENTO.COD_ESITO = DESC_ESITO_PROVVEDIMENTO.RV_LOW_VALUE "
				+ " AND DESC_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO' "
				
				//+ " LEFT OUTER JOIN CG_REF_CODES DESC_FLAG_UDIENZA ON UP.FLAG_RINVIATA = DESC_FLAG_UDIENZA.RV_LOW_VALUE "
				//+ " AND DESC_FLAG_UDIENZA.RV_DOMAIN ='STATO_FLAG_RINVIATA' "
				
				+ " INNER JOIN CG_REF_CODES DESC_TIPO_PROVVEDIMENTO_SIGE ON PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE = DESC_TIPO_PROVVEDIMENTO_SIGE.RV_LOW_VALUE "
				+ " AND DESC_TIPO_PROVVEDIMENTO_SIGE.RV_DOMAIN ='TIPO_PROVVEDIMENTO_SIGE' "	

				+ " LEFT OUTER JOIN MAGISTRATO PROC ON PROC.COD_MAGISTRATO = MAS.COD_PROCURATORE"
				+ " LEFT OUTER JOIN ASSISTENTE_GIUDIZIARIO CAN  ON CAN.ID_ASSISTENTE_GIUDIZIARIO = MAS.COD_ID_ASSISTENTE "  ;
			  

		// Aggiunge la where condition su id_udienza o data_udienza
		if (aValue instanceof java.math.BigDecimal)
			lStatement += getCondizioneByKey((BigDecimal) aValue);
		else if (aValue instanceof java.util.Date)
			lStatement += getCondizioneByDate((Date) aValue);
		else
			throw new DAOException(
					"Il valore passato non è del tipo : BigDecimal ( IDUdienza ) o Date ( DataUdienza ).");

		return lStatement;
	}
	
	
	/**
	 * Metodo di ricerca procedimenti per udienza.
	 * <p>
	 * 
	 * @param aIdUdienza
	 *            id dell'udienza.
	 * @param aOrderBy
	 *            tipo di ordinamnto.
	 * @param aStatoProcedimento
	 *            tipo stato del procedimento.
	 * @param aTipoProc
	 *            tipo procedimento.
	 * @throws DAOException
	 *             propga errore di eccezione.
	 */
	public void ricercaProcedimentiRuoloByUdienzaOrOrdinanza(BigDecimal aIdUdienza, String aOrderBy,
			String aStatoProcedimento, String aTipoProc, String flagModifBlocco, String codMagi) throws DAOException {

		siesLogger.info("inizio");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaProcedimentiRuoloByUdienzaOrOrdinanza: OrderBy -> " + aOrderBy);

		String lSql = getSqlQueryByUdienzaOrOrdinanza(aIdUdienza);
		lSql += getFiltroTipoProcOrd(aTipoProc);
		lSql += getFiltroStatoProcOrd(aStatoProcedimento);
		lSql += getFiltroFlagModifBlocco(flagModifBlocco);
		lSql += getFiltroCodMagAss(codMagi);
		
		lSql += getOrderBy(aOrderBy);

		setStatement(lSql);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
	}
	
	
	/**
	 * @param aValue
	 * @return
	 * @throws DAOException
	 */
	protected String getSqlQueryByUdienzaOrOrdinanza(Object aValue) throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT DISTINCT " + " UP.ID_UDIENZA_PROCEDIMENTO_SIGE, " + " UP.FLAG_RINVIATA, "
			
						+ " DESC_FLAG_UDIENZA.RV_MEANING AS DESC_FLAG_UDIENZA, " + " UP.COD_OPERATORE_INSERIMENTO,"
						+ " UP.DATA_INSERIMENTO, " + " UP.COD_UFFICIO_INSERIMENTO, "
						+ " UP.COD_OPERATORE_AGGIORNAMENTO, " + " UP.DATA_AGGIORNAMENTO, "
						+ " UP.COD_UFFICIO_AGGIORNAMENTO, " + " UP.FAS_ID_FASCICOLO_SIGE, "
						+ " UP.UDI_ID_UDIENZA_SIGE, " + " UP.UDI_ID_UDIENZA_RINVIO, " + " UP.EVE_ID_EVENTO, "
				
				+ " PGG.ID_PROVVEDIMENTO_SIGE, " + " PGG.ID_EVENTO_GENERATO, "
				+ " PGG.COD_TIPO_PROVVEDIMENTO_SIGE, " + " UDI.ID_UDIENZA_SIGE, "
				+ " UDI.DATA_UDIENZA, " + " COLLEGIO.COD_COLLEGIO, "
				+ " MAS.MAG_COD_MAGISTRATO AS MAG_COD_MAGISTRATO_ASS, " + " MAG.NOME AS NOME_MAGISTRATO, "
				+ " MAG.COGNOME AS COGNOME_MAGISTRATO, " + " AVV.ID_AVVOCATO, "
				+ " AVV.COGNOME AS COGNOME_AVV, " + " AVV.NOME AS NOME_AVV, " + " AVV.FORO AS FORO, "
				+ " DESC_TIPO_AVV.RV_MEANING AS DESC_TIPO_AVVOCATO,"
				+ " SEZIONE.DESCRIZIONE AS DESC_SEZIONE, " + " FAS.CHIAVE_ANNO, "
				+ " FAS.CHIAVE_PROGR, " + " FAS.COD_POSIZIONE_GIURIDICA, "
				+ " FAS.ID_FASCICOLO_SIGE, " + " FAS.COD_STATO_FASCICOLO, "
				+ " DESC_STATO_FASCICOLO.RV_MEANING AS DESC_STATO_FASCICOLO, "
				+ " DESC_POSIZIONE_GIURIDICA.RV_MEANING AS DESC_POSIZIONE_GIURIDICA, "
				+ " DESC_ESITO_PROVVEDIMENTO.RV_MEANING AS DESC_ESITO_PROVVEDIMENTO, "
				+ " DESC_TIPO_PROVVEDIMENTO_SIGE.RV_MEANING AS DESC_TIPO_PROVVEDIMENTO_SIGE, " +				
				" SOGGETTO.ID_SOGGETTO, " + " SOGGETTO.COGNOME AS COGNOME, " + " SOGGETTO.NOME NOME, "
				+ " SOGGETTO.DATA_NASCITA AS DATA_NASCITA, " + " COMUNE.DESCRIZIONE AS DESCR_COMUNE_NASCITA, "
				+ " PROV.RV_MEANING AS DESCR_PROVINCIA_NASCITA, " + " NAZ.RV_MEANING AS DESCR_STATO_NASCITA, "
				
				// inizio modifica per sies 11.2.1
				+ " PROC.COGNOME                                   AS COGNOME_PROCUR,     "
				+ " PROC.NOME                                      AS NOME_PROCUR,"
				+ " CAN.COGNOME                                    AS COGNOME_CANC,"
				+ " CAN.NOME                                       AS NOME_CANC"				
				// fine modifica
				
				+ " FROM UDIENZA_SIGE UDI "
				+ " LEFT OUTER JOIN UDIENZA_PROCEDIMENTO_SIGE UP ON UDI.ID_UDIENZA_SIGE = UP.UDI_ID_UDIENZA_SIGE "
				+ " LEFT OUTER JOIN PROVVEDIMENTO_SIGE  PG  ON UDI.ID_UDIENZA_SIGE = PG.UDI_ID_UDIENZA_SIGE "
				+ " LEFT OUTER JOIN COLLEGIO ON UDI.COL_ID_COLLEGIO = COLLEGIO.ID_COLLEGIO "
				+ " LEFT OUTER JOIN SEZIONE ON COLLEGIO.SEZ_ID_SEZIONE = SEZIONE.ID_SEZIONE "
				+ " LEFT OUTER JOIN MAGISTRATO_ASSEGNATARIO MAS "
				+ "  ON ( MAS.FAS_SIGE_ID_FASCICOLO_SIGE = UP.FAS_ID_FASCICOLO_SIGE  OR   MAS.FAS_SIGE_ID_FASCICOLO_SIGE =PG.FAS_ID_FASCICOLO_SIGE ) "				
				+ " AND MAS.DATA_FINE IS NULL "
				+ " LEFT OUTER JOIN MAGISTRATO MAG ON MAG.COD_MAGISTRATO = MAS.MAG_COD_MAGISTRATO  "
				+ " INNER JOIN FASCICOLO_SIGE FAS"
				+ "  ON ( FAS.ID_FASCICOLO_SIGE = MAS.FAS_SIGE_ID_FASCICOLO_SIGE ) "
				+ " INNER JOIN CG_REF_CODES DESC_STATO_FASCICOLO ON "
				+ " DESC_STATO_FASCICOLO.RV_LOW_VALUE = FAS.COD_STATO_FASCICOLO "
				+ " AND DESC_STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO' "
				+ " INNER JOIN SOGGETTO ON ID_SOGGETTO = FAS.SOG_ID_SOGGETTO "
				// 20170914: [SG] left outer su avvocato
				+ " LEFT OUTER JOIN AVVOCATO_FASCICOLO_SIGE AVV_FAS_SIGE ON "
				+ " AVV_FAS_SIGE.FAS_SIGE_ID_FASCICOLO_SIGE = FAS.ID_FASCICOLO_SIGE "
				+ " AND AVV_FAS_SIGE.DATA_FINE_VALIDITA IS NULL "
				+ " LEFT OUTER JOIN AVVOCATO AVV ON AVV.ID_AVVOCATO = AVV_FAS_SIGE.AVV_ID_AVVOCATO "
				+ " LEFT OUTER JOIN CG_REF_CODES DESC_TIPO_AVV ON "
				+ " AVV_FAS_SIGE.COD_TIPO_AVVOCATO = DESC_TIPO_AVV.RV_LOW_VALUE "
				+ " AND DESC_TIPO_AVV.RV_DOMAIN ='TIPO_AVVOCATO'"
				+ " INNER JOIN COMUNE ON SOGGETTO.COD_COMUNE_NASCITA = COMUNE.COD_COMUNE "
				+ " INNER JOIN CG_REF_CODES PROV ON PROV.RV_LOW_VALUE = SOGGETTO.COD_PROVINCIA_NASCITA "
				+ " AND PROV.RV_DOMAIN = 'PROVINCIA' " +

				" INNER JOIN CG_REF_CODES NAZ ON NAZ.RV_LOW_VALUE = SOGGETTO.COD_STATO_NASCITA "
				+ " AND NAZ.RV_DOMAIN = 'NAZIONE' "
				+ " INNER JOIN CG_REF_CODES DESC_POSIZIONE_GIURIDICA ON DESC_POSIZIONE_GIURIDICA.RV_LOW_VALUE = FAS.COD_POSIZIONE_GIURIDICA  "
				+ " AND DESC_POSIZIONE_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA' " +

				// 2011-01-31
				" INNER JOIN TENORE_SIGE TEN ON TEN.FAS_ID_FASCICOLO_SIGE = FAS.ID_FASCICOLO_SIGE "
				+ " AND TEN.DATA_FINE IS NULL "
				+ " INNER JOIN PROVVEDIMENTO_SIGE PGG ON PGG.ID_PROVVEDIMENTO_SIGE = TEN.PROV_ID_PROVVEDIMENTO_SIGE "
				+ " AND ( PGG.DEFINITORIO IN ('S','N') OR PGG.COD_TIPO_PROVVEDIMENTO_SIGE IN ('01','04','50')  )"
				+

				// 2011-01-31
				" INNER JOIN EVENTO ON ID_EVENTO = PGG.ID_EVENTO_GENERATO "
				+ " AND ( EVENTO.FLAG_DOCUMENTO_REGISTRATO <>'A' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL ) "
				+

				" INNER JOIN CG_REF_CODES DESC_ESITO_PROVVEDIMENTO ON EVENTO.COD_ESITO = DESC_ESITO_PROVVEDIMENTO.RV_LOW_VALUE "
				+ " AND DESC_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO' "
			+ " LEFT OUTER JOIN CG_REF_CODES DESC_FLAG_UDIENZA ON UP.FLAG_RINVIATA = DESC_FLAG_UDIENZA.RV_LOW_VALUE "
			+ " AND DESC_FLAG_UDIENZA.RV_DOMAIN ='STATO_FLAG_RINVIATA' "
				+ " INNER JOIN CG_REF_CODES DESC_TIPO_PROVVEDIMENTO_SIGE ON PGG.COD_TIPO_PROVVEDIMENTO_SIGE = DESC_TIPO_PROVVEDIMENTO_SIGE.RV_LOW_VALUE "
				+ " AND DESC_TIPO_PROVVEDIMENTO_SIGE.RV_DOMAIN ='TIPO_PROVVEDIMENTO_SIGE' "
		
			   // inizio modifica per sies 11.2.1
				+ " LEFT OUTER JOIN MAGISTRATO PROC ON PROC.COD_MAGISTRATO = MAS.COD_PROCURATORE"
				+ " LEFT OUTER JOIN ASSISTENTE_GIUDIZIARIO CAN  ON CAN.ID_ASSISTENTE_GIUDIZIARIO = MAS.COD_ID_ASSISTENTE "  ;
			   // fine modifica per sies 11.2.1

		// Aggiunge la where condition su id_udienza o data_udienza
		if (aValue instanceof java.math.BigDecimal){
			BigDecimal aKey = (BigDecimal) aValue;			
		   lStatement +=" WHERE UDI.ID_UDIENZA_SIGE = " + aKey;
		}
		else if (aValue instanceof java.util.Date){
			Date aDate = (Date) aValue;
			 lStatement +=" WHERE UDI.DATA_UDIENZA = " + " TO_DATE("
					+ DateUtils.getDateToString(aDate, "yyyyMMdd") + ",'YYYYMMDD') ";			
		}
		else
			throw new DAOException(
					"Il valore passato non è del tipo : BigDecimal ( IDUdienza ) o Date ( DataUdienza ).");

		return lStatement;
	}

}