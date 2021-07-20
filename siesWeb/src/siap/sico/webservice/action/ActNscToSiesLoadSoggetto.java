package siap.sico.webservice.action;

import it.mig.sies.type.ANAGRAFICADocument;
//import org.apache.axis.encoding.Base64;
import it.mig.sies.type.CHIAVIDocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.webservice.controller.IWebServices;
import siap.siep.SIEPException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

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
          // INIZIO: MEV_21 (avvocati)
          /*
          if (adatiAnagrafica.getDATIANAGRAFICI().getCODILUOGONASCITA() != null)
          {
              CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE", adatiAnagrafica.getDATIANAGRAFICI().getCODILUOGONASCITA());
              lCodComuneNascita = lCodiciSIESNSCModel.getCoSies().trim();
          }
          lSoggettoModel.setCodComuneNascita(lCodComuneNascita);
          */
          siesLogger.debug("Procede a decodificare il comune di nascita");
          if (adatiAnagrafica.getDATIANAGRAFICI().getCODILUOGONASCITA() != null)
          { 
        	  siesLogger.debug("Codice da NSC = "+adatiAnagrafica.getDATIANAGRAFICI().getCODILUOGONASCITA());
        	  Vector <CodiciSiesNscModel> listaComuni = DecodificaComune("COMUNE", adatiAnagrafica.getDATIANAGRAFICI().getCODILUOGONASCITA());
        	  if (listaComuni.size()==1) {
        		  lCodComuneNascita = listaComuni.elementAt(0).getCoSies().trim();
        		  siesLogger.debug("Trovato un solo comune decodificato con codice SIES = "+lCodComuneNascita);
        	  } else {
        		  // Trovati più comuni 
        		  siesLogger.debug("Trovati più comuni ("+listaComuni.size()+") testo la data di nascita per decidere quale comune usare...");
        		  Date dataNascita = lSoggettoModel.getDataNascita();
        		  siesLogger.debug("dataNascita da NSC: "+dataNascita);
        		  if (dataNascita==null && lSoggettoModel.getAnnoNascita()!=null)
        		  {
        			  siesLogger.debug("dataNascita null provo a ricostruirla con anno e mese");
        			  dataNascita = DateUtils.getDate(lSoggettoModel.getAnnoNascita().toString()
        					  , lSoggettoModel.getMeseNascita()!=null ? lSoggettoModel.getMeseNascita().toString() : "01"
        					  , "01"); 
        			  siesLogger.debug("dataNascita calcolata: "+dataNascita);
        		  }
        			  
        		  if (dataNascita!=null) {
        			  siesLogger.debug("dataNascita disponibile ("+dataNascita+") ciclo sui comuni trovati...");
        			  // Ho una data di nascita, vera o calcolata la uso mCoVal3
        			  Date minDataFineValidita = DateUtils.getDate("31/12/9999","dd/MM/yyyy");
        			  for (int kk = 0; kk<listaComuni.size(); kk++) {
        				  CodiciSiesNscModel comuneNascita = listaComuni.elementAt(kk);
        				  siesLogger.debug("comune ("+comuneNascita.getCoSies().trim()+","+comuneNascita.getCoVal3()+" )");
        				  Date dataFineValidita = DateUtils.getDate(comuneNascita.getCoVal3(), "yyyy-mm-dd");
        				  if (dataFineValidita!=null) 
        				  {
        					  if (   DateUtils.isLower(dataNascita, dataFineValidita) 
        						  && DateUtils.isLower(dataFineValidita, minDataFineValidita) 
        						 ) 
        					  {        						
        					    minDataFineValidita = dataFineValidita;        					  
        					    lCodComuneNascita=comuneNascita.getCoSies().trim();
        					    siesLogger.debug("comune trovato (cod, mindatafine)=("+lCodComuneNascita+","+minDataFineValidita+")");
        					  }
        				  }
        				  else {
        					  // mi trovo sul comune valido. Potrebbe comunque essere quello buono se non è stato 
        					  // ancora assegnato un comune non valido
        					  if ("-".equals(lCodComuneNascita)) {
        					    lCodComuneNascita=comuneNascita.getCoSies().trim();
        					    // minDataFineValidita = è ancora 31/12/9999
        					  }
        				  }
        			  }        			  
        		  }
        		  else {
        			  // non ho modo di determinare la data di nascita. 
        			  // Prendo il comune con data fine validita non valorizzata
        			  for (int kk = 0; kk<listaComuni.size(); kk++) {
        			    CodiciSiesNscModel comuneNascita = listaComuni.elementAt(kk);
        			    if (comuneNascita.getCoVal3()==null || "".equals(comuneNascita.getCoVal3())) {
        			    	lCodComuneNascita=comuneNascita.getCoSies().trim();
        			    }
        			  }
        		  }
        	  }        	  
          }
          
          lSoggettoModel.setCodComuneNascita(lCodComuneNascita);
          //FINE: MEV_21
          
          
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