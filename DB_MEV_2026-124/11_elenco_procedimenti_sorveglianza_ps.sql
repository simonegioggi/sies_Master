-- 20261126-MEV_2026-124_SG - 3.2.2 Elenco Procedimenti della Sorveglianza e del GE: nuova funzione per la nuova action
-- con annotazioni di esecuzione pena sostitutiva. La funzione 32010012 NON viene modificata.
-- I profili sono copiati da 32010012. Idempotente.
DECLARE
  n NUMBER;
BEGIN
  SELECT COUNT(*) INTO n FROM funzione WHERE id_funzione = 25020412;
  IF n = 0 THEN
    INSERT INTO funzione (id_funzione, descrizione, cod_tipo_funzione, azione_contesto_java)
    VALUES (25020412, 'Elenco Procedimenti Sorveglianza e GE - Pene Sostitutive', 'R',
            'siap.sius.fascicolo.action.ActRicercaProcedimentiPerNumeroSIEPPS');
  END IF;
  INSERT INTO funzione_profilo (data_inizio_validita, fun_id_funzione, prf_cod_profilo, flag_attivo,
                                cod_operatore_inserimento, data_inserimento)
  SELECT fp.data_inizio_validita, 25020412, fp.prf_cod_profilo, fp.flag_attivo,
         '20261126-MEV_2026-124_SG', SYSDATE
    FROM funzione_profilo fp
   WHERE fp.fun_id_funzione = 32010012
     AND NOT EXISTS (SELECT 1 FROM funzione_profilo x
                      WHERE x.fun_id_funzione = 25020412 AND x.prf_cod_profilo = fp.prf_cod_profilo);
  COMMIT;
END;
/
