-- MEV_2026-124_SG - 3.8 Migrazione dei procedimenti da U144 a U142 e soppressione del contenuto U144
-- Idempotente: se non restano riferimenti a U144 non fa nulla.
-- Il motivo (oggetto) 3190 e i tenori restano invariati: cambia solo il contenuto.
DECLARE
  TYPE t_list IS TABLE OF VARCHAR2(100);
  l_tc t_list := t_list(
    'GENERALE_PROCEDIMENTO.COD_OGGETTO_PROCEDIMENTO',
    'RIFERIMENTO_FASCICOLO_SIUS.COD_OGGETTO_PROCEDIMENTO',
    'ULTERIORE_ISTANZA.COD_OGGETTO_PROCEDIMENTO',
    'TEMPLATE.COD_OGGETTO_PROCEDIMENTO',
    'DECRETO_ORDINANZA_SIEP.COD_OGGETTO_PROCEDIMENTO',
    'DEPOSITO_ORDINANZA_PC.OGGETTO_PROCEDIMENTO',
    'DEPOSITO_SENTENZA.OGGETTO_PROCEDIMENTO',
    'NUOVA_ISTANZA.COD_CONTENUTO',
    'NUOVA_ISTANZA_BONIF.COD_CONTENUTO',
    'NUOVA_ISTANZA_SXBA.COD_CONTENUTO',
    'STATO_ESEC_TITOLO_CUMULATO.COD_CONTENUTO_ISTANZA');
  l_tab VARCHAR2(60);
  l_col VARCHAR2(60);
  l_n   NUMBER;
BEGIN
  FOR i IN 1 .. l_tc.COUNT LOOP
    l_tab := SUBSTR(l_tc(i), 1, INSTR(l_tc(i), '.') - 1);
    l_col := SUBSTR(l_tc(i), INSTR(l_tc(i), '.') + 1);
    EXECUTE IMMEDIATE 'UPDATE ' || l_tab || ' SET ' || l_col || ' = ''U142'' WHERE ' || l_col || ' = ''U144''';
    l_n := SQL%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE(l_tab || '.' || l_col || ': ' || l_n || ' righe migrate');
  END LOOP;

  -- Soppressione del contenuto U144 e degli esiti residui
  DELETE FROM cg_ref_codes WHERE rv_domain = 'ESITO_TENORE' AND rv_high_value = 'U144';
  DELETE FROM cg_ref_codes WHERE rv_domain = 'MOTIVO_PROVVEDIMENTO' AND rv_high_value = 'U144';
  DELETE FROM cg_ref_codes WHERE rv_domain = 'OGGETTO_PROCEDIMENTO' AND rv_low_value = 'U144';
  COMMIT;
END;
/
