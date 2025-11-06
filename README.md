# 🏎️ Metodologie di Sviluppo Software - Simulatore di Gara F1

Questo progetto è stato sviluppato per l'esame di **Metodologie di Programmazione** del corso di Informatica dell'università di Camerino. L'obiettivo di questo progetto era realizzare un videogioco che simulasse la F1 su carta. 

All'interno del gioco ci sarà un circuito bianco dove le macchine potranno correre, e una parte verde dove non potranno correre e quindi verranno squalificate. 

Il gioco supporta più giocatori umani, che si muoverano tramite il mouse su una griglia tramite un movimento vettoriale, ma si possono anche inserire dei giocatori bot che tramite delle strategie saranno in grado di muoversi sul circuito. 

## 🧠 Analisi, Architettura e Pattern (Il Cuore del Progetto)

### ✨ Caratteristiche Architetturali

* **Architettura Modulare e Flessibile:**
    * La struttura del codice aderisce rigorosamente ai principi **SOLID** (Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion).
    * Questo approccio garantisce un alto livello di **disaccoppiamento** (*low coupling*) tra le componenti, rendendo il codice estremamente manutenibile, testabile e scalabile.
    * L'architettura è stata sviluppata tramite il modello architetturale MVC (Model-View-Controller).

## 🛠️ Tecnologie e Strumenti Utilizzati

Questa sezione descrive la *stack* tecnologica e gli strumenti scelti per la realizzazione del progetto.

| Categoria | Tecnologia | Ruolo nel Progetto |
| :--- | :--- | :--- |
| **Linguaggio** | **Java** (JDK 17+) |
| **Build Tool** | **Apache Maven** |
| **Testing** | **JUnit 5 (Jupiter)** |

## 🚀 Guida all'Avvio (Getting Started)

Segui questi passi per scaricare, compilare ed eseguire il progetto localmente.

### Prerequisiti

Assicurati di avere installati:

* **Java Development Kit (JDK 17 o superiore)**
* **Apache Maven**

### Installazione e Compilazione

1.  Clona la repository:
    ```bash
    git clone [https://github.com/camb3la/MetodologieF1.git](https://github.com/camb3la/MetodologieF1.git)
    cd MetodologieF1
    ```
2.  Compila il progetto e risolvi le dipendenze con Maven:
    ```bash
    mvn clean install
    ```

3. Selezionare l'immagine per il circuito e selezionare il numero dei giocatori.
