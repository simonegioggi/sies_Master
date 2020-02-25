package siap.sige.provvedimento.model;

import java.util.List;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sige.documentoallegato.model.DocumentoAllegatoModel;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ProvvedimentoSigeEventoModel
 * </p>
 * <p>
 * Description: Questo Classe Model associa il PROVVEDIMENTO_SIGE con l'EVENTO collegato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProvvedimentoSigeEventoModel extends GenericModel {

	private static final long serialVersionUID = 4856436165423977091L;
	private EventoNotificaModel mEventoNotifica;
	private ProvvedimentoSigeModel mProvvedimento;
	private Vector<TenoreSigeEstesoModel> mTenoriEstesi;
	private Vector mMotiviProvvedSige;
	private DocumentoAllegatoModel mFoglioComplementare;
	private boolean allNotified;
	private DocumentoAllegatoModel sollecito;
	private boolean depositoDecretoValidato = false;

	private Vector<ImpugnazioneSigeModel> ricorsi = null;
	private Vector<ImpugnazioneSigeModel> opposizioni = null;
	private List<ImpugnazioneSigeModel> impugnazioni = null;

	public DocumentoAllegatoModel getFoglioComplementare() {
		return mFoglioComplementare;
	}

	public void setFoglioComplementare(DocumentoAllegatoModel mFoglioComplementare) {
		this.mFoglioComplementare = mFoglioComplementare;
	}

	// COSTRUTTORE DI DEFAULT
	public ProvvedimentoSigeEventoModel() {
		mEventoNotifica = null;
		mProvvedimento = null;
		mTenoriEstesi = null;
		mMotiviProvvedSige = null;
	}

	// COSTRUTTORE DI COPIA
	public ProvvedimentoSigeEventoModel(ProvvedimentoSigeEventoModel aModel) {
		mEventoNotifica = new EventoNotificaModel(aModel.getEventoNotifica());
		mProvvedimento = new ProvvedimentoSigeModel(aModel.getProvvedimento());
		mTenoriEstesi = aModel.getTenoriEstesi();
		mMotiviProvvedSige = aModel.getMotiviProvvedSige();
	}

	// COSTRUTTORE MODEL
	public ProvvedimentoSigeEventoModel(EventoModel aEvento) {
		this();
		mEventoNotifica = new EventoNotificaModel(aEvento);
	}

	public ProvvedimentoSigeEventoModel(EventoNotificaModel aEventoNotifica) {
		this();
		mEventoNotifica = new EventoNotificaModel(aEventoNotifica);
	}

	public ProvvedimentoSigeEventoModel(ProvvedimentoSigeModel aProvvedimento) {
		this();
		mProvvedimento = new ProvvedimentoSigeModel(aProvvedimento);
		mEventoNotifica = new EventoNotificaModel();
	}

	public ProvvedimentoSigeEventoModel(EventoNotificaModel aEventoNotifica,
			ProvvedimentoSigeModel aProvvedimento, String aNomeTemplate) {
		mEventoNotifica = new EventoNotificaModel(aEventoNotifica);
		mProvvedimento = new ProvvedimentoSigeModel(aProvvedimento);
		mEventoNotifica.setNomeTemplate(aNomeTemplate);
	}

	// METODI GET()
	//
	public EventoNotificaModel getEventoNotifica() {
		return mEventoNotifica;
	}

	public ProvvedimentoSigeModel getProvvedimento() {
		return mProvvedimento;
	}

	public Vector getTenoriEstesi() {
		return mTenoriEstesi;
	}

	public Vector getMotiviProvvedSige() {
		return mMotiviProvvedSige;
	}

	// METODI SET()
	//
	public void setEventoNotifica(EventoNotificaModel aValore) {
		mEventoNotifica = aValore;
	}

	public void setProvvedimento(ProvvedimentoSigeModel aValore) {
		mProvvedimento = aValore;
	}

	public void setTenoriEstesi(Vector<TenoreSigeEstesoModel> aValore) {
		mTenoriEstesi = aValore;
	}

	public void setMotiviProvvedSige(Vector aValore) {
		mMotiviProvvedSige = aValore;
	}

	public String toString() {
		String lRet = getClass().getName() + "\n";
		if (mEventoNotifica != null && mEventoNotifica.getEvento() != null)
			lRet += mEventoNotifica.getEvento().toString() + "\n";
		if (mProvvedimento != null)
			lRet += mProvvedimento.toString() + "\n";
		if (mEventoNotifica != null && mEventoNotifica.getNotifiche() != null) {
			int count = 0;
			while (count < mEventoNotifica.getNotifiche().length) {
				lRet += mEventoNotifica.getNotifiche()[count].toString();
				count++;
			}
		}
		if (mEventoNotifica != null && mEventoNotifica.getNomeTemplate() != null)
			lRet += mEventoNotifica.getNomeTemplate().toString() + "\n";

		return lRet;
	}

	public boolean isAllNotified() {
		return allNotified;
	}

	public void setAllNotified(boolean allNotified) {
		this.allNotified = allNotified;
	}

	public DocumentoAllegatoModel getSollecito() {
		return sollecito;
	}

	public void setSollecito(DocumentoAllegatoModel sollecito) {
		this.sollecito = sollecito;
	}

	public boolean isDepositoDecretoValidato() {
		return depositoDecretoValidato;
	}

	public void setDepositoDecretoValidato(boolean depositoDecretoValidato) {
		this.depositoDecretoValidato = depositoDecretoValidato;
	}

	public Vector<ImpugnazioneSigeModel> getRicorsi() {
		return ricorsi;
	}

	public void setRicorsi(Vector<ImpugnazioneSigeModel> ricorsi) {
		this.ricorsi = ricorsi;
	}

	public Vector<ImpugnazioneSigeModel> getOpposizioni() {
		return opposizioni;
	}

	public void setOpposizioni(Vector<ImpugnazioneSigeModel> opposizioni) {
		this.opposizioni = opposizioni;
	}

	public List<ImpugnazioneSigeModel> getImpugnazioni() {
		return impugnazioni;
	}

	public void setImpugnazioni(List<ImpugnazioneSigeModel> impugnazioni) {
		this.impugnazioni = impugnazioni;
	}

}