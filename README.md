# Proyecto: Bank Manager

## Descripción

Este proyecto implementa el flujo para manejo de cuentas de ahorro y corriente, se implementan diversos patrones de diseño

## Estructura del Proyecto


```plaintext
- bank/
  - README.md              # Documentación del proyecto
  - build.gradle           # Archivo con lasdependecias necesarias
  - src/                   # Código fuente de la aplicación
    - BankApplication      # Archivo principal de la aplicación spring boot
    - utils.py             # Funciones auxiliares y utilitarias
        - config           # Configuración de seguridad de la aplicación
        - command          # Implementación de patron command para las transacciones entre cuentas
        - controller       # Capa de controladores para exponer servicios
        - dto              # Data Transformer object para manejo de peticiones y respuestas
        - event            # Entidad de eventos
        - exception        # Excepciones personalizadas para posibles errores
        - factory          # Patrón de diseño para la creación de cuentas bancarias 
        - mapper           # Capa de mapeo de entidades a dto con MapStruct
        - model            # Capa de entidades
        - observer         # Patrón de diseño observer para las implementaciones de las transacciones
        - repository       # Capa de repository para el manejo de datos
        - service          # Capa de servicio para manejo de lógica del negocio
        - strategy         # Patrón de diseño strategy para las transacciones
    - resources
        - application.properties # archivo de configuracion de conexión a base de datos, documentación swagger
  - tests/                 # Pruebas automatizadas
    
