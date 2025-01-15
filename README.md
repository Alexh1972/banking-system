# J. POO Morgan Chase & Co.
## Descriere
J. POO Morgan Chase & Co. are in spate un sistem inovator de e-banking care
are functionalitatile de baza a unei aplicatii de banking. Avand in vedere
volumul mare de date, se doreste ca acest sistem sa fie unul eficient si
sigur, avand in vedere gestionarea corecta a conturilor fiecarui utilizator
si punerea la dispozitie a mai multor actiuni esentiale pentru utilizatori.
## Implementare
Actiunile in cadrul acestui sistem sunt reprezentate de cate o clasa ce extinde
o clasa de baza `Action` care **suprascrie** metoda care va executa corespunzator
acea actiune. Crearea unei actiuni este facilitata printr-un `Factory` care
va transforma numele actiunii intr-o instanta a actiunii. In acest mod se
va incepe intr-un mod facil executia actiunii, prin apelarea metodei `execute()`.
**Banca** si resursele acesteia, cum ar fi **baza de date** si relatiile dintre 
entitati pot fi retinute intr-o clasa de tip `Singleton`. Relatiile dintre
entitati sunt stocate in **hash tabel-uri**, astfel incat query-urile sa se
poata face intr-un mod rapid.
**Comerciantii** pot avea diferite strategii de cashback, acestea vor fi retinute
in banca, iar implementarea fiecarei strategii se va face prin clase de tip `Strategy`
pentru care metoda de **update si obtinere a rate-ului** de cashback vor fi suprascrise.
Pentru calculul cashbackurilor, se trece prin lista de tranzactii ale utilizatorilor
folosind `stream-uri`, astfel filtrandu-le doar pe cele are sunt plati catre comercianti.
**Comisionul** depinde de **service plan-ul** fiecarui utilizator, care este 
implementat ca un `Enum`, pentru a retine in mod facil informatiile. In acest mod
sunt implementate toate clasele care reprezinta tipuri (tipuri de card, conturi, etc),
in care se retine numele tipului ca string asociat unei valori de enum.
**Entitatile** acestei banci sunt reprezentate de **useri** care pot avea multiple
**conturi** care au asociate mai multe **carduri** (generale sau de unica 
folosinta). Fiecarui cont si user, le vor fi asociate mai multe **tranzactii** care
fac posibila observarea actiunilor in timp a fiecarui utilizator. Entitatile,
pentru a avea un raspuns detaliat in cadrul fiecarei actiuni, vor trebui
convertite in format **JSON**, iar acest lucru va fi posibil prin implementarea
unui `Visitator` care contine o metoda pentru fiecare dintre entitati. Aplicatia de
banking poate oferi reporturi despre conturile de business, acestea fiind de 2 tipuri:
transaction si commerciant. Pentru fiecare din aceste tipuri de reporturi, s-a creat
o interfata care sa permita implementari multiple de tip `Visitator`.
Cardurile si conturile vor avea campuri care nu trebuie neaparat initializate odata cu
instanta, folosindu-se astfel de un `Builder`. Un feature este contul de business
care va contine mai multi asociati cu diferite roluri, fiecare avand permisiuni
diferite si limite. Pentru fiecare asociat se vor retine sumele depozitate si
cheltuite.
Pentru a avea un istoric al fiecarei actiuni, rezultatele sau erorile acestora
sunt retinute prin intermediul **tranzactiilor**. Pentru ca o actiune poate
genera tranzactii in cadrul mai multor conturi si utilizatori, acestia vor
fi notificati cu ajutorul unui **Observer**. Utilizatorii si conturile
vor trebui doar sa isi adauge tranzactiile intr-o lista in momentul in care
sunt notificati de catre banca. Tranzactiile vor fi apoi afisate in cazul in
care se doreste **printarea** acestora sau **filtrarea** in functie de timestamp 
sau  filtrarea doar a tranzactiilor generate de plata cu cardul, care vor 
informa utilizatorul ce sume a platit fiecarui comerciant.
## Actiuni
- **Cont:** crearea, stergerea conturilor, adaugarea de fonduri sau alias-uri.
Crearea conturilor si adaugarea de fonduri, alias-uri, in cazul in care sunt 
facute de un utilizator existent, vor fi mereu valabile, dar stergerea va crea
erori, reflectate prin intermediul tranzactiilor, in momentul in care se doreste
stergerea unui cont cu bani. Crearea unui cont va adauga o tranzactie corespunzatoare.
- **Actiuni noi cont:** retragerea banilor din conturile de economie se va face
doar in anumite conditii, respectand limita legala de varsta de 21 de ani, aceasta
fiind calculata pe baza zilei de nastere a utilizatorului, retinuta ca LocalDateTime,
iar banii vor fi transferati de pe un cont de economii, in cel mai vechi cont
clasic.
- **Card:** crearea, stergerea cardurilor si verificarea statusului. Fiecare
dintre aceste actiuni va genera o tranzactie despre rezultatul actiunii.
Verificarea unui card consta in verificarea daca contul asociat are o suma
de bani mai mare decat balanta minima, iar in cazul in care nu se respecta
aceasta conditie, cardul va fi blocat.
- **Actiuni noi card:** retragerea banilor va prelua un comision calculat in functie
de planul utilizatorului.
- **Informatii despre utilizator / cont:** printarea utilizatorului si a
tranzactiilor asociate acestuia, filtrate, eventual, dupa timestamp sau dupa
tipul acestora, astfel utilizatorul poate sa isi vada cheltuielile totale
pentru fiecare comerciant.
- **Actiuni noi:** conturile business pot fi acum monitorizate prin cererea
unui report ce poate fi de tip **transaction** sau **commerciant**. Acest report
contin ori sumele depozitate si cele cheltuite, ori comerciantii catre care s-au
facut plati si persoanele care au platit.
- **Plati:** de tipul cont-cont, cont-comerciant.
In cazul in care un cont incearca sa plateasca, iar balanta este sub cea minima,
plata nu se va efectua, iar aceasta va genera o tranzactie corespunzatoare, chiar si
in cazul in care, in cadrul impartirii banilor, un cont ramane fara bani, toti 
utilizatorii vor fi notificati. Daca plata se face cu cardul, catre un comerciant, 
se vor verifica mai multe lucruri:
daca balanta este sub cea minima, cardul va fi blocat, iar daca balanta se apropie
foarte mult de acest minim, cardul va avea un status de tip 'warning'. Pentru fiecare
plata catre comercianti va interveni calcularea de cashback, care sunt de 2 tipuri:
cashbackul in functie de numarul de tranzactii si cel in functie de suma platita pana
la momentul curent.
- **Dobanda:** schimbarea si incasarea dobanzii. Schimbarea dobanzii va genera
in cadrul contului o tranzactie care va spune noua dobanda.
## Transactiile existente
- **Cont:** adaugarea si eroarea la stergerea acestuia, in cazul in care contul
mai are bani in el, dar si schimbarea dobanzii contului.
- **Card:** crearea si stergerea cardului cu succes sau eroare la verificarea
statusului unui card.
- **Plati:** plata cu cardul, plata catre un cont sau impartirea platii intre
mai multe conturi, cand acestea nu au erori, dar si eroare de fonduri insuficiente
la impartirea platilor sau la cele normale.
