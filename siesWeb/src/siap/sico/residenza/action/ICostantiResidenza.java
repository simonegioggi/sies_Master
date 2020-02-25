package siap.sico.residenza.action;

/**
* <p>Title: ICostantiResidenza</p>
* <p>Description: Classe di costanti di Residenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.siep.web.ISIEPCostantiWeb;

public interface ICostantiResidenza extends ISIEPCostantiWeb
{
  public static final String CAMPO_ID_RESIDENZA = "IdResidenza";
  public static final String CAMPO_COD_STATO = "CodStato";
  public static final String CAMPO_DESCR_STATO = "DescrStato";
  public static final String CAMPO_COD_PROVINCIA = "CodProvincia";
  public static final String CAMPO_COD_COMUNE = "CodComune";
  public static final String CAMPO_DESCR_COMUNE = "DescrComune";
  public static final String CAMPO_CAP = "Cap";
  public static final String CAMPO_INDIRIZZO = "Indirizzo";
  public static final String CAMPO_COD_TIPO_RESIDENZA = "CodTipoResidenza";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
  public static final String CAMPO_SOG_ID_SOGGETTO = "SogIdSoggetto";
  public static final String CAMPO_GIORNO_DATA_INIZIO_VALIDITA = "GiornoDataInizioValidita";
  public static final String CAMPO_MESE_DATA_INIZIO_VALIDITA = "MeseDataInizioValidita";
  public static final String CAMPO_ANNO_DATA_INIZIO_VALIDITA = "AnnoDataInizioValidita";
  public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA = "GiornoDataFineValidita";
  public static final String CAMPO_MESE_DATA_FINE_VALIDITA = "MeseDataFineValidita";
  public static final String CAMPO_ANNO_DATA_FINE_VALIDITA = "AnnoDataFineValidita";
  public static final String CAMPO_FAS_SIU_ID_FASCICOLO_SIUS = "FasSiuIdFascicoloSius";
  public static final String CAMPO_DESC_COMUNE_ESTERO = "DescComuneEstero";
  public static final String CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE = "FlagDomicilioPressoDifensore";
  
  // JSP della Residenza
  public static final String PG_LOAD_RICERCARESIDENZA	   = ROOT_DIR + "files/siap/sico/residenza/LoadRicercaResidenza.jsp";
  public static final String PG_LOAD_INSERISCIRESIDENZA	 = ROOT_DIR + "files/siap/sico/residenza/LoadInserisciResidenza.jsp";
  public static final String PG_RICERCARESIDENZA      	 = ROOT_DIR + "files/siap/sico/residenza/RicercaResidenza.jsp";

  // JSP del Domicilio
  public static final String PG_LOAD_INSERISCIDOMICILIO	 = ROOT_DIR + "files/siap/sico/residenza/LoadInserisciDomicilio.jsp";
  public static final String PG_RICERCADOMICILIO	       = ROOT_DIR + "files/siap/sico/residenza/RicercaDomicilio.jsp";

  // JSP comuni
  public static final String PG_DETTAGLIO_RES_DOM        = ROOT_DIR + "files/siap/sico/residenza/DettaglioResidenzaDomicilio.jsp";
}