package siap.sige.scadenzario.action;

import siap.sico.web.ActionSiap;
import siap.sige.scadenzario.controller.IScadenzarioSige;
import siap.sige.util.SIGELookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaScadenzarioSige</p>
* <p>Description: Classe Action per la load ricerca di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaScadenzarioSige extends ActionSiap implements ICostantiScadenzarioSige
{
  public String processRequest() throws F3BException
  {
    // Imposta la combo dei Tipi di Scadenzari SIUS.

    String lTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
    IScadenzarioSige lScaCtrl = SIGELookupRemote.getScadenzarioSigeRemote();
    Option lOption = new Option( lScaCtrl.ExElencoTipiScadenzarioByTipoUfficio( lTipoUfficio ) );
    setRequestAttribute( "tipoScadenzarioSIGE", "" + lOption );

    //this.setLinkRitorno();

    return PG_LOAD_RICERCASCADENZARIOSIGE;  //restituisce la jsp di VIEW
  }
}