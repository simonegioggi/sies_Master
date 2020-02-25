package siap.sige.udienza.dao;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.util.DateUtils;
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
 * Company:
 * </p>
 *
 * @version 1.0
 */
public class UdienzaSigeRuoloSqlDAO extends UdienzaSigeSqlDAO {

	/**
	 * Costruttore
	 *
	 * @param con
	 */
	public UdienzaSigeRuoloSqlDAO(Connection con) {
		super(con);
	}

	/**
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaUdienzaSigePerRuolo(UdienzaSigeModel aModel, String aTipoRito) throws DAOException {
		// Recupera la select...from
		// String lSql = getSqlQuery();

		// intervento per nuova gestione udienze monocratiche/collegiali per SIES 11.2.1
		String lSql = getSqlQueryPerRuolo();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel, aTipoRito);

		// if (aTipoRito.trim() == "C")
		// lCondizioni+= "AND COL_ID_COLLEGIO IS NOT NULL ";
		if (aTipoRito.compareTo("M") == 0)
			lCondizioni += "AND COL_ID_COLLEGIO IS NULL ";
		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		lSql += "  AND MASS.DATA_FINE IS NULL ";

		if (aModel.getCodMagistratoAss() != null) {
			lSql += "AND MASS.MAG_COD_MAGISTRATO='" + aModel.getCodMagistratoAss() + "'";
		}

		lSql += " " + getOrderByDataAsc() + " ";

		/// INZIO PROVAAAAAAA EMMMMA
		lSql += " ) GROUP BY COD_MAG_ASS, DATA_UDIENZA, COGNOME_GIUDICE,  NOME_GIUDICE,"
				+ " COGNOME_PROCURATORE, COGNOME_ASSISTENTE, LUOGO_UDIENZA, COL_ID_COLLEGIO ORDER BY DATA_UDIENZA ASC";
		// FINE PROVAAAAAAA EMMMMA

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/**
	 * Metodo per la costruzione della sql query
	 *
	 * @return
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		// 20170913: [SG] modificata la query
		// lStatement += " SELECT DISTINCT " +
		// " UDI.COD_GIUDICE, "+
		// " NVL(MAG_GIU.COGNOME, '-') AS COGNOME_GIUDICE, " +
		// " NVL(MAG_GIU.NOME, ' ') AS NOME_GIUDICE, " +
		// " UDI.COD_UFFICIO_APPARTENENZA, "+
		// " UDI.DATA_UDIENZA, " +
		// " UDI.ID_UDIENZA_SIGE, " +
		// " UDI.COL_ID_COLLEGIO, "+
		// " NVL(COLL.COD_COLLEGIO, ' ') AS COD_COLLEGIO, " +
		// " SEZ.ID_SEZIONE, " +
		// " NVL(SEZ.DESCRIZIONE, ' ') AS DESCRIZIONE, " +
		// " UDI.COD_PROCURATORE, " +
		// " NVL(MAG_PRO.COGNOME, '-') AS COGNOME_PROCURATORE, " +
		// " NVL(MAG_PRO.NOME, ' ') AS NOME_PROCURATORE, " +
		// " UDI.COD_ID_ASSISTENTE, " +
		// " NVL(ASS_GIU.COGNOME, '-') AS COGNOME_ASSISTENTE, " +
		// " NVL(ASS_GIU.NOME, ' ') AS NOME_ASSISTENTE, " +
		// " UDI.NUMERO_MAX_FASCICOLI, " +
		// " UDI.LUOGO_UDIENZA, " +
		// " UDI.ORA_INIZIO, " +
		// " UDI.MIN_INIZIO, " +
		// " UDI.ORA_FINE, " +
		// " UDI.MIN_FINE, " +
		// " UDI.SEZIONE_UDIENZA, " +
		// " UDI.AULA_UDIENZA, " +
		// " NVL(SEZ.CODICE, ' ') AS CODICE_SEZIONE " ;
		// lStatement += " FROM UDIENZA_SIGE UDI ";
		// lStatement +=
		// " LEFT OUTER JOIN ASSISTENTE_GIUDIZIARIO ASS_GIU ON ASS_GIU.ID_ASSISTENTE_GIUDIZIARIO =
		// UDI.COD_ID_ASSISTENTE ";
		// lStatement +=
		// " LEFT OUTER JOIN MAGISTRATO MAG_GIU ON MAG_GIU.COD_MAGISTRATO = UDI.COD_GIUDICE AND
		// MAG_GIU.COD_UFFICIO_APPARTENENZA=UDI.COD_UFFICIO_APPARTENENZA "
		// ;
		// lStatement +=
		// " LEFT OUTER JOIN MAGISTRATO MAG_PRO ON MAG_PRO.COD_MAGISTRATO = UDI.COD_PROCURATORE ";
		// lStatement += " LEFT OUTER JOIN COLLEGIO COLL ON COLL.ID_COLLEGIO = UDI.COL_ID_COLLEGIO ";
		// lStatement += " LEFT OUTER JOIN SEZIONE SEZ ON SEZ.ID_SEZIONE = COLL.SEZ_ID_SEZIONE ";

		lStatement += " SELECT DISTINCT " + " UDI.COD_GIUDICE, "
				+ " NVL(MAG_GIU.COGNOME, '-') AS COGNOME_GIUDICE, "
				+ " NVL(MAG_GIU.NOME, ' ') AS NOME_GIUDICE, " + " UDI.COD_UFFICIO_APPARTENENZA, "
				+ " UDI.DATA_UDIENZA, " + " 0 ID_UDIENZA_SIGE, "
				// 20171127: [EC] elimino la funzione min over (partition by
				// + " min(UDI.COL_ID_COLLEGIO) over (partition by COD_GIUDICE) AS COL_ID_COLLEGIO, "
				// + " min(nvl(COLL.COD_COLLEGIO, ' ')) over (partition by COD_GIUDICE) AS COD_COLLEGIO, "
				+ " UDI.COL_ID_COLLEGIO, " + " COLL.COD_COLLEGIO, " + " SEZ.ID_SEZIONE, "
				+ " NVL(SEZ.DESCRIZIONE, ' ') AS DESCRIZIONE, " + " UDI.COD_PROCURATORE, "
				+ " NVL(MAG_PRO.COGNOME, '-') AS COGNOME_PROCURATORE, "
				+ " NVL(MAG_PRO.NOME, ' ') AS NOME_PROCURATORE, " + " UDI.COD_ID_ASSISTENTE, "
				+ " NVL(ASS_GIU.COGNOME, '-') AS COGNOME_ASSISTENTE, "
				+ " NVL(ASS_GIU.NOME, ' ') AS NOME_ASSISTENTE, " + " UDI.NUMERO_MAX_FASCICOLI, "
				// 20171127: [EC] AGGIUNGO LA DESCRIZIONE DELLA SEZIONE NEL LUOGO UDIENZA
				+ " UDI.LUOGO_UDIENZA || ' ' || (select s.descrizione from sezione s where s.id_sezione= UDI.SEZIONE_UDIENZA) as LUOGO_UDIENZA, "
				+ " UDI.SEZIONE_UDIENZA, " + " UDI.AULA_UDIENZA, "
				// + " null as LUOGO_UDIENZA, null as SEZIONE_UDIENZA, null as AULA_UDIENZA, "
				// 20171006: [SG] elimino ora inizio e fine poichè modifica il risultato atteso
				// + " UDI.ORA_INIZIO, " + " UDI.MIN_INIZIO, " + " UDI.ORA_FINE, " + " UDI.MIN_FINE, "
				+ " null as ORA_INIZIO, " + " null as MIN_INIZIO, " + " null as ORA_FINE, "
				+ " null as MIN_FINE, " + " NVL(SEZ.CODICE, ' ') AS CODICE_SEZIONE ";

		lStatement += " FROM UDIENZA_SIGE UDI ";
		lStatement += " LEFT OUTER JOIN ASSISTENTE_GIUDIZIARIO ASS_GIU ON ASS_GIU.ID_ASSISTENTE_GIUDIZIARIO = UDI.COD_ID_ASSISTENTE ";
		lStatement += " LEFT OUTER JOIN MAGISTRATO MAG_GIU ON MAG_GIU.COD_MAGISTRATO = UDI.COD_GIUDICE  AND MAG_GIU.COD_UFFICIO_APPARTENENZA=UDI.COD_UFFICIO_APPARTENENZA ";
		lStatement += " LEFT OUTER JOIN MAGISTRATO MAG_PRO ON MAG_PRO.COD_MAGISTRATO = UDI.COD_PROCURATORE ";
		lStatement += " LEFT OUTER JOIN COLLEGIO COLL ON COLL.ID_COLLEGIO = UDI.COL_ID_COLLEGIO ";
		lStatement += " LEFT OUTER JOIN SEZIONE SEZ ON SEZ.ID_SEZIONE = COLL.SEZ_ID_SEZIONE ";
		lStatement += " INNER JOIN UDIENZA_PROCEDIMENTO_SIGE UP ON UDI.ID_UDIENZA_SIGE = UP.UDI_ID_UDIENZA_SIGE ";
		return lStatement;
	}

	/**
	 * Metodo per la costruzione della sezione order by
	 *
	 * @return
	 */
	protected String getOrderByDataAsc() {
		String orderBy = new String("");
		orderBy = " ORDER BY DATA_UDIENZA ASC ";
		return orderBy;
	}

