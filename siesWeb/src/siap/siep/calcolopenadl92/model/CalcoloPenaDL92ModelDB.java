package siap.siep.calcolopenadl92.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.model.GenericModel;
import siap.siep.calcolopena.model.CalcoloPenaDL92Model;
import siap.siep.calcolopena.model.SemestreDL92Model;


/**
 * CalcoloPenaDL92ModelDB - Classe Model per salvare sul DB i dati calcolati dalla calcolatrice
 * DL92
 * @since MEV_2026-1
 */
public class CalcoloPenaDL92ModelDB extends GenericModel {
    private static final long serialVersionUID = 1102882424352691224L;

    //private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

    private BigDecimal mIdCalcoloPenaDL92;
    private BigDecimal mFasSieIdFascicoloSiep;
    
    private BigDecimal mNumAnniReclusione;
    private BigDecimal mNumMesiReclusione;
    private BigDecimal mNumGiorniReclusione;
    private BigDecimal mImportoMulta;
    
    private BigDecimal mNumAnniArresto;
    private BigDecimal mNumMesiArresto;
    private BigDecimal mNumGiorniArresto;
    private BigDecimal mImportoAmmenda;

    private BigDecimal mNumAnniPresofferto;
    private BigDecimal mNumMesiPresofferto;
    private BigDecimal mNumGiorniPresofferto;

    private String mPosizioneGiuridica;
    private Date mDataInizioPena;    
    
    private BigDecimal mNumAnniDaEspiare;
    private BigDecimal mNumMesiDaEspiare;
    private BigDecimal mNumGiorniDaEspiare;

    private BigDecimal mSemestriUtili;    
    private BigDecimal mNumGgLaMatInPenaRes;    
    
    private BigDecimal mLaFungibili;
    private BigDecimal mLaNonConcesse;
    
    private BigDecimal mNumAnniPenaIpotetica;
    private BigDecimal mNumMesiPenaIpotetica;
    private BigDecimal mNumGiorniPenaIpotetica;
    
    private BigDecimal mSemestriUtiliPenaScontata;
    private BigDecimal mLaMaturate;
    private BigDecimal mLaApplicate;
    
    private Date mDataScarcNoLa;
    private Date mDataScarcLaFung;
    private Date mDataScarcLaNoFung;
    private Date mDataScarcPenultimoSem; 

    private String mCodOperatoreInserimento;
    private Date   mDataInserimento;
    private String mCodUfficioInserimento;
    private String mCodOperatoreAggiornamento;
    private Date   mDataAggiornamento;
    private String mCodUfficioAggiornamento;
    
    //
    private SemestreDL92Model mSemestrePresofferto = null;
    private Vector<SemestreDL92Model> mListaSemetri = new Vector<>();
    
    
    /**
     * Costruttore di default
     */
    public CalcoloPenaDL92ModelDB() {
        this.mIdCalcoloPenaDL92 = null;
        this.mFasSieIdFascicoloSiep = null;
        
        this.mNumAnniReclusione = null;
        this.mNumMesiReclusione = null;
        this.mNumGiorniReclusione = null;
        this.mImportoMulta = null;
        
        this.mNumAnniArresto = null;
        this.mNumMesiArresto = null;
        this.mNumGiorniArresto = null;
        this.mImportoAmmenda = null;

        this.mNumAnniPresofferto = null;
        this.mNumMesiPresofferto = null;
        this.mNumGiorniPresofferto = null;

        this.mPosizioneGiuridica = null;
        this.mDataInizioPena = null;    
        
        this.mNumAnniDaEspiare = null;
        this.mNumMesiDaEspiare = null;
        this.mNumGiorniDaEspiare = null;

        this.mSemestriUtili = null;    
        this.mNumGgLaMatInPenaRes = null;    
        
        this.mLaFungibili = null;
        this.mLaNonConcesse = null;
        
        this.mNumAnniPenaIpotetica = null;
        this.mNumMesiPenaIpotetica = null;
        this.mNumGiorniPenaIpotetica = null;
        
        this.mSemestriUtiliPenaScontata = null;
        this.mLaMaturate = null;
        this.mLaApplicate = null;
        
        this.mDataScarcNoLa = null;
        this.mDataScarcLaFung = null;
        this.mDataScarcLaNoFung = null;
        this.mDataScarcPenultimoSem = null; 

        this.mCodOperatoreInserimento = null;
        this.mDataInserimento = null;
        this.mCodUfficioInserimento = null;
        this.mCodOperatoreAggiornamento = null;
        this.mDataAggiornamento = null;
        this.mCodUfficioAggiornamento = null;        
    }
    
