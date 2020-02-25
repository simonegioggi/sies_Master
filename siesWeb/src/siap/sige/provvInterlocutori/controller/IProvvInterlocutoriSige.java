package siap.sige.provvInterlocutori.controller;

/**
* <p>Title: ProvvInterlocutoriController</p>
* <p>Description: Classe Controller per Provvedimenti Interlocutori</p>
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: </p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.model.EventoModel;
import siap.sico.utente.model.UtenteModel;
import f3b.util.F3BException;

public interface IProvvInterlocutoriSige
{
public ByteArrayOutputStream ExStampaProvvInterlocutorio(EventoModel lEvento, BigDecimal aIdFasSige, String lTipoUfficio, UtenteModel aUtenteModel )
						throws F3BException;

}
