-- 20260512-MEV_2026-124_SG: 3.5 Data Revoca e data avvenuto pagamento PP nell'elenco periodi pena sostitutiva
ALTER TABLE PERIODO_ALTRA_SANZIONE ADD (DATA_REVOCA DATE, DATA_PAGAMENTO_PP DATE);
COMMENT ON COLUMN PERIODO_ALTRA_SANZIONE.DATA_REVOCA IS 'Data revoca della pena sostitutiva (MEV_2026-124)';
COMMENT ON COLUMN PERIODO_ALTRA_SANZIONE.DATA_PAGAMENTO_PP IS 'Data avvenuto pagamento pena pecuniaria - revoca art. 660 c.15 cpp (MEV_2026-124)';
