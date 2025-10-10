package siap.siep.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.MinorMask;

/**
 * FascicoloSiepSqlDAO - Realizza Sql DAo del Fascicolo Siep
 *
 * @version 1.0
 */
public class FascicoloSiepSqlDAO extends SIAPSqlDAO {

	public FascicoloSiepSqlDAO(Connection aCon) {
		super(aCon);
	}

	/*
	 * 23/08/2007 Ottimizzazione query ricerca Fascicolo SIEP (lato SIUS). protected String
	 * getFascicoloSqlQuery() { String lStatement = new String();
	 *
	 * lStatement += "SELECT FASC.ANNO_FASCICOLO_UNIONE, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR,"; lStatement +=
	 * " FASC.CHIAVE_UFFICIO, DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE
	 * DESCR_COMUNE_UFFICIO," ; lStatement += " DESCR_TIPO_UFF.RV_LOW_VALUE COD_TIPO_UFFICIO,"; lStatement +=
	 * " FASC.COD_MOTIVO_ARCHIVIAZIONE, MOTIVO_ARCHIVIAZIONE.RV_MEANING DESCR_MOTIVO_ARCHIVIAZIONE,";
	 * lStatement += " FASC.COD_OPERATORE_AGGIORNAMENTO, FASC.COD_OPERATORE_INSERIMENTO,"; lStatement +=
	 * " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,"; lStatement +=
	 * " FASC.COD_TIPO_POS_LIBERO, TIPO_POS_LIBERO.RV_MEANING DESCR_TIPO_POS_LIBERO, "; lStatement +=
	 * " FASC.COD_UFFICIO_AGGIORNAMENTO, FASC.COD_UFFICIO_INSERIMENTO, FASC.DATA_AGGIORNAMENTO,"; lStatement
	 * += " FASC.DATA_ARCHIVIAZIONE, FASC.DATA_INSERIMENTO,"; lStatement +=
	 * " FASC.DATA_ISCRIZIONE, FASC.DATA_UNIONE, FASC.FAS_SIE_ID_FASCICOLO_SIEP,"; lStatement +=
	 * " FASC.FLAG_VALIDATO, FASC.ID_FASCICOLO_SIEP, FASC.LETTERA_FASCICOLO,"; lStatement +=
	 * " FASC.NOTE NOTE_FASCICOLO, FASC.NUM_FASCICOLO_UNIONE, FASC.SEN_ID_SENTENZA, FASC.SOG_ID_SOGGETTO,";
	 * lStatement += " FASC.FLAG_ALTRA_CAUSA, FASC.DATA_IRREVOCABILITA, "; lStatement +=
	 * " FASC.FLAG_CUMULANTE, "; lStatement += " FASC.FLAG_CUMULATO, "; lStatement += "
	 * FASC.COD_UFFICIO_UNIONE, DESCR_TIPO_UFFUNIONE.RV_MEANING DESCR_TIPO_UFFICIO_UNIONE,
	 * DESCR_COM_UFFUNIONE.DESCRIZIONE DESCR_COMUNE_UFFICIO_UNIONE " ; lStatement +=
	 * " FROM FASCICOLO_SIEP FASC, CG_REF_CODES MOTIVO_ARCHIVIAZIONE,"; lStatement +=
	 * " CG_REF_CODES STATO_FASCICOLO, CG_REF_CODES TIPO_POS_LIBERO, "; lStatement +=
	 * " UFFICIO UFF, CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF, "; lStatement +=
	 * " UFFICIO UFFUNIONE, CG_REF_CODES DESCR_TIPO_UFFUNIONE, COMUNE DESCR_COM_UFFUNIONE"; lStatement += "
	 * WHERE (MOTIVO_ARCHIVIAZIONE.RV_DOMAIN = 'MOTIVO_ARCHIVIAZIONE' AND MOTIVO_ARCHIVIAZIONE.RV_LOW_VALUE =
	 * FASC.COD_MOTIVO_ARCHIVIAZIONE)" ; lStatement += " AND (STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO'
	 * AND STATO_FASCICOLO.RV_LOW_VALUE = FASC.COD_STATO_FASCICOLO)" ; lStatement +=
	 * " AND (DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE)"
	 * ; lStatement += " AND (FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO)"; lStatement +=
	 * " AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE)"; lStatement += " AND (DESCR_TIPO_UFFUNIONE.RV_DOMAIN
	 * = 'TIPO_UFFICIO' AND UFFUNIONE.COD_TIPO_UFFICIO = DESCR_TIPO_UFFUNIONE.RV_LOW_VALUE)" ; lStatement +=
	 * " AND (FASC.COD_UFFICIO_UNIONE = UFFUNIONE.COD_UFFICIO)"; lStatement +=
	 * " AND (UFFUNIONE.COD_COMUNE = DESCR_COM_UFFUNIONE.COD_COMUNE)"; lStatement += " AND
	 * (TIPO_POS_LIBERO.RV_DOMAIN = 'TIPO_POS_LIBERO' AND TIPO_POS_LIBERO.RV_LOW_VALUE =
	 * FASC.COD_TIPO_POS_LIBERO)";
	 *
	 * return lStatement; }
	 */
	protected String getFascicoloSqlQuery() {
		return getFascicoloSqlQuery("");
	}

