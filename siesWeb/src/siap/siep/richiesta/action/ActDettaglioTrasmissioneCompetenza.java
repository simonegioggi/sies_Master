package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.jms.ICostantiJMS;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>Title: ActDettaglioTrasmissioneCompetenza</p>
 * <p>Description: ActDettaglioTrasmissioneCompetenza</p>
 
 * @version 1.0
 */

public class ActDettaglioTrasmissioneCompetenza extends ActSIESDettaglioProvvedimento implements ICostantiRichiesta
{
  public String processRequest() throws F3BException
  {

	  FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");	  
	  
// id dell'evento inserito
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    if(lIdEvento==null){ 
    	String lStrIdEvento = this.getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    	lIdEvento = new BigDecimal(lStrIdEvento);    	
    }    

    PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento,lFascMod.getIdFascicoloSiep());
    setRequestAttribute("penaresidua", llPenMod);

//ricerca posizione giuridica
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento,lFascMod.getIdFascicoloSiep());
    setRequestAttribute("posizioneluogoaltra", lPos);

//ricerca evento notifica
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
    this.setRequestAttribute("eventonotifica", lEveMod);
    
//Ricerca Magistrato
    IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
    MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
    setRequestAttribute("magistrato", lMagMod);
    
    //Altra autorità
    setRequestAttribute("noteautoritaEsterna", lEveMod.getNotifiche()[0].getNote());
   
   //PREPARO I CAMPI COMPETENZA
   
   //BigDecimal lIdCompetenza = this.getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_ID_COMPETENZA);   
   
   ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();	
   CompetenzaModel mComp = lCompCtrl.ExRicercaCompetenzaByEveIdEvento(lIdEvento);     
   setRequestAttribute("competenza", mComp);   
   
   	FascicoloSiepModel mFascComp = new FascicoloSiepModel();   	
	mFascComp.setChiaveUfficio(mComp.getChiaveUfficio());
	mFascComp.setChiaveAnno(mComp.getChiaveAnno());
	mFascComp.setChiaveProgr(mComp.getChiaveProgr());
	
	IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
	FascicoloSiepModel findedFasc = null;
	if(mComp.getFasSieIdFascicoloSiep()!=null){
		findedFasc = lCtrlFas.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(mFascComp);
		setRequestAttribute("fascCompetenza", findedFasc);
	}
	
	// MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni
	// Ricerco eventuali esiti non presi annotati
	// solo se evento validato
	if ("S".equals(lEveMod.getEvento().getFlagDocumentoRegistrato()))
	{
        Vector<String> lListaTipoMessaggio = new Vector<>();
        lListaTipoMessaggio.add(ICostantiJMS.ESITO);
        lListaTipoMessaggio.add(ICostantiJMS.RICHIESTA);
        
    	Vector<String> lListaTipoOperazione = new Vector<>();
        lListaTipoOperazione.add(ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA);
        lListaTipoOperazione.add(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI);
        lListaTipoOperazione.add(ICostantiJMS.ESITO_SEGUITO_ATTI);
        

        
        IModuloCumulo lCtrl = SIEPLookupRemote.getModuloCumuloRemote();
        Vector<MessaggioModel> lVect = lCtrl.ExRicercaMessaggi(
                  null // ICostantiJMS.DELIVERY_MODE_INVIATO
                , lListaTipoMessaggio, lListaTipoOperazione
                , null // lCodEsito
                , null // "N" // Flag_visto Sul dettaglio evento li voglio tutti
                , lFascMod.getChiaveAnno() 
                , lFascMod.getChiaveProgr() 
                , lFascMod.getChiaveUfficio() 
                , null // aCodUfficioMitt
                , getCodUfficioUtenteConnesso() // ufficio dest
                , null // lDataTrasmissioneDal
                , null // lDataTrasmissioneAl
                , null // lCognome
                , null // lNome
                // Solo quelli relativi al fascicolo Cumulante dell'evento se presente
                , mComp.getChiaveAnno()    // aChiaveAnnoFasCumulante
                , mComp.getChiaveProgr()   // aChiaveProgrFasCumulante
                , mComp.getChiaveUfficio() // aChiaveUfficioFasCumulante
                , 0); // Pagina	
    	
        setRequestAttribute("MessaggiEsiti", lVect);
	}
    // MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni - FINE
	
    return PG_DETTAGLIO_TRASMISSIONE_COMP;
  }

}