    /**
     * Costruttore di "copia"
     * 
     * @param aModel
     */
    public CalcoloPenaDL92ModelDB (CalcoloPenaDL92ModelDB aModel) {
        this.mIdCalcoloPenaDL92 = aModel.getIdCalcoloPenaDL92();
        this.mFasSieIdFascicoloSiep = aModel.getFasSieIdFascicoloSiep();
        
        this.mNumAnniReclusione = aModel.getNumAnniReclusione();
        this.mNumMesiReclusione = aModel.getNumMesiReclusione();
        this.mNumGiorniReclusione = aModel.getNumGiorniReclusione();
        this.mImportoMulta = aModel.getImportoMulta();
        
        this.mNumAnniArresto = aModel.getNumAnniArresto();
        this.mNumMesiArresto = aModel.getNumMesiArresto();
        this.mNumGiorniArresto = aModel.getNumGiorniArresto();
        this.mImportoAmmenda = aModel.getImportoAmmenda();

        this.mNumAnniPresofferto = aModel.getNumAnniPresofferto();
        this.mNumMesiPresofferto = aModel.getNumMesiPresofferto();
        this.mNumGiorniPresofferto = aModel.getNumGiorniPresofferto();

        this.mPosizioneGiuridica = aModel.getPosizioneGiuridica();
        this.mDataInizioPena = aModel.getDataInizioPena();    
        
        this.mNumAnniDaEspiare = aModel.getNumAnniDaEspiare();
        this.mNumMesiDaEspiare = aModel.getNumMesiDaEspiare();
        this.mNumGiorniDaEspiare = aModel.getNumGiorniDaEspiare();

        this.mSemestriUtili = aModel.getSemestriUtili();    
        this.mNumGgLaMatInPenaRes = aModel.getNumGgLaMatInPenaRes();    
        
        this.mLaFungibili = aModel.getLaFungibili();
        this.mLaNonConcesse = aModel.getLaNonConcesse();
        
        this.mNumAnniPenaIpotetica = aModel.getNumAnniPenaIpotetica();
        this.mNumMesiPenaIpotetica = aModel.getNumMesiPenaIpotetica();
        this.mNumGiorniPenaIpotetica = aModel.getNumGiorniPenaIpotetica();
        
        this.mSemestriUtiliPenaScontata = aModel.getSemestriUtiliPenaScontata();
        this.mLaMaturate = aModel.getLaMaturate();
        this.mLaApplicate = aModel.getLaApplicate();
        
        this.mDataScarcNoLa = aModel.getDataScarcNoLa();
        this.mDataScarcLaFung = aModel.getDataScarcLaFung();
        this.mDataScarcLaNoFung = aModel.getDataScarcLaNoFung();
        this.mDataScarcPenultimoSem = aModel.getDataScarcPenultimoSem(); 

        this.mCodOperatoreInserimento = aModel.getCodOperatoreInserimento();
        this.mDataInserimento = aModel.getDataInserimento();
        this.mCodUfficioInserimento = aModel.getCodUfficioInserimento();
        this.mCodOperatoreAggiornamento = aModel.getCodOperatoreAggiornamento();
        this.mDataAggiornamento = aModel.getDataAggiornamento();
        this.mCodUfficioAggiornamento = aModel.getCodUfficioAggiornamento();        
    }
    
