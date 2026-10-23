-- MEV_2026-105 - Legge 140/2026 Step 1 - Dati di riferimento per art. 94 ter DPR 309/90
-- Tutte le righe sono CLONATE dalle righe gia' presenti per l'art. 89 DPR 309/90
-- e sono idempotenti (se la riga di destinazione esiste, viene saltata).
--
-- ASSUNZIONI DICHIARATE (da confermare prima dell'esecuzione in ambiente reale)
--  A1. Posizioni giuridiche (dominio CG_REF_CODES 'POSIZIONE_GIURIDICA'):
--        56 Libero ex 94 ter                         <- clone di 10 (Libero)          [maschera L]
--        57 Custodia cautelare altra causa AD 94 ter <- clone di 81 (art. 89)         [maschera L3]
--        58 Custodia cautelare questa causa AD 94 ter<- clone di 70 (art. 89)         [maschera EA]
--        59 Arresti domiciliari ex 94 / ex 656 c.10  <- clone di 84 (art. 89)         [maschera EA]
--      La riga di origine della 56 e' la posizione "10 - Libero": se l'Amministrazione preferisce
--      un'altra origine, cambiare il valore nella tabella v_pos.
--  A2. Tipo misura cautelare "94 ter": nel documento di analisi NON e' indicato alcun codice.
--      Si assume 'CL' (successivo a 'CK' = altra causa AD ex art. 89), clonato da 'CK'.
--      Il codice 'CL' e' usato anche nel Java (ActGestionePosizioneGiuridica / ActLoadGestionePosizioneGiuridica).
--  A3. Motivi provvedimento (dominio 'MOTIVO_PROVVEDIMENTO'): codici da analisi (0760-0767).
--      RV_HIGH_VALUE (sigla usata dal Java) e' un'ASSUNZIONE; deve iniziare per LS/OE come le analoghe:
--        0760 LS-LIB-94T         <- 'LS-LIB'              (OE con sospensione, Libero)
--        0761 OE-S-AC-94T        <- 'OE-S-AC-ADDPR'       (OE con sospensione, altra causa AD)
--        0762 OE-CS-94T          <- 'OE-CS-ADDPR'         (OE con sospensione, AD questa causa)
--        0765 OE-DET-AC-CC-94T   <- 'OE-DET-AC-CC-ADDPR'  (OE non sospensivo, detenuto altra causa AD)
--        0766 OE-QC-94T          <- 'OE-QC-AD'            (OE non sospensivo, carcerazione con traduzione)
--        0763 revoca altra causa AD 94 ter  <- motivo 5521
--        0764 revoca libero 94 ter          <- motivo 0078
--        0767 revoca AD 94 ter              <- motivo 5516
--  A4. DETTAGLIO_PROVVEDIMENTO e TEMPLATE sono clonati dal motivo analogo (ID_TEMPLATE con suffisso _94T).
--      I NUOVI documenti Word (SIEP_OE_ARRD modificato, SIEP_RS_OLQNI, SIEP_RS_DACNI_M, ...) sono forniti
--      dall'Amministrazione: sostituire il file corrispondente e, se cambia il nome, aggiornare TEMPLATE.
--  A5. Istanza (3.3): nuova voce "Concessione Detenzione Domiciliare ex art. 94 ter DPR 309/90" nel dominio
--      CONTENUTO_ISTANZA, clonata dalla voce art. 89 omologa (descrizione contenente '89' e 'omiciliar');
--      codice = max numerico del dominio + 1 (da verificare).
--  A6. Se le tabelle hanno colonne chiave tecniche con sequence (es. ID), l'INSERT di tipo ROWTYPE va adattato.
SET SERVEROUTPUT ON
DECLARE
  PROCEDURE clone_ref(p_domain VARCHAR2, p_src VARCHAR2, p_dst VARCHAR2, p_meaning VARCHAR2) IS
    r CG_REF_CODES%ROWTYPE;
    n NUMBER;
  BEGIN
    SELECT COUNT(*) INTO n FROM CG_REF_CODES WHERE RV_DOMAIN = p_domain AND RV_LOW_VALUE = p_dst;
    IF n > 0 THEN
      DBMS_OUTPUT.PUT_LINE('SKIP ' || p_domain || ' ' || p_dst || ' (esiste)');
      RETURN;
    END IF;
    SELECT * INTO r FROM CG_REF_CODES WHERE RV_DOMAIN = p_domain AND RV_LOW_VALUE = p_src AND ROWNUM = 1;
    r.RV_LOW_VALUE := p_dst;
    r.RV_MEANING := p_meaning;
    INSERT INTO CG_REF_CODES VALUES r;
    DBMS_OUTPUT.PUT_LINE('OK   ' || p_domain || ' ' || p_dst || ' <- ' || p_src);
  END;

  -- Clona un motivo di provvedimento con nuova sigla; restituisce il codice sorgente tramite p_src_cod
  PROCEDURE clone_motivo(p_dst VARCHAR2, p_sigla_src VARCHAR2, p_sigla_dst VARCHAR2, p_meaning VARCHAR2) IS
    r CG_REF_CODES%ROWTYPE;
    n NUMBER;
  BEGIN
    SELECT COUNT(*) INTO n FROM CG_REF_CODES WHERE RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND RV_LOW_VALUE = p_dst;
    IF n > 0 THEN
      DBMS_OUTPUT.PUT_LINE('SKIP MOTIVO ' || p_dst || ' (esiste)');
      RETURN;
    END IF;
    SELECT * INTO r FROM CG_REF_CODES
     WHERE RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND RV_HIGH_VALUE = p_sigla_src AND ROWNUM = 1;
    r.RV_LOW_VALUE := p_dst;
    r.RV_HIGH_VALUE := p_sigla_dst;
    r.RV_MEANING := p_meaning;
    INSERT INTO CG_REF_CODES VALUES r;
    DBMS_OUTPUT.PUT_LINE('OK   MOTIVO ' || p_dst || ' <- ' || p_sigla_src);
  END;

  -- Clona DETTAGLIO_PROVVEDIMENTO e TEMPLATE dal motivo sorgente a quello destinazione
  PROCEDURE clone_dettaglio(p_src VARCHAR2, p_dst VARCHAR2) IS
    d DETTAGLIO_PROVVEDIMENTO%ROWTYPE;
    t TEMPLATE%ROWTYPE;
    n NUMBER;
  BEGIN
    FOR c IN (SELECT * FROM DETTAGLIO_PROVVEDIMENTO WHERE COD_MOTIVO = p_src) LOOP
      SELECT COUNT(*) INTO n FROM DETTAGLIO_PROVVEDIMENTO
       WHERE COD_TIPO_EVENTO = c.COD_TIPO_EVENTO AND COD_TIPO_PROVVEDIMENTO = c.COD_TIPO_PROVVEDIMENTO
         AND COD_MOTIVO = p_dst;
      IF n = 0 THEN
        d := c;
        d.COD_MOTIVO := p_dst;
        INSERT INTO DETTAGLIO_PROVVEDIMENTO VALUES d;
      END IF;
    END LOOP;
    FOR c IN (SELECT * FROM TEMPLATE WHERE COD_MOTIVO = p_src) LOOP
      SELECT COUNT(*) INTO n FROM TEMPLATE WHERE ID_TEMPLATE = c.ID_TEMPLATE || '_94T';
      IF n = 0 THEN
        t := c;
        t.ID_TEMPLATE := c.ID_TEMPLATE || '_94T';
        t.COD_MOTIVO := p_dst;
        INSERT INTO TEMPLATE VALUES t;
      END IF;
    END LOOP;
    DBMS_OUTPUT.PUT_LINE('OK   DETTAGLIO/TEMPLATE ' || p_dst || ' <- ' || p_src);
  END;

  FUNCTION cod_by_sigla(p_sigla VARCHAR2) RETURN VARCHAR2 IS
    v CG_REF_CODES.RV_LOW_VALUE%TYPE;
  BEGIN
    SELECT RV_LOW_VALUE INTO v FROM CG_REF_CODES
     WHERE RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND RV_HIGH_VALUE = p_sigla AND ROWNUM = 1;
    RETURN v;
  END;
BEGIN
  -- A1 - posizioni giuridiche 56 / 57 / 58 / 59
  clone_ref('POSIZIONE_GIURIDICA', '10', '56', 'Libero ex art 94 ter dpr 309/90');
  clone_ref('POSIZIONE_GIURIDICA', '81', '57',
            'Libero - Custodia Cautelare per Altra Causa - Regime di Arresti Domiciliari ex art 94 ter dpr 309/90');
  clone_ref('POSIZIONE_GIURIDICA', '70', '58',
            'Custodia Cautelare per Questa Causa - Regime di Arresti Domiciliari ex art 94 ter dpr 309/90');
  clone_ref('POSIZIONE_GIURIDICA', '84', '59',
            'Arresti Domiciliari ex art 94 dpr 309/90 - ex art. 656 comma 10 cpp');

  -- A2 - tipo misura cautelare
  clone_ref('TIPO_MISURA_CAUTELARE', 'CK', 'CL',
            'Custodia Cautelare per Altra Causa - Regime di Arresti Domiciliari ex art 94 ter dpr 309/90');

  -- A3 - motivi provvedimento (OE con sospensione, OE non sospensivo)
  clone_motivo('0760', 'LS-LIB', 'LS-LIB-94T',
               'Ordine di esecuzione con sospensione - Libero ex art 94 ter dpr 309/90');
  clone_motivo('0761', 'OE-S-AC-ADDPR', 'OE-S-AC-94T',
               'Ordine di esecuzione con sospensione - Altra causa Arresti Domiciliari ex art 94 ter dpr 309/90');
  clone_motivo('0762', 'OE-CS-ADDPR', 'OE-CS-94T',
               'Ordine di esecuzione con sospensione - Arresti Domiciliari ex art 94 ter dpr 309/90');
  clone_motivo('0765', 'OE-DET-AC-CC-ADDPR', 'OE-DET-AC-CC-94T',
               'Ordine di esecuzione - Detenuto per altra causa Arresti Domiciliari ex art 94 ter dpr 309/90');
  clone_motivo('0766', 'OE-QC-AD', 'OE-QC-94T',
               'Ordine di esecuzione - Carcerazione con traduzione, questa causa Arresti Domiciliari ex art 94 ter dpr 309/90');

  -- A3 - revoche sospensione (nel dominio le sigle revoca non sono usate dal Java: si clona anche l'HIGH_VALUE)
  FOR rv IN (SELECT '0763' dst, '5521' src, 'Revoca sospensione - Altra causa Arresti Domiciliari ex art 94 ter dpr 309/90' m FROM DUAL
             UNION ALL SELECT '0764', '0078', 'Revoca sospensione - Libero ex art 94 ter dpr 309/90' FROM DUAL
             UNION ALL SELECT '0767', '5516', 'Revoca sospensione - Arresti Domiciliari ex art 94 ter dpr 309/90' FROM DUAL) LOOP
    clone_ref('MOTIVO_PROVVEDIMENTO', rv.src, rv.dst, rv.m);
  END LOOP;

  -- A4 - dettaglio provvedimento e template (clone dal motivo analogo)
  clone_dettaglio('0061', '0760');
  clone_dettaglio(cod_by_sigla('OE-S-AC-ADDPR'), '0761');
  clone_dettaglio(cod_by_sigla('OE-CS-ADDPR'), '0762');
  clone_dettaglio('5521', '0763');
  clone_dettaglio('0078', '0764');
  clone_dettaglio(cod_by_sigla('OE-DET-AC-CC-ADDPR'), '0765');
  clone_dettaglio(cod_by_sigla('OE-QC-AD'), '0766');
  clone_dettaglio('5516', '0767');

  -- A5 - istanza: nuova voce "Contenuto"
  DECLARE
    r CG_REF_CODES%ROWTYPE;
    v_new VARCHAR2(30);
    n NUMBER;
  BEGIN
    SELECT COUNT(*) INTO n FROM CG_REF_CODES
     WHERE RV_DOMAIN = 'CONTENUTO_ISTANZA' AND UPPER(RV_MEANING) LIKE '%94 TER%' AND UPPER(RV_MEANING) LIKE '%OMICILIAR%';
    IF n = 0 THEN
      SELECT * INTO r FROM CG_REF_CODES
       WHERE RV_DOMAIN = 'CONTENUTO_ISTANZA' AND RV_MEANING LIKE '%89%' AND UPPER(RV_MEANING) LIKE '%OMICILIAR%'
         AND ROWNUM = 1;
      SELECT TO_CHAR(MAX(TO_NUMBER(RV_LOW_VALUE)) + 1) INTO v_new FROM CG_REF_CODES
       WHERE RV_DOMAIN = 'CONTENUTO_ISTANZA' AND REGEXP_LIKE(RV_LOW_VALUE, '^[0-9]+$');
      r.RV_LOW_VALUE := v_new;
      r.RV_MEANING := 'Concessione Detenzione Domiciliare ex art. 94 ter DPR 309/90';
      INSERT INTO CG_REF_CODES VALUES r;
      DBMS_OUTPUT.PUT_LINE('OK   CONTENUTO_ISTANZA ' || v_new);
    END IF;
  END;

  COMMIT;
END;
/
