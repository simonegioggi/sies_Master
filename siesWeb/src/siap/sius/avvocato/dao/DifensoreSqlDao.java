package siap.sius.avvocato.dao;

import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.StringUtils;
import siap.dao.SIAPSqlDAO;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;

public class DifensoreSqlDao extends SIAPSqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public DifensoreSqlDao(Connection con) {
		super(con);
	}

	public void ricercaDifensoreAttualeFascicolo(AvvocatoModel aModel, AvvocatoFascicoloSiusModel aFModel)
			throws DAOException {

		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE," + "PROVINCIA," + "AVVOCATO.CAP,"
				+ "FLAG_VISUALIZZA," + "ID_AVVOCATO_STANDARD," + "AVVOCATO.NOTE NOTEAVV, "
				+ "AVVOCATO.COD_OPERATORE_INSERIMENTO, " + "AVVOCATO.DATA_INSERIMENTO, "
				+ "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, " + "AVVOCATO.COD_UFFICIO_INSERIMENTO, "
				+ "AVVOCATO.DATA_AGGIORNAMENTO, " + "AVVTIPODESC.RV_MEANING DESCRTIPO, "
				+ "AVVOCATO.DATA_SOSPESO_FINO_AL, " + "AVVOCATO.DATA_RADIATO_DAL, "
				+ "AVVOCATO.COD_NON_ATTIVITA, " + "AVVOCATO.COD_LUOGO_NASCITA, "
				+ "AVVOCATO.COD_COMUNE_RESIDENZA, " + "AVVOCATO.FLAG_CANCELLATO, "
				+ "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, " + "AVVOCATO.COD_UFFICIO_APPARTENENZA, "
				+ "AVVOCATO.DATA_NASCITA, " + "CG.RV_MEANING DESCR_NON_ATTIVITA, "
				// MEV_21: aggiunti nuovi campi 
				+ "PEC, FLAG_REGINDE, DESCR_COMUNE_STUDIO, DESC_LUOGO_NAS_REGINDE, COD_STATO_NASCITA_AVV, ID_AVVOCATO_BONIFICATO, "
				// MEV_21: FINE 
				+ "AVVOCATO_FASCICOLO_SIUS.DATA_INIZIO_VALIDITA ";
		lStatement += " FROM AVVOCATO,AVVOCATO_FASCICOLO_SIUS,CG_REF_CODES AVVTIPODESC,CG_REF_CODES CG, COMUNE DESNASCITA,COMUNE DESCR";
		lStatement += " WHERE ";
		lStatement += " DATA_FINE_VALIDITA IS NULL AND ";
		lStatement += " AVVOCATO.ID_AVVOCATO=AVVOCATO_FASCICOLO_SIUS.AVV_ID_AVVOCATO AND ";
		lStatement += " AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIUS.COD_TIPO_AVVOCATO ";
		lStatement += " AND AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' ";
		lStatement += "AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA ";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA ";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA' ";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA ";
		// lStatement += " AND FLAG_VISUALIZZA = 1 ";

		lStatement += " " + setCondizione(aModel, aFModel);

		// Gli avvocati sono ordinati in ordine crescente di ID_AVVOCATO
		lStatement += " ORDER BY ID_AVVOCATO_FASCICOLO_SIUS ASC";

		setStatement(lStatement);
	}

	/**
	 * MEV_21 il metodo è stato riscritto per reuperare sologli avvocati certificati REGINDE
	 * su tutta la tabella AVVOCATO indipendentemente dell'ufficio appartenenza non più pertinente
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaDifensore(AvvocatoModel aModel) throws DAOException {

		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE," + "PROVINCIA," + "AVVOCATO.CAP,"
				+ "FLAG_VISUALIZZA," + "ID_AVVOCATO_STANDARD," + "AVVOCATO.NOTE NOTEAVV, "
				+ "AVVOCATO.COD_OPERATORE_INSERIMENTO, " + "AVVOCATO.DATA_INSERIMENTO, "
				+ "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, " + "AVVOCATO.COD_UFFICIO_INSERIMENTO, "
				+ "AVVOCATO.DATA_AGGIORNAMENTO, " + "COGNOME  DESCRTIPO, " + "AVVOCATO.DATA_SOSPESO_FINO_AL, "
				+ "AVVOCATO.DATA_RADIATO_DAL, " + "AVVOCATO.COD_NON_ATTIVITA, "
				+ "AVVOCATO.COD_LUOGO_NASCITA, " + "AVVOCATO.COD_COMUNE_RESIDENZA, "
				+ "AVVOCATO.FLAG_CANCELLATO, " + "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, " + "AVVOCATO.COD_UFFICIO_APPARTENENZA, "
				+ "AVVOCATO.DATA_NASCITA, " + "CG.RV_MEANING DESCR_NON_ATTIVITA, "
				// MEV_21: aggiunti nuovi campi 
				+ "PEC, FLAG_REGINDE, DESCR_COMUNE_STUDIO, DESC_LUOGO_NAS_REGINDE, COD_STATO_NASCITA_AVV, ID_AVVOCATO_BONIFICATO, "
				// MEV_21: FINE 
				+ "DATA_INSERIMENTO DATA_INIZIO_VALIDITA ";
		lStatement += " FROM AVVOCATO,CG_REF_CODES CG, COMUNE DESNASCITA,COMUNE DESCR";
		lStatement += " WHERE ";
		lStatement += " DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA ";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA ";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA' ";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA ";
		lStatement += " AND FLAG_REGINDE = 'SI' ";
		
		lStatement += " " + setCondizione(aModel);

		lStatement += " ORDER BY COGNOME,NOME ASC";

		siesLogger.debug(" lStatement = " + lStatement);

		setStatement(lStatement);
	}
	
	// MEV_21 vecchio metodo che effettuava la ricerca degli avvocati SIUE dell'ufficio.
	// Riscritto per recuperare solo gli avvocati certificati REGINDE
//	public void ricercaDifensore(AvvocatoModel aModel) throws DAOException {
//
//		String lStatement = new String("");
//
//		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
//				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE," + "PROVINCIA," + "AVVOCATO.CAP,"
//				+ "FLAG_VISUALIZZA," + "ID_AVVOCATO_STANDARD," + "AVVOCATO.NOTE NOTEAVV, "
//				+ "AVVOCATO.COD_OPERATORE_INSERIMENTO, " + "AVVOCATO.DATA_INSERIMENTO, "
//				+ "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, " + "AVVOCATO.COD_UFFICIO_INSERIMENTO, "
//				+ "AVVOCATO.DATA_AGGIORNAMENTO, " + "COGNOME  DESCRTIPO, " + "AVVOCATO.DATA_SOSPESO_FINO_AL, "
//				+ "AVVOCATO.DATA_RADIATO_DAL, " + "AVVOCATO.COD_NON_ATTIVITA, "
//				+ "AVVOCATO.COD_LUOGO_NASCITA, " + "AVVOCATO.COD_COMUNE_RESIDENZA, "
//				+ "AVVOCATO.FLAG_CANCELLATO, " + "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
//				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, " + "AVVOCATO.COD_UFFICIO_APPARTENENZA, "
//				+ "AVVOCATO.DATA_NASCITA, " + "CG.RV_MEANING DESCR_NON_ATTIVITA, "
//				+ "DATA_INSERIMENTO DATA_INIZIO_VALIDITA ";
//		lStatement += " FROM AVVOCATO,CG_REF_CODES CG, COMUNE DESNASCITA,COMUNE DESCR";
//		lStatement += " WHERE ";
//		lStatement += " DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA ";
//		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA ";
//		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA' ";
//		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA ";
//		lStatement += " " + setCondizione(aModel);
//		int lPos = lStatement.indexOf("WHERE");
//		String lSql1 = lStatement.substring(0, lPos);
//		String lSql2 = lStatement.substring(lPos + 5, lStatement.length());
//		lStatement += " MINUS " + lSql1;
//		lStatement += " where cod_ufficio_appartenenza='00000' and id_avvocato_standard in ";
//		// 11/12/2007 lStatement += " (select id_avvocato_standard from avvocato where flag_visualizza = 1 and
//		// FLAG_CANCELLATO ='N' ";
//		lStatement += " (select id_avvocato_standard from avvocato where flag_visualizza = 1 ";
//		// 20191028 [SG]: aggiunta and condition
//		lStatement += "AND FLAG_CANCELLATO = 'N'";
//		lStatement += " and cod_ufficio_appartenenza = '"
//				+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase());
//		lStatement += "') and " + lSql2;
//		lStatement += " ORDER BY COGNOME,NOME ASC";
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug(" lStatement1 = " + lStatement);
//
//		setStatement(lStatement);
//	}	
	

	public GenericModel getModel() throws DAOException {

		AvvocatoSiusModel aModel = new AvvocatoSiusModel();

		// Inserire le opportune set delle descrizioni!
		aModel.getAvvocato().setIdAvvocato(getBigDecimal("ID_AVVOCATO"));
		aModel.getAvvocato().setCognome(getString("COGNOME"));
		aModel.getAvvocato().setNome(getString("NOME"));
		aModel.getAvvocato().setForo(getString("FORO"));
		aModel.getAvvocato().setIndirizzo(getString("INDIRIZZO"));
		aModel.getAvvocato().setTelefono(getString("TELEFONO"));
		aModel.getAvvocato().setFax(getString("FAX"));
		aModel.getAvvocato().setEMail(getString("E_MAIL"));
		aModel.getAvvocato().setNote(getString("NOTEAVV"));
		aModel.getAvvocato().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.getAvvocato().setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.getAvvocato().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.getAvvocato().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.getAvvocato().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.getAvvocato().setDescrTipo(getString("DESCRTIPO"));
		aModel.getAvvocato().setCodLuogoNascita(getString("COD_LUOGO_NASCITA"));
		aModel.getAvvocato().setDescLuogoNascita(getString("DESCR_LUOGO_NASCITA"));
		aModel.getAvvocato().setDescrNonAttivita(getString("DESCR_NON_ATTIVITA"));
		aModel.getAvvocato().setDescComuneResidenza(getString("DESCR_COMUNE_RESIDENZA"));
		aModel.getAvvocato().setCodComuneResidenza(getString("COD_COMUNE_RESIDENZA"));
		aModel.getAvvocato().setDataNascita(getDate("DATA_NASCITA"));
		aModel.getAvvocato().setDataSospensione(getDate("DATA_SOSPESO_FINO_AL"));
		aModel.getAvvocato().setDataRadiazione(getDate("DATA_RADIATO_DAL"));
		aModel.getAvvocato().setCodNonAttivita(getString("COD_NON_ATTIVITA"));
		aModel.getAvvocato().setCodUffAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
		aModel.getAvvocatoFascicoloSiusModel().setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.getAvvocato().setFlagCancellato(getString("FLAG_CANCELLATO"));
		aModel.getAvvocato().setCodiceFiscale(getString("COD_FISCALE"));
		aModel.getAvvocato().setProvincia(getString("PROVINCIA"));
		aModel.getAvvocato().setCap(getString("CAP"));
		aModel.getAvvocato().setFlagVisualizza(getBigDecimal("FLAG_VISUALIZZA"));
		aModel.getAvvocato().setIdAvvocatoStandard(getBigDecimal("ID_AVVOCATO_STANDARD"));
		
		// MEV_21: nuovi campi
		//aModel.setDescComuneSedeForo(getString("DescComuneSedeForo"));	
		aModel.getAvvocato().setPec                     (getString("PEC"));
		aModel.getAvvocato().setFlagRegInde             (getString("FLAG_REGINDE"));
		aModel.getAvvocato().setDescrComuneStudio       (getString("DESCR_COMUNE_STUDIO"));
		aModel.getAvvocato().setDescLuogoNascitaReginde (getString("DESC_LUOGO_NAS_REGINDE"));
		aModel.getAvvocato().setCodStatoNascita         (getString("COD_STATO_NASCITA_AVV"));
		//aModel.getAvvocato().setDescrStatoNascita       (getString("DescComuneSedeForo"));
		aModel.getAvvocato().setIdAvvocatoBonificato    (getBigDecimal("ID_AVVOCATO_BONIFICATO"));
	  // MEV_21: FINE
		
		return aModel;
	}

	private String setCondizione(AvvocatoModel aModel, AvvocatoFascicoloSiusModel aFModel) {

		String lCondizioni = new String();
		if (aModel != null) {
			if (aModel.getIdAvvocato() != null)
				lCondizioni = " AND ID_AVVOCATO=" + aModel.getIdAvvocato();
			if (aModel.getCognome() != null && aModel.getCognome() != "")
				lCondizioni += " AND COGNOME LIKE '" + aModel.getCognome().toUpperCase() + "%'";

		}

		if (aFModel != null) {
			if (aFModel.getFasSiuIdFascicoloSius() != null) {
				lCondizioni += " AND FAS_SIU_ID_FASCICOLO_SIUS=" + aFModel.getFasSiuIdFascicoloSius();
			} else {
				lCondizioni += " AND FLAG_VISUALIZZA = 1 ";
			}
		}
		// 11/12/2007 lCondizioni += " AND FLAG_CANCELLATO ='N'";

		return lCondizioni;
	}

	private String setCondizione(AvvocatoModel aModel) {

		String lCondizioni = new String();
		if (aModel.getIdAvvocato() != null) {
			lCondizioni = "  AND ID_AVVOCATO=" + aModel.getIdAvvocato();
		} else {
			if (aModel.getCognome() != null && !aModel.getCognome().equals("")) {
				lCondizioni += " AND COGNOME  LIKE '"
						+ StringUtils.convertSqlString(aModel.getCognome().toUpperCase()) + "%'";
			}

			if (aModel.getNome() != null && !aModel.getNome().equals("")) {
				lCondizioni += " AND NOME  LIKE '"
						+ StringUtils.convertSqlString(aModel.getNome().toUpperCase()) + "%'";
			}

			if (aModel.getForo() != null && !aModel.getForo().equals("")) {
				lCondizioni += " AND FORO LIKE '"
						+ StringUtils.convertSqlString(aModel.getForo().toUpperCase()) + "%'";
				;
			}
			if (aModel.getCodUffAppartenenza() != null && aModel.getCodUffAppartenenza() != "") {
				lCondizioni += " AND (COD_UFFICIO_APPARTENENZA ='"
						+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase()) + "'";
				lCondizioni += " OR COD_UFFICIO_APPARTENENZA ='00000' ";
				// lCondizioni += " OR COD_OPERATORE_INSERIMENTO like 'E%' or COD_OPERATORE_INSERIMENTO like
				// 'D%'";
				lCondizioni += ")";

			}

			// 11/12/2007 lCondizioni += " AND FLAG_CANCELLATO ='N' AND FLAG_VISUALIZZA = 1 ";
			lCondizioni += " AND FLAG_VISUALIZZA = 1 ";
			// 20191028 [SG]: aggiunta and condition
			lCondizioni += " AND FLAG_CANCELLATO = 'N' ";
		}

		return lCondizioni;
	}

}