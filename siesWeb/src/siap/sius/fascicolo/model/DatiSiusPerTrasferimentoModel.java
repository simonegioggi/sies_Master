package siap.sius.fascicolo.model;

import java.util.List;

import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: DatiSiusPerTrasferimentoModel
 * </p>
 * <p>
 * Description: Altri Dati Siep per il trasferimento del FASCICOLO_SIEP
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 */
@SuppressWarnings("rawtypes")
public class DatiSiusPerTrasferimentoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6984607548494395574L;

	private List mListResidenzaFasSius;
	private LuogoDetenzioneModel mLuogoDetenzione;
	private MagistratoRelatoreModel mMagistratoRelatore;
	private List mListAvvocatiFasSius;
	private List mListNote;
	private EsecuzioneSanzioneSostitutivaModel mESS; // 22/02/2008
	private List mListPAS; // 22/02/2008
	private ScambioSanzioneModel mScambioSanzione; // 22/02/2008
	private List mListRCPP; // 23/03/2009 Conversione Pene Pecuniarie.
	private List mListMSA; // 08/01/2014 Misure di Sicurezza Applicate.

	// COSTRUTTORE DI DEFAULT
	public DatiSiusPerTrasferimentoModel() {
		mListResidenzaFasSius = null;
		mLuogoDetenzione = new LuogoDetenzioneModel();
		mMagistratoRelatore = new MagistratoRelatoreModel();
		mListAvvocatiFasSius = null;
		mListNote = null;
		mESS = new EsecuzioneSanzioneSostitutivaModel();
		mListPAS = null;
		mScambioSanzione = new ScambioSanzioneModel();
		mListRCPP = null;
		mListMSA = null; // 08/01/2015
	}

	public DatiSiusPerTrasferimentoModel(List aListResidenzaFasSius, LuogoDetenzioneModel aLuogoDetenzione,
			MagistratoRelatoreModel aMagistratoRelatore, List aListAvvocatiFasSius, List aListNote,
			EsecuzioneSanzioneSostitutivaModel aESS, List aListPAS, ScambioSanzioneModel aScambioSanzione,
			List aListRCPP, List aListMSA) {
		mListResidenzaFasSius = aListResidenzaFasSius;
		mLuogoDetenzione = aLuogoDetenzione;
		mMagistratoRelatore = aMagistratoRelatore;
		mListAvvocatiFasSius = aListAvvocatiFasSius;
		mListNote = aListNote;
		mESS = aESS;
		mListPAS = aListPAS;
		mScambioSanzione = aScambioSanzione;
		mListRCPP = aListRCPP;
		mListMSA = aListMSA; // 08/01/2015
	}

	//
	// METODI GET()
	//
	public List getListResidenzaFasSius() {
		return mListResidenzaFasSius;
	}

	public LuogoDetenzioneModel getLuogoDetenzione() {
		return mLuogoDetenzione;
	}

	public MagistratoRelatoreModel getMagistratoRelatore() {
		return mMagistratoRelatore;
	}

	public List getAvvocatiFasSius() {
		return mListAvvocatiFasSius;
	}

	public List getNote() {
		return mListNote;
	}

	public EsecuzioneSanzioneSostitutivaModel getESS() {
		return mESS;
	}

	public List getPAS() {
		return mListPAS;
	}

	public ScambioSanzioneModel getSS() {
		return mScambioSanzione;
	}

	public List getRCPP() {
		return mListRCPP;
	}

	public List getMSA() {
		return mListMSA;
	} // 08/01/2015

	//
	// METODI SET()
	//
	public void setListResidenzaFasSius(List aValore) {
		mListResidenzaFasSius = aValore;
	}

	public void setLuogoDetenzione(LuogoDetenzioneModel aValore) {
		mLuogoDetenzione = aValore;
	}

	public void setMagistratoRelatore(MagistratoRelatoreModel aValore) {
		mMagistratoRelatore = aValore;
	}

	public void setListAvvocatiFasSius(List aValore) {
		mListAvvocatiFasSius = aValore;
	}

	public void setNote(List aValore) {
		mListNote = aValore;
	}

	public void setESS(EsecuzioneSanzioneSostitutivaModel aValore) {
		mESS = aValore;
	}

	public void setPAS(List aValore) {
		mListPAS = aValore;
	}

	public void setSS(ScambioSanzioneModel aValore) {
		mScambioSanzione = aValore;
	}

	public void setRCPP(List aValore) {
		mListRCPP = aValore;
	}

	public void setMSA(List aValore) {
		mListMSA = aValore;
	} // 08/01/2015

}