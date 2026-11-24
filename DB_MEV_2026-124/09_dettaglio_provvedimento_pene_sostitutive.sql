-- 20261124-MEV_2026-124_SG - 3.1.2 / 3.5.2 SIEP
-- Dettaglio degli eventi di annotazione pena sostitutiva in Dettaglio Procedimento / Elenco Provvedimenti PM.
-- Idempotente.
DECLARE
  n NUMBER;
  PROCEDURE ins(p_motivo VARCHAR2, p_action VARCHAR2) IS
  BEGIN
    SELECT COUNT(*) INTO n FROM dettaglio_provvedimento
     WHERE cod_tipo_evento = '01' AND cod_tipo_provvedimento = '04' AND cod_motivo = p_motivo;
    IF n = 0 THEN
      INSERT INTO dettaglio_provvedimento (cod_tipo_evento, cod_tipo_provvedimento, cod_motivo, action_dettaglio)
      VALUES ('01', '04', p_motivo, p_action);
    END IF;
  END;
BEGIN
  FOR m IN (SELECT column_value AS v FROM TABLE(sys.odcivarchar2list('2900', '2901'))) LOOP
    ins(m.v, 'siap.siep.sanzionesostitutiva.action.ActDettaglioAnnotazioneProvvedimentoPS');
  END LOOP;
  FOR m IN (SELECT column_value AS v FROM TABLE(sys.odcivarchar2list(
      '2910', '2911', '2912', '2913', '2914', '2935', '2936', '2937', '2938', '2940', '2941', '3200', '3190'))) LOOP
    ins(m.v, 'siap.siep.sanzionesostitutiva.action.ActDettaglioAnnotazioneRevocaConversionePS');
  END LOOP;
  COMMIT;
END;
/
