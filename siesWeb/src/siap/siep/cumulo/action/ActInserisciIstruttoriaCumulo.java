package siap.siep.cumulo.action;


/**
* <p>Title: ActInserisciIstruttoriaCumulo</p>
* <p>Description: Classe Action per l'Inserimento/Apertura dell'Istruttoria Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Eutelia S.p.a</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciIstruttoriaCumulo extends ActCumulo implements ICostantiCumulo
{
  /**
  * Azione di Inserimento/Apertura dell'Istruttoria Cumulo
  *
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException
  {

    FascicoloSiepModel lFascMod = ((FascicoloSiepModel)(getSessionAttribute("fascicolo")));

    //==========================================================================
    // NUOVO EVENTO per l'Istruttoria Cumulo
    //==========================================================================
    EventoModel lEventoModel = new EventoModel();

    lEventoModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
    
    // da stabilire i codici
    lEventoModel.setCodTipoEvento("01");
    lEventoModel.setCodTipoProvvedimento("04"); // Provvedimento
    lEventoModel.setCodMotivo("0946"); // 
    
    lEventoModel.setFlagDocumentoRegistrato("N"); // n.b. Viene Validato in fase di Validazione Provvedimento di cumulo o annullamento istruttoria
    lEventoModel.setFlagStampaSiep("S"); //  ??
    lEventoModel.setFlagVideoSiep("S");  //  ??
    lEventoModel.setAnnoProtocollo(new BigDecimal(2007));
    
    lEventoModel.setDataEmissione(DateUtils.getDate(DateUtils.getYearToString(DateUtils.getSysDate()),
                                                    DateUtils.getMonthToString(DateUtils.getSysDate()),
                                                    DateUtils.getDayToString(DateUtils.getSysDate()))
                                  );
    lEventoModel.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
    lEventoModel.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
    
    lEventoModel.setDataTrasmissioneAtti(null);
    lEventoModel.setCodMagistrato(null);
    lEventoModel.setCodEsito("-");
    lEventoModel.setCodTipoUfficioDestinatario("-");
    lEventoModel.setCodLuogoDestinatario("-");
    lEventoModel.setCodOperatoreInserimento (getCodUtenteConnesso());
    lEventoModel.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
    lEventoModel.setDataInserimento         (DateUtils.getSysDate());


    //==========================================================================
    // Inserisce evento
    //==========================================================================
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    lEventoModel = lCtrlEvento.ExInserisciEvento(lEventoModel);

    //==========================================================================
    // Va effettuata l'estrazione dei dati del cumulante
    //==========================================================================

    //Pagina di dettaglio
    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.cumulo.action.ActDettaglioIstruttoriaCumulo&" +
                   ICostantiEvento.CAMPO_ID_EVENTO + "=" +
                   lEventoModel.getIdEvento();

    return lPage;
  }
}