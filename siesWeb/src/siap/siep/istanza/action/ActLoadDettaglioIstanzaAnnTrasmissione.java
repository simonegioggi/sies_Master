package siap.siep.istanza.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.controller.IIstanza;
import siap.siep.istanza.model.IstanzaSoggettoEventoFascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadDettaglioIstanzaAnnTrasmissione</p>
* <p>Description: Classe Action per la load dettaglio di Istanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioIstanzaAnnTrasmissione extends ActionSiap implements ICostantiIstanza
{
  public String processRequest() throws Exception
  {
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_ISTANZA);

    IIstanza lCtrl = SIEPLookupRemote.getIstanzaRemote();
    IstanzaSoggettoEventoFascicoloSiepModel lMod = lCtrl.ExRicercaIstanzaSoggettoEventoFascicoloSiepByKey(lId);

    if(lMod.getIstanza() == null)
      throw new F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato");

    setRequestAttribute("istanzaSoggettoEventoFascicoloSiep", lMod);

    FascicoloSiepModel lFascicolo = lMod.getFascicoloSiep();
    if(lFascicolo != null)
    {
      setSessionAttribute("fascicolo", lFascicolo);
    }

    return PG_LOAD_DETTAGLIOISTANZA_ANN_TRASMISSIONE;
  }
}