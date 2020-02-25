package siap.siep.penaaccessoria.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActStampaProvvedimentoPA extends ActionSiap implements ICostantiPenaAccessoria
{
  public String processRequest() throws F3BException
  {
    BigDecimal lIdEvento = getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO);

    UtenteModel lUtenteMod = this.getUtenteConnesso();

    EventoModel lEventoMod = new EventoModel();

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    lEventoMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

    if(lEventoMod == null)
      throw new F3BException(F3BException.USER_MESSAGE, "Evento non Registrato");

    //RICERCA TEMPLATE
    TemplateModel lTempMod = new TemplateModel();

    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();

    String lFlagTemplate = "1";
    // 26/09/2011 Se si elabora la "Esecuzione della Pena Accesssora" FlagTemplate = 1 TipoEvento = 16 Tipo_Provvedimento = 32 ) 
    // 						occorre valorizzare il parametro CodMotivo)
    if (lEventoMod.getCodTipoEvento().compareTo("16")==0  &&
    		lEventoMod.getCodTipoProvvedimento().compareTo("32")==0  &&
    		lFlagTemplate.compareTo("1")==0 )
    {
      lTempMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate( lEventoMod.getCodTipoEvento(),
          																																				lEventoMod.getCodTipoProvvedimento(),
          																																				lEventoMod.getCodMotivo(),
          																																				lFlagTemplate );
    } else {		
    	lTempMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate( lEventoMod.getCodTipoEvento(),
                                                                       lEventoMod.getCodTipoProvvedimento(),
                                                                       null,
                                                                       lFlagTemplate );
    }
    String lIdTemplate = "";
    lIdTemplate = lTempMod.getIdTemplate();

    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEventoMod.setDescrLuogoEmittente(lUtenteMod.getUfficioUtente().getDescrComune());
    lEventoMod.setDescrUfficioEmittente(lUtenteMod.getUfficioUtente().getDescrTipoUfficio());

    lEventoMod.setDataAggiornamento(DateUtils.getSysDate());
    lEventoMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
    lEventoMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
    lEventoMod.setFlagDocumentoRegistrato("N");

    lEveMod.setEvento(lEventoMod);
    lEveMod.setNomeTemplate(lIdTemplate);

    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta nella request

    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}
