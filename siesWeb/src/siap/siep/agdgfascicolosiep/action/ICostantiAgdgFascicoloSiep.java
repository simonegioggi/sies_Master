package siap.siep.agdgfascicolosiep.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiAgdgFascicoloSiep</p>
* <p>Description: Classe di costanti di AgdgFascicoloSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiAgdgFascicoloSiep
{
		 public static final String CAMPO_ID_AGDG_FASCICOLO_SIEP = "IdAgdgFascicoloSiep"; 
		 public static final String CAMPO_AGDG_ID_ALTRIGRADIGIUDIZIO = "AgdgIdAltrigradigiudizio"; 
		 public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep"; 
		 public static final String PG_LOAD_RICERCAAGDGFASCICOLOSIEP	= IWebConstants.ROOT_DIR + "files/siap/siep/agdgfascicolosiep/LoadRicercaAgdgFascicoloSiep.jsp";
		 public static final String PG_LOAD_DETTAGLIOAGDGFASCICOLOSIEP	= IWebConstants.ROOT_DIR + "files/siap/siep/agdgfascicolosiep/LoadRicercaAgdgFascicoloSiep.jsp";
		 public static final String PG_RICERCAAGDGFASCICOLOSIEP	= IWebConstants.ROOT_DIR + "files/siap/siep/altrigradigiudizio/RicercaAgdgFascicoloSiep.jsp";
		 public static final String PG_LOAD_INSERISCIAGDGFASCICOLOSIEP	= IWebConstants.ROOT_DIR + "files/siap/siep/agdgfascicolosiep/LoadInserisciAgdgFascicoloSiep.jsp";
}