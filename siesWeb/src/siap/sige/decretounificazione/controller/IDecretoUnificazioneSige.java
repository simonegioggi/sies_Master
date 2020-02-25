package siap.sige.decretounificazione.controller;

/**
* <p>Title: DecretoUnificazioneController</p>
* <p>Description: Classe Controller per DecretoUnificazione</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.model.EventoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import f3b.util.F3BException;

public interface IDecretoUnificazioneSige
{
  public FascicoloSigeEstesoModel ExVerificaFascicoloSigePerUnificazione (String annoFascicolo, String numeroFascicolo, String codUfficioUtente, String ruoloFascicolo )
  throws F3BException;

  public ProvvedimentoSigeModel ExInserisciDecretoUnificazioneSige (String AnnoDaUnif, String NumeroDaUnif, String AnnoUnificante, String NumeroUnificante, String codUfficioUtente, String codUtente, String codComuneUfficioUtente, Date dataUnificazione )
  throws F3BException;
  
  public ProvvedimentoSigeModel ExInserisciVerbaleUnificazioneSige (String AnnoDaUnif, String NumeroDaUnif, String AnnoUnificante, String NumeroUnificante, String codUfficioUtente, String codUtente, String codComuneUfficioUtente, Date dataUnificazione )
  throws F3BException;

  public ByteArrayOutputStream ExStampaDecretoUnificazioneSige ( EventoModel lEvento, BigDecimal aIdFascicolo, BigDecimal aIdFascicoloUnificante, String lTipoUfficio, UtenteModel aUtenteModel )
  throws F3BException;
  
  public void ExCancellaDecretoUnificazioneSige( BigDecimal aKeyProvvedimento, String aUfficioUtenteConnesso, String aUtenteConnesso, String aLuogoUfficioUtenteConnesso)
  throws F3BException;
  
  public void ExCancellaVerbaleUnificazioneSige( BigDecimal aKeyProvvedimento, String aUfficioUtenteConnesso, String aUtenteConnesso, String aLuogoUfficioUtenteConnesso)
  throws F3BException;

  public ByteArrayOutputStream ExStampaVerbaleUnificazioneSige ( EventoModel lEvento, BigDecimal aIdFascicolo, BigDecimal aIdFascicoloUnificante, String lTipoUfficio, UtenteModel aUtenteModel )
  throws F3BException;

}