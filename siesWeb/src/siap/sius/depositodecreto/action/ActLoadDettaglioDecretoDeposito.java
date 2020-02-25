package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.template.util.UtilTemplate;
import siap.sico.util.SICOLookupRemote;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel;
import siap.sius.depositodecreto.model.DepositoDecretoFascicoloModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.prescrizione.controller.IPrescrizione;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.tenore.controller.ITenore;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadDettaglioDecretoDeposito
 * </p>
 * <p>
 * Description: Classe Action per il caricamento del dettaglio di qualsiasi tipo di decreto.
 * </p>
* L'azione implementata riceve dalla request l'ID_EVENTO con il quale viene attivata la ricerca.</p>
 * Effettuata la ricerca del decreto viene effettuata una diramazione delle possibili uscite, una per ogni
 * tipo di decreto.
 * <p>
 * Company: Bull
 * </p>
 * 
* @version 1.0
*/
@SuppressWarnings("rawtypes")
public class ActLoadDettaglioDecretoDeposito extends ActionSius implements ICostantiDepositoDecreto,
		ICostantiTemplate {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  IDepositoDecreto mDecCtrl = null;
  // Dati del decreto
  protected DepositoDecretoEventoMotivazioniModel mDepDecrMotMod = null;
  // Elenco Tenori
  protected Vector mTenori = null;
  
	public String processRequest() throws Exception {
  
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("ActLoadDettaglioDecretoDeposito: inizio" );

    String lRetPage = PG_LOAD_DETTAGLIO_DECRETO_INAMMISSIBILITA;
		// String lNextAction = null;

    // Lettura dell' id evento dalla request.
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    // Pagina di redirect usata per richiamare gli ActDettaglio non standard
    RedirectTo lRedirectTo = new RedirectTo();
    lRedirectTo.setPage(IWebConstants.PG_MAIN);
    lRedirectTo.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, "" + lIdEvento);
		// if (lNextAction != null)
		// lRedirectTo.setParameter("acdest", lNextAction);

    FascicoloGPModel lFasGPMod = new FascicoloGPModel();
    lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
    
    // Ricerca attraverso l'id evento del decreto in DEPOSITO_DECRETO.
    mDecCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
    mDepDecrMotMod = mDecCtrl.ExRicercaDecretoEventoMotivazioniInammissibilitaByIdEvento( lIdEvento );
    DepositoDecretoModel lDepDecreto = mDepDecrMotMod.getDepositoDecreto();
    
    // Se il decreto è revocato si ricercano i dati relativi al Decreto di revoca
		if (mDepDecrMotMod.getEvento() != null && mDepDecrMotMod.getEvento().getEveIdEventoRevoca() != null) {
       ricercaDecretoDiRevoca(mDepDecrMotMod.getEvento().getEveIdEventoRevoca());
    }

    // Tipo di decreto
    String lTipoDecreto = lDepDecreto.getCodTipoDecreto();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("ActLoadDettaglioDecretoDeposito tipo decreto: "+lTipoDecreto);

    // Luigi 5-10-2004 Per i dettagli non standard non si fissa il Link di ritorno
		if ((lTipoDecreto.compareTo(CITAZIONE) == 0) || (lTipoDecreto.compareTo(UNIFICAZIONE) == 0)
				|| (lTipoDecreto.compareTo(IRREPERIBILITA) == 0))
      this.gestioneRitorno();
    else
      setLinkRitorno();

    String lLink = "";
    if( ! isRequestAttributeNullObj(IWebConstants.LINK_RITORNO))
      lLink = (String) getRequestAttribute(IWebConstants.LINK_RITORNO);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("Link di ritorno -> " + lLink);

    // Valorizzazione dell'id evento nel model.
    mDepDecrMotMod.getEvento().setIdEvento( lIdEvento );

    // Modificabilità
    String lModificabile = "NO";
    String lStampabile = "NO";
		if (IsFascicoloSiusModificabile()) {
    // Stampabilità
			if (mDepDecrMotMod.getEvento().getFlagDocumentoRegistrato() == null
					|| mDepDecrMotMod.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
        lStampabile = "SI";
        // Se depositato non può essere cancellato
				if (mDepDecrMotMod.getDepositoDecreto().getAnnoS72() == null
						&& mDepDecrMotMod.getDepositoDecreto().getNumS72() == null)
          lModificabile = "SI";
        else
          lModificabile = "NO";
      }

    }
    setRequestAttribute("Modificabile",lModificabile);
    lRedirectTo.setParameter("Modificabile",lModificabile);
    setRequestAttribute("Stampabile",lStampabile);
    lRedirectTo.setParameter("Stampabile",lStampabile);
    lRedirectTo.setParameter(IWebConstants.LINK_RITORNO,lLink);


    // Ricerca dei tenori collegati al decreto.
    ITenore lTenCtrl = SIUSLookupRemote.getTenoreRemote();
    mTenori = lTenCtrl.ExRicercaTenoreByDecreto(lDepDecreto.getIdDepositoDecreto());


    // Prima di impostare gli oggetti nella rquest, nel caso di decreto INAMMISSIBILITA'
    // Gestione del carattere € da passare alla jsp (al momento solo per Remissione Debito)
		if ((lTipoDecreto.compareTo(INAMMISSIBILITA) == 0)
				&& (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U011") == 0)) {
			if ((mDepDecrMotMod.getMotivazioniDecreto() != null)
					&& (mDepDecrMotMod.getMotivazioniDecreto().length > 0)) {
			for (int j=0; j< mDepDecrMotMod.getMotivazioniDecreto().length; j++) {
					mDepDecrMotMod.getMotivazioniDecreto()[j].setDescrMotivazione(mDepDecrMotMod
							.getMotivazioniDecreto()[j].getDescrMotivazione().replace("€", "&#8364;"));
			}
		}
	}
    
	// MEV63: aggiunta gestione della sezione per minorenni
	String codOggettoProcedimento = lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
	if ((codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_APPLICAZIONE_PROVVISORIA_MISURA_ALTERNATIVA)
			)
			&& super.isUserUDSM()) {
		BigDecimal chiaveAnno = lFasGPMod.getFascicoloSiusModel().getChiaveAnno();
		BigDecimal chiaveProgr = lFasGPMod.getFascicoloSiusModel().getChiaveProgr();
		IMisuraAlternativa iMisuraAlternativa = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel misuraAlternativa = iMisuraAlternativa
				.ExRicercaMisuraAlternativaCorrenteByAnnoProgr(chiaveAnno, chiaveProgr);
		setRequestAttribute("misuraAlternativa", misuraAlternativa);
	}
	
    // Imposta gli oggetti nella request.
    setRequestAttribute("tenori", mTenori);
    setRequestAttribute("depositoDecretoMotivazioni", mDepDecrMotMod );
    
    // 20131201 - Imposta l'id dell'evento.
    setRequestAttribute("IdEvento", lIdEvento);
    // 20131203 - Imposta "forzatamente" il codice del tipo provvedimento per il decreto = "02".
    setRequestAttribute("CodTipoProvvedimento", "02"); // decreto
    // 20131205 - Tipo decreto, per inibizione della modifica magistrato per alcuni tipi di decreti.
    setRequestAttribute("CodTipoDecreto", lTipoDecreto); // decreto

    // 20131203 - ( questo può essere il punto d'intervento )  Ricerca del Magistrato Relatore
    //gestioneMagistratoAvvocati(lFasGPMod);
    
    // 20131203 - carica il magistrato per evento.
    gestioneMagistratoByEvento(lIdEvento);
    
    // 20131203 - carica gli avvocati associati al fascicolo.
    gestioneAvvocati(lFasGPMod);

    // Trascodifica Stato Libertatis
    setRequestAttribute("StatoLibertatis", DecodificheManager.getInstance().getStatoLibertatis());

    // Switch sul tipo di decreto
    if (   lTipoDecreto.compareTo(GENERICO) == 0
				|| lTipoDecreto.compareTo(ICostantiDepositoOrdinanzaPc.RICHIESTA_OTTEMPERANZA) == 0) {
      // Decreto di tipo GENERICO
        lRetPage = PG_DETTAGLIO_GENERICO;
        ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());
        gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(LIMITAZIONI_CONTROLLI_CORRISPONDENZA) == 0) {
      // Decreto di tipo LIMITAZIONI E CONTROLLI DELLA CORRISPONDENZA
      lRetPage = PG_DETTAGLIO_LIMITAZIONE_CONTROLLI_CORRISPONDENZA;
      ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());
      gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(INAMMISSIBILITA) == 0) {
      setFunctionsAvailableToRequest("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoInammissibilita");
      lRetPage = PG_LOAD_DETTAGLIO_DECRETO_INAMMISSIBILITA;
		} else if (lTipoDecreto.compareTo(CITAZIONE) == 0) {
      // Se ricavabile viene passata l'ID Udienza nella request al Dettaglio Fissazione Udienza

      // STUB: chiamo il dettaglio perchè non standardizzato
      lRedirectTo.setAction("siap.sius.udienza.action.ActLoadDettaglioFissazioneUdienza");
      lRetPage = lRedirectTo.toString();
		} else if (lTipoDecreto.compareTo(INCOMPETENZA) == 0) {
      gestioneTemplateDecIncompetenza();
      setFunctionsAvailableToRequest("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoIncompetenza");
      lRetPage = PG_LOAD_DETTAGLIO_DECRETO_INCOMPETENZA;
		} else if (lTipoDecreto.compareTo(NDP_NLP) == 0) {
      setFunctionsAvailableToRequest("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoNDPNLP");
      lRetPage = PG_LOAD_DETTAGLIO_DECRETO_NDP_NLP;
		} else if (lTipoDecreto.compareTo(UNIFICAZIONE) == 0) {
      // STUB: chiamo il dettaglio perchè ancora non standardizzato
      lRedirectTo.setAction("siap.sius.decretounificazione.action.ActLoadDettaglioDecretoUnificazione");
      lRetPage = lRedirectTo.toString();
		} else if (lTipoDecreto.compareTo(IRREPERIBILITA) == 0) {
      // STUB: chiamo il dettaglio perchè ancora non standardizzato
      lRedirectTo.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoIrreperibilità");
      lRetPage = lRedirectTo.toString();

		} else if (lTipoDecreto.compareTo(APPLICAZIONE_PROVVISORIA_MA) == 0) {
    // Decreto di tipo "Applicazione Provvisoria di Misura Alternativa
      lRetPage = PG_DETTAGLIO_APPLICAZIONE_PROVVISORIA_MA;
      // Creazione lista template.
      gestioneTemplate(lIdEvento);
      // Ricerca di eventuali prescrizioni collegatie al decreto.
      ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());
		} else if (lTipoDecreto.compareTo(ESPULSIONE) == 0) {
      // Decreto di tipo ESPULSIONE
        lRetPage = PG_LOAD_DETTAGLIO_DECRETO_ESPULSIONE;
        gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(MODIFICA_ATT_LAVORATIVA) == 0) {
      // Decreto di tipo MODIFICA ATTIVITA' LAVORATIVA
        lRetPage = PG_LOAD_DETTAGLIO_DECRETO_MODIFICA_ATT_LAVORATIVA;
        gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(MODIFICA_PRESCRIZIONI) == 0) {
      // Decreto di tipo MODIFICA PRESCRIZIONI
        lRetPage = PG_LOAD_DETTAGLIO_DECRETO_MODIFICA_PRESCRIZIONI;
        gestioneTemplate(lIdEvento);
        // Ricerca di eventuali prescrizioni collegatie al decreto.
        ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());
		} else if (lTipoDecreto.compareTo(RICOVERI) == 0) {
        // Decreto di tipo RICOVERI
          lRetPage = PG_LOAD_DETTAGLIO_DECRETO_RICOVERI;
          gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(AUTORIZZAZIONE_CORRISPONDENZA_TELEFONICA) == 0) {
        // Decreto di tipo Autorizzazione Corrispondenza Telefonica
          lRetPage = PG_DETTAGLIO_AUTORIZ_CORRIS_TELEFONICA;
          gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(RINVIO_ESECUZIONE_PENA) == 0) {
        // Decreto di tipo Rinvio Esecuzione Pena
          lRetPage = PG_DETTAGLIO_RINVIO_ESECUZIONE_PENA;
          gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(SOSPENSIONE_ESECUZIONE_PENA) == 0) {
        // Decreto di tipo Sospensione Esecuzione Pena
          lRetPage = PG_DETTAGLIO_SOSPENSIONE_ESECUZIONE_PENA;
          gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(REVOCA_SOSPENSIONE_ESECUZIONE_PENA) == 0) {
        // Decreto di tipo Revoca Sospensione Esecuzione Pena
          lRetPage = PG_DETTAGLIO_REVOCA_SOSPENSIONE_ESECUZIONE_PENA;
          gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(REVOCA_APPLICAZIONE_PROVVISORIA_MA) == 0) {
        // Decreto di tipo Revoca Applicazione Provvisoria M. A.
          lRetPage = PG_DETTAGLIO_REVOCA_APPLICAZIONE_PROVVISORIA_MA;
          gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(PERMESSO) == 0) {
        // Decreto di tipo Permesso
        lRetPage = PG_DETTAGLIO_PERMESSO;
        gestioneTemplate(lIdEvento);
        Vector lPermessi = ricercaPermessi(lDepDecreto.getIdEventoGenerato());
        if (lPermessi != null)
          setRequestAttribute("permessi", lPermessi);
        // Ricerca di eventuali prescrizioni collegatie al decreto.
        ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());
		} else if (lTipoDecreto.compareTo(RICOVERO_OPG_OSS_PSICHE) == 0) {
        // Decreto Ricovero OPG per osservazione
        lRetPage = PG_DETTAGLIO_RICOVERO_OPG_OSS_PSICHE ;
        gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(ICostantiDepositoOrdinanzaPc.RICOVERO_OPG) == 0) {
       // Decreto Ricovero OPG Espiazione Pena
       lRetPage = PG_DETTAGLIO_RICOVERO_OPG_ESP_PENA;
       gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(MODIFICA_ATTIVITA_LUOGO_DET) == 0) {
        //Modifica Attivita' / Luogo Detenzione
        lRetPage = PG_DETTAGLIO_MODIFICAATTLUOGODET ;
        gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(PROPOSTA_DECLAR_ESITO_PROVA) == 0) {
        //Proposta declaratoria esito pena
        lRetPage = PG_DETTAGLIO_PROPOSTA_DECLAR_ESITO;
        gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(INOSSERVANZA_OBBLIGHI) == 0) {
        //Inosservanza Obblighi/Prescrizioni
        lRetPage = PG_DETTAGLIO_INOSSERVANZAOBBLIGHI;
        gestioneTemplate(lIdEvento);
        //model Istituto Detenzione
        IstitutoDetenzioneModel lIstMod = null;
			if (lDepDecreto.getIstDetIdIstitutoDetenzione() != null
					&& !lDepDecreto.getIstDetIdIstitutoDetenzione().equals("-")) {
          IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
				lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lDepDecreto
						.getIstDetIdIstitutoDetenzione());
        }
        setRequestAttribute("istitutodetenzione", lIstMod);
		} else if (lTipoDecreto.compareTo(SOPRAVVENIENZA_NT) == 0) {
        //Sopravvenienza Nuovo Titolo
        lRetPage = PG_DETTAGLIO_SOPRAVVENIENZA_NT;
        gestioneTemplate(lIdEvento);
        //model Istituto Detenzione
        IstitutoDetenzioneModel lIstMod = null;
			if (lDepDecreto.getIstDetIdIstitutoDetenzione() != null
					&& !lDepDecreto.getIstDetIdIstitutoDetenzione().equals("-")) {
          IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
				lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lDepDecreto
						.getIstDetIdIstitutoDetenzione());
        }
        setRequestAttribute("istitutodetenzione", lIstMod);
		} else if (lTipoDecreto.compareTo(LICENZA) == 0) {
        // Decreto di tipo Licenza
        lRetPage = PG_DETTAGLIO_LICENZA;
        gestioneTemplate(lIdEvento);
        Vector lLicenze = ricercaPermessi(lDepDecreto.getIdEventoGenerato());
        if (lLicenze != null)
          setRequestAttribute("Licenze", lLicenze);
        // Ricerca di eventuali prescrizioni collegatie al decreto.
        ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());
		} else if ((lTipoDecreto.compareTo(REVOCA_PERMESSO) == 0)
				|| (lTipoDecreto.compareTo(REVOCA_LICENZA) == 0)
				|| (lTipoDecreto.compareTo(ESCLUSIONE_COMPUTO) == 0)
				|| (lTipoDecreto.compareTo(ESCLUSIONE_COMPUTO_LICENZA) == 0)) {
        // Decreto di tipo Revoca
        lRetPage = PG_DETTAGLIO_REVOCA_PERMESSO;
        gestioneTemplate(lIdEvento);
        Vector lLicenze = ricercaPermessi(lDepDecreto.getIdEventoGenerato());
			if (lLicenze != null && lLicenze.size() > 0) {
          LicenzaLibAnticipataModel lRevoca = (LicenzaLibAnticipataModel) lLicenze.get(0);
          setRequestAttribute("revoca", lRevoca);
        }
        // Ricerca di eventuali prescrizioni collegatie al decreto.
        ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());

        //Ricerca del decreto Revocato
        if (mDepDecrMotMod.getEvento().getEveIdEvento() != null)
          ricercaDecretoDiRiferimento(mDepDecrMotMod.getEvento().getEveIdEvento());
		} else if ((lTipoDecreto.compareTo(REVOCA_DECRETO) == 0)) {
        // Decreto di tipo Revoca
        lRetPage = PG_LOAD_DETTAGLIO_REVOCA_DECRETO;
        gestioneTemplate(lIdEvento);

        // Ricerca di eventuali prescrizioni collegatie al decreto.
        //ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());

        //Ricerca del decreto Revocato
        if (mDepDecrMotMod.getEvento().getEveIdEvento() != null)
          ricercaDecretoRevocato(mDepDecrMotMod.getEvento().getEveIdEvento());
      }
    
