-- 20261028-MEV_2026-105_INLAY
-- MEV_2026-105 (Legge 140/2026) - 3.5 Voce di menu "Ricerca Provvedimenti emessi ex art. 94 Ter DPR 309/90"
-- Menu: Ordini di Esecuzione/Scarcerazione > Sospensione Esecuzione ex art. 656 c.p.p.
-- Clona la funzione "Ricerca Decreti in corso di Definizione" (ActLoadRicercaDecretiSospInDefinizione):
-- stesso menu padre e stessi profili abilitati. La funzione di riferimento deve esistere ed essere unica.
DECLARE
  v_old FUNZIONE.ID_FUNZIONE%TYPE;
  v_new FUNZIONE.ID_FUNZIONE%TYPE;
BEGIN
  SELECT ID_FUNZIONE INTO v_old
    FROM FUNZIONE
   WHERE AZIONE_CONTESTO_JAVA LIKE '%ActLoadRicercaDecretiSospInDefinizione%'
     AND ROWNUM = 1;

  SELECT NVL(MAX(ID_FUNZIONE), 0) + 1 INTO v_new FROM FUNZIONE;

  INSERT INTO FUNZIONE (ID_FUNZIONE, DESCRIZIONE, COD_TIPO_FUNZIONE, AZIONE_CONTESTO_JAVA)
  SELECT v_new,
         'Ricerca Provvedimenti emessi ex art. 94 Ter DPR 309/90',
         COD_TIPO_FUNZIONE,
         REPLACE(AZIONE_CONTESTO_JAVA, 'ActLoadRicercaDecretiSospInDefinizione',
                 'ActLoadRicercaProvvedimenti94Ter')
    FROM FUNZIONE
   WHERE ID_FUNZIONE = v_old;

  INSERT INTO RELAZIONE_FUNZIONE (FUN_ID_FUNZIONE, FUN_ID_FUNZIONE_FIGLIA, COD_TIPO_VISUALIZZAZIONE,
                                  LABEL_FUNZIONE, ORDINE_VISUALIZZAZIONE, IMMAGINE)
  SELECT FUN_ID_FUNZIONE, v_new, COD_TIPO_VISUALIZZAZIONE,
         'Ricerca Provvedimenti emessi ex art. 94 Ter DPR 309/90', ORDINE_VISUALIZZAZIONE + 1, IMMAGINE
    FROM RELAZIONE_FUNZIONE
   WHERE FUN_ID_FUNZIONE_FIGLIA = v_old;

  INSERT INTO FUNZIONE_PROFILO (DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, FUN_ID_FUNZIONE, PRF_COD_PROFILO,
                                COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, FLAG_ATTIVO)
  SELECT DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, v_new, PRF_COD_PROFILO,
         COD_OPERATORE_INSERIMENTO, SYSDATE, FLAG_ATTIVO
    FROM FUNZIONE_PROFILO
   WHERE FUN_ID_FUNZIONE = v_old;

  COMMIT;
END;
/
