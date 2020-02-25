package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.controller.IPenaComplessivaCumulo;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import siap.siep.tipologiaorario.controller.ITipologiaOrario;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action che gestisce il caricamente delle form di inserimento/modifica dei Benefici Disposti in Sentenza
 * (titolo CUMULATO)
 *
 *
 * @author d.fiorletta
 *
 */
public class ActLoadInserisciBeneficiCumulo extends ActionModuloCumulo implements ICostantiBeneficiCumulo {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {
		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();
		BigDecimal lIdTitolo = getRequestBigDecimalParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		// TIPO_FORM_BENEFICIO = 01(TIPO_FORM_SOSPENSIONE) se inserisco o modifico Sospensione o Non menzione
		// = 02(TIPO_FORM_INDULTO) se inserisco o modifico Indulto o Amnistia
		String lTipoForm = getRequestStringParameter(TIPO_FORM_BENEFICIO);

		// MODALITA_INSERIMENTO = 'I' per INSERIMENTO, 'M' per MODIFICA, 'C' per CANCELLAZIONE
		String lModalita = MODALITA_INSERIMENTO;

		if (!isRequestParameterNullObj(MODALITA) && !"".equals(getRequestStringParameter(MODALITA)))
			lModalita = getRequestStringParameter(MODALITA);

		setRequestAttribute(MODALITA, lModalita);

		IBeneficioCumulo lCtrl = SIEPLookupRemote.getBeneficioCumuloRemote();
		BeneficioCumuloModel lBeneficioCumulo = null;
		// =================================================================================
		// Se siamo in MODIFICA, recuperiamo i dati del Beneficio da modificare, e
		// se si tratta di SOPSPENSIONE, recuperiamo anche TIPOLOGIA_ORARIO e NON MENZIONE
		// =================================================================================
		if (lModalita.equals(MODALITA_MODIFICA)) {
			BigDecimal idBeneficioCumulo = getRequestBigDecimalParameter(CAMPO_ID_BENEFICIO_CUMULO);
			lBeneficioCumulo = lCtrl.ExRicercaBeneficioCumuloByKey(idBeneficioCumulo);

			if (lBeneficioCumulo != null && lBeneficioCumulo.getIdBeneficioCumulo() != null) {
				if ("01".equals(lBeneficioCumulo.getCodTipoBeneficio())
						|| "02".equals(lBeneficioCumulo.getCodTipoBeneficio())) {
					// lTipoForm = "01";
					// Sospensione e/o non menzione
					BeneficioCumuloModel llBenNMMod = lCtrl.ExRicercaBeneficioCumuloByBenIdBeneficioCum(
							lBeneficioCumulo.getIdBeneficioCumulo());
					setRequestAttribute("beneficioNonMenzione", llBenNMMod);

					ITipologiaOrario CtrlTip = SIEPLookupRemote.getTipologiaOrarioRemote();
					Vector lTipologia = CtrlTip.ExRicercaTipologiaOrarioByIdBeneficioCumulo(
							lBeneficioCumulo.getIdBeneficioCumulo());
					setRequestAttribute("tipologiaOrario", lTipologia);

					if (lTipologia.size() > 0)
						lBeneficioCumulo.setListaOrari(lTipologia);
				}
			}

			setRequestAttribute("beneficioCumulo", lBeneficioCumulo);

		}

		// ===========================================================
		// Sospensione: Caricamento combo da visualizzare in form
		// ===========================================================
		if (lTipoForm.equals(TIPO_FORM_SOSPENSIONE)) {
			Option lOption = new Option(DecodificheManager.getInstance().getTipoSospSubordinata(), "-");
			if (lBeneficioCumulo != null && lBeneficioCumulo.getIdBeneficioCumulo() != null)
				if (lBeneficioCumulo.getCodTipoSospSubordinata() != null)
					lOption.setSelected(lBeneficioCumulo.getCodTipoSospSubordinata());

			setRequestAttribute("sospensioneSubordinata", "" + lOption);

			Option lOptionSotto = new Option(DecodificheManager.getInstance().getSottoTipoBeneficio(), "-");
			if (lBeneficioCumulo != null && lBeneficioCumulo.getIdBeneficioCumulo() != null)
				if (lBeneficioCumulo.getCodSottotipoBeneficio() != null)
					lOptionSotto.setSelected(lBeneficioCumulo.getCodSottotipoBeneficio());

			setRequestAttribute("sottotipoBeneficio", "" + lOptionSotto);
		}

