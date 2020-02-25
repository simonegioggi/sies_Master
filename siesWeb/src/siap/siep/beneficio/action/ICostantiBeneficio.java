package siap.siep.beneficio.action;

import f3b.web.IWebConstants;

/**
 * <p>Title: ICostantiBeneficio</p>
 * <p>Description: Classe di costanti di Beneficio</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public interface ICostantiBeneficio
{
  public static final String CAMPO_ID_BENEFICIO = "IdBeneficio";
  public static final String CAMPO_COD_NATURA_BENEFICIO = "CodNaturaBeneficio";
  public static final String CAMPO_COD_TIPO_BENEFICIO = "CodTipoBeneficio";
  public static final String CAMPO_COD_TIPO_SOSP_SUBORDINATA = "CodTipoSospSubordinata";
  public static final String CAMPO_FLAG_SOSP_COND = "FlagSospCond";
  public static final String CAMPO_NUM_ANNI_RECLUSIONE = "NumAnniReclusione";
  public static final String CAMPO_NUM_MESI_RECLUSIONE = "NumMesiReclusione";
  public static final String CAMPO_NUM_GIORNI_RECLUSIONE = "NumGiorniReclusione";
  public static final String CAMPO_IMPORTO_MULTA = "ImportoMulta";
  public static final String CAMPO_NUM_ANNI_ARRESTO = "NumAnniArresto";
  public static final String CAMPO_NUM_MESI_ARRESTO = "NumMesiArresto";
  public static final String CAMPO_NUM_GIORNI_ARRESTO = "NumGiorniArresto";
  public static final String CAMPO_IMPORTO_AMMENDA = "Importoammenda";
  public static final String CAMPO_COD_DPR = "CodDpr";
  public static final String CAMPO_NOTE = "Note";
  public static final String CAMPO_INAPPLICABILITA_MIS_SICUREZZA = "InapplicabilitaMisSicurezza";
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
  public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
  
  public static final String CAMPO_COD_SOTTOTIPO_BENEFICIO = "CodSottotipoBeneficio";
  public static final String CAMPO_NUM_ANNI_SOSPENSIONE = "NumAnniSospensione";
  public static final String CAMPO_NUM_MESI_PRESTAZIONE = "NumMesiPrestazione";
  public static final String CAMPO_NUM_GIORNI_PRESTAZIONE = "NumGiorniPrestazione";
  public static final String CAMPO_NUM_ORE_SETTIMANALI = "NumOreSettimanali";
  public static final String CAMPO_FLAG_FREQUENZA_SETTIMANALE = "FlagFrequenzaSettimanale";
  public static final String CAMPO_SEN_ID_SENTENZA = "SenIdSentenza";
  public static final String CAMPO_COD_TIPO_PROVVEDIMENTO = "CodTipoProvvedimento";
  public static final String CAMPO_DATA_PROVVEDIMENTO = "DataProvvedimento";
  public static final String CAMPO_GIORNO_PROVVEDIMENTO = "GiornoProvvedimento";
  public static final String CAMPO_MESE_PROVVEDIMENTO = "MeseProvvedimento";
  public static final String CAMPO_ANNO_PROVVEDIMENTO = "AnnoProvvedimento";
  public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE = "CodTipoAutoritaEmittente";
  public static final String CAMPO_COD_LUOGO_EMITTENTE = "CodLuogoEmittente";
  public static final String CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE= "NumSezioneAutoritaEmittente";
  public static final String CAMPO_ANNO_SENTENZA = "AnnoSentenza";
  public static final String CAMPO_NUMERO_SENTENZA= "NumetoSentenza"; 
  public static final String CAMPO_BEN_ID_BENEFICIO = "BenIdBeneficio";
  public static final String CAMPO_NUM_ANNI_ADEMPIMENTO   = "NumAnniAdempimento";
  public static final String CAMPO_NUM_MESI_ADEMPIMENTO   = "NumMesiAdempimento";
  public static final String CAMPO_NUM_GIORNI_ADEMPIMENTO = "NumGiorniAdempimento";
  public static final String CAMPO_FLAG_NON_MENZIONE = "FlagNonMenzione";
  
  public static final String CAMPO_DATA_IRREVOCABILITA = "DataIrrevocabilita";
  public static final String CAMPO_GIORNO_IRREVOCABILITA = "GiornoIrrevocabilita";
  public static final String CAMPO_MESE_IRREVOCABILITA = "MeseIrrevocabilita";
  public static final String CAMPO_ANNO_IRREVOCABILITA = "AnnoIrrevocabilita";

  public static final String CAMPO_PROVENIENZA = "Provenienza";

  public static final String PG_LOAD_RICERCABENEFICIO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/LoadRicercaBeneficio.jsp";
  public static final String PG_LOAD_DETTAGLIOBENEFICIO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/DettaglioBeneficio.jsp";
  public static final String PG_RICERCABENEFICIO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/RicercaBeneficio.jsp";
  public static final String PG_LOAD_INSERISCIBENEFICIO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/LoadInserisciBeneficio.jsp";
  public static final String PG_GRIGLIA_REVOCA_BENEFICIO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/LoadGrigliaBottoniRevocaBeneficio.jsp";
  public static final String PG_LOAD_INSERISCI_REVOCA_SOSPCOND	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/LoadInsRevocaSospCondAP.jsp";
  public static final String PG_LOAD_MODIFICA_REVOCA_SOSPCOND	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/LoadModificaRevocaSospCondAP.jsp";
  public static final String PG_LISTA_PROCEDIMENTI_ASSOCIATI	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/ListaProcAssociati.jsp";
  public static final String PG_RICERCA_REVOCA_SOSPCOND	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/RiepilogaRevocaSospCondAP.jsp";
  public static final String PG_LOAD_DETTAGLIO_REVOCA_SOSPCOND	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/DettaglioRevocaSospCondAP.jsp";
  
  public static final String PG_LOAD_GRIGLIA_ISCRIZIONE_BENEFICIO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/LoadGrigliaIscrizioneBenefici.jsp";
  
  public static final String PG_LOAD_INSERISCI_BENEFICIO_INDULTO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/LoadInserisciBeneficioIndulto.jsp";
  public static final String PG_LOAD_DETTAGLIO_BENEFICIO_INDULTO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/DettaglioBeneficioIndulto.jsp";
  
  
  
  public static final String PG_GRIGLIA_ELENCO_REVOCA_BENEFICIO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/LoadGrigliaElencoRevocaBeneficio.jsp";

  public static final String PG_LOAD_INSERISCI_REVOCA_INDULTO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/LoadInsRevocaIndultoAP.jsp";
  public static final String PG_RICERCA_REVOCA_INDULTO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/RiepilogaRevocaIndultoAP.jsp";
  public static final String PG_LISTA_PROCEDIMENTI_ASSOCIATI_INDU	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/ListaProcAssociatiIndu.jsp";
  public static final String PG_LOAD_DETTAGLIO_REVOCA_INDULTO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/DettaglioRevocaIndultoAP.jsp";
  public static final String PG_LOAD_MODIFICA_REVOCA_INDULTO	= IWebConstants.ROOT_DIR + "files/siap/siep/beneficio/LoadModificaRevocaIndultoAP.jsp";
}