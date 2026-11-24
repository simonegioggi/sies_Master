-- 20261124-MEV_2026-124_SG - 3.2.2 SIEP Scadenzario pene sostitutive scadute
-- Nuove azioni (Esecuzione/Procura: profili 4, 30, 40, 50) e voce nel menu Scadenzari (funzione 53). Idempotente.
DECLARE
  n NUMBER;
  PROCEDURE ins(p_id NUMBER, p_descr VARCHAR2, p_azione VARCHAR2) IS
  BEGIN
    SELECT COUNT(*) INTO n FROM funzione WHERE id_funzione = p_id;
    IF n = 0 THEN
      INSERT INTO funzione (id_funzione, descrizione, cod_tipo_funzione, azione_contesto_java)
      VALUES (p_id, p_descr, 'I', p_azione);
    END IF;
    FOR p IN (SELECT column_value AS prf FROM TABLE(sys.odcinumberlist(4, 30, 40, 50))) LOOP
      SELECT COUNT(*) INTO n FROM funzione_profilo WHERE fun_id_funzione = p_id AND prf_cod_profilo = p.prf;
      IF n = 0 THEN
        INSERT INTO funzione_profilo (data_inizio_validita, fun_id_funzione, prf_cod_profilo, flag_attivo)
        VALUES (TO_DATE('2000-01-01', 'YYYY-MM-DD'), p_id, p.prf, 'S');
      END IF;
    END LOOP;
  END;
BEGIN
  ins(25020410, 'Scadenzario Pene Sostitutive Scadute', 'siap.siep.scadenzario.action.ActLoadRicercaScadenzarioPenaSostitutiva');
  ins(25020411, 'Ricerca Scadenzario Pene Sostitutive Scadute', 'siap.siep.scadenzario.action.ActRicercaScadenzarioPenaSostitutiva');
  SELECT COUNT(*) INTO n FROM relazione_funzione WHERE fun_id_funzione = 53 AND fun_id_funzione_figlia = 25020410;
  IF n = 0 THEN
    INSERT INTO relazione_funzione (fun_id_funzione, fun_id_funzione_figlia, cod_tipo_visualizzazione,
                                    label_funzione, ordine_visualizzazione)
    VALUES (53, 25020410, 'BO', 'Pene Sostitutive Scadute', 13);
  END IF;
  COMMIT;
END;
/