	/**
	 * Metodo che imposta le condizioni di where per la ricerca
	 *
	 * @param aModel
	 * @return
	 */
	public String setCondizioni(UdienzaSigeModel aModel, String aTipoRito) {
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
		if (aTipoRito.trim().compareTo("M") == 0)
			lCondizioni += " AND COL_ID_COLLEGIO IS NULL ";

		// -- Per Collegiale --//
		if (aTipoRito.trim().compareTo("C") == 0)
			lCondizioni += " AND COL_ID_COLLEGIO IS NOT NULL ";

		if (aModel.getCodGiudice() != null && aModel.getCodGiudice().length() > 1)
			lCondizioni += " AND COD_GIUDICE = '" + aModel.getCodGiudice() + "' ";

		if (aModel.getColIdCollegio() != null) {
			lCondizioni += " AND COL_ID_COLLEGIO = " + aModel.getColIdCollegio();
		}

		if (aModel.getCodProcuratore() != null && aModel.getCodProcuratore().length() > 0) {
			lCondizioni += " AND COD_PROCURATORE = '" + aModel.getCodProcuratore() + "' ";
		}

		if (aModel.getCodIdAssistente() != null) {
			lCondizioni += " AND COD_ID_ASSISTENTE = " + aModel.getCodIdAssistente() + "";
		}

		if (aModel.getNumeroMaxFascicoli() != null) {
			lCondizioni += " AND NUMERO_MAX_FASCICOLI = " + aModel.getNumeroMaxFascicoli() + "";
		}

		if (aModel.getLuogoUdienza() != null && aModel.getLuogoUdienza().length() > 0) {
			lCondizioni += " AND LUOGO_UDIENZA = '" + aModel.getLuogoUdienza() + "' ";
		}
		if (aModel.getCodUfficioAppartenenza() != null && aModel.getCodUfficioAppartenenza().length() > 0) {
			lCondizioni += " AND UDI.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza()
					+ "' ";
		}
		if (aModel.getOraInizio() != null && aModel.getOraInizio().length() > 0) {
			lCondizioni += " AND ORA_INIZIO = '" + aModel.getOraInizio() + "' ";
		}
		if (aModel.getMinInizio() != null && aModel.getMinInizio().length() > 0) {
			lCondizioni += " AND MIN_INIZIO = '" + aModel.getMinInizio() + "' ";
		}
		if (aModel.getOraFine() != null && aModel.getOraFine().length() > 0) {
			lCondizioni += " AND ORA_FINE = '" + aModel.getOraFine() + "' ";
		}
		if (aModel.getMinFine() != null && aModel.getMinFine().length() > 0) {
			lCondizioni += " AND MIN_FINE = '" + aModel.getMinFine() + "' ";
		}
		// 20170913: [SG] aggiunta condizione
		lCondizioni += "  AND up.flag_rinviata<>'A' ";

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		// logger.info("lCondizioni = "+lCondizioni);
		return lCondizioni;
	}

