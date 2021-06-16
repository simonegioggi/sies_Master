package siap.sico.decodifiche.action;

import siap.util.ICostanti;

public interface ICostantiComune extends ICostanti
{
  public static final String PG_LOAD_RICERCACOMUNE            =  ROOT_DIR + "files/siap/sico/comune/LoadRicercaComune.jsp";
  public static final String PG_LOAD_RICERCACOMUNEPERTIPOUFFICIO	=  ROOT_DIR + "files/siap/sico/comune/LoadRicercaComunePerTipoUfficio.jsp";
  public static final String PG_RICERCACOMUNE 	              =  ROOT_DIR + "files/siap/sico/comune/RicercaComune.jsp";
  public static final String PG_LISTAPROV                     =  ROOT_DIR + "files/siap/sico/comune/ListaProv.jsp";
  public static final String PG_COMUNEPROV                    =  ROOT_DIR + "files/siap/sico/comune/FiltraProv.jsp";
  public static final String PG_FILTRA_UFF_COMUNE             =  ROOT_DIR + "files/siap/sico/comune/FiltraUffSorvComune.jsp";

  public static final String PG_RICERCACOMUNE_TIPOUFF_PER_DISTRETTO	=  ROOT_DIR + "files/siap/sico/comune/RicercaComuneTipoUfficoPerDistretto.jsp";

  public static final String PG_LOAD_RICERCACOMUNENASCITA     =  ROOT_DIR + "files/siap/sico/comune/LoadRicercaComuneNascita.jsp";
  public static final String PG_RICERCACOMUNENASCITA          =  ROOT_DIR + "files/siap/sico/comune/RicercaComuneNascita.jsp";
  public static final String PG_COMUNEPROVNASCITA             =  ROOT_DIR + "files/siap/sico/comune/FiltraProvNascita.jsp";
  public static final String PG_LISTAPROVNASCITA              =  ROOT_DIR + "files/siap/sico/comune/ListaProvNascita.jsp";
  
  public static final String PG_RICERCA_COMUNE_TDS 	      =  ROOT_DIR + "files/siap/sico/comune/RicercaComuneTds.jsp";
  public static final String CAMPO_COD_PROVINCIA = "provincia";
  
  // Questa serve per evitare il problema dei comuni omonimi.
  // CAMPO_COD_COMUNE_REALE è usato dalla jsp RicercaComune. 
  // Per implementare il controllo dei comuni omonimi si deve inserire nella form CAMPO_COD_COMUNE_REALE
  // come campo hidden e gestirlo poi nelle relative action.
  public static final String CAMPO_COD_COMUNE_REALE = "CodComuneReale";  

  public static final String PG_LISTA_QUESTURE  =  ROOT_DIR + "files/siap/sico/comune/ListaQuesture.jsp";

  public static final String COD_COMUNE_ROMA = "058091";

}