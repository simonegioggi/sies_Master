package siap.sius.sanzionesostitutiva.action;

import f3b.web.IWebConstants;

/**
 * <p>Title: ICostantiSanzioneSostitutiva</p>
 * <p>Description: Classe di costanti di SanzioneSostitutiva</p>
 */

public interface ICostantiSanzioneSostitutiva {

	 // inizio sanzione sostitutiva
	 public static final String PG_LOADINSERISCIINIZIOSANZIONESOSTITUTIVA = IWebConstants.ROOT_DIR +
	 				"files/siap/sius/sanzionesostitutiva/LoadInserisciInizioSanzioneSostitutivaUDS.jsp";
	 public static final String PG_LOAD_VALIDAINIZIOSANZIONESOSTITUTIVA = IWebConstants.ROOT_DIR +
					"files/siap/sius/sanzionesostitutiva/LoadValidaInizioSanzioneSostitutivaUDS.jsp";
	 public static final String PG_LOAD_DETTAGLIOINIZIOSANZIONESOSTITUTIVA = IWebConstants.ROOT_DIR +
					"files/siap/sius/sanzionesostitutiva/LoadDettaglioInizioSanzioneSostitutivaUDS.jsp";
	 public static final String PG_LOAD_LISTASANZIONISOSTITUTIVE = IWebConstants.ROOT_DIR +
					"files/siap/sius/sanzionesostitutiva/LoadListaSanzioniSostitutiveUDS.jsp";
	 // sospensione sanzione sostitutiva
	 public static final String PG_LOADINSERISCISOSPENSIONESANZIONESOSTITUTIVA = IWebConstants.ROOT_DIR +
		"files/siap/sius/sanzionesostitutiva/LoadInserisciSospensioneSanzioneSostitutivaUDS.jsp";
	 public static final String PG_LOAD_DETTAGLIOSOSPENSIONESANZIONESOSTITUTIVA = IWebConstants.ROOT_DIR +
		"files/siap/sius/sanzionesostitutiva/LoadDettaglioSospensioneSanzioneSostitutivaUDS.jsp";


	 //==========================================================================
	    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella
	    //==========================================================================
	    public static final String CAMPO_ID_PERIODO_ALTRA_SANZIONE      = "IdPeriodoAltraSanzione";
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
	    public static final String CAMPO_EVE_ID_EVENTO                  = "EveIdEvento";
	    public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP      = "FasSieIdFascicoloSiep";
	    public static final String CAMPO_COD_OPERATORE_INSERIMENTO      = "CodOperatoreInserimento";
	    public static final String CAMPO_GIORNO_DATA_INSERIMENTO        = "GiornoDataInserimento";
	    public static final String CAMPO_MESE_DATA_INSERIMENTO          = "MeseDataInserimento";
	    public static final String CAMPO_ANNO_DATA_INSERIMENTO          = "AnnoDataInserimento";
	    public static final String CAMPO_COD_UFFICIO_INSERIMENTO        = "CodUfficioInserimento";
	    public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO    = "CodOperatoreAggiornamento";
	    public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO      = "GiornoDataAggiornamento";
	    public static final String CAMPO_MESE_DATA_AGGIORNAMENTO        = "MeseDataAggiornamento";
	    public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO        = "AnnoDataAggiornamento";
	    public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO      = "CodUfficioAggiornamento";
	    public static final String CAMPO_FAS_SIU_ID_FASCICOLO_SIUS		= "FasSiuIdFascicoloSius";
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

}
