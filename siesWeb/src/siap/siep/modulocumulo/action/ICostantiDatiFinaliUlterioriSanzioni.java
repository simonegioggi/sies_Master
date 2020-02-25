package siap.siep.modulocumulo.action;


/**
* <p>Title: ICostantiDatiFinaliUlterioriSanzioni</p>
* <p>Description: Classe di costanti di DatiFinaliUlterioriSanzioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/


public interface ICostantiDatiFinaliUlterioriSanzioni {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
//    public static final String CAMPO_ID_DATI_FINALI_ULTERIORI_SANZ = "IdDatiFinaliUlterioriSanz"; 
//    public static final String CAMPO_COD_TIPO_ULTERIORE_SANZIONE   = "CodTipoUlterioreSanzione"; 
//    public static final String CAMPO_NUM_ANNI                      = "NumAnni"; 
//    public static final String CAMPO_NUM_MESI                      = "NumMesi"; 
//    public static final String CAMPO_NUM_GIORNI                    = "NumGiorni"; 
//    public static final String CAMPO_MULTA                         = "Multa"; 
//    public static final String CAMPO_AMMENDA                       = "Ammenda"; 
//    public static final String CAMPO_FLAG_ESPUL_PERP               = "FlagEspulPerp"; 
//    public static final String CAMPO_COD_TIPO_LPU                  = "CodTipoLpu"; 
//    public static final String CAMPO_NUM_ORE_TOT                   = "NumOreTot"; 
//    public static final String CAMPO_NUM_ORE_SETT                  = "NumOreSett"; 
//    public static final String CAMPO_COD_FREQ_SETT                 = "CodFreqSett"; 
//    public static final String CAMPO_DAT_ID_DATI_FINALI_CUMULO     = "DatIdDatiFinaliCumulo"; 
//    public static final String CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO    = "IstrIdIstruttoriaCumulo"; 

    
    
    // Sanzione Sostitutiva - Semidetenzione
    public static final String CAMPO_CHECK_SEMIDETENZIONE_SS  = "check_semidetenzione_ss";
    public static final String COD_TIPO_SANZIONE_SEMIDET_SS = "01";

    public static final String CAMPO_ID_SEMIDETENZIONE_SS = "IdDatiFinaliUlterioriSanzSemidetenzione";
    
    public static final String CAMPO_NUM_ANNI_SEMIDETENZIONE_SS   = "NumAnniSemidetenzioneSS";
    public static final String CAMPO_NUM_MESI_SEMIDETENZIONE_SS   = "NumMesiSemidetenzioneSS";
    public static final String CAMPO_NUM_GIORNI_SEMIDETENZIONE_SS = "NumGiorniSemidetenzioneSS";

    
    // Sanzione Sostitutiva - Libertà Controllata
    public static final String CAMPO_CHECK_LIBERTACTRL_SS  = "check_libertaCtrl_ss"; 
    public static final String COD_TIPO_SANZIONE_LIBCTRL_SS = "02";

    public static final String CAMPO_ID_LIBERTCONTR_SS = "IdDatiFinaliUlterioriSanzLibertaCtrl";
    public static final String CAMPO_NUM_ANNI_LIBERTACTRL_SS   = "NumAnniLibertaCtrlSS";
    public static final String CAMPO_NUM_MESI_LIBERTACTRL_SS   = "NumMesiLibertaCtrlSS";
    public static final String CAMPO_NUM_GIORNI_LIBERTACTRL_SS = "NumGiorniLibertaCtrlSS";

    // Sanzione Sostitutiva - Pena Pecuniaria Multa
    public static final String CAMPO_CHECK_MULTA_SS = "checkMultaSS";
    public static final String COD_TIPO_SANZIONE_MULTA_SS = "04";

    public static final String CAMPO_ID_MULTA_SS = "IdDatiFinaliUlterioriSanzMultaSS";
    public static final String CAMPO_INTERO_MULTA_APPLICATA_SS = "interoMultaApplicataSS";
    public static final String CAMPO_DECIMALE_MULTA_APPLICATA_SS = "decimaleMultaApplicataSS";
    
    // Sanzione Sostitutiva - Pena Pecuniaria Ammenda
    public static final String CAMPO_CHECK_AMMENDA_SS = "checkAmmendaSS";
    public static final String COD_TIPO_SANZIONE_AMMENDA_SS = "04";

    public static final String CAMPO_ID_AMMENDA_SS = "IdDatiFinaliUlterioriSanzAmmendaSS";
    public static final String CAMPO_INTERO_AMMENDA_APPLICATA_SS = "interoAmmendaApplicataSS";
    public static final String CAMPO_DECIMALE_AMMENDA_APPLICATA_SS = "decimaleAmmendaApplicataSS"; 

    // Sanzione Sostitutiva - Espulsione
    public static final String CAMPO_CHECK_ESPULSIONE_STATO_SS = "checkEspulsioneStatoSS";
    public static final String COD_TIPO_SANZIONE_ESPULSIONE_SS = "03";
    
    public static final String CAMPO_ID_ESPULSIONE_SS = "IdDatiFinaliUlterioriSanzEspulsioneSS";

    public static final String CAMPO_CHECK_ESPULSIONE_PERPETUA_SS = "checkEspulsionePerpetuaSS";
    
    public static final String CAMPO_NUM_ANNI_ESPULSIONE_SS = "NumAnniEspulsioneSS";
    public static final String CAMPO_NUM_MESI_ESPULSIONE_SS = "NumMesiEspulsioneSS";
    public static final String CAMPO_NUM_GIORNI_ESPULSIONE_SS = "NumGiorniEspulsioneSS";
    
    // Sanzione Sostitutiva - Lavoro Pubblica Utilità
    public static final String CAMPO_CHECK_LPU_SS = "checkLPUSS";
    
    public static final String COD_TIPO_SANZIONE_LPU_SS = "11";
    public static final String CAMPO_ID_LPU_SS = "IdDatiFinaliUlterioriSanzLPUSS";
   
    
    public static final String CAMPO_COD_TIPO_LPU_SS   = "CodTipoLpuSS"; //Combo
    public static final String CAMPO_NUM_ANNI_LPU_SS   = "NumAnniLPUSS";
    public static final String CAMPO_NUM_MESI_LPU_SS   = "NumMesiLPUSS";
    public static final String CAMPO_NUM_GIORNI_LPU_SS = "NumGiorniLPUSS";
    public static final String CAMPO_NUM_ORE_TOT       = "NumOreTot"; 
    public static final String CAMPO_NUM_ORE_SETT      = "NumOreSett"; 
    
    public static final String CAMPO_COD_FREQ_SETT   = "CodFreqSett"; 
    public static final String VAL_COD_FREQ_NON_DET  = "0";  // Non Determinata
    public static final String VAL_COD_FREQ_DETER    = "1";  // Determinata
    
    // Campi per per la tipologia orario. I campi sono composti con la costante_n dove n=1=lunedi, n=2=martedi...
    public static final String CAMPO_LPU_CHECK_ORARIO_DAY    = "LPUCheckOrarioDay"; // Check tipologia orario LPUFlagOrarioDay_1 = lunedi...
    public static final String CAMPO_LPU_IDORA_DAY     = "LPUIdOraDay";
    public static final String CAMPO_LPU_DALLE_ORE_DAY = "LPUDalleOreDay";
    public static final String CAMPO_LPU_ALLE_ORE_DAY  = "LPUAlleOreDay";
    
    
    // Pena da conversione Pena Pecuniaria 
    public static final String CAMPO_CHECK_LAVSOST_PP = "check_lavsost_pp"; 
    public static final String COD_TIPO_SANZIONE_LAVSOST_PP = "05";

    public static final String CAMPO_ID_LAVSOST_PP = "IdDatiFinaliUlterioriSanzLavSostPP";
    public static final String CAMPO_NUM_ANNI_LAVSOST_PP   = "NumAnniLavSostPP";
    public static final String CAMPO_NUM_MESI_LAVSOST_PP   = "NumMesiLavSostPP";
    public static final String CAMPO_NUM_GIORNI_LAVSOST_PP = "NumGiorniLavSostPP"; 
    
    
    
    public static final String CAMPO_CHECK_LIBCTRL_PP = "check_libctrl_pp"; 
    public static final String COD_TIPO_SANZIONE_LIBCTRL_PP = "12";  //new da aggiungere

    public static final String CAMPO_ID_LIBCTRL_PP = "IdDatiFinaliUlterioriSanzLibCtrlPP";
    public static final String CAMPO_NUM_ANNI_LIBCTRL_PP   = "NumAnniLibCtrlPP";
    public static final String CAMPO_NUM_MESI_LIBCTRL_PP   = "NumMesiLibCtrlPP";
    public static final String CAMPO_NUM_GIORNI_LIBCTRL_PP = "NumGiorniLibCtrlPP";     
    
    
    // Sanzioni del Giudice di pace
    // Permanenza Domiciliare 
    public static final String CAMPO_CHECK_PERMANENZA_GP = "check_permanenza_gp"; 
    public static final String COD_TIPO_SANZIONE_PERMDOM_GP = "07";

    public static final String CAMPO_ID_PERMDOM_GP = "IdDatiFinaliUlterioriSanzPermDomGP";
    public static final String CAMPO_NUM_ANNI_PERMDOM_GP   = "NumAnniPermDomGP";
    public static final String CAMPO_NUM_MESI_PERMDOM_GP   = "NumMesiPermDomGP";
    public static final String CAMPO_NUM_GIORNI_PERMDOM_GP = "NumGiorniPermDomGP";     
    
    
    //Lavoro sostitutivo 
    public static final String CAMPO_CHECK_LAVSOST_GP    = "check_lavsost_gp"; 
    public static final String COD_TIPO_SANZIONE_LAVSOST_GP = "08";

    public static final String CAMPO_ID_LAVSOST_GP = "IdDatiFinaliUlterioriSanzLavSostGP";
    public static final String CAMPO_NUM_ANNI_LAVSOST_GP   = "NumAnniLavSostGP";
    public static final String CAMPO_NUM_MESI_LAVSOST_GP   = "NumMesiLavSostGP";
    public static final String CAMPO_NUM_GIORNI_LAVSOST_GP = "NumGiorniLavSostGP";     
    
    
    // Lavoro pubblica utilità 
    public static final String CAMPO_CHECK_LPU_GP        = "check_LPU_gp";     
    public static final String COD_TIPO_SANZIONE_LPU_GP = "09";

    public static final String CAMPO_ID_LPU_GP = "IdDatiFinaliUlterioriSanzLpuGP";
    public static final String CAMPO_NUM_ANNI_LPU_GP   = "NumAnniLpuGP";
    public static final String CAMPO_NUM_MESI_LPU_GP   = "NumMesiLpuGP";
    public static final String CAMPO_NUM_GIORNI_LPU_GP = "NumGiorniLpuGP";     
    
    // Espulsione del GP 
    public static final String CAMPO_CHECK_ESP_GP        = "check_ESP_gp";     
    public static final String COD_TIPO_SANZIONE_ESP_GP = "13";

    public static final String CAMPO_ID_ESPULSIONE_GP = "IdDatiFinaliUlterioriSanzEspulsioneGP";
    
    public static final String CAMPO_CHECK_ESPULSIONE_PERPETUA_GP = "checkEspulsionePerpetuaGP";
    public static final String CAMPO_NUM_ANNI_ESP_GP   = "NumAnniEspGP";
    public static final String CAMPO_NUM_MESI_ESP_GP   = "NumMesiEspGP";
    public static final String CAMPO_NUM_GIORNI_ESP_GP = "NumGiorniEspGP";     
    
    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    
    

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
}