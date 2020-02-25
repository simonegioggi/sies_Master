package siap.sico.soggettodattilo.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiSoggettoDattilo</p>
* <p>Description: Classe di costanti di SoggettoDattilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public interface ICostantiSoggettoDattilo {

	public static final String CAMPO_ID_DATTILO = "IdDattilo";
	public static final String CAMPO_COD_SOGGETTO = "CodSoggetto";
	public static final String CAMPO_DOC_TIPO = "DocTipo";
	public static final String CAMPO_DOC_NOME = "DocNome";
	public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
	public static final String CAMPO_BLOB = "CampoBlob";

	public static final String PG_BUTTONS_MORE_PARAMETERS = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/buttonsSoggettoDattilo.jsp";

	public static final String PG_LOAD_RICERCASOGGETTODATTILO = IWebConstants.ROOT_DIR + "files/siap/sico/soggettodattilo/LoadRicercaSoggettoDattilo.jsp";
	public static final String PG_LOAD_DETTAGLIOSOGGETTODATTILO = IWebConstants.ROOT_DIR + "files/siap/sico/soggettodattilo/LoadRicercaSoggettoDattilo.jsp";
	public static final String PG_RICERCASOGGETTODATTILO = IWebConstants.ROOT_DIR + "files/siap/sico/soggettodattilo/RicercaSoggettoDattilo.jsp";
	public static final String PG_LOAD_INSERISCISOGGETTODATTILO = IWebConstants.ROOT_DIR + "files/siap/sico/soggettodattilo/LoadInserisciSoggettoDattilo.jsp";

}