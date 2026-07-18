# Documentación: Uso de Programación Funcional en el Proyecto

Aunque históricamente Java no era un lenguaje funcional, a partir de la versión 8 integró fuertes características de este paradigma (como Lambdas y Streams). La **Programación Funcional** trata a la computación como la evaluación de funciones matemáticas y evita cambiar el estado y mutar datos. Es decir, se prioriza el *qué* hacer por encima del *cómo* hacerlo (enfoque declarativo).

A continuación se detalla con ejemplos reales cómo el Sistema de Gestión de Hotel aplica estos conceptos, cubriendo exactamente los temas requeridos en el sílabo.

---

## 1. Funciones Anónimas (Expresiones Lambda)

Las funciones anónimas son funciones que no tienen un nombre declarado y se definen "al vuelo" directamente donde se necesitan. En Java, se escriben utilizando la sintaxis de flecha `->`.

*   **Implementación:** En casi todas las interfaces gráficas (vistas), los botones utilizan funciones anónimas para reaccionar a los clics del usuario sin necesidad de crear clases separadas que implementen `ActionListener`.
*   **Archivo de evidencia:** `src/main/java/vistas/Snack.java` (Línea 123)
    ```java
    // La variable 'e' entra a la función anónima (Lambda) que define el comportamiento del botón
    btnAgregar.addActionListener(e -> { 
        // Lógica funcional a ejecutar cuando se hace clic
    });
    ```

---

## 2. Funciones como Parámetros (First-Class Functions)

En programación funcional, las funciones pueden ser tratadas como cualquier otra variable, permitiendo que una función sea enviada como parámetro (argumento) a otra función.

*   **Implementación:** En la lógica de base de datos plana (archivos txt), se pasa una función de evaluación (predicado lógico) como parámetro al método `removeIf()` para que decida qué elementos eliminar de la lista.
*   **Archivo de evidencia:** `src/main/java/persistencia/ClienteArchivo.java` (Línea 90)
    ```java
    // "Aplicamos paradigma funcional(Lambda) uwu"
    // Se está enviando la FUNCIÓN "c -> c.getIdCliente() == idBuscado" como parámetro al método removeIf
    lista.removeIf(c -> c.getIdCliente() == idBuscado);
    ```

---

## 3. Definición Declarativa y Funciones de Orden Superior

Las **Funciones de Orden Superior** son aquellas que reciben otras funciones como argumento o devuelven una función. Además, el estilo declarativo permite encadenar estas funciones para transformar datos sin tener que escribir bucles imperativos manuales.

*   **Implementación (El API de Streams):** Al momento de buscar un registro específico (por ejemplo, buscar un usuario por su ID), en lugar de usar un bucle `for` y un `if` (que sería imperativo), se usa un `Stream` para transformar la lista en un flujo de datos y se usan funciones de orden superior como `.filter()`.
*   **Archivo de evidencia:** `src/main/java/persistencia/ConsumoArchivo.java` (Línea 81)
    ```java
    // Enfoque Declarativo y Funcional:
    return listar().stream() // 1. Crea un flujo de datos inmutable
            .filter(c -> c.getIdConsumo() == idBusqueda) // 2. Función de Orden Superior que filtra
            .findFirst() // 3. Toma el primero que encuentre
            .orElse(null); // 4. Retorna nulo si no hay coincidencias
    ```
    *Diferencia clave:* No le dices a la computadora *cómo* hacer el bucle, solo le declaras *qué* quieres (filtrar, buscar el primero).

---

## Conclusión

La adopción de la **Programación Funcional** en este proyecto es puntual pero altamente efectiva. Su uso principal recae en:
1.  **Limpiar la Interfaz Gráfica:** Reduciendo cientos de líneas de código "boilerplate" en los ActionListeners mediante **Lambdas**.
2.  **Optimizar Colecciones:** Reemplazando los verbosos y propensos a error bucles `for` por **Streams** elegantes, declarativos y de lectura fluida para filtrar y eliminar elementos de las listas (`ArrayList`) en la capa de persistencia.
