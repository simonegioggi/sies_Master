package siap.siepe.richiesta.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiRichiesta</p>
* <p>Description: Classe di costanti di Richiesta</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public interface ICostantiRichiesta
{
  // ? ******** ? Verificare se serve metterle qui oppure riusarle dalla
  // intefaccia ICostantiFascicoloSiepe//
  // Campi del fascicolo siepe
  public static final String CAMPO_CHIAVE_ANNO_SIEPE = "ChiaveAnno";
  public static final String CAMPO_CHIAVE_PROGR_SIEPE = "ChiaveProgr";
  // Campi del fascicolo UEPE
  public static final String CAMPO_NUM_UEPE = "NumUepe";
  public static final String CAMPO_ANNO_UEPE = "AnnoUepe";
  public static final String CAMPO_PROGR_UEPE = "ProgrUepe";
  // ? ******* ? //
  //
  public static final String CAMPO_SEDE = "Sede";

  public static final String CAMPO_ID_RICHIESTA = "IdRichiesta";
  public static final String CAMPO_GIORNO_DATA_RICHIESTA = "GiornoDataRichiesta";
  public static final String CAMPO_MESE_DATA_RICHIESTA = "MeseDataRichiesta";
  public static final String CAMPO_ANNO_DATA_RICHIESTA = "AnnoDataRichiesta";
  public static final String CAMPO_COD_TIPO_RICHIESTA = "CodTipoRichiesta";
  public static final String CAMPO_COD_TIPO_RICHIEDENTE = "CodTipoRichiedente";
  public static final String CAMPO_NOTE = "Note";
  public static final String CAMPO_DOC_BLOB = "DocBlob";

  public static final String CAMPO_COD_OPERATORE_INSERIMENTO ="CodOperatoreInserimento";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO ="GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO ="MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO ="AnnoDataInserimento";
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO ="CodUfficioInserimento";

  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO ="CodOperatoreAggiornamento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO ="GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO ="MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO ="AnnoDataAggiornamento";
  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO ="CodUfficioAggiornamento";

  public static final String CAMPO_FAS_SIE_ID_FAS_SIEPE = "FasSieIdFasSiepe";
  public static final String CAMPO_DOCUMENTO_REGISTRATO = "FlagDocumentoRegistrato";
  public static final String CAMPO_COD_UFFICIO_DESTINATARIO ="CodUfficioDestinatario";
  public static final String CAMPO_COD_TIPO_UFFICIO_DESTINATARIO = "CodTipoUfficioDestinatario";

  // Path delle pagine JSP
  public static final String PG_LOAD_RICERCARICHIESTA = IWebConstants.ROOT_DIR +"files/siap/siepe/richiesta/LoadRicercaRichiesta.jsp";
  public static final String PG_LOAD_DETTAGLIORICHIESTA = IWebConstants.ROOT_DIR + "files/siap/siepe/richiesta/DettaglioRichiesta.jsp";
  public static final String PG_LOAD_TRASFERISCIRICHIESTA	= IWebConstants.ROOT_DIR + "files/siap/siepe/richiesta/TrasferisciRichiesta.jsp";
  public static final String PG_RICERCARICHIESTA = IWebConstants.ROOT_DIR +"files/siap/siepe/richiesta/RicercaRichiesta.jsp";
  public static final String PG_LOAD_INSERISCIRICHIESTA = IWebConstants.ROOT_DIR + "files/siap/siepe/richiesta/LoadInserisciRichiesta.jsp";
  public static final String PG_ELENCORICHIESTE = IWebConstants.ROOT_DIR +"files/siap/siepe/richiesta/ElencoRichieste.jsp";
}
