# Documentación: Uso de Programación Imperativa y Estructurada en el Proyecto

Aunque Java es un lenguaje predominantemente Orientado a Objetos, el **cómo** se ejecuta la lógica interna dentro de los métodos (funciones) está profundamente arraigado en la **Programación Imperativa y Estructurada**. 

El paradigma imperativo se basa en decirle a la máquina exactamente *paso a paso* qué hacer, modificando el "estado" del programa a través de secuencias de comandos. Por su parte, la programación estructurada es una rama del paradigma imperativo que mejora la claridad del código usando tres estructuras lógicas de control: Secuencia, Selección (condicionales) e Iteración (bucles).

A continuación se detalla con ejemplos cómo se aplican en el Sistema de Gestión de Hotel.

---

## 1. Instrucciones de Control Secuenciales y Mutación de Estado

En el paradigma imperativo, las variables cambian de estado constantemente mediante asignaciones secuenciales (línea por línea). 

*   **Implementación:** Al calcular totales o procesar formularios, se van creando variables cuyo valor es modificado secuencialmente paso a paso.
*   **Archivo de evidencia:** `src/main/java/vistas/checkin.java` (Ejemplo lógico)
    ```java
    // Secuencia paso a paso que muta el estado (calculando el total):
    double precioHabitacion = obtenerPrecio(habitacionSeleccionada);
    int dias = Integer.parseInt(txtDias.getText());
    
    // Mutación del estado:
    double total = precioHabitacion * dias; 
    txtTotalPagar.setText(String.valueOf(total));
    ```

---

## 2. Instrucciones Condicionales (Selección)

El flujo del programa se ramifica utilizando sentencias lógicas de decisión (`if`, `else`, `switch`). Esto es puro control de flujo estructurado.

*   **Implementación con `if`:** Extensamente usado para validaciones de seguridad o campos vacíos.
    *   Ejemplo: Validar si un usuario existe antes de hacer login, o si un archivo `.txt` ya fue creado.
*   **Implementación con `switch`:** Utilizado para evaluar múltiples casos de una misma variable de manera limpia.
*   **Archivo de evidencia:** `src/main/java/vistas/habitaciones.java` (Línea 247)
    ```java
    // Control de flujo para decidir de qué color pintar un botón según su estado
    switch (estado) {
        case "LIBRE": 
            btnHabitacion.setBackground(COLOR_LIBRE); 
            break;
        case "OCUPADA": 
            btnHabitacion.setBackground(COLOR_OCUPADA); 
            break;
        case "RESERVADA": 
            btnHabitacion.setBackground(COLOR_RESERVADA); 
            break;
        case "MANTENIMIENTO": 
            btnHabitacion.setBackground(COLOR_MANTENIMIENTO); 
            break;
    }
    ```

---

## 3. Estructuras de Control Repetitivas (Bucles)

La iteración es el corazón del paradigma imperativo/estructurado para procesar colecciones o tareas repetitivas en lugar de usar recursividad.

*   **Bucles `while` (Para Entradas/Salidas e I/O):**
    Utilizado ampliamente en el paquete `persistencia` para leer líneas de los archivos `.txt` hasta que se agoten.
    *   **Archivo de evidencia:** `src/main/java/persistencia/ConsumoArchivo.java` (Línea 25)
        ```java
        // Repite la instrucción mientras la línea no esté vacía
        while ((linea = br.readLine()) != null) {
            // Procesamiento de la línea
        }
        ```
*   **Bucles `for` (Para Iteración definida):**
    Usados en las vistas y lógica de persistencia para recorrer listas, sumar acumulados o llenar tablas (`JTable`).
    *   **Archivo de evidencia:** `src/main/java/vistas/habitaciones.java` (Línea 166-169)
        ```java
        // Bucle for clásico (imperativo) con mutación de un contador (i++)
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 5; j++) {
                // Crear 5 habitaciones para cada uno de los 3 pisos
            }
        }
        ```

---

## 4. Estructuras de Datos Estáticas (Arreglos Bidimensionales)

En lugar de delegar todo a objetos complejos, el paradigma estructurado a menudo usa arreglos primitivos para mover información simple a través de la memoria.

*   **Implementación:** Conversión de objetos a matrices nativas.
*   **Archivo de evidencia:** `src/main/java/vistas/habitaciones.java` (Línea 208)
    ```java
    // Matriz (Arreglo Bidimensional) que recibe directamente filas y columnas primitivas de texto
    private JPanel crearSeccionPiso(String tituloPiso, String[][] habitaciones) {
        // Uso de un For-Each estructurado para leer el arreglo
        for (String[] hab : habitaciones) {
            String numero = hab[0]; // Acceso imperativo por índice
            String estado = hab[1];
        }
    }
    ```

---

## Conclusión

Mientras que la **Programación Orientada a Objetos** diseña *quién* interactúa (las entidades del hotel) y cómo se agrupan en el proyecto, la **Programación Imperativa y Estructurada** dicta el *cómo* funcionan por dentro esas entidades. Sin la POO, tendrías un código desordenado; pero sin la programación imperativa y sus bucles (`while`, `for`) e `if`s, tus métodos estarían vacíos y el software no ejecutaría ningún paso lógico.
