package siap.siep.modulocumulo.util;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;

/**
 * Classe di Utility per caricare sotto forma di CalendarModel
 * 
 * @since MEV_2025-48 - ALTRO – Ordinamento Periodi Carcerazione Sofferti
 * 
 * */
public class PeriodiCarcerazioneSoffertiModel {

    private String        mDescrizione = null;
    private String        mDescTitolo  = null;
    private CalendarModel mCalendar = null;
    private Date          mDataInizio = null; //  per l'ordinamento
    private BigDecimal    mIdTitoloCumulato = null;  
    private boolean       mIsDataInizioEvidenziata = false;
    private boolean       mIsDataFineEvidenziata   = false;    
    private String        mMsgAlert   = null;
    
    // periodo effettivo ma da non conteggiare come singolo 
    private boolean       mIsInContinuazione = false; 
    // periodo fittizio ottenuto aggregando quelli in continuazione per rifare i calcoli
    private boolean       mIsContinuativo = false; 
    
    // Nel caso di periodo fittizio in continuazione dove inglobare i singoli
    // periodi che lo compongono
    private Vector <PeriodiCarcerazioneSoffertiModel> lListaPerInContinuazione = new Vector <PeriodiCarcerazioneSoffertiModel>();
    
    public String        getDescrizione()           { return mDescrizione; }
    public String        getDescTitolo()            { return mDescTitolo; }
    public CalendarModel getCalendar()              { return mCalendar; }
    public Date          getDataInizio()            { return mDataInizio; }
    public BigDecimal    getIdTitoloCumulato()      { return mIdTitoloCumulato; }
    public String        getMsgAlert()              { return mMsgAlert; }    
    public boolean       isDataInizioEvidenziata()  { return mIsDataInizioEvidenziata; }
    public boolean       isDataFineEvidenziata()    { return mIsDataFineEvidenziata; }       
    public boolean       isInContinuazione()        { return mIsInContinuazione; }
    public boolean       isContinuativo()           { return mIsContinuativo; }
    
    
    public void setDescrizione(String mDescrizione)   
        {this.mDescrizione = mDescrizione; }
    public void setDescTitolo(String mDescTitolo)   
        {this.mDescTitolo = mDescTitolo; }
    public void setCalendar (CalendarModel mCalendar) 
        {this.mCalendar = mCalendar; }
    public void setDataInizio (Date mDataInizio) 
        {this.mDataInizio = mDataInizio; }
    public void setIdTitoloCumulato (BigDecimal mIdTitoloCumulato) 
        { this.mIdTitoloCumulato = mIdTitoloCumulato;}    
    public void setIsDataInizioEvidenziata(boolean mIsDataInizioEvidenziata)   
        {this.mIsDataInizioEvidenziata = mIsDataInizioEvidenziata; }   
    public void setIsDataFineEvidenziata(boolean mIsDataFineEvidenziata)   
        {this.mIsDataFineEvidenziata = mIsDataFineEvidenziata; } 
    public void setMsgAlert(String mMsgAlert)   
        {this.mMsgAlert = mMsgAlert; } 
    public void setIsInContinuazione(boolean mIsInContinuazione)   
        {this.mIsInContinuazione = mIsInContinuazione; }
    public void setIsContinuativo(boolean mIsContinuativo)   
        {this.mIsContinuativo = mIsContinuativo; }
    
    public void ricalcolaQuantum(){
        if (mCalendar!=null) {
            CalendarUtil lCalUtils = new CalendarUtil();
            CalendarModel lCalCalcolo = new CalendarModel();
            lCalCalcolo = lCalUtils.CalcolaNumGiorniMesiAnni(mCalendar, false);
            mCalendar.setNumGiorni (lCalCalcolo.getNumGiorni());
            mCalendar.setNumMesi   (lCalCalcolo.getNumMesi());
            mCalendar.setNumAnni   (lCalCalcolo.getNumAnni());
        }
    }
    
    public void addPeriodoInContinuazione (PeriodiCarcerazioneSoffertiModel lModel) {
        lListaPerInContinuazione.add(lModel);
    }
    
    public Vector <PeriodiCarcerazioneSoffertiModel> getListaPerInContinuazione (){
        // Riordino prima la lista 
        Collections.sort(lListaPerInContinuazione, Comparator.comparing(PeriodiCarcerazioneSoffertiModel::getDataInizio));
        
        return lListaPerInContinuazione;
    }
}
