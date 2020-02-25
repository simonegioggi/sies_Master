package siap.sico.cssa.action;

import siap.util.ICostanti;

public interface ICostantiCSSA extends ICostanti
{

  public static final String CAMPO_ID_CSSA = "IdCssa";
  public static final String CAMPO_COD_COMUNE = "Comune";
  public static final String CAMPO_INDIRIZZO = "Indirizzo";


  public static final String PG_LOAD_RICERCACSSA  =  ROOT_DIR + "files/siap/sico/cssa/RicercaCSSA.jsp";
  public static final String PG_LISTACSSA         =  ROOT_DIR + "files/siap/sico/cssa/ListaCSSA.jsp";
  public static final String PG_LISTAUSSM         =  ROOT_DIR + "files/siap/sico/cssa/ListaUSSM.jsp";
  public static final String PG_LISTACSSA_FILTROCOMUNE         =  ROOT_DIR + "files/siap/sico/cssa/ListaCSSAFiltroComune.jsp";
  public static final String PG_RICERCA_CSSALISTA         =  ROOT_DIR + "files/siap/sico/cssa/RicercaCSSAFiltroComune.jsp";
}