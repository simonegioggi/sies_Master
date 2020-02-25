package siap.siepe.ricezioneatti.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicercaAttiPerSIEPeSIUS</p>
 * <p>Description: Azione di caricamento della form di ricerca degli atti ricevuti per Numeri SIEP e SIUS</p>
 * <p>Copyright: Bull Italia Copyright (c) 2006</p>
 * <p>Company: Bull Italia</p>
 */

public class ActLoadRicercaAttiPerSIEPeSIUS extends ActionSiap implements ICostantiRicezioneAtti
{
  public String processRequest() throws Exception
  {
    this.setLinkRitorno();
    // Imposta Tipo Atto.
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAtto());
    setRequestAttribute("tipoAtto", "" + lOption );

    // Imposta Tipo Ufficio Mittente.
    lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSius());
    setRequestAttribute("tipoUfficioSIUS", "" + lOption );

    // Imposta Stato Ricezione.
    lOption = new Option( DecodificheManager.getInstance().getStatoRicezioneSiepe());
    setRequestAttribute("statoRicezione", "" + lOption );


    return PG_LOAD_RICERCAATTIPERSIEPESIUS; //restituisce la jsp di VIEW
  }
}
