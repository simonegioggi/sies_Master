package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;

/**
 * Action per la load inserisci e modifica delle Concessioni Misure Alternative Cumulo
 * 
 * @author Intersistemi S.p.A.
 *
 */
public class ActLoadInserisciRevocaMisuraAlternativaCumulo extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiComputiCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
    TitoloCumulatoModel lTitoloCum = super.getDatiTitoloCumulato();

		StatoEsecTitoloCumulatoModel lStato = null;

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		String lIdCompDaModificare = "";

		if ("I".equals(lModalita)) {
			// Inserimento
      siesLogger.debug("--XX-- Sto In Inserimento - id_Titolo= "+lTitoloCum.getIdTitoloCumulato());
      
      // Cerco eventuale Pena Residua
      IComputiCumulo lctrlC = SIEPLookupRemote.getComputiCumuloRemote();
      Vector<ComputiCumuloModel> lVecCompu = new Vector<ComputiCumuloModel>();
      lVecCompu = lctrlC.ExRicercaComputiCumuloByIdTitoloCum(lTitoloCum.getIdTitoloCumulato());
      //siesLogger.debug("--XX-- Sto In Inserimento - size vector "+lVecCompu.size());
      ComputiCumuloModel lDateComp = new ComputiCumuloModel();
      if(lVecCompu!=null && lVecCompu.size() > 0 ) {
      	lDateComp = (ComputiCumuloModel)lVecCompu.get(0);
      }
      setRequestAttribute("aComputoDate", lDateComp );
      
		} else if ("M".equals(lModalita) || "NP".equals(lModalita)) {
			// Modifica
			BigDecimal lIdStat = getRequestBigDecimalParameter(
					ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);

			if ("M".equals(lModalita))
				lIdCompDaModificare = getRequestStringParameter(
						ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO);

      siesLogger.debug("--XX-- Sto In modifica ("+lModalita+"), idStat = "+lIdStat+", lIdComp = "+lIdCompDaModificare);

			// Recupero i dati e li passo alla form
			IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

			lStato = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoByIdFull(lIdStat);
			setRequestAttribute("aProvvedimento", lStato);
			setRequestAttribute("aIdComputo", "" + lIdCompDaModificare);
		} else {
			// Rilanciare Eccezione - Operazione non supportata
		}
		setRequestAttribute("modalita", lModalita);

		// ==========================================================================
		// Caricamento Dati delle Combo
		// ==========================================================================

		// Preparazione codice Motivo Provvedimento per Concessione Misura Alternativa cumulo.
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlt4("REVOCUM");
		Vector<DecodificheModel> lDecodMotivoProvv = new Vector<>();
		lDecodMotivoProvv.add(new DecodificheModel("-", "-", "-", "", "", "", "", "", ""));
		lDecodMotivoProvv.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));

		// 23/07/2018 Descrizione Motivo provvedimento concatenata con RV_ALT5_VALUE
		// Option lOptionMotivoProvv = new Option(lDecodMotivoProvv);
		// Collection lCollMotivoProvv = lDecodifiche.ExRicercaDecodifiche(lModel);
		Collection lCollMotivoProvvNew = new Vector();
		Iterator itx = lDecodMotivoProvv.iterator();
		// int ind = 0 ;
		while (itx.hasNext()) {
			DecodificheModel lDecMod = (DecodificheModel) itx.next();
			if (lDecMod.getCodiceAlt5() != null && lDecMod.getCodiceAlt5().length() > 0) {
				String lDescription = lDecMod.getDescription() + " " + lDecMod.getCodiceAlt5();
				lDecMod.setDescription(lDescription);
			}
			lCollMotivoProvvNew.add(lDecMod);
			// ind++;
		}
		Option lOptionMotivoProvv = new Option(lCollMotivoProvvNew);

		// Tipo Provvedimento
		Option lOptionTipoProvv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza());

		// Tipo Ufficio Emittente
		Option lOptionTUE = new Option(DecodificheManager.getInstance().getTipoUfficio(), "-");
		lOptionTUE.setFilter(new String[] { "TDS", "UDS", "UDSM", "TDSM", "-" });

		if ("M".equals(lModalita)) {
			ComputiCumuloModel lComputo = null;

			BigDecimal idComp = new BigDecimal(lIdCompDaModificare);

			Vector<ComputiCumuloModel> lListaComputi = lStato.getListaComputi();
			Iterator itxComputi = lListaComputi.iterator();
			while (itxComputi.hasNext()) {
				lComputo = (ComputiCumuloModel) itxComputi.next();
				if (lComputo.getIdComputiCumulo().compareTo(idComp) == 0) {
					break;
				}
			}

			if (lComputo.getCodUfficioEmittenteProvv() != null) {
				UfficioModel lUfficioEmittente = getUfficioByCodUfficio(
						lComputo.getCodUfficioEmittenteProvv());
				lOptionTUE.setSelected(lUfficioEmittente.getCodTipoUfficio());
			}
			if (lStato.getCodMotivo() != null)
				lOptionMotivoProvv.setSelected(lStato.getCodMotivo());
			if (lStato.getCodTipoProvvedimento() != null)
				lOptionTipoProvv.setSelected(lStato.getCodTipoProvvedimento());
		}

		setRequestAttribute("tipoUfficioEmittente", "" + lOptionTUE);
		setRequestAttribute("oggettoProvvedimento", "" + lOptionMotivoProvv);
		setRequestAttribute("tipoProvvedimento", "" + lOptionTipoProvv);

		// Imposta Modalità.
		setRequestAttribute("modalita", lModalita);

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCI_REVOCA_MISURAALTERNATIVA_CUMULO;
	}

}