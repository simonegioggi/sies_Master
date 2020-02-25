package siap.siep.modulocumulo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;

public class ModuloCumuloUtils {

	// Hashtable <CodPosGiu, String[Codici Motivo]>
	private Hashtable<String, String[]> PG_TIPO_PROVV = null;

	// Libero
	// private String[] aCodProvvLibero = new
	// String[]{"0630","0631","0632","0633","0634","0635","0656","0659"};

	// test x aggiungere Provvedimento Generico (0665) se Libero
	// private String[] aCodProvvLibero = new
	// String[]{"0630","0631","0632","0633","0634","0635","0656","0659","0665"};

  //private String[] aCodProvvLibero = new String[]{"0630","0631","0632","0633","0634","0635","0656","0659","0666","0667","0665"};
  // 14/05/2019 eliminato codice 0666 come da richiesta della commissione di collaudo
  private String[] aCodProvvLibero = new String[]{"0630","0631","0632","0633","0634","0635","0656","0659","0667","0665"};

	// Differimento
  private String[] aCodProvvDifferimento = new String[]{"0636","0637","0654","0656","0665"};
  private String[] aCodProvvDifferimentoProvv = new String[]{"0636","0637","0662","0656","0665"};
	// Custodia Cautelare per Questa Causa in Regime di Detenzione
  private String[] aCodProvvCustCautDetQC = new String[]{"0638","0657","0660","0665"};
	// Custodia Cautelare Arresti Domiciliari
  private String[] aCodProvvCustCautADQC = new String[]{"0639","0653","0658","0664","0665"};  
	// Custodia Cautelare per Altra Causa in Regime di Detenzione
  private String[] aCodProvvCustCautDetAC = new String[]{"0640","0661","0665"};
	// Custodia Cautelare per Altra Causa - Arresti Domiciliari
  private String[] aCodProvvCustCautADAC = new String[]{"0641","0642","0665"};
	// Detenuto
  private String[] aCodProvvDetenuto = new String[]{"0643","0657","0660","0665"};
	// Sospensione cautelativa delle 'misure alternative' 51 ter
  private String[] aCodProvvSospCaut = new String[]{"0644","0658","0665"};
	// Espiazione pena in arresti domiciliari
  private String[] aCodProvvEspPenaAD = new String[]{"0645","0663","0658","0665"};
	// Affidamento in prova
  private String[] aCodProvvAffProv = new String[]{"0646","0647","0665"};
	// Detenzione Domiciliare
  private String[] aCodProvvDetDom = new String[]{"0648","0649","0665"};

  private String[] aCodProvvDiffDetDom = new String[]{"0668","0665"};

	// Semilibertà
  private String[] aCodProvvSemilib = new String[]{"0650","0651","0665"};

	private boolean aIsGE = false;
	private String aCodProvvGE = "0652";

	private boolean aIsNLP = false;
	private String aCodNLP = "0655"; // non luogo a provvedere

