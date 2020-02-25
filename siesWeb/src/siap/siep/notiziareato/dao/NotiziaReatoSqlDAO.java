package siap.siep.notiziareato.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.siep.notiziareato.model.NotiziaReatoModel;

/**
 * <p>
 * Title: NotiziaReatoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Notizia di Reato
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

public class NotiziaReatoSqlDAO extends SqlDAO {
	public NotiziaReatoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	// 05/06/2009
	/**
	 * RIcerca la notizia di reato da varie condizioni
	 * 
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	public void ricercaNotiziaReatoBySIGE(NotiziaReatoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneBySIGE(aModel);
		// lSql += " " + setOrderNReato();
		lSql += " " + setOrderInsReato();
		setStatement(lSql);
	}

	/**
	 * RIcerca la notizia di reato da varie condizioni
	 * 
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	public void ricercaNotiziaReato(NotiziaReatoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		// lSql += " " + setOrderNReato();
		lSql += " " + setOrderInsReato();
		setStatement(lSql);
	}

	/**
	 * RIcerca la notizia di reato dall'id del fascicolo
	 * 
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	public void ricercaNotiziaReatoByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += "  AND  FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo;
		lSql += " " + setOrderNReato();
		setStatement(lSql);
	}

	/**
	 * RIcerca le notizie di reato dall'id del fascicolo, ordinate per data di inserimento
	 * 
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	public void ricercaNotiziaReatoByIdFascicoloOrder(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += "  AND  FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo;
		lSql += " " + setOrderInsReato();
		setStatement(lSql);
	}

	// ANGELA 19.05.2009
	/**
	 * RIcerca le notizie di reato dall'id del fascicolo sige, ordinate per data di inserimento
	 * 
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	public void ricercaNotiziaReatoByIdFascicoloSigeOrder(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += "  AND  FAS_ID_FASCICOLO_SIGE=" + aIdFascicolo;
		lSql += " " + setOrderInsReato();
		setStatement(lSql);
	}

	/**
	 * ricerca la notizia di reato per il campo chiave
	 * 
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaNotiziaReatoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_NOTIZIA_REATO, " + "PROGR_NOTIZIA, " + "DATA_PERVENIMENTO, "
				+ "ACQUISIZIONE_DIRETTA, " + "DATA_FATTO, " + "COD_FONTE, " + "TIPO_FONTE, "
				+ "COD_COMUNE_FONTE, " + "COM.DESCRIZIONE DESCRIZIONECOMUNE, " + "NUM_REG_AUTORITA, "
				+ "LUOGO_PROVENIENZA, " + "DATA_ACQUISIZIONE, " + "NUMERO_RICEVUTA, " + "DESCRIZIONE_FONTE, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " +
				// modifiche per integrazione REGE-SIES
				"DATA_ARRESTO, " + "DATA_FERMO, " + "FLAG_FOTOSEGNALATO, " + "FLAG_ARRESTATO, "
				+ "DATA_FOTO, " + "COD_AUTORITA_FOTO, " + "COD_COMUNE_FOTO, " + "FAS_ID_FASCICOLO_SIGE, "
				+ "COMFOTO.DESCRIZIONE DESCR_COMUNE_FOTO, " + "CG_COD.RV_MEANING DESCR_AUTORITA_FOTO";

		lStatement += " FROM NOTIZIA_REATO, COMUNE COM, CG_REF_CODES CG_COD, COMUNE COMFOTO";
		// lStatement += " WHERE COD_COMUNE_FONTE=COM.COD_COMUNE";
		lStatement += " WHERE COD_COMUNE_FONTE=COM.COD_COMUNE AND CG_COD.RV_DOMAIN='TIPO_AUTORITA'";
		lStatement += " AND CG_COD.RV_LOW_VALUE=COD_AUTORITA_FOTO ";
		lStatement += " AND COD_COMUNE_FOTO=COMFOTO.COD_COMUNE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		NotiziaReatoModel aModel = new NotiziaReatoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdNotiziaReato(getBigDecimal("ID_NOTIZIA_REATO"));
		aModel.setProgrNotizia(getString("PROGR_NOTIZIA"));
		aModel.setDataPervenimento(getDate("DATA_PERVENIMENTO"));
		aModel.setAcquisizioneDiretta(getString("ACQUISIZIONE_DIRETTA"));
		aModel.setDataFatto(getDate("DATA_FATTO"));
		aModel.setCodFonte(getString("COD_FONTE"));
		// aModel.setDescrFonte(getString("") );
		aModel.setTipoFonte(getString("TIPO_FONTE"));
		aModel.setCodComuneFonte(getString("COD_COMUNE_FONTE"));
		aModel.setDescrComuneFonte(getString("DESCRIZIONECOMUNE"));
		aModel.setNumRegAutorita(getString("NUM_REG_AUTORITA"));
		aModel.setLuogoProvenienza(getString("LUOGO_PROVENIENZA"));
		aModel.setDataAcquisizione(getDate("DATA_ACQUISIZIONE"));
		aModel.setNumeroRicevuta(getString("NUMERO_RICEVUTA"));
		aModel.setDescrizioneFonte(getString("DESCRIZIONE_FONTE"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		// modifiche per integrazione REGE-SIES
		aModel.setDataArresto(getDate("DATA_ARRESTO"));
		aModel.setDataFermo(getDate("DATA_FERMO"));
		aModel.setFlagFotosegnalato(getString("FLAG_FOTOSEGNALATO"));
		aModel.setFlagArrestato(getString("FLAG_ARRESTATO"));
		aModel.setDataFoto(getDate("DATA_FOTO"));
		aModel.setCodAutoritaFoto(getString("COD_AUTORITA_FOTO"));
		aModel.setCodComuneFoto(getString("COD_COMUNE_FOTO"));
		aModel.setDescAutoritaFoto(getString("DESCR_AUTORITA_FOTO"));
		aModel.setDescComuneFoto(getString("DESCR_COMUNE_FOTO"));
		aModel.setFasIdFascicoloSige(getBigDecimal("FAS_ID_FASCICOLO_SIGE"));

		return aModel;
	}

	// 05.06.2009
	public String setCondizioneBySIGE(NotiziaReatoModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		// Controlla che esista l'ID del Procedimento e lo inserisce come parametro nella ricerca
		if (aModel.getFasIdFascicoloSige() != null) {
			lCondizioni = " AND FAS_ID_FASCICOLO_SIGE=" + aModel.getFasIdFascicoloSige();
			// lInserito=true;
		}

		return lCondizioni;
	}

	public String setCondizione(NotiziaReatoModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		// Controlla che esista l'ID del Procedimento e lo inserisce come parametro nella ricerca
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni = " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
			// lInserito=true;
		}

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_NOTIZIA_REATO = " + aKey;
	}

	// Ordinamento della lista Notizie di Reato per data Pervenimento Ascendente
	public String setOrderNReato() {
		return " ORDER BY DATA_PERVENIMENTO ";
	}

	// Ordinamento della lista Notizie di Reato per Data Inserimento
	public String setOrderInsReato() {
		return " ORDER BY DATA_INSERIMENTO ";
	}

}