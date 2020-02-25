package siap.siep.archiviazione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.sico.evento.model.EventoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * <p>
 * Title: ArchiviazioneController
 * </p>
 * <p>
 * Description: Classe Controller per Archiviazione
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
public interface IArchiviazione {

	public ArchiviazioneModel ExInserisciEventoNotificaArchiviazione(EventoNotificaModel aEveNotMod,
			ArchiviazioneModel aArchiviazione, FascicoloSiepModel aFascicolo) throws F3BException;

	public Vector ExRicercaArchiviazione(ArchiviazioneModel aArchiviazione) throws F3BException;

	public ArchiviazioneModel ExRicercaArchiviazioneByKey(BigDecimal aKey) throws F3BException;

	public ArchiviazioneModel ExRicercaArchiviazioneByIdEvento(BigDecimal aKey) throws F3BException;

	public ArchiviazioneModel ExRicercaArchiviazioneCssaIstitutoByIdEvento(BigDecimal aKey)
			throws F3BException;

	public EventoModel ExUpdateValidaArchiviazione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaArchiviazione(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			Connection aDBConnection) throws F3BException;

	public EventoModel ExUpdateValidaAnnProvCumulo(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaAnnProvCumulo(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			Connection aDBConnection) throws F3BException;

	public EventoModel ExUpdateValidaVistoAttesa(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			String aStato) throws F3BException;

	/**
	 * Effettua l'archiviazione semplificata dei fascicolo migrati RES. - Aggiorna il fascicolo SIEP -
	 * Aggiorna la posizione giuridica in Libero - Aggiorna lo stato procedimento - Cancella eventuali
	 * scadenzari - Azzera la pena residua e le date.
	 * 
	 * @param aFascicolo
	 * @throws F3BException
	 */
	public void ExArchiviazioneSemplificataRES(FascicoloSiepModel aFascicolo) throws F3BException;

}