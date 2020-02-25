package siap.sius.permesso.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiEventoPermessoLicenza</p>
* <p>Description: Classe di costanti di EventoPermessoLicenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiEventoPermessoLicenza
{
  public static final String CAMPO_ID_EVENTO_PERMESSO_LICENZA = "IdEventoPermessoLicenza"; 
  public static final String CAMPO_COD_TIPO_EVENTO = "CodTipoEvento";
  public static final String CAMPO_DESCR_EVENTO = "DescrEvento";
	public static final String CAMPO_GIORNO_DATA_SEGNALAZIONE = "GiornoDataSegnalazione"; 
	public static final String CAMPO_MESE_DATA_SEGNALAZIONE = "MeseDataSegnalazione"; 
	public static final String CAMPO_ANNO_DATA_SEGNALAZIONE = "AnnoDataSegnalazione"; 
	public static final String CAMPO_MITTENTE_SEGNALAZIONE = "MittenteSegnalazione"; 
	public static final String CAMPO_COD_TIPO_CONSEGUENZA = "CodTipoConseguenza";
  public static final String CAMPO_DESCR_CONSEGUENZE = "DescrConseguenze";
	public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento"; 
	public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento"; 
	public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento"; 
	public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento"; 
	public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento"; 
	public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento"; 
	public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento"; 
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento"; 
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento"; 
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento"; 
  public static final String CAMPO_LIC_ID_LICENZA_LIBANTICIPATA = "LicIdLicenzaLibanticipata";
  
  public static final String PG_LOAD_RICERCAEVENTOPERMESSOLICENZA	= 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/LoadRicercaEventoPermessoLicenza.jsp";
  public static final String PG_RICERCAEVENTOPERMESSOLICENZA	= 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/RicercaEventoPermessoLicenza.jsp";
  public static final String PG_LOAD_INSERISCIEVENTOPERMESSOLICENZA	= 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/LoadInserisciEventoPermessoLicenza.jsp";
  public static final String PG_LOAD_DETTAGLIOEVENTOPERMESSOLICENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/DettaglioEventoPermessoLicenza.jsp";
  public static final String PG_ELENCOEVENTOPERMESSOLICENZA  = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/ElencoEventiPermessiLicenze.jsp";
}