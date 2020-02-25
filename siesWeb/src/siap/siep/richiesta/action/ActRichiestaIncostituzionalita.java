package siap.siep.richiesta.action;

/**
* <p>Title: ActRichiestaIncostituzionalita</p>
* <p>Description: Classe Action per l'inserimento di Circostanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.evento.model.EventoModel;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;

public class ActRichiestaIncostituzionalita extends ActRichiestaGE
                                            implements ICostantiRichiesta
{
  public String processRequest() throws Exception
  {
    AnnotazioneManualeModel lAnnMod = setAnnotazioneManuale();
    lAnnMod.setCodTipoAnnotazione(INCOSTITUZIONALITA);
    lAnnMod.setAnnoCc(getRequestBigDecimalParameter("annoSCC"));
    lAnnMod.setNumeroCc(getRequestStringParameter("numeroSCC"));
    lAnnMod.setDataCC( getRequestDateParameter( "aaScc", "mmScc", "ggScc") );
    lAnnMod.setCodDpr("-");

    // STUB 28/09/2005 REWORK STATO ESECUZIONE
    //EventoModel lEveMod = setEvento("26","0211" );

    EventoModel lEveMod = setEvento("0211" );

   // lEveMod.setCodMotivo("0211"); // INCOSTITUZIONALITA
    lEveMod.setDataEmissione(lAnnMod.getDataRichiesta());

    String lPage = "";
    lPage = eseguiRichiesta("siap.siep.richiesta.action.ActLoadRichiestaIncostituzionalita",
                            lAnnMod, lEveMod);

    return lPage;
  }
}