-- 20261124-MEV_2026-124_SG - 3.2.2 Elenco Procedimenti della Sorveglianza e del GE: nuova action con annotazioni
-- di esecuzione pena sostitutiva (la funzione mantiene id e profili esistenti). Idempotente.
UPDATE funzione
   SET azione_contesto_java = 'siap.sius.fascicolo.action.ActRicercaProcedimentiPerNumeroSIEPPS'
 WHERE id_funzione = 32010012
   AND azione_contesto_java = 'siap.sius.fascicolo.action.ActRicercaProcedimentiPerNumeroSIEP';
COMMIT;
/
