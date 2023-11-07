package siap.sige.udienza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.magistratosezione.model.MagistratoSezioneModel;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.udienza.model.UdienzaSigeModel;

/**
 * <p>
 * Title: UdienzaSigeSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella UdienzaSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 *
 * @version 1.0
 */
public class UdienzaSigeSqlDAO extends SIAPSqlDAO {

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public UdienzaSigeSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountUdienzaSige(UdienzaSigeModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM UDIENZA_SIGE ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagfina
	 * passata in input
	 * 
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaUdienzaSigePaged(UdienzaSigeModel aModel, int aPage) throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		lStatement += " " + getOrderBy() + " ";

		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
		// logger.info("lPaginedStatement = "+lPaginedStatement);
	}

	/*****************************************************************************
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaUdienzaSige(UdienzaSigeModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		// 20171020 [EC] : AGGIIUNGO ORDER BY
		lSql += "  ORDER BY UDI.DATA_INSERIMENTO DESC ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
		// logger.info("lSql = "+lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 * 
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaUdienzaSigeByKey(BigDecimal aIdUdienzaSige) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdUdienzaSige);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 * 
	 * @return
	 ****************************************************************************/
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT DISTINCT " + " UDI.COD_GIUDICE, " + " MAG_GIU.COGNOME AS COGNOME_GIUDICE, "
				+ " MAG_GIU.NOME AS NOME_GIUDICE, " + " UDI.COD_UFFICIO_APPARTENENZA, "
				+ " UDI.DATA_UDIENZA, " + " UDI.ID_UDIENZA_SIGE, " + " UDI.COL_ID_COLLEGIO, "
				+ " COL.COD_COLLEGIO, " + " SEZ.ID_SEZIONE, " + " SEZ.DESCRIZIONE, "
				+ " UDI.COD_PROCURATORE, " + " NVL(MAG_PRO.COGNOME, '-') AS COGNOME_PROCURATORE, "
				+ " NVL(MAG_PRO.NOME, ' ') AS NOME_PROCURATORE, " + " UDI.COD_ID_ASSISTENTE, "
				+ " NVL(ASS_GIU.COGNOME, '-') AS COGNOME_ASSISTENTE, "
				+ " NVL(ASS_GIU.NOME, ' ') AS NOME_ASSISTENTE, " + " UDI.NUMERO_MAX_FASCICOLI, "
				+ " UDI.LUOGO_UDIENZA, " + " UDI.ORA_INIZIO, " + " UDI.MIN_INIZIO, " + " UDI.ORA_FINE, "
				+ " UDI.MIN_FINE, " + " UDI.SEZIONE_UDIENZA, " + " UDI.AULA_UDIENZA, "
				// 20171020 [EC] : AGGIIUNGO IN SELECT , UDI.DATA_INSERIMENTO PERCHE MI SERVE IN ORDER BY
				+ " UDI.DATA_INSERIMENTO ";

		// Aggiungere qui gli eventuali campi descrizioni.

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM UDIENZA_SIGE UDI ";
		lStatement += " LEFT OUTER JOIN ASSISTENTE_GIUDIZIARIO ASS_GIU ON ASS_GIU.ID_ASSISTENTE_GIUDIZIARIO = UDI.COD_ID_ASSISTENTE ";
		lStatement += " LEFT OUTER JOIN MAGISTRATO MAG_GIU ON MAG_GIU.COD_MAGISTRATO = UDI.COD_GIUDICE  AND MAG_GIU.COD_UFFICIO_APPARTENENZA=UDI.COD_UFFICIO_APPARTENENZA ";
		lStatement += " LEFT OUTER JOIN COLLEGIO COL ON COL.ID_COLLEGIO = UDI.COL_ID_COLLEGIO ";
		lStatement += " LEFT OUTER JOIN SEZIONE SEZ ON SEZ.ID_SEZIONE = COL.SEZ_ID_SEZIONE ";
		lStatement += " LEFT OUTER JOIN MAGISTRATO MAG_PRO ON MAG_PRO.COD_MAGISTRATO = UDI.COD_PROCURATORE ";

		// lStatement +=
		// " ( nvl(UDIENZA_MONOCRATICA_SIGE.COD_GIUDICE,'-') = CODGIUDICE.RV_LOW_VALUE AND
		// CODGIUDICE.RV_DOMAIN = 'GIUDICE' ) "
		// lStatement +=
		// " ( nvl(UDIENZA_MONOCRATICA_SIGE.COD_UFFICIO_APPARTENENZA,'-') =
		// CODUFFICIOAPPARTENENZA.RV_LOW_VALUE AND CODUFFICIOAPPARTENENZA.RV_DOMAIN = 'UFFICIO_APPARTENENZA' )
		// "
		// lStatement +=
		// " ( nvl(UDIENZA_MONOCRATICA_SIGE.COD_OPERATORE_INSERIMENTO,'-') =
		// CODOPERATOREINSERIMENTO.RV_LOW_VALUE AND CODOPERATOREINSERIMENTO.RV_DOMAIN =
		// 'OPERATORE_INSERIMENTO' ) "
		// lStatement +=
		// " ( nvl(UDIENZA_MONOCRATICA_SIGE.COD_UFFICIO_INSERIMENTO,'-') = CODUFFICIOINSERIMENTO.RV_LOW_VALUE
		// AND CODUFFICIOINSERIMENTO.RV_DOMAIN = 'UFFICIO_INSERIMENTO' ) "
		// lStatement +=
		// " ( nvl(UDIENZA_MONOCRATICA_SIGE.COD_OPERATORE_AGGIORNAMENTO,'-') =
		// CODOPERATOREAGGIORNAMENTO.RV_LOW_VALUE AND CODOPERATOREAGGIORNAMENTO.RV_DOMAIN =
		// 'OPERATORE_AGGIORNAMENTO' ) "
		// lStatement +=
		// " ( nvl(UDIENZA_MONOCRATICA_SIGE.COD_UFFICIO_AGGIORNAMENTO,'-') =
		// CODUFFICIOAGGIORNAMENTO.RV_LOW_VALUE AND CODUFFICIOAGGIORNAMENTO.RV_DOMAIN =
		// 'UFFICIO_AGGIORNAMENTO' ) "

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		UdienzaSigeModel aModel = new UdienzaSigeModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdUdienzaSige(getBigDecimal("ID_UDIENZA_SIGE"));
		aModel.setDataUdienza(getDate("DATA_UDIENZA"));
		aModel.setColIdCollegio(getBigDecimal("COL_ID_COLLEGIO"));
		aModel.setCodGiudice(getString("COD_GIUDICE"));
		aModel.setDescrGiudice(getString("COGNOME_GIUDICE") + " " + getString("NOME_GIUDICE"));

