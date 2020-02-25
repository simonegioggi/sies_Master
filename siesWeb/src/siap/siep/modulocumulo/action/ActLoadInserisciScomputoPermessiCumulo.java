package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load dell'inserimento del provvedimento di Scomputo Permessi (Titolo Cumulato)
 * 
 * @author
 *
 */

public class ActLoadInserisciScomputoPermessiCumulo extends ActionModuloCumulo
		implements ICostantiLibAnticipataCumulo, ICostantiStatoEsecTitoloCumulato {
	/*****************************************************************************
	 * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche di precaricare tutti i dati
	 * da visualizzare in tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		String lModalita = "I"; // default inserimento

		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		// LogF3B.getLogger().debug("Sto In modalita "+lModalita);

		BigDecimal IdStatoEsec = null;
		StatoEsecTitoloCumulatoModel Stato = null;
		// String TipoLibAnt = "";

		if ("M".equals(lModalita) || "C".equals(lModalita)) {
			IdStatoEsec = getRequestBigDecimalParameter(
					ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
			IStatoEsecTitoloCumulato CtrlS = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
			Stato = CtrlS.ExRicercaStatoEsecTitoloCumulatoByIdFull(IdStatoEsec);

			/*
			 * if(Stato!=null && Stato.getIdStatoEsecTitoloCumulato()!=null ) {
			 * if(Stato.getListaLiberazioniAnticipate()!=null && Stato.getListaLiberazioniAnticipate().size()
			 * > 0 ) { LibAnticipataCumuloModel LibAntCumMod =
			 * (LibAnticipataCumuloModel)Stato.getListaLiberazioniAnticipate().get(0);
			 * if(LibAntCumMod.getTipoLa()!=null) { TipoLibAnt = LibAntCumMod.getTipoLa(); } } }
			 * 
			 * setRequestAttribute("TipoLiberazioneAnt", TipoLibAnt);
			 * //LogF3B.getLogger().debug("Sto In modifica/cancellazione ("+lModalita+"), IdStatoEsec = "
			 * +IdStatoEsec);
			 */
		}

		// LogF3B.getLogger().debug("Sto In modalita 2 "+lModalita);
		// ----------------------------------------------------
		// ==========================================================================
		// Caricamento Dati delle Combo
		// ==========================================================================
		// Combo Autorita Emittente
		Option lOptionAutEmi = new Option(DecodificheManager.getInstance().getTipoUfficioSius());
		lOptionAutEmi.setFilter(new String[] { "TDS", "UDS", "TDSM", "UDSM", "-" });
		if (Stato != null && Stato.getIdStatoEsecTitoloCumulato() != null
				&& Stato.getCodUfficioEmittente() != null) {
			UfficioModel lUffi = getUfficioByCodUfficio(Stato.getCodUfficioEmittente());
			lOptionAutEmi.setSelected(lUffi.getCodTipoUfficio());
		}
		setRequestAttribute("autoritaEmi", "" + lOptionAutEmi);

		// Tipo Provvedimento
		Option lOptionSorv = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOptionSorv.setFilter(new String[] { "02", "03", "-" });
		if (Stato != null && Stato.getIdStatoEsecTitoloCumulato() != null
				&& Stato.getCodTipoProvvedimento() != null) {
			lOptionSorv.setSelected(Stato.getCodTipoProvvedimento());
		}
		setRequestAttribute("tipoProvvedimento", "" + lOptionSorv);

		// Oggetti Provvedimento (COD. MOTIVO_PROVVEDIMENTO)
		Option lOggettoProv = new Option(DecodificheManager.getInstance().getMotivoProvvedimento(), "-");
		lOggettoProv.setFilter(new String[] { "0039", "2250", "-" });
		if (Stato != null && Stato.getIdStatoEsecTitoloCumulato() != null && Stato.getCodMotivo() != null) {
			lOggettoProv.setSelected(Stato.getCodMotivo());
		}
		setRequestAttribute("oggettoProvvedimento", "" + lOggettoProv);

		// Esito_Provvedimento
		/*
		 * try { Option lEsito = new Option(DecodificheManager.getInstance().getEsitoProvvedimento(),"-");
		 * lEsito.setFilter( new String[] {"0020", "0406", "0023", "0407", "-"} ); if(Stato!=null &&
		 * Stato.getIdStatoEsecTitoloCumulato()!=null && Stato.getCodEsito()!=null) {
		 * lEsito.setSelected(Stato.getCodEsito()); } setRequestAttribute("EsitoProvvedimento", "" + lEsito);
		 * } catch (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		Option lEsito = new Option(DecodificheManager.getInstance().getMotivoProvvedimento(), "-");
		lEsito.setFilter(new String[] { "0958", "0994", "-" });
		if (Stato != null && Stato.getIdStatoEsecTitoloCumulato() != null && Stato.getCodMotivo() != null) {
			lEsito.setSelected(Stato.getCodMotivo());
		}
		setRequestAttribute("EsitoProvvedimento", "" + lEsito);

		// Imposta Modalità.
		setRequestAttribute("modalita", lModalita);

		setRequestAttribute("StatoEsecTitoloCum", Stato);

		// LogF3B.getLogger().debug("Sto In 3 modalita "+lModalita);

		// Restituisce la pagina di Inserimento dei Dati
		/*
		 * String lpage="";
		 * 
		 * if(lModalita.equals("I") ) { lpage = PG_LOAD_INSERISCI_SCOMPUTO_PERMESSI; } else
		 * if(lModalita.equals("M") ) { lpage = PG_LOAD_INSERISCI_SCOMPUTO_PERMESSI; }
		 * 
		 * return lpage;
		 */

		return PG_LOAD_INSERISCI_SCOMPUTO_PERMESSI;

	}
}
