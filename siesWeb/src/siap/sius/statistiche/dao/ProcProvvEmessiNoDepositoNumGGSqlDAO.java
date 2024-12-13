package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>Title: ProcDataUdienzaFissataNoDefinitiNumGGSqlDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ProcProvvEmessiNoDepositoNumGGSqlDAO extends SIAPSqlDAO {
    
    private RicercaProcedimentoModel mRicercaModel = null;
    
    public ProcProvvEmessiNoDepositoNumGGSqlDAO (Connection aConn) {
        super(aConn);
    }

    protected String getSqlQuery() {
        String lStatement = new String("");
        
        lStatement = 
                "SELECT fasc.id_fascicolo_sius, " +
                "  fasc.chiave_anno CHIAVE_ANNO, " +
                "  fasc.data_inserimento, " +
                "  fasc.data_iscrizione DATA_ISCRIZIONE, " +
                "  fasc.chiave_progr CHIAVE_PROGR, " +
                "  fasc.cod_stato_fascicolo, " +
                "  fasc.chiave_ufficio CHIAVE_UFFICIO, " +
                "  fasc.data_definizione, " +
                "  uff.cod_tipo_ufficio, " +
                "  sogg.id_soggetto, " +
                "  sogg.cognome COGNOME, " +
                "  sogg.nome NOME, " +
                "  sogg.data_nascita DATA_NASCITA, " +
                "  sogg.cod_provincia_nascita, " +
                "  oggetto_procedimento.rv_meaning DESCR_PROCEDIMENTO, " +
                "  pos_giuridica.rv_meaning DESCR_POS_GIURIDICA, " +
                "  comune_uff.descrizione DESCR_COMUNE_UFFICIO, " +
                "  comune_nascita.descrizione DESCR_COMUNE_NASCITA, " +
                "  gp.cod_posizione_giuridica, " +
                "  gp.cod_oggetto_procedimento, " +
                "  ev.id_evento ID_EVENTO, " +
                "  ev.data_emissione EV_DATA_EMISSIONE, " +
                "  da.data_emissione DA_DATA_EMISSIONE, " +
                "  gp.data_camera_consiglio DATA_CAMERA_CONSIGLIO, mag.cognome MAG_COGNOME, mag.nome MAG_NOME, " +
                "  TRUNC( TO_DATE ('" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "', 'YYYYMMDD') - ev.data_emissione ) DIFF_GIORNI " +
                "FROM fascicolo_sius fasc " +
                "INNER JOIN evento ev " +
                "ON ev.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " +
                "AND ev.id_evento                = " +
                "  (SELECT MAX (id_evento ) " +
                "  FROM evento " +
                "  WHERE fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " +
                "  AND cod_tipo_provvedimento     IN ('02', '03' ) " +
                "  AND (NVL(flag_documento_registrato,' ')) <> 'A' " +
                "  AND ev.cod_esito NOT           IN ('0601', '0602', '0603', '0604', '0605' ) " +
                "  ) " +
                "LEFT OUTER JOIN documento_allegato da " +
                "ON da.eve_id_evento        = ev.id_evento " +
                "AND da.cod_tipo_documento IN ('02', '03' ), " +
                "  soggetto sogg, " +
                "  generale_procedimento gp, " +
                "  cg_ref_codes oggetto_procedimento, " +
                "  cg_ref_codes pos_giuridica, " +
                "  ufficio uff, " +
                "  comune comune_uff, " +
                "  comune comune_nascita, " +
                "  magistrato mag, " +
                "  DUAL " +
                "WHERE fasc.sog_id_soggetto                                             = sogg.id_soggetto " +
                "AND fasc.id_fascicolo_sius                                             = gp.fas_siu_id_fascicolo_sius " +
                "AND oggetto_procedimento.rv_domain                                     = 'OGGETTO_PROCEDIMENTO' " +
                "AND gp.cod_oggetto_procedimento                                        = oggetto_procedimento.rv_low_value " +
                "AND pos_giuridica.rv_domain                                            = 'POSIZIONE_GIURIDICA' " +
                "AND (NVL (gp.cod_posizione_giuridica, '-')                             = pos_giuridica.rv_low_value ) " +
                "AND uff.cod_ufficio                                                    = fasc.chiave_ufficio " +
                "AND uff.cod_comune                                                     = comune_uff.cod_comune " +
                "AND sogg.cod_comune_nascita                                            = comune_nascita.cod_comune " +
                "AND fasc.chiave_ufficio                                                = '" + mRicercaModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + "' ";
            if (mRicercaModel.getDataIscrizioneInizio() != null) {
                lStatement += "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') >= '" + DateUtils.getDateToString(mRicercaModel.getDataIscrizioneInizio(), "yyyyMMdd") + "' ";
            }
            if (mRicercaModel.getDataIscrizioneFine() != null) {
                lStatement += "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') <= '" + DateUtils.getDateToString(mRicercaModel.getDataIscrizioneFine(), "yyyyMMdd") + "' ";
            }
            if (mRicercaModel.getDataEmissioneInizio() != null) {
                lStatement += "AND TO_CHAR (ev.data_emissione, 'yyyyMMdd') >= '" + DateUtils.getDateToString(mRicercaModel.getDataEmissioneInizio(), "yyyyMMdd") + "' "; 
            }
            if (mRicercaModel.getDataEmissioneFine() != null) {
                lStatement += "AND TO_CHAR (ev.data_emissione, 'yyyyMMdd') <= '" + DateUtils.getDateToString(mRicercaModel.getDataEmissioneFine(), "yyyyMMdd") + "' "; 
            }
            
            lStatement += "AND (NVL(ev.cod_magistrato,'-')) = mag.cod_magistrato and mag.cod_ufficio_appartenenza = fasc.chiave_ufficio ";
            
            lStatement += " AND ((TRUNC (TO_DATE ('" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "', 'YYYYMMDD')) - ev.data_emissione ) > " + mRicercaModel.getNumeroGiorni() + " " +
                    	  " AND ( cod_stato_fascicolo NOT                                         IN ('01', '05') " +
                    	  " AND TO_CHAR (fasc.data_inserimento, 'YYYYMMDD')                       <= '" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "' ) " +
                    	  " OR ( cod_stato_fascicolo                                              IN ('01', '05') " +
                    	  " AND TO_CHAR (fasc.data_definizione, 'YYYYMMDD')                        > '" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "' ) ) " +
                          " AND ((TRUNC(TO_DATE('" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "', 'YYYYMMDD')) - ev.data_emissione)     > " + mRicercaModel.getNumeroGiorni() + " " +
            			  " AND (cod_stato_fascicolo NOT IN ('07')) " +
                    	  " OR ( cod_stato_fascicolo = '07' " +
                    	  " AND ((fasc.id_fascicolo_sius) =  " +
                                 " (SELECT fas_siu_id_fascicolo_sius " +
                                    " FROM evento ev LEFT OUTER JOIN documento_allegato da ON da.eve_id_evento = ev.id_evento " +
                                                                                      " AND da.cod_tipo_documento IN " +
                                                                                             " ('02', " + 
                                                                                             "  '03' " +
                                                                                             " ) " +
                                   " WHERE ev.fas_siu_id_fascicolo_sius = " +
                                                                   " fasc.id_fascicolo_sius " +
                                     " AND (   ev.cod_tipo_provvedimento = '02' " +
                                          " OR ev.cod_tipo_provvedimento = '03' " +
                                         " ) " +
                                     " AND ev.data_inserimento = " + 
                                            " (SELECT MAX (data_inserimento) " +
                                               " FROM evento ev2 " +
                                              " WHERE ev2.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " +
                                                " AND (   ev2.cod_tipo_provvedimento = '02' " +
                                                     " OR ev2.cod_tipo_provvedimento = '03' "+
                                                    " ) "+
                                                " AND TO_CHAR (da.data_emissione, 'YYYYMMDD') > '" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "')) " +
                             " ) " +
                        " ) " +
                    " ) " +
           " ORDER BY fasc.data_iscrizione ASC ";


            /*
            	"AND (NVL(ev.cod_magistrato,'-')) = mag.cod_magistrato and mag.cod_ufficio_appartenenza = fasc.chiave_ufficio " +
                "AND ((TRUNC(TO_DATE('" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "', 'YYYYMMDD')) - ev.data_emissione)     > " + mRicercaModel.getNumeroGiorni() + " " +
                "AND ( cod_stato_fascicolo NOT                                         IN ('01', '05') " +
                "AND TO_CHAR (fasc.data_inserimento, 'YYYYMMDD')                       <= '" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "' ) " +
                "OR ( cod_stato_fascicolo                                              IN ('01', '05') " +
                "AND TO_CHAR (fasc.data_definizione, 'YYYYMMDD')                        > '" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "' ) ) " +
                "AND ((TRUNC(TO_DATE('" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "', 'YYYYMMDD')) - ev.data_emissione)     > " + mRicercaModel.getNumeroGiorni() + " " +
                "AND (cod_stato_fascicolo NOT                                          IN ('07')) " +
                "OR ( cod_stato_fascicolo                                               = '07' " +
                "AND TO_CHAR (da.data_emissione, 'YYYYMMDD' )                           > '" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "') ) " +
                "ORDER BY fasc.data_iscrizione ASC  ";
            	*/
            

            
            
            
        return lStatement;
    }
    
    
    
    
    

    //
    // METODO GETMODEL()
    //
    public GenericModel getModel() throws DAOException {
        EveFasGepSogProvModel lModel = new EveFasGepSogProvModel();
        
        lModel.setFascicoloSius(new FascicoloSiusModel());
        lModel.getFascicoloSius().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
        lModel.getFascicoloSius().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
        lModel.getFascicoloSius().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
        lModel.getFascicoloSius().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
        //lModel.getFascicoloSius().setDataInserimento(getDate("DATA_INSERIMENTO"));
        lModel.getFascicoloSius().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
        
        lModel.getFascicoloSius().setSoggetto(new SoggettoModel());
        lModel.getFascicoloSius().getSoggetto().setCognome(getString("COGNOME"));
        lModel.getFascicoloSius().getSoggetto().setNome(getString("NOME"));
        lModel.getFascicoloSius().getSoggetto().setDataNascita(getDate("DATA_NASCITA"));
        lModel.getFascicoloSius().getSoggetto().setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
        
        lModel.setGeneraleProcedimento(new GeneraleProcedimentoModel());
        lModel.getGeneraleProcedimento().setDescrPosGiuridica(getString("DESCR_POS_GIURIDICA"));
        lModel.getGeneraleProcedimento().setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
        
        lModel.setEvento(new EventoModel());
        lModel.getEvento().setIdEvento(getBigDecimal("ID_EVENTO"));
        lModel.getEvento().setDataEmissione(getDate("EV_DATA_EMISSIONE"));
        
        lModel.setMagistrato(new MagistratoModel() );
        lModel.getMagistrato().setNome(getString("MAG_NOME"));
        lModel.getMagistrato().setCognome(getString("MAG_COGNOME"));
        
        lModel.setTotale(getInteger("DIFF_GIORNI"));

        return lModel;
    }
/*
    protected String getCondizione(RicercaProcedimentoModel aModel) {
        String lCondizione = new String();
        
        lCondizione =  " AND FASC.CHIAVE_UFFICIO = '" + 
                       aModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + 
                       "' ";
        
        if (aModel.getDataCameraConsiglioInizio() != null) {
            lCondizione += " AND TO_CHAR (GP.DATA_CAMERA_CONSIGLIO, 'yyyyMMdd') >= '" + 
                           DateUtils.getDateToString(aModel.getDataCameraConsiglioInizio(), "yyyyMMdd") + 
                           "' ";
        }
        
        if (aModel.getDataCameraConsiglioFine() != null) {
            lCondizione += " AND TO_CHAR (GP.DATA_CAMERA_CONSIGLIO, 'yyyyMMdd') <= '" + 
                           DateUtils.getDateToString(aModel.getDataCameraConsiglioFine(), "yyyyMMdd") + 
                           "' ";
        }
        
        if (aModel.getDataIscrizioneInizio() != null) {
            lCondizione += " AND TO_CHAR (FASC.DATA_ISCRIZIONE, 'yyyyMMdd') >= '" +
                           DateUtils.getDateToString(aModel.getDataIscrizioneInizio(), "yyyyMMdd") +
                           "' ";
        }
        
        if (aModel.getDataIscrizioneFine() != null) {
            lCondizione += " AND TO_CHAR (FASC.DATA_ISCRIZIONE, 'yyyyMMdd') <= '" +
                           DateUtils.getDateToString(aModel.getDataIscrizioneFine(), "yyyyMMdd") +
                           "' ";
        }
        
        if (aModel.getNumeroGiorni() != null ) {
        	lCondizione += " AND TRUNC(GP.DATA_CAMERA_CONSIGLIO) - TRUNC(FASC.DATA_ISCRIZIONE) > " + aModel.getNumeroGiorni();
        }
        
        return lCondizione;
    }
*/    
    public void ricercaProcProvvEmessiNoDepositoNumGG(RicercaProcedimentoModel aModel){
    	String lStatement = "";
    	
    	mRicercaModel = new RicercaProcedimentoModel(aModel);
    	
    	lStatement = this.getSqlQuery();

    	setStatement(lStatement);
    }
}