package siap.sius.scadenzario.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiScadenzario</p>
* <p>Description: Classe di costanti di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiScadenzarioSius
{
  public static final String CAMPO_ID_SCADENZARIO = "IdScadenzario";
  public static final String CAMPO_COD_TIPO_SCADENZARIO = "CodTipoScadenzario";
  public static final String CAMPO_DESC_TIPO_SCADENZARIO = "DescTipoScadenzario";
  public static final String CAMPO_GIORNO_DATA_INIZIO_SCADENZA = "GiornoDataInizioScadenza";
  public static final String CAMPO_MESE_DATA_INIZIO_SCADENZA = "MeseDataInizioScadenza";
  public static final String CAMPO_ANNO_DATA_INIZIO_SCADENZA = "AnnoDataInizioScadenza";
  public static final String CAMPO_GIORNO_DATA_FINE_SCADENZA = "GiornoDataFineScadenza";
  public static final String CAMPO_MESE_DATA_FINE_SCADENZA = "MeseDataFineScadenza";
  public static final String CAMPO_ANNO_DATA_FINE_SCADENZA = "AnnoDataFineScadenza";

  public static final String CAMPO_ANNI_SCADENZA = "AnniScadenza";
  public static final String CAMPO_MESI_SCADENZA = "MesiScadenza";
  public static final String CAMPO_GIORNI_SCADENZA = "GiorniScadenza";
  public static final String CAMPO_TIPO_RICERCA = "TipoRicerca";

  public static final String TIPO_SCA_IRREVOCABILITA_PROVVEDIMENTO = "50";
  public static final String TIPO_SCA_TERMINE_SOTTOSCRIZIONE_VERBALE_MA= "70";
  public static final String TIPO_SCA_TERMINE_SANZIONE_SOSTITUTIVA = "80";
  public static final String TIPO_SCA_TERMINE_DIFFERIMENTO = "81";
  public static final String TIPO_SCA_RICHIESTA_CONVERSIONE_PP = "82";

  public static final String CAMPO_FLAG_VISTO = "FlagVisto";
  public static final String CAMPO_GIORNO_DATA_VISTO = "GiornoDataVisto";
  public static final String CAMPO_MESE_DATA_VISTO = "MeseDataVisto";
  public static final String CAMPO_ANNO_DATA_VISTO = "AnnoDataVisto";
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
  public static final String PG_LOAD_RICERCASCADENZARIO	= IWebConstants.ROOT_DIR + "files/siap/sius/scadenzario/LoadRicercaScadenzarioSius.jsp";
  public static final String PG_LOAD_DETTAGLIOSCADENZARIO	= IWebConstants.ROOT_DIR + "files/siap/sius/scadenzario/DettaglioScadenzario.jsp";
  public static final String PG_RICERCASCADENZARIO	= IWebConstants.ROOT_DIR + "files/siap/sius/scadenzario/RicercaScadenzarioSius.jsp";
  public static final String PG_LOAD_INSERISCISCADENZARIO	= IWebConstants.ROOT_DIR + "files/siap/siep/scadenzario/LoadInserisciScadenzario.jsp";
  public static final String PG_LOAD_RICERCASCADENZARIOFINEPENA = IWebConstants.ROOT_DIR + "files/siap/siep/scadenzario/LoadRicercaScadenzarioFinePena.jsp";
  public static final String PG_RICERCASCADENZARIOFINEPENA	= IWebConstants.ROOT_DIR + "files/siap/siep/scadenzario/RicercaScadenzarioFinePena.jsp";
  public static final String PG_LOAD_DETTAGLIOSCADENZARIOFINEPENA	= IWebConstants.ROOT_DIR + "files/siap/siep/scadenzario/DettaglioScadenzarioFinePena.jsp";
  public static final String PG_LOAD_RICERCASCADENZARIOVANERICERCHE	= IWebConstants.ROOT_DIR + "files/siap/siep/scadenzario/LoadRicercaScadenzarioVaneRicerche.jsp";
  public static final String PG_RICERCASCADENZARIOVANERICERCHE	= IWebConstants.ROOT_DIR + "files/siap/siep/scadenzario/RicercaScadenzarioVaneRicerche.jsp";
  public static final String PG_LOAD_DETTAGLIOSCADENZARIOVANERICERCHE	= IWebConstants.ROOT_DIR + "files/siap/siep/scadenzario/DettaglioScadenzarioVaneRicerche.jsp";
}