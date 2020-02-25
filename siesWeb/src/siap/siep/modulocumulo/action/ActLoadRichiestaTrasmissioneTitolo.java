package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.action.ICostantiSiepJMS;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action che effettua la ricerca, sulla base dati locale, del Titolo da richiedere per competenza. Se trovato
 * passa i dati alla form di inserimento richiesta. Altrimenti restituisce un warning e redirect alla form di
 * inserimento
 *
 * @author d.fiorletta
 *
 */
public class ActLoadRichiestaTrasmissioneTitolo extends ActionModuloCumulo implements ICostantiModuloCumulo {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		// super.getDatiIstruttoria();

		// ==========================================================================
		// Recupera i dati di ricerca
		// ==========================================================================
		String lTipoUfficioRicerca = "";
		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO)
				&& getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO).length() > 0) {
			// Recupero i dati di ricerca
			BigDecimal lChiaveAnnoFasc = getRequestBigDecimalParameter(
					ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO);
			BigDecimal lChiaveProgrFasc = getRequestBigDecimalParameter(
					ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR);

			if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO) && !"0"
					.equals(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO))) {
				BigDecimal lIncrementoAccorpato = getRequestBigDecimalParameter(
						ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO);
				lChiaveProgrFasc = lChiaveProgrFasc.add(lIncrementoAccorpato);
			}

			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUffMod = new UfficioModel();
			if (!"-".equals(getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO))) {
				lUffMod = lUff.getUfficioByCodTipoUffDescrComune(
						getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO),
						getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO));
			}
			FascicoloSiepModel lFascModel = new FascicoloSiepModel();
			lFascModel.setChiaveAnno(lChiaveAnnoFasc);
			lFascModel.setChiaveProgr(lChiaveProgrFasc);
			lFascModel.setChiaveUfficio(lUffMod.getCodUfficio());

			// Ricerca Fascicolo, Soggetto e Sentenza
			IFascicoloSiep lFascCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			lFascModel = lFascCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFascModel);

			if (lFascModel != null) {
				setRequestAttribute("fascicoloDaRichiedere", lFascModel);
				setRequestAttribute("sentenzaFascicoloDaRichiedere", lFascModel.getSentenza());
				setRequestAttribute("insertManuale", "0");
			} else {
				// Se non trovo i dati restituisco in MSG di avviso reindirizzo alla form di
				// inserimento.

				// Provare a precaricare nella form almeno i dati della ricerca (anno/num/ufficio)
				setRequestAttribute("annoRicerca", lChiaveAnnoFasc.toString());
				setRequestAttribute("progrRicerca",
						getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR));
				setRequestAttribute("ufficioRicerca", lUffMod);
				setRequestAttribute("chiaveAccorpato",
						getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO));
				lTipoUfficioRicerca = lUffMod.getCodTipoUfficio();

				setRequestAttribute("insertManuale", "1");
			}

		} else {
			// Non provengo dalla form di ricerca. Carico la form di inserimento senza dati
			setRequestAttribute("insertManuale", "1");
		}

		// ==============================
		// Valorizzazione delle combo
		// ==============================
		// TIPO PROVVEDIMENTO
		Option lTipoProvv = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lTipoProvv.setFilter(new String[] { "-", "01", "02" });
		setRequestAttribute("tipoprovvedimento", "" + lTipoProvv);

		// Autorità emittente Titolo (sentenza/Decreto)
		Option lOptionAutEmi = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		setRequestAttribute("autoritaEmi", "" + lOptionAutEmi);

		// Tipo Ufficio Procedimento
		Option lOptionUfficioPM = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
		lOptionUfficioPM.setFilter(new String[] { "-", "PM", "PMM", "PGCAP" });
		lOptionUfficioPM.setSelected(lTipoUfficioRicerca);
		setRequestAttribute("ufficioPM", "" + lOptionUfficioPM);

		// Magistrato competene
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascicoloModel.getIdFascicoloSiep());
		setRequestAttribute("magistratocompetente", lMagi);

		// ALTRO DESTINATARIO
		Option lAEOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaN", "" + lAEOption);

		// tipo richiesta

		// Oggetto
		Option lOggetto = new Option(DecodificheManager.getInstance().getMotivoProvvedimentiRichGen());
		lOggetto.setFilter(new String[] { "0340" }); // FIXME Cumulo: prevedere altro codice
		setRequestAttribute("oggetto", "" + lOggetto);

		// Lista uffici accorpati di tipo 'PM', gli altri non sono di interesse per ora
		IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUffCtrl.ListaUfficiAccorpati("PM", null);
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);

		return PG_LOAD_RICHIESTA_TRASMISSIONE_TITOLO;
	}

}