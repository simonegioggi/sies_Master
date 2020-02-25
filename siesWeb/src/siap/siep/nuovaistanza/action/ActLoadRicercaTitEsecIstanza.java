package siap.siep.nuovaistanza.action;


import f3b.web.html.Option;
/**
 * <p>Title: ActLoadRicercaTitEsecIstanza</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: Agile</p>
 * @version 5.0
 */import siap.sico.decodifiche.controller.DecodificheManager;

import siap.sico.web.ActionSiap;

public class ActLoadRicercaTitEsecIstanza extends ActionSiap implements ICostantiNuovaIstanza
{
  public String processRequest() throws Exception
  {
  	// Attivazione punto di Ritorno
  	setLinkRitorno();

  	Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente());
    setRequestAttribute("autoritaemittente", "" + lOption );

    return PG_LOAD_RICERCA_TIT_ESEC_ISTANZA; //restituisce la jsp di VIEW
  }
}
