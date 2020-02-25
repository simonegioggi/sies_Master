package siap.sius.richiestaatti.controller;

import java.math.BigDecimal;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import f3b.util.F3BException;

public interface IRichiestaAtti
{
  public EventoNotificaModel ExStampaRichiestaAtti ( EventoModel aEvento, UfficioModel lUfficio,UtenteModel lUtenteMod)
  throws F3BException;

  public DocumentoAllegatoModel ExInserisciSollecito ( EventoNotificaModel aEveNot ,DocumentoAllegatoModel aDocAllegato,UfficioModel lUfficio,UtenteModel lUtenteMod)
  throws F3BException;

  public void ExCancellaRichiestaAtti( BigDecimal aIdEvento )
      throws Exception;


}