// DL 146/2014 - luglio 2014 - 
		else if (lTipoDecreto.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LIBERAZIONE_ANTICIPATA) == 0) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug(" ActLoadDettaglioDecretoDeposito --> Decreto di Revoca Liberazione Anticipata ");
	        // Per questi decreti devo cercare i Periodi di L.A.
	        ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
	        Vector lLicenzePeriodi = lCtrlDep.ExRicercaLicenzeLibanticipataByEve(lIdEvento);
	        this.setRequestAttribute("LicenzePeriodi",lLicenzePeriodi);
			// for (int cc = 0; cc < lLicenzePeriodi.size(); cc++) {
			// LicenzaPeriodiLibAnticipataModel llModel = new LicenzaPeriodiLibAnticipataModel();
			// llModel = (LicenzaPeriodiLibAnticipataModel) lLicenzePeriodi.get(cc);
			// }
	        
	        gestioneTemplate(lIdEvento);
        	lRetPage = PG_DETTAGLIO_DECRETO_REVOCA_LA;
	        
      }
    
// DL 92 2014 Violazione CEDU - 
		else if (lTipoDecreto.compareTo(ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU) == 0) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
    	  	siesLogger.debug(" ActLoadDettaglioDecretoDeposito ---> Decreto Violazione Art 3 CEDU");
	        // Per questi decreti devo cercare i Periodi di L.A.
    	  	
	        ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
	        Vector lLicenzePeriodi = lCtrlDep.ExRicercaLicenzeLibanticipataByEve(lIdEvento);
	        this.setRequestAttribute("LicenzePeriodi",lLicenzePeriodi);
     
	        gestioneTemplate(lIdEvento);
        	lRetPage = PG_DETTAGLIO_DECRETO_VIOLAZIONE_CEDU;
      }
