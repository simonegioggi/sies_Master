package siap.sige.avvocato.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.StringUtils;
import siap.dao.SIAPSqlDAO;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;

public class AvvocatoSqlDAO extends SIAPSqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public AvvocatoSqlDAO(Connection con) {
		super(con);
	}

	//
	// Metodo di Ricerca Avvocati
	//

	public void ricercaAvvocato(AvvocatoModel aModel, AvvocatoFascicoloSigeModel aFModel)
			throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE," + "PROVINCIA," + "AVVOCATO.CAP,"
				+ "FLAG_VISUALIZZA," + "FLAG_CANCELLATO," + "ID_AVVOCATO_STANDARD," + "COD_COMUNE_RESIDENZA, "
				+ "COD_LUOGO_NASCITA, " + "DATA_NASCITA, " + "COD_NON_ATTIVITA, "
				+ "COD_UFFICIO_APPARTENENZA, " + "AVVOCATO.COD_OPERATORE_INSERIMENTO, "
				+ "AVVOCATO.DATA_INSERIMENTO, " + "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVVOCATO.DATA_AGGIORNAMENTO, " + "AVVTIPODESC.RV_MEANING DESCRTIPO ";
		lStatement += " FROM AVVOCATO,AVVOCATO_FASCICOLO_SIGE,CG_REF_CODES AVVTIPODESC";
		lStatement += " WHERE ";
		lStatement += " AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIGE.COD_TIPO_AVVOCATO AND ";
		lStatement += " FLAG_VISUALIZZA = 1 AND ";
		lStatement += " AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' ";

		lStatement += " " + setCondizione(aModel, aFModel);

		setStatement(lStatement);
	}

	//
	// METODO RICERCA() per avvocati SIEP
	//

	public void ricercaAvvocatobyKey(BigDecimal aIdAvvocato) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE, " + "PROVINCIA, " + "AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, " + "AVVOCATO.NOTE NOTEAVV, " + "AVVOCATO.COD_OPERATORE_INSERIMENTO, "
				+ "AVVOCATO.DATA_INSERIMENTO, " + "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVVOCATO.COD_UFFICIO_INSERIMENTO, " + "AVVOCATO.DATA_AGGIORNAMENTO, "
				+ "AVVOCATO.DATA_SOSPESO_FINO_AL, " + "AVVOCATO.DATA_RADIATO_DAL, "
				+ "AVVOCATO.COD_NON_ATTIVITA, " + "AVVOCATO.COD_LUOGO_NASCITA, "
				+ "AVVOCATO.COD_COMUNE_RESIDENZA, " + "AVVOCATO.FLAG_CANCELLATO, "
				+ "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, " + "AVVOCATO.DATA_NASCITA, "
				+ "AVVOCATO.COD_UFFICIO_APPARTENENZA, " + "COGNOME  DESCRTIPO, " + "ID_AVVOCATO_STANDARD";

		lStatement += " FROM AVVOCATO, COMUNE DESNASCITA,COMUNE DESCR";
		lStatement += " WHERE ";
		lStatement += " ID_AVVOCATO=" + aIdAvvocato;
		lStatement += " AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA ";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA ";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lStatementa = " + lStatement);
		setStatement(lStatement);
	}

	/*************************** Ricerca Avvocato Siep ******************************************/

	/**
	 *
	 * @param aModel
	 * @param iKey
	 * @throws DAOException
	 */
	public void ricercaAvvocatoSiep(AvvocatoModel aModel, BigDecimal iKey) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "AVV.ID_AVVOCATO ID_AVVOCATO, " + "AVV.COGNOME COGNOME, "
				+ "AVV.NOME NOME, " + "AVV.FORO FORO, " + "AVV.INDIRIZZO INDIRIZZO, "
				+ "AVV.TELEFONO TELEFONO, " + "AVV.FAX  FAX, " + "AVV.E_MAIL E_MAIL, " + "COD_FISCALE,"
				+ "PROVINCIA," + "AVV.CAP," + "FLAG_VISUALIZZA," + "FLAG_CANCELLATO,"
				+ "ID_AVVOCATO_STANDARD," + "COD_COMUNE_RESIDENZA, " + "COD_LUOGO_NASCITA, "
				+ "DATA_NASCITA, " + "COD_NON_ATTIVITA, " + "COD_UFFICIO_APPARTENENZA, "
				+ "AVV.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, "
				+ "AVV.DATA_INSERIMENTO DATA_INSERIMENTO, "
				+ "AVV.COD_OPERATORE_AGGIORNAMENTO COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVV.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO, " + "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, " + "AVVTIPODESC.RV_MEANING DESCRTIPO ";
		lStatement += " FROM AVVOCATO AVV, AVVOCATO_FASCICOLO_SIEP AFS, CG_REF_CODES AVVTIPODESC, COMUNE DESNASCITA,COMUNE DESCR";
		lStatement += " WHERE ";
		lStatement += " AFS.DATA_FINE_VALIDITA is null AND ";
		lStatement += " AFS.AVV_ID_AVVOCATO = AVV.ID_AVVOCATO AND  ";
		lStatement += " AFS.FAS_SIE_ID_FASCICOLO_SIEP = " + iKey + " AND ";
		lStatement += " AVVTIPODESC.RV_LOW_VALUE=AFS.COD_TIPO_AVVOCATO AND ";
		lStatement += " FLAG_VISUALIZZA = 1 AND ";
		lStatement += " AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' AND ";
		lStatement += " DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA AND";
		lStatement += " DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA ";

		lStatement += " " + setCondizione(aModel, true);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lStatement0 = " + lStatement);
		setStatement(lStatement);
	}

	//
	// METODO RICERCA() per avvocati SIGE
	//

	/*************************** Ricerca Avvocato Sige ******************************************/

	/**
	 *
	 * @param aModel
	 * @throws DAOException
	 */

	public void ricercaAvvocatoSige(AvvocatoFascicoloSigeModel aModel) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "AFS.ID_AVVOCATO_FASCICOLO_SIGE ID_AVVOCATO, " + "AVV.COGNOME COGNOME, "
				+ "AVV.NOME NOME, " + "AVV.FORO FORO, " + "AVV.INDIRIZZO INDIRIZZO, "
				+ "AVV.TELEFONO TELEFONO, " + "AVV.FAX  FAX, " + "AVV.E_MAIL E_MAIL, " + "COD_FISCALE,"
				+ "PROVINCIA," + "AVV.CAP," + "FLAG_VISUALIZZA," + "FLAG_CANCELLATO,"
				+ "ID_AVVOCATO_STANDARD," + "COD_COMUNE_RESIDENZA, " + "COD_LUOGO_NASCITA, "
				+ "DATA_NASCITA, " + "COD_NON_ATTIVITA, " + "COD_UFFICIO_APPARTENENZA, "
				+ "AVV.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, "
				+ "AVV.DATA_INSERIMENTO DATA_INSERIMENTO, "
				+ "AVV.COD_OPERATORE_AGGIORNAMENTO COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVV.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO, " + "AVVTIPODESC.RV_MEANING DESCRTIPO ";
		lStatement += " FROM AVVOCATO AVV, AVVOCATO_FASCICOLO_SIGE AFS, CG_REF_CODES AVVTIPODESC ";
		lStatement += " WHERE ";
		lStatement += " AFS.DATA_FINE_VALIDITA is null AND ";
		lStatement += " AFS.AVV_ID_AVVOCATO = AVV.ID_AVVOCATO AND  ";
		lStatement += " AVVTIPODESC.RV_LOW_VALUE=AFS.COD_TIPO_AVVOCATO AND ";
		lStatement += " AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' AND";
		lStatement += " FLAG_VISUALIZZA = 1 ";

		lStatement += " " + setCondizioneAvvocatoSige(aModel);

		setStatement(lStatement);
	}

	/**
	 *
	 * @param aModel
	 * @param aFModel
	 * @throws DAOException
	 */

	public void ricercaAvvocatoAttualeFascicolo(AvvocatoModel aModel, AvvocatoFascicoloSigeModel aFModel)
			throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE," + "PROVINCIA," + "AVVOCATO.CAP,"
				+ "FLAG_VISUALIZZA," + "FLAG_CANCELLATO," + "ID_AVVOCATO_STANDARD," + "COD_COMUNE_RESIDENZA, "
				+ "COD_LUOGO_NASCITA, " + "DATA_NASCITA, " + "COD_NON_ATTIVITA, "
				+ "COD_UFFICIO_APPARTENENZA, " + "AVVOCATO.COD_OPERATORE_INSERIMENTO, "
				+ "AVVOCATO.DATA_INSERIMENTO, " + "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVVOCATO.DATA_AGGIORNAMENTO, " + "AVVTIPODESC.RV_MEANING DESCRTIPO, "
				+ "AVVOCATO_FASCICOLO_SIGE.COD_TIPO_AVVOCATO";
		lStatement += " FROM AVVOCATO,AVVOCATO_FASCICOLO_SIGE,CG_REF_CODES AVVTIPODESC";
		lStatement += " WHERE ";
		lStatement += " DATA_FINE_VALIDITA IS NULL AND ";
		lStatement += " AVVOCATO.ID_AVVOCATO=AVVOCATO_FASCICOLO_SIGE.AVV_ID_AVVOCATO AND ";
		lStatement += " AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIGE.COD_TIPO_AVVOCATO AND ";
		lStatement += " FLAG_VISUALIZZA = 1 AND ";
		lStatement += " AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' ";

		lStatement += " " + setCondizione(aModel, aFModel);

		// Gli avvocati sono ordinati in ordine crescente di ID_AVVOCATO
		lStatement += " ORDER BY ID_AVVOCATO ASC";

		setStatement(lStatement);
	}

	/**
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaAvvocato(AvvocatoModel aModel) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE," + "PROVINCIA," + "AVVOCATO.CAP,"
				+ "FLAG_VISUALIZZA," + "FLAG_CANCELLATO," + "ID_AVVOCATO_STANDARD,"
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_APPARTENENZA, " + "COD_COMUNE_RESIDENZA, "
				+ "COD_LUOGO_NASCITA, " + "DATA_NASCITA, " + "COD_NON_ATTIVITA, " + "COGNOME  DESCRTIPO ";
		lStatement += " FROM AVVOCATO ";
		lStatement += " WHERE ";
		lStatement += " " + setCondizione(aModel, false);

		lStatement += " ORDER BY COGNOME,NOME ASC";

		setStatement(lStatement);
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		AvvocatoModel aModel = new AvvocatoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdAvvocato(getBigDecimal("ID_AVVOCATO"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setForo(getString("FORO"));
		aModel.setIndirizzo(getString("INDIRIZZO"));
		aModel.setTelefono(getString("TELEFONO"));
		aModel.setFax(getString("FAX"));
		aModel.setEMail(getString("E_MAIL"));
		aModel.setCodComuneResidenza(getString("COD_COMUNE_RESIDENZA"));
		aModel.setCodLuogoNascita(getString("COD_LUOGO_NASCITA"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setCodNonAttivita(getString("COD_NON_ATTIVITA"));
		aModel.setCodUffAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setDescrTipo(getString("DESCRTIPO"));
		aModel.setCodiceFiscale(getString("COD_FISCALE"));
		aModel.setProvincia(getString("PROVINCIA"));
		aModel.setCap(getString("CAP"));
		aModel.setFlagVisualizza(getBigDecimal("FLAG_VISUALIZZA"));
		aModel.setFlagCancellato(getString("FLAG_CANCELLATO"));
		aModel.setIdAvvocatoStandard(getBigDecimal("ID_AVVOCATO_STANDARD"));
		return aModel;
	}

	public GenericModel getModelSiep() throws DAOException {
		AvvocatoModel aModel = new AvvocatoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdAvvocato(getBigDecimal("ID_AVVOCATO"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setForo(getString("FORO"));
		aModel.setIndirizzo(getString("INDIRIZZO"));
		aModel.setTelefono(getString("TELEFONO"));
		aModel.setFax(getString("FAX"));
		aModel.setEMail(getString("E_MAIL"));
		aModel.setCodComuneResidenza(getString("COD_COMUNE_RESIDENZA"));
		aModel.setCodLuogoNascita(getString("COD_LUOGO_NASCITA"));
		aModel.setDescLuogoNascita(getString("DESCR_LUOGO_NASCITA"));
		aModel.setDescComuneResidenza(getString("DESCR_COMUNE_RESIDENZA"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setCodNonAttivita(getString("COD_NON_ATTIVITA"));
		aModel.setCodUffAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setDescrTipo(getString("DESCRTIPO"));
		aModel.setCodiceFiscale(getString("COD_FISCALE"));
		aModel.setProvincia(getString("PROVINCIA"));
		aModel.setCap(getString("CAP"));
		aModel.setFlagVisualizza(getBigDecimal("FLAG_VISUALIZZA"));
		aModel.setFlagCancellato(getString("FLAG_CANCELLATO"));
		aModel.setIdAvvocatoStandard(getBigDecimal("ID_AVVOCATO_STANDARD"));
		return aModel;
	}

	private String setCondizione(AvvocatoModel aModel, AvvocatoFascicoloSigeModel aFModel) {
		String lCondizioni = new String();
		if (aModel != null) {
			if (aModel.getIdAvvocato() != null)
				lCondizioni = " AND ID_AVVOCATO=" + aModel.getIdAvvocato();
			if (aModel.getCognome() != null && aModel.getCognome() != "")
				lCondizioni += " AND COGNOME LIKE '" + aModel.getCognome().toUpperCase() + "%'";
		}

		if (aFModel != null) {
			if (aFModel.getFasSigeIdFascicoloSige() != null)
				lCondizioni += " AND FAS_SIGE_ID_FASCICOLO_SIGE=" + aFModel.getFasSigeIdFascicoloSige();
		}
		return lCondizioni;
	}

	/**
	 * Setta le condizioni sullo statment
	 *
	 * metodo introdotto per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018 (errore
	 * prototipo PALERMO)
	 *
	 * @param idFascSige
	 * @return
	 */
	private String setCondizione(BigDecimal idFascSige) {
		String lCondizioni = new String();

		if (idFascSige != null) {
			lCondizioni += "  and AVVOCATO_FASCICOLO_SIGE.Avv_Id_Avvocato=avvocato.id_avvocato ";

			lCondizioni += " AND FAS_SIGE_ID_FASCICOLO_SIGE=" + idFascSige;
		}
		return lCondizioni;
	}

	private String setCondizione(AvvocatoModel aModel, boolean lInserito) {
		// boolean lInserito=false;
		String lCondizioni = new String();

		if (aModel.getIdAvvocato() != null) {
			if (lInserito)
				lCondizioni += " AND ";
			lCondizioni = "  ID_AVVOCATO=" + aModel.getIdAvvocato();
			lInserito = true;
		}
		if (aModel.getCognome() != null && aModel.getCognome() != "") {
			if (lInserito) {
				lCondizioni += " AND COGNOME LIKE '"
						+ StringUtils.convertSqlString(aModel.getCognome().toUpperCase()) + "%'";
			} else
				lCondizioni += " COGNOME LIKE '"
						+ StringUtils.convertSqlString(aModel.getCognome().toUpperCase()) + "%'";
		}
		if (aModel.getForo() != null && aModel.getForo() != "") {
			if (lInserito)
				lCondizioni += " AND ";
			lCondizioni += " FORO LIKE '" + StringUtils.convertSqlString(aModel.getForo().toUpperCase())
					+ "%'";
		}

		// lCondizioni += " ORDER BY COGNOME";
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lCondizioni0 = " + lCondizioni);
		return lCondizioni;
	}

	private String setCondizioneAvvocatoSige(AvvocatoFascicoloSigeModel aModel) {
		String lCondizioni = new String();

		if (aModel.getFasSigeIdFascicoloSige() != null) {
			lCondizioni += " AND FAS_SIGE_ID_FASCICOLO_SIGE = " + aModel.getFasSigeIdFascicoloSige();
		}

		lCondizioni += " ORDER BY COGNOME";

		return lCondizioni;
	}

	public void ricercaForo() throws DAOException {
		String lStatement = new String("");
		lStatement += " select distinct(FORO) from AVVOCATO  where FLAG_VISUALIZZA=1";
		lStatement += " order by FORO asc";
		setStatement(lStatement);
	}

	public GenericModel getModelForo() throws DAOException {
		AvvocatoModel aModel = new AvvocatoModel();
		// Inserire le opportune set delle descrizioni!
		aModel.setForo(getString("FORO"));
		return aModel;
	}

	/**
	 * Il metodo ricerca gli avvocati difensori di uno specifico fascicolo sige
	 *
	 * metodo introdotto per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018 (errore
	 * prototipo PALERMO)
	 *
	 * @param idFascSige
	 * @throws DAOException
	 */
	public void ricercaAvvocatoByIDFascicoloSige(BigDecimal idFascSige) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE," + "PROVINCIA," + "AVVOCATO.CAP,"
				+ "FLAG_VISUALIZZA," + "FLAG_CANCELLATO," + "ID_AVVOCATO_STANDARD," + "COD_COMUNE_RESIDENZA, "
				+ "COD_LUOGO_NASCITA, " + "DATA_NASCITA, " + "COD_NON_ATTIVITA, "
				+ "COD_UFFICIO_APPARTENENZA, " + "AVVOCATO.COD_OPERATORE_INSERIMENTO, "
				+ "AVVOCATO.DATA_INSERIMENTO, " + "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVVOCATO.DATA_AGGIORNAMENTO, " + "AVVTIPODESC.RV_MEANING DESCRTIPO ";
		lStatement += " FROM AVVOCATO,AVVOCATO_FASCICOLO_SIGE,CG_REF_CODES AVVTIPODESC";
		lStatement += " WHERE ";
		lStatement += " AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIGE.COD_TIPO_AVVOCATO AND ";
		lStatement += " FLAG_VISUALIZZA = 1 AND ";
		lStatement += " AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' ";

		lStatement += " " + setCondizione(idFascSige);

		setStatement(lStatement);
	}

	// MEV_21: aggiunto metodo di ricerca avvocato certificato reginde
	public void ricercaAvvocatoCertRegInde(AvvocatoModel lAvvMod) throws DAOException {

		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE, " + "PROVINCIA, " + "AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, " + "PEC, " + "FLAG_REGINDE, " + "DESCR_COMUNE_STUDIO, "
				+ "COD_STATO_NASCITA_AVV, " + "DESC_LUOGO_NAS_REGINDE, " + "ID_AVVOCATO_BONIFICATO, "
				+ "AVVOCATO.NOTE NOTEAVV, " + "AVVOCATO.COD_OPERATORE_INSERIMENTO, "
				+ "AVVOCATO.DATA_INSERIMENTO, " + "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVVOCATO.COD_UFFICIO_INSERIMENTO, " + "AVVOCATO.DATA_AGGIORNAMENTO, "
				+ "AVVTIPODESC.RV_MEANING DESCRTIPO, " + "AVVOCATO.DATA_SOSPESO_FINO_AL, "
				+ "AVVOCATO.DATA_RADIATO_DAL, " + "AVVOCATO.COD_NON_ATTIVITA, "
				+ "AVVOCATO.COD_LUOGO_NASCITA, " + "AVVOCATO.COD_COMUNE_RESIDENZA, "
				+ "AVVOCATO.FLAG_CANCELLATO, " + "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, " + "AVVOCATO.DATA_NASCITA, "
				+ "AVVOCATO.COD_UFFICIO_APPARTENENZA, " + "CG.RV_MEANING DESCR_NON_ATTIVITA, "
				+ "ID_AVVOCATO_STANDARD, " + "AVVOCATO_FASCICOLO_SIGE.DATA_INIZIO_VALIDITA";

		lStatement += " FROM AVVOCATO, AVVOCATO_FASCICOLO_SIGE, CG_REF_CODES AVVTIPODESC, CG_REF_CODES CG, COMUNE DESNASCITA, COMUNE DESCR";
		lStatement += " WHERE";
		lStatement += " FLAG_REGINDE='SI'";
		lStatement += " AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIGE.COD_TIPO_AVVOCATO";
		lStatement += " AND AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO'";
		lStatement += " AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA'";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA";
		lStatement += " AND NOME = '" + StringUtils.convertSqlString(lAvvMod.getNome().toUpperCase()) + "'";
		lStatement += " AND COGNOME = '" + StringUtils.convertSqlString(lAvvMod.getCognome().toUpperCase())
				+ "'";
		lStatement += " AND COD_FISCALE = '"
				+ StringUtils.convertSqlString(lAvvMod.getCodiceFiscale().toUpperCase()) + "'";

		setStatement(lStatement);
	}

}