	/**
	 * 
	 * @param isGE
	 *            - Indica se
	 * @param isNLP
	 */
	public ModuloCumuloUtils(boolean isGE, boolean isNLP) {
		aIsGE = isGE;
		aIsNLP = isNLP;

		PG_TIPO_PROVV = new Hashtable<>();

		// Libero
		String[] lCodProvvLibero = addGE(aCodProvvLibero);

		PG_TIPO_PROVV.put("10", lCodProvvLibero);
		PG_TIPO_PROVV.put("46", lCodProvvLibero);
		PG_TIPO_PROVV.put("47", lCodProvvLibero);
		PG_TIPO_PROVV.put("20", lCodProvvLibero);
		PG_TIPO_PROVV.put("26", lCodProvvLibero);

		// Differimento
		String[] lCodProvvDifferimento = addGE(aCodProvvDifferimento);

		PG_TIPO_PROVV.put("16", lCodProvvDifferimento);
		PG_TIPO_PROVV.put("17", aCodProvvDifferimentoProvv);

		// Custodia Cautelare per Questa Causa in Regime di Detenzione
		String[] lCodProvvCustCautDetQC = addGE(aCodProvvCustCautDetQC);

		PG_TIPO_PROVV.put("01", lCodProvvCustCautDetQC);

		// Custodia Cautelare Arresti Domiciliari
		String[] lCodProvvCustCautADQC = addGE(aCodProvvCustCautADQC);

		PG_TIPO_PROVV.put("02", lCodProvvCustCautADQC);
		PG_TIPO_PROVV.put("70", lCodProvvCustCautADQC);
		PG_TIPO_PROVV.put("71", lCodProvvCustCautADQC);
		PG_TIPO_PROVV.put("72", lCodProvvCustCautADQC);

		// Custodia Cautelare per Altra Causa in Regime di Detenzione
		String[] lCodProvvCustCautDetAC = addGE(aCodProvvCustCautDetAC);

		PG_TIPO_PROVV.put("76", lCodProvvCustCautDetAC);

		// Custodia Cautelare per Altra Causa - Arresti Domiciliari
		String[] lCodProvvCustCautADAC = addGE(aCodProvvCustCautADAC);

		PG_TIPO_PROVV.put("77", lCodProvvCustCautADAC);
		PG_TIPO_PROVV.put("78", lCodProvvCustCautADAC);
		PG_TIPO_PROVV.put("79", lCodProvvCustCautADAC);
		PG_TIPO_PROVV.put("80", lCodProvvCustCautADAC);
		PG_TIPO_PROVV.put("81", lCodProvvCustCautADAC);

		// Detenuto
		String[] lCodProvvDetenuto = addGE(aCodProvvDetenuto);

		PG_TIPO_PROVV.put("03", lCodProvvDetenuto);

		// Sospensione cautelativa delle misure alternative 51 ter
		String[] lCodProvvSospCaut = addGE(aCodProvvSospCaut);

		PG_TIPO_PROVV.put("62", lCodProvvSospCaut);
		PG_TIPO_PROVV.put("64", lCodProvvSospCaut);
		PG_TIPO_PROVV.put("65", lCodProvvSospCaut);

		// Espiazione pena in arresti domiciliari
		String[] lCodProvvEspPenaAD = addGE(aCodProvvEspPenaAD);

		PG_TIPO_PROVV.put("04", lCodProvvEspPenaAD);
		PG_TIPO_PROVV.put("82", lCodProvvEspPenaAD);
		PG_TIPO_PROVV.put("83", lCodProvvEspPenaAD);
    PG_TIPO_PROVV.put("50", lCodProvvEspPenaAD);   //MEV70 Abilitata Posizione Giuridica 

		// Affidamento in prova
		String[] lCodProvvAffProv = addGE(aCodProvvAffProv);

		PG_TIPO_PROVV.put("54", lCodProvvAffProv);
		PG_TIPO_PROVV.put("13", lCodProvvAffProv);
		PG_TIPO_PROVV.put("32", lCodProvvAffProv);

		// Detenzione Domiciliare
		String[] lCodProvvDetDom = addGE(aCodProvvDetDom);

		PG_TIPO_PROVV.put("29", lCodProvvDetDom);
		PG_TIPO_PROVV.put("12", lCodProvvDetDom);
		PG_TIPO_PROVV.put("31", lCodProvvDetDom);
		PG_TIPO_PROVV.put("25", lCodProvvDetDom);

		// Differimento nelle forme della Detenzione Domiciliare
		String[] lCodProvvDiffDetDom = addGE(aCodProvvDiffDetDom);
		PG_TIPO_PROVV.put("12Diff", lCodProvvDiffDetDom);

		// Semilibertà
		String[] lCodProvvSemilib = addGE(aCodProvvSemilib);

		PG_TIPO_PROVV.put("14", lCodProvvSemilib);
		PG_TIPO_PROVV.put("33", lCodProvvSemilib);
		PG_TIPO_PROVV.put("38", lCodProvvSemilib);

		// Arresti domiciliari - Esecuzione presso domicilio della pena detentiva
    PG_TIPO_PROVV.put("53", new String[]{"0658","0665"});

	}

