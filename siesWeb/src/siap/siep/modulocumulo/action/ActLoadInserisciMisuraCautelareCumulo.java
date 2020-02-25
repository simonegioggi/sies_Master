package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.modulocumulo.controller.IMisuraCautelareCumulo;
import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * Action richiamata per la load inserisci e modifica delle Misure Cautelari Cumulo
 * 
 * @author d.fiorletta
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ActLoadInserisciMisuraCautelareCumulo extends ActionModuloCumulo implements
		ICostantiMisuraCautelareCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		// I=Inserisci, M=Modifica, C=Cancella
		String lModalita = "I"; // default
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		MisuraCautelareCumuloModel lMisura = null;
		if ("M".equals(lModalita)) {
			// Recupero i dati
			BigDecimal idMisuraCautelare = getRequestBigDecimalParameter(CAMPO_ID_MISURA_CAUTELARE_CUMULO);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Sono in Modifica: recupero il record per id = " + idMisuraCautelare);

			IMisuraCautelareCumulo lCtrl = SIEPLookupRemote.getMisuraCautelareCumuloRemote();
			lMisura = lCtrl.ExRicercaMisuraCautelareCumuloById(idMisuraCautelare);

			setRequestAttribute("MisuraCautelareCumulo", lMisura);
		}

		setRequestAttribute("modalita", lModalita);

		// ==========================================================================
		// Caricamento Dati delle Combo
		// ==========================================================================
		/*
		 * // Tipo Ufficio PM RGNR Option lOptionUffPM = new Option(); lOptionUffPM = new Option(
		 * DecodificheManager.getInstance().getTipoUfficioPM()); //PGCAP = Procura Generale della Repubblica
		 * Presso la Corte D'Appello //PM = Procura della Repubblica Presso il Tribunale Ordinario //PMM =
		 * Procura della Repubblica Presso il Tribunale per i Minorenni lOptionUffPM.setFilter( new String[]
		 * {"-","PM","PMM","PGCAP"}); if (lMisura!=null && lMisura.getCodiceUfficioPmSede()!=null) {
		 * UfficioModel lUffPM = getUfficioByCodUfficio (lMisura.getCodiceUfficioPmSede());
		 * lOptionUffPM.setSelected (lUffPM.getCodTipoUfficio()); } setRequestAttribute("comboUfficioPM", "" +
		 * lOptionUffPM );
		 * 
		 * // Tipo Ufficio REG. GEN. Option tipoRegGen = new Option(
		 * DecodificheManager.getInstance().getTipoRegistroGenerale(), "-"); if (lMisura!=null &&
		 * lMisura.getTipoUfficioRegGen()!=null) tipoRegGen.setSelected(lMisura.getTipoUfficioRegGen());
		 * setRequestAttribute("comboTipoRegGen", "" + tipoRegGen );
		 * 
		 * // Autorita Emittente (tipo_autorita) Option lAutoritaEmittenteUff = new Option(
		 * DecodificheManager.getInstance().getTipoAutoritaEmittente()); if (lMisura!=null &&
		 * lMisura.getAutoritaEmittente()!=null) lAutoritaEmittenteUff.setSelected
		 * (lMisura.getAutoritaEmittente()); setRequestAttribute("comboAutEmittente", "" +
		 * lAutoritaEmittenteUff );
		 */

		// Tipo Misura - Detentiva/NON Detentiva
		Option lOptionDetentive = new Option(getCodiciMisureDetentive());
		Option lOptionNonDetentive = new Option(getCodiciMisureNonDetentive());

		if (lMisura != null && lMisura.getCodTipoMisura() != null) {
			if (DecodificheUtils.containsCode(getCodiciMisureDetentive(), lMisura.getCodTipoMisura())) {
				lOptionDetentive.setSelected(lMisura.getCodTipoMisura());
				setRequestAttribute("TipoEspiazione", VAL_TIPO_ESPIAZIONE_ISTITUTO);
			} else if (DecodificheUtils.containsCode(getCodiciMisureNonDetentive(),
					lMisura.getCodTipoMisura())) {
				lOptionNonDetentive.setSelected(lMisura.getCodTipoMisura());
				setRequestAttribute("TipoEspiazione", VAL_TIPO_ESPIAZIONE_ALTRO);
			}
		}

		setRequestAttribute("tipoMisuraDetentive", "" + lOptionDetentive);
		setRequestAttribute("tipoMisuraNonDetentive", "" + lOptionNonDetentive);

		//
		if (lMisura != null && lMisura.getIstDetIdIstitutoDetenzione() != null
				&& !"".equals(lMisura.getIstDetIdIstitutoDetenzione())) {
			IIstitutoDetenzione lCtrlIstituto = SIEPLookupRemote.getIstitutoDetenzioneRemote();
			IstitutoDetenzioneModel lIstitutoModel = lCtrlIstituto.ExRicercaIstitutoDetenzioneByKey(lMisura
					.getIstDetIdIstitutoDetenzione());
			setRequestAttribute("IstitutoDetenzione", lIstitutoModel);
		}

		// Combo Autorità Competente Per territorio (non detentiva)
		Option lOptionAutoritaCompTerritorio = new Option(DecodificheManager.getInstance().getTipoAutorita());
		lOptionAutoritaCompTerritorio.setFilter(new String[] { "-", "19", "20", "28", "32", "92" });

		if (lMisura != null && lMisura.getAutoritaCompetente() != null) {
			lOptionAutoritaCompTerritorio.setSelected(lMisura.getAutoritaCompetente());
		}
		setRequestAttribute("comboAutCompTerritorio", "" + lOptionAutoritaCompTerritorio);

		// NON COMPUTABILI
		// Motivo non computabilità
		// Option lOptionNonComp = new Option( DecodificheManager.getInstance().getMotivoNonComputabile());
		// setRequestAttribute("comboNonComp", "" + lOptionNonComp );

		// Tipo Ufficio Autorità Emittente (non computabilità) n.b le stesse dell'autorità Emittete prima
		// sezione
		// setRequestAttribute("comboTipoUffAutEmi", "" + lAutoritaEmittenteUff );

		return PG_LOAD_INSERISCI_MISURA_CAUTELARE_CUMULO;
	}

	/*
	 * Solo per debug
	 */
	private Collection getCodiciMisureDetentive() {

		Collection lCodMisureDetentiveCollection = new Vector();

		DecodificheModel lDec = new DecodificheModel();
		lDec.setCode("-");
		lDec.setDescription("-");
		lCodMisureDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("CA");
		lDec.setDescription("Custodia cautelare in carcere");
		lCodMisureDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("CD");
		lDec.setDescription("Custodia Cautelare in Misura di Sicurezza Applicata in via Provvisoria");
		lCodMisureDetentiveCollection.add(lDec);

		return lCodMisureDetentiveCollection;
	}

	private Collection getCodiciMisureNonDetentive() {

		Collection lCodMisureNonDetentiveCollection = new Vector();

		DecodificheModel lDec = new DecodificheModel();
		lDec.setCode("-");
		lDec.setDescription("-");
		lCodMisureNonDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("AD");
		lDec.setDescription("Custodia cautelare in Arresti domiciliari");
		lCodMisureNonDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("CB");
		lDec.setDescription("Custodia Cautelare in Regime di Permanenza in Casa");
		lCodMisureNonDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("CC");
		lDec.setDescription("Custodia Cautelare in Collocamento in Comunita'");
		lCodMisureNonDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("CE");
		lDec.setDescription("Custodia cautelare in camera di sicurezza");
		lCodMisureNonDetentiveCollection.add(lDec);

		// 20170802: modifica su richiesta Amministrazione --> tolgo CL
		// lDec = new DecodificheModel();
		// lDec.setCode("CL");
		// lDec.setDescription("Computo periodo di messa alla prova");
		// lCodMisureNonDetentiveCollection.add(lDec);

		return lCodMisureNonDetentiveCollection;
	}

}