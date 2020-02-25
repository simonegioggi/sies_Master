package siap.sico.decodifiche.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import f3b.util.F3BException;

/**
 * Classe con le più nuove decodifiche
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class DecodificheManagerCore extends DecodificheManagerBean {

	private Collection mTipoUfficioSIEPSIGE;
	private Collection mRagioneSociale;
	private Collection mTipoMisuraCautelareDetentive;
	private Collection mTipoMisuraCautelareNonDetentive;
	private Collection mAutoritaCompetentePerTerritorio;
	private Collection mUfficioEmittenteArrestiDomiciliari;
	private Collection mTipoUfficioMinor;
	private Collection mTipoUfficioRegGen;
	private Collection mMotivoProvvedimentoSospProvvDetArrestiDom;
	private Collection mMotivoProvvedimentoRipristinoDetArrestiDom;
	private Collection mMotivoProvvedimentoRevocaDetArrestiDom;

	/**
	 * Inizializzazione degli attributi del Singleton
	 */
	protected void init() {
		super.init();
		DecodificheModel lModel = new DecodificheModel();
		try {
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();

			// popolato con misure detentive
			lModel = new DecodificheModel();
			List lListMisuraCautelareDetentive = new ArrayList();
			lModel.setContesto("TIPO_MISURA_CAUTELARE");
			// settiamo i codici, tabella MISURA_CAUTELARE.RV_LOW_VALUE
			lModel.setCode("-");
			lListMisuraCautelareDetentive.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			// lModel.setCode("CE");
			// lListMisuraCautelareDetentive.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("CA");
			lListMisuraCautelareDetentive.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("CD");
			lListMisuraCautelareDetentive.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mTipoMisuraCautelareDetentive = lListMisuraCautelareDetentive;

			// popolato con misure non detentive
			lModel = new DecodificheModel();
			List lListMisuraCautelareNonDetentive = new ArrayList();
			lModel.setContesto("TIPO_MISURA_CAUTELARE");
			// settiamo i codici, tabella MISURA_CAUTELARE.RV_LOW_VALUE
			lModel.setCode("-");
			lListMisuraCautelareNonDetentive.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			// 20170802: modifica su richiesta Amministrazione --> tolgo CL
//			lModel.setCode("CL");
//			lListMisuraCautelareNonDetentive.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("AD");
			lListMisuraCautelareNonDetentive.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("CB");
			lListMisuraCautelareNonDetentive.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("CC");
			lListMisuraCautelareNonDetentive.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			// lModel.setCode("CD");
			// lListMisuraCautelareNonDetentive.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("CE");
			lListMisuraCautelareNonDetentive.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mTipoMisuraCautelareNonDetentive = lListMisuraCautelareNonDetentive;

			// Autorita' competente per territorio
			lModel = new DecodificheModel();
			List lListAutoritaCompetentePerTerritorio = new ArrayList();
			lModel.setContesto("TIPO_AUTORITA");
			lModel.setCode("-");
			lListAutoritaCompetentePerTerritorio.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setContesto("TIPO_AUTORITA");
			lModel.setCode("19");
			lListAutoritaCompetentePerTerritorio.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setContesto("TIPO_AUTORITA");
			lModel.setCode("20");
			lListAutoritaCompetentePerTerritorio.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setContesto("TIPO_AUTORITA");
			lModel.setCode("28");
			lListAutoritaCompetentePerTerritorio.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setContesto("TIPO_AUTORITA");
			lModel.setCode("32");
			lListAutoritaCompetentePerTerritorio.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setContesto("TIPO_AUTORITA");
			lModel.setCode("92");
			lListAutoritaCompetentePerTerritorio.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mAutoritaCompetentePerTerritorio = lListAutoritaCompetentePerTerritorio;

			// tipo ufficio emittente arresti domiciliari
			lModel = new DecodificheModel();
			List lListUfficioEmittenteArrestiDomiciliari = new ArrayList();
			lModel.setContesto("TIPO_UFFICIO");
			// settiamo i codici
			lModel.setCode("-");
			lListUfficioEmittenteArrestiDomiciliari.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("TDS");
			lListUfficioEmittenteArrestiDomiciliari.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("UDS");
			lListUfficioEmittenteArrestiDomiciliari.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("TDSM");
			lListUfficioEmittenteArrestiDomiciliari.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			lModel.setCode("UDSM");
			lListUfficioEmittenteArrestiDomiciliari.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
			mUfficioEmittenteArrestiDomiciliari = lListUfficioEmittenteArrestiDomiciliari;

			mTipoUfficioMinor = new Vector();
			mTipoUfficioMinor.add(new DecodificheModel("-", "-", "-", "-", "-", "-", "-", "-", "-"));
			mTipoUfficioMinor.add(new DecodificheModel("PM", "PROCURA REPUBBLICA PRESSO TRIBUNALE",
					"TIPO_PROCURA", "", "", "", "", "", ""));
			mTipoUfficioMinor.add(new DecodificheModel("PMM",
					"PROCURA DELLA REPUBBLICA PRESSO IL TRIBUNALE DEI MINORENNI", "TIPO_PROCURA", "", "", "",
					"", "", ""));
			mTipoUfficioMinor.add(new DecodificheModel("PGCAP", "PROCURA GENERALE PRESSO CORTE D'APPELLO",
					"TIPO_PROCURA", "", "", "", "", "", ""));

			mTipoUfficioRegGen = new Vector();
			mTipoUfficioRegGen.add(new DecodificheModel("-", "-", "-", "-", "-", "-", "-", "-", "-"));
			mTipoUfficioRegGen.add(new DecodificheModel("GIP", "GIP", "TIPO_UFFICIO_GE", "", "", "", "", "",
					""));
			mTipoUfficioRegGen.add(new DecodificheModel("DIB", "DIB", "TIPO_UFFICIO_GE", "", "", "", "", "",
					""));
			mTipoUfficioRegGen.add(new DecodificheModel("CAS", "CAS", "TIPO_UFFICIO_GE", "", "", "", "", "",
					""));
			mTipoUfficioRegGen.add(new DecodificheModel("CAP", "CAP", "TIPO_UFFICIO_GE", "", "", "", "", "",
					""));
			mTipoUfficioRegGen.add(new DecodificheModel("CASAP", "CASAP", "TIPO_UFFICIO_GE", "", "", "", "",
					"", ""));

			mMotivoProvvedimentoSospProvvDetArrestiDom = lDecodifiche
					.ExListaMotivoProvvSospProvvAD("DETENZIONE");
			mMotivoProvvedimentoRipristinoDetArrestiDom = lDecodifiche
					.ExListaMotivoProvvRipristinoAD("DETENZIONE");
			mMotivoProvvedimentoRevocaDetArrestiDom = lDecodifiche.ExListaMotivoProvvRevocaAD("DETENZIONE");

			mTipoUfficioSIEPSIGE = new Vector();
			mTipoUfficioSIEPSIGE.add(new DecodificheModel("PM", "PROCURA REPUBBLICA PRESSO TRIBUNALE",
					"TIPO_PROCURA", "", "", "", "", "", ""));
			mTipoUfficioSIEPSIGE.add(new DecodificheModel("PGCAP", "PROCURA GENERALE PRESSO CORTE D'APPELLO",
					"TIPO_PROCURA", "", "", "", "", "", ""));
			mTipoUfficioSIEPSIGE.add(new DecodificheModel("PMM",
					"PROCURA REPUBBLICA PRESSO TRIBUNALE PER I MINORENNI", "TIPO_PROCURA", "", "", "", "",
					"", ""));

			mRagioneSociale = new Vector();
			mRagioneSociale.add(new DecodificheModel("-", "-", "-", "-", "-", "-", "-", "-", "-"));
			mRagioneSociale.add(new DecodificheModel("SpA", "S.p.A.", "RAGIONE_SOCIALE", "", "", "", "", "",
					""));
			mRagioneSociale.add(new DecodificheModel("Srl", "S.r.l.", "RAGIONE_SOCIALE", "", "", "", "", "",
					""));
			mRagioneSociale.add(new DecodificheModel("SapA", "S.a.p.A.", "RAGIONE_SOCIALE", "", "", "", "",
					"", ""));
			mRagioneSociale
					.add(new DecodificheModel("Ss", "S.s.", "RAGIONE_SOCIALE", "", "", "", "", "", ""));
			mRagioneSociale.add(new DecodificheModel("Snc", "S.n.c.", "RAGIONE_SOCIALE", "", "", "", "", "",
					""));
			mRagioneSociale.add(new DecodificheModel("Sas", "S.a.s.", "RAGIONE_SOCIALE", "", "", "", "", "",
					""));
		} catch (F3BException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Collection getTipoUfficioSIEPSIGE() {
		return mTipoUfficioSIEPSIGE;
	}

	public Collection getRagioneSociale() {
		return mRagioneSociale;
	}

	public Collection getTipoMisuraCautelareDetentive() {
		return mTipoMisuraCautelareDetentive;
	}

	public Collection getAutoritaCompetentePerTerritorio() {
		return mAutoritaCompetentePerTerritorio;
	}

	public Collection getTipoMisuraCautelareNonDetentive() {
		return mTipoMisuraCautelareNonDetentive;
	}

	public Collection getUfficioEmittenteArrestiDomiciliari() {
		return mUfficioEmittenteArrestiDomiciliari;
	}

	public Collection getTipoUfficioMinor() {
		return mTipoUfficioMinor;
	}

	public Collection getTipoUfficioRegGen() {
		return mTipoUfficioRegGen;
	}

	public Collection getMotivoProvvedimentoSospProvvArrestiDom() {
		return mMotivoProvvedimentoSospProvvDetArrestiDom;
	}

	public Collection getMotivoProvvedimentoRipristinoArrestiDom() {
		return mMotivoProvvedimentoRipristinoDetArrestiDom;
	}

	public Collection getMotivoProvvedimentoRevocaArrestiDom() {
		return mMotivoProvvedimentoRevocaDetArrestiDom;
	}

}