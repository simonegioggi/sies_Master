package siap.regesies.regesoggetto.action;

import java.math.BigDecimal;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regesoggetto.model.RegeSoggettoModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>Title: ActLoadDettaglioRegeSoggetto</p>
 * <p>Description: Classe Action per il dettaglio di un Soggetto Omonimo</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 */
public class ActDettaglioSoggettoOmonimo extends ActionRegeSiap implements ICostantiRegeSoggetto
{
  public String processRequest() throws Exception
  {
    BigDecimal lId = getRequestBigDecimalParameter("IdSoggetto");

    //Chiama il controller.
    ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
    SoggettoModel lSoggetto = lSogCtrl.ExRicercaSoggettoByKey(lId);

    RegeSoggettoModel lRegeSogg = new RegeSoggettoModel(lSoggetto);

    //Inserisce il model soggetto nella request
    setRequestAttribute("regesoggetto", lRegeSogg);
    setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,"siap.regesies.regesentenza.action.ActDettaglioProvvedimento");

    return PG_LOAD_DETTAGLIOREGESOGGETTO;
  }
}