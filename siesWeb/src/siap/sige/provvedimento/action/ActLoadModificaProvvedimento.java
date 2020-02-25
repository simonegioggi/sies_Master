package siap.sige.provvedimento.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import siap.sige.detenzione.controller.IFasSigeDetenzione;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.provvInterlocutori.action.ICostantiProvvInterlocutoriSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.tenore.util.TenoriSigeUtil;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaProvvedimento
 * </p>
 * 
* @version 1.0
*/
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadModificaProvvedimento extends ActDettaglioOrdinanza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		String lRetPage = PG_LOAD_EMISSIONE_ORDINANZA;

		String tipoAutoSogg = "";
		String decrTipoAutoSogg= "";
		String tipoAuto = "";
		String tipoAutoIsti = "";
		String sedeAutoSogg = "";
		String sedeAuto= "";
		String decrTipoAuto= "";
		
		// Viene chiamata processRequest() per la lettura dei dati
		super.processRequest();

		// Fascicolo Sige Esteso in sessione.
		// FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel)
		// getSessionAttribute("FascicoloSigeEsteso");

		if (mProvEvento == null)
			throw new SIGEException("errore nella lettura del Provvedimento");
		
		// super.setRequestAttribute("idProvvedimentoTitoliEsecutivi",
		// mProvEvento.getProvvedimento().getIdProvvedimentoSige());
		super.setRequestAttribute("idProvvedimentoTitoliEsecutivi", mProvEvento.getProvvedimento()
				.getIdProvvedimentoSige());	
		
		
		// switch sul tipo di provvedimento
		if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(COD_ORDINANZA_NDPNLP))
			lRetPage = PG_LOAD_EMISSIONE_ORDINANZA_NDPNLP;
		else if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(COD_ORDINANZA_INCOMPETENZA))
			lRetPage = PG_LOAD_EMISSIONE_ORDINANZA_INCOMPETENZA;
		else if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(COD_ORDINANZA_SOSPENSIONE)) {
			if (mProvEvento.getProvvedimento().getProvvIdProvvedimentoSige() == null)
				throw new SIGEException("Errore nella lettura del Provvedimento da Sospendere");
			IProvvedimentoSige ctrPS = SIGELookupRemote.getProvvedimentoRemote();
			ProvvedimentoSigeEventoModel lProvvedimento = ctrPS.ExRicercaProvvedimentoById(mProvEvento
					.getProvvedimento().getProvvIdProvvedimentoSige());
			if (lProvvedimento == null)
				throw new SIGEException("Errore nella lettura del Provvedimento da Sospendere");
			
			TenoreSigeModel lTenore = new TenoreSigeModel();
			// lTenore.setProvIdProvvedimentoSige(mProvEvento.getProvvedimento().getProvvIdProvvedimentoSige());
			lTenore.setProvIdProvvedimentoSige(mProvEvento.getProvvedimento().getIdProvvedimentoSige());
			ITenoreSige ctrTS = SIGELookupRemote.getTenoreSigeRemote();
			Vector <TenoreSigeEstesoModel>lTenoriEstesi = ctrTS.ExRicercaTenoriEstesiAttivi(lTenore);

			// 24/05/2010 in caso di assenza di tenori attivi, si ripristinano quelli della richiesta SIGE.
			if (lTenoriEstesi.size() == 0 && mFasEsteso.getRichiestaSige() != null)
				lTenoriEstesi = ctrTS.ExRicercaTenoreEstesoByRichiesta(mFasEsteso.getRichiestaSige()
						.getIdRichiestaSige());
		
			TenoriSigeUtil tsu = new TenoriSigeUtil ();
			Vector <TenoreSigeModel>lTenori = tsu.listaTenoriDaListaTenoriEstesi(lTenoriEstesi);
			setRequestAttribute("provvDaSospendere", lProvvedimento);
			setSessionAttribute("tenori", lTenori);
			setSessionAttribute("tenoriEstesi", lTenoriEstesi);
			
			// **** INIZIO TEST EMMA *** Lettura delle notifiche.
			INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
			// Vector lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento (lIdEvento);			
			Vector lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento(mProvEvento.getEventoNotifica()
					.getEvento().getIdEvento());			
			siesLogger.debug(">>>>> Numero notifiche = " + lVect.size());
			
			for (Iterator iterator = lVect.iterator(); iterator.hasNext();) {
				NotificaModel notifica = (NotificaModel) iterator.next();
				// notiifche per autorità esterne 
				if(notifica.getAutEstIdAutoritaEsterna() != null){
					// notifche per autorità esterne (notifca al soggetto)
					if(  notifica.getSogIdSoggetto() != null){
						 tipoAutoSogg = notifica.getAutoritaEsterna().getCodTipoAutorita();
						 decrTipoAutoSogg = notifica.getAutoritaEsterna().getDescrTipoAutorita();
						 sedeAutoSogg = notifica.getAutoritaEsterna().getDescrSede();
						 setRequestAttribute("tipoAutoSogg", tipoAutoSogg);
						 setRequestAttribute("decrTipoAutoSogg", decrTipoAutoSogg);
						// lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(), tipoAutoSogg, 75);
					}
					// notifche per autorità esterne (altri destinatari)
					else if(notifica.getSogIdSoggetto() == null && "C".equals( notifica.getCodTipoNotifica())){
						 tipoAuto = notifica.getAutoritaEsterna().getCodTipoAutorita();
						 sedeAuto = notifica.getAutoritaEsterna().getDescrSede();
						 decrTipoAuto = notifica.getAutoritaEsterna().getDescrTipoAutorita();
						 setRequestAttribute("tipoAuto", tipoAuto);						 
						 setRequestAttribute("decrTipoAuto", decrTipoAuto);
					}else if(notifica.getSogIdSoggetto() == null && "N".equals( notifica.getCodTipoNotifica())){
						 tipoAutoIsti = notifica.getAutoritaEsterna().getCodTipoAutorita();
						 //sedeAuto = notifica.getAutoritaEsterna().getDescrSede();
						// decrTipoAuto = notifica.getAutoritaEsterna().getDescrTipoAutorita();
//						 setRequestAttribute("tipoAuto", tipoAuto);						 
//						 setRequestAttribute("decrTipoAuto", decrTipoAuto);
					}
				}
			}
			// **** FINE TEST EMMA ***
			
			lRetPage = PG_LOAD_EMISSIONE_ORDINANZA_SOSPENSIONE;
		} else if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(COD_DECRETO_INAMMISSIBILITA)) {
			listaMotivi();
			lRetPage = PG_LOAD_EMISSIONE_DECRETO_INAMMISSIBILITA;
		} else if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(COD_ORDINANZA_CONFLITTO_COMPETENZA)) {
			lRetPage = ICostantiProvvInterlocutoriSige.PG_LOAD_ORDINANZA_CONFLITTO_COMPETENZA;
		}

		FascicoloSigeUtils lFasSigeUtils = new FascicoloSigeUtils();
		// Carica Tipo Destinatario in base al Tipo Ufficio.
		String strTipoDest = lFasSigeUtils.leggiTipoDestinatario(this.getUfficioUtenteConnesso()
				.getCodTipoUfficio());
		setRequestAttribute("TipoDest", strTipoDest); 		
		
	   // imposto la sede della notifica al soggetto
       setRequestAttribute("DescrLuogoDetenzione", sedeAutoSogg);
    // imposto la sede della notifica altri destinatari
       setRequestAttribute("sedeAuto", sedeAuto);
				
		// Ricerca LUOGO DETENZIONE
		IFasSigeDetenzione lDetenzioneCtrl = SIGELookupRemote.getFasSigeDetenzioneRemote();
		FasSigeDetenzioneModel lDetenzione = lDetenzioneCtrl.ExRicercaUltimaDetenzioneFascicolo(mFasEsteso
				.getFascicoloSige().getIdFascicoloSige());
		setRequestAttribute("detenzione", lDetenzione);

		// LISTA UFFICI SOGGETTO
		//Option lOptionSog = new Option();
		// if (lDetenzione != null && lDetenzione.getLuogoDetenzione() != null &&
		// lDetenzione.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto().length() > 0)
		// lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(),
		// lDetenzione.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto(), 75);
		//else
		//	lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), 75);

		// LISTA UFFICI
		Collection<DecodificheModel> lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		String[] lStringFilter = { "-", "22" };
		// 30//11/2018 aggiungo autorità A2 su richiesta Nunzia (email del 29/11/2018 - Documento1.docx)
		if (mProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(COD_ORDINANZA_SOSPENSIONE)) {
			lStringFilter = new String[]{ "-", "22", "A2" };
		}			
				
		// Preleva elenco dei Tipi istituto
		Option lOptionAvv = new Option();
		if(tipoAutoIsti != null){
			lOptionAvv = new Option(lTipoIstituto, tipoAutoIsti, 75);
		}else{
			lOptionAvv = new Option(lTipoIstituto, "22", 75);
		}
		lOptionAvv.setFilter(lStringFilter);

		// Preleva elenco degli altri destinatari.
		Option lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(), 75);	
				
		setRequestAttribute("luogodet", lDetenzione);
		setRequestAttribute("TipiIstituti1", "" + lOptionAvv);		
		setRequestAttribute("tipoAutorita", lOptionAut.toString());
		

		lockApplicativo("Emissione_Provvedimento");
		setRequestAttribute("modalita", "M");

		if (mFasEsteso.getUdienzaProcedimento() != null
				&& mFasEsteso.getUdienzaProcedimento().getIdUdienzaProcedimentoSige() != null) {
			caricaDatiUdienza(mFasEsteso.getUdienzaProcedimento().getIdUdienzaProcedimentoSige());
		}

		// il tipo giudizio va definito quando si definisce l'udienza
		if (lTipoGiudizio == null || "".equals(lTipoGiudizio) || "-".equals(lTipoGiudizio)) {
			// altrimenti si verifica quello in precedenza selezionato nella definizione del procedimento
			lTipoGiudizio = (mFasEsteso.getFascicoloSige().getCodTipoGiudizio() == null ? "-" : mFasEsteso
					.getFascicoloSige().getCodTipoGiudizio().trim());
		}
		String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		// 20170907: [SG] aggiunta impostazione RequestAttribute
		setRequestAttribute("ctu", lCodTipoUfficio);

		// Carica Combo x TipoGiudizio.
		// setComboTipoGiudizio();
		Option lOptionGiudizio = getComboTipoGiudizio(lTipoGiudizio, lCodTipoUfficio);
		setRequestAttribute("tipoGiudizioVal", lTipoGiudizio);
		setRequestAttribute("tipoGiudizio", lOptionGiudizio.toString());

	 	// Combo per l'Ufficio Competente.
	 	setComboUfficioCompetente();
	 	
	    IUdienzaSige ctrlUdiSige=SIGELookupRemote.getUdienzaSigeRemote();
	    UdienzaSigeModel udienza=ctrlUdiSige.ExRicercaUdienzaSigeById(mProvEvento.getProvvedimento().getUdiIdUdienzaSige());
	    super.setRequestAttribute("UdienzaSige", udienza);
	    
		setFunctionsAvailableToRequest("siap.sige.provvedimento.action.ActDettaglioOrdinanza");
		return lRetPage;
	}

	private void listaMotivi() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotivi: inizio");
		String lTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio().toUpperCase();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("tipo ufficio ->" + lTipoUff);

		// Chiamata Controller per la ricerca delle Decodifiche.
		IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();
		Vector lVect = new Vector(lDecCtrl.ExListaMotiviInammissibilitaxSottoSistema(lTipoUff, "SIGE"));
		setRequestAttribute("motivi", lVect);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotivi: fine");
	}

}