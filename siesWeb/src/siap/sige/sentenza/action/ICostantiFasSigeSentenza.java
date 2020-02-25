package siap.sige.sentenza.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiFasSigeSentenza</p>
* <p>Description: Classe di costanti di FasSigeSentenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiFasSigeSentenza 
{
		 public static final String CAMPO_ID_FAS_SIGE_SENTENZA = "IdFasSisgeSentenza"; 
		 public static final String CAMPO_FAS_ID_FASCICOLO_SIGE = "FasIdFascicoloSige"; 
		 public static final String CAMPO_SEN_ID_SENTENZA = "SenIdSentenza"; 
		 public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento"; 
		 public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento"; 
		 public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento"; 
		 public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento"; 
		 public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento"; 
		 public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA = "GiornoDataIrrevocabilita"; 
		 public static final String CAMPO_MESE_DATA_IRREVOCABILITA = "MeseDataIrrevocabilita"; 
		 public static final String CAMPO_ANNO_DATA_IRREVOCABILITA = "AnnoDataIrrevocabilita"; 
		 public static final String CAMPO_FLAG_COMPETENZA = "FlagCompetenza"; 
		 public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep"; 
		 public static final String RADIO_TIPO_FUNZIONE = "TipoFunzione"; 
		 public static final String RADIO_TIPO_PROVVEDIMENTO = "TipoProvvedimento"; 
		 public static final String CAMPO_AMBITO_RICERCA = "CampoAmbitoRicerca"; 
		 
		 public static final String PG_LOAD_RICERCA_TITOLO	= IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/LoadRicercaTitoloEsecutivo.jsp";
		 public static final String PG_LOAD_ASSEGNA_SENTENZA	= IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/LoadAssegnaSentenza.jsp";
		 public static final String PG_INCLUDE_SENTENZA	= IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/IncSentenza.jsp";
		 public static final String PG_DETTAGLIO_ALTRO_TITOLO	= IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/DettaglioAltroTitolo.jsp";
		 public static final String PG_INCLUDE_RICERCA_SENTENZA	= IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/IncLoadRicercaSentenza.jsp";
		 public static final String PG_INCLUDE_RICERCA_FASCICOLO_SIEP	= IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/IncLoadRicercaFasSiep.jsp";
		 public static final String PG_LOAD_INSERISCISENTENZADECRETO = IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/LoadInserisciSentenzaDecreto.jsp";		 
		 public static final String PG_LOAD_INSERISCIALTRITITOLI = IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/LoadInserisciAltriTitoli.jsp";		 
		 public static final String CONTROLLI_SENTENZA = IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/ControlliSentenza.jsp";
		 public static final String JS_SENTENZA = IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/JsSentenza.js";
		 public static final String DIV_SENTENZA = IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/DivSentenza.jsp";		 
		 public static final String DIV_CUMULO = IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/DivCumulo.jsp";
		 public static final String DIV_ORDINANZA = IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/DivOrdinanza.jsp";
		 public static final String DIV_DECRETO_ARCHIVIAZIONE = IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/DivDecretoArchiviazione.jsp";		 
		 public static final String DIV_SENTENZA_STRANIERA = IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/DivSentenzaStraniera.jsp";
		 public static final String DIV_DECRETO = IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/DivDecreto.jsp";
		 public static final String PG_BUTTONS_MOD_CANC      = IWebConstants.ROOT_DIR  + "files/siap/sige/sentenza/BottoniModCancLink.jsp";
		 public static final String PG_BUTTONS_SENTENZA      = IWebConstants.ROOT_DIR  + "files/siap/sige/sentenza/buttonsSentenza.jsp";
		 public static final String PG_LOAD_MODIFICASENTENZA = IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/LoadModificaSentenza.jsp";
		 public static final String PG_RICERCA_FASSIGE_SENTENZE =  IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/RicercaSentenzaPerSige.jsp";
		
		 public static final String INC_FASCICOLI_SIGE 			= IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/IncFascicoliSige.jsp";
		 public static final String NUM_FASCICOLI_SIGE 			= "NumFasSige";
		 
		 public static final String PG_LISTA_ORDINANZE_NEL_DISTRETTO           =  IWebConstants.ROOT_DIR + "files/siap/sige/sentenza/ListaOrdinanzeNelDistretto.jsp";

}