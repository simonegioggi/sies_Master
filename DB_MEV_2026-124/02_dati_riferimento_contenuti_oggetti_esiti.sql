-- MEV_2026-124_SG - 3.7/3.8 Contenuti, oggetti ed esiti
-- Idempotente. Eseguire dopo 01 e PRIMA di 03 (la soppressione di U144 e' nello script 03,
-- dopo la migrazione dei riferimenti).

-- Contenuto U142 rinominato
UPDATE cg_ref_codes
   SET rv_meaning = 'Conversione pene pecuniarie per mancato pagamento (artt. 71 - 102 - 103 L. 689/81 - 55 d. lgs. 274/00)'
 WHERE rv_domain = 'OGGETTO_PROCEDIMENTO' AND rv_low_value = 'U142';

-- Nuovo oggetto sotto "Esecuzione delle pene sostitutive" (U126)
INSERT INTO cg_ref_codes (rv_domain, rv_low_value, rv_high_value, rv_abbreviation, rv_meaning)
SELECT 'MOTIVO_PROVVEDIMENTO', '2914', 'U126', 'art. 71 - 103 L. 689/1981',
       'Detenzione Domiciliare Sostitutiva (art. 71 - 103 L. 689/1981)'
  FROM dual
 WHERE NOT EXISTS (SELECT 1 FROM cg_ref_codes
                    WHERE rv_domain = 'MOTIVO_PROVVEDIMENTO' AND rv_low_value = '2914');

-- Oggetto 3190 riagganciato al contenuto U142
UPDATE cg_ref_codes
   SET rv_high_value = 'U142',
       rv_meaning = 'Revoca e Conversione pene pecuniarie sostitutive per mancato pagamento (art.71 L. 689/81)'
 WHERE rv_domain = 'MOTIVO_PROVVEDIMENTO' AND rv_low_value = '3190';

-- Esiti di revoca e conversione (3222/3223/3224) riagganciati a U142
UPDATE cg_ref_codes
   SET rv_high_value = 'U142'
 WHERE rv_domain = 'ESITO_TENORE' AND rv_low_value IN ('3222', '3223', '3224');

-- Esiti duplicati di U144 (equivalenti a 3239/3216/3215 di U142)
DELETE FROM cg_ref_codes
 WHERE rv_domain = 'ESITO_TENORE' AND rv_high_value = 'U144'
   AND rv_low_value IN ('3225', '3226', '3227');

-- Template di ordinanza della revoca/conversione agganciati a U142 (stessi ID)
UPDATE template SET cod_oggetto_procedimento = 'U142'
 WHERE cod_oggetto_procedimento = 'U144';

COMMIT;
