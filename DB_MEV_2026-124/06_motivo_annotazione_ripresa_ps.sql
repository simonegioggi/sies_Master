-- MEV_2026-124_SG - 3.4 Annotazione Ripresa Esecuzione Pena Sostitutiva
-- Nuovo motivo provvedimento per l'evento (tipo 13 - tipo provvedimento 25 Annotazione)
-- inserito alla validazione della ripresa e visibile in Elenco Altri Atti.
-- Nessuna nuova classe Act*: non servono righe in FUNZIONE / FUNZIONE_PROFILO.
-- Idempotente.
DECLARE
  n NUMBER;
BEGIN
  SELECT COUNT(*) INTO n FROM cg_ref_codes
   WHERE rv_domain = 'MOTIVO_PROVVEDIMENTO' AND rv_low_value = '9301';
  IF n = 0 THEN
    INSERT INTO cg_ref_codes (rv_domain, rv_low_value, rv_meaning)
    VALUES ('MOTIVO_PROVVEDIMENTO', '9301', 'Annotazione Ripresa Esecuzione Pena Sostitutiva');
  END IF;
  COMMIT;
END;
/
