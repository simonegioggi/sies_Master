package siap.siep.statis.model;

/**
* <p>Title: RiepilogoPendentiDefinitiCPPModel </p>
* <p>Description: Model utilizzato per la produzione del Foglio xls "Definiti"	</p>
* <p>	nella statistica 'Riepilogo pprocedimenti Pendenti' 					</p>
* <p>	(Classe VII - Conversione Pene Pecuniarie)							</p>
*/

import f3b.model.GenericModel;

public class RiepilogoPendentiDefinitiCPPModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 2621622253871471473L;
	private String mTipoFoglio;
	private Integer mDefGE_Indulto;
	private Integer mDefGE_Amnistia;
	private Integer mDefGE_MorteReo;
	private Integer mDefGE_DecorsoTempo;
	private Integer mDefGE_Depen;
	private Integer mDefGE_Altro;

	private Integer mDefSanSos_Est_libCon;
	private Integer mDefSanSos_RevocaLC_inPenaDet;
	private Integer mDefSanSos_ConvLC_inPenaDet;
	private Integer mDefSanSos_Altro_LC;

	private Integer mDefSanSos_Est_LavSos;
	private Integer mDefSanSos_RevocaLS_inPenaDet;
	private Integer mDefSanSos_ConvLS_inPenaDet;
	private Integer mDefSanSos_Altro_LS;

	private Integer mDefNLP_Pagamento;
	private Integer mDefNLP_MorteReo;
	private Integer mDefNLP_Irreper;
	private Integer mDefNLP_Solvibil;
	private Integer mDefNLP_Prescrizio;
	private Integer mDefNLP_Cumulo;
	private Integer mDefNLP_Altro;

	private Integer mAltre_Definizioni;
	private Integer mAnno;
	private String mPeriodo;

	// COSTRUTTORE DI DEFAULT
	public RiepilogoPendentiDefinitiCPPModel() {
		this.mTipoFoglio = null;

		this.mDefGE_Indulto = null;
		this.mDefGE_Amnistia = null;
		this.mDefGE_MorteReo = null;
		this.mDefGE_DecorsoTempo = null;
		this.mDefGE_Depen = null;
		this.mDefGE_Altro = null;

		this.mDefSanSos_Est_libCon = null;
		this.mDefSanSos_RevocaLC_inPenaDet = null;
		this.mDefSanSos_ConvLC_inPenaDet = null;
		this.mDefSanSos_Altro_LC = null;

		this.mDefSanSos_Est_LavSos = null;
		this.mDefSanSos_RevocaLS_inPenaDet = null;
		this.mDefSanSos_ConvLS_inPenaDet = null;
		this.mDefSanSos_Altro_LS = null;

		this.mDefNLP_Pagamento = null;
		this.mDefNLP_MorteReo = null;
		this.mDefNLP_Irreper = null;
		this.mDefNLP_Solvibil = null;
		this.mDefNLP_Prescrizio = null;
		this.mDefNLP_Cumulo = null;
		this.mDefNLP_Altro = null;

		this.mAltre_Definizioni = null;
		this.mAnno = null;
		this.mPeriodo = null;
	}

	// COSTRUTTORE MODEL per SqlDAO
	public RiepilogoPendentiDefinitiCPPModel(String aTipoFoglio, Integer aDefGE_Indulto,
			Integer aDefGE_Amnistia, Integer aDefGE_MorteReo, Integer aDefGE_Decorsotempo,
			Integer aDefGE_Depen, Integer aDefGE_Altro,

			Integer aDefSanSos_Est_libCont, Integer aDefGE_RevocaLC_inPenaDet,
			Integer aDefSanSos_ConvLC_inPenaDet, Integer aDefSanSos_Altro_LC,

			Integer aDefSanSos_Est_LavSos, Integer aDefSanSos_RevocaLS_inPenaDet,
			Integer aDefSanSos_ConvLS_inPenaDet, Integer aDefSanSos_Altro_LS,

			Integer aDefNLP_Pagamento, Integer aDefNLP_MorteReo, Integer aDefNLP_Irreper,
			Integer aDefNLP_Solvibil, Integer aDefNLP_Prescrizio, Integer aDefNLP_Cumulo,
			Integer aDefNLP_Altro,

			Integer aAltre_Definizioni, Integer aAnno, String aPeriodo) {
		this.mTipoFoglio = aTipoFoglio;

		this.mDefGE_Indulto = aDefGE_Indulto;
		this.mDefGE_Amnistia = aDefGE_Amnistia;
		this.mDefGE_MorteReo = aDefGE_MorteReo;
		this.mDefGE_DecorsoTempo = aDefGE_Decorsotempo;
		this.mDefGE_Depen = aDefGE_Depen;
		this.mDefGE_Altro = aDefGE_Altro;

		this.mDefSanSos_Est_libCon = aDefSanSos_Est_libCont;
		this.mDefSanSos_RevocaLC_inPenaDet = aDefGE_RevocaLC_inPenaDet;
		this.mDefSanSos_ConvLC_inPenaDet = aDefSanSos_ConvLC_inPenaDet;
		this.mDefSanSos_Altro_LC = aDefSanSos_Altro_LC;

		this.mDefSanSos_Est_LavSos = aDefSanSos_Est_LavSos;
		this.mDefSanSos_RevocaLS_inPenaDet = aDefSanSos_RevocaLS_inPenaDet;
		this.mDefSanSos_ConvLS_inPenaDet = aDefSanSos_ConvLS_inPenaDet;
		this.mDefSanSos_Altro_LS = aDefSanSos_Altro_LS;

		this.mDefNLP_Pagamento = aDefNLP_Pagamento;
		this.mDefNLP_MorteReo = aDefNLP_MorteReo;
		this.mDefNLP_Irreper = aDefNLP_Irreper;
		this.mDefNLP_Solvibil = aDefNLP_Solvibil;
		this.mDefNLP_Prescrizio = aDefNLP_Prescrizio;
		this.mDefNLP_Cumulo = aDefNLP_Cumulo;
		this.mDefNLP_Altro = aDefNLP_Altro;

		this.mAltre_Definizioni = aAltre_Definizioni;
		this.mAnno = aAnno;
		this.mPeriodo = aPeriodo;
	}

	// TipoFoglio
	public String gettipoFoglio() {
		return mTipoFoglio;
	}

	public void settipoFoglio(String aValore) {
		mTipoFoglio = aValore;
	}

	// Anno
	public Integer getAnno() {
		return mAnno;
	}

	public void setAnno(Integer anno) {
		mAnno = anno;
	}

	// Periodo (Per Statistiche Semestrali o Trimestrali)
	public String getPeriodo() {
		return mPeriodo;
	}

	public void setPeriodo(String aValore) {
		mPeriodo = aValore;
	}

	// Definizione per GE:
	// Indulto
	public Integer getDefGEIndulto() {
		return mDefGE_Indulto;
	}

	public void setDefGEIndulto(Integer aValore) {
		mDefGE_Indulto = aValore;
	}

	// Amnistia
	public Integer getDefGEAmnistia() {
		return mDefGE_Amnistia;
	}

	public void setDefGEAmnistia(Integer aValore) {
		mDefGE_Amnistia = aValore;
	}

	// Morte del Reo
	public Integer getDefGEMorteReo() {
		return mDefGE_MorteReo;
	}

	public void setDefGEMorteReo(Integer aValore) {
		mDefGE_MorteReo = aValore;
	}

	// Estinzione pena per Decorso Tempo
	public Integer getDefGE_EstinzioneperDecorsoTempo() {
		return mDefGE_DecorsoTempo;
	}

	public void setDefGE_EstinzioneperDecorsoTempo(Integer aValore) {
		mDefGE_DecorsoTempo = aValore;
	}

	// Depenalizzazione
	public Integer getDefGEDepenalizzazione() {
		return mDefGE_Depen;
	}

	public void setDefGEDepenalizzazione(Integer aValore) {
		mDefGE_Depen = aValore;
	}

	// Altro (definizione per GE)
	public Integer getDefGEAltro() {
		return mDefGE_Altro;
	}

	public void setDefGEAltro(Integer aValore) {
		mDefGE_Altro = aValore;
	}

	// Definizione: Pena pec. Convertita in Sanzione Sostitutiva (LIBERTA' CONTROLLATA):
	// Estinzione Lib.Contr.
	public Integer getDefSanSos_Est_libCon() {
		return mDefSanSos_Est_libCon;
	}

	public void setDefSanSos_Est_libCon(Integer aValore) {
		mDefSanSos_Est_libCon = aValore;
	}

	// Revoca Lib.Contr. e conversione in Pena Det.
	public Integer getDefSanSos_RevocaLC_inPenaDet() {
		return mDefSanSos_RevocaLC_inPenaDet;
	}

	public void setDefSanSos_RevocaLC_inPenaDet(Integer aValore) {
		mDefSanSos_RevocaLC_inPenaDet = aValore;
	}

	// Conversione Lib.Contr. in Pena det.
	public Integer getDefSanSos_ConvLC_inPenaDet() {
		return mDefSanSos_ConvLC_inPenaDet;
	}

	public void setDefSanSos_ConvLC_inPenaDet(Integer aValore) {
		mDefSanSos_ConvLC_inPenaDet = aValore;
	}

	// Altro
	public Integer getDefSanSos_AltroLC() {
		return mDefSanSos_Altro_LC;
	}

	public void setDefSanSos_AltroLC(Integer aValore) {
		mDefSanSos_Altro_LC = aValore;
	}

	// Definizione: Pena pec. Convertita in Sanzione Sostitutiva (LAVORO SOSTITUTIVO):
	// Estinzione Lav. Sos.
	public Integer getDefSanSos_Est_LavSos() {
		return mDefSanSos_Est_LavSos;
	}

	public void setDefSanSos_Est_LavSos(Integer aValore) {
		mDefSanSos_Est_LavSos = aValore;
	}

	// Revoca Lav. Sost. e conversione in Pena Det.
	public Integer getDefSanSos_RevocaLS_inPenaDet() {
		return mDefSanSos_RevocaLS_inPenaDet;
	}

	public void setDefSanSos_RevocaLS_inPenaDet(Integer aValore) {
		mDefSanSos_RevocaLS_inPenaDet = aValore;
	}

	// Conversione Lav. Sost. in Pena det.
	public Integer getDefSanSos_ConvLS_inPenaDet() {
		return mDefSanSos_ConvLS_inPenaDet;
	}

	public void setDefSanSos_ConvLS_inPenaDet(Integer aValore) {
		mDefSanSos_ConvLS_inPenaDet = aValore;
	}

	// Altro
	public Integer getDefSanSos_AltroLS() {
		return mDefSanSos_Altro_LS;
	}

	public void setDefSanSos_AltroLS(Integer aValore) {
		mDefSanSos_Altro_LS = aValore;
	}

	// Definizione per NON LUOGO A PROVVEDERE (emesso da Uds) per:
	// Avvenuto Pagamento
	public Integer getDefNLP_Pagamento() {
		return mDefNLP_Pagamento;
	}

	public void setDefNLP_Pagamento(Integer aValore) {
		mDefNLP_Pagamento = aValore;
	}

	// Morte del reo
	public Integer getDefNLP_MorteReo() {
		return mDefNLP_MorteReo;
	}

	public void setDefNLP_MorteReo(Integer aValore) {
		mDefNLP_MorteReo = aValore;
	}

	// Irreperibilità
	public Integer getDefNLP_Irreperibilita() {
		return mDefNLP_Irreper;
	}

	public void setDefNLP_Irreperibilita(Integer aValore) {
		mDefNLP_Irreper = aValore;
	}

	// Accertata Solvibilità
	public Integer getDefNLP_Solvibilita() {
		return mDefNLP_Solvibil;
	}

	public void setDefNLP_Solvibilita(Integer aValore) {
		mDefNLP_Solvibil = aValore;
	}

	// Intervenuta Prescrizione
	public Integer getDefNLP_Prescrizione() {
		return mDefNLP_Prescrizio;
	}

	public void setDefNLP_Prescrizione(Integer aValore) {
		mDefNLP_Prescrizio = aValore;
	}

	// Assorbimento in Cumulo
	public Integer getDefNLP_AssorbimentoCumulo() {
		return mDefNLP_Cumulo;
	}

	public void setDefNLP_AssorbimentoCumulo(Integer aValore) {
		mDefNLP_Cumulo = aValore;
	}

	// Altro (NLP)
	public Integer getDefNLP_Altro() {
		return mDefNLP_Altro;
	}

	public void setDefNLP_Altro(Integer aValore) {
		mDefNLP_Altro = aValore;
	}

	// Altre DEFINIZIONI
	public Integer getAltreDefinizioni() {
		return mAltre_Definizioni;
	}

	public void setAltreDefinizioni(Integer aValore) {
		mAltre_Definizioni = aValore;
	}
}
