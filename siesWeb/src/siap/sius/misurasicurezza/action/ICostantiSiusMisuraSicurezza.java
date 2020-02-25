package siap.sius.misurasicurezza.action;

import f3b.web.IWebConstants;

public interface ICostantiSiusMisuraSicurezza
{
  //==========================================================================================
  // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  misura_sicurezza
  //========================================================================================== 
	  public static final String CAMPO_ID_MISURA_SICUREZZA		     = "IdMisuraSicurezza"; 
	  public static final String CAMPO_COD_NATURA = "CodNatura";
	  public static final String CAMPO_COD_TIPO = "CodTipo";
	  // MERGE v10: aggiunte due nuove costanti
	  public static final String CAMPO_DESCR_COD_NATURA = "DescrCodNatura";
	  public static final String CAMPO_DESCR_COD_TIPO = "DescrCodTipo";
	  public static final String CAMPO_NUM_ANNI = "NumAnni";
	  public static final String CAMPO_NUM_MESI = "NumMesi";
	  public static final String CAMPO_NUM_GIORNI = "NumGiorni";
	  public static final String CAMPO_COD_NATURA_NUOVA_MISURA = "CodNaturaNuovaMisura";
	  public static final String CAMPO_COD_TIPO_NUOVA_MISURA = "CodTipoNuovaMisura";
	  public static final String CAMPO_NUM_ANNI_NUOVA_MISURA = "NumAnniNuovaMisura";
	  public static final String CAMPO_NUM_MESI_NUOVA_MISURA = "NumMesiNuovaMisura";
	  public static final String CAMPO_NUM_GIORNI_NUOVA_MISURA = "NumGiorniNuovaMisura";
	  public static final String CAMPO_ANNO_REG_38 = "AnnoReg38";
	  public static final String CAMPO_NUM_REG_38 = "NumReg38";
	  public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	  public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	  public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	  public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	  public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
	  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
	  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
	  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
	  public static final String CAMPO_FAS_SIU_ID_FASCICOLO_SIUS = "FasSiuIdFascicoloSius"; 
	  public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
	  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_RIF = "FasSieIdFascicoloSiepRif";

//==============================================================================================
// Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  periodo_altra_misura
//==============================================================================================
    public static final String CAMPO_ID_PERIODO_ALTRA_MISURA        = "IdPeriodoAltraMisura";
    public static final String CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE  = "GiornoDataInizioEsecuzione";
    public static final String CAMPO_MESE_DATA_INIZIO_ESECUZIONE    = "MeseDataInizioEsecuzione";
    public static final String CAMPO_ANNO_DATA_INIZIO_ESECUZIONE    = "AnnoDataInizioEsecuzione";
    public static final String CAMPO_GIORNO_DATA_SCADENZA           = "GiornoDataScadenza";
    public static final String CAMPO_MESE_DATA_SCADENZA             = "MeseDataScadenza";
    public static final String CAMPO_ANNO_DATA_SCADENZA             = "AnnoDataScadenza";
    public static final String CAMPO_COD_TIPO_AUTORITA				= "CodTipoAutorita";
    public static final String CAMPO_COD_LUOGO_AUTORITA				= "CodLuogoAutorita";
    public static final String CAMPO_DESCR_TIPO_AUTORITA			= "DescrTipoAutorita";
    public static final String CAMPO_DESCR_LUOGO_AUTORITA			= "DescrLuogoAutorita";
    public static final String CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE = "IstDetIdIstitutoDetenzione";
    public static final String CAMPO_MOTIVAZIONE                    = "Motivazione";
    public static final String CAMPO_FLAG_MOTIVO					= "FlagMotivo";
    public static final String CAMPO_FLAG_VALIDA					= "FlagValida";
    public static final String CAMPO_SOSPENSIONE_GG				    = "SospensioneGG";
    public static final String CAMPO_SOSPENSIONE_MM					= "SospensioneMM";
    public static final String CAMPO_SOSPENSIONE_AA					= "SospensioneAA";
    // 12/05/2008 public static final String CAMPO_NUMERO_GIORNI					= "NumeroGiorni";
    public static final String CAMPO_DA_RECUPERARE					= "DaRecuperare";
    public static final String CAMPO_DA_RECUPERARE_GG		= "DaRecuperareGG";
    public static final String CAMPO_DA_RECUPERARE_MM		= "DaRecuperareMM";
    public static final String CAMPO_DA_RECUPERARE_AA		= "DaRecuperareAA";
    public static final String CAMPO_COD_TIPO_UFFICIO		= "TipoUfficio";
	  