		aModel.setCodProcuratore(getString("COD_PROCURATORE"));
		aModel.setDescrProcuratore(getString("COGNOME_PROCURATORE") + " " + getString("NOME_PROCURATORE"));

		aModel.setCodIdAssistente(getBigDecimal("COD_ID_ASSISTENTE"));
		aModel.setDescrIdAssistente(getString("COGNOME_ASSISTENTE") + " " + getString("NOME_ASSISTENTE"));

		aModel.setNumeroMaxFascicoli(getBigDecimal("NUMERO_MAX_FASCICOLI"));
		// aModel.setCodOperatoreInserimento ( getString ("COD_OPERATORE_INSERIMENTO" ) );
		// aModel.setDataInserimento ( getDate ("DATA_INSERIMENTO" ) );
		// aModel.setCodUfficioInserimento ( getString ("COD_UFFICIO_INSERIMENTO" ) );
		// aModel.setDescrUfficioInserimento(getString("") );
		// aModel.setCodOperatoreAggiornamento ( getString ("COD_OPERATORE_AGGIORNAMENTO") );
		// aModel.setDataAggiornamento ( getDate ("DATA_AGGIORNAMENTO" ) );
		// aModel.setCodUfficioAggiornamento ( getString ("COD_UFFICIO_AGGIORNAMENTO" ) );
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setLuogoUdienza(getString("LUOGO_UDIENZA"));
		aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
		// aModel.setDescrUfficioAppartenenza(getString("") );
		aModel.setOraInizio(getString("ORA_INIZIO"));
		aModel.setMinInizio(getString("MIN_INIZIO"));
		aModel.setOraFine(getString("ORA_FINE"));
		aModel.setMinFine(getString("MIN_FINE"));
		aModel.setCodIdSezioneUdienza(getBigDecimal("SEZIONE_UDIENZA"));
		aModel.setCodIdAulaUdienza(getBigDecimal("AULA_UDIENZA"));

		if (aModel.getColIdCollegio() != null) {
			aModel.setCollegio(new CollegioModel());
			aModel.getCollegio().setIdCollegio(aModel.getColIdCollegio());
			aModel.getCollegio().setCodCollegio(getString("COD_COLLEGIO"));
			// Dati Sezione.
			aModel.getCollegio().setSezione(new SezioneModel());
			aModel.getCollegio().getSezione().setIdSezione(getBigDecimal("ID_SEZIONE"));
			aModel.getCollegio().getSezione().setDescrizione(getString("DESCRIZIONE"));
		}

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(UdienzaSigeModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdUdienzaSige() != null) {
			lCondizioni += " AND ID_UDIENZA_SIGE = " + aModel.getIdUdienzaSige() + "";
		}

		if (aModel.getDateUdienze() != null) {
			if (aModel.getDateUdienze().length > 0 && aModel.getDateUdienze()[0] != null)
				lCondizioni += " AND DATA_UDIENZA  >= " + " TO_DATE('"
						+ DateUtils.getDateToString(aModel.getDateUdienze()[0], "dd/MM/yyyy")
						+ "', 'DD/MM/YYYY')";

			if (aModel.getDateUdienze().length > 1 && aModel.getDateUdienze()[1] != null)
				lCondizioni += " AND DATA_UDIENZA  <= " + " TO_DATE('"
						+ DateUtils.getDateToString(aModel.getDateUdienze()[1], "dd/MM/yyyy")
						+ "', 'DD/MM/YYYY')";
		}

		if (aModel.getDataUdienza() != null) {
			lCondizioni += " AND DATA_UDIENZA  = " + " TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataUdienza(), "dd/MM/yyyy") + "', 'DD/MM/YYYY')";
		}

		// -- Per Monocratica --//
		// la length è > 1 poichè non considera il "-" come filtro.
		if (aModel.getCodGiudice() != null) {
			lCondizioni += " AND UDI.COL_ID_COLLEGIO IS NULL ";

			if (aModel.getCodGiudice().length() > 1)
				lCondizioni += " AND COD_GIUDICE = '" + aModel.getCodGiudice() + "' ";
		} else
			lCondizioni += " AND UDI.COL_ID_COLLEGIO IS NOT NULL ";

		// [EC] 20171019 : anche le monocratiche possono aver impostatao una sezione
		if (aModel.getCodIdSezioneUdienza() != null
				&& !"0".equals(aModel.getCodIdSezioneUdienza().toString())) {
			lCondizioni += " AND UDI.SEZIONE_UDIENZA = " + aModel.getCodIdSezioneUdienza();
		}

		// -- Per Collegiale --//

		if (aModel.getColIdCollegio() != null
				|| (aModel.getCollegio() != null && aModel.getCollegio().getIdCollegio() != null)) {
			BigDecimal codCollegio = aModel.getColIdCollegio() != null ? aModel.getColIdCollegio()
					: aModel.getCollegio().getIdCollegio();
			lCondizioni += " AND COL_ID_COLLEGIO = " + codCollegio;
		}
		// [EC] - 20171019 aggiungo parametro in più
		if (aModel.getCollegio() != null && aModel.getCollegio().getSezIdSezione() != null) {
			lCondizioni += " AND COL.SEZ_ID_SEZIONE = " + aModel.getCollegio().getSezIdSezione();
		}

