package siap.siep.pagoPaBatch.action;

import f3b.web.IWebConstants;

public interface ICostantiBatchPagoPa {
	
	public static final String CAMPO_ID_BATCH_PAGOPA = "IdBatchPagoPa";
	public static final String CAMPO_DATA_INIZIO_ESECUZIONE = "DataInizioEsecuzione";
	public static final String CAMPO_DATA_FINE_ESECUZIONE = "DataFineEsecuzione";
	public static final String CAMPO_NUM_POS_DEBITORIE_VERIFICATE = "NumPosDebitorieVerificate";
	public static final String CAMPO_NUM_BOLLETTINI_AGGIORNATI = "NumBollettiniAggiornati";
	public static final String CAMPO_ESITO_ESECUZIONE = "EsitoEsecuzione";
	public static final String CAMPO_ERRORE_ESECUZIONE = "ErroreEsecuzione";
	
	//
    public static final String CAMPO_SECONDI = "SecondiSchedulazione";
    public static final String CAMPO_MINUTI  = "MinutiSchedulazione";
    public static final String CAMPO_ORA     = "OraSchedulazione";
    public static final String CAMPO_GIORNO  = "GiornoSchedulazione"; // 1-31
    public static final String CAMPO_MESE    = "MeseSchedulazione";
    public static final String CAMPO_GG_SETT = "GiornoSettimanaSchedulazione";  // 1-SUN >> 7

    public static final String CAMPO_CHECK_GG_SET =  "CheckGGSett";
    
    public static final String CAMPO_CHECK_CRON_EXPR =  "CheckCronExpression";
    public static final String CAMPO_CRON_EXPR =  "CronExpression";
    
    public static final String STATO_INPAUSA    = "In Pausa";
    public static final String STATO_SCHEDULATO = "Schedulato";
    public static final String STATO_ERRORE     = "Errore";
    
    public static final String CAMPO_GIORNO_ESECUZIONE_INIZIALE = "GiornoEsecuzioneIniziale";
    public static final String CAMPO_MESE_ESECUZIONE_INIZIALE = "MeseEsecuzioneIniziale";
    public static final String CAMPO_ANNO_ESECUZIONE_INIZIALE = "AnnoEsecuzioneIniziale";

    public static final String CAMPO_GIORNO_ESECUZIONE_FINALE = "GiornoEsecuzioneFinale";
    public static final String CAMPO_MESE_ESECUZIONE_FINALE = "MeseEsecuzioneFinale";
    public static final String CAMPO_ANNO_ESECUZIONE_FINALE = "AnnoEsecuzioneFinale";    
    
    
    public static final String PG_LOAD_DETTAGLIO_BATCH_PAGOPA = IWebConstants.ROOT_DIR
            + "files/siap/siep/pagoPABatch/LoadDettaglioBatchPagoPA.jsp";
    
    public static final String PG_LOAD_CONFIGURA_BATCH_PAGOPA = IWebConstants.ROOT_DIR
            + "files/siap/siep/pagoPABatch/LoadConfiguraBatchPagoPA.jsp";
    
    public static final String PG_LOAD_DETTAGLIO_ESECUZIONE_BATCH = IWebConstants.ROOT_DIR
            + "files/siap/siep/pagoPABatch/LoadDettaglioEsecuzioneBatchPagoPA.jsp";
    
    public static final String PG_LOAD_DETTAGLIO_SCHEDULAZIONE_BATCH_PAGOPA = IWebConstants.ROOT_DIR
            + "files/siap/siep/pagoPABatch/LoadDettaglioSchedulazioneBatchPagoPA.jsp";
}
