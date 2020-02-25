package siap.sige.richiestaatti.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.richiestaatti.controller.IRichiestaAttiSige;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.util.SIGELookupRemote;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.richiestaatti.action.ActInserisciRicAtti;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActInserisciSollecito extends ActInserisciRicAtti implements ICostantiRichiestaAtti
{
/**
  * Azione di Stampa del Sollecito
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
    // Preleva dalla sessione i dati dell'utente connesso.
    String lCodiceOperatore = getCodUtenteConnesso();
    String lCodiceUfficio   = getCodUfficioUtenteConnesso();
    DocumentoAllegatoModel lDocAMod = null;
    DocumentoAllegatoModel lDocOutMod = null;
    EventoNotificaModel lEveMod = new EventoNotificaModel();

    // Dati ufficio connesso
    UfficioModel lUfficio     = getUfficioUtenteConnesso();
    
 // Fascicolo Sige Esteso in sessione.
    FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
 
    // Chiama il controller di Evento.
    IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
    lEveMod = lCtrlEve.ExRicercaEventoNotificaByKey(lIdEvento);

    

    //Si Recupera l'utente dalla sessione.
    UtenteModel lUtenteMod = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    
    
 // Setto il Model DocumentoAllegato
    lDocAMod = new DocumentoAllegatoModel();
    lDocAMod.setEveIdEvento(lIdEvento);
    lDocAMod.setDataEmissione(DateUtils.getSysDate());
    lDocAMod.setCodTipoDocumento("05"); // Codifica di COD_TIPO_DOCUMENTO_ALLEGATO = Atto Ric. Istruttoria
    lDocAMod.setFlagDocumentoRegistrato("N");
    lDocAMod.setCodUfficioInserimento(lCodiceUfficio);
    lDocAMod.setCodOperatoreInserimento(lCodiceOperatore);
    lDocAMod.setDataInserimento(DateUtils.getSysDate());
    lDocAMod.setTemIdTemplate("SIGE_IS_021");
   
    // Chiama il controller di richiestaatti.
    IRichiestaAttiSige lCtrlAtti = SIGELookupRemote.getRichiestaAttiSigeRemote();
    lDocOutMod = lCtrlAtti.ExInserisciSollecito(lEveMod,lDocAMod,lUfficio,lUtenteMod,lFasEsteso);
    
/*


    String lId = getRequestStringParameter( ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO);

    DocumentoAllegatoModel lDAMod = new DocumentoAllegatoModel();
    // chiama il controller di Documento Allegato.
    IDocumentoAllegato lCtrlDA = SIUSLookupRemote.getDocumentoAllegatoRemote();
    lDAMod = lCtrlDA.ExRicercaDocumentoAllegatoByKey(new BigDecimal(lId));

    //lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
    lDAMod.setIdDocumentoAllegato(new BigDecimal(lId));

    UfficioModel lUff = new UfficioModel(this.getUfficioUtenteConnesso());

    lDAMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lDAMod.setCodUfficioAggiornamento(lUff.getCodUfficio());
    lDAMod.setDataAggiornamento(DateUtils.getSysDate());
    lDAMod.setFlagDocumentoRegistrato("N");

    lDAMod.setDescrUfficioAggiornamento(lUff.getDescrTipoUfficio());

    IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumentoAllegato(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),lDAMod, lUff.getCodUfficio());

    setRequestAttribute("documentoAllegato", lDAMod);
    setRequestAttribute("report", lReport);


    return IWebConstants.PG_DOWNLOAD;
*/
    //IDocumentoAllegato lCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
    //ByteArrayOutputStream lReport = lCtrl.ExGetDocumentoByKey(lDocOutMod.getIdDocumentoAllegato() );
    
    
    IStampaSige ctrlStampa=SIGELookupRemote.getStampaRemote();
    ByteArrayOutputStream lReport=ctrlStampa.ExStampaSollecito(lEveMod, lFasEsteso, super.getUtenteConnesso());
    ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lReport.toByteArray());
    lDocOutMod.setDocBlobIn(lByteArrayInput);
    lCtrlAtti.ExModificaSollecito(lDocOutMod);
    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);
    return IWebConstants.PG_DOWNLOAD;

  }

}