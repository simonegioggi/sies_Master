package siap.siep.cumulo.action;

import siap.sico.evento.action.ICostantiEvento;
import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiCumulo</p>
* <p>Description: Classe di costanti di Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiCumulo
{
     public static final String CAMPO_ID_CUMULO = "IdCumulo";
     public static final String CAMPO_ID_FASCICOLO_SIEP_CUMULATO = "IdFascicoloSiepCumulato";
     public static final String CAMPO_CHIAVE_ANNO_FAS_CUMULATO = "ChiaveAnnoFasCumulato";
     public static final String CAMPO_CHIAVE_PROGR_FAS_CUMULATO = "ChiaveProgrFasCumulato";
     public static final String CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO = "CodTipoUfficioFasCumulato";
     public static final String CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO = "CodLuogoUfficioFasCumulato";
     public static final String CAMPO_COD_UFFICIO_FAS_CUMULATO = "CodUfficioFasCumulato";
     public static final String CAMPO_COD_TIPO_CUMULO = "CodTipoCumulo";
     public static final String CAMPO_GIORNO_DATA_RICHIESTA_FASCICOLO = "GiornoDataRichiestaFascicolo";
     public static final String CAMPO_MESE_DATA_RICHIESTA_FASCICOLO = "MeseDataRichiestaFascicolo";
     public static final String CAMPO_ANNO_DATA_RICHIESTA_FASCICOLO = "AnnoDataRichiestaFascicolo";
     public static final String CAMPO_GIORNO_DATA_PERVENIMENTO_FASCICOLO = "GiornoDataPervenimentoFascicolo";
     public static final String CAMPO_MESE_DATA_PERVENIMENTO_FASCICOLO = "MeseDataPervenimentoFascicolo";
     public static final String CAMPO_ANNO_DATA_PERVENIMENTO_FASCICOLO = "AnnoDataPervenimentoFascicolo";
     public static final String CAMPO_GIORNO_DATA_CUMULO = "GiornoDataCumulo";
     public static final String CAMPO_MESE_DATA_CUMULO = "MeseDataCumulo";
     public static final String CAMPO_ANNO_DATA_CUMULO = "AnnoDataCumulo";
     public static final String CAMPO_COD_MOTIVO_SOSPENSIONE_CUMULO = "CodMotivoSospensioneCumulo";
     public static final String CAMPO_GIORNO_DATA_SOSPENSIONE_CUMULO = "GiornoDataSospensioneCumulo";
     public static final String CAMPO_MESE_DATA_SOSPENSIONE_CUMULO = "MeseDataSospensioneCumulo";
     public static final String CAMPO_ANNO_DATA_SOSPENSIONE_CUMULO = "AnnoDataSospensioneCumulo";

     public static final String CAMPO_NOTE = "Note";
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
     public static final String CAMPO_FLAG_TIPO_STAMPA = "FlagTipoStampa";
     public static final String CAMPO_SEN_ID_SENTENZA = "SenIdSentenza";
     public static final String CAMPO_FLAG_VALIDATO = "FlagValidato";

     public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
     public static final String CAMPO_PRIMO_CUMULO = "PrimoCumulo";

     public static final String CAMPO_AZIONE_CHIAMANTE = "AzioneChiamante";

     //=========================================================================
     //
     //=========================================================================
     public static final String PG_LOAD_RICERCACUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/LoadRicercaCumulo.jsp";
     public static final String PG_LOAD_DETTAGLIOCUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/LoadRicercaCumulo.jsp";
     public static final String PG_RICERCACUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/RicercaCumulo.jsp";
     public static final String PG_DETTAGLIOPENACUMULO	= IWebConstants.ROOT_DIR +"files/siap/siep/cumulo/DettaglioPenaCumulo.jsp";
     public static final String PG_LOAD_INSERISCICUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/LoadInserisciCumulo.jsp";
     public static final String PG_LOAD_CARICAFASCICOLI	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/LoadFascicoliCumulo.jsp";
     public static final String PG_LISTAFASCICOLICUMULATI	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/ListaFascicoliCumulati.jsp";
     public static final String PG_LOAD_INSERIMENTO_PENA_COMPLESSIVA_CUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/LoadInserisciPenaComplessivaCumulo.jsp";
     public static final String REDIRECT_CAMBIO_POS_GIURIDICA = IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/ConfermaCambioPosizioneGiuridica.jsp";
     public static final String REDIRECT_CALCOLO_PENA = IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/ConfermaCalcoloPena.jsp";

     public static final String PG_LOAD_INSERIMENTO_STAMPA_CUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/LoadInserisciStampaCumulo.jsp";
     public static final String PG_LOAD_DETTALIO_STAMPA_CUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/DettaglioStampaCumulo.jsp";

     public static final String PG_LOAD_INSERIMENTO_ULTERIORI_SANZIONI	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/LoadInserisciUlterioriSanzioni.jsp";
     public static final String PG_LOAD_DETTAGLIO_ULTERIORI_SANZIONI	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/LoadDettaglioUlterioriSanzioni.jsp";

     public static final String PG_LOAD_INSERIMENTO_MISURA_SICUREZZA_PENA_ACCESSORIA	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/LoadCumuloCampoMisuraSicurezzaPenaAccessoria.jsp";

     public static final String PG_LOAD_RICHIESTA_APPLICAZIONE_BENEFICI = IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/LoadRichiestaApplicazioneBenefici.jsp";
     public static final String PG_LOAD_INSERISCIPENAACCESSORIA	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/LoadInserisciPenaAccessoria.jsp";
 
     public static final String PG_LOAD_DETTAGLIO_APPLICAZIONE_BENEFICI	= IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/DettaglioApplicazioneBenefici.jsp";

     public static final String REDIRECT_DETTAGLIO_CUMULO = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
    		    "=siap.siep.cumulo.action.ActDettaglioCumuloStampa&" +
    		    ICostantiEvento.CAMPO_ID_EVENTO + "=";
     
     public static final String PG_LOAD_INSERISCIMISURASICUREZZA = IWebConstants.ROOT_DIR + "files/siap/siep/cumulo/LoadInserisciMisuraSicurezza.jsp";

}