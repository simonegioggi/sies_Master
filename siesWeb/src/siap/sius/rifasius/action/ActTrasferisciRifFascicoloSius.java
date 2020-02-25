package siap.sius.rifasius.action;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.jms.controller.ITrasmissioneJMS;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActTrasferisciRifFascicoloSius </p>
 * <p>Description: Trasferisce il Riferimento Fascicolo Sius </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActTrasferisciRifFascicoloSius extends ActionSiap implements ICostantiFascicoloSius, ICostantiJMS
{
  public String processRequest() throws Exception
  {
    // Si preleva dalla sessione il fascicolo GPModel appena iscritto/Letto.
    if(this.isSessionAttributeNullObj("fascicoloSiusGP"))
      throw new SIUSException(SIUSException.USER_MESSAGE,"fascicoloSiusGP non in sessione");
    FascicoloGPModel lFasGPMod = new FascicoloGPModel((FascicoloGPModel)getSessionAttribute("fascicoloSiusGP"));

    // Si preleva dalla sessione il fascicolo SIEP origine: quando si iscrive o si effettua una ricerca puntuale del F. Sius, il F. Siep viene posto in sessione.
    if(this.isSessionAttributeNullObj("fascicolo"))
      throw new SIUSException(SIUSException.USER_MESSAGE,"fascicolo SIEP non presente in sessione");
    FascicoloSiepModel lFasMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");

    ITrasmissioneJMS lCtrlMess = SIUSLookupRemote.getTrasmissioneJMS();
    MessaggioModel lMessage = lCtrlMess.getMessageForRiFaSius(lFasGPMod.getFascicoloSiusModel().getChiaveAnno(), lFasGPMod.getFascicoloSiusModel().getChiaveProgr(), lFasGPMod.getFascicoloSiusModel().getChiaveUfficio(), lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() );



    UfficioModel lBDIDestinataria = this.getUfficioByCodUfficio(lFasMod.getChiaveUfficio());
    UfficioModel lBDIMittente = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());

    lMessage.setDescrBdiDestinataria(lBDIDestinataria.getDescrComune());
    lMessage.setCodBdiDestinataria(lBDIDestinataria.getCodUfficio());

    lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio() );
    lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune() );
    lMessage.setCodUfficioDestinatario(lBDIDestinataria.getCodUfficio() );
    lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso() );
    lMessage.setCodTipoMessaggio(RICHIESTA);
    lMessage.setCodTipoOperazione(TRASFERIMENTO_RIF_FAS_SIUS);
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    lMessage.setDataInvio(DateUtils.getSysDate());

    //SETTA RIFERIMENTI FASCICOLO SIEP/SIUS
    lMessage.setChiaveAnnoSiep(lFasMod.getChiaveAnno());
    lMessage.setChiaveProgrSiep(lFasMod.getChiaveProgr());
    lMessage.setChiaveAnnoSius(lFasGPMod.getFascicoloSiusModel().getChiaveAnno());
    lMessage.setChiaveProgrSius(lFasGPMod.getFascicoloSiusModel().getChiaveProgr());


    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);

    setRequestAttribute(CAMPO_ID_FASCICOLO_SIUS, lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.sius.fascicolo.action.ActLoadDettaglioFascicolo" );
    lRedirigi.setParameter(CAMPO_ID_FASCICOLO_SIUS, lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString());
    //setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    // setta la risposta nella request
    // setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Procedimento Sius iscritto e trasmissione Riferimento Fascicolo Sius sottomessa al Sistema!");
    //return IWebConstants.PG_MESSAGE;
    return lRedirigi.toString();

  }
}