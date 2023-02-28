package siap.siep.pagoPA.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;

/**
 * MEV_2023-13 
 * Title: CivilmenteObbligatoSqlDAO 
 * Description: Classe SqlDAO per la gestione del Civilmente Obbligato
 *
 * @author sgioggi
 * @version 1.0
 */
public class CivilmenteObbligatoSqlDAO extends SIAPSqlDAO {

	public CivilmenteObbligatoSqlDAO(Connection con) {

		super(con);
	}

	protected String getSqlQuery() {

		String s = new String("");

		s += "SELECT CO.ID_CIVILMENTE_OBBLIGATO, CO.COD_TIPO_PART, CO.COD_PARTE,"
				+ " CO.COD_FISCALE, CO.COGNOME, CO.NOME, CO.DENOMINAZIONE,"
				+ " CO.DATA_NASCITA, CO.COD_COMUNE_NASCITA, COM.DESCRIZIONE COMUNE_NASCITA,"
				+ " CO.COD_STATO_NASCITA, DESCR_STATO.RV_MEANING DESCR_STATO_NASCITA,"
				+ " CO.DESC_COMUNE_NASCITA_ESTERO, CO.SESSO, CO.RAG_SOCIALE,"
				+ " CO.COD_PROVINCIA, CO.IND_SEDE_LEGALE, CO.IND_SEDE_OPERATIVA,"
				+ " CO.FLG_CONV_UDIENZA, CO.COD_OPERATORE_INSERIMENTO,"
				+ " CO.DATA_INSERIMENTO, CO.COD_UFFICIO_INSERIMENTO,"
				+ " CO.COD_OPERATORE_AGGIORNAMENTO, CO.DATA_AGGIORNAMENTO,"
				+ " CO.COD_UFFICIO_AGGIORNAMENTO, CO.COD_FISCALE_RAP,"
				+ " CO.COD_PROVINCIA_NASCITA, DESCR_PROVINCIA.RV_MEANING DESCR_PROVINCIA"
				+ " CO.PEC, CO.E_MAIL, CO.FAS_SIE_ID_FASCICOLO_SIEP"
				+ " FROM CIVILMENTE_OBBLIGATO CO"
				+ " LEFT OUTER JOIN COMUNE COM ON (CO.COD_COMUNE_NASCITA = COM.COD_COMUNE)"
				+ " LEFT OUTER JOIN CG_REF_CODES DESCR_PROVINCIA ON (CO.COD_PROVINCIA ="
				+ " DESCR_PROVINCIA.RV_LOW_VALUE AND DESCR_PROVINCIA.RV_DOMAIN = 'PROVINCIA')"
				+ " LEFT OUTER JOIN CG_REF_CODES DESCR_STATO ON (DESCR_STATO.RV_DOMAIN = 'NAZIONE' AND"
				+ " DESCR_STATO.RV_LOW_VALUE = CO.COD_STATO_NASCITA)"
				+ " WHERE 1 = 1 ";
		return s;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {

		CivilmenteObbligatoModel aModel = new CivilmenteObbligatoModel();

		aModel.setIdCivilmenteObbligato(getBigDecimal("ID_CIVILMENTE_OBBLIGATO"));
		aModel.setCodTipoPart(getString("COD_TIPO_PART"));
		aModel.setCodParte(getString("COD_PARTE"));
		aModel.setCodFiscale(getString("COD_FISCALE"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setDenominazione(getString("DENOMINAZIONE"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		aModel.setDescComuneNascita(getString("COMUNE_NASCITA"));
		aModel.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		aModel.setDescrStatoNascita(getString("DESCR_STATO_NASCITA"));
		aModel.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		aModel.setSesso(getString("SESSO"));
		aModel.setRagSociale(getString("RAG_SOCIALE"));
		aModel.setCodProvincia(getString("COD_PROVINCIA"));
		aModel.setIndSedeLegale(getString("IND_SEDE_LEGALE"));
		aModel.setIndSedeOperativa(getString("IND_SEDE_OPERATIVA"));
		aModel.setFlagConvUdienza(getString("FLG_CONV_UDIENZA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setCodFiscaleRap(getString("COD_FISCALE_RAP"));
		aModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		aModel.setDescrProvincia(getString("DESCR_PROVINCIA"));
		aModel.setPec(getString("PEC"));
		aModel.setEmail(getString("E_MAIL"));
		aModel.setFasSieIdFascicolSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));

		return aModel;
	}

	public void ricercaCivilmenteObbligatiByFasSieIdFascicoloSiep(BigDecimal fasSieIdFascicoloSiep)
			throws DAOException {

		String lSql = getSqlQuery();
		lSql += "  " + setCondizioniByFasSieIdFascicoloSiep(fasSieIdFascicoloSiep);
		setStatement(lSql);
	}

	public String setCondizioniByFasSieIdFascicoloSiep(BigDecimal fasSieIdFascicoloSiep) {

		String condizioni = new String();
		condizioni += " AND CO.FAS_SIE_ID_FASCICOLO_SIEP = " + fasSieIdFascicoloSiep;
		condizioni += " ORDER BY CO.COGNOME, CO.NOME, CO.DENOMINAZIONE ";
		return condizioni;
	}

	public void ricercaCivilmenteObbligatoByKey(BigDecimal aKey) throws DAOException {

		String s = getSqlQuery();
		s += " " + setCondizionByKey(aKey);
		setStatement(s);
	}

	private String setCondizionByKey(BigDecimal aId) {

		String condizioni = " AND CO.ID_CIVILMENTE_OBBLIGATO = " + aId;
		return condizioni;
	}

}