package siap.sico.stampa.controller;

import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

public interface IStampa
{
	public TreeModel prelevaDatiEventoSiep(EventoNotificaModel aEveModel )
    throws F3BException;

	public TreeModel prelevaDatiEventoSiep(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
    throws F3BException;

	public TreeModel prelevaDatiIstruttoria(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
    throws F3BException;

  public TreeModel prelevaDatiEventoSiepXAnnotazioni(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
    throws F3BException;

  public TreeModel prelevaDatiAvvocato(AvvocatoFascicoloSiepModel aAvvocatoSiep, UtenteModel aUtenteModel )
    throws F3BException;

  public TreeModel prelevaDatiSospensioni(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
    throws F3BException;

  public TreeModel prelevaDatiDifferimento(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
  throws F3BException;

  public TreeModel prelevaDatiEventoSiepXCumulo(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
    throws F3BException;

  public TreeModel prelevaDatiPenaSospesa(EventoNotificaModel aEveModel, UtenteModel aUtenteModel) throws F3BException;
//  public XModel createRoot(EventoNotificaModel aEveModel) throws F3BException;
}