//  
		else if (lTipoDecreto.compareTo(AUTORIZZAZIONE_MA) == 0) {
        // Decreto di tipo Autorizzazione su Misura Alternativa
          lRetPage = PG_DETTAGLIO_AUTORIZZAZIONE_MA;
          ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());
          gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(AUTORIZZAZIONE_SS) == 0) {
    	  // Decreto di tipo Autorizzazione su Sanzioni Sostitutive
          lRetPage = PG_DETTAGLIO_AUTORIZZAZIONE_SANZIONI_SOSTITUTIVE;
          ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());
          gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(DECLARATORIA_ESTINZIONE_SS) == 0) {
    	  //Declaratoria Estinzione Sanzioni Sostitutive
          lRetPage = PG_DETTAGLIO_DECLARATORIA_ESTINZIONE_SANZIONI_SOSTITUTIVE;
          gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(CONVOCAZIONE_DIFFIDA_SS) == 0) {
    	  //Convocazione/Diffida Sanzioni Sostitutive
          lRetPage = PG_DETTAGLIO_CONVOCAZIONE_DIFFIDA_SANZIONI_SOSTITUTIVE;
          gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(MODIFICA_PERMANENTE_SS) == 0) {
    	  //Modifica Permanente Sanzioni Sostitutive
          lRetPage = PG_DETTAGLIO_MODIFICA_PERMANENTE_SANZIONI_SOSTITUTIVE;
          gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(SOSPENSIONE_ESECUZIONE_SS) == 0) {
    	  //Sospensione Esecuzione Sanzione Sostitutiva
          lRetPage = PG_DETTAGLIO_SOSPENSIONE_ESECUZIONE_SANZIONI_SOSTITUTIVE;
          gestioneTemplate(lIdEvento);
          ricercaPeriodoAltraSanzione();
		} else if (lTipoDecreto.compareTo(REVOCA_AUTORIZZAZIONE_SS) == 0) {
    	  //Revoca Autorizzazione Sanzione Sostitutiva
          lRetPage = PG_DETTAGLIO_REVOCA_AUTORIZZAZIONE_SANZIONE_SOSTITUTIVA;	
          gestioneTemplate(lIdEvento);  
          
           //Ricerca Fascicolo
			if (lFasGPMod.getFascicoloSiusModel() != null
					&& lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null) {
        	  ricercaFascicoloOrigine(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine());
          }
		} else if (lTipoDecreto.compareTo(AUTORIZZAZIONE_MS) == 0) {
   	  // Decreto di tipo Autorizzazione su Misure Sicurezza
         lRetPage = PG_DETTAGLIO_AUTORIZZAZIONE_MISURE_SICUREZZA;
         ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());
         gestioneTemplate(lIdEvento);
		} else if (lTipoDecreto.compareTo(MODIFICA_PRESCRIZIONI_MS) == 0) {
       // Decreto di tipo MODIFICA PRESCRIZIONI
         lRetPage = PG_LOAD_DETTAGLIO_DECRETO_MODIFICA_PRESCRIZIONI_MS;
         gestioneTemplate(lIdEvento);
         // Ricerca di eventuali prescrizioni collegatie al decreto.
         ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());
		} else if (lTipoDecreto.compareTo(SOSPENSIONE_ESECUZIONE_MS) == 0) {
   	  //Sospensione Esecuzione Misura Sicurezza
         lRetPage = PG_DETTAGLIO_SOSPENSIONE_ESECUZIONE_MISURE_SICUREZZA;
         gestioneTemplate(lIdEvento);
         ricercaPeriodoAltraMisura();
		} else if (lTipoDecreto.compareTo(DEC_INOSSERVANZA_OBBLIGHI_MS) == 0) {
   	  // Diffida Misura Sicurezza
         lRetPage = PG_DETTAGLIO_GENERICO;
         gestioneTemplate(lIdEvento);
         // Ricerca di eventuali prescrizioni collegatie al decreto.
         ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());
     }
		/* 
		 * ISSUE MEV : aggiunta casisitica per gestione rinvio MS
		 * Numero MEV : 39
		 * Autore    : Gioggi
		 * Data      : 09/giu/2017
		 * Branch    : MEV_39
		 */
		else if (lTipoDecreto.compareTo(RINVIO_ESECUZIONE_MS) == 0) {
			// Rinvio Misura Sicurezza
			lRetPage = PG_DETTAGLIO_RINVIO_ESECUZIONE_MS;
			gestioneTemplate(lIdEvento);
			// Ricerca di eventuali prescrizioni collegatie al decreto.
			ricercaPrescrizioni(lDepDecreto.getIdEventoGenerato());
        }
		//***** FINE INTERVENTO MEV_39 *****//
		else {
        lRetPage = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Tipo di decreto non supportato. ["
					+ lTipoDecreto + "]");
      }
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
      siesLogger.debug("ActLoadDettaglioDecretoDeposito: fine" );
      return lRetPage;
  }

  // Costruzione della Combo con i template di stampa
	private void gestioneTemplate(BigDecimal alIdEvento) throws Exception {

    Option lOptTemplate = null;
    lOptTemplate = UtilTemplate.listaCbxTemplate(alIdEvento);
    setRequestAttribute( CAMPO_COMBO_TEMPLATE, "" + lOptTemplate );

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("ElencoTemplate nella Combo -> " + lOptTemplate);

    // template di default
    String[] lSelected = lOptTemplate.getSelecteds();
		if (lSelected != null && lSelected.length > 0) {
      setRequestAttribute(CAMPO_DEFAULT_TEMPLATE, lSelected[0]);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
      siesLogger.debug("TemplateDiDefault -> " + lSelected[0]);
    }
    return;
  }

	// Costruzione specifica della Combo con i template per il decreto di Incompetenza
	private void gestioneTemplateDecIncompetenza() throws Exception {
  
    Option lOptTemplate = null;
    
    // Il template viene cercato per ID
    TemplateModel lTempRic = new TemplateModel();
    lTempRic.setIdTemplate(TEMPLATE_DECRETO_INCOMPETENZA);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
     siesLogger.debug("Ricerca lista template per decreto di Incompetenza -> "+ lTempRic.getIdTemplate());
   
    lOptTemplate = UtilTemplate.listaCbxTemplate(lTempRic);
    setRequestAttribute( CAMPO_COMBO_TEMPLATE, "" + lOptTemplate );

    return;
  }

  /**
   * 
   * 
   * @param aIdEvento
   * @throws Exception
   */
  private void gestioneMagistratoByEvento(BigDecimal aIdEvento) throws Exception {
	  IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
	  MagistratoModel lMagModel = lMagCtrl.ExRicercaMagistratoByEvento(aIdEvento);
		MagistratoRelatoreModel lMagRel = new MagistratoRelatoreModel();
		lMagRel.setMagistrato(lMagModel);
	  setRequestAttribute("magistratorelatore", lMagRel);
  }

  /**
   * Gestione Avvocati per idFascicolo. Ricerca gli avvocati associati al fascicolo.
   * <p>
	 * 
   * @param aFasGPMod
   * @throws Exception
   */
  private void gestioneAvvocati(FascicoloGPModel aFasGPMod) throws Exception {
	    // Ricerca avvocati assegnati al fascicolo
	    IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector lAvvocato = lAvvCtrl.ExRicercaAvvocatiByFascicoloNoError(aFasGPMod.getFascicoloSiusModel()
				.getIdFascicoloSius());
	    setRequestAttribute("avvocato", lAvvocato);
  }
    
	// private void gestioneMagistratoAvvocati(FascicoloGPModel aFasGPMod) throws Exception {
	// // Ricerca del Magistrato Relatore
	// IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
	// MagistratoRelatoreModel lMagRel = lMagCtrl.ExRicercaEstesaMagRelByFascicolo(aFasGPMod
	// .getFascicoloSiusModel().getIdFascicoloSius());
	// setRequestAttribute("magistratorelatore", lMagRel);
	// // Ricerca avvocati assegnati al fascicolo
	// IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
	// Vector lAvvocato = lAvvCtrl.ExRicercaAvvocatiByFascicoloNoError(aFasGPMod.getFascicoloSiusModel()
	// .getIdFascicoloSius());
	// setRequestAttribute("avvocato", lAvvocato);
	// return;
	// }

	private void ricercaPrescrizioni(BigDecimal aIdEvento) throws Exception {

    Vector lPrescrizioni = null;
		try {
      // Ricerca Prescrizioni
      IPrescrizione lPreCtrl = SIUSLookupRemote.getPrescrizioneRemote();
      lPrescrizioni = lPreCtrl.ExRicercaPrescrizioneByEvento(aIdEvento);
		} catch (F3BException fex) {
      // Si filtra l'eccezione per elementi non trovati
			if (fex.getErrorCode() == F3BException.USER_MESSAGE)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
        siesLogger.debug("ActLoadDettaglioDecretoDeposito : " + fex);
      else
        throw fex;
		} catch (Exception ex) {
      throw ex;
		} finally {
      // Vengono passate le prescrizioni nella request solo se presenti.
      setRequestAttribute("prescrizioni", lPrescrizioni);
    }
  }

	private Vector ricercaPermessi(BigDecimal aIdEvento) throws Exception {

    Vector lPermessi = null;
		try {
      // Ricerca Permessi
      ILicenzaPeriodiLibAnticipata lLicCtrl = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
      lPermessi = lLicCtrl.ExRicercaLicenzeByEve(aIdEvento);
		} catch (F3BException fex) {
      // Si filtra l'eccezione per elementi non trovati
			if (fex.getErrorCode() == F3BException.USER_MESSAGE)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
        siesLogger.debug("ActLoadDettaglioDecretoDeposito : " + fex);
      else
        throw fex;
		} catch (Exception ex) {
      throw ex;
    }
     return lPermessi;
  }

	private void ricercaDecretoDiRiferimento(BigDecimal aIdEvento) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
     siesLogger.debug("ricercaDecretoDiRiferimento : inizio");

     DepositoDecretoFascicoloModel lDepDecMod = null;

    lDepDecMod = mDecCtrl.ExRicercaDecretoFascicoloByIdEvento(aIdEvento);

		if (lDepDecMod != null) {
      setRequestAttribute("decretoRevocato",lDepDecMod.getDepositoDecreto());
      //Ricerca dei permessi
      Vector lPermessi = ricercaPermessi(aIdEvento);
      setRequestAttribute("permessiRevocati", lPermessi);
      //Ricerca Fascicolo
      setRequestAttribute("fascicoloRevocato",lDepDecMod.getFascicolo());
    }
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("ricercaDecretoDiRiferimento : fine");

  }

    // Ricerca dell'ID Udienza dalla tabella UDIENZA_PROCEDIMENTO
	// private BigDecimal RicercaIdUdienza(BigDecimal aIdEvento) throws Exception {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("RicercaIdUdienza: inizio");
	// BigDecimal lIdUdienza = null;
	// UdienzaProcedimentoModel lUdienzaProcedimento = null;
	//
	// IUdienzaProcedimento lUdiProCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
	// lUdienzaProcedimento = lUdiProCtrl.ExRicercaUdienzaProcedimentoByEve(aIdEvento);
	// if (lUdienzaProcedimento != null)
	// lIdUdienza = lUdienzaProcedimento.getUdiIdUdienza();
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("RicercaIdUdienza: fine");
	// return lIdUdienza;
	// }

	private void ricercaDecretoRevocato(BigDecimal aIdEvento) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
   siesLogger.debug("ricercaDecretoRevocato : inizio");

		DepositoDecretoEventoMotivazioniModel lDepDecrMotMod = mDecCtrl
				.ExRicercaDecretoEventoMotivazioniInammissibilitaByIdEvento(aIdEvento);

		if (lDepDecrMotMod != null && lDepDecrMotMod.getDepositoDecreto() != null) {
     setRequestAttribute("decretoRevocato",  lDepDecrMotMod.getDepositoDecreto());
     //Ricerca dei permessi
     Vector lPermessi = ricercaPermessi(aIdEvento);
     setRequestAttribute("permessiRevocati", lPermessi);

     //Ricerca Fascicolo
			if (lDepDecrMotMod.getEvento() != null
					&& lDepDecrMotMod.getEvento().getFasSiuIdFascicoloSius() != null)
        ricercaFascicoloOrigine(lDepDecrMotMod.getEvento().getFasSiuIdFascicoloSius());
  }

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
     siesLogger.debug("ricercaDecretoRevocato : fine");

}

  // Interfaccia al Controller del Fascicolo SIUS
  IFascicoloSius mFasSiusCtrl = null;

	private void ricercaFascicoloOrigine(BigDecimal lIdFascicoloOrigine) throws Exception {

         FascicoloGPModel lFasGPModOrigine = new FascicoloGPModel();

         lFasGPModOrigine = ricercaFascicoloSIUS(lIdFascicoloOrigine);
         setRequestAttribute("fascicolo_origine", lFasGPModOrigine);
   }

	private FascicoloGPModel ricercaFascicoloSIUS(BigDecimal aIdFascicoloSius) throws Exception {

      FascicoloGPModel lFascicolo = null;

      if (mFasSiusCtrl == null)
         mFasSiusCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
      lFascicolo = mFasSiusCtrl.ExRicercaFascicoloByKey(aIdFascicoloSius);
      return lFascicolo;
   }

	private void ricercaDecretoDiRevoca(BigDecimal aIdEvento) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
      siesLogger.debug("ricercaDecretoDiRevoca : inizio");

		DepositoDecretoEventoMotivazioniModel lDepDecrMotMod = mDecCtrl
				.ExRicercaDecretoEventoMotivazioniInammissibilitaByIdEvento(aIdEvento);

		if (lDepDecrMotMod != null && lDepDecrMotMod.getDepositoDecreto() != null) {
         setRequestAttribute("decretoDiRevoca",  lDepDecrMotMod.getDepositoDecreto());

         //Ricerca Fascicolo
			if (lDepDecrMotMod.getEvento() != null
					&& lDepDecrMotMod.getEvento().getFasSiuIdFascicoloSius() != null) {
				FascicoloGPModel lFascicoloSius = ricercaFascicoloSIUS(lDepDecrMotMod.getEvento()
						.getFasSiuIdFascicoloSius());
            setRequestAttribute("FascicoloDiRevoca", lFascicoloSius);
         }
      }
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
      siesLogger.debug("ricercaDecretoDiRevoca : fine");
   }

   // Ricerca Periodo Altra Sanzione
	private void ricercaPeriodoAltraSanzione() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
	   	siesLogger.debug("ricercaPeriodoAltraSanzione: inizio");
	   	   
	   	// Ricerca il Periodo Altra Sanzione tramite l'ID dell'evento
		IPeriodoAltraSanzione lCtrlPAS = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		PeriodoAltraSanzioneModel mPerAlSanz = lCtrlPAS.ExRicercaSanzioneSostitutivaByIdEvento(mDepDecrMotMod
				.getEvento().getIdEvento());
		
		// Passa i dati trovati del Periodo Altra Sanzione alla JSP
		setRequestAttribute("PeriodoAltraSanzione", mPerAlSanz);
	
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
	   siesLogger.debug("ricercaPeriodoAltraSanzione: fine");
   }
   // Ricerca Periodo Altra Misura
	private void ricercaPeriodoAltraMisura() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
	   	siesLogger.debug("ricercaPeriodoAltraMisura: inizio");
	   	   
	   	// Ricerca il Periodo Altra Misura tramite l'ID dell'evento
		IPeriodoAltraMisura lCtrlPAM = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		PeriodoAltraMisuraModel mPerAlMisu = lCtrlPAM.ExRicercaMisuraSicurezzaByIdEvento(mDepDecrMotMod
				.getEvento().getIdEvento());
		
		// Passa i dati trovati del Periodo Altra Misura alla JSP
		setRequestAttribute("PeriodoAltraMisura", mPerAlMisu);
	
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
	   siesLogger.debug("ricercaPeriodoAltraMisura: fine");
   }

}