    /**
     * Costruttore di "copia"
     * 
     * @param aModel
     */
    public CalcoloPenaDL92ModelDB (CalcoloPenaDL92Model aModel) {
        //@TODO Implementare la mappatura del model con i dati calcolati verso questo model che mappa il DB
        //this.mIdCalcoloPenaDL92 = aModel.getIdCalcoloPenaDL92();
        //this.mFasSieIdFascicoloSiep = aModel.getFasSieIdFascicoloSiep();
        
        this.mNumAnniReclusione = aModel.getNumAnniReclusione();
        this.mNumMesiReclusione = aModel.getNumMesiReclusione();
        this.mNumGiorniReclusione = aModel.getNumGiorniReclusione();
        this.mImportoMulta = aModel.getImportoMulta();
        
        this.mNumAnniArresto = aModel.getNumAnniArresto();
        this.mNumMesiArresto = aModel.getNumMesiArresto();
        this.mNumGiorniArresto = aModel.getNumGiorniArresto();
        this.mImportoAmmenda = aModel.getImportoAmmenda();

        this.mNumAnniPresofferto = aModel.getNumAnniPresofferto();
        this.mNumMesiPresofferto = aModel.getNumMesiPresofferto();
        this.mNumGiorniPresofferto = aModel.getNumGiorniPresofferto();

        this.mPosizioneGiuridica = aModel.getPosizioneGiuridica();
        this.mDataInizioPena = aModel.getDataInizioPena();    
        
        this.mNumAnniDaEspiare = aModel.getNumAnniDaEspiare();
        this.mNumMesiDaEspiare = aModel.getNumMesiDaEspiare();
        this.mNumGiorniDaEspiare = aModel.getNumGiorniDaEspiare();

        this.mSemestriUtili = aModel.getSemestriUtili();    
        this.mNumGgLaMatInPenaRes = aModel.getNumGiorniLAMaturataInPenaresidua();    
        
        this.mLaFungibili = aModel.getLAFungibili();
        this.mLaNonConcesse = aModel.getLANonConcesse();
        
        this.mNumAnniPenaIpotetica = aModel.getNumAnniPenaIpotetica();
        this.mNumMesiPenaIpotetica = aModel.getNumMesiPenaIpotetica();
        this.mNumGiorniPenaIpotetica = aModel.getNumGiorniPenaIpotetica();
        
        this.mSemestriUtiliPenaScontata = aModel.getSemestriUtiliPenaScontata();
        this.mLaMaturate = aModel.getLAMaturate();
        this.mLaApplicate = aModel.getLAApplicate();
        
        this.mDataScarcNoLa = aModel.getDataScarcerazioneNoLA();
        this.mDataScarcLaFung = aModel.getDataScarcerazioneLAFung();
        this.mDataScarcLaNoFung = aModel.getDataScarcerazioneLANoFung();
        this.mDataScarcPenultimoSem = aModel.getDataScarcerazionePenultimoSemestre(); 
    }
    
    //===========================================
    // Metodi Getter
    //===========================================
    public BigDecimal getIdCalcoloPenaDL92()     { return mIdCalcoloPenaDL92;     }
    public BigDecimal getFasSieIdFascicoloSiep() { return mFasSieIdFascicoloSiep; }
    
    public BigDecimal getNumAnniReclusione()    { return mNumAnniReclusione;   }
    public BigDecimal getNumMesiReclusione()    { return mNumMesiReclusione;   }
    public BigDecimal getNumGiorniReclusione()  { return mNumGiorniReclusione; }
    public BigDecimal getImportoMulta()         { return mImportoMulta;        }
    public BigDecimal getNumAnniArresto()       { return mNumAnniArresto;      }
    public BigDecimal getNumMesiArresto()       { return mNumMesiArresto;      }
    public BigDecimal getNumGiorniArresto()     { return mNumGiorniArresto;    }
    public BigDecimal getImportoAmmenda()       { return mImportoAmmenda;      }
    
    public BigDecimal getNumAnniPresofferto()   { return mNumAnniPresofferto;   }
    public BigDecimal getNumMesiPresofferto()   { return mNumMesiPresofferto;   }
    public BigDecimal getNumGiorniPresofferto() { return mNumGiorniPresofferto; }
    
    public String     getPosizioneGiuridica()   { return mPosizioneGiuridica; }
    public Date       getDataInizioPena()       { return mDataInizioPena;     }
    
    public BigDecimal getNumAnniDaEspiare()    { return mNumAnniDaEspiare;   }
    public BigDecimal getNumMesiDaEspiare()    { return mNumMesiDaEspiare;   }
    public BigDecimal getNumGiorniDaEspiare()  { return mNumGiorniDaEspiare; }
    
