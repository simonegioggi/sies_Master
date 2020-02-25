package siap.siep.modulocumulo.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiContinuazioneCumulo</p>
* <p>Description: Classe di costanti di Continuazione</p>
* <p>   in ambito Cumulo (Pena_complessiva_/SanzSost /Continuaz Cumulo) </p>
*
* @version 1.0
*/

public interface ICostantiContinuazioneCumulo
{
     public static final String CAMPO_ID_CONTINUAZIONE_CUM = "IdContinuazioneCum"; 
     public static final String CAMPO_PROGR_CONTINUAZIONE_CUM = "ProgrContinuazioneCum"; 
     public static final String CAMPO_COD_TIPO_CONTINUAZIONE = "CodTipoContinuazione"; 
     public static final String CAMPO_COD_TIPO_AUTORITA = "CodTipoAutorita"; 
     public static final String CAMPO_COD_LUOGO_AUTORITA = "CodLuogoAutorita";
     
     public static final String CAMPO_GIORNO_DATA_SENTENZA = "GiornoDataSentenza"; 
     public static final String CAMPO_MESE_DATA_SENTENZA = "MeseDataSentenza"; 
     public static final String CAMPO_ANNO_DATA_SENTENZA = "AnnoDataSentenza"; 
     public static final String CAMPO_ANNO_SENTENZA = "AnnoSentenza"; 
     public static final String CAMPO_NUM_SENTENZA = "NumSentenza"; 

     public static final String CAMPO_ANNO_REGE_PM = "AnnoRegePm"; 
     public static final String CAMPO_NUM_REGE_PM = "NumRegePm"; 

     public static final String CAMPO_ANNO_REG_GEN    = "AnnoRegGen"; 
     public static final String CAMPO_NUMERO_REG_GEN  = "NumeroRegGen"; 
     public static final String CAMPO_TIPO_REG_GEN    = "TipoRegGen"; 
     
     public static final String CAMPO_PEN_COM_ID_PENA_COMPLESSIVA = "PenComIdPenaComplessiva";
     public static final String CAMPO_TIT_ID_TITOLO_CUMULATO_CONT = "TitIdTitoloCumulatoCont";     
     
     public static final String CAMPO_FLAG_STATO_CON_CUM     = "FlagStatoCon"; 
     public static final String CAMPO_MOTIVO_INS_MOD         = "MotivoInsMod"; 
     public static final String CAMPO_TIT_ID_TITOLO_CUMULATO = "TitIdTitoloCumulato"; 

     //
     public static final String PG_LOAD_RICERCACONTINUAZIONE  = IWebConstants.ROOT_DIR + "files/siap/siep/continuazione/LoadRicercaContinuazione.jsp";
     public static final String PG_LOAD_DETTAGLIOCONTINUAZIONE  = IWebConstants.ROOT_DIR + "files/siap/siep/continuazione/LoadRicercaContinuazione.jsp";
     public static final String PG_RICERCACONTINUAZIONE = IWebConstants.ROOT_DIR + "files/siap/siep/continuazione/RicercaContinuazione.jsp";
     public static final String PG_LOAD_INSERISCICONTINUAZIONE  = IWebConstants.ROOT_DIR + "files/siap/siep/continuazione/LoadInserisciContinuazione.jsp";
}