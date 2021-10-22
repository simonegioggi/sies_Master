package siap.sico.decodifiche.dao;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

public class DecodificheSqlDAO extends SIAPSqlDAO {

	public DecodificheSqlDAO(Connection con) {
		super(con);
	}

	/**
	 * Ricerca gli Esiti Tenore raggrupati per il RV_HIGH_VALUE
	 * 
	 * @param lCodOggetto
	 */
	public void ricercaEsitiByOggetto(String lCodOggetto) {
		// 09/10/2009 String lStatement =
		// " SELECT RV_DOMAIN,RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'ESITO_TENORE' AND "
				+ " RV_HIGH_VALUE IN (SELECT  RV_HIGH_VALUE FROM CG_REF_CODES WHERE "
				+ " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND RV_LOW_VALUE='" + lCodOggetto + "') ";

		setStatement(lStatement);

	}

	/**
	 * Ricerca gli Esiti Tenore raggrupati per il RV_HIGH_VALUE e l'ALT2_VALUE relativo all'esito
	 * 
	 * @param lCodOggetto
	 */
	public void ricercaEsitiCompatibiliByEsitoOggetto(String lCodOggetto, String lCodEsito) {
		// 09/10/2009 String lStatement =
		// " SELECT RV_DOMAIN,RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'ESITO_TENORE' AND "
				+ " RV_HIGH_VALUE IN (SELECT  RV_HIGH_VALUE FROM CG_REF_CODES WHERE "
				+ " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND RV_LOW_VALUE='" + lCodOggetto + "')  AND "
				+ " RV_ALT2_VALUE IN (SELECT  RV_ALT2_VALUE FROM CG_REF_CODES WHERE "
				+ " RV_DOMAIN = 'ESITO_TENORE' AND RV_ABBREVIATION ='" + lCodEsito + "')";

		setStatement(lStatement);

	}

	/**
	 * Dal Codice dell'EsitoTenore mi ricavo il codice nell'RV_ABBREVIATION che rappresenta il COdice
	 * Provvedimento corrispondente
	 * 
	 * @param lCodEsito
	 */
	public void ricercaCodEsitoByEsitoTenore(String lCodEsito) {
		// 09/10/2009 String lStatement =
		// " SELECT RV_DOMAIN,RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'ESITO_TENORE' AND "
				+ " RV_LOW_VALUE = '" + lCodEsito + "'";

		setStatement(lStatement);

	}

	public void ricercaOggettoDecisioneSospDifferimento(String lOggSosp) {
		// 09/10/2009 String lStatement =
		// " SELECT RV_DOMAIN,RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'OGGETTO_SOSPENSIONI' AND "
				+ " RV_ABBREVIATION = '" + lOggSosp + "'";

		setStatement(lStatement);

	}

	public void ricercaProvvAnnMan(String lCodice) {
		// 09/10/2009 String lStatement =
		// " SELECT RV_DOMAIN,RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' "
				+ " AND RV_HIGH_VALUE = 'ANN_MAN' " + " AND RV_LOW_VALUE = '" + lCodice + "'";

		setStatement(lStatement);

	}

	public GenericModel getModel() throws DAOException {
		String lMeaning = getString("RV_MEANING");
		if (lMeaning != null)
			lMeaning = lMeaning.toUpperCase();

		return new DecodificheModel(getString("RV_LOW_VALUE"),
				// getString("RV_MEANING"),
				lMeaning, getString("RV_DOMAIN"), getString("RV_HIGH_VALUE"), getString("RV_ABBREVIATION"),
				getString("RV_ALT2_VALUE"), getString("RV_ALT3_VALUE"), getString("RV_ALT4_VALUE"),
				getString("RV_ALT5_VALUE"));
	}

	public String getModelRvLowValue() throws DAOException {
		return (getString("RV_LOW_VALUE"));
	}

	public String getModelRvHighValue() throws DAOException {
		return (getString("RV_HIGH_VALUE"));
	}

	public String getModelRvAbbreviation() throws DAOException {
		return (getString("RV_ABBREVIATION"));
	}

	public String getModelRvMeaning() throws DAOException {
		return (getString("RV_MEANING"));
	}

	// STUB 09/10/2009 Inserite Nuove colonne di decodifica.
	public String getModelRvAlt2Value() throws DAOException {
		return (getString("RV_ALT2_VALUE"));
	}

	public String getModelRvAlt3Value() throws DAOException {
		return (getString("RV_ALT3_VALUE"));
	}

	public String getModelRvAlt4Value() throws DAOException {
		return (getString("RV_ALT4_VALUE"));
	}

	public String getModelRvAlt5Value() throws DAOException {
		return (getString("RV_ALT5_VALUE"));
	}

