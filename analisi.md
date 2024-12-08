
---

### **1. Per què l’esquema de backtracking és aplicable per a resoldre aquest enunciat?**

L'esquema de backtracking és aplicable perquè el problema implica generar i provar diferents combinacions d'ubicacions
per col·locar les paraules a la reixa, tot respectant les restriccions donades (mides, direcció, superposicions, etc.).
Aquest tipus de problema és de cerca sistemàtica, on cal explorar l'espai de possibles solucions i retrocedir (
backtrack) si una combinació no és viable.

---

### **2. Quina pregunta et fas en cada nivell de l’arbre? Quines són les possibles respostes d’aquesta pregunta (domini)?**

- **Pregunta**: Quina paraula es pot col·locar en la ubicació actual?
- **Respostes**:
    - Una paraula del conjunt disponible que encaixi segons les restriccions.
    - Cap, si no és possible col·locar cap paraula en aquesta posició.

El domini de possibles respostes és el conjunt de paraules disponibles per assignar.

---

### **3. Quin és el criteri per determinar si una decisió és o no acceptable?**

Una decisió és acceptable si:

- La paraula cap en la ubicació escollida segons la longitud i la direcció.
- No entra en conflicte amb paraules ja col·locades (no es sobreescriuen caràcters incompatibles).

---

### **4. Quin és el criteri per determinar si un conjunt de decisions és o no completable?**

Un conjunt de decisions és completable si:

- Queden suficients ubicacions disponibles per col·locar les paraules restants.
- Cap paraula restant està restringida per ubicacions incompatibles.

---

### **5. Quin és el criteri per determinar si un conjunt de decisions són o no solució?**

Un conjunt de decisions és solució si:

- Totes les paraules s'han col·locat a la reixa completament i sense conflictes.
- No hi ha cap paraula restant al conjunt inicial.

---

### **6. Dibuixeu l’espai de cerca del problema, indica quina és l’alçada i l’amplada i si són valors exactes o màxims. Usaràs marcatge?**

- **Espai de cerca**: És un arbre on cada node representa una paraula col·locada a la reixa.
- **Alçada**: És igual al nombre total de paraules a col·locar. Aquest valor és exacte.
- **Amplada**: És el nombre màxim de ubicacions possibles per col·locar una paraula en cada pas. Aquest valor és màxim,
  ja que depèn de les restriccions.
- **Marcatge**: Sí, utilitzaré marcatge per evitar reutilitzar paraules o ubicacions ja assignades.

---

### **7. Quin és el criteri per determinar si una solució és o no millor a una altra ja trobada prèviament?**

Una solució és millor si la suma dels valors ASCII dels caràcters de totes les paraules és superior. Això es determina
amb la funció objectiu definida al problema.

---