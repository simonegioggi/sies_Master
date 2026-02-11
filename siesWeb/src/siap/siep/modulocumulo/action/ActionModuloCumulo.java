package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Classe Action di Base per il ModuloCumuli
 * 
 * @author
 */
@SuppressWarnings("rawtypes")
public class ActionModuloCumulo extends ActionSiap implements ICostantiModuloCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public IstruttoriaCumuloModel getDatiIstruttoria() throws F3BException {

		return getDatiIstruttoria(null);
	}

	/**
	 * Recupera i dati dell'istruttoria passata sulla request n.b. nella form di chiamata deve essere presente
	 * il campo ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO
	 */
	public IstruttoriaCumuloModel getDatiIstruttoria(BigDecimal aIdIstruttoria) throws F3BException {

		if (aIdIstruttoria == null
				&& (isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) || getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) == null)) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Istruttoria non selezionata");

			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessuna istruttoria selezionata");
			lRedirigi.setAction("siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			throw new F3BException(F3BException.USER_MESSAGE, "Nessuna istruttoria selezionata");
		}

		BigDecimal lIdIstruttoriaCorrente = null;

		if (aIdIstruttoria != null)
			lIdIstruttoriaCorrente = aIdIstruttoria;
		else
			lIdIstruttoriaCorrente = this
					.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

		IstruttoriaCumuloModel lIstruttoriaCumModel = null;
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		lIstruttoriaCumModel = lIstrCtrl.ExRicercaIstruttoriaCumuloById(lIdIstruttoriaCorrente);
		setRequestAttribute("IstruttoriaCumulo", lIstruttoriaCumModel);

		return lIstruttoriaCumModel;

	}

	/**
	 * Nuova versione si recuperano i dati del titolo cumulato
	 * 
	 * @throws F3BException
	 */
	public TitoloCumulatoModel getDatiTitoloCumulato() throws F3BException {

		// ==========================================================================
		// Recupero l'ID del CUMULO
		// ==========================================================================
		BigDecimal lIdTitolo = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		// ==========================================================================
		// Recupero i dati del TITOLO_CUMULATO da passare alla finestra di visualizzazione
		// (visualizzati sempre in testa alle pagine di gestione dei dati analitici
		// include jsp dettaglio titolo)
		// ==========================================================================
		ITitoloCumulato lTitoloCtrl = SIEPLookupRemote.getTitoloCumulatoRemote();
		TitoloCumulatoModel lTitoloModel = null;
		lTitoloModel = lTitoloCtrl.ExRicercaTitoloCumulatoById(lIdTitolo);

		// ===================================================
		// Recupero i dati del PROCEDIMENTO_CUMULATO
		// ===================================================
		ProcedimentoCumulatoModel lProcModel = lTitoloCtrl
				.ExRicercaProcedimentoCumulatoByIdTitolo(lTitoloModel.getIdTitoloCumulato());
		if (lProcModel != null && "S".equals(lProcModel.getFlagAccorpato())) {
			UfficioModel lUfficioOrigine = getUfficioByCodUfficio(lProcModel.getChiaveUfficioOrigine());
			lProcModel.setUfficioOrigine(lUfficioOrigine);
		}

		lTitoloModel.setProcedimentoCumulato(lProcModel);

		// ===================================================
		// Recupero i dati SOGGETTO_CUMULATO
		// ===================================================
		// [SERVE?]

		// FIXME
		// Provare a recuperare un aggregato di tutti i dati analitici in modo
		// che sulla griglia si possa mettere un segno sulle voci che hanno dati inseriti

		setRequestAttribute("TitoloInCumulo", lTitoloModel);

		return lTitoloModel;
	}

	public DatiFinaliCumuloAggregatoModel getDatiFinaliCumuloAggregato() throws F3BException {

		// FIXME TEST
		getListaTitoli();

		return getDatiFinaliCumuloAggregato(null);
	}

	/**
	 * Recupera se presenti i dati finali cumulo per l'istruttoria corrente restituendoli nel model aggregato
	 * 
	 * @return
	 * @throws F3BException
	 */
	public DatiFinaliCumuloAggregatoModel getDatiFinaliCumuloAggregato(BigDecimal aIdIstruttoria)
			throws F3BException {

		DatiFinaliCumuloAggregatoModel lDatiFinali = new DatiFinaliCumuloAggregatoModel();

		if (aIdIstruttoria == null
				&& (isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) || getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) == null)) {
			return null;
		} else {
			BigDecimal lIdIstruttoriaCorrente = null;

			if (aIdIstruttoria != null)
				lIdIstruttoriaCorrente = aIdIstruttoria;
			else
				lIdIstruttoriaCorrente = this
						.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

			// recuperare qui i dati finali aggregati
			IDatiFinaliCumulo lCtrl = SIEPLookupRemote.getDatiFinaliCumuloRemote();
			lDatiFinali = lCtrl.ExRicercaDatiFinaliAggregatiByIdIstruttoria(lIdIstruttoriaCorrente);

			//
			if (lDatiFinali.getPosizioneGiuridicaCumulo() != null) {
				String lCodPos = lDatiFinali.getPosizioneGiuridicaCumulo().getCodPosizioneGiuridica();

				Collection aColLib = DecodificheManager.getInstance().getPosizioniGiuridicheCumLibero();
				Collection aColEspIst = DecodificheManager.getInstance().getPosizioniGiuridicheCumEspIst();
				Collection aColEspAltro = DecodificheManager.getInstance()
						.getPosizioniGiuridicheCumEspAltro();

				if (DecodificheUtils.containsCode(aColLib, lCodPos))
					lDatiFinali.getPosizioneGiuridicaCumulo().setTipoPosGiu(
							ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_LIBERO);
				else if (DecodificheUtils.containsCode(aColEspIst, lCodPos))
					lDatiFinali.getPosizioneGiuridicaCumulo().setTipoPosGiu(
							ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPIST);
				else if (DecodificheUtils.containsCode(aColEspAltro, lCodPos))
					lDatiFinali.getPosizioneGiuridicaCumulo().setTipoPosGiu(
							ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPALTRO);

				if (lDatiFinali.getPosizioneGiuridicaCumulo().getIstDetIdIstitutoDetenzione() != null) {
					IIstitutoDetenzione lIstitutoCtrl = SIEPLookupRemote.getIstitutoDetenzioneRemote();

					IstitutoDetenzioneModel lIstituto = lIstitutoCtrl
							.ExRicercaIstitutoDetenzioneByKey(lDatiFinali.getPosizioneGiuridicaCumulo()
									.getIstDetIdIstitutoDetenzione());
					lDatiFinali.getPosizioneGiuridicaCumulo().setIstitutoDetenzione(lIstituto);
				}
				if (lDatiFinali.getPosizioneGiuridicaCumulo().getChiaveUffFasSius() != null) {
					UfficioModel lUffSius = getUfficioByCodUfficio(lDatiFinali.getPosizioneGiuridicaCumulo()
							.getChiaveUffFasSius());
					lDatiFinali.getPosizioneGiuridicaCumulo().setUfficioSorv(lUffSius);
				}

			}

			//========================================================================
			//
      //========================================================================
	    Vector <MisuraSicurezzaCumuloModel> lElencoMisure = new Vector <MisuraSicurezzaCumuloModel>();
	    IMisuraSicurezzaCumulo lCtrlMisura = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
	    
	    lElencoMisure = lCtrlMisura.ExRicercaMisureSicurezzaCumuloByIdIstruttoria (lIdIstruttoriaCorrente,true);
	    lDatiFinali.setListaMisureSicurezza(lElencoMisure);
			
	    Vector <PenaAccessoriaCumuloModel> lElencoPeneAcc = new Vector <PenaAccessoriaCumuloModel>();
	    IPenaAccessoriaCumulo lCtrlPA = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
	    
	    lElencoPeneAcc = lCtrlPA.ExRicercaPenaAccessoriaCumuloByIdIstruttoria (lIdIstruttoriaCorrente,true);
	    lDatiFinali.setListaPeneAccessorie(lElencoPeneAcc);
      //========================================================================
			
			// Recupero se presente il provvedimento di cumulo
			if (lDatiFinali.getDatiFinaliCumulo() != null
					&& lDatiFinali.getDatiFinaliCumulo().getEveIdEvento() != null) {
				try {
					IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
					EventoNotificaModel lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lDatiFinali
							.getDatiFinaliCumulo().getEveIdEvento());
					lDatiFinali.setProvvedimentoCumulo(lEveNotMod);
				} catch (F3BException e) {
					// Evento cancellato ma l'id è rimasto. Per ora non rilancio eccezione
					// Da modificare la SP di cancellazione
					if (!"Nessun Elemento trovato".equals(e.getMessage()))
						throw e;
				}
			}

			setRequestAttribute("datiFinaliAggregatoModel", lDatiFinali);

		}

		return lDatiFinali;
	}

	public BigDecimal getIdIstruttoria() throws F3BException {

		BigDecimal lIdIstruttoriaCorrente = null;

		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
				&& getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) != null) {
			lIdIstruttoriaCorrente = this
					.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		}
		return lIdIstruttoriaCorrente;
	}

	public Vector getListaTitoli() throws F3BException {

		return getListaTitoli(null);
	}

	/**
	 * Recupera Tutti i titoli dell'istruttoria passata sulla request n.b. nella form di chiamata deve essere
	 * presente il campo ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO
	 */
	public Vector getListaTitoli(BigDecimal aIdIstruttoria) throws F3BException {

		Vector lTitoli = new Vector();
		if (aIdIstruttoria == null
				&& (isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) || getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) == null)) {
			return null;
		} else {
			BigDecimal lIdIstruttoriaCorrente = null;

			if (aIdIstruttoria != null)
				lIdIstruttoriaCorrente = aIdIstruttoria;
			else
				lIdIstruttoriaCorrente = this
						.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

			// Recupero i Titoli_Cumulati
			IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			lTitoli = lIstrCtrl.ExRicercaTitoliByIstruttoria(lIdIstruttoriaCorrente);
			setRequestAttribute("ListaTitoli", lTitoli);

		}

		return lTitoli;
	}
	
	/**
	 * Recupera la lista dei titoli con continuazioni di tipo R non correttamente
	 * agganciate ad altri titoli in istruttoria
	 * 
	 * @since MEV_2025-48 - 2.12 Alert su continuazione
	 */
    public Vector <TitoloCumulatoModel> getListaTitContSganciate (BigDecimal aIdIstruttoria) throws F3BException {

        Vector <TitoloCumulatoModel> lTitoli = new Vector <TitoloCumulatoModel>();
        
        if (aIdIstruttoria == null
                && (isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) || getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) == null)) {
            return null;
        } else {
            BigDecimal lIdIstruttoriaCorrente = null;

            if (aIdIstruttoria != null)
                lIdIstruttoriaCorrente = aIdIstruttoria;
            else
                lIdIstruttoriaCorrente = this
                        .getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

            // Recupero le continuazioni
            IModuloCumulo lModuloCtrl = SIEPLookupRemote.getModuloCumuloRemote();
            lTitoli = lModuloCtrl.ExRicercaTitoliConContinuazioniSganciate (lIdIstruttoriaCorrente);
            
            setRequestAttribute("ListaTitContSganciate", lTitoli);
        }

        return lTitoli;
    }
    
    /**
     * Recupera la lista dei titoli con annotate Revoche Benefici (sosp con 01, Indulto 03) non correttamente
     * agganciate ad altri titoli in istruttoria
     * 
     * @since MEV_2025-48 - 2.12 Alert su continuazione
     */
    public Vector <TitoloCumulatoModel> getListaTitConRevBenSganciati (BigDecimal aIdIstruttoria) throws F3BException {

        Vector <TitoloCumulatoModel> lTitoli = new Vector <TitoloCumulatoModel>();
        
        if (aIdIstruttoria == null
                && (isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) || getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) == null)) {
            return null;
        } else {
            BigDecimal lIdIstruttoriaCorrente = null;

            if (aIdIstruttoria != null)
                lIdIstruttoriaCorrente = aIdIstruttoria;
            else
                lIdIstruttoriaCorrente = this
                        .getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

            // Recupero le continuazioni
            IModuloCumulo lModuloCtrl = SIEPLookupRemote.getModuloCumuloRemote();
            lTitoli = lModuloCtrl.ExRicercaTitoliConRevBenSganciati (lIdIstruttoriaCorrente);
            
            setRequestAttribute("ListaTitRevBenSganciati", lTitoli);
        }

        return lTitoli;
    }

}