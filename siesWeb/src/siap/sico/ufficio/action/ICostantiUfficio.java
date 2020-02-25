package siap.sico.ufficio.action;

import siap.util.ICostanti;


public interface ICostantiUfficio extends ICostanti
{
  public static final String CAMPO_SEDE_UFF_EMITTENTE = "SedeUffEmittente";
  public static final String CAMPO_COD_DISTRETTO = "campoCodDistretto";
  public static final String CAMPO_SEDE_UFFICIO = "SedeUfficio";
  public static final String CAMPO_TIPO_UFFICIO = "TipoUfficio";

  public static final String CAMPO_COD_UFFICIO = "CodUfficio";
  public static final String CAMPO_COD_TIPO_UFFICIO = "CodTipoUfficio";
  public static final String CAMPO_COD_PROVINCIA = "CodProvincia";
  public static final String CAMPO_COD_COMUNE = "CodComune";

  public static final String CAMPO_GIORNO_DATA_CARICAMENTO_REGE = "GiornoDataCaricamentoRege";
  public static final String CAMPO_MESE_DATA_CARICAMENTO_REGE = "MeseDataCaricamentoRege";
  public static final String CAMPO_ANNO_DATA_CARICAMENTO_REGE = "AnnoDataCaricamentoRege";

  public static final String CAMPO_COD_UFFICIO_COMPETENTE = "CodUfficioCompetente";
  public static final String CAMPO_INDIRIZZO = "Indirizzo";
  public static final String CAMPO_CAP = "Cap";
  public static final String CAMPO_TELEFONO = "Telefono";
  public static final String CAMPO_FAX = "Fax";
  public static final String CAMPO_E_MAIL = "EMail";
  
  public static final String CAMPO_FLAG_ACCORP = "FlagAccorp";

  public static final String CAMPO_GIORNO_DATA_INIZIO_PERIODO_FERIALE = "GiornoDataInizioPeriodoFeriale";
  public static final String CAMPO_MESE_DATA_INIZIO_PERIODO_FERIALE   = "MeseDataInizioPeriodoFeriale";
  public static final String CAMPO_ANNO_DATA_INIZIO_PERIODO_FERIALE   = "AnnoDataInizioPeriodoFeriale";

  public static final String CAMPO_GIORNO_DATA_FINE_PERIODO_FERIALE   = "GiornoDataFinePeriodoFeriale";
  public static final String CAMPO_MESE_DATA_FINE_PERIODO_FERIALE     = "MeseDataFinePeriodoFeriale";
  public static final String CAMPO_ANNO_DATA_FINE_PERIODO_FERIALE     = "AnnoDataFinePeriodoFeriale";

  public static final String CAMPO_ANNO_CORRENTE                      = "AnnoCorrente";

  public static final String PG_LOAD_RICERCAUFFICIO             =  ROOT_DIR + "files/siap/sico/ufficio/RicercaUfficio.jsp";
  public static final String PG_LISTADISTRETTIPROCURE           =  ROOT_DIR + "files/siap/sico/ufficio/ListaDistrettiProcure.jsp";
  public static final String PG_UFFICIDISTRETTO                 =  ROOT_DIR + "files/siap/sico/ufficio/UfficiDistretto.jsp";
  public static final String PG_SEDI_UNEP_DISTRETTO             =  ROOT_DIR + "files/siap/sico/ufficio/SediUNEPdistretto.jsp";
  public static final String PG_LISTAUDS                        =  ROOT_DIR + "files/siap/sico/ufficio/ListaUDS.jsp";
  public static final String PG_LISTADISTRETTI                  =  ROOT_DIR + "files/siap/sico/ufficio/ListaDistretti.jsp";
  public static final String PG_RICERCA_COMUNE_PER_DISTRETTO    =  ROOT_DIR + "files/siap/sico/ufficio/LoadRicercaComunePerDistretto.jsp";
  public static final String PG_LISTADISTRETTI_COMBO            =  ROOT_DIR + "files/siap/sico/ufficio/ListaDistrettiCombo.jsp";
  public static final String PG_LISTA_UFFICI_PER_TIPO           =  ROOT_DIR + "files/siap/sico/ufficio/ListaComuniUffici.jsp";
  public static final String PG_MODIFICA_UFFICIO                =  ROOT_DIR + "files/siap/sico/ufficio/LoadModificaUfficio.jsp";
  public static final String PG_LOAD_DETTAGLIOUFFICIO           =  ROOT_DIR + "files/siap/sico/ufficio/DettaglioUfficio.jsp";
  
  public static final String PG_LOAD_INSERISCI_PERIODO_FERIALE  =  ROOT_DIR + "files/siap/sico/ufficio/LoadInserisciPeriodoFeriale.jsp";
  public static final String PG_LOAD_DETTALGIO_PERIODO_FERIALE  =  ROOT_DIR + "files/siap/sico/ufficio/DettaglioPeriodoFeriale.jsp";
}