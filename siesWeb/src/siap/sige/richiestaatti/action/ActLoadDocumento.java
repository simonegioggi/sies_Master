package siap.sige.richiestaatti.action;


import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActLoadDocumento</p>
 * <p>Description: Carica il Documento BLOB </p>
 * <p>Copyright: Copyright (c) 2002</p>
 */

public class ActLoadDocumento extends ActionSiap
  implements ICostantiEvento
{

   public String processRequest() throws Exception
    {
    EventoModel lEveMod = new EventoModel();
    
    BigDecimal lIdProvvedimento = null;
    lIdProvvedimento = getRequestBigDecimalParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);

// Ricerca Provvedimento dalla chiave
    IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
	ProvvedimentoSigeEventoModel lProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);

	setRequestAttribute("ProvvedimentoEvento", lProvEvento);

	BigDecimal lId = lProvEvento.getEventoNotifica().getEvento().getIdEvento();

    //lEveMod.setIdEvento( getRequestBigDecimalParameter( CAMPO_ID_EVENTO) );
	lEveMod.setIdEvento(lId);

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExGetDocumento( lEveMod );

    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);
    //setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
   }
}