	protected String getSqlQueryPerRuolo() {

		String lStatement = new String("");

		/// INZIO PROVAAAAAAA EMMMMA
		lStatement += " SELECT COD_MAG_ASS, DATA_UDIENZA, COGNOME_GIUDICE, NOME_GIUDICE, COGNOME_PROCURATORE,COGNOME_ASSISTENTE,"
				+ " LUOGO_UDIENZA, LISTAGG(ID_UDIENZA_SIGE, '; ') WITHIN GROUP(ORDER BY DATA_UDIENZA)  AS ID_UDIENZE, COUNT(ID_UDIENZA_SIGE) AS NUM_UDIENZE, COL_ID_COLLEGIO FROM ( ";
		// FINE PROVAAAAAAA EMMMMA

		lStatement += " SELECT DISTINCT " + " UDI.COD_GIUDICE, " + " MASS.MAG_COD_MAGISTRATO COD_MAG_ASS,"
				+ " NVL(MAG_GIU.COGNOME, '-') AS COGNOME_GIUDICE, "
				+ " NVL(MAG_GIU.NOME, ' ') AS NOME_GIUDICE, " + " UDI.COD_UFFICIO_APPARTENENZA, "
				+ " UDI.DATA_UDIENZA, " + " UDI.ID_UDIENZA_SIGE , " + " UDI.COL_ID_COLLEGIO, "
				+ " COLL.COD_COLLEGIO, " + " SEZ.ID_SEZIONE, " + " NVL(SEZ.DESCRIZIONE, ' ') AS DESCRIZIONE, "
				+ " UDI.COD_PROCURATORE, " + " NVL(MAG_PRO.COGNOME, '-') AS COGNOME_PROCURATORE, "
				+ " NVL(MAG_PRO.NOME, ' ') AS NOME_PROCURATORE, " + " UDI.COD_ID_ASSISTENTE, "
				+ " NVL(ASS_GIU.COGNOME, '-') AS COGNOME_ASSISTENTE, "
				+ " NVL(ASS_GIU.NOME, ' ') AS NOME_ASSISTENTE, " + " UDI.NUMERO_MAX_FASCICOLI, "
				+ " UDI.LUOGO_UDIENZA || ' ' || (select s.descrizione from sezione s where s.id_sezione= UDI.SEZIONE_UDIENZA) as LUOGO_UDIENZA, "
				+ " UDI.SEZIONE_UDIENZA, " + " UDI.AULA_UDIENZA, " + " null as ORA_INIZIO, "
				+ " null as MIN_INIZIO, " + " null as ORA_FINE, " + " null as MIN_FINE, "
				+ " NVL(SEZ.CODICE, ' ') AS CODICE_SEZIONE ";
		lStatement += " FROM UDIENZA_SIGE UDI ";
		lStatement += " LEFT OUTER JOIN ASSISTENTE_GIUDIZIARIO ASS_GIU ON ASS_GIU.ID_ASSISTENTE_GIUDIZIARIO = UDI.COD_ID_ASSISTENTE ";
		lStatement += " LEFT OUTER JOIN MAGISTRATO MAG_PRO ON MAG_PRO.COD_MAGISTRATO = UDI.COD_PROCURATORE ";
		lStatement += " LEFT OUTER JOIN COLLEGIO COLL ON COLL.ID_COLLEGIO = UDI.COL_ID_COLLEGIO ";
		lStatement += " LEFT OUTER JOIN SEZIONE SEZ ON SEZ.ID_SEZIONE = COLL.SEZ_ID_SEZIONE ";

		lStatement += " INNER JOIN UDIENZA_PROCEDIMENTO_SIGE UP ON UDI.ID_UDIENZA_SIGE = UP.UDI_ID_UDIENZA_SIGE ";
		// inizio
		lStatement += " LEFT OUTER JOIN MAGISTRATO_ASSEGNATARIO MASS  ON MASS.FAS_SIGE_ID_FASCICOLO_SIGE = UP.FAS_ID_FASCICOLO_SIGE ";
		lStatement += " LEFT OUTER JOIN MAGISTRATO MAG_GIU ON MAG_GIU.COD_MAGISTRATO = MASS.MAG_COD_MAGISTRATO  AND MAG_GIU.COD_UFFICIO_APPARTENENZA=UDI.COD_UFFICIO_APPARTENENZA ";
		// fine

		return lStatement;
	}

