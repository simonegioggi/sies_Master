package siap.siep.istanza.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciIstanzaAnnTrasmissione</p>
* <p>Description: Classe Action per la load inserisci di Istanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciIstanzaAnnTrasmissione extends ActionSiap implements ICostantiIstanza
{
  public String processRequest() throws Exception
  {

    if( isSessionAttributeNullObj("fascicolo") )
      {
        String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
                        "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                        ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" +
                        "siap.siep.istanza.action.ActLoadInserisciIstanzaAnnTrasmissione";
                        return lPage;
      }

    if (!this.isSessionAttributeNullObj("fascicolo"))
    {
      FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
      setRequestAttribute("fascicolo", lFascicoloModel);
    }
      this.isFascicoloSiepDiCompetenza();
      this.isEventoNonValidato();

    //Oggetto dell'istanza
    Option lOption = new Option( DecodificheManager.getInstance().getOggettoProcedimentoTDS());
    setRequestAttribute("contenuto", "" + lOption );

    setRequestAttribute("modalita", "I");

    return PG_LOAD_INSERISCIISTANZA_ANN_TRASMISSIONE;  //restituisce la jsp di VIEW
  }
}