	/**
	 * Restituisce il tipo provvedimento da emettere in funzione della posizione giuridica da utilizzare come
	 * filtro per la costruzione delle Option
	 * 
	 * @param aCodPosGiu
	 * @return
	 */
	// public String[] getCodMotivoByPosGiu (String aCodPosGiu) {
	// String[] lCodici = PG_TIPO_PROVV.get (aCodPosGiu);
	//
	// return lCodici;
	// }

	public String[] getCodMotivoByPosGiu(PosizioneGiuridicaCumuloModel aPosGiuCumModel) {
		String lCodPG = aPosGiuCumModel.getCodPosizioneGiuridica();

		if ("12".equals(lCodPG) && "S".equals(aPosGiuCumModel.getFlagDifferimentoDetDom())) {
			lCodPG = "12Diff";
		}

		String[] lCodici = PG_TIPO_PROVV.get(lCodPG);

		return lCodici;
	}

	/**
	 * Costruisce una NUOVA collection ottenuta da quella in input sostituendo il valore del campo Description
	 * con il valore del campo della CG_REF_CODES.RV_ABBREVIATION Utilizza in pratica una descrizione
	 * alternativa da utilizare nelle combo
	 */
	public Collection<DecodificheModel> getMotiviProvvCumuloNewAltDesc(
			Collection<DecodificheModel> aCollection) {
		Collection<DecodificheModel> newDescMotivo = new Vector<>();

		Iterator<DecodificheModel> iter = aCollection.iterator();

		while (iter.hasNext()) {
			DecodificheModel decOld = iter.next();

			DecodificheModel decNew = new DecodificheModel(decOld);
			decNew.setDescription(decOld.getFiltro());

			newDescMotivo.add(decNew);
		}

		return newDescMotivo;

	}

	/**
	 * Restituisce una String[] aggiungendo se necessario il codice motivo del provvedimento generico del GE
	 * 
	 * @param listaCodici
	 * @return
	 */
	private String[] addGE(String[] listaCodici) {
		String[] lNuoviCodici = null;
		List<String> lCodProvvAsList = Arrays.asList(listaCodici);

		// Serve per creare una lista modificabile. Arrays.asList restituisce
		// una lista non modificabile
		List<String> lCodProvvAsListNew = new ArrayList<>(lCodProvvAsList);

		if (aIsGE) {
			lCodProvvAsListNew.add(aCodProvvGE);
		}

		if (aIsNLP) {
			lCodProvvAsListNew.add(aCodNLP);
		}

		lNuoviCodici = lCodProvvAsListNew.toArray(new String[0]);

		return lNuoviCodici;
	}

	/**
	 * Verifica se il codice motivo in input è uno dei nuovi codici cumulo
	 * 
	 * @param aCodMotivo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isCumulo(String aCodMotivo) {
		boolean isCumulo = false;

		Collection lCodiciProvv = DecodificheManager.getInstance().getMotiviProvvCumuloNew();

		Iterator lIter = lCodiciProvv.iterator();

		while (lIter.hasNext()) {
			DecodificheModel lDecode = (DecodificheModel) lIter.next();

			if (aCodMotivo.equals(lDecode.getCode())) {
				isCumulo = true;
				break;
			}
		}

		return isCumulo;
	}

	/**
	 * Restitiusce true se la MEV 42 risulta NON disabilitata
	 * 
	 * @return
	 */
	public static boolean isMev42Abilitata() {
		boolean isMev42Abilitata = true;
		/*
		 * Collection <DecodificheModel> lCheckMev42 = DecodificheManager.getInstance().getDisattivaMev42();
		 * siesLogger.debug("lCheckMev42 = "+lCheckMev42); if (lCheckMev42==null) {
		 * siesLogger.debug("MEV42 ABILITATA"); isMev42Abilitata = true; } else {
		 * //siesLogger.debug("lCheckMev42.size() = "+lCheckMev42.size());
		 * siesLogger.debug("MEV42 DISABILITATA"); isMev42Abilitata = false; }
		 */
		return isMev42Abilitata;
	}

}