    public BigDecimal getSemestriUtili()       { return mSemestriUtili;       }
    public BigDecimal getNumGgLaMatInPenaRes() { return mNumGgLaMatInPenaRes; }
    public BigDecimal getLaFungibili()         { return mLaFungibili;         }
    public BigDecimal getLaNonConcesse()       { return mLaNonConcesse;       }
    
    public BigDecimal getNumAnniPenaIpotetica()   { return mNumAnniPenaIpotetica;   }
    public BigDecimal getNumMesiPenaIpotetica()   { return mNumMesiPenaIpotetica;   }
    public BigDecimal getNumGiorniPenaIpotetica() { return mNumGiorniPenaIpotetica; }
    
    public BigDecimal getSemestriUtiliPenaScontata() { return mSemestriUtiliPenaScontata; }
    public BigDecimal getLaMaturate()  { return mLaMaturate;  }
    public BigDecimal getLaApplicate() { return mLaApplicate; }
    
    public Date getDataScarcNoLa()         { return mDataScarcNoLa;    }
    public Date getDataScarcLaFung()       { return mDataScarcLaFung;    }
    public Date getDataScarcLaNoFung()     { return mDataScarcLaNoFung;    }
    public Date getDataScarcPenultimoSem() { return mDataScarcPenultimoSem;    }
    
    public String getCodOperatoreInserimento()   { return mCodOperatoreInserimento;}
    public Date   getDataInserimento()           { return mDataInserimento; }
    public String getCodUfficioInserimento()     { return mCodUfficioInserimento; }
    public String getCodOperatoreAggiornamento() { return mCodOperatoreAggiornamento; }
    public Date   getDataAggiornamento()         { return mDataAggiornamento; }
    public String getCodUfficioAggiornamento()   { return mCodUfficioAggiornamento; }
    
    
    public SemestreDL92Model getSemestrePresofferto()  { return mSemestrePresofferto; }
    public Vector<SemestreDL92Model> getListaSemetri() { return mListaSemetri; }
    
