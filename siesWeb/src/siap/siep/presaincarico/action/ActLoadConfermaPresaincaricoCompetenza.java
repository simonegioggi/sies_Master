package siap.siep.presaincarico.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.log.LogF3B;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * <p>Title: ActLoadConfermaPresaincaricoCompetenza</p>
 * Action che carica il dettagli con più unformazioni e consente di procedere alla
 * presa in carico effettiva
 */
public class ActLoadConfermaPresaincaricoCompetenza extends ActionSiap implements ICostantiPresaincarico
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws Exception
  {
    
    BigDecimal lIdIstruttoria = null;
    if(!isRequestParameterNullObj (ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)){
      lIdIstruttoria = this.getRequestBigDecimalParameter (ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
      
      IIstruttoriaCumulo lIstrCumCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
      IstruttoriaCumuloModel lIstruttoriaCumuloModel = lIstrCumCtrl.ExRicercaIstruttoriaCumuloById(lIdIstruttoria);

      setRequestAttribute("IstruttoriaCumulo", lIstruttoriaCumuloModel);    

      Vector <TitoloCumulatoModel> lListaTitoliInIstruttoria = null;        
      lListaTitoliInIstruttoria = lIstrCumCtrl.ExRicercaTitoliByIstruttoria (lIdIstruttoria);
      setRequestAttribute("ListaTitoliInIstruttoria", lListaTitoliInIstruttoria);    
      
    }

    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
  
    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
    this.setRequestAttribute("Messaggio", lMess);
    siesLogger.debug("--XX-- ActLoadConfermaPresaincaricoCompetenza - Messaggio da Prendere in Carico = "+lMess);

    BigDecimal lChiaveAnno    = lMess.getChiaveAnnoFasCumulante();
    BigDecimal lChiaveProg    = lMess.getChiaveProgrFasCumulante();
    String     lChiaveUfficio = lMess.getChiaveUfficioFasCumulante();
    String Messa = "";  

    if (lChiaveAnno != null && lChiaveProg != null && lChiaveUfficio!=null)
    {       
      //  ricerco il fascicolo cumulante
      FascicoloSiepModel aFas = new FascicoloSiepModel();
      aFas.setChiaveAnno    (lMess.getChiaveAnnoFasCumulante());
      aFas.setChiaveProgr   (lMess.getChiaveProgrFasCumulante());
      aFas.setChiaveUfficio (lMess.getChiaveUfficioFasCumulante());
        
      IFascicoloSiep lCrtlFas = SIEPLookupRemote.getFascicoloSiepRemote();
      FascicoloSiepModel mFas = lCrtlFas.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(aFas);

      if(mFas==null)
      {
        Messa = "Il Procedimento "+lMess.getChiaveAnnoFasCumulante()+"/"+lMess.getChiaveProgrFasCumulante()+
                " indicato da "+ lMess.getDescrUfficioMittente()+" di "+lMess.getDescrSedeUfficioMittente()+" come competente all'esecuzione, non è presente nel sistema!";
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
    
  
    //==========================================================================
    // OTTIMIZZAZIONE BLOB
    // Se il fascicolo trasmesso è della stessa BDI, non è stato inserito nel 
    // Messaggio (Campo blob) per contenere lo spazio. Recupero i dati direttamente
    // dalla BDI

    DettaglioFascicoloModel lDettaglio = null;

    UfficioModel lUfficioFascicoloRicevuto = getUfficioByCodUfficio (lMess.getChiaveUfficioSiep());
    if (lUfficioFascicoloRicevuto.getCodDistretto().equals(getCodDistrettoUtenteConnesso())) {
      siesLogger.debug("Fascicolo da Cumulare della stessa BDI, recupero i dati direttamete dalla Base DATI");
      
      IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
      FascicoloSiepModel lFasModel = new FascicoloSiepModel();
      lFasModel.setChiaveAnno    (lMess.getChiaveAnnoSiep());
      lFasModel.setChiaveProgr   (lMess.getChiaveProgrSiep());
      lFasModel.setChiaveUfficio (lMess.getChiaveUfficioSiep());
      
      lFasModel = lCtrlFasc.ExRicercaFascicoloSiepByProgrAnnoCodUfficio (lFasModel);
      
      if (lFasModel!=null)
        lDettaglio = lCtrlFasc.ExDettaglioFascicoloSiep(lFasModel.getIdFascicoloSiep());
      else {
        // Errore improbabile da gestire        
      }
    }
    else 
    {
      siesLogger.debug("Fascicolo da Cumulare proveniente da fuori Distretto, leggo il BLOB del MESSAGGIO");
      ParserMessage lParser = new ParserMessage(lMess.getTreeModel());
      
      lDettaglio = lParser.getDettaglioFascicoloSiep();
    }
    
    
    //FascicoloSiepModel lFasModel = new FascicoloSiepModel();    
//    if (lParser.getDettaglioFascicoloSiep().getFascicoloSiep().getSoggetto()!=null)
//      lFasModel.setSoggetto(lParser.getDettaglioFascicoloSiep().getFascicoloSiep().getSoggetto());
//    else 
//      throw new SIUSException( SIUSException.USER_MESSAGE, "Errore nella Ricezione del Soggetto. <BR>Rivolgersi all'amministratore di sistema!" );

    this.setRequestAttribute("dettaglioFasSIEP", lDettaglio);
    this.setRequestAttribute("NoProcedimento", Messa);

    return PG_DETTAGLIO_CONFERMA_PRESAICARICO_COMPETENZA;
  }
}