package siap.sius.ulterioreistanza.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiUlterioreIstanza</p>
* <p>Description: Classe di costanti di UlterioreIstanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiUlterioreIstanza
{
  // Costanti in mappatura con la tabella
  public static final String CAMPO_ID_ULTERIORE_ISTANZA = "IdUlterioreIstanza"; 
	public static final String CAMPO_COD_OGGETTO_PROCEDIMENTO = "CodOggettoProcedimento";
	public static final String CAMPO_GIORNO_DATA_RICHIESTA = "GiornoDataRichiesta"; 
  public static final String CAMPO_MESE_DATA_RICHIESTA = "MeseDataRichiesta"; 
  public static final String CAMPO_ANNO_DATA_RICHIESTA = "AnnoDataRichiesta"; 
  public static final String CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA = "GiornoDataArrivoCancelleria"; 
  public static final String CAMPO_MESE_DATA_ARRIVO_CANCELLERIA = "MeseDataArrivoCancelleria"; 
  public static final String CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA = "AnnoDataArrivoCancelleria"; 
  public static final String CAMPO_COD_TIPO_ATTO = "CodTipoAtto"; 
  public static final String CAMPO_COD_TIPO_MITTENTE_ATTO = "CodTipoMittenteAtto"; 
  public static final String CAMPO_SEDE_MITTENTE = "SedeMittente"; 
  public static final String CAMPO_DESCR_MITTENTE = "DescrMittente"; 
  public static final String CAMPO_NOTE = "Note"; 
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento"; 
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento"; 
  public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento"; 
  public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento"; 
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento"; 
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento"; 
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento"; 
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento"; 
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento"; 
  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento"; 
  public static final String CAMPO_FAS_SIU_ID_FASCICOLO_SIUS = "FasSiuIdFascicoloSius"; 
  
  // Costanti non direttamente mappati con la tabella.
  public static final String CAMPO_DESCR_OGGETTO_PROCEDIMENTO = "DescrOggettoProcedimento";
  public static final String CAMPO_COD_CONTENUTO = "CodContenuto";
  public static final String CAMPO_DESCR_CONTENUTO = "DescrContenuto";
  
  public static final String PG_LOAD_RICERCAULTERIOREISTANZA	= IWebConstants.ROOT_DIR + "files/siap/sius/ulterioreistanza/LoadRicercaUlterioreIstanza.jsp";
  public static final String PG_LOAD_DETTAGLIOULTERIOREISTANZA	= IWebConstants.ROOT_DIR + "files/siap/sius/ulterioreistanza/DettaglioUlterioreIstanza.jsp";
  public static final String PG_RICERCAULTERIOREISTANZA	= IWebConstants.ROOT_DIR + "files/siap/sius/ulterioreistanza/RicercaUlterioreIstanza.jsp";
  public static final String PG_LOAD_INSERISCIULTERIOREISTANZA	= IWebConstants.ROOT_DIR + "files/siap/sius/ulterioreistanza/LoadInserisciUlterioreIstanza.jsp";
}