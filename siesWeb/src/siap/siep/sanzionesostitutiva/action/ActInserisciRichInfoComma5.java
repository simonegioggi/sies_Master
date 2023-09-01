package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Funzione di inserimento della richiesta infomazione comma 5 per le notifiche degli ordini di ingiunzione
 * 
 * @author d.fiorletta
 * @since MEV_2023-33
 */
public class ActInserisciRichInfoComma5 extends ActionSiap implements ICostantiNotifica {
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		
		ComuneModel lComMod = null;
		Vector <RinnovoModel> lRinnovi = new Vector <RinnovoModel> ();		
		
		// RINNOVO
		if (!this.isRequestParameterNullObj("primoAvvocato") && this.getRequestStringParameter("primoAvvocato").equals("S")) 
		{
		  siesLogger.debug("Primo avvocato con idNotifica = "+this.getRequestBigDecimalParameter("idPrimaNotifica"));
			
		  RinnovoModel lRinModel = new RinnovoModel();
		  
		  lRinModel.setNotIdNotifica(this.getRequestBigDecimalParameter("idPrimaNotifica"));
		  lRinModel.setCodTipoRinnovo("D");  // Richiesta Informazioni Difensore
		
		  lRinModel.setCodTipoAutoritaRinnovo(getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO));
		
		  lRinModel.setDataRinnovo(getRequestDateParameter(ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN,
		      ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN,
		      ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN));
		  lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO)));
		  lRinModel.setCodLuogoRinnovo(lComMod.getCodComune());
		  lRinModel.setNuovoLuogoNotifica("-");
		  lRinModel.setNote(getRequestStringParameter(ICostantiRinnovo.CAMPO_NOTE));
		  
		  lRinModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		  lRinModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		  lRinModel.setDataInserimento(DateUtils.getSysDate());
		
		  lRinnovi.add(lRinModel);
		}
		
		// RINNOVO NOTIFICA' secondo Avvocato
		if (!this.isRequestParameterNullObj("secondoAvvocato") && this.getRequestStringParameter("secondoAvvocato").equals("S")) 
		{
	      siesLogger.debug("Secondo avvocato con idNotifica = "+this.getRequestBigDecimalParameter("idSecondaNotifica"));
	      
		  RinnovoModel lRinModelSecondo = new RinnovoModel();

		  lRinModelSecondo.setNotIdNotifica(this.getRequestBigDecimalParameter("idSecondaNotifica"));
		  lRinModelSecondo.setCodTipoRinnovo("D"); // Richiesta Informazioni Difensore
		  
		  lRinModelSecondo.setCodTipoAutoritaRinnovo (getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_A));
		  lRinModelSecondo.setDataRinnovo(getRequestDateParameter(
		      ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA, ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA,
		      ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA));
		  lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_A)));
		  lRinModelSecondo.setCodLuogoRinnovo(lComMod.getCodComune());
		  lRinModelSecondo.setNuovoLuogoNotifica("-");
		  lRinModelSecondo.setNote(getRequestStringParameter(ICostantiRinnovo.CAMPO_NOTE_A));
		  
		  lRinModelSecondo.setCodOperatoreInserimento(getCodUtenteConnesso());
		  lRinModelSecondo.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		  lRinModelSecondo.setDataInserimento(DateUtils.getSysDate());
		
		  lRinnovi.add(lRinModelSecondo);
		}
		
		// RINNOVO NOTIFICA'
		if (this.getRequestStringParameter("altri").equals("S")) 
		{
		  siesLogger.debug("Richiesta ad altri idNotifica = "+this.getRequestBigDecimalParameter("idAltraNotifica"));
		  
		  RinnovoModel lRinModelAltri = new RinnovoModel();

		  lRinModelAltri.setNotIdNotifica(this.getRequestBigDecimalParameter("idAltraNotifica"));
		  lRinModelAltri.setCodTipoRinnovo("I");  // Richiesta Informazioni Altri

		  lRinModelAltri.setCodTipoAutoritaRinnovo(getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_AL));
		  lRinModelAltri.setDataRinnovo(getRequestDateParameter(ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR,
		      ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR,
		      ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR));
		  lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_AL)));
		  lRinModelAltri.setCodLuogoRinnovo(lComMod.getCodComune());
		  lRinModelAltri.setNuovoLuogoNotifica("-");
		  lRinModelAltri.setNote(getRequestStringParameter(ICostantiRinnovo.CAMPO_NOTE_AL));
		  
		  lRinModelAltri.setCodOperatoreInserimento(getCodUtenteConnesso());
		  lRinModelAltri.setCodUfficioInserimento(getCodUfficioUtenteConnesso());		
		  lRinModelAltri.setDataInserimento(DateUtils.getSysDate());
		
		  lRinnovi.add(lRinModelAltri);
		}
		
		// INSERIMENTO RINNOVO
		IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
		Vector <RinnovoModel> lRinnoviInseriti = lCtrl.ExInserisciRinnovo(lRinnovi);

		// Attenzione è gestibile un solo rinnovo alla volta
		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioRichInfoComma5";
		lPage += "&"+ICostantiRinnovo.CAMPO_ID_RINNOVO+"="+lRinnoviInseriti.elementAt(0).getIdRinnovo();
		return lPage;
	}
}
