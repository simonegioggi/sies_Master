-- 20261023-MEV_2026-105_INLAY
-- MEV_2026-105 - Voce di menu "Riassegnazione massiva procedimenti"
-- Clona la funzione "Elenco Procedimenti Assegnati" (ActLoadRicercaProcedimentiAssegnati)
-- con le stesse relazioni di menu e gli stessi profili abilitati.
-- Verificare l'ambiente prima di eseguire: la funzione di riferimento deve esistere ed essere unica.
DECLARE
  v_old FUNZIONE.ID_FUNZIONE%TYPE;
  v_new FUNZIONE.ID_FUNZIONE%TYPE;
BEGIN
  SELECT ID_FUNZIONE INTO v_old
    FROM FUNZIONE
   WHERE AZIONE_CONTESTO_JAVA LIKE '%ActLoadRicercaProcedimentiAssegnati%'
     AND ROWNUM = 1;

  SELECT NVL(MAX(ID_FUNZIONE), 0) + 1 INTO v_new FROM FUNZIONE;

  INSERT INTO FUNZIONE (ID_FUNZIONE, DESCRIZIONE, COD_TIPO_FUNZIONE, AZIONE_CONTESTO_JAVA)
  SELECT v_new,
         'Riassegnazione massiva procedimenti',
         COD_TIPO_FUNZIONE,
         REPLACE(AZIONE_CONTESTO_JAVA, 'ActLoadRicercaProcedimentiAssegnati',
                 'ActLoadRiassegnazioneMassivaProcedimenti')
    FROM FUNZIONE
   WHERE ID_FUNZIONE = v_old;

  INSERT INTO RELAZIONE_FUNZIONE (FUN_ID_FUNZIONE, FUN_ID_FUNZIONE_FIGLIA, COD_TIPO_VISUALIZZAZIONE,
                                  LABEL_FUNZIONE, ORDINE_VISUALIZZAZIONE, IMMAGINE)
  SELECT FUN_ID_FUNZIONE, v_new, COD_TIPO_VISUALIZZAZIONE,
         'Riassegnazione massiva procedimenti', ORDINE_VISUALIZZAZIONE, IMMAGINE
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
