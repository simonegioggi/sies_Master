package siap.siep.scambiosanzione.controller;

/**
* <p>Title: ScambioSanzioneController</p>
* <p>Description: Classe Controller per ScambioSanzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IScambioSanzione {

	public Vector ExRicercaScambioSanzione(ScambioSanzioneModel aScambioSanzione) throws F3BException;

	public BigDecimal ExGetCountScambioSanzione(ScambioSanzioneModel aScambioSanzione) throws F3BException;

	public ScambioSanzioneModel ExRicercaScambioSanzioneById(BigDecimal aIdScambioSanzione)
			throws F3BException;

	public ScambioSanzioneModel ExRicercaScambioSanzioneByIdXRichConv(BigDecimal aIdScambioSanzione)
			throws F3BException;

	public Vector ExRicercaScambioSanzioneRichConv(String TipoDec, BigDecimal aIdFascicolo)
			throws F3BException;

	public Vector[] ExRicercaScambioSanzioneRichConv(String[] aTipoProv, BigDecimal aIdFascicolo)
			throws F3BException;

	public Vector[] ExRicercaScambioSanzioneRichConvEPS(String[] aTipoProv, BigDecimal aIdFascicolo)
			throws F3BException;

	public Vector ExRicercaScambioSanzionePaged(ScambioSanzioneModel aScambioSanzione, int aPage)
			throws F3BException;

	public List ExRicercaScambioSanzioneByIdFascicoloSiepNaturaTipo(BigDecimal aFascicolo,
			String[] aTipoDecisone, String[] aNaturaSanzione, String[] aTipoSanzione) throws F3BException;

	public EventoModel ExInserisciRevocaConversione(EventoNotificaModel aEveUffMod,
			DepositoOrdinanzaPcModel lDepOrdMod, DepositoDecretoModel lDepDecMod, TenoreModel lTenMod,
			ScambioSanzioneModel lScSanzioneMod, LuogoDetenzioneModel aLuogoDetenzione,
			AltraCausaModel aAltraCausa, EventoModel aEveMod, PosizioneGiuridicaModel aPos,
			FascicoloSiepModel aFascicoloModel, PenaResiduaModel aPenaResidua,
			StatoProcedimentoModel aStatoProcMod, String aPosizione, AnnotazioneManualeModel aAnnMod)
			throws F3BException;

	public EventoModel ExInserisciEventoAggiornaStatoProcedimento(EventoModel aEveMod,
			PosizioneGiuridicaModel aPos, PenaResiduaModel aPenaResMod, StatoProcedimentoModel aStatoProcMod)
			throws F3BException;

	public ScambioSanzioneModel ExRicercaScambioSanzioneByEveIdEvento(BigDecimal aIdEvento)
			throws F3BException;

	public ScambioSanzioneModel ExRicercaScambioSanzioneByEveIdEventoNoControlValid(BigDecimal aIdEvento)
			throws F3BException;

}