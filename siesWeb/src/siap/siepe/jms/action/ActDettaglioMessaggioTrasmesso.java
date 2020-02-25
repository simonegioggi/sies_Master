package siap.siepe.jms.action;

import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.web.ActionSiap;
import siap.siepe.SIEPEException;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.relazione.model.RelazioneModel;
import siap.siepe.richiesta.model.RichiestaModel;

/**
 * <p>Title: ActDettaglioMessaggioTrasmesso</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActDettaglioMessaggioTrasmesso extends ActionSiap implements ICostantiSiepeJMS
{
  public String processRequest() throws Exception
  {
    //DI fatto questa classe dovrebbe solo andare a vedere i risultati sulla
    // tabella DI MESSAGGIO.
    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();

    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
    MessaggioModel lMessCorr =  lCrtl.ExRicercaMessaggioByCorrelationId(lMess.getIdMessaggio().toString());

    lMess.setMessaggioCorrelato(lMessCorr);

    this.setRequestAttribute("Messaggio", lMess);

    ParserMessage lParser;

    // Se la classe ParserMessage "guarda" i model inclusi nel blob del messaggio con un formato diverso, il parsing Fallisce!
    if (lMess.getTreeModel()!= null)
      lParser = new ParserMessage(lMess.getTreeModel());
    else
      throw new SIEPEException(SIEPEException.USER_MESSAGE, "Messaggio non conforme al Modello attuale: Rivolgersi all'amministratore di sistema!");

    // Parsing del fascicolo SIEPE.
    if (lParser.getFascicoloSiepeEsteso()!=null)
      this.setSessionAttribute("FascicoloSiepeEsteso", lParser.getFascicoloSiepeEsteso());
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore! Procedimento SIEPE esteso non conforme al Modello attuale: Rivolgersi all'amministratore di sistema!" );

    // Parsing dell'ATTIVITA.
    if (lMess.getCodTipoOperazione().compareTo(TRASFERIMENTO_ATTIVITA)==0)
    {
      if (lParser.getAttivita()!=null)
      {
        AttivitaModel lAtt = lParser.getAttivita() ;
        this.setRequestAttribute("lAttivita", lAtt);
      }
      else
        throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore! Attività SIEPE non conforme al Modello attuale: Rivolgersi all'amministratore di sistema!" );
    }

    // Parsing della RICHIESTA UEPE.
    if (lMess.getCodTipoOperazione().compareTo(TRASFERIMENTO_RICHIESTA_UEPE)==0)
    {
      if (lParser.getRichiesta()!=null)
      {
        RichiestaModel lRich = lParser.getRichiesta() ;
        this.setRequestAttribute("lRichiesta", lRich);
      }
      else
        throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore! Richiesta SIEPE non conforme al Modello attuale: Rivolgersi all'amministratore di sistema!" );
    }

    // Parsing della RELAZIONE UEPE.
    if (lMess.getCodTipoOperazione().compareTo(TRASFERIMENTO_RELAZIONE_UEPE)==0)
    {
      if (lParser.getRelazione()!=null)
      {
        RelazioneModel lRel = lParser.getRelazione() ;
        this.setRequestAttribute("lRelazione", lRel);
      }
      else
        throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore! Relazione SIEPE non conforme al Modello attuale: Rivolgersi all'amministratore di sistema!" );
    }

    setRequestAttribute("FlagFasSiepe", "SI");
    return PG_DETTAGLIO_MESSAGGIO_TRASMESSO;
  }
}
