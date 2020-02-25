package siap.sius.statistiche.dao;

import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import siap.sius.statistiche.model.IspMotivoOggettoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

public class IspMotivoOggettoDAO extends TableDAO {

    //
    // Costruttore di default
    //
    public IspMotivoOggettoDAO(Connection conn) {
        
        super(conn);
        
        this.setTable("ISP_MOTIVO_OGGETTO");
        
        this.setField("COD_MOTIVO", STRING);
        this.setField("DESC_MOTIVO", STRING);
        this.setField("COD_OGGETTO", STRING);
        this.setField("DESC_OGGETTO", STRING);
        this.setField("TIPO_UFFICIO", STRING);
    }
    
    //
    // Metodi GET
    //
    public String   getCodMotivo()      throws DAOException  { return getString("COD_MOTIVO");      } 
    public String   getDescMotivo()     throws DAOException  { return getString("DESC_MOTIVO");     } 
    public String   getCodOggetto()     throws DAOException  { return getString("COD_OGGETTO");     } 
    public String   getDescOggetto()    throws DAOException  { return getString("DESC_OGGETTO");    } 
    public String   getTipoUfficio()    throws DAOException  { return getString("TIPO_UFFICIO");    } 
    
    //
    // Metodi SET
    //
    // Poiché la tabella è di sola lettura, non viene definito alcun metodo 'set'
    
    
    //
    // Model
    //
    public GenericModel getModel() throws DAOException
    {
        return new IspMotivoOggettoModel(
                this.getCodMotivo(),
                this.getDescMotivo(),
                this.getCodOggetto(),
                this.getDescOggetto(),
                this.getTipoUfficio()
                );
    }
    
    public void setCondizione(IspMotivoOggettoModel aModel)
    {
        String lCondizione = new String();
        
        // definendo la condizione '1 = 1' (che non ha alcun effetto sulla WHERE CONDITION),
        // è possibile aggiungere tutte le altre condizioni anteponendo sempre 'AND'. In questo modo non è più necessario effettuare il
        // controllo per verificare se la condizione che si sta inserendo è la prima [in questo caso 'AND' non deve essere aggiunto]
        // o è successiva alla prima [e quindi aggiungere 'AND']
        
        lCondizione = " 1 = 1 ";
        
        if(aModel != null) {
            if(aModel.getCodMotivo()    != null)     lCondizione += " AND COD_MOTIVO LIKE '"     + aModel.getCodMotivo()     + "' ";
            if(aModel.getDescMotivo()   != null)     lCondizione += " AND DESC_MOTIVO LIKE '"    + aModel.getDescMotivo()    + "' ";
            if(aModel.getCodOggetto()   != null)     lCondizione += " AND COD_OGGETTO LIKE '"    + aModel.getCodOggetto()    + "' ";
            if(aModel.getDescOggetto()  != null)     lCondizione += " AND DESC_OGGETTO LIKE '"   + aModel.getDescOggetto()   + "' ";
            if(aModel.getTipoUfficio()  != null)     lCondizione += " AND TIPO_UFFICIO LIKE '"   + aModel.getTipoUfficio()   + "' ";
        }
        
        this.setCondition(lCondizione);
    }
    
    /**
     * Questo metodo imposta una WHERE CONDITION necessaria ad ottenere l'eleco degli Oggetti 
     * non inclusi nell'insieme di oggetti i cui codici sono passati nel vettore di tipo TipoUfficio ricavato da codUfficio
     * @param aCodici
     * @param aTipoUfficio
     */
    public void setCondizioneEsclusioneCodiciOggetto(Vector<String> aCodici, String aCodUfficio) {
        String lCondizione = new String();
        
        if(aCodici != null) {
            if(aCodici.size() > 0) {
                lCondizione = "COD_OGGETTO NOT IN ( ";
                
                Iterator<String> iter = aCodici.iterator();
                
                while(iter.hasNext()) {
                    lCondizione += "'" + iter.next() + "',";
                }
                
                lCondizione = lCondizione.substring(0, lCondizione.length()-1);
                
                lCondizione += ") ";
            }
            if(aCodUfficio != null) {
                if(lCondizione.length() > 0) {
                    lCondizione += " AND ";
                }
                lCondizione +=  " TIPO_UFFICIO = (" +
                                    "SELECT " +
                                        "COD_TIPO_UFFICIO " + 
                                    "FROM " +
                                        "UFFICIO " +
                                    "WHERE " +
                                        "COD_UFFICIO = '" + aCodUfficio + "')";
            }
            this.setCondition(lCondizione);
        }
    }
    
    /**
     * Questo metodo imposta una WHERE CONDITION necessaria ad ottenere l'eleco degli Oggetti 
     * non inclusi nell'insieme di oggetti i cui codici sono passati nel vettore di tipo TipoUfficio ricavato da codUfficio
     * @param aCodici
     * @param aTipoUfficio
     */
    public void setCondizioneEsclusioneCodiciMotivo(Vector<String> aCodici, String aCodUfficio) {
        String lCondizione = new String();
        
        if(aCodici != null) {
            if(aCodici.size() > 0) {
                lCondizione = "COD_MOTIVO NOT IN ( ";
                
                Iterator<String> iter = aCodici.iterator();
                
                while(iter.hasNext()) {
                    lCondizione += "'" + iter.next() + "',";
                }
                
                lCondizione = lCondizione.substring(0, lCondizione.length()-1);
                
                lCondizione += ") ";
            }
            if(aCodUfficio != null) {
                if(lCondizione.length() > 0) {
                    lCondizione += " AND ";
                }
                lCondizione +=  " TIPO_UFFICIO = (" +
                                    "SELECT " +
                                        "COD_TIPO_UFFICIO " + 
                                    "FROM " +
                                        "UFFICIO " +
                                    "WHERE " +
                                        "COD_UFFICIO = '" + aCodUfficio + "')";
            }
            this.setCondition(lCondizione);
        }
    }

    public void setCondizione(String aWhereCondition)
    {
        this.setCondition(aWhereCondition);
    }
    
    public void setOrdine(String aOrder)
    {
        this.setOrder(aOrder);
    }
}
