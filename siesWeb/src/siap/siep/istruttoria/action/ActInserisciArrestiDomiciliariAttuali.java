package siap.siep.istruttoria.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.notifica.model.NotificaModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;


/**
 * <p>Title: ActInserisciArrestiDomiciliariAttuali</p>
 * <p>Description: ActInserisciArrestiDomiciliariAttuali</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
  * @version 1.0
 */
public class ActInserisciArrestiDomiciliariAttuali extends ActionSiap implements ICostantiIstruttoria
{
   public String processRequest() throws Exception
   {
           //controllo comune e ufficio
          ComuneModel lComune =  this.getCodComuneByDescrFlagVal(getRequestStringParameter(ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO));
           //String lCodice = getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(ICostantiIstruttoria.AUTORITA_DESTINATARIO),getRequestStringParameter(ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO) );
          //*************************************


           EventoNotificaModel lEve = new EventoNotificaModel();

           lEve.getEvento().setCodTipoEvento("05"); //Tipo Evento = RIchiesta Istruttoria
           lEve.getEvento().setCodTipoProvvedimento("-"); //Tipo Provvedimento = Ordinanza

          //Codice motivo da CG_REF_CODES....
          // IDecodifiche lDec = SICOLookupRemote.getDecodificheRemote();
           //DecodificheModel lDecMod = lDec.ExRicercaDecodificheByHighValue("OE-DET-QC");
           lEve.getEvento().setCodMotivo("0052");

           FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
           lEve.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );

           Date lDataEmissione = getRequestDateParameter( ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,  ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,  ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE );
           lEve.getEvento().setDataEmissione( lDataEmissione );

           UfficioModel lUff = this.getUfficioUtenteConnesso();

           lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
           lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
           lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
           lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
           lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
           lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
           lEve.getEvento().setCodEsito("-");
           lEve.getEvento().setCodLuogoDestinatario("-");
           lEve.getEvento().setCodTipoUfficioDestinatario("-");
           
		   // Se vengo da IstruttoriaCUMULO
		   if(!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) &&
			   getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) != null &&
			  !getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO).equals("") )
		   {
			   lEve.getEvento().setIstruidIstruttoriaCumulo(new BigDecimal(getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)));
		   }

           //lEve.getEvento().setFlagDocumentoRegistrato("N");
//***************************************************

           NotificaModel lNotifiche[] = new NotificaModel[1];
           NotificaModel lNot = new NotificaModel();

           lNot.setCodTipoNotifica("N");
           lNot.setDataInvio(lDataEmissione);
           lNot.setCodEsito("-");
           lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
           lNot.setDataInserimento(DateUtils.getSysDate());
           lNot.setCodUfficioInserimento(lUff.getCodUfficio());

           String lTestoNote = getRequestStringParameter(ICostantiIstruttoria.CAMPO_NOTE);

           AutoritaEsternaModel lAutEstMod = new AutoritaEsternaModel();

           lAutEstMod.setCodTipoAutorita(getRequestStringParameter(ICostantiIstruttoria.AUTORITA_DESTINATARIO) );
           lAutEstMod.setCodSede(lComune.getCodComune());
           lAutEstMod.setCodOperatoreInserimento(getCodUtenteConnesso());
           lAutEstMod.setDataInserimento(DateUtils.getSysDate());
           lAutEstMod.setCodUfficioInserimento(lUff.getCodUfficio());
           lNot.setAutoritaEsterna(lAutEstMod);


           lNot.setNote(lTestoNote);

           lNotifiche[0] = lNot;

       //Inserisco l'array di Notifiche nell'Evento
           lEve.setNotifiche(lNotifiche);
          // lEve.getEvento().setFlagDocumentoRegistrato("N");

           IEvento lCtrl = SICOLookupRemote.getEventoRemote();
           EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

           String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
               "=siap.siep.istruttoria.action.ActDettaglioArrestiDomiciliariAttuali&" +
               ICostantiEvento.CAMPO_ID_EVENTO + "=" +
               lRetModel.getEvento().getIdEvento() + "&modalita=I";


         return lPage;


   }

}