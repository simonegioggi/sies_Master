package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.statistiche.model.EveFasGepSogCancModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class ProcPosizioneGiuridicaSqlDAO extends SIAPSqlDAO {

  public ProcPosizioneGiuridicaSqlDAO(Connection aCon) {
    super(aCon);
  }
  
  public void ricercaProcedimentiPosizioneGiuridicaOrig(RicercaProcedimentoModel aModel) {
        String lStatement = "";
        String lDataPattern = "yyyyMMdd";
        lStatement =  " SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, "
                  +" FASC.CHIAVE_ANNO CHIAVE_ANNO, "
                  +" SOGG.ID_SOGGETTO ID_SOGGETTO, "
                  +" FASC.CHIAVE_PROGR CHIAVE_PROGR, "
                  +" FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, "
                  +" UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , "
                  +" FASC.DATA_INSERIMENTO DATA_INSERIMENTO, "
                  +" FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, "
                  //+" FASC.DATA_DEFINIZIONE DATA_DEFINIZIONE, "
                  // La data di definizione se è null, ossia c,è un provvedimenti depositato
                  // si ritorna laa data di emissione del documento allegato ( data deposito )
                  +" NVL(FASC.DATA_DEFINIZIONE,DA.DATA_EMISSIONE) DATA_DEFINIZIONE, "
                  +" SOGG.ID_SOGGETTO ID_SOGGETTO, "
                  +" SOGG.COGNOME COGNOME, "
                  +" SOGG.NOME NOME, "
                  +" SOGG.DATA_NASCITA, "
                  +" DESCR_OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_PROCEDIMENTO, "
                  +" DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, "
                  +" DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, "
                  +" DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA, "
                  +" GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, "
                  +" GP.DATA_RICHIESTA DATA_RICHIESTA, "
                  +" NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, "
                  +" NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, "
                  +" GP.DATA_ARRIVO_CANCELLERIA, "
                  +" GP.DATA_CAMERA_CONSIGLIO DATA_UDIENZA, "
                  +" EV.DATA_EMISSIONE, " 
                  +" DA.DATA_EMISSIONE DATA_DEPOSITO "
                  //+" NULL DATA_DEPOSITO, "
                  //+" NULL DATA_DEFINIZIONE_EVENTO, "
                  //+" NULL DEPOSITO_VALIDATO "
                 +" FROM FASCICOLO_SIUS FASC "        
                    +" LEFT OUTER JOIN EVENTO EV ON EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS "
                      +" AND EV.ID_EVENTO = (SELECT MAX(ID_EVENTO) "
                                    +" FROM EVENTO "
                                    +" WHERE FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS "
                                    +" AND COD_TIPO_PROVVEDIMENTO IN ('02', '03' ) "
                                    +" AND EV.COD_ESITO NOT IN ('0601','0602','0603','0604','0605')) "
                     +" LEFT OUTER JOIN DOCUMENTO_ALLEGATO DA ON DA.EVE_ID_EVENTO = EV.ID_EVENTO "
                        +" AND DA.COD_TIPO_DOCUMENTO IN ('02','03' ), "
                      +" SOGGETTO SOGG, "
                  +" GENERALE_PROCEDIMENTO GP, "
                  +" CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO, "
                  +" UFFICIO UFF, "
                  +" COMUNE DESCR_COM_UFF, "
                  +" COMUNE DESCR_COM_NASCITA "
                +" WHERE FASC.SOG_ID_SOGGETTO                       = SOGG.ID_SOGGETTO "
                  +" AND FASC.ID_FASCICOLO_SIUS                     = GP.FAS_SIU_ID_FASCICOLO_SIUS "
                  +" AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN       = 'OGGETTO_PROCEDIMENTO' "
                  +" AND GP.COD_OGGETTO_PROCEDIMENTO                = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE "
                  +" AND UFF.COD_UFFICIO                            = FASC.CHIAVE_UFFICIO "
                  +" AND UFF.COD_COMUNE                             = DESCR_COM_UFF.COD_COMUNE "
                  +" AND SOGG.COD_COMUNE_NASCITA                    = DESCR_COM_NASCITA.COD_COMUNE "
                +" AND UFF.COD_UFFICIO                            = NVL('" +  aModel.getCodUfficio() + "', UFF.COD_UFFICIO ) ";
        
        if (aModel.getCodOggettoProcedimento() != null && aModel.getCodOggettoProcedimento().compareTo("-") != 0) 
          lStatement += "AND GP.COD_OGGETTO_PROCEDIMENTO = '" + aModel.getCodOggettoProcedimento() + "' ";
        
        if (aModel.getCodPosizioneGiuridica() != null && aModel.getCodPosizioneGiuridica().compareTo("-") != 0) 
            lStatement += "AND GP.COD_POSIZIONE_GIURIDICA = '" + aModel.getCodPosizioneGiuridica() + "' ";                    
        
    if ( aModel.getDataIscrizioneInizio() != null )
      lStatement += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') >= '" + DateUtils.getDateToString(aModel.getDataIscrizioneInizio(), lDataPattern)+"' " ;
    
    if ( aModel.getDataIscrizioneFine() != null ) 
      lStatement += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= '" + DateUtils.getDateToString(aModel.getDataIscrizioneFine(), lDataPattern)+ "' ";
        
    if (aModel.getCodMagistrato() != null && !aModel.getCodMagistrato().equals("-") 
        &&  !aModel.getCodMagistrato().equalsIgnoreCase("Tutti") )  {
            lStatement += " AND (GP.COD_AUTORITA_DELEGATA = NVL('"  + aModel.getCodMagistrato() +  "', GP.COD_AUTORITA_DELEGATA ) ) ";
        }
                  
    if ( aModel.getDataFinePendenza() != null ) {
      lStatement +=" "
          + " AND ( ( COD_STATO_FASCICOLO NOT IN ('01', '05') "
                    +" AND TO_CHAR (FASC.DATA_INSERIMENTO, 'YYYYMMDD') <= '" + DateUtils.getDateToString(aModel.getDataFinePendenza(),lDataPattern) + "' ) "
                     +" OR ( COD_STATO_FASCICOLO IN ('01', '05') "
                      +" AND TO_CHAR (FASC.DATA_DEFINIZIONE, 'YYYYMMDD') > '" + DateUtils.getDateToString(aModel.getDataFinePendenza(),lDataPattern) + "' ) ) " 
              +" AND( ( COD_STATO_FASCICOLO NOT IN ('07')) "
                +" OR ( COD_STATO_FASCICOLO = '07' "
                   +" AND ((FASC.ID_FASCICOLO_SIUS) = "
                           +" (SELECT FAS_SIU_ID_FASCICOLO_SIUS "
                              +" FROM EVENTO EV  "
                    +" LEFT OUTER JOIN DOCUMENTO_ALLEGATO DA  "
                          +" ON DA.EVE_ID_EVENTO = EV.ID_EVENTO  "
                          +" AND DA.COD_TIPO_DOCUMENTO IN ('02','03') "
                              +" WHERE EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS "
                                +" AND ( EV.COD_TIPO_PROVVEDIMENTO = '02' OR EV.COD_TIPO_PROVVEDIMENTO = '03') "
                                +" AND EV.DATA_INSERIMENTO = "
                                    +" (SELECT MAX (DATA_INSERIMENTO) "
                         +" FROM EVENTO EV2 "
                        +" WHERE EV2.FAS_SIU_ID_FASCICOLO_SIUS = "
                          +" FASC.ID_FASCICOLO_SIUS "
                        +" AND ( EV2.COD_TIPO_PROVVEDIMENTO = '02' OR EV2.COD_TIPO_PROVVEDIMENTO = '03' ) "
                        +" AND TO_CHAR (DA.DATA_EMISSIONE,'YYYYMMDD') > '" + DateUtils.getDateToString(aModel.getDataFinePendenza(),lDataPattern) + "')) "
              +" ) "
            +" ) "
          +" ) ";

    }
    
    // cancelleria assegnataria
    if( aModel.getCodCancelleria() != null && !aModel.getCodCancelleria().equals("-")) { 
        lStatement += " AND ID_FASCICOLO_SIUS IN ( SELECT FAS_SIUS_ID_FASCICOLO_SIUS "
                            +" FROM CANC_ASS_FASC_SIUS "
                           +" WHERE COD_CANCELLERIA_ASSEGNATARIA = '"+ aModel.getCodCancelleria() + "' "
                             +" AND COD_UFFICIO = '"+ aModel.getCodUfficio() + "' "
                             +" AND DATA_FINE IS NULL ) ";
    }               
    
    lStatement += " ORDER BY CHIAVE_ANNO, CHIAVE_PROGR, COGNOME, NOME ";
            
    setStatement(lStatement);
  }
  
  
  /**
   * Metodo modificato x errore Oracle ORA-01799 sulla OUTER JOIN x Roma e Napoli
   * @param aModel
   */
   public void ricercaProcedimentiPosizioneGiuridica(RicercaProcedimentoModel aModel) {
     String lStatement = "";
     String lDataPattern = "yyyyMMdd";
     
     
     lStatement =  " SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, "
               +" FASC.CHIAVE_ANNO CHIAVE_ANNO, "
               +" SOGG.ID_SOGGETTO ID_SOGGETTO, "
               +" FASC.CHIAVE_PROGR CHIAVE_PROGR, "
               +" FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, "
               +" UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , "
               +" FASC.DATA_INSERIMENTO DATA_INSERIMENTO, "
               +" FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, "
               //+" FASC.DATA_DEFINIZIONE DATA_DEFINIZIONE, "
               // La data di definizione se è null, ossia c,è un provvedimenti depositato
               // si ritorna laa data di emissione del documento allegato ( data deposito )
               +" NVL(FASC.DATA_DEFINIZIONE,DA.DATA_EMISSIONE) DATA_DEFINIZIONE, "
               +" SOGG.ID_SOGGETTO ID_SOGGETTO, "
               +" SOGG.COGNOME COGNOME, "
               +" SOGG.NOME NOME, "
               +" SOGG.DATA_NASCITA, "
               +" DESCR_OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_PROCEDIMENTO, "
               +" DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, "
               +" DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, "
               +" DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA, "
               +" GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, "
               +" GP.DATA_RICHIESTA DATA_RICHIESTA, "
               +" NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, "
               +" NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, "
               +" GP.DATA_ARRIVO_CANCELLERIA, "
               +" GP.DATA_CAMERA_CONSIGLIO DATA_UDIENZA, "
               +" evento_app.DATA_EMISSIONE, " 
               +" DA.DATA_EMISSIONE DATA_DEPOSITO ";

    lStatement +=  " FROM FASCICOLO_SIUS FASC "  ;     
    //
    lStatement += " LEFT OUTER JOIN (SELECT ev.id_evento, ev.fas_siu_id_fascicolo_sius, ev.data_emissione " +
                                     " FROM evento ev, FASCICOLO_SIUS FASC " +
                                    " WHERE ev.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " +
                                      " AND FASC.CHIAVE_UFFICIO = '" +aModel.getCodUfficio() + "' " +
                                      " AND EV.ID_EVENTO = (SELECT MAX(ID_EVENTO) " +
                                                            " FROM EVENTO " +
                                                           " WHERE FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS " +
                                                             " AND COD_TIPO_PROVVEDIMENTO IN ('02', '03' ) " +
                                                             " AND EV.COD_ESITO NOT IN ('0601','0602','0603','0604','0605')" +
                                                          " ) " +
                                  " ) evento_app  " +
                                " ON evento_app.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius ";
    //
    lStatement += " LEFT OUTER JOIN DOCUMENTO_ALLEGATO DA ON DA.EVE_ID_EVENTO = evento_app.ID_EVENTO "
                                                      +" AND DA.COD_TIPO_DOCUMENTO IN ('02','03' ) " ;
    //           
    lStatement += " , SOGGETTO SOGG  " +
                  " , GENERALE_PROCEDIMENTO GP "+
                  " , CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO "+
                  " , UFFICIO UFF "+
                  " , COMUNE DESCR_COM_UFF "+
                  " , COMUNE DESCR_COM_NASCITA ";
    // WHERE CONDITIONS
    lStatement += " WHERE FASC.SOG_ID_SOGGETTO                       = SOGG.ID_SOGGETTO "
                   +" AND FASC.ID_FASCICOLO_SIUS                     = GP.FAS_SIU_ID_FASCICOLO_SIUS "
                   +" AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN       = 'OGGETTO_PROCEDIMENTO' "
                   +" AND GP.COD_OGGETTO_PROCEDIMENTO                = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE "
                   +" AND UFF.COD_UFFICIO                            = FASC.CHIAVE_UFFICIO "
                   +" AND UFF.COD_COMUNE                             = DESCR_COM_UFF.COD_COMUNE "
                   +" AND SOGG.COD_COMUNE_NASCITA                    = DESCR_COM_NASCITA.COD_COMUNE "
                   +" AND UFF.COD_UFFICIO                            = NVL('" +  aModel.getCodUfficio() + "', UFF.COD_UFFICIO ) ";

    // Posizione Giuridica
    if (aModel.getCodPosizioneGiuridica() != null && aModel.getCodPosizioneGiuridica().compareTo("-") != 0) 
        lStatement += "AND GP.COD_POSIZIONE_GIURIDICA = '" + aModel.getCodPosizioneGiuridica() + "' ";                    

    // TIPO ATTO
    if (aModel.getCodOggettoProcedimento() != null && aModel.getCodOggettoProcedimento().compareTo("-") != 0) 
       lStatement += "AND GP.COD_OGGETTO_PROCEDIMENTO = '" + aModel.getCodOggettoProcedimento() + "' ";
     
    
    // Magistrato
    if (   aModel.getCodMagistrato() != null 
        && !aModel.getCodMagistrato().equals("-") 
        && !aModel.getCodMagistrato().equalsIgnoreCase("Tutti") 
       )  
    {
      lStatement += " AND (GP.COD_AUTORITA_DELEGATA = NVL('"  + aModel.getCodMagistrato() +  "', GP.COD_AUTORITA_DELEGATA ) ) ";
    }
    
    // Date di iscrizione DAL - AL
    if ( aModel.getDataIscrizioneInizio() != null )
       lStatement += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') >= '" + DateUtils.getDateToString(aModel.getDataIscrizioneInizio(), lDataPattern)+"' " ;
     
    if ( aModel.getDataIscrizioneFine() != null ) 
       lStatement += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= '" + DateUtils.getDateToString(aModel.getDataIscrizioneFine(), lDataPattern)+ "' ";
    

    //     
    if ( aModel.getDataFinePendenza() != null ) {
      lStatement +=" "
       + " AND (   (     COD_STATO_FASCICOLO NOT IN ('01', '05') " +
                   " AND TO_CHAR (FASC.DATA_INSERIMENTO, 'YYYYMMDD') <= '" + DateUtils.getDateToString(aModel.getDataFinePendenza(),lDataPattern) + "' ) " +
               " OR (    COD_STATO_FASCICOLO IN ('01', '05') " +
                   " AND TO_CHAR (FASC.DATA_DEFINIZIONE, 'YYYYMMDD') > '" + DateUtils.getDateToString(aModel.getDataFinePendenza(),lDataPattern) + "' ) " +
             " ) " 
       + " AND (   ( COD_STATO_FASCICOLO NOT IN ('07')) " +
              " OR (    COD_STATO_FASCICOLO = '07' " +
                  " AND ( FASC.ID_FASCICOLO_SIUS = (SELECT FAS_SIU_ID_FASCICOLO_SIUS " +
                                                    " FROM EVENTO EV LEFT OUTER JOIN DOCUMENTO_ALLEGATO DA " +
                                                                                " ON DA.EVE_ID_EVENTO = EV.ID_EVENTO  " +
                                                                               " AND DA.COD_TIPO_DOCUMENTO IN ('02','03') " +
                                                   " WHERE EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS " +
                                                     " AND EV.COD_TIPO_PROVVEDIMENTO in ('02','03') " +
                                                     " AND EV.DATA_INSERIMENTO = ( SELECT MAX (DATA_INSERIMENTO) " +
                                                                                  " FROM EVENTO EV2 " +
                                                                                 " WHERE EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS " +
                                                                                   " AND EV2.COD_TIPO_PROVVEDIMENTO in ('02','03') " +
                                                                                   " AND TO_CHAR (DA.DATA_EMISSIONE,'YYYYMMDD') > '" + DateUtils.getDateToString(aModel.getDataFinePendenza(),lDataPattern) + "'" +
                                                                                " ) " +
                                                   " ) " +
                      " ) "
                +" ) "
           +" ) ";

    }
 
    // cancelleria assegnataria
    if( aModel.getCodCancelleria() != null && !aModel.getCodCancelleria().equals("-")) { 
       lStatement += " AND ID_FASCICOLO_SIUS IN ( SELECT FAS_SIUS_ID_FASCICOLO_SIUS "
                                                 +" FROM CANC_ASS_FASC_SIUS "
                                                +" WHERE COD_CANCELLERIA_ASSEGNATARIA = '"+ aModel.getCodCancelleria() + "' "
                                                  +" AND COD_UFFICIO = '"+ aModel.getCodUfficio() + "' "
                                                  +" AND DATA_FINE IS NULL ) ";
    }               
 
    lStatement += " ORDER BY CHIAVE_ANNO, CHIAVE_PROGR, COGNOME, NOME ";
         
    setStatement(lStatement);
  }
  
  public GenericModel getModel() throws DAOException {
      EveFasGepSogCancModel lModel = new EveFasGepSogCancModel();
      
      // popola fascicolo sius.
      lModel.setFascicoloSius(new FascicoloSiusModel());
      lModel.getFascicoloSius().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
      lModel.getFascicoloSius().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
        lModel.getFascicoloSius().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
        lModel.getFascicoloSius().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
        lModel.getFascicoloSius().setDataDefinizione(getDate("DATA_DEFINIZIONE"));
        // popola fascicolo sius -> Soggetto.
        lModel.getFascicoloSius().setSoggetto(new SoggettoModel());
        lModel.getFascicoloSius().getSoggetto().setCognome(getString("COGNOME"));
        lModel.getFascicoloSius().getSoggetto().setNome(getString("NOME"));
        // popola generale procedimento.
        lModel.setGeneraleProcedimento(new GeneraleProcedimentoModel());
        lModel.getGeneraleProcedimento().setDataCameraConsiglio(getDate("DATA_UDIENZA"));
        lModel.getGeneraleProcedimento().setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
        lModel.getGeneraleProcedimento().setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA"));
        // popola evento.        
        lModel.setEvento(new EventoModel());
        lModel.getEvento().setDataEmissione(getDate("DATA_EMISSIONE"));
        // popola documento allegato.
        lModel.setDocumentoAllegato(new DocumentoAllegatoModel());
        lModel.getDocumentoAllegato().setDataEmissione(getDate("DATA_DEPOSITO"));
      
      return lModel;
  }    
}