	/**
	 * Lista Oggetti per parametro Contenuto
	 * <p>
	 * 
	 * @param aContenuto
	 *            per cui filtrare.
	 * @param aCodTipoUfficio
	 *            per cui filtrare.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void listaOggetti(String aContenuto, String aCodTipoUfficio) throws DAOException {

		String lStatement = new String();

		lStatement += " SELECT CG_RC.RV_LOW_VALUE COD_CONTENUTO, CG_RC.RV_MEANING DESC_CONTENUTO,";
		lStatement += " CG_RC2.RV_LOW_VALUE COD_OGGETTO, CG_RC2.RV_MEANING DESC_OGGETTO";
		lStatement += " ,NVL('',CG_RC3.RV_LOW_VALUE) COD_DETTAGLIO, NVL('',CG_RC3.RV_MEANING) DESC_DETTAGLIO, NVL('',CG_RC2.RV_ABBREVIATION) ABBR_OGGETTO ";
		lStatement += " FROM CG_REF_CODES CG_RC, CG_REF_CODES CG_RC2 ";
		lStatement += " ,CG_REF_CODES CG_RC3 ";
		lStatement += " WHERE CG_RC.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' ";
		// Se il codice contenuto è il (-) presenta la lista di tutti gli oggetti
		// che iniziano con C% (Per Tribunali di sorveglianza).
		// che iniziano con U% (Per Uffici di sorveglianza).
		// senza distinzione per gli altri Uffici ).
		if (aContenuto.equals("-")) {
			// MEV10-s3: aggiunte or condition per gestire trib. sorv. minori ed uff. sorv. minori
			if ("TDS".equals(aCodTipoUfficio) || "TDSM".equals(aCodTipoUfficio)) {
				lStatement += " AND CG_RC.RV_LOW_VALUE LIKE 'C%' ";
				// MERGE v10 COLLAUDO inizio ********
				// ulteriore filtro che elimina gli oggetti visibili ai maggiorenni e non visibili
				// ai minorenni e viceversa
				// 20170922: [SG] rimosso un oggetto per contenuto 'C002' = revoca MA per violazione
				// prescrizioni su proposta del magistrato (TDSM) e rimossi quattro oggetti per contenuto
				// 'C002' = revoca MA per violazione prescrizioni su proposta del magistrato (TDS)
				if ("TDS".equals(aCodTipoUfficio)) {
					lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('0317','9020','9025','9026','2741','2742','2743','2756','0233','2744','2746','2747') ";
				} else if ("TDSM".equals(aCodTipoUfficio)) {
					lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('0034','0035','2744','2746','2747','2757','2741','2742','2743','2756','0232') ";
				}
				// MERGE v10 COLLAUDO fine ********
			} else if ("UDS".equals(aCodTipoUfficio) || "UDSM".equals(aCodTipoUfficio)) {
				lStatement += " AND CG_RC.RV_LOW_VALUE LIKE 'U%' ";
				// MERGE v10 COLLAUDO inizio ********
				// ulteriore filtro che elimina gli oggetti visibili ai maggiorenni e non visibili
				// ai minorenni e viceversa
				if ("UDS".equals(aCodTipoUfficio)) {
					// 20170922: [SG] rimosso un oggetto per contenuto 'U004' = Esecuzione MA
					// ed un oggetto per contenuto 'U037' = Sopravvenienza nuovo titolo
					// MEV_66: aggiunti 4 contenuti solo x minori, rimossi 5 oggetti
					lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2409','2548','9022','9031','9050','9053','9055','9066','9070','9071','9073','9074','9075','2741','2742','2743','2756','2375','2294','2780','2781','2782','2783','2784') ";
				} else if ("UDSM".equals(aCodTipoUfficio)) {
					// lStatement +=
					// " AND CG_RC2.RV_LOW_VALUE NOT IN
					// ('2235','2236','2245','2280','2290','2291','2310','2311','2348','2349','2350','2351','2353','2354','2355','2356','2357','2358','2404','2405','2406','2407','2430','2440','2441','2442','2694')
					// ";
					// MAC 2017/04/01 Ripristinati i codici (2741,2742,2743,2756), tali codici non sono
					// visibili a UDSM e neppure a UDS
					// 20170922: [SG] rimosso un oggetto per contenuto 'U004' = Esecuzione MA
					//MEV63 GLI OGGETTI 2235 E 2236 DEVONO ESSERE VISIBILI AGLI UFFICI UDSM
					lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2245','2280','2290','2291','2310','2311','2348','2349','2350','2351','2353','2354','2355','2356','2357','2358','2404','2405','2406','2407','2430','2440','2441','2442','2694','2741','2742','2743','2756','2368','2292') ";
				}
				// MERGE v10 COLLAUDO fine ********
			}
		} else {
			lStatement += " AND CG_RC.RV_LOW_VALUE = '" + aContenuto + "'";

			// MERGE v10 COLLAUDO inizio ******************
			// ulteriore filtro che elimina gli oggetti visibili ai maggiorenni e non visibili
			// ai minorenni e viceversa in base al contenuto
			// UDS/UDSM
			if ("UDSM".equals(aCodTipoUfficio) || "UDS".equals(aCodTipoUfficio)) {
				if ("U019".equals(aContenuto)) {
					if ("UDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('9022') ";
				}
				if ("U024".equals(aContenuto)) {
					if ("UDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2409') ";
					if ("UDSM".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE IN ('2352','2359','2408','2409') ";
				}
				if ("U037".equals(aContenuto)) {
					if ("UDSM".equals(aCodTipoUfficio))
						// 20170922: [SG] rimosso un oggetto per contenuto 'U037' = Sopravvenienza nuovo
						// titolo
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2290','2292') ";
					// 20171124: [SG] rimosso un oggetto per contenuto 'U037' = Esecuzione MA
					else
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2294') ";
				}
				// MEV63: IL CONTENUTO ED I RELATIVI OGGETTI DEVONO ESSERE VISIBIIAD UFFICI UDSM
//				if ("U043".equals(aContenuto)) {
//					if ("UDSM".equals(aCodTipoUfficio))
//						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2235','2236') ";
//				}
				if ("U045".equals(aContenuto)) {
					if ("UDSM".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2245') ";
				}
				if ("U051".equals(aContenuto)) {
					if ("UDSM".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2280') ";
				}
				if ("U052".equals(aContenuto)) {
					if ("UDSM".equals(aCodTipoUfficio)) {
						// lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2291') ";
						// MAC 2017/04/01 inizio
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2291','2741','2742','2743','2756') ";
					}
					if ("UDS".equals(aCodTipoUfficio)) {
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2741','2742','2743','2756') ";
					}
					// MAC 2017/04/01 fine
				}
				if ("U054".equals(aContenuto)) {
					if ("UDSM".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2310','2311') ";
				}
				if ("U066".equals(aContenuto)) {
					if ("UDSM".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2430') ";
				}
				if ("U067".equals(aContenuto)) {
					if ("UDSM".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2440','2441','2442') ";
				}
				if ("U076".equals(aContenuto)) {
					if ("UDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2548') ";
				}
				if ("U093".equals(aContenuto)) {
					if ("UDSM".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2694') ";
				}
				if ("U101".equals(aContenuto)) {
					if ("UDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('9031') ";
				}
				if ("U102".equals(aContenuto)) {
					if ("UDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('9050') ";
				}
				if ("U110".equals(aContenuto)) {
					if ("UDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('9053') ";
				}
				if ("U112".equals(aContenuto)) {
					if ("UDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('9055','9066') ";
				}
				if ("U113".equals(aContenuto)) {
					if ("UDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('9070','9071') ";
				}
				if ("U114".equals(aContenuto)) {
					if ("UDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('9073','9074','9075') ";
				}
				// 20170922: [SG] rimosso un oggetto per contenuto 'U004' = Esecuzione MA
				if ("U004".equals(aContenuto)) {
					if ("UDSM".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2368') ";
					// 20171124: [SG] rimosso un oggetto per contenuto 'U004' = Esecuzione MA
					else
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2375') ";
				}
			}
			// TDS/TDSM
			if ("TDSM".equals(aCodTipoUfficio) || "TDS".equals(aCodTipoUfficio)) {
				if ("C002".equals(aContenuto)) {
					if ("TDSM".equals(aCodTipoUfficio))
						// 20170922: [SG] rimosso un oggetto per contenuto 'C002' = revoca MA per violazione
						// prescrizioni su proposta del magistrato
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('0232','2744','2746','2747','2757') ";
					else
						// TDS
						// 20170922: [SG] rimossi quattro oggetti per contenuto 'C002' = revoca MA per
						// violazione prescrizioni su proposta del magistrato
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('0233','2744','2746','2747') ";
				}
				if ("C004".equals(aContenuto)) {
					if ("TDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('0317') ";
				}
				if ("C015".equals(aContenuto)) {
					if ("TDSM".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('0034') ";
				}
				if ("C016".equals(aContenuto)) {
					if ("TDSM".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('0035') ";
				}
				if ("C027".equals(aContenuto)) {
					if ("TDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('9020') ";
				}
				if ("C046".equals(aContenuto)) {
					lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('2741','2742','2743','2756') ";
				}
				if ("C047".equals(aContenuto)) {
					if ("TDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('9025') ";
				}
				if ("C048".equals(aContenuto)) {
					if ("TDS".equals(aCodTipoUfficio))
						lStatement += " AND CG_RC2.RV_LOW_VALUE NOT IN ('9026') ";
				}
			}
			// MERGE v10 COLLAUDO fine ******************
		}

		lStatement += " AND CG_RC2.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		lStatement += " AND CG_RC.RV_LOW_VALUE = CG_RC2.RV_HIGH_VALUE ";
		lStatement += " AND CG_RC3.RV_DOMAIN(+) = 'DETTAGLIO_MOTIVO' ";
		lStatement += " AND CG_RC2.RV_LOW_VALUE = CG_RC3.RV_HIGH_VALUE(+) ";

		lStatement += " ORDER BY DESC_CONTENUTO, COD_CONTENUTO, COD_OGGETTO, COD_DETTAGLIO ";

		setStatement(lStatement);
	}

	/**
	 * Lista Contenuti per parametro CodTipoUfficio
	 * <p>
	 * 
	 * @param aCodTipoUfficio
	 *            per cui filtrare.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void listaContenuti(String aCodTipoUfficio) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' ";
		// MEV10-s3: aggiunte OR condition per gestire la casistica dei minori
		if ("TDS".equals(aCodTipoUfficio) || "TDSM".equals(aCodTipoUfficio))
			lStatement += " AND (RV_LOW_VALUE LIKE 'C%' OR RV_LOW_VALUE = '-') ";
		else if ("UDS".equals(aCodTipoUfficio) || "UDSM".equals(aCodTipoUfficio))
			lStatement += " AND (RV_LOW_VALUE LIKE 'U%' OR RV_LOW_VALUE = '-') ";

		lStatement += " ORDER BY RV_MEANING ";

		setStatement(lStatement);
	}

	/**
	 * Restituisce la lista degli oggetti del differimento (C014, U003)
	 * 
	 * @throws DAOException
	 */
	public void listaOggettoSospDifferimento() throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION " +
		lStatement = " SELECT * " + "  FROM CG_REF_CODES " + "  WHERE RV_DOMAIN = 'OGGETTO_SOSPENSIONI' ";
		lStatement += " AND (RV_ABBREVIATION = 'C014' OR RV_ABBREVIATION = 'U003')";

