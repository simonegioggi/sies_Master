package siap.sius.unificazione.controller;

/**
* <p>Title: UnificazioneController</p>
* <p>Description: Classe Controller per Unificazione</p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import f3b.util.F3BException;

public interface IUnificazione
{
  public boolean ExVerificaUnificazione (String annoDaUnif, String numeroDaUnif, String annoUnificante, String numeroUnificante, String codUfficioUtente )
  throws F3BException;

  public FascicoloGPModel ExVerificaFascicoloDaUnificare (String annoDaUnif, String numeroDaUnif, String codUfficioUtente )
  throws F3BException;

  public FascicoloGPModel ExVerificaSoggettoDaUnificare (String annoDaUnif, String numeroDaUnif, String codUfficioDaUnif, BigDecimal aIdSoggettoUnificante )
  throws F3BException;

  public FascicoloGPModel ExVerificaFascicoloUnificante (String annoUnificante, String numeroUnificante, String codUfficioUtente )
  throws F3BException;

  //public String ExVerificaUnificabilitaEMA (FascicoloGPModel lFascicolo, String aCodUffDaUnif, BigDecimal aIdSoggettoUnificante, Connection aConn )
  //throws F3BException;

  public FascicoloGPModel ExVerificaSoggettoUnificante (String annoUnificante, String numeroUnificante, String codUfficioUnificante )
  throws F3BException;

  public EventoModel ExInserisciUnificazione (String AnnoDaUnif, String NumeroDaUnif, String AnnoUnificante, String NumeroUnificante, String codUfficioUtente, String codUtente, String codComuneUfficioUtente, Date dataUnificazione )
  throws F3BException;

  public EventoNotificaModel ExStampaUnificazione ( EventoModel aEvento , UfficioModel lUfficio, UtenteModel aUtenteModel)
  throws F3BException;

  public void ExCancellaUnificazione( BigDecimal aKeyEvento, String aUfficioUtenteConnesso, String aUtenteConnesso, String aLuogoUfficioUtenteConnesso)
  throws F3BException;

  public void ExInsUnificazioneSoggetti( FascicoloSiusModel lFasUnificante, FascicoloSiusModel lFasDaUnificare, String aUfficioUtenteConnesso, String aUtenteConnesso, String aLuogoUfficioUtenteConnesso)
  throws F3BException;
}
