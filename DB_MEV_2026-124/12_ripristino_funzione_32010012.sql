-- 20261126-MEV_2026-124_SG - Ripristino della funzione 32010012 alla action originale
-- (la versione precedente dello script 11 l'aveva ridirezionata alla nuova action). Idempotente.
UPDATE funzione
   SET azione_contesto_java = 'siap.sius.fascicolo.action.ActRicercaProcedimentiPerNumeroSIEP'
 WHERE id_funzione = 32010012
   AND azione_contesto_java = 'siap.sius.fascicolo.action.ActRicercaProcedimentiPerNumeroSIEPPS';
COMMIT;
/
