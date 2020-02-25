package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.statistiche.model.EveFasGepSogDetModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.udienza.model.UdienzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>Title: ProcDataUdienzaFissataNoDefinitiNumGGSqlDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ProcDataUdienzaFissataNoDefinitiNumGGSqlDAO extends SIAPSqlDAO {
    
    private RicercaProcedimentoModel mRicercaModel = null;
    
    public ProcDataUdienzaFissataNoDefinitiNumGGSqlDAO (Connection aConn) {
        super(aConn);
    }

    protected String getSqlQuery() {
        String lStatement = new String("");
        
        lStatement = 
                "SELECT fasc.id_fascicolo_sius , " +
                "  fasc.chiave_anno CHIAVE_ANNO, " +
                "  fasc.data_inserimento , " +
                "  fasc.data_iscrizione DATA_ISCRIZIONE, " +
                "  fasc.chiave_progr CHIAVE_PROGR, " +
                "  fasc.cod_stato_fascicolo, " +
                "  ud.data_udienza, " +
                "  fasc.chiave_ufficio CHIAVE_UFFICIO, " +
                "  fasc.data_definizione, " +
                "  uff.cod_tipo_ufficio , " +
                "  sogg.id_soggetto , " +
                "  sogg.cognome COGNOME, " +
                "  sogg.nome NOME, " +
                "  sogg.data_nascita DATA_NASCITA, " +
                "  sogg.cod_provincia_nascita, " +
                "  oggetto_procedimento.rv_meaning DESCR_PROCEDIMENTO, " +
                "  pos_giuridica.rv_meaning DESCR_POS_GIURIDICA, " +
                "  comune_uff.descrizione descr_comune_ufficio, " +
                "  comune_nascita.descrizione DESCR_COMUNE_NASCITA, " +
                "  gp.cod_posizione_giuridica, " +
                "  gp.cod_oggetto_procedimento, " +
                "  ev.data_emissione EV_DATA_EMISSIONE,  " +
                "  da.data_emissione DA_DATA_EMISSIONE, " +
                "  gp.data_camera_consiglio DATA_CAMERA_CONSIGLIO, " +
                "  gp.data_fine_pena DATA_FINE_PENA, " + 
                "  gp.data_richiesta DATA_RICHIESTA, " +
                "  NVL (gp.descr_mittente, '-') DESCR_MITTENTE, " +
                "  TRUNC(TO_DATE('" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "','YYYYMMDD') - ud.data_udienza) DIFF_GIORNI, " +
                "  mr.mag_cod_magistrato, "+ 
                "  mag.cognome mag_cognome, " +
                "  mag.nome mag_nome " +
                "FROM FASCICOLO_SIUS fasc " +
                " LEFT OUTER JOIN MAGISTRATO_RELATORE mr on mr.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius and mr.data_fine is null " +
                " LEFT OUTER JOIN EVENTO ev " +
                " ON ev.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " +
                " AND ev.id_evento                = " +
                "  (SELECT MAX (id_evento) " +
                "  FROM EVENTO " +
                "  WHERE fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " +
                "  AND cod_tipo_provvedimento     IN ('02','03') " +
                // 20140404 - aggiunta di nuova condizione
                "  AND (NVL(flag_documento_registrato,' ')) <> 'A' " +
                "  AND ev.cod_esito NOT IN ('0601','0602','0603','0604','0605') " +
                "  ) " +
                "LEFT OUTER JOIN DOCUMENTO_ALLEGATO da " +
                "ON da.eve_id_evento        = ev.id_evento " +
                "AND da.cod_tipo_documento IN ('02','03'), " +
                "  SOGGETTO sogg, " +
                "  GENERALE_PROCEDIMENTO gp, " +
                "  CG_REF_CODES oggetto_procedimento, " +
                "  CG_REF_CODES pos_giuridica, " +
                "  UFFICIO uff, " +
                "  COMUNE comune_uff, " +
                "  COMUNE comune_nascita, " +
                "  dual, " +
                "  UDIENZA_PROCEDIMENTO up, " +
                "  UDIENZA ud, w_magistrato mag " +
                "WHERE fasc.sog_id_soggetto                 = sogg.id_soggetto " +
                "AND fasc.id_fascicolo_sius                 = gp.fas_siu_id_fascicolo_sius " +
                "AND oggetto_procedimento.rv_domain         = 'OGGETTO_PROCEDIMENTO' " +
                "AND gp.cod_oggetto_procedimento            = oggetto_procedimento.rv_low_value " +
                "AND pos_giuridica.rv_domain                = 'POSIZIONE_GIURIDICA' " +
                "AND (NVL (gp.cod_posizione_giuridica, '-') = pos_giuridica.rv_low_value ) " +
                "AND uff.cod_ufficio                        = fasc.chiave_ufficio " +
                "AND uff.cod_comune                         = comune_uff.cod_comune " +
                "AND sogg.cod_comune_nascita                = comune_nascita.cod_comune " +
                "AND (NVL (mr.mag_cod_magistrato,'-')     = mag.cod_magistrato ) " +
                "AND gp.udi_id_udienza                      IS NOT NULL " +
                "AND fasc.chiave_ufficio                    = '" + mRicercaModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + "' " +
                "AND up.id_udienza_procedimento             = " +
                "  (SELECT MIN (id_udienza_procedimento) " +
                "  FROM UDIENZA_PROCEDIMENTO " +
                "  WHERE gen_prid_generale_procedimento = gp.id_generale_procedimento " +
                "  AND FLAG_RINVIATA                   <> 'A' " +
                "  ) " +
                "AND ud.id_udienza                                                    = up.udi_id_udienza ";
        if (mRicercaModel.getDataIscrizioneInizio() != null) {
            lStatement += "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') >= '" + DateUtils.getDateToString(mRicercaModel.getDataIscrizioneInizio(), "yyyyMMdd") + "' ";
        }
        if (mRicercaModel.getDataIscrizioneFine() != null) {
            lStatement += "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') <= '" + DateUtils.getDateToString(mRicercaModel.getDataIscrizioneFine(), "yyyyMMdd") + "' ";
        }
        if (mRicercaModel.getDataCameraConsiglioInizio() != null) {
            lStatement += "AND TO_CHAR (ud.data_udienza, 'yyyyMMdd') >= '" + DateUtils.getDateToString(mRicercaModel.getDataCameraConsiglioInizio(), "yyyyMMdd") + "' "; 
        }
        if (mRicercaModel.getDataCameraConsiglioFine() != null) {
            lStatement += "AND TO_CHAR (ud.data_udienza, 'yyyyMMdd') <= '" + DateUtils.getDateToString(mRicercaModel.getDataCameraConsiglioFine(), "yyyyMMdd") + "' "; 
        }
        lStatement +=
                "AND ( ( TRUNC(TO_DATE('" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "','YYYYMMDD'))                    - ud.data_udienza) > " + mRicercaModel.getNumeroGiorni() + " " +
                "AND ( cod_stato_fascicolo NOT                                       IN ('01', '05') " +
                "AND TO_CHAR (fasc.data_inserimento, 'YYYYMMDD')                     <= '" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "' ) " +
                "OR ( cod_stato_fascicolo                                            IN ('01', '05') " +
                "AND TO_CHAR (fasc.data_definizione, 'YYYYMMDD')                      > '" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "' ) ) " +
                "AND ( ( TRUNC(TO_DATE('" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "','YYYYMMDD')) - ud.data_udienza) > " + mRicercaModel.getNumeroGiorni() + " " +
                "AND (cod_stato_fascicolo NOT                                        IN ('07')) " +
                "OR ( cod_stato_fascicolo                                             = '07' " +
                "AND ((fasc.id_fascicolo_sius)                                        = " +
                "  (SELECT fas_siu_id_fascicolo_sius " +
                "  FROM evento ev " +
                "  LEFT OUTER JOIN documento_allegato da " +
                "  ON da.eve_id_evento                = ev.id_evento " +
                "  AND da.cod_tipo_documento         IN ('02', '03' ) " +
                "  WHERE ev.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " +
                "  AND ( ev.cod_tipo_provvedimento    = '02' " +
                "  OR ev.cod_tipo_provvedimento       = '03' ) " +
                "  AND ev.data_inserimento            = " +
                "    (SELECT MAX (data_inserimento) " +
                "    FROM evento ev2 " +
                "    WHERE ev2.fas_siu_id_fascicolo_sius          = fasc.id_fascicolo_sius " +
                "    AND ( ev2.cod_tipo_provvedimento             = '02' " +
                "    OR ev2.cod_tipo_provvedimento                = '03' ) " +
                "    AND TO_CHAR (da.data_emissione, 'YYYYMMDD' ) > '" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "' " +
                "    ) " +
                "  ) ) ) ) " +
                "ORDER BY fasc.data_iscrizione ASC ";
                    
        return lStatement;
    }

    
  /**
   * Come getSqlQuery ma modificata per errore su Oracle:
   *   -01799 Una colonna non può essere collegataad una query secondaria con un join esterno
   * 
   * @return
   */
  protected String getSqlQuery1() {
    String lStatement = new String("");
    
    lStatement = "SELECT fasc.id_fascicolo_sius , " +
                      "  fasc.chiave_anno CHIAVE_ANNO, " +
                      "  fasc.data_inserimento , " +
                      "  fasc.data_iscrizione DATA_ISCRIZIONE, " +
                      "  fasc.chiave_progr CHIAVE_PROGR, " +
                      "  fasc.cod_stato_fascicolo, " +
                      "  ud.data_udienza, " +
                      "  fasc.chiave_ufficio CHIAVE_UFFICIO, " +
                      "  fasc.data_definizione, " +
                      "  uff.cod_tipo_ufficio , " +
                      "  sogg.id_soggetto , " +
                      "  sogg.cognome COGNOME, " +
                      "  sogg.nome NOME, " +
                      "  sogg.data_nascita DATA_NASCITA, " +
                      "  sogg.cod_provincia_nascita, " +
                      "  oggetto_procedimento.rv_meaning DESCR_PROCEDIMENTO, " +
                      "  pos_giuridica.rv_meaning DESCR_POS_GIURIDICA, " +
                      "  comune_uff.descrizione descr_comune_ufficio, " +
                      "  comune_nascita.descrizione DESCR_COMUNE_NASCITA, " +
                      "  gp.cod_posizione_giuridica, " +
                      "  gp.cod_oggetto_procedimento, " +
 "  evento_app.data_emissione EV_DATA_EMISSIONE,  " +
//"  null EV_DATA_EMISSIONE,  " +
                      "  da.data_emissione DA_DATA_EMISSIONE, " +
                      "  gp.data_camera_consiglio DATA_CAMERA_CONSIGLIO, " +
                      "  gp.data_fine_pena DATA_FINE_PENA, " + 
                      "  gp.data_richiesta DATA_RICHIESTA, " +
                      "  NVL (gp.descr_mittente, '-') DESCR_MITTENTE, " +
                      "  TRUNC(TO_DATE('" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "','YYYYMMDD') - ud.data_udienza) DIFF_GIORNI, " +
                      "  mr.mag_cod_magistrato, "+ 
                      "  mag.cognome mag_cognome, " +
                      "  mag.nome mag_nome ";
              
    lStatement += " FROM FASCICOLO_SIUS fasc " ;
    lStatement += 		" LEFT OUTER JOIN MAGISTRATO_RELATORE mr on mr.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius and mr.data_fine is null " ;
    // Condizione su EVENTO max(id)   
    lStatement += " LEFT OUTER JOIN  (SELECT ev.id_evento, ev.fas_siu_id_fascicolo_sius, ev.data_emissione " +
                                      " FROM evento ev, FASCICOLO_SIUS FASC " +
                                     " WHERE ev.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " + 
                                       " AND FASC.CHIAVE_UFFICIO = '" + mRicercaModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + "' " +               
                                       " AND ev.id_evento = (SELECT MAX (id_evento) " +
                                                             " FROM EVENTO " +
                                                            " WHERE fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius "+
                                                              " AND cod_tipo_provvedimento IN ('02', '03') "+
                                                              " AND (NVL (flag_documento_registrato, ' ')) <> 'A' "+
                                                              " AND ev.cod_esito NOT IN ('0601', '0602', '0603', '0604', '0605') "+
                                         " )) evento_app " +
                   " ON evento_app.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " ;  		
    // OUTER JOIN SU DOCUMENTO_ALLEGATO
    lStatement += " LEFT OUTER JOIN DOCUMENTO_ALLEGATO da ON da.eve_id_evento = evento_app.id_evento " +
                                                       " AND da.cod_tipo_documento IN ('02','03') " ;
    lStatement += " , SOGGETTO sogg, " +
                  "  GENERALE_PROCEDIMENTO gp, " +
                  "  CG_REF_CODES oggetto_procedimento, " +
                  "  CG_REF_CODES pos_giuridica, " +
                  "  UFFICIO uff, " +
                  "  COMUNE comune_uff, " +
                  "  COMUNE comune_nascita, " +
                  "  UDIENZA_PROCEDIMENTO up, " +
                  "  UDIENZA ud, w_magistrato mag " ;
    // WHERE CONDITION
    lStatement += " WHERE fasc.sog_id_soggetto                   = sogg.id_soggetto " +
                    " AND fasc.id_fascicolo_sius                 = gp.fas_siu_id_fascicolo_sius " +
                    " AND oggetto_procedimento.rv_domain         = 'OGGETTO_PROCEDIMENTO' " +
                    " AND gp.cod_oggetto_procedimento            = oggetto_procedimento.rv_low_value " +
                    " AND pos_giuridica.rv_domain                = 'POSIZIONE_GIURIDICA' " +
                    " AND (NVL (gp.cod_posizione_giuridica, '-') = pos_giuridica.rv_low_value ) " +
                    " AND uff.cod_ufficio                        = fasc.chiave_ufficio " +
                    " AND uff.cod_comune                         = comune_uff.cod_comune " +
                    " AND sogg.cod_comune_nascita                = comune_nascita.cod_comune " +
                    " AND (NVL (mr.mag_cod_magistrato,'-')       = mag.cod_magistrato ) " +
                    " AND gp.udi_id_udienza                      IS NOT NULL " +
                    " AND up.id_udienza_procedimento             = ( SELECT MIN (id_udienza_procedimento) " +
                                                                    "  FROM UDIENZA_PROCEDIMENTO " +
                                                                   "  WHERE gen_prid_generale_procedimento = gp.id_generale_procedimento " +
                                                                     "  AND FLAG_RINVIATA  <> 'A' " +
                                                                  "  ) " +
                    " AND ud.id_udienza                          = up.udi_id_udienza ";
    //===========================
    // Condizione sull'ufficio
    //===========================
    lStatement +=  " AND fasc.chiave_ufficio                    = '" + mRicercaModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + "' " ;
    //=========================
    // Data IScrizione DAL-AL
    //=========================
    if (mRicercaModel.getDataIscrizioneInizio() != null) {
        lStatement += "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') >= '" + DateUtils.getDateToString(mRicercaModel.getDataIscrizioneInizio(), "yyyyMMdd") + "' ";
    }
    if (mRicercaModel.getDataIscrizioneFine() != null) {
        lStatement += "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') <= '" + DateUtils.getDateToString(mRicercaModel.getDataIscrizioneFine(), "yyyyMMdd") + "' ";
    }
    //=================
    // Fissati DAL-AL
    //=================
    if (mRicercaModel.getDataCameraConsiglioInizio() != null) {
        lStatement += "AND TO_CHAR (ud.data_udienza, 'yyyyMMdd') >= '" + DateUtils.getDateToString(mRicercaModel.getDataCameraConsiglioInizio(), "yyyyMMdd") + "' "; 
    }
    if (mRicercaModel.getDataCameraConsiglioFine() != null) {
        lStatement += "AND TO_CHAR (ud.data_udienza, 'yyyyMMdd') <= '" + DateUtils.getDateToString(mRicercaModel.getDataCameraConsiglioFine(), "yyyyMMdd") + "' "; 
    }
    //
    lStatement += " AND (    ( TRUNC(TO_DATE('" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "','YYYYMMDD')) - ud.data_udienza) > " + mRicercaModel.getNumeroGiorni() + " " +
                       " AND ( cod_stato_fascicolo NOT  IN ('01', '05') AND TO_CHAR (fasc.data_inserimento, 'YYYYMMDD')  <= '" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "' ) " +
                       "  OR ( cod_stato_fascicolo IN ('01', '05') AND TO_CHAR (fasc.data_definizione, 'YYYYMMDD')  > '" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "' ) " +
                      " ) ";
    // ???
    lStatement += " AND ( ( TRUNC(TO_DATE('" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "','YYYYMMDD')) - ud.data_udienza) > " + mRicercaModel.getNumeroGiorni() + " " +
                  " AND (cod_stato_fascicolo NOT IN ('07')) " +
                  "  OR (    cod_stato_fascicolo = '07' " +
                       " AND (fasc.id_fascicolo_sius = " +
            "  ( SELECT fas_siu_id_fascicolo_sius " +
               "  FROM evento ev LEFT OUTER JOIN documento_allegato da " +
                                            " ON da.eve_id_evento = ev.id_evento " +
                                           " AND da.cod_tipo_documento IN ('02', '03' ) " +
              "  WHERE ev.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " +
                "  AND ev.cod_tipo_provvedimento in ('02','03') " +
                "  AND ev.data_inserimento = (SELECT MAX (data_inserimento) " +
                                              " FROM evento ev2 " +
                                             " WHERE ev2.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " +
                                               " AND ev2.cod_tipo_provvedimento in ('02','03') " +
                                               " AND TO_CHAR (da.data_emissione, 'YYYYMMDD' ) > '" + DateUtils.getDateToString(mRicercaModel.getDataFine(), "yyyyMMdd") +  "' " +
                                           " ) " +
            "  ) " +
            ") ) ) " ;
    
    lStatement +=" ORDER BY fasc.data_iscrizione ASC ";
                  
    return lStatement;
  }

    //
    // METODO GETMODEL()
    //
    public GenericModel getModel() throws DAOException {
        EveFasGepSogDetModel lModel = new EveFasGepSogDetModel();
        
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
        lModel.getGeneraleProcedimento().setDataRichiesta(getDate("DATA_RICHIESTA"));
        lModel.getGeneraleProcedimento().setDataFinePena(getDate("DATA_FINE_PENA"));
        lModel.getGeneraleProcedimento().setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));
        lModel.getGeneraleProcedimento().setDescrMittente(getString("DESCR_MITTENTE"));
        
        lModel.getGeneraleProcedimento().setUdienza(new UdienzaModel());
        lModel.getGeneraleProcedimento().getUdienza().setDataUdienza(getDate("DATA_UDIENZA"));
                
        lModel.setEvento(new EventoModel());
        lModel.getEvento().setDataEmissione(getDate("EV_DATA_EMISSIONE"));
        
        lModel.setDocumentoAllegato(new DocumentoAllegatoModel());
        lModel.getDocumentoAllegato().setDataEmissione(getDate("DA_DATA_EMISSIONE"));
        
        lModel.setMagistrato(new MagistratoModel());
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
    public void ricercaProcDataUdienzaFissataNoDefinitiNumGG(RicercaProcedimentoModel aModel){
      String lStatement = "";
      
      mRicercaModel = new RicercaProcedimentoModel(aModel);
      
      lStatement = this.getSqlQuery1();

      setStatement(lStatement);
    }
}