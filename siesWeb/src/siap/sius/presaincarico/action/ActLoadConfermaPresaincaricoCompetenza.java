package siap.sius.presaincarico.action;

import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;

/**
 * <p>Title: ActLoadConfermaPresaincaricoCompetenza</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * 
 * 
 * @author not attributable
 * @version 1.0
 * @deprecated Funzione SIEP spostata nel package SIEP
 */
public class ActLoadConfermaPresaincaricoCompetenza extends ActionSiap implements ICostantiPresaincarico
{
  public String processRequest() throws Exception
  {

    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
  
    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
    this.setRequestAttribute("Messaggio", lMess);

    BigDecimal Ann = lMess.getChiaveAnnoFasCumulante();
    BigDecimal Prog = lMess.getChiaveProgrFasCumulante();
    String Messa = "";  

    if (Ann != null && Prog != null)
    {       
      //  ricerco il fascicolo cumulante
      FascicoloSiepModel aFas = new FascicoloSiepModel();
      aFas.setChiaveAnno(lMess.getChiaveAnnoFasCumulante());
      aFas.setChiaveProgr(lMess.getChiaveProgrFasCumulante());
      aFas.setChiaveUfficio(lMess.getCodUfficioDestinatario());
        
      IFascicoloSiep lCrtlFas = SIEPLookupRemote.getFascicoloSiepRemote();
      FascicoloSiepModel mFas = lCrtlFas.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(aFas);

      if(mFas==null)
      {
        Messa = "Il Procedimento "+lMess.getChiaveAnnoFasCumulante()+"/"+lMess.getChiaveProgrFasCumulante()+
                " inviato da "+ lMess.getDescrUfficioMittente() +" non è presente nel sistema!";
      }
      else
      {
        //pena residua per dettaglio fascicolo
        IPenaResidua lCrtlP = SIEPLookupRemote.getPenaResiduaRemote();
        PenaResiduaModel mPena = lCrtlP.ExRicercaPenaResiduaCorrenteByFascicoloSiep(mFas.getFasSieIdFascicoloSiep());
        this.setRequestAttribute("penaresiduaCumulante", mPena);  
      }
      this.setRequestAttribute("fascicoloCumulante", mFas);
    }
    
  
    ParserMessage lParser = new ParserMessage(lMess.getTreeModel());
    FascicoloSiepModel lFasModel = new FascicoloSiepModel();    
    
    if (lParser.getDettaglioFascicoloSiep().getFascicoloSiep().getSoggetto()!=null)
      lFasModel.setSoggetto(lParser.getDettaglioFascicoloSiep().getFascicoloSiep().getSoggetto());
    else 
      throw new SIUSException( SIUSException.USER_MESSAGE, "Errore nella Ricezione del Soggetto. <BR>Rivolgersi all'amministratore di sistema!" );

    this.setRequestAttribute("dettaglioFasSIEP", lParser.getDettaglioFascicoloSiep());
    this.setRequestAttribute("NoProcedimento", Messa);

    //passo il parametro dell'action di presa in carico in base alla BDI di appartenenza
    if( !lMess.getCodBdiMittente().equalsIgnoreCase(lMess.getCodBdiDestinataria())) {
      this.setRequestAttribute("actionConferma",  "siap.sius.presaincarico.action.ActConfermaPresaInCaricoAttiSiep");
    }
    else  {
      this.setRequestAttribute("actionConferma", "siap.sius.presaincarico.action.ActConfermaPresaInCaricoAttiSiepStessaBDI");
    }
    
    

    return PG_DETTAGLIO_CONFERMA_PRESAICARICO_COMPETENZA;
  }
}