		// if (aModel.getCodProcuratore() != null && aModel.getCodProcuratore().length() > 0) {
		// lCondizioni += " AND COD_PROCURATORE = '" + aModel.getCodProcuratore() + "' ";
		// }
		//
		// if (aModel.getCodIdAssistente() != null ) {
		// lCondizioni += " AND COD_ID_ASSISTENTE = " + aModel.getCodIdAssistente() + "";
		// }
		//
		// if (aModel.getNumeroMaxFascicoli() != null ) {
		// lCondizioni += " AND NUMERO_MAX_FASCICOLI = " + aModel.getNumeroMaxFascicoli() + "";
		// }
		//
		// if (aModel.getLuogoUdienza() != null && aModel.getLuogoUdienza().length() > 0) {
		// // [EC] - 20171127 corezione anomalia apostostro segnalata da Padova email Maffucci
		// lCondizioni += " AND LUOGO_UDIENZA = '" + StringUtils.convertSqlString(aModel.getLuogoUdienza()) +
		// "' ";
		// }

		if (aModel.getCodUfficioAppartenenza() != null && aModel.getCodUfficioAppartenenza().length() > 0) {
			lCondizioni += " AND UDI.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza()
					+ "' ";
		}
		// if (aModel.getOraInizio() != null && aModel.getOraInizio().length() > 0) {
		// lCondizioni += " AND ORA_INIZIO = '" + aModel.getOraInizio() + "' ";
		// }
		// if (aModel.getMinInizio() != null && aModel.getMinInizio().length() > 0) {
		// lCondizioni += " AND MIN_INIZIO = '" + aModel.getMinInizio() + "' ";
		// }
		// if (aModel.getOraFine() != null && aModel.getOraFine().length() > 0) {
		// lCondizioni += " AND ORA_FINE = '" + aModel.getOraFine() + "' ";
		// }
		// if (aModel.getMinFine() != null && aModel.getMinFine().length() > 0) {
		// lCondizioni += " AND MIN_FINE = '" + aModel.getMinFine() + "' ";
		// }
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		// logger.info("lCondizioni = "+lCondizioni);
		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di select per chiave
	 * 
	 * @param aKey
	 * @return
	 ****************************************************************************/
	public String setCondizioniByKey(BigDecimal aIdUdienzaSige) {
		String lCondizioni = new String();

		lCondizioni += " AND ID_UDIENZA_SIGE = " + aIdUdienzaSige;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		// logger.info("lCondizioni = "+lCondizioni);
		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sezione order by
	 * 
	 * @return
	 ****************************************************************************/
	protected String getOrderBy() {
		String orderBy = new String("");
		// orderBy = " ORDER BY ";
		return orderBy;
	}

	/**
	 * 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un ufficio
	 * differente da quello in cui ha delle udienze poichè trasferito
	 *
	 * Metodo che imposta lo statement, per recuperare la count dei records legati al Magistrato
	 * aCodMagistrato
	 *
	 * @param aCodMagistrato
	 * @param codUfficioAppartenenza
	 */
	public void countMagUdiCodMagistrato(String aCodMagistrato, String codUfficioAppartenenza) {
		String lStatement = "SELECT COUNT(*) AS COUNT FROM UDIENZA_SIGE WHERE";
		// Ticket#20230427016 - la condizione in OR va tra parentesi
		//lStatement += " COD_PROCURATORE = '" + aCodMagistrato + "'";
		//lStatement += " OR COD_GIUDICE = '" + aCodMagistrato + "'";
		lStatement += " (COD_PROCURATORE = '" + aCodMagistrato + "'";
		lStatement += " OR COD_GIUDICE = '" + aCodMagistrato + "')";
		// Ticket#20230427016 - FINE
		// 20170918: [SG] aggiunta and condition poichè il magistrato può essere inserito da un ufficio
		// differente da quello in cui ha delle udienze poichè trasferito
		lStatement += " AND cod_ufficio_appartenenza = '" + codUfficioAppartenenza + "'";

		setStatement(lStatement);
	}

	/**
	 * 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un ufficio
	 * differente da quello in cui ha delle udienze poichè trasferito
	 *
	 * * 20171012: [EC] aggiunto parametro di passaggio magistratoSezioneModel
	 *
	 * Metodo che imposta lo statement, per recuperare la count dei records legati al Magistrato
	 * aCodMagistrato
	 *
	 * @param aCodMagistrato
	 *            utilizzato per impostare le condizioni di filtro.
	 * @param codUfficioAppartenenza
	 * @param magistratoSezioneModels
	 */
	public void countMagUdiCodMagistratoSezioneValida(String aCodMagistrato, Date dataFineAssegnazione,
			String codUfficioAppartenenza, MagistratoSezioneModel[] magistratoSezioneModels) {
		String lStatement = " SELECT COUNT(*) AS COUNT ";
		lStatement += " FROM UDIENZA_SIGE a, collegio_magistrato b, magistrato_sezione c  WHERE ";
		lStatement += " a.col_id_collegio = b.col_id_collegio ";
		// 20171013: [SG] aggiunta and condition per aggiunta colonna su tabella collegio_magistrato
		lStatement += " and a.id_udienza_sige = b.udi_id_udienza_sige ";
		lStatement += " and b.mag_cod_magistrato = '" + aCodMagistrato + "'";
		lStatement += " and b.mag_cod_magistrato = c.mag_cod_magistrato ";
		lStatement += " and ( c.flg_valido_sn = 'S' OR c.flg_valido_sn is null)";
		// lStatement += " and c.sez_id_sezione = a.sezione_udienza ";
		// lStatement += "and a.data_udienza not between c.data_inizio_ass and '"+dataFineAssegnazione+"' ";
		lStatement += "  and a.data_udienza not between NVL(c.data_inizio_ass,TO_DATE('01/01/1900','DD/MM/YYYY')) and TO_DATE("
				+ DateUtils.getDateToString(dataFineAssegnazione, "yyyyMMdd") + ",'YYYYMMDD' )";
		// 20170918: [SG] aggiunte and conditions poichè il magistrato può essere inserito da un ufficio
		// differente da quello in cui ha delle udienze poichè trasferito
		lStatement += " AND A.cod_ufficio_appartenenza = '" + codUfficioAppartenenza + "'";
		lStatement += " AND b.cod_ufficio_appartenenza = a.cod_ufficio_appartenenza";
		// 20171012: [EC] aggiunte and conditions PER ID SEZIONE (haimè per gli uffici senza sezione arriva un
		// array con un'occorrenza )
		if (magistratoSezioneModels != null && magistratoSezioneModels.length > 0
				&& magistratoSezioneModels[0].getSezIdSezione() != null) {
			String sezIdSezione = "";
			BigDecimal mSezIdSezione = null;
			lStatement += " AND C.SEZ_ID_SEZIONE IN (  ";
			for (int i = 0; i < magistratoSezioneModels.length; i++) {
				mSezIdSezione = magistratoSezioneModels[i].getSezIdSezione();

				if (i != 0 && i < magistratoSezioneModels.length) {
					sezIdSezione += ",";
				}
				sezIdSezione = sezIdSezione + mSezIdSezione.toString();
			}
			lStatement += sezIdSezione + ")";
		}

		setStatement(lStatement);
	}

	/**
	 * Metodo che imposta lo statement, per recuperare la count dei records legati alla Sezione/Aula
	 * <p>
	 *
	 * @param aIdAula
	 *            utilizzato per impostare le condizioni di filtro.
	 * @param aIdSezione
	 *            utilizzato per impostare le condizioni di filtro.
	 */
	public void countUdienzaSezioneAula(BigDecimal aIdAula, BigDecimal aIdSezione) {
		String lStatement = "SELECT COUNT(*) AS COUNT FROM UDIENZA_SIGE WHERE";
		lStatement += " AULA_UDIENZA = " + aIdAula;
		lStatement += " AND SEZIONE_UDIENZA = " + aIdSezione;

		setStatement(lStatement);
	}

	/**
	 * Metodo che imposta lo statement, per recuperare la count dei records legati al Magistrato
	 * aCodMagistrato
	 * <p>
	 *
	 * @param aCodMagistrato
	 *            utilizzato per impostare le condizioni di filtro.
	 */
	public void getIdUdienzaCollegiale(String aCodMagistrato, String dataUdienza,
			String codUfficioAppartenenza) {

		// 20171004: [SG] aggiunta tabella IN JOIN per controllare se le udienze sono state annullate
		String lStatement = "select us.id_udienza_sige from UDIENZA_SIGE us, COLLEGIO_MAGISTRATO cm, udienza_procedimento_sige p where cm.col_id_collegio = us.col_id_collegio";
		lStatement += " and cm.mag_cod_magistrato = '" + aCodMagistrato + "' ";
		lStatement += " and to_char(us.data_udienza,'dd/MM/yyyy') = '" + dataUdienza + "' ";
		lStatement += " and p.udi_id_udienza_sige = us.id_udienza_sige and p.flag_rinviata <> 'A'";
		lStatement += " and us.id_udienza_sige = cm.udi_id_udienza_sige";
		// qui va aggiunta la condizione su us.cod_ufficio_appartenenza =
		lStatement += " and us.cod_ufficio_appartenenza = '" + codUfficioAppartenenza + "' ";
		//
		lStatement += " order by cm.data_inserimento desc ";

		setStatement(lStatement);
	}

	/**
	 * Metodo che imposta lo statement, per recuperare la count dei records legati al Magistrato
	 * aCodMagistrato
	 * <p>
	 *
	 * @param aCodMagistrato
	 *            utilizzato per impostare le condizioni di filtro.
	 */
	public void getIdUdienzaMonocratica(String aCodMagistrato, String dataUdienza, BigDecimal sez,
			String ufficioAppartenenza) {
		String lStatement = "select us.id_udienza_sige from UDIENZA_SIGE us";
		lStatement += " where us.cod_giudice = '" + aCodMagistrato + "' ";
		lStatement += " and to_char(us.data_udienza,'dd/MM/yyyy') = '" + dataUdienza + "' ";
		// [EC] 20171019 aggiungo la sezione nella query se è valorizzata
		if (Utils.isPresent(sez))
			lStatement += " and sezione_udienza = '" + sez + "' ";

		// [EC] 20190325: AGGIUNGO IL PARAMETR COD_UFFICIO IN INPUT (L'UDIENZA DEVE ESSERE UNIVOCA PER
		// UFFICIO)
		lStatement += " and us.cod_ufficio_appartenenza  = '" + ufficioAppartenenza + "' ";
		lStatement += " order by us.data_inserimento desc ";

		setStatement(lStatement);
	}

	/**
	 * 20170913: [SG] aggiunta query
	 *
	 * @param dataUdienza
	 * @param codUfficioAppartenenza
	 * @param codMagis
	 * @param idSezione
	 * @param tipoRito
	 */
	public void cercaCollegi(Date dataUdienza, String codUfficioAppartenenza, String codMagis,
			BigDecimal idSezione, String tipoRito, String idCollegio) {

		// QUERY
		String lSql = "SELECT DISTINCT UDI.COD_GIUDICE," + " UDI.COD_UFFICIO_APPARTENENZA,"
				+ " NVL(MAG_GIU.COGNOME, '-') AS COGNOME_GIUDICE,"
				+ " NVL(MAG_GIU.NOME, ' ') AS NOME_GIUDICE," + " UDI.DATA_UDIENZA," + " UDI.ID_UDIENZA_SIGE,"
				+ " UDI.COL_ID_COLLEGIO,"
				// 20171127: [EC] elimino la funzione min over (partition by
				// + " min(COLL.COD_COLLEGIO) over (partition by COD_GIUDICE) COD_COLLEGIO,"
				+ " COLL.COD_COLLEGIO," + " SEZ.ID_SEZIONE," + " NVL(SEZ.DESCRIZIONE, ' ') AS DESCRIZIONE,"
				+ " UDI.COD_PROCURATORE," + " NVL(MAG_PRO.COGNOME, '-') AS COGNOME_PROCURATORE,"
				+ " NVL(MAG_PRO.NOME, ' ') AS NOME_PROCURATORE," + " UDI.COD_ID_ASSISTENTE,"
				+ " NVL(ASS_GIU.COGNOME, '-') AS COGNOME_ASSISTENTE,"
				+ " NVL(ASS_GIU.NOME, ' ') AS NOME_ASSISTENTE," + " UDI.NUMERO_MAX_FASCICOLI,"
				+ " UDI.LUOGO_UDIENZA," + " UDI.ORA_INIZIO," + " UDI.MIN_INIZIO," + " UDI.ORA_FINE,"
				+ " UDI.MIN_FINE," + " UDI.SEZIONE_UDIENZA," + " UDI.AULA_UDIENZA,"
				+ " NVL(SEZ.CODICE, ' ') AS CODICE_SEZIONE" + "  FROM UDIENZA_SIGE UDI"
				+ "  LEFT OUTER JOIN ASSISTENTE_GIUDIZIARIO ASS_GIU"
				+ "    ON ASS_GIU.ID_ASSISTENTE_GIUDIZIARIO = UDI.COD_ID_ASSISTENTE"
				+ "  LEFT OUTER JOIN MAGISTRATO MAG_GIU" + "    ON MAG_GIU.COD_MAGISTRATO = UDI.COD_GIUDICE"
				+ "   AND MAG_GIU.COD_UFFICIO_APPARTENENZA = UDI.COD_UFFICIO_APPARTENENZA"
				+ "  LEFT OUTER JOIN MAGISTRATO MAG_PRO"
				+ "    ON MAG_PRO.COD_MAGISTRATO = UDI.COD_PROCURATORE" + "  LEFT OUTER JOIN COLLEGIO COLL"
				+ "    ON COLL.ID_COLLEGIO = UDI.COL_ID_COLLEGIO" + "  LEFT OUTER JOIN SEZIONE SEZ"
				+ "    ON SEZ.ID_SEZIONE = COLL.SEZ_ID_SEZIONE" + " INNER JOIN UDIENZA_PROCEDIMENTO_SIGE UP"
				+ "    ON UDI.ID_UDIENZA_SIGE = UP.UDI_ID_UDIENZA_SIGE" + " WHERE DATA_UDIENZA = TO_DATE('"
				+ DateUtils.getDateToString(dataUdienza, "dd/MM/yyyy") + "', 'DD/MM/YYYY')";
		if (!"M".equals(tipoRito)) {
			lSql += "   AND COL_ID_COLLEGIO IS NOT NULL";

			if (idCollegio != null)
				lSql += "   AND coll.cod_collegio='" + idCollegio + "'";
		}

		lSql += "   AND UDI.COD_UFFICIO_APPARTENENZA = '" + codUfficioAppartenenza + "'"
				+ "   AND up.flag_rinviata <> 'A'";

		// 20170918: [ec] aggiungo le condizioni solo se presenti i dati
		if (Utils.isPresent(codMagis) && !codMagis.equals("null"))
			lSql += "   AND UDI.COD_GIUDICE = '" + codMagis + "'";
		// 20171127: [EC] gestisco la differenza tra Monocratico e Collegiale
		if ("C".equals(tipoRito)) {
			if (Utils.isPresent(idSezione))
				lSql += "  AND nvl(SEZ.ID_SEZIONE, 0) = '" + idSezione + "'";
			else
				lSql += "  AND SEZ.ID_SEZIONE IS NULL ";
		} else if ("M".equals(tipoRito)) {
			if (Utils.isPresent(idSezione))
				lSql += "  AND nvl(UDI.SEZIONE_UDIENZA, 0) = '" + idSezione + "'";
		}

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/**
	 * 20170914: [SG] aggiunto metodo di ricerca
	 *
	 * @param codMagis
	 * @param dataUdienza
	 * @param idSezione
	 * @param idProcuratore
	 * @param idAssistente
	 * @param modalita
	 */
	public void getIdUdienzaCollegiale(String codMagis, String dataUdienza, BigDecimal idSezione,
			BigDecimal idAssistente, String idProcuratore, String modalita, String codUfficioAppartenenza) {

		// 20171122 [EC] aggiungo la join con la tabella magistrato
		String lStatement = "select us.id_udienza_sige from UDIENZA_SIGE us, COLLEGIO_MAGISTRATO cm, "
				+ "magistrato m  where cm.col_id_collegio = us.col_id_collegio";
		lStatement += " and cm.mag_cod_magistrato = '" + codMagis + "' ";
		// 20171013: [SG] aggiunta condizione dopo alter table in cm
		lStatement += " and us.id_udienza_sige = cm.udi_id_udienza_sige";
		lStatement += " and to_char(us.data_udienza,'dd/MM/yyyy') = '" + dataUdienza + "' ";

		// 20171122 [EC] aggiungo le condizioni di join con la tabella magistrato
		lStatement += " and cm.mag_cod_magistrato=m.cod_magistrato ";
		lStatement += " and m.cod_ufficio_appartenenza  = '" + codUfficioAppartenenza + "' ";
		lStatement += " and us.cod_ufficio_appartenenza  = '" + codUfficioAppartenenza + "' ";

		if (Utils.isPresent(idSezione) && idSezione.intValue() != 0)
			lStatement += " and us.sezione_udienza = '" + idSezione.toString() + "' ";
		if (Utils.isPresent(idAssistente) && idAssistente.intValue() != 0)
			lStatement += " and us.cod_id_assistente = '" + idAssistente.toString() + "' ";
		if ("I".equals(modalita)) {
			if (Utils.isPresent(idProcuratore))
				lStatement += " and us.cod_procuratore = '" + idProcuratore + "' ";
		} else
			lStatement += " and us.cod_procuratore = '" + idProcuratore + "' ";

		lStatement += " order by cm.data_inserimento desc ";

		setStatement(lStatement);
	}

	// 20170919: [SG] aggiunta query per controllo preventivo
	public void controllaMagistratoAttivoAltriUffici(String codMagistrato, String codUfficioAppartenenza) {

		String lStatement = "select ud.descr_tipo_ufficio|| ' di ' || ud.descr_comune as descrUfficio"
				+ " from magistrato m, ufficio_descr ud  where m.cod_magistrato = '" + codMagistrato + "'"
				+ " and m.cod_ufficio_appartenenza <> '" + codUfficioAppartenenza + "'"
				+ " and m.data_fine_validita is null" + " and m.cod_ufficio_appartenenza=ud.cod_ufficio";

		// String lStatement = "select count(*) AS COUNT" + " from magistrato m"
		// + " where m.cod_magistrato = '" + codMagistrato + "'"
		// + " and m.cod_ufficio_appartenenza <> '" + codUfficioAppartenenza + "'"
		// + " and m.data_fine_validita is null";

		setStatement(lStatement);
	}

	/**
	 * Metodo aggiunto a seguito richieste per 11.2.1
	 *
	 * @return
	 * @throws DAOException
	 */
	public void ricercaUdienzaSigePerFunzioniSupporto(UdienzaSigeModel aModel) throws DAOException {

		// Recupera la select...from
		String lSql = getSqlQueryPerFunzioniSupporto();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		lSql += " AND ( MA.Flag_Modif_In_Blocco='B' or MA.Flag_Modif_In_Blocco is null OR MA.Flag_Modif_In_Blocco='P' ) ";

		// intervento 11.2.1
		// devo estrarre solo udienze relative a fascicoli NON DEFINITI, QUINDI AGGIUNGO LA SEGUENTE
		// CONDIZIONE:
		lSql += "  AND ( F.COD_STATO_FASCICOLO IN ('02','05','10','14','18','20','21')  or F.COD_STATO_FASCICOLO is null) ";

		// 20171020 [EC] : AGGIIUNGO ORDER BY
		lSql += "  ORDER BY UDI.DATA_INSERIMENTO DESC ";

		// Imposta lo statement da eseguire
		setStatement(lSql);

	}

	/**
	 * Metodo aggiunto a seguito richieste per 11.2.1
	 *
	 * @return
	 * @throws DAOException
	 */
	protected String getSqlQueryPerFunzioniSupporto() {
		String lStatement = new String("");

		lStatement += " SELECT DISTINCT " + " UDI.COD_GIUDICE, " + " MAG_GIU.COGNOME AS COGNOME_GIUDICE, "
				+ " MAG_GIU.NOME AS NOME_GIUDICE, " + " UDI.COD_UFFICIO_APPARTENENZA, "
				+ " UDI.DATA_UDIENZA, " + " UDI.ID_UDIENZA_SIGE, " + " UDI.COL_ID_COLLEGIO, "
				+ " COL.COD_COLLEGIO, " + " SEZ.ID_SEZIONE, " + " SEZ.DESCRIZIONE, "
				+ " UDI.COD_PROCURATORE, " + " NVL(MAG_PRO.COGNOME, '-') AS COGNOME_PROCURATORE, "
				+ " NVL(MAG_PRO.NOME, ' ') AS NOME_PROCURATORE, " + " UDI.COD_ID_ASSISTENTE, "
				+ " NVL(ASS_GIU.COGNOME, '-') AS COGNOME_ASSISTENTE, "
				+ " NVL(ASS_GIU.NOME, ' ') AS NOME_ASSISTENTE, " + " UDI.NUMERO_MAX_FASCICOLI, "
				+ " UDI.LUOGO_UDIENZA, " + " UDI.ORA_INIZIO, " + " UDI.MIN_INIZIO, " + " UDI.ORA_FINE, "
				+ " UDI.MIN_FINE, " + " UDI.SEZIONE_UDIENZA, " + " UDI.AULA_UDIENZA, "
				+ " UDI.DATA_INSERIMENTO, MAG_GIU_ASS.COGNOME AS COGNOME_MAG_ASS,  MAG_GIU_ASS.NOME AS NOME_MAG_ASS "
				+ "" + "" + ", MAG_GIU_ASS.COD_MAGISTRATO  COD_MAG_ASS";

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM UDIENZA_SIGE UDI ";
		lStatement += " LEFT OUTER JOIN ASSISTENTE_GIUDIZIARIO ASS_GIU ON ASS_GIU.ID_ASSISTENTE_GIUDIZIARIO = UDI.COD_ID_ASSISTENTE ";
		lStatement += " LEFT OUTER JOIN MAGISTRATO MAG_GIU ON MAG_GIU.COD_MAGISTRATO = UDI.COD_GIUDICE  AND MAG_GIU.COD_UFFICIO_APPARTENENZA=UDI.COD_UFFICIO_APPARTENENZA ";
		lStatement += " LEFT OUTER JOIN COLLEGIO COL ON COL.ID_COLLEGIO = UDI.COL_ID_COLLEGIO ";
		lStatement += " LEFT OUTER JOIN SEZIONE SEZ ON SEZ.ID_SEZIONE = COL.SEZ_ID_SEZIONE ";
		lStatement += " LEFT OUTER JOIN MAGISTRATO MAG_PRO ON MAG_PRO.COD_MAGISTRATO = UDI.COD_PROCURATORE ";

		lStatement += " LEFT OUTER JOIN UDIENZA_PROCEDIMENTO_SIGE UP  ON UDI.ID_UDIENZA_SIGE = UP.UDI_ID_UDIENZA_SIGE";

		lStatement += " LEFT OUTER JOIN PROVVEDIMENTO_SIGE PS  ON UDI.ID_UDIENZA_SIGE = PS.UDI_ID_UDIENZA_SIGE ";

		lStatement += "  LEFT OUTER JOIN MAGISTRATO_ASSEGNATARIO MA"
				+ " ON ( UP.FAS_ID_FASCICOLO_SIGE = MA.FAS_SIGE_ID_FASCICOLO_SIGE OR MA.FAS_SIGE_ID_FASCICOLO_SIGE = PS.FAS_ID_FASCICOLO_SIGE) AND MA.DATA_FINE IS NULL"
				+ " LEFT OUTER JOIN MAGISTRATO MAG_GIU_ASS  ON MAG_GIU_ASS.COD_MAGISTRATO = MA.MAG_COD_MAGISTRATO  "
				+ " AND MAG_GIU_ASS.COD_UFFICIO_APPARTENENZA = MA.COD_UFFICIO_INSERIMENTO    AND MA.DATA_FINE IS NULL "
				+ " AND MA.Flag_Modif_In_Blocco IN('P', 'B')";

		// intervento 11.2.1
		// devo estrarre solo udienze relative a fascicoli NON DEFINITI, QUINDI AGGIUNGO LE SEGUENTI JOIN:
		// lStatement += " LEFT OUTER JOIN UDIENZA_PROCEDIMENTO_SIGE UP"
		// + " ON UP.UDI_ID_UDIENZA_SIGE = UDI.ID_UDIENZA_SIGE";

		lStatement += "  LEFT OUTER JOIN FASCICOLO_SIGE F   ON F.ID_FASCICOLO_SIGE = UP.FAS_ID_FASCICOLO_SIGE";

		return lStatement;
	}

	/**
	 * Metodo aggiunto a seguito richieste per 11.2.1
	 *
	 * @return
	 * @throws DAOException
	 */
	public GenericModel getExtendModel() throws DAOException {
		UdienzaSigeModel aModel = (UdienzaSigeModel) this.getModel();
		// aggiungo per 11.2.1
		aModel.setDescrMagistratoAss(getString("COGNOME_MAG_ASS") != null ? getString("COGNOME_MAG_ASS")
				: "" + " " + getString("NOME_MAG_ASS") != null ? getString("NOME_MAG_ASS") : "");
		aModel.setCodMagistratoAss(getString("COD_MAG_ASS") != null ? getString("COD_MAG_ASS") : "");
		return aModel;
	}

	/**
	 * Metodo aggiunto a seguito richieste per 11.2.1
	 *
	 * @return
	 * @throws DAOException
	 */
	public GenericModel getNewExtendModel() throws DAOException {
		// UdienzaSigeModel aModel = (UdienzaSigeModel)this.getModel();
		UdienzaSigeModel aModel = new UdienzaSigeModel();

		// aggiungo per 11.2.1
		aModel.setDataUdienza(getDate("DATA_UDIENZA"));
		aModel.setDescrGiudice(getString("COGNOME_GIUDICE") + " " + getString("NOME_GIUDICE"));
		aModel.setDescrProcuratore(getString("COGNOME_PROCURATORE"));
		aModel.setDescrIdAssistente(getString("COGNOME_ASSISTENTE"));
		aModel.setLuogoUdienza(getString("LUOGO_UDIENZA"));
		aModel.setCodMagistratoAss(getString("COD_MAG_ASS") != null ? getString("COD_MAG_ASS") : "");

		aModel.setListaIdUdienze(getString("ID_UDIENZE") != null ? getString("ID_UDIENZE") : "");
		aModel.setNumeroUdienze(getBigDecimal("NUM_UDIENZE"));
		aModel.setColIdCollegio(getBigDecimal("COL_ID_COLLEGIO"));
		return aModel;
	}

	/**
	 * Metodo aggiunto a seguito richieste per 11.2.1
	 *
	 * @return
	 * @throws DAOException
	 */
	public GenericModel getModelCollegiali() throws DAOException {

		UdienzaSigeModel aModel = new UdienzaSigeModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdUdienzaSige(getBigDecimal("ID_UDIENZA_SIGE"));
		aModel.setDataUdienza(getDate("DATA_UDIENZA"));
		aModel.setColIdCollegio(getBigDecimal("COL_ID_COLLEGIO"));
		aModel.setCodGiudice(getString("COD_GIUDICE"));
		aModel.setDescrGiudice(getString("COGNOME_GIUDICE") + " " + getString("NOME_GIUDICE"));

		aModel.setCodProcuratore(getString("COD_PROCURATORE"));
		aModel.setDescrProcuratore(getString("COGNOME_PROCURATORE") + " " + getString("NOME_PROCURATORE"));

		aModel.setCodIdAssistente(getBigDecimal("COD_ID_ASSISTENTE"));
		aModel.setDescrIdAssistente(getString("COGNOME_ASSISTENTE") + " " + getString("NOME_ASSISTENTE"));

		aModel.setNumeroMaxFascicoli(getBigDecimal("NUMERO_MAX_FASCICOLI"));

		aModel.setLuogoUdienza(getString("LUOGO_UDIENZA"));
		aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));

		aModel.setOraInizio(getString("ORA_INIZIO"));
		aModel.setMinInizio(getString("MIN_INIZIO"));
		aModel.setOraFine(getString("ORA_FINE"));
		aModel.setMinFine(getString("MIN_FINE"));
		aModel.setCodIdSezioneUdienza(getBigDecimal("SEZIONE_UDIENZA"));
		aModel.setCodIdAulaUdienza(getBigDecimal("AULA_UDIENZA"));

		aModel.setListaIdUdienze(getString("ID_UDIENZE") != null ? getString("ID_UDIENZE") : "");
		aModel.setNumeroUdienze(getBigDecimal("NUM_UDIENZE"));

		if (aModel.getColIdCollegio() != null) {
			aModel.setCollegio(new CollegioModel());
			aModel.getCollegio().setIdCollegio(aModel.getColIdCollegio());
			aModel.getCollegio().setCodCollegio(getString("COD_COLLEGIO"));
			// Dati Sezione.
			aModel.getCollegio().setSezione(new SezioneModel());
			aModel.getCollegio().getSezione().setIdSezione(getBigDecimal("ID_SEZIONE"));
			aModel.getCollegio().getSezione().setDescrizione(getString("DESCRIZIONE"));
		}

		return aModel;
	}

