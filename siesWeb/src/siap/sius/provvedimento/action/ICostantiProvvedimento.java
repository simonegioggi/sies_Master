package siap.sius.provvedimento.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiUdienza</p>
* <p>Description: Classe di costanti di Provvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiProvvedimento
{
  public static final String CAMPO_GIORNO_DATA_EMISSIONE    = "GiornoDataEmissione";
  public static final String CAMPO_MESE_DATA_EMISSIONE      = "MeseDataEmissione";
  public static final String CAMPO_ANNO_DATA_EMISSIONE      = "AnnoDataEmissione";
  public static final String CAMPO_GIORNO_DATA_TRASMISSIONE    = "GiornoDataTrasmissione";
  public static final String CAMPO_MESE_DATA_TRASMISSIONE      = "MeseDataTrasmissione";
  public static final String CAMPO_ANNO_DATA_TRASMISSIONE      = "AnnoDataTrasmissione";
  public static final String CAMPO_TIPO_PROVVEDIMENTO      = "CodTipoProvvedimento";

  public static final String PG_LOADDETTAGLIOCOMPFOGLIOCOMP = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/LoadDettaglioCompFoglioComp.jsp";
  public static final String PG_LOADINSERISCICOMPFOGLIOCOMP = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/LoadInserisciCompFoglioComp.jsp";
  public static final String PG_ELENCOPROVVEDIMENTICFC = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/ElencoProvvedimentiCFC.jsp";
  public static final String PG_ELENCOPROVVEDIMENTI = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/ElencoProvvedimenti.jsp";
  public static final String PG_ELENCOALTRIPROVVEDIMENTI = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/ElencoAltriProvvedimenti.jsp";
  public static final String PG_BUTTONS_RICERCA  = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/buttonsProvvedimento.jsp";
  public static final String PG_NOTIFICHE_EVENTO  = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/NotificheEvento.jsp";
  public static final String PG_BUTTONS_NOTIFICHE  = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/buttonsNotifiche.jsp";
  public static final String PG_BUTTONS_ALTRI  = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/buttonsAltriProvvedimenti.jsp";
  public static final String PG_BUTTONS_DETTAGLIO_PROV  = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/buttonsDettaglioProvvedimento.jsp";
  public static final String PG_BUTTONS_CFC  = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/buttonsCFC.jsp";
  public static final String PG_LISTA_CFC_ANNULLATI	    = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/ListaDocAllNulli.jsp";
  public static final String PG_LISTA_PROVVEDIMENTI_POPUP	    = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/ListaProvvedimenti.jsp";

  public static final String COD_EVENTO_PROVVEDIMENTO = "01";
  public static final String COD_ORDINANZA = "03";
  public static final String COD_DECRETO = "02";
  public static final String NOTIFICA_ESEGUITA = "01";
  public static final String MOTIVO_FISSAZIONE_UDIENZA = "0601";
  public static final String SCADENZARIO_IRREVOCABILITA = "50";
  public static final String FASCICOLO_ARCHIVIATO = "01";
  public static final String CHK_CAMBIO_CASELLARIO = "CheckCasellario";
  public static final String NUOVO_CASELLARIO = "NuovoCasellario";
  public static final String CAMPO_COD_MOTIVO_NON_INVIO = "CodMotivoNonInvio";
  public static final String CAMPO_DESCR_MOTIVO_NON_INVIO = "DescrMotivoNonInvio";
  public static final String CAMPO_GIORNO_DATA_INS_MANUALE = "GiornoDataInsManuale";
  public static final String CAMPO_MESE_DATA_INS_MANUALE = "MeseDataInsManuale";
  public static final String CAMPO_ANNO_DATA_INS_MANUALE = "AnnoDataInsManuale";
  public static final String COD_SENTENZA = "01";

}