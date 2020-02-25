package siap.sico.webservice.action;

import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.CHIAVIDocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

public class ActNscToSiesLoadFascicolo extends ActWsBase
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
private String mCodUfficio="";
  
    public ActNscToSiesLoadFascicolo(String aCodUfficio) 
    {
        mCodUfficio=aCodUfficio;
    }
    
    public FascicoloSiepModel processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica, DATIUTENTEDocument.DATIUTENTE adatiUtente, CHIAVIDocument.CHIAVI aChiavi) throws Exception
    {
          
        Integer lAnno, lMese, lGiorno;
        String lAnnoAppo,lMeseAppo,lGiornoAppo;
        BigDecimal annoCorrente;
        
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.info("---------------------------------------------");
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.info("ActNscToLoadFascicolo - TITOLO ESECUTIVO");
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.info("---------------------------------------------");
        
        FascicoloSiepModel lFascicoloModel = new FascicoloSiepModel();

        lFascicoloModel.setKeyProvvNsc(new BigDecimal(aChiavi.getKPNSC()));
        
        //==============================================================
        // I seguenti campi sono valorizzati nel WebServicesController
        // lFascicoloModel.setIdFascicoloSiep(aValore);
        // lFascicoloModel.setChiaveProgr(aValore);
        // lFascicoloModel.setSenIdSentenza(aValore)
        // lFascicoloModel.setSogIdSoggetto(aValore)
        //==============================================================
        annoCorrente = new BigDecimal(DateUtils.getSysDate("yyyy"));
        lFascicoloModel.setChiaveAnno(annoCorrente);  
        //lFascicoloModel.setChiaveUfficio(adatiUtente.getDATIUFFICIO().getCODICESEDEUFFICIO());
        lFascicoloModel.setChiaveUfficio(mCodUfficio);
        
        lFascicoloModel.setCodStatoFascicolo("02");  // Iscritto 
        
        lFascicoloModel.setCodMotivoArchiviazione("-");
        lFascicoloModel.setCodTipoPosLibero("-");
        lFascicoloModel.setCodUfficioUnione("-");
        
        lFascicoloModel.setDataIscrizione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));

        lFascicoloModel.setFlagValidato("N");
        lFascicoloModel.setCodOperatoreInserimento("nsc-"+adatiUtente.getUSERNAME());
        lFascicoloModel.setDataInserimento(DateUtils.getSysDate());
        lFascicoloModel.setCodUfficioInserimento(mCodUfficio);
        
        if ( adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO() != null && 
             adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getANNO() != null &&
             !adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getANNO().equals("")) 
        {
            lAnnoAppo = (adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getANNO());
        }
        else
        {
            lAnnoAppo=null;
        }
          
        if ( adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO() != null && 
             adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getMESE() != null &&
             !adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getMESE().equals(""))
        {
            lMeseAppo = (adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getMESE());
        }
        else
        {
             lMeseAppo=null;
        }
        if ( adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO() != null && 
             adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getGIORNO() != null &&
             !adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getGIORNO().equals(""))
        {
             lGiornoAppo = (adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getGIORNO());
        }
        else
        {
              lGiornoAppo=null;
        }  
        
        Date lDataIrrevocabilita;
        if ((lAnnoAppo != null && !lAnnoAppo.equals("")) &&
            (lMeseAppo != null && !lMeseAppo.equals("")) &&
            (lGiornoAppo != null && !lGiornoAppo.equals("")))
        {  
            lAnno = new Integer(lAnnoAppo);
            lMese = new Integer(lMeseAppo);
            lGiorno = new Integer(lGiornoAppo);
            lDataIrrevocabilita = DateUtils.getDate(lAnno.intValue(),lMese.intValue(), lGiorno.intValue());
        } 
        else
        {
            lDataIrrevocabilita=null;
        }
        lFascicoloModel.setDataIrrevocabilita(lDataIrrevocabilita);
           
      
        if (adatiAnagrafica.getPROCEDIMENTO().getCODICLASSE() != 0 && adatiAnagrafica.getPROCEDIMENTO().getCODICLASSE() > 0)
        {
            lFascicoloModel.setTipoProgressivo(adatiAnagrafica.getPROCEDIMENTO().getCODICLASSE());
        }
        else
        {  
            lFascicoloModel.setTipoProgressivo(1);
        }
        
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.info("ChiaveAnno:"+lFascicoloModel.getChiaveAnno());
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.info("CodStatoFascicolo:"+lFascicoloModel.getCodStatoFascicolo());
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.info("Cod Classe:"+ lFascicoloModel.getTipoProgressivo());
        
        
        return lFascicoloModel;
      
    }
}