-- 20261124-MEV_2026-124_SG - 3.1.2 / 3.5.2 SIEP Annotazione Provvedimento e Revoca/Conversione Pena Sostitutiva
-- Nuove azioni SIEP (Esecuzione/Procura: profili 4, 30, 40, 50). Idempotente.
DECLARE
  n NUMBER;
  PROCEDURE ins(p_id NUMBER, p_descr VARCHAR2, p_azione VARCHAR2) IS
  BEGIN
    SELECT COUNT(*) INTO n FROM funzione WHERE id_funzione = p_id;
    IF n = 0 THEN
      INSERT INTO funzione (id_funzione, descrizione, cod_tipo_funzione, azione_contesto_java)
      VALUES (p_id, p_descr, 'R', p_azione);
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
  ins(25020400, 'Popup provvedimenti SIUS pene sostitutive', 'siap.siep.scambiosanzione.action.ActListaProvvedimentiPeneSostitutiveSius');
  ins(25020401, 'Load Annotazione Provvedimento Pena Sostitutiva', 'siap.siep.sanzionesostitutiva.action.ActLoadAnnotazioneProvvedimentoPS');
  ins(25020402, 'Inserisci Annotazione Provvedimento Pena Sostitutiva', 'siap.siep.sanzionesostitutiva.action.ActInserisciAnnotazioneProvvedimentoPS');
  ins(25020403, 'Dettaglio Annotazione Provvedimento Pena Sostitutiva', 'siap.siep.sanzionesostitutiva.action.ActDettaglioAnnotazioneProvvedimentoPS');
  ins(25020404, 'Load Annotazione Revoca/Conversione Pena Sostitutiva', 'siap.siep.sanzionesostitutiva.action.ActLoadAnnotazioneRevocaConversionePS');
  ins(25020405, 'Inserisci Annotazione Revoca/Conversione Pena Sostitutiva', 'siap.siep.sanzionesostitutiva.action.ActInserisciAnnotazioneRevocaConversionePS');
  ins(25020406, 'Dettaglio Annotazione Revoca/Conversione Pena Sostitutiva', 'siap.siep.sanzionesostitutiva.action.ActDettaglioAnnotazioneRevocaConversionePS');
  COMMIT;
END;
/
