package siap.sius.depositosentenza.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.utente.model.UtenteModel;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.depositosentenza.model.SentenzaEventoTenoriGProcModel;
import siap.sius.depositosentenza.model.SentenzaEventoTenoriPrescrizioniModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import f3b.util.F3BException;

/**
 * <p>Title: DepositoSentenzaController</p>
 * <p>Description: Classe Controller per DepositoSentenza</p>
 * <p>Company: Engineering S.p.A.</p>
 * @version 1.0
 */
public interface IDepositoSentenza
{

	public boolean ExEsisteDepositoSentenzaByGenProcEccettoTipi( BigDecimal aKey,  String[] aTipiDaEscludere)
		throws F3BException;
	
	public DepositoSentenzaModel ExRicercaDepositoSentenzaByAnnoNumUfficio (DepositoSentenzaModel aDepositoSentenza )
		throws F3BException;

	public SentenzaEventoTenoriGProcModel ExInserisciSentenza( SentenzaEventoTenoriGProcModel aGProcSenEveTenori)
			throws F3BException;

	public SentenzaEventoTenoriGProcModel ExInserisciSentenza( SentenzaEventoTenoriGProcModel aGProcSenEveTenori, BigDecimal IdFascicoloOrigine)
			throws F3BException;

	public SentenzaEventoTenoriPrescrizioniModel ExRicercaSentenzaEventoTenoriPrescrizioniByIdEvento( BigDecimal aIdEvento)
			throws F3BException;

	public DepositoSentenzaModel ExRicercaDepositoSentenzaByEvento (BigDecimal aEveKey)
			throws F3BException;

	public void ExCancellaDepositoSentenza(DepositoSentenzaModel aDepSen)
			throws F3BException;

	public void ExCancellaRimessioneAtti(DepositoSentenzaModel aDepSen)
			throws F3BException;

	public ByteArrayOutputStream ExStampEmissioneSentenza ( EventoModel lEvento, String aCodUff, UtenteModel aUtenteModel)
			throws F3BException;

	public DocumentoAllegatoModel ExInserisciDataDepositoSentenza ( FascicoloGPModel aFasGPMod, DepositoSentenzaModel aDepositoSentenza, EventoNotificaModel  aEveNot, String[] lCheck, ScadenzarioSiusModel lScadenzarioSiusModPrincipal, ScadenzarioSiusModel lScadenzarioSiusModSecond )
			throws F3BException;

	public DocumentoAllegatoModel ExModificaDataDepositoSentenza (FascicoloGPModel aFasGPMod, DepositoSentenzaModel aDepositoSentenza, EventoNotificaModel aEveNot , String[] lCheck)
			throws F3BException;

	public ByteArrayOutputStream ExStampaDocumentoAllegato ( BigDecimal aIdFascicoloSius, DocumentoAllegatoModel aDAMod, String aCodUff, UtenteModel aUtenteModel )
			throws F3BException;

	public DepositoSentenzaModel ExRicercaSentenzaRimessioneAttiPcByKeyPerUpdate (BigDecimal aKey)
			throws F3BException;

	public DepositoSentenzaModel ExModificaDepositoSentenza (DepositoSentenzaModel aDepositoSentenza )
			throws F3BException;

	public ByteArrayOutputStream ExStampaDocumentoModello ( FascicoloGPModel aFasc, EventoNotificaModel aEvento, UtenteModel aUtenteModel )
			throws F3BException;

	// Definizione del metodo afferente alla modifica del Magistrato alla Sentenza. 
	public DepositoSentenzaModel ExModificaMagistratoSentenza(DepositoSentenzaModel aDepSenMod )
			throws F3BException;

	public ByteArrayOutputStream ExStampaDocumento ( FascicoloGPModel aFasc, EventoNotificaModel aEvento, UtenteModel aUtenteModel )
			throws F3BException;

}
