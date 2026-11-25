-- MEV_2026-124_SG - 3.11 Ricerca procedimenti con pene sostitutive sospese e prive di data ripresa
-- Menu Statistiche/Monitoraggio > Ricerche. Idempotente.
-- Profili: Tribunale di Sorveglianza (14,31,41,51) e Ufficio di Sorveglianza (24,32,42,52),
-- gli stessi che vedono il menu Ricerche (38101000).
DECLARE
  c_id CONSTANT NUMBER := 38101047;
  n NUMBER;
BEGIN
  SELECT COUNT(*) INTO n FROM funzione WHERE id_funzione = c_id;
  IF n = 0 THEN
    INSERT INTO funzione (id_funzione, descrizione, cod_tipo_funzione, azione_contesto_java)
    VALUES (c_id, 'Ricerca procedimenti con pene sostitutive sospese e prive di data ripresa', 'R',
            'siap.sius.statistiche.action.ActRicercaPeneSostitutiveSospese');
  END IF;

  SELECT COUNT(*) INTO n FROM relazione_funzione
   WHERE fun_id_funzione = 38101000 AND fun_id_funzione_figlia = c_id;
  IF n = 0 THEN
    INSERT INTO relazione_funzione (fun_id_funzione, fun_id_funzione_figlia, cod_tipo_visualizzazione,
                                    label_funzione, ordine_visualizzazione)
    VALUES (38101000, c_id, 'BO', 'Pene sostitutive sospese senza ripresa', 16);
  END IF;

  FOR p IN (SELECT column_value AS prf FROM TABLE(sys.odcinumberlist(14, 31, 41, 51, 24, 32, 42, 52))) LOOP
    SELECT COUNT(*) INTO n FROM funzione_profilo
     WHERE fun_id_funzione = c_id AND prf_cod_profilo = p.prf;
    IF n = 0 THEN
      INSERT INTO funzione_profilo (data_inizio_validita, fun_id_funzione, prf_cod_profilo, flag_attivo,
        cod_operatore_inserimento, data_inserimento)
      VALUES (TO_DATE('2000-01-01', 'YYYY-MM-DD'), c_id, p.prf, 'S', '20261125-MEV_2026-124_SG', SYSDATE);
    END IF;
  END LOOP;
  COMMIT;
END;
/
