package siap.siep.avvocato.dao;

/**
* <p>Title: AvvocatoSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;

public class AvvocatoSqlDAO extends SIAPSqlDAO {

	public AvvocatoSqlDAO(Connection con) {

		super(con);
	}

	/*****************************************************************************
	 * Imposta le condizioni di ricerca per tutti gli avvocati di un certo fascicolo. (anche quelle
	 * deassegnati). Condizioni per: id_fascicolo, cognome avvocato (like), id_avvocato, flag cancellato = N
	 *
	 * @param aModel
	 * @param aFModel
	 * @throws DAOException
	 */
	public void ricercaAvvocato(AvvocatoModel aModel, AvvocatoFascicoloSiepModel aFModel)
			throws DAOException {

		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE, " + "PROVINCIA, " + "AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, " // 20210610 MEV_21 (avvocati): aggiunti tre+tre campi in tabella
				+ "PEC, " + "FLAG_REGINDE, " + "DESCR_COMUNE_STUDIO, "
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
				+ "ID_AVVOCATO_STANDARD, " + "AVVOCATO_FASCICOLO_SIEP.DATA_INIZIO_VALIDITA";

		lStatement += " FROM AVVOCATO,AVVOCATO_FASCICOLO_SIEP,CG_REF_CODES AVVTIPODESC,CG_REF_CODES CG,COMUNE DESNASCITA,COMUNE DESCR";
		lStatement += " WHERE";
		lStatement += " AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIEP.COD_TIPO_AVVOCATO";
		lStatement += " AND AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO'";
		lStatement += " AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA'";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA";
		lStatement += " AND FLAG_VISUALIZZA = 1";
		lStatement += " " + setCondizione(aModel, aFModel);

		int lPos = lStatement.indexOf("WHERE");
		String lSql1 = lStatement.substring(0, lPos);
		String lSql2 = lStatement.substring(lPos + 5, lStatement.length());
		lStatement += " MINUS " + lSql1;
		lStatement += " where cod_ufficio_appartenenza='00000' and id_avvocato_standard in";
		lStatement += " (select id_avvocato_standard from avvocato where flag_visualizza = 1 and FLAG_CANCELLATO ='N'";
		lStatement += " and cod_ufficio_appartenenza = '"
				+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase());
		lStatement += "') and " + lSql2;
		lStatement += " ORDER BY COGNOME,NOME ASC";

		setStatement(lStatement);
	}

	public void ricercaAvvocatobyKey(BigDecimal aIdAvvocato) throws DAOException {

		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE, " + "PROVINCIA, " + "AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, " // 20210610 MEV_21 (avvocati): aggiunti tre+tre campi in tabella
				+ "PEC, " + "FLAG_REGINDE, " + "DESCR_COMUNE_STUDIO, "
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
				+ "ID_AVVOCATO_STANDARD, " + "AVVOCATO_FASCICOLO_SIEP.DATA_INIZIO_VALIDITA";

		lStatement += " FROM AVVOCATO,AVVOCATO_FASCICOLO_SIEP,CG_REF_CODES AVVTIPODESC,CG_REF_CODES CG, COMUNE DESNASCITA,COMUNE DESCR";
		lStatement += " WHERE";
		lStatement += " ID_AVVOCATO=" + aIdAvvocato;
		lStatement += " AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIEP.COD_TIPO_AVVOCATO";
		lStatement += " AND AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO'";
		lStatement += " AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA'";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA";

		setStatement(lStatement);
	}

	// 20210614  MEV_21 Ricerca Avvocato certificato RegInDE.
	public void ricercaAvvocatoCertRegInde(AvvocatoModel lAvvMod) throws DAOException {

		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE, " + "PROVINCIA, " + "AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, "
				+ "PEC, " + "FLAG_REGINDE, " + "DESCR_COMUNE_STUDIO, "
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
				+ "ID_AVVOCATO_STANDARD, " + "AVVOCATO_FASCICOLO_SIEP.DATA_INIZIO_VALIDITA";

		lStatement += " FROM AVVOCATO,AVVOCATO_FASCICOLO_SIEP,CG_REF_CODES AVVTIPODESC,CG_REF_CODES CG, COMUNE DESNASCITA,COMUNE DESCR";
		lStatement += " WHERE";
		lStatement += " FLAG_REGINDE='SI' ";
		lStatement += " AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIEP.COD_TIPO_AVVOCATO";
		lStatement += " AND AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO'";
		lStatement += " AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA'";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA";
		lStatement += " AND NOME = '"
						+ StringUtils.convertSqlString(lAvvMod.getNome().toUpperCase()) + "'";
		lStatement += " AND COGNOME = '"
						+ StringUtils.convertSqlString(lAvvMod.getCognome().toUpperCase()) + "'";
		lStatement += " AND COD_FISCALE = '"
						+ StringUtils.convertSqlString(lAvvMod.getCodiceFiscale().toUpperCase())+ "'";

		setStatement(lStatement);
	}
	
	public void ricercaAvvocatoAttualeFascicolo(AvvocatoModel aModel, AvvocatoFascicoloSiepModel aFModel)
			throws DAOException {

		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE, " + "PROVINCIA, " + "AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, " // 20210610 MEV_21 (avvocati): aggiunti tre+tre campi in tabella
				+ "PEC, " + "FLAG_REGINDE, " + "DESCR_COMUNE_STUDIO, "
				+ "COD_STATO_NASCITA_AVV, " + "DESC_LUOGO_NAS_REGINDE, " + "ID_AVVOCATO_BONIFICATO, "
				+ "AVVOCATO.NOTE NOTEAVV, " + "AVVOCATO.COD_OPERATORE_INSERIMENTO, "
				+ "AVVOCATO.DATA_INSERIMENTO, " + "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVVOCATO.COD_UFFICIO_INSERIMENTO, " + "AVVOCATO.DATA_AGGIORNAMENTO, "
				+ "AVVTIPODESC.RV_MEANING DESCRTIPO, " + "AVVOCATO.DATA_SOSPESO_FINO_AL, "
				+ "AVVOCATO.DATA_RADIATO_DAL, " + "AVVOCATO.COD_NON_ATTIVITA, "
				+ "AVVOCATO.COD_LUOGO_NASCITA, " + "AVVOCATO.COD_COMUNE_RESIDENZA, "
				+ "AVVOCATO.FLAG_CANCELLATO, " + "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, " + "AVVOCATO.COD_UFFICIO_APPARTENENZA, "
				+ "AVVOCATO.DATA_NASCITA, " + "CG.RV_MEANING DESCR_NON_ATTIVITA, " + "ID_AVVOCATO_STANDARD, "
				+ "AVVOCATO_FASCICOLO_SIEP.DATA_INIZIO_VALIDITA";

		lStatement += " FROM AVVOCATO,AVVOCATO_FASCICOLO_SIEP,CG_REF_CODES AVVTIPODESC,CG_REF_CODES CG,COMUNE DESNASCITA,COMUNE DESCR";
		lStatement += " WHERE";
		lStatement += " DATA_FINE_VALIDITA IS NULL AND";
		lStatement += " AVVOCATO.ID_AVVOCATO=AVVOCATO_FASCICOLO_SIEP.AVV_ID_AVVOCATO AND";
		lStatement += " AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIEP.COD_TIPO_AVVOCATO";
		lStatement += " AND AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO'";
		lStatement += " AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA'";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA";

		lStatement += " " + setCondizione(aModel, aFModel);

		// Gli avvocati sono ordinati in ordine crescente di ID_AVVOCATO
		lStatement += " ORDER BY ID_AVVOCATO_FASCICOLO_SIEP ASC";

		setStatement(lStatement);
	}

	public void ricercaAvvocato(AvvocatoModel aModel) throws DAOException {

		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE, " + "PROVINCIA, " + "AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, " // 20210610 MEV_21 (avvocati): aggiunti tre+tre campi in tabella
				+ "PEC, " + "FLAG_REGINDE, " + "DESCR_COMUNE_STUDIO, "
				+ "COD_STATO_NASCITA_AVV, " + "DESC_LUOGO_NAS_REGINDE, " + "ID_AVVOCATO_BONIFICATO, "
				+ "AVVOCATO.NOTE NOTEAVV, " + "AVVOCATO.COD_OPERATORE_INSERIMENTO, "
				+ "AVVOCATO.DATA_INSERIMENTO, " + "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVVOCATO.COD_UFFICIO_INSERIMENTO, " + "AVVOCATO.DATA_AGGIORNAMENTO, "
				+ "COGNOME  DESCRTIPO, " + "AVVOCATO.DATA_SOSPESO_FINO_AL, " + "AVVOCATO.DATA_RADIATO_DAL, "
				+ "AVVOCATO.COD_NON_ATTIVITA, " + "AVVOCATO.COD_LUOGO_NASCITA, "
				+ "AVVOCATO.COD_COMUNE_RESIDENZA, " + "AVVOCATO.FLAG_CANCELLATO, "
				+ "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, " + "AVVOCATO.COD_UFFICIO_APPARTENENZA, "
				+ "AVVOCATO.DATA_NASCITA, " + "ID_AVVOCATO_STANDARD, " + "CG.RV_MEANING DESCR_NON_ATTIVITA, "
				+ "DATA_INSERIMENTO DATA_INIZIO_VALIDITA";
		lStatement += " FROM AVVOCATO,CG_REF_CODES CG, COMUNE DESNASCITA,COMUNE DESCR";
		lStatement += " WHERE";
		lStatement += " DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA'";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA AND";

		lStatement += " " + setCondizione(aModel);
		int lPos = lStatement.indexOf("WHERE");
		String lSql1 = lStatement.substring(0, lPos);
		String lSql2 = lStatement.substring(lPos + 5, lStatement.length());
		lStatement += " MINUS " + lSql1;
		lStatement += " where cod_ufficio_appartenenza='00000' and id_avvocato_standard in";
		lStatement += " (select id_avvocato_standard from avvocato where flag_visualizza = 1 and FLAG_CANCELLATO ='N'";
		lStatement += " and cod_ufficio_appartenenza = '"
				+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase());
		lStatement += "') and " + lSql2;
		lStatement += " ORDER BY COGNOME,NOME ASC";

		setStatement(lStatement);
	}

	public void ricercaAvvocatoPerUffApparteneza(AvvocatoModel aModel) throws DAOException {

		String lStatement = new String("");
		lStatement += " " + getSqlQuery();

		lStatement += " " + setCondizionePerUffAppartenenza(aModel);
		int lPos = lStatement.indexOf("WHERE");
		String lSql1 = lStatement.substring(0, lPos);
		String lSql2 = lStatement.substring(lPos + 5, lStatement.length());
		lStatement += " MINUS " + lSql1;
		lStatement += " where cod_ufficio_appartenenza='00000' and id_avvocato_standard in";
		lStatement += " (select id_avvocato_standard from avvocato where flag_visualizza = 1 and FLAG_CANCELLATO ='N'";
		lStatement += " and cod_ufficio_appartenenza = '"
				+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase());
		lStatement += "') and " + lSql2;
		lStatement += " ORDER BY COGNOME,NOME ASC";

		setStatement(lStatement);
	}

	public void ricercaAvvocatoPerInserimento(AvvocatoModel aModel) throws DAOException {

		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE, " + "PROVINCIA, " + "AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, " // 20210610 MEV_21 (avvocati): aggiunti tre+tre campi in tabella
				+ "PEC, " + "FLAG_REGINDE, " + "DESCR_COMUNE_STUDIO, "
				+ "COD_STATO_NASCITA_AVV, " + "DESC_LUOGO_NAS_REGINDE, " + "ID_AVVOCATO_BONIFICATO, "
				+ "AVVOCATO.NOTE NOTEAVV, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_COMUNE_RESIDENZA, " + "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "COD_LUOGO_NASCITA, " + "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, " + "DATA_NASCITA, "
				+ "COD_UFFICIO_INSERIMENTO, " + "DATA_SOSPESO_FINO_AL, " + "DATA_RADIATO_DAL, "
				+ "COD_NON_ATTIVITA, " + "CG.RV_MEANING DESCR_NON_ATTIVITA, " + "COD_UFFICIO_APPARTENENZA, "
				+ "FLAG_CANCELLATO, " + "COGNOME  DESCRTIPO, " + "ID_AVVOCATO_STANDARD, "
				+ "DATA_INSERIMENTO DATA_INIZIO_VALIDITA";

		lStatement += " FROM AVVOCATO, COMUNE DESCR, COMUNE DESNASCITA, CG_REF_CODES CG";
		lStatement += " WHERE ";
		lStatement += " DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA'";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA";
		// lStatement += " AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' ";
		// lStatement += " AVVOCATO_FASCICOLO_SIEP.AVV_ID_AVVOCATO=AVVOCATO.ID_AVVOCATO";
		lStatement += " " + setCondizionePerInserimento(aModel);
		int lPos = lStatement.indexOf("WHERE");
		String lSql1 = lStatement.substring(0, lPos);
		String lSql2 = lStatement.substring(lPos + 5, lStatement.length());
		lStatement += " MINUS " + lSql1;
		lStatement += " where cod_ufficio_appartenenza='00000' and id_avvocato_standard in";
		lStatement += " (select id_avvocato_standard from avvocato where flag_visualizza = 1 and FLAG_CANCELLATO ='N'";
		lStatement += " and cod_ufficio_appartenenza = '"
				+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase());
		lStatement += "') and " + lSql2;
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
		aModel.setCodiceFiscale(getString("COD_FISCALE"));
		aModel.setProvincia(getString("PROVINCIA"));
		aModel.setCap(getString("CAP"));
		aModel.setFlagVisualizza(getBigDecimal("FLAG_VISUALIZZA"));
		aModel.setNote(getString("NOTEAVV"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setDescrTipo(getString("DESCRTIPO"));
		aModel.setCodLuogoNascita(getString("COD_LUOGO_NASCITA"));
		aModel.setDescLuogoNascita(getString("DESCR_LUOGO_NASCITA"));
		aModel.setDescrNonAttivita(getString("DESCR_NON_ATTIVITA"));
		aModel.setDescComuneResidenza(getString("DESCR_COMUNE_RESIDENZA"));
		aModel.setCodComuneResidenza(getString("COD_COMUNE_RESIDENZA"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setDataSospensione(getDate("DATA_SOSPESO_FINO_AL"));
		aModel.setDataRadiazione(getDate("DATA_RADIATO_DAL"));
		aModel.setCodNonAttivita(getString("COD_NON_ATTIVITA"));
		aModel.setCodUffAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
		aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.setFlagCancellato(getString("FLAG_CANCELLATO"));
		aModel.setIdAvvocatoStandard(getBigDecimal("ID_AVVOCATO_STANDARD"));
		// MEV_21 (avvocati): aggiunti tre+tre campi in tabella
		aModel.setPec(getString("PEC"));
		aModel.setFlagRegInde(getString("FLAG_REGINDE"));
		aModel.setDescrComuneStudio(getString("DESCR_COMUNE_STUDIO"));
		aModel.setCodStatoNascita(getString("COD_STATO_NASCITA_AVV"));
		aModel.setDescLuogoNascitaReginde(getString("DESC_LUOGO_NAS_REGINDE"));
		aModel.setIdAvvocatoBonificato(getBigDecimal("ID_AVVOCATO_BONIFICATO"));

		return aModel;
	}

	public GenericModel getModelStorico() throws DAOException {

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
		aModel.setCodiceFiscale(getString("COD_FISCALE"));
		aModel.setProvincia(getString("PROVINCIA"));
		aModel.setCap(getString("CAP"));
		aModel.setFlagVisualizza(getBigDecimal("FLAG_VISUALIZZA"));
		aModel.setNote(getString("NOTEAVV"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setDescrTipo(getString("DESCRTIPO"));
		aModel.setCodLuogoNascita(getString("COD_LUOGO_NASCITA"));
		aModel.setDescLuogoNascita(getString("DESCR_LUOGO_NASCITA"));
		aModel.setDescrNonAttivita(getString("DESCR_NON_ATTIVITA"));
		aModel.setDescComuneResidenza(getString("DESCR_COMUNE_RESIDENZA"));
		aModel.setCodComuneResidenza(getString("COD_COMUNE_RESIDENZA"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setDataSospensione(getDate("DATA_SOSPESO_FINO_AL"));
		aModel.setDataRadiazione(getDate("DATA_RADIATO_DAL"));
		aModel.setCodNonAttivita(getString("COD_NON_ATTIVITA"));
		aModel.setCodUffAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
		aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		aModel.setFlagCancellato(getString("FLAG_CANCELLATO"));
		aModel.setIdAvvocatoStandard(getBigDecimal("ID_AVVOCATO_STANDARD"));
		// MEV_21 (avvocati): aggiunti tre+tre campi in tabella
		aModel.setPec(getString("PEC"));
		aModel.setFlagRegInde(getString("FLAG_REGINDE"));
		aModel.setDescrComuneStudio(getString("DESCR_COMUNE_STUDIO"));
		aModel.setCodStatoNascita(getString("COD_STATO_NASCITA_AVV"));
		aModel.setDescLuogoNascitaReginde(getString("DESC_LUOGO_NAS_REGINDE"));
		aModel.setIdAvvocatoBonificato(getBigDecimal("ID_AVVOCATO_BONIFICATO"));

		return aModel;
	}

	protected String getAvvocatoSqlQuery() {

		String lStatement = new String();
		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE, " + "PROVINCIA, " + "AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, " // 20210610 MEV_21 (avvocati): aggiunti tre+tre campi in tabella
				+ "PEC, " + "FLAG_REGINDE, " + "DESCR_COMUNE_STUDIO, "
				+ "COD_STATO_NASCITA_AVV, " + "DESC_LUOGO_NAS_REGINDE, " + "ID_AVVOCATO_BONIFICATO, "
				+ "AVVOCATO.NOTE NOTEAVV, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_COMUNE_RESIDENZA, "
				+ "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, " + "COD_LUOGO_NASCITA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, " + "DATA_NASCITA, " + "DATA_SOSPESO_FINO_AL, "
				+ "DATA_RADIATO_DAL, " + "COD_NON_ATTIVITA, " + "CG.RV_MEANING DESCR_NON_ATTIVITA, "
				+ "COD_UFFICIO_APPARTENENZA, " + "FLAG_CANCELLATO, " + "COGNOME  DESCRTIPO ,"
				+ "ID_AVVOCATO_STANDARD, " + "DATA_INSERIMENTO DATA_INIZIO_VALIDITA";

		lStatement += " FROM AVVOCATO, COMUNE DESCR, COMUNE DESNASCITA, CG_REF_CODES CG";
		lStatement += " WHERE";
		lStatement += " DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND FLAG_VISUALIZZA = 1";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA'";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA";

		return lStatement;
	}

	protected String getSqlQuery() {

		String lStatement = new String();
		lStatement += " SELECT " + "ID_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE, " + "PROVINCIA, " + "AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, " // 20210610  MEV_21 (avvocati): aggiunti tre+tre campi in tabella
				+ "PEC, " + "FLAG_REGINDE, " + "DESCR_COMUNE_STUDIO, "
				+ "COD_STATO_NASCITA_AVV, " + "DESC_LUOGO_NAS_REGINDE, " + "ID_AVVOCATO_BONIFICATO, "
				+ "AVVOCATO.NOTE NOTEAVV, " + "AVVOCATO.COD_OPERATORE_INSERIMENTO, "
				+ "AVVOCATO.DATA_INSERIMENTO, " + "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVVOCATO.COD_UFFICIO_INSERIMENTO, " + "AVVOCATO.DATA_AGGIORNAMENTO, "
				+ "COGNOME  DESCRTIPO, " + "AVVOCATO.DATA_SOSPESO_FINO_AL, " + "AVVOCATO.DATA_RADIATO_DAL, "
				+ "AVVOCATO.COD_NON_ATTIVITA, " + "AVVOCATO.COD_LUOGO_NASCITA, "
				+ "AVVOCATO.COD_COMUNE_RESIDENZA, " + "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, " + "AVVOCATO.COD_UFFICIO_APPARTENENZA, "
				+ "AVVOCATO.FLAG_CANCELLATO, " + "AVVOCATO.DATA_NASCITA, "
				+ "CG.RV_MEANING DESCR_NON_ATTIVITA, " + "ID_AVVOCATO_STANDARD, "
				+ "DATA_INSERIMENTO DATA_INIZIO_VALIDITA";

		lStatement += " FROM AVVOCATO,CG_REF_CODES CG, COMUNE DESNASCITA,COMUNE DESCR";
		lStatement += " WHERE";
		lStatement += " DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND FLAG_VISUALIZZA = 1";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA'";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA AND";

		return lStatement;
	}

	private String setCondizione(AvvocatoModel aModel, AvvocatoFascicoloSiepModel aFModel) {

		String lCondizioni = new String();
		if (aModel != null) {
			if (aModel.getIdAvvocato() != null)
				lCondizioni = " AND ID_AVVOCATO=" + aModel.getIdAvvocato();
			if (aModel.getCognome() != null && aModel.getCognome() != "")
				lCondizioni += " AND COGNOME LIKE '"
						+ StringUtils.convertSqlString(aModel.getCognome().toUpperCase()) + "%'";
		}

		if (aFModel != null) {
			if (aFModel.getFasSieIdFascicoloSiep() != null)
				lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aFModel.getFasSieIdFascicoloSiep();
		}
		lCondizioni += " AND FLAG_CANCELLATO ='N'";

		return lCondizioni;
	}

	private String setCondizione(AvvocatoModel aModel) {

		boolean lInserito = false;
		String lCondizioni = new String();
		if (aModel.getIdAvvocato() != null) {
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
		lCondizioni += " AND FLAG_CANCELLATO ='N'";
		lCondizioni += " AND FLAG_VISUALIZZA = 1";

		return lCondizioni;
	}

	private String setCondizionePerInserimento(AvvocatoModel aModel) {

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
				lCondizioni += " AND FORO = '" + StringUtils.convertSqlString(aModel.getForo().toUpperCase())
						+ "'";
			}

			if (aModel.getCodUffAppartenenza() != null && aModel.getCodUffAppartenenza() != "") {
				lCondizioni += " AND (COD_UFFICIO_APPARTENENZA ='"
						+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase())
						+ "' OR COD_UFFICIO_APPARTENENZA ='00000')";
			}

			lCondizioni += " AND FLAG_CANCELLATO ='N'";
			lCondizioni += " AND FLAG_VISUALIZZA = 1";
		}
		return lCondizioni;
	}

	public void ricercaAvvocatoPaged(AvvocatoModel aModel, int aPage) throws DAOException {

		String lStatement = new String("");
		String lPaginedStatement = new String("");

		lStatement += " " + getAvvocatoSqlQuery();
		lStatement += " " + setCondizionePerInserimento(aModel);

		int lPos = lStatement.indexOf("WHERE");
		String lSql1 = lStatement.substring(0, lPos);
		String lSql2 = lStatement.substring(lPos + 5, lStatement.length());
		lStatement += " MINUS " + lSql1;
		lStatement += " where cod_ufficio_appartenenza='00000' and id_avvocato_standard in";
		lStatement += " (select id_avvocato_standard from avvocato where flag_visualizza = 1 and FLAG_CANCELLATO ='N'";
		lStatement += " and cod_ufficio_appartenenza = '"
				+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase());
		lStatement += "') and " + lSql2;
		lStatement += " ORDER BY COGNOME,NOME ASC";

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	public void getCountAvvocati(AvvocatoModel aModel) throws DAOException {

		String lStatement = "SELECT COUNT(*) HowManyRecords FROM AVVOCATO WHERE";
		lStatement += " (COD_UFFICIO_APPARTENENZA ='"
				+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase()) + "'";
		lStatement += " OR COD_UFFICIO_APPARTENENZA ='00000')";
		lStatement += " AND FLAG_VISUALIZZA = 1";
		lStatement += " " + setCondizionePerInserimento(aModel);
		int lPos = lStatement.indexOf("WHERE");
		String lSql1 = lStatement.substring(0, lPos);
		String lSql2 = lStatement.substring(lPos + 5, lStatement.length());
		lStatement += " MINUS " + lSql1;
		lStatement += " where cod_ufficio_appartenenza='00000' and id_avvocato_standard in";
		lStatement += " (select id_avvocato_standard from avvocato where flag_visualizza = 1 and FLAG_CANCELLATO ='N'";
		lStatement += " and cod_ufficio_appartenenza = '"
				+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase());
		lStatement += "') and " + lSql2;

		setStatement(lStatement);
	}

	private String setCondizionePerUffAppartenenza(AvvocatoModel aModel) {

		boolean lInserito = false;
		String lCondizioni = new String();
		if (aModel.getIdAvvocato() != null) {
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
		if (aModel.getForo() != null && aModel.getForo() != "")
			lCondizioni += " AND FORO LIKE '" + StringUtils.convertSqlString(aModel.getForo().toUpperCase())
					+ "%'";

		lCondizioni += " AND (COD_UFFICIO_APPARTENENZA ='"
				+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase())
				+ "' OR COD_UFFICIO_APPARTENENZA ='00000')";
		lCondizioni += " AND FLAG_CANCELLATO ='N'";
		lCondizioni += " AND FLAG_VISUALIZZA = 1";

		return lCondizioni;
	}

	public void ricercaForiCaricati() throws DAOException {

		String lStatement = new String("");
		lStatement += "select distinct(FORO) from AVVOCATO where FLAG_VISUALIZZA = 1";
		lStatement += " order by FORO asc";
		setStatement(lStatement);

	}

	public void ricercaForiDisponibili() throws DAOException {

		String lStatement = new String("");
		lStatement += "select DESCRIZIONE FORO from UFFICIO u,COMUNE c";
		lStatement += "	where COD_TIPO_UFFICIO = 'DIB'";
		lStatement += " and u.COD_COMUNE = c.COD_COMUNE";
		lStatement += " order by FORO";
		setStatement(lStatement);
	}

	public GenericModel getModelForo() throws DAOException {

		AvvocatoModel aModel = new AvvocatoModel();
		// Inserire le opportune set delle descrizioni!
		aModel.setForo(getString("FORO"));
		return aModel;
	}

}