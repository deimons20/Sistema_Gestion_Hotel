# Documentación: Uso de Programación Orientada a Objetos (POO) en el Proyecto

El proyecto actual (Sistema de Gestión de Hotel) está fuertemente estructurado sobre el paradigma de **Programación Orientada a Objetos (POO)**. Se ha diseñado separando responsabilidades lógicas en paquetes (`modelo`, `persistencia`, `vistas`, `interfaces`), lo que se asemeja a patrones arquitectónicos como MVC (Modelo-Vista-Controlador) o DAO (Data Access Object).

A continuación, se detalla rigurosamente cómo se aplican los **4 pilares fundamentales de la POO** a lo largo del código fuente, con ejemplos extraídos directamente de los archivos.

---

## 1. Clases y Objetos (Abstracción de Entidades)

El sistema modela entidades del "mundo real" (del dominio del hotel) como **Clases**. Estas clases sirven como moldes para instanciar (crear) **Objetos** en memoria que interactúan a lo largo de la ejecución del programa.

*   **Paquete responsable:** `src/main/java/modelo/`
*   **Ejemplos de Clases:** `Habitacion.java`, `Cliente.java`, `Consumo.java`, `Pago.java`, `Reserva.java`, `Usuario.java`.
*   **Evidencia de instanciación (Objetos):** En las vistas y en la persistencia, constantemente se fabrican objetos utilizando la palabra reservada `new`.
    ```java
    // Ejemplo de creación de un objeto en memoria:
    Habitacion h = new Habitacion();
    h.setNumeroHabitacion(101);
    ```

---

## 2. Encapsulamiento (Ocultamiento de Datos)

El encapsulamiento protege el estado interno de un objeto, evitando que otras partes del código modifiquen variables directamente de forma insegura. El proyecto cumple con esta regla al 100%.

*   **Implementación:** Todos los atributos de las clases del `modelo` están declarados con el modificador de acceso `private`. El acceso y modificación se realiza exclusivamente a través de métodos `public` conocidos como **Getters** y **Setters**.
*   **Archivo de evidencia:** `src/main/java/modelo/Usuario.java` (Líneas 4-7, 19-20)
    ```java
    public class Usuario {
        // Atributos privados (Encapsulados)
        private int idUsuario;
        private String username;
        private String password;
        private String rol;

        // Métodos públicos para interactuar con los datos (Getters/Setters)
        public int getIdUsuario() { return idUsuario; }
        public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
        // ...
    }
    ```

---

## 3. Herencia (Reutilización de Código)

La herencia permite crear nuevas clases basadas en clases existentes, adoptando sus atributos y comportamientos. 

*   **Implementación:** Se ha creado una clase base llamada `Persona` que agrupa las propiedades comunes (como DNI, nombre, apellidos y teléfono). Luego, la clase `Cliente` **hereda** de ella.
*   **Archivo de evidencia:** `src/main/java/modelo/Cliente.java` (Líneas 3, 10-11)
    ```java
    // Cliente hereda todos los atributos de Persona mediante 'extends'
    public class Cliente extends Persona {
        private int idCliente; // Atributo específico de Cliente

        public Cliente(int idCliente, String dni, String nombres, String apellidos, String telefono) {
            // Llama al constructor de la clase padre (Persona) para inicializar los datos comunes
            super(dni, nombres, apellidos, telefono); 
            this.idCliente = idCliente;
        }
    }
    ```

---

## 4. Polimorfismo y Abstracción (Contratos e Interfaces)

El polimorfismo (junto con la abstracción) permite que objetos de diferentes clases sean tratados de manera uniforme si comparten una misma interfaz o superclase, definiendo un "contrato" de qué debe hacer una clase sin importar cómo lo haga internamente.

*   **Implementación:** El proyecto usa la interfaz genérica `CRUD<T>` para forzar a todas las clases del paquete de persistencia (los DAOs) a implementar de forma estandarizada los métodos básicos: Crear, Leer, Actualizar y Eliminar.
*   **Archivo de evidencia:** `src/main/java/persistencia/ClienteArchivo.java` (Línea 8)
    ```java
    import interfaces.CRUD;
    import modelo.Cliente;

    // ClienteArchivo se compromete a implementar las reglas de la interfaz CRUD
    // adaptándolas específicamente al objeto 'Cliente' mediante el uso de Genéricos (<Cliente>).
    public class ClienteArchivo implements CRUD<Cliente> {
        
        // El compilador forzará a esta clase a tener métodos polimórficos como:
        // public void registrar(Cliente c) { ... }
        // public ArrayList<Cliente> listar() { ... }
    }
    ```

---

## Conclusión Arquitectónica

El diseño implementado garantiza un alto nivel de **cohesión** (cada clase tiene una responsabilidad única) y reduce el **acoplamiento**.
*   **Capa Modelo (`modelo/`):** Estructura los datos.
*   **Capa Vista (`vistas/`):** Maneja la interacción con el usuario.
*   **Capa Controlador/Persistencia (`persistencia/`):** Manipula el almacenamiento en archivos `.txt`.

Esta separación de capas bajo la filosofía de POO facilita enormemente el mantenimiento, la escalabilidad y la futura refactorización (por ejemplo, si se deseara cambiar el guardado de archivos de texto `.txt` a una base de datos real como MySQL o PostgreSQL, solo se modificaría el paquete de persistencia sin alterar el resto del sistema).
