package siap.sius.statistiche.dao;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.statistiche.model.EveFasGepSogModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;

public class ProcedimentiDlgs123_2018SqlDAO extends SIAPSqlDAO {

	public ProcedimentiDlgs123_2018SqlDAO(Connection aCon) {
		super(aCon);
	}

	/**
	 * Metodo per recuperare i procedimenti che rientrano dell'applicazione del Dlgs 123/2018
	 * 
	 * @param aModel
	 */
	public void ricercaProcedimentiDlgs123_2018(UfficioModel ufficioModel, RicercaProcedimentoModel aModel) {

		String lStatement = "";
		String lDataPattern = "yyyyMMdd";

		lStatement = " SELECT D.CHIAVE_ANNO," + " D.CHIAVE_PROGR, E.COGNOME, E.NOME ";

		lStatement += " FROM GENERALE_PROCEDIMENTO A,  CG_REF_CODES  C,"
				+ " FASCICOLO_SIUS D,  SOGGETTO  E, CG_REF_CODES C1,"
				+ " CG_REF_CODES C2, COMUNE F, PENA_RESIDUA PR ";

		lStatement += " WHERE A.FAS_SIU_ID_FASCICOLO_SIUS = D.ID_FASCICOLO_SIUS "
				+ " AND (A.COD_OGGETTO_PROCEDIMENTO = C1.RV_LOW_VALUE AND "
				+ " C1.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') "
				+ " AND (A.COD_POSIZIONE_GIURIDICA = C.RV_LOW_VALUE AND"
				+ " C.RV_DOMAIN = 'POSIZIONE_GIURIDICA') "
				+ " AND (E.ID_SOGGETTO = D.SOG_ID_SOGGETTO)  AND (F.COD_COMUNE = E.COD_COMUNE_NASCITA)"
				+ "   AND (D.COD_STATO_FASCICOLO = C2.RV_LOW_VALUE AND C2.RV_DOMAIN = 'STATO_FASCICOLO')"
				+ "  AND D.COD_STATO_FASCICOLO IN ('02', '10')"
				+ " AND A.COD_POSIZIONE_GIURIDICA IN ('07', '10', '16', '17', '46', '47')"
				+ "  AND PR.ID_PENA_RESIDUA =  (Select max(c.ID_PENA_RESIDUA)"
				+ " from pena_residua c where c.FAS_SIE_ID_FASCICOLO_SIEP = d.fas_sie_id_fascicolo_siep)"
				+ " AND PR.FAS_SIE_ID_FASCICOLO_SIEP = d.fas_sie_id_fascicolo_siep"
				+ " AND ((PR.NUM_ANNI_ARRESTO + PR.NUM_ANNI_RECLUSIONE) * 360) +"
				+ " ((PR.NUM_MESI_ARRESTO + PR.NUM_MESI_RECLUSIONE) * 30) + "
				+ " PR.NUM_GIORNI_ARRESTO + PR.NUM_GIORNI_RECLUSIONE <= 540   AND A.COD_OGGETTO_PROCEDIMENTO = 'C001'";

		// Date di iscrizione DAL - AL
		if (aModel.getDataIscrizioneInizio() != null)
			lStatement += " AND TO_CHAR(D.DATA_INSERIMENTO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aModel.getDataIscrizioneInizio(), lDataPattern) + "' ";

		if (aModel.getDataIscrizioneFine() != null)
			lStatement += " AND TO_CHAR(D.DATA_INSERIMENTO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aModel.getDataIscrizioneFine(), lDataPattern) + "' ";

		if (ufficioModel.getCodUfficio() != null)
			lStatement += "  AND  D.CHIAVE_UFFICIO = '" + ufficioModel.getCodUfficio() + "' ";

		lStatement += " ORDER BY D.CHIAVE_ANNO, D.CHIAVE_PROGR, E.COGNOME, E.NOME ";

		setStatement(lStatement);
	}

	public GenericModel getModel() throws DAOException {

		EveFasGepSogModel lModel = new EveFasGepSogModel();

		// popola fascicolo sius.
		lModel.setFascicoloSius(new FascicoloSiusModel());
		// lModel.getFascicoloSius().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lModel.getFascicoloSius().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lModel.getFascicoloSius().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		// lModel.getFascicoloSius().setDataIscrizione(getDate("DATA_ISCRIZIONE"));

		// popola fascicolo sius -> Soggetto.
		lModel.getFascicoloSius().setSoggetto(new SoggettoModel());
		lModel.getFascicoloSius().getSoggetto().setCognome(getString("COGNOME"));
		lModel.getFascicoloSius().getSoggetto().setNome(getString("NOME"));

		// popola generale procedimento.
		// lModel.setGeneraleProcedimento(new GeneraleProcedimentoModel());
		// lModel.getGeneraleProcedimento().setDataCameraConsiglio(getDate("DATA_UDIENZA"));
		// lModel.getGeneraleProcedimento().setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
		// lModel.getGeneraleProcedimento().setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA"));

		return lModel;
	}

}