		setStatement(lStatement);
	}

	/**
	 * Restituisce la lista dei soli oggetti del differimento <b>Provvisorio</b> (U003)
	 * 
	 * @throws DAOException
	 */
	public void listaOggettoSospDifferimentoProvv() throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'OGGETTO_SOSPENSIONI' ";
		lStatement += " AND RV_ABBREVIATION = 'U003'";

		setStatement(lStatement);
	}

	/**
	 * Restituisce la lista dei soli oggetti del differimento <b>Provvisorio</b> (U003)
	 * 
	 * @throws DAOException
	 */
	public void listaOggettoSospDifferimentoDef() throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'OGGETTO_SOSPENSIONI' ";
		lStatement += " AND RV_ABBREVIATION = 'C014'";

		setStatement(lStatement);
	}

	/**
	 * Restituisce la lista dei soli oggetti della <b>Revoca</b> del differimento (C025)
	 * 
	 * @throws DAOException
	 */
	public void listaOggettoRevocaDifferimento() throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'OGGETTO_SOSPENSIONI' ";
		lStatement += " AND RV_ABBREVIATION = 'C025'";

		setStatement(lStatement);
	}

	public void listaOggettiSospensioneDecisioneSor() throws DAOException {
		String lStatement = new String();
		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		// Ticket#20211006019 - Sostituito il dominio U001 in X001 a seguito dell'aggiornamento della CG_REF_CODES
		// E' cambiato l'oggetto per le sospensioni
		//lStatement += " AND (RV_HIGH_VALUE = 'U001' OR RV_HIGH_VALUE = 'U071')";
		lStatement += " AND (RV_HIGH_VALUE = 'X001' OR RV_HIGH_VALUE = 'U071')";
		// Ticket#20211006019 - FINE
		setStatement(lStatement);
	}

	/**
	 * Imposta la query per il caricamento della lista dei Motivi Provvedimento per il Differimneto
	 * 
	 * @throws DAOException
	 */
	public void listaMotivoProvvSospDifferimento() throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION " +
		lStatement = " SELECT * " + "   FROM CG_REF_CODES " + "  WHERE RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		lStatement += "AND (RV_HIGH_VALUE = 'C014' OR RV_HIGH_VALUE = 'U003')";

		setStatement(lStatement);
	}

	/**
	 * Imposta la query per il caricamento della lista dei Motivi Provvedimento per il Differimneto
	 * Provvisorio
	 * 
	 * @throws DAOException
	 */
	public void listaMotivoProvvSospDifferimentoProvv() throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION " +
		lStatement = " SELECT * " + "   FROM CG_REF_CODES " + "  WHERE RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'"
				+ "    AND RV_HIGH_VALUE = 'U003' ";
		// 21/11/2014 d.f. eliminati i codici non di pertinenza SIEP
		lStatement += " AND RV_LOW_VALUE in ('2010','2011')";

		setStatement(lStatement);
	}

	/**
	 * Imposta la query per il caricamento della lista dei Motivi Provvedimento per il Differimneto Definitivo
	 * 
	 * @throws DAOException
	 */
	public void listaMotivoProvvSospDifferimentoDef() throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION " +
		lStatement = " SELECT * " + "   FROM CG_REF_CODES " + "  WHERE RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' "
				+ "    AND RV_HIGH_VALUE = 'C014'";

		// 21/11/2014 d.f. eliminati i codici non di pertinenza SIEP
		lStatement += " AND RV_LOW_VALUE in ('0030', '0031', '0032', '0033', '0201', '0202')";

		setStatement(lStatement);
	}

	/**
	 * Imposta la query per il caricamento della lista dei Motivi Provvedimento per la Revoca del Differimneto
	 * 
	 * @throws DAOException
	 */
	public void listaMotivoProvvRevocaDifferimento() throws DAOException {
		String lStatement = new String();

		// 10/09/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION " +
		lStatement = " SELECT * " + "   FROM CG_REF_CODES " + "  WHERE RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' "
				+ "    AND RV_HIGH_VALUE = 'C025'";

		setStatement(lStatement);
	}

	/**
	 * Imposta la query per il caricamento della lista dei Motivi Provvedimento per la Rigetto del
	 * Differimneto
	 * 
	 * @throws DAOException
	 */
	public void listaMotivoProvvRigettoDifferimento() throws DAOException {
		String lStatement = new String();

		// 10/09/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION " +
		lStatement = " SELECT * " + "   FROM CG_REF_CODES " + "  WHERE RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' "
				+ "    AND RV_HIGH_VALUE = 'C014'";

		// 21/11/2014 d.f. eliminati i codici non di pertinenza SIEP
		lStatement += " AND RV_LOW_VALUE in ('0030', '0031', '0032', '0033', '0201', '0202')";

		setStatement(lStatement);
	}

	public void listaTipologiaRigettoDifferimento() throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE "
				+ " RV_DOMAIN = 'ESITO_TENORE'  AND RV_HIGH_VALUE = 'C014' ";
		lStatement += " AND (RV_ABBREVIATION IN ('0002','0003','0004','0005','0115')) ";
		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvMA(String aCodTipoUfficio) throws DAOException {
		String lStatement = new String();

		// 10/09/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aCodTipoUfficio.equals("DETENZIONE"))
			lStatement += " AND (RV_LOW_VALUE = '0005' OR RV_LOW_VALUE = '0010' OR RV_LOW_VALUE = '0013') ";
		else if (aCodTipoUfficio.equals("AFFIDAMENTO"))
			lStatement += " AND (RV_LOW_VALUE = '0001' OR RV_LOW_VALUE = '0002' OR RV_LOW_VALUE = '0003') ";
		else if (aCodTipoUfficio.equals("SEMILIBERTA"))
			lStatement += " AND (RV_LOW_VALUE = '0004') ";
		else if (aCodTipoUfficio.equals("INDULTINO"))
			lStatement += " AND (RV_LOW_VALUE = '2245') ";
		else if (aCodTipoUfficio.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
			lStatement += " AND (RV_LOW_VALUE = '" + ICostantiMisuraAlternativa.ESP_PRESSO_DOM_MOTIVO + "' OR RV_LOW_VALUE = '0610' ) ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoOS(String aCodTipoUfficio) throws DAOException {
		String lStatement = new String();

		// 10/09/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aCodTipoUfficio.equals("OS_LIBERAZIONE_ANTICIPATA"))
			lStatement += " AND RV_LOW_VALUE = '0081'";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoOSLiberazioneAnticipataMA(String aCodTipoUfficio) throws DAOException {
		String lStatement = new String();

		// 10/09/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aCodTipoUfficio.equals("OS_LIBERAZIONE_ANTICIPATA_MA"))
			lStatement += " AND RV_LOW_VALUE = '0083'";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvSospProvvMA(String aCodTipoUfficio) throws DAOException {
		String lStatement = new String();

		// 10/09/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aCodTipoUfficio.equals("DETENZIONE"))
			lStatement += " AND (RV_LOW_VALUE = '2149' OR RV_LOW_VALUE = '2150' OR RV_LOW_VALUE = '2151' OR RV_LOW_VALUE = '2293' OR RV_LOW_VALUE = '2153') ";
		else if (aCodTipoUfficio.equals("AFFIDAMENTO"))
			lStatement += " AND (RV_LOW_VALUE = '2145' OR RV_LOW_VALUE = '2146' OR RV_LOW_VALUE = '2147') ";
		else if (aCodTipoUfficio.equals("SEMILIBERTA"))
			lStatement += " AND (RV_LOW_VALUE = '2148') ";
		else if (aCodTipoUfficio.equals("INDULTINO"))
			lStatement += " AND (RV_LOW_VALUE = '2280') ";
		else if (aCodTipoUfficio.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
			lStatement += " AND (RV_LOW_VALUE = '" + ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_MOTIVO
					+ "') ";
		else if (aCodTipoUfficio.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS))
			lStatement += " AND (RV_LOW_VALUE = '"
					+ ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_MOTIVO + "') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvSospProvvArrestiDom(String aCodTipoUfficio) throws DAOException {
		String lStatement = new String();

		// 10/09/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aCodTipoUfficio.equals("DETENZIONE"))
			// MERGE v10 COLLAUDO: aggiunto codice per revoca arresti domiciliari			
			//PEC m_dg.DOG07.01-10-2018.0030206.U (deve essere censito anche i codice 2291)			
			lStatement +=
		    " AND (RV_LOW_VALUE = '2756' OR RV_LOW_VALUE = '2741' OR RV_LOW_VALUE = '2742' OR RV_LOW_VALUE = '2743' OR RV_LOW_VALUE = '2291') ";
			// MAC 2017/04/01 Ripristinati i codici (2741,2742,2743,2756)
			//lStatement += " AND (RV_LOW_VALUE = '2756' OR RV_LOW_VALUE = '2741' OR RV_LOW_VALUE = '2742' OR RV_LOW_VALUE = '2743') ";		
		else if (aCodTipoUfficio.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
			lStatement += " AND (RV_LOW_VALUE = '" + ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_MOTIVO
					+ "') ";
		else if (aCodTipoUfficio.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS))
			lStatement += " AND (RV_LOW_VALUE = '"
					+ ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_MOTIVO + "') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvRipristinoArrestiDom(String aCodTipoUfficio) throws DAOException {
		String lStatement = new String();

		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aCodTipoUfficio.equals("DETENZIONE"))
			lStatement += " AND (RV_LOW_VALUE = '-' OR RV_LOW_VALUE = '2748' OR RV_LOW_VALUE = '2749' OR RV_LOW_VALUE = '2758' OR RV_LOW_VALUE = '2759' OR RV_LOW_VALUE = '2752' OR RV_LOW_VALUE = '2753' OR RV_LOW_VALUE = '2754' OR RV_LOW_VALUE = '2755') ";
		else if (aCodTipoUfficio.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
			lStatement += " AND (RV_LOW_VALUE = '" + ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_MOTIVO
					+ "') ";
		else if (aCodTipoUfficio.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS))
			lStatement += " AND (RV_LOW_VALUE = '"
					+ ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_MOTIVO + "') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvRevocaArrestiDom(String aCodTipoUfficio) throws DAOException {
		String lStatement = new String();

		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aCodTipoUfficio.equals("DETENZIONE"))
			// MERGE v10 COLLAUDO: aggiunto codice per revoca arresti domiciliari
			lStatement += " AND (RV_LOW_VALUE = '2744' OR RV_LOW_VALUE = '2757' OR RV_LOW_VALUE = '2746' OR RV_LOW_VALUE = '2747' OR RV_LOW_VALUE = '0232') ";
		else if (aCodTipoUfficio.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
			lStatement += " AND (RV_LOW_VALUE = '" + ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_MOTIVO
					+ "') ";
		else if (aCodTipoUfficio.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS))
			lStatement += " AND (RV_LOW_VALUE = '"
					+ ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_MOTIVO + "') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvRipristinoMA(String aCodTipoUfficio) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aCodTipoUfficio.equals("DETENZIONE"))
			lStatement += " AND (RV_LOW_VALUE = '0016' OR RV_LOW_VALUE = '0087' OR RV_LOW_VALUE = '0089' OR RV_LOW_VALUE = '0088') ";
		else if (aCodTipoUfficio.equals("AFFIDAMENTO"))
			lStatement += " AND (RV_LOW_VALUE = '0014' OR RV_LOW_VALUE = '0015' OR RV_LOW_VALUE = '0086') ";
		else if (aCodTipoUfficio.equals("SEMILIBERTA"))
			lStatement += " AND (RV_LOW_VALUE = '0091') ";
		else if (aCodTipoUfficio.equals("INDULTINO"))
			lStatement += " AND (RV_LOW_VALUE = '0196') ";
		else if (aCodTipoUfficio.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
			// 05/10/2011 Gestione Revoca Esp. pena presso Dom. emessa da UDS
			// lStatement +=
			// " AND (RV_LOW_VALUE = '"+ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO+"') ";
			lStatement += " AND (RV_LOW_VALUE in ('" + ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO
					+ "','" + ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO_DAUDS + "') ) ";
		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvDetDomSpeciale(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aTipo.equals("AMMISSIONE_PERIODO"))
			lStatement += " AND (RV_LOW_VALUE = '0012') ";
		if (aTipo.equals("SOSPENSIONE_PROVVISORIA"))
			lStatement += " AND (RV_LOW_VALUE = '2152') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvDicEffMA(String aCodTipoUfficio) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aCodTipoUfficio.equals("AFFIDAMENTO"))
			lStatement += " AND (RV_LOW_VALUE = '0021' OR RV_LOW_VALUE = '0190' OR RV_LOW_VALUE = '0191') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoRevocaMA(String aMisAlt) throws DAOException {

		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aMisAlt.equals("DETENZIONE"))
			lStatement += " AND (RV_LOW_VALUE = '2270' OR RV_LOW_VALUE = '0016' OR RV_LOW_VALUE = '0087' OR RV_LOW_VALUE = '0089' OR RV_LOW_VALUE = '0088' ) ";
		else if (aMisAlt.equals("AFFIDAMENTO"))
			lStatement += " AND (RV_LOW_VALUE = '0014' OR RV_LOW_VALUE = '0015' OR RV_LOW_VALUE = '0086') ";
		else if (aMisAlt.equals("SEMILIBERTA"))
			lStatement += " AND (RV_LOW_VALUE = '0091') ";
		else if (aMisAlt.equals("INDULTINO"))
			lStatement += " AND (RV_LOW_VALUE = '0196') ";
		else if (aMisAlt.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
			// lStatement +=
			// " AND (RV_LOW_VALUE = '"+ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO+"' OR
			// RV_LOW_VALUE = '2640') ";
			lStatement += " AND (RV_LOW_VALUE in ('" + ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO
					+ "','" + ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO_DAUDS + "') ) ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	// 02/12/2010 Inizio : Aggiunto Daniela
	public void listaMotivoCessazioneMA(String aCodTipoUfficio) throws DAOException {
		String lStatement = new String();

		lStatement = " SELECT * " + " FROM CG_REF_CODES " + " WHERE RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";

		if (aCodTipoUfficio.equals("DETENZIONE"))
			lStatement += " AND (RV_LOW_VALUE = '0024' OR RV_LOW_VALUE = '0110' OR RV_LOW_VALUE = '0112' ) ";
		else if (aCodTipoUfficio.equals("AFFIDAMENTO"))
			lStatement += " AND (RV_LOW_VALUE = '0167' OR RV_LOW_VALUE = '0166' OR RV_LOW_VALUE = '0168') ";
		else if (aCodTipoUfficio.equals("SEMILIBERTA"))
			lStatement += " AND (RV_LOW_VALUE = '0169') ";
		else if (aCodTipoUfficio.equals("INDULTINO"))
			lStatement += " AND (RV_LOW_VALUE = '0172') ";
		if (aCodTipoUfficio.equals("DETENZIONE_TERM"))
			lStatement += " AND (RV_LOW_VALUE = '0111' ) ";
		else if (aCodTipoUfficio.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
			lStatement += " AND (RV_LOW_VALUE = '"
					+ ICostantiMisuraAlternativa.CESSAZIONE_ESP_PRESSO_DOM_MOTIVO + "') ";
		// =========== MDS =======================================
		else if (aCodTipoUfficio.equals("AFFIDAMENTO_MDS_51BIS"))
			lStatement += " AND (RV_LOW_VALUE in ('2281','2205','2282') ) ";
		else if (aCodTipoUfficio.equals("DETENZIONE_MDS_51BIS"))
			lStatement += " AND (RV_LOW_VALUE in ('2284','2285','2287','2288') ) ";
		else if (aCodTipoUfficio.equals("SEMILIBERTA_MDS_51BIS"))
			lStatement += " AND (RV_LOW_VALUE in ('2283') ) ";
		else if (aCodTipoUfficio.equals("INDULTINO_MDS_51BIS"))
			lStatement += " AND (RV_LOW_VALUE in ('2290') ) ";
		else if (aCodTipoUfficio.equals("DETENZIONE_TERM_MDS_51BIS"))
			lStatement += " AND (RV_LOW_VALUE in ('2286') ) ";
		else if (aCodTipoUfficio.equals("ESP_PRESSO_DOM_MDS_51BIS"))
			lStatement += " AND (RV_LOW_VALUE in ('2299') ) ";
		// ========== TDS su reclamo =================================
		else if (aCodTipoUfficio.equals("AFFIDAMENTO_TDS_51BIS"))
			lStatement += " AND (RV_LOW_VALUE in ('1200','1201','1202') ) ";
		else if (aCodTipoUfficio.equals("DETENZIONE_TDS_51BIS"))
			lStatement += " AND (RV_LOW_VALUE in ('1203','1205','1206','1207') ) ";
		else if (aCodTipoUfficio.equals("SEMILIBERTA_TDS_51BIS"))
			lStatement += " AND (RV_LOW_VALUE in ('1208') ) ";
		else if (aCodTipoUfficio.equals("DETENZIONE_TERM_TDS_51BIS"))
			lStatement += " AND (RV_LOW_VALUE in ('1204') ) ";
		else if (aCodTipoUfficio.equals("ESP_PRESSO_DOM_TDS_51BIS"))
			lStatement += " AND (RV_LOW_VALUE in ('1209') ) ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	// 02/12/2010 Fine

	public void listaMotivoProvvProrogaUltPeriodo(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aTipo.equals("PROROGA_ULT_PERIODO"))
			lStatement += " AND (RV_LOW_VALUE = '0077') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvAmmAff(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aTipo.equals("AMMISSIONE_AFFIDAMENTO"))
			lStatement += " AND (RV_LOW_VALUE = '0192') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvRipristinoDetDomSpec(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aTipo.equals("RIPRISTINO_DET_DOM_SPEC"))
			lStatement += " AND (RV_LOW_VALUE = '0194') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvAmmProvvisoria(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aTipo.equals("AMMISSIONE_PROV_DET_DOM"))
			lStatement += " AND (RV_LOW_VALUE = '2005') ";
		else if (aTipo.equals("AMMISSIONE_PROV_AFFI"))
			lStatement += " AND (RV_LOW_VALUE in ('2006','2008')) ";
		// lStatement += " AND (RV_LOW_VALUE = '2006') "; //mod d.f.

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvMADetDomTemp(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aTipo.equals("AMMISSIONE_PROV_DET_DOM_TEMP")) {
			lStatement += " AND (RV_LOW_VALUE = '0011') ";
		} else if (aTipo.equals("PROROGA_AMMISSIONE_PROV_DET_DOM_TEMP")) {
			lStatement += " AND (RV_LOW_VALUE = '0197'OR RV_LOW_VALUE = '0011') ";
		} else if (aTipo.equals("PROROGA_PROVVISORIA_DET_DOM_TEMP")) {
			lStatement += " AND (RV_LOW_VALUE = '2340') ";
		}

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvUltPeriodoMA(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aTipo.equals("ULT_PERIODO_MA"))
			lStatement += " AND (RV_LOW_VALUE = '0101') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoMAPreEff(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aTipo.equals("AFFIDAMENTO"))
			lStatement += " AND (RV_LOW_VALUE IN ('2160','2161','2162')) ";

		if (aTipo.equals("DETENZIONE"))
			lStatement += " AND (RV_LOW_VALUE IN ('2164','2165','2166','2167')) ";

		if (aTipo.equals("SEMILIBERTA"))
			lStatement += " AND (RV_LOW_VALUE = '2163') ";

		if (aTipo.equals("INDULTINO"))
			lStatement += " AND (RV_LOW_VALUE = '2289') ";

		if (aTipo.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
			lStatement += " AND (RV_LOW_VALUE = '" + ICostantiMisuraAlternativa.PEREFF_ESP_PRESSO_DOM_MOTIVO
					+ "') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvedimentoProsecProvvMA(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";

		if (aTipo.equals("AFFIDAMENTO"))
			lStatement += " AND (RV_LOW_VALUE IN ('2205','2281','2282')) ";

		if (aTipo.equals("DETENZIONE"))
			lStatement += " AND (RV_LOW_VALUE IN ('2284','2285','2288','2286','2287')) ";

		if (aTipo.equals("SEMILIBERTA"))
			lStatement += " AND (RV_LOW_VALUE = '2283') ";

		if (aTipo.equals("INDULTINO51BIS"))
			lStatement += " AND (RV_LOW_VALUE = '2290') ";

		if (aTipo.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS))
			lStatement += " AND (RV_LOW_VALUE = '"
					+ ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_MOTIVO + "') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	/**
	 * 
	 * @param aTipo
	 * @throws DAOException
	 * @since feb-2014
	 */
	public void listaMotivoProvvedimentoProsecMA51Bis(String aTipo) throws DAOException {
		String lStatement = new String();

		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";

		// ====================================
		// Magistrato
		// ====================================
		if (aTipo.equals("AFFIDAMENTO_MDS"))
			lStatement += " AND (RV_LOW_VALUE IN ('2281','2205','2282')) ";

		if (aTipo.equals("DETENZIONE_MDS"))
			lStatement += " AND (RV_LOW_VALUE IN ('2284','2285','2287','2288')) ";

		if (aTipo.equals("SEMILIBERTA_MDS"))
			lStatement += " AND (RV_LOW_VALUE = '2283') ";

		if (aTipo.equals("ESECPREDOM_MDS"))
			lStatement += " AND (RV_LOW_VALUE = '2299') ";

		if (aTipo.equals("DETDOMTERM_MDS"))
			lStatement += " AND (RV_LOW_VALUE = '2286') ";

		// ====================================
		// Tribunale
		// ====================================
		if (aTipo.equals("AFFIDAMENTO_TDS"))
			lStatement += " AND (RV_LOW_VALUE IN ('1200','1201','1202')) ";

		if (aTipo.equals("DETENZIONE_TDS"))
			lStatement += " AND (RV_LOW_VALUE IN ('1203','1205','1206','1207')) ";

		if (aTipo.equals("SEMILIBERTA_TDS"))
			lStatement += " AND (RV_LOW_VALUE = '1208') ";

		if (aTipo.equals("ESECPREDOM_TDS"))
			lStatement += " AND (RV_LOW_VALUE = '1209') ";

		if (aTipo.equals("DETDOMTERM_TDS"))
			lStatement += " AND (RV_LOW_VALUE = '1204') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvedimentoEstDefMA(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aTipo.equals("AFFIDAMENTO"))
			lStatement += " AND (RV_LOW_VALUE IN ('0020','0092','0093')) ";

		if (aTipo.equals("DETENZIONE"))
			lStatement += " AND (RV_LOW_VALUE IN ('0095','0100','0101','0102','0103')) ";

		if (aTipo.equals("SEMILIBERTA"))
			lStatement += " AND (RV_LOW_VALUE = '0094') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvedimentoEspulsione(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aTipo.equals("CONCESSIONE"))
			lStatement += " AND (RV_LOW_VALUE IN ('2140')) ";
		else if (aTipo.equals("ACCOGLIE") || aTipo.equals("RIFIUTA"))
			lStatement += " AND (RV_LOW_VALUE IN ('0029')) ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvedimentoRigettoMA() throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		lStatement += " AND (RV_LOW_VALUE IN ('9000','9001','9002')) ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvMAReLibCond(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aTipo.equals("AMMISSIONE_PROV_RE_LIB_COND"))
			lStatement += " AND (RV_LOW_VALUE = '0026') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaMotivoProvvMACOLibCond(String aTipo) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		if (aTipo.equals("AMMISSIONE_CO_LIB_COND"))
			lStatement += " AND (RV_LOW_VALUE = '0025') ";

		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	public void listaAutoritaSospTDSUDS() throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'TIPO_UFFICIO_SOSP' ";
		lStatement += " AND (RV_HIGH_VALUE = 'UDS' OR RV_HIGH_VALUE = 'TDS') ";
		lStatement += " ORDER BY RV_ABBREVIATION ";

		setStatement(lStatement);
	}

	/**
	 * Query per recupero dei tipi ufficio cumulo per : - RV_DOMAIN = 'TIPO_UFFICIO_CUMULO' - E (
	 * RV_HIGH_VALUE = 'T' OR RV_HIGH_VALUE = 'S' OR RV_HIGH_VALUE = 'C' ). Le occorrenze sono ordinate per il
	 * campo RV_MEANING
	 * <p>
	 * 
	 * @throws DAOException
	 */
	public void listaTipoUfficioCumuloRifSiep() throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES " ;
		lStatement = " SELECT * FROM CG_REF_CODES ";
		lStatement += " WHERE RV_DOMAIN = 'TIPO_UFFICIO_CUMULO' ";
		lStatement += " AND ( RV_HIGH_VALUE = 'T' OR RV_HIGH_VALUE = 'S' OR RV_HIGH_VALUE = 'C' ) ";
		lStatement += " ORDER BY RV_MEANING ";

		setStatement(lStatement);
	}

	/**
	 * Query per recupero dei tipi ufficio cumulo per : - RV_DOMAIN = 'TIPO_UFFICIO_CUMULO' e RV_DOMAIN =
	 * 'UFFICIO_LOGIN' - E ( RV_HIGH_VALUE = 'T' OR RV_HIGH_VALUE = 'S' OR RV_HIGH_VALUE = 'C' ). - E
	 * RV_ABBREVIATION pari ='V' Le occorrenze sono ordinate per il campo RV_MEANING
	 * <p>
	 * 
	 * @throws DAOException
	 */
	public void listaTipoUfficioCumuloUfficioLoginRifSiep() throws DAOException {
		String lStatement = new String();

		lStatement = "select * from (SELECT * FROM CG_REF_CODES";
		lStatement += " WHERE RV_DOMAIN = 'TIPO_UFFICIO_CUMULO'";
		lStatement += " AND ( RV_HIGH_VALUE = 'T' OR RV_HIGH_VALUE = 'S' OR RV_HIGH_VALUE = 'C' )";
		lStatement += " and RV_HIGH_VALUE IS NOT NULL";
		// MEV10-s3: refactoring della query, aggiunte 2 union
		lStatement += " union";
		lStatement += " SELECT * FROM CG_REF_CODES";
		lStatement += " WHERE RV_DOMAIN = 'UFFICIO_LOGIN'";
		lStatement += " AND RV_ABBREVIATION ='V'";
		lStatement += " union";
		lStatement += " SELECT * FROM CG_REF_CODES";
		lStatement += " WHERE RV_DOMAIN = 'UFFICIO_LOGIN'";
		lStatement += " AND RV_ABBREVIATION ='Z')";
		lStatement += " ORDER BY RV_MEANING ";

		setStatement(lStatement);
	}

	/**
	 * Query per recupero dei tipi ufficio cumulo per : - RV_DOMAIN = 'TIPO_UFFICIO_CUMULO' - E (
	 * RV_HIGH_VALUE = 'T' OR RV_HIGH_VALUE = 'S' OR RV_HIGH_VALUE = 'C' OR RV_HIGH_VALUE = 'D'). Le
	 * occorrenze sono ordinate per il campo RV_MEANING
	 * <p>
	 * 
	 * @throws DAOException
	 */
	public void listaTipoUfficioCumuloRifMSic() throws DAOException {
		String lStatement = new String();

		lStatement = " select * from (SELECT * FROM CG_REF_CODES ";
		lStatement += " WHERE RV_DOMAIN = 'TIPO_UFFICIO_CUMULO' ";
		lStatement += " AND ( RV_HIGH_VALUE = 'T' OR RV_HIGH_VALUE = 'S' OR RV_HIGH_VALUE = 'C' OR RV_HIGH_VALUE = 'D' ) ";
		lStatement += " union";
		lStatement += " SELECT * FROM CG_REF_CODES";
		lStatement += " WHERE RV_DOMAIN = 'UFFICIO_LOGIN'";
		lStatement += " AND RV_ABBREVIATION ='V'";
		// MEV10-s3: refactoring della query, aggiunta union e tolta and condition inutile
		// lStatement += " and RV_ABBREVIATION IS NOT NULL  ";
		lStatement += " union";
		lStatement += " SELECT * FROM CG_REF_CODES";
		lStatement += " WHERE RV_DOMAIN = 'UFFICIO_LOGIN'";
		lStatement += " AND RV_ABBREVIATION ='Z')";
		lStatement += " ORDER BY RV_MEANING ";

		setStatement(lStatement);
	}

	public void listaTipoDecreto() throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'TIPO_DECRETO' ";

		setStatement(lStatement);
	}

	/**
	 * Lista Oggetti con parametro Contenuto diverso da quello di input.
	 * <p>
	 * 
	 * @param aContenuto
	 *            per cui filtrare.
	 * @param aCodTipoUfficio
	 *            per cui filtrare.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void listaOggettiDiversi(String aContenuto, String aCodTipoUfficio) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT CG_RC.RV_LOW_VALUE COD_CONTENUTO, CG_RC.RV_MEANING DESC_CONTENUTO,";
		lStatement += " CG_RC2.RV_LOW_VALUE COD_OGGETTO, CG_RC2.RV_MEANING DESC_OGGETTO";
		lStatement += " ,NVL('',CG_RC3.RV_LOW_VALUE) COD_DETTAGLIO, NVL('',CG_RC3.RV_MEANING) DESC_DETTAGLIO, NVL('',CG_RC2.RV_ABBREVIATION) ABBR_OGGETTO";
		lStatement += " FROM CG_REF_CODES CG_RC, CG_REF_CODES CG_RC2 ";
		lStatement += " ,CG_REF_CODES CG_RC3 ";
		lStatement += " WHERE CG_RC.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' ";
		// Si presenta la lista di tutti gli oggetti che iniziano con C% (Per Tribunali di sorveglianza) o che
		// iniziano con U% (Per Uffici di sorveglianza).
		// Esclusi gli oggetti con il contenuto pari al codice in input.
		if (aCodTipoUfficio.equals("TDS"))
			lStatement += " AND CG_RC.RV_LOW_VALUE LIKE 'C%' ";
		else if (aCodTipoUfficio.equals("UDS"))
			lStatement += " AND CG_RC.RV_LOW_VALUE LIKE 'U%' ";

		lStatement += " AND CG_RC.RV_LOW_VALUE <> '" + aContenuto + "'";

		lStatement += " AND CG_RC2.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		lStatement += " AND CG_RC.RV_LOW_VALUE = CG_RC2.RV_HIGH_VALUE ";
		lStatement += " AND CG_RC3.RV_DOMAIN(+) = 'DETTAGLIO_MOTIVO' ";
		lStatement += " AND CG_RC2.RV_LOW_VALUE = CG_RC3.RV_HIGH_VALUE(+) ";
		lStatement += " ORDER BY DESC_CONTENUTO, COD_CONTENUTO, COD_OGGETTO, COD_DETTAGLIO ";

		setStatement(lStatement);
	}

	public void listaMotiviProvvedimentiSospensione(String aCodTipoRegistroOrdinanza) throws DAOException {
		String lStatement = new String();

		lStatement = " SELECT MOTIVO.RV_DOMAIN, MOTIVO.RV_LOW_VALUE, MOTIVO.RV_MEANING, MOTIVO.RV_HIGH_VALUE, MOTIVO.RV_ABBREVIATION";
		lStatement += ", MOTIVO.RV_ALT2_VALUE, MOTIVO.RV_ALT3_VALUE, MOTIVO.RV_ALT4_VALUE, MOTIVO.RV_ALT5_VALUE ";
		lStatement += " FROM CG_REF_CODES MOTIVO, CG_REF_CODES SOSPENSIONI";
		lStatement += " WHERE MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'";
		lStatement += " AND SOSPENSIONI.RV_DOMAIN = 'OGGETTO_SOSPENSIONI'";
		lStatement += " AND MOTIVO.RV_HIGH_VALUE = SOSPENSIONI.RV_ABBREVIATION";
		lStatement += " AND SOSPENSIONI.RV_HIGH_VALUE = '" + aCodTipoRegistroOrdinanza + "'";
		if (aCodTipoRegistroOrdinanza.equals("0001")) // SIUS
			lStatement += " AND ( MOTIVO.RV_HIGH_VALUE !=  'C025' AND MOTIVO.RV_HIGH_VALUE !=  'C020' AND MOTIVO.RV_HIGH_VALUE !=  'U028')";
		lStatement += " ORDER BY MOTIVO.RV_MEANING ";

		setStatement(lStatement);
	}

	public void listaMotiviProvvedimentiRevoca(String aCodTipoRegistroOrdinanza) throws DAOException {
		String lStatement = new String();

		lStatement = " SELECT MOTIVO.RV_DOMAIN, MOTIVO.RV_LOW_VALUE, MOTIVO.RV_MEANING, MOTIVO.RV_HIGH_VALUE, MOTIVO.RV_ABBREVIATION";
		lStatement += ", MOTIVO.RV_ALT2_VALUE, MOTIVO.RV_ALT3_VALUE, MOTIVO.RV_ALT4_VALUE, MOTIVO.RV_ALT5_VALUE ";
		lStatement += " FROM CG_REF_CODES MOTIVO, CG_REF_CODES SOSPENSIONI";
		lStatement += " WHERE MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'";
		lStatement += " AND SOSPENSIONI.RV_DOMAIN = 'OGGETTO_SOSPENSIONI'";
		lStatement += " AND MOTIVO.RV_HIGH_VALUE = SOSPENSIONI.RV_ABBREVIATION";
		lStatement += " AND SOSPENSIONI.RV_HIGH_VALUE = '" + aCodTipoRegistroOrdinanza + "'";
		lStatement += " AND ( MOTIVO.RV_HIGH_VALUE = 'C025' OR MOTIVO.RV_HIGH_VALUE = 'C020' OR MOTIVO.RV_HIGH_VALUE = 'G001' OR MOTIVO.RV_HIGH_VALUE = 'G007' OR MOTIVO.RV_HIGH_VALUE = 'REVS' )";
		lStatement += " ORDER BY MOTIVO.RV_MEANING ";

		setStatement(lStatement);
	}

	public void listaOggettiRevoca(String aCodTipoRegistroOrdinanza) throws DAOException {
		String lStatement = new String();

		// 09/10/2009 lStatement =
		// " SELECT SOSPENSIONI.RV_DOMAIN, SOSPENSIONI.RV_LOW_VALUE, SOSPENSIONI.RV_MEANING,
		// SOSPENSIONI.RV_HIGH_VALUE, SOSPENSIONI.RV_ABBREVIATION";
		lStatement = " SELECT * ";
		lStatement += " FROM CG_REF_CODES SOSPENSIONI";
		lStatement += " WHERE SOSPENSIONI.RV_DOMAIN = 'OGGETTO_SOSPENSIONI'";
		lStatement += " AND SOSPENSIONI.RV_HIGH_VALUE = '" + aCodTipoRegistroOrdinanza + "'";
		lStatement += " AND ( SOSPENSIONI.RV_ABBREVIATION = 'C025' OR SOSPENSIONI.RV_ABBREVIATION = 'C020' OR SOSPENSIONI.RV_ABBREVIATION = 'G001' OR SOSPENSIONI.RV_ABBREVIATION =  'G007' OR SOSPENSIONI.RV_ABBREVIATION = 'REVS')";
		lStatement += " ORDER BY SOSPENSIONI.RV_MEANING ";

		setStatement(lStatement);
	}

	public void listaEsitiTenoreSospensione(String aCodTipoRegistroOrdinanza) throws DAOException {
		String lStatement = new String();

		lStatement = " SELECT ESITO.RV_DOMAIN, ESITO.RV_LOW_VALUE, ESITO.RV_MEANING, ESITO.RV_HIGH_VALUE, ESITO.RV_ABBREVIATION";
		lStatement += ", ESITO.RV_ALT2_VALUE, ESITO.RV_ALT3_VALUE, ESITO.RV_ALT4_VALUE, ESITO.RV_ALT5_VALUE ";
		lStatement += " FROM CG_REF_CODES ESITO, CG_REF_CODES SOSPENSIONI";
		lStatement += " WHERE ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'";
		lStatement += " AND SOSPENSIONI.RV_DOMAIN = 'OGGETTO_SOSPENSIONI'";
		lStatement += " AND ESITO.RV_HIGH_VALUE = SOSPENSIONI.RV_ABBREVIATION";
		lStatement += " AND SOSPENSIONI.RV_HIGH_VALUE = '" + aCodTipoRegistroOrdinanza + "'";
		lStatement += " AND ESITO.RV_LOW_VALUE NOT IN ('0091', '0109', '0103', '0100', '0095', '0106', '0092', '0093', '0111', '0090', '0085')";
		lStatement += " ORDER BY ESITO.RV_MEANING ";

		setStatement(lStatement);
	}

	public void listaEsitiTenoreRevoca(String aCodTipoRegistroOrdinanza) throws DAOException {
		String lStatement = new String();

		lStatement = " SELECT ESITO.RV_DOMAIN, ESITO.RV_LOW_VALUE, ESITO.RV_MEANING, ESITO.RV_HIGH_VALUE, ESITO.RV_ABBREVIATION";
		lStatement += ", ESITO.RV_ALT2_VALUE, ESITO.RV_ALT3_VALUE, ESITO.RV_ALT4_VALUE, ESITO.RV_ALT5_VALUE ";
		lStatement += " FROM CG_REF_CODES ESITO, CG_REF_CODES SOSPENSIONI";
		lStatement += " WHERE ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'";
		lStatement += " AND SOSPENSIONI.RV_DOMAIN = 'OGGETTO_SOSPENSIONI'";
		lStatement += " AND ESITO.RV_HIGH_VALUE = SOSPENSIONI.RV_ABBREVIATION";
		lStatement += " AND SOSPENSIONI.RV_HIGH_VALUE = '" + aCodTipoRegistroOrdinanza + "'";
		lStatement += " AND ( ESITO.RV_LOW_VALUE = '0091' OR ESITO.RV_LOW_VALUE = '0109' )";
		lStatement += " ORDER BY ESITO.RV_MEANING ";

		setStatement(lStatement);
	}

	/**
	 * Dal Codice dell'Incarico si ricava la lista delle Attività previste. SIEPE
	 * 
	 * @param Cod
	 *            Incarico
	 */
	public void ricercaAttivitaByIncarico(String aCodIncarico) {
		// 09/10/2009 String lStatement =
		// " SELECT RV_DOMAIN, RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES " +
		String lStatement = " SELECT * FROM CG_REF_CODES "
				+ " JOIN INCARICO_ATTIVITA ON INCARICO_ATTIVITA.RV_ATTIVITA = CG_REF_CODES.RV_LOW_VALUE AND INCARICO_ATTIVITA.RV_INCARICO = '"
				+ aCodIncarico + "'";
		// lStatement += "WHERE RV_DOMAIN = 'ATTIVITA'" ;

		setStatement(lStatement);
	}

	/**
	 * Lista Oggetti-Contenuti SIGE. La funzione restituisce l'elenco di tutti gli Oggetti SIGE ed i loro
	 * Contenuti
	 * <p>
	 * 
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	/*
	 * STUB: Si noti che nella select si usa una "outer join" perchè non sono stati ancora aggiunti alcuni
	 * Contenuti SIGE nella CG_REF_CODES (OGGETTO_PROCEDIMENTO). Appena sarà fatto bisogna sostituire
	 * "left outer join" con "join". Luigi 25-06-2008.
	 */
	public void listaOggettiSige() throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT CG_RC1.RV_LOW_VALUE COD_OGGETTO, CG_RC1.RV_MEANING DESC_OGGETTO, CG_RC1.RV_ABBREVIATION ABBR_OGGETTO,";
		lStatement += " CG_RC1.RV_HIGH_VALUE COD_CONTENUTO, CG_RC2.RV_MEANING DESC_CONTENUTO";
		lStatement += " FROM CG_REF_CODES CG_RC1 left outer join CG_REF_CODES CG_RC2 on  (CG_RC1.RV_HIGH_VALUE = CG_RC2.RV_LOW_VALUE AND CG_RC2.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')";
		lStatement += " WHERE CG_RC1.RV_DOMAIN = 'OGGETTO_SIGE' ";
		lStatement += " ORDER BY DESC_CONTENUTO, COD_CONTENUTO, CG_RC1.RV_LOW_VALUE ";

		setStatement(lStatement);
	}

	/**
	 * Ricerca gli Esiti Tenore Sige raggrupati per il RV_HIGH_VALUE
	 * 
	 * @param lCodOggettoSige
	 */
	public void ricercaEsitiByOggettoSige(String lCodOggettoSige) {
		// 09/10/2009 String lStatement =
		// " SELECT RV_DOMAIN,RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'ESITO_TENORE_SIGE' AND "
				+ " RV_HIGH_VALUE IN (SELECT  RV_HIGH_VALUE FROM CG_REF_CODES WHERE "
				+ " RV_DOMAIN = 'OGGETTO_SIGE' AND RV_LOW_VALUE='" + lCodOggettoSige + "') ";

		setStatement(lStatement);

	}

	/**
	 * Ricerca gli Dati Provvedimento Sige raggrupati per il RV_HIGH_VALUE
	 * 
	 * @param lCodOggettoSige
	 */
	public void ricercaDatiProvvSigeByOggetto(String lCodOggettoSige) {
		// 09/10/2009 String lStatement =
		// " SELECT RV_DOMAIN,RV_LOW_VALUE, RV_MEANING, RV_HIGH_VALUE,RV_ABBREVIATION FROM CG_REF_CODES WHERE
		// "
		// +
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE "
				+ " RV_DOMAIN = 'DATI_PROVVEDIMENTO_SIGE' AND "
				+ " RV_HIGH_VALUE IN (SELECT  RV_HIGH_VALUE FROM CG_REF_CODES WHERE "
				+ " RV_DOMAIN = 'OGGETTO_SIGE' AND RV_LOW_VALUE='" + lCodOggettoSige + "') ";

		setStatement(lStatement);
	}

	public void ricercaTipoAutorita() {
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'TIPO_AUTORITA' AND "
				+ " (RV_HIGH_VALUE is NULL or RV_HIGH_VALUE != 'AUTORITA_MINORENNI') ";

		setStatement(lStatement);
	}

	public void ricercaDecodificheTipoMisureMinorenni() {
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'TIPO_MISURA_SICUREZZA' AND "
				// + " rv_low_value in ('02','08','13','14','17','18') ORDER BY RV_MEANING ";
				// Per gli utenti UDSM devono essere selezionabili solo i seguenti Tipo Misura
				+ " rv_low_value in ('02','08','17','18') ORDER BY RV_MEANING ";

		setStatement(lStatement);
	}

	/**
	 * MEV10-s3: aggiunto metodo per estrazione dati condizionati dal parametro di passaggio
	 * 
	 * @param codice
	 */
	public void ricercaDecodificheTipoMSMinorenniByNatura(String codice) {
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'TIPO_MISURA_SICUREZZA' AND "
				// + " rv_low_value in ('02','08','13','14','17','18') AND RV_ABBREVIATION = '" + codice
				// Per gli utenti UDSM devono essere selezionabili solo i seguenti Tipo Misura
				+ " rv_low_value in ('02','08','17','18') AND RV_ABBREVIATION = '" + codice
				+ "' ORDER BY RV_MEANING ";
		setStatement(lStatement);
	}

	/**
	 * Lista Contenuti SIGE. La funzione restituisce l'elenco di tutti i Contenuti SIGE
	 * <p>
	 * 
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void listaContenutiSige() throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT  distinct";
		lStatement += " CG_RC1.RV_HIGH_VALUE COD_CONTENUTO, CG_RC2.RV_MEANING DESC_CONTENUTO";
		lStatement += " FROM CG_REF_CODES CG_RC1 join CG_REF_CODES CG_RC2 on  (CG_RC1.RV_HIGH_VALUE = CG_RC2.RV_LOW_VALUE AND CG_RC2.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')";
		lStatement += " WHERE CG_RC1.RV_DOMAIN = 'OGGETTO_SIGE' ";
		lStatement += " ORDER BY DESC_CONTENUTO, COD_CONTENUTO";

		setStatement(lStatement);
	}

	/**
	 * Lista Oggetti SIGE per Contenuto. La funzione restituisce l'elenco di tutti gli Oggetti SIGE per
	 * Contenuto
	 * <p>
	 * 
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void listaOggettiSigePerContenuto(String codContenuto) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT CG_RC1.RV_LOW_VALUE COD_OGGETTO, CG_RC1.RV_MEANING DESC_OGGETTO, CG_RC1.RV_ABBREVIATION ABBR_OGGETTO,";
		lStatement += " CG_RC1.RV_HIGH_VALUE COD_CONTENUTO, CG_RC2.RV_MEANING DESC_CONTENUTO";
		lStatement += " FROM CG_REF_CODES CG_RC1 join CG_REF_CODES CG_RC2 on  (CG_RC1.RV_HIGH_VALUE = CG_RC2.RV_LOW_VALUE AND CG_RC2.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')";
		lStatement += " WHERE CG_RC1.RV_DOMAIN = 'OGGETTO_SIGE' ";
		lStatement += " AND CG_RC1.RV_HIGH_VALUE = '" + codContenuto + "'";

		lStatement += " ORDER BY DESC_CONTENUTO, COD_CONTENUTO, CG_RC1.RV_LOW_VALUE ";

		setStatement(lStatement);
	}

	/**
	 * Recupera il campo RV_MEANING nella tabella CG_REF_CODES corrispondente al codOggettoSige passato in
	 * ingresso
	 * 
	 * @param codOggettoSige
	 *            codice oggetto sige
	 */
	public void ricercaDescrByCodOggettoSige(String codOggettoSige) {
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'OGGETTO_SIGE' AND "
				+ " RV_LOW_VALUE = '" + codOggettoSige + "'";

		setStatement(lStatement);

	}
	
	 /**
	 * Ricerca tutte le autorità escludendo quelle passate nella lista listaNotInValue
	 * //intervento post COLLAUDO 11.2 
	 * 
	 * @throws DAOException
	 */
	public void  ricercaAllTipoAutoritaNotIn(String [] listaNotInValue) throws  DAOException {
		String lStatement = " SELECT * FROM CG_REF_CODES  WHERE " + " RV_DOMAIN = 'TIPO_AUTORITA' ";
	  
		if(listaNotInValue!=null && listaNotInValue.length>0){
		   lStatement += " AND RV_LOW_VALUE NOT IN ( ";
		  // List<String>listValue =  (ArrayList<String>) Arrays.asList(listaNotInValue);
		   List<String> listValue = Arrays.asList(listaNotInValue);

		   Iterator<String> lItx = listValue.iterator();
			while (lItx.hasNext()) {
				lStatement += " '" + (String) lItx.next() + "'";
				if(lItx.hasNext())
					lStatement += " ," ;				
			}			
			lStatement += " ) ORDER BY RV_MEANING ";
		}		
		setStatement(lStatement);
	  }
	

	/**
	 * @param lCodOggetto
	 * @param lCodEsito
	 * 
	 * emma: 28/08/2018 : intervento post-collaudo
	 */
	public void ricercaEsitiCompatibiliByEsitoOggettoU023(String lCodOggetto,  String lCodEsito) {
		String[]esitiConMS = new String[]{"0051", "0052", "0193", "0194", "0195", "0351"};
		String[]esitiSenzaMS = new String[]{"0002", "0003", "0004", "0005",  "0350", "0387"};
		
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'ESITO_TENORE' AND "
				+ " RV_HIGH_VALUE IN (SELECT  RV_HIGH_VALUE FROM CG_REF_CODES WHERE "
				+ " RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND RV_LOW_VALUE='" + lCodOggetto + "')  AND "
				+ " RV_ALT2_VALUE IN (SELECT  RV_ALT2_VALUE FROM CG_REF_CODES WHERE "
				+ " RV_DOMAIN = 'ESITO_TENORE' AND RV_ABBREVIATION ='" + lCodEsito + "')";
		
		if(Arrays.binarySearch(esitiConMS, lCodEsito) >= 0){
			lStatement += " MINUS SELECT *  FROM CG_REF_CODES a"
					+ " WHERE RV_DOMAIN = 'ESITO_TENORE'"
					+ " AND a.rv_high_value = 'U023'"
					+ " and a.rv_low_value in ('1207', '1203', '1193', '1192', '2751', '2722') ";

			
		}
		else if(Arrays.binarySearch(esitiSenzaMS, lCodEsito) >= 0){
			
			lStatement += " MINUS SELECT *  FROM CG_REF_CODES a"
					+ " WHERE RV_DOMAIN = 'ESITO_TENORE'"
					+ " AND a.rv_high_value = 'U023'"
					+ " and a.rv_low_value in ('1190', '1191', '1991', '1992', '1993', '2720') ";
			
			
		}
		setStatement(lStatement);
	}

	
	/**
	 * Recupera il campo RV_MEANING nella tabella CG_REF_CODES corrispondente al codContenutoSige passato in
	 * ingresso
	 * 
	 * @param codContenutoSige
	 *            codice contenuto sige
	 */
	public void ExRicercaDescrByCodContenutoSige(String codContenutoSige) {
		String lStatement = " SELECT * FROM CG_REF_CODES WHERE " + " RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' AND "
				+ " RV_LOW_VALUE = '" + codContenutoSige + "'";

		setStatement(lStatement);

	}

}