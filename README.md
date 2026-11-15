# TorneoTotal

Aplicación de escritorio en Java para gestionar torneos deportivos, individuales y por equipos: registro de participantes, formación de grupos y llaves, registro de resultados y tablas de posiciones.

Proyecto final del curso **Desarrollo de Software Orientado a Objetos**, Escuela Profesional de Ingeniería de Sistemas (EPIS), Universidad Nacional de San Agustín de Arequipa (UNSA), 2026.

## Equipo de desarrollo

**Nombre del equipo:** Los Sin Tokens

| N.º | Apellidos y nombres | Rol / módulo |
|-----|---------------------|--------------|
| 1 | Mamáni Paucar Francisco Joan  (líder) | Torneo de Tenis de Mesa |
| 2 | Mendoza Florez Yael Jesus | Torneo de fútbol |
| 3 | Bobbio Pérez Kiara Antonella|Torneo de Voley |
| 4 | Sacari Pacoambia Delisse Anamilé | Torno de Básquet|

## Funcionalidades

Estado: en desarrollo. Esta lista se actualiza al terminar cada Sprint.

- [X] Creación de torneos individuales y por equipos
- [ ] Registro de jugadores y equipos
- [ ] Formación de grupos y llaves
- [ ] Registro de marcadores y resultados
- [ ] Tabla de posiciones y ranking
- [ ] Persistencia en base de datos (MySQL, JDBC)

### Deportes contemplados

| Tipo | Deporte |
|------|---------|
| Individual | Tenis de mesa |
| Por equipos | Fútbol, básquet, vóley |

## Tecnologías

| Componente | Tecnología |
|------------|------------|
| Lenguaje | Java |
| Interfaz gráfica | Java Swing |
| Apariencia (Look and Feel) | FlatLaf |
| Base de datos | MySQL |
| Persistencia | JDBC |
| Arquitectura | MVC (Modelo, Vista, Controlador) |
| Diseño | Programación Orientada a Objetos y UML |
| Control de versiones | Git y GitHub |
| Metodología | Scrum |

## Estructura del proyecto

```
TorneoTotal/
├── src/
│   ├── model/        Clases del dominio (torneos, equipos, jugadores, partidos)
│   ├── view/         Ventanas y paneles Swing
│   ├── controller/   Controladores (TorneoController, ...)
│   └── dao/          Acceso a datos (ConexionBD, clases DAO)
├── lib/              Librerías externas (FlatLaf, conector MySQL)
├── sql/              Script de creación de la base de datos
├── docs/             Diagramas UML, informes semanales y capturas
├── .gitignore
└── README.md
```

## Requisitos

- JDK 
- MySQL 
- IDE: Eclipse, VSCODE
- Librerías en `lib/`: `flatlaf-.jar` y `mysql-connector-j-.jar`

## Cómo ejecutar

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/kyssifur/Torneo_Total
   cd TorneoTotal
   ```
2. Crear la base de datos ejecutando el script en MySQL.
3. Configurar la conexión (usuario, contraseña y nombre de la base de datos) en [archivo de configuración, completar].
   No subir credenciales reales al repositorio.
4. Abrir el proyecto en el IDE y agregar a las librerías los `.jar` de la carpeta `lib/`.
5. Ejecutar la clase principal .

## Flujo de trabajo con Git

- La rama `main` siempre debe contener una versión que compile y se pueda ejecutar.
- Cada módulo o historia de usuario se desarrolla en su propia rama: `feature/futbol`, `feature/basquet`, `feature/login`, etc.
- Los cambios llegan a `main` mediante Pull Request revisado por otro integrante.
- Commits pequeños, con mensajes claros (por ejemplo: `Agrega clase PartidoFutbol`).
- Antes de empezar a trabajar: `git pull origin main`.

## Organización Scrum

- Sprints: 1
- Presentación de avance: semanal
- Product Backlog y tablero: GitHub Projects
- Informes semanales: carpeta `docs/informes/`

## Documentación

- Diagrama de clases: `docs/`
- Informes semanales de avance: `docs/informes/`

## Licencia

Proyecto académico sin fines comerciales.