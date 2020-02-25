package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class ProcProvvNoValidatiNoDepositoSqlDAO extends SIAPSqlDAO {

	public ProcProvvNoValidatiNoDepositoSqlDAO(Connection aCon) {
		super(aCon);
	}
	
	private String getCondizione(RicercaProcedimentoModel aModel) {
	    String lCondizione = "";
        String lDataPattern = "yyyyMMdd";
	    
        lCondizione = "fasc.CHIAVE_UFFICIO = '" + aModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + "' ";

        if (aModel.getDataDepositoInizio() != null) {
            lCondizione += 
                    "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') >= '" + DateUtils.getDateToString(aModel.getDataDepositoInizio(), lDataPattern) + "' ";
        }
        if (aModel.getDataDepositoFine() != null) {
            lCondizione += 
                    "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') <= '" + DateUtils.getDateToString(aModel.getDataDepositoFine(), lDataPattern) + "' ";
        }
        
        if (aModel.getAnnoInizio() != null) {
            lCondizione +=
                    "AND fasc.CHIAVE_ANNO >= " + aModel.getAnnoInizio() + " ";
        }

        if (aModel.getAnnoFine() != null) {
            lCondizione +=
                    "AND fasc.CHIAVE_ANNO <= " + aModel.getAnnoFine() + " ";
        }
        
        if (aModel.getNumeroInizio() != null) {
            lCondizione +=
                    "AND fasc.CHIAVE_PROGR >= " + aModel.getNumeroInizio() + " ";
        }
        
        if (aModel.getNumeroFine() != null) {
            lCondizione +=
                    "AND fasc.CHIAVE_PROGR <= " +aModel.getNumeroFine() + " ";
        }
	    return lCondizione;
	}
	
	public void ricercaProcedimentiPriviProvvedimenti(RicercaProcedimentoModel aModel) {
        String lStatement = "";
        
        lStatement = 
                "SELECT "
                + "fasc.ID_FASCICOLO_SIUS, "        		
                + "fasc.CHIAVE_ANNO, "
                + "fasc.CHIAVE_PROGR, "
                + "sog.COGNOME, "
                + "sog.NOME, "
                + "fasc.DATA_ISCRIZIONE, "
                + "GP.DATA_CAMERA_CONSIGLIO DATA_UDIENZA, "
                + "EVENTO.ID_EVENTO ID_EVENTO, "
                + "EVENTO.DATA_EMISSIONE DATA_EMISSIONE,  "
                + "NULL TIPO_PROVVEDIMENTO, "
                + "CODMOV.RV_MEANING OGGETTO, "
                + "EVENTO.COD_ESITO ESITO, " // 20140407
                //+ "NULL ESITO, "
                + "EVENTO.FLAG_DOCUMENTO_REGISTRATO PROVVEDIMENTO_VALIDATO, "
                + "NULL DATA_DEPOSITO, "
                + "NULL DEPOSITO_VALIDATO "
                + "FROM "
                + "FASCICOLO_SIUS fasc LEFT OUTER JOIN EVENTO "
                + "ON EVENTO.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS "
                + "AND (EVENTO.COD_TIPO_PROVVEDIMENTO IN ('03','02') "
                + "AND EVENTO.COD_ESITO IN ('0600','0601','0602','0603','0604')), " // 20140407
                //+ "AND EVENTO.COD_ESITO NOT IN ('0600','0601','0602','0603','0604')), "
                + "CG_REF_CODES CODMOV, "
                + "SOGGETTO SOG, "
                + "GENERALE_PROCEDIMENTO GP "
                + "WHERE "
                + this.getCondizione(aModel)
                + "AND fasc.COD_STATO_FASCICOLO IN ('02','10') "
                + "AND GP.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
                + "AND (GP.COD_OGGETTO_PROCEDIMENTO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') "
                + "AND sog.id_soggetto = fasc.sog_id_soggetto "
                + "ORDER BY fasc.CHIAVE_ANNO,fasc.CHIAVE_PROGR";
		
		setStatement(lStatement);
		
		
		
		/* 20140407 - Query proposta dalla segnalazione 
		" SELECT fasc.id_fascicolo_sius, " +
		  " fasc.chiave_anno, " +
		  " fasc.chiave_progr, " +
		  " fasc.cod_stato_fascicolo, " +
		  " sog.cognome, " +
		  " sog.nome, " +
		  " fasc.data_iscrizione, " +
		  " gp.data_camera_consiglio data_udienza, " +
		  " evento.id_evento id_evento, " +
		  " evento.data_emissione data_emissione, " +
		  " NULL tipo_provvedimento, " +
		  " codmov.rv_meaning oggetto, " +
		  " evento.cod_esito esito, " +
		  " evento.flag_documento_registrato provvedimento_validato, " +
		  " NULL data_deposito, " +
		  " NULL deposito_validato " +
		" FROM fascicolo_sius fasc " +
		" LEFT OUTER JOIN evento " +
		" ON evento.fas_siu_id_fascicolo_sius  = fasc.id_fascicolo_sius " +
		" AND ( evento.cod_tipo_provvedimento IN ('03', '02') " +
		" AND evento.cod_esito                IN ('0600','0601','0602','0603','0604') ), " +
		  " cg_ref_codes codmov, " +
		  " soggetto sog, " +
		  " generale_procedimento gp " +
		" WHERE " +
			this.getCondizione(aModel) +
			
		" AND fasc.cod_stato_fascicolo     IN ('02', '10') " +
		" AND gp.fas_siu_id_fascicolo_sius  = fasc.id_fascicolo_sius " +
		" AND ( gp.cod_oggetto_procedimento = codmov.rv_low_value " +
		" AND codmov.rv_domain              = 'OGGETTO_PROCEDIMENTO' ) " +
		" AND sog.id_soggetto               = fasc.sog_id_soggetto " +
		" ORDER BY fasc.chiave_anno, fasc.chiave_progr " 
		*/
		
		
		
	}
	
	public void ricercaProcedimentiPerProvvedimentiNoValidati(RicercaProcedimentoModel aModel) {
        String lStatement = "";
        
        lStatement = 
                "SELECT "
                + "fasc.ID_FASCICOLO_SIUS, "
                + "fasc.CHIAVE_ANNO, "
                + "fasc.CHIAVE_PROGR, "
        		+ "fasc.ID_FASCICOLO_SIUS, "                
                + "sog.COGNOME, "
                + "sog.NOME, "
                + "fasc.DATA_ISCRIZIONE, "
                + "GP.DATA_CAMERA_CONSIGLIO DATA_UDIENZA, "
                + "EVENTO.ID_EVENTO ID_EVENTO, "
                + "EVENTO.DATA_EMISSIONE DATA_EMISSIONE, "
                + "CODTIPPRO.RV_MEANING TIPO_PROVVEDIMENTO, "
                + "CODMOV.RV_MEANING OGGETTO, "
                + "CODESI.RV_MEANING ESITO, "
                + "EVENTO.FLAG_DOCUMENTO_REGISTRATO PROVVEDIMENTO_VALIDATO, "
                + "DA.DATA_EMISSIONE DATA_DEPOSITO, "
                + "DA.FLAG_DOCUMENTO_REGISTRATO DEPOSITO_VALIDATO "
                + "FROM "
                + "EVENTO "
                + "LEFT OUTER JOIN  DOCUMENTO_ALLEGATO DA ON DA.EVE_ID_EVENTO = ID_EVENTO AND DA.DATA_EMISSIONE IS NULL, "
                + "FASCICOLO_SIUS fasc, "
                + "CG_REF_CODES CODESI,"
                + "CG_REF_CODES CODMOV, "
                + "CG_REF_CODES CODTIPPRO, "
                + "SOGGETTO sog, "
                + "GENERALE_PROCEDIMENTO GP "
                + "WHERE "
                + this.getCondizione(aModel)
                + "AND GP.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
                + "AND EVENTO.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
                + "AND EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE "
                + "AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' "
                + "AND"
                + "("
                + "(NVL(EVENTO.COD_MOTIVO, '-') = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') "
                + "OR "
                + "(EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') "
                + ") "
                + "AND EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE "
                + "AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' "
                + "AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL) "
                + "AND (COD_TIPO_PROVVEDIMENTO  IN ('03','02') AND COD_ESITO NOT IN ('0600','0601','0602','0603','0604')) "
                + "AND sog.id_soggetto = fasc.sog_id_soggetto "
                + "ORDER BY "
                + "fasc.CHIAVE_ANNO,"
                + "fasc.CHIAVE_PROGR"; 
        
        setStatement(lStatement);
	}
	
	public void ricercaProcedimentiPerProvvedimentiNoDepositati(RicercaProcedimentoModel aModel) {
        String lStatement = "";
        
        lStatement = 
                "SELECT "
                + "fasc.ID_FASCICOLO_SIUS, "
                + "fasc.CHIAVE_ANNO, "
                + "fasc.CHIAVE_PROGR, "
                + "sog.COGNOME, "
                + "sog.NOME, "
                + "fasc.DATA_ISCRIZIONE, "
                + "GP.DATA_CAMERA_CONSIGLIO DATA_UDIENZA, "
                + "EVENTO.ID_EVENTO ID_EVENTO, "
                + "EVENTO.DATA_EMISSIONE DATA_EMISSIONE, "
                + "CODTIPPRO.RV_MEANING TIPO_PROVVEDIMENTO, "
                + "CODMOV.RV_MEANING OGGETTO, "
                + "CODESI.RV_MEANING ESITO, "
                + "EVENTO.FLAG_DOCUMENTO_REGISTRATO PROVVEDIMENTO_VALIDATO, "
                + "DA.DATA_EMISSIONE DATA_DEPOSITO, "
                + "DA.FLAG_DOCUMENTO_REGISTRATO DEPOSITO_VALIDATO "
                + "FROM "
                + "EVENTO "
                + "LEFT OUTER JOIN DOCUMENTO_ALLEGATO DA ON DA.EVE_ID_EVENTO = ID_EVENTO AND DA.DATA_EMISSIONE IS NULL, "
                + "FASCICOLO_SIUS fasc, "
                + "CG_REF_CODES CODESI, "
                + "CG_REF_CODES CODMOV, "
                + "CG_REF_CODES CODTIPPRO, "
                + "SOGGETTO sog, "
                + "GENERALE_PROCEDIMENTO GP "
                + "WHERE "
                + this.getCondizione(aModel) 
                + "AND fasc.COD_STATO_FASCICOLO IN ('02','10') "
                + "AND GP.FAS_SIU_ID_FASCICOLO_SIUS=fasc.ID_FASCICOLO_SIUS "
                + "AND EVENTO.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
                + "AND EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE "
                + "AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' "
                + "AND "
                + "("
                + "(NVL(EVENTO.COD_MOTIVO, '-') = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') "
                + "OR "
                + "(EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') "
                + ") "
                + "AND EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE "
                + "AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' "
                + "AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL) "
                + "AND (COD_TIPO_PROVVEDIMENTO  IN ('03','02') AND COD_ESITO NOT IN ('0600','0601','0602','0603','0604')) "
                + "AND sog.id_soggetto = fasc.sog_id_soggetto "
                + "ORDER BY "
                + "fasc.CHIAVE_ANNO,"
                + "fasc.CHIAVE_PROGR";
        
        setStatement(lStatement);
	}

	public void ricercaProcedimentiPerProvvedimentiDepostatiNoValidati(RicercaProcedimentoModel aModel) {
        String lStatement = "";
        
        lStatement = 
                "SELECT " 
        		+ "fasc.ID_FASCICOLO_SIUS, "
                + "fasc.CHIAVE_ANNO, "
                + "fasc.CHIAVE_PROGR, "
                + "sog.COGNOME, "
                + "sog.NOME, "
                + "fasc.DATA_ISCRIZIONE, "
                + "GP.DATA_CAMERA_CONSIGLIO DATA_UDIENZA, "
                + "EVENTO.ID_EVENTO ID_EVENTO, "                
                + "EVENTO.DATA_EMISSIONE DATA_EMISSIONE, "
                + "CODTIPPRO.RV_MEANING TIPO_PROVVEDIMENTO, "
                + "CODMOV.RV_MEANING OGGETTO, "
                + "CODESI.RV_MEANING ESITO, "
                + "EVENTO.FLAG_DOCUMENTO_REGISTRATO PROVVEDIMENTO_VALIDATO, "
                + "DA.DATA_EMISSIONE DATA_DEPOSITO, "
                + "DA.FLAG_DOCUMENTO_REGISTRATO DEPOSITO_VALIDATO "
                + "FROM "
                + "EVENTO, "
                + "DOCUMENTO_ALLEGATO DA, "
                + "FASCICOLO_SIUS fasc, "
                + "CG_REF_CODES CODESI, "
                + "CG_REF_CODES CODMOV, "
                + "CG_REF_CODES CODTIPPRO, "
                + "SOGGETTO sog, "
                + "GENERALE_PROCEDIMENTO GP "
                + "WHERE "
                + this.getCondizione(aModel)
                + "AND EVENTO.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
                + "AND GP.FAS_SIU_ID_FASCICOLO_SIUS=fasc.ID_FASCICOLO_SIUS "
                + "AND EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' "
                + "AND "
                + "("
                + "(NVL(EVENTO.COD_MOTIVO, '-') = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') "
                + "OR "
                + "(EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO')"
                + ") "
                + "AND EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' "
                + "AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' ) "
                + "AND (EVENTO.COD_TIPO_PROVVEDIMENTO  IN ('03','02') AND EVENTO.COD_ESITO NOT IN ('0600','0601','0602','0603','0604')) "
                + "AND DA.EVE_ID_EVENTO = EVENTO.ID_EVENTO AND DA.FLAG_DOCUMENTO_REGISTRATO <> 'S' AND DA.COD_TIPO_DOCUMENTO IN('02','03')"
                + "AND sog.id_soggetto = fasc.sog_id_soggetto "
                + "ORDER BY "
                + "fasc.CHIAVE_ANNO,"
                + "fasc.CHIAVE_PROGR";        
        setStatement(lStatement);
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
}