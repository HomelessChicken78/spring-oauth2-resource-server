# Comment (documentazione)

Questo documento descrive il modello Comment e gli endpoint HTTP esposti dal controller /posts/{postId}/comments.

## Modello Comment
- id: ObjectId (MongoDB) — identificatore del commento
- postId: ObjectId — riferimento al post a cui il commento appartiene
- content: String — contenuto del commento
- author: String — nickname dell'autore (impostato automaticamente dal servizio)
- createdAt: LocalDateTime — timestamp di creazione (automatico con Auditing)
- updatedAt: LocalDateTime — timestamp dell'ultimo aggiornamento (automatico con Auditing)

## Note generali
- Gli id dei post e dei commenti devono essere ObjectId validi: se non lo sono, viene sollevata una BadRequestException.
- Azioni sensibili (eliminare) sono autorizzate solo per l'autore del commento, l'autore del post o per ruoli privilegiati (ADMIN, MANAGER).
- La modifica di un commento è consentita solo all'autore del commento stesso.
- Alla creazione il campo author viene preso dal profilo dell'utente autenticato (CurrentUserProvider).

## Endpoint /posts/{postId}/comments

1) POST /posts/{postId}/comments: crea un nuovo commento per il post specificato con i parametri passati dal json della richiesta

2) GET /posts/{postId}/comments?page={page}: restituisce una lista paginata di commenti del post specificato (parametro page è obbligatorio, valore di default è 1)

3) DELETE /posts/{postId}/comments/{commentId}: elimina un commento esistente (consentito solo all'autore del commento, l'autore del post, ADMIN o MANAGER)

4) PATCH /posts/{postId}/comments/{commentId}: aggiorna il contenuto del commento specificato utilizzando i dati inviati nella richiesta (consentito solo all'autore del commento)