	/**
	 * Metodo di ricerca di un udienza collegiale
	 *
	 * introdotto per nterventi 11.2.1
	 *
	 * @param udi
	 * @param codUfficioAppartenenza
	 */
	public void ricercaUdienzaCollegiale(UdienzaSigeModel lUdienzaSige, String codUfficioAppartenenza,
			String provenienza) {

		String lStatement = " select distinct us.id_udienza_sige, us.data_udienza, sez.id_sezione, sez.descrizione, us.ora_inizio,"
				+ " us.min_inizio, us.ora_fine, us.min_fine,  us.col_id_collegio, col.cod_collegio, col.mag_cod_magistrato "
				+ " from UDIENZA_SIGE us, COLLEGIO_MAGISTRATO cm, "
				+ " magistrato m, collegio col, sezione sez "
				// intervento 11.2.1
				// devo estrarre solo udienze relative a fascicoli NON DEFINITI, QUINDI AGGIUNGO LE SEGUENTI
				// TABELLE IN SELECT :
				+ " , UDIENZA_PROCEDIMENTO_SIGE UP,  FASCICOLO_SIGE F"
				+ " where us.col_id_collegio = cm.col_id_collegio(+) ";

		// per la ricerca proveniente da Funzioni Amministrative (FA) non occorre mettere il filtro su
		// collegio magigrato
		if (lUdienzaSige.getCollegio() != null && lUdienzaSige.getCollegio().getCollegioMagistrati() != null
				&& !"FA".equals(provenienza) && provenienza != null) {
			String codMagis = lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagCodMagistrato();
			if (codMagis != null && !"".equals(codMagis))
				lStatement += " and cm.mag_cod_magistrato = '" + codMagis + "' ";
		}

		lStatement += " and us.id_udienza_sige = cm.udi_id_udienza_sige (+)";

		if (lUdienzaSige.getDateUdienze() != null) {
			if (lUdienzaSige.getDateUdienze().length > 0 && lUdienzaSige.getDateUdienze()[0] != null)
				lStatement += " AND us.data_udienza >= " + " TO_DATE('"
						+ DateUtils.getDateToString(lUdienzaSige.getDateUdienze()[0], "dd/MM/yyyy")
						+ "', 'DD/MM/YYYY')";

			if (lUdienzaSige.getDateUdienze().length > 1 && lUdienzaSige.getDateUdienze()[1] != null)
				lStatement += " AND us.data_udienza  <= " + " TO_DATE('"
						+ DateUtils.getDateToString(lUdienzaSige.getDateUdienze()[1], "dd/MM/yyyy")
						+ "', 'DD/MM/YYYY')";
		} else if (lUdienzaSige.getDataUdienza() != null) {
			lStatement += " AND us.data_udienza = " + " TO_DATE('"
					+ DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "dd/MM/yyyy")
					+ "', 'DD/MM/YYYY')";
		}

