package siap.sius.statistiche.dao;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;

// MEV_9: aggiunta classe per le statistiche di Misure Alternative
public class ProcPerStatisticaMisureAlternativeSqlDAO extends SIAPSqlDAO {

	public ProcPerStatisticaMisureAlternativeSqlDAO(Connection aCon) {

		super(aCon);
	}

	private String getCondizione(RicercaProcedimentoModel aModel) {

		String lCondizione = "";
		String lDataPattern = "yyyyMMdd";

		lCondizione = "fasc.CHIAVE_UFFICIO = '"
				+ aModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + "' ";

		if (aModel.getDataDepositoInizio() != null) {
			lCondizione += "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') >= '"
					+ DateUtils.getDateToString(aModel.getDataDepositoInizio(), lDataPattern) + "' ";
		}
		if (aModel.getDataDepositoFine() != null) {
			lCondizione += "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') <= '"
					+ DateUtils.getDateToString(aModel.getDataDepositoFine(), lDataPattern) + "' ";
		}

		if (aModel.getAnnoInizio() != null) {
			lCondizione += "AND fasc.CHIAVE_ANNO >= " + aModel.getAnnoInizio() + " ";
		}

		if (aModel.getAnnoFine() != null) {
			lCondizione += "AND fasc.CHIAVE_ANNO <= " + aModel.getAnnoFine() + " ";
		}

		if (aModel.getNumeroInizio() != null) {
			lCondizione += "AND fasc.CHIAVE_PROGR >= " + aModel.getNumeroInizio() + " ";
		}

		if (aModel.getNumeroFine() != null) {
			lCondizione += "AND fasc.CHIAVE_PROGR <= " + aModel.getNumeroFine() + " ";
		}
		return lCondizione;
	}

	public GenericModel getModel() throws DAOException {

		EveFasGepSogProvModel lModel = new EveFasGepSogProvModel();
		// popola fascicolo sius.
		lModel.setFascicoloSius(new FascicoloSiusModel());
		lModel.getFascicoloSius().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lModel.getFascicoloSius().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lModel.getFascicoloSius().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lModel.getFascicoloSius().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		// popola fascicolo sius -> Soggetto.
		lModel.getFascicoloSius().setSoggetto(new SoggettoModel());
		lModel.getFascicoloSius().getSoggetto().setCognome(getString("COGNOME"));
		lModel.getFascicoloSius().getSoggetto().setNome(getString("NOME"));
		// popola generale procedimento.
		lModel.setGeneraleProcedimento(new GeneraleProcedimentoModel());
		lModel.getGeneraleProcedimento().setDataCameraConsiglio(getDate("DATA_UDIENZA"));
		// popola evento.
		lModel.setEvento(new EventoModel());
		lModel.getEvento().setIdEvento(getBigDecimal("ID_EVENTO"));
		lModel.getEvento().setDataEmissione(getDate("DATA_EMISSIONE"));
		lModel.getEvento().setDescrTipoProvvedimento(getString("TIPO_PROVVEDIMENTO"));
		lModel.getEvento().setDescrMotivo(getString("OGGETTO"));
		lModel.getEvento().setDescrEsito(getString("ESITO"));
		lModel.getEvento().setFlagDocumentoRegistrato(getString("PROVVEDIMENTO_VALIDATO")); // N o S
		// popola documento allegato.
		lModel.setDocumentoAllegato(new DocumentoAllegatoModel());
		lModel.getDocumentoAllegato().setDataEmissione(getDate("DATA_DEPOSITO"));
		lModel.getDocumentoAllegato().setFlagDocumentoRegistrato(getString("DEPOSITO_VALIDATO"));

		return lModel;
	}

