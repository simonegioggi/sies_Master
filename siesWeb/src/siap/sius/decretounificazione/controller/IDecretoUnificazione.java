package siap.sius.decretounificazione.controller;

/**
* <p>Title: DecretoUnificazioneController</p>
* <p>Description: Classe Controller per DecretoUnificazione</p>
* <p>Copyright: Copyright (c) 2003</p>
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
import f3b.util.F3BException;

public interface IDecretoUnificazione
{
  public boolean ExVerificaDecretoUnificazione (String annoDaUnif, String numeroDaUnif, String annoUnificante, String numeroUnificante, String codUfficioUtente )
  throws F3BException;

  public FascicoloGPModel ExVerificaFascicoloDaUnificare (String annoDaUnif, String numeroDaUnif, String codUfficioUtente )
  throws F3BException;

  public FascicoloGPModel ExVerificaFascicoloUnificante (String annoUnificante, String numeroUnificante, String codUfficioUtente )
  throws F3BException;

  public EventoModel ExInserisciDecretoUnificazione (String AnnoDaUnif, String NumeroDaUnif, String AnnoUnificante, String NumeroUnificante, String codUfficioUtente, String codUtente, String codComuneUfficioUtente, Date dataUnificazione )
  throws F3BException;

  public EventoNotificaModel ExStampaDecretoUnificazione ( EventoModel aEvento , UfficioModel lUfficio, UtenteModel aUtenteModel )
  throws F3BException;

  public void ExCancellaDecretoUnificazione( BigDecimal aKeyEvento, String aUfficioUtenteConnesso, String aUtenteConnesso, String aLuogoUfficioUtenteConnesso)
  throws F3BException;
}