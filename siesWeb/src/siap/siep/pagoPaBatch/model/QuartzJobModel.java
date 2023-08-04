package siap.siep.pagoPaBatch.model;

import java.util.StringTokenizer;


/**
 * Model che contiene le properties di un job Quartz
 * @author d.fiorletta
 *
 */
public class QuartzJobModel {
    private String nomeDemone;
    private String gruppoDemone;
    private String descrizione; 
    private String cronExpression;
    private String lastExec;
    private String nextSched;
    private String status;
    private String esito;
    
    private String[] decodeGionoSett = {"Domenica","Lunedi'","Martedi'","Mercoledi'","Giovedi'","Venerdi'","Sabato"};
    
    public QuartzJobModel() {};
    
    // GETTER
    public String getNomeDemone()     { return nomeDemone; }
    public String getGruppoDemone()   { return gruppoDemone; }
    public String getDescrizione()    { return descrizione; }
    public String getCronExpression() { return cronExpression; }
    public String getNextSched()      { return nextSched; }
    public String getLastExec()       { return lastExec; }
    public String getStatus()         { return status; }
    public String getEsito()          { return esito; }
    
    // SETTER
    public void setNomeDemone     (String nomeDemone)     { this.nomeDemone = nomeDemone; }
    public void setGruppoDemone   (String gruppoDemone)   { this.gruppoDemone = gruppoDemone; }
    public void setDescrizione    (String descrizione)    { this.descrizione = descrizione; }
    public void setCronExpression (String cronExpression) { this.cronExpression = cronExpression; }
    public void setNextSched      (String nextSched)      { this.nextSched = nextSched; }
    public void setLastExec       (String lastExec)       { this.lastExec = lastExec; }
    public void setStatus         (String status)         { this.status = status; }
    public void setEsito          (String esito)          { this.esito = esito; }
    
    public String getSecondi () {
        String str = "";
        if (cronExpression!=null) {
           //StringTokenizer st = new StringTokenizer(cronExpression, " ");           
           String[] token = cronExpression.split(" ");
           str = token[0];
        }
        return str;
    }
    
    public String getMinuti () {
        String str = "";
        if (cronExpression!=null) {
           String[] token = cronExpression.split(" ");
           str = token[1];
        }
        return str;
    }
    
    public String getOre () {
        String str = "";
        if (cronExpression!=null) {
           String[] token = cronExpression.split(" ");
           str = token[2];
        }
        return str;
    }
    
    public String getGiornoDelMese () {
        String str = "";
        if (cronExpression!=null) {
           String[] token = cronExpression.split(" ");
           str = token[3];
        }
        return str;
    }

    public String getMese () {
        String str = "";
        if (cronExpression!=null) {
           String[] token = cronExpression.split(" ");
           str = token[5];
        }
        return str;
    }
    
    public String getGiornoSettimana () {
        String str = "";
        if (cronExpression!=null) {
           String[] token = cronExpression.split(" ");
           str = token[5];
        }
        return str;
    }
    
    /**
     * 
     * @return
     */
    public String getDecodificaExpr() {
        String str = "";
        if (cronExpression!=null) {
            String[] token = cronExpression.split(" ");
            if (!token[5].equals("?")) { // Giorno della settimana
                str+="Ogni "+decodeGionoSett[Integer.parseInt(token[5])-1]+" del mese";  
            }
            else if (token[3].equals("*")) {
                str+="Ogni giorno del mese"; 
            }
            else if (token[3].contains("/")  ) {
                String[] token2 = token[3].split("/");
                str+="Ogni "+token2[0]+" giorni a partire dal "+token2[1]+" del mese"; 
            }
            else if (token[3].contains(",")  ) {
                str+="I giorni "+token[3]+" del mese"; 
            }
        }
        
        return str;
    }
}
