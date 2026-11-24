-- Backup sorgente originale package body ISPETTORATO (DB sviluppo), prima di 20261117-MEV_2026-105_SG
-- (caratteri accentati nei commenti non preservati dalla lettura da USER_SOURCE)
CREATE OR REPLACE PACKAGE BODY        Ispettorato IS

  eseguo_insert             EXCEPTION;
  salta_record              EXCEPTION;
  registra_scarto           EXCEPTION;
  close_cur                 EXCEPTION;
  duplico_pena_residua      EXCEPTION;
  caso_non_sviluppato       EXCEPTION;
  gestione_MA               EXCEPTION;
  gestione_lib_condizionale EXCEPTION;
  v_cod_tipo_evento        EVENTO.cod_tipo_evento%TYPE := '';
  v_cod_tipo_provvedimento EVENTO.cod_tipo_provvedimento%TYPE := '';
  v_cod_motivo             EVENTO.cod_motivo%TYPE := '';
  --v_desc_motivo              CG_REF_CODES.rv_meaning%TYPE;
  --v_conta                    NUMBER;
  --v_progressivo              NUMBER;
  --v_progressivo_nota         NUMBER;
  --v_progressivo_pg           NUMBER := NULL;
  --v_progressivo_pen          NUMBER;
  --v_progressivo_ann          NUMBER;
  --v_progressivo_lib          NUMBER;
  --v_progressivo_fun          NUMBER;
  --v_progressivo_sos          NUMBER;
  --v_progressivo_ist          NUMBER;
  --v_progressivo_not          NUMBER;
  --v_progressivo_ten          NUMBER;
  --v_progressivo_dep_ord      NUMBER;
  --v_progressivo_dec_ord      NUMBER;
  --v_progressivo_dep_dec      NUMBER;
  --v_progressivo_mis_alt      NUMBER;
  --v_progressivo_cum          NUMBER;
  --v_progressivo_pen_cum      NUMBER;
  --v_progressivo_ult_san      NUMBER;
  --v_cod_tipo_evento_1        EVENTO.cod_tipo_evento%TYPE := '';
  v_cod_tipo_provvedimento_1 EVENTO.cod_tipo_provvedimento%TYPE := '';
  v_cod_motivo_1             EVENTO.cod_motivo%TYPE := '';
  --v_cod_tipo_provvedimento_2 EVENTO.cod_tipo_provvedimento%TYPE := '';
  --v_cod_motivo_2             EVENTO.cod_motivo%TYPE := '';
  --v_cod_tipo_provvedimento_3 EVENTO.cod_tipo_provvedimento%TYPE := '';
  --v_cod_motivo_3             EVENTO.cod_motivo%TYPE := '';
  --v_cod_tipo_provvedimento_4 EVENTO.cod_tipo_provvedimento%TYPE := '';
  --v_cod_motivo_4             EVENTO.cod_motivo%TYPE := '';
  --v_cod_tipo_provvedimento_5 EVENTO.cod_tipo_provvedimento%TYPE := '';
  --v_cod_motivo_5             EVENTO.cod_motivo%TYPE := '';
  --v_data_appo                VARCHAR2(30);
  --v_data_appo1               VARCHAR2(30);
  v_data_inserimento DATE;
  --v_data_verifica            DATE;
  --glb_tipo_provv             tbl_tipo_provv := tbl_tipo_provv();
  --glb_motivo                 tbl_motivo := tbl_motivo();
  --glb_ufficio_emit           tbl_ufficio_emit := tbl_ufficio_emit();
  --glb_comune_emit            tbl_comune_emit := tbl_comune_emit();
  --glb_data_emis              tbl_data_emis := tbl_data_emis();
  --glb_data_tras              tbl_data_tras := tbl_data_tras();

  --v_cod_stat_fas VARCHAR2(4) := '';
  --v_note         VARCHAR2(2000);
  --glb_tipo_sanz  tbl_tipo_sanz := tbl_tipo_sanz();
  --glb_anni       tbl_anni := tbl_anni();
  --glb_mesi       tbl_mesi := tbl_mesi();
  --glb_giorni     tbl_giorni := tbl_giorni();

  -----------------------------------------------------------------------------------------------------------------------
  ------------------------------------PROCEDURA CARICA_PROVVEDIMENTI-------------------------------------------------------
  -----------------------------------------------------------------------------------------------------------------------

  PROCEDURE stat_provvedimenti
  --  Umberto  8/10/2013 (par_ufficio        IN  VARCHAR2,
    --- Umberto 8/10/2013          aggiunti 3 parametri ufficio per NGG
  (par_ufficio       IN VARCHAR2,
   par_ufficio1      IN VARCHAR2,
   par_ufficio2      IN VARCHAR2,
   par_ufficio3      IN VARCHAR2,
   par_data_verifica IN VARCHAR2
   --   , par_id_fascicolo_test IN NUMBER
   ) IS

    --DEFINIZIONE VARIABILI
    --v_note1 VARCHAR2(2000);

    --v_tipo_richiesta    CAMPO_NOTA.oggetto_nota_res%TYPE;
    v_id_fascicolo_siep FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;
    --v_anno_af8_nrgem    EVENTO.anno_protocollo%TYPE;
    --v_progr_af8_nrgem   EVENTO.progr_protocollo%TYPE;
    v_chiave_anno  FASCICOLO_SIEP.chiave_anno%TYPE;
    v_chiave_progr FASCICOLO_SIEP.chiave_progr%TYPE;
    --- Umberto 8/10/2013          aggiunti 2 variabili per NGG
    v_cod_ufficio_inserimento FASCICOLO_SIEP.cod_ufficio_inserimento%TYPE;
    v_chiave_progr_orig       FASCICOLO_SIEP.chiave_progr_orig%TYPE;
    -- Umberto 8/10/2013   Fine modifiche
    v_cod_posizione_giuridica POSIZIONE_GIURIDICA.cod_posizione_giuridica%TYPE;
    --v_con_rev                 VARCHAR2(30);
    --v_tip_aut                 VARCHAR2(70);
    --v_tip_att                 VARCHAR2(80);
    --v_motiv                   VARCHAR2(50);
    v_af8_nres FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;

    v_cod_stato_procedimento STATO_PROCEDIMENTO.cod_stato_procedimento%TYPE;
    --flag_no_riepilogo           CHAR(1);
    --tot_recl                    NUMBER;
    --tot_arre                    NUMBER;
    v_num_anni_reclusione   NUMBER(4);
    v_num_mesi_reclusione   NUMBER(4);
    v_num_giorni_reclusione NUMBER(4);
    v_importo_multa         NUMBER(11, 2);
    v_num_anni_arresto      NUMBER(4);
    v_num_mesi_arresto      NUMBER(4);
    v_num_giorni_arresto    NUMBER(4);
    --v_importo_ammenda           NUMBER(11, 2);
    --gg_tot                      NUMBER;
    --tot_pres                    NUMBER;
    v_data_inizio    PENA_RESIDUA.data_inizio%TYPE;
    v_data_fine      PENA_RESIDUA.data_fine%TYPE;
    v_flag_ergastolo PENA_RESIDUA.flag_ergastolo%TYPE;
    --v_id_fascicolo_siep_old     FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;
    --v_id_evento_old             FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;
    v_cod_stato_fascicolo       FASCICOLO_SIEP.cod_stato_fascicolo%TYPE;
    v_cod_stato_fascicolo_res   STATO_FASCICOLO_RES.cod_stato_fascicolo%TYPE := '';
    v_num_anni_reclusione_sen   NUMBER(4);
    v_num_mesi_reclusione_sen   NUMBER(4);
    v_num_giorni_reclusione_sen NUMBER(4);
    --v_importo_multa_sen         NUMBER(11, 2);
    v_num_anni_arresto_sen   NUMBER(4);
    v_num_mesi_arresto_sen   NUMBER(4);
    v_num_giorni_arresto_sen NUMBER(4);
    --v_importo_ammenda_sen       NUMBER(11, 2);

    -- MEV42 07/2017
    v_Id_Istruttoria_Cumulo ISTRUTTORIA_CUMULO.ID_ISTRUTTORIA_CUMULO%TYPE;

    -- variabile generali
    --conta_commit NUMBER;

    --v_trovato NUMBER;

    --num_record_scritti NUMBER := 0;
    num_record_letti NUMBER := 0;
    --num_record_scartati       NUMBER:= 0;
    --v_anno_res NUMBER(4) := NULL;
    --v_num_res            VARCHAR2(6) := '';
    v_quantum            VARCHAR2(1) := '';
    v_quantum_pena_compl VARCHAR2(1) := '';
    salta_record EXCEPTION;
    v_uscita VARCHAR2(1) := 'N';
    --v_conta_provv  NUMBER := 0;
    --v_conta_scarti NUMBER := 0;

    v_data_inserimento DATE;

    v_fasc_archiviato VARCHAR2(1) := 'N';
    esiste_altri      NUMBER;
    --- Umberto 8/10/2013          aggiunta 1 variabile per NGG
    v_desc_ufficio_inserimento VARCHAR2(200);

    -- dichiarazione del cursore

    --bvn 11 04 2016
    v_altra_causa_pos_giud altra_causa.cod_tipo_pos_giuridica%TYPE;
    --fine bvn 11 04 2016
    CURSOR cur1 IS
      SELECT id_fascicolo_siep,
             chiave_anno,
             chiave_progr,
             -- Umberto 8/10/2013 inserita estrazione colonne aggiuntive
             a.cod_ufficio_inserimento,
             a.chiave_progr_orig,
             -- Umberto  8/10/2013  Fine modifiche
             a.fas_sie_id_fascicolo_siep nres,
             a.cod_stato_fascicolo
        FROM FASCICOLO_SIEP         a,
             STATO_PROCEDIMENTO     b,
             MAX_STATO_PROCEDIMENTO c
      -- Umberto 8/10/2013            WHERE a.cod_ufficio_inserimento    =  par_ufficio
      -- Umberto 8/10/2013  Aggiunti 3 parametri ufficio
       WHERE (a.cod_ufficio_inserimento = par_ufficio OR
             a.cod_ufficio_inserimento = par_ufficio1 OR
             a.cod_ufficio_inserimento = par_ufficio2 OR
             a.cod_ufficio_inserimento = par_ufficio3)
            -- Ticket#20231017016 - STATISTICHE UFFICIO - TEMPI ISCRIZIONI PROCEDIMENTI
            -- Vanno estratti SOLO i classe I
         AND (a.chiave_progr BETWEEN 1 AND 19999 OR
             a.chiave_progr BETWEEN 10000000000001 AND 10000000019999 OR
             a.chiave_progr BETWEEN 20000000000001 AND 20000000019999 OR
             a.chiave_progr BETWEEN 30000000000001 AND 30000000019999 OR
             a.chiave_progr BETWEEN 800000 AND 819999 -- Pretura
             OR a.chiave_progr BETWEEN 900001000000 AND 919999000000 -- Pretura/RES con lettere classe I
             OR a.chiave_progr BETWEEN 1700001000000 AND 1719999000000 -- Pretura/RES con lettere classe I
             )
            /*
               -- Umberto 8/10/2013  Fine
            AND a.chiave_progr NOT BETWEEN 930000000000 AND 939999999999 ---escludo le pene sospese
            AND a.chiave_progr NOT BETWEEN 30000 AND 39999
               -- Umberto 26/9/2013       esclusione pene sospese uffici accorpati
            AND a.chiave_progr NOT BETWEEN 10000000030000 AND 10000000039999
            AND a.chiave_progr NOT BETWEEN 20000000030000 AND 20000000039999
            AND a.chiave_progr NOT BETWEEN 30000000030000 AND 30000000039999
               -- Umberto 26/9/2013  Fine modifiche
               -- Ticket#20220311014 Umberto 11/03/2022  Esclusione Procedimenti Pene Pecuniarie (classe 20000)
            AND a.chiave_progr NOT BETWEEN 20000 AND 29999
            AND a.chiave_progr NOT BETWEEN 10000000020000 AND 10000000029999
            AND a.chiave_progr NOT BETWEEN 20000000020000 AND 20000000029999
            AND a.chiave_progr NOT BETWEEN 30000000020000 AND 30000000029999
               -- Umberto 11/03/2022  Fine Ticket#20220311014
               -- Umberto 24/03/2016  Esclusione Procedimenti di Misure Sicurezza
            AND a.chiave_progr NOT BETWEEN 40000 AND 49999
            AND a.chiave_progr NOT BETWEEN 10000000040000 AND 10000000049999
            AND a.chiave_progr NOT BETWEEN 20000000040000 AND 20000000049999
            AND a.chiave_progr NOT BETWEEN 30000000040000 AND 30000000049999
               -- Umberto 24/03/2016  Fine
               -- Umberto 24/03/2016  Esclusione Procedimenti di Conversione Pene Pecuniarie
            AND a.chiave_progr NOT BETWEEN 70000 AND 79999
            AND a.chiave_progr NOT BETWEEN 10000000070000 AND 10000000079999
            AND a.chiave_progr NOT BETWEEN 20000000070000 AND 20000000079999
            AND a.chiave_progr NOT BETWEEN 30000000070000 AND 30000000079999
               -- Umberto 24/03/2016  Fine
            AND a.chiave_progr NOT BETWEEN 700000 AND 799999 ---escludo cumulo aggiunto il 09/01/2008
               -- Umberto 26/9/2013       esclusione fascicoli cinvolti in cumulo uffici accorpati
            AND a.chiave_progr NOT BETWEEN 10000000700000 AND 10000000799999
            AND a.chiave_progr NOT BETWEEN 20000000700000 AND 20000000799999
            AND a.chiave_progr NOT BETWEEN 30000000700000 AND 30000000799999
               -- Umberto 26/9/2013  Fine modifiche
               -- Daniela 22.04.2010 : Aggiunta su segnalazione di Umberto !!!
               -- scarto A seguito della gestione della Nuova Istanza
            AND a.chiave_progr NOT BETWEEN 90000 AND 99999
               -- Daniela 22.04.2010 : Fine
               -- Umberto 26/9/2013       esclusione fascicoli cinvolti in cumulo uffici accorpati
            AND a.chiave_progr NOT BETWEEN 10000000090000 AND 10000000099999
            AND a.chiave_progr NOT BETWEEN 20000000090000 AND 20000000099999
            AND a.chiave_progr NOT BETWEEN 30000000090000 AND 30000000099999
            */
            -- Ticket#20231017016 - FINE
         AND a.id_fascicolo_siep = b.fas_sie_id_fascicolo_siep
         AND b.fas_sie_id_fascicolo_siep = c.fas_sie_id_fascicolo_siep(+)
         AND b.progressivo = c.sta_pro_progressivo
      --SOLO PER TEST!!!!!!!!!! Attivare le seguenti condizioni per limitare
      -- il numero di fascicoli estratti in caso di test puntuali
      -- AND a.CHIAVE_ANNO = 2022
      -- !!!!!!!!!
       -- AND a.ID_FASCICOLO_SIEP = 276352012026 -- SOLO PER TEST!!!!!!!!!!
      -- FINE TEST
       ORDER BY id_fascicolo_siep;

    CURSOR cur_evento(par_id_fascicolo_siep IN NUMBER) IS
      SELECT cod_tipo_evento,
             cod_tipo_provvedimento,
             cod_motivo,
             -- Ticket#20230914017 - la data inserimento va presa piena (hh:mm:ss)
             -- altrimenti gli eventi inseriti lo stesso giorno dell'ultimo non
             -- vengono estratti dalla select del penultimo evento
             -- TO_DATE(TO_CHAR(data_inserimento, 'dd/mm/yyyy'), 'dd/mm/yyyy')
             data_inserimento
      -- Ticket#20230914017 - FINE
        FROM EVENTO
       WHERE fas_sie_id_fascicolo_siep = par_id_fascicolo_siep
            --   Umberto 8/10/2013     AND cod_ufficio_inserimento     = par_ufficio
            --   Umberto 8/10/2013  Aggiunti 3 parametri ufficio
         AND (cod_ufficio_inserimento = par_ufficio OR
             cod_ufficio_inserimento = par_ufficio1 OR
             cod_ufficio_inserimento = par_ufficio2 OR
             cod_ufficio_inserimento = par_ufficio3)
            --   Umberto 8/10/2013  Fine
         AND flag_documento_registrato = 'S' ---modificato il 20/02/2007
         AND fas_siu_id_fascicolo_sius IS NULL
            -- Test Ticket#20220512014 - bisogne includere il decreto di irreperibilit?
            -- AND cod_tipo_provvedimento NOT IN ('02', '03', '-') --decreti ordinanza ISTRUTTORIA
         AND (cod_motivo in ('0282') -- aggiungere eventuali altri decreti o ordinanze che si vogliono includere
             OR cod_tipo_provvedimento NOT IN ('02', '03', '-'))
            -- FINE Test Ticket#20220512014
            /* Ticket#202202280122 - La condizione di esclusione della comunicazione provv cumulo era errata
            * Escludeva tutte le comunicazioni.*/
            --AND (cod_tipo_provvedimento NOT IN ('12') and
            --cod_motivo not in ('0670')) -- comunicazioni provv. cumulo */
         AND NOT (cod_tipo_provvedimento = '12' AND cod_motivo = '0670')
      -- Ticket#202202280122 - FINE
       ORDER BY data_inserimento DESC, id_evento DESC;

    --- inizio procedura
  BEGIN
    --DBMS_OUTPUT.ENABLE (buffer_size => NULL);

    DBMS_OUTPUT.PUT_LINE('===================================================');
    DBMS_OUTPUT.PUT_LINE('Inizio Procedura: stat_provvedimenti ');
    DBMS_OUTPUT.PUT_LINE('par_ufficio  =  ' || par_ufficio);
    DBMS_OUTPUT.PUT_LINE('par_ufficio1 =  ' || par_ufficio1);
    DBMS_OUTPUT.PUT_LINE('par_ufficio2 =  ' || par_ufficio2);
    DBMS_OUTPUT.PUT_LINE('par_ufficio3 =  ' || par_ufficio3);
    DBMS_OUTPUT.PUT_LINE('par_data_verifica =  ' || par_data_verifica);
    DBMS_OUTPUT.PUT_LINE('===================================================');
    /*
    IF (par_id_fascicolo_test IS NOT NULL) THEN
      DBMS_OUTPUT.PUT_LINE('SONO IN FASE DI TEST PER IL FASCICOLO CON ID '||par_id_fascicolo_test);
    END IF;
    */
    --conta_commit := 1;

    --- CANCELLO tabelle di appoggio
    -- 30 Ottobre 2008 - Sancamillo : Aggiunto ufficio in where condition
    -- 26.01.2009 - Ripristino - Sancamillo - Inizio
    -- DELETE FROM STATIS.ISP_PROVVEDIMENTI;
    -- DELETE FROM STATIS.ISP_SCARTI;
    DELETE FROM STATIS.ISP_PROVVEDIMENTI; -- WHERE COD_UFFICIO = par_ufficio;
    DELETE FROM STATIS.ISP_SCARTI; -- WHERE COD_UFFICIO = par_ufficio;
    -- 26.01.2009 - Ripristino - Sancamillo - Fine
    -- 30 Ottobre 2008 - Sancamillo : Fine
    COMMIT;

    -- New d.f. 05/11/2014 Aggiungo subito alla tabella ISP_PROVVEDIMENTI i
    -- record relativi ai titoli1 e 2 per la creazione del foglio excel in
    -- fase di estrazione.
    Insert into ISP_PROVVEDIMENTI
      (COD_STATO_FASCICOLO_RES)
      select COD_STATO_FASCICOLO
        from STATO_FASCICOLO_RES
       WHERE TIPO_CAMPO in ('T1', 'T2');

    /* Sancamillo genaio 2009 : Eliminata truncate per limite ad un solo utente per distretto
    BEGIN
      SELECT COUNT(*)
        INTO v_conta_provv
        FROM STATIS.ISP_PROVVEDIMENTI;

      IF v_conta_provv > 0 THEN
         EXECUTE  IMMEDIATE 'TRUNCATE TABLE STATIS.ISP_PROVVEDIMENTI';
      END IF;
    END;

    BEGIN
      SELECT COUNT(*)
        INTO v_conta_scarti
        FROM STATIS.ISP_SCARTI;

      IF v_conta_scarti > 0 THEN
         EXECUTE  IMMEDIATE 'TRUNCATE TABLE STATIS.ISP_SCARTI';
      END IF;
    END;
    Fine Sancamillo gennaio 2009 */

    -- APERTURA CURSORE
    OPEN cur1;

    -- LETTURA FUORI CICLO DEI DATI DEL FASCICOLO
    FETCH cur1
      INTO v_id_fascicolo_siep,
           v_chiave_anno,
           v_chiave_progr,
           -- Umberto 8/10/2013  inserita estrazione colonne aggiuntive
           v_cod_ufficio_inserimento,
           v_chiave_progr_orig,
           -- Umberto 8/10/2013  Fine modifiche
           v_af8_nres,
           v_cod_stato_fascicolo;

    DBMS_OUTPUT.PUT_LINE('fetch fuori ciclo ');

    -- CALCOLO DELL'ORA DI CARICAMENTO
    v_data_inserimento := SYSDATE;
    -- v_data_verifica := SYSDATE;

    -- RICERCA DEL CODICE COMUNE DI APPARTENENZA DELL'UFFICIO
    BEGIN
      SELECT cod_comune
        INTO APPO_VAR.cod_comune
        FROM UFFICIO
       WHERE cod_ufficio = par_ufficio;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        APPO_VAR.cod_comune := '-';
    END;

    -- Umberto 8/10/2013  inserita routine recupero descrizione ufficio inserimento
    ----RICERCA LUOGO UFFICIO INSERIMENTO
    BEGIN
      SELECT descrizione
        INTO v_desc_ufficio_inserimento
        FROM COMUNE
       WHERE cod_comune = SUBSTR(v_cod_ufficio_inserimento, 1, 6);
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_desc_ufficio_inserimento := '-';
    END;

    -- INIZIO CICLO DI ESTRAZIONE DATI PER OGNI RECORD RILASCIATO DALLA FETCH
    WHILE cur1%FOUND LOOP
      BEGIN
        --- inizio loop
        /*
        IF (par_id_fascicolo_test IS NOT NULL) THEN
          IF (par_id_fascicolo_test = v_id_fascicolo_siep) THEN
              DBMS_OUTPUT.PUT_LINE('FASCICOLO DI TEST TROVATO ('||par_id_fascicolo_test||'). PROCEDO...');
            ELSE
                -- DBMS_OUTPUT.PUT_LINE('Salto il fascicolo con ID = '||v_id_fascicolo_siep);
                GOTO skip_fascicolo;
              -- CONTINUE; -- Salto il fascicolo in quanto non coincide con quello di test in input
          END IF;
        END IF;
        */
        DBMS_OUTPUT.PUT_LINE('                            ');
        DBMS_OUTPUT.PUT_LINE('INIZIO DEL LOOP - FASCICOLI ');

        num_record_letti          := num_record_letti + 1;
        v_data_inizio             := '';
        v_data_fine               := '';
        v_quantum                 := NULL;
        v_cod_stato_fascicolo_res := NULL;
        v_num_anni_reclusione     := NULL;
        v_num_mesi_reclusione     := NULL;
        v_num_giorni_reclusione   := NULL;
        v_importo_multa           := NULL;
        v_num_anni_arresto        := NULL;
        v_num_mesi_arresto        := NULL;
        v_num_giorni_arresto      := NULL;
        v_cod_motivo              := NULL;
        v_cod_stato_procedimento  := NULL;
        v_quantum_pena_compl      := NULL;
        v_flag_ergastolo          := NULL;
        v_fasc_archiviato         := 'N';

        DBMS_OUTPUT.PUT_LINE('v_id_fascicolo_siep ' || v_id_fascicolo_siep);
        DBMS_OUTPUT.PUT_LINE('v_chiave_anno  ' || v_chiave_anno);
        DBMS_OUTPUT.PUT_LINE('v_chiave_progr ' || v_chiave_progr);

        -- RICERCA DELLA POSIZIONE GIURIDICA
        BEGIN
          /*     Umberto 13/1/2009 - Commentata la seguente select su posizione giuridica per eccessiva lentezza
                   (per la PM Roma la procedura impiegava 1h e 30m, ma nel frattempo scadeva la sessione)
                   sostituita con query semplificata che ottiene gli stessi risultati
            SELECT cod_posizione_giuridica
              INTO v_cod_posizione_giuridica
              FROM POSIZIONE_GIURIDICA,(SELECT MAX(data_inserimento) max_data
                                          FROM POSIZIONE_GIURIDICA
                                         WHERE fas_sie_id_fascicolo_siep=v_id_fascicolo_siep
                                           AND cod_ufficio_inserimento=par_ufficio) pos_giu
             WHERE fas_sie_id_fascicolo_siep=v_id_fascicolo_siep
               AND cod_ufficio_inserimento=par_ufficio
            -- AND data_fine is null
               AND data_inserimento=pos_giu.max_data
               AND id_posizione_giuridica=(SELECT MAX(id_posizione_giuridica)
                                             FROM POSIZIONE_GIURIDICA
                                            WHERE fas_sie_id_fascicolo_siep=v_id_fascicolo_siep
                                              AND cod_ufficio_inserimento=par_ufficio
                                              AND data_inserimento=pos_giu.max_data);
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
                 v_cod_posizione_giuridica:='-';
               Fine commento vecchia query */
          ---       Inizio nuova query
          SELECT cod_posizione_giuridica
            INTO v_cod_posizione_giuridica
            FROM POSIZIONE_GIURIDICA
           WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                -- Umberto 8/10/2013           AND cod_ufficio_inserimento=par_ufficio
                --   Umberto 8/10/2013  Aggiunti 3 parametri ufficio
             AND (cod_ufficio_inserimento = par_ufficio OR
                 cod_ufficio_inserimento = par_ufficio1 OR
                 cod_ufficio_inserimento = par_ufficio2 OR
                 cod_ufficio_inserimento = par_ufficio3)
                --   Umberto 8/10/2013  Fine
             AND id_posizione_giuridica =
                 (SELECT MAX(id_posizione_giuridica)
                    FROM POSIZIONE_GIURIDICA
                   WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep);
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_cod_posizione_giuridica := '-';
            --       Umberto 13/1/2009 - Fine intervento
        END;

        DBMS_OUTPUT.PUT_LINE('v_cod_posizione_giuridica ' ||
                             v_cod_posizione_giuridica);

        ----RICERCA PENA RESIDUA
        BEGIN
          SELECT data_inizio,
                 data_fine,
                 num_anni_reclusione,
                 num_mesi_reclusione,
                 num_giorni_reclusione,
                 num_anni_arresto,
                 num_mesi_arresto,
                 num_giorni_arresto,
                 flag_ergastolo
            INTO v_data_inizio,
                 v_data_fine,
                 v_num_anni_reclusione,
                 v_num_mesi_reclusione,
                 v_num_giorni_reclusione,
                 v_num_anni_arresto,
                 v_num_mesi_arresto,
                 v_num_giorni_arresto,
                 v_flag_ergastolo
            FROM PENA_RESIDUA,
                 (SELECT MAX(data_inserimento) max_data
                    FROM PENA_RESIDUA
                   WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                     AND cod_ufficio_inserimento = par_ufficio) pena_res
           WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                --  Umberto  8/10/2013           AND cod_ufficio_inserimento=par_ufficio
                --   Umberto 8/10/2013  Aggiunti 3 parametri ufficio
             AND (cod_ufficio_inserimento = par_ufficio OR
                 cod_ufficio_inserimento = par_ufficio1 OR
                 cod_ufficio_inserimento = par_ufficio2 OR
                 cod_ufficio_inserimento = par_ufficio3)
                --   Umberto 8/10/2013  Fine
             AND data_inserimento = pena_res.max_data
             AND id_pena_residua =
                 (SELECT MAX(id_pena_residua)
                    FROM PENA_RESIDUA
                   WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                        -- Umberto 7/10/2013                                   AND cod_ufficio_inserimento=par_ufficio
                     AND data_inserimento = pena_res.max_data);

        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_data_inizio := NULL;
            v_data_fine   := NULL;
        END;

        DBMS_OUTPUT.PUT_LINE('Recupero pena residua.... ');
        DBMS_OUTPUT.PUT_LINE('v_data_inizio    >' || v_data_inizio);
        DBMS_OUTPUT.PUT_LINE('v_data_fine      >' || v_data_fine);
        DBMS_OUTPUT.PUT_LINE('            ');
        DBMS_OUTPUT.PUT_LINE('v_num_anni_reclusione   >' ||
                             v_num_anni_reclusione);
        DBMS_OUTPUT.PUT_LINE('v_num_mesi_reclusione   >' ||
                             v_num_mesi_reclusione);
        DBMS_OUTPUT.PUT_LINE('v_num_giorni_reclusione >' ||
                             v_num_giorni_reclusione);
        DBMS_OUTPUT.PUT_LINE('v_num_anni_arresto      >' ||
                             v_num_anni_arresto);
        DBMS_OUTPUT.PUT_LINE('v_num_mesi_arresto      >' ||
                             v_num_mesi_arresto);
        DBMS_OUTPUT.PUT_LINE('v_num_giorni_arresto    >' ||
                             v_num_giorni_arresto);
        DBMS_OUTPUT.PUT_LINE('            ');
        DBMS_OUTPUT.PUT_LINE('v_flag_ergastolo >' || v_flag_ergastolo);

        -- Sancamillo - Luglio 2009 : Testa
        -- Inizio
        IF NVL(v_num_anni_reclusione, 0) = 0 AND
           NVL(v_num_mesi_reclusione, 0) = 0 AND
           NVL(v_num_giorni_reclusione, 0) = 0 AND
           NVL(v_num_anni_arresto, 0) = 0 AND
           NVL(v_num_mesi_arresto, 0) = 0 AND
           NVL(v_num_giorni_arresto, 0) = 0
          -- Umberto - 19/1/2010 - Inserito test su v_flag_ergastolo
           AND v_flag_ergastolo NOT IN ('S', 'D', 'O') THEN
          v_flag_ergastolo := 'N';
        END IF;
        -- Fine
        DBMS_OUTPUT.PUT_LINE('v_flag_ergastolo >' || v_flag_ergastolo);

        ----RICERCA STATO PROCEDIMENTO
        BEGIN
          SELECT COD_STATO_PROCEDIMENTO
            INTO v_cod_stato_procedimento
            FROM STATO_PROCEDIMENTO
           WHERE FAS_SIE_ID_FASCICOLO_SIEP = v_id_fascicolo_siep
             AND (DATA, progressivo) IN
                 (SELECT MAX(DATA), MAX(progressivo)
                    FROM STATO_PROCEDIMENTO
                   WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                        --   Umberto 8/10/2013                               AND cod_ufficio_inserimento=par_ufficio)
                        --   Umberto 8/10/2013  Aggiunti 3 parametri ufficio
                     AND (cod_ufficio_inserimento = par_ufficio OR
                         cod_ufficio_inserimento = par_ufficio1 OR
                         cod_ufficio_inserimento = par_ufficio2 OR
                         cod_ufficio_inserimento = par_ufficio3))
                --   Umberto 8/10/2013  Fine
                --  Umberto 8/10/2013            AND COD_UFFICIO_INSERIMENTO=par_ufficio;
                --   Umberto 8/10/2013  Aggiunti 3 parametri ufficio
             AND (cod_ufficio_inserimento = par_ufficio OR
                 cod_ufficio_inserimento = par_ufficio1 OR
                 cod_ufficio_inserimento = par_ufficio2 OR
                 cod_ufficio_inserimento = par_ufficio3);
          --   Umberto 8/10/2013  Fine
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            BEGIN
              SELECT COD_STATO_PROCEDIMENTO
                INTO v_cod_stato_procedimento
                FROM STATO_PROCEDIMENTO
               WHERE FAS_SIE_ID_FASCICOLO_SIEP = v_id_fascicolo_siep
                 AND (progressivo) IN
                     (SELECT MAX(progressivo)
                        FROM STATO_PROCEDIMENTO
                       WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                            --   Umberto 8/10/2013                               AND cod_ufficio_inserimento=par_ufficio)
                            --   Umberto 8/10/2013  Aggiunti 3 parametri ufficio
                         AND (cod_ufficio_inserimento = par_ufficio OR
                             cod_ufficio_inserimento = par_ufficio1 OR
                             cod_ufficio_inserimento = par_ufficio2 OR
                             cod_ufficio_inserimento = par_ufficio3))
                    --   Umberto 8/10/2013  Fine
                    --  Umberto 8/10/2013            AND COD_UFFICIO_INSERIMENTO=par_ufficio;
                    --   Umberto 8/10/2013  Aggiunti 3 parametri ufficio
                 AND (cod_ufficio_inserimento = par_ufficio OR
                     cod_ufficio_inserimento = par_ufficio1 OR
                     cod_ufficio_inserimento = par_ufficio2 OR
                     cod_ufficio_inserimento = par_ufficio3);
              --   Umberto 8/10/2013  Fine
            EXCEPTION
              WHEN NO_DATA_FOUND THEN
                v_cod_stato_procedimento := '-';
            END;
        END;

        DBMS_OUTPUT.PUT_LINE('v_cod_stato_procedimento >' ||
                             v_cod_stato_procedimento);
        ---- aggiunto il 24/05/2007
        ---- RICERCA PENA COMPLESSIVA
        BEGIN
          SELECT num_anni_reclusione,
                 num_mesi_reclusione,
                 num_giorni_reclusione,
                 num_anni_arresto,
                 num_mesi_arresto,
                 num_giorni_arresto
            INTO v_num_anni_reclusione_sen,
                 v_num_mesi_reclusione_sen,
                 v_num_giorni_reclusione_sen,
                 v_num_anni_arresto_sen,
                 v_num_mesi_arresto_sen,
                 v_num_giorni_arresto_sen
            FROM PENA_COMPLESSIVA
           WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
             AND cod_ufficio_inserimento = par_ufficio
             AND ROWNUM = 1; ----modificato il 19/07/2007
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_quantum_pena_compl        := 'N';
            v_num_anni_reclusione_sen   := 0;
            v_num_mesi_reclusione_sen   := 0;
            v_num_giorni_reclusione_sen := 0;
            v_num_anni_arresto_sen      := 0;
            v_num_mesi_arresto_sen      := 0;
            v_num_giorni_arresto_sen    := 0;
        END;

        -----CONTROLLO DEL QUANTUM    ---modificato il 20/02/2007
        -- va fatto sulla pena residua (quella effettiva!!!!)
        IF (v_num_anni_reclusione = 0 OR v_num_anni_reclusione IS NULL) AND
           (v_num_mesi_reclusione = 0 OR v_num_mesi_reclusione IS NULL) AND
           (v_num_giorni_reclusione = 0 OR v_num_giorni_reclusione IS NULL) AND
           (v_num_anni_arresto = 0 OR v_num_anni_arresto IS NULL) AND
           (v_num_mesi_arresto = 0 OR v_num_mesi_arresto IS NULL) AND
           (v_num_giorni_arresto = 0 OR v_num_giorni_arresto IS NULL) THEN
          v_quantum := 'N';
        ELSE
          v_quantum := 'S';
        END IF;
        DBMS_OUTPUT.PUT_LINE('v_quantum >' || v_quantum);

        -- APERTURA CURSORE
        OPEN cur_evento(v_id_fascicolo_siep);

        -- LETTURA FUORI CICLO DEI DATI DELL'EVENTO
        DBMS_OUTPUT.PUT_LINE('                      ');
        DBMS_OUTPUT.PUT_LINE('Recupero gli eventi...');
        FETCH cur_evento
          INTO v_cod_tipo_evento,
               v_cod_tipo_provvedimento,
               v_cod_motivo,
               v_data_inserimento;
        DBMS_OUTPUT.PUT_LINE('v_cod_tipo_evento  >' || v_cod_tipo_evento);
        DBMS_OUTPUT.PUT_LINE('v_cod_motivo       >' || v_cod_motivo);
        DBMS_OUTPUT.PUT_LINE('v_data_inserimento >' ||
                             to_char(v_data_inserimento,
                                     'yyyy-mm-dd hh24:mi:ss'));

        DBMS_OUTPUT.PUT_LINE('fetch fuori ciclo evento');

        ----aggiunto il 17/05/2007
        DBMS_OUTPUT.PUT_LINE('VALORE PRIMA DELLA IF (v_cod_motivo IS NULL): [V_COD_MOTIVO,  v_cod_stato_procedimento, v_cod_stato_fascicolo_res] = [' ||
                             V_COD_MOTIVO || ' , ' ||
                             v_cod_stato_procedimento || ' , ' ||
                             v_cod_stato_fascicolo_res || ']');
        IF v_cod_motivo IS NULL THEN
          DBMS_OUTPUT.PUT_LINE('V_COD_MOTIVO NULL cerco di classificare in base a stato procedimento');
          v_cod_stato_fascicolo_res := '163';
          IF v_cod_stato_procedimento = '0109' THEN
            ----istruttorie senza pena non devono essere considerate solo per procura di TO
            -- Sancamillo Luglio 2009 richiesta di TESTA 2^ mail (riepilogo roma)
            -- INIZIO
            --IF par_ufficio='00127202101' AND v_quantum_pena_compl='N' THEN ----aggiunto il 24/05/2007
            IF v_quantum_pena_compl = 'N' THEN
              ----aggiunto il 24/05/2007
              -- FINE
              v_cod_stato_fascicolo_res := '163';
              ---RAISE salta_record;    ---- modificato il 16/10/2007
            ELSE
              v_cod_stato_fascicolo_res := '154';
            END IF;
          ELSIF v_cod_stato_procedimento = '0076' THEN
            --aggiunto il 10/05/2007
            v_cod_stato_fascicolo_res := '131';
            -- Aggiunto il 10/12/2008 - UMBERTO
          ELSIF v_cod_stato_procedimento = '0129' THEN
            --aggiunto il 10/12/2008
            v_cod_stato_fascicolo_res := '129';
            -- Fine 10/12/2008
          ELSIF v_cod_stato_procedimento = '0108' THEN
            --aggiunto il 10/01/2008 ISCRITTI
            v_cod_stato_fascicolo_res := '159';
            -- Aggiunto il 28/01/2009 - DANIELA
            ---
            -- Ticket#20220713016 Riepilogo ispettivo   Umberto 15/7/2022
            --  aggiunto recupero procedimenti con stato = '0113' (Richiesta Informazioni in data)
          ELSIF v_cod_stato_procedimento IN ('9999', '0113') THEN
            -- Fine -- Ticket#20220713016
            -- aggiunto su segnalazione di Umberto/Testa
            v_cod_stato_fascicolo_res := '129'; -- Altre Posizioni
            -- Fine 28/01/2009 - Fine
          END IF;
          ----   UMBERTO  17/4/2009  PER SUPERARE ERRORE CAUSATO DAI PROCEDIMENTI DI CONVERSIONE PENA PECUNIARIA
          IF v_af8_nres > '999999999' THEN
            ----aggiunto il 24/05/2007
            v_af8_nres := '';
          END IF;
          ----   FINE - UMBERTO  17/4/2009  PER SUPERARE ERRORE CAUSATO DAI PROCEDIMENTI DI CONVERSIONE PENA PECUNIARIA
          DBMS_OUTPUT.PUT_LINE('VALORE DENTRO IF: [V_COD_MOTIVO,  v_cod_stato_procedimento, v_cod_stato_fascicolo_res] = [' ||
                               V_COD_MOTIVO || ' , ' ||
                               v_cod_stato_procedimento || ' , ' ||
                               v_cod_stato_fascicolo_res || ']');

          -- Umberto 8/10/2013  inserita routine recupero descrizione ufficio inserimento
          ----RICERCA LUOGO UFFICIO INSERIMENTO
          BEGIN
            SELECT descrizione
              INTO v_desc_ufficio_inserimento
              FROM COMUNE
             WHERE cod_comune = SUBSTR(v_cod_ufficio_inserimento, 1, 6);
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              v_desc_ufficio_inserimento := '-';
          END;
          -- Umberto 8/10/2013   Fine modifiche
          BEGIN
            SELECT descrizione
              INTO v_desc_ufficio_inserimento
              FROM COMUNE
             WHERE cod_comune = SUBSTR(v_cod_ufficio_inserimento, 1, 6);
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              v_desc_ufficio_inserimento := '-';
          END;

          IF v_cod_stato_fascicolo_res IS NOT NULL THEN

            -- Umberto 25/10/2013  Recupero procedimenti che finirebbero in Altre posizioni (129) in specifiche categorie
            IF v_cod_stato_fascicolo_res = '129' AND
               v_cod_posizione_giuridica IN ('53', '04') THEN
              v_cod_stato_fascicolo_res := '18';
            END IF;

            IF v_cod_stato_fascicolo_res = '129' AND
               v_cod_posizione_giuridica IN ('49') THEN
              v_cod_stato_fascicolo_res := '166'; --Emesso Decreto MdS Sospensione Provvisoria Arresti Domiciliari
            END IF;

            IF v_cod_stato_fascicolo_res = '129' AND
               v_cod_tipo_provvedimento IN ('-', '05', '26') THEN
              v_cod_stato_fascicolo_res := '154';
            END IF;
            -- Umberto 25/10/2013  Fine

            -- MEV42 07/2017  Recupero procedimenti che finirebbero in Istruttoria (154)
            IF v_cod_stato_fascicolo_res = '154' THEN

              BEGIN
                SELECT Id_Istruttoria_Cumulo
                  INTO v_Id_Istruttoria_Cumulo
                  FROM ISTRUTTORIA_CUMULO
                 WHERE Fas_Sie_Id_Fascicolo_Siep = v_id_fascicolo_siep
                   AND Flag_Stato = 'A'
                   AND ROWNUM = 1;

                v_cod_stato_fascicolo_res := '3';
                DBMS_OUTPUT.PUT_LINE('Istruttoria in Stato A - v_Id_Istruttoria_Cumulo= ' ||
                                     v_Id_Istruttoria_Cumulo);

              EXCEPTION
                WHEN NO_DATA_FOUND THEN
                  v_cod_stato_fascicolo_res := '154';
                  DBMS_OUTPUT.PUT_LINE('NON Trovato - v_cod_stato_fascicolo_res = ' ||
                                       v_cod_stato_fascicolo_res);
              END;

            END IF;

            DBMS_OUTPUT.PUT_LINE('faccio insert ISP_PROVVEDIMENTI FUORI CICLO EVENTO  ' ||
                                 v_cod_stato_fascicolo_res || '-' ||
                                 v_cod_posizione_giuridica);
            BEGIN
              INSERT INTO STATIS.ISP_PROVVEDIMENTI
                (ID_FASCICOLO_SIEP,
                 NRES,
                 CHIAVE_ANNO,
                 CHIAVE_PROGR,
                 ULT_TIPO_PROVVEDIMENTO,
                 ULT_COD_MOTIVO,
                 PEN_TIPO_PROVVEDIMENTO,
                 PEN_COD_MOTIVO,
                 COD_UFFICIO,
                 COD_POSIZIONE_GIURIDICA,
                 COD_STATO_PROCEDIMENTO,
                 -- Umberto   8/10/2013                                               COD_STATO_FASCICOLO_RES)
                 COD_STATO_FASCICOLO_RES,
                 -- Umberto   8/10/2013  inserita valorizzazione colonne aggiuntive
                 COD_UFFICIO_INSERIMENTO,
                 CHIAVE_PROGR_ORIG,
                 DESC_UFFICIO_INSERIMENTO)
              -- Umberto  8/10/2013   Fine modifiche
              VALUES
                (v_id_fascicolo_siep,
                 v_af8_nres,
                 v_chiave_anno,
                 v_chiave_progr,
                 v_cod_tipo_provvedimento,
                 v_cod_motivo,
                 v_cod_tipo_provvedimento_1,
                 v_cod_motivo_1,
                 par_ufficio,
                 v_cod_posizione_giuridica,
                 v_cod_stato_procedimento,
                 -- Umberto 8/10/2013                                               v_cod_stato_fascicolo_res);
                 v_cod_stato_fascicolo_res,
                 -- Umberto  8/10/2013  inserite nuove variabili
                 v_cod_ufficio_inserimento,
                 v_chiave_progr_orig,
                 v_desc_ufficio_inserimento);
              -- Umberto 8/10//2013  Fine modifiche
            EXCEPTION
              WHEN DUP_VAL_ON_INDEX THEN
                --aggiunto il 3/12/2007
                NULL;
            END;
            COMMIT; ---aggiunto il 05/05/2008
          END IF;

        END IF;

        DBMS_OUTPUT.PUT_LINE('VALORE DOPO DELLA IF: [V_COD_MOTIVO, v_cod_stato_procedimento, v_cod_stato_fascicolo_res] = [' ||
                             V_COD_MOTIVO || ', ' ||
                             v_cod_stato_procedimento || ', ' ||
                             v_cod_stato_fascicolo_res || ']');
        -- INIZIO CICLO DI ESTRAZIONE DATI PER OGNI RECORD RILASCIATO DALLA FETCH
        WHILE cur_evento%FOUND LOOP
          BEGIN
            ---inizio loop

            DBMS_OUTPUT.PUT_LINE('INIZIO DEL LOOP - EVENTI ');

            DBMS_OUTPUT.PUT_LINE('v_cod_tipo_evento        >' ||
                                 v_cod_tipo_evento);
            DBMS_OUTPUT.PUT_LINE('v_cod_tipo_provvedimento >' ||
                                 v_cod_tipo_provvedimento);
            DBMS_OUTPUT.PUT_LINE('v_cod_motivo             >' ||
                                 v_cod_motivo);
            DBMS_OUTPUT.PUT_LINE('v_data_inserimento       >' ||
                                 v_data_inserimento);
            DBMS_OUTPUT.PUT_LINE('   ');
            DBMS_OUTPUT.PUT_LINE('v_cod_stato_fascicolo_res >' ||
                                 v_cod_stato_fascicolo_res);

            v_cod_stato_fascicolo_res := NULL;
            v_uscita                  := 'N';

            ----------------------------------------------
            -------TROVO PENULTIMO EVENTO------------------
            -----------------------------------------------
            BEGIN
              SELECT cod_tipo_provvedimento, cod_motivo
                INTO v_cod_tipo_provvedimento_1, v_cod_motivo_1
                FROM EVENTO,
                     (SELECT MAX(data_inserimento) max_data
                         FROM EVENTO
                        WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                             --  Umberto  8/10/2013           AND cod_ufficio_inserimento=par_ufficio
                             --   Umberto 8/10/2013  Aggiunti 3 parametri ufficio
                          AND (cod_ufficio_inserimento = par_ufficio OR
                              cod_ufficio_inserimento = par_ufficio1 OR
                              cod_ufficio_inserimento = par_ufficio2 OR
                              cod_ufficio_inserimento = par_ufficio3)
                             -- Ticket#20230914017 la data deve essere strettamente minore altrimenti estrae di nuovo
                             -- l'ultimo eveto
                            --AND data_inserimento <= v_data_inserimento
                         AND data_inserimento < v_data_inserimento
                            -- Ticket#20230914017 - FINE
                         AND flag_documento_registrato = 'S'
                         AND fas_siu_id_fascicolo_sius IS NULL
                            -- Umberto 19/1/2009 - inserito scarto evento con cod_tipo_evento = '05' - Richiesta Istruttoria
                         AND COD_TIPO_EVENTO <> '05'
                            -- Umberto 31/10/2013 scarto decreti, ordinanze
                         AND cod_tipo_provvedimento NOT IN ('02', '03', '-')) eve
               WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                    --  Umberto  8/10/2013           AND cod_ufficio_inserimento=par_ufficio
                    --   Umberto 8/10/2013  Aggiunti 3 parametri ufficio
                 AND (cod_ufficio_inserimento = par_ufficio OR
                     cod_ufficio_inserimento = par_ufficio1 OR
                     cod_ufficio_inserimento = par_ufficio2 OR
                     cod_ufficio_inserimento = par_ufficio3)
                    --   Umberto 8/10/2013  Fine
                 AND data_inserimento = eve.max_data
                 AND id_evento =
                     (SELECT MAX(id_evento)
                        FROM EVENTO
                       WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                            --  Umberto  8/10/2013           AND cod_ufficio_inserimento=par_ufficio
                            --   Umberto 8/10/2013  Aggiunti 3 parametri ufficio
                         AND (cod_ufficio_inserimento = par_ufficio OR
                             cod_ufficio_inserimento = par_ufficio1 OR
                             cod_ufficio_inserimento = par_ufficio2 OR
                             cod_ufficio_inserimento = par_ufficio3)
                            --   Umberto 8/10/2013  Fine
                         AND data_inserimento = eve.max_data
                         AND flag_documento_registrato = 'S'
                         AND fas_siu_id_fascicolo_sius IS NULL
                            -- Umberto 19/1/2009 - inserito scarto evento con cod_tipo_evento = '05' - Richiesta Istruttoria
                         AND COD_TIPO_EVENTO <> '05'
                            -- Umberto 31/10/2013 scarto decreti, ordinanze
                         AND cod_tipo_provvedimento NOT IN ('02', '03', '-'));
            EXCEPTION
              WHEN NO_DATA_FOUND THEN
                v_cod_tipo_provvedimento_1 := NULL;
                v_cod_motivo_1             := NULL;
            END;
            --bvn 11 04 2016
            v_altra_causa_pos_giud := NULL;
            BEGIN
              SELECT a.cod_tipo_pos_giuridica
                INTO v_altra_causa_pos_giud
                FROM posizione_giuridica b, altra_causa a
               WHERE b.alt_cau_id_altra_causa = a.id_altra_causa
                 AND b.fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                 AND b.data_fine IS NULL;
            EXCEPTION
              WHEN OTHERS THEN
                v_altra_causa_pos_giud := NULL;
            END;

            -- 2023.11.21 Ticket#20230914017 ????????? RITM0609865 - SIEP - False pendenze
            -- TEST nel caso delle richieste conversione (0942) si porva a sostituire con
            -- il penultimo eventi
            /*
            IF v_cod_motivo IN ('0942') THEN
              DBMS_OUTPUT.PUT_LINE('Ticket#20230914017 v_cod_motivo = ' || v_cod_motivo||', v_cod_motivo_1 = '||v_cod_motivo_1);
              IF v_cod_motivo_1 IN ('0044','0045','0046','0047','0048','0049','0050','0051','0052','0053',
                                    '0054','0056','0064','0219','0565','0566','0557','0578','0579','0580') OR
                 (v_cod_motivo_1 BETWEEN '5001' AND '5129') OR
                 v_cod_motivo_1 IS NULL THEN
                v_cod_motivo := v_cod_motivo;
              ELSE
                DBMS_OUTPUT.PUT_LINE('Ticket#20230914017 sostituisco con ' || v_cod_motivo_1);
                v_cod_motivo             := v_cod_motivo_1;
                v_cod_tipo_provvedimento := v_cod_tipo_provvedimento_1;
              END IF;
            END IF;
            */
            -- 2023.11.21

            --fine 11 04 2016
            -- Sancamillo : Aggiunto ---> Giugno 2009 su segnalazione di Testa
            -- SCARTO tutti i codici 50
            -- Inizio
            -- pagina 2 del doc di testa (mail del 11.06.2009)
            DBMS_OUTPUT.PUT_LINE('v_cod_MOTIVO ' || v_cod_motivo);
            DBMS_OUTPUT.PUT_LINE('v_cod_MOTIVO_1 ' || v_cod_motivo_1);
            IF v_cod_motivo IN ('0341',
                                '0352',
                                '0335',
                                '0332',
                                '0320',
                                '0350',
                                '0347',
                                '0323',
                                '0327',
                                '0343',
                                '0333',
                                '0321',
                                '0324',
                                '5130',
                                '5400',
                                '0925',
                                '0922',
                                '0923' -- LA comunicazioni
                               ,
                                '5493',
                                '5494',
                                '5495' -- DL92 comunicazioni
                                ) THEN
              -- '0344','0329','0328','0325' questi vengono catalogati giusti !!!!
              -- Umberto 7/1/2010: inserita modifica per recupero ultimo evento, se penultimo ??????????????????????????? relativo a Richiesta Istruttoria o Pene Accessorie
              IF v_cod_motivo_1 IN ('0044',
                                    '0045',
                                    '0046',
                                    '0047',
                                    '0048',
                                    '0049',
                                    '0050',
                                    '0051',
                                    '0052',
                                    '0053',
                                    '0054',
                                    '0056',
                                    '0064',
                                    '0219',
                                    '0565',
                                    '0566',
                                    '0557',
                                    '0578',
                                    '0579',
                                    '0580') OR
                 (v_cod_motivo_1 BETWEEN '5001' AND '5129') OR
                 v_cod_motivo_1 IS NULL THEN
                v_cod_motivo := v_cod_motivo;
              ELSE
                v_cod_motivo             := v_cod_motivo_1;
                v_cod_tipo_provvedimento := v_cod_tipo_provvedimento_1;
              END IF;
            END IF;
            -- Umberto 30/10/2013: inserito scarto comunicazione/annotazione Rideterminazione Pena
            IF v_cod_motivo IN ('1007',
                                '0916',
                                '0954',
                                '0969',
                                '0981',
                                '0915',
                                '0959',
                                '0974',
                                '0986',
                                '0914',
                                '0956',
                                '0973',
                                '0985',
                                '0913',
                                '0958',
                                '0987',
                                '0989',
                                '0991',
                                '0988',
                                '0990',
                                '0992',
                                '0917',
                                '0957',
                                '0972',
                                '0984',
                                '0994',
                                '0995',
                                '0162',
                                '0999',
                                '1000',
                                '1003',
                                '1004',
                                '1005',
                                '1006') THEN
              -- '0344','0329','0328','0325' questi vengono catalogati giusti !!!!
              IF v_cod_motivo_1 IN ('0044',
                                    '0045',
                                    '0046',
                                    '0047',
                                    '0048',
                                    '0049',
                                    '0050',
                                    '0051',
                                    '0052',
                                    '0053',
                                    '0054',
                                    '0056',
                                    '0064',
                                    '0219',
                                    '0565',
                                    '0566',
                                    '0557',
                                    '0578',
                                    '0579',
                                    '0580') OR
                 (v_cod_motivo_1 BETWEEN '5001' AND '5129') OR
                 v_cod_motivo_1 IS NULL THEN
                v_cod_motivo := v_cod_motivo;
              ELSE
                v_cod_tipo_provvedimento := v_cod_tipo_provvedimento_1;
                v_cod_motivo             := v_cod_motivo_1;
              END IF;
              -- Umberto 7/1/2010: Fine intervento
              --RAISE salta_record;
            END IF;
            -- pagina 3 del doc di testa (mail del 11.06.2009)
            IF v_cod_motivo IN ('0331', '0330', '0322', '0326') AND
               v_cod_motivo_1 IS NOT NULL THEN
              v_cod_motivo             := v_cod_motivo_1;
              v_cod_tipo_provvedimento := v_cod_tipo_provvedimento_1;
              --RAISE salta_record;
            END IF;
            -- integrazione del doc di testa con altra mail del 09.07.2009 (tabelle)
            IF v_cod_motivo IN ('0334',
                                '0336',
                                '0337',
                                '0338',
                                '0340',
                                '0342',
                                '0345',
                                '0348',
                                '0349',
                                '0351',
                                '5135') THEN
              -- Umberto 7/1/2010: inserita modifica per recupero ultimo evento, se penultimo ??????????????????????????? relativo a Richiesta Istruttoria o Pene Accessorie
              IF v_cod_motivo_1 IN ('0044',
                                    '0045',
                                    '0046',
                                    '0047',
                                    '0048',
                                    '0049',
                                    '0050',
                                    '0051',
                                    '0052',
                                    '0053',
                                    '0054',
                                    '0056',
                                    '0064',
                                    '0219',
                                    '0565',
                                    '0566',
                                    '0557',
                                    '0578',
                                    '0579',
                                    '0580') OR
                 (v_cod_motivo_1 BETWEEN '5001' AND '5129') OR
                 v_cod_motivo_1 IS NULL THEN
                v_cod_motivo := v_cod_motivo;
              ELSE
                v_cod_motivo             := v_cod_motivo_1;
                v_cod_tipo_provvedimento := v_cod_tipo_provvedimento_1;
              END IF;
              -- Umberto 7/1/2010: Fine intervento
              --RAISE salta_record;
            END IF;
            -- Fine

            -- Sancamillo : Aggiunto ---> Giugno 2009 su segnalazione di Testa
            -- SCARTO tutti i codici 60
            -- Inizio
            IF v_cod_motivo IN ('5136') THEN
              v_cod_motivo := v_cod_motivo_1;
              --RAISE salta_record;
            END IF;
            -- Fine

            -- Modifiche UMBERTO
            IF v_cod_motivo IN ('0335') THEN
              -- aggiunto il 27/03/2007
              -- Umberto 7/1/2010: inserita modifica per recupero ultimo evento, se penultimo ??????????????????????????? relativo a Richiesta Istruttoria o Pene Accessorie
              IF v_cod_motivo_1 IN ('0044',
                                    '0045',
                                    '0046',
                                    '0047',
                                    '0048',
                                    '0049',
                                    '0050',
                                    '0051',
                                    '0052',
                                    '0053',
                                    '0054',
                                    '0056',
                                    '0064',
                                    '0219',
                                    '0565',
                                    '0566',
                                    '0557',
                                    '0578',
                                    '0579',
                                    '0580') OR
                 (v_cod_motivo_1 BETWEEN '5001' AND '5129') OR
                 v_cod_motivo_1 IS NULL THEN
                v_cod_motivo := v_cod_motivo;
              ELSE
                v_cod_motivo             := v_cod_motivo_1;
                v_cod_tipo_provvedimento := v_cod_tipo_provvedimento_1;
              END IF;
              -- Umberto 7/1/2010: Fine intervento
              -- RAISE salta_record;               -- commentato il 15/12/2008
            END IF;
            -- Fine Modifiche UMBERTO

            -- COMMENTATE IL 9/12/2008 UMBERTO
            /*
            IF v_cod_motivo IN ('0925','0321') THEN  ----aggiunto il 11/01/2008
               RAISE salta_record;
            END IF;
            */
            -- FINE 9/12/2008 UMBERTO

            -- COMMENTATE IL 9/12/2008 - UMBERTO
            /*
            -- PENE ACCESSORIE COMMENTATE IL 11/01/2008 CON CODICE 160
            IF v_cod_motivo BETWEEN '5001' AND '5129' THEN
               RAISE salta_record;
            END IF;
            */
            -- FINE 9/12/2008 - UMBERTO
            /*
              IF v_cod_motivo BETWEEN '5001' AND '5129' THEN
                RAISE salta_record;
             END IF;

             IF v_cod_motivo ='0122' THEN
                --v_cod_motivo:=v_cod_motivo_1;
                RAISE salta_record;
             END IF;
            */

            --- devo trovare l'ultimo evento che non ??????????????????????????? un'istruttoria
            --- nel caso che stato procedimento ??????????????????????????? diverso da validato   aggiunto il 27/03/2007
            ----- in data 5/1/2009 COMMENTATE LE 3 ISTRUZIONI SUCCESSIVE PER ESTRARRE ANCHE I PROCEDIMENTI MIGRATI 'In Attesa Archiviazione' - UMBERTO
            /*
            IF v_cod_motivo IN ('0161','0162','0163') THEN
               RAISE salta_record;
            END IF;
            */
            ---- FINE MODIFICHE 05/01/2009

            --- devo trovare l'ultimo evento che non ??????????????????????????? un'istruttoria
            --- nel caso che stato procedimento ??????????????????????????? diverso da validato   aggiunto il 27/03/2007
            ---
            -- Ticket#20220713016 Riepilogo ispettivo   Umberto 15/7/2022
            --  aggiunto per valore '0113' per recupero procedimenti con stato 'Richiesta informazioni in data'
            IF (v_cod_stato_procedimento NOT IN
               ('0109', '0108', '0129', '0216', '0142', '0113')) AND
              -- Fine Ticket#20220713016
               (v_cod_motivo IN ('0044',
                                 '0045',
                                 '0046',
                                 '0047',
                                 '0048',
                                 '0049',
                                 '0050',
                                 '0051',
                                 '0052',
                                 '0053',
                                 '0054',
                                 '0056',
                                 '0064',
                                 '0219',
                                 '0565',
                                 '0566',
                                 '0557',
                                 '0578',
                                 '0579',
                                 '0580',
                                 '0122') OR
               v_cod_motivo BETWEEN '5001' AND '5129') THEN
              ----modificato il 09/09/2008
              -- Umberto 29/10/2013 Fine
              RAISE salta_record;
            END IF;

            -- devo trovare l'ultimo evento che non ??????????????????????????? una pena accessoria
            -- nel caso che stato procedimento ??????????????????????????? diverso da validato   aggiunto il 27/03/2007
            -- DA ESCLUDERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            -- IF v_cod_motivo between '5001' and '5129' THEN
            --    BEGIN
            --      select b.cod_motivo
            --        into v_cod_motivo
            --        from evento b, (select max(data_inserimento) max_data
            --                          from evento
            --                         where cod_ufficio_inserimento=par_ufficio
            --                           and fas_sie_id_fascicolo_siep=v_id_fascicolo_siep
            --                           and flag_documento_registrato='S'
            --                           and fas_siu_id_fascicolo_sius is null
            --                           and cod_motivo not between '5001' and '5129') eve
            --       where b.data_inserimento=eve.max_data
            --         and b.fas_sie_id_fascicolo_siep=v_id_fascicolo_siep
            --         and b.cod_ufficio_inserimento=par_ufficio
            --         and b.id_evento = (select max(id_evento)
            --                              from evento
            --                             where cod_ufficio_inserimento=par_ufficio
            --                               and fas_sie_id_fascicolo_siep=v_id_fascicolo_siep
            --                               and flag_documento_registrato='S'
            --                               and fas_siu_id_fascicolo_sius is null
            --                               and data_inserimento=eve.max_data
            --                               and cod_motivo not between '5001' and '5129');
            --
            --    EXCEPTION
            --      WHEN NO_DATA_FOUND THEN
            --           v_cod_stato_fascicolo_res:='129';
            --    END;
            -- END IF;

            DBMS_OUTPUT.PUT_LINE('[Diego] fine eventuale rimappatura motivo evento con penultimo');
            DBMS_OUTPUT.PUT_LINE('v_cod_motivo = ' || v_cod_motivo);
            DBMS_OUTPUT.PUT_LINE('v_cod_motivo_1 = ' || v_cod_motivo_1);
            DBMS_OUTPUT.PUT_LINE('v_cod_stato_fascicolo_res = ' ||
                                 v_cod_stato_fascicolo_res);

            DBMS_OUTPUT.PUT_LINE('Inizio assegnazione v_cod_stato_fascicolo_res... ');
            IF v_cod_stato_fascicolo = '01' THEN
              -- aggiunto il 10/05/2007
              v_cod_stato_fascicolo_res := '131'; -- 131:  Archiviazioni
              -- [Ticket#20210506013] - Per evitare che venga riclassificato come pendente in caso di ergastolo.
              v_fasc_archiviato := 'S';
              -- FINE [Ticket#20210506013]
            ELSIF v_cod_stato_procedimento = '0154' AND -- 0154: In attesa di archiviazione
                  v_cod_posizione_giuridica = '10' THEN
              -- 10:   Libero                                    ---aggiunto il 5/12/2007
              v_cod_stato_fascicolo_res := '130'; -- 130:  Attesa Definizione Procedimento
            ELSIF v_cod_stato_procedimento = '0139' THEN
              -- 139:  Trasmessi Atti per competenza per emissione provvedimento di cumulo
              v_cod_stato_fascicolo_res := '160'; -- 160:  Trasmissione Atti per Competenza per Emissione Provvedimento di Cumulo                       ----aggiunto il 20/03/2008
              -- Sancamillo Gennaio 2009 (MODIFICA 4) Inizio
            ELSIF v_cod_stato_procedimento = '9999' THEN
              -- 9999: Altre Posizioni
              v_cod_stato_fascicolo_res := '129'; --  129: Altre Posizioni
              -- Sancamillo Gennaio 2009 (MODIFICA 4) Fine
              -- Sancamillo Gennaio 2009 (MODIFICA 1) Inizio
            ELSIF v_cod_stato_procedimento = '0004' AND -- 0004: Pena in Esecuzione dal
                  v_cod_posizione_giuridica = '03' THEN
              -- 03:   Espiazione Pena in Regime Carcerario
              v_cod_stato_fascicolo_res := '13'; -- 13:   In Corso di Espiazione in Regime Carcerario
              -- Sancamillo Gennaio 2009 (MODIFICA 1) Fine
              -- Sancamillo Gennaio 2009 (MODIFICA 2) Inizio
            ELSIF v_cod_stato_procedimento = '0108' THEN
              -- 0108: Iscritto
              -- Controllo se gli eventi sono tutti con codice_motivo = 5136
              esiste_altri := 0;
              BEGIN
                SELECT COUNT(*)
                  INTO esiste_altri
                  FROM EVENTO
                 WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                      --  Umberto  8/10/2013           AND cod_ufficio_inserimento=par_ufficio
                      --   Umberto 8/10/2013  Aggiunti 3 parametri ufficio
                   AND (cod_ufficio_inserimento = par_ufficio OR
                       cod_ufficio_inserimento = par_ufficio1 OR
                       cod_ufficio_inserimento = par_ufficio2 OR
                       cod_ufficio_inserimento = par_ufficio3)
                      --   Umberto 8/10/2013  Fine
                   AND cod_motivo != '5136';
              EXCEPTION
                WHEN NO_DATA_FOUND THEN
                  esiste_altri := 0;
              END;
              DBMS_OUTPUT.PUT_LINE('CONTROLLO  valore  v_cod_stato_fascicolo_res prima ' || ' ' ||
                                   esiste_altri || ' ' ||
                                   v_cod_stato_fascicolo_res);
              IF v_cod_motivo = '5136' AND -- 5136: Provvedimento
                 esiste_altri = 0 THEN
                v_cod_stato_fascicolo_res := '129'; -- 129:  Altre Posizioni
              ELSE
                v_cod_stato_fascicolo_res := '159'; -- 159:  Iscritto
              END IF;

              DBMS_OUTPUT.PUT_LINE('cONTROLLO  valore  v_cod_stato_fascicolo_res' ||
                                   v_cod_stato_fascicolo_res);
              -- Sancamillo Gennaio 2009 (MODIFICA 2) Fine
              -- Sancamillo Gennaio 2009 (MODIFICA 3) Inizio
            ELSIF v_cod_motivo = '0557' THEN
              -- 0557:  Richiesta Codice CUI
              -- Controllo se gli eventi sono tutti con codice_motivo = 0557
              esiste_altri := 0;
              BEGIN
                SELECT COUNT(*)
                  INTO esiste_altri
                  FROM EVENTO
                 WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                   AND cod_ufficio_inserimento = par_ufficio
                   AND cod_motivo != '0557';
              EXCEPTION
                WHEN NO_DATA_FOUND THEN
                  esiste_altri := 0;
              END;
              IF esiste_altri = 0 THEN
                v_cod_stato_fascicolo_res := '154'; -- 154:  Istruttoria
              END IF;
              -- MEV_9-SIEP
              -- APPLICAZIONI PROVVISORIE
              -- MEV_92_2024 APPLICAZIONI
            ELSIF v_cod_motivo IN ('1400', '5422') THEN
              v_cod_stato_fascicolo_res := '225'; -- Applicazione Affidamento in prova al Servizio Sociale (Art. 47 O.P. -  Art. 678 comma 1-ter c.p.p.)
            ELSIF v_cod_motivo IN ('1401', '5423') THEN
              v_cod_stato_fascicolo_res := '226'; -- Applicazione Affidamento in prova al Servizio Sociale  (Art. 94 DPR 309/90 - Art. 678 comma 1-ter c.p.p.)
            ELSIF v_cod_motivo IN ('1402') THEN
              v_cod_stato_fascicolo_res := '227'; -- Applicazione Detenzione domiciliare (Art. 47 ter O.P. - Art. 678 comma 1-ter c.p.p.)
            ELSIF v_cod_motivo IN ('1403') THEN
              v_cod_stato_fascicolo_res := '228'; -- Applicazione Semilibert? (Art. 50 comma 1 O.P. - Art. 678 comma 1-ter c.p.p.)
            ELSIF v_cod_motivo IN ('1404', '1415') THEN
              v_cod_stato_fascicolo_res := '229'; -- Applicazione Sospensione Esecuzione Pena (Art. 90 DPR 309/90 - Art. 678 comma 1-ter c.p.p.)
            ELSIF v_cod_motivo IN ('1410', '5424') THEN
              v_cod_stato_fascicolo_res := '230'; -- Applicazione Affidamento in Prova al Servizio Sociale (art. 4 D.lgs. 121/2018, art. 678 comma 1 ter cp.p.)
            ELSIF v_cod_motivo IN ('1411', '5425') THEN
              v_cod_stato_fascicolo_res := '231'; -- Applicazione Affidamento in Prova in casi particolari  (art. 4 D.lgs. 121/2018 -  94 DPR 309/90, art. 678 comma 1 ter cp.p.)
            ELSIF v_cod_motivo IN ('1412', '5426') THEN
              v_cod_stato_fascicolo_res := '232'; -- Applicazione Affidamento in prova con detenzione domiciliare (art. 5 d.lgs. 121/2018, art. 678 comma 1 ter cp.p.)
            ELSIF v_cod_motivo IN ('1413') THEN
              v_cod_stato_fascicolo_res := '233'; -- Applicazione Detenzione Domiciliare (art. 6 d.lgs. 121/2018 , art. 678 comma 1 ter cp.p.)
            ELSIF v_cod_motivo IN ('1414') THEN
              v_cod_stato_fascicolo_res := '234'; -- Applicazione Semiliberta' (art. 7 d.lgs. 121/2018, art. 678 comma 1 ter cp.p.)
            ELSIF v_cod_motivo IN ('1415') THEN
              v_cod_stato_fascicolo_res := '235'; -- Applicazione Sospensione dell'Esecuzione della Pena (Art. 90 DPR 309/90 - Art. 678 comma 1-ter c.p.p.)
            ELSIF v_cod_motivo IN ('1416', '1421', '1423') THEN
              v_cod_stato_fascicolo_res := '33'; -- Concessione Affidamento in prova al Servizio Sociale (Art. 47 O.P. -  Art. 678 comma 1-ter c.p.p.)
            ELSIF v_cod_motivo IN ('1417', '1422') THEN
              v_cod_stato_fascicolo_res := '47'; -- Concessione Affidamento in prova al Servizio Sociale  (Art. 94 DPR 309/90 - Art. 678 comma 1-ter c.p.p.)
            ELSIF v_cod_motivo IN ('1418', '1424') THEN
              v_cod_stato_fascicolo_res := '55'; -- Concessione Detenzione domiciliare (Art. 47 ter O.P. - Art. 678 comma 1-ter c.p.p.)
            ELSIF v_cod_motivo IN ('1419', '1425') THEN
              v_cod_stato_fascicolo_res := '90'; -- Concessione Semilibert? (Art. 50 comma 1 O.P. - Art. 678 comma 1-ter c.p.p.)
            ELSIF v_cod_motivo IN ('1420', '1426') THEN
              v_cod_stato_fascicolo_res := '120'; -- Concessione Sospensione Esecuzione Pena (Art. 90 DPR 309/90 - Art. 678 comma 1-ter c.p.p.)
            ELSIF v_cod_motivo IN ('2007') THEN
              v_cod_stato_fascicolo_res := '236'; -- Ammissione provvisoria Semilibert? (Art. 50 comma 1 O.P.)
              -- MEV_92_2024  Eliminate estrazioni ratifiche non pi? previste
              -- RATIFICHE
              /* ELSIF v_cod_motivo IN ('5460', '5462', '5463', '5464') THEN
                v_cod_stato_fascicolo_res := '230'; -- Ratifica Applicazione Provvisoria a Affidamento in Prova al Servizio Sociale (art. 47 O.P.)
              ELSIF v_cod_motivo IN ('5461') THEN
                v_cod_stato_fascicolo_res := '231'; -- Ratifica Applicazione provvisoria Affidamento Servizio Sociale (ex art. 94 DPR 309/90 )
              ELSIF v_cod_motivo IN ('5465', '5466') THEN
                v_cod_stato_fascicolo_res := '232'; -- Ratifica Applicazione Provvisoria a Detenzione Domiciliare (art. 678 c1 ter)
              ELSIF v_cod_motivo IN ('5467', '5468') THEN
                v_cod_stato_fascicolo_res := '233'; -- Ratifica Applicazione Provvisoria a Semilibert?? (art. 678 c1 ter)
              ELSIF v_cod_motivo IN ('5469', '5496') THEN
                v_cod_stato_fascicolo_res := '234'; -- Ratifica Applicazione Provvisoria Sospensione Esecuzione Pena (art. 678 c1 ter)
              -- MEV_92_2024  FINE
              -- MEV_9-SIEP*/
              -- Sancamillo Gennaio 2009 (MODIFICA 3) Fine
            ELSIF v_cod_motivo IN ('0141', '0325') THEN
              v_cod_stato_fascicolo_res := '151'; ----aggiunto il 20/03/2008
            ELSIF v_cod_motivo = '0488' THEN
              v_cod_stato_fascicolo_res := '161'; ----aggiunto il 20/03/2008
            ELSIF v_cod_motivo = '0062' THEN
              v_cod_stato_fascicolo_res := '19'; ----aggiunto il 20/03/2008
            ELSIF v_cod_motivo = '9001' THEN
              v_cod_stato_fascicolo_res := '13';
              -- GESTIONE DELLA NUOVA_ISTANZA - UMBERTO 29/06/2011
            ELSIF v_cod_motivo = '0993' THEN
              v_cod_stato_fascicolo_res := '28';
              -- FINE GESTIONE NUOVA_ISTANZA
            ELSIF v_cod_motivo BETWEEN '0006' AND '0009' OR ---modificato il 20/03/2008  ARCHIVIAZIONE
                  v_cod_motivo IN ('0019', '0022') OR
                  v_cod_motivo BETWEEN '0096' AND '0099' OR
                  v_cod_motivo IN
                  ('0119', '0120', '0353', '0356', '0357', '7002', '0473') OR
                  v_cod_motivo BETWEEN '0214' AND '0216' OR
                  v_cod_motivo BETWEEN '0400' AND '0409' OR
                  v_cod_motivo BETWEEN '0411' AND '0426' OR
                  v_cod_motivo BETWEEN '0476' AND '0481' THEN
              v_cod_stato_fascicolo_res := '131';
              v_fasc_archiviato         := 'S'; ----aggiunto il 10/09/2008
            ELSIF v_cod_motivo = '0341' AND v_cod_motivo_1 = '0340' THEN
              ---aggiunto 28/03/2007
              v_cod_stato_fascicolo_res := '4';
            ELSIF v_cod_motivo IS NOT NULL AND
                  v_cod_stato_fascicolo_res IS NULL THEN
              DBMS_OUTPUT.PUT_LINE('[Diego] stato non ancora assegnato... ');

              IF SUBSTR(v_cod_motivo, 1, 1) = 'C' THEN
                v_cod_stato_fascicolo_res := '28';
              ELSIF v_cod_motivo IN ('0320',
                                     '0322',
                                     '0324',
                                     '0326',
                                     '0344',
                                     '0289',
                                     '0210',
                                     '0293',
                                     '0295',
                                     '0297',
                                     '0299',
                                     '0211',
                                     '0288',
                                     '0287',
                                     '0290',
                                     '0291',
                                     '0292',
                                     '0294',
                                     '0296',
                                     '0298',
                                     --                                     '0342','0352','0341','0339','0338', COMMENTATO 24/3/2009 UMBERTO
                                     '0342',
                                     '0352',
                                     '0339',
                                     '0338',
                                     '0337',
                                     '0336',
                                     '0351',
                                     '0349',
                                     '0348',
                                     '0347',
                                     '0332',
                                     '0334',
                                     '0333',
                                     '0327',
                                     '0343',
                                     '0323') AND v_quantum = 'S' AND
                   -- v_cod_motivo_1 in ('0057','0058','0059','0060') AND  ---modificato il 10/05/2007
                    v_data_inizio IS NULL AND v_data_fine IS NULL THEN
                ----RICHIESTE GE
                v_cod_stato_fascicolo_res := '156';
                -----   umberto 24/3/2008
              ELSIF v_cod_motivo = '0341' THEN
                v_cod_stato_fascicolo_res := '154';
                -----   umberto 24/3/2008 fine
              ELSIF v_cod_motivo = '0055' THEN
                v_cod_stato_fascicolo_res := '114';
              ELSIF v_cod_motivo = '0813' THEN
                -----aggiunto il 19/10/2007
                v_cod_stato_fascicolo_res := '145';
              ELSIF v_cod_motivo = '0911' THEN
                -----aggiunto il 10/01/2008
                v_cod_stato_fascicolo_res := '128';
              ELSIF v_cod_motivo = '0904' THEN
                -----aggiunto il 10/01/2008
                v_cod_stato_fascicolo_res := '118';
              ELSIF v_cod_motivo = '0905' THEN
                -----aggiunto il 10/01/2008
                v_cod_stato_fascicolo_res := '119';
              ELSIF v_cod_motivo IN ('0901') THEN
                -----modificato il 20/03/2008
                v_cod_stato_fascicolo_res := '28';
              ELSIF v_cod_motivo IN ('0900') THEN
                -----modificato il 20/03/2008
                v_cod_stato_fascicolo_res := '28';
              ELSIF v_cod_motivo = '0910' THEN
                -----aggiunto il 10/01/2008
                v_cod_stato_fascicolo_res := '127';
              ELSIF v_cod_motivo = '2196' THEN
                -----aggiunto il 11/01/2008
                v_cod_stato_fascicolo_res := '55';
              ELSIF v_cod_motivo IN ('0104', '0105') THEN
                -----aggiunto il 11/01/2008
                IF v_cod_stato_procedimento = '0159' THEN
                  ---decreto sosp con istanza
                  v_cod_stato_fascicolo_res := '20';
                ELSIF v_cod_stato_procedimento = '0011' THEN
                  ---decreto sosp senza istanza
                  v_cod_stato_fascicolo_res := '19';
                END IF;
                -- PENE ACCESSORIE COMMENTATE IL 11/01/2008 CON CODICE 160
                -- RIABILITATE IL 9/12/2008 CON CREAZIONE  NUOVO STATO_FASCICOLO_RES = 162  - UMBERTO
                --                ELSIF v_cod_motivo BETWEEN '5001' AND '5129' THEN
                --                      v_cod_stato_fascicolo_res:='162';   ---PENE ACCESSORIE
                --    v_cod_stato_fascicolo_res:='160';   ---PENE ACCESSORIE 28/03/2007
                -- FINE MODIFICHE DEL 9/12/2008 - UMBERTO
              ELSIF v_cod_stato_procedimento = '0109' AND
                    v_cod_motivo IN ('0044',
                                     '0045',
                                     '0046',
                                     '0047',
                                     '0048',
                                     '0049',
                                     '0050',
                                     '0051',
                                     '0052',
                                     '0053',
                                     '0054',
                                     '0056',
                                     '0064',
                                     '0219',
                                     '0565',
                                     '0566',
                                     '0557',
                                     '0578',
                                     '0579',
                                     '0580',
                                     '0608',
                                     '1022') THEN
                ----aggiunti il 09/09/2008
                ----istruttorie senza pena non devono essere considerate solo per procura di TO
                -- Sancamillo Luglio 2009 richiesta di TESTA
                -- INIZIO
                --IF par_ufficio='00127202101' AND v_quantum_pena_compl='N' THEN ----aggiunto il 24/05/2007
                IF v_quantum_pena_compl = 'N' THEN
                  ----aggiunto il 24/05/2007
                  -- FINE
                  --                        RAISE salta_record;
                  v_cod_stato_fascicolo_res := '163';
                ELSE
                  v_cod_stato_fascicolo_res := '154';
                END IF;
                --  Umberto   24/10/2013   Recupero Pene Accessorie con attivit??????????????????????????? di Esecuzione
              ELSIF (v_cod_stato_procedimento = '0109' AND
                    v_cod_motivo BETWEEN '5001' AND '5129') THEN
                v_cod_stato_fascicolo_res := '162';
                -- Umberto   24/10/2013   Fine
              ELSIF v_cod_motivo IN ('0320',
                                     '0322',
                                     '0324',
                                     '0326',
                                     '0344',
                                     '0289',
                                     '0210',
                                     '0293',
                                     '0295',
                                     '0297',
                                     '0299',
                                     '0211',
                                     '0288',
                                     '0287',
                                     '0290',
                                     '0291',
                                     '0292',
                                     '0294',
                                     --                                      '0296','0298','0342','0352','0341','0339', COMMENTATO 24/3/2009 UMBERTO
                                     '0296',
                                     '0298',
                                     '0342',
                                     '0352',
                                     '0339',
                                     '0338',
                                     '0337',
                                     '0336',
                                     '0351',
                                     '0349',
                                     '0348',
                                     '0347',
                                     '0332',
                                     '0334',
                                     '0333',
                                     '0327',
                                     '0343',
                                     '0323',
                                     '5400') AND ---caso RICHIESTE GE
                    v_quantum = 'S' AND
                    (v_data_inizio IS NOT NULL OR v_data_fine IS NOT NULL) THEN
                --  v_cod_motivo_1 in ('0057','0058','0059','0060') AND    ---modificato il 10/05/2007
                IF TO_DATE(par_data_verifica, 'dd/mm/yyyy') >
                   TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '155';
                ELSIF TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
                      TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                              'dd/mm/yyyy') THEN
                  -----aggiunto il 11/01/2008
                  v_cod_stato_fascicolo_res := '13';
                END IF;
              ELSIF v_cod_motivo IN ('0014') AND v_quantum = 'S' AND -----aggiunto il 11/01/2008
                    (v_data_inizio IS NOT NULL OR v_data_fine IS NOT NULL) THEN
                IF TO_DATE(par_data_verifica, 'dd/mm/yyyy') >
                   TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '130';
                ELSIF TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
                      TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                              'dd/mm/yyyy') THEN
                  -----aggiunto il 11/01/2008
                  v_cod_stato_fascicolo_res := '13';
                END IF;
              ELSIF v_cod_motivo IN ('0014') AND v_quantum = 'S' AND -----aggiunto il 11/01/2008
                    (v_data_inizio IS NULL OR v_data_fine IS NULL) THEN
                v_cod_stato_fascicolo_res := '39';
              ELSIF v_cod_motivo IN ('0015') AND v_quantum = 'S' AND
                    (v_data_inizio IS NOT NULL OR v_data_fine IS NOT NULL) THEN
                IF TO_DATE(par_data_verifica, 'dd/mm/yyyy') >
                   TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '130';
                ELSIF TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
                      TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                              'dd/mm/yyyy') THEN
                  -----aggiunto il 11/01/2008
                  v_cod_stato_fascicolo_res := '13';
                END IF;
              ELSIF v_cod_motivo IN ('0015') AND v_quantum = 'S' AND -----aggiunto il 11/01/2008
                    (v_data_inizio IS NULL OR v_data_fine IS NULL) THEN
                v_cod_stato_fascicolo_res := '54';
              ELSIF v_cod_motivo IN
                    ('0160', '0159', '0174', '0175', '0158') THEN
                ----aggiunto il 27/03/2007
                IF v_data_inizio IS NOT NULL AND v_data_fine IS NOT NULL THEN
                  ----aggiunto il 27/03/2007
                  IF TO_DATE(par_data_verifica, 'dd/mm/yyyy') >=
                     TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                             'dd/mm/yyyy') THEN
                    v_cod_stato_fascicolo_res := '130';
                  ELSIF TO_DATE(par_data_verifica, 'dd/mm/yyyy') <
                        TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                                'dd/mm/yyyy') THEN
                    v_cod_stato_fascicolo_res := '13';
                  END IF;
                END IF;
              ELSIF v_cod_motivo IN ('0284') THEN
                IF v_quantum = 'N' THEN
                  ----aggiunto il 27/03/2007
                  v_cod_stato_fascicolo_res := '130';
                ELSIF v_quantum = 'S' AND v_data_inizio IS NULL AND
                      v_data_fine IS NULL THEN
                  ----aggiunto il 27/03/2007
                  v_cod_stato_fascicolo_res := '156';
                ELSIF v_quantum = 'S' AND v_data_inizio IS NOT NULL AND
                      v_data_fine IS NOT NULL THEN
                  ----aggiunto il 27/03/2007
                  IF TO_DATE(par_data_verifica, 'dd/mm/yyyy') >=
                     TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                             'dd/mm/yyyy') THEN
                    v_cod_stato_fascicolo_res := '13';
                  ELSIF TO_DATE(par_data_verifica, 'dd/mm/yyyy') <
                        TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                                'dd/mm/yyyy') THEN
                    v_cod_stato_fascicolo_res := '130';
                  END IF;
                END IF;
                -- SANCAMILLO luglio 2009 richiesta TESTA
                -- INIZIO
                --ELSIF v_cod_motivo IN ('0228') THEN  ---aggiunto il 4/5/2007
              ELSIF v_cod_motivo IN ('0228', '0271') THEN
                ---aggiunto il 4/5/2007
                -- FINE
                IF TO_DATE(par_data_verifica, 'dd/mm/yyyy') >=
                   TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '130';
                ELSIF TO_DATE(par_data_verifica, 'dd/mm/yyyy') <
                      TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                              'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '62';
                ELSIF v_data_fine IS NULL THEN
                  v_cod_stato_fascicolo_res := '7'; ----aggiunto il 21/03/2008
                END IF;
                --ELSIF v_cod_motivo IN ('0277') THEN   --  MEV42 07/2017 Aggiunti Codici Nuovo Cumulo
                --ELSIF v_cod_motivo IN ('0277', '0652', '0655', '0659', '0660') THEN
                --ELSIF v_cod_motivo IN ('0277') THEN   --  MEV70 18/04/2019 Aggiornati Codici Nuovo Cumulo assoviati a 0277
              ELSIF v_cod_motivo IN ('0665') AND v_quantum = 'S' AND
                    v_cod_posizione_giuridica in ('16', '17') THEN
                v_cod_stato_fascicolo_res := '10';
              ELSIF v_cod_motivo IN ('0277', '0652', '0655', '0665') THEN
                IF v_quantum = 'N' THEN
                  ----aggiunto il 27/03/2007
                  v_cod_stato_fascicolo_res := '130';
                ELSIF v_quantum = 'S' THEN
                  v_cod_stato_fascicolo_res := '19';
                END IF;
              ELSIF v_cod_motivo IN ('0370') AND v_quantum = 'S' AND
                    (v_data_inizio IS NOT NULL OR v_data_fine IS NOT NULL) THEN
                v_cod_stato_fascicolo_res := '47';
              ELSIF v_cod_motivo IN ('0244') AND v_quantum = 'S' AND
                    (v_data_inizio IS NOT NULL OR v_data_fine IS NOT NULL) THEN
                v_cod_stato_fascicolo_res := '33';
              ELSIF v_cod_motivo IN ('0245') AND v_quantum = 'S' AND
                    (v_data_inizio IS NOT NULL OR v_data_fine IS NOT NULL) THEN
                v_cod_stato_fascicolo_res := '40';
                --ELSIF v_cod_motivo='0224' THEN -- MEV42 07/2017 Aggiunti Codici Nuovo Cumulo
              ELSIF v_cod_motivo IN ('0224',
                                     '0644',
                                     '0646',
                                     '0647',
                                     '0648',
                                     '0649',
                                     '0650',
                                     '0651') THEN
                ------CASO DEL CUMULO IN MISURA ALTERNATIVA
                IF v_cod_posizione_giuridica = '13' THEN
                  v_cod_stato_fascicolo_res := '33';
                ELSIF v_cod_posizione_giuridica = '12' THEN
                  v_cod_stato_fascicolo_res := '55';
                ELSIF v_cod_posizione_giuridica = '25' THEN
                  v_cod_stato_fascicolo_res := '83';
                ELSIF v_cod_posizione_giuridica = '14' THEN
                  v_cod_stato_fascicolo_res := '90';
                ELSIF v_cod_posizione_giuridica = '27' THEN
                  v_cod_stato_fascicolo_res := '97';
                END IF;
              ELSIF (v_cod_motivo = '2245' AND
                    v_cod_stato_procedimento = '0080') THEN
                IF v_cod_tipo_provvedimento IN ('09', '12') THEN
                  v_cod_stato_fascicolo_res := '97';
                ELSIF v_cod_tipo_provvedimento = '06' THEN
                  v_cod_stato_fascicolo_res := '99';
                END IF;
                IF TO_DATE(par_data_verifica, 'dd/mm/yyyy') <
                   TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '97';
                ELSIF TO_DATE(par_data_verifica, 'dd/mm/yyyy') >
                      TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                              'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '130';
                END IF;
                --bvn 01/04/2016 -- PER GESTIONE RIEPILOGO ISPETTIVO PER ORDINI DI ESECUZIONE
              ELSIF (v_cod_stato_procedimento IN
                    ('0529',
                      '0531',
                      '0533',
                      '0535',
                      '0545',
                      '0546',
                      '0547',
                      '0548')) THEN
                IF v_cod_tipo_provvedimento IN ('09', '06') THEN
                  v_cod_stato_fascicolo_res := '166';
                END IF;
                IF TO_DATE(par_data_verifica, 'dd/mm/yyyy') <
                   TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '166';
                END IF;
              ELSIF nvl(v_altra_causa_pos_giud, v_cod_posizione_giuridica) IN
                    ('82', '83', '84') THEN
                v_cod_stato_fascicolo_res := '18';
              ELSIF nvl(v_altra_causa_pos_giud, v_cod_posizione_giuridica) IN
                    ('74', '75', '76', '77', '79', '81', '78', '80') THEN
                v_cod_stato_fascicolo_res := '13';
                --vedi email di testa del 11 04 2016
              ELSIF nvl(v_altra_causa_pos_giud, v_cod_posizione_giuridica) IN
                    ('62', '63', '64', '65') THEN
                v_cod_stato_fascicolo_res := '166';
                --fine bvn
              ELSIF v_cod_motivo = '0082' THEN
                ---modificato il 10/01/2007
                IF (TO_DATE(par_data_verifica, 'dd/mm/yyyy') =
                   TO_DATE(TO_CHAR(v_data_inizio, 'dd/mm/yyyy'),
                            'dd/mm/yyyy')) OR
                   (TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
                   TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                            'dd/mm/yyyy')) THEN
                  v_cod_stato_fascicolo_res := '13';
                ELSIF TO_DATE(par_data_verifica, 'dd/mm/yyyy') <
                      TO_DATE(TO_CHAR(v_data_inizio, 'dd/mm/yyyy'),
                              'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '14';
                ELSIF TO_DATE(par_data_verifica, 'dd/mm/yyyy') >
                      TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                              'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '130';
                ELSIF v_data_fine IS NULL THEN
                  v_cod_stato_fascicolo_res := '130'; ---aggiunto il 21/03/2008
                END IF;
              ELSIF v_cod_motivo = '0059' OR v_cod_motivo = '0132' THEN
                IF (TO_DATE(par_data_verifica, 'dd/mm/yyyy') =
                   TO_DATE(TO_CHAR(v_data_inizio, 'dd/mm/yyyy'),
                            'dd/mm/yyyy')) OR
                   (TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
                   TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                            'dd/mm/yyyy')) THEN
                  v_cod_stato_fascicolo_res := '8';
                ELSIF TO_DATE(par_data_verifica, 'dd/mm/yyyy') <
                      TO_DATE(TO_CHAR(v_data_inizio, 'dd/mm/yyyy'),
                              'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '14';
                ELSIF TO_DATE(par_data_verifica, 'dd/mm/yyyy') >
                      TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                              'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '130';
                ELSIF v_data_fine IS NULL AND v_data_inizio IS NULL THEN
                  v_cod_stato_fascicolo_res := '8';
                END IF;
              ELSIF v_cod_motivo IN
                    ('0083', '0081', '0121', '0212', '0213', '5491', '5492') OR
                    v_cod_motivo BETWEEN '0913' AND '0919' THEN
                ----COMPUTO E LA
                IF TO_DATE(par_data_verifica, 'dd/mm/yyyy') >
                   TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy') THEN
                  v_cod_stato_fascicolo_res := '130';
                ELSIF TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
                      TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'),
                              'dd/mm/yyyy') THEN
                  IF v_cod_posizione_giuridica IN ('03', '31') THEN
                    v_cod_stato_fascicolo_res := '13';
                  ELSIF v_cod_posizione_giuridica = '11' THEN
                    v_cod_stato_fascicolo_res := '111';
                  ELSIF v_cod_posizione_giuridica = '12' THEN
                    v_cod_stato_fascicolo_res := '55';
                  ELSIF v_cod_posizione_giuridica = '13' THEN
                    v_cod_stato_fascicolo_res := '40';
                  ELSIF v_cod_posizione_giuridica = '14' THEN
                    v_cod_stato_fascicolo_res := '90';
                  ELSIF v_cod_posizione_giuridica = '15' THEN
                    v_cod_stato_fascicolo_res := '112';
                  ELSIF v_cod_posizione_giuridica = '19' THEN
                    v_cod_stato_fascicolo_res := '113';
                  ELSIF v_cod_posizione_giuridica = '25' THEN
                    v_cod_stato_fascicolo_res := '83';
                  ELSIF v_cod_posizione_giuridica = '27' THEN
                    v_cod_stato_fascicolo_res := '97';
                  ELSIF v_cod_posizione_giuridica = '29' THEN
                    v_cod_stato_fascicolo_res := '133';
                  ELSIF v_cod_posizione_giuridica = '31' THEN
                    v_cod_stato_fascicolo_res := '58';
                  ELSIF v_cod_posizione_giuridica = '32' THEN
                    v_cod_stato_fascicolo_res := '36';
                  ELSIF v_cod_posizione_giuridica = '33' THEN
                    v_cod_stato_fascicolo_res := '93';
                  ELSIF v_cod_posizione_giuridica = '34' THEN
                    v_cod_stato_fascicolo_res := '86';
                  ELSIF v_cod_posizione_giuridica = '35' THEN
                    v_cod_stato_fascicolo_res := '100';
                  END IF;
                ELSIF v_quantum = 'S' AND v_data_inizio IS NULL AND
                      v_data_fine IS NULL THEN
                  ----aggiunto il 2/04/2007
                  v_cod_stato_fascicolo_res := '7';
                ELSIF v_quantum = 'N' AND v_data_inizio IS NULL AND
                      v_data_fine IS NULL THEN
                  ----aggiunto il 20/03/2008
                  v_cod_stato_fascicolo_res := '130';
                END IF;
                -- Sancamillo luglio 2009 richiesta di TESTA
                -- INIZIO
                --ELSIF v_cod_motivo ='0908' THEN
              ELSIF v_cod_motivo IN ('0908', '2140') OR
                    (v_cod_motivo IN ('2141') AND
                    v_cod_tipo_provvedimento = '27') THEN
                -- FINE
                v_cod_stato_fascicolo_res := '157';
              ELSIF v_cod_motivo = '0043' THEN
                v_cod_stato_fascicolo_res := '152';
              ELSIF v_cod_motivo = '0367' THEN
                v_cod_stato_fascicolo_res := '158';
              ELSIF v_cod_motivo_1 = '2010' AND
                    (v_cod_motivo = '0221' OR v_cod_motivo = '0274') THEN
                ---DIFFERIMENTO
                v_cod_stato_fascicolo_res := '117';
              ELSIF v_cod_motivo_1 = '2011' AND
                    (v_cod_motivo = '0221' OR v_cod_motivo = '0274') THEN
                v_cod_stato_fascicolo_res := '116';
              ELSE
                -- IF v_cod_motivo ='0122' THEN
                --    v_cod_motivo:=v_cod_motivo_1;
                -- END IF;
                DBMS_OUTPUT.PUT_LINE('[Diego] stato non ancora assegnato ricerco su STATO_ESECUZIONE_PROCEDIMENTO per STATO,MOTIVO e PG[' ||
                                     v_cod_stato_procedimento || '-' ||
                                     v_cod_motivo || '-' ||
                                     v_cod_posizione_giuridica || ']... ');
                DBMS_OUTPUT.PUT_LINE('Ricerca Stato Fascicolo RES ' ||
                                     v_cod_stato_fascicolo_res);
                ----RICERCA STATO FASCICOLO RES
                BEGIN
                  SELECT COD_STATO_FASCICOLO_RES
                    INTO v_cod_stato_fascicolo_res
                    FROM STATO_ESECUZIONE_PROCEDIMENTO
                   WHERE (cod_stato_procedimento = v_cod_stato_procedimento)
                        -- Diego 07/07/2014 aggiunta gestione posizione giuridica su tabella STATO_ESECUZIONE_PROCEDIMENTO
                     AND (COD_POSIZIONE_GIURIDICA =
                         v_cod_posizione_giuridica OR
                         COD_POSIZIONE_GIURIDICA IS NULL)
                     AND cod_motivo = v_cod_motivo
                     AND ROWNUM = 1;

                  DBMS_OUTPUT.PUT_LINE('[Diego] trovato1 ;-) ' ||
                                       v_cod_stato_fascicolo_res);
                EXCEPTION
                  -- Ticket 20211220019_Riepilogo ispettivo
                  -- SI aggiunge ulteriore test per TIPO E MOTIVO
                  WHEN NO_DATA_FOUND THEN
                    BEGIN
                      DBMS_OUTPUT.PUT_LINE('[Diego] Non trovato per v_cod_stato_procedimento, v_cod_motivo e v_cod_posizione_giuridica ');
                      DBMS_OUTPUT.PUT_LINE('[Diego] Provo per v_cod_tipo_provvedimento (' ||
                                           v_cod_tipo_provvedimento ||
                                           '), v_cod_motivo (' ||
                                           v_cod_motivo ||
                                           ') e v_cod_posizione_giuridica (' ||
                                           v_cod_posizione_giuridica || ')');
                      SELECT COD_STATO_FASCICOLO_RES
                        INTO v_cod_stato_fascicolo_res
                        FROM STATO_ESECUZIONE_PROCEDIMENTO
                       WHERE COD_TIPO_PROVVEDIMENTO =
                             v_cod_tipo_provvedimento
                         AND cod_motivo = v_cod_motivo
                         AND (COD_POSIZIONE_GIURIDICA =
                             v_cod_posizione_giuridica OR
                             COD_POSIZIONE_GIURIDICA IS NULL)
                         AND ROWNUM = 1;

                      DBMS_OUTPUT.PUT_LINE('[Diego] trovato2 ;-) ' ||
                                           v_cod_stato_fascicolo_res);
                    EXCEPTION
                      --==============================================================================
                      WHEN NO_DATA_FOUND THEN
                        BEGIN
                          DBMS_OUTPUT.PUT_LINE('[Diego] non trovato su STATO_ESECUZIONE_PROCEDIMENTO, provo solo v_cod_motivo... ');
                          SELECT COD_STATO_FASCICOLO_RES
                            INTO v_cod_stato_fascicolo_res
                            FROM STATO_ESECUZIONE_PROCEDIMENTO
                           WHERE cod_motivo = v_cod_motivo
                             AND ROWNUM = 1;
                          DBMS_OUTPUT.PUT_LINE('[Diego] trovato3 ;-) ' ||
                                               v_cod_stato_fascicolo_res);
                        EXCEPTION
                          WHEN NO_DATA_FOUND THEN
                            DBMS_OUTPUT.PUT_LINE('[Diego] niente da fare su STATO_ESECUZIONE_PROCEDIMENTO. 129 Altre posizioni SCARTI. ');
                            v_cod_stato_fascicolo_res := '129'; ----modificato il 07/12/2006
                            INSERT INTO STATIS.ISP_SCARTI
                              (ID_FASCICOLO_SIEP,
                               NRES,
                               CHIAVE_ANNO,
                               CHIAVE_PROGR,
                               ULT_TIPO_PROVVEDIMENTO,
                               ULT_COD_MOTIVO,
                               PEN_TIPO_PROVVEDIMENTO,
                               PEN_COD_MOTIVO,
                               COD_UFFICIO,
                               COD_POSIZIONE_GIURIDICA,
                               COD_STATO_PROCEDIMENTO,
                               -- Umberto   8/10/2013                                               COD_STATO_FASCICOLO_RES)
                               COD_STATO_FASCICOLO_RES,
                               -- Umberto   8/10/2013  inserita valorizzazione colonne aggiuntive
                               COD_UFFICIO_INSERIMENTO,
                               CHIAVE_PROGR_ORIG,
                               DESC_UFFICIO_INSERIMENTO)
                            -- Umberto  8/10/2013   Fine modifiche
                            VALUES
                              (v_id_fascicolo_siep,
                               v_af8_nres,
                               v_chiave_anno,
                               v_chiave_progr,
                               v_cod_tipo_provvedimento,
                               v_cod_motivo,
                               v_cod_tipo_provvedimento_1,
                               v_cod_motivo_1,
                               par_ufficio,
                               v_cod_posizione_giuridica,
                               v_cod_stato_procedimento,
                               -- Umberto 8/10/2013                                               v_cod_stato_fascicolo_res);
                               v_cod_stato_fascicolo_res,
                               -- Umberto  8/10/2013  inserite nuove variabili
                               v_cod_ufficio_inserimento,
                               v_chiave_progr_orig,
                               v_desc_ufficio_inserimento);
                            -- Umberto 8/10//2013  Fine modifiche
                        END;
                        --==============================================================================
                    END; -- Ticket 20211220019_Riepilogo ispettivo
                  --==============================================================================
                END;
              END IF;
            END IF;

            DBMS_OUTPUT.PUT_LINE('[Diego] Procedimento classificato: ' ||
                                 v_cod_stato_fascicolo_res);
            DBMS_OUTPUT.PUT_LINE('[Diego] Procedimento ad eventuale riclassificazione...');
            ---- CASO ERGASTOLO aggiunto il 10/01/2008
            IF v_flag_ergastolo IN ('S', 'D', 'O') AND
               v_fasc_archiviato = 'N' THEN
              ----aggiunto il 10/09/2008
              v_cod_stato_fascicolo_res := '13';
            END IF;

            -- Umberto 23/10/2013 Se data fine pena > data di sistema  e espiazione in reg. carcerario - In corso espiazione
            IF (TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
               TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy')) AND
               v_cod_posizione_giuridica IN ('01', '03') THEN
              v_cod_stato_fascicolo_res := '13';
            END IF;

            -- MEV_9-SIEP si evita la riclassificazione in base alla PG per il fasicolo gi?? classificati nella Ammissioni Provvisorie
            -- aggiunto IF NOT IN
            IF v_cod_stato_fascicolo_res NOT IN
               ('225',
                '226',
                '227',
                '228',
                '229',
                '230',
                '231',
                '232',
                '233',
                '234') THEN
              -- MEV70 - Umberto 23/04/2019   Aggiunte ulteriori pos. giuridice alle preesistenti (13,14,12, 50)
              IF (TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
                 TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy')) AND
                 v_cod_posizione_giuridica IN ('13', '32', '54') THEN
                v_cod_stato_fascicolo_res := '33';
              END IF;
              IF (TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
                 TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy')) AND
                 v_cod_posizione_giuridica IN ('14', '33', '38') THEN
                v_cod_stato_fascicolo_res := '90';
              END IF;
              IF (TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
                 TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy')) AND
                 v_cod_posizione_giuridica IN ('12', '25', '29', '31') THEN
                v_cod_stato_fascicolo_res := '62';
              END IF;
            END IF; -- MEV_9-SIEP - FINE IF

            IF (TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
               TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy')) AND
               v_cod_posizione_giuridica IN ('11') THEN
              v_cod_stato_fascicolo_res := '111';
            END IF;
            IF (TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
               TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy')) AND
               v_cod_posizione_giuridica IN ('50') THEN
              v_cod_stato_fascicolo_res := '164';
            END IF;
            -- MEV70 - Umberto 23/04/2019   Associazione posizioni giuridiche 04,82,83 a 'In corso di espiazione Esecuzione agli Arresti Domiciliari.....'
            IF (TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
               TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy')) AND
               v_cod_posizione_giuridica IN ('04', '82', '83') THEN
              v_cod_stato_fascicolo_res := '180';
            END IF;
            -- MEV70 - Umberto 23/04/2019   Fine

            DBMS_OUTPUT.PUT_LINE('CONTROLLO v_data_fine  -   v_cod_stato_fascicolo_res' ||
                                 v_data_fine || '-' ||
                                 v_cod_stato_fascicolo_res);
            -- Umberto 23/190/2013 Fine

            --- RICOMMENTATA IL 08/02/2008
            --- CASO RESIDUO PENA NULLO ma non archiviato indipendentemente da stato procedimento e posizione giuridica
            --- quindi ultimo eventoemesso aggiunto il 11/01/2008
            -- IF v_quantum = 'N' AND v_cod_stato_fascicolo NOT IN ( '01','06','02') THEN
            --    v_cod_stato_fascicolo_res:='130';
            -- END IF;

            --- aggiunto il 03/04/2007
            DBMS_OUTPUT.PUT_LINE('CONTROLLO v_cod_stato_fascicolo_res PRIMA ' ||
                                 v_cod_stato_fascicolo_res);
            -- d.f. impossibile
            IF v_cod_stato_fascicolo_res IS NULL THEN
              v_cod_stato_fascicolo_res := '129';
            END IF;

            -- Sancamillo luglio 2009 chiesto da Testa
            -- Inizio

            DBMS_OUTPUT.PUT_LINE('CONTROLLO Date Inizio e Fine pena:  v_data_inizio = >' ||
                                 v_data_inizio || '<,  v_data_fine = >' ||
                                 v_data_fine ||
                                 '<, v_cod_stato_fascicolo_res = >' ||
                                 v_cod_stato_fascicolo_res || '<');
            IF v_data_inizio IS NOT NULL AND v_data_fine IS NOT NULL AND
               v_data_fine < TRUNC(SYSDATE) THEN
              v_cod_stato_fascicolo_res := '130'; -- Attesa definizione
            END IF;
            -- Fine

            -- Sancamillo luglio 2009 chiesto da Testa
            -- Inizio

            DBMS_OUTPUT.PUT_LINE('CONTROLLO  2 Date Inizio e Fine pena ' ||
                                 v_data_fine);
            IF v_cod_stato_fascicolo_res IN
               ('96',
                '54',
                '103',
                '108',
                '110',
                '39',
                '61',
                '46',
                '82',
                '68',
                '89',
                '75') AND v_data_inizio IS NOT NULL AND
               v_data_fine IS NOT NULL AND v_data_fine > TRUNC(SYSDATE) THEN
              DBMS_OUTPUT.PUT_LINE('cONTROLLO  2 Date Inizio e Fine pena ' ||
                                   v_data_fine);
              v_cod_stato_fascicolo_res := '13'; -- In Corso di Espiazione in Regime Carcerario
            END IF;
            -- Fine

            DBMS_OUTPUT.PUT_LINE('v_cod_stato_procedimento ' ||
                                 v_cod_stato_procedimento);
            DBMS_OUTPUT.PUT_LINE('v_cod_stato_fascicolo_res ' ||
                                 v_cod_stato_fascicolo_res);
            DBMS_OUTPUT.PUT_LINE('v_cod_posizione_giuridica ' ||
                                 v_cod_posizione_giuridica);

            -- Umberto 25/10/2013  Recupero procedimenti che finirebbero in Altre posizioni (129) in specifiche categorie
            IF v_cod_stato_fascicolo_res = '129' AND
               v_cod_posizione_giuridica IN ('53', '04', '02') THEN
              v_cod_stato_fascicolo_res := '18'; --Esecuzione Ordini di Carcerazione per Condannati agli Arresti Domiciliari - Prosecuzione
            END IF;

            IF v_cod_stato_fascicolo_res = '129' AND
               v_cod_posizione_giuridica IN ('49') THEN
              v_cod_stato_fascicolo_res := '166'; -- Emesso Decreto MdS Sospensione Provvisoria Arresti Domiciliari
            END IF;

            IF v_cod_stato_fascicolo_res = '129' AND
               v_cod_tipo_provvedimento IN ('-', '05', '26') THEN
              v_cod_stato_fascicolo_res := '154'; -- Istruttoria
            END IF;
            -- MEV70 - Umberto 23/04/2019   Aggiunte ulteriori pos. giuridice alle preesistenti
            IF v_cod_stato_fascicolo_res = '129' AND
               v_cod_posizione_giuridica IN ('01', '03') THEN
              v_cod_stato_fascicolo_res := '13';
            END IF;
            IF v_cod_stato_fascicolo_res = '129' AND
               v_cod_posizione_giuridica IN ('13', '32', '54') THEN
              v_cod_stato_fascicolo_res := '33';
            END IF;
            IF v_cod_stato_fascicolo_res = '129' AND
               v_cod_posizione_giuridica IN ('14', '33', '38') THEN
              v_cod_stato_fascicolo_res := '90';
            END IF;
            -- MEV_92_2024   catalogazione Ammissione provvisoria Semilibert?
            IF v_cod_stato_fascicolo_res = '90' AND v_cod_motivo = '2007' THEN
              v_cod_stato_fascicolo_res := '236';
            END IF;
            -- MEV_92_2024   catalogazione Ammissione provvisoria Semilibert? - FINE
            IF v_cod_stato_fascicolo_res = '129' AND
               v_cod_posizione_giuridica IN ('12', '25', '29', '31') THEN
              v_cod_stato_fascicolo_res := '62';
            END IF;
            IF v_cod_stato_fascicolo_res = '129' AND
               v_cod_posizione_giuridica IN ('11') THEN
              v_cod_stato_fascicolo_res := '111';
            END IF;
            IF v_cod_stato_fascicolo_res = '129' AND
               v_cod_posizione_giuridica IN ('50') THEN
              v_cod_stato_fascicolo_res := '164';
            END IF;
            -- MEV70 - Umberto 23/04/2019   Associazione posizioni giuridiche 04,82,83 a 'In corso di espiazione Esecuzione agli Arresti Domiciliari.....'
            IF (TO_DATE(par_data_verifica, 'dd/mm/yyyy') <=
               TO_DATE(TO_CHAR(v_data_fine, 'dd/mm/yyyy'), 'dd/mm/yyyy')) AND
               v_cod_posizione_giuridica IN ('04', '82', '83') THEN
              v_cod_stato_fascicolo_res := '180';
            END IF;
            -- MEV70 - Umberto 23/04/2019   Fine

            -- MEV42 07/2017  Recupero procedimenti che finirebbero in Istruttoria (154)
            DBMS_OUTPUT.PUT_LINE('HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH PRIMA DI INSERT = ' ||
                                 v_chiave_anno || '-' || v_chiave_progr);

            IF v_cod_stato_fascicolo_res = '154' THEN

              DBMS_OUTPUT.PUT_LINE('zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz STRART= ' ||
                                   v_chiave_anno || '-' || v_chiave_progr);

              BEGIN
                SELECT Id_Istruttoria_Cumulo
                  INTO v_Id_Istruttoria_Cumulo
                  FROM ISTRUTTORIA_CUMULO
                 WHERE Fas_Sie_Id_Fascicolo_Siep = v_id_fascicolo_siep
                   AND Flag_Stato = 'A'
                   AND ROWNUM = 1;

                v_cod_stato_fascicolo_res := '3';
                DBMS_OUTPUT.PUT_LINE('Istruttoria in Stato A - v_Id_Istruttoria_Cumulo= ' ||
                                     v_Id_Istruttoria_Cumulo);

              EXCEPTION
                WHEN NO_DATA_FOUND THEN
                  v_cod_stato_fascicolo_res := '154';
                  DBMS_OUTPUT.PUT_LINE('NON Trovato - v_cod_stato_fascicolo_res = ' ||
                                       v_cod_stato_fascicolo_res);
              END;

              DBMS_OUTPUT.PUT_LINE('zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz END = ' ||
                                   v_chiave_anno || '-' || v_chiave_progr);
            END IF;

            DBMS_OUTPUT.PUT_LINE('[Diego] ...dopo riclassificazione: -------------->  ' ||
                                 v_cod_stato_fascicolo_res);
            BEGIN
              IF v_cod_stato_fascicolo_res IS NOT NULL THEN
                DBMS_OUTPUT.PUT_LINE('faccio insert ISP_PROVVEDIMENTI');
                BEGIN
                  INSERT INTO STATIS.ISP_PROVVEDIMENTI
                    (ID_FASCICOLO_SIEP,
                     NRES,
                     CHIAVE_ANNO,
                     CHIAVE_PROGR,
                     ULT_TIPO_PROVVEDIMENTO,
                     ULT_COD_MOTIVO,
                     PEN_TIPO_PROVVEDIMENTO,
                     PEN_COD_MOTIVO,
                     COD_UFFICIO,
                     COD_POSIZIONE_GIURIDICA,
                     COD_STATO_PROCEDIMENTO,
                     -- Umberto   8/10/2013                                               COD_STATO_FASCICOLO_RES)
                     COD_STATO_FASCICOLO_RES,
                     -- Umberto   8/10/2013  inserita valorizzazione colonne aggiuntive
                     COD_UFFICIO_INSERIMENTO,
                     CHIAVE_PROGR_ORIG,
                     DESC_UFFICIO_INSERIMENTO)
                  -- Umberto  8/10/2013   Fine modifiche
                  VALUES
                    (v_id_fascicolo_siep,
                     v_af8_nres,
                     v_chiave_anno,
                     v_chiave_progr,
                     v_cod_tipo_provvedimento,
                     v_cod_motivo,
                     v_cod_tipo_provvedimento_1,
                     v_cod_motivo_1,
                     par_ufficio,
                     v_cod_posizione_giuridica,
                     v_cod_stato_procedimento,
                     -- Umberto 8/10/2013                                               v_cod_stato_fascicolo_res);
                     v_cod_stato_fascicolo_res,
                     -- Umberto  8/10/2013  inserite nuove variabili
                     v_cod_ufficio_inserimento,
                     v_chiave_progr_orig,
                     v_desc_ufficio_inserimento);
                  -- Umberto 8/10//2013  Fine modifiche
                  RAISE close_cur;
                EXCEPTION
                  WHEN DUP_VAL_ON_INDEX THEN
                    --aggiunto il 3/12/2007
                    RAISE close_cur;
                END;
                COMMIT; ---aggiunto il 05/05/2008
              END IF;

              --dbms_output.put_line ('faccio insert ISP_STAT_RIS_SIES');

              -- INSERT
              --   INTO ISP_STAT_RIS_SIES (CHIAVE_ANNO,
              --                           CHIAVE_PROGR,
              --                           COD_UFFICIO,
              --                           DATA_ESTRAZIONE,
              --                           COD_POSIZIONE_GIURIDICA,
              --                           COD_STATO_PROCEDIMENTO,
              --                           FAS_SIE_ID_FASCICOLO_SIEP,
              --                           COD_OPERATORE_INSERIMENTO,
              --                           COD_UFFICIO_INSERIMENTO,
              --                           DATA_INSERIMENTO)
              --                   VALUES (v_chiave_anno,
              --                           v_chiave_progr,
              --                           par_ufficio,
              --                           sysdate,
              --                           v_cod_posizione_giuridica,
              --                           v_cod_stato_procedimento,
              --                           v_id_fascicolo_siep,
              --                          'STAT',
              --                           par_ufficio,
              --                           sysdate);
            END;
            COMMIT;

          EXCEPTION
            WHEN salta_record THEN
              v_uscita := 'N'; --null ;
              DBMS_OUTPUT.PUT_LINE('salto record ' || v_chiave_anno || '/' ||
                                   v_chiave_progr || ' ' ||
                                   v_cod_tipo_provvedimento || ' ' ||
                                   v_cod_motivo);
            WHEN close_cur THEN
              DBMS_OUTPUT.PUT_LINE('forzo chiusura cursore ');
              EXIT;
          END; ---- fine Begin loop evento

          -- LETTURA IN CICLO EVENTO
          FETCH cur_evento
            INTO v_cod_tipo_evento,
                 v_cod_tipo_provvedimento,
                 v_cod_motivo,
                 v_data_inserimento;

        END LOOP; ----fine loop evento  -- CHIUSURA DEL WHILE

        CLOSE cur_evento;

      END; ---- fine Begin loop fascicolo

      DBMS_OUTPUT.PUT_LINE('fetch cur1 alla fine ');

      -- LETTURA IN CICLO FASCICOLO
      <<skip_fascicolo>>
      FETCH cur1
        INTO v_id_fascicolo_siep,
             v_chiave_anno,
             v_chiave_progr,
             -- Umberto 8/10/2013  inserita estrazione colonne aggiuntive
             v_cod_ufficio_inserimento,
             v_chiave_progr_orig,
             -- Umberto 8/10/2013  Fine modifiche
             v_af8_nres,
             v_cod_stato_fascicolo;

      -- Umberto 8/10/2013  inserita routine recupero descrizione ufficio inserimento
      ----RICERCA LUOGO UFFICIO INSERIMENTO
      BEGIN
        SELECT descrizione
          INTO v_desc_ufficio_inserimento
          FROM COMUNE
         WHERE cod_comune = SUBSTR(v_cod_ufficio_inserimento, 1, 6);
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_desc_ufficio_inserimento := '-';
      END;
      -- Umberto 8/10/2013   Fine modifiche

    -- CHIUSURA DEL WHILE
    END LOOP;

    CLOSE cur1;

    COMMIT;

  END; --- fine procedura carica provvedimenti
  -----------------------------------------------------------------------------------------------------------------------
  ------------------------------------PROCEDURA TEMPI ISCRIZIONE-------------------------------------------------------
  -----------------------------------------------------------------------------------------------------------------------

  PROCEDURE tempi_iscrizione(par_data_inizio IN VARCHAR2,
                             par_data_fine   IN VARCHAR2,
                             par_ufficio     IN VARCHAR2) IS
    --- Umberto 26/9/2013          aggiunti 3 parametri ufficio per NGG, disabiliati nuovamente in data 13/10/2013
    ---                            par_ufficio      IN  VARCHAR2, par_ufficio1      IN  VARCHAR2, par_ufficio2      IN  VARCHAR2, par_ufficio3     IN  VARCHAR2) IS

    --DEFINIZIONE VARIABILI
    --v_note1 VARCHAR2(2000);

    --v_tipo_richiesta    CAMPO_NOTA.oggetto_nota_res%TYPE;
    v_id_fascicolo_siep FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;
    --v_anno_af8_nrgem    EVENTO.anno_protocollo%TYPE;
    --v_progr_af8_nrgem   EVENTO.progr_protocollo%TYPE;
    v_chiave_anno  FASCICOLO_SIEP.chiave_anno%TYPE;
    v_chiave_progr FASCICOLO_SIEP.chiave_progr%TYPE;
    --- Umberto 26/9/2013          aggiunti 2 variabili per NGG
    v_cod_ufficio_inserimento FASCICOLO_SIEP.cod_ufficio_inserimento%TYPE;
    v_chiave_progr_orig       FASCICOLO_SIEP.chiave_progr_orig%TYPE;
    -- Umberto 26/9/2013  Fine modifiche
    --v_cod_posizione_giuridica POSIZIONE_GIURIDICA.cod_posizione_giuridica%TYPE;
    --v_con_rev                 VARCHAR2(30);
    --v_tip_aut                 VARCHAR2(70);
    --v_tip_att                 VARCHAR2(80);
    --v_motiv                   VARCHAR2(50);
    v_af8_nres FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;
    --conta                     NUMBER;

    v_cod_stato_procedimento STATO_PROCEDIMENTO.cod_stato_procedimento%TYPE;
    --flag_no_riepilogo         CHAR(1);
    --tot_recl                  NUMBER;
    --tot_arre                  NUMBER;
    --v_num_anni_reclusione     NUMBER(4);
    --v_num_mesi_reclusione     NUMBER(4);
    --v_num_giorni_reclusione   NUMBER(4);
    --v_importo_multa           NUMBER(11, 2);
    --v_num_anni_arresto        NUMBER(4);
    --v_num_mesi_arresto        NUMBER(4);
    --v_num_giorni_arresto      NUMBER(4);
    --v_importo_ammenda         NUMBER(11, 2);
    --gg_tot                    NUMBER;
    --tot_pres                  NUMBER;
    --v_data_inizio             PENA_RESIDUA.data_inizio%TYPE;
    --v_data_fine               PENA_RESIDUA.data_fine%TYPE;
    --v_id_fascicolo_siep_old   FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;
    --v_id_evento_old           FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;
    --v_cod_stato_fascicolo_res STATO_FASCICOLO_RES.cod_stato_fascicolo%TYPE := '';

    -- variabile generali
    --conta_commit       NUMBER;
    --v_trovato          NUMBER;
    --num_record_scritti NUMBER := 0;
    num_record_letti NUMBER := 0;
    --num_record_scartati         NUMBER:= 0;
    --v_anno_res                NUMBER(4) := NULL;
    --v_num_res                 VARCHAR2(6) := '';
    v_data_iscrizione         DATE;
    v_data_irrevocabilita     DATE;
    v_data_arrivo_atto        DATE;
    v_ricezione_iscrizione    NUMBER := 0;
    v_giudicato_iscrizione    NUMBER := 0;
    v_giudicato_ricezione     NUMBER := 0;
    v_data_ordinanza          DATE;
    v_cod_tipo_autorita       SENTENZA.cod_tipo_autorita_emittente%TYPE;
    v_cod_luogo_emittente     SENTENZA.cod_luogo_emittente%TYPE;
    v_sez_autorita_emittente  SENTENZA.num_sezione_autorita_emittente%TYPE;
    v_desc_autorita_emittente VARCHAR2(200);
    v_desc_luogo_emittente    VARCHAR2(200);
    --- Umberto 26/9/2013          aggiunti 1 variabile per NGG
    v_desc_ufficio_inserimento VARCHAR2(200);
    -- Umberto  26/9/2013  Fine modifiche

    -- dichiarazione del cursore
    CURSOR cur_iscrizione IS
      SELECT id_fascicolo_siep,
             chiave_anno,
             chiave_progr,
             a.fas_sie_id_fascicolo_siep nres,
             a.data_iscrizione,
             a.data_irrevocabilita,
             a.data_arrivo_atto,
             -- Umberto 26/9/2013  inserita estrazione colonne aggiuntive
             a.cod_ufficio_inserimento,
             a.chiave_progr_orig,
             -- Umberto  26/9/2013  Fine modifiche
             b.cod_tipo_autorita_emittente,
             b.cod_luogo_emittente,
             b.num_sezione_autorita_emittente
        FROM FASCICOLO_SIEP a, SENTENZA b
       WHERE a.sen_id_sentenza = b.id_sentenza
         AND TO_DATE(TO_CHAR(a.data_iscrizione, 'dd/mm/yyyy'), 'dd/mm/yyyy') BETWEEN
             TO_DATE(par_data_inizio, 'dd/mm/yyyy') AND
             TO_DATE(par_data_fine, 'dd/mm/yyyy')
         AND a.cod_ufficio_inserimento = par_ufficio
            -- Umberto 26/9/2013 inserita nuova condizione per NGG, disabilitata in data 13/10/2013
            --         AND ( a.cod_ufficio_inserimento=par_ufficio OR a.cod_ufficio_inserimento=par_ufficio1 OR a.cod_ufficio_inserimento=par_ufficio2 OR a.cod_ufficio_inserimento=par_ufficio3 )
            -- Umberto 26/9/2013  Fine modifiche
            -- Umberto 26/9/2013        AND a.cod_ufficio_inserimento=a.chiave_ufficio  ----aggiunto il 09/01/2008
            -- Umberto 26/9/2013        AND a.cod_ufficio_inserimento=b.cod_ufficio_inserimento
            -- Ticket#20231017016 - STATISTICHE UFFICIO - TEMPI ISCRIZIONI PROCEDIMENTI
            -- Vanno estratti SOLO i classe I
         AND (a.chiave_progr BETWEEN 1 AND 19999 OR
             a.chiave_progr BETWEEN 10000000000001 AND 10000000019999 OR
             a.chiave_progr BETWEEN 20000000000001 AND 20000000019999 OR
             a.chiave_progr BETWEEN 30000000000001 AND 30000000019999 OR
             a.chiave_progr BETWEEN 800000 AND 819999 -- Pretura
             OR a.chiave_progr BETWEEN 900001000000 AND 919999000000 -- Pretura/RES con lettere classe I
             OR a.chiave_progr BETWEEN 1700001000000 AND 1719999000000 -- Pretura/RES con lettere classe I
             )
      /*
           AND a.chiave_progr NOT BETWEEN 930000000000 AND 939999999999 ---escludo le pene sospese
           AND a.chiave_progr NOT BETWEEN 30000 AND 39999
              -- Umberto 26/9/2013       esclusione pene sospese uffici accorpati
           AND a.chiave_progr NOT BETWEEN 10000000030000 AND 10000000039999
           AND a.chiave_progr NOT BETWEEN 20000000030000 AND 20000000039999
           AND a.chiave_progr NOT BETWEEN 30000000030000 AND 30000000039999
              -- Umberto 26/9/2013  Fine modifiche
              -- Umberto 24/03/2016  Esclusione Procedimenti di Misure Sicurezza
           AND a.chiave_progr NOT BETWEEN 40000 AND 49999
           AND a.chiave_progr NOT BETWEEN 10000000040000 AND 10000000049999
           AND a.chiave_progr NOT BETWEEN 20000000040000 AND 20000000049999
           AND a.chiave_progr NOT BETWEEN 30000000040000 AND 30000000049999
              -- Umberto 24/03/2016  Fine
              -- Umberto 24/03/2016  Esclusione Procedimenti di Conversione Pene Pecuniarie
           AND a.chiave_progr NOT BETWEEN 70000 AND 79999
           AND a.chiave_progr NOT BETWEEN 10000000070000 AND 10000000079999
           AND a.chiave_progr NOT BETWEEN 20000000070000 AND 20000000079999
           AND a.chiave_progr NOT BETWEEN 30000000070000 AND 30000000079999
              -- Umberto 24/03/2016  Fine
           AND a.chiave_progr NOT BETWEEN 700000 AND 799999 ---escludo cumulo aggiunto il 09/01/2008
              -- Umberto 26/9/2013       esclusione fascicoli cinvolti in cumulo uffici accorpati
           AND a.chiave_progr NOT BETWEEN 10000000700000 AND 10000000799999
           AND a.chiave_progr NOT BETWEEN 20000000700000 AND 20000000799999
           AND a.chiave_progr NOT BETWEEN 30000000700000 AND 30000000799999
              -- Umberto 26/9/2013  Fine modifiche
              -- Umberto 18/12/2013       esclusione fascicoli ISTANZE
           AND a.chiave_progr NOT BETWEEN 90000 AND 99999
           AND a.chiave_progr NOT BETWEEN 10000000090000 AND 10000000099999
           AND a.chiave_progr NOT BETWEEN 20000000090000 AND 20000000099999
           AND a.chiave_progr NOT BETWEEN 30000000090000 AND 30000000099999
              -- Umberto 18/12/2013  Fine modifiche
              -- Umberto 21/01/2014     esclusione fascicoli CONVERSIONE PENE PECUNIARIE
           AND a.chiave_progr NOT BETWEEN 70000 AND 79999
           AND a.chiave_progr NOT BETWEEN 10000000070000 AND 10000000079999
           AND a.chiave_progr NOT BETWEEN 20000000070000 AND 20000000079999
           AND a.chiave_progr NOT BETWEEN 30000000070000 AND 30000000079999
        -- Umberto  21/01/2014  Fine modifiche
      */
      -- Ticket#20231017016 - FINE
       ORDER BY id_fascicolo_siep;

    CURSOR cur_ricezione IS
      SELECT id_fascicolo_siep,
             chiave_anno,
             chiave_progr,
             a.fas_sie_id_fascicolo_siep nres,
             a.data_irrevocabilita,
             a.data_arrivo_atto,
             -- Umberto inserito estrazione colonne aggiuntive  26/9/2013
             a.cod_ufficio_inserimento,
             a.chiave_progr_orig,
             -- Umberto Fine modifiche
             b.cod_tipo_autorita_emittente,
             b.cod_luogo_emittente,
             b.num_sezione_autorita_emittente
        FROM FASCICOLO_SIEP a, SENTENZA b
       WHERE a.sen_id_sentenza = b.id_sentenza
            --  Sancamillo : commentato per problemi su napoli 10.06.2009 (con Diego)
            --  AND TO_DATE(TO_CHAR(b.data_arrivo_atto,'dd/mm/yyyy'),'dd/mm/yyyy') BETWEEN
         AND TO_DATE(TO_CHAR(a.data_iscrizione, 'dd/mm/yyyy'), 'dd/mm/yyyy') BETWEEN
             TO_DATE(par_data_inizio, 'dd/mm/yyyy') AND
             TO_DATE(par_data_fine, 'dd/mm/yyyy')
         AND a.cod_ufficio_inserimento = par_ufficio
            -- Umberto 26/9/2013 inserita nuova condizione per NGG, disabilitata in data 13/10/2013
            ---          AND ( a.cod_ufficio_inserimento=par_ufficio OR a.cod_ufficio_inserimento=par_ufficio1 OR a.cod_ufficio_inserimento=par_ufficio2 OR a.cod_ufficio_inserimento=par_ufficio3 )
            --  Umberto 26/9/2013       AND a.cod_ufficio_inserimento=a.chiave_ufficio  ----aggiunto il 09/01/2008
            --  Umberto 26/9/2013       AND a.cod_ufficio_inserimento=b.cod_ufficio_inserimento
            -- Ticket#20231017016 - STATISTICHE UFFICIO - TEMPI ISCRIZIONI PROCEDIMENTI
            -- Vanno estratti SOLO i classe I
         AND (a.chiave_progr BETWEEN 1 AND 19999 OR
             a.chiave_progr BETWEEN 10000000000001 AND 10000000019999 OR
             a.chiave_progr BETWEEN 20000000000001 AND 20000000019999 OR
             a.chiave_progr BETWEEN 30000000000001 AND 30000000019999 OR
             a.chiave_progr BETWEEN 800000 AND 819999 -- Pretura
             OR a.chiave_progr BETWEEN 900001000000 AND 919999000000 -- Pretura/RES con lettere classe I
             OR a.chiave_progr BETWEEN 1700001000000 AND 1719999000000 -- Pretura/RES con lettere classe I
             )
      /*
         AND a.chiave_progr NOT BETWEEN 930000000000 AND 939999999999 ---escludo le pene sospese
         AND a.chiave_progr NOT BETWEEN 30000 AND 39999
            -- Umberto 26/9/2013       esclusione pene sospese uffici accorpati
         AND a.chiave_progr NOT BETWEEN 10000000030000 AND 10000000039999
         AND a.chiave_progr NOT BETWEEN 20000000030000 AND 20000000039999
         AND a.chiave_progr NOT BETWEEN 30000000030000 AND 30000000039999
            -- Umberto 26/9/2013  Fine modifiche
            -- Umberto 24/03/2016  Esclusione Procedimenti di Misure Sicurezza
         AND a.chiave_progr NOT BETWEEN 40000 AND 49999
         AND a.chiave_progr NOT BETWEEN 10000000040000 AND 10000000049999
         AND a.chiave_progr NOT BETWEEN 20000000040000 AND 20000000049999
         AND a.chiave_progr NOT BETWEEN 30000000040000 AND 30000000049999
            -- Umberto 24/03/2016  Fine
            -- Umberto 24/03/2016  Esclusione Procedimenti di Conversione Pene Pecuniarie
         AND a.chiave_progr NOT BETWEEN 70000 AND 79999
         AND a.chiave_progr NOT BETWEEN 10000000070000 AND 10000000079999
         AND a.chiave_progr NOT BETWEEN 20000000070000 AND 20000000079999
         AND a.chiave_progr NOT BETWEEN 30000000070000 AND 30000000079999
            -- Umberto 24/03/2016  Fine
         AND a.chiave_progr NOT BETWEEN 700000 AND 799999 ---escludo cumulo aggiunto il 09/01/2008
            -- Umberto 26/9/2013       esclusione fascicoli cinvolti in cumulo uffici accorpati
         AND a.chiave_progr NOT BETWEEN 10000000700000 AND 10000000799999
         AND a.chiave_progr NOT BETWEEN 20000000700000 AND 20000000799999
         AND a.chiave_progr NOT BETWEEN 30000000700000 AND 30000000799999
            -- Umberto 26/09/2013  Fine modifiche
            -- Umberto 18/12/2013       esclusione fascicoli ISTANZE
         AND a.chiave_progr NOT BETWEEN 90000 AND 99999
         AND a.chiave_progr NOT BETWEEN 10000000090000 AND 10000000099999
         AND a.chiave_progr NOT BETWEEN 20000000090000 AND 20000000099999
         AND a.chiave_progr NOT BETWEEN 30000000090000 AND 30000000099999
      -- Umberto 18/12/2013  Fine modifiche
      -- Ticket#20231017016
      */
       ORDER BY id_fascicolo_siep;

  BEGIN
    ---inizio procedura

    --conta_commit := 1;

    BEGIN
      -----inizio iscrizione

      ---CANCELLO tabelle di appoggio
      DELETE FROM ISP_TEMPI_ISCRIZIONE;
      DELETE FROM ISP_TEMPI_RICEZIONE;
      COMMIT;

      -- APERTURA CURSORE
      OPEN cur_iscrizione;

      -- LETTURA FUORI CICLO DEI DATI DEL FASCICOLO
      FETCH cur_iscrizione
        INTO v_id_fascicolo_siep,
             v_chiave_anno,
             v_chiave_progr,
             v_af8_nres,
             v_data_iscrizione,
             v_data_irrevocabilita,
             v_data_arrivo_atto,
             -- Umberto 26/9/2013  inserita estrazione colonne aggiuntive
             v_cod_ufficio_inserimento,
             v_chiave_progr_orig,
             -- Umberto 26/9/2013  Fine modifiche
             v_cod_tipo_autorita,
             v_cod_luogo_emittente,
             v_sez_autorita_emittente;

      DBMS_OUTPUT.PUT_LINE('fetch fuori ciclo ');

      -- CALCOLO DELL'ORA DI CARICAMENTO
      v_data_inserimento := SYSDATE;
      -- v_data_verifica := SYSDATE;

      v_ricezione_iscrizione := 0;
      v_giudicato_iscrizione := 0;

      -- RICERCA DEL CODICE COMUNE DI APPARTENENZA DELL'UFFICIO
      BEGIN
        SELECT cod_comune
          INTO APPO_VAR.cod_comune
          FROM UFFICIO
         WHERE cod_ufficio = par_ufficio;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          APPO_VAR.cod_comune := '-';
      END;

      -- INIZIO CICLO DI ESTRAZIONE DATI PER OGNI RECORD RILASCIATO DALLA FETCH
      WHILE cur_iscrizione%FOUND LOOP
        BEGIN
          ---inizio loop

          DBMS_OUTPUT.PUT_LINE('INIZIO DEL LOOP ISCRIZIONE');

          num_record_letti := num_record_letti + 1;

          DBMS_OUTPUT.PUT_LINE('v_id_fascicolo_siep ' ||
                               v_id_fascicolo_siep);
          DBMS_OUTPUT.PUT_LINE('v_chiave_anno ' || v_chiave_anno);
          DBMS_OUTPUT.PUT_LINE('v_chiave_progr ' || v_chiave_progr);
          DBMS_OUTPUT.PUT_LINE('v_data_iscrizione ' || v_data_iscrizione);
          DBMS_OUTPUT.PUT_LINE('v_data_irrevocabilita ' ||
                               v_data_irrevocabilita);
          DBMS_OUTPUT.PUT_LINE('v_data_arrivo_atto ' || v_data_arrivo_atto);

          ----RICERCA TIPO AUTORITA EMITTENTE
          BEGIN
            SELECT rv_meaning
              INTO v_desc_autorita_emittente
              FROM CG_REF_CODES
             WHERE rv_low_value = v_cod_tipo_autorita
               AND rv_domain = 'TIPO_UFFICIO';
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              v_desc_autorita_emittente := '-';
          END;

          ----RICERCA LUOGO EMITTENTE
          BEGIN
            SELECT descrizione
              INTO v_desc_luogo_emittente
              FROM COMUNE
             WHERE cod_comune = v_cod_luogo_emittente;
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              v_desc_luogo_emittente := '-';
          END;

          -- Umberto 26/9/2013  inserita routine recupero descrizione ufficio inserimento
          ----RICERCA LUOGO UFFICIO INSERIMENTO
          BEGIN
            SELECT descrizione
              INTO v_desc_ufficio_inserimento
              FROM COMUNE
             WHERE cod_comune = SUBSTR(v_cod_ufficio_inserimento, 1, 6);
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              v_desc_ufficio_inserimento := '-';
          END;
          -- Umberto 26/9/2013  Fine modifiche

          ----RICERCA STATO PROCEDIMENTO
          BEGIN
            SELECT COD_STATO_PROCEDIMENTO
              INTO v_cod_stato_procedimento
              FROM STATO_PROCEDIMENTO
             WHERE FAS_SIE_ID_FASCICOLO_SIEP = v_id_fascicolo_siep
               AND (DATA, progressivo) IN
                   (SELECT MAX(DATA), MAX(progressivo)
                      FROM STATO_PROCEDIMENTO
                     WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep);
            -- Umberto 26/9/2013 commentate le 2 condizioni aggiuntive
            --                                          AND ( cod_ufficio_inserimento=par_ufficio OR cod_ufficio_inserimento=par_ufficio1 OR cod_ufficio_inserimento=par_ufficio2 OR cod_ufficio_inserimento=par_ufficio3 ))
            --            AND ( cod_ufficio_inserimento=par_ufficio OR cod_ufficio_inserimento=par_ufficio1 OR cod_ufficio_inserimento=par_ufficio2 OR cod_ufficio_inserimento=par_ufficio3 );
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              BEGIN
                SELECT COD_STATO_PROCEDIMENTO
                  INTO v_cod_stato_procedimento
                  FROM STATO_PROCEDIMENTO
                 WHERE FAS_SIE_ID_FASCICOLO_SIEP = v_id_fascicolo_siep
                   AND (progressivo) IN
                       (SELECT MAX(progressivo)
                          FROM STATO_PROCEDIMENTO
                         WHERE fas_sie_id_fascicolo_siep =
                               v_id_fascicolo_siep);
                -- Umberto 26/9/2013 commentate le 2 condizioni aggiuntive
                --                                            AND ( cod_ufficio_inserimento=par_ufficio OR cod_ufficio_inserimento=par_ufficio1 OR cod_ufficio_inserimento=par_ufficio2 OR cod_ufficio_inserimento=par_ufficio3 ))
                --                    AND ( cod_ufficio_inserimento=par_ufficio OR cod_ufficio_inserimento=par_ufficio1 OR cod_ufficio_inserimento=par_ufficio2 OR cod_ufficio_inserimento=par_ufficio3 );
              EXCEPTION
                WHEN NO_DATA_FOUND THEN
                  v_cod_stato_procedimento := '-';
              END;
          END;

          DBMS_OUTPUT.PUT_LINE('faccio insert ISP_TEMPI_ISCRIZIONE');

          BEGIN
            IF v_cod_stato_procedimento IN ('0108', '0109') THEN
              BEGIN
                SELECT data_emissione
                  INTO v_data_ordinanza
                  FROM EVENTO
                 WHERE cod_motivo = '0818'
                      -- Umberto   27/2/2014 gestione eventuale doppia occorrenza Evento con cod_motivo = '0818' (verificatosi su BDI Napoli)
                   AND fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                   AND cod_ufficio_inserimento = par_ufficio
                      -- Umberto 13/10/2013                   AND ( cod_ufficio_inserimento=par_ufficio OR cod_ufficio_inserimento=par_ufficio1 OR cod_ufficio_inserimento=par_ufficio2 OR cod_ufficio_inserimento=par_ufficio3 )
                   AND ID_EVENTO IN
                       (SELECT MAX(ID_EVENTO)
                          FROM EVENTO
                         WHERE fas_sie_id_fascicolo_siep =
                               v_id_fascicolo_siep);
                -- Umberto 27/2/2014 fine
              EXCEPTION
                WHEN NO_DATA_FOUND THEN
                  v_data_ordinanza := NULL;
              END;
            END IF;

            v_ricezione_iscrizione := v_data_iscrizione -
                                      v_data_arrivo_atto;

            IF v_data_ordinanza IS NOT NULL THEN
              v_giudicato_iscrizione := v_data_iscrizione -
                                        v_data_ordinanza;
            ELSE
              v_giudicato_iscrizione := v_data_iscrizione -
                                        v_data_irrevocabilita;
            END IF;

            INSERT INTO ISP_TEMPI_ISCRIZIONE
              (ID_FASCICOLO_SIEP,
               --NRES,
               CHIAVE_ANNO,
               CHIAVE_PROGR,
               COD_UFFICIO,
               DATA_ISCRIZIONE,
               DATA_IRREVOCABILITA,
               DATA_ARRIVO_ATTO,
               TEMPO_RICEZIONE_ISCRIZIONE,
               TEMPO_GIUDICATO_ISCRIZIONE,
               DESC_TIPO_AUTORITA_EMITTENTE,
               DESC_LUOGO_EMITTENTE,
               -- Umberto 26/9/2013           DESC_SEZIONE_AUTORITA)
               DESC_SEZIONE_AUTORITA,
               -- Umberto  26/9/2013  inserita valorizzazione colonne aggiuntive
               COD_UFFICIO_INSERIMENTO,
               CHIAVE_PROGR_ORIG,
               DESC_UFFICIO_INSERIMENTO)
            -- Umberto 26/9/2013  Fine modifiche
            VALUES
              (v_id_fascicolo_siep,
               --_af8_nres,
               v_chiave_anno,
               v_chiave_progr,
               par_ufficio,
               v_data_iscrizione,
               v_data_irrevocabilita,
               v_data_arrivo_atto,
               v_ricezione_iscrizione,
               v_giudicato_iscrizione,
               v_desc_autorita_emittente,
               v_desc_luogo_emittente,
               -- Umberto 26/9/2013          v_sez_autorita_emittente);
               v_sez_autorita_emittente,
               -- Umberto inserite nuove variabili  26/9/2013
               v_cod_ufficio_inserimento,
               v_chiave_progr_orig,
               v_desc_ufficio_inserimento);
            -- Umberto 26/9/2013  Fine modifiche
          END;

          COMMIT;

          DBMS_OUTPUT.PUT_LINE('fatto commit ISP_TEMPI_ISCRIZIONE');

        EXCEPTION
          WHEN salta_record THEN
            NULL;
        END; ----fine begin loop iscrizione

        -- LETTURA IN CICLO
        FETCH cur_iscrizione
          INTO v_id_fascicolo_siep,
               v_chiave_anno,
               v_chiave_progr,
               v_af8_nres,
               v_data_iscrizione,
               v_data_irrevocabilita,
               v_data_arrivo_atto,
               -- Umberto 26/9/2013  inserita estrazione colonne aggiuntive
               v_cod_ufficio_inserimento,
               v_chiave_progr_orig,
               -- Umberto 26/9/2013  Fine modifiche
               v_cod_tipo_autorita,
               v_cod_luogo_emittente,
               v_sez_autorita_emittente;

      -- CHIUSURA DEL WHILE
      END LOOP;

      CLOSE cur_iscrizione;

      COMMIT;

    END; ----fine iscrizione

    ----TEMPI DI RICEZIONE--------
    BEGIN
      ----inizio ricezione

      -- APERTURA CURSORE
      OPEN cur_ricezione;

      -- LETTURA FUORI CICLO DEI DATI DELLA RICEZIONE
      FETCH cur_ricezione
        INTO v_id_fascicolo_siep,
             v_chiave_anno,
             v_chiave_progr,
             v_af8_nres,
             v_data_irrevocabilita,
             v_data_arrivo_atto,
             -- Umberto 26/9/2013  inserita estrazione colonne aggiuntive
             v_cod_ufficio_inserimento,
             v_chiave_progr_orig,
             -- Umberto 26/9/2013  Fine modifiche
             v_cod_tipo_autorita,
             v_cod_luogo_emittente,
             v_sez_autorita_emittente;

      DBMS_OUTPUT.PUT_LINE('fetch fuori ciclo ');

      -- INIZIO CICLO DI ESTRAZIONE DATI PER OGNI RECORD RILASCIATO DALLA FETCH
      WHILE cur_ricezione%FOUND LOOP
        BEGIN
          ---inizio loop

          DBMS_OUTPUT.PUT_LINE('INIZIO DEL LOOP RICEZIONE');

          num_record_letti      := num_record_letti + 1;
          v_giudicato_ricezione := 0;

          DBMS_OUTPUT.PUT_LINE('v_id_fascicolo_siep ' ||
                               v_id_fascicolo_siep);
          DBMS_OUTPUT.PUT_LINE('v_chiave_anno ' || v_chiave_anno);
          DBMS_OUTPUT.PUT_LINE('v_chiave_progr ' || v_chiave_progr);
          DBMS_OUTPUT.PUT_LINE('v_data_iscrizione ' || v_data_iscrizione);
          DBMS_OUTPUT.PUT_LINE('v_data_irrevocabilita ' ||
                               v_data_irrevocabilita);
          DBMS_OUTPUT.PUT_LINE('v_data_arrivo_atto ' || v_data_arrivo_atto);

          ----RICERCA TIPO AUTORITA EMITTENTE
          BEGIN
            SELECT rv_meaning
              INTO v_desc_autorita_emittente
              FROM CG_REF_CODES
             WHERE rv_low_value = v_cod_tipo_autorita
               AND rv_domain = 'TIPO_UFFICIO';
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              v_desc_autorita_emittente := '-';
          END;

          ----RICERCA LUOGO EMITTENTE
          BEGIN
            SELECT descrizione
              INTO v_desc_luogo_emittente
              FROM COMUNE
             WHERE cod_comune = v_cod_luogo_emittente;
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              v_desc_luogo_emittente := '-';
          END;

          ----RICERCA LUOGO UFFICIO INSERIMENTO
          BEGIN
            SELECT descrizione
              INTO v_desc_ufficio_inserimento
              FROM COMUNE
             WHERE cod_comune = SUBSTR(v_cod_ufficio_inserimento, 1, 6);
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              v_desc_ufficio_inserimento := '-';
          END;

          DBMS_OUTPUT.PUT_LINE('faccio insert ISP_TEMPI_RICEZIONE');
          BEGIN

            v_giudicato_ricezione := v_data_arrivo_atto -
                                     v_data_irrevocabilita;

            INSERT INTO ISP_TEMPI_RICEZIONE
              (ID_FASCICOLO_SIEP,
               --NRES,
               CHIAVE_ANNO,
               CHIAVE_PROGR,
               COD_UFFICIO,
               DATA_IRREVOCABILITA,
               DATA_ARRIVO_ATTO,
               TEMPO_GIUDICATO_RICEZIONE,
               DESC_TIPO_AUTORITA_EMITTENTE,
               DESC_LUOGO_EMITTENTE,
               -- Umberto 26/9/2013           DESC_SEZIONE_AUTORITA)
               DESC_SEZIONE_AUTORITA,
               -- Umberto  26/9/2013  inserita valorizzazione colonne aggiuntive
               COD_UFFICIO_INSERIMENTO,
               CHIAVE_PROGR_ORIG,
               DESC_UFFICIO_INSERIMENTO)
            -- Umberto 26/9/2013  Fine modifiche
            VALUES
              (v_id_fascicolo_siep,
               --_af8_nres,
               v_chiave_anno,
               v_chiave_progr,
               par_ufficio,
               v_data_irrevocabilita,
               v_data_arrivo_atto,
               v_giudicato_ricezione,
               v_desc_autorita_emittente,
               v_desc_luogo_emittente,
               -- Umberto 26/9/2013          v_sez_autorita_emittente);
               v_sez_autorita_emittente,
               -- Umberto inserite nuove variabili  26/9/2013
               v_cod_ufficio_inserimento,
               v_chiave_progr_orig,
               v_desc_ufficio_inserimento);
            -- Umberto 26/9/2013  Fine modifiche
          END;

          COMMIT;

          DBMS_OUTPUT.PUT_LINE('fatto commit ISP_TEMPI_RICEZIONE');

        EXCEPTION
          WHEN salta_record THEN
            NULL;
        END; ---- fine loop

        -- LETTURA IN CICLO
        FETCH cur_ricezione
          INTO v_id_fascicolo_siep,
               v_chiave_anno,
               v_chiave_progr,
               v_af8_nres,
               v_data_irrevocabilita,
               v_data_arrivo_atto,
               -- Umberto 30/9/2013  inserita estrazione colonne aggiuntive
               v_cod_ufficio_inserimento,
               v_chiave_progr_orig,
               -- Umberto 30/9/2013  Fine modifiche
               v_cod_tipo_autorita,
               v_cod_luogo_emittente,
               v_sez_autorita_emittente;

      -- CHIUSURA DEL WHILE
      END LOOP;

      CLOSE cur_ricezione;

      COMMIT;

    END; ---fine ricezione

  END; ---fine procedura tempi iscrizione

  -----------------------------------------------------------------------------------------------------------------------
  ------------------------------------PROCEDURA TEMPI EMISSIONE-------------------------------------------------------
  -----------------------------------------------------------------------------------------------------------------------

  PROCEDURE tempi_emissione(par_data_inizio IN VARCHAR2,
                            par_data_fine   IN VARCHAR2,
                            --- Umberto  30/9/2013             par_ufficio      IN  VARCHAR2) IS
                            --- Umberto  30/9/2013          aggiunti 3 parametri ufficio per NGG
                            par_ufficio  IN VARCHAR2,
                            par_ufficio1 IN VARCHAR2,
                            par_ufficio2 IN VARCHAR2,
                            par_ufficio3 IN VARCHAR2) IS

    --DEFINIZIONE VARIABILI
    v_id_fascicolo_siep FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;
    --v_anno_af8_nrgem    EVENTO.anno_protocollo%TYPE;
    --v_progr_af8_nrgem   EVENTO.progr_protocollo%TYPE;
    v_chiave_anno  FASCICOLO_SIEP.chiave_anno%TYPE;
    v_chiave_progr FASCICOLO_SIEP.chiave_progr%TYPE;
    --- Umberto 30/9/2013          aggiunte 2 variabili per NGG
    v_cod_ufficio_inserimento FASCICOLO_SIEP.cod_ufficio_inserimento%TYPE;
    v_chiave_progr_orig       FASCICOLO_SIEP.chiave_progr_orig%TYPE;
    -- Umberto 30/9/2013  Fine modifiche
    --v_cod_posizione_giuridica POSIZIONE_GIURIDICA.cod_posizione_giuridica%TYPE;
    --v_con_rev                 VARCHAR2(30);
    --v_tip_aut                 VARCHAR2(70);
    --v_tip_att                 VARCHAR2(80);
    --v_motiv                   VARCHAR2(50);
    v_af8_nres FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;

    --v_cod_stato_procedimento  STATO_PROCEDIMENTO.cod_stato_procedimento%TYPE;
    --flag_no_riepilogo         CHAR(1);
    --tot_recl                  NUMBER;
    --tot_arre                  NUMBER;
    --v_num_anni_reclusione     NUMBER(4);
    --v_num_mesi_reclusione     NUMBER(4);
    --v_num_giorni_reclusione   NUMBER(4);
    --v_importo_multa           NUMBER(11, 2);
    --v_num_anni_arresto        NUMBER(4);
    --v_num_mesi_arresto        NUMBER(4);
    --v_num_giorni_arresto      NUMBER(4);
    --v_importo_ammenda         NUMBER(11, 2);
    --gg_tot                    NUMBER;
    --tot_pres                  NUMBER;
    --v_data_inizio             PENA_RESIDUA.data_inizio%TYPE;
    --v_data_fine               PENA_RESIDUA.data_fine%TYPE;
    --v_id_fascicolo_siep_old   FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;
    --v_id_evento               EVENTO.id_evento%TYPE;
    v_cod_magistrato          EVENTO.cod_magistrato%TYPE;
    v_cod_stato_fascicolo_res STATO_FASCICOLO_RES.cod_stato_fascicolo%TYPE := '';
    -- variabile generali
    --conta_commit NUMBER;

    --v_trovato  NUMBER;
    v_inserito NUMBER := 0;

    --num_record_scritti NUMBER := 0;
    num_record_letti NUMBER := 0;
    --num_record_scartati            NUMBER:= 0;
    --v_anno_res               NUMBER(4) := NULL;
    --v_num_res                VARCHAR2(6) := '';
    v_data_iscrizione  DATE;
    v_data_arrivo_atto DATE;
    --v_data_emissione         DATE;
    v_data_irrevocabilita DATE;
    --v_iscrizione_emissione   NUMBER := 0;
    primo_record_letto       VARCHAR2(1) := 'N';
    secondo_record_letto     VARCHAR2(1) := 'N';
    terzo_record_letto       VARCHAR2(1) := 'N';
    v_id_evento_esec         EVENTO.id_evento%TYPE := '';
    v_motivo_esec            EVENTO.cod_motivo%TYPE := '';
    v_data_ordine_esec       DATE;
    v_desc_motivo_esec       CG_REF_CODES.rv_meaning%TYPE;
    v_id_evento_primo_atto   EVENTO.id_evento%TYPE := '';
    v_motivo_primo_atto      EVENTO.cod_motivo%TYPE := '';
    v_data_primo_atto        DATE;
    v_desc_motivo_primo_atto CG_REF_CODES.rv_meaning%TYPE;
    --v_id_evento_ord          EVENTO.id_evento%TYPE := '';
    --v_motivo_ord             EVENTO.cod_motivo%TYPE := '';
    --v_data_emissione_ord     DATE;
    --v_desc_motivo_ord        CG_REF_CODES.rv_meaning%TYPE;
    v_caso_rich VARCHAR2(1) := 'N';

    v_id_evento_istruttoria   EVENTO.id_evento%TYPE := '';
    v_motivo_istruttoria      EVENTO.cod_motivo%TYPE := '';
    v_data_istruttoria        DATE;
    v_desc_motivo_istruttoria CG_REF_CODES.rv_meaning%TYPE;

    --v_DATA_PASSATO_GIUDICATO     DATE;
    v_TEMPI_GIUDICATO_ARRIVO     NUMBER;
    v_TEMPI_ARRIVO_ISCRIZIONE    NUMBER;
    v_TEMPI_ISCRIZIONE_EMISSIONE NUMBER;
    v_DATA_INIZIO_ISTRUTTORIA    DATE;
    v_DATA_FINE_ISTRUTTORIA      DATE;
    v_TEMPI_ISTRUTTORIA          NUMBER;
    v_DATA_INIZIO_INATTIVITA     DATE;
    V_DATA_FINE_INATTIVITA       DATE;
    v_TEMPI_INATTIVITA           NUMBER;
    v_GIORNI_DA_NEUTRALIZZARE    NUMBER;
    --v_elenco_istruttoria         VARCHAR2(200);
    --v_elenco_primo_atto          VARCHAR2(500);
    --v_elenco_esecuzione          VARCHAR2(100);
    -- Umberto 30/9/2013          aggiunti 1 variabile per NGG
    v_desc_ufficio_inserimento VARCHAR2(200);
    -- Umberto  30/9/2013  Fine modifiche

    -- dichiarazione del cursore
    CURSOR cur_fascicolo IS
      SELECT a.id_fascicolo_siep,
             a.chiave_anno,
             a.chiave_progr,
             a.data_iscrizione,
             TO_DATE(TO_CHAR(a.data_arrivo_atto, 'dd/mm/yyyy'),
                     'dd/mm/yyyy'),
             a.fas_sie_id_fascicolo_siep,
             -- Umberto 30/9/2013            a.data_irrevocabilita
             a.data_irrevocabilita,
             -- Umberto 30/9/2013  inserita estrazione colonne aggiuntive
             a.cod_ufficio_inserimento,
             a.chiave_progr_orig
      -- Umberto  30/9/2013  Fine modifiche
        FROM FASCICOLO_SIEP a, SENTENZA b
      -- Umberto 30/9/2013                      WHERE a.cod_ufficio_inserimento=par_ufficio
      -- Umberto 30/9/2013 inserita nuova condizione per NGG
       WHERE (a.cod_ufficio_inserimento = par_ufficio OR
             a.cod_ufficio_inserimento = par_ufficio1 OR
             a.cod_ufficio_inserimento = par_ufficio2 OR
             a.cod_ufficio_inserimento = par_ufficio3)
         AND a.cod_ufficio_inserimento = b.cod_ufficio_inserimento
         AND a.data_iscrizione BETWEEN
             TO_DATE(par_data_inizio, 'dd/mm/yyyy') AND
             TO_DATE(par_data_fine, 'dd/mm/yyyy')
         AND a.sen_id_sentenza = b.id_sentenza
            -- Ticket#20231017016 - STATISTICHE UFFICIO - TEMPI ISCRIZIONI PROCEDIMENTI
            -- Vanno estratti SOLO i classe I
         AND (a.chiave_progr BETWEEN 1 AND 19999 OR
             a.chiave_progr BETWEEN 10000000000001 AND 10000000019999 OR
             a.chiave_progr BETWEEN 20000000000001 AND 20000000019999 OR
             a.chiave_progr BETWEEN 30000000000001 AND 30000000019999 OR
             a.chiave_progr BETWEEN 800000 AND 819999 -- Pretura
             OR a.chiave_progr BETWEEN 900001000000 AND 919999000000 -- Pretura/RES con lettere classe I
             OR a.chiave_progr BETWEEN 1700001000000 AND 1719999000000 -- Pretura/RES con lettere classe I
             )
            /*
            AND a.chiave_progr NOT BETWEEN 930000000000 AND 939999999999 ---escludo le pene sospese
            AND a.chiave_progr NOT BETWEEN 30000 AND 39999
               -- Umberto 26/9/2013       esclusione pene sospese uffici accorpati
            AND a.chiave_progr NOT BETWEEN 10000000030000 AND 10000000039999
            AND a.chiave_progr NOT BETWEEN 20000000030000 AND 20000000039999
            AND a.chiave_progr NOT BETWEEN 30000000030000 AND 30000000039999
               -- Umberto 26/9/2013  Fine modifiche
               -- Umberto 24/03/2016  Esclusione Procedimenti di Misure Sicurezza
            AND a.chiave_progr NOT BETWEEN 40000 AND 49999
            AND a.chiave_progr NOT BETWEEN 10000000040000 AND 10000000049999
            AND a.chiave_progr NOT BETWEEN 20000000040000 AND 20000000049999
            AND a.chiave_progr NOT BETWEEN 30000000040000 AND 30000000049999
               -- Umberto 24/03/2016  Fine
               -- Umberto 24/03/2016  Esclusione Procedimenti di Conversione Pene Pecuniarie
            AND a.chiave_progr NOT BETWEEN 70000 AND 79999
            AND a.chiave_progr NOT BETWEEN 10000000070000 AND 10000000079999
            AND a.chiave_progr NOT BETWEEN 20000000070000 AND 20000000079999
            AND a.chiave_progr NOT BETWEEN 30000000070000 AND 30000000079999
               -- Umberto 24/03/2016  Fine
            AND a.chiave_progr NOT BETWEEN 700000 AND 799999 ---escludo cumulo aggiunto il 09/01/2008
               -- Umberto 26/9/2013       esclusione fascicoli cinvolti in cumulo uffici accorpati
            AND a.chiave_progr NOT BETWEEN 10000000700000 AND 10000000799999
            AND a.chiave_progr NOT BETWEEN 20000000700000 AND 20000000799999
            AND a.chiave_progr NOT BETWEEN 30000000700000 AND 30000000799999
               -- Umberto 26/9/2013  Fine modifiche
               -- Umberto 18/12/2013       esclusione fascicoli ISTANZE
            AND a.chiave_progr NOT BETWEEN 90000 AND 99999
            AND a.chiave_progr NOT BETWEEN 10000000090000 AND 10000000099999
            AND a.chiave_progr NOT BETWEEN 20000000090000 AND 20000000099999
            AND a.chiave_progr NOT BETWEEN 30000000090000 AND 30000000099999
               -- Umberto 18/12/2013  Fine modifiche
               */
            -- Ticket#20231017016 - FINE
         AND a.id_fascicolo_siep IN
             (SELECT fas_sie_id_fascicolo_siep
                FROM EVENTO
               WHERE cod_motivo IN ('0057',
                                    '0058',
                                    '0059',
                                    '0060',
                                    '0061',
                                    '0062',
                                    '0063',
                                    '0225',
                                    '0222',
                                    '0130',
                                    '0131',
                                    '0132',
                                    '0133',
                                    '0134',
                                    '0354',
                                    '0355',
                                    '0104',
                                    '0105',
                                    '0117',
                                    '0271',
                                    '0246',
                                    '0223',
                                    '0224',
                                    '0277',
                                    -- Umberto 4/10/2013  Aggiunti altri COD_MOTIVO su indicazione di Testa
                                    '0011',
                                    '0013',
                                    '0217',
                                    '0218',
                                    '0228',
                                    '0229',
                                    '0364',
                                    '0365',
                                    '0397',
                                    '0490',
                                    '0491',
                                    '0493',
                                    '0960',
                                    '0961',
                                    '0962',
                                    '1023',
                                    '1024',
                                    '2005',
                                    '2630'
                                    -- Giugno 2017 - MEV42 - Aggiunti Nuovi Codici Cumulo
                                   ,
                                    '0630',
                                    '0635',
                                    '0636',
                                    '0637',
                                    '0638',
                                    '0639',
                                    '0640',
                                    '0641',
                                    '0642',
                                    '0643',
                                    '0644',
                                    '0645',
                                    '0646',
                                    '0647',
                                    '0648',
                                    '0649',
                                    '0650',
                                    '0651',
                                    '0652',
                                    '0653',
                                    '0654',
                                    '0655',
                                    '0656',
                                    '0657',
                                    '0658',
                                    '0659',
                                    -- 23/4/2019 - MEV70 - Aggiunto Nuovo Codice '0665'
                                    --                         '0660','0661','0662','0663','0664','0666','0667','0668')
                                    '0660',
                                    '0661',
                                    '0662',
                                    '0663',
                                    '0664',
                                    '0665',
                                    '0666',
                                    '0667',
                                    '0668',
                                    -- Ticket#20220224017 Umberto 25/2/2022 Aggiunti codici di OE per Minori
                                    '5501',
                                    '5502',
                                    '5503',
                                    '5504',
                                    '5505',
                                    '5506',
                                    '5507',
                                    '5508',
                                    '5509',
                                    '5510',
                                    '5511',
                                    '5512',
                                    '5513',
                                    '5523',
                                    '5524',
                                    '5525',
                                    '5526',
                                    '5527',
                                    '5528',
                                    '5529',
                                    '5533',
                                    '5534',
                                    '5535',
                                    '5536',
                                    '5537')
              -- Ticket#20220224017 25/2/2022  FINE
               GROUP BY fas_sie_id_fascicolo_siep)
       ORDER BY id_fascicolo_siep;

  BEGIN
    ---inizio procedura

    --conta_commit := 1;

    ---CANCELLO tabelle di appoggio
    DELETE FROM ISP_TEMPI_EMISSIONE;
    COMMIT;

    --  v_elenco_esecuzione :=   '0057'||','||'0058'||','||'0059'||','||'0060'||','||'0061'||','
    --                         ||'0062'||','||'0063'||','||'0225'||','||'0222'||','||'0130'||','
    --                         ||'0131'||','||'0132'||','||'0133'||','||'0134'||','||'0354'||','
    --                         ||'0355'||','||'0104'||','||'0105'||','||'0117'||','||'0271'||','
    --                         ||'0246'||','||'0223'||','||'0224'||','||'0277';
    --
    --  v_elenco_istruttoria:=   '0044'||','||'0045'||','||'0046'||','||'0047'||','||'0048'||','
    --                         ||'0049'||','||'0050'||','||'0051'||','||'0052'||','||'0053'||','
    --                         ||'0054'||','||'0056'||','||'0545'||','||'0550'||','||'0557'||','
    --                         ||'0565'||','||'0566'||','||'0577'||','||'0578' ;
    --
    --  v_elenco_primo_atto:=    '0122'||','||'0158'||','||'0159'||','||'0160'||','||'0161'||','
    --                         ||'0162'||','||'0163'||','||'0174'||','||'0175'||','||'0186'||','
    --                         ||'0210'||','||'0211'||','||'0213'||','||'0284'||','||'0285'||','
    --                         ||'0286'||','||'0291'||','||'0292'||','||'0293'||','||'0294'||','
    --                         ||'0297'||','||'0298'||','||'0299'||','||'0030'||','||'0031'||','
    --                         ||'0032'||','||'0033'||','||'0201'||','||'0202'||','||'2010'||','
    --                         ||'2011'||','||'0909'||','||'0116'||','||'0263'||','||'0264'||','
    --                         ||'0265'||','||'0274'||','||'0900'||','||'0901'||','||'0902'||','
    --                         ||'0903'||','||'0904'||','||'0905'||','||'0906'||','||'0907'||','
    --                         ||'0908'||','||'0910'||','||'0912'||','||'0937'||','||'0322'||','
    --                         ||'0326'||','||'0346';

    -- APERTURA CURSORE
    OPEN cur_fascicolo;

    -- LETTURA FUORI CICLO DEI DATI DEL FASCICOLO
    FETCH cur_fascicolo
      INTO v_id_fascicolo_siep,
           v_chiave_anno,
           v_chiave_progr,
           v_data_iscrizione,
           v_data_arrivo_atto,
           v_af8_nres,
           -- Umberto 30/9/2013             v_data_irrevocabilita;
           v_data_irrevocabilita,
           -- Umberto 30/9/2013  inserita estrazione colonne aggiuntive
           v_cod_ufficio_inserimento,
           v_chiave_progr_orig;
    -- Umberto 30/9/2013  Fine modifiche

    DBMS_OUTPUT.PUT_LINE('fetch fuori ciclo ');

    -- CALCOLO DELL'ORA DI CARICAMENTO
    v_data_inserimento := SYSDATE;

    -- INIZIO CICLO DI ESTRAZIONE DATI PER OGNI RECORD RILASCIATO DALLA FETCH
    WHILE cur_fascicolo%FOUND LOOP
      BEGIN
        ---inizio loop

        DBMS_OUTPUT.PUT_LINE('INIZIO DEL LOOP ');

        DBMS_OUTPUT.PUT_LINE('v_id_fascicolo_siep ' || v_id_fascicolo_siep);
        DBMS_OUTPUT.PUT_LINE('v_chiave_anno ' || v_chiave_anno);
        DBMS_OUTPUT.PUT_LINE('v_chiave_progr ' || v_chiave_progr);

        v_cod_stato_fascicolo_res := NULL;
        num_record_letti          := 0;
        v_inserito                := 0;
        v_cod_motivo              := '';
        primo_record_letto        := 'N';
        secondo_record_letto      := 'N';
        terzo_record_letto        := 'N';
        v_caso_rich               := 'N';
        v_id_evento_esec          := NULL;
        v_motivo_esec             := NULL;
        v_data_ordine_esec        := NULL;
        v_desc_motivo_esec        := NULL;
        v_cod_magistrato          := NULL;
        v_id_evento_primo_atto    := NULL;
        v_motivo_primo_atto       := NULL;
        v_data_primo_atto         := NULL;
        v_desc_motivo_primo_atto  := NULL;
        v_id_evento_istruttoria   := NULL;
        v_motivo_istruttoria      := NULL;
        v_data_istruttoria        := NULL;
        v_desc_motivo_istruttoria := NULL;

        -- Umberto 30/9/2013  inserita nuova routine ricerca Descrizione Ufficio Inserimento
        ----RICERCA LUOGO UFFICIO INSERIMENTO
        BEGIN
          SELECT descrizione
            INTO v_desc_ufficio_inserimento
            FROM COMUNE
           WHERE cod_comune = SUBSTR(v_cod_ufficio_inserimento, 1, 6);
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_desc_ufficio_inserimento := '-';
        END;
        -- Umberto 30/9/2013  Fine modifiche

        ----trovo il primo evento OE
        BEGIN
          SELECT id_evento,
                 cod_motivo,
                 TO_DATE(TO_CHAR(data_emissione, 'dd/mm/yyyy'),
                         'dd/mm/yyyy'),
                 tipo_provv.rv_meaning || ' ' || motivo_provv.rv_meaning,
                 cod_magistrato
            INTO v_id_evento_esec,
                 v_motivo_esec,
                 v_data_ordine_esec,
                 v_desc_motivo_esec,
                 v_cod_magistrato
            FROM EVENTO, CG_REF_CODES motivo_provv, CG_REF_CODES tipo_provv
           WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                -- Umberto 4/10/2013 ristabilito filtro solo su ufficio principale
             AND cod_ufficio_inserimento = par_ufficio
                -- Umberto 30/9/2013 inserita nuova condizione per NGG
                --         AND ( cod_ufficio_inserimento=par_ufficio OR cod_ufficio_inserimento=par_ufficio1 OR cod_ufficio_inserimento=par_ufficio2 OR cod_ufficio_inserimento=par_ufficio3 )
                -- Umberto 30/9/2013  Fine modifiche
             AND motivo_provv.rv_domain = 'MOTIVO_PROVVEDIMENTO'
             AND motivo_provv.rv_low_value = cod_motivo
             AND tipo_provv.rv_domain = 'TIPO_PROVVEDIMENTO'
             AND tipo_provv.rv_low_value = cod_tipo_provvedimento
             AND id_evento IN
                 (SELECT MIN(id_evento)
                    FROM EVENTO
                   WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                     AND cod_motivo IN ('0057',
                                        '0058',
                                        '0059',
                                        '0060',
                                        '0061',
                                        '0062',
                                        '0063',
                                        '0225',
                                        '0222',
                                        '0130',
                                        '0131',
                                        '0132',
                                        '0133',
                                        '0134',
                                        '0354',
                                        '0355',
                                        '0104',
                                        '0105',
                                        '0117',
                                        '0271',
                                        '0246',
                                        '0223',
                                        '0224',
                                        '0277',
                                        -- Umberto 4/10/2013  Aggiunti altri COD_MOTIVO su indicazione di Testa
                                        '0011',
                                        '0013',
                                        '0217',
                                        '0218',
                                        '0228',
                                        '0229',
                                        '0364',
                                        '0365',
                                        '0397',
                                        '0490',
                                        '0491',
                                        '0493',
                                        '0960',
                                        '0961',
                                        '0962',
                                        '1023',
                                        '1024',
                                        '2005',
                                        '2630',
                                        -- Giugno 2017 - MEV42 - Aggiunti Nuovi Codici Cumulo
                                        '0630',
                                        '0635',
                                        '0636',
                                        '0637',
                                        '0638',
                                        '0639',
                                        '0640',
                                        '0641',
                                        '0642',
                                        '0643',
                                        '0644',
                                        '0645',
                                        '0646',
                                        '0647',
                                        '0648',
                                        '0649',
                                        '0650',
                                        '0651',
                                        '0652',
                                        '0653',
                                        '0654',
                                        '0655',
                                        '0656',
                                        '0657',
                                        '0658',
                                        '0659',
                                        -- 23/4/2019 - MEV70 - Aggiunto Nuovo Codice '0665'
                                        --                         '0660','0661','0662','0663','0664','0666','0667','0668')
                                        '0660',
                                        '0661',
                                        '0662',
                                        '0663',
                                        '0664',
                                        '0665',
                                        '0666',
                                        '0667',
                                        '0668',
                                        -- Ticket#20220224017 Umberto 25/2/2022 Aggiunti codici di OE per Minori
                                        '5501',
                                        '5502',
                                        '5503',
                                        '5504',
                                        '5505',
                                        '5506',
                                        '5507',
                                        '5508',
                                        '5509',
                                        '5510',
                                        '5511',
                                        '5512',
                                        '5513',
                                        '5523',
                                        '5524',
                                        '5525',
                                        '5526',
                                        '5527',
                                        '5528',
                                        '5529',
                                        '5533',
                                        '5534',
                                        '5535',
                                        '5536',
                                        '5537')
                        -- Ticket#20220224017 25/2/2022  FINE
                        -- Umberto 4/10/2013  seleziona solo eventi Validati
                     AND flag_documento_registrato = 'S'
                        -- Umberto 4/10/2013 Fine
                        -- Umberto 18/7/2014 inserito controllo su data emissione del primo OE, non deve essere superiore alla data fine periodo
                     AND data_emissione <=
                         TO_DATE(par_data_fine, 'dd/mm/yyyy'))
          -- Umberto  18/7/2014  Fine
           ORDER BY data_emissione, id_evento;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_id_evento_esec   := NULL;
            v_motivo_esec      := NULL;
            v_data_ordine_esec := NULL;
            v_desc_motivo_esec := NULL;
        END;
        --- trovo il primo evento primo atto
        BEGIN
          SELECT id_evento,
                 cod_motivo,
                 TO_DATE(TO_CHAR(data_emissione, 'dd/mm/yyyy'),
                         'dd/mm/yyyy'),
                 tipo_provv.rv_meaning || ' ' || motivo_provv.rv_meaning
            INTO v_id_evento_primo_atto,
                 v_motivo_primo_atto,
                 v_data_primo_atto,
                 v_desc_motivo_primo_atto
            FROM EVENTO, CG_REF_CODES motivo_provv, CG_REF_CODES tipo_provv
           WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                -- Umberto 4/10/2013 ristabilito filtro solo su ufficio principale
                --           AND cod_ufficio_inserimento=par_ufficio
                -- Umberto 30/9/2013 inserita nuova condizione per NGG
             AND (cod_ufficio_inserimento = par_ufficio OR
                 cod_ufficio_inserimento = par_ufficio1 OR
                 cod_ufficio_inserimento = par_ufficio2 OR
                 cod_ufficio_inserimento = par_ufficio3)
                -- Umberto 30/9/2013  Fine modifiche
             AND motivo_provv.rv_domain = 'MOTIVO_PROVVEDIMENTO'
             AND motivo_provv.rv_low_value = cod_motivo
             AND tipo_provv.rv_domain = 'TIPO_PROVVEDIMENTO'
             AND tipo_provv.rv_low_value = cod_tipo_provvedimento
             AND id_evento IN
                 (SELECT MIN(id_evento)
                    FROM EVENTO
                   WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                     AND cod_motivo IN ('0122',
                                        '0158',
                                        '0159',
                                        '0160',
                                        '0161',
                                        '0162',
                                        '0163',
                                        '0174',
                                        '0175',
                                        '0186',
                                        '0210',
                                        '0211',
                                        '0213',
                                        '0284',
                                        '0285',
                                        '0286',
                                        '0291',
                                        '0292',
                                        '0293',
                                        '0294',
                                        '0297',
                                        '0298',
                                        '0299',
                                        '0030',
                                        '0031',
                                        '0032',
                                        '0033',
                                        '0201',
                                        '0202',
                                        '2010',
                                        '2011',
                                        '0909',
                                        '0116',
                                        '0263',
                                        '0264',
                                        '0265',
                                        '0274',
                                        '0900',
                                        '0901',
                                        '0902',
                                        '0903',
                                        '0904',
                                        '0905',
                                        '0906',
                                        '0907',
                                        '0908',
                                        '0910',
                                        '0912',
                                        '0937',
                                        '0322',
                                        '0326',
                                        '0346',
                                        -- Umberto 4/10/2013  Aggiunti altri COD_MOTIVO su indicazione di Testa
                                        '0042',
                                        '0287',
                                        '0289',
                                        '0290',
                                        '0319',
                                        '0320',
                                        '0321',
                                        '0324',
                                        '0347',
                                        '0565',
                                        '0608',
                                        '5507')
                        -- Umberto 4/10/2013  seleziona solo eventi Validati
                     AND flag_documento_registrato = 'S'
                        -- Umberto 4/10/2013 Fine
                        -- Umberto 18/7/2014 inserito controllo su data emissione del primo OE, non deve essere superiore alla data fine periodo
                     AND data_emissione <=
                         TO_DATE(par_data_fine, 'dd/mm/yyyy'))
          -- Umberto  18/7/2014  Fine
           ORDER BY data_emissione, id_evento;

        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_id_evento_primo_atto   := NULL;
            v_motivo_primo_atto      := NULL;
            v_data_primo_atto        := NULL;
            v_desc_motivo_primo_atto := NULL;
        END;

        --- trovo la prima istruttoria
        BEGIN
          SELECT id_evento,
                 cod_motivo,
                 TO_DATE(TO_CHAR(data_emissione, 'dd/mm/yyyy'),
                         'dd/mm/yyyy'),
                 tipo_provv.rv_meaning || ' ' || motivo_provv.rv_meaning
            INTO v_id_evento_istruttoria,
                 v_motivo_istruttoria,
                 v_data_istruttoria,
                 v_desc_motivo_istruttoria
            FROM EVENTO, CG_REF_CODES motivo_provv, CG_REF_CODES tipo_provv
           WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                -- Umberto 4/10/2013 ristabilito filtro solo su ufficio principale
                --      AND cod_ufficio_inserimento=par_ufficio
                -- Umberto 30/9/2013 inserita nuova condizione per NGG
             AND (cod_ufficio_inserimento = par_ufficio OR
                 cod_ufficio_inserimento = par_ufficio1 OR
                 cod_ufficio_inserimento = par_ufficio2 OR
                 cod_ufficio_inserimento = par_ufficio3)
                -- Umberto 30/9/2013  Fine modifiche
             AND motivo_provv.rv_domain = 'MOTIVO_PROVVEDIMENTO'
             AND motivo_provv.rv_low_value = cod_motivo
             AND tipo_provv.rv_domain = 'TIPO_PROVVEDIMENTO'
             AND tipo_provv.rv_low_value = cod_tipo_provvedimento
             AND id_evento IN
                 (SELECT MIN(id_evento)
                    FROM EVENTO
                   WHERE fas_sie_id_fascicolo_siep = v_id_fascicolo_siep
                     AND cod_motivo IN ('0044',
                                        '0045',
                                        '0046',
                                        '0047',
                                        '0048',
                                        '0049',
                                        '0050',
                                        '0051',
                                        '0052',
                                        '0053',
                                        '0054',
                                        '0056',
                                        '0545',
                                        '0550',
                                        '0557',
                                        '0565',
                                        '0566',
                                        '0577',
                                        '0578')
                        -- Umberto 4/10/2013  seleziona solo eventi non Annullati
                     AND flag_documento_registrato <> 'A'
                        -- Umberto 4/10/2013 Fine
                        -- Umberto 18/7/2014 inserito controllo su data emissione della prima Istruttoria, non deve essere superiore alla data fine periodo
                     AND data_emissione <=
                         TO_DATE(par_data_fine, 'dd/mm/yyyy'))
          -- Umberto  18/7/2014  Fine
           ORDER BY data_emissione, id_evento;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_id_evento_istruttoria   := NULL;
            v_motivo_istruttoria      := NULL;
            v_data_istruttoria        := NULL;
            v_desc_motivo_istruttoria := NULL;
        END;

        --------------------------------------
        ------l'OE ??????????????????????????? il primo evento---------
        ---------------------------------------
        IF (v_id_evento_esec < v_id_evento_istruttoria AND
           v_id_evento_esec < v_id_evento_primo_atto) OR
           (v_id_evento_esec < v_id_evento_istruttoria AND
           v_id_evento_primo_atto IS NULL) OR
           (v_id_evento_istruttoria IS NULL AND
           v_id_evento_esec < v_id_evento_primo_atto) OR
           (v_id_evento_istruttoria IS NULL AND
           v_id_evento_primo_atto IS NULL) THEN

          DBMS_OUTPUT.PUT_LINE('caso OE primo evento ');

          v_id_evento_primo_atto    := NULL;
          v_motivo_primo_atto       := NULL;
          v_data_primo_atto         := NULL;
          v_desc_motivo_primo_atto  := NULL;
          v_data_inizio_istruttoria := v_data_iscrizione;
          v_data_fine_istruttoria   := v_data_ordine_esec;
          v_data_inizio_inattivita  := v_data_iscrizione;
          v_data_fine_inattivita    := v_data_inizio_istruttoria;
        END IF;

        ---------------------------------------------------------
        ------il primo evento ??????????????????????????? un primo_atto o istruttoria-----
        --------------------------------------------------------
        IF v_id_evento_istruttoria < v_id_evento_esec AND
           v_id_evento_primo_atto < v_id_evento_esec AND
           v_id_evento_istruttoria > 0 AND v_id_evento_primo_atto > 0 THEN

          DBMS_OUTPUT.PUT_LINE('caso OE ultimo evento ');

          v_data_inizio_istruttoria := v_data_istruttoria;
          v_data_fine_istruttoria   := v_data_primo_atto;
          v_data_inizio_inattivita  := v_data_iscrizione;
          v_data_fine_inattivita    := v_data_inizio_istruttoria;
        END IF;

        ---------------------------------------------------------
        ------il primo evento ??????????????????????????? un primo_atto------------
        --------------------------------------------------------
        IF (v_id_evento_primo_atto > 0 AND ------modificato il 19/05/2008
           v_id_evento_istruttoria > 0 AND
           v_id_evento_primo_atto < v_id_evento_istruttoria AND
           v_id_evento_primo_atto < v_id_evento_esec) OR
           (v_id_evento_primo_atto > 0 AND v_id_evento_istruttoria IS NULL AND
           v_id_evento_primo_atto < v_id_evento_esec) THEN

          DBMS_OUTPUT.PUT_LINE('caso primo evento ??????????????????????????? un atto');

          v_data_inizio_istruttoria := v_data_iscrizione;
          v_data_fine_istruttoria   := v_data_primo_atto;
          v_data_inizio_inattivita  := v_data_iscrizione;
          v_data_fine_inattivita    := v_data_inizio_istruttoria;
        END IF;

        ---------------------------------------------------------
        ------il primo evento ??????????????????????????? un'istruttoria------------
        --------------------------------------------------------
        IF (v_id_evento_primo_atto > 0 AND ------modificato il 19/05/2008
           v_id_evento_istruttoria > 0 AND
           v_id_evento_istruttoria < v_id_evento_primo_atto AND
           v_id_evento_istruttoria < v_id_evento_esec) OR
           (v_id_evento_istruttoria > 0 AND v_id_evento_primo_atto IS NULL AND
           v_id_evento_istruttoria < v_id_evento_esec) THEN

          DBMS_OUTPUT.PUT_LINE('caso primo evento ??????????????????????????? istruttoria');

          v_data_inizio_istruttoria := v_data_istruttoria;
          v_data_fine_istruttoria   := v_data_ordine_esec;
          v_data_inizio_inattivita  := v_data_iscrizione;
          v_data_fine_inattivita    := v_data_inizio_istruttoria;
        END IF;

        DBMS_OUTPUT.PUT_LINE('v_id_evento_esec ' || v_id_evento_esec);
        DBMS_OUTPUT.PUT_LINE('v_data_ordine_esec ' || v_data_ordine_esec);
        DBMS_OUTPUT.PUT_LINE('v_id_evento_primo_atto ' ||
                             v_id_evento_primo_atto);
        DBMS_OUTPUT.PUT_LINE('v_data_primo_atto ' || v_data_primo_atto);
        DBMS_OUTPUT.PUT_LINE('v_id_evento_istruttoria ' ||
                             v_id_evento_istruttoria);
        DBMS_OUTPUT.PUT_LINE('v_data_istruttoria ' || v_data_istruttoria);
        DBMS_OUTPUT.PUT_LINE('v_data_inizio_istruttoria ' ||
                             v_data_inizio_istruttoria);
        DBMS_OUTPUT.PUT_LINE('v_data_fine_istruttoria ' ||
                             v_data_fine_istruttoria);
        DBMS_OUTPUT.PUT_LINE('v_data_inizio_inattivita ' ||
                             v_data_inizio_inattivita);
        DBMS_OUTPUT.PUT_LINE('v_data_fine_inattivita ' ||
                             v_data_fine_inattivita);

        DBMS_OUTPUT.PUT_LINE('eseguo insert ');

        v_TEMPI_GIUDICATO_ARRIVO     := v_data_arrivo_atto -
                                        v_data_irrevocabilita;
        v_TEMPI_ARRIVO_ISCRIZIONE    := v_data_iscrizione -
                                        v_data_arrivo_atto;
        v_tempi_iscrizione_emissione := v_data_ordine_esec -
                                        v_data_iscrizione;
        v_tempi_istruttoria          := v_data_fine_istruttoria -
                                        v_data_inizio_istruttoria;
        v_tempi_inattivita           := v_data_fine_inattivita -
                                        v_data_inizio_inattivita;
        v_GIORNI_da_neutralizzare    := v_TEMPI_ARRIVO_ISCRIZIONE +
                                        v_tempi_istruttoria;
        IF v_id_evento_esec IS NOT NULL THEN
          INSERT INTO ISP_TEMPI_EMISSIONE
            (ID_FASCICOLO_SIEP,
             CHIAVE_ANNO,
             CHIAVE_PROGR,
             COD_UFFICIO,
             COD_MAGISTRATO,
             DATA_PASSATO_GIUDICATO,
             DATA_ARRIVO_ATTO,
             TEMPI_GIUDICATO_ARRIVO,
             DATA_ISCRIZIONE_FASCICOLO,
             TEMPI_ARRIVO_ISCRIZIONE,
             ID_EVENTO_PRIMO_ATTO,
             DATA_PRIMO_ATTO,
             COD_MOTIVO_PRIMO_ATTO,
             DESCRIZIONE_MOTIVO_PRIMO_ATTO,
             ID_EVENTO_ESECUZIONE,
             DATA_ORDINE_ESECUZIONE,
             COD_MOTIVO_ESECUZIONE,
             DESCRIZIONE_MOTIVO_ESECUZIONE,
             TEMPI_ISCRIZIONE_EMISSIONE,
             DATA_INIZIO_ISTRUTTORIA,
             DATA_FINE_ISTRUTTORIA,
             TEMPI_ISTRUTTORIA,
             DATA_INIZIO_INATTIVITA,
             DATA_FINE_INATTIVITA,
             TEMPI_INATTIVITA,
             -- Umberto 39/9/2013      GIORNI_DA_NEUTRALIZZARE)
             GIORNI_DA_NEUTRALIZZARE,
             -- Umberto  30/9/2013  inserita valorizzazione colonne aggiuntive
             COD_UFFICIO_INSERIMENTO,
             CHIAVE_PROGR_ORIG,
             DESC_UFFICIO_INSERIMENTO)
          -- Umberto 30/9/2013  Fine modifiche
          VALUES
            (v_id_fascicolo_siep,
             v_chiave_anno,
             v_chiave_progr,
             par_ufficio,
             v_cod_magistrato,
             v_data_irrevocabilita,
             v_DATA_ARRIVO_ATTO,
             v_TEMPI_GIUDICATO_ARRIVO,
             v_DATA_ISCRIZIONE,
             v_TEMPI_ARRIVO_ISCRIZIONE,
             v_ID_EVENTO_PRIMO_ATTO,
             v_DATA_PRIMO_ATTO,
             v_MOTIVO_PRIMO_ATTO,
             v_DESC_MOTIVO_PRIMO_ATTO,
             v_ID_EVENTO_ESEC,
             v_DATA_ORDINE_ESEC,
             v_MOTIVO_ESEC,
             v_DESC_MOTIVO_ESEC,
             v_TEMPI_ISCRIZIONE_EMISSIONE,
             v_DATA_INIZIO_ISTRUTTORIA,
             v_DATA_FINE_ISTRUTTORIA,
             v_TEMPI_ISTRUTTORIA,
             v_DATA_INIZIO_INATTIVITA,
             v_DATA_FINE_INATTIVITA,
             v_TEMPI_INATTIVITA,
             -- Umberto 30/9/2013          v_GIORNI_DA_NEUTRALIZZARE);
             v_GIORNI_DA_NEUTRALIZZARE,
             -- Umberto inserite nuove variabili  30/9/2013
             v_cod_ufficio_inserimento,
             v_chiave_progr_orig,
             v_desc_ufficio_inserimento);
        END IF;
        -- Umberto 30/9/2013  Fine modifiche
        COMMIT;
      END; ----fine loop

      -- LETTURA IN CICLO
      FETCH cur_fascicolo
        INTO v_id_fascicolo_siep,
             v_chiave_anno,
             v_chiave_progr,
             v_data_iscrizione,
             v_data_arrivo_atto,
             v_af8_nres,
             -- Umberto 30/9/2013             v_data_irrevocabilita;
             v_data_irrevocabilita,
             -- Umberto 30/9/2013  inserita estrazione colonne aggiuntive
             v_cod_ufficio_inserimento,
             v_chiave_progr_orig;
      -- Umberto 30/9/2013  Fine modifiche

    -- CHIUSURA DEL WHILE

    END LOOP;

    CLOSE cur_fascicolo;

    -- UMBERTO 18/10/2013  I records con magistrato a NULL vengono accorpati a quelli con magistrato a '-'
    UPDATE ISP_TEMPI_EMISSIONE
       SET COD_MAGISTRATO = '-'
     WHERE COD_MAGISTRATO IS NULL;
    -- UMBERTO 18/10/2013  FINE

    COMMIT;

  END; ---fine procedura tempi emissione

  -----------------------------------------------------------------------------------------------------------------------
  ------------------------------------PROCEDURA ATTIVITA MAGISTRATI----------------------------------------------------
  -----------------------------------------------------------------------------------------------------------------------

  PROCEDURE attivita_magistrati(par_data_inizio IN VARCHAR2,
                                par_data_fine   IN VARCHAR2,
                                --- Umberto  30/9/2013             par_ufficio      IN  VARCHAR2) IS
                                --- Umberto  30/9/2013          aggiunti 3 parametri ufficio per NGG
                                par_ufficio IN VARCHAR2) IS

    -- DEFINIZIONE VARIABILI

    v_id_fascicolo_siep FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;
    --v_anno_af8_nrgem    EVENTO.anno_protocollo%TYPE;
    --v_progr_af8_nrgem   EVENTO.progr_protocollo%TYPE;
    v_chiave_anno  FASCICOLO_SIEP.chiave_anno%TYPE;
    v_chiave_progr FASCICOLO_SIEP.chiave_progr%TYPE;
    --- Umberto  13/10/2013          aggiunte 2 VARIABILI  per NGG
    v_cod_ufficio_inserimento FASCICOLO_SIEP.cod_ufficio_inserimento%TYPE;
    v_chiave_progr_orig       FASCICOLO_SIEP.chiave_progr_orig%TYPE;
    --- Umberto 13/10/2013      Fine
    --v_cod_posizione_giuridica POSIZIONE_GIURIDICA.cod_posizione_giuridica%TYPE;
    --v_con_rev                 VARCHAR2(30);
    --v_tip_aut                 VARCHAR2(70);
    --v_tip_att                 VARCHAR2(80);
    --v_motiv                   VARCHAR2(50);
    --v_af8_nres                FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;

    --v_cod_stato_procedimento STATO_PROCEDIMENTO.cod_stato_procedimento%TYPE;
    --flag_no_riepilogo        CHAR(1);
    --tot_recl                 NUMBER;
    --tot_arre                 NUMBER;
    --v_num_anni_reclusione    NUMBER(4);
    --v_num_mesi_reclusione    NUMBER(4);
    --v_num_giorni_reclusione  NUMBER(4);
    --v_importo_multa          NUMBER(11, 2);
    --v_num_anni_arresto       NUMBER(4);
    --v_num_mesi_arresto       NUMBER(4);
    --v_num_giorni_arresto     NUMBER(4);
    --v_importo_ammenda        NUMBER(11, 2);
    --gg_tot                   NUMBER;
    --tot_pres                 NUMBER;
    --v_data_inizio            PENA_RESIDUA.data_inizio%TYPE;
    --v_data_fine              PENA_RESIDUA.data_fine%TYPE;
    --v_id_fascicolo_siep_old FASCICOLO_SIEP.fas_sie_id_fascicolo_siep%TYPE;
    v_id_evento      EVENTO.id_evento%TYPE;
    v_cod_magistrato EVENTO.cod_magistrato%TYPE;
    -- Umberto 11/10/2013  aggiunta variabile per recupero tipo provvedimento
    v_cod_tipo_provvedimento EVENTO.cod_tipo_provvedimento%TYPE;
    -- Umberto 11/10/2013  FINE
    -- MEV70 - Umberto 18/04/2019  aggiunta variabile per recupero motivo provvedimento
    v_cod_motivo_orig VARCHAR2(4);
    -- Umberto 18/04/2019 FINE
    v_cod_magistrato_ass      MAGISTRATO_COMPETENTE.mag_cod_magistrato%TYPE;
    v_cod_magistrato_comp     MAGISTRATO_COMPETENTE.mag_cod_magistrato%TYPE;
    v_cod_stato_fascicolo_res STATO_FASCICOLO_RES.cod_stato_fascicolo%TYPE := '';
    -- variabile generali
    --conta_commit NUMBER;

    --v_trovato  NUMBER;
    --v_inserito NUMBER := 0;

    --num_record_scritti NUMBER := 0;
    num_record_letti NUMBER := 0;
    --num_record_scartati         NUMBER:= 0;
    --v_anno_res             NUMBER(4) := NULL;
    --v_num_res              VARCHAR2(6) := '';
    --v_data_iscrizione      DATE;
    --v_data_arrivo_atto     DATE;
    v_data_emissione DATE;
    --v_iscrizione_emissione NUMBER := 0;
    --primo_record_letto     VARCHAR2(1) := 'N';
    --secondo_record_letto   VARCHAR2(1) := 'N';
    --terzo_record_letto     VARCHAR2(1) := 'N';
    v_id_evento_provv      EVENTO.id_evento%TYPE := '';
    v_motivo_provv         EVENTO.cod_motivo%TYPE := '';
    v_data_emissione_provv DATE;
    --v_desc_motivo_provv    CG_REF_CODES.rv_meaning%TYPE;
    --v_id_evento_rich       EVENTO.id_evento%TYPE := '';
    --v_motivo_rich          EVENTO.cod_motivo%TYPE := '';
    --v_data_emissione_rich  DATE;
    --v_desc_motivo_rich     CG_REF_CODES.rv_meaning%TYPE;
    --v_id_evento_ord        EVENTO.id_evento%TYPE := '';
    --v_motivo_ord           EVENTO.cod_motivo%TYPE := '';
    --v_data_emissione_ord   DATE;
    --v_desc_motivo_ord      CG_REF_CODES.rv_meaning%TYPE;
    --v_caso_rich            VARCHAR2(1) := 'N';
    v_tipologia VARCHAR2(2);
    --- Umberto  13/10/2013          aggiunta 1 VARIABILE  per NGG
    v_desc_ufficio_inserimento VARCHAR2(200);
    --- Umberto 13/10/2013      Fine

    -- dichiarazione del cursore

    CURSOR cur_evento IS
      SELECT a.id_evento,
             a.cod_motivo,
             a.data_emissione,
             a.cod_magistrato,
             -- Umberto 11/10/2013  aggiunto  recupero codice tipo provvedimento
             a.cod_tipo_provvedimento,
             -- Umberto 11/10/2013 Fine
             c.mag_cod_magistrato,
             b.id_fascicolo_siep,
             b.chiave_anno,
             -- Umberto 13/10/2013  inserito recupero colonne aggiuntive
             b.cod_ufficio_inserimento,
             b.chiave_progr_orig,
             -- Umberto 13/10/2013 Fine
             b.chiave_progr
        FROM EVENTO a, FASCICOLO_SIEP b, MAGISTRATO_COMPETENTE c
       WHERE a.cod_ufficio_inserimento = par_ufficio
            -- Inizio - 14 Luglio 2009 : su segnalazione telefonica di Testa
            -- Sancamillo : Eliminazione dei provvedimenti Annullati
            --   AND a.FLAG_DOCUMENTO_REGISTRATO <> 'A' -- Annullato
            --d.f. 07/2014 solo i validati
         AND a.FLAG_DOCUMENTO_REGISTRATO = 'S' -- Annullato
            -- Fine
         AND TO_DATE(TO_CHAR(a.data_emissione, 'dd/mm/yyyy'), 'dd/mm/yyyy') BETWEEN
             TO_DATE(par_data_inizio, 'dd/mm/yyyy') AND
             TO_DATE(par_data_fine, 'dd/mm/yyyy')
         AND a.fas_sie_id_fascicolo_siep = b.id_fascicolo_siep
            -- Umberto 4/10/2013      AND a.cod_ufficio_inserimento=b.cod_ufficio_inserimento
            -- Umberto 4/10/2013  inserita nuova condizione
         AND a.cod_ufficio_inserimento = par_ufficio
            -- Umberto 4/10/2013 FINE
            -- Sancamillo : Eliminato per problema sui decreti di irreperibilit??????????????????????????? : giugno 2009 richiesto da Testa
            --  AND a.cod_tipo_provvedimento NOT IN ('02','03')
            -- Umberto - 21/1/2010 -reinserito scarto decreti e ordinanze, eccetto decreto irreperibilit???????????????????????????
            -- Ticket#20231017016 - STATISTICHE UFFICIO - TEMPI ISCRIZIONI PROCEDIMENTI
            -- Vanno estratti SOLO i classe I
            -- Ticket#202312140112 - Ripristino parametri riepilogo lavoro magistrati ticket 20231017016
            -- si ritorna al vecchio. Per le sole attivit?????? vanno estratti tutti i fascicoli
            /*
             AND (   b.chiave_progr BETWEEN              1 AND          19999
                  OR b.chiave_progr BETWEEN 10000000000001 AND 10000000019999
                  OR b.chiave_progr BETWEEN 20000000000001 AND 20000000019999
                  OR b.chiave_progr BETWEEN 30000000000001 AND 30000000019999
                  OR b.chiave_progr BETWEEN         800000 AND         819999 -- Pretura
                  OR b.chiave_progr BETWEEN   900001000000 AND   919999000000 -- Pretura/RES con lettere classe I
                  OR b.chiave_progr BETWEEN  1700001000000 AND  1719999000000 -- Pretura/RES con lettere classe I
                )
            */
            -- Ticket#202312140112 - Ripristino parametri riepilogo lavoro magistrati ticket 20231017016 - FINE
            -- Ticket#20231017016 - FINE
            --bvn 29 03 2017
            /*AND (a.cod_tipo_provvedimento NOT IN ('02','03')
            OR  (a.cod_tipo_provvedimento = '02' AND a.COD_MOTIVO = '0282') )*/
            --fine bvn 29 03 2017

            --      AND b.cod_ufficio_inserimento=c.cod_ufficio_inserimento (+)
         AND b.id_fascicolo_siep = c.fas_sie_id_fascicolo_siep(+)
         AND c.data_fine IS NULL
      -- AND cod_motivo IN ('0222','0223','0224','0225','0277')
       ORDER BY id_evento;

    --
  BEGIN
    ---inizio procedura

    --conta_commit := 1;

    v_id_evento              := NULL;
    v_cod_motivo             := NULL;
    v_data_emissione         := NULL;
    v_cod_magistrato         := NULL;
    v_cod_tipo_provvedimento := NULL;
    v_id_fascicolo_siep      := NULL;
    v_chiave_anno            := NULL;
    v_chiave_progr           := NULL;
    v_cod_magistrato_ass     := NULL;
    -- Umberto 11/10/2013  impostazione nuova variabile
    v_cod_tipo_provvedimento   := NULL;
    v_desc_ufficio_inserimento := NULL;

    ---CANCELLO tabelle di appoggio
    DELETE FROM ISP_ATTIVITA_MAGISTRATI;
    COMMIT;

    OPEN cur_evento;

    FETCH cur_evento
      INTO v_id_evento,
           v_cod_motivo,
           v_data_emissione,
           v_cod_magistrato,
           -- Umberto 11/10/2013 inserita  nuova variabile
           v_cod_tipo_provvedimento,
           v_cod_magistrato_comp,
           v_id_fascicolo_siep,
           v_chiave_anno,
           v_cod_ufficio_inserimento,
           v_chiave_progr_orig,
           v_chiave_progr;

    DBMS_OUTPUT.PUT_LINE('fetch fuori ciclo ');

    -- CALCOLO DELL'ORA DI CARICAMENTO
    v_data_inserimento := SYSDATE;

    v_motivo_provv         := '';
    v_tipologia            := '';
    v_id_evento_provv      := NULL;
    v_data_emissione_provv := NULL;

    -- MEV70 - Umberto 18/04/2019  memorizzo valore originale di cod_motivo
    v_cod_motivo_orig := v_cod_motivo;
    -- MEV70 - Umberto 18/04/2019   FINE
    -- Umberto 13/10/2013  inserita nuova routine ricerca Descrizione Ufficio Inserimento
    ----RICERCA LUOGO UFFICIO INSERIMENTO
    BEGIN
      SELECT descrizione
        INTO v_desc_ufficio_inserimento
        FROM COMUNE
       WHERE cod_comune = SUBSTR(v_cod_ufficio_inserimento, 1, 6);
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_desc_ufficio_inserimento := '-';
    END;
    -- Umberto 13/10/2013  Fine modifiche

    WHILE cur_evento%FOUND LOOP
      BEGIN
        ---inizio loop

        DBMS_OUTPUT.PUT_LINE('INIZIO DEL LOOP ');

        DBMS_OUTPUT.PUT_LINE('v_id_fascicolo_siep ' || v_id_fascicolo_siep);
        DBMS_OUTPUT.PUT_LINE('v_chiave_anno ' || v_chiave_anno);
        DBMS_OUTPUT.PUT_LINE('v_chiave_progr ' || v_chiave_progr);

        v_cod_stato_fascicolo_res := NULL;
        v_motivo_provv            := '';
        v_tipologia               := '';
        v_id_evento_provv         := NULL;
        v_data_emissione_provv    := NULL;
        v_cod_magistrato_ass      := NULL;
        num_record_letti          := 0;

        num_record_letti := num_record_letti + 1;

        DBMS_OUTPUT.PUT_LINE('v_cod_motivo ' || v_cod_motivo);
        DBMS_OUTPUT.PUT_LINE('num_record_letti ' || num_record_letti);
        DBMS_OUTPUT.PUT_LINE('v_cod_magistrato ' || v_cod_magistrato);
        DBMS_OUTPUT.PUT_LINE('v_cod_magistrato_comp ' ||
                             v_cod_magistrato_comp);
        DBMS_OUTPUT.PUT_LINE('v_cod_magistrato_ass ' ||
                             v_cod_magistrato_ass);

        DBMS_OUTPUT.PUT_LINE('v_motivo_provv ' || v_motivo_provv);

        ----RICERCA LUOGO UFFICIO INSERIMENTO
        BEGIN
          SELECT descrizione
            INTO v_desc_ufficio_inserimento
            FROM COMUNE
           WHERE cod_comune = SUBSTR(v_cod_ufficio_inserimento, 1, 6);
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_desc_ufficio_inserimento := '-';
        END;
        -- Umberto 13/10/2013  Fine modifiche

        -- DIEGO 03/03/2014 DL 146/2013
        --============================================
        -- Prosecuzioni delle misure di sicurezza
        --============================================
        -- Prosecuzione Aff Prova
        IF (v_cod_motivo IN ('5470', '5471', '5472') -- MDS DL 146
           OR v_cod_motivo IN ('5480', '5481', '5482') -- TDS DL 146 su reclamo
           ) THEN
          v_tipologia          := '18'; --'SORV';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Detenzione Domiciliare + a termine
        IF (v_cod_motivo IN ('5473', '5474', '5475', '5476', '5479') -- MDS DL 146
           OR v_cod_motivo IN ('5483', '5484', '5485', '5486', '5490') -- TDS DL 146 su reclamo
           ) THEN
          v_tipologia          := '19'; --'SORV';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Semilibert???????????????????????????
        IF (v_cod_motivo IN ('5477') -- MDS DL 146
           OR v_cod_motivo IN ('5487') -- TDS DL 146 su reclamo
           ) THEN
          v_tipologia          := '21'; --'SORV';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Espiazione presso il domicilio
        IF (v_cod_motivo IN ('5478') -- MDS DL 146
           OR v_cod_motivo IN ('5489') -- TDS DL 146 su reclamo
           ) THEN
          v_tipologia          := '24'; --'SORV';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        --============================================
        -- Cessazioni delle misure di sicurezza
        --============================================
        -- Cessazione Aff Prova
        IF (v_cod_motivo IN ('0166', '0167', '0168') -- TDS
           OR v_cod_motivo IN ('5450', '5451', '5452') -- TDS DL 146 su reclamo PM
           OR v_cod_motivo IN ('5430', '5431', '5432') -- MDS DL 146
           ) THEN
          v_tipologia          := '70'; --'OE';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Cessazione Detenzione Domiciliare
        IF (v_cod_motivo IN ('0024', '0110', '0112') -- TDS
           OR v_cod_motivo IN ('5453', '5454', '5455', '5456') -- TDS DL 146 su reclamo PM
           OR v_cod_motivo IN ('5433', '5434', '5435', '5436') -- MDS DL 146
           ) THEN
          v_tipologia          := '71'; --'OE';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Cessazione Semilibert???????????????????????????
        IF (v_cod_motivo IN ('0169') -- TDS
           OR v_cod_motivo IN ('5457') -- TDS DL 146 su reclamo PM
           OR v_cod_motivo IN ('5437') -- MDS DL 146
           ) THEN
          v_tipologia          := '72'; --'OE';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Esecuzione Presso Il domicilio
        IF (v_cod_motivo IN ('0363') -- TDS
           OR v_cod_motivo IN ('5458') -- TDS DL 146 su reclamo PM
           OR v_cod_motivo IN ('5438') -- MDS DL 146
           ) THEN
          v_tipologia          := '73'; --'OE';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Indultino
        IF (v_cod_motivo IN ('0172') -- TDS
           OR v_cod_motivo IN ('5442') -- MDS DL 146
           ) THEN
          v_tipologia          := '74'; --'OE';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Differimento nelle forme della detenzione domiciliare
        IF (v_cod_motivo IN ('0111') -- TDS
           OR v_cod_motivo IN ('5459') -- TDS DL 146 su reclamo PM
           OR v_cod_motivo IN ('5439') -- MDS DL 146
           ) THEN
          v_tipologia          := '75'; --'OE';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- FINE DIEGO 03/03/2014 DL 146/2013

        -- DIEGO 29/10/2014 DL 92/2014
        --============================================
        -- DL 92
        --============================================
        IF (v_cod_motivo IN ('5491', '5492') -- Ordini di scarcerazione (detenuto/in misura)
           OR v_cod_motivo IN ('5493', '5494', '5495') -- Comunicazioni (libero/Ergastolo/Pena gi??????????????????????????? espiata)
           ) THEN
          v_tipologia          := '80'; --
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- FINE DIEGO 29/10/2014 DL 92/2014

        ---gestione OE senza sospensione
        -- Umberto  11/10/2013 modifiche per nuovi cod_motivi
        --    IF v_cod_motivo IN ('0057','0058','0059') THEN
        --     v_tipologia:='03'; --'OE_NO_SOSP';
        -- carmela 11/09/2015 modifica MEV 10 Step 3
        -- aggiunti i seguenti nuovi cod_motivo ('5501','5502','5503','5504','5505','5533','5534','5535','5536','5537')
        IF v_cod_motivo IN ('0057',
                            '0058',
                            '0059',
                            '1024',
                            '0498',
                            '0060',
                            '5501',
                            '5502',
                            '5503',
                            '5504',
                            '5505',
                            '5533',
                            '5534',
                            '5535',
                            '5536',
                            '5537') THEN
          v_tipologia := '05'; --'OE_NO_SOSP';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        ---gestione OE con sospensione
        -- carmela 11/09/2015 modifica MEV 10 Step 3
        -- aggiunti i seguenti nuovi cod_motivo ('0117','5509','5510','5511','5512','5513')
        IF v_cod_motivo IN
           ('0061', '0062', '0117', '5509', '5510', '5511', '5512', '5513') THEN
          -- Umberto 11/10/2013 modifica tipologia
          --     v_tipologia:='04';  --'OE_CON_SOSP';
          v_tipologia := '07'; --'OE_CON_SOSP';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        ---gestione OE detenzione domiciliare
        -- Umberto 11/10/2013 modifica tipologia
        --  IF v_cod_motivo IN ('0060','0063') THEN
        --    v_tipologia:='05';   --'OE_DET_DOM';
        -- carmela 11/09/2015 modifica MEV 10 Step 3
        -- aggiunti i seguenti nuovi cod_motivo ('5506','5522','5527','5507','5523','5528','5508','5524','5529')
        IF v_cod_motivo IN ('0063',
                            '0365',
                            '1023',
                            '5506',
                            '5522',
                            '5527',
                            '5507',
                            '5523',
                            '5528',
                            '5508',
                            '5524',
                            '5529') THEN
          v_tipologia := '11'; --'OE_DET_DOM';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        -- Umberto 11/10/2013 nuova tipologia
        ---gestione OE legge 199/2010
        -- carmela 11/09/2015 modifica MEV 10 Step 3
        -- aggiunti i seguenti nuovi cod_motivo ('5525','5526')
        IF v_cod_motivo IN ('0364', '0499', '5525', '5526') THEN
          v_tipologia          := '09'; --'OE_299/2010';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        --  Umberto 11/10/2013  Fine

        -- Umberto 11/10/2013 nuova tipologia
        ---gestione Richieste del PM
        IF v_cod_motivo IN ('1022', '0608') THEN
          v_tipologia          := '13'; --'RICH_PM';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        --  Umberto 11/10/2013  Fine

        -- Umberto 11/10/2013 nuova tipologia
        ---gestione Altre Richieste del PM
        IF v_cod_motivo IN ('5408',
                            '5409''5410',
                            '5411',
                            '5412',
                            '5413',
                            '0340',
                            '0345',
                            '5403',
                            '5401',
                            '5400') THEN
          v_tipologia          := '14'; --'ALTRE_RICH_PM';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        --  Umberto 11/10/2013  Fine

        -- Umberto 11/10/2013 nuova tipologia
        ---gestione Ricorso per Cassazione
        IF v_cod_motivo IN ('0342') THEN
          v_tipologia          := '15'; --'RIC_CASS;
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        --  Umberto 11/10/2013  Fine

        -- Umberto 11/10/2013 nuova tipologia
        ---gestione Pareri
        IF v_cod_motivo IN ('0351', '0336', '0337') THEN
          v_tipologia          := '16'; --'PARERI';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        --  Umberto 11/10/2013  Fine

        -- Umberto 11/10/2013 nuova tipologia
        ---gestione Visto del PM
        ---Umberto Ticket#20220222015   24/2/2022
        ---Umberto aggiunto al codice 0338, anche 0488 (Visto del PM per archiviazione)
        IF v_cod_motivo IN ('0338', '0488') THEN
          v_tipologia          := '17'; --'VISTO_PM';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        --  Umberto 11/10/2013  Fine

        ---gestione cumulo
        IF v_cod_motivo IN ('0222', '0223', '0224', '0225', '0277') THEN
          v_tipologia    := '01'; --'CUMULO';
          v_motivo_provv := v_cod_motivo;

          DBMS_OUTPUT.PUT_LINE('SONO NEL CUMULO COD_MOTIVO ' ||
                               v_cod_motivo);

          --- CERCO MAGISTRATO ASSEGNATARIO NAL CASO IL MAGISTRATO SIA NULL aggiunto il 10/01/2008
          --- POICHE' NON E OBBLIGATORIO
          IF v_cod_magistrato IS NULL THEN
            v_cod_magistrato_ass := v_cod_magistrato_comp;
          ELSE
            v_cod_magistrato_ass := v_cod_magistrato;
          END IF;

          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato ' ||
                               v_cod_magistrato);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_comp ' ||
                               v_cod_magistrato_comp);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_ass ' ||
                               v_cod_magistrato_ass);
        END IF;

        --======================================================
        -- MEV42 Statistiche Cumulo New  --> INIZIO MODIFICA <--
        --======================================================
        -- Accorpamento Nuovi Codici al Cod_Motivo 0222
        IF v_cod_motivo IN ('0630', '0636', '0641') THEN
          v_tipologia    := '01'; --'CUMULO';
          v_motivo_provv := '0222';

          DBMS_OUTPUT.PUT_LINE('SONO NEL CUMULO COD_MOTIVO ' ||
                               v_cod_motivo);

          IF v_cod_magistrato IS NULL THEN
            v_cod_magistrato_ass := v_cod_magistrato_comp;
          ELSE
            v_cod_magistrato_ass := v_cod_magistrato;
          END IF;

          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato ' ||
                               v_cod_magistrato);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_comp ' ||
                               v_cod_magistrato_comp);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_ass ' ||
                               v_cod_magistrato_ass);
        END IF;

        -- Accorpamento Nuovi Codici al Cod_Motivo 0223  MEV42
        ---IF v_cod_motivo IN ('0638','0639','0640','0643','0645','0657','0658')  THEN
        ---
        --- 18/04/2019 - Umberto MEV70  Nuovo accorpamento al Cod_Motivo 0223
        IF v_cod_motivo IN ('0638', '0640', '0643', '0645') THEN
          v_tipologia    := '01'; --'CUMULO';
          v_motivo_provv := '0223';

          DBMS_OUTPUT.PUT_LINE('SONO NEL CUMULO COD_MOTIVO ' ||
                               v_cod_motivo);

          IF v_cod_magistrato IS NULL THEN
            v_cod_magistrato_ass := v_cod_magistrato_comp;
          ELSE
            v_cod_magistrato_ass := v_cod_magistrato;
          END IF;

          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato ' ||
                               v_cod_magistrato);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_comp ' ||
                               v_cod_magistrato_comp);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_ass ' ||
                               v_cod_magistrato_ass);
        END IF;

        -- Accorpamento Nuovi Codici al Cod_Motivo 0224     MEV42
        ---IF v_cod_motivo IN ('0644','0646','0647','0648','0649','0650','0651')  THEN
        ---
        --- 18/04/2019 - Umberto MEV70  Nuovo accorpamento al Cod_Motivo 0224
        IF v_cod_motivo IN
           ('0646', '0647', '0648', '0649', '0650', '0651', '0668') THEN
          v_tipologia    := '01'; --'CUMULO';
          v_motivo_provv := '0224';

          DBMS_OUTPUT.PUT_LINE('SONO NEL CUMULO COD_MOTIVO ' ||
                               v_cod_motivo);

          IF v_cod_magistrato IS NULL THEN
            v_cod_magistrato_ass := v_cod_magistrato_comp;
          ELSE
            v_cod_magistrato_ass := v_cod_magistrato;
          END IF;

          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato ' ||
                               v_cod_magistrato);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_comp ' ||
                               v_cod_magistrato_comp);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_ass ' ||
                               v_cod_magistrato_ass);
        END IF;

        -- Accorpamento Nuovi Codici al Cod_Motivo 0225   MEV42
        ---       IF v_cod_motivo IN ('0635','0637','0642','0653','0654','0656','0661')  THEN
        ---
        --- 18/04/2019 - Umberto MEV70  Nuovo accorpamento al Cod_Motivo 0225
        IF v_cod_motivo IN ('0635', '0637', '0642', '0661', '0666', '0667') THEN
          v_tipologia    := '01'; --'CUMULO';
          v_motivo_provv := '0225';

          DBMS_OUTPUT.PUT_LINE('SONO NEL CUMULO COD_MOTIVO ' ||
                               v_cod_motivo);

          IF v_cod_magistrato IS NULL THEN
            v_cod_magistrato_ass := v_cod_magistrato_comp;
          ELSE
            v_cod_magistrato_ass := v_cod_magistrato;
          END IF;

          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato ' ||
                               v_cod_magistrato);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_comp ' ||
                               v_cod_magistrato_comp);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_ass ' ||
                               v_cod_magistrato_ass);
        END IF;

        -- Accorpamento Nuovi Codici al Cod_Motivo 0277 MEV42
        ---  IF v_cod_motivo IN ('0652','0655','0659','0660')  THEN
        ---
        --- 18/04/2019 - Umberto MEV70  Nuovo accorpamento al Cod_Motivo 0277
        IF v_cod_motivo IN ('0652', '0655', '0665') THEN
          v_tipologia    := '01'; --'CUMULO';
          v_motivo_provv := '0277';

          DBMS_OUTPUT.PUT_LINE('SONO NEL CUMULO COD_MOTIVO ' ||
                               v_cod_motivo);

          IF v_cod_magistrato IS NULL THEN
            v_cod_magistrato_ass := v_cod_magistrato_comp;
          ELSE
            v_cod_magistrato_ass := v_cod_magistrato;
          END IF;

          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato ' ||
                               v_cod_magistrato);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_comp ' ||
                               v_cod_magistrato_comp);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_ass ' ||
                               v_cod_magistrato_ass);
        END IF;

        -- =====================================================================
        -- --> FINE MODIFICA <--    MEV42 Statistiche Cumulo New
        -- =====================================================================

        -- 18/04/2019  Umberto  MEV70 - nuovi accorpamenti codici cumulo

        -- 18/04/2019 - Umberto MEV70  Nuovo accorpamento al Cod_Motivo 0656
        IF v_cod_motivo IN ('0656', '0657') THEN
          v_tipologia    := '01'; --'CUMULO';
          v_motivo_provv := '0656';

          DBMS_OUTPUT.PUT_LINE('SONO NEL CUMULO COD_MOTIVO ' ||
                               v_cod_motivo);

          IF v_cod_magistrato IS NULL THEN
            v_cod_magistrato_ass := v_cod_magistrato_comp;
          ELSE
            v_cod_magistrato_ass := v_cod_magistrato;
          END IF;

          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato ' ||
                               v_cod_magistrato);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_comp ' ||
                               v_cod_magistrato_comp);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_ass ' ||
                               v_cod_magistrato_ass);
        END IF;
        -- 18/04/2019 - Umberto MEV70  Nuovo accorpamento al Cod_Motivo 0659
        IF v_cod_motivo IN ('0659', '0660', '0664') THEN
          v_tipologia    := '01'; --'CUMULO';
          v_motivo_provv := '0659';

          DBMS_OUTPUT.PUT_LINE('SONO NEL CUMULO COD_MOTIVO ' ||
                               v_cod_motivo);

          IF v_cod_magistrato IS NULL THEN
            v_cod_magistrato_ass := v_cod_magistrato_comp;
          ELSE
            v_cod_magistrato_ass := v_cod_magistrato;
          END IF;

          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato ' ||
                               v_cod_magistrato);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_comp ' ||
                               v_cod_magistrato_comp);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_ass ' ||
                               v_cod_magistrato_ass);
        END IF;

        -- 18/04/2019 - Umberto MEV70  Nuovo accorpamento al Cod_Motivo 0654
        IF v_cod_motivo IN ('0654', '0662') THEN
          v_tipologia    := '01'; --'CUMULO';
          v_motivo_provv := '0654';
          DBMS_OUTPUT.PUT_LINE('SONO NEL CUMULO COD_MOTIVO ' ||
                               v_cod_motivo);

          IF v_cod_magistrato IS NULL THEN
            v_cod_magistrato_ass := v_cod_magistrato_comp;
          ELSE
            v_cod_magistrato_ass := v_cod_magistrato;
          END IF;

          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato ' ||
                               v_cod_magistrato);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_comp ' ||
                               v_cod_magistrato_comp);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_ass ' ||
                               v_cod_magistrato_ass);
        END IF;
        -- 18/04/2019 - Umberto MEV70  Nuovo accorpamento al Cod_Motivo 0663
        IF v_cod_motivo IN ('0639', '0644', '0653', '0658', '0663') THEN
          v_tipologia    := '01'; --'CUMULO';
          v_motivo_provv := '0663';
          DBMS_OUTPUT.PUT_LINE('SONO NEL CUMULO COD_MOTIVO ' ||
                               v_cod_motivo);

          IF v_cod_magistrato IS NULL THEN
            v_cod_magistrato_ass := v_cod_magistrato_comp;
          ELSE
            v_cod_magistrato_ass := v_cod_magistrato;
          END IF;

          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato ' ||
                               v_cod_magistrato);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_comp ' ||
                               v_cod_magistrato_comp);
          DBMS_OUTPUT.PUT_LINE('CUMULO v_cod_magistrato_ass ' ||
                               v_cod_magistrato_ass);
        END IF;
        ------ 18/04/2019 - Umberto Fine interventi MEV70
        ---gestione computo
        IF v_cod_motivo IN ('0212', '0121', '0213') THEN
          -- Umberto 11/10/2013 modifica tipologia
          --   v_tipologia    := '02';  --'COMPUTO';
          v_tipologia := '03'; --'COMPUTO';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv := v_cod_motivo;
          ----CERCO MAGISTRATO ASSEGNATARIO NAL CASO IL MAGISTRATO SIA NULL  aggiunto il 15/01/2008
          ----POICHE  NON VIENE INSERITO
          IF v_cod_magistrato IS NULL THEN
            v_cod_magistrato_ass := v_cod_magistrato_comp;
          ELSE
            v_cod_magistrato_ass := v_cod_magistrato;
          END IF;

          DBMS_OUTPUT.PUT_LINE('COMP v_cod_magistrato ' ||
                               v_cod_magistrato);
          DBMS_OUTPUT.PUT_LINE('COMP v_cod_magistrato_comp ' ||
                               v_cod_magistrato_comp);
          DBMS_OUTPUT.PUT_LINE('COMP v_cod_magistrato_ass ' ||
                               v_cod_magistrato_ass);
        END IF;

        /*-- Umberto 11/10/2013 eliminati conteggi Decisioni del GE
         ---decisioni del GE
         IF v_cod_motivo IN ('0158','0159','0160','0174','0175','0300','0301')  THEN
            v_tipologia           := '06';   --'GE_BENEFICI';
            v_motivo_provv        := v_cod_motivo;
            v_cod_magistrato_ass  := v_cod_magistrato;
         END IF;

         ---decisioni del GE
         IF v_cod_motivo IN ('0265')  THEN
            v_tipologia           := '07';  ---'GE_SOSPENSIONI';
            v_motivo_provv        := v_cod_motivo;
            v_cod_magistrato_ass  := v_cod_magistrato;
         END IF;

         ---decisioni del GE
         IF (v_cod_motivo BETWEEN '0800' AND '0807') OR
            (v_cod_motivo BETWEEN '0810' AND '0829') THEN
             v_tipologia          := '09';  --'GE_ALTRO';
             v_motivo_provv       := v_cod_motivo;
             v_cod_magistrato_ass := v_cod_magistrato;
         END IF;
        --- Umberto 11/10/2013 Fine --*/

        ---OS con decorrenza e scadenza
        -- Umberto 11/10/2013 rivista tipologia e cod_motivo
        -- IF v_cod_motivo IN ('0082')  THEN
        --   v_tipologia           := '23';  --'OS_DEC_SCA_PENA';
        IF v_cod_motivo IN ('0082',
                            '0226',
                            '0227',
                            '0371',
                            '0228',
                            '0229',
                            '0372',
                            '0373',
                            '0442') AND v_cod_tipo_provvedimento = '09' THEN
          v_tipologia := '45'; --'OS_DEC_SCA_PENA';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        ---Revoca decreti di sospensione
        -- Umberto 11/10/2013 nuova tipologia
        --    IF v_cod_motivo IN ('0078','0079','0080')  THEN
        --      v_tipologia           := '24';  --'REVOCA_DEC_SOSP';
        -- carmela 11/09/2015 modifica MEV 10 Step 3
        -- aggiunti i seguenti nuovi cod_motivo ('5514','5515','5516','5517','5518','5519','5520','5521','0497','5530','5531','5532')
        IF v_cod_motivo IN ('0078',
                            '0079',
                            '0080',
                            '0495',
                            '5514',
                            '5515',
                            '5516',
                            '5517',
                            '5518',
                            '5519',
                            '5520',
                            '5521',
                            '0497',
                            '5530',
                            '5531',
                            '5532') THEN
          v_tipologia := '12'; --'REVOCA_DEC_SOSP';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        ---Decreti di irreperibilit???????????????????????????
        IF v_cod_motivo IN ('0282') THEN
          -- Umberto 11/10/2013 rivista tipologia
          --    v_tipologia           := '25';  --'DEC_IRREPERIBILITA';
          v_tipologia := '37'; --'DEC_IRREPERIBILITA';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        ---decisioni della Sorveglianza
        -- Umberto 11/10/2013 rivista intera classificazione decisioni della Sorveglianza
        ---Espulsione
        -- IF v_cod_motivo IN ('0276')  THEN
        --   v_tipologia           := '10'; --'SORV_ESPULSIONE';
        --IF v_cod_motivo IN ('2141','0926')  THEN
        IF v_cod_motivo IN ('0926') THEN
          v_tipologia := '35'; --'SORV_ESPULSIONE';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        -- DIEGO 14/07/2014 il verbale di espulsione (27-2141) non deve essere
        -- classificato. Va classificato solo il provvedimento.
        -- n.b. hanno lo stesso codice 2141
        IF v_cod_motivo IN ('2141') AND v_cod_tipo_provvedimento <> '27' THEN
          v_tipologia          := '35'; --'SORV_ESPULSIONE';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        ---decisioni della Sorveglianza
        IF v_cod_motivo IN ('0923', '0922', '0081', '0083') THEN
          -- Umberto 11/10/2013 rivista tipologia
          --   v_tipologia           := '11';  --'SORV_LIB_ANT';
          v_tipologia := '39'; --'SORV_LIB_ANT';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        ---decisioni della Sorveglianza
        -- Umberto 11/10/2013 rivista intera classificazione decisioni della Sorveglianza
        ---Comunicazione Affidamento Servizio Sociale
        -- IF v_cod_motivo IN ('0370','0244','0245','0371','0226','0227','2006','0242','2146',
        --                    '2161','0305','0015','0374','0390','0391','2147','2145','0304',
        --                    '0306','2162','2160','0014','0086','0376','0375','0389','0388',
        --                    '0235','0236','0243','0450')  THEN
        --   v_tipologia           := '13'; --'SORV_AFFIDAMENTO';
        -- Umberto 7/2/2014  aggiunto oggetto 2008 - Ammisione provvisoria S.S.
        IF v_cod_motivo IN
           ('0370', '0244', '0245', '0371', '0226', '0227', '2006', '2008') AND
           v_cod_tipo_provvedimento = '12' THEN
          v_tipologia          := '18'; --'SORV_AFFIDAMENTO';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        -- MEV_9-SIEP - nuovo codici per la concessione 678
        -- MEV_92_2024 -- sostituiti codici da 0680 a 0695 con  nuovi codici da 1416 a 1426
        IF v_cod_motivo IN
          --   ('0680', '0681', '0690', '0691', '0692') AND
           ('1416', '1417', '1421', '1422', '1423') AND
          --   v_cod_tipo_provvedimento = '12' THEN
          --  v_cod_tipo_provvedimento = '12' THEN
           v_cod_tipo_provvedimento IN ('06', '04', '09', '12') THEN
          v_tipologia := '18'; --'SORV_AFFIDAMENTO';

          v_motivo_provv := v_cod_motivo;

          --  IF v_cod_motivo = '0680' THEN
          IF v_cod_motivo IN ('1416', '1421', '1423') THEN
            v_motivo_provv := '0226';
          END IF;
          --        IF v_cod_motivo = '0681' THEN
          IF v_cod_motivo IN ('1417', '1422') THEN
            v_motivo_provv := '0227';
          END IF;

          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- MEV_9-SIEP - FINE
        -- MEV_92_2024 - FINE

        ---decisioni della Sorveglianza
        -- Umberto 11/10/2013 rivista intera classificazione decisioni della Sorveglianza
        ---Comunicazione Detenzione Domiciliare
        -- IF v_cod_motivo IN ('0372','2151','2150','2149','2293','2166','0410','0308','0307',
        --                    '0089','0309','0087','0088','0016','0228','0229','0380','0379',
        --                    '0378','0381','0377','0385','0384','0386','0387','0237','0392',
        --                    '0238','0240','0239','0453','0459','0458','0452','0451','2005',
        --                    '2165','2167','2164')  THEN
        IF v_cod_motivo IN ('0228', '0229', '0372', '2005', '0011', '0012') AND
           v_cod_tipo_provvedimento = '12' THEN
          v_tipologia := '19'; --'SORV_DET_DOM';
          -- Umberto 11/10/2013 Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        -- MEV_9-SIEP - nuovo codici per la concessione 678
        -- MEV_92_2024 -- sostituiti codici da 0680 a 0695 con  nuovi codici da 1416 a 1426
        IF v_cod_motivo IN
          --   ('0682', '0693') AND
           ('1418', '1424') AND
          --   v_cod_tipo_provvedimento = '12' THEN
           v_cod_tipo_provvedimento IN ('06', '04', '09', '12') THEN
          v_tipologia := '19'; --'SORV_AFFIDAMENTO';
          --  v_motivo_provv       := v_cod_motivo;
          --   IF v_cod_motivo = '0682' THEN
          --  IF v_cod_motivo = '1418' THEN
          v_motivo_provv := '0228';
          --  END IF;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- MEV_9-SIEP - FINE
        -- MEV_92_2024 - FINE

        ---decisioni della Sorveglianza
        IF v_cod_motivo IN ('0011', '0197', '2340', '0427') THEN
          v_tipologia          := '17'; --'SORV_DET_DOM_TERMINE';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        ---decisioni della Sorveglianza
        -- Umberto 11/10/2013 rivista intera classificazione decisioni della Sorveglianza
        ---Comunicazione Semilibert???????????????????????????
        -- IF v_cod_motivo IN ('0373','2148','2163','0310','0091','0382','0383','0393','0454')  THEN
        -- v_tipologia           := '14';  --'SORV_SEMILIBERTA';
        IF v_cod_motivo IN ('0373') AND v_cod_tipo_provvedimento = '12' THEN
          v_tipologia := '21'; --'SORV_SEMILIBERTA';
          -- Umberto 11/10/2013 Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        -- MEV_9-SIEP - nuovo codici per la concessione 678
        -- MEV_92_2024 -- sostituiti codici da 0680 a 0695 con  nuovi codici da 1416 a 1426
        IF v_cod_motivo IN
          --   ('0683', '0694') AND
           ('1419', '1425') AND
          --   v_cod_tipo_provvedimento = '12' THEN
           v_cod_tipo_provvedimento IN ('06', '04', '09', '12') THEN
          v_tipologia    := '21'; --'SORV_AFFIDAMENTO';
          v_motivo_provv := '0373';
          --  IF v_cod_motivo = '0683' THEN
          -- IF v_cod_motivo = '1419' THEN
          -- v_motivo_provv := '0005';
          --  END IF;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        IF v_cod_motivo IN ('1420', '1426') AND
          --   v_cod_tipo_provvedimento = '12' THEN
           v_cod_tipo_provvedimento IN ('06', '04', '09', '12') THEN
          v_tipologia          := '33'; --'SORV_SOSPENSIONE'
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- MEV_9-SIEP - FINE
        -- MEV_92_2024 - FINE

        ---decisioni della Sorveglianza
        -- Umberto 11/10/2013 rivista intera classificazione decisioni della Sorveglianza
        ---Comunicazione Indultino
        -- IF v_cod_motivo IN ('2245','0455','0456','0457','2289','0311','0196')  THEN
        --   v_tipologia           := '15';  --'SORV_INDULTINO';
        IF v_cod_motivo IN ('2245') AND v_cod_tipo_provvedimento = '12' THEN
          v_tipologia := '23'; --'SORV_INDULTINO';
          -- Umberto 11/10/2013 Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        ---decisioni della Sorveglianza
        -- Umberto 11/10/2013 rivista intera classificazione decisioni della Sorveglianza
        ---Comunicazione Liberazione Condizionale
        IF v_cod_motivo IN ('0025') AND v_cod_tipo_provvedimento = '12' THEN
          v_tipologia          := '25'; --'SORV_LIB_COND';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Umberto 11/10/2013 Fine

        ---decisioni della Sorveglianza
        -- Umberto 11/10/2013 rivista intera classificazione decisioni della Sorveglianza
        ---Revoca Misure Alternative
        IF v_cod_motivo IN ('0014',
                            '0015',
                            '0016',
                            '0086',
                            '0087',
                            '0088',
                            '0089',
                            '0090',
                            '0091',
                            '0074',
                            '0026',
                            '0196')
          --- Umberto 23/11/2021 Ticket#20211119014 bisogna scartare le ordinanze e i decreti della Sorveglianza inseriti dall'ufficio esecuzione
           AND v_cod_tipo_provvedimento NOT IN ('02', '03')
        --- fine modifica  Ticket#20211119014
         THEN
          v_tipologia          := '27'; --'REV_MIS_ALT';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Umberto 11/10/2013 Fine

        ---decisioni della Sorveglianza
        -- Umberto 11/10/2013 rivista intera classificazione decisioni della Sorveglianza
        ---Differimento Misure Alternative
        -- IF v_cod_motivo IN ('0221','0354','0355')  THEN
        --   v_tipologia           := '21';  ---'SORV_DIFFERIMENTO';
        IF v_cod_motivo IN ('0221') THEN
          v_tipologia          := '31'; ---'SORV_DIFFERIMENTO';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Umberto 11/10/2013 Fine

        ---decisioni della Sorveglianza
        -- Umberto 11/10/2013 rivista intera classificazione decisioni della Sorveglianza
        ---Sospensioni
        IF v_cod_motivo IN
           ('0900', '0901', '00902', '0903', '0920', '0921', '0937') THEN
          v_tipologia          := '33'; --'REV_MIS_ALT';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Umberto 11/10/2013 Fine

        ---decisioni della Sorveglianza
        -- Umberto 11/10/2013 rivista intera classificazione decisioni della Sorveglianza
        ---Rigetto Misure Alternative
        -- IF v_cod_motivo IN ('9000','9001','9002')  THEN
        --   v_tipologia           := '20';  --'SORV_RIGETTO_MA';
        IF v_cod_motivo IN ('9000') THEN
          v_tipologia := '29'; --'SORV_RIGETTO_MA';
          -- Umberto 11/10/2013 Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        -- carmela 29/04/2014
        -- Inizio modifica MEV 10 Step 3
        -- Sospensione provvisoria Arresti Domiciliari '2741','2742','2743','2756'
        -- Ripristino Arresti Domiciliari '2748','2749','2752','2753','2754','2755','2758','2759'
        -- Revoca Arresti Domiciliari '2744','2746','2747','2757'
        IF v_cod_motivo IN ('2741',
                            '2742',
                            '2743',
                            '2756',
                            '2748',
                            '2749',
                            '2752',
                            '2753',
                            '2754',
                            '2755',
                            '2758',
                            '2759',
                            '2744',
                            '2746',
                            '2747',
                            '2757') THEN
          v_tipologia          := '26';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Fine modifica MEV 10 Step 3

        ---VALE SOLO PER ATTIVITA DI UFFICIO
        ---decisioni della Sorveglianza
        -- IF v_cod_motivo in ('0250','0166','0025','0030','0042','0108','0191','0199','0023',
        --                     '0027','0073','0029','0077','0017','0036','0113','0038','0203',
        --                     '0028','0026','0035','0074','0034','0262','0043')  THEN
        --    v_tipologia           := '22';  ---;'SORV_ALTRO';
        --    v_motivo_provv        := v_cod_motivo;
        --    v_cod_magistrato_ass  := v_cod_magistrato;
        -- END IF;
        -- Umberto 11/10/2013 Rivista classificazione Archiviazioni
        -- IF  v_cod_motivo BETWEEN '0006' AND '0009'        OR     ---modificato il 21/01/2007
        --    v_cod_motivo IN ('0019','0022')               OR
        --    v_cod_motivo BETWEEN '0096' AND '0099'        OR
        --    v_cod_motivo IN ('0119','0120','0353','7002') OR
        --    v_cod_motivo BETWEEN '0214' AND '0216'        OR
        --    v_cod_motivo BETWEEN '0400' AND '0409'        OR
        --    v_cod_motivo BETWEEN '0411' AND '0426'        THEN
        --    v_tipologia     := '28';  --'ARCHIVIAZIONI';
        --    v_motivo_provv  := v_cod_motivo;
        -- Umberto 07/11/2025 MEV 2025_48 aggiunto codice '0736' archiviazione per passaggio classe
        IF v_cod_motivo IN ('0006',
                            '0007',
                            '0008',
                            '0009',
                            '0120',
                            '0353',
                            '0407',
                            '0408',
                            '0404',
                            '0478',
                            '0736') THEN
          v_tipologia    := '47'; --'ARCHIVIAZIONI NLP';
          v_motivo_provv := v_cod_motivo;
        END IF;
        IF v_cod_motivo IN ('0096',
                            '0097',
                            '0098',
                            '0099',
                            '0473',
                            '0479',
                            '0480',
                            '0481',
                            '0587',
                            '0588',
                            '0401') THEN
          v_tipologia    := '49'; --'ARCHIVIAZIONI FINE ESPIAZIONE';
          v_motivo_provv := v_cod_motivo;
        END IF;
        IF v_cod_motivo IN ('0405',
                            '0411',
                            '0412',
                            '0413',
                            '0414',
                            '0415',
                            '0416',
                            '0417',
                            '0418',
                            '0419',
                            '0420',
                            '0476',
                            '0492',
                            '1120',
                            '1121',
                            '0402') THEN
          v_tipologia    := '51'; --'ARCHIVIAZIONI FINE ESPIAZIONE';
          v_motivo_provv := v_cod_motivo;
        END IF;
        IF v_cod_motivo IN
           ('0403', '0406', '0424', '0425', '0426', '0474', '0477') THEN
          v_tipologia    := '53'; --'ARCHIVIAZIONI PROVV SORVEGLIANZA';
          v_motivo_provv := v_cod_motivo;
        END IF;
        IF v_cod_motivo IN ('0019', '0022', '0400') THEN
          v_tipologia    := '55'; --'ARCHIVIAZIONI PERDITA COMPETENZA';
          v_motivo_provv := v_cod_motivo;
        END IF;
        IF v_cod_motivo IN ('0421', '0422') THEN
          v_tipologia    := '57'; --'ARCHIVIAZIONI PROVV ALTRE AUTORITA'';
          v_motivo_provv := v_cod_motivo;
        END IF;
        IF v_cod_motivo IN
           ('0119', '0214', '0215', '0216', '0423', '0474', '0477') THEN
          v_tipologia    := '59'; --'ARCHIVIAZIONI PROVV SORVEGLIANZA';
          v_motivo_provv := v_cod_motivo;
        END IF;
        ----CERCO MAGISTRATO ASSEGNATARIO NAL CASO IL MAGISTRATO SIA NULL  aggiunto il 10/01/2008
        ----POICHE PER 0411-0417 0476 0477 NON VIENE INSERITO
        DBMS_OUTPUT.PUT_LINE('SONO prima delle pene accessorie ' ||
                             v_cod_motivo);
        -- Umberto 11/10/2013 inserita nuova tipologia
        -- Esecuzione Pene Accessorie
        IF v_cod_motivo BETWEEN '5001' AND '5009' OR
           v_cod_motivo BETWEEN '5011' AND '5039' OR
           v_cod_motivo BETWEEN '5041' AND '5047' OR
           v_cod_motivo IN ('5061',
                            '5062',
                            '5063',
                            '5064',
                            '5066',
                            '5067',
                            '5068',
                            '5069',
                            '5070',
                            '5074') OR
           v_cod_motivo BETWEEN '5077' AND '5084' OR
           v_cod_motivo IN
           ('5086', '5088', '5089', '5090', '5091', '5092', '5093', '5399') OR
           v_cod_motivo BETWEEN '5099' AND '5134' THEN
          v_tipologia    := '69'; --'ESECUZIONE PENA ACCESSORIE';
          v_motivo_provv := v_cod_motivo;
        END IF;
        -- UMBERTO 11/10/2013 Fine

        IF v_cod_magistrato IS NULL THEN
          v_cod_magistrato_ass := v_cod_magistrato_comp;
        ELSE
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        DBMS_OUTPUT.PUT_LINE('ARCHIV v_cod_magistrato ' ||
                             v_cod_magistrato);
        DBMS_OUTPUT.PUT_LINE('ARCHIV v_cod_magistrato_comp ' ||
                             v_cod_magistrato_comp);
        DBMS_OUTPUT.PUT_LINE('ARCHIV v_cod_magistrato_ass ' ||
                             v_cod_magistrato_ass);
        --     END IF;

        ----aggiunto il 12/07/2007
        ----RICHIESTE APPLICAZIONE INDULTO AMNISTIA
        IF v_cod_motivo IN ('0290', '0293', '0294') THEN
          --  Umberto 11/10/2013  rivista tipologia
          --  v_tipologia           := '26';  --'RICHIESTE APPLICAZIONE INDULTO AMNISTIA
          v_tipologia := '40'; --'RICHIESTE APPLICAZIONE INDULTO AMNISTIA
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        ---Richieste al giudice di esecuzione - altre
        --  Umberto 11/10/2013  rivista tipologia e cod_motivo
        --- IF v_cod_motivo IN ('0344','0346','0348','0349')  OR
        --  v_cod_motivo BETWEEN '0320' AND '0331'  THEN
        --  v_tipologia           := '27';  --'RIC_GE';
        -- UMBERTO 21/01/2014  Rivista catalogazione  classe 43 in 41,42,43
        --- IF v_cod_motivo IN ('0344','0346','0348','0349','0611','1108','1100','1101','1102','1103','1104','1105','1106','1107')
        IF v_cod_motivo IN ('0344', '0346', '0348', '0349') OR
           v_cod_motivo BETWEEN '0320' AND '0331' THEN
          v_tipologia := '41'; --'RIC_GE';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        ---Richieste al giudice di esecuzione - altre
        --  Umberto 21/01/2014  rivista tipologia e cod_motivo
        IF v_cod_motivo IN
           ('1100', '1101', '1102', '1103', '1104', '1105', '1106') THEN
          v_tipologia := '42'; --'RIC_GE';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        ---Richieste al giudice di esecuzione - altre
        --  Umberto 21/01/2014  rivista tipologia e cod_motivo
        IF v_cod_motivo IN ('0611', '1107') THEN
          v_tipologia := '43'; --'RIC_GE';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        ---Richieste al giudice di esecuzione - altre
        --  Umberto 21/01/2014  rivista tipologia e cod_motivo
        IF v_cod_motivo IN ('1108') THEN
          v_tipologia := '44'; --'RIC_GE';
          --  Umberto 11/10/2013  Fine
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        --- Trasmissione istanza per concessione misure alternativa art. (art. 656 co. 5??????????????????????????? c.p.p.)
        --  Umberto 14/10/2013  inserita nuova tipologia
        IF v_cod_motivo IN ('0104', '0105') THEN
          v_tipologia          := '61'; --'ESTINZIONI';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        --  Umberto 14/10/2013  Fine

        --- Estinzioni
        --  Umberto 11/10/2013  inserita nuova tipologia
        -- UMBERTO 21/01/2014  Rivista catalogazione  classe 63 in 63,64
        --  IF v_cod_motivo IN ('0411','0476','0477','0492','1120','0424','0425') THEN
        IF v_cod_motivo IN ('0477', '0424', '0425') THEN
          v_tipologia          := '63'; --'ESTINZIONI';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        --  Umberto 11/10/2013  Fine

        -- UMBERTO 21/01/2014  Rivista catalogazione  classe 63 in 63,64
        --  IF v_cod_motivo IN ('0411','0476','0477','0492','1120','0424','0425') THEN
        IF v_cod_motivo IN ('0411', '0492', '1120', '0476') THEN
          v_tipologia          := '64'; --'ESTINZIONI';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        --  Umberto 11/10/2013  Fine
        --- Trasmissione atti per conv. pene pecuniarie
        --  Umberto 11/10/2013  inserita nuova tipologia
        IF v_cod_motivo IN ('0934') THEN
          v_tipologia          := '65'; --'TRASM CONV PP';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        --  Umberto 11/10/2013  Fine

        --- Estinzioni
        --  Umberto 11/10/2013  inserita nuova tipologia
        IF v_cod_motivo IN ('0396') THEN
          v_tipologia          := '67'; --'TRASM CONV PP';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        -- MEV_9-SIEP - Nuovi codici per applicazione Provvisoria
        -- MEV_92_2024 - Codici riutilizzati per Applicazione M.A. 678
        -- Affidamento in prova
        IF v_cod_motivo IN ('1400', '1401', '1410', '1411', '1412') AND
           v_cod_tipo_provvedimento IN ('06', '04', '09', '12') THEN
          v_tipologia          := '18'; --'SORV_AFFIDAMENTO';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Detenzione Domiciliare
        IF v_cod_motivo IN ('1402', '1413') AND
           v_cod_tipo_provvedimento IN ('06', '04', '09', '12') THEN
          v_tipologia          := '19'; --'SORV_DETENZIONE';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Semiliberta'
        IF v_cod_motivo IN ('2007', '1403', '1414') AND
           v_cod_tipo_provvedimento IN ('06', '04', '09', '12') THEN
          v_tipologia          := '21'; --'SORV_SEMILIBERTA';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;
        -- Sospensione' - NON ESISTE ATTUALMENTE UNA TIPOLOGIA nemmeno per i codice esistenti
        IF v_cod_motivo IN ('1404', '1415') AND
           v_cod_tipo_provvedimento IN ('06', '04', '09', '12') THEN
          v_tipologia          := '33'; --'SORV_SEMILIBERTA';
          v_motivo_provv       := v_cod_motivo;
          v_cod_magistrato_ass := v_cod_magistrato;
        END IF;

        -- RATIFICHE
        -- MEV_92_2024 RATIFICHE NON PIU' PREVISTE
        /*IF v_cod_motivo IN ('5460', '5461', '5462', '5463', '5464')
        /*AND v_cod_tipo_provvedimento = '12'*/ /* THEN
                                      v_tipologia          := '18'; --'SORV_AFFIDAMENTO';
                                      v_motivo_provv       := v_cod_motivo;
                                      v_cod_magistrato_ass := v_cod_magistrato;
                                    END IF;
                                    -- Detenzione Domiciliare
                                    IF v_cod_motivo IN ('5465', '5466') /*AND v_cod_tipo_provvedimento = '12'*/ /*THEN
                                      v_tipologia          := '19'; --'SORV_DETENZIONE';
                                      v_motivo_provv       := v_cod_motivo;
                                      v_cod_magistrato_ass := v_cod_magistrato;
                                    END IF;
                                    -- Semiliberta'
                                    IF v_cod_motivo IN ('5467', '5468') /*AND  v_cod_tipo_provvedimento = '12'*/ /* THEN
                                      v_tipologia          := '21'; --'SORV_SEMILIBERTA';
                                      v_motivo_provv       := v_cod_motivo;
                                      v_cod_magistrato_ass := v_cod_magistrato;
                                    END IF;
                                    -- Sospensione???*/
        -- MEV_9-SIEP - FINE
        -- MEV_92_2024 - FINE

        --  Umberto 11/10/2013  Fine
        DBMS_OUTPUT.PUT_LINE('SONO della FINE ' || v_cod_motivo || ' ' ||
                             v_motivo_provv);
        IF v_motivo_provv IS NOT NULL THEN
          DBMS_OUTPUT.PUT_LINE('FIN v_cod_magistrato ' || v_cod_magistrato);
          DBMS_OUTPUT.PUT_LINE('FIN v_cod_magistrato_comp ' ||
                               v_cod_magistrato_comp);
          DBMS_OUTPUT.PUT_LINE('FIN v_cod_magistrato_ass ' ||
                               v_cod_magistrato_ass);
          DBMS_OUTPUT.PUT_LINE('eseguo insert ');

          v_id_evento_provv      := v_id_evento;
          v_data_emissione_provv := v_data_emissione;

          INSERT INTO ISP_ATTIVITA_MAGISTRATI
            (ID_FASCICOLO_SIEP,
             CHIAVE_ANNO,
             CHIAVE_PROGR,
             COD_UFFICIO,
             COD_MAGISTRATO,
             DATA_EMISSIONE,
             COD_MOTIVO,
             TIPOLOGIA,
             --  Umberto 13/10/2013  inserita valorizzazione 3 nuove colonne per NGG
             COD_UFFICIO_INSERIMENTO,
             CHIAVE_PROGR_ORIG,
             DESC_UFFICIO_INSERIMENTO,
             --  Umberto 11/10/2013  Fine
             COD_MOTIVO_ORIG,
             ID_EVENTO)
          VALUES
            (v_id_fascicolo_siep,
             v_chiave_anno,
             v_chiave_progr,
             par_ufficio,
             v_cod_magistrato_ass,
             v_data_emissione_provv,
             v_motivo_provv,
             v_tipologia,
             --  Umberto 13/10/2013  inserita valorizzazione 3 nuove colonne per NGG
             v_cod_ufficio_inserimento,
             v_chiave_progr_orig,
             v_desc_ufficio_inserimento,
             --  Umberto 11/10/2013  Fine
             v_cod_motivo_orig,
             v_id_evento_provv);
        END IF;

        COMMIT;

      END; ----fine loop

      -- LETTURA IN CICLO
      FETCH cur_evento
        INTO v_id_evento,
             v_cod_motivo,
             v_data_emissione,
             v_cod_magistrato,
             v_cod_tipo_provvedimento,
             v_cod_magistrato_comp,
             v_id_fascicolo_siep,
             v_chiave_anno,
             v_cod_ufficio_inserimento,
             v_chiave_progr_orig,
             v_chiave_progr;
      -- MEV70 18/04/2019 Umberto
      v_cod_motivo_orig := v_cod_motivo;
      -- MEV70 18/04/2019 Umberto Fine
    -- CHIUSURA DEL WHILE
    END LOOP;

    CLOSE cur_evento;

    COMMIT;

    -- UMBERTO 18/10/2013  I records con magistrato a NULL vengono accorpati a quelli con magistrato a '-'
    UPDATE ISP_ATTIVITA_MAGISTRATI
       SET COD_MAGISTRATO = '-'
     WHERE COD_MAGISTRATO IS NULL;
    -- UMBERTO 18/10/2013  FINE

  END; ---fine procedura attivita magistrati

END ISPETTORATO;
/
