package siap.siep.archiviazione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiArchiviazione</p>
* <p>Description: Classe di costanti di Archiviazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiArchiviazione
{
		 public static final String CAMPO_ID_ARCHIVIAZIONE = "IdArchiviazione";
		 public static final String CAMPO_COD_TIPO_PROVVEDIMENTO = "CodTipoProvvedimento";
		 public static final String CAMPO_GIORNO_DATA_EMISSIONE = "GiornoDataEmissione";
		 public static final String CAMPO_MESE_DATA_EMISSIONE = "MeseDataEmissione";
		 public static final String CAMPO_ANNO_DATA_EMISSIONE = "AnnoDataEmissione";
		 public static final String CAMPO_GIORNO_DATA_RICEZIONE = "GiornoDataRicezione";
		 public static final String CAMPO_MESE_DATA_RICEZIONE = "MeseDataRicezione";
		 public static final String CAMPO_ANNO_DATA_RICEZIONE = "AnnoDataRicezione";
		 public static final String CAMPO_ANNO_NOTA = "AnnoNota";
		 public static final String CAMPO_NUM_NOTA = "NumNota";
		 public static final String CAMPO_COD_PROVVEDIMENTO = "CodProvvedimento";
		 public static final String CAMPO_ANNO_PROVVEDIMENTO = "AnnoProvvedimento";
		 public static final String CAMPO_NUM_PROVVEDIMENTO = "NumProvvedimento";
		 public static final String CAMPO_COD_TIPO_PROVVEDIMENTO_ARC = "CodTipoProvvedimentoArc";
		 public static final String CAMPO_GIORNO_DATA_DEFINIZIONE = "GiornoDataDefinizione";
		 public static final String CAMPO_MESE_DATA_DEFINIZIONE = "MeseDataDefinizione";
		 public static final String CAMPO_ANNO_DATA_DEFINIZIONE = "AnnoDataDefinizione";
		 public static final String CAMPO_COD_OGGETTO_DEFINIZIONE = "CodOggettoDefinizione";
		 public static final String CAMPO_COD_TIPO_EMITTENTE = "CodTipoEmittente";
		 public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE = "CodTipoAutoritaEmittente";
		 public static final String CAMPO_COD_LUOGO_EMITTENTE = "CodLuogoEmittente";
		 public static final String CAMPO_INDIRIZZO_EMITTENTE = "IndirizzoEmittente";
		 public static final String CAMPO_ALTRA_AUTORITA = "AltraAutorita";
		 public static final String CAMPO_NOTE = "NoteArc";
		 public static final String CAMPO_FLAG_ANNULLAMENTO = "FlagAnnullamento";
		 public static final String CAMPO_GIORNO_DATA_ANNULLAMENTO = "GiornoDataAnnullamento";
		 public static final String CAMPO_MESE_DATA_ANNULLAMENTO = "MeseDataAnnullamento";
		 public static final String CAMPO_ANNO_DATA_ANNULLAMENTO = "AnnoDataAnnullamento";
		 public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
		 public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
		 public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
		 public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
		 public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
		 public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
		 public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
		 public static final String CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE = "IstDetIdIstitutoDetenzione";
		 public static final String CAMPO_CSS_ID_CSSA = "CssIdCssa";
	
	// MEV_2 Step2 : Archiviazione Provvedimento Giudice/Cassazione
		 public static final String CAMPO_DESC_TIPO_PROVVEDIMENTO_ARC = "DescTipoProvvedimentoArc";
		 public static final String CAMPO_DESC_TIPO_AUTORITA_EMITTENTE = "DescTipoAutoritaEmittente";

     // GRIGLIA BOTTONI
     public static final String PG_GRIGLIA_DEFINIZIONE_PROCEDIMENTO = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/GrigliaBottoniDefinizioneProcedimento.jsp";

     // NON LUOGO A PROVVEDERE
     public static final String PG_LOAD_INSERISCI_NON_LUOGO_A_PROVVEDERE = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadInserisciNonLuogoAProvvedere.jsp";
     public static final String PG_LOAD_DETTAGLIO_NON_LUOGO_A_PROVVEDERE = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadDettaglioNonLuogoAProvvedere.jsp";

     // PENA ESPIATA
     public static final String PG_LOAD_INSERISCI_PENA_ESPIATA = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadInserisciPenaEspiata.jsp";
     public static final String PG_LOAD_DETTAGLIO_PENA_ESPIATA = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadDettaglioPenaEspiata.jsp";

     // PROVVEDIMENTO ALTRA AUTORITA'
     public static final String PG_LOAD_INSERISCI_PROVV_ALTRA_AUT = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadInserisciProvvAltraAutorita.jsp";
     public static final String PG_LOAD_DETTAGLIO_PROVV_ALTRA_AUT = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadDettaglioProvvAltraAutorita.jsp";

     // PROVVEDIMENTO SORVEGLIANZA'
     public static final String PG_LOAD_INSERISCI_PROVV_SORV = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadInserisciProvvConvPenPec.jsp";
     public static final String PG_LOAD_DETTAGLIO_PROVV_SORV = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadDettaglioProvvConvPenPec.jsp";
     
     // ASSORBIMENTO IN CUMULO
     public static final String PG_LOAD_INSERISCI_ASSOR_CUMULO = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadInserisciAssorCumulo.jsp";
     public static final String PG_LOAD_DETTAGLIO_ASSOR_CUMULO = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadDettaglioAssorCumulo.jsp";
     public static final String PG_LOAD_DETTAGLIO_ASSOR_CUMULO_ALTRO = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadDettaglioAssorCumuloAltro.jsp";

     // ANNOTAZIONE PROVVEDIMENTO DI CUMULO DOPO ARCHIVIAZIONE
     public static final String PG_LOAD_INSERISCI_ANN_PROV_CUMULO = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadInserisciAnnProvCumulo.jsp";
     public static final String PG_LOAD_DETTAGLIO_ANN_PROV_CUMULO = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadDettaglioAnnProvCumulo.jsp";

     // VISTO PM
     public static final String PG_LOAD_INSERISCI_VISTO_PM = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadInserisciVistoPm.jsp";
     public static final String PG_LOAD_DETTAGLIO_VISTO_PM = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadDettaglioVistoPm.jsp";

     // ATTESA ARCHIVIAZIONE
     public static final String PG_LOAD_INSERISCI_ATTESA_ARCHIVIAZIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadInserisciAttesaArchiviazione.jsp";
     public static final String PG_LOAD_DETTAGLIO_ATTESA_ARCHIVIAZIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadDettaglioAttesaArchiviazione.jsp";
     
     public static final String PG_LOAD_LISTA = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/ListaDocumentiArchiviazione.jsp";

     // ARCHIVIAZIONE SEMPLIFICARA RES
     public static final String PG_LOAD_INS_ARCH_SEMPL_RES = IWebConstants.ROOT_DIR + "/files/siap/siep/archiviazione/LoadInsArchSemplRES.jsp";
     
     
}