	public void ricercaOrdinanzeNonEmesseAttiAlPresidente(RicercaProcedimentoModel rpm) {

		String query = "SELECT fasc.ID_FASCICOLO_SIUS, fasc.CHIAVE_ANNO,fasc.CHIAVE_PROGR, EVENTO.id_Evento, "
				+ "sog.COGNOME, sog.NOME, fasc.DATA_ISCRIZIONE, GP.DATA_CAMERA_CONSIGLIO DATA_UDIENZA, "
				+ "GP.DATA_RESTITUZIONE, GP.DESCR_RESTITUZIONE, CODSTA.RV_MEANING STATO_FASCICOLO, "
				+ "CODTIPPRO.RV_MEANING TIPO_PROVVEDIMENTO, EVENTO.COD_ESITO, CODESI.RV_MEANING ESITO, "
				+ "NULL DATA_DEPOSITO, NULL DEPOSITO_VALIDATO, NULL DATA_UDIENZA, "
				+ "EVENTO.FLAG_DOCUMENTO_REGISTRATO PROVVEDIMENTO_VALIDATO, CODMOT.RV_MEANING OGGETTO, "
				+ "CODOGGPROC.RV_MEANING OGGETTO_PROCEDIMENTO, FASC.COD_STATO_FASCICOLO, "
				+ "EVENTO.DATA_EMISSIONE DATA_EMISSIONE "
				+ "FROM FASCICOLO_SIUS fasc LEFT OUTER JOIN EVENTO "
				+ "ON EVENTO.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS, "
				+ "CG_REF_CODES CODESI, CG_REF_CODES CODTIPPRO, cg_ref_codes CODMOT, "
				+ "CG_REF_CODES CODOGGPROC, CG_REF_CODES CODSTA, SOGGETTO SOG, GENERALE_PROCEDIMENTO GP "
				+ "WHERE " + getCondizione(rpm) + "AND GP.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
				+ "AND GP.COD_OGGETTO_PROCEDIMENTO IN ('C050', 'C051') "
				+ "AND GP.DATA_RESTITUZIONE IS NOT NULL "
				+ "AND (GP.COD_OGGETTO_PROCEDIMENTO = CODOGGPROC.RV_LOW_VALUE "
				+ "AND CODOGGPROC.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') "
				+ "AND (FASC.COD_STATO_FASCICOLO = CODSTA.RV_LOW_VALUE "
				+ "AND CODSTA.RV_DOMAIN = 'STATO_FASCICOLO') AND sog.id_soggetto = fasc.sog_id_soggetto "
                + "AND EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' "
                + "AND EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE "
                + "AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' "
                + "AND ((nvl(EVENTO.COD_MOTIVO, '-') = CODMOT.RV_LOW_VALUE "
                + "AND CODMOT.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') "
                + "OR (EVENTO.COD_MOTIVO = CODMOT.RV_LOW_VALUE "
                + "AND CODMOT.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) "
				+ "ORDER BY fasc.CHIAVE_ANNO, fasc.CHIAVE_PROGR";

		setStatement(query);
	}

	public void ricercaOrdinanzeNonEmesse(RicercaProcedimentoModel rpm) {

		String query = "SELECT fasc.ID_FASCICOLO_SIUS, fasc.CHIAVE_ANNO, fasc.CHIAVE_PROGR, "
				+ "sog.COGNOME, sog.NOME, fasc.DATA_ISCRIZIONE, EVENTO.id_Evento, "
				+ "GP.DATA_CAMERA_CONSIGLIO DATA_UDIENZA, DD.DATA_TERMINE_EMISSIONE, "
				+ "DD.NUM_GIORNI_TERMINE_EMISSIONE, CODSTA.RV_MEANING STATO_FASCICOLO, "
				+ "CODTIPPRO.RV_MEANING TIPO_PROVVEDIMENTO, EVENTO.COD_ESITO, CODESI.RV_MEANING ESITO, "
				+ "NULL DATA_DEPOSITO, NULL DEPOSITO_VALIDATO, NULL DATA_UDIENZA, "
				+ "EVENTO.FLAG_DOCUMENTO_REGISTRATO PROVVEDIMENTO_VALIDATO, CODMOT.RV_MEANING OGGETTO, "
				+ "CODOGGPROC.RV_MEANING OGGETTO_PROCEDIMENTO, FASC.COD_STATO_FASCICOLO, "
				+ "EVENTO.DATA_EMISSIONE DATA_EMISSIONE "
				+ "FROM FASCICOLO_SIUS fasc LEFT OUTER JOIN EVENTO "
				+ "ON EVENTO.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS, "
				+ "CG_REF_CODES CODOGGPROC, CG_REF_CODES CODSTA, SOGGETTO SOG, "
				+ "CG_REF_CODES CODESI, CG_REF_CODES CODTIPPRO, cg_ref_codes CODMOT, "
				+ "GENERALE_PROCEDIMENTO GP, DEPOSITO_DECRETO DD, EVENTO EV WHERE " + getCondizione(rpm)
				+ "AND fasc.COD_STATO_FASCICOLO = '22' "
				+ "AND GP.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
				+ "AND GP.COD_OGGETTO_PROCEDIMENTO IN ('C050', 'C051') "
				+ "AND (GP.COD_OGGETTO_PROCEDIMENTO = CODOGGPROC.RV_LOW_VALUE AND "
				+ "CODOGGPROC.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') "
				+ "AND (FASC.COD_STATO_FASCICOLO = CODSTA.RV_LOW_VALUE AND "
				+ "CODSTA.RV_DOMAIN = 'STATO_FASCICOLO') AND sog.id_soggetto = fasc.sog_id_soggetto "
				+ "AND NOT EXISTS (SELECT 1 FROM EVENTO EV "
				+ "WHERE EV.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
				+ "AND EV.COD_TIPO_PROVVEDIMENTO = '03' AND EV.COD_ESITO = '0270') "
				+ "AND EV.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
				+ "AND EV.COD_TIPO_PROVVEDIMENTO = '02' AND EV.COD_ESITO = '0610' "
				+ "AND EV.FLAG_DOCUMENTO_REGISTRATO = 'S' AND DD.ID_EVENTO_GENERATO = EV.ID_EVENTO "
                + "AND EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' "
                + "AND EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE "
                + "AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' "
                + "AND ((nvl(EVENTO.COD_MOTIVO, '-') = CODMOT.RV_LOW_VALUE "
                + "AND CODMOT.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') "
                + "OR (EVENTO.COD_MOTIVO = CODMOT.RV_LOW_VALUE "
                + "AND CODMOT.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) "
				+ "ORDER BY fasc.CHIAVE_ANNO, fasc.CHIAVE_PROGR";
		setStatement(query);
	}

