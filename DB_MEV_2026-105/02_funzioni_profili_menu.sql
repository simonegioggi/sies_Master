-- 20261124-MEV_2026-105_SG
-- MEV_2026-105 (Legge 140/2026) - Funzioni, voci di menu e profili delle nuove classi Java (siesWeb)
-- Sostituisce i due vecchi script di menu (riassegnazione massiva e ricerca provvedimenti 94 ter).
-- Per ogni classe Act* creata dalla MEV: una riga in FUNZIONE e le righe in FUNZIONE_PROFILO
-- per i profili abilitati (v_profili). Le funzioni "Load" hanno anche la voce di menu (RELAZIONE_FUNZIONE),
-- clonata dalla funzione di riferimento (stesso menu padre).
-- Lo script e' idempotente: se la funzione (AZIONE_CONTESTO_JAVA), la relazione o il profilo esistono, vengono saltati.
--
--   Classe                                      Funzione di riferimento (menu padre)   Menu
--   ActLoadRiassegnazioneMassivaProcedimenti    ActLoadRicercaProcedimentiAssegnati    SI
--   ActRicercaRiassegnazioneMassivaProcedimenti -                                      NO
--   ActEseguiRiassegnazioneMassivaProcedimenti  -                                      NO
--   ActLoadRicercaProvvedimenti94Ter            ActLoadRicercaDecretiSospInDefinizione SI (ordine + 1)
--   ActRicercaProvvedimenti94Ter                -                                      NO
--   ActReportExcelProvvedimenti94Ter            -                                      NO
-- (ActProvvedimenti94TerBase e RiassegnazioneMassivaUtil non sono azioni: nessuna funzione.)
--
-- Profili abilitati: 4, 30, 40, 50 (gli stessi delle funzioni di riferimento). Modificare v_profili se necessario.
SET SERVEROUTPUT ON
DECLARE
  TYPE t_profili IS TABLE OF FUNZIONE_PROFILO.PRF_COD_PROFILO%TYPE;
  v_profili t_profili := t_profili(4, 30, 40, 50);

  PROCEDURE add_funzione(p_descr     VARCHAR2,
                         p_classe    VARCHAR2,
                         p_rif       VARCHAR2,   -- classe della funzione di riferimento per il menu (NULL = nessuna voce di menu)
                         p_label     VARCHAR2,
                         p_ord_delta NUMBER) IS
    v_id  FUNZIONE.ID_FUNZIONE%TYPE;
    v_rif FUNZIONE.ID_FUNZIONE%TYPE;
  BEGIN
    BEGIN
      SELECT ID_FUNZIONE INTO v_id FROM FUNZIONE
       WHERE AZIONE_CONTESTO_JAVA = p_classe AND ROWNUM = 1;
    EXCEPTION WHEN NO_DATA_FOUND THEN
      SELECT NVL(MAX(ID_FUNZIONE), 0) + 1 INTO v_id FROM FUNZIONE;
      INSERT INTO FUNZIONE (ID_FUNZIONE, DESCRIZIONE, COD_TIPO_FUNZIONE, AZIONE_CONTESTO_JAVA)
      VALUES (v_id, p_descr, 'R', p_classe);
      DBMS_OUTPUT.PUT_LINE('FUNZIONE ' || v_id || ' creata: ' || p_classe);
    END;

    IF p_rif IS NOT NULL THEN
      SELECT ID_FUNZIONE INTO v_rif FROM FUNZIONE
       WHERE AZIONE_CONTESTO_JAVA LIKE '%.' || p_rif AND ROWNUM = 1;
      INSERT INTO RELAZIONE_FUNZIONE (FUN_ID_FUNZIONE, FUN_ID_FUNZIONE_FIGLIA, COD_TIPO_VISUALIZZAZIONE,
                                      LABEL_FUNZIONE, ORDINE_VISUALIZZAZIONE, IMMAGINE)
      SELECT r.FUN_ID_FUNZIONE, v_id, r.COD_TIPO_VISUALIZZAZIONE,
             p_label, r.ORDINE_VISUALIZZAZIONE + p_ord_delta, r.IMMAGINE
        FROM RELAZIONE_FUNZIONE r
       WHERE r.FUN_ID_FUNZIONE_FIGLIA = v_rif
         AND NOT EXISTS (SELECT 1 FROM RELAZIONE_FUNZIONE x WHERE x.FUN_ID_FUNZIONE_FIGLIA = v_id);
    END IF;

    FOR i IN 1 .. v_profili.COUNT LOOP
      INSERT INTO FUNZIONE_PROFILO (DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, FUN_ID_FUNZIONE, PRF_COD_PROFILO,
                                    COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, FLAG_ATTIVO)
      SELECT DATE '2000-01-01', NULL, v_id, v_profili(i), '20261124-MEV_2026-105_SG', SYSDATE, 'S'
        FROM DUAL
       WHERE NOT EXISTS (SELECT 1 FROM FUNZIONE_PROFILO
                          WHERE FUN_ID_FUNZIONE = v_id AND PRF_COD_PROFILO = v_profili(i));
    END LOOP;
  END;
