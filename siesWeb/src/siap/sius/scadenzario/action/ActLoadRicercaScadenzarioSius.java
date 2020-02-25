package siap.sius.scadenzario.action;

import siap.sico.web.ActionSiap;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaScadenzario</p>
* <p>Description: Classe Action per la load ricerca di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaScadenzarioSius extends ActionSiap implements ICostantiScadenzarioSius
{
  public String processRequest() throws F3BException
  {
    // Imposta la combo dei Tipi di Scadenzari SIUS.

    String lTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
    IScadenzarioSius lScaCtrl = SIUSLookupRemote.getScadenzarioRemote();
    Option lOption = new Option( lScaCtrl.ExElencoTipiScadenzarioByTipoUfficio( lTipoUfficio ) );
    setRequestAttribute( "tipoScadenzarioSIUS", "" + lOption );

    //this.setLinkRitorno();

    return PG_LOAD_RICERCASCADENZARIO;  //restituisce la jsp di VIEW
  }
}