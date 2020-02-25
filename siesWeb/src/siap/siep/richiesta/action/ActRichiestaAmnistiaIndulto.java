package siap.siep.richiesta.action;

/**
* <p>Title: ActRichiestaAmnistiaIndulto</p>
* <p>Description: Classe Action per l'inserimento di Circostanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.evento.model.EventoModel;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;

public class ActRichiestaAmnistiaIndulto extends ActRichiestaGE
                                         implements ICostantiRichiesta
{
  public String processRequest() throws Exception
  {
    // ANNOTAZIONE
    AnnotazioneManualeModel lAnnMod = setAnnotazioneManuale();
    lAnnMod.setCodTipoAnnotazione(getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE));
    lAnnMod.setCodDpr(getRequestStringParameter("dpr"));
    lAnnMod.setDataRichiesta(getRequestDateParameter( CAMPO_ANNO_DATA_RICHIESTA, CAMPO_MESE_DATA_RICHIESTA, CAMPO_GIORNO_DATA_RICHIESTA) );

    // EVENTO
    // Luigi 29-09-2005
    EventoModel lEveMod = setEvento("0122" );
   // lEveMod.setCodMotivo("0122"); //APPLICAZIONE AMNISTIA/INDULTO
    lEveMod.setDataEmissione(lAnnMod.getDataRichiesta());

    String lPage = "";
    lPage = eseguiRichiesta("siap.siep.richiesta.action.ActLoadRichiestaAmnistiaIndulto",
                            lAnnMod, lEveMod);

    return lPage;
  }
}