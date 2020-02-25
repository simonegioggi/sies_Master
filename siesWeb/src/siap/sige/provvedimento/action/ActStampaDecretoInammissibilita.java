package siap.sige.provvedimento.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.ufficio.model.UfficioModel;
import siap.sige.SIGEException;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActStampaDecretoInammissibilita extends ActionSige implements ICostantiProvvedimentoSige
{

	/**
  * Azione di Stampa del Decreto di Inammissibilita
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
    // Fascicolo Sige Esteso in sessione.
  	FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");
   
    // chiama il controller x leggere il ProvvedimentoSigeEventoModel.
    IProvvedimentoSige lCtrl = SIGELookupRemote.getProvvedimentoRemote();
    ProvvedimentoSigeEventoModel lProEveMod = lCtrl.ExRicercaProvvedimentoDefinitorioByIdFascicolo(lFasEsteso.getFascicoloSige().getIdFascicoloSige() );

    // Valorizzazione dell'Evento.
    String lId = getRequestStringParameter( ICostantiEvento.CAMPO_ID_EVENTO);
    EventoModel lEveMod = new EventoModel();
    lEveMod.setIdEvento(new BigDecimal(lId));

    UfficioModel lUff = new UfficioModel(this.getUfficioUtenteConnesso());

    lEveMod.setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

    lEveMod.setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.setFlagDocumentoRegistrato("N");

    if(!isRequestParameterNullObj(ICostantiTemplate.CAMPO_ID_TEMPLATE))
    	lEveMod.setTemIdTemplate(this.getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE));
    else
    	lEveMod.setTemIdTemplate("SIGE_DE_003") ;

    IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();

    ByteArrayOutputStream lReport = lCtrlProv.ExStampaProvvedimento(lEveMod, lFasEsteso.getFascicoloSige().getIdFascicoloSige(), getCodUfficioUtenteConnesso(), super.getUtenteConnesso() );

   //Prepara la pagina di destinazione
   if (lReport != null)
       setRequestAttribute("report", lReport);
   else
       throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun documento è stato generato!");

    setRequestAttribute("ProvvEvento", lProEveMod);


    return IWebConstants.PG_DOWNLOAD;
    //return IWebConstants.PG_DOWNLOAD_NEW;
  }

}