	public void ricercaOrdinanzeApplicazioneProvvisoriaEmesseNoDataEsecutivita(RicercaProcedimentoModel rpm) {

		String query = "SELECT fasc.ID_FASCICOLO_SIUS, fasc.CHIAVE_ANNO, EVENTO.id_Evento, "
				+ "fasc.CHIAVE_PROGR, sog.COGNOME, sog.NOME, "
				+ "fasc.DATA_ISCRIZIONE, DO.DATA_CAMERA_CONSIGLIO DATA_EMISSIONE, "
				+ "DO.DATA_DEPOSITO, DO.DATA_ESECUTIVITA, CODSTA.RV_MEANING STATO_FASCICOLO, "
				+ "CODTIPPRO.RV_MEANING TIPO_PROVVEDIMENTO, EVENTO.COD_ESITO, CODESI.RV_MEANING ESITO, "
				+ "EVENTO.FLAG_DOCUMENTO_REGISTRATO PROVVEDIMENTO_VALIDATO, CODMOT.RV_MEANING OGGETTO, "
				+ "NULL DATA_DEPOSITO, NULL DEPOSITO_VALIDATO, NULL DATA_UDIENZA, "
				+ "CODOGGPROC.RV_MEANING OGGETTO_PROCEDIMENTO, FASC.COD_STATO_FASCICOLO, "
				+ "EVENTO.DATA_EMISSIONE DATA_EMISSIONE "
				+ "FROM FASCICOLO_SIUS fasc LEFT OUTER JOIN EVENTO "
				+ "ON EVENTO.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS, "
				+ "CG_REF_CODES CODOGGPROC, CG_REF_CODES CODSTA, SOGGETTO SOG, "
				+ "CG_REF_CODES CODESI, CG_REF_CODES CODTIPPRO, cg_ref_codes CODMOT, "
				+ "GENERALE_PROCEDIMENTO GP, DEPOSITO_ORDINANZA_PC DO, EVENTO EV WHERE " + getCondizione(rpm)
				+ "AND fasc.COD_STATO_FASCICOLO = '24' "
				+ "AND GP.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
				+ "AND GP.COD_OGGETTO_PROCEDIMENTO IN ('C050', 'C051') "
				+ "AND (GP.COD_OGGETTO_PROCEDIMENTO = CODOGGPROC.RV_LOW_VALUE "
				+ "AND CODOGGPROC.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') "
				+ "AND (FASC.COD_STATO_FASCICOLO = CODSTA.RV_LOW_VALUE "
				+ "AND CODSTA.RV_DOMAIN = 'STATO_FASCICOLO') AND sog.id_soggetto = fasc.sog_id_soggetto "
				+ "AND EV.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
				+ "AND EV.COD_TIPO_PROVVEDIMENTO = '03' AND EV.COD_ESITO = '0270' "
				+ "AND EV.FLAG_DOCUMENTO_REGISTRATO = 'S' "
				+ "AND DO.ID_EVENTO_GENERATO = EV.ID_EVENTO AND DO.DATA_ESECUTIVITA IS NULL "
                + "AND EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' "
                + "AND EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE "
                + "AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' "
                + "AND ((nvl(EVENTO.COD_MOTIVO, '-') = CODMOT.RV_LOW_VALUE "
                + "AND CODMOT.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') "
                + "OR (EVENTO.COD_MOTIVO = CODMOT.RV_LOW_VALUE "
                + "AND CODMOT.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) "
				+ "ORDER BY fasc.CHIAVE_ANNO, fasc.CHIAVE_PROGR";
		setStatement(query);
	}

