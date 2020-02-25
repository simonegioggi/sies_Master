package siap.sius.stralcio.controller;

/**
* <p>Title: StralcioController</p>
* <p>Description: Classe Controller per Stralcio</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IStralcio {

	public FascicoloGPModel ExVerificaFascicoloDestStralcio(BigDecimal lIdSoggetto, String annoDestStralcio,
			String progrDestStralcio, String aUfficioUtenteConnesso) throws F3BException;

	public EventoModel ExInserisciStralcio(BigDecimal lIdFasSiusDaStralciare,
			FascicoloGPModel lFasDestStralcio, Date dataStralcio, String[] lArrayCheckBox,
			Integer numOggettiIniziali, String aUfficioUtenteConnesso, EventoModel aEvento)
			throws F3BException;

	public void ExCancellaStralcio(BigDecimal aKeyEvento, String aUfficioUtenteConnesso,
			String aUtenteConnesso, String aLuogoUfficioUtenteConnesso) throws F3BException;

	public Vector ExRicercaTenoriStralciatiByIdFascicolo(BigDecimal aIdFascicoloStralciato)
			throws F3BException;

	// public EventoNotificaModel ExStampaStralcio ( EventoModel aEvento , UfficioModel lUfficio, UtenteModel
	// aUtenteModel)
	// throws F3BException;

}