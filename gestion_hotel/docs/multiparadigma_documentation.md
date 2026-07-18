# Documentación: Uso de Programación Multiparadigma en el Proyecto

La **Programación Multiparadigma** no es un paradigma con reglas propias, sino la capacidad de un lenguaje de programación (y del desarrollador) de combinar dos o más paradigmas diferentes para aprovechar las fortalezas de cada uno, compensando sus debilidades. Java, especialmente desde su versión 8, es un lenguaje fuertemente multiparadigma.

El Sistema de Gestión de Hotel es un excelente ejemplo empírico de este enfoque. A continuación, se detalla rigurosamente cómo el proyecto orquesta y combina los distintos paradigmas que exige la **Unidad 3 del Sílabo**.

---

## 1. La Sinergia de los Paradigmas (Cómo se complementan)

En lugar de forzar todo el proyecto bajo una sola filosofía, el código ha sido diseñado delegando responsabilidades específicas a cada paradigma.

### A. La Arquitectura Global (Orientación a Objetos)
*   **Rol:** Define la estructura, las piezas del tablero y los contratos.
*   **Aplicación:** Se encapsulan entidades complejas (`Habitacion`, `Cliente`) y se dividen las responsabilidades en capas (`vistas`, `modelo`, `persistencia`) usando polimorfismo (interfaz `CRUD<T>`) y herencia (`Cliente extends Persona`). 
*   *Sin POO, el proyecto sería un solo archivo inmenso, caótico e inmanejable.*

### B. El Motor Interno y Algoritmia (Imperativo / Estructurado)
*   **Rol:** Define las matemáticas, la mutación de estados y la I/O (Entrada/Salida).
*   **Aplicación:** Dentro de los métodos de esas clases orientadas a objetos, el código se vuelve imperativo. Necesitas leer un archivo de texto línea por línea (`while`), necesitas dibujar 3 pisos con 5 habitaciones (`for` anidados), y necesitas calcular el costo total de estadía reasignando variables paso a paso.
*   *Sin la Programación Imperativa, las clases estarían vacías y no habría flujo de ejecución paso a paso.*

### C. El Procesamiento Limpio y Reactividad (Funcional)
*   **Rol:** Procesar datos y responder a eventos sin efectos secundarios y de forma declarativa.
*   **Aplicación:** Cuando las listas de objetos se vuelven muy grandes, en lugar de mutar un arreglo manualmente (imperativo), se usan Streams para filtrar datos inmutables (`.stream().filter(...)`). Igualmente, se usan Lambdas para inyectar "comportamientos" limpios en la UI (`btn.addActionListener(e -> { ... })`).
*   *Sin la Programación Funcional, la interfaz gráfica estaría inundada de clases anónimas repetitivas y el filtrado de listas requeriría decenas de líneas de código extra.*

---

## 2. Ejemplo Integral: Donde los 3 Paradigmas Convergen

Para demostrar el enfoque multiparadigma de manera contundente, observemos cómo convergen los tres paradigmas simultáneamente en los archivos de persistencia, específicamente en las operaciones de búsqueda y eliminación.

**Archivo de evidencia:** `src/main/java/persistencia/ConsumoArchivo.java`

```java
// 1. PARADIGMA ORIENTADO A OBJETOS
// Uso de clases (ConsumoArchivo), Herencia/Implementación (implements CRUD<Consumo>), 
// y encapsulamiento (métodos públicos).
public class ConsumoArchivo implements CRUD<Consumo> {
    
    // ...
    
    // 2. PARADIGMA IMPERATIVO / ESTRUCTURADO
    // Uso del método listar() que internamente muta un ArrayList y lee un archivo txt
    // mediante un bucle 'while' estructurado paso a paso.
    public Consumo buscar(int idBusqueda) {
        
        // 3. PARADIGMA FUNCIONAL
        // Aplicación del API de Streams y Lambdas (Declarativo)
        // No se dice *cómo* buscar con bucles, se le dice *qué* buscar.
        return listar().stream()
                .filter(c -> c.getIdConsumo() == idBusqueda) // Expresión Lambda
                .findFirst()
                .orElse(null);
    }
}
```

---

## 3. Relación directa con el Sílabo (Unidad 3)

El código desarrollado cumple a cabalidad con la progresión exigida en el sílabo de la Unidad 3:

*   **Sesiones 11, 12 y 13 (Fundamentos, Manipulación y Control):** Ampliamente cubierto mediante la asignación de variables, ingreso de datos (vía `JTextFields` en las `vistas`), uso de cadenas de texto (`String`), instrucciones condicionales (`if/switch`) y bucles anidados.
*   **Sesión 14 (Estructuras de Datos y Modularidad):** Evidenciado en el uso masivo de `ArrayList<T>` para el manejo de colecciones y matrices `[][]`, así como la modularización en paquetes y métodos.
*   **Sesión 15 (Orientación a Objetos):** Pilar absoluto del sistema a través de las clases en el paquete `modelo`.

## Conclusión

El proyecto es un sistema maduro **Multiparadigma**. No se encasilla en una sola herramienta mental. Usa objetos para modelar la realidad del hotel (POO), usa bucles y condicionales para las transacciones paso a paso (Imperativo) y usa flujos inmutables y lambdas para extraer datos y manejar eventos con elegancia (Funcional).
