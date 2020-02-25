package siap.sige.titoloesecutivo.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiTitoloEsecutivo</p>
* <p>Description: Classe di costanti di TitoloEsecutivo</p>
* <p>Copyright: Copyright (c)</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public interface ICostantiTitoloEsecutivo
{
  public static final String CAMPO_ID_FASCICOLO_SIEP = "IdFascicoloSiep";
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
  public static final String CAMPO_ANNO_FASCICOLO_SIEP = "AnnoFascicoloSiep";
  public static final String CAMPO_PROGR_FASCICOLO_SIEP = "ProgrFascicoloSiep";
  public static final String CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN = "ProgrFascicoloSiepOrigin";
  public static final String CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP = "CodTipoUffFascicoloSiep";
  public static final String CAMPO_SEDE_UFF_FASCICOLO_SIEP = "SedeUffFascicoloSiep";
  public static final String CAMPO_SEDE_UFF_ACCORPATO = "SedeUffAccorpato";
  public static final String CAMPO_COD_TIPO_PROVVEDIMENTO = "CodTipoProvvedimento";
  public static final String CAMPO_GIORNO_DATA_PROVVEDIMENTO = "GiornoDataProvvedimento";
  public static final String CAMPO_MESE_DATA_PROVVEDIMENTO = "MeseDataProvvedimento";
  public static final String CAMPO_ANNO_DATA_PROVVEDIMENTO = "AnnoDataProvvedimento";
  public static final String CAMPO_ANNO_PROVVEDIMENTO = "AnnoProvvedimento";
  public static final String CAMPO_NUMERO_PROVVEDIMENTO = "NumeroProvvedimento";
  public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE = "CodTipoAutoritaEmittente";
  public static final String CAMPO_COD_LUOGO_EMITTENTE = "CodLuogoEmittente";
  public static final String CAMPO_DESCR_LUOGO_EMITTENTE = "DescrLuogoEmittente";
  public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA = "GiornoDataIrrevocabilita";
  public static final String CAMPO_MESE_DATA_IRREVOCABILITA = "MeseDataIrrevocabilita";
  public static final String CAMPO_ANNO_DATA_IRREVOCABILITA = "AnnoDataIrrevocabilita";
  public static final String CAMPO_FAS_SIGE_ID_FASCICOLO_SIGE = "FasSigeIdFascicoloSige";
  public static final String CAMPO_NOTE = "Note";
  public static final String ACTION_DOPO_CANCELLAZIONE = "ActDopoCanc";
  public static final String CHECK_DEASSEGNA_TITOLO_ESECUTIVO = "ChkDeassegnaTitoloEsecutivo";

  public static final String PG_LOAD_ASSEGNA_TITOLOESECUTIVO	= IWebConstants.ROOT_DIR + "files/siap/sige/titoloesecutivo/LoadAssegnaTitoloEsecutivo.jsp";
  public static final String PG_ASSEGNA_TITOLOESECUTIVO	= IWebConstants.ROOT_DIR + "files/siap/sige/titoloesecutivo/AssegnaTitoloEsecutivo.jsp";
  public static final String PG_LOAD_DEASSEGNA_TITOLOESECUTIVO	= IWebConstants.ROOT_DIR + "files/siap/sius/titoloesecutivo/LoadDeassegnaTitoloEsecutivo.jsp";
  public static final String PG_LOAD_DEASSEGNA_TITOLOESECUTIVO_SIGE	= IWebConstants.ROOT_DIR + "files/siap/sige/titoloesecutivo/LoadDeassegnaTitoloEsecutivo.jsp";
  public static final String PG_DEASSEGNA_TITOLOESECUTIVO	= IWebConstants.ROOT_DIR + "files/siap/sius/titoloesecutivo/DeassegnaTitoloEsecutivo.jsp";
  public static final String PG_LOAD_DETTAGLIORIFASIEP	= IWebConstants.ROOT_DIR + "files/siap/sius/rifasiep/DettaglioRifFascicoloSiep.jsp";
  public static final String PG_ELENCO_RIFERIMENTI	= IWebConstants.ROOT_DIR + "files/siap/sius/rifasiep/ElencoRifFascicoloSiep.jsp";
}