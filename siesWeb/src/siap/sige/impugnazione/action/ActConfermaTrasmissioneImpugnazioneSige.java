package siap.sige.impugnazione.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sius.SIUSException;
import siap.sius.jms.controller.ITrasmissioneJMS;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActConfermaTrasmissioneImpugnazioneSige</p>
 * <p>Description: L'Azione impacchetta i dati da inviare
 * nel messaggio, poi attiva l'invio del messaggio stesso ai destinatari.</p>
 */
public class ActConfermaTrasmissioneImpugnazioneSige extends ActionSiap implements ICostantiImpugnazioneSige, ICostantiJMS
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    
    String lIdImpugnazioneSige = getRequestStringParameter(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE);
    
    boolean inviato = false;  // flag di controllo invio messaggio.

    // Si Prepara la trasmissione dell'Impugnazione.
    String[] lTipoUff = getRequestStringParameters(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
    String[] lSedeUff = getRequestStringParameters(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO);

    // Fascicolo SIGE in sessione.
    FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

    // Dati BDI mittente.
    UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("BDI MIttente = " +lBDIMittente );
    String lCodiceUfficio = new String();
    UfficioModel lLocal = new UfficioModel();
    UfficioModel lBDI   = new UfficioModel();

    if(lTipoUff[0].trim().compareTo("-") !=0 )
    {
      lCodiceUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUff[0], lSedeUff[0]);
      lLocal = getUfficioByCodUfficio(lCodiceUfficio);
      lBDI = getUfficioByCodUfficio(lLocal.getCodDistretto());

      ITrasmissioneJMS lCtrlMess = SIUSLookupRemote.getTrasmissioneJMS();
      MessaggioModel lMessage = lCtrlMess.getMessageForOpposizioneRicorso(lEveId, new BigDecimal(lIdImpugnazioneSige));

      lMessage.setDescrBdiDestinataria(lBDI.getDescrComune());
      lMessage.setCodBdiDestinataria(lBDI.getCodUfficio());
      lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
      lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
      lMessage.setCodUfficioDestinatario(lCodiceUfficio);
      lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
      lMessage.setCodTipoMessaggio(RICHIESTA);
      lMessage.setCodTipoOperazione(TRASFERIMENTO_OPPOSIZIONE_RICORSO);
      lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
      lMessage.setDataInvio(DateUtils.getSysDate());

     if(lFasSigeEsteso.getSoggetto()!=null)
      {
        if(lFasSigeEsteso.getSoggetto().getNome()!=null)
          lMessage.setNomeSoggetto(lFasSigeEsteso.getSoggetto().getNome());
        if(lFasSigeEsteso.getSoggetto().getCognome()!=null)
          lMessage.setCognomeSoggetto(lFasSigeEsteso.getSoggetto().getCognome());
        if(lFasSigeEsteso.getSoggetto().getDataNascita()!=null)
          lMessage.setDataNascita(lFasSigeEsteso.getSoggetto().getDataNascita());
        if(lFasSigeEsteso.getSoggetto().getCodComuneNascita()!=null)
          lMessage.setCodComuneNascita(lFasSigeEsteso.getSoggetto().getCodComuneNascita());
        if(lFasSigeEsteso.getSoggetto().getCodStatoNascita()!=null)
          lMessage.setCodStatoNascita(lFasSigeEsteso.getSoggetto().getCodStatoNascita());
      }
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug ("MESSAGGIO DA SPEDIRE A " + lBDI.getDescrComune() + " : " + lMessage.toString());

      SIAPSender lSender = new SIAPSender();
      lSender.send(lMessage);
      inviato = true;
    }

    if (inviato == false)
      throw new SIUSException(SIUSException.USER_MESSAGE, "Attenzione: selezionare almeno un destinatario! " );

    setRequestAttribute("IdEvento", lEveId.toString());

    //Prepara la "pagina" di destinAction.
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setParameter("IdEvento", lEveId.toString());
    lRedirigi.setParameter("IdImpugnazione", lIdImpugnazioneSige);

    String strMessage="";
    strMessage = "Trasmissione Opposizione/Ricorso sottomessa al Sistema!";
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, strMessage );
    lRedirigi.setAction( "siap.sige.impugnazione.action.ActLoadDettaglioImpugnazioneSige" );
 
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE;
  }
}