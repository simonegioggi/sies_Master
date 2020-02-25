package siap.sico.webservice.action;

import f3b.util.DateUtils;
import it.mig.sies.type.CHIAVIDocument;
import it.mig.sies.type.DATIOPERAZIONEDocument;
import it.mig.sies.type.DATIRISPOSTATRASFERIMENTODocument;
import it.mig.sies.type.ESITODocument;
import it.mig.sies.type.TRASFERIMENTODocument;

import java.math.BigDecimal;

import siap.sico.trasmissione.controller.ITrasmissioni;
import siap.sico.trasmissione.model.TrasmissioniModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.webservice.controller.WebServicesController;

public class ActRispostaDatiFascicoloCancellato
{
    public String processRequest(String flussoXmlRisposta,BigDecimal lAnnoFascicolo,BigDecimal lNumeroFascicolo,UtenteModel lUteMod) throws Exception
    {
      
      //  Caricamento del flusso xml di Risposta
      TRASFERIMENTODocument lDocNsc = TRASFERIMENTODocument.Factory.parse(flussoXmlRisposta);
      TRASFERIMENTODocument.TRASFERIMENTO rootRisp = lDocNsc.getTRASFERIMENTO();
      DATIRISPOSTATRASFERIMENTODocument.DATIRISPOSTATRASFERIMENTO datiRispostaTrasf =  rootRisp.getDATIRISPOSTATRASFERIMENTO();
      CHIAVIDocument.CHIAVI datiChiavi = datiRispostaTrasf.getCHIAVI();
      ESITODocument.ESITO datiEsito = datiRispostaTrasf.getESITO();
      DATIOPERAZIONEDocument.DATIOPERAZIONE datiOperazione = datiRispostaTrasf.getDATIOPERAZIONE();
      if (datiEsito. getCODICE().toString().equals("0"))  // OK
      {   // Disaccoppiamento  delle chiavi NSC su DB SIES e Registrazione Dati Trasmissione  
          WebServicesController lWebServicesController = new WebServicesController();
          lWebServicesController.ExCancellazioneChiaviNsc(datiChiavi, datiOperazione, datiEsito,lAnnoFascicolo,lNumeroFascicolo,lUteMod);
      }
      else
      {
          RegistrazioneTrasmissione(datiChiavi,datiOperazione,datiEsito,lAnnoFascicolo,lNumeroFascicolo,lUteMod);
      }
      
      return datiEsito. getCODICE().toString();
    }
    
    private void RegistrazioneTrasmissione(CHIAVIDocument.CHIAVI adatiChiavi, DATIOPERAZIONEDocument.DATIOPERAZIONE adatiOperazione, ESITODocument.ESITO adatiEsito,BigDecimal lAnnoFascicolo,BigDecimal lNumeroFascicolo,UtenteModel lUteMod) throws Exception
    {
        
        TrasmissioniModel lTrasmissioniModel = new TrasmissioniModel();
        
        lTrasmissioniModel.setTipoTrasmissione("01"); // Provvedimenti Principali
        lTrasmissioniModel.setDataTrasmissione(DateUtils.getSysDate());
        lTrasmissioniModel.setEsitoTrasmissione(adatiEsito.getCODICE().toString());
        lTrasmissioniModel.setCodErrore("");
        lTrasmissioniModel.setTipoOperazione(adatiOperazione.getOPERAZIONE().toString()) ;
        lTrasmissioniModel.setDestinazione("NSC");
        if (adatiEsito. getCODICE().toString().equals("0"))
        {  
            lTrasmissioniModel.setChiaveNscProv(null);
            lTrasmissioniModel.setChiaveNscSogg(null);
            lTrasmissioniModel.setChiaveSiesFasc(null);
            lTrasmissioniModel.setChiaveSiesSogg(null);
        }
        else
        {  
            lTrasmissioniModel.setChiaveNscProv(new BigDecimal(adatiChiavi.getKPNSC()));
            lTrasmissioniModel.setChiaveNscSogg(new BigDecimal(adatiChiavi.getKANSC()));
            lTrasmissioniModel.setChiaveSiesFasc(new BigDecimal(adatiChiavi.getKPSIES()));
            lTrasmissioniModel.setChiaveSiesSogg(new BigDecimal(adatiChiavi.getKASIES()));
      }  
        lTrasmissioniModel.setChiaveAnno(lAnnoFascicolo);
        lTrasmissioniModel.setChiaveProgr(lNumeroFascicolo);
        
        lTrasmissioniModel.setCodOperatoreInserimento(lUteMod.getUserId());
        lTrasmissioniModel.setDataInserimento(DateUtils.getSysDate());
        lTrasmissioniModel.setCodUfficioInserimento(lUteMod.getUfficioUtente().getCodUfficio());
        
        //    Scrivo TRASMISSIONE
        ITrasmissioni lCtrlTrasmissioni = SICOLookupRemote.getTrasmissioniRemote();
        lCtrlTrasmissioni.ExInserisciTrasmissioni(lTrasmissioniModel);
        
    } 
    
}
