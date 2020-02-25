package siap.siep.provvedimentopm.action;

/**
* <p>Title: ICostantiProvvedimento</p>
* <p>Description: Classe di costanti di Provvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.siep.web.ISIEPCostantiWeb;
import f3b.web.IWebConstants;

public interface ICostantiProvvedimento extends ISIEPCostantiWeb
{
    public static final String CAMPO_ID_PROVVEDIMENTO = "IdProvvedimento";
    public static final String CAMPO_COD_TIPO = "CodTipo";
    public static final String CAMPO_COD_MOTIVO = "CodMotivo";
    public static final String CAMPO_GIORNO_DATA = "GiornoData";
    public static final String CAMPO_MESE_DATA = "MeseData";
    public static final String CAMPO_ANNO_DATA = "AnnoData";
    public static final String CAMPO_COD_ESITO = "CodEsito";
    public static final String CAMPO_FLAG_PIU_MENO = "FlagPiuMeno";
    public static final String CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI = "GiornoDataTrasmissioneAtti";
    public static final String CAMPO_MESE_DATA_TRASMISSIONE_ATTI = "MeseDataTrasmissioneAtti";
    public static final String CAMPO_ANNO_DATA_TRASMISSIONE_ATTI = "AnnoDataTrasmissioneAtti";
    public static final String CAMPO_GIORNO_DATA_SCADENZA = "GiornoDataScadenza";
    public static final String CAMPO_MESE_DATA_SCADENZA = "MeseDataScadenza";
    public static final String CAMPO_ANNO_DATA_SCADENZA = "AnnoDataScadenza";
    public static final String CAMPO_ANNO_PROTOCOLLO = "AnnoProtocollo";
    public static final String CAMPO_PROGR_PROTOCOLLO = "ProgrProtocollo";
    public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
    public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
    public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
    public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
    public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
    public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
    public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
    public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
    public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
    public static final String CAMPO_MAG_COD_MAGISTRATO = "MagCodMagistrato";

    public static final String AUTORITA_DESTINATARIO      = "AutoritaDestinatario";
    public static final String AUTORITA_SEDE              = "AutoritaSede";

    public static final String CAMPO_NOTE = "Note";

    // Stub:20030219 verificare se è preferibile ereditarle dalla interfaccia
    // ISIEPCostantiWeb.
    public static final String CAMPO_DATA_GG_EMISSIONE    = "DataGGEmissione";
    public static final String CAMPO_DATA_MM_EMISSIONE    = "DataMMEmissione";
    public static final String CAMPO_DATA_AAAA_EMISSIONE  = "DataAAAAEmissione";

    public static final String PG_LOAD_RICERCAPROVVEDIMENTO	  = IWebConstants.ROOT_DIR + "files/siap/siep/provvedimentopm/LoadRicercaProvvedimento.jsp";
    public static final String PG_RICERCAPROVVEDIMENTO	      = IWebConstants.ROOT_DIR + "files/siap/siep/provvedimentopm/RicercaProvvedimento.jsp";
    public static final String PG_LOAD_INSERISCIPROVVEDIMENTO	= IWebConstants.ROOT_DIR + "files/siap/siep/provvedimentopm/LoadInserisciProvvedimento.jsp";
    public static final String PG_LOADDETTAGLIOCOMPFOGLIOCOMPNSC = IWebConstants.ROOT_DIR + "files/siap/siep/fogliocomplementare/LoadDettaglioCompFoglioComp.jsp";
   
}