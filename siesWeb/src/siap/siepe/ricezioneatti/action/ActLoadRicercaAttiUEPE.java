package siap.siepe.ricezioneatti.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActRicercaAttiPerUEPE</p>
 * <p>Description: Azione che si occupa di presentare la maschera d'interrogazione degli
 * atti ricevuti UEPE.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company:Bull Gruppo Eunics </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadRicercaAttiUEPE extends ActionSiap implements ICostantiRicezioneAtti
{
  public String processRequest() throws Exception
  {
    // Imposta link di ritorno
    this.setLinkRitorno();
    // Imposta Elenco percombo box Tipo Atto.
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAtto());
    setRequestAttribute("tipoAtto", "" + lOption );

    // Imposta Elenco per Tipo Ufficio Mittente.
    lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSiepe());
    setRequestAttribute("tipoUfficioSIEPE", "" + lOption );

    // Imposta Elenco Stato Ricezione.
    lOption = new Option( DecodificheManager.getInstance().getStatoRicezioneSiepe());
    setRequestAttribute("statoRicezione", "" + lOption );

    return PG_LOAD_RICERCAATTIUEPE; //restituisce la jsp di VIEW
  }
}
