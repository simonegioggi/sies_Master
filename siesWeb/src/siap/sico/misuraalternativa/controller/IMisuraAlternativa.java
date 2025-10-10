package siap.sico.misuraalternativa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.verbale.model.VerbaleModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MisuraAlternativaController
 * </p>
 * <p>
 * Description: Classe Controller per MisuraAlternativa
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IMisuraAlternativa {

//modifica
	public MisuraAlternativaModel ExModificaMisuraAlternativa(MisuraAlternativaModel aMisuraAlternativa)
			throws F3BException;

  /**
	 * Aggiorna la misura alternativa con data inizio, data fine e durata. Calcolo la durata della misura
	 * alternativa che è = pena residua se il soggetto era inizialmente libero, o pari al residuo pena alla
	 * data di sottoscrizione se il soggetto era già in espiazione. Aggiorna il fine pena manuale e Valida la
	 * pena residua associata al Verbale Aggiorna lo scadenzario.
	 * 
	 * @param lVerMod
	 *            - Model del verbale di sottoscrizione
	 * @param lPen
	 *            - PenaResiduaModel legato al verbale di sottoscrizione
	 * @param lPos
	 *            - Posizione Giuridica Precedente
	 * @param lMisMod
	 *            - Misura Alternativa Corrente
   * @param aKeyFascicolo
   * @return
   * @throws F3BException
   */
	public MisuraAlternativaModel ExCalcolaFineEspiazionePenaMAConcessa(VerbaleModel lVerMod,
			PenaResiduaModel lPen, PosizioneGiuridicaModel lPos, MisuraAlternativaModel lMisMod,
			BigDecimal aKeyFascicolo) throws F3BException;

	public MisuraAlternativaModel ExCalcolaFineEspiazionePenaMARevocata(PosizioneGiuridicaModel lPos,
			MisuraAlternativaModel lMisMod, BigDecimal aKeyFascicolo, String FlagRicalcola)
			throws F3BException;

//inserimenti
	public EventoNotificaModel ExInserisciOModificaOSNotifica(EventoNotificaModel aEvento, String tipoMisura)
			throws F3BException;

	public EventoNotificaModel ExInserisciOModificaMANotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, MisuraAlternativaModel aMisura, SospensioneModel aSospMod)
			throws F3BException;

  public MisuraAlternativaModel ExInserisciMisuraAlternativaEventoNotifica(EventoNotificaModel aEveNotMod,
			DepositoOrdinanzaPcModel lDepOrdMod, TenoreModel lTenMod,
     MisuraAlternativaModel aMisuraAlternativa) throws F3BException;
  public MisuraAlternativaModel ExInserisciDecretoSospEventoNotifica(EventoNotificaModel aEveNotMod,
			DepositoDecretoModel lDepDecMod, TenoreModel lTenMod, MisuraAlternativaModel aMisuraAlternativa)
			throws F3BException;

//ricerche
	public MisuraAlternativaModel ExRicercaMisuraAlternativaCorrenteByIdFascicolo(BigDecimal aKey)
			throws F3BException;

	public MisuraAlternativaModel ExRicercaMisuraAlternativaByFascicoloOrdinanza(BigDecimal aKey)
			throws F3BException;

  public MisuraAlternativaModel ExRicercaMisuraAlternativaByIdEvento(BigDecimal aKey) throws F3BException;
  public MisuraAlternativaModel ExRicercaMisuraAlternativaByKey(BigDecimal aKey) throws F3BException;

	public MisuraAlternativaModel ExRicercaMisuraAlternativaConcessaCorrenteByIdFascicolo(
			BigDecimal aKeyFascicolo) throws F3BException;

	public MisuraAlternativaModel ExRicercaMisuraAlternativaSospesaCorrenteByIdFascicolo(
			BigDecimal aKeyFascicolo) throws F3BException;

	public MisuraAlternativaModel ExRicercaMisuraAlternativaPerOrdineScarcerazioneByIdFascicolo(
			BigDecimal aKey) throws F3BException;

	public MisuraAlternativaModel ExRicercaMisuraAlternativaPerOSLiberazioneAnticipataMAByIdFascicolo(
			BigDecimal aKey) throws F3BException;

  public List ExRicercaMisureAlternativeEventiOrderDesc( BigDecimal aIdFascicolo,
			String[] aCodTipoDecisione, String[] aCodNaturaDecisione, String[] aCodTipoMisura)
                                                      throws F3BException;
  
	public MisuraAlternativaModel ExRicercaMisuraAlternativaCorrenteByAnnoProgr(BigDecimal anno,
			BigDecimal progr) throws F3BException;

//validazioni
	public EventoModel ExUpdateValidaMADetDomTemp(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaMAPerditaEfficacia(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaMARipristino(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	// public EventoModel ExUpdateValidaMA(EventoModel aEvento, FascicoloSiepModel aFascicolo, String
	// tipoMisura, String IdEveAmmProvvAff) throws F3BException;
	public EventoModel ExUpdateValidaMA(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaMASospProvv(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaMARevoca(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaMACessazione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaMACessazione51bisMDS(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

    public Vector ExRicercaMisureAlternativeByIdFascicolo(BigDecimal aKey) throws F3BException;

    public String ExInserisciMisuraAlternativaWithoutSequence(MisuraAlternativaModel aMisuraAlternativa, Connection lConn)
    		throws F3BException;
    
	public String ExInserisciMisuraAlternativaWithoutSequence(ArrayList aMisureAlternative, Connection lConn)
			throws F3BException;

	public MisuraAlternativaModel ExInserisciMisuraAlternativa(MisuraAlternativaModel aMisuraAlternativa)
			throws F3BException;

	// MEV_2019-09-SIEP si aggiunge metodo generico che inserisce si deposoto decreto o deposito ordinanza
  public MisuraAlternativaModel ExInserisciDecretoOrdinanzaMisAlt (EventoNotificaModel aEveNotMod,
      DepositoDecretoModel lDepDecMod, DepositoOrdinanzaPcModel lDepOrdMod, TenoreModel lTenMod
      , MisuraAlternativaModel aMisuraAlternativa)
      throws F3BException ;
}