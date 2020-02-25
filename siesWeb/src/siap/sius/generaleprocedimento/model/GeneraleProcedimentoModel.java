package siap.sius.generaleprocedimento.model;

/**
* <p>Title: GeneraleProcedimentoModel</p>
* <p>Description: Classe Model che rappresenta il GeneraleProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sius.udienza.model.UdienzaModel;
import f3b.model.GenericModel;

public class GeneraleProcedimentoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5079531042764592283L;

	private BigDecimal mIdGeneraleProcedimento;
	private BigDecimal mAnnoS1;
	private BigDecimal mProgrS1;
	private String mCodTipoRegistro;
	private String mDescrTipoRegistro;
	private String mCodOggettoProcedimento;
	private String mDescrOggettoProcedimento;
	private Date mDataRichiesta;
	private Date mDataArrivoCancelleria;
	private Date mDataCameraConsiglio;
	private String mDescrRichiestaDelegazione;
	private String mCodAutoritaDelegata;
	private String mDescrAutoritaDelegata;
	private Date mDataRestituzDelegazione;
	private Date mDataRicorsoImpugn;
	private Date mDataInvioAttiImpugn;
	private Date mDataInvioEsecuzProvvisoria;
	private Date mDataInvioEsecuzOrdinaria;
	private Date mDataCompilazComplementare;
	private String mCodTipoFoglioComplementare;
	private String mDescrTipoFoglioComplementare;
	private Date mDataAnnotazione;
	private String mAnnotazione;
	private String mTipoDefinizione;
	private Date mDataDefinizione;
	private String mDescrDefinizione;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mCodTipoAtto;
	private String mDescrTipoAtto;
	private String mCodSedeMittente;
	private String mDescrSedeMittente;
	private String mCodTipoMittenteAtto;
	private String mDescrTipoMittenteAtto;
	private BigDecimal mFasSiuIdFascicoloSius;
	private String mSezione;
	private Date mDataFinePena;
	private String mCodPosGiuridica;
	private String mDescrPosGiuridica;
	private BigDecimal mUdiIdUdienza;

	// 29/12/2003 Aggiunti per gestione luogo detenzione.
	private String mIdLuogoDetenzione;
	private String mIdAltraCausa;

	private String mDescrMittente;
	private String mCodUfficioMittente; // 01/10/2004

	// 20140113 - Aggiunto per la gestione dei dati di udienza per funzioni statistiche
	private UdienzaModel mUdienza;

	// COSTRUTTORE DI DEFAULT
	public GeneraleProcedimentoModel() {
		this.mIdGeneraleProcedimento = null;
		this.mAnnoS1 = null;
		this.mProgrS1 = null;
		this.mCodTipoRegistro = "-";
		this.mDescrTipoRegistro = "";
		this.mCodOggettoProcedimento = "-";
		this.mDescrOggettoProcedimento = "";
		this.mDataRichiesta = null;
		this.mDataArrivoCancelleria = null;
		this.mDataCameraConsiglio = null;
		this.mDescrRichiestaDelegazione = "";
		this.mCodAutoritaDelegata = "-";
		this.mDescrAutoritaDelegata = "";
		this.mDataRestituzDelegazione = null;
		this.mDataRicorsoImpugn = null;
		this.mDataInvioAttiImpugn = null;
		this.mDataInvioEsecuzProvvisoria = null;
		this.mDataInvioEsecuzOrdinaria = null;
		this.mDataCompilazComplementare = null;
		this.mCodTipoFoglioComplementare = "-";
		this.mDescrTipoFoglioComplementare = "";
		this.mDataAnnotazione = null;
		this.mAnnotazione = "";
		this.mTipoDefinizione = "";
		this.mDataDefinizione = null;
		this.mDescrDefinizione = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "-";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "-";
		this.mDescrUfficioAggiornamento = "";
		this.mCodTipoAtto = "-";
		this.mDescrTipoAtto = "";
		this.mCodSedeMittente = "-";
		this.mDescrSedeMittente = "";
		this.mCodTipoMittenteAtto = "-";
		this.mDescrTipoMittenteAtto = "";
		this.mFasSiuIdFascicoloSius = null;
		this.mSezione = "";
		this.mDataFinePena = null;
		this.mCodPosGiuridica = "";
		this.mDescrPosGiuridica = "";
		this.mUdiIdUdienza = null;
		this.mIdLuogoDetenzione = "";
		this.mIdAltraCausa = "";
		this.mDescrMittente = "";
		this.mCodUfficioMittente = "";

		this.mUdienza = null;
	}

	// COSTRUTTORE DI COPIA
	public GeneraleProcedimentoModel(GeneraleProcedimentoModel aModel) {
		this.mIdGeneraleProcedimento = aModel.mIdGeneraleProcedimento;
		this.mAnnoS1 = aModel.mAnnoS1;
		this.mProgrS1 = aModel.mProgrS1;
		this.mCodTipoRegistro = aModel.mCodTipoRegistro;
		this.mDescrTipoRegistro = aModel.mDescrTipoRegistro;
		this.mCodOggettoProcedimento = aModel.mCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aModel.mDescrOggettoProcedimento;
		this.mDataRichiesta = aModel.mDataRichiesta;
		this.mDataArrivoCancelleria = aModel.mDataArrivoCancelleria;
		this.mDataCameraConsiglio = aModel.mDataCameraConsiglio;
		this.mDescrRichiestaDelegazione = aModel.mDescrRichiestaDelegazione;
		this.mCodAutoritaDelegata = aModel.mCodAutoritaDelegata;
		this.mDescrAutoritaDelegata = aModel.mDescrAutoritaDelegata;
		this.mDataRestituzDelegazione = aModel.mDataRestituzDelegazione;
		this.mDataRicorsoImpugn = aModel.mDataRicorsoImpugn;
		this.mDataInvioAttiImpugn = aModel.mDataInvioAttiImpugn;
		this.mDataInvioEsecuzProvvisoria = aModel.mDataInvioEsecuzProvvisoria;
		this.mDataInvioEsecuzOrdinaria = aModel.mDataInvioEsecuzOrdinaria;
		this.mDataCompilazComplementare = aModel.mDataCompilazComplementare;
		this.mCodTipoFoglioComplementare = aModel.mCodTipoFoglioComplementare;
		this.mDescrTipoFoglioComplementare = aModel.mDescrTipoFoglioComplementare;
		this.mDataAnnotazione = aModel.mDataAnnotazione;
		this.mAnnotazione = aModel.mAnnotazione;
		this.mTipoDefinizione = aModel.mTipoDefinizione;
		this.mDataDefinizione = aModel.mDataDefinizione;
		this.mDescrDefinizione = aModel.mDescrDefinizione;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mCodTipoAtto = aModel.mCodTipoAtto;
		this.mDescrTipoAtto = aModel.mDescrTipoAtto;
		this.mCodSedeMittente = aModel.mCodSedeMittente;
		this.mDescrSedeMittente = aModel.mDescrSedeMittente;
		this.mCodTipoMittenteAtto = aModel.mCodTipoMittenteAtto;
		this.mDescrTipoMittenteAtto = aModel.mDescrTipoMittenteAtto;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mSezione = aModel.mSezione;
		this.mDataFinePena = aModel.mDataFinePena;
		this.mCodPosGiuridica = aModel.mCodPosGiuridica;
		this.mDescrPosGiuridica = aModel.mDescrPosGiuridica;
		this.mUdiIdUdienza = aModel.mUdiIdUdienza;
		this.mIdLuogoDetenzione = aModel.mIdLuogoDetenzione;
		this.mIdAltraCausa = aModel.mIdAltraCausa;
		this.mDescrMittente = aModel.mDescrMittente;
		this.mCodUfficioMittente = aModel.mCodUfficioMittente;

		this.mUdienza = aModel.mUdienza;
	}

	// COSTRUTTORE MODEL
	public GeneraleProcedimentoModel(BigDecimal aIdGeneraleProcedimento, BigDecimal aAnnoS1,
			BigDecimal aProgrS1, String aCodTipoRegistro, String aDescrTipoRegistro,
			String aCodOggettoProcedimento, String aDescrOggettoProcedimento, Date aDataRichiesta,
			Date aDataArrivoCancelleria, Date aDataCameraConsiglio, String aDescrRichiestaDelegazione,
			String aCodAutoritaDelegata, String aDescrAutoritaDelegata, Date aDataRestituzDelegazione,
			Date aDataRicorsoImpugn, Date aDataInvioAttiImpugn, Date aDataInvioEsecuzProvvisoria,
			Date aDataInvioEsecuzOrdinaria, Date aDataCompilazComplementare,
			String aCodTipoFoglioComplementare, String aDescrTipoFoglioComplementare, Date aDataAnnotazione,
			String aAnnotazione, String aTipoDefinizione, Date aDataDefinizione, String aDescrDefinizione,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, String aCodTipoAtto,
			String aDescrTipoAtto, String aCodSedeMittente, String aDescrSedeMittente,
			String aCodTipoMittenteAtto, String aDescrTipoMittenteAtto, BigDecimal aFasSiuIdFascicoloSius,
			String aSezione, Date aDataFinePena, String aCodPosGiuridica, String aDescrPosGiuridica,
			BigDecimal aUdiIdUdienza, String aIdLuogoDetenzione, String aIdAltraCausa, String aDescrMittente,
			String aCodUfficioMittente) {
		this.mIdGeneraleProcedimento = aIdGeneraleProcedimento;
		this.mAnnoS1 = aAnnoS1;
		this.mProgrS1 = aProgrS1;
		this.mCodTipoRegistro = aCodTipoRegistro;
		this.mDescrTipoRegistro = aDescrTipoRegistro;
		this.mCodOggettoProcedimento = aCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aDescrOggettoProcedimento;
		this.mDataRichiesta = aDataRichiesta;
		this.mDataArrivoCancelleria = aDataArrivoCancelleria;
		this.mDataCameraConsiglio = aDataCameraConsiglio;
		this.mDescrRichiestaDelegazione = aDescrRichiestaDelegazione;
		this.mCodAutoritaDelegata = aCodAutoritaDelegata;
		this.mDescrAutoritaDelegata = aDescrAutoritaDelegata;
		this.mDataRestituzDelegazione = aDataRestituzDelegazione;
		this.mDataRicorsoImpugn = aDataRicorsoImpugn;
		this.mDataInvioAttiImpugn = aDataInvioAttiImpugn;
		this.mDataInvioEsecuzProvvisoria = aDataInvioEsecuzProvvisoria;
		this.mDataInvioEsecuzOrdinaria = aDataInvioEsecuzOrdinaria;
		this.mDataCompilazComplementare = aDataCompilazComplementare;
		this.mCodTipoFoglioComplementare = aCodTipoFoglioComplementare;
		this.mDescrTipoFoglioComplementare = aDescrTipoFoglioComplementare;
		this.mDataAnnotazione = aDataAnnotazione;
		this.mAnnotazione = aAnnotazione;
		this.mTipoDefinizione = aTipoDefinizione;
		this.mDataDefinizione = aDataDefinizione;
		this.mDescrDefinizione = aDescrDefinizione;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mCodTipoAtto = aCodTipoAtto;
		this.mDescrTipoAtto = aDescrTipoAtto;
		this.mCodSedeMittente = aCodSedeMittente;
		this.mDescrSedeMittente = aDescrSedeMittente;
		this.mDescrTipoMittenteAtto = aDescrTipoMittenteAtto;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mSezione = aSezione;
		this.mDataFinePena = aDataFinePena;
		this.mCodPosGiuridica = aCodPosGiuridica;
		this.mDescrPosGiuridica = aDescrPosGiuridica;
		this.mUdiIdUdienza = aUdiIdUdienza;
		this.mIdLuogoDetenzione = aIdLuogoDetenzione;
		this.mIdAltraCausa = aIdAltraCausa;
		this.mDescrMittente = aDescrMittente;
		this.mCodUfficioMittente = aCodUfficioMittente;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdGeneraleProcedimento() {
		return mIdGeneraleProcedimento;
	}

	public BigDecimal getAnnoS1() {
		return mAnnoS1;
	}

	public BigDecimal getProgrS1() {
		return mProgrS1;
	}

	public String getCodTipoRegistro() {
		return mCodTipoRegistro;
	}

	public String getDescrTipoRegistro() {
		return mDescrTipoRegistro;
	}

	public String getCodOggettoProcedimento() {
		return mCodOggettoProcedimento;
	}

	public String getDescrOggettoProcedimento() {
		return mDescrOggettoProcedimento;
	}

	public Date getDataRichiesta() {
		return mDataRichiesta;
	}

	public Date getDataArrivoCancelleria() {
		return mDataArrivoCancelleria;
	}

	public Date getDataCameraConsiglio() {
		return mDataCameraConsiglio;
	}

	public String getDescrRichiestaDelegazione() {
		return mDescrRichiestaDelegazione;
	}

	public String getCodAutoritaDelegata() {
		return mCodAutoritaDelegata;
	}

	public String getDescrAutoritaDelegata() {
		return mDescrAutoritaDelegata;
	}

	public Date getDataRestituzDelegazione() {
		return mDataRestituzDelegazione;
	}

	public Date getDataRicorsoImpugn() {
		return mDataRicorsoImpugn;
	}

	public Date getDataInvioAttiImpugn() {
		return mDataInvioAttiImpugn;
	}

	public Date getDataInvioEsecuzProvvisoria() {
		return mDataInvioEsecuzProvvisoria;
	}

	public Date getDataInvioEsecuzOrdinaria() {
		return mDataInvioEsecuzOrdinaria;
	}

	public Date getDataCompilazComplementare() {
		return mDataCompilazComplementare;
	}

	public String getCodTipoFoglioComplementare() {
		return mCodTipoFoglioComplementare;
	}

	public String getDescrTipoFoglioComplementare() {
		return mDescrTipoFoglioComplementare;
	}

	public Date getDataAnnotazione() {
		return mDataAnnotazione;
	}

	public String getAnnotazione() {
		return mAnnotazione;
	}

	public String getTipoDefinizione() {
		return mTipoDefinizione;
	}

	public Date getDataDefinizione() {
		return mDataDefinizione;
	}

	public String getDescrDefinizione() {
		return mDescrDefinizione;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public String getCodTipoAtto() {
		return mCodTipoAtto;
	}

	public String getDescrTipoAtto() {
		return mDescrTipoAtto;
	}

	public String getCodSedeMittente() {
		return mCodSedeMittente;
	}

	public String getDescrSedeMittente() {
		return mDescrSedeMittente;
	}

	public String getCodTipoMittenteAtto() {
		return mCodTipoMittenteAtto;
	}

	public String getDescrTipoMittenteAtto() {
		return mDescrTipoMittenteAtto;
	}

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public String getSezione() {
		return mSezione;
	}

	public Date getDataFinePena() {
		return mDataFinePena;
	}

	public String getCodPosGiuridica() {
		return mCodPosGiuridica;
	}

	public String getDescrPosGiuridica() {
		return mDescrPosGiuridica;
	}

	public BigDecimal getUdiIdUdienza() {
		return mUdiIdUdienza;
	}

	public String getIdLuogoDetenzione() {
		return mIdLuogoDetenzione;
	}

	public String getIdAltraCausa() {
		return mIdAltraCausa;
	}

	public String getDescrMittente() {
		return mDescrMittente;
	}

	public String getCodUfficioMittente() {
		return mCodUfficioMittente;
	}

	public UdienzaModel getUdienza() {
		return mUdienza;
	}
	//
	// METODI SET()
	//

	public void setIdGeneraleProcedimento(BigDecimal aValore) {
		mIdGeneraleProcedimento = aValore;
	}

	public void setAnnoS1(BigDecimal aValore) {
		mAnnoS1 = aValore;
	}

	public void setProgrS1(BigDecimal aValore) {
		mProgrS1 = aValore;
	}

	public void setCodTipoRegistro(String aValore) {
		mCodTipoRegistro = aValore;
	}

	public void setDescrTipoRegistro(String aValore) {
		mDescrTipoRegistro = aValore;
	}

	public void setCodOggettoProcedimento(String aValore) {
		mCodOggettoProcedimento = aValore;
	}

	public void setDescrOggettoProcedimento(String aValore) {
		mDescrOggettoProcedimento = aValore;
	}

	public void setDataRichiesta(Date aValore) {
		mDataRichiesta = aValore;
	}

	public void setDataArrivoCancelleria(Date aValore) {
		mDataArrivoCancelleria = aValore;
	}

	public void setDataCameraConsiglio(Date aValore) {
		mDataCameraConsiglio = aValore;
	}

	public void setDescrRichiestaDelegazione(String aValore) {
		mDescrRichiestaDelegazione = aValore;
	}

	public void setCodAutoritaDelegata(String aValore) {
		mCodAutoritaDelegata = aValore;
	}

	public void setDescrAutoritaDelegata(String aValore) {
		mDescrAutoritaDelegata = aValore;
	}

	public void setDataRestituzDelegazione(Date aValore) {
		mDataRestituzDelegazione = aValore;
	}

	public void setDataRicorsoImpugn(Date aValore) {
		mDataRicorsoImpugn = aValore;
	}

	public void setDataInvioAttiImpugn(Date aValore) {
		mDataInvioAttiImpugn = aValore;
	}

	public void setDataInvioEsecuzProvvisoria(Date aValore) {
		mDataInvioEsecuzProvvisoria = aValore;
	}

	public void setDataInvioEsecuzOrdinaria(Date aValore) {
		mDataInvioEsecuzOrdinaria = aValore;
	}

	public void setDataCompilazComplementare(Date aValore) {
		mDataCompilazComplementare = aValore;
	}

	public void setCodTipoFoglioComplementare(String aValore) {
		mCodTipoFoglioComplementare = aValore;
	}

	public void setDescrTipoFoglioComplementare(String aValore) {
		mDescrTipoFoglioComplementare = aValore;
	}

	public void setDataAnnotazione(Date aValore) {
		mDataAnnotazione = aValore;
	}

	public void setAnnotazione(String aValore) {
		mAnnotazione = aValore;
	}

	public void setTipoDefinizione(String aValore) {
		mTipoDefinizione = aValore;
	}

	public void setDataDefinizione(Date aValore) {
		mDataDefinizione = aValore;
	}

	public void setDescrDefinizione(String aValore) {
		mDescrDefinizione = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setCodTipoAtto(String aValore) {
		mCodTipoAtto = aValore;
	}

	public void setDescrTipoAtto(String aValore) {
		mDescrTipoAtto = aValore;
	}

	public void setCodSedeMittente(String aValore) {
		mCodSedeMittente = aValore;
	}

	public void setDescrSedeMittente(String aValore) {
		mDescrSedeMittente = aValore;
	}

	public void setCodTipoMittenteAtto(String aValore) {
		mCodTipoMittenteAtto = aValore;
	}

	public void setDescrTipoMittenteAtto(String aValore) {
		mDescrTipoMittenteAtto = aValore;
	}

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setSezione(String aValore) {
		mSezione = aValore;
	}

	public void setDataFinePena(Date aValore) {
		mDataFinePena = aValore;
	}

	public void setCodPosGiuridica(String aValore) {
		mCodPosGiuridica = aValore;
	}

	public void setDescrPosGiuridica(String aValore) {
		mDescrPosGiuridica = aValore;
	}

	public void setUdiIdUdienza(BigDecimal aValore) {
		mUdiIdUdienza = aValore;
	}

	public void setIdLuogoDetenzione(String aValore) {
		mIdLuogoDetenzione = aValore;
	}

	public void setIdAltraCausa(String aValore) {
		mIdAltraCausa = aValore;
	}

	public void setDescrMittente(String aValore) {
		mDescrMittente = aValore;
	}

	public void setCodUfficioMittente(String aValore) {
		mCodUfficioMittente = aValore;
	}

	public void setUdienza(UdienzaModel aValore) {
		mUdienza = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdGeneraleProcedimento + " - " + mAnnoS1 + " - " + mProgrS1 + " - " + mCodTipoRegistro
				+ " - " + mDescrTipoRegistro + " - " + mCodOggettoProcedimento + " - "
				+ mDescrOggettoProcedimento + " - " + mDataRichiesta + " - " + mDataArrivoCancelleria + " - "
				+ mDataCameraConsiglio + " - " + mDescrRichiestaDelegazione + " - " + mCodAutoritaDelegata
				+ " - " + mDescrAutoritaDelegata + " - " + mDataRestituzDelegazione + " - "
				+ mDataRicorsoImpugn + " - " + mDataInvioAttiImpugn + " - " + mDataInvioEsecuzProvvisoria
				+ " - " + mDataInvioEsecuzOrdinaria + " - " + mDataCompilazComplementare + " - "
				+ mCodTipoFoglioComplementare + " - " + mDescrTipoFoglioComplementare + " - "
				+ mDataAnnotazione + " - " + mAnnotazione + " - " + mTipoDefinizione + " - "
				+ mDataDefinizione + " - " + mDescrDefinizione + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mCodTipoAtto + " - " + mDescrTipoAtto + " - " + mCodSedeMittente + " - "
				+ mDescrSedeMittente + " - " + mCodTipoMittenteAtto + " - " + mDescrTipoMittenteAtto + " - "
				+ mFasSiuIdFascicoloSius + " - " + mSezione + " - " + mDataFinePena + " - " + mCodPosGiuridica
				+ " - " + mDescrPosGiuridica + " - " + mUdiIdUdienza + " - " + mIdLuogoDetenzione + " - "
				+ mIdAltraCausa + " - " + mDescrMittente + " - " + mCodUfficioMittente;

		return lStr;
	}

}