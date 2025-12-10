package siap.siep.modulocumulo.util;

import java.math.BigDecimal;

import f3b.model.GenericModel;
import siap.sico.calendar.model.CalendarModel;

public class CalendarStampaModel extends GenericModel {
    public static final String TIPO_TOT_PENA_PRINC = "001"; // Totali pene principali
    public static final String TIPO_PERIODI_CARC_SOFFERTI = "002";
    public static final String TIPO_PERIODI_ESP_REV_MA = "003";
    public static final String TIPO_PERIODI_SOSP_DIFF  = "004";
    public static final String TIPO_BENEFICI = "005";    
    public static final String TIPO_SOMME_PAGATE = "006";
    public static final String TIPO_RIDE_PENA_ALTRO = "007";
    public static final String TIPO_LA_PERMESSI = "008";
    public static final String TIPO_RICH_GE_CONC = "009";
    public static final String TIPO_RICH_GE_REV = "010";
    public static final String TIPO_TOTALI = "011";
    
    private String mTipoConteggio;
    private String mDescConteggio;

    public CalendarStampaModel () {
        //
    }
    
    public String getTipoConteggio() {
        return mTipoConteggio;
    }

    public void setTipoConteggio(String mTipoConteggio) {
        this.mTipoConteggio = mTipoConteggio;
    }

    public String getDescConteggio() {
        return mDescConteggio;
    }

    public void setDescConteggio(String mDescConteggio) {
        this.mDescConteggio = mDescConteggio;
    }

    
    private BigDecimal mNumGiorniLA;
    private BigDecimal mNumGiorniLS;
    private BigDecimal mNumGiorniLI;
    private BigDecimal mNumRimedi;
    private BigDecimal mNumScomputi;
    
    private BigDecimal mNumAnniReclusione;
    private BigDecimal mNumMesiReclusione;
    private BigDecimal mNumGiorniReclusione;
    private BigDecimal mImportoMulta;
    private BigDecimal mNumAnniArresto;
    private BigDecimal mNumMesiArresto;
    private BigDecimal mNumGiorniArresto;
    private BigDecimal mImportoAmmenda;
    
    private BigDecimal mNumAnni;
    private BigDecimal mNumMesi;
    private BigDecimal mNumGiorni;
    
    private String mSegnoQuantumReclusione;
    private String mSegnoQuantumArresto;    
    
    // =========================================
    //  Metodi GET
    // =========================================
    public BigDecimal getNumGiorniLA() { return mNumGiorniLA; }
    public BigDecimal getNumGiorniLS() { return mNumGiorniLS; }
    public BigDecimal getNumGiorniLI() { return mNumGiorniLI; }
    public BigDecimal getNumRimedi()   { return mNumRimedi;   }
    public BigDecimal getNumScomputi() { return mNumScomputi; }    
    
    public BigDecimal getNumAnniReclusione()  { return mNumAnniReclusione;   }
    public BigDecimal getNumMesiReclusione()  { return mNumMesiReclusione;   }
    public BigDecimal getNumGiorniReclusione(){ return mNumGiorniReclusione; }
    public BigDecimal getImportoMulta()       { return mImportoMulta;        }
    
    public BigDecimal getNumAnniArresto()   { return mNumAnniArresto;   }
    public BigDecimal getNumMesiArresto()   { return mNumMesiArresto;   }
    public BigDecimal getNumGiorniArresto() { return mNumGiorniArresto; }
    public BigDecimal getImportoAmmenda()   { return mImportoAmmenda;   }
    
    public BigDecimal getNumAnni()   { return mNumAnni;   }
    public BigDecimal getNumMesi()   { return mNumMesi;   }
    public BigDecimal getNumGiorni() { return mNumGiorni; }
    
    public String getSegnoQuantumReclusione() { return mSegnoQuantumReclusione; }
    public String getSegnoQuantumArresto()    { return mSegnoQuantumArresto; }
    
    
    // =========================================
    //  Metodi SET
    // =========================================
    public void setNumGiorniLA (BigDecimal mNumGiorniLA) { this.mNumGiorniLA = mNumGiorniLA; }
    public void setNumGiorniLS (BigDecimal mNumGiorniLS) { this.mNumGiorniLS = mNumGiorniLS; }
    public void setNumGiorniLI (BigDecimal mNumGiorniLI) { this.mNumGiorniLI = mNumGiorniLI; }
    public void setNumRimedi   (BigDecimal mNumRimedi)   { this.mNumRimedi   = mNumRimedi;   }
    public void setNumScomputi (BigDecimal mNumScomputi) { this.mNumScomputi = mNumScomputi; }

    public void setNumAnniReclusione   (BigDecimal mNumAnniReclusione)   { this.mNumAnniReclusione   = mNumAnniReclusione;   }
    public void setNumMesiReclusione   (BigDecimal mNumMesiReclusione)   { this.mNumMesiReclusione   = mNumMesiReclusione;   }
    public void setNumGiorniReclusione (BigDecimal mNumGiorniReclusione) { this.mNumGiorniReclusione = mNumGiorniReclusione; }
    public void setImportoMulta        (BigDecimal mImportoMulta)        { this.mImportoMulta        = mImportoMulta;        }
    
    public void setNumAnniArresto   (BigDecimal mNumAnniArresto)   { this.mNumAnniArresto   = mNumAnniArresto;   }
    public void setNumMesiArresto   (BigDecimal mNumMesiArresto)   { this.mNumMesiArresto   = mNumMesiArresto;   }
    public void setNumGiorniArresto (BigDecimal mNumGiorniArresto) { this.mNumGiorniArresto = mNumGiorniArresto; }
    public void setImportoAmmenda   (BigDecimal mImportoAmmenda)   { this.mImportoAmmenda   = mImportoAmmenda;   }
    
    public void setNumAnni   (BigDecimal mNumAnni)   { this.mNumAnni   = mNumAnni;   }
    public void setNumMesi   (BigDecimal mNumMesi)   { this.mNumMesi   = mNumMesi;   }
    public void setNumGiorni (BigDecimal mNumGiorni) { this.mNumGiorni = mNumGiorni; }
    
    public void setSegnoQuantumReclusione (String mSegnoQuantumReclusione) { this.mSegnoQuantumReclusione = mSegnoQuantumReclusione; }
    public void setSegnoQuantumArresto    (String mSegnoQuantumArresto)    { this.mSegnoQuantumArresto = mSegnoQuantumArresto; }
    //StringUtils.toEuroFormat
}