	public void ricercaOrdinanzeApplicazioneProvvisoriaEmesseNoDecisioneCollegio(
			RicercaProcedimentoModel rpm) {

		String query = "SELECT fasc.ID_FASCICOLO_SIUS, fasc.CHIAVE_ANNO, fasc.CHIAVE_PROGR, "
				+ "sog.COGNOME, sog.NOME, fasc.DATA_ISCRIZIONE, EVENTO.id_Evento, "
				+ "DO.DATA_CAMERA_CONSIGLIO DATA_EMISSIONE, DO.DATA_DEPOSITO, DO.DATA_ESECUTIVITA, "
				+ "CODSTA.RV_MEANING STATO_FASCICOLO, CODOGGPROC.RV_MEANING OGGETTO_PROCEDIMENTO, "
				+ "CODTIPPRO.RV_MEANING TIPO_PROVVEDIMENTO, EVENTO.COD_ESITO, CODESI.RV_MEANING ESITO, "
				+ "NULL DATA_DEPOSITO, NULL DEPOSITO_VALIDATO, NULL DATA_UDIENZA, "
				+ "EVENTO.FLAG_DOCUMENTO_REGISTRATO PROVVEDIMENTO_VALIDATO, CODMOT.RV_MEANING OGGETTO, "
				+ "FASC.COD_STATO_FASCICOLO, EVENTO.DATA_EMISSIONE DATA_EMISSIONE "
				+ "FROM FASCICOLO_SIUS fasc LEFT OUTER JOIN EVENTO "
				+ "ON EVENTO.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS, "
				+ "CG_REF_CODES CODOGGPROC, CG_REF_CODES CODSTA, SOGGETTO SOG, GENERALE_PROCEDIMENTO GP, "
				+ "CG_REF_CODES CODESI, CG_REF_CODES CODTIPPRO, cg_ref_codes CODMOT, "
				+ "DEPOSITO_ORDINANZA_PC DO, EVENTO EV WHERE " + getCondizione(rpm)
				+ "AND fasc.COD_STATO_FASCICOLO <> '07' "
				+ "AND GP.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
				+ "AND GP.COD_OGGETTO_PROCEDIMENTO IN ('C050', 'C051') "
				+ "AND (GP.COD_OGGETTO_PROCEDIMENTO = CODOGGPROC.RV_LOW_VALUE "
				+ "AND CODOGGPROC.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') "
				+ "AND (FASC.COD_STATO_FASCICOLO = CODSTA.RV_LOW_VALUE "
				+ "AND CODSTA.RV_DOMAIN = 'STATO_FASCICOLO') AND sog.id_soggetto = fasc.sog_id_soggetto "
				+ "AND EXISTS (SELECT EV.ID_EVENTO FROM EVENTO EV "
				+ "WHERE EV.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
				+ "AND EV.COD_TIPO_PROVVEDIMENTO = '03' AND EV.COD_ESITO = '0270' "
				+ "AND EV.FLAG_DOCUMENTO_REGISTRATO = 'S') "
				+ "AND EV.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
				+ "AND EV.COD_TIPO_PROVVEDIMENTO = '03' AND EV.COD_ESITO = '0270' "
				+ "AND EV.FLAG_DOCUMENTO_REGISTRATO = 'S' AND DO.ID_EVENTO_GENERATO = EV.ID_EVENTO "
                + "AND EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' "
                + "AND EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE "
                + "AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' "
                + "AND ((nvl(EVENTO.COD_MOTIVO, '-') = CODMOT.RV_LOW_VALUE "
                + "AND CODMOT.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') "
                + "OR (EVENTO.COD_MOTIVO = CODMOT.RV_LOW_VALUE "
                + "AND CODMOT.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) "
				+ "AND DO.DATA_ESECUTIVITA IS NOT NULL ORDER BY fasc.CHIAVE_ANNO, fasc.CHIAVE_PROGR";
		setStatement(query);
	}

}