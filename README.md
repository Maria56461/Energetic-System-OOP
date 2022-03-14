# Energetic-System-OOP
This is a project implemented in Java for the Object-Oriented Programming Course. This is an energetic system with consumers, producers and distributors.

# Proiect Energy System Etapa 2

## Despre

Proiectare Orientata pe Obiecte, Seriile CA, CD
2020-2021

<https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa2>

Student: Barbu Maria-Alexandra, grupa 325CD 

## Rulare teste

Clasa Test#main
  * ruleaza solutia pe testele din checker/, comparand rezultatele cu cele de  
referinta
  * ruleaza checkstyle
  * entry-point-ul programului este clasa Main 

Detalii despre teste: checker/README

Biblioteci necesare pentru implementare:
* Jackson Core 
* Jackson Databind 
* Jackson Annotations
* Org.Json.Simple

Tutorial Jackson JSON: 
<https://ocw.cs.pub.ro/courses/poo-ca-cd/laboratoare/tutorial-json-jackson>

## Implementare

### Entitati (Clasele folosite, enum-uri, interfete)

- Consumer: cuprinde toate proprietatile unui consumator, inclusiv
distribuitorul la care are contract, luna curenta a contractului, pretul
contractului si datoria din luna precedenta. 
- Distributor: distribuitorul cu toate proprietatile sale, inclusiv pretul
contractului, lista de producatori de la care primeste energie, costul de
productie. Implementeaza interfata Observer. Metoda "getProductionCost()"
calculeaza costul de productie conform formulei date. 
- MonthlyUpdate: toate schimbarile de input care au loc intr-o singura luna.
- Energy Type: enumeratie cu toate tipurile de energie 
- Producer: producatorul cu toate proprietatile sale, inclusiv un istoric cu
id-urile distribuitorilor din fiecare luna si numarul curent de distribuitori 
- Observable: clasa intermediara ce contine o lista de producatori care va juca
rol de Subiect in design pattern-ul Observer.
- DistributorChange: toate schimbarile referitoare la distribuitor care pot
aparea intr-o luna. 
- ProducerChange: toate schimbarile referitoare la producator care pot
aparea intr-o luna.    
- EnergyChoiceStrategyType: enumeratie cu toate tipurile de energie pe care un
producator le poate oferi. 
- StrategyForChoosingProducers: interfata care cuprinde antetul functiei
"chooseProducers" 
- QuantityStrategy, GreenStrategy, PriceStrategy- mostenesc interfata
anterioara
- Input: contine numarul "numberOfTurns" si cele 4 liste primite ca input al
problemei- producatori, consumatori, distribuitori si schimbari lunare 
- CitireInput: foloseste bibliotecile Jackson si realizeaza citirea unui fisier
de tip Json, returnand un obiect de tip Input
- DistributorOut, ConsumerOut, ProducerOut: informatiile despre un
distribuitor, consumator, respectiv producator, care vor fi scrise in fisierul
de Output in urma rularii programului
- Contract: datele despre un contract care trebuie afisate in output. 
- MonthlyStatus: informatiile dintr-o anumita luna despre ce distribuitori au
luat energie de la un producator- aceste date vor fi afisate ca output 
- Output: cele trei liste (de consumatori, distribuitori si producatori) care
trebuie scrise in fisierul de iesire     

### Flow

 La inceputul programului, se citesc informatiile dintr-un fisier de tip Json
 si se formeaza un obiect de tip Input. 
 In luna zero (runda initiala):
- fiecare distribuitor isi seteaza lista de producatori in functie de strategia
sa, alegand mereu dintre producatorii care inca nu au atins numarul maxim de
distribuitori. Cand un producator este ales, i se incrementeaza numarul de
distribuitori si i se actualizeaza lista cu id-urile distribuitorilor din luna
respectiva. Functia "chooseProducers" sorteaza lista de producatori primita ca
parametru dupa un Comparator din clasa Producer (ales in functie de strategia
utilizata). Se parcurge apoi lista sortata si se aleg producatori pana cand
cantitatea de energie necesara distribuitorului este satisfacuta. 
- fiecare distribuitor isi seteaza costul de productie, profitul si pretul
contractului.
- se gaseste distribuitorul cu cel mai mic pret al contractului: 
"bestDistributor" 
- pentru fiecare consumator, setez ca distribuitor pe acela gasit anterior
(care ofera cel mai bun pret). Setez pretul aferent al contractului si
actualizez numarul de clienti ai distribuitorului ales. 
- fiecare consumator isi primeste venitul lunar si, daca are destui bani, isi
plateste factura, daca nu, o amana si ramane indatorat la distribuitorul sau.
- fiecare distribuitor isi actualizeaza bugetul adaugand suma primita de la
clientii sai si scazand cheltuielile 
- daca bugetul unui distribuitor devine negativ, acesta da faliment si ii setez
numarul de clienti la 0. Fiecare consumator care avea acest distribuitor
trebuie sa inceapa alt contract in luna urmatoare (am setat luna curenta a
contractului ca fiind ultima luna). 

In fiecare dintre urmatoarele luni: 