		if (!"M".equals(provenienza) && provenienza != null) {
			if (lUdienzaSige.getCollegio() != null
					&& lUdienzaSige.getCollegio().getMagCodMagistrato() != null) {
				String codMagisCollegio = lUdienzaSige.getCollegio().getMagCodMagistrato();

				if (codMagisCollegio != null && !"".equals(codMagisCollegio))
					lStatement += " and col.mag_cod_magistrato = '" + codMagisCollegio + "' ";

			}
		}

		if (lUdienzaSige.getCollegio() != null && lUdienzaSige.getCollegio().getIdCollegio() != null) {
			lStatement += " and col.id_collegio = '" + lUdienzaSige.getCollegio().getIdCollegio() + "' ";
		}

		// aggiungo le condizioni di join con la tabella magistrato
		lStatement += " and m.cod_magistrato = cm.mag_cod_magistrato(+) ";
		lStatement += " and m.cod_ufficio_appartenenza  = '" + codUfficioAppartenenza + "' ";
		lStatement += " and us.cod_ufficio_appartenenza  = '" + codUfficioAppartenenza + "' ";

		if (lUdienzaSige.getCollegio() != null && lUdienzaSige.getCollegio().getSezIdSezione() != null) {
			lStatement += " and us.sezione_udienza = '"
					+ lUdienzaSige.getCollegio().getSezIdSezione().toString() + "' ";
		}

