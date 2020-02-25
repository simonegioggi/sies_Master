package siap.sico.w_magistrato.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiWMagistrato</p>
* <p>Description: Classe di costanti di WMagistrato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiWMagistrato
{
		 public static final String CAMPO_COD_MAGISTRATO = "CodMagistrato";
		 public static final String CAMPO_COGNOME = "Cognome";
		 public static final String CAMPO_NOME = "Nome";
		 public static final String CAMPO_GIORNO_DATA_NASCITA = "GiornoDataNascita";
		 public static final String CAMPO_MESE_DATA_NASCITA = "MeseDataNascita";
		 public static final String CAMPO_ANNO_DATA_NASCITA = "AnnoDataNascita";
		 public static final String CAMPO_DESC_LUOGO_NASCITA = "DescLuogoNascita";
		 public static final String CAMPO_GIORNO_DATA_CARICAMENTO = "GiornoDataCaricamento";
		 public static final String CAMPO_MESE_DATA_CARICAMENTO = "MeseDataCaricamento";
		 public static final String CAMPO_ANNO_DATA_CARICAMENTO = "AnnoDataCaricamento";
		 public static final String PG_LOAD_RICERCAWMAGISTRATO	= IWebConstants.ROOT_DIR + "files/siap/sico/w_magistrato/LoadRicercaWMagistrato.jsp";
		 public static final String PG_LOAD_DETTAGLIOWMAGISTRATO	= IWebConstants.ROOT_DIR + "files/siap/sico/w_magistrato/LoadRicercaWMagistrato.jsp";
		 public static final String PG_RICERCAWMAGISTRATO	= IWebConstants.ROOT_DIR + "files/siap/sico/w_magistrato/RicercaWMagistrato.jsp";
		 public static final String PG_LOAD_INSERISCIWMAGISTRATO	= IWebConstants.ROOT_DIR + "files/siap/sico/w_magistrato/LoadInserisciWMagistrato.jsp";
     public static final String PG_LOAD_RICERCAWMAGISTRATO_COMP	= IWebConstants.ROOT_DIR + "files/siap/sico/w_magistrato/LoadRicercaWMagistratoComp.jsp";
     public static final String PG_RICERCAWMAGISTRATO_COMP	= IWebConstants.ROOT_DIR + "files/siap/sico/w_magistrato/RicercaWMagistratoComp.jsp";

}