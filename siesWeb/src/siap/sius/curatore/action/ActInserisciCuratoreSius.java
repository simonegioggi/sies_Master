package siap.sius.curatore.action;


/**
* <p>Title: ActInserisciCuratoreSius</p>
* <p>Description: Classe Action per l'inserimento di CuratoreSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import siap.sige.curatore.action.ICostantiCuratore;
import siap.sius.curatore.controller.ICuratoreSius;
import siap.sius.curatore.model.CuratoreSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


public class ActInserisciCuratoreSius extends ActionSiap implements ICostantiCuratoreSius
{
/**
* Azione di Inserimento del Curatore Sius
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
	public String processRequest() throws Exception
	{
    //setLinkRitorno();
    this.gestioneRitorno();

    //generale
    FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
    CuratoreSiusModel lCuratore = new CuratoreSiusModel();

    //curatore nuovo
    //lCuratore.getCuratore().setCognome(this.getRequestStringParameter(ICostantiCuratore.CAMPO_COGNOME));
    //lCuratore.getCuratore().setNome(this.getRequestStringParameter(ICostantiCuratore.CAMPO_NOME));
    //curatore vecchio
    lCuratore.setCurIdCuratore(this.getRequestBigDecimalParameter(ICostantiCuratore.CAMPO_ID_CURATORE));

    lCuratore.setFasSiuIdFascicoloSius(lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
    lCuratore.setDataInizio( this.getRequestDateParameter(ICostantiCuratoreSius.CAMPO_ANNO_DATA_INIZIO,ICostantiCuratoreSius.CAMPO_MESE_DATA_INIZIO,ICostantiCuratoreSius.CAMPO_GIORNO_DATA_INIZIO));
    lCuratore.setFlagTipo(this.getRequestStringParameter(ICostantiCuratoreSius.CAMPO_FLAG_TIPO));
    lCuratore.setDataInserimento(DateUtils.getSysDate());
    lCuratore.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    lCuratore.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

    ICuratoreSius lCtrl = SIUSLookupRemote.getCuratoreSiusRemote();
    CuratoreSiusModel lCurSiusModRet = lCtrl.ExInserisciCuratoreSius(lCuratore) ;

    // setta la risposta nella request
    setRequestAttribute("curatoreprecedente",this.getRequestStringParameter(ICostantiCuratoreSius.CAMPO_ID_CURATORE_VECCHIO));
    setRequestAttribute("curatore", lCurSiusModRet);

    //Prepara la pagina di destinazione
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.curatore.action.ActRicercaCuratoreSius&"+CAMPO_CUR_ID_CURATORE+"="+lCuratore.getCurIdCuratore().toString();
    return lPage;
	}

}
