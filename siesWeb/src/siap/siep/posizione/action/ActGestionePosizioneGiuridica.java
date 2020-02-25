package siap.siep.posizione.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.action.ICostantiAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.action.ICostantiMisuraCautelare;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
* <p>Title: ActInserisciPosizioneGiuridica</p>
* <p>Description: Classe Action per l'inserimento di PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActGestionePosizioneGiuridica extends ActionSiap implements ICostantiPosizioneGiuridica {

	protected boolean isFilled(String val) {
		boolean ret = false;
		if (val != null && !"".equals(val) && !"-".equals(val)) {
			ret = true;
		}
		return ret;
	}

	protected String translatePosizioneGiuridicaAltraCausa(String code, String posGiuridicaCombo) {
		String ret = posGiuridicaCombo;
		Map<String, String> mappa = new HashMap<String, String>();
//		mappa.put("22", "74"); // POSIZIONE_GIURIDICA - ALTRA_CAUSA / Custodia Cautelare in Regime di Detenzione [22] ---> POSIZIONE_GIURIDICA / Libero - Espiazione pena per Altra Causa in Regime di Detenzione [74]
//		mappa.put("24", "75"); // POSIZIONE_GIURIDICA - ALTRA_CAUSA / Espiazione Pena Definitiva in Carcere [24] ---> POSIZIONE_GIURIDICA / Libero - Espiazione pena per Altra Causa in Misura Sicurezza Detentiva (Internato) [75]
		mappa.put("CF", "76"); // TIPO_MISURA_CAUTELARE / Custodia Cautelare per Altra Causa in Regime di Detenzione [CF] ---> POSIZIONE_GIURIDICA / Libero - Custodia Cautelare per Altra Causa in Regime di Detenzione [76]
		mappa.put("CG", "77"); // TIPO_MISURA_CAUTELARE / Espiazione pena per Altra Causa in Misura di Sicurezza Applicata in Via Provvisoria [CG] ---> POSIZIONE_GIURIDICA / Libero - Espiazione pena per Altra Causa in Misura di Sicurezza Applicata in Via Provvisoria [77]
		mappa.put("CH", "78"); // TIPO_MISURA_CAUTELARE / Custodia Cautelare per Altra Causa - Regime di Arresti Domiciliari [CH] ---> POSIZIONE_GIURIDICA / Libero - Custodia Cautelare per Altra Causa - Regime di Arresti Domiciliari [78]
		mappa.put("CI", "79"); // TIPO_MISURA_CAUTELARE / Custodia Cautelare per Altra Causa - Regime Permanenza in Casa [CI] ---> POSIZIONE_GIURIDICA / Libero - Custodia Cautelare per Altra Causa - Regime Permanenza in Casa [79]
		mappa.put("CJ", "80"); // TIPO_MISURA_CAUTELARE / Custodia Cautelare per Altra Causa - Collocamento in Comunita' [CJ] ---> POSIZIONE_GIURIDICA / Libero - Custodia Cautelare per Altra Causa - Collocamento in Comunità [80]
		mappa.put("CK", "81"); // TIPO_MISURA_CAUTELARE / Custodia Cautelare per Altra Causa - Regime di Arresti Domiciliari ex art 89 dpr 309/90 [CK] ---> POSIZIONE_GIURIDICA / Libero - Custodia Cautelare per Altra Causa - Regime di Arresti Domiciliari ex art 89 dpr 309/90 [81]		
		mappa.put("EA", "74"); // TIPO_MISURA_CAUTELARE / Espiazione pena per Altra Causa in Regime di Detenzione (cod. EA) --> POSIZIONE_GIURIDICA / Libero - Espiazione pena per Altra Causa in Regime di Detenzione (cod. 74)
		mappa.put("EB", "75"); // TIPO_MISURA_CAUTELARE / Espiazione pena per Altra Causa  in Misura Sicurezza  Detentiva (Internato) (cod. EB) ---> POSIZIONE_GIURIDICA / Libero - Espiazione pena per Altra Causa  in Misura Sicurezza  Detentiva (Internato) (cod. 75)
		
		if (mappa.containsKey(code)) {
			ret = mappa.get(code);
		}
		return ret;
	}

	protected void checkFlagAltraCausa(FascicoloSiepModel lFasMod, String codMaschera) {

		if (CAMPO_TIPO_MASCHERA_Libero.equals(codMaschera) // L
		) {
			lFasMod.setFlagAltraCausa("N");

		} else if (CAMPO_TIPO_MASCHERA_LiberoIstituto.equals(codMaschera) // L1
				|| CAMPO_TIPO_MASCHERA_LiberoCautelareIstituto.equals(codMaschera) // L2
				|| CAMPO_TIPO_MASCHERA_LiberoCautelareAltro.equals(codMaschera)// L3
		) {
			lFasMod.setFlagAltraCausa("S");

		} else if (CAMPO_TIPO_MASCHERA_EspiazioneIstituto.equals(codMaschera) // EI
				|| CAMPO_TIPO_MASCHERA_EspiazioneAltro.equals(codMaschera)// EA
		) {
			// empty
			lFasMod.setFlagAltraCausa("");

		} else { // old
			// check flag altra causa
			if (isRequestChecked(ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA))
				lFasMod.setFlagAltraCausa("S");
			else
				lFasMod.setFlagAltraCausa("N");
		}

	}

	protected MisuraCautelareModel getMisuraCautelare(BigDecimal lIdFascicoloSiepAssociato, String codMaschera) throws F3BException {
		MisuraCautelareModel lMisuraCautelare = new MisuraCautelareModel();

		if (CAMPO_TIPO_MASCHERA_Libero.equals(codMaschera) // L
				|| CAMPO_TIPO_MASCHERA_LiberoIstituto.equals(codMaschera) // L1
				|| CAMPO_TIPO_MASCHERA_EspiazioneIstituto.equals(codMaschera) // EI
				//|| CAMPO_TIPO_MASCHERA_EspiazioneAltro.equals(codMaschera)// EA
		) {
			// nothing
			return null;

		} else if (CAMPO_TIPO_MASCHERA_LiberoCautelareIstituto.equals(codMaschera) // L2
		) {

			String subSez = "_L2";
			lMisuraCautelare.setAnnoFascBdmc(getRequestBigDecimalParameter(ICostantiMisuraCautelare.CAMPO_ANNO_FASC_BDMC + subSez));
			lMisuraCautelare.setNumeFascBdmc(getRequestBigDecimalParameter(ICostantiMisuraCautelare.CAMPO_NUME_FASC_BDMC + subSez));

			String lUfficioPMSede = getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE + subSez);
			String lUfficioPMTipo = getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE + "_Tipo" + subSez);
			if (isFilled(lUfficioPMSede) && isFilled(lUfficioPMTipo)) {
				lMisuraCautelare.setCodiceUfficioPmSede(UfficioUtils.getCodUfficioByCodTipoUfficioDescrComune(lUfficioPMTipo, lUfficioPMSede));
			}

			lMisuraCautelare.setAnnoRgnr(getRequestBigDecimalParameter(ICostantiMisuraCautelare.CAMPO_ANNO_RGNR + subSez));
			lMisuraCautelare.setNumeroRgnr(getRequestBigDecimalParameter(ICostantiMisuraCautelare.CAMPO_NUMERO_RGNR + subSez));

			lMisuraCautelare.setAnnoRegGen(getRequestBigDecimalParameter(ICostantiMisuraCautelare.CAMPO_ANNO_REG_GEN + subSez));
			lMisuraCautelare.setNumeroRegGen(getRequestBigDecimalParameter(ICostantiMisuraCautelare.CAMPO_NUMERO_REG_GEN + subSez));
			lMisuraCautelare.setTipoUfficioRegGen(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_TIPO_UFFICIO_REG_GEN + subSez));

			String lDescrLuogoAutorita = getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE_LUOGO + subSez);
			lMisuraCautelare.setAutoritaEmittente(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE + subSez));
			lMisuraCautelare.setAutoritaEmittenteLuogo(getCodComuneByDescr(lDescrLuogoAutorita).getCodComune());

			lMisuraCautelare.setCodTipoMisura(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA + subSez));
			lMisuraCautelare.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE + subSez));
			
		    String giornoEmissioneOrdinanza = this.getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA + subSez);  
		    String meseEmissioneOrdinanza = this.getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA + subSez);  
		    String annoEmissioneOrdinanza = this.getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA + subSez);
			lMisuraCautelare.setDataEmissioneOrdinanza(DateUtils.getDate(annoEmissioneOrdinanza,meseEmissioneOrdinanza,giornoEmissioneOrdinanza));
		} else if (CAMPO_TIPO_MASCHERA_LiberoCautelareAltro.equals(codMaschera)// L3
		) {

			String subSez = "_L3";
			lMisuraCautelare.setAnnoFascBdmc(getRequestBigDecimalParameter(ICostantiMisuraCautelare.CAMPO_ANNO_FASC_BDMC + subSez));
			lMisuraCautelare.setNumeFascBdmc(getRequestBigDecimalParameter(ICostantiMisuraCautelare.CAMPO_NUME_FASC_BDMC + subSez));

			String lUfficioPMSede = getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE + subSez);
			String lUfficioPMTipo = getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE + "_Tipo" + subSez);
			if (isFilled(lUfficioPMSede) && isFilled(lUfficioPMTipo)) {
				lMisuraCautelare.setCodiceUfficioPmSede(UfficioUtils.getCodUfficioByCodTipoUfficioDescrComune(lUfficioPMTipo, lUfficioPMSede));
			}

			lMisuraCautelare.setAnnoRgnr(getRequestBigDecimalParameter(ICostantiMisuraCautelare.CAMPO_ANNO_RGNR + subSez));
			lMisuraCautelare.setNumeroRgnr(getRequestBigDecimalParameter(ICostantiMisuraCautelare.CAMPO_NUMERO_RGNR + subSez));

			lMisuraCautelare.setAnnoRegGen(getRequestBigDecimalParameter(ICostantiMisuraCautelare.CAMPO_ANNO_REG_GEN + subSez));
			lMisuraCautelare.setNumeroRegGen(getRequestBigDecimalParameter(ICostantiMisuraCautelare.CAMPO_NUMERO_REG_GEN + subSez));
			lMisuraCautelare.setTipoUfficioRegGen(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_TIPO_UFFICIO_REG_GEN + subSez));

			lMisuraCautelare.setAutoritaEmittente(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE + subSez));
			String lAutEmittLuogo = getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE_LUOGO + subSez);
			lMisuraCautelare.setAutoritaEmittenteLuogo(getCodComuneByDescr(lAutEmittLuogo).getCodComune());

			lMisuraCautelare.setAutoritaCompetente(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_AUTORITA_COMPETENTE + subSez));
			String lAutCompSede = getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_AUTORITA_COMPETENTE_SEDE + subSez);
			lMisuraCautelare.setAutoritaCompetenteSede(getCodComuneByDescr(lAutCompSede).getCodComune());
			lMisuraCautelare.setAutoritaCompetenteIndirizzo(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_AUTORITA_COMPETENTE_INDIRIZZO + subSez));

			lMisuraCautelare.setCodTipoMisura(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA + subSez));
			lMisuraCautelare.setAltroLuogoDetenzione(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_ALTRO_LUOGO_DETENZIONE + subSez));
			
			String giornoEmissioneOrdinanza = this.getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA + subSez);  
			String meseEmissioneOrdinanza = this.getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA + subSez);  
			String annoEmissioneOrdinanza = this.getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA + subSez);
			lMisuraCautelare.setDataEmissioneOrdinanza(DateUtils.getDate(annoEmissioneOrdinanza,meseEmissioneOrdinanza,giornoEmissioneOrdinanza));

		} 
		else if (CAMPO_TIPO_MASCHERA_EspiazioneAltro.equals(codMaschera)// EA
		) {
			lMisuraCautelare.setAutoritaCompetente(getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_AUTORITA_COMPETENTE));
			String lAutCompSede = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_AUTORITA_COMPETENTE_SEDE);
			lMisuraCautelare.setAutoritaCompetenteSede(getCodComuneByDescr(lAutCompSede).getCodComune());
			lMisuraCautelare.setAutoritaCompetenteIndirizzo(getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_AUTORITA_COMPETENTE_INDIRIZZO));
			lMisuraCautelare.setAltroLuogoDetenzione(getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_LUOGO_ESPIAZIONE));
		}
		else { // old
			// nothing
			return null;

		}

		// valori di default...
		lMisuraCautelare.setCodTipoUfficioRifer("-");
		lMisuraCautelare.setCodLuogoUfficioRifer("-");
		lMisuraCautelare.setCodMotivoNonComputabile("-");
		lMisuraCautelare.setFlagComputabile("S");

		lMisuraCautelare.setCodOperatoreInserimento(getCodUtenteConnesso());
		lMisuraCautelare.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lMisuraCautelare.setDataInserimento(DateUtils.getSysDate());

		lMisuraCautelare.setFasSieIdFascicoloSiep(lIdFascicoloSiepAssociato);

		return lMisuraCautelare;
	}

	protected LuogoDetenzioneModel getLuogoDetenzione(BigDecimal lIdFascicoloSiepAssociato, String codMaschera) throws F3BException {
		LuogoDetenzioneModel lLuogoDetenzione = new LuogoDetenzioneModel();

		if (CAMPO_TIPO_MASCHERA_Libero.equals(codMaschera) // L
				//|| CAMPO_TIPO_MASCHERA_LiberoIstituto.equals(codMaschera) // L1
				//|| CAMPO_TIPO_MASCHERA_LiberoCautelareIstituto.equals(codMaschera) // L2
				|| CAMPO_TIPO_MASCHERA_LiberoCautelareAltro.equals(codMaschera)// L3
				|| CAMPO_TIPO_MASCHERA_EspiazioneAltro.equals(codMaschera)// EA
		) {

			// empty record

		} 
		else if (CAMPO_TIPO_MASCHERA_LiberoIstituto.equals(codMaschera) // L1
		) {																					
			lLuogoDetenzione.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE + "_L1"));
		} 
		else if (CAMPO_TIPO_MASCHERA_LiberoCautelareIstituto.equals(codMaschera) // L2
		) {
																					
			//lLuogoDetenzione.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE + "_L2"));
			lLuogoDetenzione.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE + "_L2"));

		} 		
		else if (CAMPO_TIPO_MASCHERA_EspiazioneIstituto.equals(codMaschera) // EI
		) {

			lLuogoDetenzione.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE + "_EI"));

		} 	
