# 📚 Literalura - Catálogo de Libros

Aplicación de consola desarrollada en Java con Spring Boot que permite gestionar un catálogo de libros utilizando la API de Gutendex y una base de datos PostgreSQL.

## 🎯 Características

### Funcionalidades Principales

- **Búsqueda de libros**: Busca libros por título en la API de Gutendex y los guarda en la base de datos
- **Gestión de catálogo**: Lista todos los libros y autores registrados
- **Filtros avanzados**:
  - Autores vivos en un año específico
  - Libros por idioma (Español, Inglés, Francés, Portugués)
  - Autores por rango de años de nacimiento

### Funcionalidades Avanzadas

- **Estadísticas**: Visualiza estadísticas de descargas (total, promedio, máximo, mínimo)
- **Top 10**: Muestra los 10 libros más descargados
- **Búsqueda de autores**: Busca autores por nombre (búsqueda parcial)
- **Filtro por años**: Lista autores nacidos en un rango de años específico

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 4.0.2**
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL** - Base de datos relacional
- **Maven** - Gestión de dependencias
- **Gutendex API** - Fuente de datos de libros

## 📋 Requisitos Previos

- Java JDK 17 o superior
- PostgreSQL 12 o superior
- Maven 3.6 o superior

## ⚙️ Configuración

### 1. Base de Datos

Crea una base de datos PostgreSQL:

```sql
CREATE DATABASE literalura;
```

### 2. Configuración de la Aplicación

Edita el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### 3. Instalación

Clona el repositorio:

```bash
git clone https://github.com/tu-usuario/literalura.git
cd literalura
```

## 🚀 Ejecución

### Usando Maven Wrapper (recomendado)

**Windows:**

```powershell
.\mvnw spring-boot:run
```

**Linux/Mac:**

```bash
./mvnw spring-boot:run
```

### Usando Maven instalado

```bash
mvn spring-boot:run
```

## 📖 Uso

Al ejecutar la aplicación, verás el siguiente menú:

```
╔════════════════════════════════════════╗
║         LITERALURA - CATALOGO          ║
╠════════════════════════════════════════╣
║ 1 - Buscar libro por titulo           ║
║ 2 - Listar libros registrados          ║
║ 3 - Listar autores registrados         ║
║ 4 - Listar autores vivos en un anio   ║
║ 5 - Listar libros por idioma           ║
║ 6 - Estadisticas de libros             ║
║ 7 - Top 10 libros mas descargados      ║
║ 8 - Buscar autor por nombre            ║
║ 9 - Autores por rango de anios         ║
║ 0 - Salir                              ║
╚════════════════════════════════════════╝
```

### Ejemplos de Uso

**Buscar un libro:**

1. Selecciona opción `1`
2. Ingresa el título (ej: "Don Quijote")
3. El libro se guardará automáticamente en la base de datos

**Ver estadísticas:**

1. Selecciona opción `6`
2. Visualiza el total de libros, descargas totales, promedio, máximo y mínimo

**Buscar autores:**

1. Selecciona opción `8`
2. Ingresa parte del nombre (ej: "Cervantes")
3. Verás todos los autores que coincidan con la búsqueda

## 🗂️ Estructura del Proyecto

```
literalura/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/alura/literalura/
│   │   │       ├── dto/              # Data Transfer Objects
│   │   │       ├── model/            # Entidades JPA
│   │   │       ├── repository/       # Repositorios Spring Data
│   │   │       ├── service/          # Lógica de negocio
│   │   │       ├── principal/        # Interfaz de consola
│   │   │       └── LiteraluraApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── .gitignore
├── pom.xml
└── README.md
```

## 📊 Modelo de Datos

### Entidad Book

- `id`: Long (PK)
- `title`: String
- `author`: Author (FK)
- `language`: String
- `downloads`: Double

### Entidad Author

- `id`: Long (PK)
- `name`: String
- `birthYear`: Integer
- `deathYear`: Integer

## 🔗 API Externa

Este proyecto utiliza la [Gutendex API](https://gutendex.com/), una API gratuita que proporciona acceso al catálogo del Proyecto Gutenberg.

**Endpoint utilizado:**

```
https://gutendex.com/books/?search={titulo}
```

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request