BEGIN
  -- Voce errata inserita a mano in passato (classe ActLoadRiassegnazioneMassiva mai esistita)
  DELETE FROM FUNZIONE_PROFILO WHERE FUN_ID_FUNZIONE IN
    (SELECT ID_FUNZIONE FROM FUNZIONE WHERE AZIONE_CONTESTO_JAVA = 'siap.sico.magistratocompetente.action.ActLoadRiassegnazioneMassiva');
  DELETE FROM RELAZIONE_FUNZIONE WHERE FUN_ID_FUNZIONE_FIGLIA IN
    (SELECT ID_FUNZIONE FROM FUNZIONE WHERE AZIONE_CONTESTO_JAVA = 'siap.sico.magistratocompetente.action.ActLoadRiassegnazioneMassiva');
  DELETE FROM FUNZIONE WHERE AZIONE_CONTESTO_JAVA = 'siap.sico.magistratocompetente.action.ActLoadRiassegnazioneMassiva';

  -- 3.8 Riassegnazione massiva procedimenti (menu Gestione Magistrato)
  add_funzione('Load Riassegnazione Massiva Procedimenti',
               'siap.sico.magistratocompetente.action.ActLoadRiassegnazioneMassivaProcedimenti',
               'ActLoadRicercaProcedimentiAssegnati', 'Riassegnazione massiva procedimenti', 0);
  add_funzione('Ricerca Riassegnazione Massiva Procedimenti',
               'siap.sico.magistratocompetente.action.ActRicercaRiassegnazioneMassivaProcedimenti', NULL, NULL, 0);
  add_funzione('Esegui Riassegnazione Massiva Procedimenti',
               'siap.sico.magistratocompetente.action.ActEseguiRiassegnazioneMassivaProcedimenti', NULL, NULL, 0);

  -- 3.5 Ricerca Provvedimenti emessi ex art. 94 Ter DPR 309/90 (menu Sospensione Esecuzione ex art. 656 c.p.p.)
  add_funzione('Ricerca Provvedimenti emessi ex art. 94 Ter DPR 309/90',
               'siap.siep.scadenzario.action.ActLoadRicercaProvvedimenti94Ter',
               'ActLoadRicercaDecretiSospInDefinizione', 'Ricerca Provvedimenti emessi ex art. 94 Ter DPR 309/90', 1);
  add_funzione('Ricerca Provvedimenti emessi ex art. 94 Ter DPR 309/90',
               'siap.siep.scadenzario.action.ActRicercaProvvedimenti94Ter', NULL, NULL, 0);
  add_funzione('Export in Excel della Ricerca Provvedimenti emessi ex art. 94 Ter DPR 309/90',
               'siap.siep.scadenzario.action.ActReportExcelProvvedimenti94Ter', NULL, NULL, 0);

  COMMIT;
END;
/