	// MEV_57: aggiunto parametro di passaggio
	protected String getFascicoloSqlQuery(String majorOffice) {

		String lStatement = new String();

		lStatement += "SELECT FASC.ANNO_FASCICOLO_UNIONE, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR,";
		lStatement += " FASC.CHIAVE_UFFICIO, DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO,"
				+ " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_TIPO_UFF.RV_LOW_VALUE  COD_TIPO_UFFICIO,";
		lStatement += " FASC.COD_MOTIVO_ARCHIVIAZIONE, MOTIVO_ARCHIVIAZIONE.RV_MEANING DESCR_MOTIVO_ARCHIVIAZIONE,";
		lStatement += " FASC.COD_OPERATORE_AGGIORNAMENTO, FASC.COD_OPERATORE_INSERIMENTO,";
		lStatement += " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
		lStatement += " FASC.COD_TIPO_POS_LIBERO, TIPO_POS_LIBERO.RV_MEANING DESCR_TIPO_POS_LIBERO, ";
		lStatement += " FASC.COD_UFFICIO_AGGIORNAMENTO, FASC.COD_UFFICIO_INSERIMENTO,"
				+ " FASC.DATA_AGGIORNAMENTO,";
		lStatement += " FASC.DATA_ARCHIVIAZIONE, FASC.DATA_INSERIMENTO,";
		lStatement += " FASC.DATA_ISCRIZIONE, FASC.DATA_UNIONE, FASC.FAS_SIE_ID_FASCICOLO_SIEP,";
		lStatement += " FASC.FLAG_VALIDATO, FASC.ID_FASCICOLO_SIEP, FASC.LETTERA_FASCICOLO,";
		lStatement += " FASC.NOTE NOTE_FASCICOLO, FASC.NUM_FASCICOLO_UNIONE, FASC.SEN_ID_SENTENZA,"
				+ " FASC.SOG_ID_SOGGETTO,";
		lStatement += " FASC.FLAG_ALTRA_CAUSA, FASC.DATA_IRREVOCABILITA, ";
		lStatement += " FASC.FLAG_CUMULANTE, ";
		lStatement += " FASC.FLAG_CUMULATO, ";
		// lStatement +=
		// " FASC.COD_UFFICIO_UNIONE, DESCR_TIPO_UFFUNIONE.RV_MEANING DESCR_TIPO_UFFICIO_UNIONE,
		// DESCR_COM_UFFUNIONE.DESCRIZIONE DESCR_COMUNE_UFFICIO_UNIONE ";
		lStatement += " FASC.COD_UFFICIO_UNIONE, DESCR_TIPO_UFFUNIONE.RV_MEANING DESCR_TIPO_UFFICIO_UNIONE,"
				+ " DESCR_COM_UFFUNIONE.DESCRIZIONE DESCR_COMUNE_UFFICIO_UNIONE, FASC.KEY_PROVV_NSC ";
		/* inizio modifica Marzo 2010 */
		lStatement += " ,FASC.DATA_ARRIVO_ATTO ";
		/* fine modifica Marzo 2010 */
		// Modifica Accorpamento Uffici
		lStatement += " ,FASC.CHIAVE_PROGR_ORIG ";
		lStatement += " ,UFFINSERIMENTO.COD_TIPO_UFFICIO COD_TIPO_UFFICIO_INS, "
				+ "DESCR_TIPO_UFFINSERIMENTO.RV_MEANING DESCR_TIPO_UFFICIO_INS, "
				+ "DESCR_COM_UFFINSERIMENTO.DESCRIZIONE DESCR_COMUNE_UFFICIO_INS ";
		lStatement += " ,UFFINSERIMENTO.FLAG_ACCORP FLAG_UFFICIO_ACCORPATO";
		lStatement += " ,FASC.VISIBILITA_EX_MINORENNE ";

		// Modifica MEV 12 (Richiesta Certificato Penale)
		// lStatement += " , CERTIFICATO_PENALE ";
		// lStatement += " , DBMS_LOB.GETLENGTH(CERTIFICATO_PENALE) LEN_BLOB_CERT_PENALE ";

		lStatement += " FROM FASCICOLO_SIEP FASC";
		lStatement += " LEFT OUTER JOIN UFFICIO UFF ON (FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFF ON (UFF.COD_TIPO_UFFICIO = "
				+ "DESCR_TIPO_UFF.RV_LOW_VALUE AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFF ON (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE)";
		lStatement += " LEFT OUTER JOIN UFFICIO UFFUNIONE ON (FASC.COD_UFFICIO_UNIONE = UFFUNIONE.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFFUNIONE ON (UFFUNIONE.COD_TIPO_UFFICIO ="
				+ " DESCR_TIPO_UFFUNIONE.RV_LOW_VALUE AND DESCR_TIPO_UFFUNIONE.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFFUNIONE ON (UFFUNIONE.COD_COMUNE ="
				+ " DESCR_COM_UFFUNIONE.COD_COMUNE)";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES STATO_FASCICOLO ON (FASC.COD_STATO_FASCICOLO ="
				+ " STATO_FASCICOLO.RV_LOW_VALUE AND STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES MOTIVO_ARCHIVIAZIONE ON (FASC.COD_MOTIVO_ARCHIVIAZIONE ="
				+ " MOTIVO_ARCHIVIAZIONE.RV_LOW_VALUE AND MOTIVO_ARCHIVIAZIONE.RV_DOMAIN = 'MOTIVO_ARCHIVIAZIONE')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES TIPO_POS_LIBERO ON (FASC.COD_TIPO_POS_LIBERO ="
				+ " TIPO_POS_LIBERO.RV_LOW_VALUE AND TIPO_POS_LIBERO.RV_DOMAIN = 'TIPO_POS_LIBERO')";
		// Modifica Accorpamento Uffici
		lStatement += " LEFT OUTER JOIN UFFICIO UFFINSERIMENTO ON (FASC.Cod_Ufficio_Inserimento ="
				+ " UFFINSERIMENTO.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFFINSERIMENTO ON (UFFINSERIMENTO.COD_TIPO_UFFICIO ="
				+ " DESCR_TIPO_UFFINSERIMENTO.RV_LOW_VALUE AND DESCR_TIPO_UFFINSERIMENTO.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFFINSERIMENTO ON (UFFINSERIMENTO.COD_COMUNE ="
				+ " DESCR_COM_UFFINSERIMENTO.COD_COMUNE)";

		// MEV_57: aggiunto parametro di passaggio
		if (StringUtils.checkValidValue(majorOffice))
			lStatement += " LEFT OUTER JOIN V_SOGGETTO_ETA vse ON (FASC.id_fascicolo_siep ="
					+ " vse.FAS_SIE_ID_FASCICOLO_SIEP)";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIEP is not NULL";

		return lStatement;
	}

	// Ambrosino 03/2010
	protected String getFascicoloUfficioSqlQuery(String TipoFascicolo) {
		String lStatement = new String();

		lStatement += "SELECT FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR, FASC.CHIAVE_UFFICIO,";
		lStatement += " UFF.COD_TIPO_UFFICIO TIPO_UFFICIO, UFFD.DESCR_COMUNE DESCR_COMUNE_UFF";
		lStatement += " FROM " + TipoFascicolo + " FASC, UFFICIO UFF, UFFICIO_DESCR UFFD ";

		return lStatement;
	}

	protected String getIDFascicoloSqlQuery() {
		String lStatement = new String();

		lStatement += "SELECT ID_FASCICOLO_SIEP FROM FASCICOLO_SIEP WHERE ";

		return lStatement;
	}

	/**
	 * Esegue la ricerca di un fascicolo
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicolo(FascicoloSiepModel aModel) throws DAOException {
		String lStatement = getFascicoloSqlQuery();
		lStatement += " " + setCondizione(aModel);
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	// Ambrosino 03/2010
	/**
	 * Esegue la ricerca di un fascicolo Con I campi dell'ufficio
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicoloProgFasc(FascicoloSiepModel aModel, String TipoFascicolo)
			throws DAOException {
		String lStatement = getFascicoloUfficioSqlQuery(TipoFascicolo);
		lStatement += " " + setCondizioneUfficio(aModel);

		setStatement(lStatement);
	}

	public void ricercaFascicoloBySoggettoValidato(BigDecimal aIdModel) throws DAOException {
		String lStatement = getFascicoloSqlQuery();
		lStatement += " AND SOG_ID_SOGGETTO = " + aIdModel;
		lStatement += " AND FLAG_VALIDATO = 'S' " + setOrder();

		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca di un fascicolo
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicoloPerUfficio(FascicoloSiepModel aModel) throws DAOException {
		String lStatement = getFascicoloSqlQuery();
		lStatement += " " + setCondizione(aModel);
		lStatement += " AND (FASC.CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "')";
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	public boolean isValidate(BigDecimal aKey) throws DAOException {
		this.ricercaFascicoloByKey(aKey);
		FascicoloSiepModel lFasc = new FascicoloSiepModel((FascicoloSiepModel) this.getModelByKey());

		if (lFasc.getFlagValidato().compareTo("S") == 0)
			return true;
		else
			return false;
	}

	/**
	 * Esegue la ricerca di un fascicolo
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicoloUfficioEValidatiAltriUffici(FascicoloSiepModel aModel) throws DAOException {
		String lStatement = getFascicoloSqlQuery();
		lStatement += " " + setCondizione(aModel);
		lStatement += " AND (FASC.CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "')";
		lStatement += " UNION ";
		lStatement += getFascicoloSqlQuery();
		lStatement += " " + setCondizione(aModel);
		lStatement += " AND (FASC.FLAG_VALIDATO = 'S')";
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca di un fascicolo tramite Chiave
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicoloByKey(FascicoloSiepModel aModel) throws DAOException {
		String lStatement = getFascicoloSqlQuery();

		lStatement += " AND ID_FASCICOLO_SIEP = " + aModel.getIdFascicoloSiep();

		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca di un fascicolo tramite Chiave
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicoloByKey(BigDecimal aKey) throws DAOException {
		String lStatement = getFascicoloSqlQuery();

		lStatement += " AND ID_FASCICOLO_SIEP = " + aKey;

		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca dei fascicoli siep per Progressivo, Anno e Ufficio ovvero per chiave naturale
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicoloByProgrAnnoCodUfficio(FascicoloSiepModel aModel) throws DAOException {
		String lStatement = getFascicoloSqlQuery();

		// 18/11/2015 eliminato il test sui valori <>null. La ricerca va fatta con
		// l'intera chiave sempre. Non ha senso effettuare la ricerca
		// per chiave parziale. Il risultato DEVE essere un SOLO record.
		// if ( (aModel.getChiaveProgr() != null) && (aModel.getChiaveProgr().longValue() > 0) )
		// {
		lStatement += " AND (CHIAVE_PROGR = " + aModel.getChiaveProgr() + ")";
		// }
		// if ( (aModel.getChiaveAnno() != null) && (aModel.getChiaveAnno().intValue() > 0) )
		// {
		lStatement += " AND (CHIAVE_ANNO = " + aModel.getChiaveAnno() + ")";
		// }
		// if ( (aModel.getChiaveUfficio() != null) && (!aModel.getChiaveUfficio().equals("")) )
		// {
		lStatement += " AND (CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "')";
		// }

		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca del fascicolo siep per Progressivo, Anno e Ufficio e ritorna solo l'ID
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaIDFascicoloByProgrAnnoCodUfficio(FascicoloSiepModel aModel) throws DAOException {
		String lStatement = getIDFascicoloSqlQuery();

		lStatement += " (CHIAVE_PROGR = " + aModel.getChiaveProgr() + ")";
		lStatement += " AND (CHIAVE_ANNO = " + aModel.getChiaveAnno() + ")";
		lStatement += " AND (CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "')";
		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca dei fascicoli siep per Progressivo, Anno e Ufficio
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicoloByProgrAnnoDescrComune(FascicoloSiepModel aModel) throws DAOException {
		ricercaFascicoloByProgrAnnoDescrComune(aModel, "");
	}

	/**
	 * Esegue la ricerca dei fascicoli siep per Progressivo, Anno e Ufficio
	 *
	 * MEV_57: aggiunto parametro di passaggio
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicoloByProgrAnnoDescrComune(FascicoloSiepModel aModel, String majorOffice)
			throws DAOException {
		// MEV_57: aggiunto parametro di passaggio
		String lStatement = getFascicoloSqlQuery(majorOffice);

		if ((aModel.getChiaveProgr() != null) && (aModel.getChiaveProgr().longValue() > 0))
			lStatement += " AND (CHIAVE_PROGR = " + aModel.getChiaveProgr() + ")";
		if ((aModel.getChiaveAnno() != null) && (aModel.getChiaveAnno().intValue() > 0))
			lStatement += " AND (CHIAVE_ANNO = " + aModel.getChiaveAnno() + ")";

		if ((aModel.getChiaveProgrIniziale() != null) && (aModel.getChiaveProgrIniziale().longValue() > 0))
			lStatement += " AND (CHIAVE_PROGR >= " + aModel.getChiaveProgrIniziale() + ")";
		if ((aModel.getChiaveAnnoIniziale() != null) && (aModel.getChiaveAnnoIniziale().intValue() > 0))
			// STUB 19/05/2005 lStatement += " AND (CHIAVE_ANNO >= "+aModel.getChiaveAnnoIniziale()+")";
			lStatement += " AND (CHIAVE_ANNO = " + aModel.getChiaveAnnoIniziale() + ")";

		if ((aModel.getChiaveProgrFinale() != null) && (aModel.getChiaveProgrFinale().longValue() > 0))
			lStatement += " AND (CHIAVE_PROGR <= " + aModel.getChiaveProgrFinale() + ")";
		// STUB 19/05/2005 if ( (aModel.getChiaveAnnoFinale() != null) &&
		// (aModel.getChiaveAnnoFinale().longValue() > 0) )
		// lStatement += " AND (CHIAVE_ANNO <= "+aModel.getChiaveAnnoFinale()+")";

		if ((aModel.getCodUfficioAccorpato() != null) && (!aModel.getCodUfficioAccorpato().equals(""))) {
			// Si utilizza getCodUfficioAccorpato come veicolo per leggermi il codice ufficio accorpato
			lStatement += " AND (FASC.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioAccorpato() + "')";

		} else {
			if ((aModel.getDescrComuneUfficio() != null) && (!aModel.getDescrComuneUfficio().equals("")))
				lStatement += " AND (DESCR_COM_UFF.DESCRIZIONE = '"
						+ StringUtils.convertSqlString(aModel.getDescrComuneUfficio()) + "')";

			// Si utilizza getCodUfficioInserimento come veicolo per leggermi il codice ufficio
			if ((aModel.getCodUfficioInserimento() != null))
				lStatement += " AND (UFF.COD_UFFICIO = nvl('" + aModel.getCodUfficioInserimento()
						+ "', UFF.COD_UFFICIO ) )";

			// Si utilizza getChiaveUfficio come veicolo per leggermi il tipo ufficio
			if ((aModel.getChiaveUfficio() != null))
				lStatement += " AND (UFF.COD_TIPO_UFFICIO = nvl('" + aModel.getChiaveUfficio()
						+ "', UFF.COD_TIPO_UFFICIO ) )";
		}

		// MEV_57: aggiunta condizione
		if (StringUtils.checkValidValue(majorOffice))
			lStatement += MinorMask.minorCondition("vse", "FASC", majorOffice);

		lStatement += " AND FLAG_VALIDATO = 'S' " + setOrder();

		setStatement(lStatement);
	}

	/**
	 * Setta le condizioni per la Ricerca
	 *
	 * @param aModel
	 * @return
	 */
	private String setCondizione(FascicoloSiepModel aModel) {
		String lCondizioni = new String();
		if ((aModel.getSenIdSentenza() != null) && (aModel.getSenIdSentenza().intValue() > 0)) {
			lCondizioni = " AND SEN_ID_SENTENZA = " + aModel.getSenIdSentenza() + "";
		}
		if ((aModel.getSogIdSoggetto() != null)
		// && (aModel.getSogIdSoggetto().intValue() > 0)
		) {
			lCondizioni += " AND SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto() + "";
		}
		if ((aModel.getIdFascicoloSiep() != null) && (aModel.getIdFascicoloSiep().intValue() > 0)) {
			lCondizioni += " AND ID_FASCICOLO_SIEP = " + aModel.getIdFascicoloSiep() + "";
		}
		// Cerca i fascicoli a partire da una coppia Progressivo/Anno
		if ((aModel.getChiaveAnnoIniziale() != null) && (aModel.getChiaveAnnoIniziale().intValue() > 0)
				&& (aModel.getChiaveProgrIniziale() != null)
				&& (aModel.getChiaveProgrIniziale().longValue() > 0)) {
			lCondizioni += " AND ( (CHIAVE_ANNO > " + aModel.getChiaveAnnoIniziale() + ")";
			lCondizioni += " OR (CHIAVE_ANNO = " + aModel.getChiaveAnnoIniziale() + " AND CHIAVE_PROGR >= "
					+ aModel.getChiaveProgrIniziale() + "))";
		}
		// Cerca i fascicoli fino ad una coppia Progressivo/Anno
		if ((aModel.getChiaveAnnoFinale() != null) && (aModel.getChiaveAnnoFinale().intValue() > 0)
				&& (aModel.getChiaveProgrFinale() != null)
				&& (aModel.getChiaveProgrFinale().longValue() > 0)) {
			// Nel caso non venga specificata la coppia di ricerca iniziale,
			// vengono cercati i fascicoli
			// a partire dal primo fascicolo dell'anno finale specificato
			if ((aModel.getChiaveAnnoIniziale() == null)
					|| (aModel.getChiaveAnnoIniziale().intValue() <= 0)
							&& (aModel.getChiaveProgrIniziale() == null)
					|| (aModel.getChiaveProgrIniziale().longValue() <= 0)) {
				lCondizioni += " AND ( (CHIAVE_ANNO > " + aModel.getChiaveAnnoFinale() + ")";
				lCondizioni += " OR (CHIAVE_ANNO = " + aModel.getChiaveAnnoFinale()
						+ " AND CHIAVE_PROGR >= 1))";
			}

			lCondizioni += " AND ( (CHIAVE_ANNO < " + aModel.getChiaveAnnoFinale() + ")";
			lCondizioni += " OR (CHIAVE_ANNO = " + aModel.getChiaveAnnoFinale() + " AND CHIAVE_PROGR <= "
					+ aModel.getChiaveProgrFinale() + "))";
		}
		/*
		 * if ( (aModel.getChiaveAnno()!= null ) && (aModel.getChiaveProgr()!= null) ) { if (
		 * (aModel.getChiaveAnno().intValue() > 0 ) && (aModel.getChiaveProgr().longValue() > 0) ) {
		 * lCondizioni += " AND CHIAVE_ANNO = " + aModel.getChiaveAnno() + ""; lCondizioni +=
		 * " AND CHIAVE_PROGR = " + aModel.getChiaveProgr() + ""; } }
		 */

		// Ambrosino 27/03/2009
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni = " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + "";
		}

		return lCondizioni;
	}

