package siap.sius.misuresicurezzarichiestaatti.action;

import f3b.web.IWebConstants;

public interface ICostantiMisureSicurezza
{
	// Costanti per form Richiesta Atti.
	  public static final String CAMPO_GIORNO_DATA_EMISSIONE  = "giornoDataEmissione";
	  public static final String CAMPO_MESE_DATA_EMISSIONE    = "meseDataEmissione";
	  public static final String CAMPO_ANNO_DATA_EMISSIONE    = "annoDataEmissione";
	  public static final String CAMPO_COD_DESTINATARIO       = "codDestinatario";
	  public static final String CAMPO_DES_DESTINATARIO       = "desDestinatario";
	  public static final String CAMPO_SEDE                   = "sede";
	  public static final String CAMPO_NOTE                   = "note";
	  public static final String CAMPO_AGGIUNTIVO             = "campoAggiuntivo";
	  public static final String CODTIPOEVENTO                = "05";
	  public static final String CODTIPONOTIFICA              = "R";
	  public static final String CAMPO_COD_AVVOCATO_DESTINATARIO       = "codAvvocatoDestinatario";
	  public static final String CAMPO_SEDE_AVVOCATO	      = "codSedeAvvocatoDestinatario";
	  public static final String CAMPO_INDIRIZZO_AVVOCATO     = "indirizzoAvvocatoDestinatario";
	  
	  // Campo Ulteriore di tipo data
	  public static final String CAMPO_ANNO_ULTERIORE         = "CampoAnnoUlteriore";
	  public static final String CAMPO_MESE_ULTERIORE         = "CampoMeseUlteriore";
	  public static final String CAMPO_GIORNO_ULTERIORE       = "CampoGiornoUlteriore";

	  // Elenco stampe.
	  public static final String PG_ELENCOSTAMPEDOCISTRUTTORI = IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/ElencoStampeDocIstruttori.jsp";
	  public static final String PG_LOAD_RICHIESTAINFORMAZIONIPERICOLOSITA= IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/LoadInserisciInformazioniPericolosita.jsp";
	  public static final String PG_LOAD_RICHIESTACONDECONOMICHE = IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/LoadInserisciRicCondEconomiche.jsp";
	  
	  public static final String PG_LOAD_RICHIESTARELAZIONESOCIOFAM = IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/LoadInserisciRichiestaSocioFam.jsp";
	  public static final String PG_LOAD_RICHIESTAINFDSMUOSM = IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/LoadInserisciInformazioniDSMUOSM.jsp";
	  public static final String PG_LOAD_RICHIESTACARTAPRECETTIVA= IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/LoadInserisciCartaPrecettiva.jsp";
	  
	  
	  public static final String PG_LOAD_RICHIESTALFE = IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/LoadInserisciCondizioniLFE.jsp";
	  public static final String PG_LOAD_RICHIESTAPERIZIAPSICHIATRICA = IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/LoadInserisciPeriziaPsichiatrica.jsp";
	  public static final String PG_LOAD_RICHIESTACERTIFICATICOMUNE = IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/LoadInserisciCertificatiComune.jsp";
	  public static final String PG_LOAD_RICHIESTAINPS = IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/LoadInserisciInfINPS.jsp";
	  
	  public static final String PG_LOAD_RICHIESTAINFORMAZIONIDOMICILIO= IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/LoadInserisciInformazioniDomicilio.jsp";
	  public static final String PG_LOAD_CARTELLABIOGRAFICA = IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/LoadInserisciCartellaBiografica.jsp";
	  	 
	  
	  public static final String PG_ELENCOSTAMPEMODELLIRICHIESTAATTI = IWebConstants.ROOT_DIR + "files/siap/sius/richiestaatti/ElencoStampeModelliRichiestaAtti.jsp";

	  
	  // Pagina di dettaglio Richiesta atti
	  public static final String PG_DETTAGLIO_ATTIMISURESICUREZZA = IWebConstants.ROOT_DIR + "files/siap/sius/misuresicurezzarichiestaatti/DettaglioRichiestaAtti.jsp";

	  
	  public static final String MSG_BUTTON_HISTORY = "Ritorna ad elenco documenti istruttori";
  
}