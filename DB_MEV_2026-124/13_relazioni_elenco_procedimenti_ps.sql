-- 20261126-MEV_2026-124_SG - Relazioni con le funzioni figlie (dettaglio, stampa) per la funzione 25020412,
-- copiate da 32010012. Nessuna voce di menu: il menu e' condiviso con gli altri branch. Idempotente.
INSERT INTO relazione_funzione (fun_id_funzione, fun_id_funzione_figlia, cod_tipo_visualizzazione,
                                label_funzione, ordine_visualizzazione, immagine)
SELECT 25020412, r.fun_id_funzione_figlia, r.cod_tipo_visualizzazione, r.label_funzione,
       r.ordine_visualizzazione, r.immagine
  FROM relazione_funzione r
 WHERE r.fun_id_funzione = 32010012
   AND NOT EXISTS (SELECT 1 FROM relazione_funzione x
                    WHERE x.fun_id_funzione = 25020412
                      AND x.fun_id_funzione_figlia = r.fun_id_funzione_figlia);
COMMIT;
/
