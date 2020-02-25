package siap.siep.presaincarico.action;

import java.math.BigDecimal;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.SICOException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

public class ActAssociaTrasmissioneAProcedimento extends ActionSiap implements ICostantiPresaincarico
{
  public String processRequest() throws Exception
  {
    BigDecimal lIdMessage = null;

    if(!isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)){
      lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
    }
    else {
      throw new SICOException(SICOException.USER_MESSAGE, "Id Messaggio non valorizzato");
    }
    
    MessaggioModel lMess = new MessaggioModel();
    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    
    lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
    
    if (lMess==null || lMess.getIdMessaggio() == null){
      throw new SICOException(SICOException.USER_MESSAGE, "Messaggio non trovato");
    }

    BigDecimal lChiaveAnnoCumulante = null;
    BigDecimal lChiaveProgCumulante = null;
    
    lChiaveAnnoCumulante = getRequestBigDecimalParameter (ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE);
    lChiaveProgCumulante = getRequestBigDecimalParameter (ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE);
    
    lMess.setChiaveAnnoFasCumulante    (lChiaveAnnoCumulante);
    lMess.setChiaveProgrFasCumulante   (lChiaveProgCumulante);
    lMess.setChiaveUfficioFasCumulante (getCodUfficioUtenteConnesso());
    
    //==========================================================================
    // Verifico l'esistenza del procedimento proma di effettuare l'associazione
    //==========================================================================
    IFascicoloSiep lCtrlFascSiep = SIEPLookupRemote.getFascicoloSiepRemote();
    FascicoloSiepModel lFascModRicerca = new FascicoloSiepModel();
    lFascModRicerca.setChiaveAnno    (lChiaveAnnoCumulante);
    lFascModRicerca.setChiaveProgr   (lChiaveProgCumulante);
    lFascModRicerca.setChiaveUfficio (getCodUfficioUtenteConnesso());
    
    FascicoloSiepModel lFascSiep = lCtrlFascSiep.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFascModRicerca);
    
    if (lFascSiep==null || lFascSiep.getIdFascicoloSiep()==null){
      throw new SICOException(SICOException.USER_MESSAGE, "Il procedimento indicato ("+lChiaveAnnoCumulante+"/"+lChiaveProgCumulante+") non è stato trovato. Accertarsi che sia un procedimento di questo ufficio.");
    }
    
    //======================================================
    //  Affettuo l'aggiornamento
    //======================================================
    lCrtl.ExModificaMessaggio (lMess);

    //======================================================
    //  Ricarica la pagina di dettaglio
    //======================================================
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.presaincarico.action.ActDettaglioPresaincaricoCompetenza&" 
            + ICostantiMessaggio.CAMPO_ID_MESSAGGIO +"="+lIdMessage;
            //&TornaQui=20
    return lPage;
  }
}
