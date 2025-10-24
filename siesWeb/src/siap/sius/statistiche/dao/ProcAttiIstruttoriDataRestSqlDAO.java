package siap.sius.statistiche.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;

public class ProcAttiIstruttoriDataRestSqlDAO extends SIAPSqlDAO {

	public ProcAttiIstruttoriDataRestSqlDAO(Connection aCon) {
		super(aCon);
	}
	
	
	public void ricercaProcedimentiAttiIstruttoriDataRest (RicercaProcedimentoModel aModel) {
        String lStatement = "";
        
        lStatement = 
                "SELECT fasc.ID_FASCICOLO_SIUS, "
		                + "fasc.CHIAVE_ANNO, "
		                + "fasc.CHIAVE_PROGR, "
		                + "fasc.DATA_ISCRIZIONE, "
		                + "sog.COGNOME, "
		                + "sog.NOME, "
		                + "EVENTO.ID_EVENTO ID_EVENTO, "                
		                + "EVENTO.DATA_EMISSIONE DATA_EMISSIONE, "
		                + "EVENTO.DATA_RESTITUZIONE_AI DATA_RESTITUZIONE_ATTI, "
		                + "CODCONT.RV_MEANING OGGETTO, "
		                + "CODMOV.RV_MEANING TIPO_ATTO_ISTRUTTORIO, "                
		                + "CODSTATO.RV_MEANING DESC_STATO_PROCEDIMENTO " 
                + "FROM "
		                + "FASCICOLO_SIUS fasc, "
		                + "EVENTO, "
		                + "CG_REF_CODES CODCONT, "
		                + "CG_REF_CODES CODMOV, "
		                + "CG_REF_CODES CODSTATO, "
		                + "SOGGETTO sog, "
		                + "GENERALE_PROCEDIMENTO GP "
                + "WHERE "
                + this.getCondizione(aModel)
                + "AND EVENTO.FAS_SIU_ID_FASCICOLO_SIUS = fasc.ID_FASCICOLO_SIUS "
                + "AND EVENTO.COD_TIPO_EVENTO IN ('05') "
                + "AND EVENTO.DATA_RESTITUZIONE_AI IS NOT NULL "
                + "AND GP.FAS_SIU_ID_FASCICOLO_SIUS=fasc.ID_FASCICOLO_SIUS "
                + "AND (GP.COD_OGGETTO_PROCEDIMENTO = CODCONT.RV_LOW_VALUE AND CODCONT.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') "
                + "AND (EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') "
                + "AND (FASC.COD_STATO_FASCICOLO = CODSTATO.RV_LOW_VALUE AND CODSTATO.RV_DOMAIN = 'STATO_FASCICOLO') "
                + "AND sog.id_soggetto = fasc.sog_id_soggetto "
                + "ORDER BY fasc.CHIAVE_ANNO, fasc.CHIAVE_PROGR";        
        setStatement(lStatement);
    }	
	
	
	private String getCondizione(RicercaProcedimentoModel aModel) {
	    String lCondizione = "";
        String lDataPattern = "yyyyMMdd";
	    
        lCondizione = "fasc.CHIAVE_UFFICIO = '" + aModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + "' ";

        if (aModel.getAnnoInizio() != null) {
        	BigDecimal numIniziale = aModel.getNumeroInizio();
        	if (numIniziale==null) numIniziale = new BigDecimal(1);		
            lCondizione += " AND ( CHIAVE_ANNO||LPAD(CHIAVE_PROGR,38,'0') >= "+aModel.getAnnoInizio()+"||LPAD("+numIniziale+",38,'0')) ";
        }
        if (aModel.getAnnoFine() != null) {
        	BigDecimal numFinale = aModel.getNumeroFine();
        	if (numFinale==null) numFinale = new BigDecimal(1000000000); // numero volutamente elevato '
            lCondizione += " AND ( CHIAVE_ANNO||LPAD(CHIAVE_PROGR,38,'0') <= "+aModel.getAnnoFine()+"||LPAD("+numFinale+",38,'0')) ";
        }        
        
        if (aModel.getDataDepositoInizio() != null) {
            lCondizione += "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') >= '" + DateUtils.getDateToString(aModel.getDataDepositoInizio(), lDataPattern) + "' ";
        }
        if (aModel.getDataDepositoFine() != null) {
            lCondizione += "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') <= '" + DateUtils.getDateToString(aModel.getDataDepositoFine(), lDataPattern) + "' ";
        }
        
        if (aModel.getDataRestituzioneInizio() != null) {
            lCondizione += "AND TO_CHAR (evento.data_restituzione_ai, 'yyyyMMdd') >= '" + DateUtils.getDateToString(aModel.getDataRestituzioneInizio(), lDataPattern) + "' ";
        }
        if (aModel.getDataRestituzioneFine() != null) {
            lCondizione += "AND TO_CHAR (evento.data_restituzione_ai, 'yyyyMMdd') <= '" + DateUtils.getDateToString(aModel.getDataRestituzioneFine(), lDataPattern) + "' ";
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
        lModel.getFascicoloSius().setDescrStatoFascicolo(getString("DESC_STATO_PROCEDIMENTO"));
        
        // popola fascicolo sius -> Soggetto.
        lModel.getFascicoloSius().setSoggetto(new SoggettoModel());
        lModel.getFascicoloSius().getSoggetto().setCognome(getString("COGNOME"));
        lModel.getFascicoloSius().getSoggetto().setNome(getString("NOME"));
        
        // popola evento.
        lModel.setEvento(new EventoModel());
        lModel.getEvento().setIdEvento(getBigDecimal("ID_EVENTO"));
        lModel.getEvento().setDataEmissione(getDate("DATA_EMISSIONE"));
        lModel.getEvento().setDataRestituzioneAi(getDate("DATA_RESTITUZIONE_ATTI"));
        lModel.getEvento().setDescrMotivo(getString("TIPO_ATTO_ISTRUTTORIO"));
        
        // popola generale procedimento.
        lModel.setGeneraleProcedimento(new GeneraleProcedimentoModel());
        lModel.getGeneraleProcedimento().setDescrOggettoProcedimento(getString("OGGETTO"));
        
	    return lModel;
	}
	
}