    //===========================================
    //
    //===========================================
    public void setIdCalcoloPenaDL92(BigDecimal mIdCalcoloPenaDL92) {
        this.mIdCalcoloPenaDL92 = mIdCalcoloPenaDL92;
    }
    public void setFasSieIdFascicoloSiep(BigDecimal mFasSieIdFascicoloSiep) {
        this.mFasSieIdFascicoloSiep = mFasSieIdFascicoloSiep;
    }
    public void setNumAnniReclusione(BigDecimal mNumAnniReclusione) {
        this.mNumAnniReclusione = mNumAnniReclusione;
    }
    public void setNumMesiReclusione(BigDecimal mNumMesiReclusione) {
        this.mNumMesiReclusione = mNumMesiReclusione;
    }
    public void setNumGiorniReclusione(BigDecimal mNumGiorniReclusione) {
        this.mNumGiorniReclusione = mNumGiorniReclusione;
    }
    public void setImportoMulta(BigDecimal mImportoMulta) {
        this.mImportoMulta = mImportoMulta;
    }
    public void setNumAnniArresto(BigDecimal mNumAnniArresto) {
        this.mNumAnniArresto = mNumAnniArresto;
    }
    public void setNumMesiArresto(BigDecimal mNumMesiArresto) {
        this.mNumMesiArresto = mNumMesiArresto;
    }
    public void setNumGiorniArresto(BigDecimal mNumGiorniArresto) {
        this.mNumGiorniArresto = mNumGiorniArresto;
    }
    public void setImportoAmmenda(BigDecimal mImportoAmmenda) {
        this.mImportoAmmenda = mImportoAmmenda;
    }
    public void setNumAnniPresofferto(BigDecimal mNumAnniPresofferto) {
        this.mNumAnniPresofferto = mNumAnniPresofferto;
    }
    public void setNumMesiPresofferto(BigDecimal mNumMesiPresofferto) {
        this.mNumMesiPresofferto = mNumMesiPresofferto;
    }
    public void setNumGiorniPresofferto(BigDecimal mNumGiorniPresofferto) {
        this.mNumGiorniPresofferto = mNumGiorniPresofferto;
    }
    public void setPosizioneGiuridica(String mPosizioneGiuridica) {
        this.mPosizioneGiuridica = mPosizioneGiuridica;
    }
    public void setDataInizioPena(Date mDataInizioPena) {
        this.mDataInizioPena = mDataInizioPena;
    }
    public void setNumAnniDaEspiare(BigDecimal mNumAnniDaEspiare) {
        this.mNumAnniDaEspiare = mNumAnniDaEspiare;
    }
    public void setNumMesiDaEspiare(BigDecimal mNumMesiDaEspiare) {
        this.mNumMesiDaEspiare = mNumMesiDaEspiare;
    }
    public void setNumGiorniDaEspiare(BigDecimal mNumGiorniDaEspiare) {
        this.mNumGiorniDaEspiare = mNumGiorniDaEspiare;
    }
    public void setSemestriUtili(BigDecimal mSemestriUtili) {
        this.mSemestriUtili = mSemestriUtili;
    }
    public void setNumGgLaMatInPenaRes(BigDecimal mNumGgLaMatInPenaRes) {
        this.mNumGgLaMatInPenaRes = mNumGgLaMatInPenaRes;
    }
    public void setLaFungibili(BigDecimal mLaFungibili) {
        this.mLaFungibili = mLaFungibili;
    }
    public void setLaNonConcesse(BigDecimal mLaNonConcesse) {
        this.mLaNonConcesse = mLaNonConcesse;
    }
    public void setNumAnniPenaIpotetica(BigDecimal mNumAnniPenaIpotetica) {
        this.mNumAnniPenaIpotetica = mNumAnniPenaIpotetica;
    }
    public void setNumMesiPenaIpotetica(BigDecimal mNumMesiPenaIpotetica) {
        this.mNumMesiPenaIpotetica = mNumMesiPenaIpotetica;
    }
    public void setNumGiorniPenaIpotetica(BigDecimal mNumGiorniPenaIpotetica) {
        this.mNumGiorniPenaIpotetica = mNumGiorniPenaIpotetica;
    }
    public void setSemestriUtiliPenaScontata(BigDecimal mSemestriUtiliPenaScontata) {
        this.mSemestriUtiliPenaScontata = mSemestriUtiliPenaScontata;
    }
    public void setLaMaturate(BigDecimal mLaMaturate) {
        this.mLaMaturate = mLaMaturate;
    }
    public void setLaApplicate(BigDecimal mLaApplicate) {
        this.mLaApplicate = mLaApplicate;
    }
    public void setDataScarcNoLa(Date mDataScarcNoLa) {
        this.mDataScarcNoLa = mDataScarcNoLa;
    }
    public void setDataScarcLaFung(Date mDataScarcLaFung) {
        this.mDataScarcLaFung = mDataScarcLaFung;
    }
    public void setDataScarcLaNoFung(Date mDataScarcLaNoFung) {
        this.mDataScarcLaNoFung = mDataScarcLaNoFung;
    }
    public void setDataScarcPenultimoSem(Date mDataScarcPenultimoSem) {
        this.mDataScarcPenultimoSem = mDataScarcPenultimoSem;
    }
    public void setCodOperatoreInserimento(String mCodOperatoreInserimento) {
        this.mCodOperatoreInserimento = mCodOperatoreInserimento;
    }
    public void setDataInserimento(Date mDataInserimento) {
        this.mDataInserimento = mDataInserimento;
    }
    public void setCodUfficioInserimento(String mCodUfficioInserimento) {
        this.mCodUfficioInserimento = mCodUfficioInserimento;
    }
    public void setCodOperatoreAggiornamento(String mCodOperatoreAggiornamento) {
        this.mCodOperatoreAggiornamento = mCodOperatoreAggiornamento;
    }
    public void setDataAggiornamento(Date mDataAggiornamento) {
        this.mDataAggiornamento = mDataAggiornamento;
    }
    public void setCodUfficioAggiornamento(String mCodUfficioAggiornamento) {
        this.mCodUfficioAggiornamento = mCodUfficioAggiornamento;
    }
    
    public void setSemestrePresofferto (SemestreDL92Model mSemestrePresofferto) {
        this.mSemestrePresofferto = mSemestrePresofferto; 
    }  
    public void setListaSemetri (Vector<SemestreDL92Model> mListaSemetri) {
        this.mListaSemetri = mListaSemetri;
    }
}
