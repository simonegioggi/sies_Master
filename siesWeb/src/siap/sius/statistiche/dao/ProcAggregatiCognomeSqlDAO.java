package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.sius.statistiche.model.ProcAggregatiCognomeModel;
import siap.sius.statistiche.model.RicercaAggregatiCognomeModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class ProcAggregatiCognomeSqlDAO extends SqlDAO {

    public ProcAggregatiCognomeSqlDAO(Connection aCon) {
        super(aCon);
    }

    private String getCondizione(RicercaAggregatiCognomeModel aModel) {
        String lCondizione = "";
        String lDataPattern = "ddMMyyyy";
        
        lCondizione = "fasc.CHIAVE_UFFICIO = '" + aModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + "' ";
        
        if (aModel.getDataInizio() != null && aModel.getDataFine() != null) {
            lCondizione += 
                    " AND (fasc.data_iscrizione BETWEEN TO_DATE('" + 
                    DateUtils.getDateToString(aModel.getDataInizio(), lDataPattern) + 
                    " 00:00:00','DDMMYYYY HH24:MI:SS') AND TO_DATE('" +
                    DateUtils.getDateToString(aModel.getDataFine(), lDataPattern) +
                    " 23:59:59','DDMMYYYY HH24:MI:SS')) ";
        }

        return lCondizione;
    }
    
    public void ricercaAggregati1Lettera(RicercaAggregatiCognomeModel aModel) {
        String lStatement = "";
        
        lStatement = 
                "SELECT "
                + "SUBSTR(sogg.COGNOME,1,1) INIZIALE, "
                + "COUNT(*) TOTALE "
                + "FROM "
                + "FASCICOLO_SIUS fasc, SOGGETTO sogg "
                + "WHERE "
                + this.getCondizione(aModel)
                + "AND (sogg.ID_SOGGETTO = fasc.SOG_ID_SOGGETTO) "
                + "GROUP BY "
                + "SUBSTR(sogg.COGNOME,1,1) "
                + "ORDER BY "
                + "SUBSTR(sogg.COGNOME,1,1) ASC";
        
        setStatement(lStatement);
    }

    public void ricercaAggregati2Lettere(RicercaAggregatiCognomeModel aModel) {
        String lStatement = "";
        
        lStatement = 
                "SELECT "
                + "SUBSTR(sogg.COGNOME,1,2) INIZIALE, "
                + "COUNT(*) TOTALE "
                + "FROM "
                + "FASCICOLO_SIUS fasc, SOGGETTO sogg "
                + "WHERE "
                + this.getCondizione(aModel)
                + "AND (sogg.ID_SOGGETTO = fasc.SOG_ID_SOGGETTO) "
                + "GROUP BY "
                + "SUBSTR(sogg.COGNOME,1,2) "
                + "ORDER BY "
                + "SUBSTR(sogg.COGNOME,1,2) ASC";
        
        setStatement(lStatement);
    }
    
    public GenericModel getModel() throws DAOException {
        ProcAggregatiCognomeModel lModel = new ProcAggregatiCognomeModel();
        
        lModel.setIniziale(this.getString("INIZIALE"));
        lModel.setTotale(this.getBigDecimal("TOTALE"));
        
        return lModel;
    }
}
