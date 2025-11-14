
GoPass 
Aplicacion Android con Login, registro y firebase Realtime Database

Descripción
Es una aplicacion Android desarrollada con Jetpack Compose que permite a los usuarios registrarse, iniciar sesion
y visualizar su información desde Firebase Realtime Database.
La aplicacion incluye:

Login: Autenticacion mediante Firebase Authentication.
Registro: Creacion de usuarios en Firebase Auth y almacenamiento de datos como el nombre o el dni en Realtime Database.
Home: Muestra el correo electronico mediante una bienvenida el nombre del usuario y un boton que permite cerrar sesión.

Tecnologias y librerias utilizadas;
- Kotlin
- Jetpack Compose
- Firebase Authentication
- Firebase Realtime Database
- Android Gradle Plugin
- Material3


Configuracion e instalacion:
1. Clonar el proyecto o descargar el zip que no contiene el google-services.json.
2. abrir el proyecto en Android Studio.
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
     - Guarda los datos en Realtime Database.
   - Enlace para volver al Login si ya existe cuenta.

3. Home
   - Muestra el correo del usuario autenticado y su nombre en la cabecera.
   - Boton Cerrar sesión:
     - Finaliza sesión en Firebase Auth.
     - Redirige al Login.


Credenciales de Prueba
Email:
joaquin@gmail.com

Contraseña:
1234567

Puedes registrar nuevos usuarios para probar la app










