# Documentación: Uso de Arreglos Bidimensionales (Matrices) en el Proyecto

Dentro del sílabo, específicamente en la **Sesión 13: Estructuras de Control Avanzadas**, se aborda el uso de Arreglos (Arrays). En la programación estructurada y orientada a objetos, un arreglo bidimensional es una matriz matemática (filas y columnas) utilizada para almacenar datos tabulares de forma estática en memoria.

En el Sistema de Gestión de Hotel, se hace un uso muy específico de un arreglo bidimensional para resolver un problema de renderizado en la interfaz gráfica (UI).

---

## 1. Ubicación y Declaración

La matriz se crea y procesa exclusivamente en la vista encargada de mostrar el mapa interactivo del hotel.

*   **Archivo de evidencia:** `src/main/java/vistas/habitaciones.java`
*   **Firma del método (Línea 208):**
    ```java
    // Se declara el parámetro 'habitaciones' como un arreglo bidimensional de Cadenas de Texto (String)
    private JPanel crearSeccionPiso(String tituloPiso, String[][] habitaciones) { ... }
    ```

## 2. Estructura Lógica de la Matriz

El arreglo `String[][] habitaciones` funciona como una tabla donde:
*   **Cada fila (`habitaciones[i]`)** representa una habitación individual.
*   **Cada columna (índices `0` y `1`)** contiene atributos primitivos de esa habitación.

De manera esquemática, la memoria se ve así:
| Fila (Índice) | Columna 0 (Número) | Columna 1 (Estado) |
| :--- | :--- | :--- |
| `[0]` | `"101"` | `"LIBRE"` |
| `[1]` | `"102"` | `"OCUPADA"` |
| `[2]` | `"103"` | `"MANTENIMIENTO"` |

---

## 3. Flujo de Datos (Cómo se llena y cómo se lee)

### Fase A: Llenado de la Matriz (Conversión)
Originalmente, las habitaciones se leen de la base de datos (archivos `.txt`) como objetos `Habitacion`. Para pasarlas al dibujado de la UI, se extraen sus datos a listas dinámicas (`ArrayList<String[]>`). 
Finalmente, estas listas se "aplastan" y se convierten en una **matriz estática pura** usando el método `.toArray()` (Líneas 193-199):
```java
// Se convierte la lista dinámica del piso 1 a una matriz String[][] pura
mapaPanel.add(crearSeccionPiso("PISO 1 (Básicas)", piso1.toArray(new String[0][0])));
```

### Fase B: Lectura de la Matriz (Renderizado UI)
Una vez que la matriz ingresa al método `crearSeccionPiso()`, se lee utilizando un bucle `for-each` estructurado (Líneas 231-233).
```java
for (String[] hab : habitaciones) {
    // Lectura por índices fijos (Hardcoding)
    String numero = hab[0]; // La columna 0 siempre es el número
    String estado = hab[1]; // La columna 1 siempre es el estado

    // Con estos datos, se crea un JButton dinámico y se pinta de un color
    JButton btnHabitacion = new JButton(numero);
    // ...
}
```

---

## 4. Análisis Arquitectónico y Dependencias

*   **Ventaja Actual:** Es una forma rápida de pasar datos simples a un método sin necesidad de acoplar la interfaz gráfica profundamente a los objetos complejos del modelo. Al ser primitivos (`String`), ocupan muy poca memoria en el renderizado.
*   **Deuda Técnica (Dependencia Crítica):** Depende de "Índices Mágicos" (`0` y `1`). Si el día de mañana se necesita agregar un tercer dato a la matriz (por ejemplo, el Tipo de habitación para mostrar "101 - Suite"), el arreglo bidimensional tendría que crecer (`hab[2]`), y el desarrollador tendría que recordar mentalmente qué significa cada número de índice, lo cual es propenso a errores en sistemas grandes.