		// ==================================================================================
		// Indulto : Caricamento combo da visualizzare nella form e RECUPERO dei dati nel D.B.
		// ==================================================================================
		if (lTipoForm.equals(TIPO_FORM_INDULTO)) {
			// ricerca pena complessiva
			IPenaComplessivaCumulo lCtrlPenCom = SIEPLookupRemote.getPenaComplessivaCumuloRemote();
			PenaComplessivaCumuloModel lPenComMod = lCtrlPenCom
					.ExRicercaPenaComplessivaCumByIdTitolo(lIdTitolo);
			setRequestAttribute("penacomplessiva", lPenComMod);

			PenaAccessoriaCumuloModel lPenAccMod = new PenaAccessoriaCumuloModel();
			lPenAccMod.setTitIdTitoloCumulato(lIdTitolo);
			IPenaAccessoriaCumulo lCtrlPen = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
			Vector lPenAcce = lCtrlPen.ExRicercaPeneAccessorieCumulo_Valide(lPenAccMod);
			setRequestAttribute("peneaccessorie", lPenAcce);

			Option lOption = new Option(DecodificheManager.getInstance().getTipoBeneficio(), "03");
			if (lBeneficioCumulo != null && lBeneficioCumulo.getIdBeneficioCumulo() != null)
				if (lBeneficioCumulo.getCodTipoBeneficio() != null)
					lOption.setSelected(lBeneficioCumulo.getCodTipoBeneficio());

			setRequestAttribute("tipoBeneficio", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getDPR(), "24");
			if (lBeneficioCumulo != null && lBeneficioCumulo.getIdBeneficioCumulo() != null)
				if (lBeneficioCumulo.getCodDpr() != null)
					lOption.setSelected(lBeneficioCumulo.getCodDpr());

			setRequestAttribute("listaDPR", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getSottoTipoBeneficioIndulto());
			if (lBeneficioCumulo != null && lBeneficioCumulo.getIdBeneficioCumulo() != null)
				if (lBeneficioCumulo.getCodSottotipoBeneficio() != null)
					lOption.setSelected(lBeneficioCumulo.getCodSottotipoBeneficio());

			setRequestAttribute("sottotipoBeneficio", "" + lOption);

		}

		String lPage = "";
		setRequestAttribute(TIPO_FORM_BENEFICIO, lTipoForm);

		if (lModalita.compareTo(MODALITA_CANCELLA) == 0) {
			BigDecimal idBeneficioCumulo = getRequestBigDecimalParameter(CAMPO_ID_BENEFICIO_CUMULO);
			lBeneficioCumulo = lCtrl.ExRicercaBeneficioCumuloByKey(idBeneficioCumulo);
			// Cancellazione
			lBeneficioCumulo.setFlagStato(getRequestStringParameter(CAMPO_FLAG_STATO));
			lBeneficioCumulo.setMotivoModifica(
					getRequestStringParameter(ICostantiPenaAccessoriaCumulo.CAMPO_MOTIVO_MODIFICA));

			lBeneficioCumulo.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lBeneficioCumulo.setDataAggiornamento(DateUtils.getSysDate());
			lBeneficioCumulo.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

			lCtrl.ExCancellaBeneficioCumuloTipologiaOrario(lBeneficioCumulo);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActRicercaBeneficiCumulo&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" + lIdTitolo;
		} else { // Inserimento/Modifica
			if (lTipoForm.equals(TIPO_FORM_SOSPENSIONE))
				lPage = PG_LOAD_INSERISCI_BENEFICI_CUMULO;
			else if (lTipoForm.equals(TIPO_FORM_INDULTO))
				lPage = PG_LOAD_INSERISCI_BENEFICI_INDULTO_CUMULO;
		}

		return lPage;

	} // Chiude ProcessRequest()

} // Chiude ActLoadInserisciBeneficiCumulo()
