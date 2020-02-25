package siap.siep.competenza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;
import f3b.util.F3BException;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.EventoModel;
import siap.siep.competenza.model.CompetenzaModel;

/**
 * <p>
 * Title: CompetenzaController
 * </p>
 * <p>
 * Description: Classe Controller per Competenza
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
public interface ICompetenza {

	public CompetenzaModel ExInserisciCompetenza(CompetenzaModel aCompetenza) throws F3BException;

	public Vector ExRicercaCompetenza(CompetenzaModel aCompetenza) throws F3BException;

	public void ExModificaCompetenza(CompetenzaModel aCompetenza) throws F3BException;

	public void ExCancellaCompetenza(CompetenzaModel aCompetenza) throws F3BException;

	public BigDecimal ExGetCountCompetenza(CompetenzaModel aCompetenza) throws F3BException;

	public CompetenzaModel ExRicercaCompetenzaById(BigDecimal aIdCompetenza) throws F3BException;

	public Vector ExRicercaCompetenzaPaged(CompetenzaModel aCompetenza, int aPage) throws F3BException;

	public CompetenzaModel ExRicercaCompetenzaByEveIdEvento(BigDecimal aIdCompetenza) throws F3BException;

	public EventoNotificaModel ExInserisciRichiestaTrasmissioneAtti(EventoNotificaModel aEveNotModel,
			CompetenzaModel aCompetenzaModel) throws F3BException;

	public Vector ExRicercaCompetenzaByIdFascicoloSiep(BigDecimal aIdFascicoloSiep) throws F3BException;

	public String ExInserisciCompetenzeWithoutSequence(ArrayList aListaCompetenze, Connection lConn)
			throws F3BException;

	public void ExModificaEvento_e_Competenza(EventoModel aEveModel, CompetenzaModel mCompModel)
			throws F3BException;

	public CompetenzaModel ExRicercaCompetenzaByIdMessaggioRichiesta(BigDecimal aIdMessaggio)
			throws F3BException;

	public CompetenzaModel ExInserisciCompetenzaEventoNotifica(CompetenzaModel mCompModel,
			EventoNotificaModel aEveModel) throws F3BException;

	// mev_39
	public Vector ExRicercaCompetenzePerCumulo(BigDecimal aIdFascicoloSiep, boolean stessoDistretto) throws F3BException;

}