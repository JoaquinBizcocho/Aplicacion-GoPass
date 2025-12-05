GoPass
Aplicacion Android con Login, registro y firebase Realtime Database

Descripción
Es una aplicacion Android desarrollada con Jetpack Compose que permite a los usuarios registrarse, iniciar sesion
y visualizar su información desde Firebase Realtime Database.

La aplicacion incluye:

Login: Autenticacion mediante Firebase Authentication.
Registro: Creacion de usuarios en Firebase Auth y almacenamiento de datos como el nombre o el dni en Realtime Database.
Home: Muestra el correo electronico mediante una bienvenida, el nombre del usuario y un boton que permite cerrar sesión.

###  Extensión del Proyecto: Gestión de Elementos y Permisos 

Se ha añadido la funcionalidad de gestión de elementos con un control de acceso basado en roles (`"admin"` o `"usuario"`), tal como se especifica en la actividad.

Modelo de Datos**: Implementación del modelo `Elemento` en la base de datos Realtime Database en el nodo `elementos`.
Permisos de Rol**:
  Todos los usuarios (`"usuario"` y `"admin"`) pueden ver la lista completa de elementos (`ElementListScreen`).
  Solo los administradores (`"admin"`) tienen acceso a las funciones de Crear, Editar y Eliminar elementos, con lógica de acceso condicional en la interfaz y en el código.
Vistas Implementadas**:
  `ElementListScreen`: Muestra la lista de elementos en tiempo real y solo habilita botones de acción (Editar/Eliminar) y el botón flotante (Agregar) para usuarios con rol `admin`.
    `ElementFormScreen`: Permite la creación y edición de un elemento, y su acceso está restringido a usuarios con rol `admin`.

Tecnologias y librerias utilizadas;
- Kotlin
- Jetpack Compose
- Firebase Authentication
- Firebase Realtime Database
- Android Gradle Plugin
- Material3


Configuracion e instalacion:
1. Clonar el proyecto o descargar el zip que no contiene el google-services.json.
2. Abrir el proyecto en Android Studio.
3. Para usar tu propio Firebase:
    - Crear un proyecto en Firebase Console.
    - Activar Authentication > Correo/Contraseña.
    - Activar Realtime Database en modo prueba.
    - Agregar tu `google-services.json` en `app/`.
4. Sicroniza Gradle y ejecuta la app en un dispositivo o emulador.

Uso de la Aplicacion

1. Login
    - Ingresa tu email y contraseña
    - Validaciones
        - campos obligatorios
        - contraseña minima de 6 caracteres
    - Si el inicio de sesion es exitoso te llevara a home
    - Si ocurre un error se muestra un Toast con el mensaje correspondiente

2. Registro

    - Completa los campos: Nombre, DNI, Email, Contraseña.
    - Botón de registro:
        - Crea un usuario en Firebase Auth.
        - Guarda los datos en Realtime Database con el `rol: "usuario"`.
    - Enlace para volver al Login si ya existe cuenta.

3. Home
    - Muestra el correo del usuario autenticado y su nombre en la cabecera.
    - Muestra el rol actual del usuario.
    - Botón "Ver elementos compartidos": Navega a la lista principal de elementos.
    - Boton Cerrar sesión:
        - Finaliza sesión en Firebase Auth.
        - Redirige al Login.


Credenciales de Prueba
Email:
admin@gmail.com --- admin
a@gmail.com ----- usuario

Contraseña:
123456

Puedes registrar nuevos usuarios para probar la app.

Para probar la funcionalidad de Administrador, debes editar manualmente el nodo del usuario en Firebase Realtime Database y cambiar el campo `rol` a `"admin"`.

