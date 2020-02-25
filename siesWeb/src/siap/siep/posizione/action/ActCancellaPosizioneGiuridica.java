package siap.siep.posizione.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.controller.IPosizioneGiuridicaLuogoDetenzione;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActCancellaPosizioneGiuridica extends ActionSiap implements ICostantiPosizioneGiuridica {

	public String processRequest() throws F3BException {

		this.isFascicoloSiepDiCompetenza();

		BigDecimal lId = new BigDecimal(getRequestStringParameter(CAMPO_ID_POSIZIONE_GIURIDICA));
		// riempie il model
		// chiama il controller
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosGiu = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridicaLuogoDetenzione lCtrl = SIEPLookupRemote.getPosizioneGiuridicaLuogoDetenzioneRemote();
		lPosGiu = lCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByKey(lId);
		if(lPosGiu!=null)
			lCtrl.ExCancellaPosizioneGiuridicaLuogoDetenzioneAltraCausa(lPosGiu);
		
		//modifiva FASCICOLO_SIEP.FLAG_ALTRA_CAUSA in funzione della posizione giuridica
		// ** Seleziona l'ultima eventuale Posizione Giuridica associata al Fascicolo Siep **
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		if(lFascMod != null && lFascMod.getIdFascicoloSiep()!=null){
			PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();		
			IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			lPosLuoAltMod = lCtrlPos.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaMisuraCautelareCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

			//modifiva FASCICOLO_SIEP.FLAG_ALTRA_CAUSA in funzione della posizione giuridica			
			IFascicoloSiep lFasCtrl = SIEPLookupRemote.getFascicoloSiepRemote();	
			if(lPosLuoAltMod!=null && lPosLuoAltMod.getPosizioneGiuridica()!=null && lPosLuoAltMod.getPosizioneGiuridica().getAltCauIdAltraCausa()!=null && lFascMod != null && lFascMod.getIdFascicoloSiep()!=null){	        
				checkFlagAltraCausa(lFascMod, lPosLuoAltMod.getPosizioneGiuridica().getCodMaschera());
				//lFascMod.setFlagAltraCausa("S");
			    lFasCtrl.ExModificaFascicoloSiep(lFascMod);		    
			} 
//			else {
//				 //aggiorno tabella FASCICOLO_SIEP Associato alla Posizione Giuridica			      	     
//			    lFascMod.setFlagAltraCausa(null);
//			    lFasCtrl.ExModificaFascicoloSiep(lFascMod);		    
//			}

		}
		

		BigDecimal lIdFasc = ((FascicoloSiepModel) this.getSessionAttribute("fascicolo")).getIdFascicoloSiep();

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.posizione.action.ActRicercaPosizioneGiuridica&" + ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lIdFasc;
		return lPage;
	}
	
	protected void checkFlagAltraCausa(FascicoloSiepModel lFasMod, String codMaschera) {

		if (CAMPO_TIPO_MASCHERA_Libero.equals(codMaschera) // L
		) {
			lFasMod.setFlagAltraCausa("N");

		} else if (CAMPO_TIPO_MASCHERA_LiberoIstituto.equals(codMaschera) // L1
				|| CAMPO_TIPO_MASCHERA_LiberoCautelareIstituto.equals(codMaschera) // L2
				|| CAMPO_TIPO_MASCHERA_LiberoCautelareAltro.equals(codMaschera)// L3
		) {
			lFasMod.setFlagAltraCausa("S");

		} else if (CAMPO_TIPO_MASCHERA_EspiazioneIstituto.equals(codMaschera) // EI
				|| CAMPO_TIPO_MASCHERA_EspiazioneAltro.equals(codMaschera)// EA
		) {
			// empty
			lFasMod.setFlagAltraCausa("");

		} else { // old
			// check flag altra causa
			if (isRequestChecked(ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA))
				lFasMod.setFlagAltraCausa("S");
			else
				lFasMod.setFlagAltraCausa("N");
		}

	}

}