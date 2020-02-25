package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * 
 * @author 
 *
 */
public class ActConvertiRIinFascicoloSIEP extends ActionNuovaIstanza implements ICostantiNuovaIstanza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	public String processRequest() throws Exception
	 {
		
		FascicoloSiepModel lRIMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("MODEL TROVATO: " + lRIMod);
		FascicoloSiepModel lFascMod = new FascicoloSiepModel();
	    
	    UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

	    lFascMod.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); //Anno corrente
	    //Il progressivo del fascicolo (in base all'anno e all'ufficio) viene calcolato applicativamente
	      
	    lFascMod.setChiaveUfficio(lUtenteMod.getUfficioUtente().getCodUfficio()); //Ufficio dell'operatore che inserisce
	    
	    lFascMod.setCodStatoFascicolo("02"); //Stato fascicolo settato ad aperto
	    lFascMod.setCodMotivoArchiviazione("-"); //Motivo di archiviazione '-' per le join
	    lFascMod.setCodTipoPosLibero("-"); //Motivo di archiviazione '-' per le join

	    lFascMod.setDataIscrizione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
	    lFascMod.setFlagValidato("N"); // Il flag di validazione viene impostato a 'NO'
	    lFascMod.setFlagAltraCausa("N"); // Il flag altra causa viene gestito nella gestione della posizione giuridica

	    lFascMod.setCodOperatoreInserimento( lUtenteMod.getUserId() );
	    lFascMod.setDataInserimento( DateUtils.getSysDate() );
	    lFascMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

	    lFascMod.setSenIdSentenza(lRIMod.getSenIdSentenza());
	    lFascMod.setSogIdSoggetto(lRIMod.getSogIdSoggetto());

	    //lFasMod.setTipoProgressivo(9); 
		//lFascMod.setFasSieIdFascicoloSiep(lRIMod.getIdFascicoloSiep());

	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.info("MODEL del SOGGETTO: " + lRIMod.getSoggetto());
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.info("MODEL della SENTENZA: " + lRIMod.getSentenza());
		lFascMod.setSoggetto(lRIMod.getSoggetto());
		lFascMod.setSentenza(lRIMod.getSentenza());

	    INuovaIstanza lCtrl1 = SIEPLookupRemote.getNuovaIstanzaRemote();

	    //preparo il model dell'ISTANZA per la duplicazione
	    Collection<NuovaIstanzaModel>  collNIMod = lCtrl1.ExRicercaNuovaIstanzaNonAnnullataByIdFascicolo(lRIMod.getIdFascicoloSiep());
	    if (collNIMod.size() != 1)
	    	throw new F3BException(F3BException.USER_MESSAGE, "Attenzione : Conversione impossibile per questo registro! Sono presenti più istanze.");
	    // Deve esserci una sola nuova istanza per un registro classe 90000.
	    // Per il momento il blocco viene messo qui (e non in ActLoad) in attesa di ulteriori chiarimenti sulla possibilità
	    // di più istanze.

	    NuovaIstanzaModel lNuoMod = (NuovaIstanzaModel) collNIMod.toArray()[0 ];
		
	    //preparo il model dell'EVENTO per la duplicazione
	    IEvento lCtrEve = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrEve.ExRicercaEventoByKey(lNuoMod.getEveIdEvento());
		
		//lFascMod= lCtrl.ExInserisciFascicoloSiep(lFascMod);
	    lFascMod=lCtrl1.ExConvertiNuovaIstanza(lFascMod.getSoggetto(),lFascMod, lRIMod, lEveMod, lNuoMod);
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.info("MODEL MODIFICATO: " + lFascMod);
	    
		setRequestAttribute("fascicolo", lFascMod);
	    setSessionAttribute("fascicolo",lFascMod);
	    setSessionAttribute("soggetto",lFascMod.getSoggetto());
	    setSessionAttribute("sentenza",lFascMod.getSentenza());
	    
	    String lPage="";
	    lPage = IWebConstants.PG_MAIN + "?" + 
	    	IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo";
	    //lPage += "&" + "IdEvento" + "=" + lNuoRetMod.getEveIdEvento().toString();
	    //lPage += "&" + "TipoVis" + "=Inoltro";

	    return lPage;
	 }

}