	// Ambrosino 03/2010

	/**
	 * Setta le condizioni per la Ricerca ricercaFascicoloProgFasc
	 *
	 * @param aModel
	 * @return
	 */
	private String setCondizioneUfficio(FascicoloSiepModel aModel) {
		String lCondizioni = new String();

		lCondizioni += " WHERE FASC.SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto();
		lCondizioni += " AND FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO";
		lCondizioni += " AND FASC.CHIAVE_UFFICIO = UFFD.COD_UFFICIO";

		// paolo cherubini 28/09/2010 escludo dalla ricerca i fascicoli inseriti per il cumulo
		lCondizioni += " and (fasc.chiave_progr <= 700000 or fasc.chiave_progr >= 800000) ";

		return lCondizioni;
	}

	private String setOrder() {
		String lOrder = new String();

		lOrder = " ORDER BY 2, 3 ";

		return lOrder;
	}

	/**
	 * Esegue la ricerca del fascicolo siep per ID Soggetto, ID Sentenza e ritorna solo l'ID
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaIDFascicoloByIDSoggettoIDSentenza(FascicoloSiepModel aModel) throws DAOException {
		String lStatement = getIDFascicoloSqlQuery();

		lStatement += " SOG_ID_SOGGETTO ='" + aModel.getSogIdSoggetto() + "' ";
		lStatement += " AND SEN_ID_SENTENZA = '" + aModel.getSenIdSentenza() + "'";
		setStatement(lStatement);
	}

	/**
	 *
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		lFascicolo.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		lFascicolo.setDescrStatoFascicolo(getString("DESCR_STATO_FASCICOLO"));
		lFascicolo.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		lFascicolo.setDataArchiviazione(getDate("DATA_ARCHIVIAZIONE"));
		lFascicolo.setCodMotivoArchiviazione(getString("COD_MOTIVO_ARCHIVIAZIONE"));
		lFascicolo.setDescrMotivoArchiviazione(getString("DESCR_MOTIVO_ARCHIVIAZIONE"));
		lFascicolo.setLetteraFascicolo(getString("LETTERA_FASCICOLO"));
		lFascicolo.setAnnoFascicoloUnione(getString("ANNO_FASCICOLO_UNIONE"));
		lFascicolo.setNumFascicoloUnione(getString("NUM_FASCICOLO_UNIONE"));
		lFascicolo.setDataUnione(getDate("DATA_UNIONE"));
		lFascicolo.setNote(getString("NOTE_FASCICOLO"));
		lFascicolo.setCodTipoPosLibero(getString("COD_TIPO_POS_LIBERO"));
		lFascicolo.setDescrTipoPosLibero(getString("DESCR_TIPO_POS_LIBERO"));
		lFascicolo.setFlagValidato(getString("FLAG_VALIDATO"));
		lFascicolo.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		lFascicolo.setDataInserimento(getDate("DATA_INSERIMENTO"));
		lFascicolo.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		lFascicolo.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		lFascicolo.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		lFascicolo.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		lFascicolo.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		lFascicolo.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));
		lFascicolo.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		lFascicolo.setFlagAltraCausa(getString("FLAG_ALTRA_CAUSA"));
		lFascicolo.setCodTipoUfficio(getString("COD_TIPO_UFFICIO"));
		lFascicolo.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA"));
		lFascicolo.setFlagCumulante(getString("FLAG_CUMULANTE"));
		lFascicolo.setFlagCumulato(getString("FLAG_CUMULATO"));

		lFascicolo.setCodUfficioUnione(getString("COD_UFFICIO_UNIONE"));
		lFascicolo.setDescrTipoUfficioUnione(getString("DESCR_TIPO_UFFICIO_UNIONE"));
		lFascicolo.setDescrComuneUfficioUnione(getString("DESCR_COMUNE_UFFICIO_UNIONE"));
		lFascicolo.setKeyProvvNsc(getBigDecimal("KEY_PROVV_NSC"));

		lFascicolo.setDataArrivoAtto(getDate("DATA_ARRIVO_ATTO"));

		// Modifica Accorpamento Uffici
		lFascicolo.setChiaveProgrOrig(getBigDecimal("CHIAVE_PROGR_ORIG"));
		lFascicolo.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		lFascicolo.setCodTipoUfficioInserimento(getString("COD_TIPO_UFFICIO_INS"));
		lFascicolo.setDescrTipoUfficioInserimento(getString("DESCR_TIPO_UFFICIO_INS"));
		lFascicolo.setDescrComuneUfficioInserimento(getString("DESCR_COMUNE_UFFICIO_INS"));
		lFascicolo.setFlagUfficioAccorpato(getString("FLAG_UFFICIO_ACCORPATO"));
		lFascicolo.setVisibilitaMinorenne(getString("VISIBILITA_EX_MINORENNE"));

		// lFascicolo.caricaCertPenaleBlobOut(getBlob("CERTIFICATO_PENALE"));
		// lFascicolo.setLengthCertPenaleBlob(getBigDecimal("LEN_BLOB_CERT_PENALE") );

		/*
		 * SoggettoModel lSoggetto = new SoggettoModel();
		 *
		 * lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
		 * lSoggetto.setCodFiscale(getString("COD_FISCALE") ); lSoggetto.setCodCs(getString("COD_CS") );
		 * lSoggetto.setCodAfis(getString("COD_AFIS") ); lSoggetto.setCognome(getString("COGNOME") );
		 * lSoggetto.setNome(getString("NOME") ); lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
		 * lSoggetto.setDataNascita(getDate("DATA_NASCITA") );
		 * lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		 * lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
		 * lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA") );
		 * lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA") );
		 * lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		 * lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
		 * lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		 * lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO") );
		 * lSoggetto.setNazionalita(getString("NAZIONALITA") );
		 * lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		 * lSoggetto.setPaternita(getString("PATERNITA") );
		 * lSoggetto.setCognomeMadre(getString("COGNOME_MADRE") );
		 * lSoggetto.setNomeMadre(getString("NOME_MADRE") ); lSoggetto.setSesso(getString("SESSO") );
		 * lSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); lSoggetto.setNote(getString("NOTE_SOGGETTO")
		 * ); lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		 * //lSoggetto.setDescrComuneCasellario(getString("") );
		 * lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		 * //lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		 * //lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		 * //lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		 * //lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		 * //lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		 * //lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
		 *
		 * SentenzaModel lSentenza = new SentenzaModel();
		 *
		 * lSentenza.setIdSentenza(getBigDecimal("ID_SENTENZA") );
		 * lSentenza.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO") );
		 * lSentenza.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO") );
		 * lSentenza.setAnnoRegePm(getBigDecimal("ANNO_REGE_PM") );
		 * lSentenza.setNumeroRegePm(getString("NUMERO_REGE_PM") );
		 * lSentenza.setDataArrivoAtto(getDate("DATA_ARRIVO_ATTO") );
		 * lSentenza.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO") );
		 * lSentenza.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE") );
		 * lSentenza.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE") );
		 * lSentenza.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE") );
		 * lSentenza.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE") );
		 * lSentenza.setNumSezioneAutoritaEmittente(getString("NUM_SEZIONE_AUTORITA_EMITTENTE") );
		 * lSentenza.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA") );
		 * lSentenza.setNumeroSentenza(getString("NUMERO_SENTENZA") );
		 * lSentenza.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA") );
		 * lSentenza.setFlagSentenzaApplicazPena(getString("FLAG_SENTENZA_APPLICAZ_PENA") );
		 * //lSentenza.setCodTipoProvvRif(getString("COD_TIPO_PROVV_RIF") );
		 * //lSentenza.setDescrTipoProvvRif(getString("DESCR_TIPO_PROVVEDIMENTO_RIF") );
		 * lSentenza.setDataProvvRif(getDate("DATA_PROVV_RIF") );
		 * lSentenza.setCodTipoAutoritaProvvRif(getString("COD_TIPO_AUTORITA_PROVV_RIF") );
		 * lSentenza.setDescrTipoAutoritaProvvRif(getString("DESCR_TIPO_AUTORITA_PROVV_RIF") );
		 * lSentenza.setAnnoProvvRif(getBigDecimal("ANNO_PROVV_RIF") );
		 * lSentenza.setNumeroProvvRif(getString("NUMERO_PROVV_RIF") );
		 * lSentenza.setCodLuogoProvvRif(getString("COD_LUOGO_PROVV_RIF") );
		 * lSentenza.setDescrLuogoProvvRif(getString("DESCR_LUOGO_PROVV_RIF") );
		 * lSentenza.setNumSezioneAutoritaProvvRif(getString("NUM_SEZIONE_AUTORITA_PROVV_RIF") );
		 * lSentenza.setCodTipoDecisioneCassazione(getString("COD_TIPO_DECISIONE_CASSAZIONE") );
		 * lSentenza.setDescrTipoDecisioneCassazione(getString("DESCR_TIPO_DECISIONE_CASS") );
		 * lSentenza.setNote1DecisioneCassazione(getString("NOTE1_DECISIONE_CASSAZIONE") );
		 * lSentenza.setNote2DecisioneCassazione(getString("NOTE2_DECISIONE_CASSAZIONE") );
		 * lSentenza.setAnnoSentenzaCassazione(getBigDecimal("ANNO_SENTENZA_CASSAZIONE") );
		 * lSentenza.setNumeroSentenzaCassazione(getString("NUMERO_SENTENZA_CASSAZIONE") );
		 * lSentenza.setAnnoRaccoltaGenerale(getBigDecimal("ANNO_RACCOLTA_GENERALE") );
		 * lSentenza.setNumeroRaccoltaGenerale(getString("NUMERO_RACCOLTA_GENERALE") );
		 * lSentenza.setFlagAltreSentenze(getString("FLAG_ALTRE_SENTENZE") );
		 * lSentenza.setDescrAltreSentenze(getString("DESCR_ALTRE_SENTENZE") );
		 * lSentenza.setAnnoRegistro35(getBigDecimal("ANNO_REGISTRO_35") );
		 * lSentenza.setNumRegistro35(getString("NUM_REGISTRO_35") );
		 * lSentenza.setNote(getString("NOTE_SENTENZA") );
		 * lSentenza.setDescrNumCampionePenale(getString("DESCR_NUM_CAMPIONE_PENALE") );
		 * lSentenza.setAnnoRegeGip(getBigDecimal("ANNO_REGE_GIP") );
		 * lSentenza.setNumeroRegeGip(getString("NUMERO_REGE_GIP") );
		 * lSentenza.setAnnoRegeDib(getBigDecimal("ANNO_REGE_DIB") );
		 * lSentenza.setNumeroRegeDib(getString("NUMERO_REGE_DIB") );
		 * lSentenza.setAnnoRegeCas(getBigDecimal("ANNO_REGE_CAS") );
		 * lSentenza.setNumeroRegeCas(getString("NUMERO_REGE_CAS") );
		 * lSentenza.setAnnoRegeCap(getBigDecimal("ANNO_REGE_CAP") );
		 * lSentenza.setNumeroRegeCap(getString("NUMERO_REGE_CAP") );
		 * lSentenza.setAnnoRegeCasap(getBigDecimal("ANNO_REGE_CASAP") );
		 * lSentenza.setNumeroRegeCasap(getString("NUMERO_REGE_CASAP") ); // MEV_66: aggiunte quattro nuove
		 * proprietà lSentenza.setAnnoRegeGup(getBigDecimal("ANNO_REGE_GUP") );
		 * lSentenza.setNumeroRegeGup(getString("NUMERO_REGE_GUP") );
		 * lSentenza.setAnnoRegeCapsm(getBigDecimal("ANNO_REGE_CAPSM") );
		 * lSentenza.setNumeroRegeCapsm(getString("NUMERO_REGE_CAPSM") );
		 * //lSentenza.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		 * //lSentenza.setDataInserimento(getDate("DATA_INSERIMENTO") );
		 * //lSentenza.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		 * //lSentenza.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		 * //lSentenza.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		 * //lSentenza.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
		 *
		 * lFascicolo.setSoggetto(lSoggetto); lFascicolo.setSentenza(lSentenza);
		 */
		return lFascicolo;
	}

	// Ambrosino 03/2010

	public GenericModel getModelsPerRicUff() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO"));

		// lFascicolo.setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO") );
		lFascicolo.setDescrTipoUfficio(getString("TIPO_UFFICIO"));
		// lFascicolo.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO") );
		lFascicolo.setDescrComuneUfficio(getString("DESCR_COMUNE_UFF"));

		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));

		return lFascicolo;
	}

	/**
	 * Calcola il Massimo Progressivo relativo ad un certo ufficio e all'anno in corso. Il massimo progressivo
	 * rappresenta anche l'ultimo progressivo inserito all'intenro dell'ufficio trattato.
	 *
	 * @param aFascModel
	 * @throws DAOException
	 */
	public void getProgressivoFascicoloSiep(FascicoloSiepModel aFascModel) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(CHIAVE_PROGR) aMAX";
		lStatement += " FROM FASCICOLO_SIEP FS";
		lStatement += " WHERE FS.CHIAVE_ANNO = " + aFascModel.getChiaveAnno();
		lStatement += " AND FS.CHIAVE_UFFICIO = " + aFascModel.getChiaveUfficio();
		// Aggiunto GDV il 18/05/2004
		if (aFascModel.getTipoProgressivo() > 1)
			lStatement += " AND FS.CHIAVE_PROGR Between " + (aFascModel.getTipoProgressivo() * 10000)
					+ " AND " + (aFascModel.getTipoProgressivo() * 10000 + 9999);
		else
			// TipoProgressivo =1
			lStatement += " AND FS.CHIAVE_PROGR Between 1 AND "
					+ (aFascModel.getTipoProgressivo() * 10000 + 9999);

		setStatement(lStatement);
	}

	// Ascione 19/01/2005.
	// Query per la ricerca dei Fascicoli SIEP referenziabili a partire da un procedimento SIUS.
	//
	// 1) Attraverso il parametro FASC.aIdFascicoloSius, si selezionano tutti i FASCICOLI_SIUS dell'ufficio
	// (uguale CHIAVE_UFFICIO)
	// aventi i campi Anno_S1, Progr_S1, Cod_Tipo_Registro di Generale_Procedimento
	// uguali a quelli di FASC e aventi FAS_SIE_ID_FASCICOLO_SIEP differenti dal valore del campo equivalente
	// di FASC.
	// 2) Il Fascicolo SIUS fa riferimento ad un procedimento unificante (Numero_Fascicoli_Unificati > 0)
	// Attraverso il parametro FASC.aIdFascicoloSius, si selezionano tutti i FASCICOLI_SIUS dell'ufficio
	// (uguale CHIAVE_UFFICIO)
	// aventi il campo FAS_SIU_ID_FASCICOLO_SIUS uguale ID_FASCICOLO_SIUS di FASC
	// e aventi FAS_SIE_ID_FASCICOLO_SIEP differente dal valore del campo equivalente di FASC.
	//
	// Per ciascun Fascicolo SIEP selezionato si Estraggono tutti i dati previsti dalla
	// "getFascicoloSqlQuery".
	public void ricercaFascicoliReferenziatiDaSIUS(BigDecimal aIdFascicoloSius, BigDecimal aNumFascUnificati)
			throws DAOException {
		String lStatement = new String();

		lStatement += "SELECT DISTINCT FSIEP.ANNO_FASCICOLO_UNIONE, FSIEP.CHIAVE_ANNO, FSIEP.CHIAVE_PROGR,";
		lStatement += " FSIEP.CHIAVE_UFFICIO, DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO,"
				+ " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_TIPO_UFF.RV_LOW_VALUE  COD_TIPO_UFFICIO,";
		lStatement += " FSIEP.COD_MOTIVO_ARCHIVIAZIONE, MOTIVO_ARCHIVIAZIONE.RV_MEANING"
				+ " DESCR_MOTIVO_ARCHIVIAZIONE,";
		lStatement += " FSIEP.COD_OPERATORE_AGGIORNAMENTO, FSIEP.COD_OPERATORE_INSERIMENTO,";
		lStatement += " FSIEP.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
		lStatement += " FSIEP.COD_TIPO_POS_LIBERO, TIPO_POS_LIBERO.RV_MEANING DESCR_TIPO_POS_LIBERO, ";
		lStatement += " FSIEP.COD_UFFICIO_AGGIORNAMENTO, FSIEP.COD_UFFICIO_INSERIMENTO,"
				+ " FSIEP.DATA_AGGIORNAMENTO,";
		lStatement += " FSIEP.DATA_ARCHIVIAZIONE, FSIEP.DATA_INSERIMENTO,";
		lStatement += " FSIEP.DATA_ISCRIZIONE, FSIEP.DATA_UNIONE, FSIEP.FAS_SIE_ID_FASCICOLO_SIEP,";
		lStatement += " FSIEP.FLAG_VALIDATO, FSIEP.ID_FASCICOLO_SIEP, FSIEP.LETTERA_FASCICOLO,";
		lStatement += " FSIEP.NOTE NOTE_FASCICOLO, FSIEP.NUM_FASCICOLO_UNIONE, FSIEP.SEN_ID_SENTENZA,"
				+ " FSIEP.SOG_ID_SOGGETTO,";
		lStatement += " FSIEP.FLAG_ALTRA_CAUSA, FSIEP.DATA_IRREVOCABILITA, ";
		lStatement += " FSIEP.FLAG_CUMULANTE, ";
		lStatement += " FSIEP.FLAG_CUMULATO, ";
		// Dario
		lStatement += " FSIEP.COD_UFFICIO_UNIONE, DESCR_TIPO_UFFUNIONE.RV_MEANING DESCR_TIPO_UFFICIO_UNIONE,"
				+ " DESCR_COM_UFFUNIONE.DESCRIZIONE DESCR_COMUNE_UFFICIO_UNIONE, FSIEP.KEY_PROVV_NSC ";
		lStatement += " ,FSIEP.DATA_ARRIVO_ATTO ";
		// Modifica Accorpamento Uffici
		lStatement += " ,FSIEP.CHIAVE_PROGR_ORIG ";
		lStatement += " ,UFFINSERIMENTO.COD_TIPO_UFFICIO COD_TIPO_UFFICIO_INS,"
				+ " DESCR_TIPO_UFFINSERIMENTO.RV_MEANING DESCR_TIPO_UFFICIO_INS,"
				+ " DESCR_COM_UFFINSERIMENTO.DESCRIZIONE DESCR_COMUNE_UFFICIO_INS";
		lStatement += ", UFFINSERIMENTO.FLAG_ACCORP FLAG_UFFICIO_ACCORPATO";
		lStatement += ", FSIEP.VISIBILITA_EX_MINORENNE";
		// Modifica MEV 12 (Richiesta Certificato Penale)
		// lStatement += " , NULL CERTIFICATO_PENALE ";
		lStatement += " FROM FASCICOLO_SIEP FSIEP, CG_REF_CODES MOTIVO_ARCHIVIAZIONE,";
		lStatement += " CG_REF_CODES STATO_FASCICOLO, CG_REF_CODES TIPO_POS_LIBERO, ";
		lStatement += " FASCICOLO_SIUS FASC,GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FASC2,"
				+ " GENERALE_PROCEDIMENTO GP2,";
		lStatement += " UFFICIO UFF, CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF, ";
		// Dario
		lStatement += " UFFICIO UFFUNIONE, CG_REF_CODES DESCR_TIPO_UFFUNIONE, COMUNE DESCR_COM_UFFUNIONE";

		// Modifica Accorpamento Uffici
		lStatement += " , UFFICIO UFFINSERIMENTO , CG_REF_CODES DESCR_TIPO_UFFINSERIMENTO,"
				+ " COMUNE DESCR_COM_UFFINSERIMENTO ";

		lStatement += " WHERE FASC.ID_FASCICOLO_SIUS = '" + aIdFascicoloSius + "'";

		// Modifica Accorpamento Uffici
		lStatement += " AND FSIEP.COD_UFFICIO_INSERIMENTO = UFFINSERIMENTO.COD_UFFICIO ";
		lStatement += " AND UFFINSERIMENTO.COD_TIPO_UFFICIO = DESCR_TIPO_UFFINSERIMENTO.RV_LOW_VALUE ";
		lStatement += " AND DESCR_TIPO_UFFINSERIMENTO.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFFINSERIMENTO.COD_COMUNE = DESCR_COM_UFFINSERIMENTO.COD_COMUNE ";

		lStatement += " AND (FASC2.FAS_SIE_ID_FASCICOLO_SIEP <> FASC.FAS_SIE_ID_FASCICOLO_SIEP";
		lStatement += " AND FASC2.FAS_SIE_ID_FASCICOLO_SIEP = FSIEP.ID_FASCICOLO_SIEP)";
		lStatement += " AND FASC.CHIAVE_UFFICIO = FASC2.CHIAVE_UFFICIO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND FASC2.ID_FASCICOLO_SIUS = GP2.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND GP.ANNO_S1 = GP2.ANNO_S1";
		lStatement += " AND GP.PROGR_S1 = GP2.PROGR_S1";
		lStatement += " AND GP.COD_TIPO_REGISTRO = GP2.COD_TIPO_REGISTRO";
		lStatement += " AND (MOTIVO_ARCHIVIAZIONE.RV_DOMAIN = 'MOTIVO_ARCHIVIAZIONE' AND"
				+ " MOTIVO_ARCHIVIAZIONE.RV_LOW_VALUE = FSIEP.COD_MOTIVO_ARCHIVIAZIONE)";
		lStatement += " AND (STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO' AND"
				+ " STATO_FASCICOLO.RV_LOW_VALUE = FSIEP.COD_STATO_FASCICOLO)";
		lStatement += " AND (DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND"
				+ " UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE)";
		lStatement += " AND  FSIEP.CHIAVE_UFFICIO = UFF.COD_UFFICIO";
		lStatement += " AND  UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		// Dario
		lStatement += " AND (DESCR_TIPO_UFFUNIONE.RV_DOMAIN = 'TIPO_UFFICIO' AND UFFUNIONE.COD_TIPO_UFFICIO"
				+ " = DESCR_TIPO_UFFUNIONE.RV_LOW_VALUE)";
		lStatement += " AND (FSIEP.COD_UFFICIO_UNIONE = UFFUNIONE.COD_UFFICIO)";
		lStatement += " AND (UFFUNIONE.COD_COMUNE = DESCR_COM_UFFUNIONE.COD_COMUNE)";

		lStatement += " AND  TIPO_POS_LIBERO.RV_DOMAIN = 'TIPO_POS_LIBERO' AND TIPO_POS_LIBERO.RV_LOW_VALUE"
				+ " = FSIEP.COD_TIPO_POS_LIBERO";
		if (aNumFascUnificati != null && aNumFascUnificati.intValue() > 0) {
			lStatement += " Union ";
			lStatement += "(SELECT DISTINCT FSIEP.ANNO_FASCICOLO_UNIONE, FSIEP.CHIAVE_ANNO,"
					+ " FSIEP.CHIAVE_PROGR,";
			lStatement += " FSIEP.CHIAVE_UFFICIO, DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO,"
					+ " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
			lStatement += " DESCR_TIPO_UFF.RV_LOW_VALUE  COD_TIPO_UFFICIO,";
			lStatement += " FSIEP.COD_MOTIVO_ARCHIVIAZIONE, MOTIVO_ARCHIVIAZIONE.RV_MEANING"
					+ " DESCR_MOTIVO_ARCHIVIAZIONE,";
			lStatement += " FSIEP.COD_OPERATORE_AGGIORNAMENTO, FSIEP.COD_OPERATORE_INSERIMENTO,";
			lStatement += " FSIEP.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
			lStatement += " FSIEP.COD_TIPO_POS_LIBERO, TIPO_POS_LIBERO.RV_MEANING DESCR_TIPO_POS_LIBERO, ";
			lStatement += " FSIEP.COD_UFFICIO_AGGIORNAMENTO, FSIEP.COD_UFFICIO_INSERIMENTO,"
					+ " FSIEP.DATA_AGGIORNAMENTO,";
			lStatement += " FSIEP.DATA_ARCHIVIAZIONE, FSIEP.DATA_INSERIMENTO,";
			lStatement += " FSIEP.DATA_ISCRIZIONE, FSIEP.DATA_UNIONE, FSIEP.FAS_SIE_ID_FASCICOLO_SIEP,";
			lStatement += " FSIEP.FLAG_VALIDATO, FSIEP.ID_FASCICOLO_SIEP, FSIEP.LETTERA_FASCICOLO,";
			lStatement += " FSIEP.NOTE NOTE_FASCICOLO, FSIEP.NUM_FASCICOLO_UNIONE, FSIEP.SEN_ID_SENTENZA,"
					+ " FSIEP.SOG_ID_SOGGETTO,";
			lStatement += " FSIEP.FLAG_ALTRA_CAUSA, FSIEP.DATA_IRREVOCABILITA, ";
			lStatement += " FSIEP.FLAG_CUMULANTE, ";
			lStatement += " FSIEP.FLAG_CUMULATO, ";
			// Dario
			lStatement += " FSIEP.COD_UFFICIO_UNIONE, DESCR_TIPO_UFFUNIONE.RV_MEANING"
					+ " DESCR_TIPO_UFFICIO_UNIONE, DESCR_COM_UFFUNIONE.DESCRIZIONE"
					+ " DESCR_COMUNE_UFFICIO_UNIONE, FSIEP.KEY_PROVV_NSC ";
			lStatement += " ,FSIEP.DATA_ARRIVO_ATTO ";
			// Modifica Accorpamento Uffici
			lStatement += " ,FSIEP.CHIAVE_PROGR_ORIG ";
			lStatement += " ,UFFINSERIMENTO.COD_TIPO_UFFICIO COD_TIPO_UFFICIO_INS,"
					+ " DESCR_TIPO_UFFINSERIMENTO.RV_MEANING DESCR_TIPO_UFFICIO_INS,"
					+ " DESCR_COM_UFFINSERIMENTO.DESCRIZIONE DESCR_COMUNE_UFFICIO_INS ";
			lStatement += " ,UFFINSERIMENTO.FLAG_ACCORP FLAG_UFFICIO_ACCORPATO";
			lStatement += " ,FSIEP.VISIBILITA_EX_MINORENNE ";

			// Modifica MEV 12 (Richiesta Certificato Penale)
			// lStatement += " , NULL CERTIFICATO_PENALE ";

			lStatement += " FROM FASCICOLO_SIEP FSIEP, CG_REF_CODES MOTIVO_ARCHIVIAZIONE,";
			lStatement += " CG_REF_CODES STATO_FASCICOLO, CG_REF_CODES TIPO_POS_LIBERO, ";
			lStatement += " FASCICOLO_SIUS FASC,GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FASC2,";
			lStatement += " UFFICIO UFF, CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF, ";
			// Dario
			lStatement += " UFFICIO UFFUNIONE, CG_REF_CODES DESCR_TIPO_UFFUNIONE, COMUNE DESCR_COM_UFFUNIONE";

			// Modifica Accorpamento Uffici
			lStatement += " , UFFICIO UFFINSERIMENTO , CG_REF_CODES DESCR_TIPO_UFFINSERIMENTO,"
					+ " COMUNE DESCR_COM_UFFINSERIMENTO ";

			lStatement += " WHERE FASC.ID_FASCICOLO_SIUS = '" + aIdFascicoloSius + "'";

			// Modifica Accorpamento Uffici
			lStatement += " AND FSIEP.COD_UFFICIO_INSERIMENTO = UFFINSERIMENTO.COD_UFFICIO ";
			lStatement += " AND UFFINSERIMENTO.COD_TIPO_UFFICIO = DESCR_TIPO_UFFINSERIMENTO.RV_LOW_VALUE ";
			lStatement += " AND DESCR_TIPO_UFFINSERIMENTO.RV_DOMAIN = 'TIPO_UFFICIO' ";
			lStatement += " AND UFFINSERIMENTO.COD_COMUNE = DESCR_COM_UFFINSERIMENTO.COD_COMUNE ";

			lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
			lStatement += " AND FASC.ID_FASCICOLO_SIUS = FASC2.FAS_SIU_ID_FASCICOLO_SIUS";
			lStatement += " AND FASC2.FAS_SIE_ID_FASCICOLO_SIEP = FSIEP.ID_FASCICOLO_SIEP";
			lStatement += " AND FASC.FAS_SIE_ID_FASCICOLO_SIEP <> FASC2.FAS_SIE_ID_FASCICOLO_SIEP";
			lStatement += " AND FASC.CHIAVE_UFFICIO = FASC2.CHIAVE_UFFICIO";
			lStatement += " AND (MOTIVO_ARCHIVIAZIONE.RV_DOMAIN = 'MOTIVO_ARCHIVIAZIONE'"
					+ " AND MOTIVO_ARCHIVIAZIONE.RV_LOW_VALUE = FSIEP.COD_MOTIVO_ARCHIVIAZIONE)";
			lStatement += " AND (STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO' AND"
					+ " STATO_FASCICOLO.RV_LOW_VALUE = FSIEP.COD_STATO_FASCICOLO)";
			lStatement += " AND (DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND"
					+ " UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE)";
			lStatement += " AND  FSIEP.CHIAVE_UFFICIO = UFF.COD_UFFICIO";
			lStatement += " AND  UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
			// Dario
			lStatement += " AND (DESCR_TIPO_UFFUNIONE.RV_DOMAIN = 'TIPO_UFFICIO' AND"
					+ " UFFUNIONE.COD_TIPO_UFFICIO = DESCR_TIPO_UFFUNIONE.RV_LOW_VALUE)";
			lStatement += " AND (FSIEP.COD_UFFICIO_UNIONE = UFFUNIONE.COD_UFFICIO)";
			lStatement += " AND (UFFUNIONE.COD_COMUNE = DESCR_COM_UFFUNIONE.COD_COMUNE)";

			lStatement += " AND  TIPO_POS_LIBERO.RV_DOMAIN = 'TIPO_POS_LIBERO' AND"
					+ " TIPO_POS_LIBERO.RV_LOW_VALUE = FSIEP.COD_TIPO_POS_LIBERO) ";
			lStatement += " ORDER BY DESCR_COMUNE_UFFICIO";
		}
		setStatement(lStatement);
	}

	/**
	 * Recupera i fascicoli su cui lanciare il check della pena in base ai parametri passati in input
	 *
	 * @param aChiaveUfficio
	 * @param aIscritto
	 * @param aProgrAnno
	 * @throws DAOException
	 */
	public void ricercaFascicoloPerCheckPena(String aChiaveUfficio, String aIscritto, String aProgrAnno)
			throws DAOException {

		String lStatement = new String();

		lStatement += "SELECT FASC.ANNO_FASCICOLO_UNIONE, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR,";
		lStatement += " FASC.CHIAVE_UFFICIO, ";
		// lStatement +=
		// " DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " null DESCR_TIPO_UFFICIO, null DESCR_COMUNE_UFFICIO,";
		// lStatement += " DESCR_TIPO_UFF.RV_LOW_VALUE COD_TIPO_UFFICIO,";
		lStatement += " null  COD_TIPO_UFFICIO,";
		lStatement += " FASC.COD_MOTIVO_ARCHIVIAZIONE, ";
		// lStatement += " MOTIVO_ARCHIVIAZIONE.RV_MEANING DESCR_MOTIVO_ARCHIVIAZIONE,";
		lStatement += " null DESCR_MOTIVO_ARCHIVIAZIONE,";
		lStatement += " FASC.COD_OPERATORE_AGGIORNAMENTO, FASC.COD_OPERATORE_INSERIMENTO,";
		lStatement += " FASC.COD_STATO_FASCICOLO, ";
		// lStatement += " STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
		lStatement += " null DESCR_STATO_FASCICOLO,";
		lStatement += " FASC.COD_TIPO_POS_LIBERO, ";
		// lStatement += " TIPO_POS_LIBERO.RV_MEANING DESCR_TIPO_POS_LIBERO, ";
		lStatement += " null DESCR_TIPO_POS_LIBERO, ";
		lStatement += " FASC.COD_UFFICIO_AGGIORNAMENTO, FASC.COD_UFFICIO_INSERIMENTO,"
				+ " FASC.DATA_AGGIORNAMENTO,";
		lStatement += " FASC.DATA_ARCHIVIAZIONE, FASC.DATA_INSERIMENTO,";
		lStatement += " FASC.DATA_ISCRIZIONE, FASC.DATA_UNIONE, FASC.FAS_SIE_ID_FASCICOLO_SIEP,";
		lStatement += " FASC.FLAG_VALIDATO, FASC.ID_FASCICOLO_SIEP, FASC.LETTERA_FASCICOLO,";
		lStatement += " FASC.NOTE NOTE_FASCICOLO, FASC.NUM_FASCICOLO_UNIONE, FASC.SEN_ID_SENTENZA,"
				+ " FASC.SOG_ID_SOGGETTO,";
		lStatement += " FASC.FLAG_ALTRA_CAUSA, FASC.DATA_IRREVOCABILITA, ";
		lStatement += " FASC.FLAG_CUMULANTE, ";
		lStatement += " FASC.FLAG_CUMULATO, ";
		lStatement += " FASC.COD_UFFICIO_UNIONE, ";
		lStatement += " null DESCR_TIPO_UFFICIO_UNIONE, null DESCR_COMUNE_UFFICIO_UNIONE,"
				+ " FASC.KEY_PROVV_NSC ";
		lStatement += " ,FASC.DATA_ARRIVO_ATTO ";
		// Modifica Accorpamento Uffici
		lStatement += " ,FASC.CHIAVE_PROGR_ORIG ";
		lStatement += " ,UFFINSERIMENTO.COD_TIPO_UFFICIO COD_TIPO_UFFICIO_INS,"
				+ " DESCR_TIPO_UFFINSERIMENTO.RV_MEANING DESCR_TIPO_UFFICIO_INS,"
				+ " DESCR_COM_UFFINSERIMENTO.DESCRIZIONE DESCR_COMUNE_UFFICIO_INS";
		lStatement += ", UFFINSERIMENTO.FLAG_ACCORP FLAG_UFFICIO_ACCORPATO";
		lStatement += ", FASC.VISIBILITA_EX_MINORENNE ";

		// Modifica MEV 12 (Richiesta Certificato Penale)
		// lStatement += " ,NULL CERTIFICATO_PENALE ";

		lStatement += " FROM FASCICOLO_SIEP FASC ";

		// Modifica Accorpamento Uffici
		lStatement += " LEFT OUTER JOIN UFFICIO UFFINSERIMENTO ON (FASC.Cod_Ufficio_Inserimento"
				+ " = UFFINSERIMENTO.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFFINSERIMENTO ON"
				+ " (UFFINSERIMENTO.COD_TIPO_UFFICIO = DESCR_TIPO_UFFINSERIMENTO.RV_LOW_VALUE AND"
				+ " DESCR_TIPO_UFFINSERIMENTO.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFFINSERIMENTO ON (UFFINSERIMENTO.COD_COMUNE"
				+ " = DESCR_COM_UFFINSERIMENTO.COD_COMUNE)";

		// ==========================================================================
		// CONDIZIONI PER TEST
		// ==========================================================================
		// lStatement += " FROM FASCICOLO_SIEP FASC, EVENTO ";
		// lStatement += " FROM FASCICOLO_SIEP FASC, EVENTO ,annotazione_manuale ";
		// lStatement += " FROM FASCICOLO_SIEP FASC, PENA_RESIDUA ";

		// lStatement += " FROM FASCICOLO_SIEP FASC, PENA_RESIDUA, EVENTO ";

		lStatement += " WHERE FASC.COD_UFFICIO_INSERIMENTO = '" + aChiaveUfficio + "' ";

		lStatement += "   AND FASC.FLAG_VALIDATO = 'S' AND FASC.COD_STATO_FASCICOLO = '03' ";

		// ==========================================================================
		// Cumuli
		// ==========================================================================
		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND evento.cod_motivo in ('0222','0223','0224','0277') ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND evento.COD_OPERATORE_INSERIMENTO like '%res%' ";

		// ==========================================================================
		// Indulti migrati RES
		// ==========================================================================
		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND evento.cod_motivo in ('0161','0284') ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND evento.COD_OPERATORE_INSERIMENTO like '%res%' ";

		// ==========================================================================
		// Interruzioni per indulto migrate RES
		// ==========================================================================
		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND evento.cod_motivo in ('0366') ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND evento.COD_OPERATORE_INSERIMENTO like '%res%' ";

		// ==========================================================================
		// Archiviazione RES
		// ==========================================================================
		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND evento.cod_motivo = '0409' ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND evento.COD_OPERATORE_INSERIMENTO like '%res%' ";

		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement +=
		// " AND evento.cod_motivo in ('0900', '0901', '0902', '0903','0265','0263', '0241', '0264') ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";

		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND evento.cod_motivo in ('0266', '0267', '0270') ";
		// lStatement += " AND evento.cod_motivo = '0270' ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND evento.COD_OPERATORE_INSERIMENTO like '%res%' ";

		// ==========================================================================
		// Computi con quantum positivi
		// ==========================================================================
		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND evento.cod_motivo in ('0121','0212','0213') ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND annotazione_manuale.eve_id_evento = evento.id_evento ";
		// lStatement += " AND annotazione_manuale.FLAG_PIU_MENO = '+' ";
		// lStatement += " AND annotazione_manuale.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";

		// Eventi di forzatura pena
		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND evento.cod_motivo = '0162' ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND annotazione_manuale.eve_id_evento = evento.id_evento ";
		// lStatement += " AND annotazione_manuale.COD_TIPO_ANNOTAZIONE = '003' ";
		// lStatement += " AND annotazione_manuale.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";

		// Annotazioni SIEP rimaste non validate
		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND evento.cod_motivo = '0122' ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND annotazione_manuale.eve_id_evento = evento.id_evento ";
		// lStatement += " AND annotazione_manuale.COD_TIPO_ANNOTAZIONE = '002' ";
		// lStatement += " AND annotazione_manuale.FLAG_VALIDATO = 'N' ";
		// lStatement += " AND annotazione_manuale.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";

		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND cod_tipo_provvedimento = '09' ";
		// lStatement += " AND evento.cod_motivo = '2245' ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND evento.COD_OPERATORE_INSERIMENTO like '%res%' ";

		// ==========================================================================
		// REVOCA LA RES (solo Ordini di scarcerazione)
		// ==========================================================================
		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND evento.cod_motivo = '0028' ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND evento.COD_OPERATORE_INSERIMENTO like '%res%' ";

		// ==========================================================================
		// Richieste al GE annullate
		// ==========================================================================
		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement +=
		// " AND EVENTO.cod_tipo_evento = '01' AND EVENTO.cod_tipo_provvedimento = '26' AND EVENTO.cod_motivo
		// = '0290' ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'A' ";

		// ==========================================================================
		// Nuova pena residua manuale
		// ==========================================================================
		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND evento.cod_motivo = '0925' ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND PENA_RESIDUA.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND PENA_RESIDUA.EVE_ID_EVENTO = EVENTO.id_EVENTO ";
		// lStatement += " AND PENA_RESIDUA.DATA_INIZIO IS NOT NULL ";
		// lStatement += " AND PENA_RESIDUA.FLAG_VALIDATO = 'S' ";

		// ==========================================================================
		// Vecchie pene residue manuali non in decorrenza (ultima inserita)
		// Seleziona i fascicoli NON ARCHIVIATI che hanno l'ultima pena manuale
		// non un decorrenza
		// ==========================================================================
		// lStatement += " AND PENA_RESIDUA.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND PENA_RESIDUA.COD_OPERATORE_INSERIMENTO NOT LIKE '%res%' ";
		// lStatement += " AND PENA_RESIDUA.EVE_ID_EVENTO IS NULL ";
		// lStatement += " AND PENA_RESIDUA.DATA_INIZIO IS NULL ";
		// lStatement += " AND PENA_RESIDUA.DATA_INIZIO IS NOT NULL ";
		// lStatement += " AND PENA_RESIDUA.FLAG_VALIDATO = 'S' ";
		// lStatement +=
		// " AND (PENA_RESIDUA.FAS_SIE_ID_FASCICOLO_SIEP,PENA_RESIDUA.DATA_INSERIMENTO) IN (SELECT
		// PENA_RESIDUA.FAS_SIE_ID_FASCICOLO_SIEP, MAX(PENA_RESIDUA.DATA_INSERIMENTO) ";
		// lStatement +=
		// " FROM PENA_RESIDUA, FASCICOLO_SIEP ";
		// lStatement +=
		// " WHERE FASCICOLO_SIEP.COD_UFFICIO_INSERIMENTO = '"+aChiaveUfficio+"' ";
		// lStatement +=
		// " AND FASCICOLO_SIEP.FLAG_VALIDATO = 'S' ";
		// lStatement +=
		// " AND FASCICOLO_SIEP.COD_STATO_FASCICOLO = '03' ";
		// lStatement +=
		// " AND PENA_RESIDUA.FAS_SIE_ID_FASCICOLO_SIEP = FASCICOLO_SIEP.ID_FASCICOLO_SIEP ";
		// lStatement +=
		// " AND PENA_RESIDUA.COD_OPERATORE_INSERIMENTO NOT LIKE '%res%' ";
		// lStatement +=
		// " AND PENA_RESIDUA.EVE_ID_EVENTO IS NULL ";
		// lStatement +=
		// " AND PENA_RESIDUA.FLAG_VALIDATO = 'S' ";
		// lStatement +=
		// " GROUP BY PENA_RESIDUA.FAS_SIE_ID_FASCICOLO_SIEP ";
		// lStatement +=
		// " ) ";

		// ==========================================================================
		// Codici di differimento
		// ==========================================================================
		// lStatement += " AND evento.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP ";
		// lStatement += " AND evento.cod_motivo in ('0274','0221') ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND evento.COD_OPERATORE_INSERIMENTO like '%res%' ";

		if (aIscritto.equals("Migrati")) {
			lStatement += " AND FASC.COD_OPERATORE_INSERIMENTO like '%res%' ";
		} else if (aIscritto.equals("SIEP")) {
			lStatement += " AND FASC.COD_OPERATORE_INSERIMENTO not like '%res%' ";
		}

		if (aProgrAnno != null) {
			lStatement += " AND FASC.CHIAVE_ANNO in " + aProgrAnno;
		}
		lStatement += " ORDER BY FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR ";

		setStatement(lStatement);
	}

	/**
	 * 20251010 [SG]: paginata la ricerca
	 *
	 * @param aCodMagistrato
	 * @param aCodUfficio
	 * @param aStato
	 * @param aPage
	 * @throws DAOException
	 */
	public void ricercaFascicoloSiepByMagistratoAssegnatario(String aCodMagistrato, String aCodUfficio,
			String[] aStato, int aPage) throws DAOException {

		String lStatement = new String();
		String lPaginedStatement = new String("");

		lStatement += "SELECT FASC.ANNO_FASCICOLO_UNIONE, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR,";
		lStatement += " FASC.CHIAVE_UFFICIO, DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO,"
				+ " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_TIPO_UFF.RV_LOW_VALUE  COD_TIPO_UFFICIO,";
		lStatement += " FASC.COD_MOTIVO_ARCHIVIAZIONE, MOTIVO_ARCHIVIAZIONE.RV_MEANING"
				+ " DESCR_MOTIVO_ARCHIVIAZIONE,";
		lStatement += " FASC.COD_OPERATORE_AGGIORNAMENTO, FASC.COD_OPERATORE_INSERIMENTO,";
		lStatement += " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
		lStatement += " FASC.COD_TIPO_POS_LIBERO, TIPO_POS_LIBERO.RV_MEANING DESCR_TIPO_POS_LIBERO, ";
		lStatement += " FASC.COD_UFFICIO_AGGIORNAMENTO, FASC.COD_UFFICIO_INSERIMENTO,"
				+ " FASC.DATA_AGGIORNAMENTO,";
		lStatement += " FASC.DATA_ARCHIVIAZIONE, FASC.DATA_INSERIMENTO,";
		lStatement += " FASC.DATA_ISCRIZIONE, FASC.DATA_UNIONE, FASC.FAS_SIE_ID_FASCICOLO_SIEP,";
		lStatement += " FASC.FLAG_VALIDATO, FASC.ID_FASCICOLO_SIEP, FASC.LETTERA_FASCICOLO,";
		lStatement += " FASC.NOTE NOTE_FASCICOLO, FASC.NUM_FASCICOLO_UNIONE, FASC.SEN_ID_SENTENZA,"
				+ " FASC.SOG_ID_SOGGETTO,";
		lStatement += " FASC.FLAG_ALTRA_CAUSA, FASC.DATA_IRREVOCABILITA, ";
		lStatement += " FASC.FLAG_CUMULANTE, ";
		lStatement += " FASC.FLAG_CUMULATO, ";
		lStatement += " FASC.COD_UFFICIO_UNIONE, DESCR_TIPO_UFFUNIONE.RV_MEANING DESCR_TIPO_UFFICIO_UNIONE,"
				+ " DESCR_COM_UFFUNIONE.DESCRIZIONE DESCR_COMUNE_UFFICIO_UNIONE, FASC.KEY_PROVV_NSC ";
		lStatement += " ,FASC.DATA_ARRIVO_ATTO ";
		// Modifica Accorpamento Uffici
		lStatement += " ,FASC.CHIAVE_PROGR_ORIG ";
		lStatement += " ,UFFINSERIMENTO.COD_TIPO_UFFICIO COD_TIPO_UFFICIO_INS,"
				+ " DESCR_TIPO_UFFINSERIMENTO.RV_MEANING DESCR_TIPO_UFFICIO_INS,"
				+ " DESCR_COM_UFFINSERIMENTO.DESCRIZIONE DESCR_COMUNE_UFFICIO_INS";
		lStatement += ", UFFINSERIMENTO.FLAG_ACCORP FLAG_UFFICIO_ACCORPATO";
		lStatement += ", FASC.VISIBILITA_EX_MINORENNE ";
		// Modifica MEV 12 (Richiesta Certificato Penale)
		// lStatement += " ,CERTIFICATO_PENALE ";
		lStatement += "FROM FASCICOLO_SIEP FASC";
		// Modifica Accorpamento Uffici
		lStatement += " LEFT OUTER JOIN UFFICIO UFFINSERIMENTO ON (FASC.Cod_Ufficio_Inserimento ="
				+ " UFFINSERIMENTO.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFFINSERIMENTO ON"
				+ " (UFFINSERIMENTO.COD_TIPO_UFFICIO = DESCR_TIPO_UFFINSERIMENTO.RV_LOW_VALUE AND"
				+ " DESCR_TIPO_UFFINSERIMENTO.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFFINSERIMENTO ON (UFFINSERIMENTO.COD_COMUNE ="
				+ " DESCR_COM_UFFINSERIMENTO.COD_COMUNE)";
		lStatement += " LEFT OUTER JOIN UFFICIO UFF ON (FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFF ON (UFF.COD_TIPO_UFFICIO ="
				+ " DESCR_TIPO_UFF.RV_LOW_VALUE AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFF ON (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE)";
		lStatement += " LEFT OUTER JOIN UFFICIO UFFUNIONE ON (FASC.COD_UFFICIO_UNIONE = UFFUNIONE.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFFUNIONE ON (UFFUNIONE.COD_TIPO_UFFICIO ="
				+ " DESCR_TIPO_UFFUNIONE.RV_LOW_VALUE AND DESCR_TIPO_UFFUNIONE.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFFUNIONE ON (UFFUNIONE.COD_COMUNE ="
				+ " DESCR_COM_UFFUNIONE.COD_COMUNE)";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES STATO_FASCICOLO ON (FASC.COD_STATO_FASCICOLO ="
				+ " STATO_FASCICOLO.RV_LOW_VALUE AND STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES MOTIVO_ARCHIVIAZIONE ON (FASC.COD_MOTIVO_ARCHIVIAZIONE ="
				+ " MOTIVO_ARCHIVIAZIONE.RV_LOW_VALUE AND MOTIVO_ARCHIVIAZIONE.RV_DOMAIN ="
				+ " 'MOTIVO_ARCHIVIAZIONE')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES TIPO_POS_LIBERO ON (FASC.COD_TIPO_POS_LIBERO ="
				+ " TIPO_POS_LIBERO.RV_LOW_VALUE AND TIPO_POS_LIBERO.RV_DOMAIN = 'TIPO_POS_LIBERO')";
		lStatement += " , MAGISTRATO_COMPETENTE ";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIEP is not NULL";
		lStatement += "  AND (FASC.CHIAVE_UFFICIO = '" + aCodUfficio + "')";
		lStatement += " AND FASC.ID_FASCICOLO_SIEP = MAGISTRATO_COMPETENTE.FAS_SIE_ID_FASCICOLO_SIEP ";
		lStatement += " AND MAGISTRATO_COMPETENTE.MAG_COD_MAGISTRATO = '" + aCodMagistrato + "'";
		lStatement += " AND MAGISTRATO_COMPETENTE.DATA_FINE is null ";

		String lCondizioni = "";
		if (aStato != null && aStato.length > 0) {
			lCondizioni += " AND FASC.COD_STATO_FASCICOLO IN (";
			for (int i = 0; i < aStato.length; i++) {
				lCondizioni += "'" + aStato[i] + "'";
				if (aStato.length > 1 && i < aStato.length - 1)
					lCondizioni += ",";
			}
			lCondizioni += ")";
		}

		lStatement += lCondizioni;
		lStatement += " order by FASC.CHIAVE_ANNO asc, FASC.CHIAVE_PROGR asc";

		// lStatement += " " + setCondizionePerInserimento(aModel);
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	public void getCountProcedimenti(String lCodMagistrato, String lCodUfficio, String[] lStato) {

		String lStatement = new String();

		lStatement += "SELECT count(*) as HowManyRecords FROM FASCICOLO_SIEP FASC";
		lStatement += " LEFT OUTER JOIN UFFICIO UFFINSERIMENTO ON (FASC.Cod_Ufficio_Inserimento ="
				+ " UFFINSERIMENTO.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFFINSERIMENTO ON"
				+ " (UFFINSERIMENTO.COD_TIPO_UFFICIO = DESCR_TIPO_UFFINSERIMENTO.RV_LOW_VALUE AND"
				+ " DESCR_TIPO_UFFINSERIMENTO.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFFINSERIMENTO ON (UFFINSERIMENTO.COD_COMUNE ="
				+ " DESCR_COM_UFFINSERIMENTO.COD_COMUNE)";
		lStatement += " LEFT OUTER JOIN UFFICIO UFF ON (FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFF ON (UFF.COD_TIPO_UFFICIO ="
				+ " DESCR_TIPO_UFF.RV_LOW_VALUE AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFF ON (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE)";
		lStatement += " LEFT OUTER JOIN UFFICIO UFFUNIONE ON (FASC.COD_UFFICIO_UNIONE = UFFUNIONE.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFFUNIONE ON (UFFUNIONE.COD_TIPO_UFFICIO ="
				+ " DESCR_TIPO_UFFUNIONE.RV_LOW_VALUE AND DESCR_TIPO_UFFUNIONE.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFFUNIONE ON (UFFUNIONE.COD_COMUNE ="
				+ " DESCR_COM_UFFUNIONE.COD_COMUNE)";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES STATO_FASCICOLO ON (FASC.COD_STATO_FASCICOLO ="
				+ " STATO_FASCICOLO.RV_LOW_VALUE AND STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES MOTIVO_ARCHIVIAZIONE ON (FASC.COD_MOTIVO_ARCHIVIAZIONE ="
				+ " MOTIVO_ARCHIVIAZIONE.RV_LOW_VALUE AND MOTIVO_ARCHIVIAZIONE.RV_DOMAIN ="
				+ " 'MOTIVO_ARCHIVIAZIONE')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES TIPO_POS_LIBERO ON (FASC.COD_TIPO_POS_LIBERO ="
				+ " TIPO_POS_LIBERO.RV_LOW_VALUE AND TIPO_POS_LIBERO.RV_DOMAIN = 'TIPO_POS_LIBERO')";
		lStatement += " , MAGISTRATO_COMPETENTE ";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIEP is not NULL";
		lStatement += "  AND (FASC.CHIAVE_UFFICIO = '" + lCodUfficio + "')";
		lStatement += " AND FASC.ID_FASCICOLO_SIEP = MAGISTRATO_COMPETENTE.FAS_SIE_ID_FASCICOLO_SIEP ";
		lStatement += " AND MAGISTRATO_COMPETENTE.MAG_COD_MAGISTRATO = '" + lCodMagistrato + "'";
		lStatement += " AND MAGISTRATO_COMPETENTE.DATA_FINE is null ";
		String lCondizioni = "";
		if (lStato != null && lStato.length > 0) {
			lCondizioni += " AND FASC.COD_STATO_FASCICOLO IN (";
			for (int i = 0; i < lStato.length; i++) {
				lCondizioni += "'" + lStato[i] + "'";
				if (lStato.length > 1 && i < lStato.length - 1)
					lCondizioni += ",";
			}
			lCondizioni += ")";
		}
		lStatement += lCondizioni;
		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca del fascicolo siep per ID Sentenza
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicoloByIDSentenza(BigDecimal aIdSentenza) throws DAOException {
		String lStatement = getFascicoloByIDSentenzaSqlQuery();

		lStatement += " F.SEN_ID_SENTENZA = S.ID_SENTENZA";
		lStatement += " AND S.COD_TIPO_AUTORITA_EMITTENTE = DESCR_AUTORITA_EMIT.RV_LOW_VALUE";
		lStatement += " AND DESCR_AUTORITA_EMIT.RV_DOMAIN = 'TIPO_UFFICIO'";
		lStatement += " AND S.COD_LUOGO_EMITTENTE = DESCR_LUOGO_EMIT.COD_COMUNE";
		lStatement += " AND S.ID_SENTENZA = '" + aIdSentenza + "'";
		setStatement(lStatement);

	}

	protected String getFascicoloByIDSentenzaSqlQuery() {
		String lStatement = new String();

		lStatement += "SELECT F.ID_FASCICOLO_SIEP ID_FASCICOLO_SIEP, F.CHIAVE_ANNO CHIAVE_ANNO,";
		lStatement += " 	    F.CHIAVE_PROGR CHIAVE_PROGR, F.SEN_ID_SENTENZA SEN_ID_SENTENZA,"
				+ " S.DATA_PROVVEDIMENTO DATA_PROVVEDIMENTO, ";
		lStatement += "       DESCR_LUOGO_EMIT.DESCRIZIONE DESCR_LUOGO_EMITTENTE, ";
		lStatement += "       DESCR_AUTORITA_EMIT.RV_MEANING DESCR_AUTORITA_EMITTENTE";
		lStatement += "  FROM FASCICOLO_SIEP F, SENTENZA S,  ";
		lStatement += "       COMUNE DESCR_LUOGO_EMIT, CG_REF_CODES DESCR_AUTORITA_EMIT";
		lStatement += "  WHERE ";

		lStatement += " ";

		return lStatement;
	}

	public GenericModel getModelsPerIdSentenza() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		SentenzaModel sentenza = new SentenzaModel();
		sentenza.setIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));
		sentenza.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		sentenza.setDescrTipoAutoritaEmittente(getString("DESCR_AUTORITA_EMITTENTE"));
		sentenza.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		lFascicolo.setSentenza(sentenza);

		return lFascicolo;
	}

	/**
	 * Ritorna la lunghezza del certificato penale associato ad un Fascicolo
	 *
	 * @param aIdFascicolo
	 *            id Fascicolo
	 * @throws DAOException
	 */
	public void getLengthCertPenaleByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT DBMS_LOB.GETLENGTH(CERTIFICATO_PENALE) LEN_BLOB_CERT_PENALE ";
		lStatement += " FROM FASCICOLO_SIEP WHERE ";
		lStatement += " ID_FASCICOLO_SIEP = " + aIdFascicolo;
		setStatement(lStatement);
	}

	/**
	 * Ritorna il certificato penale associato ad un Fascicolo
	 *
	 * @param aIdFascicolo
	 *            id Fascicolo
	 * @throws DAOException
	 */
	public void getCertificatoPenaleByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT CERTIFICATO_PENALE ";
		lStatement += " FROM FASCICOLO_SIEP WHERE ";
		lStatement += " ID_FASCICOLO_SIEP = " + aIdFascicolo;
		setStatement(lStatement);
	}

	// MEV 15 - Revisione SIGE
	public void ricercaFascicoliDelSoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String lCodDistretto) throws DAOException {

		String lStatement = getFascicoloSqlQuery();
		lStatement += " " + setCondizioneFascBySoggetto(aModel, strCodUfficioUtenteConnesso, lCodDistretto);
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	/**
	 * Setta le condizioni per la Ricerca
	 *
	 * @param aModel
	 * @return
	 */
	private String setCondizioneFascBySoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String lCodDistretto) {

		String lCondizioni = new String();

		if (aModel.getIdSoggetto() != null) {
			lCondizioni += " AND FASC.SOG_ID_SOGGETTO = " + aModel.getIdSoggetto() + "";
		}

		if (lCodDistretto.length() > 1) {
			lCondizioni += " AND FASC.CHIAVE_UFFICIO in (SELECT UFF.COD_UFFICIO FROM UFFICIO UFF"
					+ " WHERE UFF.COD_DISTRETTO ='" + lCodDistretto + "')";
		} else if (lCodDistretto.length() != 1) {
			// Nella ricerca per tutto il DB viene passato lCodDistretto="3"
			lCondizioni += " AND FASC.CHIAVE_UFFICIO = '" + strCodUfficioUtenteConnesso + "'";
		}

		return lCondizioni;
	}

	/**
	 * Esegue la ricerca dei fascicoli siep per Progressivo, Anno e Ufficio ovvero per chiave naturale
	 *
	 * [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: creo nuovo metodo passando anche il controllo su
	 * ufficio minorenne o meno
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicoloByProgrAnnoCodUfficio(FascicoloSiepModel aModel, String majorOffice)
			throws DAOException {

		String lStatement = getFascicoloSqlQuery(majorOffice);

		// 18/11/2015 eliminato il test sui valori <>null. La ricerca va fatta con
		// l'intera chiave sempre. Non ha senso effettuare la ricerca
		// per chiave parziale. Il risultato DEVE essere un SOLO record.
		// if ( (aModel.getChiaveProgr() != null) && (aModel.getChiaveProgr().longValue() > 0) )
		// {
		lStatement += " AND (CHIAVE_PROGR = " + aModel.getChiaveProgr() + ")";
		// }
		// if ( (aModel.getChiaveAnno() != null) && (aModel.getChiaveAnno().intValue() > 0) )
		// {
		lStatement += " AND (CHIAVE_ANNO = " + aModel.getChiaveAnno() + ")";
		// }
		// if ( (aModel.getChiaveUfficio() != null) && (!aModel.getChiaveUfficio().equals("")) )
		// {
		lStatement += " AND (CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "')";
		// }

		// [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: aggiungo la condizione che il soggetto deve
		// essere maggiorenne
		// se l'ufficio mittente della ricerca è un ufficio maggiorenne
		if (StringUtils.checkValidValue(majorOffice)) {
			lStatement += " AND " + ICostantiSoggetto.CONDIZIONE_MAGGIORENNI + " ";
		}

		setStatement(lStatement);
	}

	// MEV 26 CUMULO -step 2 - Ricerca Procedimento By reato
	public void ExRicercaIstruttoriaCumuloByIdFascicoloSiep(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT DISTINCT ISTRU.ID_ISTRUTTORIA_CUMULO";
		lStatement += " FROM FASCICOLO_SIEP FAS, ISTRUTTORIA_CUMULO ISTRU, EVENTO";
		lStatement += " WHERE ";
		lStatement += " EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S'";
		lStatement += " AND ISTRU.EVE_ID_EVENTO_PROV = EVENTO.ID_EVENTO";

		lStatement += " AND EVENTO.DATA_INSERIMENTO = (";
		lStatement += " select max (EVDMAX.DATA_INSERIMENTO)";
		lStatement += " from EVENTO EVDMAX, ISTRUTTORIA_CUMULO ISTR_C";
		lStatement += " where 1=1";
		lStatement += " AND EVDMAX.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND EVDMAX.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		lStatement += " AND ISTR_C.EVE_ID_EVENTO_PROV = EVDMAX.ID_EVENTO)";

		setStatement(lStatement);
	}

	/**
	 * Recupera la lista dei procedimenti collegati al fascicoli SIEP e l'appende come string al campo NOTE
	 * del fascicolo
	 *
	 * @param aIdFascicolo
	 * @return
	 */
	public void ricercaFasCollegatiFascicoloByKey(BigDecimal aIdFascicolo, String tipoRicerca,
			String ufficio) {
		// metodo creato per PLO ANOMALIE SIUS

		String lStatement = new String();

		lStatement += " SELECT listagg( s.CHIAVE_ANNO || '/' || s.CHIAVE_PROGR || ' - ') WITHIN GROUP(ORDER"
				+ " BY S.ID_FASCICOLO_SIEP) collegati ";
		lStatement += "  from fascicolo_siep s ";
		lStatement += " WHERE ";
		if ("I".equals(tipoRicerca)) {
			lStatement += " s.fas_sie_id_fascicolo_siep =  " + aIdFascicolo;
		} else {
			lStatement += " s.ID_FASCICOLO_SIEP  = " + aIdFascicolo;
		}

		lStatement += " AND s.CHIAVE_UFFICIO = " + ufficio;

		setStatement(lStatement);
	}

}