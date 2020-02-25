package siap.siep.istruttoria.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

/**
 * IstruttoriaNotizieReatoSqlDAO
 * 
 * @author Giselda De Vita
 *
 */
public class IstruttoriaNotizieReatoSqlDAO extends SIAPSqlDAO {

	public IstruttoriaNotizieReatoSqlDAO(Connection lConn) {
		super(lConn);
	}

	/*
	 * getNotizieRege() - Seleziono se ci sono Notizie di Reato
	 */
	public void getNotizieRege(SoggettoModel aSogg, SentenzaModel aSentenza) {

		String lSql = "";
		lSql = "select distinct rege.rege_sentenza.id_file idFile" + " from rege.rege_sentenza,"
				+ " rege.rege_soggetto," + " rege.rege_notizia_reato" + " where " + " nome = '"
				+ StringUtils.convertSqlString(aSogg.getNome()) + "' and " + " cognome = '"
				+ StringUtils.convertSqlString(aSogg.getCognome()) + "' and " + " anno_sentenza = "
				+ aSentenza.getAnnoSentenza() + " and " + " numero_sentenza = '"
				+ aSentenza.getNumeroSentenza() + "' and " + " cod_tipo_autorita_emittente = '"
				+ aSentenza.getCodTipoAutoritaEmittente() + "' and " + " cod_luogo_emittente ='"
				+ aSentenza.getCodLuogoEmittente() + "' and " + " cod_comune_nascita = '"
				+ aSogg.getCodComuneNascita() + "' ";
		if (aSogg.getDataNascita() != null)
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lSql += " and trunc(data_nascita) = to_date('"
					+ DateUtils.getDateToString(aSogg.getDataNascita(), "dd/MM/yyyy") + "','DD/MM/YYYY')  ";

		lSql += " and rege.rege_sentenza.ID_FILE = rege.rege_notizia_reato.ID_FILE";

		this.setStatement(lSql);
	}

	public GenericModel getModel() throws DAOException {
		FascicoloSiepModel lFasc = new FascicoloSiepModel();

		lFasc.setIdFascicoloSiep(this.getBigDecimal("id_fascicolo_siep"));
		return lFasc;
	}

}