//		else if (CAMPO_TIPO_MASCHERA_EspiazioneAltro.equals(codMaschera)// EA 
//		) {
//
//			lLuogoDetenzione.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE + "_EA"));
//
//		} 	
		else { // old

			// Gestione Luogo Detenzione

			// modifica relativa al tipo istituto
			String lCodTipoIstitutoDetenzione = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
			// String lCodTipoIstituto = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_COD_TIPO_ISTITUTO);
			// String lDescrLuogoDete = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_COD_LUOGO);
			String lAltroLuogo = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_ALTRO_LUOGO);

			// modifica relativa al tipo istituto
			lLuogoDetenzione.setIstDetIdIstitutoDetenzione(lCodTipoIstitutoDetenzione);
			// lLuogoDetenzione.setCodTipoIstituto(lCodTipoIstituto);
			// lLuogoDetenzione.setCodLuogo(getCodComuneByDescr(lDescrLuogoDete).getCodComune());
			// lLuogoDetenzione.setIndirizzo(lAltroLuogo); // !!!ALTRO LUOGO VA NEL CAMPO INDIRIZZO!!!!
			lLuogoDetenzione.setAltroLuogo(lAltroLuogo);
			lLuogoDetenzione.setDescrTipoIstituto("");

			lLuogoDetenzione.setDataInizioDetenzione(DateUtils.getSysDate()); // ?
			// lLuogoDetenzione.setDataFineDetenzione( **** ); // ?
			// **** lLuogoDetenzione.setPosGiuIdPosizioneGiuridica( lIdPosizione );

		}

		lLuogoDetenzione.setFasSieIdFascicoloSiep(lIdFascicoloSiepAssociato);
		lLuogoDetenzione.setFasSiuIdFascicoloSius(getRequestBigDecimalParameter(ICostantiLuogoDetenzione.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS));

		lLuogoDetenzione.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLuogoDetenzione.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLuogoDetenzione.setDataInserimento(DateUtils.getSysDate());

		return lLuogoDetenzione;
	}

	protected AltraCausaModel getAltraCausa(BigDecimal lIdFascicoloSiepAssociato, String codMaschera) throws F3BException {
		AltraCausaModel lAltraCausa = new AltraCausaModel();

		if (CAMPO_TIPO_MASCHERA_Libero.equals(codMaschera) // L
				|| CAMPO_TIPO_MASCHERA_EspiazioneIstituto.equals(codMaschera)// EI
				|| CAMPO_TIPO_MASCHERA_EspiazioneAltro.equals(codMaschera)// EA
		) {
			// nothing
			return null;

		} else if (CAMPO_TIPO_MASCHERA_LiberoIstituto.equals(codMaschera) // L1
		) {

			String lCodTipoAutorita = getRequestStringParameter(ICostantiAltraCausa.CAMPO_COD_AUTORITA);
			String lDescrLuogoAutorita = getRequestStringParameter(ICostantiAltraCausa.CAMPO_COD_LUOGO);
			// Controllo esistenza ufficio
			if (isFilled(lCodTipoAutorita) || isFilled(lDescrLuogoAutorita)) {
				getCodUfficioByCodTipoUfficioDescrComune(lCodTipoAutorita, lDescrLuogoAutorita);
			}			
			//lAltraCausa.setCodTipoPosGiuridica(getRequestStringParameter(ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA));
			String posGiuridicaCombo = getRequestStringParameter(CAMPO_COD_POSIZIONE_GIURIDICA);
			lAltraCausa.setCodTipoPosGiuridica(translatePosizioneGiuridicaAltraCausa(getRequestStringParameter(ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA), posGiuridicaCombo));
			lAltraCausa.setAnno(getRequestBigDecimalParameter(ICostantiAltraCausa.CAMPO_ANNO));
			lAltraCausa.setNumero(getRequestStringParameter(ICostantiAltraCausa.CAMPO_NUMERO));

			lAltraCausa.setCodAutorita(lCodTipoAutorita);
			lAltraCausa.setCodLuogo(getCodComuneByDescr(lDescrLuogoAutorita).getCodComune());

			lAltraCausa.setDataScadenza(getRequestDateParameter(ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA, ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA, ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA));

			lAltraCausa.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE + "_L1"));

		} else if (CAMPO_TIPO_MASCHERA_LiberoCautelareIstituto.equals(codMaschera) // L2
		) {
			//lAltraCausa.setCodTipoPosGiuridica("-");          
			String posGiuridicaCombo = getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA+ "_L2");
			lAltraCausa.setCodTipoPosGiuridica(translatePosizioneGiuridicaAltraCausa(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA+ "_L2"), posGiuridicaCombo)); 
			lAltraCausa.setCodAutorita("-");
			lAltraCausa.setCodLuogo("-");
			lAltraCausa.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE + "_L2"));

		} else if (CAMPO_TIPO_MASCHERA_LiberoCautelareAltro.equals(codMaschera)// L3
		) {
			//lAltraCausa.setCodTipoPosGiuridica(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA + "_L3"));		
			String posGiuridicaCombo = getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA+ "_L3");
			lAltraCausa.setCodTipoPosGiuridica(translatePosizioneGiuridicaAltraCausa(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA+ "_L3"), posGiuridicaCombo)); 
			lAltraCausa.setCodAutorita("-");
			lAltraCausa.setCodLuogo("-");
		} else { // old

			// Gestione Altra Causa

			// modifica relativa al tipo istituto
			// String lDescrLuogo = getRequestStringParameter(ICostantiAltraCausa.CAMPO_COD_LUOGO_ISTITUTO);
			String lCodTipoAutorita = getRequestStringParameter(ICostantiAltraCausa.CAMPO_COD_AUTORITA);
			String lDescrLuogoAutorita = getRequestStringParameter(ICostantiAltraCausa.CAMPO_COD_LUOGO);
			// Controllo esistenza ufficio
			if (isFilled(lCodTipoAutorita) || isFilled(lDescrLuogoAutorita)) {
				getCodUfficioByCodTipoUfficioDescrComune(lCodTipoAutorita, lDescrLuogoAutorita);
			}

			lAltraCausa.setCodTipoPosGiuridica(getRequestStringParameter(ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA));
			if (!this.isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_ANNO_DATA_DECORRENZA) && !this.isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_MESE_DATA_DECORRENZA) && !this.isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_GIORNO_DATA_DECORRENZA)) {
				lAltraCausa.setDataDecorrenza(getRequestDateParameter(ICostantiAltraCausa.CAMPO_ANNO_DATA_DECORRENZA, ICostantiAltraCausa.CAMPO_MESE_DATA_DECORRENZA, ICostantiAltraCausa.CAMPO_GIORNO_DATA_DECORRENZA));
			}
			if (!this.isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA) && !this.isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA) && !this.isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA)) {
				lAltraCausa.setDataScadenza(getRequestDateParameter(ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA, ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA, ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA));
			}

			// modifica relativa al tipo istituto
			lAltraCausa.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
			// lAltraCausa.setCodTipoIstituto( getRequestStringParameter(ICostantiAltraCausa.CAMPO_COD_TIPO_ISTITUTO) );
			// lAltraCausa.setCodLuogoIstituto( getCodComuneByDescr(lDescrLuogo).getCodComune() );
			lAltraCausa.setAltroLuogo(getRequestStringParameter(ICostantiAltraCausa.CAMPO_ALTRO_LUOGO_ALTRA));
			lAltraCausa.setAnno(getRequestBigDecimalParameter(ICostantiAltraCausa.CAMPO_ANNO));
			lAltraCausa.setNumero(getRequestStringParameter(ICostantiAltraCausa.CAMPO_NUMERO));
			lAltraCausa.setData(getRequestDateParameter(ICostantiAltraCausa.CAMPO_ANNO_DATA, ICostantiAltraCausa.CAMPO_MESE_DATA, ICostantiAltraCausa.CAMPO_GIORNO_DATA));

			lAltraCausa.setCodAutorita(lCodTipoAutorita);
			lAltraCausa.setCodLuogo(getCodComuneByDescr(lDescrLuogoAutorita).getCodComune());

		}

		lAltraCausa.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAltraCausa.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAltraCausa.setDataInserimento(DateUtils.getSysDate());

		lAltraCausa.setFasSieIdFascicoloSiep(lIdFascicoloSiepAssociato);

		return lAltraCausa;
	}

	protected PosizioneGiuridicaModel getPosizioneGiuridica(BigDecimal lIdFascicoloSiepAssociato, String codMaschera) throws F3BException {

		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		String posGiuridicaCombo = getRequestStringParameter(CAMPO_COD_POSIZIONE_GIURIDICA);
		lPosMod.setFasSieIdFascicoloSiep(lIdFascicoloSiepAssociato);
		lPosMod.setIdPosizioneGiuridica(getRequestBigDecimalParameter(CAMPO_ID_POSIZIONE_GIURIDICA));
		lPosMod.setCodPosizioneGiuridica(posGiuridicaCombo);
		lPosMod.setCodPosizioneProcessuale(getRequestStringParameter(CAMPO_COD_POSIZIONE_PROCESSUALE));

		if (CAMPO_TIPO_MASCHERA_Libero.equals(codMaschera) // L
		) {

		} else if (CAMPO_TIPO_MASCHERA_LiberoIstituto.equals(codMaschera)// L1
		) {
			//lPosMod.setCodPosizioneGiuridica(translatePosizioneGiuridicaAltraCausa(getRequestStringParameter(ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA), posGiuridicaCombo)); // L1
			lPosMod.setAltCauIdAltraCausa(new BigDecimal(translatePosizioneGiuridicaAltraCausa(getRequestStringParameter(ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA), posGiuridicaCombo))); // L1

		} else if (CAMPO_TIPO_MASCHERA_LiberoCautelareIstituto.equals(codMaschera)// L2
		) {
			//lPosMod.setCodPosizioneGiuridica(translatePosizioneGiuridicaAltraCausa(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA + "_L2"), posGiuridicaCombo)); // L2
			lPosMod.setAltCauIdAltraCausa(new BigDecimal(translatePosizioneGiuridicaAltraCausa(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA + "_L2"), posGiuridicaCombo))); // L2

		} else if (CAMPO_TIPO_MASCHERA_LiberoCautelareAltro.equals(codMaschera)// L3
		) {
			//lPosMod.setCodPosizioneGiuridica(translatePosizioneGiuridicaAltraCausa(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA + "_L3"), posGiuridicaCombo)); // L3
			lPosMod.setAltCauIdAltraCausa(new BigDecimal(translatePosizioneGiuridicaAltraCausa(getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA + "_L3"), posGiuridicaCombo))); // L3

		} else if (CAMPO_TIPO_MASCHERA_EspiazioneIstituto.equals(codMaschera)// EI
		) {
			lPosMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO));

		} else if (CAMPO_TIPO_MASCHERA_EspiazioneAltro.equals(codMaschera)// EA
		) {
			lPosMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO));

			lPosMod.setLuogoEspiazione(getRequestStringParameter(CAMPO_LUOGO_ESPIAZIONE));
			lPosMod.setAutoritaCompetente(getRequestStringParameter(CAMPO_AUTORITA_COMPETENTE));

			String lAutCompSede = getRequestStringParameter(CAMPO_AUTORITA_COMPETENTE_SEDE);
			lPosMod.setAutoritaCompetenteSede(getCodComuneByDescr(lAutCompSede).getCodComune());
			lPosMod.setAutoritaCompetenteIndirizzo(getRequestStringParameter(CAMPO_AUTORITA_COMPETENTE_INDIRIZZO));
			//lPosMod.setAutoritaCompetenteSedeDesc(getRequestStringParameter(CAMPO_AUTORITA_COMPETENTE_SEDE));

		} else { // old
			lPosMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO));
			lPosMod.setLuogoProvaAffidamento(getRequestStringParameter(CAMPO_LUOGO_PROVA_AFFIDAMENTO));
			lPosMod.setLuogoLavoroSemiliberta(getRequestStringParameter(CAMPO_LUOGO_LAVORO_SEMILIBERTA));
		}

		lPosMod.setCodMaschera(codMaschera);

		lPosMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPosMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPosMod.setDataInserimento(DateUtils.getSysDate());

		return lPosMod;
	}

}