  //========================================== 
  // Costanti che rappresentano le pagine jsp  
  //========================================== 
  public static final String PG_RICERCA_SIUS_MISURASICUREZZA  = IWebConstants.ROOT_DIR + "files/siap/sius/misurasicurezza/RicercaSiusMisuraSicurezza.jsp";
  public static final String PG_LOAD_DETTAGLIO_SIUS_MISURASICUREZZA  = IWebConstants.ROOT_DIR + "files/siap/sius/misurasicurezza/DettaglioSiusMisuraSicurezza.jsp";
  public static final String PG_LOAD_INSERISCI_SIUS_MISURASICUREZZA  = IWebConstants.ROOT_DIR + "files/siap/sius/misurasicurezza/LoadInserisciSiusMisuraSicurezza.jsp";
  public static final String PG_LOAD_MODIFICA_SIUS_MISURASICUREZZA   = IWebConstants.ROOT_DIR + "files/siap/sius/misurasicurezza/LoadModificaSiusMisuraSicurezza.jsp";
  public static final String PG_INCLUDE_ELENCO_MISURE  				 = IWebConstants.ROOT_DIR + "files/siap/sius/misurasicurezza/ElencoSiusMisureSicurezza.jsp";
//TODO carmela verificare
  public static final String CAMPO_COD_NATURA_NUOVA_MISURA_TWO = "CodNaturaNuovaMisuraTwo";
  public static final String CAMPO_COD_TIPO_NUOVA_MISURA_TWO = "CodTipoNuovaMisuraTwo";
  public static final String CAMPO_NUM_ANNI_NUOVA_MISURA_TWO = "NumAnniNuovaMisuraTwo";
  public static final String CAMPO_NUM_MESI_NUOVA_MISURA_TWO = "NumMesiNuovaMisuraTwo";
  public static final String CAMPO_NUM_GIORNI_NUOVA_MISURA_TWO = "NumGiorniNuovaMisuraTwo";

  public static final String CAMPO_GIORNO_DATA_DECORRENZA = "GiornoDataDecorrenzaMisura";
  public static final String CAMPO_MESE_DATA_DECORRENZA = "MeseDataDecorrenzaMisura";
  public static final String CAMPO_ANNO_DATA_DECORRENZA = "AnnoDataDecorrenzaMisura";
  public static final String CAMPO_DATA_DECORRENZA = "dataDecorrenzaMisura";

	 // inizio misura sicurezza
	 public static final String PG_LOADINSERISCIINIZIOMISURASICUREZZA = IWebConstants.ROOT_DIR +
	 				"files/siap/sius/misurasicurezza/LoadInserisciInizioMisuraSicurezzaUDS.jsp";
	 public static final String PG_LOAD_VALIDAINIZIOMISURASICUREZZA = IWebConstants.ROOT_DIR +
					"files/siap/sius/misurasicurezza/LoadValidaInizioMisuraSicurezzaUDS.jsp";
	 public static final String PG_LOAD_DETTAGLIOINIZIOMISURASICUREZZA = IWebConstants.ROOT_DIR +
					"files/siap/sius/misurasicurezza/LoadDettaglioInizioMisuraSicurezzaUDS.jsp";
	 public static final String PG_LOAD_LISTAMISURESICUREZZA = IWebConstants.ROOT_DIR +
					"files/siap/sius/misurasicurezza/LoadListaMisureSicurezzaUDS.jsp";
	 // sospensione misura sicurezza
	 public static final String PG_LOADINSERISCISOSPENSIONEMISURASICUREZZA = IWebConstants.ROOT_DIR +
		"files/siap/sius/misurasicurezza/LoadInserisciSospensioneMisuraSicurezzaUDS.jsp";
	 public static final String PG_LOAD_DETTAGLIOSOSPENSIONEMISURASICUREZZA = IWebConstants.ROOT_DIR +
		"files/siap/sius/misurasicurezza/LoadDettaglioSospensioneMisuraSicurezzaUDS.jsp";
	 public static final String PG_LOAD_MODIFICADATEINIZIOMISURASICUREZZA = IWebConstants.ROOT_DIR +
		"files/siap/sius/misurasicurezza/LoadModificaDateInizioMisuraSicurezzaUDS.jsp";
	 // Popup Lista Misure SIUS
	 public static final String PG_LISTA_POPUP_SIUS_MISURASICUREZZA  = IWebConstants.ROOT_DIR + "files/siap/sius/misurasicurezza/LoadPopupDettaglioMisuraSic.jsp";
	 

}