package siap.siep.istitutodetenzione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiIstitutoDetenzione</p>
* <p>Description: Classe di costanti di IstitutoDetenzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiIstitutoDetenzione
{
		 public static final String CAMPO_ID_ISTITUTO_DETENZIONE = "IdIstitutoDetenzione";
		 public static final String CAMPO_COD_TIPO_ISTITUTO = "CodTipoIstituto";
		 public static final String CAMPO_COD_COMUNE = "CodComune";
		 public static final String CAMPO_COD_PROVINCIA = "CodProvincia";
		 public static final String CAMPO_INDIRIZZO = "Indirizzo";
		 public static final String CAMPO_DESCRIZIONE = "Descrizione";
		 public static final String CAMPO_NOTE = "Note";

                 public static final String PG_LISTA_ISTITUTO_FILTROTIPO          =  IWebConstants.ROOT_DIR + "files/siap/siep/istitutodetenzione/RicercaIstitutoFiltroTipo.jsp";
                 public static final String PG_RICERCA_ISTITUTO_LISTA             =  IWebConstants.ROOT_DIR + "files/siap/siep/istitutodetenzione/ListaIstitutoDetenzioneFiltroTipo.jsp";
                 public static final String PG_ISTITUTO_LISTA                     =  IWebConstants.ROOT_DIR + "files/siap/siep/istitutodetenzione/FiltraIstitutoLista.jsp";
}