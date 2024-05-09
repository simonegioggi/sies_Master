package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Funzione di cancellazione delle rinnovazioni se non validate
 *
 * @author d.fiorletta
 * @since MAV_2023-33
 */
public class ActCancellaRinnovazioneOIPP extends ActionSiap implements ICostantiNotifica {

  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  public String processRequest() throws F3BException {

    // info per il log
    siesLogger.debug(getClass().getName() + ".processRequest: inizio");

    BigDecimal lId = getRequestBigDecimalParameter(ICostantiRinnovo.CAMPO_ID_RINNOVO);

    IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
    RinnovoModel lRinModel = lCtrl.ExRicercaRinnovoByKey(lId);

    lCtrl.ExCancellaRinnovoPP(lRinModel);

    // Recupero l'OI
    INotifica lNotCtrl = SIEPLookupRemote.getNotificaRemote();
    NotificaModel lNotificaModel = lNotCtrl.ExRicercaNotificaByKey(lRinModel.getNotIdNotifica());

    // info per il log
    siesLogger.debug(getClass().getName() + ".processRequest: fine");

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
        + "=siap.siep.sanzionesostitutiva.action.ActLoadInserisciRinnovazioneOIPP&"
        + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lNotificaModel.getEveIdEvento();
    return lPage;
  }

}
