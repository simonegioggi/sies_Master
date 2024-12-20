package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
//import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>Title: ProcUdiFissateNoDefSqlDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ProcUdiFissateNoDefSqlDAO extends SIAPSqlDAO {
    public ProcUdiFissateNoDefSqlDAO (Connection conn) {
        super(conn);
    }

    protected String getSqlQuery() {
        String lStatement = new String("");
        /*
        lStatement = 
                " SELECT "
                + " FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, "
                + " FASC.CHIAVE_ANNO CHIAVE_ANNO, FASC.DATA_INSERIMENTO DATA_INSERIMENTO, "
                + " FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, FASC.CHIAVE_PROGR CHIAVE_PROGR, "
                + " GP.DATA_CAMERA_CONSIGLIO, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, "
                + " UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, "
                + " SOGG.ID_SOGGETTO ID_SOGGETTO, SOGG.COGNOME COGNOME, SOGG.NOME NOME, "
                + " SOGG.DATA_NASCITA, SOGG.COD_PROVINCIA_NASCITA, "
                + " OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_PROCEDIMENTO, POS_GIURIDICA.RV_MEANING DESCR_POS_GIURIDICA, "
                + " COMUNE_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, "
                + " COMUNE_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, "
                + " GP.COD_POSIZIONE_GIURIDICA, GP.COD_OGGETTO_PROCEDIMENTO, "
                + " GP.DATA_RICHIESTA, "
                + " NVL (GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, "
                + " NVL (GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, GP.DATA_FINE_PENA "
                + " FROM "
                + 	" FASCICOLO_SIUS FASC " 
                + 		" LEFT OUTER JOIN EVENTO EV ON "  
       		 	+ 			" EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS "
       		 	+ 				" AND EV.ID_EVENTO = ( SELECT MAX(ID_EVENTO) "
                + 									  "  FROM EVENTO " 
                +                                      " WHERE FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS " 
                +                                        " AND COD_TIPO_PROVVEDIMENTO IN ('02','03') " 
                +		                                 " AND EV.COD_ESITO NOT IN ('0601', '0602','0603','0604','0605') ) "
       		 	+		" LEFT OUTER JOIN CG_REF_CODES TP ON " 
       		 	+ 			" TP.RV_LOW_VALUE = EV.COD_TIPO_PROVVEDIMENTO AND TP.RV_DOMAIN='TIPO_PROVVEDIMENTO'"
       		 	+		" LEFT OUTER JOIN CG_REF_CODES CM ON " 
       		 	+			" CM.RV_LOW_VALUE = EV.COD_MOTIVO AND CM.RV_DOMAIN='MOTIVO_PROVVEDIMENTO'"
       		 	+		" LEFT OUTER JOIN CG_REF_CODES CE ON " 
        		+			" CE.RV_LOW_VALUE = EV.COD_ESITO AND CE.RV_DOMAIN='ESITO_PROVVEDIMENTO',"
                + " SOGGETTO SOGG, "
                + " GENERALE_PROCEDIMENTO GP, "
                + " CG_REF_CODES OGGETTO_PROCEDIMENTO, "
                + " CG_REF_CODES POS_GIURIDICA, "
                + " UFFICIO UFF, "
                + " COMUNE COMUNE_UFF, "
                + " COMUNE COMUNE_NASCITA "
                + " WHERE "
                + " FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO "
                + " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS "
                + " AND OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' "
                + " AND GP.COD_OGGETTO_PROCEDIMENTO = OGGETTO_PROCEDIMENTO.RV_LOW_VALUE "
                + " AND POS_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA' "
                + " AND (NVL (GP.COD_POSIZIONE_GIURIDICA, '-') = POS_GIURIDICA.RV_LOW_VALUE) "
                + " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO "
                + " AND UFF.COD_COMUNE = COMUNE_UFF.COD_COMUNE "
                + " AND SOGG.COD_COMUNE_NASCITA = COMUNE_NASCITA.COD_COMUNE "
                + " AND FASC.COD_STATO_FASCICOLO IN ('02','10') "
                + " AND GP.UDI_ID_UDIENZA IS NOT NULL ";
        */        
        
        lStatement = " SELECT "
        	+ " FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, "
        	+ " FASC.CHIAVE_ANNO CHIAVE_ANNO, "
        	   + " FASC.DATA_INSERIMENTO DATA_INSERIMENTO, "
        	   + " FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, "
        	   + " FASC.CHIAVE_PROGR CHIAVE_PROGR, "
        	   + " GP.DATA_CAMERA_CONSIGLIO, "
        	   + " FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, "
        	   + " UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, "
        	   + " SOGG.ID_SOGGETTO ID_SOGGETTO, "
        	   + " SOGG.COGNOME COGNOME, "
        	   + " SOGG.NOME NOME, "
        	   + " SOGG.DATA_NASCITA, "
        	   + " SOGG.COD_PROVINCIA_NASCITA, "
        	   + " OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_PROCEDIMENTO, "
        	   + " POS_GIURIDICA.RV_MEANING DESCR_POS_GIURIDICA, "
        	   + " COMUNE_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, "
        	   + " COMUNE_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, "
        	   + " GP.COD_POSIZIONE_GIURIDICA, "
        	   + " GP.COD_OGGETTO_PROCEDIMENTO, "
        	   + " GP.DATA_RICHIESTA, "
        	   + " NVL (GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, "
        	   + " NVL (GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, "
        	   + " GP.DATA_FINE_PENA, "
        	   + " EV.DATA_EMISSIONE, "
        	   + " EV.ID_EVENTO, "
        	   + " EV.COD_TIPO_PROVVEDIMENTO, "
        	   + " TP.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, "
        	   + " CE.RV_MEANING DESCR_ESITO_PROVVEDIMENTO, "
        	   + " CM.RV_MEANING DESCR_MOTIVO_PROVVEDIMENTO "
          + " FROM FASCICOLO_SIUS FASC "
          		+ " LEFT OUTER JOIN EVENTO EV "
          				+ " ON EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS "
          					+ " AND EV.ID_EVENTO              = 	( SELECT MAX(ID_EVENTO) "
          													+ "  FROM EVENTO " 
          													+ "  WHERE FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS "
          													+ "  AND COD_TIPO_PROVVEDIMENTO     IN ('02', '03') "
          													+ "  AND EV.COD_ESITO NOT           IN ('0601', '0602','0603','0604','0605') "
          													+ " ) "
          		+ " LEFT OUTER JOIN CG_REF_CODES TP "
          				+ " ON TP.RV_LOW_VALUE = EV.COD_TIPO_PROVVEDIMENTO "
          					+ " AND TP.RV_DOMAIN   ='TIPO_PROVVEDIMENTO' "
          		+ " LEFT OUTER JOIN CG_REF_CODES CM "
          				+ " ON CM.RV_LOW_VALUE = EV.COD_MOTIVO "
          					+ " AND CM.RV_DOMAIN   ='MOTIVO_PROVVEDIMENTO' "
          		+ " LEFT OUTER JOIN CG_REF_CODES CE "
          				+ " ON CE.RV_LOW_VALUE = EV.COD_ESITO "
          					+ " AND CE.RV_DOMAIN   ='ESITO_PROVVEDIMENTO', "
          
          		+ " SOGGETTO SOGG, "
          		+ " GENERALE_PROCEDIMENTO GP, "
          		+ " CG_REF_CODES OGGETTO_PROCEDIMENTO, "
          		+ " CG_REF_CODES POS_GIURIDICA, "
          		+ " UFFICIO UFF, "
          		+ " COMUNE COMUNE_UFF, "
          		+ " COMUNE COMUNE_NASCITA "
          + " WHERE FASC.SOG_ID_SOGGETTO                 = SOGG.ID_SOGGETTO "
          	+ " AND FASC.ID_FASCICOLO_SIUS                 = GP.FAS_SIU_ID_FASCICOLO_SIUS "
          	+ " AND OGGETTO_PROCEDIMENTO.RV_DOMAIN         = 'OGGETTO_PROCEDIMENTO' " 
		    + " AND GP.COD_OGGETTO_PROCEDIMENTO            = OGGETTO_PROCEDIMENTO.RV_LOW_VALUE "
		    + " AND POS_GIURIDICA.RV_DOMAIN                = 'POSIZIONE_GIURIDICA' "
		    + " AND (NVL (GP.COD_POSIZIONE_GIURIDICA, '-') = POS_GIURIDICA.RV_LOW_VALUE) "
		    + " AND UFF.COD_UFFICIO                        = FASC.CHIAVE_UFfICIO "
		    + " AND UFF.COD_COMUNE                         = COMUNE_UFF.COD_COMUNE "
		    + " AND SOGG.COD_COMUNE_NASCITA                = COMUNE_NASCITA.COD_COMUNE "
		    + " AND FASC.COD_STATO_FASCICOLO              IN ('02','10') "
		    + " AND GP.UDI_ID_UDIENZA                     IS NOT NULL ";
		    //+ " AND fasc.chiave_ufficio                    = '00127201301' ";
          
                
        return lStatement;
    }


    
    
    
    
    //
    // METODO GETMODEL()
    //
    public GenericModel getModel() throws DAOException {
        EveFasGepSogProvModel aModel = new EveFasGepSogProvModel();
        
        aModel.setFascicoloSius(new FascicoloSiusModel());
        aModel.getFascicoloSius().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
        aModel.getFascicoloSius().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
        aModel.getFascicoloSius().setDataInserimento(getDate("DATA_INSERIMENTO"));
        aModel.getFascicoloSius().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
        aModel.getFascicoloSius().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
        aModel.getFascicoloSius().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
        aModel.getFascicoloSius().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
        
        aModel.getFascicoloSius().setSoggetto(new SoggettoModel());
        aModel.getFascicoloSius().getSoggetto().setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
        aModel.getFascicoloSius().getSoggetto().setCognome(getString("COGNOME"));
        aModel.getFascicoloSius().getSoggetto().setNome(getString("NOME"));
        aModel.getFascicoloSius().getSoggetto().setDataNascita(getDate("DATA_NASCITA"));
        aModel.getFascicoloSius().getSoggetto().setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
        aModel.getFascicoloSius().getSoggetto().setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
        
        //aModel.setPosizioneGiuridica(new PosizioneGiuridicaModel());
        //aModel.getPosizioneGiuridica().setDescrPosizioneGiuridica(getString("DESCR_POS_GIURIDICA"));
        
        aModel.setGeneraleProcedimento(new GeneraleProcedimentoModel());
        aModel.getGeneraleProcedimento().setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
        aModel.getGeneraleProcedimento().setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));
        aModel.getGeneraleProcedimento().setCodPosGiuridica(getString("COD_POSIZIONE_GIURIDICA"));
        aModel.getGeneraleProcedimento().setDescrPosGiuridica(getString("DESCR_POS_GIURIDICA"));
        aModel.getGeneraleProcedimento().setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
        aModel.getGeneraleProcedimento().setDataRichiesta(getDate("DATA_RICHIESTA"));
        aModel.getGeneraleProcedimento().setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA"));
        aModel.getGeneraleProcedimento().setDescrMittente(getString("DESCR_MITTENTE"));
        aModel.getGeneraleProcedimento().setDataFinePena(getDate("DATA_FINE_PENA"));
        
        aModel.setEvento(new EventoModel());
        aModel.getEvento().setIdEvento(getBigDecimal("ID_EVENTO"));
        aModel.getEvento().setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
        aModel.getEvento().setDescrMotivo(getString("DESCR_MOTIVO_PROVVEDIMENTO"));
        aModel.getEvento().setDescrEsito(getString("DESCR_ESITO_PROVVEDIMENTO"));
        aModel.getEvento().setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
        aModel.getEvento().setDataEmissione(getDate("DATA_EMISSIONE"));
        

        return aModel;
    }

    protected String getCondizione(RicercaProcedimentoModel aModel) {
        String lCondizione = new String();
        
        lCondizione =  "AND fasc.chiave_ufficio = '" + 
                       aModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + 
                       "' ";
        if (aModel.getCodOggettoProcedimento() != null && aModel.getCodOggettoProcedimento().compareTo("-") != 0) {
            lCondizione += " AND gp.cod_oggetto_procedimento = '" + 
                           aModel.getCodOggettoProcedimento() + 
                           "' ";
        }
        if (aModel.getCodPosizioneGiuridica() != null && aModel.getCodPosizioneGiuridica().compareTo("-") != 0) {
            lCondizione += " AND gp.cod_posizione_giuridica = '" + 
                           aModel.getCodPosizioneGiuridica() + 
                           "' ";
        }
        if (aModel.getDataCameraConsiglioInizio() != null) {
            lCondizione += "AND TO_CHAR (gp.data_camera_consiglio, 'yyyyMMdd') >= '" + 
                           DateUtils.getDateToString(aModel.getDataCameraConsiglioInizio(), "yyyyMMdd") + 
                           "' ";
        }
        if (aModel.getDataCameraConsiglioFine() != null) {
            lCondizione += "AND TO_CHAR (gp.data_camera_consiglio, 'yyyyMMdd') <= '" + 
                           DateUtils.getDateToString(aModel.getDataCameraConsiglioFine(), "yyyyMMdd") + 
                           "' ";
        }
        if (aModel.getDataIscrizioneInizio() != null) {
            lCondizione += "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') >= '" +
                           DateUtils.getDateToString(aModel.getDataIscrizioneInizio(), "yyyyMMdd") +
                           "' ";
        }
        if (aModel.getDataIscrizioneFine() != null) {
            lCondizione += "AND TO_CHAR (fasc.data_iscrizione, 'yyyyMMdd') <= '" +
                           DateUtils.getDateToString(aModel.getDataIscrizioneFine(), "yyyyMMdd") +
                           "' ";
        }
        
        return lCondizione;
    }
    
    public void ricercaProcedimentiPerDataUdienza(RicercaProcedimentoModel aModel){
    	String lStatement = "";
    	
    	lStatement = this.getSqlQuery();
    	lStatement += this.getCondizione(aModel);
    	lStatement += "ORDER BY fasc.data_iscrizione DESC "; 

    	setStatement(lStatement);
    }
}