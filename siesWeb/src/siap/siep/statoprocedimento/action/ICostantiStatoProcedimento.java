package siap.siep.statoprocedimento.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiStatoProcedimento</p>
* <p>Description: Classe di costanti di StatoProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiStatoProcedimento
{
     public static final String CAMPO_PROGRESSIVO = "Progressivo";
     public static final String CAMPO_COD_STATO_PROCEDIMENTO = "CodStatoProcedimento";
     public static final String CAMPO_GIORNO_DATA = "GiornoData";
     public static final String CAMPO_MESE_DATA = "MeseData";
     public static final String CAMPO_ANNO_DATA = "AnnoData";
     public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
     public static final String CAMPO_DATA_INSERIMENTO = "DataInserimento";
     public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficio";
     public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
     public static final String CAMPO_EVE_ID_EVENTO = "EventoIdEvento";

     public static final String PG_LOAD_STATOPROCEDIMENTO	= IWebConstants.ROOT_DIR + "files/siap/siep/statoprocedimento/LoadInserisciStatoProcedimento.jsp";
     public static final String PG_LOAD_DETTAGLIO_STATOPROCEDIMENTO	= IWebConstants.ROOT_DIR + "files/siap/siep/statoprocedimento/LoadDettaglioStatoProcedimento.jsp";
}
