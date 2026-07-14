# Post (documentazione)

Questo documento descrive il modello Post e gli endpoint HTTP esposti dal controller /posts.

## Modello Post
- id: ObjectId (MongoDB) — identificatore del post
- title: String — titolo del post
- content: String — contenuto del post
- topic: String — argomento del post
- author: String — nickname dell'autore (impostato automaticamente dal servizio, non inviato dal client alla creazione)
- relatedPosts: Set<ObjectId> — riferimenti ad altri post (max 7, validazione via @Size)

## Note generali
- Gli id dei post devono essere ObjectId validi: se non lo sono, viene sollevata una BadRequestException.
- Azioni sensibili (eliminare, modificare titolo/contenuto, aggiungere/rimuovere related) sono autorizzate solo per l'autore del post o per ruoli privilegiati (ADMIN, MANAGER).
- Alla creazione il campo author viene preso dal profilo dell'utente autenticato (CurrentUserProvider).
- La ricerca supporta il parametro author="me" per filtrare i propri post.

## Endpoint /posts

1) POST /posts: crea un nuovo post con i parametri passati dal json della richiesta

2) DELETE /posts/{idPost}: elimina un post esistente (consentito solo all'autore, ADMIN o MANAGER)

3) GET /posts/search: restituisce una lista paginata di post con filtri opzionali per titolo, autore e argomento

4) GET /posts/{idPost}: recupera i dettagli completi di un singolo post specificato tramite ID

5) PATCH /posts/{idPost}/changeTitle: aggiorna il titolo del post specificato utilizzando i dati inviati nella richiesta

6) PUT /posts/{idPost}/changeContent: aggiorna il contenuto del post specificato utilizzando i dati inviati nella richiesta

7) PUT /posts/{idPost}/relatedPosts/{idRelatedPost}: aggiunge un post correlato alla lista dei riferimenti del post indicato

8) DELETE /posts/{idPost}/relatedPosts/{idRelatedPost}: rimuove un post correlato dalla lista dei riferimenti del post indicato