	public void ricercaUdienzaSige(UdienzaSigeModel aModel, String aTipoRito) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQueryCollegiali();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel, aTipoRito);

		// if (aTipoRito.trim() == "C")
		// lCondizioni+= "AND COL_ID_COLLEGIO IS NOT NULL ";
		if (aTipoRito.compareTo("M") == 0)
			lCondizioni += "AND COL_ID_COLLEGIO IS NULL ";

		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		if ("C".equals(aTipoRito))
			lSql += "   ) GROUP BY  COD_GIUDICE,  COGNOME_GIUDICE, NOME_GIUDICE, COD_UFFICIO_APPARTENENZA, DATA_UDIENZA, ID_UDIENZA_SIGE,  COL_ID_COLLEGIO,"
					+ " COD_COLLEGIO, ID_SEZIONE, DESCRIZIONE,  COD_PROCURATORE,  COGNOME_PROCURATORE,  NOME_PROCURATORE, COD_ID_ASSISTENTE,"
					+ " COGNOME_ASSISTENTE,  NOME_ASSISTENTE, NUMERO_MAX_FASCICOLI,  LUOGO_UDIENZA, SEZIONE_UDIENZA,AULA_UDIENZA,ORA_INIZIO,"
					+ "  MIN_INIZIO,   ORA_FINE,  MIN_FINE,  CODICE_SEZIONE ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/**
	 * Metodo per la costruzione della sql query
	 *
	 * @return
	 */
	protected String getSqlQueryCollegiali() {
		String lStatement = new String("");

		lStatement += " SELECT DISTINCT COD_GIUDICE, COGNOME_GIUDICE, NOME_GIUDICE, COD_UFFICIO_APPARTENENZA, DATA_UDIENZA, ID_UDIENZA_SIGE, COL_ID_COLLEGIO, "
				+ "COD_COLLEGIO,ID_SEZIONE, DESCRIZIONE, COD_PROCURATORE, COGNOME_PROCURATORE,NOME_PROCURATORE, COD_ID_ASSISTENTE, COGNOME_ASSISTENTE,"
				+ " NOME_ASSISTENTE, NUMERO_MAX_FASCICOLI, LUOGO_UDIENZA,SEZIONE_UDIENZA, AULA_UDIENZA,ORA_INIZIO, MIN_INIZIO, ORA_FINE, MIN_FINE,"
				+ " CODICE_SEZIONE, LISTAGG (ID_UDIENZE,'; ') WITHIN GROUP( ORDER BY ID_UDIENZE) AS ID_UDIENZE, COUNT(distinct ID_UDIENZE) AS NUM_UDIENZE from (";

		lStatement += "SELECT DISTINCT UDI.COD_GIUDICE, " + " NVL(MAG_GIU.COGNOME, '-') AS COGNOME_GIUDICE, "
				+ " NVL(MAG_GIU.NOME, ' ') AS NOME_GIUDICE, " + " UDI.COD_UFFICIO_APPARTENENZA, "
				+ " UDI.DATA_UDIENZA, " + " 0 ID_UDIENZA_SIGE, "

				+ " UDI.COL_ID_COLLEGIO, " + " COLL.COD_COLLEGIO, " + " SEZ.ID_SEZIONE, "
				+ " NVL(SEZ.DESCRIZIONE, ' ') AS DESCRIZIONE, " + " UDI.COD_PROCURATORE, "
				+ " NVL(MAG_PRO.COGNOME, '-') AS COGNOME_PROCURATORE, "
				+ " NVL(MAG_PRO.NOME, ' ') AS NOME_PROCURATORE, " + " UDI.COD_ID_ASSISTENTE, "
				+ " NVL(ASS_GIU.COGNOME, '-') AS COGNOME_ASSISTENTE, "
				+ " NVL(ASS_GIU.NOME, ' ') AS NOME_ASSISTENTE, " + " UDI.NUMERO_MAX_FASCICOLI, "

				+ " UDI.LUOGO_UDIENZA "
				// commento perchè Nunzia in fase di test della 11.2.2 , ha detto che non deve essereci la
				// descrizione della sezione nel luogo udienza
				// + " || ' ' || (select s.descrizione from sezione s where s.id_sezione=
				// UDI.SEZIONE_UDIENZA)"
				+ " as LUOGO_UDIENZA, " + " UDI.SEZIONE_UDIENZA, " + " UDI.AULA_UDIENZA, "

				+ " null as ORA_INIZIO, " + " null as MIN_INIZIO, " + " null as ORA_FINE, "
				+ " null as MIN_FINE, " + " NVL(SEZ.CODICE, ' ') AS CODICE_SEZIONE ";

		// lStatement += " ,LISTAGG(ID_UDIENZA_SIGE, '; ') WITHIN GROUP(ORDER BY DATA_UDIENZA) AS ID_UDIENZE,
		// COUNT(ID_UDIENZA_SIGE) AS NUM_UDIENZE " ;

		lStatement += " ,ID_UDIENZA_SIGE as  ID_UDIENZE ";

		lStatement += " FROM UDIENZA_SIGE UDI ";
		lStatement += " LEFT OUTER JOIN ASSISTENTE_GIUDIZIARIO ASS_GIU ON ASS_GIU.ID_ASSISTENTE_GIUDIZIARIO = UDI.COD_ID_ASSISTENTE ";
		lStatement += " LEFT OUTER JOIN MAGISTRATO MAG_GIU ON MAG_GIU.COD_MAGISTRATO = UDI.COD_GIUDICE  AND MAG_GIU.COD_UFFICIO_APPARTENENZA=UDI.COD_UFFICIO_APPARTENENZA ";
		lStatement += " LEFT OUTER JOIN MAGISTRATO MAG_PRO ON MAG_PRO.COD_MAGISTRATO = UDI.COD_PROCURATORE ";
		lStatement += " LEFT OUTER JOIN COLLEGIO COLL ON COLL.ID_COLLEGIO = UDI.COL_ID_COLLEGIO ";
		lStatement += " LEFT OUTER JOIN SEZIONE SEZ ON SEZ.ID_SEZIONE = COLL.SEZ_ID_SEZIONE ";
		lStatement += " INNER JOIN UDIENZA_PROCEDIMENTO_SIGE UP ON UDI.ID_UDIENZA_SIGE = UP.UDI_ID_UDIENZA_SIGE ";

		return lStatement;
	}

}