		lStatement += " and COL.ID_COLLEGIO = us.COL_ID_COLLEGIO and COL.SEZ_ID_SEZIONE = SEZ.ID_SEZIONE(+)";

		// intervento 11.2.1
		// devo estrarre solo udienze relative a fascicoli NON DEFINITI, QUINDI AGGIUNGO LE SEGUENTI JOIN:
		lStatement += "  AND US.ID_UDIENZA_SIGE = UP.UDI_ID_UDIENZA_SIGE(+)  AND UP.FAS_ID_FASCICOLO_SIGE = F.ID_FASCICOLO_SIGE(+)"
				+ "   AND ( F.COD_STATO_FASCICOLO IN ('02','05','10','14','18','20','21')  or F.COD_STATO_FASCICOLO is null) ";

		lStatement += " order by  us.id_udienza_sige desc ";

		setStatement(lStatement);
	}

	/**
	 * @return
	 * @throws DAOException
	 */
	public GenericModel getModelUdienzaCollegiale() throws DAOException {

		UdienzaSigeModel aModel = new UdienzaSigeModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdUdienzaSige(getBigDecimal("ID_UDIENZA_SIGE"));
		aModel.setDataUdienza(getDate("DATA_UDIENZA"));
		aModel.setColIdCollegio(getBigDecimal("COL_ID_COLLEGIO"));

		aModel.setOraInizio(getString("ORA_INIZIO"));
		aModel.setMinInizio(getString("MIN_INIZIO"));
		aModel.setOraFine(getString("ORA_FINE"));
		aModel.setMinFine(getString("MIN_FINE"));

		if (aModel.getColIdCollegio() != null) {
			aModel.setCollegio(new CollegioModel());
			aModel.getCollegio().setIdCollegio(aModel.getColIdCollegio());
			aModel.getCollegio().setCodCollegio(getString("cod_collegio"));
			aModel.getCollegio().setMagCodMagistrato(getString("mag_cod_magistrato"));
			// Dati Sezione.
			aModel.getCollegio().setSezione(new SezioneModel());
			aModel.getCollegio().getSezione().setIdSezione(getBigDecimal("ID_SEZIONE"));
			aModel.getCollegio().getSezione().setDescrizione(getString("DESCRIZIONE"));
		}

		return aModel;
	}

}