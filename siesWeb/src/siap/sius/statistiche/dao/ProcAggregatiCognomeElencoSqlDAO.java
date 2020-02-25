package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.sius.statistiche.model.ProcAggregatiCognomeElencoModel;
import siap.sius.statistiche.model.RicercaAggregatiCognomeModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class ProcAggregatiCognomeElencoSqlDAO extends SqlDAO {

    public ProcAggregatiCognomeElencoSqlDAO(Connection aCon) {
        super(aCon);
    }

    private String getCondizione(RicercaAggregatiCognomeModel aModel) {
        String lCondizione = "";
        String lDataPattern = "ddMMyyyy";
        String lDataORAPattern = "DDMMYYYY HH24:MI:SS";
        
        lCondizione = "AND D.CHIAVE_UFFICIO = '" + aModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + "' ";
		if ( aModel.getDataInizio() != null && aModel.getDataFine() != null  ) {
			lCondizione += " AND (D.DATA_ISCRIZIONE BETWEEN TO_DATE('" + 
					DateUtils.getDateToString(aModel.getDataInizio(), lDataPattern) + " 00:00:00','" + lDataORAPattern + "') " ; 			
			lCondizione += " AND TO_DATE('" + 
					DateUtils.getDateToString(aModel.getDataFine(), lDataPattern) + " 23:59:59','" + lDataORAPattern + "') )";
		}
		
         return lCondizione;
    }
    
    public void ricercaElenco(RicercaAggregatiCognomeModel aModel) {
        String lStatement = "";
        lStatement = 
                "SELECT "
                + "SUBSTR(E.COGNOME,1,1) L1, SUBSTR(E.COGNOME,1,2) L2, SUBSTR(E.COGNOME,1,3) L3, "
                + "E.COGNOME, E.NOME, E.DATA_NASCITA, "
                + "F.DESCRIZIONE LUOGO_NASCITA, "
                + "D.CHIAVE_ANNO, D.CHIAVE_PROGR, "
                + "C.RV_MEANING POSIZIONE_GIURIDICA, "
                + "C1.RV_MEANING CONTENUTO, "
                + "C2.RV_MEANING STATO_PROCEDIMENTO "
                + "FROM "
                + "GENERALE_PROCEDIMENTO A, "
                + "CG_REF_CODES C, "
                + "FASCICOLO_SIUS D, "
                + "SOGGETTO E, CG_REF_CODES C1, "
                + "CG_REF_CODES C2, "
                + "COMUNE F "
                + "WHERE "
                + "A.FAS_SIU_ID_FASCICOLO_SIUS = D.ID_FASCICOLO_SIUS "
                + "AND (A.COD_POSIZIONE_GIURIDICA=C.RV_LOW_VALUE AND C.RV_DOMAIN='POSIZIONE_GIURIDICA') "
                + "AND (E.ID_SOGGETTO=D.SOG_ID_SOGGETTO) "
                + "AND (F.COD_COMUNE=E.COD_COMUNE_NASCITA) "
                + "AND (A.COD_OGGETTO_PROCEDIMENTO=C1.RV_LOW_VALUE AND C1.RV_DOMAIN='OGGETTO_PROCEDIMENTO') "
                + "AND (D.COD_STATO_FASCICOLO=C2.RV_LOW_VALUE AND C2.RV_DOMAIN='STATO_FASCICOLO') "
                + this.getCondizione(aModel)
                + "ORDER BY "
                + "E.COGNOME, E.NOME, E.DATA_NASCITA, F.DESCRIZIONE, D.CHIAVE_ANNO, D.CHIAVE_PROGR ASC ";
        
        setStatement(lStatement);
    }

    public GenericModel getModel() throws DAOException {
        ProcAggregatiCognomeElencoModel lModel = new ProcAggregatiCognomeElencoModel();
        
        lModel.setIniziale1Lettera(getString("L1"));
        lModel.setIniziale2Lettere(getString("L2"));
        lModel.setIniziale3Lettere(getString("L3"));
        lModel.setCognome(getString("COGNOME"));
        lModel.setNome(getString("NOME"));
        lModel.setDataNascita(getDate("DATA_NASCITA"));
        lModel.setLuogoNascita(getString("LUOGO_NASCITA"));
        lModel.setChiaveAnno(getString("CHIAVE_ANNO"));
        lModel.setChiaveProgressivo(getString("CHIAVE_PROGR"));
        lModel.setPosizioneGiuridica(getString("POSIZIONE_GIURIDICA"));
        lModel.setContenuto(getString("CONTENUTO"));
        lModel.setStatoProcedimento(getString("STATO_PROCEDIMENTO"));

        return lModel;
    }
}
