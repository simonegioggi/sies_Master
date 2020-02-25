package siap.siep.posizione.action;

/**
* <p>Title: ActLoadDettaglioPosizioneGiuridica</p>
* <p>Description: Classe Action per la load dettaglio di PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.Utils;

public class ActLoadDettaglioPosizioneGiuridica extends ActionSiap implements ICostantiPosizioneGiuridica
{
  public String processRequest() throws Exception
  {
    BigDecimal lIdPosizione = getRequestBigDecimalParameter(CAMPO_ID_POSIZIONE_GIURIDICA);

    //PosizioneGiuridicaController lCtrl = new PosizioneGiuridicaController();
    IPosizioneGiuridica lCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod = lCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByKey( lIdPosizione );

    //Poichè alcuni campi editabili nella pagina LoadInserimentoPosizioneGiuridica.jsp
    //sono in realtà campi della tabella FASCICOLO_SIEP
    //nel dettaglio viene ricaricato il fascicolo e messo in sessione:
    //in questo modo il model del fascicolo già in sessione resta allineato con
    //le eventuali modifiche fatte inserendo/modificando una posizione giuridica
    IFascicoloSiep lCtrlFasSie = SIEPLookupRemote.getFascicoloSiepRemote();
    FascicoloSiepModel lFasMod = lCtrlFasSie.ExRicercaFascicoloByKey(lPosLuoAltMod.getPosizioneGiuridica().getFasSieIdFascicoloSiep());

	if (lPosLuoAltMod != null && lPosLuoAltMod.getMisuraCautelare() != null) {

		if (lPosLuoAltMod.getMisuraCautelare().getCodiceUfficioPmSede() != null) {
			UfficioModel um = UfficioUtils.getUfficioByCodUfficio(lPosLuoAltMod.getMisuraCautelare().getCodiceUfficioPmSede());
			setRequestAttribute("ufficioPmTipoDesc", um.getDescrTipoUfficio());
			setRequestAttribute("ufficioPmSedeDesc", um.getDescrComune());
		}

		if (lPosLuoAltMod.getMisuraCautelare().getTipoUfficioRegGen() != null){
			setRequestAttribute("tipoUfficioRegGenDesc", Utils.getDescItem(DecodificheManager.getInstance().getTipoUfficioGE(), lPosLuoAltMod.getMisuraCautelare().getTipoUfficioRegGen()));
		}

		if (lPosLuoAltMod.getMisuraCautelare().getAutoritaEmittente() != null){
			setRequestAttribute("autoritaEmittenteCautelareDesc", Utils.getDescItem(DecodificheManager.getInstance().getTipoAutoritaEmittente(), lPosLuoAltMod.getMisuraCautelare().getAutoritaEmittente()));
		}

		if (lPosLuoAltMod.getMisuraCautelare().getAutoritaCompetente() != null){
			setRequestAttribute("autoritaCompetenteCautelareDesc", Utils.getDescItem(DecodificheManager.getInstance().getAutoritaCompetentePerTerritorio(), lPosLuoAltMod.getMisuraCautelare().getAutoritaCompetente()));
		}

	}

	if (lPosLuoAltMod != null && lPosLuoAltMod.getPosizioneGiuridica() != null && lPosLuoAltMod.getPosizioneGiuridica().getAutoritaCompetente() != null){
		setRequestAttribute("autoritaCompetenteDesc", Utils.getDescItem(DecodificheManager.getInstance().getAutoritaCompetentePerTerritorio(), lPosLuoAltMod.getPosizioneGiuridica().getAutoritaCompetente()));
	}
	
//  	// inizio gestione desc posizione giuridica
//	//I)  se la posizione giuridica !="07" ==> parto dalla tabella POSIZIONE_GIURIDICA tramite CG_REF_CODES prendo la DescrPosizioneGiuridica
//    //II) se la posizione giuridica ="07" && COD_MASCHERA="L" non è presente altra causa la DescrPosizioneGiuridica="Libero"
//    //    else la posizione giuridica ="07" ==> è presente altra causa la DescrPosizioneGiuridica si considera 
//    //    la desc di CG_REF_CODES tramite la tabella ALTRA_CAUSA partendo dalla tabella POSIZIONE_GIURIDICA.ALT_CAU_ID_ALTRA_CAUSA
//    //    II.1) per la vecchia gestione posizione giuridica ="07" && POSIZIONE_GIURIDICA.ALT_CAU_ID_ALTRA_CAUSA=null && COD_MASCHERA=NULL  
//	//          trovo la descrizione sulla tabella ALTRA_CAUSA tramite FAS_ID_FASCICOLO_SIEP se trovo ALTRA_CAUSA metto la descrizione 
//	//			di altra causa altrimenti libero  
//	IAltraCausa lAcCtrl = SIEPLookupRemote.getAltraCausa();
//    if(lPosLuoAltMod.getPosizioneGiuridica()!=null && !lPosLuoAltMod.getPosizioneGiuridica().getCodPosizioneGiuridica().equalsIgnoreCase("07")){
//    	//è già presente la descrizione giusta
//    } else if(lPosLuoAltMod.getPosizioneGiuridica()!=null && lPosLuoAltMod.getPosizioneGiuridica().getCodPosizioneGiuridica().equalsIgnoreCase("07") && lPosLuoAltMod.getPosizioneGiuridica().getAltCauIdAltraCausa()==null){
//		AltraCausaModel altraCausa = lAcCtrl.ExRicercaAltraCausaIstitutoByFascicolo(lPosLuoAltMod.getPosizioneGiuridica().getFasSieIdFascicoloSiep());
//		if(altraCausa!=null && lPosLuoAltMod.getPosizioneGiuridica().getCodMaschera()==null){	
//			//vecchia gestione
//			lPosLuoAltMod.getPosizioneGiuridica().setDescrPosizioneGiuridica(altraCausa.getDescrTipoPosGiuridica());
//		}
//		else if(lPosLuoAltMod.getPosizioneGiuridica().getCodMaschera()=="L"){
//			lPosLuoAltMod.getPosizioneGiuridica().setDescrPosizioneGiuridica("Libero");
//		}
//    } else if(lPosLuoAltMod.getPosizioneGiuridica()!=null && lPosLuoAltMod.getPosizioneGiuridica().getCodPosizioneGiuridica().equalsIgnoreCase("07") && lPosLuoAltMod.getPosizioneGiuridica().getAltCauIdAltraCausa()!=null){
//    	// Ricerca Altra Causa
//		AltraCausaModel altraCausa = lAcCtrl.ExRicercaAltraCausaIstitutoByKey(lPosLuoAltMod.getPosizioneGiuridica().getAltCauIdAltraCausa());
//		lPosLuoAltMod.getPosizioneGiuridica().setDescrPosizioneGiuridica(altraCausa.getDescrTipoPosGiuridica());
//    } 


    setRequestAttribute("PosizioneGiuridicaLuogoDetenzioneAltraCausaModel", lPosLuoAltMod);
    setSessionAttribute("fascicolo", lFasMod);

    return PG_LOAD_DETTAGLIOPOSIZIONEGIURIDICA;
  }
}