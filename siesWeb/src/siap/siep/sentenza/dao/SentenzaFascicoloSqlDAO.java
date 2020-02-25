package siap.siep.sentenza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.sentenza.model.SentenzaFascicoloModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: SentenzaFascicoloSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che esegue ricerca su tabella Sentenza e FascicoloSiep relazionata con il
 * Soggetto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class SentenzaFascicoloSqlDAO extends SIAPSqlDAO {

	public SentenzaFascicoloSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void getCountFascicoli(BigDecimal aIdSentenzaModel) throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManyFascicoli ";
		lStatement += " FROM FASCICOLO_SIEP ";
		lStatement += " where  sen_id_sentenza =" + aIdSentenzaModel;
		// lStatement += " and  id_SENTENZA=sen_id_sentenza ";
		setStatement(lStatement);
	}

	public void ricercaSentenzaFascicolo(SoggettoModel aSoggetto, String TipoBen) throws DAOException {
		String lStatement = "SELECT "
				+ "DISTINCT(s.ID_SENTENZA), "
				+ "b.COD_DPR, "
				+ "s.DATA_PROVVEDIMENTO, f.DATA_IRREVOCABILITA, "
				+ "s.COD_TIPO_PROVVEDIMENTO, D_TIPROV.RV_MEANING DESCRTIPOPROVVEDIMENTO, "
				+ "s.COD_TIPO_AUTORITA_EMITTENTE, D_UFFICIOEMI.RV_MEANING DESCRTIPOAUTORITAEMITTENTE, "
				+ "s.COD_LUOGO_EMITTENTE, c.DESCRIZIONE, s.NUM_SEZIONE_AUTORITA_EMITTENTE, "
				+ "s.ANNO_SENTENZA, s.NUMERO_SENTENZA, "
				+ "f.ID_FASCICOLO_SIEP, f.CHIAVE_ANNO, f.CHIAVE_PROGR, f.DATA_ISCRIZIONE, "
				+ "f.FLAG_VALIDATO, f.COD_STATO_FASCICOLO,  "
				+ "f.SEN_ID_SENTENZA, f.SOG_ID_SOGGETTO, "
				+ "f.CHIAVE_UFFICIO, ud.DESCR_TIPO_UFFICIO, ud.DESCR_COMUNE, "
				+ "D_STATO_FASC.RV_MEANING DESCRSTATOFASC "
				+ "FROM "
				+ "SOGGETTO sg, SENTENZA s, FASCICOLO_SIEP f, COMUNE c, BENEFICIO b, "
				+ "CG_REF_CODES D_TIPROV,CG_REF_CODES D_UFFICIOEMI, CG_REF_CODES D_STATO_FASC, UFFICIO_DESCR ud";
		lStatement += " WHERE sg.COGNOME = '" + StringUtils.convertSqlString(aSoggetto.getCognome()) + "'";
		lStatement += " AND sg.NOME = '" + StringUtils.convertSqlString(aSoggetto.getNome()) + "'";
		lStatement += " AND sg.COD_COMUNE_NASCITA = '"
				+ StringUtils.convertSqlString(aSoggetto.getCodComuneNascita()) + "'";
		// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
		lStatement += " AND trunc(sg.DATA_NASCITA) = TO_DATE('"
				+ DateUtils.getDateToString(aSoggetto.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		lStatement += " AND sg.ID_SOGGETTO = f.SOG_ID_SOGGETTO ";
		lStatement += " AND f.SEN_ID_SENTENZA = s.ID_SENTENZA ";
		lStatement += " AND D_TIPROV.RV_DOMAIN='TIPO_PROVVEDIMENTO' AND D_TIPROV.RV_LOW_VALUE=s.COD_TIPO_PROVVEDIMENTO ";
		lStatement += " AND D_UFFICIOEMI.RV_DOMAIN='TIPO_UFFICIO_EMITTENTE' AND D_UFFICIOEMI.RV_LOW_VALUE=s.COD_TIPO_AUTORITA_EMITTENTE ";
		lStatement += " AND D_STATO_FASC.RV_DOMAIN='STATO_FASCICOLO' AND D_STATO_FASC.RV_LOW_VALUE=f.COD_STATO_FASCICOLO ";
		lStatement += " AND ud.COD_UFFICIO=f.CHIAVE_UFFICIO ";
		lStatement += " AND c.COD_COMUNE = s.COD_LUOGO_EMITTENTE";
		lStatement += " AND b.COD_NATURA_BENEFICIO = 'C'";
		lStatement += " AND b.COD_TIPO_BENEFICIO " + TipoBen;
		lStatement += " AND f.ID_FASCICOLO_SIEP = b.FAS_SIE_ID_FASCICOLO_SIEP";

		setStatement(lStatement);
	}

	/**
	 * Restituisce la rappresentazione dei dati selezionati in Model
	 * 
	 * @return GenericModel
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		SentenzaFascicoloModel lModel = new SentenzaFascicoloModel();

		lModel.setIdSentenza(getBigDecimal("ID_SENTENZA"));
		lModel.setCodDpr(getString("COD_DPR"));
		lModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lModel.setDescrTipoProvvedimento(getString("DESCRTIPOPROVVEDIMENTO"));
		lModel.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		lModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		lModel.setDescrTipoAutoritaEmittente(getString("DESCRTIPOAUTORITAEMITTENTE"));
		lModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		lModel.setDescrLuogoEmittente(getString("DESCRIZIONE"));
		lModel.setNumSezioneAutoritaEmittente(getString("NUM_SEZIONE_AUTORITA_EMITTENTE"));
		lModel.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA"));
		lModel.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA"));
		lModel.setNumSentenza(getString("NUMERO_SENTENZA"));

		lModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lModel.setCodiceStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		lModel.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		lModel.setChiaveAnnoFascicolo(getBigDecimal("CHIAVE_ANNO"));
		lModel.setChiaveNumeroFascicolo(getBigDecimal("CHIAVE_PROGR"));
		lModel.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));
		lModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		lModel.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lModel.setDescrChiaveUfficio(getString("DESCR_TIPO_UFFICIO"));
		lModel.setDescrComune(getString("DESCR_COMUNE"));
		lModel.setFlagValidato(getString("FLAG_VALIDATO"));
		lModel.setDescrStatoFasc(getString("DESCRSTATOFASC"));

		return lModel;
	}

}