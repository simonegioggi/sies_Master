package siap.siep.archiviazione.action;

import java.math.BigDecimal;

import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazioneesitotrasmissione.action.ICostantiAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.controller.IAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciAnnProvCumulo
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActLoadInserisciAnnProvCumulo extends ActionSiap implements ICostantiArchiviazione {

	public String processRequest() throws Exception {

//Controllo Presenza del Fascicolo in Sessione
    if (this.isSessionAttributeNullObj("fascicolo"))
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

    this.isFascicoloSiepDiCompetenza();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

//Controllo Validazione Fascicolo
    if(isFascicoloNonValidato())
     return IWebConstants.PG_MESSAGE;

    this.isEventoNonValidato();
    
// se Provengo da Annotazione Esito per Assorbimento in Cumulo
    BigDecimal aIdEsiTra = null;
    String lStessoUfficio = "";
    AnnotazioneEsitoTrasmissioneModel lAnnEsiTrasModel = null;
    if( !isRequestParameterNullObj(ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ID_ESITO_TRASMISSIONE) )
    {
    	IAnnotazioneEsitoTrasmissione CtrlAnn = SIEPLookupRemote.getAnnotazioneEsitoTrasmissioneRemote();
    	aIdEsiTra = getRequestBigDecimalParameter(ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ID_ESITO_TRASMISSIONE);
    	lAnnEsiTrasModel = (AnnotazioneEsitoTrasmissioneModel) CtrlAnn.ExRicercaAnnotazioneEsitoTrasmissioneById(aIdEsiTra);
    	setRequestAttribute("EsitoTrasmissione", lAnnEsiTrasModel);
    	
    	if(lAnnEsiTrasModel != null && lAnnEsiTrasModel.getIdEsitoTrasmissione()!=null)
    		if(lAnnEsiTrasModel.getChiaveUfficio()!=null)
    			if( !lAnnEsiTrasModel.getChiaveUfficio().equals(lFascMod.getChiaveUfficio()))
    				lStessoUfficio = "NO";
    			else if( lAnnEsiTrasModel.getChiaveUfficio().equals(lFascMod.getChiaveUfficio()))
    				lStessoUfficio = "SI";
    }    

    setRequestAttribute("StessoUfficio", lStessoUfficio);
    
//Posizione Giuridica
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

    if (notEsistePosizioneGiuridica(lPos))
      return IWebConstants.PG_MESSAGE;

    setRequestAttribute("posizioneluogoaltra", lPos);

// Pena Complessiva
    IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

    if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

    String lFlagErgastolo = "N";
    // se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "") {
      if(lPenComMod.getCodTipoPenaDetentiva().equals("03"))
        lFlagErgastolo = "S";
      else if(lPenComMod.getCodTipoPenaDetentiva().equals("04"))
       lFlagErgastolo = "D";
    }

    setRequestAttribute("flagergastolo", lFlagErgastolo);

//Pena Residua
    PenaResiduaModel lPenaResMod = new PenaResiduaModel();
    IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
    lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

    if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
       return IWebConstants.PG_MESSAGE;

    setRequestAttribute("penaresidua", lPenaResMod);


//commentato 23-11-2006 -- Dario sotto indicazione di viviana
		/*
		 * if(lFascMod.getCodUfficioUnione() != null && !lFascMod.getCodUfficioUnione().equals("-")) {
		 * UfficioModel lUffMod = getUfficioByCodUfficio(lFascMod.getCodUfficioUnione()); Option lOption = new
		 * Option( DecodificheManager.getInstance().getTipoAutoritaCumulo(),lUffMod.getCodTipoUfficio());
		 * setRequestAttribute("autoritaCumulo", "" + lOption ); setRequestAttribute("sedeCumulo",
		 * lUffMod.getDescrComune() ); } else {
		 */

//UFFICIO APPARTENENTE
      UfficioModel lUffMod = getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());
      setRequestAttribute("codtipoufficio",lUffMod.getCodTipoUfficio());
      setRequestAttribute("destipoufficio",lUffMod.getDescrTipoUfficio());
      setRequestAttribute("descsede", lUffMod.getDescrComune() );

      // Ufficio Cumulante
      Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaCumulo());
      if(lAnnEsiTrasModel!=null)
      {
      	UfficioModel lUffCumMod = getUfficioByCodUfficio(lAnnEsiTrasModel.getChiaveUfficio());
      	lOption.setSelected(lUffCumMod.getCodTipoUfficio());
      	setRequestAttribute("sedeCumulo", lUffCumMod.getDescrComune());
      }    
      setRequestAttribute("autoritaCumulo", "" + lOption );
   // }

    Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutorita", "" + lOptionAutorita);

    Option lOptionUffRecCrediti = new Option( DecodificheManager.getInstance().getTipoUfficio());
    lOptionUffRecCrediti.setFilter( new String[] {"DIB", "CAP"} );
    setRequestAttribute("uffrecrediti", "" + lOptionUffRecCrediti );

    return PG_LOAD_INSERISCI_ANN_PROV_CUMULO;
  }
}