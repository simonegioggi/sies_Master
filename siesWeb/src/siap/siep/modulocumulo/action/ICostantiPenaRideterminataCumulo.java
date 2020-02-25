package siap.siep.modulocumulo.action;

/**
* <p>Title: ICostantiPenaRideterminataCumulo</p>
* <p>Description: Classe di costanti di PenaRideterminataCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/


public interface ICostantiPenaRideterminataCumulo {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_PENA_RIDETERMINATA_CUMULO = "IdPenaRideterminataCumulo"; 
    public static final String CAMPO_COD_TIPO_PENA_DETENTIVA      = "CodTipoPenaDetentiva"; 
    public static final String CAMPO_NUM_ANNI_RECLUSIONE          = "NumAnniReclusione"; 
    public static final String CAMPO_NUM_MESI_RECLUSIONE          = "NumMesiReclusione"; 
    public static final String CAMPO_NUM_GIORNI_RECLUSIONE        = "NumGiorniReclusione"; 
    public static final String CAMPO_IMPORTO_MULTA                = "ImportoMulta"; 
    public static final String CAMPO_NUM_ANNI_ARRESTO             = "NumAnniArresto"; 
    public static final String CAMPO_NUM_MESI_ARRESTO             = "NumMesiArresto"; 
    public static final String CAMPO_NUM_GIORNI_ARRESTO           = "NumGiorniArresto"; 
    public static final String CAMPO_IMPORTO_AMMENDA              = "ImportoAmmenda"; 
    public static final String CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO   = "NumAnniIsolamentoDiurno"; 
    public static final String CAMPO_NUM_MESI_ISOLAMENTO_DIURNO   = "NumMesiIsolamentoDiurno"; 
    public static final String CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO = "NumGiorniIsolamentoDiurno"; 
    public static final String CAMPO_NUMERO_GIORNI_LA             = "NumeroGiorniLa"; 
    public static final String CAMPO_NUMERO_GIORNI_LS             = "NumeroGiorniLs"; 
    public static final String CAMPO_NUMERO_GIORNI_LI             = "NumeroGiorniLi"; 
    public static final String CAMPO_NUMERO_GIORNI_RIDUZIONE      = "NumeroGiorniRiduzione"; 
    public static final String CAMPO_NUMERO_GIORNI_SCOMPUTO       = "NumeroGiorniScomputo"; 
    
    public static final String CAMPO_DAT_ID_DATI_FINALI_CUMULO    = "DatIdDatiFinaliCumulo"; 
    public static final String CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO   = "IstrIdIstruttoriaCumulo"; 
    public static final String CAMPO_COD_OPERATORE_INSERIMENTO    = "CodOperatoreInserimento"; 
    public static final String CAMPO_GIORNO_DATA_INSERIMENTO      = "GiornoDataInserimento"; 
    public static final String CAMPO_MESE_DATA_INSERIMENTO        = "MeseDataInserimento"; 
    public static final String CAMPO_ANNO_DATA_INSERIMENTO        = "AnnoDataInserimento"; 
    public static final String CAMPO_COD_UFFICIO_INSERIMENTO      = "CodUfficioInserimento"; 
    public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO  = "CodOperatoreAggiornamento"; 
    public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO    = "GiornoDataAggiornamento"; 
    public static final String CAMPO_MESE_DATA_AGGIORNAMENTO      = "MeseDataAggiornamento"; 
    public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO      = "AnnoDataAggiornamento"; 
    public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO    = "CodUfficioAggiornamento"; 
    
    public static final String CAMPO_FLAG_PENA_RESIDUA_CUMULO     = "FlagPenaResiduaCumulo"; 
    public static final String CAMPO_GIORNO_DATA_INIZIO           = "GiornoDataInizio"; 
    public static final String CAMPO_MESE_DATA_INIZIO             = "MeseDataInizio"; 
    public static final String CAMPO_ANNO_DATA_INIZIO             = "AnnoDataInizio"; 
    public static final String CAMPO_GIORNO_DATA_FINE_RECLUSIONE  = "GiornoDataFineReclusione"; 
    public static final String CAMPO_MESE_DATA_FINE_RECLUSIONE    = "MeseDataFineReclusione"; 
    public static final String CAMPO_ANNO_DATA_FINE_RECLUSIONE    = "AnnoDataFineReclusione"; 
    public static final String CAMPO_GIORNO_DATA_INIZIO_ARRESTO   = "GiornoDataInizioArresto"; 
    public static final String CAMPO_MESE_DATA_INIZIO_ARRESTO     = "MeseDataInizioArresto"; 
    public static final String CAMPO_ANNO_DATA_INIZIO_ARRESTO     = "AnnoDataInizioArresto"; 
    public static final String CAMPO_GIORNO_DATA_FINE_PRESUNTA    = "GiornoDataFinePresunta"; 
    public static final String CAMPO_MESE_DATA_FINE_PRESUNTA      = "MeseDataFinePresunta"; 
    public static final String CAMPO_ANNO_DATA_FINE_PRESUNTA      = "AnnoDataFinePresunta"; 
    public static final String CAMPO_GIORNO_DATA_FINE             = "GiornoDataFine"; 
    public static final String CAMPO_MESE_DATA_FINE               = "MeseDataFine"; 
    public static final String CAMPO_ANNO_DATA_FINE               = "AnnoDataFine";     
    

    
    // Campi Check per la PENA_DETENTIVA (PD)
    public static final String CAMPO_CHECK_RECLUSIONE_PD      = "check_reclusione_pd"; 
    public static final String CAMPO_CHECK_RECLUSIONE_PD_VAL  = "check_reclusione_pd_val"; 
    
    public static final String CAMPO_CHECK_MULTA_PD       = "check_multa_pd"; 
    public static final String CAMPO_CHECK_MULTA_PD_VAL   = "check_multa_pd_val"; 

    public static final String CAMPO_CHECK_ARRESTO_PD     = "check_arresto_pd"; 
    public static final String CAMPO_CHECK_ARRESTO_PD_VAL = "check_arresto_pd_val"; 
    
    public static final String CAMPO_CHECK_AMMENDA_PD     = "check_ammenda_pd"; 
    public static final String CAMPO_CHECK_AMMENDA_PD_VAL = "check_ammenda_pd_val"; 

    public static final String CAMPO_CHECK_ERGASTOLO_PD     = "check_regastolo_pd";    
    public static final String CAMPO_CHECK_ERGASTOLO_PD_VAL = "check_regastolo_pd_val";    
    
    //Liberazione Anticipata
    public static final String CAMPO_CHECK_LIBANT = "check_libAnt"; 
    public static final String CAMPO_CHECK_LIBANT_VAL = "check_libAnt_val"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
}