-  parcurg lista de schimbari din luna respectiva. Parcurg lista de schimbari
ale distribuitorilor si actualizez costul de infrastructura oriunde este cazul.
Adaug noii consumatori in lista din input.
- pentru fiecare distribuitor nefalimentat, recalculez pretul contractului. 
- gasesc distribuitorul care ofera cel mai mic pret al contractului-
"bestDistributor"
-  fiecare consumator nou-introdus incepe un contract cu "bestDistributor" (se
actualizeaza numarul de clienti ai acestui distribuitor). Isi incaseaza venitul
lunar si isi plateste factura doar daca are suficienti bani. Altfel,
consumatorul ramane indatorat la distribuitorul sau. 
-  pentru fiecare consumator nefalimentat si care nu este introdus in luna
curenta: 
    - daca i se termina contractul, acesta isi primeste venitul lunar si incepe
    un alt contract. 
        - daca are o datorie din ultima luna a contractului anterior si la
        momentul curent are bani doar pentru factura veche plus penalitati, le
        plateste pe acestea si ramane indatorat cu noua factura
        - daca are o datorie din ultima luna a contractului anterior si la
        momentul curent are bani si pentru factura noua, si pentru factura
        veche plus penalitati, plateste tot si nu mai este indatorat
        - daca are o datorie din ultima luna a contractului anterior si la
        momentul curent nu are bani nici macar pentru factura veche plus
        penalitati, consumatorul intra in faliment
        - daca nu este dator din ultima luna a contractului anterior, dar nu
        are bani sa plateasca factura curenta, consumatorul ramane indatorat
        - daca nu este dator din ultima luna a contractului anterior si are
        bani sa plateasca factura curenta, o plateste. 
    - daca inca nu i se termina contractul, consumatorul isi primeste venitul
    lunar si are acelasi pret al contractului din luna anterioara. 
        - daca este dator din luna trecuta si nu are bani sa plateasca factura
        noua plus factura veche cu penalitati, da faliment
        - daca este dator din luna trecuta si are bani sa plateasca factura
        noua plus datoria, plateste si nu mai este dator
        - daca nu este dator din luna anterioara si nu are bani sa plateasca
        factura curenta, amana factura si ramane dator
        - daca nu este dator din luna anterioara si are bani sa isi plateasca
        factura, o plateste 
- fiecare distribuitor nefalimentat primeste banii de la clientii sai si isi
plateste cheltuielile. daca bugetul sau devine negativ, acesta intra in
faliment si toti consumatorii care au acest distribuitor incep alt contract in
luna urmatoare. 
- creez o lista de observatori in care introduc toti distribuitorii care au cel
putin un producator in lista de schimbari ale lunii curente 
- se fac actualizarile lunii pentru producatori
- fiecare dintre distribuitorii-observatori isi aplica strategia si isi
reseteaza lista de producatori. 
- pentru fiecare distribuitor nefalimentat, recalculez costul de productie si
profitul.
- la finalul lunii, scot consumatorii falimentati din listele de clienti ale
distribuitorilor.
- creez obiectul de tip "output" si il scriu in fisierul de iesire.    
        
### Elemente de design OOP

Am folosit Mostenirea pentru a aplica Design Pattern-ul Observer. Clasa mea
"Observable" mosteneste java.util.Observable. 
Am suprascris metode precum "toString" sau "update". Am folosit functii Lambda
pentru a crea comparatori in clasa Producer. Am folosit Polimorfismul
implementand metoda "chooseProducers" in trei moduri posibile, pentru fiecare
strategie intr-un alt fel. Am utilizat Abstractizarea creand interfata
"StrategyForChoosingProducers". Am utilizat Incapsularea creand campuri de tip
"private" si punand utilizatorului la dispozitie Getteri si Setteri pentru a le
putea accesa/schimba.  

### Design patterns

   Am folosit 3 tipuri de Design Patterns: 
   - Factory: clasa ObjectFactory contine un enum cu obiecte pe care le pot
   crea folosind metoda createObject. Folosesc aceasta metoda la citirea
   datelor din fisierul de input- creez obiecte folosind Factory si le populez 
   cu proprietatile care apar in fisier. 
   -  Strategy: clasele GreenStrategy, PriceStrategy si QuantityStrategy
   implementeaza interfata StrategyForChoosingProducers, fiecare clasa avand
   propria versiune de "chooseProducers". In Main actualizez lista de
   producatori a fiecarui distribuitor dand ca parametru metodei
   "chooseProducers" un tip de strategie, in functie de parametrii
   distribuitorului.  
   - Observer: producatorii joaca rolul de "Subiect", distribuitorii care
   trebuie sa isi aplice din nou strategia sunt "Observers". In Main se
   apeleaza metoda "setProducersChanges", care face actualizarile lunare
   referitoare la producatori si apoi notifica observatorii. Se apeleaza
   pentru fiecare observator metoda "update", in care un distribuitor isi
   aplica strategia, actualizandu-si astfel lista de producatori.     

### Dificultati intalnite, limitari, probleme

   Eroare Checkstyle de tipul " '4' is a magic number": pentru rezolvare am
declarat valorile constante ca si "static final" la inceputul clasei. 
Eroare Checkstyle de tipul "Class 'X' looks like designed for extension (can be 
subclassed), but the method 'Y' does not have javadoc that explains how to do
that safely": am declarat clasa ca fiind "final".
Test "complex_5" failed: distribuitorii nu isi alegeau producatorii in ordinea
ID-urilor, ci in ordine inversa. Pt. rezolvare, am inversat ordinea
observatorilor in lista. 
Problema: distribuitori care au acelasi pret al contractului. Rezolvare: 
consumatorii aleg distribuitorul cu ID-ul mai mic.   
Problema: ordine gresita a campurilor la scrierea in fisierul de output.
Rezolvare: Am asezat in ordine getter-ii si setter-ii. 
Feedback: o tema interesanta, destul de usor de facut debug, care m-a stimulat
sa invat mai bine design pattern-urile. Doar ca enuntul la prima etapa putea
fi, in opinia mea, mai detaliat. 
