package siap.sius.stampa.action;

import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ICostantiStampaSius
 * </p>
 * <p>
 * Description: Classe di costanti di StampaSius
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public interface ICostantiStampaSius {

	public static final int TREE_SENTENZA = 11;
	public static final int TREE_UDIENZA = 12;
	public static final int TREE_RESIDENZA = 13;
	public static final int TREE_DOMICILIO = 14;
	public static final int TREE_FASCICOLOSIEP = 15;
	public static final int TREE_AVVOCATO = 16;
	public static final int TREE_LUOGODET = 18;
	public static final int TREE_MAGISTRATO = 19;
	public static final int TREE_SOGGETTO = 20;
	public static final int TREEs_RICHIESTE_ISTRUTTORIE = 21; // solo per stampa
	public static final int TREEs_PROVVEDIMENTI = 22; // solo per stampa
	public static final int TREEs_PROVVEDIMENTI_ALTRI = 23; // solo per stampa
	public static final int TREE_AVVOCATOSIUS = 24;
	public static final int TREE_ESECUZIONEMISURAALTERNATIVA = 25;
	public static final int TREE_FASCICOLOSIUS = 26; // Se utilizzata deve essere la prima
	public static final int TREE_GENERALEPROCEDIMENTO = 27;
	public static final int TREE_RIF_FAS_SIEP = 28; // STUB 14/10/2004
	public static final int TREE_TIT_ESE_REF = 29; // STUB 19/01/2005
	public static final int TREE_FASCICOLOSIEP_ALL = 30; // STUB 13/01/2006
	public static final int TREE_ESECUZIONEMISURASICUREZZA = 31;

	// MEV_65: aggiunte costanti per gestire nuova funzionalita'
	public static final String PG_LOAD_RICERCA_COPERTINE_FASCICOLI_SIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/stampa/LoadRicercaCopertineFascicoliSius.jsp";
	public static final String PG_STAMPA_COPERTINE_FASCICOLI_SIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/stampa/StampaCopertineFascicoliSius.jsp";
	// FINE MEV_65

}