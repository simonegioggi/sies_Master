package siap.siep.istruttoriacumulo.model;

/**
* <p>Title: IstruttoriaCumuloModel</p>
* <p>Description: Classe Model che rappresenta il IstruttoriaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import f3b.model.GenericModel;
import siap.sico.evento.model.EventoModel;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliUlterioriSanzioniModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;

public class IstruttoriaCumuloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -134951613289217134L;
	private BigDecimal mIdIstruttoriaCumulo;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mEveIdEventoIstr;
	private BigDecimal mEveIdEventoProv;
	private BigDecimal mAnnoProtocollo;
	private BigDecimal mNumProtocollo;
	private String mChiaveUfficio;
	private Date mDataApertura;
	private Date mDataChiusura;
	private String mNote;
	private String mFlagStato;
	private String mOrdinamentoTitoli;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	// Ricerca estesa IstruttoriaCumulo
	private BigDecimal mAnnoProtocolloIniziale;
	private BigDecimal mNumProtocolloIniziale;
	private BigDecimal mAnnoProtocolloFinale;
	private BigDecimal mNumProtocolloFinale;
	private Date mDataIscrizioneIniziale;
	private Date mDataIscrizioneFinale;

	private EventoModel mProvvedimentoCumulo;

	private DatiFinaliCumuloModel mDatiFinaliCumulo;
	private PosizioneGiuridicaCumuloModel mPosizioneGiuridicaCumulo;
	private List<PenaRideterminataCumuloModel> mListPenaRideterminataCumulo;
	private List<DatiFinaliUlterioriSanzioniModel> mListDatiFinaliUlterioriSanzioni;
	private List<ComputiCumuloModel> mListComputiCumulo;

	private List<TitoloCumulatoModel> mListTitoliCumulati;

	private List<RichiesteInviateCumModel> mListaRichiesteInviate;
	private List<RichiestePmInCumuloModel> mListaRichiestePm;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public IstruttoriaCumuloModel() {
		this.mIdIstruttoriaCumulo = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mEveIdEventoIstr = null;
		this.mEveIdEventoProv = null;
		this.mAnnoProtocollo = null;
		this.mNumProtocollo = null;
		this.mChiaveUfficio = "";
		this.mDataApertura = null;
		this.mDataChiusura = null;
		this.mNote = "";
		this.mFlagStato = "";
		this.mOrdinamentoTitoli = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mAnnoProtocolloIniziale = null;
		this.mNumProtocolloIniziale = null;
		this.mAnnoProtocolloFinale = null;
		this.mNumProtocolloFinale = null;
		this.mDataIscrizioneIniziale = null;
		this.mDataIscrizioneFinale = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public IstruttoriaCumuloModel(IstruttoriaCumuloModel aModel) {
		this.mIdIstruttoriaCumulo = aModel.mIdIstruttoriaCumulo;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mEveIdEventoIstr = aModel.mEveIdEventoIstr;
		this.mEveIdEventoProv = aModel.mEveIdEventoProv;
		this.mAnnoProtocollo = aModel.mAnnoProtocollo;
		this.mNumProtocollo = aModel.mNumProtocollo;
		this.mChiaveUfficio = aModel.mChiaveUfficio;
		this.mDataApertura = aModel.mDataApertura;
		this.mDataChiusura = aModel.mDataChiusura;
		this.mNote = aModel.mNote;
		this.mFlagStato = aModel.mFlagStato;
		this.mOrdinamentoTitoli = aModel.mOrdinamentoTitoli;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mAnnoProtocolloIniziale = aModel.mAnnoProtocolloIniziale;
		this.mNumProtocolloIniziale = aModel.mNumProtocolloIniziale;
		this.mAnnoProtocolloFinale = aModel.mAnnoProtocolloFinale;
		this.mNumProtocolloFinale = aModel.mNumProtocolloFinale;
		this.mDataIscrizioneIniziale = aModel.mDataIscrizioneIniziale;
		this.mDataIscrizioneFinale = aModel.mDataIscrizioneFinale;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public IstruttoriaCumuloModel(BigDecimal aIdIstruttoriaCumulo, BigDecimal aFasSieIdFascicoloSiep,
			BigDecimal aEveIdEventoIstr, BigDecimal aEveIdEventoProv, Date aDataApertura, Date aDataChiusura,
			BigDecimal aAnnoProtocollo, BigDecimal aNumProtocollo, String aChiaveUfficio, String aNote,
			String aFlagStato, String aOrdinamentoTitoli, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdIstruttoriaCumulo = aIdIstruttoriaCumulo;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mEveIdEventoIstr = aEveIdEventoIstr;
		this.mEveIdEventoProv = aEveIdEventoProv;
		this.mDataApertura = aDataApertura;
		this.mDataChiusura = aDataChiusura;
		this.mAnnoProtocollo = aAnnoProtocollo;
		this.mNumProtocollo = aNumProtocollo;
		this.mChiaveUfficio = aChiaveUfficio;
		this.mNote = aNote;
		this.mFlagStato = aFlagStato;
		this.mOrdinamentoTitoli = aOrdinamentoTitoli;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mAnnoProtocolloIniziale = null;
		this.mNumProtocolloIniziale = null;
		this.mAnnoProtocolloFinale = null;
		this.mNumProtocolloFinale = null;
		this.mDataIscrizioneIniziale = null;
		this.mDataIscrizioneFinale = null;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdIstruttoriaCumulo() {
		return mIdIstruttoriaCumulo;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getEveIdEventoIstr() {
		return mEveIdEventoIstr;
	}

	public BigDecimal getEveIdEventoProv() {
		return mEveIdEventoProv;
	}

	public Date getDataApertura() {
		return mDataApertura;
	}

	public Date getDataChiusura() {
		return mDataChiusura;
	}

	public BigDecimal getAnnoProtocollo() {
		return mAnnoProtocollo;
	}

	public BigDecimal getNumProtocollo() {
		return mNumProtocollo;
	}

	public String getChiaveUfficio() {
		return mChiaveUfficio;
	}

	public String getNote() {
		return mNote;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getOrdinamentoTitoli() {
		return mOrdinamentoTitoli;
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

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public BigDecimal getAnnoProtocolloIniziale() {
		return mAnnoProtocolloIniziale;
	}

	public BigDecimal getNumProtocolloIniziale() {
		return mNumProtocolloIniziale;
	}

	public BigDecimal getAnnoProtocolloFinale() {
		return mAnnoProtocolloFinale;
	}

	public BigDecimal getNumProtocolloFinale() {
		return mNumProtocolloFinale;
	}

	public Date getDataIscrizioneIniziale() {
		return mDataIscrizioneIniziale;
	}

	public Date getDataIscrizioneFinale() {
		return mDataIscrizioneFinale;
	}

	public EventoModel getProvvedimentoCumulo() {
		return mProvvedimentoCumulo;
	}

	public DatiFinaliCumuloModel getDatiFinaliCumulo() {
		return mDatiFinaliCumulo;
	}

	public PosizioneGiuridicaCumuloModel getPosizioneGiuridicaCumulo() {
		return mPosizioneGiuridicaCumulo;
	}

	public List<PenaRideterminataCumuloModel> getPenaRideterminataCumulo() {
		return mListPenaRideterminataCumulo;
	}

	public List<DatiFinaliUlterioriSanzioniModel> getDatiFinaliUlterioriSanzioni() {
		return mListDatiFinaliUlterioriSanzioni;
	}

	public List<ComputiCumuloModel> getComputiCumulo() {
		return mListComputiCumulo;
	}

	public List<TitoloCumulatoModel> getTitoliCumulati() {
		return mListTitoliCumulati;
	}

	public List<RichiesteInviateCumModel> getRichiesteInviate() {
		return mListaRichiesteInviate;
	}

	public List<RichiestePmInCumuloModel> getListaRichiestePmInCumulo() {
		return mListaRichiestePm;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdIstruttoriaCumulo(BigDecimal aValore) {
		mIdIstruttoriaCumulo = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setEveIdEventoIstr(BigDecimal aValore) {
		mEveIdEventoIstr = aValore;
	}

	public void setEveIdEventoProv(BigDecimal aValore) {
		mEveIdEventoProv = aValore;
	}

	public void setDataApertura(Date aValore) {
		mDataApertura = aValore;
	}

	public void setDataChiusura(Date aValore) {
		mDataChiusura = aValore;
	}

	public void setAnnoProtocollo(BigDecimal aValore) {
		mAnnoProtocollo = aValore;
	}

	public void setNumProtocollo(BigDecimal aValore) {
		mNumProtocollo = aValore;
	}

	public void setChiaveUfficio(String aValore) {
		mChiaveUfficio = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setOrdinamentoTitoli(String aValore) {
		mOrdinamentoTitoli = aValore;
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

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setAnnoProtocolloIniziale(BigDecimal aValore) {
		mAnnoProtocolloIniziale = aValore;
	}

	public void setNumProtocolloIniziale(BigDecimal aValore) {
		mNumProtocolloIniziale = aValore;
	}

	public void setAnnoProtocolloFinale(BigDecimal aValore) {
		mAnnoProtocolloFinale = aValore;
	}

	public void setNumProtocolloFinale(BigDecimal aValore) {
		mNumProtocolloFinale = aValore;
	}

	public void setDataIscrizioneIniziale(Date aValore) {
		mDataIscrizioneIniziale = aValore;
	}

	public void setDataIscrizioneFinale(Date aValore) {
		mDataIscrizioneFinale = aValore;
	}

	public void setProvvedimentoCumulo(EventoModel aValore) {
		mProvvedimentoCumulo = aValore;
	}

	public void setDatiFinaliCumulo(DatiFinaliCumuloModel aValore) {
		mDatiFinaliCumulo = aValore;
	}

	public void setPosizioneGiuridicaCumulo(PosizioneGiuridicaCumuloModel aValore) {
		mPosizioneGiuridicaCumulo = aValore;
	}

	public void setPenaRideterminataCumulo(List<PenaRideterminataCumuloModel> aValore) {
		mListPenaRideterminataCumulo = aValore;
	}

	public void setDatiFinaliUlterioriSanzioni(List<DatiFinaliUlterioriSanzioniModel> aValore) {
		mListDatiFinaliUlterioriSanzioni = aValore;
	}

	public void setComputiCumulo(List<ComputiCumuloModel> aValore) {
		mListComputiCumulo = aValore;
	}

	public void setTitoliCumulati(List<TitoloCumulatoModel> aValore) {
		mListTitoliCumulati = aValore;
	}

	public void setRichiesteInviate(List<RichiesteInviateCumModel> aValore) {
		mListaRichiesteInviate = aValore;
	}

	public void setListaRichiestePmInCumulo(List<RichiestePmInCumuloModel> aValore) {
		mListaRichiestePm = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "IstruttoriaCumuloModel:\n" + "[ mIdIstruttoriaCumulo       = " + mIdIstruttoriaCumulo + " ]\n"
				+ "[ mFasSieIdFascicoloSiep     = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mEveIdEventoIstr           = " + mEveIdEventoIstr + " ]\n"
				+ "[ mEveIdEventoProv           = " + mEveIdEventoProv + " ]\n"
				+ "[ mAnnoProtocollo            = " + mAnnoProtocollo + " ]\n"
				+ "[ mNumProtocollo             = " + mNumProtocollo + " ]\n"
				+ "[ mChiaveUfficio             = " + mChiaveUfficio + " ]\n"
				+ "[ mDataApertura              = " + mDataApertura + " ]\n"
				+ "[ mDataChiusura              = " + mDataChiusura + " ]\n"
				+ "[ mNote                      = " + mNote + " ]\n" + "[ mFlagStato                 = "
				+ mFlagStato + " ]\n" + "[ mOrdinamentoTitoli         = " + mOrdinamentoTitoli + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}
}
