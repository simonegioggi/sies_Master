package siap.siep.storicoavvocato.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: StoricoAvvocatoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella StoricoAvvocato
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
public class StoricoAvvocatoSqlDAO extends SqlDAO {

	public StoricoAvvocatoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaStoricoAvvocato(StoricoAvvocatoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaStoricoAvvocatoByIdAvvocato(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND AVV_ID_AVVOCATO=  '" + aKey + "'";
		setStatement(lSql);
	}

	public void ricercaStoricoAvvocatoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_STORICO_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, " + "INDIRIZZO, "
				+ "TELEFONO, " + "FAX, " + "E_MAIL, " + "COD_FISCALE," + "FLAG_VISUALIZZA,"
				+ "COD_COMUNE_RESIDENZA, " + "COD_LUOGO_NASCITA, " + "DATA_NASCITA, "
				+ "DATA_SOSPESO_FINO_AL, " + "DATA_RADIATO_DAL, " + "COD_NON_ATTIVITA, " + "NOTE, "
				+ "FLAG_CANCELLATO, " + "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, " + "CG.RV_MEANING DESCR_NON_ATTIVITA, " +

				"COD_UFFICIO_APPARTENENZA, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "PROVINCIA, " + "STORICO_AVVOCATO.CAP, "
				+ "ID_AVVOCATO_STANDARD, " + "AVV_ID_AVVOCATO ";
		lStatement += " FROM STORICO_AVVOCATO, COMUNE DESCR, COMUNE DESNASCITA, CG_REF_CODES CG ";
		lStatement += " WHERE DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA ";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA ";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA' ";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		StoricoAvvocatoModel aModel = new StoricoAvvocatoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdStoricoAvvocato(getBigDecimal("ID_STORICO_AVVOCATO"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setForo(getString("FORO"));
		aModel.setIndirizzo(getString("INDIRIZZO"));
		aModel.setTelefono(getString("TELEFONO"));
		aModel.setFax(getString("FAX"));
		aModel.setEMail(getString("E_MAIL"));
		aModel.setCodiceFiscale(getString("COD_FISCALE"));
		aModel.setFlagVisualizza(getBigDecimal("FLAG_VISUALIZZA"));
		aModel.setCodComuneResidenza(getString("COD_COMUNE_RESIDENZA"));
		aModel.setDescrComuneResidenza(getString("DESCR_COMUNE_RESIDENZA"));
		aModel.setCodLuogoNascita(getString("COD_LUOGO_NASCITA"));
		aModel.setDescrLuogoNascita(getString("DESCR_LUOGO_NASCITA"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setDataSospesoFinoAl(getDate("DATA_SOSPESO_FINO_AL"));
		aModel.setDataRadiatoDal(getDate("DATA_RADIATO_DAL"));
		aModel.setCodNonAttivita(getString("COD_NON_ATTIVITA"));
		aModel.setDescrNonAttivita(getString("DESCR_NON_ATTIVITA"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
		// aModel.setDescrUfficioAppartenenza(getString("DESCR_LUOGO_NASCITA") );
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setProvincia(getString("PROVINCIA"));
		aModel.setCap(getString("CAP"));
		aModel.setIdAvvocatoStandard(getBigDecimal("ID_AVVOCATO_STANDARD"));
		aModel.setAvvIdAvvocato(getBigDecimal("AVV_ID_AVVOCATO"));
		aModel.setFlagCancellato(getString("FLAG_CANCELLATO"));

		return aModel;
	}

	public String setCondizione(StoricoAvvocatoModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_STORICO_AVVOCATO = " + aKey;
	}

}