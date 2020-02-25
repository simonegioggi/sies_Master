package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.sius.statistiche.model.IspMotivoOggettoModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

public class IspMotivoOggettoSqlDAO extends SqlDAO {
    public IspMotivoOggettoSqlDAO(Connection conn) {
        super(conn);
    }
    
    
    public GenericModel getModel() throws DAOException {
        IspMotivoOggettoModel lModel = new IspMotivoOggettoModel();
        lModel.setCodMotivo(getString("COD_MOTIVO"));
        lModel.setDescMotivo(getString("DESC_MOTIVO"));
        lModel.setCodOggetto(getString("COD_OGGETTO"));
        lModel.setDescOggetto(getString("DESC_OGGETTO"));
        lModel.setTipoUfficio(getString("TIPO_UFFICIO"));
        return lModel;
    }   
}