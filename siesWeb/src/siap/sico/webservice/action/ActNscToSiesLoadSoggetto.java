package siap.sico.webservice.action;

import it.mig.sies.type.ANAGRAFICADocument;
//import org.apache.axis.encoding.Base64;
import it.mig.sies.type.CHIAVIDocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.webservice.controller.IWebServices;
import siap.siep.SIEPException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

public class ActNscToSiesLoadSoggetto extends ActWsBase
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    private String mCodUfficio="";
  
    public ActNscToSiesLoadSoggetto(String aCodUfficio) 
    {
        mCodUfficio=aCodUfficio;
    }
    
    public SoggettoModel processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica, DATIUTENTEDocument.DATIUTENTE adatiUtente,CHIAVIDocument.CHIAVI aChiavi) throws Exception
    {
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.info("---------------------------------------------");
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.info("ActNscToLoadSoggetto");
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.info("---------------------------------------------");
          
          Integer lAnno, lMese, lGiorno;
          String lAnnoAppo,lMeseAppo,lGiornoAppo;
          String lCodComuneNascita="-", lCodStatoNascita="-";
          
          SoggettoModel lSoggettoModel = new SoggettoModel();
          
          lSoggettoModel.setKeySoggNsc(new BigDecimal(aChiavi.getKANSC()));
          
          lSoggettoModel.setCodFiscale(adatiAnagrafica.getDATIANAGRAFICI().getCODIFISCALE());
          lSoggettoModel.setCodCs(null);
          lSoggettoModel.setCodAfis(adatiAnagrafica.getDATIANAGRAFICI().getCODIIMPRONTADIGITALE());
          lSoggettoModel.setCognome(adatiAnagrafica.getDATIANAGRAFICI().getPERSCOGNOME());
          lSoggettoModel.setNome(adatiAnagrafica.getDATIANAGRAFICI().getPERSNOME());
          
          if (adatiAnagrafica.getDATIANAGRAFICI().getDATANASCITA() != null &&
              adatiAnagrafica.getDATIANAGRAFICI().getDATANASCITA().getANNO() != null &&
              !adatiAnagrafica.getDATIANAGRAFICI().getDATANASCITA().getANNO().equals(""))
          {
              lAnnoAppo = adatiAnagrafica.getDATIANAGRAFICI().getDATANASCITA().getANNO();
          }
          else
          {
              lAnnoAppo=null;
          }
          
          if (adatiAnagrafica.getDATIANAGRAFICI().getDATANASCITA() != null &&
              adatiAnagrafica.getDATIANAGRAFICI().getDATANASCITA().getMESE() != null &&
              !adatiAnagrafica.getDATIANAGRAFICI().getDATANASCITA().getMESE().equals(""))
          {
              lMeseAppo = adatiAnagrafica.getDATIANAGRAFICI().getDATANASCITA().getMESE();
          }
          else
          {
              lMeseAppo=null;
          }
          
          if (adatiAnagrafica.getDATIANAGRAFICI().getDATANASCITA() != null &&
              adatiAnagrafica.getDATIANAGRAFICI().getDATANASCITA().getGIORNO() != null &&
              !adatiAnagrafica.getDATIANAGRAFICI().getDATANASCITA().getGIORNO().equals(""))
          {
              lGiornoAppo = adatiAnagrafica.getDATIANAGRAFICI().getDATANASCITA().getGIORNO();
          }
          else
          {
              lGiornoAppo=null;
          }
          
          Date lDataNascita;
          BigDecimal lAnnoNascita;
          BigDecimal lMeseNascita;
          
          //  In caso di cittadino straniero la Data di nascita può essere parziale
          //   NSC non possiede l'informazione relativa alla Data di Nascita presunta o meno 
          if ((lAnnoAppo != null && !lAnnoAppo.equals("")) &&
              (lMeseAppo != null && !lMeseAppo.equals("")) &&
              (lGiornoAppo != null && !lGiornoAppo.equals("")))
          {  
              lAnno = new Integer(lAnnoAppo);
              lMese = new Integer(lMeseAppo);
              lGiorno = new Integer(lGiornoAppo);
              lDataNascita = DateUtils.getDate(lAnno.intValue(),lMese.intValue(), lGiorno.intValue());
              lSoggettoModel.setDataNascitaPresunta("N");
          } 
          else
          {
              lSoggettoModel.setDataNascitaPresunta("S");
              lDataNascita=null;
          }
          lSoggettoModel.setDataNascita(lDataNascita);
          
          if (lAnnoAppo != null && !lAnnoAppo.equals(""))
          {
              lAnnoNascita = new BigDecimal(lAnnoAppo); 
          }
          else
          {
              lAnnoNascita=null;
          }
          lSoggettoModel.setAnnoNascita(lAnnoNascita);
          
          if (lMeseAppo != null && !lMeseAppo.equals(""))
          {
              lMeseNascita = new BigDecimal(lMeseAppo);
          }
          else
          {
              lMeseNascita=null;
          }
          lSoggettoModel.setMeseNascita(lMeseNascita);
          
          // DECODIFICA CODICE LUOGO NASCITA
          if (adatiAnagrafica.getDATIANAGRAFICI().getCODILUOGONASCITA() != null)
          {
              CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE", adatiAnagrafica.getDATIANAGRAFICI().getCODILUOGONASCITA());
              lCodComuneNascita = lCodiciSIESNSCModel.getCoSies().trim();
          }
          lSoggettoModel.setCodComuneNascita(lCodComuneNascita);
          
          
          if (lSoggettoModel.getCodComuneNascita().equals("-"))
          {
              lSoggettoModel.setCodProvinciaNascita("-");
              lSoggettoModel.setCodComuneCasellario("-");
          }
          else
          { 
              ComuneModel lComuneModel = CercaProvincia_SedeGiudiziaria(lSoggettoModel.getCodComuneNascita());
              lSoggettoModel.setCodProvinciaNascita(lComuneModel.getCodProvincia());
              lSoggettoModel.setCodComuneCasellario(lComuneModel.getCodSedeGiudiziaria());
          }
            
          // DECODIFICA CODICE STATO DI NASCITA NASCITA
          if (adatiAnagrafica.getDATIANAGRAFICI().getCODISTATOESTERONAS() != null)
          {
              CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("NAZIONE", adatiAnagrafica.getDATIANAGRAFICI().getCODISTATOESTERONAS());
              lCodStatoNascita = lCodiciSIESNSCModel.getCoSies().trim();
          }
          lSoggettoModel.setCodStatoNascita(lCodStatoNascita);
          
          // NAZIONALITA 
          if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getFLAGCITTADINANZAISD() != null)
          {
              lSoggettoModel.setNazionalita(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getFLAGCITTADINANZAISD());
          }
          else
          {
              lSoggettoModel.setNazionalita("-");
          }  
          
          /*
          if (lSoggettoModel.getCodStatoNascita().equals("-"))
          {
              lSoggettoModel.setNazionalita("-");
          }
          else
          { 
              if (lSoggettoModel.getCodStatoNascita().equals("039"))
              {
                  lSoggettoModel.setNazionalita("I");
              }
              else
              {
                  lSoggettoModel.setNazionalita("E");
              }  
          } 
          */
          
          lSoggettoModel.setDescComuneNascitaEstero(adatiAnagrafica.getDATIANAGRAFICI().getDESCCOMUNEESTERO());
          lSoggettoModel.setPaternita(adatiAnagrafica.getDATIANAGRAFICI().getPERSPATERNITA());
          lSoggettoModel.setCognomeMadre(adatiAnagrafica.getDATIANAGRAFICI().getPERSCOGNOMEMADRE());
          lSoggettoModel.setNomeMadre(adatiAnagrafica.getDATIANAGRAFICI().getPERSNOMEMADRE());
          lSoggettoModel.setSesso(adatiAnagrafica.getDATIANAGRAFICI().getFLAGSESSOMF());
          lSoggettoModel.setAttoNascita(adatiAnagrafica.getDATIANAGRAFICI().getNUMEATTONASCITA());
          lSoggettoModel.setNote(adatiAnagrafica.getDATIANAGRAFICI().getDESCANNOTAZIONE());
          
          
          lSoggettoModel.setFlagPresenzaFascicolo("S");
          lSoggettoModel.setCodOperatoreInserimento("nsc-"+adatiUtente.getUSERNAME());
          lSoggettoModel.setDataInserimento(DateUtils.getSysDate());
          lSoggettoModel.setCodUfficioInserimento(mCodUfficio);
          lSoggettoModel.setCodOperatoreAggiornamento(null);
          lSoggettoModel.setDataAggiornamento(null);
          lSoggettoModel.setCodUfficioAggiornamento(null);
          
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.info("Cognome:"+lSoggettoModel.getCognome());
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.info("Nome:"+lSoggettoModel.getNome());
          
          return lSoggettoModel;
          
    } 
  
    private ComuneModel CercaProvincia_SedeGiudiziaria(String aCodIstatComuneNascita) throws Exception
    {
        IWebServices lCtrlComune = SICOLookupRemote.getWebServicesRemote();
        ComuneModel lComuneModel = new ComuneModel();
        try
        {
            lComuneModel = lCtrlComune.ExRicercaProvinciaSedeGiudiziaria(aCodIstatComuneNascita);
        }
        catch (SIEPException e)
        {
            throw e;
        }
        return lComuneModel;
    } 
}