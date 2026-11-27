-- MEV_2026-124_SG - 3.9 Campo "Ore" per conversione in Lavoro di Pubblica Utilita'
-- Da eseguire PRIMA del deploy del codice (le SELECT su DEPOSITO_ORDINANZA_PC includono la colonna).
DECLARE
  n NUMBER;
BEGIN
  SELECT COUNT(*) INTO n FROM user_tab_columns
   WHERE table_name = 'DEPOSITO_ORDINANZA_PC' AND column_name = 'NUM_ORE_LPU';
  IF n = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE DEPOSITO_ORDINANZA_PC ADD NUM_ORE_LPU NUMBER';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN DEPOSITO_ORDINANZA_PC.NUM_ORE_LPU IS ''Ore di Lavoro di Pubblica Utilita'''' (conversione pena pecuniaria) - MEV_2026-124''';
  END IF;
END;
/
